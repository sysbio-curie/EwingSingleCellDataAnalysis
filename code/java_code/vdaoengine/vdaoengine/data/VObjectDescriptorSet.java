package vdaoengine.data;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.util.*;
import java.awt.Color;
import vdaoengine.*;

public class VObjectDescriptorSet{

HashMap Descriptors;
HashSet UniqueList;

public VObjectDescriptorSet(){
Descriptors = new HashMap();
UniqueList = new HashSet();
}

public VObjectDescriptor getDescriptor(int ID){
Integer iID = new Integer(ID);
return getDescriptor(iID);
}

public VObjectDescriptor getDescriptor(Integer ID){
if (Descriptors.containsKey(ID))
return (VObjectDescriptor)(Descriptors.get(ID));
else
return VObjectDescriptor.DefaultDescriptor();
}

public void addDescriptor(int ID, VObjectDescriptor D){
Integer iID = new Integer(ID);
Descriptors.put(iID,D);
}

public void addDescriptors(Vector vID, VObjectDescriptor D){
for(int i=0;i<vID.size();i++){
Integer iID = (Integer)vID.elementAt(i);
Descriptors.put(iID,D);
}
}

public void removeDescriptor(int ID){
Integer iID = new Integer(ID);
if (Descriptors.containsKey(iID)){
  Descriptors.remove(iID);
}
}

public HashSet getUniqueList(){
UniqueList.clear();
UniqueList.add(VObjectDescriptor.DefaultDescriptor());
for(Iterator i=Descriptors.values().iterator();i.hasNext();)
    UniqueList.add(i.next());
return UniqueList;
}

public Vector getIDsForDefault(Vector allIDs){
Vector res = new Vector();
for (Iterator i=allIDs.iterator(); i.hasNext(); ) {
Integer ID = (Integer)(i.next());
if(getDescriptor(ID).equals(VObjectDescriptor.DefaultDescriptor()))
   res.addElement(ID);
}
return res;
}

public Vector getIDsByDescriptor(VObjectDescriptor D){
Vector res = new Vector();
for(Iterator i=Descriptors.entrySet().iterator();i.hasNext();){
Map.Entry e = (Map.Entry)(i.next());
if(e.getValue().equals(D)) res.addElement(e.getKey());
}
return res;
}

public VObjectDescriptor checkIdentical(VObjectDescriptor D){
VObjectDescriptor res = D;
Collection dcol = Descriptors.values();
for(Iterator i=dcol.iterator();i.hasNext();){
  VObjectDescriptor ds = (VObjectDescriptor)(i.next());
  if(ds.equals(D)) {res=ds; break;}
  }
return res;
}

public Color getAnotherColor(Color[] seq){
Color c = globalSettings.defaultFillColor;
boolean busy[] = new boolean[seq.length];
for(int i=0;i<busy.length;i++) busy[i]=false;
getUniqueList();
for(Iterator i=UniqueList.iterator();i.hasNext();){
	VObjectDescriptor ds = (VObjectDescriptor)(i.next());
	for(int j=0;j<seq.length;j++)
		if(ds.getFillColor().equals(seq[j])) busy[j]=true;
}
for(int i=0;i<busy.length;i++) if(!busy[i]) {c=seq[i]; break;}
return c;
}

public int getAnotherShape(int[] seq){
int s = globalSettings.defaultPointShape;
boolean busy[] = new boolean[seq.length];
for(int i=0;i<busy.length;i++) busy[i]=false;
getUniqueList();
for(Iterator i=UniqueList.iterator();i.hasNext();){
	VObjectDescriptor ds = (VObjectDescriptor)(i.next());
	for(int j=0;j<seq.length;j++)
		if(ds.getShape()==seq[j]) busy[j]=true;
}
for(int i=0;i<busy.length;i++) if(!busy[i]) {s=seq[i];break;}
return s;
}


}