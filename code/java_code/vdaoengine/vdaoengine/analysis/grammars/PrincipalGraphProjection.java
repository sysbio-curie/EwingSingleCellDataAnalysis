package vdaoengine.analysis.grammars;

import java.util.*;
import java.io.*;
import vdaoengine.data.VDataSet;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.visproc.*;
import vdaoengine.image.*;

public class PrincipalGraphProjection /*implements VProjection*/{

/*public VDataImage image = new VDataImage();
public Graph graph;	
public VDataSet data;
public static int projectionType = Graph.PROJECTION_CLOSEST_POINT;

  public double[] projectionFunction(double[] v){
    double r[] = new double[3];
    float vf[] = new float[v.length];
    for(int i=0;i<v.length;i++) vf[i] = (float)v[i];
    float vf2[] = graph.projectPoint(vf,projectionType);
    for(int i=0;i<3;i++) r[i] = (double)vf2[i];
    return r;
  }

  public double[] projectionFunctionD(float[] v){
    double r[] = new double[3];
    float vf[] = graph.projectPoint(v,projectionType);
    for(int i=0;i<3;i++) r[i] = (double)vf[i];
    return r;
  }

  public float[] projectionFunction(float[] v){
	if(elmap.grid==null){
		System.out.println("ERROR!!! grid = null");
	}
    if(getDataSet().hasGaps)
      return graph.projectPointGap(v,projectionType);
    else
      return graph.projectPoint(v,projectionType);
  }

  public float[][] get3DFMassif(){
    // Do some!
    return null;
  }

  public void setDataSet(VDataSet Data){
    data = Data;
  }

  public VDataSet getDataSet(){
    return data;
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
  VDataSet dataset = getDataSet();
  VDataPointSet set = new VDataPointSet("Data points: "+dataset.name);
  set.setDescriptorSet(dataset.getDescriptors());
  float v[];
  for(int i=0; i<dataset.pointCount; i++){
      v = dataset.getVector(i);
      int ID = dataset.getVectorID(i);
      //if(rotateXangle!=0f) r = Algorithms.rotateVectorX(r,rotateXangle);
      double r[] = projectionFunctionD(v);
      set.addPoint(ID,r);
      }
  image.addElement(set);
  }


  public void setGraph(Graph _graph){
    graph = _graph;
  }
  
  public void setDataset(VDataSet _data){
	    data = _data;
  }  

  public VDataSet getProjectedDataset(){
  VDataSet proj = new VDataSet();
  proj.pointCount = getDataSet().pointCount;
  proj.coordCount = 3;
  proj.massif = new float[proj.pointCount][proj.coordCount];
  proj.table = getDataSet().table;
  for(int i=0;i<getDataSet().pointCount;i++){
    float x[] = getDataSet().getVector(i);
    float y[] = this.projectionFunction(x);
    for(int j=0;j<proj.coordCount;j++) proj.massif[i][j] = y[j];
  }
  return proj;
  }

  public double[] projectFromInToOut(double[] vec){
    float f[] = Graph.projectFromInToOut((float)(vec[0]),(float)(vec[1]));
    double fd[] = new double[f.length];
    for(int i=0;i<f.length;i++)
      fd[i] = (double)f[i];
    return fd;
  }

  public float[] projectFromInToOut(float[] vec){
    float f[] = null;
    f= Graph.projectFromInToOut(vec[0]);
    return f;
  }

  public float calculateMSEToProjection(VDataSet vd){
    float mse = 0f;
    float x2 = 0f;
    elmap.grid.MakeNodesCopy();
    for(int i=0;i<vd.pointCount;i++){
      float x[] = vd.getVector(i);
      float p[] = null;
      //p = projectionFunction(x);
      if(getDataSet().hasGaps)
    	  p = graph.projectPointGap(x, projectionType);
      else
    	  p = graph.projectPoint(x, projectionType);
      //float xp[] = projectFromInToOut(p);
      float xp[] = p;
      //float pcheck[] = projectionFunction(xp);
      //System.out.println(pcheck[0]-p[0]);
            
      float d = 0;
      if(getDataSet().hasGaps)
        d = (float)VVectorCalc.SquareEuclDistanceGap(x,xp);
      else
        d = (float)VVectorCalc.SquareEuclDistance(x,xp);
      x2 += d;
    }
    x2 /= vd.pointCount;
    //mse = (float)Math.sqrt(x2);
    mse = x2;
    return mse;
  }*/
  
  
}
