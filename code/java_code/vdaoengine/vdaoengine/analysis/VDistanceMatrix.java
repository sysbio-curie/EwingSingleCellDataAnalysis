package vdaoengine.analysis;

import java.util.*;
import java.io.*;

import vdaoengine.data.*;
import vdaoengine.utils.*;

public class VDistanceMatrix {

  public float matrix[][] = null;

  public float matrixLinear[] = null; // Floats
  public PairInt pairs[] = null; // Pairs of points
  public int numberOfPoints = 0;

  public boolean calculateWholeMatrix = true;
  public boolean calculatePairs = false;

  public VDataSet dataset = null;
  public HashMap pairsMap = null;

  public Vector Neighbours = new Vector();

  public int dimension = -1;

  public void setDataset(VDataSet data){
    dataset = data;
    dimension = data.coordCount;
  }

  public void calculateMatrix(VDataSet vd){
    setDataset(vd);
    calculateMatrix();
  }

  public void calculateMatrix(){

    numberOfPoints = dataset.pointCount;
    if(!calculatePairs){

    matrix = new float[dataset.pointCount][dataset.pointCount];
    for(int i=0;i<dataset.pointCount;i++){ //System.out.print(i+"\t"); 
      for(int j=0;j<dataset.pointCount;j++){
        if(dataset.hasGaps)
          matrix[i][j] = (float)Math.sqrt(VVectorCalc.SquareEuclDistanceGap(dataset.getVector(i),dataset.getVector(j)));
        else
          matrix[i][j] = (float)VVectorCalc.Distance(dataset.getVector(i),dataset.getVector(j));
      }
    }

    }else{

      int n = (int)(0.5f*dataset.pointCount*(dataset.pointCount-1)+dataset.pointCount);
      matrixLinear = new float[n];
      pairs = new PairInt[n];
      int k = 0;
      pairsMap = new HashMap();
      for(int i=0;i<dataset.pointCount;i++){ //if(i==(int)(0.01f*(float)i)*100) System.out.print(i+"\t");
        for(int j=i;j<dataset.pointCount;j++){
          if(dataset.hasGaps)
            matrixLinear[k] = (float)Math.sqrt(VVectorCalc.SquareEuclDistanceGap(dataset.getVector(i),dataset.getVector(j)));
          else
            matrixLinear[k] = (float)VVectorCalc.Distance(dataset.getVector(i),dataset.getVector(j));
          pairs[k] = new PairInt(i,j);
          pairsMap.put(pairs[k].getId(),new Integer(k));
          k++;
        }
      }


    }
  }

  public float getDistance(int i,int j){
    if(!calculatePairs){
    if(calculateWholeMatrix)
      return matrix[i][j];
    else
      return (float)VVectorCalc.Distance(dataset.getVector(i),dataset.getVector(j));
    }else{
      int k = ((Integer)pairsMap.get((new PairInt(i,j)).getId())).intValue();
      return matrixLinear[k];
    }
  }

  public static float[][] getDataForSheppardPlot(Vector matrices){
    int n = ((VDistanceMatrix)matrices.elementAt(0)).dataset.pointCount;
    float res[][] = new float[(int)(n*(n-1)*0.5f)][matrices.size()];
    int k = 0;
    for(int i=0;i<n;i++)
      for(int j=i+1;j<n;j++){
        for(int l=0;l<matrices.size();l++)
          res[k][l] = ((VDistanceMatrix)matrices.elementAt(l)).getDistance(i,j);
        k++;
      }
    return res;
  }

  public static float[][] getDataForSheppardPlot(Vector matrices, Vector pairs){
    int n = pairs.size();
    float res[][] = new float[n][matrices.size()];
    int k = 0;
    for(int i=0;i<n;i++){
        PairInt pi = (PairInt)pairs.get(i);
        for(int l=0;l<matrices.size();l++)
          res[i][l] = ((VDistanceMatrix)matrices.elementAt(l)).getDistance(pi.i1,pi.i2);
    }
    return res;
  }


  public PairInt findBiggestDistance(){
    int ind[] = Algorithms.SortMass(matrixLinear);
    return pairs[ind[ind.length-1]];
  }

  public PairInt findBiggestDistanceToSet(Vector set){
    PairInt pi[] = new PairInt[numberOfPoints];
    float d[] = new float[numberOfPoints];
    for(int i=0;i<d.length;i++){
      pi[i] = new PairInt(-1,-1);
      d[i] = distanceToSet(i,set,pi[i]);
    }
    int ind[] = Algorithms.SortMass(d);
    return pi[ind[ind.length-1]];
  }
  
  

  public float distanceToSet(int i, Vector set, PairInt pi){
    float d = 0f;
    float ds[] = new float[set.size()];
    for(int j=0;j<ds.length;j++)
      ds[j] = getDistance(i,((Integer)set.elementAt(j)).intValue());
    int ind[] = Algorithms.SortMass(ds);
    pi.i1 = ((Integer)set.elementAt(ind[0])).intValue();
    pi.i2 = i;
    return ds[ind[0]];
  }

  public void calculateNeighbours(int numberOfNeighbours){
    Neighbours = new Vector();
    for(int i=0;i<numberOfPoints;i++){
      Neighbours.add(calculatePointNeighbours(i, numberOfNeighbours));
    }
  }

  public int[] calculatePointNeighbours(int i, int numberOfNeighbours){
    int neigh[] = new int[numberOfNeighbours];
    float dist[] = new float[numberOfPoints-1];
    int k=0;
    int indtmp[] = new int[numberOfPoints-1];
    for(int j=0;j<numberOfPoints;j++)if(i!=j){
      dist[k] = getDistance(i,j);
      indtmp[k] = j;
      k++;
    }
    int ind[] = Algorithms.SortMass(dist);
    for(int j=0;j<numberOfNeighbours;j++)
      neigh[j] = indtmp[ind[j]];
    return neigh;
  }
  
  public static VStatistics calculateNeighbourhoodPreservation(Vector dms, VDistanceMatrix mnd, int numberOfNeighbours){

    mnd.calculateNeighbours(numberOfNeighbours);
    for(int i=1;i<dms.size();i++){
      VDistanceMatrix dm = (VDistanceMatrix)dms.get(i);
      dm.calculateNeighbours(numberOfNeighbours);
    }

    VStatistics vst = new VStatistics(dms.size()+1);
    for(int j=0;j<mnd.numberOfPoints;j++){
      float f[] = new float[dms.size()+1];
      for(int i=0;i<dms.size();i++){
        VDistanceMatrix dm = (VDistanceMatrix)dms.get(i);
        int inters = VSimpleFunctions.IntersectionOfIntegerSets((int[])dm.Neighbours.get(j),(int[])mnd.Neighbours.get(j));
        f[i] = (float)(1f*inters/numberOfNeighbours);
      }
      vst.addNewPoint(f);
   }
   vst.calculate();

   // Now random permutations
   VStatistics vstr = new VStatistics(1);
   float fr[] = new float[1];
   Random r = new Random();
   for(int ii=0;ii<10000;ii++){
     int neigh[] = VSimpleFunctions.randomIntVector(numberOfNeighbours,mnd.numberOfPoints,r);
     int ir = r.nextInt(mnd.numberOfPoints);
     int inters = VSimpleFunctions.IntersectionOfIntegerSets(neigh,(int[])mnd.Neighbours.get(ir));
     fr[0] = (float)(1f*inters/numberOfNeighbours);
     vstr.addNewPoint(fr);
   }
   vstr.calculate();
   vst.means[vst.dimension-1] = vstr.means[0];
   vst.stdevs[vst.dimension-1] = vstr.stdevs[0];
   vst.mins[vst.dimension-1] = vstr.mins[0];
   vst.maxs[vst.dimension-1] = vstr.maxs[0];


   return vst;
  }

  public static VStatistics calculateClassNeighbourhood(Vector dms, VDistanceMatrix mnd, int numberOfNeighbours, VDataClass dcl){

    mnd.calculateNeighbours(numberOfNeighbours);
    for(int i=1;i<dms.size();i++){
      VDistanceMatrix dm = (VDistanceMatrix)dms.get(i);
      dm.calculateNeighbours(numberOfNeighbours);
    }

    int ik = mnd.dataset.table.fieldNumByName(dcl.fieldName);
    VStatistics vst = new VStatistics(dms.size()+1);
    for(int j=0;j<mnd.numberOfPoints;j++){
      String cln = mnd.dataset.table.stringTable[j][ik];
      float f[] = new float[dms.size()+1];
      if(cln.equals(dcl.shortname)){
        for(int i=0;i<dms.size();i++){
           int neigh[] = new int[numberOfNeighbours];
           VDistanceMatrix dm = (VDistanceMatrix)dms.get(i);
           neigh = (int[])dm.Neighbours.get(j);
           //for(int k=0;k<neigh.length;k++) System.out.print(neigh[k]+"\t"); System.out.println();
           int kcln = 0;
           for(int k=0;k<neigh.length;k++){
             String clname = mnd.dataset.table.stringTable[neigh[k]][ik];
             if(clname.equals(dcl.shortname))
               kcln++;
           }
           f[i] = (float)(1f*kcln/numberOfNeighbours);
         }
        vst.addNewPoint(f);
      }
   }
   vst.calculate();

   // Now random permutations
   VStatistics vstr = new VStatistics(1);
   float fr[] = new float[1];
   Random r = new Random();
   for(int ii=0;ii<10000;ii++){
     int neigh[] = VSimpleFunctions.randomIntVector(numberOfNeighbours,mnd.numberOfPoints,r);
     int kcln = 0;
     for(int k=0;k<neigh.length;k++){
       String clname = mnd.dataset.table.stringTable[neigh[k]][ik];
       if(clname.equals(dcl.shortname))
         kcln++;
     }
     fr[0] = (float)(1f*kcln/numberOfNeighbours);
     vstr.addNewPoint(fr);
   }
   vstr.calculate();
   vst.means[vst.dimension-1] = vstr.means[0];
   vst.stdevs[vst.dimension-1] = vstr.stdevs[0];
   vst.mins[vst.dimension-1] = vstr.mins[0];
   vst.maxs[vst.dimension-1] = vstr.maxs[0];

   return vst;
  }

  public void saveToFile(String fn) throws Exception{
    FileWriter fw = new FileWriter(fn);
    for(int i=0;i<numberOfPoints;i++){
      for(int j=0;j<numberOfPoints;j++){
        fw.write(getDistance(i,j)+"\t");
      }
      fw.write("\r\n");
    }
    fw.close();
  }


}