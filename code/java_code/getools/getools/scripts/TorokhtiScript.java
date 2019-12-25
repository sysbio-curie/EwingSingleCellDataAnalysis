package getools.scripts;

import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.VSimpleProcedures;

public class TorokhtiScript {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			//MethylomeGeneProjection("C:/Datas/Torokhti/Task2/initial_data/methylome.txt","C:/Datas/Torokhti/Task2/initial_data/methylome_annotation.txt");

			VDatReadWrite.writeNumberOfColumnsRows = false;
			VDatReadWrite.useQuotesEverywhere = false;

			
			Vector<String> fnames = new Vector<String>();
			//fnames.add("C:/Datas/Torokhti/Task1/initial_data/affy.txt");
			//fnames.add("C:/Datas/Torokhti/Task1/initial_data/agilent.txt");
			//fnames.add("C:/Datas/Torokhti/Task1/initial_data/rnaseq.txt");
			//fnames.add("C:/Datas/NaviCell2.2/ovca_tcga/ovca_copynumber.txt");
			//fnames.add("C:/Datas/NaviCell2.2/ovca_tcga/ovca_mutations.txt");
			//fnames.add("C:/Datas/NaviCell2.2/ovca_tcga/ovca_expression.txt");
			//fnames.add("C:/Datas/Torokhti/Task2/initial_data/genome.txt");
			//fnames.add("C:/Datas/Torokhti/Task2/initial_data/methylome_gene.txt");
			//fnames.add("C:/Datas/Torokhti/Task2/initial_data/rnaseq.txt");
			//SelectIntersectionOfRowsAndColumns(fnames);
			
			/*int number = 2500;
			CenterLogSelect("C:/Datas/Torokhti/Task1/prepared_data/affy_commonselection.txt",true,false,number);
			CenterLogSelect("C:/Datas/Torokhti/Task1/prepared_data/agilent_commonselection.txt",true,false,number);
			CenterLogSelect("C:/Datas/Torokhti/Task1/prepared_data/rnaseq_commonselection.txt",true,true,number);
			

			fnames.add("C:/Datas/Torokhti/Task1/prepared_data/affy_commonselection_processed_selected"+number+".txt");
			fnames.add("C:/Datas/Torokhti/Task1/prepared_data/agilent_commonselection_processed_selected"+number+".txt ");
			fnames.add("C:/Datas/Torokhti/Task1/prepared_data/rnaseq_commonselection_processed_selected"+number+".txt ");
			SelectIntersectionOfRowsAndColumns(fnames);*/

			int number = 5000;
			CenterLogSelect("C:/Datas/Torokhti/Task2/prepared_data/genome_commonselection.txt",false,false,number);
			CenterLogSelect("C:/Datas/Torokhti/Task2/prepared_data/methylome_gene_commonselection.txt",true,false,number);
			CenterLogSelect("C:/Datas/Torokhti/Task2/prepared_data/rnaseq_commonselection.txt",true,true,number);
			

			fnames.add("C:/Datas/Torokhti/Task2/prepared_data/genome_commonselection_processed_selected"+number+".txt");
			fnames.add("C:/Datas/Torokhti/Task2/prepared_data/methylome_gene_commonselection_processed_selected"+number+".txt ");
			fnames.add("C:/Datas/Torokhti/Task2/prepared_data/rnaseq_commonselection_processed_selected"+number+".txt ");
			SelectIntersectionOfRowsAndColumns(fnames);			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void MethylomeGeneProjection(String fn, String annot) throws Exception{
		HashMap<String, Vector<String>> name2id = new HashMap<String, Vector<String>>();
		VDataTable ann = VDatReadWrite.LoadFromSimpleDatFile(annot, true, "\t");
		ann.makePrimaryHash(ann.fieldNames[0]);
		System.out.println("Annotation loaded");
		
		VDataTable table = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t",true);
		table.makePrimaryHash(table.fieldNames[0]);
		
		System.out.println("Data loaded");
		
		for(int i=0;i<ann.rowCount;i++){
			String id = ann.stringTable[i][0];
			String names[] = ann.stringTable[i][1].split(";");
			for(int j=0;j<names.length;j++){
				String name = names[j];
				Vector<String> ids = name2id.get(name);
				if(ids==null) ids=new Vector<String>();
				ids.add(id);
				name2id.put(name, ids);
			}
		}
		
		Vector<String> names = new Vector<String>();
		for(String s:name2id.keySet()) names.add(s);
		
		System.out.println("Found gene names "+names.size());
		
		FileWriter fw = new FileWriter(fn.substring(0,fn.length()-4)+"_gene.txt");
		fw.write("GENE\t");
		for(int i=1;i<table.fieldNames.length;i++) fw.write(table.fieldNames[i]+"\t");
		fw.write("\n");
		int l=0;
		for(String gn: names){
			//System.out.println(""+(l++));
			int numberOfLines = 0;
			float vector[] = new float[table.fieldNames.length-1];
			Vector<String> ids = name2id.get(gn);
			for(String s: ids){
				boolean iscomplete = true;
				int row = table.tableHashPrimary.get(s).get(0);
				for(int k=1;k<table.fieldNames.length;k++) if(table.stringTable[row][k].equals("NA")){ iscomplete = false; break; }
				if(iscomplete){
					numberOfLines++;
					for(int k=1;k<table.fieldNames.length;k++) vector[k-1] += Float.parseFloat(table.stringTable[row][k]);
					if(gn.equals("TP53")){
						System.out.print(s+"\t"); for(int k=1;k<table.fieldNames.length;k++) System.out.print(table.stringTable[row][k]+"\t"); System.out.println();
					}
				}
			}
			if(numberOfLines>0){
				for(int k=1;k<table.fieldNames.length;k++) vector[k-1] /= (float)numberOfLines;
				fw.write(gn+"\t");
				for(int k=1;k<table.fieldNames.length;k++) fw.write(vector[k-1]+"\t");
				fw.write("\n");
			}
		}
		fw.close();
		
	}
	
	public static void SelectIntersectionOfRowsAndColumns(Vector<String> fnames){
		Vector<VDataTable> tables = new Vector<VDataTable>();
		for(String fn: fnames){
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t", true);
			vt.makePrimaryHash(vt.fieldNames[0]);
			tables.add(vt);
		}
		Vector<String> geneList = new Vector<String>();
		Vector<String> columnList = new Vector<String>();
		
		
		for(int i=0;i<tables.get(0).rowCount;i++){
			String gname = tables.get(0).stringTable[i][0];
			boolean genefound = true;
			for(int j=1;j<tables.size();j++)
				if(tables.get(j).tableHashPrimary.get(gname)==null){
					genefound = false;
				}else{
					for(int k=1;k<tables.get(j).colCount;k++)
						if(tables.get(j).stringTable[tables.get(j).tableHashPrimary.get(gname).get(0)][k].equals("NA")) genefound = false;
				}
			if(genefound) geneList.add(gname);
		}
		System.out.println("Found common genes "+geneList.size());
		
		for(int i=1;i<tables.get(0).colCount;i++){
			String colname = tables.get(0).fieldNames[i];
			boolean colfound = true;
			for(int j=1;j<tables.size();j++)
				if(tables.get(j).fieldNumByName(colname)==-1)
					colfound = false;
			if(colfound) columnList.add(colname);
		}
		System.out.println("Found common samples "+columnList.size());
		
		Collections.sort(geneList);
		Collections.sort(columnList);
		
		//for(String s: columnList) System.out.println(s);
		
		for(int i=0;i<fnames.size();i++){
			String fn = fnames.get(i);
			String fn1 = fn.substring(0, fn.length()-4)+"_commonselection.txt";
			VDataTable vt = VSimpleProcedures.selectRowsFromList(tables.get(i),geneList);
			Vector<String> columnList1 = new Vector<String>();
			columnList1.add(tables.get(i).fieldNames[0]);
			for(int j=0;j<columnList.size();j++)
				columnList1.add(columnList.get(j));
			vt = VSimpleProcedures.SelectColumns(vt,columnList1);
			VDatReadWrite.saveToSimpleDatFile(vt, fn1);
		}

		
	}
	
	public static void CenterLogSelect(String fn, boolean center, boolean log, int numberOfLinesToSelect) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t", true);
		VSimpleProcedures.findAllNumericalColumns(vt);
		
		/*for(int i=0;i<vt.rowCount;i++)
			for(int j=1;j<vt.colCount;j++){
				if(vt.stringTable[i][j].contains("@")){
					System.out.println("Row "+i+"\t"+vt.stringTable[i][0]);
					for(int k=1;k<vt.colCount;k++)
						System.out.print(vt.stringTable[i][k]+"\t");
					System.out.println();
					break;
				}
			}
		*/
		
		if(log){
			for(int i=0;i<vt.rowCount;i++)
				for(int j=1;j<vt.colCount;j++){
					float val = Float.parseFloat(vt.stringTable[i][j]);
					vt.stringTable[i][j] = ""+((float)Math.log((double)val+1)/Math.log(2)); 
				}
		}
		if(center)
			vt = TableUtils.normalizeVDat(vt, true, false);
		VDatReadWrite.saveToSimpleDatFile(vt,fn.substring(0, fn.length()-4)+"_processed.txt");
		if(numberOfLinesToSelect>0)
			vt = TableUtils.filterByVariation(vt, numberOfLinesToSelect, false);
		VDatReadWrite.saveToSimpleDatFile(vt,fn.substring(0, fn.length()-4)+"_processed_selected"+numberOfLinesToSelect+".txt");
		
	}
	
	

}
