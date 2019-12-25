package getools.scripts;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.TableUtils;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.utils.VSimpleProcedures;

public class SGA {
	
	public static HashMap<String, String> orf2name = new HashMap<String, String>();  

	/**
	 * @param args
	 */
	
	//public static String fileName = "sga_scores.txt";
	//public static String fileName = "sga_colsize.txt";
	//public static String fileName = "s12a_scores.txt";
	public static String fileName = "sga_tsv_colsize.txt";
	public static boolean takeColonySizeNotEpsilon = true;
	
	public static void main(String[] args) {
		try{
			
			loadOrf2NameTable();
			//assembleTableS12A();
			//assembleTable();
			//makePCAAnalysis("c:/datas/sga/analysis/sga_scores.dat",10,21);
			
			//computeVariationMaps();
			
			String type = "score";
			SGA.takeColonySizeNotEpsilon = false;
			
			SGA.fileName = "sga_tsv_"+type+"_MMS.txt";
			assembleTSV("C:/Datas/SGA/data/SGA 48h MMS plates analysis-bad448DMA11/","no treatment;MMS0.01;MMS0.02;MMS0.03","MMS","_25");
			
			SGA.fileName = "sga_tsv_"+type+"_Zeo.txt";
			assembleTSV("C:/Datas/SGA/data/SGA 72h Zeo plates analysis/","no treatment;Zeo60;Zeo180","Zeo","_25");

			SGA.fileName = "sga_tsv_"+type+"_HU48.txt";
			assembleTSV("C:/Datas/SGA/data/SGA HU - newDMA11/SGA 48h HU/","no treatment;HU15;HU75","HU","_25_48");
			
			SGA.fileName = "sga_tsv_"+type+"_30_48.txt";
			assembleTSV("C:/Datas/SGA/data/TSV6 30-35°C/48h TSV6 30-35°C/48h TS 30oC/","no treatment;HU;MMS;Zeo","3048","_30_48");

			SGA.fileName = "sga_tsv_"+type+"_30_72.txt";
			assembleTSV("C:/Datas/SGA/data/TSV6 30-35°C/72h TSV6 30-35°C/72h TS 30oC/","no treatment;HU;MMS;Zeo","3072","_30_72");

			SGA.fileName = "sga_tsv_"+type+"_35_48.txt";
			assembleTSV("C:/Datas/SGA/data/TSV6 30-35°C/48h TSV6 30-35°C/48h YPD35oC - only no treatment/","no treatment","3548","_35_48");
			 			
			SGA.fileName = "sga_tsv_"+type+"_35_72.txt";
			assembleTSV("C:/Datas/SGA/data/TSV6 30-35°C/72h TSV6 30-35°C/72h TS 35oC - only no treatment/","no treatment","3572","_35_72");

			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static void loadOrf2NameTable(){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/sga/data/orf2name.txt", true, "\t");
		for(int i=0;i<vt.rowCount;i++){
			if((vt.stringTable[i][1]==null)||vt.stringTable[i][1].equals("")||vt.stringTable[i][1].toLowerCase().equals("none"))
				vt.stringTable[i][1] = vt.stringTable[i][0];
			orf2name.put(vt.stringTable[i][0], vt.stringTable[i][1]);
		}
	}
	
	public static void makePCAAnalysis(String fn, int col1, int col2){
		String fn1 = fn.substring(0, fn.length()-4);
		VDataTable vt = VDatReadWrite.LoadFromVDatFile(fn);
		VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, col1-1, col2-1);
		VDatReadWrite.saveToVDatFile(vd, fn1+"_n.dat");
		VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(fn1+"_n.dat");
		VDataTable vt1p = TableUtils.PCAtable(vt1, true);
		VDatReadWrite.saveToSimpleDatFile(vt1p, fn1+"_pca.txt", true);
	}
	
	public static void assembleTable(){
		String folder = "c:/datas/sga/";
		File dir = new File(folder);
		int numberOfPlates = 14;

		String treatments[] = {"MMS","Zeo","HU"};		
		String doses[] = {"MMS0.02","Zeo60","HU15"};
		String folderkey = "SGA";
		
		/*String treatments[] = {"Zeo"};		
		String doses[] = {"Zeo60"};
		String key = "S12A";*/
		
		HashMap<String,Vector<Float>> allscores =  new HashMap<String,Vector<Float>>();
		HashMap<String,String> plates =  new HashMap<String,String>();
		
		for(File f: dir.listFiles()){
			for(int i=0;i<treatments.length;i++){
				if(f.isDirectory())if(f.getName().contains(folderkey))if(f.getName().contains(treatments[i])){
					System.out.println("Treatment:"+f.getName());
					for(File dma: f.listFiles()){
						for(int k=1;k<=numberOfPlates;k++){
							if(dma.getName().equals("DMA"+k)){
								System.out.println("\tPlate:"+dma.getName());
								for(File ff: dma.listFiles()){
									
									HashMap<String, Float> scores = new HashMap<String, Float>();
									
									int column = 0;
									
									if(ff.getName().contains("no treatment")){
											column = i*2+0;
											for(File subfolder: ff.listFiles()){
												if(subfolder.getName().startsWith("nsnormaliz")){
													System.out.println("\t\treading "+subfolder+"/"+"combined_data.dat");
													scores = readScoresFile(subfolder+"/"+"combined_data.dat");
												}
											}
									}
									for(int dose=0;dose<doses.length; dose++){
										if(ff.getName().contains(doses[dose])){
											column = i*2+1;
											if(ff.isDirectory())
											for(File subfolder: ff.listFiles()){
												if(subfolder.getName().startsWith("nsnormaliz")){
													System.out.println("\t\treading "+subfolder+"/"+"combined_data.dat");													
													scores = readScoresFile(subfolder+"/"+"combined_data.dat");
												}
											}
										}
									}
									
									if(scores.size()!=0){
										boolean empty = false;
										for(String key: scores.keySet()){
											plates.put(key.substring(2,key.length()), "DMA"+k);
											if(!scores.get(key).isNaN()) empty = false;
										}
									if(empty)
										System.out.println();

									for(String key: scores.keySet()){
										String prefix = key.substring(0, 2);
										String gene = key.substring(2, key.length());
										
										Vector<Float> v = allscores.get(gene);
										if(v==null){
											v = new Vector<Float>();
											for(int kk=0;kk<6*doses.length;kk++){
												v.add(Float.NaN);
											}
											allscores.put(gene, v);
										}
										if(prefix.equals("N_"))
											v.set(column, scores.get(key));
										if(prefix.equals("Q_"))
											v.set(column+6, scores.get(key));
										if(prefix.equals("R_"))
											v.set(column+12, scores.get(key));
									}
									
								   }
									
								}
							}
						}
					}
				}
			}
		}
		
		try{
			
		
		FileWriter fw = new FileWriter(folder+"analysis/"+fileName);
		fw.write("NAME\tORF\tPLATE\t");
		for(int k=0;k<3;k++){
			String prefix = "";
			if(k==0) prefix = "N_";
			if(k==1) prefix = "Q_";
			if(k==2) prefix = "R_";
			for(int i=0;i<treatments.length;i++){
				fw.write(prefix+treatments[i]+"\t");
				fw.write(prefix+doses[i].replace(" ", "_")+"\t");
			}
		}
		fw.write("\n");
		Vector<String> orfs = new Vector<String>();
		for(String key: allscores.keySet()){
			orfs.add(key);
		}
		Collections.sort(orfs);
		for(String s: orfs){
			String name = orf2name.get(s);
			if(name==null) name = s;
			fw.write(name+"\t"+s+"\t"+plates.get(s)+"\t");
			Vector<Float> v = allscores.get(s);
			for(Float f: v){
				fw.write(f+"\t");
			}
		fw.write("\n");			
		}
		fw.close();
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/sga/orf2name.txt", true, "\t");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public static void assembleTableS12A(){
		String folder = "c:/datas/sga/";
		File dir = new File(folder);
		int numberOfPlates = 14;

		//String treatments[] = {"MMS","Zeo","HU"};		
		//String doses[] = {"MMS0.02","Zeo60","HU15"};
		//String key = "SGA";
		
		String treatments[] = {"Zeo"};		
		String doses[] = {"Zeo00","Zeo60","Zeo180"};
		String folderkey = "S12A";
		
		HashMap<String,Vector<Float>> allscores =  new HashMap<String,Vector<Float>>();
		HashMap<String,String> plates =  new HashMap<String,String>();
		
		for(File f: dir.listFiles()){
			for(int i=0;i<treatments.length;i++){
				if(f.isDirectory())if(f.getName().contains(folderkey))if(f.getName().contains(treatments[i])){
					System.out.println("Treatment:"+f.getName());
					for(File dma: f.listFiles()){
						for(int k=1;k<=numberOfPlates;k++){
							if(dma.getName().equals("DMA"+k)){
								System.out.println("\tPlate:"+dma.getName());
								for(File ff: dma.listFiles()){
									
									HashMap<String, Float> scores = new HashMap<String, Float>();
									
									int column = 0;
									
									if(ff.getName().contains("no treatment")){
											column = i*2+0;
											for(File subfolder: ff.listFiles()){
												if(subfolder.getName().startsWith("nsnormaliz")){
													System.out.println("\t\treading "+subfolder+"/"+"combined_data.dat");
													scores = readScoresFileS12A(subfolder+"/"+"combined_data.dat",null);
												}
											}
									}
									for(int dose=0;dose<doses.length; dose++){
										if(ff.getName().contains(treatments[i])){
											column = i*2+1+dose;
											if(ff.isDirectory())
											for(File subfolder: ff.listFiles()){
												if(subfolder.getName().startsWith("nsnormaliz")){
													System.out.println("\t\treading "+subfolder+"/"+"combined_data.dat");													
													scores = readScoresFileS12A(subfolder+"/"+"combined_data.dat",doses[dose]);
												}
											}
										}
									
									if(scores.size()!=0){
										boolean empty = false;
										for(String key: scores.keySet()){
											plates.put(key.substring(2,key.length()), "DMA"+k);
											if(!scores.get(key).isNaN()) empty = false;
										}
									if(empty)
										System.out.println();

									for(String key: scores.keySet()){
										
										String parts[] = key.split("_");
										String prefix = parts[0]+"_";
										String gene = parts[1];
										//String prefix = key.substring(0, 2);
										//String gene = key.substring(2, key.length());
										
										Vector<Float> v = allscores.get(gene);
										if(v==null){
											v = new Vector<Float>();
											for(int kk=0;kk<2*(doses.length+1);kk++){
												v.add(Float.NaN);
											}
											allscores.put(gene, v);
										}
										if(prefix.equals("N_"))
											v.set(column, scores.get(key));
										if(prefix.equals("S_"))
											v.set(column+4, scores.get(key));
									}
								   }
								}
									
								}
							}
						}
					}
				}
			}
		}
		
		try{
			
		
		FileWriter fw = new FileWriter(folder+"analysis/"+fileName);
		fw.write("NAME\tORF\tPLATE\t");
		for(int k=0;k<2;k++){
			String prefix = "";
			if(k==0) prefix = "N_";
			if(k==1) prefix = "S_";
			fw.write(prefix+"NT"+"\t");
			for(int i=0;i<doses.length;i++){
				fw.write(prefix+doses[i].replace(" ", "_")+"\t");
			}
		}
		fw.write("\n");
		Vector<String> orfs = new Vector<String>();
		for(String key: allscores.keySet()){
			orfs.add(key);
		}
		Collections.sort(orfs);
		for(String s: orfs){
			String name = orf2name.get(s);
			if(name==null) name = s;
			fw.write(name+"\t"+s+"\t"+plates.get(s)+"\t");
			Vector<Float> v = allscores.get(s);
			for(Float f: v){
				fw.write(f+"\t");
			}
		fw.write("\n");			
		}
		fw.close();
		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/sga/orf2name.txt", true, "\t");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public static HashMap<String, Float> readScoresFile(String fn){
		HashMap<String, Float> scores = new HashMap<String, Float>();
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		int k=0;
		for(String s: lines){
			if(!s.startsWith("#"))if(!s.startsWith("row")){
				StringTokenizer st = new StringTokenizer(s,"\t");
				while(st.hasMoreTokens()){
					st.nextToken(); st.nextToken(); st.nextToken(); st.nextToken(); 
					String query = st.nextToken();
					String orf = st.nextToken();
					//System.out.print(k+++"\t");
					String ssize = st.nextToken();
					Float normalizedColonySize = Float.NaN;
					if(!ssize.equals("NA"))
						normalizedColonySize = Float.parseFloat(ssize);
					String sscore = st.nextToken();
					Float score = Float.NaN;
					if(!sscore.equals("NA"))
					    score = Float.parseFloat(sscore);
					//Float stdev = Float.parseFloat(st.nextToken());
					//Float pvalue = Float.parseFloat(st.nextToken());
					String addInfor = st.nextToken();
					String prefix = "";
					if(query.contains("-4KQ")) prefix = "Q_"; 
					if(query.contains("-4KR")) prefix = "R_";
					if(query.toLowerCase().contains("-wt")) prefix = "N_";
					if(query.equals("YOL012C")) prefix = "N_";
					if(prefix.equals(""))
						System.out.println("UNKNOWN QUERY TYPE:"+query);
					orf=prefix+orf;
					if(prefix.equals("N_"))
						score = normalizedColonySize;
					if(takeColonySizeNotEpsilon)
						score = normalizedColonySize;
					scores.put(orf, score);
				}
			}
		} 
		//System.out.println();
		return scores;
	}
	
	public static HashMap<String, Float> readScoresFileS12A(String fn, String dose){
		HashMap<String, Float> scores = new HashMap<String, Float>();
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		int k=0;
		for(String s: lines){
			if(!s.startsWith("#"))if(!s.startsWith("row")){
				StringTokenizer st = new StringTokenizer(s,"\t");
				while(st.hasMoreTokens()){
					st.nextToken(); st.nextToken(); st.nextToken(); String filename = st.nextToken(); 
					String query = st.nextToken();
					String orf = st.nextToken();
					System.out.print(k+++"\t");
					String ssize = st.nextToken();
					Float normalizedColonySize = Float.NaN;
					if(!ssize.equals("NA"))
						normalizedColonySize = Float.parseFloat(ssize);
					String sscore = st.nextToken();
					Float score = Float.NaN;
					if(!sscore.equals("NA"))
					    score = Float.parseFloat(sscore);
					//Float stdev = Float.parseFloat(st.nextToken());
					//Float pvalue = Float.parseFloat(st.nextToken());
					String addInfor = st.nextToken();
					String prefix = "";
					/*if(query.contains("-4KQ")) prefix = "Q_"; 
					if(query.contains("-4KR")) prefix = "R_";
					if(query.toLowerCase().contains("-wt")) prefix = "N_";
					if(query.equals("YOL012C")) prefix = "N_";*/
					if(query.equals("HTZ-wt")) prefix = "N_";
					if(query.equals("HTZ-S12A")) prefix = "S_";
					
					if(prefix.equals(""))
						System.out.println("UNKNOWN QUERY TYPE:"+query);
					orf = orf.split("_")[0];
					orf=prefix+orf;
					if(dose==null){
						if(orf.startsWith("N_"))
							score = normalizedColonySize;
					}
					else
					if(dose.contains("00"))
						score = normalizedColonySize;
					if(takeColonySizeNotEpsilon)
						score = normalizedColonySize;
					
					if(dose==null)					
						scores.put(orf, score);
					else{
						if(filename.contains(dose))
							scores.put(orf, score);
					}
				}
			}
		}System.out.println();
		return scores;
	}
	
	public static void computeVariationMaps() throws Exception{
		String prefix = "C:/Datas/SGA/analysis/";
		VDataTable vmap = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/SGA/data/DMA_1536_format.txt", true, "\t");		
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+fileName, true, "\t");
		String fields[][] = {{"N_MMS","N_Zeo","N_HU"},{"Q_MMS","Q_Zeo","Q_HU"},{"R_MMS","R_Zeo","R_HU"}};
		//FileWriter fw = new FileWriter(prefix+"sga_variation_notreatment.txt");
		FileWriter fw = new FileWriter(prefix+"sga_variation_notreatment_colsize.txt");
		fw.write("ORF\tNAME\tN\tQ\tR\tTOTAL\n");
		for(int i=0;i<vt.rowCount;i++){
			fw.write(vt.stringTable[i][1]+"\t"+vt.stringTable[i][0]+"\t");
			float total = 0f;
			float t[] = new float[6];
			for(int k=0;k<3;k++){
				float f[] = new float[3];
				for(int l=0;l<3;l++){ 
					f[l] = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName(fields[k][l])]);
				}
				float var = (float)Math.sqrt(3.0/2.0*VSimpleFunctions.calcDispersion(f))/VSimpleFunctions.calcMean(f);
				//float var = (float)Math.sqrt(3.0/2.0*VSimpleFunctions.calcDispersion(f));
				fw.write(var+"\t");
				//fw.write(vt.stringTable[i][1]+"\t"+vt.stringTable[i][0]+"\t"+fields[0][0]+"\t"+fields[1][0]+"\t"+fields[2][0]+"\n");
			total+=var*var;
			}
			fw.write((float)Math.sqrt(total)+"\n");
		}
		fw.close();
		
		VDataTable vtv = VDatReadWrite.LoadFromSimpleDatFile(prefix+"sga_variation_notreatment.txt", true, "\t");
		vtv.makePrimaryHash("ORF");
		float plates[][][][] = new float[4][14][48][32];
		for(int i=0;i<vmap.rowCount;i++){
			String orf = vmap.stringTable[i][vmap.fieldNumByName("ORF")];
			int plate = Integer.parseInt(vmap.stringTable[i][vmap.fieldNumByName("PLATE")]);
			int x = Integer.parseInt(vmap.stringTable[i][vmap.fieldNumByName("X")]);
			int y = Integer.parseInt(vmap.stringTable[i][vmap.fieldNumByName("Y")]);
			Vector<Integer> vi = vtv.tableHashPrimary.get(orf);
			if(vi!=null){
				int row = vi.get(0);
				float n = Float.parseFloat(vtv.stringTable[row][vtv.fieldNumByName("N")]);
				float q = Float.parseFloat(vtv.stringTable[row][vtv.fieldNumByName("Q")]);
				float r = Float.parseFloat(vtv.stringTable[row][vtv.fieldNumByName("R")]);
				float t = Float.parseFloat(vtv.stringTable[row][vtv.fieldNumByName("TOTAL")]);
				plates[0][plate-1][x-1][y-1] = n;
				plates[1][plate-1][x-1][y-1] = q;
				plates[2][plate-1][x-1][y-1] = r;
				plates[3][plate-1][x-1][y-1] = t;
			}
			
		}
		
		float threshs[] = {0.01f,0.02f,0.03f,0.04f,0.05f,0.07f,0.1f,0.15f,0.2f,0.3f,0.4f,0.5f};
		System.out.print("PLATE\t");
		for(int k=0;k<threshs.length;k++) System.out.print(threshs[k]+"\t"); System.out.println();
		
		fw = new FileWriter(prefix+"sga_variation_plates.xls");
		for(int plate=0;plate<14;plate++){
			int counts[] = new int[threshs.length];
			fw.write("Plate "+(plate+1)+"\n");
				for(int y=0;y<32;y++){
					for(int cond=0;cond<4;cond++){
						for(int x=0;x<48;x++){
							fw.write(plates[cond][plate][x][y]+"\t");
							if(cond==3){
								for(int k=0;k<threshs.length;k++)
									if(plates[cond][plate][x][y]>threshs[k])
										counts[k]++;
							}
						}
						fw.write("\t\t");
					}
				fw.write("\n");
				}
			fw.write("\n\n");

			System.out.print((plate+1)+"\t");
			for(int k=0;k<threshs.length;k++) System.out.print(counts[k]+"\t"); System.out.println();
			
			}
		fw.close();
	}
	
	
	public static void assembleTSV(String folder, String conditions, String treatmentName, String suffix) throws Exception{
		String conds[] = conditions.split(";");
		Vector<String> vconds = new Vector<String>();
		for(String s: conds) vconds.add(s);
		HashMap<String,Vector<Float>> allscores =  new HashMap<String,Vector<Float>>();
		String outFolder = "c:/datas/sga/";
		HashMap<String,String> plates =  new HashMap<String,String>();		
		
		
		for(int i=1;i<=4;i++){
			HashMap<String, Float> scores = new HashMap<String, Float>();
			File ffolder = new File(folder+"TSV6-"+i+"/");
			for(File f: ffolder.listFiles()){
				for(int k=0;k<vconds.size();k++){
					if(f.isDirectory())if(f.getName().contains(vconds.get(k))){
						for(File sc_folder: f.listFiles()){
							if(sc_folder.getName().startsWith("nsnormalization")){
								scores = readScoresFile(sc_folder.getAbsolutePath()+"/combined_data.dat");
								
								for(String key: scores.keySet()){
									String prefix = key.substring(0, 2);
									String gene = key.substring(2, key.length());
									
									plates.put(key.substring(2,key.length()), "TSV6-"+i);									
									
									Vector<Float> v = allscores.get(gene);
									if(v==null){
										v = new Vector<Float>();
										for(int kk=0;kk<3*conds.length;kk++){
											v.add(Float.NaN);
										}
										allscores.put(gene, v);
									}
									if(prefix.equals("N_"))
										v.set(3*k, scores.get(key));
									if(prefix.equals("Q_"))
										v.set(3*k+1, scores.get(key));
									if(prefix.equals("R_"))
										v.set(3*k+2, scores.get(key));
								}
								
							}
						}
					}
				}
			}
		}
		
		FileWriter fw = new FileWriter(outFolder+"analysis/"+fileName);
		fw.write("NAME\tORF\tPLATE\t");
		for(int k=0;k<conds.length;k++){
			String condition = conds[k];
			if(condition.equals("no treatment")) condition = treatmentName;
			condition+=suffix;
			fw.write("N_"+condition+"\t");
			fw.write("Q_"+condition+"\t");
			fw.write("R_"+condition+"\t");
		}
		fw.write("\n");
		Vector<String> orfs = new Vector<String>();
		for(String key: allscores.keySet()){
			orfs.add(key);
		}
		Collections.sort(orfs);
		for(String s: orfs){
			String name = orf2name.get(s);
			if(name==null) name = s;
			fw.write(name+"\t"+s+"\t"+plates.get(s)+"\t");
			Vector<Float> v = allscores.get(s);
			for(Float f: v){
				fw.write(f+"\t");
			}
		fw.write("\n");			
		}
		fw.close();		
		
	}

	

}
