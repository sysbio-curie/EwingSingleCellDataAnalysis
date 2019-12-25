package vdaoengine.image;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for visualization procedure
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import java.lang.*;
import java.util.*;

public class VDataPointSet extends VObjectSet{

public Vector coords;
public HashMap sizes = null;

public VDataPointSet(String nam){
super(nam);
coords = new Vector();
}

public void addPoint(int ID, float x, float y, float z){
float v[] = new float[3];
v[0] = x;
v[1] = y;
v[2] = z;
coords.addElement(v);
tofindid.put(new Integer(ID),new Integer(IDs.size()));
IDs.addElement(new Integer(ID));
}

public void addPoint(int ID, double x, double y, double z){
float v[] = new float[3];
v[0] = (float)x;
v[1] = (float)y;
v[2] = (float)z;
coords.addElement(v);
tofindid.put(new Integer(ID),new Integer(IDs.size()));
IDs.addElement(new Integer(ID));
}

public void addPoint(int ID, float[] v){
addPoint(ID,v[0],v[1],v[2]);
}

public void addPoint(int ID, double[] v){
//System.out.println("\n we are in AddPoint");
addPoint(ID,v[0],v[1],v[2]);
}


public float[] getPointPos(int ID){
int i=0;
try{
Integer I = (Integer)(tofindid.get(new Integer(ID)));
i = I.intValue();}
catch(Exception e){i = -1;}
float m[] = new float[3];
if(i!=-1) m=(float[])coords.elementAt(i);
return m;
}

public void calculateSizesByFieldValue(VDataSet vd, String field, int mins, int maxs, float minv, float maxv){
  sizes = new HashMap();
  int fieldnum = vd.table.fieldNumByName(field);
  int fieldid = -1;
  for(int i=0;i<vd.selector.selectedColumns.length;i++)
    if(vd.selector.selectedColumns[i]==fieldnum)
      fieldid=i;
  if(fieldid!=-1){
  if((minv==0f)&&(maxv==0f)){
    VStatistics vs = vd.simpleStatistics;
    if(vs==null){
       vs = vd.calcStatistics();
       minv = vs.getMin(fieldid);
       maxv = vs.getMax(fieldid);
    }
  }else{
    float f[] = new float[vd.coordCount];
    f[fieldid] = minv; vd.processIntoSpace(f); minv = f[fieldid];
    f[fieldid] = maxv; vd.processIntoSpace(f); maxv = f[fieldid];
  }
  for(int i=0;i<IDs.size();i++){
    int id = ((Integer)IDs.elementAt(i)).intValue();
    int k=-1;
    for(int j=0;j<vd.pointCount;j++){
      int kk = vd.getVectorID(j);
      if(kk==id) k = j;
    }
    if(k!=-1){
      float val = vd.getVector(k)[fieldid];
      val = (val-minv)/(maxv-minv);
      int size = (int)(mins+(maxs-mins)*val);
      sizes.put(IDs.elementAt(i),new Integer(size));
    }
  }
  }
}

public void calculateSizesByFunction(VFunction func, int mins, int maxs, float minv, float maxv){
  sizes = new HashMap();
  if((minv==0f)&&(maxv==0f)){
       minv = 1e10f; maxv = -1e10f;
       for(int i=0;i<func.values.size();i++){
         float val = ((Float)func.values.elementAt(i)).floatValue();
         if(val<minv) minv = val;
         if(val>maxv) maxv = val;
       }
  }
  for(int i=0;i<IDs.size();i++){
    int id = ((Integer)IDs.elementAt(i)).intValue();
    int k=-1;
    for(int j=0;j<func.values.size();j++){
      int kk = ((Integer)func.pointIDs.elementAt(j)).intValue();
      if(kk==id) k = j;
    }
    if(k!=-1){
      float val = ((Float)func.values.elementAt(k)).floatValue();
      val = (val-minv)/(maxv-minv);
      int size = (int)(mins+(maxs-mins)*val);
      sizes.put(IDs.elementAt(i),new Integer(size));
    }
  }
}


}