package vdaoengine;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import com.sun.image.codec.jpeg.*;
import java.awt.image.BufferedImage;
import vdaoengine.image.*;
import vdaoengine.image.im2d.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.elmap.*;
import vdaoengine.special.*;
import com.borland.jbcl.layout.*;
import javax.swing.*;
import javax.swing.event.*;

public class HumanClusters {
  public static void main(String[] args) {
    try{

      int numberOfComponents = 2;
      float beta = 0.0f;
      int gridResolution = 100;
      int width = 800;
      int height = 800;
      int numberOfDiscreteColors = 20;
      //String fname = "chr19xyn.dat";
      String fname = "ewingxy.dat";
      boolean makeWeights = false;
      boolean contrasting = true;
      boolean applyelmaps = true;

      VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetFromDatFile(fname,-1);
      PCAMethod pca = new PCAMethod();
      pca.setDataSet(vd);

      System.out.println("Calculating pca...");
      if(makeWeights){
      pca.calcBasis(numberOfComponents);
      //System.out.println(pca.getBasis().toString());
      System.out.println("Projecting...");
      VDataSet vdp = pca.getProjectedDataset();
      vdp = vd;
      System.out.println("Writing...");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vdp,"pcadecomp.dat");

      System.out.println("Statistics...");
      vd.calcStatistics();
      //double ds[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
      double ds[] = pca.calcDispersions();
      System.out.println(ds[0]+"\t"+ds[1]+"\t"+ds[2]);

      float ws[] = new float[vd.pointCount];
      for(int i=0;i<vd.pointCount;i++){
        float r = 0f;
        float vec[] = vdp.getVector(i);
        for(int j=0;j<numberOfComponents;j++)
            r+=vec[j]*vec[j]/ds[j];
        ws[i] = (float)Math.exp((double)beta*r);
      }
      vd.setWeights(ws);

      VDataTable vt = vd.table;
      vt.addNewColumn("weight","","",vt.NUMERICAL,"1");
      int id = vt.fieldNumByName("weight");
      for(int i=0;i<vd.pointCount;i++){
        vt.stringTable[i][id] = ""+ws[i];
      }
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"outw.dat");

      System.out.println("Calculating weighted pca...");
      }

      pca.setDataSet(vd);
      //pca.calcBasis(2);
      pca.linBasis = new VLinearBasis(2,2);
      pca.getBasis().basis[0][0] = 1;
      pca.getBasis().basis[0][1] = 0;
      pca.getBasis().basis[1][0] = 0;
      pca.getBasis().basis[1][1] = 1;
      pca.calculatedVectorsNumber = 2;
      pca.linBasis.isNormalized = true;
      pca.linBasis.isOrthogonal = true;

      //System.out.println(pca.getBasis().toString());
      VDataSet vdpw = pca.getProjectedDataset();
      System.out.println("Writing...");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vdpw,"pcadecompw.dat");


      VFunction funct = VFunctionCalculator.VCalculate2DGridForFunction(vdpw,gridResolution,gridResolution);
      double PCAMaxDesity2D = 0;
      if(!contrasting)
          PCAMaxDesity2D = VFunctionCalculator.VCalculateDensity(vdpw,0.8f,funct);
      else
          PCAMaxDesity2D = VFunctionCalculator.VCalculateDensityContrasted(vdpw,0.8f,2f,0*1e-3f,funct);
      VContLayer PCADensityLayer2D = new VContLayer("Density");
      PCADensityLayer2D.function = funct;

      //globalSettings.defaultPointBorder = true;
      //globalSettings.defaultPointBorderColor = Color.black;
      globalSettings.defaultPointSize = 0f;
      VDataImage imag = pca.updateDataImage();
      imag.DiscreteColor = numberOfDiscreteColors;
      imag.elements.add(PCADensityLayer2D);
      VDataImage2D im2d = new VDataImage2D(imag);
      im2d.doNotDrawPoints = true;
      BufferedImage im = im2d.createBufferedImage(width,height);
      IO.writeGIF("out.gif",im);

      /*if(applyelmaps){
          ElmapProjection elmap = new ElmapProjection();
          ElmapAlgorithm ela = new ElmapAlgorithm();
          ela.setData(vd);
          //if((new File("elmap.ini")).exists())
          //  System.out.println("Exists");
          String settings = vdaoengine.utils.IO.loadString("elmap.ini");
          ela.readIniFileStr(settings,8);
          ela.computeElasticGrid();
          elmap.setElmap(ela);
          imag = elmap.updateDataImage();
          im2d = new VDataImage2D(imag);
          //im2d.doNotDrawPoints = true;
          im = im2d.createBufferedImage(width,height);
          IO.writeGIF("out_elm.gif",im);
      }*/


    }catch(Exception e){
      e.printStackTrace();
    }
  }

}
