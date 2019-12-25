package getools;

import java.io.*;
import java.util.*;

public class MonochromaticGeneticClusters {

	/**
	 * @param args
	 */
	
	Vector<GESignature> gmt = new Vector<GESignature>();
	HashMap<String, String> pathwayNames = new HashMap<String, String>();
	HashMap<String, String> ORF2Name = new HashMap<String, String>();
	
	public static int BICLIQUE = 0;
	public static int NOT_KNOWN = 1;
	public static int QCLIQUE = 2;

	public static int maximumComplexSize = 100;
	
	private class Interaction {
		String query = null;
		String array = null;
		float score = 0f;
		Vector<String> complexes = null;
	}
	public static String getInteractionKey(String query, String array){
		String key = null;
		if(query.compareTo(array)>0)
			key=query+"_"+array;
		else
			key=array+"_"+query;
		return key;	
	}
	
	
	private class BiClique {
		Vector<String> queries = new Vector<String>();
		Vector<String> arrays = new Vector<String>();
		Vector<String> allunique = new Vector<String>();
		int type = NOT_KNOWN;
		float intersection = 0f;
		
		public void determineType(){
			for(int i=0;i<queries.size();i++){
				if(arrays.indexOf(queries.get(i))>=0)
					intersection+=1f;
			}
			//intersection/=Math.min((float)queries.size(),(float)arrays.size());
			intersection/=((float)queries.size()+(float)arrays.size())/2f;
			if(intersection==0)
				type = BICLIQUE;
			if(intersection>=0.2f)
				type = QCLIQUE;
		}
	}
	
	public Vector<BiClique> bicliques= new Vector<BiClique>();
	public HashMap<String, Interaction> interactions = new HashMap<String, Interaction>();
	
	public static void main(String[] args) {
		try{
			
			MonochromaticGeneticClusters mgc = new MonochromaticGeneticClusters();
			//mgc.createComplexGMTFileFromGO("C:/Datas/Heyer/GeneticInteractionAnalysis/go_protein_complex_slim.tab");
			//System.exit(0);
			
			mgc.loadBicliques("C:/Datas/Heyer/GeneticInteractionAnalysis/negclusterno.txt");
			mgc.gmt = GMTReader.readGMTDatabase("C:/Datas/Heyer/GeneticInteractionAnalysis/sceKEGG.gmt");
			mgc.loadPathwayNames("C:/Datas/KEGG/Yeast/pathway_list.txt");
			System.out.println(mgc.gmt.size()+" pathways loaded.");
			mgc.loadInteractions("C:/Datas/SyntheticInteractions/Constanzo2010/sgadata_costanzo2009_stringentCutoff_101120.txt");
			mgc.assignComplexesToInteractions("C:/Datas/Heyer/GeneticInteractionAnalysis/go_protein_complex_slim.gmt");
			//mgc.compareCliquesAndPathways(0.1586f,0.1928f);
			//mgc.countNumberOfInteractionsWithinPathway();
			mgc.countNumberOfInteractionsBetweenPathway("C:/Datas/Heyer/GeneticInteractionAnalysis/NegativeInteractionsKEGG_all.txt");
			//mgc.pathwayReport("sce04113");
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	public void loadBicliques(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		int total = 0;
		int bicliques = 0;
		int qcliques = 0;
		while((s=lr.readLine())!=null){
			StringTokenizer st1 = new StringTokenizer(s,"\t");
			String queries = st1.nextToken();
			String arrays = st1.nextToken();
			StringTokenizer stq = new StringTokenizer(queries,",");
			StringTokenizer sta = new StringTokenizer(arrays,",");
			BiClique bc = new BiClique();
			while(stq.hasMoreTokens()){
				String name = stq.nextToken();
				StringTokenizer st2 = new StringTokenizer(name,"_");
				name = st2.nextToken();
				bc.queries.add(name);
				if(bc.allunique.indexOf(name)<0)
					bc.allunique.add(name);
			}
			while(sta.hasMoreTokens()){
				String name = sta.nextToken();
				StringTokenizer st2 = new StringTokenizer(name,"_");
				name = st2.nextToken();
				bc.arrays.add(name);
				if(bc.allunique.indexOf(name)<0)
					bc.allunique.add(name);
			}
			bc.determineType();
			total++;
			if(bc.type==BICLIQUE) bicliques++;
			if(bc.type==QCLIQUE) qcliques++;
			System.out.print(bc.intersection+"\t");
			this.bicliques.add(bc);
		}
		System.out.println("\nTotal\t"+total+"\nBicliques\t"+bicliques+"\nQcliques\t"+qcliques);
		lr.close();
	}
	
	public void compareCliquesAndPathways(float inters_threshold_b, float inters_threshold_q) throws Exception{
		System.out.println("PATHWAY\tSIZE\tBCLIQUES\tQCLIQUES\tBCL_SUMINTER\tQCL_SUMINTER\tNAME");
		Vector<Float> bclique_intersections = new Vector<Float>();
		Vector<Float> qclique_intersections = new Vector<Float>();
		for(int i=0;i<gmt.size();i++){
			System.out.print(gmt.get(i).name+"\t"+gmt.get(i).geneNames.size()+"\t");
			int numberB = 0;
			int numberQ = 0;
			float numberB_sumOfIntersections = 0f;
			float numberQ_sumOfIntersections = 0f;
			for(int j=0;j<bicliques.size();j++){
				BiClique bc = bicliques.get(j);
				float inters = calcIntersection(gmt.get(i).geneNames,bc.allunique);
				if(bc.type==BICLIQUE){
					numberB_sumOfIntersections+=inters;
					bclique_intersections.add(inters);
					if(inters>=inters_threshold_b){
						numberB+=1;
					}
				}
				if(bc.type==QCLIQUE){
					numberQ_sumOfIntersections+=inters;
					qclique_intersections.add(inters);
					if(inters>=inters_threshold_q){
						numberQ+=1;
					}
				}
			}
			System.out.print(numberB+"\t"+numberQ+"\t"+numberB_sumOfIntersections+"\t"+numberQ_sumOfIntersections+"\t"+pathwayNames.get(gmt.get(i).name)+"\n");
		}
		FileWriter fw1 = new FileWriter("C:/Datas/Heyer/GeneticInteractionAnalysis/bclique_intersections");
		for(int i=0;i<bclique_intersections.size();i++)
			fw1.write(bclique_intersections.get(i)+"\n"); 
		FileWriter fw2 = new FileWriter("C:/Datas/Heyer/GeneticInteractionAnalysis/qclique_intersections");
		for(int i=0;i<qclique_intersections.size();i++)
			fw2.write(qclique_intersections.get(i)+"\n"); 
		fw1.close();
		fw2.close();
	}
	
	public void countNumberOfInteractionsWithinPathway(){
		System.out.println("PATHWAY\tNAME\tSIZE\tNEGATIVE\tNEG_NOCOMPLEX\tPOSITIVE\tPOS_NOCOMPLEX\tNEGATIVE_PROP\tNEG_NOCOMPLEX_PROP\tPOSITIVE_PROP\tPOS_NOCOMPLEX_PROP");
		for(int i=0;i<gmt.size();i++){
			int within_negative = 0;
			int within_positive = 0;
			int within_negative_nocomplex = 0;
			int within_positive_nocomplex = 0;
			int pathwaySize = gmt.get(i).geneNames.size();
			for(int j=0;j<gmt.get(i).geneNames.size();j++)
				for(int k=j+1;k<gmt.get(i).geneNames.size();k++){
					String key = getInteractionKey(gmt.get(i).geneNames.get(j), gmt.get(i).geneNames.get(k));
					Interaction inter = interactions.get(key);
					if(inter!=null){
					if(inter.score<0){
						within_negative++;
						if(inter.complexes==null)
							within_negative_nocomplex++;
					}
					if(inter.score>0){
						within_positive++;
						if(inter.complexes==null)
							within_positive_nocomplex++;
					}
					}
				}
		float pathwaySize1 = ((pathwaySize*pathwaySize-pathwaySize)/2f);
		System.out.println(gmt.get(i).name+"\t"+pathwayNames.get(gmt.get(i).name)+"\t"+pathwaySize+"\t"+within_negative+"\t"+within_negative_nocomplex+"\t"+within_positive+"\t"+within_positive_nocomplex+"\t"+(float)within_negative/pathwaySize1+"\t"+(float)within_negative_nocomplex/pathwaySize1+"\t"+(float)within_positive/pathwaySize1+"\t"+(float)within_positive_nocomplex/pathwaySize1);
		}
	}
	
	public void countNumberOfInteractionsBetweenPathway(String fn){
		try{
		System.out.println("PATHWAY\tNAME\tSIZE\tNEGATIVE\tNEG_NOCOMPLEX\tPOSITIVE\tPOS_NOCOMPLEX\tNEGATIVE_PROPN\tNEG_NOCOMPLEX_PROPN\tPOSITIVE_PROP\tPOS_NOCOMPLEX_PROPN\tNEGATIVE_PARTNER\tNEGATIVE_PARTNER_N");
		FileWriter fw = new FileWriter(fn);
		fw.write("PATHWAY1\tSIZE1\tPATHWAY2\tSIZE2\tSCORE_NEGATIVE\tSCORE_POSITIVE\n");
		for(int i1=0;i1<gmt.size();i1++){
			int between_negative = 0;
			int between_positive = 0;
			int between_negative_nocomplex = 0;
			int between_positive_nocomplex = 0;
			float pathwaySize1 = gmt.get(i1).geneNames.size();
			float pathwaySizeSum = 0f;
			int imax = 0;
			float negative_max = 0f;
			for(int i2=0;i2<gmt.size();i2++)if(i1!=i2){
			float negative_i2 = 0f;
			float positive_i2 = 0f;
			float pathwaySize2 = gmt.get(i2).geneNames.size();
			for(int j=0;j<gmt.get(i1).geneNames.size();j++)
				for(int k=0;k<gmt.get(i2).geneNames.size();k++){
					String key = getInteractionKey(gmt.get(i1).geneNames.get(j), gmt.get(i2).geneNames.get(k));
					Interaction inter = interactions.get(key);
					if(inter!=null){
					if(inter.score<0){
						between_negative++;
						if(inter.complexes==null)
							between_negative_nocomplex++;
							negative_i2++;
					}
					if(inter.score>0){
						between_positive++;
						if(inter.complexes==null)
							between_positive_nocomplex++;
							positive_i2++;
					}
					}
				}
			if(pathwaySize2>9)
			if(negative_i2/pathwaySize1/pathwaySize2>negative_max){
				negative_max = negative_i2/pathwaySize1/pathwaySize2;
				imax = i2;
			}
			float score_positive = negative_i2/pathwaySize1/pathwaySize2;
			float score_negative = positive_i2/pathwaySize1/pathwaySize2;;
			if(i2>i1)if(pathwaySize1>8)if(pathwaySize2>8)
				fw.write(pathwayNames.get(gmt.get(i1).name)+"\t"+pathwaySize2+"\t"+pathwayNames.get(gmt.get(i2).name)+"\t"+pathwaySize2+"\t"+score_negative+"\t"+score_positive+"\n");
			}
		//System.out.println(gmt.get(i).name+"\t"+pathwayNames.get(gmt.get(i).name)+"\t"+pathwaySize+"\t"+within_negative+"\t"+within_negative_nocomplex+"\t"+within_positive+"\t"+within_positive_nocomplex+"\t"+(float)within_negative/pathwaySize1+"\t"+(float)within_negative_nocomplex/pathwaySize1+"\t"+(float)within_positive/pathwaySize1+"\t"+(float)within_positive_nocomplex/pathwaySize1);
		System.out.println(gmt.get(i1).name+"\t"+pathwayNames.get(gmt.get(i1).name)+"\t"+pathwaySize1+"\t"+between_negative+"\t"+between_negative_nocomplex+"\t"+between_positive+"\t"+between_positive_nocomplex+"\t"+between_negative/pathwaySize1+"\t"+between_negative_nocomplex/pathwaySize1+"\t"+between_positive/pathwaySize1+"\t"+between_positive_nocomplex/pathwaySize1+"\t"+pathwayNames.get(gmt.get(imax).name)+"\t"+negative_max);
		}
		fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void loadInteractions(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		lr.readLine();
		while((s=lr.readLine())!=null){
			StringTokenizer st = new StringTokenizer(s,"\t");
			String queryORF = st.nextToken();
			StringTokenizer stq = new StringTokenizer(queryORF,"_");
			queryORF = stq.nextToken();
			String queryName = st.nextToken();
			String arrayORF = st.nextToken();
			StringTokenizer sta = new StringTokenizer(arrayORF,"_");
			arrayORF = sta.nextToken();
			String arrayName = st.nextToken();
			float eps = Float.parseFloat(st.nextToken());
			ORF2Name.put(queryORF, queryName);
			ORF2Name.put(arrayORF, arrayName);
			Interaction inter = new Interaction();
			inter.query = queryORF;
			inter.array = arrayORF;
			inter.score = eps;
			interactions.put(getInteractionKey(queryORF, arrayORF), inter);
		}
	}
	
	public void loadPathwayNames(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		while((s=lr.readLine())!=null){
			StringTokenizer st = new StringTokenizer(s,"\t");
			String pathwayId = st.nextToken();
			String pathwayName = st.nextToken();
			this.pathwayNames.put(pathwayId, pathwayName);
		}
		lr.close();
	}
	
	
	public float calcIntersection(Vector<String> set1, Vector<String> set2){
		float inters = 0f;
		for(int i=0;i<set1.size();i++){
			if(set2.indexOf(set1.get(i))>=0)
				inters+=1f;
		}
		inters/=((float)set1.size()+(float)set2.size())/2f;
		return inters;
	}
	
	public void createComplexGMTFileFromGO(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		FileWriter fw = new FileWriter(fn+".gmt");
		while((s=lr.readLine())!=null){
			StringTokenizer st = new StringTokenizer(s,"\t");
			String complex = st.nextToken();
			String composition = st.nextToken();
			fw.write(complex+"\tna\t");
			StringTokenizer st1 = new StringTokenizer(composition,"|");
			while(st1.hasMoreTokens()){
				String tok = st1.nextToken();
				System.out.println(tok);
				if(!(tok.equals("Verified/")||tok.equals("Uncharacterized/")||tok.equals("Dubious/"))){
					StringTokenizer st2 = new StringTokenizer(tok,"/");
					st2.nextToken();
					String orf = st2.nextToken();
					fw.write(orf+"\t");
				}
			}
			fw.write("\n");
		}
		fw.close();
		lr.close();
	}
	
	public void assignComplexesToInteractions(String fn) throws Exception{
		Vector<GESignature> complexes = GMTReader.readGMTDatabase(fn);
		for(int i=0;i<complexes.size();i++){
			//System.out.println(complexes.get(i).name+"\t"+complexes.get(i).geneNames.size());
			for(int j=0;j<complexes.get(i).geneNames.size();j++)if(complexes.get(i).geneNames.size()<=maximumComplexSize)
				for(int k=j+1;k<complexes.get(i).geneNames.size();k++){
					String key = getInteractionKey(complexes.get(i).geneNames.get(j), complexes.get(i).geneNames.get(k));
					Interaction inter = interactions.get(key);
					if(inter!=null){
						if(inter.complexes==null)
							inter.complexes = new Vector<String>();
						inter.complexes.add(complexes.get(i).name);
					}
				}
		}
	}
	
	public void pathwayReport(String pathwayId){
		System.out.println("KEY\tGENE1\tGENE2\tEPS\tINCOMPLEX");
		for(int i=0;i<gmt.size();i++){
			if(gmt.get(i).name.equals(pathwayId)){

				for(int j=0;j<gmt.get(i).geneNames.size();j++)
					for(int k=j+1;k<gmt.get(i).geneNames.size();k++){
						String key = getInteractionKey(gmt.get(i).geneNames.get(j), gmt.get(i).geneNames.get(k));
						Interaction inter = interactions.get(key);
						if(inter!=null){
						System.out.print(key+"\t"+this.ORF2Name.get(inter.query)+"\t"+this.ORF2Name.get(inter.array)+"\t"+inter.score+"\t");
							if(inter.complexes!=null){
								System.out.print(inter.complexes.size()+"\t");
								for(int s=0;s<inter.complexes.size();s++){
									System.out.print(inter.complexes.get(s)+"\t");
								}
							}else{
								System.out.print(0+"\t");
							}
						System.out.println();
						}
					}				
				
			}
		}
	}
	
}
