package vdaoengine.analysis.grammars;

import java.util.Date;
import java.util.Vector;

import vdaoengine.data.VDataSet;
import vdaoengine.data.VStatistics;
import vdaoengine.utils.VVectorCalc;

/*
 * This is a version of elastic energy optimization procedure with trimmed MSE term:
 * all points more distant than TrimmingRadius from the node in a taxon are NOT counted when
 * computing the right hand vector of the equations (and contribute only TrimmingRadius^2 to the
 * definition of energy).
 * 
 * Therefore, when computing taxons all points in the taxon are marked as 'close' and 'far'.
 * This information is stored in additional auxillary array taxons_close.
 * So, when the system of equations is computed taxons_close is used,
 * and when one computes elastic energy, usual taxons are used.
 */

public class ElasticEnergyOptimizationTrimmed extends ElasticEnergyOptimization {

	public float TrimmingRadiusAsFractionOfStd = Float.MAX_VALUE;
	public float TrimmingRadius = Float.MAX_VALUE;
	public int NumberOfPointsCaptured = 0;
	
	public Vector<Vector<Integer>> taxons_close = null;
	
	public ElasticEnergyOptimizationTrimmed(VDataSet vd, Graph gr) {
		super(vd, gr);
	}
	
	public void optimize(){
		iterationNumber = 0;
		calcTaxons();
		//System.out.println("BEFORE OPT:");
		//for(int i=0;i<taxons_close.size();i++)
		//	System.out.println("Taxon "+i+" :"+getTaxonContent(i));
		
		do{
			optimizationStep();
			iterationNumber++;
			calcTaxons();
			/*if(graph.Nodes.size()==2)
				System.out.println("Iteration #"+iterationNumber+"/"+parameters.maxNumberOfIterations+", Node0:"+graph.Nodes.get(0).x[0]+","+graph.Nodes.get(0).x[1]+";"+taxons_close.get(0).size()+", Node1:"+graph.Nodes.get(1).x[0]+","+graph.Nodes.get(1).x[1]+";"+taxons_close.get(1).size());
			else{
				System.out.println("Iteration #"+iterationNumber+"/"+parameters.maxNumberOfIterations+", Node0:"+graph.Nodes.get(0).x[0]+","+graph.Nodes.get(0).x[1]+";"+taxons_close.get(0).size()+", Node1:"+graph.Nodes.get(1).x[0]+","+graph.Nodes.get(1).x[1]+";"+taxons_close.get(1).size()+", Node2:"+graph.Nodes.get(2).x[0]+","+graph.Nodes.get(2).x[1]+";"+taxons_close.get(2).size());
				System.out.println("Node 0: "+getTaxonContent(0)+"; Node 1: "+getTaxonContent(1)+"; Node 2: "+getTaxonContent(2));
			}*/
			
		}while(!checkConvergency());
	}
	
	public String getTaxonContent(int k){
		/*String txd = "{";
		for(int i=0;i<taxons_close.size();i++)
			txd+=taxons_close.get(i)+",";
		txd+="}";
		return txd;*/
		return ""+taxons_close.get(k);
	}
	
	
	public void setTrimmingRadiusAsFractionOfStd(float fraction){
		TrimmingRadiusAsFractionOfStd = fraction;
		TrimmingRadius = dataset.simpleStatistics.totalDispersion*TrimmingRadiusAsFractionOfStd;
	}
	
	 public int calcTaxons(){
		    Date cld = new Date();
		    graph.compileNodesInArrays();		    
		          taxons = new Vector<Vector<Integer>>();
		          taxons_close = new Vector<Vector<Integer>>();

		          for(int j=0;j<graph.getNodeNum();j++){
		                  Vector<Integer> v = new Vector<Integer>();
		                  taxons.add(v);
		                  Vector<Integer> v_close = new Vector<Integer>();
		                  taxons_close.add(v_close);
		          }
		          float r = 0f;

		          int dimen = dataset.coordCount;
		          
		          float rtrim2 = TrimmingRadius*TrimmingRadius;
		          
		          VStatistics stat = new VStatistics(1);
		          float f[] = new float[1];
	
		          if(!dataset.hasGaps)
		          for(int i=0;i<dataset.pointCount;i++){
		                  float v[] = dataset.getVector(i);
		                  float minr = (float)Float.MAX_VALUE ;
		                  int minj = -1;
		                  if(i==283)
		                	  minj = minj;
		                  for(int j=0;j<graph.getNodeNum();j++){
		                          r = VVectorCalc.SquareEuclDistanceShift(graph.NodeArrayCopy,j*dimen,v,dataset.coordCount);
		                	  	  //r = VVectorCalc.SquareDistance(graph.Nodes.get(j).x, v);
		                          f[0] = r;
		                          stat.addNewPoint(f);
		                	  	  //r = (float)VVectorCalc.Distance(graph.NodeArray[j], v);
		                          if(r<minr){ minr=r; minj = j; }
		                  }
		                  if(minj!=-1){
		                          ((Vector)taxons.elementAt(minj)).add(new Integer(i));
		                          if(minr<=rtrim2)
		                        	  ((Vector)taxons_close.elementAt(minj)).add(new Integer(i));
		                  }
		          }
		          else
		            for(int i=0;i<dataset.pointCount;i++){
		                    float v[] = dataset.getVector(i);
		                    float minr = (float)Float.MAX_VALUE ;
		                    int minj = -1;
		                    for(int j=0;j<graph.getNodeNum();j++){
		                            r = VVectorCalc.SquareEuclDistanceShiftGap(graph.NodeArrayCopy,j*dimen,v,dataset.coordCount);
			                        f[0] = r;
			                        stat.addNewPoint(f);
		                            if(r<minr){ minr=r; minj = j; }
		                            
		                    }
		                    if(minj!=-1){
		                            ((Vector)taxons.elementAt(minj)).add(new Integer(i));
			                          if(r<=rtrim2)
			                        	  ((Vector)taxons_close.elementAt(minj)).add(new Integer(i));
		                    }
		            }
		          NumberOfPointsCaptured = 0;
		          for(int i=0;i<taxons_close.size();i++)
		        	  NumberOfPointsCaptured+=taxons_close.get(i).size();
		          //System.out.println("Found close points "+closeCount);
		          //System.out.println("Average MSE "+Math.sqrt(stat.getMean(0)));
		          if(NumberOfPointsCaptured==0){
		        	  stat.calculate();
		        	  System.out.println("ERROR: epsilon ("+TrimmingRadius+") is too small compared to the average distance to a graph node ("+Math.sqrt(stat.getMean(0))+") for estimating robust MSE. Try to increase it.");
		        	  System.exit(0);
		          }
		    return (int)((new Date()).getTime()-cld.getTime());
	 }
	 
		public void calcDiagonalMatrixElements(){
		    for(int i=0;i<graph.getNodeNum();i++){
	            double d = 0;
	            Vector<Integer> tx_close = taxons_close.elementAt(i);
	            if(dataset.weighted){
	                    for(int k=0;k<tx_close.size();k++)
	                            d+=dataset.weights[tx_close.get(k)];
	                    d/=dataset.weightSum;
	            }
	            else {
	                    d = (double)tx_close.size()/(dataset.pointCount);
		        }
	        //if(Math.abs(d)<1e-8) d=1e-8;
		    solver.addToMatrixElement(i,i,d);
	        }
		}
		
		public double calcSpecificMSE(){
			return graph.calcMSETrimmed(dataset, taxons, TrimmingRadius);
		}

	
	
	public void calcRightHandVector(int coord, float vec[]){
	    for(int i=0;i<graph.getNodeNum();i++){
	        vec[i] = 0;
	        Vector<Integer> tx_close = taxons_close.elementAt(i);
	        if(!dataset.hasGaps){
	        for(int j=0;j<tx_close.size();j++)
	                 if(!dataset.weighted)
	                              vec[i] += dataset.massif[tx_close.get(j)][coord];
	                 else
	                              vec[i] += dataset.massif[tx_close.get(j)][coord]*dataset.weights[tx_close.get(j)];
	        }else{
	          for(int j=0;j<tx_close.size();j++) if(!Float.isNaN(dataset.massif[tx_close.get(j)][coord]))
	                   if(!dataset.weighted)
	                                vec[i] += dataset.massif[tx_close.get(j)][coord];
	                   else
	                                vec[i] += dataset.massif[tx_close.get(j)][coord]*dataset.weights[tx_close.get(j)];
	        }}
	        for(int i=0;i<graph.getNodeNum();i++){
	                      if(!dataset.weighted)
	                    	  vec[i] = vec[i]/(dataset.pointCount);
	                      else
	                          vec[i] = vec[i]/dataset.weightSum;
	        }
	}


}
