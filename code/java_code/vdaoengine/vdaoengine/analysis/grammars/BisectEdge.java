package vdaoengine.analysis.grammars;

import java.util.*;

import vdaoengine.utils.*;
import vdaoengine.data.*;

public class BisectEdge extends GrammarOperation {

	public boolean applyToElement(Graph graph, VDataSet data, Vector taxons, GraphElement el) {
		boolean applicable = false;
		if(el instanceof Edge){
			
			//if(graph.toString().equals("(2,1)|(0,3)|(3,2)|")){
			//	System.out.println("("+ +")");
			//}
			
			applicable = true;
			Edge e = (Edge)el;
			graph.removeEdge(e);
			float x[] = new float[graph.getDimension()];
			x = VVectorCalc.Product_(VVectorCalc.Add_(e.getSource().x, e.getTarget().x), 0.5f);
			Node n = new Node();
			n.setX(x);
			graph.addNode(n);
			
			// Add new edge
			graph.addEdge(e.getSource(), n);
			graph.addEdge(n, e.getTarget());
			
			// Modify stars
			Vector centers = (Vector)graph.starCenters.get(e.getSource().key);
			if(centers!=null)
			for(int i=0;i<centers.size();i++){
				Star s = (Star)centers.get(i);
				for(int j=0;j<s.neighbours.size();j++){
					Node nn = (Node)s.neighbours.get(j);
					if(nn.key.equals(e.getTarget().key)){
						s.neighbours.set(j, n);
					}
				}
			}
			centers = (Vector)graph.starCenters.get(e.getTarget().key);
			if(centers!=null)
			for(int i=0;i<centers.size();i++){
				Star s = (Star)centers.get(i);
				for(int j=0;j<s.neighbours.size();j++){
					Node nn = (Node)s.neighbours.get(j);
					if(nn.key.equals(e.getSource().key)){
						s.neighbours.set(j, n);
					}
				}
			}
			
			// Adding new Rib
			Vector neighbours = new Vector();
			neighbours.add(e.getSource());
			neighbours.add(e.getTarget());
			graph.addStar(n, neighbours);
			
		}
		return applicable;
	}

}
