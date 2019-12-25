package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

//import sun.security.provider.SystemSigner;
import vdaoengine.data.VDataSet;
import vdaoengine.utils.SLAUSolver;
import vdaoengine.utils.VVectorCalc;

public class ElasticEnergyOptimization {
	
	public Vector<Vector<Integer>> taxons;
	public SLAUSolver solver;	
	
	public static int CONVERGENCE_MAXNUMITERATIONS = 0;
	public static int CONVERGENCE_MINCHANGE = 1;
	public static int CONVERGENCE_MINCHANGE_MAXNUMITERATIONS = 2;
	
	public float feeForBranching = 0;
	
	public class Parameters{
		public int convergenceCriterion = CONVERGENCE_MINCHANGE_MAXNUMITERATIONS;
		public int maxNumberOfIterations = 100;
		/*
		 * relative change - check for convergence
		 */
		public float eps = 1e-3f;
	}
	
	/*
	 * You should set a dataset and a graph to optimise the graph energy
	 */
	public VDataSet dataset = null;
	/*
	 * You should set a dataset and a graph to optimise the graph energy
	 */
	public Graph graph = null;
	
	public Parameters parameters = null;
	
	public int iterationNumber = 0;
	
	
	public float energyValue = Float.POSITIVE_INFINITY;
	public float mseValue = Float.POSITIVE_INFINITY;
	public float UEValue = Float.POSITIVE_INFINITY;
	public float URValue = Float.POSITIVE_INFINITY;
	public float URValueAsSecondDerivative= Float.POSITIVE_INFINITY;

	
	public ElasticEnergyOptimization(VDataSet vd, Graph gr){
		dataset = vd;
		graph = gr;
		parameters = new ElasticEnergyOptimization.Parameters();
	}
	
	public void optimize(){
		iterationNumber = 0;
		calcTaxons();
		do{
			optimizationStep();
			iterationNumber++;
			//System.out.println("Iteration #"+iterationNumber+"/"+parameters.maxNumberOfIterations+", "+graph.Nodes.get(0).x[0]+","+graph.Nodes.get(0).x[1]);
			calcTaxons();
		}while(!checkConvergency());
	}
	
	public void optimizationStep(){
		solver = new SLAUSolver();
		solver.dimension = graph.getNodeNum();
		solver.initMatrix();
		calcMatrix();
		float solution[] = new float[graph.getNodeNum()];
		float rightHandVector[] = new float[graph.getNodeNum()];
		solver.createMatrix();
		solver.createPreconditioner();
		for(int j=0;j<graph.getDimension();j++) {
			for(int k=0;k<graph.getNodeNum();k++)
				solution[k] = graph.getNode(k).x[j];
		calcRightHandVector(j,rightHandVector);
		solver.setSolution(solution);
		solver.setVector(rightHandVector);
		solver.solve((float)0.01,100);
		for(int k=0;k<graph.getNodeNum();k++)
			graph.getNode(k).x[j] = (float)solver.solution[k];
		}
	}
	
	public boolean checkConvergency(){
		boolean res = false;
		if(parameters.convergenceCriterion==CONVERGENCE_MINCHANGE_MAXNUMITERATIONS){
			float mem_energy = energyValue;
			updateEnergyValue();
			if(Math.abs(mem_energy-energyValue)/mem_energy<parameters.eps) res = true;
			if(iterationNumber>=parameters.maxNumberOfIterations) res = true;
		}
		if(parameters.convergenceCriterion==CONVERGENCE_MINCHANGE){
			float mem_energy = energyValue;
			updateEnergyValue();
			if(Math.abs(mem_energy-energyValue)/mem_energy<parameters.eps) res = true;
		}
		if(parameters.convergenceCriterion==CONVERGENCE_MAXNUMITERATIONS){
			if(iterationNumber>=parameters.maxNumberOfIterations) res = true;
		}
		return res;
	}
	
	public void updateEnergyValue(){
		double mse = calcSpecificMSE();
		double elEnergyUE = 0;
		double elEnergyUR = 0;
		double elEnergyURAsSecondDerivative = 0;
		graph.recalcIndexMaps();
		for(int i=0;i<graph.getEdgeNum();i++){
			Edge e = graph.getEdge(i);
			elEnergyUE+=e.getEnergy();
			//System.out.println(e.toString()+"->"+e.getEnergy());
		}
		for(int i=0;i<graph.getStarNum();i++){
			Star st = graph.getStar(i);
			elEnergyUR+=st.getEnergy();
			double averageEdgeLength = 0;
			for(int j=0;j<st.neighbours.size();j++){
				String key1 = st.centralNode.key+"_"+st.neighbours.get(j).key;
				String key2 = st.neighbours.get(j).key+"_"+st.centralNode.key;
				Edge e = null;
				if(graph.getEdge(key1)!=null) e = graph.getEdge(key1);
				if(graph.getEdge(key2)!=null) e = graph.getEdge(key2);
				if(e==null)
					System.out.println("NO SUCH EDGE FOUND "+key1);
				averageEdgeLength+=e.getLength();
			}
			averageEdgeLength/=st.neighbours.size();
			elEnergyURAsSecondDerivative+=st.getEnergy()/(averageEdgeLength*averageEdgeLength*averageEdgeLength*averageEdgeLength);
			//System.out.println(st.toString()+"->"+st.getEnergy());
		}
		energyValue = (float)(mse+elEnergyUE+elEnergyUR);
		mseValue = (float)mse;
		UEValue = (float)elEnergyUE;
		URValue = (float)elEnergyUR;
		URValueAsSecondDerivative = (float)elEnergyURAsSecondDerivative;
	}
	
	public double calcSpecificMSE(){
		return graph.calcMSE(dataset, taxons);
	}
	
	 public int calcTaxons(){
		    Date cld = new Date();
		    graph.compileNodesInArrays();		    
		          taxons = new Vector<Vector<Integer>>();

		          for(int j=0;j<graph.getNodeNum();j++){
		                  Vector<Integer> v = new Vector<Integer>();
		                  taxons.add(v);
		          }
		          float r = 0f;

		          int dimen = dataset.coordCount;

		          if(!dataset.hasGaps)
		          for(int i=0;i<dataset.pointCount;i++){
		                  float v[] = dataset.getVector(i);
		                  float minr = (float)Float.MAX_VALUE ;
		                  int minj = -1;
		                  for(int j=0;j<graph.getNodeNum();j++){
		                          r = VVectorCalc.SquareEuclDistanceShift(graph.NodeArrayCopy,j*dimen,v,dataset.coordCount);
		                	  	  //r = (float)VVectorCalc.Distance(graph.NodeArray[j], v);
		                          if(r<minr){ minr=r; minj = j; }
		                  }
		                  if(minj!=-1){
		                          ((Vector<Integer>)taxons.elementAt(minj)).add(new Integer(i));
		                  }
		          }
		          else
		            for(int i=0;i<dataset.pointCount;i++){
		                    float v[] = dataset.getVector(i);
		                    float minr = (float)Float.MAX_VALUE ;
		                    int minj = -1;
		                    for(int j=0;j<graph.getNodeNum();j++){
		                            r = VVectorCalc.SquareEuclDistanceShiftGap(graph.NodeArrayCopy,j*dimen,v,dataset.coordCount);
		                            if(r<minr){ minr=r; minj = j; }
		                    }
		                    if(minj!=-1){
		                            ((Vector<Integer>)taxons.elementAt(minj)).add(new Integer(i));
		                    }
		            }
		          /*Vector<Integer> res = new Vector<Integer>();
		          for(int i=0;i<graph.getNodeNum();i++){
		            Vector<Integer> v = (Vector<Integer>)taxons.elementAt(i);
		            int tx[] = new int[v.size()];
		            for(int j=0;j<v.size();j++){
		              tx[j] = ((Integer)v.elementAt(j)).intValue();
		              //System.out.print((tx[j]+1)+" ");
		            }
		            res.add(tx);
		            //System.out.println();
		          }
		          
		          taxons = res;*/
		    return (int)((new Date()).getTime()-cld.getTime());
	 }
	 
	public void calcMatrix(){
		
		// diagonal elements
		calcDiagonalMatrixElements();
		
	    // edge contributions
	    for(int i=0;i<graph.getEdgeNum();i++){
	    	Edge e = graph.getEdge(i);
	    	solver.addToMatrixElement(graph.getNodeIndex(e.getSource().key),graph.getNodeIndex(e.getSource().key),e.elasticity);
	    	solver.addToMatrixElement(graph.getNodeIndex(e.getTarget().key),graph.getNodeIndex(e.getTarget().key),e.elasticity);
	    	solver.addToMatrixElement(graph.getNodeIndex(e.getSource().key),graph.getNodeIndex(e.getTarget().key),-e.elasticity);
	    	solver.addToMatrixElement(graph.getNodeIndex(e.getTarget().key),graph.getNodeIndex(e.getSource().key),-e.elasticity);
	    }
	    // star contributions
	    for(int i=0;i<graph.getStarNum();i++){
	    	Star s = graph.getStar(i);
	    	int k = s.neighbours.size();
	    	if(k==1)
	    		System.out.println("FOUND edge instead of star");
	    	//if(k==3)
	    	//	System.out.println(s.elasticity);
	    	/*if(test.variant==0){
	    		solver.addToMatrixElement(graph.getNodeIndex(s.centralNode.key),graph.getNodeIndex(s.centralNode.key),k*k*s.elasticity);
	    		for(int j=0;j<s.neighbours.size();j++){
	    			solver.addToMatrixElement(graph.getNodeIndex(s.centralNode.key),graph.getNodeIndex(((Node)s.neighbours.get(j)).key),-k*s.elasticity);
	    			solver.addToMatrixElement(graph.getNodeIndex(((Node)s.neighbours.get(j)).key),graph.getNodeIndex(s.centralNode.key),-k*s.elasticity);
	    		}
	    		for(int l=0;l<s.neighbours.size();l++)for(int m=0;m<s.neighbours.size();m++){
	    			solver.addToMatrixElement(graph.getNodeIndex(((Node)s.neighbours.get(l)).key),graph.getNodeIndex(((Node)s.neighbours.get(m)).key),s.elasticity);
	    	}}
	    	if(test.variant==1){*/
	    	float kfee = (float)Math.pow((float)k, feeForBranching);
	    	if(true){
	    	solver.addToMatrixElement(graph.getNodeIndex(s.centralNode.key),graph.getNodeIndex(s.centralNode.key),kfee*s.elasticity);
	    	for(int j=0;j<s.neighbours.size();j++){
	    		solver.addToMatrixElement(graph.getNodeIndex(s.centralNode.key),graph.getNodeIndex(((Node)s.neighbours.get(j)).key),-1f/k*kfee*s.elasticity);
	    		solver.addToMatrixElement(graph.getNodeIndex(((Node)s.neighbours.get(j)).key),graph.getNodeIndex(s.centralNode.key),-1f/k*kfee*s.elasticity);
	    	}
	    	for(int l=0;l<s.neighbours.size();l++)for(int m=0;m<s.neighbours.size();m++){
	    		solver.addToMatrixElement(graph.getNodeIndex(((Node)s.neighbours.get(l)).key),graph.getNodeIndex(((Node)s.neighbours.get(m)).key),kfee*s.elasticity/(k*k));
	    	}}
	    }
	}
	
	public void calcDiagonalMatrixElements(){
	    for(int i=0;i<graph.getNodeNum();i++){
            double d = 0;
            Vector<Integer> tx = taxons.elementAt(i);
            if(dataset.weighted){
                    for(int k=0;k<tx.size();k++)
                            d+=dataset.weights[tx.get(k)];
                    d/=dataset.weightSum;
            }
            else {
                    d = (double)tx.size()/(dataset.pointCount);
	        }
        //if(Math.abs(d)<1e-8) d=1e-8;
	    solver.addToMatrixElement(i,i,d);
        }
	}
	
	public void calcRightHandVector(int coord, float vec[]){
	    for(int i=0;i<graph.getNodeNum();i++){
	        vec[i] = 0;
	        Vector<Integer> tx = taxons.elementAt(i);
	        if(!dataset.hasGaps)
	        for(int j=0;j<tx.size();j++)
	                 if(!dataset.weighted)
	                              vec[i] += dataset.massif[tx.get(j)][coord];
	                 else
	                              vec[i] += dataset.massif[tx.get(j)][coord]*dataset.weights[tx.get(j)];
	        else
	          for(int j=0;j<tx.size();j++) if(!Float.isNaN(dataset.massif[tx.get(j)][coord]))
	                   if(!dataset.weighted)
	                                vec[i] += dataset.massif[tx.get(j)][coord];
	                   else
	                                vec[i] += dataset.massif[tx.get(j)][coord]*dataset.weights[tx.get(j)];
	        }
	        for(int i=0;i<graph.getNodeNum();i++)
	                      if(!dataset.weighted)
	                    	  vec[i] = vec[i]/(dataset.pointCount);
	                      else
	                          vec[i] = vec[i]/dataset.weightSum;
	}

}
