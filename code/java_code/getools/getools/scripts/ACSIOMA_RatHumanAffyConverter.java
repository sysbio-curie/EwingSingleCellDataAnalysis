package getools.scripts;

import java.io.FileWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;

public class ACSIOMA_RatHumanAffyConverter {
	
	VDataTable ratAffyAnnotation = null;
	VDataTable humanAffyAnnotation = null;
	VDataTable human_rat_orthologs = null;
	VDataTable human_affy = null;
	VDataTable rat_human_orthologs = null;
	VDataTable rat_affy = null;
	VDataTable homologene = null;
	
	HashMap<String,Vector<Integer>> mapEnsembleID_AffyRat = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapEnsembleID_AffyHuman = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapUnigeneID_AffyRat = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapUnigeneID_AffyHuman = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapGeneSymbol_AffyRat = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapGeneSymbol_AffyHuman = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapEntrez_AffyHuman = new HashMap<String,Vector<Integer>>();
	HashMap<String,Vector<Integer>> mapHomologeneEntrez = new HashMap<String,Vector<Integer>>();


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			ACSIOMA_RatHumanAffyConverter cv = new ACSIOMA_RatHumanAffyConverter();
			//cv.convertRatHumanAffy();
			//compileTable("C:/Datas/acsioma/conversions/Rat230_2.na35_to_HGU133Plus2_na30.txt");
			//uniqueProbeIdNameConversion("C:/Datas/acsioma/conversions/Rat230_2.na35_to_HGU133Plus2_na30_combined.txt");
			//cv.makeOrthologTable("C:/Datas/acsioma/conversions/RatHumanOrthologs_Affy_new.txt");
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void convertRatHumanAffy() throws Exception{
		System.out.println("Loading annotations...");
		ratAffyAnnotation = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/affy_annotations/Rat230_2.na35.annot2.txt", true, "\t");
		humanAffyAnnotation = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/affy_annotations/Affymetrix_HGU133Plus2.na30.txt", true, "\t");
		System.out.println("Annotations loaded.");
		human_rat_orthologs = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/Human_Rat_Orthologs.txt", true, "\t");
		human_affy = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/Human_Unigene_Affy.txt", true, "\t");
		rat_human_orthologs = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/Rat_Human_Orthologs.txt", true, "\t");
		rat_affy = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/Rat_Unigene_Affy.txt", true, "\t");
		homologene = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/homologene.data", true, "\t");
		
		System.out.println("Hashing tables...");
		
		human_affy.makePrimaryHash("Gene stable ID");
		rat_affy.makePrimaryHash("AFFY Rat230 2 probe");
		rat_human_orthologs.makePrimaryHash("Gene stable ID");
		human_rat_orthologs.makePrimaryHash("Gene stable ID");
		ratAffyAnnotation.makePrimaryHash("Probe ID");
		humanAffyAnnotation.makePrimaryHash("Probe Set ID");
		homologene.makePrimaryHash("ID");
		
		fillHashMap(ratAffyAnnotation,mapEnsembleID_AffyRat,"Ensembl");
		fillHashMap(humanAffyAnnotation,mapEnsembleID_AffyHuman,"Ensembl");
		fillHashMap(ratAffyAnnotation,mapUnigeneID_AffyRat,"UniGene ID");
		fillHashMap(humanAffyAnnotation,mapUnigeneID_AffyHuman,"UniGene ID");
		fillHashMap(ratAffyAnnotation,mapGeneSymbol_AffyRat,"Gene Symbol");
		fillHashMap(humanAffyAnnotation,mapGeneSymbol_AffyHuman,"Gene Symbol");
		fillHashMap(homologene,mapHomologeneEntrez,"Entrez");
		fillHashMap(humanAffyAnnotation,mapEntrez_AffyHuman,"Entrez Gene");
		
		
		
		System.out.println("RAT_PROBESET\tHUMAN_PROBESET_BIOMARTMAPPING\tHUMAN_GENE_NAME_BIOMARTMAPPING\tHUMAN_PROBESET_ANNOTATIONENSEMBL\tHUMAN_GENE_NAME_ANNOTATIONENSEMBL\tHUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ\tHUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ\tHUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ_RESTR\tHUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ_RESTR\tHUMAN_PROBESET_ANNOTATIONGENESYMBOL\tHUMAN_GENE_NAME_ANNOTATIONGENESYMBOL");
		
		//for(int i=0;i<100;i++){
		for(int i=0;i<ratAffyAnnotation.rowCount;i++){
			String affyRatId = ratAffyAnnotation.stringTable[i][ratAffyAnnotation.fieldNumByName("Probe ID")];
			
			String affyHumanBIOMARTAffyMapping = methodBIOMARTAffyMapping(affyRatId);
			
			String parts[] = affyHumanBIOMARTAffyMapping.split("@");
			
			String affyHumanBIOMARTAffyMapping1 = "";
			String affyHumanBIOMARTAffyMapping2 = "";
			if(!affyHumanBIOMARTAffyMapping.equals("@")){
				affyHumanBIOMARTAffyMapping1 = parts[0];
				affyHumanBIOMARTAffyMapping2 = parts[1];
			}
			
			String affyHumanAnnotationEnsemble = methodAffyAnnotationEnsemble(affyRatId);

			parts = affyHumanAnnotationEnsemble.split("@");
			
			String affyHumanAnnotationEnsemble1 = "";
			String affyHumanAnnotationEnsemble2 = "";
			if(!affyHumanAnnotationEnsemble.equals("@")){
				affyHumanAnnotationEnsemble1 = parts[0];
				affyHumanAnnotationEnsemble2 = parts[1];
			}
			
			String affyHumanHomologeneEntrez = methodAffyAnnotationEntrezHomologene(affyRatId);
			
			parts = affyHumanHomologeneEntrez.split("@");
			String affyHumanHomologeneEntrez1 = "";
			String affyHumanHomologeneEntrez2 = "";
			if(!affyHumanHomologeneEntrez.equals("@")){
				affyHumanHomologeneEntrez1 = parts[0];
				affyHumanHomologeneEntrez2 = parts[1];
			}
			
			String affyHumanHomologeneEntrez_Restrictive = methodAffyAnnotationEntrezHomologene_Restrictive(affyRatId);
			parts = affyHumanHomologeneEntrez_Restrictive.split("@");
			String affyHumanHomologeneEntrez_Restrictive1 = "";
			String affyHumanHomologeneEntrez_Restrictive2 = "";
			if(!affyHumanHomologeneEntrez_Restrictive.equals("@")){
				affyHumanHomologeneEntrez_Restrictive1 = parts[0];
				affyHumanHomologeneEntrez_Restrictive2 = parts[1];
			}
			
			Date dt = new Date();
			String affyHumanAnnotationGeneSymbol = methodAffyAnnotationGeneSymbol(affyRatId);
			parts = affyHumanAnnotationGeneSymbol.split("@");
			String affyHumanAnnotationGeneSymbol1 = "";
			String affyHumanAnnotationGeneSymbol2 = "";
			if(!affyHumanAnnotationGeneSymbol.equals("@")){
				affyHumanAnnotationGeneSymbol1 = parts[0];
				affyHumanAnnotationGeneSymbol2 = parts[1];
			}
			
			System.out.println(affyRatId+"\t"+affyHumanBIOMARTAffyMapping1+"\t"+affyHumanBIOMARTAffyMapping2+"\t"+affyHumanAnnotationEnsemble1+"\t"+affyHumanAnnotationEnsemble2+"\t"+affyHumanHomologeneEntrez1+"\t"+affyHumanHomologeneEntrez2+"\t"+affyHumanHomologeneEntrez_Restrictive1+"\t"+affyHumanHomologeneEntrez_Restrictive2+"\t"+affyHumanAnnotationGeneSymbol1+"\t"+affyHumanAnnotationGeneSymbol2);
			
		}
	}
	
	public String methodBIOMARTAffyMapping(String affyRatId){
		String affyHumanId = "";
		Vector<Integer> rows = rat_affy.tableHashPrimary.get(affyRatId);
		Vector<String> ratGeneId = new Vector<String>();
		Vector<String> humanGeneId = new Vector<String>();
		Vector<String> humanAffyIds = new Vector<String>();
		Vector<String> humanGeneNames = new Vector<String>();
		Vector<String> humanGeneNamesFinal = new Vector<String>();
		if(rows!=null)
			for(int i=0;i<rows.size();i++) ratGeneId.add(rat_affy.stringTable[rows.get(i)][0]);
		
		for(int i=0;i<ratGeneId.size();i++){
			String gene = ratGeneId.get(i);
			if(rat_human_orthologs.tableHashPrimary.get(gene)!=null){
				String humanGene = rat_human_orthologs.stringTable[rat_human_orthologs.tableHashPrimary.get(gene).get(0)][rat_human_orthologs.fieldNumByName("Human gene stable ID")];
				if(!humanGene.equals(""))if(!humanGeneId.contains(humanGene)){
					String humanGeneName = rat_human_orthologs.stringTable[rat_human_orthologs.tableHashPrimary.get(gene).get(0)][rat_human_orthologs.fieldNumByName("Human gene name")];;
					humanGeneId.add(humanGene);
					humanGeneNames.add(humanGeneName);
				}
			}
		}
		
		for(int i=0;i<humanGeneId.size();i++){
			if(human_affy.tableHashPrimary.get(humanGeneId.get(i))!=null){
				Vector<Integer> lines = human_affy.tableHashPrimary.get(humanGeneId.get(i));
				if(lines!=null)
				for(int k=0;k<lines.size();k++){
				String s = human_affy.stringTable[lines.get(k)][human_affy.fieldNumByName("AFFY HG U133 Plus 2 probe")];
				if(s!=null)
				if(!s.equals(""))
					if(!humanAffyIds.contains(s)){ 
						humanAffyIds.add(s);
						if(!humanGeneNamesFinal.contains(humanGeneNames.get(i)))
							humanGeneNamesFinal.add(humanGeneNames.get(i));
					}
				}
			}
		}
		//Collections.sort(humanAffyIds);
		for(String s:humanAffyIds)
			affyHumanId+=s+";";
		if(affyHumanId!=null)if(affyHumanId.endsWith(";"))
			affyHumanId = affyHumanId.substring(0, affyHumanId.length()-1);

		String names = "";
		for(String s:humanGeneNamesFinal)
			names+=s+";";
		if(names!=null)if(names.endsWith(";"))
			names = names.substring(0, names.length()-1);
		
		
		return affyHumanId+"@"+names;
	}
	
	
	public String methodAffyAnnotationEnsemble(String affyRatId){
		String affyHumanId = ""; 
		Vector<Integer> rows = ratAffyAnnotation.tableHashPrimary.get(affyRatId);
		
		Vector<String> ratEnsembles = new Vector<String>();
		
		for(int i=0;i<rows.size();i++){
			String ensembls = ratAffyAnnotation.stringTable[rows.get(i)][ratAffyAnnotation.fieldNumByName("Ensembl")];
			StringTokenizer st = new StringTokenizer(ensembls,"///");
			while(st.hasMoreTokens()){
				String s = st.nextToken().trim();
				if(!s.equals(""))if(!ratEnsembles.contains(s))
					ratEnsembles.add(s);
			}
		}

		Vector<String> humanAffyIds = new Vector<String>();
		Vector<String> humanGeneNames = new Vector<String>();
		
		for(int i=0;i<ratEnsembles.size();i++){
			Vector<Integer> ortinds = rat_human_orthologs.tableHashPrimary.get(ratEnsembles.get(i));
			if(ortinds!=null){
				for(int j=0;j<ortinds.size();j++){
					String humanEnsembl = rat_human_orthologs.stringTable[ortinds.get(j)][rat_human_orthologs.fieldNumByName("Human gene stable ID")];
					if(!humanEnsembl.equals("")){
						Vector<Integer> inds = mapEnsembleID_AffyHuman.get(humanEnsembl);
						if(inds!=null){
							for(int k=0;k<inds.size();k++){
								String probeID = humanAffyAnnotation.stringTable[inds.get(k)][humanAffyAnnotation.fieldNumByName("Probe Set ID")];
								String names = humanAffyAnnotation.stringTable[inds.get(k)][humanAffyAnnotation.fieldNumByName("Gene Symbol")];
								if(!humanAffyIds.contains(probeID)) humanAffyIds.add(probeID);
								StringTokenizer st = new StringTokenizer(names,"///");
								while(st.hasMoreTokens()){
									String name = st.nextToken().trim();
									if(!name.equals(""))if(!humanGeneNames.contains(name)) humanGeneNames.add(name);
								}
							}
						}
					}
				}
			}
		}

		
		for(String s:humanAffyIds)
			affyHumanId+=s+";";
		if(affyHumanId!=null)if(affyHumanId.endsWith(";"))
			affyHumanId = affyHumanId.substring(0, affyHumanId.length()-1);

		String names = "";
		for(String s:humanGeneNames)
			names+=s+";";
		if(names!=null)if(names.endsWith(";"))
			names = names.substring(0, names.length()-1);
		
		return affyHumanId+"@"+names; 
	}
	
	public String methodAffyAnnotationEntrezHomologene(String affyRatId){

		String affyHumanId = ""; 
		Vector<Integer> rows = ratAffyAnnotation.tableHashPrimary.get(affyRatId);
		
		Vector<String> ratEntrezs = new Vector<String>();
		
		for(int i=0;i<rows.size();i++){
			String ensembls = ratAffyAnnotation.stringTable[rows.get(i)][ratAffyAnnotation.fieldNumByName("Entrez Gene")];
			StringTokenizer st = new StringTokenizer(ensembls,"///");
			while(st.hasMoreTokens()){
				String s = st.nextToken().trim();
				if(!s.equals(""))if(!ratEntrezs.contains(s))
					ratEntrezs.add(s);
			}
		}
		
		Vector<String> allEntrezs = new Vector<String>();
		for(int i=0;i<ratEntrezs.size();i++){
			rows = mapHomologeneEntrez.get(ratEntrezs.get(i));
			if(rows!=null){
				for(int j=0;j<rows.size();j++){
					String homologene_id = homologene.stringTable[rows.get(j)][0];
					Vector<Integer> inds = homologene.tableHashPrimary.get(homologene_id);
					for(int k=0;k<inds.size();k++)
						allEntrezs.add(homologene.stringTable[inds.get(k)][homologene.fieldNumByName("Entrez")]);
				}
			}
		}

		Vector<String> humanAffyIds = new Vector<String>();
		Vector<String> humanGeneNames = new Vector<String>();
		
		for(int i=0;i<allEntrezs.size();i++){
			Vector<Integer> inds = mapEntrez_AffyHuman.get(allEntrezs.get(i));
			if(inds!=null){
				for(int j=0;j<inds.size();j++){
					String id = humanAffyAnnotation.stringTable[inds.get(j)][0];
					if(!humanAffyIds.contains(id)) humanAffyIds.add(id);
					String names = humanAffyAnnotation.stringTable[inds.get(j)][humanAffyAnnotation.fieldNumByName("Gene Symbol")];
					StringTokenizer st = new StringTokenizer(names,"///");
					while(st.hasMoreTokens()){
						String name = st.nextToken().trim();
						if(!name.equals(""))if(!humanGeneNames.contains(name)) humanGeneNames.add(name);						
					}

				}
			}
		}
		
		for(String s:humanAffyIds)
			affyHumanId+=s+";";
		if(affyHumanId!=null)if(affyHumanId.endsWith(";"))
			affyHumanId = affyHumanId.substring(0, affyHumanId.length()-1);

		String names = "";
		for(String s:humanGeneNames)
			names+=s+";";
		if(names!=null)if(names.endsWith(";"))
			names = names.substring(0, names.length()-1);
		
		return affyHumanId+"@"+names;
	}

	public String methodAffyAnnotationEntrezHomologene_Restrictive(String affyRatId){

		
		String affyHumanId = ""; 
		Vector<Integer> rows = ratAffyAnnotation.tableHashPrimary.get(affyRatId);
		
		Vector<String> ratEntrezs = new Vector<String>();
		
		for(int i=0;i<rows.size();i++){
			String ensembls = ratAffyAnnotation.stringTable[rows.get(i)][ratAffyAnnotation.fieldNumByName("Entrez Gene")];
			StringTokenizer st = new StringTokenizer(ensembls,"///");
			while(st.hasMoreTokens()){
				String s = st.nextToken().trim();
				if(!s.equals(""))if(!ratEntrezs.contains(s))
					ratEntrezs.add(s);
			}
		}
		
		Vector<String> allEntrezs = new Vector<String>();
		Vector<String> allGeneSymbols = new Vector<String>();
		for(int i=0;i<ratEntrezs.size();i++){
			rows = mapHomologeneEntrez.get(ratEntrezs.get(i));
			if(rows!=null){
				for(int j=0;j<rows.size();j++){
					String homologene_id = homologene.stringTable[rows.get(j)][0];
					Vector<Integer> inds = homologene.tableHashPrimary.get(homologene_id);
					for(int k=0;k<inds.size();k++){
						allEntrezs.add(homologene.stringTable[inds.get(k)][homologene.fieldNumByName("Entrez")]);
						allGeneSymbols.add(homologene.stringTable[inds.get(k)][homologene.fieldNumByName("GeneSymbol")]);
					}
				}
			}
		}

		Vector<String> humanAffyIds = new Vector<String>();
		Vector<String> humanGeneNames = new Vector<String>();
		
		for(int i=0;i<allEntrezs.size();i++){
			Vector<Integer> inds = mapEntrez_AffyHuman.get(allEntrezs.get(i));
			if(inds!=null){
				for(int j=0;j<inds.size();j++){
					String id = humanAffyAnnotation.stringTable[inds.get(j)][0];
					String names = humanAffyAnnotation.stringTable[inds.get(j)][humanAffyAnnotation.fieldNumByName("Gene Symbol")];
					if(allGeneSymbols.contains(names)){
						if(!humanAffyIds.contains(id)) humanAffyIds.add(id);
						if(!humanGeneNames.contains(names)) humanGeneNames.add(names);
					}
				}
			}
		}
		
		for(String s:humanAffyIds)
			affyHumanId+=s+";";
		if(affyHumanId!=null)if(affyHumanId.endsWith(";"))
			affyHumanId = affyHumanId.substring(0, affyHumanId.length()-1);

		String names = "";
		for(String s:humanGeneNames)
			names+=s+";";
		if(names!=null)if(names.endsWith(";"))
			names = names.substring(0, names.length()-1);
		
		return affyHumanId+"@"+names;
	}
	
	
	
	public String methodAffyAnnotationGeneSymbol(String affyRatId){
		String affyHumanId = "";
		
		Vector<Integer> rows = ratAffyAnnotation.tableHashPrimary.get(affyRatId);
		
		Vector<String> ratGeneSymbols = new Vector<String>();
		
		for(int i=0;i<rows.size();i++){
			String symbol = ratAffyAnnotation.stringTable[rows.get(i)][ratAffyAnnotation.fieldNumByName("Gene Symbol")];
			if(!ratGeneSymbols.contains(symbol)) ratGeneSymbols.add(symbol.toUpperCase());
		}

		Vector<String> humanAffyIds = new Vector<String>();
		Vector<String> humanGeneNames = new Vector<String>();
		
		for(int i=0;i<ratGeneSymbols.size();i++)if(!ratGeneSymbols.get(i).equals("---")){
			rows = mapGeneSymbol_AffyHuman.get(ratGeneSymbols.get(i));
			if(rows!=null){
				for(int j=0;j<rows.size();j++){
					String id = humanAffyAnnotation.stringTable[rows.get(j)][0];
					String names = humanAffyAnnotation.stringTable[rows.get(j)][humanAffyAnnotation.fieldNumByName("Gene Symbol")];
					if(ratGeneSymbols.contains(names.toUpperCase())){
						if(!humanAffyIds.contains(id)) humanAffyIds.add(id);
						if(!humanGeneNames.contains(names)) humanGeneNames.add(names);
					}
				}
			}
		}
		
		for(String s:humanAffyIds)
			affyHumanId+=s+";";
		if(affyHumanId!=null)if(affyHumanId.endsWith(";"))
			affyHumanId = affyHumanId.substring(0, affyHumanId.length()-1);

		String names = "";
		for(String s:humanGeneNames)
			names+=s+";";
		if(names!=null)if(names.endsWith(";"))
			names = names.substring(0, names.length()-1);
		
		return affyHumanId+"@"+names;
	}
	
	public void fillHashMap(VDataTable vt, HashMap<String,Vector<Integer>> map, String field){
		for(int i=0;i<vt.rowCount;i++){
			String val = vt.stringTable[i][vt.fieldNumByName(field)];
			StringTokenizer st = new StringTokenizer(val,"///");
			while(st.hasMoreTokens()){
				String id = st.nextToken();
				id = id.trim();
				if(!id.equals("")){
					Vector<Integer> rows = map.get(id);
					if(rows==null) rows = new Vector<Integer>();
					rows.add(i);
					map.put(id, rows);
				}
			}
		}
		//System.out.println(map.size()+" keys");
	}
	
	public static void compileTable(String fn){
		
		VDataTable ratAffyAnnotation = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/affy_annotations/Rat230_2.na35.annot2.txt", true, "\t");
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		
		System.out.println("RAT_PROBESET\tRAT_GENESYMBOL\tHUMAN_PROBESETS\tHUMAN_GENESYMBOLS");
		
		for(int i=0;i<vt.rowCount;i++){
			String RAT_PROBESET =	vt.stringTable[i][vt.fieldNumByName("RAT_PROBESET")];
			String HUMAN_PROBESET_BIOMARTMAPPING =	vt.stringTable[i][vt.fieldNumByName("HUMAN_PROBESET_BIOMARTMAPPING")];
			String HUMAN_GENE_NAME_BIOMARTMAPPING =	vt.stringTable[i][vt.fieldNumByName("HUMAN_GENE_NAME_BIOMARTMAPPING")];
			String HUMAN_PROBESET_ANNOTATIONENSEMBL =	vt.stringTable[i][vt.fieldNumByName("HUMAN_PROBESET_ANNOTATIONENSEMBL")];
			String HUMAN_GENE_NAME_ANNOTATIONENSEMBL =	vt.stringTable[i][vt.fieldNumByName("HUMAN_GENE_NAME_ANNOTATIONENSEMBL")];
			String HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ =	vt.stringTable[i][vt.fieldNumByName("HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ")];
			String HUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ =	vt.stringTable[i][vt.fieldNumByName("HUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ")];
			String HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ_RESTR =	vt.stringTable[i][vt.fieldNumByName("HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ_RESTR")];
			String HUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ_RESTR = vt.stringTable[i][vt.fieldNumByName("HUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ_RESTR")];
			String HUMAN_PROBESET_ANNOTATIONGENESYMBOL = 	vt.stringTable[i][vt.fieldNumByName("HUMAN_PROBESET_ANNOTATIONGENESYMBOL")];
			String HUMAN_GENE_NAME_ANNOTATIONGENESYMBOL	= 		vt.stringTable[i][vt.fieldNumByName("HUMAN_GENE_NAME_ANNOTATIONGENESYMBOL")];
			
			String ids = HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ_RESTR;
			String names = HUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ_RESTR;
			
			
			if((ids==null)||(names==null)){
				ids = "";
				names = "";
			}
			
			if(ids.equals(""))if(!HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ.equals("")){
				ids = HUMAN_PROBESET_ANNOTATIONHOMOLOGENEENTREZ;
				names = HUMAN_GENE_NAME_ANNOTATIONHOMOLOGENEENTREZ;
				if((ids==null)||(names==null)){
					ids = "";
					names = "";
				}				
			}
			if(ids.equals(""))if(!HUMAN_PROBESET_BIOMARTMAPPING.equals("")){
				ids = HUMAN_PROBESET_BIOMARTMAPPING;
				names = HUMAN_GENE_NAME_BIOMARTMAPPING;
				if((ids==null)||(names==null)){
					ids = "";
					names = "";
				}				
			}
			if(ids.equals(""))if(!HUMAN_PROBESET_ANNOTATIONENSEMBL.equals("")){
				ids = HUMAN_PROBESET_ANNOTATIONENSEMBL;
				names = HUMAN_GENE_NAME_ANNOTATIONENSEMBL;
				if((ids==null)||(names==null)){
					ids = "";
					names = "";
				}				
			}
			if(ids.equals(""))if(!HUMAN_PROBESET_ANNOTATIONGENESYMBOL.equals("")){
				ids = HUMAN_PROBESET_ANNOTATIONGENESYMBOL;
				names = HUMAN_GENE_NAME_ANNOTATIONGENESYMBOL;
				if((ids==null)||(names==null)){
					ids = "";
					names = "";
				}				
			}			
			
			if(names.contains("orf"))if(!HUMAN_GENE_NAME_BIOMARTMAPPING.contains("orf")){
				ids = HUMAN_PROBESET_BIOMARTMAPPING;
				names = HUMAN_GENE_NAME_BIOMARTMAPPING;
				if((ids==null)||(names==null)){
					ids = "";
					names = "";
				}				
			}
			
			
			System.out.println(RAT_PROBESET+"\t"+ratAffyAnnotation.stringTable[i][ratAffyAnnotation.fieldNumByName("Gene Symbol")]+"\t"+ids+"\t"+names);
		}
	}
	
	public static void uniqueProbeIdNameConversion(String fn) throws Exception{
		FileWriter fw = new FileWriter(fn+"_gsunique.txt");
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		for(int i=0;i<vt.rowCount;i++){
			String id = vt.stringTable[i][0];
			String name = vt.stringTable[i][1];
			StringTokenizer st = new StringTokenizer(name,"///");
			while(st.hasMoreTokens()){
				String s = st.nextToken().trim();
				if(!s.equals("")){
					fw.write(id+"\t"+s+"\n");
				}
			}
		}
		fw.close();
	}

	public void makeOrthologTable(String fnout) throws Exception{

		System.out.println("Loading annotations...");
		ratAffyAnnotation = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/affy_annotations/Rat230_2.na35.annot2.txt", true, "\t");
		humanAffyAnnotation = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/affy_annotations/Affymetrix_HGU133Plus2.na30.txt", true, "\t");
		System.out.println("Annotations loaded.");
		homologene = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/homologene.data", true, "\t");

		homologene.makePrimaryHash("ID");
		fillHashMap(humanAffyAnnotation,mapGeneSymbol_AffyHuman,"Gene Symbol");
		fillHashMap(homologene,mapHomologeneEntrez,"Entrez");
		fillHashMap(humanAffyAnnotation,mapEntrez_AffyHuman,"Entrez Gene");
		
		HashMap<String,String> mapEntrezGeneSymbolHuman = new HashMap<String,String>();
		HashMap<String,String> mapGeneSymbolEntrezHuman = new HashMap<String,String>();
		HashMap<String,String> mapEntrezGeneSymbolRat = new HashMap<String,String>();
		HashMap<String,Vector<String>> mapEntrez2ProbeIdHuman = new HashMap<String,Vector<String>>();
		HashMap<String,Vector<String>> mapEntrez2ProbeIdRat = new HashMap<String,Vector<String>>();
		
		for(int i=0;i<ratAffyAnnotation.rowCount;i++){
			String id = ratAffyAnnotation.stringTable[i][0];
			String entrezs = ratAffyAnnotation.stringTable[i][ratAffyAnnotation.fieldNumByName("Entrez Gene")];
			String names = ratAffyAnnotation.stringTable[i][ratAffyAnnotation.fieldNumByName("Gene Symbol")];
			StringTokenizer ste = new StringTokenizer(entrezs,"///");
			Vector<String> entrezV = new Vector<String>();
			Vector<String> nameV = new Vector<String>();
			while(ste.hasMoreTokens()){
				String entrez = ste.nextToken().trim();
				if(!entrez.equals("")){
					entrezV.add(entrez);
				}
			}
			StringTokenizer stn = new StringTokenizer(names,"///");
			while(stn.hasMoreTokens()){
				String name = stn.nextToken().trim();
				if(!name.equals("")){
					nameV.add(name);
				}
			}
			if(entrezV.size()!=nameV.size()) 
				System.out.println("ERROR: problem found in matching Entrez and name in rat "+entrezs+"<->"+names);
			else{
				for(int k=0;k<entrezV.size();k++){
					mapEntrezGeneSymbolRat.put(entrezV.get(k), nameV.get(k));
					Vector<String> v = mapEntrez2ProbeIdRat.get(entrezV.get(k));
					if(v==null) v = new Vector<String>();
					v.add(id);
					mapEntrez2ProbeIdRat.put(entrezV.get(k), v);
				}
			}
		}

		
		for(int i=0;i<humanAffyAnnotation.rowCount;i++){
			String id = humanAffyAnnotation.stringTable[i][0];
			String entrezs = humanAffyAnnotation.stringTable[i][humanAffyAnnotation.fieldNumByName("Entrez Gene")];
			String names = humanAffyAnnotation.stringTable[i][humanAffyAnnotation.fieldNumByName("Gene Symbol")];
			StringTokenizer ste = new StringTokenizer(entrezs,"///");
			Vector<String> entrezV = new Vector<String>();
			Vector<String> nameV = new Vector<String>();
			while(ste.hasMoreTokens()){
				String entrez = ste.nextToken().trim();
				if(!entrez.equals("")){
					entrezV.add(entrez);
				}
			}
			StringTokenizer stn = new StringTokenizer(names,"///");
			while(stn.hasMoreTokens()){
				String name = stn.nextToken().trim();
				if(!name.equals("")){
					nameV.add(name);
				}
			}
			if(entrezV.size()!=nameV.size()) 
				System.out.println("ERROR: problem found in matching Entrez and name in human "+entrezs+"<->"+names);
			else{
				for(int k=0;k<entrezV.size();k++){
					mapEntrezGeneSymbolHuman.put(entrezV.get(k), nameV.get(k));
					mapGeneSymbolEntrezHuman.put(nameV.get(k), entrezV.get(k));
					Vector<String> v = mapEntrez2ProbeIdHuman.get(entrezV.get(k));
					if(v==null) v = new Vector<String>();
					v.add(id);
					mapEntrez2ProbeIdHuman.put(entrezV.get(k), v);
				}
			}
		}
		
		
		
		FileWriter fw = new FileWriter(fnout);
		fw.write("GENESYMBOL_RAT\tGENESYMBOL_HUMAN\tENTREZ_RAT\tENTREZ_HUMAN\tPROBESET_RAT\tPROBESET_HUMAN\n");
		
		Set<String> entrezsRat = mapEntrezGeneSymbolRat.keySet();
		for(String entrezRat: entrezsRat){
			if(entrezRat.equals("100366017"))
				System.out.println("100366017");
			String gsRat = mapEntrezGeneSymbolRat.get(entrezRat);
			Vector<String> idsRat = mapEntrez2ProbeIdRat.get(entrezRat);
			String entrezHuman = null;
			String gsHuman = null;
			Vector<String> idsHuman = null;
			if(mapHomologeneEntrez.get(entrezRat)!=null){
				String hgidRat = homologene.stringTable[mapHomologeneEntrez.get(entrezRat).get(0)][0];
				Vector<Integer> rows = homologene.tableHashPrimary.get(hgidRat);
				for(int i:rows){
					String eh = homologene.stringTable[i][homologene.fieldNumByName("Entrez")];
					String gh = homologene.stringTable[i][homologene.fieldNumByName("GeneSymbol")];
					if(mapEntrezGeneSymbolHuman.get(eh)!=null){
						entrezHuman = eh;
						gsHuman = gh;
					}
				}
			}else{
				// We try to simply guess entrez from the capitalizing the name 
				String gh = gsRat.toUpperCase();
				String eh = mapGeneSymbolEntrezHuman.get(gh);
				if(eh!=null){
					entrezHuman = eh;
					gsHuman = gh;
				}
			}
			if(entrezHuman!=null){
				idsHuman = mapEntrez2ProbeIdHuman.get(entrezHuman);
				String idsRats = "";
				String idsHumans = "";
				for(int k=0;k<idsRat.size()-1;k++) idsRats+=idsRat.get(k)+";"; idsRats+=idsRat.get(idsRat.size()-1);
				for(int k=0;k<idsHuman.size()-1;k++) idsHumans+=idsHuman.get(k)+";"; idsHumans+=idsHuman.get(idsHuman.size()-1);
				fw.write(gsRat+"\t"+gsHuman+"\t"+entrezRat+"\t"+entrezHuman+"\t"+idsRats+"\t"+idsHumans+"\n");
				if(!gsRat.toUpperCase().equals(gsHuman)){
					System.out.println("Capitalizing rat symbol does not match human: "+gsRat+"->"+gsHuman);
				}
			}
			
		}
		
		fw.close();
		
	}
	

}
