package getools.scripts;

import java.util.*;
import java.io.*;

import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.*;

public class selectGenesFromTable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			String folder = "c:/datas/asset/kholodenko/data_stressmodel/";
			
			String fn = "wang.txt";
			//String fn = "et_curie_ts.dat"; 
			//String fn = "bc.dat";
			String id = "ID";
			
			//VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/annot4.txt", true, "\t");
			VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/annot_95.txt", true, "\t");
			annot.makePrimaryHash("GeneSymbol");
			System.out.println("Loaded annotation");
			
			VDataTable vtt = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
			VDatReadWrite.useQuotesEverywhere = false;
			VDatReadWrite.numberOfDigitsToKeep = 2;
			VDataTable vtta = VSimpleProcedures.MergeTables(vtt, "ID", annot, "Probe", "---");
			VDatReadWrite.saveToSimpleDatFile(vtta, folder+"temp.txt");
			System.exit(1);
			
			Vector<String> geneList = Utils.loadStringListFromFile(folder+"genelist.txt");
			
			VDataTable vt = null;
			if(fn.endsWith(".txt"))
				vt = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
			if(fn.endsWith(".dat"))
				vt = VDatReadWrite.LoadFromVDatFile(folder+fn);
			vt.makePrimaryHash(id);
			System.out.println("Loaded table");
			
			
			String fn1 = folder+fn.substring(0, fn.length()-4)+"_selection.txt";
			FileWriter fw = new FileWriter(fn1);
			
			if(fn.endsWith(".txt")){
				fw.write("GENE\tPROBE\t");
				for(int i=1;i<vt.colCount;i++)
					fw.write(vt.fieldNames[i]+"\t");
						fw.write("\n");
				for(int i=0;i<geneList.size();i++){
					System.out.println("Gene:"+geneList.get(i));
					String gn = geneList.get(i);
					Vector<Integer> pos = annot.tableHashPrimary.get(gn);
					
					for(int j=0;j<pos.size();j++){
						String probe = annot.stringTable[pos.get(j)][annot.fieldNumByName("Probe")];
						Vector<Integer> posvt = vt.tableHashPrimary.get(probe);
						if(posvt!=null)if(posvt.size()>0){
							fw.write(gn+"\t"+probe+"\t");
							float f[] = new float[vt.colCount-1];
							float mean = 0;
							System.out.println("Probe: "+probe+" line:"+posvt.get(0)+"/"+vt.stringTable[posvt.get(0)][0]);
							for(int k=1;k<vt.colCount;k++){
								f[k-1] = Float.parseFloat(vt.stringTable[posvt.get(0)][k]);
								mean+=f[k-1];
							}
							mean/=vt.colCount-1;
							for(int k=1;k<vt.colCount;k++){
								fw.write((f[k-1]-mean)+"\t");
							}
							fw.write("\n");
						}
					}
				}
			}
			
			
			if(fn.endsWith(".dat")){
				fw.write("GENE\tPROBE\t");
				int numberOfSamples = 0;
				for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL)
					{ fw.write(vt.fieldNames[i]+"\t"); numberOfSamples++; }
						fw.write("\n");
				for(int i=0;i<geneList.size();i++){
					System.out.println("Gene:"+geneList.get(i));
					String gn = geneList.get(i);
					Vector<Integer> pos = annot.tableHashPrimary.get(gn);
					if(pos!=null)
					for(int j=0;j<pos.size();j++){
						String probe = annot.stringTable[pos.get(j)][annot.fieldNumByName("Probe")];
						Vector<Integer> posvt = vt.tableHashPrimary.get(probe);
						if(posvt!=null)if(posvt.size()>0){
							fw.write(gn+"\t"+probe+"\t");
							float f[] = new float[vt.colCount-1];
							float mean = 0;
							int kk=0;
							System.out.println("Probe: "+probe+" line:"+posvt.get(0)+"/"+vt.stringTable[posvt.get(0)][0]);							
							for(int k=0;k<vt.colCount;k++)if(vt.fieldTypes[k]==vt.NUMERICAL){
								f[kk] = Float.parseFloat(vt.stringTable[posvt.get(0)][k]);
								mean+=f[kk];
								kk++;
							}
							mean/=numberOfSamples;
							kk=0;
							for(int k=0;k<vt.colCount;k++)if(vt.fieldTypes[k]==vt.NUMERICAL){
								fw.write((f[kk]-mean)+"\t");
								kk++;
							}
							fw.write("\n");
						}
					}
				}
			}
			
			
			fw.close();
			
			
			/*String prefix = "c:/datas/ewing/patients/de_na";
			
			VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
			Vector<String> list = new Vector<String>();
			LineNumberReader lr = new LineNumberReader(new FileReader("c:/datas/ewing/patients/target1"));
			String s = null;
			while((s=lr.readLine())!=null)
				if(list.indexOf(s)<0)
					list.add(s);
			
			VDataTable vts = VSimpleProcedures.selectRowsFromList(vt, list, "GeneSymbol");
			VDatReadWrite.saveToVDatFile(vts, prefix+"s.dat");
			VDataTable vtst = vts.transposeTable("CHIP");
			VDatReadWrite.saveToVDatFile(vtst, prefix+"st.dat");*/
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
