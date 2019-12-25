package vdaoengine;

import java.awt.*;
import java.util.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;
import com.sun.j3d.utils.applet.MainFrame;

/**
 * Title:        Project of visualizing codon frequencies
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES, France
 * @author Andrey Zinovyev, Alessandra Carbone
 * @version 1.0
 */

public class CodonUsageGraph {

public static String fileName = "codonusage";
public static String imgName = "temp";

  public static void main(String[] args) {

  VPtolemyGraphViewer gv1 = new VPtolemyGraphViewer();
  gv1.init();
  VPtolemyGraphViewer gv = new VPtolemyGraphViewer();
  gv.init();

  Font font = new Font("Helvetica",0,10);
  Font bigfont = new Font("Helvetica",0,14);
  Font font2 = new Font("Helvetica",0,12);

  int numberOfIteration = 15;

  //System.out.println(args[0]+"/"+args[1]);
  System.out.println("Load data...");
  VDataSet VDC = VSimpleProcedures.SimplyPreparedDatasetFromSimpleDatFile(fileName,numberOfIteration,false,"\t ");
  System.out.println("Loaded");
  Vector fs = new Vector();

  gv1 = new VPtolemyGraphViewer();
  gv1.init();
  fs.clear();
  //fs.add(0,"N18");
  fs.add(0,"N3");
  //fs.add("N3");
  //fs.add("N18");
  //gv1.plot.verticalXTicks = true;
  gv1.plot.setMarksStyle("dots");
  gv1.plot.setBackground(new Color(0.9f,0.9f,0.9f));
  gv1.plot.setLabelFont("Courier");
  //gv1.plot.setXLabel("Codons");
  //gv1.plot.setYLabel("frequency");
  //gv1.plot.setTitle(args[0]);
  Font font1 = new Font("Courier",0,11);
  //gv1.plot.setLabelFont(font1);
  //gv1.plot.setTitleFont(font2);

  gv1.ViewSignalGraph(VDC.table,fs,"N2","N1",0,32);
  //gv1.plot.addLegend(0,"It. "+numberOfIteration+"");
  //gv1.plot.addLegend(0,"It. 1");
  //gv1.plot.addLegend(2,"Global");
  //gv1.plot.addLegend(0,"Overall");
  //gv1.plot.addLegend(1,"Highly express.");
  gv1.plot.setMarksStyle("various");
  gv1.Show(280,140);
  gv1.ExportToGIF(imgName+"_cug1.gif");

  gv1.destroy();

  gv1 = new VPtolemyGraphViewer();
  gv1.init();
  fs.clear();
  fs.add(0,"N3");
  //gv1.plot.verticalXTicks = true;
  gv1.plot.setBackground(new Color(0.9f,0.9f,0.9f));
  gv1.plot.setMarksStyle("dots");
  gv1.plot.setLabelFont("Courier");
  //gv1.plot.setLabelFont(font1);
  //gv1.plot.setTitleFont(font2);

  gv1.ViewSignalGraph(VDC.table,fs,"N2","N1",32,61);
  gv1.plot.setMarksStyle("various");
  gv1.Show(280,140);
  gv1.ExportToGIF(imgName+"_cug2.gif");

  //System.exit(0);
  gv1.destroy();

  try{
  Thread.sleep(1000);
  }catch(Exception e){};

  }
}