package vdaoengine.utils;

import java.util.*;
import java.io.*;

public class SparseAdapter {

  HashMap cells = new HashMap();
  int dimension;
  public int is[];
  public int js[];
  public double val[];
  public static long MAX_NUMBER_OF_NODES = 1000000;

  public void put(int i,int j,double v){
          if(Math.abs(v)>1e-20){
          //PairInt p = new PairInt(i,j);
          Long p = new Long(i*MAX_NUMBER_OF_NODES+j);
          cells.put(p,new Double(v));
          }
  }

  public double get(int i,int j){
          Long p = new Long(i*MAX_NUMBER_OF_NODES+j);
          Double d = (Double)cells.get(p);
          if(d==null) d = new Double(0);
          return d.doubleValue();
  }

  public double add(int i,int j,double v){
          //System.out.println(v);
          double p = 0;
          if(Math.abs(v)>1e-20){
          p = get(i,j);
          put(i,j,p+v);
          }
          return p+v;
  }

  public void clear(){
          cells.clear();
  }

  public void getElements(){
          Set hs = cells.keySet();
          int n = getSize();
          is = new int[n];
          js = new int[n];
          val = new double[n];
          int k=0;
          for(Iterator i = hs.iterator(); i.hasNext(); ){
                  Long p = (Long)i.next();
                  is[k] = (int)(1f*p.longValue()/MAX_NUMBER_OF_NODES);
                  js[k] = (int)(p.longValue()-is[k]*MAX_NUMBER_OF_NODES);
                  val[k] = get(is[k],js[k]);
                  k++;
          }
  }

  public int getSize(){
          return cells.size();
  }


}