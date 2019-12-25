package vdaoengine.image.im2d;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.awt.image.BufferedImage;
import com.sun.image.codec.jpeg.*;
import java.io.*;

public class Utils2d {

public static void createJPGFile(BufferedImage im, String fileName, float quality){
  try{
    /*File file = new File(fileName);
    FileOutputStream out = new FileOutputStream(file);
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(im);
    param.setQuality(quality, true);
    encoder.setJPEGEncodeParam(param);
    encoder.encode(im);*/
  }catch(Exception e){
    e.printStackTrace();
  }
}

}