package vdaoengine.analysis.elmap;

import java.util.*;
import java.io.*;
import vdaoengine.data.VDataSet;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.visproc.*;

public class ElmapAlgorithm{

  public int dimension;
  public int currentEpoch;
  public int numberOfSteps;
  public float stretchInitCoeffs[];
  public float globalEPFactor;
  public float globalRPFactor;
  public ElmapAlgorithmEpoch steps[];
  public String strategytype;
  public VDataSet data;
  public VDataSet datatest;
  public Grid grid;
  public Grid genManif;
  public SLAUSolver solver;

  public Vector taxons;
  public PCAMethod basis;

  public ElmapAlgorithm(){
           strategytype = "epoch";
           datatest = null;
           genManif = null;
  }

  public Grid readIniFile(String fn,int algnum){
    String s = vdaoengine.utils.IO.loadString(fn);
    Grid gr = readIniFileStr(s,algnum);
    return gr;
  }

  public Grid readIniFileStr(String settings,int algnum){
    try{
    globalEPFactor = 1;
    globalRPFactor = 1;

    Vector epochs = new Vector();

    LineNumberReader f = new LineNumberReader(new StringReader(settings));

    String gridtype = "simplerectangular";
    int gridsize1 = 10;
    int gridsize2 = 10;
    int gridsize3 = 10;
    stretchInitCoeffs = new float[3];
    stretchInitCoeffs[0] = .8f;
    stretchInitCoeffs[1] = .8f;
    stretchInitCoeffs[2] = .8f;
    numberOfSteps = 0;
    int minNumInCluster = 5;

    String st = "#ALG"+algnum;
    String s = null;
    while(((s=f.readLine())!=null)&&(!s.trim().equals(st)));
    do{
            s = f.readLine();
            if(s!=null){
            StringTokenizer stok = new StringTokenizer(s," =\t\n");
            if(stok.hasMoreTokens()){
                    String tok = stok.nextToken();
                    //System.out.println(tok);
                    if(stok.hasMoreTokens()){
                            String tok1 = stok.nextToken();
                            if(tok.equals("algtype")){
                                    strategytype=tok1;
                            }
                            if(tok.equals("grid.type")){
                                    gridtype=tok1;
                            }
                            if(tok.equals("grid.size1")){
                                    gridsize1 = Integer.parseInt(tok1);
                            }
                            if(tok.equals("grid.size2")){
                                    gridsize2 = Integer.parseInt(tok1);
                            }
                            if(tok.equals("grid.size3")){
                                    gridsize3 = Integer.parseInt(tok1);
                            }
                            if(tok.equals("grid.minclust")){
                                            minNumInCluster = Integer.parseInt(tok1);
                            }
                            if(tok.equals("init.stretch1")){
                                    stretchInitCoeffs[0] = Float.parseFloat(tok1);
                            }
                            if(tok.equals("init.stretch2")){
                                    stretchInitCoeffs[1] = Float.parseFloat(tok1);
                            }
                            if(tok.equals("init.stretch3")){
                                    stretchInitCoeffs[2] = Float.parseFloat(tok1);
                            }
                            if(tok.equals("epoch.id")){
                                    ElmapAlgorithmEpoch ep = new ElmapAlgorithmEpoch();
                                    ep.type = 0;
                                    ep.EP = 1;
                                    ep.RP = 1;
                                    ep.epsConvergence = (float)0.01;
                                    ep.epsConvergenceSLAU = (float)0.01;
                                    ep.numberOfIterations = 100;
                                    ep.maxNumberOfIterationsForSLAU = 100;
                                    ep.extrapolate = 0;
                                    ep.minimize = true;
                                    epochs.add(ep);
                                    numberOfSteps++;
                            }
                            if(tok.equals("epoch.minimize")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    if(tok1.equals("true"))
                                            ep.minimize = true;
                                    else
                                            ep.minimize = false;
                            }
                            if(tok.equals("epoch.ep")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.EP = Float.parseFloat(tok1);
                            }
                            if(tok.equals("epoch.rp")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.RP = Float.parseFloat(tok1);
                            }
                            if(tok.equals("epoch.extrapolate")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.extrapolate = Integer.parseInt(tok1);
                            }
                            if(tok.equals("epoch.eps")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.epsConvergence = Float.parseFloat(tok1);
                            }
                            if(tok.equals("epoch.epsSLAU")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.epsConvergenceSLAU = Float.parseFloat(tok1);
                            }
                            if(tok.equals("epoch.numiter")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.numberOfIterations = Integer.parseInt(tok1);
                            }
                            if(tok.equals("epoch.numiterSLAU")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.maxNumberOfIterationsForSLAU = Integer.parseInt(tok1);
                            }
                            if(tok.equals("adapt.id")){
                                    ElmapAlgorithmEpoch ep = new ElmapAlgorithmEpoch();
                                    ep.type = 1;
                                    ep.EP = 1;
                                    ep.RP = 1;
                                    ep.epsConvergence = (float)0.01;
                                    ep.epsConvergenceSLAU = (float)0.01;
                                    ep.numberOfIterations = 100;
                                    ep.maxNumberOfIterationsForSLAU = 100;
                                    ep.numevery = 1;
                                    ep.numberOfSteps = 100;
                                    ep.epsMSE = (float)0.01;
                                    epochs.add(ep);
                                    numberOfSteps++;
                            }
                            if(tok.equals("adapt.type")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.adapttype = tok1;
                            }
                            if(tok.equals("adapt.numiter")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.numberOfIterations = Integer.parseInt(tok1);
                            }
                            if(tok.equals("adapt.numstep")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.numberOfSteps = Integer.parseInt(tok1);
                            }
                            if(tok.equals("adapt.numevery")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.numevery = Integer.parseInt(tok1);
                            }
                            if(tok.equals("adapt.eps")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.epsConvergence = Float.parseFloat(tok1);
                            }
                            if(tok.equals("adapt.epsSLAU")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.epsConvergenceSLAU = Float.parseFloat(tok1);
                            }
                            if(tok.equals("adapt.epsMSE")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.epsMSE = Float.parseFloat(tok1);
                            }
                            if(tok.equals("adapt.ep")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.EP = Float.parseFloat(tok1);
                            }
                            if(tok.equals("adapt.rp")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.RP = Float.parseFloat(tok1);
                            }
                            if(tok.equals("adapt.numevery")){
                                    ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
                                    ep.numevery = Integer.parseInt(tok1);
                            }
                    }
    }
            
    }
            
            if(s==null) break;
    }while((s!=null)&&(((!s.equals(""))&&(s.charAt(0)!='#')))||(s.equals("")));

    grid = null;
    System.out.println(gridtype);
    if(gridtype.equals("simplerectangular")){
		grid = new SimpleRectangularGrid(gridsize1,gridsize2,data.coordCount);
		setGrid(grid);
    }
    if(gridtype.equals("curve")){
		grid = new Curve(gridsize1,data.coordCount);
		setGrid(grid);
    }
    if(gridtype.equals("circle")){
        grid = new CircleGrid(gridsize1,data.coordCount);
        setGrid(grid);
    }
    
    
//*************************************************************************************************************************
//                                ready for new grid type........
	if(gridtype.equals("elastictree")){
		grid = new ElasticTree(data.coordCount);
		setGrid(grid);
		//System.out.println("data.coordcount="+data.coordCount);
		//System.out.println("grid.nodesNum"+grid.nodesNum);
		//System.out.println("dimension = "+dimension);
	}
//*************************************************************************************************************************
    /*if(gridtype.equals("linear")){
            grid = new LinearGrid(gridsize1,data.coordCount);
            setGrid(grid);
    }
    if(gridtype.equals("circle")){
            grid = new CircleGrid(gridsize1,data.coordCount);
            setGrid(grid);
    }
    if(gridtype.equals("sphere")){
            grid = new SphericalGrid(gridsize1,gridsize2,data.coordCount);
            setGrid(grid);
    }
    if(gridtype.equals("elgraph")){
            grid = new ElasticGraph(gridsize1,data.coordCount);
            ElasticGraph elg = (ElasticGraph)grid;
            elg.minNumInCluster = minNumInCluster;
            setGrid(grid);
    }*/
    if(grid == null) System.out.println("No such grid type : " + gridtype);

    steps = new ElmapAlgorithmEpoch[epochs.size()];
    for(int i=0;i<epochs.size();i++){
            steps[i] = new ElmapAlgorithmEpoch();
            steps[i].type = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).type;
            steps[i].EP = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).EP;
            steps[i].epsConvergence = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).epsConvergence;
            steps[i].epsConvergenceSLAU = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).epsConvergenceSLAU;
            steps[i].extrapolate = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).extrapolate;
            steps[i].maxNumberOfIterationsForSLAU = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).maxNumberOfIterationsForSLAU;
            steps[i].minimize = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).minimize;
            steps[i].numberOfIterations = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).numberOfIterations;
            steps[i].RP = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).RP;
            steps[i].numberOfSteps = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).numberOfSteps;
            steps[i].epsMSE = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).epsMSE;
            steps[i].numevery = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).numevery;
            steps[i].adapttype = ((ElmapAlgorithmEpoch)epochs.elementAt(i)).adapttype;
    }

    currentEpoch = 0;

    f.close();

    }catch(Exception e){
      e.printStackTrace();
    }

    return grid;
  }


  public void setGrid(Grid gr){
    grid = gr;
    dimension = gr.dimension;
  }

  public void initializeGrid(double shift[],double vecs[][],double disp[], int vecnum){

    VStatistics st = data.calcStatistics();
    for(int i=0;i<grid.nodesNum;i++){

            for(int k=0;k<dimension;k++) grid.Nodes[i][k] = (float)shift[k];

            for(int j=0;j<vecnum;j++){
                    double d = 0;
                    d = /*disp[j]**/st.totalDispersion;
                    for(int k=0;k<dimension;k++){
                            grid.Nodes[i][k] += (float)(vecs[j][k]*grid.Nodes3D[i][j]*d*stretchInitCoeffs[j]);
                    }
            }

    }
  }

  public void setData(VDataSet d){
    data = d;
    dimension = d.getCoordCount();
  }
  public void setDataTest(VDataSet d){
    datatest = d;
  }

  public void setGenManif(Grid g){
    genManif = g;
  }

  public void setSolver(SLAUSolver s){
    solver = s;
    solver.dimension = grid.nodesNum;
  }

  //public void setBasis(PCAMethod bas){
  //}

  public int calcTaxons(){
    Date cld = new Date();
    grid.MakeNodesCopy();
    taxons = grid.calcTaxons(data);
    return (int)((new Date()).getTime()-cld.getTime());
  }
  public double calcMSE(){
     return grid.calcMSE(data,taxons);
  }
  public double calcMSEtest(){
    double res = 0;
    if(datatest!=null){
            Vector v = grid.calcTaxons(datatest);
            res = grid.calcMSE(datatest,v);
    }
    return res;
  }

  public long calcSystemMatrix(String fn){

    Date cld = new Date();

    float ep = (float)(globalEPFactor*Math.pow((double)grid.nodesNum,(double)(2-grid.intDim)/(double)grid.intDim)/100f);
    float rp = (float)(globalRPFactor*Math.pow((double)grid.nodesNum,(double)(2-grid.intDim)/(double)grid.intDim)/Math.pow(10.0,(double)grid.intDim));

    // first: diagonal addings

    solver.dimension = grid.nodesNum;
    solver.initMatrix();

    for(int i=0;i<grid.nodesNum;i++){
            double d = 0;
            int tx[] = (int[])taxons.elementAt(i);
            if(data.weighted){
                    for(int k=0;k<tx.length;k++)
                            d+=data.weights[tx[k]];
                    d/=data.weightSum;
            }
            else
                    d = (double)tx.length/data.pointCount;
            if(globalEPFactor<0) d = - d;
            solver.addToMatrixElement(i,i,d);
    }
    // now all the rest

    int k1,k2,k3;
    for(int i=0;i<grid.edgesNum;i++){
            k1 = grid.Edges[i][0];
            k2 = grid.Edges[i][1];
            solver.addToMatrixElement(k1,k1,ep*grid.EP[i]);
            solver.addToMatrixElement(k2,k2,ep*grid.EP[i]);
            solver.addToMatrixElement(k1,k2,-ep*grid.EP[i]);
            solver.addToMatrixElement(k2,k1,-ep*grid.EP[i]);
    }

    for(int i=0;i<grid.ribsNum;i++){
            k1 = grid.Ribs[i][1];
            k2 = grid.Ribs[i][0];
            k3 = grid.Ribs[i][2];

            solver.addToMatrixElement(k1,k1,rp*grid.RP[i]);
            solver.addToMatrixElement(k2,k2,4*rp*grid.RP[i]);
            solver.addToMatrixElement(k3,k3,rp*grid.RP[i]);

            solver.addToMatrixElement(k1,k2,-2*rp*grid.RP[i]);
            solver.addToMatrixElement(k2,k1,-2*rp*grid.RP[i]);
            solver.addToMatrixElement(k2,k3,-2*rp*grid.RP[i]);
            solver.addToMatrixElement(k3,k2,-2*rp*grid.RP[i]);

            solver.addToMatrixElement(k1,k3,rp*grid.RP[i]);
            solver.addToMatrixElement(k3,k1,rp*grid.RP[i]);
    }

     solver.createMatrix();
     solver.createPreconditioner();
     return (int)((new Date()).getTime()-cld.getTime()+1);
  }

  public long calcRightHandVector(int coord,double vec[]){
    for(int i=0;i<grid.nodesNum;i++){
    vec[i] = 0;
    int tx[] = (int[])taxons.elementAt(i);
    if(!data.hasGaps)
    for(int j=0;j<tx.length;j++)
             if(!data.weighted)
                          vec[i] += data.massif[tx[j]][coord];
             else
                          vec[i] += data.massif[tx[j]][coord]*data.weights[tx[j]];
    else
      for(int j=0;j<tx.length;j++) if(!Float.isNaN(data.massif[tx[j]][coord]))
               if(!data.weighted)
                            vec[i] += data.massif[tx[j]][coord];
               else
                            vec[i] += data.massif[tx[j]][coord]*data.weights[tx[j]];
    }
    for(int i=0;i<grid.nodesNum;i++)
                  if(!data.weighted)
              vec[i] = vec[i]/(data.pointCount);
                  else
                          vec[i] = vec[i]/data.weightSum;
    return 0;
  }

  public void saveRightHandVector(double vec[],String fn){
  }

  public void doit(){
    System.out.println("Strategy: "+strategytype+"\n");
    if(strategytype.equals("epoch")) {  doTheEpochStrategy(); }
    if(strategytype.equals("simple")){
    	ElmapAlgorithmEpoch epoch = new ElmapAlgorithmEpoch();
    	epoch.minimize = true;
    	steps = new ElmapAlgorithmEpoch[1];
    	steps[0] = epoch;
    	numberOfSteps = 1;
    	doTheEpochStrategy(); 
    }
  }

  public void doTheEpochStrategy(){
    long l = 1;
    while(l>=0){
            if(currentEpoch>=numberOfSteps) break;
            if(steps[currentEpoch].type==0)
                    l = proceedToTheNextEpoch();
            else
            if(steps[currentEpoch].type==1){
                    ElmapAlgorithmEpoch epoch = steps[currentEpoch];
                    calcTaxons();
                    for(int i=0;i<epoch.numberOfSteps;i++){
                            System.out.println("Adaptation step: "+(i+1));
                            double mse1 = calcMSE();
                            //if(epoch.adapttype.equals("grow"))
                            //        grid.adaptateGrow(taxons,epoch.numevery);
                            //if(epoch.adapttype.equals("break"))
                            //        grid.adaptateBreak(taxons,data);
                            l = proceedToTheNextEpoch();
                            calcTaxons();
                            double mse2 = calcMSE();
                            currentEpoch--;
                            if(Math.abs(mse1-mse2)<epoch.epsMSE)
                                    break;
                    }
            currentEpoch++;
            }
    }
  }

  public long proceedToTheNextEpoch(){
    if(currentEpoch>=numberOfSteps){
            return -1;
    }else{

    ElmapAlgorithmEpoch epoch = steps[currentEpoch];

    currentEpoch++;

    globalEPFactor = epoch.EP;
    globalRPFactor = epoch.RP;

    float MemNodesPos[][] = new float[grid.nodesNum][grid.dimension];
    for(int i=0;i<grid.nodesNum;i++) {
            for(int j=0;j<grid.dimension;j++) {
                    MemNodesPos[i][j] = grid.Nodes[i][j];
            }
    }

    double rightHandVector[] = new double[grid.nodesNum];
    double solution[] = new double[grid.nodesNum];

    if(epoch.minimize)
    for(int i=0;i<epoch.numberOfIterations;i++){
/****************************************************************************************************/
            long cl2 = calcTaxons();
            long cl3 = calcSystemMatrix("matrix");

            Date cld = new Date();

           for(int j=0;j<grid.dimension;j++){
                    for(int k=0;k<grid.nodesNum;k++)
                            solution[k] = grid.Nodes[k][j];
                    long cl4 = calcRightHandVector(j,rightHandVector);

                    solver.setSolution(solution);
                    solver.setVector(rightHandVector);

                    ///long cl1 = clock();

                    solver.solve(epoch.epsConvergenceSLAU,epoch.maxNumberOfIterationsForSLAU);

                    for(int k=0;k<grid.nodesNum;k++)
                            grid.Nodes[k][j] = (float)solver.solution[k];
            }
/*****************************************************************************************************/
            // now calculating how far it is shifted
            float diff = 0;
            for(int k=0;k<grid.nodesNum;k++){
                    for(int j=0;j<grid.dimension;j++){
                            diff+=(float)Math.abs(MemNodesPos[k][j]-grid.Nodes[k][j]);
                            MemNodesPos[k][j] = grid.Nodes[k][j];
                    }
            }
            diff/=(float)grid.dimension;
            diff/=(float)grid.nodesNum;

            // to implement
            String s = "";
            if(genManif!=null){
                    double d = 0;
                    //d = grid.MSEDistanceTo(genManif,0);
                    s = " GMSE "+d;
            }

            // to implement
            if(datatest!=null)
              System.out.println("Ep: " + currentEpoch + " It: " + i +  " MSE: " + calcMSE() + "/" + calcMSEtest() + s + " Df: " + diff + " Time: "+ ((new Date()).getTime()-cld.getTime()) + "+(" + cl2 + "," + cl3+")");
            else
              System.out.println( "Ep: " + currentEpoch + " It: " + i +  " MSE: " + calcMSE() + s + " Df: " + diff + " Time: "+ ((new Date()).getTime()-cld.getTime()) + "+(" + cl2 + "," + cl3+ ")" );

            if(diff<=epoch.epsConvergence) break;
    }

    solver.solution = null;
    solver.vector = null;

    for(int i=0;i<epoch.extrapolate;i++){
            switch(grid.type){
            case 0: {
                    SimpleRectangularGrid g = (SimpleRectangularGrid)grid;
                    g.extrapolate(); }
            break;
            case 1: {
                    Curve g = (Curve)grid;
                    g.extrapolate();
                    }
            break;
            }
    }
    return 0;
  }
  }

  public void computeElasticGrid(){
    VStatistics vs = data.calcStatistics();
    double d2 = vs.totalDispersion*vs.totalDispersion;

    PCAMethod PCA = new PCAMethod();
    PCA.setDataSet(data);
    PCA.calcBasis(3);
    double d[] = PCA.calcDispersionsRelative(vs.totalDispersion);

    SLAUSolver solv = new SLAUSolver();
    setSolver(solv);
    initializeGrid(PCA.getBasis().a0,PCA.getBasis().basis,d,3);
    
    System.out.println("Proceeding with " + data.pointCount + " points");
    Date cld = new Date();
    if(grid.type!=4)
        doit();
    else{
       //elm.doGraphConstruct();
    }

    System.out.println("Time spent: " + ((new Date()).getTime()-cld.getTime()) );
    calcTaxons();
    System.out.println("Final MSE: " + calcMSE());
  }
  
  public static void computeElasticGridPreinitialized(VDataSet ds, Grid grid, Vector epochs){
	  
        ElmapAlgorithm elm = new ElmapAlgorithm();
        SLAUSolver solver = new SLAUSolver();
        elm.setGrid(grid);
        elm.setSolver(solver);
        elm.setData(ds);
        
        elm.steps = new ElmapAlgorithmEpoch[epochs.size()];
        elm.numberOfSteps = epochs.size();
        for(int i=0;i<epochs.size();i++) elm.steps[i] = (ElmapAlgorithmEpoch)epochs.get(i);

	    elm.calcTaxons();
	    elm.data.calcStatistics();
	    System.out.println("Total variation: " + elm.data.simpleStatistics.totalDispersion*elm.data.simpleStatistics.totalDispersion);
	    System.out.println("Initial MSE: " + elm.calcMSE());
	    
	    System.out.println("Proceeding with " + elm.data.pointCount + " points");
	    Date cld = new Date();
	    if(grid.type!=4){
	    	elm.doit();
	    }else{
	       //elm.doGraphConstruct();
	    }

	    System.out.println("Time spent: " + ((new Date()).getTime()-cld.getTime()) );
	    elm.calcTaxons();
	    System.out.println("Final MSE: " + elm.calcMSE());
	  }

  public static Grid computeElasticGrid(VDataSet ds,String conf,int algnum){
      ElmapAlgorithm elm = new ElmapAlgorithm();
      elm.setData(ds);
      elm.readIniFile(conf,algnum);
      elm.computeElasticGrid();
      return elm.grid;
  }

}