package vdaoengine.analysis;

import java.util.*;
import java.io.*;

import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.elmap.*;

public class VDistanceCalculator {

  public static VDistanceMatrix calculateGridDistance(VDataSet ds, Grid gr, boolean addDistanceToNode){
    VDistanceMatrix dmat = new VDistanceMatrix();
    dmat.numberOfPoints = ds.pointCount;
    dmat.matrix = new float[ds.pointCount][ds.pointCount];
    VDistanceMatrix nd = calculateGridNodeDistance(gr);
    gr.MakeNodesCopy();
    for(int i=0;i<ds.pointCount;i++){ for(int j=i+1;j<ds.pointCount;j++){
      int ki = gr.getClosestNode(ds.getVector(i));
      int kj = gr.getClosestNode(ds.getVector(j));
      if(!addDistanceToNode)
        dmat.matrix[i][j]=nd.matrix[ki][kj];
      else
        dmat.matrix[i][j]=nd.matrix[ki][kj]+(float)VVectorCalc.Distance(ds.getVector(i),gr.Nodes[ki])+(float)VVectorCalc.Distance(ds.getVector(j),gr.Nodes[kj]);
      dmat.matrix[j][i] = dmat.matrix[i][j];
    }
    dmat.matrix[i][i]=0f;
    }
    return dmat;
  }

  public static VDistanceMatrix calculateGridNodeDistance(Grid gr){
    VDistanceMatrix dmat = new VDistanceMatrix();
    dmat.numberOfPoints = gr.Nodes.length;
    dmat.matrix = new float[gr.Nodes.length][gr.Nodes.length];
    gr.calculateEdgeWeightsAsDistances();
    for(int i=0;i<gr.Nodes.length;i++){
      double d[] = new double[gr.Nodes.length];
      Vector prev = new Vector();
      for(int j=0;j<gr.Nodes.length;j++)
        prev.add(new Vector());
      Algorithms.DijkstraUndirectedProcess(gr,i,0,d,prev);
      for(int j=0;j<d.length;j++)
        dmat.matrix[i][j] = (float)d[j];
    }
    return dmat;
  }

}