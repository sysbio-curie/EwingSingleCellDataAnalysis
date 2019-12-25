package vdaoengine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
 
public class UnZip
{
    List<String> fileList;
 
    public static void main( String[] args )
    {
    	try{
    	
    	UnZip unZip = new UnZip();
    	//unZip.unZipIt("C:/Datas/MOSAIC/viewer/test/ewing_96.zip","C:/Datas/MOSAIC/viewer/test/");
    	String s = Utils.loadString(new FileInputStream("C:/Datas/MOSAIC/viewer/test/ewing_96.zip"));
    	
    	FileWriter fw = new FileWriter("C:/Datas/MOSAIC/viewer/test/test.dat");
    	s = unZip.unZipIt(s);
    	fw.write(s);
    	fw.close();
    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
 
    
    public String unZipIt(String zippedString){
    	StringBuffer unzsb = new StringBuffer();
        byte[] buffer = new byte[1024];
        
        try{
    
       	//get the zip file content
       	ZipInputStream zis = 
       		new ZipInputStream(new StringBufferInputStream(zippedString));
       	//get the zipped file list entry
       	ZipEntry ze = zis.getNextEntry();
    
       	while(ze!=null){
    
               int len;
               while ((len = zis.read(buffer)) > 0) {
            	   for(int i=0;i<len;i++)
            	   unzsb.append((char)buffer[i]);
               }

               ze = zis.getNextEntry();
       	}
    
        zis.closeEntry();
       	zis.close();
    
    
       }catch(IOException ex){
          ex.printStackTrace(); 
       }
        	
    	return unzsb.toString();
    }
    
    
    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unZipIt(String zipFile, String outputFolder){
 
     byte[] buffer = new byte[1024];
 
     try{
 
    	//create output directory is not exists
    	File folder = new File(outputFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
 
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);
 
           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
 
            fos.close();   
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
 
    	System.out.println("Done");
 
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }    
}