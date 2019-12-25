package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;


public class GSDASignatureAnalysis extends GSDA{

  String signatureName = null;
  String outFile = null;
  VDataTable initialTable = null;
  VDataTable filteredTable = null;
  Vector filteredGenes = new Vector();
  float geneweights[] = null;


  public static void main(String[] args) {


    try{
    GSDASignatureAnalysis gsda = new GSDASignatureAnalysis();

    for(int i=0;i<args.length;i++){
      if(args[i].equals("-data")){
        gsda.data = args[i+1];
      }
      if(args[i].equals("-sigdb"))
        gsda.sigdb = args[i+1];
      if(args[i].equals("-annot"))
        gsda.annotfile = args[i+1];
      if(args[i].equals("-sig")){
        gsda.signatureName = args[i+1];
      }
      if(args[i].equals("-out"))
        gsda.outFile = args[i+1];
    }

    if(gsda.outFile==null)
      gsda.outFile = gsda.data+"_"+gsda.signatureName;


    gsda.loadAnnotation();
    gsda.loadData();
    gsda.loadSignatures();
    gsda.calcGlobalDisplacement();

    GESignature sig = gsda.metagenes.getSignatureByName(gsda.signatureName);
    int n = sig.geneNames.size();
    gsda.analyzeTable(sig);
    
    Vector<Float> sampleAssociationsScores = new Vector<Float>();
    Vector<Double> sampleAssociationsPValues = new Vector<Double>();      
    double scores[] = gsda.getSignatureScores(sig,sampleAssociationsScores,sampleAssociationsPValues);

    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(gsda.initialTable,gsda.outFile+".dat");
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(gsda.filteredTable,gsda.outFile+"_f.dat");
    TableUtils.reformatForEisenCluster(gsda.filteredTable,"GeneSymbol",gsda.outFile+".cluster",false);

    FileWriter fw = new FileWriter(gsda.outFile);
    fw.write("Signature "+gsda.signatureName+"\nsize: "+n+"\nfound:"+gsda.initialTable.rowCount+"\nfiltered: "+(gsda.initialTable.rowCount-gsda.filteredTable.rowCount)+" genes\n");
    fw.write("\nFiltered:\n");
    for(int i=0;i<gsda.filteredGenes.size();i++){
      String probe = (String)gsda.filteredGenes.get(i);
      fw.write(probe+"\n");
    }

    fw.write("\nE1\t"+scores[0]+"\n");
    fw.write("GAP1\t"+scores[1]+"\n");
    fw.write("DIFF12\t"+scores[2]+"\n");
    fw.write("GAP1S\t"+scores[3]+"\n");
    fw.write("GAP2\t"+scores[4]+"\n");

    fw.close();

    }catch(Exception e){
      e.printStackTrace();
    }

  }

  public void analyzeTable(GESignature sig){
    initialTable = VSimpleProcedures.selectRowsFromList(table,sig.geneNames);
    initialTable = vdaoengine.utils.VSimpleProcedures.substituteRowsByStatistics(initialTable,"GeneSymbol",4);
    VDataSet ds = VSimpleProcedures.SimplyPreparedDataset(initialTable,-1);
    filteredGenes = MetageneLoader.determineOutliers(ds,"GeneSymbol",outlierThreshold,outlierDimension);
    for(int j=0;j<filteredGenes.size();j++)
      sig.geneNames.remove((String)filteredGenes.get(j));
    filteredTable = VSimpleProcedures.selectRowsFromList(table,sig.geneNames);
    filteredTable = vdaoengine.utils.VSimpleProcedures.substituteRowsByStatistics(filteredTable,"GeneSymbol",4);
  }


}