package vdaoengine.utils;

import java.util.*;
import vdaoengine.data.*;
import vdaoengine.special.*;
import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.applet.MainFrame;
import java.awt.image.BufferedImage;


public class VPlot {

  public String title = "";
  public Vector columnsVector = new Vector();
  public String Xpoints = "";
  public String XLabels = "";
  public String YRange = "";
  public Vector suffixes = new Vector();
  public String LabelId = "";
  public String LabelName = "";
  public String BaseLines = "";
  public boolean logValues = false;
  public int plotType = 0;
  public int CONTINUOUS = 0;
  public int HISTOGRAM = 1;
  public int SCATTER = 2;
  public VPtolemyGraphViewer gv = null;

  public VDataTable table = null;

  public void createPlot(){
    gv = new VPtolemyGraphViewer();
    Font f = new Font("Times",0,20);
    //gv.plot.setTitleFont(f);
    gv.plot.setTitle(title);
    Vector series = new Vector();
    Vector xs = new Vector();

    StringTokenizer stx = new StringTokenizer(Xpoints,"/");

    int k=0;
    
    float minx[] = new float[stx.countTokens()];
    for(int i=0;i<minx.length;i++) minx[i] = Float.MAX_VALUE;
    float maxx[] = new float[stx.countTokens()];
    for(int i=0;i<maxx.length;i++) maxx[i] = -Float.MAX_VALUE;
    
    int series_count = 0;
    while(stx.hasMoreTokens()){
    k=0;
    String xvector = stx.nextToken();
    StringTokenizer st = new StringTokenizer(xvector,";");
    float x[] = new float[st.countTokens()];
    while(st.hasMoreTokens()){
      float ff = Float.parseFloat(st.nextToken());
      x[k++] = ff;
      if(ff<minx[series_count]) minx[series_count] = ff;
      if(ff>maxx[series_count]) maxx[series_count] = ff;
    }
    xs.add(x);
    series_count++;
    }

    int kk = 0;
    for(int i=0;i<table.rowCount;i++){
      int colc = 0;
      for(int j=0;j<columnsVector.size();j++){
        String cl = (String)columnsVector.elementAt(j);
        StringTokenizer st1 = new StringTokenizer(cl,";");
        float ser[] = new float[st1.countTokens()];
        k = 0;
        while(st1.hasMoreTokens()){
          String fname = st1.nextToken();
          if(table.fieldNumByName(fname)==-1){
        	  System.out.println("Field not found "+fname);
        	  for(int ii=0;ii<table.fieldNames.length;ii++) System.out.println(table.fieldNames[ii]);
          }
          if(table.fieldNumByName(fname)==-1)
        	  System.out.println("ERROR: Field "+fname+" is not found!");
          ser[k++] = Float.parseFloat(table.stringTable[i][table.fieldNumByName(fname)]);
        }
        series.add(ser);
        if(xs.size()<series.size())
        	xs.add(xs.get(colc++));
        String leg = table.stringTable[i][table.fieldNumByName(LabelId)];
        if((leg!=null)&&(!leg.equals("null")))
          leg += " / "+table.stringTable[i][table.fieldNumByName(LabelName)]+" "+(String)suffixes.get(j);
        else
          leg = table.stringTable[i][table.fieldNumByName(LabelName)]+" "+(String)suffixes.get(j);
        gv.plot.addLegend(kk,leg);
        kk++;
      }
    }


    if(!YRange.equals("")){
    	StringTokenizer styr = new StringTokenizer(YRange,";");
    	float miny = Float.parseFloat(styr.nextToken());
    	float maxy = Float.parseFloat(styr.nextToken());
    	if(!logValues){
    		miny = (float)Math.pow(10, miny);
    		maxy = (float)Math.pow(10, maxy);
    	}
    	gv.plot.setYRange(miny, maxy);
    }
    if(!XLabels.equals("")){
    	StringTokenizer stlx = new StringTokenizer(XLabels,"/");
    	k=0;
    	while(stlx.hasMoreTokens()){
    	StringTokenizer styr = new StringTokenizer(XLabels,";");
    	int ik=0;
    	while(styr.hasMoreTokens()){
    		gv.plot.addXTick(styr.nextToken(), ((float [])xs.get(k))[ik++]);
    	}
    	}
    	k++;
    }
    
    gv.ViewGraph(xs,series);
    
    int serc = 0;
    if(!BaseLines.equals("")){
    	for(int i=0;i<table.rowCount;i++){
    	int count = 0;	
    	StringTokenizer st = new StringTokenizer(BaseLines,"/");
    	while(st.hasMoreTokens()){
    		String colname = st.nextToken();
    		if(table.fieldNumByName(colname)!=-1){
    			float x[] = new float[2];
    			float y[] = new float[2];
    			x[0] = minx[count];
    			x[1] = maxx[count];
    			float ff = Float.parseFloat(table.stringTable[i][table.fieldNumByName(colname)]);
    			y[0] = ff;
    			y[1] = ff;
    			gv.addNewCurve(x, y, serc);
    		}
    	serc++;
    	count++;
    	}
    	}
    }
  }

  public void showPlot(int w, int h){
    gv.Show(w,h);
  }


}