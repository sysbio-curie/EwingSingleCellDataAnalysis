package vdaoengine.image;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Dimension;
import vdaoengine.special.GIFOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class VGraphicUtils {

  public VGraphicUtils() {
  }

public static void ExportToGIF(Component C,String s){
try{
Thread.sleep(10);
FileOutputStream out = new FileOutputStream(s);
GIFOutputStream G = new GIFOutputStream(out);

BufferedImage bi = new BufferedImage(C.getWidth(),C.getHeight(),BufferedImage.TYPE_INT_RGB);
Graphics g = bi.createGraphics();
C.paint(g);

G.write(bi);
out.close();
}catch(Exception e){System.out.println(e);}
}

public static void ExportToGIF(Image I,String s){
try{
Thread.sleep(10);
FileOutputStream out = new FileOutputStream(s);
GIFOutputStream G = new GIFOutputStream(out);

//Graphics g = bi.getGraphics();
//C.paint(g);

G.write(I);
out.close();
}catch(Exception e){System.out.println(e);}
}


}