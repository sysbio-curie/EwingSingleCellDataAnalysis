package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;



public class TableUtils {

  public static void main(String[] args) {
	  
	  try{
		
		if(args.length==0){
			
		args = new String[7];
		args[0] = "--classmap";
		args[1] = "C:/Datas/Peshkin/xenopus/day23r_reduced50_noPC1.pointmap";
		args[2] = "--table";
		args[3] = "C:/Datas/Peshkin/xenopus/day23l_TFt.txt";
		args[4] = "--out";
		args[5] = "C:/Datas/Peshkin/xenopus/day23r_reduced50_noPC1.nodeaverage";
		args[6] = "--averageinclass";
			
		/*VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/Gorban/housholders_agr.dat");		  
		vt1 = TableUtils.PCAtable(vt1, true, true, 5);  
		VDatReadWrite.saveToVDatFile(vt1, "C:/Datas/Gorban/housholders_agr1.dat");
		System.exit(0);  
		  
	    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/Basal/220310/Matrix.dat");
		//VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Basal/220310/Matrix.txt",true,"\t");
	    //vt = normalizeVDat(vt,true,false);
	    vt = filterByVariation(vt,2000, false);
	    
	    VDatReadWrite.saveToVDatFile(vt, "C:/Datas/Basal/220310/Matrix_filtered.dat");
	    
	    vt = vt.transposeTable("ID");
	    
	    VDatReadWrite.saveToVDatFile(vt, "C:/Datas/Basal/220310/Matrix_filtered_T.dat");*/
		}
		
		OptionParser options = new OptionParser(args, null);
		File f = null;
		File coordTableFile = null;
		File mapObjectClassFile = null;
		boolean doAverageClasses = false;
		Boolean b = false;
		File outFile = null;

		if ((f = options.fileOption("classmap", "file with pairs of integers representing a map <object>-><class>")) != null)
			mapObjectClassFile = f;
		if ((f = options.fileOption("table", "table with rows-objects and some numerical columns to be averaged over the graph nodes")) != null)
			coordTableFile = f;
		if ((f = options.fileOption("out", "output file")) != null)
			outFile = f;
		if ((b = options.booleanOption("averageinclass", "table with rows-objects and some numerical columns to be averaged over the graph nodes")) != null)
			doAverageClasses = b;
		options.done();
		
		if(doAverageClasses){
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(coordTableFile.getAbsolutePath(), true, "\t");
			TableUtils.findAllNumericalColumns(vt);
			averageTableUsingObjectClassMap(vt,mapObjectClassFile.getAbsolutePath(),outFile);
			VDatReadWrite.saveToSimpleDatFile(vt, coordTableFile.getAbsolutePath()+".classinfo");
		}
			
		  
		  
	  }catch(Exception e){
		  e.printStackTrace();
	  }

  }


public static void printFieldInfoSummary(VDataTable vt, int fieldClass, int
fieldSubclass){
    Vector cl = new Vector();
    Vector subcl = new Vector();
    for(int i=0;i<vt.colCount;i++){
      String scl = vt.fieldInfo[i][fieldClass];
      String sscl = vt.fieldInfo[i][fieldSubclass];
      if((scl!=null)&&(!scl.equals("")))
        if(cl.indexOf(scl)<0)
          cl.add(scl);
      if((sscl!=null)&&(!sscl.equals("")))
        if(subcl.indexOf(sscl)<0)
          subcl.add(sscl);
    }
    float table[][] =  new float[cl.size()][subcl.size()];
    float tablea[][] =  new float[cl.size()][subcl.size()];
    float totalsubcl[] =  new float[subcl.size()];
    for(int i=0;i<vt.colCount;i++){
      String scl = vt.fieldInfo[i][fieldClass];
      String sscl = vt.fieldInfo[i][fieldSubclass];
      if((scl!=null)&&(!scl.equals("")))
        if((sscl!=null)&&(!sscl.equals(""))){
             int i1 = cl.indexOf(scl);
             int j1 = subcl.indexOf(sscl);
             table[i1][j1]+=1f;
             tablea[i1][j1]+=1f;
             totalsubcl[j1]+=1f;
        }
    }
    System.out.print("Class");
    for(int j=0;j<subcl.size();j++)
     
System.out.print("\t"+(String)subcl.elementAt(j)+"\t"+(String)subcl.elementAt(j));
    System.out.println();
    for(int i=0;i<cl.size();i++){
      System.out.print((String)cl.elementAt(i));
      float sum = 0f;
      for(int j=0;j<subcl.size();j++)
        sum+=table[i][j];
      for(int j=0;j<subcl.size();j++){
        table[i][j]/=sum;
        System.out.print("\t"+tablea[i][j]+"\t"+table[i][j]);
      }
    System.out.println();
    }
    System.out.print("Total");
    float sum = 0f;
    for(int j=0;j<subcl.size();j++)
      sum+=totalsubcl[j];
    for(int j=0;j<subcl.size();j++){
      System.out.print("\t"+totalsubcl[j]);
      totalsubcl[j]/=sum;
      System.out.print("\t"+totalsubcl[j]);
    }
    System.out.println();
  }

  public static void printClassSpecificSignaturePrediction(VDataTable vt,
String fieldClass, String fieldPrediction, String fieldAnswer, String
rightAnswer){
    Vector cl = new Vector();
    float thresh = 0.1f;
    int idfc = vt.fieldNumByName(fieldClass);
    int idfp = vt.fieldNumByName(fieldPrediction);
    int idfa = vt.fieldNumByName(fieldAnswer);
    for(int i=0;i<vt.rowCount;i++){
      String scl = vt.stringTable[i][idfc];
      if((scl!=null)&&(!scl.equals("")))
        if(cl.indexOf(scl)<0)
          cl.add(scl);
    }
    float TP[] = new float[cl.size()];
    float TN[] = new float[cl.size()];
    float FP[] = new float[cl.size()];
    float FN[] = new float[cl.size()];
    float TPt = 0f, TNt = 0f, FPt = 0f, FNt = 0f;
    float TPr = 0f, TNr = 0f, FPr = 0f, FNr = 0f;
    float ratio = 0f; float total = 0f;

    for(int i=0;i<vt.rowCount;i++){
     String scl = vt.stringTable[i][idfc];
     float pred = Float.parseFloat(vt.stringTable[i][idfp]);
     boolean predicted = pred>thresh;
     boolean answered = vt.stringTable[i][idfa].equals(rightAnswer);
     if((predicted)&&(answered)) { TP[cl.indexOf(scl)]+=1f; TPt+=1f; }
     if((predicted)&&(!answered)) { FP[cl.indexOf(scl)]+=1f; FPt+=1f; }
     if((!predicted)&&(answered)) { FN[cl.indexOf(scl)]+=1f; FNt+=1f; }
     if((!predicted)&&(!answered)) { TN[cl.indexOf(scl)]+=1f; TNt+=1f; }
     if(answered) ratio+=1f;
     total+=1f;
    }
    ratio/=total;

    System.out.println("Class\tSn\tSp\tPPV\tNPV\tAcc\tTP\tFP\tTN\tFN");
    for(int i=0;i<cl.size();i++){
      System.out.print((String)cl.elementAt(i));
      System.out.print("\t"+(TP[i]/(TP[i]+FN[i]))+"\t"+(TN[i]/(FP[i]+TN[i])));
      System.out.print("\t"+(TP[i]/(TP[i]+FP[i]))+"\t"+(TN[i]/(FN[i]+TN[i])));
      System.out.print("\t"+((TP[i]+TN[i])/(TP[i]+FP[i]+TN[i]+FN[i])));
      System.out.println("\t"+TP[i]+"\t"+FP[i]+"\t"+TN[i]+"\t"+FN[i]);
    }
    System.out.print("Total");
    System.out.print("\t"+(TPt/(TPt+FNt))+"\t"+(TNt/(FPt+TNt)));
    System.out.print("\t"+(TPt/(TPt+FPt))+"\t"+(TNt/(FNt+TNt)));
    System.out.print("\t"+((TPt+TNt)/(TPt+FPt+TNt+FNt)));
    System.out.println("\t"+TPt+"\t"+FPt+"\t"+TNt+"\t"+FNt);

    Random r = new Random();
    for(int k=0;k<1000;k++){
      for(int i=0;i<vt.rowCount;i++){
        boolean predicted = r.nextFloat()>(1-ratio);
        boolean answered = vt.stringTable[i][idfa].equals(rightAnswer);
        if((predicted)&&(answered)) { TPr+=1f; }
        if((predicted)&&(!answered)) { FPr+=1f; }
        if((!predicted)&&(answered)) { FNr+=1f; }
        if((!predicted)&&(!answered)) { TNr+=1f; }
      }
    }
    System.out.print("Random_guess");
    System.out.print("\t"+(TPr/(TPr+FNr))+"\t"+(TNr/(FPr+TNr)));
    System.out.print("\t"+(TPr/(TPr+FPr))+"\t"+(TNr/(FNr+TNr)));
    System.out.print("\t"+((TPr+TNr)/(TPr+FPr+TNr+FNr)));
    System.out.println("\t"+TPr+"\t"+FPr+"\t"+TNr+"\t"+FNr);

  }

public static VDataTable filterByVariation(VDataTable vt, int numOfGenes, boolean doScaling){
    float var[] = new float[vt.rowCount];
    VDataTable vtr = new VDataTable();
    vtr.copyHeader(vt);
    vtr.rowCount = numOfGenes;
    vtr.colCount = vt.colCount;
    vtr.stringTable = new String[numOfGenes][vt.colCount];
    VDataSet vd = null;
    if(doScaling)
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    else
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    for(int i=0;i<vt.rowCount;i++){
      float v[] = vd.getVector(i);
      var[i] = (float)VSimpleFunctions.calcStandardDeviation(v);
    }
    int ord[] = Sort(var);
    for(int i=0;i<numOfGenes;i++){
      int k = ord[i];
      vtr.stringTable[i] = vt.stringTable[k];
    }
    return vtr;
  }
  
  public static VDataSet filterByVariation(VDataSet vd, int numOfGenes){
  float var[] = new float[vd.pointCount];
    for(int i=0;i<vd.pointCount;i++){
      float v[] = vd.getVector(i);
      var[i] = (float)VSimpleFunctions.calcStandardDeviation(v);
    }
    
    System.out.println("Sorting...");
    int ord[] = Sort(var);
    System.out.println("Sorted.");
    
    VDataSet vdf = new VDataSet();
    vdf.coordCount = vd.coordCount;
    vdf.pointCount = numOfGenes;
    vdf.massif = new float[vdf.pointCount][vdf.coordCount];
    
    for(int i=0;i<numOfGenes;i++){
      int k = ord[i];
      vdf.massif[i] = vd.massif[k];
    }
          
    return vdf;
  }  

  public static int[] filterByVariationOrder(VDataSet vd, int numOfGenes){
  float var[] = new float[vd.pointCount];
    for(int i=0;i<vd.pointCount;i++){
      float v[] = vd.getVector(i);
      var[i] = (float)VSimpleFunctions.calcStandardDeviation(v);
    }
    
    System.out.println("Sorting...");
    int ord[] = Sort(var);
    System.out.println("Sorted.");
    
    //VDataSet vdf = new VDataSet();
    //vdf.coordCount = vd.coordCount;
    //vdf.pointCount = numOfGenes;
    //vdf.massif = new float[vdf.pointCount][vdf.coordCount];
    
    //for(int i=0;i<numOfGenes;i++){
    // int k = ord[i];
    //  vdf.massif[i] = vd.massif[k];
    //}
          
    return ord;
  }  
     
  public static VDataTable PCAtable(VDataTable vt, boolean doScaling){
  return PCAtable(vt,doScaling,false);
  }

  public static VDataTable PCAtable(VDataTable vt, boolean doScaling, boolean addOriginalColumns){
	 return PCAtable(vt, doScaling, addOriginalColumns, 3);
  }
  
  public static VDataTable PCAtable(VDataTable vt, boolean doScaling, boolean addOriginalColumns, int numOfComponents){
    VDataTable vtr = new VDataTable();
    int numOfStringFields = 0;
    int numOfNumericalColumns = 0;
    for(int i=0;i<vt.colCount;i++){
      if(vt.fieldTypes[i]==vt.STRING)
        numOfStringFields++;
      if(vt.fieldTypes[i]==vt.NUMERICAL)
        numOfNumericalColumns++;
    }
    vtr.fieldClasses = new String[numOfStringFields+numOfComponents];
    vtr.fieldDescriptions = new String[numOfStringFields+numOfComponents];
    vtr.fieldNames = new String[numOfStringFields+numOfComponents];
    vtr.fieldTypes = new int[numOfStringFields+numOfComponents];
    if(vt.fieldInfo!=null)
      vtr.fieldInfo = new
String[numOfStringFields+numOfComponents][vt.fieldInfo[0].length];
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
        vd =
vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    PCAMethod pca = new PCAMethod();
    pca.setDataSet(vd);
    pca.calcBasis(numOfComponents);
    VDataSet vdp = pca.getProjectedDataset();
    for(int i=0;i<vtr.rowCount;i++)
    for(int j=0;j<numOfComponents;j++){
      vtr.stringTable[i][j+numOfStringFields] = "" + vdp.massif[i][j];
    }
    
    if(addOriginalColumns){
    for(int i=0;i<vt.colCount;i++){
    if(vt.fieldTypes[i]==vt.NUMERICAL){
    vtr.addNewColumn(vt.fieldNames[i], vt.fieldClasses[i],
vt.fieldDescriptions[i], vt.fieldTypes[i], "@");
    for(int j=0;j<vtr.rowCount;j++)
    vtr.stringTable[j][vtr.fieldNumByName(vt.fieldNames[i])] =
vt.stringTable[j][vt.fieldNumByName(vt.fieldNames[i])];
    }
    }
    }
    return vtr;
  }

  public static double norm( float[] data )
      {
       double d = 0;
       for(int i=0;i<data.length;i++)
           d+=data[i]*data[i];
       return Math.sqrt(d);
      }
  
  public static double norm( double[] data )
  {
   double d = 0;
   for(int i=0;i<data.length;i++)
       d+=data[i]*data[i];
   return Math.sqrt(d);
  }
  

      public static double mean( float[] data )
          {
           double d = 0;
           for(int i=0;i<data.length;i++)
               d+=data[i];
           return d/data.length;
          }

          public static double stddev( float[] data )
              {
               double d = 0;
               float mn = (float)mean(data);
               for(int i=0;i<data.length;i++)
                   d+=(data[i]-mn)*(data[i]-mn);
               d/=data.length;
               return Math.sqrt(d);
              }


   public static int[] Sort(float cais[]){
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
                    while (cais[res[j-inc-1]]<v) {
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


    public static VDataTable substituteRowsByStatistics(VDataTable vt, String
classField, int statType){ // 0 - mean, 1 - stdv, 2 - min, 3 - max, 4 - median
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
        vres.stringTable[i][vres.fieldNumByName(classField)] =
(String)classes.elementAt(i)+"_"+pref;
        VStatistics vs = (VStatistics)stats.elementAt(i);
        for(int j=0;j<vd.selector.selectedColumns.length;j++){
          float num = 0f;
          if(statType==0) num = vs.getMean(j);
          if(statType==1) num = vs.getStdDev(j);
          if(statType==2) num = vs.getMin(j);
          if(statType==3) num = vs.getMax(j);
          vres.stringTable[i][vd.selector.selectedColumns[j]] = ""+num;
        }
      }

      return vres;
    }

    public static VDataTable normalizeVDat(VDataTable vt, boolean centr,
boolean norm) throws Exception{

      boolean num[] = new boolean[vt.colCount];
      int numfields = 0;
      for(int i=0;i<vt.colCount;i++){
        if(vt.fieldTypes[i]==vt.NUMERICAL) {numfields++; num[i]=true; }
        else num[i]=false;
      }

      VDataTable res = new VDataTable();
      res.copyHeader(vt);
      res.rowCount = vt.rowCount;
      res.stringTable = new String[res.rowCount][res.colCount];

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
          if(vt.fieldTypes[j]==vt.NUMERICAL) { res.stringTable[i][j] =
""+f[k];  k++; }
          else res.stringTable[i][j] = vt.stringTable[i][j];
        }
      }
      return res;
    }

    public static void printAssosiationTable(VDataTable vt, String fout,
Vector strNames, float thresh, boolean transposed){
      Vector fieldNames = new Vector();
      Vector fieldClasses = new Vector();
      Vector valNames = new Vector();
      Vector vals = new Vector();

      for(int i=0;i<strNames.size();i++){
        int id = vt.fieldNumByName((String)strNames.elementAt(i));
        if(id!=-1){
          fieldNames.add((String)strNames.elementAt(i));
          String cl[] = new String[vt.rowCount];
          for(int j=0;j<vt.rowCount;j++)
            cl[j] = vt.stringTable[j][id];
          fieldClasses.add(cl);
        }
      }

      for(int i=0;i<vt.colCount;i++){
        if(vt.fieldTypes[i]==vt.NUMERICAL){
          valNames.add(vt.fieldNames[i]);
          float f[] = new float[vt.rowCount];
          for(int j=0;j<vt.rowCount;j++)
            f[j] = Float.parseFloat(vt.stringTable[j][i]);
          vals.add(f);
        }
      }

      float f[][] = findAssosiations(fieldNames, fieldClasses, valNames, vals);
      if(!transposed)
        printAssociations(f,fieldNames,valNames,fout,thresh);
      else
        printAssociationsT(f,fieldNames,valNames,fout,thresh);

    }

    public static float[][] findAssosiationsInDataTable(VDataTable vt, Vector
strNames){
      Vector fieldNames = new Vector();
      Vector fieldClasses = new Vector();
      Vector valNames = new Vector();
      Vector vals = new Vector();

      for(int i=0;i<strNames.size();i++){
        int id = vt.fieldNumByName((String)strNames.elementAt(i));
        if(id!=-1){
          fieldNames.add((String)strNames.elementAt(i));
          String cl[] = new String[vt.rowCount];
          for(int j=0;j<vt.rowCount;j++)
            cl[j] = vt.stringTable[j][id];
          fieldClasses.add(cl);
        }
      }

      for(int i=0;i<vt.colCount;i++){
        if(vt.fieldTypes[i]==vt.NUMERICAL){
          valNames.add(vt.fieldNames[i]);
          float f[] = new float[vt.rowCount];
          for(int j=0;j<vt.rowCount;j++)
            f[j] = Float.parseFloat(vt.stringTable[j][i]);
          vals.add(f);
        }
      }

      return findAssosiations(fieldNames, fieldClasses, valNames, vals);
    }

    public static float[][] findAssosiations(Vector fieldNames, Vector
fieldClasses, Vector valNames, Vector vals){
        float res[][] = new float[fieldNames.size()][valNames.size()];
        for(int i=0;i<fieldClasses.size();i++){
          String cl[] = (String[])fieldClasses.elementAt(i);
          Vector lbls = new Vector();
          for(int j=0;j<cl.length;j++){
            String lb = cl[j];
            if((!lb.equals("_"))&&(!lb.equals("NA"))){
              if(lbls.indexOf(lb)<0) lbls.add(lb);
            }
          }
          for(int j=0;j<vals.size();j++){
          float tvalues[] = new float[(int)(0.5f*(lbls.size()-1)*lbls.size())];
          int k = 0;
          for(int k1=0;k1<lbls.size();k1++)
            for(int k2=k1+1;k2<lbls.size();k2++){
               String lb1 = (String)lbls.elementAt(k1);
               String lb2 = (String)lbls.elementAt(k2);
                 float val[] = (float[])vals.elementAt(j);
                 Vector set1 = new Vector();
                 Vector set2 = new Vector();
                 for(int jj=0;jj<cl.length;jj++){
                   if(cl[jj].equals(lb1)) set1.add(new Float(val[jj]));
                   if(cl[jj].equals(lb2)) set2.add(new Float(val[jj]));
                 }
               double tvalue = calcTTest(set1,set2);
               tvalues[k++] = (float)Math.abs(tvalue);
            }
          float tval = (float)max(tvalues);
          res[i][j] = tval;
          }
        }
        return res;
    }

    public static void printAssociations(float vals[][], Vector fieldNames,
Vector valNames, String fout, float thresh){
      try{
        FileWriter fw = new FileWriter(fout);
        fw.write("FIELD");
        for(int i=0;i<valNames.size();i++)
          fw.write("\t"+(String)valNames.elementAt(i));
        fw.write("\r\n");
        for(int i=0;i<fieldNames.size();i++){
             fw.write((String)fieldNames.elementAt(i));
             for(int j=0;j<valNames.size();j++){
               float f = vals[i][j];
               fw.write("\t");
               if(f>=thresh)
                 fw.write(""+f);
               else
                 fw.write("_");
             }
        fw.write("\r\n");
        }
        fw.close();
      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public static void printAssociationsT(float vals[][], Vector fieldNames,
Vector valNames, String fout, float thresh){
      try{
        FileWriter fw = new FileWriter(fout);
        fw.write("VAL");
        for(int i=0;i<fieldNames.size();i++)
          fw.write("\t"+(String)fieldNames.elementAt(i));
        fw.write("\r\n");
        for(int i=0;i<valNames.size();i++){
             fw.write((String)valNames.elementAt(i));
             for(int j=0;j<fieldNames.size();j++){
               float f = vals[j][i];
               fw.write("\t");
               if(f>=thresh)
                 fw.write(""+f);
               else
                 fw.write("_");
             }
        fw.write("\r\n");
        }
        fw.close();
      }catch(Exception e){
        e.printStackTrace();
      }
    }


    public static double calcTTest(Vector set1, Vector set2){
      double r = 0;
      VStatistics stat1 = new VStatistics(1);
      VStatistics stat2 = new VStatistics(1);
      float d[] = new float[1];
      for(int i=0;i<set1.size();i++){
        d[0] = ((Float)set1.elementAt(i)).floatValue();
        stat1.addNewPoint(d);
      }
      for(int i=0;i<set2.size();i++){
        d[0] = ((Float)set2.elementAt(i)).floatValue();
        stat2.addNewPoint(d);
      }
      stat1.calculate();
      stat2.calculate();
      r =
(stat1.getMean(0)-stat2.getMean(0))/Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)+stat2.getStdDev(0)*stat2.getStdDev(0));
      return r;
    }

    public static float max(float[] t) {
      float maximum = 0;
        if(t.length!=0)
        {
        maximum = t[0];   // start with the first value
        for (int i=1; i<t.length; i++) {
            if (t[i] > maximum) {
                maximum = t[i];   // new maximum
            }
        }
        }
        return maximum;
    }

    public static void reformatForEisenCluster(VDataTable vt, String
labelField, String fn, boolean conserveOrder) throws Exception{
      FileWriter fw = new FileWriter(fn);
      fw.write("YORF\tNAME\tGWEIGHT\t");
      if(conserveOrder)
        fw.write("GORDER\t");
      int k = vt.fieldNumByName(labelField);
      int lastcolumn = -1;
      for(int i=0;i<vt.colCount;i++)
        if(vt.fieldTypes[i]==vt.NUMERICAL)
          lastcolumn = i;
      for(int i=0;i<vt.colCount;i++)
        if(vt.fieldTypes[i]==vt.NUMERICAL)
          if(i!=lastcolumn)
            fw.write(vt.fieldNames[i]+"\t");
          else{
            fw.write(vt.fieldNames[i]);
          }
      fw.write("\n");
      fw.write("EWEIGHT\t\t\t");
      for(int i=0;i<vt.colCount;i++)
        if(vt.fieldTypes[i]==vt.NUMERICAL)
          fw.write("1\t");
      fw.write("\n");
      for(int i=0;i<vt.rowCount;i++){
        fw.write(vt.stringTable[i][k]+"\t"+vt.stringTable[i][k]+"\t1\t");
        if(conserveOrder)
          fw.write(""+(i+1)+"\t");
        for(int j=0;j<vt.colCount;j++)
          if(vt.fieldTypes[j]==vt.NUMERICAL)
            if(j!=lastcolumn)
              fw.write(vt.stringTable[i][j]+"\t");
            else
              fw.write(vt.stringTable[i][j]);
        fw.write("\n");
      }
      fw.close();
    }

	public static void findAllNumericalColumns(VDataTable vt){
		for(int i=0;i<vt.colCount;i++){
			boolean isNumerical = true;
			for(int j=0;j<vt.rowCount;j++){
				String s = vt.stringTable[j][i].trim();
				if(s.equals("\"\"")||s.equals("NA")||s.equals("N/A")||s.equals("")||s.equals("_")){
					s = "@";
					vt.stringTable[j][i] = s;
				}else{
				try{
					Float f = Float.parseFloat(s);
				}catch(Exception e){
					//e.printStackTrace();
					isNumerical = false;
					//System.out.println("Non-numerical value = "+s);
				}}
			}
			if(isNumerical)
				vt.fieldTypes[i] = vt.NUMERICAL;
		}
	}
	
	public static VDataTable selectUniqueRowsIdsByVariance(VDataTable vt, boolean doScaling){
		VDataTable ret = null;
		vt.makePrimaryHash(vt.fieldNames[0]);
		Set<String> names = vt.tableHashPrimary.keySet();
		VDataSet vd = null;
	    if(doScaling)
	        vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
	      else
	        vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
	    Vector<Integer> selectedrows = new Vector<Integer>();
	    for(String name: names){
	    	Vector<Integer> rows = vt.tableHashPrimary.get(name);
	    	float maxvar = 0f;
	    	int maxind = -1;
	    	for(Integer row: rows){
	    		float v[] = vd.getVector(row);
	    		float var = (float)VSimpleFunctions.calcStandardDeviation(v);
	    		if(var>maxvar){
	    			maxvar = var; maxind = row;
	    		}
	    	}
	    	selectedrows.add(maxind);
	    }
	    ret = VSimpleProcedures.selectRows(vt, selectedrows);
		return ret;
	}

	public static VDataTable selectUniqueRowsIdsByMaxAbs(VDataTable vt){
		VDataTable ret = null;
		vt.makePrimaryHash(vt.fieldNames[0]);
		Set<String> names = vt.tableHashPrimary.keySet();
		VDataSet vd = null;
	    vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
	    Vector<Integer> selectedrows = new Vector<Integer>();
	    for(String name: names){
	    	Vector<Integer> rows = vt.tableHashPrimary.get(name);
	    	float maxval = 0f;
	    	int maxind = -1;
	    	for(Integer row: rows){
	    		float v[] = vd.getVector(row);
	    		float val = (float)VSimpleFunctions.calcMaxAbsValue(v);
	    		if(val>maxval){
	    			maxval = val; maxind = row;
	    		}
	    	}
	    	selectedrows.add(maxind);
	    }
	    ret = VSimpleProcedures.selectRows(vt, selectedrows);
		return ret;
	}
	
	
	  private static void averageTableUsingObjectClassMap(VDataTable vt, String mapFilePath, File outFile) throws Exception{
		  	Vector<String> fieldNames = new Vector<String>();
		  	for(int i=0;i<vt.colCount;i++) 
		  		if(vt.fieldTypes[i]==vt.NUMERICAL) 
		  			fieldNames.add(vt.fieldNames[i]);
		  	FileWriter fw = new FileWriter(outFile);
		  	vt.addNewColumn("_CLASS_", "", "", vt.STRING, "NA");
		  	Vector<String> mapS = Utils.loadStringListFromFile(mapFilePath);
		  	for(String line: mapS){
		  		StringTokenizer st = new StringTokenizer(line,"\t"); 
		  		int k = Integer.parseInt(st.nextToken());
		  		String cls = st.nextToken();
		  		vt.stringTable[k][vt.fieldNumByName("_CLASS_")] = cls;
		  	}
		  	vt.makePrimaryHash("_CLASS_");
		  	Set<String> classes = vt.tableHashPrimary.keySet();
		  	fw.write("CLASS\t"); for(String s: fieldNames) fw.write(s+"\t"); fw.write("\n");
		  	for(String s: classes){
		  		fw.write(s+"\t");
		  		Vector<Integer> rows = vt.tableHashPrimary.get(s);
		  		for(int i=0;i<fieldNames.size();i++){ 
		  		float f = 0f;
		  		int n = 0;
		  		for(Integer row: rows){
		  			f+=Float.parseFloat(vt.stringTable[row][vt.fieldNumByName(fieldNames.get(i))]);
		  			n++;
		  		}
		  		f/=n;
		  		fw.write(f+"\t");
		  		}
		  		fw.write("\n");
		  	}
		  	fw.close();
		}
	  
		public static float[][] doubleCenterMatrix(float matrix[][]){
			float res[][] = matrix.clone();
			
			int n = matrix.length;
			int m = matrix[0].length;
			float sumPerRow[] = new float[n];
			int countInRow[] = new int[n];
			float sumPerColumn[] = new float[m];
			int countInColumn[] = new int[m];
			int countTotal = 0;
			float totalSum = 0f;
			for(int i=0;i<n;i++){
				for(int j=0;j<m;j++)if(!Float.isNaN(matrix[i][j])){
					totalSum+=matrix[i][j];
					sumPerRow[i]+=matrix[i][j];
					countInRow[i]++;
					countTotal++;
				}
			}
			for(int j=0;j<m;j++){
				for(int i=0;i<n;i++)if(!Float.isNaN(matrix[i][j])){
					sumPerColumn[j]+=matrix[i][j];
					countInColumn[j]++;
				}
			}
			for(int i=0;i<n;i++){
				for(int j=0;j<m;j++){
					res[i][j] = matrix[i][j]-1/(float)countInRow[i]*sumPerRow[i]-1/(float)countInColumn[j]*sumPerColumn[j]+1/(float)countTotal*totalSum;
				}
			}
			
			return res;
		}

	
}

