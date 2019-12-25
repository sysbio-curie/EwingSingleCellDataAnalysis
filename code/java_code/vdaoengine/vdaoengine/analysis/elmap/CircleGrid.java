package vdaoengine.analysis.elmap;

public class CircleGrid extends Curve {

	  public CircleGrid(int cols, int dim) {
		    colCount = cols;
		    dimension = dim;
		    type = 1;
		    generate();
		  }

	  public void generate(){

		    nodesNum = colCount;
		    edgesNum = colCount;
		    ribsNum = colCount;
		    trianglesNum = 0;
		    intDim = 1;

		    allocate();

		      for(int j=0;j<colCount;j++) {
		    	float phi = 2*3.141526f/colCount*j;
		        Nodes3D[j][0]=(float)Math.cos(phi);
		        Nodes3D[j][1]=(float)Math.sin(phi);
		        Nodes3D[j][2]=0;
		      }

		    int k=0;
		       for(int j=0;j<colCount;j++) {
		        if(j<colCount-1) {
		         Edges[k][0]=j;
		         Edges[k][1]=j+1;
		         k++;
		        }else{
			         Edges[k][0]=j;
			         Edges[k][1]=j+1;
		        }
		       }

		     k=0;
		      for(int j=0;j<colCount;j++) {
		         if(j<colCount-2) {
		          Ribs[k][0]=j+1;
		          Ribs[k][1]=j;
		          Ribs[k][2]=j+2;
		          k++;
		         }
		        }

		  }
	  
	  
}
