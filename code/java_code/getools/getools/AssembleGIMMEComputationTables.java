package getools;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class AssembleGIMMEComputationTables {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			String folder = args[0];
			File f = (new File(folder));
			String prefix = f.getName();
			if(prefix.endsWith("_4GIMME")) prefix = prefix.substring(0, prefix.length()-7);
			
			String complete_list_of_metabolites = args[1];
			
			System.out.println("Prefix = "+prefix);
			
			File fs[] = f.listFiles();
			for(int i=0;i<fs.length;i++)if(fs[i].isDirectory()){
				File part = fs[i];
				System.out.println("   In the folder "+part.getName()+"...");
				int numDirs = 0;
				int numFiles = 0;
				File ls[] = part.listFiles();
				File folderWithFiles = null;
				File dir = null;
				for(File l:ls){
					if(l.isDirectory()) { numDirs++; dir = l;} else numFiles++;
				}
				folderWithFiles = part;
				if(numFiles>100){
					
				}
				if(numDirs==1)if(numFiles<100){
					File files[] = dir.listFiles();
					for(File ff: files){
						FileUtils.copyFile(ff, new File(part.getAbsolutePath()+File.separator+ff.getName()));
						FileUtils.deleteQuietly(ff);
					}
					FileUtils.deleteQuietly(dir);
					
				}
				System.out.println(folderWithFiles.getName());
				//ACSIOMA.assembleInconsistencyScoreTable(folderWithFiles.getAbsolutePath(),"tabla.inconsistencia.txt","Var4",prefix+".ISscore",",",null);
				//ACSIOMA.assembleInconsistencyScoreTable(folderWithFiles.getAbsolutePath(),"tabla.inconsistencia.txt","Var4",prefix+"_full.ISscore",",",complete_list_of_metabolites);
				ACSIOMA.assembleInconsistencyScoreTable(folderWithFiles.getAbsolutePath(),"tabla.inconsistencia.txt","IS_score",prefix+".ISscore",",",null);
				ACSIOMA.assembleInconsistencyScoreTable(folderWithFiles.getAbsolutePath(),"tabla.inconsistencia.txt","IS_score",prefix+"_full.ISscore",",",complete_list_of_metabolites);
				ACSIOMA.assembleInconsistencyScoreTable(folderWithFiles.getAbsolutePath(),"flux",null,prefix+".fluxes","\t",null);
				ACSIOMA.assembleInconsistencyScoreTable(folderWithFiles.getAbsolutePath(),"txt","Var2",prefix+".expmap",",",null);
				
				FileUtils.copyFile(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+".ISscore"), new File(folder+File.separator+prefix+"_"+folderWithFiles.getName()+".ISscore"));
				FileUtils.deleteQuietly(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+".ISscore"));
				FileUtils.copyFile(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+"_full.ISscore"), new File(folder+File.separator+prefix+"_"+folderWithFiles.getName()+"_full.ISscore"));
				FileUtils.deleteQuietly(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+"_full.ISscore"));
				FileUtils.copyFile(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+".fluxes"), new File(folder+File.separator+prefix+"_"+folderWithFiles.getName()+".fluxes"));
				FileUtils.deleteQuietly(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+".fluxes"));
				FileUtils.copyFile(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+".expmap"), new File(folder+File.separator+prefix+"_"+folderWithFiles.getName()+".expmap"));
				FileUtils.deleteQuietly(new File(folderWithFiles.getAbsolutePath()+File.separator+prefix+".expmap"));

			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
