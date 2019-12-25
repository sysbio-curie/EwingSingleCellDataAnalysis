package getools;

import java.util.*;
import java.io.*;

import vdaoengine.utils.VSimpleFunctions;

public class Metagene extends GESignature {

  public Vector<Float> weights = null;
  public Vector<Boolean> weightSpecified = null;  
  public Vector<String> sampleNames = null;
  public Vector samplePattern = null;
  
  public HashMap<String, Float> map = new HashMap<String, Float>();
  
  public float activity = 1f;

  public Metagene() {
  }

  public Metagene(GESignature gs) {
    for(int i=0;i<gs.probeSets.size();i++){
      probeSets.add((String)gs.probeSets.elementAt(i));
    }
    for(int i=0;i<gs.geneNames.size();i++){
    	geneNames.add((String)gs.geneNames.elementAt(i));
    }
    name = gs.name;
  }

  public void initializeWeightsByOnes(){
    weights = new Vector();
    weightSpecified = new Vector();
    for(int i=0;i<probeSets.size();i++){
      Float f = new Float(1f);
      weights.add(f);
      weightSpecified.add(new Boolean(false));
    }
  }

  public void initializeWeightsByZeros(){
    weights = new Vector();
    weightSpecified = new Vector();    
    for(int i=0;i<probeSets.size();i++){
      Float f = new Float(0f);
      weights.add(f);
      weightSpecified.add(new Boolean(false));
    }
  }

  public void saveToFile(String fn){
    try{
      FileWriter fw = new FileWriter(fn);
      for(int i=0;i<probeSets.size();i++){
        fw.write(probeSets.elementAt(i)+"\t"+((Float)weights.elementAt(i)).floatValue()+"\r\n");
      }
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
 
	public void makeMap(){
		for(int i=0;i<geneNames.size();i++){
			map.put(geneNames.get(i),weights.get(i));
		}
	}
  
	public static float CalcCorrelation(Metagene m1, Metagene m2){
		float corr = 0;
		m1.makeMap();
		m2.makeMap();
		Vector<Float> val1v = new Vector<Float>();
		Vector<Float> val2v = new Vector<Float>();
		Set<String> keys = m1.map.keySet();
		for(String s: keys){
			if(m2.map.get(s)!=null){
				val1v.add(m1.map.get(s));
				val2v.add(m2.map.get(s));
			}
		}
		float val1[] = new float[val1v.size()]; for(int i=0;i<val1v.size();i++) val1[i] = val1v.get(i);
		float val2[] = new float[val2v.size()]; for(int i=0;i<val2v.size();i++) val2[i] = val2v.get(i);
		corr = VSimpleFunctions.calcCorrelationCoeff(val1, val2);
		return corr;
	}
	
	public void normalizeWeights(){
		float vals[] = new float[weights.size()];
		float length = 0f;
		for(int i=0;i<weights.size();i++){
			vals[i] = weights.get(i);
			length+=vals[i]*vals[i];
		}
		length = (float)Math.sqrt(length);
		for(int i=0;i<weights.size();i++){
			weights.set(i, weights.get(i)/length);
		}
	}
	
	public void normalizeWeightsOnZeroCenteredStandardDeviation(){
		float vals[] = new float[weights.size()];
		float length = 0f;
		for(int i=0;i<weights.size();i++){
			vals[i] = weights.get(i);
		}
		float stdvar = VSimpleFunctions.calcZeroCenteredStandardDeviation(vals);
		for(int i=0;i<weights.size();i++){
			weights.set(i, weights.get(i)/stdvar);
		}
	}
	
	
	public void makeHeavyTailPositive(float thresh){
		float sumPositive = 0f;
		float sumNegative = 0f;
		for(int i=0;i<weights.size();i++){
			float w = weights.get(i);
			if(w>thresh) sumPositive+=w;
			if(w<-thresh) sumNegative+=w;
		}
		if(Math.abs(sumNegative)>Math.abs(sumPositive))
		for(int i=0;i<weights.size();i++){
			weights.set(i, -weights.get(i));
		}
	}
  


}