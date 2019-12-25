package vdaoengine.image.im3d;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for visualization procedure
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/


import vdaoengine.data.*;
import vdaoengine.image.*;
import vdaoengine.visproc.*;
import java.util.*;
import java.lang.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class VUtils3D {

public VUtils3D(){
}

public static Appearance getAppearanceForDataPoint(VObjectDescriptor D, boolean border){
Appearance app = new Appearance();
if(D.isSimplified()){
  PointAttributes patr = new PointAttributes(D.getSize(),D.getShape()>0);
  app.setPointAttributes(patr);
  }else{
  if(!border){
  ColoringAttributes Coloring = new ColoringAttributes();
  Coloring.setColor(new Color3f(D.getFillColor()));
  app.setColoringAttributes(Coloring);
  }else{
  ColoringAttributes Coloring = new ColoringAttributes();
  Coloring.setColor(new Color3f(D.getBorderColor()));
  app.setColoringAttributes(Coloring);
  LineAttributes lineAttrib = new LineAttributes();
  lineAttrib.setLineWidth(D.getBorderThick());
  app.setLineAttributes(lineAttrib);
  }
}
return app;
}

}
