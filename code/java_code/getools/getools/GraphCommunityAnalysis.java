package getools;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import fr.curie.BiNoM.pathways.analysis.structure.Attribute;
import fr.curie.BiNoM.pathways.analysis.structure.Edge;
import fr.curie.BiNoM.pathways.analysis.structure.Graph;
import fr.curie.BiNoM.pathways.analysis.structure.GraphAlgorithms;
import fr.curie.BiNoM.pathways.analysis.structure.Node;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;

public class GraphCommunityAnalysis {
	

	public static void main(String[] args) {
		try{
			
			int sizeThreshold = 4;
			
			String year = "2013";
			
			String communityGraphFile = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/"+year+"/protein_graph/final/communities.xgmml";
			String completeGraph = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/"+year+"/protein_graph/final/hidden_lcc.xgmml";
			//String completeGraph = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/"+year+"/protein_graph/final/direct_lcc.xgmml";
			String nameHUGOConversionTable = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/"+year+"/protein_graph/ProteinsIDList"+year+"_hugo.txt";
			
			Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(communityGraphFile));
			Graph gr_complete = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(completeGraph));
			
			String folder = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/"+year+"/protein_graph/final/communities/";
			
			//listAllCommunities(gr,folder,sizeThreshold);
			
			//analyzeCommunityList(folder,nameHUGOConversionTable,completeGraph);
			
			//convertGMTtoMetageneTable(folder+"_communities_HUGO.gmt",folder+"_communities_HUGO_table.xls");

			//GraphAlgorithms.verbose = false;
			//calcPercolationThreshold(gr_complete);
			
			//Graph gr_meta = createMetanetwork(folder,completeGraph);
			//XGMML.saveToXGMML(gr_meta, folder+"_metagraph.xgmml");
			
			String gmt1 = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/final/communities/_communities_HUGO.gmt";
			String gmt2 = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/_communities_HUGO.gmt";
			
			Graph community_match_graph = MakeCommunityMatch(gmt1,gmt2,0.0001f);
			XGMML.saveToXGMML(community_match_graph, "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/match2013_2017.xgmml");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void listAllCommunities(Graph graph, String folder, int sizeThreshold) throws Exception{
		GraphAlgorithms.verbose =true;
		Vector<Graph> connComponents = GraphAlgorithms.ConnectedComponents(graph);
		FileUtils.forceMkdir(new File(folder));
		for(int i=0;i<connComponents.size();i++){
			Graph cc = connComponents.get(i);
			if(cc.Nodes.size()>=sizeThreshold){
				XGMML.saveToXGMML(cc, folder+"cc"+(i+1)+"_"+cc.Nodes.size()+".xgmml");
			}
		}
	}
	
	public static void analyzeCommunityList(String folder, String nameHUGOConversionTable, String completeGraph) throws Exception{
		FileWriter fw = new FileWriter(folder+"_sizes.txt");
		FileWriter fw_gmt = new FileWriter(folder+"_communities_HUGO.gmt");
		FileWriter fw_gmt_i = new FileWriter(folder+"_communities.gmt");
		FileWriter fw_comm = new FileWriter(folder+"_communities.txt");
		FileWriter fw_comm_HUGO = new FileWriter(folder+"_communities_HUGO.txt");
		FileWriter fw_comm_names = new FileWriter(folder+"_communities_names.txt");
		FileWriter fw_names = new FileWriter(folder+"_nodenames.txt");
		FileWriter fw_names_HUGO = new FileWriter(folder+"_nodenames_HUGO.txt");
		VDataTable conv = VDatReadWrite.LoadFromSimpleDatFile(nameHUGOConversionTable, true, "\t");
		conv.makePrimaryHash(conv.fieldNames[0]);

		Graph gr_complete = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(completeGraph));
		HashMap<String,Integer> name2ind = new HashMap<String,Integer>();
		Vector<Integer> community_inds = new Vector<Integer>();
		Vector<String> community_names = new Vector<String>();
		for(int i=0;i<gr_complete.Nodes.size();i++){
			String pn = gr_complete.Nodes.get(i).Id;
			String hugo = null;
			if(conv.tableHashPrimary.get(pn)!=null)
				hugo = conv.stringTable[conv.tableHashPrimary.get(pn).get(0)][1];
			if(hugo!=null)
				fw_names_HUGO.write(hugo+"\n");				
			else
				System.out.println(pn+": HUGO name is not found!");
			
			fw_names.write(gr_complete.Nodes.get(i).Id+"\n");
			name2ind.put(gr_complete.Nodes.get(i).Id, i);
			community_inds.add(-1);
		}
		
		File files[] = (new File(folder)).listFiles();
		fw.write("ID\tNODES\tEDGES\tNORMALIZED_DENSITY\n");
		for(File f:files)if(f.getName().startsWith("cc"))if(f.getName().endsWith("xgmml")){
			
			Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(f.getAbsolutePath()));
			String name = f.getName().substring(0,f.getName().length()-6);
			
			FileWriter fw_sif = new FileWriter(f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-6)+".sif");
			for(int j=0;j<gr.Edges.size();j++){
				fw_sif.write(gr.Edges.get(j).Node1.Id+"\tpp\t"+gr.Edges.get(j).Node2.Id+"\n");
			}
			fw_sif.close();
			
			community_names.add(name);
			fw_comm_names.write(name+"\n");
			
			float dens = (float)gr.Edges.size()/(float)(gr.Nodes.size()*gr.Nodes.size()-gr.Nodes.size());
			fw.write(name+"\t"+gr.Nodes.size()+"\t"+gr.Edges.size()+"\t"+dens+"\n");
			
			fw_gmt.write(name+"\tna\t");
			fw_gmt_i.write(name+"\tna\t");
			for(int j=0;j<gr.Nodes.size();j++){
				String pn = gr.Nodes.get(j).Id;
				String hugo = null;
				fw_gmt_i.write(pn+"\t");
				if(conv.tableHashPrimary.get(pn)!=null)
					hugo = conv.stringTable[conv.tableHashPrimary.get(pn).get(0)][1];
				if(hugo!=null)
					fw_gmt.write(hugo+"\t");
				else
					System.out.println(pn+": HUGO name is not found!");
				community_inds.set(name2ind.get(pn),community_names.size()-1);
			}
			fw_gmt.write("\n");
			fw_gmt_i.write("\n");
			
		}
		
		for(int i=0;i<community_inds.size();i++){
			fw_comm.write(community_inds.get(i)+"\n");
		}
		
		fw.close();
		fw_gmt.close();
		fw_comm.close();
		fw_names.close();
		fw_gmt_i.close();
		fw_names_HUGO.close();
		fw_comm_names.close();
	}
	
	public static void convertGMTtoMetageneTable(String gmtFile, String outFile) throws Exception{
		Vector<GESignature> gs = GMTReader.readGMTDatabase(gmtFile);
		HashSet<String> hs = new HashSet<String>();
		
		for(int i=0;i<gs.size();i++){
			GESignature g = gs.get(i);
			for(int j=0;j<g.geneNames.size();j++)
				if(!hs.contains(g.geneNames.get(j))){
					hs.add(g.geneNames.get(j));
					//System.out.println(g.geneNames.get(j));
				}
		}
		Vector<String> geneNames = new Vector<String>();
		for(String s:hs) geneNames.add(s);
		Collections.sort(geneNames);
		HashMap<String,Integer> index = new HashMap<String,Integer>();
		for(int i=0;i<geneNames.size();i++)
			index.put(geneNames.get(i),i);
		int mat[][] = new int[gs.size()][geneNames.size()];

		for(int i=0;i<gs.size();i++){
			GESignature g = gs.get(i);
			for(int j=0;j<g.geneNames.size();j++){
				String gn = g.geneNames.get(j);
				mat[i][index.get(gn)] = 1;
			}
		}
		
		FileWriter fw = new FileWriter(outFile); fw.write("GENE\t");
		for(int i=0;i<gs.size();i++) fw.write(gs.get(i).name+"\t"); fw.write("\n");
		for(int i=0;i<geneNames.size();i++){
			fw.write(geneNames.get(i)+"\t");
			for(int j=0;j<gs.size();j++){
				fw.write(mat[j][i]+"\t");
			} fw.write("\n");
		}
		fw.close();
	}
	
	public static void calcPercolationThreshold(Graph graph) throws Exception{
		int numberOfSampling = 100;
		Random r = new Random();
		System.out.println("Before "+graph.Nodes.size());
		
		/*Vector<Graph> compsGlobal = GraphAlgorithms.ConnectedComponents(graph);
		int maxsizeGlobal = -1;
		int imaxGlobal = -1;
		for(int j=0;j<compsGlobal.size();j++)
			if(maxsizeGlobal<compsGlobal.get(j).Nodes.size()){
				maxsizeGlobal = compsGlobal.get(j).Nodes.size();
				imaxGlobal = j;
		}
		graph = compsGlobal.get(imaxGlobal);*/
		
		System.out.println("After "+graph.Nodes.size());
		System.out.println("NNODES\tAV_SIZE\tPERCENTAGE_CONNECTED\tGLOBALLY_CONNECTED\tSTDEV_SIZE");
		for(int size=100;size<=graph.Nodes.size();size+=50){
			int compSize = 0;
			int compSize2 = 0;
			for(int i=0;i<numberOfSampling;i++){
				graph.selectedIds.clear();
				int j=0;
				while(j<size){
					String randomId = graph.Nodes.get(r.nextInt(graph.Nodes.size())).Id;
					if(!graph.selectedIds.contains(randomId)){
						graph.selectedIds.add(randomId);
						j++;
					}
				}Graph subnetwork = graph.getSelectedNodes();
				subnetwork.addConnections(graph);
				Vector<Graph> comps = GraphAlgorithms.ConnectedComponents(subnetwork);
				int maxsize = -1;
				for(j=0;j<comps.size();j++)
					if(maxsize<comps.get(j).Nodes.size())
						maxsize = comps.get(j).Nodes.size();
				compSize+=maxsize;
				compSize2+=maxsize*maxsize;
			}
			float averageSize = (float)compSize/(float)numberOfSampling;
			float stdSize = (float)Math.sqrt((float)compSize2/(float)numberOfSampling-averageSize*averageSize);
			System.out.println(size+"\t"+averageSize+"\t"+averageSize/size+"\t"+averageSize/graph.Nodes.size()+"\t"+stdSize);
		}
	}
	
	
	public static Graph createMetanetwork(String folder, String completeGraph) throws Exception{
		Graph gr = new Graph();
		Vector<GESignature> gmt = GMTReader.readGMTDatabase(folder+"_communities.gmt");
		for(int i=0;i<gmt.size();i++){
			GESignature ge = gmt.get(i);
			Node n = gr.getCreateNode(ge.name);
			n.setAttributeValueUnique("NUMBER_OF_NODES", ""+ge.geneNames.size(), Attribute.ATTRIBUTE_TYPE_STRING);
		}
		HashSet<String> interactions = new HashSet<String>();
		Graph gr_complete = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(completeGraph));
		for(int i=0;i<gr_complete.Edges.size();i++){
			String id = gr_complete.Edges.get(i).Node1.Id+"~"+gr_complete.Edges.get(i).Node2.Id;
			interactions.add(id);
		}
		for(int i=0;i<gmt.size();i++)for(int j=0;j<gmt.size();j++)if(i!=j){
			Node ni = gr.Nodes.get(i);
			Node nj = gr.Nodes.get(j);
			int number_ij = 0;
			int number_ji = 0;
			for(int ii=0;ii<gmt.get(i).geneNames.size();ii++)
				for(int jj=0;jj<gmt.get(j).geneNames.size();jj++){
					String namei = gmt.get(i).geneNames.get(ii);
					String namej = gmt.get(j).geneNames.get(jj);
					String id = namei+"~"+namej;
					if(interactions.contains(id)) number_ij++;
					id = namej+"~"+namei;
					if(interactions.contains(id)) number_ji++;
				}
			Edge e = null;
			
			if(number_ij>0){
			e = gr.getCreateEdge(ni.Id+"__"+nj.Id);
			e.Node1 = ni;
			e.Node2 = nj;
			e.setAttributeValueUnique("NUMBER_OF_LINKS", ""+number_ij, Attribute.ATTRIBUTE_TYPE_REAL);
			e.setAttributeValueUnique("RELATIVE_NUMBER_OF_LINKS", ""+(float)number_ij/(float)gmt.get(i).geneNames.size(), Attribute.ATTRIBUTE_TYPE_REAL);
			}
			
			if(number_ji>0){
			e = gr.getCreateEdge(nj.Id+"__"+ni.Id);
			e.Node1 = nj;
			e.Node2 = ni;
			e.setAttributeValueUnique("NUMBER_OF_LINKS", ""+number_ji, Attribute.ATTRIBUTE_TYPE_REAL);
			e.setAttributeValueUnique("RELATIVE_NUMBER_OF_LINKS", ""+(float)number_ji/(float)gmt.get(j).geneNames.size(), Attribute.ATTRIBUTE_TYPE_REAL);
			}

		}
		return gr;
	}

	public static Graph MakeCommunityMatch(String gmt1, String gmt2, float threshold){
		Graph gr = new Graph();
		Vector<GESignature> gr1 = GMTReader.readGMTDatabase(gmt1);
		Vector<GESignature> gr2 = GMTReader.readGMTDatabase(gmt2);
		for(int i=0;i<gr1.size();i++){
			Node n = gr.getCreateNode("2013_"+gr1.get(i).name);
			n.setAttributeValueUnique("YEAR", "2013", Attribute.ATTRIBUTE_TYPE_STRING);
			n.setAttributeValueUnique("NUMBER_OF_NODES", ""+gr1.get(i).geneNames.size(), Attribute.ATTRIBUTE_TYPE_REAL);
		}
		for(int i=0;i<gr2.size();i++){
			Node n = gr.getCreateNode("2017_"+gr2.get(i).name);
			n.setAttributeValueUnique("YEAR", "2017", Attribute.ATTRIBUTE_TYPE_STRING);
			n.setAttributeValueUnique("NUMBER_OF_NODES", ""+gr2.get(i).geneNames.size(), Attribute.ATTRIBUTE_TYPE_REAL);
		}
		
		for(int i=0;i<gr1.size();i++){
			Node ni = gr.getCreateNode("2013_"+gr1.get(i).name);
			for(int j=0;j<gr2.size();j++){
				Node nj = gr.getCreateNode("2017_"+gr2.get(j).name);
				HashSet<String> seti = new HashSet<String>();
				HashSet<String> setj = new HashSet<String>();
				for(int k=0;k<gr1.get(i).geneNames.size();k++) seti.add(gr1.get(i).geneNames.get(k));
				for(int k=0;k<gr2.get(j).geneNames.size();k++) setj.add(gr2.get(j).geneNames.get(k));
				int intersection = (Utils.IntersectionOfSets(seti, setj)).size();
				int union = (Utils.UnionOfSets(seti, setj)).size();
				float jaccard = (float)intersection/(float)union;
				if(jaccard>threshold){
					Edge e = gr.getCreateEdge(ni.Id+"__"+nj.Id);
					e.Node1 = ni;
					e.Node2 = nj;
					e.setAttributeValueUnique("JACCARD", ""+(float)intersection/(float)union, Attribute.ATTRIBUTE_TYPE_REAL);
					e.setAttributeValueUnique("INTERSECTION", ""+(float)intersection, Attribute.ATTRIBUTE_TYPE_REAL);
					e.setAttributeValueUnique("UNION", ""+(float)union, Attribute.ATTRIBUTE_TYPE_REAL);
				}
			}
		}
		
		gr.calcNodesInOut();
		
		for(int i=0;i<gr.Nodes.size();i++){
			float maxjaccard = 0f;
			Node max_node = null;
			Edge max_edge = null;
			Node n = gr.Nodes.get(i);
			for(int j=0;j<n.outcomingEdges.size();j++){
				float jac = 0f;
				Edge e = n.outcomingEdges.get(j);
				if(e.getFirstAttributeValue("JACCARD")!=null)
					jac = Float.parseFloat(e.getFirstAttributeValue("JACCARD"));
				if(jac>maxjaccard){
					maxjaccard = jac;
					max_node = e.Node2;
					max_edge = e;
				}
			}
			for(int j=0;j<n.incomingEdges.size();j++){
				float jac = 0f;
				Edge e = n.incomingEdges.get(j);
				if(e.getFirstAttributeValue("JACCARD")!=null)
					jac = Float.parseFloat(e.getFirstAttributeValue("JACCARD"));
				if(jac>maxjaccard){
					maxjaccard = jac;
					max_node = e.Node1;
					max_edge = e;
				}
			}
			if(max_node!=null){
				float maxjaccard2 = 0f;
				Node max_node2 = null;
				for(int j=0;j<max_node.outcomingEdges.size();j++){
					float jac = 0f;
					Edge e = max_node.outcomingEdges.get(j);
					if(e.getFirstAttributeValue("JACCARD")!=null)
						jac = Float.parseFloat(e.getFirstAttributeValue("JACCARD"));
					if(jac>maxjaccard2){
						maxjaccard2 = jac;
						max_node2 = e.Node2;
					}
				}
				for(int j=0;j<max_node.incomingEdges.size();j++){
					float jac = 0f;
					Edge e = max_node.incomingEdges.get(j);
					if(e.getFirstAttributeValue("JACCARD")!=null)
						jac = Float.parseFloat(e.getFirstAttributeValue("JACCARD"));
					if(jac>maxjaccard2){
						maxjaccard2 = jac;
						max_node2 = e.Node1;
					}
				}
				
				if(n.Id.equals(max_node2.Id)){
					max_edge.setAttributeValueUnique("RECIPROCAL_MAX", "TRUE", Attribute.ATTRIBUTE_TYPE_STRING);
				}
				
			}
			
		}
			
		return gr;
	}
	
}
