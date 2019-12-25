package vdaoengine;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.*;
import vdaoengine.data.io.*;

import java.io.*;
import java.util.*;

public class processMIRNASummaryTable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			String prefix = "C:/Datas/EWING/miRNA/summarytable/table_mirna_target_ranking_limited_time_series_1.txt/simpletable";
			
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

			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"global.txt", true, "\t");
			for(int i=2;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, prefix+"global.dat");
			vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"analyse_gpcr.txt", true, "\t");
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

