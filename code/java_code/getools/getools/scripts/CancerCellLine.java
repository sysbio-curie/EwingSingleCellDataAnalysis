package getools.scripts;

import getools.GESignature;
import getools.GMTReader;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Vector;



import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.VStatistics;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;

public class CancerCellLine {

	/**
	 * @param args
	 */
	public class Gene{
		public String name = "";
		public int start = 0;
		public int end = 0;
		public String type = "";
		public float gc = 0f;
		public int calcOverlap(int st, int en){
			int overlap = 0;
			if((end<st)||(start>en))
				overlap = 0;
			else{
				if((start>st)&&(end<en))
					overlap = (end-start);
				else
					if((start<st)&&(end>en))
						overlap = (en-st);
					else
						if((start<st)&&(end<en))
							overlap = st-start;
						else
							overlap = end-en;
			}
			if(overlap<0)
				System.out.println("WARNING! Overlap is negative (should not be)");
			return overlap;
		}
	}
	
	
	public static void main(String[] args) {
		try{
			
			//selectColumnsFromExpressionData();
			//makeMutationTableFromMAF("C:/Datas/CellLineEncyclopedia/Broad2012/CCLE_Oncomap3_2012-04-09.maf");
			
			//String folder = "C:/Datas/CellLineEncyclopedia/";
			//String fname = "CN_BA_Affy6_BOPP_102012.txt";
			//String folder = "C:/Datas/DNARepairAnalysis/Hall_Janet/analysis/";
			//String fname = "CN_BA_celllines.txt";
			
			String folder = "C:/Datas/NaviCell2.1/cancer_cell_line_broad/";
			String fname = "CCL_Expression.txt";
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+fname, true, "\t");
			for(int i=1;i<vt.colCount;i++)
				vt.fieldTypes[i] = vt.NUMERICAL;
			vt = TableUtils.normalizeVDat(vt, true, false);
			VDatReadWrite.saveToSimpleDatFile(vt, folder+"CCL_Expression_centered.txt", true);
			System.exit(0);
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+fname, true, "\t");
			String typeToExtract = "CN";
			FileWriter fw = new FileWriter(folder+typeToExtract+".txt");
			fw.write("INDEX\tLEN_SNP\tSNP_START\tSNP_END\tCHR\t");
			for(int i=0;i<vt.colCount;i++) if(vt.fieldNames[i].startsWith(typeToExtract+"_"))
				fw.write(vt.fieldNames[i].substring(3,vt.fieldNames[i].length())+"\t");
			fw.write("\n");
			for(int j=0;j<vt.rowCount;j++){
				fw.write(vt.stringTable[j][vt.fieldNumByName("Index")]+"\t");
				fw.write(vt.stringTable[j][vt.fieldNumByName("LenSnp")]+"\t");
				fw.write(vt.stringTable[j][vt.fieldNumByName("PosSNPStart")]+"\t");
				fw.write(vt.stringTable[j][vt.fieldNumByName("PosSNPEnd")]+"\t");
				fw.write(vt.stringTable[j][vt.fieldNumByName("Chr")]+"\t");	
				for(int i=0;i<vt.colCount;i++) if(vt.fieldNames[i].startsWith(typeToExtract+"_")){
					fw.write(vt.stringTable[j][vt.fieldNumByName(vt.fieldNames[i])]+"\t");
				}
				fw.write("\n");					
			}
			fw.close();
			vt = VDatReadWrite.LoadFromSimpleDatFile(folder+typeToExtract+".txt", true, "\t");
			VDatReadWrite.saveToVDatFile(vt, folder+typeToExtract+".dat");*/
			
			
			/*VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/CellLineEncyclopedia/CCLE_sample_info_file_2012-04-06.txt",true,"\t");
			for(int i=0;i<vta.rowCount;i++){
				String snp = vta.stringTable[i][vta.fieldNumByName("SNP arrays")];
				String hist = vta.stringTable[i][vta.fieldNumByName("Histology")];
				String hist1 = vta.stringTable[i][vta.fieldNumByName("Hist Subtype1")];
				String ccl_name = vta.stringTable[i][vta.fieldNumByName("CCLE name")];
				String gender = vta.stringTable[i][vta.fieldNumByName("Gender")];												
				StringTokenizer st = new StringTokenizer(snp,"_");
				//System.out.println(snp+"\t");
				if(!snp.trim().equals("")){
				String prefix = st.nextToken();
				String prev = "";
				String s = "";
				while(st.hasMoreTokens()){
					prev = s; s = st.nextToken();
				}
				System.out.println(prefix+"_"+prev+"_"+s+"\t"+ccl_name+"\t"+hist+"\t"+hist1+"\t"+gender);
				}
			}*/
			
			/*//VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Neuroblastoma/annotated-segments.csv_window_CNA.txt",true,"\t");
			VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Neuroblastoma/temp.txt",true,"\t");
			System.out.println("Loaded.");
			Vector<String> ids = new Vector<String>();
			int countSNP = 0;
			String currentID = "";
			for(int i=0;i<vta.rowCount;i++){
				String id = vta.stringTable[i][vta.fieldNumByName("ID")];
				String pos = vta.stringTable[i][vta.fieldNumByName("Pos")];
				if(!currentID.equals(id)){
					System.out.println(currentID+"\t"+countSNP);
					currentID = id;
					ids.add(currentID); countSNP = 1;
				}else
					countSNP++;
			}
			FileWriter fw = new FileWriter("C:/Datas/Neuroblastoma/NEUROBL_CN.txt"); 
			fw.write("SNP_START\tSNP_END\tCHR");
			fw.close();*/
			
			//loadGenePositions("C:/Datas/chr17/data/biomart_human_genes.txt");
			
			
			//System.exit(0);
			
			//int lastColumn = 165;
			int lastColumn = 57;
			/*VDataTable vta = VDatReadWrite.LoadFromVDatFile(folder+"CN.dat");
			AnnotateGenomicTableByGenePositions(vta, "SNP_START", "SNP_END", "C:/Datas/chr17/data/biomart_human_genes.txt");
			AnnotateWithSubsetOfGenes(vta,"C:/Datas/DNARepairAnalysis/dna_repair_genes_all.txt","DNA_REPAIR");
			AnnotateWithSubsetOfGenes(vta,"C:/Datas/CancerGenome/census.txt","CANCER_CENSUS");			
			AnnotateWithGMTfile(vta,"C:/Datas/DNARepairAnalysis/dnarepair_path_reg_hugo.gmt","DNA_REPAIR_PATH");
			AnnotateWithGMTfile(vta,"C:/Datas/DNARepairAnalysis/dnarepair_path_reg_re_hugo.gmt","DNA_REPAIR_REACTIONS");
			AnnotateWithMMetagene(vta,"C:/Datas/HPRD9/hprd9_connectivity.txt","HPRD_CONNECTIVITY");
			SubtractAverageValuePerChromosomeArm(vta,6,lastColumn,false);
			VDatReadWrite.saveToVDatFile(vta, folder+"CNg.dat");*/
			
			
			/*VDataTable  vtt = VDatReadWrite.LoadFromVDatFile("C:/Datas/CellLineEncyclopedia/CN_samples.dat");
			annotateSamplesByDrugData(vtt);
			VDatReadWrite.saveToVDatFile(vtt, "C:/Datas/CellLineEncyclopedia/CN_samples_drugs.dat");
			filterMissingDrugValues(vtt, 24, 34, "C:/Datas/CellLineEncyclopedia/CN_samples_drugs");
			VDataTable vtd = makeDrugTable("C:/Datas/CellLineEncyclopedia/Sanger2012");
			VDatReadWrite.saveToVDatFile(vtd, "C:/Datas/CellLineEncyclopedia/Sanger2012/drugs.dat");
			filterMissingDrugValues(vtd, 2, 11, "C:/Datas/CellLineEncyclopedia/Sanger2012/drugs");			
			System.exit(0);*/
			
			/*VDataTable vtg = VDatReadWrite.LoadFromVDatFile(folder+"CNg.dat");
			countStatisticsOfSamples(vtg, 6, lastColumn, true);
			VDatReadWrite.saveToVDatFile(vtg, folder+"CNg_gl.dat");
			VDataTable vtg1 = FilterByNumericalFields(vtg,"DNA_REPAIR_PATH_NUM",1,20,null,-1,-1);		
			VDatReadWrite.saveToVDatFile(vtg1, folder+"CNg_gl_reg.dat");
			VDataTable vtg2 = FilterByNumericalFields(vtg,"NGAIN",80,200,"NLOSS",80,200);
			VDatReadWrite.saveToVDatFile(vtg2, folder+"CNg_gl_max.dat");
			VDataTable vtg3 = FilterByNumericalFields(vtg,"NGAIN2",50,200,"NLOSS2",50,200);
			VDatReadWrite.saveToVDatFile(vtg3, folder+"CNg_gl_max2.dat");*/
			
			/*VDataTable vtg = VDatReadWrite.LoadFromVDatFile(folder+"CNg.dat");
			VDataTable vtgg = convertToGeneTable(vtg,"GENE");
			VDatReadWrite.saveToVDatFile(vtgg, folder+"CNg_gene.dat");
			VDatReadWrite.saveToSimpleDatFile(vtgg, folder+"CNg_gene.txt");*/			
			
			/*VDataTable  vtt = VDatReadWrite.LoadFromVDatFile("C:/Datas/CellLineEncyclopedia/CN_samples.dat");			
			VDataTable vtgt = VDatReadWrite.LoadFromVDatFile("C:/Datas/CellLineEncyclopedia/CNg_gl_reg_transposed.dat");
			computeSetHitScores(vtgt,"C:/Datas/DNARepairAnalysis/dnarepair_path_reg_re_hugo.gmt",2,vtt);
			VDatReadWrite.saveToVDatFile(vtgt, "C:/Datas/CellLineEncyclopedia/CN_samples_scored.dat");*/
			
			//VDataTable vtgt = vtg.transposeTable("DNA_REPAIR_NAME");
			//VDatReadWrite.saveToVDatFile(vtgt, "C:/Datas/CellLineEncyclopedia/CNg_gl_reg_transposed.dat");			
			
			//System.exit(0);
			
			/*VDataTable vtg = VDatReadWrite.LoadFromVDatFile(folder+"CNg_gl.dat");
			VDataTable vtg1 = FilterByNumericalFields(vtg,"DNA_REPAIR_PATH_NUM",1,20,null,-1,-1);
			VDataTable vtg1t = vtg1.transposeTable("DNA_REPAIR_NAME");
			VDatReadWrite.saveToVDatFile(vtg1, folder+"CNg_gl_reg.dat");			
			VDatReadWrite.saveToVDatFile(vtg1t, folder+"CNg_gl_reg_transposed.dat");
			VDataTable vtg2 = FilterByNumericalFields(vtg,"COUNT_POSITIVE",29,100,"COUNT_NEGATIVE",29,30);
			VDatReadWrite.saveToVDatFile(vtg2, folder+"CNg_gl_devlocal.dat");*/
			
			/*VDataTable vtg = VDatReadWrite.LoadFromVDatFile(folder+"CNg_reg.dat");
			VDataTable vte = VDatReadWrite.LoadFromSimpleDatFile(folder+"cle.txt", true, "\t");
			VDataTable vtge = VSimpleProcedures.MergeTables(vtg, "DNA_REPAIR_NAME", vte, "Symbol", "0");
			VDatReadWrite.saveToVDatFile(vtge, folder+"CNge_reg.dat");*/
			
			VDataTable vte = VDatReadWrite.LoadFromSimpleDatFile(folder+"cle.txt", true, "\t");
			VDatReadWrite.saveToVDatFile(vte,"C:/Datas/DNARepairAnalysis/Hall_Janet/analysis/modules/data/cle.dat");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void AnnotateGenomicTableByGenePositions(VDataTable vt, String fieldFeatureStart, String fieldFeatureEnd, String genePositionsFile){
		Vector<Vector<Gene>> genes = loadGenePositions(genePositionsFile);
		vt.addNewColumn("GENE", "", "", vt.STRING, "");
		vt.addNewColumn("GC", "", "", vt.NUMERICAL, "");
		for(int i=0;i<vt.rowCount;i++){
			String chr = vt.stringTable[i][vt.fieldNumByName("CHR")];
			int chromosome = (int)Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("CHR")]);
			int start = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName(fieldFeatureStart)]);
			int end = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName(fieldFeatureEnd)]);
			Vector<Gene> gs = genes.get(chromosome-1);
			String names = "";
			float gc = 0;
			int k = 0;
			for(int j=0;j<gs.size();j++){
				//if(gs.get(j).name.equals("MTA3"))
				//	if(gs.get(j).calcOverlap(start, end)>0)
				//		System.out.println("MTA3 ("+gs.get(j).start+";"+gs.get(j).end+") overlap with "+start+";"+end+" (status "+vt.stringTable[i][vt.fieldNumByName("TCGA_04_1517")]+"): " +gs.get(j).calcOverlap(start, end));
				if(gs.get(j).calcOverlap(start, end)>0){
					names+=gs.get(j).name+";"; gc+=gs.get(j).gc; k++;
				}
			}
			if(names.length()>0) names = names.substring(0, names.length()-1); if(k!=0) gc/=(float)k;
			vt.stringTable[i][vt.fieldNumByName("GENE")] = names;
			vt.stringTable[i][vt.fieldNumByName("GC")] = ""+gc;			
		}
	}
	
	public static Vector<Vector<Gene>> loadGenePositions(String genePositionsFile){
		Vector<Vector<Gene>> genes = new Vector<Vector<Gene>>();
		for(int i=0;i<=23;i++) genes.add(new Vector<Gene>());
		VDataTable pos = VDatReadWrite.LoadFromSimpleDatFile(genePositionsFile, true, "\t");
		for(int i=0;i<pos.rowCount;i++){
			String chr = pos.stringTable[i][pos.fieldNumByName("CHR")];
			if(chr.equals("X")) chr = "23";
			if(chr.equals("Y")) chr = "24";			
			int chromosome = -1;
			try{
				chromosome = (int)Float.parseFloat(chr);
			}catch(Exception e){
				
			}
			if(chromosome!=-1){
			CancerCellLine c = new CancerCellLine();
			Gene g = c.new Gene();
			g.start = Integer.parseInt(pos.stringTable[i][pos.fieldNumByName("GENESTART")]);
			g.end = Integer.parseInt(pos.stringTable[i][pos.fieldNumByName("GENEEND")]);
			g.name = pos.stringTable[i][pos.fieldNumByName("GENE")];
			g.type = pos.stringTable[i][pos.fieldNumByName("TYPE")];
			g.gc = Float.parseFloat(pos.stringTable[i][pos.fieldNumByName("GC")]);
			String status = pos.stringTable[i][pos.fieldNumByName("STATUS")];
			if(status.equals("KNOWN"))if((g.type.equals("protein_coding"))||(g.type.equals("miRNA"))){
				Vector<Gene> gchr = genes.get(chromosome-1);
				gchr.add(g);
			}}
		}
		for(int i=0;i<24;i++){
		//	System.out.println("CHR"+(i+1)+"\t"+genes.get(i).size()+"\tgenes");
		}
		return genes;
	}
	
	public static void AnnotateWithSubsetOfGenes(VDataTable vt, String fileName, String fieldName){
		Vector<String> list = Utils.loadStringListFromFile(fileName);
		vt.addNewColumn(fieldName, "", "", vt.NUMERICAL, "0");
		vt.addNewColumn(fieldName+"_NAME", "", "", vt.STRING, "");		
		for(int i=0;i<vt.rowCount;i++){
			String gene = vt.stringTable[i][vt.fieldNumByName("GENE")];
			StringTokenizer st = new StringTokenizer(gene,";");
			Vector<String> gene_list = new Vector<String>();
			while(st.hasMoreTokens()) gene_list.add(st.nextToken());
			Vector<String> names = new Vector<String>(); 
			for(int j=0;j<list.size();j++){
				if(gene_list.contains(list.get(j))){
					names.add(list.get(j));
				}
			}
			String name = ""; for(int j=0;j<names.size();j++) name+=names.get(j)+";"; if(name.length()>0) name = name.substring(0, name.length()-1);			
			vt.stringTable[i][vt.fieldNumByName(fieldName)] = ""+names.size();
			vt.stringTable[i][vt.fieldNumByName(fieldName+"_NAME")] = name;					
		}
	}
	
	public static void AnnotateWithGMTfile(VDataTable vt, String fileName, String fieldName){
		Vector<GESignature> sets = GMTReader.readGMTDatabase(fileName);
		vt.addNewColumn(fieldName, "", "", vt.STRING, "");		
		vt.addNewColumn(fieldName+"_NUM", "", "", vt.NUMERICAL, "0");				
		for(int i=0;i<vt.rowCount;i++){
			String gene = vt.stringTable[i][vt.fieldNumByName("GENE")];
			Vector<String> names = new Vector<String>(); 
			for(int j=0;j<sets.size();j++){
				StringTokenizer st = new StringTokenizer(gene,";");
				while(st.hasMoreTokens()){
				String g = st.nextToken();
				if(sets.get(j).geneNames.contains(g)){
					if(!names.contains(g))
						names.add(sets.get(j).name);
				}
				}
			}
			String name = ""; for(int j=0;j<names.size();j++) name+=names.get(j)+";"; if(name.length()>0) name = name.substring(0, name.length()-1);
			vt.stringTable[i][vt.fieldNumByName(fieldName)] = name;
			vt.stringTable[i][vt.fieldNumByName(fieldName+"_NUM")] = ""+names.size();								
		}
	}
	
	public static void AnnotateWithMMetagene(VDataTable vt, String fileName, String fieldName){
		Vector<String> lines = Utils.loadStringListFromFile(fileName);
		Vector<String> gnames = new Vector<String>();
		Vector<Float> gvalues = new Vector<Float>();
		for(String s: lines){
			StringTokenizer st = new StringTokenizer(s,"\t");
			gnames.add(st.nextToken());
			gvalues.add(Float.parseFloat(st.nextToken()));
		}
		vt.addNewColumn(fieldName, "", "", vt.STRING, "");		
		vt.addNewColumn(fieldName+"_VALUE", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++){
			String gene = vt.stringTable[i][vt.fieldNumByName("GENE")];
			Vector<String> names = new Vector<String>();
			Vector<Float> values = new Vector<Float>();
			for(int j=0;j<gnames.size();j++){
				StringTokenizer st = new StringTokenizer(gene,";");
				while(st.hasMoreTokens()){
				String g = st.nextToken();
				if(gnames.get(j).equals(g)){
					if(!names.contains(g)){
						names.add(gnames.get(j));
						values.add(gvalues.get(j));
					}
				}
				}
			}
			String name = ""; float value = 0f; 
			for(int j=0;j<names.size();j++){ 
				name+=names.get(j)+";";
				if(Math.abs(values.get(j))>value)
					value = values.get(j);
			}
			if(name.length()>0) name = name.substring(0, name.length()-1);
			vt.stringTable[i][vt.fieldNumByName(fieldName)] = name;
			vt.stringTable[i][vt.fieldNumByName(fieldName+"_VALUE")] = ""+value;								
		}		
	}
	
	
	public static void SubtractAverageValuePerChromosomeArm(VDataTable vt, int col_begin, int col_end, boolean substituteOriginalValues){
		vt.addNewColumn("AVERAGE", "", "", vt.NUMERICAL, "0");		
		vt.addNewColumn("DEV_AV_ARM", "", "", vt.NUMERICAL, "0");
		vt.addNewColumn("COUNT_POSITIVE", "", "", vt.NUMERICAL, "0");
		vt.addNewColumn("COUNT_NEGATIVE", "", "", vt.NUMERICAL, "0");		
		float arm_averages[][] = new float[col_end-col_begin+1][48];
		float averages[] = new float[vt.rowCount];
		float deviation_from_arm_averages[] = new float[vt.rowCount];		
		int count_positive[] = new int[vt.rowCount];				
		int count_negative[] = new int[vt.rowCount];						
		int arm_counts[] = new int[48]; 		
		for(int i=0;i<vt.rowCount;i++){
			float arm = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("CHR")]);
			int arm_int = (int)(arm*2f+0.01f);
			arm_counts[arm_int-2]++;
			for(int j=col_begin-1;j<col_end;j++){
				float value = Float.parseFloat(vt.stringTable[i][j]);				
				arm_averages[j-col_begin+1][arm_int-2]+=value;
				averages[i]+=value;
			}
			averages[i]/=(float)(col_end-col_begin+1);
			//System.out.println(vt.stringTable[i][vt.fieldNumByName("INDEX")]+"\t"+vt.stringTable[i][vt.fieldNumByName("CHR")]);
		}
		for(int i=0;i<arm_counts.length;i++)
			if(arm_counts[i]==0)
				System.out.println("WARNING: No measurements for "+((float)(i+2)/2f));
		for(int i=0;i<arm_averages.length;i++)
			for(int j=0;j<arm_averages[i].length;j++){
				arm_averages[i][j]/=(float)arm_counts[j];
			}
		for(int i=0;i<vt.rowCount;i++){
			float arm = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("CHR")]);
			int arm_int = (int)(arm*2f+0.01f);
			deviation_from_arm_averages[i] = 0;
			for(int j=col_begin-1;j<col_end;j++){
				float value = Float.parseFloat(vt.stringTable[i][j]);
				if(value-arm_averages[j-col_begin+1][arm_int-2]>=1) count_positive[i]++;
				if(value-arm_averages[j-col_begin+1][arm_int-2]<=-1) count_negative[i]++;				
				deviation_from_arm_averages[i]+=value-arm_averages[j-col_begin+1][arm_int-2];
				if(substituteOriginalValues)
					vt.stringTable[i][j] = ""+(value-arm_averages[j-col_begin+1][arm_int-2]);
			}
			deviation_from_arm_averages[i]/=(float)(col_end-col_begin+1);
		}
		
		for(int i=0;i<vt.rowCount;i++){
			vt.stringTable[i][vt.fieldNumByName("AVERAGE")] = ""+averages[i];
			vt.stringTable[i][vt.fieldNumByName("DEV_AV_ARM")] = ""+deviation_from_arm_averages[i];			
			vt.stringTable[i][vt.fieldNumByName("COUNT_POSITIVE")] = ""+count_positive[i];			
			vt.stringTable[i][vt.fieldNumByName("COUNT_NEGATIVE")] = ""+count_negative[i];						
		}
		
	}
	
	public static VDataTable FilterByNumericalFields(VDataTable vt, String fn1, int min1, int max1, String fn2, int min2, int max2){
		Vector<Integer> selected = new Vector<Integer>();
		int count = 0;
		for(int i=0;i<vt.rowCount;i++){
			float value1 = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName(fn1)]);
			if((value1>=min1)&&(value1<=max1)){
				selected.add(i);
			}
			if(fn2!=null){
				float value2 = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName(fn2)]);
				if((value2>=min2)&&(value2<=max2)){
					if(!selected.contains(i)) selected.add(i);
				}
			}
		}
		return VSimpleProcedures.selectRows(vt, selected);
	}
	
	public static void countStatisticsOfSamples(VDataTable vt, int sampleBegin, int sampleEnd, boolean convertToGainLossTable){
		System.out.println("SAMPLE\tAVERAGE\tMEDIAN\tMAX\tSTDEV\tC0\tC1\tC2\tC3\tC4\tC5\tC6\tC7\tC8");
		Vector<Integer> baseLineValues = new Vector<Integer>();
		for(int i=sampleBegin-1;i<sampleEnd;i++){
			float average = 0f;
			float stddev = 0;
			VStatistics vst = new VStatistics(1);
			float counts[] = new float[9];
			for(int j=0;j<vt.rowCount;j++){
				float value = Float.parseFloat(vt.stringTable[j][i]);
				float x[] = new float[1];				
				x[0] = value;
				vst.addNewPoint(x);
				int k = (int)value;
				if(k>8) k=8;
				counts[k]++;
			}
			vst.calcMedians();
			vst.calculate();
			float cmax = 0; int imax = -1;
			for(int k=0;k<counts.length;k++)
				if(counts[k]>cmax){ cmax =counts[k]; imax = k;}
			baseLineValues.add(imax);
			System.out.println(vt.fieldNames[i]+"\t"+vst.means[0]+"\t"+vst.medians[0]+"\t"+imax+"\t"+vst.stdevs[0]+"\t"+counts[0]/vt.rowCount+"\t"+counts[1]/vt.rowCount+"\t"+counts[2]/vt.rowCount+"\t"+counts[3]/vt.rowCount+"\t"+counts[4]/vt.rowCount+"\t"+counts[5]/vt.rowCount+"\t"+counts[6]/vt.rowCount+"\t"+counts[7]/vt.rowCount+"\t"+counts[8]/vt.rowCount);
		}
		
		vt.addNewColumn("AVERAGE_GL", "", "", vt.NUMERICAL, "0");
		vt.addNewColumn("NGAIN", "", "", vt.NUMERICAL, "0");		
		vt.addNewColumn("NLOSS", "", "", vt.NUMERICAL, "0");		
		vt.addNewColumn("NGAIN2", "", "", vt.NUMERICAL, "0");		
		vt.addNewColumn("NLOSS2", "", "", vt.NUMERICAL, "0");				
		for(int k=0;k<vt.rowCount;k++){
			float average_gl = 0f;
			int ngain = 0;
			int ngain2 = 0;
			int nloss = 0;
			int nloss2 = 0;
		for(int i=sampleBegin-1;i<sampleEnd;i++){
			float value = Float.parseFloat(vt.stringTable[k][i]);
			if(value>8) value = 8;
			value = value-baseLineValues.get(i-sampleBegin+1);
			vt.stringTable[k][i] = ""+(int)value;
			average_gl+=value;
			if(value>0) ngain++;
			if(value<0) nloss++;
			if(value>1) ngain2++;
			if(value<-1) nloss2++;			
		}
		average_gl/=sampleEnd-sampleBegin;
		vt.stringTable[k][vt.fieldNumByName("AVERAGE_GL")] = ""+average_gl;
		vt.stringTable[k][vt.fieldNumByName("NGAIN")] = ""+ngain;
		vt.stringTable[k][vt.fieldNumByName("NLOSS")] = ""+nloss;
		vt.stringTable[k][vt.fieldNumByName("NGAIN2")] = ""+ngain2;
		vt.stringTable[k][vt.fieldNumByName("NLOSS2")] = ""+nloss2;	
		}
	}
	
	public static void annotateSamplesByDrugData(VDataTable vt) throws Exception{
		
		VDataTable vtsamples = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/CellLineEncyclopedia/Broad2012/CCLE_sample_info_file_2012-10-18.txt", true, "\t");
		//for(int i=0;i<vtsamples.fieldNames.length;i++)
		//	System.out.println(vtsamples.fieldNames[i]);
		Vector<String> sampleIds = new Vector<String>();
		Vector<String> celllines = new Vector<String>();
		for(int i=0;i<vtsamples.rowCount;i++){
			String snp = vtsamples.stringTable[i][vtsamples.fieldNumByName("SNP arrays")];
			String cellline = vtsamples.stringTable[i][vtsamples.fieldNumByName("Cell line primary name")];
			celllines.add(Utils.replaceString(cellline.toUpperCase(), "-", ""));
			//System.out.println(snp);
			if(!snp.equals("")){
			StringTokenizer st = new StringTokenizer(snp,"_");
			String s1 = null;
			String s2 = null;
			String s0 = st.nextToken();
			while(st.hasMoreTokens()){
				s2 = s1;
				s1 = st.nextToken();
			}
			sampleIds.add(s0+"_"+s2+"_"+s1);
			}else{
				sampleIds.add("");
			}
		}
		
		vt.addNewColumn("CELLLINE", "", "", vt.STRING, "_");		
		//vt.addNewColumn("CELLLINE_ALIASES", "", "", vt.STRING, "_");		
		
		for(int i=0;i<vt.rowCount;i++){
			String sampleId = vt.stringTable[i][vt.fieldNumByName("SAMPLE")];
			//System.out.println(sampleId);
			int k = sampleIds.indexOf(sampleId);
			if(k!=-1){
				String cellline = vtsamples.stringTable[k][vtsamples.fieldNumByName("Cell line primary name")];
				String cellline_alias = vtsamples.stringTable[k][vtsamples.fieldNumByName("Cell line aliases")]; 				
				vt.stringTable[i][vt.fieldNumByName("CELLLINE")] = cellline;
				//vt.stringTable[i][vt.fieldNumByName("CELLLINE_ALIASES")] = cellline_alias;				
			}else{
				System.out.println(sampleId+" is not found!");
			}
		}
		
		File dir = new File("C:/Datas/CellLineEncyclopedia/Sanger2012");
		HashSet<String> sangerLines = new HashSet<String>();
		vt.addNewColumn("SANGER_CLNAME", "", "", vt.STRING, "");				
		for(int i=0;i<dir.listFiles().length;i++){
			File f = dir.listFiles()[i];
			if(f.getName().endsWith(".csv")){
				VDataTable vtdrug = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), true, ",");
				String drug = f.getName().substring(0, f.getName().length()-4);
				vt.addNewColumn(drug, "", "", vt.NUMERICAL, "-10000");		
				int found = 0;
				for(int j=0;j<vtdrug.rowCount;j++){
					String cln = vtdrug.stringTable[j][vtdrug.fieldNumByName("Cell Line Name")];
					String cln_corr = Utils.replaceString(cln.toUpperCase(), "-", "");
					
					boolean cl_found = false;
					for(int k=0;k<vt.rowCount;k++){
						String scl = vt.stringTable[k][vt.fieldNumByName("CELLLINE")];
						String scl_corr = Utils.replaceString(scl.toUpperCase(), "-", "");
						if(scl_corr.equals(cln_corr)){
							vt.stringTable[k][vt.fieldNumByName("SANGER_CLNAME")] = cln;
							vt.stringTable[k][vt.fieldNumByName(drug)] =  vtdrug.stringTable[j][vtdrug.fieldNumByName("IC 50 High")];
							cl_found = true;
						}
					}
					
					if(!cl_found)
						System.out.println(cln+" is not found in Sanger");
					
					sangerLines.add(cln);
					if(celllines.indexOf(cln)!=-1) found++;
				}
				//System.out.println(f.getName()+": "+found+"("+(int)((float)(found)/vtdrug.rowCount*100)+"%) cell line names found from "+vtdrug.rowCount+" Sanger clnes out of "+celllines.size()+" Broad lines");
			}
		}
		int count=0;
		Vector<String> notFound = new Vector<String>();
		for(String s: sangerLines){
			String ss = Utils.replaceString(s.toUpperCase(), "-", "");
			if(celllines.indexOf(ss.toUpperCase())!=-1)
				count++;
			else
				notFound.add(ss.toUpperCase());
		}
		System.out.println(sangerLines.size()+" Sanger lines in drug files");
		System.out.println(count+" names are found in Broad");
		System.out.println("Not found:"); for(int i=0;i<notFound.size();i++) System.out.println(notFound.get(i));
		
	}
	
	public static void filterMissingDrugValues(VDataTable vt, int col_start, int col_end, String prefix){
		Vector<Vector<Integer>> selections = new Vector<Vector<Integer>>();
		selections.add(new Vector<Integer>());
		for(int i=col_start;i<col_end;i++)
			selections.add(new Vector<Integer>());
		for(int i=0;i<vt.rowCount;i++){
			boolean complete = true;
			for(int j=col_start;j<col_end;j++){
				String value = vt.stringTable[i][j-1];
				if(value.equals("-10000")) 
					complete = false;
				else
					selections.get(j-col_start+1).add(i);
			}
			if(complete)
				selections.get(0).add(i);
		}
		for(int i=col_start;i<col_end;i++){
			String colname = vt.fieldNames[i-1];
			VDataTable vti = VSimpleProcedures.selectRows(vt, selections.get(i-col_start+1));
			VDatReadWrite.saveToVDatFile(vti, prefix+"_"+colname+".dat");
		}
		VDataTable vtc = VSimpleProcedures.selectRows(vt, selections.get(0));
		VDatReadWrite.saveToVDatFile(vtc, prefix+"_complete.dat");
	}
	
	
	public static VDataTable makeDrugTable(String dir){
		File dirf = new File(dir);
		Vector<String> sangerLines = new Vector<String>();
		VDataTable vt = new VDataTable();
		for(int i=0;i<dirf.listFiles().length;i++){
			File f = dirf.listFiles()[i];
			if(f.getName().endsWith(".csv")){
				VDataTable vtdrug = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), true, ",");
				for(int j=0;j<vtdrug.rowCount;j++){
					String cln = vtdrug.stringTable[j][vtdrug.fieldNumByName("Cell Line Name")];
					if(!sangerLines.contains(cln)) 
						sangerLines.add(cln);
				}
			}
		}
		vt.rowCount = sangerLines.size();
		vt.colCount = 1;
		vt.fieldNames = new String[1]; vt.fieldNames[0] = "SANGER_CLNAME";
		vt.fieldTypes = new int[1]; vt.fieldTypes[0] = vt.STRING;
		vt.fieldDescriptions = new String[1];
		vt.fieldClasses = new String[1];
		vt.stringTable = new String[vt.rowCount][vt.colCount];
		
		for(int i=0;i<sangerLines.size();i++) vt.stringTable[i][0] = sangerLines.get(i);
		
		for(int i=0;i<dirf.listFiles().length;i++){
			File f = dirf.listFiles()[i];
			if(f.getName().endsWith(".csv")){
				VDataTable vtdrug = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), true, ",");
				String drug = f.getName().substring(0, f.getName().length()-4);
				vt.addNewColumn(drug, "", "", vt.NUMERICAL, "-10000");		
				int found = 0;
				for(int j=0;j<vtdrug.rowCount;j++){
					String cln = vtdrug.stringTable[j][vtdrug.fieldNumByName("Cell Line Name")];
					int k = sangerLines.indexOf(cln);
					vt.stringTable[k][vt.fieldNumByName(drug)] =  vtdrug.stringTable[j][vtdrug.fieldNumByName("IC 50 High")];
				}
			}
		}	
		return vt;
	}
	
	public static void computeSetHitScores(VDataTable vt, String setFileName, float lossThreshold, VDataTable sampleTable){
		Vector<GESignature> sets = GMTReader.readGMTDatabase(setFileName);
		System.out.println("NAME\tCOCKTAIL\tSCORE");
		for(int i=0;i<vt.rowCount;i++){
			Vector<String> names = new Vector<String>();
			Vector<String> setsHit = new Vector<String>();			
			for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
				String fn = vt.fieldNames[j];
				//if(fn.equals("PRKDC"))
				//	System.out.println();
				float value = Float.parseFloat(vt.stringTable[i][j]);
				if(value<=-lossThreshold){
					StringTokenizer st = new StringTokenizer(fn,"_");
					while(st.hasMoreTokens()){
						String fnn = st.nextToken();
						for(int k=0;k<sets.size();k++){
							if(sets.get(k).geneNames.contains(fnn)){
								if(!setsHit.contains(sets.get(k).name))
									setsHit.add(sets.get(k).name);
								if(!names.contains(fnn))
									names.add(fnn);
							}
						}
					}
				}
			}
			String name = ""; for(int j=0;j<names.size();j++) name+=names.get(j)+";"; if(name.length()>0) name = name.substring(0, name.length()-1);
			if(name.equals("")) name = "_";
			System.out.println(vt.stringTable[i][vt.fieldNumByName("NAME")]+"\t"+name+"\t"+setsHit.size());
		}
	}
	
	public static VDataTable convertToGeneTable(VDataTable vt, String geneColumn, String fieldToCompare, String genePositionsFile){
		Vector<Vector<Gene>> genePos = loadGenePositions(genePositionsFile);
		VDataTable res = new VDataTable();
		res.copyHeader(vt);
		Vector<String> listOfGenes = new Vector<String>();
		HashMap<String,Vector<Integer>> geneRows = new HashMap<String,Vector<Integer>>(); 
		for(int i=0;i<vt.rowCount;i++){
			String gene = vt.stringTable[i][vt.fieldNumByName(geneColumn)];
			Vector<String> genes = new Vector<String>();
			StringTokenizer st = new StringTokenizer(gene,";");
			while(st.hasMoreTokens()) 
				genes.add(st.nextToken());
			for(int j=0;j<genes.size();j++){
				if(!listOfGenes.contains(genes.get(j)))
					listOfGenes.add(genes.get(j));
				Vector<Integer> vi = geneRows.get(genes.get(j));
				if(vi==null) vi = new Vector<Integer>();
				vi.add(i);
				geneRows.put(genes.get(j), vi);
			}
			
		}
		Collections.sort(listOfGenes);
		res.rowCount = listOfGenes.size();
		res.stringTable = new String[res.rowCount][res.colCount];
		res.addNewColumn("START", "", "", res.NUMERICAL, "0");
		res.addNewColumn("END", "", "", res.NUMERICAL, "0");
		for(int i=0;i<res.rowCount;i++){
			if(i==(int)(0.001f*i)*1000)
				System.out.print(i+"\t");
			int k=-1;
			float minval = Float.MAX_VALUE;
			// We select the minimal value of copy number
			Vector<Integer> vi = geneRows.get(listOfGenes.get(i));
			if(fieldToCompare!=null){
			for(int ss=0;ss<vi.size();ss++){ 
				float f = Float.parseFloat(vt.stringTable[vi.get(ss)][vt.fieldNumByName(fieldToCompare)]);
				if(f<minval) { minval= f; k = vi.get(ss); }
			}}
			else{
				k = vi.get(0);
			}
			if(k==Integer.MAX_VALUE)
				System.out.println(listOfGenes.get(i)+" NOT FOUND");
			for(int j=0;j<vt.colCount;j++) res.stringTable[i][j] = vt.stringTable[k][j];
			res.stringTable[i][res.fieldNumByName(geneColumn)] = listOfGenes.get(i);
			
			int chr = (int)(Float.parseFloat(res.stringTable[i][res.fieldNumByName("CHR")]));
			Vector<Gene> v = genePos.get(chr-1);
			int ng = -1;
			for(int kk=0;kk<v.size();kk++)
				if(v.get(kk).name.equals(listOfGenes.get(i))){
					ng = kk;
					break;
				}
			
			res.stringTable[i][res.fieldNumByName("START")] = ""+v.get(ng).start;
			res.stringTable[i][res.fieldNumByName("END")] = ""+v.get(ng).end;
		}
		return res;
	}

	public static void selectColumnsFromExpressionData() throws Exception{
		VDataTable vte = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/NaviCell/data_examples/cancer_cell_line_broad/CCL_GeneExpression.txt", true, "\t");
		VDataTable full_list = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/NaviCell/data_examples/cancer_cell_line_broad/full_list.txt", true, "\t");
		full_list.makePrimaryHash("CCLE_NAME");
		Vector<String> sample_list = Utils.loadStringListFromFile("C:/Datas/NaviCell/data_examples/cancer_cell_line_broad/sample_list.txt");
		/*for(int i=0;i<sample_list.size();i++){
			if(full_list.tableHashPrimary.get(sample_list.get(i))==null)
				//System.out.println("\n"+sample_list.get(i)+" IS NOT FOUND");
				System.out.print(sample_list.get(i)+"\t");
			else{
			int k = full_list.tableHashPrimary.get(sample_list.get(i)).get(0);
			System.out.print(full_list.stringTable[k][full_list.fieldNumByName("CCLE_NAME")]+"\t");
			}
		}*/
		FileWriter fw =  new FileWriter("C:/Datas/NaviCell/data_examples/cancer_cell_line_broad/CCL_Expression.txt");
		
		fw.write("GENE\t");
		for(int j=0;j<vte.colCount;j++){
			String cname = vte.fieldNames[j];
			if(full_list.tableHashPrimary.get(cname)!=null){
				int k = full_list.tableHashPrimary.get(cname).get(0);
				String snp = full_list.stringTable[k][full_list.fieldNumByName("SNP")];
				if(sample_list.contains(snp)){
					fw.write(cname+"\t");
				}
			}
		}
		fw.write("\n");
		
		for(int i=0;i<vte.rowCount;i++){
			String desc = vte.stringTable[i][vte.fieldNumByName("Description")];
			fw.write(desc+"\t");
			for(int j=0;j<vte.colCount;j++){
				String cname = vte.fieldNames[j];
				if(full_list.tableHashPrimary.get(cname)!=null){
					int k = full_list.tableHashPrimary.get(cname).get(0);
					String snp = full_list.stringTable[k][full_list.fieldNumByName("SNP")];
					if(sample_list.contains(snp)){
						fw.write(vte.stringTable[i][j]+"\t");
					}
				}
			}
			fw.write("\n");
		}
		fw.close();
		
	}
	
	public static void makeMutationTableFromMAF(String fn){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		Vector<String> samples = new Vector<String>();
		Vector<String> genes = new Vector<String>();		
		for(int i=0;i<vt.rowCount;i++){
			String sample = vt.stringTable[i][vt.fieldNumByName("Tumor_Sample_Barcode")];
			String gene = vt.stringTable[i][vt.fieldNumByName("Hugo_Symbol")];			
			if(!samples.contains(sample)){
				samples.add(sample);
			}
			if(!genes.contains(gene)){
				genes.add(gene);
			}
		}
		Collections.sort(samples);
		Collections.sort(genes);
		String table[][] = new String[genes.size()][samples.size()];
		for(int i=0;i<vt.rowCount;i++){
			String sample = vt.stringTable[i][vt.fieldNumByName("Tumor_Sample_Barcode")];
			String gene = vt.stringTable[i][vt.fieldNumByName("Hugo_Symbol")];
			String mutation = vt.stringTable[i][vt.fieldNumByName("Variant_Classification")];
			table[genes.indexOf(gene)][samples.indexOf(sample)] = mutation;
		}		
		
		System.out.print("GENE\t");
		for(int i=0;i<samples.size();i++) System.out.print(samples.get(i)+"\t"); System.out.println();
		for(int i=0;i<genes.size();i++){
			System.out.print(genes.get(i)+"\t");
			for(int j=0;j<samples.size();j++){
				if(table[i][j]==null)
					System.out.print("_\t");
				else
					System.out.print(table[i][j]+"\t");
			}
			System.out.println();
		}
		
	}

}
