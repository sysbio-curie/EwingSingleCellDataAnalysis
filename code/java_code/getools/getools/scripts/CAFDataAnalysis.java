package getools.scripts;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.Vector;

import getools.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;

public class CAFDataAnalysis {

	public static void main(String[] args) {
		try{
			
			/*LineNumberReader lr = new LineNumberReader(new FileReader("C:/Datas/MELANOMA/GSE72056/melanoma1.txt"));
			FileWriter fw = new FileWriter("C:/Datas/MELANOMA/GSE72056/melanoma2.txt");
			int k=0;
			String s = null;
			while((s=lr.readLine())!=null){
				String s1 = Utils.replaceString(s, "0.00", "0");
				fw.write(s1+"\n");
				System.out.println(k++);
			}
			fw.close();*/
			//String fld = "C:/Datas/MOSAIC/expression/metaanalysis/initial_files/NORMAL/";
			String fld = "C:/Datas/MELANOMA/GSE72056/";
			TableUtils.filterColumnsInTxtTable(fld+"melanoma2.txt", fld+"cells.txt", "non_malignant_cell_type", "5");
			System.exit(0);
					
			
			
			String folder = "C:/Datas/CAFibroblasts/";
			String prefix = "caf_lc";
			
			String fname = folder+prefix+".txt";
			
			// Prepare table, log and center
			/*VDataTable samples = VDatReadWrite.LoadFromSimpleDatFile(folder+"meta.txt", true, "\t");
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fname, true, "\t");
			for(int i=1;i<vt.colCount;i++)
				for(int j=0;j<vt.rowCount;j++){
					float f = Float.parseFloat(vt.stringTable[j][i]);
					f = (float)Math.log10(f+1f);
					vt.stringTable[j][i] = ""+f;
				}
			VDatReadWrite.numberOfDigitsToKeep = 3;
			VDatReadWrite.useQuotesEverywhere = false;
			VDatReadWrite.writeNumberOfColumnsRows = false;
			TableUtils.findAllNumericalColumns(vt);
			VDatReadWrite.saveToVDatFile(vt,folder+prefix+"_l.dat");
			VDatReadWrite.saveToSimpleDatFile(vt, folder+prefix+"_l.txt");
			
			VDataTable vts = TableUtils.filterByVariation(vt, 3000, false);
			vts = vts.transposeTable("SYMBOL");			
			VDataTable vtsa = VSimpleProcedures.MergeTables(samples, "SAMPLE", vts, "NAME", "_");
			VDatReadWrite.saveToVDatFile(vtsa,folder+prefix+"_lc_transposed.dat");
			
			vt = TableUtils.normalizeVDat(vt, true, false);
			VDatReadWrite.saveToVDatFile(vt,folder+prefix+"_lc.dat");
			VDatReadWrite.saveToSimpleDatFile(vt, folder+prefix+"_lc.txt");
			System.exit(0);*/

			
			//PanMethylome.transformTableToICA(folder+prefix+"_lc.txt");
			
			//PanMethylome.CompileAandSTables(folder, prefix, folder+"genes_annot.txt", folder+"meta.txt", null);
			
			//PanMethylome.makeGSEABatchFile("C:/Datas/CAFibroblasts/gsea/", "msigdb_acsn.gmt", "C:/Datas/CAFibroblasts/gsea/resulTs/");

			int numOfComponents = 0; Vector<String> names = new Vector<String>();
			VDataTable vt = VDatReadWrite.LoadFromVDatFile(folder+prefix+"_A_annot.dat");
			for(int i=0;i<vt.colCount;i++) if(vt.fieldNames[i].startsWith("IC")) numOfComponents++; 
			for(int i=1;i<=numOfComponents;i++) names.add("IC"+i);
			TableUtils.printAssosiationTable(vt, folder+prefix+"_A_associations.txt", names,0f, 5, true);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
