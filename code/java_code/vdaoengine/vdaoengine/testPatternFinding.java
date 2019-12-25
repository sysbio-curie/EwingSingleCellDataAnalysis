package vdaoengine;

import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.*;
import java.util.*;


public class testPatternFinding {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			testPatternFinding("c:/datas/ewing/mirna/trianglepattern/ex_sim1000");
			testPatternFinding("c:/datas/ewing/mirna/trianglepattern/ex_sim20");
			testPatternFinding("c:/datas/ewing/mirna/trianglepattern/ex_sim50");
			testPatternFinding("c:/datas/ewing/mirna/trianglepattern/ex_sim50_noise10");
			testPatternFinding("c:/datas/ewing/mirna/trianglepattern/ex_real");			
			testPatternFinding("c:/datas/ewing/mirna/trianglepattern/ex_negative");
			//plot(fr(:,1),fr(:,2),'r-'); hold on; plot(fr(:,1),fr(:,3),'k-'); plot(fr(:,1),fr(:,4),'b-'); 

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testPatternFinding(String fn){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, false, "\t");
		vt.fieldTypes[0] = vt.NUMERICAL; vt.fieldTypes[1] = vt.NUMERICAL;
		VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
		
		float x[] = new float[vd.pointCount];
		float y[] = new float[vd.pointCount];
		for(int i=0;i<vd.pointCount;i++) {x[i]=vd.massif[i][0]; y[i]=vd.massif[i][1];} 
			
		//System.out.println(VSimpleFunctions.calcStandardDeviation(x)+"\t"+VSimpleFunctions.calcStandardDeviation(y));
		
		int numberOfPermutations = 10000;
		
		Date date = new Date();
		double pv = 0; int n = 1;
		for(int i=0;i<n;i++){
			double p = VSimpleFunctions.calcTrianglePatternPValue(x,y,-1f,numberOfPermutations);
			pv+=p;
		}
		System.out.println(fn+"\t"+(pv/n)+"("+((new Date()).getTime()-date.getTime())+")");
		
		//float nullDistribution[][] = calculatePermutationsPairs(x,y,numberOfPermutations);
		
		/*Random r = new Random();
		for(float b=-3f;b<+3f;b+=0.01){
			int countr = 0;
			for(int i=0;i<x.length;i++){
				if(-x[i]-y[i]+b>0) countr++;
			}
			int countnd = 0;
			for(int i=0;i<nullDistribution[0].length;i++){
				if(-nullDistribution[0][i]-nullDistribution[1][i]+b>0) countnd++;
			}
			
			// Calculating p-value
			int countpval = 0;
			for(int j=0;j<numberOfPermutations;j++){
				//System.out.println(j);
				int counttest = 0;
				for(int k=0;k<x.length;k++){
						int pn = r.nextInt(nullDistribution[0].length);
						if(-nullDistribution[0][pn]-nullDistribution[1][pn]+b>0) counttest++;
				}
				if((float)counttest/x.length>=(float)countr/x.length) countpval++;
			}
			System.out.println(b+"\t"+(float)(countr)/x.length+"\t"+(float)(countnd)/nullDistribution[0].length+"\t"+(float)countpval/numberOfPermutations);
		}*/
		
		
	}
	
	public static float[][] calculatePermutationsPairs(float x[],float y[],int numberOfPermutations){
		float nd[][]=new float[2][numberOfPermutations];
		Random r = new Random();
		for(int i=0;i<numberOfPermutations;i++){
			nd[0][i] = x[r.nextInt(x.length)];
			nd[1][i] = y[r.nextInt(y.length)];
			//System.out.println(nd[0][i]+"\t"+nd[1][i]);
		}
		return nd;
	}
	
	public static float[] estimateShiftPoint(float nd[][],int method){ // 0 - mean point, 1 - median point
		float sp[] = new float[2];
		if(method==0){
			sp[0] = calcMean(nd[0]);
			sp[1] = calcMean(nd[1]);
		}
		if(method==1){
			sp[0] = VSimpleFunctions.calcMedian(nd[0]);
			sp[1] = VSimpleFunctions.calcMedian(nd[1]);
		}
		return sp;
	}
	
	  public static float calcMean(float f[]){
		    float r = 0;
		    float x = 0;
		    float x2 = 0;
		    for(int i=0;i<f.length;i++){
		      x+=f[i];
		    }
		    return x/f.length;
		  }



}

