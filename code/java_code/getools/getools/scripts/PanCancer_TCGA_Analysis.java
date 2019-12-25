package getools.scripts;

import getools.GESignature;
import getools.GMTReader;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import edu.rpi.cs.xgmml.GraphDocument;
import edu.rpi.cs.xgmml.GraphicNode;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;

public class PanCancer_TCGA_Analysis {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			//geneSetROMAMetaSampleCorrelationsTable("C:/Datas/ROMA/pancancer/corr_net_ROMA/");
			geneSetROMAMetaSampleCorrelationsTable("C:/Datas/ROMA/pancancer/corr_net_ROMA/nets_allH&speed/"); 
			
			//calcGeneSetSizes("C:/Datas/ROMA/pancancer/gene_sets/all.gmt");
			//selectSignatures("C:/Datas/ROMA/pancancer/gene_sets/all.gmt","C:/Datas/ROMA/pancancer/gene_sets/informativelist");
			
			//changeNetworkLayout("C:/Datas/ROMA/pancancer/corr_net_ROMA/nets_allH&speed/final_complete_H_speed.xgmml","C:/Datas/ROMA/pancancer/corr_net_ROMA/nets_allH&speed/av_corr_matrix_tsne.txt");
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private static void changeNetworkLayout(String xgmmlFile, String layoutFile) throws Exception{
		// TODO Auto-generated method stub
		GraphDocument gr = XGMML.loadFromXMGML(xgmmlFile);
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(layoutFile, true, "\t");
		vt.makePrimaryHash(vt.fieldNames[0]);
		String s = xgmmlFile.substring(0,xgmmlFile.length()-6);
		for(GraphicNode n: gr.getGraph().getNodeArray()){
			String label = n.getLabel();
			int k = vt.tableHashPrimary.get(label).get(0);
			float x = Float.parseFloat(vt.stringTable[k][1]);
			float y = Float.parseFloat(vt.stringTable[k][2]);
			n.getGraphics().setX(x);
			n.getGraphics().setY(y);
		}
		XGMML.saveToXGMML(gr, s+"_newlayout.xgmml");
	}


	public static void geneSetROMAMetaSampleCorrelationsTable(String folder) throws Exception{
		
		File files[] = new File(folder).listFiles();
		
		int suffixLength = 15;
		
		Vector<String> names = new Vector<String>();
		HashMap<String,float[]> correlationProfiles = new HashMap<String,float[]>();
		
		for(File f:files)if(f.getName().endsWith(".txt"))if(f.getName().startsWith("net_")){
			String name = f.getName();
			//name = name.substring(4, name.length()-4);
			name = name.substring(4, name.length()-suffixLength);
			names.add(name);
		}
		
		HashSet<String> allgenesets = new HashSet<String>(); 
		
		for(File f:files)if(f.getName().endsWith(".txt"))if(f.getName().startsWith("net_")){
			String name = f.getName();
			name = name.substring(4, name.length()-suffixLength);
			System.out.println(name);
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(f.getAbsolutePath(), false, "\t");
			processCorrelationList(vt,f.getAbsolutePath());
			for(int i=0;i<vt.rowCount;i++){
				String g1 = vt.stringTable[i][0];
				g1 = g1.replace("$", "amp");
				g1 = g1.replace(",", "_");
				String g2 = vt.stringTable[i][1];
				g2 = g2.replace("$", "amp");
				g2 = g2.replace(",", "_");
				allgenesets.add(g1);
				allgenesets.add(g2);
				String link = g1+"__"+g2;
				if(!vt.stringTable[i][2].equals("NA")){
				float corr = Float.parseFloat(vt.stringTable[i][2]);
				corr = Math.abs(corr);
				if(corr>-1f){
					float corrs[] = correlationProfiles.get(link);
					if(corrs==null) { corrs = new float[names.size()]; }
					corrs[names.indexOf(name)] = corr;
					correlationProfiles.put(link,corrs);
				}
				}
			}
			System.out.println("Total number of gene sets: "+allgenesets.size());			
		}
		
		Vector<String> allgenesets_vector = new Vector<String>();
		for(String s: allgenesets) allgenesets_vector.add(s);
		Collections.sort(allgenesets_vector);
		
		Set<String> it = correlationProfiles.keySet();
		Vector<String> pairs = new Vector<String>();
		float avcorrs[] = new float[it.size()];
		HashMap<String,Float> correlations = new HashMap<String,Float>();
		for(String s: it){
			float corrs[] = correlationProfiles.get(s);
			float sum = 0f;
			for(int j=0;j<corrs.length;j++) sum+=corrs[j];
			sum/=(float)corrs.length;
			pairs.add(s);
			avcorrs[pairs.size()-1] = sum;
			correlations.put(s, sum);
			//fw.write(s+"\t"); for(int i=0;i<names.size();i++) fw.write(corrs[i]+"\t"); fw.write("\n");
		}
		
		int inds[] = Algorithms.SortMass(avcorrs);
		
		VDataTable vtj = VDatReadWrite.LoadFromSimpleDatFile(folder+"jaccards2.txt", false, "\t");
		vtj.makePrimaryHash(vtj.fieldNames[0]);
		
		VDataTable vtsz = VDatReadWrite.LoadFromSimpleDatFile(folder+"gs_sizes.txt", false, "\t");
		vtsz.makePrimaryHash(vtj.fieldNames[0]);
		
		
		FileWriter fwav = new FileWriter(folder+"average_correlations.txt");	
		fwav.write("GS1\tGS2\tPAIR\tAVERAGE_CORRELATION\tJACCCARD\tSIZE1\tSIZE2\tTOTALSIZE\n");
		for(int i=0;i<inds.length;i++){
			String pair = pairs.get(inds[inds.length-i-1]);
			String pair1 = Utils.replaceString(pair, "__", "&");
			String prs[] = pair1.split("&");
			String gs1 = prs[0];
			String gs2 = prs[1]; if(gs2.startsWith("_")) gs2 = gs2.substring(1, gs2.length());
			int size1 = 0;
			int size2 = 0;
			try{
				size1 = Integer.parseInt(vtsz.stringTable[vtsz.tableHashPrimary.get(gs1).get(0)][1]);
			}catch(Exception e){
				//System.out.println(gs1+" size is not found");
			}
			try{
				size2 = Integer.parseInt(vtsz.stringTable[vtsz.tableHashPrimary.get(gs2).get(0)][1]);
			}catch(Exception e){
				//System.out.println(gs2+" size is not found");
			}			
			
			float jaccard = 0f;
			if(vtj.tableHashPrimary.get(pair)!=null)
				jaccard = Float.parseFloat(vtj.stringTable[vtj.tableHashPrimary.get(pair).get(0)][1]);
			else{
				String pair_inverse = gs2+"__"+gs1;
				if(vtj.tableHashPrimary.get(pair_inverse)!=null)
					jaccard = Float.parseFloat(vtj.stringTable[vtj.tableHashPrimary.get(pair_inverse).get(0)][1]);
			}
				
			//if(jaccard>0)
			if(avcorrs[inds[inds.length-i-1]]>0.6f)
			//if(gs1.startsWith("HALLMARK")||gs2.startsWith("HALLMARK"))
			if(size1>0)if(size2>0)
				fwav.write(gs1+"\t"+gs2+"\t"+pair+"\t"+avcorrs[inds[inds.length-i-1]]+"\t"+jaccard+"\t"+size1+"\t"+size2+"\t"+(size1+size2)+"\n");
		}
		fwav.close();
		
		FileWriter fw = new FileWriter(folder+"corr_profiles.txt");
		fw.write("PAIR\t"); for(int i=0;i<names.size();i++) fw.write(names.get(i)+"\t"); fw.write("\n");
		for(int i=0;i<100000;i++){
			String pair = pairs.get(inds[inds.length-i-1]);
			float corrs[] = correlationProfiles.get(pair);
			fw.write(pair+"\t"); for(int k=0;k<names.size();k++) fw.write(corrs[k]+"\t"); fw.write("\n");
		}
		fw.close();
		
		FileWriter fwcorrm = new FileWriter(folder+"av_corr_matrix.txt");
		fwcorrm.write("GS\t"); for(int i=0;i<allgenesets_vector.size();i++) fwcorrm.write(allgenesets_vector.get(i)+"\t"); fwcorrm.write("\n");
		for(int i=0;i<allgenesets_vector.size();i++){
			String gi = allgenesets_vector.get(i);
			fwcorrm.write(gi+"\t");
			for(int j=0;j<allgenesets_vector.size();j++){
				String gj = allgenesets_vector.get(j);
				String pair1 = gi+"__"+gj;
				String pair2 = gj+"__"+gi;
				Float corr = 0f;
				if(correlations.get(pair1)!=null) corr = correlations.get(pair1);
				if(correlations.get(pair2)!=null) corr = correlations.get(pair2);
				if(gi.equals(gj)) corr = 1f;
				fwcorrm.write(corr+"\t");
			}
			fwcorrm.write("\n");
		}
		
		fwcorrm.close();

		
		
	}
	
	public static void calcGeneSetSizes(String fn){
	      GMTReader gmt = new GMTReader();
	      Vector<GESignature> sigs = gmt.readGMTDatabase(fn);
	      for(GESignature g: sigs){
	    	  System.out.println(g.name+"\t"+g.geneNames.size());
	      }

	}
	
	public static void processCorrelationList(VDataTable vt, String fn) throws Exception{
		fn = fn.substring(0, fn.length()-4);
		FileWriter fw = new FileWriter(fn+".tocytoscape");
		fw.write("GS1\tGS2\tABSCORR\n");
		Vector<String> list = new Vector<String>();
		for(int i=0;i<vt.rowCount;i++){
			if(!vt.stringTable[i][2].equals("NA")){
				float corr = Float.parseFloat(vt.stringTable[i][2]);
				fw.write(vt.stringTable[i][0]+"\t"+vt.stringTable[i][1]+"\t"+Math.abs(corr)+"\n");
				//if(!list.contains(vt.stringTable[i][0])) list.add(vt.stringTable[i][0]);
			}
		}
		fw.close();
		//Collections.sort(list);
		//fw = new FileWriter(fn+".list");
		//for(String s: list) fw.write(s+"\n");
		//fw.close();
	}
	
	public static void selectSignatures(String gmtFile, String listFileName) throws Exception{
	      GMTReader gmt = new GMTReader();
	      Vector<String> list = Utils.loadStringListFromFile(listFileName);
	      Vector<GESignature> sigs = gmt.readGMTDatabase(gmtFile);
	      Vector<String> sigNames = new Vector<String>();
	      FileWriter fw = new FileWriter(listFileName+".gmt");
	      for(GESignature g: sigs){
	    	  if(list.contains(g.name))if(!sigNames.contains(g.name)){
	    		  sigNames.add(g.name);
	    		  fw.write(g.name+"\tna\t");
	    		  for(int i=0;i<g.geneNames.size();i++)
	    			  fw.write(g.geneNames.get(i)+"\t");
	    		  fw.write("\n");
	    	  }
	      }
	      fw.close();
	}

}
