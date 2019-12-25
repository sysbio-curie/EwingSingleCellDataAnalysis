package vdaoengine.analysis.cluster;

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
import java.util.*;

public class VKMeansClusterization extends VClusteringProcedure {

Vector MemCentr;
public int iterationsNumber = 0;

  public VKMeansClusterization(int numberOfClusters) {
  setClusterNumber(numberOfClusters);
  MemCentr = new Vector();
  }

  public void doClusterization(VDataSet ds) {
  initialize(ds);
  double eps = Double.MAX_VALUE;
  iterationsNumber = 0;
  while (eps>clusterizationQuality)
    {
    //System.out.println("Iteration "+iterationsNumber);
    calculateClassListsFromCentroids(ds);
    rememberCentroidsPositions(ds);
    calculateCentroidsFromClassLists(ds,true);
    eps = calculateChangeInCentroidsPositions(ds);
    iterationsNumber++;
    }
  getRidOfSingles();
  calculateClassListsFromCentroids(ds);
  calculateCentroidsFromClassLists(ds,true);
  }

  public void initialize(VDataSet ds){
  assignRandomCentroids(ds);
  }

  public void assignRandomCentroids(VDataSet ds){
  centroids.clear();
  VStatistics stat = ds.calcStatistics();
  for(int i=0;i<numberOfClusters;i++)
    {
    //double cent[] = VVectorCalc.randomUnit(ds.getCoordCount());
    double cent[] = VVectorCalc.random(ds.getCoordCount());
    for(int j=0;j<cent.length;j++)
      cent[j]=stat.getMin(j)+cent[j]*(stat.getMax(j)-stat.getMin(j));
    centroids.addElement(cent);
    }
  }

  void rememberCentroidsPositions(VDataSet ds){
  MemCentr.clear();
  double centr[];
  for(int i=0;i<centroids.size();i++)
    {
    double pos[] = new double[ds.getCoordCount()];
    centr = (double[])centroids.elementAt(i);
    for(int j=0; j<ds.getCoordCount(); j++)
      pos[j] = centr[j];
    MemCentr.addElement(pos);
    }
  }

  double calculateChangeInCentroidsPositions(VDataSet ds){
  double d=0;
  double memc[];
  double centr[];
  for(int i=0;i<centroids.size();i++)
    {
    int ls[] = (int[])classLists.elementAt(i);
    if(ls.length!=0){
    centr = (double[])centroids.elementAt(i);
    memc = (double[])MemCentr.elementAt(i);
    if (Math.abs(VVectorCalc.Norm(centr))>1e-8)
    d += VVectorCalc.Distance(centr,memc)/VVectorCalc.Norm(centr);
    else
    d += VVectorCalc.Distance(centr,memc)/VVectorCalc.Norm(memc);
    }}
  d/=centroids.size();
  return d;
  }
}