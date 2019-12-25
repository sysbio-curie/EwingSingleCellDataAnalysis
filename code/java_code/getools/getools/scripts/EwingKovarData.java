package getools.scripts;

import getools.GESignature;
import getools.GMTReader;

import java.io.FileWriter;
import java.util.Set;
import java.util.Vector;

import vdaoengine.TableUtils;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class EwingKovarData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			/*VDataTable kvr = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/kovar/kvr.txt",true," ");
			VDatReadWrite.saveToVDatFile(kvr, "c:/datas/ewing/kovar/kvr1.dat");
			System.exit(0);*/
			
			/*VDataTable kvr1 =  vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kovar/kvr1.dat");
			System.out.println(kvr1.colCount);
			System.out.println(kvr1.rowCount);
			
			FileWriter fw = new FileWriter("c:/datas/ewing/kovar/kvr2.txt"); 
			fw.write("Probe\tH00\tH18\tH36\tH53\tH72\tH96\n");
			//V00\tV18\tV36\tV53\tV72\tV96\n");
			float f[] = new float[1];
			for(int i=0;i<kvr1.rowCount;i++){
				String probe = kvr1.stringTable[i][kvr1.fieldNumByName("Probe")];
				String H18_790 = kvr1.stringTable[i][kvr1.fieldNumByName("H18_790")];
				String H00_960 = kvr1.stringTable[i][kvr1.fieldNumByName("H00_960")];
				String H36_790 = kvr1.stringTable[i][kvr1.fieldNumByName("H36_790")];
				String H72_960 = kvr1.stringTable[i][kvr1.fieldNumByName("H72_960")];
				String H96_810 = kvr1.stringTable[i][kvr1.fieldNumByName("H96_810")];
				String H72_961 = kvr1.stringTable[i][kvr1.fieldNumByName("H72_961")];
				String H00_870 = kvr1.stringTable[i][kvr1.fieldNumByName("H00_870")];
				String H96_960 = kvr1.stringTable[i][kvr1.fieldNumByName("H96_960")];
				String H18_870 = kvr1.stringTable[i][kvr1.fieldNumByName("H18_870")];
				String H36_870 = kvr1.stringTable[i][kvr1.fieldNumByName("H36_870")];
				String H00_871 = kvr1.stringTable[i][kvr1.fieldNumByName("H00_871")];
				String H18_871 = kvr1.stringTable[i][kvr1.fieldNumByName("H18_871")];
				String H36_871 = kvr1.stringTable[i][kvr1.fieldNumByName("H36_871")];
				String H53_870 = kvr1.stringTable[i][kvr1.fieldNumByName("H53_870")];
				String H53_871 = kvr1.stringTable[i][kvr1.fieldNumByName("H53_871")];
				String H00_790 = kvr1.stringTable[i][kvr1.fieldNumByName("H00_790")];
				VStatistics v00 = new VStatistics(1);
				VStatistics v18 = new VStatistics(1);
				VStatistics v36 = new VStatistics(1);
				VStatistics v53 = new VStatistics(1);
				VStatistics v72 = new VStatistics(1);
				VStatistics v96 = new VStatistics(1);
				// 00
				f[0] = Float.parseFloat(H00_960); v00.addNewPoint(f);
				f[0] = Float.parseFloat(H00_870); v00.addNewPoint(f);
				f[0] = Float.parseFloat(H00_871); v00.addNewPoint(f);
				// 18
				f[0] = Float.parseFloat(H18_790); v18.addNewPoint(f);
				f[0] = Float.parseFloat(H18_870); v18.addNewPoint(f);
				f[0] = Float.parseFloat(H18_871); v18.addNewPoint(f);
				// 36
				f[0] = Float.parseFloat(H36_790); v36.addNewPoint(f);
				f[0] = Float.parseFloat(H36_870); v36.addNewPoint(f);
				f[0] = Float.parseFloat(H36_871); v36.addNewPoint(f);
				// 53
				f[0] = Float.parseFloat(H53_870); v53.addNewPoint(f);
				f[0] = Float.parseFloat(H53_871); v53.addNewPoint(f);
				// 72 
				f[0] = Float.parseFloat(H72_960); v72.addNewPoint(f);
				f[0] = Float.parseFloat(H72_961); v72.addNewPoint(f);
				// 96
				f[0] = Float.parseFloat(H96_810); v96.addNewPoint(f);
				v00.calculate(); v18.calculate(); v36.calculate(); v53.calculate(); v72.calculate(); v96.calculate();
				fw.write(probe+"\t");
				fw.write(v00.getMean(0)+"\t"+v18.getMean(0)+"\t"+v36.getMean(0)+"\t"+v53.getMean(0)+"\t"+v72.getMean(0)+"\t"+v96.getMean(0)+"\t");
				//fw.write(v00.getStdDev(0)+"\t"+v18.getStdDev(0)+"\t"+v36.getStdDev(0)+"\t"+v53.getStdDev(0)+"\t"+v72.getStdDev(0)+"\t"+v96.getStdDev(0));
				fw.write("\n");
			}
			fw.close();*/
			
			//VDataTable kvr1 =  vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/kovar/kvr2.txt",true,"\t");
			//VDatReadWrite.saveToVDatFile(kvr1, "c:/datas/ewing/kovar/kvr2.dat");
			/*VDataTable kvr1 =  vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kovar/kvr2.dat");
			VDataTable annot = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/annot4.txt", true, "\t");
			VDataTable mrg = VSimpleProcedures.MergeTables(kvr1, "Probe", annot, "Probe", "//");
			mrg = TableUtils.normalizeVDat(mrg, true, false);
			VDatReadWrite.saveToVDatFile(mrg, "c:/datas/ewing/kovar/kvr2a.dat");
			mrg = TableUtils.filterByVariation(mrg, 3000, false);
			VDatReadWrite.saveToVDatFile(mrg, "c:/datas/ewing/kovar/kvr2a_3000.dat");*/
			
			/*VDataTable kvr1 =  vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kovar/kvr2a.dat");
			kvr1 = TableUtils.filterByVariation(kvr1, 1000, false);
			VDatReadWrite.saveToVDatFile(kvr1, "c:/datas/ewing/kovar/kvr2a_1000.dat");*/
			
			/*String list = "directtargets";
			//String list = "e2f1targets";
			String file = "tsc1nas";
			//String file = "kvr2a";
			VDataTable kvr1 =  vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kovar/"+file+".dat");
			kvr1.makePrimaryHash("GeneSymbol");
			Vector<String> genes = Utils.loadStringListFromFile("c:/datas/ewing/kovar/"+list);
			kvr1 = VSimpleProcedures.selectRowsFromList(kvr1, genes);
			VDatReadWrite.saveToVDatFile(kvr1, "c:/datas/ewing/kovar/"+file+"_"+list+".dat");*/
			
			// Replace individual values by module averages
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/expression/old_kinetics/data.txt",true,"\t");
			vt.makePrimaryHash(vt.fieldNames[0]);
			Set<String> genes = vt.tableHashPrimary.keySet();
			FileWriter fw = new FileWriter("C:/Datas/MOSAIC/expression/old_kinetics/data_gene.txt");
			for(int i=0;i<vt.colCount;i++)
				fw.write(vt.fieldNames[i]+"\t");
			fw.write("\n");
			for(String g: genes){
				Vector<Integer> rows = vt.tableHashPrimary.get(g);
				fw.write(g+"\t");
				for(int i=1;i<vt.colCount;i++){
					float val = 0f;
					for(int j=0;j<rows.size();j++)
						val+=Float.parseFloat(vt.stringTable[rows.get(j)][i]);
					val/=rows.size();
					fw.write(val+"\t");
				}
				fw.write("\n");
			}
			fw.close();*/
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/expression/old_kinetics/data_gene.txt",true,"\t");
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/NaviCell2.2/ovca_tcga/t_test_prolif_mesench.txt",true,"\t");
			String pref = "DAYS_0";
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/ica/full_rescue/"+pref+"/"+pref+"_S.xls",true,"\t");
			//Vector<GESignature> sigs = GMTReader.readGMTDatabase("C:/Datas/acsn/acsn_master.gmt");
			Vector<GESignature> sigs = GMTReader.readGMTDatabase("C:/Datas/MOSAIC/analysis/ica/full_rescue/cellcycle.gmt");
			vt.makePrimaryHash(vt.fieldNames[0]);
			//FileWriter fw = new FileWriter("C:/Datas/MOSAIC/expression/old_kinetics/data_gene_moduleav.txt");
			//FileWriter fw = new FileWriter("C:/Datas/NaviCell2.2/ovca_tcga/t_test_prolif_mesench_moduleav.txt");
			FileWriter fw = new FileWriter("C:/Datas/MOSAIC/analysis/ica/full_rescue/"+pref+"_moduleaveraged.txt");
			for(int i=0;i<vt.colCount;i++)
				fw.write(vt.fieldNames[i]+"\t");
			fw.write("\n");
			for(int i=0;i<vt.rowCount;i++){
				String gene = vt.stringTable[i][0];
				
				Vector<GESignature> sg = GESignature.getAllSignaturesContainingGene(gene, sigs);
				fw.write(gene+"\t");
				for(int j=1;j<vt.colCount;j++){
					float val = Float.parseFloat(vt.stringTable[i][j]);
					
					if(sg.size()>0){
						
						val = 0;
						float vals[] = new float[sg.size()];
						for(int k=0;k<sg.size();k++){
							float avm = 0f;
							for(String g: sg.get(k).geneNames)if(vt.tableHashPrimary.get(g)!=null)
								avm+=Float.parseFloat(vt.stringTable[vt.tableHashPrimary.get(g).get(0)][j]);
							avm/=sg.get(k).geneNames.size();
							vals[k]=avm;
						}
						val = VSimpleFunctions.calcMean(vals);
					}
					
					fw.write(val+"\t");
					
				}
				fw.write("\n");
			}
			fw.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
