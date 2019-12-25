package vdaoengine.data.io;

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
import java.text.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;

public class VDatReadWrite {

  public static boolean useQuotesEverywhere = true;
  public static int numberOfDigitsToKeep = -1;
  public static boolean writeNumberOfColumnsRows = false;
  public static boolean writeHeader = true;

  public static VDataTable LoadFromVDatFileAsString(String file, String tabName){
    VDataTable Tabl = null;
    try{
    Tabl = LoadFromVDatReader(new StringReader(file),tabName);
    }catch(Exception e) { System.out.println("Error in VDatReadWrite: "+e.toString());}
    return Tabl;
  }

  public static VDataTable LoadFromVDatFile(String FileName){
    VDataTable Tabl = null;
    try{
    Tabl = LoadFromVDatReader(new FileReader(FileName),FileName);
    }catch (Exception e) { System.out.println("Error in VDatReadWrite: "+e.toString());}
    return Tabl;
  }

  public static VDataTable LoadFromSimpleDatFile(Reader reader, boolean firstLineFNames, String delim, int mark){

  VDataTable vt = new VDataTable();
	  
  String s = null;
  try{
  LineNumberReader lri = new LineNumberReader(reader);
  
  lri.mark(mark);
  //lri.mark(1);
  s = lri.readLine();
  StringTokenizer sti = new StringTokenizer(s,delim);
  vt.colCount = sti.countTokens();
  vt.fieldNames = new String[vt.colCount];
  vt.fieldTypes = new int[vt.colCount];
  if(!firstLineFNames){
    for(int i=0;i<vt.colCount;i++) vt.fieldNames[i] = "N"+(i+1);
  }else{
    int k=0;
    while(sti.hasMoreTokens()){
    	vt.fieldNames[k] = cutQuotes(sti.nextToken());
      k++;
    }
  }
  int cr = 0;
  
  
  while(lri.readLine()!= null) cr++;
  if(!firstLineFNames) cr++;
  vt.rowCount = cr;
  //System.out.println("Rows number = "+cr);
  //lri.close(); 
  lri.reset();
  //lri.mark(0);
  //lri = new LineNumberReader(reader);

  vt.stringTable = new String[vt.rowCount][vt.colCount];
  //LineNumberReader lr = new LineNumberReader(reader);
  if(firstLineFNames){
    lri.readLine();
    //rowCount = rowCount-1;
  }
  
  int i=0;
  
  while ( ((s=lri.readLine()) != null)&&(i<=vt.rowCount) )
     {
	 //System.out.println(s);
     PowerfulTokenizer st = new PowerfulTokenizer(s,delim);
     int j=0;
     while ((st.hasMoreTokens())&&(j<vt.colCount)) {
        String ss = st.nextToken();
        if (ss.length()>1)
        if (ss.charAt(0)=='\"')
           ss = new String(ss.substring(1,ss.length()-1));
        vt.stringTable[i][j] = ss;
        j++;
        }
     i++;
     }
  
  
  }
  catch (Exception e) { 
	  System.out.println("Error in VDatReadWrite: "+e.toString()+"\n"+s);
	  return null;
	  }
  return vt;
  
  }
  
  
  public static VDataTable LoadFromVDatReader(Reader rr, String tabName) throws Exception{
  VDataTable Tabl = new VDataTable();

  LineNumberReader lr = new LineNumberReader(rr);
  String s;
  s=lr.readLine();
  StringTokenizer st;
  st = new StringTokenizer(s," \t");
  //String s1 = st.nextToken(); System.out.println(s1);
  Tabl.colCount = Integer.parseInt(st.nextToken());
  //s1 = st.nextToken(); System.out.println(s1);
  Tabl.rowCount = Integer.parseInt(st.nextToken());
  Tabl.stringTable = new String[Tabl.rowCount][Tabl.colCount];
  Tabl.fieldNames = new String[Tabl.colCount];
  Tabl.fieldTypes = new int[Tabl.colCount];
  Tabl.fieldClasses = new String[Tabl.colCount];
  Tabl.fieldDescriptions = new String[Tabl.colCount];

  Vector Info = new Vector();

  for (int i=0;i<Tabl.colCount;i++)
     {
     Vector Infoi = new Vector();
     s=lr.readLine();
     st = new StringTokenizer(s," \t");
     Tabl.fieldNames[i] = new String(st.nextToken());
     String ftype = st.nextToken();
     if ((ftype.equalsIgnoreCase("FLOAT"))||(ftype.equalsIgnoreCase("INTEGER")))
        Tabl.fieldTypes[i] = VDataTable.NUMERICAL; // Number
     else
        Tabl.fieldTypes[i] = VDataTable.STRING; // String
     if(st.hasMoreTokens()){
       String sn = st.nextToken();
       Infoi.add(sn);
       Tabl.fieldClasses[i] = sn;
     }else{
       Tabl.fieldClasses[i] = "ALL";
     }
     if(st.hasMoreTokens()){
       String sn = st.nextToken();
       Infoi.add(sn);
       Tabl.fieldDescriptions[i] = sn;
     }else{
       Tabl.fieldDescriptions[i] = "no_description";
     }
     while(st.hasMoreTokens()) Infoi.add(st.nextToken());
     Info.add(Infoi);
     }

  int infoColCount = 0;
  for(int i=0;i<Info.size();i++){
    Vector vi = (Vector)Info.elementAt(i);
    if(vi.size()>infoColCount)
      infoColCount = vi.size();
  }
  if(infoColCount>0)
    Tabl.fieldInfo = new String[Tabl.colCount][infoColCount];
  for(int i=0;i<Info.size();i++){
    Vector vi = (Vector)Info.elementAt(i);
    for(int k=0;k<vi.size();k++)
      Tabl.fieldInfo[i][k] = (String)vi.elementAt(k);
  }

  int i=0;
  int ireal = 0;
  Vector v = new Vector();

  int count = 0;
  StringBuffer ss = new StringBuffer();
  String ss1 = "";
  /*for(int k=0;k<Tabl.colCount;k++)
    ss1 += " 3.000";
  st = new StringTokenizer(ss1," \t");*/
  while ( ((s=lr.readLine()) != null)&&(i<Tabl.rowCount) )
     {
     count++;
     //if(count==(int)(0.001f*count)*1000)
     //  printUsedMemory();
     st = new StringTokenizer(s," \t");
     int j=0;
     v.clear();
     if(st.countTokens()!=Tabl.colCount){
       System.out.println("Row "+(ireal)+": wrong number of columns (see below)");
       System.out.println(s.substring(0,50)+" ... "+s.substring(s.length()-50,s.length()));
     }else{
     while ((st.hasMoreTokens())&&(j<Tabl.colCount)) {
        /*ss = new StringBuffer(st.nextToken());
        if (ss.length()>1){
          if (ss.charAt(0)=='\"')
           v.add(ss.substring(1,ss.length()-1)); //new String(ss.substring(1,ss.length()-1));
        else
          v.add(ss.toString());
        }else v.add(ss.toString());*/
            //v.add(st.nextToken());
            String s1 = st.nextToken();
            if(s1.length()>1)
                    if (s1.charAt(0)=='\"')
                            s1 = s1.substring(1,s1.length()-1);
            Tabl.stringTable[i][j] = s1;
        j++;
        }
     /*if(v.size()!=Tabl.colCount){
       System.out.println("Row "+(ireal)+": wrong number of columns (see below)");
       System.out.println(s.substring(0,50)+" ... "+s.substring(s.length()-50,s.length()));
     }else{
       for(int k=0;k<v.size();k++)
         Tabl.stringTable[i][k] = (String)v.get(k);
       i++;
     }
     */
     ireal++;
     }
     i++;
     }
  Tabl.rowCount = i;

/*  ScanWord stz = new ScanWord(lr);
  stz.quoteChar('\"');
  stz.eolIsSignificant(true);
  int i=0;
  int j=0;
  while ( (stz.nextWord()!=stz.TT_EOF)&&(i<Tabl.rowCount) )
     {
     if((stz.ttype==stz.TT_WORD)||(stz.ttype=='\"'))
        {
        Tabl.stringTable[i][j] = stz.sval;
        j++;
        }
     if(stz.ttype==stz.TT_NUMBER)
        {
        StringBuffer sb = new StringBuffer();
        sb.append(stz.nval);
        Tabl.stringTable[i][j] = new String(sb);
        //Tabl.stringTable[i][j] = stz.toString();
        j++;
        }
     if (stz.ttype==stz.TT_EOL){
        i++; j=0;
        }
     }
  }*/

  Tabl.tableID = VDataTable.lastTableID+1;
  VDataTable.lastTableID++;
  Tabl.name = new String(tabName);
  return Tabl;
  }

  public static int[][] LoadIntegerMassifTabDelimited(String FileName){ 
  int massif[][] = null; 
  String s = null;
  int i = 0;  
  try{
  LineNumberReader lri = new LineNumberReader(new FileReader(FileName));
  s = lri.readLine();
  String delim = "\t";
  StringTokenizer sti = new StringTokenizer(s,delim);
  //System.out.println("Found "+sti.countTokens()+" tokens");
  if(sti.countTokens()==2){
    boolean intvalues = true;
    try{
      Integer.parseInt(sti.nextToken());
      Integer.parseInt(sti.nextToken());
      if(intvalues){
        //s = lri.readLine();
        sti = new StringTokenizer(s,delim);
      }
    }catch(Exception e){
      sti = new StringTokenizer(s,delim);
      intvalues = false;
    }
  }
  int colCount = sti.countTokens();
  int cr = 1;
  while(lri.readLine()!= null) cr++;
  int rowCount = cr;
  lri.close();


  massif = new int[rowCount][colCount];
  LineNumberReader lr = new LineNumberReader(new FileReader(FileName));
  i=0;
  //System.out.println(Tabl.rowCount+"\t"+lr.readLine());
  while ( ((s=lr.readLine()) != null)&&(i<rowCount) )
     {
	 //if(i==(int)((float)i*0.001)*1000)
	 //	 System.out.print(i+"("+Utils.getUsedMemoryMb()+")\t");
	 StringTokenizer st = new StringTokenizer(s,delim);
     //System.out.println(st.countTokens());
     int j=0;
     while ((st.hasMoreTokens())&&(j<colCount)) {
        String ss = st.nextToken();
        if(!ss.equals("-"))
        	massif[i][j] = Integer.parseInt(ss);
        else
        	massif[i][j] = Integer.MAX_VALUE;
        //System.out.print(ss+"\t");
        j++;
        }
     i++;
     }
  }
  catch (Exception e) { System.out.println("Error in VDatReadWrite: "+e.toString()+"\n"+"String("+(i+1)+"): "+s);
  e.printStackTrace();
  }
  return massif;
  }
  
  public static VDataTable LoadFromSimpleDatFile(String FileName,boolean firstLineFNames,String delim){
	  return LoadFromSimpleDatFile(FileName,firstLineFNames,delim, false);
  }

  public static VDataTable LoadFromSimpleDatFileString(String text,boolean firstLineFNames,String delim){
	  try{
		  return LoadFromSimpleDatFile(new StringReader(text),firstLineFNames,delim,1000000000);		  
	  }catch (Exception e) { System.out.println("Error in VDatReadWrite: "+e.toString()); return null; }
  }
  

  public static VDataTable LoadFromSimpleDatFile(String FileName,boolean firstLineFNames,String delim, boolean showProgress){
	  VDataTable Tabl = new VDataTable();

	  String s = null;
	  int i = 0;  
	  try{
	  LineNumberReader lri = new LineNumberReader(new FileReader(FileName));
	  s = lri.readLine();
	  if(s==null) s = "";
	  if(s.startsWith("##")) s = lri.readLine();
	  if(s.startsWith("\"##")) s = lri.readLine();
	  StringTokenizer sti = new StringTokenizer(s,delim);
	  //System.out.println("Found "+sti.countTokens()+" tokens");
	  if(sti.countTokens()==2){
	    boolean intvalues = true;
	    try{
	      Integer.parseInt(sti.nextToken());
	      Integer.parseInt(sti.nextToken());
	      if(intvalues){
	        s = lri.readLine();
	        sti = new StringTokenizer(s,delim);
	      }
	    }catch(Exception e){
	      sti = new StringTokenizer(s,delim);
	      intvalues = false;
	    }
	  }
	  Tabl.colCount = sti.countTokens();
	  Tabl.fieldNames = new String[Tabl.colCount];
	  if(!firstLineFNames){
	    for(i=0;i<Tabl.colCount;i++) Tabl.fieldNames[i] = "N"+(i+1);
	  }else{
	    int k=0;
	    while(sti.hasMoreTokens()){
	      Tabl.fieldNames[k] = cutQuotes(sti.nextToken());
	      k++;
	    }
	  }
	  Tabl.fieldTypes = new int[Tabl.colCount];
	  int cn = 0;
	  while(sti.hasMoreTokens()){
	     String s1 = cutQuotes(sti.nextToken());
	     try{
	     float f = Float.parseFloat(s1);
	     Tabl.fieldTypes[cn]=VDataTable.NUMERICAL;
	     }catch(Exception e)
	       {Tabl.fieldTypes[cn]=VDataTable.STRING;}
	  cn++;
	  }
	  int cr = 1;
	  String ss = null;
	  while((ss = lri.readLine())!= null) if(!ss.startsWith("##"))if(!ss.startsWith("\"##")) cr++;
	  Tabl.rowCount = cr;
	  lri.close();


	  Tabl.stringTable = new String[Tabl.rowCount][Tabl.colCount];
	  LineNumberReader lr = new LineNumberReader(new FileReader(FileName));
	  if(firstLineFNames){
	    ss = lr.readLine();
	    if(ss.startsWith("##")) ss = lr.readLine();
	    if(ss.startsWith("\"##")) ss = lr.readLine();
	    Tabl.rowCount = Tabl.rowCount-1;
	  }

	  i=0;
	  //System.out.println(Tabl.rowCount+"\t"+lr.readLine());
	  //System.out.println(Tabl.rowCount+" rows");
	  while ( ((s=lr.readLine()) != null)&&(i<Tabl.rowCount) )if(!s.startsWith("##"))if(!s.startsWith("\"##"))
	     {
		 if(showProgress){
			if((i>0)&&(i==(int)((float)i*0.001)*1000))
		 	 System.out.println(i+"("+Utils.getUsedMemoryMb()+")\t");
		 }
	     PowerfulTokenizer st = new PowerfulTokenizer(s,delim);
	     //System.out.println(st.countTokens());
	     int j=0;
	     //System.out.println(s);
	     while ((st.hasMoreTokens())&&(j<Tabl.colCount)) {
	        ss = st.nextToken();
	        if (ss.length()>1)
	        if (ss.charAt(0)=='\"')
	           ss = new String(ss.substring(1,ss.length()-1));
	        Tabl.stringTable[i][j] = ss;
	        //System.out.print(ss+"\t");
	        j++;
	        }
	     i++;
	     }
	     lr.close();
	  }
	  catch (Exception e) { System.out.println("Error in VDatReadWrite: "+e.toString()+"\n"+"String("+(i+1)+"): "+s);
	  e.printStackTrace();
	  }
	  Tabl.tableID = VDataTable.lastTableID+1;
	  Tabl.fieldClasses = new String[Tabl.colCount];
	  Tabl.fieldDescriptions = new String[Tabl.colCount];
	  VDataTable.lastTableID++;
	  Tabl.name = new String(FileName);
	  return Tabl;
	  }


  

  public static VDataSet LoadFileWithFirstIDColumn(String fn, boolean firstLineColumnNames) throws Exception{
	  VDataSet vd = new VDataSet();
	  LineNumberReader lr = new LineNumberReader(new FileReader(fn));
	  int numberOfLines = 1;
	  String s = lr.readLine();
	  String cols[] = s.split("\t");
	  int numberOfColumns = cols.length;
	  while(lr.readLine()!=null) numberOfLines++;
	  vd.coordCount = numberOfColumns-1;
	  vd.pointCount = numberOfLines;
	  vd.massif = new float[vd.pointCount][vd.coordCount];
	  lr.close();
	  int k=0;
	  lr = new LineNumberReader(new FileReader(fn));
	  if(firstLineColumnNames) lr.readLine();
	  while((s=lr.readLine())!=null){
		 if(k==(int)((float)k*0.000001)*1000000)
		 	 System.out.println(k+"("+Utils.getUsedMemoryMb()+")\t");
		  cols = s.split("\t");
		  for(int i=1;i<cols.length;i++)
			  vd.massif[k][i-1] = Float.parseFloat(cols[i]);
		  k++;
	  }
	  return vd;
  }
  

  public static void saveToVDatFile(VDataTable tab,String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      fw.write(tab.colCount+" "+tab.rowCount+"\r\n");
      for(int i=0;i<tab.colCount;i++){
        String typ = "FLOAT";
        if(tab.fieldTypes[i]!=VDataTable.NUMERICAL) typ = "STRING";
        //fw.write(tab.fieldNames[i]+" "+typ+" "+tab.fieldClasses[i]+" "+tab.fieldDescriptions[i]+"\r\n");
        fw.write(tab.fieldNames[i]+"\t"+typ);
        if(tab.fieldInfo!=null){
        for(int k=0;k<tab.fieldInfo[i].length;k++)
          if(tab.fieldInfo[i][k]!=null)
              fw.write("\t"+tab.fieldInfo[i][k]);
        }
        fw.write("\r\n");
      }
      String fs = "#.";
      for(int i=0;i<numberOfDigitsToKeep;i++)
        fs+="#";
      DecimalFormat nf = new DecimalFormat(fs);


      for(int i=0;i<tab.rowCount;i++){
        for(int j=0;j<tab.colCount;j++){
          String s = tab.getV(i,j);
          if(s==null) s="";
          s = replaceString(s," ","_");
            if(tab.fieldTypes[j]==tab.NUMERICAL){
                if(numberOfDigitsToKeep>=0){
                   if((!s.equals("NULL"))&&(!s.equals("@"))){
                	 if(s.equals("")){
                		 //System.out.println("String is empty in row"+i+", column "+j);
                		 s = null;
                	 }else{
                     float f = Float.parseFloat(s);
                     //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep))/(float)Math.pow(10,(double)numberOfDigitsToKeep));
                     //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep)));
                     //s = ""+f;
                     s = nf.format(f);
                	 }
                   }
                }
              //if(useQuotesEverywhere)
              //  fw.write("\""+s+"\" ");
              //else
                fw.write(""+s+"\t");
            }
            else{
              if(useQuotesEverywhere)
                fw.write("\""+s+"\"\t");
              else
                fw.write(""+s+"\t");
            }
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void saveToVDatFile(VDataSet tab,String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      fw.write(tab.coordCount+" "+tab.pointCount+"\r\n");
      for(int i=0;i<tab.coordCount;i++){
        String typ = "FLOAT";
        //fw.write(tab.fieldNames[i]+" "+typ+" "+tab.fieldClasses[i]+" "+tab.fieldDescriptions[i]+"\r\n");
        fw.write("F"+(i+1)+" "+typ+"\r\n");
      }
      for(int i=0;i<tab.pointCount;i++){
        for(int j=0;j<tab.coordCount;j++){
          String s = ""+tab.massif[i][j];
          s = replaceString(s," ","_");
          fw.write("\""+s+"\" ");
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void saveToVDatFileLabeled(VDataSet tab,String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      fw.write((tab.coordCount+1)+" "+tab.pointCount+"\r\n");
      fw.write("N"+" "+"STRING"+"\r\n");
      for(int i=0;i<tab.coordCount;i++){
        String typ = "FLOAT";
        //fw.write(tab.fieldNames[i]+" "+typ+" "+tab.fieldClasses[i]+" "+tab.fieldDescriptions[i]+"\r\n");
        fw.write("F"+(i+1)+" "+typ+"\r\n");
      }
      for(int i=0;i<tab.pointCount;i++){
        fw.write("\""+i+"\" ");
        for(int j=0;j<tab.coordCount;j++){
          String s = ""+tab.massif[i][j];
          s = replaceString(s," ","_");
          fw.write("\""+s+"\" ");
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  
  
  //public static void saveToSimpleDatFile(VDataTable tab,String fn, boolean writeNumberOfColumnsRows){
  public static void saveToSimpleDatFile(VDataTable tab,String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      if(writeNumberOfColumnsRows)
    	  fw.write(tab.colCount+"\t"+tab.rowCount+"\r\n");
      if(writeHeader){
      for(int i=0;i<tab.colCount;i++){
        fw.write(tab.fieldNames[i]+"\t");
      }
      fw.write("\r\n");
      }
      String fs = "#.";
      for(int i=0;i<numberOfDigitsToKeep;i++)
        fs+="#";
      DecimalFormat nf = new DecimalFormat(fs);
      for(int i=0;i<tab.rowCount;i++){
        for(int j=0;j<tab.colCount;j++){
          String s = tab.getV(i,j);
          if(s==null) s="";
          s = replaceString(s," ","_");
          if(tab.fieldTypes[j]==tab.NUMERICAL){
            if(numberOfDigitsToKeep>=0){
               if((!s.equals("NULL"))&&(!s.equals("@"))&&(!s.toLowerCase().equals("n/a"))){
                 float f = Float.parseFloat(s);
                 //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep))/(float)Math.pow(10,(double)numberOfDigitsToKeep));
                 //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep)));
                 //s = ""+f;
                 s = nf.format(f);
               }
            }
          }
          if(useQuotesEverywhere)
            fw.write("\""+s+"\"\t");
          else
            fw.write(""+s+"\t");
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public static void saveToSimpleDatFile(VDataTable tab,String fn, boolean simpleTabFile){
	    try{
	      FileWriter fw = new FileWriter(fn);
	      for(int i=0;i<tab.colCount;i++){
	        fw.write(tab.fieldNames[i]+"\t");
	      }
	      fw.write("\r\n");
	      String fs = "#.";
	      for(int i=0;i<numberOfDigitsToKeep;i++)
	        fs+="#";
	      DecimalFormat nf = new DecimalFormat(fs);
	      for(int i=0;i<tab.rowCount;i++){
	        for(int j=0;j<tab.colCount;j++){
	          String s = tab.getV(i,j);
	          if(s==null) s="";
	          if(tab.fieldTypes[j]==tab.NUMERICAL){
	            if(numberOfDigitsToKeep>=0){
	               if((!s.equals("NULL"))&&(!s.equals("@"))){
	                 float f = Float.parseFloat(s);
	                 //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep))/(float)Math.pow(10,(double)numberOfDigitsToKeep));
	                 //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep)));
	                 //s = ""+f;
	                 s = nf.format(f);
	               }
	            }
	          }
	            fw.write(""+s+"\t");
	        }
	        fw.write("\r\n");
	      }
	      fw.close();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  }
  

  public static void saveToSimpleDatFilePureNumerical(VDataTable tab,String fn){
	  saveToSimpleDatFilePureNumerical(tab,fn,true);
  }
  
  public static void saveToSimpleDatFilePureNumerical(VDataTable tab,String fn, boolean addHeaderLine){
    try{
      FileWriter fw = new FileWriter(fn);
      //fw.write(tab.colCount+" "+tab.rowCount+"\r\n");
      if(addHeaderLine){
      for(int i=0;i<tab.colCount;i++){
        if(tab.fieldTypes[i]==tab.NUMERICAL)
          fw.write(tab.fieldNames[i]+"\t");
      }
      fw.write("\r\n");
      }
      String fs = "#.";
      for(int i=0;i<numberOfDigitsToKeep;i++)
        fs+="#";
      DecimalFormat nf = new DecimalFormat(fs);
      for(int i=0;i<tab.rowCount;i++){
        for(int j=0;j<tab.colCount;j++) if(tab.fieldTypes[j]==tab.NUMERICAL){
          String s = tab.getV(i,j);
          if(s==null) s="";
          s = replaceString(s," ","_");
          if(tab.fieldTypes[j]==tab.NUMERICAL){
            if(numberOfDigitsToKeep>=0){
            if((!s.equals("NULL"))&&(!s.equals("@"))){
               float f = Float.parseFloat(s);
               //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep))/(float)Math.pow(10,(double)numberOfDigitsToKeep));
               //f = (float)(1f*((int)f*Math.pow(10,(double)numberOfDigitsToKeep)));
               //s = ""+f;
               s = nf.format(f);
            }
            }
          }
          if(useQuotesEverywhere)
            fw.write("\""+s+"\"\t");
          else
            fw.write(""+s+"\t");
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void saveToSimpleDatFilePureNumerical(VDataSet tab,String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      String fs = "#.";
      for(int i=0;i<numberOfDigitsToKeep;i++)
        fs+="#";
      DecimalFormat nf = new DecimalFormat(fs);
      for(int i=0;i<tab.pointCount;i++){
          for(int j=0;j<tab.coordCount;j++){
            String s = null;
            float f = tab.massif[i][j];
            if(numberOfDigitsToKeep==-1)
              s = ""+f;
            else
              s = nf.format(f);
            if(useQuotesEverywhere)
              fw.write("\""+s+"\"\t");
            else
              fw.write(""+s+"\t");
           }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }


  public static String replaceString(String source,String shabl,String val){
      int i=0;
      String s1 = new String(source);
      while((i=s1.indexOf(shabl))>=0){
              StringBuffer sb = new StringBuffer(s1);
              sb.replace(i,i+shabl.length(),val);
              s1 = sb.toString();
      }
      return s1;
  }

  public static String cutQuotes(String s){
    String r = s;
    if(s.charAt(0)=='"')
      r = s.substring(1,s.length());
    if(s.charAt(s.length()-1)=='"')
      r = r.substring(0,r.length()-1);
    return r;
  }

  public static void printUsedMemory(){
      long mem = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
      //long mem = Runtime.getRuntime().freeMemory();
      mem = (long)(1e-6f*mem);
      System.out.println("Used memory "+mem+"M");
  }



}