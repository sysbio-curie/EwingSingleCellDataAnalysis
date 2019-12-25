package vdaoengine.visproc;

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


public interface VVisualizationProc {

public void setDataSet(VDataSet Data);
public VDataSet getDataSet();
public VDataImage getDataImage();
public VDataImage updateDataImage();

}