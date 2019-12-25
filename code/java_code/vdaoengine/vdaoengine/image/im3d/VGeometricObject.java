package vdaoengine.image.im3d;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.util.*;
import java.awt.Color;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;


public abstract class VGeometricObject {

Color color = Color.red;
String ID = "";
String description = "";

  public VGeometricObject() {
  }
  public void setColor(Color c){
    color = c;
  }
  public Color getColor(){
    return color;
  }
  public void setID(String id){
    ID = id;
  }
  public String getID(){
    return ID;
  }
  public void setDescription(String d){
    description = d;
  }
  public String getDescription(){
    return description;
  }
  public abstract Shape3D getShape();

}