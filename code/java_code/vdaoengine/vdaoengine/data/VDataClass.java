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
import vdaoengine.utils.*;
import java.util.*;

public class VDataClass {

public String name;
public String shortname;
public String fieldName;
public String description;
HashSet IDs;
public VObjectDescriptor descriptor;

  public VDataClass(String nam, VObjectDescriptor D) {
  name = new String(nam);
  description = new String("");
  descriptor = D;
  IDs = new HashSet();
  }

  public void addID(int ID){
  IDs.add(new Integer(ID));
  }

  public void addIDs(Vector vIDs){
  for(int i=0;i<vIDs.size();i++) IDs.add(vIDs.elementAt(i));
  }

  public void removeID(int ID){
  IDs.remove(new Integer(ID));
  }

  public boolean containsID(int ID){
  return IDs.contains(new Integer(ID));
  }

  public HashSet getIDset(){
  return IDs;
  }

  public void updateDescriptors(VObjectDescriptorSet dset){
  for(Iterator i=IDs.iterator();i.hasNext();){
	Integer ID = (Integer)(i.next());
	//VObjectDescriptor ds = (VObjectDescriptor)dset.getDescriptor(ID).clone();
        VObjectDescriptor ds = descriptor;
	/*if(descriptor.with_fillColor) ds.setFillColor(descriptor.getFillColor());
	if(descriptor.with_borderColor) ds.setBorderColor(descriptor.getBorderColor());
	if(descriptor.with_borderThick) ds.setBorderThick(descriptor.getBorderThick());
	if(descriptor.with_size) ds.setSize(descriptor.getSize());
	if(descriptor.with_shape) ds.setShape(descriptor.getShape());
	if(descriptor.with_withBorder) ds.setWithBorder(descriptor.isWithBorder());
	if(descriptor.with_simplified) ds.setSimplified(descriptor.isSimplified());*/
        //System.out.println(ds);
        ds = dset.checkIdentical(ds);
        //System.out.println(ds);
	dset.addDescriptor(ID.intValue(),ds);
	}
  }

  public String toString(){
  StringBuffer res = new StringBuffer();
  res.append(name+": ");
  res.append(IDs.size());
  res.append(" elements");
  return res.toString();
  }

}
