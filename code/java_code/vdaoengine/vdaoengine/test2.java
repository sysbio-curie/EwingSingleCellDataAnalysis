package vdaoengine;

import java.io.*;
import java.util.*;

public class test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			String file = "C:/Datas/LiquideStructure/hsc3i0b0s040.d";
			LineNumberReader lr = new LineNumberReader(new FileReader(file));
			String s = null;
			
			FileWriter fw = new FileWriter("C:/Datas/LiquideStructure/hsc3i0b0s040.1r");
			while((s=lr.readLine())!=null){
				StringTokenizer st = new StringTokenizer(s," ");
				int n = 1;
				while(st.hasMoreTokens()){
					float rn = Float.parseFloat(st.nextToken());
					rn = 1/rn;
					fw.write(rn+"\t");
					n++;
				}
				fw.write("\n");
			}
			fw.close();
			lr.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
