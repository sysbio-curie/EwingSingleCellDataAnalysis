package vdaoengine.analysis;

import java.io.*;
import java.util.*;
import vdaoengine.utils.*;

public class NPCAMethod {

  public VDistanceMatrix dmatrix = null;

  public Vector componentPairs = new Vector();
  public Vector points = new Vector();

  public void calc(VDistanceMatrix m){
    dmatrix = m;
    int numberOfComponents = dmatrix.numberOfPoints-1;
    calc(numberOfComponents);
  }

  public void calc(VDistanceMatrix m, int numberOfComponents){
    dmatrix = m;
    calc(numberOfComponents);
  }

  public void calc(){
    int numberOfComponents = dmatrix.numberOfPoints-1;
    calc(numberOfComponents);
  }

  public void addAllDistances(VDistanceMatrix md){
    dmatrix = md;
    int numberOfComponents = dmatrix.matrixLinear.length;
    int ind[] = Algorithms.SortMass(dmatrix.matrixLinear);
    for(int i=0;i<numberOfComponents;i++)
      componentPairs.add(dmatrix.pairs[ind[numberOfComponents-i-1]]);
  }


  public void calc(int n){
    for(int i=0;i<n;i++){
    	if(i==(int)(0.01f*(float)i)*100) System.out.print(i+"\t");
      PairInt pi = getNextComponent();
      if(points.size()==0){
        points.add(new Integer(pi.i1));
        points.add(new Integer(pi.i2));
        componentPairs.add(pi);
      }else{
        points.add(new Integer(pi.i2));
        componentPairs.add(pi);
      }
    }
  }

  public PairInt getNextComponent(){
    PairInt pi = null;
    if(points.size()==0){
      pi = dmatrix.findBiggestDistance();
    }else{
      pi = dmatrix.findBiggestDistanceToSet(points);
    }
    return pi;
  }



}

