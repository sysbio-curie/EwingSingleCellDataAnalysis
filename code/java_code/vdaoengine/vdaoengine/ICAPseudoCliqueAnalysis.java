package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;


public class ICAPseudoCliqueAnalysis {

	  public static int numberOfDatasets = 5;
	  public static int numberOfComponents = 20;
	
	  public static void main(String[] args) {
		  try{
			  
			  String prefix = "C:/Datas/EWING/ICA/";
			  //String file = "corr_breast_cancer";	  
			  String file = "corr05";	  

			  FileWriter fw = new FileWriter(prefix+"pc_out.txt"); 
			  fw.write("PSEUDOCLIQUE\tSCORE\tNEDGES\n");
			  
			  //for(int kk=0;kk<100;kk++){
			  //	  System.out.println(kk);
			  
			  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+file, true, "\t");
			  HashMap<Long, Float> pairs = new HashMap<Long, Float>();
			  HashMap<String,Set<String>> neighbours = new HashMap<String,Set<String>>();
			  
			  for(int i=0;i<vt.rowCount;i++){
				  String source = vt.stringTable[i][vt.fieldNumByName("SOURCE")];
				  String target = vt.stringTable[i][vt.fieldNumByName("TARGET")];
				  
				  Set<String> neigh = neighbours.get(source);
				  if(neigh==null) { neigh = new HashSet<String>(); neighbours.put(source, neigh); }
				  neigh.add(target);
				  
				  neigh = neighbours.get(target);
				  if(neigh==null) { neigh = new HashSet<String>(); neighbours.put(target, neigh); }
				  neigh.add(source);				  
				  
				  //System.out.println(vt.stringTable[i][vt.fieldNumByName("RABS")]);
				  
				  float r = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("RABS")]);
				  StringTokenizer st = new StringTokenizer(source,"IC");
				  int sourceds = Integer.parseInt(st.nextToken());
				  int sourcecomp = Integer.parseInt(st.nextToken());
				  st = new StringTokenizer(target,"IC");
				  int targetds = Integer.parseInt(st.nextToken());
				  int targetcomp = Integer.parseInt(st.nextToken());
				  long pair = sourcecomp*(long)(Math.pow(100, sourceds-1))+targetcomp*(long)(Math.pow(100, targetds-1));
				  pairs.put(pair, r);
				  //System.out.println(pair+"\t"+conv	ertLongLabelToString(pair)+"\t"+r);
			  }
			  //System.exit(0);
			  
			  //permuteCorrelations(pairs, 10000);
			  
			  // Component sets are labeled by long number
			  // with maximal number of components = 100
			  // Example: 190201  - 19 component set 3, 2 component set 2, 1 component set 1
			  Vector<Long> pseudocliques = generateCandidatePseudoCliques(pairs, neighbours, numberOfDatasets-1);
			  Vector<Float> scores = computePseudoCliqueScores(pseudocliques, pairs);
			  float scoresf[] = new float[scores.size()];
			  for(int i=0;i<scoresf.length;i++) scoresf[i] = scores.get(i);
			  
			  
			  int ind[] = Algorithms.SortMass(scoresf);
			  filterIntersectingPseudoCliques(pseudocliques, scores, ind, numberOfDatasets-1);
			  scoresf = new float[scores.size()];
			  for(int i=0;i<scoresf.length;i++) scoresf[i] = scores.get(i);
			  ind = Algorithms.SortMass(scoresf);

			  
			  System.out.println("Number of cliques = "+pseudocliques.size());
			  System.out.println("Number of scores = "+scores.size());
			  
			  for(int i=ind.length-1;i>=0;i--)if(scoresf[ind[i]]>0){
				  fw.write(convertLongLabelToString(pseudocliques.get(ind[i]))+"\t"+scoresf[ind[i]]+"\t"+getNumberOfEdges(pseudocliques.get(ind[i]),pairs)+"\n");
			  }

			  //}
			  
			  fw.close();
			  
			  
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	  }	
	  
	  public static Vector<Long> generateCandidatePseudoCliques(HashMap<Long, Float> pairs, HashMap<String,Set<String>> neighbours, int support){
		  Vector<Long> pc = new Vector<Long>();
		  long totalNumber = (long)Math.pow(numberOfComponents+1, numberOfDatasets);
		  for(long i=0;i<totalNumber;i++){
			  long num = i;
			  int comps[] = new int[numberOfDatasets];
			  for(int j=numberOfDatasets-1;j>=0;j--){
				  comps[j] = (int)((float)(num/Math.pow(numberOfComponents+1, j)));
				  num = num - comps[j]*(long)Math.pow(numberOfComponents+1, j);
			  }
			  long label = 0;
			  for(int j=0;j<comps.length;j++){
				  label+=comps[j]*(long)Math.pow(100, j);
			  }
			  if(getNumberOfEdges(label,pairs)>=support){
				  if(checkIfConnected(label,pairs,neighbours))
				        pc.add(label);
			  }
		  }
		  
		  // Now exclude included sets
		  Vector<Long> pcunique = new Vector<Long>();
		  for(int i=0;i<pc.size();i++){
			  if(!checkIfIncluded(pc.get(i),pc))
				  pcunique.add(pc.get(i));
		  }
		  return pcunique;
	  }
	  
	  
	  public static Vector<Float> computePseudoCliqueScores(Vector<Long> pseudocliques, HashMap<Long, Float> pairs){
		  Vector<Float> scores = new Vector<Float>();
		  for(int i=0;i<pseudocliques.size();i++)
			  scores.add(computePseudoCliqueScore(pseudocliques.get(i),pairs));
		  return scores;
	  }
	  
	  public static float computePseudoCliqueScore(Long pc, HashMap<Long, Float> pairs){
		  float score = 0f;
		  long allpairs[] = getAllPairs(pc);
		  for(int i=0;i<allpairs.length;i++){
			  if(pairs.get(allpairs[i])!=null)
				  score+=pairs.get(allpairs[i]);
		  }
		  return score;
	  }
	  
	  public static String convertLongLabelToString(Long label){
		  String labs = "";
		  for(int i=numberOfDatasets-1;i>=0;i--){
			  int icomp = (int)(label/Math.pow(100, i));
			  if(icomp!=0)
				  labs = ""+(i+1)+"IC"+icomp+"_"+labs;
			  label = label - icomp*(long)Math.pow(100, i);
		  }
		  if(labs.endsWith("_"))
			  labs = labs.substring(0, labs.length()-1);
		  return labs;
	  }
	  
	  public static int getNumberOfEdges(Long pc, HashMap<Long, Float> pairs){
		  int nedges = 0;
		  long allpairs[] = getAllPairs(pc);
		  for(int i=0;i<allpairs.length;i++)
			  if(pairs.get(allpairs[i])!=null)
				  nedges++;
		  return nedges;
	  }
	  
	  public static boolean checkIfConnected(Long pc, HashMap<Long, Float> pairs, HashMap<String,Set<String>> neighbours){
		  Set<String> nodes = getNodes(pc);
		  String node1 = nodes.iterator().next();
		  Set<String> connectedSet = new HashSet<String>();
		  getConnectedSet(node1,nodes,neighbours,connectedSet);
		  return connectedSet.size()==nodes.size();
	  }
	  
	  public static Set<String> getNodes(long pc){
		  String labels = convertLongLabelToString(pc);
		  StringTokenizer st = new StringTokenizer(labels,"_");
		  Set<String> nodes = new HashSet<String>();
		  while(st.hasMoreTokens())
			  nodes.add(st.nextToken());
		  return nodes;
	  }
	  
	  public static void getConnectedSet(String node1, Set<String> nodes, HashMap<String,Set<String>> neighbours, Set<String> connSet){
		  Set<String> neigh = neighbours.get(node1);
		  if(neigh!=null){
		  Iterator<String> it = neigh.iterator();
		  while(it.hasNext()){
			  String next = it.next();
			  if(nodes.contains(next))if(!connSet.contains(next)){
				  connSet.add(next);
				  getConnectedSet(next, nodes, neighbours, connSet);
			  }
		  }}
	  }
	  
	  public static boolean checkIfIncluded(long label, Vector<Long> pcs){
		  boolean included = false;
		  Set<String> nodes = getNodes(label);
		  for(int i=0;i<pcs.size();i++)if(label!=pcs.get(i)){
			  Set<String> nodesPC = getNodes(pcs.get(i));
			  if(nodesPC.containsAll(nodes)){
				  included = true;
				  break;
			  }
		  }
		  return included;
	  }
	  
	  public static int sizeOfIntersection(long label1, long label2){
		  Set<String> nodes1 = getNodes(label1);
		  Set<String> nodes2 = getNodes(label2);
		  int count = 0;
		  Iterator<String> it = nodes1.iterator();
		  while(it.hasNext())
			  if(nodes2.contains(it.next()))
				  count++;
		  return count;
	  }
	  
	  
	  
	  
	  public static long[] getAllPairs(Long label){
		  int comps[] = new int[numberOfDatasets];
		  for(int i=numberOfDatasets-1;i>=0;i--){
			  int icomp = (int)(label/Math.pow(100, i));
			  comps[i] = icomp;
			  label = label - icomp*(long)Math.pow(100, i);
		  }
		  Vector<Long> prsV = new Vector<Long>();
		  for(int i=0;i<numberOfDatasets;i++)
			  for(int j=i+1;j<numberOfDatasets;j++){
				  if((comps[i]!=0)&&(comps[j]!=0))
					  prsV.add(comps[i]*(long)Math.pow(100, i)+comps[j]*(long)Math.pow(100, j));
			  }
		  long prs[] = new long[prsV.size()];
		  for(int i=0;i<prs.length;i++)
			  prs[i] = prsV.get(i);
		  return prs;
	  }
	  
	  public static void permuteCorrelations(HashMap<Long, Float> pairs, int numberOfPermutations){
		  Random r = new Random();
		  Iterator<Long> it = pairs.keySet().iterator();
		  Vector<Long> keys = new Vector<Long>();
		  while(it.hasNext())
			  keys.add(it.next());
		  for(int i=0;i<numberOfPermutations;i++){
			  int ik = r.nextInt(pairs.size());
			  int jk = r.nextInt(pairs.size());
			  Float fik = pairs.get(keys.get(ik));
			  pairs.put(keys.get(ik), pairs.get(keys.get(jk)));
			  pairs.put(keys.get(jk), fik);
		  }
	  }
	  
	  public static void filterIntersectingPseudoCliques(Vector<Long> pseudocliques, Vector<Float> scores, int ind[], int intersection){
		  Vector<Long> newPseudocliques = new Vector<Long>();
		  Vector<Float> newScores = new Vector<Float>();
		  for(int i=ind.length-1;i>=0;i--){
			  newPseudocliques.add(pseudocliques.get(ind[i]));
			  newScores.add(scores.get(ind[i]));
		  }
		  int k = newPseudocliques.size()-1;
		  while(k>0){
			  long label = newPseudocliques.get(k);
			  boolean foundBetter = false;
			  for(int i=k-1;i>=0;i--){
				  long toCheck = newPseudocliques.get(i);
				  int is = sizeOfIntersection(label,toCheck);
				  if(is>=intersection){
					  foundBetter = true;
					  break;
				  }
			  }
			  if(foundBetter){
				  newPseudocliques.remove(k);
				  newScores.remove(k);
			  }
			  k--;
		  }
		  pseudocliques.clear();
		  scores.clear();
		  for(int i=0;i<newPseudocliques.size();i++){
			  pseudocliques.add(newPseudocliques.get(i));
			  scores.add(newScores.get(i));
		  }
	  }
}
