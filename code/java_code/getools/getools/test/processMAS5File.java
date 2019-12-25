package getools.test;

import java.io.*;
import java.text.*;
import java.util.*;

import getools.*;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.*;

public class processMAS5File {

	public static void main(String[] args) {
		try{

			VDataTable tab1 = VDatReadWrite.LoadFromVDatFile("c:/datas/anne/030608/X.dat");
			tab1 = TableUtils.filterByVariation(tab1, 5000, false);
			tab1 = tab1.transposeTable("probes");
			
			VDatReadWrite.saveToVDatFile(tab1, "c:/datas/anne/030608/x_5000t.dat");
			
			System.exit(0);
			
			/*VDataTable tab1 = VDatReadWrite.LoadFromVDatFile("c:/datas/bladdercancer/public/bcpn_mas5.dat");
			VDataTable tab2 = VDatReadWrite.LoadFromVDatFile("c:/datas/bladdercancer/public/bc2.dat");
			VDataTable tab = VSimpleProcedures.MergeTables(tab2, "Probe", tab1, "Probe", "_");
			VDatReadWrite.saveToVDatFile(tab, "c:/datas/bladdercancer/public/bc3.dat");*/
			
			/*VDataTable tab = VDatReadWrite.LoadFromVDatFile("c:/datas/bladdercancer/public/bc4.dat");
			for(int i=0;i<tab.rowCount;i++){
				for(int j=0;j<tab.colCount;j++)if(tab.fieldTypes[j]==tab.NUMERICAL){
					String val = tab.stringTable[i][j];
					float f = Float.parseFloat(val);
					f = (float)Math.log(f);
					tab.stringTable[i][j] = ""+f;
				}
			}
			
			tab = TableUtils.normalizeVDat(tab, true, false);
			
			VDatReadWrite.saveToVDatFile(tab, "c:/datas/bladdercancer/public/bc4c.dat");*/
			
			VDataTable tab = VDatReadWrite.LoadFromVDatFile("c:/datas/bladdercancer/public/bc4c.dat");
			Vector normals = new Vector();
			for(int i=0;i<tab.colCount;i++){
				String fn = tab.fieldNames[i];
				String ftyp = tab.fieldInfo[i][5];
				if(ftyp!=null)
				if(ftyp.equals("normal"))
					normals.add(fn);
			}
			for(int i=0;i<tab.rowCount;i++){
				float f[] = new float[tab.colCount-normals.size()];
				float n[] = new float[normals.size()];
				int kf = 0;
				int kn = 0;
				for(int j=0;j<tab.colCount;j++){
					String fn = tab.fieldNames[j];
					if(tab.fieldTypes[j]==tab.NUMERICAL){
					if(normals.indexOf(fn)<0)
					{
						f[kf] = Float.parseFloat(tab.stringTable[i][j]);
						kf = kf+1;
					}
					if(normals.indexOf(fn)>=0){
						n[kn] = Float.parseFloat(tab.stringTable[i][j]);
						kn = kn+1;
					}
					}
				}
				float aver = 0f;
				float aver2 = 0f;
				for(int k=0;k<n.length;k++) { aver+=n[k]; aver2+=n[k]*n[k]; } 
				aver/=n.length;
				aver2 = (float)Math.sqrt(aver2/n.length-aver*aver);
				for(int k=0;k<f.length;k++) 
					//f[k]=f[k]-aver;
					f[k]=(f[k]-aver)/aver2;
				kf = 0;
				kn = 0;
				for(int j=0;j<tab.colCount;j++){
					String fn = tab.fieldNames[j];
					if(tab.fieldTypes[j]==tab.NUMERICAL)
					if(normals.indexOf(fn)<0)
					{
						tab.stringTable[i][j] = ""+f[kf];
						kf = kf+1;
					}
					if(normals.indexOf(fn)>=0)
					{
						n[kn] = (n[kn]-aver)/aver2;
						//n[kn] = (n[kn]-aver);
						tab.stringTable[i][j] = ""+n[kn];
						kn = kn+1;
					}
				}
			}
			//tab = VSimpleProcedures.removeColumns(tab,normals);
			VDatReadWrite.saveToVDatFile(tab, "c:/datas/bladdercancer/public/bc6.dat");
			
			/*Vector samplesExclude = new Vector();
			samplesExclude.add("1533-10");
			samplesExclude.add("1533-13");
			samplesExclude.add("963-1");
			VDataTable tab = VDatReadWrite.LoadFromVDatFile("c:/datas/bladdercancer/public/bc3.dat");
			VDataTable tabr = new VDataTable(); 
			for(int i=0;i<tab.colCount;i++){
				String fn = tab.fieldNames[i];
				if(samplesExclude.indexOf(fn)<0){
					tabr.addNewColumn(fn, "", "", tab.fieldTypes[i], "_");
				}
			}
			if(tab.fieldInfo!=null)
				tabr.fieldInfo = new String[tabr.colCount][tab.fieldInfo[0].length];
			tabr.rowCount = tab.rowCount;
			tabr.stringTable = new String[tabr.rowCount][tabr.colCount];
			for(int i=0;i<tab.colCount;i++){
				String fn = tab.fieldNames[i];
				if(samplesExclude.indexOf(fn)<0){
					if(tab.fieldInfo!=null){
						tabr.fieldInfo[tabr.fieldNumByName(fn)] = tab.fieldInfo[i];
					}
					for(int j=0;j<tab.rowCount;j++)
						tabr.stringTable[j][tabr.fieldNumByName(fn)] = tab.stringTable[j][i];
				}
			}
			VDatReadWrite.saveToVDatFile(tabr, "c:/datas/bladdercancer/public/bc4.dat");*/
			
			System.exit(0);
			
			String prefix = "c:/datas/bladdercancer/public/bcpn_mas5";
			Vector excludeSamples = new Vector(); 
			//excludeSamples.add("1533-");
			
			VDataTable vmas5 = VDatReadWrite.LoadFromSimpleDatFile(prefix+".txt", true, "\t");
			
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/bladdercancer/public/bc2.txt", true, "\t");
			
			VDataTable res = new VDataTable();
			
			res.addNewColumn("Probe", "", "", res.STRING, "");
			for(int i=0;i<vmas5.fieldNames.length;i++){
				String fn = vmas5.fieldNames[i];
				//if(fn.equals("CompositeSequence name"))
				if(fn.startsWith("P"))if(fn.indexOf("CHPSignal")>=0){
					StringTokenizer st = new StringTokenizer(fn," ");
					String sn = st.nextToken().substring(1);
					String rep = st.nextToken();
					boolean found = false;
					for(int j=0;j<vt.colCount;j++)
						if(vt.fieldNames[j].equals(sn))
							found = true;
					//System.out.println(sn+"_"+rep+"\t"+found+"\t"+vmas5.stringTable[1][i]);
					res.addNewColumn(sn+"_"+rep, "", "", res.NUMERICAL, "");					
				}
			}
			res.fieldInfo = new String[res.colCount][6];
			res.rowCount = vmas5.rowCount-4; System.out.println(res.rowCount+" rows");
			res.stringTable = new String[res.rowCount][res.colCount];
			for(int i=0;i<vmas5.fieldNames.length;i++){
				String fn = vmas5.fieldNames[i];
				if(fn.startsWith("P"))if(fn.indexOf("CHPSignal")>=0){
					StringTokenizer st = new StringTokenizer(fn," ");
					String sn = st.nextToken().substring(1);
					String rep = st.nextToken();
					String fname = sn+"_"+rep;
					int k = res.fieldNumByName(fname);
					String stage = vmas5.stringTable[0][i];
					String state = vmas5.stringTable[1][i];					
					String sex = vmas5.stringTable[2][i];;
					String grade = vmas5.stringTable[3][i];

					res.fieldInfo[k][4] = sex;
					res.fieldInfo[k][3] = grade;
					res.fieldInfo[k][5] = state;
					if(state.equals("normal"))
						System.out.println(vmas5.fieldNames[i]);

					if(!stage.equals("")){
					StringTokenizer stst = new StringTokenizer(stage,", ");
						res.fieldInfo[k][0] = stst.nextToken();
						res.fieldInfo[k][1] = stst.nextToken();
						res.fieldInfo[k][2] = stst.nextToken();
					}else{
						res.fieldInfo[k][0] = "_";
						res.fieldInfo[k][1] = "_";
						res.fieldInfo[k][2] = "_";
					}

					if(res.fieldInfo[k][2].indexOf("Ta")>=0) res.fieldInfo[k][5] = "noninvasive";					
					if(res.fieldInfo[k][2].indexOf("T1")>=0) res.fieldInfo[k][5] = "noninvasive";					
					if(res.fieldInfo[k][2].indexOf("T2")>=0) res.fieldInfo[k][5] = "invasive";
					if(res.fieldInfo[k][2].indexOf("T3")>=0) res.fieldInfo[k][5] = "invasive";					
					if(res.fieldInfo[k][2].indexOf("T4")>=0) res.fieldInfo[k][5] = "invasive";					
					
					for(int j=4;j<vmas5.rowCount;j++){
						float f = Float.parseFloat(vmas5.stringTable[j][i]);
						//f = (float)Math.log(f);
						res.stringTable[j-4][k] = ""+f;
						res.stringTable[j-4][0] = vmas5.stringTable[j][1];						
					}
				}
			}
			
			VDataTable res1 = new VDataTable();
			res1.addNewColumn("Probe", "", "", res1.STRING, "_");
			for(int i=1;i<res.colCount;i++){
				String fn = res.fieldNames[i];
				if(res.fieldInfo[i][5].equals("normal"))
				if(fn.toLowerCase().endsWith("_rep1")){
					fn = fn.substring(0,fn.length()-5);
					res1.addNewColumn(fn, "", "", res1.NUMERICAL, "_");
				}
			}
			res1.rowCount = res.rowCount;
			res1.stringTable = new String[res1.rowCount][res1.colCount];
			res1.fieldInfo = new String[res1.colCount][6];
			System.out.println("Averaging duplicates");
			for(int j=0;j<res.rowCount;j++){
				res1.stringTable[j][0] = res.stringTable[j][0];
				for(int i=1;i<res.colCount;i++){
					String fn = res.fieldNames[i];
					if(res.fieldInfo[i][5].equals("normal"))
					if(fn.toLowerCase().endsWith("_rep1")){
						fn = fn.substring(0,fn.length()-5);
						String val1 = res.stringTable[j][res.fieldNumByName(fn+"_REP1")];
						String val2 = res.stringTable[j][res.fieldNumByName(fn+"_REP2")];
						float f = (Float.parseFloat(val1)+Float.parseFloat(val2))/2;
						res1.stringTable[j][res1.fieldNumByName(fn)] = ""+f;
						int k = res.fieldNumByName(fn+"_REP1");
						int k1 = res1.fieldNumByName(fn);
						res1.fieldInfo[k1] = res.fieldInfo[k];
					}
				}
			}
			
			VDatReadWrite.saveToVDatFile(res1, prefix+".dat");
			VDatReadWrite.saveToSimpleDatFile(res1, prefix+"_1.xls");			
			VDatReadWrite.saveToSimpleDatFile(vt, prefix+"_2.xls");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
