package getools.scripts;

import fr.curie.BiNoM.pathways.analysis.structure.Edge;

import org.apache.commons.io.FileUtils;
import org.sbml.x2001.ns.celldesigner.SbmlDocument;

import fr.curie.BiNoM.pathways.analysis.structure.Graph;
import fr.curie.BiNoM.pathways.analysis.structure.Node;
import fr.curie.BiNoM.pathways.analysis.structure.Attribute;
import fr.curie.BiNoM.pathways.analysis.structure.StructureAnalysisUtils;
import fr.curie.BiNoM.pathways.utils.MetaGene;
import fr.curie.BiNoM.pathways.utils.ModifyCellDesignerNotes;
import fr.curie.BiNoM.pathways.wrappers.CellDesigner;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import getools.MakeCorrelationGraph;
import getools.TableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;












//import vdaoengine.TableUtils;
import vdaoengine.analysis.PCAMethod;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class PanMethylome {
	
	public static String dataFolder = "";
	public static String cancers[] = {"ACC","BLCA","BLCAC","BRCA","CESC","COAD","ESCA","GBM","HNSC","KICH","KIRC","KIRP","LGG","LIHC","LUAD","LUSC","OVCA","PAAD","PBCA","PCPG","PRAD","RBC","READ","SKCM","STAD","THCA","UCEC","UCS"};
	//public static String cancers[] = {"READ"};


	public static void main(String[] args) {
		try{
	
			PanMethylome pm = new PanMethylome();
			
			/*
			 * Copy files to Google Drive
			 */
			//copyFilesToGoogleDrive("C:/Datas/PanMethylome/methylome/","C:/Users/zinovyev/Google Drive/ACI methylome pan can/MethylICA/");
			//System.exit(0);
			
			/*
			 *  assemble correlation graph
			 */
			//String folder = "C:/Datas/PanMethylome/analysis/correlationgraph/complete_set/";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/ica_corrgraph/full_rescue/";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/stability_corrgraph/";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/saves/search_for_IC3/";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_FINALGRAPH/";
			/*VDataTable vtt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/MOSAIC/analysis/ica/metaanalysis/_TEST/GENES_S.xls", true, "\t");
			VSimpleProcedures.findAllNumericalColumns(vtt);
			fr.curie.BiNoM.pathways.utils.Utils.separatePositiveAndNegativeValues(vtt);			
			System.exit(0);*/
			//String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_TEST/";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_FINALGRAPH/";
			//String folder = "C:/Datas/CAFibroblasts/analysis/metaanalysis/";
			
			
			//String folder = "C:/Datas/SylviaFre/graph/";
			//String folder = "C:/Datas/MOSAIC/analysis/ica/mosaic_variance_decomposition/corr_graph/";
			//String folder = "./";
			String folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_FINALGRAPH/";
			//String folder = "C:/Datas/ColonCancer/analysis/often_communities/_analysis/corrgraph/";
			//MakeCorrelationGraph.SplitAllFilesIntoPositiveAndNegativeTails(folder);
			//String folder = "C:/Datas/ColonCancer/analysis/corrgraph_PCA/";
			/*MakeCorrelationGraph.MakeCorrelationGraph(folder, false);
			MakeCorrelationGraph.assembleCorrelationGraph(folder);
			System.exit(0);*/
				
			/*
			 * Make community table, from a folder with XGGML files
			 */
			//HashMap<String, String> commap = pm.makeCommunityTable("C:/Datas/PanMethylome/analysis/metacomponents/","C:/Datas/PanMethylome/analysis/correlationgraph/correlation_graph_norecipedges.xgmml");
			//pm.makeMetaComponentTable(commap,"C:/Datas/PanMethylome/analysis/metacomponents/","C:/Datas/PanMethylome/methylome/",false);
			/*HashMap<String, String> commap = pm.makeCommunityTable("C:/_DatasArchive/ASSET/_work/analysis/ica/metanalysis/metacomponents/","C:/_DatasArchive/ASSET/_work/analysis/ica/metanalysis/correlation_graph_norecipedges.xgmml");
			pm.makeMetaComponentTable(commap,"C:/_DatasArchive/ASSET/_work/analysis/ica/metanalysis/metacomponents/","C:/_DatasArchive/ASSET/_work/analysis/ica/metanalysis/",false);
			System.exit(0);*/
			
			/*
			 * Annotatate CellDesigner-based correlation graph
			 */
			//pm.annotateCellDesignerFile("C:/Datas/PanMethylome/analysis/correlationgraph/panmeth_navicell/maps/panmeth_src/panmeth_master.xml");
			
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/GBM/raw_data/";
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/PBCA/";
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/COAD/raw_data/";
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/BLCA/raw_data/";
			
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/OVCA/";			
			//String prefix = "OVCA";
			
			//String prefix = "RBC";
			//String prefix = "PBCA";
			//String prefix = "COAD";
			//String prefix = "BLCAC";
			//String prefix = "GBM";
			//String prefix = "BLCA";
			//String prefix = "OVCA";
			//String prefix = "ACC"; 
			//String prefix = "CESC";
			//String prefix = "PAAD";
			//String prefix = "HNSC";
			//String prefix = "LUSC";
			//String prefix = "PCPG";
			//String prefix = "BRCA";
			//String prefix = "STAD";
			//String prefix = "SKCM";
			//String prefix = "ESCA";
			//String prefix = "KICH";
			//String prefix = "KIRC";
			//String prefix = "KIRP";
			//String prefix = "LGG";
			//String prefix = "LIHC";
			//String prefix = "LUAD";
			//String prefix = "PRAD";
			//String prefix = "READ";
			//String prefix = "THCA";
			//String prefix = "UCEC";
			//String prefix = "UCS";
			String prefix = "ACC";
			pm.dataFolder = "C:/Datas/PanMethylome/methylome/"+prefix+"/";
			//pm.dataFolder = "C:/Datas/OvarianCancer/TCGA/analysis/corrgraph_TCGA_3levels/genomic/";
			
			//transformTableToICA("C:/Datas/OvarianCancer/TCGA/analysis/corrgraph_TCGA_3levels/genomic/genomic.txt");
			
			//System.out.println(pm.processTCGASampleNames(pm.dataFolder+prefix+"_samples.txt",3,false));
			//System.out.println(pm.processTCGASampleNames(pm.dataFolder+prefix+"_samples.txt",3,true, "C:/Datas/PanMethylome/sample_info/"+prefix+".txt"));
			//System.out.println(pm.processTCGASampleNames(pm.dataFolder+prefix+"n_samples.txt",3,false, "C:/Datas/PanMethylome/sample_info/"+prefix+".txt"));
			//System.out.println(pm.processTCGASampleNames(pm.dataFolder+prefix+"_samples.txt",3,false, "C:/Datas/PanMethylome/sample_info/"+prefix+".txt"));
			//System.exit(0);

			
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/BLCAC/";
			//String prefix = "BLCAC";
			
			
			//String prefix = "OVCA_test";
			
			//pm.dataFolder = "C:/Datas/PanMethylome/methylome/GBM/";
			//String prefix = "GBM";
			
			Vector<String> files = new Vector<String>();
			//files.add(pm.dataFolder+"OVCA_nonfiltered.txt");
			//files.add(pm.dataFolder+"gbm_met_tum.txt");
			//files.add(pm.dataFolder+"test1.txt");
			//files.add(pm.dataFolder+"coad_met_tum.txt");
			//files.add(pm.dataFolder+"test2.txt");
			//files.add(pm.dataFolder+"rb_met_tum.txt");
			//files.add(pm.dataFolder+"blca_met_tum.txt");
			//files.add(pm.dataFolder+"pbca_met_tum.txt");
			//files.add(pm.dataFolder+"blca_met_tum.txt");
			//files.add(pm.dataFolder+"acc_met_tum.txt");
			//files.add(pm.dataFolder+"cesc_met_tum.txt");
			//files.add(pm.dataFolder+"paad_met_tum.txt");
			//files.add(pm.dataFolder+"hnsc_met_tum.txt");
			//files.add(pm.dataFolder+"lusc_met_tum.txt");
			//files.add(pm.dataFolder+"pcpg_met_tum.txt");
			//files.add(pm.dataFolder+"brca_met_tum.txt");

			//files.add(pm.dataFolder+"ucs_met_tum.txt");
			//files.add(pm.dataFolder+prefix.toLowerCase()+"_met_norm.txt");
			 
			/*files.add(pm.dataFolder+"blca_met_tum1.txt");
			files.add(pm.dataFolder+"blca_met_tum2.txt");
			files.add(pm.dataFolder+"blca_met_tum3.txt");
			files.add(pm.dataFolder+"blca_met_tum4.txt");*/
			
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+"GBM.txt",0.0f, false,0f,0f);
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+"OVCA.txt",0.0f, true,0f,0f);
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+"RBC.txt",0.0f, false,0f,0f);
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+"BLCAC.txt",0.0f, false,0f,0f);
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+"PBCA.txt",0.0f, false,0f,0f);
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+"BLCA.txt",0.0f, false,0f,0f);
			//ReformatMethylomeFile(files,pm.dataFolder+"OVCA.txt",0.0f,true,1f,0.5f);
			
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+prefix+".txt",0.0f, false,0f,0f);
			//ReformatMethylomeFile(files,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt",pm.dataFolder+prefix+"n.txt",0.0f, false,0f,0f);
			
			//System.out.println(pm.processTCGASampleNames(pm.dataFolder+"GBM_samples.txt"));
			//pm.CompileAandSTables(pm.dataFolder,"GBM","C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/GBM.txt","C:/Datas/PanMethylome/sample_info/GBM_clusters.txt");
			//pm.CompileAandSTables(pm.dataFolder,"BLCAC","C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/clin_blca_tum.txt",null);
			//pm.CompileAandSTables(pm.dataFolder,"OVCA","C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/OVCA.txt",null);
			//pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/clin_rb_tum.txt",null);
			//pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/clin_rb_tum.txt",null);
			//pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/COAD.txt","C:/Datas/PanMethylome/sample_info/COAD_clusters.txt");
			//pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/GBM.txt","C:/Datas/PanMethylome/sample_info/GBM_clusters.txt");
			//pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/BLCA.txt","C:/Datas/PanMethylome/sample_info/BLCA_clusters.txt");
			///pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/"+prefix+".txt",null);

			/*BIODICAPipeLine biodica = new BIODICAPipeLine();
			biodica.ConfigFileName = "C:/Datas/BIODICA/config";
			biodica.readConfigFile();
			Vector<String> componentNames = new Vector<String>();
			Vector<Float> componentVals = new Vector<Float>();
			String gene = "patient.age_at_initial_pathologic_diagnosis";
			for(String pfx: cancers){
				System.out.println("=================================================");
				System.out.println("================  "+pfx+"       ==================");
				System.out.println("=================================================");
				pm.dataFolder = "C:/Datas/PanMethylome/methylome/"+pfx+"/";
				//pm.CompileAandSTables(pm.dataFolder,pfx,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/"+pfx+".txt",null);
				//pm.CompileAandSTables(pm.dataFolder,pfx+"n","C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/"+pfx+"n.txt",null);
				
				Vector<String> strNames = new Vector<String>();
				VDataTable vta = VDatReadWrite.LoadFromSimpleDatFile(pm.dataFolder+pfx+"_A_annot.xls", true, "\t");
				VSimpleProcedures.findAllNumericalColumns(vta);
				for(int i=1;i<vta.colCount;i++) if(vta.fieldNames[i].startsWith("IC")) strNames.add(vta.fieldNames[i]);
				AssociationFinder.printAssosiationTable(vta, pm.dataFolder+pfx+"_A_associate.xls", strNames, biodica, true);
				
				VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(pm.dataFolder+pfx+"_A_associate.xls", true, "\t");
				vt.makePrimaryHash(vt.fieldNames[0]);
				if(vt.tableHashPrimary.get(gene)!=null){
					int k = vt.tableHashPrimary.get(gene).get(0);
					for(int i=1;i<vt.colCount;i++){
						componentNames.add(vt.fieldNames[i]+"_"+pfx);
						componentVals.add(Float.parseFloat(vt.stringTable[k][i]));
					}
				}
				
			}
			for(int i=0;i<componentNames.size();i++){
				System.out.println(componentNames.get(i)+"\t"+componentVals.get(i));
			}
			
			System.exit(0);
			*/
			
			/*
			 * Annotation of S and A files
			 */
			/*for(String px: cancers){
				pm.dataFolder = "C:/Datas/PanMethylome/methylome/"+px+"/";
				pm.CompileAandSTables(pm.dataFolder,px,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/"+px+".txt",null);
			}*/
			
			// tumours
			//pm.CompileAandSTables(pm.dataFolder,prefix,"C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/"+prefix+".txt",null);
			// normals
			//pm.CompileAandSTables(pm.dataFolder,prefix+"n","C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga_s.txt","C:/Datas/PanMethylome/sample_info/"+prefix+".txt",null);
			
			//pm.CompileAandSTables(pm.dataFolder,prefix,null,null,null);
			//System.exit(0);
			
			// GENOME VIEWER
			//pm.prepareDataForGenomeViewer(pm.dataFolder,"OVCA");
			//pm.prepareDataForGenomeViewer(pm.dataFolder,prefix,1,false);
			
			// FOR ENRICHMENT ANALYSIS
			/*for(String pfx: cancers){
				System.out.println("=======================================");
				System.out.println("=======     "+pfx+"        ===========");
				System.out.println("=======================================");
				//pm.makeRNKFiles("C:/Datas/PanMethylome/methylome/"+pfx+"/",pfx);
				if((new File("C:/Datas/PanMethylome/methylome/"+pfx+"/"+pfx+"n_S_annot.xls")).exists())
					pm.makeRNKFiles("C:/Datas/PanMethylome/methylome/"+pfx+"/",pfx+"n");
			}*/

			
			// FIND ASSOCIATIONS IN THE TABLE
			
			Vector<String> names = new Vector<String>(); int numOfComponents = 0;
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(pm.dataFolder+prefix+"_A_annot.xls", true, "\t");
			for(int i=0;i<vt.colCount;i++) if(vt.fieldNames[i].startsWith("IC")) numOfComponents++; 
			//for(int i=1;i<vt.colCount;i++) names.add(vt.fieldNames[i]);
			for(int i=1;i<=numOfComponents;i++) names.add("IC"+i);
			TableUtils.findAllNumericalColumns(vt);	
			VDatReadWrite.saveToVDatFile(vt, pm.dataFolder+prefix+"_A_annot.dat");
			TableUtils.printAssosiationTable(vt, pm.dataFolder+prefix+"_A_associations.txt", names,0f, 5, true);
			
			// MAKE CORRELATION GRAPH
			//pm.MakeCorrelationGraph(args[0]);
			//pm.MakeCorrelationGraph("C:/Datas/PanMethylome/analysis/correlationgraph/gene_based/");
			//pm.MakeCorrelationGraph("C:/Datas/MOSAIC/analysis/ica/ica_corrgraph/full_rescue/");
			//pm.MakeCorrelationGraph("C:/Datas/OvarianCancer/TCGA/analysis/corrgraph_TCGA_3levels/sample_based/");
			/*pm.MakeCorrelationGraph("C:/Datas/PanMethylome/analysis/correlationgraph/"); 
			Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML("C:/Datas/PanMethylome/analysis/correlationgraph/corr_graph.xgmml"));
			System.out.println(gr.Nodes.size()+" nodes, "+gr.Edges.size()+" edges.");
			Graph gr1 = RemoveReciprocalEdgesInCorrelationGraph(gr);
			System.out.println(gr1.Nodes.size()+" nodes, "+gr1.Edges.size()+" edges.");
			XGMML.saveToXGMML(gr1, "C:/Datas/PanMethylome/analysis/correlationgraph/correlation_graph_norecipedges.xgmml");
			*/
			
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(pm.dataFolder+prefix+".txt", true, "\t",true);
			System.out.println("It's read.");
			for(int i=1;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt, pm.dataFolder+prefix+".dat");*/
			
			//pm.PCAAnalysis(pm.dataFolder+prefix+".txt",50);
			
			/*float f[] = VSimpleFunctions.getBrokenStickDistribution(50);
			for(int i=0;i<f.length;i++)
				System.out.println(f[i]);*/
			
			//String fields[] = {"TargetID","INFINIUM_DESIGN_TYPE","CHR","MAPINFO","METHYL27_LOCI","UCSC_REFGENE_NAME","UCSC_REFGENE_ACCESSION","UCSC_REFGENE_GROUP","UCSC_CPG_ISLANDS_NAME","RELATION_TO_UCSC_CPG_ISLAND","ENHANCER"};
			//pm.ExtractAnnotationFile("C:/Datas/PanMethylome/chip_annotation/annotCpGs_tcga.txt",fields);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void ReformatMethylomeFile(Vector<String> fnins, String annotFile, String fnout, float thresholdForNApercentage, boolean firstIdColumn, float thresholdMean, float thresholdStd) throws Exception{
		Vector<LineNumberReader> lrs = new Vector<LineNumberReader>();
		for(String fnin: fnins){
			LineNumberReader lr = new LineNumberReader(new FileReader(fnin));
			lrs.add(lr);
		}
		LineNumberReader lrannot = new LineNumberReader(new FileReader(annotFile));
		String s = null;
		FileWriter fw = new FileWriter(fnout);
		FileWriter fw_num = new FileWriter(fnout.substring(0, fnout.length()-4)+"_numerical.txt");
		FileWriter fw_ids = new FileWriter(fnout.substring(0, fnout.length()-4)+"_ids.txt");
		FileWriter fw_samples = new FileWriter(fnout.substring(0, fnout.length()-4)+"_samples.txt");
		
		Vector<String> columns = new Vector<String>();
		String id = null;
		for(LineNumberReader lr: lrs){
			String firstLine = lr.readLine();
			StringTokenizer stf = new StringTokenizer(firstLine,"\t");
			if(firstIdColumn) id=stf.nextToken();
			while(stf.hasMoreTokens())
				columns.add(stf.nextToken());
		}
		lrannot.readLine();
		
		if(id!=null) fw.write(id+"\t");
		
		fw.write("PROBE\t");
		for(String col: columns) { 
			fw_samples.write(col+"\t");
			fw.write(col+"\t");
		}
		fw.write("\n");

		int k = 0;
		boolean quit = false;
		Date tm = new Date();
		
		while(!quit){
		
		int count = 0;
		int countNA = 0;
		Vector<String> v = new Vector<String>();
		Vector<Float> vf = new Vector<Float>();
		
		String annotString = lrannot.readLine();
		
		for(LineNumberReader lr: lrs){
			
		s = lr.readLine();
		
		
		if(s==null) { quit = true; break; }
		
			if(k==10000*(int)(0.0001f*k)){
				Date tm1 = new Date();
				long elapsed = tm1.getTime()-tm.getTime();
				int estimatedMinutes = (int)(((double)elapsed*(double)450000/(double)(k+1)-(double)elapsed)/(double)1000/(double)60f); 
				System.out.print(k+"("+elapsed+","+estimatedMinutes+")\t");
			}
			StringTokenizer st = new StringTokenizer(s,"\t");
			
			
			if(firstIdColumn){
				id = st.nextToken();
			}else{
				StringTokenizer st1 = new StringTokenizer(annotString,"\t");
				id = st1.nextToken();
			}
			
			while(st.hasMoreTokens()){
				String vs = st.nextToken();
				float f = 0f;
				if(vs.equals("NA")) countNA++; else{
					f = Float.parseFloat(vs)*10;
					DecimalFormat nf = new DecimalFormat("#.#");
					vs = nf.format(f);
				}
				v.add(vs);
				vf.add(f);
				count++;
			}
			}

			float f[] = new float[vf.size()];
			for(int i=0;i<vf.size();i++) f[i] = vf.get(i);
			float mn = VSimpleFunctions.calcMean(f);
			float st = VSimpleFunctions.calcStandardDeviation(f);
			
			if(mn>thresholdMean)
			if(st>thresholdStd)
			if((float)(countNA)/(float)(count)<=thresholdForNApercentage){
				if(id!=null){
					fw.write(id+"\t");
					fw_ids.write(id+"\n");
				}
				for(String ss: v)
					fw.write(ss+"\t");
				for(String ss: v)
					fw_num.write(ss+"\t");
				fw.write("\n");
				fw_num.write("\n");
			}
		if(quit) break;
		k++;
		}
		
		for(LineNumberReader lr: lrs) lr.close();
		lrannot.close();
		fw.close();
		fw_num.close();
		fw_ids.close();
		fw_samples.close();
	}
	
	public void PCAAnalysis(String fn, int ncomp) throws Exception{
		VDataSet vd = VDatReadWrite.LoadFileWithFirstIDColumn(fn,true);
		vd.calcStatistics();
		PCAMethod pca = new PCAMethod();
		pca.setDataSet(vd);
		pca.calcBasis(ncomp);
		double d[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
		for(int i=0;i<ncomp;i++) System.out.println(d[i]);
		for(int i=0;i<ncomp;i++){
			System.out.print("PC"+(i+1)+":\t");
			for(int j=0;j<vd.coordCount;j++) System.out.print(pca.linBasis.basis[i][j]+"\t");
			System.out.println();
		}
	}
	
	public static void ExtractAnnotationFile(String fn, String fields[]){
		System.out.println("Loading");
		VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t", true);
		Vector<String> columns = new Vector<String>();
		for(String s: fields) columns.add(s);
		VDataTable annots = VSimpleProcedures.SelectColumns(annot, columns);
		String fn1 = fn.substring(0, fn.length()-4)+"_s.txt";
		for(int i=0;i<annots.rowCount;i++){
			String group = annots.stringTable[i][annots.fieldNumByName("UCSC_REFGENE_GROUP")];
			StringTokenizer st = new StringTokenizer(group,";");
			Vector<String> terms = new Vector<String>();
			while(st.hasMoreTokens()) { String s = st.nextToken(); if(!terms.contains(s)) terms.add(s); }
			Collections.sort(terms);
			String groupn = "";
			for(String ss: terms) groupn+=ss+";";
			if(groupn.endsWith(";")) groupn = groupn.substring(0, groupn.length()-1);
			if(groupn.equals("")) groupn = "_";
			annots.stringTable[i][annots.fieldNumByName("UCSC_REFGENE_GROUP")] = groupn;
		}
		VDatReadWrite.saveToSimpleDatFile(annots, fn1);
	}
	
	public static void CompileAandSTables(String folder, String prefix, String idAnnotFile, String sampleAnnotFile, String sampleClusterFile) throws Exception{
		
		// First, let us determine the right file, with _numerical.txt_XX.num ending, we want to know XX
		File lf[] = new File(folder).listFiles();
		String ending = "_numerical.txt.num";
		for(File f:lf){
			String fn = f.getName(); 
			if(fn.contains("_numerical.txt"))if(fn.endsWith(".num")) { int k=fn.indexOf("_numerical.txt"); ending = fn.substring(k, fn.length()); }
		}
		System.out.println("Ends with "+ending);
		
		if(new File(folder+"A_"+prefix+ending).exists()){
		
		VDataTable vtA = VDatReadWrite.LoadFromSimpleDatFile(folder+"A_"+prefix+ending, false, "\t");
		System.out.println(vtA.rowCount+" samples in "+folder+"A_"+prefix+ending);
		VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(folder+"S_"+prefix+ending, false, "\t");
		
		
		VDataTable idAnnotations = null;
		if(idAnnotFile!=null){
			idAnnotations = VDatReadWrite.LoadFromSimpleDatFile(idAnnotFile, true, "\t");
			idAnnotations.makePrimaryHash(idAnnotations.fieldNames[0]);
		}
		
		VDataTable sampleAnnotations = null;
		if(sampleAnnotFile!=null){
			sampleAnnotations = VDatReadWrite.LoadFromSimpleDatFile(sampleAnnotFile, true, "\t");
			sampleAnnotations.makePrimaryHash(sampleAnnotations.fieldNames[0]);
		}
		
		//Set<String> keys = sampleAnnotations.tableHashPrimary.keySet();
		//for(String sss: keys) System.out.println("Key '"+sss+"'");

		VDataTable sampleClusters = null;
		if(sampleClusterFile!=null){
			sampleClusters = VDatReadWrite.LoadFromSimpleDatFile(sampleClusterFile, true, "\t");
			sampleClusters.makePrimaryHash(sampleClusters.fieldNames[0]);
		}
			
		String colString = Utils.loadString(folder+prefix+"_samples.txt");
		Vector<String> samples = new Vector<String>();
		StringTokenizer st = new StringTokenizer(colString,"\t");
		while(st.hasMoreTokens()){ 
			String ss = st.nextToken(); 
			if(!ss.trim().equals("")) samples.add(ss); 
		}
		System.out.println(samples.size()+" samples in "+folder+prefix+"_samples.txt");
		Vector<String> ids = Utils.loadStringListFromFile(folder+prefix+"_ids.txt");
		
		/*
		 * Normalize table of component projections, heavy tail always positive
		 */
		Vector<Boolean> swapped = new Vector<Boolean>();
		Vector<MetaGene> sMetagenes = new Vector<MetaGene>();
		for(int i=0;i<vtS.colCount;i++){
			String fn = vtS.fieldNames[i];
			Boolean swpd = false;
			System.out.println("Field "+fn);
			MetaGene mg = new MetaGene();
			for(int j=0;j<vtS.rowCount;j++){
				//if(j==100000*(int)(0.00001f*j))
				//	System.out.print(j+"\t");
				mg.add(ids.get(j), Float.parseFloat(vtS.stringTable[j][i]));
			}
			//System.out.println();
			//float positiveStd = mg.sidedStandardDeviation(+1);
			//float negativeStd = mg.sidedStandardDeviation(-1);
			float positiveSide = mg.sumOfWeightsAboveThreshold(+1,3f);
			float negativeSide = mg.sumOfWeightsAboveThreshold(-1,3f);
			if(positiveSide+negativeSide<1e-6){
				positiveSide = mg.sumOfWeightsAboveThreshold(+1,2f);
				negativeSide = mg.sumOfWeightsAboveThreshold(-1,2f);
				if(positiveSide+negativeSide<1e-6){
					positiveSide = mg.sumOfWeightsAboveThreshold(+1,1.5f);
					negativeSide = mg.sumOfWeightsAboveThreshold(-1,1.5f);
				}
			}
			
			System.out.println("positiveSide = "+positiveSide+", negativeSide = "+negativeSide);
			if(negativeSide>positiveSide){
				//System.out.println("TSPAN6 weight = "+mg.getWeight("TSPAN6"));
				mg.invertSignsOfWeights();
				//System.out.println("Flipped, TSPAN6 weight = "+mg.getWeight("TSPAN6"));
				swpd = true;
			}
			sMetagenes.add(mg);
			swapped.add(swpd);
		}

		System.out.println("Saving to "+folder+prefix+"_A.xls");
		FileWriter fwA = new FileWriter(folder+prefix+"_A.xls");
		FileWriter fwAa = new FileWriter(folder+prefix+"_A_annot.xls");
		fwA.write("SAMPLE\t"); for(int i=1;i<=vtA.colCount;i++) fwA.write("IC"+i+"\t"); fwA.write("\n");
		if(sampleAnnotFile!=null){
			fwAa.write("SAMPLE\t"); for(int i=1;i<=vtA.colCount;i++) fwAa.write("IC"+i+"\t");
			for(int i=1;i<sampleAnnotations.colCount;i++) fwAa.write(sampleAnnotations.fieldNames[i]+"\t");
		}
		if(sampleClusterFile!=null){
			for(int i=1;i<sampleClusters.colCount;i++) fwAa.write(sampleClusters.fieldNames[i]+"\t");
		}
		fwAa.write("\n");
		for(int i=0;i<vtA.rowCount;i++){
			String sample = samples.get(i);
			fwA.write(sample+"\t");
			if(sampleAnnotFile!=null){
				fwAa.write(sample+"\t");
			}
			for(int j=0;j<vtA.colCount;j++){
				float f = Float.parseFloat(vtA.stringTable[i][j]);
				if(swapped.get(j)) f = -f;
				DecimalFormat nf = new DecimalFormat("#.##");
				String vs = nf.format(f);
				fwA.write(vs+"\t");
				if(sampleAnnotFile!=null){
					fwAa.write(vs+"\t");
				}
			}
			if(sampleAnnotations!=null){
				if(sampleAnnotations.tableHashPrimary.get(sample)!=null){
					int k = sampleAnnotations.tableHashPrimary.get(sample).get(0);
					for(int l=1;l<sampleAnnotations.colCount;l++) fwAa.write(sampleAnnotations.stringTable[k][l]+"\t");
				}else{
					//System.out.println("Sample not found: '"+sample+"'");
					for(int l=1;l<sampleAnnotations.colCount;l++) fwAa.write("_\t");
				}
			}
			if(sampleClusters!=null){
				if(sampleClusters.tableHashPrimary.get(sample)!=null){
					int k = sampleClusters.tableHashPrimary.get(sample).get(0);
					for(int l=1;l<sampleClusters.colCount;l++) fwAa.write(sampleClusters.stringTable[k][l]+"\t");
				}else{
					for(int l=1;l<sampleClusters.colCount;l++) fwAa.write("_\t");
				}
			}			
			if(sampleAnnotations!=null) fwAa.write("\n");
			fwA.write("\n");
		}
		fwA.close();
		fwAa.close();
		
		System.out.println("Saving to "+folder+prefix+"_S.xls");
		FileWriter fwS = new FileWriter(folder+prefix+"_S.xls");
		FileWriter fwSa = new FileWriter(folder+prefix+"_S_annot.xls");
		fwS.write("PROBE\t"); for(int i=1;i<=vtS.colCount;i++) fwS.write("IC"+i+"\t"); fwS.write("\n");
		if(idAnnotations!=null){
			fwSa.write("PROBE\t"); for(int i=1;i<=vtS.colCount;i++) fwSa.write("IC"+i+"\t"); 
			for(int i=1;i<idAnnotations.colCount;i++) fwSa.write(idAnnotations.fieldNames[i]+"\t");
			fwSa.write("\n"); 
		}
		for(int i=0;i<vtS.rowCount;i++){
			String id = ids.get(i);
			if(i==100000*(int)(0.00001f*i))
				System.out.print(i+"\t");
			fwS.write(id+"\t");
			if(idAnnotations!=null){
				fwSa.write(ids.get(i)+"\t");
			}
			for(int j=0;j<vtS.colCount;j++){
				/*float f = Float.parseFloat(vtS.stringTable[i][j]);
				DecimalFormat nf = new DecimalFormat("#.###");
				String vs = nf.format(f);
				fwS.write(vs+"\t");
				if(idAnnotations!=null) fwSa.write(vs+"\t");*/
				float f = sMetagenes.get(j).getWeight(id);
				DecimalFormat nf = new DecimalFormat("#.###");
				String vs = nf.format(f);
				fwS.write(vs+"\t");
				if(idAnnotations!=null) fwSa.write(vs+"\t");				
			}
			if(idAnnotations!=null){
				if(idAnnotations.tableHashPrimary.get(id)!=null){
					int k = idAnnotations.tableHashPrimary.get(id).get(0);
					for(int l=1;l<idAnnotations.colCount;l++) fwSa.write(idAnnotations.stringTable[k][l]+"\t");
				}else{
					for(int l=1;l<idAnnotations.colCount;l++) fwSa.write("_\t");
				}
			}
			if(idAnnotations!=null) fwSa.write("\n");
			fwS.write("\n");
		}
		fwS.close();
		fwSa.close();
		System.out.println();

		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+"_S_annot.xls", true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		VDatReadWrite.saveToVDatFile(vt, folder+prefix+"_S_annot.dat");
		vt = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+"_A_annot.xls", true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		VDatReadWrite.saveToVDatFile(vt, folder+prefix+"_A_annot.dat");
		
		}else{
			System.out.println("Did not find "+folder+"A_"+prefix+ending);
		}
	}

	
	
	public static String processTCGASampleNames(String fn, int numberOfTokens, boolean replaceTireToDot, String annotationFileName) throws Exception{
		String s = Utils.loadString(fn);
		FileWriter fw = new FileWriter(fn.substring(0, fn.length()-4)+".old"); fw.write(s+"\n"); fw.close();
		StringTokenizer st = new StringTokenizer(s,"\t");
		String news = "";
		Vector<String> names = new Vector<String>();
		while(st.hasMoreTokens()){
			String ss = st.nextToken().toLowerCase().trim();
			//System.out.println(ss);
			String newname = "";
			if(!ss.equals("")){	
			StringTokenizer st1 = new StringTokenizer(ss,"-");
			for(int i=0;i<numberOfTokens;i++){
				String s1 = st1.nextToken();;
				newname+=s1+"-";
			}
			if(newname.endsWith("-")) newname = newname.substring(0, newname.length()-1);
			news+=newname+"\t";
			if(replaceTireToDot)
				news = newname.replace('-', '.');
			}
			names.add(newname);
		}
		fw = new FileWriter(fn); fw.write(news+"\n"); fw.close();
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(annotationFileName, true, "\t");
		Vector<String> found = new Vector<String>();
		for(String name: names){
			for(int i=0;i<vt.rowCount;i++) if(vt.stringTable[i][0].equals(name)) if(!found.contains(name)) found.add(name);
		}
		System.out.println(found.size()+" names found in annotations in "+fn);
		return news;
	}
	
	public static void prepareDataForGenomeViewer(String folder, String prefix, float thresh, boolean showRealSpan) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+"_S_annot.xls", true, "\t");
		(new File(folder+"genome_view")).mkdir();
		FileWriter fwcomp = new FileWriter(folder+"genome_view/comps.txt");
		FileWriter fw_chr = new FileWriter(folder+"genome_view/chrs.txt");
		FileWriter fw_pos = new FileWriter(folder+"genome_view/positions.txt");		
		FileWriter fw_islands = new FileWriter(folder+"genome_view/islands.txt");
		Vector<String> icfields = new Vector<String>();
		double positions[] = new double[vt.rowCount];
		
		for(int i=0;i<vt.rowCount;i++){
			String chr = vt.stringTable[i][vt.fieldNumByName("CHR")];
			if(chr.equals("X")) chr = "23";
			if(chr.equals("Y")) chr = "24";
			String pos = vt.stringTable[i][vt.fieldNumByName("MAPINFO")];
			double posf = 0;
			if(!chr.equals("_"))if(!pos.equals("_"))
				if(!chr.equals(""))if(!pos.equals("")){
					//posf = Float.parseFloat(chr)+Float.parseFloat(pos)/1000000000;
					posf = Double.parseDouble(chr)*1000000000+Double.parseDouble(pos);
				}
			positions[i] = posf;
		}
		int inds[] = Algorithms.SortMass(positions);
		
		int ncomps = 0;
		for(int j=0;j<vt.colCount;j++) if(vt.fieldNames[j].startsWith("IC")) ncomps++;
		
		for(int i=0;i<inds.length;i++){
			double posf = positions[inds[i]];
			if(posf>0.5){
			for(int j=0;j<vt.colCount;j++) if(vt.fieldNames[j].startsWith("IC")) fwcomp.write(vt.stringTable[inds[i]][j]+"\t"); fwcomp.write("\n");
			String chr = vt.stringTable[inds[i]][vt.fieldNumByName("CHR")];
			if(chr.equals("X")) chr = "23";
			if(chr.equals("Y")) chr = "24";
			String pos = vt.stringTable[inds[i]][vt.fieldNumByName("MAPINFO")];
			String island = vt.stringTable[inds[i]][vt.fieldNumByName("RELATION_TO_UCSC_CPG_ISLAND")];
			if(island.equals("")) island = "_";
			fw_chr.write(chr+"\n");
			fw_pos.write(pos+"\n");
			fw_islands.write(island+"\n");
			//System.out.println(posf+"\t"+pos);
			}
		}
		fwcomp.close();
		fw_chr.close();
		fw_islands.close();
		fw_pos.close();
		
		// Generate WIGs
		for(int i=0;i<ncomps;i++){
			String s = ""+(i+1); if(s.length()==1) s="0"+s;
			System.out.println("WIG "+s);
			FileWriter fwwig = new FileWriter(folder+"genome_view/"+prefix+"_IC"+s+".wig");
			fwwig.write("track type=wiggle_0  name="+prefix+"_IC"+s+"\n");
			String currentChr = "";
			for(int j=0;j<inds.length;j++){
				String chr = vt.stringTable[inds[j]][vt.fieldNumByName("CHR")];
				String pos = vt.stringTable[inds[j]][vt.fieldNumByName("MAPINFO")];
				String span = "500";
				if(showRealSpan){
					String name = vt.stringTable[inds[j]][vt.fieldNumByName("UCSC_CPG_ISLANDS_NAME")];
					if(name.startsWith("chr")){
						StringTokenizer st = new StringTokenizer(name,":"); st.nextToken();
						String interval = st.nextToken();
						st = new StringTokenizer(interval,"-");
						long pos1 = Long.parseLong(st.nextToken());
						long pos2 = Long.parseLong(st.nextToken());
						span = ""+(pos2-pos1);
					}
				}
				if(!pos.equals("NA")){
				float val = Float.parseFloat(vt.stringTable[inds[j]][vt.fieldNumByName("IC"+(i+1))]);
				if(chr.equals("23")) chr="X";
				if(chr.equals("24")) chr="Y";
				if(!showRealSpan){
				if(!chr.equals(currentChr)){
					fwwig.write("variableStep\tchrom=chr"+chr+"\tspan=500\n");
					//System.out.println("variableStep\tchrom=chr"+chr+"\tspan=500\n");
					currentChr = chr;
				}
				if(Math.abs(val)>thresh)
					fwwig.write(pos+"\t"+val+"\n");
				}else{
					if(Math.abs(val)>thresh){
						fwwig.write("variableStep\tchrom=chr"+chr+"\tspan="+span+"\n");
						fwwig.write(pos+"\t"+val+"\n");
					}
				}
				}
			}
			fwwig.close();
		}
		
	}
	
	public static void makeRNKFiles(String folder, String prefix) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+"_S_annot.xls", true, "\t");
		Vector<HashMap<String, Float>> ics = new Vector<HashMap<String, Float>>();
		for(int i=0;i<vt.colCount;i++){
			if(vt.fieldNames[i].startsWith("IC")){
				HashMap<String, Float> rnk = new HashMap<String, Float>();
				ics.add(rnk);
			}
		}
		for(int j=0;j<vt.rowCount;j++){
			if(j==10000*(int)(j*0.0001f))
				System.out.println(j);
			String gn = vt.stringTable[j][vt.fieldNumByName("UCSC_REFGENE_NAME")];
			if(!gn.trim().equals(""))if(!gn.trim().equals("_")){
			StringTokenizer st = new StringTokenizer(gn,";");
			Vector<String> names = new Vector<String>();
			while(st.hasMoreTokens()){
				String s = st.nextToken();
				if(!names.contains(s)) names.add(s);
			}
			int count=0;
			for(int i=0;i<vt.colCount;i++)if(vt.fieldNames[i].startsWith("IC")){
				HashMap<String, Float> rnk = ics.get(count); count++;
				Float f = Float.parseFloat(vt.stringTable[j][i]);
				for(int k=0;k<names.size();k++){
					Float reff = rnk.get(names.get(k));
					if(reff==null){
						rnk.put(names.get(k), f);
					}else{
						if(Math.abs(f)>Math.abs(reff))
							rnk.put(names.get(k), f);
					}
				}
			}
			}
		}
		
		(new File(folder+"gsea/")).mkdir();
		
		for(int i=0;i<ics.size();i++){
			HashMap<String, Float> rnk = ics.get(i);
			String s = ""+(i+1);
			if(s.length()==1) s = "0"+s;
			FileWriter fw = new FileWriter(folder+"gsea/"+prefix+"_IC"+s+".rnk");
			Vector<String> names = new Vector<String>();
			Vector<Float> values = new Vector<Float>();
			Iterator<String> it = rnk.keySet().iterator();
			while(it.hasNext()) { String key = it.next(); names.add(key); values.add(rnk.get(key)); }
			float vals[] = new float[values.size()]; for(int k=0;k<values.size();k++) vals[k] = values.get(k);
			int inds[] = Algorithms.SortMass(vals);
			for(int k=0;k<inds.length;k++){
				fw.write(names.get(inds[k])+"\t"+vals[inds[k]]+"\n");
			}
			fw.close();
		}
		
		
		FileWriter fw_genetable = new FileWriter(folder+prefix+"_ICgenes"+".xls");
		Vector<String> allnames = new Vector<String>();
		Iterator<String> it = ics.get(0).keySet().iterator();
		while(it.hasNext()) allnames.add(it.next());
		Collections.sort(allnames);
		fw_genetable.write("GENE\t");
		for(int i=0;i<ics.size();i++) fw_genetable.write("IC"+(i+1)+"\t"); fw_genetable.write("\n");
		for(String name: allnames){
			fw_genetable.write(name+"\t");
			for(int i=0;i<ics.size();i++){
				HashMap<String, Float> rnk = ics.get(i);
				float val = rnk.get(name);
				fw_genetable.write(""+val+"\t");
			}
			fw_genetable.write("\n");
		}
		fw_genetable.close();
		
		VDataTable vtg = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+"_ICgenes"+".xls", true, "\t");
		TableUtils.findAllNumericalColumns(vtg);
		VDatReadWrite.saveToVDatFile(vtg, folder+prefix+"_ICgenes"+".dat");
		
	}
	
	
	
	  
	  public static void transformTableToICA(String fname) throws Exception{
		  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fname, true, "\t");
		  String fn = fname.substring(0, fname.length()-4);
		  FileWriter fw = new FileWriter(fn+"_numerical.txt");
		  for(int i=0;i<vt.rowCount;i++){
			  for(int j=1;j<vt.colCount;j++)
				  fw.write(vt.stringTable[i][j]+"\t");
			  fw.write("\n");
		  }
		  fw.close();
		  FileWriter fws = new FileWriter(fn+"_samples.txt");
		  for(int j=1;j<vt.colCount;j++)
			  fws.write(vt.fieldNames[j]+"\t");
		  fws.close();
		  FileWriter fwids = new FileWriter(fn+"_ids.txt");
		  for(int i=0;i<vt.rowCount;i++){
			  fwids.write(vt.stringTable[i][0]+"\n");
		  }
		  fwids.close();
	  }
	  
	  public static void copyFilesToGoogleDrive(String folder1, String folder2) throws Exception{
		  Vector<String> cancersv = new Vector<String>();
		  for(String s: cancers) cancersv.add(s);
		  File files[] = (new File(folder1)).listFiles();
		  for(int k=0;k<2;k++){
		  String s = k==0?"":"n";
		  for(File f:files){
			  if(f.isDirectory())if(cancersv.contains(f.getName())){
				  (new File(folder2+f.getName())).mkdir();
				  if(new File(f.getAbsolutePath()+"/"+f.getName()+s+"_A.xls").exists()){
					  FileUtils.copyFile(new File(f.getAbsolutePath()+"/"+f.getName()+s+"_A.xls"), new File(folder2+f.getName()+"/"+f.getName()+s+"_A.xls"));
					  FileUtils.copyFile(new File(f.getAbsolutePath()+"/"+f.getName()+s+"_S.xls"), new File(folder2+f.getName()+"/"+f.getName()+s+"_S.xls"));
					  FileUtils.copyFile(new File(f.getAbsolutePath()+"/"+f.getName()+s+"_A_annot.xls"), new File(folder2+f.getName()+"/"+f.getName()+s+"_A_annot.xls"));
					  FileUtils.copyFile(new File(f.getAbsolutePath()+"/"+f.getName()+s+"_S_annot.xls"), new File(folder2+f.getName()+"/"+f.getName()+s+"_S_annot.xls"));
					  FileUtils.copyFile(new File(f.getAbsolutePath()+"/"+f.getName()+s+"_ICgenes.xls"), new File(folder2+"_gene_projections/"+f.getName()+s+"_S.xls"));
				  }
			  }
		  }
		  }
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
				   //String fn = datafolder+dataset+"/"+dataset+"_S.xls";
				   String fn = datafolder+"/"+dataset+"_S.xls";
				   VDataTable vtm = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
				   MetaGene mg = new MetaGene();
				   mg.name = s;
				   for(int j=0;j<vtm.rowCount;j++)
					   if(!vtm.stringTable[j][vtm.fieldNumByName(column)].equals("N/A"))
					   mg.add(vtm.stringTable[j][0], Float.parseFloat(vtm.stringTable[j][vtm.fieldNumByName(column)]));
				   metagenes.add(mg);
			   }
			   // Determine the "core" component
			   int coreComp = -1;
			   for(int i=0;i<components.size();i++) if(components.get(i).contains("BLCAC")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("BLCA")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("BRCA")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("KIRC")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("LUAD")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("LUSC")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("PBCA")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("THCA")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("USC")) coreComp = i;
			   if(coreComp<0) for(int i=0;i<components.size();i++) if(components.get(i).contains("fullc")) coreComp = i;
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
	  
		public static void makeGSEABatchFile(String folder, String gmtFile, String outfolder) throws Exception{
			File dir = new File(folder);
			File files[] = dir.listFiles();
			FileWriter fw = new FileWriter(folder+"GSEA.bat"); 
			FileWriter fwsh = new FileWriter(folder+"GSEA.sh");
			for(File f: files){
				String fileName = f.getAbsolutePath();
				if(fileName.endsWith(".rnk")){
					String prefix = f.getName();
					String fname = f.getName();
					prefix = prefix.substring(0, prefix.length()-4);
					String command = "java -cp .;gsea2-2.0.14.jar -Xmx3000m xtools.gsea.GseaPreranked -gmx "+gmtFile+" -collapse false -mode Max_probe -norm meandiv -nperm 1000 -rnk "+fileName+" -scoring_scheme classic -rpt_label "+prefix+" -include_only_symbols true -make_sets true -plot_top_x 200 -rnd_seed timestamp -set_max 200 -set_min 8 -zip_report false -out "+outfolder+prefix+" -gui false";
					String commandsh = "java -cp .:gsea2-2.0.14.jar -Xmx3000m xtools.gsea.GseaPreranked -gmx "+gmtFile+" -collapse false -mode Max_probe -norm meandiv -nperm 1000 -rnk "+fname+" -scoring_scheme classic -rpt_label "+prefix+" -include_only_symbols true -make_sets true -plot_top_x 200 -rnd_seed timestamp -set_max 200 -set_min 8 -zip_report false -out "+outfolder+prefix+" -gui false";
					fw.write(command+"\n");
					fwsh.write(command+"\n");
				}
			}
			fw.close();
			fwsh.close();
		}
		
		public static void annotateCellDesignerFile(String fn) throws Exception{
	  		SbmlDocument cd = CellDesigner.loadCellDesigner(fn);
	  		
			ModifyCellDesignerNotes mn = new ModifyCellDesignerNotes();
			mn.generateReadableNamesForReactionsAndSpecies = false;
			mn.allannotations = true;
			mn.formatAnnotation = false;
			mn.sbmlDoc = cd;
			mn.comments = mn.exportCellDesignerNotes();
			mn.splitCommentsIntoNotes();
			
			for(int i=0;i<mn.keys.size();i++)if(mn.keys.get(i).startsWith("pr_")){
				String key = mn.keys.get(i);
				key = key.substring(3, key.length());
				System.out.println(key);
				String comm = mn.noteAdds.get(i);
				if(!comm.contains("HUGO:")){
					comm+="HUGO:"+key;
					mn.noteAdds.set(i, comm);
				}
			}
				
			mn.comments = mn.mergeNotesIntoComments();
			mn.ModifyCellDesignerNotes();
			String fnout = fn.substring(0, fn.length()-4)+"_annot.xml";
			CellDesigner.saveCellDesigner(cd, fnout);
		}

	

}
