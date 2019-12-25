package vdaoengine.image.im3d;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.image.*;
import java.lang.*;
import java.awt.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;


public class VFlatPoint3D extends VFlatPoint{

OrientedShape3D shape;
OrientedShape3D border = null;
boolean withborder = false;

  public VFlatPoint3D(int type, float sc) {
  super(type);
  this.scale(sc);
  shape = new OrientedShape3D();
  shape.setAlignmentMode(shape.ROTATE_ABOUT_POINT);

  if(type>RHOMB){
  GeometryInfo ginf = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
  float coord[] = new float[coordinates.length*3];
  for (int i=0;i<coordinates.length;i++){
     coord[i*3+0] = coordinates[i][0];
     coord[i*3+1] = coordinates[i][1];
     coord[i*3+2] = coordinates[i][2];
     }
  int[] stripCount = {coordinates.length};
  ginf.setCoordinates(coord);
  ginf.setStripCounts(stripCount);
  Triangulator tr = new Triangulator();
  tr.triangulate(ginf);

  Stripifier st = new Stripifier();
  st.stripify(ginf);
  ginf.recomputeIndices();
  shape.setGeometry(ginf.getGeometryArray());
  }
  else{
    if(type<SQUARE){
    TriangleArray tr = new TriangleArray(3,GeometryArray.COORDINATES|GeometryArray.NORMALS);
    float coord[] = new float[coordinates.length*3];
    for (int i=0;i<coordinates.length;i++){
       coord[i*3+0] = coordinates[i][0];
       coord[i*3+1] = coordinates[i][1];
       coord[i*3+2] = coordinates[i][2];
     }
    tr.setCoordinates(0,coord);
    shape.setGeometry(tr);
    }else
    {
    QuadArray tr = new QuadArray(4,GeometryArray.COORDINATES|GeometryArray.NORMALS);
    float coord[] = new float[coordinates.length*3];
    for (int i=0;i<coordinates.length;i++){
       coord[i*3+0] = coordinates[i][0];
       coord[i*3+1] = coordinates[i][1];
       coord[i*3+2] = coordinates[i][2];
     }
    tr.setCoordinates(0,coord);
    shape.setGeometry(tr);
    }
  }

  }

  public void setBorder(float thick){
  withborder = true;
  float coord[] = new float[coordinates.length*3+3];
  for (int i=0;i<coordinates.length;i++){
     coord[i*3+0] = coordinates[i][0];
     coord[i*3+1] = coordinates[i][1];
     coord[i*3+2] = coordinates[i][2];
     }
  coord[coordinates.length*3] = coord[0];
  coord[coordinates.length*3+1] = coord[1];
  coord[coordinates.length*3+2] = coord[2];
  int[] stripCount = {coordinates.length+1};
  LineStripArray lineArray = new LineStripArray(coordinates.length+1, LineArray.COORDINATES, stripCount);
  lineArray.setCoordinates(0,coord);
  border = new OrientedShape3D();
  border.setAlignmentMode(shape.ROTATE_ABOUT_POINT);
  border.setGeometry(lineArray);
  }
}