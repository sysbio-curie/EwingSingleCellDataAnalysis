package vdaoengine.analysis.fastica;

import java.util.*;
import java.lang.*;
import java.lang.RuntimeException;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;

import vdaoengine.data.*;

/* FastICA for Java
 *
 * fastica resultICA = new fastica(X); estimates the independant components
 * from given multidimensional signals. Each row of matrix X is one observed
 * signal. fastica uses Hyvarinen's fixed-point algorithm. Output can obtain
 * by :
 *		DoubleMatrix2D icasig	= 	resultICA.getIcaSignal();
 *		DoubleMatrix2D A		= 	resultICA.getA();
 *		DoubleMatrix2D W		=	resultICA.getW();
 *
 * The rows of icasig contain the estimated independent components
 * A is the estimated mixing matrix
 * W is the estimated separating matrix
 *
 * Some optional arguments induce other output format, see below.
 *
 * fastica can be construct with numerous optional arguments. You must use
 * LinkedList class java in order to insert it. For example :
 *
 * 		LinkedList arguments = new LinkedList();
 * 		arguments.add(0,"numOfIC");
 * 		arguments.add(1,2);
 *
 *		fastica resultICA = new fastica(X,arguments);
 *
 *
 * OPTIONAL PARAMETERS:
 *
 * Parameter name				Values and description
 * ======================================================================
 * --Basic parameters in fixed-point algorithm:
 *
 *"approach"            (String) The decorrelation approach used. Can be
 *                      symmetric ("symm"), i.e. estimate all the
 *                      independent component in parallel, or
 *                      deflation ("defl"), i.e. estimate independent
 *                      component one-by-one like in projection pursuit.
 *                      Default is "defl".
 *
 *"numOfIC"             (Integer) Number of independent components to
 *                      be estimated. Default equals the dimension of data.
 *
 * ======================================================================
 *--Choosing the nonlinearity:
 *
 *"g"                   (String) Chooses the nonlinearity g used in
 *                      the fixed-point algorithm. Possible values:
 *                      Value of "g":      Nonlinearity used:
 *                      "pow3" (default)   g(u)=u^3
 *                      "tanh"             g(u)=tanh(a1*u)
 *                      "gauss"            g(u)=u*exp(-a2*u^2/2)
 *                      "skew"             g(u)=u^2.
 *
 *"finetune"			(String) Chooses the nonlinearity g used when
 *                      fine-tuning. In addition to same values
 *                       as for "g", the possible value "finetune" is:
 *                      "off"              fine-tuning is disabled.
 *
 *"a1"                  (Double) Parameter a1 used when g="tanh".
 *                      Default is 1.
 *
 *"a2"                  (Double) Parameter a2 used when g="gaus".
 *                      Default is 1.
 *
 *"myy"					(Double) Step size. Default is 1.
 *                      If the value of mu is other than 1, then the
 *                      program will use the stabilized version of the
 *                      algorithm (see also parameter "stabilization").
 *
 *"stabilization"       (Double) Values "on" or "off". Default "off".
 *                      This parameter controls wether the program uses
 *                      the stabilized version of the algorithm or
 *                      not. If the stabilization is on, then the value
 *                      of mu can momentarily be halved if the program
 *                      senses that the algorithm is stuck between two
 *                      points (this is called a stroke). Also if there
 *                      is no convergence before half of the maximum
 *                      number of iterations has been reached then mu
 *                      will be halved for the rest of the rounds.
 *
 * ======================================================================
 *--Controlling convergence:
 *
 *"epsilon"             (Double) Stopping criterion. Default is 0.0001.
 *
 *"maxNumIterations"    (Integer) Maximum number of iterations.
 *                      Default is 1000.
 *
 *"maxFinetune"         (Integer) Maximum number of iterations in
 *                      fine-tuning. Default 100.
 *
 *"sampleSize"          (Double) [0 - 1] Percentage of samples used in
 *                      one iteration. Samples are chosen in random.
 *                      Default is 1 (all samples).
 *
 *"initGuess"           (DoubleMatrix2D) Initial guess for A. Default is random.
 *                      You can now do a "one more" like this:
 *						LinkedList L1 = new LinkedList();
 * 						L1.add(0,"numOfIC");
 * 						L1.add(1,3);
 *                      fastica resultICA = fastica(mix, L1);
 *						DoubleMatrix2D ica1	=	resultICA.getIcaSignal();
 *						DoubleMatrix2D A	=	resultICA.getA();
 *						DoubleMatrix2D W	=	resultICA.getW();
 *
 *						LinkedList L2 = new LinkedList();
 *						L2.add(0,"initGuess");
 *						L2.add(1,A);
 *						L2.add(2,"numOfIC");
 *						L2.add(3,4);
 *                      fastica resultICA2 = fastica(mix, L2);
 *						DoubleMatrix2D ica2	=	resultICA2.getIcaSignal();
 *						DoubleMatrix2D A2	=	resultICA2.getA();
 *						DoubleMatrix2D W2	=	resultICA2.getW();
 *
 *======================================================================
 *--Graphics and text output:
 *
 *"verbose"             (String) Either "on" or "off". Default is
 *                      "on": report progress of algorithm in text format.
 *
 *"displayInterval"     Number of iterations between plots.
 *                      Default is 1 (plot after every iteration).
 *
 *======================================================================
 *--Controlling reduction of dimension and whitening:
 *
 * Reduction of dimension is controlled by "firstEig" and "lastEig".
 *
 *"firstEig"            (Integer) This and "lastEig" specify the range for
 *                      eigenvalues that are retained, "firstEig" is
 *                      the index of largest eigenvalue to be
 *                      retained. Default is 1.
 *
 *"lastEig"             (Integer) This is the index of the last (smallest)
 *                      eigenvalue to be retained. Default equals the
 *                      dimension of data.
 *
 * If you already know the eigenvalue decomposition of the covariance
 * matrix, you can avoid computing it again by giving it with the
 * following options:
 *
 *"pcaE"                (DoubleMatrix2D) Eigenvectors
 *"pcaD"                (DoubleMatrix2D) Eigenvalues
 *
 * If you already know the whitened data, you can give it directly to
 * the algorithm using the following options:
 *
 *"whiteSig"            (DoubleMatrix2D) Whitened signal
 *"whiteMat"            (DoubleMatrix2D) Whitening matrix
 *"dewhiteMat"          (DoubleMatrix2D) dewhitening matrix
 *
 * If values for all the "whiteSig", "whiteSig" and "dewhiteMat" are
 * supplied, they will be used in computing the ICA. PCA and whitening
 * are not performed. Though 'mixedsig' is not used in the main
 * algorithm it still must be entered - some values are still
 * calculated from it.
 *
 * Performing preprocessing only is possible by the option:
 *
 *"only"                (String) Compute only PCA i.e. reduction of
 *                      dimension ("pca") or only PCA plus whitening
 *                      ("white"). Default is "all": do ICA estimation
 *                      as well.  This option changes the output
 *                      format accordingly. For example:
 *
 *						LinkedList arguments = new LinkedList();
 * 						arguments.add(0,"only");
 * 						arguments.add(1,"white");
 *                      fastica resultICA = fastica(X, arguments);
 *						DoubleMatrix2D whitesig	=	resultICA.getWhitesig();
 *						DoubleMatrix2D WM	=	resultICA.getWM();
 *						DoubleMatrix2D DWM	=	resultICA.getDWM();
 *
 *                      returns the whitened signals, the whitening matrix
 *                      (WM) and the dewhitening matrix (DWM). (See also
 *                      WHITENV.) In FastICA the whitening matrix performs
 *                      whitening and the reduction of dimension. Dewhitening
 *                      matrix is the pseudoinverse of whitening matrix.
 *
 *						LinkedList arguments = new LinkedList();
 * 						arguments.add(0,"only");
 * 						arguments.add(1,"pca");
 *						fastica resultICA = fastica(X, arguments);
 *						DoubleMatrix2D E	=	resultICA.getE();
 *						DoubleMatrix2D D	=	resultICA.getD();
 *
 *                      returns the eigenvector (E) and diagonal
 *                      eigenvalue (D) matrices  containing the
 *                      selected subspaces.
 *
 * ======================================================================
 *EXAMPLES
 *
 *				LinkedList arguments = new LinkedList();
 * 				arguments.add(0,"approach");	arguments.add(1,"symm");
 * 				arguments.add(0,"g");	arguments.add(1,"tanh");
 *				fastica resultICA = fastica(X, arguments);
 *
 *				===> Do ICA with tanh nonlinearity and in parallel (like
 *              maximum likelihood estimation for supergaussian data).
 *
 *
 *
 *				LinkedList arguments = new LinkedList();
 * 				arguments.add(0,"lastEig");	arguments.add(1,10);
 * 				arguments.add(0,"numOfIC");	arguments.add(1,3);
 *				fastica resultICA = fastica(X, arguments);
 *
 *              ===>Reduce dimension to 10, and estimate only 3
 *              independent components.
 */



public class fastica{


	private
			int Dim;
			int NumOfSampl;

			Object[] result;

			static Object firstEig;
			static Object lastEig;
			static Object interactivePCA;

			static Object E;
			static Object D;

			static Object whitesig;

			static Object mixedsig;
			static Object whiteningMatrix;
			static Object dewhiteningMatrix;
			static Object verbose;

			static Object approach;
			static Object numOfIC;
			static Object g;
			static Object finetune;
			static Object a1;
			static Object a2;
			static Object myy;
			static Object stabilization;
			static Object epsilon;
			static Object maxNumIterations;
			static Object maxFinetune;
			static Object initState;
			static Object guess;
			static Object sampleSize;
			static Object displayMode;
			static Object displayInterval;

//______________________________________________________________________________

public fastica(DoubleMatrix2D X, LinkedList L){

		try{
			Algebra alg=new Algebra();
			basicRequirement(X);

			Object[] mixed=remmean(X);

			Dim=((DoubleMatrix2D)mixed[0]).rows();
			NumOfSampl=((DoubleMatrix2D)mixed[0]).columns();

			mixedsig=((DoubleMatrix2D)mixed[0]).copy();
			DoubleMatrix1D mixedmean=((DoubleMatrix1D)mixed[1]).copy();

			defaultValues();

			int[] parameter=optionalParameter(L);
			/* parameter[0] : b_verbose
			 * parameter[1] : userNumOfIC
			 * parameter[2] : jumpPCA
			 * parameter[3] : jumpWhitening
			 * parameter[4] : only
			 */

			// Print information about data :
			if(parameter[0]==1){
				System.out.println("Number of signals: "+Dim+".");
				System.out.println("Number of samples: "+NumOfSampl+".");
			}

			// Warning if the data has been entered the wrong way :
			if(Dim>NumOfSampl)
				if(parameter[0]==1){
					System.out.println("Warning: The signal matrix may be oriented in the wrong way.");
					System.out.println("In that case transpose the martix...");
				}


			calculatePCA(parameter);
			DoubleMatrix2D icasig=null;

			if(parameter[4]>1){
				Object[] result_whitening=whitening(parameter);
				whitesig=((DoubleMatrix2D)result_whitening[0]).copy();
                                //for testing
                                VDataTable vt = testFastICA1.convertColtMatrix(alg.transpose((DoubleMatrix2D)whitesig));
                                vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"whitesig.dat");
                                //
				whiteningMatrix=((DoubleMatrix2D)result_whitening[1]).copy();
				dewhiteningMatrix=((DoubleMatrix2D)result_whitening[2]).copy();
			}

			Object A=null,W=null;
			if(parameter[4]>2){
				Object[] result_ica=calculateICA(parameter);
				A=((DoubleMatrix2D)result_ica[0]).copy();
		 		W=((DoubleMatrix2D)result_ica[1]).copy();


				if(W!=null){
					// add the mean back :
					if(parameter[0]==1)
						System.out.println("Adding the mean back to the data.");

					int nrows = ((DoubleMatrix2D)W).rows();
					int ncolumns =((DoubleMatrix2D)W).columns();

					DoubleMatrix2D multWmixedsig = alg.mult((DoubleMatrix2D)W,(DoubleMatrix2D)mixedsig);
					DoubleMatrix1D multMean = alg.mult((DoubleMatrix2D)W,mixedmean);
					DoubleMatrix2D multMeanOnes = new DenseDoubleMatrix2D(nrows,NumOfSampl);
					for(int i=0;i<nrows;i++)
						for(int j=0;j<NumOfSampl;j++)
							multMeanOnes.set(i,j,multMean.get(i));

					icasig=new DenseDoubleMatrix2D(nrows,ncolumns);
					for(int i=0;i<nrows;i++)
						for(int j=0;j<ncolumns;j++)
							icasig.set(i,j,multWmixedsig.get(i,j)+multMeanOnes.get(i,j));


				}
				else
					icasig=null;
			}

			// The output depends on the number of output parameters and the 'only' parameter :
			if(parameter[4]==1){
				// only pca :
				result=new Object[3];
				result[0]=((DoubleMatrix2D)E).copy();
				result[1]=((DoubleMatrix2D)D).copy();
				result[2]=new Integer(1);
			}
			else{
				result=new Object[4];
				if(parameter[4]==2){
					// only pca and whitening :
					result[0]=((DoubleMatrix2D)whitesig).copy();
					result[1]=((DoubleMatrix2D)whiteningMatrix).copy();
					result[2]=((DoubleMatrix2D)dewhiteningMatrix).copy();
					result[3]=new Integer(2);
				}
				else{
					result[0]=icasig.copy();
					result[1]=((DoubleMatrix2D)A).copy();
					result[2]=((DoubleMatrix2D)W).copy();
					result[3]=new Integer(3);
				}
			}



		}
		catch (ErrRequirements e){
			e.printStackTrace();
			System.exit(-1);
			result=null;
		}
	}



public fastica(DoubleMatrix2D X){
		LinkedList L = new LinkedList(); // empty list
		try{
			Algebra alg=new Algebra();
			basicRequirement(X);

			Object[] mixed=remmean(X);

			Dim=((DoubleMatrix2D)mixed[0]).rows();
			NumOfSampl=((DoubleMatrix2D)mixed[0]).columns();

			mixedsig=((DoubleMatrix2D)mixed[0]).copy();
			DoubleMatrix1D mixedmean=((DoubleMatrix1D)mixed[1]).copy();

			defaultValues();

			int[] parameter=optionalParameter(L);
			/* parameter[0] : b_verbose
			 * parameter[1] : userNumOfIC
			 * parameter[2] : jumpPCA
			 * parameter[3] : jumpWhitening
			 * parameter[4] : only
			 */

			// Print information about data :
			if(parameter[0]==1){
				System.out.println("Number of signals: "+Dim+".");
				System.out.println("Number of samples: "+NumOfSampl+".");
			}

			// Warning if the data has been entered the wrong way :
			if(Dim>NumOfSampl)
				if(parameter[0]==1){
					System.out.println("Warning: The signal matrix may be oriented in the wrong way.");
					System.out.println("In that case transpose the martix...");
				}


			calculatePCA(parameter);
			DoubleMatrix2D icasig=null;

			if(parameter[4]>1){
				Object[] result_whitening=whitening(parameter);
				whitesig=((DoubleMatrix2D)result_whitening[0]).copy();
				whiteningMatrix=((DoubleMatrix2D)result_whitening[1]).copy();
				dewhiteningMatrix=((DoubleMatrix2D)result_whitening[2]).copy();
			}

			Object A=null,W=null;
			if(parameter[4]>2){
				Object[] result_ica=calculateICA(parameter);
				A=((DoubleMatrix2D)result_ica[0]).copy();
		 		W=((DoubleMatrix2D)result_ica[1]).copy();

				if(W!=null){
					// add the mean back :
					if(parameter[0]==1)
						System.out.println("Adding the mean back to the data.");

					int nrows = ((DoubleMatrix2D)W).rows();
					int ncolumns =((DoubleMatrix2D)W).columns();

					DoubleMatrix2D multWmixedsig = alg.mult((DoubleMatrix2D)W,(DoubleMatrix2D)mixedsig);
					DoubleMatrix1D multMean = alg.mult((DoubleMatrix2D)W,mixedmean);
					DoubleMatrix2D multMeanOnes = new DenseDoubleMatrix2D(nrows,NumOfSampl);
					for(int i=0;i<nrows;i++)
						for(int j=0;j<NumOfSampl;j++)
							multMeanOnes.set(i,j,multMean.get(i));

					icasig=new DenseDoubleMatrix2D(nrows,ncolumns);
					for(int i=0;i<nrows;i++)
						for(int j=0;j<ncolumns;j++)
							icasig.set(i,j,multWmixedsig.get(i,j)+multMeanOnes.get(i,j));


				}
				else
					icasig=null;
			}

			// The output depends on the number of output parameters and the 'only' parameter :
			if(parameter[4]==1){
				// only pca :
				result=new Object[3];
				result[0]=((DoubleMatrix2D)E).copy();
				result[1]=((DoubleMatrix2D)D).copy();
				result[2]=new Integer(1);
			}
			else{
				result=new Object[4];
				if(parameter[4]==2){
					// only pca and whitening :
					result[0]=((DoubleMatrix2D)whitesig).copy();
					result[1]=((DoubleMatrix2D)whiteningMatrix).copy();
					result[2]=((DoubleMatrix2D)dewhiteningMatrix).copy();
					result[3]=new Integer(2);
				}
				else{
					result[0]=icasig.copy();
					result[1]=((DoubleMatrix2D)A).copy();
					result[2]=((DoubleMatrix2D)W).copy();
					result[3]=new Integer(3);
				}
			}



		}
		catch (ErrRequirements e){
			e.printStackTrace();
			System.exit(-1);
			result=null;
		}

	}



	/*=========================================================================
	 *  public DoubleMatrix2D getIcaSignal()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getIcaSignal(){

		DoubleMatrix2D icaSignal;

		if(result.length==3 & ((Integer)result[3]).intValue()==3)
			icaSignal=((DoubleMatrix2D)result[0]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'all'");
			icaSignal=null;
		}
		return icaSignal;
	}


	/*=========================================================================
	 *  public DoubleMatrix2D getA()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getA(){

		DoubleMatrix2D matrixA;
		if(result.length==4 & ((Integer)result[3]).intValue()==3)
			matrixA=((DoubleMatrix2D)result[1]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'all'");
			matrixA=null;
		}
		return matrixA;
	}


	/*=========================================================================
	 *  public DoubleMatrix2D getW()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getW(){

		DoubleMatrix2D matrixW;
		if(result.length==4 & ((Integer)result[3]).intValue()==3)
			matrixW=((DoubleMatrix2D)result[2]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'all'");
			matrixW=null;
		}
		return matrixW;

	}

	/*=========================================================================
	 *  public DoubleMatrix2D getWhitesig()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getWhitesig(){

		DoubleMatrix2D whitesig;
		if(result.length==4 & ((Integer)result[3]).intValue()==2)
			whitesig=((DoubleMatrix2D)result[0]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'white'");
			whitesig=null;
		}
		return whitesig;

	}

	/*=========================================================================
	 *  public DoubleMatrix2D getWM()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getWM(){

		DoubleMatrix2D WM;
		if(result.length==4 & ((Integer)result[3]).intValue()==2)
			WM=((DoubleMatrix2D)result[1]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'white'");
			WM=null;
		}
		return WM;

	}

	/*=========================================================================
	 *  public DoubleMatrix2D getWhitesig()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getDWM(){

		DoubleMatrix2D DWM;
		if(result.length==4 & ((Integer)result[3]).intValue()==2)
			DWM=((DoubleMatrix2D)result[2]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'white'");
			DWM=null;
		}
		return DWM;

	}

	/*=========================================================================
	 *  public DoubleMatrix2D getE()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getE(){

		DoubleMatrix2D E;
		if(result.length==3 & ((Integer)result[2]).intValue()==1)
			E=((DoubleMatrix2D)result[0]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'pca'");
			E=null;
		}
		return E;

	}

	/*=========================================================================
	 *  public DoubleMatrix2D getD()
	 *
	 *=======================================================================*/
	public DoubleMatrix2D getD(){

		DoubleMatrix2D D;
		if(result.length==3 & ((Integer)result[2]).intValue()==1)
			D=((DoubleMatrix2D)result[1]).copy();
		else{
			System.out.println("Error: wrong parameter 'only': you should use 'pca'");
			D=null;
		}
		return D;

	}


	/*=========================================================================
	 *  public void basicRequirement(DoubleMatrix2D X)throws ErrRequirements
	 *
	 *=======================================================================*/
	public void basicRequirement(DoubleMatrix2D X)throws ErrRequirements{

		for(int i=0;i<X.rows();i++)
			for(int j=0;j<X.columns();j++)
				if((new Double(X.get(i,j))).isNaN())
					throw new ErrRequirements("Input data contains NaN's.");
	}

	/*=========================================================================
	 *  public void defaultValues()
	 *
	 *=======================================================================*/
	public void defaultValues(){

		// All :
		verbose			="on";

		//Default values for pcamat parameters :
		firstEig		=new Integer(1);
		lastEig			=new Integer(Dim);
		interactivePCA	="off";

		//Default values for fpica parameters :
		approach		="defl";
		numOfIC			=new Integer(Dim);
		g				="pow3";
		finetune		="off";
		a1				=new Double(1.0);
		a2				=new Double(1.0);
		myy				=new Double(1.0);
		stabilization	="off";
		epsilon			=new Double(0.0001);
		maxNumIterations=new Integer(1000);
		maxFinetune		=new Integer(5);
		initState		="rand";
		guess			=new DenseDoubleMatrix2D(1,1);
		sampleSize		=new Double(1.0);
		displayMode		="off";
		displayInterval	=new Integer(1);
	}

	/*=========================================================================
	 *  public void optionalParameter(LinkedList L)
	 *
	 *=======================================================================*/
	public int[] optionalParameter(LinkedList L)throws ErrRequirements{
		int[] param=new int[5];

		if(((L.size())%2)==1)
			throw new ErrRequirements("Optional parameters should always go by pairs !");
		else{
			int b_verbose=1;
			int jumpPCA=0;
			int jumpWhitening=0;
			int only=3;
			int userNumOfIC=0;

			boolean flag;

			for(int i=0;i<L.size()-1;i=i+2){
					flag=true;


					//if((L.get(i)).getClass().getName() != "java.lang.String"){
					//	throw new ErrRequirements("Unknown type of optional parameter name(parameter names must be strings).");
					//}

					// change the value of parameter :
					if(((String)L.get(i)).equalsIgnoreCase("stabilization")){
						stabilization=((String)(L.get(i+1))).toLowerCase();
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("maxfinetune")){
						maxFinetune=(Integer)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("samplesize")){
						sampleSize=(Double)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("verbose")){
						verbose=((String)(L.get(i+1))).toLowerCase();
						if((String)verbose=="off")
							b_verbose=0;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("firsteig")){
						firstEig=(Integer)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("lasteig")){
						lastEig=(Integer)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("interactivepca")){
						interactivePCA=((String)(L.get(i+1))).toLowerCase();
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("approach")){
						approach=((String)(L.get(i+1))).toLowerCase();
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("numofic")){
						numOfIC=(Integer)L.get(i+1);
						userNumOfIC=1;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("g")){
						g=((String)(L.get(i+1))).toLowerCase();
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("finetune")){
						finetune=((String)(L.get(i+1))).toLowerCase();
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("a1")){
						a1=(Double)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("a2")){
						a2=(Double)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("myy")){
						myy=(Double)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("epsilon")){
						epsilon=(Double)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("maxnumiterations")){
						maxNumIterations=(Integer)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("initguess")){
						guess=(DoubleMatrix2D)L.get(i+1);
						initState="guess";
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("displaymode")){
						displayMode=((String)(L.get(i+1))).toLowerCase();
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("displayinterval")){
						displayInterval=(Integer)L.get(i+1);
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("pcae")){
						// calculate if there are enought parameters to skip PCA
						E=(DoubleMatrix2D)L.get(i+1);
						jumpPCA++;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("pcad")){
						// calculate if there are enought parameters to skip PCA
						D=(DoubleMatrix2D)L.get(i+1);
						jumpPCA++;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("whitesig")){
						// calculate if there are enought parameters to skip PCA and whitening
						whitesig=(DoubleMatrix2D)L.get(i+1);
						jumpWhitening++;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("whitemat")){
						// calculate if there are enought parameters to skip PCA and whitening
						whiteningMatrix=(DoubleMatrix2D)L.get(i+1);
						jumpWhitening++;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("dewhitemat")){
						// calculate if there are enought parameters to skip PCA and whitening
						dewhiteningMatrix=(DoubleMatrix2D)L.get(i+1);
						jumpWhitening++;
						flag=false;
					}

					if(((String)L.get(i)).equalsIgnoreCase("only")){
						// if the user only wants to calculate PCA or...
						if(((String)L.get(i)).equalsIgnoreCase("pca"))
						only=1;
						if(((String)L.get(i)).equalsIgnoreCase("white"))
						only=2;
						if(((String)L.get(i)).equalsIgnoreCase("all"))
						only=3;
						flag=false;
					}

					if(flag){
						throw new ErrRequirements("Unrecognized parameter in linked list !");
					}

			}// end for

			param[0]=b_verbose;
			param[1]=userNumOfIC;
			param[2]=jumpPCA;
			param[3]=jumpWhitening;
			param[4]=only;

		}
		return param;

	}


	/*=========================================================================
	 *  public void calculatePCA(int[] param)
	 *
	 *=======================================================================*/
	public void calculatePCA(int[] param){

		/* param[0] : b_verbose in cast Integer
		 * param[1] : userNumOfIC
		 * param[2] : jumpPCA
		 * param[3] : jumpWhitening
		 * param[4] : only
		 */



		/* We need the results of PCA for whitening, but if we don't need
		 * to do whitening... the we don't need PCA...
		 */
		 if(param[3]==3){
		 	if(param[0]==1){
		 		System.out.println("Whitened signal and corresponding matrises supplied.");
		 		System.out.println("PCA calculations not needed.");
		 	}
		 }
		 else{
		 	/* OK, so first we need to calculate PCA
		 	 * Check to see if we already have the PCA data.
		 	 */
		 	 if(param[2]==2){
		 	 	if(param[0]==1){
		 	 		System.out.println("Values for PCA calculations supplied.");
		 	 		System.out.println("PCA calculations not needed.");
		 	 	}
		 	 }
		 	 else{
		 	 	if(param[2]>0 & param[0]==1){
		 	 		System.out.println("You must supply all of these in order to jump PCA:");
		 	 		System.out.println("'pcaE', 'pcaD'.");
		 	 	}

		 	 	//calculate PCA :
		 	 	LinkedList paramPCAMAT=new LinkedList();
		 	 	paramPCAMAT.add(0,mixedsig);
		 	 	paramPCAMAT.add(1,firstEig);
		 	 	paramPCAMAT.add(2,lastEig);
		 	 	paramPCAMAT.add(3,interactivePCA);
		 	 	paramPCAMAT.add(4,verbose);

		 	 	Object[] resultPCA=pcamat.calculate(paramPCAMAT);

		 	 	E=((DoubleMatrix2D)resultPCA[0]).copy();
		 	 	D=((DoubleMatrix2D)resultPCA[1]).copy();


		 	 }
		 }


	}

	/*=========================================================================
	 *  public Object[] whitening(int[] param)
	 *
	 *=======================================================================*/
	public Object[] whitening(int[] param){

		/* param[0] : b_verbose in cast Integer
		 * param[1] : userNumOfIC
		 * param[2] : jumpPCA
		 * param[3] : jumpWhitening
		 * param[4] : only
		 */
		Object[] resultWhitening=new Object[3];

		// check to see if the whitening is needed...
		if(param[3]==3){
			if(param[0]==1)
				System.out.println("Whitening not needed.");
		}
		else{
			if(param[3]>0 & param[0]==1){
				System.out.println("You must supply all of these in order to jump whitening:");
				System.out.println("'whiteSig', 'whiteMat', 'dewhiteMat'");
			}

			//Calculate the whitening :
			LinkedList paramWhitenv = new LinkedList();
			paramWhitenv.add(0,mixedsig);
			paramWhitenv.add(1,E);
			paramWhitenv.add(2,D);
			paramWhitenv.add(3,verbose);

			resultWhitening = whitenv.calculate(paramWhitenv);


		}
		return resultWhitening;

	}




	/*=========================================================================
	 *  public Object[] calculateICA(int[] param)
	 *
	 *=======================================================================*/
	public Object[] calculateICA(int[] param){

		/* param[0] : b_verbose in cast Integer
		 * param[1] : userNumOfIC
		 * param[2] : jumpPCA
		 * param[3] : jumpWhitening
		 * param[4] : only
		 */

		Object[] resultFPICA=new Object[2];
		/* Check some parameters
		 * The dimension of the data may have been reduced during PCA calculations.
		 * The original dimension is calculated from the data by default, and
		 * the number of IC is by default set to equal that dimension.
		 */

		 Dim=((DoubleMatrix2D)whitesig).rows();

		 // The number of IC's must be less or equal to the dimension of data :
		 if(((Integer)numOfIC).intValue()>Dim){
		 	numOfIC=new Integer(Dim);
		 	// Show warning only if verbose = 'on' and user supplied a value for 'numOfIC' :
		 	if(param[0]==1 & param[1]==1){
		 		System.out.println("Warning: estimating only "+(Integer)numOfIC+" independent components.");
		 		System.out.println("(Can't estimate more independent components than dimension of data)");
		 	}
		 }

		 // Calculate the ICA with fixed point algorithm :
		 LinkedList paramFPICA=new LinkedList();
		 paramFPICA.add(0,whitesig);
		 paramFPICA.add(1,whiteningMatrix);
		 paramFPICA.add(2,dewhiteningMatrix);
		 paramFPICA.add(3,approach);
		 paramFPICA.add(4,numOfIC);
		 paramFPICA.add(5,g);
		 paramFPICA.add(6,finetune);
		 paramFPICA.add(7,a1);
		 paramFPICA.add(8,a2);
		 paramFPICA.add(9,myy);
		 paramFPICA.add(10,stabilization);
		 paramFPICA.add(11,epsilon);
		 paramFPICA.add(12,maxNumIterations);
		 paramFPICA.add(13,maxFinetune);
		 paramFPICA.add(14,initState);
		 paramFPICA.add(15,guess);
		 paramFPICA.add(16,sampleSize);
		 paramFPICA.add(17,displayMode);
		 paramFPICA.add(18,displayInterval);
		 paramFPICA.add(19,verbose);

		 resultFPICA=fpica.calculate(paramFPICA);

		 return resultFPICA;
	}


	/*=========================================================================
	 *  public Object[] remmean(DoubleMatrix2D A)
	 *
	 *=======================================================================*/
	public Object[] remmean(DoubleMatrix2D A){

		Algebra alg=new Algebra();
		DoubleMatrix2D newA=new DenseDoubleMatrix2D(A.rows(),A.columns());

		DoubleMatrix1D meanValue = new DenseDoubleMatrix1D(A.rows());
		for(int i=0;i<A.rows();i++){

			double sum=0.0;
			for(int j=0;j<A.columns();j++)
				sum=sum+A.get(i,j);
			sum=sum/A.columns();

			meanValue.set(i,sum);
		}

		for(int i=0;i<A.rows();i++)
			for(int j=0;j<A.columns();j++)
				newA.set(i,j,A.get(i,j)-meanValue.get(i));

		Object[] result=new Object[2];
		result[0]=newA.copy();
		result[1]=meanValue.copy();
		return result;

	}



//______________________________________________________________________________

	/**************************************************************************
	 *                           EXCEPTION CLASS
	 *************************************************************************/
	class ErrRequirements extends Exception {
		ErrRequirements() {}
		ErrRequirements(String msg){
			super(msg);
		}
	}

}