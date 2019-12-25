package vdaoengine.analysis.elmap;

import java.util.*;
import java.io.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;

public class Grid {


int nodesNum;
int edgesNum;
int ribsNum;
int trianglesNum;

String name = "";

/*Vector Nodes = new Vector();
Vector Nodes3D = new Vector();
Vector Edges = new Vector();
Vector Ribs = new Vector();
Vector Triangles = new Vector();
Vector nodesIndex = new Vector();
Vector connectList = new Vector();
Vector EP = new Vector();
Vector RP = new Vector();*/

//int nribsNum;

public float Nodes[][];
public float Nodes3D[][];
public int Edges[][];
public int Ribs[][];
public Vector NRibs = new Vector();		// declare a vector to hold the NRibs (each one an integervector)
public int Triangles[][];
int nodesIndex[];
int connectList[];
float EP[];
float RP[];
float NRP[];				// and array of floats to hold NRib elastic coeffs
float NodesCopy[];
float NodesCopy2[][];

public float edgeweights[];

int n2;

public int type; // 0 - 2D rectangular
public int dimension;
public int intDim;

public static int PROJECTION_CLOSEST_NODE = 0;
public static int PROJECTION_CLOSEST_POINT = 1;

  public Grid() {
  }

  public void loadFromFile(String fn){
    try{
      LineNumberReader lr = new LineNumberReader(new FileReader(fn));
      String s = null;
      int nribsNum = 0;
      while((s=lr.readLine())!=null){

        if(s.startsWith("# GRIDTYPE")){
          String ss = lr.readLine();
          if(ss.equals("RECTANGULAR")){
            SimpleRectangularGrid sgr = (SimpleRectangularGrid)this;
            ss = lr.readLine();
            StringTokenizer st = new StringTokenizer(ss,"\t ");
            sgr.rowCount = Integer.parseInt(st.nextToken());
            sgr.colCount = Integer.parseInt(st.nextToken());
          }
          if(ss.equals("TREE")){
            ElasticTree sgr = (ElasticTree)this;
          }
        }

        if(s.startsWith("# DIMENSION"))
          dimension = Integer.parseInt(lr.readLine().trim());
        if(s.startsWith("# NODES")){
          nodesNum = Integer.parseInt(lr.readLine().trim());
          Nodes = new float[nodesNum][dimension];
          Nodes3D = new float[nodesNum][3];
        }
        if(s.startsWith("# EDGES")){
          edgesNum = Integer.parseInt(lr.readLine().trim());
          Edges = new int[edgesNum][2];
          EP = new float[edgesNum];
        }
        if(s.startsWith("# RIBS")){
          ribsNum = Integer.parseInt(lr.readLine().trim());
          Ribs = new int[ribsNum][3];
          RP = new float[ribsNum];
        }
        if(s.startsWith("# STARS")){
          nribsNum = Integer.parseInt(lr.readLine().trim());
          NRP = new float[nribsNum];
        }
        if(s.equals("# GRIDNODES")){
          for(int i=0;i<nodesNum;i++){
            s = lr.readLine();
            StringTokenizer st = new StringTokenizer(s," \t");
            try{
            for(int j=0;j<dimension;j++)
              Nodes[i][j] = Float.parseFloat(st.nextToken());
            }catch(Exception e){
              System.out.println("Error in node "+i);
            }
          }
        }
        if(s.startsWith("# GRIDNODES2D")){
          for(int i=0;i<nodesNum;i++){
            s = lr.readLine();
            StringTokenizer st = new StringTokenizer(s," \t");
            for(int j=0;j<2;j++)
              Nodes3D[i][j] = Float.parseFloat(st.nextToken());
          }
        }
        if(s.startsWith("# GRIDEDGES")){
          for(int i=0;i<edgesNum;i++){
            s = lr.readLine();
            StringTokenizer st = new StringTokenizer(s," \t");
            for(int j=0;j<2;j++)
              Edges[i][j] = Integer.parseInt(st.nextToken());
              EP[i] = 1;
          }
        }
        if(s.startsWith("# GRIDRIBS")){
          for(int i=0;i<ribsNum;i++){
            s = lr.readLine();
            StringTokenizer st = new StringTokenizer(s," \t");
            for(int j=0;j<3;j++)
              Ribs[i][j] = Integer.parseInt(st.nextToken());
              RP[i] = 1;
          }
        }
        if(s.startsWith("# GRIDSTARS")){
          NRibs = new Vector();
          for(int i=0;i<nribsNum;i++){
            s = lr.readLine();
            StringTokenizer st = new StringTokenizer(s," \t");
            IntegerVector nrib = new IntegerVector();
            while(st.hasMoreTokens()){
              nrib.appendInt(Integer.parseInt(st.nextToken()));
            }
            NRibs.add(nrib);
            NRP[NRibs.size()-1] = 1f;
        }
        }
        if(s.startsWith("# TRIANGLES")){
          trianglesNum = Integer.parseInt(lr.readLine().trim());
          Triangles = new int[trianglesNum][3];
        }
        if(s.startsWith("# GRIDTRIANGLES")){
        for(int i=0;i<trianglesNum;i++){
          s = lr.readLine();
          StringTokenizer st = new StringTokenizer(s," \t");
          for(int j=0;j<3;j++)
            Triangles[i][j] = Integer.parseInt(st.nextToken());
        }
        }



      }
      lr.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void loadNodesFromFile(String fn){
/*          FILE *f;
          f = fopen(fn,"r");
          char s[10000];
          char *sptr;
          char *tok;
          float *node = new float[dimension];
          if(f!=NULL){
                  while((sptr!=NULL)&&(s!=NULL)){
                          sptr = fgets(s,10000,f);
                          if((sptr!=NULL)&&(s!=NULL)){
                          //cout << "s = " << s << " " << endl;
                          tok = strtok(s,"\t ");
                          //cout << " tok: " << tok << endl;
                          if(tok!=NULL){
                          node[0] = atof(tok); //cout << node[0] << " ";
                          //cout << "Read toks : ";
                          for(int i=1;i<dimension;i++) { tok = strtok(NULL," \t"); node[i] = atof(tok); } //cout << endl;
                          vector<float> nd;
                          for(i=0;i<dimension;i++) nd.push_back(node[i]);
                          Nodes.push_back(nd);
                          vector<float> nd3;
                          for(i=0;i<3;i++) { if(i<dimension) nd3.push_back(node[i]); else nd3.push_back(0); }
                          Nodes3D.push_back(nd3);
                          }
                          }
                  };
          delete [] node;
          nodesNum = Nodes.size();
          ribsNum = Ribs.size();
          edgesNum = Edges.size();
          trianglesNum = Triangles.size();
          }else{ cout<< "Can not open file " << fn << endl; exit(1);};*/
  }

  public void saveToFile(String fn,String dat){
    try{
          FileWriter f = new FileWriter(fn);
          char s[] = new char[100];
                  f.write("# DATAFILENAME\r\n");
                  f.write(dat+".ved\r\n\r\n");
                  f.write("# TABLENAME\r\n");
                  f.write(dat+".vet\r\n\r\n");
                  f.write("# DIMENSION\r\n"+dimension+"\r\n\r\n");
                  f.write("# INITIALIZATION\r\nMAINCOMP 0.5 30 FALSE\r\n\r\n");
                  f.write("# ELASTICITY\r\n1 1\r\n\r\n");
                  f.write("# DISCRETE\r\nFALSE\r\n\r\n");
                  f.write("# SMOOTHNESS\r\n1\r\n\r\n");
                  f.write("# TONES\r\n0\r\n\r\n");
                  f.write("# GRIDTYPE\r\n");
                  switch(type){
                  case 0:
                          f.write("RECTANGULAR\r\n"+((SimpleRectangularGrid)this).rowCount+" "+((SimpleRectangularGrid)this).colCount+"\r\n\r\n");
                  break;
                  case 1:
                	  f.write("RECTANGULAR\r\n"+((Curve)this).rowCount+" "+((Curve)this).colCount+"\r\n\r\n");
                  break;
                  case 2:
                          //f.write("RECTANGULAR\r\n10 10\r\n\r\n");
                  break;
                  case 3:
                          //f.write("RECTANGULAR\r\n10 10\r\n\r\n");
                  break;
                  case 4:
                          //f.write("RECTANGULAR\r\n10 10\r\n\r\n");
                  break;
                case 10:
                   f.write("RECTANGULAR\r\n10 10\r\n\r\n");
                break;

                  }
                  f.write("# NODES\r\n"+nodesNum+"\r\n\r\n");
                  f.write("# EDGES\r\n"+edgesNum+"\r\n\r\n");
                  f.write("# RIBS\r\n"+ribsNum+"\r\n\r\n");
                  f.write("# STARS\r\n"+NRibs.size()+"\r\n\r\n");
                  f.write("# TRIANGLES\r\n"+trianglesNum+"\r\n\r\n");
                  f.write("# GRIDNODES\r\n");
                  for(int k=0;k<nodesNum;k++){
                          for(int i=0;i<dimension;i++){
                                  f.write(Nodes[k][i]+" ");
                          }
                  f.write("\r\n");
                  }
                  f.write("\r\n# GRIDEDGES\r\n");
                  for(int k=0;k<edgesNum;k++){
                          for(int i=0;i<2;i++){
                                  f.write(Edges[k][i]+" ");
                          }
                  f.write("\r\n");
                  }
                  f.write("\r\n# GRIDRIBS\r\n");
                  for(int k=0;k<ribsNum;k++){
                          for(int i=0;i<3;i++){
                                  f.write(Ribs[k][i]+" ");
                          }
                  f.write("\r\n");
                  }

                  f.write("\r\n# GRIDSTARS\r\n");
                  for(int k=0;k<NRibs.size();k++){
                    IntegerVector nrib = (IntegerVector)NRibs.elementAt(k);
                          for(int i=0;i<nrib.size();i++){
                                  f.write(nrib.getInt(i)+" ");
                          }
                  f.write("\r\n");
                  }

                  f.write("\r\n# GRIDTRIANGLES\r\n");
                  for(int k=0;k<trianglesNum;k++){
                          for(int i=0;i<3;i++){
                                  f.write(Triangles[k][i]+" ");
                          }
                  f.write("\r\n");
                  }
                  f.write("\r\n# GRIDNODES2D\r\n");
                  for(int k=0;k<nodesNum;k++){
                          for(int i=0;i<2;i++){
                                  f.write(Nodes3D[k][i]+" ");
                          }
                  f.write("\r\n");
                  }
                  f.write("\r\n# GRIDEP\r\n");
                  for(int k=0;k<edgesNum;k++) f.write(EP[k]+" ");
                  f.write("\r\n\r\n");
                  f.write("# GRIDRP\r\n");
                  for(int k=0;k<ribsNum;k++) f.write(RP[k]+" ");
                  f.write("\r\n\r\n");

                  f.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void generate(){
  }

  public void extrapolate(){
  }

  public void allocate(){

    Nodes = new float[nodesNum][dimension];
    Nodes3D = new float[nodesNum][3];
    Edges = new int[edgesNum][2];
    EP = new float[edgesNum];
    Ribs = new int[ribsNum][3];
    RP = new float[ribsNum];
    for(int i=0;i<edgesNum;i++) EP[i] = 1f;
    for(int i=0;i<ribsNum;i++) RP[i] = 1f;
    Triangles = new int[trianglesNum][3];
    MakeNodesCopy();
  }

  public void MakeNodesCopy(){
    NodesCopy = new float[nodesNum*dimension];
    for(int i=0;i<nodesNum;i++) {
	    for(int j=0;j<dimension;j++) {
                  NodesCopy[i*dimension+j] = Nodes[i][j];
	    }
    }
    NodesCopy2 = new float[nodesNum][dimension];
    for(int i=0;i<nodesNum;i++){
                  for(int j=0;j<dimension;j++)
                          NodesCopy2[i][j] = Nodes[i][j];
    }
    n2 = nodesNum;
  }


  public float[] projectPoint(float point[], int projectionType){

  float res[] = new float[3];
  float r, minr = Float.MAX_VALUE;
  int minj = -1;
                  for(int j=0;j<nodesNum;j++){ 
                               r = VVectorCalc.SquareEuclDistanceShift(NodesCopy,j*dimension,point,dimension);
                          if(r<minr){ minr=r; minj = j; }
                  }
                  if(projectionType==PROJECTION_CLOSEST_POINT){
                  float u = 0f,v = 0f,d,mind = Float.MAX_VALUE, minu = 0f, minv = 0f;
                  int mini = -1;
                  for(int i=0;i<Triangles.length;i++){
                          if((Triangles[i][0]==minj)||(Triangles[i][1]==minj)||(Triangles[i][2]==minj)){
                                  float p1[] = NodesCopy2[Triangles[i][0]];
                                  float p2[] = NodesCopy2[Triangles[i][1]];
                                  float p3[] = NodesCopy2[Triangles[i][2]];
                                  float rr[] = VVectorCalc.projectOnTriangle(point,p1,p2,p3);
                                  u = rr[0]; v = rr[1]; d = rr[2];
                                  if(d<mind){ mind = d; mini = i; minu = u; minv = v;}
                          }
                  }
                  if(mini>=0){
                          float x0,x1,x2;
                          for(int i=0;i<3;i++){
                                  x0 = Nodes3D[Triangles[mini][0]][i];
                                  x1 = Nodes3D[Triangles[mini][1]][i];
                                  x2 = Nodes3D[Triangles[mini][2]][i];
                                  res[i] = x0 + minu*(x1-x0) + minv*(x2-x0);
                          }
                  }
                  
                  // In case of principal curves
                  if(type==1){
                	  u = Nodes3D[minj][0];
                	  int edge1[] = null;
                	  int edge2[] = null;
                	  for(int k=0;k<edgesNum;k++){
                		  if(Edges[k][0]==minj) edge1 = Edges[k];
                		  if(Edges[k][1]==minj) edge2 = Edges[k];
                	  }
                	  float u1 = -1f;
                	  float u2 = -1f;
                	  if(edge1!=null)
                		  u1 = VVectorCalc.projectOnSegment(point, Nodes[edge1[0]], Nodes[edge1[1]])[0];
                	  if(edge2!=null)
                		  u2 = VVectorCalc.projectOnSegment(point, Nodes[edge2[0]], Nodes[edge2[1]])[0];
                	  if((u1>0)&&(u1<1)) u=Nodes3D[edge1[0]][0]+u1*(Nodes3D[edge1[1]][0]-Nodes3D[edge1[0]][0]);
                	  if((u2>0)&&(u2<1)) u=Nodes3D[edge2[0]][0]+u2*(Nodes3D[edge2[1]][0]-Nodes3D[edge2[0]][0]);
                	  res[0] = u;
                  }
                  
                  }else{
                    res = Nodes3D[minj];
                  }
  return res;
  }

  public float[] projectPointGap(float point[], int projectionType){

  float res[] = new float[3];
  float r, minr = Float.MAX_VALUE;
  int minj = -1;
                  for(int j=0;j<nodesNum;j++){
                               r = VVectorCalc.SquareEuclDistanceShiftGap(NodesCopy,j*dimension,point,dimension);
                          if(r<minr){ minr=r; minj = j; }
                  }
                  if(projectionType==PROJECTION_CLOSEST_POINT){
                  float u = 0f,v = 0f,d,mind = Float.MAX_VALUE, minu = 0f, minv = 0f;
                  int mini = -1;
                  for(int i=0;i<Triangles.length;i++){
                          if((Triangles[i][0]==minj)||(Triangles[i][1]==minj)||(Triangles[i][2]==minj)){
                                  float p1[] = NodesCopy2[Triangles[i][0]];
                                  float p2[] = NodesCopy2[Triangles[i][1]];
                                  float p3[] = NodesCopy2[Triangles[i][2]];
                                  float rr[] = VVectorCalc.projectOnTriangleGap(point,p1,p2,p3);
                                  u = rr[0]; v = rr[1]; d = rr[2];
                                  if(d<mind){ mind = d; mini = i; minu = u; minv = v;}
                          }
                  }
                  if(mini>=0){
                          float x0,x1,x2;
                          for(int i=0;i<3;i++){
                                  x0 = Nodes3D[Triangles[mini][0]][i];
                                  x1 = Nodes3D[Triangles[mini][1]][i];
                                  x2 = Nodes3D[Triangles[mini][2]][i];
                                  res[i] = x0 + minu*(x1-x0) + minv*(x2-x0);
                          }
                  }
                  
                  // In case of principal curves
                  if(type==1){
                	  u = Nodes3D[minj][0];
                	  int edge1[] = null;
                	  int edge2[] = null;
                	  for(int k=0;k<edgesNum;k++){
                		  if(Edges[k][0]==minj) edge1 = Edges[k];
                		  if(Edges[k][1]==minj) edge2 = Edges[k];
                	  }
                	  float u1 = -1f;
                	  float u2 = -1f;
                	  if(edge1!=null)
                		  u1 = VVectorCalc.projectOnSegmentGap(point, Nodes[edge1[0]], Nodes[edge1[1]])[0];
                	  if(edge2!=null)
                		  u2 = VVectorCalc.projectOnSegmentGap(point, Nodes[edge2[0]], Nodes[edge2[1]])[0];
                	  if((u1>0)&&(u1<1)) u=Nodes3D[edge1[0]][0]+u1*(Nodes3D[edge1[1]][0]-Nodes3D[edge1[0]][0]);
                	  if((u2>0)&&(u2<1)) u=Nodes3D[edge2[0]][0]+u2*(Nodes3D[edge2[1]][0]-Nodes3D[edge2[0]][0]);
                	  res[0] = u;
                  }
                  
                  }else{
                    res = Nodes3D[minj];
                  }
  return res;
  }


  public int getClosestNode(float point[]){
  float r, minr = Float.MAX_VALUE;
  int minj = -1;
                  for(int j=0;j<nodesNum;j++){
                               r = VVectorCalc.SquareEuclDistanceShift(NodesCopy,j*dimension,point,dimension);
                          if(r<minr){ minr=r; minj = j; }
                  }
  return minj;
  }

  public int getClosestNodeGap(float point[]){
  float r, minr = Float.MAX_VALUE;
  int minj = -1;
                  for(int j=0;j<nodesNum;j++){
                               r = VVectorCalc.SquareEuclDistanceShiftGap(NodesCopy,j*dimension,point,dimension);
                          if(r<minr){ minr=r; minj = j; }
                  }
  return minj;
  }



  public void saveProjections(String fn, VDataSet ds, int projectionType){
       try{
                  FileWriter f = new FileWriter(fn);
                  MakeNodesCopy();
                  for(int i=0;i<ds.pointCount;i++){
                          float v[] = ds.getVector(i);
                          float proj[] = projectPoint(v, projectionType);
                          f.write(proj[0]+"\t"+proj[1]+"\t"+proj[2]+"\n");
                  }
                  f.close();

       }catch(Exception e){
         e.printStackTrace();
       }
  }

  public void saveNodeClassClusters(String fn, VDataSet ds, VClassifier vcl){
       try{

         MakeNodesCopy();
         Vector classes = vcl.getClassVector();

         Vector classNums = new Vector();
         FileWriter f = new FileWriter(fn);
         FileWriter fw = new FileWriter(fn+".dat");

         for(int i=0;i<classes.size();i++){
           VDataClass cl = (VDataClass)classes.elementAt(i);
           HashSet hs = cl.getIDset();
           Iterator it = hs.iterator();
           Vector v = new Vector();
           while(it.hasNext()){
             int k = ((Integer)it.next()).intValue();
             int n = (int)((float)k/1000000f);
             k-=n*1000000;
             v.add(new Integer(k));
             //System.out.print(""+k+",");
           }
           classNums.add(v);
           //System.out.println();
         }

         fw.write((dimension+2+classNums.size())+" "+Nodes.length+"\r\n");
         fw.write("N STRING\r\n");
         for(int i=0;i<classNums.size();i++){
           VDataClass cl = (VDataClass)classes.elementAt(i);
           fw.write(cl.shortname+" FLOAT\r\n");
         }
         fw.write("TTL"+" FLOAT\r\n");
         for(int i=0;i<dimension;i++){
           fw.write("F"+(i+1)+" FLOAT\r\n");
         }

         Vector taxons = calcTaxons(ds);
         for(int i=0;i<taxons.size();i++){
           fw.write(""+i+" ");
           int tax[] = (int[])taxons.elementAt(i);
           int cls[] = new int[classNums.size()];
           for(int j=0;j<tax.length;j++){
             int k = -1;
             for(int l=0;l<classNums.size();l++){
               Vector vc = (Vector)classNums.elementAt(l);
               if(vc.indexOf(new Integer(tax[j]))>=0)
                 k = l;
             }
             if(k>=0) cls[k]++;
           }
           for(int j=0;j<cls.length;j++){
             f.write(cls[j]+"\t");
             fw.write(""+cls[j]+" ");
           }
           f.write(tax.length+"\r\n");
           fw.write(tax.length+" ");
           for(int j=0;j<dimension;j++)
             fw.write(Nodes[i][j]+" ");
           fw.write("\r\n");
         }


         f.close();
         fw.close();

       }catch(Exception e){
         e.printStackTrace();
       }
  }


  public void copyfrom(Grid sgr){
          for(int i=0;i<nodesNum;i++){
                  for(int j=0;j<dimension;j++) Nodes[i][j] = sgr.Nodes[i][j];
                  for(int j=0;j<3;j++) Nodes3D[i][j] = sgr.Nodes3D[i][j];
          }
          for(int i=0;i<edgesNum;i++){
                  Edges[i][0] = sgr.Edges[i][0];
                  Edges[i][1] = sgr.Edges[i][1];
                  EP[i] = sgr.EP[i];
          }
          for(int i=0;i<ribsNum;i++){
                  Ribs[i][0] = sgr.Ribs[i][0];
                  Ribs[i][1] = sgr.Ribs[i][1];
                  Ribs[i][2] = sgr.Ribs[i][2];
                  RP[i] = sgr.RP[i];
          }
          for(int i=0;i<trianglesNum;i++){
                  Triangles[i][0] = sgr.Triangles[i][0];
                  Triangles[i][1] = sgr.Triangles[i][1];
                  Triangles[i][2] = sgr.Triangles[i][2];
          }
  }

  public double[] getDistancesToCentroids(VDataSet dat, Vector taxons){
          double mas[] = new double[nodesNum];
          float vec[] = new float[dimension];
          for(int i=0;i<nodesNum;i++){
                  int tax[] = (int [])taxons.elementAt(i);
                  for(int j=0;j<dimension;j++) {
                          vec[j] = (float)0;
                          for(int k=0;k<tax.length;k++)
                                  vec[j]+=dat.massif[tax[k]][j];
                          if(tax.length==0)
                          vec[j]=0;
                          else
                          vec[j]/=tax.length;
                  }
                  mas[i] = (VVectorCalc.SquareEuclDistance(Nodes[i],vec));
                  mas[i] = mas[i]*mas[i];
          }
        return mas;
  }

  public void fixEElasticity(int mode){

          int i;

          float minl = Float.MAX_VALUE;
          float maxl = -1f;
          float meanl = 0f;
          for(i=0;i<Edges.length;i++){
                  float l = 0;
                  for(int k=0;k<dimension;k++){
                          float h = 0;
                          if(mode==0)if(k<=3)
                                  h = Nodes3D[Edges[i][0]][k] - Nodes3D[Edges[i][1]][k];
                          if(mode==1)
                                  h = Nodes[Edges[i][0]][k] - Nodes[Edges[i][1]][k];
                          l+= h*h;
                  }
          if(l>maxl) maxl = l;
          if(l<minl) minl = l;
          meanl+=l;
          }
          meanl/=Edges.length;
          for(i=0;i<Edges.length;i++){
                  float l = 0;
                  for(int k=0;k<dimension;k++){
                          float h = 0;
                          if(mode==0)
                                  h = Nodes3D[Edges[i][0]][k] - Nodes3D[Edges[i][1]][k];
                          if(mode==1)
                                  h = Nodes[Edges[i][0]][k] - Nodes[Edges[i][1]][k];
                          l+= h*h;
                  }
          EP[i] = meanl/l;
          }
  }

  public void fixRElasticity(int mode){

          int i;
          float minl = Float.MAX_VALUE;
          float maxl = -1f;
          float meanl = 0f;

          for(i=0;i<Ribs.length;i++){
                  float l = 0f;
                  for(int k=0;k<dimension;k++){
                          float h = 0f;
                          if(mode==0)if(k<=3)
                                  h = Nodes3D[Ribs[i][1]][k] + Nodes3D[Ribs[i][2]][k] - 2*Nodes3D[Ribs[i][0]][k];
                          if(mode==1)
                                  h = Nodes[Ribs[i][1]][k] + Nodes[Ribs[i][2]][k] - 2*Nodes3D[Ribs[i][0]][k];
                          l+= h*h;
                  }
          if(l>maxl) maxl = l;
          if(l<minl) minl = l;
          meanl+=l;
          }
          meanl/=Ribs.length;
          for(i=0;i<Ribs.length;i++){
                  float l = 0;
                  for(int k=0;k<dimension;k++){
                          float h = 0f;
                          if(mode==0)if(k<=3)
                                  h = Nodes3D[Ribs[i][1]][k] + Nodes3D[Ribs[i][2]][k] - 2*Nodes3D[Ribs[i][0]][k];
                          if(mode==1)
                                  h = Nodes[Ribs[i][1]][k] + Nodes[Ribs[i][2]][k] - 2*Nodes3D[Ribs[i][0]][k];
                          l+= h*h;
                  }
          RP[i] = meanl/l;
          }
  }


  public double calcMSE(VDataSet data, Vector taxons){

          float vec[] = new float[dimension];

          /*double d = 0;
          for(int i=0;i<nodesNum;i++){
                  int tax[] = (int [])taxons.elementAt(i);
                  for(int k=0;k<dimension;k++) vec[k] = 0;
                  for(int j=0;j<tax.length;j++){
                          for(int k=0;k<dimension;k++){
                                  vec[k]+=data.massif[tax[j]][k];
                          }
                  }
                  if(tax.length>0)
                          for(int k=0;k<dimension;k++) vec[k] = vec[k]/tax.length;
                  if(!data.hasGaps)
                    for(int j=0;j<tax.length;j++){
                           d+=VVectorCalc.SquareEuclDistance(data.getVector(tax[j]),vec);
                    }
                  else
                    for(int j=0;j<tax.length;j++){
                           d+=VVectorCalc.SquareEuclDistanceGap(data.getVector(tax[j]),vec);
                    }
          }*/

          double d = 0;
          for(int i=0;i<nodesNum;i++){
                  int tax[] = (int [])taxons.elementAt(i);
                  for(int k=0;k<dimension;k++) vec[k] = 0;
                  if(!data.hasGaps)
                    for(int j=0;j<tax.length;j++){
                           d+=VVectorCalc.SquareEuclDistance(data.getVector(tax[j]),Nodes[i]);
                    }
                  else
                    for(int j=0;j<tax.length;j++){
                           d+=VVectorCalc.SquareEuclDistanceGap(data.getVector(tax[j]),Nodes[i]);
                    }
          }

          return Math.sqrt(d/data.pointCount);
  }
  
  


  public Vector calcTaxons(VDataSet data){

          Vector taxons = new Vector();

          for(int j=0;j<nodesNum;j++){
                  Vector v = new Vector();
                  taxons.add(v);
          }
          float r = 0f;

          int dimen = data.coordCount;

          if(!data.hasGaps)
          for(int i=0;i<data.pointCount;i++){
                  float v[] = data.getVector(i);
                  float minr = (float)Float.MAX_VALUE ;
                  int minj = -1;
                  for(int j=0;j<nodesNum;j++){
                          r = VVectorCalc.SquareEuclDistanceShift(NodesCopy,j*dimen,v,data.coordCount);
                          if(r<minr){ minr=r; minj = j; }
                  }
                  if(minj!=-1){
                          ((Vector)taxons.elementAt(minj)).add(new Integer(i));
                  }
          }
          else
            for(int i=0;i<data.pointCount;i++){
                    float v[] = data.getVector(i);
                    float minr = (float)Float.MAX_VALUE ;
                    int minj = -1;
                    for(int j=0;j<nodesNum;j++){
                            r = VVectorCalc.SquareEuclDistanceShiftGap(NodesCopy,j*dimen,v,data.coordCount);
                            if(r<minr){ minr=r; minj = j; }
                    }
                    if(minj!=-1){
                            ((Vector)taxons.elementAt(minj)).add(new Integer(i));
                    }
            }
          Vector res = new Vector();
          for(int i=0;i<nodesNum;i++){
            Vector v = (Vector)taxons.elementAt(i);
            int tx[] = new int[v.size()];
            for(int j=0;j<v.size();j++)
              tx[j] = ((Integer)v.elementAt(j)).intValue();
            res.add(tx);
          }
          return res;
  }

  public void filterEdges(){
    int newEdgesNum = 0;
    Vector v = new Vector();
    for(int i=0;i<edgesNum;i++){
      boolean found = false;
      for(int j=0;j<v.size();j++){
        int edge[] = (int[])v.elementAt(j);
        if(Edges[i][0]==Edges[i][1]) found = true;
        if((Edges[i][0]==edge[0])&&(Edges[i][1]==edge[1])) found = true;
        if((Edges[i][0]==edge[1])&&(Edges[i][1]==edge[0])) found = true;
      }
      if(!found)
        v.add(Edges[i]);
    }
    edgesNum = v.size();
    Edges = new int[v.size()][2];
    for(int j=0;j<v.size();j++){
      int edge[] = (int[])v.elementAt(j);
      Edges[j] = edge;
    }
  }

  public void calculateEdgeWeightsAsDistances(){
     edgeweights = new float[Edges.length];
     for(int i=0;i<Edges.length;i++){
       int i1 = Edges[i][0];
       int i2 = Edges[i][1];
       edgeweights[i] = (float)VVectorCalc.Distance(Nodes[i1],Nodes[i2]);
     }
  }
/****************************** SAME THINGS BUT WITH NEW ARRAYS ***********************************

public Vector calcTreeTaxons(VDataSet data){

          Vector taxons = new Vector();
	  taxons.setSize(0);

          for(int j=0;j<treeNodesNum;j++){
                  Vector v = new Vector();
                  taxons.add(v);
          }
          float r = 0f;

          int dimen = data.coordCount;

          for(int i=0;i<data.pointCount;i++){
                  float v[] = data.getVector(i);
                  float minr = (float)Float.MAX_VALUE ;
                  int minj = -1;
                  for(int j=0;j<treeNodesNum;j++){
                          r = VVectorCalc.VecSquareEuclDistanceShift(TreeNodesCopy,j,v,data.coordCount);
                          if(r<minr){ minr=r; minj = j; }
                  }
                  if(minj!=-1){
                          ((Vector)taxons.elementAt(minj)).add(new Integer(i));
                  }
          }
          Vector res = new Vector();
          for(int i=0;i<treeNodesNum;i++){
            Vector v = (Vector)taxons.elementAt(i);   ///taxons.elementAt(i)..... ?
            int tx[] = new int[v.size()];
	    //System.out.println("length of vector at tx[node"+i+"] is "+v.size());
            for(int j=0;j<v.size();j++) {
              tx[j] = ((Integer)v.elementAt(j)).intValue();
	      //System.out.println("tx["+j+"] = "+tx[j]);

	    }
            res.add(tx);
	    //System.out.println("length of element in res = "+ ((int[])res.elementAt(i)).length);
          }
          return res;
  }


   public void MakeTreeNodesCopy() {
//treenodescopy doesnt exist here ...... so ... well what? i guess it may not exist elsewhere?
    TreeNodesCopy = new Vector();
    TreeNodesCopy.setSize(treeNodesNum);
    //and here it is null
/************************************************************************************************************************************
//output the nodes before testing for additions
for(int i9=0;i9<treeNodesNum;i9++) {
		  for(int j9=0;j9<dimension;j9++) {
			  System.out.println("$$$$$$$$$$$$$$$BEFORE MAKING NODES COPY treenode "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodes.elementAt(i9))[j9]);
		  	System.out.println("$$$$$$$$$$$$$$$BEFORE MAKING NODES COPY treenodevopy "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodesCopy.elementAt(i9))[j9]);

		  }
	  }

/************************************************************************************************************************************


     for(int i=0;i<treeNodesNum;i++) {
	    temp = new float[dimension];
	    temp2 = new float[dimension];

	    temp = (float[])TreeNodes.elementAt(i);
	    for(int j=0;j<dimension;j++) {
		    temp2[j] = temp[j];
	    }
	 TreeNodesCopy.setElementAt((float[])temp2, i);
    }


/************************************************************************************************************************************
//output the nodes before testing for additions
for(int i9=0;i9<treeNodesNum;i9++) {
		  for(int j9=0;j9<dimension;j9++) {
			  System.out.println("$$$$$$$$$$$$$$AFTER MAKIING NODES COPY treenode "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodes.elementAt(i9))[j9]);
			  System.out.println("$$$$$$$$$$$$$$AFTER MAKIING NODES COPY treenodecopy "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodesCopy.elementAt(i9))[j9]);
		  }
	  }

/************************************************************************************************************************************/


  //}
  
  public void setDefaultEP(float ep){
	  for(int i=0;i<EP.length;i++) EP[i] = ep;
  }
  public void setDefaultRP(float rp){
	  for(int i=0;i<RP.length;i++) RP[i] = rp;
  }
  
  public float getNodeTension(int k){
	  float tension = 0f;
	  for(int i=0;i<Ribs.length;i++){
		  if(Ribs[i][0]==k){
			  float x1[] = VVectorCalc.Subtract_(Nodes[Ribs[i][1]],Nodes[Ribs[i][0]]);
			  float x2[] = VVectorCalc.Subtract_(Nodes[Ribs[i][2]],Nodes[Ribs[i][0]]);
			  VVectorCalc.Normalize(x1);
			  VVectorCalc.Normalize(x2);
			  tension+=VVectorCalc.ScalarMult(x1, x2);
			  //tension+=VVectorCalc.Norm(VVectorCalc.Subtract_(VVectorCalc.Product_(VVectorCalc.Add_(Nodes[Ribs[i][1]], Nodes[Ribs[i][1]]), 0.5f), Nodes[Ribs[i][0]]));
		  }
	  }
	  return tension+1;
  }
  

}