package vdaoengine.data;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.data.*;
import vdaoengine.*;
import vdaoengine.utils.*;
import java.util.*;
import java.awt.Color;

public class VClassifier {

public final static int SIMPLE_MODE = 1;
public final static int BW_WHITEBG = 2;
public final static int BW_DARKBG = 4;
public final static int CHANGE_FILLCOLOR = 8;
public final static int CHANGE_SHAPE = 16;

Vector niceDescriptors = null;
public Color[] colorSequence = null;
public int[] shapeSequence = null;

HashSet classSet;

  public VClassifier() {
  colorSequence = globalSettings.standardColors;
  shapeSequence = globalSettings.standardShapes;
  classSet = new HashSet();
  }

  public void addClass(VDataClass cl){
  classSet.add(cl);
  }

  public VDataClass getClass(int ID){
  VDataClass cl = null;
  for(Iterator i=classSet.iterator();i.hasNext();){
        cl = (VDataClass)(i.next());
        if(cl.containsID(ID)) break;
	}
  return cl;
  }

  public VDataClass getClass(String s){
  VDataClass cl = null;
  for(Iterator i=classSet.iterator();i.hasNext();){
        cl = (VDataClass)(i.next());
        if(cl.name.equals(s)) break;
	}
  return cl;
  }


  public Vector getAllClasses(int ID){
  Vector v = new Vector();
  for(Iterator i=classSet.iterator();i.hasNext();){
        VDataClass cl = (VDataClass)(i.next());
        if(cl.containsID(ID)) v.addElement(cl);
	}
  return v;
  }

  public HashSet getClassSet(){
  return classSet;
  }

  public Vector getClassVector(){
  Vector v = new Vector();
  for(Iterator i=classSet.iterator();i.hasNext();){
        VDataClass cl = (VDataClass)(i.next());
	 v.addElement(cl);
  }
  for(int i=v.size()-1;i>=0;i--)
     for(int j=0;j<i;j++){
       int i1 = ((VDataClass)(v.elementAt(j))).IDs.size();
       int i2 = ((VDataClass)(v.elementAt(j+1))).IDs.size();
       if(i1<i2){
         VDataClass cl1 = (VDataClass)(v.elementAt(j));
         VDataClass cl2 = (VDataClass)(v.elementAt(j+1));
         v.setElementAt(cl1,j+1);
         v.setElementAt(cl2,j);
         }
       }
  return v;
  }

  public void classifyByTableUniqueField(VDataTable tab, VObjectDescriptorSet dset, String fieldName, int mode){
  int fn = tab.fieldNumByName(fieldName);
  if(fn!=-1)
    classifyByTableUniqueField(tab,dset,fn,mode);
  }

  public void classifyByTableUniqueField(VDataTable tab, VObjectDescriptorSet dset, int fieldNum, int mode){
  //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(tab,"temp.dat");
  HashMap hm = new HashMap();
  for(int i=0;i<tab.rowCount;i++){
     String s = tab.getV(i,fieldNum);
     if(s==null) s = "";
     String key = new String(s);
     if(hm.containsKey(key)){
	Vector IDs = (Vector)(hm.get(key));
	IDs.addElement(new Integer(tab.getRowID(i)));
     }else{
	Vector IDs = new Vector();
	IDs.addElement(new Integer(tab.getRowID(i)));
	hm.put(key,IDs);
     }
     }
  Set keys = hm.entrySet();
  int j=0;
  prepareNiceDescriptors(keys.size(),mode);
  for(Iterator i=keys.iterator();i.hasNext();){
  Map.Entry e = (Map.Entry)(i.next());
  Vector IDs = (Vector)(e.getValue());
  String nam = new String(tab.fieldNames[fieldNum]+"=\""+(String)(e.getKey())+"\"");
  String shortnam = new String((String)(e.getKey()));
  VObjectDescriptor ds = getNiceDescriptor(j);
  //System.out.println("Nice Descriptor: "+ds.toString());
  VDataClass dc = addNewClass(IDs,nam,ds,dset);
  dc.shortname = shortnam;
  dc.fieldName = tab.fieldNames[fieldNum];
  j++;
  }

  }

  public VDataClass addNewClass(Vector IDs, String nam, VObjectDescriptor ds, VObjectDescriptorSet dset){
  VDataClass dc = new VDataClass(nam,ds);
  dc.addIDs(IDs);
  dc.updateDescriptors(dset);
  addClass(dc);
  return dc;
  }

  public void addNewClassChooseAutoColor(Vector IDs, String nam, VObjectDescriptorSet dset){
  Color newColor = dset.getAnotherColor(colorSequence);
  VObjectDescriptor ds = new VObjectDescriptor();
  ds.setFillColor(newColor);
  addNewClass(IDs,nam,ds,dset);
  }

  public void addNewClassChooseAutoShape(Vector IDs, String nam, VObjectDescriptorSet dset){
  int newShape = dset.getAnotherShape(shapeSequence);
  VObjectDescriptor ds = new VObjectDescriptor();
  ds.setShape(newShape);
  addNewClass(IDs,nam,ds,dset);
  }

  public String toString(){
  StringBuffer s = new StringBuffer();
  s.append("Number of classes: "+classSet.size());
  int j=1;
  for(Iterator i=classSet.iterator();i.hasNext();){
        VDataClass cl = (VDataClass)(i.next());
        s.append("Class "+j+": "+cl.toString()); j++;
  }
  return s.toString();
  }

  public void prepareNiceDescriptors(int num, int mode){
  niceDescriptors = new Vector();
  switch(mode){
  case SIMPLE_MODE:
  boolean twink = true;
  for(int i=0;i<num;i++){
        VObjectDescriptor ds = new VObjectDescriptor();
        ds.setSimplified(true);
        if(i<colorSequence.length) ds.setFillColor(colorSequence[i]);
        if(twink) ds.setShape(0); else ds.setShape(1);
        twink = !twink;
  	niceDescriptors.addElement(ds);
	}
  break;
  case CHANGE_FILLCOLOR:
  for(int i=0;i<num;i++){
        VObjectDescriptor ds = new VObjectDescriptor();
        if(i<colorSequence.length) ds.setFillColor(colorSequence[i]);
  	niceDescriptors.addElement(ds);
	}
  break;
  case CHANGE_SHAPE:
  for(int i=0;i<num;i++){
        VObjectDescriptor ds = new VObjectDescriptor();
        if(i<shapeSequence.length) ds.setShape(shapeSequence[i]);
  	niceDescriptors.addElement(ds);
	}
  break;
  }
  }

  public VObjectDescriptor getNiceDescriptor(int n){
  return (VObjectDescriptor)(niceDescriptors.elementAt(n));
  }

}
