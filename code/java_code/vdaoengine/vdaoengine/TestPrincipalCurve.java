package vdaoengine;

import java.util.Vector;

import vdaoengine.analysis.PCAMethod;
import vdaoengine.analysis.elmap.Curve;
import vdaoengine.analysis.elmap.ElmapAlgorithm;
import vdaoengine.analysis.elmap.ElmapAlgorithmEpoch;
import vdaoengine.analysis.elmap.ElmapProjection;
import vdaoengine.analysis.elmap.Grid;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;
import vdaoengine.utils.VVectorCalc;

public class TestPrincipalCurve {
	// Initialization file for the algorithm of elastic curve
	public static String INI_FILE = "C:/Datas/MOSAIC/analysis/cellcycle/elmap.ini";

	public static void main(String[] args) {
		try{
			
			String folder = "C:/Datas/MOSAIC/analysis/cellcycle/";
			
			// Loading table of data in VidaExpert format
			//VDataTable vt = VDatReadWrite.LoadFromVDatFile(folder+"day9.txt");
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+"day9b.txt",true,"\t");
			TableUtils.findAllNumericalColumns(vt);
			
			// Create a dataset, from all FLOAT (numerical) columns, standard centering and scaling is applied
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			// Alternatively, one can not to do any scaling
			//VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
			
			// Compute simple statistics for the dataset (needed to estimate the total percentage of variance explained)
			vd.calcStatistics();
			
			// Computing elastic curve
			ElmapAlgorithm elmap = new ElmapAlgorithm();
			// Last argument - the number of the algorithm in the file (section named "#ALG12" will be used for computation)
			elmap.grid = ElmapAlgorithm.computeElasticGrid(vd,INI_FILE,12);
			// Saving to the vem file (second argument is the name of the dataset - vet and ved files)
			elmap.grid.saveToFile(folder+"day9b.vem","day9b");
			
			// Project data points onto the curve
			ElmapProjection proj = new ElmapProjection();
			proj.setElmap(elmap);
			proj.setDataSet(vd);
			VDataSet vd_proj = proj.getProjectedDataset();
			
			// Saved projected data to a dat file
			VDatReadWrite.saveToVDatFile(vd_proj, folder+"day9_proj.dat");
			
			
			// Finally, estimate how much explained variance was improved

			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd); pca.calcBasis(1);
			float variance = vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion;
			double d[] = pca.calcDispersionsRelative(variance);
			System.out.println("Dataset variance:\t"+variance);
			System.out.println("Explained variance by first principal component:\t"+pca.calcDispersions()[0]+"\t"+d[0]*100+" %");
			
		    proj.elmap.grid.MakeNodesCopy();
		    proj.elmap.taxons = proj.elmap.grid.calcTaxons(vd);
		    float mseelmapt = proj.calculateMSEToProjection(vd);
		    float explainedvar = (1-mseelmapt/(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion));
		    System.out.println("Explained variance by principal curve:\t"+(variance-mseelmapt)+"\t"+explainedvar*100+" %");

			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	

}
