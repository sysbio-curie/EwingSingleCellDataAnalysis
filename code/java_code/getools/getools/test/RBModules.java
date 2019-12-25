package getools.test;

import java.io.*;
import java.text.*;
import java.util.*;

import getools.*;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.*;

public class RBModules {
	
	public static Vector tables = new Vector();
	public static String dataPrefix = "";
	public static String dataFile = "";
	public static Vector signatures = new Vector(); 
	public static VDataTable vt;
	public static VDataTable moduleTable;	
	public static int[] activitySigns = {
		1,//APC
		-1,//Apoptosis_entry
		-1,//CDC25C
		-1,//CycA2:CDK2
		-1,//CycB1:CDC2
		1,//CycC:CDK3
		1,//CycD1:CDK4/6
		1,//CycE1:CDK2
		1,//CycH:CDK7
		1,//E2F1
		1,//E2F1_targets
		1,//E2F2_targets
		1,//E2F3_targets
		-1,//E2F4
		1,//E2F4_targets
		1,//E2F5_targets
		1,//E2F6
		1,//E2F6_targets
		1,//E2F7_targets
		1,//E2F8_targets
		1,//p16/p15
		-1,//p27/p21
		-1,//RB
		1//Wee1
	};

	public static void main(String[] args) {
		try{
			
			/*VDataTable vt1 = VDatReadWrite.LoadFromVDatFile("c:/datas/rbmaps/rbmodules/data/bcpublic/bc2.dat");
			for(int i=0;i<vt1.rowCount;i++){
				for(int j=0;j<vt1.colCount;j++)if(vt1.fieldTypes[j]==vt1.NUMERICAL){
					float f = Float.parseFloat(vt1.stringTable[i][j]);
					f = (float)Math.log(f);
					vt1.stringTable[i][j] = ""+f;
				}
			}
			vt1 = TableUtils.normalizeVDat(vt1, true, false);
			VDatReadWrite.saveToVDatFile(vt1, "c:/datas/rbmaps/rbmodules/data/bcpublic/bc2c.dat");
			System.exit(0);*/
			
			/*VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/rbmaps/rbmodules/data/miller/gse3494a.txt",true,"\t");
			VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/gsda/hg_u133_plus_2.chip",true,"\t");
			vt1 = VSimpleProcedures.MergeTables(vt1, "ID", annot, "Probe", "_");
			VDatReadWrite.saveToVDatFile(vt1, "c:/datas/rbmaps/rbmodules/data/miller/gse3494a.dat");
			System.exit(0);*/
			
			/*VDataTable vt1 = VDatReadWrite.LoadFromVDatFile("c:/datas/rbmaps/rbmodules/data/miller/gse3494a.dat");
			vt1 = TableUtils.normalizeVDat(vt1, true, false);
			VDatReadWrite.saveToVDatFile(vt1, "c:/datas/rbmaps/rbmodules/data/miller/gse3494ac.dat");
			System.exit(0);*/
			
			String moduleFile = "c:/datas/rbmaps/rbmodules/modules_proteins_c2.gmt";
			
			//dataPrefix = "c:/datas/rbmaps/rbmodules/data/wang/";
			//dataFile = "wangn_a.dat";
			
			//String dataPrefix = "c:/datas/rbmaps/rbmodules/data/ewing_olivier/";
			//String dataFile = "tsc1na.dat";
			
			//String dataPrefix = "c:/datas/rbmaps/rbmodules/data/ewing_lessnick/";
			//String dataFile = "lessnick2006c.dat";
			
			//String dataPrefix = "c:/datas/rbmaps/rbmodules/data/vijver/";
			//String dataFile = "gse2990c.dat";

			//String dataPrefix = "c:/datas/rbmaps/rbmodules/data/miller/";
			//String dataFile = "gse3494ac.dat";

			//dataPrefix = "c:/datas/rbmaps/rbmodules/data/bcprivate/";
			//dataFile = "bc1na.dat";
			
			dataPrefix = "c:/datas/rbmaps/rbmodules/data/bcpublic/";
			//dataFile = "bc2c.dat";
			dataFile = "bc4c.dat";
			//dataFile = "bc5.dat";
			
			signatures = GMTReader.readGMTDatabase(moduleFile);
			
			vt = VDatReadWrite.LoadFromVDatFile(dataPrefix+dataFile);
			vt.makePrimaryHash("GeneSymbol");
			
			Vector allgenes = new Vector();
			
			for(int i=0;i<signatures.size();i++){
				GESignature sig = (GESignature)signatures.get(i);
				VDataTable moddata = VSimpleProcedures.selectRowsFromList(vt, sig.geneNames);
				moddata = VSimpleProcedures.substituteRowsByStatistics(moddata, "GeneSymbol", 4);
				tables.add(moddata);
				String fn = new String(sig.name);
				fn = Utils.replaceString(fn, ":", "_");
				fn = Utils.replaceString(fn, "/", "_");
				VDatReadWrite.saveToVDatFile(moddata, dataPrefix+fn+".dat");
				System.out.println(sig.name);
				System.out.print("NOT FOUND:\t");
				for(int j=0;j<sig.geneNames.size();j++){
					String id = (String)sig.geneNames.get(j);
					allgenes.add(id);
					if(vt.tableHashPrimary.get(id)==null){
						System.out.print(id+"\t");
					}
				}
				System.out.println();
			}
			VDataTable alldata = VSimpleProcedures.selectRowsFromList(vt, allgenes);
			alldata = VSimpleProcedures.substituteRowsByStatistics(alldata, "GeneSymbol", 4);
			VDatReadWrite.saveToVDatFile(alldata, dataPrefix+"allgenes.dat");
			
			findHotSpotGenes();
			moduleActivityTable();
			calcAverageModuleActivities(5,"normal");
			annotateCGH(dataPrefix+"cgh.csv","c:/datas/rbmaps/oncogene_locations");
			
			/*Vector labs1 = new Vector(); labs1.add("intrinsic_lumA"); labs1.add("intrinsic_lumB");
			Vector labs2 = new Vector(); labs2.add("intrinsic_basal");
			findDiffGenes(5,labs1,labs2);*/

			/*Vector labs1 = new Vector(); labs1.add("ER+"); 
			Vector labs2 = new Vector(); labs2.add("ER-");
			findDiffGenes(1,labs1,labs2);*/			
			
			/*Vector labs1 = new Vector(); labs1.add("0"); 
			Vector labs2 = new Vector(); labs2.add("1");
			findDiffGenes(0,labs1,labs2);*/			

			/*Vector labs1 = new Vector(); labs1.add("Ta"); labs1.add("T0"); //labs1.add("T1"); labs1.add("T1a"); labs1.add("T1b"); 
			Vector labs2 = new Vector(); labs2.add("T4"); labs2.add("T4a"); labs2.add("T4b"); //labs2.add("T2"); labs2.add("T2ousuperieur"); labs2.add("T3"); labs2.add("T3a"); labs2.add("T3b");  
			findDiffGenes(14,labs1,labs2);*/			
			
			
			// Create table of module activities
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void findHotSpotGenes(){
		// Create report on hot-spot genes
		float zthreshold = 1f;
		for(int i=0;i<tables.size();i++){
			String mname = ((GESignature)signatures.get(i)).name;
			System.out.print("Module "+mname+"\t"+((GESignature)signatures.get(i)).geneNames.size()+" genes\t");
			VDataTable dat = (VDataTable)tables.get(i);
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(dat, -1);
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.calcBasis(1);
			// correct the sign
			for(int k=0;k<vd.coordCount;k++)
				pca.linBasis.basis[0][k]*=activitySigns[i];			
			VDataSet vdp = pca.getProjectedDataset();
			vdp.calcStatistics();
			vdp.simpleStatistics.calcMedians();
			Vector genePlusNames = new Vector();
			Vector genePlusScores = new Vector();
			Vector geneMinusNames = new Vector();
			Vector geneMinusScores = new Vector();
			for(int j=0;j<vdp.pointCount;j++){
				float f[] = vdp.getVector(j);
				float z = (f[0]-vdp.simpleStatistics.getMedian(0))/vdp.simpleStatistics.getStdDev(0);
				if(z<=-zthreshold){
					geneMinusNames.add(dat.stringTable[j][dat.fieldNumByName("GeneSymbol")]);
					geneMinusScores.add(new Float(z));
				}
				if(z>=+zthreshold){
					genePlusNames.add(dat.stringTable[j][dat.fieldNumByName("GeneSymbol")]);
					genePlusScores.add(new Float(z));
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
		float zthreshold = 1f;
		for(int i=0;i<tables.size();i++){
			String mname = ((GESignature)signatures.get(i)).name;
			System.out.println("Module "+mname+"\t"+((GESignature)signatures.get(i)).geneNames.size()+" genes\t");
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
				float z = (float)((stat1.getMean(0)-stat2.getMean(0))/Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)+stat2.getStdDev(0)*stat2.getStdDev(0)));
				if(z<=-zthreshold){
					geneMinusNames.add(dat.stringTable[j][dat.fieldNumByName("GeneSymbol")]);
					geneMinusScores.add(new Float(z));
				}
				if(z>=+zthreshold){
					genePlusNames.add(dat.stringTable[j][dat.fieldNumByName("GeneSymbol")]);
					genePlusScores.add(new Float(z));
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
				System.out.print((geneMinusNames.get(ind_minus[j]))+"("+fm.format(z)+")"+"\t");
			}
			for(int j=0;j<genePlusScores.size();j++){
				float z = ((Float)genePlusScores.get(ind_plus[j])).floatValue();
				System.out.print((genePlusNames.get(ind_plus[j]))+"("+fm.format(z)+")"+"\t");
			}
			System.out.println();
		}
	}
	
	public static void moduleActivityTable(){
		moduleTable = new VDataTable();
		moduleTable.copyHeader(vt);
		moduleTable.rowCount = tables.size();
		moduleTable.stringTable = new String[moduleTable.rowCount][moduleTable.colCount];
		for(int i=0;i<tables.size();i++){
			String mname = ((GESignature)signatures.get(i)).name;
			VDataTable dat = (VDataTable)tables.get(i);
			moduleTable.stringTable[i][moduleTable.fieldNumByName("GeneSymbol")] = mname;

			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(dat, -1);
			PCAMethod pca = new PCAMethod();

			//VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(dat, -1);
			//PCAMethodFixedCenter pca = new PCAMethodFixedCenter();  
			
			pca.setDataSet(vd);
			pca.calcBasis(1);
			// correct the sign
			for(int k=0;k<vd.coordCount;k++)
				pca.linBasis.basis[0][k]*=activitySigns[i];			
			
			
			int k=0;
			for(int j=0;j<dat.colCount;j++)if(vd.selector.isColumnSelected(j)){
				moduleTable.stringTable[i][j] = ""+pca.getBasis().basis[0][k];
				k++;
			}
			
		}
		VDatReadWrite.saveToVDatFile(moduleTable, dataPrefix+"moduletable_simple.dat"); 
	}
	
	public static void calcAverageModuleActivities(int fieldN, String normalClassLabel) throws Exception{
		FileWriter fw = new FileWriter(dataPrefix+"moduleActivities.xls");
		FileWriter fwz = new FileWriter(dataPrefix+"moduleActivitiesZscores.xls");
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
		} fw.write("\n"); fwz.write("\n");
		VDataTable transp = moduleTable.transposeTable("GeneSymbol");
		VDatReadWrite.saveToVDatFile(transp, dataPrefix+"temp.dat");
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
			fw.write(moduleTable.stringTable[i][moduleTable.fieldNumByName("GeneSymbol")]+"\t");
			fwz.write(moduleTable.stringTable[i][moduleTable.fieldNumByName("GeneSymbol")]+"\t");			
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
			
			fwz.write(""+(((VStatistics)stats.get(0)).getMean(i)-((VStatistics)stats.get(1)).getMean(i))/stdev+"\t");
			
			fw.write("\n");
			fwz.write("\n");
		}
		fw.close();
		fwz.close();
	}
	
	public static void annotateCGH(String cghfn, String geneLocations) throws Exception{
		
		float ThresholdPositive = 1f;
		float ThresholdNegative = -0.5f;
		
		LineNumberReader lr = new LineNumberReader(new FileReader(geneLocations));
		VDataTable cgh = VDatReadWrite.LoadFromSimpleDatFile(cghfn, true, ",");
		VDataTable transp = moduleTable.transposeTable("GeneSymbol");
		transp.addNewColumn("GENEGAIN", "", "", VDataTable.STRING, "");
		transp.addNewColumn("GENELOSS", "", "", VDataTable.STRING, "");
		transp.addNewColumn("GAIN_RBPATHWAY", "", "", VDataTable.STRING, "");
		transp.addNewColumn("LOSS_RBPATHWAY", "", "", VDataTable.STRING, "");
		transp.addNewColumn("GAIN_RBMODULE", "", "", VDataTable.STRING, "");
		transp.addNewColumn("LOSS_RBMODULE", "", "", VDataTable.STRING, "");
		transp.addNewColumn("LABEL", "", "", VDataTable.STRING, "");
		String s = null;
		System.out.println("N\tGENE\tPOSITION\tALT_INVASIVE_LOSS\tALT_INVASIVE_GAIN\tALT_UNINVASIVE_LOSS\tALT_UNINVASIVE_GAIN\tALT_TOTAL");
		int kk = 0;
		while((s=lr.readLine())!=null){
			StringTokenizer st = new StringTokenizer(s,"\t");
			String geneName = st.nextToken().trim();
			
			boolean foundprotein = false;
			boolean foundgenes = false;
			for(int m=0;m<signatures.size();m++){
				GESignature sig = (GESignature)signatures.get(m);
				if(sig.name.indexOf("target")<0)
					if(sig.geneNames.indexOf(geneName)>=0)
							foundprotein = true;
				if(sig.name.indexOf("target")>=0)
					if(sig.geneNames.indexOf(geneName)>=0)
							foundgenes = true;
			}
			
			
			//System.out.print((++kk)+"\t"+geneName+"\t"+foundprotein+"\t"+foundgenes+"\t");
			System.out.print((++kk)+"\t"+geneName+"\t");
			// extracting position and chromosome
			String position = st.nextToken(); int k = position.indexOf("."); 
			String part1 = position.substring(0,k); int start = Integer.parseInt(part1); 
			int megabase = (int)(1f*start/1000); int kilobase = start - megabase*1000;
			int avPosition = 0;
			String part2 = position.substring(k+1); 
			//System.out.print(part1+"\t"+part2+"\t"+megabase+"\t");
			int end = Integer.parseInt(part2.trim());
			//System.out.print(end+"\t");
			if(kilobase>end)
				avPosition = (int)(megabase*1000000+0.5f*(kilobase+end+1000)*1000);
			else
				avPosition = (int)(megabase*1000000+0.5f*(kilobase+end)*1000);
			String chrom = st.nextToken().trim();
			//System.out.print(chrom+"\t");
			if(!chrom.equals("-")){
			
			k = chrom.indexOf("q"); 
			String chr = ""; 
			if(k>0) chr = chrom.substring(0,k);
			else{
				k = chrom.indexOf("p");
				chr = chrom.substring(0,k);
			}
			System.out.print(chr+":"+avPosition+",");			
			int found_invasive_loss = 0;
			int found_uninvasive_loss = 0;
			int found_invasive_gain = 0;
			int found_uninvasive_gain = 0;
			
			int closestBac = 0; int closestDist = Integer.MAX_VALUE;
			for(int j=0;j<cgh.rowCount;j++){
				String cgh_chr = cgh.stringTable[j][cgh.fieldNumByName("Chromosome")];
				int pos = Integer.parseInt(cgh.stringTable[j][cgh.fieldNumByName("Position")]);
				if(cgh_chr.equals(chr)){
					if(Math.abs(pos-avPosition)<closestDist){
						closestBac = j; closestDist = Math.abs(pos-avPosition);
					}
				}
			}	
			System.out.print(cgh.stringTable[closestBac][cgh.fieldNumByName("BAC")]+"\t");
			
			for(int i=0;i<transp.rowCount;i++)if(cgh.fieldNumByName(transp.stringTable[i][transp.fieldNumByName("NAME")]+"_GNL")>=0){
				String sampleName = transp.stringTable[i][transp.fieldNumByName("NAME")];
				String gnl = cgh.stringTable[closestBac][cgh.fieldNumByName(sampleName+"_GNL")];
				String outl = cgh.stringTable[closestBac][cgh.fieldNumByName(sampleName+"_Outlier")];
				float ratio = 0f;
				String rat = cgh.stringTable[closestBac][cgh.fieldNumByName(sampleName+"_log2ratio")];
				if(!rat.equals("")) ratio = Float.parseFloat(rat);
				if((ratio>0)&&(ratio<=ThresholdPositive)) gnl = "0";
				if((ratio<0)&&(ratio>=ThresholdNegative)) gnl = "0";
				int gainloss = 0;
				if(outl==null){
					gnl = ""; outl = "";
				}
				if(!outl.equals("1"))if(!outl.equals("-1")){
					if((closestBac>0)&&(closestBac<cgh.rowCount))
					if(gnl.equals("")){
						int gainloss1 = 0;
						int gainloss2 = 0;
						rat = "";
						String cgh_chr = cgh.stringTable[closestBac-1][cgh.fieldNumByName("Chromosome")];
						gnl = cgh.stringTable[closestBac-1][cgh.fieldNumByName(sampleName+"_GNL")];
						rat = cgh.stringTable[closestBac-1][cgh.fieldNumByName(sampleName+"_log2ratio")];
						if(!rat.equals("")) ratio = Float.parseFloat(rat);
						if((ratio>0)&&(ratio<ThresholdPositive)) gnl = "0";
						if((ratio<0)&&(ratio>ThresholdNegative)) gnl = "0";
						if((!gnl.equals(""))&&(cgh_chr.equals(chr))) gainloss1 = Integer.parseInt(gnl);
						
						rat = "";
						cgh_chr = cgh.stringTable[closestBac+1][cgh.fieldNumByName("Chromosome")];
						gnl = cgh.stringTable[closestBac+1][cgh.fieldNumByName(sampleName+"_GNL")];
						rat = cgh.stringTable[closestBac+1][cgh.fieldNumByName(sampleName+"_log2ratio")];
						if(!rat.equals("")) ratio = Float.parseFloat(rat);
						if((ratio>0)&&(ratio<ThresholdPositive)) gnl = "0";
						if((ratio<0)&&(ratio>ThresholdNegative)) gnl = "0";
						if((!gnl.equals(""))&&(cgh_chr.equals(chr))) gainloss2 = Integer.parseInt(gnl);
						gainloss = gainloss1+gainloss2;
					}else{
						gainloss = Integer.parseInt(gnl);
						if(gainloss==1) gainloss = 0;
					}
				}
				if(gainloss!=0){
					String prefix = gainloss>0?"GAIN":"LOSS";
					String sign = gainloss>0?"^":"-";
					String cell = transp.stringTable[i][transp.fieldNumByName("GENE"+prefix)];
					String commas = "";
					//if(!cell.equals("")) commas = sign;
					if(cell.equals("")) cell = "/";
					cell = cell+sign+geneName;
					transp.stringTable[i][transp.fieldNumByName("GENE"+prefix)] = cell;
					
					String modulesFound = "";
					String oldcell = transp.stringTable[i][transp.fieldNumByName(prefix+"_RBMODULE")];
					for(int m=0;m<signatures.size();m++){
						GESignature sig = (GESignature)signatures.get(m);
						if(sig.name.indexOf("target")<0)
							if(sig.geneNames.indexOf(geneName)>=0)
								if(oldcell.indexOf(sig.name)<0)
									modulesFound = sign+sig.name;
					}
					cell = transp.stringTable[i][transp.fieldNumByName(prefix+"_RBMODULE")];
					if(cell.equals("")) cell = "/";
					cell = cell+modulesFound;
					transp.stringTable[i][transp.fieldNumByName(prefix+"_RBMODULE")] = cell;					

					if(!modulesFound.equals("")){
						cell = transp.stringTable[i][transp.fieldNumByName(prefix+"_RBPATHWAY")];
						if(cell.equals("")) cell = "/";						
						cell = cell+sign+geneName;
						transp.stringTable[i][transp.fieldNumByName(prefix+"_RBPATHWAY")] = cell;
						String labl = transp.stringTable[i][transp.fieldNumByName("LABEL")];
						if(labl.equals("")) labl = "/";
						labl+=sign+geneName;
						transp.stringTable[i][transp.fieldNumByName("LABEL")] = labl;
					}
					
					/*cell = transp.stringTable[i][transp.fieldNumByName("GENE"+prefix)];
					commas = "";
					if(!cell.equals("")) commas = ","; cell = cell+commas+geneName;
					transp.stringTable[i][transp.fieldNumByName("GENE"+prefix)] = cell;*/
					
					String d6 = transp.stringTable[i][transp.fieldNumByName("D6")];
					//System.out.println(d6);
					if(gainloss>0) if(d6.equals("invasive")) found_invasive_gain++; else found_uninvasive_gain++;
					if(gainloss<0) if(d6.equals("invasive")) found_invasive_loss++; else found_uninvasive_loss++;
				}
			}
			System.out.print(found_invasive_loss+"\t"+found_invasive_gain+"\t"+found_uninvasive_loss+"\t"+found_uninvasive_gain+"\t"+(found_invasive_loss+found_invasive_gain+found_uninvasive_loss+found_uninvasive_gain)+"\t");
			}
			System.out.println();			
		}
		lr.close();
		VDatReadWrite.saveToVDatFile(transp, dataPrefix+"cgh_samples.dat");
	}
	
	
}
