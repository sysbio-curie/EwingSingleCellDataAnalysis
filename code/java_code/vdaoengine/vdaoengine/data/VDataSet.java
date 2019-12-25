package vdaoengine.data;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.data.*;
import vdaoengine.*;
import vdaoengine.utils.*;

import java.util.*;

public class VDataSet {

  public float massif[][];
  public int coordCount;
  public int pointCount;
  public Vector preProcess;
  public VDataTable table = null;
  public VTableSelector selector;
  VObjectDescriptorSet descriptors;
  public VStatistics simpleStatistics;
  public String name;
  public boolean hasGaps = false;

  public boolean weighted = false;
  public float weights[];
  public float weightSum = 1f;

  public VDataSet() {
  selector = new VTableSelector();
  preProcess = new Vector();
  name = new String();
  }

  public void addToPreProcess(VTablePreprocess proc) {
  preProcess.addElement(proc);
  }
  public void clearPreProcess() {
  preProcess.clear();
  }

  public void loadFromDataTable(VDataTable tab, VTableSelector sel) {
  selector = sel;
  int nc,nr;
  if (!sel.turnedOnForColumns) sel.selectAllNumericalColumns(tab);
  nc = sel.selectedColumns.length;
  if (!sel.turnedOnForRows) sel.selectAllRows(tab);
  nr = sel.selectedRows.length;
  massif = new float[nr][nc];
  int i = 0;

  for(i=0; i<nr; i++)
    for(int j=0; j<nc; j++)
       {
       //massif[i][j] = Float.parseFloat(tab.getRow(sel.selectedRows[i])[sel.selectedColumns[j]]);
       try{
       if(tab.getV(sel.selectedRows[i],sel.selectedColumns[j]).equals("@")){
         massif[i][j] = Float.NaN;
         hasGaps = true;
       }
       else
         massif[i][j] = Float.parseFloat(tab.getV(sel.selectedRows[i],sel.selectedColumns[j]));
       }
       catch (Exception e)
          {
          System.out.println("Error in loadFromDataTable(row "+i+",col "+j+"): "+tab.getV(sel.selectedRows[i],sel.selectedColumns[j])+" "+e.toString());
       }
      }
  coordCount = nc;
  pointCount = nr;
  table = tab;
  name = new String("from "+tab.name);
  }

  public void preProcessData(){
  int n = preProcess.size();
  for(int j=0;j<n;j++)
    for(int i=0;i<pointCount;i++)
      ((VTablePreprocess)preProcess.elementAt(j)).process(getVector(i));
  }

  public float[] getVector(int n){
  /*float r[] = new float[coordCount];
  for(int i=0; i<coordCount;i++)
    r[i] = massif[n][i];
  return r;*/
  return massif[n];
  }

  public float[] getVectorUnProcessed(int n){
  float v[] = new float[coordCount];
  for(int i=0;i<coordCount;i++) v[i] = massif[n][i];
  int ps = preProcess.size();
    for(int j=ps-1;j>=0;j--)
        ((VTablePreprocess)preProcess.elementAt(j)).unprocess(v);
  return v;
  }

  public void processIntoSpace(float v[]){
    int n = preProcess.size();
    for(int j=0;j<n;j++)
      ((VTablePreprocess)preProcess.elementAt(j)).process(v);
  }
  public void unprocessFromSpace(float v[]){
    int n = preProcess.size();
    for(int j=n-1;j>=0;j--)
      ((VTablePreprocess)preProcess.elementAt(j)).unprocess(v);
  }


  public int getVectorID(int n){
  int ID = 0;
  //System.out.println("Sel = "+selector.selectedRows.length+" n="+n);
  if (table!=null) ID = table.tableID*globalSettings.maximumNumberOfRows;
  if (selector!=null){
    if (!selector.turnedOnForRows) ID+=n;
    else ID+=selector.selectedRows[n];
    }
  else ID+=n;
  //System.out.println("ID = "+ID);
  return ID;
  }

  public VStatistics calcStatistics(){
  simpleStatistics = new VStatistics(coordCount);
  simpleStatistics.initialize();
  for(int i=0; i<pointCount; i++)
    simpleStatistics.addNewPoint(getVector(i));
  simpleStatistics.calculate();
  return simpleStatistics;
  }

  public String toString(){
  StringBuffer s = new StringBuffer();
  for(int i=0;i<pointCount;i++)
    {
    for(int j=0;j<coordCount;j++)
       s.append(massif[i][j]+"\t");
    s.append("\n");
    }
  return s.toString();
  }

  public int getPointCount(){
  return pointCount;
  }

  public int getCoordCount(){
  return coordCount;
  }

  public void setPointCount(int n){
  pointCount = n;
  }

  public void setCoordCount(int n){
  coordCount = n;
  }

  public VObjectDescriptorSet getDescriptors(){
  return descriptors;
  }

  public void setDescriptors(VObjectDescriptorSet ds){
  descriptors = ds;
  }


  public void standardPreprocess(){
  calcStatistics();
  VNormalization N = new VNormalization(simpleStatistics);
  addToPreProcess(N);
  preProcessData();
  }

  public void setWeights(float ws[]){
    weighted = true;
    weights = ws;
    weightSum = 0f;
    for(int i=0;i<ws.length;i++) weightSum+=weights[i];
  }

  public int getCoordNumByTableName(String fn){
    int r = -1;
    if(table!=null){
      int tn = table.fieldNumByName(fn);
      if(tn!=-1){
        for(int i=0;i<selector.selectedColumns.length;i++)
          if(selector.selectedColumns[i]==tn){
            r = i; break;
          }
      }
    }
    return r;
  }


}