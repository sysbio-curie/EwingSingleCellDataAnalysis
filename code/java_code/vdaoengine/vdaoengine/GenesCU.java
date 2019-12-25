package vdaoengine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;
import vdaoengine.image.im3d.*;
import vdaoengine.image.im2d.*;
import com.sun.j3d.utils.applet.MainFrame;
import javax.swing.*;
import vdaoengine.image.*;


public class GenesCU {

  public static String fileName = "outg.dat";
  public static String imgName = "temp";

  public static void main(String[] args) {

    try{

      VDataSet VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile(fileName,64);
      globalSettings.AddMoreStandardColors(100);

      System.out.println("Calc PCA...");
      PCAMethod PCA = new PCAMethod();
      PCA.setDataSet(VD);
      PCA.calcBasis(3);



      VClassifier Cl = new VClassifier();

      Vector IDs = new Vector();
      Vector IDsHyp = new Vector();
      int prn = VD.table.fieldNumByName("ADDF");
      //System.out.println(prn);
      for(int i=0;i<VD.table.rowCount;i++){
        String prod = VD.table.getV(i,prn);
        prod = prod.toLowerCase();
        boolean show = false;
        if(prod.indexOf("riboso")>=0) { IDs.add(new Integer(VD.table.getRowID(i))); show = true; }
        if(prod.indexOf("rpl")>=0)  { IDs.add(new Integer(VD.table.getRowID(i))); show = true; }
        if(prod.indexOf("membr")>=0)  { IDsHyp.add(new Integer(VD.table.getRowID(i))); show = true; }
        //if(prod.indexOf("unknown")>=0)  { IDsHyp.add(new Integer(VD.table.getRowID(i))); show = true; }
        //if(show) System.out.println(prod);
      }

      if(VD.getDescriptors()==null) VD.setDescriptors(new VObjectDescriptorSet());
      VObjectDescriptor ds = new VObjectDescriptor();
      ds.setFillColor(Color.red);
      ds.setSize(4f);
      ds.setShape(VFlatPoint.CIRCLE);
      Cl.addNewClass(IDs,"ribosomal",ds,VD.getDescriptors());
      /*ds = new VObjectDescriptor();
      ds.setFillColor(Color.green);
      ds.setSize(3f);
      ds.setShape(VFlatPoint.TRIANGLE_DOWN);
      Cl.addNewClass(IDsHyp,"hypothetical",ds,VD.getDescriptors());*/

      /*Vector v = Cl.getClassVector();
      for(int i=0;i<v.size();i++){
        VDataClass vc = (VDataClass)v.elementAt(i);
        System.out.println(vc.toString());
      }*/

      globalSettings.defaultPointSize = 2f;
      VDataImage2D im2 = new VDataImage2D(PCA.updateDataImage());
      im2.provideClassInformation(Cl);
      globalSettings.BackgroundColor = Color.white;
      globalSettings.defaultFillColor = Color.white;
      globalSettings.defaultPointBorder = true;
      globalSettings.defaultPointBorderColor = Color.black;
      globalSettings.defaultPointShape = VFlatPoint.SQUARE;

      int sz = 250;
      im2.calculateScale(sz,sz);
      im2.bkgColor = new Color(0.9f,0.9f,0.9f);
      BufferedImage bim = im2.createBufferedImage(sz,sz);
      //Utils2d.createJPGFile(bim,"temp.jpg",1f);
      IO.writeGIF(imgName+".gif",bim);

      float ax = im2.ax;
      float bx = im2.bx;
      float ay = im2.ay;
      float by = im2.by;

      int n = 15;
      for(int kk=0;kk<n;kk++){
        float phi = 2*3.1415f*(float)kk/(float)n;
        PCA.image.imageActual = false;
        PCA.rotateXangle = -phi;
        im2 = new VDataImage2D(PCA.updateDataImage());
        im2.provideClassInformation(Cl);
        im2.ax = ax/1.2f; im2.bx = bx;
        im2.ay = ay/1.2f; im2.by = by*1f;
        im2.bkgColor = new Color(0.9f,0.9f,0.9f);
        bim = im2.createBufferedImage(sz,sz);
        //Utils2d.createJPGFile(bim,"temp.jpg",1f);
        String num = ""+kk;
        if(kk<10) num = "0"+num;
        IO.writeGIF("temp/frame_"+num+".gif",bim);
      }


      Runtime.getRuntime().exec("temp/gifsicle.exe --delay 17 -l -o "+imgName+"_anim.gif temp/frame_*.gif");

      for(int kk=0;kk<n;kk++){
        String num = ""+kk;
        if(kk<10) num = "0"+num;
        File f = new File("frame_"+num+".gif");
        f.delete();
      }

    }catch(Exception e){
      e.printStackTrace();
    }

  }
}