package vdaoengine;

import java.awt.*;
import java.util.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.cluster.*;
import vdaoengine.special.*;
import vdaoengine.image.im3d.*;
import com.sun.j3d.utils.applet.MainFrame;
import javax.swing.*;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

public class CodonUsagePhylogeny {

  public CodonUsagePhylogeny() {
  }

  public static void main(String[] args) {

    VDataSet VD = VSimpleProcedures.SimplyPreparedDatasetFromDatFile("out.dat",64);
    globalSettings.AddMoreStandardColors(100);

    System.out.println("Calc PCA...");
    PCAMethod PCA = new PCAMethod();
    PCA.setDataSet(VD);
    PCA.calcBasis(3);
    //VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(VD,"ADD2",VClassifier.CHANGE_FILLCOLOR);

    VKMeansClusterization vk = new VKMeansClusterization(4);
    vk.setClusterizationQuality(0.001);
    vk.doClusterization(VD);
    vk.rearrangeClusterNumbers();
    VClassifier Cl = vk.getClassifier(VClassifier.SIMPLE_MODE,VD);

    globalSettings.defaultPointSize = 4f;
    VDataImage3D im = new VDataImage3D(PCA.updateDataImage());
    im.provideClassInformation(Cl);

    VViewer3D V3d = new VViewer3D();
    V3d.addImageScene(im);
    V3d.makeLive();
    V3d.showViewer("",800,600);

  }
}