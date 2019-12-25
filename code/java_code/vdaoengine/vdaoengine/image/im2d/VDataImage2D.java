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
import vdaoengine.image.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import java.util.*;
import java.awt.*;

public class VDataImage2D extends VDataImage{

  public float ax=1f,ay=1f,bx=0f,by=0f;
  BufferedImage im = null;
  public boolean doNotDrawPoints = false;

  public VDataImage2D() {
  }

  public VDataImage2D(VDataImage imag){
  this.elements = imag.elements;
  DiscreteColor = imag.DiscreteColor;
  }

  public BufferedImage createBufferedImage(int wid, int hei){

    if(Math.abs(ax-1f)<1e-8) calculateScale(wid,hei);

    im = new BufferedImage(wid,hei,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = im.createGraphics();
    g.setBackground(bkgColor);
    g.clearRect(0,0,im.getWidth(),im.getHeight());
    g.setColor(Color.black);
    //g.drawOval(100,100,15,15);
    // Layers always first
    //System.out.println("Drawing "+elements.size()+" elements:");
    for(int i=0;i<elements.size();i++){
      VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
      if(vie instanceof VContLayer){
        VContLayer lay = (VContLayer)vie;
        //System.out.println("Layer "+lay.name);
        drawLayer(g,im,lay);
      }
    }
    for(int i=0;i<elements.size();i++){
      VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
      if(vie instanceof VDataPointSet){
        VDataPointSet ps = (VDataPointSet)vie;
        //System.out.println("PointSet "+ps.name);
        if(!doNotDrawPoints)
            drawPointSet(g,im,ps);
      }
    }
    return im;
  }

  public void calculateScale(int wid, int hei){

    float minx = 1e10f;  float miny = 1e10f;
    float maxx = -1e10f;  float maxy = -1e10f;

    for(int i=0;i<elements.size();i++){
      VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
      if(vie instanceof VContLayer){
        VContLayer lay = (VContLayer)vie;
        for(int k=0;i<lay.function.points.size();i++){
          float v[] = (float [])lay.function.points.elementAt(k);
          if(v[0]<minx) minx=v[0];
          if(v[1]<miny) miny=v[1];
          if(v[0]>maxx) maxx=v[0];
          if(v[1]>maxy) maxy=v[1];
        }
      }
      if(vie instanceof VDataPointSet){
        VDataPointSet ps = (VDataPointSet)vie;
        // first extract the coordinates
        for(int k=0;k<ps.coords.size();k++){
          float v[] = (float [])ps.coords.elementAt(k);
          if(v[0]<minx) minx=v[0];
          if(v[1]<miny) miny=v[1];
          if(v[0]>maxx) maxx=v[0];
          if(v[1]>maxy) maxy=v[1];
        }
      }
    }

    float diapx = maxx-minx;
    float diapy = maxy-miny;
    ax = wid/(diapx*1f); bx = -wid*minx/(maxx-minx);
    ay = hei/(diapy*1f); by = -hei*miny/(maxy-miny);
    ax = ax*0.9f;
    ay = ay*0.9f;

  }

  public void drawLayer(Graphics2D g,BufferedImage im, VContLayer layer){
    /*for(int i=0;i<layer.function.points.size();i++){
      float xx[] = (float[])layer.function.points.elementAt(i);
      float v = ((Float)layer.function.values.elementAt(i)).floatValue();
      float xr = xx[0];
      float yr = xx[1];
      int x = (int)(ax*xr+bx);
      int y = (int)(ay*yr+by);
      g.setColor(new Color(1f-v,1f-v,1f-v));
      g.drawRect(x,y,10,10);
    }*/
    for(int i=0;i<layer.function.triangles.size();i++){
    //for(int i=0;i<4;i++){
      int tr[] = (int[])layer.function.triangles.elementAt(i);
      float x1[] = (float[])layer.function.points.elementAt(tr[0]);
      float x2[] = (float[])layer.function.points.elementAt(tr[1]);
      float x3[] = (float[])layer.function.points.elementAt(tr[2]);
      float v1 = ((Float)layer.function.values.elementAt(tr[0])).floatValue();
      float v2 = ((Float)layer.function.values.elementAt(tr[1])).floatValue();
      float v3 = ((Float)layer.function.values.elementAt(tr[2])).floatValue();
      float v = (v1+v2+v3)/3f;
      if(DiscreteColor>0){
        v = (1f*(int)(v*DiscreteColor)/DiscreteColor);
      }
      Polygon triang = new Polygon();
      triang.addPoint((int)(ax*x1[0]+bx),(int)(ay*x1[1]+by));
      triang.addPoint((int)(ax*x2[0]+bx),(int)(ay*x2[1]+by));
      triang.addPoint((int)(ax*x3[0]+bx),(int)(ay*x3[1]+by));
      if(v<0) v = 0;
      if(v>1) v = 1;
      g.setColor(new Color(1f-v,1f-v,1f-v));
      g.draw(triang);
      //GradientPaint p = new GradientPaint((float)triang.xpoints[0],(float)triang.ypoints[0],
      //    new Color(1f-v1,1f-v1,1f-v1),(float)triang.xpoints[1],(float)triang.ypoints[1],
      //    new Color(1f-v2,1f-v2,1f-v2));
      /*float c = v1+(v2-v1)*0.5f+(v3-v1)*0.5f;
      float xx1 = x1[0]+(x2[0]-x1[0])*0.5f+(x3[0]-x1[0])*0.5f;
      float yy1 = x1[1]+(x2[1]-x1[1])*0.5f+(x3[1]-x1[1])*0.5f;
      xx1 = ax*xx1+bx;
      yy1 = ax*yy1+by;
      GradientPaint p = new GradientPaint((float)triang.xpoints[0],(float)triang.ypoints[0],
          new Color(1f-v1,1f-v1,1f-v1),xx1,yy1,new Color(1f-c,1f-c,1f-c));
      g.setPaint(p);*/
      g.fill(triang);
      /*g.setColor(new Color(1f-v1,1f-v1,1f-v1));
      g.drawOval((int)(ax*x1[0]+bx),(int)(ay*x1[1]+by),5,5);
      g.setColor(new Color(1f-v2,1f-v2,1f-v2));
      g.drawOval((int)(ax*x2[0]+bx),(int)(ay*x2[1]+by),5,5);
      g.setColor(new Color(1f-v3,1f-v3,1f-v3));
      g.drawOval((int)(ax*x3[0]+bx),(int)(ay*x3[1]+by),5,5);*/
    }
  }

  public void drawPointSet(Graphics2D g,BufferedImage im, VDataPointSet vs){

    VObjectDescriptorSet dss = vs.getDescriptorSet();
    HashSet uniqSet = dss.getUniqueList();
    // then draw
    Vector points = new Vector();
    for(Iterator itds=uniqSet.iterator();itds.hasNext();){
            VObjectDescriptor ds = (VObjectDescriptor)(itds.next());
            //System.out.println("In DataImage: "+ds+" "+ds.visible); System.out.flush();
            if(!ds.visible) continue;
            Vector ids = null;
            if(ds.equals(VObjectDescriptor.DefaultDescriptor()))
                    ids = dss.getIDsForDefault(vs.IDs);
              else
                    ids = dss.getIDsByDescriptor(ds);
            //System.out.println("Ids size:"+ids.size());
            if(ids.size()>0){
              for (int i = 0; i < ids.size(); i++) {
                      int ID = ((Integer)(ids.elementAt(i))).intValue();
                      float trv[] = vs.getPointPos(ID);
                      ePoint ep = new ePoint();
                      ep.ds = ds;
                      ep.x = (int)(ax*trv[0]+bx);
                      ep.y = (int)(ay*trv[1]+by);
                      ep.depth = trv[2];
                      if(vs.sizes!=null)
                        ep.size = ((Integer)vs.sizes.get(new Integer(ID))).intValue();
                      if(vs.annotations!=null){
                        int num = vs.getObjectNum(ID);
                        ep.annotation = (String)vs.annotations.elementAt(num);
                      }
                      //drawPoint(g,ep.ds,ep.x,ep.y,ep.annotation);
                      points.add(ep);
              }//for
            }
    }
  float depth[] = new float[points.size()];
  for(int i=0;i<points.size();i++){
    ePoint p = (ePoint)points.elementAt(i);
    depth[i] = p.depth;
  }
  int ord[] = Algorithms.SortMass(depth);
  for(int i=0;i<points.size();i++){
    ePoint p = (ePoint)points.elementAt(ord[i]);
    drawPoint(g,p.ds,p.x,p.y,p.annotation,p.size);
  }

  }

  private void drawPoint(Graphics2D g,VObjectDescriptor ds,int x, int y, String annotation, int optSize){
    Polygon point = new Polygon();
VFlatPoint fp = new VFlatPoint(ds.getShape());
fp.annotation = annotation;
if(optSize==-1)
 fp.scale(ds.getSize());
else
 fp.scale(optSize);
for(int k=0;k<fp.coordinates.length;k++){
  int x1 = (int)fp.coordinates[k][0];
  int y1 = (int)fp.coordinates[k][1];
  point.addPoint(x+x1,y+y1);
}
g.setColor(ds.getFillColor());
g.draw(point);
g.setColor(ds.getFillColor());
g.fill(point);
if(ds.isWithBorder()){
  g.setColor(ds.getBorderColor());
  g.draw(point);
}
if(ds.getAnnotation()!=null){
  g.setFont(ds.getAnnotationFont());
  g.setColor(ds.getAnnotationColor());
  g.drawString(ds.getAnnotation(),x,y);
}else
if((fp.annotation!=null)&&(!fp.annotation.equals(""))){
  g.setFont(ds.getAnnotationFont());
  g.setColor(ds.getAnnotationColor());
  g.drawString(fp.annotation,x,y);
  if(ds.getShape()==6){
    g.setColor(ds.getAnnotationColor());
  }
                      }
  }

  public int findClosestPoint(int x, int y){
    int ID = -1;
    float rmin = 1e10f;
    for(int i=0;i<elements.size();i++){
     VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
     if(vie instanceof VDataPointSet){
       VDataPointSet ps = (VDataPointSet)vie;
       for(int k=0;k<ps.coords.size();k++){
         float v[] = (float [])ps.coords.elementAt(k);
         int xx = (int)(ax*v[0]+bx);
         int yy = (int)(ay*v[1]+by);
         float r = (x-xx)*(x-xx)+(y-yy)*(y-yy);
         if(r<rmin){
           rmin = r;
           ID = ((Integer)ps.IDs.elementAt(k)).intValue();
         }
       }
     }
   }
   return ID;
  }

  public Vector findAllClosestPoints(int x, int y){
    int ID = -1;
    Vector ids = new Vector();
    float rmin = 1e10f;
    for(int i=0;i<elements.size();i++){
     VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
     if(vie instanceof VDataPointSet){
       VDataPointSet ps = (VDataPointSet)vie;
       for(int k=0;k<ps.coords.size();k++){
         float v[] = (float [])ps.coords.elementAt(k);
         int xx = (int)(ax*v[0]+bx);
         int yy = (int)(ay*v[1]+by);
         float r = (x-xx)*(x-xx)+(y-yy)*(y-yy);
         if(r<rmin){
           rmin = r;
           ID = ((Integer)ps.IDs.elementAt(k)).intValue();
         }
       }
     }
   }
   for(int i=0;i<elements.size();i++){
    VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
    if(vie instanceof VDataPointSet){
      VDataPointSet ps = (VDataPointSet)vie;
      for(int k=0;k<ps.coords.size();k++){
        float v[] = (float [])ps.coords.elementAt(k);
        int xx = (int)(ax*v[0]+bx);
        int yy = (int)(ay*v[1]+by);
        float r = (x-xx)*(x-xx)+(y-yy)*(y-yy);
        if(Math.abs(r-rmin)<1e-8f){
          ID = ((Integer)ps.IDs.elementAt(k)).intValue();
          ids.add(new Integer(ID));
        }
      }
    }
   }
   return ids;
  }


  public BufferedImage getImage(){
    return im;
  }

  private class ePoint{
    VObjectDescriptor ds = null;
    int x = 0;
    int y = 0;
    float depth = 0f;
    int size = -1;
    String annotation = "";
  }

}