package vdaoengine.analysis.elmap;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.elmap.*;
import vdaoengine.analysis.*;

public class TestElmap {

  public static void main(String[] args) {
	  
	String prefix = "C:/Datas/VidaExpertTutorial/";
	String fn = "iris_original";

    VDataSet ds = VSimpleProcedures.SimplyPreparedDatasetFromDatFile(prefix+fn+".dat",-1);
    Grid gr = ElmapAlgorithm.computeElasticGrid(ds,"elmap.ini",13);
    //gr.saveProjections(prefix+fn+".projt",ds);

    gr.saveToFile(prefix+fn+".vem",fn);
    SimpleRectangularGrid grt = new SimpleRectangularGrid();
    grt.loadFromFile(prefix+fn+".vem");
    grt.MakeNodesCopy();
    
    ElmapProjection elmap = new ElmapProjection();
    ElmapAlgorithm ela = new ElmapAlgorithm();
    ela.setGrid(grt);
    elmap.setElmap(ela);
    elmap.setDataSet(ds);
    VDataSet vdmp = elmap.getProjectedDataset();
    grt.saveProjections(prefix+fn+".projt",ds,grt.PROJECTION_CLOSEST_POINT);

    float uncertainties[][] = elmap.calcCoordinateUncertainties(ds,5);
    
    System.out.println("\n\n");
    int gridSize = 12;
    for(int i=0;i<gridSize;i++){
    	for(int j=0;j<gridSize;j++){
    		//System.out.print(-gr.Nodes[j*gridSize+i][3]+"\t");
    		System.out.print(-uncertainties[j*gridSize+i][2]+"\t");
    	}
    	System.out.println();
    }
    
  }
}