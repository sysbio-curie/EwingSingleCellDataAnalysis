package vdaoengine.analysis.fastica;


import java.util.*;
import java.lang.*;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;
import cern.colt.matrix.doublealgo.*;


/*
 * 	Exemple:	calculate PCA :
		 	 	LinkedList paramPCAMAT=new LinkedList();
		 	 	paramPCAMAT.add(0,mixedsig);
		 	 	paramPCAMAT.add(1,firstEig);
		 	 	paramPCAMAT.add(2,lastEig);
		 	 	paramPCAMAT.add(3,interactivePCA);
		 	 	paramPCAMAT.add(4,verbose);

		 	 	Object[] resultPCA=pcamat.calculate(paramPCAMAT);


*/

public class pcamat{


	private
			static Object X; 				// Cast : DoubleMatrix2D
			static Object verbose;			// Cast : String
			static Object lastEig;			// Cast : Integer
			static Object firstEig;			// Cast : Integer


	/*=========================================================================
	 *  public static Object[] calculate(LinkedList L)
	 *
	 *=======================================================================*/
	public static Object[] calculate(LinkedList L){

		Object[] EDmatrix=new Object[2];

		try{
			Algebra alg=new Algebra();
			defaultValues(L);
			boolean b_verbose = optionalParameters();

			EDmatrix=calculatePCA(b_verbose);

			int maxLastEig=calculateMaxLastEig((DoubleMatrix2D)EDmatrix[1]);

			DoubleMatrix1D eigenvalues=sortEigenvalues((DoubleMatrix2D)EDmatrix[1]);

			checkDimension(b_verbose,maxLastEig);

			DoubleMatrix1D selectedColumns=dropEig(eigenvalues,(DoubleMatrix2D)EDmatrix[1]);

			checkInfo(b_verbose,selectedColumns,eigenvalues,(DoubleMatrix2D)EDmatrix[1]);

			// Select the columns which correpond to the desired range of eigenvalues :
			EDmatrix[0]=selcol((DoubleMatrix2D)EDmatrix[0],selectedColumns);
			EDmatrix[1]=selcol(alg.transpose(selcol((DoubleMatrix2D)EDmatrix[1],selectedColumns)),selectedColumns);

			// Some more information :
			if(b_verbose){
				double sumAll=0.0;

				for(int i=0;i<eigenvalues.size();i++)
					sumAll=sumAll+eigenvalues.get(i);

				double sumUsed=0.0;

				for(int i=0;i<((DoubleMatrix2D)EDmatrix[1]).rows();i++)
					sumUsed=sumUsed+((DoubleMatrix2D)EDmatrix[1]).get(i,i);

				double retained=(sumUsed/sumAll)*100;
				System.out.println(retained+"% of (non-zero) eigenvalues retained.");
			}

		}
		catch (ErrParam e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrDimensions e){
			e.printStackTrace();
			System.exit(-1);
		}
		return EDmatrix;

	}



	/*=========================================================================
	 *  private static defaultValues(LinkedList L)
	 *
	 *=======================================================================*/
	private static void defaultValues(LinkedList L) throws ErrDimensions{

		if(L.size()==0)
			throw new ErrDimensions("No input data in list !");

		X=L.getFirst();

		int flag=0;
		if(L.size()<5){
			verbose="on";
			if(L.size()<3){
				lastEig= new Integer(((DoubleMatrix2D)X).rows());
				if(L.size()<2){
					firstEig=new Integer(1);
				}
				else flag=3;
			}
			else flag=2;
		}
		else flag=1;

		switch(flag){
			case 1 : verbose=L.get(4);
			case 2 : lastEig=L.get(2);
			case 3 : firstEig=L.get(1);
		}


	}

	/*=========================================================================
	 *  private static boolean optionalParameters()
	 *
	 *=======================================================================*/
	private static boolean optionalParameters() throws ErrParam{
		boolean bool_verbose;

		if(((String)verbose).equalsIgnoreCase("on"))
			bool_verbose=true;
		else
			if(((String)verbose).equalsIgnoreCase("off"))
				bool_verbose=false;
			else
				throw new ErrParam("Illegal value ["+(String)verbose+"] for parameter: verbose");

		int oldDimension = ((DoubleMatrix2D)X).rows();
		
		System.out.println("OldDimension = "+oldDimension);
		
		if((((Integer)lastEig).intValue() <1) | (((Integer)lastEig).intValue()>oldDimension))
			throw new ErrParam("Illegal value ["+(Integer)lastEig+"] for parameter: lastEig");
		if((((Integer)firstEig).intValue()<1) | (((Integer)firstEig).intValue()>((Integer)lastEig).intValue()))
			throw new ErrParam("Illegal value ["+(Integer)firstEig+"] for parameter: firstEig");

		return bool_verbose;

	}


	/*=========================================================================
	 *  private static Object[] calculatePCA(boolean bool_verbose)
	 *
	 *=======================================================================*/
	private static Object[] calculatePCA(boolean bool_verbose){

		Algebra alg = new Algebra();
		//Calculate the covariance matrix :
		if(bool_verbose)
			System.out.println("Calculating covariance...");
		DoubleMatrix2D covarianceMatrix=Statistic.covariance(alg.transpose((DoubleMatrix2D)X));

		//Calculate the eigenvalues and eigenvectors of covariance matrix :
		EigenvalueDecomposition eig = new EigenvalueDecomposition(covarianceMatrix);
		Object[] eigValVect=new Object[2];
		eigValVect[0]=eig.getV();
		eigValVect[1]=eig.getD();

		return eigValVect;

	}

	/*=========================================================================
	 *  private static int calculateMaxLastEig(DoubleMatrix2D D)throws ErrParam
	 *
	 *=======================================================================*/
	private static int calculateMaxLastEig(DoubleMatrix2D D)throws ErrParam{

		int size=D.rows();

		double rankTolerance = Math.pow(10,-7);
		int maxLastEig = 0;
		for(int i=0;i<size;i++)
			if(D.get(i,i)>rankTolerance)
				maxLastEig++;

		if(maxLastEig==0){
			System.out.println("[ Eigenvalues of the covariance matrix are all"+
			" smaller than tolerance "+rankTolerance+". Please make sure that"+
			" your data matrix contains nonzero values. \nIf the values are"+
			" very small, try rescaling the data matrix. ]");
			throw new ErrParam("Unable to continue, aborting");
		}

		return maxLastEig;
	}

	/*=========================================================================
	 *  private static DoubleMatrix1D sortEigenvalues(DoubleMatrix2D D)
	 *  Sort the eigenvalues - decending :
	 *=======================================================================*/
	private static DoubleMatrix1D sortEigenvalues(DoubleMatrix2D D){

		int size=D.rows();
		DoubleMatrix1D eigenvalues=new DenseDoubleMatrix1D(size);
		DoubleMatrix1D sortD=new DenseDoubleMatrix1D(size);
		for(int i=0;i<size;i++)
			sortD.set(i,D.get(i,i));
		sortD=Sorting.quickSort.sort(sortD);
		for(int i=0;i<size;i++)
			eigenvalues.set(i,sortD.get(size-i-1));

		return eigenvalues;
	}


	/*=========================================================================
	 *  private static void checkDimension(boolean bool_verbose,int max)
	 *
	 *=======================================================================*/
	private static void checkDimension(boolean bool_verbose,int max){

		if(((Integer)lastEig).intValue() > max){
			lastEig = new Integer(max);
			int dim=((Integer)lastEig).intValue()-((Integer)firstEig).intValue()+1;
			if(bool_verbose){
				System.out.println("Dimension reduced to ["+dim+"] due to the "+
				"singularity of covariance matrix.");
			}
		}
		else{
			int dim=((Integer)lastEig).intValue()-((Integer)firstEig).intValue()+1;
			// Reduce the dimensionality of the problem :
			if(bool_verbose){
				if(((DoubleMatrix2D)X).rows()==dim)
					System.out.println("Dimension not reduced.");
				else
					System.out.println("Reducing dimension...");
			}
		}

	}

	/*=========================================================================
	 *  private static DoubleMatrix1D dropEig(DoubleMatrix1D vect)
	 *
	 *=======================================================================*/
	private static DoubleMatrix1D dropEig(DoubleMatrix1D vect,DoubleMatrix2D D){
		double lowerLimitValue,higherLimitValue;

		if(((Integer)lastEig).intValue() < ((DoubleMatrix2D)X).rows())
			lowerLimitValue = (vect.get(((Integer)lastEig).intValue()-1)+vect.get(((Integer)lastEig).intValue()))/2.0;
		else
			lowerLimitValue = vect.get(((DoubleMatrix2D)X).rows()-1)-1.0;

		if(((Integer)firstEig).intValue()>1)
			higherLimitValue = (vect.get(((Integer)firstEig).intValue()-2)+vect.get(((Integer)firstEig).intValue()-1))/2.0;
		else
			higherLimitValue = vect.get(0)+1.0;

		int dim=D.rows();
		DoubleMatrix1D lowerColumns = new DenseDoubleMatrix1D(dim);
		DoubleMatrix1D higherColumns = new DenseDoubleMatrix1D(dim);

		for(int i=0;i<dim;i++){
			if(D.get(i,i)>lowerLimitValue)
				lowerColumns.set(i,1);
			if(D.get(i,i)<higherLimitValue)
				higherColumns.set(i,1);
		}

		DoubleMatrix1D selectedColumns = new DenseDoubleMatrix1D(dim);
		for(int i=0;i<dim;i++)
			if(lowerColumns.get(i)==1 & higherColumns.get(i)==1)
				selectedColumns.set(i,1);

		return selectedColumns;

	}


	/*=========================================================================
	 *  private static void checkInfo(boolean bool_verbose,DoubleMatrix1D vect)
	 *
	 *=======================================================================*/
	private static void checkInfo(boolean bool_verbose,DoubleMatrix1D selectedC,DoubleMatrix1D eig,DoubleMatrix2D D) throws ErrDimensions{

		double sum=0.0;
		for(int i=0;i<selectedC.size();i++)
				sum=sum+selectedC.get(i);

		if(bool_verbose)
			System.out.println("Selected ["+sum+"] dimensions.");

		if(sum!=(((Integer)lastEig).intValue()-((Integer)firstEig).intValue()+1))
			throw new ErrDimensions("Selected a wrong number of dimensions.");

		if(bool_verbose){

			double sum_diag=0.0;
			for(int i=0;i<D.rows();i++)
				if(selectedC.get(i)==0)
					sum_diag=sum_diag+D.get(i,i);

			System.out.println("Smallest remaining (non-zero) eigenvalue ["+eig.get(((Integer)lastEig).intValue()-1) +"].");
			System.out.println("Largest remaining (non-zero) eigenvalue ["+eig.get(((Integer)firstEig).intValue()-1)+"].");
			System.out.println("Sum of removed eigenvalues ["+sum_diag+"].");
		}

	}


	/*=========================================================================
	 *  private static DoubleMatrix2D selcol(DoubleMatrix2D oldMatrix,DoubleMatrix1D maskVector)throws ErrDimensions
	 *
	 *=======================================================================*/
	private static DoubleMatrix2D selcol(DoubleMatrix2D oldMatrix,DoubleMatrix1D maskVector)throws ErrDimensions{

		if(maskVector.size()!=oldMatrix.columns())
			throw new ErrDimensions("The mask vector and matrix are of uncompatible size.");

		int numTaken=0;
		int[] temp = new int[maskVector.size()];
		for(int i=0;i<maskVector.size();i++)
			if(maskVector.get(i)==1){
				temp[numTaken]=i;
				numTaken++;
			}
		int[] takingMask=new int[numTaken];
		for(int j=0;j<numTaken;j++)
			takingMask[j]=temp[j];

		DoubleMatrix2D newMatrix=new DenseDoubleMatrix2D(oldMatrix.rows(),numTaken);
		for(int i=0;i<newMatrix.rows();i++)
			for(int j=0;j<newMatrix.columns();j++)
				newMatrix.set(i,j,oldMatrix.get(i,takingMask[j]));

		return newMatrix;

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

	/*=========================================================================
	 *  class ErrDimensions extends Exception
	 *
	 *=======================================================================*/
	static class ErrDimensions extends Exception {
		ErrDimensions() {}
		ErrDimensions(String msg){
			super(msg);
		}
	}
}