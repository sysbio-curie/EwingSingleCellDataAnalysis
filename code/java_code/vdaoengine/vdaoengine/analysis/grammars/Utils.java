package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

public class Utils {
	
	public static void setNodesForGraph(Graph graph, float NodePositions[][]){
		int num = NodePositions.length;
		//System.out.println("Number of nodes = "+num);
		for(int i=0;i<num;i++){
			String id = "Node"+(i+1);
			Node n = new Node();
			n.key = id;
			n.x = NodePositions[i];
			//System.out.println("Adding a node "+id+", "+n.x[0]+"\t"+n.x[1]);
			graph.addNode(n);
		}
	}
	
	public static void setEdgesForGraph(Graph graph, int edgeNodePairs[][]){
		int num = edgeNodePairs.length;
		for(int i=0;i<num;i++){
			Node n1 = graph.Nodes.get(edgeNodePairs[i][0]);
			Node n2 = graph.Nodes.get(edgeNodePairs[i][1]);
			Edge e = new Edge(n1,n2);
			graph.addEdge(e);
		}
	}
	
	public static float[][] loadMatrixFromFile(String fn) throws Exception{
		LineNumberReader ln = new LineNumberReader(new FileReader(fn));
		Vector<Vector<Float>> numbers = new Vector<Vector<Float>>();
		String s = null;
		int maxnum = 0;
		while((s=ln.readLine())!=null){
			String fs[] = s.split("\t");
			Vector<Float> v = new Vector<Float>();
			if(fs.length>maxnum)
				maxnum = fs.length;
			for(String val: fs){
				float vf = Float.NaN;
				try{
					vf = Float.parseFloat(val);
				}catch(Exception e){
				}
				v.add(vf);
			}
			numbers.add(v);
		}
		float res[][] = new float[numbers.size()][maxnum];
		for(int i=0;i<res.length;i++) for(int j=0;j<maxnum;j++) res[i][j] = Float.NaN; 
		
		for(int i=0;i<numbers.size();i++){
			Vector<Float> v = numbers.get(i);
			for(int j=0;j<v.size();j++)
				res[i][j] = v.get(j);
		}
		return res;
	}

	public static int[][] loadIntegerMatrixFromFile(String fn) throws Exception{
		float fm[][] = loadMatrixFromFile(fn);
		int res[][] = new int[fm.length][fm[0].length];
		for(int i=0;i<fm.length;i++)
			for(int j=0;j<fm[0].length;j++){
				res[i][j] = (int)fm[i][j];
			}
		return res;
	}
	

}
