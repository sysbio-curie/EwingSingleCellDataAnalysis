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
import java.lang.*;
import java.util.*;

public class VFlatPointSet extends VObjectSet{

public Vector coords;

public VFlatPointSet(String nam){
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


}