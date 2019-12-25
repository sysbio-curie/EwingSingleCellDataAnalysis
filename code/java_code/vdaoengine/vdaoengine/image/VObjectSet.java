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

import java.util.*;
import java.lang.*;

public abstract class VObjectSet extends VDataImageElement{

HashMap tofindid;
public Vector IDs;
public Vector names;
public Vector annotations = null;
VObjectDescriptorSet descriptors = null;

public VObjectSet(String nam){
super(nam);
IDs = new Vector();
tofindid = new HashMap();
names = new Vector();
}

public void setDescriptorSet(VObjectDescriptorSet desc){
descriptors = desc;
}

public VObjectDescriptorSet getDescriptorSet(){
if(descriptors==null) {descriptors=new VObjectDescriptorSet();}
return descriptors;
}

public void annotateByFields(VDataTable vd,Vector fieldNames){
  if(annotations==null) annotations = new Vector();
  annotations.clear();
  int names[] = new int[fieldNames.size()];
  for(int i=0;i<fieldNames.size();i++){
    names[i] = vd.fieldNumByName((String)fieldNames.elementAt(i));
  }
for(int i=0;i<IDs.size();i++){
  int id = ((Integer)IDs.elementAt(i)).intValue();
  String s[] = vd.getRowByID(id);
  StringBuffer sb = new StringBuffer();
  for(int j=0;j<names.length;j++){
    sb.append(s[names[j]]+";");
  }
  sb.deleteCharAt(sb.length()-1);
  annotations.addElement(sb.toString());
}
}

public void annotateByFields(VDataTable vd,Vector fieldNames, Vector aIDS){
  int names[] = new int[fieldNames.size()];
  for(int i=0;i<fieldNames.size();i++){
    names[i] = vd.fieldNumByName((String)fieldNames.elementAt(i));
  }
for(int i=0;i<aIDS.size();i++){
  int id = ((Integer)aIDS.elementAt(i)).intValue();
  String s[] = vd.getRowByID(id);
  StringBuffer sb = new StringBuffer();
  for(int j=0;j<names.length;j++){
    sb.append(s[names[j]]+";");
  }
  sb.deleteCharAt(sb.length()-1);
  setAnnotation(id,sb.toString());
}
}


public void annotateByField(VDataTable vd,String fieldName){
Vector v = new Vector();
v.add(fieldName);
annotateByFields(vd,v);
}

public void annotateByField(VDataTable vd,String fieldName, Vector IDS){
Vector v = new Vector();
v.add(fieldName);
annotateByFields(vd,v,IDS);
}


public int getObjectNum(int ID){
  return ((Integer)tofindid.get(new Integer(ID))).intValue();
}

public void setAnnotation(int ID, String s){
  if(annotations==null){
    annotations = new Vector();
    for(int i=0;i<IDs.size();i++) annotations.add("");
  }
  int k = getObjectNum(ID);
  if(k>=0) annotations.set(k,s);
}


}