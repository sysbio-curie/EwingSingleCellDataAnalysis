package vdaoengine.analysis.elmap;

import java.util.*;
import java.io.*;
import vdaoengine.data.VDataSet;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.visproc.*;

public class ElasticTreeAlgorithm extends ElmapAlgorithm {

    public ElasticTree grid;

	public int k[];		//should really be a vector
	public int cent;
	public int outer;
	public int riblen;
	public int candrib;
	public int candindex1;
	public int candindex2;
	public int nribEdgeToBisect;
	public int foundrib;
	public float len;
	public float energy;
	public float minenergy;
        public float nodedata_energy = 0f;
        public float rib_energy = 0f;
        public float edge_energy = 0f;

	public float[] candNode;
	public float[] minCandNode;
	public float[] ribSum;
	public double lambda;
	//public double ep;
	//public double rp;
	public boolean addNode;
	public boolean addRib;
	public boolean bisectEdge;
	public boolean noNode;
	public boolean bisectingOverlap;
	public boolean bisectIsOptimal;

        public static String project;



//****************************************************************************************************************
public static Grid computeElasticTree(VDataSet ds,String conf,int algnum, int maxit, double ep, double rp, float le) {
	ElasticTreeAlgorithm eltree = new ElasticTreeAlgorithm();
		eltree.setData(ds);
		ElasticTree gr = (ElasticTree)eltree.readIniFile(conf,algnum);
                eltree.setGrid(gr);
		eltree.computeElasticTree(maxit, ep, rp, ds);
		return eltree.grid;
	}

public void setGrid(ElasticTree gr){
         grid = gr;
         dimension = gr.dimension;
}

//***************************************************************************************************************
public void computeElasticTree(int maxit, double ep, double rp, VDataSet ds){
		//lambda = lam;
		globalEPFactor = (float)ep;
		globalRPFactor = (float)rp;
    		initializeTree(ds);
		System.out.println("number of data points = "+ data.pointCount);
		dotree();
		for(int i=0;i<grid.nodesNum;i++) {
			grid.TreeNodes.setElementAt((float[])grid.TreeNodesCopy.elementAt(i), i);
		}
		minCandNode = new float[grid.dimension];
		candNode = new float[grid.dimension];

                allocateAndMakeCopy();
                String siter = "00";
                //grid.writeOutTreeNodes("temp/map"+siter);
                //grid.writeOutNRibs("temp/nrib"+siter);
                //grid.writeOutEdges("temp/nrib"+siter);
                grid.type = 10;
                //grid.saveToFile("temp/vem"+siter+".vem",project);


/*START OF ITERATION */
		for(int iter=0;iter<maxit;iter++) {
			addNode = false;
			addRib = false;
			bisectEdge = false;
			noNode = true;
			bisectIsOptimal = false;
			bisectingOverlap = false;
			minenergy = 1.0f/0.0f;
			for(int i=0;i<grid.nribsNum;i++) {
				/*GET THE AVERAGE INCIDENT EDGE LENGTH*/
				cent = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(0);
				riblen = ((IntegerVector)grid.NRibs.elementAt(i)).size();
				len = 0f;
				for(int j=1;j<riblen;j++) {
					outer = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
					len += VVectorCalc.Norm(VVectorCalc.Subtract_((float[])grid.TreeNodes.elementAt(outer),(float[])grid.TreeNodes.elementAt(cent)));
				}
				//len = (len*globalRPFactor)/(riblen -1);
                                len = (len)/(riblen -1);
				/*TEST ADDING NODES TO RIBS*/
				for(int j=1;j<riblen;j++) {
					outer = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
					for(int j1=j+1;j1<riblen;j1++) {
						if(j1==outer) {
							j1++;
						}
						else {
							int outer2 = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j1);
							getNewInnerNodePosition(outer, outer2, cent, len);
							addNodeToRib((grid.nodesNum), i, candNode);
							dotree();
							if(energy < minenergy) {
								minenergy = energy;
								for(int i1=0;i1<grid.dimension;i1++) {
									minCandNode[i1] = candNode[i1];
								}
								candrib = i;
								addNode = true;
								noNode = false;
								addRib = false;
								bisectEdge = false;
								bisectIsOptimal = false;
							}
							deleteNodeFromRib(i);
						}
					}
				}
				/*TEST BISECTING EDGES*/
				for(int j=1;j<riblen;j++) {
					outer = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
					getCentreOfEdge(outer, cent);
					createNewRib((grid.nodesNum), cent, outer, candNode);
					((IntegerVector)grid.NRibs.elementAt(i)).setInt((grid.nodesNum-1), j);
					bisectingOverlap = false;
					for(int k=0;k<(grid.nribsNum-1);k++) {
							if(((IntegerVector)grid.NRibs.elementAt(k)).getInt(0)==outer) {
								bisectingOverlap = true;
								for(int k1=1;k1<((IntegerVector)grid.NRibs.elementAt(k)).size();k1++) {
									if(((IntegerVector)grid.NRibs.elementAt(k)).getInt(k1)==cent) {
										((IntegerVector)grid.NRibs.elementAt(k)).setInt((grid.nodesNum-1), k1);
									}
								}
							}
					}
					dotree();
					if(energy < minenergy) {
								minenergy = energy;
								for(int i1=0;i1<grid.dimension;i1++) {
									minCandNode[i1] = candNode[i1];
								}
								candrib = i;
								candindex1 = outer;
								candindex2 = cent;
								nribEdgeToBisect = j;
								if(bisectingOverlap) {
									bisectIsOptimal = true;
								}
								else {
									bisectIsOptimal = false;
								}
								addNode = false;
								bisectEdge = true;
								addRib = false;
								noNode = false;
							}
					deleteRib();
					((IntegerVector)grid.NRibs.elementAt(i)).setInt(outer, j);
					if(bisectingOverlap) {
						for(int k=0;k<grid.nribsNum;k++) {
							if(((IntegerVector)grid.NRibs.elementAt(k)).getInt(0)==outer) {
								for(int k1=1;k1<((IntegerVector)grid.NRibs.elementAt(k)).size();k1++) {
									if(((IntegerVector)grid.NRibs.elementAt(k)).getInt(k1)==grid.nodesNum) {
										((IntegerVector)grid.NRibs.elementAt(k)).setInt(cent, k1);
									}
								}
							}
						}
					}
				}
				/*TEST ADDING NEW NRIBS*/
				/*for(int j=1;j<riblen;j++) {
					outer = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
					getNewOuterNodePosition(outer, cent, len);
					createNewRib(outer, cent, (grid.nodesNum), candNode);
					dotree();
					if(energy < minenergy) {
						minenergy = energy;
						for(int i1=0;i1<grid.dimension;i1++) {
							minCandNode[i1] = candNode[i1];
						}
						candindex1 = outer;
						candindex2 = cent;
						addRib = true;
						addNode = false;
						bisectEdge = false;
						noNode = false;
						bisectIsOptimal = false;
					}
					deleteRib();
				}*/
			}
			if(noNode) {
				System.out.println("could not find an edge");
			}
			if(addRib&!noNode) {
                                System.out.println("Create new rib "+candindex1+" "+candindex2);
				createNewRib(candindex1, candindex2, (grid.nodesNum), minCandNode);
			}
			if(addNode&!noNode) {
                                System.out.println("Add node to rib "+candrib);
				addNodeToRib(grid.nodesNum, candrib, minCandNode);
			}
			if(bisectEdge&!noNode) {
                                System.out.println("Bisect edge "+candindex2+" of rib "+candindex1);
				createNewRib((grid.nodesNum), candindex2, candindex1, minCandNode);
				((IntegerVector)grid.NRibs.elementAt(candrib)).setInt((grid.nodesNum-1), nribEdgeToBisect);
				if(bisectIsOptimal) {
						for(int k=0;k<(grid.nribsNum-1);k++) {
							if(((IntegerVector)grid.NRibs.elementAt(k)).getInt(0)==candindex1) {
								for(int k1=1;k1<((IntegerVector)grid.NRibs.elementAt(k)).size();k1++) {
									if(((IntegerVector)grid.NRibs.elementAt(k)).getInt(k1)==candindex2) {
										((IntegerVector)grid.NRibs.elementAt(k)).setInt((grid.nodesNum-1), k1);
									}
								}
							}
						}
					}
			}
			dotree();
			for(int i=0;i<grid.nodesNum;i++) {
			grid.TreeNodes.setElementAt((float[])grid.TreeNodesCopy.elementAt(i), i);
			}
			System.out.println("ITERATION = " + (iter+1) + " ***************************** Minimum energy this iteration " + minenergy + " NDE:"+nodedata_energy+" RE:"+rib_energy+" EE:"+edge_energy);

                        siter = ""+(iter+1);
                        if(siter.length()==1) siter="0"+siter;
                        allocateAndMakeCopy();
                        //grid.writeOutTreeNodes("temp/map"+siter);
                        //grid.writeOutNRibs("temp/nrib"+siter);
                        //grid.writeOutEdges("temp/nrib"+siter);
                        //grid.saveToFile("temp/vem"+siter+".vem",project);
		}

                globalEPFactor/=10;
                globalRPFactor/=10;
                dotree();
                for(int i=0;i<grid.nodesNum;i++) {
                grid.TreeNodes.setElementAt((float[])grid.TreeNodesCopy.elementAt(i), i);
                }
                allocateAndMakeCopy();
                //grid.writeOutTreeNodes("temp/map99");
                //grid.writeOutNRibs("temp/nrib99");
                //grid.writeOutEdges("temp/nrib99");
                //grid.saveToFile("temp/vem99.vem",project);

		/* END OF ITERATION */
/**************************************************************************************************************************/
/*		ALLOCATE AND COPY THE RESULT TO THE ORIGINAL ARRAYS		*/
}
/****************************************************************************************************************/
public void allocateAndMakeCopy(){
grid.nodesNum = grid.TreeNodes.size();
grid.edgesNum = 0;
for(int i=0;i<grid.NRibs.size();i++) {
        grid.edgesNum = grid.edgesNum + (((IntegerVector)grid.NRibs.elementAt(i)).size());
}
grid.allocate();
for(int i=0;i<grid.nodesNum;i++) {
        for(int j=0;j<grid.dimension;j++) {
        grid.Nodes[i][j] = ((float[])grid.TreeNodes.elementAt(i))[j];
        }
}
int temp1 = 0;
for(int i=0;i<grid.NRibs.size();i++) {
        for(int j=1;j<((IntegerVector)grid.NRibs.elementAt(i)).size();j++) {
                grid.Edges[temp1][0] = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(0);
                grid.Edges[temp1][1] = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
                temp1++;
        }
}
grid.MakeTreeNodesCopy();
}


public void initializeTree(VDataSet data){
    VStatistics st = ((VDataSet)data).calcStatistics();
		grid.TreeNodes.setElementAt(st.means , 0);
		grid.TreeNodes.setElementAt(VVectorCalc.Add_(st.means,VVectorCalc.Product_(st.stdevs, -0.5f)),1);
		grid.TreeNodes.setElementAt(VVectorCalc.Add_(st.means,VVectorCalc.Product_(st.stdevs, 0.5f)), 2);
		//grid.TreeNodes.setElementAt((float[])data.getVectorUnProcessed(1) , 0);
		//grid.TreeNodes.setElementAt((float[])data.getVectorUnProcessed(0),1);
		//grid.TreeNodes.setElementAt((float[])data.getVectorUnProcessed(2), 2);
		grid.NRibs.add(new IntegerVector());
		((IntegerVector)grid.NRibs.elementAt(0)).appendInt(0);
		((IntegerVector)grid.NRibs.elementAt(0)).appendInt(1);
		((IntegerVector)grid.NRibs.elementAt(0)).appendInt(2);
 }

public void dotree(){
//SET UP THE SYSTEM MATRIX AND SOLVE TO OPTIMISE
		double rightHandVector[] = new double[grid.nodesNum];
		double solution[] = new double[grid.nodesNum];
		solver = new SLAUSolver();
		solver.dimension = grid.nodesNum;
		solver.initMatrix();
		calcTreeTaxons();
		boolean nodeWithNoData = false;
		/*for(int i=0;i<grid.nodesNum;i++) {
			int tx[] = (int[])taxons.elementAt(i);
			if(tx.length==0) {
				nodeWithNoData = true;
			}
		}*/
		if(!nodeWithNoData) {
			calcTreeMatrix();
			for(int j=0;j<grid.dimension;j++) {
				for(int k=0;k<grid.nodesNum;k++) {
					solution[k] = ((float[])grid.TreeNodesCopy.elementAt(k))[j];
				}
			calcRightHandVector(j,rightHandVector);
			solver.setSolution(solution);
			solver.setVector(rightHandVector);
			solver.solve((float)0.01,100);
                                for(int k=0;k<grid.nodesNum;k++) {
					float tempvec[] = (float[])grid.TreeNodesCopy.elementAt(k);
					tempvec[j] = (float)(solver.solution[k]);
					grid.TreeNodesCopy.setElementAt((float[])tempvec, k);
                                        grid.NodesCopy[k*grid.dimension+j] = tempvec[j];
				}
			}
		}
/*CALCULATE THE GRAPH ENERGY */
	taxons = ((ElasticTree) grid).calcTaxons(data);
	energy = 0f;
        nodedata_energy = 0f;
        rib_energy = 0f;
        edge_energy = 0f;
        float en = 0f;
//node-data energies
	for(int i=0;i<grid.nodesNum;i++) {
	float dist = 0;
	int tx[] = (int[])taxons.elementAt(i);
	/*if(tx.length==0) {		//reject addtions that leave nodes owning no data
		energy = 1f/0f;
	}*/
	float tempvec1[] = (float[])grid.TreeNodesCopy.elementAt(i);

	if(data.weighted) {
		for(int k=0;k<tx.length;k++) {
			float tempvec2[] = (float[])data.getVector(tx[k]);
                        en = data.weights[tx[k]]*VVectorCalc.SquareEuclDistance(tempvec2,tempvec1);
			energy+=en;
                        nodedata_energy+=en;
		}
	}
	else {
		for(int k=0;k<tx.length;k++) {
			float tempvec2[] = (float[])data.getVector(tx[k]);
                        en = VVectorCalc.SquareEuclDistance(tempvec2,tempvec1);
			energy += en;
                        nodedata_energy+=en;
		}
	}
	}
	if(data.weighted) {
		energy/=data.weightSum;
                nodedata_energy/=data.weightSum;
	}
	else {
		energy/= data.pointCount;
                nodedata_energy/=data.pointCount;
	}
//rib energies
	for(int i=0;i<grid.nribsNum;i++) {
		int order = ((IntegerVector)grid.NRibs.elementAt(i)).size()-1;
		//double rp = lambda;
		ribSum = new float[grid.dimension];
		for(int j=0;j<grid.dimension;j++) {
			ribSum[j] = 0f;
		}
		for(int j=1;j<=order;j++) {
			int extnode = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
			float tempvec4[] = (float[])grid.TreeNodesCopy.elementAt(extnode);
			ribSum = VVectorCalc.Add_(ribSum , tempvec4);
		}
		float tempvec3[] = VVectorCalc.Product_((float[])grid.TreeNodesCopy.elementAt(((IntegerVector)grid.NRibs.elementAt(i)).getInt(0)),(float)order);

                en = (float)globalRPFactor*(VVectorCalc.SquareEuclDistance(ribSum,tempvec3));
                // Experiment!!! Prevent branching
                //en = (float)Math.pow(order,4)*globalRPFactor*(VVectorCalc.SquareEuclDistance(ribSum,tempvec3));
		energy += en;
                rib_energy+=en;

//edge energies

		if(order>1) {
			//double ep = lambda/10;
			int ribcentre = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(0);
			for(int j=1;j<=order;j++) {
				int ribouter = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
				//search forwards in the array for other instances of edge ribcentre-ribouter
				boolean sameEdge = false;
				for(int l=(i+1);l<grid.nribsNum;l++) {
				int forwardcentre = ((IntegerVector)grid.NRibs.elementAt(l)).getInt(0);
					for(int m=1;m<((IntegerVector)grid.NRibs.elementAt(l)).size();m++) {
						int forwardouter = ((IntegerVector)grid.NRibs.elementAt(l)).getInt(m);
						if((forwardouter==ribouter)&(forwardcentre==ribcentre)) {
							sameEdge = true;
						}
						if((forwardouter==ribcentre)&(forwardcentre==ribouter)) {
							sameEdge = true;
						}
					}
				}
				// if sameEdge is false add the edge otherwise leave it until the last instance of the edge is found
				if(!sameEdge) {
					float outervec[] = (float[])grid.TreeNodesCopy.elementAt(ribouter);
					float centrevec[] = (float[])grid.TreeNodesCopy.elementAt(ribcentre);
                                        en = (float)globalEPFactor*(VVectorCalc.SquareEuclDistance(outervec,centrevec));
					energy += en;
                                        edge_energy+=en;
				}
			}

		}
	}

	/*for(int i=0;i<grid.nribsNum;i++)  {
		double ep = lambda/100;
		int cent = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(0);
		for(int j=1;j<((IntegerVector)grid.NRibs.elementAt(i)).size();j++) {
			int outer = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
			float node1[] = (float[])grid.TreeNodesCopy.elementAt(cent);
			float node2[] = (float[])grid.TreeNodesCopy.elementAt(outer);
			energy += (float)ep*(VVectorCalc.SquareEuclDistance(node1,node2));
		}
	}*/
}
//****************************************************************************************************************
// CALCULATE THE SYSTEM MATRIX
  public void calcTreeMatrix(){
//diagonal elements
    for(int i=0;i<grid.nodesNum;i++){
            double d = 0;
            int tx[] = (int[])taxons.elementAt(i);
            if(data.weighted){
                    for(int k=0;k<tx.length;k++)
                            d+=data.weights[tx[k]];
                    d/=data.weightSum;
            }
            else {
                    d = (double)tx.length/(data.pointCount);
	    }
	    solver.addToMatrixElement(i,i,d);
    }
//NRib elements

		/*ELASTICITY COEFFICIENT IS HERE*/
	//double rp = lambda;
	for(int i=0;i<grid.nribsNum;i++) {
                int order = ((IntegerVector)grid.NRibs.elementAt(i)).size()-1;
                k = new int[order+1];
		for(int j=0;j<=order;j++) {
			k[j] = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
		}
		solver.addToMatrixElement(k[0],k[0],order*order*globalRPFactor);
		for(int j=1;j<=order;j++) {
			solver.addToMatrixElement(k[0],k[j],-order*globalRPFactor);
			solver.addToMatrixElement(k[j],k[0],-order*globalRPFactor);
			for(int l=1;l<((IntegerVector)grid.NRibs.elementAt(i)).size();l++) {
				solver.addToMatrixElement(k[j],k[l],globalRPFactor);
			}
		}
		//edge elements
		//double ep = lambda/10;
		if(order>1) { //add edges for rib if rib is more than one edge (it will be :) but might not be if we initialise differently later)
			int ribcentre = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(0);
			for(int j=1;j<=order;j++) {
				int ribouter = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
				//search forwards in the array for other instances of edge ribcentre-ribouter
				boolean sameEdge = false;
				for(int l=(i+1);l<grid.nribsNum;l++) {
				int forwardcentre = ((IntegerVector)grid.NRibs.elementAt(l)).getInt(0);
					for(int m=1;m<((IntegerVector)grid.NRibs.elementAt(l)).size();m++) {
						int forwardouter = ((IntegerVector)grid.NRibs.elementAt(l)).getInt(m);
						if((forwardouter==ribouter)&(forwardcentre==ribcentre)) {
							sameEdge = true;
						}
						if((forwardouter==ribcentre)&(forwardcentre==ribouter)) {
							sameEdge = true;
						}
					}
				}
				// if sameEdge is false add the edge otherwise leave it until the last instance of the edge is found
				if(!sameEdge) {
					k[0] = ribcentre;
					k[1] = ribouter;
					solver.addToMatrixElement(k[0],k[0],globalEPFactor);
					solver.addToMatrixElement(k[1],k[1],globalEPFactor);
					solver.addToMatrixElement(k[0],k[1],-globalEPFactor);
					solver.addToMatrixElement(k[1],k[0],-globalEPFactor);
				}
			}

		}
	}
// edge elements
	/*for(int i=0;i<grid.nribsNum;i++){
		k[0] = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(0);
		for(int j=1;j<((IntegerVector)grid.NRibs.elementAt(i)).size();j++)  {
			k[1] = ((IntegerVector)grid.NRibs.elementAt(i)).getInt(j);
			solver.addToMatrixElement(k[0],k[0],rp);
			solver.addToMatrixElement(k[1],k[1],rp);
			solver.addToMatrixElement(k[0],k[1],-rp);
			solver.addToMatrixElement(k[1],k[0],-rp);
			//System.out.println("rp is " + rp);
			//System.out.println("lambda is " + lambda);
		}
	}*/

     solver.createMatrix();
     solver.createPreconditioner();
  }
  public void calcTreeTaxons(){
    ((ElasticTree)grid).MakeTreeNodesCopy();
    taxons = ((ElasticTree)grid).calcTaxons(data);
  }

   /*public void calcTreeRightHandVector(int coord,double vec[]){
    for(int i=0;i<grid.nodesNum;i++){
    vec[i] = 0;
    int tx[] = (int[])taxons.elementAt(i);
    for(int j=0;j<tx.length;j++)
             if(!data.weighted)
                          vec[i] += data.massif[tx[j]][coord];
             else
                          vec[i] += data.massif[tx[j]][coord]*data.weights[tx[j]];
    }
    for(int i=0;i<grid.nodesNum;i++)
                  if(!data.weighted)
              vec[i] = vec[i]/(data.pointCount);
                  else
                          vec[i] = vec[i]/data.weightSum;
  }*/

  public void addNodeToRib(int nodenumber, int ribnumber, float[] nodePos) {
	  ((IntegerVector)grid.NRibs.elementAt(ribnumber)).appendInt(nodenumber);
	  grid.TreeNodes.add(new float[grid.dimension]);
	  grid.TreeNodes.setElementAt((float[])nodePos, grid.nodesNum);
	  grid.nodesNum++;
  }
  public void deleteNodeFromRib(int ribnumber) {
	  ((IntegerVector)grid.NRibs.elementAt(ribnumber)).removeElementAt((((IntegerVector)grid.NRibs.elementAt(ribnumber)).size()-1));
	  grid.TreeNodes.removeElementAt(grid.TreeNodes.size()-1);
	  grid.nodesNum--;
  }
  public void createNewRib(int node0, int node1, int node2, float[] nodePos) {
	  IntegerVector newRib = new IntegerVector();
	  newRib.appendInt(node0);
	  newRib.appendInt(node1);
	  newRib.appendInt(node2);
	  grid.NRibs.add(new IntegerVector());
	  grid.NRibs.setElementAt((IntegerVector)newRib, grid.nribsNum);
  	  grid.nribsNum++;
	  grid.TreeNodes.add(new float[grid.dimension]);
	  grid.TreeNodes.setElementAt((float[])nodePos, grid.nodesNum);
	  grid.nodesNum++;
  }
  public void deleteRib() {
	  grid.NRibs.removeElementAt(grid.NRibs.size()-1);
	  grid.nribsNum--;
	  grid.TreeNodes.removeElementAt(grid.TreeNodes.size()-1);
	  grid.nodesNum--;
  }
   public void getNewInnerNodePosition(int out1, int out2, int cen, float len) {
	  float tempvec1[] = (float[])grid.TreeNodes.elementAt(out1);
	  float tempvec2[] = (float[])grid.TreeNodes.elementAt(out2);
	  float tempvec0[] = (float[])grid.TreeNodes.elementAt(cen);
	  candNode = VVectorCalc.Subtract_(tempvec1,tempvec0);
	  candNode = VVectorCalc.Product_(candNode,1/(float)VVectorCalc.Norm(candNode));
	  float tempvec[] = VVectorCalc.Subtract_(tempvec2,tempvec0);
	  float vecnorm = (float)VVectorCalc.Norm(tempvec);
	  tempvec = VVectorCalc.Product_(tempvec,1/vecnorm);
	  candNode = VVectorCalc.Add_(candNode,tempvec);
	  float candnorm = (float)VVectorCalc.Norm(candNode);
	  candNode = VVectorCalc.Product_(candNode,len/candnorm);
	  candNode = VVectorCalc.Add_(candNode, tempvec0);
   }
  public void getNewOuterNodePosition(int out, int cen, float len) {
	  float tempvec[] = (float[])grid.TreeNodes.elementAt(out);
	  float tempvec0[] = (float[])grid.TreeNodes.elementAt(cen);
	  candNode = VVectorCalc.Subtract_(tempvec,tempvec0);
	  float length = (float)((len)/VVectorCalc.Norm(candNode));
	  candNode = VVectorCalc.Product_(candNode,length);
	  candNode = VVectorCalc.Add_(tempvec,candNode);
  }
  public void getCentreOfEdge(int out, int cen) {
	  float tempvec[] = (float[])grid.TreeNodes.elementAt(out);
	  float tempvec0[] = (float[])grid.TreeNodes.elementAt(cen);
	  candNode = VVectorCalc.Subtract_(tempvec,tempvec0);
	  float length = (float)(0.5/VVectorCalc.Norm(candNode));
	  candNode = VVectorCalc.Product_(candNode,length);
	  candNode = VVectorCalc.Add_(tempvec0,candNode);
  }

}