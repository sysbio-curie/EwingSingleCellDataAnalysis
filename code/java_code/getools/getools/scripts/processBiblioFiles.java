package getools.scripts;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.utils.Utils;

public class processBiblioFiles {

	public class AuthorName{
		String initials = "";
		String familyname = "";
	}
	
	public class BibRecord{
		String key = "";
		int year = -1;
		Vector<AuthorName> authors = new Vector<AuthorName>();
		String title = "";
		String journalWithVolumePages = "";
		
		public void loadFromString(String s){
			year = extractYear(s);
			StringTokenizer st = new StringTokenizer(s,".{}().");
			st.nextToken();
			key = st.nextToken();
			String authorsList = st.nextToken().trim();
			authors = decipherAuthorList(authorsList);
			title = st.nextToken().trim();
			journalWithVolumePages = s.substring(s.indexOf(title)+title.length()).trim();
		}
		public String formatForMMNP(){
			String res = "\\bibitem{"+key+"} ";
			for(int i=0;i<authors.size();i++){
				AuthorName name = authors.get(i);
				res+=name.initials+"~"+name.familyname+", ";
			}
			res = res.substring(0,res.length()-2);
			res+=".";
			res+="\\textit{"+title+"}";
			res+=journalWithVolumePages;
			res+=" ("+year+")";
			return res;
		}
		public int extractYear(String s){
			int res = -1;
			for(int y=1900;y<2050;y++){
				if(s.contains(""+y))
					res = y;
			}
			return res;
		}
		public Vector<AuthorName> decipherAuthorList(String s){
			StringTokenizer st = new StringTokenizer(s,",");
			Vector<AuthorName> res = new Vector<AuthorName>();
			while(st.hasMoreTokens()){
				String s1 = st.nextToken();
				StringTokenizer st1 = new StringTokenizer(s1," ");
				AuthorName an = new AuthorName();
				an.familyname = st1.nextToken();
				an.initials = st1.nextToken();
				String in = "";
				for(int i=0;i<an.initials.length();i++)
					in+=an.initials.toCharArray()[i]+".";
				an.initials = in;
				res.add(an);
			}
			return res;
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			Vector<BibRecord> records = new Vector<BibRecord>();
			
			LineNumberReader lr = new LineNumberReader(new FileReader("c://mypapers/mmnp2014/biblio1.txt"));
			String s = null;
			while((s=lr.readLine())!=null){
				s=s.trim();
				if(!s.equals("")){
					BibRecord rec = (new processBiblioFiles()).new BibRecord();
					rec.loadFromString(s);
					System.out.println(rec.formatForMMNP());
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
