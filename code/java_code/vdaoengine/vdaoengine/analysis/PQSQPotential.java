package vdaoengine.analysis;

import java.util.Date;
import java.util.Vector;

import vdaoengine.globalSettings;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.utils.VMathFunction;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class PQSQPotential {
	
	public static int numberOfIntervals = 5;
	public static float amplitudeFactor = 1f;
	
	public float[][] intervals = null;
	private float[][] intervals_inf = null;
	public float[][] Acoefficients = null;  
	public float[][] Bcoefficients = null;
	public VMathFunction MajoratingFunction = null;
	public  float functionParameters[] = null;

	public static void main(String[] args) {
		try{

			VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/SubquadraticPotential/test_medium1.txt.dat");
		    VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
		    vd.calcStatistics(); vd.simpleStatistics.calcMedians();
		    
		    PQSQPotential.numberOfIntervals = 5;
		    PQSQPotential.amplitudeFactor = 0.5f;
		    //PQSQPotential l1 = getTrimmedLinearPQSQPotential(vd.massif);
		    //PQSQPotential l1 = getTrimmedXsquaredPlusXPQSQPotential(vd.massif);
		    PQSQPotential l1 = getTrimmedSqrtPQSQPotential(vd.massif);
		    //PQSQPotential l1 = getTrimmedQuadraticPQSQPotential(vd.massif);

		    //vd.massif[0][0] = 200; vd.calcStatistics(); 
		    
		    System.out.println("Mean value:");
		    Date tm = new Date();
		    float meanPQSQ[] = l1.getMean(vd.massif);
		    System.out.println("Time to compute PQSQ mean value = "+((new Date()).getTime()-tm.getTime()));
		    for(int i=0;i<vd.coordCount;i++) System.out.print(vd.simpleStatistics.getMean(i)+"\t"); System.out.println();
		    for(int i=0;i<vd.coordCount;i++) System.out.print(vd.simpleStatistics.getMedian(i)+"\t"); System.out.println();
		    for(int i=0;i<vd.coordCount;i++) System.out.print(meanPQSQ[i]+"\t"); System.out.println();

		    System.out.println("Variance:");
		    float variancePQSQ[] = l1.getVariance(vd.massif);
		    for(int i=0;i<vd.coordCount;i++) System.out.print(vd.simpleStatistics.getStdDev(i)*vd.simpleStatistics.getStdDev(i)+"\t"); System.out.println();
		    for(int i=0;i<vd.coordCount;i++) System.out.print(variancePQSQ[i]+"\t"); System.out.println();


		    System.out.println("Plotting the potential:");
		    float diff =0f;
		    for(int i=0;i<vd.pointCount;i++) System.out.print(vd.massif[i][0]+"\t"); System.out.println(); 
		    for(int i=0;i<vd.pointCount;i++) { System.out.print(l1.getValue(vd.massif[i][0],0)+"\t"); diff+=(l1.MajoratingFunction.getValue(vd.massif[i][0],l1.functionParameters)-l1.getValue(vd.massif[i][0],0));} System.out.println();
			System.out.println("Average diff = "+diff/vd.pointCount);
		    
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Function to define intervals for each coordinate of a dataset
	 */
	public void defineIntervals(float x[][]){
		
		intervals = new float[x[0].length][numberOfIntervals+1];
		intervals_inf = new float[x[0].length][numberOfIntervals+2];
		for(int k=0;k<x[0].length;k++){
			float minv = Float.POSITIVE_INFINITY;
			float maxv = -Float.POSITIVE_INFINITY;
			for(int i=0;i<x.length;i++){
				if(x[i][k]<minv) minv = x[i][k];
				if(x[i][k]>maxv) maxv = x[i][k];
			}
			float characteristic_distance = (maxv-minv)*amplitudeFactor;
			float delta = (float)Math.sqrt(characteristic_distance)/(float)numberOfIntervals;
			float xx = 0;
			for(int i=0;i<numberOfIntervals+1;i++){
				intervals[k][i] = (xx+i*delta)*(xx+i*delta);
				intervals_inf[k][i] = (xx+i*delta)*(xx+i*delta);
			}
			intervals_inf[k][numberOfIntervals+1] = Float.POSITIVE_INFINITY;
		}
	}

	/*
	 * Function to pre-compute A and B coefficients for PQSQ potential, based on a majorating function and interval definitions
	 */
	public void computeABCoefficients(VMathFunction f, float functionParameters[]){
		
		Acoefficients = new float[intervals.length][numberOfIntervals+1];
		Bcoefficients = new float[intervals.length][numberOfIntervals+1];

		for(int k=0;k<intervals.length;k++){
			for(int i=0;i<intervals[0].length-1;i++){
				float xk = intervals[k][i];
				float xk1 = intervals[k][i+1];
				float fk = f.getValue(xk,functionParameters);
				float fk1 = f.getValue(xk1,functionParameters);;
				Acoefficients[k][i] = (fk-fk1)/(xk*xk-xk1*xk1);
				Bcoefficients[k][i] = (fk1*xk*xk-fk*xk1*xk1)/(xk*xk-xk1*xk1);
			}
			Acoefficients[k][intervals[0].length-1] = 0f;
			Bcoefficients[k][intervals[0].length-1] = f.getValue(intervals[k][intervals[0].length-1],functionParameters);
		}
	}
	
	public void computeABCoefficients(){	
		computeABCoefficients(MajoratingFunction, functionParameters);
	}
	
	/*
	 * Value of PQSQ Potential
	 */
	public float getValue(float x, int coordinate){
		float pqsq = 0;
		int interval = getInterval(x,coordinate);
		pqsq = Acoefficients[coordinate][interval]*x*x+Bcoefficients[coordinate][interval];
		return pqsq;
	}
	/*
	 * Convert a number to an interval
	 */
	public int getInterval(float x, int coordinate){
		int interval = -1;
		x = Math.abs(x);
		for(int i=0;i<intervals_inf[0].length-1;i++)
			if((intervals_inf[coordinate][i]<=x)&&(x<intervals_inf[coordinate][i+1])){
				interval = i;
				break;
			}
		return interval;
	}
	/*
	 * Convert massif to intervals
	 */
	public int[] getIntervals(float x[], float shift, int coordinate){
		int intervals[] = new int[x.length];
		for(int j=0;j<x.length;j++){
		float xx = Math.abs(x[j]-shift);
		for(int i=0;i<intervals_inf[0].length-1;i++)
			if((intervals_inf[coordinate][i]<=xx)&&(xx<intervals_inf[coordinate][i+1])){
				intervals[j] = i;
				break;
			}
		}
		return intervals;
	}
	/*
	 * 
	 */
	public float getMean(float x[], int coordinate){
		float mean = 0;
		int count = 0;
		while(count<100){
			int intervals[] = getIntervals(x,mean,coordinate);
			float mean1 = mean;
			float x1 = 0f;
			float x2 = 0f;
			for(int i=0;i<x.length;i++){
				float as = Acoefficients[coordinate][intervals[i]];
				x1+=as*x[i];
				x2+=as;
			}
			mean = x1/x2;
			if(Math.abs((mean-mean1)/(mean1+0.001))<globalSettings.epsOnCalculatingVectorComp){
				mean = mean1;
				break;
			}
			
			count++;
			if(count==99) System.out.println("No convergence for PQSQ after 100 iterations!");
		}
		return mean;
	}
	
	
	
	public float getVariance(float x[], int coordinate){
		float variance = 0;
		float mean = getMean(x,coordinate);
		for(int i=0;i<x.length;i++)
			variance+=getValue(x[i]-mean, coordinate);
		variance/=x.length;
		return variance;
	}
	
	public float[] getVariance(float x[][]){
		float variance[] = new float[x[0].length];
		for(int coordinate=0;coordinate<variance.length;coordinate++){
			int count = 0;
			float xx[] = new float[x.length];
			for(int i=0;i<x.length;i++) xx[i] = x[i][coordinate];
			variance[coordinate] = getVariance(xx,coordinate);
		}
		return variance;
	}
	
	
	public float[] getMean(float x[][]){
		float mean[] = new float[x[0].length];
		for(int coordinate=0;coordinate<mean.length;coordinate++){
			int count = 0;
			float xx[] = new float[x.length];
			for(int i=0;i<x.length;i++) xx[i] = x[i][coordinate];
			mean[coordinate] = getMean(xx,coordinate);
		}
		return mean;
	}
	
	
	/*
	 * Pre-configured trimmed linear PQSQ potential
	 */
	public static PQSQPotential getTrimmedLinearPQSQPotential(float x[][]){
		PQSQPotential pqsq = new PQSQPotential();
		pqsq.defineIntervals(x);
		pqsq.MajoratingFunction = new VSimpleFunctions(). new VAbsFunction();
		pqsq.computeABCoefficients(pqsq.MajoratingFunction, null);
		return pqsq;
	}
	/*
	 * Pre-configured quadratic linear PQSQ potential
	 */
	public static PQSQPotential getTrimmedQuadraticPQSQPotential(float x[][]){
		PQSQPotential pqsq = new PQSQPotential();
		pqsq.defineIntervals(x);
		pqsq.MajoratingFunction = new VSimpleFunctions(). new VQuadraticFunction();
		pqsq.computeABCoefficients(pqsq.MajoratingFunction, null);
		return pqsq;
	}
	/*
	 * x*x+x potential
	 */
	public static PQSQPotential getTrimmedXsquaredPlusXPQSQPotential(float x[][]){
		PQSQPotential pqsq = new PQSQPotential();
		pqsq.defineIntervals(x);
		pqsq.MajoratingFunction = new VSimpleFunctions(). new VPolynomialFunction();
		float pars[] = {0,1,1};
		pqsq.functionParameters = pars;
		pqsq.computeABCoefficients(pqsq.MajoratingFunction, pars);
		return pqsq;
	}
	/*
	 * Sqrt potential
	 */
	public static PQSQPotential getTrimmedSqrtPQSQPotential(float x[][]){
		PQSQPotential pqsq = new PQSQPotential();
		pqsq.defineIntervals(x);
		pqsq.MajoratingFunction = new VSimpleFunctions(). new VSqrtFunction();
		pqsq.computeABCoefficients(pqsq.MajoratingFunction, null);
		return pqsq;
	}
	
	
	


}
