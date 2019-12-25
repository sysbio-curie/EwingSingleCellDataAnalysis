package vdaoengine.analysis.grammars;

public class Node extends GraphElement {

   public float x[] = null;
   public float xin[] = null;
   
   public String key = "";
   
   public static int globalCounter = 0;
   
   public void setX(double xd[]){
	   x = new float[xd.length];
	   for(int i=0;i<x.length;i++)
		   x[i] = (float)xd[i];
   }
   public void setX(float xd[]){
	   x = new float[xd.length];
	   for(int i=0;i<x.length;i++)
		   x[i] = (float)xd[i];
   }
   
   public Node(){
	   key = ""+globalCounter;
	   globalCounter++;
   }

   public Node clone(){
	   Node n = new Node();
	   if(x!=null){
	   n.x = new float[x.length];
	   for(int i=0;i<x.length;i++)
		   n.x[i] = x[i];
	   }
	   if(xin!=null){
	   n.xin = new float[xin.length];
	   for(int i=0;i<xin.length;i++)
		   n.xin[i] = xin[i];
	   }
	   copyProperties(n);
	   n.key = key;
	   return n;
   }
   
   public String toString(){
	   return key;
   }
	
}
