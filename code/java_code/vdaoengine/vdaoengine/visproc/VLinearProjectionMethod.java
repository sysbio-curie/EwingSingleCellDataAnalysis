package vdaoengine.visproc;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.data.*;
import vdaoengine.visproc.*;
import vdaoengine.utils.*;
import vdaoengine.image.*;


public class VLinearProjectionMethod implements VLinearProjection{

VDataSet dataset;
public int calculatedVectorsNumber;
int dimension;
public float rotateXangle = 0f;
public VLinearBasis linBasis;
public VDataImage image;

public VLinearProjectionMethod(){
//System.out.println("We are in VLinearProjectionMethod constructor");
image = new VDataImage();
}

public void setDataSet(VDataSet Data){
dataset = Data;
image.imageActual = false;
image.name = new String(Data.name);
}

public void setData(double x[][]){
dataset = new VDataSet();
dataset.pointCount = x.length;
dataset.coordCount = x[0].length;
dataset.massif = new float[dataset.pointCount][dataset.coordCount];
for(int k=0;k<dataset.coordCount;k++)
	for(int i=0;i<dataset.pointCount;i++)
		dataset.massif[i][k] = (float)x[i][k];
image.imageActual = false;
image.name = new String("no name");
}

public void setDataTransposed(double x_transposed[][]){
dataset = new VDataSet();
dataset.pointCount = x_transposed[0].length;
dataset.coordCount = x_transposed.length;
dataset.massif = new float[dataset.pointCount][dataset.coordCount];
for(int k=0;k<dataset.coordCount;k++)
	for(int i=0;i<dataset.pointCount;i++)
		dataset.massif[i][k] = (float)x_transposed[k][i];
image.imageActual = false;
image.name = new String("no name");
}


public void setData(float x[][]){
dataset = new VDataSet();
dataset.pointCount = x.length;
dataset.coordCount = x[0].length;
dataset.massif = x;
image.imageActual = false;
image.name = new String("no name");
}


public VDataSet getDataSet(){
return dataset;
}

public VObjectDescriptorSet getDescriptors(){
return dataset.getDescriptors();
}

public VDataImage getDataImage(){
return image;
}

public VDataImage updateDataImage(){
if (!image.imageActual){
  image.elements.clear();
  constructImageElement();
  image.imageActual = true;
  }
return image;
}

public void constructImageElement(){
VDataPointSet set = new VDataPointSet("Data points: "+dataset.name);
set.setDescriptorSet(dataset.getDescriptors());
float v[];
for(int i=0; i<dataset.pointCount; i++){
    v = dataset.getVector(i);
    int ID = dataset.getVectorID(i);
    double vd[] = new double[v.length];
    for(int j=0;j<v.length;j++) vd[j] = v[j];
    //for(int j=0;j<vd.length;j++)
    //   System.out.print(vd[j]+" ");
    //System.out.println();
    double r[] = linBasis.project(vd,3);
    if(rotateXangle!=0f) r = Algorithms.rotateVectorX(r,rotateXangle);
    //for(int j=0;j<r.length;j++)
    //   System.out.print(r[j]+" ");
    //System.out.println();
    set.addPoint(ID,r);
    }
image.addElement(set);
}

public float[][] get3DFMassif(){
// Do some!
return null;
}

public VDataSet getProjectedDataset(){
VDataSet proj = new VDataSet();
proj.pointCount = getDataSet().pointCount;
proj.coordCount = calculatedVectorsNumber;
proj.massif = new float[proj.pointCount][proj.coordCount];

proj.table = getDataSet().table;

for(int i=0;i<getDataSet().pointCount;i++){
  float x[] = getDataSet().getVector(i);
  float y[] = this.projectionFunction(x);
  for(int j=0;j<proj.coordCount;j++) proj.massif[i][j] = y[j];
}
return proj;
}

public VDataSet getProjectedDataset(VDataSet vd){
VDataSet proj = new VDataSet();
proj.pointCount = vd.pointCount;
proj.coordCount = calculatedVectorsNumber;
proj.massif = new float[proj.pointCount][proj.coordCount];

proj.table = vd.table;

for(int i=0;i<vd.pointCount;i++){
  float x[] = vd.getVector(i);
  float y[] = this.projectionFunction(x);
  for(int j=0;j<proj.coordCount;j++) proj.massif[i][j] = y[j];
}
return proj;
}



public void setBasis(VLinearBasis Basis){
linBasis = Basis;
}

public VLinearBasis getBasis(){
return linBasis;
}

public double[] projectionFunction(double[] vec){
  /*if(getDataSet().hasGaps)
    return linBasis.project(vec);
  else*/
    return linBasis.project(vec);
}

public float[] projectionFunction(float[] vec){
  /*if(getDataSet().hasGaps)
    return linBasis.project(vec);
  else*/
    return linBasis.project(vec);
}


public void calcBasis(int nvec){
recalculateBasis(dataset,nvec);
calculatedVectorsNumber = nvec;
image.imageActual = false;
}

public double[] calcDispersions(){
  double res[] = null;
  double res2[] = null;
  VLinearBasis lb = linBasis;

  if(dataset!=null){

    res = new double[calculatedVectorsNumber];
    res2 = new double[calculatedVectorsNumber];

    float n = 0f, st = 0f, st2 = 0f, t=0f;
    float projs[][] = new float[dataset.pointCount][calculatedVectorsNumber];

    for(int j=0; j<dataset.pointCount; j++){
      projs[j] = lb.project(dataset.getVector(j));
      if(!dataset.weighted) n+=1; else n+=dataset.weights[j];
      for(int k=0;k<calculatedVectorsNumber;k++){
        if(!dataset.weighted){
              res[k]+=projs[j][k];
              res2[k]+=projs[j][k]*projs[j][k];
        }else{
              res[k]+=projs[j][k]*dataset.weights[j];
              res2[k]+=projs[j][k]*projs[j][k]*dataset.weights[j];
        }
      }
     }
   for(int k=0;k<calculatedVectorsNumber;k++){
     res[k]/=n;
     res2[k] = res2[k]/n - res[k]*res[k];
   }
  }
  return res2;
}

public double[] calcDispersionsRelative(double total_disp){
  double d[] = calcDispersions();
  for(int i=0;i<calculatedVectorsNumber;i++){
    d[i]/=total_disp;
  }
  return d;
}

protected void recalculateBasis(VDataSet Data, int nvec){
}

public double[] projectFromInToOut(double[] vec){
  return getBasis().projectFromInToOut(vec);
}

public float[] projectFromInToOut(float[] vec){
  return getBasis().projectFromInToOut(vec);
}

public float calculateMSEToProjection(VDataSet vd){
  float mse = 0f;
  float x2 = 0f;
  for(int i=0;i<vd.pointCount;i++){
    float x[] = vd.getVector(i);
    float p[] = projectionFunction(x);
    float xp[] = projectFromInToOut(p);
    float d = 0;
    if(getDataSet().hasGaps)
      d = (float)VVectorCalc.SquareEuclDistanceGap(x,xp);
    else
      d = (float)VVectorCalc.SquareEuclDistance(x,xp);
    x2 += d;
  }
  x2 /= vd.pointCount;
  mse = (float)Math.sqrt(x2);
  return mse;
  }


}