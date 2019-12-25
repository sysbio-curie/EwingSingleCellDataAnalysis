package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class testElasticity {

	public static void main(String[] args) {
		
		try{
		
		
		//This is 4 points test 
		 
		/*String project = "4points";
		String path = "c:/datas/elastictree/";
		//VDataTable vt = VDatReadWrite.LoadFromVDatFile("c:/datas/elastictree/iris.dat");
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(path+project+".dat");
		VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);

		GraphGrammar grammarGrow = new GraphGrammar(); 
		BisectEdge be = new BisectEdge();
		AddNodeToNode an = new AddNodeToNode(); 
		grammarGrow.operations.add(be);
		grammarGrow.operations.add(an);

		int numberOfNodes = 4;
		
		FileWriter fw = new FileWriter(path+project+"_test.txt");
		fw.write("EdgeE\tRibE\tStarE\tENERGY\tMSE\tCODE\t");
		for(int i=0;i<numberOfNodes;i++)for(int j=0;j<vd.coordCount;j++){
			fw.write("C"+i+"_"+j+"\t");
		}
		fw.write("\n");
		
		float step = 1f;
		float minval = -8;
		float maxval = 2;
		for(float edgeE=minval-10;edgeE<=maxval;edgeE+=step*100){
			for(float ribE=minval;ribE<=maxval;ribE+=step){
				for(float starE=minval;starE<=maxval;starE+=step){
		
					float eE = (float)Math.pow(10,edgeE);
					float rE = (float)Math.pow(10,ribE);
					float sE = (float)Math.pow(10,starE);
					
					//sE = rE;
					
					Graph graph = new Graph();			
					graph.setDefaultElasticityCoeff(0.05f);
					graph.setDefaultElasticityCoeffs(1,eE);
					graph.setDefaultElasticityCoeffs(2,rE);
					graph.setDefaultElasticityCoeffs(3,sE);

					ElasticEnergyOptimization elo = new ElasticEnergyOptimization(vd,graph);
					BaseOptimizationAlgorithm alg = new BaseOptimizationAlgorithm(vd);
					alg.parameters.maxNumberOfNodes = numberOfNodes;
					alg.grammars.add(grammarGrow);
					alg.setGraph(graph);
					alg.initializeGraph();
					alg.run(elo);
					
					elo.calcTaxons();
					
					fw.write(edgeE+"\t"+ribE+"\t"+starE+"\t"+elo.energyValue+"\t"+alg.graph.calcMSE(vd, elo.taxons)+"\t"+alg.graph.getSimpleTopologyCode()+"\t");
					
					for(int i=0;i<numberOfNodes;i++)for(int j=0;j<vd.coordCount;j++){
						fw.write(""+alg.graph.getNode(i).x[j]+"\t");
					}
					fw.write("\n");
					
					alg.graph.writeOutNodes(path+project+"/map_m2_"+ribE+"_m3_"+starE);
					alg.graph.writeOutEdges(path+project+"/edge_m2_"+ribE+"_m3_"+starE);
					
					//alg.graph.writeOutNodes(path+project+"/map_m2_"+edgeE+"_m3_"+ribE);
					//alg.graph.writeOutEdges(path+project+"/edge_m2_"+edgeE+"_m3_"+ribE);
					
					
				}
			}
		}
		
		fw.close();
		VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(path+project+"_test.txt", true, "\t");
		for(int i=0;i<vtt.colCount;i++)
			vtt.fieldTypes[i] = vtt.NUMERICAL;
		vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt, path+project+"_test.dat");*/
		
		
			
			String project = "tree23";
			String path = "c:/datas/elastictree/";
			//VDataTable vt = VDatReadWrite.LoadFromVDatFile("c:/datas/elastictree/iris.dat");
			VDataTable vt = VDatReadWrite.LoadFromVDatFile(path+project+".dat");
			VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);

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
			

			int numberOfNodes = 17;
			
			FileWriter fw = new FileWriter(path+project+"_test.txt");
			
			fw.write("EdgeE\tRibE\tStarE\tENERGY\tMSE\tCODE\tURN2\t");
			for(int i=0;i<numberOfNodes;i++)for(int j=0;j<vd.coordCount;j++){
				fw.write("C"+i+"_"+j+"\t");
			}
			fw.write("\n");
			
			float step = 0.25f;
			float minval = -4;
			float maxval = 1;
			
			//test.variant = 0;
			
			for(float edgeE=minval;edgeE<=maxval;edgeE+=step){
				for(float ribE=minval;ribE<=maxval;ribE+=step){
					for(float starE=maxval;starE<=maxval;starE+=step*100){
			
						float eE = (float)Math.pow(10,edgeE);
						float rE = (float)Math.pow(10,ribE);
						float sE = (float)Math.pow(10,starE);
						sE = rE;
						
						Graph graph = new Graph();
						//graph.complexityFee = 0.01f;
						graph.setDefaultElasticityCoeffs(rE);
						graph.setDefaultEdgeElasticityCoeff(eE);
						//graph.setDefaultElasticityCoeff(2,rE);
						//graph.setDefaultElasticityCoeff(3,sE);

						ElasticEnergyOptimization elo = new ElasticEnergyOptimization(vd,graph);
						BaseOptimizationAlgorithm alg = new BaseOptimizationAlgorithm(vd);
						alg.parameters.maxNumberOfNodes = numberOfNodes;
						alg.verbose = false;
						alg.grammars.add(grammarGrow);
						alg.grammars.add(grammarGrow);
						alg.grammars.add(grammarShrink);
						
						//alg.setElementFeeExponential(1e-4f, 1.5f);
						
						alg.setGraph(graph);
						alg.initializeGraph();
						alg.run(elo);
						
						elo.calcTaxons();
						elo.updateEnergyValue();
						
						float complexity = elo.URValue*graph.getNodeNum()*graph.getNodeNum();
						
						System.out.println(edgeE+"\t"+ribE+"\t"+starE+"\tEN="+elo.energyValue+"\tMSE="+elo.graph.calcMSE(vd, elo.taxons)+"\t"+elo.graph.getSimpleTopologyCode()+"\t"+complexity);						
						
						fw.write(edgeE+"\t"+ribE+"\t"+starE+"\t"+elo.energyValue+"\t"+elo.graph.calcMSE(vd, elo.taxons)+"\t"+elo.graph.getSimpleTopologyCode()+"\t"+complexity+"\t");
						
						for(int i=0;i<alg.graph.getNodeNum();i++)for(int j=0;j<vd.coordCount;j++){
							fw.write(""+alg.graph.getNode(i).x[j]+"\t");
						}
						fw.write("\n");
						fw.flush();
						
						//alg.graph.writeOutNodes(path+project+"/map_m2_"+ribE+"_m3_"+starE);
						//alg.graph.writeOutEdges(path+project+"/edge_m2_"+ribE+"_m3_"+starE);
						
						alg.graph.writeOutNodes(path+project+"/map_m2_"+edgeE+"_m3_"+ribE);
						alg.graph.writeOutEdges(path+project+"/edge_m2_"+edgeE+"_m3_"+ribE);
						String code = alg.graph.getSimpleTopologyCode();
						FileWriter fwc = new FileWriter(path+project+"/code_m2_"+edgeE+"_m3_"+ribE);
						fwc.write(code);
						fwc.close();
						
						
					}
				}
			}
			
			fw.close();
			VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(path+project+"_test.txt", true, "\t");
			for(int i=0;i<vtt.colCount;i++)
				vtt.fieldTypes[i] = vtt.NUMERICAL;
			vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt, path+project+"_test.dat");			
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
