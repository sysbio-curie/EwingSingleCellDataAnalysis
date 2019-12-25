package getools.scripts;

import java.io.FileWriter;
import java.util.Collections;
import java.util.Vector;

import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;

public class NaviCellDataPreparation {

	String folder = "";
	String fileName = "";
	String sampleList = "";
	String prefix = "";
	
	public static void main(String[] args) {
		try{
			
			NaviCellDataPreparation ncp = new NaviCellDataPreparation();
			
			ncp.folder = "C:/Datas/OvarianCancer/TCGA/expression/example_navicell/initial/";
			//ncp.fileName = "mRNAexpr_RNAseqRPKM_TCGA_duplOut.txt";
			ncp.sampleList = "selected_samples.txt";
			ncp.prefix = "ovca";
			
			//ncp.prepareExpression("mRNAexpr_RNAseqRPKM_TCGA_duplOut.txt");
			//ncp.prepareCopyNumber("Copy.txt");
			ncp.prepareMutations("somatic_mutations.mut");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void prepareExpression(String fn) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
		Vector<String> list = new Vector<String>();
		list.add("GENE");
		if(!sampleList.equals("")){
			Vector<String> list1 = Utils.loadStringListFromFile(folder+sampleList);
			for(String s: list1){
				if(vt.fieldNumByName(s)>=0)
					list.add(s);
				else
					System.out.println(s+" field NOT FOUND");
			}
		}else{
			for(int i=1;i<vt.fieldNames.length;i++) list.add(vt.fieldNames[i]);
		}
		for(int i=1;i<vt.colCount;i++)
			vt.fieldTypes[i] = vt.NUMERICAL;
		VDatReadWrite.numberOfDigitsToKeep = 2;
		VDatReadWrite.useQuotesEverywhere = false;
		vt = VSimpleProcedures.SelectColumns(vt, list);
		for(int i=0;i<vt.rowCount;i++){
			for(int j=1;j<vt.colCount;j++){
				float f = Float.parseFloat(vt.stringTable[i][j]);
				if(f>0f)
					f = (float)Math.log10(f);
				vt.stringTable[i][j] = ""+f;
			}
		}
		vt = TableUtils.normalizeVDat(vt, true, false);
		 VDatReadWrite.saveToSimpleDatFile(vt, folder+prefix+"_expression.txt");
	}
	
	public void prepareCopyNumber(String fn){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+fn, true, "\t");
		Vector<String> list = new Vector<String>();
		list.add("GENE");
		if(!sampleList.equals("")){
			Vector<String> list1 = Utils.loadStringListFromFile(folder+sampleList);
			for(String s: list1){
				if(vt.fieldNumByName(s)>=0)
					list.add(s);
				else
					System.out.println(s+" field NOT FOUND");
			}
		}else{
			for(int i=1;i<vt.fieldNames.length;i++) list.add(vt.fieldNames[i]);
		}
		for(int i=1;i<vt.colCount;i++)
			vt.fieldTypes[i] = vt.NUMERICAL;
		VDatReadWrite.numberOfDigitsToKeep = 2;
		VDatReadWrite.useQuotesEverywhere = false;
		vt = VSimpleProcedures.SelectColumns(vt, list);
		VDatReadWrite.saveToSimpleDatFile(vt, folder+prefix+"_copynumber.txt");
	}
	
	public void prepareMutations(String fn) throws Exception{
		VDataTable vt = ConvertMutFileToTable(folder+fn);
		Vector<String> list = new Vector<String>();
		list.add("GENE");
		if(!sampleList.equals("")){
			Vector<String> list1 = Utils.loadStringListFromFile(folder+sampleList);
			for(String s: list1){
				if(vt.fieldNumByName(s)>=0)
					list.add(s);
				else
					System.out.println(s+" field NOT FOUND");
			}
		}else{
			for(int i=1;i<vt.fieldNames.length;i++) list.add(vt.fieldNames[i]);
		}
		for(int i=1;i<vt.colCount;i++)
			vt.fieldTypes[i] = vt.NUMERICAL;
		VDatReadWrite.numberOfDigitsToKeep = 2;
		VDatReadWrite.useQuotesEverywhere = false;
		vt = VSimpleProcedures.SelectColumns(vt, list);
		VDatReadWrite.saveToSimpleDatFile(vt, folder+prefix+"_mutations.txt");
	}
	
	public static VDataTable ConvertMutFileToTable(String fn) throws Exception{
		String geneNameField = "Hugo_Symbol";
		String sampleField = "BARCODE";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
		Vector<String> geneList = new Vector<String>();
		Vector<String> sampleList = new Vector<String>();
		for(int i=0;i<vt.rowCount;i++){
			String gene = vt.stringTable[i][vt.fieldNumByName(geneNameField)];
			String sample = vt.stringTable[i][vt.fieldNumByName(sampleField)];
			if(!geneList.contains(gene)) geneList.add(gene);
			if(!sampleList.contains(sample)) sampleList.add(sample);
		}
		Collections.sort(geneList);
		Collections.sort(sampleList);
		int mutTable[][] = new int[geneList.size()][sampleList.size()];
		for(int i=0;i<vt.rowCount;i++){
			String gene = vt.stringTable[i][vt.fieldNumByName(geneNameField)];
			String sample = vt.stringTable[i][vt.fieldNumByName(sampleField)];
			mutTable[geneList.indexOf(gene)][sampleList.indexOf(sample)] = 1;
		}
		FileWriter fw = new FileWriter(fn+".txt");
		fw.write("GENE\t");
		for(int i=0;i<sampleList.size();i++)
			fw.write(sampleList.get(i)+"\t");
		fw.write("\n");
		for(int i=0;i<geneList.size();i++){
			String gene = geneList.get(i);
			fw.write(gene+"\t");
			for(int j=0;j<sampleList.size();j++)
				fw.write(mutTable[i][j]+"\t");
			fw.write("\n");
		}
		fw.close();
		VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(fn+".txt", true, "\t");
		return vt1;
	}

}
