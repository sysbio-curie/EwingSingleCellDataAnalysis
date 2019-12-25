package vdaoengine.analysis.fastica;


import java.util.*;
import java.lang.*;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;

/*
 *	Example :		Calculate the whitening :
				LinkedList paramWhitenv = new LinkedList();
				paramWhitenv.add(0,mixedsig);
				paramWhitenv.add(1,E);
				paramWhitenv.add(2,D);
				paramWhitenv.add(3,verbose);
			
			resultWhitening = whitenv.calculate(paramWhitenv);
*/
public class whitenv{
	
	
	private 
			
			static Object vectors; 					// Cast : DoubleMatrix2D
			static Object E;  						// Cast : DoubleMatrix2D
			static Object D;						// Cast : DoubleMatrix2D
			static Object s_verbose;				// Cast : String
	
	
	
	/*=========================================================================
	 *  public static Object[] calculate(LinkedList L)
	 *   
	 *=======================================================================*/
	public static Object[] calculate(LinkedList L){
		Object[] matrix=new Object[3];
		try{

			Algebra alg = new Algebra();
			
			
			boolean b_verbose=defaultValues(L);
			testEigenvalues();
			
			/* Calculate the whitening and dewhitening matrices (these handle
			 * dimensionality simultaneously)
			 */
			int Dsize=((DoubleMatrix2D)D).rows();
			int Erows=((DoubleMatrix2D)E).rows();
			 
			DoubleMatrix2D sqrtD=new DenseDoubleMatrix2D(Dsize,Dsize);
			for(int i=0;i<Dsize;i++)
				sqrtD.set(i,i,Math.sqrt(((DoubleMatrix2D)D).get(i,i)));			 	
			 
			DoubleMatrix2D wm=alg.mult(alg.inverse(sqrtD),alg.transpose((DoubleMatrix2D)E));
	
			matrix[1]=wm.clone();
			 
			DoubleMatrix2D dwm=alg.mult((DoubleMatrix2D)E,sqrtD);
			matrix[2]=dwm.clone(); 
			
			/* Project to the eigenvectors of the covariance matrix.
			 * Whiten the samples and reduce dimension simultaneously
			 */
			if(b_verbose)
				System.out.println("Whitening...");

			int vectColumns=((DoubleMatrix2D)vectors).columns();
			DoubleMatrix2D nv=alg.mult(wm,(DoubleMatrix2D)vectors);
			  
			matrix[0]=nv.clone();  
			  			
		}
		
		catch (ErrParam e){
			e.printStackTrace();
			System.exit(-1);
		}

		return matrix;
	}
	
	
	
	/*=========================================================================
	 *  private boolean defaultValues(LinkedList arguments)
	 *   
	 *=======================================================================*/
	 private static boolean defaultValues(LinkedList arguments)throws ErrParam{
	 	vectors=arguments.getFirst();
		E=arguments.get(1);
		D=arguments.get(2);
	
		boolean bool_verbose;
		if (arguments.size()<4)
			s_verbose="on";
		else
			s_verbose=arguments.get(3);
		
		// check the optimal parameter verbose :
		if((String)s_verbose=="on")
			bool_verbose=true;
		else
			if((String)s_verbose=="off")
				bool_verbose=false;
			else
				throw new ErrParam("Error: Illegal value "+(String)s_verbose+" for parameter: 'verbose'.");
		return bool_verbose;
		
	}
	
	/*=========================================================================
	 *  private void testEigenvalues()
	 *   
	 *=======================================================================*/
	private static void testEigenvalues()throws ErrParam{
		
		for(int i=0;i<((DoubleMatrix2D)D).rows();i++){
			if(((DoubleMatrix2D)D).get(i,i)<0){
				throw new ErrParam("Error: Negative eigenvalues computed from "+
				"the covariance matrix.\nThese are due to rounding errors "+
				"(the correct eigenvalues are probably very small).\nTo "+
				"correct the situation, please reduce the number of dimensions"
				+" in the data");
			}
		}
		
	}
	
	/*=========================================================================
	 *  class ErrArgs extends Exception
	 *   
	 *=======================================================================*/
	static class ErrParam extends Exception {
		ErrParam() {} 
		ErrParam(String msg){
			super(msg); 
		}
	}
}