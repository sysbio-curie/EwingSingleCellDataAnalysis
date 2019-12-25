package vdaoengine.analysis;

import vdaoengine.TableUtils;
import vdaoengine.analysis.grammars.Edge;
import vdaoengine.analysis.grammars.Graph;
import vdaoengine.analysis.grammars.Node;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.VSimpleProcedures;
import vdaoengine.utils.VVectorCalc;

public class KNNGraph {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/ElGraph_Matlab/test_data/tree23_noise.txt", true, "\t");
			TableUtils.findAllNumericalColumns(vt);
			VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
			int k = 10;
			Graph knn = ConstructKNNGraph(vd,k);
			knn.writeOutEdges("C:/Datas/ElGraph_Matlab/test_data/tree23_noise.knn"+k+".edges");
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static Graph ConstructKNNGraph(VDataSet vd, int k){
		Graph knn = new Graph();
		for(int i=0;i<vd.pointCount;i++){
			Node n = new Node();
			n.label = "Node"+i;
			knn.addNode(n);
		}
		for(int i=0;i<vd.pointCount;i++){
			float x[] = vd.massif[i];
			float dist[] = new float[vd.pointCount];
			for(int j=0;j<vd.pointCount;j++){
				dist[j] = VVectorCalc.SquareDistance(x, vd.massif[j]);
			}
			int inds[] = Algorithms.SortMass(dist);
			for(int s=0;s<=k;s++)if(inds[s]!=i){
				Edge e = new Edge(knn.Nodes.get(i),knn.Nodes.get(inds[s]));
				knn.addEdge(e);
			}
		}
		return knn;
	}

}
