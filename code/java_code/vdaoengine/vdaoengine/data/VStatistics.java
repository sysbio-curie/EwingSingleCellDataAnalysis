package vdaoengine.data;

import java.util.*;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.lang.Math;

public class VStatistics {

public float mins[];
public float maxs[];
public float means[];
public float stdevs[];
public float stdevs0[];
public float medians[];
public float skews[];
public int nums[];
public int dimension;
public int pointsNumber;
public float totalDispersion;
public float totalSDVtozero;

public float medianPairwiseCorrelation = 0f;
public float meanPairwiseCorrelation = 0f;

public Vector points = new Vector();

public boolean rememberPoints = true;

public boolean alreadyCalculated = false;

  public VStatistics(int dimen) {
  mins = new float[dimen];
  maxs = new float[dimen];
  means = new float[dimen];
  stdevs = new float[dimen];
  stdevs0 = new float[dimen];
  skews = new float[dimen];
  nums = new int[dimen];
  dimension = dimen;
  pointsNumber = 0;
  totalDispersion = 0;
  }
  
  public void initsums(){
	  for(int i=0;i<dimension;i++)
	   {
	   means[i] = 0;
	   stdevs[i] =0;
	   stdevs0[i] =0;   
	   skews[i] =0;
	   mins[i] = +1e10f;
	   maxs[i] = -1e10f;
	   nums[i] = 0;
	   pointsNumber = 0;
	   }
  }
  
  public void recomputesums(){
	  Vector<float[]> temppoints = new Vector<float[]>();
	  for(int i=0;i<points.size();i++) temppoints.add((float[])points.get(i));
	  points.clear();
	  for(int i=0;i<temppoints.size();i++){
		  this.addNewPoint((float[])temppoints.get(i));
	  }
  }

  public void initialize(){
   initsums();
   points.clear();
  }

  public void calculate(){
	  
  if(alreadyCalculated)if(rememberPoints)if(points.size()>0){
	  initsums();
	  recomputesums();
	  alreadyCalculated = false;
  }
  
  if(!alreadyCalculated){
	  
  for(int i=0;i<dimension;i++)
   {
   //System.out.println(means[i]+"\t"+stdevs[i]+"\t"+skews[i]);
   means[i]/=nums[i];
   double v = (double)(stdevs[i]/nums[i]-means[i]*means[i]);
   if(v<0) v=0;   
   v = (float)java.lang.Math.sqrt(v);
   skews[i] = (float)((skews[i]/nums[i]-3*means[i]*stdevs[i]/nums[i]+2*means[i]*means[i]*means[i])/(v*v*v));
   stdevs[i]=(float)v*(float)Math.sqrt((float)nums[i]/(float)(nums[i]-1));
   v = (double)(stdevs0[i]/nums[i]);
   if(v<0) v=0;   
   v = (float)java.lang.Math.sqrt(v);
   stdevs0[i]=(float)v*(float)Math.sqrt((float)nums[i]/(float)(nums[i]-1));   
   }
	totalDispersion = 0;
	for(int j=0;j<dimension;j++){
		totalDispersion+=stdevs[j]*stdevs[j];
	}
	totalDispersion = (float)Math.sqrt(totalDispersion);
    calcMedians();
    
    totalSDVtozero = 0f;
	for(int j=0;j<dimension;j++){
		totalSDVtozero+=stdevs0[j]*stdevs0[j];
	}
	totalSDVtozero = (float)Math.sqrt(totalSDVtozero);
  
	alreadyCalculated = true;
   }
	
  }

  public void addNewPoint(float p[]){
  for(int i=0;i<dimension;i++)
   {
   if(!Float.isNaN(p[i])){
     means[i] +=p[i];
     stdevs[i] +=p[i]*p[i];
     stdevs0[i] +=p[i]*p[i];
     skews[i] += p[i]*p[i]*p[i];
     if(p[i]<mins[i]) mins[i]=p[i];
     if(p[i]>maxs[i]) maxs[i]=p[i];
     nums[i]++;
   }
  }
  pointsNumber++;
  if(rememberPoints)
    points.add(p);
  }

  public float getMax(int n){
  return maxs[n];
  }

  public float getMin(int n){
  return mins[n];
  }

  public float getMean(int n){
  return means[n];
  }

  public float getStdDev(int n){
  return stdevs[n];
  }
  
  public float getStdDev0(int n){
  return stdevs[n];
  }  

  public float getMedian(int n){
  return medians[n];
  }

  public float getSkew(int n){
  return skews[n];
  }


  public String toString(){
  StringBuffer s = new StringBuffer();
  s.append("Means:\t");
  for(int i=0;i<dimension;i++)
    s.append(getMean(i)+"\t");
  s.append("\n");
  s.append("StdDevs:\t");
  for(int i=0;i<dimension;i++)
    s.append(getStdDev(i)+"\t");
  s.append("\n");
   s.append("Maxs:\t");
  for(int i=0;i<dimension;i++)
    s.append(getMax(i)+"\t");
  s.append("\n");
  s.append("Mins:\t");
  for(int i=0;i<dimension;i++)
    s.append(getMin(i)+"\t");
  s.append("\n");

  return s.toString();
  }

  public void calcMedians(){
    medians = new float[dimension];
    for(int j=0;j<dimension;j++){
      float x[] = new float[points.size()];
      for(int i=0;i<points.size();i++){
        x[i] = ((float[])points.get(i))[j];
      }
    medians[j] = vdaoengine.utils.VSimpleFunctions.calcMedian(x);
    }
  }

}
