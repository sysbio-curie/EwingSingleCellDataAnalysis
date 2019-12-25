package vdaoengine;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.VSimpleProcedures;

public class BentelleModelAnalysis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			String path = "C:/Datas/Bentel/";
			String fn = "matrix.txt";
			float probabilitySelection = 0.01f;
			
			selectRandomPortionOfLines(path, fn, probabilitySelection);
			
			selectLinesFromFile(path, fn, 1, 20000);
			
			//VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(tab, firstNum, center, normalization);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void selectRandomPortionOfLines(String path, String fn, float prob) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(path+fn));
		String s = null;
		Random r = new Random();
		FileWriter fw = new FileWriter(path+fn.substring(0, fn.length()-4)+"_f.txt");
		while((s=lr.readLine())!=null){
			if(r.nextFloat()<prob){
				fw.write(s+"\n");
			}
		}
		fw.close();
		lr.close();
	}
	
	public static void selectLinesFromFile(String path, String fn, int start, int end) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(path+fn));
		String s = null;
		Random r = new Random();
		FileWriter fw = new FileWriter(path+fn.substring(0, fn.length()-4)+"_s"+start+"-"+end+".txt");
		int line = 0;
		while((s=lr.readLine())!=null){
			line++;
			if(line>=start)if(line<=end){
				fw.write(s+"\n");
			}
			if(line>end) break;
		}
		fw.close();
		lr.close();
	}
	

}
