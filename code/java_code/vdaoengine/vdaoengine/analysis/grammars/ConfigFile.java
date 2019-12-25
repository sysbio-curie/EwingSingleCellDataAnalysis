package vdaoengine.analysis.grammars;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import vdaoengine.analysis.elmap.*;


public class ConfigFile {

	/**
	 * @param args
	 */
	
	public String algtype = null;
	public String gridtype = null;
	public float stretchInitCoeffs[] = new float[3];
	public int initStrategy = 0;
	public int gridsize1 = 0;
	public int gridsize2 = 0;
	public int gridsize3 = 0;
	public int minNumInCluster	= 0;
	
	public Vector<ElmapAlgorithmEpoch> epochs = new Vector<ElmapAlgorithmEpoch>();

	public static void main(String[] args) {
		try{
			
			String file = "c:/myprograms/vdaoengine/elmap.ini";
			ConfigFile cf = new ConfigFile(); 
			cf.readFile(file,14);
			System.out.println(cf);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ConfigFile(){
	}
	
	public String toString(){
		String s = "";
		s+="algtype = "+algtype+"\n";
		s+="gridtype = "+gridtype+"\n";
		//s+="grammartype = "+grammartype+"\n";
		s+="gridsize1 = "+gridsize1+"\n";
		s+="gridsize2 = "+gridsize2+"\n";
		s+="gridsize3 = "+gridsize3+"\n";
		s+="init.stretch1 = "+stretchInitCoeffs[0]+"\n";
		s+="init.stretch2 = "+stretchInitCoeffs[1]+"\n";
		s+="init.stretch3 = "+stretchInitCoeffs[2]+"\n";
		s+="minNumInCluster = "+minNumInCluster+"\n\n";
		
		for(int i=0;i<epochs.size();i++){
			ElmapAlgorithmEpoch ep = epochs.get(i);
			s+="epoch.id = "+(i+1)+"\n";
			s+="ep.minimize = "+ep.minimize+"\n";
			s+="ep.ep = "+ep.EP+"\n";
			s+="ep.rp = "+ep.RP+"\n";
			s+="ep.eps = "+ep.epsConvergence+"\n";
			s+="ep.epsSLAU = "+ep.epsConvergenceSLAU+"\n";
			s+="ep.numiter = "+ep.numberOfIterations+"\n";
			s+="ep.nuiterSLAU = "+ep.maxNumberOfIterationsForSLAU+"\n";
			s+="ep.extrapolate = "+ep.extrapolate+"\n";
			s+="ep.epsMSE = "+ep.epsMSE+"\n";
			s+="ep.robust = "+ep.robust+"\n";
			s+="ep.trimradius = "+ep.trimradius+"\n";
		}


		return s;
	}
	
	public void readFile(String fn, int number){
	    String s = vdaoengine.utils.IO.loadString(fn);
	    readFromString(s, number);
	}
	
	public void readFromString(String configuration, int number){
		try{
	    epochs = new Vector<ElmapAlgorithmEpoch>();
	    LineNumberReader f = new LineNumberReader(new StringReader(configuration));
	    
	    String st = "#ALG"+number;
	    String s = null;
	    int numberOfSteps = 0;
	    
	    while((s=f.readLine())!=null){
	    	if(s.equals(st))
	    		break;
	    }
	    
	    
	    while((s=f.readLine())!=null)if(!s.trim().equals("")){
	    	Vector<String> pair = split(s);
	    	String tok = pair.get(0);
	    	String tok1 = pair.get(1);

            if(tok.equals("algtype")){
            	algtype=tok1;
        }
        if(tok.equals("grid.type")){
                gridtype=tok1;
        }
        //if(tok.equals("grammar.type")){
        //    	grammartype=tok1;
        //}
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
        if(tok.equals("init.algorithm")){
            initStrategy = Integer.parseInt(tok1);
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
                ep.grammarType = null;
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
        if(tok.equals("epoch.grammar.type")){
        	ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
            ep.grammarType = tok1;
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
        if(tok.equals("epoch.robust")){
            ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
            ep.robust = Boolean.parseBoolean(tok1);
        }
        if(tok.equals("epoch.trimradius")){
            ElmapAlgorithmEpoch ep = (ElmapAlgorithmEpoch)epochs.elementAt(numberOfSteps-1);
            ep.trimradius = Float.parseFloat(tok1);
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
        
        if(tok.startsWith("#ALG")||tok.startsWith("#end"))
	    	break;
	    }
	    
		}catch(Exception e){
			e.printStackTrace();
		}
	    
	}
	
	private Vector<String> split(String statement){
		Vector<String> pair = new Vector<String>();
		StringTokenizer st = new StringTokenizer(statement," =\t");
		pair.add(st.nextToken());
		if(st.hasMoreTokens())
			pair.add(st.nextToken());
		else 
			pair.add("");
		return pair;
	}
	
	

}
