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
import vdaoengine.utils.*;
import java.util.*;

public class VTableSelector {

boolean turnedOnForRows;
boolean turnedOnForColumns;
public int selectedRows[];
public int selectedColumns[];
public HashSet selectedColumnsH = new HashSet();

  public VTableSelector() { // Selector turned off by default
  turnedOnForRows = false;
  turnedOnForColumns = false;
  selectedRows = new int[0];
  selectedColumns = new int[0];
  }

  public boolean isRowSelected(int p) { // Is row p in the table selected?
  boolean res = false;
  for(int i=0; i<selectedRows.length; i++)
   if (selectedRows[i]==p)
      {
      res = true;
      break;
      }
  return res;
  }

  public boolean isColumnSelected(int p) { // Is column p in the table selected?
  boolean res = false;
  for(int i=0; i<selectedColumns.length; i++)
   if (selectedColumns[i]==p) { res = true; break; }
  return res;
  }

  public boolean isColumnSelectedH(int p) { // Is column p in the table selected?
  boolean res = false;
  res = selectedColumnsH.contains(new Integer(p));
  return res;
  }


  public void selectRows(Vector sel){
  selectedRows = new int[sel.size()];
  for (int i=0;i<sel.size();i++)
     selectedRows[i] = ((Integer)sel.elementAt(i)).intValue();
  turnedOnForRows = true;
  }

  public void selectColumns(Vector sel){
  selectedColumns = new int[sel.size()];
  for (int i=0;i<sel.size();i++)
     selectedColumns[i] = ((Integer)sel.elementAt(i)).intValue();
  turnedOnForColumns = true;
  }

  public void selectRowsByCriteria(VDataTable table, VSelectorCriteria Criteria1, VSelectorCriteria Criteria2, boolean orcond) {
  Vector v = new Vector();
  for (int i=0;i<table.rowCount;i++)
    {
    boolean c1 = Criteria1.isSatisfy(table,table.getRow(i));
    boolean c2 = Criteria2.isSatisfy(table,table.getRow(i));
    if (orcond) c1 = c1||c2;
    else c1 = c1&&c2;
    if(c1) v.addElement(new Integer(i));
    }
  selectRows(v);
  }

  public void selectRowsByCriteria(VDataTable table, VSelectorCriteria Criteria1) {
  Vector v = new Vector();
  for (int i=0;i<table.rowCount;i++)
    {
    boolean c1 = Criteria1.isSatisfy(table,table.getRow(i));
    if(c1) v.addElement(new Integer(i));
    }
  selectRows(v);
  }

  public void selectAllNumericalColumns(VDataTable tab){
  Vector v = new Vector();
  for (int i=0;i<tab.colCount;i++)
   if(tab.fieldTypes[i]==0)
    {
    v.addElement(new Integer(i));
    }
  selectColumns(v);
  }

  public void selectAllRows(VDataTable tab){
  Vector v = new Vector();
  for (int i=0;i<tab.rowCount;i++)
    {
    v.addElement(new Integer(i));
    }
  selectRows(v);
  }

  public void selectFirstNumericalColumns(VDataTable tab,int n){
  Vector v = new Vector();
  int nc=0;
  if(n==-1) nc = -1000000;
  for (int i=0;i<tab.colCount;i++)
   if(tab.fieldTypes[i]==VDataTable.NUMERICAL)
    {
    v.addElement(new Integer(i));
    nc++;
    if (nc>=n) break;
    }
  selectColumns(v);
  }

  public float[] selectVector(String tablerow[]){
  float res[] = new float[tablerow.length];
  for (int i=0;i<tablerow.length;i++)
    res[i] = Float.parseFloat(tablerow[i]);
  return res;
  }

  public static VDataTable excludeRows(VDataTable vt, Vector IDs, String IDName){
    VDataTable tr = new VDataTable();
    tr.copyHeader(vt);
    int id = vt.fieldNumByName(IDName);
    Vector rows = new Vector();
    for(int i=0;i<vt.rowCount;i++){
      String r[] = vt.getRow(i);
      if(IDs.indexOf(r[id])<0) rows.add(r);
    }
    tr.rowCount = rows.size();
    tr.stringTable = new String[rows.size()][tr.colCount];
    for(int i=0;i<tr.rowCount;i++)
      tr.stringTable[i] = (String[])rows.elementAt(i);
    return tr;
  }

  public void hashSelectedColumns(){
    selectedColumnsH.clear();
    for(int i=0;i<selectedColumns.length;i++)
      selectedColumnsH.add(new Integer(selectedColumns[i]));
  }

}
