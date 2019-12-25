package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.data.io.VDatReadWrite;


public class ProcessHouseHold {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			String path = "c:/datas/gorban/";
			String prefix = "household_nogaps";
			VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(path+prefix+".txt", true, "\t");
			
			/*for(int i=0;i<vt.rowCount;i++){
				int ndots = 0;
				for(int j=0;j<vt.colCount;j++)
					if(vt.stringTable[i][j].trim().equals("."))
						ndots++;
				System.out.println(ndots);
				
			}*/
			
			for(int i=8;i<vt.colCount;i++)
				vt.fieldTypes[i] = vt.NUMERICAL;
			vt.rowCount = vt.rowCount-12;
			VDatReadWrite.saveToVDatFile(vt, path+prefix+".dat");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
