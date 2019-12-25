package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

import vdaoengine.analysis.elmap.SimpleRectangularGrid;
import vdaoengine.data.VDataSet;
import vdaoengine.utils.IntegerVector;
import vdaoengine.utils.VVectorCalc;

public class Graph extends Object{

	public static int PROJECTION_CLOSEST_POINT = 0;
	public static int PROJECTION_CLOSEST_NODE = 1;
	
	
	public Vector<Node> Nodes = new Vector();
	//private Vector NodesInternal = new Vector();
	public Vector<Edge> Edges = new Vector();	
	public Vector<Star> Stars = new Vector();
		
	/*
	 * from Node key to Integer
	 */
	private HashMap<String,Integer> NodeIndexMap = new HashMap<String,Integer>();
	/*
	 * from Edge key to Integer
	 */
	private HashMap<String,Integer> EdgeIndexMap = new HashMap<String,Integer>(); 
	/*
	 * from Star key to Integer
	 */
	private HashMap<String,Integer> StarIndexMap = new HashMap<String,Integer>();
	
	/* Node position array [vector][coordinate] form used in computations
	 * It is updated by compileNodesInArrays function
	*/
	public float NodeArray[][] = null;
	/* Node position array [coordinate][vector] form used in computations
	 * It is updated by compileNodesInArrays function
	*/
	public float NodeArrayCopy[] = null;
	public float NodeArrayCopy2[][] = null;
	
	public boolean nodesCompiled = false; 
	
	public static int MAX_STAR_DEGREE = 1000;
	
	//public float complexityFee = 1f;
	public float StarDefaultElasticity[] = new float[MAX_STAR_DEGREE];
	
	public HashMap<String,Vector<Edge>> outgoingEdges = new HashMap<String,Vector<Edge>>();
	public HashMap<String,Vector<Star>> starCenters = new HashMap<String,Vector<Star>>();
	public HashMap<String,Vector<Star>> starLeaves = new HashMap<String,Vector<Star>>();
	
	public void compileNodesInArrays(){
		NodeArray = new float[getNodeNum()][getDimension()];
	    for(int i=0;i<getNodeNum();i++){
	    	for(int j=0;j<getDimension();j++)
	    		NodeArray[i][j] = getNode(i).x[j];
	    }
	    NodeArrayCopy = new float[getNodeNum()*getDimension()];
	    for(int i=0;i<getNodeNum();i++) {
		    for(int j=0;j<getDimension();j++) {
		    	NodeArrayCopy[i*getDimension()+j] = NodeArray[i][j];
		    }
	    }
	    NodeArrayCopy2 = new float[getNodeNum()][getDimension()];
	    for(int i=0;i<getNodeNum();i++){
	                  for(int j=0;j<getDimension();j++)
	                	  NodeArrayCopy2[i][j] = NodeArray[i][j];
	    }
	}
	
	public int getNodeNum(){
		return Nodes.size();
	}
	public int getEdgeNum(){
		return Edges.size();
	}
	public int getStarNum(){
		return Stars.size();
	}
	public int getRibNum(){
		int k=0;
		for(int i=0;i<Stars.size();i++){
			Star s = (Star)Stars.get(i);
			if(s.neighbours.size()==2)
				k++;
		}
		return k;
	}
	
	public int getRayNum(){
		int k=0;
		for(int i=0;i<Stars.size();i++){
			Star s = (Star)Stars.get(i);
			if(s.neighbours.size()!=2)
				k+=s.neighbours.size()-2;
		}
		return k;
	}
	
		
	public int getDimension(){
		int res = -1;
		if(Nodes.size()!=0)
			res = ((Node)Nodes.get(0)).x.length;
		return res;
	}
	
	public Node getNode(int i){
		return (Node)Nodes.get(i);
	}
	
	public Node getNode(String key){
		return (Node)Nodes.get(getNodeIndex(key));
	}	
	
	public int getNodeIndex(String key){
		Integer I = (Integer)NodeIndexMap.get(key);
		if(I==null){
			return -1;
		}else
			return (I.intValue());
	}
	
	public Edge getEdge(int i){
		return (Edge)Edges.get(i);
	}
	public Star getStar(int i){
		return (Star)Stars.get(i);
	}
	
	public void addNode(Node n){
		Nodes.add(n);
		NodeIndexMap.put(n.toString(),new Integer(Nodes.size()-1));
		n.label = ""+(Nodes.size()-1);
	}
	
	public Edge addEdge(Node n1, Node n2){
		Edge e = new Edge(n1,n2);
		e.elasticity = this.StarDefaultElasticity[1];
		if(!EdgeIndexMap.containsKey(e.toString())){
			Edges.add(e);
			EdgeIndexMap.put(e.toString(),new Integer(Edges.size()-1));
		}else{
			e = (Edge)getEdge(e.toString());
		}
		return e;
	}
	
	public Edge addEdge(Edge e){
		Edge en = e.clone(this);
		if(!EdgeIndexMap.containsKey(en.toString())){
			Edges.add(en);
			EdgeIndexMap.put(en.toString(),new Integer(Edges.size()-1));
		}else{
			en = (Edge)getEdge(en.toString());
		}
		return en;
	}
	
	
	public Star addStar(Node central, Vector neigbours){
		Star s = new Star();
		s.elasticity = this.StarDefaultElasticity[neigbours.size()];
		s.centralNode = central;
		s.neighbours = neigbours;
		if(!StarIndexMap.containsKey(s)){
			Stars.add(s);
			StarIndexMap.put(s.toString(),new Integer(Stars.size()-1));
		}else{
			s = (Star)getStar(s.toString());
		}
		return s;
	}
	
	public Star addStar(Star s){
		Star sn = s.clone(this);
		if(!StarIndexMap.containsKey(sn)){
			Stars.add(sn);
			StarIndexMap.put(sn.toString(),new Integer(Stars.size()-1));
		}else{
			sn = (Star)getStar(sn.toString());
		}
		return sn;
	}
	

	public Edge getEdge(String key){
		int index = getEdgeIndex(key);
		Edge e = null;
		if(index!=-1)
			e = (Edge)Edges.get(index);
		return e;
	}
	
	public int getEdgeIndex(String key){
		int ret = -1;
		if(EdgeIndexMap.get(key)!=null)
			ret = ((Integer)EdgeIndexMap.get(key)).intValue(); 
		return ret;
	}
	
	public Star getStar(String key){
		return (Star)Stars.get(getStarIndex(key));
	}
	
	public int getStarIndex(String key){
		return ((Integer)StarIndexMap.get(key)).intValue();
	}	

	
	  public void saveToFile(String fn,String dat){
		    try{
		          FileWriter f = new FileWriter(fn);
		          char s[] = new char[100];
		                  f.write("# DATAFILENAME\r\n");
		                  f.write(dat+".ved\r\n\r\n");
		                  f.write("# TABLENAME\r\n");
		                  f.write(dat+".vet\r\n\r\n");
		                  f.write("# DIMENSION\r\n"+getDimension()+"\r\n\r\n");
		                  f.write("# INITIALIZATION\r\nMAINCOMP 0.5 30 FALSE\r\n\r\n");
		                  f.write("# ELASTICITY\r\n1 1\r\n\r\n");
		                  f.write("# DISCRETE\r\nFALSE\r\n\r\n");
		                  f.write("# SMOOTHNESS\r\n1\r\n\r\n");
		                  f.write("# TONES\r\n0\r\n\r\n");
		                  f.write("# GRIDTYPE\r\n");
		                  f.write("RECTANGULAR\r\n10 10\r\n");
		                  f.write("# NODES\r\n"+getNodeNum()+"\r\n\r\n");
		                  f.write("# EDGES\r\n"+getEdgeNum()+"\r\n\r\n");
		                  f.write("# RIBS\r\n"+getRibNum()+"\r\n\r\n");
		                  f.write("# STARS\r\n"+getStarNum()+"\r\n\r\n");
		                  f.write("# TRIANGLES\r\n"+0+"\r\n\r\n");
		                  f.write("# GRIDNODES\r\n");
		                  for(int k=0;k<getNodeNum();k++){
		                          for(int i=0;i<getDimension();i++){
		                                  f.write(getNode(k).x[i]+" ");
		                          }
		                  f.write("\r\n");
		                  }
		                  f.write("\r\n# GRIDEDGES\r\n");
		                  for(int k=0;k<getEdgeNum();k++){
		                                  f.write(getNodeIndex(getEdge(k).getSource().key)+" "+getNodeIndex(getEdge(k).getTarget().key));
		                                  f.write("\r\n");
		                  }
		                  /*f.write("\r\n# GRIDRIBS\r\n");
		                  for(int k=0;k<ribsNum;k++){
		                	   f.write(getNodeIndex(getEdge(k).getSource())+" "+getNodeIndex(getEdge(k).getTarget()));
		                  f.write("\r\n");
		                  }*/

		                  f.write("\r\n# GRIDRIBS\r\n");
		                  for(int k=0;k<getStarNum();k++){
		                	  	  Star st = getStar(k);
		                	  	  f.write(getNodeIndex(st.centralNode.key)+" ");
		                	      for(int i=0;i<st.neighbours.size();i++){
		                	    	      Node n = (Node)st.neighbours.get(i);
		                                  f.write(getNodeIndex(n.key)+" ");
		                          }
		                  f.write("\r\n");
		                  }

		                  /*f.write("\r\n# GRIDTRIANGLES\r\n");
		                  for(int k=0;k<trianglesNum;k++){
		                          for(int i=0;i<3;i++){
		                                  f.write(Triangles[k][i]+" ");
		                          }
		                  f.write("\r\n");
		                  }*/
		                  f.write("\r\n# GRIDNODES2D\r\n");
		                  for(int k=0;k<getNodeNum();k++){
		                	      if(getNode(k).xin!=null)
		                          for(int i=0;i<2;i++){
		                                  f.write(getNode(k).xin[i]+" ");
		                          }
		                  f.write("\r\n");
		                  }
		                  f.write("\r\n# GRIDEP\r\n");
		                  for(int k=0;k<getEdgeNum();k++) f.write(getEdge(k).elasticity+" ");
		                  f.write("\r\n\r\n");
		                  f.write("# GRIDRP\r\n");
		                  for(int k=0;k<getStarNum();k++) f.write(getStar(k).elasticity+" ");
		                  f.write("\r\n\r\n");

		                  f.close();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
		  }

	  public double calcMSE(VDataSet data, Vector<Vector<Integer>> taxons){
          float vec[] = new float[getDimension()];
          double d = 0;
          for(int i=0;i<getNodeNum();i++){
                  Vector<Integer> tax = taxons.elementAt(i);
                  for(int k=0;k<getDimension();k++) vec[k] = 0;
                  if(!data.hasGaps)
                    for(int j=0;j<tax.size();j++){
                           d+=VVectorCalc.SquareEuclDistance(data.getVector(tax.get(j)),getNode(i).x);
                    }
                  else
                    for(int j=0;j<tax.size();j++){
                           d+=VVectorCalc.SquareEuclDistanceGap(data.getVector(tax.get(j)),getNode(i).x);
                    }
          }
          return d/data.pointCount;
      }
	  
	  public double calcMSETrimmed(VDataSet data, Vector<Vector<Integer>> taxons, double radius){
          float vec[] = new float[getDimension()];
          double rtrim2 = radius*radius;
          double d = 0;
          for(int i=0;i<getNodeNum();i++){
                  Vector<Integer> tax = taxons.elementAt(i);
                  for(int k=0;k<getDimension();k++) vec[k] = 0;
                  if(!data.hasGaps)
                    for(int j=0;j<tax.size();j++){
                    	   double r2 = VVectorCalc.SquareEuclDistance(data.getVector(tax.get(j)),getNode(i).x);
                    	   if(r2>rtrim2) r2 = rtrim2;
                           d+=r2;
                    }
                  else
                    for(int j=0;j<tax.size();j++){
                           double r2=VVectorCalc.SquareEuclDistanceGap(data.getVector(tax.get(j)),getNode(i).x);
                    	   if(r2>rtrim2) r2 = rtrim2;
                           d+=r2;
                    }
          }
          return d/data.pointCount;
      }
	  
	  
	  public double calcFVE(VDataSet data, Vector<Vector<Integer>> taxons){
		  double mse = calcMSE(data,taxons);
		  return 1-mse*mse/data.simpleStatistics.totalDispersion/data.simpleStatistics.totalDispersion;
	  }
	
	  public void setElasticityCoeffs(int degree, float coeff){
		  if(degree==1){
			  for(int i=0;i<Edges.size();i++){
				  Edge e = (Edge)Edges.get(i);
				  e.elasticity = coeff;
			  }
		  }else{
			  for(int i=0;i<Stars.size();i++){
				  Star s = (Star)Stars.get(i);
				  if(s.neighbours.size()==degree)
					  s.elasticity = coeff;
			  }
		  }
	  }
	  
	  public void setDefaultElasticityCoeff(int degree, float coeff){
			  StarDefaultElasticity[degree] = coeff;
	  }
	  
	  public void setDefaultElasticityCoeffs(float coeff){
		  float cf = coeff;
		  for(int i=2;i<this.MAX_STAR_DEGREE;i++){
			  setDefaultElasticityCoeff(i,cf);
			  //cf = cf*complexityFee;
		  }
      }
	  
	  public void setDefaultEdgeElasticityCoeff(float coeff){
		  setDefaultElasticityCoeff(1,coeff);
      }
	  
	  public GraphElement[] getAllSimpleElements(){
		  GraphElement res[] = new GraphElement[Nodes.size()+Edges.size()+Stars.size()];
		  int k=0;
		  for(int i=0;i<Nodes.size();i++)
			  res[k++] = (Node)Nodes.get(i);
		  for(int i=0;i<Edges.size();i++)
			  res[k++] = (Edge)Edges.get(i);
		  for(int i=0;i<Stars.size();i++)
			  res[k++] = (Star)Stars.get(i);
		  return res;
	  }
	  
	  public Graph clone(){
		  //return (Graph)clone();
		  Graph graph = new Graph();
		  for(int i=0;i<Nodes.size();i++)
			  graph.addNode(((Node)Nodes.get(i)).clone());
		  //for(int i=0;i<NodesInternal.size();i++)
		  //	  graph.NodesInternal.add(NodesInternal.get(i));
		  for(int i=0;i<Edges.size();i++)
			  graph.addEdge(((Edge)Edges.get(i)).clone(graph));
		  for(int i=0;i<Stars.size();i++)
			  graph.addStar(((Star)Stars.get(i)).clone(graph));
		  
		  graph.nodesCompiled = nodesCompiled;
		  graph.MAX_STAR_DEGREE = MAX_STAR_DEGREE;
		  //graph.complexityFee = complexityFee;
		  graph.StarDefaultElasticity  = new float[StarDefaultElasticity.length];
		  for(int i=0;i<graph.StarDefaultElasticity.length;i++)
			  graph.StarDefaultElasticity[i] = StarDefaultElasticity[i];
		  
		  graph.recalcIndexMaps();
		  graph.calcNodeInOut();
		  return graph;
	  }
	  
	  public void calcNodeInOut(){
		  outgoingEdges.clear();
		  starCenters.clear();
		  starLeaves.clear();
		  
		  for(int i=0;i<Edges.size();i++){
			  Edge e = (Edge)Edges.get(i);
			  Node source = e.getSource();
			  Node target = e.getTarget();
			  if(outgoingEdges.get(source.key)==null)
				  outgoingEdges.put(source.key, new Vector());
			  Vector v = (Vector)outgoingEdges.get(source.key);
			  v.add(e);
			  if(outgoingEdges.get(target.key)==null)
				  outgoingEdges.put(target.key, new Vector());
			  v = (Vector)outgoingEdges.get(target.key);
			  v.add(e);
		  }
		  for(int i=0;i<Stars.size();i++){
			  Star s = (Star)Stars.get(i);
			  if(starCenters.get(s.centralNode.key)==null)
				  starCenters.put(s.centralNode.key, new Vector());
			  Vector v = (Vector)starCenters.get(s.centralNode.key);
			  v.add(s);
			  for(int j=0;j<s.neighbours.size();j++){
				  Node neigh = (Node)s.neighbours.get(j);
				  if(starLeaves.get(neigh.key)==null)
					  starLeaves.put(neigh.key, new Vector());
			  	  v = (Vector)starLeaves.get(neigh.key);
			  	  v.add(s);
		      }
		  }
		  
	  }
	  
	  public void removeEdge(Edge e){
		  Integer I = (Integer)EdgeIndexMap.get(e.toString());
		  if(I==null){
			  System.out.println("ERROR: no edge found "+getNodeIndex(e.getSource().key)+","+getNodeIndex(e.getTarget().key));
		  }else{
			  int k = getEdgeIndex(e.toString());
			  Edges.remove(k);
		  }
		  recalcIndexMaps();
	  }
	  
	  public void removeStar(Star s){
		  Integer I = (Integer)StarIndexMap.get(s.toString());
		  if(I==null){
			  System.out.println("ERROR: no star found "+s.toString());
		  }else{
			  int k = getStarIndex(s.toString());
			  Stars.remove(k);
		  }
		  recalcIndexMaps();
	  }
	  
	  
	  public void removeNode(String key){
		  Integer I = (Integer)NodeIndexMap.get(key);
		  if(I==null){
			  System.out.println("ERROR: no node found "+key);
		  }else{
			  int k = getNodeIndex(key);
			  Nodes.remove(k);
		  }
		  recalcIndexMaps();
	  }
	  
	  
	  public void recalcIndexMaps(){
		  EdgeIndexMap.clear();
		  NodeIndexMap.clear();
		  StarIndexMap.clear();
		  for(int i=0;i<Nodes.size();i++){
			  NodeIndexMap.put((Nodes.get(i)).toString(), new Integer(i));
		  }
		  for(int i=0;i<Edges.size();i++){
			  EdgeIndexMap.put((Edges.get(i)).toString(), new Integer(i));
		  }
		  for(int i=0;i<Stars.size();i++){
			  StarIndexMap.put((Stars.get(i)).toString(), new Integer(i));
		  }
		  
		  for(int i=0;i<Edges.size();i++){
			  Edge e = (Edge)Edges.get(i);
			  if(getNodeIndex(e.getSource().key)==-1)
				  System.out.println("NOT FOUND , Edge "+i+" Source");
			  if(getNodeIndex(e.getTarget().key)==-1)
				  System.out.println("NOT FOUND , Star "+i+" Target");
		  }
		  for(int i=0;i<Stars.size();i++){
			  Star st = (Star)Stars.get(i);
			  for(int k=0;k<st.neighbours.size();k++){
				  Node n = (Node)st.neighbours.get(k);
				  if(getNodeIndex(n.key)==-1)
					  System.out.println("NOT FOUND , Star "+i+" Leaf "+k+" lab:'"+n.label+"'");
			  }
		  }
		  
	  }
	  
	  public String toString(){
		  String s = "";
		  for(int i=0;i<Edges.size();i++){
			  Edge e = (Edge)Edges.get(i);
			  String se = "("+getNodeIndex(e.getSource().key)+","+getNodeIndex(e.getTarget().key)+")";
			  //String se = "("+e.getSource().key+","+e.getTarget().key+")";
			  s = s+se+"|";  
		  }
		  /*for(int i=0;i<Stars.size();i++){
			  Star e = (Star)Stars.get(i);
			  String se = "("+getNodeIndex(e.centralNode.key);
			  for(int j=0;j<e.neighbours.size();j++){
				  Node n = (Node)e.neighbours.get(j);
				  se = se+","+getNodeIndex(n.key);
			  }
			  se = se+")";
			  s = s+"|"+se;  
		  }*/
		  return s;
	  }
	  
	  
	  public void writeOutNodes(String fn){
		    try{
		      FileWriter fw = new FileWriter(fn);
		      for(int i=0;i<Nodes.size();i++){
		        //float v[] = (float[])TreeNodes.elementAt(i);
		        for(int j=0;j<getDimension();j++){
		          fw.write(""+((Node)Nodes.get(i)).x[j]);
		          if(j<getDimension()-1)
		            fw.write("\t");
		        }
		        fw.write("\r\n");
		      }
		      fw.close();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
		  }
	  
	  public float[][] getNodePositions(){
		    float res[][] = new float[Nodes.size()][getDimension()];
		  	for(int i=0;i<Nodes.size();i++){
		        for(int j=0;j<getDimension();j++){
		          res[i][j] = Nodes.get(i).x[j]; 
		      }
		  	}
		  	return res;
	  }
	  

	  public void writeOutEdges(String fn){
		    try{
		      FileWriter fw = new FileWriter(fn);
		      for(int i=0;i<Edges.size();i++){
		        fw.write(getNodeIndex(((Edge)(Edges.get(i))).getSource().key)+"\t"+getNodeIndex(((Edge)(Edges.get(i))).getTarget().key)+"\r\n");
		      }
		      fw.close();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
		  }
	  
	  public int[][] getEdgeTable(){
		      int res[][] = new int[Edges.size()][2];
		      for(int i=0;i<Edges.size();i++){
		        int src = getNodeIndex(((Edge)(Edges.get(i))).getSource().key);
		        int trg = getNodeIndex(((Edge)(Edges.get(i))).getTarget().key);
		        res[i][0] = src;
		        res[i][1] = trg;
		      }
		      return res;
		  }


	  public void writeOutStars(String fn){
		    try{
		      FileWriter fw = new FileWriter(fn);
		      int maxstardegree = -1;
		      for(int i=0;i<Stars.size();i++){
			        Star s = (Star)Stars.elementAt(i);
			        if(s.neighbours.size()>maxstardegree)
			        	maxstardegree = s.neighbours.size();
		      }
		      //System.out.println("maxstardegree = "+maxstardegree);
		      for(int i=0;i<Stars.size();i++){
		        Star s = (Star)Stars.elementAt(i);
		        fw.write(""+getNodeIndex(s.centralNode.key)+"\t");
		        for(int j=0;j<s.neighbours.size();j++)
		        	fw.write(""+getNodeIndex(((Node)s.neighbours.get(j)).key)+"\t");
		        for(int j=s.neighbours.size();j<maxstardegree;j++)
		        	fw.write("-1\t");
		        fw.write("\r\n");
		      }
		      fw.close();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
		  }
	  
	  public HashMap<Integer,Integer> writeOutNodePointMap(VDataSet vd, String fn){
		    HashMap<Integer,Integer> map = new HashMap<Integer,Integer>(); 
		    try{
		      FileWriter fw = new FileWriter(fn);
		      for(int i=0;i<vd.pointCount;i++){
		    	  float x[] = vd.massif[i];
		    	  int k = getClosestNode(x);
		    	  fw.write(""+i+"\t"+k+"\n");
		    	  map.put(i, k);
		      }
		      fw.close();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
		    return map;
		  }
	  
	  public int[] countNumberOfPointsProjected(VDataSet vd){
		  int sizes[] = new int[Nodes.size()];
	      for(int i=0;i<vd.pointCount;i++){
	    	  float x[] = vd.massif[i];
	    	  int k = getClosestNode(x);
	    	  sizes[k]++;
	      }
	      return sizes;
	  }
	  
	  
	  public String getSimpleTopologyCode(){
		  String res = "";
		  int sn[] = new int[MAX_STAR_DEGREE];
		  for(int i=0;i<Stars.size();i++){
			  Star s = (Star)Stars.get(i);
			  sn[s.neighbours.size()]++;
		  }
		  boolean wr = false;
		  for(int i=MAX_STAR_DEGREE-1;i>=3;i--){
			  if(sn[i]!=0)
				  wr = true;
			  if(wr)
				  res+=sn[i]+"|";
		  }
		  if(res.length()>0)
			  res = res.substring(0,res.length()-1);
		  else
			  res = "0";
		  res+="||"+getNodeNum();
		  return res;
	  }
	  
	  public float getElasticitiesSum(){
		  float res = 0f;
		  for(int i=0;i<Stars.size();i++){
			  res+=((Star)Stars.get(i)).elasticity;
		  }
		  return res;
	  }

	  public float[] projectPoint(float point[], int projectionType){

		  float res[] = new float[point.length];
		  float r, minr = Float.MAX_VALUE;
		  int minj = -1;
		                  for(int j=0;j<getNodeNum();j++){ 
		                               r = VVectorCalc.SquareEuclDistanceShift(NodeArrayCopy,j*getDimension(),point,getDimension());
		                          if(r<minr){ minr=r; minj = j; }
		                  }
		                  if(projectionType==PROJECTION_CLOSEST_POINT){

		                	  Vector<Edge> closeedges = new Vector<Edge>();
		                	  for(int k=0;k<getEdgeNum();k++){
		                		  if(Edges.get(k).getSource().key.equals(Nodes.get(minj).key)) 
		                			  closeedges.add(Edges.get(k));
		                		  if(Edges.get(k).getTarget().key.equals(Nodes.get(minj).key)) 
		                			  closeedges.add(Edges.get(k));
		                	  }
		                	  
		                	  if(closeedges.size()==0)
		                		  System.out.println("ERROR: Node "+minj+"("+getNode(minj).key+") does not belong to any edge");
		                	  
		                	  float projections[][] = new float[closeedges.size()][getDimension()];
		                	  double mindist = Double.POSITIVE_INFINITY;
		                	  int kmin = -1;
		                	  for(int k=0;k<closeedges.size();k++){
		                		  String key1 = closeedges.get(k).getSource().key;
		                		  String key2 = closeedges.get(k).getTarget().key;
		                		  float source[] = NodeArray[getNodeIndex(key1)];
		                		  float target[] = NodeArray[getNodeIndex(key2)];
		                		  float u = VVectorCalc.projectOnSegment(point, source, target)[0];
		                		  float diff[] = VVectorCalc.Subtract_(target, source);
		                		  VVectorCalc.Mult(diff, u);
		                		  projections[k] = VVectorCalc.Add_(source, diff);
		                		  double distanceToEdge = VVectorCalc.Distance(point, projections[k]);
		                		  //System.out.println("Distance = "+distanceToEdge);
		                		  if(distanceToEdge<mindist){ 
		                			    mindist = distanceToEdge; 
		                			    kmin = k; 
		                			    //System.out.println(kmin);
		                		   }
		                	  }
		                	  res = projections[kmin];
		                  
		                  }else{
		                    res = NodeArray[minj];
		                  }
		  return res;
		  }

		  public float[] projectPointGap(float point[], int projectionType){
			  float res[] = new float[point.length];
			  float r, minr = Float.MAX_VALUE;
			  int minj = -1;
			                  for(int j=0;j<getNodeNum();j++){ 
			                               r = VVectorCalc.SquareEuclDistanceShiftGap(NodeArrayCopy,j*getDimension(),point,getDimension());
			                          if(r<minr){ minr=r; minj = j; }
			                  }
			                  if(projectionType==PROJECTION_CLOSEST_POINT){

			                	  Vector<Edge> closeedges = new Vector<Edge>();
			                	  for(int k=0;k<getEdgeNum();k++){
			                		  if(Edges.get(k).getSource().key.equals(Nodes.get(minj).key)) closeedges.add(Edges.get(k));
			                		  if(Edges.get(k).getTarget().key.equals(Nodes.get(minj).key)) closeedges.add(Edges.get(k));
			                	  }
			                	  
			                	  float projections[][] = new float[closeedges.size()][getDimension()];
			                	  double mindist = Double.NEGATIVE_INFINITY;
			                	  int kmin = -1;
			                	  for(int k=0;k<closeedges.size();k++){
			                		  float source[] = NodeArray[getNodeIndex(closeedges.get(k).getSource().key)];
			                		  float target[] = NodeArray[getNodeIndex(closeedges.get(k).getTarget().key)];
			                		  float u = VVectorCalc.projectOnSegmentGap(point, source, target)[0];
			                		  float diff[] = VVectorCalc.Subtract_(target, source);
			                		  VVectorCalc.Mult(diff, u);
			                		  projections[k] = VVectorCalc.Add_(source, diff);
			                		  double distanceToEdge = VVectorCalc.SquareEuclDistanceGap(point, projections[k]);
			                		  if(distanceToEdge<mindist)
			                		  	{ mindist = distanceToEdge; kmin = k; }
			                	  }
			                	  res = projections[kmin];
			                  
			                  }else{
			                    res = NodeArray[minj];
			                  }
			  return res;		  
		}


		  public int getClosestNode(float point[]){
		  float r, minr = Float.MAX_VALUE;
		  int minj = -1;
		                  for(int j=0;j<getNodeNum();j++){
		                               r = VVectorCalc.SquareEuclDistanceShift(NodeArrayCopy,j*getDimension(),point,getDimension());
		                          if(r<minr){ minr=r; minj = j; }
		                  }
		  return minj;
		  }

		  public int getClosestNodeGap(float point[]){
		  float r, minr = Float.MAX_VALUE;
		  int minj = -1;
		                  for(int j=0;j<getNodeNum();j++){
		                               r = VVectorCalc.SquareEuclDistanceShiftGap(NodeArrayCopy,j*getDimension(),point,getDimension());
		                          if(r<minr){ minr=r; minj = j; }
		                  }
		  return minj;
		  }

	  
			  public float calcMSEToProjection(VDataSet vd, int projectionType){
			    float mse = 0f;
			    float x2 = 0f;
			    for(int i=0;i<vd.pointCount;i++){
			      float x[] = vd.getVector(i);
			      float p[] = null;
			      //p = projectionFunction(x);
			      if(vd.hasGaps)
			    	  p = projectPointGap(x, projectionType);
			      else
			    	  p = projectPoint(x, projectionType);
			      //float xp[] = projectFromInToOut(p);
			      float xp[] = p;
			      //float pcheck[] = projectionFunction(xp);
			      //System.out.println(pcheck[0]-p[0]);
			      
			      /*float pcap[] = pca.projectionFunction(x);
			      float pcax[] = pca.projectFromInToOut(pcap);
			      System.out.println(pcax[0]+"\t"+xp[0]);*/
			      
			      float d = 0;
			      if(vd.hasGaps)
			        d = (float)VVectorCalc.SquareEuclDistanceGap(x,xp);
			      else
			        d = (float)VVectorCalc.SquareEuclDistance(x,xp);
			      x2 += d;
			    }
			    x2 /= vd.pointCount;
			    mse = (float)Math.sqrt(x2);
			    //mse = x2;
			    return mse;
			  }
			  
			  /*
			   * Produces stars definitions from the structure of the graph (primitive graph case)
			   */
			  public void defineStarsFromPrimitiveGraphStructure(){
				  calcNodeInOut();
				  for(int i=0;i<Nodes.size();i++){
					  String id = Nodes.get(i).key;
					  Vector<Edge> edges = outgoingEdges.get(id);
					  if(edges==null){
						  System.out.println("Graph might be not connected! Do not find an edge for "+id);
					  }else{
					  if(edges.size()>1){
						  Star s = new Star();
						  s.centralNode = Nodes.get(i);
						  s.neighbours = new Vector<Node>();
						  for(int k=0;k<edges.size();k++){
							  Edge e = edges.get(k);
							  if(e.getSource().key.equals(id))
								  s.neighbours.add(e.getTarget());
							  if(e.getTarget().key.equals(id))
								  s.neighbours.add(e.getSource());
						  }
						  addStar(s);
					  }
				  }}
			  }
			  
			  public Node getStarCentralNodeMean(){
				  Node n = null;
				  float m[][] = getNodePositions();
				  float meanv[] = new float[m[0].length];
				  for(int i=0;i<meanv.length;i++) {
					  float vec[] = new float[m.length]; 
					  for(int k=0;k<vec.length;k++) vec[k] = m[k][i];
					  meanv[i] = vdaoengine.utils.VSimpleFunctions.calcMean(vec);
				  }
				  int mini = -1; float mindist = Float.MAX_VALUE;
				  for(int k=0;k<m.length;k++){
					  if(getStarByCentralNode(k)!=null){
					  float dist = (float)vdaoengine.utils.VVectorCalc.Distance(m[k], meanv);
					  if(dist<mindist){
						  mindist = dist; mini = k;
					  }
					  }
				  }
				  if(mini>=0)
					  n = Nodes.get(mini);
				  return n;
			  }
			  
			  public int findEdgeIndex(int k1, int k2){
				    int res = -1;
				    Node n1 = Nodes.get(k1);
				    Node n2 = Nodes.get(k2);
				    for(int i=0;i<Edges.size();i++){
				      if(Edges.get(i).getSource().key.equals(n1.key)&&Edges.get(i).getTarget().key.equals(n2.key))
				        res = i;
				      if(Edges.get(i).getTarget().key.equals(n1.key)&&Edges.get(i).getSource().key.equals(n2.key))
				        res = i;
				    }
				    return res;
			}
			  
			public Star getStarByCentralNode(int centralnode){
				    Star r = null;
				    for(int i=0;i<Stars.size();i++){
				    	if(Stars.get(i).centralNode.key.equals(Nodes.get(centralnode).key))
				    			r = Stars.get(i);
				    }
				    return r;
			}
			  
			  
}
