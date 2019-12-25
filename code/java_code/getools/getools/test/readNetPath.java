package getools.test;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import getools.*;

public class readNetPath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			Vector signatures = new Vector();
			
			File f = new File("c:/datas/netpath");
			File files[] = f.listFiles();
			for(int i=0;i<files.length;i++){
				String fn = files[i].getName();
				System.out.println(fn);
				if(fn.endsWith("_NP")){
					GESignature gs = new GESignature();
					gs.name = fn;
					LineNumberReader lr = new LineNumberReader(new FileReader(files[i]));
					String s = null;
					while((s=lr.readLine())!=null){
						StringTokenizer st = new StringTokenizer(s," \t");
						while(st.hasMoreTokens()){
							String ss = st.nextToken();
							gs.geneNames.add(ss);
							gs.probeSets.add(ss);
						}
					}
					lr.close();
					signatures.add(gs);
				}
				if(fn.endsWith("_NP_GENES")){
					VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(files[i].getAbsolutePath(), true, "\t");
					GESignature gsup = new GESignature(); gsup.name = fn+"_UP";
					GESignature gsdown = new GESignature();	gsdown.name = fn+"_DOWN";
					GESignature gsup_m = new GESignature(); gsup_m.name = fn+"_UP_M";
					GESignature gsdown_m = new GESignature(); gsdown_m.name = fn +"_DOWN_M";
					GESignature gs = new GESignature(); gs.name = fn;
					GESignature gs_m = new GESignature(); gs_m.name = fn+"_M";
					
					for(int j=0;j<vt.rowCount;j++){
						String gname = vt.stringTable[j][vt.fieldNumByName("GeneName")];
						String regulation = vt.stringTable[j][vt.fieldNumByName("Regulation")];
						String exp = vt.stringTable[j][vt.fieldNumByName("ExperimentType")];
						System.out.println(vt.stringTable[j][0]+"\t"+vt.stringTable[j][1]+"\t"+vt.stringTable[j][2]+"\t"+vt.stringTable[j][3]);
						gs_m.probeSets.add(gname);
						if(exp.indexOf("Non-Microarray")>=0)
							gs.probeSets.add(gname);
						if(regulation.equals("Downregulation")){
							gsdown_m.probeSets.add(gname);
							if(exp.indexOf("Non-Microarray")>=0)
								gsdown.probeSets.add(gname);
						}else{
							gsup_m.probeSets.add(gname);
							if(exp.indexOf("Non-Microarray")>=0)
								gsup.probeSets.add(gname);
						}
					}
					
					signatures.add(gs);
					signatures.add(gs_m);
					signatures.add(gsup);
					signatures.add(gsdown);
					signatures.add(gsup_m);
					signatures.add(gsdown_m);					
				}
			}
			
			GMTReader.saveSignaturesTOGMTFormat(signatures, "c:/datas/netpath/NetPath.gmt");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
