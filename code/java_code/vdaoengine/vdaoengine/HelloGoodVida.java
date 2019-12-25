package vdaoengine;



/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;
import vdaoengine.image.im3d.*;
import vdaoengine.image.im2d.*;
import com.sun.j3d.utils.applet.MainFrame;
import javax.swing.*;
import vdaoengine.image.*;

public class HelloGoodVida {

  public HelloGoodVida() {
  }

  public static void main(String[] args) {

  try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { }

  System.out.println("Load data...");

/*  VDataSet VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile("out.dat",64);

  System.out.println("Calc PCA...");
  PCAMethod PCA = new PCAMethod();
  PCA.setDataSet(VD);
  PCA.calcBasis(3);
  VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"ADD3",VClassifier.CHANGE_FILLCOLOR);
  VDataImage3D im = new VDataImage3D(PCA.updateDataImage());

  VViewer3D V3d = new VViewer3D();

  V3d.addImageScene(im);
  //V3d.rotate();
  V3d.makeLive();

  V3d.showViewer("",650,500);*/


/*  VDataSet VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile("iris.dat",4);
  globalSettings.AddMoreStandardColors(100);
  //VDataSet VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile("out.dat",64);

  System.out.println("Calc PCA...");
  PCAMethod PCA = new PCAMethod();
  PCA.setDataSet(VD);
  PCA.calcBasis(3);
  VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"IRIS-SETOS",VClassifier.CHANGE_FILLCOLOR);
//  VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"ADD3",VClassifier.CHANGE_FILLCOLOR);
  VDataClass dc = Cl.getClass("IRIS-SETOS=\"Iris-setosa\"");
  //VDataClass dc = Cl.getClass("ADD3=\"1.2\"");
  if(dc!=null){
  dc.descriptor.setSimplified(false);
  dc.descriptor.setShape(1);
  dc.descriptor.setSize(15);
  dc.descriptor.setFillColor(Color.red);
  }
  dc = Cl.getClass("ADD3=\"3.7.1\"");
  if(dc!=null){
  dc.descriptor.setSimplified(false);
  dc.descriptor.setShape(4);
  dc.descriptor.setSize(15);
  dc.descriptor.setFillColor(Color.yellow);
  }
  dc = Cl.getClass("ADD3=\"6\"");
  if(dc!=null){
  dc.descriptor.setSimplified(false);
  dc.descriptor.setShape(4);
  dc.descriptor.setSize(9);
  dc.descriptor.setFillColor(Color.green);
  }*/


VDataSet VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile("wtf.dat",64);
globalSettings.AddMoreStandardColors(100);

System.out.println("Calc PCA...");
PCAMethod PCA = new PCAMethod();
PCA.setDataSet(VD);
PCA.calcBasis(3);
VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"CL",VClassifier.CHANGE_FILLCOLOR);
/*VDataClass dc = Cl.getClass("IRIS-SETOS=\"Iris-setosa\"");
if(dc!=null){
dc.descriptor.setSimplified(false);
dc.descriptor.setShape(1);
dc.descriptor.setSize(15);
dc.descriptor.setFillColor(Color.red);
}
dc = Cl.getClass("ADD3=\"3.7.1\"");
if(dc!=null){
dc.descriptor.setSimplified(false);
dc.descriptor.setShape(4);
dc.descriptor.setSize(15);
dc.descriptor.setFillColor(Color.yellow);
}
dc = Cl.getClass("ADD3=\"6\"");
if(dc!=null){
dc.descriptor.setSimplified(false);
dc.descriptor.setShape(4);
dc.descriptor.setSize(9);
dc.descriptor.setFillColor(Color.green);
}*/

  //globalSettings.BackgroundColor = color.white;
  globalSettings.defaultPointSize = 8f;
  VDataImage3D im = new VDataImage3D(PCA.updateDataImage());
  VDataImage2D im2 = new VDataImage2D(PCA.updateDataImage());
  im.provideClassInformation(Cl);
  globalSettings.BackgroundColor = Color.white;
  globalSettings.defaultPointBorder = true;
  //globalSettings.defaultPointSimplified = false;
  globalSettings.defaultPointShape = VFlatPoint.SQUARE;

  /*VViewer3D V3d = new VViewer3D();
  V3d.addImageScene(im);
  //V3d.rotate();
  V3d.makeLive();

  V3d.showViewer("",800,600);*/

  im2.calculateScale(500,500);
  BufferedImage bim = im2.createBufferedImage(500,500);
  Utils2d.createJPGFile(bim,"temp.jpg",1f);


/*  VHistogram h = new VHistogram();
  Vector fs = new Vector();
  fs.addElement("N2");
  fs.addElement("N3");
  //h.MakeMinMaxForLabelHistogram(VD.table,fs,"IRIS-SETOS","");
  h.setBordersMinMax(VD.table,20,fs);
  h.MakeBasicHistogram(VD.table,fs);
  h.sortLabels();

  VPtolemyGraphViewer gv = new VPtolemyGraphViewer();
  gv.init();
  gv.plot.verticalXTicks = true;
  gv.plot.setBackground(Color.white);
  gv.plot.setYLabel("Y Axis");
  gv.plot.setXLabel("X Axis");
  gv.plot.setTitle("Hello, great Vida!");
  gv.ViewHistogram(h);
  //MainFrame fr = new MainFrame(gv,800,600);
  gv.ExportToEPS("11.eps");
  gv.ExportToGIF("11.gif");
  try{
  Thread.sleep(1000);
  }catch(Exception e){};
//  fr.dispose();*/

/*  VDataSet VD1 = VSimpleProcedures.SimplyPreparedDatasetFromSimpleDatFile("codonusage",15,false);
  VPtolemyGraphViewer gv = new VPtolemyGraphViewer();
  gv.init();
  Vector fs = new Vector();
  fs.addElement("N3");
  fs.addElement("N18");
  gv.plot.setMarksStyle("dots");
  gv.drawLegendAutomatically=true;
  //gv.plot.setXLog(true);
  //gv.plot.setYLog(true);
  gv.plot.verticalXTicks = true;
  //gv.ViewSignalGraph(VD1.table,fs,"N2","N1");
  gv.ViewDistributionGraph(VD1.table,"N3",fs,"N2");
  gv.Show(900,300);*/



  }
}