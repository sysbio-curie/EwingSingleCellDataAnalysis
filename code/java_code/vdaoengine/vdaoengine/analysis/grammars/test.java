package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class test {
	
	public static int variant = 1;

	public static void main(String[] args) {
		
		//String project = "4points";
		String project = "tree23";
		//String project = "iris";
		//VDataTable vt = VDatReadWrite.LoadFromVDatFile("c:/datas/elastictree/iris.dat");
		VDataTable vt = VDatReadWrite.LoadFromVDatFile("c:/datas/elastictree/"+project+".dat");
		//VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		Graph graph = new Graph();
		
		//graph.setDefaultElasticityCoeffs(1, 0.05f);
		
		int numberOfNodes = 40;

		//graph.complexityFee = 1f;
		graph.setDefaultEdgeElasticityCoeff(1e-2f);
		graph.setDefaultElasticityCoeffs(1e-2f);		
		/*if(variant==0){
		   graph.setDefaultElasticityCoeffs(2, 0.01f/4);
   		   graph.setDefaultElasticityCoeffs(3, 0.01f/9);
		   graph.setDefaultElasticityCoeffs(4, 0.01f/16);
		   graph.setDefaultElasticityCoeffs(5, 0.01f/25);
		}*/
		
		
		GraphGrammar grammarGrow = new GraphGrammar(); 
		BisectEdge be = new BisectEdge();
		AddNodeToNode an = new AddNodeToNode(); 
		grammarGrow.operations.add(be);
		grammarGrow.operations.add(an);
		
		GraphGrammar grammarShrink = new GraphGrammar();
		RemoveLeaf rl = new RemoveLeaf();
		RemoveInternalEdge rie = new RemoveInternalEdge(); 
		grammarShrink.operations.add(rl);
		grammarShrink.operations.add(rie);
		
		ElasticEnergyOptimization elo = new ElasticEnergyOptimization(vd,graph);
		
		BaseOptimizationAlgorithm alg = new BaseOptimizationAlgorithm(vd);
		
		//alg.setElementFeeLinear(0.001f, 1f);
		alg.setElementFeeExponential(1e-4f, 3f);
		
		
		alg.parameters.maxNumberOfNodes = numberOfNodes;
		alg.movieFolder = "c:/datas/elastictree/movie/";
		alg.grammars.add(grammarGrow);
		alg.grammars.add(grammarGrow);
		alg.grammars.add(grammarShrink);
		alg.setGraph(graph);
		alg.initializeGraph();
		
		/*Vector graphs = grammarGrow.applyAllPossibleTransformations(graph);
		System.out.println(""+graphs.size()+" graphs generated:");
		for(int i=0;i<graphs.size();i++){
			Graph gr = (Graph)graphs.get(i);
			System.out.println(i+": "+gr.toString());
		}*/
		
		
		alg.run(elo);
		//graph.setElasticityCoeffs(1, 0.0001f);
		//alg.run(elo);
		
		alg.graph.recalcIndexMaps();
		alg.graph.saveToFile("c:/datas/elastictree/"+project+".vem",project);
		
		System.out.println("Code = "+elo.graph.getSimpleTopologyCode());
		
		if(alg.convergedByComplexity)
			System.out.println("Converged by complexity");
		
		elo.updateEnergyValue();
		
		System.out.println("MSE = "+alg.graph.calcMSE(vd, elo.taxons));
		System.out.println("Energy = "+elo.energyValue);
		
		for(int i=0;i<elo.graph.getStarNum();i++){
			Star s = elo.graph.getStar(i);
			//s.elasticity = 0.01f;
			//System.out.println(s.neighbours.size()+" -> "+s.elasticity);
		}
		test.variant = 1;
		elo.updateEnergyValue();
		System.out.println("MSE1 = "+alg.graph.calcMSE(vd, elo.taxons));
		System.out.println("Total = "+alg.dataset.simpleStatistics.totalDispersion);
		System.out.println("Energy1 = "+elo.energyValue);
		System.out.println("ElSum = "+elo.graph.getElasticitiesSum());		
		

	}

}
