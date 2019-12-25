package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;
import vdaoengine.analysis.*;

public class test1 {
  public static void main(String[] args) {

    try{
    	
      VDataTable tab = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/moduleactivities/data/bcpublic/wee1.dat");
      VDataSet vd1 = VSimpleProcedures.SimplyPreparedDataset(tab, -1);
      
      vd1.weighted = true;
      vd1.weights = new float[4];
      vd1.weights[0] = 10;
      vd1.weights[1] = 1;
      vd1.weights[2] = 1;
      vd1.weights[3] = 1;
      
      PCAMethod pca = new PCAMethod();
      pca.setDataSet(vd1);
      pca.calcBasis(1);
      
      VDataSet vdp = pca.getProjectedDataset(); 
      
		for(int j=0;j<vdp.pointCount;j++){
			float f[] = vdp.getVector(j);
			System.out.println(tab.stringTable[j][tab.fieldNumByName("GeneSymbol")]+"\t"+f[0]);
		}
		for(int k=0;k<vd1.coordCount;k++){
			System.out.println(tab.fieldNames[5+k]+"\t"+pca.linBasis.basis[0][k]);
		}
      
      
      System.exit(0);

      /*VDataTable tab = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/gsda/data/brain/gse1572.txt",true,"\t");
      for(int i=0;i<tab.rowCount;i++) for(int j=1;j<tab.colCount;j++){
        float f = Float.parseFloat(tab.stringTable[i][j]);
        if(f<0) f = 1f;
        tab.stringTable[i][j] = ""+Math.log(f);
      }
      vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(tab,"c:/datas/gsda/data/brain/gse1572l.txt");*/
      float x1[] = {0.1f,0.2f,0.5f};
      float x2[] = {3f,4f,5f};
      System.out.println(VSimpleFunctions.calcCorrelationCoeff(x1,x2));
      System.exit(0);

      VDataTable sco = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/kinetics/fit/scoresa.txt",true,"\t");
      VDataTable t618 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/kinetics/618genes_rs.txt",true,"\t");
      VDataTable sco618 = VSimpleProcedures.MergeTables(sco,"ID",t618,"ID","_");
      //vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(sco618,"c:/datas/ewing/kinetics/fit/scoresa_rs.txt");
      //VDataTable ann = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/annot4.txt",true,"\t");
      //VDataTable scoann = VSimpleProcedures.MergeTables(sco,"ID",ann,"Probe","_");
      vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(sco618,"c:/datas/ewing/kinetics/fit/scoresa_rs.txt");
      System.exit(0);


      VDataTable table = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/gsda/data/brain/gse1572.dat");
      VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(table,-1,true,true);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vd,"c:/datas/gsda/data/brain/gse1572_.dat");
      vd.calcStatistics();
      float ff[] = new float[vd.coordCount];
      for(int i=0;i<vd.coordCount;i++) ff[i] = vd.simpleStatistics.getMean(i);
      //vd.processIntoSpace(ff);
      for(int i=0;i<vd.coordCount;i++) System.out.print(ff[i]+"\t");
      System.out.println("\n"+VVectorCalc.Norm(ff));
      System.exit(0);

      float f[] = {1,2,3,4,5,6,7,8,9,1};
      //float f[] = {-1,0,1};
      VStatistics vst = new VStatistics(1);
      for(int i=0;i<f.length;i++){
        float f1[] = new float[1];
        f1[0] = f[i];
        vst.addNewPoint(f1);
      }
      vst.calculate();
      System.out.println(vst.getSkew(0)+"\t"+vst.getStdDev(0));

      //LoadData(true,args[0]);
      System.exit(0);

      LineNumberReader ln = new LineNumberReader(new FileReader("c:/datas/accmolecules"));
      VDataTable vt =  vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/tsc2nas.dat");
      String s = null;
      HashMap idname = new HashMap();
      HashMap nameid = new HashMap();
      System.out.println("Hashing names...");
      int kk=0;
      while((s=ln.readLine())!=null){
        if(kk==(int)(0.0001f*kk)*10000)
          System.out.print(kk+"\t");
        kk++;
        StringTokenizer st = new StringTokenizer(s,"\t");
        String cod = st.nextToken();
        if(st.hasMoreTokens()){
          String id = st.nextToken();
          String name = "";
          StringTokenizer st1 = new StringTokenizer(cod,":");
          while(st1.hasMoreTokens())
            name = st1.nextToken();
          if((name.length()<13)&&(name.length()>1)&&(!name.equals("Human"))&&(name.indexOf("Hs.")<0)){
            //
            Vector v = (Vector)idname.get(id);
            if(v==null)
              v = new Vector();
            if(v.indexOf(name)<0)
              v.add(name);
            idname.put(id,v);
            //
            v = (Vector)nameid.get(name);
            if(v==null)
              v = new Vector();
            if(v.indexOf(id)<0)
              v.add(id);
            nameid.put(name,v);
          }
        }
      }

      /*System.out.println();
      Vector test = (Vector)nameid.get("121_at");
      for(int i=0;i<test.size();i++){
        System.out.print((String)test.get(i)+"\t");
        Vector names = (Vector)idname.get((String)test.get(i));
        for(int j=0;j<names.size();j++)
          System.out.print((String)names.get(j)+"\t");
        System.out.println();
      }*/

      System.out.println("Writing table...");
      vt.addNewColumn("SYNONYM","","",vt.STRING,"_");
      for(int i=0;i<vt.rowCount;i++){
        if(i==(int)(0.001f*i)*1000)
          System.out.print(i+"\t");
        String id = vt.stringTable[i][vt.fieldNumByName("CHIP")];
        Vector vid = (Vector)nameid.get(id);;
        if(vid!=null){
        //System.out.print(vid.size()+" ");
        String syn = "";
        Vector vvv = new Vector();
        for(int k=0;k<vid.size();k++){
        Vector v = (Vector)idname.get(vid.get(k));
        if(v!=null){
          for(int j=0;j<v.size();j++){
            String ss = (String)v.get(j);
            if(vvv.indexOf(ss)<0)
              vvv.add(ss);
          }
        }
        }
        for(int j=0;j<vvv.size();j++)
            syn+=(String)vvv.get(j)+";";
        vt.stringTable[i][vt.fieldNumByName("SYNONYM")] = syn.substring(0,syn.length()-1);
        //System.out.println(vt.stringTable[i][vt.fieldNumByName("CHIP")]+"\t"+vt.stringTable[i][vt.fieldNumByName("SYNONYM")]);
        }
      }

      vdaoengine.data.io.VDatReadWrite.numberOfDigitsToKeep = 2;
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"c:/datas/ewing/tsc2nas1.dat");

    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void LoadData(boolean verbose, String dataset1) throws Exception{
    VDataTable expressionTable = null;
    StringBuffer resultText = new StringBuffer();
    if(expressionTable==null){
      if(verbose) System.out.println("Loading expression data...");
      String dat = VDownloader.downloadURL(dataset1);
      if(verbose) System.out.println("Loaded "+dat.length()+" bytes\n"+dat.substring(0,Math.min(100,dat.length())));
      if(verbose) System.out.println("Parsing data...");
      expressionTable = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFileAsString(dat,"expressionTable");
      if(verbose) System.out.println("Parsed "+expressionTable.rowCount+" rows,"+expressionTable.colCount+" columns");
    }
  }


}