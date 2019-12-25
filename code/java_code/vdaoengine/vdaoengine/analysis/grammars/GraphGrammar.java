package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.utils.*;

public class GraphGrammar {
	
	public Vector operations = new Vector();

	public Vector applyAllPossibleTransformations(Graph graph, VDataSet data, Vector taxons){
		Vector res = new Vector();
		for(int i=0;i<operations.size();i++){
			GrammarOperation op = (GrammarOperation)operations.get(i);
			Vector v = op.applyAll(graph,data,taxons);
			//System.out.println(op.getClass().getCanonicalName()+", "+v.size()+" possible transformations");
			for(int j=0;j<v.size();j++)
				res.add(v.get(j));
		}
		return res;
	}
	
}
