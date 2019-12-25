package getools.scripts;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import vdaoengine.utils.Utils;

public class SequenceSearch {
	
	public static Sequence guessSequence = null;
	public static int guessStart = 0;
	
	public class Sequence{
		public String seqName = null;
		public char cseq[] = null;
		
		public void setName(String name){
			seqName = name;
		}
		public void setSeqString(String s){
			s = s.toLowerCase();
			cseq = s.toCharArray();
		}
		
		public String toString(){
			return (new String(cseq));
		}
		
		public int findExact(String s, int startFrom){
			int k=-1;
			char c[] = s.toCharArray();
			for(int i=startFrom;i<cseq.length-c.length-1;i++){
				if(compareExact(c,cseq,i)){
					k = i+1;
					break;
				}
			}
			return k;
		}
		
		public int findWithErrors(String s, int maxNumErrors){
			int k=-1;
			char c[] = s.toCharArray();
			for(int i=0;i<cseq.length-c.length-1;i++){
				if(compareWithErrors(c,cseq,i,maxNumErrors)){
					k = i+1;
					break;
				}
			}
			return k;
		}
		
		public boolean compareExact(char c1[], char c2[], int startWith){
			boolean res = true;
			for(int i=0;i<c1.length;i++){
				if(c1[i]!=c2[startWith+i]){ res = false; break;}
			}
			return res;
		}
		
		public boolean compareWithErrors(char c1[], char c2[], int startWith, int maxNumErrors){
			boolean res = true;
			int count = 0;
			for(int i=0;i<c1.length;i++){
				if(c1[i]!=c2[startWith+i]){ count++; if(count>maxNumErrors){ res=false; break;} }
			}
			return res;
		}
		
		
		}

	public static void main(String[] args) {
		try{
			
			Vector<Sequence> base = loadBase("C:/Datas/Ivashkine/ZebraFish/genome/danRer7.fa","chr");
			//for(Sequence s: base)
			//	System.out.println(">"+s.seqName+"\n"+s.toString()+"\n");
			//Vector<Sequence> query = readFromFasta("C:/Datas/Ivashkine/ZebraFish/genome/test1.fa",null);
			Vector<Sequence> query = readFromFasta("C:/Datas/Ivashkine/ZebraFish/genome/test.fa", null);
			for(Sequence s: query){
				Date tm = new Date();
				HashMap<Sequence, Integer> ps = searchExact(base, s.toString());
				Set<Sequence> found = ps.keySet();
				for(Sequence ss: found)
					System.out.println("Found exact "+s.seqName+" in "+ss.seqName+" @"+ps.get(ss));
				System.out.println((new Date()).getTime()-tm.getTime());
				/*ps = searchWithErrors(base, s.toString(), 2);
				found = ps.keySet();
				for(Sequence ss: found)
					System.out.println("Found approx "+s.seqName+" in "+ss.seqName+" @"+ps.get(ss));*/
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static Vector<Sequence> readFromFasta(String fn, String substring) throws Exception{

		Vector<Sequence> d = new Vector<Sequence>();
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		StringBuffer seq = null;
		int count = 0;
		Sequence ps = null;
		while((s=lr.readLine())!=null){
			if(s.trim().startsWith(">")){
				if(ps!=null) {
					ps.setSeqString(seq.toString());
					//if((count>=start)&&((count<=end)||(end==-1)))
					if((substring==null)||(ps.seqName.contains(substring)))
						d.addElement(ps);
				}
				ps = (new SequenceSearch()).new Sequence();
				s = s.trim();
				ps.setName(s.substring(1,s.length()).trim());
				count++;
				seq = new StringBuffer();
			}
			else
			{
				if(seq!=null) seq.append(s.trim());
			}
		}
		ps.setSeqString(seq.toString());
		//if((count>=start)&&((count<=end)||(end==-1)))
		if((substring==null)||(ps.seqName.contains(substring)))
		        d.addElement(ps);
		lr.close();
		return d;
		}
	
	public static HashMap<Sequence, Integer> searchExact(Vector<Sequence> base, String query){
		HashMap<Sequence, Integer> pointers = new HashMap<Sequence, Integer>();
		// First, we try to guess, if the previous was good hit already
		int k = 0;
		if(guessSequence!=null){
			guessStart = guessStart-1000;
			if(guessStart<0) guessStart=0;
			k = guessSequence.findExact(query, guessStart);
			if(k>0){
				pointers.put(guessSequence, k);
				guessStart = k;
			}
		}
		if(k<=0){
		for(int i=0;i<base.size();i++){
			k = base.get(i).findExact(query, 0);
			if(k>0){
				pointers.put(base.get(i), k);
				guessSequence = base.get(i);
				guessStart = k;
				break;
			}
		}}
		return pointers;
	}
	
	public static HashMap<Sequence, Integer> searchWithErrors(Vector<Sequence> base, String query, int maxNumOfErrors){
		HashMap<Sequence, Integer> pointers = new HashMap<Sequence, Integer>();
		for(int i=0;i<base.size();i++){
			int k = base.get(i).findWithErrors(query,maxNumOfErrors);
			if(k>0){
				pointers.put(base.get(i), k);
				break;
			}
		}
		return pointers;
	}
	
	public static Vector<Sequence> loadBase(String fn, String filter) throws Exception{
		Date tm = new Date();
		System.out.print("Loading base...");
		Vector<Sequence> base = readFromFasta(fn, filter);
		System.out.print(base.size()+"seqs loaded...");
		System.out.println("... took "+((new Date()).getTime()-tm.getTime())+"ms, "+Utils.getUsedMemoryMb());
		return base;
	}
	
	

}
