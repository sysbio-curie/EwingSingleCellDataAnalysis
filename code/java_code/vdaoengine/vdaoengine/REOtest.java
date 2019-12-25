package vdaoengine;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import com.sun.image.codec.jpeg.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.geom.GeneralPath;
import java.io.*;
import javax.swing.*;


public class REOtest extends JApplet{

  public void init() {
  }

  public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      Dimension d = getSize();
      g2.setBackground(getBackground());
      g2.clearRect(0, 0, d.width, d.height);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                          RenderingHints.VALUE_RENDER_QUALITY);
      int w = d.width;
      int hh = d.height;
      //BufferedImage bi = new BufferedImage(w, hh, BufferedImage.TYPE_INT_RGB);
      BufferedImage bi = (BufferedImage) createImage(w,hh);
      Graphics2D big = bi.createGraphics();

      big.setBackground(Color.white);
      big.clearRect(0,0,w,hh);
      big.setColor(Color.green.darker());

      big.drawOval(50,50,10,10);
      g2.drawImage(bi, 0, 0, this);

  }


  public static void main(String[] args) {
    int w = 500;
    int hh = 500;
    BufferedImage bi = new BufferedImage(w, hh, BufferedImage.TYPE_INT_RGB);
    Graphics2D big = bi.createGraphics();

    big.setBackground(Color.white);
    big.clearRect(0,0,w,hh);
    big.setColor(Color.green.darker());

    big.drawOval(50,50,10,10);

    /*try{
    File file = new File("test.jpg");
    FileOutputStream out = new FileOutputStream(file);

    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
    param.setQuality(1f, false);
    encoder.setJPEGEncodeParam(param);
    encoder.encode(bi);
    }catch(Exception e){
      e.printStackTrace();
    }*/

    JFrame f = new JFrame("Java 2D(TM) Demo - JPEGFlip");
    REOtest app = new REOtest();
    f.getContentPane().add("Center", app);
    f.pack();
    f.setSize(new Dimension(400,300));
    f.show();
  }


}