package getools;

import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;


public class MetageneAnalyzer {

  public static void main(String args[]){

    //signatureAnalysis();

    metageneAnalysis();

  }



public static void signatureAnalysis(){

MetageneLoader ml = new MetageneLoader();
ml.loadSignatures("c:/datas/breastcancer/signatures");
ml.annotateList("c:/datas/annot3.txt");
VDataTable vts = null;

//String prefix = "c:/datas/breastcancer/onlyA/an_a";
String prefix = "c:/datas/breastcancer/onlyA/an_a_intrinsic_normal";

// make intersection table
ml.printIntersectionTable("c:/datas/breastcancer/metagene_intersection.xls");
ml.mapMetagenesOnData(prefix+".dat",prefix+"m.dat","c:/datas/breastcancer/metagene_intersection.xls","CHIP");

// annotate table with signatures
Vector mgl = new Vector();
VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");;
for(int i=0;i<ml.listOfGESignatures.size();i++)
  mgl.add((GESignature)ml.listOfGESignatures.elementAt(i));
vts = ml.selectSignatures(vt1,mgl,"CHIP");
vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts,prefix+"ms.dat");
ml.mapMetagenesOnData(prefix+"ms.dat",prefix+"msm.dat","c:/datas/breastcancer/metagene_intersection.xls","CHIP");



// analyze one signature, detect and exclude outliers (in 3D), calculate weights as 1st PCA
Random r = new Random();
//ml.Reshuffle(10000,r);
vts = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+"msm.dat");
for(int i=0;i<ml.listOfGESignatures.size();i++){
  //Metagene mg = ml.getMetageneByName("E2F3");
  GESignature gs = (GESignature)ml.listOfGESignatures.elementAt(i);
  ml.calcWeightsByPCA(gs,vts,"CHIP",2);
}

VDataTable vtst = vts.transposeTable("CHIP");
vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtst,prefix+"msmt.dat");
VDataTable vtp = ml.projectData(vtst,"NAME",0);
vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtp,prefix+"msmp.dat");

Vector nms = new Vector();
nms.add("D1"); nms.add("D2"); nms.add("D3");
nms.add("D4"); nms.add("D5"); nms.add("D6");
nms.add("D7"); nms.add("D8"); nms.add("D9");
nms.add("D10"); nms.add("D11"); nms.add("D12");
nms.add("D13"); nms.add("D14");
ml.printAssosiationTable(vtp,prefix+"msmp_associations.xls",nms,0.8f,false);

VDataTable outl = ml.OutlierTable(vts,"CHIP");
VDataTable outlt = outl.transposeTable("CHIP");
vdaoengine.data.io.VDatReadWrite.saveToVDatFile(outlt,prefix+"_outliers.dat");
ml.printAssosiationTable(outlt,prefix+"_outliers_associations.xls",nms,0.8f,true);

System.out.println();

VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+"m.dat");
Vector listOfExtremalPoints = new Vector();
System.out.print("Extremals:");
for(int i=0;i<ml.listOfMetagenes.size();i++){
  Metagene mg = (Metagene)ml.listOfMetagenes.elementAt(i);
  System.out.print("\t"+mg.name);
  Vector extp = ml.getExtremalPoints(mg,vt,"CHIP","GeneSymbol",10f);
  for(int j=0;j<extp.size();j++){
    String ps = (String)extp.elementAt(j);
    if(listOfExtremalPoints.indexOf(ps)<0)
        listOfExtremalPoints.add(ps);
  }
}
System.out.print("\r\n");
GESignature extremals = new GESignature();
extremals.probeSets = listOfExtremalPoints;
Vector extl = new Vector();
extl.add(extremals);
VDataTable vte = ml.selectSignatures(vt,extl,"CHIP");
VDataTable vtet = vte.transposeTable("CHIP");
vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vte,prefix+"_extremals.dat");
vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtet,prefix+"_extremals_t.dat");


  }


  public static void metageneAnalysis(){

    /*MetageneLoader ml = new MetageneLoader();
    ml.loadMetagenes("c:/datas/breastcancer/metagenes");
    ml.annotateList("c:/datas/annot3.txt");
    VDataTable vts = null;
    //String prefix = "c:/datas/breastcancer/onlyA/an_a";
    String prefix = "c:/datas/breastcancer/onlyA/an_a_intrinsic_normal";

    Vector mgl = new Vector();
    VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");;
    for(int i=0;i<ml.listOfGESignatures.size();i++)
      mgl.add((GESignature)ml.listOfGESignatures.elementAt(i));
    vts = ml.selectSignatures(vt1,mgl,"CHIP");
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts,prefix+"ms.dat");

    VDataTable vtst = vts.transposeTable("CHIP");
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtst,prefix+"msmt.dat");
    VDataTable vtp = ml.projectData(vtst,"NAME",1);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtp,prefix+"msmp.dat");

    VDataTable vtp_class = ml.classifyByCorrelation(vtp);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtp_class,prefix+"msmp_class.dat");

    Vector nms = new Vector();
    nms.add("D1"); nms.add("D2"); nms.add("D3");
    nms.add("D4"); nms.add("D5"); nms.add("D6");
    nms.add("D7"); nms.add("D8"); nms.add("D9");
    nms.add("D10"); nms.add("D11"); nms.add("D12");
    nms.add("D13"); nms.add("D14");
    ml.printAssosiationTable(vtp,prefix+"msmp_associations.xls",nms,0.4f,false);*/

    //vdaoengine.utils.VSimpleProcedures.SeparateTableByField("c:/datas/breastcancer/onlya/an_a.dat",13);

    //String s = "intrinsic_luma";
    //VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/breastcancer/onlya/an_a_"+s+".dat");
    //GMTReader.writeGCTandCLS(vtt,"c:/datas/breastcancer/onlya/an_a_"+s,0,"CHIP","GeneSymbol");

  }



}