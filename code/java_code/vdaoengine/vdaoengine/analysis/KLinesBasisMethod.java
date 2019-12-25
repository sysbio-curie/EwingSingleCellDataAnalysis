package vdaoengine.analysis;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.visproc.*;


public class KLinesBasisMethod extends VLinearProjectionMethod {

public KLinesBasisMethod(){
//System.out.println("We are in the PCAMethod constructor");
}

public void recalculateBasis(VDataSet Data, int nvec){
linBasis = new VLinearBasis(Data.coordCount,nvec);
double Vectors[][] = new double[nvec][Data.coordCount];
double Shift[] = new double[Data.coordCount];
computeKLines(nvec,Data.massif,Vectors,Shift);
linBasis.set(Shift,Vectors,true);
}

public void computeKLines(int nvec, float[][] Dat, double[][] Vectors, double[] Shift){
	
}


}