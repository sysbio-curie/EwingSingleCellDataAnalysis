package vdaoengine.analysis.grammars;

import java.util.Vector;
import vdaoengine.data.*;


public class RemoveInternalEdge extends GrammarOperation {
	
	public boolean applyToElement(Graph graph, VDataSet data, Vector taxons, GraphElement el) {
		boolean applicable = false;
		if(el instanceof Edge){
			Edge e = (Edge)el;
			if(((Vector)graph.outgoingEdges.get(e.getSource().key)).size()>1)if(((Vector)graph.outgoingEdges.get(e.getTarget().key)).size()>1){
				applicable = true;
				Star s1 = (Star)((Vector)graph.starCenters.get(e.getSource().key)).get(0);
				Star s2 = (Star)((Vector)graph.starCenters.get(e.getTarget().key)).get(0);
				int i = 0;
				
				int k=-1;
				for(int j=0;j<s1.neighbours.size();j++)
					if(((Node)s1.neighbours.get(j)).key.equals(s2.centralNode.key))
						k = j;
				if(k!=-1)
					s1.neighbours.remove(k);
				//for(int i=0;i<s2.neighbours.size();i++){
				while(i<s2.neighbours.size()){
					Edge ee = new Edge(s2.centralNode,(Node)s2.neighbours.get(i));
					if(graph.getEdgeIndex(ee.toString())>=0)
						graph.removeEdge(ee);
					ee = new Edge((Node)s2.neighbours.get(i),s2.centralNode);
					if(graph.getEdgeIndex(ee.toString())>=0)
						graph.removeEdge(ee);					
					boolean found = false;
					for(int j=0;j<s1.neighbours.size();j++)
						if(((Node)s1.neighbours.get(j)).key.equals(((Node)s2.neighbours.get(i)).key))
							found = true;
					if(((Node)s2.neighbours.get(i)).key.equals(s1.centralNode.key))
						    found = true;
					if(!found){
						s1.neighbours.add(s2.neighbours.get(i));
						graph.addEdge(s1.centralNode,(Node)s2.neighbours.get(i));
					}
					//if(((Node)s2.neighbours.get(i)).key.equals(s1.centralNode.key)){
					//	s2.neighbours.remove(i);
					//}else
						i++;
				}
				s1.elasticity = graph.StarDefaultElasticity[s1.neighbours.size()];
				graph.removeStar(s2);
				// Now for all stars containing s.centralNode, modify leaves
				Vector leaves = (Vector)graph.starLeaves.get(s2.centralNode.key);
				if(leaves!=null)
				for(int j=0;j<leaves.size();j++){
					Star s = (Star)leaves.get(j);
					int kk = -1;
					for(int m=0;m<s.neighbours.size();m++){
						String key = ((Node)s.neighbours.get(m)).key;
						if(((Node)s.neighbours.get(m)).key.equals(s2.centralNode.key))
							kk = m;
					}
					if(kk!=-1){
						s.neighbours.remove(kk);
						s.neighbours.add(s1.centralNode);
					}
				}
				graph.removeNode(s2.centralNode.key);				
			}
		}
		return applicable;
	}

}
