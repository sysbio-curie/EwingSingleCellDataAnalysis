package vdaoengine.analysis.grammars;

import java.util.HashMap;
import java.util.Vector;

import vdaoengine.analysis.PCAMethod;
import vdaoengine.data.VDataSet;
import vdaoengine.trees.SimpleTree;
import vdaoengine.trees.TreeNode;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.IntegerVector;
import vdaoengine.utils.VLinearBasis;
import vdaoengine.utils.VVectorCalc;

public class MetroMapLayout {
	
	public Tree principalTree = null;
	
	public Tree computedLayout = null;
	
	SimpleTree TreeRepresentation = null;
	
	public Vector<Integer> flips = new Vector<Integer>();
	public HashMap<Integer,Float> multipliers = new HashMap<Integer,Float>();
	
	public int dataDimension = 0; 


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			MetroMapLayout mml = new MetroMapLayout();
			
			// Defining tree embedment and structure
			mml.principalTree = new Tree();
			
			String prefix = "iris";
			//String prefix = "tree23";
			
			float NodePositions[][] = Utils.loadMatrixFromFile("C:/Datas/ElGraph_Matlab/test_data/"+prefix+".nodes");
			int EdgeDefinitions[][] = Utils.loadIntegerMatrixFromFile("C:/Datas/ElGraph_Matlab/test_data/"+prefix+".edges");
			Utils.setNodesForGraph(mml.principalTree,NodePositions);
			Utils.setEdgesForGraph(mml.principalTree,EdgeDefinitions);
			mml.principalTree.defineStarsFromPrimitiveGraphStructure();
			
			//mml.principalTree.saveToFile("C:/Datas/ElGraph_Matlab/test_data/iris3.vem", "iris");
			
			// Computing the initial metro map layout
			mml.computeLayout();
			
			mml.computedLayout.writeOutNodes("C:/Datas/ElGraph_Matlab/test_data/"+prefix+".nodes2d");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void computeLayout(){
		Tree layout = new Tree();
		Node firstNode = principalTree.Nodes.get(0);
		dataDimension = firstNode.x.length;
		
		// Find 'central' tree node
		Node central = principalTree.getStarCentralNodeMean();
		//Node central = principalTree.Nodes.get(1);
		
		layout = computeTreeLayout(principalTree.Nodes.indexOf(central),true);
		
		computedLayout = layout;
	}
	
	  public Tree computeTreeLayout(int rootNodeNumber, boolean uniform){

		  	Tree layout = new Tree();
		  	for(int i=0;i<principalTree.Nodes.size();i++){
		  		Node n = principalTree.Nodes.get(i).clone();
		  		n.x = new float[3];
		  		layout.addNode(n);
		  	}
		  	layout.Edges = principalTree.Edges;
		  	layout.Stars = principalTree.Stars;
		  
		    //First, we calculate global orientation directions (first two PC)
		    VDataSet vdg = new VDataSet();
		    vdg.pointCount = principalTree.Nodes.size();
		    vdg.coordCount = dataDimension;
		    vdg.massif = principalTree.getNodePositions();
		    PCAMethod pcg = new PCAMethod();
		    pcg.setDataSet(vdg);
		    pcg.calcBasis(2);
		    float pcgd[][] = new float[2][vdg.coordCount];
		    for(int i=0;i<2;i++)
		      for(int j=0;j<vdg.coordCount;j++){
		          pcgd[i][j] = (float)pcg.getBasis().basis[i][j];
		      }

		    principalTree.calcNodeInOut();
		    SimpleTree tr = convertToTree(rootNodeNumber);
		    TreeNode root = tr.root;
		    int iroot = Integer.parseInt(root.label);

		    layout.Nodes.get(iroot).x[0] = 0;
		    layout.Nodes.get(iroot).x[1] = 0;
		    layout.Nodes.get(iroot).x[2] = 0;

		    Star rootStar = principalTree.getStarByCentralNode(iroot);
		    Vector<Float> angs = calcAnglesOfStar(rootStar,1,pcgd,uniform);
		    int ncroot = root.childs.size();
		    for(int i=0;i<ncroot;i++){
		      //float phi = (float)2f*3.1415926f*(float)i/(float)ncroot;
		      float phi = ((Float)angs.elementAt(i+1)).floatValue();
		      float len = ((Float)(root.lengthes.elementAt(i))).floatValue();
		      TreeNode nd = (TreeNode)root.childs.elementAt(i);
		      iroot = Integer.parseInt(nd.label);
		      if(flips.indexOf(rootNodeNumber)>=0)
		        phi = 3.1415926f - phi;
		      layout.Nodes.get(iroot).x[0] = len*(float)Math.cos(phi);
		      layout.Nodes.get(iroot).x[1] = len*(float)Math.sin(phi);		      
		      layout.Nodes.get(iroot).x[2] = 0;
		      calcTreeLayoutStartingFromNode(layout, nd,pcgd,uniform);
		    }
		    return layout;
		  }
	  
	  public void calcTreeLayoutStartingFromNode(Tree layout, TreeNode node, float pcgd[][], boolean uniform){
		     int ncroot = node.childs.size()+1;
		     int inode = Integer.parseInt(node.label);
		     int inodep = Integer.parseInt(node.parent.label);
		     
		     Star currentStar = new Star();
		     currentStar.centralNode = principalTree.Nodes.get(inode);
		     currentStar.neighbours.add(principalTree.Nodes.get(inodep));
		     for(int i=0;i<node.childs.size();i++){
			       int k = Integer.parseInt(((TreeNode)node.childs.elementAt(i)).label);
			       currentStar.neighbours.add(principalTree.Nodes.get(k));
			     }
		     
		     
		     float edgevec[] = VVectorCalc.Subtract_(layout.Nodes.get(inodep).x,layout.Nodes.get(inode).x);
		     VVectorCalc.Normalize(edgevec);
		     //float dphi = (float)2f*3.1415926f/(float)ncroot;
		     Vector angs = calcAnglesOfStar(currentStar,1,pcgd,uniform);
		     for(int i=1;i<ncroot;i++){
		       float xv[] = new float[3];
		       float phi = -((Float)angs.elementAt(i+1)).floatValue();
		       if(flips.indexOf(inode)>=0)
		         phi = - phi;
		       xv[0] = (float)(Math.cos(phi)*edgevec[0] + Math.sin(phi)*edgevec[1]);
		       xv[1] = (float)(-Math.sin(phi)*edgevec[0] + Math.cos(phi)*edgevec[1]);
		       TreeNode tn = (TreeNode)node.childs.elementAt(i-1);
		       int ichild = Integer.parseInt(tn.label);
		       float len = ((Float)node.lengthes.elementAt(i-1)).floatValue();

			     if(node.label.equals("2"))
			    	 System.out.println();
		       
		       layout.Nodes.get(ichild).x[0] = layout.Nodes.get(inode).x[0] + len*xv[0];
		       layout.Nodes.get(ichild).x[1] = layout.Nodes.get(inode).x[1] + len*xv[1];
		       layout.Nodes.get(ichild).x[2] = 0;
		       
		       calcTreeLayoutStartingFromNode(layout,tn,pcgd,uniform);
		     }
		  }

		  public PCAMethod calcPrincipalPlaneForStar(Star star,float pcgd[][]){
		    PCAMethod res = new PCAMethod();
		    VDataSet vd = new VDataSet();
		    VDataSet vde = new VDataSet();

		    
		    
		    //Only local
		    vd.coordCount = dataDimension;
		    vd.pointCount = star.neighbours.size()+1;
		    vd.massif = new float[vd.pointCount][vd.coordCount];
		    vd.massif[0] = star.centralNode.x;
		    for(int i=0;i<star.neighbours.size();i++)
		      vd.massif[i+1] = star.neighbours.get(i).x;
		    float v[] = new float[vd.coordCount];
		    for(int i=0;i<v.length;i++)
		      v[i] = vd.massif[0][i];
		    for(int i=0;i<vd.pointCount;i++)
		      vd.massif[i] = VVectorCalc.Subtract_(vd.massif[i],v);
		    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vd,"out.dat");

		    
		    //Local extended
		    /*int radius = 3;
		    Vector neighbours = new Vector();
		    getNodeNeighbourhood(principalTree.Nodes.indexOf(star.centralNode),radius,neighbours);
		    vde.coordCount = dataDimension;
		    vde.pointCount = neighbours.size();
		    vde.massif = new float[vde.pointCount][vde.coordCount];
		    for(int i=0;i<neighbours.size();i++){
		      int k = ((Integer)neighbours.elementAt(i)).intValue();
		      vde.massif[i] = Nodes[k];
		    }
		    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vd,"out.dat");
		    v = new float[vde.coordCount];
		    for(int i=0;i<v.length;i++)
		      v[i] = vde.massif[0][i];
		    for(int i=0;i<vde.pointCount;i++)
		      vde.massif[i] = VVectorCalc.Subtract_(vde.massif[i],v);
		      
		    res.setDataSet(vde);
		    res.calcBasis(2);
		    */

		    // We can use global pcs
		    res.linBasis = new VLinearBasis();
		    res.linBasis.a0 = new double[pcgd[0].length];
		    res.linBasis.basis = new double[pcgd.length][pcgd[0].length];
		    res.setDataSet(vd);
		    res.calculatedVectorsNumber = 2;
		    res.linBasis.numberOfBasisVectors = 2;
		    res.linBasis.isNormalized = true;
		    res.linBasis.isOrthogonal = true;
		    res.linBasis.spaceDimension = vd.coordCount;
		    for(int k=0;k<2;k++)
		      for(int i=0;i<vd.coordCount;i++)
		            res.getBasis().basis[k][i] = pcgd[k][i];
			
		    // we have to solve 'mirroring' problem
		    float ang1 = VVectorCalc.ScalarMult(pcgd[0],res.getBasis().basis[0]);
		    if(ang1<0)
		      for(int i=0;i<vd.coordCount;i++)
		        res.getBasis().basis[0][i] = -res.getBasis().basis[0][i];
		    float ang2 = VVectorCalc.ScalarMult(pcgd[1],res.getBasis().basis[1]);
		    if(ang2<0)
		      for(int i=0;i<vd.coordCount;i++)
		        res.getBasis().basis[1][i] = -res.getBasis().basis[1][i];

		    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vd,"outr.dat");
		    return res;
		  }

		  public Vector<Float> calcAnglesOfStar(Star star, int sourceNode, float pcgd[][], boolean uniform){
		    Vector<Float> ang = new Vector<Float>();
		    PCAMethod pca = calcPrincipalPlaneForStar(star,pcgd);
		    VDataSet proj = pca.getProjectedDataset();
		    float v[] = new float[proj.coordCount];
		    for(int i=0;i<v.length;i++)
		      v[i] = proj.massif[0][i];
		    for(int i=0;i<proj.pointCount;i++)
		      proj.massif[i] = VVectorCalc.Subtract_(proj.massif[i],v);
		    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(proj,"outp.dat");
		    // now rotating projections
		    //IntegerVector nrib = (IntegerVector)NRibs.elementAt(nribnum);
		    float edgevec[] = VVectorCalc.Subtract_(proj.massif[sourceNode],proj.massif[0]);
		    VVectorCalc.Normalize(edgevec);
		    float phi = getAngle(edgevec);
		    for(int i=0;i<proj.pointCount;i++){
		      proj.massif[i] = rotate2DVector(proj.massif[i], phi);
		      //System.out.println(proj.massif[i][0]+"\t"+proj.massif[i][1]);
		    }
		    ang.add(0f);
		    for(int i=1;i<proj.pointCount;i++){
		      VVectorCalc.Normalize(proj.massif[i]);
		      //System.out.println(getAngle(proj.massif[i]));
		      ang.add(new Float(getAngle(proj.massif[i])));
		    }
		    if(uniform){
		      float angs[] = new float[ang.size()-1];
		      angs[0] = 0f;
		      for(int i=2;i<ang.size();i++){
		        angs[i-1] = ((Float)ang.elementAt(i)).floatValue();
		      }
		      int ord[] = Algorithms.SortMass(angs);
		      ang.clear(); ang.add(new Float(0f));
		      float dphi = 2f*3.1415926f/(float)angs.length;
		      for(int i=0;i<angs.length;i++){
		        ang.add(new Float(dphi*Algorithms.IndexOfI(ord,i)));
		      }
		    }
		    return ang;
		  }

		  public static float getAngle(float x[]){
		    float r = (float)Math.acos(-x[0]);
		    if(x[1]>0f){
		      r = 2f*3.1415926f - r;
		    }
		    return r;
		  }

		  public static float[] rotate2DVector(float x[], float phi){
		    float r[] = new float[2];
		    r[0] = (float)(Math.cos(phi)*x[0] + Math.sin(phi)*x[1]);
		    r[1] = (float)(-Math.sin(phi)*x[0] + Math.cos(phi)*x[1]);
		    return r;
		  }
	  
	  
	  public SimpleTree convertToTree(int rootNodeNumber){ // -1 auto (central)
		    TreeRepresentation = new SimpleTree();
		    TreeNode root = new TreeNode();
		    TreeRepresentation.root = root; TreeRepresentation.setRooted(true);
		    if(rootNodeNumber==-1) rootNodeNumber = 0;
		    root.label = ""+rootNodeNumber;
		    addNodeChilds(root);

		    // Now calculate all edges lengthes

		    return TreeRepresentation;
		  }
	  
	  public void addNodeChilds(TreeNode node){
		    int rootNodeNumber = Integer.parseInt(node.label);
		    Vector childs = new Vector();
		    for(int i=0;i<principalTree.Edges.size();i++){
		      Edge e = principalTree.Edges.get(i);
		      int i1 = principalTree.Nodes.indexOf(e.getSource());
		      int i2 = principalTree.Nodes.indexOf(e.getTarget());
		      if(i1==rootNodeNumber){
		        TreeNode nod = new TreeNode();
		        nod.label = ""+i2;
		        nod.parent = node;
		        if(node.parent==null){
		           childs.add(nod);
		           addNodeChilds(nod);
		           }
		        if((node.parent!=null)&&(Integer.parseInt(node.parent.label)!=i2)){
		           childs.add(nod);
		           addNodeChilds(nod);
		           }
		      }
		      if(i2==rootNodeNumber){
		        TreeNode nod = new TreeNode();
		        nod.label = ""+i1;
		        nod.parent = node;
		        if(node.parent==null){
		           childs.add(nod);
		           addNodeChilds(nod);
		           }
		        if((node.parent!=null)&&(Integer.parseInt(node.parent.label)!=i1)){
		           childs.add(nod);
		           addNodeChilds(nod);
		           }
		      }
		    }
		    for(int i=0;i<childs.size();i++){
		      TreeNode nd = (TreeNode)childs.elementAt(i);
		      int i1 = Integer.parseInt(nd.label);
		      int i2 = Integer.parseInt(node.label);
		      float len = (float)VVectorCalc.Distance(principalTree.Nodes.get(i1).x,principalTree.Nodes.get(i2).x);

		      int iedge = principalTree.findEdgeIndex(i1,i2);
		      if(iedge!=-1)
		       if(multipliers.containsKey(new Integer(iedge))){
		         len*=((Float)multipliers.get(new Integer(iedge))).floatValue();
		       }

		      node.addChild(nd,new Float(len));
		    }
		  }
	  
	
}
