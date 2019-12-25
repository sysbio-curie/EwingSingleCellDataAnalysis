package vdaoengine.image;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:  Here is description of flat points contours of different types
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

public class VFlatPoint {

public float coordinates[][];

public final static int TRIANGLE_UP = 0;
public final static int TRIANGLE_DOWN = 1;
public final static int TRIANGLE_LEFT = 2;
public final static int TRIANGLE_RIGHT = 3;
public final static int SQUARE = 4;
public final static int RHOMB = 5;
public final static int PENTAGON = 6;
public final static int HEXAGON = 7;
public final static int CIRCLE = 8;
public final static int CROSS = 9;
public final static int CROSS_ROTATED = 10;
public String annotation = null;

  public VFlatPoint(int type) {
  switch(type){
  case TRIANGLE_UP:
  coordinates = InCircle(0,3);
  break;
  case TRIANGLE_DOWN:
  coordinates = InCircle(Math.PI,3);
  break;
  case TRIANGLE_LEFT:
  coordinates = InCircle(Math.PI/2f,3);
  break;
  case TRIANGLE_RIGHT:
  coordinates = InCircle(-Math.PI/2f,3);
  break;
  case SQUARE:
  coordinates = InCircle(Math.PI/4,4);
  break;
  case RHOMB:
  coordinates = InCircle(0,4);
  break;
  case PENTAGON:
  coordinates = InCircle(0,5);
  break;
  case HEXAGON:
  coordinates = InCircle(0,6);
  break;
  case CIRCLE:
  coordinates = InCircle(0,12);
  break;
  case CROSS:
  break;
  case CROSS_ROTATED:
  break;
  }
  }

  public float[][] InCircle(double initialAngle, int num){
  float res[][] = new float[num][3];
  for(int i=0;i<num;i++){
    double phi = 2*Math.PI*(double)(i)/num;
    res[i][0]=(float)Math.sin(initialAngle-phi);
    res[i][1]=(float)Math.cos(initialAngle-phi);
    res[i][2]=0;
    }
  return res;
  }

  public void scale(float sc){
  for(int i=0; i<coordinates.length;i++){
    coordinates[i][0]*=sc;
    coordinates[i][1]*=sc;
    }
  }

}