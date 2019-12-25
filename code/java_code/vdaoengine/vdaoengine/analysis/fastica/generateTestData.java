package vdaoengine.analysis.fastica;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import java.util.*;


public class generateTestData {

  public static void main(String[] args) {
    VDataTable vt = simulateMultiFactorDataSet(100,10,10000,150,0.1,0.05);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"multifactor.dat");
  }

  public static VDataTable simulateMultiFactorDataSet(int dimension, int numberOfFactors, int numberOfPoints, int numberOfPointsInFactors, double noiseLevel, double factorNoiseLevel){
    VDataTable vt = new VDataTable();
    vt.rowCount = numberOfPoints;
    vt.colCount = dimension+1;
    vt.stringTable = new String[vt.rowCount][vt.colCount];
    vt.fieldNames = new String[vt.colCount];
    vt.fieldClasses = new String[vt.colCount];
    vt.fieldDescriptions = new String[vt.colCount];
    vt.fieldTypes = new int[vt.colCount];
    vt.fieldNames[0] = "FACTOR";
    vt.fieldTypes[0] = vt.STRING;
    for(int i=1;i<vt.colCount;i++){
      vt.fieldNames[i] = "N"+i;
      vt.fieldTypes[i] = vt.NUMERICAL;
    }
    float factorVolume[] = new float[numberOfFactors];
    float factorStrength[] = new float[numberOfFactors];
    Random r = new Random();
    for(int i=0;i<numberOfFactors;i++){
      factorVolume[i] = r.nextFloat()*numberOfPointsInFactors/numberOfPoints;
      factorStrength[i] = (r.nextFloat()+1)/2f;
    }
    double factor[][] = new double[numberOfFactors][dimension];
    for(int i=0;i<numberOfFactors;i++){
         factor[i] = VVectorCalc.randomUnitAllSpace(dimension);
         System.out.print("FACTOR"+i);
         for(int j=0;j<dimension;j++)
           System.out.print("\t"+factor[i][j]);
         System.out.println("\tSTRENGTH\t"+factorStrength[i]+"\tVOLUME\t"+factorVolume[i]);
    }
    for(int i=0;i<numberOfPoints;i++){
      for(int j=1;j<=dimension;j++)
        vt.stringTable[i][j] = ""+(float)(r.nextGaussian()*noiseLevel);
      vt.stringTable[i][0] = "NOISE";
      int k = -1;
      for(int j=0;j<numberOfFactors;j++){
        float f = r.nextFloat();
        if(f<factorVolume[j]){ k = j; break; }
      }
      if(k>=0){
        //float amplitude = r.nextFloat();
    	float amplitude = Math.abs((float)r.nextGaussian()/2);
        for(int j=1;j<=dimension;j++){
          //vt.stringTable[i][j] = ""+(Float.parseFloat(vt.stringTable[i][j])+factor[k][j-1]*factorStrength[k]*r.nextFloat()+r.nextGaussian()*factorNoiseLevel);
          vt.stringTable[i][j] = ""+(factor[k][j-1]*factorStrength[k]*amplitude+r.nextGaussian()*factorNoiseLevel);
        }
        vt.stringTable[i][0] = "FACTOR"+k;
      }
    }
    return vt;
    }

}