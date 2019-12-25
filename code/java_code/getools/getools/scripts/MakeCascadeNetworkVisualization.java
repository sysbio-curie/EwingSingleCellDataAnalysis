package getools.scripts;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import fr.curie.BiNoM.pathways.analysis.structure.Graph;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import fr.curie.BiNoM.pathways.analysis.structure.Node;
import fr.curie.BiNoM.pathways.analysis.structure.Attribute;
import fr.curie.BiNoM.pathways.analysis.structure.Edge;


public class MakeCascadeNetworkVisualization {

	public static void main(String[] args) {
		try{
			
			//ExtractCascadesNetworks("C:/Datas/Kondratova/innate_immune_map/ver130417","inn_immune_master.xgmml");
			//ExtractCascadesNetworks("C:/Datas/Kondratova/innate_immune_map/ver040517","inn_immune_master.xgmml","MODULE","modules");
			//ExtractCascadesNetworks("C:/Datas/Kondratova/innate_immune_map/ver140517","innate_immune_master_14_05_2017.xgmml","MODULE","modules");
			//ExtractCascadesNetworks("C:/Datas/Kondratova/innate_immune_map/ver140517","innate_immune_master_14_05_2017.xgmml","MAP","maps");
			
			//CorrectIllegalCharacters("C:/Datas/Kondratova/innate_immune_map/ver140517/NEUTROPHIL.xml");
			//CorrectIllegalCharacters("C:/Datas/Kondratova/innate_immune_map/ver140517/MAST_CELL.xml");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void ExtractCascadesNetworks(String folder, String fn, String tagName, String folderWithResults) throws Exception{
		Graph gr = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(folder+File.separator+fn));
		Vector<String> listOfCascades = new Vector<String>();
		for(Node n: gr.Nodes){
			String cascade = n.getFirstAttributeValue(tagName);
			if(cascade!=null){
			String parts[] = cascade.split("@@");
			for(String s: parts)
				if(!s.trim().equals(""))
					if(!listOfCascades.contains(s))
						listOfCascades.add(s);
			}
		}
		Collections.sort(listOfCascades);
		//for(String s: listOfCascades){
		//	System.out.println("CASCADE : "+s);
		//}
		for(String cascade: listOfCascades){
			System.out.println(tagName+" : "+cascade);
			Graph cascade_graph = new Graph();
			for(Node n: gr.Nodes){
				String val = n.getFirstAttributeValue(tagName);
				if(val!=null){
					String parts[] = val.split("@@");
					for(String s: parts)
						if(!s.trim().equals(""))
							if(s.equals(cascade)){
								cascade_graph.addNode(n);
								//System.out.println(cascade_graph.Nodes.size()+"("+val+")");
							}
				}
			}
			//System.out.println(cascade_graph.Nodes.size()+" nodes.");
			gr.calcNodesInOut();
			for(Node n: gr.Nodes){
				boolean foundIn = false;
				for(Edge e: n.incomingEdges)
					if(cascade_graph.getNode(e.Node1.Id)!=null)
						foundIn = true;
				boolean foundOut = false;
				for(Edge e: n.outcomingEdges)
					if(cascade_graph.getNode(e.Node2.Id)!=null)
						foundOut = true;
				if(foundIn&&foundOut)
					cascade_graph.addNode(n);
			}
			cascade_graph.addConnections(gr);
			cascade_graph.name = cascade;
			for(Node n: cascade_graph.Nodes){
				for(Attribute att: (Vector<Attribute>)n.Attributes){
					boolean badSymbol = false;
					char[] chars = att.name.toCharArray();
					for(int i=0;i<chars.length;i++)
						if((chars[i]<32)||(chars[i]>122))
							badSymbol = true;
					if(badSymbol)
						att.name = "BADSYMBOLS";
					badSymbol = false;
					chars = att.value.toCharArray();
					for(int i=0;i<chars.length;i++)
						if((chars[i]<32)||(chars[i]>122))
							badSymbol = true;
					if(badSymbol)
						att.value = "BADSYMBOLS";					
				}
			}
			XGMML.saveToXGMML(cascade_graph, folder+File.separator+folderWithResults+File.separator+cascade+".xgmml");
			System.out.println(cascade_graph.Nodes.size()+" nodes.");
		}
	}
	
	public static void CorrectIllegalCharacters(String fn) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		FileWriter fw = new FileWriter(fn.substring(0,fn.length()-4)+"_fixed.xml"); 
		String s = null;
		HashMap<String,Integer> counts = new HashMap<String,Integer>(); 
		while((s=lr.readLine())!=null){
			char cs[] = s.toCharArray();
			for(char c: cs){
				String css = (new StringBuffer()).append(c).toString();
				Integer count = counts.get(css);
				if(count==null) count=0;
				counts.put(css, count+1);
			}
			StringBuffer sf = new StringBuffer("");
			for(char c: cs){
				int ci=(int)c;
				if(ci<129)
					sf.append(c);
			}
			fw.write(sf.toString()+"\n");
		}
		lr.close();
		fw.close();
		for(String ss: counts.keySet())
			System.out.println(""+(int)ss.toCharArray()[0]+"\t"+ss+"\t"+counts.get(s));
	}
	

}
