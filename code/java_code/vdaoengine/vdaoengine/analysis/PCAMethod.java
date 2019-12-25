package vdaoengine.analysis;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.visproc.*;


public class PCAMethod extends VLinearProjectionMethod {
	
public static boolean verboseMode = true;
public double pointProjections[][] = null;
public double tempProjections[] = null;


public PCAMethod(){
//System.out.println("We are in the PCAMethod constructor");
}

public void recalculateBasis(VDataSet Data, int nvec){
linBasis = new VLinearBasis(Data.coordCount,nvec);
double Vectors[][] = new double[nvec][Data.coordCount];
double Shift[] = new double[Data.coordCount];
calculateAllPC(nvec,Data.massif,Vectors,Shift);
linBasis.set(Shift,Vectors,true);
}

public void calcBasisUntilBSDimension(VDataSet Data){
	
	Data.calcStatistics();
	double Vectors_[][] = new double[Data.coordCount][Data.coordCount];
	double Shift_[] = new double[Data.coordCount];
	int realDim = calculateAllPC_BSDimension(Data.massif,Vectors_,Shift_,Data.simpleStatistics.totalDispersion);
	//System.out.println("Dimension = "+realDim);

	double Vectors[][] = new double[realDim][Data.coordCount];
	double Shift[] = new double[Data.coordCount];
	for(int i=0;i<Data.coordCount;i++){
		Shift[i] = Shift_[i];
		for(int j=0;j<realDim;j++){
			Vectors[j][i] = Vectors_[j][i];
		}
	}

	linBasis = new VLinearBasis(Data.coordCount,realDim);	
	linBasis.set(Shift,Vectors,true);
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

	//System.out.print("PCA ");
	for(int i=0; i<dimen; i++){
		res = i;
		//System.out.print("\n"+i+" ");
		double Comp[] = VVectorCalc.randomUnit(dimen);
		calculateFirstPC(dDat,Comp,Shift);
		Vectors[i] = Comp;
		float projs[] = new float[pointNum];
		for(int j=0; j<pointNum; j++){
			double t=0, no = 0,d=0;
			for(int k=0; k<dimen; k++) d+=Comp[k]*Comp[k];
			for(int k=0; k<dimen; k++) if(!Double.isNaN(dDat[k][j])) t+=(dDat[k][j] - Shift[k])*Comp[k]/d;
			for(int k=0; k<dimen; k++) dDat[k][j] = dDat[k][j]-Shift[k]-t*Comp[k];
			for(int k=0; k<dimen; k++) Comp[k]/=Math.sqrt(d);
			projs[j] = (float)t;
		}
		float stdv = (float)(VSimpleFunctions.calcDispersion(projs)/(totalDispersion*totalDispersion));
		//System.out.print("\t"+stdv+"\t"+bsd[i]);
		if(stdv<bsd[i]) break;
		else
			if(Float.isNaN(stdv)) break;
	}
	//System.out.println();

	return res;
	
	}

public void calculateAllPC(int nvec, float[][] Dat, double[][] Vectors, double[] Shift){

int dimen = Dat[0].length;
int pointNum = Dat.length;
double dDat[][] = new double[dimen][pointNum];
pointProjections = new double[nvec][pointNum];

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
        if(sw==0) sw=1;
        Shift[i]=sh/sw;
}

if(verboseMode)
	System.out.print("PCA ");
for(int i=0; i<nvec; i++){
if(verboseMode)	
	System.out.print(i+" ");
double Comp[] = VVectorCalc.randomUnit(dimen);
calculateFirstPC(dDat,Comp,Shift);
Vectors[i] = Comp;

for(int j=0;j<pointNum;j++)
	  pointProjections[i][j] = tempProjections[j];

for(int j=0; j<pointNum; j++){
  double t=0, no = 0;
  //for(int k=0; k<dimen; k++) if(!Double.isNaN(dDat[k][j])) t+=(dDat[k][j] - Shift[k])*Comp[k]/d;
  for(int k=0; k<dimen; k++) dDat[k][j] = dDat[k][j]-Shift[k]-tempProjections[j]*Comp[k];
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
while (e>eps)
  {
  PCIteration(dDat,Vector,Shift);
  e1=0; e2=0; e = 0;
  for(int i=0;i<dimen;i++){
  sy+=Vector[i]*Vector[i];   sb+=Shift[i]*Shift[i];
  //e1+=(MY[i]-Vector[i])*(MY[i]-Vector[i]);
  //e2+=(MB[i]-Shift[i])*(MB[i]-Shift[i]);
  e1 = java.lang.Math.max(java.lang.Math.abs(MY[i]-Vector[i]),e1);
  e2 = java.lang.Math.max(java.lang.Math.abs(MB[i]-Shift[i]),e2);
  //if (java.lang.Math.abs(MY[i]-Vector[i])>e1) e1 = java.lang.Math.abs(MY[i]-Vector[i]);
  //if (java.lang.Math.abs(MB[i]-Shift[i])>e2) e2 = java.lang.Math.abs(MB[i]-Shift[i]);
  }
  //e1/=sy;
  //e2/=sb;
  e = java.lang.Math.max(e1,e2);
  //System.out.println(e);
  for (int i=0; i<dimen; i++)
    {
    MY[i]=Vector[i];
    MB[i]=Shift[i];
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

//Ord1[0]=1;
double sp=0;
//for (int i=0; i<dimen; i++) sp+=Vector[i]*Ord1[i];
//if (sp<0) for (int i=0; i<dimen; i++) Vector[i]*=-1;

float d=0;
for(int k=0; k<dimen; k++) d+=Vector[k]*Vector[k];
for(int k=0; k<dimen; k++) Vector[k]/=Math.sqrt(d);

tempProjections = computeProjections(dDat, Vector, dimen, getDataSet().pointCount);
for (int i=0; i<getDataSet().pointCount; i++) sp+=tempProjections[i];
if (sp<0){
	  for (int i=0; i<dimen; i++) Vector[i]*=-1;
	  tempProjections = computeProjections(dDat, Vector, dimen, getDataSet().pointCount);
}

}

public void PCIteration(double[][] dDat, double[] Vector, double[] Shift){
int dimen = Vector.length;
int pointNum = dDat[0].length;
tempProjections = new double[pointNum];
Double NaN = new Double(Double.NaN);

if(getDataSet().hasGaps)
for (int i=0; i<pointNum; i++)
  {
  double s1=0, s2=0;
  for (int j=0;j<dimen; j++)
    {
    if(!Double.isNaN(dDat[j][i])){
       s1=s1+(dDat[j][i]-Shift[j])*Vector[j];
       s2=s2+Vector[j]*Vector[j];
    }
    }
  tempProjections[i]=s1/s2;
  }
else
  for (int i=0; i<pointNum; i++)
    {
    double s1=0, s2=0;
    for (int j=0;j<dimen; j++)
      {
         s1=s1+(dDat[j][i]-Shift[j])*Vector[j];
         s2=s2+Vector[j]*Vector[j];
      }
    tempProjections[i]=s1/s2;
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
                    a11+=tempProjections[i]*tempProjections[i];
                    a12+=tempProjections[i];
                    b1+=dDat[j][i]*tempProjections[i];
                    b2+=dDat[j][i];
                    }
    }else{
            a11=0; a12=0; a21=0; a22=0; b1=0; b2=0;
            for (int i=0; i<pointNum; i++)
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
  Vector[j]=det1/det;
  Shift[j]=det2/det;
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
}

public static VDataTable PCAtable(VDataTable vt, int numOfComponents, boolean doScaling){
  VDataTable vtr = new VDataTable();
  int numOfStringFields = 0;
  for(int i=0;i<vt.colCount;i++)
    if(vt.fieldTypes[i]==vt.STRING)
      numOfStringFields++;
  vtr.fieldClasses = new String[numOfStringFields+numOfComponents];
  vtr.fieldDescriptions = new String[numOfStringFields+numOfComponents];
  vtr.fieldNames = new String[numOfStringFields+numOfComponents];
  vtr.fieldTypes = new int[numOfStringFields+numOfComponents];
  if(vt.fieldInfo!=null)
    vtr.fieldInfo = new String[numOfStringFields+numOfComponents][vt.fieldInfo[0].length];
  int k=0;
  for(int i=0;i<vt.colCount;i++)
    if(vt.fieldTypes[i]==vt.STRING){
      vtr.fieldClasses[k] = vt.fieldClasses[i];
      vtr.fieldNames[k] = vt.fieldNames[i];
      vtr.fieldDescriptions[k] = vt.fieldDescriptions[i];
      vtr.fieldTypes[k] = vt.fieldTypes[i];
      if(vt.fieldInfo!=null)
        vtr.fieldInfo[k] = vt.fieldInfo[i];
      k++;
    }
  for(int i=0;i<numOfComponents;i++){
    vtr.fieldNames[i+numOfStringFields] = "PC"+(i+1);
    vtr.fieldTypes[i+numOfStringFields] = vt.NUMERICAL;
  }
  vtr.rowCount = vt.rowCount;
  vtr.colCount = numOfStringFields+numOfComponents;
  vtr.stringTable = new String[vtr.rowCount][vtr.colCount];
  for(int i=0;i<vtr.rowCount;i++){
    k = 0;
    for(int j=0;j<vt.colCount;j++){
      if(vt.fieldTypes[j]==vt.STRING){
          vtr.stringTable[i][k] = vt.stringTable[i][j];
          k++;
      }
    }
  }

  VDataSet vd = null;
    if(doScaling)
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    else
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
  PCAMethod pca = new PCAMethod();
  pca.setDataSet(vd);
  pca.calcBasis(numOfComponents);
  VDataSet vdp = pca.getProjectedDataset();
  for(int i=0;i<vtr.rowCount;i++)
  for(int j=0;j<numOfComponents;j++){
    vtr.stringTable[i][j+numOfStringFields] = "" + vdp.massif[i][j];
  }
  return vtr;
}

public float calcMSE(VDataSet vd){
	float mse = 0f;
	VDataSet vdp = getProjectedDataset();
	for(int i=0;i<vd.pointCount;i++)
		mse+=VVectorCalc.SquareEuclDistanceGap(vd.getVector(i), projectFromInToOut(vdp.getVector(i)));
	mse/=vd.pointCount;
	return mse;
}

public float[] recoverMissingValues(float xn[]){
	float pint[] = projectionFunction(xn);
	float xnp[] = projectFromInToOut(pint);
	return xnp;
}

public double[] computeProjections(double dDat[][], double Vector[], int dimen, int pointCount){
	  double projs[] = new double[getDataSet().pointCount];
	  for(int j=0;j<pointCount;j++){
	  	  double s1 = 0f;
	  	  double s2 = 0f;
	  	  for(int k=0;k<dimen;k++){
	  		  s1+=dDat[k][j]*Vector[k];
	  		  s2+=Vector[k]*Vector[k];
	  	  }
	  	  projs[j] = s1/s2;
	  }
	  return projs;
}


}