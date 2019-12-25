package vdaoengine.trees;

import java.util.*;
import java.io.*;


public class TreeNode{

public String label = null;
public Vector childs = new Vector();
public Vector lengthes = new Vector();
public TreeNode parent;
public Object addObject;

public TreeNode(){
}

public void addChild(TreeNode ch, Float len){
	childs.add(ch);
	lengthes.add(len);
	ch.setParent(this);
}

public void setParent(TreeNode p){
	parent = p;
}

public TreeNode getParent(){
	return parent;
}

public Object getAddObject(){
	return addObject;
}
public void setAddObject(Object o){
	addObject = o;
}


public Vector getChilds(){
	return childs;
}

public void setLabel(String ob){
	label = ob;
}

public Vector getLengthes(){
	return lengthes;
}


public String getLabel(){
	return label;
}

public float getAverageLength(){
float res = 0f;
int n = 0;
	for(int i=0;i<childs.size();i++){
		if(lengthes.elementAt(i)!=null){
			TreeNode tn = (TreeNode)childs.elementAt(i);
			res+=((Float)lengthes.elementAt(i)).floatValue()+tn.getAverageLength();
			n++;
		}
	}
if(n!=0) res=res/n;
return res;
}

public float getLengthToRoot(){
	float res = 0f;
	if(parent!=null){
		for(int i=0;i<parent.getChilds().size();i++){
			if(parent.getChilds().elementAt(i).equals(this)){
			res = ((Float)(parent.getLengthes().elementAt(i))).floatValue();
			}
		}
	res+=parent.getLengthToRoot();
	}
	return res;
}

public Vector getLeafs(){
Vector res = new Vector();
int n = 0;
for(int i=0;i<childs.size();i++){
		TreeNode tn = (TreeNode)childs.elementAt(i);
		if(tn.getChilds().size()==0) res.addElement(tn);
		else{
		Vector r = tn.getLeafs();
		for(int j=0;j<r.size();j++){
			res.addElement(r.elementAt(j));
		}
		}
}
return res;
}

public String print(String otst){
	StringBuffer res = new StringBuffer();
	String addinfo = "";
	if(childs.size()!=0)  addinfo = "(l="+getAverageLength()+",n="+getLeafs().size()+")";
	else addinfo = "(lr="+getLengthToRoot()+")";
	res.append(getLabel()+addinfo+"\n");
	for(int i=0;i<childs.size();i++){
        	TreeNode tr = (TreeNode)childs.elementAt(i);
		Float len = (Float)lengthes.elementAt(i);
		if(len==null)
			res.append(otst+"- "+tr.print(otst+"     "));
		else
			res.append(otst+"- "+len.toString()+":"+tr.print(otst+"     "));
	}
return res.toString();
}

public String toString(){
	return(print(""));
}

public void constructFromString(String s){
	Vector pts = divideIntoParts(s,',');
	for(int i=0;i<pts.size();i++){
		String n = (String)pts.elementAt(i);
		Vector naml = divideIntoParts(n,':');
		if(naml.size()>2) System.out.println("Something wrong with:"+n);
		String nam = ((String)naml.elementAt(0)).trim();
		String len = "";
		if(naml.size()>1)
			len = ((String)naml.elementAt(1)).trim();
		TreeNode nd = new TreeNode();
		if(nam.startsWith("(")){
			String newn = nam.substring(1,nam.length()-1);
			nd.constructFromString(newn);
			nd.setLabel("bp");
		}else{
			nd.setLabel(nam);
		}
		float l = -1;
		if(!len.equals("")) {
			l = Math.abs(Float.parseFloat(len));
			addChild(nd,new Float(l));
			}
		else{
			addChild(nd,null);
		}
	}
}

public static Vector divideIntoParts(String s,char delim){
Vector res = new Vector();
byte st[] = s.getBytes();
int i=0;
while(i<st.length){
	int start = i;
	if(st[i]=='(')
		i = skipParenthes(st,i);
	while((i<st.length-1)&&(st[i]!=delim)){
		i++;
		if(st[i]=='(')
			i = skipParenthes(st,i);
	}
	if(i==st.length-1)
		res.add(s.substring(start,i+1));
	else
		res.add(s.substring(start,i));
	i++;
}
return res;
}

public static int skipParenthes(byte st[],int k){
int i = k;
while((i<st.length)&&(st[i]!=')')){
	i++;
	if(st[i]=='('){
		i = skipParenthes(st,i);
	}
}
return i+1;
}

public static void main(String args[]){

try{
//	String s = "((PSTS_ECOLI:0.06509,1IXH|:-0.00929):0.26870,a,(c,d));";
//	String s = "(PSTS_ECOLI:0.06509,1IXH|:-0.00929:0.26870,a);";
	String ss = "";
	String s = "";
	LineNumberReader lr = new LineNumberReader(new FileReader("treefile"));
	while((ss=lr.readLine())!=null){
		s = s + ss;
	}
        int start = 0;
	byte bm[] = s.getBytes();
	int k = skipParenthes(bm,start);
	System.out.println(k+" "+((char)bm[k]));
	System.out.println(s);
	for(int i=0;i<k;i++) System.out.print("-");
	System.out.println("*");
	String subs = s.substring(start+1,k-1);
	System.out.println(subs);

	Vector pts = divideIntoParts(subs,',');
	for(int i=0;i<pts.size();i++)
		System.out.println("Part"+i+":"+pts.elementAt(i));

	TreeNode tr = new TreeNode();
	tr.setLabel("root - ");
	tr.constructFromString(subs);
	System.out.println(tr);
}catch(Exception e){e.printStackTrace();}
}

}