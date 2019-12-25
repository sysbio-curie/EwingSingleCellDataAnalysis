package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class GSDA {

  private class Association{
	  boolean continuos = false;
	  String fieldName = "";
	  Vector labelsClass1 = new Vector();
	  Vector labelsClass2 = new Vector();
	  float contValues[] = null;
	  Vector indClass1 = new Vector();
	  Vector indClass2 = new Vector();
	  //Vector indContinuos = new Vector();
  }

  VDataTable table = null;
  VDataTable sampleAnnotation = null;
  VDataTable annotation = null;
  VDataTable restable = null;
  HashMap probeToName = new HashMap();
  HashMap nameToProbe = new HashMap();
  MetageneLoader metagenes = new MetageneLoader();
  
  //Parameters  
  String dat = "";
  String data = "";
  String sigdb = "";
  String resfile = "";
  String annotfile = "";
  int minsetsize = 10;
  int numberofresampling = 100;
  int samplingGeneSetSizes[] = {10,15,20,30,40,50,60,70,80,90,100,200};
  boolean onlySampling = false;

  String resTableFile = null;
  String sampleAnnotationFile = null;
  Vector sampleAssociationsDescriptions = new Vector();
  Vector<Association> sampleAssociations = new Vector();   

  Vector<Float> associationFirstPCAScores = new Vector<Float>();
  Vector<Double> associationFirstPCAPValues = new Vector<Double>();  
  Vector<Float> associationFirstPCAFixedScores = new Vector<Float>();
  Vector<Double> associationFirstPCAFixedPValues = new Vector<Double>();
  
  // Internal parameters
  float outlierThreshold = 1.2f;
  int outlierDimension = 3;
  
  int numberOfScores = 22;

  Vector sizes = new Vector();

  VDataSet globalDataset = null;
  float globalDisplacement[] = null;
  PCAMethod globalPCA = null;

  boolean useZscores = true;
  
  boolean calcAlternativeDimensions = false;
  

  public static void main(String[] args) {

    try{


    GSDA gsda = new GSDA();

    for(int i=0;i<args.length;i++){
      if(args[i].equals("-data"))
        gsda.data = args[i+1];
      if(args[i].equals("-dat"))
          gsda.dat = args[i+1];
      if(args[i].equals("-sigdb"))
        gsda.sigdb = args[i+1];
      if(args[i].equals("-out"))
        gsda.resfile = args[i+1];
      if(args[i].equals("-minsize"))
        gsda.minsetsize = (new Integer(args[i+1])).intValue();
      if(args[i].equals("-annot"))
        gsda.annotfile = args[i+1];
      if(args[i].equals("-alpha"))
        gsda.outlierThreshold = (new Integer(args[i+1])).intValue();
      if(args[i].equals("-k"))
        gsda.outlierDimension = (new Integer(args[i+1])).intValue();
      if(args[i].equals("-so"))
      	gsda.onlySampling = true;
      if(args[i].equals("-sannot"))
    	gsda.sampleAnnotationFile = args[i+1];
      if(args[i].equals("-sassd"))
      	gsda.sampleAssociationsDescriptions.add("d_"+args[i+1]);
      if(args[i].equals("-sassc"))
        gsda.sampleAssociationsDescriptions.add("c_"+args[i+1]);
      if(args[i].equals("-nos"))
          gsda.numberofresampling = (new Integer(args[i+1])).intValue();
    }

    gsda.loadAnnotation();
    gsda.loadData();
    gsda.loadSampleAnnotation();
    

    /*System.out.println(gsda.probeToName.size());
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(gsda.table,"c:/datas/gsda/temp.dat");
    Vector tt = new Vector(); tt.add("ANXA7");
    VDataTable vts = VSimpleProcedures.selectRowsFromList(gsda.table,tt);
    System.out.println("Found "+vts.rowCount);
    System.exit(0);*/

    gsda.loadSignatures();

    gsda.selectOnlySignatureGenes();
    //gsda.savePathwayAnnotations();

    gsda.calcGlobalDisplacement();
    
    if(gsda.sampleAssociations!=null)
    	gsda.calcGlobalAssociations();


    if(!gsda.onlySampling){
    
    Date tm = new Date();
    
    Vector scores = gsda.scanSignatures();
    
    FileWriter fw = new FileWriter(gsda.resfile);
    
    gsda.writeResultHeader(fw);
    
    Vector vtemp = gsda.metagenes.listOfGESignatures;
    gsda.loadSignatures();
    
    int k=0;
    for(int i=0;i<gsda.metagenes.listOfGESignatures.size();i++){
      GESignature sig = (GESignature)gsda.metagenes.listOfGESignatures.get(i);
      //GESignature sigpruned = (GESignature)vtemp.get(i);
      k = gsda.writeScores(fw,sig,scores,i,k);
    }
    fw.close();
    
    VDataTable vtres = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(gsda.resfile, true, "\t");
    for(int i=1;i<vtres.colCount;i++)
    	vtres.fieldTypes[i] = vtres.NUMERICAL;
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtres, gsda.resfile+".dat");

    System.out.println("Time spent: "+((new Date()).getTime()-tm.getTime()));
    }
    
    // Sampling part...
    
    if(gsda.numberofresampling>0){
    	System.out.println("\n\nStarting sampling...\n\n");
        Date tm = new Date();
        gsda.restable = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(gsda.resfile, true, "\t");
        
        String scorefile = gsda.resfile.substring(0,gsda.resfile.length()-4)+"_scores."+gsda.resfile.substring(gsda.resfile.length()-3,gsda.resfile.length());
    	FileWriter fw = new FileWriter(scorefile);
    	gsda.writeResultHeader(fw);        
        
        for(int size=0;size<gsda.samplingGeneSetSizes.length;size++){
            int sizev = gsda.samplingGeneSetSizes[size];
            Vector scores = gsda.calcSampling(sizev,fw);
            int k=0;
            for(int i=0;i<gsda.metagenes.listOfGESignatures.size();i++){
                GESignature sig = (GESignature)gsda.metagenes.listOfGESignatures.get(i);
                k = gsda.writeScores(fw,sig,scores,i,k);
              }
        }
        fw.close();
        
        System.out.println("Time spent: "+((new Date()).getTime()-tm.getTime()));
        
        System.out.println("\nCalculating p-values...\n");
        VDataTable scoreTable = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(scorefile, true, "\t");;
        gsda.calcPValues(scoreTable);
        
        String pvaluefile = gsda.resfile.substring(0,gsda.resfile.length()-4)+"_pvalue."+gsda.resfile.substring(gsda.resfile.length()-3,gsda.resfile.length());

        vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(gsda.restable, pvaluefile);        
        
    }

    }catch(Exception e){
      e.printStackTrace();
    }



  }

  public void loadData(){
	if(!dat.equals("")){
	    System.out.print("Loading data... ");	
	    table = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(dat);
	    table.makePrimaryHash("GeneSymbol");	    
	    System.out.println("Done.");
	    vdaoengine.utils.Utils.printUsedMemory();
	}
	
	if(!data.equals("")){
    System.out.print("Loading data... ");
    table = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(data,true,"\t");
    System.out.println(table.rowCount+"x"+table.colCount+" loaded.");
    vdaoengine.utils.Utils.printUsedMemory();
    System.out.print("Processing data... ");
    for(int i=1;i<table.colCount;i++)
      table.fieldTypes[i] = table.NUMERICAL;
    for(int i=0;i<table.rowCount;i++){
      if(i==(int)(0.001f*i)*1000)
        System.out.print(i+"\t");
      float f[] = new float[table.colCount-1];
      float sum = 0f;
      for(int j=1;j<table.colCount;j++){
        f[j-1] = Float.parseFloat(table.stringTable[i][j]);
        sum+=f[j-1];
      }
      for(int j=1;j<table.colCount;j++)
        f[j-1] = f[j-1]-sum/(table.colCount-1);
      DecimalFormat nf = new DecimalFormat("#.###");
      for(int j=1;j<table.colCount;j++)
        //table.stringTable[i][j] = new String(nf.format(f[j-1]));
        table.stringTable[i][j] = new String(Float.toString(f[j-1]));
    }
    if(annotation!=null){
      table.addNewColumn("GeneSymbol","","",table.STRING,"_");
      for(int i=0;i<table.rowCount;i++){
        String probe = table.stringTable[i][table.fieldNumByName("ID")];
        table.stringTable[i][table.fieldNumByName("GeneSymbol")] = (String)probeToName.get(probe);
      }
      table.makePrimaryHash("GeneSymbol");
    }
    System.out.println();
    System.out.println("Done.");
    vdaoengine.utils.Utils.printUsedMemory();
	}
  }

  public void loadAnnotation(){
	if(!annotfile.equals("")){
    annotation = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(annotfile,true,"\t");
    for(int i=0;i<annotation.rowCount;i++){
      String probe = annotation.stringTable[i][annotation.fieldNumByName("Probe")];
      String name = annotation.stringTable[i][annotation.fieldNumByName("GeneSymbol")];
      probeToName.put(probe,name);
      Vector v = (Vector)nameToProbe.get(name);
      if(v==null){
        v = new Vector();
      }
      v.add(probe);
      nameToProbe.put(name,v);
    }}
  }
  
  public void loadSampleAnnotation(){
	  if(sampleAnnotationFile!=null){
		  sampleAnnotation = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(sampleAnnotationFile,true,"\t");
		  sampleAssociations = new Vector();
	  if(sampleAssociationsDescriptions.size()>0){
		  for(int i=0;i<sampleAssociationsDescriptions.size();i++){
			  String s = (String)sampleAssociationsDescriptions.get(i);
			  StringTokenizer st = new StringTokenizer(s,"|");
			  Association assoc = new Association();
			  assoc.fieldName = st.nextToken();
			  if(assoc.fieldName.startsWith("d_"))
					  assoc.continuos = false;
			  if(assoc.fieldName.startsWith("c_"))
					  assoc.continuos = true;
			  assoc.fieldName = assoc.fieldName.substring(2);
			  if(!assoc.continuos){
				  String label1 = st.nextToken();
				  String label2 = st.nextToken();
				  st = new StringTokenizer(label1,";");
				  while(st.hasMoreTokens())
					  assoc.labelsClass1.add(st.nextToken());
				  st = new StringTokenizer(label2,";");
				  while(st.hasMoreTokens())
					  assoc.labelsClass2.add(st.nextToken());
			  }
			  sampleAssociations.add(assoc);
		  }
	  }
	  // now pre-process table data
	  for(int j=0;j<sampleAssociations.size();j++){
		  Association assoc = (Association)sampleAssociations.get(j);
		  if(assoc.continuos)
			  assoc.contValues = new float[sampleAnnotation.rowCount]; 
		  for(int i=0;i<sampleAnnotation.rowCount;i++){
			  String sample = sampleAnnotation.stringTable[i][sampleAnnotation.fieldNumByName("SAMPLE")];
			  int k = table.fieldNumByName(sample);
			  if(assoc.continuos)
				  assoc.contValues[k-1] = Float.parseFloat(sampleAnnotation.stringTable[i][sampleAnnotation.fieldNumByName(assoc.fieldName)]);
			  else{
				  String label = sampleAnnotation.stringTable[i][sampleAnnotation.fieldNumByName(assoc.fieldName)];
				  if(assoc.labelsClass1.indexOf(label)>=0)
					  assoc.indClass1.add(new Integer(k-1));
				  if(assoc.labelsClass2.indexOf(label)>=0)
					  assoc.indClass2.add(new Integer(k-1));
			  }
		  }
	  }
	  }
  }

  public void loadSignatures(){
    GMTReader gr = new GMTReader();
    Vector sigs = gr.readGMTDatabase(sigdb);
    metagenes = new MetageneLoader();
    System.out.print("Loading signatures... ");
    //metagenes.loadMetagenes(sigdb);
    metagenes.listOfGESignatures = sigs;
    System.out.println(metagenes.listOfGESignatures.size()+" loaded");
  }

  public double[] getSignatureScores(GESignature sig, Vector<Float> sampleAssociationsScores, Vector<Double> sampleAssociationsPValues){
    double scores[] = new double[numberOfScores];
    int tsizes[] = new int[2];
    if(sig.geneNames.size()>=minsetsize){
      VDataTable vts = VSimpleProcedures.selectRowsFromList(table,sig.geneNames);
      vts = vdaoengine.utils.VSimpleProcedures.substituteRowsByStatistics(vts,"GeneSymbol",4);
      tsizes[0] = vts.rowCount;
      if(vts.rowCount>=minsetsize){
        VDataSet ds = null;
        if(useZscores)
           ds = VSimpleProcedures.SimplyPreparedDataset(vts,-1,true,true);
        else
           ds = VSimpleProcedures.SimplyPreparedDataset(vts,-1,false,false);
        Vector v = MetageneLoader.determineOutliers(ds,"GeneSymbol",outlierThreshold,outlierDimension);
        System.out.print(sig.name+" outliers:\t");
          for(int j=0;j<v.size();j++){
        		sig.geneNames.remove((String)v.get(j));
        		System.out.print((String)v.get(j)+"\t");
          }
          System.out.println();
          vts = VSimpleProcedures.selectRowsFromList(table,sig.geneNames);
          vts = vdaoengine.utils.VSimpleProcedures.substituteRowsByStatistics(vts,"GeneSymbol",4);
          tsizes[1] = vts.rowCount;
          if(vts.rowCount>=minsetsize){
             if(useZscores)
               ds = VSimpleProcedures.SimplyPreparedDataset(vts,-1,false,true);
             else
               ds = VSimpleProcedures.SimplyPreparedDataset(vts,-1,false,false);
             VDataSet ds1 = VSimpleProcedures.SimplyPreparedDataset(vts,-1,false,false);
             ds1.calcStatistics();
             VDataSet dsf = VSimpleProcedures.SimplyPreparedDataset(vts,-1,false,false);
             for(int kk=0;kk<dsf.pointCount;kk++)
               globalDataset.processIntoSpace(dsf.getVector(kk));

             PCAMethod pca = new PCAMethod();
             pca.setDataSet(ds);
             pca.calcBasis(3);

             PCAMethodFixedCenter pcaf = new PCAMethodFixedCenter();
             pcaf.setDataSet(dsf);
             pcaf.calcBasis(3);
             double df[] = pcaf.calcDispersions();

             VDataSet dsp = pca.getProjectedDataset();
             VDataSet dsfp = pcaf.getProjectedDataset();
             dsp.calcStatistics();
             dsfp.calcStatistics();
             ds.calcStatistics();
             float displ[] = new float[ds1.coordCount];
             for(int k=0;k<displ.length;k++) displ[k] = (float)ds1.simpleStatistics.getMean(k);
             globalDataset.processIntoSpace(displ);
             double d[] = pca.calcDispersionsRelative(ds.simpleStatistics.totalDispersion*ds.simpleStatistics.totalDispersion);
             scores[0] = d[0]; // L1
             scores[1] = d[0]/d[1]; // GAP1
             scores[2] = d[0]-d[1]; // DIFF12
             scores[3] = d[0]/(d[1]+d[2]); // GAP1t
             scores[4] = d[1]/d[2]; // GAP2
             scores[5] = 0.5f*(d[0]+d[1])/(d[2]); //GAP2t
             scores[6] = d[1]; // L2
             scores[7] = d[2]; // L3
             scores[8] = VVectorCalc.Norm(displ); // DISPLACMNT
             scores[9] = ds.simpleStatistics.totalDispersion; // VARIATION
             scores[10] = dsp.simpleStatistics.getStdDev(0); // VARIATION OF GENE WEIGHTS
             scores[11] = Math.abs(dsp.simpleStatistics.getSkew(0)); // SKEW
             scores[12] = VSimpleFunctions.calcMedianPairwiseCorrelation(ds1,false); // MEDCORR
             scores[13] = VSimpleFunctions.calcMedianPairwiseCorrelation(ds1,true); // MEDABSCORR
             scores[14] = df[0]/df[1]; // GAP1S
             scores[15] = VSimpleFunctions.calcMedianPairwiseSpearmanCorrelation(ds1,true); // MEDABSSPCORR
             scores[16] = Math.abs(dsfp.simpleStatistics.getSkew(0)); // SKEWF
             scores[17] = VSimpleFunctions.calcMeanPairwiseCorrelation(ds1, true, 0.5f); // CORRGRWEIGHT5
             scores[18] = VSimpleFunctions.calcMeanPairwiseCorrelation(ds1, true, 0.8f);; // CORRGRWEIGHT8
             scores[19] = VSimpleFunctions.calcChavezIntrinsicDimension(ds1); // DIM_CHAVEZ
             if(calcAlternativeDimensions){
             	pca.calcBasisUntilBSDimension(ds1);
             	int dim = pca.linBasis.numberOfBasisVectors; if(dim==0) dim = ds1.coordCount;
             scores[20] = dim; //	DIM_BRST
             scores[21] = 0; //	DIM_BRST2
             }             
             //scores[17] = Math.abs(VSimpleFunctions.calcMultidimensionalSkewness); // SKEWM
             
             associateFirstPCAwithSampleAnnotation(pca, pcaf, sampleAssociationsScores, sampleAssociationsPValues);

             
        }else scores = null;
      }else scores = null;
    }else scores = null;
    sizes.add(tsizes);
    return scores;
  }

  public Vector scanSignatures(){
    Vector v = new Vector();

    associationFirstPCAScores = new Vector<Float>();
    associationFirstPCAPValues = new Vector<Double>();  
    associationFirstPCAFixedScores = new Vector<Float>();
    associationFirstPCAFixedPValues = new Vector<Double>();
    
    for(int i=0;i<metagenes.listOfGESignatures.size();i++){
      GESignature sig = (GESignature)metagenes.listOfGESignatures.get(i);
      System.out.println((i+1)+")"+sig.name);
      Vector<Float> sampleAssociationsScores = new Vector<Float>();
      Vector<Double> sampleAssociationsPValues = new Vector<Double>();
      
      double d[] = getSignatureScores(sig,sampleAssociationsScores,sampleAssociationsPValues);
      
      int k = 0;
      
      if(sampleAssociationsScores.size()>0){
    	  for(int j=0;j<sampleAssociations.size();j++){
    	  	  associationFirstPCAScores.add(sampleAssociationsScores.get(k));
    	  	  associationFirstPCAPValues.add(sampleAssociationsPValues.get(k));
    	  	  k++;
      	  }
      	  for(int j=0;j<sampleAssociations.size();j++){
      		  associationFirstPCAFixedScores.add(sampleAssociationsScores.get(k));
      		  associationFirstPCAFixedPValues.add(sampleAssociationsPValues.get(k));
      		  k++;
      	  }
      }else{
    	  if(sampleAssociations!=null){
          for(int j=0;j<sampleAssociations.size();j++){
        	  associationFirstPCAScores.add(new Float(0f));
        	  associationFirstPCAPValues.add(new Double(0));
        	  k++;
          }
          for(int j=0;j<sampleAssociations.size();j++){
        	  associationFirstPCAFixedScores.add(new Float(0f));
        	  associationFirstPCAFixedPValues.add(new Double(0));
        	  k++;
          }
    	  }
      }
      
      if(d==null){
        d = new double[numberOfScores];
      for(int j=0;j<d.length;j++)
        d[j] = Double.NaN;
      }
      v.add(d);
    }
    return v;
  }

  public void selectOnlySignatureGenes(){
    Vector names = new Vector();
    HashSet nset = new HashSet();
    for(int i=0;i<metagenes.listOfGESignatures.size();i++){
      GESignature sig = (GESignature)metagenes.listOfGESignatures.get(i);
      for(int j=0;j<sig.geneNames.size();j++)
        if(!nset.contains((String)sig.geneNames.get(j)))
          nset.add((String)sig.geneNames.get(j));
    }
    Iterator it = nset.iterator();
    while(it.hasNext())
      names.add(it.next());
    VDataTable vt = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(table,names);
    vt.makePrimaryHash("GeneSymbol");
    table = vt;
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(table,data+".dat");
 }
  
  public void savePathwayAnnotations(){
	    VDataTable tablec = vdaoengine.utils.VSimpleProcedures.substituteRowsByStatistics(table,"GeneSymbol",4);
	    tablec.addNewColumn("GENESETS", "", "", VDataTable.STRING, "");
	    for(int k=0;k<tablec.rowCount;k++){
	    	String gene = tablec.stringTable[k][tablec.fieldNumByName("GeneSymbol")];
	    	for(int i=0;i<metagenes.listOfGESignatures.size();i++){
	    		GESignature sig = (GESignature)metagenes.listOfGESignatures.get(i);
	    		if(sig.geneNames.indexOf(gene)>=0){
	    			tablec.stringTable[k][tablec.fieldNumByName("GENESETS")]+=sig.name+"*";
	    		}
	    	}
	    }
	    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(tablec,data+"_a.dat");
  }  

 public void calcGlobalDisplacement(){
   VDataSet vd = null;
   //if(useZscores)
   //  vd = VSimpleProcedures.SimplyPreparedDataset(table,-1,true,true);
   //else
     vd = VSimpleProcedures.SimplyPreparedDataset(table,-1,true,false);
   vd.calcStatistics();
   float ff[] = new float[vd.coordCount];
   for(int i=0;i<vd.coordCount;i++) ff[i] = vd.simpleStatistics.getMean(i);
   //for(int i=0;i<vd.coordCount;i++) System.out.print(ff[i]+"\t");
   //System.out.println("\n"+VVectorCalc.Norm(ff));
   globalDisplacement = ff;
   globalDataset = vd;
}
 
 public void associateVectorWithSampleAnnotation(float f[], Vector<Float> sampleAssociationsScores, Vector<Double> sampleAssociationsPValues){
	 if(sampleAssociations!=null)
	 for(int i=0;i<sampleAssociations.size();i++){
		 Association assoc = (Association)sampleAssociations.get(i);
		 float score = 0f;
		 double pval = 0; 
		 if(assoc.continuos){
			 score = Math.abs(vdaoengine.utils.VSimpleFunctions.calcSpearmanCorrelationCoeff(f, assoc.contValues));
			 pval = vdaoengine.utils.VSimpleFunctions.calcCorrelationPValue(score, f.length);
		 }else{
			 Vector set1 = new Vector();
			 Vector set2 = new Vector();
			 for(int j=0;j<assoc.indClass1.size();j++)
				 set1.add(new Float(f[((Integer)assoc.indClass1.get(j)).intValue()]));
			 for(int j=0;j<assoc.indClass2.size();j++)
				 set2.add(new Float(f[((Integer)assoc.indClass2.get(j)).intValue()]));
		     Vector<Integer> dfvec = new Vector<Integer>();			 
			 //score = Math.abs((float)vdaoengine.utils.VSimpleFunctions.calcTTest(set1,set2,false,dfvec));
			 //pval = vdaoengine.utils.VSimpleFunctions.ttest(score, set1.size()+set2.size()-2);
			 pval = vdaoengine.utils.VSimpleFunctions.ttest(score, dfvec.get(0).intValue());
			 score/=3f;
		 }
		 sampleAssociationsScores.add(score);
		 sampleAssociationsPValues.add(pval);
	 }
 }
 
 public void associateFirstPCAwithSampleAnnotation(PCAMethod pca, PCAMethodFixedCenter pcaf, Vector<Float> sampleAssociationsScores, Vector<Double> sampleAssociationsPValues){
	 float pca1[] = vdaoengine.utils.Utils.doubleArrayTofloat(pca.linBasis.basis[0]);
	 Vector<Float> sas = new Vector<Float>();
	 Vector<Double> sap = new Vector<Double>();
	 associateVectorWithSampleAnnotation(pca1,sas,sap);
	 for(int i=0;i<sas.size();i++){
		 sampleAssociationsScores.add(sas.get(i));
		 sampleAssociationsPValues.add(sap.get(i));
	 }
	 float pca1f[] = vdaoengine.utils.Utils.doubleArrayTofloat(pcaf.linBasis.basis[0]);	 
	 sas = new Vector<Float>();
	 sap = new Vector<Double>();
	 associateVectorWithSampleAnnotation(pca1f,sas,sap);
	 for(int i=0;i<sas.size();i++){
		 sampleAssociationsScores.add(sas.get(i));
		 sampleAssociationsPValues.add(sap.get(i));
	 }
 }
 
 public void writeResultHeader(FileWriter fw) throws Exception{
	 fw.write("NAME\tNRND\tL1\tL2\tL3\tGAP1\tGAP1F\tGAP2\tDISPLCMNT\tVAR\tVAR1\tSKEW\tSKEWF\tMEDCORR\tABSMEDCORR\tABSMEDSPCORR\tCORRGRWEIGHT5\tCORRGRWEIGHT8\tDIM_CHAVEZ\tDIM_BRST\tDIM_BRST2\tSIZE\tSIZE_FOUND\tSIZE_PRUNED\t");
	 if(sampleAssociations!=null)
	 for(int i=0;i<sampleAssociations.size();i++){
		 Association assoc = (Association)sampleAssociations.get(i);
		 fw.write(assoc.fieldName+"\t"+assoc.fieldName+"_p\t");
		 fw.write(assoc.fieldName+"_F\t"+assoc.fieldName+"_F_p\t");
	 }
	 fw.write("\n");
 }
 
 public int writeScores(FileWriter fw, GESignature sig, Vector scores, int counteri, int counterassoc) throws Exception{
	 double d[] = (double[])scores.get(counteri);
	 if(!Double.isNaN(d[0])){
		 int random = 1;
		 if(sig.name.startsWith("RND")) random = 0;
		 if(sig.name.indexOf("SKW")>=0) random = 0;
		 fw.write(sig.name+"\t"+random+"\t");
		 fw.write(d[0]+"\t"+d[6]+"\t"+d[7]+"\t"+d[1]+"\t"+d[14]+"\t"+d[4]+"\t"+d[8]+"\t"+d[9]+"\t"+d[10]+"\t"+d[11]+"\t"+d[16]+"\t"+d[12]+"\t"+d[13]+"\t"+d[15]+"\t"+d[17]+"\t"+d[18]+"\t"+d[19]+"\t"+d[20]+"\t"+d[21]+"\t");
		 fw.write(sig.geneNames.size()+"\t"+((int[])sizes.get(counteri))[0]+"\t"+((int[])sizes.get(counteri))[1]+"\t");
		 if(sampleAssociations!=null)		 
		 for(int j=0;j<sampleAssociations.size();j++){
			 fw.write(associationFirstPCAScores.get(counterassoc).floatValue()+"\t"+associationFirstPCAPValues.get(counterassoc).doubleValue()+"\t");
			 fw.write(associationFirstPCAFixedScores.get(counterassoc).floatValue()+"\t"+associationFirstPCAFixedPValues.get(counterassoc).doubleValue()+"\t");
			 counterassoc++;
		 }
		 fw.write("\n");
	 }else{
		 if(sampleAssociations!=null)
			 counterassoc+=sampleAssociations.size();
	 } 
	 return counterassoc;
 }
 
 public void calcGlobalAssociations(){
	 globalPCA = new PCAMethod();
	 globalPCA.setDataSet(globalDataset);
	 globalPCA.calcBasis(3);
	 if(sampleAssociations.size()>0){
		 System.out.println("\n\nGlobal PCA1 associations:");
		 Vector<Float> sas = new Vector<Float>();
		 Vector<Double> sap = new Vector<Double>();
		 float pca1[] = vdaoengine.utils.Utils.doubleArrayTofloat(globalPCA.linBasis.basis[0]);
		 associateVectorWithSampleAnnotation(pca1,sas,sap);
		 System.out.println("FIELD\tSCORE\tP-VALUE");
		 for(int i=0;i<sampleAssociations.size();i++){
			 Association acc = (Association)sampleAssociations.get(i);
			 System.out.println(acc.fieldName+"\t"+sas.get(i)+"\t"+sap.get(i));
		 }

		 System.out.println("\nGlobal PCA2 associations:");
		 sas = new Vector<Float>();
		 sap = new Vector<Double>();
		 float pca2[] = vdaoengine.utils.Utils.doubleArrayTofloat(globalPCA.linBasis.basis[1]);
		 associateVectorWithSampleAnnotation(pca2,sas,sap);
		 System.out.println("FIELD\tSCORE\tP-VALUE");
		 for(int i=0;i<sampleAssociations.size();i++){
			 Association acc = (Association)sampleAssociations.get(i);
			 System.out.println(acc.fieldName+"\t"+sas.get(i)+"\t"+sap.get(i));
		 }
		 
		 
		 System.out.println("\nGlobal PCA3 associations:");
		 sas = new Vector<Float>();
		 sap = new Vector<Double>();
		 float pca3[] = vdaoengine.utils.Utils.doubleArrayTofloat(globalPCA.linBasis.basis[2]);
		 associateVectorWithSampleAnnotation(pca3,sas,sap);
		 System.out.println("FIELD\tSCORE\tP-VALUE");
		 for(int i=0;i<sampleAssociations.size();i++){
			 Association acc = (Association)sampleAssociations.get(i);
			 System.out.println(acc.fieldName+"\t"+sas.get(i)+"\t"+sap.get(i));
		 }
		 System.out.println();
		 
	 }
 }
 
 public Vector calcSampling(int size, FileWriter fw) throws Exception{
	  
	    metagenes = new MetageneLoader();
	    sizes.clear();
	    
	    for(int j=0;j<numberofresampling;j++){
	    	System.out.println(size+":"+(j+1));
	    	Random r = new Random();
	    	GESignature rs = new GESignature();
	    	rs.name = "RND"+size+"_"+(j+1);
	    	for(int i=0;i<size;i++)
	    		rs.geneNames.add(table.stringTable[r.nextInt(table.rowCount)][table.fieldNumByName("GeneSymbol")]);
	    	metagenes.listOfGESignatures.add(rs);
	    }
	    Vector scores = scanSignatures();
	    
	    return scores;
 }
 
 public void calcPValues(VDataTable scoreTable){
	 for(int i=2;i<scoreTable.colCount;i++){
		 scoreTable.fieldTypes[i] = scoreTable.NUMERICAL;
		 restable.fieldTypes[i] = scoreTable.NUMERICAL;
	 }
	 VDataSet scoreData = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(scoreTable, -1);
	 VDataSet resultData = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(restable, -1);
	 
	 for(int i=0;i<restable.rowCount;i++){
		 int size = (int)resultData.massif[i][restable.fieldNumByName("SIZE")-2];
		 int approxSize = 1;
		 for(int j=0;j<samplingGeneSetSizes.length;j++){
			 int s = samplingGeneSetSizes[j];
			 if(Math.abs(Math.log(size)-Math.log(s))<Math.abs(Math.log(size)-Math.log(approxSize)))
				 approxSize = s;
		 }
		 int sup[] = new int[scoreData.coordCount];
		 int count[] = new int[scoreData.coordCount];
		 for(int k=0;k<scoreData.pointCount;k++){
			 //String fname = ;
			 //if()
		 }
	 }
	 
 }



}