package vdaoengine;

import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VDownloader;

public class GoogleScholarStatistics {
	
	public class GoogleScholarRecord{
		public String name = null;
		public String link = null;
		public String affiliation = null;
		public int totalCitations = 0;
		public int hircsh = 0;
		public int i10 = 0;
		public int[] topCited = null; 
		public String page = null;
		public String toString(){
			String res = "";
			res =  "Name: "+name+"\n";
			res += "Link: "+link+"\n";
			res += "Affiliation: "+affiliation+"\n";
			res += "TotalCitations: "+totalCitations+"\n";
			res += "H-index: "+hircsh+"\n";
			res += "i10: "+i10+"\n";
			return res;
		}
	}
	
	public static String linkKeywordSearch = "http://scholar.google.fr/citations?view_op=search_authors&mauthors=label:";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
		
		//String keyword = "model_reduction";
		//String keyword = "bioinformatics";
		String keyword = "physics";
		
		GoogleScholarStatistics gs = new GoogleScholarStatistics();
		Vector<GoogleScholarRecord> recs = gs.getRecordsByKeyword(keyword);

		FileWriter fw = new FileWriter("c:/datas/GoogleScholar/"+keyword+".xls");
		fw.write("NAME\tLINK\tAFFILIATION\tTOTAL\n");
		for(GoogleScholarRecord rec: recs)
			fw.write(rec.name+"\t"+rec.link+"\t"+rec.affiliation+"\t"+rec.totalCitations+"\n");
		fw.close();
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Vector<GoogleScholarRecord> getRecordsByKeyword(String keyword){
		Vector<GoogleScholarRecord> records = new Vector<GoogleScholarRecord>();
		boolean haveNext = true;
		String resp = VDownloader.downloadURL(linkKeywordSearch+keyword);		
		Vector<String> alreadySeen = new Vector<String>();
		while(haveNext){
			extractRecordsFromPage(resp, records);
			StringTokenizer st = new StringTokenizer(resp,"<>");
			String s = null;
			haveNext = false;
			boolean found = false;
			while((s=st.nextToken())!=null){
				if(s.startsWith("a href=\"/citations?view_op=search_authors")){
					s = s.split(" ")[1];
					s = s.substring(6, s.length()-1);
					haveNext = true;
					s = Utils.replaceString(s, "amp;", "");
					if(!alreadySeen.contains(s))if(!s.endsWith("astart=0"))if(s.contains("after_author")){
						System.out.println(s);										
						alreadySeen.add(s);
						resp = VDownloader.downloadURL("http://scholar.google.fr/"+s);
						found = true;
						break;
					}
				}
			if(!st.hasMoreTokens()) break;
			}
			if(!found) haveNext = false;
		}
		
		return records;
	}
	
	public void extractRecordsFromPage(String resp, Vector<GoogleScholarRecord> records){
		StringTokenizer st = new StringTokenizer(resp,"<>"); 
		String s = null;
		try{
		while((s=st.nextToken())!=null){
			//System.out.println(s);
			if(s.startsWith("a class=\"cit-dark-large-link\" href=\"/citations?")){
				StringTokenizer st1 = new StringTokenizer(s,"\" ");
				st1.nextToken();st1.nextToken();st1.nextToken();st1.nextToken(); // skip a class= cit-dark-large-link href= 
				GoogleScholarRecord rec = new GoogleScholarRecord();
				rec.link = st1.nextToken();
				rec.name = st.nextToken();
				st.nextToken();st.nextToken(); // skip /a /br
				rec.affiliation = st.nextToken();
				st.nextToken(); st.nextToken();  st.nextToken();  // skip br adress_valide br
				String cite = st.nextToken();
				String cites[] = cite.split(" ");
				cite = cites[1];
				if(cite.endsWith("fois")) cite = cite.substring(0, cite.length()-5);
				rec.totalCitations = Integer.parseInt(cite);
				records.add(rec);
			}
		}
		}catch(Exception e){
			
		}
	}
	
	public Vector<GoogleScholarRecord> fillRecordsFromFile(String fn){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		Vector<GoogleScholarRecord> records = new Vector<GoogleScholarRecord>();
		for(int i=0;i<vt.rowCount;i++){
			System.out.println("Record "+i);
			String name = vt.stringTable[i][vt.fieldNumByName("NAME")];
			String link = vt.stringTable[i][vt.fieldNumByName("LINK")];
			String affiliation = vt.stringTable[i][vt.fieldNumByName("AFFILITATION")];
			String total = vt.stringTable[i][vt.fieldNumByName("TOTAL")];
			GoogleScholarRecord rec = new GoogleScholarRecord();
			rec.name = name;
			rec.link = Utils.replaceString(link,"amp;","");
			rec.affiliation = affiliation;
			rec.totalCitations = Integer.parseInt(total);
			analyzeRecord(rec);
			records.add(rec);
		}
		vt.addNewColumn("HINDEX", "", "", vt.NUMERICAL, "");
		vt.addNewColumn("I10", "", "", vt.NUMERICAL, "");
		for(int i=0;i<100;i++)
			vt.addNewColumn("TOP"+(i+1), "", "", vt.NUMERICAL, "");
		
		for(int i=0;i<vt.rowCount;i++){
			vt.stringTable[i][vt.fieldNumByName("HINDEX")] = ""+records.get(i).hircsh;
			vt.stringTable[i][vt.fieldNumByName("I10")] = ""+records.get(i).i10;
			for(int j=0;j<100;j++)
				vt.stringTable[i][vt.fieldNumByName("TOP"+(j+1))] = ""+records.get(i).topCited[j];
		}
			
		VDatReadWrite.saveToSimpleDatFile(vt, fn+".dat");
		return records;
	}
	
	
	public void analyzeRecord(GoogleScholarRecord record){
		String page = VDownloader.downloadURL("http://scholar.google.fr"+record.link);
		StringTokenizer st = new StringTokenizer(page,"<>");
		String key = "a class=\"cit-dark-link\" href=\"http://scholar.google.fr/scholar?oi=bibs";
		String keyh = "indice h</a></td><td class=\"cit-borderleft cit-data";
		String keyi10 = "indice i10</a></td><td class=\"cit-borderleft cit-data";
		String s = null;
		record.topCited = new int[100];
		int k = 0;
		while((s=st.nextToken())!=null){
			if(s.startsWith(keyh)){
				record.hircsh = Integer.parseInt(st.nextToken());
			}
			if(s.startsWith(keyi10)){
				record.i10 = Integer.parseInt(st.nextToken());
			}
			if(s.startsWith(key)){
				record.topCited[k] = Integer.parseInt(st.nextToken());
				k++;
			}
		}

	}
	
	

}
