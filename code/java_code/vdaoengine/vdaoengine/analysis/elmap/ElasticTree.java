package vdaoengine.analysis.elmap;

import vdaoengine.analysis.elmap.Grid;

import java.util.*;
import java.io.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.trees.*;
import vdaoengine.analysis.*;

public class ElasticTree extends Grid {


int nribsNum;		// declare the number of NRibs

//int treeNodesNum;


Vector TreeNodes = new Vector();	//probably going for vector of float[dimension] arrays
Vector TreeNodes3D = new Vector();					//to be used generically

float temp[];
float temp2[];

public static int PROJECT_POINT = 0;
public static int PROJECT_NODE = 1;
public static int PROJECT_TERMINAL_NODE = 2;

Vector TreeNodesCopy = new Vector();			//actually want a vector

SimpleTree TreeRepresentation = null;

public Vector flips = new Vector();
public HashMap multipliers = new HashMap();

public static void main(String args[]){
	ElasticTree eltree = new ElasticTree(4);
	eltree.loadFromFile("C:/Datas/ElGraph_Matlab/test_data/iris.vem");
	
	System.out.println("Number of nodes ="+eltree.Nodes.length);
	System.out.println("Number of edges ="+eltree.Edges.length);
	System.out.println("Number of ribs ="+eltree.NRibs.size());
	
	eltree.calculateTreeLayout(1, true);
	eltree.writeOutTreeNodes2D("C:/Datas/ElGraph_Matlab/test_data/iris.eltree.txt");
	eltree.writeOutEdges("C:/Datas/ElGraph_Matlab/test_data/iris.eltree.edges");	
	
}


public ElasticTree(int dim) {
	nodesNum = 3;
	edgesNum = 1;
	nribsNum = 1;
	dimension = dim;
	type = 0;

	for(int i=0;i<nodesNum;i++) {
		TreeNodes.add(new float[dimension]);
	}
}
public Vector calcTreeTaxons(VDataSet data){

          Vector taxons = new Vector();
	  taxons.setSize(0);

          for(int j=0;j<nodesNum;j++){
                  Vector v = new Vector();
                  taxons.add(v);
          }
          float r = 0f;

          int dimen = data.coordCount;

          for(int i=0;i<data.pointCount;i++){
                  float v[] = data.getVector(i);
                  float minr = (float)Float.MAX_VALUE ;
                  int minj = -1;
                  for(int j=0;j<nodesNum;j++){
                          r = VVectorCalc.VecSquareEuclDistanceShift(TreeNodesCopy,j,v,data.coordCount);
                          if(r<minr){ minr=r; minj = j; }
                  }
                  if(minj!=-1){
                          ((Vector)taxons.elementAt(minj)).add(new Integer(i));
                  }
          }
          Vector res = new Vector();
          for(int i=0;i<nodesNum;i++){
            Vector v = (Vector)taxons.elementAt(i);   ///taxons.elementAt(i)..... ?
            int tx[] = new int[v.size()];
	    //System.out.println("length of vector at tx[node"+i+"] is "+v.size());
            for(int j=0;j<v.size();j++) {
              tx[j] = ((Integer)v.elementAt(j)).intValue();
	      //System.out.println("tx["+j+"] = "+tx[j]);

	    }
            res.add(tx);
	    //System.out.println("length of element in res = "+ ((int[])res.elementAt(i)).length);
          }
          return res;
  }


   public void MakeTreeNodesCopy() {
//treenodescopy doesnt exist here ...... so ... well what? i guess it may not exist elsewhere?
    TreeNodesCopy = new Vector();
    TreeNodesCopy.setSize(nodesNum);
    //and here it is null
/************************************************************************************************************************************
//output the nodes before testing for additions
for(int i9=0;i9<treeNodesNum;i9++) {
		  for(int j9=0;j9<dimension;j9++) {
			  System.out.println("$$$$$$$$$$$$$$$BEFORE MAKING NODES COPY treenode "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodes.elementAt(i9))[j9]);
		  	System.out.println("$$$$$$$$$$$$$$$BEFORE MAKING NODES COPY treenodevopy "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodesCopy.elementAt(i9))[j9]);

		  }
	  }

/************************************************************************************************************************************/



    NodesCopy = new float[nodesNum*dimension];

    for(int i=0;i<nodesNum;i++) {
	    temp = new float[dimension];
	    temp2 = new float[dimension];

	    temp = (float[])TreeNodes.elementAt(i);

	    for(int j=0;j<dimension;j++) {
		    temp2[j] = temp[j];
                    NodesCopy[i*dimension+j] = temp[j];
	    }
	 TreeNodesCopy.setElementAt((float[])temp2, i);

    }


/************************************************************************************************************************************
//output the nodes before testing for additions
for(int i9=0;i9<treeNodesNum;i9++) {
		  for(int j9=0;j9<dimension;j9++) {
			  System.out.println("$$$$$$$$$$$$$$AFTER MAKIING NODES COPY treenode "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodes.elementAt(i9))[j9]);
			  System.out.println("$$$$$$$$$$$$$$AFTER MAKIING NODES COPY treenodecopy "+ i9 +" component ["+j9+"] = " + ((float[])TreeNodesCopy.elementAt(i9))[j9]);
		  }
	  }

/************************************************************************************************************************************/


  }

  public void writeOutNRibs(String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      for(int i=0;i<NRibs.size();i++){
        IntegerVector nrib = (IntegerVector)NRibs.elementAt(i);
        int cent = nrib.getInt(0);
        fw.write(""+cent);
        for(int j=1;j<nrib.size();j++){
          fw.write("\t"+nrib.getInt(j));
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void writeOutEdges(String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      for(int i=0;i<Edges.length;i++){
        fw.write(Edges[i][0]+"\t"+Edges[i][1]+"\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void writeOutTreeNodes(String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      for(int i=0;i<Nodes.length;i++){
        //float v[] = (float[])TreeNodes.elementAt(i);
        for(int j=0;j<dimension;j++){
          fw.write(""+Nodes[i][j]);
          if(j<dimension-1)
            fw.write("\t");
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void writeOutTreeNodes2D(String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      for(int i=0;i<Nodes3D.length;i++){
        for(int j=0;j<3;j++){
          fw.write(""+Nodes3D[i][j]);
          if(j<3-1)
            fw.write("\t");
        }
        fw.write("\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
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
    for(int i=0;i<edgesNum;i++){
      int i1 = Edges[i][0];
      int i2 = Edges[i][1];
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
      float len = (float)VVectorCalc.Distance(Nodes[Integer.parseInt(nd.label)],Nodes[Integer.parseInt(node.label)]);

      int iedge = findEdge(Integer.parseInt(nd.label),Integer.parseInt(node.label));
      if(iedge!=-1)
       if(multipliers.containsKey(new Integer(iedge))){
         len*=((Float)multipliers.get(new Integer(iedge))).floatValue();
       }

      node.addChild(nd,new Float(len));
    }
  }


  public void calculateTreeLayout(int rootNodeNumber, boolean uniform){

    //First, we calculate global orientation directions (first two PC)
    VDataSet vdg = new VDataSet();
    vdg.pointCount = Nodes.length;
    vdg.coordCount = dimension;
    vdg.massif = Nodes;
    PCAMethod pcg = new PCAMethod();
    pcg.setDataSet(vdg);
    pcg.calcBasis(2);
    float pcgd[][] = new float[2][vdg.coordCount];
    for(int i=0;i<2;i++)
      for(int j=0;j<vdg.coordCount;j++){
          pcgd[i][j] = (float)pcg.getBasis().basis[i][j];
      }

    SimpleTree tr = convertToTree(rootNodeNumber);
    TreeNode root = tr.root;
    int iroot = Integer.parseInt(root.label);
    Nodes3D[iroot][0] = 0;
    Nodes3D[iroot][1] = 0;
    Nodes3D[iroot][2] = 0;

    IntegerVector nrib = getStarByCentralNode(iroot);
    Vector angs = calcAnglesOfStar(nrib,1,pcgd,uniform);
    int ncroot = root.childs.size();
    for(int i=0;i<ncroot;i++){
      //float phi = (float)2f*3.1415926f*(float)i/(float)ncroot;
      float phi = ((Float)angs.elementAt(i+1)).floatValue();
      float len = ((Float)(root.lengthes.elementAt(i))).floatValue();
      TreeNode nd = (TreeNode)root.childs.elementAt(i);
      iroot = Integer.parseInt(nd.label);
      if(flips.indexOf(new Integer(0))>=0)
        phi = 3.1415926f - phi;
      Nodes3D[iroot][0] = len*(float)Math.cos(phi);
      Nodes3D[iroot][1] = len*(float)Math.sin(phi);
      Nodes3D[iroot][2] = 0;
      calcTreeLayoutStartingFromNode(nd,pcgd,uniform);
    }
  }

  public void calcTreeLayoutStartingFromNode(TreeNode node, float pcgd[][], boolean uniform){
     int ncroot = node.childs.size()+1;
     int inode = Integer.parseInt(node.label);
     int inodep = Integer.parseInt(node.parent.label);

     IntegerVector nrib = new IntegerVector();
     nrib.appendInt(inode); nrib.appendInt(inodep);
     for(int i=0;i<node.childs.size();i++){
       int k = Integer.parseInt(((TreeNode)node.childs.elementAt(i)).label);
       nrib.appendInt(k);
     }
     float edgevec[] = VVectorCalc.Subtract_(Nodes3D[inodep],Nodes3D[inode]);
     VVectorCalc.Normalize(edgevec);
     //float dphi = (float)2f*3.1415926f/(float)ncroot;
     Vector angs = calcAnglesOfStar(nrib,1,pcgd,uniform);
     for(int i=1;i<ncroot;i++){
       float xv[] = new float[3];
       float phi = -((Float)angs.elementAt(i+1)).floatValue();
       if(flips.indexOf(new Integer(inode))>=0)
         phi = - phi;
       xv[0] = (float)(Math.cos(phi)*edgevec[0] + Math.sin(phi)*edgevec[1]);
       xv[1] = (float)(-Math.sin(phi)*edgevec[0] + Math.cos(phi)*edgevec[1]);
       TreeNode tn = (TreeNode)node.childs.elementAt(i-1);
       int ichild = Integer.parseInt(tn.label);
       float len = ((Float)node.lengthes.elementAt(i-1)).floatValue();

       Nodes3D[ichild][0] = Nodes3D[inode][0]+len*xv[0];
       Nodes3D[ichild][1] = Nodes3D[inode][1]+len*xv[1];
       Nodes3D[ichild][2] = 0f;
       calcTreeLayoutStartingFromNode(tn,pcgd,uniform);
     }
  }

  public PCAMethod calcPrincipalPlaneForStar(IntegerVector nrib,float pcgd[][]){
    PCAMethod res = new PCAMethod();
    VDataSet vd = new VDataSet();
    VDataSet vde = new VDataSet();

    //Only local
    vd.coordCount = dimension;
    vd.pointCount = nrib.size();
    vd.massif = new float[vd.pointCount][vd.coordCount];
    for(int i=0;i<nrib.size();i++)
      vd.massif[i] = Nodes[nrib.getInt(i)];
    float v[] = new float[vd.coordCount];
    for(int i=0;i<v.length;i++)
      v[i] = vd.massif[0][i];
    for(int i=0;i<vd.pointCount;i++)
      vd.massif[i] = VVectorCalc.Subtract_(vd.massif[i],v);
    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vd,"out.dat");

    //Local extended
    int radius = 3;
    Vector neighbours = new Vector();
    getNodeNeighbourhood(nrib.getInt(0),radius,neighbours);
    vde.coordCount = dimension;
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
    res.setDataSet(vd);


    // We can use global pcs
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

  public Vector calcAnglesOfStar(IntegerVector nrib, int sourceNode, float pcgd[][], boolean uniform){
    Vector ang = new Vector();
    PCAMethod pca = calcPrincipalPlaneForStar(nrib,pcgd);
    VDataSet proj = pca.getProjectedDataset();
    float v[] = new float[proj.coordCount];
    for(int i=0;i<v.length;i++)
      v[i] = proj.massif[0][i];
    for(int i=0;i<proj.pointCount;i++)
      proj.massif[i] = VVectorCalc.Subtract_(proj.massif[i],v);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(proj,"outp.dat");
    // now rotating projections
    //IntegerVector nrib = (IntegerVector)NRibs.elementAt(nribnum);
    float edgevec[] = VVectorCalc.Subtract_(proj.massif[sourceNode],proj.massif[0]);
    VVectorCalc.Normalize(edgevec);
    float phi = getAngle(edgevec);
    for(int i=0;i<proj.pointCount;i++){
      proj.massif[i] = rotate2DVector(proj.massif[i], phi);
      //System.out.println(proj.massif[i][0]+"\t"+proj.massif[i][1]);
    }
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(proj,"outpr.dat");
    for(int i=0;i<proj.pointCount;i++){
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

  public IntegerVector getStarByCentralNode(int centralnode){
    IntegerVector r = null;
    for(int i=0;i<NRibs.size();i++){
      IntegerVector nrib = (IntegerVector)NRibs.elementAt(i);
      if(nrib.getInt(0)==centralnode)
        r = nrib;
    }
    return r;
  }

  public void getNodeNeighbourhood(int node, int radius, Vector res){
    //Vector res = new Vector();
    if(res.indexOf(new Integer(node))<0)
       res.add(new Integer(node));
    if(radius>0)
    for(int i=0;i<Edges.length;i++){
      Vector neigh = new Vector();
      if(Edges[i][0]==node){
        getNodeNeighbourhood(Edges[i][1],radius-1,res);
      }
      if(Edges[i][1]==node){
        getNodeNeighbourhood(Edges[i][0],radius-1,res);
      }
      for(int j=0;j<neigh.size();j++)
        res.add(neigh.elementAt(j));
    }
    //return res;
  }

  public int findEdge(int k1, int k2){
    int res = -1;
    for(int i=0;i<Edges.length;i++){
      if((Edges[i][0]==k1)&&(Edges[i][1]==k2))
        res = i;
      if((Edges[i][1]==k1)&&(Edges[i][0]==k2))
        res = i;
    }
    return res;
  }

}

