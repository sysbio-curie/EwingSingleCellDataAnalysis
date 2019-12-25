package getools.scripts;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.utils.Utils;

public class SNPAnalysis {

	public static void main(String[] args) {
		try{

			VDataTable vt_myProfile = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/mygnm/hgdp_aprofile.txt", false, "\t");
			Vector<Float> distances = calcSNPDistances("C:/Datas/mygnm/hgdp_num.txt",vt_myProfile);
			for(int i=0;i<distances.size();i++)
				System.out.println(distances.get(i));
			System.exit(0);
			
			Vector<String> prof = Utils.loadStringListFromFile("C:/Datas/mygnm/gnmaz_filtered.txt");
			HashMap<String,String> aprofile = new HashMap<String,String>();
			for(int i=0;i<prof.size();i++){
				String ss[] = prof.get(i).split("\t");
				aprofile.put(ss[0], ss[1]);
			}
			
			//numeriseSNPtable("C:/Datas/mygnm/hgdp_test.txt",false,aprofile);
			numeriseSNPtable("C:/Datas/mygnm/hgdp.txt",false,aprofile);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void numeriseSNPtable(String fn, boolean writeLabels, HashMap<String,String> aprofile) throws Exception{
		String fout = null;
		String fout_snps = null;
		String fout_samples = null;
		
		FileWriter fw_snps = null;
		FileWriter fw_samples = null;
		
		if(writeLabels)
			fout = fn.substring(0,fn.length()-4)+"_table.txt";
		else{
			fout = fn.substring(0,fn.length()-4)+"_num.txt";
			fout_snps = fn.substring(0,fn.length()-4)+"_snps.txt";
			fout_samples = fn.substring(0,fn.length()-4)+"_samples.txt";
			fw_snps = new FileWriter(fout_snps);
			fw_samples = new FileWriter(fout_samples);
		}
		
		FileWriter indivProfile = null;
		if(aprofile!=null){
			String fprof = fn.substring(0,fn.length()-4)+"_aprofile.txt";
			indivProfile = new FileWriter(fprof);
		}
		
		FileWriter fw = new FileWriter(fout);
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		
		// read first line
		Vector<String> sampleNames = new Vector<String>();
		String s = lr.readLine(); String sm[] = s.split("\t");
		for(int i=1;i<sm.length;i++) sampleNames.add(sm[i]);
		if(writeLabels){
			for(int i=0;i<sm.length;i++) fw.write(sm[i]+"\t"); fw.write("\n");
		}else{
			for(int i=0;i<sm.length;i++) fw_samples.write(sm[i]+"\t"); fw_samples.write("\n");
		}
		
		// read next
		int countLine = 0;
		while((s=lr.readLine())!=null){
			
			countLine++;
			
			//System.out.println(s);
			
			sm = s.split("\t");
			//fw.write(s+"\n");
			
			// count occurencies
			HashMap<String,Integer> count = new HashMap<String,Integer>();
			Vector<String> als = new Vector<String>();
			for(int i=1;i<sm.length;i++){
				Integer c = count.get(sm[i]);
				if(c==null) c = new Integer(0);
				c++; count.put(sm[i], c);
				if(!als.contains(sm[i])) als.add(sm[i]);
			}
			int kdominant = -1; int countmax = 0; 
			for(int i=0;i<als.size();i++) 
				if(count.get(als.get(i))>countmax){
					countmax = count.get(als.get(i));
					kdominant = i;
				}
			int kheterozygous = -1;
			String alheterozygous = "";
			for(int i=0;i<als.size();i++){
				if(als.get(i).charAt(0)!=als.get(i).charAt(1)){
					kheterozygous = i;
					alheterozygous = als.get(i);
				}
			}
			int khomoz1 = -1; String alhomoz1 = "";
			int khomoz2 = -1; String alhomoz2 = "";
			if(kheterozygous!=-1){
				alhomoz1 = ""+alheterozygous.charAt(0)+alheterozygous.charAt(0);
				alhomoz2 = ""+alheterozygous.charAt(1)+alheterozygous.charAt(1);
				if(als.contains(alhomoz1)) khomoz1 = als.indexOf(alhomoz1);
				if(als.contains(alhomoz2)) khomoz2 = als.indexOf(alhomoz2);
			}else{
				for(int i=0;i<als.size();i++)if(!als.get(i).equals("--")){
					if(khomoz1 == -1) { khomoz1 = i; alhomoz1 = als.get(i); } 
					else { khomoz2 = i; alhomoz2 = als.get(i); }
				}
			}
			
			if(khomoz1!=-1)if(khomoz2!=-1)
			if(count.get(alhomoz2)>count.get(alhomoz1)){
				int temp = khomoz1; khomoz1=khomoz2; khomoz2 = temp;
				String stemp = alhomoz1; alhomoz1 = alhomoz2; alhomoz2 = stemp;
			}
			
			
			int emptyCounts = 0;
			if(count.get("--")!=null)
				emptyCounts = count.get("--");
			float percDominant = (float)count.get(als.get(kdominant))/(float)(sampleNames.size()-emptyCounts);
			float percEmpty = (float)emptyCounts/(float)sampleNames.size();
			
			if(percEmpty<1e-6)if(!als.get(kdominant).equals("--"))if(percDominant<0.9f){
				//Collections.sort(als);
				System.out.println(sm[0]+"\t ("+countLine+")");
				for(int i=0;i<als.size();i++){
					//fw.write(als.get(i)+":"+count.get(als.get(i))+"\t");
					System.out.print(als.get(i)+":"+count.get(als.get(i))+"\t");
				}
				System.out.println();
				//fw.write("\n");
			//sm = s.split("\t"); 
			if(writeLabels)
					fw.write(sm[0]+"\t");
			else
					fw_snps.write(sm[0]+"\n");
			for(int i=1;i<sm.length;i++){
				int num = -100;
				String al = sm[i];
				if(sm[i].equals("--")) al = als.get(kdominant);
				if(kheterozygous!=-1) if(als.get(kheterozygous).equals(al)) num = 0;
				if(khomoz1!=-1) if(als.get(khomoz1).equals(al)) num = -1;
				if(khomoz2!=-1) if(als.get(khomoz2).equals(al)) num = +1;
				fw.write(num+"\t");
			}
			fw.write("\n");
			
			// Individual profile to test
			if(aprofile!=null){
				String val = aprofile.get(sm[0]);
				String res = "N/A";
				int num = -100;
				if(val!=null){
					String valT = "N/A";
					String valR = "N/A";
					String valRT = "N/A";
					if(val.length()>1){ 
						valT = ""+val.charAt(1)+val.charAt(0);
						
						valR =  val.replace("C", "1");
						valR = valR.replace("G", "2"); 
						valR = valR.replace("A", "3");
						valR = valR.replace("T", "4");
						valR = valR.replace("1", "G");
						valR = valR.replace("2", "C"); 
						valR = valR.replace("3", "T");
						valR = valR.replace("4", "A");
						
						valRT = ""+valR.charAt(1)+valR.charAt(0);
					}
					
					if(val.equals("--")) val = als.get(kdominant);
					if(kheterozygous!=-1) if(als.get(kheterozygous).equals(val)) num = 0;
					if(kheterozygous!=-1) if(als.get(kheterozygous).equals(valT)) num = 0;
					if(kheterozygous!=-1) if(als.get(kheterozygous).equals(valR)) num = 0;
					if(kheterozygous!=-1) if(als.get(kheterozygous).equals(valRT)) num = 0;
					
					if(khomoz1!=-1) if(als.get(khomoz1).equals(val)) num = -1;
					if(khomoz1!=-1) if(als.get(khomoz1).equals(valR)) num = -1;
					if(khomoz2!=-1) if(als.get(khomoz2).equals(val)) num = +1;
					if(khomoz2!=-1) if(als.get(khomoz2).equals(valR)) num = +1;
					res = ""+num;
				}
				indivProfile.write(res+"\n");
			}
			
			}
		}
		
		
		lr.close();
		fw.close();
		if(aprofile!=null){
			indivProfile.close();
		}
		if(fw_snps!=null)
			fw_snps.close();
		if(fw_samples!=null)
			fw_samples.close();
	}
	
	public static Vector<Float> calcSNPDistances(String fn, VDataTable profile) throws Exception{
		Vector<Float> distances = new Vector<Float>();
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		int count = 0;
		while((s=lr.readLine())!=null){
			String vals[] = s.split("\t");
			if(distances.size()==0){
				for(int i=0;i<vals.length;i++)
					distances.add(0f);
			}
			String val = profile.stringTable[count][0];
			if(!val.equals("N/A")){
				Integer valf = Integer.parseInt(val);
				for(int i=0;i<vals.length;i++){
					Integer genomeValf = Integer.parseInt(vals[i]);
					
					int dist = Math.abs(genomeValf-valf);
					
					if(dist>1.1f)
						distances.set(i, (float)dist+(float)distances.get(i));
				}
			}
			count++;
		}
		lr.close();
		return distances;
	}
	
	

}
