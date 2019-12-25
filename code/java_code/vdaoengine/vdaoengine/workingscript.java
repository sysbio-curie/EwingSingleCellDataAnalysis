package vdaoengine;

import java.util.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

public class workingscript {

  public workingscript() {
  }
  public static void main(String[] args) {

    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/test.dat");

    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);


  }
}