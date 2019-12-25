package vdaoengine.analysis.grammars;

import java.util.*;

import vdaoengine.utils.*;
import vdaoengine.data.*;

public class AddNodeToNode extends GrammarOperation {
	
	public boolean applyToElement(Graph graph, VDataSet data, Vector<Vector<Integer>> taxons, GraphElement el) {
		boolean applicable = false;
		if(el instanceof Node){
			Node n = (Node)el;
			//System.out.println("Applying NodetoNode to "+n.key);
			if(graph.starCenters.get(n.key)!=null){
				Vector v = (Vector)graph.starCenters.get(n.key);
				if(v.size()>0){
					
					Graph localGraph = new Graph();
					
					Star s = (Star)v.get(0);
					Vector lineNumbers = new Vector();
					int k = graph.getNodeIndex(s.centralNode.key);
					Vector<Integer> tx = taxons.get(k);
					for(int i=0;i<tx.size();i++)
						lineNumbers.add(new Integer(tx.get(i)));
					localGraph.addNode(s.centralNode);
					for(int j=0;j<s.neighbours.size();j++){
						Node node = (Node)s.neighbours.get(j);
						localGraph.addNode(node);
						k = graph.getNodeIndex(s.centralNode.key);
						tx = taxons.get(k);
						for(int i=0;i<tx.size();i++)
							lineNumbers.add(new Integer(tx.get(i)));
					}
					
					VDataSet localData = VSimpleProcedures.selectRows(data, lineNumbers);
					ElasticEnergyOptimization elo = new ElasticEnergyOptimization(localData, localGraph);
					
					Node newNode = new Node();
					float minMSE = Float.MAX_VALUE;
					int mink = -1;
					localGraph.addNode(newNode);
					
					for(int i=0;i<localData.pointCount;i++){
						Node nn = localGraph.getNode(newNode.key);
						nn.x = localData.massif[i];
						elo.calcTaxons();
						float mse = (float)localGraph.calcMSE(localData, elo.taxons);
						if(mse<minMSE){
							minMSE = mse; mink = i;
						}
					}
					
					if(mink>=0){
					Node newLeaf = new Node();
					newLeaf.x = new float[graph.getDimension()];
					for(int i=0;i<graph.getDimension();i++)
						newLeaf.x[i] = localData.massif[mink][i];
					graph.addNode(newLeaf);
					Edge ee = graph.addEdge(s.centralNode,newLeaf);
					s.neighbours.add(newLeaf);
					s.elasticity = graph.StarDefaultElasticity[s.neighbours.size()];
					
					applicable = true;
					}
				}
			}else{ // Node is a leaf, not a center of any star
				Node newLeaf = new Node();
				newLeaf.x = new float[graph.getDimension()];
				graph.addNode(newLeaf);
				Edge ee = graph.addEdge(n,newLeaf);
				Star newStar = new Star();
				Vector<Edge> es = graph.outgoingEdges.get(n.key);
				Edge eprec = es.get(0);
				Node nprec = null;
					if(eprec.getSource().key.equals(n.key))
						nprec = eprec.getTarget();
					else
						nprec = eprec.getSource();
				newStar.centralNode = n;
				newStar.neighbours.add(newLeaf);
				newStar.neighbours.add(nprec);
				newStar.elasticity = graph.StarDefaultElasticity[newStar.neighbours.size()];
				graph.Stars.add(newStar);
				float newPos[] = VVectorCalc.Add_(n.x, VVectorCalc.Subtract_(n.x, nprec.x));
				newLeaf.x = newPos;
				applicable = true;
			}
		}
		return applicable;
	}
	

	public boolean applyToElementOld(Graph graph, VDataSet data, GraphElement el) {
		boolean applicable = false;
		if(el instanceof Node){
			Node n = (Node)el;
			if(graph.starCenters.get(n.key)!=null){
				Vector v = (Vector)graph.starCenters.get(n.key);
				if(v.size()>0){
					Star s = (Star)v.get(0);
					
					float averageLeafLength = 0;
					// We will attach the node in the direction of one of the coordinate axes,
					// which is the most 'orthogonal' to the leaves directions
					for(int i=0;i<s.neighbours.size();i++){
						Node leaf = (Node)s.neighbours.get(i);
						averageLeafLength+=VVectorCalc.Distance(s.centralNode.x, leaf.x);
					}
					averageLeafLength/=s.neighbours.size();

					float minscproduct = Float.MAX_VALUE; int axisnumber = -1;
					
					Vector axes = new Vector();
					for(int j=0;j<graph.getDimension();j++){
						float axe[] = new float[graph.getDimension()];
						axe[j] = 1f;
						axes.add(axe);
					}
					if(graph.getDimension()==2){
						for(int i=0;i<graph.getDimension();i++)
							for(int j=i+1;j<graph.getDimension();j++){
								float axe[] = new float[graph.getDimension()];
								axe[i] = 1f;
								axe[j] = 1f;								
								axes.add(axe);
								axe = new float[graph.getDimension()];
								axe[i] = -1f;
								axe[j] = 1f;								
								axes.add(axe);
								axe = new float[graph.getDimension()];
								axe[i] = 1f;
								axe[j] = -1f;								
								axes.add(axe);
								axe = new float[graph.getDimension()];
								axe[i] = -1f;
								axe[j] = -1f;								
								axes.add(axe);
							}
					}
					if(graph.getDimension()==3){
						for(int i=0;i<graph.getDimension();i++)
							for(int j=i+1;j<graph.getDimension();j++)
								for(int k=j+1;k<graph.getDimension();k++){
									float axe[] = new float[graph.getDimension()];
									axe[i] = 1f;
									axe[j] = 1f;								
									axe[k] = 1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = -1f;
									axe[j] = 1f;
									axe[k] = 1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = 1f;
									axe[j] = -1f;
									axe[k] = 1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = -1f;
									axe[j] = -1f;
									axe[k] = 1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = 1f;
									axe[j] = 1f;								
									axe[k] = -1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = -1f;
									axe[j] = 1f;
									axe[k] = -1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = 1f;
									axe[j] = -1f;
									axe[k] = -1f;
									axes.add(axe);
									axe = new float[graph.getDimension()];
									axe[i] = -1f;
									axe[j] = -1f;
									axe[k] = -1f;
									axes.add(axe);
							}
					}
					
					
					for(int j=0;j<axes.size();j++){
						float maxscproduct = -Float.MAX_VALUE;
						for(int i=0;i<s.neighbours.size();i++){
							Node leaf = (Node)s.neighbours.get(i);
							float leafLength = (float)VVectorCalc.Distance(s.centralNode.x, leaf.x);
							float leafv[] = VVectorCalc.Subtract_(leaf.x, s.centralNode.x);
							float axe[] = (float[])axes.get(j);
							//float x = Math.abs(leafv[j]/leafLength);
							//float x = VVectorCalc.ScalarMult(leafv, (float[])axes.get(j));
							float x = Math.abs(VVectorCalc.ScalarMult(leafv, axe));
							x/=leafLength;
							if(x>maxscproduct) maxscproduct = x;
						}
						if(maxscproduct<minscproduct){
							minscproduct = maxscproduct; 
							axisnumber = j;
						}
					}
					
					// We put a new leaf such as to cancel the star's energy
					
					Node newLeaf = new Node();
					/*float averageLeafLength = 0;
					newLeaf.x = new float[graph.getDimension()];
					for(int i=0;i<s.neighbours.size();i++){
						Node leaf = (Node)s.neighbours.get(i);
						float x[] = VVectorCalc.Subtract_(leaf.x, s.centralNode.x);
						VVectorCalc.Add(newLeaf.x,x);
						averageLeafLength+=VVectorCalc.Distance(leaf.x, s.centralNode.x);
					}
					averageLeafLength/=1f*s.neighbours.size();
					VVectorCalc.Mult(newLeaf.x, -1f/(float)VVectorCalc.Norm(newLeaf.x)*averageLeafLength);
					//VVectorCalc.Mult(newLeaf.x, -1f/(float)VVectorCalc.Norm(newLeaf.x));
					VVectorCalc.Add(newLeaf.x, s.centralNode.x);*/
					
					//newLeaf.x = new float[graph.getDimension()];
					newLeaf.x = (float[])axes.get(axisnumber);
					//newLeaf.x[axisnumber] = 1;
					newLeaf.x = VVectorCalc.Add_(s.centralNode.x, VVectorCalc.Product_(newLeaf.x, averageLeafLength));
					
					graph.addNode(newLeaf);
					
					Edge ee = graph.addEdge(s.centralNode,newLeaf);
					//System.out.println("Elasticity = "+ee.elasticity);
					
					s.neighbours.add(newLeaf);
					s.elasticity = graph.StarDefaultElasticity[s.neighbours.size()];
					
					applicable = true;
				}
			}
		}
		return applicable;
	}

}
