package getools.scripts;

import java.util.*;
import java.io.FileWriter;
import java.text.*;

import vdaoengine.analysis.PCAMethod;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;
import getools.*;

public class process3LinesEwing {

	public static void main(String[] args) {
		try{
			
			String path = "c:/datas/ewing/kinetics/karineClones/norm1/";
			
			/*FileWriter fwt = new FileWriter(path+"shtest");
			for(float x=0;x<100;x+=0.01f){
				float asinh = (float)Math.log(x+Math.sqrt(x*x+1));
				fwt.write(x+"\t"+asinh+"\n");
			}
			fwt.close();*/
			
			
			//processFitScores(path); System.exit(0);
			
			//VDataTable vtglobal = VDatReadWrite.LoadFromSimpleDatFile(path+"3lines.txt", true, "\t");
			VDataTable vtglobal = VDatReadWrite.LoadFromSimpleDatFile(path+"E17neg.GCRMA.txt", true, "\t");
			//VDataTable vtglobal = VDatReadWrite.LoadFromSimpleDatFile(path+"ewingmas5norm.txt", true, "\t");
			//System.out.println(vtglobal.rowCount+"\t"+vtglobal.colCount);
			
			/*String A673Clone1[] = {"A673_1CT0","A673_1CT1","A673_1CT2","A673_1CT3","A673_1CT6","A673_1CT9","A673_1CT11","A673_1CT13","A673_1CT15","A673_1CT17","A673_1CT13T3","A673_1CT15T5","A673_1CT17T7"};
			String A673Clone2[] = {"A673_2CT0","A673_2CT1","A673_2CT2","A673_2CT3","A673_2CT6","A673_2CT9","A673_2CT11","A673_2CT13","A673_2CT15","A673_2CT17","A673_2CT13T3","A673_2CT15T5","A673_2CT17T7"};
			String C22[] = {"C22T0","C22T1","C22T2","C22T3","C22T4","C22T5","C22T6","C22T7","C22T8","C22T9","C22T10","C22T11","C22T12","C22T13","C22T14","C22T15","C22T16","C22T17","C22T18","C22T19","C22T20","C22T21","C22T22","C22T23","C22T24"};
			String E17[] = {"E17T0","E17T1","E17T2","E17T3","E17T4","E17T5","E17T6","E17T7","E17T8","E17T9","E17T10","E17T11","E17T12","E17T13","E17T14","E17T15","E17T16","E17T17","E17T18","E17T19","E17T20","E17T21","E17T22","E17T23","E17T24"};			

			String A673Clone1_control[] = {"A673_1CT0","A673_1CT1","A673_1CT2","A673_1CT3","A673_1CT6","A673_1CT9","A673_1CT11","A673_1CT13","A673_1CT15","A673_1CT17"};
			String A673Clone2_control[] = {"A673_2CT0","A673_2CT1","A673_2CT2","A673_2CT3","A673_2CT6","A673_2CT9","A673_2CT11","A673_2CT13","A673_2CT15","A673_2CT17"};
			String C22_control[] = {"C22T1","C22T3","C22T5","C22T7","C22T9","C22T11","C22T13","C22T16","C22T19","C22T22"};
			String E17_control[] = {"E17T1","E17T3","E17T5","E17T7","E17T9","E17T11","E17T13","E17T16","E17T19","E17T22"};*/
			

			/*String C22_act[] = {"C22T0","C22T2","C22T4","C22T6","C22T8","C22T10","C22T12","C22T14","C22T15","C22T17","C22T18","C22T20","C22T21","C22T23","C22T24"};
			String C22_control[] = {"C22T1","C22T3","C22T5","C22T7","C22T9","C22T11","C22T13","C22T16","C22T19","C22T22"};*/
			String E17_act[] = {"E17T0","E17T2","E17T4","E17T6","E17T8","E17T10","E17T12","E17T14","E17T15","E17T17","E17T18","E17T20","E17T21","E17T23","E17T24"};			
			String E17_control[] = {"E17T1","E17T3","E17T5","E17T7","E17T9","E17T11","E17T13","E17T16","E17T19","E17T22"};
			
			String empty[] = {};
			
			Vector<String> fnames = new Vector<String>();
			fnames.add("C22i");
			fnames.add("C22nci");
			fnames.add("E17i");
			fnames.add("E17nci");
			
			//VDatReadWrite.useQuotesEverywhere = false;			
			
			//VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile(path+"annot4.txt", true, "\t");
			
			/*VDataTable vtA673_1 = processTable(vtglobal,A673Clone1,A673Clone1_control);
			VDataTable vtA673_1a = VSimpleProcedures.MergeTables(vtA673_1, "Probeset", annot, "Probeset", "_"); 
			VDatReadWrite.saveToVDatFile(vtA673_1a, path+"A673_c1.dat");

			VDataTable vtA673_2 = processTable(vtglobal,A673Clone2,A673Clone2_control);
			VDataTable vtA673_2a = VSimpleProcedures.MergeTables(vtA673_2, "Probeset", annot, "Probeset", "_"); 
			VDatReadWrite.saveToVDatFile(vtA673_2a, path+"A673_c2.dat");*/

			/*VDataTable vtC22 = processTable(vtglobal,E17_control,empty);
			VDataTable vtC22a = VSimpleProcedures.MergeTables(vtC22, "Probeset", annot, "Probeset", "_"); 
			VDatReadWrite.saveToVDatFile(vtC22a, path+"E17nci.dat");
			
			VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(path+"E17nci.dat");
			VDataTable vt2 = filterGenesNonExpressedInAverage(vt1,3.5f);
			VDatReadWrite.saveToVDatFile(vt2, path+"E17ncif.dat");*/
			

			/* VDataTable vtE17 = processTable(vtglobal,E17,E17_control);
			VDataTable vtE17a = VSimpleProcedures.MergeTables(vtE17, "Probeset", annot, "Probeset", "_"); 
			VDatReadWrite.saveToVDatFile(vtE17a, path+"E17i.dat");*/
			
			String C22[] = {"C22_T0","C22_T1","C22_T2","C22_T3","C22_T6","C22_T9","C22_T11","C22_T13","C22_T15","C22_T17","C22_T20","C22_T13T3","C22_T15T5","C22_T17T7","C22_T20T10"};
			String C22act[] = {"C22_T0","C22_T1","C22_T2","C22_T3","C22_T6","C22_T9","C22_T11","C22_T13","C22_T15","C22_T17","C22_T20"};
			String C22nc[] = {"C22_T0","C22nc_T1","C22nc_T2","C22nc_T3","C22nc_T6","C22nc_T9","C22nc_T11","C22nc_T13","C22nc_T15","C22nc_T17","C22nc_T20"};

			String C22act1[] = {"C22_T1","C22_T2","C22_T3","C22_T6","C22_T9","C22_T11","C22_T13","C22_T15","C22_T17","C22_T20"};
			String C22nc1[] = {"C22nc_T1","C22nc_T2","C22nc_T3","C22nc_T6","C22nc_T9","C22nc_T11","C22nc_T13","C22nc_T15","C22nc_T17","C22nc_T20"};			
			
			String E17[] = {"E17_T0","E17_T1","E17_T2","E17_T3","E17_T6","E17_T9","E17_T11","E17_T13","E17_T15","E17_T17","E17_T20","E17_T13T3","E17_T15T5","E17_T17T7","E17_T20T10"};
			String E17nc[] = {"E17_T0","E17nc_T1","E17nc_T2","E17nc_T3","E17nc_T6","E17nc_T9","E17nc_T11","E17nc_T13","E17nc_T15","E17nc_T17","E17nc_T20"};
			String E17act[] = {"E17_T0","E17_T1","E17_T2","E17_T3","E17_T6","E17_T9","E17_T11","E17_T13","E17_T15","E17_T17","E17_T20"};
			
			String E17nc1[] = {"E17nc_T1","E17nc_T2","E17nc_T3","E17nc_T6","E17nc_T9","E17nc_T11","E17nc_T13","E17nc_T15","E17nc_T17","E17nc_T20"};
			String E17act1[] = {"E17_T1","E17_T2","E17_T3","E17_T6","E17_T9","E17_T11","E17_T13","E17_T15","E17_T17","E17_T20"};
			
			
			String A673c1[] = {"A673_1CT0","A673_1CT1","A673_1CT2","A673_1CT3","A673_1CT6","A673_1CT9","A673_1CT11","A673_1CT13","A673_1CT15","A673_1CT17","A673_1CT13T3","A673_1CT15T5","A673_1CT17T7"};
			String A673c2[] = {"A673_2CT0","A673_2CT1","A673_2CT2","A673_2CT3","A673_2CT6","A673_2CT9","A673_2CT11","A673_2CT13","A673_2CT15","A673_2CT17","A673_2CT13T3","A673_2CT15T5","A673_2CT17T7"};
			String A673c1act[] = {"A673_1CT0","A673_1CT1","A673_1CT2","A673_1CT3","A673_1CT6","A673_1CT9","A673_1CT11","A673_1CT13","A673_1CT15","A673_1CT17"};
			String A673c2act[] = {"A673_2CT0","A673_2CT1","A673_2CT2","A673_2CT3","A673_2CT6","A673_2CT9","A673_2CT11","A673_2CT13","A673_2CT15","A673_2CT17"};
			
			float times[] = {0,1,2,3,6,9,11,13,15,17,20};
			
			/*for(int k=0;k<6;k++){
				Vector columns = new Vector();
				String mas[] = null; String name = null; String out = null;
				switch(k){
					case 0:  name = "A673_c1f";  mas = A673c1; out = "A673_c1f_act"; break;
					case 1:  name = "A673_c2f";  mas = A673c2; out = "A673_c2f_act"; break;
					case 2:  name = "C22if";  mas = C22; out = "C22if_act"; break;
					case 3:  name = "E17if";  mas = E17; out = "E17if_act"; break;
					case 4:  name = "C22if";  mas = C22nc; out = "C22if_nc"; break;
					case 5:  name = "E17if";  mas = E17nc; out = "E17if_nc"; break;
				}
				VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(path+name+".dat");
				columns.add("Probeset"); columns.add("GeneTitle");
				columns.add("GeneSymbol"); columns.add("Location"); 
				columns.add("Pathway");
				for(int i=0;i<mas.length;i++) columns.add(mas[i]);
				vt1 = VSimpleProcedures.SelectColumns(vt1, columns);
				vt1 = TableUtils.normalizeVDat(vt1, true, false);
				VDatReadWrite.saveToVDatFile(vt1, path+out+".dat");
				VDataTable vt2 = TableUtils.filterByVariation(vt1, 3000, false);
				VDatReadWrite.saveToVDatFile(vt2, path+out+"_3000.dat");
			}*/
			
			/*for(int i=0;i<fnames.size();i++){
				VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(path+fnames.get(i)+".dat");
				VDataTable vt2 = filterGenesNonExpressedInAverage(vt1,3.5f);
				VDatReadWrite.saveToVDatFile(vt2, path+fnames.get(i)+"f.dat");
			}*/
			/*for(int i=3;i<fnames.size();i++){
				VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(path+fnames.get(i)+"f.dat");
				System.out.println(vt1.rowCount+"\t"+vt1.colCount);
				vt1 = TableUtils.normalizeVDat(vt1, true, false);
				VDatReadWrite.saveToVDatFile(vt1, path+fnames.get(i)+"fc.dat");
			}*/
			
			//calcCorrelationVariation(path+"A673_c1fc.dat",path+"A673_c2fc.dat",A673c1act,A673c2act,path+"A673_12act");
			//calcCorrelationVariation(path+"A673_c1fc.dat",path+"A673_c2fc.dat",A673c1,A673c2,path+"A673_12");
			//calcCorrelationVariation(path+"C22ifc.dat",path+"E17ifc.dat",C22act,E17act,path+"C22_E17act");
			//calcCorrelationVariation(path+"C22ifc.dat",path+"E17ifc.dat",C22,E17,path+"C22_E17");			
			//calcCorrelationVariation(path+"C22ifc.dat",path+"C22ncifc.dat",C22act1,C22nc1,path+"C22_nc");
			calcCorrelationVariation(path+"E17ifc.dat",path+"E17ncifc.dat",E17act1,E17nc1,path+"E17_nc");
			//calcCorrelationVariation(path+"A673_c1f_act.dat",path+"E17if_act.dat",A673c1act,E17act,path+"A673c1_E17");
			
			//calcCorrelationVariation(path+"A673_c1fc.dat",path+"E17ifc.dat",A673c1act,E17act,path+"A673c1_E17");
			
			//calcRegressionCoeffs(path+"A673_c1fc.dat",A673c1act,times,path+"A673c1_regr");
			//calcRegressionCoeffs(path+"A673_c2fc.dat",A673c2act,times,path+"A673c2_regr");
			//calcRegressionCoeffs(path+"E17ifc.dat",E17act,times,path+"E17_regr");
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(path+"commongenes_vb.txt",true,"\t");
			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile(path+"A673c1_regr.xls",true,"\t");
			VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile(path+"A673c2_regr.xls",true,"\t");
			VDataTable vt3 = VDatReadWrite.LoadFromSimpleDatFile(path+"E17_regr.xls",true,"\t");
			vt = VSimpleProcedures.MergeTables(vt, "Probeset", vt1, "Probeset", "@");
			vt = VSimpleProcedures.MergeTables(vt, "Probeset", vt2, "Probeset", "@");
			vt = VSimpleProcedures.MergeTables(vt, "Probeset", vt3, "Probeset", "@");
			VDatReadWrite.saveToSimpleDatFile(vt, path+"commongenes1.xls");*/
			
			/*for(int k=0;k<6;k++){
			String name = null; 
			switch(k){
				case 0:  name = "A673_c1f_act"; break;
				case 1:  name = "A673_c2f_act"; break;
				case 2:  name = "C22if_act"; break;
				case 3:  name = "E17if_act"; break;
				case 4:  name = "C22if_nc"; break;
				case 5:  name = "E17if_nc"; break;
			}
			VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(path+name+".dat");
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt1, -1);
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.calcBasis(1);
			VDataSet vdp = pca.getProjectedDataset();
			FileWriter fw = new FileWriter(path+name+"_pc1u.xls");
			HashMap<String,Float> pc1projs = new HashMap<String,Float>();
			HashMap<String,Integer> pathcounts = new HashMap<String,Integer>();
			for(int i=0;i<vt1.rowCount;i++){
				String gs = vt1.stringTable[i][vt1.fieldNumByName("GeneSymbol")];
				float cur_proj = vdp.massif[i][0];
				Float proj = pc1projs.get(gs);
				if(proj==null) proj = new Float(0f);
				if(Math.abs(cur_proj)>Math.abs(proj)) proj = cur_proj;
				pc1projs.put(gs, proj);
				String pth = vt1.stringTable[i][vt1.fieldNumByName("Pathway")];
				StringTokenizer st = new StringTokenizer(pth,"/");
				while(st.hasMoreTokens()){
					String cpth = st.nextToken();
					Integer count = pathcounts.get(cpth);
					if(count==0) count = new Integer(0);
					count++; pathcounts.put(cpth, count);
				}
			}
			Iterator<String> it = pc1projs.keySet().iterator();
			while(it.hasNext()){
				String s = it.next();
				fw.write(s+"\t"+pc1projs.get(s)+"\n");
			}
			it = pathcounts.keySet().iterator();
			while(it.hasNext()){
				String s = it.next();
				fw.write(s+"\t"+pathcounts.get(s)+"\n");
			}			
			fw.close();
		}*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static VDataTable processTable(VDataTable table, String columns[], String controls[]){
		Vector v = new Vector();
		
		v.add("Probeset");		
		for(int i=0;i<columns.length;i++) v.add(columns[i]);
		VDataTable vt = VSimpleProcedures.SelectColumns(table, v);
		float f[] = new float[columns.length];
		float fc[] = new float[controls.length];
		DecimalFormat fm = new DecimalFormat("#.###");
		for(int i=0;i<columns.length;i++) vt.fieldTypes[vt.fieldNumByName(columns[i])] = VDataTable.NUMERICAL;
		
		//vt.rowCount = 100;
		
		for(int k=0;k<table.rowCount;k++){
		//for(int k=0;k<100;k++){
			for(int i=0;i<columns.length;i++) f[i] = Float.parseFloat(vt.stringTable[k][vt.fieldNumByName(columns[i])]);
			for(int i=0;i<controls.length;i++) fc[i] = Float.parseFloat(vt.stringTable[k][vt.fieldNumByName(controls[i])]);
			float average = 0f;
			for(int i=0;i<controls.length;i++)  average+=fc[i];
			average/=fc.length;
			//for(int i=0;i<columns.length;i++) f[i]-=average;
			for(int i=0;i<columns.length;i++) vt.stringTable[k][vt.fieldNumByName(columns[i])] = ""+fm.format(f[i]);
		}
		
		return vt;
	}
	
	public static VDataTable filterGenesNonExpressedInAverage(VDataTable vt, float threshold){
		VDataTable vtr = new VDataTable();
		vtr.copyHeader(vt);
		Vector rows = new Vector();
		Vector<Float> mvalues = new Vector<Float>();
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		for(int i=0;i<vd.pointCount;i++){
			float mn = VSimpleFunctions.calcMean(vd.getVector(i));
			if(mn>threshold){
				rows.add(vt.stringTable[i]);
				mvalues.add(mn);
			}
		}
		System.out.println(rows.size()+" rows are retained ("+(1f*rows.size()/vt.rowCount*100)+"%)");
		vtr.rowCount = rows.size();
		vtr.stringTable = new String[vtr.rowCount][vtr.colCount];
		float mv[] = new float[rows.size()];
		for(int i=0;i<mv.length;i++) mv[i]=mvalues.get(i);
		int inds[] = Algorithms.SortMass(mv);
		for(int i=0;i<rows.size();i++)
			vtr.stringTable[i] = (String[])rows.get(inds[vtr.rowCount-i-1]);
		return vtr;
	}
	
	public static void calcCorrelationVariation(String fn1, String fn2, String fields1[], String fields2[], String outname){
		VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(fn1);
		VDataTable vt2 = VDatReadWrite.LoadFromVDatFile(fn2);
		vt1.makePrimaryHash("Probeset"); vt2.makePrimaryHash("Probeset");
		HashSet<String> ids = new HashSet<String>();
		for(int i=0;i<vt1.rowCount;i++) ids.add(vt1.stringTable[i][vt1.fieldNumByName("Probeset")]);
		for(int i=0;i<vt2.rowCount;i++) ids.add(vt2.stringTable[i][vt2.fieldNumByName("Probeset")]);
		Vector<String> idsv = new Vector<String>();
		Iterator<String> it = ids.iterator();
		while(it.hasNext()) idsv.add(it.next());
		float correlations[] = new float[idsv.size()];
		float sp_correlations[] = new float[idsv.size()];
		float abscorrelations[] = new float[idsv.size()];
		float var1[] = new float[idsv.size()];
		float var2[] = new float[idsv.size()];
		float varm[] = new float[idsv.size()];
		String names[] =  new String[idsv.size()];
		
		for(int i=0;i<idsv.size();i++){
			float x1[] = new float[fields1.length];
			float x2[] = new float[fields1.length];
			Vector<Integer> v1 = vt1.tableHashPrimary.get(idsv.get(i));
			Vector<Integer> v2 = vt2.tableHashPrimary.get(idsv.get(i));
			int k1=-1; int k2=-1;
			if(v1!=null)if(v1.size()>0) k1 = v1.get(0);
			if(v2!=null)if(v2.size()>0) k2 = v2.get(0);
			if(k1!=-1)if(k2!=-1){
				for(int j=0;j<fields1.length;j++) x1[j] = Float.parseFloat(vt1.stringTable[k1][vt1.fieldNumByName(fields1[j])]);
				for(int j=0;j<fields1.length;j++) x2[j] = Float.parseFloat(vt2.stringTable[k2][vt2.fieldNumByName(fields2[j])]);
				correlations[i] = VSimpleFunctions.calcCorrelationCoeff(x1, x2);
				sp_correlations[i] = VSimpleFunctions.calcSpearmanCorrelationCoeff(x1, x2);
				abscorrelations[i] = Math.abs(VSimpleFunctions.calcCorrelationCoeff(x1, x2));
				var1[i] = VSimpleFunctions.calcStandardDeviation(x1);
				var2[i] = VSimpleFunctions.calcStandardDeviation(x2);
				varm[i] = (float)Math.sqrt(var1[i]*var1[i]+var2[i]*var2[i]);
				names[i] = vt1.stringTable[k1][vt1.fieldNumByName("GeneSymbol")];
			}
		}
		
		int inds[] = Algorithms.SortMass(varm);
		
		try{
			FileWriter fw = new FileWriter(outname+".xls");
			fw.write("Probeset\tGeneSymbol\tCORR\tABSCORR\tCORR_SP\tVAR1\tVAR2\tVARM\n");
			for(int i=0;i<correlations.length;i++){
				//int k = inds[i];
				int k = inds[correlations.length-i-1];
				fw.write(idsv.get(k)+"\t"+names[k]+"\t"+correlations[k]+"\t"+abscorrelations[k]+"\t"+sp_correlations[k]+"\t"+var1[k]+"\t"+var2[k]+"\t"+varm[k]+"\n");
			}
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void calcRegressionCoeffs(String fn1, String fields1[], float times[], String outname){
		VDataTable vt1 = VDatReadWrite.LoadFromVDatFile(fn1);
		try{
			FileWriter fw = new FileWriter(outname+".xls");
			fw.write("Probeset\tGeneSymbol\t"+outname+"\n");
		for(int i=0;i<vt1.rowCount;i++){
			float x[] = new float[fields1.length];
			float y[] = new float[fields1.length];
			for(int j=0;j<fields1.length;j++){
				x[j] = times[j];
				y[j] = Float.parseFloat(vt1.stringTable[i][vt1.fieldNumByName(fields1[j])]);
			}
			float c[] = VSimpleFunctions.calcLinearRegression(x, y);
			//System.out.println(c[0]+"\t"+c[1]);
			fw.write(vt1.stringTable[i][vt1.fieldNumByName("Probeset")]+"\t"+vt1.stringTable[i][vt1.fieldNumByName("GeneSymbol")]+"\t"+c[1]+"\n");
		}
		
		fw.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	public static void processFitScores(String path){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(path+"ResA673SKNMCExprFits_a.xls", true, "\t");
		vt.makePrimaryHash("GeneSymbol");
		Set genes = vt.tableHashPrimary.keySet();
		try{
		/*VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile(path+"annot4.txt", true, "\t");
		//System.out.println(""+vt.fieldNumByName("Probeset")+"\t"+annot.fieldNumByName("Probeset"));
		vt = VSimpleProcedures.MergeTables(vt, "Probeset", annot, "Probeset", "_");
		VDatReadWrite.saveToSimpleDatFile(vt, path+"ResA673SKNMCExprFits_a.xls");*/
		String scoresToTest[] = {"A673Cl1ThScore","A673Cl2ThScore","A673Cl1GgaussScore","A673Cl2GgaussScore","A673Cl1DecScore","A673Cl2DecScore","C22ThScore","E17ThScore","C22GgaussScore","E17GgaussScore","C22DecScore","E17DecScore","C22CtrlThScore","E17CtrlThScore","C22CtrlGgaussScore","E17CtrlGgaussScore","C22CtrlDecScore","E17CtrlDecScore"};
		for(int i=0;i<scoresToTest.length;i++){
			System.out.println("Processing \""+scoresToTest[i]+"\""+","+vt.fieldNumByName(scoresToTest[i]));
			Iterator it = genes.iterator();
			FileWriter fw = new FileWriter(path+scoresToTest[i]+".txt"); 
			while(it.hasNext()){
				String gs = (String)it.next();
				Vector<Integer> rows = vt.tableHashPrimary.get(gs);
				int imax = -1; double valmax = Float.NEGATIVE_INFINITY;
				for(int k=0;k<rows.size();k++){
					float Expr = Float.parseFloat(vt.stringTable[rows.get(k)][vt.fieldNumByName("Expr")]);
					if(Math.abs(Expr-1f)<1e-6){
						imax = rows.get(k);
						valmax = Float.parseFloat(vt.stringTable[rows.get(k)][vt.fieldNumByName(scoresToTest[i])]);
					}
				}
				if(imax>=0){
					if(scoresToTest[i].indexOf("ThScore")>=0)
						valmax = Math.log(10*valmax+Math.sqrt(10*valmax*10*valmax+1));
					else
						valmax = Math.log(valmax+Math.sqrt(valmax*valmax+1));
					fw.write(gs+"\t"+valmax+"\n");
				}
			}
			fw.close();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
