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
import java.awt.*;
import java.awt.Color;
import vdaoengine.*;

public class VObjectDescriptor {

Color fillColor;
float size;
int shape;
Color borderColor;
int fillType;
boolean withBorder = false;
float borderThick = 0.5f;
boolean simplified = true;
String annot = null;
Font annotFont = new Font("Times New Roman",0,12);
Color annotColor = Color.black;
public boolean visible = true;
static int curn = 0;
public int intId = -1;

public boolean with_fillColor = false;
public boolean with_size = false;
public boolean with_shape = false;
public boolean with_borderColor = false;
public boolean with_fillType = false;
public boolean with_withBorder = false;
public boolean with_borderThick = false;
public boolean with_simplified = false;

static VObjectDescriptor defaultDesc;


public VObjectDescriptor(){
//Random r = new Random();
//intId = r.nextInt();
intId = curn;
curn++;
}

public Color getFillColor(){
return (with_fillColor)?fillColor:globalSettings.defaultFillColor;
}

public float getSize(){
float s = 0;
s = (with_size)?size:globalSettings.defaultPointSize;
return s;
}

public int getShape(){
int s = 0;
s =(with_shape)?shape:globalSettings.defaultPointShape;
return s;
}

public Color getBorderColor(){
return (with_borderColor)?borderColor:globalSettings.defaultPointBorderColor;
}

public boolean isWithBorder(){
boolean c = false;
c = (with_withBorder)?withBorder:globalSettings.defaultPointBorder;
return c;
}

public float getBorderThick(){
float s = 0;
s = (with_borderThick)?borderThick:globalSettings.defaultPointBorderThick;
return s;
}

public boolean isSimplified(){
boolean c = false;
c = (with_simplified)?simplified:globalSettings.defaultPointSimplified;
return c;
}

public void setFillColor(Color c){
with_fillColor=true;
fillColor = c;
}

public void setSize(float s){
with_size = true;
size = s;
}

public void setShape(int s){
with_shape = true;
shape = s;
}

public void setBorderColor(Color c){
with_borderColor = true;
borderColor = c;
}

public void setWithBorder(boolean b){
with_withBorder = true;
withBorder = b;
}

public void setBorderThick(float t){
with_borderThick = true;
borderThick = t;
}

public void setSimplified(boolean b){
with_simplified = true;
simplified = b;
}


public static VObjectDescriptor DefaultDescriptor(){
if(defaultDesc==null){
defaultDesc = new VObjectDescriptor();
/*defaultDesc.fillColor = globalSettings.defaultFillColor;
defaultDesc.size = globalSettings.defaultPointSize;
defaultDesc.shape = globalSettings.defaultPointShape;
defaultDesc.borderColor = globalSettings.defaultPointBorderColor;
defaultDesc.fillType = globalSettings.defaultFillType;
defaultDesc.withBorder = globalSettings.defaultPointBorder;
defaultDesc.borderThick = globalSettings.defaultPointBorderThick;
defaultDesc.simplified = defaultDesc.defaultPointSimplified;*/}
return defaultDesc;
}

public boolean equals(Object d){
boolean res = false;
if(d instanceof VObjectDescriptor)
{
VObjectDescriptor dd = (VObjectDescriptor)d;
res = true;
Color c = dd.getFillColor();
if(!dd.getFillColor().equals(getFillColor())) res=false;
if(!dd.getBorderColor().equals(getBorderColor())) res=false;
if(getSize()!=dd.getSize()) res=false;
if(getShape()!=dd.getShape()) res=false;
//if(fillType!=dd.getFillType()) res=false;
if(getBorderThick()!=dd.getBorderThick()) res=false;
if(isWithBorder()!=dd.isWithBorder()) res=false;
if(isSimplified()!=dd.isSimplified()) res=false;
if((annot!=null)&&(!annot.equals(dd.annot))) res = false;
}
return res;
}

public Object clone(){
VObjectDescriptor dd = new VObjectDescriptor();
dd.setFillColor(fillColor);
dd.setBorderColor(borderColor);
dd.setSize(size);
dd.setShape(shape);
dd.fillType=fillType;
dd.setBorderThick(borderThick);
dd.withBorder=withBorder;
dd.simplified=simplified;
dd.setAnnotation(annot);
dd.setAnnotationColor(annotColor);
dd.setAnnotationFont(annotFont);
return dd;
}

public String toString(){
return new String(intId+" "+annot+" : fillColor = "+getFillColor().toString().substring(14)+"Size = "+getSize()+" shape = "+getShape());
}

public void setAnnotation(String s){
  annot = s;
}

public String getAnnotation(){
  return annot;
}

public void setAnnotationFont(Font i){
  annotFont = i;
}

public Font getAnnotationFont(){
  return annotFont;
}

public void setAnnotationColor(Color c){
  annotColor = c;
}

public Color getAnnotationColor(){
  return annotColor;
}


}