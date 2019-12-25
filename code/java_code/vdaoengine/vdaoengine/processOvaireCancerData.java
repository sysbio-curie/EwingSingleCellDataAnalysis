package vdaoengine;

import java.util.*;
import java.io.*;
import java.awt.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.*;
import vdaoengine.special.*;
import javax.swing.*;


public class processOvaireCancerData {
  public static void main(String[] args) {
    try{

      /*VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ovaire/ov.txt",true,"\t");
      for(int i=0;i<vtt.rowCount;i++){
        for(int j=1;j<vtt.colCount;j++){
          float f = Float.parseFloat(vtt.stringTable[i][j]);
          f = (float)Math.log(f+2000f);
          vtt.stringTable[i][j] = ""+f;
          vtt.fieldTypes[j] = vtt.NUMERICAL;
        }
      }
      //vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(vtt,"c:/datas/gsda/data/brain/GSE1572.txt");
      VDataTable vttn = TableUtils.normalizeVDat(vtt,true,false);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vttn,"c:/datas/ovaire/ov.dat");
      System.exit(0);*/

      //String prefix = "c:/datas/bladdercancer/ica/A_symmtanh20";

      //String prefix = "c:/datas/ovaire/ovf";
      //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
      //TableUtils.printFieldInfoSummary(vt,1,0);
      
      /*VDataTable annt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/annot4.txt",true,"\t");
      VDataTable mt = vdaoengine.utils.VSimpleProcedures.MergeTables(vt,"ID",annt,"Probe","0");
      //VDataTable mt = vdaoengine.utils.VSimpleProcedures.MergeTables(vt,"SAMPLE",annt,"SAMPLE","0");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(mt,"c:/datas/ovaire/ovfa.dat");*/
      

      //ICAAnalyzer icaa = new ICAAnalyzer();
      //icaa.data = vt;
      //icaa.signalMatrix = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ovaire/signal.txt",false,"\t");
      //VDataTable vts = icaa.extractTable(4,3f,100f,"Probe");
      //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts,"c:/datas/bladdercancer/ica/s_symmtanh20_ica5.dat");
      //icaa.classifyByICALoading(vt,3,"ICACLASS");
      //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"c:/datas/bladdercancer/ica/s_symmtanh20_ica.dat");

      VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ovaire/signal.dat");
      vt = vt.transposeTable("COMP");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt, "c:/datas/ovaire/signalt.dat");
      Vector names = new Vector();
      names.add("D1");//names.add("D3");names.add("D5");names.add("D6");names.add("D7");names.add("D9");names.add("D12");names.add("D13");names.add("D15");
      TableUtils.printAssosiationTable(vt,"c:/datas/ovaire/signal.ast",names,0f,false);

      /*VDataTable vtrt = vt.transposeTable("Probe");
      VDataTable vtrPCA = TableUtils.PCAtable(vtrt,true);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtrPCA,prefix+"samplePCA_10000.dat");*/

      /*FileWriter fw = new FileWriter(prefix+"_p53.gmt");
      MetageneLoader ml = new MetageneLoader();
      ml.loadSignatures("c:/datas/bladdercancer/p53signatures/");
      for(int i=0;i<ml.listOfGESignatures.size();i++){
        GESignature gs = (GESignature)ml.listOfGESignatures.get(i);
        System.out.println(gs.name+":");
        for(int j=0;j<gs.probeSets.size();j++)
          System.out.print((String)gs.probeSets.get(j)+"\t");
        System.out.println();
        VDataTable vt1 = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(vt,gs.probeSets,"Probe");

        //vt1 = VSimpleProcedures.substituteRowsByTheMostVariable(vt1,"GeneSymbol");

        vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt1,prefix+"_"+gs.name+".dat");
        fw.write(gs.name+"\tna\t");
        for(int k=0;k<vt1.rowCount;k++)
          fw.write(vt1.stringTable[k][0]+"\t");
        fw.write("\n");
      }
      fw.close();*/

      //GMTReader.writeGCTandCLS(vt,prefix+"1",2,"Probe","GeneSymbol");


      //String prefix = "c:/datas/breastcancer/wang/wangn";

      //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(prefix+".csv",true,",");
      /*VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
      vdaoengine.data.io.VDatReadWrite.numberOfDigitsToKeep = 3;
      vdaoengine.data.io.VDatReadWrite.useQuotesEverywhere = false;
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,prefix+"2.dat");*/

      /*VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
      printUsedMemory();
      VDataTable vtr = TableUtils.normalizeVDat(vt,true,false);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtr,prefix+"n.dat");
      printUsedMemory();*/

      //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
      /*VDataTable vtr = TableUtils.filterByVariation(vt,5000,false);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtr,prefix+"f.dat");*/

      /*MetageneLoader ml = new MetageneLoader();
      ml.loadSignatures("c:/datas/bladdercancer/p53signatures/");
      GESignature gs = ml.getSignatureByName("MillerSmedsPNAS2005");

      for(int i=0;i<gs.probeSets.size();i++)
        System.out.println((String)gs.probeSets.get(i));

      VDataTable vt1 = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(vt,gs.probeSets,"CHIP");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt1,prefix+"fs.dat");*/

    }catch(Exception e){
      e.printStackTrace();
    }


  }

  public static void printUsedMemory(){
      long mem = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
      //long mem = Runtime.getRuntime().freeMemory();
      mem = (long)(1e-6f*mem);
      System.out.println("Used memory "+mem+"M");
  }


}