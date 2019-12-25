package getools.scripts;

import getools.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.VSimpleProcedures;

public class ASSETDataAnalysis {

	public static void main(String[] args) {
		try{
			
			String folder = "";
			String file = "";
			// miRNA data
			
			folder = "C:/Datas/ASSET/_work/expression/mixed/";
			file = "pediatric_mirna.txt";
			
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+file, true, "\t");
			for(int i=1;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			
			VDataTable vt1 = TableUtils.fillMissingValues(vt, 10);
			
			vt1 = TableUtils.normalizeVDat(vt1, true, false);
			
			VDatReadWrite.useQuotesEverywhere = false;
			VDatReadWrite.saveToSimpleDatFile(vt1, folder+"pediatric_mirna_imp.txt");
			VDatReadWrite.saveToVDatFile(vt1, folder+"pediatric_mirna_imp.dat");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
