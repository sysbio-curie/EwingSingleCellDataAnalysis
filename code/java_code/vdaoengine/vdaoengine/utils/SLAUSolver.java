package vdaoengine.utils;

import java.util.*;
import java.io.*;

import cern.colt.matrix.*;
import cern.colt.matrix.linalg.*;
import cern.colt.matrix.impl.*;


public class SLAUSolver{ // implementation of sparse SLAU solver with Colt library

  public int dimension;
  public double solution[];
  public SparseAdapter sparseAdapter = new SparseAdapter();
  public double vector[];
  DoubleMatrix2D solution_v;
  DoubleMatrix2D vector_v;
  DoubleMatrix2D Matrix;

  LUDecompositionQuick LUdecompos;
  //LUDecomposition LUdecompos;

  public void readMatrix(String fn){

  }

  public void saveMatrix(String fn){

  }

  public void readVector(String fn){

  }

  public void saveVector(String fn){

  }

  public void saveSolution(String fn){
          try{
          FileWriter f = new FileWriter(fn);
          for(int i=0;i<dimension;i++){
                  f.write(solution[i]+"\n");
          }
          f.close();
          }catch(Exception e){
            e.printStackTrace();
          }
  }

  public void setVector(double vec[]){
    vector = vec;
    vector_v = new SparseDoubleMatrix2D(dimension,1);
    for(int i=0;i<dimension;i++){
            vector_v.set(i,0,vec[i]);
    }
  }

  public void setVector(float vec[]){
    double v[] = new double[vec.length];
    for(int i=0;i<v.length;i++) v[i] = vec[i];
    setVector(v);
  }


  public void setSolution(double sol[]){
    solution = sol;
    solution_v = new SparseDoubleMatrix2D(dimension,1);
    for(int i=0;i<dimension;i++){
            solution_v.set(i,0,sol[i]);
    }
  }

  public void setSolution(float vec[]){
    double v[] = new double[vec.length];
    for(int i=0;i<v.length;i++) v[i] = vec[i];
    setSolution(v);
  }


  public long solve(double tolerance, int maxNumberOfIter){
    try{
      Algebra al = new Algebra(tolerance);
//      System.out.println(Matrix);

      //solution_v = al.solve(Matrix,vector_v);

      // with preconditioner LUDecomposition
      //solution_v = LUdecompos.solve(vector_v);

      // with preconditioner LUDecompositionQuick
      if(LUdecompos==null) 
    	  System.out.println("LUdecompos = null");
      LUdecompos.solve(vector_v);
      solution_v = vector_v;


      for(int i=0;i<dimension;i++) solution[i] = solution_v.get(i,0);
    }catch(Exception e){
      e.printStackTrace();
    }
    return 0;
  }

  public void initMatrix(){
    sparseAdapter.dimension = dimension;
    sparseAdapter.clear();
  }

  public void createMatrix(){
          sparseAdapter.getElements();
          Matrix = new SparseDoubleMatrix2D(dimension,dimension);
          for(int i=0;i<sparseAdapter.is.length;i++){
            int i1 = sparseAdapter.is[i];
            int i2 = sparseAdapter.js[i];
            double v = sparseAdapter.val[i];
            //System.out.println(""+i1+"\t"+i2+"\t"+v);
            Matrix.set(i1,i2,v);
            Matrix.set(i2,i1,v);
          }
  }

  public void setMatrixElement(int i,int j,double v){
          sparseAdapter.put(i,j,v);
  }

  public double getMatrixElement(int i,int j){
          return sparseAdapter.get(i,j);
  }

  public double addToMatrixElement(int i,int j,double v){
	  	  double d = sparseAdapter.add(i,j,v);
	  	  //System.out.println(""+i+","+j+" "+getMatrixElement(i,j));
          return d;
  }

  public void createPreconditioner(){
    // we must do prepare the matrix: some decomposition?
    //LUdecompos = new LUDecomposition(Matrix);
    LUdecompos = new LUDecompositionQuick();
    LUdecompos.decompose(Matrix);
  }

}