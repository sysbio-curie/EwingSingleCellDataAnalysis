package vdaoengine.analysis.elmap;

import vdaoengine.analysis.elmap.Grid;
import vdaoengine.utils.*;

public class SimpleRectangularGrid extends Grid {

          public int rowCount;
          public int colCount;

  public SimpleRectangularGrid(int cols, int rows, int dim) {
    rowCount = rows;
    colCount = cols;
    dimension = dim;
    type = 0;
    generate();
  }

  public SimpleRectangularGrid() {
  }


  public void extrapolate(){
          System.out.println("Extrapolation step... \n");
          SimpleRectangularGrid sgr = new SimpleRectangularGrid(colCount+2,rowCount+2,dimension);
          for(int i=0;i<nodesNum;i++){
                  int row = (int)((double)i/colCount);
                  int col = i-row*colCount;
                  row++; col++;
                  int j = row*(colCount+2)+col;
                  for(int k=0;k<dimension;k++) sgr.Nodes[j][k] = Nodes[i][k];
          }
          for(int i=0;i<sgr.nodesNum;i++){
                  int row = (int)((double)i/sgr.colCount);
                  int col = i-row*sgr.colCount;
                  if((row==0)&&(col!=0)&&(col!=sgr.colCount-1)){
                          int i1 = i+sgr.colCount;
                          int i2 = i+2*sgr.colCount;
                          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = 2*sgr.Nodes[i1][k]-sgr.Nodes[i2][k];
                  }
                  if((row==sgr.rowCount-1)&&(col!=0)&&(col!=sgr.colCount-1)){
                          int i1 = i-sgr.colCount;
                          int i2 = i-2*sgr.colCount;
                          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = 2*sgr.Nodes[i1][k]-sgr.Nodes[i2][k];
                  }
                  if((col==0)&&(row!=0)&&(row!=sgr.rowCount-1)){
                          int i1 = i+1;
                          int i2 = i+2;
                          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = 2*sgr.Nodes[i1][k]-sgr.Nodes[i2][k];
                  }
                  if((col==sgr.colCount-1)&&(row!=0)&&(row!=sgr.rowCount-1)){
                          int i1 = i-1;
                          int i2 = i-2;
                          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = 2*sgr.Nodes[i1][k]-sgr.Nodes[i2][k];
                  }
          }
          int i = 0;
          int i1 = sgr.colCount+1;
          int i2 = 1;
          int i3 = sgr.colCount;
          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = -sgr.Nodes[i1][k]+sgr.Nodes[i2][k]+sgr.Nodes[i3][k];

          i = sgr.colCount-1;
          i1 = i+sgr.colCount-1;
          i2 = i+sgr.colCount;
          i3 = i-1;
          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = -sgr.Nodes[i1][k]+sgr.Nodes[i2][k]+sgr.Nodes[i3][k];

          i = sgr.colCount*(sgr.rowCount-1);
          i1 = i-sgr.colCount+1;
          i2 = i-sgr.colCount;
          i3 = i+1;
          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = -sgr.Nodes[i1][k]+sgr.Nodes[i2][k]+sgr.Nodes[i3][k];

          i = sgr.colCount*sgr.rowCount-1;
          i1 = i-sgr.colCount-1;
          i2 = i-sgr.colCount;
          i3 = i-1;
          for(int k=0;k<dimension;k++) sgr.Nodes[i][k] = -sgr.Nodes[i1][k]+sgr.Nodes[i2][k]+sgr.Nodes[i3][k];
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

    nodesNum = colCount*rowCount;
    edgesNum = 2*colCount*rowCount-colCount-rowCount;
    ribsNum = 2*(colCount*rowCount-colCount-rowCount);
    trianglesNum = 2*(rowCount-1)*(colCount-1);
    intDim = 2;

    allocate();

    for(int i=0;i<rowCount;i++)
      for(int j=0;j<colCount;j++) {
        Nodes3D[colCount*i+j][0]=(float)((float)i/(rowCount-1)*2.0f-1.0f);
        if(colCount==1) Nodes3D[colCount*i+j][1]=0;
        else
            Nodes3D[colCount*i+j][1]=(float)((float)j/(colCount-1)*2.0f-1.0f);
        Nodes3D[colCount*i+j][2]=0;
            }

    int k=0;
    for(int i=0;i<rowCount;i++)
       for(int j=0;j<colCount;j++) {
        if(j<colCount-1) {
         Edges[k][0]=colCount*i+j;
         Edges[k][1]=colCount*i+j+1;
         k++;
        }
        if(i<rowCount-1) {
         Edges[k][0]=i*colCount+j;
         Edges[k][1]=(i+1)*colCount+j;
         k++;
        }
       }

     k=0;
     for(int i=0;i<rowCount;i++)
      for(int j=0;j<colCount;j++) {
         if(j<colCount-2) {
          Ribs[k][0]=colCount*i+j+1;
          Ribs[k][1]=colCount*i+j;
          Ribs[k][2]=colCount*i+j+2;
          k++;
         }
         if(i<rowCount-2) {
          Ribs[k][0]=(i+1)*colCount+j;
          Ribs[k][1]=i*colCount+j;
          Ribs[k][2]=(i+2)*colCount+j;
          k++;
         }
        }

      k=0;
      for(int i=0;i<colCount-1;i++)
       for(int j=0;j<rowCount-1;j++) {
        Triangles[k][0]=colCount*j+i;
        Triangles[k][1]=colCount*j+i+1;
        Triangles[k][2]=colCount*(j+1)+i;
        k++;
        Triangles[k][0]=colCount*(j+1)+i+1;
        Triangles[k][1]=colCount*j+i+1;
        Triangles[k][2]=colCount*(j+1)+i;
        k++;
       }
  }

  public float[] projectFromInToOut(float u, float v){
    float res[] = new float[dimension];
    u = (u+1)/2*(rowCount-1);
    v = (v+1)/2*(colCount-1);
    float u0 = (float)Math.floor(u);
    float v0 = (float)Math.floor(v);
    int nr = rowCount;
    int nc = colCount;
    float us = u-u0;
    float vs = v-v0;

    int node00 = (int)(colCount*u0+v0+0.01);
    int node01 = (int)(colCount*(u0+1)+v0+0.01);
    int node10 = (int)(colCount*u0+(v0+1)+0.01);
    int node11 = (int)(colCount*(u0+1)+(v0+1)+0.01);

    if((Math.abs(us)<1e-10)&&(Math.abs(vs)<1e-10)){
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
    }
    return res;
  }

}