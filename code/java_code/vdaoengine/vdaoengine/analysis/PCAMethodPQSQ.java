package vdaoengine.analysis;

import java.util.Date;

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.visproc.*;


public class PCAMethodPQSQ extends PCAMethod {

  public PQSQPotential PQSQpotential = null;
  
  public long timeToSplitInIntervals = 0;
	
  public static void main(String[] args) {

	  /*
	   * Define accuracy for convergence testing
	   */
	  globalSettings.epsOnCalculatingVectorComp = 0.001f;
	  /*
	   * Define the number of intervals
	   */
	  PQSQPotential.numberOfIntervals = 5;
	  
	  /*
	   * First, make the dataset: it can be a simple tab-delimited file (LoadFromSimpleDatFile + findAllNumericalColumns) or VidaExpert dat file (LoadFromVDatFile)
	   */
	  
	  //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/GSDA/data/brain/brain.txt_AGEING_BRAIN_UP_f.dat");
    //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/GSDA/data/brain/test.dat");
	//VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/SubquadraticPotential/iris_outliers_names.txt.dat");
	 //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/SubquadraticPotential/test_medium1.txt.dat");
	  VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/SubquadraticPotential/test_medium1.txt",true,"\t");
	  TableUtils.findAllNumericalColumns(vt);
	 //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/SubquadraticPotential/iris_names.txt.dat");
	  
	   
    VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    vd.calcStatistics();

    /*
     * Define the potential (TrimmedLinear, Quadratic, Sqrt)
     */
    //PCAMethodPQSQ pca = new PCAMethodPQSQ();
    //pca.PQSQpotential = PQSQPotential.getTrimmedLinearPQSQPotential(vd.massif);
    //pca.PQSQpotential = PQSQPotential.getTrimmedQuadraticPQSQPotential(vd.massif);
    //pca.PQSQpotential = PQSQPotential.getTrimmedSqrtPQSQPotential(vd.massif);

    // For comparison with normal PCA, comment last two lines and uncomment this one
    PCAMethod pca = new PCAMethod();
    
    PCAMethod.verboseMode = false;


    /*
     * Compute 5 components, the result is: 
     * central point  is in pca.getBasis().a0, basis vectors are in pca.getBasis().basis, point projections are in pca.pointProjections)
     */
    
    pca.setData(vd.massif);
    Date tm = new Date();
    pca.calcBasis(5);
    //System.out.println("Time spent: "+((new Date()).getTime()-tm.getTime())+"/ time to split: "+pca.timeToSplitInIntervals);
    System.out.println("Time spent: "+((new Date()).getTime()-tm.getTime()));

    System.out.println(pca.getBasis());
    /*System.out.print("Point projections:\t");
    for(int i=0;i<vd.pointCount;i++)
    	System.out.print(pca.pointProjections[0][i]+"\t");
    System.out.println("\n");*/

    /*
     * TODO: This function should be better adapted to non-orthogonal basis + non-quadratic PQSQ-based variance
     * otherwise explained variances can be non-monotonously decreasing  
     */
    System.out.println("Total variance:\t"+vd.simpleStatistics.totalDispersion);
    System.out.print("Explained variance fractions:\t");
    double d[] = pca.calcDispersions();
    for(int i=0;i<d.length;i++) System.out.print(d[i]/vd.simpleStatistics.totalDispersion+"\t"); System.out.println();

    
  }
  
  
  public void recalculateBasis(VDataSet Data, int nvec){
	  linBasis = new VLinearBasis(Data.coordCount,nvec);
	  double Vectors[][] = new double[nvec][Data.coordCount];
	  double Shift[] = new double[Data.coordCount];
	  calculateAllPCPQSQ(nvec,Data.massif,Vectors,Shift);
	  linBasis.set(Shift,Vectors,true);
	  }

  
  public void calculateAllPCPQSQ(int nvec, float[][] Dat, double[][] Vectors, double[] Shift){
	  
	  int dimen = Dat[0].length;
	  int pointNum = Dat.length;
	  double dDat[][] = new double[dimen][pointNum];
	  pointProjections = new double[dimen][pointNum];
	  double Center[] = new double[dimen];

	  for(int i=0;i<dimen;i++) for(int j=0;j<pointNum;j++) dDat[i][j]=Dat[j][i];

	  for(int i=0;i<dimen;i++){
		  float x[] = new float[pointNum];
		  for(int j=0;j<pointNum;j++) x[j]=(float)dDat[i][j];
		  Shift[i] = PQSQpotential.getMean(x,i);
		  Center[i] = Shift[i]; 
	  }

	  if(verboseMode)
	  	System.out.print("PCA ");
	  
	  /* Computing components one by one
	   * */
	  for(int i=0; i<nvec; i++){
	  if(verboseMode)	
	  	System.out.print(i+" ");
	  //double Comp[] = VVectorCalc.randomUnit(dimen);
	  
	  /*
	   * Initialize by PC1
	   */
	  PCAMethod pca = new PCAMethod();
	  pca.setDataTransposed(dDat);
	  boolean verb = PCAMethod.verboseMode;
	  PCAMethod.verboseMode = false;
	  Date tm = new Date();
	  pca.calcBasis(1);
	  //System.out.println("Time for calc PC1 L2 = "+((new Date()).getTime()-tm.getTime()));
	  PCAMethod.verboseMode = verb;
	  double Comp[] = pca.getBasis().basis[0];
	  //System.out.println(pca.getBasis());
	  tempProjections = new double[getDataSet().pointCount];
	  for(int j=0;j<getDataSet().pointCount;j++){
		  double s1 = 0f;
		  double s2 = 0f;
		  for(int k=0;k<dimen;k++){
			  s1+=(dDat[k][j]-Center[k])*Comp[k];
			  s2+=Comp[k]*Comp[k];
		  }
		  tempProjections[j] = s1/s2;
	  }
	  
	  calculateFirstPC(dDat,Comp,Center);
	  
	  for(int j=0;j<pointNum;j++)
		  pointProjections[i][j] = tempProjections[j];
	  for(int k=0; k<dimen; k++) 
		  Vectors[i][k] = Comp[k];

	  tempProjections = new double[getDataSet().pointCount];
	  for(int j=0;j<getDataSet().pointCount;j++){
		  double s1 = 0f;
		  double s2 = 0f;
		  for(int k=0;k<dimen;k++){
			  s1+=(dDat[k][j]-Center[k])*Comp[k];
			  s2+=Comp[k]*Comp[k];
		  }
		  tempProjections[j] = s1/s2;
	  }
	  
	  if(i>0) for(int k=0; k<dimen; k++) Center[k]=0;
	  
	  for(int j=0; j<pointNum; j++){
	    for(int k=0; k<dimen; k++) dDat[k][j] = dDat[k][j]-Center[k]-tempProjections[j]*Comp[k];
	  }
	  
	  }
	  if(verboseMode)
	  	System.out.println();

  }
  
  public void calculateFirstPC(double[][] dDat, double[] Vector, double[] Shift){
	  double eps = globalSettings.epsOnCalculatingVectorComp;

	  int dimen = Vector.length;
	  int pointNum = dDat[0].length;

	  double MY[] = new double[dimen];
	  double MB[] = new double[dimen];
	  double Ord1[] = new double[dimen];

	  for (int i=0; i<dimen; i++)
	   {
	   MY[i]=Vector[i];
	   MB[i]=Shift[i];
	   }
	  int j=0;
	  double e1=Double.MAX_VALUE, e2=Double.MAX_VALUE, e=Double.MAX_VALUE;
	  double sy=0,sb=0;
	  Date tm = new Date();
	  while (e>eps)
	    {
	    PCIteration(dDat,Vector,Shift);
	    e1=0; e2=0; e = 0;
	    for(int i=0;i<dimen;i++){
	    sy+=Vector[i]*Vector[i];   sb+=Shift[i]*Shift[i];
	    e1 = java.lang.Math.max(java.lang.Math.abs(MY[i]-Vector[i]),e1);
	    e2 = java.lang.Math.max(java.lang.Math.abs(MB[i]-Shift[i]),e2);
	    }
	    e = java.lang.Math.max(e1,e2);
	    
	    for (int i=0; i<dimen; i++)
	      {
	      MY[i]=Vector[i];
	      MB[i]=Shift[i];
	      }
	    j++;
	    if(verboseMode){
	    	System.out.println("Iteration "+j+": "+e);
	    }
	    if (j>globalSettings.MAX_NUM_ITER){
	       e1=0;
	       e2=0;
	       e=0;
	       if(verboseMode)
	  	     System.out.println("Maximum number of iterations exceeded!");
	       }
	    }
	  //System.out.println("Time for calc PC1 = "+((new Date()).getTime()-tm.getTime()));

	  Ord1[0]=1;
	  double sp=0;
	  for (int i=0; i<dimen; i++) sp+=Vector[i]*Ord1[i];
	  if (sp<0) for (int i=0; i<dimen; i++) Vector[i]*=-1;

	  float d=0;
	  for(int k=0; k<dimen; k++) d+=Vector[k]*Vector[k];
	  for(int k=0; k<dimen; k++) Vector[k]/=Math.sqrt(d);

	  /*int intervals[][] = new int[dimen][pointNum];
	  for(int k=0;k<dimen;k++){
		    float xx[] = new float[pointNum];
		    for(int i=0;i<pointNum;i++)
		    	xx[i] = (float) (dDat[k][i]-Shift[k]-Vector[k]*tempProjections[i]);
		    intervals[k] = PQSQpotential.getIntervals(xx,0,k);
	  }
	  
	  
	  tempProjections = new double[getDataSet().pointCount];
	  for(j=0;j<getDataSet().pointCount;j++){
	  	  double s1 = 0f;
	  	  double s2 = 0f;
	  	  double as = 0f;
	  	  for(int k=0;k<dimen;k++){
	  		  as = (double)PQSQpotential.Acoefficients[k][intervals[k][j]];
	  		  s1+=as*(dDat[k][j]-Shift[k])*Vector[k];
	  		  s2+=as*Vector[k]*Vector[k];
	  	  }
	  	  tempProjections[j] = s1/s2;
	  }*/
  }
  
  
  public void PCIteration(double[][] dDat, double[] Vector, double[] Shift){
	  int dimen = Vector.length;
	  int pointNum = dDat[0].length;
	  Double NaN = new Double(Double.NaN);

	  /*
	   * First, distribute all points into intervals
	   */
	  Date tm = new Date();
	  int intervals[][] = new int[dimen][pointNum];
	  for(int k=0;k<dimen;k++){
		    float xx[] = new float[pointNum];
		    for(int i=0;i<pointNum;i++)
		    	xx[i] = (float) (dDat[k][i]-Shift[k]-Vector[k]*tempProjections[i]);
		    intervals[k] = PQSQpotential.getIntervals(xx,0,k);
	  }
	  //System.out.println("Time for iteration point1 = "+((new Date()).getTime()-tm.getTime()));
	  timeToSplitInIntervals+=((new Date()).getTime()-tm.getTime());

	  if(getDataSet().hasGaps)
	  for (int i=0; i<pointNum; i++)
	    {
	    double s1=0, s2=0, as=0;
	    for (int j=0;j<dimen; j++)
	      {
	      if(!Double.isNaN(dDat[j][i])){
	    	 as = (double)PQSQpotential.Acoefficients[j][intervals[j][i]];
	         s1=s1+(dDat[j][i]-Shift[j])*Vector[j];
	         s2=s2+Vector[j]*Vector[j];
	      }
	      }
	    tempProjections[i]=s1/s2;
	    }
	  else /* no gaps */
	    for (int i=0; i<pointNum; i++)
	      {
	      double s1=0, s2=0, as = 0;
	      for (int j=0;j<dimen; j++)
	        {
	    	   as = (double)PQSQpotential.Acoefficients[j][intervals[j][i]];
	           s1=s1+as*(dDat[j][i]-Shift[j])*Vector[j];
	           s2=s2+as*Vector[j]*Vector[j];
	        }
	      if(s2>0)
	    	  tempProjections[i]=s1/s2;
	      else
	    	  tempProjections[i]=0;
	      }
	  
	  //System.out.println("Time for iteration point2 = "+((new Date()).getTime()-tm.getTime()));

	  if(!getDataSet().hasGaps)
	  for (int j=0;j<dimen;j++)
	    {
	    double a11=0, a12=0, a21=0, a22=pointNum, b1=0, b2=0, as = 0;

	      if(!getDataSet().weighted){
	              a11=0; a12=0; a21=0; b1=0; b2=0;
	              a22 = getDataSet().pointCount;
	              for (int i=0; i<pointNum; i++)
	                  {
	            	  	  as = (double)PQSQpotential.Acoefficients[j][intervals[j][i]];
	            	  	  a11+=as*tempProjections[i]*tempProjections[i];
	                      //a12+=tempProjections[i];
	                      b1+=as*(dDat[j][i]-Shift[j])*tempProjections[i];
	                      //b2+=dDat[j][i];
	                      }
	      }else{
	              a11=0; a12=0; a21=0; a22=0; b1=0; b2=0;
	              for (int i=0; i<pointNum; i++)
	                  {    
	            	  	  as = (double)PQSQpotential.Acoefficients[j][intervals[j][i]];
	                      a11+=as*tempProjections[i]*tempProjections[i]*getDataSet().weights[i];
	                      //a12+=tempProjections[i]*getDataSet().weights[i];
	                      //a22+=getDataSet().weights[i];
	                      b1+=as*dDat[j][i]*tempProjections[i]*getDataSet().weights[i];
	                      //b2+=dDat[j][i]*getDataSet().weights[i];
	                      }
	      }

	      Vector[j]=b1/a11;  
	    }
	  else // with gaps
	    for (int j=0;j<dimen;j++)
	      {
	      double a11=0, a12=0, a21=0, a22=pointNum, b1=0, b2=0;

	        if(!getDataSet().weighted){
	                a11=0; a12=0; a21=0; b1=0; b2=0;
	                a22 = getDataSet().pointCount;
	                for (int i=0; i<pointNum; i++)
	                  if(!Double.isNaN(dDat[j][i]))
	                    {
	                        a11+=tempProjections[i]*tempProjections[i];
	                        a12+=tempProjections[i];
	                        b1+=dDat[j][i]*tempProjections[i];
	                        b2+=dDat[j][i];
	                    }
	        }else{
	                a11=0; a12=0; a21=0; a22=0; b1=0; b2=0;
	                for (int i=0; i<pointNum; i++)
	                  if(!Double.isNaN(dDat[j][i]))
	                    {
	                        a11+=tempProjections[i]*tempProjections[i]*getDataSet().weights[i];
	                        a12+=tempProjections[i]*getDataSet().weights[i];
	                        a22+=getDataSet().weights[i];
	                        b1+=dDat[j][i]*tempProjections[i]*getDataSet().weights[i];
	                        b2+=dDat[j][i]*getDataSet().weights[i];
	                        }
	        }

	      a21 = a12;
	      double det=a11*a22-a12*a21;
	      double det1=b1*a22-b2*a12;
	      double det2=a11*b2-a21*b1;
	      if(det!=0){
	      	Vector[j]=det1/det;
	      	Shift[j]=det2/det;
	      }else{
	      	Vector[j]=0;
	      	Shift[j]=0;
	      }
	    }


	  double d = vdaoengine.utils.VVectorCalc.Norm(Vector);
	  for(int i=0;i<Vector.length;i++) Vector[i]/=d;
	  
	  //System.out.println("Time for iteration point3 = "+((new Date()).getTime()-tm.getTime()));
	  
	  }
  
  
  
}