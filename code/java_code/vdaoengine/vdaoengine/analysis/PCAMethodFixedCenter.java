package vdaoengine.analysis;

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.visproc.*;


public class PCAMethodFixedCenter extends PCAMethod {
  public static void main(String[] args) {
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/GSDA/data/brain/brain.txt_AGEING_BRAIN_UP_f.dat");
    //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/GSDA/data/brain/test.dat");
    VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt,-1,false,true);
    vd.calcStatistics();
    for(int i=0;i<vd.coordCount;i++)
      System.out.print(vd.simpleStatistics.getMean(i)+"\t");

    PCAMethodFixedCenter pca = new PCAMethodFixedCenter();
    pca.setDataSet(vd);
    pca.calcBasis(3);
    double d[] = pca.calcDispersions();
    System.out.println(pca.getBasis());

    for(int i=0;i<d.length;i++)
      System.out.println(d[i]);
  }
  
  public int calculateAllPC_BSDimension(float[][] Dat, double[][] Vectors, double[] Shift, double totalDispersion){

		int res = -1;
		
		float bsd[] = VSimpleFunctions.getBrokenStickDistribution(Dat[0].length);
		
		int dimen = Dat[0].length;
		int pointNum = Dat.length;
		double dDat[][] = new double[dimen][pointNum];

		for(int i=0;i<dimen;i++) for(int j=0;j<pointNum;j++) dDat[i][j]=Dat[j][i];

		for(int i=0;i<dimen;i++){
		        double sh = 0, sw = 0;
		        for(int j=0;j<getDataSet().pointCount;j++){
		                if(!Double.isNaN(Dat[j][i]))
		                if(getDataSet().weighted){
		                        sh+=Dat[j][i]*getDataSet().weights[j];
		                        sw+=getDataSet().weights[j];
		                }else{
		                        sh+=Dat[j][i];
		                        sw+=1;
		                }
		        }
		        Shift[i]=sh/sw;
		}

		if(verboseMode)
			System.out.print("PCA ");
		for(int i=0; i<dimen; i++){
			res = i;
			if(verboseMode)
				System.out.print("\n"+i+" ");
			double Comp[] = VVectorCalc.randomUnit(dimen);
			calculateFirstPC(dDat,Comp,Shift);
			Vectors[i] = Comp;
			float projs[] = new float[pointNum];
			for(int j=0; j<pointNum; j++){
				double t=0, no = 0,d=0;
				for(int k=0; k<dimen; k++) d+=Comp[k]*Comp[k];
				for(int k=0; k<dimen; k++) if(!Double.isNaN(dDat[k][j])) t+=(dDat[k][j])*Comp[k]/d;
				for(int k=0; k<dimen; k++) dDat[k][j] = dDat[k][j]-t*Comp[k];
				for(int k=0; k<dimen; k++) Comp[k]/=Math.sqrt(d);
				projs[j] = (float)t;
			}
			float stdv = (float)(VSimpleFunctions.calcDispersion(projs)/(totalDispersion*totalDispersion));
			System.out.print("\t"+stdv+"\t"+bsd[i]);
			if(stdv<bsd[i]) break;
		}
		if(verboseMode)
			System.out.println();

		return res;
		
		}
  

  public void calculateFirstPC(double[][] dDat, double[] Vector, double[] Shift){
  double eps = globalSettings.epsOnCalculatingVectorComp;

  int dimen = Vector.length;
  int pointNum = dDat[0].length;

  double MY[] = new double[dimen];
  double Ord1[] = new double[dimen];

  for (int i=0; i<dimen; i++)
   {
   MY[i]=Vector[i];
   Shift[i] = 0;
   }
  int j=0;
  double e1=Double.MAX_VALUE, e2=Double.MAX_VALUE, e=Double.MAX_VALUE;
  double sy=0;
  while (e>eps)
    {
    PCIteration(dDat,Vector);
    e1=0; e2=0; e = 0;
    for(int i=0;i<dimen;i++){
    sy+=Vector[i]*Vector[i];
    //e1+=(MY[i]-Vector[i])*(MY[i]-Vector[i]);
    //e2+=(MB[i]-Shift[i])*(MB[i]-Shift[i]);
    e1 = java.lang.Math.max(java.lang.Math.abs(MY[i]-Vector[i]),e1);
    //if (java.lang.Math.abs(MY[i]-Vector[i])>e1) e1 = java.lang.Math.abs(MY[i]-Vector[i]);
    //if (java.lang.Math.abs(MB[i]-Shift[i])>e2) e2 = java.lang.Math.abs(MB[i]-Shift[i]);
    }
    //e1/=sy;
    //e2/=sb;
    e = e1;
    //System.out.println(e);
    for (int i=0; i<dimen; i++)
      {
      MY[i]=Vector[i];
      }
    j++;
    if (j>globalSettings.MAX_NUM_ITER){
       e1=0;
       e2=0;
       e=0;
       if(verboseMode)
	       System.out.println("Maximum number of iterations exceeded!");
       }
    }

  //System.out.println("["+Vector[0]+"\t"+Vector[1]+"\t"+Vector[2]+"...]");
  
  //Ord1[0]=1;
  double sp=0;
  //for (int i=0; i<dimen; i++) sp+=Vector[i]*Ord1[i];
  
  tempProjections = computeProjections(dDat, Vector, dimen, getDataSet().pointCount);
  for (int i=0; i<getDataSet().pointCount; i++) sp+=tempProjections[i];
  if (sp<0){
	  for (int i=0; i<dimen; i++) Vector[i]*=-1;
	  tempProjections = computeProjections(dDat, Vector, dimen, getDataSet().pointCount);
  }
  
  //System.out.println("["+Vector[0]+"\t"+Vector[1]+"\t"+Vector[2]+"...]");
  
  }
  

  public void PCIteration(double[][] dDat, double[] Vector){
  int dimen = Vector.length;
  int pointNum = dDat[0].length;
  double X[] = new double[pointNum];
  Double NaN = new Double(Double.NaN);

  if(getDataSet().hasGaps)
  for (int i=0; i<pointNum; i++)
    {
    double s1=0, s2=0;
    for (int j=0;j<dimen; j++)
      {
      if(!Double.isNaN(dDat[j][i])){
         s1=s1+(dDat[j][i])*Vector[j];
         s2=s2+Vector[j]*Vector[j];
      }
      }
    X[i]=s1/s2;
    }
  else
    for (int i=0; i<pointNum; i++)
      {
      double s1=0, s2=0;
      for (int j=0;j<dimen; j++)
        {
           s1=s1+(dDat[j][i])*Vector[j];
           s2=s2+Vector[j]*Vector[j];
        }
      X[i]=s1/s2;
      }

  if(!getDataSet().hasGaps)
  for (int j=0;j<dimen;j++)
    {
    double a11=0, a12=0, a21=0, a22=pointNum, b1=0, b2=0;

      if(!getDataSet().weighted){
              a11=0; a12=0; a21=0; b1=0; b2=0;
              a22 = getDataSet().pointCount;
              for (int i=0; i<pointNum; i++)
                  {
                      a11+=X[i]*X[i];
                      a12+=X[i];
                      b1+=dDat[j][i]*X[i];
                      b2+=dDat[j][i];
                      }
      }else{
              a11=0; a12=0; a21=0; a22=0; b1=0; b2=0;
              for (int i=0; i<pointNum; i++)
                  {
                      a11+=X[i]*X[i]*getDataSet().weights[i];
                      a12+=X[i]*getDataSet().weights[i];
                      a22+=getDataSet().weights[i];
                      b1+=dDat[j][i]*X[i]*getDataSet().weights[i];
                      b2+=dDat[j][i]*getDataSet().weights[i];
                      }
      }

    a21 = a12;
    //double det=a11*a22-a12*a21;
    //double det1=b1*a22-b2*a12;
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
                        a11+=X[i]*X[i];
                        a12+=X[i];
                        b1+=dDat[j][i]*X[i];
                        b2+=dDat[j][i];
                    }
        }else{
                a11=0; a12=0; a21=0; a22=0; b1=0; b2=0;
                for (int i=0; i<pointNum; i++)
                  if(!Double.isNaN(dDat[j][i]))
                    {
                        a11+=X[i]*X[i]*getDataSet().weights[i];
                        a12+=X[i]*getDataSet().weights[i];
                        a22+=getDataSet().weights[i];
                        b1+=dDat[j][i]*X[i]*getDataSet().weights[i];
                        b2+=dDat[j][i]*getDataSet().weights[i];
                        }
        }

      a21 = a12;
      double det=a11*a22-a12*a21;
      double det1=b1*a22-b2*a12;
      Vector[j]=det1/det;
    }


  double d = vdaoengine.utils.VVectorCalc.Norm(Vector);
  for(int i=0;i<Vector.length;i++) Vector[i]/=d;
  }

  public double[] calcDispersions(){
    double res[] = null;
    double res2[] = null;
    VLinearBasis lb = linBasis;

    if(getDataSet()!=null){

      res = new double[calculatedVectorsNumber];
      res2 = new double[calculatedVectorsNumber];

      float n = 0f, st = 0f, st2 = 0f, t=0f;
      float projs[][] = new float[getDataSet().pointCount][calculatedVectorsNumber];

      for(int j=0; j<getDataSet().pointCount; j++){
        projs[j] = lb.project(getDataSet().getVector(j));
        if(!getDataSet().weighted) n+=1; else n+=getDataSet().weights[j];
        for(int k=0;k<calculatedVectorsNumber;k++){
          if(!getDataSet().weighted){
                res[k]+=projs[j][k];
                res2[k]+=projs[j][k]*projs[j][k];
          }else{
                res[k]+=projs[j][k]*getDataSet().weights[j];
                res2[k]+=projs[j][k]*projs[j][k]*getDataSet().weights[j];
          }
        }
       }
     for(int k=0;k<calculatedVectorsNumber;k++){
       res[k]/=n;
       res2[k] = res2[k]/n; //- res[k]*res[k];
     }
    }
    return res2;
  }

}