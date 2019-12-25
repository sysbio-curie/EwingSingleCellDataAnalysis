package vdaoengine;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;

public class ICAAnalyzer {

  public VDataTable data = null;
  public VDataTable mixingMatrix = null; // rows - number of samples, columns - components
  public VDataTable signalMatrix = null; // rows - number of probesets, columns - components

  // question 1 - how component is related to clinical information
  //public void correlateToClinicalData(){
  //
  //}

  //question 2 -extract table accordingly to the threshold
  public VDataTable extractTable(int componentNumber, float minvalue, float maxvalue, String id){
    Vector pslist = new Vector();
    int k = data.fieldNumByName(id);
    for(int i=0;i<signalMatrix.rowCount;i++){
      float f = Float.parseFloat(signalMatrix.stringTable[i][componentNumber]);
      if((f>minvalue)&&(f<maxvalue))
        pslist.add(data.stringTable[i][k]);
    }
    VDataTable vt = VSimpleProcedures.selectRowsFromList(data,pslist,id);
    return vt;
  }

  public void classifyByICALoading(VDataTable vt, float maxThreshold, String fieldName){
    vt.addNewColumn(fieldName,"","",vt.STRING,"not_classified");
    int k = vt.fieldNumByName(fieldName);
    for(int i=0;i<signalMatrix.rowCount;i++){
      float maxf = 0f; int imax = -1;
      for(int j=0;j<signalMatrix.colCount;j++){
        float f = Float.parseFloat(signalMatrix.stringTable[i][j]);
        if(Math.abs(f)>maxf) { maxf = Math.abs(f); imax = j; }
      }
      if(maxf>maxThreshold)
        vt.stringTable[i][k] = ""+(imax+1);
    }
  }

}