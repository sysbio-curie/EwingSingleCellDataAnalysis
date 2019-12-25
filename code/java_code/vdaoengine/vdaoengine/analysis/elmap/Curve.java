package vdaoengine.analysis.elmap;

import vdaoengine.analysis.elmap.Grid;
import vdaoengine.utils.*;

public class Curve extends Grid {

          public int rowCount;
          public int colCount;

  public Curve(int cols, int dim) {
    colCount = cols;
    dimension = dim;
    type = 1;
    generate();
  }

  public Curve() {
	  type = 1;
  }


  public void extrapolate(){
          System.out.println("Extrapolation step... \n");
          Curve sgr = new Curve(colCount+2,dimension);
          for(int i=1;i<nodesNum+1;i++){
                  int col = i;
                  col++;
                  for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = Nodes[i-1][k];
          }
          
          for(int k=0;k<dimension;k++) sgr.Nodes[0][k] = 2*Nodes[0][k]-Nodes[1][k];
          for(int k=0;k<dimension;k++) sgr.Nodes[nodesNum+1][k] = 2*Nodes[nodesNum-1][k]-Nodes[nodesNum-2][k];
          
          nodesNum = sgr.nodesNum;
          edgesNum = sgr.edgesNum;
          ribsNum = sgr.ribsNum;
          trianglesNum = sgr.trianglesNum;
          colCount = sgr.colCount;
          rowCount = sgr.rowCount;
          allocate();
          copyfrom(sgr);
  }

  public void interpolate(){
    // not yet implemented
  }

  public void generate(){

    nodesNum = colCount;
    edgesNum = colCount-1;
    ribsNum = colCount-2;
    trianglesNum = 0;
    intDim = 1;

    allocate();

      for(int j=0;j<colCount;j++) {
        Nodes3D[j][0]=(float)((float)j/(colCount-1)*2.0f-1.0f);
        Nodes3D[j][1]=0;
        Nodes3D[j][2]=0;
      }

    int k=0;
       for(int j=0;j<colCount;j++) {
        if(j<colCount-1) {
         Edges[k][0]=j;
         Edges[k][1]=j+1;
         k++;
        }
       }

     k=0;
      for(int j=0;j<colCount;j++) {
         if(j<colCount-2) {
          Ribs[k][0]=j+1;
          Ribs[k][1]=j;
          Ribs[k][2]=j+2;
          k++;
         }
        }

  }

  public float[] projectFromInToOut(float u){
    float res[] = new float[dimension];
    int nodei = (int)((u+1)/2*(colCount-1));
    float u0 = (float)((float)nodei/(colCount-1)*2.0f-1.0f);
    float fraction = 0.5f*(u-u0)*(colCount-1);
    res = VVectorCalc.Add_(Nodes[nodei],VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[nodei+1], Nodes[nodei]), fraction));
    
    /*float u0 = (float)Math.floor(u);
    int nc = colCount;
    float us = u-u0;

    int node00 = (int)(colCount*u0+0.01);
    int node01 = (int)(colCount*(u0+1)+0.01);
    int node10 = (int)(colCount*u0+1+0.01);
    int node11 = (int)(colCount*(u0+1)+1+0.01);*/

    /*if((Math.abs(us)<1e-10)&&(Math.abs(vs)<1e-10)){
      res = Nodes[node00];
    }else{

    if(us>1-vs)
      res = VVectorCalc.Add_(VVectorCalc.Add_(Nodes[node11],VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node10],Nodes[node11]),1-us)),
                                             VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node01],Nodes[node11]),1-vs));
    else
     if((v0<nr-1)&&(u0<nc-1))
       res = VVectorCalc.Add_(VVectorCalc.Add_(Nodes[node00],VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node01],Nodes[node00]),us)),
                                         VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node10],Nodes[node00]),vs));
     else
      if((v0>=nr-1)&&(u0>=nc-1))
       res = VVectorCalc.Add_(VVectorCalc.Add_(Nodes[node00],VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node00],Nodes[node00]),us)),
                                         VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node00],Nodes[node00]),vs));
      else
      if((v0>=nr-1))
       res = VVectorCalc.Add_(VVectorCalc.Add_(Nodes[node00],VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node01],Nodes[node00]),us)),
                                         VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node00],Nodes[node00]),vs));
      else
      if((u0>=nc-1))
       res = VVectorCalc.Add_(VVectorCalc.Add_(Nodes[node00],VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node00],Nodes[node00]),us)),
                                         VVectorCalc.Product_(VVectorCalc.Subtract_(Nodes[node10],Nodes[node00]),vs));
    }*/
    return res;
  }

}