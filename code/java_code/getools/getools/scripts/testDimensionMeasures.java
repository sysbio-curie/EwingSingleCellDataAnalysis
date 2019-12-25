package getools.scripts;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class testDimensionMeasures {

	public static void main(String[] args) {
		try{
			VDataTable vt = VDatReadWrite.LoadFromVDatFile("c:/datas/gsdatest/small1.dat");
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			
			/*Random r = new Random();
			float rvec1[] = VVectorCalc.randomUnitAllSpacef(vd.coordCount);
			float rvec2[] = VVectorCalc.randomUnitAllSpacef(vd.coordCount);
			float rvec3[] = VVectorCalc.randomUnitAllSpacef(vd.coordCount);
			
			System.out.println(VVectorCalc.ScalarMult(rvec1, rvec2));
			
			for(int i=0;i<vd.pointCount;i++){
				float f1 = r.nextFloat();
				float f2 = r.nextFloat();
				float f3 = r.nextFloat();
				float r1[] = VVectorCalc.Product_(rvec1, f1);
				float r2[] = VVectorCalc.Product_(rvec2, f2);
				float r3[] = VVectorCalc.Product_(rvec3, f3);
				//float x[] = VVectorCalc.Add_(r1, VVectorCalc.Add_(r2,r3));
				//float x[] = VVectorCalc.Add_(r1, r2);
				float x[] = r1;
				for(int j=0;j<x.length;j++)
					vd.massif[i][j] = (float)x[j]; 
			}
			
			VDatReadWrite.saveToVDatFile(vd, "c:/datas/gsdatest/temp.dat");*/
			
			System.out.println("Measures for z-values:"); calcMeasures(vd);
			VDataSet vdn = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
			System.out.println("\nMeasures without normalization:"); calcMeasures(vdn);
			VDataSet vdnr = VSimpleProcedures.randomizeDataset(vdn,10000);
			System.out.println("\nMeasures randomized:"); calcMeasures(vdnr);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void calcMeasures(VDataSet vd){
		vd.calcStatistics();
		PCAMethod pca = new PCAMethod(); 
		pca.setDataSet(vd);
		pca.calcBasis(3);
		double d[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
		System.out.println("L1-3\t"+d[0]+"\t"+d[1]+"\t"+d[2]);
		System.out.println("SPGAP\t"+d[0]/d[1]);
		pca.calcBasisUntilBSDimension(vd);
		System.out.println("DIM_BS\t"+pca.linBasis.numberOfBasisVectors);
		System.out.println("DIM_CHAVEZ\t"+VSimpleFunctions.calcChavezIntrinsicDimension(vd));
		System.out.println("CORRGRWEIGHT5\t"+VSimpleFunctions.calcMeanPairwiseCorrelation(vd, true, 0.5f));
		System.out.println("CORRGRWEIGHT8\t"+VSimpleFunctions.calcMeanPairwiseCorrelation(vd, true, 0.8f));
		
		PCAMethodFixedCenter pca1 = new PCAMethodFixedCenter();
		pca1.setDataSet(vd);
		pca1.calcBasis(3);
		d = pca1.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
		System.out.println("L1-3_SHIFT\t"+d[0]+"\t"+d[1]+"\t"+d[2]);
		System.out.println("SPGAP_SHIFT\t"+d[0]/d[1]);
	}

}
