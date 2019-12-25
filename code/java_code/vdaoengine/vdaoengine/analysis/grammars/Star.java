package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;
import vdaoengine.utils.*;

public class Star extends GraphElement {

	/*
	 * Elasticity coefficient: 0 - no connection, +Inf - absolutely rigid
	 */
	public float elasticity = 1f;  
	
	public Node centralNode = null;
	public Vector<Node> neighbours = new Vector<Node>();
	
	/*public Star(Node _centralNode, Vector _neighbours){
		centralNode = _centralNode;
		neighbours = _neighbours;
	}*/
	

	public double getEnergy(){
		double energy = 0;
		double center[] = new double[centralNode.x.length];
		for(int i=0;i<neighbours.size();i++){
			VVectorCalc.Add(center, ((Node)neighbours.get(i)).x);
		}
		VVectorCalc.Mult(center,1.0f/neighbours.size());
		if(test.variant==0)
		   energy = elasticity*neighbours.size()*neighbours.size()*VVectorCalc.SquareDistance(center, centralNode.x);
		if(test.variant==1)
			energy = elasticity*VVectorCalc.SquareDistance(center, centralNode.x);
		return energy;
	}
	
	public Star clone(Graph gr){
		Star s = new Star(); 
		s.centralNode = gr.getNode(centralNode.key);
		s.elasticity = elasticity;
		s.neighbours = new Vector(); for(int i=0;i<neighbours.size();i++) s.neighbours.add( gr.getNode(((Node)neighbours.get(i)).key) );
		copyProperties(s);
		return s;
	}
	
	public String toString(){
		  String s = centralNode.key;
		  for(int i=0;i<neighbours.size();i++)
			  s+="_"+((Node)neighbours.get(i)).key;
		  return s;	
	}

	
}
