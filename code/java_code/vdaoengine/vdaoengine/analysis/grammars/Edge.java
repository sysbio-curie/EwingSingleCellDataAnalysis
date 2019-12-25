package vdaoengine.analysis.grammars;

import vdaoengine.utils.VVectorCalc;

public class Edge extends Star {

	public Edge(Node source, Node target){
		centralNode = source;
		neighbours.add(target);
	}
	
	public Node getSource(){
		return centralNode;
	}
	public void setSource(Node source){
		centralNode = source;
	}
	
	public double getLength(){
		double length = 0;
		length = VVectorCalc.Distance(getSource().x, getTarget().x);
		return length;
	}
	
	public Node getTarget(){
		if(neighbours.size()>0)
			return (Node)neighbours.get(0);
		else
			return null;
	}
	public void setTarget(Node target){
		neighbours.clear();
		neighbours.add(target);
	}
	
	public boolean equals(Edge e){
		boolean res = false;
		if((getSource().equals(e.getSource()))&&(getTarget().equals(e.getTarget())))
				res = true;
		return res;
	}
	
	public Edge clone(Graph gr){
		Node src = gr.getNode(getSource().key);
		Node tgt = gr.getNode(getTarget().key);
		Edge e = new Edge(src,tgt); 
		e.elasticity = elasticity;
		copyProperties(e);
		return e;
	}
	
	public String toString(){
	  return getSource().key+"_"+getTarget().key;	
	}
	
	
	
}
