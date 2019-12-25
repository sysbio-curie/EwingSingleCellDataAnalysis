package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class GSDASimulatedAnalysis {
	public static void main(String args[]){
		try{
			
			String resfile = "";
		    for(int i=0;i<args.length;i++){
		      if(args[i].equals("-res"))
		          resfile = args[i+1];
		    }
		    
		    VDataTable restable = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(resfile,true,"\t");
		    
		    String fname = resfile.substring(0,resfile.length()-4)+"_res."+resfile.substring(resfile.length()-3,resfile.length());		    
		    
		    FileWriter fw = new FileWriter(fname);
		    
		    fw.write("PVALUE\tGnS_Sn\tGnS_Sp\t");
		    
		    for(int i=0;i<restable.colCount;i++){
		    	if(restable.fieldNames[i].startsWith("p"))
		    		fw.write(restable.fieldNames[i]+"_Sn\t"+restable.fieldNames[i]+"_Sp\t");
		    }
		    fw.write("\n");
		    
		    for(double pv=-3;pv<=-0.5;pv+=0.1f){
		    	double pvalue = Math.pow(10, pv);
		    	fw.write(pvalue+"\t");

		    	int tp = 0, tn = 0, fp = 0, fn = 0;
		    	// First, we test rule 'GAP1S is significant but SKEWF is not'
		    	for(int i=0;i<restable.rowCount;i++){
		    		String sigName = restable.stringTable[i][restable.fieldNumByName("NAME")];
		    		float pGAP1S =  Float.parseFloat(restable.stringTable[i][restable.fieldNumByName("pGAP1S")]);
		    		float pSKEWF =  Float.parseFloat(restable.stringTable[i][restable.fieldNumByName("pSKEWF")]);
		    		boolean randomSig = sigName.startsWith("RND")||(sigName.indexOf("SKW")>0);
		    		boolean predictedNonRandom = (pGAP1S<=pvalue)&&(pSKEWF>0.1f);
		    		if(predictedNonRandom&&(!randomSig)) tp++;
		    		if(predictedNonRandom&&randomSig) fp++;
		    		if((!predictedNonRandom)&&(randomSig)) tn++;
		    		if((!predictedNonRandom)&&(!randomSig)) fn++;
		    		//System.out.println(sigName+"\t"+pGAP1S+"\t"+pSKEWF+"\t"+"nonRandom="+predictedNonRandom+"\trandomSig="+randomSig);
		    	}
		    	float sn = 1f*tp/(tp+fn);
		    	float sp = 1f*tp/(tp+fp);
		    	fw.write(""+sn+"\t"+sp+"\t");
		    	//fw.write(""+tp+"\t"+fn+"\t");
		    	
			    for(int i=0;i<restable.colCount;i++){
			    	if(restable.fieldNames[i].startsWith("p")){
			    		String metric = restable.fieldNames[i];
			    		tp = 0; tn = 0; fp = 0; fn = 0;
				    	for(int j=0;j<restable.rowCount;j++){
				    		String sigName = restable.stringTable[j][restable.fieldNumByName("NAME")];
				    		float metricValue =  Float.parseFloat(restable.stringTable[j][restable.fieldNumByName(metric)]);
				    		boolean randomSig = sigName.startsWith("RND")||(sigName.indexOf("SKW")>0);
				    		boolean predictedNonRandom = (metricValue<=pvalue);
				    		if(predictedNonRandom&&!randomSig) tp++;
				    		if(predictedNonRandom&&randomSig) fp++;
				    		if((!predictedNonRandom)&&(randomSig)) tn++;
				    		if((!predictedNonRandom)&&(!randomSig)) fn++;
				    	}
				    	sn = 1f*tp/(tp+fn);
				    	sp = 1f*tp/(tp+fp);
				    	fw.write(""+sn+"\t"+sp+"\t");
			    	}
			    }
		    	
		    
		    fw.write("\n");
		    }
		    
		    fw.flush();
		    fw.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
