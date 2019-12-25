package getools.scripts;

import fr.curie.BiNoM.pathways.analysis.structure.Attribute;
import fr.curie.BiNoM.pathways.analysis.structure.Edge;
import fr.curie.BiNoM.pathways.analysis.structure.Graph;
import fr.curie.BiNoM.pathways.analysis.structure.Node;
import fr.curie.BiNoM.pathways.utils.MetaGene;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import getools.GESignature;
import getools.GMTReader;
import getools.Metagene;
import getools.TableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.ProcessBuilder.Redirect;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vdaoengine.ProcessTxtData;
import vdaoengine.analysis.PCAMethod;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;


public class MOSAICAnalysis {

	public static String prefix = null;
	public static String dataFolder = null;
	String dataFile = null;
	String sampleAnnotationFile = null;
	public static int kNeighbours = 10;
	public static float mit_perc_score = 0.1f;
	public static int minReadCounts = 6000;
	public static int maxReadCounts = 40000;
	public static String signatureDefinitionFile = "C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt";
	public static String matlabCodeFolder = "./";
	
	public VDataTable data = null;
	
	public static int numDigits = 2;

	
	public static void main(String[] args) {
		try{

			
			MOSAICAnalysis mos = new MOSAICAnalysis();
			
			readJSONSPringGraph("C:/GitPrograms/SPRING-master/datasets/pdx1057_nufp5/graph_data.json");
			System.exit(0);
			//String fn = "C:/Datas/MOSAIC/expression/chromium_data/all_pdx/pdx856_tb.txt";
			//readcountPercentageScores(fn,"C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt");
			//String fn = "C:/Datas/ColorMap/temp.txt";
			//String fn = "C:/Datas/ColorMap/aggregated_1964_log_ica_S.xls";
			//String fn = "C:/Datas/ColorMap/aggregated_1964_log_ica_S_copy.txt";
			
			//ComputeAverageModuleValues("C:/Datas/ColorMap/aggregated_1964_log_ica_S.xls","C:/Datas/ColorMap/acsn2_global_master.gmt","C:/Datas/ColorMap/aggregated_1964_log_ica_S.xls_filtered_var.txt");
			//ComputeAverageModuleValues_fast("C:/Datas/ColorMap/aggregated_1964_log_ica_S.xls","C:/Datas/ColorMap/acsn2_global_master.gmt");
			//System.exit(0);
			
			/*String fn = "C:/Datas/ColorMap/aggregated_1964_log_ica_S.xls";
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
			TableUtils.findAllNumericalColumns(vt);
			vt = takeOnlyTopContributingGenes(vt,7f);
			VDatReadWrite.useQuotesEverywhere = false;
			fn = fn.substring(0, fn.length()-4)+"_onlytop.txt";
			VDatReadWrite.saveToSimpleDatFile(vt, fn);
			//ComputeAverageModuleValues(fn,"C:/Datas/ColorMap/acsn2_global_master.gmt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/agg1964_nufp_variances.txt");
			ComputeAverageModuleValues(fn,"C:/Datas/ColorMap/acsn2_global_master.gmt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/agg1964_nufp_variances.txt");
			
			System.exit(0);*/
			
			
			 //prefix = "pdx857"; float naive_mit_score = 1.8f; float pooled_mit_score = 2.3f; mit_perc_score = 0.1f;
			//prefix = "pdxa"; float naive_mit_score = 1.8f; float pooled_mit_score = 2.3f; mit_perc_score = 1f;
			//String pdx = "pdx861"; float naive_mit_score = 1.8f; float pooled_mit_score = 2.32f; float mit_perc_score = 0.1; 
			//String pdx = "pdx856"; float naive_mit_score = 1.8f; float pooled_mit_score = 1.55f; float mit_perc_score = 0.05f;
			//String pdx = "pdx471"; float naive_mit_score = 1.8f; float pooled_mit_score = 2.2f;
			//String pdx = "pdx184"; float naive_mit_score = 1.8f; float pooled_mit_score = 1.55f;
			//String pdx = "pdx352"; float naive_mit_score = 1.8f; float pooled_mit_score = 2.2f;
			
			String prefix = "pdx1057"; float naive_mit_score = 1.8f; float pooled_mit_score = 2.2f;
			
			//dataFolder = "c:/Datas/MOSAIC/expression/chromium_data/all_pdx/";
			dataFolder = "c:/Datas/MOSAIC/expression/chromium_data/final/data/";
			//String folder = "c:/Datas/MOSAIC/expression/chromium_data/three_novel_PDXs/";
			//ProcessPDXDataset_qc(dataFolder,prefix+".txt",naive_mit_score,pooled_mit_score,mit_perc_score,minReadCounts,maxReadCounts);
			ProcessPDXDataset_qc(dataFolder,prefix+".txt",naive_mit_score,pooled_mit_score,100f,6000,40000);
			//ProcessPDXDataset_process(dataFolder,prefix+".txt");
			//ProcessPDXDataset_process_test(dataFolder,prefix+".txt");
			//ProcessPDXDataset_SPRING(dataFolder,prefix+".txt",kNeighbours);

			//String folder = "c:/Datas/MOSAIC/expression/chromium_data/three_novel_PDXs/";
			//String fn = "pdx857.tpmNorm.txt";
			//String pref = fn.substring(0,fn.length()-4);
			
			
			System.exit(0);
			
			//transformGeneProjectionMatrix("C:/Datas/MOSAIC/analysis/roma/PDX352_magic/GeneProjectionMatrix.xls");
			//transformGeneProjectionMatrix("C:/Datas/MOSAIC/analysis/roma/ASP14_TS_all_recalc/GeneProjectionMatrix.xls","C:/Datas/MOSAIC/analysis/roma/ASP14_TS_ul_meanvalues.txt");
			//transformGeneProjectionMatrix("C:/Datas/MOSAIC/analysis/roma/PDX471_all_recalc/GeneProjectionMatrix.xls","C:/Datas/MOSAIC/analysis/roma/PDX471_u13kf_meanvalues.txt");
			//transformGeneProjectionMatrix("C:/Datas/MOSAIC/analysis/roma/PDX352_magic/GeneProjectionMatrix.xls","C:/Datas/MOSAIC/analysis/roma/PDX352_u10kf_MAGIC_meanvalues.txt");
			//System.exit(0);

			//makeRNKFileFromScoreExtremePoints("C:/Datas/MOSAIC/analysis/roma/PDX352_u10kfc.txt","C:/Datas/MOSAIC/analysis/chromium_data_analysis/scoring/PDX352/roma_scores.txt","IC10+",300,300,"C:/Datas/MOSAIC/analysis/chromium_data_analysis/scoring/PDX352/proliferating_cells.txt","0");
			//makeRNKFileFromScoreExtremePoints("C:/Datas/MOSAIC/analysis/roma/PDX352_u10kfc.txt","C:/Datas/MOSAIC/analysis/chromium_data_analysis/scoring/PDX352/IC10+T_pc2diff.txt","PC2_highlow",460,460,"C:/Datas/MOSAIC/analysis/chromium_data_analysis/scoring/PDX352/proliferating_cells.txt","0");
			
			//String days[] = {"7","9","10","11","14","17","22"};
			//ComputeStateTransitionDynamics("C:/Datas/MOSAIC/expression/aggregated_data/ASP14_TS_ul.txt","C:/Datas/MOSAIC/analysis/critical_transition_index/aggregated_1964_log_ica_S5.0.gmt","C:/Datas/MOSAIC/expression/aggregated_data/sample_annotation.txt","SUB_ANNOTATION",days,20);
			//ComputeStateTransitionDynamics("C:/Datas/MOSAIC/expression/aggregated_data_April2017/aggregated_1964_log.txt_filtered.txt","C:/Datas/MOSAIC/analysis/critical_transition_index/aggregated_1964_log_ica_S5.0.gmt","C:/Datas/MOSAIC/expression/aggregated_data/sample_annotation.txt","SUB_ANNOTATION",days,20);
			 
			
			//ComputeAverageModuleValues("C:/Datas/MOSAIC/analysis/roma/ASP14_TS_ul_centered.txt","C:/Datas/MOSAIC/analysis/roma/cellcycleef.gmt");
			//ComputeAverageModuleValues("C:/Datas/MOSAIC/analysis/roma/PDX_OR_INVIVO_ul_centered.txt","C:/Datas/MOSAIC/analysis/roma/cellcycleef.gmt");
			
			//ComputeAverageModuleValues("C:/Datas/MOSAIC/analysis/roma/PDX352TMM_u2kfc.txt","C:/Datas/MOSAIC/analysis/roma/ics_and_signatures.gmt","C:/Datas/MOSAIC/analysis/roma/PDX352TMM_variances.txt");
			//ComputeAverageModuleValues("C:/Datas/Peshkin/demo/day22/scoring/s22fng_T.txt","C:/Datas/Peshkin/demo/day22/scoring/s22fng_ica_A2.0.gmt");
			
			//ComputeAverageModuleValues("C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/agg1964_nufp2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/agg1964_nufp_variances.txt");
			//String prefix = "pdx352";
			//ComputeAverageModuleValues("C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/"+prefix+"_nufp2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/"+prefix+"_nufp_variances.txt");
			//ComputeAverageModuleValues("C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/"+prefix+"_nufp2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/"+prefix+"_nufp_variances.txt");
			
			//ConvertGMT2OccurenceTable("C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt");

			//readJSONSPringGraph("C:/GitPrograms/SPRING-master/datasets/pdx471f_pool/graph_data.json");
			
			//MakeSignatureSelectionFile("C:/Datas/MOSAIC/expression/chromium_data/PDX352TMM_u10kf_midquartgenes.txt","C:/Datas/MOSAIC/analysis/roma/ics_and_signatures.gmt","COL6A1_COL6A2");
			
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/affymetalogu.txt", true, "\t", true);
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/version2_dataset/957TumorNormals_unique.txt", true, "\t", true);
			VSimpleProcedures.findAllNumericalColumns(vt);
			doPCAonSubsets(vt,"C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/version2_dataset/","IC");
			*/
			
			//compileGeneVisibleReport2Table("C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/genevisible_total/analysis101117/ic1+_table.txt");
			//computeAverageCenteredExpression("C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/genevisible_total/analysis101117/nonregulated100_table.txt_sel.txt");
			
			//makeSelectionOfSamplesBasedOnWords("C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/genevisible_total/ic1+_table.txt_sel.txt_average.txt","C:/Datas/MOSAIC/analysis/metaanalysis/MOSAIC_ASP14/PCA_analysis/words");
			
			//makeGMTFromSFile("C:/Datas/Peshkin/demo/day22/ica/s22fng_ica_A.xls",2f);
			//IntersectTwoSetsOfSignatures("C:/Datas/MOSAIC/genesets/LESSNICK.gmt","C:/Datas/MOSAIC/genesets/aggregated_1964_log_ica_S5.0.gmt");
			//IntersectTwoSetsOfSignatures("C:/Datas/MOSAIC/genesets/LESSNICK.gmt","C:/Datas/MOSAIC/genesets/aggregated_1964_log_ica_S5.0.gmt");
			
			
			//IntersectTwoSetsOfSignatures("C:/Datas/acsioma/projects/nov2017_ICA/MSTDplus/TG_HS_classifier.gmt","C:/Datas/acsioma/projects/nov2017_ICA/MSTDplus/TG_HSLiver_S5.0.gmt");
			//IntersectTwoSetsOfSignatures("C:/Datas/acsioma/projects/nov2017_ICA/MSTDplus/DM_aggregated_classifier.gmt","C:/Datas/acsioma/projects/nov2017_ICA/MSTDplus/DM_full_S5.0.gmt");
			//IntersectTwoSetsOfSignatures("C:/Datas/acsioma/projects/nov2017_ICA/MSTDplus/TG_RN_classifier.gmt","C:/Datas/acsioma/projects/nov2017_ICA/MSTDplus/TG_RNaggregated_S3.0.gmt");
			 
			
			System.exit(0);
			
			
			
			//makeGMTFromSFile("C:/Datas/MOSAIC/analysis/IC_specificity_analysis/ewingSCALL/EWINGSC_ul_ica_S.xls",3f);
			//makeGMTFromSFile("C:/Datas/MOSAIC/analysis/tsne/aggregated_data_analysis/manytypes1964/30components/aggregated_1964_log_ica_S.xls",5f);
			System.exit(0);
			
			/*FileWriter fw = new FileWriter("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/PCA_analysis/component_dynamics/ICS.gmt");
			File fles[] = new File("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/PCA_analysis/component_dynamics/").listFiles();
			for(File f:fles)if(f.getName().endsWith("txt")){
				Vector<String> list = Utils.loadVectorOfStrings(f.getAbsolutePath());
				fw.write(f.getName()+"\tna\t");
				String sign = "[1]";
				if(f.getName().endsWith("-.txt")) sign = "[-1]";
				for(int j=0;j<list.size();j++)
					fw.write(list.get(j)+sign+"\t");
				fw.write("\n");
			}
			fw.close();
			System.exit(0);*/
			
			/*VDataTable vt = VDatReadWrite.LoadFromVDatFile("C:/Datas/MOSAIC/analysis/ica/compare_with_transposed/ASP14_norep.txt.dat");
			vt = vt.transposeTable(vt.fieldNames[0]);
			VDatReadWrite.saveToVDatFile(vt, "C:/Datas/MOSAIC/analysis/ica/compare_with_transposed/ASP14_norep_transposed.dat");
			VDataTable vt1 = TableUtils.PCAtable(vt, true, 9);
			VDatReadWrite.saveToSimpleDatFile(vt1, "C:/Datas/MOSAIC/analysis/ica/compare_with_transposed/ASP14_norep_transposed_PCA.txt");
			//pca.
			System.exit(0);*/
			
			/*Random r = new Random();
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/ROMA/data/mosaic/Rescue.txt", true, "\t");
			GMTReader gr = new GMTReader();
			Vector<GESignature> sigs = gr.readGMTDatabase("C:/Datas/ROMA/data/mosaic/ewing_targets.gmt");
			for(int i=0;i<sigs.size();i++){
				GESignature gs = sigs.get(i);
				int size = gs.geneNames.size();
				for(int j=0;j<100;j++){	
				System.out.print("RAND_"+size+"_"+(j+1)+"\tna\t");
				for(int k=0;k<size;k++){	
						String name = vt.stringTable[r.nextInt(vt.rowCount)][0];
						if(!name.equals("@"))
							System.out.print(name+"\t");
					}
					System.out.println();
				}
			}
			System.exit(0);*/
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/Rescue_ica_S.xls", true, "\t");
			VSimpleProcedures.findAllNumericalColumns(vt);
			TableUtils.ConverTableIntoIdLists(vt, "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/ica_lists/",5);*/
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/PCA_analysis/affymetalogu.txt", true, "\t", true);
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/PCA_analysis/Rescue_ica_S.xls", true, "\t", true);
			VSimpleProcedures.findAllNumericalColumns(vt);
			doPCAonSubsets(vt,"C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/PCA_analysis/thresh5/","IC");
			System.exit(0);*/
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/PCA_analysis/Rescue_ica_S.xls", true, "\t", true);
			Random r = new Random();
			for(int i=0;i<100;i++){
				Vector<String> v = new Vector<String>();
				for(int j=0;j<155;j++){
					int k = r.nextInt(vt.rowCount);
					if(!vt.stringTable[k][0].equals("@"))
						v.add(vt.stringTable[k][0]);
				}
				System.out.print("RANDOM"+i+"\tna\t");
				for(String s: v) System.out.print(s+"\t");
				System.out.println();
			}
			System.exit(0);*/
			
			/*System.out.println("Loading 1...");
			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/E-MTAB-2805/G2M_singlecells_counts.txt", true, "\t");
			System.out.println("Loading 2...");
			VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/E-MTAB-2805/S_singlecells_counts.txt", true, "\t");
			System.out.println("Loading 3...");
			VDataTable vt3 = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/E-MTAB-2805/G1_singlecells_counts.txt", true, "\t");
			
			VDataTable merged = VSimpleProcedures.MergeTables(vt1, "EnsemblGeneID", vt2, "EnsemblGeneID", "N/A");
			merged = VSimpleProcedures.MergeTables(merged, "EnsemblGeneID", vt3, "EnsemblGeneID", "N/A");
			VDatReadWrite.saveToSimpleDatFile(merged, "C:/Datas/MOSAIC/expression/metaanalysis/initial_files/E-MTAB-2805/EMTAB.txt");
			System.exit(0);*/
			
			Vector<String> files = new Vector<String>();
			Vector<Vector<String>> geneNames = new Vector<Vector<String>>();
			mos.makeDetectionCurveSampleWise("C:/Datas/MOSAIC/analysis/ica/metaanalysis/PDX_EWING/pdx_lf.txt",null,1f);
			System.exit(0);
			//files.add("Day0.txt"); files.add("Day7.txt"); files.add("d7-R.txt"); files.add("d9-R.txt"); files.add("d10-R.txt"); files.add("d11-R.txt"); files.add("d14-R.txt"); files.add("d17-R.txt"); files.add("d22-R.txt");
			//mos.makeDetectionCurve("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/MOSAIC/",files);
			//files.add("GBM6.txt"); files.add("GBM8.txt"); files.add("MGH30.txt"); files.add("MGH31.txt"); files.add("MGH30L.txt"); files.add("MGH26.txt"); files.add("MGH28.txt"); files.add("MGH29.txt"); 
			//mos.makeDetectionCurve("C:/Datas/MOSAIC/analysis/ica/metaanalysis/GLIOBLASTOMA/",files);
			//mos.makeDetectionCurveSampleWise("C:/Datas/SylviaFre/htseq_l.txt");
			//mos.makeDetectionCurveSampleWise("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/XENOASP14/xeno_copy.txt");
			//mos.makeDetectionCurveSampleWise("C:/Datas/MOSAIC/expression/ASP14_norep.txt");
			//mos.makeDetectionCurveSampleWise("C:/Datas/MOSAIC/expression/ASP14_norep.txt");
			/*files.add("A412.txt"); files.add("A412U1.txt"); files.add("A412U2.txt"); files.add("A413.txt"); files.add("A413U1.txt"); files.add("A413U2.txt");
			files.add("d7_copy.txt"); files.add("d9_copy.txt"); files.add("d10_copy.txt"); files.add("d11_copy.txt"); files.add("d14_copy.txt"); files.add("d17_copy.txt"); files.add("d22_copy.txt");
			
			mos.makeDetectionCurve("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/XENOASP14/",files,geneNames,1f);
			for(int i=0;i<files.size();i++){
				System.out.print(files.get(i)+"\t");
				for(int j=0;j<geneNames.get(i).size();j++)
					System.out.print(geneNames.get(i).get(j)+"\t");
				System.out.println();
			}*/
			
			//mos.makeDetectionCurveSampleWise("C:/Datas/MOSAIC/expression/metaanalysis/initial_files/XENOASP14/xeno_copy_copy.txt",geneNames,1f);
			//mos.makeDetectionCurveSampleWise("C:/Datas/MOSAIC/expression/ASP14_norep.txt",geneNames,1f);
			//mos.makeDetectionCurveSampleWise("C:/Datas/MELANOMA/GSE72056/macrophages.txt",geneNames,1f);
			mos.makeDetectionCurveSampleWise("C:/Datas/MELANOMA/GSE72056/nk.txt",geneNames,1f);
			for(int i=0;i<geneNames.size();i++){
				for(int j=0;j<geneNames.get(i).size();j++)
					System.out.print(geneNames.get(i).get(j)+"\t");
				System.out.println();
			}
			
			System.exit(0);
			
			/*VDataTable table = VDatReadWrite.LoadFromVDatFile("C:/Datas/MOSAIC/viewer/day_fullseries.dat");
			VDatReadWrite.numberOfDigitsToKeep = 1;
			VDatReadWrite.saveToVDatFile(table, "C:/Datas/MOSAIC/viewer/day_fullseries_.dat");
			System.exit(0);*/
			
			/*String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/";
			//mos.MakeCorrelationGraph(folder);
			mos.assembleCorrelationGraph(folder);
			System.exit(0);*/
			
			/*
			 * Make community table, from a folder with XGGML files
			 */
			//HashMap<String, String> commap = mos.makeCommunityTable("C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/metacomponents/","C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/correlation_graph_norecipedges.xgmml");
			//mos.makeMetaComponentTable(commap,"C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/metacomponents/","C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/",false);
			//PanMethylome.makeGSEABatchFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/", "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/msigdb.v4.0.symbols_KEGG.gmt", "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/results/");
			//PanMethylome.makeGSEABatchFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/", "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/Lessnick.gmt", "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/lessnick/");
			//System.exit(0);

			

			mos.prefix = "C:/Datas/MOSAIC/";
			//mos.dataFolder = "expression/ewing_48/";
			//mos.dataFolder = "expression/rhabdoid_48/";
			//mos.dataFolder = "expression/table1114/";
			//mos.dataFolder = "expression/rescue/";
			mos.dataFolder = "analysis/ica/full_rescue/";
			//mos.dataFolder = "analysis/ica/full_rescue/BATCH_FIRST/";
			//mos.dataFile = "A172T_RNASeq_genes.cufflinks.quantile -annotated_4_delog_ewing.txt";
			//mos.dataFolder = "expression/";
			//mos.dataFile = "alldata.txt";
			//mos.dataFile = "rhabdo_96.dat";
			//mos.dataFile = "HTseq-alldata.txt";
			//mos.dataFile = "ts_rescue_c.dat";
			//mos.dataFile = "ewing_96_cf.dat";
			//mos.dataFile = "ts_rescue_complete.dat";
			//mos.dataFile = "Ewing_htseq-_all.ewing_DEnorm_onlySC_2_goodorder.txt";
			mos.dataFile = "day_fullcompleteseries.txt";
			mos.sampleAnnotationFile = 	"fullc_sample_annotation.txt";
			
			/*
			 * Prepare for ICA
			 */
			//mos.prepareForICA("fullc");
			//System.exit(0);
			
			/*
			 * Make metacomponents
			 */
			
			/*HashMap<String, String> commap = PanMethylome.makeCommunityTable("C:/Datas/MOSAIC/analysis/ica/full_rescue/metacomponents/","C:/Datas/MOSAIC/analysis/ica/ica_corrgraph/full_rescue/correlation_graph_norecipedges.xgmml");
			PanMethylome.makeMetaComponentTable(commap,"C:/Datas/MOSAIC/analysis/ica/full_rescue/metacomponents/","C:/Datas/MOSAIC/analysis/ica/full_rescue/",false);
			System.exit(0);*/

			/*
			 * GSEA
			 */
			//PanMethylome.makeGSEABatchFile("C:/Datas/MOSAIC/analysis/ica/full_rescue/metacomponents/gsea/", "C:/Datas/MOSAIC/analysis/ica/full_rescue/metacomponents/gsea/msigdb.v4.0.symbols_KEGG.gmt", "C:/Datas/MOSAIC/analysis/ica/full_rescue/metacomponents/gsea/results/");

			/*
			 * Decompose into subsets
			 */
			/*String annotationColumn = "BATCH";
			VDatReadWrite.writeNumberOfColumnsRows = false;
			VDatReadWrite.useQuotesEverywhere = false;
			VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile(mos.prefix+mos.dataFolder+mos.sampleAnnotationFile, true, "\t");
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(mos.prefix+mos.dataFolder+mos.dataFile, true, "\t");
			Vector<String> series = new Vector<String>();
			for(int i=0;i<vta.rowCount;i++){
				String sn = vta.stringTable[i][vta.fieldNumByName(annotationColumn)];
				if(!sn.equals("_"))if(!series.contains(sn)) series.add(sn);
			}
			for(int i=0;i<series.size();i++){
				String ser = series.get(i);
				System.out.println("series = "+ser);
				Vector<String> columns = new Vector<String>();
				columns.add(vt.fieldNames[0]);
				for(int j=0;j<vta.rowCount;j++){
					if(vta.stringTable[j][vta.fieldNumByName(annotationColumn)].equals(ser)){
						columns.add(vta.stringTable[j][0]);
						System.out.println("Column "+vta.stringTable[j][0]);
					}
				}
				VDataTable vts = VSimpleProcedures.SelectColumns(vt, columns);
				VDatReadWrite.saveToSimpleDatFile(vts, mos.prefix+mos.dataFolder+annotationColumn+"_"+ser+".txt");
				for(int k=1;k<vts.colCount;k++)
					vts.fieldTypes[k] = vts.NUMERICAL;
				VDatReadWrite.saveToVDatFile(vts, mos.prefix+mos.dataFolder+annotationColumn+"_"+ser+".dat");
				VDataTable vtc = TableUtils.normalizeVDat(vts, true, false);
				VDataTable vtan = VDatReadWrite.LoadFromSimpleDatFile(mos.prefix+mos.dataFolder+"fullc_gene_annotation.txt", true, "\t");
				vtc = VSimpleProcedures.MergeTables(vtc, "SYMBOL", vtan, "GENE", "_");
				VDatReadWrite.saveToVDatFile(vtc, mos.prefix+mos.dataFolder+annotationColumn+"_"+ser+"_c.dat");
				//mos.dataFile = annotationColumn+"_"+ser+".txtx`";
				//mos.prepareForICA(annotationColumn+"_"+ser);
			}
			System.exit(0);
			*/

			/*
			 * Compile S and A tables
			 */
			String set[] = {"fullc","BATCH_FIRST","BATCH_RESCUE1123","BATCH_RESCUE711","DAYS_0","DAYS_7","DAYS_7r","DAYS_9","DAYS_10","DAYS_11","DAYS_11c","DAYS_11c3","DAYS_14","DAYS_17","DAYS_23"};
			//String set[] = {"DAYS_0"};
			//mos.CompileAandSTables(mos.prefix+mos.dataFolder,"fullc",mos.prefix+mos.dataFolder+"fullc_gene_annotation.txt",mos.prefix+mos.dataFolder+"fullc_sample_annotation.txt",null);
			/*for(String s: set)
				PanMethylome.CompileAandSTables(mos.prefix+mos.dataFolder+s+"/",s,mos.prefix+mos.dataFolder+"fullc_gene_annotation.txt",mos.prefix+mos.dataFolder+"fullc_sample_annotation.txt",null);
			System.exit(0);*/
			
			/*  
			 * Decompose the files into parts and simultaneously process
			 */
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(mos.prefix+mos.dataFolder+mos.dataFile, true, "\t", true);
			HashMap<String, String> columnnames = getStringMap(mos.prefix+mos.dataFolder+"columnns_renaming.txt");
			
			extractPart(vt,"day0_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day00.dat");
			extractPart(vt,"day7_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day07.dat");
			extractPart(vt,"day7r_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day07r.dat");
			extractPart(vt,"day7rb_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day07rb.dat");
			extractPart(vt,"day9_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day09.dat");
			extractPart(vt,"day9b_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day09b.dat");
			extractPart(vt,"day10_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day10.dat");
			extractPart(vt,"day10b_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day10b.dat");
			extractPart(vt,"day11_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day11.dat");
			extractPart(vt,"day11b_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day11b.dat");
			extractPart(vt,"day11c_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day11c.dat");
			extractPart(vt,"day14_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day14.dat");
			extractPart(vt,"day17_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day17.dat");
			extractPart(vt,"day23_",columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day23.dat");
			
			Vector<String> day07 = new Vector<String>(); day07.add("day0_"); day07.add("day7_");
			extractPart(vt,day07,columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day00_07.dat");
			Vector<String> full = new Vector<String>(); 
			full.add("day0_"); full.add("day7_"); full.add("day7r_"); full.add("day9_"); full.add("day10_"); 
			full.add("day11_"); full.add("day11c_"); full.add("day14_"); full.add("day17_"); full.add("day23_");
			extractPart(vt,full,columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day_fullseries.dat");
			
			Vector<String> fullcomplete = new Vector<String>(); 
			fullcomplete.add("day0_"); fullcomplete.add("day7_"); fullcomplete.add("day7r_"); fullcomplete.add("day7rb_"); fullcomplete.add("day9_"); fullcomplete.add("day9b_"); 
			fullcomplete.add("day10_"); fullcomplete.add("day10b_"); fullcomplete.add("day11_"); fullcomplete.add("day11b_"); fullcomplete.add("day11c_"); 
			fullcomplete.add("day14_"); fullcomplete.add("day17_"); fullcomplete.add("day23_");
			extractPart(vt,fullcomplete,columnnames,"SYMBOL","ID", mos.prefix+mos.dataFolder+"day_fullcompleteseries.dat");*/
			
			
			
			/*
			 * Make correlation graph
			 */
			/*float threshold = 0f;
			float threshold1 = 0.15f;
			String day = "day23";
			Vector<GESignature> modules = GMTReader.readGMTDatabase(mos.prefix+"genesets/signatures76.gmt",5);
			VDataTable vt = VDatReadWrite.LoadFromVDatFile(mos.prefix+mos.dataFolder+day+".dat");
			makeCorrelationGraph(vt, modules, "C:/Datas/MOSAIC/analysis/correlationgraph/"+day,threshold, threshold1);*/
			

			
			
			
			
			//mos.data = VDatReadWrite.LoadFromVDatFile(mos.prefix+mos.dataFolder+mos.dataFile);
			
			//mos.data.fieldTypes[mos.data.fieldNumByName("POP0")] = mos.data.STRING;
			//mos.data.fieldTypes[mos.data.fieldNumByName("POP7")] = mos.data.STRING;
			
			//mos.processTable("ewing_96_cf",false,true);
			
			/*CancerCellLine.AnnotateWithGMTfile(mos.data,mos.prefix+"genesets/e2f1_targets.gmt", "E2F1");
			CancerCellLine.AnnotateWithGMTfile(mos.data,mos.prefix+"genesets/ewsfli1_targets.gmt", "EWSFLI1");
			VDatReadWrite.saveToVDatFile(mos.data, mos.prefix+mos.dataFolder+mos.dataFile.substring(0, mos.dataFile.length()-4)+"a.dat");*/
			
			//mos.processTable("rhb96",true,true);
			//mos.processTable("ts_rescue_complete_c",false,true);
			
			//mos.decomposeCompleteFile();
			//mos.decomposeCompleteFile_part2();
			//mos.decomposeCompleteFile_part3();
			System.exit(0);

			//mos.processRawData();
			
			//mos.data = VDatReadWrite.LoadFromVDatFile(mos.prefix+mos.dataFolder+"data_day7.dat");
			//mos.data = VDatReadWrite.LoadFromVDatFile(mos.prefix+mos.dataFolder+"datafc.dat");
			
			/*mos.data = VDatReadWrite.LoadFromVDatFile(mos.prefix+mos.dataFolder+"data.dat");
			for(int i=1;i<mos.data.colCount;i++)
				for(int j=0;j<mos.data.rowCount;j++){
					float f = Float.parseFloat(mos.data.stringTable[j][i]);
					f = (float)Math.log10(f+1f);
					mos.data.stringTable[j][i] = ""+f;
				}
			VDatReadWrite.numberOfDigitsToKeep = 2;
			VDatReadWrite.saveToVDatFile(mos.data,mos.prefix+mos.dataFolder+"data_short.dat");
			System.exit(0);*/
			
			/*mos.data.fieldTypes[mos.data.fieldNumByName("MEAN")] = mos.data.STRING;
			mos.data.fieldTypes[mos.data.fieldNumByName("STDEV")] = mos.data.STRING;
			mos.data = mos.data.transposeTable("GENE");
			//VDatReadWrite.saveToVDatFile(mos.data, mos.prefix+mos.dataFolder+"datafc_T.dat");
			VDataTable pcat = TableUtils.PCAtable(mos.data, true, 4);
			VDatReadWrite.saveToVDatFile(pcat, mos.prefix+mos.dataFolder+"datafc_T_pca.dat");*/
			
			//mos.processTable("dataf",false);
			//mos.countTranscripts(true, 5f);
			//mos.annotateTable();
			
			//int sel1[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48};
			//int sel2[] = {0,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96};
			//mos.separateIn2Groups(sel1,sel2,"day0","day7");
			//mos.processTable("datafc_day7",false,true);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private static void readJSONSPringGraph(String fn) throws Exception{
		Gson gson = new Gson();
		SpringGraphRoot gr = gson.fromJson(new FileReader(fn), SpringGraphRoot.class);
		for(int i=0;i<gr.links.size();i++){
			System.out.println(gr.links.get(i).source+"\t"+gr.links.get(i).target);
		}
	}

	private static void transformGeneProjectionMatrix(String fn, String meanvalues_fn) throws Exception{
		// TODO Auto-generated method stub
		FileWriter fw = new FileWriter(fn+".sorted.txt");
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		String allgenesfn = (new File(fn)).getParentFile().getAbsolutePath()+"/allgenes_T.dat";
		System.out.println("Loading "+allgenesfn);
		VDataTable allgenes = VDatReadWrite.LoadFromVDatFile(allgenesfn);
		System.out.println("ColCount = "+allgenes.colCount);
		System.out.println("Making dataset...");
		VDataSet allgenesdata = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(allgenes, -1);
		System.out.println("Computing statistics...");
		allgenesdata.calcStatistics();
		System.out.println("done.");
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(int i=1;i<allgenes.colCount;i++){
			map.put(allgenes.fieldNames[i], i-1);
			//System.out.println(allgenes.fieldNames[i]);
		}
		//System.out.println(map.get("CASC5"));
		//System.exit(0);
		VDataTable meanvalues = VDatReadWrite.LoadFromSimpleDatFile(meanvalues_fn, false, "\t");
		meanvalues.makePrimaryHash(meanvalues.fieldNames[0]);
		
		Vector<Metagene> metagenes = new Vector<Metagene>();
		int maxlen = 0;
		for(int i=1;i<vt.colCount;i++){
			Vector<Integer> nonEmptyRows = new Vector<Integer>();
			for(int j=0;j<vt.rowCount;j++){
				String s = vt.stringTable[j][i];
				if(!s.equals("N/A"))
					nonEmptyRows.add(j);
			}
			float vals[] = new float[nonEmptyRows.size()];
			int k=0;
			for(int j=0;j<nonEmptyRows.size();j++)
				vals[k++] = Float.parseFloat(vt.stringTable[nonEmptyRows.get(j)][i]);
			int inds[] = Algorithms.SortMass(vals);
			Metagene mg = new Metagene();
			mg.weights = new Vector<Float>();
			System.out.println(vt.fieldNames[i]+"\tlegnth="+inds.length);
			mg.name = vt.fieldNames[i];
			for(int j=0;j<nonEmptyRows.size();j++){
				String gn = vt.stringTable[nonEmptyRows.get(inds[inds.length-1-j])][0];
				String fv = vt.stringTable[nonEmptyRows.get(inds[inds.length-1-j])][i];
				//System.out.println(gn+"\t"+fv);
				mg.geneNames.add(gn);
				mg.weights.add(Float.parseFloat(fv));
			}
			if(mg.geneNames.size()>maxlen)
				maxlen = mg.geneNames.size();
			metagenes.add(mg);
		}
		for(int i=1;i<vt.colCount;i++){
			fw.write(vt.fieldNames[i]+"\t"+vt.fieldNames[i]+"_W"+"\t"+vt.fieldNames[i]+"_VAR"+"\t"+vt.fieldNames[i]+"_MN"+"\t");
		}
		fw.write("\n");
		for(int j=0;j<maxlen;j++){
		for(int i=0;i<metagenes.size();i++){
			Metagene mg = metagenes.get(i);
			System.out.println(j+"\t"+mg.name+"\tlegnth="+mg.geneNames.size()+","+mg.weights.size());
			if(mg.geneNames.size()>j){
				String gn = mg.geneNames.get(j);
				if(!map.containsKey(gn))
					System.out.println("Not found: "+gn);
				int k1 = map.get(gn);
				if(meanvalues.tableHashPrimary.get(gn)==null)
					System.out.println("Not found mean value for "+gn);
				int k2 = meanvalues.tableHashPrimary.get(gn).get(0);
				fw.write(gn+"\t"+mg.weights.get(j)+"\t"+allgenesdata.simpleStatistics.getStdDev(k1)+"\t"+meanvalues.stringTable[k2][1]+"\t");
				//fw.write(gn+"\t"+mg.weights.get(j)+"\t");
			}else{
				fw.write("\t\t\t\t");
			}
		}
		fw.write("\n");
		}
		fw.close();
	}

	private static void computeAverageCenteredExpression(String fn) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		System.out.println("Loaded.");
		FileWriter fw = new FileWriter(fn+"_average.txt");
		//for(int i=0;i<vt.colCount;i++)
		//	fw.write(vt.fieldNames[i]+"\t");
		fw.write("ID\tSamples\tGROUP\t"+new File(fn).getName()+"\n");
		
		float avs[] = new float[vt.rowCount];
		
		for(int i=3;i<vt.colCount;i++){
			//System.out.println(i);
			float av = 0f;
			for(int j=0;j<vt.rowCount;j++){
				try{
				av+=Float.parseFloat(vt.stringTable[j][i].trim());
				}catch(Exception e){
					System.out.println("error = '"+vt.stringTable[j][i].trim().substring(0, 6)+"' in line "+(j+1));
				}
			}
			av/=(float)(vt.rowCount-1);
			for(int j=0;j<vt.rowCount;j++)
				avs[j] += (Float.parseFloat(vt.stringTable[j][i].trim())-av);
		}
		
		for(int j=0;j<vt.rowCount;j++){
			fw.write(vt.stringTable[j][0]+"\t");
			fw.write(vt.stringTable[j][1]+"\t");
			fw.write(vt.stringTable[j][2]+"\t");
			fw.write(avs[j]/((float)(vt.colCount-3))+"\t\n");
		}
		fw.write("\n");
		fw.close();
	}

	private static void computeAverageCenteredExpression_transposed(String fn) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		System.out.println("Loaded.");
		FileWriter fw = new FileWriter(fn+"_average.txt");
		for(int i=0;i<vt.colCount;i++)
			fw.write(vt.fieldNames[i]+"\t");
		fw.write("\n");
		
		float avs[] = new float[vt.colCount-1];
		
		for(int i=0;i<vt.rowCount;i++){
			System.out.println(i);
			float av = 0f;
			for(int j=1;j<vt.colCount;j++)
				av+=Float.parseFloat(vt.stringTable[i][j]);
			av/=(float)(vt.colCount-1);
			for(int j=1;j<vt.colCount;j++)
				avs[j-1] += (Float.parseFloat(vt.stringTable[i][j])-av);
		}
		
		fw.write(new File(fn).getName()+"\t");
		for(int j=1;j<vt.colCount;j++)
			fw.write(avs[j-1]/((float)(vt.rowCount))+"\t");
		fw.write("\n");
		fw.close();
	}
	
	private static void compileGeneVisibleReport2Table(String fn) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, false, "\t",true);
		FileWriter fw = new FileWriter(fn+"_sel.txt");
		FileWriter fw1 = new FileWriter(fn+"_groups.txt");
		
		fw.write("ID\tGROUP\t");
		for(int i=0;i<vt.colCount;i++){
			String gene = vt.stringTable[0][i];
			//String tp = vt.stringTable[4][i];
			String tp = vt.stringTable[3][i];
			//if(tp.equals("Mean value")){
			if(tp.equals("signal")){
				fw.write(gene+"\t");
			}
		}
		fw.write("\n");
		for(int i=4;i<vt.rowCount;i++){
		//	for(int i=5;i<vt.rowCount;i++){ 	
			System.out.println(i);
			String group = vt.stringTable[i][0];
			String name = vt.stringTable[i][1];
			String sample = vt.stringTable[i][3];
			name = Utils.replaceString(name, "´", "");
			name = Utils.replaceString(name, "(", "");
			name = Utils.replaceString(name, ")", "");
			name = Utils.replaceString(name, "/", "");
			name = Utils.replaceString(name, ";", "_");
			fw.write("\""+name+(i-4)+"\"\t\""+group+"\"\t"+sample+"\t");
			for(int j=0;j<vt.colCount;j++){
				String gene = vt.stringTable[0][j];
				//String tp = vt.stringTable[4][j];				
				String tp = vt.stringTable[3][j];
				//if(tp.equals("Mean value")){
				if(tp.equals("signal")){
					fw.write(vt.stringTable[i][j]+"\t");
				}
			}
			fw.write("\n");
			fw1.write(group+"\n");
		}
		
		fw1.close();
		fw.close();
		
	}

	private static void ComputeAverageModuleValues(String dataFile, String gmtFile, String variances) throws Exception{
		System.out.println("Loading table...");
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(dataFile, true, "\t",true);
		System.out.println("Loaded.");
		vt.makePrimaryHash(vt.fieldNames[0]);
		VSimpleProcedures.findAllNumericalColumns(vt);
		Vector<GESignature> gmt = GMTReader.readGMTDatabase(gmtFile);
		
		System.out.println("Filtering gmt...");
		VDataTable vvar = VDatReadWrite.LoadFromSimpleDatFile(variances, false, "\t");
		vvar.makePrimaryHash(vvar.fieldNames[0]);
		FileWriter fwg = new FileWriter(gmtFile+"_filtered.gmt");
		for(int i=0;i<gmt.size();i++){
			GESignature ge = gmt.get(i);
			fwg.write(ge.name+"\tna\t");
			Vector<String> filtered_names = new Vector<String>();
			for(int j=0;j<ge.geneNames.size();j++)
				if(vt.tableHashPrimary.get(ge.geneNames.get(j))!=null)
					filtered_names.add(ge.geneNames.get(j));
			// Here we match variances to signature genes
			float vals[] = new float[filtered_names.size()];
			for(int j=0;j<filtered_names.size();j++){
				int k = vvar.tableHashPrimary.get(filtered_names.get(j)).get(0);
				vals[j] = Float.parseFloat(vvar.stringTable[k][1]);
			}
			int inds[] = Algorithms.SortMass(vals);
			DecimalFormat df = new DecimalFormat("#.##");
			for(int j=inds.length-1;j>=0;j--){
				fwg.write(filtered_names.get(inds[j])+"["+df.format(vals[inds[j]])+"]\t");
			}
			fwg.write("\n");
		}
		fwg.close();
		System.out.println("Filtered.");
		
		
		System.out.println("Making selection tables...");
		Vector<VDataTable> list = new Vector<VDataTable>();
		for(int i=0;i<gmt.size();i++){
			VDataTable vti = VSimpleProcedures.selectRowsFromList(vt, gmt.get(i).geneNames);
			list.add(vti);
		}
		System.out.println("Made.");
		
		FileWriter fw = new FileWriter(dataFile+".moduleAverages");
		fw.write("SAMPLE\t");
		for(int i=0;i<gmt.size();i++)
			fw.write(gmt.get(i).name+"\t");
		fw.write("\n");
		for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL){
			if(i==(int)((float)i*0.01f)*100)
				System.out.println(i);
			String sample = vt.fieldNames[i];
			fw.write(sample+"\t");
			for(int k=0;k<gmt.size();k++){
				float val = 0f;
				for(int j=0;j<list.get(k).rowCount;j++){
					val+=Float.parseFloat(list.get(k).stringTable[j][list.get(k).fieldNumByName(sample)]);
				}
				if(list.get(k).rowCount>0)
					val/=(float)list.get(k).rowCount;
				System.out.println(gmt.get(k).name+"\t"+list.get(k).rowCount);
				fw.write(val+"\t");
			}
			fw.write("\n");
		}
		fw.close();
	}
	
	public static void readcountPercentageScores(String fn, String gmtfile) throws Exception{
		System.out.println("Read Count Percentage Scores...");
		Vector<GESignature> gmt = GMTReader.readGMTDatabase(gmtfile);
		
		System.out.println("First reading...");
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		int numberOfRows = 0;
		String s = null;
		s = lr.readLine();
		String parts[] = s.split("\t");
		int numberOfColumns = parts.length-1;
		Vector<String> sampleNames = new Vector<String>();
		for(int i=1;i<parts.length;i++)
			sampleNames.add(parts[i]);
		System.out.println("Number of samples : "+numberOfColumns+","+sampleNames.size());
		Vector<String> geneNames = new Vector<String>();
		while((s=lr.readLine())!=null){
			numberOfRows++;
			parts = s.split("\t");
			geneNames.add(parts[0]);
		}
		lr.close();
		int matrix[][] = new int[numberOfColumns][numberOfRows];
		HashMap<String,Integer> geneMap = new HashMap<String,Integer>();
		for(int i=0;i<geneNames.size();i++) geneMap.put(geneNames.get(i), i);
		System.out.println("Second reading...");
		lr = new LineNumberReader(new FileReader(fn));
		s = null;
		lr.readLine();
		int k=0;
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			for(int i=1;i<parts.length;i++)
				matrix[i-1][k] = Integer.parseInt(parts[i]);
			k++;
		}
		lr.close();
		
		int sums[] = new int[numberOfColumns];
		for(int j=0;j<numberOfColumns;j++)
			for(int i=0;i<numberOfRows;i++) 
				sums[j]+=matrix[j][i];
		
		System.out.println("Computing scores...");
		FileWriter fw = new FileWriter(fn+".rcPercentage");
		fw.write("SAMPLE\t");
		for(int i=0;i<gmt.size();i++)
			fw.write(gmt.get(i).name+"\t");
		fw.write("TOTAL_COUNT\n");
		for(int i=0;i<numberOfColumns;i++){
			fw.write(sampleNames.get(i)+"\t");
			for(int j=0;j<gmt.size();j++){
				int sumOfCounts = 0;
				for(k=0;k<gmt.get(j).geneNames.size();k++){
					Integer l = geneMap.get(gmt.get(j).geneNames.get(k));
					if(l!=null)
						sumOfCounts+=matrix[i][l];
				}
				fw.write((float)sumOfCounts/(float)sums[i]+"\t");
			}
			fw.write(sums[i]+"\n");
		}
		fw.close();
	}
	
	public static void ComputeAverageModuleValues_fast(String fn, String gmtfile) throws Exception{
		System.out.println("Compute gene set scores as average values...");
		Vector<GESignature> gmt = GMTReader.readGMTDatabase(gmtfile);
		
		System.out.println("First reading...");
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		int numberOfRows = 0;
		String s = null;
		s = lr.readLine();
		String parts[] = s.split("\t");
		int numberOfColumns = parts.length-1;
		Vector<String> sampleNames = new Vector<String>();
		for(int i=1;i<parts.length;i++)
			sampleNames.add(parts[i]);
		System.out.println("Number of samples : "+numberOfColumns+","+sampleNames.size());
		Vector<String> geneNames = new Vector<String>();
		while((s=lr.readLine())!=null){
			numberOfRows++;
			parts = s.split("\t");
			geneNames.add(parts[0]);
		}
		lr.close();
		float matrix[][] = new float[numberOfColumns][numberOfRows];
		HashMap<String,Integer> geneMap = new HashMap<String,Integer>();
		for(int i=0;i<geneNames.size();i++) geneMap.put(geneNames.get(i), i);
		System.out.println("Second reading...");
		lr = new LineNumberReader(new FileReader(fn));
		s = null;
		lr.readLine();
		int k=0;
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			for(int i=1;i<parts.length;i++)
				matrix[i-1][k] = Float.parseFloat(parts[i]);
			k++;
		}
		lr.close();
		
		System.out.println("Computing scores...");
		FileWriter fw = new FileWriter(fn+".moduleAverages");
		fw.write("SAMPLE\t");
		for(int i=0;i<gmt.size();i++)
			fw.write(gmt.get(i).name+"\t");
		fw.write("\n");
		for(int i=0;i<numberOfColumns;i++){
			fw.write(sampleNames.get(i)+"\t");
			for(int j=0;j<gmt.size();j++){
				float sumOfValues = 0f;
				int n = 0;
				for(k=0;k<gmt.get(j).geneNames.size();k++){
					Integer l = geneMap.get(gmt.get(j).geneNames.get(k));
					if(l!=null){
						sumOfValues+=matrix[i][l];
						n++;
					}
				}
				if(n>0)
					fw.write((float)sumOfValues/(float)n+"\t");
				else
					fw.write("0\t");
			}
			fw.write("\n");
		}
		fw.close();
	}
	

	public void decomposeCompleteFile(){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+dataFile, true, "\t");
		String cr[] = {"SYMBOL","I2A-POP-day0_1","I2A-POP-day0_2","I2A-POP-day0_3","I2A-POP-day7_1","I2A-POP-day7_2","I2A-POP-day7_3","I2A-SC-day0_1","I2A-SC-day0_2","I2A-SC-day0_3","I2A-SC-day0_4","I2A-SC-day0_5","I2A-SC-day0_6","I2A-SC-day0_7","I2A-SC-day0_8","I2A-SC-day0_9","I2A-SC-day0_10","I2A-SC-day0_11","I2A-SC-day0_12","I2A-SC-day0_13","I2A-SC-day0_14","I2A-SC-day0_15","I2A-SC-day0_16","I2A-SC-day0_17","I2A-SC-day0_18","I2A-SC-day0_19","I2A-SC-day0_20","I2A-SC-day0_21","I2A-SC-day0_22","I2A-SC-day0_23","I2A-SC-day0_24","I2A-SC-day0_25","I2A-SC-day0_26","I2A-SC-day0_27","I2A-SC-day0_28","I2A-SC-day0_29","I2A-SC-day0_30","I2A-SC-day0_31","I2A-SC-day0_32","I2A-SC-day0_33","I2A-SC-day0_34","I2A-SC-day0_35","I2A-SC-day0_36","I2A-SC-day0_37","I2A-SC-day0_38","I2A-SC-day0_39","I2A-SC-day0_40","I2A-SC-day0_41","I2A-SC-day0_42","I2A-SC-day0_43","I2A-SC-day0_44","I2A-SC-day0_45","I2A-SC-day0_46","I2A-SC-day0_47","I2A-SC-day0_48","I2A-SC-day7_49","I2A-SC-day7_50","I2A-SC-day7_51","I2A-SC-day7_52","I2A-SC-day7_53","I2A-SC-day7_54","I2A-SC-day7_55","I2A-SC-day7_56","I2A-SC-day7_57","I2A-SC-day7_58","I2A-SC-day7_59","I2A-SC-day7_60","I2A-SC-day7_61","I2A-SC-day7_62","I2A-SC-day7_63","I2A-SC-day7_64","I2A-SC-day7_65","I2A-SC-day7_66","I2A-SC-day7_67","I2A-SC-day7_68","I2A-SC-day7_69","I2A-SC-day7_70","I2A-SC-day7_71","I2A-SC-day7_72","I2A-SC-day7_73","I2A-SC-day7_74","I2A-SC-day7_75","I2A-SC-day7_76","I2A-SC-day7_77","I2A-SC-day7_78","I2A-SC-day7_79","I2A-SC-day7_80","I2A-SC-day7_81","I2A-SC-day7_82","I2A-SC-day7_83","I2A-SC-day7_84","I2A-SC-day7_85","I2A-SC-day7_86","I2A-SC-day7_87","I2A-SC-day7_88","I2A-SC-day7_89","I2A-SC-day7_90","I2A-SC-day7_91","I2A-SC-day7_92","I2A-SC-day7_93","I2A-SC-day7_94","I2A-SC-day7_95","I2A-SC-day7_96"};
		//String ce[] = {"SYMBOL","ASP14-POP-day0_1","ASP14-POP-day0_2","ASP14-POP-day0_3","ASP14-POP-day7_1","ASP14-POP-day7_2","ASP14-POP-day7_3","ASP14-SC-day0_1","ASP14-SC-day0_2","ASP14-SC-day0_3","ASP14-SC-day0_4","ASP14-SC-day0_5","ASP14-SC-day0_6","ASP14-SC-day0_7","ASP14-SC-day0_8","ASP14-SC-day0_9","ASP14-SC-day0_10","ASP14-SC-day0_11","ASP14-SC-day0_12","ASP14-SC-day0_13","ASP14-SC-day0_14","ASP14-SC-day0_15","ASP14-SC-day0_16","ASP14-SC-day0_17","ASP14-SC-day0_18","ASP14-SC-day0_19","ASP14-SC-day0_20","ASP14-SC-day0_21","ASP14-SC-day0_22","ASP14-SC-day0_23","ASP14-SC-day0_24","ASP14-SC-day0_25","ASP14-SC-day0_26","ASP14-SC-day0_27","ASP14-SC-day0_28","ASP14-SC-day0_29","ASP14-SC-day0_30","ASP14-SC-day0_31","ASP14-SC-day0_32","ASP14-SC-day0_33","ASP14-SC-day0_34","ASP14-SC-day0_35","ASP14-SC-day0_36","ASP14-SC-day0_37","ASP14-SC-day0_38","ASP14-SC-day0_39","ASP14-SC-day0_40","ASP14-SC-day0_41","ASP14-SC-day0_42","ASP14-SC-day0_43","ASP14-SC-day0_44","ASP14-SC-day0_45","ASP14-SC-day0_46","ASP14-SC-day0_47","ASP14-SC-day0_48","ASP14-SC-day7_49","ASP14-SC-day7_50","ASP14-SC-day7_51","ASP14-SC-day7_52","ASP14-SC-day7_53","ASP14-SC-day7_54","ASP14-SC-day7_55","ASP14-SC-day7_56","ASP14-SC-day7_57","ASP14-SC-day7_58","ASP14-SC-day7_59","ASP14-SC-day7_60","ASP14-SC-day7_61","ASP14-SC-day7_62","ASP14-SC-day7_63","ASP14-SC-day7_64","ASP14-SC-day7_65","ASP14-SC-day7_66","ASP14-SC-day7_67","ASP14-SC-day7_68","ASP14-SC-day7_69","ASP14-SC-day7_70","ASP14-SC-day7_71","ASP14-SC-day7_72","ASP14-SC-day7_73","ASP14-SC-day7_74","ASP14-SC-day7_75","ASP14-SC-day7_76","ASP14-SC-day7_77","ASP14-SC-day7_78","ASP14-SC-day7_79","ASP14-SC-day7_80","ASP14-SC-day7_81","ASP14-SC-day7_82","ASP14-SC-day7_83","ASP14-SC-day7_84","ASP14-SC-day7_85","ASP14-SC-day7_86","ASP14-SC-day7_87","ASP14-SC-day7_88","ASP14-SC-day7_89","ASP14-SC-day7_90","ASP14-SC-day7_91","ASP14-SC-day7_92","ASP14-SC-day7_93","ASP14-SC-day7_94","ASP14-SC-day7_95","ASP14-SC-day7_96"};
		String ce[] = {"SYMBOL","ASP14-POP_day0-1","ASP14-POP_day0-2","ASP14-POP_day0-3","ASP14-POP_day7-4","ASP14-POP_day7-5","ASP14-POP_day7-6","ASP14-SC-day0_1","ASP14-SC-day0_2","ASP14-SC-day0_3","ASP14-SC-day0_4","ASP14-SC-day0_5","ASP14-SC-day0_6","ASP14-SC-day0_7","ASP14-SC-day0_8","ASP14-SC-day0_9","ASP14-SC-day0_10","ASP14-SC-day0_11","ASP14-SC-day0_12","ASP14-SC-day0_13","ASP14-SC-day0_14","ASP14-SC-day0_15","ASP14-SC-day0_16","ASP14-SC-day0_17","ASP14-SC-day0_18","ASP14-SC-day0_19","ASP14-SC-day0_20","ASP14-SC-day0_21","ASP14-SC-day0_22","ASP14-SC-day0_23","ASP14-SC-day0_24","ASP14-SC-day0_25","ASP14-SC-day0_26","ASP14-SC-day0_27","ASP14-SC-day0_28","ASP14-SC-day0_29","ASP14-SC-day0_30","ASP14-SC-day0_31","ASP14-SC-day0_32","ASP14-SC-day0_33","ASP14-SC-day0_34","ASP14-SC-day0_35","ASP14-SC-day0_36","ASP14-SC-day0_37","ASP14-SC-day0_38","ASP14-SC-day0_39","ASP14-SC-day0_40","ASP14-SC-day0_41","ASP14-SC-day0_42","ASP14-SC-day0_43","ASP14-SC-day0_44","ASP14-SC-day0_45","ASP14-SC-day0_46","ASP14-SC-day0_47","ASP14-SC-day0_48","ASP14-SC-day7_49","ASP14-SC-day7_50","ASP14-SC-day7_51","ASP14-SC-day7_52","ASP14-SC-day7_53","ASP14-SC-day7_54","ASP14-SC-day7_55","ASP14-SC-day7_56","ASP14-SC-day7_57","ASP14-SC-day7_58","ASP14-SC-day7_59","ASP14-SC-day7_60","ASP14-SC-day7_61","ASP14-SC-day7_62","ASP14-SC-day7_63","ASP14-SC-day7_64","ASP14-SC-day7_65","ASP14-SC-day7_66","ASP14-SC-day7_67","ASP14-SC-day7_68","ASP14-SC-day7_69","ASP14-SC-day7_70","ASP14-SC-day7_71","ASP14-SC-day7_72","ASP14-SC-day7_73","ASP14-SC-day7_74","ASP14-SC-day7_75","ASP14-SC-day7_76","ASP14-SC-day7_77","ASP14-SC-day7_78","ASP14-SC-day7_79","ASP14-SC-day7_80","ASP14-SC-day7_81","ASP14-SC-day7_82","ASP14-SC-day7_83","ASP14-SC-day7_84","ASP14-SC-day7_85","ASP14-SC-day7_86","ASP14-SC-day7_87","ASP14-SC-day7_88","ASP14-SC-day7_89","ASP14-SC-day7_90","ASP14-SC-day7_91","ASP14-SC-day7_92","ASP14-SC-day7_93","ASP14-SC-day7_94","ASP14-SC-day7_95","ASP14-SC-day7_96"};
		
		Vector<String> columnsRhabdoid = new Vector<String>();
		Vector<String> columnsEwing = new Vector<String>();
		for(String s: cr) columnsRhabdoid.add(s);
		for(String s: ce) columnsEwing.add(s);
		
		VDataTable vtRhabdoid = VSimpleProcedures.SelectColumns(vt, columnsRhabdoid);
		VDataTable vtEwing = VSimpleProcedures.SelectColumns(vt, columnsEwing);
		
		Vector<Integer> lineNumbers = new Vector<Integer>();
		for(int i=0;i<vtRhabdoid.rowCount;i++)
			if(!vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("SYMBOL")].equals("NA"))
				lineNumbers.add(i);
		
		vtEwing = VSimpleProcedures.selectRows(vtEwing, lineNumbers);
		vtRhabdoid = VSimpleProcedures.selectRows(vtRhabdoid, lineNumbers);
		for(int i=1;i<vtEwing.colCount;i++) vtEwing.fieldTypes[i] = vtEwing.NUMERICAL;
		for(int i=1;i<vtRhabdoid.colCount;i++) vtRhabdoid.fieldTypes[i] = vtRhabdoid.NUMERICAL;
		
		VDatReadWrite.numberOfDigitsToKeep = 2;
		
		VDatReadWrite.saveToVDatFile(vtRhabdoid, prefix+dataFolder+"rhabdo96.dat");
		VDatReadWrite.saveToVDatFile(vtEwing, prefix+dataFolder+"ewing96.dat");
		
		String cr1[] = {"SYMBOL","I2A-SC-day0_1","I2A-SC-day0_2","I2A-SC-day0_3","I2A-SC-day0_4","I2A-SC-day0_5","I2A-SC-day0_6","I2A-SC-day0_7","I2A-SC-day0_8","I2A-SC-day0_9","I2A-SC-day0_10","I2A-SC-day0_11","I2A-SC-day0_12","I2A-SC-day0_13","I2A-SC-day0_14","I2A-SC-day0_15","I2A-SC-day0_16","I2A-SC-day0_17","I2A-SC-day0_18","I2A-SC-day0_19","I2A-SC-day0_20","I2A-SC-day0_21","I2A-SC-day0_22","I2A-SC-day0_23","I2A-SC-day0_24","I2A-SC-day0_25","I2A-SC-day0_26","I2A-SC-day0_27","I2A-SC-day0_28","I2A-SC-day0_29","I2A-SC-day0_30","I2A-SC-day0_31","I2A-SC-day0_32","I2A-SC-day0_33","I2A-SC-day0_34","I2A-SC-day0_35","I2A-SC-day0_36","I2A-SC-day0_37","I2A-SC-day0_38","I2A-SC-day0_39","I2A-SC-day0_40","I2A-SC-day0_41","I2A-SC-day0_42","I2A-SC-day0_43","I2A-SC-day0_44","I2A-SC-day0_45","I2A-SC-day0_46","I2A-SC-day0_47","I2A-SC-day0_48","I2A-SC-day7_49","I2A-SC-day7_50","I2A-SC-day7_51","I2A-SC-day7_52","I2A-SC-day7_53","I2A-SC-day7_54","I2A-SC-day7_55","I2A-SC-day7_56","I2A-SC-day7_57","I2A-SC-day7_58","I2A-SC-day7_59","I2A-SC-day7_60","I2A-SC-day7_61","I2A-SC-day7_62","I2A-SC-day7_63","I2A-SC-day7_64","I2A-SC-day7_65","I2A-SC-day7_66","I2A-SC-day7_67","I2A-SC-day7_68","I2A-SC-day7_69","I2A-SC-day7_70","I2A-SC-day7_71","I2A-SC-day7_72","I2A-SC-day7_73","I2A-SC-day7_74","I2A-SC-day7_75","I2A-SC-day7_76","I2A-SC-day7_77","I2A-SC-day7_78","I2A-SC-day7_79","I2A-SC-day7_80","I2A-SC-day7_81","I2A-SC-day7_82","I2A-SC-day7_83","I2A-SC-day7_84","I2A-SC-day7_85","I2A-SC-day7_86","I2A-SC-day7_87","I2A-SC-day7_88","I2A-SC-day7_89","I2A-SC-day7_90","I2A-SC-day7_91","I2A-SC-day7_92","I2A-SC-day7_93","I2A-SC-day7_94","I2A-SC-day7_95","I2A-SC-day7_96"};
		String ce1[] = {"SYMBOL","ASP14-SC-day0_1","ASP14-SC-day0_2","ASP14-SC-day0_3","ASP14-SC-day0_4","ASP14-SC-day0_5","ASP14-SC-day0_6","ASP14-SC-day0_7","ASP14-SC-day0_8","ASP14-SC-day0_9","ASP14-SC-day0_10","ASP14-SC-day0_11","ASP14-SC-day0_12","ASP14-SC-day0_13","ASP14-SC-day0_14","ASP14-SC-day0_15","ASP14-SC-day0_16","ASP14-SC-day0_17","ASP14-SC-day0_18","ASP14-SC-day0_19","ASP14-SC-day0_20","ASP14-SC-day0_21","ASP14-SC-day0_22","ASP14-SC-day0_23","ASP14-SC-day0_24","ASP14-SC-day0_25","ASP14-SC-day0_26","ASP14-SC-day0_27","ASP14-SC-day0_28","ASP14-SC-day0_29","ASP14-SC-day0_30","ASP14-SC-day0_31","ASP14-SC-day0_32","ASP14-SC-day0_33","ASP14-SC-day0_34","ASP14-SC-day0_35","ASP14-SC-day0_36","ASP14-SC-day0_37","ASP14-SC-day0_38","ASP14-SC-day0_39","ASP14-SC-day0_40","ASP14-SC-day0_41","ASP14-SC-day0_42","ASP14-SC-day0_43","ASP14-SC-day0_44","ASP14-SC-day0_45","ASP14-SC-day0_46","ASP14-SC-day0_47","ASP14-SC-day0_48","ASP14-SC-day7_49","ASP14-SC-day7_50","ASP14-SC-day7_51","ASP14-SC-day7_52","ASP14-SC-day7_53","ASP14-SC-day7_54","ASP14-SC-day7_55","ASP14-SC-day7_56","ASP14-SC-day7_57","ASP14-SC-day7_58","ASP14-SC-day7_59","ASP14-SC-day7_60","ASP14-SC-day7_61","ASP14-SC-day7_62","ASP14-SC-day7_63","ASP14-SC-day7_64","ASP14-SC-day7_65","ASP14-SC-day7_66","ASP14-SC-day7_67","ASP14-SC-day7_68","ASP14-SC-day7_69","ASP14-SC-day7_70","ASP14-SC-day7_71","ASP14-SC-day7_72","ASP14-SC-day7_73","ASP14-SC-day7_74","ASP14-SC-day7_75","ASP14-SC-day7_76","ASP14-SC-day7_77","ASP14-SC-day7_78","ASP14-SC-day7_79","ASP14-SC-day7_80","ASP14-SC-day7_81","ASP14-SC-day7_82","ASP14-SC-day7_83","ASP14-SC-day7_84","ASP14-SC-day7_85","ASP14-SC-day7_86","ASP14-SC-day7_87","ASP14-SC-day7_88","ASP14-SC-day7_89","ASP14-SC-day7_90","ASP14-SC-day7_91","ASP14-SC-day7_92","ASP14-SC-day7_93","ASP14-SC-day7_94","ASP14-SC-day7_95","ASP14-SC-day7_96"};
		columnsRhabdoid.clear();
		columnsEwing.clear();
		for(String s: cr1) columnsRhabdoid.add(s);
		for(String s: ce1) columnsEwing.add(s);
		
		VDataTable vtRhabdoid1 = VSimpleProcedures.SelectColumns(vtRhabdoid, columnsRhabdoid);
		VDataTable vtEwing1 = VSimpleProcedures.SelectColumns(vtEwing, columnsEwing);
		
		vtRhabdoid1.addNewColumn("POP0", "", "", vtRhabdoid1.NUMERICAL, "0");
		vtRhabdoid1.addNewColumn("POP7", "", "", vtRhabdoid1.NUMERICAL, "0");
		vtEwing1.addNewColumn("POP0", "", "", vtRhabdoid1.NUMERICAL, "0");
		vtEwing1.addNewColumn("POP7", "", "", vtRhabdoid1.NUMERICAL, "0");
		
		for(int i=0;i<vtRhabdoid1.rowCount;i++){
			float p01 = Float.parseFloat(vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("I2A-POP-day0_1")]);
			float p02 = Float.parseFloat(vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("I2A-POP-day0_2")]);
			float p03 = Float.parseFloat(vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("I2A-POP-day0_3")]);
			float p71 = Float.parseFloat(vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("I2A-POP-day7_1")]);
			float p72 = Float.parseFloat(vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("I2A-POP-day7_2")]);
			float p73 = Float.parseFloat(vtRhabdoid.stringTable[i][vtRhabdoid.fieldNumByName("I2A-POP-day7_3")]);
			vtRhabdoid1.stringTable[i][vtRhabdoid1.fieldNumByName("POP0")] = ""+1f/3f*(p01+p02+p03);
			vtRhabdoid1.stringTable[i][vtRhabdoid1.fieldNumByName("POP7")] = ""+1f/3f*(p71+p72+p73);
		}
		
		for(int i=0;i<vtEwing1.rowCount;i++){
			/*float p01 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP-day0_1")]);
			float p02 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP-day0_2")]);
			float p03 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP-day0_3")]);
			float p71 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP-day7_1")]);
			float p72 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP-day7_2")]);
			float p73 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP-day7_3")]);*/
			float p01 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP_day0-1")]);
			float p02 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP_day0-2")]);
			float p03 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP_day0-3")]);
			float p71 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP_day7-4")]);
			float p72 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP_day7-5")]);
			float p73 = Float.parseFloat(vtEwing.stringTable[i][vtEwing.fieldNumByName("ASP14-POP_day7-6")]);			
			vtEwing1.stringTable[i][vtEwing1.fieldNumByName("POP0")] = ""+1f/3f*(p01+p02+p03);
			vtEwing1.stringTable[i][vtEwing1.fieldNumByName("POP7")] = ""+1f/3f*(p71+p72+p73);
		}
		
		for(int i=1;i<vtEwing1.colCount;i++)
			for(int j=0;j<vtEwing1.rowCount;j++){
				float f = Float.parseFloat(vtEwing1.stringTable[j][i]);
				f = (float)Math.log10(f+1f);
				vtEwing1.stringTable[j][i] = ""+f;
			}
		for(int i=1;i<vtRhabdoid1.colCount;i++)
			for(int j=0;j<vtRhabdoid1.rowCount;j++){
				float f = Float.parseFloat(vtRhabdoid1.stringTable[j][i]);
				f = (float)Math.log10(f+1f);
				vtRhabdoid1.stringTable[j][i] = ""+f;
			}		
		
		for(int i=1;i<vtEwing1.colCount;i++) vtEwing1.fieldTypes[i] = vtEwing1.NUMERICAL;
		for(int i=1;i<vtRhabdoid1.colCount;i++) vtRhabdoid1.fieldTypes[i] = vtRhabdoid1.NUMERICAL;
		
		VDatReadWrite.saveToVDatFile(vtRhabdoid1, prefix+dataFolder+"rhabdo_96.dat");
		VDatReadWrite.saveToVDatFile(vtEwing1, prefix+dataFolder+"ewing_96.dat");
		
	}
	
	public void decomposeCompleteFile_part2(){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+dataFile, true, "\t");
		String cr[] = {"SYMBOL",
		"ASP14_DAY7_DOX+_1",
		"ASP14_DAY7_DOX+_2",
		"ASP14_DAY7_DOX+_3",
		"ASP14_DAY7_DOX+_4",
		"ASP14_DAY7_DOX+_5",
		"ASP14_DAY7_DOX+_6",
		"ASP14_DAY7_DOX+_7",
		"ASP14_DAY7_DOX+_8",
		"ASP14_DAY7_DOX+_9",
		"ASP14_DAY7_DOX+_10",
		"ASP14_DAY7_DOX+_11",
		"ASP14_DAY7_DOX+_12",
		"ASP14_DAY7_DOX+_13",
		"ASP14_DAY7_DOX+_14",
		"ASP14_DAY7_DOX+_15",
		"ASP14_DAY7_DOX+_16",
		"ASP14_DAY7_DOX+_17",
		"ASP14_DAY7_DOX+_18",
		"ASP14_DAY7_DOX+_19",
		"ASP14_DAY7_DOX+_20",
		"ASP14_DAY7_DOX+_21",
		"ASP14_DAY7_DOX+_22",
		"ASP14_DAY7_DOX+_23",
		"ASP14_DAY7_DOX+_97",
		"ASP14_DAY7_DOX+_98",
		"ASP14_DAY7_DOX+_99",
		"ASP14_DAY7_DOX+_100",
		"ASP14_DAY7_DOX+_101",
		"ASP14_DAY7_DOX+_102",
		"ASP14_DAY7_DOX+_103",
		"ASP14_DAY7_DOX+_104",
		"ASP14_DAY7_DOX+_105",
		"ASP14_DAY7_DOX+_106",
		"ASP14_DAY7_DOX+_107",
		"ASP14_DAY7_DOX+_108",
		"ASP14_DAY7_DOX+_109",
		"ASP14_DAY7_DOX+_110",
		"ASP14_DAY7_DOX+_111",
		"ASP14_DAY7_DOX+_112",
		"ASP14_DAY7_DOX+_113",
		"ASP14_DAY7_DOX+_114",
		"ASP14_DAY7_DOX+_115",
		"ASP14_DAY7_DOX+_116",
		"ASP14_DAY7_DOX+_117",
		"ASP14_DAY7_DOX+_118",
		"ASP14_DAY7_DOX+_119",
		"ASP14_DAY7_DOX+_120",
		
		"ASP14_DAY7+2_DOX-_25",
		"ASP14_DAY7+2_DOX-_26",
		"ASP14_DAY7+2_DOX-_27",
		"ASP14_DAY7+2_DOX-_28",
		"ASP14_DAY7+2_DOX-_29",
		"ASP14_DAY7+2_DOX-_30",
		"ASP14_DAY7+2_DOX-_31",
		"ASP14_DAY7+2_DOX-_32",
		"ASP14_DAY7+2_DOX-_33",
		"ASP14_DAY7+2_DOX-_34",
		"ASP14_DAY7+2_DOX-_35",
		"ASP14_DAY7+2_DOX-_36",
		"ASP14_DAY7+2_DOX-_37",
		"ASP14_DAY7+2_DOX-_38",
		"ASP14_DAY7+2_DOX-_39",
		"ASP14_DAY7+2_DOX-_40",
		"ASP14_DAY7+2_DOX-_41",
		"ASP14_DAY7+2_DOX-_42",
		"ASP14_DAY7+2_DOX-_43",
		"ASP14_DAY7+2_DOX-_44",
		"ASP14_DAY7+2_DOX-_45",
		"ASP14_DAY7+2_DOX-_46",
		"ASP14_DAY7+2_DOX-_47",
		"ASP14_DAY7+2_DOX-_48",
		"ASP14_DAY7+2_DOX-_121",
		"ASP14_DAY7+2_DOX-_122",
		"ASP14_DAY7+2_DOX-_123",
		"ASP14_DAY7+2_DOX-_124",
		"ASP14_DAY7+2_DOX-_125",
		"ASP14_DAY7+2_DOX-_126",
		"ASP14_DAY7+2_DOX-_127",
		"ASP14_DAY7+2_DOX-_128",
		"ASP14_DAY7+2_DOX-_129",
		"ASP14_DAY7+2_DOX-_130",
		"ASP14_DAY7+2_DOX-_131",
		"ASP14_DAY7+2_DOX-_132",
		"ASP14_DAY7+2_DOX-_133",
		"ASP14_DAY7+2_DOX-_134",
		"ASP14_DAY7+2_DOX-_135",
		"ASP14_DAY7+2_DOX-_136",
		"ASP14_DAY7+2_DOX-_137",
		"ASP14_DAY7+2_DOX-_138",
		"ASP14_DAY7+2_DOX-_139",
		"ASP14_DAY7+2_DOX-_140",
		"ASP14_DAY7+2_DOX-_141",
		"ASP14_DAY7+2_DOX-_142",
		"ASP14_DAY7+2_DOX-_143",
		"ASP14_DAY7+2_DOX-_144",
		
		"ASP14_DAY7+3_DOX-_49",
		"ASP14_DAY7+3_DOX-_50",
		"ASP14_DAY7+3_DOX-_51",
		"ASP14_DAY7+3_DOX-_52",
		"ASP14_DAY7+3_DOX-_53",
		"ASP14_DAY7+3_DOX-_54",
		"ASP14_DAY7+3_DOX-_55",
		"ASP14_DAY7+3_DOX-_56",
		"ASP14_DAY7+3_DOX-_57",
		"ASP14_DAY7+3_DOX-_58",
		"ASP14_DAY7+3_DOX-_59",
		"ASP14_DAY7+3_DOX-_60",
		"ASP14_DAY7+3_DOX-_61",
		"ASP14_DAY7+3_DOX-_62",
		"ASP14_DAY7+3_DOX-_63",
		"ASP14_DAY7+3_DOX-_64",
		"ASP14_DAY7+3_DOX-_65",
		"ASP14_DAY7+3_DOX-_66",
		"ASP14_DAY7+3_DOX-_67",
		"ASP14_DAY7+3_DOX-_68",
		"ASP14_DAY7+3_DOX-_69",
		"ASP14_DAY7+3_DOX-_70",
		"ASP14_DAY7+3_DOX-_71",
		"ASP14_DAY7+3_DOX-_72",
		"ASP14_DAY7+3_DOX-_145",
		"ASP14_DAY7+3_DOX-_146",
		"ASP14_DAY7+3_DOX-_147",
		"ASP14_DAY7+3_DOX-_148",
		"ASP14_DAY7+3_DOX-_149",
		"ASP14_DAY7+3_DOX-_150",
		"ASP14_DAY7+3_DOX-_151",
		"ASP14_DAY7+3_DOX-_152",
		"ASP14_DAY7+3_DOX-_153",
		"ASP14_DAY7+3_DOX-_154",
		"ASP14_DAY7+3_DOX-_155",
		"ASP14_DAY7+3_DOX-_156",
		"ASP14_DAY7+3_DOX-_157",
		"ASP14_DAY7+3_DOX-_158",
		"ASP14_DAY7+3_DOX-_159",
		"ASP14_DAY7+3_DOX-_160",
		"ASP14_DAY7+3_DOX-_161",
		"ASP14_DAY7+3_DOX-_162",
		"ASP14_DAY7+3_DOX-_163",
		"ASP14_DAY7+3_DOX-_164",
		"ASP14_DAY7+3_DOX-_165",
		"ASP14_DAY7+3_DOX-_166",
		"ASP14_DAY7+3_DOX-_167",
		"ASP14_DAY7+3_DOX-_168",

		"ASP14_DAY7+4_DOX-_73",
		"ASP14_DAY7+4_DOX-_74",
		"ASP14_DAY7+4_DOX-_75",
		"ASP14_DAY7+4_DOX-_76",
		"ASP14_DAY7+4_DOX-_77",
		"ASP14_DAY7+4_DOX-_78",
		"ASP14_DAY7+4_DOX-_79",
		"ASP14_DAY7+4_DOX-_80",
		"ASP14_DAY7+4_DOX-_81",
		"ASP14_DAY7+4_DOX-_82",
		"ASP14_DAY7+4_DOX-_83",
		"ASP14_DAY7+4_DOX-_84",
		"ASP14_DAY7+4_DOX-_85",
		"ASP14_DAY7+4_DOX-_86",
		"ASP14_DAY7+4_DOX-_87",
		"ASP14_DAY7+4_DOX-_88",
		"ASP14_DAY7+4_DOX-_89",
		"ASP14_DAY7+4_DOX-_90",
		"ASP14_DAY7+4_DOX-_91",
		"ASP14_DAY7+4_DOX-_92",
		"ASP14_DAY7+4_DOX-_93",
		"ASP14_DAY7+4_DOX-_94",
		"ASP14_DAY7+4_DOX-_95",
		"ASP14_DAY7+4_DOX-_96",
		"ASP14_DAY7+4_DOX-_169",
		"ASP14_DAY7+4_DOX-_170",
		"ASP14_DAY7+4_DOX-_171",
		"ASP14_DAY7+4_DOX-_172",
		"ASP14_DAY7+4_DOX-_173",
		"ASP14_DAY7+4_DOX-_174",
		"ASP14_DAY7+4_DOX-_175",
		"ASP14_DAY7+4_DOX-_176",
		"ASP14_DAY7+4_DOX-_177",
		"ASP14_DAY7+4_DOX-_178",
		"ASP14_DAY7+4_DOX-_179",
		"ASP14_DAY7+4_DOX-_180",
		"ASP14_DAY7+4_DOX-_181",
		"ASP14_DAY7+4_DOX-_182",
		"ASP14_DAY7+4_DOX-_183",
		"ASP14_DAY7+4_DOX-_184",
		"ASP14_DAY7+4_DOX-_185",
		"ASP14_DAY7+4_DOX-_186",
		"ASP14_DAY7+4_DOX-_187",
		"ASP14_DAY7+4_DOX-_188",
		"ASP14_DAY7+4_DOX-_189",
		"ASP14_DAY7+4_DOX-_190",
		"ASP14_DAY7+4_DOX-_191",
		"ASP14_DAY7+4_DOX-_192"};
		
		
		String ce[] ={"SYMBOL",
				"MSC-7BJ-1",
				"MSC-7BJ-2",
				"MSC-7BJ-3",
				"MSC-7BJ-4",
				"MSC-7BJ-5",
				"MSC-7BJ-6",
				"MSC-7BJ-7",
				"MSC-7BJ-8",
				"MSC-7BJ-9",
				"MSC-7BJ-10",
				"MSC-7BJ-11",
				"MSC-7BJ-12",
				"MSC-7BJ-13",
				"MSC-7BJ-14",
				"MSC-7BJ-15",
				"MSC-7BJ-16",
				"MSC-7BJ-17",
				"MSC-7BJ-18",
				"MSC-7BJ-19",
				"MSC-7BJ-20",
				"MSC-7BJ-21",
				"MSC-7BJ-22",
				"MSC-7BJ-23",
				"MSC-7BJ-24",
				"MSC-7BJ-25",
				"MSC-7BJ-26",
				"MSC-7BJ-27",
				"MSC-7BJ-28",
				"MSC-7BJ-29",
				"MSC-7BJ-30",
				"MSC-7BJ-31",
				"MSC-7BJ-32",
				"MSC-7BJ-33",
				"MSC-7BJ-34",
				"MSC-7BJ-35",
				"MSC-7BJ-36",
				"MSC-7BJ-37",
				"MSC-7BJ-38",
				"MSC-7BJ-39",
				"MSC-7BJ-40",
				"MSC-7BJ-41",
				"MSC-7BJ-42",
				"MSC-7BJ-43",
				"MSC-7BJ-44",
				"MSC-7BJ-45",
				"MSC-7BJ-46",
				"MSC-7BJ-47",
				"MSC-7BJ-48",
				"MSC-15-FL-1",
				"MSC-15-FL-2",
				"MSC-15-FL-3",
				"MSC-15-FL-4",
				"MSC-15-FL-5",
				"MSC-15-FL-6",
				"MSC-15-FL-7",
				"MSC-15-FL-8",
				"MSC-15-FL-9",
				"MSC-15-FL-10",
				"MSC-15-FL-11",
				"MSC-15-FL-12",
				"MSC-15-FL-13",
				"MSC-15-FL-14",
				"MSC-15-FL-15",
				"MSC-15-FL-16",
				"MSC-15-FL-17",
				"MSC-15-FL-18",
				"MSC-15-FL-19",
				"MSC-15-FL-20",
				"MSC-15-FL-21",
				"MSC-15-FL-22",
				"MSC-15-FL-23",
				"MSC-15-FL-24",
				"MSC-15-FL-25",
				"MSC-15-FL-26",
				"MSC-15-FL-27",
				"MSC-15-FL-28",
				"MSC-15-FL-29",
				"MSC-15-FL-30",
				"MSC-15-FL-31",
				"MSC-15-FL-32",
				"MSC-15-FL-33",
				"MSC-15-FL-34",
				"MSC-15-FL-35",
				"MSC-15-FL-36",
				"MSC-15-FL-37",
				"MSC-15-FL-38",
				"MSC-15-FL-39",
				"MSC-15-FL-40",
				"MSC-15-FL-41",
				"MSC-15-FL-42",
				"MSC-15-FL-43",
				"MSC-15-FL-44",
				"MSC-15-FL-45",
				"MSC-15-FL-46",
				"MSC-15-FL-47",
				"MSC-15-FL-48"
		};
		
		Vector<String> columnsTimeSeriesRescue = new Vector<String>();
		Vector<String> columnsMSC = new Vector<String>();
		for(String s: cr) columnsTimeSeriesRescue.add(s);
		for(String s: ce) columnsMSC.add(s);
		
		VDataTable vtTimeSeriesRescue = VSimpleProcedures.SelectColumns(vt, columnsTimeSeriesRescue);
		VDataTable vtMSC = VSimpleProcedures.SelectColumns(vt, columnsMSC);
		
		Vector<Integer> lineNumbers = new Vector<Integer>();
		for(int i=0;i<vtTimeSeriesRescue.rowCount;i++)
			if(!vtTimeSeriesRescue.stringTable[i][vtTimeSeriesRescue.fieldNumByName("SYMBOL")].equals("NA"))
				lineNumbers.add(i);
		
		vtMSC = VSimpleProcedures.selectRows(vtMSC, lineNumbers);
		vtTimeSeriesRescue = VSimpleProcedures.selectRows(vtTimeSeriesRescue, lineNumbers);
		for(int i=1;i<vtMSC.colCount;i++) vtMSC.fieldTypes[i] = vtMSC.NUMERICAL;
		for(int i=1;i<vtTimeSeriesRescue.colCount;i++) vtTimeSeriesRescue.fieldTypes[i] = vtTimeSeriesRescue.NUMERICAL;
		
		VDatReadWrite.numberOfDigitsToKeep = 2;
		
		VDatReadWrite.saveToVDatFile(vtTimeSeriesRescue, prefix+dataFolder+"ts_rescue.dat");
		VDatReadWrite.saveToVDatFile(vtMSC, prefix+dataFolder+"msc.dat");
		
		String cr1[] = cr;
		String ce1[] = ce;
		columnsTimeSeriesRescue.clear();
		columnsMSC.clear();
		for(String s: cr1) columnsTimeSeriesRescue.add(s);
		for(String s: ce1) columnsMSC.add(s);
		
		VDataTable vtTimeSeriesRescue1 = VSimpleProcedures.SelectColumns(vtTimeSeriesRescue, columnsTimeSeriesRescue);
		VDataTable vtMSC1 = VSimpleProcedures.SelectColumns(vtMSC, columnsMSC);
		
		for(int i=1;i<vtMSC1.colCount;i++)
			for(int j=0;j<vtMSC1.rowCount;j++){
				float f = Float.parseFloat(vtMSC1.stringTable[j][i]);
				f = (float)Math.log10(f+1f);
				vtMSC1.stringTable[j][i] = ""+f;
			}
		for(int i=1;i<vtTimeSeriesRescue1.colCount;i++)
			for(int j=0;j<vtTimeSeriesRescue1.rowCount;j++){
				float f = Float.parseFloat(vtTimeSeriesRescue1.stringTable[j][i]);
				f = (float)Math.log10(f+1f);
				vtTimeSeriesRescue1.stringTable[j][i] = ""+f;
			}		
		
		for(int i=1;i<vtMSC1.colCount;i++) vtMSC1.fieldTypes[i] = vtMSC1.NUMERICAL;
		for(int i=1;i<vtTimeSeriesRescue1.colCount;i++) vtTimeSeriesRescue1.fieldTypes[i] = vtTimeSeriesRescue1.NUMERICAL;
		
		VDatReadWrite.saveToVDatFile(vtTimeSeriesRescue1, prefix+dataFolder+"ts_rescue_log.dat");
		VDatReadWrite.saveToVDatFile(vtMSC1, prefix+dataFolder+"mcs_log.dat");
		
	}
	
	public void decomposeCompleteFile_part3(){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+dataFile, true, "\t");
		String cr[] = {"SYMBOL",
		"ASP14-SC-day0_1","ASP14-SC-day0_2","ASP14-SC-day0_3","ASP14-SC-day0_4","ASP14-SC-day0_5","ASP14-SC-day0_6","ASP14-SC-day0_7","ASP14-SC-day0_8","ASP14-SC-day0_9","ASP14-SC-day0_10","ASP14-SC-day0_11","ASP14-SC-day0_12","ASP14-SC-day0_13","ASP14-SC-day0_14","ASP14-SC-day0_15","ASP14-SC-day0_16","ASP14-SC-day0_17","ASP14-SC-day0_18","ASP14-SC-day0_19","ASP14-SC-day0_20","ASP14-SC-day0_21","ASP14-SC-day0_22","ASP14-SC-day0_23","ASP14-SC-day0_24","ASP14-SC-day0_25","ASP14-SC-day0_26","ASP14-SC-day0_27","ASP14-SC-day0_28","ASP14-SC-day0_29","ASP14-SC-day0_30","ASP14-SC-day0_31","ASP14-SC-day0_32","ASP14-SC-day0_33","ASP14-SC-day0_34","ASP14-SC-day0_35","ASP14-SC-day0_36","ASP14-SC-day0_37","ASP14-SC-day0_38","ASP14-SC-day0_39","ASP14-SC-day0_40","ASP14-SC-day0_41","ASP14-SC-day0_42","ASP14-SC-day0_43","ASP14-SC-day0_44","ASP14-SC-day0_45","ASP14-SC-day0_46","ASP14-SC-day0_47",
		"ASP14-SC-day0_48","ASP14-SC-day7_49","ASP14-SC-day7_50","ASP14-SC-day7_51","ASP14-SC-day7_52","ASP14-SC-day7_53","ASP14-SC-day7_54","ASP14-SC-day7_55","ASP14-SC-day7_56","ASP14-SC-day7_57","ASP14-SC-day7_58","ASP14-SC-day7_59","ASP14-SC-day7_60","ASP14-SC-day7_61","ASP14-SC-day7_62","ASP14-SC-day7_63","ASP14-SC-day7_64","ASP14-SC-day7_65","ASP14-SC-day7_66","ASP14-SC-day7_67","ASP14-SC-day7_68","ASP14-SC-day7_69","ASP14-SC-day7_70","ASP14-SC-day7_71","ASP14-SC-day7_72","ASP14-SC-day7_73","ASP14-SC-day7_74","ASP14-SC-day7_75","ASP14-SC-day7_76","ASP14-SC-day7_77","ASP14-SC-day7_78","ASP14-SC-day7_79","ASP14-SC-day7_80","ASP14-SC-day7_81","ASP14-SC-day7_82","ASP14-SC-day7_83","ASP14-SC-day7_84","ASP14-SC-day7_85","ASP14-SC-day7_86","ASP14-SC-day7_87","ASP14-SC-day7_88","ASP14-SC-day7_89","ASP14-SC-day7_90","ASP14-SC-day7_91","ASP14-SC-day7_92","ASP14-SC-day7_93","ASP14-SC-day7_94","ASP14-SC-day7_95","ASP14-SC-day7_96",				
		"ASP14_DAY7_DOX+_1",
		"ASP14_DAY7_DOX+_2",
		"ASP14_DAY7_DOX+_3",
		"ASP14_DAY7_DOX+_4",
		"ASP14_DAY7_DOX+_5",
		"ASP14_DAY7_DOX+_6",
		"ASP14_DAY7_DOX+_7",
		"ASP14_DAY7_DOX+_8",
		"ASP14_DAY7_DOX+_9",
		"ASP14_DAY7_DOX+_10",
		"ASP14_DAY7_DOX+_11",
		"ASP14_DAY7_DOX+_12",
		"ASP14_DAY7_DOX+_13",
		"ASP14_DAY7_DOX+_14",
		"ASP14_DAY7_DOX+_15",
		"ASP14_DAY7_DOX+_16",
		"ASP14_DAY7_DOX+_17",
		"ASP14_DAY7_DOX+_18",
		"ASP14_DAY7_DOX+_19",
		"ASP14_DAY7_DOX+_20",
		"ASP14_DAY7_DOX+_21",
		"ASP14_DAY7_DOX+_22",
		"ASP14_DAY7_DOX+_23",
		"ASP14_DAY7_DOX+_24",
		
		
		"ASP14_DAY7+2_DOX-_25",
		"ASP14_DAY7+2_DOX-_26",
		"ASP14_DAY7+2_DOX-_27",
		"ASP14_DAY7+2_DOX-_28",
		"ASP14_DAY7+2_DOX-_29",
		"ASP14_DAY7+2_DOX-_30",
		"ASP14_DAY7+2_DOX-_31",
		"ASP14_DAY7+2_DOX-_32",
		"ASP14_DAY7+2_DOX-_33",
		"ASP14_DAY7+2_DOX-_34",
		"ASP14_DAY7+2_DOX-_35",
		"ASP14_DAY7+2_DOX-_36",
		"ASP14_DAY7+2_DOX-_37",
		"ASP14_DAY7+2_DOX-_38",
		"ASP14_DAY7+2_DOX-_39",
		"ASP14_DAY7+2_DOX-_40",
		"ASP14_DAY7+2_DOX-_41",
		"ASP14_DAY7+2_DOX-_42",
		"ASP14_DAY7+2_DOX-_43",
		"ASP14_DAY7+2_DOX-_44",
		"ASP14_DAY7+2_DOX-_45",
		"ASP14_DAY7+2_DOX-_46",
		"ASP14_DAY7+2_DOX-_47",
		"ASP14_DAY7+2_DOX-_48",
		
		
		"ASP14_DAY7+3_DOX-_49",
		"ASP14_DAY7+3_DOX-_50",
		"ASP14_DAY7+3_DOX-_51",
		"ASP14_DAY7+3_DOX-_52",
		"ASP14_DAY7+3_DOX-_53",
		"ASP14_DAY7+3_DOX-_54",
		"ASP14_DAY7+3_DOX-_55",
		"ASP14_DAY7+3_DOX-_56",
		"ASP14_DAY7+3_DOX-_57",
		"ASP14_DAY7+3_DOX-_58",
		"ASP14_DAY7+3_DOX-_59",
		"ASP14_DAY7+3_DOX-_60",
		"ASP14_DAY7+3_DOX-_61",
		"ASP14_DAY7+3_DOX-_62",
		"ASP14_DAY7+3_DOX-_63",
		"ASP14_DAY7+3_DOX-_64",
		"ASP14_DAY7+3_DOX-_65",
		"ASP14_DAY7+3_DOX-_66",
		"ASP14_DAY7+3_DOX-_67",
		"ASP14_DAY7+3_DOX-_68",
		"ASP14_DAY7+3_DOX-_69",
		"ASP14_DAY7+3_DOX-_70",
		"ASP14_DAY7+3_DOX-_71",
		"ASP14_DAY7+3_DOX-_72",
		
		
		"ASP14_DAY7+4_DOX-_73",
		"ASP14_DAY7+4_DOX-_74",
		"ASP14_DAY7+4_DOX-_75",
		"ASP14_DAY7+4_DOX-_76",
		"ASP14_DAY7+4_DOX-_77",
		"ASP14_DAY7+4_DOX-_78",
		"ASP14_DAY7+4_DOX-_79",
		"ASP14_DAY7+4_DOX-_80",
		"ASP14_DAY7+4_DOX-_81",
		"ASP14_DAY7+4_DOX-_82",
		"ASP14_DAY7+4_DOX-_83",
		"ASP14_DAY7+4_DOX-_84",
		"ASP14_DAY7+4_DOX-_85",
		"ASP14_DAY7+4_DOX-_86",
		"ASP14_DAY7+4_DOX-_87",
		"ASP14_DAY7+4_DOX-_88",
		"ASP14_DAY7+4_DOX-_89",
		"ASP14_DAY7+4_DOX-_90",
		"ASP14_DAY7+4_DOX-_91",
		"ASP14_DAY7+4_DOX-_92",
		"ASP14_DAY7+4_DOX-_93",
		"ASP14_DAY7+4_DOX-_94",
		"ASP14_DAY7+4_DOX-_95",
		"ASP14_DAY7+4_DOX-_96"};
		
		
		Vector<String> columnsTimeSeriesRescue = new Vector<String>();
		for(String s: cr) columnsTimeSeriesRescue.add(s);
		
		VDataTable vtTimeSeriesRescue = VSimpleProcedures.SelectColumns(vt, columnsTimeSeriesRescue);
		
		Vector<Integer> lineNumbers = new Vector<Integer>();
		for(int i=0;i<vtTimeSeriesRescue.rowCount;i++){
			boolean na = false;
			String name = vtTimeSeriesRescue.stringTable[i][vtTimeSeriesRescue.fieldNumByName("SYMBOL")];
			if(name.equals("NA")||name.startsWith("RP11-")||name.startsWith("CTC-")||name.startsWith("CTD-")||name.startsWith("LINC")||name.startsWith("AC0")||name.startsWith("AC1")||name.startsWith("CTA-")||name.startsWith("DKFZ"))
				na = true;
			boolean zeroexp = true;
			for(int j=1;j<=191;j++)
				if(!vtTimeSeriesRescue.stringTable[i][j].equals("0"))
					zeroexp = false;
			if((!na)&&(!zeroexp))
				lineNumbers.add(i);
		}
		
		vtTimeSeriesRescue = VSimpleProcedures.selectRows(vtTimeSeriesRescue, lineNumbers);
		for(int i=1;i<vtTimeSeriesRescue.colCount;i++) vtTimeSeriesRescue.fieldTypes[i] = vtTimeSeriesRescue.NUMERICAL;
		
		VDatReadWrite.numberOfDigitsToKeep = 2;
		
		VDatReadWrite.saveToVDatFile(vtTimeSeriesRescue, prefix+dataFolder+"ts_rescue_complete.dat");
		
		String cr1[] = cr;
		columnsTimeSeriesRescue.clear();
		for(String s: cr1) columnsTimeSeriesRescue.add(s);
		
		VDataTable vtTimeSeriesRescue1 = VSimpleProcedures.SelectColumns(vtTimeSeriesRescue, columnsTimeSeriesRescue);
		
		vtTimeSeriesRescue1.addNewColumn("AV0", "", "", vtTimeSeriesRescue1.NUMERICAL, "0");
		vtTimeSeriesRescue1.addNewColumn("AV7", "", "", vtTimeSeriesRescue1.NUMERICAL, "0");
		vtTimeSeriesRescue1.addNewColumn("AVR7", "", "", vtTimeSeriesRescue1.NUMERICAL, "0");
		vtTimeSeriesRescue1.addNewColumn("AVR9", "", "", vtTimeSeriesRescue1.NUMERICAL, "0");
		vtTimeSeriesRescue1.addNewColumn("AVR10", "", "", vtTimeSeriesRescue1.NUMERICAL, "0");
		vtTimeSeriesRescue1.addNewColumn("AVR11", "", "", vtTimeSeriesRescue1.NUMERICAL, "0");

		for(int i=0;i<vtTimeSeriesRescue1.rowCount;i++){
			float av0 = 0;
			float av7 = 0;
			float avr7 = 0;
			float avr9 = 0;
			float avr10 = 0;
			float avr11 = 0;
			
			for(int j=0;j<=47;j++) av0+=Float.parseFloat(vtTimeSeriesRescue1.stringTable[i][j+1]);
			for(int j=48;j<=95;j++) av7+=Float.parseFloat(vtTimeSeriesRescue1.stringTable[i][j+1]);
			for(int j=96;j<=119;j++) avr7+=Float.parseFloat(vtTimeSeriesRescue1.stringTable[i][j+1]);
			for(int j=120;j<=143;j++) avr9+=Float.parseFloat(vtTimeSeriesRescue1.stringTable[i][j+1]);
			for(int j=144;j<=167;j++) avr10+=Float.parseFloat(vtTimeSeriesRescue1.stringTable[i][j+1]);
			for(int j=168;j<=191;j++) avr11+=Float.parseFloat(vtTimeSeriesRescue1.stringTable[i][j+1]);
			
			av0/=48;
			av7/=48;
			avr7/=24;
			avr9/=24;
			avr10/=24;
			avr11/=24;
			
			vtTimeSeriesRescue1.stringTable[i][vtTimeSeriesRescue1.fieldNumByName("AV0")] = ""+av0;
			vtTimeSeriesRescue1.stringTable[i][vtTimeSeriesRescue1.fieldNumByName("AV7")] = ""+av7;
			vtTimeSeriesRescue1.stringTable[i][vtTimeSeriesRescue1.fieldNumByName("AVR7")] = ""+avr7;
			vtTimeSeriesRescue1.stringTable[i][vtTimeSeriesRescue1.fieldNumByName("AVR9")] = ""+avr9;
			vtTimeSeriesRescue1.stringTable[i][vtTimeSeriesRescue1.fieldNumByName("AVR10")] = ""+avr10;
			vtTimeSeriesRescue1.stringTable[i][vtTimeSeriesRescue1.fieldNumByName("AVR11")] = ""+avr11;
		}

		
		for(int i=1;i<vtTimeSeriesRescue1.colCount;i++)
			for(int j=0;j<vtTimeSeriesRescue1.rowCount;j++){
				float f = Float.parseFloat(vtTimeSeriesRescue1.stringTable[j][i]);
				f = (float)Math.log10(f+1f);
				vtTimeSeriesRescue1.stringTable[j][i] = ""+f;
			}		
		
		for(int i=1;i<vtTimeSeriesRescue1.colCount;i++) vtTimeSeriesRescue1.fieldTypes[i] = vtTimeSeriesRescue1.NUMERICAL;
		
		VDatReadWrite.numberOfDigitsToKeep = 2;
		
		VDatReadWrite.saveToVDatFile(vtTimeSeriesRescue1, prefix+dataFolder+"ts_rescue_complete_log.dat");
		
	}
	

	
	public void processRawData() throws IOException{
		System.out.println("Loading...");
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+dataFile, true, "\t");
		for(int i=1;i<vt.colCount;i++){
			vt.fieldTypes[i] = vt.NUMERICAL;
		}
		System.out.println("Transposing...");
		VDataTable vt1 = vt.transposeTable("Gene_Id");
		VDatReadWrite.saveToVDatFile(vt1, prefix+dataFolder+"data.dat");
		
		
	}
	
	public void processTable(String fn, boolean normalize, boolean docenter) throws Exception{

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

		/*FileWriter fw = new FileWriter(prefix+dataFolder+"found.txt");
		for(int i=0;i<data.rowCount;i++){
			int found = 0;
			for(int j=1;j<data.colCount;j++){
				String s = data.stringTable[i][j];
				if(Float.parseFloat(s)!=0)
					found++;
				if(s==null)
					System.out.println("null in column "+j);
			}
			fw.write(found+"\n");
		}
		fw.close();*/

		if(normalize)
		for(int i=0;i<data.rowCount;i++)
			for(int j=1;j<data.colCount;j++){
				String s = data.stringTable[i][j];
				Float f = Float.parseFloat(s);
				//data.stringTable[i][j] = ""+tanh(f/100);
				data.stringTable[i][j] = ""+Math.log10(f+1f);
			}
		
		
		System.out.println("Filter data");
		//data = TableUtils.filterByAverageValue(data, 3.5f);
		//VDatReadWrite.saveToVDatFile(data, prefix+dataFolder+"dataf.dat");
		System.out.println("Center data");
		VDataTable datac = TableUtils.normalizeVDat(data, docenter, false, false, docenter, docenter);
		//VDataTable datac = TableUtils.normalizeVDat(data, false, false, false, false, false);
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.numberOfDigitsToKeep = 2;
		VDatReadWrite.saveToVDatFile(datac, prefix+dataFolder+fn+".dat");
		if(docenter){
		datac.fieldTypes[datac.fieldNumByName("MEAN")] = datac.STRING;
		datac.fieldTypes[datac.fieldNumByName("STDEV")] = datac.STRING;
		}
		VDatReadWrite.saveToSimpleDatFilePureNumerical(datac, prefix+dataFolder+fn+"_numerical.txt",false);
		FileWriter fw1 = new FileWriter(prefix+dataFolder+fn+"_genenames.txt");
		for(int i=0;i<datac.rowCount;i++) fw1.write(datac.stringTable[i][0]+"\n");
		fw1.close();
	}
	
	double tanh(double x)
	{
	return (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
	}	
	
	public void countTranscripts(boolean onlyNonZeros, float thresh) throws Exception{
		
		data.makePrimaryHash("NAME");

		FileWriter fw = new FileWriter(prefix+dataFolder+"transcript_counts.txt");
		fw.write("GENE\tMEAN\tMEAN0\tMEAN7\tMEDIAN\tMEDIAN0\tMEDIAN7\tSTDEV\tSTDEV0\tSTDEV7\t");
		if(onlyNonZeros){
			fw.write("MEAN"+thresh+"\tMEAN"+thresh+"_0\tMEAN"+thresh+"_7\tMEDIAN"+thresh+"\tMEDIAN"+thresh+"_0\tMEDIAN"+thresh+"_7\tSTDEV"+thresh+"\tSTDEV"+thresh+"_0\tSTDEV"+thresh+"_7\t");
			fw.write("Log_MEAN\tLog_MEAN0\tLog_MEAN7\tLog_MEAN"+thresh+"\tLog_MEAN"+thresh+"_0\tLog_MEAN"+thresh+"_7\t");
		}
		float threshs[] = {0f,0.1f,0.5f,1f,5f,10f,50f,100f,500f,1000f,5000f,10000f};
		for(int i=0;i<threshs.length;i++)
			fw.write("C_"+threshs[i]+"\t");
		for(int i=0;i<threshs.length;i++)
			fw.write("C0_"+threshs[i]+"\t");
		for(int i=0;i<threshs.length;i++)
			fw.write("C7_"+threshs[i]+"\t");
		fw.write("\n");
		
		for(int i=0;i<data.rowCount;i++){
			if(i==(int)((float)i*0.001)*1000)
				System.out.print(i+"\t");
			float x[] = new float[data.colCount-1];
			String gene = data.stringTable[i][0];
			for(int j=1;j<data.colCount;j++){
				x[j-1] = Float.parseFloat(data.stringTable[i][j]);
			}
			float x0[] = cutVector(x,0,47);
			float x7[] = cutVector(x,48,95);
			float xp[] = null;
			float xp0[] = null;
			float xp7[] = null;
			if(onlyNonZeros){
				xp = cutVectorNonZeros(x,thresh);
				xp0 = cutVectorNonZeros(x0,thresh);
				xp7 = cutVectorNonZeros(x7,thresh);				
			}
			fw.write(gene+"\t");
			float mnc[] = new float[3];
			mnc[0] = VSimpleFunctions.calcMean(x);
			mnc[1] = VSimpleFunctions.calcMean(x0);
			mnc[2] = VSimpleFunctions.calcMean(x7);
			fw.write(mnc[0]+"\t"+mnc[1]+"\t"+mnc[2]+"\t");
			fw.write(VSimpleFunctions.calcMedian(x)+"\t"+VSimpleFunctions.calcMedian(x0)+"\t"+VSimpleFunctions.calcMedian(x7)+"\t");
			fw.write(VSimpleFunctions.calcStandardDeviation(x)+"\t"+VSimpleFunctions.calcStandardDeviation(x0)+"\t"+VSimpleFunctions.calcStandardDeviation(x7)+"\t");
			float mn[] = new float[3]; float md[] = new float[3]; float st[] = new float[3];
			if(onlyNonZeros){
				mn[0] = VSimpleFunctions.calcMean(xp);
				mn[1] = VSimpleFunctions.calcMean(xp0);
				mn[2] = VSimpleFunctions.calcMean(xp7);
				md[0] = VSimpleFunctions.calcMedian(xp);
				md[1] = VSimpleFunctions.calcMedian(xp0);
				md[2] = VSimpleFunctions.calcMedian(xp7);
				st[0] = VSimpleFunctions.calcStandardDeviation(xp);
				st[1] = VSimpleFunctions.calcStandardDeviation(xp0);
				st[2] = VSimpleFunctions.calcStandardDeviation(xp7);
				for(int k=0;k<3;k++) if(Float.isNaN(mn[k])) mn[k] = 0f;
				for(int k=0;k<3;k++) if(Float.isNaN(md[k])) md[k] = 0f;
				for(int k=0;k<3;k++) if(Float.isNaN(st[k])) st[k] = 0f;
				fw.write(mn[0]+"\t"+mn[1]+"\t"+mn[2]+"\t");
				fw.write(md[0]+"\t"+md[1]+"\t"+md[2]+"\t");
				fw.write(st[0]+"\t"+st[1]+"\t"+st[2]+"\t");
				fw.write(Math.log10(mnc[0]+thresh/10)+"\t"+Math.log10(mnc[1]+thresh/10)+"\t"+Math.log10(mnc[2]+thresh/10)+"\t");
				fw.write(Math.log10(mn[0]+thresh/10)+"\t"+Math.log10(mn[1]+thresh/10)+"\t"+Math.log10(mn[2]+thresh/10)+"\t");
			}
			for(int j=0;j<threshs.length;j++)
				fw.write(getNumberOfNonZeros(x,threshs[j])+"\t");
			for(int j=0;j<threshs.length;j++)
				fw.write(getNumberOfNonZeros(x0,threshs[j])+"\t");
			for(int j=0;j<threshs.length;j++)
				fw.write(getNumberOfNonZeros(x7,threshs[j])+"\t");
			fw.write("\n");
		}
		fw.close();
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+"transcript_counts.txt", true, "\t");
		for(int i=1;i<vt.colCount;i++)
			vt.fieldTypes[i] = vt.NUMERICAL;
		VDatReadWrite.saveToVDatFile(vt, prefix+dataFolder+"transcript_counts.dat");
	}
	
	
	public static int getNumberOfNonZeros(float x[], float thresh){
		int count = 0;
		for(int i=0;i<x.length;i++)
			if(x[i]>thresh)
				count++;
		return count;
	}
	
	public static float[] cutVector(float x[], int start, int end){
		float y[] = new float[end-start+1];
		for(int i=start;i<=end;i++)
				y[i-start]=x[i];
		return y;
	}

	public static float[] cutVectorNonZeros(float x[], float thresh){
		Vector<Float> nz = new Vector<Float>();
		for(int i=0;i<x.length;i++)
				if(x[i]>thresh)
					nz.add(x[i]);
		float y[] = new float[nz.size()];
		for(int i=0;i<nz.size();i++)
			y[i] = nz.get(i);
		return y;
	}
	
	public void annotateTable(){
		// Add Lessnick's targets of EWS/FLI1
		TableUtils.AnnotateWithSubsetOfGenes(data, this.prefix+this.dataFolder+"Lessnick_list.txt", "LESSNICK");
		data.fieldTypes[data.fieldNumByName("LESSNICK")] = data.STRING;
		VDatReadWrite.saveToVDatFile(data, this.prefix+this.dataFolder+"datafca.dat");
	}
	
	public void separateIn2Groups(int selection1[], int selection2[], String prefix1, String prefix2){
		Vector<String> sel1 = new Vector<String>();
		Vector<String> sel2 = new Vector<String>();
		for(int i=0;i<selection1.length;i++) sel1.add(data.fieldNames[selection1[i]]);
		for(int i=0;i<selection2.length;i++) sel2.add(data.fieldNames[selection2[i]]);
		VDataTable vt1 = VSimpleProcedures.SelectColumns(data, sel1);
		VDataTable vt2 = VSimpleProcedures.SelectColumns(data, sel2);
		VDatReadWrite.saveToVDatFile(vt1, this.prefix+this.dataFolder+"data_"+prefix1+".dat");
		VDatReadWrite.saveToVDatFile(vt2, this.prefix+this.dataFolder+"data_"+prefix2+".dat");		
	}
	
	public static HashMap<String, String> getStringMap(String fn){
		HashMap<String, String> map = new HashMap<String, String>();
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		for(String s: lines){
			StringTokenizer st = new StringTokenizer(s,"\t");
			map.put(st.nextToken(), st.nextToken());
		}
		return map;
	}
	
	public static void extractPart(VDataTable vt, String columnPrefix, HashMap<String, String> columnRename, String nameColumn, String idcolumn, String outputFileName) throws Exception{
		Vector<String> prefixes = new Vector<String>();
		prefixes.add(columnPrefix);
		extractPart(vt, prefixes, columnRename, nameColumn, idcolumn, outputFileName);
	}
	
	public static void extractPart(VDataTable vt, Vector<String> columnPrefixes, HashMap<String, String> columnRename, String nameColumn, String idcolumn, String outputFileName) throws Exception{
		Vector<String> columnsToSelect = new Vector<String>();
		
		for(int i=0;i<vt.fieldNames.length;i++){
			String fn = vt.fieldNames[i];
			if(columnRename.get(fn)!=null){
				vt.fieldNames[i] = columnRename.get(fn);
			}
		}
		
		columnsToSelect.add(nameColumn);
		for(int i=0;i<vt.fieldNames.length;i++){
			for(String prefixColumn: columnPrefixes)
				if(vt.fieldNames[i].startsWith(prefixColumn))
					columnsToSelect.add(vt.fieldNames[i]);
		}
		VDataTable vt1 = VSimpleProcedures.SelectColumns(vt, columnsToSelect);
		
		Vector<Integer> lineNumbers = new Vector<Integer>();
		for(int i=0;i<vt1.rowCount;i++){
			boolean na = false;
			String name = vt1.stringTable[i][vt1.fieldNumByName(nameColumn)];
			if(name.equals("NA")||name.startsWith("RP11-")||name.startsWith("CTC-")||name.startsWith("CTD-")||name.startsWith("LINC")||name.startsWith("AC0")||name.startsWith("AC1")||name.startsWith("CTA-")||name.startsWith("DKFZ"))
				na = true;
			boolean zeroexp = true;
			for(int j=1;j<vt1.colCount;j++)
				if(!vt1.stringTable[i][j].equals("0"))
					zeroexp = false;
			if((!na)&&(!zeroexp))
				lineNumbers.add(i);
		}
		
		vt1 = VSimpleProcedures.selectRows(vt1, lineNumbers);
		
		vt1.fieldTypes[0] = vt1.STRING;
		for(int i=1;i<vt1.colCount;i++)
			vt1.fieldTypes[i] = vt1.NUMERICAL;
		/*for(int i=0;i<vt1.rowCount;i++)
			if(vt1.stringTable[i][0].equals("NA"))
				vt1.stringTable[i][0] = vt.stringTable[i][vt.fieldNumByName(idcolumn)];*/
		for(int i=1;i<vt1.colCount;i++)
			for(int j=0;j<vt1.rowCount;j++){
				float f = Float.parseFloat(vt1.stringTable[j][i]);
				f = (float)Math.log10(f+1f);
				vt1.stringTable[j][i] = ""+f;
			}		
		
		VDatReadWrite.numberOfDigitsToKeep = numDigits;
		VDatReadWrite.saveToVDatFile(vt1, outputFileName);
		VDatReadWrite.saveToSimpleDatFile(vt1, outputFileName.substring(0, outputFileName.length()-4)+".txt", true);
		
		// compute averages and dispersions
		FileWriter fw = new FileWriter(outputFileName.substring(0, outputFileName.length()-4)+"_stat.txt");
		fw.write("GENE\t");
		for(int j=0;j<columnPrefixes.size();j++){
			fw.write(columnPrefixes.get(j)+"_av\t"+columnPrefixes.get(j)+"_std\t"+columnPrefixes.get(j)+"_av0\t"+columnPrefixes.get(j)+"_std0\t");
		}
		fw.write("\n");
		for(int i=0;i<vt1.rowCount;i++){
			fw.write(vt1.stringTable[i][0]+"\t");
			for(int j=0;j<columnPrefixes.size();j++){
				float av = 0f; float std = 0f; float av0 = 0f; float std0 = 0f;
				Vector<Float> vals = new Vector<Float>();
				for(int k=1;k<vt1.colCount;k++)
					if(vt1.fieldNames[k].startsWith(columnPrefixes.get(j)))
						vals.add(Float.parseFloat(vt1.stringTable[i][k]));
				float valsf[] = new float[vals.size()]; for(int k=0;k<valsf.length;k++) valsf[k] = vals.get(k);
				av = VSimpleFunctions.calcMean(valsf);
				std = VSimpleFunctions.calcStandardDeviation(valsf);
				av0 = (float)VSimpleFunctions.calcMeanBiggerThan(valsf,1e-6f);
				std0 = VSimpleFunctions.calcStandardDeviationBiggerThan(valsf,1e-6f);
				fw.write(av+"\t"+std+"\t"+av0+"\t"+std0+"\t");
			}
			fw.write("\n");
		}
		fw.close();
		VDataTable vts = VDatReadWrite.LoadFromSimpleDatFile(outputFileName.substring(0, outputFileName.length()-4)+"_stat.txt", true, "\t");
		for(int i=1;i<vts.colCount;i++) vts.fieldTypes[i] = vts.NUMERICAL;
		VDatReadWrite.saveToVDatFile(vts, outputFileName.substring(0, outputFileName.length()-4)+"_stat.dat");
	}
	
	public static void makeCorrelationGraph(VDataTable vt, Vector<GESignature> modules, String outname, float threshold, float threshold1) throws Exception{
		vt.makePrimaryHash(vt.fieldNames[0]);
		Vector<VDataSet> ds = new Vector<VDataSet>();
		for(int i=0;i<modules.size();i++){
			Vector<Integer> lineNumbers = new Vector<Integer>();
			GESignature ge = modules.get(i);
			for(String s: ge.geneNames)if(vt.tableHashPrimary.get(s)!=null){
				int k = vt.tableHashPrimary.get(s).get(0);
				lineNumbers.add(k);
			}
			VDataTable vs = VSimpleProcedures.selectRows(vt, lineNumbers);
			String folder = (new File(outname)).getParentFile().getAbsolutePath();
			String name = (new File(outname)).getName();
			VDatReadWrite.saveToSimpleDatFile(vs, folder+"/modules/"+name+"_"+ge.name+".dat");
			VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vs, -1);
			ds.add(vd);
		}

		float corrmat[][] = new float[ds.size()][ds.size()];
		FileWriter fwn = new FileWriter(outname+"_node"+threshold+".txt");
		FileWriter fwn1 = new FileWriter(outname+"_node_names"+threshold+".txt");
		fwn.write("MODULE\tINTRA_CORR\n");
		for(int i=0;i<ds.size();i++){
			float corr = VSimpleFunctions.calcMeanPairwiseCorrelation(ds.get(i), true, threshold);
			//if(corr>threshold1)
				fwn.write(modules.get(i).name+"\t"+corr+"\n");
			fwn1.write(modules.get(i).name+"\n");
			corrmat[i][i] = corr;
		}
		fwn.close();
		fwn1.close();
		
		FileWriter fwe = new FileWriter(outname+"_edge"+threshold+".txt");
		
		fwe.write("MODULE1\tMODULE2\tINTER_CORR\n");
		for(int i=0;i<ds.size();i++)for(int j=i+1;j<ds.size();j++){{
			float corr = VSimpleFunctions.calcMeanPairwiseCorrelation(ds.get(i),ds.get(j), true, threshold, true);
			if(corr>threshold1)
				fwe.write(modules.get(i).name+"\t"+modules.get(j).name+"\t"+corr+"\n");
			corrmat[i][j] = corr;
			corrmat[j][i] = corr;
		}
		}
		fwe.close();
		
		FileWriter fwem = new FileWriter(outname+"_corrmatrix"+threshold+".txt");
		for(int i=0;i<ds.size();i++){
			for(int j=0;j<ds.size();j++){
				fwem.write(corrmat[i][j]+"\t");
			}
			fwem.write("\n");
		}
		fwem.close();
	}
	
	
	public void prepareForICA(String fn) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+dataFolder+dataFile, true, "\t");
		for(int i=1;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
		VDataTable datac = TableUtils.normalizeVDat(vt, true, false);
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.numberOfDigitsToKeep = 3;
		VDatReadWrite.saveToSimpleDatFilePureNumerical(datac, prefix+dataFolder+fn+"_numerical.txt",false);
		FileWriter fw1 = new FileWriter(prefix+dataFolder+fn+"_ids.txt");
		for(int i=0;i<vt.rowCount;i++)
			fw1.write(vt.stringTable[i][0]+"\n");
		fw1.close();
		FileWriter fws = new FileWriter(prefix+dataFolder+fn+"_samples.txt");
		for(int i=1;i<vt.colCount;i++)
			fws.write(vt.fieldNames[i]+"\t");
		fws.write("\n");;
		fws.close();
	}
	
	  public static void assembleCorrelationGraph(String folder) throws Exception{
			Graph gr = new Graph();
			File fles[] = (new File(folder)).listFiles();
			for(File f:fles){
				if(f.getName().endsWith(".xgmml"))if(!f.getName().startsWith("correlation")){
					System.out.println("Loaded "+f.getName());
					Graph gri = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f.getAbsolutePath()));
					gr.addNodes(gri);
					gr.addEdges(gri);
				}
			}
			XGMML.saveToXGMML(gr, folder+"correlation_graph.xgmml");
			gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(folder+"correlation_graph.xgmml"));
			System.out.println(gr.Nodes.size()+" nodes, "+gr.Edges.size()+" edges.");
			Graph gr1 = RemoveReciprocalEdgesInCorrelationGraph(gr);
			gr1.name = "correlation_graph_"+(new Random()).nextInt(10000);
			System.out.println(gr1.Nodes.size()+" nodes, "+gr1.Edges.size()+" edges.");
			XGMML.saveToXGMML(gr1, folder+"correlation_graph_norecipedges.xgmml");
	  }
	
		public static void MakeCorrelationGraph(String folder) throws Exception{
			Vector<VDataTable> tables = new Vector<VDataTable>();
			Vector<String> fileNames = new Vector<String>();
			File fs[] = new File(folder).listFiles();
			for(int i=0;i<fs.length;i++)if(fs[i].getName().endsWith("_S.xls")){
				System.out.println(fs[i].getName()+"\t"+Utils.getUsedMemoryMb());
				//VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(fs[i].getAbsolutePath(), true, "\t");
				//for(int k=1;k<vt1.colCount;k++) vt1.fieldTypes[k] = vt1.NUMERICAL;
				VDataTable vt1 = null;
				tables.add(vt1);
				fileNames.add(fs[i].getName());
			}
			
			Graph graph = new Graph();
			for(int i=0;i<tables.size();i++){
				String fn1 = new String(fileNames.get(i));
				VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(folder+fn1, true, "\t");
				fn1 = fn1.substring(0, fn1.length()-6);
				System.out.println("Table i :"+fn1);
				for(int k=1;k<vt1.colCount;k++) vt1.fieldTypes[k] = vt1.NUMERICAL;
				for(int j=0;j<tables.size();j++){
				if(i!=j){
					
					String fn2 = new String(fileNames.get(j));
					String fn2_short = fn2.substring(0, fn2.length()-6);
					
					File computedGraph = new File(folder+fn1+"_"+fn2_short+".xgmml");
					if(!computedGraph.exists()){
						
					FileWriter fw = new FileWriter(computedGraph.getAbsolutePath());
					fw.write("<empty>");
					fw.close();
					
					VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile(folder+fn2, true, "\t");
					System.out.println("Table j :"+fn2_short);
					for(int k=1;k<vt2.colCount;k++) vt2.fieldTypes[k] = vt2.NUMERICAL;
					
					fn2 = fn2_short;
					System.out.println("=============================");
					System.out.println(fn1+"\tvs\t"+fn2);
					//VDataTable vt1 = tables.get(i);
					//VDataTable vt2 = tables.get(j);
					Graph gr = fr.curie.BiNoM.pathways.utils.Utils.makeTableCorrelationGraph(vt1, fn1, vt2, fn2, 0.1f, true);
					//graph.addNodes(gr);
					//graph.addEdges(gr);
					gr.name = "corr_graph"+fn1+"_"+fn2+"_"+(new Random()).nextInt(10000);
					XGMML.saveToXGMML(gr, computedGraph.getAbsolutePath());
					}
				}}
			}
		}
		
		  public static Graph RemoveReciprocalEdgesInCorrelationGraph(Graph graph){
			  Graph gr = graph;
			  float connectivity[] = new float[graph.Nodes.size()];
			  gr.calcNodesInOut();
			  for(int i=0;i<graph.Nodes.size();i++){
				  connectivity[i] = 0f+graph.Nodes.get(i).incomingEdges.size()+graph.Nodes.get(i).outcomingEdges.size();
			  }
			  int ind[] = Algorithms.SortMass(connectivity);
			  for(int i=0;i<ind.length;i++){
				  Node n = graph.Nodes.get(ind[i]);
				  gr.calcNodesInOut();
				  for(int j=0;j<n.incomingEdges.size();j++)for(int k=0;k<n.outcomingEdges.size();k++){
					  Edge ej = n.incomingEdges.get(j);
					  Edge ek = n.outcomingEdges.get(k);
					  float corrj = Float.parseFloat(ej.getFirstAttributeValue("ABSCORR"));
					  float corrk = Float.parseFloat(ek.getFirstAttributeValue("ABSCORR"));
					  if(ej.Node1==ek.Node2)if(ej.Node2==ek.Node1){
						     if(corrk>corrj){
						    	 ej.setAttributeValueUnique("ABSCORR", ""+corrk, Attribute.ATTRIBUTE_TYPE_REAL);
						    	 ej.setAttributeValueUnique("CORR", ""+Float.parseFloat(ek.getFirstAttributeValue("CORR")), Attribute.ATTRIBUTE_TYPE_REAL);
						     }
							 gr.removeEdge(ek.Id);
							 ej.setAttributeValueUnique("RECIPROCAL", "TRUE", Attribute.ATTRIBUTE_TYPE_STRING);
					  }
				  }
				  gr.calcNodesInOut();
				  for(int j=0;j<n.incomingEdges.size();j++)for(int k=j+1;k<n.incomingEdges.size();k++){
					  Edge ej = n.incomingEdges.get(j);
					  Edge ek = n.incomingEdges.get(k);
					  float corrj = Float.parseFloat(ej.getFirstAttributeValue("ABSCORR"));
					  float corrk = Float.parseFloat(ek.getFirstAttributeValue("ABSCORR"));
					  if(ej.Node1==ek.Node1)if(ej.Node2==ek.Node2){
						     if(corrk>corrj){
						    	 ej.setAttributeValueUnique("ABSCORR", ""+corrk, Attribute.ATTRIBUTE_TYPE_REAL);
						    	 ej.setAttributeValueUnique("CORR", ""+Float.parseFloat(ek.getFirstAttributeValue("CORR")), Attribute.ATTRIBUTE_TYPE_REAL);
						     }
							 gr.removeEdge(ek.Id);
							 ej.setAttributeValueUnique("RECIPROCAL", "TRUE", Attribute.ATTRIBUTE_TYPE_STRING);
					  }
				  }
			  }
			  return gr;
		  }
		  
		  public static HashMap<String, String> makeCommunityTable(String folder, String completeGraphPath) throws Exception{
			  	Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(completeGraphPath));
				File fs[] = new File(folder).listFiles();
				HashMap<String, String> commap = new HashMap<String, String>();
				for(File f: fs){
					String fn = f.getName();
					if(fn.endsWith(".xgmml")){
						String commname = fn.substring(0, fn.length()-6);
						Graph grcomm = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f.getAbsolutePath()));
						for(Node n: grcomm.Nodes){
							commap.put(n.Id, commname);
						}
					}
				}
				System.out.println("NODE\tCOMMUNITY");
				for(Node n: gr.Nodes){
					System.out.println(n.Id+"\t"+commap.get(n.Id));
				}
				return commap;
		  }
		  
		  
		  public static void makeMetaComponentTable(HashMap<String, String> commap, String folder, String datafolder, boolean normalize) throws Exception{
			  
			  
			   //Make list of communities
			   Vector<String> communities = new Vector<String>();
			   Iterator<String> it = commap.values().iterator();
			   while(it.hasNext()) { String s = it.next(); if(!communities.contains(s)) communities.add(s); }
			   
			   for(String community: communities){
				   System.out.println("Community "+community);
				   // List all components in the community and collect all metagenes
				   Vector<String> components = new Vector<String>();
				   Set<String> comps = commap.keySet();
				   Vector<MetaGene> metagenes = new Vector<MetaGene>();
				   for(String s: comps) if(commap.get(s).equals(community)){ 
					   components.add(s);
					   StringTokenizer st = new StringTokenizer(s,"_");
					   String column = st.nextToken();
					   String dataset = s.substring(s.indexOf(column)+column.length()+1, s.length());
					   System.out.println("Column="+column+",Dataset="+dataset);
					   String fn = datafolder+dataset+"_S.xls";
					   VDataTable vtm = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
					   MetaGene mg = new MetaGene();
					   mg.name = s;
					   for(int j=0;j<vtm.rowCount;j++)
						   mg.add(vtm.stringTable[j][0], Float.parseFloat(vtm.stringTable[j][vtm.fieldNumByName(column)]));
					   metagenes.add(mg);
				   }
				   // Determine the "core" component
				   int coreComp = -1;
				   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("Rescue")) coreComp = i;
				   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("GBM_")) coreComp = i;
				   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("Myoblast_")) coreComp = i;
				   if(coreComp<0) coreComp = 0;
				   MetaGene coreMetaGene = metagenes.get(coreComp);
				   System.out.println("CoreComponent "+coreMetaGene.name);
				   
				   // select all other metagenes
				   Vector<MetaGene> mgs = new Vector<MetaGene>();
				   for(MetaGene mg: metagenes) if(!mg.name.equals(metagenes.get(coreComp).name)) mgs.add(mg);
				   int minOccurence = (int)(mgs.size()*0.5f);
				   //if(minOccurence>2)
				   //	minOccurence = 2;
				   System.out.println("minOccurence = "+minOccurence);
				   //System.out.println("Standardizing....");
				   Vector<MetaGene> mgs1 = coreMetaGene.standartatizeMetaGenes(mgs);
				   mgs1.add(coreMetaGene);

				   //System.out.println("Normalizing....");
				   if(normalize)
					   for(MetaGene m: mgs1)
				   			m.normalizeWeightsToSidedZScores();
				   //System.out.println("Averaging....");
				   MetaGene metameta = coreMetaGene.makeMetaGeneScoredFromMetagenes(mgs1, minOccurence, true);
				   //System.out.println("Sorting....");
				   metameta.sortWeights();
				   //System.out.println("Saving....");
				   metameta.saveToFile(folder+community+".rnk");
				   
			   }
				
		  }

		  public static void makeDetectionCurve(String folder, Vector<String> files, Vector<Vector<String>> geneNames, float threshValue){
			  float det[][] = new float[100][files.size()];
			  for(int i=0;i<files.size();i++){
				  geneNames.add(new Vector<String>());
				  String fn = folder+files.get(i);
				  System.out.println("Reading "+files.get(i));
				  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
				  VSimpleProcedures.findAllNumericalColumns(vt);
				  int numnumerical = 0;
				  for(int col=0;col<vt.colCount;col++)if(vt.fieldTypes[col]==vt.NUMERICAL) numnumerical++;
				  for(int row=0;row<vt.rowCount;row++){
					  float average = 0f;
					  float maxval = 0f;
					  
					  for(int col=0;col<vt.colCount;col++)if(vt.fieldTypes[col]==vt.NUMERICAL){
						  float f = Float.parseFloat(vt.stringTable[row][col]);
						  average+=f/(float)numnumerical;
						  maxval = Math.max(maxval, f);
						  for(int k=0;k<100;k++){
							  float thresh = (float)k*0.01f*4f;
							  //if(f>thresh){
							  //	  det[k][i] +=1f/(float)numnumerical;
							  //}
						  }
					  }
					  if(average>threshValue) 
					  //if(maxval>threshValue)
						  geneNames.get(geneNames.size()-1).add(vt.stringTable[row][0]);
					  for(int k=0;k<100;k++){
						  float thresh = (float)k*0.01f*4f;
						  if(average>thresh){
							  det[k][i] +=1f;
						  }
					  }
					  
				  }
				  Collections.sort(geneNames.get(geneNames.size()-1));
			  }
			  System.out.print("THRESHOLD\t");
			  for(int i=0;i<files.size();i++){
				  System.out.print(files.get(i)+"\t");
			  }
			  System.out.println();
			  for(int k=0;k<100;k++){
				  float thresh = (float)k*0.01f*4f;
				  System.out.print(thresh+"\t");
				  for(int i=0;i<files.size();i++){
					  System.out.print(det[k][i]+"\t");
				  }
				  System.out.println();
			  }
		  }
		  
		  public static void makeDetectionCurveSampleWise(String fn, Vector<Vector<String>> geneNames, float threshValue){
			  Vector<String> samples = new Vector<String>();
			  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
			  VSimpleProcedures.findAllNumericalColumns(vt);
			  for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL) samples.add(vt.fieldNames[i]);
			  float det[][] = new float[100][samples.size()];
			  for(int i=0;i<samples.size();i++){
				  
				  geneNames.add(new Vector<String>());
				  
				  for(int row=0;row<vt.rowCount;row++){
				  float f = Float.parseFloat(vt.stringTable[row][vt.fieldNumByName(samples.get(i))]);
				  for(int k=0;k<100;k++){
					  float thresh = (float)k*0.01f*4f;
					  if(f>thresh){
						  det[k][i] +=1f;
					  }
				  }
				  if(f>threshValue)
				  //if(!geneNames.get(geneNames.size()-1).contains(vt.stringTable[row][0]))
					  geneNames.get(geneNames.size()-1).add(vt.stringTable[row][0]);

				  }
				  
				  Collections.sort(geneNames.get(geneNames.size()-1));
			  }
			  System.out.print("THRESHOLD\t");
			  for(int i=0;i<samples.size();i++){
				  System.out.print(samples.get(i)+"\t");
			  }
			  System.out.println();
			  for(int k=0;k<100;k++){
				  float thresh = (float)k*0.01f*4f;
				  System.out.print(thresh+"\t");
				  for(int i=0;i<samples.size();i++){
					  System.out.print(det[k][i]+"\t");
				  }
				  System.out.println();
			  }
		  }
		  
		  
		  public static void doPCAonSubsets(VDataTable vt, String folder, String prefix){
			  File list[] = new File(folder).listFiles();
			  vt.makePrimaryHash(vt.fieldNames[0]);
			  VDatReadWrite.useQuotesEverywhere = false;
			  for(File f: list){
				  if(f.getName().startsWith(prefix))if(!f.getName().contains("_sel"))if(!f.getName().contains("_pca")){
					  String pr = f.getName().substring(0,f.getName().length()-4);
					  System.out.println(pr);
					  Vector<String> ids = Utils.loadStringListFromFile(f.getAbsolutePath());
					  VDataTable vts = VSimpleProcedures.selectRowsFromList(vt, ids);
					  vts = vts.transposeTable(vts.fieldNames[0]);
					  VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vts, -1);
					  VDatReadWrite.saveToSimpleDatFile(vts, folder+pr+"_sel.txt");
					  //VDatReadWrite.saveToVDatFile(vts, folder+pr+"_sel.dat");
					  PCAMethod pca = new PCAMethod();
					  pca.setDataSet(vd);
					  pca.calcBasis(3);
					  VDataSet vdp = pca.getProjectedDataset();
					  VDatReadWrite.saveToSimpleDatFilePureNumerical(vdp, folder+pr+"_pca.txt");
				  }
			  }
		  }
		  
		  public static void makeGMTFromSFile(String sfile, float threshold) throws Exception{
				VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(sfile, true, "\t");
				TableUtils.findAllNumericalColumns(vt);
				vt.makePrimaryHash(vt.fieldNames[0]);
				String fn = sfile.substring(0, sfile.length()-4);
				FileWriter fw = new FileWriter(fn+threshold+".gmt");
				for(int i=1;i<vt.colCount;i++){
					String col = vt.fieldNames[i];
					for(int j=-1;j<=1;j+=2){
						String sign = (j<0)?"-":"+";
						String setname = col+sign;
						fw.write(setname+"\tna\t");
						for(int k=0;k<vt.rowCount;k++){
							float val = Float.parseFloat(vt.stringTable[k][i]);
							if(Math.abs(val)>threshold){
								if((float)j*val>0) 
									fw.write(vt.stringTable[k][0]+"\t");
							}
						}
						fw.write("\n");
					}
				}
				fw.close();
		  }

    public static void ComputeStateTransitionDynamics(String dataFile, String gmtFile, String annotationFile, String field, String labels[], int minNumberOfGenes){
    	VDataTable data = VDatReadWrite.LoadFromSimpleDatFile(dataFile, true, "\t",true);
    	data.makePrimaryHash(data.fieldNames[0]);
    	VDataTable annotations = VDatReadWrite.LoadFromSimpleDatFile(annotationFile, true, "\t");
    	annotations.makePrimaryHash(field);
    	Vector<GESignature> sigs = GMTReader.readGMTDatabase(gmtFile);
    	
    	System.out.print("MODULE\t"); for(int i=0;i<labels.length;i++) System.out.print(labels[i]+"\t"); System.out.println();
    	
    	for(int i=0;i<sigs.size();i++)if(sigs.get(i).geneNames.size()>minNumberOfGenes){
    		System.out.print(sigs.get(i).name+"\t");
    		for(int j=0;j<labels.length;j++){
    			Vector<String> samples = new Vector<String>();
    			samples.add(data.fieldNames[0]);
    			Vector<Integer> sinds = annotations.tableHashPrimary.get(labels[j]);
    			for(Integer index: sinds) samples.add(annotations.stringTable[index][0]);
    			Vector<Integer> ginds = new Vector<Integer>();
    			
    			for(String g: sigs.get(i).geneNames)if(data.tableHashPrimary.get(g)!=null){
    			    int ind = data.tableHashPrimary.get(g).get(0);
    			    ginds.add(ind);
    			}
    			
    			VDataTable subTable = VSimpleProcedures.selectRows(data, ginds);
    			
    			TableUtils.findAllNumericalColumns(subTable);
    			VDatReadWrite.useQuotesEverywhere = false;
    			VDatReadWrite.saveToVDatFile(subTable, "C:/Datas/MOSAIC/analysis/critical_transition_index/"+sigs.get(i).name+".dat");
    			VDatReadWrite.saveToSimpleDatFile(subTable, "C:/Datas/MOSAIC/analysis/critical_transition_index/"+sigs.get(i).name+".txt");
    			
    			VDataTable subTableT = VSimpleProcedures.selectRows(data, ginds);
    			subTableT = subTableT.transposeTable(subTableT.fieldNames[0]);
    			subTableT = VSimpleProcedures.MergeTables(subTableT, subTableT.fieldNames[0], annotations, annotations.fieldNames[0], "_");
    			VDatReadWrite.saveToVDatFile(subTableT, "C:/Datas/MOSAIC/analysis/critical_transition_index/"+sigs.get(i).name+"_T.dat");
    			VDatReadWrite.saveToSimpleDatFile(subTableT, "C:/Datas/MOSAIC/analysis/critical_transition_index/"+sigs.get(i).name+"_T.txt");
    			
    			subTable = VSimpleProcedures.SelectColumns(subTable, samples);
    			
    			TableUtils.findAllNumericalColumns(subTable);
    			VDatReadWrite.useQuotesEverywhere = false;
    			VDatReadWrite.saveToVDatFile(subTable, "C:/Datas/MOSAIC/analysis/critical_transition_index/"+sigs.get(i).name+"_"+labels[j]+".dat");
    			
    			float stateTransitionIndex = computeRatioAverageColumnRowCorrelation(subTable);
    			
    			System.out.print(stateTransitionIndex+"\t");
    		}
    		System.out.println();
    	}
    }
    
    public static float computeRatioAverageColumnRowCorrelation(VDataTable vt){
    	float index = 0f;
    	TableUtils.findAllNumericalColumns(vt);
    	VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
    	
    	float correlationColumns = 0f;
    	for(int i=0;i<vd.coordCount;i++)
    		for(int j=i+1;j<vd.coordCount;j++){
    			float v1[] = new float[vd.pointCount];
    			for(int k=0;k<vd.pointCount;k++) v1[k] = vd.massif[k][i];
    			float v2[] = new float[vd.pointCount];
    			for(int k=0;k<vd.pointCount;k++) v2[k] = vd.massif[k][j];
    			correlationColumns+=Math.abs(VSimpleFunctions.calcCorrelationCoeff(v1, v2));
    		}
    	correlationColumns/=(float)(vd.coordCount*vd.coordCount-vd.coordCount)/2f;
    	
    	float correlationRows = 0f;
    	for(int i=0;i<vd.pointCount;i++)
    		for(int j=i+1;j<vd.pointCount;j++){
    			float v1[] = vd.massif[i];
    			float v2[] = vd.massif[j];
    			correlationRows+=Math.abs(VSimpleFunctions.calcCorrelationCoeff(v1, v2));
    		}
    	correlationRows/=(float)(vd.pointCount*vd.pointCount-vd.pointCount)/2f;
    	index = correlationColumns/correlationRows;
    	return correlationRows;
    }
    
    public static void makeSelectionOfSamplesBasedOnWords(String fn, String fn_words){
    	VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
    	Vector<String> words = Utils.loadStringListFromFile(fn_words);
    	for(int j=0;j<words.size();j++){
    		String word = words.get(j);
    	for(int i=0;i<vt.rowCount;i++){
    		String id = vt.stringTable[i][vt.fieldNumByName("ID")];
    		String Samples = vt.stringTable[i][vt.fieldNumByName("Samples")];
    		String group = vt.stringTable[i][vt.fieldNumByName("GROUP")];
    		String ic = vt.stringTable[i][3];
    		if(id!=null)
    		if(id.toLowerCase().contains(word)){
    			//System.out.println(id+"\t"+word+"\t"+group+"\t"+ic);
    			System.out.println(id+"\t"+word+"\t_0000FF");
    		}
    	}
    	}
    }
    
    public static void IntersectTwoSetsOfSignatures(String gmt1, String gmt2) throws Exception{
    	Vector<GESignature> g1 = GMTReader.readGMTDatabase(gmt1);
    	Vector<GESignature> g2 = GMTReader.readGMTDatabase(gmt2);
    	
    	FileWriter fw = new FileWriter(gmt1+".sif"); 
    	
    	Graph graph = new Graph();
    	
    	String table = "IC\tNUMBER\tGENES\n";
    	
    	for(int i=0;i<g1.size();i++){
    		System.out.println(">>>"+g1.get(i).name+" ("+g1.get(i).geneNames.size()+")");
    		HashSet sig = new HashSet();
    		for(int k=0;k<g1.get(i).geneNames.size();k++) sig.add(g1.get(i).geneNames.get(k));
    		for(int j=0;j<g2.size();j++){
    			HashSet s1 = new HashSet();
    			HashSet s2 = new HashSet();
    			for(int k=0;k<g1.get(i).geneNames.size();k++) s1.add(g1.get(i).geneNames.get(k));
    			for(int k=0;k<g2.get(j).geneNames.size();k++) { s2.add(g2.get(j).geneNames.get(k));}
    			if(false)for(int l=0;l<g2.get(j).geneNames.size();l++)
    				for(int k=0;k<g2.size();k++)if(k!=j){
    					if(g2.get(k).geneNames.contains(g2.get(j).geneNames.get(l)))
    						s2.remove(g2.get(j).geneNames.get(l));
    				}
    			
    			for(Object s: s2) sig.remove(s); 
    			
    			HashSet hs = Utils.IntersectionOfSets(s1, s2);
    			int intersection = hs.size();
    			//System.out.println(g2.get(j).name+" ("+g2.get(j).geneNames.size()+")"+"\t"+intersection);
    			String names = "";
    			for(Object s:hs)
    				names+=(String)s+",";
    			if(names.endsWith(",")) names = names.substring(0, names.length()-1);

    			Node n = graph.getCreateNode(g2.get(j).name);
				n.setAttributeValueUnique("SET", "IC", Attribute.ATTRIBUTE_TYPE_STRING);
				
    			for(Object s:hs){
    				fw.write((String)s+"\tna\t"+g2.get(j).name+"\n");
    				Node ni = graph.getCreateNode((String)s);
    				Edge e = graph.getCreateEdge((String)s+"_"+g2.get(j).name);
    				e.Node1 = ni;
    				e.Node2 = n;
    			}
    			
    			String ss = g2.get(j).name+"\t"+intersection+"\t"+names;
    			System.out.println(ss);
    			table += ss+"\n";
    			
    		}
    		System.out.println("NOTIN:\t"+sig.size());
    	}
    	
    	graph.calcNodesInOut();
    	Random r = new Random();
    	int count=0;
    	for(int j=0;j<2;j++)
    	for(int i=0;i<graph.Nodes.size();i++){
    		Node n = graph.Nodes.get(i);
    		//System.out.println(n.Id);
    		if(n.getFirstAttributeValue("SET")!=null){
    			if(n.incomingEdges.size()>0){
    				n.x = 100f;
    				n.y = (count++)*50;
    				//System.out.println("\t"+n.Id);
    			}else{
    				n.x = -500f;
    				n.y = 0f;
    			}
    		}
    		if(n.getFirstAttributeValue("SET")==null){
    			System.out.println(n.Id);
    			if(n.outcomingEdges.size()>0){
    				float mx = 0f;
    				float my = 0f;
    				int c = 0;
    				for(int k=0;k<n.outcomingEdges.size();k++){
    					Node nic = n.outcomingEdges.get(k).Node2;
    					System.out.println("\t"+nic.Id+"\t"+nic.y);
    					//if(nic.y>1e-6){
    						mx+=nic.x;
    						my+=nic.y;
    						c++;
    					//}
    				}
    				n.x = -250f+r.nextFloat()*100f; //mx/(float)c;
    				n.y = my/(float)c+r.nextFloat()*100f;
    			}
    		}
    		}
    	
    	fw.close();
    	XGMML.saveToXGMML(graph, gmt1+".xgmml");

    	FileWriter fw1 = new FileWriter(gmt1+".tmp");
    	fw1.write(table);
    	fw1.close();
    	VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(gmt1+".tmp", true, "\t");
    	vt1.makePrimaryHash(vt1.fieldNames[0]);
    	for(int i=1;i<=100;i++){
    		String s = "IC"+i;
    		if(vt1.tableHashPrimary.get(s+"+")!=null)
    			if(vt1.tableHashPrimary.get(s+"-")!=null){
    				String s1 = vt1.stringTable[vt1.tableHashPrimary.get(s+"+").get(0)][2];
    				int n1 = Integer.parseInt(vt1.stringTable[vt1.tableHashPrimary.get(s+"+").get(0)][1]);
    				String s2 = vt1.stringTable[vt1.tableHashPrimary.get(s+"-").get(0)][2];
    				int n2 = Integer.parseInt(vt1.stringTable[vt1.tableHashPrimary.get(s+"-").get(0)][1]);
    				if(n1+n2>0)
    					System.out.println(s+"\t"+(s1==null?"":s1)+"\t"+(s2==null?"":s2));
    			}
    	}
    	
    }
    
    public static void makeRNKFileFromScoreExtremePoints(String fn, String moduleFile, String moduleName, int numberOfPointsLow, int numberOfPointsHigh, String maskFile, String maskValue) throws Exception{
    	VDataTable scores = VDatReadWrite.LoadFromSimpleDatFile(moduleFile, true, "\t");
    	Vector<String> masks = Utils.loadStringListFromFile(maskFile);
    	float vals[] = new float[scores.rowCount];
    	for(int i=0;i<vals.length;i++) vals[i] = Float.parseFloat(scores.stringTable[i][scores.fieldNumByName(moduleName)]);
    	int inds[] = Algorithms.SortMass(vals);
    	Vector<Integer> lowSamples = new Vector<Integer>();
    	Vector<Integer> highSamples = new Vector<Integer>();
    	Vector<String> lowSampleIDs = new Vector<String>();
    	Vector<String> highSampleIDs = new Vector<String>();
    	
    		for(int i=0;i<inds.length;i++){
    			if(masks.get(inds[i]).equals(maskValue)) { lowSamples.add(inds[i]); lowSampleIDs.add(scores.stringTable[inds[i]][0]); }
    	    	if(lowSamples.size()>=numberOfPointsLow) break;
    		}
    		for(int i=0;i<inds.length;i++){
    			if(masks.get(inds[inds.length-1-i]).equals(maskValue)) { highSamples.add(inds[inds.length-1-i]); highSampleIDs.add(scores.stringTable[inds[inds.length-1-i]][0]); }
    	    	if(highSamples.size()>=numberOfPointsHigh) break;
    		}
    	for(int i=0;i<lowSamples.size();i++) System.out.println("low"+i+"("+lowSampleIDs.get(i)+")"+"\t"+vals[lowSamples.get(i)]);
    	for(int i=0;i<highSamples.size();i++) System.out.println("high"+i+"("+highSampleIDs.get(i)+")"+"\t"+vals[highSamples.get(i)]);
    	
    	makeRNKFileFromTtest(fn,lowSampleIDs,highSampleIDs,fn+"_"+moduleName+".rnk");
    }
    
    public static void makeRNKFileFromTtest(String fn, Vector<String> lowSamples,Vector<String> highSamples, String outf) throws Exception{
    	LineNumberReader lr = new LineNumberReader(new FileReader(fn));
    	Vector<Integer> lowIndices = new Vector<Integer>();
    	Vector<Integer> highIndices = new Vector<Integer>();
    	String firstLine = lr.readLine();
    	String parts[] = firstLine.split("\t");
    	for(int i=0;i<parts.length;i++){
    		if(lowSamples.contains(parts[i])) lowIndices.add(i);
    		if(highSamples.contains(parts[i])) highIndices.add(i);
    	}
    	String s = null;
    	FileWriter fw = new FileWriter(outf);
    	while((s=lr.readLine())!=null){
    		parts = s.split("\t");
    		Vector<Float> vLow = new Vector<Float>();
    		Vector<Float> vHigh = new Vector<Float>();
    		for(int i=0;i<lowIndices.size();i++) vLow.add(Float.parseFloat(parts[lowIndices.get(i)]));
    		for(int i=0;i<highIndices.size();i++) vHigh.add(Float.parseFloat(parts[highIndices.get(i)]));
    		Vector<Integer> df = new Vector<Integer>();
    		double ttest = VSimpleFunctions.calcTTest(vLow, vHigh, false, df);
    		fw.write(parts[0]+"\t"+ttest+"\n");
    	}
    	fw.close();
    }
    
    public static void MakeSignatureSelectionFile(String data, String gmt, String signature) throws Exception{
    	GMTReader gr = new GMTReader();
    	Vector<GESignature> sigs = gr.readGMTDatabase(gmt);
    	String fn = data.substring(0,data.length()-4);
    	FileWriter fw = new FileWriter(fn+"_"+signature+".txt");
    	for(int i=0;i<sigs.size();i++)if(sigs.get(i).name.equals(signature)){
    		
    		HashMap<String,String> genes = new HashMap<String,String>();
    		for(int j=0;j<sigs.get(i).geneNames.size();j++){
    			System.out.println(sigs.get(i).geneNames.get(j));
    			genes.put(sigs.get(i).geneNames.get(j), sigs.get(i).geneNames.get(j));
    		}
    		
    		LineNumberReader lr = new LineNumberReader(new FileReader(data));
    		String s = lr.readLine();
    		fw.write(s+"\n");
    		while((s=lr.readLine())!=null){
    			String parts[] = s.split("\t");
    			if(genes.containsKey(parts[0])){
    				fw.write(s+"\n");
    			}
    		}
    		lr.close();
    	}
    	fw.close();
    }

	public class SpringNode
	{
	  private int name;
	  public int getName() { return this.name; }
	  public void setName(int name) { this.name = name; }
	  private int number;
	  public int getNumber() { return this.number; }
	  public void setNumber(int number) { this.number = number; }
	}
	public class SpringLink
	{
	  private int source;
	  public int getSource() { return this.source; }
	  public void setSource(int source) { this.source = source; }
	  private int target;
	  public int getTarget() { return this.target; }
	  public void setTarget(int target) { this.target = target; }
	}

	public class SpringGraphRoot
	{
	  private ArrayList<Node> nodes;
	  public ArrayList<Node> getNodes() { return this.nodes; }
	  public void setNodes(ArrayList<Node> nodes) { this.nodes = nodes; }
	  private ArrayList<SpringLink> links;
	  public ArrayList<SpringLink> getLinks() { return this.links; }
	  public void setLinks(ArrayList<SpringLink> links) { this.links = links; }
	}
	
	public static void ConvertGMT2OccurenceTable(String gmtFile) throws Exception{
		Vector<GESignature> gmt = GMTReader.readGMTDatabase(gmtFile);
		FileWriter fw = new FileWriter(gmtFile+".table");
		fw.write("GENE\t"); 
		for(int i=0;i<gmt.size();i++) fw.write(gmt.get(i).name+"\t"); fw.write("\n");
		HashMap<String,Integer> allgenes = new HashMap<String,Integer>();
		for(int i=0;i<gmt.size();i++){
			GESignature sig = gmt.get(i);
			for(int j=0;j<gmt.get(i).geneNames.size();j++)
				allgenes.put(gmt.get(i).geneNames.get(j),1);
		}
		Vector<String> genes = new Vector<String>();
		Set<String> gns = allgenes.keySet();
		for(String g:gns) genes.add(g);
		Collections.sort(genes);
		for(int i=0;i<genes.size();i++) allgenes.put(genes.get(i), i);
		
		for(int i=0;i<genes.size();i++){
			fw.write(genes.get(i)+"\t");
			System.out.println(genes.get(i));
			for(int j=0;j<gmt.size();j++){
				GESignature sig = gmt.get(j);
				if(sig.geneNames.contains(genes.get(i)))
					fw.write("1\t");
				else
					fw.write("0\t");
			}
			fw.write("\n");
				
		}
		
		fw.close();
	}
	
	public static void ProcessPDXDataset_qc(String folder, String fn, float mit_threshold, float pooled_mit_threshold, float mit_perc_threshold, int low_tc_limit, int high_tc_limit) throws Exception{
		
		System.out.println("Processing "+fn);
		
		String pref = fn.substring(0,fn.length()-4);
		
		// Replace spaces with tabs
		System.out.println("Replacing spaces with tabs...");
		LineNumberReader lr = new LineNumberReader(new FileReader(folder+fn));
		String s = null;
		FileWriter fw = new FileWriter(folder+pref+"_tb.txt");
		while((s=lr.readLine())!=null){
			s = Utils.replaceString(s, " ", "\t");
			fw.write(s+"\n");
		}
		fw.close();

		System.out.println("Selecting only unique gene names...");
		ProcessTxtData.SelectUniqueByVariationFast(folder+pref+"_tb.txt");
		FileUtils.copyFile(new File(folder+pref+"_tb.txt_unique.txt"), new File(folder+pref+"_u.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_tb.txt_unique.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_tb.txt"));

		System.out.println("Compute count percentage in different gene sets...");
		readcountPercentageScores(folder+pref+"_u.txt",signatureDefinitionFile);
		
		// Computing library size
		lr = new LineNumberReader(new FileReader(folder+pref+"_u.txt"));
		s = lr.readLine();
		String parts[] = s.split("\t");
		int nsamples = parts.length-1;
		int sums[] = new int[nsamples];
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			for(int i=1;i<parts.length;i++)
				sums[i-1] = sums[i-1] + Integer.parseInt(parts[i]);
		}
		String suffix = "_u.txt";
		
		// Questionable step - normalize by library size (column size)
		
		float vals[] = new float[sums.length];
		for(int l=0;l<vals.length;l++) vals[l] = sums[l];
		float med = VSimpleFunctions.calcMedian(vals);
		System.out.println("Median column sum = "+med);
		
		lr = new LineNumberReader(new FileReader(folder+pref+"_u.txt"));
		fw = new FileWriter(folder+pref+"_u1.txt");
		s = lr.readLine();
		fw.write(s+"\n");
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			fw.write(parts[0]+"\t");
			for(int i=1;i<parts.length;i++){
				float val = Float.parseFloat(parts[i]);
				val=val/(float)sums[i-1]*(float)med;
				fw.write(val+"\t");
			}
			fw.write("\n");
		}
		fw.close();
		suffix = "_u1.txt";
		
		ProcessTxtData.LogXTruncFast(folder+pref+suffix, 1f, 3);
		FileUtils.copyFile(new File(folder+pref+suffix+"_logx1.0.txt"), new File(folder+pref+"_nu.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+suffix+"_logx1.0.txt"));
		
		ProcessTxtData.filterByVariationFast(folder+pref+"_nu.txt", 2000);
		FileUtils.copyFile(new File(folder+pref+"_nu.txt_filtered.txt"), new File(folder+pref+"_nu2k.txt"));
		
		ProcessTxtData.filterByVariationFast(folder+pref+"_nu.txt", 10000);
		
		// produce mitochondrial scores
		//ComputeAverageModuleValues(folder+pref+"_nu.txt",folder+"mitochondrial_genes.gmt",folder+pref+"_nu.txt_filtered_var.txt");
		ComputeAverageModuleValues_fast(folder+pref+"_nu.txt",folder+"../code/mitochondrial_genes.gmt");
		System.out.println("Computing raw mitochondrial scores from "+pref+"_nu2k.txt...");
		//ComputeAverageModuleValues(folder+pref+"_nu2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt",folder+pref+"_nu.txt_filtered_var.txt");
		ComputeAverageModuleValues_fast(folder+pref+"_nu2k.txt",signatureDefinitionFile);
		//ComputeAverageModuleValues(folder+pref+"_nu2k.txt",folder+"mitochondrial_genes.gmt",folder+pref+"_nu.txt_filtered_var.txt");		

		
		System.out.println("Filtering cells...");
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nu.txt.moduleAverages", true, "\t");
		VDataTable vt_perc = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_u.txt.rcPercentage", true, "\t");

		fw = new FileWriter(folder+pref+"_goodcells.naive");
		FileWriter fw1 = new FileWriter(folder+pref+"_nu_qcdata.txt");
		fw1.write("SAMPLE\tMIT_SCORE\tMIT_PERCENTAGE\tTOTAL_COUNT\n");
		int k = vt.fieldNumByName("Ilicic2016");
		int k_perc = vt_perc.fieldNumByName("Ilicic2016");
		for(int i=0;i<vt_perc.rowCount;i++){
			fw1.write(vt.stringTable[i][0]+"\t"+vt.stringTable[i][k]+"\t"+vt_perc.stringTable[i][k_perc]+"\t"+sums[i]+"\n");
			//if(Float.parseFloat(vt.stringTable[i][k])<mit_threshold)
			boolean selected = false;
			if(Float.parseFloat(vt_perc.stringTable[i][k_perc])<mit_perc_threshold)
			if(sums[i]>low_tc_limit)
			if(sums[i]<high_tc_limit)
			{
				fw.write(vt.stringTable[i][0]+"\n");
				selected = true;
			}
			
			if(!selected){
				System.out.println("Not selected: "+vt.stringTable[i][0]+"\t"+vt_perc.stringTable[i][k_perc]+"\t"+sums[i]);
			}
		}
		fw1.close();
		fw.close();
		
		FileUtils.copyFile(new File(folder+pref+"_goodcells.naive"), new File(folder+pref+"_goodcells.txt"));
		
		// Now we pool all cells, in order to determine the pooled mitochondrial score
		// Note: this step is not needed anymore if we use MIT_PERCENTAGE score
		/*
		  
		FileUtils.deleteQuietly(new File(folder+pref+"_nup.txt"+"_done"));
		String comm = "cd C:/GitPrograms/SPRING-master/preprocessing_matlab/;simple_pooling_function('"+folder+pref+"_u.txt"+"','"+folder+pref+"_nup.txt"+"',5,0.01);quit;";
		System.out.println(comm);
		
		String command[] = new String[]{"matlab","-nodesktop","-nosplash","-r",comm};
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		Process process = pb.start();
		int exitCode = process.waitFor();
		if (exitCode == 0) {
			System.out.println("Executed successfully");
		}
		else {
			System.out.println("Failed ...");
		}
		
		while(!new File(folder+pref+"_nup.txt"+"_done").exists());
		FileUtils.deleteQuietly(new File(folder+pref+"_nup.txt"+"_done"));
		
		System.out.println("Initial pooling done.");
		
		ComputeAverageModuleValues(folder+pref+"_nup.txt",folder+"mitochondrial_genes.gmt",folder+pref+"_nu.txt_filtered_var.txt");		
		
		System.out.println("Pooled mitochondrial score has been computed.");
		
		
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nup.txt.moduleAverages", true, "\t");
		fw = new FileWriter(folder+pref+"_goodcells.pooled");
		for(int i=0;i<vt.rowCount;i++){
			if(Float.parseFloat(vt.stringTable[i][1])<pooled_mit_threshold)
			if(sums[i]>low_tc_limit)
			if(sums[i]<high_tc_limit)
			{
				String name = vt.stringTable[i][0];
				name = Utils.replaceString(name, "_", "-");
				fw.write(name+"\n");
			}
		}
		fw.close();
		
		FileUtils.copyFile(new File(folder+pref+"_goodcells.pooled"), new File(folder+pref+"_goodcells.txt"));
		*/

		
		//FileUtils.deleteQuietly(new File(folder+pref+"_nu.txt.moduleAverages"));
		FileUtils.deleteQuietly(new File(folder+pref+"_nu.txt_filtered.txt"));
		
	}
	
	public static void ProcessPDXDataset_process(String folder, String fn) throws Exception{
		String pref = fn.substring(0,fn.length()-4);
		
		System.out.println("Processing the dataset...");
		
		// In this procedure, we work only with selection, established before
		
		System.out.println("Selecting cells...");
		
		ProcessTxtData.SelectColumnsFast(folder+pref+"_nu.txt", folder+pref+"_goodcells.txt", true);
		
		FileUtils.copyFile(new File(folder+pref+"_nu.txt.colsel.txt"), new File(folder+pref+"_nuf.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_nu.txt.colsel.txt"));

		ProcessTxtData.SelectColumnsFast(folder+pref+"_u.txt", folder+pref+"_goodcells.txt", true);
		
		FileUtils.copyFile(new File(folder+pref+"_u.txt.colsel.txt"), new File(folder+pref+"_uf.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_u.txt.colsel.txt"));
		
		// Computing library size
		LineNumberReader lr = new LineNumberReader(new FileReader(folder+pref+"_uf.txt"));
		String s = lr.readLine();
		String parts[] = s.split("\t");
		int nsamples = parts.length-1;
		int sums[] = new int[nsamples];
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			for(int i=1;i<parts.length;i++)
				sums[i-1] = sums[i-1] + Integer.parseInt(parts[i]);
		}
		
		ProcessTxtData.filterByVariationFast(folder+pref+"_nuf.txt", 2000);
		
		FileUtils.copyFile(new File(folder+pref+"_nuf.txt_filtered.txt"), new File(folder+pref+"_nuf2k.txt"));

		FileUtils.deleteQuietly(new File(folder+pref+"_nuf.txt_filtered.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_nuf.txt_filtered_var.txt"));
		

		
		ProcessTxtData.filterByVariationFast(folder+pref+"_nuf.txt", 10000);
		
		FileUtils.copyFile(new File(folder+pref+"_nuf.txt_filtered.txt"), new File(folder+pref+"_nuf10k.txt"));

		FileUtils.deleteQuietly(new File(folder+pref+"_nuf.txt_filtered.txt"));
		//FileUtils.deleteQuietly(new File(folder+pref+"_nuf.txt_filtered_var.txt"));
		
		System.out.println("Computing module scores from "+pref+"_nuf2k.txt...");
		//ComputeAverageModuleValues(folder+pref+"_nuf2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt",folder+pref+"_nuf.txt_filtered_var.txt");
		ComputeAverageModuleValues_fast(folder+pref+"_nuf2k.txt",signatureDefinitionFile);
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nuf2k.txt.moduleAverages", true, "\t");
		VDatReadWrite.useQuotesEverywhere = false;

		FileWriter fw = new FileWriter(folder+pref+"_nuf2k_modulescores.txt");
		for(int i=0;i<vt.rowCount;i++){
			if(!vt.stringTable[i][1].equals("NaN")){
				String name = vt.stringTable[i][0];
				name = Utils.replaceString(name, "+", "plus");
				name = Utils.replaceString(name, "-", "minus");
				fw.write(name+"\t");
				for(int j=1;j<vt.colCount;j++)
					fw.write(vt.stringTable[i][j]+"\t");
				fw.write("\n");
			}	
		}
		fw.close();
		
		vt.addNewColumn("TOTAL_COUNTS", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++)
			vt.stringTable[i][vt.fieldNumByName("TOTAL_COUNTS")] = ""+sums[i];
		
		VDataTable vtqc = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nu_qcdata.txt",true,"\t");
		vtqc.makePrimaryHash(vtqc.fieldNames[0]);
		vt.addNewColumn("MIT_SCORE", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++){
			String sample = vt.stringTable[i][0];
			Float ms = Float.parseFloat(vtqc.stringTable[vtqc.tableHashPrimary.get(sample).get(0)][1]);
			vt.stringTable[i][vt.fieldNumByName("MIT_SCORE")] = ""+ms;
		}
		
		VDatReadWrite.saveToSimpleDatFile(vt, folder+pref+"_nuf2k.txt.moduleAverages");
		
		
		// =================================================================
		if(true){
		
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt"+"_done"));
		String comm = "cd "+matlabCodeFolder+";simple_pooling_function('"+folder+pref+"_uf.txt"+"','"+folder+pref+"_nufp.txt"+"',5,0.01);quit;";
		System.out.println(comm);
		
		String command[] = new String[]{"matlab","-nodesktop","-nosplash","-r",comm};
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		Process process = pb.start();
		int exitCode = process.waitFor();
		if (exitCode == 0) {
			System.out.println("Executed successfully");
		}
		else {
			System.out.println("Failed ...");
		}
		
		while(!new File(folder+pref+"_nufp.txt"+"_done").exists()) Thread.sleep(1000);
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt"+"_done"));
		
		System.out.println("Pooling finished.");
		
		// Replacing tires back
		lr = new LineNumberReader(new FileReader(folder+pref+"_nufp.txt"));
		fw = new FileWriter(folder+pref+"_nufp_tires.txt");
		s = lr.readLine();
		s = Utils.replaceString(s, "_1", "-1");
		fw.write(s+"\n");
		while((s=lr.readLine())!=null){
			fw.write(s+"\n");
		}
		fw.close();
		lr.close();
		
		FileUtils.copyFile(new File(folder+pref+"_nufp_tires.txt"), new File(folder+pref+"_nufp.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp_tires.txt"));
		
		
		ProcessTxtData.filterByVariationFast(folder+pref+"_nufp.txt", 2000);
		
		FileUtils.copyFile(new File(folder+pref+"_nufp.txt_filtered.txt"), new File(folder+pref+"_nufp2k.txt"));

		FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt_filtered.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt_filtered_var.txt"));
		

		
		ProcessTxtData.filterByVariationFast(folder+pref+"_nufp.txt", 10000);
		
		FileUtils.copyFile(new File(folder+pref+"_nufp.txt_filtered.txt"), new File(folder+pref+"_nufp10k.txt"));

		FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt_filtered.txt"));
		//FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt_filtered_var.txt"));
		
		System.out.println("Computing module scores from "+pref+"_nufp2k.txt");
		//ComputeAverageModuleValues_fast(folder+pref+"_nufp2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt",folder+pref+"_nufp.txt_filtered_var.txt");
		ComputeAverageModuleValues_fast(folder+pref+"_nufp2k.txt",signatureDefinitionFile);
		
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nufp2k.txt.moduleAverages", true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		vt = vt.transposeTable(vt.fieldNames[0]);
		VDatReadWrite.useQuotesEverywhere = false;

		fw = new FileWriter(folder+pref+"_nufp2k_modulescores.txt");
		for(int i=0;i<vt.rowCount;i++){
			if(!vt.stringTable[i][1].equals("NaN")){
				String name = vt.stringTable[i][0];
				name = Utils.replaceString(name, "+", "plus");
				name = Utils.replaceString(name, "-", "minus");
				fw.write(name+"\t");
				for(int j=1;j<vt.colCount;j++)
					fw.write(vt.stringTable[i][j]+"\t");
				fw.write("\n");
			}	
		}
		fw.write("TOTAL_COUNTS\t");
		for(int i=0;i<sums.length;i++) fw.write(sums[i]+"\t");
		fw.write("\n");
		fw.close();
		}
		
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nufp2k.txt.moduleAverages", true, "\t");
		vt.addNewColumn("TOTAL_COUNTS", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++)
			vt.stringTable[i][vt.fieldNumByName("TOTAL_COUNTS")] = ""+sums[i];
		
		vtqc = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nu_qcdata.txt",true,"\t");
		vtqc.makePrimaryHash(vtqc.fieldNames[0]);
		vt.addNewColumn("MIT_SCORE", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++){
			String sample = vt.stringTable[i][0];
			Float ms = Float.parseFloat(vtqc.stringTable[vtqc.tableHashPrimary.get(sample).get(0)][1]);
			vt.stringTable[i][vt.fieldNumByName("MIT_SCORE")] = ""+ms;
		}
		
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.saveToSimpleDatFile(vt, folder+pref+"_nufp2k.txt.moduleAverages");
		

		//====================== CLUSTERING IN 2 CLUSTERS ========================

		ProcessTxtData.SelectRowsFast(folder+pref+"_nufp.txt", folder+"IC10+_genes.txt");
		FileUtils.copyFile(new File(folder+pref+"_nufp.txt.rowsel.txt"), new File(folder+pref+"_nufp_IC10+.txt"));
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp.txt.rowsel.txt"));
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nufp_IC10+.txt", true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		vt = vt.transposeTable(vt.fieldNames[0]);
		VDatReadWrite.useQuotesEverywhere = false;
		vt.fieldNames[0] = "SAMPLE";
		VDatReadWrite.saveToSimpleDatFile(vt, folder+pref+"_nufp_IC10+_T.txt");
		
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp_IC10+_T.txt"+"_done"));
		String comm = "cd "+matlabCodeFolder+";cluster_in_IC10plus_space('"+pref+"_nufp','"+folder+pref+"_nufp_IC10+_T.txt"+"','"+folder+pref+"_nufp2k.txt.moduleAverages"+"');quit;";
		System.out.println(comm);
		
		String command[] = new String[]{"matlab","-nodesktop","-nosplash","-r",comm};
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		Process process = pb.start();
		int exitCode = process.waitFor();
		if (exitCode == 0) {
			System.out.println("Executed successfully");
		}
		else {
			System.out.println("Failed ...");
		}
		
		while(!new File(folder+pref+"_nufp_IC10+_T.txt"+"_done").exists()) Thread.sleep(1000);
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp_IC10+_T.txt"+"_done"));
		
		System.out.println("Clustering finished.");

		
		
	}
	
	public static void ProcessPDXDataset_SPRING(String folder, String fn, int knn) throws Exception{
		String pref = fn.substring(0,fn.length()-4);
		
		FileUtils.deleteQuietly(new File(folder+pref+"_nufp10k.txt"+"_done"));
		String comm = "cd "+matlabCodeFolder+";addpath "+folder+";prepareSPRING('"+pref+"_nufp','"+folder+pref+"_nufp10k.txt"+"','"+folder+pref+"_nufp_IC10+_T.txt_clusters2"+"','"+folder+pref+"_nufp2k_modulescores.txt"+"',"+knn+");quit;";
		System.out.println(comm);
		
		String command[] = new String[]{"matlab","-nodesktop","-nosplash","-r",comm};
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.INHERIT);
		Process process = pb.start();
		int exitCode = process.waitFor();
		if (exitCode == 0) {
			System.out.println("Executed successfully");
		}
		else {
			System.out.println("Failed ...");
		}
		
		while(!new File(folder+pref+"_nufp10k.txt"+"_done").exists()) Thread.sleep(1000);
		FileUtils.deleteQuietly(new File(folder+pref+"_IC10+_T.txt"+"_done"));
		
		System.out.println("Preparing SPRING finished.");
		
		
	}
	
	public static VDataTable takeOnlyTopContributingGenes(VDataTable vt, float threshold){
		vt.makePrimaryHash(vt.fieldNames[0]);
		HashSet<String> set = new HashSet<String>();
		for(int i=0;i<vt.rowCount;i++)for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
			float val = Float.parseFloat(vt.stringTable[i][j]);
			//if(Math.abs(val)>threshold)
			if(val>threshold)
				set.add(vt.stringTable[i][0]);
		}
		Vector<String> ids = new Vector<String>();
		for(String s: set) ids.add(s); 
		VDataTable vt1 = VSimpleProcedures.selectRowsFromList(vt, ids);
		
		// remove negative values
		/*for(int i=0;i<vt1.rowCount;i++)
			for(int j=1;j<vt1.colCount;j++){
				String val = vt1.stringTable[i][j];
				float valf = Float.parseFloat(val);
				if(valf<0) vt1.stringTable[i][j] = "0.0";
			}
		*/
		return vt1;
	}

	public static void ProcessPDXDataset_process_test(String folder, String fn) throws Exception{
		String pref = fn.substring(0,fn.length()-4);

		// Computing library size
		LineNumberReader lr = new LineNumberReader(new FileReader(folder+pref+"_uf.txt"));
		String s = lr.readLine();
		String parts[] = s.split("\t");
		int nsamples = parts.length-1;
		int sums[] = new int[nsamples];
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			for(int i=1;i<parts.length;i++)
				sums[i-1] = sums[i-1] + Integer.parseInt(parts[i]);
		}

		
	}
	
	public static void ProcessPDXDataset_recomputeModuleScores(String folder, String fn) throws Exception{
		
		String pref = fn.substring(0,fn.length()-4);
		
		System.out.println("Recomputing module score table...");
		// Computing library size
		System.out.println("Computing library size...");
		LineNumberReader lr = new LineNumberReader(new FileReader(folder+pref+"_uf.txt"));
		String s = lr.readLine();
		String parts[] = s.split("\t");
		int nsamples = parts.length-1;
		int sums[] = new int[nsamples];
		while((s=lr.readLine())!=null){
			parts = s.split("\t");
			for(int i=1;i<parts.length;i++)
				sums[i-1] = sums[i-1] + Integer.parseInt(parts[i]);
		}

		System.out.println("Computing module scores from "+pref+"_nufp2k.txt");
		//ComputeAverageModuleValues_fast(folder+pref+"_nufp2k.txt","C:/Datas/MOSAIC/expression/chromium_data/clean_count_data/ics_and_signatures.gmt",folder+pref+"_nufp.txt_filtered_var.txt");
		ComputeAverageModuleValues_fast(folder+pref+"_nufp2k.txt",signatureDefinitionFile);
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nufp2k.txt.moduleAverages", true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		vt = vt.transposeTable(vt.fieldNames[0]);
		VDatReadWrite.useQuotesEverywhere = false;

		FileWriter fw = new FileWriter(folder+pref+"_nufp2k_modulescores.txt");
		for(int i=0;i<vt.rowCount;i++){
			if(!vt.stringTable[i][1].equals("NaN")){
				String name = vt.stringTable[i][0];
				name = Utils.replaceString(name, "+", "plus");
				name = Utils.replaceString(name, "-", "minus");
				fw.write(name+"\t");
				for(int j=1;j<vt.colCount;j++)
					fw.write(vt.stringTable[i][j]+"\t");
				fw.write("\n");
			}	
		}
		fw.write("TOTAL_COUNTS\t");
		for(int i=0;i<sums.length;i++) fw.write(sums[i]+"\t");
		fw.write("\n");
		fw.close();
		
		
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nufp2k.txt.moduleAverages", true, "\t");
		vt.addNewColumn("TOTAL_COUNTS", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++)
			vt.stringTable[i][vt.fieldNumByName("TOTAL_COUNTS")] = ""+sums[i];
		
		VDataTable vtqc = VDatReadWrite.LoadFromSimpleDatFile(folder+pref+"_nu_qcdata.txt",true,"\t");
		vtqc.makePrimaryHash(vtqc.fieldNames[0]);
		vt.addNewColumn("MIT_SCORE", "", "", vt.NUMERICAL, "0");
		for(int i=0;i<vt.rowCount;i++){
			String sample = vt.stringTable[i][0];
			Float ms = Float.parseFloat(vtqc.stringTable[vtqc.tableHashPrimary.get(sample).get(0)][1]);
			vt.stringTable[i][vt.fieldNumByName("MIT_SCORE")] = ""+ms;
		}
		
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.saveToSimpleDatFile(vt, folder+pref+"_nufp2k.txt.moduleAverages");

		
	}
    
}
