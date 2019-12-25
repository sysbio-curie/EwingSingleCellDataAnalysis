package vdaoengine;

import java.io.*;
import java.util.*;

public class ProcessListOfGenes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			//LineNumberReader lr = new LineNumberReader(new FileReader("C:/Datas/HPRD9/pathways/wang_most_variable_genes.txt"));
			LineNumberReader lr = new LineNumberReader(new FileReader("C:/Datas/HPRD9/pathways/wang_basal1.txt"));
			String s = null;
			while((s=lr.readLine())!=null)if(!s.equals("---")){
				StringTokenizer st = new StringTokenizer(s,"_/");
				while(st.hasMoreTokens())
					System.out.println(st.nextToken());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
