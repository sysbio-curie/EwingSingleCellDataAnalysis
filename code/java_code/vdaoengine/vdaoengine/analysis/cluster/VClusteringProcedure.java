package vdaoengine.analysis.cluster;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.util.*;
import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;

public abstract class VClusteringProcedure {

public Vector<double[]> centroids;
public Vector<int[]> classLists;

double intraClassDispersions[];
public int numberOfClusters = 0;
double clusterizationQuality = 0.001;
int dimension = 0;

  public VClusteringProcedure() {
  centroids = new Vector<double[]>();
  classLists = new Vector<int[]>();
  }
  public abstract void doClusterization(VDataSet ds);
  // ---------------------
  public void calculateCentroidsFromClassLists(VDataSet ds,boolean randmov){
  int ls[];
  double centr[];
  double mcentr[];
  dimension = ds.getCoordCount();
  for(int i=0; i<classLists.size(); i++)
    {
    ls = (int[])classLists.elementAt(i);
    centr = (double[])centroids.elementAt(i);
    mcentr = (double[])centroids.elementAt(i);
    for(int k=0;k<centr.length;k++)
      centr[k] = 0;
    for(int j=0;j<ls.length; j++)
      {
      VVectorCalc.Add(centr,ds.getVector(ls[j]));
      }
    if(ls.length>0)
      VVectorCalc.Mult(centr,1.0/ls.length);
    else{
      if(randmov){
      centr = VVectorCalc.random(ds.getCoordCount());
      for(int kk=0;kk<centr.length;kk++)
        centr[kk] = ds.simpleStatistics.getMin(kk)+centr[kk]*(ds.simpleStatistics.getMax(kk)-ds.simpleStatistics.getMin(kk));
      }else{
        centr = mcentr;
      }
    }
    }
  }
  // ---------------------
  public void calculateClassListsFromCentroids(VDataSet ds){
  double centr[];
  dimension = ds.getCoordCount();
  Vector vl[] = new Vector[centroids.size()];
  for(int i=0; i<centroids.size(); i++)
    vl[i] = new Vector();

  for(int k=0; k<ds.getPointCount(); k++)
  {
  int mini = -1;
  double mind = Double.MAX_VALUE;
  double d = 0;
  for(int i=0; i<centroids.size(); i++)
    {
    centr = (double[])centroids.elementAt(i);
    d = VVectorCalc.SquareDistance(centr,ds.getVector(k));
    if (d<mind) {mind = d; mini = i;}
    }
  vl[mini].addElement(new Integer(k));
  }
  classLists.clear();
  for(int i=0; i<centroids.size(); i++)
     {
     int ls[] = new int[vl[i].size()];
     for(int k=0; k<ls.length; k++)
       ls[k] = ((Integer)vl[i].elementAt(k)).intValue();
     classLists.addElement(ls);
     };
  }
  // ---------------------
  public void calculateIntraClassDispersion(VDataSet ds){
  double res=0;
  double centr[];
  dimension = ds.getCoordCount();
  intraClassDispersions = new double[classLists.size()];
  for(int i=0; i<classLists.size(); i++)
    {
    int ls[] = (int[])classLists.elementAt(i);
    centr = (double[])centroids.elementAt(i);
    double d1 = 0;
    for(int j=0;j<ls.length; j++)
      { d1+=VVectorCalc.SquareDistance(centr,ds.getVector(ls[j])); }
    if (ls.length>0)
    intraClassDispersions[i] = d1/ls.length;
    }
  }
  // ---------------------
  public double calculateAverageIntraClassStdDev(VDataSet ds){
  if ((intraClassDispersions==null)||(intraClassDispersions.length==0)) calculateIntraClassDispersion(ds);
  double res=0;
  dimension = ds.getCoordCount();
  int n = intraClassDispersions.length;
  for(int i=0; i<n; i++)
    {
    int ls[] = (int[])classLists.elementAt(i);
    res+=intraClassDispersions[i]*ls.length;
    }
  res = java.lang.Math.sqrt(res/ds.getPointCount());
  return res;
  }
  // ----------------------
  public void setClusterNumber(int n){
  numberOfClusters = n;
  }

  public void setClusterizationQuality(double d){
  clusterizationQuality = d;
  }

  public double getClusterizationQuality(){
  return clusterizationQuality;
  }

  public int[] getClassList(int n){
  return (int[])classLists.elementAt(n);
  }

  public int calculateClosestCentroid(float[] v){
  int mini = -1;
  double mind = Double.MAX_VALUE;
  double d = 0;
  double centr[];
  for(int i=0; i<centroids.size(); i++)
    {
    centr = (double[])centroids.elementAt(i);
    d = VVectorCalc.SquareDistance(centr,v);
    if (d<mind) {mind = d; mini = i;}
    }
  return mini;
  }

  public int calculateClosestCentroid(float[] v, Vector ExceptList){
  int mini = -1;
  double mind = Double.MAX_VALUE;
  double d = 0;
  double centr[];
  for(int i=0; i<centroids.size(); i++)
    {
    centr = (double[])centroids.elementAt(i);
    d = VVectorCalc.SquareDistance(centr,v);
    if (d<mind)
      {
      int found = -1;
      for(int j=0; j<ExceptList.size();j++)
        {
        int k=((Integer)ExceptList.elementAt(j)).intValue();
        if(k==i) found=1;
        }
      if(found==-1)
         {mind = d; mini = i;}
      }
    }
  return mini;
  }

  public void rearrangeClusterNumbers(){
  float  v[] = new float[dimension];
  Vector NewNumbers = new Vector();
  for (int i=0;i<centroids.size();i++)
    {
    int j=calculateClosestCentroid(v,NewNumbers);
    NewNumbers.addElement(new Integer(j));
    }
  Vector NewCentr = new Vector();
  for (int i=0;i<centroids.size();i++)
    NewCentr.addElement(centroids.elementAt(((Integer)NewNumbers.elementAt(i)).intValue()));
  centroids = NewCentr;
  }

  public void addCentroid(double f[]){
  centroids.addElement(f);
  }

  public void addCentroid(float f[]){
  double fd[] = new double[f.length];
  for(int i=0;i<f.length;i++)
    fd[i] = f[i];
  centroids.addElement(fd);
  }

  public void clearCentroids(){
  centroids.clear();
  }

  public double[] getCentroidVector(int n){
  return (double [])centroids.elementAt(n);
  }

  public float[] getCentroidVectorF(int n){
  double d[] = (double [])centroids.elementAt(n);
  float f[] = new float[d.length];
  for(int i=0;i<d.length;i++) f[i] = (float)d[i];
  return f;
  }

  public VClassifier getClassifier(int mode, VDataSet VD){
    VClassifier cl = new VClassifier();
    if(VD.getDescriptors()==null) VD.setDescriptors(new VObjectDescriptorSet());
    VObjectDescriptorSet dset = VD.getDescriptors();
    cl.prepareNiceDescriptors(centroids.size(),mode);
    int kn = 1;
    for(Iterator i=classLists.iterator();i.hasNext();){
      int k[] = (int[])i.next();
      Vector IDs = new Vector();
      for(int j=0;j<k.length;j++)
           IDs.addElement(new Integer(VD.getVectorID(k[j])));
      VObjectDescriptor ds = cl.getNiceDescriptor(kn-1);
      System.out.println(ds);
      cl.addNewClass(IDs,"Cluster"+kn,ds,dset);
      kn++;
    }
    return cl;
  }

  public void getRidOfEmptyClusters(){
    Vector<double[]> centroids_ = new Vector<double[]>();
    Vector<int[]> classLists_ = new Vector<int[]>();
    for(int i=0;i<centroids.size();i++){
      int ls[] = (int[])classLists.elementAt(i);
      if(ls.length!=0){
        centroids_.add(centroids.elementAt(i));
        classLists_.add(classLists.elementAt(i));
      }
    }
    centroids = centroids_;
    classLists = classLists_;
    numberOfClusters = centroids.size();
  }

  public void getRidOfSingles(){
    Vector centroids_ = new Vector();
    Vector classLists_ = new Vector();
    for(int i=0;i<centroids.size();i++){
      int ls[] = (int[])classLists.elementAt(i);
      if(ls.length>1){
        centroids_.add(centroids.elementAt(i));
        classLists_.add(classLists.elementAt(i));
      }
    }
    centroids = centroids_;
    classLists = classLists_;
    numberOfClusters = centroids.size();
  }
}