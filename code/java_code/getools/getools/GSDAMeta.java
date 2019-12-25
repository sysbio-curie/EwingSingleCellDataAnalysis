package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;


public class GSDAMeta {

  String directory = "";
  VDataTable tables[] = null;
  VDataTable summary = null;
  VDataTable counting1 = null;
  VDataTable counting2 = null;
  String extention = "res1";
  String summarypath = "summary.xls";
  String countingpath = "counting.xls";

  public static void main(String[] args) {

    try{
    GSDAMeta gsm = new GSDAMeta();

    for(int i=0;i<args.length;i++){
      if(args[i].equals("-dir"))
        gsm.directory = args[i+1];
      if(args[i].equals("-ext"))
        gsm.extention = args[i+1];
      if(args[i].equals("-sum"))
        gsm.summarypath = args[i+1];
      if(args[i].equals("-count"))
        gsm.countingpath = args[i+1];

    }

    File dir = new File(gsm.directory);
    File files[] = dir.listFiles();
    Vector v = new Vector();
    for(int i=0;i<files.length;i++){
      if(files[i].getName().endsWith("."+gsm.extention)){
        v.add(files[i].getAbsolutePath());
      }
    }
    System.out.println(v.size()+" tables found.");
    gsm.tables = new VDataTable[v.size()];
    for(int i=0;i<v.size();i++){
      String fn = (String)v.get(i);
      gsm.tables[i] = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(fn,true,"\t");
    }

    gsm.makeSummary();
    gsm.countPathways();
    //System.out.println(gsm.tables[0].rowCount+"\t"+gsm.tables[0].colCount+"\t"+gsm.tables[0].stringTable[1][1]);
    vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(gsm.summary,gsm.summarypath);
    vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(gsm.counting1,gsm.countingpath);

    }catch(Exception e){
      e.printStackTrace();
    }

  }

  public void makeSummary(){
    summary = new VDataTable();
    summary.fieldClasses = new String[0];
    summary.fieldDescriptions = new String[0];
    summary.fieldNames = new String[0];
    summary.fieldTypes = new int[0];
    summary.stringTable = new String[0][0];
    int rowCount = 0;
    summary.addNewColumn("GS","","",summary.NUMERICAL,"_");
    for(int i=0;i<tables.length;i++){
      VDataTable tab = tables[i];
      File f = new File(tab.name);
      String s = f.getName(); s = s.substring(0,s.length()-extention.length()-1);
      summary.addNewColumn(s+"_pGAP1","","",summary.NUMERICAL,"NaN");
      summary.addNewColumn(s+"_GAP1","","",summary.NUMERICAL,"NaN");
      summary.addNewColumn(s+"_pGAP2","","",summary.NUMERICAL,"NaN");
      summary.addNewColumn(s+"_GAP2","","",summary.NUMERICAL,"NaN");
      rowCount = tab.rowCount;
    }
    summary.rowCount = rowCount;
    summary.stringTable = new String[summary.rowCount][summary.colCount];
    for(int i=0;i<tables.length;i++){
      VDataTable tab = tables[i];
      File f = new File(tab.name);
      String s = f.getName(); s = s.substring(0,s.length()-extention.length()-1);
      for(int j=0;j<tab.rowCount;j++){
        summary.stringTable[j][summary.fieldNumByName("GS")] = tab.stringTable[j][tab.fieldNumByName("NAME")];
        summary.stringTable[j][summary.fieldNumByName(s+"_pGAP1")] = tab.stringTable[j][tab.fieldNumByName("pGAP1")];
        summary.stringTable[j][summary.fieldNumByName(s+"_GAP1")] = tab.stringTable[j][tab.fieldNumByName("GAP1")];
        summary.stringTable[j][summary.fieldNumByName(s+"_pGAP2")] = tab.stringTable[j][tab.fieldNumByName("pGAP2")];
        summary.stringTable[j][summary.fieldNumByName(s+"_GAP2")] = tab.stringTable[j][tab.fieldNumByName("GAP2")];
      }
    }
  }

  public void countPathways(){
    Vector names = new Vector();
    for(int i=0;i<tables.length;i++){
      VDataTable tab = tables[i];
      File f = new File(tab.name);
      String s = f.getName(); s = s.substring(0,s.length()-extention.length()-1);
      names.add(s);
    }
    counting1 = new VDataTable();
    counting1.fieldClasses = new String[0];
    counting1.fieldDescriptions = new String[0];
    counting1.fieldNames = new String[0];
    counting1.fieldTypes = new int[0];
    counting1.stringTable = new String[0][0];
    counting1.addNewColumn("GS","","",counting1.STRING,"_");
    counting1.addNewColumn("p0","","",counting1.STRING,"NaN");
    counting1.addNewColumn("p01","","",counting1.STRING,"NaN");
    counting1.addNewColumn("p1","","",counting1.STRING,"NaN");
    counting1.addNewColumn("p5","","",counting1.STRING,"NaN");
    counting1.addNewColumn("p10","","",counting1.STRING,"NaN");
    counting1.addNewColumn("AVGAP","","",counting1.STRING,"NaN");
    counting1.rowCount = summary.rowCount;
    counting1.stringTable = new String[summary.rowCount][summary.colCount];
    for(int i=0;i<names.size();i++){
      String s = (String)names.get(i);
      counting1.addNewColumn(s+"_p001","","",counting1.STRING,"NaN");
      counting1.addNewColumn(s+"_p01","","",counting1.STRING,"NaN");
      counting1.addNewColumn(s+"_p1","","",counting1.STRING,"NaN");
      counting1.addNewColumn(s+"_p5","","",counting1.STRING,"NaN");
      counting1.addNewColumn(s+"_p10","","",counting1.STRING,"NaN");
    }
    for(int j=0;j<summary.rowCount;j++){
      counting1.stringTable[j][counting1.fieldNumByName("GS")] = summary.stringTable[j][summary.fieldNumByName("GS")];
      float f[] = new float[names.size()];
      float gaps[] = new float[names.size()];
      for(int i=0;i<names.size();i++){
        String s = (String)names.get(i);
        String val = summary.stringTable[j][summary.fieldNumByName(s+"_pGAP1")];
        String gap = summary.stringTable[j][summary.fieldNumByName(s+"_GAP1")];
        if(gap.equals("NaN")) gaps[i] = Float.NaN; else gaps[i] = Float.parseFloat(gap);
        if(val.equals("NaN")) f[i] = Float.NaN; else {
          f[i] = Float.parseFloat(val);
          if(f[i]<=0.0001f)
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p001")] = "1";
          else
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p001")] = "0";
          if(f[i]<=0.001f)
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p01")] = "1";
          else
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p01")] = "0";
          if(f[i]<=0.01f)
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p1")] = "1";
          else
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p1")] = "0";
          if(f[i]<=0.05f)
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p5")] = "1";
          else
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p5")] = "0";
          if(f[i]<=0.1f)
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p10")] = "1";
          else
            counting1.stringTable[j][counting1.fieldNumByName(s+"_p10")] = "0";
        }
      }
      int p0 = 0; int p01 = 0; int p1 = 0; int p5 = 0; int p10 = 0;
      for(int i=0;i<f.length;i++) if(!Float.isNaN(f[i])){
        if(f[i]<0.001f) p0++;
        if(f[i]<=0.001f) p01++;
        if(f[i]<=0.01f) p1++;
        if(f[i]<=0.05f) p5++;
        if(f[i]<=0.10f) p10++;
      }
      float sum = 0f; int count = 0;
      for(int i=0;i<gaps.length;i++) if(!Float.isNaN(gaps[i])){
        sum+=gaps[i]; count++;
      }
      counting1.stringTable[j][counting1.fieldNumByName("p0")] = ""+p0;
      counting1.stringTable[j][counting1.fieldNumByName("p01")] = ""+p01;
      counting1.stringTable[j][counting1.fieldNumByName("p1")] = ""+p1;
      counting1.stringTable[j][counting1.fieldNumByName("p5")] = ""+p5;
      counting1.stringTable[j][counting1.fieldNumByName("p10")] = ""+p10;
      counting1.stringTable[j][counting1.fieldNumByName("AVGAP")] = ""+(sum/(1f*count));
    }
  }

}