package vdaoengine.visproc;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for dimension reduction by linear projection
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/


import vdaoengine.data.*;
import vdaoengine.image.*;
import vdaoengine.utils.*;


public interface VLinearProjection extends VProjection {

public void setBasis(VLinearBasis Basis);
public VLinearBasis getBasis();
public double[] calcDispersions();

}