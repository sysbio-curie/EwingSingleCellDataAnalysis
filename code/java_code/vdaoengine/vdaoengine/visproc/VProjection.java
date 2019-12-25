package vdaoengine.visproc;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for continuous dimension reduction
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/


import vdaoengine.data.*;
import vdaoengine.image.*;


public interface VProjection extends VDimensionReduction {

public double[] projectionFunction(double[] vec);
public double[] projectFromInToOut(double[] vec);

public float[] projectionFunction(float[] vec);
public float[] projectFromInToOut(float[] vec);


}