package vdaoengine;

import cern.colt.matrix.*;
import cern.colt.matrix.linalg.*;
import cern.colt.matrix.impl.*;
import vdaoengine.data.*;

public class testcolt {

  public static void main(String args[]){

    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/myprograms/ewingkinetics/time_ser.txt",true,"\t ");



    System.out.println("Hello, Colt!");
    DoubleMatrix2D matrix, vec, x, ch;
    matrix = new SparseDoubleMatrix2D(3,3);
    matrix.set(0,0,1); matrix.set(0,1,6); matrix.set(0,2,3);
    matrix.set(1,0,4); matrix.set(1,1,3); matrix.set(1,2,2);
    matrix.set(2,0,2); matrix.set(2,1,1); matrix.set(2,2,9);
    System.out.println(matrix);
    vec = new SparseDoubleMatrix2D(3,1);
    vec.set(0,0,2); vec.set(1,0,3); vec.set(2,0,1);
    Algebra al = new Algebra(0.001);
    x = al.solve(matrix,vec);
    System.out.println(x);
    ch = al.mult(matrix,x);
    System.out.println(ch);
  }

}