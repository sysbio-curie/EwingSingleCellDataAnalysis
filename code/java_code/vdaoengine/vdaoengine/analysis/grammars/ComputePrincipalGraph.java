package vdaoengine.analysis.grammars;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Vector;

import vdaoengine.TableUtils;
import vdaoengine.analysis.elmap.ElmapAlgorithmEpoch;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.VStatistics;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.OptionParser;
import vdaoengine.utils.VSimpleProcedures;

public class ComputePrincipalGraph{

	public ConfigFile config = new ConfigFile();
	public VDataSet dataset = null;
	public String project = "";

	public ElasticEnergyOptimization elo = null;
	
	public BaseOptimizationAlgorithm alg = null;
	
	public Graph graph = null;
	
	public int maxnumnodes = -1;
	
	public float initNodePositions[][] = null;
	public int initEdges[][] = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			vdaoengine.data.io.VDatReadWrite.useQuotesEverywhere = false;
			String fileInitNodePositions = null;
			String fileInitEdges = null;
			
			
			if(args.length==0){
				args = new String[10];
				args[0] = "--data";
				//args[0] = "--dat";
				args[1] = "C:/Datas/ElGraph_Matlab/test_data/tree23.txt";
				//args[1] = "C:/Datas/ElGraph_Matlab/test_data/tree23_noise.txt";
				//args[1] = "C:/Datas/mygnm/hgdp_PC100_nation.txt";
				//args[1] = "C:/Datas/ElGraph_Matlab/test_data/hgdp_PC3.txt"; 
				//args[1] = "c:/datas/elastictree/iris.dat";
				//args[1] = "c:/datas/elastictree/wine.dat";
				//args[1] = "c:/datas/elastictree/wdbc_cut.dat";
				//args[1] = "c:/datas/elastictree/winequality-red.dat";
				//args[1] = "c:/_datasarchive/elastictree/scoelicolor.dat";
				//args[1] = "C:/_DatasArchive/ElasticTree/iris_test/iris.txt";
				//args[1] = "C:/Datas/Peshkin/xenopus/day23r_reduced50.dat";
				//args[1] = "c:/datas/elastictree/forestfires.dat";
				//args[1] = "c:/datas/elastictree/abalone.dat";
				//args[1] = "c:/datas/elastictree/wangn5000_t.dat";
				args[2] = "--config";
				args[3] = "elmap.ini";
				args[4] = "--num";
				args[5] = "18";
				args[6] = "--normalize";
				args[7] = "0";
				args[8] = "--maxnumnodes";
				args[9] = "30";
			}
			
			OptionParser options = new OptionParser(args, null);
			String s = null;
			File f = null;
			File datFile = null;
			File txtFile = null;
			File configFile = null;
			int algorithmNumber = 0;
			int maxnumnodes = -1;
			boolean normalize = false;
			Boolean b = false;
			
			if ((f = options.fileOption("dat", "dat file")) != null)
				datFile = f;
			if ((f = options.fileOption("data", "txt tab-delimited file with one-line header")) != null)
				txtFile = f;
			if ((f = options.fileRequiredOption("config", "configuration file")) != null)
				configFile = f;
			if ((s = options.stringOption("num", "number of the algorithm in the config file")) != null)
				algorithmNumber = Integer.parseInt(s);
			if ((s = options.stringOption("normalize", "do standard data normalization")) != null)
				normalize = s.equals("1");
			if ((s = options.stringOption("maxnumnodes", "maximum number of nodes in the graph")) != null)
				maxnumnodes = Integer.parseInt(s);
			if ((s = options.stringOption("initNodes", "file with node positions for initializing the graph")) != null){
				fileInitNodePositions = s;
			}
			if ((s = options.stringOption("initEdges", "file with edge description in the format <node1><tab><node2> per line for initializing the graph")) != null){
				fileInitEdges = s;
			}
				
			
			options.done();
			
			ComputePrincipalGraph cpg = new ComputePrincipalGraph();
			String projectName = null;
			if(txtFile!=null){
				projectName = txtFile.getName().substring(0,txtFile.getName().length()-4);
			}else
				projectName = datFile.getName().substring(0,datFile.getName().length()-4);
			System.out.println("Project "+projectName);
			cpg.project = projectName;
			cpg.config.readFile(configFile.getAbsolutePath(), algorithmNumber);
			
			VDataTable vt = null;
			if(txtFile!=null){	
				vt = VDatReadWrite.LoadFromSimpleDatFile(txtFile.getAbsolutePath(), true, "\t");
				TableUtils.findAllNumericalColumns(vt);
				datFile = txtFile;
			}else{
				vt = VDatReadWrite.LoadFromVDatFile(datFile.getAbsolutePath());
			}
			if(!normalize) 
				cpg.dataset = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
			else
				cpg.dataset = VSimpleProcedures.SimplyPreparedDataset(vt, -1);

			cpg.dataset.calcStatistics();
			System.out.println("Total standart deviation = "+cpg.dataset.simpleStatistics.totalDispersion);
			
			
			cpg.maxnumnodes = maxnumnodes;
			
			if(fileInitNodePositions!=null)if(fileInitEdges!=null){
				System.out.println("Using pre-defined graph configuration...");
				VDataTable nodepos = VDatReadWrite.LoadFromSimpleDatFile(fileInitNodePositions, false, "\t");
				cpg.initNodePositions = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(nodepos, -1).massif;
				cpg.initEdges = VDatReadWrite.LoadIntegerMassifTabDelimited(fileInitEdges);
				System.out.println("Number of nodes="+cpg.initNodePositions.length);
				System.out.println("Number of edges="+cpg.initEdges.length);
				cpg.setPrimitiveGraphByNodesAndEdges(cpg.initNodePositions, cpg.initEdges);
				cpg.config.initStrategy = -1;
			}
			
			cpg.compute();
			
			String prefix = datFile.getAbsolutePath().substring(0,datFile.getAbsolutePath().length()-4);
			
			cpg.saveToFile(prefix+".vem");
						
			cpg.graph.writeOutNodes(prefix+".nodes");
			cpg.graph.writeOutEdges(prefix+".edges");
			HashMap<Integer,Integer> map = cpg.graph.writeOutNodePointMap(cpg.dataset,prefix+".pointmap");
			
			cpg.writeNodesAsTable(vt,prefix+".nodestable");
			
			int sizes[] = cpg.graph.countNumberOfPointsProjected(cpg.dataset);
			vdaoengine.utils.Utils.writeMassif1DToFile(sizes,prefix+".projectednums");
			
			

			cpg.writeNodeSizesAsTable(sizes,prefix+".projnumstable");
			
			vdaoengine.data.io.VDatReadWrite.useQuotesEverywhere = false;
			vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFilePureNumerical(cpg.dataset,datFile.getAbsolutePath().substring(0,datFile.getAbsolutePath().length()-4)+".data");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	

	public String compute() throws Exception{
		
		String report = "";
		
		dataset.calcStatistics();
		//System.out.println("Variation = "+dataset.simpleStatistics.totalDispersion);
		//dataset.simpleStatistics.calculate();
		//System.out.println("Total dispersion = "+dataset.simpleStatistics.totalDispersion);
		
		
		//alg.verbose = false;
		
		//alg.setElementFeeExponential(1e-4f, 3f);
		//alg.setElementFeeLinear(0.3e-4f, 1f);
		if(graph==null){
			graph = new Graph();
		}
		alg = new BaseOptimizationAlgorithm(dataset);
		alg.setGraph(graph);
	
		alg.parameters.initStrategy = config.initStrategy;
		alg.parameters.maxNumberOfNodes = 0;
		alg.initializeGraph();
		//alg.graph.saveToFile("C:/Datas/ElGraph_Matlab/test_data/tree23_init.vem", "tree23");
		//alg.graph.saveToFile("C:/Datas/ElGraph_Matlab/test_data/hgdp_PC3_init.vem", "hgdp_PC3");
		
		
		for(int i=0;i<config.epochs.size();i++){
			
			System.out.println("Epoch: "+(i+1));
			ElmapAlgorithmEpoch ep = config.epochs.get(i);
			
			defineGrammarType(ep);
			
			graph.setDefaultEdgeElasticityCoeff(ep.EP);
			graph.setDefaultElasticityCoeffs(ep.RP);
			graph.setElasticityCoeffs(1, ep.EP);
			for(int k=2;k<graph.MAX_STAR_DEGREE;k++) graph.setElasticityCoeffs(k, ep.RP);
			
			if(maxnumnodes>0)
				alg.parameters.maxNumberOfNodes += maxnumnodes;
			else
				alg.parameters.maxNumberOfNodes += ep.numberOfIterations;
			//alg.movieFolder = "c:/datas/elastictree/movie/";

			if(ep.minimize){

				if(ep.robust){
					elo = new ElasticEnergyOptimizationTrimmed(dataset, graph);
					((ElasticEnergyOptimizationTrimmed)elo).setTrimmingRadiusAsFractionOfStd(ep.trimradius);
				}else
					elo = new ElasticEnergyOptimization(dataset, graph);

				//elo.calcTaxons();
				//System.out.println("Number of points captured "+((ElasticEnergyOptimizationTrimmed)elo).NumberOfPointsCaptured);
				
				report = alg.run(elo);
				if(alg.convergedByComplexity)
					System.out.println("Converged by complexity");
				
				elo.updateEnergyValue();
				
				System.out.println("MSE = "+alg.graph.calcMSE(dataset, elo.taxons));
				System.out.println("Energy = "+elo.energyValue);
				
				/*System.out.println();System.out.println();System.out.println();
				elo.calcTaxons();
				Vector<Vector<Integer>> taxons = null;
				if(elo.getClass().getName().endsWith("Trimmed"))
					taxons = ((ElasticEnergyOptimizationTrimmed)elo).taxons_close;
				else
					taxons = elo.taxons;
				for(int l=0;l<taxons.size();l++){
					Star star = elo.graph.getStarByCentralNode(l);
					int degree = 0;
					if(star!=null) degree = star.neighbours.size();
					System.out.println("Taxon("+degree+") "+l+" :"+taxons.get(l)); 
				}*/
				
			}
			
		graph = alg.graph;
			
		}
		
		
		
		return report;
	}
	
	public void defineGrammarType(ElmapAlgorithmEpoch ep) throws Exception{
		
		alg.grammars = new Vector<GraphGrammar>();
		
		if(ep.grammarType.equals("tree")){
			GraphGrammar grammarGrow = new GraphGrammar(); 
			BisectEdge be = new BisectEdge();
			AddNodeToNode an = new AddNodeToNode(); 
			grammarGrow.operations.add(be);
			grammarGrow.operations.add(an);		
			alg.grammars.add(grammarGrow);
		}
		if(ep.grammarType.equals("treeWithPruning")){
			GraphGrammar grammarGrow = new GraphGrammar(); 
			BisectEdge be = new BisectEdge();
			AddNodeToNode an = new AddNodeToNode(); 
			grammarGrow.operations.add(be);
			grammarGrow.operations.add(an);			
			GraphGrammar grammarShrink = new GraphGrammar();
			RemoveLeaf rl = new RemoveLeaf();
			RemoveInternalEdge rie = new RemoveInternalEdge(); 
			grammarShrink.operations.add(rl);
			grammarShrink.operations.add(rie);
			alg.grammars.add(grammarGrow);
			alg.grammars.add(grammarGrow);
			alg.grammars.add(grammarShrink);
		}
		if(ep.grammarType.equals("curve")){
			GraphGrammar grammarGrow = new GraphGrammar(); 
			BisectEdge be = new BisectEdge();
			grammarGrow.operations.add(be);
			alg.grammars.add(grammarGrow);
		}
		if(ep.grammarType.equals("circle")){
			GraphGrammar grammarGrow = new GraphGrammar(); 
			BisectEdge be = new BisectEdge();
			grammarGrow.operations.add(be);
			alg.grammars.add(grammarGrow);
		}
		
	}
	
	public void saveToFile(String fn) throws Exception{
		alg.graph.recalcIndexMaps();
		
		alg.graph.saveToFile(fn,project);
		
	}
	
	public void writeNodesAsTable(VDataTable vt, String fn){
	    try{
		      FileWriter fw = new FileWriter(fn);
		      fw.write("NODE\t");
		      int k=0;
		      for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL){
		    	  fw.write(vt.fieldNames[i]+"\t");
		      }
		      fw.write("\n");		      
		      for(int i=0;i<graph.Nodes.size();i++){
		        //float v[] = (float[])TreeNodes.elementAt(i);
		    	fw.write(i+"\t");
		        for(int j=0;j<graph.getDimension();j++){
		          fw.write(""+((Node)graph.Nodes.get(i)).x[j]);
		          if(j<graph.getDimension()-1)
		            fw.write("\t");
		        }
		        fw.write("\n");
		      }
		      fw.close();
		    }catch(Exception e){
		      e.printStackTrace();
		    }
	}
	
	private void writeNodeSizesAsTable(int sizes[], String fn) {
		try{
			FileWriter fw = new FileWriter(fn);
			fw.write("NODE\tNPOINTS\n");
			for(int i=0;i<graph.Nodes.size();i++)
				fw.write(i+"\t"+sizes[i]+"\n");
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

/*	private void averageTableOverGraphNodes(HashMap<Integer, Integer> map, File table, String string) {
		
		
	}*/

	public void setDataSetAsMassif(float massif[][]){
		int numpoints = massif.length;
		int coordnum = massif[0].length;
		dataset = new VDataSet();
		dataset.massif = massif;
		dataset.pointCount = numpoints;
		dataset.coordCount = coordnum;
	}
	
	public void setPrimitiveGraphByNodesAndEdges(float nodes[][], int edges[][]){
		graph = new Graph();
		Utils.setNodesForGraph(graph, nodes);
		Utils.setEdgesForGraph(graph, edges);
		graph.defineStarsFromPrimitiveGraphStructure();
	}

}
