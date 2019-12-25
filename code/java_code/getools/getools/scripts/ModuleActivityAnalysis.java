package getools.scripts;

import getools.GESignature;
import getools.GMTReader;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import vdaoengine.analysis.*;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;

import getools.*;

public class ModuleActivityAnalysis {

	// Constants
	
	static int STANDARD_GMT = 0;
	static int GMT_WITH_WEIGHTS = 1;
	static int WEIGHT_MATRIX = 2;
	public static int PCA_STANDARD = 0;
	public static int PCA_FIXED_CENTER = 1;
	public static int NON_LINEAR = 2;
	
	// Options
	
	//static int typeOfModuleFile = GMT_WITH_WEIGHTS;
	static int typeOfModuleFile = STANDARD_GMT;
	//static int typeOfPCAUsage = PCA_STANDARD;
	static int typeOfPCAUsage = PCA_FIXED_CENTER;
	
	static int minimalNumberOfGenesInModule = 10;
	static int minimalNumberOfGenesInModuleFound = 8;	
	
	static String dataDatFileFolder = null;
	static String datFile = null;
	static String moduleFile = null;
	static String activitySignsFile = null;
	
	static float hotSpotGenesZthreshold = 1f;
	static float diffSpotGenesZthreshold = 1f;
	
	static int featureNumberForAveraging = -1;
	static String featureValueForAveraging = null;
	
	static int featureNumberForDiffAnalysis = -1;
	static String featureValuesForDiffAnalysis = null;
	
	static boolean centerData = false;
	static int fillMissingValues = -1;
	

	// Working objects
	
	public static Vector<Metagene> signatures = new Vector<Metagene>();
	static VDataTable table = null;
	static VDataTable alldata = null;
	static VDataTable moduleTable = null; 
	static HashMap moduleWeightsSum = null;
	
	
	static Vector<VDataTable> tables = new Vector<VDataTable>();
	
	public static String geneField = "GeneSymbol";
	

	public static void main(String[] args) {
		try{
			
			
			//Merge several gmts in one
			Vector<GESignature> gl = GMTReader.readGMTDatabase("C:/Datas/ROMA/data/mosaic/msigdb.v4.0.symbols_KEGG.gmt");
			//Vector<String> list = Utils.loadStringListFromFile("C:/Datas/ROMA/data/mosaic/G2M.txt");
			//Vector<String> list = Utils.loadStringListFromFile("C:/Datas/ROMA/data/mosaic/GLUCOSE_TRANSPORT.txt");
			Vector<String> list = Utils.loadStringListFromFile("C:/Datas/ROMA/data/mosaic/TRANSLATION.txt");
			Vector<String> merged = new Vector<String>();
				for(GESignature ges: gl){
					if(list.contains(ges.name)){
						System.out.println(ges.geneNames.size());
						for(String s: ges.geneNames)
							if(!merged.contains(s)) 
								merged.add(s);
					}
				}
			for(String s: merged)
				System.out.print(s+"\t");
			System.out.println();
			System.exit(0);
			
			// Reannotation
			//VDataTable vt1 = VDatReadWrite.LoadFromVDatFile("c:/datas/moduleactivities/data/bcpublic/bc4c.dat");
			/*VDataTable vt1 = VDatReadWrite.LoadFromVDatFile("c:/datas/moduleactivities/data/bcprivate/bc1na.dat");
			VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/moduleactivities/annot4.txt", true, "\t");
			VDataTable annot1 = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/moduleactivities/annot_95av2.txt", true, "\t");
			annot.makePrimaryHash("Probeset");
			annot1.makePrimaryHash("Probeset");
			for(int i=0;i<vt1.rowCount;i++){
				String ps = vt1.stringTable[i][vt1.fieldNumByName("Probe")];
				String gs = vt1.stringTable[i][vt1.fieldNumByName("GeneSymbol")];
				String gt = vt1.stringTable[i][vt1.fieldNumByName("GeneTitle")];
				Vector<Integer> rows = annot.tableHashPrimary.get(ps);
				Vector<Integer> rows1 = annot1.tableHashPrimary.get(ps);
				if(((rows==null)||(rows.size()==0))&&((rows1==null)||(rows1.size()==0)))
					System.out.println(ps+" not found");
				else{
					String gs1 = null;
					String gt1 = null;
					if(rows!=null){
						gs1 = annot.stringTable[rows.get(0)][annot.fieldNumByName("GeneSymbol")];
						gt1 = annot.stringTable[rows.get(0)][annot.fieldNumByName("GeneTitle")];
					}else{
						gs1 = annot1.stringTable[rows1.get(0)][annot1.fieldNumByName("GeneSymbol")];
						gt1 = annot1.stringTable[rows1.get(0)][annot1.fieldNumByName("GeneTitle")];
					}
					vt1.stringTable[i][vt1.fieldNumByName("GeneTitle")] = gt1;					
					if(!gs.equals(gs1)){
						vt1.stringTable[i][vt1.fieldNumByName("GeneSymbol")] = gs1;
						System.out.println(gs+" -> "+gs1);
					}
				}
			}
			//VDatReadWrite.saveToVDatFile(vt1,"c:/datas/moduleactivities/data/bcpublic/bc4c_.dat");
			VDatReadWrite.saveToVDatFile(vt1,"c:/datas/moduleactivities/data/bcprivate/bc1na_.dat");
			System.exit(0);
			*/
			/*moduleFile = "c:/datas/ModuleActivities/modules_proteins_050209.txt";
			//moduleFile = "c:/datas/ModuleActivities/rbmodules.gmt";

			dataDatFileFolder = "c:/datas/ModuleActivities/data/bcpublic/";
			datFile = "bc4c.dat";
			featureNumberForAveraging = 5;
			featureValueForAveraging = "normal";
			featureNumberForDiffAnalysis = 5;
			featureValuesForDiffAnalysis = "noninvasive;invasive";
			
			/*dataDatFileFolder = "c:/datas/ModuleActivities/data/bcprivate/";
			datFile = "bc1na.dat";
			//featureNumberForAveraging = 15;
			featureValueForAveraging = null;
			featureNumberForDiffAnalysis = 15;
			featureValuesForDiffAnalysis = "noninvasive;invasive";*/
			//hotSpotGenesZthreshold = 0.8f;
			
			//activitySignsFile = dataDatFileFolder+"moduleSigns.txt";

			
			for(int i=0;i<args.length;i++){
				if(args[i].equals("-typeOfModuleFile"))
					typeOfModuleFile = Integer.parseInt(args[i+1]);
				if(args[i].equals("-typeOfPCAUsage"))
					typeOfPCAUsage = Integer.parseInt(args[i+1]);
				if(args[i].equals("-dataDatFileFolder"))
					dataDatFileFolder = args[i+1];
				if(args[i].equals("-datFile"))
					datFile = args[i+1];
				if(args[i].equals("-moduleFile"))
					moduleFile = args[i+1];
				if(args[i].equals("-activitySignsFile"))
					activitySignsFile = args[i+1];
				if(args[i].equals("-hotSpotGenesZthreshold"))
					hotSpotGenesZthreshold = Float.parseFloat(args[i+1]);
				if(args[i].equals("-diffSpotGenesZthreshold"))
					diffSpotGenesZthreshold = Float.parseFloat(args[i+1]);
				if(args[i].equals("-featureNumberForAveraging"))
					featureNumberForAveraging = Integer.parseInt(args[i+1]);
				if(args[i].equals("-featureValueForAveraging"))
					featureValueForAveraging = args[i+1];
				if(args[i].equals("-featureNumberForDiffAnalysis"))
					featureNumberForDiffAnalysis = Integer.parseInt(args[i+1]);
				if(args[i].equals("-featureValuesForDiffAnalysis"))
					featureValuesForDiffAnalysis = args[i+1];
				if(args[i].equals("-centerData"))
					centerData = Integer.parseInt(args[i+1])==1;
				if(args[i].equals("-fillMissingValues"))
					fillMissingValues = Integer.parseInt(args[i+1]);
				if(args[i].equals("-minimalNumberOfGenesInModule"))
					minimalNumberOfGenesInModule = Integer.parseInt(args[i+1]);
				if(args[i].equals("-minimalNumberOfGenesInModuleFound"))
					minimalNumberOfGenesInModuleFound = Integer.parseInt(args[i+1]);
				
			}
			
			loadData();
			
			System.out.println("============================================");
			System.out.println((new Date()).toString()+"\n");
			System.out.println("dataDatFileFolder= "+dataDatFileFolder);
			System.out.println("datFile= "+datFile);
			System.out.println("moduleFile= "+moduleFile);
			System.out.println("centerData= "+centerData);			
			System.out.println("fillMissingValues= "+fillMissingValues);
			System.out.println("activitySignsFile= "+activitySignsFile);
			System.out.println("hotSpotGenesZthreshold= "+hotSpotGenesZthreshold);
			System.out.println("diffSpotGenesZthreshold= "+diffSpotGenesZthreshold);			
			System.out.println("typeOfPCAUsage= "+typeOfPCAUsage);
			System.out.println("typeOfModuleFile= "+typeOfModuleFile);			
			System.out.println("featureNumberForAveraging= "+featureNumberForAveraging);
			System.out.println("featureValueForAveraging= "+featureValueForAveraging);
			System.out.println("featureNumberForDiffAnalysis= "+featureNumberForDiffAnalysis);
			System.out.println("featureValuesForDiffAnalysis= "+featureValuesForDiffAnalysis);
			System.out.println("minimalNumberOfGenesInModule= "+minimalNumberOfGenesInModule);			
			System.out.println("minimalNumberOfGenesInModuleFound = "+minimalNumberOfGenesInModuleFound);						
			System.out.println("============================================");
			
			
			System.out.println();
			System.out.println("============================================");
			System.out.println("      Decomposing dataset into modules");
			System.out.println("============================================");
			decomposeDataSet();
			
			writeGeneModuleMatrix();			

			System.out.println();
			System.out.println("============================================");
			System.out.println("        Hot spot genes determination");
			System.out.println("============================================");
			findHotSpotGenes();

			System.out.println();
			System.out.println("============================================");
			System.out.println("       Creating module activity table");
			System.out.println("============================================");
			moduleActivityTable();

			if(featureNumberForAveraging!=-1){
			System.out.println();
			System.out.println("============================================");
			System.out.println("     Calculating average module activities");
			System.out.println("============================================");
			calcAverageModuleActivities(featureNumberForAveraging,featureValueForAveraging);
			}
			
			if(featureNumberForDiffAnalysis!=-1)if(featureValuesForDiffAnalysis!=null){
			System.out.println();
			System.out.println("============================================");
			System.out.println("     Differential genes determination");
			System.out.println("============================================");
			
			StringTokenizer st = new StringTokenizer(featureValuesForDiffAnalysis,";");
			String lab1s = st.nextToken();
			String lab2s = st.nextToken();
			Vector lab1 = new Vector();
			Vector lab2 = new Vector();
			st = new StringTokenizer(lab1s,",");
			while(st.hasMoreTokens())
				lab1.add(st.nextToken());
			st = new StringTokenizer(lab2s,",");
			while(st.hasMoreTokens())
				lab2.add(st.nextToken());
			findDiffGenes(featureNumberForDiffAnalysis,lab1,lab2);			
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void loadData() throws Exception{
		table = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(dataDatFileFolder+datFile);

		if(fillMissingValues>0){
			System.out.println("Filling missing values using first "+fillMissingValues+" principal components...");
			table = TableUtils.fillMissingValues(table,fillMissingValues);
		}
		
		
		if(centerData){
			System.out.println("Centering data (zero mean for any gene)...");
			table = TableUtils.normalizeVDat(table, true, false);
			String _fn = datFile;
			if(datFile.endsWith(".dat"))
				_fn = datFile.substring(0, datFile.length()-4);
			VDatReadWrite.saveToVDatFile(table, dataDatFileFolder+_fn+"_centered.dat");
		}
		
		if(table.fieldNumByName(geneField)==-1)
			for(int i=0;i<table.colCount;i++){
				if(table.fieldTypes[i]==table.STRING)
					geneField = table.fieldNames[i];
			}
		table.makePrimaryHash(geneField);
		
		
		if(typeOfModuleFile==STANDARD_GMT){
		Vector<GESignature> sigs = GMTReader.readGMTDatabase(moduleFile,minimalNumberOfGenesInModule);
		for(int i=0;i<sigs.size();i++){
			Metagene mg = new Metagene(sigs.get(i));
			mg.probeSets = mg.geneNames;			
			mg.initializeWeightsByOnes();
			signatures.add(mg);
		}
		}
		if(typeOfModuleFile==GMT_WITH_WEIGHTS){
			signatures = GMTReader.readGMTDatabaseWithWeights(moduleFile,minimalNumberOfGenesInModule);
			for(int i=0;i<signatures.size();i++){
				Metagene mg = new Metagene(signatures.get(i));
				mg.probeSets = mg.geneNames;			
			}
		}
		if(activitySignsFile!=null){
			try{
				LineNumberReader lr = new LineNumberReader(new FileReader(activitySignsFile));
				String s = null;
				while((s=lr.readLine())!=null){
					StringTokenizer st = new StringTokenizer(s,"\t");
					String name = st.nextToken();
					String sign = st.nextToken();
					int k = GESignature.findSignatureByName(name, signatures);
					if(k!=-1){
						Metagene mg = signatures.get(k);
						mg.activity = Float.parseFloat(sign);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void decomposeDataSet(){
		Vector allgenes = new Vector();
		
		Vector<Metagene> signatureCompleted = new Vector<Metagene>();
		
		for(int i=0;i<signatures.size();i++){
			Metagene sig = (Metagene)signatures.get(i);
			System.out.println();
			System.out.println(sig.name);			
			VDataTable moddata = VSimpleProcedures.selectRowsFromList(table, sig.geneNames);
			if(moddata.rowCount>=minimalNumberOfGenesInModuleFound){
			moddata = VSimpleProcedures.substituteRowsByStatistics(moddata, geneField, 4);
			tables.add(moddata);
			String fn = new String(sig.name);
			fn = Utils.replaceString(fn, ":", "_");
			fn = Utils.replaceString(fn, "/", "_");
			
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(moddata, -1);
			assignWeights(sig,vd,moddata);
			if(moddata.fieldNumByName("WEIGHT")!=-1)
				moddata.fieldTypes[moddata.fieldNumByName("WEIGHT")] = moddata.NUMERICAL;
			
			VDatReadWrite.saveToVDatFile(moddata, dataDatFileFolder+fn+".dat");
			VDatReadWrite.saveToVDatFile(moddata.transposeTable(moddata.fieldNames[1]), dataDatFileFolder+fn+"_T.dat");
			
			if(moddata.fieldNumByName("WEIGHT")!=-1)
				moddata.fieldTypes[moddata.fieldNumByName("WEIGHT")] = moddata.STRING;
			
			Vector notFound = new Vector();
			for(int j=0;j<sig.geneNames.size();j++){
				String id = (String)sig.geneNames.get(j);
				if(allgenes.indexOf(id)<0)
					allgenes.add(id);
				if(table.tableHashPrimary.get(id)==null){
					notFound.add(id);
				}
			}
			if(notFound.size()>0){
				System.out.print("NOT FOUND:\t");
				for(int j=0;j<notFound.size();j++)
					System.out.print((String)notFound.get(j)+"\t");
				System.out.println();
			}
			signatureCompleted.add(sig);
			}else{
				System.out.println("Signature is not complete (less than "+minimalNumberOfGenesInModuleFound+" genes)");
			}
		}
		
		System.out.println("\n\n"+signatureCompleted.size()+" signatures are complete from "+signatures.size());
		signatures = signatureCompleted;
		
		System.out.println("\nSelecting genes from all modules...");
		alldata = VSimpleProcedures.selectRowsFromList(table, allgenes);
		alldata = VSimpleProcedures.substituteRowsByStatistics(alldata, geneField, 4);
		
		alldata.addNewColumn("MODULE", "", "", alldata.STRING, "");
		alldata.makePrimaryHash(geneField);
		
		for(int i=0;i<signatures.size();i++){
			Metagene sig = (Metagene)signatures.get(i);
			for(int j=0;j<sig.probeSets.size();j++){
				String gn = (String)sig.probeSets.get(j);
				Vector<Integer> rows = alldata.tableHashPrimary.get(gn);
				if(rows!=null)
				for(int k=0;k<rows.size();k++){
					alldata.stringTable[rows.get(k)][alldata.fieldNumByName("MODULE")] += sig.name+";";
				}
			}
		}
		
		VDatReadWrite.saveToVDatFile(alldata, dataDatFileFolder+"allgenes.dat");
		VDatReadWrite.saveToVDatFile(alldata.transposeTable(alldata.fieldNames[0]), dataDatFileFolder+"allgenes_T.dat");
	}
	
	public static void findHotSpotGenes(){
		// Create report on hot-spot genes
		VDataSet vdglobal = VSimpleProcedures.SimplyPreparedDataset(table, -1);
		moduleWeightsSum = new HashMap();
		
		for(int i=0;i<tables.size();i++){
			String mname = ((GESignature)signatures.get(i)).name;
			System.out.print("\nModule "+mname+"\t"+((GESignature)signatures.get(i)).geneNames.size()+" genes\t");
			VDataTable dat = (VDataTable)tables.get(i);
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(dat, -1);
			VDataSet vdnn = VSimpleProcedures.SimplyPreparedDataset(dat, -1,false,false);

			VDataSet vdp = null;
			
			switch(typeOfPCAUsage){
			case 0/*PCA_STANDARD*/:
				assignWeights(signatures.get(i),vd,dat);				
				PCAMethod pca = new PCAMethod();
				pca.setDataSet(vd);
				pca.calcBasis(1);
				for(int k=0;k<vd.coordCount;k++)
					pca.linBasis.basis[0][k]*=((Metagene)signatures.get(i)).activity;			
				/*for(int k=0;k<pca.getBasis().spaceDimension;k++)
					System.out.print(pca.getBasis().basis[0][k]+"\t");
				System.out.println();*/
				vdp = pca.getProjectedDataset();
				float weightSum = calcSpecifiedWeightSum(dat,vdp,signatures.get(i));
				if(weightSum<0){
					for(int k=0;k<vd.coordCount;k++) pca.linBasis.basis[0][k]*=-1;			
					vdp = pca.getProjectedDataset();
				}
			break;
			case 1/*PCA_FIXED_CENTER*/:
				assignWeights(signatures.get(i),vd,dat);				
	             for(int kk=0;kk<vdnn.pointCount;kk++)
	                 vdglobal.processIntoSpace(vdnn.getVector(kk));
				PCAMethodFixedCenter pcaf = new PCAMethodFixedCenter();
				pcaf.setDataSet(vdnn);
				//System.out.println("Computing "+signatures.get(i).name);
				pcaf.calcBasis(1);
				for(int k=0;k<vd.coordCount;k++)
					pcaf.linBasis.basis[0][k]*=((Metagene)signatures.get(i)).activity;			
				vdp = pcaf.getProjectedDataset();
				weightSum = calcSpecifiedWeightSum(dat,vdp,signatures.get(i));
				if(weightSum<0){
					for(int k=0;k<vd.coordCount;k++) pcaf.linBasis.basis[0][k]*=-1;			
					vdp = pcaf.getProjectedDataset();
				}
			break;
			}			
			
			vdp.calcStatistics();
			vdp.simpleStatistics.calcMedians();
			Vector genePlusNames = new Vector();
			Vector genePlusScores = new Vector();
			Vector geneMinusNames = new Vector();
			Vector geneMinusScores = new Vector();
			for(int j=0;j<vdp.pointCount;j++){
				float f[] = vdp.getVector(j);
				float median = vdp.simpleStatistics.getMedian(0);
				float stdev = vdp.simpleStatistics.getStdDev(0);
				if(typeOfPCAUsage == PCA_FIXED_CENTER){
					median = 0;
					stdev = vdp.simpleStatistics.getStdDev0(0);
				}
				float z = (f[0]-median)/stdev;
				if(z<=-hotSpotGenesZthreshold){
					geneMinusNames.add(dat.stringTable[j][dat.fieldNumByName(geneField)]);
					geneMinusScores.add(new Float(z));
					//geneMinusScores.add(new Float(f[0]));
				}
				if(z>=+hotSpotGenesZthreshold){
					genePlusNames.add(dat.stringTable[j][dat.fieldNumByName(geneField)]);
					genePlusScores.add(new Float(z));
					//genePlusScores.add(new Float(f[0]));
				}
			}
			// make output
			DecimalFormat fm = new DecimalFormat("#.#");
			float scores[] = new float[geneMinusScores.size()];
			for(int j=0;j<geneMinusScores.size();j++) scores[j] = ((Float)geneMinusScores.get(j)).floatValue();
			int ind_minus[] = Algorithms.SortMass(scores);
			scores = new float[genePlusScores.size()];
			for(int j=0;j<genePlusScores.size();j++) scores[j] = ((Float)genePlusScores.get(j)).floatValue();
			int ind_plus[] = Algorithms.SortMass(scores);
			for(int j=0;j<geneMinusScores.size();j++){
				float z = ((Float)geneMinusScores.get(ind_minus[j])).floatValue();
				System.out.print((geneMinusNames.get(ind_minus[j]))+"("+fm.format(z)+")"+",");
			}
			System.out.print("\t");
			for(int j=0;j<genePlusScores.size();j++){
				float z = ((Float)genePlusScores.get(ind_plus[j])).floatValue();
				System.out.print((genePlusNames.get(ind_plus[j]))+"("+fm.format(z)+")"+",");
			}
			System.out.println();
		}
	}
	
	public static void findDiffGenes(int feature, Vector labs1, Vector labs2){
		DecimalFormat fm = new DecimalFormat("#.##");		
		Vector allGeneNames = new Vector();
		Vector allGeneScores = new Vector();
		for(int i=0;i<tables.size();i++){
			String mname = ((GESignature)signatures.get(i)).name;
			VDataTable dat = (VDataTable)tables.get(i);
			Vector genePlusNames = new Vector();
			Vector genePlusScores = new Vector();
			Vector geneMinusNames = new Vector();
			Vector geneMinusScores = new Vector();
			for(int j=0;j<dat.rowCount;j++){
				VStatistics stat1 = new VStatistics(1); 
				VStatistics stat2 = new VStatistics(1);
				for(int k=0;k<dat.colCount;k++)if(dat.fieldTypes[k]==dat.NUMERICAL){
					float f[] = new float[1];
					f[0] = Float.parseFloat(dat.stringTable[j][k]);
					String label = dat.fieldInfo[k][feature];
					if(labs1.indexOf(label)>=0){
						stat1.addNewPoint(f);
						//System.out.println("Added "+f[0]);
					}
					if(labs2.indexOf(label)>=0){
						stat2.addNewPoint(f);
						//System.out.println("Added "+f[0]);
					}
				}
				stat1.calculate();
				stat2.calculate();
				float z = (float)((stat2.getMean(0)-stat1.getMean(0))/Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)+stat2.getStdDev(0)*stat2.getStdDev(0)));
				String gn = dat.stringTable[j][dat.fieldNumByName(geneField)];
				if(allGeneNames.indexOf(gn)<0){
					allGeneNames.add(gn);
					allGeneScores.add(new Float(z));
				}
				if(z<=-diffSpotGenesZthreshold){
					geneMinusNames.add(gn);
					geneMinusScores.add(new Float(z));
				}
				if(z>=+diffSpotGenesZthreshold){
					genePlusNames.add(gn);
					genePlusScores.add(new Float(z));
				}
			}
			// make output
			if((genePlusScores.size()>0)||(geneMinusScores.size()>0)){
			System.out.println("Module "+mname+"\t"+((GESignature)signatures.get(i)).geneNames.size()+" genes\t");
			float scores[] = new float[geneMinusScores.size()];
			for(int j=0;j<geneMinusScores.size();j++) scores[j] = ((Float)geneMinusScores.get(j)).floatValue();
			int ind_minus[] = Algorithms.SortMass(scores);
			scores = new float[genePlusScores.size()];
			for(int j=0;j<genePlusScores.size();j++) scores[j] = ((Float)genePlusScores.get(j)).floatValue();
			int ind_plus[] = Algorithms.SortMass(scores);
			for(int j=0;j<geneMinusScores.size();j++){
				float z = ((Float)geneMinusScores.get(ind_minus[j])).floatValue();
				System.out.print((geneMinusNames.get(ind_minus[j]))+"("+fm.format(z)+")"+"\t");
			}
			for(int j=0;j<genePlusScores.size();j++){
				float z = ((Float)genePlusScores.get(ind_plus[j])).floatValue();
				System.out.print((genePlusNames.get(ind_plus[j]))+"("+fm.format(z)+")"+"\t");
			}
			System.out.println();
			}

		}
		float absscores[] = new float[allGeneNames.size()];
		for(int i=0;i<allGeneScores.size();i++){
			float z = ((Float)allGeneScores.get(i)).floatValue();
			//absscores[i] = Math.abs(z);
			absscores[i] = z;
		}
		int inds[] = vdaoengine.utils.Algorithms.SortMass(absscores);
		try{
			FileWriter fw = new FileWriter(dataDatFileFolder+"diffgenes.xls");
			for(int i=inds.length-1;i>=0;i--){
				fw.write(((String)allGeneNames.get(inds[i]))+"\t"+fm.format(((Float)allGeneScores.get(inds[i])).floatValue())+"\n");
			}
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void moduleActivityTable(){
		moduleTable = new VDataTable();
		moduleTable.copyHeader(table);
		moduleTable.rowCount = tables.size();
		moduleTable.stringTable = new String[moduleTable.rowCount][moduleTable.colCount];
		double evars[][] = new double[tables.size()][2];
		
		VDataSet vdglobal = VSimpleProcedures.SimplyPreparedDataset(table, -1);
		
		for(int i=0;i<tables.size();i++){
			Metagene mg = (Metagene)signatures.get(i);
			String mname = mg.name;
			VDataTable dat = (VDataTable)tables.get(i);
			moduleTable.stringTable[i][moduleTable.fieldNumByName(geneField)] = mname;

			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(dat, -1);
			VDataSet vdnn = VSimpleProcedures.SimplyPreparedDataset(dat, -1,false,false);
			
			switch(typeOfPCAUsage){
			case 0/*PCA_STANDARD*/:
				assignWeights(mg,vd,dat);				
				PCAMethod pca = new PCAMethod();
				pca.setDataSet(vd);
				pca.calcBasis(2);
				// correct the sign
				for(int k=0;k<vd.coordCount;k++)
					pca.linBasis.basis[0][k]*=((Metagene)signatures.get(i)).activity;
				VDataSet vdp = pca.getProjectedDataset();
				float weightSum = calcSpecifiedWeightSum(dat,vdp,signatures.get(i));
				if(weightSum<0){
					for(int k=0;k<vd.coordCount;k++) pca.linBasis.basis[0][k]*=-1;			
				}
				int k=0;
				for(int j=0;j<dat.colCount;j++)if(vd.selector.isColumnSelected(j)){
					moduleTable.stringTable[i][j] = ""+pca.getBasis().basis[0][k];
					k++;
				}
				vd.calcStatistics();				
				double explained_var[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
				evars[i][0] = explained_var[0];
				evars[i][1] = explained_var[1];
			break;
			case 1/*PCA_FIXED_CENTER*/:
				assignWeights(mg,vd,dat);
	             for(int kk=0;kk<vdnn.pointCount;kk++)
	                 vdglobal.processIntoSpace(vdnn.getVector(kk));
				PCAMethodFixedCenter pcaf = new PCAMethodFixedCenter();
				pcaf.setDataSet(vdnn);
				pcaf.calcBasis(2);
				// correct the sign
				for(k=0;k<vd.coordCount;k++)
					pcaf.linBasis.basis[0][k]*=((Metagene)signatures.get(i)).activity;
				vdp = pcaf.getProjectedDataset();
				weightSum = calcSpecifiedWeightSum(dat,vdp,signatures.get(i));
				if(weightSum<0){
					for(k=0;k<vd.coordCount;k++) pcaf.linBasis.basis[0][k]*=-1;			
				}
				k=0;
				for(int j=0;j<dat.colCount;j++)if(vd.selector.isColumnSelected(j)){
					moduleTable.stringTable[i][j] = ""+pcaf.getBasis().basis[0][k];
					k++;
				}
				vdnn.calcStatistics();
                double df[] = pcaf.calcDispersionsRelative(vdnn.simpleStatistics.totalSDVtozero*vdnn.simpleStatistics.totalSDVtozero);
				evars[i][0] = df[0];
				evars[i][1] = df[1];
			break;
			}
		}
		System.out.println("MODULE\tL1\tL1/L2\tNUMBER_OF_GENES");
		for(int i=0;i<tables.size();i++){
			Metagene mg = (Metagene)signatures.get(i);
			System.out.println(mg.name+"\t"+evars[i][0]+"\t"+evars[i][0]/evars[i][1]+"\t"+mg.geneNames.size());
		}
		
		VDatReadWrite.saveToVDatFile(moduleTable, dataDatFileFolder+"moduletable_simple.dat");
		VDatReadWrite.saveToSimpleDatFile(moduleTable, dataDatFileFolder+"moduletable_simple.txt", true);
		VDataTable moduleTableT = moduleTable.transposeTable(geneField);
		VDatReadWrite.saveToVDatFile(moduleTableT, dataDatFileFolder+"moduletable_simple_T.dat");
	}
	
	public static void calcAverageModuleActivities(int fieldN, String normalClassLabel) throws Exception{
		FileWriter fw = new FileWriter(dataDatFileFolder+"moduleActivities.xls");
		FileWriter fwz = new FileWriter(dataDatFileFolder+"moduleActivitiesZscores.xls");
		Vector classes = new Vector();
		for(int i=0;i<moduleTable.fieldNames.length;i++){
			String cl = moduleTable.fieldInfo[i][fieldN];
			if(cl!=null)
			if(classes.indexOf(cl)<0){
				classes.add(cl);
			}
		}
		int normalClass = classes.indexOf(normalClassLabel);		
		System.out.println("Normal class = "+normalClass);
		Vector stats = new Vector();
		VStatistics glstat = new VStatistics(moduleTable.rowCount);
		fw.write("NAME\t");
		fwz.write("NAME\t");
		for(int i=0;i<classes.size();i++){
			stats.add(new VStatistics(moduleTable.rowCount));
			fw.write((String)classes.get(i)+"\t");
		} 
		for(int i=0;i<classes.size();i++){
			fw.write((String)classes.get(i)+"_s\t");
		}		
		fw.write("\n"); fwz.write("\n");
		VDataTable transp = moduleTable.transposeTable(geneField);
		VDatReadWrite.saveToVDatFile(transp, dataDatFileFolder+"temp.dat");
		VDataSet transpd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(transp, -1);
		for(int i=0;i<transpd.pointCount;i++){
			float f[] = transpd.getVector(i);
			glstat.addNewPoint(f);
			String cl = transp.stringTable[i][transp.fieldNumByName("D"+(fieldN+1))];
			if(cl!=null){
				int k = classes.indexOf(cl);
				((VStatistics)stats.get(k)).addNewPoint(f);
			}
		}
		glstat.calculate(); glstat.calcMedians();
		for(int i=0;i<classes.size();i++){
			((VStatistics)stats.get(i)).calculate();
			((VStatistics)stats.get(i)).calcMedians();
		}
		for(int i=0;i<moduleTable.rowCount;i++){
			fw.write(moduleTable.stringTable[i][moduleTable.fieldNumByName(geneField)]+"\t");
			fwz.write(moduleTable.stringTable[i][moduleTable.fieldNumByName(geneField)]+"\t");			
			float min = glstat.getMin(i);
			float max = glstat.getMax(i);
			float glmedian = glstat.getMedian(i);
			float glmean = glstat.getMean(i);
			float glstdev = glstat.getStdDev(i);
			//System.out.println(min+"\t"+max);
			float stdev = 0f;
			for(int k=0;k<classes.size();k++){
				float sd = ((VStatistics)stats.get(k)).getStdDev(k);
				stdev += sd*sd;
			}
			stdev = (float)Math.sqrt(stdev);
			if(normalClass>=0){
				glmean = ((VStatistics)stats.get(normalClass)).getMean(i);
				stdev = ((VStatistics)stats.get(normalClass)).getStdDev(i);
				if(stdev==0) stdev = 1;
				glmedian = ((VStatistics)stats.get(normalClass)).getMedian(i);
			}
			for(int k=0;k<classes.size();k++){
				//float mean = ((VStatistics)stats.get(k)).getMedian(i);
				float mean = ((VStatistics)stats.get(k)).getMean(i);
				//System.out.println((String)classes.get(k)+":"+mean+"\t"+((VStatistics)stats.get(k)).pointsNumber);				
				//float val = (mean-min)/(max-min);
				//float val = (float)(mean-glmedian)/glstdev;
				//float val = mean-glmedian;
				//float val = (float)(mean-glmean)/stdev;
				float val = (float)(mean-glmean);
				//float val = mean;
				fw.write(""+val+"\t");
			}
			for(int k=0;k<classes.size();k++){
				float sv = ((VStatistics)stats.get(k)).getStdDev(i);
				fw.write(""+sv+"\t");
			}
			
			
			fwz.write(""+(((VStatistics)stats.get(0)).getMean(i)-((VStatistics)stats.get(1)).getMean(i))/stdev+"\t");
			
			fw.write("\n");
			fwz.write("\n");
		}
		fw.close();
		fwz.close();
	}
	
	public static void writeGeneModuleMatrix(){
		Vector allGeneNames = new Vector();
		for(int i=0;i<signatures.size();i++){
			Metagene mg = signatures.get(i);
			for(int j=0;j<mg.geneNames.size();j++)
				if(allGeneNames.indexOf((String)mg.geneNames.get(j))<0)
					allGeneNames.add((String)mg.geneNames.get(j));
		}
		Collections.sort(allGeneNames);
		try{
			FileWriter fw = new FileWriter(dataDatFileFolder+"GeneModuleMatrix.xls");
			fw.write("GENESYMBOL\t");
			for(int i=0;i<signatures.size();i++){
				Metagene mg = signatures.get(i);
				fw.write(mg.name+"\t");
			}
			fw.write("\n");
			
			for(int i=0;i<allGeneNames.size();i++){
				String gname = (String)allGeneNames.get(i);
				fw.write(gname+"\t");
				for(int j=0;j<signatures.size();j++){
					Metagene mg = signatures.get(j);
					int k = mg.geneNames.indexOf(gname); 
					if(k>=0){
						fw.write(((Float)mg.weights.get(k)).floatValue()+"\t");
					}else{
						fw.write("0\t");
					}
				}
				fw.write("\n");	
			}
			
			
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void assignWeights(Metagene mg, VDataSet vd, VDataTable dat){
		for(int k=0;k<mg.weights.size();k++){
			float w = ((Float)mg.weights.get(k)).floatValue();
			if(Math.abs(w-1f)>1e-6f){
				vd.weighted = true;
				//System.out.println("Weight "+mg.geneNames.get(k)+"="+w);
				break;
			}
		}
		
		if(vd.weighted){
			
			if(dat.fieldNumByName("WEIGHT")==-1)
				dat.addNewColumn("WEIGHT", "", "", dat.STRING, "1");
			
			float wSum = 0f;
			vd.weights = new float[vd.pointCount];
			for(int k=0;k<vd.pointCount;k++){
				String gname = dat.stringTable[k][dat.fieldNumByName(geneField)];
				int kk = mg.geneNames.indexOf(gname);
				float w = ((Float)mg.weights.get(kk)).floatValue();
				if(dat.fieldNumByName("WEIGHT")!=-1)
					dat.stringTable[k][dat.fieldNumByName("WEIGHT")] = ""+w;				
				if(Math.abs(w-1f)>1e-6f){
					vd.weights[k] = Math.abs(w);
				}else
					vd.weights[k] = 1;
				wSum+=w;
			}
			vd.weightSum = wSum;
		}
	}
	
	public static float calcSpecifiedWeightSum(VDataTable vt, VDataSet vdp, Metagene sig){
		float res = 0f;
		//System.out.println(sig.name);
		for(int i=0;i<vt.rowCount;i++){
			int k = sig.geneNames.indexOf(vt.stringTable[i][vt.fieldNumByName(geneField)]);
			if((Boolean)sig.weightSpecified.get(k)){
				res+=(Float)sig.weights.get(k)*vdp.massif[i][0];
				//System.out.println(vt.stringTable[i][vt.fieldNumByName("GeneSymbol")]+"\tProj="+vdp.massif[i][0]+"Weight=\t"+(Float)sig.weights.get(k)+"\tRes="+res);
			}
		}
		return res;
	}

}
