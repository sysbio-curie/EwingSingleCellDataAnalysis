package vdaoengine.image.im3d;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.image.*;

import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;

public class VSphere3D extends VGeometricObject{

float radius = 0.1f;
float position[] = new float[3];

  public VSphere3D() {
  }

  public void setRadius(float r){
    radius = r;
  }
  public float getRadius(){
    return radius;
  }
  public float[] getPosition(){
    return position;
  }
  public void setPosition(float f[]){
    position = f;
  }
  public Shape3D getShape(){
    return null;
  }


}