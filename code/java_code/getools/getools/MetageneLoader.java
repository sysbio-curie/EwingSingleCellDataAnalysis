package getools;

import java.util.*;
import java.io.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class MetageneLoader {

  Vector listOfGESignatures = new Vector();
  Vector<Metagene> listOfMetagenes = new Vector<Metagene>();
  VDataTable annotationTable = null;
  Vector listOfOutlierProbeSets = new Vector();
  Set<String> allProbeSets = new HashSet<String>();
  HashMap hm = null;

  public MetageneLoader() {
  }


  public void calcWeightsByPCA(GESignature gs, VDataTable vt, String probeID, int numPC){

    Vector mgl = new Vector();
    mgl.add(gs);
    VDataTable vts = selectSignatures(vt,mgl,probeID);
    String fname = vt.name; fname = fname.substring(0,fname.length()-4);

    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts,fname+"_"+gs.name+".dat");

    VDataSet vds = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vts,-1);
    Vector outl = determineOutliers(vds,probeID,3,3);
    VTableQuery qr = new VTableQuery();
    qr.table = vts;
    qr.hashTable(probeID);
    //System.out.println();    System.out.println();
    for(int i=0;i<outl.size();i++){
      String ps = (String)outl.elementAt(i);
      int k = qr.queryHash(ps);
      System.out.println("Outlier:\t"+gs.name+"\t"+ps+"\t"+vts.stringTable[k][vts.fieldNumByName("GeneSymbol")]+"\t"+vts.stringTable[k][vts.fieldNumByName("GeneTitle")]);
      listOfOutlierProbeSets.add(ps);
    }

    VDataTable vtmo = VTableSelector.excludeRows(vts,outl,probeID);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtmo,fname+"_"+gs.name+"o.dat");
    VDataTable vtmot = vtmo.transposeTable(probeID);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtmot,fname+"_"+gs.name+"ot.dat");
    VDataSet vdm = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vtmot,-1);
    VDataSet vdms = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vtmo,-1);
    PCAMethod PCA = new PCAMethod();
    PCA.setDataSet(vdm);
    PCA.calcBasis(numPC);
    PCAMethod PCAs = new PCAMethod();
    PCAs.setDataSet(vdms);
    PCAs.calcBasis(numPC);

    for(int kk=0;kk<numPC;kk++){
      Metagene mgn = new Metagene(gs);
      mgn.initializeWeightsByZeros();
      mgn.sampleNames = new Vector();
      mgn.samplePattern = new Vector();
      if(numPC>1)
          mgn.name = gs.name + "_" + (kk+1);
      else
          mgn.name = gs.name;
      int pid = vtmo.fieldNumByName(probeID);
      int pids = vtmot.fieldNumByName("NAME");
      for(int i=0;i<PCA.getBasis().basis[kk].length;i++){
        String ps = vtmo.stringTable[i][pid];
        int ii = mgn.probeSets.indexOf(ps);
        mgn.weights.setElementAt(new Float(PCA.getBasis().basis[kk][i]),ii);
      }
      for(int i=0;i<PCAs.getBasis().basis[kk].length;i++){
        String sample = vtmot.stringTable[i][pids];
        mgn.sampleNames.add(sample);
        mgn.samplePattern.add(new Float(PCAs.getBasis().basis[kk][i]));
      }
      listOfMetagenes.add(mgn);
    }
  }

  public VDataTable projectData(VDataTable vt, String id, int distType){ // distType 0 - Angle, 1 - P.correlation
    VDataTable vtr = new VDataTable();
    Vector stringNames = new Vector();
    Vector numNames = new Vector();
    for(int i=0;i<vt.colCount;i++){
      if(vt.fieldTypes[i]==vtr.NUMERICAL) numNames.add(vt.fieldNames[i]);
      if(vt.fieldTypes[i]==vtr.STRING) stringNames.add(vt.fieldNames[i]);
    }
    vtr.rowCount = vt.rowCount;
    vtr.colCount = this.listOfMetagenes.size()+stringNames.size();
    vtr.fieldNames = new String[vtr.colCount];
    vtr.fieldClasses = new String[vtr.colCount];
    vtr.fieldDescriptions = new String[vtr.colCount];
    vtr.fieldTypes = new int[vtr.colCount];
    vtr.stringTable = new String[vtr.rowCount][vtr.colCount];
    for(int i=0;i<stringNames.size();i++){
      String fn = (String)stringNames.elementAt(i);
      int fni = vt.fieldNumByName(fn);
      vtr.fieldNames[i] = fn;
      vtr.fieldTypes[i] = vtr.STRING;
      for(int j=0;j<vtr.rowCount;j++){
        vtr.stringTable[j][i] = vt.stringTable[j][fni];
      }
    }
    for(int i=0;i<listOfMetagenes.size();i++){
      Metagene mg = (Metagene)listOfMetagenes.elementAt(i);
      vtr.fieldNames[stringNames.size()+i] = mg.name;
      vtr.fieldTypes[stringNames.size()+i] = vtr.NUMERICAL;
    }
    //VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);

    Vector idVector = new Vector();
    Vector valVector = new Vector();

    for(int j=0;j<numNames.size();j++){
      String fn = (String)numNames.elementAt(j);
      idVector.add(fn);
    }

    for(int j=0;j<vtr.rowCount;j++){
      valVector.clear();
      float vec[] = vd.getVector(j);
      for(int k=0;k<vec.length;k++) valVector.add(new Float(vec[k]));
      for(int i=0;i<listOfMetagenes.size();i++){
        Metagene mg = (Metagene)listOfMetagenes.elementAt(i);
        float dd = 0f;
        switch(distType){
          case 0: dd = calcAngle(mg,idVector,valVector); break;
          case 1: dd = calcCorrelation(mg,idVector,valVector); break;
        }
        //vtr.stringTable[j][stringNames.size()+i] = ""+Math.abs(ang);
        vtr.stringTable[j][stringNames.size()+i] = ""+dd;
      }
    }
    return vtr;
  }

  public float calcAngle(Metagene mg,Vector idVector,Vector valVector){
    float res = 0f;
    float sumw = 0f;
    float sumv = 0f;
    float w = 0f;
    float v = 0f;
    for(int i=0;i<idVector.size();i++){
      String id = (String)idVector.elementAt(i);
      int k = mg.probeSets.indexOf(id);
      if(k>=0){
        w = ((Float)mg.weights.elementAt(k)).floatValue();
        v = ((Float)valVector.elementAt(i)).floatValue();
        res+=v*w;
        sumw+=w*w;
        sumv+=v*v;
      }
    }
    return res/(float)Math.sqrt(sumw)/(float)Math.sqrt(sumv);
  }

  public float calcCorrelation(Metagene mg,Vector idVector,Vector valVector){
    float res = 0f;
    float sumw = 0f;
    float sumv = 0f;
    float sumw2 = 0f;
    float sumv2 = 0f;
    float w = 0f;
    float v = 0f;
    float N = 0f;
    for(int i=0;i<idVector.size();i++){
      String id = (String)idVector.elementAt(i);
      int k = mg.probeSets.indexOf(id);
      if(k>=0){
        w = ((Float)mg.weights.elementAt(k)).floatValue();
        v = ((Float)valVector.elementAt(i)).floatValue();
        res+=v*w;
        sumw2+=w*w;
        sumv2+=v*v;
        sumw+=w;
        sumv+=v;
        N+=1f;
      }
    }
    //return res/(float)Math.sqrt(sumw)/(float)Math.sqrt(sumv);
    res = (float)((res-sumw*sumv/N)/Math.sqrt((sumw2-sumw*sumw/N)*(sumv2-sumv*sumv/N)));
    return res;
  }


  public Vector SignatureLookUp(String id){
    Vector v = new Vector();
    for(int i=0;i<listOfGESignatures.size();i++){
      GESignature mg = (GESignature)listOfGESignatures.elementAt(i);
      int k = -1;
      for(int j=0;j<mg.probeSets.size();j++){
        String s = (String)mg.probeSets.elementAt(j);
        if(s.equals(id)){ k=j;  break; }
      }
      if(k>=0) v.add(mg);
    }
    return v;
  }

  public Metagene getMetageneByName(String nam){
    Metagene mgr = null;
    for(int i=0;i<listOfMetagenes.size();i++){
      Metagene mg = (Metagene)listOfMetagenes.elementAt(i);
      if(mg.name.equals(nam))
        mgr = mg;
    }
    return mgr;
  }

  public GESignature getSignatureByName(String nam){
    GESignature mgr = null;
    for(int i=0;i<listOfGESignatures.size();i++){
      GESignature mg = (GESignature)listOfGESignatures.elementAt(i);
      if(mg.name.equals(nam))
        mgr = mg;
    }
    return mgr;
  }

  public VDataTable selectSignatures(VDataTable vt, Vector mgList, String probeID){
    VDataTable vtr = null;
    Vector all = new Vector();
    for(int i=0;i<mgList.size();i++){
      GESignature mg = (GESignature)mgList.elementAt(i);
      for(int j=0;j<mg.probeSets.size();j++){
        if(all.indexOf((String)mg.probeSets.elementAt(j))==-1)
          all.add(mg.probeSets.elementAt(j));
      }
    }
    int pID = vt.fieldNumByName(probeID);
    if(pID>=0){
      vtr = new VDataTable();
      vtr.copyHeader(vt);
      VTableQuery vq = new VTableQuery();
      vq.table = vt;
      vq.hashTable(probeID);

      Vector rows = new Vector();

      for(int i=0;i<all.size();i++){
        int k = vq.queryHash((String)all.elementAt(i));
        if(k>=0){
          rows.add(vt.getRow(k));
        }else{
          Vector v = SignatureLookUp((String)all.elementAt(i));
          //System.out.println((String)all.elementAt(i)+"("+((Metagene)v.elementAt(0)).name+") was not found in the table "+vt.name);
        }
      }

      vtr.rowCount = rows.size();
      vtr.stringTable = new String[vtr.rowCount][vtr.colCount];
      for(int i=0;i<rows.size();i++)
        vtr.stringTable[i] = (String[])rows.elementAt(i);
    }

    return vtr;
  }

  public int metaGeneByName(String name){
    int num = -1;
    for(int i=0;i<listOfMetagenes.size();i++){
      Metagene mg = (Metagene)listOfMetagenes.elementAt(i);
      if(mg.name.equals(name)) num=i;
    }
    return num;
  }

  public void mapMetagenesOnData(String fin,String fout,String ann,String psName){
    VDataTable annt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(ann,true,"\t");
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(fin);
    VDataTable mt = vdaoengine.utils.VSimpleProcedures.MergeTables(vt,psName,annt,"Probe","0");
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(mt,fout);
  }

  public void printIntersectionTable(String fname){
    Vector all = new Vector();
    for(int i=0;i<listOfGESignatures.size();i++){
      GESignature mg = (GESignature)listOfGESignatures.elementAt(i);
      for(int j=0;j<mg.probeSets.size();j++){
        if(all.indexOf((String)mg.probeSets.elementAt(j))==-1)
          all.add(mg.probeSets.elementAt(j));
      }
    }
    try{
    FileWriter fw = new FileWriter(fname);
    fw.write("Probe\t");
    for(int i=0;i<listOfGESignatures.size();i++){
      GESignature mg = (GESignature)listOfGESignatures.elementAt(i);
      fw.write(mg.name+"\t");
    }
    fw.write("Signatures\r\n");
    for(int i=0;i<all.size();i++){
      String ps = (String)all.elementAt(i);
      fw.write(ps+"\t");
      String names = "";
      for(int j=0;j<listOfGESignatures.size();j++){
        GESignature mg = (GESignature)listOfGESignatures.elementAt(j);
        String f = "0";
        if(mg.probeSets.indexOf(ps)!=-1) { f = "1"; names+=mg.name+","; }
        fw.write(f+"\t");
      }
      names=names.substring(0,names.length()-1);
      fw.write(names+"\r\n");
    }
    fw.close();
    }catch(Exception e){
       e.printStackTrace();
    }
  }

  public void loadSignatures(String dirname){
    File f = new File(dirname);
    File fa[] = f.listFiles();
    for(int i=0;i<fa.length;i++){
      File ff = fa[i];
      VDataTable sg = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(ff.getAbsolutePath(),false,"\t");;
      GESignature mg = new GESignature();
      for(int j=0;j<sg.rowCount;j++)
        mg.probeSets.add(sg.stringTable[j][0]);
      mg.name = ff.getName();
      listOfGESignatures.add(mg);
    }
    System.out.print("Loaded: ");
    for(int i=0;i<listOfGESignatures.size();i++){
      GESignature mg = (GESignature)listOfGESignatures.elementAt(i);
      System.out.print(mg.name+" ");
    }
    System.out.println();
  }

  public void loadMetagenes(String dirname){
	allProbeSets.clear();
    File f = new File(dirname);
    File fa[] = f.listFiles();
    for(int i=0;i<fa.length;i++)if(!fa[i].isDirectory()){
      File ff = fa[i];
      VDataTable sg = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(ff.getAbsolutePath(),false,"\t");;
      Metagene mg = new Metagene();
      mg.weights = new Vector();
      for(int j=0;j<sg.rowCount;j++){
        mg.probeSets.add(sg.stringTable[j][0]);
        mg.weights.add(new Float(Float.parseFloat(sg.stringTable[j][1])));
        allProbeSets.add(sg.stringTable[j][0]);
      }
      mg.name = ff.getName();
      listOfMetagenes.add(mg);

      GESignature gs = new GESignature();
      for(int j=0;j<sg.rowCount;j++)
        gs.probeSets.add(sg.stringTable[j][0]);
      gs.name = ff.getName();
      listOfGESignatures.add(gs);
    }
    System.out.print("Loaded: ");
    for(int i=0;i<listOfMetagenes.size();i++){
      Metagene mg = (Metagene)listOfMetagenes.elementAt(i);
      System.out.print(mg.name+" ");
    }
    System.out.println();
  }


  public void annotateList(String fname){
    for(int i=0;i<listOfMetagenes.size();i++){
      Metagene mg = (Metagene)listOfMetagenes.elementAt(i);
      annotate(mg,fname);
    }
  }

  public void annotate(GESignature ges, String fname){

    int pid = -1;

    if(annotationTable==null){
         annotationTable = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(fname,true,"\t");
         pid = annotationTable.fieldNumByName("Probe");
         hm = new HashMap();
         for(int j=0;j<annotationTable.rowCount;j++){
           String ps = (String)annotationTable.stringTable[j][pid];
           hm.put(ps,new Integer(j));
         }
    }

    int gid = annotationTable.fieldNumByName("GeneTitle");
    int gsid = annotationTable.fieldNumByName("GeneSymbol");
    int lid = annotationTable.fieldNumByName("Location");
    for(int i=0;i<ges.probeSets.size();i++){
      String ps = (String)ges.probeSets.elementAt(i);
      boolean found = false;
      Integer J = (Integer)hm.get(ps);
      int j = -1;
      if(J!=null) j = J.intValue();
        ges.geneNames.add("NA");
        ges.locusPosition.add("NA");
        ges.annotation.add("NA");
        if(j!=-1){
          ges.geneNames.setElementAt(annotationTable.stringTable[j][gsid],i);
          ges.annotation.setElementAt(annotationTable.stringTable[j][gid],i);
          ges.locusPosition.setElementAt(annotationTable.stringTable[j][lid],i);
          found = true;
        }
    }
  }

  public static Vector determineOutliers(VDataSet vd, String probeID, double thresh, int comp){
    Vector vr = new Vector();
    double dt[][] = new double[vd.pointCount][vd.pointCount];

    PCAMethod PCA = new PCAMethod();
    PCA.setDataSet(vd);
    PCA.calcBasis(comp);
    VDataSet vdp = PCA.getProjectedDataset();

    VStatistics sgn = new VStatistics(1);
    float d[] = new float[1];
    for(int i=0;i<vd.pointCount;i++)
      for(int j=i+1;j<vd.pointCount;j++){
         float xi[] = vdp.getVector(i);
         float xj[] = vdp.getVector(j);
         dt[i][j] = 0;
         for(int k=0;k<xi.length;k++) dt[i][j]+=(xi[k]-xj[k])*(xi[k]-xj[k]);
         dt[i][j] = Math.sqrt(dt[i][j]);
         d[0] = (float)dt[i][j];
         sgn.addNewPoint(d);
      }
    sgn.calculate();

    int pid = vd.table.fieldNumByName(probeID);
    for(int i=0;i<vd.pointCount;i++){
      VStatistics vst = new VStatistics(1);
      for(int j=0;j<vd.pointCount;j++) if(i!=j){
        d[0] = (float)(dt[i][j]+dt[j][i]);
        vst.addNewPoint(d);
      }
      vst.calculate();
      float z = Math.abs((vst.getMean(0)-sgn.getMean(0))/sgn.getStdDev(0));
      float x[] = vdp.getVector(i);
      float s = 0f;
      for(int k=1;k<x.length;k++) s+=Math.abs(x[k]);
      //z = z*s/Math.abs(x[0]);
      if(z>thresh){
        vr.add(vd.table.stringTable[i][pid]);
        //System.out.println(vd.table.stringTable[i][pid]+"\t"+z);
      }
    }
    return vr;
  }

  public void Reshuffle(int num, Random r){
    for(int i=0;i<num;i++){
      int n = listOfMetagenes.size();
      int ii = (int)(r.nextFloat()*n);
      int jj = (int)(r.nextFloat()*n);
      int iin = (int)(r.nextFloat()*((Metagene)listOfMetagenes.elementAt(ii)).probeSets.size());
      int jjn = (int)(r.nextFloat()*((Metagene)listOfMetagenes.elementAt(jj)).probeSets.size());
      String si = (String)((Metagene)listOfMetagenes.elementAt(ii)).probeSets.elementAt(iin);
      String sj = (String)((Metagene)listOfMetagenes.elementAt(jj)).probeSets.elementAt(jjn);
      ((Metagene)listOfMetagenes.elementAt(ii)).probeSets.setElementAt(sj,iin);
      ((Metagene)listOfMetagenes.elementAt(jj)).probeSets.setElementAt(si,jjn);
    }
  }

  public void printAssosiationTable(VDataTable vt, String fout, Vector strNames, float thresh, boolean transposed){
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

  public float[][] findAssosiationsInDataTable(VDataTable vt, Vector strNames){
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

  public float[][] findAssosiations(Vector fieldNames, Vector fieldClasses, Vector valNames, Vector vals){
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

  public void printAssociations(float vals[][], Vector fieldNames, Vector valNames, String fout, float thresh){
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

  public void printAssociationsT(float vals[][], Vector fieldNames, Vector valNames, String fout, float thresh){
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
    r = (stat1.getMean(0)-stat2.getMean(0))/Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)+stat2.getStdDev(0)*stat2.getStdDev(0));
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

  public VDataTable OutlierTable(VDataTable vt, String probeID){
    GESignature outliers =  new GESignature();
    outliers.name = "outliers";
    outliers.probeSets = listOfOutlierProbeSets;
    Vector outll = new Vector();
    outll.add(outliers);
    VDataTable outlt = selectSignatures(vt,outll,probeID);
    return outlt;
  }

  public Vector getExtremalPoints(Metagene mg, VDataTable vt, String probeID, String gsID, float thresh){
    Vector r = new Vector();
    VDataSet vds = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    VStatistics stat = new VStatistics(1);
    int pid = vt.fieldNumByName(probeID);
    int spid = vt.fieldNumByName(gsID);
    float fmg[] = new float[vds.coordCount];
    for(int i=0;i<mg.sampleNames.size();i++){
      String sn = (String)mg.sampleNames.elementAt(i);
      int id = vt.fieldNumByName(sn);
      if(id!=-1){
        int ids = -1;
        for(int j=0;j<vds.selector.selectedColumns.length;j++)
          if(vds.selector.selectedColumns[j]==id)
            ids = j;
        if(ids!=-1)
          fmg[ids] = ((Float)(mg.samplePattern.elementAt(i))).floatValue();
      }
    }
    float d[] = new float[1];
    for(int i=0;i<vds.pointCount;i++){
      float f[] = vds.getVector(i);
      d[0] = 0f;
      for(int j=0;j<f.length;j++)
        d[0]+=f[j]*fmg[j];
      stat.addNewPoint(d);
    }
    stat.calculate();
    for(int i=0;i<vds.pointCount;i++){
      float f[] = vds.getVector(i);
      d[0] = 0f;
      for(int j=0;j<f.length;j++)
        d[0]+=f[j]*fmg[j];
      float zscore = (d[0]-stat.getMean(0))/stat.getStdDev(0);
      if(Math.abs(zscore)>thresh) {
          r.add(vt.stringTable[i][pid]);
          //System.out.println(mg.name+"\t"+vt.stringTable[i][pid]+"\t"+vt.stringTable[i][spid]+"\t"+zscore);
      }
    }

    return r;
  }

  public static HashMap hashAnnotation(String annotf, boolean convertToLowerCase){
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(annotf,true,"\t");
    //int id = vt.fieldNumByName("Probe Set ID");
    //int gs = vt.fieldNumByName("Gene Symbol");
    //int gt = vt.fieldNumByName("Gene Title");
    int id = vt.fieldNumByName("Probe");
    int gs = vt.fieldNumByName("GeneSymbol");
    int gt = vt.fieldNumByName("GeneTitle");

    HashMap hm = new HashMap();
    for(int i=0;i<vt.rowCount;i++){
      String geneName = vt.stringTable[i][gs];
      if(convertToLowerCase)
        geneName = geneName.toLowerCase();
      String ids = vt.stringTable[i][id];
      GESignature gsig = (GESignature)hm.get(geneName);
      if(gsig==null){
        gsig = new GESignature();
        gsig.name = geneName;
        gsig.description = vt.stringTable[i][gt];
      }
      gsig.probeSets.add(vt.stringTable[i][id]);
      hm.put(geneName,gsig);
    }
    return hm;
  }

  public static VDataTable classifyByCorrelation(VDataTable vt){
    VDataTable res = new VDataTable();
    res.copyHeader(vt);
    res.stringTable = new String[res.rowCount][res.colCount];
    res.addNewColumn("CLASS","","",VDataTable.STRING,"_");
    int idc = res.fieldNumByName("CLASS");
    for(int i=0;i<vt.rowCount;i++){
      float maxc = -1; String maxs = "Not_assigned";
      for(int j=0;j<vt.colCount;j++){
        //if(res.fieldTypes[j]==res.STRING)
           res.stringTable[i][j] = vt.stringTable[i][j];
        if(res.fieldTypes[j]==res.NUMERICAL){
           float x = Float.parseFloat(vt.stringTable[i][j]);
           if(x>maxc)
             { maxc = x; maxs = res.fieldNames[j]; }
        }
      }
      if(maxc>0.1)
        res.stringTable[i][idc] = maxs;
    }
    return res;
  }

}

