package getools.scripts;

import java.io.FileWriter;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;

import getools.GMTReader;
import getools.Metagene;

public class ICAAnalysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			//String prefix = "C:/Datas/ColorMaps/whole_acsn/";
			//String prefix = "C:/Datas/ColorMaps/whole_acsn/ewing_kinetics/";
			//String moduleFile = "acsn_master.gmt";
			String prefix = "C:/Datas/MOSAIC/analysis/ica/full_rescue/"; 
			String moduleFile = "cellcycle.gmt";
			//String datafile = "ica_breastcancer_GSE20686_data.txt";
			//String datafile = "S_CIT.txt";
			//String datafile = "meta_IC4.txt";
			//String datafile = "tsc1na.txt";
			String prefix2 = "DAYS_0";
			String datafile = prefix2+"_S.xls ";
			
			
			Vector<Metagene> signatures = GMTReader.readGMTDatabaseWithWeights(prefix+moduleFile);
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+prefix2+"/"+datafile, true, "\t");
			//vt.makePrimaryHash("GeneSymbol");
			vt.makePrimaryHash("PROBE");
			
			String fields[] = {"IC1","IC2","IC3","IC4","IC5","IC6","IC7","IC8","IC9","IC10","IC11","IC12","IC13","IC14","IC15","IC16","IC17","IC18","IC19","IC20"};
			//String fields[] = {"CIT4"};
			//String fields[] = {"D0","D1","D2","D3","D6","D9","D11","D13","D15","D17","D13T","D15T","D17T"};
			
			FileWriter fw = new FileWriter(prefix+"module_averages_"+datafile);
			fw.write("MODULE\t");
			for(String s: fields) fw.write(s+"\t");
			fw.write("\n");
			
			for(Metagene meta: signatures){
				fw.write(meta.name+"\t");
				for(String field: fields)if(vt.fieldNumByName(field)>=0){
					float average = 0f;
					int count = 0;
					for(String gene: meta.geneNames){
						Vector<Integer> inds = vt.tableHashPrimary.get(gene);
						if(inds!=null){
							float value = Float.parseFloat(vt.stringTable[inds.get(0)][vt.fieldNumByName(field)]);
							average+=value;
							count++;
						}
					}
					if(count!=0)
					     average/=(float)count;
					fw.write(average+"\t");
				}
				fw.write("\n");
			}
			fw.close();

			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(prefix+"module_averages_"+datafile, true, "\t");
			for(int i=1;i<vt1.colCount;i++)
				vt1.fieldTypes[i] = vt1.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt1, prefix+"module_averages.dat");
			VDatReadWrite.saveToVDatFile(vt1.transposeTable("MODULE"), prefix+"module_averages_t.dat");
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
