package vdaoengine;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.*;
import vdaoengine.data.io.*;

import java.io.*;
import java.util.*;

public class ProteaseAnalysisScript {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			String prefix = "c:/datas/ewing/protease2/";
			Vector<String> files = new Vector<String>();
			files.add("LIST_CHIPCHIP.txt");
			files.add("LIST_CHIPSEQ.txt");
			files.add("LIST_GPCR.txt");
			files.add("LIST_KINASE.txt");
			files.add("LIST_NETWORK.txt");
			files.add("LIST_PHOSPHATASE.txt");
			files.add("LIST_PROTEASOME.txt");
			
			files.add("INDUCTION_SCORE.txt");
			files.add("DECAY_SCORE.txt");			
			files.add("MOTIF.txt");
			files.add("EW_VS_NORM.txt");
			files.add("EWCL_VS_MSC.txt");
			files.add("EWCL_VS_NORM.txt");
			files.add("EWTM_VS_MSC.txt");
			files.add("EWTM_VS_NORM.txt");
			files.add("EWXN.txt");
			
			/*Vector<String> fullListIds = new Vector<String>();
			
			for(int i=0;i<files.size();i++){
				VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(prefix+files.get(i), true, "\t");
				int k=vt.fieldNumByName("ENTREZ");
				System.out.println(files.get(i)+" fn="+k+" nrows="+vt.rowCount);				
				for(int j=0;j<vt.rowCount;j++){
					if(vt.stringTable[j][k].equals("9997"))
						System.out.println("9997 is found");
					if(vt.stringTable[j][k].equals("protein"))
						System.out.println("protein is found");
					if(fullListIds.indexOf(vt.stringTable[j][k])<0)
							fullListIds.add(vt.stringTable[j][k]);
				}
			}
			
			Collections.sort(fullListIds);
			
			FileWriter fw = new FileWriter(prefix+"allids.txt");
			for(int i=0;i<fullListIds.size();i++)
				fw.write(fullListIds.get(i)+"\n");
			fw.close();*/
			
			/*VDataTable vti = VDatReadWrite.LoadFromSimpleDatFile(prefix+"IDS.txt", true, "\t");
			
			for(int i=0;i<files.size();i++){
				VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(prefix+files.get(i), true, "\t");
				System.out.println(files.get(i)+"\t"+vt.colCount+" columns");
				String cn = files.get(i).substring(0,files.get(i).length()-4);
				if(cn.toLowerCase().startsWith("list_")) cn = cn.substring(5);
				vt.addNewColumn(cn, "", "", vt.NUMERICAL, "1");
				vti = VSimpleProcedures.MergeTables(vti, "ENTREZ", vt, "ENTREZ", "0");
			}
			
			VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile(prefix+"EntrezHug.cleant.txt", true, "\t");
			vti = VSimpleProcedures.MergeTables(vti, "ENTREZ", annot, "ENTREZ", "0");
			
			VDatReadWrite.saveToSimpleDatFile(vti, prefix+"Total.xls");*/
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"scores.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;			
			VDatReadWrite.saveToVDatFile(vt, prefix+"scores.dat");*/	
			
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"proteasome.txt", true, "\t");
			//VDatReadWrite.saveToVDatFile(vt, prefix+"proteasome.dat");
			
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"allgenome.txt", true, "\t");
			//VDatReadWrite.saveToVDatFile(vt, prefix+"allgenome.dat");

			/*VDataTable vt = VDatReadWrite.LoadFromVDatFile(prefix+"proteasome1.dat");
			
			LineNumberReader lr = new LineNumberReader(new FileReader(prefix+"tsgenes.txt"));
			Vector<String> ids = new Vector<String>();
			String s = null;
			while((s=lr.readLine())!=null){
				if(ids.indexOf(s)<0)
					ids.add(s);
			}
			
			VDataTable vts = VSimpleProcedures.selectRowsFromList(vt, ids, "ENTREZ");
			ids.clear(); ids.add("1");
			vts = VSimpleProcedures.selectRowsFromList(vts, ids, "PROTEASE");*/
			
			/*VDataTable vts = VDatReadWrite.LoadFromVDatFile(prefix+"global.dat");			
			
			
			VDataTable vt_ttest = VDatReadWrite.LoadFromSimpleDatFile(prefix+"ttest.txt", true, "\t");
			for(int i=1;i<vt_ttest.colCount;i++) vt_ttest.fieldTypes[i] = vt_ttest.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt_ttest, prefix+"ttest.dat");			
			VDataSet vd_ttest = VSimpleProcedures.SimplyPreparedDataset(vt_ttest, -1);
			PCAMethod pca_ttest = new PCAMethod();
			pca_ttest.setDataSet(vd_ttest);
			pca_ttest.calcBasis(1);
			VDataSet vd_ttestp = pca_ttest.getProjectedDataset();

			vts.addNewColumn("PC1_ttest", "", "", VDataTable.NUMERICAL, "0.0");
			for(int i=0;i<vts.rowCount;i++) vts.stringTable[i][vts.fieldNumByName("PC1_ttest")] = ""+vd_ttestp.massif[i][0];
			
			VDataTable vt_genomic = VDatReadWrite.LoadFromSimpleDatFile(prefix+"motif_only.txt", true, "\t");
			VDatReadWrite.saveToVDatFile(vt_genomic, prefix+"motif_only.dat");
			for(int i=1;i<vt_genomic.colCount;i++) vt_genomic.fieldTypes[i] = vt_genomic.NUMERICAL;
			VDataSet vd_genomic = VSimpleProcedures.SimplyPreparedDataset(vt_genomic, -1);
			PCAMethod pca_genomic = new PCAMethod();
			pca_genomic.setDataSet(vd_genomic);
			pca_genomic.calcBasis(1);
			VDataSet vd_genomicp = pca_genomic.getProjectedDataset();

			vts.addNewColumn("PC1_genomic", "", "", VDataTable.NUMERICAL, "0.0");
			for(int i=0;i<vts.rowCount;i++) vts.stringTable[i][vts.fieldNumByName("PC1_genomic")] = ""+vd_genomicp.massif[i][0];

			
			VDataTable vt_scores = VDatReadWrite.LoadFromSimpleDatFile(prefix+"scores.txt", true, "\t");
			VDatReadWrite.saveToVDatFile(vt_scores, prefix+"scores.dat");
			for(int i=1;i<vt_scores.colCount;i++) vt_scores.fieldTypes[i] = vt_scores.NUMERICAL;
			VDataSet vd_scores = VSimpleProcedures.SimplyPreparedDataset(vt_scores, -1);
			PCAMethod pca_scores = new PCAMethod();
			pca_scores.setDataSet(vd_scores);
			pca_scores.calcBasis(1);
			VDataSet vd_scoresp = pca_scores.getProjectedDataset();

			vts.addNewColumn("PC1_scores", "", "", VDataTable.NUMERICAL, "0.0");
			for(int i=0;i<vts.rowCount;i++) vts.stringTable[i][vts.fieldNumByName("PC1_scores")] = ""+vd_scoresp.massif[i][0];
			
			
		    VDatReadWrite.saveToVDatFile(vts, prefix+"global1.dat");
		    VDatReadWrite.saveToSimpleDatFile(vts, prefix+"global1.xls");*/

			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"global.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"global.dat");*/
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"analyse_gpcr.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"analyse_gpcr.dat");
			vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"analyse_kinase.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"analyse_kinase.dat");
			vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"analyse_network.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"analyse_network.dat");
			vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"analyse_phosphatase.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"analyse_phosphatase.dat");
			vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"analyse_proteasome.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"analyse_proteasome.dat");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}

