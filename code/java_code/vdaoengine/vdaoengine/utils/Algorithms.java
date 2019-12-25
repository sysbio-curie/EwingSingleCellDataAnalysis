package vdaoengine.utils;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.elmap.*;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

public class Algorithms {

  public Algorithms() {
  }

  public static int IndexOfI(int mas[], int n){
    int k = -1;
    for(int i=0;i<mas.length;i++){
      if(mas[i]==n) k = i;
    }
    return k;
  }
  
  public static int[] SortMass(Float mass[]){
	  float m[] = new float[mass.length];
	  for(int i=0;i<mass.length;i++)
		  m[i] = mass[i];
	  return SortMass(m);
  }
  

  public static int[] SortMass(float cais[]){
  int res[]=new int[cais.length];
  for (int i = 0; i < res.length; i++) res[i]=i;

  int i,j,k,inc,n=cais.length;
  float v;

  inc=1;
  do {
  	inc *= 3;
  	inc++;
  } while (inc <= n);

  do {
  	inc /= 3;
  	for (i=inc+1;i<=n;i++) {
  		v=cais[res[i-1]];
  		j=i;
                  k=res[i-1];
  		while (cais[res[j-inc-1]]>v) {
  			//cais[j]=cais[j-inc];
                          res[j-1]=res[j-inc-1];
  			j -= inc;
  			if (j <= inc) break;
  		}
  		//cais[j]=v;
                  res[j-1]=k;
  	}
  } while (inc > 0);

  return res;
}
  
  public static int[] SortMass(double cais[]){
  int res[]=new int[cais.length];
  for (int i = 0; i < res.length; i++) res[i]=i;

  int i,j,k,inc,n=cais.length;
  double v;

  inc=1;
  do {
  	inc *= 3;
  	inc++;
  } while (inc <= n);

  do {
  	inc /= 3;
  	for (i=inc+1;i<=n;i++) {
  		v=cais[res[i-1]];
  		j=i;
                  k=res[i-1];
  		while (cais[res[j-inc-1]]>v) {
  			//cais[j]=cais[j-inc];
                          res[j-1]=res[j-inc-1];
  			j -= inc;
  			if (j <= inc) break;
  		}
  		//cais[j]=v;
                  res[j-1]=k;
  	}
  } while (inc > 0);

  return res;
}


public static int[] SortStringMass(String cais[]){
int res[]=new int[cais.length];
for (int i = 0; i < res.length; i++) res[i]=i;

int i,j,k,inc,n=cais.length;
String v;

inc=1;
do {
        inc *= 3;
        inc++;
} while (inc <= n);

do {
        inc /= 3;
        for (i=inc+1;i<=n;i++) {
                v=cais[res[i-1]];
                j=i;
                k=res[i-1];
                while (cais[res[j-inc-1]].compareTo(v)>0) {
                        //cais[j]=cais[j-inc];
                        res[j-1]=res[j-inc-1];
                        j -= inc;
                        if (j <= inc) break;
                }
                //cais[j]=v;
                res[j-1]=k;
        }
} while (inc > 0);

return res;
}


public static float[] rotateVectorX(float vec[], float angle){
  float res[] = new float[3];
  res[0] = vec[0];
  //float r = Math.sqrt(vec[1]*vec[1]+vec[2]*vec[2]);
  res[1] = (float)(vec[1]*Math.cos(angle)+vec[2]*Math.sin(angle));
  res[2] = (float)(vec[1]*Math.sin(angle)-vec[2]*Math.cos(angle));
  return res;
}

public static double[] rotateVectorX(double vec[], float angle){
  double res[] = new double[3];
  res[0] = vec[0];
  //double r = Math.sqrt(vec[1]*vec[1]+vec[2]*vec[2]);
  res[1] = (double)(vec[1]*Math.cos(angle)+vec[2]*Math.sin(angle));
  res[2] = (double)(vec[1]*Math.sin(angle)-vec[2]*Math.cos(angle));
  return res;
}

public static VDataTable PCAtable(VDataTable vt, boolean doScaling){
  VDataTable vtr = new VDataTable();
  int numOfComponents = 3;
  int numOfStringFields = 0;
  for(int i=0;i<vt.colCount;i++)
    if(vt.fieldTypes[i]==vt.STRING)
      numOfStringFields++;
  vtr.fieldClasses = new String[numOfStringFields+numOfComponents];
  vtr.fieldDescriptions = new String[numOfStringFields+numOfComponents];
  vtr.fieldNames = new String[numOfStringFields+numOfComponents];
  vtr.fieldTypes = new int[numOfStringFields+numOfComponents];
  if(vt.fieldInfo!=null)
    vtr.fieldInfo = new String[numOfStringFields+numOfComponents][vt.fieldInfo[0].length];
  int k=0;
  for(int i=0;i<vt.colCount;i++)
    if(vt.fieldTypes[i]==vt.STRING){
      vtr.fieldClasses[k] = vt.fieldClasses[i];
      vtr.fieldNames[k] = vt.fieldNames[i];
      vtr.fieldDescriptions[k] = vt.fieldDescriptions[i];
      vtr.fieldTypes[k] = vt.fieldTypes[i];
      if(vt.fieldInfo!=null)
        vtr.fieldInfo[k] = vt.fieldInfo[i];
      k++;
    }
  for(int i=0;i<numOfComponents;i++){
    vtr.fieldNames[i+numOfStringFields] = "PC"+(i+1);
    vtr.fieldTypes[i+numOfStringFields] = vt.NUMERICAL;
  }
  vtr.rowCount = vt.rowCount;
  vtr.colCount = numOfStringFields+numOfComponents;
  vtr.stringTable = new String[vtr.rowCount][vtr.colCount];
  for(int i=0;i<vtr.rowCount;i++){
    k = 0;
    for(int j=0;j<vt.colCount;j++){
      if(vt.fieldTypes[j]==vt.STRING){
          vtr.stringTable[i][k] = vt.stringTable[i][j];
          k++;
      }
    }
  }

  VDataSet vd = null;
    if(doScaling)
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    else
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
  PCAMethod pca = new PCAMethod();
  pca.setDataSet(vd);
  pca.calcBasis(numOfComponents);
  VDataSet vdp = pca.getProjectedDataset();
  for(int i=0;i<vtr.rowCount;i++)
  for(int j=0;j<numOfComponents;j++){
    vtr.stringTable[i][j+numOfStringFields] = "" + vdp.massif[i][j];
  }
  return vtr;
  }

  public static float[] fitEvenPolynome(float x[], float y[], int degree){
    float coeff[] = new float[degree+1];
    float A[][] = new float[degree+1][degree+1];
    for(int j=0;j<=degree;j++)for(int k=j;k<=degree;k++){
      A[j][k] = 0f;
      for(int i=0;i<x.length;i++)
        A[j][k]+=intPower(x[i],2*(j+k));
      A[k][j] = A[j][k];
    }
    float B[] = new float[degree+1];
    for(int k=0;k<=degree;k++){
      B[k] = 0f;
      for(int i=0;i<x.length;i++)
        B[k]+=y[i]*intPower(x[i],2*k);
    }
    float meany = 0f;
    for(int i=0;i<x.length;i++) meany+=y[i];
    coeff[0] = meany/x.length;
    coeff = solveLinearSystem(A,B,coeff);
    return coeff;
  }

  public static float[] fitPolynome(float x[], float y[], int degree){
    float coeff[] = new float[degree+1];
    float A[][] = new float[degree+1][degree+1];
    for(int j=0;j<=degree;j++)for(int k=j;k<=degree;k++){
      A[j][k] = 0f;
      for(int i=0;i<x.length;i++)
        A[j][k]+=intPower(x[i],(j+k));
      A[k][j] = A[j][k];
    }
    float B[] = new float[degree+1];
    for(int k=0;k<=degree;k++){
      B[k] = 0f;
      for(int i=0;i<x.length;i++)
        B[k]+=y[i]*intPower(x[i],k);
    }
    float meany = 0f;
    for(int i=0;i<x.length;i++) meany+=y[i];
    coeff[0] = meany/x.length;
    coeff = solveLinearSystem(A,B,coeff);
    return coeff;
  }


  public static float intPower(float x, int pow){
    float r = 1f;
    if(pow>0) r = x;
    if(pow>1){
      for(int i=1;i<pow;i++) r*=x;
    }
    return r;
  }
  public static float[] solveLinearSystem(float[][] A, float b[], float x[]){
    float xr[] = new float[b.length];
    SLAUSolver solv = new SLAUSolver();
    solv.dimension = b.length;
    solv.initMatrix();
    for(int j=0;j<b.length;j++)for(int k=0;k<b.length;k++){
      solv.addToMatrixElement(j,k,A[j][k]);
    }
    solv.createMatrix();
    solv.createPreconditioner();
    solv.setSolution(x);
    solv.setVector(b);
    solv.solve(0.0001,1000);
    for(int j=0;j<b.length;j++) xr[j] = (float)solv.solution[j];
    return xr;
  }

  public static float evalEvenPolynome(float x, float coeff[]){
    float r = 0f;
    for(int i=0;i<coeff.length;i++){
      r+=coeff[i]*Math.pow(x,i*2);
    }
    return r;
  }

  public static float evalPolynome(float x, float coeff[]){
    float r = 0f;
    for(int i=0;i<coeff.length;i++){
      r+=coeff[i]*Math.pow(x,i);
    }
    return r;
  }

  public static double DijkstraUndirected(Grid grid, int isource, int iend, Vector pathf){ // path is a vector of vectors of Integers (paths)
    Vector path = new Vector();
    double dist = 0;
    path.clear();
    double d[] = new double[grid.Nodes.length];
    Vector previous = new Vector(); // Vector of Vectors (previous)
    for(int i=0;i<grid.Nodes.length;i++)
      previous.add(new Vector());
    DijkstraUndirectedProcess(grid, isource, iend, d, previous);
    // now extract recursively all shortest paths
    Vector prev = (Vector)previous.get(iend);
    Vector pth = new Vector();
    pth.add(new Integer(iend));
    prolongatePath(pth,previous,path);
    //pth.add(new Integer(isource));
    path.add(pth);
    // reverse all paths (from source to end)
    for(int i=0;i<path.size();i++){
      pth = (Vector)path.get(i);
      Vector pthr = new Vector();
      for(int j=pth.size()-1;j>=0;j--){
        pthr.add(pth.get(j));
      }
      pathf.add(pthr);
    }
    return d[iend];
  }

  private static void prolongatePath(Vector pth, Vector previous, Vector paths){
    int k = ((Integer)pth.get(pth.size()-1)).intValue();
    Vector prev = (Vector)previous.get(k);
    if(prev.size()>0){
      pth.add(prev.get(0));
    for(int i=1;i<prev.size();i++){
      Vector pthb = new Vector();
      for(int j=0;j<pth.size()-1;j++)
        pthb.add(pth.get(j));
      pthb.add(prev.get(i));
      prolongatePath(pthb,previous,paths);
      paths.add(pthb);
    }
      prolongatePath(pth,previous,paths);
    }
  }

  public static void DijkstraUndirectedProcess(Grid grid, int isource, int iend, double d[], Vector previous){
    Vector neighbours = neighboursOfNodeHash(grid);

    Set S = new HashSet();
    Set Q = new HashSet();
    for(int i=0;i<grid.Nodes.length;i++){
      d[i] = Double.POSITIVE_INFINITY;
      Q.add(new Integer(i));
    }
    d[isource] = 0;
    while(Q.size()>0){
      //System.out.println(Q.size());
      Set u = extractClosestNodes(Q,d);
      S = VSimpleFunctions.UnionOfSets(S,u);
      int best = ((Integer)u.iterator().next()).intValue();
      Vector neigh = (Vector)neighbours.get(best);
      for(int i=0;i<neigh.size();i++){
         int iedge = ((Integer)neigh.get(i)).intValue();
         int v = -1;
         if(grid.Edges[iedge][0]==best)
           v = grid.Edges[iedge][1];
         else
           v = grid.Edges[iedge][0];
         double weight = 1;
         if(grid.edgeweights!=null)
           weight = grid.edgeweights[iedge];
         if(d[v]>d[best]+weight+1e-8){ // new path
           Vector prev = (Vector)previous.get(v);
           prev.clear();
           prev.add(new Integer(best));
           d[v] = d[best]+weight;
         }else
         if(Math.abs(d[v]-d[best]-weight)<1e-8){ // equivalent path
           Vector prev = (Vector)previous.get(v);
           prev.add(new Integer(best));
           d[v] = d[best]+weight;
         }
      }
    }

    /*2     for each vertex v in V[G]                        // Initializations
     3           d[v] := infinity                           // Unknown distance function from s to v
     4           previous[v] := undefined
     5     d[s] := 0                                        // Distance from s to s
     6     S := empty set                                   // Set of all visited vertices
     7     Q := V[G]                                        // Set of all unvisited vertices
     8     while Q is not an empty set                      // The algorithm itself
     9           u := Extract_Min(Q)                        // Remove best vertex from priority queue
    10           S := S union {u}                           // Mark it 'visited'
    11           for each edge (u,v) outgoing from u
    12                  if d[u] + w(u,v) < d[v]             // Relax (u,v)
    13                        d[v] := d[u] + w(u,v)
14                        previous[v] := u*/

  }

  public static Vector neighboursOfNodeHash(Grid grid){
    Vector v = new Vector(); // it is vector (of length number of nodes) of vectors of outgoing edges numbers
    for(int i=0;i<grid.Nodes.length;i++){
      Vector neigh = new Vector();
      v.add(neigh);
    }
    for(int i=0;i<grid.Edges.length;i++){
      int i1 = grid.Edges[i][0];
      int i2 = grid.Edges[i][1];
      if(i1!=i2){
      Vector vn1 = (Vector)v.get(i1);
      vn1.add(new Integer(i));
      vn1 = (Vector)v.get(i2);
      vn1.add(new Integer(i));
      }
    }
    return v;
  }

  public static Set extractClosestNodes(Set Q, double d[]){ // search for an items with minimum d[item]  and removes from Q
    // here we extract only one minimum at random!!!
    Set res = new HashSet();
    Iterator it = Q.iterator();
    double minv = Double.POSITIVE_INFINITY;
    while(it.hasNext()){
      Integer i = (Integer)it.next();
      if(d[i.intValue()]<minv)
        minv = d[i.intValue()];
    }
    it = Q.iterator();
    while(it.hasNext()){
      Integer i = (Integer)it.next();
      if(Math.abs(d[i.intValue()]-minv)<1e-8)
        { res.add(i); break; }
    }
    it = res.iterator();
    while(it.hasNext())
      Q.remove(it.next());
    return res;
  }




}