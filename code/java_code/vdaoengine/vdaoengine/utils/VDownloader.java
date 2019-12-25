package vdaoengine.utils;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.net.*;
import java.io.*;

public class VDownloader {

public URL url;

  public VDownloader() {
  }
  
  public static void downloadToFile(String link, String fileName){
	    try{
	    java.io.FileOutputStream fos = new java.io.FileOutputStream(fileName);
	    java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);	    
	    URL yahoo = new URL(link);
	    URLConnection yc = null;
	    DataInputStream in = null;
	    yc = yahoo.openConnection();
	    in = new DataInputStream(yc.getInputStream());
	    int i;
	    char c;
	    if(in!=null){
	    while ((i = in.read()) >= 0) {
	        c = (char)i;
	        bout.write(c);
	        }
	    in.close(); }
	    bout.flush();
	    bout.close();
	    }
	    catch(Exception e){ System.out.println(e.getMessage()); }
  }

  public static String downloadURL(String link){
    StringBuffer sb = new StringBuffer();
    try{
    URL yahoo = new URL(link);
    URLConnection yc = null;
    DataInputStream in = null;
    yc = yahoo.openConnection();
    in = new DataInputStream(yc.getInputStream());
    int i;
    char c;
    if(in!=null){
    while ((i = in.read()) >= 0) {
        c = (char)i;
        sb.append(c);
        }
    in.close(); }
    }
    catch(Exception e){ sb.append(e.getMessage()); }

    return sb.toString();
  }

}