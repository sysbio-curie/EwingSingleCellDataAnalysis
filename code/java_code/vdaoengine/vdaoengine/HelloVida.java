package vdaoengine;



import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;
import java.util.Vector;
import com.sun.j3d.utils.behaviors.mouse.*;
import java.awt.*;

import vdaoengine.image.*;
import vdaoengine.image.im3d.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;

public class HelloVida extends Applet {

static CapturingCanvas3D Canvas;

    private static SimpleUniverse u = null;

    public HelloVida() {
    }

    public void init() {
	setLayout(new BorderLayout());

  System.out.println("Load data...");
  VDataTable VT = VDatReadWrite.LoadFromVDatFile("iris.dat");
  VDataSet VD = new VDataSet();
  VTableSelector VS = new VTableSelector();
  VS.selectFirstNumericalColumns(VT,4);
  VD.loadFromDataTable(VT,VS);
  VD.standardPreprocess();
  System.out.println("Calc PCA...");
  PCAMethod PCA = new PCAMethod();
  System.out.println("Set dataset...");
  System.out.println(VT.toString(0));
  PCA.setDataSet(VD);
  //System.out.println(PCA.getBasis());
  System.out.println("Calculate image...");
/*  VHistogram h = new VHistogram();
  Vector fs = new Vector();
  fs.addElement("CAI_IT1");
  fs.addElement("CAI_IT15");
  //fs.addElement("N2");
  //fs.addElement("N3");
  //h.setBordersMinMax(VT,15,fs);
  //h.MakeBasicHistogram(VT,fs);
  //h.MakeHistogramBasedOnLabelField(VT,"IRIS-SETOS","");
  h.MakeAverageForLabelHistogram(VT,fs,"ADD3","");
  //h.MakeAverageForLabelHistogram(VT,fs,"IRIS-SETOS","");
  System.out.println(h);
  VPtolemyGraphViewer gv = new VPtolemyGraphViewer();
  gv.init();
  h.sortLabels();
  gv.ViewHistogram(h);
  //gv.plot.samplePlot();
  gv.plot.setBackground(Color.white);
  gv.plot.verticalXTicks = true;
  gv.plot.setYLabel("CAI Value");
  gv.plot.setXLabel("Functional category");
  gv.plot.setTitle("Bacilus subtilis");
  gv.Show(800,600);
  gv.plot.addLegend(0,"Iteration 1");
  gv.plot.addLegend(1,"Iteration 8");
  gv.ExportToEPS("1.eps");
  gv.ExportToGIF("1.gif");*/

  System.out.println("Calc bas...");
  PCA.calcBasis(3);


  VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"IRIS-SETOS",VClassifier.CHANGE_FILLCOLOR);

  System.out.println(System.getProperty("java.library.path"));


  VDataImage3D im = new VDataImage3D(PCA.updateDataImage());

        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

  /*JToolBar jToolBar1 = new JToolBar();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  JButton jButton4 = new JButton();
    jButton1.setPreferredSize(new Dimension(27, 27));
    jButton1.setText("jButton1");
    jButton2.setPreferredSize(new Dimension(27, 27));
    jButton2.setText("jButton1");
    jButton3.setPreferredSize(new Dimension(27, 27));
    jButton3.setText("jButton1");
    jButton4.setPreferredSize(new Dimension(27, 27));
    jButton4.setText("jButton1");
    jToolBar1.setAlignmentX((float) 0.0);
    jToolBar1.add(jButton4, null);
    jToolBar1.add(jButton3, null);
    jToolBar1.add(jButton2, null);
    jToolBar1.add(jButton1, null);
    this.add(jToolBar1, null);*/
    Panel Panel1 = new Panel();
    Panel1.setSize(new Dimension(300, 300));
    Panel1.setBackground(new Color(0.5f,0.5f,0.1f));
    //Panel1.setPreferredSize(new Dimension(300, 300));

	Canvas = new CapturingCanvas3D(config);
	//jPanel1.add("Center", Canvas);
        add(Canvas);
        //add(Panel1, null);

	// Create a simple scene and attach it to the virtual universe
        BranchGroup scene0 = new BranchGroup();

	TransformGroup objTrans = new TransformGroup();
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	scene0.addChild(objTrans);

	Transform3D yAxis = new Transform3D();
	Alpha rotationAlpha = new Alpha(-1, 4000);

	RotationInterpolator rotator =
	    new RotationInterpolator(rotationAlpha, objTrans, yAxis,
				     0.0f, (float) Math.PI*2.0f);
	BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
	rotator.setSchedulingBounds(bounds);
	//scene0.addChild(rotator);

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objTrans);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());

        System.out.println("Calculating scene graph...");
	BranchGroup scene = im.createSceneGraph();
        System.out.println("Scene created");
	u = new SimpleUniverse(Canvas);
        objTrans.addChild(scene);
        objTrans.addChild(new vdaoengine.image.im3d.Axis());

        //scene0.addChild(new Axis());
        //scene0.addChild(scene);
        scene0.addChild(myMouseRotate);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(0f,0f,15f));
        u.getViewer().getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
        u.getViewer().getView().setScreenScalePolicy(View.SCALE_EXPLICIT);
        u.getViewer().getView().setScreenScale(0.06);
        u.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);
        //u.getViewingPlatform().setNominalViewingTransform();
        //u.getViewingPlatform().setViewPlatform();

        Background background = new Background();
        background.setColor(new Color3f(globalSettings.BackgroundColor));
        background.setApplicationBounds(bounds);
        scene0.addChild(background);

        System.out.println("Adding to universe...");
	u.addBranchGraph(scene0);
        System.out.println("Added");

    }

    public void destroy() {
	u.removeAllLocales();
    }

    public static void main(String[] args) {
        HelloVida HV = new HelloVida();
        //HV.init();
        //VViewer3D V = new VViewer3D();
        //V.add(Canvas);
        //Canvas.validate();
        new MainFrame(HV, 400, 400);
	//new MainFrame(new HelloVida(), 400, 400);
        HV.Canvas.validate();
        //Image Im = HV.Canvas.createImage(400,400);
        //VGraphicUtils.ExportToGIF(HV,"Im.gif");
        HV.Canvas.captureSize = new Dimension(400,400);
        HV.Canvas.writeGIF_=true;
        for(;;);
    }
}
