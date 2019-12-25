package vdaoengine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class ArchiveFolders {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			
			String folder = "C:/Datas/MOSAIC/analysis/gsea/ica/";
			Date tm = new Date();
			System.out.println(folder+"\t"+isOverMax(folder,1000,true)+"\t"+(new Date().getTime()-tm.getTime()));
			folder = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/_METAGENES/gsea/";
			tm = new Date();
			System.out.println(folder+"\t"+isOverMax(folder,1000,true)+"\t"+(new Date().getTime()-tm.getTime()));
			
			 
			
			/*FileWriter fw = new FileWriter("_archive.bat");
			File files[] = (new File(".")).listFiles();
			for(File f: files){
				if(f.isDirectory()){
					
				}
			}
			fw.close();*/
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	public static int isOverMax(String folder, int MAX_FILES, boolean recursive){
	    Path dir = Paths.get(folder);
	    int i = 1;

	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	        for (Path p : stream) {
	            //larger than max files, exit
	        	//System.out.println(""+(i)+"\t"+p);
	        	
	        	if(recursive){
	        		File f = p.toFile();
	        		if(f.isDirectory()){
	        			i+=isOverMax(f.getAbsolutePath(),MAX_FILES,true);
	        		}
	        	}
	        	
	            if (++i > MAX_FILES) {
	                return MAX_FILES;
	            }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return i;
	}
	
	

}
