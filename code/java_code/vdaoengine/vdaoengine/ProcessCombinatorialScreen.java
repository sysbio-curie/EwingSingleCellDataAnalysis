package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.data.io.VDatReadWrite;

public class ProcessCombinatorialScreen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			String prefix = "c:/datas/aposys/combinatorial/";
			
			// Order cytoscape pair relations (from weak to strong by absolute value)
			/*String name = "network_eps_rescaled";
			VDataTable vc = VDatReadWrite.LoadFromSimpleDatFile(prefix+name+".txt", true, "\t");
			for(int i=0;i<vc.rowCount;i++){
				String gene1 = vc.stringTable[i][vc.fieldNumByName("GENE1")];
				String gene2 = vc.stringTable[i][vc.fieldNumByName("GENE2")];
				float w1 = Float.parseFloat(vc.stringTable[i][vc.fieldNumByName("W1")]);
				float w2 = Float.parseFloat(vc.stringTable[i][vc.fieldNumByName("W2")]);
				if(Math.abs(w1)>Math.abs(w2)){
					vc.stringTable[i][vc.fieldNumByName("W2")] = ""+w1;
					vc.stringTable[i][vc.fieldNumByName("W1")] = ""+w2;
					vc.stringTable[i][vc.fieldNumByName("GENE1")] = gene2;
					vc.stringTable[i][vc.fieldNumByName("GENE2")] = gene1;
				}
			}
			VDatReadWrite.saveToSimpleDatFile(vc, prefix+name+"_ordered.txt",true);
			System.exit(0);*/
			
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"int1_mod.xls", true, "\t");
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"int2.txt", true, "\t");
			VDataTable groups = VDatReadWrite.LoadFromSimpleDatFile(prefix+"groups", true, " ");
			
			String epsField = "e_rescaled";
			
			Vector<String> geneNames = new Vector<String>();
			HashMap<String,Float> wValues = new HashMap<String,Float>();
			for(int i=0;i<vt.rowCount;i++){
				String inter = vt.stringTable[i][vt.fieldNumByName("PAIR")];
				StringTokenizer st = new StringTokenizer(inter,"-");
				String gene1 = st.nextToken();
				String gene2 = st.nextToken();
				if(geneNames.indexOf(gene1)<0) geneNames.add(gene1);
				if(geneNames.indexOf(gene2)<0) geneNames.add(gene2);
				wValues.put(gene1, Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("W1")]));
				wValues.put(gene2, Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("W2")]));
			}
			Collections.sort(geneNames);
			
			Vector<Vector<Float>> eps = new Vector<Vector<Float>>();
			for(int i=0;i<geneNames.size();i++){
				Vector<Float> row = new Vector<Float>();
				for(int j=0;j<geneNames.size();j++)
					row.add(Float.NaN);
				eps.add(row);
			}
			
			HashMap<String,Integer> group = new HashMap<String,Integer>();
			for(int i=0;i<groups.rowCount;i++)
				group.put(groups.stringTable[i][groups.fieldNumByName("siRNA")], Integer.parseInt(groups.stringTable[i][groups.fieldNumByName("groupe")]));
			
			for(int i=0;i<vt.rowCount;i++){
				String inter = vt.stringTable[i][vt.fieldNumByName("PAIR")];
				StringTokenizer st = new StringTokenizer(inter,"-");
				String gene1 = st.nextToken();
				String gene2 = st.nextToken();
				int i1 = geneNames.indexOf(gene1);
				int i2 = geneNames.indexOf(gene2);
				eps.get(i1).set(i2, Float.parseFloat(vt.stringTable[i][vt.fieldNumByName(epsField)]));
				eps.get(i2).set(i1, Float.parseFloat(vt.stringTable[i][vt.fieldNumByName(epsField)]));
				eps.get(i1).set(i1, wValues.get(gene1));
				eps.get(i2).set(i2, wValues.get(gene2));
				//eps.get(i1).set(i1, 0f);
				//eps.get(i2).set(i2, 0f);
			}
			
			FileWriter fw = new FileWriter(prefix+"int_"+epsField+".txt");
			
			float epsmaxplus = 0;
			float epsmaxminus = 0;
			
			fw.write("GENENAME\tGROUP\tW\t");
			for(int i=0;i<geneNames.size();i++) fw.write(geneNames.get(i)+"\t"); fw.write("\n");
			for(int i=0;i<eps.size();i++){
				fw.write(geneNames.get(i)+"\t"+group.get(geneNames.get(i))+"\t"+wValues.get(geneNames.get(i))+"\t");
				for(int j=0;j<eps.size();j++) fw.write(eps.get(i).get(j)+"\t");
				for(int j=0;j<eps.size();j++){
					float e = eps.get(i).get(j);
					if((e>0)&&(e>epsmaxplus)) epsmaxplus = e; 
					if((e<0)&&(Math.abs(e)>epsmaxminus)) epsmaxminus = Math.abs(e);
				}
				fw.write("\n");
			}
			
			fw.close();
			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(prefix+"int_"+epsField+".txt", true, "\t");
			for(int i=1;i<vt1.colCount;i++)
				vt1.fieldTypes[i] = vt1.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt1, prefix+"int_"+epsField+".dat");
			
			fw = new FileWriter(prefix+"int_"+epsField+"2.veo");
			
			float radmaxplus = 0;
			float radmaxminus = 0;
			for(int i=0;i<geneNames.size();i++){
				float w = wValues.get(geneNames.get(i));
				if((w>0)&&(w>radmaxplus)) radmaxplus = w; 
				if((w<0)&&(Math.abs(w)>radmaxminus)) radmaxminus = Math.abs(w);
			}
			
			for(int i=0;i<geneNames.size();i++){
				fw.write(geneNames.get(i)+" sphere ");
				for(int j=0;j<geneNames.size();j++)
					fw.write(eps.get(i).get(j)+" ");
				int gr = group.get(geneNames.get(i));
				float w = wValues.get(geneNames.get(i));
				float rad = Math.abs(wValues.get(geneNames.get(i)))+0.1f;
				/*switch(gr){
					case 1: fw.write(" "+rad+" 255 0 0"); break;
					case 2: fw.write(" "+rad+" 0 255 0"); break;
					case 3: fw.write(" "+rad+" 0 0 255"); break;
					case 4: fw.write(" "+rad+" 255 255 0"); break;
					case 5: fw.write(" "+rad+" 255 0 255"); break;
					case 6: fw.write(" "+rad+" 0 255 255"); break;
				}*/
				if(w>0)
					fw.write(" "+rad+" "+(int)(((rad-0.1f)/radmaxplus)*255)+" 0 0");
				else
					fw.write(" "+rad+" 0 "+(int)(((rad-0.1f)/radmaxminus)*255)+" 0");
				fw.write("\n");
			}
			float epsthreshplus = 0.3f;
			float epsthreshminus = 0.3f;
			for(int i=0;i<geneNames.size();i++)
				for(int j=0;j<geneNames.size();j++)if(i!=j){
					String genei = geneNames.get(i);
					String genej = geneNames.get(j);
					float epsilon = eps.get(i).get(j);
					if((epsilon>0)&&(Math.abs(epsilon)>epsthreshplus)){
						float rad = Math.abs(epsilon)*0.1f;
						fw.write(genei+"_"+genej+" cylinder connect "+genei+" "+genej+" "+rad+" "+rad+" ");
						rad = rad*10f;
						fw.write((int)((rad/epsmaxplus)*255)+" "+(int)((rad/epsmaxplus)*255)+" 0\n");
					}
					if((epsilon<0)&&(Math.abs(epsilon)>epsthreshminus)){
						float rad = Math.abs(epsilon)*0.1f;
						fw.write(genei+"_"+genej+" cylinder connect "+genei+" "+genej+" "+rad+" "+rad+" ");
						rad = rad*10f;
						fw.write("0 "+(int)((rad/epsmaxminus)*255)+" "+(int)((rad/epsmaxminus)*255)+"\n");
					}
				}
			fw.close();
			
			fw = new FileWriter(prefix+"degrees_"+epsField+".xls");
			fw.write("GENE\tPOS_WEIGHT\tNEG_WEIGHT\tABS_WEIGHT\tPOS_WEIGHT_TRUNC\tNEG_WEIGHT_TRUNC\tABS_WEIGHT_TRUNC\n");
			for(int i=0;i<geneNames.size();i++){
				fw.write(geneNames.get(i)+"\t");
				float posw = 0f;
				float negw = 0f;
				float absw = 0f;
				float poswt = 0f;
				float negwt = 0f;
				float abswt = 0f;
				for(int j=0;j<geneNames.size();j++){
					if(eps.get(i).get(j)>0) { posw+=eps.get(i).get(j);
						if(eps.get(i).get(j)>epsthreshplus){
							poswt+=eps.get(i).get(j);
							abswt+=eps.get(i).get(j);
						}
					}
					if(eps.get(i).get(j)<0) { negw+=Math.abs(eps.get(i).get(j));
						if(Math.abs(eps.get(i).get(j))>epsthreshminus){
							negwt+=Math.abs(eps.get(i).get(j));
							abswt+=Math.abs(eps.get(i).get(j));
						}
					}
					absw+=Math.abs(eps.get(i).get(j));
				}
				fw.write(posw+"\t"+negw+"\t"+absw+"\t"+poswt+"\t"+negwt+"\t"+abswt+"\n");
			}
			fw.close();
			

			// Work with modules
			
			Vector<String> modulesNames = new Vector<String>();
			//modulesNames.add("module_upgma_1");
			//modulesNames.add("module_upgma_2");
			//modulesNames.add("module_upgma_3");
			//modulesNames.add("module_upgma_4");
			//modulesNames.add("module_mds_1");
			//modulesNames.add("module_mds_2");
			//modulesNames.add("module_mds_3");
			modulesNames.add("module_big1");
			modulesNames.add("module_big2");
			
			Vector<Vector<String>> modules = new Vector<Vector<String>>();
			for(int i=0;i<modulesNames.size();i++){
				Vector<String> list = vdaoengine.utils.Utils.loadStringListFromFile(prefix+"modules/"+modulesNames.get(i));
				modules.add(list);
			}
			
			// insert module information into the table
			
			boolean doSampling = false;
			int numberOfSamples = 1;
			
			Random r = new Random(); 

			for(int k=0;k<numberOfSamples;k++){
				
			if(doSampling)
			for(int j=0;j<modules.size();j++){
				for(int j1=0;j1<modules.get(j).size();j1++){
					modules.get(j).set(j1, vt1.stringTable[r.nextInt(vt1.rowCount)][vt1.fieldNumByName("GENENAME")]);
				}
			}
			
			VDataTable vtmod = VDatReadWrite.LoadFromSimpleDatFile(prefix+"int1.txt", true, "\t");
			vtmod.addNewColumn("epsm", "", "", vtmod.STRING, "");
			vtmod.addNewColumn("epsavm", "", "", vtmod.STRING, "");
			vtmod.addNewColumn("epsavmd", "", "", vtmod.STRING, "");			
			vtmod.addNewColumn("MODULE1", "", "", vtmod.STRING, "");
			vtmod.addNewColumn("MODULE2", "", "", vtmod.STRING, "");
			vtmod.addNewColumn("MODMOD", "", "", vtmod.STRING, "");
			vtmod.addNewColumn("TYPE", "", "", vtmod.STRING, "");
			
			float alpha_weight = 0.657f;
			float theta_thresh_plus = 0.2f;
			float theta_thresh_minus = 0.2f;
			
			for(int i=0;i<vtmod.rowCount;i++){
				String gene1 = vtmod.stringTable[i][vtmod.fieldNumByName("GENE1")];
				String gene2 = vtmod.stringTable[i][vtmod.fieldNumByName("GENE2")];
				String module1 = "/";
				String module2 = "/";
				for(int j=0;j<modulesNames.size();j++){
					if(modules.get(j).indexOf(gene1)>=0) module1 = modulesNames.get(j);
					if(modules.get(j).indexOf(gene2)>=0) module2 = modulesNames.get(j);
				}
				vtmod.stringTable[i][vtmod.fieldNumByName("MODULE1")] = module1;
				vtmod.stringTable[i][vtmod.fieldNumByName("MODULE2")] = module2;
				if(module1.compareTo(module2)<0) vtmod.stringTable[i][vtmod.fieldNumByName("MODMOD")] = module1+"_"+module2;
				if(module1.compareTo(module2)>0) vtmod.stringTable[i][vtmod.fieldNumByName("MODMOD")] = module2+"_"+module1;
				vtmod.stringTable[i][vtmod.fieldNumByName("TYPE")] = "intermodule"; 
				if(module1.compareTo(module2)==0){ vtmod.stringTable[i][vtmod.fieldNumByName("MODMOD")] = module1+"_"+module2;
					vtmod.stringTable[i][vtmod.fieldNumByName("TYPE")] = "intramodule";
				}
				float w1 = Float.parseFloat(vtmod.stringTable[i][vtmod.fieldNumByName("W1")]);
				float w2 = Float.parseFloat(vtmod.stringTable[i][vtmod.fieldNumByName("W2")]);
				float w12 = Float.parseFloat(vtmod.stringTable[i][vtmod.fieldNumByName("W12")]);
				float w = (w1+w2)*alpha_weight;
				float epsm = 0f;
				float epsavm = 0f;
				int epsavmd = 0;
				if(w>0){
					epsm = w12-w*1f/alpha_weight;
					epsavm = w12-w;
				}else{
					epsm = w*alpha_weight-w12;
					epsavm = w-w12;
				}
				if(epsavm>theta_thresh_plus) epsavmd = 1;
				if(epsavm<-theta_thresh_minus) epsavmd = -1;
				
				
				vtmod.stringTable[i][vtmod.fieldNumByName("epsm")] = ""+epsm;
				vtmod.stringTable[i][vtmod.fieldNumByName("epsavm")] = ""+epsavm;
				vtmod.stringTable[i][vtmod.fieldNumByName("epsavmd")] = ""+epsavmd;
				
			}
			VDatReadWrite.saveToSimpleDatFile(vtmod, prefix+"int1_mod.xls", true);
			VDatReadWrite.saveToVDatFile(vtmod, prefix+"int1_mod.dat");
			
			// Now do some calculations on vtmod, to see if positive epistasis is biased inside modules
			
			epsField = "epsm";
			boolean countValues = true;
			float thresh = 0.15f;
			Vector<Float> moduleIntraNegative = new Vector<Float>();
			Vector<Float> moduleExternalNegative = new Vector<Float>();
			Vector<Float> moduleIntraNeutral = new Vector<Float>();
			Vector<Float> moduleExternalNeutral = new Vector<Float>();
			Vector<Float> moduleIntraPositive = new Vector<Float>();
			Vector<Float> moduleExternalPositive = new Vector<Float>();
			for(int i=0;i<modulesNames.size();i++){
				moduleIntraNegative.add(0f);
				moduleExternalNegative.add(0f);
				moduleIntraNeutral.add(0f);
				moduleExternalNeutral.add(0f);
				moduleIntraPositive.add(0f);
				moduleExternalPositive.add(0f);
			}
			for(int i=0;i<vtmod.rowCount;i++){
				String module1 = vtmod.stringTable[i][vtmod.fieldNumByName("MODULE1")];
				String module2 = vtmod.stringTable[i][vtmod.fieldNumByName("MODULE2")];
				int ind1 = modulesNames.indexOf(module1);
				int ind2 = modulesNames.indexOf(module2);
				float epsv = Float.parseFloat(vtmod.stringTable[i][vtmod.fieldNumByName(epsField)]);
				if((ind1!=-1)&&(ind2!=-1)){
				if(epsv>thresh){
					if(ind1==ind2){
						if(countValues) moduleIntraPositive.set(ind1, moduleIntraPositive.get(ind1)+1);
								   else moduleIntraPositive.set(ind1, moduleIntraPositive.get(ind1)+epsv);
					}else{
						if(countValues) moduleExternalPositive.set(ind1, moduleExternalPositive.get(ind1)+1);
						           else moduleExternalPositive.set(ind1, moduleExternalPositive.get(ind1)+epsv);
						if(countValues) moduleExternalPositive.set(ind2, moduleExternalPositive.get(ind2)+1);
						           else moduleExternalPositive.set(ind2, moduleExternalPositive.get(ind2)+epsv);
					}
				}
				if((epsv<thresh)&&(epsv>-thresh)){
					if(ind1==ind2){
						if(countValues) moduleIntraNeutral.set(ind1, moduleIntraNeutral.get(ind1)+1);
								   else moduleIntraNeutral.set(ind1, moduleIntraNeutral.get(ind1)+Math.abs(epsv));
					}else{
						if(countValues) moduleExternalNeutral.set(ind1, moduleExternalNeutral.get(ind1)+1);
						           else moduleExternalNeutral.set(ind1, moduleExternalNeutral.get(ind1)+Math.abs(epsv));
						if(countValues) moduleExternalNeutral.set(ind2, moduleExternalNeutral.get(ind2)+1);
						           else moduleExternalNeutral.set(ind2, moduleExternalNeutral.get(ind2)+Math.abs(epsv));
					}					
				}
				if(epsv<-thresh){
					if(ind1==ind2){
						if(countValues) moduleIntraNegative.set(ind1, moduleIntraNegative.get(ind1)+1);
								   else moduleIntraNegative.set(ind1, moduleIntraNegative.get(ind1)+epsv);
					}else{
						if(countValues) moduleExternalNegative.set(ind1, moduleExternalNegative.get(ind1)+1);
						           else moduleExternalNegative.set(ind1, moduleExternalNegative.get(ind1)+epsv);
						if(countValues) moduleExternalNegative.set(ind2, moduleExternalNegative.get(ind2)+1);
						           else moduleExternalNegative.set(ind2, moduleExternalNegative.get(ind2)+epsv);
					}					
				}
				}
			}
			VStatistics stat = new VStatistics(3);
			float f[] = new float[3];
			System.out.println("MODULE\tNEG_IN\tNEG_EX\tNEG_RATIO\tNTR_IN\tNTR_EX\tNTR_RATIO\tPOS_IN\tPOS_EX\tPOS_RATIO");
			for(int i=0;i<modulesNames.size();i++){
				f[0] = moduleExternalNegative.get(i)/moduleIntraNegative.get(i);
				f[1] = moduleExternalNeutral.get(i)/moduleIntraNeutral.get(i);
				f[2] = moduleExternalPositive.get(i)/moduleIntraPositive.get(i);
				System.out.println(modulesNames.get(i)+"\t"+moduleIntraNegative.get(i)+"\t"+moduleExternalNegative.get(i)+"\t"+f[0]+"\t"+moduleIntraNeutral.get(i)+"\t"+moduleExternalNeutral.get(i)+"\t"+f[1]+"\t"+moduleIntraPositive.get(i)+"\t"+moduleExternalPositive.get(i)+"\t"+f[2]);
				//if(!Float.isInfinite(f[0]))if(!Float.isInfinite(f[1]))if(!Float.isInfinite(f[2]))
				stat.addNewPoint(f);
			}
			stat.calculate();
			System.out.println("Epistasis\tMean\tStdDev");
			System.out.println("Negative epistasis\t"+stat.getMean(0)+"\t"+stat.getStdDev(0));
			System.out.println("Neutral epistasis\t"+stat.getMean(1)+"\t"+stat.getStdDev(1));
			System.out.println("Positive epistasis\t"+stat.getMean(2)+"\t"+stat.getStdDev(2));
			}
			 // modules end
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
