package getools.scripts;

import java.io.*;
import java.util.*;
import vdaoengine.data.io.*;
import vdaoengine.data.*;

public class MakeSurvivalAnalysis {

	Vector<HashSet<String>> groups = new Vector<HashSet<String>>();
	VDataTable table = null;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			String table = "C:/Datas/ModuleActivities/data/wang1/sample_scoring.txt";
			
			MakeSurvivalAnalysis ms = new MakeSurvivalAnalysis();
			ms.table = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(table, true, "\t");

			//System.out.println("\""+ms.table.fieldNames[0]+"\"\t\""+ms.table.fieldNames[1]+"\"");
			
			ms.separateIntoGroupsByScore(3);
			
			Vector<Boolean[]> patientsLeft = new Vector<Boolean[]>();
			float curves[][] = ms.makeSurvivalCurves(180, patientsLeft);
			
			System.out.print("TIME\t");
			for(int i=0;i<ms.groups.size();i++) System.out.print("GROUP"+(i+1)+"\t");
			for(int i=0;i<ms.groups.size();i++) System.out.print("LEFT"+(i+1)+"\t");
			System.out.println();
			for(int i=0;i<curves.length;i++){
				System.out.print(i+"\t");
				for(int j=0;j<ms.groups.size();j++) System.out.print(curves[i][j]+"\t");
				Boolean left[] = patientsLeft.get(i);
				for(int j=0;j<ms.groups.size();j++) 
				if(left[j])
						System.out.print(curves[i][j]+"\t");
					else
						System.out.print("\t");
				System.out.println();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void separateIntoGroupsByScore(int numberOfGroups){
		groups.clear();
		if(numberOfGroups==1){
			HashSet<String> group = new HashSet<String>();
			for(int i=0;i<table.rowCount;i++){
				String sample = table.stringTable[i][table.fieldNumByName("SAMPLE")];
				group.add(sample);
			}
			groups.add(group);
		}else if(numberOfGroups==2){
			HashSet<String> group_negative = new HashSet<String>();
			HashSet<String> group_positive = new HashSet<String>();
			for(int i=0;i<table.rowCount;i++){
				String sample = table.stringTable[i][table.fieldNumByName("SAMPLE")];
				Float score = Float.parseFloat(table.stringTable[i][table.fieldNumByName("SCORE")]);
				if(score<0)
					group_negative.add(sample);
				else
					group_positive.add(sample);
			}
			groups.add(group_negative);
			groups.add(group_positive);
		}else if(numberOfGroups==3){
			VStatistics vst = new VStatistics(1);
			for(int i=0;i<table.rowCount;i++){
				Float score = Float.parseFloat(table.stringTable[i][table.fieldNumByName("SCORE")]);
				float f[] = new float[1]; f[0] = score;
				vst.addNewPoint(f);
			}
			vst.calculate();
			HashSet<String> group_negative = new HashSet<String>();
			HashSet<String> group_zero = new HashSet<String>();
			HashSet<String> group_positive = new HashSet<String>();
			for(int i=0;i<table.rowCount;i++){
				String sample = table.stringTable[i][table.fieldNumByName("SAMPLE")];
				Float score = Float.parseFloat(table.stringTable[i][table.fieldNumByName("SCORE")]);
				if(score<vst.getMean(0)-vst.getStdDev(0))
					group_negative.add(sample);
				if(score>vst.getMean(0)+vst.getStdDev(0))
					group_positive.add(sample);
				if((score>vst.getMean(0)-vst.getStdDev(0))&&(score<vst.getMean(0)+vst.getStdDev(0)))
					group_zero.add(sample);
			}
			groups.add(group_negative);
			groups.add(group_zero);
			groups.add(group_positive);
		}
	}
	
	public float[][] makeSurvivalCurves(int maxnumber, Vector<Boolean[]> patientsLeft){
		float curves[][] = new float[maxnumber][groups.size()];
		patientsLeft.clear();
		for(int time=0;time<maxnumber;time++){
			for(int j=0;j<groups.size();j++)
				curves[time][j] = groups.get(j).size();
			Boolean left[] = new Boolean[groups.size()];
			for(int j=0;j<groups.size();j++)
				left[j] = false;
			patientsLeft.add(left);
		}
		for(int time=0;time<maxnumber;time++){
			for(int i=0;i<table.rowCount;i++){
				String sample = table.stringTable[i][table.fieldNumByName("SAMPLE")];
				boolean event = table.stringTable[i][table.fieldNumByName("EVENT")].equals("1");
				Float event_time = Float.parseFloat(table.stringTable[i][table.fieldNumByName("TIME")]);
				int ngroup = -1;
				for(int j=0;j<groups.size();j++)
					if(groups.get(j).contains(sample))
						ngroup = j;
				if(event)
					if(time>=event_time)
						curves[time][ngroup]--;

			}
		}

		for(int i=0;i<table.rowCount;i++){
			String sample = table.stringTable[i][table.fieldNumByName("SAMPLE")];
			boolean event = table.stringTable[i][table.fieldNumByName("EVENT")].equals("1");
			Float event_time = Float.parseFloat(table.stringTable[i][table.fieldNumByName("TIME")]);
			int ngroup = -1;
			for(int j=0;j<groups.size();j++)
				if(groups.get(j).contains(sample))
					ngroup = j;
			if(!event){
				Boolean left[] = patientsLeft.get(Math.round(event_time));
				left[ngroup] = true;
			}
		}
		
		for(int time=0;time<maxnumber;time++){
			for(int j=0;j<groups.size();j++)
				curves[time][j]/=groups.get(j).size();
		}		
		return curves;
	}

}
