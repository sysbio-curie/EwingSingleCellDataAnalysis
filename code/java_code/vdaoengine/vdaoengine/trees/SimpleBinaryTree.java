package vdaoengine.trees;

import java.util.*;
import java.io.*;


public class SimpleBinaryTree{

public boolean rooted = false;
public TreeNode root;
public Vector rankDivision = null;

public SimpleBinaryTree(){
}

public void setRooted(boolean b){
	rooted = b;
}
public boolean getRooted(){
	return rooted;
}

public TreeNode getRoot(){
        return root;
}



public void loadFromFile(String fn) throws Exception{
	String ss = "";
	String s = "";
	LineNumberReader lr = new LineNumberReader(new FileReader(fn));
	while((ss=lr.readLine())!=null){
		s = s + ss.trim();
	}
        int start = 0;
	byte bm[] = s.getBytes();
	int k = TreeNode.skipParenthes(bm,start);
	String subs = s.substring(start+1,k-1);
	TreeNode tr = new TreeNode();
	tr.constructFromString(subs);
	Vector ch = tr.getChilds();
	if(ch.size()==2){
		rooted = true;
		root = tr;
		root.setLabel("root");
	}
	if(ch.size()==3){
		rooted = false;
		TreeNode ch1 = (TreeNode)ch.elementAt(0);
		TreeNode ch2 = (TreeNode)ch.elementAt(1);
		TreeNode ch3 = (TreeNode)ch.elementAt(2);
		float l1 = ((Float)tr.getLengthes().elementAt(0)).floatValue();
		float l2 = ((Float)tr.getLengthes().elementAt(1)).floatValue();
		float l3 = ((Float)tr.getLengthes().elementAt(2)).floatValue();
		TreeNode n = null;
		TreeNode n1 = null;
		float len = 0;
		float len1 = 0;
		if((l1>=l2)&&(l1>=l3)){
			n1 = ch1;
			n = new TreeNode();
			n.addChild(ch2,new Float(l2));
			n.addChild(ch3,new Float(l3));
			n.setLabel("bp");
			float av = n.getAverageLength();
			float av1 = ch1.getAverageLength();
			float x = (av1-av+l1)/2f;
			if(x<0f) x=0f;
			if(x>=l1) x=l1;
			len = x;
			len1 = l1 - x;
		}
		if((l2>=l1)&&(l2>=l3)){
			n1 = ch2;
			n = new TreeNode();
			n.addChild(ch1,new Float(l2));
			n.addChild(ch3,new Float(l3));
			n.setLabel("bp");
			float av = n.getAverageLength();
			float av1 = ch2.getAverageLength();
			float x = (av1-av+l2)/2f;
			if(x<0f) x=0f;
			if(x>=l2) x=l2;
			len = x;
			len1 = l2 - x;
		}
		if((l3>=l2)&&(l3>=l1)){
			n1 = ch3;
			n = new TreeNode();
			n.addChild(ch1,new Float(l2));
			n.addChild(ch2,new Float(l3));
			n.setLabel("bp");
			float av = n.getAverageLength();
			float av1 = ch1.getAverageLength();
			float x = (av1-av+l3)/2f;
			if(x<0f) x=0f;
			if(x>=l3) x=l3;
			len = x;
			len1 = l3 - x;
		}
		root = new TreeNode();
		root.setLabel("root");
		root.addChild(n,new Float(len));
		root.addChild(n1,new Float(len1));
	}
}


public Vector getRankDivision(){
	return rankDivision;
}

public Vector getDivisionForRank(int rank){
	return (Vector)rankDivision.elementAt(rank-1);
}

/**
calculates Vector         of Vectors         TreeNodes
           for every rank    set of classes  class
*/
public void calculateRankDivision(){
int numberOfLeafs = root.getLeafs().size();
rankDivision = new Vector();

// initializing rank1
Vector fr1 = new Vector();
rankDivision.addElement(fr1);
for(int k=0;k<root.getChilds().size();k++){
	TreeNode n = (TreeNode)root.getChilds().elementAt(k);
	fr1.addElement(n);
}

//Then by induction
for(int i=0;i<numberOfLeafs-2;i++){
	Vector prevRank = (Vector)rankDivision.elementAt(i);
	Vector thisRank = new Vector();
	// calculate which node divides:
	int minj = -1;
	float minv = Float.MAX_VALUE;
        for(int j=0;j<prevRank.size();j++){
                TreeNode trn = (TreeNode)prevRank.elementAt(j);
		float dist = trn.getLengthToRoot();
                if(trn.getChilds().size()>0)
                  if(dist<minv){ minv = dist; minj = j; }
	}
        for(int j=0;j<prevRank.size();j++){
		TreeNode tn = (TreeNode)prevRank.elementAt(j);
		if(j!=minj) thisRank.addElement(tn);
		else{
			for(int k=0;k<tn.getChilds().size();k++){
				thisRank.addElement(tn.getChilds().elementAt(k));
			}
		}
	}
	rankDivision.addElement(thisRank);
}

}

public TreeNode getNodeByLabel(String label){
  TreeNode res = null;
  Vector all = getAllNodes(root);
  for(int i=0;i<all.size();i++){
    TreeNode nod = (TreeNode)all.elementAt(i);
    if(nod.label.equals(label))
      res = nod;
  }
  return res;
}

public Vector getAllNodes(TreeNode nod){
  Vector res = new Vector();
  res.add(nod);
  for(int i=0;i<nod.childs.size();i++){
    TreeNode nd = (TreeNode)nod.childs.elementAt(i);
    Vector all = getAllNodes(nd);
    for(int j=0;j<all.size();j++)
      res.add(all.elementAt(j));
  }
  return res;
}


}