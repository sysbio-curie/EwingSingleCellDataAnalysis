package getools.scripts;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import fr.curie.BiNoM.pathways.wrappers.XGMML;
import vdaoengine.TableUtils;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;

public class ZebraFishAnalysis {

	VDataTable table = null;
	VDataSet data = null;
	String prefix = "c:/datas/ivashkine/zebrafish/";
	String tableFileName = "dre2";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			ZebraFishAnalysis zfa = new ZebraFishAnalysis();
			
			// Humanize using ENSEMBLE
			VDataTable vtHuman = VDatReadWrite.LoadFromSimpleDatFile("C:/_DatasArchive/Ivashkine/ZebraFish/human_orthologues/human_orthologues_zfish.txt", true, "\t");
			vtHuman.makePrimaryHash("Associated Gene Name");
			VDataTable vtZFIN = VDatReadWrite.LoadFromSimpleDatFile("C:/_DatasArchive/Ivashkine/ZebraFish/human_orthologues/mart_export.txt", true, "\t");
			vtZFIN.makePrimaryHash("Ensembl Gene ID");
			
			Vector<String> htGenes = Utils.loadStringListFromFile("C:/_DatasArchive/Ivashkine/ZebraFish/human_orthologues/all_genes.txt");
			
			//System.out.println("ZFIN\tHUMAN_FIRST\tHUMAN_ALL");
			System.out.println("ZFIN\tHUMAN_FIRST");
			for(String name: htGenes){
				Vector<Integer> ids_n = vtHuman.tableHashPrimary.get(name);
				String human_first = "_";
				String human_all = "_";
				
				Vector<String> humanNames = new Vector<String>();
				
				if(ids_n!=null)
				for(int i=0;i<ids_n.size();i++){
					String ensembleid = vtHuman.stringTable[ids_n.get(i)][vtHuman.fieldNumByName("Ensembl Gene ID")];
					Vector<Integer> rn = vtZFIN.tableHashPrimary.get(ensembleid);
					for(int k=0;k<rn.size();k++){
						String humanName = vtZFIN.stringTable[rn.get(k)][vtZFIN.fieldNumByName("Human associated gene name")];
						if(!humanNames.contains(humanName))
							humanNames.add(humanName);
					}
				}
				if(humanNames.size()>0){
					human_first = humanNames.get(0);
					human_all = ""; for(int k=0;k<humanNames.size();k++) human_all+=humanNames.get(k)+";"; human_all = human_all.substring(0, human_all.length()-1);
					System.out.println(name+"\t"+human_first);
				}
				//System.out.println(name+"\t"+human_first+"\t"+human_all);
			}
			System.exit(0);
			
			
			// Good GO database
			/*LineNumberReader lr = new LineNumberReader(new FileReader("C:/_DatasArchive/Ivashkine/ZebraFish/GO/go.owl"));
			FileWriter fw1 = new FileWriter("C:/_DatasArchive/Ivashkine/ZebraFish/GO/go_names.txt");
			String s = null;
			String currentGO = null;
			while((s=lr.readLine())!=null){
				s = s.trim();
				
				if(s.startsWith("<!-- "))if(s.contains("GO_")){
					//String parts[] = s.split(" /");
					s = s.replaceAll("<!-- http://purl.obolibrary.org/obo/","");
					s = s.replaceAll(" -->","");
					currentGO = s.replace("_", ":");
				}
				if(s.startsWith("<rdfs:label ")){
					s = s.replaceAll("<rdfs:label rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\">", "");
					s = s.replaceAll("</rdfs:label>", "");
					String label = s;
					fw1.write(currentGO+"\t"+label+"\n");
				}
			}
			fw1.close();
			System.exit(0);*/
			
			// Reformat the GO table, count frequencies of GOs in a list
			/* Vector<String> geneList = Utils.loadStringListFromFile("C:/_DatasArchive/Ivashkine/ZebraFish/final_work/top_HT_genes_1");
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/_DatasArchive/Ivashkine/ZebraFish/GO/goa_zebrafish.gaf", true, "\t");
			//HashMap<String,Vector<String>> gos = new HashMap<String,Vector<String>>();
			vt.makePrimaryHash("SYMBOL");
			Set<String> names = vt.tableHashPrimary.keySet();
			FileWriter fw = new FileWriter("C:/_DatasArchive/Ivashkine/ZebraFish/GO/zebrafish_gos.txt");

			for(String name: names){
				Vector<Integer> rows = vt.tableHashPrimary.get(name);
				fw.write(name+"\t");
				for(Integer rown: rows)
					fw.write(vt.stringTable[rown][vt.fieldNumByName("GO")]+" // ");
					fw.write("\n");
			}
			fw.close();
			
			HashMap<String,Vector<String>> GOcounts = new HashMap<String,Vector<String>>();
			Vector<String> notFound = new Vector<String>();
			for(String name: geneList){
				Vector<Integer> rows = vt.tableHashPrimary.get(name);
				if(rows!=null){
				for(Integer rown: rows){
					String go = vt.stringTable[rown][vt.fieldNumByName("GO")];
					Vector<String> count = GOcounts.get(go);
					if(count==null){
						count = new Vector<String>();
					}
					if(!count.contains(name))
						count.add(name);
					GOcounts.put(go, count);
				}
				}else{
					notFound.add(name);
				}
			}
			Set<String> gos = GOcounts.keySet();
			for(String go: gos){
				Vector<String> nms = GOcounts.get(go);
				String namess = "";
				for(int k=0;k<nms.size();k++)
					namess+=nms.get(k)+",";
				if(namess.length()>0) namess = namess.substring(0, namess.length()-1);
				System.out.println(go+"\t"+nms.size()+"\t"+namess);
			}
			System.out.println(notFound.size()+" genes were not found");
			*/
			
			//Convert to KEGG visualization format
			/*Vector<String> scores = Utils.loadStringListFromFile("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/DRE_HT_DAY_global_scoring_HUMAN.rnk");
			for(String s: scores){
				StringTokenizer st = new StringTokenizer(s,"\t");
				String gene = st.nextToken();
				String score = st.nextToken();
				float f = -Float.parseFloat(score);
				if(f>=2f) f=2f; 
				if(f<=-2f) f=-2f; 
				f = 1+(f+2f)/4f*99f;
				int num = (int)(f+0.5);
				if(num<30||num>70)
				System.out.println(gene+","+num);
			}*/
			
			// Degree distribution in a network and subnetwork
			/*Vector<String> nodeList = Utils.loadStringListFromFile("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/DRE_HT_DAY_400posneg_HUMAN.txt");
			System.out.println("Loading...");
			fr.curie.BiNoM.pathways.analysis.structure.Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/network_analysis/Reactome_functional_network.xgmml"));
			System.out.println("Loaded...");
			gr.calcNodesInOut();
			for(fr.curie.BiNoM.pathways.analysis.structure.Node n: gr.Nodes){
				int found = nodeList.contains(n.Id)?1:0;
				System.out.println(n.Id+"\t"+(n.incomingEdges.size()+n.outcomingEdges.size())+"\t"+found);
			}*/
			
			
			// Eliminate repeating names in a scoring file
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/DRE_HT_DAY_global_scoring_HUMAN.txt", true, "\t");
			vt.makePrimaryHash("GENE_HUMAN");
			System.out.println("GENE_HUMAN\tSCORE");
			Set<String> keys = vt.tableHashPrimary.keySet();
			for(String gene: keys){
				Vector<Integer> rows = vt.tableHashPrimary.get(gene);
				float maxabsvalue = -1f;
				int k1 = -1;
				for(Integer k: rows){
					float x = Float.parseFloat(vt.stringTable[k][vt.fieldNumByName("SCORE")]);
					if(Math.abs(x)>maxabsvalue){
						maxabsvalue = x;
						k1 = k;
					}
				}
				System.out.println(gene+"\t"+vt.stringTable[k1][vt.fieldNumByName("SCORE")]);
			}*/
			
			// Process HomoloGene
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/REACTOME/ZebraFish/Homologene.txt", true, "\t");
			vt.makePrimaryHash("HID");
			System.out.println("ZebraFish\tHUMAN");
			for(int i=0;i<vt.rowCount;i++){
				String HID = vt.stringTable[i][vt.fieldNumByName("HID")];
				String TID = vt.stringTable[i][vt.fieldNumByName("TID")];
				String GeneSymbol = vt.stringTable[i][vt.fieldNumByName("GeneSymbol")];
				if(TID.equals("7955")){
					Vector<Integer> hs = vt.tableHashPrimary.get(HID);
					for(int j=0;j<hs.size();j++){
						int k = hs.get(j);
						String hID = vt.stringTable[k][vt.fieldNumByName("HID")];
						String tID = vt.stringTable[k][vt.fieldNumByName("TID")];
						String geneSymbol = vt.stringTable[k][vt.fieldNumByName("GeneSymbol")];
						if(tID.equals("9606")){
							System.out.println(GeneSymbol.toUpperCase()+"\t"+geneSymbol.toUpperCase());
						}
					}
				}
			}*/
			
			// Convert rnk to upper case
			/*Vector<String> list = Utils.loadStringListFromFile("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/DRE_HT_DAY.rnk");
			FileWriter fw = new FileWriter("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/DRE_HT_DAY_maj.rnk");
			for(String s: list){
				StringTokenizer st = new StringTokenizer(s,"\t");
				fw.write(st.nextToken().toUpperCase()+"\t"+st.nextToken()+"\n");
			}
			fw.close();*/
			
			//zfa.table = VDatReadWrite.LoadFromSimpleDatFile(zfa.prefix+"dre2.txt", true, "\t");
			//VDatReadWrite.saveToVDatFile(zfa.table, zfa.prefix+"dre2.dat");
			
			//zfa.prepareTable();
			
			//zfa.table = VDatReadWrite.LoadFromVDatFile(zfa.prefix+zfa.tableFileName+".dat");
			//zfa.extractGOs();
			
			/*zfa.table = VDatReadWrite.LoadFromVDatFile(zfa.prefix+zfa.tableFileName+".dat");
			VDatReadWrite.useQuotesEverywhere = true;
			VDatReadWrite.saveToVDatFile(zfa.table, zfa.prefix+zfa.tableFileName+"knq.dat");
			
			zfa.table.fieldTypes[zfa.table.fieldNumByName("AVG")] = zfa.table.STRING;
			zfa.table.fieldTypes[zfa.table.fieldNumByName("STDEV")] = zfa.table.STRING;			
			
			VDataTable zfaf= TableUtils.filterByVariation(zfa.table, 2000, false);
			VDatReadWrite.saveToVDatFile(zfaf, zfa.prefix+zfa.tableFileName+"knq_f2000.dat");
			VDataTable zfaft = zfaf.transposeTable("probeset_id");
			VDatReadWrite.saveToVDatFile(zfaft, zfa.prefix+zfa.tableFileName+"knq_f2000t.dat");*/			
			
			//zfa.extractFastaFile("C:/Datas/Ivashkine/ZebraFish/chips/ZebGene-1_0-st-v1.zv9.probe.fa","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_plus","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_plus.fa");
			//zfa.extractFastaFile("C:/Datas/Ivashkine/ZebraFish/chips/ZebGene-1_0-st-v1.zv9.probe.fa","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_minus","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_minus.fa");
			//zfa.extractFastaFile("C:/Datas/Ivashkine/ZebraFish/chips/ZebGene-1_0-st-v1.zv9.probe.fa","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_plus_known","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_plus_known.fa");
			//zfa.extractFastaFile("C:/Datas/Ivashkine/ZebraFish/chips/ZebGene-1_0-st-v1.zv9.probe.fa","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_minus","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_minus.fa");
			
			//zfa.prepareForGSEAAnalysis("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/dre3_full.txt");
			//zfa.prepareForGSEAAnalysis("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/test.txt");
			//zfa.makeGSEABatchFile("C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/", "C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/go_symbol.gmt", "C:/Datas/Ivashkine/ZebraFish/repeat_corrected/gsea/results/");
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Ivashkine/ZebraFish/genome/annot_refseq.txt", true, "\t");
			//for(int i=0;i<vt.colCount;i++)
			//	System.out.println(vt.fieldNames[i]);
			vt.makePrimaryHash("chrom");
			zfa.extractFastaFile("C:/Datas/Ivashkine/ZebraFish/chips/ZebGene-1_0-st-v1.zv9.probe.fa","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/all_probes","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/all_probes.fa",vt);
			//zfa.extractFastaFile("C:/Datas/Ivashkine/ZebraFish/chips/ZebGene-1_0-st-v1.zv9.probe.fa","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_plus","C:/Datas/Ivashkine/ZebraFish/repeat_corrected/HT_CTR_plus_.fa",vt);
			*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadData(){
		
	}
	
	public void extractGOs() throws Exception{
		HashMap<String, Vector<String>> gos_symbol = new HashMap<String, Vector<String>>();
		HashMap<String, Vector<String>> gos_probeset = new HashMap<String, Vector<String>>();		
		for(int i=0; i<table.rowCount; i++){
			String biological_process = table.stringTable[i][table.fieldNumByName("biological_process")];
			String cellular_component = table.stringTable[i][table.fieldNumByName("cellular_component")];
			String molecular_function = table.stringTable[i][table.fieldNumByName("molecular_function")];
			String symbol = table.stringTable[i][table.fieldNumByName("symbol")];
			String entrez_gene_id = table.stringTable[i][table.fieldNumByName("entrez_gene_id")];
			String probeset_id = table.stringTable[i][table.fieldNumByName("probeset_id")];			
			addToHashMap(gos_symbol,extractGOTerms(biological_process),symbol);
			addToHashMap(gos_probeset,extractGOTerms(biological_process),probeset_id);
			addToHashMap(gos_symbol,extractGOTerms(cellular_component),symbol);
			addToHashMap(gos_probeset,extractGOTerms(cellular_component),probeset_id);
			addToHashMap(gos_symbol,extractGOTerms(molecular_function),symbol);	
			addToHashMap(gos_probeset,extractGOTerms(molecular_function),probeset_id);	
		}
		FileWriter fw = new FileWriter(this.prefix+"go_symbol.gmt");
		Set<String> gos = gos_symbol.keySet();
		for(String go: gos){
			Vector<String> v = gos_symbol.get(go);
			fw.write(go+"\tna\t");
			for(String ss: v) fw.write(ss+"\t");
			fw.write("\n");
		}
		fw.close();
		fw = new FileWriter(this.prefix+"go_probeset.gmt");
		gos = gos_probeset.keySet();
		for(String go: gos){
			Vector<String> v = gos_probeset.get(go);
			fw.write(go+"\tna\t");
			for(String ss: v) fw.write(ss+"\t");
			fw.write("\n");
		}
		fw.close();
		
	}
	
	public Vector<String> extractGOTerms(String s){
		Vector<String> v = new Vector<String>();
		StringTokenizer st = new StringTokenizer(s,"$");
		while(st.hasMoreTokens()){
			String go = st.nextToken();
			if(go.length()>0){
			if(go.startsWith("_")) go = go.substring(1, go.length());
			if(go.endsWith("_")) go = go.substring(0, go.length()-1);
			v.add(go);
			}
		}
		return v;
	}
	
	public void addToHashMap(HashMap<String, Vector<String>> hm, Vector<String> v, String term){
		 for(String go: v){
		 if(!go.equals("")){
		 Vector<String> vterm = hm.get(go);
		 if(vterm==null)
			 vterm = new Vector<String>();
		 if(!vterm.contains(term))
			 vterm.add(term);
		 hm.put(go, vterm);
		 }
		 }
	}
	
	public void prepareTable() throws Exception{
		String fn_reference = prefix+"dre2.dat";
		VDataTable annot = VDatReadWrite.LoadFromVDatFile(fn_reference);
		annot.makePrimaryHash("probeset_id");
		prefix = prefix+"repeat_corrected/";
		String fn = prefix+"data.txt";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t",true);
		
		VDatReadWrite.numberOfDigitsToKeep = 3;
		
		for(int i=1;i<vt.colCount;i++) vt.fieldTypes[i] = vt.NUMERICAL;
		vt = VSimpleProcedures.filterMissingValues(vt, 1e-6f);
		vt = VSimpleProcedures.centerTableRows(vt, true, true);
		
		//Annotate
		vt.addNewColumn("symbol", "", "", vt.STRING, "_");
		vt.addNewColumn("entrez_gene_id", "", "", vt.STRING, "_");
		vt.addNewColumn("biological_process", "", "", vt.STRING, "_");
		vt.addNewColumn("cellular_component", "", "", vt.STRING, "_");
		vt.addNewColumn("molecular_function", "", "", vt.STRING, "_");
		for(int i=0;i<vt.rowCount;i++){
			String probeid = vt.stringTable[i][0];
			if(annot.tableHashPrimary.get(probeid)!=null){
				int k = annot.tableHashPrimary.get(probeid).get(0);
				vt.stringTable[i][vt.fieldNumByName("symbol")] = annot.stringTable[k][annot.fieldNumByName("symbol")];
				vt.stringTable[i][vt.fieldNumByName("entrez_gene_id")] = annot.stringTable[k][annot.fieldNumByName("entrez_gene_id")];
				vt.stringTable[i][vt.fieldNumByName("biological_process")] = annot.stringTable[k][annot.fieldNumByName("biological_process")];
				vt.stringTable[i][vt.fieldNumByName("cellular_component")] = annot.stringTable[k][annot.fieldNumByName("cellular_component")];
				vt.stringTable[i][vt.fieldNumByName("molecular_function")] = annot.stringTable[k][annot.fieldNumByName("molecular_function")];
			}
		}
		
		VDatReadWrite.saveToVDatFile(vt, prefix+"drera.dat");
		VDatReadWrite.saveToSimpleDatFile(vt, prefix+"drera.txt");
		
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.saveToSimpleDatFilePureNumerical(vt, prefix+"data_numerical.txt",false);
		System.out.println("Save probesets");
		FileWriter fw3 = new FileWriter(prefix+"data_probesets.txt");
		for(int i=0;i<vt.rowCount;i++)
			fw3.write(vt.stringTable[i][0]+"\n");
		fw3.close();
		System.out.println("Save gene names");
		fw3 = new FileWriter(prefix+"data_genenames.txt");
		for(int i=0;i<vt.rowCount;i++)
			fw3.write(vt.stringTable[i][vt.fieldNumByName("symbol")]+"\n");
		fw3.close();
		System.out.println("Save sample names");
		fw3 = new FileWriter(prefix+"data_samplenames.txt");
		for(int i=1;i<vt.colCount;i++)
			fw3.write(vt.fieldNames[i]+"\n");
		fw3.close();
	}
	
	public static void extractFastaFile(String fasta, String listFn, String outFile, VDataTable annot) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fasta));
		Vector<String> list = Utils.loadStringListFromFile(listFn);
		HashSet<String> lisths = new HashSet<String>();
		for(String s: list) lisths.add(s);
		String s = null;
		FileWriter fw = new FileWriter(outFile);
		FileWriter fwa = new FileWriter(outFile.substring(0, outFile.length()-3)+"_annot.txt");
		HashSet<String> annotatedTranscripts = new HashSet<String>();
		int count=0;
		int countT=0;
		HashSet<String> TDistinct = new HashSet<String>();
		HashSet<String> PSDistinct = new HashSet<String>();
		int countPS=0;
		while((s=lr.readLine())!=null){
			if(count==10000*(int)(0.0001f*count))
				System.out.print(count+"\t");
			count++;
			if(s.startsWith(">")){
				StringTokenizer st = new StringTokenizer(s,";");
				String ProbeID = "";
				String ProbeSetID = "";
				String TranscriptClusterID = "";
				String ProbeSetType = "";
				String Seqname = "";
				String Start = "";
				String Stop = "";
				String category = "";
				while(st.hasMoreTokens()){
					String token = st.nextToken().trim();
					if(token.startsWith("ProbeID")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						ProbeID = st1.nextToken();
					}
					if(token.startsWith("TranscriptClusterID")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						TranscriptClusterID = st1.nextToken();
					}
					if(token.startsWith("ProbeSetID")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						ProbeSetID = st1.nextToken();
					}
					if(token.startsWith("ProbeSetType")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						ProbeSetType = st1.nextToken().trim();
					}
					if(token.startsWith("Seqname")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						Seqname = st1.nextToken().trim();
					}					
					if(token.startsWith("Start")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						Start = st1.nextToken().trim();
					}					
					if(token.startsWith("Stop")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						Stop = st1.nextToken().trim();
					}					
					if(token.startsWith("category")){
						StringTokenizer st1 = new StringTokenizer(token,"=");
						st1.nextToken();
						category = st1.nextToken().trim();
					}					
				}
				String sequence = lr.readLine();
				if(lisths.contains(ProbeSetID)||lisths.contains(TranscriptClusterID)){
					if(!ProbeSetID.equals(""))
						fw.write(">ProbeID="+ProbeID+";"+"ProbeSetID="+ProbeSetID+";"+"ProbeSetType="+ProbeSetType+";"+"Seqname="+Seqname+";"+"Start="+Start+";"+"Stop="+Stop+";"+"category="+category+"\n");
					if(!TranscriptClusterID.equals(""))
						fw.write(">ProbeID="+ProbeID+";"+"TranscriptClusterID="+TranscriptClusterID+";"+"Seqname="+Seqname+";"+"Start="+Start+";"+"Stop="+Stop+";"+"category="+category+"\n");
					fw.write(sequence+"\n");
					if(lisths.contains(ProbeSetID)) countPS++;
					if(lisths.contains(TranscriptClusterID)) countT++;
					if(!TranscriptClusterID.equals("")) TDistinct.add(TranscriptClusterID);
					if(!ProbeSetID.equals("")) PSDistinct.add(ProbeSetID);
					
					// Here, try to annotate with a gene name
					if(!TranscriptClusterID.equals(""))if(!annotatedTranscripts.contains(TranscriptClusterID)){
						Vector<Integer> inds = annot.tableHashPrimary.get(Seqname);
						int start = Integer.parseInt(Start);
						String name = null;
						String name2 = null;
						if(inds!=null)
						for(int kk=0;kk<inds.size();kk++){
							int txStart = Integer.parseInt(annot.stringTable[inds.get(kk)][annot.fieldNumByName("txStart")]);
							int txEnd = Integer.parseInt(annot.stringTable[inds.get(kk)][annot.fieldNumByName("txEnd")]);
							if((start>txStart-1000)&&(start<txEnd+1000)){
								name = annot.stringTable[inds.get(kk)][annot.fieldNumByName("name")];
								name2 = annot.stringTable[inds.get(kk)][annot.fieldNumByName("name2")];
								if(name2.equals("")) name2 = name;
								if(name2.equals("")) name2 = "_";
								break;
							}
						}
						if(name2!=null){
							if(!annotatedTranscripts.contains(TranscriptClusterID))
								fwa.write(TranscriptClusterID+"\t"+name2+"\n");
							annotatedTranscripts.add(TranscriptClusterID);
						}
					}
					
				}
			}
		}
		System.out.println("\n"+countPS+"\tprobesets records and "+countT+" trancript cluster records found in "+PSDistinct.size()+" probeset clusters and "+TDistinct.size()+" transcript clusters");
		lr.close();
		fw.close();
		fwa.close();
	}
	
	public void prepareForGSEAAnalysis(String file) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(file, true, "\t");
		for(int i=0;i<vt.colCount;i++)
			System.out.println(vt.fieldNames[i]);
		// Extract the gene name and save back
		FileWriter fw = new FileWriter(file.substring(0, file.length()-4)+"a.txt");
		FileWriter fws = new FileWriter(file.substring(0, file.length()-4)+"as.txt");
		FileWriter fw_probes = new FileWriter(file.substring(0, file.length()-4)+"_probes.txt");
		FileWriter fw_names = new FileWriter(file.substring(0, file.length()-4)+"_names.txt");
		FileWriter fw_data = new FileWriter(file.substring(0, file.length()-4)+"_data.txt");
		fw.write("Probe\tSymbol\tCHR\tGO\t");
		fws.write("Symbol\t");
		for(int i=6;i<vt.colCount;i++) fw.write(vt.fieldNames[i]+"\t"); fw.write("\n");
		for(int i=6;i<vt.colCount;i++) fws.write(vt.fieldNames[i]+"\t"); fws.write("\n");
		for(int k=0;k<vt.rowCount;k++){
			String probe = vt.stringTable[k][vt.fieldNumByName("GENE")];
			String chr = vt.stringTable[k][vt.fieldNumByName("CHR")];
			String go = vt.stringTable[k][vt.fieldNumByName("GO")];
			String id = vt.stringTable[k][vt.fieldNumByName("ID")];
			String mysymbol = vt.stringTable[k][vt.fieldNumByName("REANNOT")];
			String symbol = "";
			if(!mysymbol.equals("_")) symbol = mysymbol; else{
				if(!id.equals("---")){
				StringTokenizer st = new StringTokenizer(id," /");
				//if(st.countTokens()<2){
				try{
				String ens = st.nextToken();
				String sym = st.nextToken();
				symbol = sym;
				System.out.println(k+"\t"+symbol);
				}catch(Exception e){
					System.out.println("Problem with id: "+id);
				}}
				//}
			}
			
			if(!symbol.equals(""))if(!symbol.equals("_")){
				fw.write(probe+"\t"+symbol+"\t"+chr+"\t"+go+"\t");
				for(int i=6;i<vt.colCount;i++) fw.write(vt.stringTable[k][i]+"\t");
				fw.write("\n");
				fws.write(symbol+"\t");
				for(int i=6;i<vt.colCount;i++) fws.write(vt.stringTable[k][i]+"\t");
				fws.write("\n");
				
				fw_probes.write(probe+"\n");
				fw_names.write(symbol+"\n");
				for(int kk=6;kk<vt.colCount;kk++)
					fw_data.write(vt.stringTable[k][kk]+"\t");
				fw_data.write("\n");
			}
		}
		fw.close();
		fws.close();
		fw_probes.close();
		fw_names.close();
		fw_data.close();
		
		// Now, let us choose some columns to make rnks
		decomposeTableIntoRNKs(file.substring(0, file.length()-4)+"as.txt","DRE");
	}
	
	
	public void decomposeTableIntoRNKs(String fn, String name) throws Exception{
		// produce ranked lists from a table
		String folder = (new File(fn)).getParentFile().getAbsolutePath();
		//SimpleTable tab = new SimpleTable();
		//tab.LoadFromSimpleDatFile(fn, true, "\t");
		VDataTable tab = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		for(int i=1;i<tab.colCount;i++){
			String field = tab.fieldNames[i];
			FileWriter fw_minus_rnk = new FileWriter(folder+"/"+name+"_"+field+".rnk");
			FileWriter fw_plus = new FileWriter(folder+"/"+name+"_"+field+"_plus");
			FileWriter fw_minus = new FileWriter(folder+"/"+name+"_"+field+"_minus");
			FileWriter fw_abs = new FileWriter(folder+"/"+name+"_"+field+"_abs");
			float vals[] = new float[tab.rowCount];
			float val_abs[] = new float[tab.rowCount];
			for(int j=0;j<tab.rowCount;j++){ 
				float f = Float.parseFloat(tab.stringTable[j][i]); 
				vals[j] = f;
				val_abs[j] = Math.abs(f);
			}
			int inds[] = Algorithms.SortMass(vals);
			int ind_abs[] = Algorithms.SortMass(val_abs);
			for(int j=0;j<inds.length;j++){
				/*fw_plus.write(tab.stringTable[inds[inds.length-1-j]][0]+"\t"+vals[inds[inds.length-1-j]]+"\n");
				fw_minus.write(tab.stringTable[inds[j]][0]+"\t"+vals[inds[j]]+"\n");
				fw_abs.write(tab.stringTable[ind_abs[inds.length-1-j]][0]+"\t"+val_abs[ind_abs[inds.length-1-j]]+"\n");*/
				fw_plus.write(tab.stringTable[inds[inds.length-1-j]][0]+"\n");
				fw_minus.write(tab.stringTable[inds[j]][0]+"\n");
				fw_abs.write(tab.stringTable[ind_abs[inds.length-1-j]][0]+"\n");				
			}
			
			HashMap<String, Float> values = new HashMap<String, Float>(); 
			for(int j=0;j<tab.rowCount;j++){
				String nm = tab.stringTable[j][0];
				float f = Float.parseFloat(tab.stringTable[j][i]);
				Float fv =values.get(nm);
				if(fv==null)
					values.put(nm, f);
				else{
					if(Math.abs(f)>Math.abs(fv)){
						values.put(nm, f);
					}
				}
			}
			float vls[] = new float[values.keySet().size()];
			String nms[] = new String[values.keySet().size()];
			int k=0;
			for(String s: values.keySet()){
				nms[k] = s;
				vls[k] = values.get(s);
				k++;
			}
			inds = Algorithms.SortMass(vls);
			for(int j=0;j<inds.length;j++){
				fw_minus_rnk.write(nms[inds[j]]+"\t"+vls[inds[j]]+"\n");
			}
			
			fw_plus.close();fw_minus.close();fw_abs.close(); fw_minus_rnk.close();
		}
		// make often for all lists
		int nstart = 300;
		int nend = 1000;
		int step = 50;	
		int valuesToTest[] = new int[(int)((nend-nstart)/step)+1];
		int k = 0;
		for(int i=nstart;i<=nend;i+=step)
			valuesToTest[k++] = i;
		Vector<String> names = new Vector<String>();
		Vector<Float> scores_plus = new Vector<Float>();
		Vector<Float> scores_minus = new Vector<Float>();
		Vector<Float> scores_abs = new Vector<Float>();
		Vector<Integer> sizes_plus = new Vector<Integer>();
		Vector<Integer> sizes_minus = new Vector<Integer>();
		Vector<Integer> sizes_abs = new Vector<Integer>();
		Vector<Integer> genes_plus = new Vector<Integer>();
		Vector<Integer> genes_minus = new Vector<Integer>();
		Vector<Integer> genes_abs = new Vector<Integer>();
		for(int i=1;i<tab.colCount;i++){ 
			String field = tab.fieldNames[i];
			System.out.println("============================");
			System.out.println("=====  FIELD "+field+"   ==========");
			System.out.println("============================");
			names.add(field); 
			for(k=0;k<3;k++){
			String suff = "_abs";
			if(k==1) suff = "_plus";
			if(k==2) suff = "_minus";
			String filename = folder+"/"+name+"_"+field+suff;
		}
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



}
