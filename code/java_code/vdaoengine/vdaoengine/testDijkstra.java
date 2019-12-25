package vdaoengine;

import java.util.*;

import vdaoengine.analysis.elmap.*;
import vdaoengine.utils.*;

public class testDijkstra {
  public static void main(String[] args) {

    /*Set q = new HashSet();
    q.add(new Integer(1));
    System.out.println(q.size());
    q.add(new Integer(1));
    System.out.println(q.size());
    q.remove(new Integer(2));
    System.out.println(q.size());
    q.remove(new Integer(1));
    System.out.println(q.size());
    System.exit(0);*/

    SimpleRectangularGrid gr = new SimpleRectangularGrid();
    gr.loadFromFile("iris25.vem");
    gr.calculateEdgeWeightsAsDistances();
    //for(int i=0;i<gr.Nodes.length;i++){
    //  System.out.println(i+"\t"+gr.Nodes3D[i][0]+"\t"+gr.Nodes3D[i][1]);
    //}
    int isource = 2;
    int iend = 22;
    Vector paths = new Vector();
    double d = Algorithms.DijkstraUndirected(gr,isource,iend,paths);
    for(int i=0;i<paths.size();i++){
      System.out.println("Path "+(i+1));
      Vector v = (Vector)paths.get(i);
      for(int j=0;j<v.size();j++)
        System.out.print(((Integer)v.get(j)).intValue()+"\t");
      System.out.println();
    }
    System.out.println("Distance = "+d);
  }
}