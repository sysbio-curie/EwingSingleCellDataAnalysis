package vdaoengine;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.image.*;
import vdaoengine.image.im2d.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;


import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Ellipse2D;

import java.util.*;
import java.net.*;

import java.awt.image.BufferedImage;
import com.sun.image.codec.jpeg.*;
import java.awt.image.BufferedImage;

public class TestReo extends JApplet {

static JFrame f = null;
static TestReo demo = null;
public static BufferedImage im = null;
static VDataSet VD = null;
static VObjectDescriptor D = null;
static VDataImage imag = null;
static VDataImage2D im2d = null;
static VClassifier Cl = null;
static VContLayer layer = null;
static PCAMethod PCA = null;
static String fn = "c:/datas/data.dat";

  public void doit(){

    //if(!(new File("data.dat")).exists()){
    /*im = new BufferedImage(700,700,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = im.createGraphics();
    g.setBackground(Color.red);
    g.clearRect(0,0,im.getWidth(),im.getHeight());
    g.setColor(Color.black);
    g.drawOval(100,100,200,200);*/

    String dat = VDownloader.downloadURL(fn);
    System.out.println(dat);
    //String dat = null;
    im = new BufferedImage(700,700,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = im.createGraphics();
    g.setFont(new Font("Arial Black",0,15));
    if(dat==null)
    g.drawString("Null String",200,200);
    else
    g.drawString(dat,200,200);
    if((dat==null)||(dat.indexOf("denied")>0)){
      if(dat!=null)
      g.drawString(dat,200,200);
    }
    else{

    //VD = VSimpleProcedures.SimplyPreparedDatasetFromDatString(dat,"data.dat",-1);
    VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile(fn,-1);
    PCA = new PCAMethod();
    PCA.setDataSet(VD);
    PCA.calcBasis(2);
    System.out.println("PCs calculated");
    Cl = new VClassifier();
    //VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"IRIS-SETOS",VClassifier.CHANGE_FILLCOLOR);
    D = new VObjectDescriptor();

    D.setFillColor(Color.red);
    D.setShape(VFlatPoint.PENTAGON);
    D.setSize(10f);
    D.setWithBorder(true);
    D.setBorderColor(Color.black);
    //D.setAnnotation("This is reo55");
    D.setAnnotationFont(new Font("Times New Roman",1,15));
    D.setAnnotationColor(Color.red.darker());

    VClassifier Cl = new VClassifier();
    VD.setDescriptors(new VObjectDescriptorSet());
    //VSimpleProcedures.AddClassByFieldValue(Cl,VD,"NAME","reo55",D);

    imag = PCA.updateDataImage();
    ((VObjectSet)imag.elements.elementAt(0)).annotateByField(VD.table,"NAME");

    VDataSet proj = PCA.getProjectedDataset();
    VFunction funct = VFunctionCalculator.VCalculate2DGridForFunction(proj,40,40);
    VFunctionCalculator.VCalculateDensity(proj,0.8f,funct);
    layer = new VContLayer("Density");
    layer.function = funct;
    imag.addElement(layer);

    im2d = new VDataImage2D(imag);
    globalSettings.defaultPointBorder = false;
    im2d.calculateScale(700,700);
    im = im2d.createBufferedImage(700,700);

    /*int clickx = 300;
    int clicky = 200;

    int ID = im2d.findClosestPoint(clickx,clicky);
    System.out.println("ID="+ID);
    String s[] = VD.table.getRowByID(ID);
    System.out.println(s[0]);
    VObjectDescriptor D1 = (VObjectDescriptor)D.clone();
    //D1.setSize(15);
    //D1.setAnnotation(s[0]);
    D1.setFillColor(Color.yellow);
    D1.setAnnotation(s[0]);
    VSimpleProcedures.AddClassByFieldValue(cl,VD,"NAME",s[0],D1);
    imag = PCA.updateDataImage();
    imag.addElement(layer);
    im2d = new VDataImage2D(imag);
    im2d.calculateScale(700,700);
    im = im2d.createBufferedImage(700,700);
    Graphics2D g = im.createGraphics();
    g.setColor(Color.black);
    g.drawOval(clickx-10,clicky-10,20,20);*/


    /*Polygon p = new Polygon();
    p.addPoint(10,10);
    p.addPoint(300,10);
    p.addPoint(10,300);
    Graphics2D g = im.createGraphics();
    g.setColor(Color.black);
    g.drawPolygon(p);

    float L = 300f;
    float rx = 200f;
    float c0 = 0.8f*L/rx;

    GradientPaint gp = new GradientPaint(10,10,Color.black,10+rx,10,new Color(1f-c0,1f-c0,1f-c0));

    g.setPaint(gp);
    g.fill(p);
    g.fillOval(0,0,10,10);
    g.setColor(new Color(0.8f,0.8f,0.8f));
    g.fillOval(300,0,10,10);
    g.setColor(new Color(0.5f,0.5f,0.5f));
    g.fillOval(0,300,10,10);*/


    //Utils2d.createJPGFile(im,"test1.jpg",0.8f);
    }

  }

  public static void main(String[] args) {



    demo = new TestReo();
    demo.init();
    demo.doit();

    f = new JFrame("Viewing REO data");
    f.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {System.exit(0);}
    });
    f.getContentPane().add("Center", demo);
    demo.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) { onMouseMoved(e); }
    });
    demo.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) { onMouseClicked(e); }
    });

    //f.getContentPane().add(pnl);
    f.pack();
    f.setSize(new Dimension(700,700));
    f.show();

  }


  public static void onMouseMoved(MouseEvent e){
    int ID = im2d.findClosestPoint(e.getX(),e.getY());
    String s[] = VD.table.getRowByID(ID);
    f.setTitle("Closest point: "+s[0]);
  }

  public static void onMouseClicked(MouseEvent e){
    int ID = im2d.findClosestPoint(e.getX(),e.getY());
    String s[] = VD.table.getRowByID(ID);
    System.out.println(s[0]);
    VSimpleProcedures.AddClassByFieldValue(Cl,VD,"NAME",s[0],D);
    imag = PCA.updateDataImage();
    imag.addElement(layer);
    im2d = new VDataImage2D(imag);
    im2d.calculateScale(700,700);
    im = im2d.createBufferedImage(700,700);

    Graphics2D g = im.createGraphics();
    g.setColor(Color.black);
    g.drawOval(e.getX()-10,e.getY()-10,20,20);
    demo.setVisible(false);
    demo.setVisible(true);
  }

  public void paint(Graphics g) {

    if(im==null) {doit(); main(null); }

    Graphics2D g2 = (Graphics2D) g;
    Dimension d = getSize();
    g2.setBackground(getBackground());
    g2.clearRect(0, 0, d.width, d.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
    g2.drawImage(im,null,0,0);
    }

    public void init() {
      try{
      String fn = getParameter("datfile");
      }catch(Exception e){};
    }


    public void drawDemo(int w, int h, Graphics2D g2) {

        Color reds[] = { Color.red.darker(), Color.red };

        /*
         * fills 18 Ellipse2D.Float objects, which get smaller as
         * N increases
         */
        for (int N = 0; N < 18; N++) {
            float i = (N + 2) / 2.0f;
            float x = (float) (5+i*(w/2/10));
            float y = (float) (5+i*(h/2/10));
            float ew = (w-10)-(i*w/10);
            float eh = (h-10)-(i*h/10);

            /*
             * assigns a higher value of alpha, corresponding to
             * a higher value of N
             */
            float alpha = (N == 0) ? 0.1f : 1.0f / (19.0f - N);

            // sets the ellipse to a darker version of red if N < 16
            if ( N >= 16 ) {
                g2.setColor(reds[N-16]);
            } else {
                g2.setColor(new Color(0f, 0f, 0f, alpha));
            }
            g2.fill(new Ellipse2D.Float(x,y,ew,eh));
        }
    }


    /*public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        drawDemo(d.width, d.height, g2);
    }*/



}