package vdaoengine.analysis.grammars;

import java.util.Vector;
import vdaoengine.data.*;

public abstract class GrammarOperation {
	
	public abstract boolean applyToElement(Graph graph, VDataSet data, Vector<Vector<Integer>> taxons, GraphElement el);
	
	public Vector applyAll(Graph graph, VDataSet data, Vector taxons){
		Vector res = new Vector();
		GraphElement els[] = graph.getAllSimpleElements();
		graph.calcNodeInOut();
		for(int i=0;i<els.length;i++){
			Graph gr = graph.clone(); 
			if(gr.getNodeNum()!=graph.getNodeNum())
				System.out.println("ERROR in cloning");
			if(applyToElement(gr, data, taxons, els[i]))
				res.add(gr);
		}
		return res;
	}

}
