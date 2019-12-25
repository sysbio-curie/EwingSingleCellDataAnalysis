package vdaoengine;

 

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

//import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.analysis.cluster.*;
import vdaoengine.analysis.*;
import vdaoengine.visproc.*;
import java.util.Date;

public class TestingEnv {

  public TestingEnv() {
  }
  public static void main(String[] args) {
//  VDataTable VT = new VDataTable();
  System.out.println("Reading dat-file "+args[0]+"...");
//  VT.LoadFromVDatFile(args[0]);
  Date DB = new Date();
  VDataTable VT = VDatReadWrite.LoadFromVDatFile(args[0]);
  Date DA = new Date();
  System.out.println("Time = "+(DA.getTime()-DB.getTime())+" ms");
  VDataSet VD = new VDataSet();
  VTableSelector VS = new VTableSelector();
  VS.selectFirstNumericalColumns(VT,64);
  System.out.println("Loading dataset...");
  DB = new Date();
  VD.loadFromDataTable(VT,VS);
  DA = new Date();
  System.out.println("Time = "+(DA.getTime()-DB.getTime())+" ms");

  System.out.println("CalcStatistics...");
  VD.standardPreprocess();

  System.out.println(VObjectDescriptor.DefaultDescriptor());
  //VKMeansClusterization KM;
  //KM = new VKMeansClusterization(10);
  //PCAMethod PCA = new PCAMethod();
  //System.out.println(PCA.image.imageActual);
  //PCA.setDataSet(VD);
  //System.out.println("Start calc vectors");
  //DB = new Date();
  //PCA.calcBasis(3);
  //DA = new Date();
  //System.out.println("Time = "+(DA.getTime()-DB.getTime())+" ms");
  //System.out.println(PCA.getBasis().toString());
  //PCA.recalculateBasis();
  /*for(int i=1;i<20;i++)
  {
  KM = new VKMeansClusterization(i);
  KM.setClusterizationQuality(0.00001);
  double d=0;
  int num = 3;
  for(int j=0;j<num;j++) {
  KM.doClusterization(VD);
  d+=KM.calculateAverageIntraClassStdDev(VD);}
  System.out.print(i+"\t"+d/num+"\n");
  }
  System.out.println();*/
  }
}