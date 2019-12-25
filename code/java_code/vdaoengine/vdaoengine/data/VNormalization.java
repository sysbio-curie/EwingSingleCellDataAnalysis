package vdaoengine.data;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.data.*;
import vdaoengine.utils.*;


public class VNormalization implements VTablePreprocess {

VStatistics statistics;
int normalizationTypes[]; // -1 - none, 0 - centr-stddev (default), 1 - [-1,1], 2 - logarithmic, 3 - only centr, 4 - only stddev

  public VNormalization(VStatistics stat) {
  setStatistics(stat);
  normalizationTypes = new int[statistics.dimension];
  for(int i=0;i<statistics.dimension;i++)
     if (Math.abs(stat.getStdDev(i))<1e-10)
       normalizationTypes[i]=3;
  }

  public void setStatistics(VStatistics stat){
  statistics = stat;
  }

  public void setType(int n, int type){
  if ((type==0)&&(java.lang.Math.abs(statistics.getStdDev(n))<1e-10))
     {
     normalizationTypes[n] = 3;
     }
  else
     normalizationTypes[n] = type;
  }

  public void process(float row[]){
  for(int i=0; i<statistics.dimension; i++)
    {
    switch(normalizationTypes[i])
     { case -1: row[i] = row[i]; break;
       case  0: row[i] = (row[i]-statistics.getMean(i))/statistics.getStdDev(i); break;
       case  1: row[i] = (row[i]-statistics.getMin(i))/(statistics.getMax(i)-statistics.getMin(i)); break;
       case  2: row[i] = (float)java.lang.Math.log(row[i]); break;
       case  3: row[i] -=statistics.getMean(i); break;
       case  4: row[i] = row[i]/statistics.getStdDev(i); break;
     }
    }
  }

  public void unprocess(float row[]){
  for(int i=0; i<statistics.dimension; i++)
    {
    switch(normalizationTypes[i])
     { case -1: ; row[i] = row[i]; break;
       case  0: row[i] = row[i]*statistics.getStdDev(i)+statistics.getMean(i); break;
       case  1: row[i] = row[i]*(statistics.getMax(i)-statistics.getMin(i))+statistics.getMin(i); break;
       case  2: row[i] = (float)java.lang.Math.exp(row[i]); break;
       case  3: row[i] +=statistics.getMean(i); break;
       case  4: row[i] = row[i]*statistics.getStdDev(i); break;
     }
    }
  }
}
