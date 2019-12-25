package getools.scripts;

import getools.GESignature;
import getools.GMTReader;
import getools.Metagene;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;

public class ROMASignaturePrioritization {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{

			//extractCorrelationMatrix("C:/Datas/ROMA/pancancer/conserved_metagenes/","correlations.xls","correlations_pvals.xls","WEE");
			//extractCorrelationMatrix("C:/Datas/ROMA/pancancer/conserved_metagenes/","correlations.xls","correlations_pvals.xls","GSE3920_UNTREATED_VS_IFNA_TREATED_ENDOTHELIAL_CELL");
			//System.exit(0);
			
			//computeGMTCorrelations("C:/Datas/ROMA_Signature_selection_correlatedmetagenes/");
			//assembleTheCorrelationMatrix("D:/Datas/ROMA_Signature_selection_correlatedmetagenes/");
			filterGMTFile("C:/Datas/BIODICA/knowledge/genesets/msigdb.v5.2.symbols.gmt",50,200);
			System.exit(0);
			
			String databases[] = {"acsn","anne","c1","c2_cp","c3_mir","c3_tf","c4","c5","c6","c7_finala","c7_finalb","c7a","c7b","h","IPA","speed","CGP","CGPa","CGPb"};
			//String databases[] = {"acsn","anne"};
			//String prefixes[] = {"ACC","BLCA","BRCA","CESC","CHOL","COAD","DLBC","ESCA","GBM","HNSC","KICH","KIRC","KIRP","LGG","LIHC","LUAD","LUSC","MESO","OV","PAAD","PCPG","PRAD","READ","SARC","SKCM","STAD","TGCT","THCA","THYM","UCEC","UCS","UVM"};
			//String prefixes[] = {"BLCA","BRCA","CESC","CHOL","COAD","DLBC","ESCA","GBM","HNSC","KICH","KIRC","KIRP","LGG","LIHC","LUAD","LUSC","MESO","OV","PAAD","PCPG","PRAD","READ","SARC","SKCM","STAD","TGCT","THCA","THYM","UCEC","UCS","UVM"};
			String prefixes[] = {"STAD","TGCT","THCA","THYM","UCEC","UCS","UVM"};
			//String prefixes[] = {"ACC"};
			
			String folder = "D:/Datas/ROMA_Signature_selection2016/";
			
			for(int i=0;i<prefixes.length;i++){
				System.out.println(prefixes[i]);
				Date tm = new Date();
				Vector<GESignature> sig = new Vector<GESignature>();
				for(int j=0;j<databases.length;j++){
					for(int kk=-1;kk<10;kk++){
						String ns = "";
						if(kk>=0) ns = ""+kk;
					int count = 0;
					String folderName = folder+prefixes[i]+databases[j]+ns;
					File f = new File(folderName);
					if(f.exists()){
						System.out.print("\t\t"+databases[j]+ns);
						VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folderName+"/GeneProjectionMatrix.xls", true, "\t");
						//Utils.printUsedMemory();
						for(int k=1;k<vt.colCount;k++){
							Metagene mg = new Metagene();
							mg.initializeWeightsByOnes();
							mg.name = vt.fieldNames[k];
							for(int l=0;l<vt.rowCount;l++){
								if(!vt.stringTable[l][k].equals("N/A")){
									//System.out.println(vt.stringTable[l][k]);
									String geneName = vt.stringTable[l][0];
									float weight = Float.parseFloat(vt.stringTable[l][k]);
									mg.geneNames.add(geneName);
									mg.weightSpecified.add(true);
									mg.weights.add(weight);
								}
							}
							sig.add(mg);
							count++;
						}
						System.out.println("\t"+count+" signatures"+"\t"+((new Date()).getTime()-tm.getTime())/1000+" sec");
					}
					}
				}
				GMTReader.saveSignaturesTOGMTFormat(sig, folder+prefixes[i]+".gmt",true);
				//System.out.println("Time = "+((new Date()).getTime()-tm.getTime())/1000+" sec");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void computeGMTCorrelations(String folder) throws Exception{
		Vector<String> cancerTypes = new Vector<String>();
		File fs[] = new File(folder).listFiles();
		for(File f: fs)if(f.getName().endsWith(".gmt")){
			String fn = f.getName();
			String cancerType = fn.substring(0, fn.length()-4);
			cancerTypes.add(cancerType);
		}

		String exampleSig = "KEGG_ASTHMA";
		String exampleType1 = "BLCA";
		String exampleType2 = "BRCA";
		
		
		HashMap<String,Metagene> averageMetagenes = new HashMap<String,Metagene>();
		HashMap<String,Metagene> averageMetagenesOccurences = new HashMap<String,Metagene>();
		Collections.sort(cancerTypes);
		for(int i=0;i<cancerTypes.size();i++){
			Vector<Metagene> mi = GMTReader.readGMTDatabaseWithWeights(folder+cancerTypes.get(i)+".gmt");
			
			// Update average metagene map
			for(int l=0;l<mi.size();l++){
				Metagene avmg = averageMetagenes.get(mi.get(l).name);
				Metagene avmgOccurences = averageMetagenesOccurences.get(mi.get(l).name);
				if(avmg==null){
					avmg = new Metagene();
					avmgOccurences = new Metagene();
					avmg.weights = new Vector<Float>();
					avmgOccurences.weights = new Vector<Float>();
					averageMetagenes.put(mi.get(l).name,avmg);
					averageMetagenesOccurences.put(mi.get(l).name,avmgOccurences);
				}
				mi.get(l).normalizeWeights(); mi.get(l).makeHeavyTailPositive(1f/(float)Math.sqrt(mi.get(l).geneNames.size()));
				for(int p=0;p<mi.get(l).geneNames.size();p++){
					String gn = mi.get(l).geneNames.get(p);
					int ind = avmg.geneNames.indexOf(gn);
					if(ind<0){
						avmg.geneNames.add(gn);
						avmg.weights.add(mi.get(l).weights.get(p));
						avmgOccurences.geneNames.add(gn);
						avmgOccurences.weights.add(1f);
					}else{
						avmg.weights.set(ind, avmg.weights.get(ind)+mi.get(l).weights.get(p));
						avmgOccurences.weights.set(ind, avmgOccurences.weights.get(ind)+1f);
					}
				}
			}
			
			for(int j=i+1;j<cancerTypes.size();j++){
				String pair = cancerTypes.get(i)+"_"+cancerTypes.get(j);
				System.out.println(pair);
				File f = new File(folder+pair+".txt");
				if(!f.exists()){
					Vector<GESignature> mj = GMTReader.readGMTDatabaseWithWeights(folder+cancerTypes.get(j)+".gmt");
					FileWriter fw = new FileWriter(folder+pair+".txt");
					for(int k=0;k<mi.size();k++){
						Metagene mg = (Metagene)mi.get(k);
						mg.normalizeWeights(); mg.makeHeavyTailPositive(0.1f);
						int ind = GESignature.findSignatureByName(mg.name, mj);
						if(ind>0){
							Metagene m1 = mg;
							Metagene m2 = (Metagene)mj.get(ind);
							m2.normalizeWeights(); m2.makeHeavyTailPositive(0.1f);
							float corr = Metagene.CalcCorrelation(m1, m2);
							int intersection = VSimpleFunctions.IntersectionOfSets(m1.geneNames, m2.geneNames);
							double pval = VSimpleFunctions.calcCorrelationPValue(corr, m1.geneNames.size());
							//System.out.println("\t"+m1.name+"\t"+corr+"\t"+pval+"\t"+intersection);
							fw.write(m1.name+"\t"+corr+"\t"+Math.abs(corr)+"\t"+pval+"\t"+intersection+"\n");
							if(mg.name.equals(exampleSig))if(cancerTypes.get(i).equals(exampleType1))if(cancerTypes.get(j).equals(exampleType2)){
								System.out.println(exampleSig+"\t"+exampleType1+"\t"+exampleType2);
								m1.makeMap();
								m2.makeMap();
								Set<String> keys = m1.map.keySet();
								for(String s: keys){
									if(m2.map.get(s)!=null){
										System.out.println(s+"\t"+m1.map.get(s)+"\t"+m2.map.get(s));
									}
								}

							}
						}
					}
					fw.close();
				}
			}
		}
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+"annotations_informative.txt", true, "\t");
		vt.makePrimaryHash(vt.fieldNames[1]);
		
		FileWriter fw = new FileWriter(folder+"AverageConservedMetaGenes._gmt_");
		Set<String> keys = averageMetagenes.keySet();
		System.out.println(exampleSig+"\tWEIGHT");
		for(String name: keys){
			if(vt.tableHashPrimary.containsKey(name)){
			float conservationScore = Float.parseFloat(vt.stringTable[vt.tableHashPrimary.get(name).get(0)][vt.fieldNumByName("AVERAGE_CORR_PVAL")]);
			System.out.println(name+"\t"+conservationScore);
			if(conservationScore>=6f){
			fw.write(name+"\tna\t");
			Metagene avmg = averageMetagenes.get(name);
			Metagene avmgOccurences = averageMetagenesOccurences.get(name);
			for(int k=0;k<avmg.geneNames.size();k++){
				float weight = avmg.weights.get(k);
				float numOccur = avmgOccurences.weights.get(k);
				avmg.weights.set(k, weight/numOccur);
			}
			avmg.normalizeWeightsOnZeroCenteredStandardDeviation();
			for(int k=0;k<avmg.geneNames.size();k++){
				String geneName = avmg.geneNames.get(k);
				float weight = avmg.weights.get(k);
				float numOccur = avmgOccurences.weights.get(k);
				if(numOccur>4f){
				DecimalFormat df = new DecimalFormat("#.##"); 
				fw.write(geneName+"["+df.format(weight)+"]\t");
				if(false)if(exampleSig.equals(name)){
					System.out.println(geneName+"\t"+weight+"\t"+numOccur);
				}
				}
			}
			fw.write("\n");
			}}
		}
		fw.close();
		
	}
	
	public static void assembleTheCorrelationMatrix(String folder) throws Exception{
		Vector<String> pairs = new Vector<String>();
		File fs[] = new File(folder).listFiles();
		HashSet<String> allSignatures = new HashSet<String>();
		for(File f: fs)if(f.getName().endsWith(".txt")){
			String fn = f.getName();
			String pair = fn.substring(0, fn.length()-4);
			pairs.add(pair);
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pair+".txt", false, "\t");
			for(int k=0;k<vt.rowCount;k++)
				allSignatures.add(vt.stringTable[k][0]);
		}
		Collections.sort(pairs);
		Vector<String> signatures = new Vector<String>();
		for(String s: allSignatures) signatures.add(s);
		Collections.sort(signatures);
		System.out.println("Total number of signatures = "+signatures.size());
		
		float matrix_corr[][] = new float[signatures.size()][pairs.size()];
		float matrix_pval[][] = new float[signatures.size()][pairs.size()];

		for(int i=0;i<pairs.size();i++)for(int j=0;j<signatures.size();j++){
			matrix_corr[j][i] = Float.NaN;
			matrix_pval[j][i] = Float.NaN;
		}
		
		for(int i=0;i<pairs.size();i++){
			String pair = pairs.get(i);
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+pair+".txt", false, "\t");
			for(int k=0;k<vt.rowCount;k++){
				String signature = vt.stringTable[k][0];
				float corr = Float.parseFloat(vt.stringTable[k][2]);
				double pval = Double.parseDouble(vt.stringTable[k][3]);
				int ind = signatures.indexOf(signature);
				matrix_corr[ind][i] = corr;
				matrix_pval[ind][i] = (float)(-Math.log10(pval));
			}
		}
		
		FileWriter fw = new FileWriter(folder+"correlations.xls");
		fw.write("SIGNATURE\t"); for(int i=0;i<pairs.size();i++) fw.write(pairs.get(i)+"\t");
		fw.write("\n");
		for(int k=0;k<signatures.size();k++){
			fw.write(signatures.get(k)+"\t"); 
			for(int i=0;i<pairs.size();i++){ 
				float val = matrix_corr[k][i];
				if(Float.isNaN(val))
					fw.write("N/A\t");
				else
					fw.write(val+"\t");
			}
			fw.write("\n");
		}
		fw.close();
		
		fw = new FileWriter(folder+"correlations_pvals.xls");
		fw.write("SIGNATURE\t"); for(int i=0;i<pairs.size();i++) fw.write(pairs.get(i)+"\t");
		fw.write("\n");
		for(int k=0;k<signatures.size();k++){
			fw.write(signatures.get(k)+"\t"); 
			for(int i=0;i<pairs.size();i++){ 
				float val = matrix_pval[k][i];
				if(Float.isNaN(val))
					fw.write("N/A\t");
				else
					fw.write(val+"\t");
			}
			fw.write("\n");
		}
		fw.close();
		
		
	}
	
	public static void extractCorrelationMatrix(String folder, String fileWithAllCorrelations, String fileWithAllCorrelationsPvals, String signature) throws Exception{
		VDataTable corrs = VDatReadWrite.LoadFromSimpleDatFile(folder+fileWithAllCorrelations, true, "\t",true);
		corrs.makePrimaryHash("SIGNATURE");
		VDataTable corrs_pvals = VDatReadWrite.LoadFromSimpleDatFile(folder+fileWithAllCorrelationsPvals, true, "\t",true);
		corrs_pvals.makePrimaryHash("SIGNATURE");
		int kcorr = corrs.tableHashPrimary.get(signature).get(0);
		int kcorrpval = corrs_pvals.tableHashPrimary.get(signature).get(0);
		
		Vector<String> types = new Vector<String>();
		for(int i=1;i<corrs.colCount;i++){
			String pair = corrs.fieldNames[i];
			String couple[] = pair.split("_");
			if(!types.contains(couple[0])) types.add(couple[0]);
			if(!types.contains(couple[1])) types.add(couple[1]);
		}
		Collections.sort(types);
		
		float matrix[][] = new float[types.size()][types.size()];
		float matrix_pval[][] = new float[types.size()][types.size()];
		for(int i=1;i<corrs.colCount;i++){
			String pair = corrs.fieldNames[i];
			if(!corrs.stringTable[kcorr][i].equals("N/A")){
				String couple[] = pair.split("_");
				float corr = Float.parseFloat(corrs.stringTable[kcorr][i]);
				float corr_pval = Float.parseFloat(corrs_pvals.stringTable[kcorrpval][i]);
				matrix[types.indexOf(couple[0])][types.indexOf(couple[1])] = corr;
				matrix[types.indexOf(couple[1])][types.indexOf(couple[0])] = corr;
				matrix_pval[types.indexOf(couple[0])][types.indexOf(couple[1])] = corr_pval;
				matrix_pval[types.indexOf(couple[1])][types.indexOf(couple[0])] = corr_pval;
			}
		}
		
		FileWriter fw = new FileWriter(folder+signature+"_corr.txt");
		FileWriter fwp = new FileWriter(folder+signature+"_corrpval.txt");
		FileWriter fwn = new FileWriter(folder+signature+"_types.txt");
		
		for(int i=0;i<types.size();i++) fwn.write(types.get(i)+"\n");
		for(int i=0;i<types.size();i++){
			for(int j=0;j<types.size();j++){ 
				fw.write(matrix[i][j]+"\t");
				fwp.write(matrix_pval[i][j]+"\t");
			}
				fw.write("\n");
				fwp.write("\n");
		}
		
		fw.close();
		fwp.close();
		fwn.close();
	}

	public static void filterGMTFile(String fn, int minNum, int maxNum) throws Exception{
		System.out.println("Reading GMT...");
		Vector<GESignature> sigs = GMTReader.readGMTDatabase(fn);
		System.out.println("GMT Loaded.");
		String fn1 = fn.substring(0, fn.length()-4)+"_"+minNum+"_"+maxNum+".gmt";
		FileWriter fw = new FileWriter(fn1);
		for(GESignature gs: sigs){
			if(gs.geneNames.size()>=minNum)
				if(gs.geneNames.size()<=maxNum){
					fw.write(gs.name+"\tna\t");
					for(String s: gs.geneNames)
						fw.write(s+"\t");
					fw.write("\n");
				}
		}
		fw.close();
	}

	
}
