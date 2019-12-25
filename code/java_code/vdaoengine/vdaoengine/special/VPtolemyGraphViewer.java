package vdaoengine.special;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import javax.swing.JApplet;
import ptolemy.plot.*;
import ptolemy.gui.*;
import java.applet.Applet;
import java.util.Vector;
import java.io.FileOutputStream;
import com.sun.j3d.utils.applet.MainFrame;
import vdaoengine.analysis.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.special.GIFOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;

public class VPtolemyGraphViewer extends JApplet {

public Plot plot;
public boolean drawLegendAutomatically = false;

public VPtolemyGraphViewer(){
plot = new Plot();
plot.setBackground(Color.white);
getContentPane().add(plot);
}

public void init(){
super.init();
}

public void ViewHistogram(VHistogram hist){
float w = 0;
switch(hist.type){
case VHistogram.BASIC_HISTOGRAM:
plot.setBars(true);
w = (hist.barPosition(1)-hist.barPosition(0))*0.8f;
plot.setBars(w,w/10);
for(int i=0;i<hist.Bars.size();i++){
float f[] = (float [])(hist.Bars.elementAt(i));
for(int j=0;j<f.length;j++){
   double x = (double)(hist.barPosition(i));
   double y = (double)(f[j]);
   plot.addPoint(j,x,y,false);
   }
   }
for(int i=0;i<hist.ColNames.size();i++)
   plot.addLegend(i,(String)(hist.ColNames.elementAt(i)));
break;

case VHistogram.LABELED_HISTOGRAM:
plot.setBars(true);
w = 0.8f;
plot.setBars(w,w/10);
for(int i=0;i<hist.Bars.size();i++){
float f[] = (float [])(hist.Bars.elementAt(i));
for(int j=0;j<f.length;j++){
   double x = i;
   double y = (double)(f[j]);
   plot.addPoint(j,x,y,false);
   }
   }
for(int i=0;i<hist.Labels.size();i++)
   plot.addXTick((String)(hist.Labels.elementAt(i)),(double)(i));
for(int i=0;i<hist.ColNames.size();i++)
   plot.addLegend(i,(String)(hist.ColNames.elementAt(i)));
break;

case VHistogram.LABELED_ERR_HISTOGRAM:
plot.setMarksStyle("dots");
for(int i=0;i<hist.Bars.size();i++){
float f[] = (float [])(hist.Bars.elementAt(i));
int n = f.length/3;
for(int j=0;j<n;j++){
   double x = i;
   double y = (double)(f[3*j+2]);
   double yh = (double)(f[3*j]);
   double yl = (double)(f[3*j+1]);
   plot.addPointWithErrorBars (j,x,y,yl,yh,false);
   }
   }
for(int i=0;i<hist.Labels.size();i++)
   plot.addXTick((String)(hist.Labels.elementAt(i)),(double)(i));
if(drawLegendAutomatically)
for(int i=0;i<hist.FieldNames.size();i++)
   plot.addLegend(i,(String)(hist.FieldNames.elementAt(i)));
break;

case VHistogram.LABELED_ERR_MIN_MAX_HISTOGRAM:
plot.setMarksStyle("dots");
for(int i=0;i<hist.Bars.size();i++){
float f[] = (float [])(hist.Bars.elementAt(i));
int n = f.length/4;
for(int j=0;j<n;j++){
   double x = i;
   double y = (double)(f[4*j+2]);
   double yh = (double)(f[4*j]);
   double yl = (double)(f[4*j+1]);
   plot.addPointWithErrorBars (j,x,y,yl,yh,false);
   }
   }
for(int i=0;i<hist.Labels.size();i++)
   plot.addXTick((String)(hist.Labels.elementAt(i)),(double)(i));
if(drawLegendAutomatically)
for(int i=0;i<hist.FieldNames.size();i++)
   plot.addLegend(i,(String)(hist.FieldNames.elementAt(i)));
break;


}
}

public void ViewGraph(float x[], Vector series){
  plot.setMarksStyle("dots");
  for(int i=0;i<series.size();i++){
    float y[] = (float[])series.get(i);
    for(int j=0;j<y.length;j++)
      plot.addPoint(i,x[j],y[j],true);
  }
}

public void ViewGraph(Vector xpoints, Vector series){
	  plot.setMarksStyle("dots");
	  for(int i=0;i<series.size();i++){
		float x[] = (float[])xpoints.get(i);
	    float y[] = (float[])series.get(i);
	    for(int j=0;j<y.length;j++)
	      plot.addPoint(i,x[j],y[j],true);
	  }
}

public void addNewCurve(float x[], float y[], int k){
		//plot.setMarksStyle(markStyle);
	    for(int j=0;j<y.length;j++)
	    	if(j==0)
	    		plot.addPoint(k,x[j],y[j],false);
	    	else
	    		plot.addPoint(k,x[j],y[j],true);
}

public void ViewPointGraph(VDataTable VT,String col1,Vector f){
plot.setMarksStyle("dots");
int i1 = VT.fieldNumByName(col1);
try{
if((i1!=-1))
for(int j=0;j<f.size();j++){
String coln = (String)(f.elementAt(j));
int icoln = VT.fieldNumByName(coln);
if(icoln!=-1)
for(int i=0;i<VT.rowCount;i++){
float x = Float.parseFloat(VT.getV(i,i1));
float y = Float.parseFloat(VT.getV(i,icoln));
//System.out.println(x+"\t"+y);
plot.addPoint(j,x,y,false);
}}
}catch(Exception e){System.out.println(e);}

if(drawLegendAutomatically)
for(int j=0;j<f.size();j++){
String coln = (String)(f.elementAt(j));
plot.addLegend(j,(String)(f.elementAt(j)));
}
}

public void ViewSignalGraph(VDataTable VT,Vector f,String labelF,String altLabelF){
ViewSignalGraph(VT,f,labelF,altLabelF,0,VT.rowCount);
}

public void ViewSignalGraph(VDataTable VT,Vector f,String labelF,String altLabelF, int ist, int ien){
int i1 = VT.fieldNumByName(labelF);
int i2 = VT.fieldNumByName(altLabelF);
String wasAlt = new String();
if(i2!=-1) wasAlt = VT.getV(0,i2);
try{
for(int j=0;j<f.size();j++){
String coln = (String)(f.elementAt(j));
int icoln = VT.fieldNumByName(coln);
if(icoln!=-1)
for(int i=ist;i<ien;i++){
float x = (float)i;
float y = Float.parseFloat(VT.getV(i,icoln));
if(i2!=-1){
if(VT.getV(i,i2).equals(wasAlt)) plot.addPoint(j,x,y,true);
else{
plot.addPoint(j,x,y,false);
wasAlt = VT.getV(i,i2);
}
}
else{
plot.addPoint(j,x,y,true);}
}}

if(i1!=-1)
for(int i=0;i<VT.rowCount;i++){
String s = VT.getV(i,i1);
if(i2!=-1) s = VT.getV(i,i2)+" "+s;
plot.addXTick(s,(float)i);
}
}catch(Exception e){System.out.println(e);}

if(drawLegendAutomatically)
for(int j=0;j<f.size();j++){
String coln = (String)(f.elementAt(j));
plot.addLegend(j,(String)(f.elementAt(j)));
}
}


public void ViewDistributionGraph(VDataTable VT, String mainF, Vector f, String lablF){
int i1 = VT.fieldNumByName(mainF);
int ilabl = VT.fieldNumByName(lablF);

if(drawLegendAutomatically){
plot.addLegend(0,lablF);
}

try{
float mass[] = new float[VT.rowCount];
for(int i=0;i<VT.rowCount;i++){
mass[i]=Float.parseFloat(VT.getV(i,i1));
}
int ord[] = Algorithms.SortMass(mass);

for(int i=0;i<VT.rowCount;i++){
  float x = (float)i+1;
  float y = mass[ord[i]];
  plot.addPoint(0,x,y,true);
}

  for(int j=0;j<f.size();j++){
    String coln = (String)(f.elementAt(j));
    int icoln = VT.fieldNumByName(coln);
    if(icoln!=-1)
    for(int i=0;i<VT.rowCount;i++){
      float x = (float)i+1;
      float y = Float.parseFloat(VT.getV(ord[i],icoln));
      plot.addPoint(j+1,x,y,true);
    }
  }


if(ilabl!=-1)
for(int i=0;i<VT.rowCount;i++){
String s = VT.getV(ord[i],ilabl);
plot.addXTick(s,(float)(i+1));
}
}catch(Exception e){System.out.println(e);}


if(drawLegendAutomatically)
for(int j=0;j<f.size();j++){
String coln = (String)(f.elementAt(j));
plot.addLegend(j+1,(String)(f.elementAt(j)));
}

}

public void Show(int width, int height){
final MainFrame mf = new MainFrame(this,width,height);
//mf.addWindowListener();
mf.addWindowListener(new java.awt.event.WindowListener(){
          public void windowClosing(WindowEvent e){ }
          public void windowClosed(WindowEvent e){ }
          public void windowActivated(WindowEvent e){   }
          public void windowDeactivated(WindowEvent e){   }
          public void windowDeiconified(WindowEvent e){   }
          public void windowIconified(WindowEvent e){   }
          public void windowOpened(WindowEvent e){   }
    });
}

public void ExportToGIF(String s){
try{
Thread.sleep(1000);
GIFOutputStream G = new GIFOutputStream(new FileOutputStream(s));

BufferedImage bi = new BufferedImage(plot.getWidth(),plot.getHeight(),BufferedImage.TYPE_INT_RGB);
Graphics g = bi.getGraphics();
plot.paint(g);

G.write(bi);
}catch(Exception e){System.out.println(e);}
}

public void ExportToEPS(String s){
try{
Thread.sleep(1000);
FileOutputStream out = new FileOutputStream(s);
plot.export(out);
out.close();
}catch(Exception e){System.out.println(e);}
}

}
