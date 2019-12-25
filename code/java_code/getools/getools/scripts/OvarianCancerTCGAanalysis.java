package getools.scripts;

import getools.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class OvarianCancerTCGAanalysis {
	
	public class Score35{
		public float value = 0f;
		public float threshold = 0f;
		public float pvalue = 0f;
		public int side = 0; // 1 - right, -1 - left
		public int support = 0; // number of 3 and 5
		public int exceptions = 0; // number of -1s
	}

	/**
	 * @param args
	 */
	
	public static float shrinkSmallExpressionThreshold = -1f;
	public static float minimalAmplitudeForScoreComputing = 0.5f;
	
	public static int numberOfShufflingsForTest = 10000;
	
	public VDataTable sample_info = null;
	public VDataTable data = null;
	String prefix = null;
	String dataFolder = null;
	String dataFile = null;
	
	public String numerical[] = {"BRCA12_class","BRCAness", "p_BAF", "DNA_ind", "Ploidy", "LST_10MB"};

	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			OvarianCancerTCGAanalysis ovc = new OvarianCancerTCGAanalysis();
			
			ovc.prefix = "C:/Datas/OvarianCancer/TCGA/";
			ovc.dataFolder = "expression/rnaseq/";
			//ovc.dataFile = "mRNAexpr_RNAseqRPKM_TCGA_duplOut.txt";
			//ovc.dataFolder = "expression/affymetrix/";
			//ovc.dataFile = "mRNAexpr_Affy_TCGA.txt";
				
			ovc.loadSampleInformation();
			
			//ovc.makeComplexGMTfromHPRD();
			
			//ovc.loadDecomposeData();
			
			//ovc.extractNumericalTable(ovc.prefix+"genome/Copy.dat",ovc.prefix+"expression/affymetrix/samples.txt",ovc.prefix+"expression/affymetrix/names.txt",ovc.prefix+"expression/affymetrix/data_genomic_numerical.txt");
			//ovc.extractNumericalTable(ovc.prefix+"genome/Copy.dat",ovc.prefix+"expression/rnaseq/samples.txt",ovc.prefix+"expression/rnaseq/names.txt",ovc.prefix+"expression/rnaseq/data_genomic_numerical.txt");
			
			//ovc.compileGeneCopyNumber_BAF_Tables(ovc.prefix+"genome/procs_All_included/","Copy");
			//ovc.compileGeneCopyNumber_BAF_Tables(ovc.prefix+"genome/procs_All_included/","Mallele");			
			//ovc.extractCNBAFromGapFile(ovc.prefix+"genome/procs_All_included/proc_ACOLD_A01_466074.txt","Copy");
			//ovc.createGeneCopyNumberTable(ovc.prefix+"genome/procs_All_included/proc_ACOLD_A01_466074_Copy.dat");
			
			/*VDataTable vta = VDatReadWrite.LoadFromVDatFile(ovc.prefix+"genome/Copy1.dat");
			CancerCellLine.AnnotateWithSubsetOfGenes(vta,ovc.prefix+"genesets/sources/diff_genesBRCAness_TCGA_toIPA.txt","BRCANESS_1000DF");
			VDatReadWrite.saveToVDatFile(vta, ovc.prefix+"genome/Copy1a.dat");*/
			
			//ovc.data = VDatReadWrite.LoadFromVDatFile(ovc.prefix+ovc.dataFolder+"data.dat");
			//ovc.data = VDatReadWrite.LoadFromVDatFile(ovc.prefix+ovc.dataFolder+"dataf.dat");
			//ovc.data = VDatReadWrite.LoadFromVDatFile(ovc.prefix+ovc.dataFolder+"dnarepair_genes.dat");
			ovc.data = VDatReadWrite.LoadFromVDatFile(ovc.prefix+ovc.dataFolder+"datafc.dat");
			//ovc.processTable();
			//ovc.makeSampleTable(2000);

			//ovc.ModuleAnalysis(ovc.prefix+"genesets/dnarepair_v1.0.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/dnr_complexes_hugo.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/dnr_entpartn_hugo.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/acsn_complexes_hugo.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/Havugimana2012.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/ovary_signatures.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/hprd9_complexes.gmt");
			ovc.ModuleAnalysis(ovc.prefix+"genesets/kegg_pathway.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/msigdb.v4.0.symbols_KEGG.gmt");
			//ovc.ModuleAnalysis(ovc.prefix+"genesets/msigdb.v4.0.symbols_miRNA.gmt");
			
			if(ovc.dataFolder.contains("affy"))
				shrinkSmallExpressionThreshold = 3f; //ovc.TestSingleGenesFor35();
			if(ovc.dataFolder.contains("rnaseq"))
				shrinkSmallExpressionThreshold = -1f;
			/*Vector<String> geneNames = Utils.loadStringListFromFile(ovc.prefix+"genesets/listOfHighScore35_affy_rnaseq__");
			ovc.computeThresholdsAndPValuesScore35(geneNames, 5,"AFFY");*/
			
			//ovc.annotateSamplesWithScore35(ovc.prefix+"pathways/Score35_genes_ReducedRNAS_.txt", ovc.prefix+"expression/affymetrix/data.dat", ovc.prefix+"expression/rnaseq/data.dat", "AFFY", "RNAS", ovc.prefix+"pathways/Score35_samples_abs.txt");
			//ovc.annotateSamplesWithScore35(ovc.prefix+"pathways/Score35_genes_ReducedRNAS.txt", ovc.prefix+"expression/rnaseq/data.dat", null, "RNAS", null, ovc.prefix+"pathways/Score35_samples_RNAabs.txt");
			
			//ovc.TestModuleActivitiesFor35("dnr_complexes_hugo");
			//ovc.TestModuleActivitiesFor35("dnr_entpartn_hugo");
			//ovc.TestModuleActivitiesFor35("acsn_complexes_hugo");
			//ovc.TestModuleActivitiesFor35("Havugimana2012");
			//ovc.TestModuleActivitiesFor35("ovary_signatures");
			//ovc.TestModuleActivitiesFor35("hprd9_complexes");
			
			//String moduleSet = "msigdb.v4.0.symbols_miRNA";
			//ovc.MergeSampleTableWithModules(ovc.prefix+"analysis/ica/rnaseq/complete/A.dat",moduleSet);
			//ovc.makeSignatureCorrelationGraph(ovc.prefix+"analysis/ica/rnaseq/complete/A_"+moduleSet+".dat",0.8f, null);
			

			//ovc.MergeSampleTableWithModules(ovc.prefix+"analysis/ica/affymetrix/complete/A.dat","ovary_signatures");
			//ovc.MergeSampleTableWithModules(ovc.prefix+"analysis/ica/rnaseq/complete/A.dat","acsn_complexes_hugo");
			//ovc.MergeSampleTableWithModules(ovc.prefix+"analysis/ica/rnaseq/complete/A_ovary_signatures.dat","acsn_complexes_hugo");
			
			
			//String selectedFields[] = {"MCM2_MCM3_MCM4_MCM5_MCM6_MCM7_complex"};
			//String selectedFields[] = {"LST_10MB"};
			//ovc.makeSignatureCorrelationGraph(ovc.prefix+"analysis/ica/rnaseq/complete/A.dat",0.8f, null);
			//ovc.makeSignatureCorrelationGraph(ovc.prefix+"analysis/ica/rnaseq/complete/A_ovary_signatures_acsn_complexes_hugo.dat",0.3f, selectedFields);
			//ovc.makeSignatureCorrelationGraph(ovc.prefix+"analysis/ica/affymetrix/complete/A_ovary_signatures.dat",0.5f, null);
			//ovc.makeSignatureCorrelationGraph(ovc.prefix+"analysis/ica/rnaseq/complete/A_ovary_signatures_acsn_complexes_hugo.dat",0.6f, null);
			
			
			 //ovc.TestMetaSampleFor35(ovc.prefix+"analysis/ica/affymetrix/complete/AT.dat");
			
			//ovc.makeTableCorrelationGraph(ovc.prefix+"analysis/ica/rnaseq/complete/S.dat","rs",ovc.prefix+"analysis/ica/affymetrix/complete/S.dat","af",ovc.prefix+"analysis/ica/",0.5f);
						
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadSampleInformation(){
		sample_info = VDatReadWrite.LoadFromSimpleDatFile(prefix+"sample_info/sample_info1.txt", true, "\t");
		sample_info.makePrimaryHash("Names");
		sample_info.tableHashSecondary = sample_info.tableHashPrimary;
		sample_info.makePrimaryHash("BCRPATIENTBARCODE");
	}
	
	public void loadDecomposeData() throws Exception{
		
		VDatReadWrite.numberOfDigitsToKeep = 2;		
		VDatReadWrite.useQuotesEverywhere = false;
		
		System.out.println("Load data");
		data = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+dataFile, true, "\t");
		for(int i=1;i<data.colCount;i++)
			data.fieldTypes[i] = VDataTable.NUMERICAL;
		
		/*System.out.println("Save all values in rows");
		FileWriter fw = new FileWriter(prefix+dataFolder+"allvalues.txt");
		for(int i=0;i<data.rowCount;i++)
			for(int j=1;j<data.colCount;j++){
				String s = data.stringTable[i][j];
				if(s==null)
					System.out.println("null in column "+j);
				fw.write(s+"\n");
			}
		fw.close();*/
		
		System.out.println("Remove few non-annotated samples");
		//String toremove[] = {"TCGA_13_0765","TCGA_23_1107","TCGA_25_1324","TCGA_25_1326","TCGA_25_1328","TCGA_61_1721"};
		
		/*for(int i=0;i<data.colCount;i++)
			if(sample_info.tableHashPrimary.get(data.fieldNames[i])==null)
				System.out.println("Sample "+data.fieldNames[i]+" not found");*/
		

		Vector<Integer> duplicatedColumns = new Vector<Integer>();
		for(int i=0;i<data.colCount;i++){
			String fni = data.fieldNames[i];
			for(int j=i+1;j<data.colCount;j++)if(i!=j){
				String fnj = data.fieldNames[j];
				if(fni.equals(fnj)){
					System.out.println("Duplicated "+fni+" "+i+" and "+j);
					duplicatedColumns.add(j);
				}
			}
		}

		// In Affy data		
		String toremove[] = {"TCGA_04_1369", "TCGA_13_0765", "TCGA_13_2061", "TCGA_13_2066", "TCGA_20_0996", "TCGA_23_1107", "TCGA_23_2647", "TCGA_23_2649", "TCGA_25_1324", "TCGA_25_1325", "TCGA_25_1326", "TCGA_25_1328", "TCGA_29_2429", "TCGA_36_2532", "TCGA_42_2587", "TCGA_61_1721", "TCGA_61_1916", "TCGA_61_1916"};
		Vector<String> vtoremove = new Vector<String>();
		for(String s: toremove){ vtoremove.add(s);
		boolean found = false;
		for(int i=0;i<data.colCount;i++){
			if(data.fieldNames[i].equals(s))
				found = true;
		}
		if(!found)
			System.out.println(s+" column is not FOUND");
		}
		data = VSimpleProcedures.removeColumnNumbers(data, duplicatedColumns);
		data = VSimpleProcedures.removeColumns(data, vtoremove);
					
		System.out.println("Save gene names");
		FileWriter fw3 = new FileWriter(prefix+dataFolder+"names.txt");
		for(int i=0;i<data.rowCount;i++)
			fw3.write(data.stringTable[i][0]+"\n");
		fw3.close();
		
		System.out.println("Make annotation numerical table");
		Vector<String> vnumerical = new Vector<String>();
		for(String s: numerical) vnumerical.add(s);
		FileWriter fw1 = new FileWriter(prefix+dataFolder+"samples.txt");
		for(int i=1;i<data.colCount;i++)
			//fw1.write(sample_info.stringTable[i][0]+"\n");
			fw1.write(data.fieldNames[i]+"\n");
		fw1.close();
		
		FileWriter fw2 = new FileWriter(prefix+dataFolder+"samples_info_numerical.txt");
		for(int i=1;i<data.colCount;i++){
			String fn = data.fieldNames[i];
			if(sample_info.tableHashPrimary.get(fn)==null)
				System.out.println("Sample "+fn+" is not found");
			int k = sample_info.tableHashPrimary.get(fn).get(0);
			for(int j=0;j<sample_info.colCount;j++)
				if(vnumerical.contains(sample_info.fieldNames[j]))
					fw2.write(sample_info.stringTable[k][j]+"\t");
			fw2.write("\n");
		}
		fw2.close();
		
		/*System.out.println("log transform for RNASeq data");
		if(dataFolder.contains("rnaseq")){
			for(int i=0;i<data.rowCount;i++)
				for(int j=1;j<data.colCount;j++){
					float val = Float.parseFloat(data.stringTable[i][j]);
					if(val==0f)
						val = (float)1e-4;
					data.stringTable[i][j] = ""+((float)Math.log((double)val)/Math.log(2)); 
				}
		}*/
		VDatReadWrite.saveToSimpleDatFilePureNumerical(data, prefix+dataFolder+"data_numerical.txt");
		
		System.out.println("annotate Table columns");
		data.fieldInfo = new String[data.colCount][sample_info.colCount-2];
		for(int i=0;i<data.colCount;i++){
			String fn = data.fieldNames[i];
			Vector<Integer> rs = sample_info.tableHashPrimary.get(fn);
			if(rs!=null){
				int k = rs.get(0);
				data.fieldInfo[i] = new String[sample_info.colCount-2];
				for(int j=2;j<sample_info.colCount;j++)
					data.fieldInfo[i][j-2] = sample_info.stringTable[k][j];
			}
		}
		
		System.out.println("Saving data");
		VDatReadWrite.saveToVDatFile(data, prefix+dataFolder+"data.dat");

		
	}
	
	public void processTable() throws Exception{
		System.out.println("Filter data");
		data = TableUtils.filterByAverageValue(data, 3.5f);
		VDatReadWrite.saveToVDatFile(data, prefix+dataFolder+"dataf.dat");
		System.out.println("Center data");
		VDataTable datac = TableUtils.normalizeVDat(data, true, false);
		VDatReadWrite.saveToVDatFile(datac, prefix+dataFolder+"datafc.dat");
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.numberOfDigitsToKeep = 2;
		VDatReadWrite.saveToSimpleDatFilePureNumerical(datac, prefix+dataFolder+"datafc_numerical.txt");
		FileWriter fw = new FileWriter(prefix+dataFolder+"datafc_genenames.txt");
		for(int i=0;i<datac.rowCount;i++) fw.write(datac.stringTable[i][0]+"\n");
		fw.close();
	}
	
	public void makeSampleTable(int numVariableGenes){
		if(numVariableGenes!=-1){
			data = TableUtils.filterByVariation(data, numVariableGenes, false);
		}
		VDataTable data_samples = data.transposeTable(data.fieldNames[0]);
		VDataTable datapca = TableUtils.PCAtable(data_samples, true, 20);
		
		// Change D1-DN field names for real sample info
		Vector<String> vnumerical = new Vector<String>();
		for(String s: numerical) vnumerical.add(s);
		for(int i=2;i<sample_info.colCount;i++){
			int k = data_samples.fieldNumByName("D"+(i-1));
			data_samples.fieldNames[k] = sample_info.fieldNames[i];
			if(vnumerical.contains(data_samples.fieldNames[k]))
				data_samples.fieldTypes[k] = VDataTable.NUMERICAL;
			
			k = datapca.fieldNumByName("D"+(i-1));
			datapca.fieldNames[k] = sample_info.fieldNames[i];
			if(vnumerical.contains(data_samples.fieldNames[k]))
				datapca.fieldTypes[k] = VDataTable.NUMERICAL;
		}
		VDatReadWrite.saveToVDatFile(data_samples, prefix+dataFolder+"data_samples"+numVariableGenes+".dat");
		
		VDatReadWrite.saveToVDatFile(datapca, prefix+dataFolder+"data_samlpes"+numVariableGenes+"_pca.dat");
		
		//VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(data, -1);
	}
	
	public void makeGenomicSampleTable(VDataTable vt, int numVariableGenes){
		if(numVariableGenes!=-1){
			data = TableUtils.filterByVariation(data, numVariableGenes, false);
		}
		VDataTable data_samples = data.transposeTable(data.fieldNames[0]);
		VDataTable datapca = TableUtils.PCAtable(data_samples, true, 20);
		
		// Change D1-DN field names for real sample info
		Vector<String> vnumerical = new Vector<String>();
		for(String s: numerical) vnumerical.add(s);
		for(int i=2;i<sample_info.colCount;i++){
			int k = data_samples.fieldNumByName("D"+(i-1));
			data_samples.fieldNames[k] = sample_info.fieldNames[i];
			if(vnumerical.contains(data_samples.fieldNames[k]))
				data_samples.fieldTypes[k] = VDataTable.NUMERICAL;
			
			k = datapca.fieldNumByName("D"+(i-1));
			datapca.fieldNames[k] = sample_info.fieldNames[i];
			if(vnumerical.contains(data_samples.fieldNames[k]))
				datapca.fieldTypes[k] = VDataTable.NUMERICAL;
		}
		VDatReadWrite.saveToVDatFile(data_samples, prefix+dataFolder+"data_samples"+numVariableGenes+".dat");
		
		VDatReadWrite.saveToVDatFile(datapca, prefix+dataFolder+"data_samlpes"+numVariableGenes+"_pca.dat");
		
		//VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(data, -1);
	}
	
	
	public void ModuleAnalysis(String gmtFileName) throws Exception{
		File f = new File(gmtFileName);
		String s = f.getName();
		s = s.substring(0, s.length()-4);
		File dir = new File(prefix+dataFolder+s);
		dir.mkdir();
		String datFile = prefix+dataFolder+s+"/datafc.dat";
		VDatReadWrite.saveToVDatFile(data, datFile);
		String feature = "1";
		String featureValue = "0";		
		String featureDiff = "1";
		String featureValueDiff = "0;1";	
		
		PrintStream console = System.out;
		File file = new File(dir.getAbsolutePath()+"/report.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		
		String args[] = {"-typeOfModuleFile","0","-typeOfPCAUsage","1","-dataDatFileFolder",dir.getAbsolutePath()+"/","-datFile",(new File(datFile)).getName(),"-moduleFile",gmtFileName,"-hotSpotGenesZthreshold","1.0","-diffSpotGenesZthreshold","0.5","-featureNumberForAveraging",feature,"-featureValueForAveraging",featureValue,"-featureNumberForDiffAnalysis",featureDiff,"-featureValuesForDiffAnalysis",featureValueDiff,"-minimalNumberOfGenesInModule","10","-minimalNumberOfGenesInModuleFound","8"};
		ModuleActivityAnalysis.main(args);
		
		System.setOut(console);
	}
	
	public Score35 scoreTail35(float metaSample[], VDataTable table, boolean shrinkZeroExpression, int support, int numberOfPermutations){
		Score35 sc = new Score35();
		Random r = new Random();
		Score35 score = scoreTail35(metaSample, table, shrinkZeroExpression, support);
		float distribution[] = new float[numberOfPermutations];
		for(int k=0;k<numberOfPermutations;k++){
			for(int i=0;i<numberOfShufflingsForTest;i++){
				int i1 = r.nextInt(metaSample.length);
				int i2 = r.nextInt(metaSample.length);
				float temp = metaSample[i1];
				metaSample[i1] = metaSample[i2];
				metaSample[i2] = temp;
			}
			distribution[k] = scoreTail35(metaSample, table, shrinkZeroExpression, support).value;
		}
		int num = 0;
		for(int k=0;k<numberOfPermutations;k++){
			if(distribution[k]>=score.value)
				num++;
		}
		sc.value = score.value;
		sc.threshold = score.threshold;
		sc.side = score.side;
		sc.pvalue = (float)num/(float)numberOfPermutations;
		sc.support = score.support;
		sc.exceptions = score.exceptions;
		return sc;
	}
	
	
	public Score35 scoreTail35(float metaSample[], VDataTable table, boolean shrinkZeroExpression, int support){
		float score = -Float.MAX_VALUE;
		
		if(shrinkZeroExpression)
			for(int i=0;i<metaSample.length;i++){
				float f = metaSample[i];
				if(f<shrinkSmallExpressionThreshold) f=shrinkSmallExpressionThreshold; 
				metaSample[i] = f;
			}
		
		float threshold = 0f;
		int side = 1;
		int supportDetected = 0;
		int exceptions = 0;
		
		float minv = Float.MAX_VALUE;
		float maxv = -Float.MAX_VALUE;
		for(float f: metaSample){
			if(f<minv) minv=f;
			if(f>maxv) maxv=f;
		}
		float dx = (maxv-minv)/100f;
		
		if((maxv-minv)<minimalAmplitudeForScoreComputing) score = 0f;
		else{
			
		//System.out.println("X\tSCORE\tLEFT-1\tLEFT35\tRIGHT-1\tRIGHT35");		
		for(float x=minv;x<=maxv;x+=dx){
			
			int left35 = 0;
			int right35 = 0;
			int left3 = 0;
			int right3 = 0;
			int leftp = 0;
			int rightp = 0;
			
			float left35v = 0f;
			float right35v = 0f;
			float left_1v = 0f;
			float right_1v = 0f;
			
			
			int left_1 = 0;
			int right_1 = 0;
			
			for(int i=0;i<metaSample.length;i++){
				float f = metaSample[i];
				float label = Float.parseFloat(table.fieldInfo[i+1][0]);
				if(label<0){
					if(f>=x){ right_1++; right_1v+=f-x; }
					if(f<x) { left_1++; left_1v+=x-f; }
					
				}
				if((label>2.99f)){
					if(f>=x) { right35++; right35v+=f-x; }
					if(f<x) { left35++; left35v+=x-f; }
				}
				if((label>0f)){
					if(f>=x) rightp++;
					if(f<x) leftp++;
				}
				if(Math.abs(label-3f)<0.01f){
					if(f>=x) right3++;
					if(f<x) left3++;
				}
					
			}
			
			float sc = -Float.MAX_VALUE;
			// left test
			float leftsc = -Float.MAX_VALUE;
			if((left35+left_1)>0)
				if(left3>=support)
				//leftsc = (float)left35/((float)(left35+left_1));
				//leftsc = left3*((float)(left35-left_1))/((float)(left35+left_1))-left_1;
				leftsc = (left35v-left_1v)/(left_1+left35);
			// right test
			float rightsc = -Float.MAX_VALUE;
			if((right35+right_1)>0)
				if(right3>=support)
				//rightsc = (float)right35/((float)(right35+right_1));
				//rightsc = right3*((float)(right35-right_1))/((float)(right35+right_1))-right_1;
				rightsc = (right35v-right_1v)/(right_1+right35);

			sc = Math.max(leftsc, rightsc);
			//System.out.println(x+"\t"+sc+"\t"+left_1/(left_1+left35)+"\t"+left35v/(left_1+left35)+"\t"+right_1v/(right_1+right35)+"\t"+right35v/(right_1+right35));
			//System.out.println(x+"\t"+sc+"\t"+left_1+"\t"+left35+"\t"+right_1+"\t"+right35);
			
			if(sc>score){
				score=sc;
				threshold = x;	
				if(leftsc>rightsc){ 
					side = -1;
					supportDetected = left35;
					exceptions = left_1;
				}else{ 
					side = 1;
					supportDetected = right35;
					exceptions = right_1;
				}
				}
			}
		}
		
		Score35 sc35 = new Score35();
		sc35.value = score;
		sc35.threshold = threshold;
		sc35.side = side;
		sc35.support = supportDetected;
		sc35.exceptions = exceptions;
		return sc35;
	}
	
	
	public void TestSingleGenesFor35(){
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(data, -1);
		minimalAmplitudeForScoreComputing = 0.5f;
		for(int i=0;i<data.rowCount;i++){
			if(true){
			//if(data.stringTable[i][0].equals("PTEN")){
				float ms[] = vd.massif[i];
				float score3 = scoreTail35(ms, data, true, 3).value;
				float score4 = scoreTail35(ms, data, true, 4).value;
				float score5 = scoreTail35(ms, data, true, 5).value;
				float score6 = scoreTail35(ms, data, true, 6).value;
				float score10 = scoreTail35(ms, data, true, 10).value;
				System.out.println(data.stringTable[i][0]+"\t"+score3+"\t"+score4+"\t"+score5+"\t"+score6+"\t"+score10);
			}
		}
		
	}
	
	public void TestModuleActivitiesFor35(String moduleSet){
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(prefix+dataFolder+moduleSet+"/moduletable_simple.dat");
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		minimalAmplitudeForScoreComputing = 0.01f;
		for(int i=0;i<vt.rowCount;i++){
			if(true){
			//if(data.stringTable[i][0].equals("PTEN")){
				float ms[] = vd.massif[i];
				float score3 = scoreTail35(ms, vt, false, 3).value;
				float score4 = scoreTail35(ms, vt, false, 4).value;
				float score5 = scoreTail35(ms, vt, false, 5).value;
				float score6 = scoreTail35(ms, vt, false, 6).value;
				float score10 = scoreTail35(ms, vt, false, 10).value;
				System.out.println(vt.stringTable[i][0]+"\t"+score3+"\t"+score4+"\t"+score5+"\t"+score6+"\t"+score10);
			}
		}
	}
	
	public void TestMetaSampleFor35(String fn){
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(fn);
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		minimalAmplitudeForScoreComputing = 0.01f;
		for(int i=0;i<vt.rowCount;i++){
			if(true){
			//if(data.stringTable[i][0].equals("PTEN")){
				float ms[] = vd.massif[i];
				float score3 = scoreTail35(ms, vt, false, 3).value;
				float score4 = scoreTail35(ms, vt, false, 4).value;
				float score5 = scoreTail35(ms, vt, false, 5).value;
				float score6 = scoreTail35(ms, vt, false, 6).value;
				float score10 = scoreTail35(ms, vt, false, 10).value;
				System.out.println(vt.stringTable[i][0]+"\t"+score3+"\t"+score4+"\t"+score5+"\t"+score6+"\t"+score10);
			}
		}
	}

	public void makeComplexGMTfromHPRD(){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/OvarianCancer/TCGA/genesets/sources/PROTEIN_COMPLEXES.txt", true, "\t");
		vt.makePrimaryHash("COMPLEX");
		for(String s: vt.tableHashPrimary.keySet()){
			String name = "";
			String desc = "";
			String genes = "";
			Vector<Integer> rows = vt.tableHashPrimary.get(s);
			int k=0;
			for(int i=0;i<rows.size();i++){
				String val = vt.stringTable[rows.get(i)][vt.fieldNumByName("PROTEIN")];
				if(!val.equals("-")){
					name+=val+"_";
					genes+=val+"\t";
					k++;
				}
			}
			if(name.length()>100) name = name.substring(0, 100);
			System.out.println(s+"_"+name+"\t"+k+"\t"+genes);
		}
	}
	
	public void MergeSampleTableWithModules(String table, String signature){
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(table);
		VDataTable vtsig = VDatReadWrite.LoadFromVDatFile(prefix+dataFolder+signature+"/moduletable_simple_T.dat");
		vt = VSimpleProcedures.MergeTables(vt, "SAMPLE", vtsig, "NAME", "_");
		table = table.substring(0, table.length()-4)+"_"+signature+".dat";
		VDatReadWrite.saveToVDatFile(vt, table);
	}
	
	public void makeSignatureCorrelationGraph(String fn, float correlationThreshold, String selectedFields[]) throws Exception{
		Vector<String> vselectedFields = new Vector<String>();
		if(selectedFields!=null) for(String s: selectedFields) vselectedFields.add(s);
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(fn);
		fn = fn.substring(0, fn.length()-4);
		fn = fn+"_"+correlationThreshold+"_cyt.txt";
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		FileWriter fw = new FileWriter(fn);
		fw.write("FIELD1\tFIELD2\tCORRELATION\tCORRELATION_ABS\n");
		for(int i=0;i<vd.coordCount;i++){
			for(int j=i+1;j<vd.coordCount;j++){
				String fieldi = vt.fieldNames[vd.selector.selectedColumns[i]];
				String fieldj = vt.fieldNames[vd.selector.selectedColumns[j]];
				float xi[] = new float[vd.pointCount];
				float xj[] = new float[vd.pointCount];
				for(int k=0;k<vd.pointCount;k++) xi[k] = vd.massif[k][i];
				for(int k=0;k<vd.pointCount;k++) xj[k] = vd.massif[k][j];
				float corr = VSimpleFunctions.calcCorrelationCoeff(xi, xj);
				if(Math.abs(corr)>=correlationThreshold){
					if((selectedFields==null)||(vselectedFields.contains(fieldi)))
						fw.write(fieldi+"\t"+fieldj+"\t"+corr+"\t"+Math.abs(corr)+"\n");
				}
			}
		}
		fw.close();
		// testing for random correlations
		/*FileWriter fwr = new FileWriter(fn+".random");
		Random r = new Random();
		for(int k=0;k<10000;k++){
			float xr[] = new float[vd.pointCount];
			for(int l=0;l<vd.pointCount;l++) xr[l] = r.nextFloat()*2-1f;
			float maxcorr = -1f;
			for(int i=0;i<vd.coordCount;i++){
			float xi[] = new float[vd.pointCount];			
			for(int l=0;l<vd.pointCount;l++) xi[l] = vd.massif[l][i];
			float corr = VSimpleFunctions.calcCorrelationCoeff(xi, xr);
			if(Math.abs(corr)>maxcorr)
				maxcorr = Math.abs(corr);
			}
			fwr.write(maxcorr+"\n");
		}
		fwr.close();*/
	}
	
	public void makeTableCorrelationGraph(String table1, String prefix1, String table2, String prefix2, String folder, float correlationThreshold) throws Exception{
		VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(table1);
		vt1.makePrimaryHash(vt1.fieldNames[0]);
		VDataTable vt2 = VDatReadWrite.LoadFromVDatFile(table2);
		vt2.makePrimaryHash(vt2.fieldNames[0]);
		// make common list of objects
		HashSet<String> names_set = new HashSet<String>();
		Vector<String> names = new Vector<String>();
		for(int i=0;i<vt1.rowCount;i++){
			String name1 = vt1.stringTable[i][0];
			if(vt2.tableHashPrimary.get(name1)!=null){
				if(!names_set.contains(name1))
					names_set.add(name1);
			}
		}
		for(String s: names_set)
			names.add(s);
		Collections.sort(names);
		System.out.println(names.size()+" common objects are found.");
		
		FileWriter fw  = new FileWriter(folder+prefix1+"_"+prefix2+".txt");
		fw.write("FIELD1\tFIELD2\tCORRELATION\tCORRELATION_ABS\n");
		
		for(int i=0;i<vt1.colCount;i++)if(vt1.fieldTypes[i]==vt1.NUMERICAL){
			String fni = vt1.fieldNames[i];
			for(int j=0;j<vt2.colCount;j++)if(vt2.fieldTypes[j]==vt2.NUMERICAL){
				String fnj = vt2.fieldNames[j];
				float xi[] = new float[names.size()];
				float xj[] = new float[names.size()];
				for(int k=0;k<names.size();k++){
					xi[k] = Float.parseFloat(vt1.stringTable[vt1.tableHashPrimary.get(names.get(k)).get(0)][i]);
					xj[k] = Float.parseFloat(vt2.stringTable[vt2.tableHashPrimary.get(names.get(k)).get(0)][j]);
				}
				float corr = VSimpleFunctions.calcCorrelationCoeff(xi, xj);
				float abscorr = Math.abs(VSimpleFunctions.calcCorrelationCoeff(xi, xj));
				if(abscorr>=correlationThreshold){
					fw.write(fni+"_"+prefix1+"\t"+fnj+"_"+prefix2+"\t"+corr+"\t"+Math.abs(corr)+"\n");
				}
			}
		}
		
		fw.close();
	}
	
	public void computeThresholdsAndPValuesScore35(Vector<String> geneNames, int support, String suffix){
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(data, -1);
		data.makePrimaryHash(data.fieldNames[0]);
		minimalAmplitudeForScoreComputing = 0.5f;
		System.out.println("GENE\tSCORE_"+suffix+"\tTHRESH_"+suffix+"\tPVALUE_"+suffix+"\tSIDE_"+suffix+"\tSUPPORT_"+suffix+"\tEXCEPT_"+suffix);
		for(String s: geneNames){
			//System.out.println("Testing "+s);
			if(data.tableHashPrimary.get(s)!=null){
				int k = data.tableHashPrimary.get(s).get(0);
				float ms[] = vd.massif[k];
				Score35 score35 = scoreTail35(ms, data, true, support, 1000);
				System.out.println(s+"\t"+score35.value+"\t"+score35.threshold+"\t"+score35.pvalue+"\t"+score35.side+"\t"+score35.support+"\t"+score35.exceptions);
			}
		}
	}
	
	public void annotateSamplesWithScore35(String annotationFile, String data1, String data2, String prefix1, String prefix2, String outFile) throws Exception{
		Vector<String> vsamples = new Vector<String>();
		VDataTable vt1 = null;
		VDataTable vt2 = null;
		System.out.println("Loading "+annotationFile);
		VDataTable score35 = VDatReadWrite.LoadFromSimpleDatFile(annotationFile, true, "\t");
		score35.makePrimaryHash(score35.fieldNames[0]);
		System.out.println("Loading "+data1);
		vt1 = VDatReadWrite.LoadFromVDatFile(data1);
		vt1.makePrimaryHash(vt1.fieldNames[0]);
		
		if(data2!=null){
		System.out.println("Loading "+data2);
		vt2 = VDatReadWrite.LoadFromVDatFile(data2);
		vt2.makePrimaryHash(vt2.fieldNames[0]);
		System.out.println("Find common list of samples");
		HashSet<String> samples = new HashSet<String>();
		for(int i=1;i<vt1.colCount;i++){
			String fn = vt1.fieldNames[i];
			if(vt2.fieldNumByName(fn)!=-1){
				samples.add(fn);
			}
		}
		for(String s: samples) vsamples.add(s);
		System.out.println(vsamples.size()+" common samples found");
		}else{
			for(int i=1;i<vt1.colCount;i++)
				vsamples.add(vt1.fieldNames[i]);
		}
		Collections.sort(vsamples);		
		
		FileWriter fw = new FileWriter(outFile);
		fw.write("SAMPLE\t");
		for(int i=0;i<vt1.fieldInfo[1].length;i++) fw.write("D"+(i+1)+"\t");
		for(int i=0;i<score35.rowCount;i++){ 
			fw.write(score35.stringTable[i][0]+"_"+prefix1+"\t");
			if(data2!=null)
				fw.write(score35.stringTable[i][0]+"_"+prefix2+"\t");
		}
		fw.write("\n");
		
		for(String sample: vsamples){
			int if1 = vt1.fieldNumByName(sample);
			int if2 = -1; if(data2!=null) if2 = vt2.fieldNumByName(sample);
			fw.write(sample+"\t");
			for(int i=0;i<vt1.fieldInfo[if1].length;i++)
				fw.write(vt1.fieldInfo[if1][i]+"\t");
			for(int i=0;i<score35.rowCount;i++){
				String gname = score35.stringTable[i][0];
				int i1 = vt1.tableHashPrimary.get(gname).get(0);
				int i2 =-1; if(data2!=null) i2 = vt2.tableHashPrimary.get(gname).get(0);
				float val1 = Float.parseFloat(vt1.stringTable[i1][if1]);
				float val2 = 0f; if(data2!=null) val2 = Float.parseFloat(vt2.stringTable[i2][if2]);
				float thresh1 = Float.parseFloat(score35.stringTable[i][score35.fieldNumByName("THRESH_"+prefix1)]);
				float thresh2 = 0f; if(data2!=null) thresh2 = Float.parseFloat(score35.stringTable[i][score35.fieldNumByName("THRESH_"+prefix2)]);
				int side1 = Integer.parseInt(score35.stringTable[i][score35.fieldNumByName("SIDE_"+prefix1)]);
				int side2 = -1; if(data2!=null) side2 = Integer.parseInt(score35.stringTable[i][score35.fieldNumByName("SIDE_"+prefix2)]);
				int res1 = 0;
				int res2 = 0;
				if(side1==1)
					if(val1>=thresh1)
						res1 = side1;
				if(side1==-1)
					if(val1<=thresh1)
						res1 = side1;
				if(side2==1)
					if(val2>=thresh2)
						res2 = side2;
				if(side2==-1)
					if(val2<=thresh2)
						res2 = side2;		
				//fw.write(res1+"\t"+res2+"\t");
				fw.write(Math.abs(res1)+"\t");
				if(data2!=null)
					fw.write(Math.abs(res2)+"\t");
				fw.write(val1+"\t"+thresh1+"\t");
				//if((res1!=0)||(res2!=0)) 
				//	fw.write(val1+"\t"+val2+"\t");
			}
			fw.write("\n");
		}
		fw.close();
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(outFile, true, "\t");
		for(int i=0;i<score35.rowCount;i++){
			String fn = score35.stringTable[i][0];
			vt.fieldTypes[vt.fieldNumByName(fn+"_"+prefix1)] = vt.NUMERICAL;
			if(data2!=null)
				vt.fieldTypes[vt.fieldNumByName(fn+"_"+prefix2)] = vt.NUMERICAL;
		}
		VDatReadWrite.saveToVDatFile(vt, outFile.substring(0, outFile.length()-4)+".dat");
		VDatReadWrite.saveToVDatFile(vt.transposeTable(vt.fieldNames[0]), outFile.substring(0, outFile.length()-4)+"_T.dat");
	}
	
	public void compileGeneCopyNumber_BAF_Tables(String folder, String field) throws Exception{
		File dir = new File(folder);
		for(File f: dir.listFiles()){
			if(f.getName().startsWith("proc_"))if(f.getName().endsWith("txt")){
				String fn = f.getAbsolutePath();
				String fn1 = fn.substring(0, fn.length()-4)+"_"+field+".dat";
				extractCNBAFromGapFile(fn,field);
				createGeneCopyNumberTable(fn1);
			}
		}
		VDataTable vt = null;
		for(File f: dir.listFiles()){
			System.out.println("===========" + f.getName() +"===============");
			if(f.getName().startsWith("proc_"))if(f.getName().endsWith(field+"g.dat")){
				if(vt==null)
					vt = VDatReadWrite.LoadFromVDatFile(f.getAbsolutePath());
				else{
					VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(f.getAbsolutePath());
					vt = VSimpleProcedures.MergeTables(vt, "GENE", vt1, "GENE", "2");
				}
			}
		}
		Vector<String> vex = new Vector<String>(); vex.add("INDEX"); vex.add("LEN_SNP"); vex.add("SNP_START"); vex.add("SNP_END");
		vt = VSimpleProcedures.removeColumns(vt, vex);
		Vector<String> vstart = new Vector<String>(); vstart.add("GENE"); vstart.add("CHR"); vstart.add("START"); vstart.add("END"); vstart.add("GC");
		FileWriter fw = new FileWriter(folder+field+".txt");
		for(String s: vstart) fw.write(s+"\t");
		for(int i=0;i<vt.colCount;i++) if(!vstart.contains(vt.fieldNames[i])) fw.write(vt.fieldNames[i]+"\t"); fw.write("\n");
		for(int i=0;i<vt.rowCount;i++){
			for(String s: vstart) fw.write(vt.stringTable[i][vt.fieldNumByName(s)]+"\t");
			for(int j=0;j<vt.colCount;j++) if(!vstart.contains(vt.fieldNames[j])) fw.write(vt.stringTable[i][j]+"\t"); fw.write("\n");
		}
		fw.close();
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+field+".txt", true, "\t");
		for(int i=0;i<vt.colCount;i++) if(!vt.fieldNames[i].equals("GENE")) vt.fieldTypes[i] = vt.NUMERICAL;
		VDatReadWrite.saveToVDatFile(vt, folder+field+".dat");
	}
	
	public void extractCNBAFromGapFile(String fn, String fieldToExtract) throws Exception{
		System.out.println("=========================");
		System.out.println("====== "+(new File(fn)).getName()+"=======");
		System.out.println("=========================");
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		String sample = (new File(fn)).getName();
		sample = sample.substring(5, sample.length()-4);
		
		int k = sample_info.tableHashSecondary.get(sample).get(0);
		sample = sample_info.getV(k,0);
		
		System.out.println(vt.rowCount);
		FileWriter fw = new FileWriter(fn.substring(0, fn.length()-4)+"_"+fieldToExtract+".temp");
		fw.write("INDEX\tLEN_SNP\tSNP_START\tSNP_END\tCHR\t");
		for(int i=0;i<vt.colCount;i++) if(vt.fieldNames[i].equals(fieldToExtract))
			fw.write(sample+"\t");
		fw.write("\n");
		for(int j=0;j<vt.rowCount;j++){
			fw.write(vt.stringTable[j][vt.fieldNumByName("Index")]+"\t");
			fw.write(vt.stringTable[j][vt.fieldNumByName("LenSnp")]+"\t");
			fw.write(vt.stringTable[j][vt.fieldNumByName("PosSNPstart")]+"\t");
			fw.write(vt.stringTable[j][vt.fieldNumByName("PosSNPend")]+"\t");
			fw.write(vt.stringTable[j][vt.fieldNumByName("Chr")]+"\t");	
			for(int i=0;i<vt.colCount;i++) if(vt.fieldNames[i].equals(fieldToExtract)){
				fw.write(vt.stringTable[j][vt.fieldNumByName(vt.fieldNames[i])]+"\t");
			}
			fw.write("\n");					
		}
		fw.close();
		vt = VDatReadWrite.LoadFromSimpleDatFile(fn.substring(0, fn.length()-4)+"_"+fieldToExtract+".temp", true, "\t");
		vt.fieldTypes[vt.fieldNumByName(sample)] = vt.NUMERICAL;
		vt.fieldTypes[vt.fieldNumByName("CHR")] = vt.NUMERICAL;
		VDatReadWrite.saveToVDatFile(vt, fn.substring(0, fn.length()-4)+"_"+fieldToExtract+".dat");
	}
	
	public void createGeneCopyNumberTable(String fn){
		VDataTable vta = VDatReadWrite.LoadFromVDatFile(fn);

		String sample = (new File(fn)).getName();
		sample = sample.substring(5, sample.length()-4);
		if(sample.endsWith("_Copy")) sample = sample.substring(0, sample.length()-5);
		if(sample.endsWith("_Mallele")) sample = sample.substring(0, sample.length()-8);
		int k = sample_info.tableHashSecondary.get(sample).get(0);
		sample = sample_info.getV(k,0);
		
		CancerCellLine.AnnotateGenomicTableByGenePositions(vta, "SNP_START", "SNP_END", "C:/Datas/chr17/data/biomart_human_genes.txt");
		String fn1 = fn.substring(0, fn.length()-4)+"g.dat";
		vta = CancerCellLine.convertToGeneTable(vta, "GENE",sample, "C:/Datas/chr17/data/biomart_human_genes.txt");
		vta.fieldTypes[vta.fieldNumByName("GC")] = vta.NUMERICAL;
		VDatReadWrite.saveToVDatFile(vta, fn1);
	}
	
	public VDataTable extractNumericalTable(String dataTable, String sampleList, String geneList, String outFile) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(dataTable);
		vt.makePrimaryHash(vt.fieldNames[0]);
		Vector<String> sList = Utils.loadStringListFromFile(sampleList);
		Vector<String> gList = Utils.loadStringListFromFile(geneList);
		VDataTable vt1 = extractNumericalTable(vt, sList, gList, outFile);

		System.out.println("Save gene names");
		FileWriter fw3 = new FileWriter(geneList.substring(0, geneList.length()-4)+"_g.txt");
		for(int i=0;i<vt1.rowCount;i++)
			fw3.write(vt1.stringTable[i][0]+"\n");
		fw3.close();
		
		System.out.println("Make annotation numerical table");
		FileWriter fw1 = new FileWriter(sampleList.substring(0, sampleList.length()-4)+"_g.txt");
		for(int i=1;i<vt1.colCount;i++)
			//fw1.write(sample_info.stringTable[i][0]+"\n");
			fw1.write(vt1.fieldNames[i]+"\n");
		fw1.close();
		return vt;
		
	}
	
	public VDataTable extractNumericalTable(VDataTable vt, Vector<String> sampleList, Vector<String> geneList, String outFile) throws Exception{
		VDatReadWrite.useQuotesEverywhere = false;	
		VDataTable vt1 = VSimpleProcedures.selectRowsFromList(vt, geneList);
		VDataTable vt2 = VSimpleProcedures.SelectColumns(vt1, sampleList);
		sampleList.insertElementAt("GENE", 0);
		VDataTable vt3 = VSimpleProcedures.SelectColumns(vt1, sampleList);
		VDatReadWrite.saveToSimpleDatFilePureNumerical(vt2, outFile);
		return vt3;
	}

	
	

}
