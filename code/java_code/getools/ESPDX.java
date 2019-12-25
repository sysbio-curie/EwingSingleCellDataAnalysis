import getools.scripts.MOSAICAnalysis;

public class ESPDX {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			boolean doQC = false;
			boolean doAnalysis = false;
			boolean doSPRING = false;
			boolean doModuleScores = false;
			
			for(int i=0;i<args.length;i++){
				if(args[i].equals("--dataFolder"))
					MOSAICAnalysis.dataFolder = args[i+1];
				if(args[i].equals("--prefix"))
					MOSAICAnalysis.prefix = args[i+1];
				if(args[i].equals("--kNeighbours"))
					MOSAICAnalysis.kNeighbours = Integer.parseInt(args[i+1]);
				if(args[i].equals("--mit_perc_score"))
					MOSAICAnalysis.mit_perc_score = Float.parseFloat(args[i+1]);
				if(args[i].equals("--minReadCounts"))
					MOSAICAnalysis.minReadCounts = Integer.parseInt(args[i+1]);
				if(args[i].equals("--maxReadCounts"))
					MOSAICAnalysis.maxReadCounts = Integer.parseInt(args[i+1]);
				if(args[i].equals("--signatureDefinitionFile"))
					MOSAICAnalysis.signatureDefinitionFile = args[i+1];
				if(args[i].equals("--matlabCodeFolder"))
					MOSAICAnalysis.matlabCodeFolder = args[i+1];
				if(args[i].equals("--doQC"))
					doQC = true;
				if(args[i].equals("--doAnalysis"))
					doAnalysis = true; 
				if(args[i].equals("--doSPRING"))
					doSPRING = true; 
				if(args[i].equals("--doModuleScores"))
					doModuleScores = true; 
				
			} 
			
			if(doQC)
				MOSAICAnalysis.ProcessPDXDataset_qc(MOSAICAnalysis.dataFolder,MOSAICAnalysis.prefix+".txt",0,0,MOSAICAnalysis.mit_perc_score,MOSAICAnalysis.minReadCounts,MOSAICAnalysis.maxReadCounts);
			//ProcessPDXDataset_qc(folder,pdx+".txt",naive_mit_score,pooled_mit_score,100f,1000,40000);
			if(doAnalysis)
				MOSAICAnalysis.ProcessPDXDataset_process(MOSAICAnalysis.dataFolder,MOSAICAnalysis.prefix+".txt");
			if(doSPRING)
				MOSAICAnalysis.ProcessPDXDataset_SPRING(MOSAICAnalysis.dataFolder,MOSAICAnalysis.prefix+".txt",MOSAICAnalysis.kNeighbours);
			if(doModuleScores)
				MOSAICAnalysis.ProcessPDXDataset_recomputeModuleScores(MOSAICAnalysis.dataFolder,MOSAICAnalysis.prefix+".txt");
	
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
