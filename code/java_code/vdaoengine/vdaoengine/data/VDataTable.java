package vdaoengine.data;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.io.LineNumberReader;
import java.io.*;
import java.util.*;
import vdaoengine.*;

public class VDataTable {

 public static int NUMERICAL = 1;
 public static int STRING = 0;

 public static int lastTableID = 0;

 public int rowCount;
 public int colCount;
 public int tableID = 0;
 public String name;

  public String stringTable[][];
  public String additionalColumns[][];
  public String fieldNames[];
  public int fieldTypes[];
  public String fieldClasses[];
  public String fieldDescriptions[];
  public String additionalColumnsNames[];
  public String fieldInfo[][] = null;

  public HashMap<String,Vector<Integer>> tableHashPrimary = null;
  public HashMap<String,Vector<Integer>> tableHashSecondary = null;


  public VDataTable() {
  name = new String();
  this.tableID = ++VDataTable.lastTableID;
  }

  public void copyHeader(VDataTable vt){
    this.fieldNames = vt.fieldNames;
    this.fieldTypes = vt.fieldTypes;
    this.fieldClasses = vt.fieldClasses;
    this.fieldDescriptions = vt.fieldDescriptions;
    this.fieldInfo = vt.fieldInfo;
    this.colCount = vt.colCount;
    this.rowCount = vt.rowCount;
  }

  public void addNewColumn(String Name, String Class, String Description, int Type, String defVal){
    colCount++;
    String memStr[][] = stringTable;
    String memNames[] = fieldNames;
    int memTypes[] = fieldTypes;
    String memClasses[] = fieldClasses;
    String memDescriptions[] = fieldDescriptions;
    String memInfo[][] = fieldInfo;

    stringTable = new String[rowCount][colCount];
    for(int i=0;i<rowCount;i++)
      for(int j=0;j<colCount;j++){
        if(j<colCount-1) stringTable[i][j] = memStr[i][j];
        else stringTable[i][j] = defVal;
      }
    fieldNames = new String[colCount];
    for(int i=0;i<colCount-1;i++) fieldNames[i] = memNames[i];
    fieldNames[colCount-1] = Name;

    fieldClasses = new String[colCount];
    for(int i=0;i<colCount-1;i++) fieldClasses[i] = memClasses[i];
    fieldClasses[colCount-1] = Class;

    fieldDescriptions = new String[colCount];
    for(int i=0;i<colCount-1;i++) fieldDescriptions[i] = memDescriptions[i];
    fieldDescriptions[colCount-1] = Description;

    fieldTypes = new int[colCount];
    for(int i=0;i<colCount-1;i++) fieldTypes[i] = memTypes[i];
    fieldTypes[colCount-1] = Type;

    fieldInfo = null;
    if((memInfo!=null)&&(memInfo.length>0)){
      int nInfo = memInfo[0].length;
      fieldInfo = new String[memInfo.length+1][nInfo];
      for(int i=0;i<colCount-1;i++)
        for(int k=0;k<nInfo;k++)
          try{
          fieldInfo[i][k] = memInfo[i][k];
          }catch(Exception e){
            System.out.println("k = "+k+" i = "+i+" fieldInfo="+fieldInfo.length+" "+fieldInfo[0].length);
          }
    }

  }

  public int fieldNumByName(String nam){
  int res=-1;
  for(int i=0;i<fieldNames.length;i++)
    if(fieldNames[i].equals(nam)) res=i;
  return res;
  }

  public int getFirstStringField(){
  int res=-1;
  for(int i=0;i<fieldNames.length;i++)
    if(fieldTypes[i]==STRING) res=i;
  return res;
  }


  public void setV(int row,int col, String val){
  stringTable[row][col]=new String(val);
  }

  public String getV(int row,int col){
  return stringTable[row][col];
  }

  public String[] getRow(int row){
  //String Res[] = new String [colCount];
  //for (int i=0; i<colCount; i++) Res[i] = new String(stringTable[i][row]);
  //return Res;
  return stringTable[row];
  }

  public String[] getRowByID(int ID){
  int num = ID-globalSettings.maximumNumberOfRows*tableID;
  return getRow(num);
  }


  public String toString() {
  StringBuffer s = new StringBuffer("");
  s.append(name+"\n");
  s.append("Rows = "+rowCount+" Columns = "+colCount+"\n");
  for(int j=0; j<colCount;j++){
  s.append(fieldNames[j]+"\t");
  }
  s.append("\n");
  for(int i=0; i<rowCount;i++)
  {
    for(int j=0; j<colCount;j++)
      {
      s.append(stringTable[i][j]+"\t");
      }
  s.append("\n");
  }
  return s.toString();
  }

  public String toString(int i) {
  StringBuffer s = new StringBuffer("");
  s.append(name+"\n");
  s.append("Rows = "+rowCount+" Columns = "+colCount+"\n");
  for(int j=0; j<colCount;j++){
  s.append(fieldNames[j]+"\t");
  }
  s.append("\n");
  return s.toString();
  }

  public Vector getIDsVector(VTableSelector sel){
  Vector res = new Vector();
  if(sel.turnedOnForRows)
  for(int i=0;i<sel.selectedRows.length;i++){
  	res.addElement(new Integer(getRowID(sel.selectedRows[i])));
  }
  return res;
  }

  public int getRowID(int i){
  return globalSettings.maximumNumberOfRows*tableID+i;
  }

  public Vector getIDbyFieldValue(String fieldName,String fieldValue){
    VTableSelector ts = new VTableSelector();
    VSelectorCriteria sc = new VSelectorCriteria(fieldName,fieldValue,VSelectorCriteria.EQUAL);
    ts.selectRowsByCriteria(this,sc);
    Vector IDs = getIDsVector(ts);
    return IDs;
  }

  public Vector getFieldClassesList(){
    Vector res = new Vector();
    for(int i=0;i<fieldClasses.length;i++){
      String s = fieldClasses[i];
      if(res.indexOf(s)<0) res.add(s);
    }
    return res;
  }

  public VDataTable transposeTable(String id){
    VDataTable vt = new VDataTable();

    int iname = this.fieldNumByName(id);
    int infoColcount = 0;
    if(this.fieldInfo!=null)
      infoColcount = this.fieldInfo[0].length;
    vt.colCount = this.rowCount+1+infoColcount;
    Vector selColumns = new Vector();
    for(int i=0;i<this.colCount;i++)
      if(this.fieldTypes[i]==VDataTable.NUMERICAL)
        selColumns.add(new Integer(i));
    vt.rowCount = selColumns.size();
    vt.fieldNames = new String[vt.colCount];
    vt.fieldTypes = new int[vt.colCount];
    vt.fieldClasses = new String[vt.colCount];
    vt.fieldDescriptions = new String[vt.colCount];
    vt.stringTable = new String[vt.rowCount][vt.colCount];
    //field names and types
    vt.fieldNames[0] = "NAME"; vt.fieldTypes[0] = VDataTable.STRING;
    for(int i=0;i<this.rowCount;i++){
        int k = i;
        vt.fieldTypes[i+1] = VDataTable.NUMERICAL;
        vt.fieldNames[i+1] = this.getV(k,iname);
        vt.fieldClasses[i+1] = "";
        vt.fieldDescriptions[i+1] = "";
    }
    for(int i=0;i<infoColcount;i++){
        int k = i;
        vt.fieldTypes[i+1+this.rowCount] = VDataTable.STRING;
        vt.fieldNames[i+1+this.rowCount] = "D"+(i+1);
        vt.fieldClasses[i+1+this.rowCount] = "";
        vt.fieldDescriptions[i+1+this.rowCount] = "";
    }
    // filling values
    for(int i=0;i<selColumns.size();i++){
      int k = ((Integer)selColumns.elementAt(i)).intValue();
      vt.stringTable[i][0] = this.fieldNames[k];
      for(int j=0;j<this.rowCount;j++){
        int kc = j;
        vt.stringTable[i][j+1] = this.getV(kc,k);
      }
      for(int j=0;j<infoColcount;j++){
        vt.stringTable[i][this.rowCount+j+1] = this.fieldInfo[k][j];
      }
    }
    return vt;
  }

  public void makePrimaryHash(String keyField){
    tableHashPrimary = new HashMap<String,Vector<Integer>>();
    for(int i=0;i<rowCount;i++){
      String label = stringTable[i][fieldNumByName(keyField)];
      Vector<Integer> vl = (Vector)tableHashPrimary.get(label);
      if(vl==null) vl = new Vector<Integer>();
      vl.add(new Integer(i));
      tableHashPrimary.put(label,vl);
    }
  }
  
  public void makePrimaryHashLowerCase(String keyField){
	    tableHashPrimary = new HashMap<String,Vector<Integer>>();
	    for(int i=0;i<rowCount;i++){
	      String label = stringTable[i][fieldNumByName(keyField)];
	      if(label!=null)
	    	  label = label.toLowerCase();
	      Vector<Integer> vl = (Vector)tableHashPrimary.get(label);
	      if(vl==null) vl = new Vector<Integer>();
	      vl.add(new Integer(i));
	      tableHashPrimary.put(label,vl);
	    }
	  }
  


}