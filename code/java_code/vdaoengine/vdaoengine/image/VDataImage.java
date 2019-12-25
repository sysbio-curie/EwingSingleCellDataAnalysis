package vdaoengine.image;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for visualization procedure
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/

import java.awt.*;
import vdaoengine.data.*;
import vdaoengine.visproc.*;
import java.util.*;
import java.lang.*;

public class VDataImage {

public Color bkgColor = Color.white;
public int DiscreteColor = -1;
public Vector elements;
public boolean imageActual = false;
public String name;
public VClassifier classes = null;
public VDataTable table = null;

public VDataImage(){
//System.out.println("We are in VDataImage constructor");
elements = new Vector();
}

public void addElement(VDataImageElement elem){
int i= getElementByName(elem.name);
if(i!=-1) delElement(i);
elements.addElement(elem);
}

public void delElement(String Name){
int todel = getElementByName(Name);
if(todel!=-1) elements.remove(todel);
}

public void delElement(int num){
elements.remove(num);
}

public int getElementByName(String Name){
int res = -1;
for(int i=0;i<elements.size();i++){
  if(((VDataImageElement)(elements.elementAt(i))).name.equals(Name)){
    { res=i; break;}
    }
  }
return res;
}

public void provideClassInformation(VClassifier Cl){
classes = Cl;
}

public void provideTableInformation(VDataTable tab){
table = tab;
}

}
