package vdaoengine.utils;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.lang.Math.*;
import java.util.*;
import java.io.*;

/*import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.decomposition.DenseDoubleEigenvalueDecomposition;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.tfloat.FloatMatrix2D;
import cern.colt.matrix.tfloat.algo.decomposition.DenseFloatEigenvalueDecomposition;
import cern.colt.matrix.tfloat.impl.DenseFloatMatrix2D;*/
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.CholeskyDecomposition;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import cern.colt.matrix.linalg.QRDecomposition;
import vdaoengine.data.*;
import vdaoengine.data.io.*;

public class VSimpleProcedures {

public VSimpleProcedures(){
}

public static VDataSet SimplyPreparedDatasetFromDatFile(String fn, int firstNum){
VDataTable VT = VDatReadWrite.LoadFromVDatFile(fn);
return SimplyPreparedDataset(VT,firstNum);
}

public static VDataSet SimplyPreparedDatasetFromDatString(String s, String tabName, int firstNum){
VDataTable VT = VDatReadWrite.LoadFromVDatFileAsString(s,tabName);
System.out.println("Table: "+tabName);
System.out.println("Number of rows: "+VT.rowCount);
System.out.println("Number of colums: "+VT.colCount);
return SimplyPreparedDataset(VT,firstNum);
}


public static VDataSet SimplyPreparedDatasetFromSimpleDatFile(String fn, int firstNum,boolean firstLineFNames,String delim){
VDataTable VT = VDatReadWrite.LoadFromSimpleDatFile(fn,firstLineFNames,delim);
return SimplyPreparedDataset(VT,firstNum);
}

public static VDataSet SimplyPreparedDataset(VDataTable tab, int firstNum){
VDataSet VD = new VDataSet();
VTableSelector VS = new VTableSelector();
VS.selectFirstNumericalColumns(tab,firstNum);
VD.loadFromDataTable(tab,VS);
VD.standardPreprocess();
VD.table = tab;
return VD;
}

public static VDataSet SimplyPreparedDataset(VDataTable tab, int fromColumn, int toColumn){
VDataSet VD = new VDataSet();
VTableSelector VS = new VTableSelector();
Vector<Integer> sel = new Vector<Integer>();
for(int i=fromColumn;i<=toColumn;i++) sel.add(i);
VS.selectColumns(sel);
VD.loadFromDataTable(tab,VS);
VD.standardPreprocess();
VD.table = tab;
return VD;
}


public static VDataSet SimplyPreparedDatasetWithoutNormalization(VDataTable tab, int firstNum){
VDataSet VD = new VDataSet();
VTableSelector VS = new VTableSelector();
VS.selectFirstNumericalColumns(tab,firstNum);
VD.loadFromDataTable(tab,VS);
VD.table = tab;
return VD;
}

public static VDataSet SimplyPreparedDataset(VDataTable tab, int firstNum, boolean center, boolean normalization){
VDataSet VD = new VDataSet();
VTableSelector VS = new VTableSelector();
VS.selectFirstNumericalColumns(tab,firstNum);
VD.loadFromDataTable(tab,VS);

VD.calcStatistics();
VNormalization N = new VNormalization(VD.simpleStatistics);

for(int i=0;i<VS.selectedColumns.length;i++){
  if(center&&normalization) N.setType(i,0);
  if(!center&&!normalization) N.setType(i,-1);
  if(center&&!normalization) N.setType(i,3);
  if(!center&&normalization) N.setType(i,4);
}

VD.addToPreProcess(N);
VD.preProcessData();

VD.table = tab;
return VD;
}


public static VClassifier ClassifyDataSetByField(VDataSet d, String fieldName, int mode){
if(d.getDescriptors()==null) d.setDescriptors(new VObjectDescriptorSet());
VClassifier cls = new VClassifier();
cls.classifyByTableUniqueField(d.table,d.getDescriptors(),fieldName,mode);
return cls;
}

//public static void selectPointsByCriteria(VDataSet d,VSelectorCriteria sc, VObjectDescriptor ds){
//
//}

public static void AddClassByFieldValue(VClassifier cls, VDataSet d, String fieldName, String fieldValue, VObjectDescriptor ds){
if(d.getDescriptors()==null) d.setDescriptors(new VObjectDescriptorSet());
VTableSelector ts = new VTableSelector();
VSelectorCriteria sc = new VSelectorCriteria(fieldName,fieldValue,VSelectorCriteria.EQUAL);
ts.selectRowsByCriteria(d.table,sc);
Vector IDs = d.table.getIDsVector(ts);
cls.addNewClass(IDs,fieldName+"="+fieldValue,ds,d.getDescriptors());
}

public static VDataTable MergeTables(VDataTable t1, String idField1, VDataTable t2, String idField2, String missingValue){
VDataTable rt = new VDataTable();
Vector fnames = new Vector();
for(int i=0;i<t1.colCount;i++) fnames.add(t1.fieldNames[i]);
for(int i=0;i<t2.colCount;i++){
  //System.out.println(t2.fieldNames[i]);
  if(fnames.indexOf(t2.fieldNames[i])<0)
    if(!t2.fieldNames[i].equals(idField2)) fnames.add(t2.fieldNames[i]);
}
rt.colCount = fnames.size();
rt.rowCount = t1.rowCount;
rt.stringTable = new String[rt.rowCount][rt.colCount];
rt.fieldNames = new String[fnames.size()];
for(int i=0;i<fnames.size();i++)
  rt.fieldNames[i] = (String)fnames.elementAt(i);
rt.fieldTypes = new int[rt.colCount];
rt.fieldClasses = new String[rt.colCount];
rt.fieldDescriptions = new String[rt.colCount];

if(t1.fieldInfo!=null){
  int infoColcount = 0;
  infoColcount = t1.fieldInfo[0].length;
  rt.fieldInfo = new String[rt.colCount][infoColcount];
  for(int i=0;i<fnames.size();i++){
    int fni = t1.fieldNumByName((String)fnames.elementAt(i));
    if(fni!=-1)
    for(int j=0;j<infoColcount;j++)
      rt.fieldInfo[i][j] = t1.fieldInfo[fni][j];
  }
}

for(int i=0;i<rt.fieldNames.length;i++){
  String fn = rt.fieldNames[i];
  if(t1.fieldNumByName(fn)!=-1){
    rt.fieldTypes[i] = t1.fieldTypes[i];
    if(t1.fieldClasses!=null)  rt.fieldClasses[i] = t1.fieldClasses[i];
    if(t1.fieldDescriptions!=null) rt.fieldDescriptions[i] = t1.fieldDescriptions[i];
  }else{
    int k = t2.fieldNumByName(fn);
    if(k!=-1){
      rt.fieldTypes[i] = t2.fieldTypes[k];
      if(t2.fieldClasses!=null) rt.fieldClasses[i] = t2.fieldClasses[k];
      if(t2.fieldDescriptions!=null) rt.fieldDescriptions[i] = t2.fieldDescriptions[k];
    }
  }
}
int ik1 = t1.fieldNumByName(idField1);
int ik2 = t2.fieldNumByName(idField2);
//System.out.println(idField1+":"+ik1);
//System.out.println(idField2+":"+ik2);
System.out.print("Merging : ");
for(int j=0;j<t1.rowCount;j++){
  if(Math.floor(j/1000.0)*1000==j)
     System.out.print(" "+j);
  String key = t1.stringTable[j][ik1];
  int ikey = -1;
  for(int i=0;i<t2.rowCount;i++){
    if(t2.stringTable[i][ik2].equals(key)) {ikey=i; break;}
  }
  for(int i=0;i<rt.fieldNames.length;i++){
    String fn = rt.fieldNames[i];
    if(t1.fieldNumByName(fn)!=-1){
      rt.stringTable[j][i] = t1.stringTable[j][t1.fieldNumByName(fn)];
    }else{
      int k = t2.fieldNumByName(fn);
      if((k!=-1)&&(ikey!=-1)){
        rt.stringTable[j][i] = t2.stringTable[ikey][k];
      }else{
        rt.stringTable[j][i] = missingValue;
      }
    }
  }
}
System.out.println();
return rt;
}


public static void MergeTablesUnionOfIDs(VDataTable t1, String idField1, VDataTable t2, String idField2, String outfile) throws Exception{

FileWriter fw = new FileWriter(outfile);
t1.makePrimaryHash(idField1);
t2.makePrimaryHash(idField2);

for(int i=0;i<t1.colCount;i++){
	fw.write(t1.fieldNames[i]+"\t");
} 
for(int i=0;i<t2.colCount-1;i++) if(!t2.fieldNames[i].equals(idField2)){
	fw.write(t2.fieldNames[i]+"\t");
} fw.write(t2.fieldNames[t2.colCount-1]);
fw.write("\n");

for(int j=0;j<t1.rowCount;j++){
	String key = t1.stringTable[j][t1.fieldNumByName(idField1)];
	Vector<Integer> inds = t2.tableHashPrimary.get(key);
	if(inds!=null){
		int k = inds.get(0);
		for(int i=0;i<t1.colCount;i++){
			fw.write(t1.stringTable[j][i]+"\t");
		} 
		for(int i=0;i<t2.colCount-1;i++) if(!t2.fieldNames[i].equals(idField2)){
			fw.write(t2.stringTable[k][i]+"\t");
		} fw.write(t2.stringTable[k][t2.colCount-1]);
		fw.write("\n");
	}
	if(inds==null){
		for(int i=0;i<t1.colCount;i++){
			fw.write(t1.stringTable[j][i]+"\t");
		} 
		for(int i=0;i<t2.colCount-1;i++) if(!t2.fieldNames[i].equals(idField2)){
			fw.write("_\t");
		} fw.write("_");
		fw.write("\n");
	}
}
for(int j=0;j<t2.rowCount;j++){
	String key = t2.stringTable[j][t2.fieldNumByName(idField2)];
	Vector<Integer> inds = t1.tableHashPrimary.get(key);
	if(inds==null){
		fw.write(key+"\t");
		for(int i=1;i<t1.colCount;i++){
			fw.write("_\t");
		} 
		for(int i=0;i<t2.colCount-1;i++) if(!t2.fieldNames[i].equals(idField2)){
			fw.write(t2.stringTable[j][i]+"\t");
		} fw.write(t2.stringTable[j][t2.colCount-1]);
		fw.write("\n");
	}
}

fw.close();
}


public static void MergeTablesNoMissingValues(VDataTable t1, String idField1, VDataTable t2, String idField2, String outfile) throws Exception{

FileWriter fw = new FileWriter(outfile);
t1.makePrimaryHash(idField1);
t2.makePrimaryHash(idField2);

for(int i=0;i<t1.colCount;i++){
	fw.write(t1.fieldNames[i]+"\t");
} 
for(int i=0;i<t2.colCount-1;i++) if(!t2.fieldNames[i].equals(idField2)){
	fw.write(t2.fieldNames[i]+"\t");
} fw.write(t2.fieldNames[t2.colCount-1]);
fw.write("\n");

for(int j=0;j<t1.rowCount;j++){
	String key = t1.stringTable[j][t1.fieldNumByName(idField1)];
	Vector<Integer> inds = t2.tableHashPrimary.get(key);
	if(inds!=null){
		int k = inds.get(0);
		for(int i=0;i<t1.colCount;i++){
			fw.write(t1.stringTable[j][i]+"\t");
		} 
		for(int i=0;i<t2.colCount-1;i++) if(!t2.fieldNames[i].equals(idField2)){
			fw.write(t2.stringTable[k][i]+"\t");
		} fw.write(t2.stringTable[k][t2.colCount-1]);
		fw.write("\n");
	}
}

fw.close();
}


public static void SeparateTableByField(String path, int descrNumber){
  VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(path);
  Vector val = new Vector();
  for(int i=0;i<vt.colCount;i++){
    String s = vt.fieldInfo[i][descrNumber];
    if((s!=null)&&(!s.equals("")))
      if(val.indexOf(s)<0) val.add(s);
  }
  String pref = path.substring(0,path.length()-4);
  for(int i=0;i<val.size();i++){
    VDataTable vt1 = extractClass(vt,descrNumber,(String)val.elementAt(i));
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt1,pref+"_"+(String)val.elementAt(i)+".dat");
  }
}

public static VDataTable extractClass(VDataTable vt, int descrNumber, String infoVal){
  VDataTable res = new VDataTable();
  Vector v = new Vector();
  for(int i=0;i<vt.colCount;i++){
    String s = vt.fieldInfo[i][descrNumber];
    if(s==null) s = "";
    if(s.equals(infoVal))
      v.add(new Integer(i));
    if(s.equals(""))
      v.add(new Integer(i));
  }
  res.rowCount = vt.rowCount;
  res.colCount = v.size();
  res.stringTable = new String[res.rowCount][res.colCount];
  res.fieldClasses = new String[res.colCount];
  res.fieldDescriptions = new String[res.colCount];
  res.fieldNames = new String[res.colCount];
  res.fieldTypes = new int[res.colCount];
  res.fieldInfo = new String[res.colCount][vt.fieldInfo[0].length];
  for(int i=0;i<res.colCount;i++){
    int k = ((Integer)v.elementAt(i)).intValue();
    res.fieldClasses[i] = vt.fieldClasses[k];
    res.fieldNames[i] = vt.fieldNames[k];
    res.fieldDescriptions[i] = vt.fieldDescriptions[k];
    res.fieldTypes[i] = vt.fieldTypes[k];
    for(int j=0;j<vt.fieldInfo[0].length;j++)
      res.fieldInfo[i][j] = vt.fieldInfo[k][j];
    for(int j=0;j<res.rowCount;j++)
      res.stringTable[j][i] = vt.stringTable[j][k];
  }
  return res;
}

public static VDataTable SelectColumns(VDataTable vt, Vector<String> columns){
  VDataTable res = new VDataTable();
  Vector v = new Vector<String>();
  for(String s: columns)if(vt.fieldNumByName(s)>=0) v.add(s);
  res.rowCount = vt.rowCount;
  res.colCount = v.size();
  res.stringTable = new String[res.rowCount][res.colCount];
  res.fieldClasses = new String[res.colCount];
  res.fieldDescriptions = new String[res.colCount];
  res.fieldNames = new String[res.colCount];
  res.fieldTypes = new int[res.colCount];
  if(vt.fieldInfo!=null)
  if(vt.fieldInfo.length>0)
    res.fieldInfo = new String[res.colCount][vt.fieldInfo[0].length];
  for(int i=0;i<res.colCount;i++){
    String fn = (String)v.elementAt(i);
    int k = vt.fieldNumByName(fn);
    res.fieldClasses[i] = vt.fieldClasses[k];
    res.fieldNames[i] = vt.fieldNames[k];
    res.fieldDescriptions[i] = vt.fieldDescriptions[k];
    res.fieldTypes[i] = vt.fieldTypes[k];
    if(vt.fieldInfo!=null)
    if(vt.fieldInfo.length>0)
    for(int j=0;j<vt.fieldInfo[0].length;j++)
      res.fieldInfo[i][j] = vt.fieldInfo[k][j];
    for(int j=0;j<res.rowCount;j++)
      res.stringTable[j][i] = vt.stringTable[j][k];
  }
  return res;
}


  public static VDataTable filterMissingValues(VDataTable vt, float thresh){
    VDataTable vtr = new VDataTable();
    vtr.copyHeader(vt);
    Vector v = new Vector();
    for(int i=0;i<vt.rowCount;i++){
      String vs[] = vt.getRow(i);
      int allnumeric = 0;
      int missing = 0;
      for(int j=0;j<vs.length;j++){
        if(vt.fieldTypes[j]==vt.NUMERICAL){
          allnumeric++;
          if(vs[j].equals("NULL")||vs[j].equals("@")||vs[j].equals("NA")||vs[j].equals("N/A")||vs[j].toLowerCase().equals("nan"))
            missing++;
        }
      }
      //if(missing>0)
      //	  System.out.println("Line "+i+"\t"+missing+"\t"+allnumeric+"\t"+((float)missing/(float)(allnumeric)<thresh));
      //System.out.println(((float)missing/(float)(allnumeric)<thresh)+"\t"+allnumeric+"\t"+thresh);
      if((float)missing/(float)(allnumeric)<thresh)
        v.add(vs);
    }
    //System.out.println("Size="+v.size());
    vtr.rowCount = v.size();
    vtr.colCount = vt.colCount;
    vtr.stringTable = new String[vt.rowCount][vt.colCount];
    for(int i=0;i<v.size();i++)
      vtr.stringTable[i] = (String[])v.elementAt(i);
    return vtr;
  }
  
  public static VDataTable filterMissingValues(VDataTable vt, float thresh, Vector<String> fields){
	    VDataTable vtr = new VDataTable();
	    vtr.copyHeader(vt);
	    Vector v = new Vector();
	    for(int i=0;i<vt.rowCount;i++){
	      String vs[] = vt.getRow(i);
	      int allnumeric = 0;
	      int missing = 0;
	      for(int j=0;j<vs.length;j++){
	        if(vt.fieldTypes[j]==vt.NUMERICAL)if(fields.indexOf(vt.fieldNames[j])>=0){
	          allnumeric++;
	          if(vs[j].equals("NULL")||vs[j].equals("@"))
	            missing++;
	        }
	      }
	      if((float)missing/(float)(allnumeric)<thresh)
	        v.add(vs);
	    }
	    vtr.rowCount = v.size();
	    vtr.colCount = vt.colCount;
	    vtr.stringTable = new String[vt.rowCount][vt.colCount];
	    for(int i=0;i<v.size();i++)
	      vtr.stringTable[i] = (String[])v.elementAt(i);
	    return vtr;
	  }
  


  public static VDataTable selectRowsFromList(VDataTable vt, Vector ids, String idfield){
    Vector rows = new Vector();
    int k = vt.fieldNumByName(idfield);
    int found = 0;
    Vector found_ids = new Vector();
    for(int i=0;i<vt.rowCount;i++){
      String id = vt.stringTable[i][k];
      String r[] = null;
      if(ids.indexOf(id)>=0){
        r = vt.stringTable[i];
        rows.add(r);
        if(found_ids.indexOf(id)<0)
          found_ids.add(id);
        found++;
      }
    }
    VDataTable res = new VDataTable();
    res.copyHeader(vt);
    res.rowCount = rows.size();
    res.stringTable = new String[res.rowCount][res.colCount];
    for(int i=0;i<rows.size();i++){
      res.stringTable[i] = (String[])rows.elementAt(i);
    }
    System.out.println(found+" from "+ids.size()+" found ("+found_ids.size()+" unique)");
    return res;
  }
  
  public static VDataTable selectRows(VDataTable vt, Vector lineNumbers){
	    Vector rows = new Vector();
	    int found = 0;
	    Vector found_ids = new Vector();
	    for(int i=0;i<vt.rowCount;i++){
	      String r[] = null;
	      if(lineNumbers.indexOf(new Integer(i))>=0){
	        r = vt.stringTable[i];
	        rows.add(r);
	        found++;
	      }
	    }
	    VDataTable res = new VDataTable();
	    res.copyHeader(vt);
	    res.rowCount = rows.size();
	    res.stringTable = new String[res.rowCount][res.colCount];
	    for(int i=0;i<rows.size();i++){
	      res.stringTable[i] = (String[])rows.elementAt(i);
	    }
	    //System.out.println(found+" from "+ids.size()+" found ("+found_ids.size()+" unique)");
	    return res;
	  }
  
  public static VDataSet selectRows(VDataSet vd, Vector lineNumbers){
	  
	  	VDataSet vdn = new VDataSet();
	  	vdn.coordCount = vd.coordCount;
	  	vdn.pointCount = lineNumbers.size();
	  	vdn.massif = new float[lineNumbers.size()][vd.coordCount];
	  	
	  	for(int i=0;i<lineNumbers.size();i++){
	  		Integer I = (Integer)lineNumbers.get(i);
	  		vdn.massif[i] = vd.getVector(I.intValue());
	  	}
	  
	    return vdn;
	  }
  
  public static VDataTable selectRowsFromList(VDataTable vt, Vector ids){
	  return selectRowsFromList(vt,ids,true);
  }

  public static VDataTable selectRowsFromList(VDataTable vt, Vector ids, boolean verbose){
    Vector rows = new Vector();
    int found = 0;
    Vector found_ids = new Vector();
    if(vt.tableHashPrimary!=null)
    for(int i=0;i<ids.size();i++){
      String id = (String)ids.get(i);
      Vector rs = (Vector)vt.tableHashPrimary.get(id);
      if(rs!=null){
        for(int j=0;j<rs.size();j++){
          rows.add(rs.get(j));
          found++;
          if(found_ids.indexOf(id)<0)
            found_ids.add(id);
        }
      }
    }
    VDataTable res = new VDataTable();
    res.copyHeader(vt);
    res.rowCount = rows.size();
    res.stringTable = new String[res.rowCount][res.colCount];
    for(int i=0;i<rows.size();i++){
      //res.stringTable[i] = (String[])rows.elementAt(i);
      res.stringTable[i] = vt.stringTable[((Integer)rows.get(i)).intValue()];
    }
    if(verbose)
    	System.out.println(found+" from "+ids.size()+" found ("+found_ids.size()+" unique)");
    return res;
  }


  public static void LogTransform(VDataTable vt){
    for(int i=0;i<vt.rowCount;i++){
      String v[] = vt.getRow(i);
      for(int j=0;j<v.length;j++){
        if(vt.fieldTypes[j]==vt.NUMERICAL)
          if((!v[j].equals("NULL"))&&(!v[j].equals("@")))
            {
              float f = Float.parseFloat(v[j]);
              f = (float)Math.log(f);
              v[j] = ""+f;
            }
      }
    }
  }

  public static VDataTable normalizeVDat(VDataTable vt, boolean centr, boolean norm){

    VDataTable vtr = new VDataTable();
    vtr.copyHeader(vt);
    vtr.rowCount = vt.rowCount;
    vtr.colCount = vt.colCount;
    vtr.stringTable = new String[vt.rowCount][vt.colCount];

    boolean num[] = new boolean[vt.colCount];
    int numfields = 0;
    for(int i=0;i<vt.colCount;i++){
      if(vt.fieldTypes[i]==vt.NUMERICAL) {numfields++; num[i]=true; }
      else num[i]=false;
    }

    VStatistics vs = new VStatistics(vt.rowCount);
    for(int j=0;j<vt.colCount;j++){
      float f[] = new float[vt.rowCount];
      if(num[j]){
        for(int i=0;i<vt.rowCount;i++)
           f[i] = Float.parseFloat(vt.stringTable[i][j]);
        vs.addNewPoint(f);
      }
    }
    vs.calculate();

    for(int i=0;i<vt.rowCount;i++){
      float f[] = new float[numfields];
      int k=0;
      for(int j=0;j<vt.colCount;j++){
        if(num[j]){
        float x = Float.parseFloat(vt.stringTable[i][j]);
        //f[j-1] = (x-vs.getMean(i))/vs.getStdDev(i);
        if(centr) f[k] = (x-vs.getMean(i));
        else
          f[k] = x/10;
        if(norm)
          f[k] = (x-vs.getMean(i))/vs.getStdDev(i);
        k++;
        }
      }
      k = 0;
      for(int j=0;j<vt.colCount;j++){
        if(vt.fieldTypes[j]==vt.NUMERICAL) { vtr.stringTable[i][j] = ""+f[k]; k++; }
        else vtr.stringTable[i][j] = vt.stringTable[i][j];
      }
    }

    return vtr;
  }

  public static float parseFloat(String s){
    float fr = 0f;
    if((s.equals("NULL"))&&(s.equals("@")))
      fr = Float.parseFloat(s);
    else
      fr = Float.NaN;
    return fr;
  }

  public static VDataTable substituteRowsByStatistics(VDataTable vt, String classField, int statType){ // 0 - mean, 1 - stdv, 2 - min, 3 - max, 4 - median
    VDataTable vres = new VDataTable();
    Vector classes = new Vector();
    for(int i=0;i<vt.rowCount;i++){
      String cl = vt.stringTable[i][vt.fieldNumByName(classField)];
      if(classes.indexOf(cl)<0)
        classes.add(cl);
    }

    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    Vector stats = new Vector();
    for(int i=0;i<classes.size();i++){
      VStatistics vst = new VStatistics(vd.coordCount);
      stats.add(vst);
    }
    for(int i=0;i<vd.pointCount;i++){
      float f[] = vd.getVector(i);
      String cl = vt.stringTable[i][vt.fieldNumByName(classField)];
      int k = classes.indexOf(cl);
      VStatistics vs = (VStatistics)stats.elementAt(k);
      vs.addNewPoint(f);
    }
    for(int i=0;i<stats.size();i++){
      VStatistics vs = (VStatistics)stats.elementAt(i);
      vs.calculate();
    }

    vres.copyHeader(vt);
    vres.rowCount = classes.size();
    vres.stringTable = new String[vres.rowCount][vres.colCount];
    String pref = "";
    if(statType==0) pref = "MEAN";
    if(statType==1) pref = "STV";
    if(statType==2) pref = "MIN";
    if(statType==3) pref = "MAX";
    for(int i=0;i<vres.rowCount;i++){
      vres.stringTable[i][vres.fieldNumByName(classField)] = (String)classes.elementAt(i);
      VStatistics vs = (VStatistics)stats.elementAt(i);
      for(int j=0;j<vd.selector.selectedColumns.length;j++){
        float num = 0f;
        if(statType==0) num = vs.getMean(j);
        if(statType==1) num = vs.getStdDev(j);
        if(statType==2) num = vs.getMin(j);
        if(statType==3) num = vs.getMax(j);
        if(statType==4) num = vs.getMedian(j);
        vres.stringTable[i][vd.selector.selectedColumns[j]] = ""+num;
      }
    }

    return vres;
  }

  public static VDataTable substituteRowsByTheMostVariable(VDataTable vt, String classField){
    VDataTable vres = new VDataTable();
    Vector classes = new Vector();
    for(int i=0;i<vt.rowCount;i++){
      String cl = vt.stringTable[i][vt.fieldNumByName(classField)];
      if(classes.indexOf(cl)<0)
        classes.add(cl);
    }
    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    float vars[] = new float[vd.pointCount];
    for(int i=0;i<vd.pointCount;i++){
      float vec[] = vd.getVector(i);
      vars[i] = VSimpleFunctions.calcStandardDeviation(vec);
    }
    int inds[] = new int[classes.size()];
    float maxvar[] = new float[classes.size()];
    for(int i=0;i<inds.length;i++) { inds[i] = -1; maxvar[i] = -1f; }
    for(int i=0;i<vd.pointCount;i++){
      String cl = vt.stringTable[i][vt.fieldNumByName(classField)];
      int k = classes.indexOf(cl);
      if(inds[k]==-1) { inds[k] = i; maxvar[k] = vars[i]; }
      if(vars[i]>maxvar[k]) { inds[k] = i; maxvar[k] = vars[i]; }
    }

    vres.copyHeader(vt);
    vres.rowCount = classes.size();
    vres.stringTable = new String[vres.rowCount][vres.colCount];
    String pref = "";
    for(int i=0;i<vres.rowCount;i++){
      vres.stringTable[i][vres.fieldNumByName(classField)] = (String)classes.elementAt(i);
      for(int j=0;j<vt.colCount;j++){
        if(vt.fieldTypes[j]==vt.STRING)
          vres.stringTable[i][j] = vt.stringTable[inds[i]][j];
      }
      for(int j=0;j<vd.selector.selectedColumns.length;j++){
        float num = 0f;
        vres.stringTable[i][vd.selector.selectedColumns[j]] = vt.stringTable[inds[i]][vd.selector.selectedColumns[j]];
      }
    }

    return vres;
  }
  
  public static VDataTable removeColumns(VDataTable vt, Vector<String> columnsExclude){
		VDataTable tabr = new VDataTable(); 
		for(int i=0;i<vt.colCount;i++){
			String fn = vt.fieldNames[i];
			if(columnsExclude.indexOf(fn)<0){
				tabr.addNewColumn(fn, "", "", vt.fieldTypes[i], "_");
			}
		}
		if(vt.fieldInfo!=null)
			tabr.fieldInfo = new String[tabr.colCount][vt.fieldInfo[0].length];
		tabr.rowCount = vt.rowCount;
		tabr.stringTable = new String[tabr.rowCount][tabr.colCount];
		for(int i=0;i<vt.colCount;i++){
			String fn = vt.fieldNames[i];
			if(columnsExclude.indexOf(fn)<0){
				if(vt.fieldInfo!=null){
					tabr.fieldInfo[tabr.fieldNumByName(fn)] = vt.fieldInfo[i];
				}
				for(int j=0;j<vt.rowCount;j++)
					tabr.stringTable[j][tabr.fieldNumByName(fn)] = vt.stringTable[j][i];
			}
		}
		return tabr;
  }
  
  public static VDataTable removeColumnNumbers(VDataTable vt, Vector<Integer> columnsExcludeI){
		VDataTable tabr = new VDataTable(); 
		for(int i=0;i<vt.colCount;i++){
			String fn = vt.fieldNames[i];
			if(columnsExcludeI.indexOf(i)<0){
				tabr.addNewColumn(fn, "", "", vt.fieldTypes[i], "_");
			}
		}
		if(vt.fieldInfo!=null)
			tabr.fieldInfo = new String[tabr.colCount][vt.fieldInfo[0].length];
		tabr.rowCount = vt.rowCount;
		tabr.stringTable = new String[tabr.rowCount][tabr.colCount];
		for(int i=0;i<vt.colCount;i++){
			String fn = vt.fieldNames[i];
			if(columnsExcludeI.indexOf(i)<0){
				if(vt.fieldInfo!=null){
					tabr.fieldInfo[tabr.fieldNumByName(fn)] = vt.fieldInfo[i];
				}
				for(int j=0;j<vt.rowCount;j++)
					tabr.stringTable[j][tabr.fieldNumByName(fn)] = vt.stringTable[j][i];
			}
		}
		return tabr;
  }
  
  
  public static VDataSet randomizeDataset(VDataSet vd, int numberPermutations){
	  VDataSet res = new VDataSet();
	  res.coordCount = vd.coordCount;
	  res.pointCount = vd.pointCount;
	  res.table = vd.table;
	  res.selector = vd.selector;
	  res.weighted = vd.weighted;
	  res.weights = vd.weights;
	  res.massif = new float[res.pointCount][res.coordCount];
	  for(int i=0;i<res.pointCount;i++){
		  for(int j=0;j<res.coordCount;j++)
			  res.massif[i][j] = vd.massif[i][j];
	  }
	  Random r = new Random();
	  for(int i=0;i<numberPermutations;i++){
		  int point1 = r.nextInt(res.pointCount);
		  int coord1 = r.nextInt(res.coordCount);
		  int point2 = r.nextInt(res.pointCount);
		  int coord2 = r.nextInt(res.coordCount);
		  float temp = res.massif[point1][coord1];
		  res.massif[point1][coord1] = res.massif[point2][coord2];
		  res.massif[point2][coord2] = temp;
	  }
	  return res;
  }
  
  public static VDataTable centerTableRows(VDataTable vt, boolean addColumnWithAverage, boolean addColumnWithStdVar){
	  int numNumerical = 0;
	  for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL) numNumerical++;
	  float f[] = new float[numNumerical];
	  int colCount = vt.colCount;
	  if(addColumnWithAverage)
		  vt.addNewColumn("AVERAGE", "", "", vt.NUMERICAL, "0");
	  if(addColumnWithStdVar)
		  vt.addNewColumn("STDEV", "", "", vt.NUMERICAL, "0");
	  for(int j=0;j<vt.rowCount;j++){
	  int k=0;
	  for(int i=0;i<colCount;i++){
		  if(vt.fieldTypes[i]==vt.NUMERICAL){
			  f[k++] = Float.parseFloat(vt.stringTable[j][i]);
		  }
	  }
	  float mn = VSimpleFunctions.calcMean(f);
	  float stdev = VSimpleFunctions.calcStandardDeviation(f);
	  for(int i=0;i<colCount;i++){
		  if(vt.fieldTypes[i]==vt.NUMERICAL){
			  vt.stringTable[j][i] = ""+(Float.parseFloat(vt.stringTable[j][i])-mn);
			  if(addColumnWithAverage)
				  vt.stringTable[j][vt.fieldNumByName("AVERAGE")] = ""+mn;
			  if(addColumnWithStdVar)
				  vt.stringTable[j][vt.fieldNumByName("STDEV")] = ""+stdev;
		  }
	  }
	  }
	  return vt;
  }
  
  public static double[][] CalcEigenVectors(double matrix[][], Vector<Double> eigenValues){
	  double[][] res = new double[matrix.length][matrix[0].length];
	  DoubleMatrix2D mat = new DenseDoubleMatrix2D(res.length,res[0].length);
	  //DenseFloatMatrix2D mat = new DenseFloatMatrix2D(res.length,res[0].length);
	  for(int i=0;i<matrix.length;i++){
		  for(int j=0;j<matrix[0].length;j++){
			  if(Math.abs(matrix[i][j])>1e-10)
				  mat.set(i, j, (float)matrix[i][j]);
		  }
	  }
	  /*DenseFloatEigenvalueDecomposition dd = new DenseFloatEigenvalueDecomposition(mat);
	  FloatMatrix2D v = dd.getV();
	  FloatMatrix2D d = dd.getD();*/
	  EigenvalueDecomposition dd = new EigenvalueDecomposition(mat);
	  DoubleMatrix2D v = dd.getV();
	  DoubleMatrix2D d = dd.getD();
	  for(int i=0;i<matrix.length;i++)
		  eigenValues.add((double)d.get(i, i));
	  for(int i=0;i<matrix.length;i++){
		  for(int j=0;j<matrix[0].length;j++){
			  res[i][j] = v.get(i, j);
		  }
	  }
	  return res;
  }
  

  /*public static void CalcCholeskyDecomposition(double matrix[][]){
	  DenseDoubleMatrix2D mat = new DenseDoubleMatrix2D(matrix.length,matrix.length);
	  for(int i=0;i<matrix.length;i++){
		  for(int j=0;j<matrix[0].length;j++){
			  if(Math.abs(matrix[i][j])>1e-10)
				  mat.set(i, j, matrix[i][j]);
		  }
	  }
	  
	  CholeskyDecomposition dd = new CholeskyDecomposition(mat);
	  System.out.println(dd.isSymmetricPositiveDefinite());
  }
  
  public static void CalcQRDecomposition(double matrix[][]){
	  DenseDoubleMatrix2D mat = new DenseDoubleMatrix2D(matrix.length,matrix.length);
	  for(int i=0;i<matrix.length;i++){
		  for(int j=0;j<matrix[0].length;j++){
			  if(Math.abs(matrix[i][j])>1e-10)
				  mat.set(i, j, matrix[i][j]);
		  }
	  }
	  
	  QRDecomposition dd = new QRDecomposition(mat);
	  System.out.println(dd.hasFullRank());
  }*/

	public static void findAllNumericalColumns(VDataTable vt){
		for(int i=0;i<vt.colCount;i++){
			boolean isNumerical = true;
			for(int j=0;j<vt.rowCount;j++){
				try{
				String s = vt.stringTable[j][i];
				if(s!=null)
					s = s.trim();
				else
					s = "N/A";
				if(s.equals("\"\"")||s.equals("NA")||s.equals("")||s.equals("_")||s.equals("N/A")){
					s = "@";
					vt.stringTable[j][i] = s;
				}else{
				try{
					Float f = Float.parseFloat(s);
				}catch(Exception e){
					//e.printStackTrace();
					isNumerical = false;
				}}
				}catch(Exception e){
					System.out.println("Value = null, Row "+j+", Col "+i);
					e.printStackTrace();
				}
			}
			if(isNumerical)
				vt.fieldTypes[i] = vt.NUMERICAL;
		}
	}
	


}