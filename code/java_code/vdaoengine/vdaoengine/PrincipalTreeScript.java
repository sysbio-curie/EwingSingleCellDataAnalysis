package vdaoengine;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.elmap.*;
import vdaoengine.analysis.*;

public class PrincipalTreeScript {
  public static void main(String[] args) {
    try{


      String project = "c:/datas/elastictree/iris/";
      String fn = "iris";

      System.out.println("Creating dataset "+project+"...");
      VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(project+fn+".dat");
      //VDataSet ds = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
      VDataSet ds = VSimpleProcedures.SimplyPreparedDataset(vt,-1);

      /*vdaoengine.data.io.VDatReadWrite.useQuotesEverywhere = false;
      vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFilePureNumerical(ds,project+fn+".data");
      int it = 60; double ep = 0.05; double rp = 0.5;  float le = 0.1f;
      System.out.println("Computing elmap...");
      Date cld = new Date();
      Grid gr1 = ElasticTreeAlgorithm.computeElasticTree(ds,"elmap.ini",9,it,ep,rp,le);
      gr1.filterEdges();
      System.out.println("Time: "+(int)((new Date()).getTime()-cld.getTime())+" ms");
      ((ElasticTree)gr1).writeOutTreeNodes(project+fn+".nodes");
      ((ElasticTree)gr1).writeOutEdges(project+fn+".edges");
      System.out.println("Saving projections...");
      gr1.saveProjections(project+"projt",ds,gr1.PROJECTION_CLOSEST_NODE);
      System.out.println("Saving .vem file...");
      gr1.saveToFile(project+fn+".eltree.vem",fn);*/

      ElasticTree gr = new ElasticTree(ds.coordCount);
      gr.loadFromFile(project+fn+".eltree.vem");

      //VDistanceMatrix dm = VDistanceCalculator.calculateGridNodeDistance(gr);
      VDistanceMatrix dm = VDistanceCalculator.calculateGridDistance(ds,gr,true);
      dm.saveToFile(project+fn+".grdist1");
      VDistanceMatrix mnd = new VDistanceMatrix();
      mnd.calculateMatrix(ds);
      mnd.saveToFile(project+fn+".ndist");

      VDataTable vtpca = PCAMethod.PCAtable(vt,3,true);
      VDataSet dspca = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vtpca,-1);
      VDistanceMatrix mpca = new VDistanceMatrix();
      mpca.calculateMatrix(dspca);
      mpca.saveToFile(project+fn+".pca3dist");

    }catch(Exception e){
      e.printStackTrace();
    }

  }
}