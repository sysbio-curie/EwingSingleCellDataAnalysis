package vdaoengine.utils;

import java.io.*;
import java.util.*;
import com.sun.j3d.utils.applet.MainFrame;
import vdaoengine.analysis.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.special.GIFOutputStream;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.*;


public class IO {

public static void writeGIF(String s, BufferedImage im){

  try{
  Thread.sleep(100);
  GIFOutputStream G = new GIFOutputStream(new FileOutputStream(s));
  G.write(im);
  }catch(Exception e){System.out.println(e);}

}

public static String loadString(String fn){
  StringBuffer sb = new StringBuffer();
  try{
  LineNumberReader lr = new LineNumberReader(new FileReader(fn));
  String s = null;
  while((s=lr.readLine())!=null){
    sb.append(s+"\n");
  }
  lr.close();
  }catch(Exception e){
    e.printStackTrace();
  }
  return sb.toString();
}

public static void makeMatrixDisplayGIF(double matrix[][], String fn, double square_size){
  BufferedImage im = new BufferedImage((int)(square_size*matrix.length),(int)(square_size*matrix[0].length),BufferedImage.TYPE_INT_RGB);
  Graphics g2 = im.createGraphics();
  makeMatrixDisplay(g2,matrix,square_size);
  writeGIF(fn,im);
}

public static double[][] convertFloatArrayToDouble(float matrix[][]){
  double matr[][] = new double[matrix.length][matrix[0].length];
  for(int i=0;i<matrix.length;i++)
    for(int j=0;j<matrix[0].length;j++)
      matr[i][j] = (double)matrix[i][j];
  return matr;
}

public static void makeMatrixDisplay(Graphics g, double matrix[][], double square_size){
  Graphics2D g2 = (Graphics2D) g;
  double tab[][];
  Rectangle2D r[][];

  double x,y,size;
  double max;

  int i,j;

  tab=matrix;

  g2.setPaint(Color.blue);
  Rectangle2D r2 = new Rectangle2D.Double(0,0,tab.length*square_size,tab[0].length*square_size);
  g2.fill(r2);

  r = new Rectangle2D[tab[0].length][tab.length];

  y=0;
  double pc[] = new double[tab.length];
  // Pour chaque colonne de la table tab :
  for(j=0;j<tab[0].length;j++) {

          for(i=0;i<tab.length;i++) {
                  pc[i] = tab[i][j];
          }
          max = maxAbsInVector(pc);

          x = 0;
          // Pour chaque ligne de la table tab :
          for(i=0;i<tab.length;i++) {
                  size = Math.abs(tab[i][j]) * square_size / max;
                  if (tab[i][j]>0) {
                          g2.setPaint(Color.red);
                  }
                  else g2.setPaint(Color.green);
                  y = j*square_size + (square_size - size)/2;
                  x=i*square_size + (square_size - size)/2;
                  r[j][i] = new Rectangle2D.Double(x,y,size,size);
                  g2.fill(r[j][i]);
          }
  }
}

public static double maxAbsInVector(double [] vect) {
        int i;
        double max = Math.abs(vect[0]);
        for(i=1;i<vect.length;i++){
                if (Math.abs(vect[i]) > max)
                        max = Math.abs(vect[i]);
        }
        return max;
}



}
