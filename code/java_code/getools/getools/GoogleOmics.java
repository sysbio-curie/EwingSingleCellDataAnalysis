package getools;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.text.html.CSS;

import org.apache.commons.io.FileUtils;

import fr.curie.BiNoM.pathways.analysis.structure.Attribute;
import fr.curie.BiNoM.pathways.analysis.structure.Edge;
import fr.curie.BiNoM.pathways.analysis.structure.Graph;
import fr.curie.BiNoM.pathways.analysis.structure.GraphAlgorithms;
import fr.curie.BiNoM.pathways.analysis.structure.Node;
import fr.curie.BiNoM.pathways.analysis.structure.StructureAnalysisUtils;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VDownloader;

import com.sun.image.codec.jpeg.*;


public class GoogleOmics extends Applet{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{

			formatCellDesignerNotes();
			//changeProteinNamesInCellDesignerFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/navicell/wikiproteinmap_master.xml","C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/navicell/materials/wikiprotein_communities_commnames.gmt");
			//replaceIdsInCommunityGMTFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/wikiprotein_communities.gmt","C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/wikiprotein_communities_annotations.gmt","C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/","C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/ProteinsIDList2017_hugo.txt");
			//addCommunityNamesToHeader("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/_communities_HUGO_table.xls","C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/wikiprotein_communities_commnames.gmt");
			System.exit(0);
			
			//extractPageRanks("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Gqr_complete_matrix.txt");
			
			String fn_direct = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/dir_net_2017.txt";
			String fn_hidden = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/hidden_0.00173_2017.txt";
			 
			String conversionTable = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/ProteinsIDList2017_hugo.txt";
			
			String fn_direct2013 = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/final/dir_net_2013.txt";
			String fn_direct2017 = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/dir_net_2017.txt";

			String fn_hidden2013 = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/final/hidden_0.002217_2013.txt ";
			String fn_hidden2017 = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/hidden_0.00173_2017.txt";
			
			/*Graph graph_direct2013 = loadGraphFromTxtFile(fn_direct2013);
			Graph graph_direct2017 = loadGraphFromTxtFile(fn_direct2017);
			System.out.println("Graph direct 2013:"+graph_direct2013.Nodes.size()+":"+graph_direct2013.Edges.size());
			System.out.println("Graph direct 2017:"+graph_direct2017.Nodes.size()+":"+graph_direct2017.Edges.size());
			countCommonEdges(graph_direct2013,graph_direct2017);*/
			Graph graph_hidden2013 = loadGraphFromTxtFile(fn_hidden2013);
			Graph graph_hidden2017 = loadGraphFromTxtFile(fn_hidden2017);
			System.out.println("Graph hidden 2013:"+graph_hidden2013.Nodes.size()+":"+graph_hidden2013.Edges.size());
			System.out.println("Graph hidden 2017:"+graph_hidden2017.Nodes.size()+":"+graph_hidden2017.Edges.size());
			countCommonEdges(graph_hidden2013,graph_hidden2017);
			
			
			//convertWikiGraph2HUGO(fn,conversionTable);
			
			//String fn = fn_hidden;
			//String fn = fn_direct;
			
			//Graph graph = loadGraphFromTxtFile(fn+".HUGO.txt");
			//Graph graph_direct = loadGraphFromTxtFile(fn_direct+".HUGO.txt");
			//Graph graph_hidden = loadGraphFromTxtFile(fn_hidden+".HUGO.txt");
			
			//Graph graph = graph_direct;
			//Graph graph = graph_hidden;
			
			//graph.addNodes(graph_hidden);
			//graph.addEdges(graph_hidden);
			
			//Graph pathDB = loadGraphFromSifFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/pathway_database/signor_ppi.sif");
			//Graph pathDB = loadGraphFromSifFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/pathway_database/PathwayCommons11.All.hgnc.sif");
			
			//pathDB = filterPathDB(pathDB,graph_direct);
			
			//countCommonEdges(graph,pathDB);
			
			
			//compareConnectivities(graph,pathDB);
			
			
			//MatchPageTitleHUGOName("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/ProteinsIDList2017.txt","C:/Datas/acsioma/conversions/HUGO_HGNC_Human.txt");
			//FindEntrezIDFromProteinWikiTitle("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/ProteinsIDList2017.txt.unmatched");
			//FindEntrezIDFromProteinWikiTitle("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/temp");
			
			//RemoveHeaderFromTheFullGraphFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/full_graph/enwiki2017.txt");
			 
			/*String text = Utils.loadString("C:/Datas/RussianScientists/experts2017.txt");
			String textenc = encodeRussianText(text);
			System.out.println(textenc);*/
			
			/*ProcessDatFile("C://Datas//Googlomics//WikiProteins//work//Gqr_Enwiki_ProteinsIDList_4899.dat");
			System.exit(0);*/
			
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Gqr_complete_matrix.txt",0.00173f,"Myoglobin");
			
			//ExtractLCCFromGraph("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/networks/dir_net_2017.txt");
			//ExtractLCCFromGraph("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/networks/dir_net_2013.txt");
			//ExtractLCCFromGraph("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/networks/hidden_0.002217 _2013.txt");
			//ExtractLCCFromGraph("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/networks/hidden_0.00173_2017.txt");
			//ComputeGraphDensity("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/networks/dir_net_2013.txt");

			// Extract network of direct interactions
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Grr_complete_matrix2017.dat",0.00001f);
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/Grr_Enwiki_ProteinsIDList_4899.dat",0.00001f);
			//
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/Gqr_complete_matrix.txt",0.001888f);
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/Gqr_complete_matrix.txt",0.005f);
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/Gqr_complete_matrix.txt",0.0018f);
			

			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Grr_complete_matrix2017.dat",0.00001f);
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Gqr_complete_matrix2017.dat",0.0015503050f);
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Gqr_complete_matrix2017.dat",0.005f);

			// Extract network of hidden interactions 
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/Gqr_complete_matrix2017.dat",0.0015503050f	);
			//ProcessDatFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2013/protein_graph/Gqr_complete_matrix.txt",0.005f);
			//ConvertDat2TxtFile("C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/hidden_0.001550305_2017.dat");
			
			System.exit(0);
			
			
			//String text = Utils.loadString("C:/Datas/Googlomics/poets/network/network_indirect_Gqr.txt");
			//String text = Utils.loadString("C:/Datas/Googlomics/poets/network/subpagerank_small_poets_RU_Ruwiki_0.15.dat");
			//String text = Utils.loadString("C:/Datas/Googlomics/poets/network/network_direct_Grr.txt");
			//String text = Utils.loadString("C:/Datas/Googlomics/poets/network/alphabet.txt");
			//String textenc = encodeRussianText(text);
			//System.out.println(textenc)	;
			/*File files[] = new File("C:/Datas/Googlomics/poets/network/photos/copy/").listFiles();
			for(File f: files){
				if(f.getName().endsWith(".jpg")|f.getName().endsWith(".jpeg")|f.getName().endsWith(".gif")|f.getName().endsWith(".png")|f.getName().endsWith(".JPG")){
					String newName = encodeRussianText(f.getAbsolutePath());
					FileUtils.copyFile(f,new File(newName));
				}
			}*/
			
			/*(new GoogleOmics()).MakePoetMapImages("C:/Datas/Googlomics/poets/network/networks/direct_only.xgmml","C:/Datas/Googlomics/poets/network/photos/",3,8990,5888);
			(new GoogleOmics()).MakePoetMapImages("C:/Datas/Googlomics/poets/network/networks/direct_only.xgmml","C:/Datas/Googlomics/poets/network/photos/",2,8990,5888);
			(new GoogleOmics()).MakePoetMapImages("C:/Datas/Googlomics/poets/network/networks/direct_only.xgmml","C:/Datas/Googlomics/poets/network/photos/",1,8990,5888);
			(new GoogleOmics()).MakePoetMapImages("C:/Datas/Googlomics/poets/network/networks/direct_only.xgmml","C:/Datas/Googlomics/poets/network/photos/",0,8990,5888);
			 */
			
			//extractGeneNetworkFromWiki(true);
			
			//MakeAdjacencyMatrixFromSif("C:/Datas/Googlomics/GRN_project/GRN/signor.sif");
			//MakeAdjacencyMatrixFromSif("C:/Datas/Googlomics/GRN_project/GRN/GM_SIGNOR.sif");
			//MakeAdjacencyMatrixFromSif("C:/Datas/Googlomics/GRN_project/work/comparison_to_other_centrality/K562_SIGNOR.sif"); 
			
			
			//System.exit(0);
			//countConnectivityDegrees("C:/Datas/Googlomics/GRN_project/v3/GRN/SIGNOR.sif","C:/Datas/Googlomics/GRN_project/v3/GRN/SIGNOR.connectivity");
			//countConnectivityDegrees("C:/Datas/Googlomics/GRN_project/v3/GRN/K562_SIGNOR.sif","C:/Datas/Googlomics/GRN_project/v3/GRN/K562_SIGNOR.connectivity");
			//countConnectivityDegrees("C:/Datas/Googlomics/GRN_project/v3/GRN/GM_SIGNOR.sif","C:/Datas/Googlomics/GRN_project/v3/GRN/GM_SIGNOR.connectivity");
			//compileCompleteTableOfRanks("C:/Datas/Googlomics/GRN_project/v3/");
			
			//classifyProteinsByNetworks("C:/Datas/Googlomics/GRN_project/v3/GRN/");
			
			//compileReducedNetworks("C:/Datas/Googlomics/GRN_project/v3/","aktmtor","PageRank");
			//compileReducedNetworks("C:/Datas/Googlomics/GRN_project/v3/","cit7","PageRank");
			//compileReducedNetworks("C:/Datas/Googlomics/GRN_project/v3/","e2f1targets","PageRank");
			
			/*VDataTable sif = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Googlomics/signor.sif", false, "\t");
			VDataTable hugos = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/Googlomics/hugos.txt", true, "\t");
			hugos.makePrimaryHash("symbol");
			
			HashMap<String, Integer> counts = new HashMap<String, Integer>();
			
			FileWriter sif_f = new FileWriter("C:/Datas/Googlomics/signor_ppi.sif");
			for(int i=0;i<sif.rowCount;i++){
				String interactor1 = sif.stringTable[i][0];
				String interaction = sif.stringTable[i][1];
				
				Integer count = counts.get(interaction);
				if(count==null){
					count =0;
				}
				counts.put(interaction, count+1);
				
				String interactor2 = sif.stringTable[i][2];
				if(hugos.tableHashPrimary.get(interactor1)!=null)
					if(hugos.tableHashPrimary.get(interactor2)!=null)
						sif_f.write(interactor1+"\t"+interaction+"\t"+interactor2+"\n");
			}
			sif_f.close();
			
			for(String s: counts.keySet())
				System.out.println(s+"\t"+counts.get(s));
				
			*/
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private static void RemoveHeaderFromTheFullGraphFile(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		FileWriter fw = new FileWriter(fn+".noheader");
		String s = null;
		String first = lr.readLine(); System.out.println(first);
		String second = lr.readLine(); System.out.println(second);
		while((s=lr.readLine())!=null){
			fw.write(s+"\n");
		}
		fw.close();
	}

	private static void ConvertDat2TxtFile(String fn) throws Exception{
		// TODO Auto-generated method stub
		String fn1 = fn.substring(0, fn.length()-4)+".txt";
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		FileWriter fw = new FileWriter(fn1);
		lr.readLine();
		while((s=lr.readLine())!=null){
			String parts[] = s.split("\t");
			fw.write(parts[3]+"\t"+parts[4]+"\t"+parts[2]+"\n");
		}
		fw.close();
		lr.close();
	}

	private static void ProcessDatFile(String datFile, float threshold, String exceptionTarget) throws Exception{
		// TODO Auto-generated method stub
		
		//FileWriter fw = new FileWriter(datFile+".values");
		FileWriter fw1 = new FileWriter(datFile+".filtered1");
		FileWriter fw2 = new FileWriter(datFile+".txt");
		fw1.write("PR1\tPR2\tWEIGHT\tTERM1\tTERM2\n");
		fw2.write("SOURCE\tTARGET\tWEIGHT\n");
		
		LineNumberReader lr = new LineNumberReader(new FileReader(datFile));
		String s = null;
		while((s=lr.readLine())!=null)if(!s.trim().equals("")){
			String parts[] = s.split("\t");
			float val = Float.parseFloat(parts[2]);
			if(val>threshold)if(!parts[0].equals(parts[1]))if(!parts[3].equals(exceptionTarget)){
				fw1.write(parts[0]+"\t"+parts[1]+"\t"+parts[2]+"\t"+parts[3]+"\t"+parts[4]+"\n");
				fw2.write(parts[4]+"\t"+parts[3]+"\t"+parts[2]+"\n");
			}
		}
		lr.close();
		//fw.close();
		fw1.close();
		fw2.close();
		
	}

	private void MakePoetMapImages(String networkfile, String photoFolder, int level, int width, int height) throws Exception{
		Graph network = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(networkfile));
		// 8990 x 5888
		
	    BufferedImage thumbImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = thumbImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    
	    graphics2D.setColor(new Color(28,28,28));
	    graphics2D.setBackground(new Color(28,28,28));
	    graphics2D.fillRect(0, 0, width, height);
	    
	    float minx = 1000, maxx = -1000, miny = 1000, maxy = -1000; 
	    for(Node n: network.Nodes){
	    	float x = n.x;
	    	float y = n.y;
	    	if(x<minx) minx=x;
	    	if(x>maxx) maxx=x;
	    	if(y<miny) miny=y;
	    	if(y>maxy) maxy=y;
	    }

	    float avnormvals[] = new float[4];
	    int countlevel[] = new int[4];
	    for(Node n: network.Nodes){
	    	int lev = Integer.parseInt(n.getFirstAttributeValue("LEVEL"));
	    	float normval = Float.parseFloat(n.getFirstAttributeValue("NORM_VAL"));
	    	countlevel[lev]++;
	    	avnormvals[lev]+=normval;
	    }
	    for(int i=0;i<4;i++) avnormvals[i]/=(float)countlevel[i];
	    
	    
	    for(Edge e: network.Edges){
	    	graphics2D.setColor(new Color(200,100,180));
	    	float x1 = e.Node1.x;
	    	float y1 = e.Node1.y;
	    	x1 = (x1-minx)/(maxx-minx);
	    	y1 = (y1-miny)/(maxy-miny);
	    	x1 = x1*width*0.93f;
	    	y1 = y1*height*0.93f;
	    	float x2 = e.Node2.x;
	    	float y2 = e.Node2.y;
	    	x2 = (x2-minx)/(maxx-minx);
	    	y2 = (y2-miny)/(maxy-miny);
	    	x2 = x2*width*0.93f;
	    	y2 = y2*height*0.93f;

	    	String name1 = e.Node1.getFirstAttributeValue("NAME");
	    	String name2 = e.Node2.getFirstAttributeValue("NAME");
	    	
	    	Toolkit toolkit = Toolkit.getDefaultToolkit();
	    	Image im1 = toolkit.getImage(photoFolder+name1+".jpg");
	    	Image im2 = toolkit.getImage(photoFolder+name2+".jpg");
	    	MediaTracker mediaTracker = new MediaTracker(this);
	    	mediaTracker.addImage(im1, 0);
	    	mediaTracker.waitForID(0);
	    	mediaTracker.addImage(im2, 1);
	    	mediaTracker.waitForID(1);
	    	
	    	graphics2D.setStroke(new BasicStroke(15));
	    	
	    	int lev1 = Integer.parseInt(e.Node1.getFirstAttributeValue("LEVEL"));
	    	int lev2 = Integer.parseInt(e.Node2.getFirstAttributeValue("LEVEL"));
	    	float normval1 = Float.parseFloat(e.Node1.getFirstAttributeValue("NORM_VAL"));
	    	float normval2 = Float.parseFloat(e.Node2.getFirstAttributeValue("NORM_VAL"));

	    	
	    	float imwidth1 = (float)im1.getWidth(this);
	    	float imheight1 = (float)im1.getHeight(this);
	    	imheight1 *= 200f/imwidth1;
	    	imwidth1 = 200f;
	    	float scale1 = 1f*(float)(4-lev1);
	    	if(normval1/avnormvals[lev1]>1f) scale1 +=Math.min(normval1/avnormvals[lev1]-1f,1.5f);
	    	if(normval1/avnormvals[lev1]<1f) scale1 +=Math.max(normval1/avnormvals[lev1]-1f,-0.5f);
	    	
	    	
	    	float imwidth2 = (float)im2.getWidth(this);
	    	float imheight2 = (float)im2.getHeight(this);
	    	imheight2 *= 200f/imwidth2;
	    	imwidth2 = 200f;
	    	float scale2 = 1f*(float)(4-lev2);
	    	imwidth2*=scale2;
	    	imheight2*=scale2;

	    	graphics2D.drawLine((int)x1+(int)(0.5f*imwidth1), (int)y1+(int)(0.5f*imheight1), (int)x2+(int)(0.5f*imwidth2), (int)y2+(int)(0.5f*imheight2));
	    }
	    
	    
	    for(int i=3;i>=0;i--)
	    for(Node n: network.Nodes){

	    	int lev = Integer.parseInt(n.getFirstAttributeValue("LEVEL"));
	    	float normval = Float.parseFloat(n.getFirstAttributeValue("NORM_VAL"));
	    	
	    	if(lev==i){
	    	float x = n.x;
	    	float y = n.y;
	    	x = (x-minx)/(maxx-minx);
	    	y = (y-miny)/(maxy-miny);
	    	x = x*width*0.93f;
	    	y = y*height*0.93f;
	    	
	    	String name = n.getFirstAttributeValue("NAME");
	    	
	    	String familyname = name.split(",")[0];
	    
	    	Toolkit toolkit = Toolkit.getDefaultToolkit();
	    	Image im = toolkit.getImage(photoFolder+name+".jpg");
	    	
	    	if(!(new File(photoFolder+name+".jpg").exists()))
	    		System.out.println(name+" is not found!");
	    	
	    	MediaTracker mediaTracker = new MediaTracker(this);
	    	mediaTracker.addImage(im, 0);
	    	mediaTracker.waitForID(0);

	    	float imwidth = (float)im.getWidth(this);
	    	float imheight = (float)im.getHeight(this);
	    	imheight *= 200f/imwidth;
	    	imwidth = 200f;
	    	
	    	float scale = 1f*(float)(4-lev);
	    	if(normval/avnormvals[lev]>1f) scale +=Math.min(normval/avnormvals[lev]-1f,1.5f);
	    	if(normval/avnormvals[lev]<1f) scale +=Math.max(normval/avnormvals[lev]-1f,-0.5f);

	    	if(level==3) { scale*=0.5f; if(imwidth*scale<200f) scale=1f; }
	    	if(level==2) { scale*=0.7f; if(imwidth*scale<150f) scale=1f; }
	    	if(level==1) { scale*=0.8f; if(imwidth*scale<100f) scale=1f; }
	    	
	    	imwidth*=scale;
	    	imheight*=scale;
	    	
	    	
	    	graphics2D.drawImage(im, (int)x, (int)y, (int)imwidth, (int)imheight, null);

	    	Font font = new Font("Serif", Font.BOLD, (int)(50f*scale));
	    	
	    	graphics2D.setFont(font);
	    	
		    graphics2D.setColor(new Color(188,184,0));
	    	graphics2D.drawString(familyname, (int)x, (int)(y+imheight+70));
	    	}
	    }
	    
	    /*BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(networkfile+"_level_"+level+".jpg"));
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
	    int quality = 85;
	    param.setQuality((float)quality / 100.0f, false);
	    encoder.setJPEGEncodeParam(param);
	    encoder.encode(thumbImage);
	    out.close(); 
	    */
		
	}

	private static String encodeRussianText(String textRussian) {
		StringBuffer textenc = new StringBuffer();//new String(textRussian);
		char cc[] = textRussian.toCharArray();
		for(int i=0;i<textRussian.length();i++){
			//System.out.println((int)cc[i]);
			switch(cc[i]){
			 case 848+224: textenc.append('a'); break;
			 case 848+225: textenc.append('b'); break;
			 case 848+226: textenc.append('v'); break;
			 case 848+227: textenc.append('g'); break;
			 case 848+228: textenc.append('d'); break;
			 case 848+229: textenc.append('e'); break;
			 case 848+230: textenc.append("zh"); break;
			 case 848+231: textenc.append('z'); break;
			 case 848+232: textenc.append('i'); break;
			 case 848+233: textenc.append('y'); break;
			 case 848+234: textenc.append('k'); break;
			 case 848+235: textenc.append('l'); break;
			 case 848+236: textenc.append('m'); break;
			 case 848+237: textenc.append('n'); break;
			 case 848+238: textenc.append('o'); break;
			 case 848+239: textenc.append('p'); break;
			 case 848+240: textenc.append('r'); break;
			 case 848+241: textenc.append('s'); break;
			 case 848+242: textenc.append('t'); break;
			 case 848+243: textenc.append('u'); break;
			 case 848+244: textenc.append('f'); break;
			 case 848+245: textenc.append('h'); break;
			 case 848+246: textenc.append("ts"); break;
			 case 848+247: textenc.append("ch"); break;
			 case 848+248: textenc.append("sh"); break;
			 case 848+249: textenc.append("sch"); break;
			 case 848+250: textenc.append(""); break;
			 case 848+251: textenc.append("i"); break;			 
			 case 848+252: textenc.append(""); break;
			 case 848+253: textenc.append('e'); break;
			 case 848+254: textenc.append("yu"); break;
			 case 848+255: textenc.append("ya"); break;
			 
			 case 816+224: textenc.append('A'); break;
			 case 816+225: textenc.append('B'); break;
			 case 816+226: textenc.append('V'); break;
			 case 816+227: textenc.append('G'); break;
			 case 816+228: textenc.append('D'); break;
			 case 816+229: textenc.append('E'); break;
			 case 816+230: textenc.append("Zh"); break;
			 case 816+231: textenc.append('Z'); break;
			 case 816+232: textenc.append('I'); break;
			 case 816+233: textenc.append('Y'); break;
			 case 816+234: textenc.append('K'); break;
			 case 816+235: textenc.append('L'); break;
			 case 816+236: textenc.append('M'); break;
			 case 816+237: textenc.append('N'); break;
			 case 816+238: textenc.append('O'); break;
			 case 816+239: textenc.append('P'); break;
			 case 816+240: textenc.append('R'); break;
			 case 816+241: textenc.append('S'); break;
			 case 816+242: textenc.append('T'); break;
			 case 816+243: textenc.append('U'); break;
			 case 816+244: textenc.append('F'); break;
			 case 816+245: textenc.append('H'); break;
			 case 816+246: textenc.append("Ts"); break;
			 case 816+247: textenc.append("Ch"); break;
			 case 816+248: textenc.append("Sh"); break;
			 case 816+249: textenc.append("Sch"); break;
			 case 816+250: textenc.append(""); break;
			 case 816+251: textenc.append("I"); break;			 
			 case 816+252: textenc.append(""); break;
			 case 816+253: textenc.append('E'); break;
			 case 816+254: textenc.append("Yu"); break;
			 case 816+255: textenc.append("Ya"); break;
			 
			 case 1105: textenc.append("yo"); break;
			 case 1025: textenc.append("Yo"); break;
			 
			 
			 default: textenc.append(cc[i]); break;
			 }
			 //System.out.print(cc[i]);
		}
		return textenc.toString();
	}

	public static Graph loadGraphFromSifFile(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		Graph gr = new Graph();
		String s = null;
		while((s=lr.readLine())!=null){
			String parts[] = s.split("\t");
			String name1 = parts[0];
			String name2 = parts[2];
			Node n1 = gr.getCreateNode(name1);
			Node n2 = gr.getCreateNode(name2);
			Edge e = new Edge();
			e.Id = n1.Id+"__"+n2.Id;
			e.Node1 = n1;
			e.Node2 = n2;
			gr.addEdge(e);
			e.setAttributeValueUnique("TYPE", parts[1], Attribute.ATTRIBUTE_TYPE_STRING);
		}
		lr.close();
		return gr;
	}
	
	public static void countConnectivityDegrees(String fn, String outfile) throws Exception{
		Graph gr = loadGraphFromSifFile(fn);
		gr.calcNodesInOut();
		FileWriter fw = new FileWriter(outfile);
		for(int i=0;i<gr.Nodes.size();i++)
			fw.write(gr.Nodes.get(i).Id+"\t"+gr.Nodes.get(i).incomingEdges.size()+"\t"+gr.Nodes.get(i).outcomingEdges.size()+"\t"+(gr.Nodes.get(i).incomingEdges.size()+gr.Nodes.get(i).outcomingEdges.size())+"\n");
		fw.close();
	}
	
	public static void compileCompleteTableOfRanks(String folder) throws Exception{
		FileWriter fw = new FileWriter(folder+"completetable.txt");
		fw.write("PROTEIN\tSIGNOR_IN\tSIGNOR_OUT\tSIGNOR_CONN\t");
		fw.write("SIGNOR_PAGERANK\tSIGNOR_PAGERANKVAL\tSIGNOR_CHEIRANK\tSIGNOR_CHEIRANKVAL\tSIGNOR_RANK2D\t");
		fw.write("GM_SIGNOR_IN\tGM_SIGNOR_OUT\tGM_SIGNOR_CONN\t");
		fw.write("GM_SIGNOR_PAGERANK\tGM_SIGNOR_PAGERANKVAL\tGM_SIGNOR_CHEIRANK\tGM_SIGNOR_CHEIRANKVAL\tGM_SIGNOR_RANK2D\t");
		fw.write("K562_SIGNOR_IN\tK562_SIGNOR_OUT\tK562_SIGNOR_CONN\t");
		fw.write("K562_SIGNOR_PAGERANK\tK562_SIGNOR_PAGERANKVAL\tK562_SIGNOR_CHEIRANK\tK562_SIGNOR_CHEIRANKVAL\tK562_SIGNOR_RANK2D\n");
		
		VDataTable signor_conn = VDatReadWrite.LoadFromSimpleDatFile(folder+"GRN/signor.connectivity", false, "\t");
		signor_conn.makePrimaryHash("N1");
		VDataTable gm_conn = VDatReadWrite.LoadFromSimpleDatFile(folder+"GRN/GM_SIGNOR.connectivity", false, "\t");
		gm_conn.makePrimaryHash("N1");
		VDataTable k562_conn = VDatReadWrite.LoadFromSimpleDatFile(folder+"GRN/K562_SIGNOR.connectivity", false, "\t");
		k562_conn.makePrimaryHash("N1");
		
		correctTheDataTable(folder+"signor/rankpr.dat",2);
		correctTheDataTable(folder+"GM/rankpr.dat",2);
		correctTheDataTable(folder+"K562/rankpr.dat",2);
		correctTheDataTable(folder+"signor/rankcr.dat",2);
		correctTheDataTable(folder+"GM/rankcr.dat",2);
		correctTheDataTable(folder+"K562/rankcr.dat",2);
		correctTheDataTable(folder+"signor/rank2d.dat",3);
		correctTheDataTable(folder+"GM/rank2d.dat",3);
		correctTheDataTable(folder+"K562/rank2d.dat",3);
		
		
		VDataTable signor_page = VDatReadWrite.LoadFromSimpleDatFile(folder+"signor/rankpr.dat_", false, "\t");
		signor_page.makePrimaryHash("N3");
		VDataTable gm_page = VDatReadWrite.LoadFromSimpleDatFile(folder+"GM/rankpr.dat_", false, "\t");
		gm_page.makePrimaryHash("N3");
		VDataTable k562_page = VDatReadWrite.LoadFromSimpleDatFile(folder+"K562/rankpr.dat_", false, "\t");
		k562_page.makePrimaryHash("N3");
		
		VDataTable signor_chei = VDatReadWrite.LoadFromSimpleDatFile(folder+"signor/rankcr.dat_", false, "\t");
		signor_chei.makePrimaryHash("N3");
		VDataTable gm_chei = VDatReadWrite.LoadFromSimpleDatFile(folder+"GM/rankcr.dat_", false, "\t");
		gm_chei.makePrimaryHash("N3");
		VDataTable k562_chei = VDatReadWrite.LoadFromSimpleDatFile(folder+"K562/rankcr.dat_", false, "\t");
		k562_chei.makePrimaryHash("N3");
		
		VDataTable signor_2d = VDatReadWrite.LoadFromSimpleDatFile(folder+"signor/rank2d.dat_", false, "\t");
		signor_2d.makePrimaryHash("N4");
		VDataTable gm_2d = VDatReadWrite.LoadFromSimpleDatFile(folder+"GM/rank2d.dat_", false, "\t");
		gm_2d.makePrimaryHash("N4");
		VDataTable k562_2d = VDatReadWrite.LoadFromSimpleDatFile(folder+"K562/rank2d.dat_", false, "\t");
		k562_2d.makePrimaryHash("N4");
		
		Vector<String> allGenes = new Vector<String>();
		for(String s: signor_conn.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: gm_conn.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: k562_conn.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: signor_page.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: gm_page.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: k562_page.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: signor_chei.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: gm_chei.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: k562_chei.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: signor_2d.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: gm_2d.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		for(String s: k562_2d.tableHashPrimary.keySet()) if(!allGenes.contains(s)) allGenes.add(s);
		
		Collections.sort(allGenes);
		System.out.println(allGenes.size()+" genes found.");
		
		int k=0; VDataTable t = null;
		for(String gene: allGenes){
			fw.write(gene+"\t");
			t = signor_conn;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][1]+"\t"+t.stringTable[k][2]+"\t"+t.stringTable[k][3]+"\t"); }else{ fw.write("N/A\tN/A\tN/A\t"); };
			t = signor_page;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"+Math.log10(Float.parseFloat(t.stringTable[k][1]))+"\t"); }else{ fw.write("N/A\tN/A\t"); };
			t = signor_chei;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"+Math.log10(Float.parseFloat(t.stringTable[k][1]))+"\t"); }else{ fw.write("N/A\tN/A\t"); };
			t = signor_2d;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"); }else{ fw.write("N/A\t"); };
			
			t = gm_conn;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][1]+"\t"+t.stringTable[k][2]+"\t"+t.stringTable[k][3]+"\t"); }else{ fw.write("N/A\tN/A\tN/A\t"); };
			t = gm_page;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"+Math.log10(Float.parseFloat(t.stringTable[k][1]))+"\t"); }else{ fw.write("N/A\tN/A\t"); };
			t = gm_chei;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"+Math.log10(Float.parseFloat(t.stringTable[k][1]))+"\t"); }else{ fw.write("N/A\tN/A\t"); };
			t = gm_2d;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"); }else{ fw.write("N/A\t"); };
			
			t = k562_conn;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][1]+"\t"+t.stringTable[k][2]+"\t"+t.stringTable[k][3]+"\t"); }else{ fw.write("N/A\tN/A\tN/A\t"); };
			t = k562_page;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"+Math.log10(Float.parseFloat(t.stringTable[k][1]))+"\t"); }else{ fw.write("N/A\tN/A\t"); };
			t = k562_chei;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"+Math.log10(Float.parseFloat(t.stringTable[k][1]))+"\t"); }else{ fw.write("N/A\tN/A\t"); };
			t = k562_2d;
			if(t.tableHashPrimary.containsKey(gene)){ k = t.tableHashPrimary.get(gene).get(0); fw.write(t.stringTable[k][0]+"\t"); }else{ fw.write("N/A\t"); };
			
			fw.write("\n");
		}
		
		fw.close();
	}
	
	public static void correctTheDataTable(String fn, int column) throws Exception{
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		HashSet<String> list = new HashSet<String>();
		FileWriter fw = new FileWriter(fn+"_");
		for(String line: lines){
			String parts[] = line.split("\t");
			String name = parts[column];
			if(!list.contains(name)){
				fw.write(line+"\n");
				list.add(name);
			}
		}
		fw.close();
	}
	
	public static void classifyProteinsByNetworks(String folder) throws Exception{
		FileWriter fw = new FileWriter(folder+"proteinsbynetworks.txt");
		fw.write("PROTEIN\tSIGNOR\tGM\tK562\tGM_K562\tSIGNOR_GM_K562\tGM_ONLY\tK562_ONLY\n");
		Graph SIGNOR = loadGraphFromSifFile(folder+"signor.sif");
		Graph GM = loadGraphFromSifFile(folder+"GM.sif");
		Graph K562 = loadGraphFromSifFile(folder+"K562.sif");
		XGMML.saveToXGMML(XGMML.convertGraphToXGMML(SIGNOR), folder+"signor.xgmml");
		XGMML.saveToXGMML(XGMML.convertGraphToXGMML(SIGNOR), folder+"GM.xgmml");
		XGMML.saveToXGMML(XGMML.convertGraphToXGMML(SIGNOR), folder+"K562.xgmml");
		Vector<String> allgenes = new Vector<String>();
		for(int i=0;i<SIGNOR.Nodes.size();i++) if(!allgenes.contains(SIGNOR.Nodes.get(i).Id)) allgenes.add(SIGNOR.Nodes.get(i).Id);
		for(int i=0;i<GM.Nodes.size();i++) if(!allgenes.contains(GM.Nodes.get(i).Id)) allgenes.add(GM.Nodes.get(i).Id);
		for(int i=0;i<K562.Nodes.size();i++) if(!allgenes.contains(K562.Nodes.get(i).Id)) allgenes.add(K562.Nodes.get(i).Id);
		Collections.sort(allgenes);
		for(String gene: allgenes){
			fw.write(gene+"\t");
			if(SIGNOR.getNode(gene)!=null) fw.write("1\t"); else fw.write("0\t");
			if(GM.getNode(gene)!=null) fw.write("1\t"); else fw.write("0\t");
			if(K562.getNode(gene)!=null) fw.write("1\t"); else fw.write("0\t");
			if((GM.getNode(gene)!=null)&&(K562.getNode(gene)!=null)) fw.write("1\t"); else fw.write("0\t");
			if((SIGNOR.getNode(gene)!=null)&&(GM.getNode(gene)!=null)&&(K562.getNode(gene)!=null)) fw.write("1\t"); else fw.write("0\t");
			if((GM.getNode(gene)!=null)&&(K562.getNode(gene)==null)) fw.write("1\t"); else fw.write("0\t");
			if((GM.getNode(gene)==null)&&(K562.getNode(gene)!=null)) fw.write("1\t"); else fw.write("0\t");
			fw.write("\n");
		}
		fw.close();
	}
	
	public static void compileReducedNetworks(String folder, String subnetwork, String typeOfAnalysis) throws Exception{
		VDataTable SIGNOR = VDatReadWrite.LoadFromSimpleDatFile(folder+"signor/G_reduced/"+subnetwork+"/"+typeOfAnalysis+"/Gall_tab.dat", true, "\t");
		SIGNOR.makePrimaryHash("Source");
		VDataTable GM = VDatReadWrite.LoadFromSimpleDatFile(folder+"GM/G_reduced/"+subnetwork+"/"+typeOfAnalysis+"/Gall_tab.dat", true, "\t");
		GM.makePrimaryHash("Source");
		VDataTable K562 = VDatReadWrite.LoadFromSimpleDatFile(folder+"K562/G_reduced/"+subnetwork+"/"+typeOfAnalysis+"/Gall_tab.dat", true, "\t");
		K562.makePrimaryHash("Source");
		
		String networks[] = {"SIGNOR","GM","K562"};
		VDataTable tables[] = {SIGNOR,GM,K562};
		
		//Vector<String> list = Utils.loadStringListFromFile(folder+subnetwork+"_genes.txt");
		
		Vector<String> allproteins = new Vector<String>();
		VDataTable vt = SIGNOR;
		for(int i=0;i<vt.rowCount;i++){ 
			String source = vt.stringTable[i][vt.fieldNumByName("Source")]; if(!allproteins.contains(source)) allproteins.add(source);
			String target = vt.stringTable[i][vt.fieldNumByName("Target")]; if(!allproteins.contains(target)) allproteins.add(target);
		}
		vt = GM;
		for(int i=0;i<vt.rowCount;i++){ 
			String source = vt.stringTable[i][vt.fieldNumByName("Source")]; if(!allproteins.contains(source)) allproteins.add(source);
			String target = vt.stringTable[i][vt.fieldNumByName("Target")]; if(!allproteins.contains(target)) allproteins.add(target);
		}
		vt = K562;
		for(int i=0;i<vt.rowCount;i++){ 
			String source = vt.stringTable[i][vt.fieldNumByName("Source")]; if(!allproteins.contains(source)) allproteins.add(source);
			String target = vt.stringTable[i][vt.fieldNumByName("Target")]; if(!allproteins.contains(target)) allproteins.add(target);
		}
		Collections.sort(allproteins);
		System.out.println(allproteins.size()+" proteins in the gene set.");
		
		Graph gr = new Graph();
		for(int i=0;i<allproteins.size();i++){
			Node n = gr.getCreateNode(allproteins.get(i));
			for(int j=0;j<networks.length;j++){
				Attribute att = new Attribute(networks[j]+"_PRVAL",""+tables[j].stringTable[tables[j].tableHashPrimary.get(n.Id).get(0)][tables[j].fieldNumByName("PR")],Attribute.ATTRIBUTE_TYPE_REAL);
				n.Attributes.add(att);
				att = new Attribute(networks[j]+"_PR",""+tables[j].stringTable[tables[j].tableHashPrimary.get(n.Id).get(0)][tables[j].fieldNumByName("S")],Attribute.ATTRIBUTE_TYPE_REAL);
				n.Attributes.add(att);
			}
		}
		for(int i=0;i<networks.length;i++)for(int j=0;j<tables[i].rowCount;j++){
			String source = tables[i].stringTable[j][tables[i].fieldNumByName("Source")];
			String target = tables[i].stringTable[j][tables[i].fieldNumByName("Target")];
			String link = source+"_"+target;
			Edge e = gr.getCreateEdge(link);
			e.Node1 = gr.getNode(source);
			e.Node2 = gr.getNode(target);
			float grr = Float.parseFloat(tables[i].stringTable[j][tables[i].fieldNumByName("Grr")]);
			String DIRECT = "FALSE";
			if(grr>1e-4) DIRECT = "TRUE";
			Attribute att = new Attribute(networks[i]+"_DIRECT",DIRECT,Attribute.ATTRIBUTE_TYPE_STRING);
			e.Attributes.add(att);
			att = new Attribute(networks[i]+"_Grr",""+grr,Attribute.ATTRIBUTE_TYPE_REAL);
			e.Attributes.add(att);
			float gqr = Float.parseFloat(tables[i].stringTable[j][tables[i].fieldNumByName("Gqr")]);
			if(gqr<0) gqr = 0f;
			att = new Attribute(networks[i]+"_Gqr",""+gqr,Attribute.ATTRIBUTE_TYPE_REAL);
			e.Attributes.add(att);
		}
		for(Node n: gr.Nodes){
			float gmpr = Float.parseFloat(n.getFirstAttributeValue("GM_PR")); 
			float k562pr = Float.parseFloat(n.getFirstAttributeValue("K562_PR"));
			float gmprval = Float.parseFloat(n.getFirstAttributeValue("GM_PRVAL")); 
			float k562prval = Float.parseFloat(n.getFirstAttributeValue("K562_PRVAL"));
			Attribute att = new Attribute("K562_GM_LOGRATIO",""+Math.log(gmpr/k562pr),Attribute.ATTRIBUTE_TYPE_REAL);
			n.Attributes.add(att);
			att = new Attribute("K562_GM_LOGVALRATIO",""+Math.log(k562prval/gmprval),Attribute.ATTRIBUTE_TYPE_REAL);
			n.Attributes.add(att);
			att = new Attribute("IN_SUBNETWORK","TRUE",Attribute.ATTRIBUTE_TYPE_STRING);
			n.Attributes.add(att);
		}
		
		for(Edge e: gr.Edges){
			float GM_Gqr = Float.parseFloat(e.getFirstAttributeValue("GM_Gqr"));
			float K562_Gqr = Float.parseFloat(e.getFirstAttributeValue("K562_Gqr"));
			float SIGNOR_Gqr = Float.parseFloat(e.getFirstAttributeValue("SIGNOR_Gqr"));
			Attribute att = new Attribute("GM_SIGNOR_RATIO",""+(GM_Gqr+1e-6)/(SIGNOR_Gqr+1e-6),Attribute.ATTRIBUTE_TYPE_REAL);
			e.Attributes.add(att);
			att = new Attribute("K562_SIGNOR_RATIO",""+(K562_Gqr+1e-6)/(SIGNOR_Gqr+1e-6),Attribute.ATTRIBUTE_TYPE_REAL);
			e.Attributes.add(att);			
			att = new Attribute("K562_GM_RATIO",""+(K562_Gqr+1e-6)/(GM_Gqr+1e-6),Attribute.ATTRIBUTE_TYPE_REAL);
			e.Attributes.add(att);			
		}
		saveGraphAsMatrix(gr,folder+subnetwork+"_"+typeOfAnalysis+".txt",folder+subnetwork+"_"+typeOfAnalysis+"_nodesonly.txt");
		gr.name = subnetwork+"_"+typeOfAnalysis;
		XGMML.saveToXGMML(XGMML.convertGraphToXGMML(gr), folder+subnetwork+"_"+typeOfAnalysis+".xgmml");
	}
	
	public static void saveGraphAsMatrix(Graph gr, String fn, String fnnodes) throws Exception{
		Vector<String> listOfNodeAttributes = new Vector<String>();
		for(int i=0;i<gr.Nodes.size();i++)for(int j=0;j<gr.Nodes.get(i).Attributes.size();j++){
			Attribute at = (Attribute)(gr.Nodes.get(i).Attributes.get(j));
			String atname = at.name;
			if(!listOfNodeAttributes.contains(atname)) listOfNodeAttributes.add(atname);
		}
		Vector<String> listOfEdgeAttributes = new Vector<String>();
		for(int i=0;i<gr.Edges.size();i++)for(int j=0;j<gr.Edges.get(i).Attributes.size();j++){
			Attribute at = (Attribute)(gr.Edges.get(i).Attributes.get(j));
			String atname = at.name;
			if(!listOfEdgeAttributes.contains(atname)) listOfEdgeAttributes.add(atname);
		}		
		FileWriter fw = new FileWriter(fn);
		fw.write("SOURCE\tTARGET\t"); 
		for(int i=0;i<listOfNodeAttributes.size();i++) fw.write(listOfNodeAttributes.get(i)+"\t");
		for(int i=0;i<listOfEdgeAttributes.size();i++) fw.write(listOfEdgeAttributes.get(i)+"\t");
 		fw.write("\n");
 		for(Edge e: gr.Edges){
 			String source = e.Node1.Id;
 			String target = e.Node2.Id;
 			fw.write(source+"\t"+target+"\t");
 			for(int i=0;i<listOfNodeAttributes.size();i++) fw.write(gr.getNode(source).getFirstAttributeValue(listOfNodeAttributes.get(i))+"\t");
 			for(int i=0;i<listOfEdgeAttributes.size();i++) fw.write(e.getFirstAttributeValue(listOfEdgeAttributes.get(i))+"\t");
 	 		fw.write("\n");
 		}
		fw.close();
		fw = new FileWriter(fnnodes);
		fw.write("PROTEIN\t"); 
		for(int i=0;i<listOfNodeAttributes.size();i++) fw.write(listOfNodeAttributes.get(i)+"\t");
 		fw.write("\n");
 		for(Node n: gr.Nodes){
 			String protein = n.Id;
 			fw.write(protein+"\t");
 			for(int i=0;i<listOfNodeAttributes.size();i++) fw.write(gr.getNode(protein).getFirstAttributeValue(listOfNodeAttributes.get(i))+"\t");
 	 		fw.write("\n");
 		}
		fw.close();
	}
	
	public static void extractGeneNetworkFromWiki(boolean neighbours) throws Exception{
		//Vector<String> geneNamesList = Utils.loadStringListFromFile("C:/Datas/Googlomics/WikiProteins/WikiData/enwiki/gene_articles.txt");
		//Vector<String> geneNamesList = Utils.loadStringListFromFile("C:/Datas/Googlomics/WikiProteins/work/fln_partners_titles");
		//Vector<String> geneNamesList = Utils.loadStringListFromFile("C:/Datas/Googlomics/WikiProteins/work/fln_partners_neighbours_titles");
		Vector<String> geneNamesList = Utils.loadStringListFromFile("C:/Datas/Googlomics/WikiProteins/work/racrho_neighbours_titles");
		 
		System.out.println("Loading titles...");
		HashSet<String> geneNamesSet = new HashSet<String>();
		for(String s: geneNamesList){ s = makeKeyFromName(s); geneNamesSet.add(s); 
		//System.out.println(s);
		}
		Vector<String> wikiTitles = Utils.loadStringListFromFile("C:/Datas/Googlomics/WikiProteins/WikiData/enwiki/enwiki.titles");
		HashSet<String> wikiTitlesSet = new HashSet<String>();
		HashMap<String,Integer> wikiTitlesMap = new HashMap<String,Integer>();
		HashMap<Integer,String> wikiTitlesMapInverse = new HashMap<Integer,String>();
		
		for(int i=0;i<wikiTitles.size();i++) { 
			String s = makeKeyFromName(wikiTitles.get(i)); wikiTitlesSet.add(s); 
			wikiTitlesMap.put(s, i+1);
			wikiTitlesMapInverse.put(i+1, s);
		}
		System.out.println(geneNamesList.size()+" gene names.");
		System.out.println(wikiTitles.size()+" wiki articles.");
		System.out.println("Computing intersection...");
		HashSet<String> intersection = Utils.IntersectionOfSets(geneNamesSet, wikiTitlesSet);
		System.out.println(intersection.size()+" in the intersection.");
		System.out.println("Not found: ");
		for(String s: geneNamesList){ 
			String s1 = makeKeyFromName(s);
			if(!wikiTitlesSet.contains(s1))
				System.out.println(s);
		}
		//FileWriter fw = new FileWriter("C:/Datas/Googlomics/WikiProteins/WikiData/enwiki/geneNetwork.txt");
		//FileWriter fwnond = new FileWriter("C:/Datas/Googlomics/WikiProteins/WikiData/enwiki/geneNetwork_nondirected.txt");
		FileWriter fw = new FileWriter("C:/Datas/Googlomics/WikiProteins/work/racrho_neighbours_second.txt");
		FileWriter fwnond = new FileWriter("C:/Datas/Googlomics/WikiProteins/work/racrho_neighbours_second_nondirected.txt");
		
		
		LineNumberReader lr = new LineNumberReader(new FileReader("C:/Datas/Googlomics/WikiProteins/WikiData/enwiki/enwiki.txt"));
		String s = null;
		long count = 0;
		HashSet<String> links = new HashSet<String>();
		while((s=lr.readLine())!=null){
			count++;
			if(count==(long)(count*1e-6)*1e6)
				System.out.println(count);
			String parts[] = s.split("\t");
			int i1 = Integer.parseInt(parts[0]);
			int i2 = Integer.parseInt(parts[1]);
			String s1 = wikiTitlesMapInverse.get(i1);
			String s2 = wikiTitlesMapInverse.get(i2);
			if(!s1.equals(s2))
				if(!neighbours)if(geneNamesSet.contains(s1))if(geneNamesSet.contains(s2)){
					String link = s1+"$$$"+s2;
					String inverselink = s2+"$$$"+s1;
					String name1 = wikiTitles.get(i1-1);
					String name2 = wikiTitles.get(i2-1);
					fw.write(name1+"\t"+name2+"\n");
					if(!links.contains(link))
						if(!links.contains(inverselink))
							fwnond.write(name1+"\t"+name2+"\n");
					links.add(link);
					links.add(inverselink);
				}
			    if(neighbours){

					if(geneNamesSet.contains(s1)||geneNamesSet.contains(s2)){
						String link = s1+"$$$"+s2;
						String inverselink = s2+"$$$"+s1;
						String name1 = wikiTitles.get(i1-1);
						String name2 = wikiTitles.get(i2-1);
						fw.write(name1+"\t"+name2+"\n");
						if(!links.contains(link))
							if(!links.contains(inverselink))
								fwnond.write(name1+"\t"+name2+"\n");
						links.add(link);
						links.add(inverselink);
					}
			    	
			    }
		}
		lr.close();
		
		fw.close();
		fwnond.close();
	}
	
	public static String makeKeyFromName(String s){
		s = s.toLowerCase(); s = s.replace("-", " "); s = s.replace(" ", ""); s = s.replace("(", ""); s = s.replace(")", ""); s = s.replace(",", ""); s = s.replace(".", "");
		return s;
	}
	
	public static void MakeAdjacencyMatrixFromSif(String fn) throws Exception{
		String s = null;
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		Vector<String> names = new Vector<String>();
		while((s=lr.readLine())!=null){
			String parts[] = s.split("\t");
			String source = parts[0];
			String target = parts[2];
			if(!names.contains(source)) names.add(source);
			if(!names.contains(target)) names.add(target);
		}
		Collections.sort(names);
		lr.close();
		lr = new LineNumberReader(new FileReader(fn));
		int A[][] = new int[names.size()][names.size()];
		int numberOfEdges = 0;
		int numberOfSelfInteractions = 0;
		int numberOfUniqueEdges = 0;
		while((s=lr.readLine())!=null){
			String parts[] = s.split("\t");
			String source = parts[0];
			String target = parts[2];
			int k1 = names.indexOf(source);
			int k2 = names.indexOf(target);
			if(A[k1][k2]==0) numberOfUniqueEdges++;
			//if(k1!=k2)
				A[k1][k2] = 1;
			numberOfEdges++;
			if(k1==k2) numberOfSelfInteractions++;
			if(target.equals("IRF3")){
				System.out.println("source="+source+" target="+target+" k1="+k1+" k2="+k2+" A[k1][k2]="+A[k1][k2]+"");
			}
			
		}
		lr.close();
		
		FileWriter fwnames = new FileWriter(fn+".names");
		for(int i=0;i<names.size();i++) fwnames.write(names.get(i)+"\n");
		fwnames.close();

		FileWriter fwA = new FileWriter(fn+".A");
		for(int j=0;j<names.size();j++){
			for(int i=0;i<names.size();i++) fwA.write(A[j][i]+"\t"); fwA.write("\n");
		}
		fwA.close();
		
		System.out.println("numberOfEdges = "+numberOfEdges);
		System.out.println("numberOfSelfInteractions = "+numberOfSelfInteractions);
		System.out.println("numberOfUniqueEdges = "+numberOfUniqueEdges);
		
	}
	
	public static Graph ExtractLCCFromGraph(String fn) throws Exception{
		System.out.println("Loading graph...");
		Graph gr = loadGraphFromTxtFile(fn);
		System.out.println("Loaded.");
		GraphAlgorithms.verbose = true;
		System.out.println("Computing connected components...");
		Vector<Graph> ccs = GraphAlgorithms.ConnectedComponents(gr);
		System.out.println("Computed.");
		int max = -1;
		int k = -1;
		for(int i=0;i<ccs.size();i++)
			if(ccs.get(i).Nodes.size()>max){
				max = ccs.get(i).Nodes.size();
				k = i;
			}
		XGMML.saveToXGMML(ccs.get(k), fn+".lcc.xgmml");
		ccs.get(k).saveAsCytoscapeSif(fn+".lcc.sif");
		saveGraphToTxtFile(ccs.get(k),fn+".lcc.txt");
		System.out.println("Reloading graph from "+fn+".lcc.sif");
		Graph gr_un = loadGraphFromSIFFie(fn+".lcc.sif");
		System.out.println("Loaded.");
		System.out.println("LCC: Number of nodes = "+gr_un.Nodes.size()+", Number of edges = "+gr_un.Edges.size()+", Density = "+(float)gr_un.Edges.size()/(float)gr_un.Nodes.size());
		gr_un = StructureAnalysisUtils.removeReciprocalEdges(gr_un);
		System.out.println("LCC: Number of nodes = "+gr_un.Nodes.size()+", Number of unique (non-reciprocal) edges = "+gr_un.Edges.size()+", Density = "+(float)gr_un.Edges.size()/(float)gr_un.Nodes.size());
		XGMML.saveToXGMML(gr_un, fn+".lcc_undirected.xgmml");
		gr_un.saveAsCytoscapeSif(fn+".lcc_undirected.sif");
		saveGraphToTxtFile(gr_un,fn+".lcc_undirected.txt");
		return ccs.get(k);
	}

	
	public static Graph loadGraphFromTxtFile(String fn){
		Graph gr = new Graph();
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		for(int i=0;i<vt.rowCount;i++){
			Node ns = gr.getCreateNode(vt.stringTable[i][0]);		
			Node nt = gr.getCreateNode(vt.stringTable[i][1]);
			Edge e = gr.getCreateEdge(ns.Id+"__"+nt.Id);
			e.Node1 = ns;
			e.Node2 = nt;
			for(int j=2;j<vt.colCount;j++){
				e.setAttributeValueUnique(vt.fieldNames[j], vt.stringTable[i][j], vt.NUMERICAL);
			}
		}
		return gr;
	}
	
	public static void saveGraphToTxtFile(Graph gr, String fn) throws Exception{
		FileWriter fw = new FileWriter(fn);
		fw.write("SOURCE\tTARGET\tWEIGHT\n");
		for(int i=0;i<gr.Edges.size();i++){
			Edge ed = gr.Edges.get(i);
			fw.write(ed.Node1.Id+"\t"+ed.Node2.Id+"\t"+ed.getFirstAttributeValue("WEIGHT")+"\n");
		}
		fw.close();
	}
	
	
	
	public static Graph loadGraphFromSIFFie(String fn){
		Graph gr = new Graph();
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, false, "\t");
		for(int i=0;i<vt.rowCount;i++){
			Node ns = gr.getCreateNode(vt.stringTable[i][0]);		
			Node nt = gr.getCreateNode(vt.stringTable[i][2]);
			Edge e = gr.getCreateEdge(ns.Id+"__"+nt.Id);
			e.Node1 = ns;
			e.Node2 = nt;
			e.setAttributeValueUnique("TYPE", vt.stringTable[i][1], vt.STRING);
		}
		return gr;
	}
	
	
	public static void ComputeGraphDensity(String fn){
		System.out.println("Loading graph...");
		Graph gr = loadGraphFromTxtFile(fn);
		System.out.println("Loaded.");
		System.out.println("Graph: Number of nodes = "+gr.Nodes.size()+", Number of edges = "+gr.Edges.size()+", Density = "+(float)gr.Edges.size()/(float)gr.Nodes.size());
		gr = StructureAnalysisUtils.removeReciprocalEdges(gr);
		System.out.println("Graph: Number of nodes = "+gr.Nodes.size()+", Number of unique (non-reciprocal) edges = "+gr.Edges.size()+", Density = "+(float)gr.Edges.size()/(float)gr.Nodes.size());
	}
	
	public static void MatchPageTitleHUGOName(String listOfPageTitles, String HUGOTable) throws Exception{
		Vector<String> titles = Utils.loadStringListFromFile(listOfPageTitles);
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(HUGOTable, true, "\t");
		vt.makePrimaryHash(vt.fieldNames[0]);
		FileWriter fw_yes = new FileWriter(listOfPageTitles+".matched");
		FileWriter fw_non = new FileWriter(listOfPageTitles+".unmatched");
		for(String s: titles){
			if(vt.tableHashPrimary.get(s)!=null)
				fw_yes.write(s+"\t"+s+"\n");
			else
				fw_non.write(s+"\n");
		}
		fw_yes.close();
		fw_non.close();
	}
	
	public static void FindEntrezIDFromProteinWikiTitle(String listOfPageTitles) throws Exception{
		Vector<String> titles = Utils.loadStringListFromFile(listOfPageTitles);
		
		VDataTable HGNC2Entrez = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/HGNC_ENTREZ_short.txt ", true, "\t");
		HGNC2Entrez.makePrimaryHash(HGNC2Entrez.fieldNames[0]);
		VDataTable Hugo2HGNC = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/HUGO_HGNC_Human.txt", true, "\t");
		Hugo2HGNC.makePrimaryHash(Hugo2HGNC.fieldNames[1]);
		VDataTable Hugo2HGNC_hugo = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/acsioma/conversions/HUGO_HGNC_Human.txt", true, "\t");
		Hugo2HGNC_hugo.makePrimaryHash(Hugo2HGNC.fieldNames[0]);
		
		FileWriter fw = new FileWriter(listOfPageTitles+".entrez");
		//fw.write("PAGE\tENTREZ\tHUGO\n");
		fw.write("PAGE\tHUGO\n");
		int k=0;
		for(String s: titles){
			String sr = Utils.replaceString(s, " ", "%20");
			String content = VDownloader.downloadURL("https://en.wikipedia.org/w/index.php?action=raw&title="+sr);
			
			System.out.println((k+1)+":"+sr+" > "+content.substring(0,Math.min(100,content.length()))+"\n"); k++;
			
			//System.out.println(content);
			LineNumberReader lr = new LineNumberReader(new StringReader(content));
			String ss = null;
			String entrez = null;
			String hugo = null;
			while((ss=lr.readLine())!=null){
				StringTokenizer st = new StringTokenizer(ss,"&|= }{");
				boolean containsGeneName = false;
				boolean UCSCString = false;
				boolean redirect = false;
				if(ss.contains("[[gene]]"))
					containsGeneName = true;
				if(ss.contains("UCSC gene info"))
					UCSCString = true;
				if(ss.contains("UCSC genome browser"))
					UCSCString = true;
				if(ss.contains("#REDIRECT"))
					redirect = true;
				
				
				
				while(st.hasMoreTokens()){
					String sn = st.nextToken();
					if(sn.equals("TermToSearch"))
						entrez = st.nextToken();
					if(containsGeneName)if(sn.startsWith("''"))if(sn.charAt(2)!='\''){
						hugo = sn.substring(2, sn.length()-2);
						if(Hugo2HGNC_hugo.tableHashPrimary.get(hugo)==null)
							hugo = null;
					}
					if(UCSCString)if(sn.equals("info")){
						hugo = st.nextToken();
						if(Hugo2HGNC_hugo.tableHashPrimary.get(hugo)==null)
							hugo = null;
					}
					if(UCSCString)if(sn.equals("browser")){
						hugo = st.nextToken();
						if(Hugo2HGNC_hugo.tableHashPrimary.get(hugo)==null)
							hugo = null;
					}
					if(redirect)if(sn.equals("#REDIRECT")){
						hugo = st.nextToken();
						hugo = hugo.substring(2,hugo.length()-2);
						if(Hugo2HGNC_hugo.tableHashPrimary.get(hugo)==null)
							hugo = null;
					}
						
				}
				if(hugo!=null)
					 break;
				if(entrez!=null)
						break;
			}
			lr.close();
			if(entrez==null){
				if(hugo==null)
					//fw.write(s+"\tNONE\tNONE\n");
					fw.write(s+"\tNONE\n");
				else
					//fw.write(s+"\tNONE\t"+hugo+"\n");
					fw.write(s+"\t"+hugo+"\n");
			}else{
				hugo = getHUGOFromEntrez(entrez,HGNC2Entrez,Hugo2HGNC);
				fw.write(s+"\t"+hugo+"\n");
			}
			fw.flush();
		}
		fw.close();
	}
	
	public static String getHUGOFromEntrez(String entrez, VDataTable HGNC2Entrez, VDataTable Hugo2HGNC){
		String hugo = null;
		if(HGNC2Entrez.tableHashPrimary.get(entrez)!=null){
			String hgnc = HGNC2Entrez.stringTable[HGNC2Entrez.tableHashPrimary.get(entrez).get(0)][1];
			if(Hugo2HGNC.tableHashPrimary.get(hgnc)!=null){
				hugo = Hugo2HGNC.stringTable[Hugo2HGNC.tableHashPrimary.get(hgnc).get(0)][0];
			}
		}
		return hugo;
	}
	
	public static void convertWikiGraph2HUGO(String fn, String conversionTableFileName)throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(conversionTableFileName, true, "\t");
		vt.makePrimaryHash(vt.fieldNames[0]);
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		FileWriter fw = new FileWriter(fn+".HUGO.txt");
		while((s=lr.readLine())!=null){
			String parts[] = s.split("\t");
			String part1 = parts[0];
			String part2 = parts[1];
			String part3 = parts[2];
			if(vt.tableHashPrimary.containsKey(part1))
				part1 = vt.stringTable[vt.tableHashPrimary.get(part1).get(0)][1];
			if(vt.tableHashPrimary.containsKey(part2))
				part2 = vt.stringTable[vt.tableHashPrimary.get(part2).get(0)][1];
			fw.write(part1+"\t"+part2+"\t"+part3+"\n");
		}
		fw.close();
		lr.close();
	}
	
	public static Graph filterPathDB(Graph pathDB, Graph gr) throws Exception{
		Vector<Edge> edges = new Vector<Edge>(); 
		for(int i=0;i<pathDB.Edges.size();i++){
			Edge e = pathDB.Edges.get(i);
			Node n1 = e.Node1;
			Node n2 = e.Node2;
			if((gr.getNode(n1.Id)!=null)&&(gr.getNode(n2.Id)!=null)){
				edges.add(e);
			}
		}
		System.out.println("Making new graph.. ("+edges.size()+")");
		Graph gr_out = new Graph();
		for(Edge e: edges){
			Node n1 = e.Node1;
			Node n2 = e.Node2;
			gr_out.addNode(n1);
			gr_out.addNode(n2);
			gr_out.addEdge(e);
		}
		System.out.println("Size of the new graph.. (nodes="+gr_out.Nodes.size()+",edge="+edges.size()+")");
		return gr_out;
	}
	
	public static void countCommonEdges(Graph gr, Graph pathDB) throws Exception{
		int count = 0;
		HashMap<String,Integer> countTypes = new HashMap<String,Integer>();
		for(int i=0;i<pathDB.Edges.size();i++){
			Edge e = pathDB.Edges.get(i);
			String tp = e.getFirstAttributeValue("TYPE");
			Integer c = 0;
			if(countTypes.get(tp)!=null)
				c = countTypes.get(tp);
			countTypes.put(tp, ++c);
		}
		HashMap<String,Integer> countTypes2 = new HashMap<String,Integer>();
		
		for(int i=0;i<gr.Edges.size();i++){
			Edge e = gr.Edges.get(i);
			//System.out.println(e.Id);
			Edge e1 = pathDB.getEdge(e.Id);
			if(e1!=null){
				count++;
				String tp = e1.getFirstAttributeValue("TYPE");
				Integer c = 0;
				if(countTypes2.get(tp)!=null)
					c = countTypes2.get(tp);
				countTypes2.put(tp, ++c);
			}
		}
		System.out.println("Found "+count);
		for(String s:countTypes.keySet()){
			System.out.println(s+"\t"+countTypes.get(s)+"\t"+(countTypes2.get(s)==null?0:countTypes2.get(s)));
		}
	}
	
	public static void compareConnectivities(Graph gr1, Graph gr2){
		gr1.calcNodesInOut();
		gr2.calcNodesInOut();
		System.out.println("NODE\tGR1\tGR2");
		for(int i=0;i<gr1.Nodes.size();i++){
			Node n = gr1.Nodes.get(i);
			if(gr2.getNode(n.Id)!=null){
				int conn1 = gr1.getNode(n.Id).incomingEdges.size()+gr1.getNode(n.Id).outcomingEdges.size();
				int conn2 = gr2.getNode(n.Id).incomingEdges.size()+gr2.getNode(n.Id).outcomingEdges.size();
				System.out.println(n.Id+"\t"+conn1+"\t"+conn2);
			}
		}
	}
	
	public static void extractPageRanks(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String s = null;
		FileWriter fw = new FileWriter(fn+".PR");
		fw.write("PROTEIN\tPAGERANK\n");
		HashMap<String,Integer> prmap = new HashMap<String,Integer>();
		while((s=lr.readLine())!=null){
			String parts[] = s.split("\t");
			if(parts.length>2){
				prmap.put(parts[3], Integer.parseInt(parts[0]));
			}
		}
		Set<String> keys = prmap.keySet();
		for(String ss:keys){
			fw.write(ss+"\t"+prmap.get(ss)+"\n");
		}
		
		lr.close();
		fw.close();
	}
	
	public static void addCommunityNamesToHeader(String community_table, String gmtFile) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(community_table, true, "\t");
		Vector<GESignature> gmts = GMTReader.readGMTDatabaseWithWeights(gmtFile);
		HashMap<String,String> idname = new HashMap<String,String>();
		for(int i=0;i<gmts.size();i++){
			GESignature gs = gmts.get(i);
			idname.put(gs.description, gs.name);
		}
		for(int i=1;i<vt.fieldNames.length;i++){
			String fn = vt.fieldNames[i];
			if(idname.get(fn)!=null){
				vt.fieldNames[i] = fn+"("+idname.get(fn)+")"; 
			}
		}
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.saveToSimpleDatFile(vt, community_table.substring(0,community_table.length()-4)+"_headnames.xls");
	}
	
	
	public static void replaceIdsInCommunityGMTFile(String gmtFile, String annotFile, String folder, String proteinIDHUGOTable) throws Exception{
		Vector<GESignature> gmts = GMTReader.readGMTDatabaseWithWeights(gmtFile);
		
		VDataTable vt_conv = VDatReadWrite.LoadFromSimpleDatFile(proteinIDHUGOTable, true, "\t");
		vt_conv.makePrimaryHash(vt_conv.fieldNames[0]);
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(annotFile, false, "\t");
		vt.makePrimaryHash(vt.fieldNames[0]);
		FileWriter fw = new FileWriter(gmtFile.substring(0,gmtFile.length()-4)+"_commnames.gmt");
		FileWriter fwext = new FileWriter(gmtFile.substring(0,gmtFile.length()-4)+"_commnames_extended.gmt");
		for(int i=0;i<gmts.size();i++){
			GESignature gs = gmts.get(i);
			if(vt.tableHashPrimary.get(gs.name)!=null){
				int k = vt.tableHashPrimary.get(gs.name).get(0);
				String comm_name = vt.stringTable[k][1];
				if(comm_name.contains("(protein)"))
						comm_name = comm_name.substring(0, comm_name.length()-10);
				if(comm_name.contains("(gene)"))
					comm_name = comm_name.substring(0, comm_name.length()-7);
				comm_name = comm_name.replaceAll(" ", "_");
				comm_name = comm_name.replaceAll("/", "_");
				comm_name = comm_name.replaceAll(",", "_");
				fw.write(comm_name+"\t"+gs.name+"\t");				
				for(int j=0;j<gs.geneNames.size()-1;j++)
					fw.write(gs.geneNames.get(j)+"\t");
				fw.write(gs.geneNames.get(gs.geneNames.size()-1)+"\n");
				
				Vector<String> extendedList = new Vector<String>();
				VDataTable vtl = VDatReadWrite.LoadFromSimpleDatFile(folder+gs.name+".sif.communitynodes", true, "\t");
				for(int m=0;m<vtl.rowCount;m++){
					String pn = vtl.stringTable[m][0];
					String pn_hugo = null;
					if(vt_conv.tableHashPrimary.get(pn)!=null)
						pn_hugo = vt_conv.stringTable[vt_conv.tableHashPrimary.get(pn).get(0)][1];
					if(pn_hugo!=null)
						extendedList.add(pn_hugo);
				}
				
				fwext.write(comm_name+"\t"+gs.name+"\t");
				for(int j=0;j<extendedList.size()-1;j++)
					fwext.write(extendedList.get(j)+"\t");
				fwext.write(extendedList.get(extendedList.size()-1)+"\n");
				
			}
		}
		fw.close();
		fwext.close();
	}
	
	public static void changeProteinNamesInCellDesignerFile(String cdfn, String gmtFile) throws Exception{
		FileWriter fw = new FileWriter(cdfn+"_commonname.xml");
		LineNumberReader lr = new LineNumberReader(new FileReader(cdfn));
		String s = null;
		Vector<GESignature> gmts = GMTReader.readGMTDatabaseWithWeights(gmtFile);
		HashMap<String,String> idname = new HashMap<String,String>();
		for(int i=0;i<gmts.size();i++){
			GESignature gs = gmts.get(i);
			idname.put(gs.description, gs.name);
		}
		Set<String> ids = idname.keySet();
		
		while((s=lr.readLine())!=null){
			s = s.trim();
			if(s.startsWith("<celldesigner:protein")){
				for(String key: ids){
					if(s.contains("name=\""+key+"\""))
						s = Utils.replaceString(s, "name=\""+key+"\"", "name=\""+idname.get(key)+"\"");
				}
			}
			if(s.startsWith("<species")){
				for(String key: ids){
					if(s.contains("name=\""+key+"\""))
						s = Utils.replaceString(s, "name=\""+key+"\"", "name=\""+idname.get(key)+"\"");
				}
			}
			fw.write(s+"\n");
		}
		lr.close();
		fw.close();
	}
	
	public static void formatCellDesignerNotes() throws Exception{
		
		String folder = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/navicell/materials/";
		
		Vector<String> lines = Utils.loadStringListFromFile(folder+"wikiproteinmap_master_notes.txt");
		FileWriter fw = new FileWriter(folder+"wikiproteinmap_master_notes_modified.txt");
		
		VDataTable pagehugo = VDatReadWrite.LoadFromSimpleDatFile(folder+"ProteinsIDList2017_hugo.txt", true, "\t");
		pagehugo.makePrimaryHash(pagehugo.fieldNames[0]);
		
		Vector<GESignature> gmts = GMTReader.readGMTDatabaseWithWeights(folder+"_communities.gmt");
		
		//VDataTable commannot = VDatReadWrite.LoadFromSimpleDatFile(folder+"_communities_annotations.txt",true,"\t");
		VDataTable commannot = VDatReadWrite.LoadFromSimpleDatFile(folder+"_communities_annot_localconn.txt",true,"\t");
		commannot.makePrimaryHash(commannot.fieldNames[0]);
		
		int k=0;
		while(k<lines.size()){
			String s = lines.get(k);
			if(s.startsWith("### ")){
				String pn = lines.get(k+1);
				k++;
				int j = s.indexOf("pr_");
				String id = s.substring(j+3,s.length());
				System.out.println(id);
				
				fw.write(s+"\n"+pn+"\n\n");
				
				fw.write("Community_begin:\n");
				for(int i=0;i<gmts.size();i++)
					if(gmts.get(i).name.equals(id)){
						GESignature gs = gmts.get(i);
						for(int ii=0;ii<gs.geneNames.size();ii++){
							String comm_name = gs.geneNames.get(ii);
							String hugo = pagehugo.stringTable[pagehugo.tableHashPrimary.get(comm_name).get(0)][1];
							fw.write("WIKI:"+comm_name.replaceAll(" ", "_")+"     "+"HUGO:"+hugo+"\n");
						}
					}
				fw.write("Community_end\n\n");
				
				fw.write("FriendshipNetwork_begin:\n");
				String annotation = commannot.stringTable[commannot.tableHashPrimary.get(id).get(0)][1];
				String parts[] = annotation.split(";");
				for(int ii=0;ii<parts.length;ii++){
					fw.write("WIKI:"+parts[ii].replaceAll(" ", "_")+"\n");
				}
				fw.write("FriendshipNetwork_end\n\n");
			}
			k++;
		}
		fw.close();
	}

}
