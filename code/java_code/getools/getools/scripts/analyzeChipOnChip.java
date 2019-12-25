package getools.scripts;

import vdaoengine.data.*;
import java.io.*;
import java.util.*; 

public class analyzeChipOnChip {

	public static void main(String[] args) {
		try{
			
			//VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/EWING/CHIPChip/Promoters/All_pair.txt", true, "\t");
			//FileWriter fw = new FileWriter("C:/Datas/EWING/CHIPChip/Promoters/promoter_brut.txt");
			//VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:\\Datas\\EWING\\CHIPChip\\figures\\all_pair.txt", true, "\t");
			//FileWriter fw = new FileWriter("C:/Datas/EWING/CHIPChip/figures/chr22_sft.txt");
			VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:\\Datas\\EWING\\CHIPChip\\analysis\\all.dat");
			FileWriter fw = new FileWriter("C:\\Datas\\EWING\\CHIPChip\\analysis\\sft.txt");
			//VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:\\Datas\\EWING\\CHIPChip\\analysis\\prom.dat");
			//FileWriter fw = new FileWriter("C:\\Datas\\EWING\\CHIPChip\\analysis\\prom.txt");
			
			
			Vector<String> ids = new Vector<String>();
			
			/*ids.add("89179");	
			ids.add("89202");	
			ids.add("89204");	
			ids.add("95809");*/
			
			/*ids.add("66645_norm");
			ids.add("74222_norm");
			ids.add("78948_norm");*/
			
			/*ids.add("89208_532");
			ids.add("89208_635");
			ids.add("98388_532");
			ids.add("98388_635");*/
			
			ids.add("89208");
			ids.add("98388");
			
			/*ids.add("PM_66645_532");
			ids.add("MM_66645_532");
			ids.add("PM_66645_635");
			ids.add("MM_66645_635");
			ids.add("PM_74222_532");
			ids.add("MM_74222_532");
			ids.add("PM_74222_635");
			ids.add("MM_74222_635");
			ids.add("PM_78948_532");
			ids.add("MM_78948_532");
			ids.add("PM_78948_635");
			ids.add("MM_78948_635");*/
			for(int i=0;i<ids.size();i++)
				fw.write(ids.get(i)+"\t");
			fw.write("\n");
			for(int i=0;i<vt.rowCount;i++){
				for(int j=0;j<ids.size();j++)
					fw.write(vt.stringTable[i][vt.fieldNumByName(ids.get(j))]+"\t");
				fw.write("\n");
			}
			fw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
