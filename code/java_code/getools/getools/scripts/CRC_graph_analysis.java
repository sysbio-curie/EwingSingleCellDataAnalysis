package getools.scripts;

import java.io.FileWriter;
import java.util.Vector;

import logic.Utils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;

public class CRC_graph_analysis {

	public static void main(String[] args) {
		try{
			
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE13067");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE13294");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE17536");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE20916");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE2109");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE23878");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE33113");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE35896");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE37892");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","GSE41258");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","TCGACRCga");
			addGeneNameColumn("C:/Datas/ColonCancer/analysis/test_nmf_normalization/S_files/","TCGACRChi");

			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void addGeneNameColumn(String folder, String prefix) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+"_S.txt", false, "\t");
		Vector<String> list = Utils.loadStringListFromFile(folder+prefix+".genes");
		FileWriter fw = new FileWriter(folder+prefix+"_S.xls");
		if(vt.rowCount!=list.size()){
			System.out.println("ERROR!!!!");
			System.exit(0);
		}
		fw.write("GENE\t");
		for(int i=0;i<vt.colCount;i++) fw.write(vt.fieldNames[i]+"\t");
		fw.write("\n");
		for(int i=0;i<vt.rowCount;i++){
			fw.write(list.get(i)+"\t");
			for(int j=0;j<vt.colCount;j++)
				fw.write(vt.stringTable[i][j]+"\t");
			fw.write("\n");
		}
		fw.close();
	}

}
