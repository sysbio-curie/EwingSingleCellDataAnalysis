package vdaoengine;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

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

public class TestApplet extends Applet {

  public BufferedImage im = null;
  VDataSet VD = null;
  VDataSet VDNormal = null;
  VDataSet VDTransposed = null;
  int problemType = 1; // 0 - normal, 1 - transposed
  String settings = null;
  String dat = null;

  VClassifier Cl = null;
  VObjectDescriptor D = null;
  VDataImage imag = null;
  VDataImage2D im2d = null;
  VContLayer layer = null;
  PCAMethod PCA = null;
  ElmapProjection elmap = null;
  Vector selected = new Vector();
  Vector selectedIDs = new Vector();
  Vector selected_desc = new Vector();

  VDataPointSet points = null;
  VObjectDescriptor selectedDescriptor = new VObjectDescriptor();

  VContLayer PCADensityLayer2D = null;
  VContLayer ElmapDensityLayer2D = null;

  VContLayer PCADensityLayerND = null;
  VContLayer ElmapDensityLayerND = null;

  VContLayer PCALayer2D = null;
  VContLayer ElmapLayer2D = null;

  double PCAMaxDesity2D  = 1;
  double ElmapMaxDesity2D  = 1;
  double PCAMaxDesityND  = 1;
  double ElmapMaxDesityND  = 1;

  VDataSet PCAproj = null;
  VDataSet Elmapproj = null;

  int visualizationMode = 0; // 0 - PCA, 1 - elmap, 2 - Kernel PCA
  int gridResolution = 50;

  private boolean isStandalone = false;
  public String var_datfile = "Not found";
  public String var_problemtype = "transposed";
  public String var_settings;
  public String var_proxy;
  private Panel imagepanel = new Panel();
  private Button button1 = new Button();
  private TextArea textArea = new TextArea();
  private Label label1 = new Label();
  private Button button2 = new Button();
  private JRadioButton jRadioButton1 = new JRadioButton();
  private JRadioButton jRadioButton2 = new JRadioButton();
  private JRadioButton jRadioButton3 = new JRadioButton();
  private ButtonGroup Rgroup = new ButtonGroup();
  private JToolBar jToolBar1 = new JToolBar();
  private Choice choice1 = new Choice();
  private Checkbox RDcheck = new Checkbox();
  private Checkbox NDcheck = new Checkbox();
  private Choice taskTypeChoice = new Choice();
  private Checkbox LNcheck = new Checkbox();
  private Checkbox cbSizes = new Checkbox();
  private Choice choice2 = new Choice();

  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  public void prepareImageTest(){
  }

  public boolean getData(){

    boolean r = true;
    if(VD==null){
    dat = VDownloader.downloadURL(var_datfile);
    if(dat.equals(var_datfile)) dat = "Not found";
    if(dat==null){
      System.out.println("The dat string is null...\n");
    }
    else
    if(dat.length()>99)
      System.out.println(dat.substring(0,100)+"\n...\n"+dat.substring(dat.length()-100,dat.length())+"\n");
    else
      System.out.println("The dat string has less than 100 chars...\n"+dat+"\n");
    if((dat.indexOf("denied")==-1)&&(dat.indexOf("refused")==-1)&&(dat.indexOf("Not found")==-1)){
      VDNormal = VSimpleProcedures.SimplyPreparedDatasetFromDatString(dat,"data",-1);
      //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(VDNormal.table,"c:/datas/sabrina/test.dat");
      VDNormal = addRequiredFields(VDNormal);
      VD = VDNormal;
      Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"CLASS",VClassifier.CHANGE_FILLCOLOR);
      // Let us transpose it right now
      if(var_problemtype.equals("transposed")){
        TransposedProblem();
        Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"CLASS",VClassifier.CHANGE_FILLCOLOR);
      }
      }else r = false;
    }
    return r;
  }

  public void setProxy(){
    if(!var_proxy.equals("")){
      StringTokenizer st = new StringTokenizer(var_proxy,":");
      System.getProperties().put("proxySet","true");
      String host = st.nextToken();
      System.getProperties().put("proxyHost",host);
      String port = "8080";
      if(st.hasMoreTokens())
        port = st.nextToken();
      System.getProperties().put("proxyPort",port);
    }
  }

  public boolean getSettings(){
    boolean r = true;

    settings = VDownloader.downloadURL(var_settings);
    if(settings.equals(var_settings)) settings = "Not found";
    if((settings.indexOf("denied")==-1)&&(settings.indexOf("refused")==-1)&&(settings.indexOf("Not found")==-1)){
    }else r = false;
    return r;
  }


  public void fillElements(){
    if(taskTypeChoice.getItemCount()==0){
    taskTypeChoice.removeAll();
    taskTypeChoice.add("Direct problem");
    taskTypeChoice.add("Transposed");
    }
    choice1.removeAll();
    choice1.add("Density");
    Vector clas = Cl.getClassVector();
    for(int i=0;i<clas.size();i++){
      String s = ((VDataClass)clas.elementAt(i)).name;
      if(s.startsWith("CLASS=\"")){
        s = s.substring(7,s.length()-1);
      }
      choice1.add(s+" density");
    }
    //for(int i=0;i<VD.selector.selectedColumns.length;i++)
    //  System.out.println(VD.selector.selectedColumns[i]);
    //Date t = new Date();
    Vector cls1 = new Vector();
    VD.selector.hashSelectedColumns();
    for(int i=0;i<Math.min(VD.table.colCount,100);i++){
      if(VD.selector.isColumnSelectedH(i)){
        if(cls1.indexOf(VD.table.fieldClasses[i])<0)
            cls1.add(VD.table.fieldClasses[i]);
      }
    }
    for(int i=0;i<cls1.size();i++){
      choice1.add((String)cls1.elementAt(i));
    }
    //System.out.println("In fillElements2 "+(new Date().getTime()-t.getTime())+" ms");
    //System.out.println(""+VD.table.colCount+"columns");
    for(int i=0;i<Math.min(VD.table.colCount,100);i++){
      if(VD.selector.isColumnSelectedH(i))
         choice1.add(VD.table.fieldNames[i]);
    }
    //System.out.println("In fillElements3 "+(new Date().getTime()-t.getTime())+" ms");
    int ind = choice2.getSelectedIndex();
    if(numberOfClasses()<=1) choice2.setVisible(false);
    if(numberOfClasses()>1){
      choice2.setVisible(true);
      choice2.removeAll();
      for(int i=0;i<numberOfClasses();i++){
        choice2.add("CLASS"+(i+1));
      }
      if(ind>=0)
        choice2.select(ind);
    }

  }

  public void prepareImage(){
    try{
    selected.clear();
    selectedIDs.clear();
    selected_desc.clear();
    im = new BufferedImage(500,520,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = im.createGraphics();
    g.setFont(new Font("Arial Black",0,15));
    g.drawString(var_datfile,10,30);
    g.drawString(var_settings,10,100);

    setProxy();
    boolean setLoaded = getSettings();
    boolean dataLoaded = getData();
    if((!setLoaded)||(!dataLoaded))
    {
    if(!setLoaded){
      g.drawString(settings.substring(0,Math.min(50,settings.length())),10,120);
      if(settings.length()>50)
        g.drawString(settings.substring(50,Math.min(100,settings.length())),10,135);
      if(settings.length()>100)
        g.drawString(settings.substring(100),10,100);
    }
    if(!dataLoaded){
      g.drawString(dat.substring(0,Math.min(50,dat.length())),10,50);
      if(dat.length()>50)
        g.drawString(dat.substring(50,Math.min(100,dat.length())),10,65);
      if(dat.length()>100)
        g.drawString(dat.substring(100),10,100);
    }
    }else{
    fillElements();
    if(var_problemtype.equals("transposed"))
      taskTypeChoice.select(1);
    else
      taskTypeChoice.select(0);
    switch(visualizationMode){
    case 0: createPCAImage(); break;
    case 1: createElmapImage(); break;
    }
    }
    }catch(Exception e){
      e.printStackTrace();
      textArea.setText(e.toString());
      //try{
      //PrintWriter wr = new PrintWriter(new FileWriter("temp"));
      //e.printStackTrace(wr);
      //}catch(Exception ee){
      //  ee.printStackTrace();
      //}
      //e.printStackTrace();
    }
  }

  public void paint(Graphics g){
    Graphics2D g2 = (Graphics2D) getGraphics();
    Dimension d = getSize();
    g2.setBackground(getBackground());
    g2.clearRect(0, 0, d.width, d.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
    if(im!=null)
        g2.drawImage(im,null,0,0);
    imagepanel.setBounds(0,(int)(2.8f*this.getHeight()/4)-20,this.getWidth(), this.getHeight());
  }

  //Construct the applet
  public TestApplet() {
  }
  //Initialize the applet
  public void init() {
    try {
      var_datfile = "http://www.ihes.fr/~zinovyev/test/out.dat";
      //var_datfile = "file:///C:/Datas/wtf.dat";
      //var_datfile = "file:///C:/Datas/CUBISM/test.dat";

      var_datfile = this.getParameter("datfile", var_datfile);
      var_settings = this.getParameter("settings_file","file:///C:/MyPrograms/VDAOEngine/VDAOEngine/elmap.ini");
      //var_settings = this.getParameter("settings_file","http://www.ihes.fr/~zinovyev/test/elmap.ini");
      var_proxy = this.getParameter("proxy","");
      var_problemtype = this.getParameter("problemtype","transposed");

      globalSettings.AddMoreStandardColors(22);

      //var_datfile = "file:///C:/MyPrograms/Vimida/examples/sein/out_ap.dat";
      //var_datfile = "file:///C:/MyPrograms/Vimida/examples/gliome/out.dat";
      //var_datfile = "file:///C:/MyPrograms/Vimida/examples/neuro/out.dat";
      //var_datfile = "file:///C:/MyPrograms/Vimida/examples/sarcome/out.dat";
      //var_datfile = "file:///C:/Datas/Sabrina/transposed.dat";
      //var_datfile = "file:///C:/Datas/Sabrina/norm5a_spc12.dat";
      //var_datfile = "file:///C:/MyPrograms/VDAOEngine/VDAOEngine/chr19a.dat";
      //var_datfile = "file:///C:/Datas/Melanome/melanomefn1.dat";
      //var_datfile = "file:///c:/datas/sabrina/ttt/145_gcrma1.dat";
      //var_datfile = "file:///c:/datas/neuroblastome/nb_nf8.dat";
      //var_datfile = "file:///c:/datas/breastcancer/old/abn_af.dat";
      //var_datfile = "file:///c:/datas/breastcancer/dataAB/afab.dat";
      //var_datfile = "file:///c:/datas/vimida/wangn5000.dat";
      //var_datfile = "file:///c:/datas/sample.dat";
      //var_datfile = "file:///c:/datas/sabrina/230_nda100.dat";

      //var_datfile = "file:///c:/datas/princmanif2006/blcancer/d2f3000.dat";
      var_datfile = "file:///c:/datas/test.dat";

      }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception {
    this.setLayout(null);
    imagepanel.setBackground(new Color(118, 197, 92));
    imagepanel.setBounds(new Rectangle(2, 4, 461, 141));
    imagepanel.setLayout(null);
    this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        this_mouseMoved(e);
      }
    });
    button2.setLabel("Label all");
    button2.setBounds(new Rectangle(7, 29, 120, 20));
    button2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button2_actionPerformed(e);
      }
    });
    button2.setFont(new java.awt.Font("Dialog", 1, 12));
    button1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button1_actionPerformed(e);
      }
    });
    this.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        this_mouseClicked(e);
      }
    });
    label1.setBounds(new Rectangle(0, 150, 120, 19));
    textArea.setBounds(new Rectangle(136, 7, (int)(getWidth()-150), 90));
    //Font f = textArea.getFont();
    //textArea.setFont();
    button1.setBounds(new Rectangle(7, 7, 120, 20));
    jRadioButton1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jRadioButton1_mouseClicked(e);
      }
    });
    jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jRadioButton2_mouseClicked(e);
      }
    });
    choice1.setBounds(new Rectangle(252, 106, 104, 19));
    choice1.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        choice1_itemStateChanged(e);
      }
    });
    RDcheck.setLabel("RD");
    RDcheck.setBounds(new Rectangle(360, 106, 39, 19));
    RDcheck.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        RDcheck_itemStateChanged(e);
      }
    });
    NDcheck.setBounds(new Rectangle(400, 106, 39, 19));
    NDcheck.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        NDcheck_itemStateChanged(e);
      }
    });
    NDcheck.setLabel("ND");
    taskTypeChoice.setBounds(new Rectangle(135, 106, 113, 19));
    taskTypeChoice.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        taskTypeChoice_itemStateChanged(e);
      }
    });
    LNcheck.setLabel("LN");
    LNcheck.setVisible(false);
    LNcheck.setBounds(new Rectangle(440, 106, 39, 19));
    cbSizes.setLabel("show by size");
    cbSizes.setVisible(false);
    cbSizes.setBounds(new Rectangle(253, 124, 97, 25));
    cbSizes.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        cbSizes_mouseClicked(e);
      }
    });

    choice2.setBounds(new Rectangle(252, 128, 104, 19));
    choice2.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        choice2_itemStateChanged(e);
      }
    });
    Rgroup.add(jRadioButton1);
    Rgroup.add(jRadioButton2);
    Rgroup.add(jRadioButton3);
    jRadioButton1.setOpaque(false);
    jRadioButton1.setSelected(true);
    jRadioButton1.setText("Linear PCA");
    jRadioButton1.setBounds(new Rectangle(7, 55, 103, 25));
    jRadioButton2.setBounds(new Rectangle(7, 78, 103, 25));
    jRadioButton2.setOpaque(false);
    jRadioButton2.setText("Elastic Map");
    jRadioButton3.setBounds(new Rectangle(7, 101, 103, 25));
    jRadioButton3.setEnabled(false);
    jRadioButton3.setOpaque(false);
    jRadioButton3.setText("Kernel PCA");
    jToolBar1.setBounds(new Rectangle(1, 347, 458, 14));
    //System.out.println(this.getHeight());
    //System.out.println(this.getWidth());
    button1.setFont(new java.awt.Font("Dialog", 1, 12));
    button1.setLabel("RESET");
    label1.setText("");
    label1.setBounds(label1.getX(),label1.getY(),getWidth(),label1.getHeight());
    //this.add(imagepanel,  new XYConstraints(0, 209, 468, 152));
    imagepanel.add(button1, null);
    imagepanel.add(textArea, null);
    imagepanel.add(button2, null);
    imagepanel.add(jRadioButton1, null);
    imagepanel.add(jRadioButton2, null);
    imagepanel.add(jRadioButton3, null);
    imagepanel.add(label1, null);
    imagepanel.add(taskTypeChoice, null);
    imagepanel.add(choice1, null);
    imagepanel.add(RDcheck, null);
    imagepanel.add(NDcheck, null);
    imagepanel.add(LNcheck, null);
    imagepanel.add(cbSizes, null);
    imagepanel.add(choice2, null);
    //this.add(choice1, null);
    this.add(jToolBar1, null);
    this.add(imagepanel, null);
  }
  //Start the applet
  public void start() {
    prepareImage();
    imagepanel.setBounds(0,(int)(3f*this.getHeight()/4),this.getWidth(), this.getHeight());
    imagepanel.setVisible(true);
    updateImage();
  }
  //Stop the applet
  public void stop() {
  }
  //Destroy the applet
  public void destroy() {
  }
  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    String[][] pinfo =
      {
      {"datfile", "String", ""},
      };
    return pinfo;
  }
  //Main method
  public static void main(String[] args) {
    TestApplet applet = new TestApplet();
    applet.isStandalone = true;
    Frame frame;
    frame = new Frame() {
      protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
          System.exit(0);
        }
      }
      public synchronized void setTitle(String title) {
        super.setTitle(title);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      }
    };
    frame.setTitle("Applet Frame");
    frame.setSize(700,620);
    frame.add(applet, BorderLayout.CENTER);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
    applet.init();
    applet.start();

  }

  void this_mouseMoved(MouseEvent e) {
    if(im2d!=null){
    int ID = im2d.findClosestPoint(e.getX(),e.getY());
    //Vector ids = im2d.findAllClosestPoints(e.getX(),e.getY());
    String s[] = VD.table.getRowByID(ID);
    int iname = VD.table.fieldNumByName("NAME");
    int iclas = VD.table.fieldNumByName("CLASS");
    int idesc = VD.table.fieldNumByName("DESCRIPTION");

    String name = s[iname];
    String classname = s[iclas];
    String desc = s[idesc];
    VObjectDescriptor od = VD.getDescriptors().getDescriptor(ID);

    label1.setText("CLASS: "+classname+", ID: "+name+", DESC: "+desc);
    //label1.setBounds(0,imagepanel.getHeight()-50,imagepanel.getWidth(),19);
    //System.out.println(""+label1.getX()+","+label1.getY());
    label1.setBackground(od.getFillColor());
    }
    //paint(getGraphics());
    //getGraphics().drawOval(e.getX()-5,e.getY()-5,10,10);
  }

  void button2_actionPerformed(ActionEvent e) {
    ((VObjectSet)imag.elements.elementAt(0)).annotateByField(VD.table,"NAME");
    updateImage();
  }

  public void updateImage(){
    if(imag!=null){
    im2d = new VDataImage2D(imag);
    im = im2d.createBufferedImage(getWidth(),(int)(2.8f/4*getHeight())-20);
    }else{

    }
    paint(getGraphics());
  }

  void button1_actionPerformed(ActionEvent e) {
    imag.imageActual = false;
    prepareImage();
    updateImage();
    updateSelected();
  }

  void this_mouseClicked(MouseEvent e) {
    if((e.getModifiers()&(e.BUTTON1_MASK))!=0){
    if(im2d!=null){
    //int ID = im2d.findClosestPoint(e.getX(),e.getY());
    Vector ids = im2d.findAllClosestPoints(e.getX(),e.getY());
    for(int i=0;i<ids.size();i++){
    int ID = ((Integer)ids.elementAt(i)).intValue();
    String s[] = VD.table.getRowByID(ID);

    int iname = VD.table.fieldNumByName("NAME");
    int idesc = VD.table.fieldNumByName("DESCRIPTION");

    String name = s[iname];
    String desc = s[idesc];

    int k = selected.indexOf(name);
    //System.out.println(k);
    if(k!=-1){
    selected.remove(k);
    selectedIDs.remove(k);
    selected_desc.remove(k);
    //points.getDescriptorSet().removeDescriptor(ID);
    points.setAnnotation(ID,"");
    }else{
    selected.addElement(name);
    selectedIDs.addElement(new Integer(ID));
    selected_desc.addElement(desc);
    //points.getDescriptorSet().addDescriptor(ID,selectedDescriptor);
    iname = VD.table.fieldNumByName("NAME");
    name = s[iname];
    points.setAnnotation(ID,name);
    }
    }
    }
    updateImage();
    updateSelected();
    }
  }

  void updateSelected(){
    textArea.setText("");
    for(int i=0;i<selected.size();i++){
      textArea.append((String)selected.elementAt(i)+": "+(String)selected_desc.elementAt(i)+"\n");
    }
    if(im2d.getImage()!=null)if(im2d.getImage()!=null)if(points!=null){
    im2d.drawPointSet(im2d.getImage().createGraphics(),im2d.getImage(),points);
    }
    im = im2d.getImage();
  }

  void updateAnnotations(){
    for(int i=0;i<selected.size();i++){
      int ID = ((Integer)selectedIDs.elementAt(i)).intValue();
      String name = (String)selected.elementAt(i);
      points.setAnnotation(ID,name);
    }
  }


  void createPCAImage() throws Exception{

    if(PCA==null){
      PCA = new PCAMethod();
      PCA.setDataSet(VD);
      PCA.calcBasis(2);
    }

    /*FileWriter fw = null;
    try{
      fw = new FileWriter("c:/datas/breastcancer/old/pca");
      //fw.write(PCA.getBasis().toString());
      for (int i = 0; i < PCA.getBasis().spaceDimension; i++){
        for(int j=0; j<PCA.getBasis().numberOfBasisVectors;j++){
              fw.write(""+PCA.getBasis().basis[j][i]+"\t");
      }
      fw.write("\r\n");
      }

    }catch(Exception e){
      e.printStackTrace();
    }
    fw.close();
    */
    imag = PCA.updateDataImage();
    points = (VDataPointSet)imag.elements.elementAt(0);
    updateAnnotations();

    imag.DiscreteColor = 10;

    im2d = new VDataImage2D(imag);
    globalSettings.defaultPointBorder = true;
    globalSettings.defaultPointBorderColor = Color.black;
    globalSettings.defaultPointSize = 5f;
    im = im2d.createBufferedImage(getWidth(),(int)(2.8f/4*getHeight())-20);

    selectedDescriptor.setFillColor(Color.red);

    calculateLayer();
  }

  void createElmapImage() throws Exception{

    if(elmap==null){
      elmap = new ElmapProjection();
      ElmapAlgorithm ela = new ElmapAlgorithm();
      ela.setData(VD);
      ela.readIniFileStr(settings,8);
      ela.computeElasticGrid();
      elmap.setElmap(ela);
      ela.grid.saveToFile("out_elmap.vem","test");
      //ela.grid.saveProjections("proj1",ela.data,ela.grid.PROJECTION_CLOSEST_POINT);
    }

    imag = elmap.updateDataImage();
    points = (VDataPointSet)imag.elements.elementAt(0);
    updateAnnotations();

    imag.DiscreteColor = 10;
    im2d = new VDataImage2D(imag);
    globalSettings.defaultPointBorder = true;
    globalSettings.defaultPointBorderColor = Color.black;
    globalSettings.defaultPointSize = 5f;

    im = im2d.createBufferedImage(getWidth(),(int)(2.8f/4*getHeight())-20);


    selectedDescriptor.setFillColor(Color.red);

    calculateLayer();
   }

  void jRadioButton1_mouseClicked(MouseEvent e) {
    try{
    createPCAImage();
    visualizationMode = 0;
    //choice1.select(0);
    calculateLayer();
    updateImage();
    }catch(Exception ex){
      ex.printStackTrace();
      textArea.setText(ex.toString());
    }

  }

  void jRadioButton2_mouseClicked(MouseEvent e) {
    try{
    createElmapImage();
    visualizationMode = 1;
    //choice1.select(0);
    calculateLayer();
    updateImage();
    }catch(Exception ex){
     ex.printStackTrace();
     textArea.setText(ex.toString());
    }

  }

  void substituteLayer(VContLayer lay){
    clearContLayers();
    imag.elements.add(lay);
  }

  void clearContLayers(){
    int i=0;
    while(i<imag.elements.size()){
      if(imag.elements.elementAt(i) instanceof VContLayer){
        imag.elements.remove(i);
      }else i++;
    }
  }

  void calculatePCADensity2D(){
    if(PCADensityLayer2D==null){
        PCAproj = PCA.getProjectedDataset();
        VFunction funct = VFunctionCalculator.VCalculate2DGridForFunction(PCAproj,gridResolution,gridResolution);
        //PCAMaxDesity2D = VFunctionCalculator.VCalculateDensityContrasted(PCAproj,0.8f,2f,funct);
        PCAMaxDesity2D = VFunctionCalculator.VCalculateDensity(PCAproj,0.8f,funct);
        PCADensityLayer2D = new VContLayer("Density");
        PCADensityLayer2D.function = funct;
    }
  }

  void calculatePCADensityND(){
  if(PCADensityLayerND==null){
      PCAproj = PCA.getProjectedDataset();
      VFunction funct = VFunctionCalculator.VCalculate2DGridForFunction(PCAproj,gridResolution,gridResolution);
      PCAMaxDesityND = VFunctionCalculator.VCalculateDensityND(VD,PCA,1f,funct);
      PCADensityLayerND = new VContLayer("Density");
      PCADensityLayerND.function = funct;
  }
  }

 void calculateElmapDensity2D(){
  if(ElmapDensityLayer2D==null){
      Elmapproj = elmap.getProjectedDataset();
      VFunction funct = VFunctionCalculator.VCalculate2DGridForFunction(Elmapproj,gridResolution,gridResolution);
      ElmapMaxDesity2D = VFunctionCalculator.VCalculateDensity(Elmapproj,0.8f,funct);
      //ElmapMaxDesity2D = VFunctionCalculator.VCalculateDensityContrasted(Elmapproj,0.8f,2f,funct);
      ElmapDensityLayer2D = new VContLayer("Density");
      ElmapDensityLayer2D.function = funct;
          }
 }

 void calculateElmapDensityND(){
 if(ElmapDensityLayerND==null){
    Elmapproj = elmap.getProjectedDataset();
    VFunction funct = VFunctionCalculator.VCalculate2DGridForFunction(Elmapproj,gridResolution,gridResolution);
    ElmapMaxDesityND = VFunctionCalculator.VCalculateDensityND(VD,elmap,1f,funct);
    ElmapDensityLayerND = new VContLayer("Density");
    ElmapDensityLayerND.function = funct;
}
 }


  void calculateLayer(){

    String s = choice1.getSelectedItem();
    RDcheck.setEnabled(false);
    NDcheck.setEnabled(false);

    if(s.indexOf("Densi")>=0){
      NDcheck.setEnabled(true);
      cbSizes.setVisible(false);
      switch(visualizationMode){
        case 0:
          if(!NDcheck.getState()){
            calculatePCADensity2D();
            substituteLayer(PCADensityLayer2D);}
          else{
            calculatePCADensityND();
            substituteLayer(PCADensityLayerND);
          }
        break;
        case 1:
          if(!NDcheck.getState()){
            calculateElmapDensity2D();
            substituteLayer(ElmapDensityLayer2D);
          }else{
            calculateElmapDensityND();
            substituteLayer(ElmapDensityLayerND);
          }
        break;
      }
    }

    if(s.indexOf("densi")>=0){

      RDcheck.setEnabled(true);
      NDcheck.setEnabled(true);
      cbSizes.setVisible(false);

      VFunction funct;
      VTableSelector sel = new VTableSelector();
      String clname = s.substring(0,s.indexOf(" "));
      VSelectorCriteria crit = new VSelectorCriteria("CLASS",clname,VSelectorCriteria.EQUAL);
      sel.selectRowsByCriteria(VD.table,crit);

      switch(visualizationMode){
        case 0:
          if(!NDcheck.getState()){
            funct = VFunctionCalculator.VCalculate2DGridForFunction(PCAproj,gridResolution,gridResolution);
            PCALayer2D = new VContLayer(s);
            calculatePCADensity2D();
            VFunctionCalculator.VCalculateDensityClass(PCAproj,sel,1f,funct,PCAMaxDesity2D,PCADensityLayer2D.function,RDcheck.getState());
            PCALayer2D.function = funct;
          }else{
            funct = VFunctionCalculator.VCalculate2DGridForFunction(PCAproj,gridResolution,gridResolution);
            PCALayer2D = new VContLayer(s);
            calculatePCADensityND();
            VFunctionCalculator.VCalculateDensityClassND(VD,PCA,sel,1f,funct,PCAMaxDesityND,PCADensityLayerND.function,RDcheck.getState());
            PCALayer2D.function = funct;
          }
          substituteLayer(PCALayer2D);
        break;
        case 1:
          if(!NDcheck.getState()){
            funct = VFunctionCalculator.VCalculate2DGridForFunction(Elmapproj,gridResolution,gridResolution);
            ElmapLayer2D = new VContLayer(s);
            calculateElmapDensity2D();
            VFunctionCalculator.VCalculateDensityClass(Elmapproj,sel,1f,funct,ElmapMaxDesity2D,ElmapDensityLayer2D.function,RDcheck.getState());
            ElmapLayer2D.function = funct;
          }else{
            funct = VFunctionCalculator.VCalculate2DGridForFunction(Elmapproj,gridResolution,gridResolution);
            ElmapLayer2D = new VContLayer(s);
            calculateElmapDensityND();
            VFunctionCalculator.VCalculateDensityClassND(VD,elmap,sel,1f,funct,ElmapMaxDesityND,ElmapDensityLayerND.function,RDcheck.getState());
            ElmapLayer2D.function = funct;
          }
          substituteLayer(ElmapLayer2D);
        break;
      }
    }

    int fieldNum = VD.table.fieldNumByName(s);
    if(fieldNum!=-1){
      int coordnum = -1;
      cbSizes.setVisible(true);
      if(cbSizes.getState())
          points.calculateSizesByFieldValue(VD,s,4,12,-1,1);
      else
          points.sizes = null;
      for(int i=0;i<VD.selector.selectedColumns.length;i++){
        if(VD.selector.selectedColumns[i]==fieldNum)
          coordnum = i;
      }
      if(coordnum!=-1){
        VFunction funct;
        VLinearFunction lf = new VLinearFunction();
        lf.setDimension(VD.coordCount);
        double sh = 0; double co[] = new double[VD.coordCount];
        co[coordnum] = 1;
        lf.set(sh,co);
        switch(visualizationMode){
          case 0:
            funct = VFunctionCalculator.VCalculate2DGridForFunction(PCAproj,gridResolution,gridResolution);
            PCALayer2D = new VContLayer(s);
            VFunctionCalculator.VCalculateLinearColor(PCA.getBasis(),lf,funct);
            VFunctionCalculator.VDiscretizeFunctionThreeLevels(funct,0.3f);
            PCALayer2D.function = funct;
            substituteLayer(PCALayer2D);
          break;
          case 1:
            calculateElmapDensity2D();
            funct = VFunctionCalculator.VCalculate2DGridForFunction(Elmapproj,gridResolution,gridResolution);
            ElmapLayer2D = new VContLayer(s);
            VFunctionCalculator.VCalculateGridLinearColor(elmap.elmap.grid,lf,funct);
            VFunctionCalculator.VDiscretizeFunctionThreeLevels(funct,0.3f);
            ElmapLayer2D.function = funct;
            substituteLayer(ElmapLayer2D);
          break;
       }
      }
  }
  // coloring by field classes
  int clsNum = VD.table.getFieldClassesList().indexOf(s);
  if(clsNum!=-1){
    cbSizes.setVisible(true);
    VFunction funct = new VFunction(VD.coordCount);
    VLinearFunction lf = new VLinearFunction();
    lf.setDimension(VD.coordCount);
    double sh = 0; double co[] = new double[VD.coordCount];
    int nn = 0;
    for(int i=0;i<VD.coordCount;i++){
      String fc = VD.table.fieldClasses[VD.selector.selectedColumns[i]];
      if(fc.equals(s)) {co[i] = 1; nn++;}
    }
    for(int i=0;i<co.length;i++) co[i]/=nn;
    lf.set(sh,co);
    if(cbSizes.getState()){
        VDataSet vdn = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(VD.table,-1);
        VFunctionCalculator.VCalculateLinearFunctionValues(vdn,lf,funct);
        points.calculateSizesByFunction(funct,4,12,-1,1);
    }
    else
        points.sizes = null;
    switch(visualizationMode){
      case 0:
        funct = VFunctionCalculator.VCalculate2DGridForFunction(PCAproj,gridResolution,gridResolution);
        PCALayer2D = new VContLayer(s);
        VFunctionCalculator.VCalculateLinearColor(PCA.getBasis(),lf,funct);
        VFunctionCalculator.VBinarizeFunction(funct,0.5f);
        PCALayer2D.function = funct;
        substituteLayer(PCALayer2D);
      break;
      case 1:
        calculateElmapDensity2D();
        funct = VFunctionCalculator.VCalculate2DGridForFunction(Elmapproj,gridResolution,gridResolution);
        ElmapLayer2D = new VContLayer(s);
        VFunctionCalculator.VCalculateGridLinearColor(elmap.elmap.grid,lf,funct);
        VFunctionCalculator.VBinarizeFunction(funct,0.5f);
        ElmapLayer2D.function = funct;
        substituteLayer(ElmapLayer2D);
      break;
   }
  }
  }

  void choice1_itemStateChanged(ItemEvent e){
    calculateLayer();
    updateImage();
  }

  void RDcheck_itemStateChanged(ItemEvent e) {
    calculateLayer();
    updateImage();
  }

  void NDcheck_itemStateChanged(ItemEvent e) {
    calculateLayer();
    updateImage();
  }

  void NormalProblem(){
    problemType = 0;
    VD = VDNormal;
  }

  void TransposedProblem(){
    if(VDTransposed==null){
      VDataTable VTTransposed = new VDataTable();
      int iname = VDNormal.table.fieldNumByName("NAME");
      int iclas = VDNormal.table.fieldNumByName("CLASS");
      int idesc = VDNormal.table.fieldNumByName("DESCRIPTION");
      VTTransposed.colCount = VDNormal.pointCount+3;
      VTTransposed.rowCount = VDNormal.coordCount;
      VTTransposed.fieldNames = new String[VTTransposed.colCount];
      VTTransposed.fieldTypes = new int[VTTransposed.colCount];
      VTTransposed.fieldClasses = new String[VTTransposed.colCount];
      VTTransposed.fieldDescriptions = new String[VTTransposed.colCount];
      VTTransposed.stringTable = new String[VTTransposed.rowCount][VTTransposed.colCount];
      //field names and types
      VTTransposed.fieldNames[0] = "NAME"; VTTransposed.fieldTypes[0] = VDataTable.STRING;
      VTTransposed.fieldNames[1] = "CLASS"; VTTransposed.fieldTypes[1] = VDataTable.STRING;
      VTTransposed.fieldNames[2] = "DESCRIPTION"; VTTransposed.fieldTypes[2] = VDataTable.STRING;
      for(int i=0;i<VDNormal.selector.selectedRows.length;i++){
          int k = VDNormal.selector.selectedRows[i];
          VTTransposed.fieldTypes[i+3] = VDataTable.NUMERICAL;
          VTTransposed.fieldNames[i+3] = VDNormal.table.getV(k,iname);
          VTTransposed.fieldClasses[i+3] = VDNormal.table.getV(k,iclas);
          VTTransposed.fieldDescriptions[i+3] = VDNormal.table.getV(k,idesc);
      }
      // filling values
      for(int i=0;i<VDNormal.selector.selectedColumns.length;i++){
        int k = VDNormal.selector.selectedColumns[i];
        VTTransposed.stringTable[i][0] = VDNormal.table.fieldNames[k];
        VTTransposed.stringTable[i][1] = VDNormal.table.fieldClasses[k];
        VTTransposed.stringTable[i][2] = VDNormal.table.fieldDescriptions[k];
        for(int j=0;j<VDNormal.selector.selectedRows.length;j++){
          int kc = VDNormal.selector.selectedRows[j];
          VTTransposed.stringTable[i][j+3] = VDNormal.table.getV(kc,k);
        }
      }
      // Create additional class fields
      if(VDNormal.table.fieldInfo!=null){
        int infoColcount = VDNormal.table.fieldInfo[0].length;
        for(int j=0;j<infoColcount;j++)
          VTTransposed.addNewColumn("CLASS"+(j+1),"","",VDataTable.STRING,"");

        for(int j=0;j<infoColcount;j++){
          String cln = "CLASS"+(j+1);
          int k = VTTransposed.fieldNumByName(cln);
          for(int i=0;i<VTTransposed.rowCount;i++){
            int kk = -1;
            VTTransposed.stringTable[i][k] = VDNormal.table.fieldInfo[VDNormal.selector.selectedColumns[i]][j];
          }
        }
      }
      //VDatReadWrite.saveToVDatFile(VTTransposed,"out_t.dat");
      VDTransposed = VSimpleProcedures.SimplyPreparedDataset(VTTransposed,-1);
    }
    problemType = 1;
    VD = VDTransposed;
  }

  void taskTypeChoice_itemStateChanged(ItemEvent e) {
    try{
    imag = null;
    im2d = null;
    layer = null;
    PCA = null;
    elmap = null;
    selected = new Vector();
    selectedIDs = new Vector();
    selected_desc = new Vector();
    points = null;
    selectedDescriptor = new VObjectDescriptor();
    PCADensityLayer2D = null;
    ElmapDensityLayer2D = null;
    PCADensityLayerND = null;
    ElmapDensityLayerND = null;
    PCALayer2D = null;
    ElmapLayer2D = null;
    PCAproj = null;
    Elmapproj = null;

    if(taskTypeChoice.getSelectedItem().equals("Direct problem")){
      NormalProblem();
    }else{
      TransposedProblem();
    }
    Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"CLASS",VClassifier.CHANGE_FILLCOLOR);
    fillElements();
    switch(visualizationMode){
    case 0: createPCAImage(); break;
    case 1: createElmapImage(); break;
    }
    calculateLayer();
    updateImage();
    }catch(Exception ex){
      ex.printStackTrace();
      textArea.setText(ex.toString());
    }

  }

  public VDataSet addRequiredFields(VDataSet VD){
    VDataSet resd = new VDataSet();
    VDataTable vt = VD.table;
    // if there is no NAME field, then we take the first string field for it
    if(vt.fieldNumByName("NAME")==-1){
      int sf = vt.getFirstStringField();
      vt.addNewColumn("NAME","","",vt.STRING,"noname");
      if(sf!=-1){
        for(int i=0;i<vt.rowCount;i++)
            vt.stringTable[i][vt.colCount-1] = vt.stringTable[i][sf];
      }
    }
    // if there is no CLASS ans DESCRIPTION fields then we just add them
    if(vt.fieldNumByName("CLASS")==-1){
      vt.addNewColumn("CLASS","","",vt.STRING,"");
    }
    if(vt.fieldNumByName("DESCRIPTION")==-1){
      vt.addNewColumn("DESCRIPTION","","",vt.STRING,"");
    }
    VTableSelector vs = VD.selector;
    resd = new VDataSet();
    resd.loadFromDataTable(vt,vs);
    resd.preProcess = VD.preProcess;
    resd.preProcessData();
    resd.table = vt;
    return resd;
  }

  void cbSizes_mouseClicked(MouseEvent e) {
    calculateLayer();
    updateImage();
    repaint();
  }

  void choice2_itemStateChanged(ItemEvent e) {
    String s = choice2.getSelectedItem();
    int k = VD.table.fieldNumByName(s);
    int kcl = VD.table.fieldNumByName("CLASS");
    if(k>=0){
      for(int i=0;i<VD.table.rowCount;i++)
        VD.table.stringTable[i][kcl] = VD.table.stringTable[i][k];
    }
    Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"CLASS",VClassifier.CHANGE_FILLCOLOR);
    //Date t = new Date();
    fillElements();
    //System.out.println(""+((new Date()).getTime()-t.getTime())+" ms");
    calculateLayer();
    updateImage();
    repaint();
  }

  int numberOfClasses(){
    int res = 1;
    for(int i=2;i<1000;i++){
      int k = VD.table.fieldNumByName("CLASS"+i);
      if(k==-1) break;
      else
      res = i;
    }
    return res;
  }

}