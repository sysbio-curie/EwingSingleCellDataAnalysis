package vdaoengine.visproc;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for dimension reduction procedures
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/


import vdaoengine.data.*;
import vdaoengine.image.*;


public interface VDimensionReduction extends VVisualizationProc {

public float[][] get3DFMassif();

}