package vdaoengine.utils;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.lang.Math.*;

public class VLinearBasis {

public int spaceDimension;
public int numberOfBasisVectors;
public double[] a0;
public double[][] basis;
public boolean isOrthogonal = false;
public boolean isNormalized = false;

public VLinearBasis(){

}

public VLinearBasis(int n, int numVec){
setDimensions(n,numVec);
}

public void setDimensions(int spaceDim, int numVec){
spaceDimension = spaceDim;
numberOfBasisVectors = numVec;
basis = new double[numVec][spaceDimension];
a0 = new double[spaceDim];
}


public void set(double[] Centr, double[][] basVectors, boolean isOrth){
for(int i=0;i<spaceDimension;i++) {
  a0[i] = Centr[i];
  }
isNormalized = true;
for(int j=0;j<numberOfBasisVectors;j++){
double r = 0;
for(int i=0;i<spaceDimension;i++){
  basis[j][i] = basVectors[j][i];
  r+=basis[j][i]*basis[j][i];
  }

//for(int i=0;i<spaceDimension;i++){
//  if(i==j) basis[j][i]=1.0d;
//  r+=basis[j][i]*basis[j][i];
//  }

if (Math.abs(r-1)>1e-5) isNormalized = false;
}
isOrthogonal = isOrth;
}

//---------------------

public double[] project(double[] vec){
double res[] = new double[numberOfBasisVectors];

//System.out.println("isNO: "+isNormalized+" "+isOrthogonal);

if (isNormalized&&isOrthogonal){

for(int j=0;j<numberOfBasisVectors;j++)
for(int i=0;i<spaceDimension;i++)
  if(!Double.isNaN(vec[i]))
     res[j]+=(vec[i]-a0[i])*basis[j][i];

}
else if(isOrthogonal){

for(int j=0;j<numberOfBasisVectors;j++){
double b2 = 0;
for(int i=0;i<spaceDimension;i++) b2+=basis[j][i]*basis[j][i];
for(int i=0;i<spaceDimension;i++) if(!Double.isNaN(vec[i])) res[j]=+(vec[i]-a0[i])*basis[j][i];
res[j]/=b2;
}

}
else
  {
  }
return res;
}

public float[] project(float[] vec){
float res[] = new float[numberOfBasisVectors];

//System.out.println("isNO: "+isNormalized+" "+isOrthogonal);

if (isNormalized&&isOrthogonal){

for(int j=0;j<numberOfBasisVectors;j++)
for(int i=0;i<spaceDimension;i++)
  if(!Float.isNaN(vec[i]))
    res[j]+=(vec[i]-a0[i])*basis[j][i];

}
else if(isOrthogonal){

for(int j=0;j<numberOfBasisVectors;j++){
float b2 = 0;
for(int i=0;i<spaceDimension;i++) b2+=basis[j][i]*basis[j][i];
for(int i=0;i<spaceDimension;i++) if(!Float.isNaN(vec[i])) res[j]=+(float)((vec[i]-a0[i])*basis[j][i]);
res[j]/=b2;
}

}
else
  {
  }
return res;
}


public double[] project(double[] vec, int dim){
double res[] = new double[dim];
//System.out.println("Projecting "+vec.length);
double resf[] = project(vec);
//System.out.println(dim+"/"+numberOfBasisVectors);
int k = Math.min(dim,numberOfBasisVectors);
for(int i=0;i<k;i++) res[i]=resf[i];
return res;
}


public double[] projectN(double[] vec){
double res[] = new double[spaceDimension];
double b[] = project(vec);
res = projectFromInToOut(b);
return res;
}

public double[] projectFromInToOut(double[] b){
double res[] = new double[spaceDimension];
for(int i=0;i<spaceDimension;i++) res[i] = a0[i];
for(int j=0;j<Math.min(b.length,numberOfBasisVectors);j++)
for(int i=0;i<spaceDimension;i++)
  res[i]=+basis[j][i]*b[j];
return res;
}

public float[] projectFromInToOut(float[] b){
float res[] = new float[spaceDimension];
for(int i=0;i<spaceDimension;i++) res[i] = (float)a0[i];
for(int i=0;i<spaceDimension;i++)
  for(int j=0;j<Math.min(b.length,numberOfBasisVectors);j++)
    res[i]+=(float)(basis[j][i]*b[j]);
return res;
}


public String toString(){
StringBuffer s = new StringBuffer();
s.append("Shift: ");
for (int i = 0; i < a0.length; i++) s.append(a0[i]+" ");
s.append("\n");

for(int j=0; j<numberOfBasisVectors;j++){
s.append("Vector "+(j+1)+":");
for (int i = 0; i < spaceDimension; i++) s.append(basis[j][i]+" ");
s.append("\n");}

return s.toString();
}

}