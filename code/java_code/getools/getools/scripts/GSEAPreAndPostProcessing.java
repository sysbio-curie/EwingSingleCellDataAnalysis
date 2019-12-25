package getools.scripts;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;

public class GSEAPreAndPostProcessing{
	
	private class GSEAResult{
		String name = "";
		String folder = "";
		VDataTable negativeTable = null;
		VDataTable positiveTable = null;
		Vector<String> listOfGeneSetNames = new Vector<String>();
		
		public void load(String _folder){
			String fn_neg = "";
			String fn_pos = "";
			folder = _folder;
			File lf[] = new File(folder).listFiles();
			for(File f:lf){
				if(f.getName().startsWith("gsea_report_for_na_neg_")&&f.getName().endsWith(".xls"))
					fn_neg = f.getAbsolutePath();
				if(f.getName().startsWith("gsea_report_for_na_pos_")&&f.getName().endsWith(".xls"))
					fn_pos = f.getAbsolutePath();
			}
			negativeTable = VDatReadWrite.LoadFromSimpleDatFile(fn_neg, true, "\t");
			positiveTable = VDatReadWrite.LoadFromSimpleDatFile(fn_pos, true, "\t");
			negativeTable.makePrimaryHash("NAME");
			positiveTable.makePrimaryHash("NAME");
			for(int i=0;i<negativeTable.rowCount;i++){
				String pn = negativeTable.stringTable[i][0];
				if(!listOfGeneSetNames.contains(pn))
					listOfGeneSetNames.add(pn);
			}
			for(int i=0;i<positiveTable.rowCount;i++){
				String pn = positiveTable.stringTable[i][0];
				if(!listOfGeneSetNames.contains(pn))
					listOfGeneSetNames.add(pn);
			}
		}
	}

	public static void main(String[] args) {
		try{
			String folder = "";
			
			//String GSEAResultsFolder = "C:/Datas/Ivashkine/ZebraFish/repeat/gsea/results/";
			String GSEAResultsFolder = "C:/Datas/Ivashkine/ZebraFish/repeat/gsea/ht4effect_last2/";
			
			makeSummaryTable(GSEAResultsFolder);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void makeSummaryTable(String gseaResultsFolder) throws Exception{
		File folder = new File(gseaResultsFolder);
		
		Vector<GSEAResult> results = new Vector<GSEAResult>();
		Vector<String> allGeneSets = new Vector<String>();
		
		File resFolders[] = folder.listFiles();
		for(File f:resFolders){
			String name = f.getName();
			if(!name.startsWith("summary")){
			String fold = f.listFiles()[0].getAbsolutePath();
			GSEAResult res = (new GSEAPreAndPostProcessing()).new GSEAResult();
			res.name = name;
			res.load(fold);
			results.add(res);
			}
		}
		
		for(GSEAResult res: results){
			for(String s:res.listOfGeneSetNames)
				if(!allGeneSets.contains(s))
					allGeneSets.add(s);
		}
		
		Collections.sort(allGeneSets);
		
		FileWriter fw = new FileWriter(gseaResultsFolder+"summary.xls");
		fw.write("NAME\t");
		for(GSEAResult r: results){
			//fw.write(r.name+"_NESP\t"+r.name+"_NESN\t"+r.name+"_NESPA\t"+r.name+"_NESNA\t"+r.name+"_pVP\t"+r.name+"_pVN\t"+r.name+"_pVcP\t"+r.name+"_pVcN\t");
			fw.write(r.name+"_NES\t"+r.name+"_NESA\t"+r.name+"_pVP\t"+r.name+"_pVcP\t");
		}
		fw.write("LE_GENES_NUM\tLE_GENES\n");
		for(String s:allGeneSets){
			fw.write(s+"\t");
			float NESP = 0;
			float NESN = 0;
			float NES = 0;
			float NESA = 0;
			float pVP = 1;
			float pVN = 1;
			float pVcP = 1;
			float pVcN = 1;
			float pV = 1;
			float pVc = 1;
			for(GSEAResult r: results){
				NESP = 0;
				pVP = 1;
				pVcP = 1;
				NESN = 0;
				pVN = 1;
				pVcN = 1;
				if(r.positiveTable.tableHashPrimary.get(s)!=null){
					int k = r.positiveTable.tableHashPrimary.get(s).get(0);
					NESP = Float.parseFloat(r.positiveTable.stringTable[k][r.positiveTable.fieldNumByName("NES")]);
					pVP = Float.parseFloat(r.positiveTable.stringTable[k][r.positiveTable.fieldNumByName("NOM p-val")]);
					pVcP = Float.parseFloat(r.positiveTable.stringTable[k][r.positiveTable.fieldNumByName("FWER p-val")]);
				}
				if(r.negativeTable.tableHashPrimary.get(s)!=null){
					int k = r.negativeTable.tableHashPrimary.get(s).get(0);
					NESN = Float.parseFloat(r.negativeTable.stringTable[k][r.negativeTable.fieldNumByName("NES")]);
					pVN = Float.parseFloat(r.negativeTable.stringTable[k][r.negativeTable.fieldNumByName("NOM p-val")]);
					pVcN = Float.parseFloat(r.negativeTable.stringTable[k][r.negativeTable.fieldNumByName("FWER p-val")]);
				}				
			if(Math.abs(NESN)>Math.abs(NESP)){
				NES = NESN; NESA = Math.abs(NESN);
			}else{
				NES = NESP; NESA = Math.abs(NESP);
			}
			if(pVP<pVN){
				pV = pVP;
			}else{
				pV = pVN;
			}
			if(pVcP<pVcN){
				pVc = pVcP;
			}else{
				pVc = pVcN;
			}
			fw.write(NES+"\t"+NESA+"\t"+pV+"\t"+pVc+"\t");
			}
			
			Vector<Vector<String>> allLeadingGenes = new Vector<Vector<String>>();
			for(GSEAResult r:results){
				String fn = r.folder+"\\"+s+".xls";
				if((new File(fn)).exists()){
					Vector<String> leadingEdgeGenes = GetLeadingGenes(fn,0f);
					allLeadingGenes.add(leadingEdgeGenes);
				}
			}
			Vector<String> commonLeadingGenes = new Vector<String>();
			if(allLeadingGenes.size()>0)
			for(String g:allLeadingGenes.get(0)){
				int k=0;
				for(int i=0;i<allLeadingGenes.size();i++)
					if(allLeadingGenes.get(i).contains(g))
						k++;
				if(k==allLeadingGenes.size())
					commonLeadingGenes.add(g);
			}
			fw.write(commonLeadingGenes.size()+"\t");
			String commonLeadingGenesString = "";
			for(int i=0;i<commonLeadingGenes.size();i++)
				commonLeadingGenesString+=commonLeadingGenes.get(i)+",";
			if(commonLeadingGenesString.length()>0) commonLeadingGenesString = commonLeadingGenesString.substring(0,commonLeadingGenesString.length()-1);
			fw.write(commonLeadingGenesString+"\t");
			fw.write("\n");
		}
		fw.close();
	}
	
	public static Vector<String> GetLeadingGenes(String fn, float scoreThreshold){
		Vector<String> genes = new Vector<String>();
		VDataTable tab = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		
		for(int k=0;k<tab.rowCount;k++){
			String gene = tab.stringTable[k][tab.fieldNumByName("PROBE")];
			float score = Float.parseFloat(tab.stringTable[k][tab.fieldNumByName("RANK METRIC SCORE")]);
			String core = tab.stringTable[k][tab.fieldNumByName("CORE ENRICHMENT")];
			if(core.equals("Yes"))if(Math.abs(score)>scoreThreshold){
				genes.add(gene);
			}
		}
		return genes;
	}
	
	

}
