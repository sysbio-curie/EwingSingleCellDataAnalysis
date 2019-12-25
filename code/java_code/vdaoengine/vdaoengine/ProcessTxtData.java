package vdaoengine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.analysis.PCAMethod;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.MetaGene;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class ProcessTxtData {
	
	public static int numberOfDigitsToKeep = 3;

	public static void main(String[] args) {
		try{
			
			//CompileAandSTables("C:/Datas/BIODICA/work/OVCA_ICA/","OVCA_ica"); System.exit(0);
			//CompileAandSTablesAllResults("C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/","Rescue_ica"); System.exit(0);
			//CompileAandSTablesAllResults("C:/Datas/MOSAIC/analysis/ica/metaanalysis/EMTAB/","EMTAB_ica"); System.exit(0);
			//CompileAandSTablesAllResults("C:/Datas/MOSAIC/analysis/ica/metaanalysis/ALLDATA_OLIVIER/","scmeta_ica"); System.exit(0);
			
			VDatReadWrite.writeNumberOfColumnsRows = false;
			VDatReadWrite.useQuotesEverywhere = false;
			
			//System.out.println(Locale.getDefault().toString());

			
			if(args.length>0){
				String fn = args[0];
				VDataTable vt = null;
				if(!args[0].equals("-assembleICAresults"))
				if(args.length>1)if(!args[1].equals("-prepare4ICAFast"))if(!args[1].equals("-selectrowsvarfast"))if(!args[1].equals("-centerfast"))if(!args[1].equals("-replacecolumnfast"))if(!args[1].equals("-s"))if(!args[1].equals("-selectcolumnsfromfilefast"))if(!args[1].equals("-subtractvaluefast"))if(!args[1].equals("-reducedigits"))if(!args[1].equals("-selectuniquebyvarfast"))if(!args[1].equals("-decomposebycolumns"))if(!args[1].equals("-mergefilefast"))if(!args[1].contains("fast")){
					vt = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t", true);
					VSimpleProcedures.findAllNumericalColumns(vt);
				}
				for(int i=0;i<args.length;i++)if(args[i]!=null){
					if(args[i].equals("-logx1")){
						System.out.println("Convert to logarithmic scale x'<-log10(x+1)...");
						for(int k=0;k<vt.rowCount;k++)
							for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
								float v = Float.parseFloat(vt.stringTable[k][j]);
								v = (float)Math.log10((double)v+1);
							      String fs = "#.";
							      for(int l=0;l<numberOfDigitsToKeep;l++)
							        fs+="#";
							      DecimalFormat df = new DecimalFormat(fs);
							      String s = df.format(v);
								vt.stringTable[k][j] = s;
							}
					}
					if(args[i].equals("-logx")){
						System.out.println("Convert to logarithmic scale x'<-log10(x)...");
						for(int k=0;k<vt.rowCount;k++)
							for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
								float v = Float.parseFloat(vt.stringTable[k][j]);
								v = (float)Math.log10((double)v);
							      String fs = "#.";
							      for(int l=0;l<numberOfDigitsToKeep;l++)
							        fs+="#";
							      DecimalFormat df = new DecimalFormat(fs);
							      String s = df.format(v);
								vt.stringTable[k][j] = s;
							}
					}
					if(args[i].equals("-logxfast")){
						System.out.println("Convert to logarithmic scale x'<-log10(x) (fast version)...");
						LogXFast(fn,numberOfDigitsToKeep);
					}
					if(args[i].equals("-dividebycolumnsum")){
						System.out.println("Computing column sums...");
						Vector<Float> sums = new Vector<Float>();
						for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
							sums.add(0f);
						}
						for(int k=0;k<vt.rowCount;k++){
							int col = 0;
							for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
								float v = Float.parseFloat(vt.stringTable[k][j]);
								sums.set(col,sums.get(col)+v);
								col++;
							}
						}
						float vals[] = new float[sums.size()];
						for(int l=0;l<vals.length;l++) vals[l] = sums.get(l);
						float med = VSimpleFunctions.calcMedian(vals);
						System.out.println("Median column sum = "+med);
						System.out.println("Dividing values by sums in columns...");
						for(int k=0;k<vt.rowCount;k++){
							int col = 0;
							for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
								float v = Float.parseFloat(vt.stringTable[k][j]);
								v/=sums.get(col);
							      /*String fs = "#.";
							      for(int l=0;l<numberOfDigitsToKeep;l++)
							        fs+="#";
							    DecimalFormat df = new DecimalFormat(fs);
							    String s = df.format(v);*/
								vt.stringTable[k][j] = ""+v;
								col++;
							}
						}
					}
					if(args[i].equals("-log10")){
						float scalefactor = Float.parseFloat(args[i+1]);
						System.out.println("Convert to logarithmic scale x'<-log10(x*"+scalefactor+"+1)...");
						for(int k=0;k<vt.rowCount;k++)
							for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
								float v = Float.parseFloat(vt.stringTable[k][j]);
								v = (float)Math.log10((double)v*scalefactor+1);
							      String fs = "#.";
							      for(int l=0;l<numberOfDigitsToKeep;l++)
							        fs+="#";
							      DecimalFormat df = new DecimalFormat(fs);
							      String s = df.format(v);
								vt.stringTable[k][j] = s;
							}
					}
					if(args[i].equals("-log2x1")){
						System.out.println("Convert to logarithmic scale x'<-log2(x+1)...");
						for(int k=0;k<vt.rowCount;k++)
							for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
								float v = Float.parseFloat(vt.stringTable[k][j]);
								v = (float)(Math.log10((double)v+1)/Math.log10(2));
							      String fs = "#.";
							      for(int l=0;l<numberOfDigitsToKeep;l++)
							        fs+="#";
							      DecimalFormat df = new DecimalFormat(fs);
							      String s = df.format(v);
								vt.stringTable[k][j] = s;
							}
					}
					if(args[i].equals("-filtersum")){
						System.out.println("Filtering by sum of values in a row...");
						float val = Float.parseFloat(args[i+1]);
					}
					if(args[i].equals("-center")){
						System.out.println("Centering...");
						vt = VSimpleProcedures.normalizeVDat(vt, true, false);
					}
					if(args[i].equals("-centerfast")){
						System.out.println("Centering (fast version)...");
						CenterFast(fn);
					}
					if(args[i].equals("-subtractvaluefast")){
						float val = Float.parseFloat(args[i+1]);
						System.out.println("Subtracting "+val+ " from each value (fast)...");
						SubtractValueFast(fn,val);
					}
					if(args[i].equals("-doublecenter")){
						System.out.println("Double centering...");
						VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
						float mas[][] = TableUtils.doubleCenterMatrix(vd.massif);
						for(int l=0;l<vt.rowCount;l++){
							int k=0;
							for(int s=0;s<vt.colCount;s++){
								if(vt.fieldTypes[s]==vt.NUMERICAL){
									vt.stringTable[l][s] = ""+mas[l][k];
									k++;
								}
							}
						}
					}
					if(args[i].equals("-selectrowsvarfast")){
						System.out.println("Selecting most variable "+args[i+1]+" rows... (fast version)");
						//vt = TableUtils.filterByVariation(vt, Integer.parseInt(args[i+1]), false);
						int numgenes = Integer.parseInt(args[i+1]);
						filterByVariationFast(fn,numgenes);
						
					}
					if(args[i].equals("-selectrowsvar")){
						System.out.println("Selecting most variable "+args[i+1]+" rows...");
						vt = TableUtils.filterByVariation(vt, Integer.parseInt(args[i+1]), false);
					}
					if(args[i].equals("-selectuniquebyvar")){
						System.out.println("Selecting unique rows id based on variance...");
						vt = TableUtils.selectUniqueRowsIdsByVariance(vt, false);
						System.out.println(vt.rowCount+" were selected.");
					}
					if(args[i].equals("-selectuniquebyvarfast")){
						System.out.println("Selecting unique rows id based on variance (fast version)...");
						int numLines = SelectUniqueByVariationFast(fn);
						System.out.println(numLines+" were selected.");
					}
					if(args[i].equals("-selectuniquebymaxabs")){
						System.out.println("Selecting unique rows id based on maximum abs value in the vector...");
						vt = TableUtils.selectUniqueRowsIdsByMaxAbs(vt);
						System.out.println(vt.rowCount+" were selected.");
					}
					if(args[i].equals("-selectrowsfromfile")){
						String filen = args[i+1];
						System.out.println("Selecting rows from file..."+filen);
						Vector<String> lines = Utils.loadStringListFromFile(filen);
						Vector<String> rows = new Vector<String>();
						rows.add(vt.fieldNames[0]);
						for(String s: lines){
							StringTokenizer st = new StringTokenizer(s,"\t");
							String row = st.nextToken();
							rows.add(row);
						}
						vt.makePrimaryHash(vt.fieldNames[0]);
						vt = VSimpleProcedures.selectRowsFromList(vt, rows);
					}
					if(args[i].equals("-selectrowsbynumfast")){
						String filen = args[i+1];
						System.out.println("Selecting rows by their numbers (0-based numbering) from file..."+filen);
						Vector<String> numbers = Utils.loadStringListFromFile(filen);
						HashSet<Integer> nums = new HashSet<Integer>();
						for(int j=0;j<numbers.size();j++) nums.add(Integer.parseInt(numbers.get(j)));
						int count = 0;
						LineNumberReader lr = new LineNumberReader(new FileReader(fn));
						String s = null;
						FileWriter fw = new FileWriter(fn+"_selectednum.txt");
						fw.write(lr.readLine()+"\n");
						while((s=lr.readLine())!=null){
							if(nums.contains(count))
								fw.write(s+"\n");
							count++;
						}
						fw.close();
					}
					if(args[i].equals("-ordercolumnspc")){
						int k = Integer.parseInt(args[i+1]);
						System.out.println("Ordering samples accordingly to PC1...");
						PCAMethod pca = new PCAMethod();
						VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
						pca.setDataSet(vd);
						pca.calcBasis(k);
						float contrs[] = new float[vd.coordCount];
						for(int j=0;j<vd.coordCount;j++){
							contrs[j] = (float)pca.getBasis().basis[k-1][j];
						}
						int inds[] = Algorithms.SortMass(contrs);
						Vector<String> numericalColumns = new Vector<String>();
						for(int j=0;j<vd.coordCount;j++){
							//System.out.println(vt.fieldNames[vd.selector.selectedColumns[inds[j]]]+"\t"+pca.getBasis().basis[k-1][inds[j]]);
							numericalColumns.add(vt.fieldNames[vd.selector.selectedColumns[inds[j]]]);
						}
						int l=0;
						Vector<String> columns = new Vector<String>();
						for(int j=0;j<vt.colCount;j++){
							if(vt.fieldTypes[j]==vt.STRING) columns.add(vt.fieldNames[j]);
							else{
								columns.add(numericalColumns.get(l)); l++;
							}
						}
						vt = VSimpleProcedures.SelectColumns(vt, columns);
					}
					if(args[i].equals("-selectcolumnsfromfile")){
						String filen = args[i+1];
						System.out.println("Selecting samples from file..."+filen);						
						Vector<String> lines = Utils.loadStringListFromFile(filen);
						Vector<String> cols = new Vector<String>();
						//cols.add(vt.fieldNames[0]);
						for(String s: lines){
							StringTokenizer st = new StringTokenizer(s,"\t");
							String col = st.nextToken();
							if(vt.fieldNumByName(col)!=-1)
								if(!cols.contains(col))
									cols.add(col);
						}
						vt = VSimpleProcedures.SelectColumns(vt, cols);
						//System.out.println(vt.fieldNames[0]+"..."+vt.fieldNames[1]+"..."+vt.fieldNames[2]+"...");
					}
					if(args[i].equals("-decomposebycolumns")){
						decomposeByColumns(fn,"txt");
					}
					if(args[i].equals("-selectcolumnsfromfilefast")){
						String filen = args[i+1];
						System.out.println("Selecting samples from file (fast version)..."+filen);						
						Vector<String> lines = Utils.loadStringListFromFile(filen);
						Vector<String> cols = new Vector<String>();
						//cols.add(vt.fieldNames[0]);
						for(String s: lines){
							StringTokenizer st = new StringTokenizer(s,"\t");
							String col = st.nextToken();								
							if(!cols.contains(col))
									cols.add(col);
						}
						SelectColumnsFast(fn, cols);
						//System.out.println(vt.fieldNames[0]+"..."+vt.fieldNames[1]+"..."+vt.fieldNames[2]+"...");
					}					
					if(args[i].equals("-reducedigits")){
						int numdigits = Integer.parseInt(args[i+1]);
						System.out.println("Reducing number of digits to "+numdigits);
						ReduceDigits(fn,numdigits);
					}
					if(args[i].equals("-decompose")){
						String sampleFile = args[i+1];
						StringTokenizer st = new StringTokenizer(sampleFile,"#");
						sampleFile = st.nextToken();
						String field = st.nextToken();
						System.out.println("Decomposing from... "+sampleFile);
						HashMap<String, Vector<String>> groupSample = new HashMap<String, Vector<String>>();
						Vector<String> allsamples = new Vector<String>();
						VDataTable samples = VDatReadWrite.LoadFromSimpleDatFile(sampleFile, true, "\t");
						for(int k=0;k<samples.rowCount;k++){
							String sample = samples.stringTable[k][0];
							String group = samples.stringTable[k][samples.fieldNumByName(field)];
							Vector<String> ss = groupSample.get(group);
							if(ss==null) ss = new Vector<String>();
							ss.add(sample);
							groupSample.put(group, ss);
							if(!allsamples.contains(sample)) 
								allsamples.add(sample);
						}
						Set<String> keys = groupSample.keySet();
						for(String group: keys){
							Vector<String> cols = groupSample.get(group);
							cols.insertElementAt(vt.fieldNames[0], 0);
							VDataTable vtg = VSimpleProcedures.SelectColumns(vt, cols);
							System.out.println("Saving "+group+".txt ...");
							VDatReadWrite.saveToSimpleDatFile(vtg, group+".txt");
						}
						System.out.println("Saving all groups ...");
						allsamples.insertElementAt(vt.fieldNames[0], 0);
						VDataTable vta = VSimpleProcedures.SelectColumns(vt, allsamples);
						VDatReadWrite.saveToSimpleDatFile(vta, fn.substring(0, fn.length()-4)+"_"+field+".txt");
					}
					if(args[i].equals("-collapsevar")){
						String sampleFile = args[i+1];
						StringTokenizer st = new StringTokenizer(sampleFile,"#");
						sampleFile = st.nextToken();
						String field = st.nextToken();
						int type = Integer.parseInt(st.nextToken());
						System.out.println("Collapsing variance from... "+sampleFile);
						HashMap<String, Vector<String>> groupSample = new HashMap<String, Vector<String>>();
						Vector<String> allsamples = new Vector<String>();
						VDataTable samples = VDatReadWrite.LoadFromSimpleDatFile(sampleFile, true, "\t");
						for(int k=0;k<samples.rowCount;k++){
							String sample = samples.stringTable[k][0];
							String group = samples.stringTable[k][samples.fieldNumByName(field)];
							Vector<String> ss = groupSample.get(group);
							if(ss==null) ss = new Vector<String>();
							ss.add(sample);
							groupSample.put(group, ss);
							if(!allsamples.contains(sample)) 
								allsamples.add(sample);
						}
						Set<String> keys = groupSample.keySet();
						Vector<String> keysv = new Vector<String>();
						for(String group: keys) keysv.add(group);
						Collections.sort(keysv);
						FileWriter fw = new FileWriter(fn.substring(0, fn.length()-4)+"_"+field+".txt");
						fw.write(vt.fieldNames[0]+"\t"); for(String group: keysv){ fw.write(field+"_"+group+"_var"+type+"\t"); } fw.write("\n");
						for(int row=0;row<vt.rowCount;row++){
							fw.write(vt.stringTable[row][0]+"\t");
						for(String group: keysv){
							Vector<String> cols = groupSample.get(group);
							//System.out.println(group+"\t"+vt.stringTable[row][0]);
							float f[] = new float[cols.size()];
							for(int kk=0;kk<f.length;kk++){
								f[kk] = Float.parseFloat(vt.stringTable[row][vt.fieldNumByName(cols.get(kk))]);
								//System.out.print(f[kk]+"\t");
							}
							//System.out.println();
							float val = 0f;
							if(type==0) val = VSimpleFunctions.calcStandardDeviation(f);
							if(type==2) val = VSimpleFunctions.calcStandardDeviationBiggerThan(f, 1e-6f);
							if(type==3) {
								float stdv = VSimpleFunctions.calcStandardDeviation(f);
								float mean = VSimpleFunctions.calcMean(f);
								val = stdv/Math.abs(mean+0.001f);
								val = val*val;
							}
							if(type==4) {
								val = VSimpleFunctions.calcMean(f);
							}
							fw.write(val+"\t");
							//System.out.println(val);
						}
						fw.write("\n");
						}
						fw.close();
						
					}
					if(args[i].equals("-mergefile")){
						String filen = args[i+1];
						System.out.println("Merging with file..."+filen);
						VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(filen, true, "\t");
						vt = VSimpleProcedures.MergeTables(vt, vt.fieldNames[0], vt1, vt1.fieldNames[0], "_");
					}
					if(args[i].equals("-mergefilenomissing")){
						String filen = args[i+1];
						System.out.println("Merging with file (no missing files)..."+filen);
						VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(filen, true, "\t", false);
						String fn1 = fn+"_merged.txt";
						VSimpleProcedures.MergeTablesNoMissingValues(vt, vt.fieldNames[0], vt1, vt1.fieldNames[0], fn1);
					}
					if(args[i].equals("-mergefileunion")){
						String filen = args[i+1];
						System.out.println("Merging with file (include all ids symmetrically)..."+filen);
						VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(filen, true, "\t", false);
						String fn1 = fn+"_merged.txt";
						VSimpleProcedures.MergeTablesUnionOfIDs(vt, vt.fieldNames[0], vt1, vt1.fieldNames[0], fn1);
					}
					if(args[i].equals("-mergefilefast")){
						String filen = args[i+1];
						System.out.println("Fast glueing with file..."+filen+" (same order and number of rows is assumed)");
						MergeTablesFast(fn,filen);
					}
					if(args[i].equals("-mergefilesfast")){
						String filen = args[i+1];
						System.out.println("Fast glueing of files..."+filen+" (any order of rows, many file names separated by tilda)");
						boolean makeUnique = false;
						if(args.length>i){
							makeUnique = Boolean.parseBoolean(args[i+2]);
						}
						MergeTablesFast_anyorder(fn+"~"+filen,makeUnique);
					}
					if(args[i].equals("-replacecolumnfast")){
						System.out.println("Replacing the content of a column (fast version) from "+args[i+1]+"...");
						ReplaceColumnFast(fn,args[i+1]);
					}
					if(args[i].equals("-replacewordfast")){
						System.out.println("Replacing words (fast version) from "+args[i+1]+"...");
						ReplaceWordFast(fn,args[i+1]);
					}
					if(args[i].equals("-transpose")){
						System.out.println("Transposing...");
						vt = vt.transposeTable(vt.fieldNames[0]);
					}
					if(args[i].equals("-objectcorr")){
						System.out.println("Computing correlation table between objects...");
						vt = VSimpleFunctions.makeObjectCorrelationTable(vt,Float.parseFloat(args[i+1]));
					}
					if(args[i].equals("-prepare4ICA")){
						System.out.println("Preparing for ICA...");
						VDatReadWrite.writeNumberOfColumnsRows = false;
						VDatReadWrite.useQuotesEverywhere = false;
						VDatReadWrite.saveToSimpleDatFile(vt, fn.substring(0, fn.length()-4)+"_ica.txt");						
						prepareTable4ICA(fn.substring(0, fn.length()-4)+"_ica.txt");
					}
					if(args[i].equals("-prepare4ICAFast")){
						System.out.println("Center and preparing for ICA fast...");
						prepareTable4ICAFast(fn);
					}
					if(args[i].equals("-savetxt")){
						System.out.println("Saving to..."+fn.substring(0, fn.length()-4)+"_copy.txt");
						VDatReadWrite.useQuotesEverywhere = false;
						VDatReadWrite.numberOfDigitsToKeep = 3;
						VDatReadWrite.writeNumberOfColumnsRows = false;
						VDatReadWrite.saveToSimpleDatFile(vt, fn.substring(0, fn.length()-4)+"_copy.txt");
					}
					if(args[i].equals("-assembleICAresults")){
						String fff = args[i+1];
						StringTokenizer st = new StringTokenizer(fff,";");
						String folder = st.nextToken();
						String prefix = st.nextToken();
						System.out.println("Assembling ICA results in "+folder+"...");
						CompileAandSTables(folder,prefix);
					}
				}
				if(vt!=null){
					System.out.println("Saving dat file to "+fn+".dat"+"...");
					VDatReadWrite.numberOfDigitsToKeep = 3;
					VDatReadWrite.saveToVDatFile(vt, fn+".dat");
				}
			}else{
				System.out.println("Please indicate the name of the tab-limited file with the first line containint column names to convert to ViDaExpert .dat format!");
				System.out.println("Options: [-center] [-doublecenter] [-selectrowsvar n] [-selectrowsfromfile filename] [-selectcolumnsfromfile filename] [-selectuniquebyvar] [-mergefile filename] [-mergefilefast filename_same_row_order] [-mergefilesfast several_filenames_tildaseparated] [-prepare4ICA] [-ordercolumnspc1] [-savetxt] [-transpose] [-logx1] [-decompose sampleFileName#fieldName] [-collapsevar sampleFileName#fieldName#type]");
				System.out.println("Notes: for collapsevar type is 0 - standard variance, 1 - trimmed top 5%, 2 - exclude zeros, 3 - coeffvar^2, 4 - standard mean");
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private static void SelectColumnsFast(String fn, Vector<String> cols) throws Exception{
		LineNumberReader lr = new LineNumberReader(new FileReader(fn));
		String firstLine = lr.readLine();
		lr.close();
		String tabcols[] = firstLine.split("\t");
		Vector<Integer> indices = new Vector<Integer>();
		HashSet<String> colsset = new HashSet<String>();
		for(String s: cols) colsset.add(s);
		for(int i=0;i<tabcols.length;i++)
			if(colsset.contains(tabcols[i]))
				indices.add(i);
		System.out.println(indices.size()+" columns will be selected.");
		lr = new LineNumberReader(new FileReader(fn));
		FileWriter fw = new FileWriter(fn+".colselected.txt");
		String line = null;
		while((line=lr.readLine())!=null){
			String parts[] = line.split("\t");
			for(int i=0;i<indices.size()-1;i++)
				fw.write(parts[indices.get(i)]+"\t");
			fw.write(parts[indices.get(indices.size()-1)]+"\n");
		}
		fw.close();
		lr.close();
	}

	public static void callFromMatlab(String args_string){
		System.out.println("Calling ProcessTxtData with arguments "+args_string);
		Vector<String> varg = new Vector<String>();
		StringTokenizer st = new StringTokenizer(args_string,"%");
		while(st.hasMoreTokens()){
			varg.add(st.nextToken());
		}
		String args[] = new String[varg.size()];
		for(int i=0;i<varg.size();i++) args[i] = varg.get(i);
		main(args);
	}
	
	  public static void prepareTable4ICA(String fname) throws Exception{
		  VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fname, true, "\t");
		  String fn = fname.substring(0, fname.length()-4);
		  FileWriter fw = new FileWriter(fn+"_numerical.txt");
		  for(int i=0;i<vt.rowCount;i++){
			  for(int j=1;j<vt.colCount;j++)
				  fw.write(vt.stringTable[i][j]+"\t");
			  fw.write("\n");
		  }
		  fw.close();
		  FileWriter fws = new FileWriter(fn+"_samples.txt");
		  for(int j=1;j<vt.colCount;j++)
			  fws.write(vt.fieldNames[j]+"\t");
		  fws.close();
		  FileWriter fwids = new FileWriter(fn+"_ids.txt");
		  for(int i=0;i<vt.rowCount;i++){
			  fwids.write(vt.stringTable[i][0]+"\n");
		  }
		  fwids.close();
	  }
	  
	  public static void prepareTable4ICAFast(String fname) throws Exception{
		  String fn = fname.substring(0, fname.length()-4)+"_ica";
		  LineNumberReader lr = new LineNumberReader(new FileReader(fname));
		  FileWriter fw = new FileWriter(fn+"_numerical.txt");
		  Vector<String> object_names = new Vector<String>();
		  String firstLine = lr.readLine();
		  String colnames[] = firstLine.split("\t");
		  String s = null;
		  float vals[] = new float[colnames.length-1];
		  int count = 0;
		  while((s=lr.readLine())!=null){
			  count++;
			  if(count==(int)((float)count/1000f)*1000)
				  System.out.println(count+" (Memory used "+Utils.getUsedMemoryMb()+")");
			  String parts[] = s.split("\t");
			  String id = parts[0];
			  object_names.add(id);
			  float sum = 0f;
			  for(int i=1;i<parts.length;i++){
				  vals[i-1] = Float.parseFloat(parts[i]);
				  sum+=vals[i-1];
			  }
			  float av = sum/(float)vals.length;
			  for(int i=0;i<vals.length;i++){
				  DecimalFormat df = new DecimalFormat("#.###");
				  DecimalFormatSymbols custom=new DecimalFormatSymbols();
				  custom.setDecimalSeparator('.');
				  df.setDecimalFormatSymbols(custom);				  
				  String ss = df.format(vals[i]-av);
				  if(ss.equals("0.000")) ss = "0";
				  fw.write(ss+"\t");
			  }
			  fw.write("\n");
		  }
		  fw.close();
		  lr.close();
		  FileWriter fws = new FileWriter(fn+"_samples.txt");
		  for(int j=1;j<colnames.length;j++)
			  fws.write(colnames[j]+"\t");
		  fws.close();
		  FileWriter fwids = new FileWriter(fn+"_ids.txt");
		  for(int i=0;i<object_names.size();i++){
			  fwids.write(object_names.get(i)+"\n");
		  }
		  fwids.close();
	  }
	  
	  
	  public static void CompileAandSTables(String folder, String prefix) throws Exception{
		  CompileAandSTables(folder,prefix,-1);
	  }
	  
		public static void CompileAandSTables(String folder, String prefix, int numberOfComponentsToTake) throws Exception{

			System.out.println("Looking into the folder "+folder);
			
			Locale.setDefault(new Locale("en", "US"));
			System.out.println("Forcing using point as decimal separator... "+Locale.getDefault().getCountry());
			
			String ending = "_numerical.txt.num";
			// If file with exact prefix exists then we just proceed 
			if(new File(folder+"A_"+prefix).exists()){
				ending = "";
			}else{
			// First, let us determine the right file, with _numerical.txt_XX.num ending, we want to know XX
			File lf[] = new File(folder).listFiles();
			int maxnumcomp = 0;
			String selectedEnding = "";
			for(File f:lf){
				String fn = f.getName(); 
				if(fn.contains("_numerical.txt"))if(fn.endsWith(".num")) { 
					int k=fn.indexOf("_numerical.txt"); 
					ending = fn.substring(k, fn.length());
					//System.out.println(ending);
					int numcomp = Integer.parseInt(ending.substring(15,ending.length()-4));
					if(numcomp>maxnumcomp){
						maxnumcomp = numcomp;
						selectedEnding = ending;
					}
					if(numberOfComponentsToTake>0){
						if(numcomp==numberOfComponentsToTake)
							selectedEnding = ending;
					}
				}
			}
			
			/*if(!new File(folder+"A_"+prefix+ending).exists()){
				ending = "_ica"+ending;
			}*/
			ending = selectedEnding;
			}
						
			if(new File(folder+"A_"+prefix+ending).exists()){
			
			VDataTable vtA = VDatReadWrite.LoadFromSimpleDatFile(folder+"A_"+prefix+ending, false, "\t");
			System.out.println(vtA.rowCount+" samples in "+folder+"A_"+prefix+ending);
			VDataTable vtS = VDatReadWrite.LoadFromSimpleDatFile(folder+"S_"+prefix+ending, false, "\t");
			
			
			
			//Set<String> keys = sampleAnnotations.tableHashPrimary.keySet();
			//for(String sss: keys) System.out.println("Key '"+sss+"'");

			String prefixsid = prefix;
			if(ending.equals("")){
				int k = prefix.indexOf("_numerical.txt");
				prefixsid = prefix.substring(0, k);
			}
			
			String colString = Utils.loadString(folder+prefixsid+"_samples.txt");
			Vector<String> samples = new Vector<String>();
			StringTokenizer st = new StringTokenizer(colString,"\t");
			while(st.hasMoreTokens()){ 
				String ss = st.nextToken(); 
				if(!ss.trim().equals("")) samples.add(ss); 
			}
			System.out.println(samples.size()+" samples in "+folder+prefixsid+"_samples.txt");
			Vector<String> ids = Utils.loadStringListFromFile(folder+prefixsid+"_ids.txt");
			
			/*
			 * Normalize table of component projections, heavy tail always positive
			 */
			Vector<Boolean> swapped = new Vector<Boolean>();
			Vector<MetaGene> sMetagenes = new Vector<MetaGene>();
			for(int i=0;i<vtS.colCount;i++){
				String fn = vtS.fieldNames[i];
				Boolean swpd = false;
				System.out.println("Field "+fn);
				MetaGene mg = new MetaGene();
				for(int j=0;j<vtS.rowCount;j++){
					//if(j==100000*(int)(0.00001f*j))
					//	System.out.print(j+"\t");
					mg.add(ids.get(j), Float.parseFloat(vtS.stringTable[j][i]));
				}
				
				mg.normalizeWeightsToZScores();
				
				//System.out.println();
				//float positiveStd = mg.sidedStandardDeviation(+1);
				//float negativeStd = mg.sidedStandardDeviation(-1);
				float positiveSide = mg.sumOfWeightsAboveThreshold(+1,3f);
				float negativeSide = mg.sumOfWeightsAboveThreshold(-1,3f);
				if(positiveSide+negativeSide<1e-6){
					positiveSide = mg.sumOfWeightsAboveThreshold(+1,2f);
					negativeSide = mg.sumOfWeightsAboveThreshold(-1,2f);
					if(positiveSide+negativeSide<1e-6){
						positiveSide = mg.sumOfWeightsAboveThreshold(+1,1.5f);
						negativeSide = mg.sumOfWeightsAboveThreshold(-1,1.5f);
					}
				}
				
				System.out.println("positiveSide = "+positiveSide+", negativeSide = "+negativeSide);
				if(negativeSide>positiveSide){
					//System.out.println("TSPAN6 weight = "+mg.getWeight("TSPAN6"));
					mg.invertSignsOfWeights();
					//System.out.println("Flipped, TSPAN6 weight = "+mg.getWeight("TSPAN6"));
					swpd = true;
				}
				sMetagenes.add(mg);
				swapped.add(swpd);
			}

			System.out.println("Saving to "+folder+prefix+"_A.xls");
			
			
			FileWriter fwA = new FileWriter(folder+prefix+"_A.xls");
			fwA.write("SAMPLE\t"); for(int i=1;i<=vtA.colCount;i++) fwA.write("IC"+i+"\t"); fwA.write("\n");
			for(int i=0;i<vtA.rowCount;i++){
				String sample = samples.get(i);
				fwA.write(sample+"\t");
				for(int j=0;j<vtA.colCount;j++){
					float f = Float.parseFloat(vtA.stringTable[i][j]);
					if(swapped.get(j)) f = -f;
					DecimalFormat nf = new DecimalFormat("#.####");
					String vs = nf.format(f);
					fwA.write(vs+"\t");
				}
				fwA.write("\n");
			}
			fwA.close();
			
			System.out.println("Saving to "+folder+prefix+"_S.xls");
			FileWriter fwS = new FileWriter(folder+prefix+"_S.xls");
			fwS.write("PROBE\t"); for(int i=1;i<=vtS.colCount;i++) fwS.write("IC"+i+"\t"); fwS.write("\n");
			for(int i=0;i<vtS.rowCount;i++){
				String id = ids.get(i);
				if(i==100000*(int)(0.00001f*i))
					System.out.print(i+"\t");
				fwS.write(id+"\t");
				for(int j=0;j<vtS.colCount;j++){
					/*float f = Float.parseFloat(vtS.stringTable[i][j]);
					DecimalFormat nf = new DecimalFormat("#.###");
					String vs = nf.format(f);
					fwS.write(vs+"\t");
					if(idAnnotations!=null) fwSa.write(vs+"\t");*/
					float f = sMetagenes.get(j).getWeight(id);
					DecimalFormat nf = new DecimalFormat("#.####");
					String vs = nf.format(f);
					fwS.write(vs+"\t");
				}
				fwS.write("\n");
			}
			fwS.close();
			System.out.println();
			
			}else{
				System.out.println("Did not find "+folder+"A_"+prefix+ending);
			}
		}

		public static void CompileAandSTablesAllResults(String folder, String prefix) throws Exception{
			
			// First, let us determine the right file, with _numerical.txt_XX.num ending, we want to know XX
			File lf[] = new File(folder).listFiles();
			String ending = "_numerical.txt.num";
			Vector<String> processed = new Vector<String>();
			for(File f:lf){
				String fn = f.getName(); 
				if(fn.contains("_numerical.txt"))if(fn.endsWith(".num")) { 
					int k=fn.indexOf("_numerical.txt"); ending = fn.substring(k, fn.length());
					if(!processed.contains(prefix+ending)){
						System.out.println(prefix+ending);
						CompileAandSTables(folder,prefix+ending);
						processed.add(prefix+ending);
					}
				}
			}
			
		}

		public static void filterByVariationFast(String fileName, int numofgenes) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fileName));
			String firstline = lr.readLine();
			Vector<Float> variances = new Vector<Float>();
			String s = null;
			FileWriter fwv = new FileWriter(fileName+"_filtered_var.txt");
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				float x = 0f;
				float x2= 0f;
				int n = 0;
				for(int i=1;i<parts.length;i++){
					String p = parts[i];
					try{
						float val = Float.parseFloat(p);
						n++;
						x+=val;
						x2+=val*val;
					}catch(Exception e){
						
					}
				}
				float var = x2/(float)n - (x/(float)n)*(x/(float)n);
				variances.add(var);
				fwv.write(parts[0]+"\t"+var+"\n");
				//System.out.println(var);
			}
			lr.close();
			fwv.close();
			float vrs[] = new float[variances.size()];
			for(int i=0;i<variances.size();i++) vrs[i] = variances.get(i);
			int inds[] = Algorithms.SortMass(vrs);
			
			float varthreshold = variances.get(inds[inds.length-1-numofgenes]);
			//System.out.println(inds[0]+"\t"+inds[1]+"\t"+inds[2]);
			//System.out.println("varthreshold="+varthreshold);
			
			FileWriter fw = new FileWriter(fileName+"_filtered.txt");
			lr = new LineNumberReader(new FileReader(fileName));
			firstline = lr.readLine();
			fw.write(firstline+"\n");
			int k=0;
			s = null;
			while((s=lr.readLine())!=null){
				float var = variances.get(k);
				k++;
				if(var>varthreshold)
					fw.write(s+"\n");
			}
			lr.close();
			fw.close();
			
		}

		public static void CenterFast(String fileName) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fileName));
			String firstline = lr.readLine();
			FileWriter fw = new FileWriter(fileName+"_centered.txt");
			fw.write(firstline+"\n");
			String s = null;
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				float x = 0f;
				int n = 0;
				for(int i=1;i<parts.length;i++){
					String p = parts[i];
					try{
						float val = Float.parseFloat(p);
						n++;
						x+=val;
					}catch(Exception e){
						
					}
				}
				float average = x/n;
				fw.write(parts[0]+"\t");
				for(int i=1;i<parts.length;i++){
					String p = parts[i];
					try{
						float val = Float.parseFloat(p)-average;
						DecimalFormat df = new DecimalFormat("#.###"); 
						fw.write(df.format(val)+"\t");
					}catch(Exception e){
						fw.write(p+"\t");
					}
				}
				fw.write("\n");
			}
			fw.close();
			lr.close();
		}

		
		public static void SubtractValueFast(String fileName, float val) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fileName));
			String firstline = lr.readLine();
			FileWriter fw = new FileWriter(fileName+"_subtracted.txt");
			fw.write(firstline+"\n");
			String s = null;
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				fw.write(parts[0]+"\t");
				for(int i=1;i<parts.length;i++){
					String p = parts[i];
					try{
						float value = Float.parseFloat(p)-val;
						DecimalFormat df = new DecimalFormat("#.###"); 
						fw.write(df.format(value)+"\t");
					}catch(Exception e){
						fw.write(p+"\t");
					}
				}
				fw.write("\n");
			}
			fw.close();
			lr.close();
		}
		
		
		public static void ReplaceColumnFast(String fn, String templateFile) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(templateFile, false, "\t");
			vt.makePrimaryHash(vt.fieldNames[0]);
			FileWriter fw = new FileWriter(fn+"_replaced.txt");
			String s = null;
			int count=0;
			int columnToReplace=0;
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				if(count==0){
					for(int i=0;i<parts.length;i++)
						if(vt.tableHashPrimary.containsKey(parts[i]))
							columnToReplace = i;
				}
				String key = parts[columnToReplace];
				String value = "N/A";
				if(vt.tableHashPrimary.containsKey(key))
					value = vt.stringTable[vt.tableHashPrimary.get(key).get(0)][1];
				parts[columnToReplace] = value;
				for(int i=0;i<parts.length-1;i++)
					fw.write(parts[i]+"\t");
				fw.write(parts[parts.length-1]+"\n");
			}
			fw.close();
			lr.close();
		}
		
		public static void ReduceDigits(String fn, int numdigits) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			FileWriter fw = new FileWriter(fn+"_reducedigits"+numdigits+".txt");
			String s = lr.readLine();
			fw.write(s+"\n");
			s = null;
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				fw.write(parts[0]+"\t");
				for(int i=1;i<parts.length;i++){
					float f = Float.parseFloat(parts[i]);
					String fmt = "#.";
					for(int j=0;j<numdigits;j++) fmt+="#";
					DecimalFormat df = new DecimalFormat(fmt);
					fw.write(df.format(f)+"\t");
				}
				fw.write("\n");
			}
			fw.close();
			lr.close();
		}

		public static int SelectUniqueByVariationFast(String fn) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			String s = null;
			HashMap<String,Vector<Integer>> mapLines = new HashMap<String,Vector<Integer>>();
			Vector<Double> variances = new Vector<Double>();
			int lineNumber=0;
			
			s = lr.readLine();
			
			System.out.println("First reading...");
			
			while((s=lr.readLine())!=null){
			
			String parts[] = s.split("\t");
			
			String id = parts[0];
			
			Vector<Integer> vi = mapLines.get(id);
			if(vi==null) vi = new Vector<Integer>();
			vi.add(lineNumber);
			mapLines.put(id, vi);
			
			lineNumber++;
			double x = 0f;
			double x2 = 0f;
			int n = 0;
			for(int i=1;i<parts.length;i++){
				String p = parts[i];
				try{
					float val = Float.parseFloat(p);
					n++;
					x+=val;
					x2+=val*val;
				}catch(Exception e){
					
				}
			}
			double variance = x2/n - (x/n)*(x/n);
			variances.add(variance);
			}
			lr.close();
			
			System.out.println("Second reading...");
			
			FileWriter fw = new FileWriter(fn+"_unique.txt");			

			lineNumber=0;
			int numLines = 0;			
			
			lr = new LineNumberReader(new FileReader(fn));
			s = lr.readLine();
			fw.write(s+"\n");
			while((s=lr.readLine())!=null){
			
			String parts[] = s.split("\t");
			String id = parts[0];
			Vector<Integer> vi = mapLines.get(id);
			if(vi!=null){
			int maxVarLine = -1;
			double maxVarValue = 0;
			for(int j=0;j<vi.size();j++)
				if(variances.get(vi.get(j))>maxVarValue){
					maxVarValue = variances.get(vi.get(j));
					maxVarLine = vi.get(j);
				}

			if(maxVarLine==lineNumber)if(maxVarValue>1e-100){
				//System.out.println(maxVarValue);
				//System.out.println(lineNumber);
				for(int j=0;j<parts.length-1;j++)
					fw.write(parts[j]+"\t");
				fw.write(parts[parts.length-1]+"\n");
				numLines++;
				mapLines.remove(id);
			}
			}
			
			lineNumber++;
			
			}
			lr.close();
			fw.close();
			
			return numLines;
		}
		
		public static void decomposeByColumns(String fn, String fileExtension) throws Exception{
			int k=1;
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			String firstLine = lr.readLine();
			String firstParts[] = firstLine.split("\t");
			int numberOfColumns = firstParts.length-1;
			System.out.println("Numbre of columns = "+numberOfColumns);
			int maxFiles = 100;
			lr.close();
			
			String folder = new File(new File(fn).getParent()).getAbsolutePath()+File.separator;
			
			while(k<numberOfColumns){

			lr = new LineNumberReader(new FileReader(fn));
			lr.readLine();
				
			FileWriter fws[] = new FileWriter[maxFiles];
			for(int i=0;i<maxFiles;i++)if(k+i<=numberOfColumns)
				fws[i] = new FileWriter(folder+firstParts[k+i]+"."+fileExtension);
				
				System.out.println("Column "+k);
			
				String s = null;
				while((s=lr.readLine())!=null){
					String parts[] = s.split("\t");
					if(parts.length<numberOfColumns)
						System.out.println("WARNING: something strange (k="+k+") in "+s);
					for(int i=0;i<maxFiles;i++)if(k+i<=numberOfColumns){
						fws[i].write(parts[0]+"\t"+parts[k+i]+"\n");
					}
				}
			
			for(int i=0;i<maxFiles;i++)if(k+i<=numberOfColumns)
				fws[i].close();
			k+=maxFiles;
			
			lr.close();
			}
			
			
		}
		
		public static void MergeTablesFast(String fn,String filen) throws Exception{
			LineNumberReader lr1 = new LineNumberReader(new FileReader(fn));
			LineNumberReader lr2 = new LineNumberReader(new FileReader(filen));
			String s1 = null;
			FileWriter fw = new FileWriter(fn+".merged.txt");
			while((s1=lr1.readLine())!=null){
				String s2 = lr2.readLine();
				String parts1[] = s1.split("\t");
				String parts2[] = s2.split("\t");
				for(int i=0;i<parts1.length;i++)
					fw.write(parts1[i]+"\t");
				for(int i=1;i<parts2.length-1;i++)
					fw.write(parts2[i]+"\t");
				fw.write(parts2[parts2.length-1]+"\n");
			}
			fw.close();
			lr1.close();
			lr2.close();
		}
		
		public static void MergeMultipleTablesFast(Vector<String> fileNames, String outputFile) throws Exception{
			Vector<LineNumberReader> lrs = new Vector<LineNumberReader>(); 
			for(int i=0;i<fileNames.size();i++){
				LineNumberReader lr = new LineNumberReader(new FileReader(fileNames.get(i)));
				lrs.add(lr);
			}
			String s1 = null;
			FileWriter fw = new FileWriter(outputFile);
			while((s1=lrs.get(0).readLine())!=null){
				String parts1[] = s1.split("\t");
				for(int i=0;i<parts1.length;i++)
					fw.write(parts1[i]+"\t");
				for(int j=1;j<lrs.size();j++){
					String s2 = lrs.get(j).readLine();
					String parts2[] = s2.split("\t");
					for(int i=1;i<parts2.length-1;i++)
						fw.write(parts2[i]+"\t");
					fw.write(parts2[parts2.length-1]+"\t");
				}
				fw.write("\n");
			}
			fw.close();
			for(int i=0;i<lrs.size();i++)
				lrs.get(i).close();
		}

		public static void LogXFast(String fn, int numberOfDigitsToKeep) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			String s = lr.readLine();
			FileWriter fw = new FileWriter(fn+"_logx10.txt");
		      String fs = "#.";
		      for(int l=0;l<numberOfDigitsToKeep;l++)
		        fs+="#";
		      DecimalFormat df = new DecimalFormat(fs);
			fw.write(s+"\n");
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				fw.write(parts[0]+"\t");
				for(int k=1;k<parts.length;k++){
					float v = Float.parseFloat(parts[k]);
					String sw = df.format(Math.log10(v));
					if(k<parts.length-1) 
						fw.write(sw+"\t");
					else
						fw.write(sw+"\n");
				}
			}
			fw.close();

		}
		
		public static void LogXTruncFast(String fn, float truncationthreshold, int numberOfDigitsToKeep) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			String s = lr.readLine();
			FileWriter fw = new FileWriter(fn+"_logx"+truncationthreshold+".txt");
		      String fs = "#.";
		      for(int l=0;l<numberOfDigitsToKeep;l++)
		        fs+="#";
		      DecimalFormat df = new DecimalFormat(fs);
			fw.write(s+"\n");
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
				fw.write(parts[0]+"\t");
				for(int k=1;k<parts.length;k++){
					float v = Float.parseFloat(parts[k]);
					String sw = df.format(Math.log10(v+truncationthreshold));
					if(k<parts.length-1) 
						fw.write(sw+"\t");
					else
						fw.write(sw+"\n");
				}
			}
			fw.close();

		}

		public static void SelectColumnsFast(String fn,String flist,boolean selectFirstColumn) throws Exception{
			LineNumberReader lr1 = new LineNumberReader(new FileReader(fn));
			
			Vector<String> list = Utils.loadStringListFromFile(flist);
			
			String s1 = lr1.readLine();
			String parts[] = s1.split("\t");
			Vector<Integer> selcols = new Vector<Integer>();
			if(selectFirstColumn)
				selcols.add(0);
			for(int i=0;i<parts.length;i++)
				if(list.contains(parts[i]))
					selcols.addElement(i);
			
			FileWriter fw = new FileWriter(fn+".colsel.txt");
			for(int i=0;i<selcols.size();i++)
				fw.write(parts[selcols.get(i)]+"\t");
			fw.write("\n");
			
			while((s1=lr1.readLine())!=null){
				String parts1[] = s1.split("\t");
				for(int i=0;i<selcols.size();i++)
					fw.write(parts1[selcols.get(i)]+"\t");
				fw.write("\n");
			}
			fw.close();
			lr1.close();
		}


		public static void SelectRowsFast(String fn,String flist) throws Exception{
			LineNumberReader lr1 = new LineNumberReader(new FileReader(fn));
			
			Vector<String> list = Utils.loadStringListFromFile(flist);
			HashSet<String> set = new HashSet<String>();
			for(int i=0;i<list.size();i++) set.add(list.get(i));
			
			String s1 = lr1.readLine();
			
			FileWriter fw = new FileWriter(fn+".rowsel.txt");
			fw.write(s1+"\n");
			
			while((s1=lr1.readLine())!=null){
				String parts[] = s1.split("\t");
				if(set.contains(parts[0]))
					fw.write(s1+"\n");
			}
			fw.close();
			lr1.close();
		}
		
		public static void MergeTablesFast_anyorder(String filen, boolean makeColumnNameUnique) throws Exception{
			String parts[] = filen.split("~");
			
			Vector<HashMap<String,Integer>> maps = new Vector<HashMap<String,Integer>>(); 
			
			Vector<Vector<String>> allstrings = new Vector<Vector<String>>();
			
			Vector<String> allsamples = new Vector<String>();
			
			String prefixes[] = new String[parts.length];
			for(int i=0;i<parts.length;i++){
				String fn = (new File(parts[i])).getName();
				prefixes[i] = fn;
			}

			
			for(int i=0;i<parts.length;i++){
				System.out.println("Indexing "+parts[i]+"...");
				LineNumberReader lr = new LineNumberReader(new FileReader(parts[i]));
				String s = null;
				s = lr.readLine();
				String smpls[] = s.split("\t");
				for(int l=1;l<smpls.length;l++){
					if(makeColumnNameUnique){
						smpls[l] = prefixes[i]+"_"+smpls[l];
					}
					allsamples.add(smpls[l]);
				}
				int k=0;
				HashMap<String,Integer> hm = new HashMap<String,Integer>();
				Vector<String> strings = new Vector<String>();
				while((s=lr.readLine())!=null){
					String prts[] = s.split("\t");
					hm.put(prts[0], k);
					strings.add(s);
					k++;
				}
				maps.add(hm);
				allstrings.add(strings);
				lr.close();
			}
			
			
			// find all common names
			HashMap<String,Integer> genenames = new HashMap<String,Integer>();
			for(int i=0;i<parts.length;i++){
				HashMap<String,Integer> hm = maps.get(i);
				Set<String> it = hm.keySet();
				for(String s: it){
					int j=0;
					if(genenames.get(s)==null){
						j = 1;
						genenames.put(s, j);
					}else{
						j = genenames.get(s);
						genenames.put(s,j+1);
					}
				}
			}
			
			Set<String> allgn = genenames.keySet();
			Vector<String> commongenenames = new Vector<String>();
			for(String s: allgn){
				int k = genenames.get(s);
				if(k==parts.length)
					commongenenames.add(s);
			}
			Collections.sort(commongenenames);
			
			FileWriter fw = new FileWriter(parts[0]+".merged");
			
			fw.write("GENE\t");
			for(int l=0;l<allsamples.size();l++)
				fw.write(allsamples.get(l)+"\t");
			fw.write("\n");
			
			for(int i=0;i<commongenenames.size();i++){
				String gn = commongenenames.get(i);
				fw.write(gn+"\t");
				for(int j=0;j<parts.length;j++){
					int k = maps.get(j).get(gn);
					String st = allstrings.get(j).get(k);
					if(st.endsWith("\t"))
						st = st.substring(0, st.length()-1);
					int kk = st.indexOf("\t");
					st = st.substring(kk+1,st.length());
					fw.write(st+"\t");
				}
				fw.write("\n");
			}
			
			fw.close();
		}
		
		public static void ReplaceWordFast(String fn, String conversionTable) throws Exception{
			LineNumberReader lr = new LineNumberReader(new FileReader(fn));
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(conversionTable, false, "\t");
			vt.makePrimaryHash(vt.fieldNames[0]);
			FileWriter fw = new FileWriter(fn+"_replacedwords.txt");
			String s = null;
			while((s=lr.readLine())!=null){
				String parts[] = s.split("\t");
					for(int i=0;i<parts.length;i++){
						if(vt.tableHashPrimary.containsKey(parts[i])){
							int k = vt.tableHashPrimary.get(parts[i]).get(0);
							fw.write(vt.stringTable[k][1]+"\t");
						}else{
							fw.write(parts[i]+"\t");
						}
					}
					fw.write("\n");
			}
			fw.close();
			lr.close();
		}

		
}
