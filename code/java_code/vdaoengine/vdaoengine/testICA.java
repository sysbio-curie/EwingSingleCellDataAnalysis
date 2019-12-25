package vdaoengine;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import java.util.*;

public class testICA {

  public static void main(String[] args) {

    String prefix = "c:/datas/breastcancer/160606/wang_basal/wang_basal_na";
    VDataTable vta = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+"_averaged.dat");

    VHistogram vh = new VHistogram();
    Vector f = new Vector(); f.add("A_MEAN"); f.add("B_MEAN");
    vh.setBordersMinMax(vta,10,f);
    vh.MakeBasicHistogram(vta,f);
    System.out.println(vh.toString());


  }
}