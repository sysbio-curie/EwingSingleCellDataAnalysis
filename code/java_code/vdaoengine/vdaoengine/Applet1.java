package vdaoengine;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import com.borland.jbcl.layout.*;


/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

public class Applet1 extends Applet {


  private Panel imagepanel = new Panel();
  private Button button1 = new Button();
  private TextArea textArea = new TextArea();
  private Label label1 = new Label();
  private Button button2 = new Button();


  private boolean isStandalone = false;
  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public Applet1() {
  }
  //Initialize the applet
  public void init() {
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
    imagepanel.setBackground(new Color(212, 208, 255));
    imagepanel.setBounds(new Rectangle(-1, 1, 461, 161));
    imagepanel.setLayout(null);
    button2.setLabel("Label all");
    button2.setBounds(new Rectangle(7, 29, 120, 20));
    button2.setFont(new java.awt.Font("Dialog", 1, 12));
    //System.out.println(this.getHeight());
    //System.out.println(this.getWidth());
    button1.setFont(new java.awt.Font("Dialog", 1, 12));
    button1.setLabel("RESET");
    button1.setBounds(new Rectangle(7, 7, 120, 20));
    button1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        button1_actionPerformed(e);
      }
    });
    label1.setText("closest point is");
    label1.setBounds(new Rectangle(10, 128, 120, 19));
    //this.add(imagepanel,  new XYConstraints(0, 209, 468, 152));
    textArea.setBounds(new Rectangle(136, 7, 325, 118));
    imagepanel.add(button1, null);
    imagepanel.add(textArea, null);
    imagepanel.add(label1, null);
    imagepanel.add(button2, null);
    this.add(imagepanel, null);
  }
  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
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
    //if(im!=null)
    //g2.drawImage(im,null,0,0);
    imagepanel.setBounds(0,(int)(2.8f*this.getHeight()/4),this.getWidth(), this.getHeight());
    textArea.append(this.getWidth()+" "+this.getHeight()+"\n");
  }

  void button1_actionPerformed(ActionEvent e) {
    paint(getGraphics());
  }


}