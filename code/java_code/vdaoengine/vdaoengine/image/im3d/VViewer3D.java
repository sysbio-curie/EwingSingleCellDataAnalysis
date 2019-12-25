/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

package vdaoengine.image.im3d;

import java.applet.Applet;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;
import java.util.Vector;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.swing.tree.DefaultMutableTreeNode;

import vdaoengine.image.*;
import vdaoengine.image.im3d.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;
import vdaoengine.image.VFlatPointSet;
import vdaoengine.*;
import java.awt.*;
import com.borland.jbcl.layout.*;
import javax.swing.event.*;
import javax.swing.tree.TreeSelectionModel;
import java.util.HashSet;
import java.util.Iterator;


public class VViewer3D extends Applet implements MouseBehaviorCallback{

  static CapturingCanvas3D Canvas;
  private SimpleUniverse Universe = null;
  public TransformGroup objTrans;
  public BranchGroup sceneRoot;
  public BranchGroup sceneFromImage;
  public Shape3D defaultAxes;
  public MainFrame viewerFrame;
  public VDataImage3D viewerDataImage;
  public BranchGroup rotatorG;
  private boolean rotating = false;

  public int XRotationAngle = 0;
  public int YRotationAngle = 0;
  public int ZRotationAngle = 0;
  public int XShift = 0;
  public int YShift = 0;
  public int ZShift = 0;

  ImageIcon imageNorm1;
  ImageIcon imageNorm2;
  ImageIcon imageNorm3;
  ImageIcon imageRot1;
  ImageIcon imageRot2;
  ImageIcon imageRot3;
  ImageIcon arrowDown1;
  ImageIcon arrowDown2;
  ImageIcon arrowUp1;
  ImageIcon arrowUp2;
  ImageIcon imageCapt1;
  ImageIcon imageCapt2;
  ImageIcon imageCapt3;
  JTree elementTree = new JTree();



  JPanel jPanel3 = new JPanel();
  JPanel CanvasPanel = new JPanel();
  XYLayout xYLayout4 = new XYLayout();
  XYLayout xYLayout5 = new XYLayout();
  JToolBar toolBar = new JToolBar();
  JButton initImageButton = new JButton();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel hintText = new JLabel();
  Component component1;
  JSlider zoomSlider = new JSlider();
  JButton captureImageButton = new JButton();
  JTextField captureName = new JTextField();
  JToggleButton rotateButton = new JToggleButton();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JButton YShiftUp = new JButton();
  JButton XrotateDown = new JButton();
  JTextField XShifText = new JTextField();
  JButton ZShiftDown = new JButton();
  JPanel positionPanel = new JPanel();
  JTextField ZShiftText = new JTextField();
  JButton YrotateDown = new JButton();
  JButton XShiftDown = new JButton();
  JButton ZrotateUp = new JButton();
  JButton ZrotateDown = new JButton();
  JTextField XRotateText = new JTextField();
  JButton ZShiftUp = new JButton();
  JTextField YRotateText = new JTextField();
  JButton YShiftDown = new JButton();
  JTextField YShiftText = new JTextField();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JButton YrotateUp = new JButton();
  JLabel jLabel2 = new JLabel();
  JTextField ZRotateText = new JTextField();
  JLabel jLabel1 = new JLabel();
  JButton XrotateUp = new JButton();
  XYLayout xYLayout3 = new XYLayout();
  JButton XShiftUp = new JButton();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton updateButton = new JButton();
  JButton setTransfButton = new JButton();
  JPanel managementPanel = new JPanel();

  JScrollPane TreeScrollPane = new JScrollPane();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel InstrPanel = new JPanel();
  JSplitPane SplitPane = new JSplitPane();
  JButton ImageUpdateButton = new JButton();
  JCheckBox visibleCheck = new JCheckBox();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

  public VViewer3D() {
    try {
      jbInit();
      initCanvas();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void initCanvas(){
  //setLayout(new BorderLayout());

  GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
  Canvas = new CapturingCanvas3D(config);
  CanvasPanel.add(Canvas);
  sceneRoot = new BranchGroup();

  objTrans = new TransformGroup();
  objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
  objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  sceneRoot.addChild(objTrans);

  MouseRotate myMouseRotate = new MouseRotate();
  myMouseRotate.setTransformGroup(objTrans);
  myMouseRotate.setSchedulingBounds(new BoundingSphere());
  myMouseRotate.setupCallback(this);
  sceneRoot.addChild(myMouseRotate);

  MouseTranslate myMouseTranslate = new MouseTranslate();
  myMouseTranslate.setTransformGroup(objTrans);
  myMouseTranslate.setSchedulingBounds(new BoundingSphere());
  myMouseTranslate.setupCallback(this);
  sceneRoot.addChild(myMouseTranslate);


  Universe = new SimpleUniverse(Canvas);

  Transform3D t3d = new Transform3D();
  t3d.set(new Vector3f(0f,0f,15f));
  Universe.getViewer().getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
  Universe.getViewer().getView().setScreenScalePolicy(View.SCALE_EXPLICIT);
  Universe.getViewer().getView().setScreenScale(0.05);
  Universe.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);
  sceneRoot.setCapability(sceneRoot.ALLOW_CHILDREN_EXTEND);
  sceneRoot.setCapability(sceneRoot.ALLOW_CHILDREN_WRITE);
  setBackground();
  }

  public void addAxes(){
  defaultAxes = new vdaoengine.image.im3d.Axis();
  objTrans.addChild(defaultAxes);
  }

  public void setBackground(){
  Background background = new Background();
  background.setColor(new Color3f(globalSettings.BackgroundColor));
  BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
  background.setApplicationBounds(bounds);
  sceneRoot.addChild(background);
  }

  public void addImageScene(VDataImage3D im){
  sceneFromImage = im.createSceneGraph();
  sceneFromImage.setCapability(sceneFromImage.ALLOW_DETACH);
  viewerDataImage = im;
  objTrans.addChild(sceneFromImage);
  fillTree();
  }

  public void makeLive(){
  Universe.addBranchGraph(sceneRoot);
  Universe.getViewer().getView().setDepthBufferFreezeTransparent(false);
  }

  public void rotate(){
  if(rotating){
  rotatorG.detach();
  rotating = false;
  }
  else{
	Transform3D yAxis = new Transform3D();
        objTrans.getTransform(yAxis);
        yAxis.transpose();
	Alpha rotationAlpha = new Alpha(-1, 4000);

	RotationInterpolator rotator =
	    new RotationInterpolator(rotationAlpha, objTrans, yAxis,
				     0.0f, (float) Math.PI*2.0f);
	BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
	rotator.setSchedulingBounds(bounds);
  rotatorG = new BranchGroup();
  rotatorG.setCapability(rotatorG.ALLOW_DETACH);
  rotatorG.addChild(rotator);
  sceneRoot.addChild(rotatorG);
  rotating = true;
  }
  //readTransform();
  }

  public void readTransform(){
  Transform3D r = new Transform3D();
  objTrans.getTransform(r);
  r.normalize();
  Matrix3d mat = new Matrix3d();
  Vector3f vec = new Vector3f();
  r.get(mat);
  r.get(vec);
  StringBuffer sb = new StringBuffer();

  double th = Math.asin(mat.m20);
  double ph = 0;
  double ps = 0;
  if(mat.m22*Math.cos(th)>0) ph = Math.atan(-mat.m21/mat.m22);
  if(mat.m22*Math.cos(th)<0) ph = Math.atan(-mat.m21/mat.m22)+Math.PI;
  if(mat.m00*Math.cos(th)>0) ps = Math.atan(-mat.m10/mat.m00);
  if(mat.m00*Math.cos(th)<0) ps = Math.atan(-mat.m10/mat.m00)+Math.PI;

  XRotateText.setText(new Float(Math.rint(ph*180/Math.PI)).toString());

  YRotateText.setText(new Float(Math.rint(th*180/Math.PI)).toString());
//  YRotateText.setText(sb.toString());
//  ZRotateText.setText(sb.toString());
  ZRotateText.setText(new Float(Math.rint(ps*180/Math.PI)).toString());
  r = new Transform3D();
  //r.rotX(th);
  //r.rotY(ph);
  //r.rotZ(ps);
  //objTrans.setTransform(r);
  XShifText.setText(new Float(Math.rint(vec.x*100)/100f).toString());
  YShiftText.setText(new Float(Math.rint(vec.y*100)/100f).toString());
  ZShiftText.setText(new Float(Math.rint(vec.z*100)/100f).toString());
  }

  public void setTransform(){
  Transform3D rx = new Transform3D();
  Transform3D ry = new Transform3D();
  Transform3D rz = new Transform3D();
  Transform3D rt = new Transform3D();
  Transform3D r = new Transform3D();

  double th = Double.parseDouble(XRotateText.getText())/180*Math.PI;
  double ph = Double.parseDouble(YRotateText.getText())/180*Math.PI;
  double ps = Double.parseDouble(ZRotateText.getText())/180*Math.PI;
  Vector3f vec = new Vector3f();
  vec.x = Float.parseFloat(XShifText.getText());
  vec.y = Float.parseFloat(YShiftText.getText());
  vec.z = Float.parseFloat(ZShiftText.getText());

  rx.rotX(-th);
  ry.rotY(-ph);
  rz.rotZ(-ps);
  rt.set(vec);
  r.mul(rx);
  r.mul(ry);
  r.mul(rz);
  r.mul(rt);
  objTrans.setTransform(r);
  }


  public void transformChanged(int type, Transform3D transform){
  readTransform();
  }

  public void showViewer(String s, int wid, int height){
  viewerFrame = new MainFrame(this,wid,height);
  viewerFrame.setTitle("Visual DAO Viewer3D: "+viewerDataImage.name);
  }

  private void jbInit() throws Exception {

    imageNorm1 = new ImageIcon("normal1.gif");
    imageNorm2 = new ImageIcon("normal2.gif");
    imageNorm3 = new ImageIcon("normal3.gif");

    imageRot1 = new ImageIcon("rotate1.gif");
    imageRot2 = new ImageIcon("rotate2.gif");
    imageRot3 = new ImageIcon("rotate3.gif");

    imageCapt1 = new ImageIcon("capt1.gif");
    imageCapt2 = new ImageIcon("capt2.gif");
    imageCapt3 = new ImageIcon("capt3.gif");

    arrowDown1 = new ImageIcon("arrd1.gif");
    arrowDown2 = new ImageIcon("arrd2.gif");

    arrowUp1 = new ImageIcon("arru1.gif");
    arrowUp2 = new ImageIcon("arru2.gif");


    setLayout(borderLayout2);
    jPanel3.setBorder(BorderFactory.createEtchedBorder());
    jPanel3.setMinimumSize(new Dimension(4, 20));
    jPanel3.setPreferredSize(new Dimension(4, 20));
    jPanel3.setLayout(xYLayout4);
    CanvasPanel.setBackground(Color.black);
    CanvasPanel.setBorder(BorderFactory.createRaisedBevelBorder());
    CanvasPanel.setLayout(borderLayout1);
    initImageButton.setBorder(null);
    initImageButton.setMaximumSize(new Dimension(40, 40));
    initImageButton.setMinimumSize(new Dimension(40, 40));
    initImageButton.setPreferredSize(new Dimension(40, 40));
    initImageButton.setToolTipText("Normalize picture");
    initImageButton.setFocusPainted(false);
    initImageButton.setIcon(imageNorm1);
    initImageButton.setMargin(new Insets(2, 2, 2, 2));
    initImageButton.setRolloverIcon(imageNorm2);
    initImageButton.setPressedIcon(imageNorm3);
    initImageButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        initImageButton_actionPerformed(e);
      }
    });
    hintText.setFont(new java.awt.Font("Dialog", 1, 12));
    zoomSlider.setOrientation(JSlider.VERTICAL);
    zoomSlider.setPaintTrack(false);
    zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        zoomSlider_stateChanged(e);
      }
    });
    captureImageButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        captureImageButton_actionPerformed(e);
      }
    });
    captureImageButton.setPreferredSize(new Dimension(40, 40));
    captureImageButton.setFocusPainted(false);
    captureImageButton.setIcon(imageCapt1);
    captureImageButton.setPressedIcon(imageCapt3);
    captureImageButton.setRolloverIcon(imageCapt2);
    captureImageButton.setMinimumSize(new Dimension(32, 32));
    captureImageButton.setBorder(null);
    captureImageButton.setMaximumSize(new Dimension(40, 40));
    captureName.setBackground(Color.gray);
    captureName.setFont(new java.awt.Font("SansSerif", 1, 12));
    captureName.setBorder(BorderFactory.createLoweredBevelBorder());
    captureName.setMaximumSize(new Dimension(80, 25));
    captureName.setMinimumSize(new Dimension(50, 40));
    captureName.setPreferredSize(new Dimension(80, 21));
    captureName.setCaretColor(Color.red);
    captureName.setFocusAccelerator(' ');
    captureName.setMargin(new Insets(8, 10, 10, 20));
    rotateButton.setBorder(null);
    rotateButton.setMaximumSize(new Dimension(40, 40));
    rotateButton.setMinimumSize(new Dimension(32, 32));
    rotateButton.setFocusPainted(false);
    rotateButton.setIcon(imageRot1);
    rotateButton.setPressedIcon(imageRot3);
    rotateButton.setRolloverIcon(imageRot2);
    rotateButton.setRolloverSelectedIcon(imageRot3);
    rotateButton.setSelectedIcon(imageRot3);
    rotateButton.setPreferredSize(new Dimension(40, 40));
    rotateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rotateButton_actionPerformed(e);
      }
    });
    YShiftUp.setBorder(null);
    YShiftUp.setFocusPainted(false);
    YShiftUp.setIcon(arrowUp1);
    YShiftUp.setPressedIcon(arrowUp2);
    YShiftUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        YShiftUp_actionPerformed(e);
      }
    });
    XrotateDown.setBorder(null);
    XrotateDown.setFocusPainted(false);
    XrotateDown.setHorizontalAlignment(SwingConstants.LEFT);
    XrotateDown.setIcon(arrowDown1);
    XrotateDown.setMargin(new Insets(0, 0, 0, 0));
    XrotateDown.setPressedIcon(arrowDown2);
    XrotateDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        XrotateDown_actionPerformed(e);
      }
    });
    XShifText.setText("0");
    ZShiftDown.setBorder(null);
    ZShiftDown.setFocusPainted(false);
    ZShiftDown.setIcon(arrowDown1);
    ZShiftDown.setPressedIcon(arrowDown2);
    ZShiftDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZShiftDown_actionPerformed(e);
      }
    });
    positionPanel.setBorder(BorderFactory.createEtchedBorder());
    positionPanel.setMinimumSize(new Dimension(180, 4));
    positionPanel.setPreferredSize(new Dimension(180, 4));
    positionPanel.setLayout(xYLayout3);
    ZShiftText.setText("0");
    YrotateDown.setBorder(null);
    YrotateDown.setFocusPainted(false);
    YrotateDown.setIcon(arrowDown1);
    YrotateDown.setPressedIcon(arrowDown2);
    YrotateDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        YrotateDown_actionPerformed(e);
      }
    });
    XShiftDown.setBorder(null);
    XShiftDown.setFocusPainted(false);
    XShiftDown.setIcon(arrowDown1);
    XShiftDown.setPressedIcon(arrowDown2);
    XShiftDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        XShiftDown_actionPerformed(e);
      }
    });
    ZrotateUp.setBorder(null);
    ZrotateUp.setFocusPainted(false);
    ZrotateUp.setIcon(arrowUp1);
    ZrotateUp.setPressedIcon(arrowUp2);
    ZrotateUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZrotateUp_actionPerformed(e);
      }
    });
    ZrotateDown.setBorder(null);
    ZrotateDown.setFocusPainted(false);
    ZrotateDown.setIcon(arrowDown1);
    ZrotateDown.setPressedIcon(arrowDown2);
    ZrotateDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZrotateDown_actionPerformed(e);
      }
    });
    XRotateText.setText("0");
    ZShiftUp.setBorder(null);
    ZShiftUp.setFocusPainted(false);
    ZShiftUp.setIcon(arrowUp1);
    ZShiftUp.setPressedIcon(arrowUp2);
    ZShiftUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZShiftUp_actionPerformed(e);
      }
    });
    YRotateText.setText("0");
    YShiftDown.setBorder(null);
    YShiftDown.setFocusPainted(false);
    YShiftDown.setIcon(arrowDown1);
    YShiftDown.setPressedIcon(arrowDown2);
    YShiftDown.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        YShiftDown_actionPerformed(e);
      }
    });
    YShiftText.setText("0");
    jLabel6.setText("X Shift");
    jLabel5.setText("Y Shift");
    jLabel4.setText("Z Shift");
    jLabel3.setText("Z Rotate");
    YrotateUp.setBorder(null);
    YrotateUp.setFocusPainted(false);
    YrotateUp.setIcon(arrowUp1);
    YrotateUp.setPressedIcon(arrowUp2);
    YrotateUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        YrotateUp_actionPerformed(e);
      }
    });
    jLabel2.setText("Y Rotate");
    ZRotateText.setText("0");
    jLabel1.setText("X Rotate");
    XrotateUp.setBorder(null);
    XrotateUp.setFocusPainted(false);
    XrotateUp.setHorizontalAlignment(SwingConstants.LEFT);
    XrotateUp.setIcon(arrowUp1);
    XrotateUp.setMargin(new Insets(0, 0, 0, 0));
    XrotateUp.setPressedIcon(arrowUp2);
    XrotateUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        XrotateUp_actionPerformed(e);
      }
    });
    XShiftUp.setBorder(null);
    XShiftUp.setFocusPainted(false);
    XShiftUp.setIcon(arrowUp1);
    XShiftUp.setPressedIcon(arrowUp2);
    XShiftUp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        XShiftUp_actionPerformed(e);
      }
    });
    jTabbedPane1.setTabPlacement(JTabbedPane.BOTTOM);
    jTabbedPane1.setToolTipText("1111");
    jPanel1.setMaximumSize(new Dimension(5, 30));
    jPanel1.setMinimumSize(new Dimension(5, 30));
    jPanel1.setPreferredSize(new Dimension(5, 30));
    updateButton.setMaximumSize(new Dimension(55, 18));
    updateButton.setMinimumSize(new Dimension(55, 18));
    updateButton.setPreferredSize(new Dimension(55, 18));
    updateButton.setToolTipText("");
    updateButton.setMargin(new Insets(0, 0, 0, 0));
    updateButton.setText("UPDATE");
    updateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateButton_actionPerformed(e);
      }
    });

    ImageUpdateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ImageUpdateButton_actionPerformed(e);
      }
    });

    visibleCheck.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        visibleCheck_actionPerformed(e);
      }
    });


    setTransfButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setTransfButton_actionPerformed(e);
      }
    });
    setTransfButton.setMaximumSize(new Dimension(55, 18));
    setTransfButton.setMinimumSize(new Dimension(55, 18));
    setTransfButton.setPreferredSize(new Dimension(55, 18));
    setTransfButton.setMargin(new Insets(0, 0, 0, 0));
    setTransfButton.setText("SET");
    managementPanel.setLayout(borderLayout3);

    InstrPanel.setBackground(Color.lightGray);
    InstrPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    InstrPanel.setMinimumSize(new Dimension(10, 100));
    InstrPanel.setLayout(verticalFlowLayout1);
    SplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    SplitPane.setBottomComponent(InstrPanel);
    SplitPane.setDividerSize(10);
    SplitPane.setOneTouchExpandable(true);

    ImageUpdateButton.setMaximumSize(new Dimension(83, 18));
    ImageUpdateButton.setMinimumSize(new Dimension(83, 18));
    ImageUpdateButton.setPreferredSize(new Dimension(83, 18));
    ImageUpdateButton.setText("UPDATE IMAGE");
    visibleCheck.setOpaque(false);
    visibleCheck.setText("visible");

    this.add(jPanel3, BorderLayout.SOUTH);
    jPanel3.add(hintText,   new XYConstraints(6, 3, 610, 9));
    this.add(CanvasPanel, BorderLayout.CENTER);
    CanvasPanel.add(zoomSlider, BorderLayout.WEST);
    this.add(toolBar, BorderLayout.NORTH);
    toolBar.add(initImageButton, null);
    toolBar.add(rotateButton, null);
    toolBar.add(captureImageButton, null);
    toolBar.add(jPanel1, null);
    toolBar.add(captureName, null);
    this.add(jTabbedPane1, BorderLayout.EAST);
    positionPanel.add(jLabel1, new XYConstraints(13, 17, 52, 15));
    positionPanel.add(XrotateUp, new XYConstraints(147, 5, 22, 19));
    positionPanel.add(XRotateText, new XYConstraints(68, 14, 72, 22));
    positionPanel.add(YrotateUp, new XYConstraints(147, 50, 22, 19));
    positionPanel.add(YRotateText, new XYConstraints(68, 57, 72, 22));
    positionPanel.add(jLabel2, new XYConstraints(13, 60, 52, 15));
    positionPanel.add(ZRotateText, new XYConstraints(68, 103, 72, 22));
    positionPanel.add(jLabel3, new XYConstraints(13, 106, 52, 15));
    positionPanel.add(ZrotateUp, new XYConstraints(147, 96, 22, 19));
    positionPanel.add(jLabel6, new XYConstraints(12, 153, 52, 15));
    positionPanel.add(jLabel5, new XYConstraints(12, 196, 52, 15));
    positionPanel.add(YShiftText, new XYConstraints(67, 193, 72, 22));
    positionPanel.add(ZShiftUp, new XYConstraints(146, 232, 22, 19));
    positionPanel.add(XShifText, new XYConstraints(67, 150, 72, 22));
    positionPanel.add(YShiftUp, new XYConstraints(146, 186, 22, 19));
    positionPanel.add(jLabel4, new XYConstraints(12, 242, 52, 15));
    positionPanel.add(ZShiftText, new XYConstraints(67, 239, 72, 22));
    positionPanel.add(XShiftUp, new XYConstraints(146, 142, 22, 19));
    positionPanel.add(XrotateDown,  new XYConstraints(145, 24, 22, 19));
    positionPanel.add(YShiftDown, new XYConstraints(144, 205, 22, 19));
    positionPanel.add(YrotateDown, new XYConstraints(145, 69, 22, 19));
    positionPanel.add(ZrotateDown, new XYConstraints(145, 115, 22, 19));
    positionPanel.add(XShiftDown, new XYConstraints(144, 161, 22, 19));
    positionPanel.add(ZShiftDown, new XYConstraints(144, 251, 22, 19));
    positionPanel.add(setTransfButton,      new XYConstraints(97, 281, 57, 37));
    positionPanel.add(updateButton,     new XYConstraints(38, 281, -1, 37));
    jTabbedPane1.add(managementPanel,  "Scene Tree");
    managementPanel.add(SplitPane, BorderLayout.CENTER);
    //managementPanel.add(TreeScrollPane, new XYConstraints(5, 3, 170, 273));
    jTabbedPane1.add(positionPanel, "Position");
    captureName.setText(globalSettings.defaultCaptureFileName);
    jTabbedPane1.setSelectedIndex(0);
  }

  void zoomSlider_stateChanged(ChangeEvent e) {
  float v = (zoomSlider.getValue())/100f;
  if(v<=0.5)
  v = 0.05f+(v-0.5f)*0.08f;
  else
  v = 0.05f+(v-0.5f)*0.5f;
  Universe.getViewer().getView().setScreenScale(v);
  //Universe.getCanvas().invalidate();
  //Universe.getCanvas().paint(Universe.getCanvas().getGraphics());
  //Universe.getCanvas().update(Universe.getCanvas().getGraphics());
  //hintText.setText(new Float(v).toString());
  }


  void initImageButton_actionPerformed(ActionEvent e) {
  zoomSlider.setValue(50);
  objTrans.setTransform(new Transform3D());
  readTransform();
  }

  void captureImageButton_actionPerformed(ActionEvent e) {
  Canvas.captureFileName = captureName.getText()+".gif";
  Canvas.captureSize = new Dimension(Canvas.getWidth(),Canvas.getHeight());
  Canvas.writeGIF_ = true;
  Canvas.postSwap();
  }

  void rotateButton_actionPerformed(ActionEvent e) {
  rotate();
  }

  void updateButton_actionPerformed(ActionEvent e) {
  readTransform();
  }
  void setTransfButton_actionPerformed(ActionEvent e) {
  setTransform();
  }

  void XrotateUp_actionPerformed(ActionEvent e) {
  double x = Math.rint(Double.parseDouble(XRotateText.getText())/10)*10;
  XRotateText.setText(new Double(x+10).toString());
  }

  void XrotateDown_actionPerformed(ActionEvent e) {
  double x = Math.rint(Double.parseDouble(XRotateText.getText())/10)*10;
  XRotateText.setText(new Double(x-10).toString());
  }

  void YrotateUp_actionPerformed(ActionEvent e) {
  double y = Math.rint(Double.parseDouble(YRotateText.getText())/10)*10;
  YRotateText.setText(new Double(y+10).toString());
  }

  void YrotateDown_actionPerformed(ActionEvent e) {
  double y = Math.rint(Double.parseDouble(YRotateText.getText())/10)*10;
  YRotateText.setText(new Double(y-10).toString());
  }

  void ZrotateUp_actionPerformed(ActionEvent e) {
  double z = Math.rint(Double.parseDouble(ZRotateText.getText())/10)*10;
  ZRotateText.setText(new Double(z+10).toString());
  }

  void ZrotateDown_actionPerformed(ActionEvent e) {
  double z = Math.rint(Double.parseDouble(ZRotateText.getText())/10)*10;
  ZRotateText.setText(new Double(z-10).toString());
  }

  void XShiftUp_actionPerformed(ActionEvent e) {
  float x = (float)(Math.rint(Double.parseDouble(XShifText.getText())*10d)/10f);
  XShifText.setText(new Float(x+0.1).toString());
  }

  void XShiftDown_actionPerformed(ActionEvent e) {
  float x = (float)Math.rint(Float.parseFloat(XShifText.getText())*10d)/10f;
  XShifText.setText(new Float(x-0.1).toString());
  }

  void YShiftUp_actionPerformed(ActionEvent e) {
  float y = (float)Math.rint(Float.parseFloat(YShiftText.getText())*10d)/10f;
  YShiftText.setText(new Float(y+0.1).toString());
  }

  void YShiftDown_actionPerformed(ActionEvent e) {
  float y = (float)Math.rint(Float.parseFloat(YShiftText.getText())*10d)/10f;
  YShiftText.setText(new Float(y-0.1).toString());
  }

  void ZShiftUp_actionPerformed(ActionEvent e) {
  float z = (float)Math.rint(Float.parseFloat(ZShiftText.getText())*10d)/10f;
  ZShiftText.setText(new Float(z+0.1).toString());
  }

  void ZShiftDown_actionPerformed(ActionEvent e) {
  float z = (float)Math.rint(Float.parseFloat(ZShiftText.getText())*10d)/10f;
  ZShiftText.setText(new Float(z-0.1).toString());
  }

  public void fillTree(){
  DefaultMutableTreeNode top =  new DefaultMutableTreeNode("Scene elements");
  DefaultMutableTreeNode s1 =  new DefaultMutableTreeNode("Axes");
  top.add(s1);
  for(int i=0; i<viewerDataImage.elements.size(); i++){
  VDataImageElement vie = (VDataImageElement)(viewerDataImage.elements.elementAt(i));
  s1 =  new DefaultMutableTreeNode(vie);
  //s1.setUserObject(vie);
  top.add(s1);
  if(vie instanceof VDataPointSet){
     if(viewerDataImage.classes==null){
     HashSet vd = ((VDataPointSet)(vie)).getDescriptorSet().getUniqueList();
     for(Iterator j=vd.iterator();j.hasNext();){
        DefaultMutableTreeNode s2 =  new DefaultMutableTreeNode(j.next());
        s1.add(s2);
        }
     }else{
     for(int j=0;j<viewerDataImage.classes.getClassVector().size();j++){
        DefaultMutableTreeNode s2 =  new DefaultMutableTreeNode(viewerDataImage.classes.getClassVector().elementAt(j));
        s1.add(s2);
        }
     }
  }
  }
  elementTree = new JTree(top);
  elementTree.setCellRenderer(new Viewer3DTreeCellRenderer());
  TreeScrollPane = new JScrollPane(elementTree);
  TreeScrollPane.getViewport().setBackground(Color.gray);
  TreeScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
  Dimension minimumSize = new Dimension(100, 50);
  TreeScrollPane.setMinimumSize(minimumSize);
  //managementPanel.add(TreeScrollPane, BorderLayout.CENTER);
  SplitPane.setTopComponent(TreeScrollPane);
   //SplitPane.setOneTouchExpandable(true);

  elementTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

  elementTree.addTreeSelectionListener(new TreeSelectionListener() {
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           elementTree.getLastSelectedPathComponent();

        if (node == null) return;
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
        } else {
        }
        updateInstrumentPanel(nodeInfo);
    }
  });

  }

  public void updateInstrumentPanel(Object Info){
  InstrPanel.removeAll();
  if(Info!=null){
  if(Info instanceof VDataImageElement){
    VDataImageElement vie = (VDataImageElement)(Info);
    InstrPanel.add(visibleCheck, null);
    visibleCheck.setSelected(vie.visible);
    }
  if(Info instanceof VObjectDescriptor){
    VObjectDescriptor vie = (VObjectDescriptor)(Info);
    InstrPanel.add(visibleCheck, null);
    visibleCheck.setSelected(vie.visible);
    }
  if(Info instanceof VDataClass){
    VDataClass vie = (VDataClass)(Info);
    InstrPanel.add(visibleCheck, null);
    visibleCheck.setSelected(vie.descriptor.visible);
    }
  if(Info.equals("Axes")){
  InstrPanel.add(visibleCheck, null);
  visibleCheck.setSelected(viewerDataImage.drawDefaultAxes);
  }
  }
  InstrPanel.add(ImageUpdateButton, null);
  InstrPanel.update(InstrPanel.getGraphics());
  }

  void ImageUpdateButton_actionPerformed(ActionEvent e){
  reconstructImage();
  }

  public void reconstructImage(){
  sceneFromImage.detach();
  sceneFromImage = viewerDataImage.createSceneGraph();
  sceneFromImage.setCapability(sceneFromImage.ALLOW_DETACH);
  objTrans.addChild(sceneFromImage);
  }

  void visibleCheck_actionPerformed(ActionEvent e){
  DefaultMutableTreeNode node = (DefaultMutableTreeNode) elementTree.getLastSelectedPathComponent();
  if(node.getUserObject() instanceof VDataImageElement){
  ((VDataImageElement)(node.getUserObject())).visible = visibleCheck.isSelected();
  }
  if(node.getUserObject() instanceof VObjectDescriptor){
  ((VObjectDescriptor)(node.getUserObject())).visible = visibleCheck.isSelected();
  }
  if(node.getUserObject() instanceof VDataClass){
  VObjectDescriptor od = ((VDataClass)(node.getUserObject())).descriptor;
  //System.out.println(od.toString());
  od.visible = visibleCheck.isSelected();
  }
  if(node.getUserObject().equals("Axes")){
  viewerDataImage.drawDefaultAxes = visibleCheck.isSelected();
  }
  }

  void jButton1_actionPerformed(ActionEvent e) {
  Universe.getViewer().getView().setScreenScale(0.01);
  }



}