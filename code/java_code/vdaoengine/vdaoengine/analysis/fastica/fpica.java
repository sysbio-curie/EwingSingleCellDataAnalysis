package vdaoengine.analysis.fastica;


import java.util.*;
import java.lang.*;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;



/* FPICA - Fixed point ICA. Main algorithm of FASTICA.
		 Calculate the ICA with fixed point algorithm :

Example :	 LinkedList paramFPICA=new LinkedList();
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


 Perform independent component analysis using Hyvarinen's fixed point
 algorithm. Outputs an estimate of the mixing matrix A and its inverse W.

 whitesig                              :the whitened data as row vectors
 whiteningMatrix                       :can be obtained with function whitenv
 dewhiteningMatrix                     :can be obtained with function whitenv
 approach      [ "symm" | "defl" ]     :the approach used (deflation or symmetric)
 numOfIC       [ 0 - Dim of whitesig ] :number of independent components estimated
 g             [ "pow3" | "tanh" |     :the nonlinearity used
                 "gaus" | "skew" ]
 finetune      [same as g + "off"]     :the nonlinearity used in finetuning.
 a1                                    :parameter for tuning 'tanh'
 a2                                    :parameter for tuning 'gaus'
 mu                                    :step size in stabilized algorithm
 stabilization [ "on" | "off" ]        :if mu < 1 then automatically on
 epsilon                               :stopping criterion
 maxNumIterations                      :maximum number of iterations
 maxFinetune                           :maximum number of iteretions for finetuning
 initState     [ "rand" | "guess" ]    :initial guess or random initial state. See below
 guess                                 :initial guess for A. Ignored if initState = 'rand'
 sampleSize    [ 0 - 1 ]               :percentage of the samples used in one iteration
 displayMode   [ "signals" | "basis" | :plot running estimate
                 "filters" | "off" ]
 displayInterval                       :number of iterations we take between plots
 verbose       [ "on" | "off" ]        :report progress in text format

*/

public class fpica{


	private

			static Object X; 					// Cast : DoubleMatrix2D
			static Object whiteningMatrix;  	// Cast : DoubleMatrix2D
			static Object dewhiteningMatrix;	// Cast : DoubleMatrix2D

			static Object approach;				// Cast : String
			static Object numOfIC;				// Cast : Integer
			static Object g;					// Cast : String
			static Object finetune;				// Cast : String
			static Object a1;					// Cast : Float
			static Object a2;					// Cast : Float
			static Object myy;					// Cast : Double
			static Object stabilization;		// Cast : String
			static Object epsilon;				// Cast : Double
			static Object maxNumIterations;		// Cast : Integer
			static Object maxFinetune;			// Cast : Integer
			static Object initState;			// Cast : String
			static Object guess;				// Cast : DoubleMatrix2D
			static Object sampleSize;			// Cast : Double
			static Object displayMode;			// Cast : String
			static Object displayInterval;		// Cast : Integer
			static Object s_verbose;			// Cast : String


			static boolean g_FastICA_interrupt=true;

			static int dim;


	/*=========================================================================
	 *  public static Object[] calculate(LinkedList arguments)
	 *
	 *=======================================================================*/
	public static Object[] calculate(LinkedList arguments){

		Object[] matrix = new Object[2];

		try{



			argTest(arguments);

			//Global variable for stopping the ICA calculations from the GUI
			boolean interruptible = interrupt();

			// Default values :
			defaultValues(arguments);

			// Checking the value for verbose :
			boolean b_verbose = checkVerbose();

			// Cheking the value for approach :
			int approachMode = checkApproach();
			if(b_verbose)
				System.out.println("Used approach ["+(String)approach+"].");

			// Cheking the value for NumOfIC :
			checkNumOfIC();

			// Checking the sampleSize :
			checkSampleSize(b_verbose);

			// Checking the value for nonlinearity
			int[] paramNonLinearity;
			paramNonLinearity = checkNonLinearity(b_verbose);
			/* paramNonLinearity[0]=gOrig, paramNonLinearity[1]=gFine
			 * paramNonLinearity[2]=finetuningEnabled
			 * paramNonLinearity[3]=stabilizationEnabled.
			 */

			// Checking the other parameters :
			double myyOrig = ((Double)myy).doubleValue();
				// When we start fine-tuning we'll set myy = myyK * myy :
			double myyK = 0.01;
				// How many times do we try for convergence until we give up :
			int failureLimit = 5;


			int usedNlinearity = paramNonLinearity[0];
			double stroke = 0;
			boolean notFine = true;
			boolean long_ = false;


			// Checking the value for initial state :
			boolean initialStateMode = checkInitialState(b_verbose);

			// Checking the value for display mode :
			int usedDisplay = checkDisplayMode(b_verbose);

			if(b_verbose)
				System.out.println("Starting ICA calculation...");


			if (approachMode==1){
				//System.out.println("\nEnter in symmetricApproach...");
				matrix=symetricApproach(initialStateMode,interruptible,b_verbose,paramNonLinearity,myyOrig,usedDisplay);
			}
			else
			 	if(approachMode==2){
			 		//System.out.println("\nEnter in deflationApproach...");
					matrix=deflationApproach(initialStateMode,interruptible,b_verbose,paramNonLinearity,myyOrig,usedDisplay,myyK,failureLimit);
				}

			//System.out.println("End ICA calculation...");

		}
		catch (ErrArgs e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrVerbose e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrApproach e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrNumOfIC e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrNonLinearity e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrInitialState e){
			e.printStackTrace();
			System.exit(-1);
		}
		catch (ErrDisplayMode e){
			e.printStackTrace();
			System.exit(-1);
		}
		return matrix;
	}

//______________________________________________________________________________

	/**************************************************************************
	 *                           CHECK CLASS
	 *************************************************************************/

	/*=========================================================================
	 *  private static int interrupt(int[])
	 *  Global variable for stopping the ICA calculations from the GUI
	 *=======================================================================*/
	private static boolean interrupt() {
		boolean stop;
		if (g_FastICA_interrupt)
			stop = false;
		else
			stop = true;
		return	stop;
	}


	/*=========================================================================
	 *  private static void defaultValues(LinkedList arguments)
	 *
	 *=======================================================================*/
	 private static void defaultValues(LinkedList arguments){
	 	X=arguments.getFirst();
		whiteningMatrix=arguments.get(1);
		dewhiteningMatrix=arguments.get(2);

		int flag = 0;
		if (arguments.size()<20) {
			s_verbose="on";
			if (arguments.size()<19) {
				displayInterval=new Integer(1);
				if (arguments.size()<18) {
					displayMode="on";
					if (arguments.size()<17) {
						sampleSize=new Double(1.0);
						if (arguments.size()<16) {
							guess=new DenseDoubleMatrix2D(1,1);
							((DoubleMatrix2D)guess).set(0,0,1.0);
							if (arguments.size()<15) {
								initState="rand";
								if (arguments.size()<14) {
									maxFinetune=new Integer(100);
									if (arguments.size()<13) {
										maxNumIterations=new Integer(1000);
										if (arguments.size()<12) {
											epsilon=new Double(0.0001);
											if (arguments.size()<11) {
												stabilization="on";
												if (arguments.size()<10) {
													myy=new Double(1.0);
				/*  [...]  */
				if (arguments.size()<9) {
					a2=new Integer(1);
					if (arguments.size()<8) {
						a1=new Integer(1);
						if (arguments.size()<7) {
							finetune="off";
							if (arguments.size()<6) {
								g="pow3";
								if (arguments.size()<5) {
									numOfIC=new Integer(((DoubleMatrix2D)X).rows());//dim;
									if (arguments.size()<4) {
										approach="defl";
									}
									else flag=17;
								}
								else flag=16;
							}
							else flag=15;
						}
						else flag=14;
					}
					else flag=13;
				}
				else flag=12;
				/*  [...]  */
												}
												else flag=11;
											}
											else flag=10;
										}
										else flag=9;
									}
									else flag=8;
								}
								else flag=7;
							}
							else flag=6;
						}
						else flag=5;
					}
					else flag=4;
				}
				else flag=3;
			}
			else flag=2;
		}
		else flag=1;

		switch (flag) {
			case 1 : 	s_verbose=arguments.get(19);
			case 2 : 	displayInterval=arguments.get(18);
			case 3 : 	displayMode=arguments.get(17);
			case 4 : 	sampleSize=arguments.get(16);
			case 5 : 	guess=arguments.get(15);
			case 6 : 	initState=arguments.get(14);
			case 7 : 	maxFinetune=arguments.get(13);
			case 8 : 	maxNumIterations=arguments.get(12);
			case 9 : 	epsilon=arguments.get(11);
			case 10 : 	stabilization=arguments.get(10);
			case 11 : 	myy=arguments.get(9);
			case 12 : 	a1=arguments.get(8);
			case 13 : 	a2=arguments.get(7);
			case 14 : 	finetune=arguments.get(6);
			case 15 : 	g=arguments.get(5);
			case 16 : 	numOfIC=arguments.get(4);
			case 17 : 	approach=arguments.get(3);
		}
	 }



	/*=========================================================================
	 *  private static void argTest()
	 *
	 *=======================================================================*/
	private static void argTest(LinkedList L) throws ErrArgs {
		if (L.size() < 3) throw new ErrArgs("Error : not enough arguments !");

	}

	/*=========================================================================
	 *  private static boolean checkVerbose()
	 *  Checking the value for verbose
	 *=======================================================================*/
	private static boolean checkVerbose() throws ErrVerbose {
		boolean bool_verbose;

		if ((String)s_verbose=="on") bool_verbose=true;
		else {
			if ((String)s_verbose=="off") bool_verbose=false;
			else
				throw new ErrVerbose("Error : illegal value for parameter"+
				" verbose");
		}

		return bool_verbose;
	}

	/*=========================================================================
	 *  private static int checkApproach()
	 *  Checking the value for approach
	 *=======================================================================*/
	private static int checkApproach() throws ErrApproach {
		int approachMode;

		if ((String)approach=="symm") approachMode=1;
		else {
			if ((String)approach=="defl") approachMode=2;
			else throw new ErrApproach("Error : illegal value for parameter"+
			" approach");
		}
		return approachMode;
	}

	/*=========================================================================
	 *  private static void checkNumOfIC()
	 *  Checking the value for numOfIC
	 *=======================================================================*/
	private static void checkNumOfIC() throws ErrNumOfIC {
		if (((DoubleMatrix2D)X).rows() < ((Integer)numOfIC).intValue())
			throw new ErrNumOfIC("Error : must have numOfIC <= Dimension");
	}

	/*=========================================================================
	 *  private static void checkSampleSize(boolean bool_verbose)
	 *  Checking the sampleSize
	 *=======================================================================*/
	private static void checkSampleSize(boolean bool_verbose) {
		int numSamples = ((DoubleMatrix2D)X).columns();
		if (((Double)sampleSize).doubleValue() > 1.0) {
			sampleSize=new Double(1.0);
			if (bool_verbose)
				System.out.println("Warning: Setting sampleSize to 1");
		}
		else {
			if (((Double)sampleSize).doubleValue() < 1.0) {
				if (((Double)sampleSize).doubleValue() * numSamples < 1000.0 ){
					sampleSize = new Double(Math.min(1000f/numSamples, 1.0));
					if(bool_verbose) {
						System.out.println("Warning: Setting sampleSize to "
						+ (Double)sampleSize + " ("
						+ Math.floor(((Double)sampleSize).doubleValue() * numSamples) + ").");
					}
				}
			}
		}
		if(bool_verbose && (((Double)sampleSize).doubleValue() <1.0)){
			System.out.println("Using about " + ((Double)sampleSize).doubleValue()*100 +
			" of the samples in random order in every step");
		}


	}

	/*=========================================================================
	 *  private static int[] checkNonLinearity(int)
	 *  Checking the value for nonlinearity
	 *=======================================================================*/
	private static int[] checkNonLinearity(boolean bool_verbose) throws ErrNonLinearity{
		int[] param = new int[4];
		/* param[0]=gOrig, param[1]=gFine, param[2]=finetuningEnabled,
		 * param[3]=stabilizationEnabled.
		 */

		if(((String)g).equalsIgnoreCase("pow3")) param[0]=10;
		else
			if (((String)g).equalsIgnoreCase("tanh")) param[0]=20;
			else
				if(((String)g).equalsIgnoreCase("gaus")| ((String)g).equalsIgnoreCase("gauss")) param[0]=30;
				else
					if(((String)g).equalsIgnoreCase("skew")) param[0]=40;
					else throw new ErrNonLinearity("Error: illegal value " +
					(String)g + " for parameter g.");

		if (((Double)sampleSize).doubleValue() != 1.0) param[0]=param[0]+2;

		if (((Double)myy).doubleValue() != 1.0) param[0]=param[0]+1;
		if (bool_verbose)
			System.out.println("Used nonlinearity [" + (String)g + "].");

		param[2]=1;

		if(((String)finetune).equalsIgnoreCase("pow3")) param[1]=11;
		else
			if(((String)finetune).equalsIgnoreCase("tanh")) param[1]=21;
			else
				if(((String)finetune).equalsIgnoreCase("gaus") | ((String)finetune).equalsIgnoreCase("gauss"))
					param[1]=31;
				else
					if(((String)finetune).equalsIgnoreCase("skew")) param[1]=41;
					else {
						if(((String)finetune).equalsIgnoreCase("off")){
							if(((Double)myy).doubleValue()!=1) param[1]=param[0];
							else param[1]=param[0]+1;
							param[2]=0;
						}
						else throw new ErrNonLinearity("Error: illegal value ["
						+ (String)finetune + "] for parameter finetune.");
					}

		if(bool_verbose && param[2]==1)
			System.out.println("Finetuning enabled (nonlinearity: [" +
			(String)finetune + "] .)");

		if(((String)stabilization).equalsIgnoreCase("on")) param[3]=1;
		else {
			if(((String)stabilization).equalsIgnoreCase("off")){
				if(((Double)myy).doubleValue() != 1) param[3]=1;
				else param[3]=0;
			}
			else
				throw new ErrNonLinearity("Error: illegal value [" +
				(String)stabilization + "] for parameter stabilization.");
		}

		if(bool_verbose && param[3]==1)
			System.out.println("Using stabilized algorithm");

		return param;

	}

	/*=========================================================================
	 *  private static boolean checkInitialState(boolean)
	 *  Checking the value for initial state
	 *=======================================================================*/
	private static boolean checkInitialState(boolean bool_verbose)
		throws ErrInitialState{

		boolean init;
		int sizeGuessC = ((DoubleMatrix2D)guess).columns();


		if((String)initState=="rand") init=false;
		else {
			if((String)initState=="guess"){
				if(((DoubleMatrix2D)guess).rows() !=
				((DoubleMatrix2D)whiteningMatrix).columns()) {
					init=false;
					if(bool_verbose)
						System.out.println("Warning: size of initial guess is"+
						" incorrect. Using random initial guess.");
				}
				else {
					init=true;
					if(sizeGuessC < ((Integer)numOfIC).intValue()) {
						if(bool_verbose)
							System.out.println("Warning: initial guess only"+
							" for first [" + sizeGuessC + "] components. Using"+
							" random initial guess for others.");

						int sizeGuessTmpL=((DoubleMatrix2D)guess).rows();
						int sizeGuessTmpC=((DoubleMatrix2D)guess).columns() +
							((Integer)numOfIC).intValue();
						Object guess_tmp =
							new Double[sizeGuessTmpL][sizeGuessTmpC];
						for(int i=0;i<sizeGuessTmpL;i++){
							for(int j=0;j<sizeGuessC;j++){
								((DoubleMatrix2D)guess_tmp).set(i,j,((DoubleMatrix2D)guess).get(i,j));
							}
							for(int j=sizeGuessC+1;j<sizeGuessTmpC;j++){
								((DoubleMatrix2D)guess_tmp).set(i,j,Math.random()-0.5);
							}
						}
						guess=guess_tmp;
					}
					else
						if(sizeGuessC > ((Integer)numOfIC).intValue()){
							int sizeGuessTmpL=((DoubleMatrix2D)guess).rows();
							int sizeGuessTmpC=((Integer)numOfIC).intValue();
							Object guess_tmp =
								new Double[sizeGuessTmpL][sizeGuessTmpC];
							for(int i=0;i<sizeGuessTmpL;i++)
								for(int j=0;j<sizeGuessTmpC;j++)
									((DoubleMatrix2D)guess_tmp).set(i,j,((DoubleMatrix2D)guess).get(i,j));
							guess=guess_tmp;
						}
					if(bool_verbose)
						System.out.println("Using initial guess");
				}
			}
			else
				throw new ErrInitialState("Error: illegal value ["+
				(String)initState +"] for parameter initState.");
		}
		return init;
	}

	/*=========================================================================
	 *  private static int checkDisplayMode(boolean)
	 *   Checking the value for display mode
	 *=======================================================================*/
	private static int checkDisplayMode(boolean bool_verbose) throws ErrDisplayMode {
		int used;

		if((String)displayMode=="off" | (String)displayMode=="none")
			used=0;
		else
			if((String)displayMode=="on" | (String)displayMode=="signals"){
				used=1;
				if(bool_verbose & (((DoubleMatrix2D)X).columns()>10000))
					System.out.println("Warning: Data vectors are very long."+
					" Plotting may take long time.");
				if(bool_verbose & ((Integer)numOfIC).intValue()>25)
					System.out.println("Warning: There are too many signals"+
					" to plot. Plot may not look good.");
			}
			else
				if((String)displayMode=="basis"){
					used=2;
					if(bool_verbose & ((Integer)numOfIC).intValue()>25)
						System.out.println("Warning: There are too many"+
						" signals to plot. Plot may not look good.");
				}
				else
					if((String)displayMode=="filters"){
						used=3;
						if(bool_verbose & ((DoubleMatrix2D)X).rows()>25)
							System.out.println("Warning: There are too many"+
							" signals to plot. Plot may not look good.");
					}
					else
						throw new ErrDisplayMode("Error: illegal value ["+
						(String)displayMode +"] for parameter displayMode.");
		if (((Integer)displayInterval).intValue()<1)
			displayInterval=new Integer(1);
		return used;
	}


	/*=========================================================================
	 *  private static DoubleMatrix2D symmetricOrthogonalization(DoubleMatrix2D B)
	 *
	 *=======================================================================*/
	private static DoubleMatrix2D symmetricOrthogonalization(DoubleMatrix2D A){

		Algebra alg=new Algebra();
		DoubleMatrix2D Temp;
		Temp=alg.mult(alg.transpose(A),A);
		EigenvalueDecomposition eig=new EigenvalueDecomposition(Temp);

		DoubleMatrix2D D=eig.getD();
		DoubleMatrix2D V=eig.getV();

		for(int i=0;i<Temp.rows();i++)
			D.set(i,i,Math.sqrt(D.get(i,i)));

		Temp=alg.mult(V,alg.mult(D,alg.transpose(V)));
		Temp=alg.inverse(Temp);

		DoubleMatrix2D B = alg.mult(A,Temp);
		return B;
	}



	/*=========================================================================
	 *  private static DoubleMatrix2D switchUsedNLinearity (DoubleMatrix2D B)
	 *
	 *=======================================================================*/
	private static DoubleMatrix2D switchUsedNLinearity (DoubleMatrix2D A,int used)throws ErrNonLinearity{

		int numSamples = ((DoubleMatrix2D)X).columns();
		Algebra alg=new Algebra();
		DoubleMatrix2D B=A.copy();

		switch (used){
		//--------------------- pow3 ---------------------
			case 10 :	{
						DoubleMatrix2D Y=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Gpow3=new DenseDoubleMatrix2D(Y.rows(),Y.columns());

						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								Gpow3.set(i,j,Math.pow(Y.get(i,j),3));

						Gpow3=alg.mult((DoubleMatrix2D)X,Gpow3);

						for(int i=0;i<Gpow3.rows();i++)
							for(int j=0;j<Gpow3.columns();j++)
								Gpow3.set(i,j,Gpow3.get(i,j)/numSamples);

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
							B.set(i,j,Gpow3.get(i,j)-3*B.get(i,j));
						}

						break;

			case 11 :	{
						DoubleMatrix2D Y=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Gpow3=new DenseDoubleMatrix2D(Y.rows(),Y.columns());

						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								Gpow3.set(i,j,Math.pow(Y.get(i,j),3));

						DoubleMatrix2D susBeta=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								susBeta.set(i,j,Y.get(i,j)*Gpow3.get(i,j));

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=susBeta.get(0,j);
							for(int i=1;i<susBeta.rows();i++)
								sum=sum+susBeta.get(i,j);
							Beta.set(j,sum);
						}

						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int i=0;i<D.rows();i++)
							D.set(i,i,1/(Beta.get(i)-3*numSamples));

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),Gpow3);
						for(int i=0;i<Z.rows();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));

						Z=alg.mult(B,alg.mult(Z,D));
						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}

						break;

			case 12 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D Y=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Gpow3=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								Gpow3.set(i,j,Math.pow(Y.get(i,j),3));

						DoubleMatrix2D Z=alg.mult(Xsub,Gpow3);
						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,(Z.get(i,j)/Xsub.columns())-(3*B.get(i,j)));
						}
						break;

			case 13 : 	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D Y=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Gpow3=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								Gpow3.set(i,j,Math.pow(Y.get(i,j),3));

						DoubleMatrix2D susBeta=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								susBeta.set(i,j,Y.get(i,j)*Gpow3.get(i,j));

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=susBeta.get(0,j);
							for(int i=1;i<susBeta.rows();i++)
								sum=sum+susBeta.get(i,j);
							Beta.set(j,sum);
						}

						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int i=0;i<D.rows();i++)
							D.set(i,i,1/(Beta.get(i)-3*Y.rows()));

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),Gpow3);
						for(int i=0;i<Z.rows();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));

						Z=alg.mult(B,alg.mult(Z,D));
						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;

		//--------------------- tanh ---------------------
			case 20 :	{
						DoubleMatrix2D C=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).columns(),B.columns());
						((DoubleMatrix2D)X).zMult(B,C,((Double)a1).doubleValue(),0,true,false);
						DoubleMatrix2D hypTan = tanh(C);

						DoubleMatrix2D E=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),hypTan.columns());
						((DoubleMatrix2D)X).zMult(hypTan,E,1/numSamples,0,false,false);

						DoubleMatrix2D F=new DenseDoubleMatrix2D(B.rows(),hypTan.columns());
						for(int j=0;j<F.columns();j++){
							double sum=1-hypTan.get(0,j)*hypTan.get(0,j);
							for(int k=1;k<hypTan.rows();k++)
								sum=sum+1-hypTan.get(k,j)*hypTan.get(k,j);
							for(int i=0;i<F.rows();i++)
								F.set(i,j,sum*B.get(i,j)*((Double)a1).doubleValue()/numSamples);
						}

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,E.get(i,j)-F.get(i,j));
						}
						break;

			case 21 :	{
						DoubleMatrix2D Y=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Ymult=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								Ymult.set(i,j,((Double)a1).doubleValue()*Y.get(i,j));
						DoubleMatrix2D hypTan = tanh(Ymult);

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=Y.get(0,j)*hypTan.get(0,j);
							for(int k=1;k<Y.rows();k++)
								sum=sum+Y.get(k,j)*hypTan.get(k,j);
							Beta.set(j,sum);
						}

						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int j=0;j<D.size();j++){
							double sum=1-hypTan.get(0,j)*hypTan.get(0,j);
							for(int k=1;k<hypTan.rows();k++)
								sum=sum+1-hypTan.get(k,j)*hypTan.get(k,j);
							D.set(j,j,1/(Beta.get(j)-((Double)a1).doubleValue()*sum));
						}

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),hypTan);
						for(int i=0;i<Z.columns();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));
						Z=alg.mult(B,alg.mult(Z,D));

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;

			case 22 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D C=new DenseDoubleMatrix2D(Xsub.columns(),B.columns());
						((DoubleMatrix2D)X).zMult(B,C,((Double)a1).doubleValue(),0,true,false);
						DoubleMatrix2D hypTan = tanh(C);

						DoubleMatrix2D E=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),hypTan.columns());
						((DoubleMatrix2D)X).zMult(hypTan,E,1/Xsub.columns(),0,false,false);

						DoubleMatrix2D F=new DenseDoubleMatrix2D(B.rows(),hypTan.columns());
						for(int j=0;j<F.columns();j++){
							double sum=1-hypTan.get(0,j)*hypTan.get(0,j);
							for(int k=1;k<hypTan.rows();k++)
								sum=sum+1-hypTan.get(k,j)*hypTan.get(k,j);
							for(int i=0;i<F.rows();i++)
								F.set(i,j,sum*B.get(i,j)*((Double)a1).doubleValue()/Xsub.columns());
						}

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,E.get(i,j)-F.get(i,j));
						}
						break;

			case 23 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D Y=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Ymult=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Y.rows();i++)
							for(int j=0;j<Y.columns();j++)
								Ymult.set(i,j,((Double)a1).doubleValue()*Y.get(i,j));
						DoubleMatrix2D hypTan = tanh(Ymult);

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=Y.get(0,j)*hypTan.get(0,j);
							for(int k=1;k<Y.rows();k++)
								sum=sum+Y.get(k,j)*hypTan.get(k,j);
							Beta.set(j,sum);
						}

						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int j=0;j<D.size();j++){
							double sum=1-hypTan.get(0,j)*hypTan.get(0,j);
							for(int k=1;k<hypTan.rows();k++)
								sum=sum+1-hypTan.get(k,j)*hypTan.get(k,j);
							D.set(j,j,1/(Beta.get(j)-((Double)a1).doubleValue()*sum));
						}

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),hypTan);
						for(int i=0;i<Z.columns();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));
						Z=alg.mult(B,alg.mult(Z,D));

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;

		//--------------------- gauss ---------------------
			case 30 :	{
						DoubleMatrix2D U=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Usquared=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Usquared.rows();i++)
							for(int j=0;j<Usquared.columns();j++)
								Usquared.set(i,j,U.get(i,j)*U.get(i,j));

						DoubleMatrix2D E=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<E.rows();i++)
							for(int j=0;j<E.columns();j++)
								E.set(i,j,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i,j)/2));

						DoubleMatrix2D Gauss=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Gauss.rows();i++)
							for(int j=0;j<Gauss.columns();j++)
								Gauss.set(i,j,U.get(i,j)*E.get(i,j));

						DoubleMatrix2D dGauss=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<dGauss.rows();i++)
							for(int j=0;j<dGauss.columns();j++)
								dGauss.set(i,j,(1-((Double)a2).doubleValue()*Usquared.get(i,j))*E.get(i,j));

						DoubleMatrix2D F=new DenseDoubleMatrix2D(B.rows(),dGauss.columns());
						for(int j=0;j<F.columns();j++){
							double sum=dGauss.get(0,j);
							for(int k=1;k<dGauss.rows();k++)
								sum=sum+dGauss.get(k,j);
							for(int i=0;i<F.rows();i++)
								F.set(i,j,sum*B.get(i,j)/numSamples);
						}
						DoubleMatrix2D H=alg.mult((DoubleMatrix2D)X,Gauss);
						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,H.get(i,j)/numSamples-F.get(i,j));
						}
						break;
			case 31 :	{
						DoubleMatrix2D Y=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Ysquared=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Ysquared.rows();i++)
							for(int j=0;j<Ysquared.columns();j++)
								Ysquared.set(i,j,Y.get(i,j)*Y.get(i,j));

						DoubleMatrix2D E=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<E.rows();i++)
							for(int j=0;j<E.columns();j++)
								E.set(i,j,Math.exp(-((Double)a2).doubleValue()*Ysquared.get(i,j)/2));

						DoubleMatrix2D Gauss=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Gauss.rows();i++)
							for(int j=0;j<Gauss.columns();j++)
								Gauss.set(i,j,Y.get(i,j)*E.get(i,j));

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=Y.get(0,j)*E.get(0,j);
							for(int k=1;k<Y.rows();k++)
								sum=sum+Y.get(k,j)*E.get(k,j);
							Beta.set(j,sum);
						}

						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int j=0;j<Beta.size();j++){
							double sum=1-((Double)a2).doubleValue()*Ysquared.get(0,j)*E.get(0,j);
							for(int k=1;k<Ysquared.rows();k++)
								sum=sum+1-((Double)a2).doubleValue()*Ysquared.get(k,j)*E.get(k,j);
							D.set(j,j,1/(Beta.get(j)-sum));
						}

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),Gauss);
						for(int i=0;i<Z.rows();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));

						Z=alg.mult(B,alg.mult(Z,D));

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;

			case 32 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D U=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Usquared=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Usquared.rows();i++)
							for(int j=0;j<Usquared.columns();j++)
								Usquared.set(i,j,U.get(i,j)*U.get(i,j));

						DoubleMatrix2D E=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<E.rows();i++)
							for(int j=0;j<E.columns();j++)
								E.set(i,j,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i,j)/2));

						DoubleMatrix2D Gauss=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Gauss.rows();i++)
							for(int j=0;j<Gauss.columns();j++)
								Gauss.set(i,j,U.get(i,j)*E.get(i,j));

						DoubleMatrix2D dGauss=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<dGauss.rows();i++)
							for(int j=0;j<dGauss.columns();j++)
								dGauss.set(i,j,(1-((Double)a2).doubleValue()*Usquared.get(i,j))*E.get(i,j));

						DoubleMatrix2D F=new DenseDoubleMatrix2D(B.rows(),dGauss.columns());
						for(int j=0;j<F.columns();j++){
							double sum=dGauss.get(0,j);
							for(int k=1;k<dGauss.rows();k++)
								sum=sum+dGauss.get(k,j);
							for(int i=0;i<F.rows();i++)
								F.set(i,j,sum*B.get(i,j)/Xsub.columns());
						}
						DoubleMatrix2D H=alg.mult(Xsub,Gauss);
						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,H.get(i,j)/Xsub.columns()-F.get(i,j));
						}
						break;

			case 33 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D U=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Usquared=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Usquared.rows();i++)
							for(int j=0;j<Usquared.columns();j++)
								Usquared.set(i,j,U.get(i,j)*U.get(i,j));

						DoubleMatrix2D E=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<E.rows();i++)
							for(int j=0;j<E.columns();j++)
								E.set(i,j,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i,j)/2));

						DoubleMatrix2D Gauss=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Gauss.rows();i++)
							for(int j=0;j<Gauss.columns();j++)
								Gauss.set(i,j,U.get(i,j)*E.get(i,j));

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(U.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=U.get(0,j)*E.get(0,j);
							for(int k=1;k<U.rows();k++)
								sum=sum+U.get(k,j)*E.get(k,j);
							Beta.set(j,sum);
						}

						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int j=0;j<Beta.size();j++){
							double sum=1-((Double)a2).doubleValue()*Usquared.get(0,j)*E.get(0,j);
							for(int k=1;k<Usquared.rows();k++)
								sum=sum+1-((Double)a2).doubleValue()*Usquared.get(k,j)*E.get(k,j);
							D.set(j,j,1/(Beta.get(j)-sum));
						}

						DoubleMatrix2D Z=alg.mult(alg.transpose(U),Gauss);
						for(int i=0;i<Z.rows();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));

						Z=alg.mult(B,alg.mult(Z,D));

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;

		//--------------------- skew ---------------------
			case 40 :	{
						DoubleMatrix2D U=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Usquared=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Usquared.rows();i++)
							for(int j=0;j<Usquared.columns();j++)
								Usquared.set(i,j,U.get(i,j)*U.get(i,j));

						DoubleMatrix2D Z=alg.mult((DoubleMatrix2D)X,Usquared);

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,Z.get(i,j)/numSamples);
						}
						break;

			case 41 :	{
						DoubleMatrix2D Y=alg.mult(alg.transpose((DoubleMatrix2D)X),B);
						DoubleMatrix2D Gskew=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Gskew.rows();i++)
							for(int j=0;j<Gskew.columns();j++)
								Gskew.set(i,j,Y.get(i,j)*Y.get(i,j));

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=Y.get(0,j)*Gskew.get(0,j);
							for(int k=1;k<Y.rows();k++)
								sum=sum+Y.get(k,j)*Gskew.get(k,j);
							Beta.set(j,sum);
						}
						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int i=0;i<D.rows();i++)
							D.set(i,i,1/Beta.get(i));

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),Gskew);
						for(int i=0;i<Z.rows();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));

						Z=alg.mult(B,alg.mult(Z,D));

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;

			case 42 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D U=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Usquared=new DenseDoubleMatrix2D(U.rows(),U.columns());
						for(int i=0;i<Usquared.rows();i++)
							for(int j=0;j<Usquared.columns();j++)
								Usquared.set(i,j,U.get(i,j)*U.get(i,j));

						DoubleMatrix2D Z=alg.mult(Xsub,Usquared);

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,Z.get(i,j)/Xsub.columns());
						}
						break;

			case 43 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix2D Y=alg.mult(alg.transpose(Xsub),B);
						DoubleMatrix2D Gskew=new DenseDoubleMatrix2D(Y.rows(),Y.columns());
						for(int i=0;i<Gskew.rows();i++)
							for(int j=0;j<Gskew.columns();j++)
								Gskew.set(i,j,Y.get(i,j)*Y.get(i,j));

						DoubleMatrix1D Beta=new DenseDoubleMatrix1D(Y.columns());
						for(int j=0;j<Beta.size();j++){
							double sum=Y.get(0,j)*Gskew.get(0,j);
							for(int k=1;k<Y.rows();k++)
								sum=sum+Y.get(k,j)*Gskew.get(k,j);
							Beta.set(j,sum);
						}
						DoubleMatrix2D D=new DenseDoubleMatrix2D(Beta.size(),Beta.size());
						for(int i=0;i<D.rows();i++)
							D.set(i,i,1/Beta.get(i));

						DoubleMatrix2D Z=alg.mult(alg.transpose(Y),Gskew);
						for(int i=0;i<Z.rows();i++)
							Z.set(i,i,Z.get(i,i)-Beta.get(i));

						Z=alg.mult(B,alg.mult(Z,D));

						for(int i=0;i<B.rows();i++)
							for(int j=0;j<B.columns();j++)
								B.set(i,j,B.get(i,j)+((Double)myy).doubleValue()*Z.get(i,j));
						}
						break;
			default : 	throw new ErrNonLinearity("Error: Code for desired nonlinearity not found!");

		}
		return B;

	}


	/*=========================================================================
	 *  private static DoubleMatrix1D switchUsedNLinearity (DoubleMatrix1D V,int used)
	 *
	 *=======================================================================*/
	private static DoubleMatrix1D switchUsedNLinearity (DoubleMatrix1D V,int used)throws ErrNonLinearity{

		int numSamples = ((DoubleMatrix2D)X).columns();
		Algebra alg=new Algebra();
		DoubleMatrix1D W=V.copy();

		switch (used){
		//--------------------- pow3 ---------------------
			case 10 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Ucube=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ucube.size();i++)
							Ucube.set(i,Math.pow(U.get(i),3));

						DoubleMatrix1D Z=alg.mult((DoubleMatrix2D)X,Ucube);
						for(int i=0;i<W.size();i++)
							W.set(i,(Z.get(i)/numSamples)-(3*W.get(i)));
						}
						break;
			case 11 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Ucube=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ucube.size();i++)
							Ucube.set(i,Math.pow(U.get(i),3));

						DoubleMatrix1D Z=alg.mult((DoubleMatrix2D)X,Ucube);
						DoubleMatrix1D EXGpow3=Z.copy();
						for(int i=0;i<EXGpow3.size();i++)
							EXGpow3.set(i,Z.get(i)/numSamples);

						double Beta=W.get(1)*EXGpow3.get(1);
						for(int i=1;i<W.size();i++)
							Beta=Beta+W.get(i)*EXGpow3.get(i);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(EXGpow3.get(i)-Beta*W.get(i))/(3-Beta));
						}
						break;
			case 12 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Ucube=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ucube.size();i++)
							Ucube.set(i,Math.pow(U.get(i),3));

						DoubleMatrix1D Z=alg.mult(Xsub,Ucube);
						for(int i=0;i<W.size();i++)
							W.set(i,(Z.get(i)/Xsub.columns())-(3*W.get(i)));
						}
						break;
			case 13 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Ucube=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ucube.size();i++)
							Ucube.set(i,Math.pow(U.get(i),3));

						DoubleMatrix1D Z=alg.mult(Xsub,Ucube);
						DoubleMatrix1D EXGpow3=Z.copy();
						for(int i=0;i<EXGpow3.size();i++)
							EXGpow3.set(i,Z.get(i)/Xsub.columns());

						double Beta=W.get(1)*EXGpow3.get(1);
						for(int i=1;i<W.size();i++)
							Beta=Beta+W.get(i)*EXGpow3.get(i);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(EXGpow3.get(i)-Beta*W.get(i))/(3-Beta));
						}
						break;

		//--------------------- tanh ---------------------
			case 20 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Ua1=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ua1.size();i++)
							Ua1.set(i,((Double)a1).doubleValue()*U.get(i));

						DoubleMatrix1D hypTan=tanh(Ua1);
						DoubleMatrix1D Y=alg.mult((DoubleMatrix2D)X,hypTan);

						double sum=1-hypTan.get(0)*hypTan.get(0);
						for(int k=1;k<hypTan.size();k++)
							sum=sum+1-hypTan.get(k)*hypTan.get(k);
						for(int i=0;i<W.size();i++)
							W.set(i,(Y.get(i)-((Double)a1).doubleValue()*sum*W.get(i))/numSamples);
						}
						break;
			case 21 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Ua1=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ua1.size();i++)
							Ua1.set(i,((Double)a1).doubleValue()*U.get(i));

						DoubleMatrix1D hypTan=tanh(Ua1);
						DoubleMatrix1D Y=alg.mult((DoubleMatrix2D)X,hypTan);

						double Beta=W.get(0)*Y.get(0);
						for(int i=1;i<W.size();i++)
							Beta=Beta+W.get(i)*Y.get(i);

						double sum=1-hypTan.get(0)*hypTan.get(0);
						for(int k=1;k<hypTan.size();k++)
							sum=sum+1-hypTan.get(k)*hypTan.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(Y.get(i)-Beta*W.get(i))/(((Double)a1).doubleValue()*sum-Beta));
						}
						break;
			case 22 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Ua1=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ua1.size();i++)
							Ua1.set(i,((Double)a1).doubleValue()*U.get(i));

						DoubleMatrix1D hypTan=tanh(Ua1);
						DoubleMatrix1D Y=alg.mult(Xsub,hypTan);

						double sum=1-hypTan.get(0)*hypTan.get(0);
						for(int k=1;k<hypTan.size();k++)
							sum=sum+1-hypTan.get(k)*hypTan.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,(Y.get(i)-((Double)a1).doubleValue()*sum*W.get(i))/Xsub.columns());
						}
						break;
			case 23 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Ua1=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Ua1.size();i++)
							Ua1.set(i,((Double)a1).doubleValue()*U.get(i));

						DoubleMatrix1D hypTan=tanh(Ua1);
						DoubleMatrix1D Y=alg.mult(Xsub,hypTan);

						double Beta=W.get(0)*Y.get(0);
						for(int i=1;i<W.size();i++)
							Beta=Beta+W.get(i)*Y.get(i);

						double sum=1-hypTan.get(0)*hypTan.get(0);
							for(int k=1;k<hypTan.size();k++)
								sum=sum+1-hypTan.get(k)*hypTan.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(Y.get(i)-Beta*W.get(i))/(((Double)a1).doubleValue()*sum-Beta));
						}
						break;

		//--------------------- gauss ---------------------
			case 30 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D E=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<E.size();i++)
							E.set(i,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i)/2));

						DoubleMatrix1D Gauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Gauss.size();i++)
							Gauss.set(i,U.get(i)*E.get(i));

						DoubleMatrix1D dGauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<dGauss.size();i++)
							dGauss.set(i,(1-((Double)a2).doubleValue()*Usquared.get(i))*E.get(i));

						DoubleMatrix1D Y=alg.mult((DoubleMatrix2D)X,Gauss);

						double sum=dGauss.get(0);
						for(int k=1;k<dGauss.size();k++)
							sum=sum+dGauss.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,(Y.get(i)-sum*W.get(i))/numSamples);
						}
						break;

			case 31 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D E=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<E.size();i++)
							E.set(i,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i)/2));

						DoubleMatrix1D Gauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Gauss.size();i++)
							Gauss.set(i,U.get(i)*E.get(i));

						DoubleMatrix1D dGauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<dGauss.size();i++)
							dGauss.set(i,(1-((Double)a2).doubleValue()*Usquared.get(i))*E.get(i));

						double sum=dGauss.get(0);
						for(int k=1;k<dGauss.size();k++)
							sum=sum+dGauss.get(k);

						DoubleMatrix1D Y=alg.mult((DoubleMatrix2D)X,Gauss);

						double Beta=W.get(0)*Y.get(0);
						for(int k=1;k<W.size();k++)
							Beta=Beta+W.get(k)*Y.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(Y.get(i)-Beta*W.get(i))/(sum-Beta));
						}
						break;

			case 32 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D E=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<E.size();i++)
							E.set(i,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i)/2));

						DoubleMatrix1D Gauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Gauss.size();i++)
							Gauss.set(i,U.get(i)*E.get(i));

						DoubleMatrix1D dGauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<dGauss.size();i++)
							dGauss.set(i,(1-((Double)a2).doubleValue()*Usquared.get(i))*E.get(i));

						DoubleMatrix1D Y=alg.mult(Xsub,Gauss);

						double sum=dGauss.get(0);
						for(int k=1;k<dGauss.size();k++)
							sum=sum+dGauss.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,(Y.get(i)-sum*W.get(i))/Xsub.columns());

						}
						break;
			case 33	: 	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D E=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<E.size();i++)
							E.set(i,Math.exp(-((Double)a2).doubleValue()*Usquared.get(i)/2));

						DoubleMatrix1D Gauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Gauss.size();i++)
							Gauss.set(i,U.get(i)*E.get(i));

						DoubleMatrix1D dGauss=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<dGauss.size();i++)
							dGauss.set(i,(1-((Double)a2).doubleValue()*Usquared.get(i))*E.get(i));

						double sum=dGauss.get(0);
						for(int k=1;k<dGauss.size();k++)
							sum=sum+dGauss.get(k);

						DoubleMatrix1D Y=alg.mult(Xsub,Gauss);

						double Beta=W.get(0)*Y.get(0);
						for(int k=1;k<W.size();k++)
							Beta=Beta+W.get(k)*Y.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(Y.get(i)-Beta*W.get(i))/(sum-Beta));
						}
						break;

		//--------------------- skew ---------------------
			case 40 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D Z=alg.mult((DoubleMatrix2D)X,Usquared);
						for(int i=0;i<W.size();i++)
							W.set(i,Z.get(i)/numSamples);
						}
						break;
			case 41 :	{
						DoubleMatrix1D U=alg.mult(alg.transpose((DoubleMatrix2D)X),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D Z=alg.mult((DoubleMatrix2D)X,Usquared);

						DoubleMatrix1D EXGskew=new DenseDoubleMatrix1D(W.size());
						for(int i=0;i<EXGskew.size();i++)
							EXGskew.set(i,Z.get(i)/numSamples);

						double Beta=W.get(0)*EXGskew.get(0);
						for(int k=1;k<W.size();k++)
							Beta=Beta+W.get(k)*EXGskew.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(EXGskew.get(i)-Beta*W.get(i))/(-Beta));
						}
						break;
			case 42 : 	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D Z=alg.mult(Xsub,Usquared);
						for(int i=0;i<W.size();i++)
							W.set(i,Z.get(i)/Xsub.columns());
						}
						break;
			case 43 :	{
						int[] vect=getSamples(numSamples,((Double)sampleSize).doubleValue());
						DoubleMatrix2D Xsub=new DenseDoubleMatrix2D(((DoubleMatrix2D)X).rows(),vect.length);
						for(int i=0;i<Xsub.rows();i++)
							for(int j=0;j<Xsub.columns();j++)
								Xsub.set(i,j,((DoubleMatrix2D)X).get(i,vect[j]));

						DoubleMatrix1D U=alg.mult(alg.transpose(Xsub),W);
						DoubleMatrix1D Usquared=new DenseDoubleMatrix1D(U.size());
						for(int i=0;i<Usquared.size();i++)
							Usquared.set(i,Math.pow(U.get(i),2));

						DoubleMatrix1D Z=alg.mult(Xsub,Usquared);

						DoubleMatrix1D EXGskew=new DenseDoubleMatrix1D(W.size());
						for(int i=0;i<EXGskew.size();i++)
							EXGskew.set(i,Z.get(i)/Xsub.columns());

						double Beta=W.get(0)*EXGskew.get(0);
						for(int k=1;k<W.size();k++)
							Beta=Beta+W.get(k)*EXGskew.get(k);

						for(int i=0;i<W.size();i++)
							W.set(i,W.get(i)-((Double)myy).doubleValue()*(EXGskew.get(i)-Beta*W.get(i))/(-Beta));
						}
						break;
			default :	throw new ErrNonLinearity("Error: Code for desired nonlinearity not found!");

		}// end switch
		return W;

	}// end function



	/*=========================================================================
	 *  private static DoubleMatrix1D getSamples(int max ,double val)
	 *
	 *=======================================================================*/
	private static int[] getSamples(int max,double val){

		LinkedList list = new LinkedList();
		ListIterator iter = list.listIterator();

		double random;
		for(int k=0;k<max;k++){
			random=Math.random();
			if(random<val)
				iter.add(new Integer(k));
		}

		int[] vect = new int[list.size()];
		for(int k=0;k<vect.length;k++)
			vect[k]=((Integer)list.get(k)).intValue();

		return vect;
	}

	/*=========================================================================
	 *  private static DoubleMatrix2D tanh(DoubleMatrix2D x)
	 *
	 *=======================================================================*/
	private static DoubleMatrix2D tanh(DoubleMatrix2D x){
		DoubleMatrix2D y = new DenseDoubleMatrix2D(x.rows(),x.columns());
		for(int i=0;i<x.rows();i++)
			for(int j=0;j<x.columns();j++)
				y.set(i,j,1-(2/(Math.exp(2*x.get(i,j))+1)));
		return y;
	}

	/*=========================================================================
	 *  public DoubleMatrix1D tanh(DoubleMatrix1D x)
	 *
	 *=======================================================================*/
	private static DoubleMatrix1D tanh(DoubleMatrix1D x){
		DoubleMatrix1D y = new DenseDoubleMatrix1D(x.size());
		for(int i=0;i<x.size();i++)
				y.set(i,1-(2/(Math.exp(2*x.get(i))+1)));
		return y;
	}

//______________________________________________________________________________

	/**************************************************************************
	 *                           SYMETRIC APPROACH
	 *************************************************************************/

	/*=========================================================================
	 *  private static void symetricApproach(boolean initMode,boolean interruptible,
	 *  boolean bool_verbose,int[] paramNonLinearity,double myyOrig,
	 *  int usedDisplay)
	 *
	 *=======================================================================*/

	private static Object[] symetricApproach(boolean initMode,boolean interruptible,
	boolean bool_verbose,int[] paramNonLinearity,double myyOrig,int usedDisplay){
		Object[] AWmatrix=new Object[2];
		try{


			int numberOfLines = ((DoubleMatrix2D)X).rows();
			DoubleMatrix2D B = new DenseDoubleMatrix2D(numberOfLines,((Integer)numOfIC).intValue());
			Algebra alg = new Algebra();

			DoubleMatrix2D W=new DenseDoubleMatrix2D(B.rows(),B.columns());
			DoubleMatrix2D A=new DenseDoubleMatrix2D(B.rows(),B.columns());

			int usedNLinearity = paramNonLinearity[0];
			double stroke = 0;
			boolean notFine = true;
			boolean long_ = false;


			if(!initMode){
				// Take randoom orthonormal initial vectors :
				for(int i=0;i<numberOfLines;i++)
					for(int j=0;j<((Integer)numOfIC).intValue();j++)
						B.set(i,j,Math.random());

				B=orthMatrix(B);
			}
			else
				// Use the given initial vector as the initial state :
				B=alg.mult((DoubleMatrix2D)whiteningMatrix,(DoubleMatrix2D)guess);

			DoubleMatrix2D B_old = new DenseDoubleMatrix2D(B.rows(),B.columns());
			DoubleMatrix2D B_old2 = new DenseDoubleMatrix2D(B.rows(),B.columns());

			// This is the actual fixed-point iteration loop :
			for(int round=0;round<((Integer)maxNumIterations).intValue()+1;round++){
				if(round==((Integer)maxNumIterations).intValue()){

					System.out.println("No convergence after [" +
					maxNumIterations +"] steps. \nNote that the plots are " +
					"probably wrong.");

					// Symmetric orthogonalization :
					B=symmetricOrthogonalization(B);

					W=alg.mult(alg.transpose(B),(DoubleMatrix2D)whiteningMatrix);
					A=alg.mult((DoubleMatrix2D)dewhiteningMatrix,B);

					break;

				}
				if(interruptible & g_FastICA_interrupt){
					if(bool_verbose)
						System.out.println("Calculation interrupted by the user");

					W=alg.mult(alg.transpose(B),(DoubleMatrix2D)whiteningMatrix);
					A=alg.mult((DoubleMatrix2D)dewhiteningMatrix,B);

					break;
				}

				// Symmetric orthogonalization :
				B=symmetricOrthogonalization(B);

				// Test for termination condition. Note that we consider opposite directions here as well :
				DoubleMatrix2D TempCos=alg.mult(alg.transpose(B),B_old);
				DoubleMatrix2D TempCos2=alg.mult(alg.transpose(B),B_old2);

				double minAbsCos=Math.abs(TempCos.get(0,0)),
					minAbsCos2=Math.abs(TempCos2.get(0,0));

				for(int k=1;k<TempCos.columns();k++){
					if(Math.abs(TempCos.get(k,k))<minAbsCos)
						minAbsCos=Math.abs(TempCos.get(k,k));
					if(Math.abs(TempCos2.get(k,k))<minAbsCos2)
						minAbsCos2=Math.abs(TempCos2.get(k,k));
				}

				if(1-minAbsCos<((Double)epsilon).intValue()){
					if(paramNonLinearity[2]==1 & notFine){
						if(bool_verbose)
							System.out.println("Initial convergence, fine-tuning: ");
						notFine=false;
						usedNLinearity=paramNonLinearity[1]; //gFine
						myy = new Double(((Double)myy).doubleValue() * myyOrig);

						B_old=B_old.assign(0.0);
						B_old2=B_old2.assign(0.0);
					}
					else{
						if(bool_verbose)
							System.out.println("Convergence after ["+round+"] steps.");
						// Calculate the de-whitened vectors :
						A=alg.mult((DoubleMatrix2D)dewhiteningMatrix,B);
						break;
					}
				}
				else
					if(paramNonLinearity[3]==1){ // stabilizationEnabled
						if((stroke==0.0) & (1-minAbsCos2<((Double)epsilon).doubleValue())){
							if(bool_verbose)
								System.out.println("Stroke !");
							stroke=((Double)myy).doubleValue();
							myy=new Double(0.5*((Double)myy).doubleValue());
							if((usedNLinearity%2)==0)
								usedNLinearity++;
						}
					}
					else
						if(stroke!=0.0){
							myy=new Double(stroke);
							stroke=0.0;
							if(((Double)myy).doubleValue()==1.0 & (usedNLinearity%2)!=0)
								usedNLinearity--;
						}
						else
							if((!long_) & (round>((Integer)maxNumIterations).doubleValue()/2)){
								if(bool_verbose)
									System.out.println("Taking long (reducing step size)");
								long_=true;
								myy=new Double(0.5*((Double)myy).doubleValue());
								if((usedNLinearity%2)==0)
									usedNLinearity++;
							}

				B_old2=B_old.copy();
				B_old=B.copy();

				// Show the process :
				if(bool_verbose)
					if(round==1)
						System.out.println("Step no. "+round+".");
					else
						System.out.println("Step no. "+round+", change in value of estimate: "+ (1-minAbsCos) +".");

//*********** ===>// Plot the current state... TO DO switch usedDisplay

				B=switchUsedNLinearity(B,usedNLinearity);


			}// end of "for(int round=0;round<(Integer)maxNumIterations+1;round++)"


			//Calculate ICA filters :
			W=alg.mult(alg.transpose(B),(DoubleMatrix2D)whiteningMatrix);


//*********** ===>// Plot the last one... TO DO

			AWmatrix[0]= new DenseDoubleMatrix2D(A.rows(),A.columns());
	 		AWmatrix[1]= new DenseDoubleMatrix2D(W.rows(),W.columns());

	 		for(int i=0;i<A.rows();i++)
	 			for(int j=0;j<A.columns();j++)
	 				((DoubleMatrix2D)AWmatrix[0]).set(i,j,A.get(i,j));

	 		for(int i=0;i<W.rows();i++)
	 			for(int j=0;j<W.columns();j++)
	 				((DoubleMatrix2D)AWmatrix[1]).set(i,j,W.get(i,j));


		}
		catch (ErrNonLinearity e){
			e.printStackTrace();
			System.exit(-1);
		}
		return AWmatrix;

	}

	/**************************************************************************
	 *                           DEFLATION APPROACH
	 *************************************************************************/

	/*=========================================================================
	 *  private static Object[] deflationApproach(boolean initMode,int usedDisplay,
	 boolean interruptible,boolean bool_verbose,int[] paramNonLinearity,
	 double myyOrig,double myyK,int failureLimit)
	 *
	 *=======================================================================*/
	 private static Object[] deflationApproach(boolean initMode,
	 boolean interruptible,boolean bool_verbose,int[] paramNonLinearity,
	 double myyOrig,int usedDisplay,double myyK,int failureLimit){

	 	Object[] AWmatrix=new Object[2];
	  	try{

	 		Algebra alg=new Algebra();
	 		int numberOfRows = ((DoubleMatrix2D)X).rows();
	 		int numberOfColumns = ((DoubleMatrix2D)X).columns();
	 		DoubleMatrix2D B = new DenseDoubleMatrix2D(numberOfRows,numberOfRows);

	 		DoubleMatrix2D W=new DenseDoubleMatrix2D(Math.min(((Integer)numOfIC).intValue(),numberOfRows),numberOfColumns);
			DoubleMatrix2D A=new DenseDoubleMatrix2D(numberOfColumns,Math.min(((Integer)numOfIC).intValue(),numberOfRows));


	 		// The search for a basis vector is repeated numOfIC times.
	 		int round=1;
	 		int numFailures=0;

	 		while(round<=((Integer)numOfIC).intValue()){
	 			myy=new Double(myyOrig);
		 		int usedNLinearity=paramNonLinearity[0]; //gOrig
		 		double stroke=0.0;
	 			boolean notFine=true;
	 			boolean long_=false;
		 		int endFinetuning=0;

		 		// Show the process...
	 			if(bool_verbose)
	 				System.out.print("IC "+round);

		 		/* Take a random initial vector of length 1 and orthogonalize
		 		 * it with respect to the other vectors :
		 		 */
		 		 DoubleMatrix1D w=new DenseDoubleMatrix1D(numberOfRows);
		 		 if(!initMode)
		 		 	for(int k=0;k<numberOfRows;k++)
		 		 		w.set(k,Math.random());
		 		 else{
		 		 	DoubleMatrix1D temp = ((DoubleMatrix2D)guess).viewColumn(round);
		 		 	w=alg.mult((DoubleMatrix2D)whiteningMatrix,temp);
		 		 }

		 		 DoubleMatrix1D temp2=alg.mult(alg.mult(B,alg.transpose(B)),w);
		 		 for(int k=0;k<w.size();k++)
		 		 	w.set(k,w.get(k)-temp2.get(k));
		 		 double norm=norm2(w);
		 		 for(int k=0;k<w.size();k++)
		 		 	w.set(k,w.get(k)/norm);


		 		 DoubleMatrix1D w_old=new DenseDoubleMatrix1D(w.size());
		 		 DoubleMatrix1D w_old2=new DenseDoubleMatrix1D(w.size());


		 		 // This is the actual fixed-point iteration loop :
		 		 int i=1,gabba=1;
		 		 while(i<=((Integer)maxNumIterations).intValue()+gabba){
		 		 	/* if(usedDisplay>0)
		 		 		drawnow;
		 		 	*/

		 		 	if(interruptible & g_FastICA_interrupt){
		 		 		if(bool_verbose)
		 		 			System.out.println("Calculation interrupted by the user.");
		 		 		break; // return
		 		 	}

		 		 	/* Project the vector into the space orthogonal to the space
		 		 	 * spanned by the earlier found basis vectors. Note that we can
		 		 	 * do the projection with matrix B, since the zero entries do
		 		 	 * not contribute to the projection.
		 		 	 */
		 		 	temp2=alg.mult(alg.mult(B,alg.transpose(B)),w);
		 		 	for(int k=0;k<w.size();k++)
		 		 		w.set(k,w.get(k)-temp2.get(k));
		 		 	norm=norm2(w);
		 		 	for(int k=0;k<w.size();k++)
		 		 		w.set(k,w.get(k)/norm);


		 		 	if(notFine){
		 		 		if(i==((Integer)maxNumIterations).intValue()+1){
		 		 			if(bool_verbose)
		 		 				System.out.println("Component number ["+round+"] did not converge in "+(Integer)maxNumIterations+" iterations.");
		 		 			round--;
		 		 			numFailures++;
		 		 			if(numFailures>failureLimit){
		 		 				if(bool_verbose)
		 		 					System.out.println("Too many failures to converge ("+numFailures+"). Giving up");
		 		 				if(round==0){
		 		 					A=null;
		 		 					W=null;
		 		 				}
		 		 				break; // return
		 		 			}
		 		 			break;
		 		 		}
		 		 	}
		 		 	else{
		 		 		if(i>=endFinetuning){
		 		 			w_old=w.copy(); // So the algorithm will stop on the next test...
		 		 		}
		 		 	}

		 		 	// Show the progress...
		 		 	if(bool_verbose)
		 		 		System.out.print(".");

		 		 	/* Test for termination condition. Note that the algorithm has
		 		 	 * converged if the direction of w and w_old is the same, this
		 		 	 * is why we test the two cases.
		 		 	 */
		 		 	 DoubleMatrix1D temp_plus=new DenseDoubleMatrix1D(w.size());
		 		 	 DoubleMatrix1D temp_less=new DenseDoubleMatrix1D(w.size());
		 		 	 for(int k=0;k<w.size();k++){
		 		 	 	temp_less.set(k,w.get(k)-w_old.get(k));
		 		 	 	temp_plus.set(k,w.get(k)+w_old.get(k));
		 		 	 }

		 		 	 if(alg.norm2(temp_less)<((Double)epsilon).doubleValue() | alg.norm2(temp_plus)<((Double)epsilon).doubleValue()){

		 		 	 	if(paramNonLinearity[2]==1 & notFine){//finetuningEnabled
		 		 	 		if(bool_verbose)
		 		 	 			System.out.println("Initial convergence, fine-tuning: ");
		 		 	 		notFine=false;
		 		 	 		gabba=((Integer)maxFinetune).intValue();
		 		 	 		w_old.assign(0.0);
		 		 	 		w_old2.assign(0.0);
		 		 	 		usedNLinearity=paramNonLinearity[1];//gFine
		 		 	 		myy=new Double(myyK*myyOrig);
		 		 	 		endFinetuning=((Integer)maxFinetune).intValue() +i;
		 		 	 	}
		 		 	 	else{
		 		 	 		 numFailures =0;
		 		 	 		 // Save the vector :
		 		 	 		 for(int k=0;k<B.rows();k++)
		 		 	 		 	B.set(k,round-1,w.get(k));

		 		 	 		 // Calculate the de-whitened vector :
		 		 	 		 DoubleMatrix1D tempMult=alg.mult((DoubleMatrix2D)dewhiteningMatrix,w);
		 		 	 		 for(int k=0;k<A.rows();k++)
		 		 	 		 	A.set(k,round-1,tempMult.get(k));

		 		 	 		 // Calculate ICA filter :
		 		 	 		 for(int k=0;k<W.columns();k++){
		 		 	 		 	double sum=w.get(0)*((DoubleMatrix2D)whiteningMatrix).get(0,k);
		 		 	 		 	for(int p=1;p<w.size();p++)
		 		 	 		 		sum=sum+w.get(p)*((DoubleMatrix2D)whiteningMatrix).get(p,k);
		 		 	 		 	W.set(round-1,k,sum);
		 		 	 		 }

		 		 	 		 // Show the progress :
		 		 	 		 if(bool_verbose)
		 		 	 		 	System.out.println("computed ("+i+" steps)");

//*********** ===>// Also plot the current state... TO DO
	 		 	 		 //switch usedDisplay
	 		 	 		 // ....

	 		 	 		 break; // IC ready - next...
		 		 	 	}

	 			 	 }
		 		 	 else
		 		 	 	if(paramNonLinearity[3]==0){ // stabilizationEnabled
		 		 	 		for(int k=0;k<w.size();k++){
		 		 	 			temp_less.set(k,w.get(k)-w_old2.get(k));
		 		 	 			temp_plus.set(k,w.get(k)+w_old2.get(k));
		 		 	 		}
		 		 	 		if((stroke==0.0) & (alg.norm2(temp_less)<((Double)epsilon).doubleValue() | alg.norm2(temp_plus)<((Double)epsilon).doubleValue())){
		 		 	 			stroke=((Double)myy).doubleValue();
		 		 	 			if(bool_verbose)
		 		 	 				System.out.println("Stroke!");
		 		 	 			myy=new Double(0.5*((Double)myy).doubleValue());
		 		 	 			if((usedNLinearity%2)==0)
		 		 	 				usedNLinearity++;
		 		 	 		}
		 		 	 		else
		 		 	 			if(stroke!=0.0){
		 		 	 				myy=new Double(stroke);
		 		 	 				stroke=0.0;
		 		 	 				if(((Double)myy).doubleValue()==1.0 & (usedNLinearity%2)!=0)
		 		 	 					usedNLinearity--;
		 		 	 			}
		 		 	 			else
		 		 	 				if(notFine & !long_ & i>((Integer)maxNumIterations).intValue()/2){
		 		 	 					if(bool_verbose)
		 		 	 						System.out.println("Taking long (reducing step size).");
		 		 	 					long_ =true;
		 		 	 					myy=new Double(0.5*((Double)myy).doubleValue());
		 		 	 					if((usedNLinearity%2)==0)
		 		 	 						usedNLinearity++;
		 		 	 				}

		 		 	 	}

		 		 	 w_old2=w_old.copy();
	 			 	 w_old=w.copy();


	 			 	 w=switchUsedNLinearity(w,usedNLinearity);

	 			 	 // Normalize the new w :
	 			 	 norm =norm2(w);
	 			 	 for(int p=0;p<w.size();p++)
	 			 	 	w.set(p,w.get(p)/norm);

	 			 	 i++;

	 			 	}//end while

	 				round++;

	 			}//end while

	 			if(bool_verbose)
	 				System.out.println("Done.");

//*********** ===>// Also plot the the ones that not have been plotted... TO DO
	 		 	 		 //switch usedDisplay
	 		 	 		 // ....

	 		AWmatrix[0]= new DenseDoubleMatrix2D(A.rows(),A.columns());
	 		AWmatrix[1]= new DenseDoubleMatrix2D(W.rows(),W.columns());

	 		for(int i=0;i<A.rows();i++)
	 			for(int j=0;j<A.columns();j++)
	 				((DoubleMatrix2D)AWmatrix[0]).set(i,j,A.get(i,j));

	 		for(int i=0;i<W.rows();i++)
	 			for(int j=0;j<W.columns();j++)
	 				((DoubleMatrix2D)AWmatrix[1]).set(i,j,W.get(i,j));


	 		}// end try
	 		catch (ErrNonLinearity e){
				e.printStackTrace();
				System.exit(-1);
			}

	 		return AWmatrix;
	 }// end function












//_____________________________________________________________________________

	/*=========================================================================
	 *  public staticDoubleMatrix2D orthMatrix(DoubleMatrix2D A)
	 *
	 *=======================================================================*/
	public static DoubleMatrix2D orthMatrix(DoubleMatrix2D A){

		DoubleMatrix2D Q = new DenseDoubleMatrix2D(A.rows(),A.columns());

		// calcul of first vector :
		double norm=A.get(0,0)*A.get(0,0);
		for(int k=1;k<A.rows();k++)
			norm=norm+(A.get(k,0)*A.get(k,0));
		norm=Math.sqrt(norm);

		for(int i=0;i<A.rows();i++)
			Q.set(i,0,(1/norm)*A.get(i,0));

		// calcul of remaining vectors :

		for(int j=1;j<A.columns();j++){
			double[] r = new double[j];

			for(int k=0;k<j;k++){
				r[k]=A.get(0,j)*Q.get(0,k);

				for(int l=1;l<A.rows();l++)
					r[k]=r[k]+(A.get(l,j)*Q.get(l,k));
			}


			double[] v = new double[A.rows()];
			for(int i=0;i<A.rows();i++){
				double cpt=r[0]*Q.get(i,0);
				for(int k=1;k<j;k++)
					cpt=cpt+(r[k]*Q.get(i,k));
				v[i]=A.get(i,j)-cpt;
			}


			norm=v[0]*v[0];
			for(int i=1;i<A.rows();i++)
				norm=norm+(v[i]*v[i]);
			norm=Math.sqrt(norm);

			for(int i=0;i<A.rows();i++){
				Q.set(i,j,(1/norm)*v[i]);
			}
		}

		return Q;
	}


	/*=========================================================================
	 *  public static double norm2(DoubleMatrix1D v)
	 *
	 *=======================================================================*/
	public static double norm2(DoubleMatrix1D v){

		double val=0.0;
		for(int k=0;k<v.size();k++)
			val=val+Math.pow(Math.abs(v.get(k)),2);
		val=Math.sqrt(val);
		return val;
	}






//______________________________________________________________________________

	/**************************************************************************
	 *                           EXCEPTION CLASS
	 *************************************************************************/
	static class ErrArgs extends Exception {
		ErrArgs() {}
		ErrArgs(String msg){
			super(msg);
		}
	}

	static class ErrVerbose extends Exception {
		ErrVerbose() {}
		ErrVerbose(String msg){
			super(msg);
		}
	}

	static class ErrApproach extends Exception {
		ErrApproach() {}
		ErrApproach(String msg){
			super(msg);
		}
	}

	static class ErrNumOfIC extends Exception {
		ErrNumOfIC() {}
		ErrNumOfIC(String msg){
			super(msg);
		}
	}

	static class ErrNonLinearity extends Exception {
		ErrNonLinearity() {}
		ErrNonLinearity(String msg){
			super(msg);
		}
	}

	static class ErrInitialState extends Exception {
		ErrInitialState() {}
		ErrInitialState(String msg){
			super(msg);
		}
	}

	static class ErrDisplayMode extends Exception {
		ErrDisplayMode() {}
		ErrDisplayMode(String msg){
			super(msg);
		}
	}
	static class ErrMath extends Exception {
		ErrMath() {}
		ErrMath(String msg){
			super(msg);
		}
	}

}

