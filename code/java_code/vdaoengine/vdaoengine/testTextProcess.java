package vdaoengine;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

public class testTextProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			//LineNumberReader lr = new LineNumberReader(new FileReader("C:/Datas/NaviCell/maps/dnarepair_src/temp/pubs.txt"));
			LineNumberReader lr = new LineNumberReader(new FileReader("C:/Datas/NaviCell/maps/dnarepair_src/master.xml"));
			String s = null;
			Vector<String> ids = new Vector<String>();
			while((s=lr.readLine())!=null)if(!s.trim().equals("")){
				//if(s.contains("18931676"))
				//	System.out.println(s);
				//StringTokenizer st = new StringTokenizer(s,",");
				StringTokenizer st = new StringTokenizer(s,":, \t\n");
				while(st.hasMoreTokens()){
					String tag = st.nextToken();
					if(tag.equals("PMID"))if(st.hasMoreTokens()){
						String id = st.nextToken();
						if(!ids.contains(id))
							ids.add(id);
				}}
			}
			Collections.sort(ids);
			for(int i=0;i<ids.size();i++)
				System.out.println(ids.get(i));
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
