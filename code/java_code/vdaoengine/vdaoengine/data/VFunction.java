package vdaoengine.data;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.util.*;

public class VFunction {

  public int dimension = 3;
  public Vector points = null;
  public Vector pointIDs = null;
  public Vector values = null;
  public Vector triangles = null;

  public double smax = Double.MIN_VALUE;
  public double smin = Double.MAX_VALUE;

  public VFunction(int dim) {
    dimension = dim;
    points = new Vector();
    values = new Vector();
    triangles= new Vector();
  }

}