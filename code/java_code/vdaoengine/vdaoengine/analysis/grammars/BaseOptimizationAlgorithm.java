package vdaoengine.analysis.grammars;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import corejava.Format;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.cluster.VKMeansClusterization;

public class BaseOptimizationAlgorithm {

	public static int INIT_TWO_NODES_ALONG_PC1 = 0;
	public static int INIT_TWO_POINTS_IN_DENSEST_CLUSTER = 1;
	public static int INIT_FOUR_POINTS_CLOSED_LINE_PC12 = 2;	
	public static int INIT_PREDEFINED_CONFIGURATION = -1;
	public static int CONVERGENCE_MAXNUMNODES = 0;
	public static int CONVERGENCE_APPROXIMATION_ERROR = 1;
	
	public String movieFolder = null;
	public int iteration = 0;
	
	public float elementFees[] = null;
	
	public boolean verbose = false;
	public boolean includeImageBeforeOptimization = true;
	public Graph memGraphBeforeOptimization = null;
	
	public boolean convergedByComplexity = false;
	
	public class Parameters{
		public int initStrategy = INIT_TWO_NODES_ALONG_PC1;
		public int convergenceCriterion = CONVERGENCE_MAXNUMNODES;
		public float length = 0.5f;
		public int maxNumberOfNodes = 30;
		public float eps = 1e-3f;
	}

	/*
	 * You should set a dataset and a set of graph grammars to apply to run the algorithm
	 */
	public VDataSet dataset = null;
	/*
	 * You should set a dataset and a set of graph grammars to apply to run the algorithm
	 */
	public Vector grammars = new Vector();

	public Graph graph = null;
	
	public Parameters parameters = null;
	
	public BaseOptimizationAlgorithm(VDataSet vd, Vector grs){
		dataset = vd;
		grammars = grs;
		parameters = new BaseOptimizationAlgorithm.Parameters();
	}
	
	public BaseOptimizationAlgorithm(VDataSet vd){
		dataset = vd;
		parameters = new BaseOptimizationAlgorithm.Parameters();		
	}
	
	private ElasticEnergyOptimization optimization = null;
	
	public void setGraph(Graph gr){
		graph = gr;
	}
	
	public String run(ElasticEnergyOptimization _optimization){
		String report = "";
		optimization = _optimization;
		optimization.optimize();
		//System.out.println("Number of nodes = "+graph.getNodeNum());
		String line = "STEP\tBARCODE\tENERGY\tNNODES\tNEDGES\tNRIBS\tNSTARS\tNRAYS-2\tMSE\tMSEP\tFVE\tFVEP\tUE\tUR\tURN\tURN2\tURSD";
		System.out.println(line);
		report+=line+"\n";
		while(!checkForConvergence()){
			
			for(int i=0;i<grammars.size();i++){
				iteration++;
				doOptimalTransformation((GraphGrammar)grammars.get(i));
				if(movieFolder!=null){
					
					if(includeImageBeforeOptimization){
			            String siter = ""+iteration;
			            if(siter.length()==1) siter="00"+siter;
			            else
			            	if(siter.length()==2) siter="0"+siter;
			            memGraphBeforeOptimization.writeOutNodes(movieFolder+"map"+siter);
			            memGraphBeforeOptimization.writeOutEdges(movieFolder+"nedge"+siter);
			            memGraphBeforeOptimization.writeOutStars(movieFolder+"nstar"+siter);
						iteration++;
					}
		            String siter = ""+iteration;
		            if(siter.length()==1) siter="00"+siter;
		            else
		            	if(siter.length()==2) siter="0"+siter;
					graph.writeOutNodes(movieFolder+"map"+siter);
					graph.writeOutEdges(movieFolder+"nedge"+siter);
					graph.writeOutStars(movieFolder+"nstar"+siter);
				}
			}
			float elementFee = calcElementFee(graph);
			optimization.updateEnergyValue();
			optimization.graph.compileNodesInArrays();
			//float mse1 = optimization.graph.calcMSEToProjection(optimization.dataset, Graph.PROJECTION_CLOSEST_NODE);
			float msep = optimization.graph.calcMSEToProjection(optimization.dataset, Graph.PROJECTION_CLOSEST_POINT);
			float fvep = 1-msep*msep/optimization.dataset.simpleStatistics.totalDispersion/optimization.dataset.simpleStatistics.totalDispersion;
			line = ""+iteration+"\t"+optimization.graph.getSimpleTopologyCode()+"\t"+optimization.energyValue+"\t"+optimization.graph.getNodeNum()+"\t"+optimization.graph.getEdgeNum()+"\t"+optimization.graph.getRibNum()+"\t"+(optimization.graph.getStarNum()-optimization.graph.getRibNum())+"\t"+optimization.graph.getRayNum()+"\t"+optimization.mseValue+"\t"+msep+"\t"+optimization.graph.calcFVE(optimization.dataset, optimization.taxons)+"\t"+fvep+"\t"+optimization.UEValue+"\t"+optimization.URValue+"\t"+optimization.URValue*optimization.graph.getNodeNum()+"\t"+optimization.URValue*optimization.graph.getNodeNum()*optimization.graph.getNodeNum()+"\t"+optimization.URValueAsSecondDerivative;
			System.out.println(line);
			report+=line+"\n";
			//System.out.println("Number of nodes = "+graph.getNodeNum());
		}
		return report;
	}
	
	public float calcElementFee(Graph _graph){
		float fee = 0f;
		if(elementFees!=null){
			fee+=elementFees[0]*_graph.getNodeNum();
			fee+=elementFees[1]*_graph.getEdgeNum();
			for(int i=0;i<_graph.getStarNum();i++){
				Star s = (Star)_graph.getStar(i);
				fee+=elementFees[s.neighbours.size()];
				//System.out.println("Star "+s.neighbours.size()+" "+elementFees[s.neighbours.size()]);
			}
		}
		fee*=dataset.simpleStatistics.totalDispersion;
		//System.out.println("Total fee = "+fee);
		return fee;
	}
	
	public void initializeGraph(){
		if(parameters.initStrategy==INIT_TWO_NODES_ALONG_PC1){
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(dataset);
			pca.calcBasis(1);
			dataset.calcStatistics();
			double c1[] = new double[dataset.coordCount];
			double c2[] = new double[dataset.coordCount];
			for(int i=0;i<dataset.coordCount;i++){
				c1[i] = dataset.simpleStatistics.getMean(i);
				c2[i] = dataset.simpleStatistics.getMean(i);
			}
			Node n1 = new Node();
			Node n2 = new Node();			
			VVectorCalc.Mult(pca.getBasis().basis[0],parameters.length*dataset.simpleStatistics.totalDispersion);
			VVectorCalc.Add(c1,pca.getBasis().basis[0]);
			n1.setX(c1);
			VVectorCalc.Subtr(c2,pca.getBasis().basis[0]);
			n2.setX(c2);
			graph.addNode(n1); graph.addNode(n2);
			Edge e = graph.addEdge(n1,n2);
		}
		if(parameters.initStrategy==INIT_TWO_POINTS_IN_DENSEST_CLUSTER){
		    //VKMeansClusterization vk = new VKMeansClusterization(parameters.maxNumberOfNodes);
			VKMeansClusterization vk = new VKMeansClusterization(100);
		    vk.doClusterization(dataset);
		    vk.calculateClassListsFromCentroids(dataset);
		    int maxi = -1; int maxsize = -1;
		    for(int i=0;i<vk.classLists.size();i++){
		    	int size = vk.classLists.get(i).length;
		    	if(size>maxsize){
		    		maxsize = size;
		    		maxi = i;
		    	}
		    }
			Node n1 = new Node();
			Node n2 = new Node();
			float f1[] = dataset.massif[vk.classLists.get(maxi)[0]];
			float f2[] = dataset.massif[vk.classLists.get(maxi)[1]];
			n1.setX(f1);
			n2.setX(f2);
			graph.addNode(n1); graph.addNode(n2);
			Edge e = graph.addEdge(n1,n2);
		}
		if(parameters.initStrategy==INIT_FOUR_POINTS_CLOSED_LINE_PC12){
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(dataset);
			pca.calcBasis(2);
			dataset.calcStatistics();
			double c1[] = new double[dataset.coordCount];
			double c2[] = new double[dataset.coordCount];
			double c3[] = new double[dataset.coordCount];
			double c4[] = new double[dataset.coordCount];
			for(int i=0;i<dataset.coordCount;i++){
				c1[i] = dataset.simpleStatistics.getMean(i);
				c2[i] = dataset.simpleStatistics.getMean(i);
				c3[i] = dataset.simpleStatistics.getMean(i);
				c4[i] = dataset.simpleStatistics.getMean(i);				
			}
			Node n1 = new Node();
			Node n2 = new Node();			
			Node n3 = new Node();
			Node n4 = new Node();			
			VVectorCalc.Mult(pca.getBasis().basis[0],parameters.length*dataset.simpleStatistics.totalDispersion);
			VVectorCalc.Mult(pca.getBasis().basis[1],parameters.length*dataset.simpleStatistics.totalDispersion);
			
			// Right bottom corner
			VVectorCalc.Add(c1,pca.getBasis().basis[0]);
			VVectorCalc.Subtr(c1,pca.getBasis().basis[1]);
			n1.setX(c1);
			
			// Right top corner
			VVectorCalc.Add(c2,pca.getBasis().basis[0]);
			VVectorCalc.Add(c2,pca.getBasis().basis[1]);
			n2.setX(c2);

			// Left top corner
			VVectorCalc.Subtr(c3,pca.getBasis().basis[0]);
			VVectorCalc.Add(c3,pca.getBasis().basis[1]);
			n3.setX(c3);

			// Left bottom corner
			VVectorCalc.Subtr(c4,pca.getBasis().basis[0]);
			VVectorCalc.Subtr(c4,pca.getBasis().basis[1]);
			n4.setX(c4);
			
			graph.addNode(n1); graph.addNode(n2);
			graph.addNode(n3); graph.addNode(n4);
			graph.addEdge(n1,n2);
			graph.addEdge(n2,n3);
			graph.addEdge(n3,n4);
			graph.addEdge(n4,n1);
			graph.defineStarsFromPrimitiveGraphStructure();
		}
		if(parameters.initStrategy==INIT_PREDEFINED_CONFIGURATION){
			// graph = graph should be defined elsewhere
			//System.out.println("Elasticity = "+graph.Edges.get(0).elasticity);
		}
	}
	
	public void doOptimalTransformation(GraphGrammar grammar){

		float energyOld = optimization.energyValue;
		//System.out.print("i>");
		float efeeOld = calcElementFee(graph);
		int nodesOld = graph.getNodeNum();
		
		//graph.saveToFile("C:/Datas/ElGraph_Matlab/test_data/tree23_before.vem", "tree23");
		
		if(verbose){
			//System.out.println(i+": "+optimization.graph.toString()+" ("+optimization.energyValue+","+efee+","+(optimization.energyValue+efee)+") "+optimization.graph.getSimpleTopologyCode());
			optimization.updateEnergyValue();
			DecimalFormat df = new DecimalFormat("##.##");
			String energy = df.format(optimization.energyValue);
			String mse = df.format(optimization.mseValue);
			String ue = df.format(optimization.UEValue);
			String ur = df.format(optimization.URValue);			
			if(optimization.getClass().getName().endsWith("ElasticEnergyOptimizationTrimmed")){
				int np = ((ElasticEnergyOptimizationTrimmed)optimization).NumberOfPointsCaptured;
				System.out.println("Before: "+optimization.graph.toString()+" (E="+energy+",MSE="+mse+",UE="+ue+",UR="+ur+") "+"NPointsCaptured = "+np+", "+optimization.graph.getSimpleTopologyCode());
			}else
				System.out.println("Before: "+optimization.graph.toString()+" (E="+energy+",MSE="+mse+",UE="+ue+",UR="+ur+") "+optimization.graph.getSimpleTopologyCode());
		}
		
		Vector graphs = grammar.applyAllPossibleTransformations(graph,dataset,optimization.taxons);
		//System.out.println("Possible number of transformations = "+graphs.size());
		Vector memgraphs = new Vector();
		for(int i=0;i<graphs.size();i++)
			memgraphs.add(((Graph)graphs.get(i)).clone());
		//System.out.println(""+graphs.size()+" graphs generated:");
		float energies[] = new float[graphs.size()];
		for(int i=0;i<graphs.size();i++){
			optimization.graph = (Graph)graphs.get(i);
			
			optimization.optimize();
			
			//optimization.graph.saveToFile("C:/Datas/ElGraph_Matlab/test_data/tree23_"+(i+1)+".vem", "tree23");
			
			optimization.updateEnergyValue();
			energies[i] = optimization.energyValue;
			float efee = calcElementFee(optimization.graph);
			energies[i]+=efee;
			if(verbose){
				
				
				//System.out.println(i+": "+optimization.graph.toString()+" ("+optimization.energyValue+","+efee+","+(optimization.energyValue+efee)+") "+optimization.graph.getSimpleTopologyCode());
				DecimalFormat df = new DecimalFormat("##.##");
				String energy = df.format(optimization.energyValue);
				String mse = df.format(optimization.mseValue);
				String ue = df.format(optimization.UEValue);
				String ur = df.format(optimization.URValue);			
				
				if(optimization.getClass().getName().endsWith("ElasticEnergyOptimizationTrimmed")){
					int np = ((ElasticEnergyOptimizationTrimmed)optimization).NumberOfPointsCaptured;
					System.out.println((i+1)+": "+optimization.graph.toString()+" (E="+energy+",MSE="+mse+",UE="+ue+",UR="+ur+") "+"NPointsCaptured = "+np+", "+optimization.graph.getSimpleTopologyCode());
				}else
					System.out.println((i+1)+": "+optimization.graph.toString()+" (E="+energy+",MSE="+mse+",UE="+ue+",UR="+ur+") "+optimization.graph.getSimpleTopologyCode());
			}
		}
		int inds[] = Algorithms.SortMass(energies);
		//graph = (Graph)graphs.get(inds[graphs.size()-1]);
		graph = (Graph)graphs.get(inds[0]);
		memGraphBeforeOptimization = (Graph)memgraphs.get(inds[0]);
		optimization.graph = graph;
		optimization.calcTaxons();
		optimization.updateEnergyValue();
		
		float energyNew = optimization.energyValue;
		//System.out.print("i>");
		float efeeNew = calcElementFee(graph);
		
		//if(efeeNew>efeeOld)
		/*if(nodesOld<graph.getNodeNum()) // need better criterium what we want to do from trimming
			if(energyNew+efeeNew>energyOld+efeeOld){
				convergedByComplexity = true;
				if(verbose)
					System.out.println("convergedByComplexity, oldEnergy: ("+energyOld+","+efeeOld+","+(efeeOld+energyOld)+")");
		}*/
		if(verbose)
			//System.out.println(iteration+"-"+graph.getNodeNum()+") Graph selected : "+graph.toString()+" ("+energyNew+","+efeeNew+","+(energyNew+efeeNew)+") "+graph.getSimpleTopologyCode());
			System.out.println(iteration+"-"+graph.getNodeNum()+") Graph selected : "+graph.toString()+" ("+optimization.energyValue+","+optimization.mseValue+","+(optimization.UEValue+optimization.URValue)+") "+graph.getSimpleTopologyCode());
	}
	
	public boolean checkForConvergence(){
		boolean res = true;
		if(parameters.convergenceCriterion==CONVERGENCE_MAXNUMNODES){
			if(graph.getNodeNum()<parameters.maxNumberOfNodes)
				res = false;
		}
		if(parameters.convergenceCriterion==CONVERGENCE_APPROXIMATION_ERROR){
			double mse = graph.calcMSE(dataset, optimization.taxons);
			if(mse<parameters.eps)
				res = false;
		}
		if(convergedByComplexity) res = true;
		return res;
	}
	
	public void setElementFeeLinear(float nodeFee, float complexityFeeAddon){
		elementFees = new float[Graph.MAX_STAR_DEGREE];
		for(int i=0;i<Graph.MAX_STAR_DEGREE;i++)
			elementFees[i] = nodeFee*(1+i*complexityFeeAddon); 
	}
	
	public void setElementFeeExponential(float nodeFee, float complexityFeeBase){
		elementFees = new float[Graph.MAX_STAR_DEGREE];
		for(int i=0;i<Graph.MAX_STAR_DEGREE;i++)
			elementFees[i] = nodeFee*(float)Math.pow(complexityFeeBase, i); 
	}
	
	
}


