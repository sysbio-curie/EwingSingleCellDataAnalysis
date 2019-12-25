package vdaoengine;

import java.io.*;
import java.util.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.elmap.*;

public class AtlasScript {
	
	
	private class Group{
		String name = "";
		Vector<String> members = new Vector<String>(); 
	}
	private class GroupList{
		Vector<Group> groups = new Vector<Group>();
		public void readFromFile(String fn){
			try{
				LineNumberReader lr = new LineNumberReader(new FileReader(fn));
				String s = null;
				while((s=lr.readLine())!=null){
					StringTokenizer st = new StringTokenizer(s,"\t");
					Group gr = new Group();
					gr.name = st.nextToken();
					while(st.hasMoreTokens()) 
						gr.members.add(st.nextToken());
					groups.add(gr);
				}
				lr.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			
			//String prefix = "c:/datas/atlas/timeseries/";
			//String file1 = "tsf1l_traj_quality_nogap_2005";
			//String file1 = "tsf1l_traj_democracy_nogap"; 
			//String file1 = "tsf1l_traj";
			//String prefix = "C:/Datas/Podkidisheva/ViDaExpert/Banki/tables/";
			//String prefix = "C:/Datas/Podkidisheva/ViDaExpert/Econom_integr/"; 
			//String file1 = "econom_integr";
			String prefix = "C:/Docs/WIKI/QualitiOfLife/data/";
			String file1 = "gapminder_ppp";

			//String prefix = "c:/datas/bladdercancer/";
			//String file1 = "p21_p53";

			
			VDataTable vt = VDatReadWrite.LoadFromVDatFile(prefix+file1+".dat");
			
			/*Vector<String> vm = new Vector<String>();
			vm.add("COMPETITION_PARLAMENT");
			vm.add("COMPETITION_PRESIDENCE");
			vm.add("ELECTION_INVOLVMENT");
			vm.add("COMPETITION_PERIOD");
			
			VDataTable vt1 = VSimpleProcedures.filterMissingValues(vt, 1e-3f,vm);
			vt1.fieldTypes[vt.fieldNumByName("YEAR")] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"_nogap.dat");*/			
			/*VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1); 
			
			Grid grid1 = ElmapAlgorithm.computeElasticGrid(vd,"elmap.ini",10);
			grid1.saveToFile(prefix+file1+".vem",file1);
			
			vd.calcStatistics();
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.calcBasis(1);
			
			double disp[] = pca.calcDispersions();
			double dispr[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
			float msepca = pca.calcMSE(vd);
			System.out.println("msepca="+msepca+"\ttotalDispersion="+vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion+"\tdisp[0]="+disp[0]+"\texp1="+dispr[0]);
			
			grid1.saveToFile(prefix+file1+".vem",file1);
			grid1.MakeNodesCopy();
			Vector taxons = grid1.calcTaxons(vd);
			double mse = grid1.calcMSE(vd, taxons);
			
			ElmapProjection elmapprojectiont = new ElmapProjection();
		    ElmapAlgorithm elmapt = new ElmapAlgorithm();
		    elmapt.grid = grid1;
		    elmapprojectiont.setElmap(elmapt);
		    elmapprojectiont.setDataSet(vd);
		    elmapprojectiont.elmap.grid.MakeNodesCopy();
		    elmapprojectiont.elmap.taxons = elmapprojectiont.elmap.grid.calcTaxons(vd);
		    float mseelmapt = elmapprojectiont.calculateMSEToProjection(vd);
			
			System.out.println("disp=\t"+disp[0]+"\tmse="+mse+"\tmsep="+mseelmapt);
			
			for(int i=0;i<vd.pointCount;i++)
				System.out.print(pca.projectionFunction(vd.getVector(i))[0]+"\t");
			System.out.println();
			
			for(int i=0;i<vd.pointCount;i++)
				System.out.print(elmapprojectiont.projectionFunction(vd.getVector(i))[0]+"\t");
			System.out.println();			
			
			System.exit(0);*/
			

			/*Vector<String> v = new Vector<String>();
			v.add("TERACTS_NUM_89");v.add("TERACTS_NUM_90");v.add("TERACTS_NUM_91");v.add("TERACTS_NUM_92");v.add("TERACTS_NUM_93");v.add("TERACTS_NUM_94");v.add("TERACTS_NUM_95");
			v.add("TERACTS_NUM_96");v.add("TERACTS_NUM_97");v.add("TERACTS_NUM_98");v.add("TERACTS_NUM_99");v.add("TERACTS_NUM_00");
			v.add("TERACTS_NUM_01");v.add("TERACTS_NUM_02");v.add("TERACTS_NUM_03");v.add("TERACTS_NUM_04");v.add("TERACTS_NUM_05");

			v.add("TERACTS_WOUNDED_89");v.add("TERACTS_WOUNDED_90");v.add("TERACTS_WOUNDED_91");v.add("TERACTS_WOUNDED_92");v.add("TERACTS_WOUNDED_93");v.add("TERACTS_WOUNDED_94");v.add("TERACTS_WOUNDED_95");
			v.add("TERACTS_WOUNDED_96");v.add("TERACTS_WOUNDED_97");v.add("TERACTS_WOUNDED_98");v.add("TERACTS_WOUNDED_99");v.add("TERACTS_WOUNDED_00");
			v.add("TERACTS_WOUNDED_01");v.add("TERACTS_WOUNDED_02");v.add("TERACTS_WOUNDED_03");v.add("TERACTS_WOUNDED_04");v.add("TERACTS_WOUNDED_05");

			v.add("TERACTS_KILLED_89");v.add("TERACTS_KILLED_90");v.add("TERACTS_KILLED_91");v.add("TERACTS_KILLED_92");v.add("TERACTS_KILLED_93");v.add("TERACTS_KILLED_94");v.add("TERACTS_KILLED_95");
			v.add("TERACTS_KILLED_96");v.add("TERACTS_KILLED_97");v.add("TERACTS_KILLED_98");v.add("TERACTS_KILLED_99");v.add("TERACTS_KILLED_00");
			v.add("TERACTS_KILLED_01");v.add("TERACTS_KILLED_02");v.add("TERACTS_KILLED_03");v.add("TERACTS_KILLED_04");v.add("TERACTS_KILLED_05");
			
			VDataTable vt1 = VSimpleProcedures.removeColumns(vt, v);
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"t.dat");*/
			
			// Recovery of empty columns
			/*Vector<String> v = new Vector<String>();
			v.add("EXTERNAL_DEBT_90");v.add("EXTERNAL_DEBT_91");v.add("EXTERNAL_DEBT_92");v.add("EXTERNAL_DEBT_93");v.add("EXTERNAL_DEBT_94");
			restoreValueByLinearTrend(vt,"EXTERNAL_DEBT_89",-1f,v,true);

			v.clear();
			v.add("ELECTRICITY_CONSUM_00");v.add("ELECTRICITY_CONSUM_01");v.add("ELECTRICITY_CONSUM_02");v.add("ELECTRICITY_CONSUM_03");v.add("ELECTRICITY_CONSUM_04");
			restoreValueByLinearTrend(vt,"ELECTRICITY_CONSUM_05",5f,v,true);
			
			v.clear();
			v.add("TUBERCULOSIS_INCID_90");v.add("TUBERCULOSIS_INCID_91");v.add("TUBERCULOSIS_INCID_92");v.add("TUBERCULOSIS_INCID_93");v.add("TUBERCULOSIS_INCID_94");
			restoreValueByLinearTrend(vt,"TUBERCULOSIS_INCID_89",-1f,v,false);
			
			VDatReadWrite.saveToVDatFile(vt, prefix+file1+"1.dat");*/
			
			/*Vector<String> v = new Vector<String>();
			//v.add("TERACTS_NUM");v.add("TERACTS_WOUNDED");v.add("TERACTS_KILLED");
			v.add("GDP");v.add("EXPORT");v.add("ARMY");v.add("MILITARY_EXPENSES");
			v.add("ELECTRICITY_CONSUM");
			v.add("POPULATION");v.add("POPULATION_URBAN");
			//v.add("EXTERNAL_AID_ABS");
			VDataTable vt1 = convertToLogarithms(vt,v);
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"l.dat");*/
			
			/*VDataTable vt1 = transformTableTrajectories(vt);
			VDataTable vtpca = makePCATableAndLines(vt1,prefix+file1+"_trajpca.vev");
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"_traj.dat");
			VDatReadWrite.saveToVDatFile(vtpca, prefix+file1+"_trajpca.dat");*/
			
			/*VDataTable vt1 = calcCorrelationYearPCA(vt);
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"_regr.dat");*/
			
			/*
			// Analysis of contributions and factor importance 
			vt.fieldTypes[vt.fieldNumByName("PC1")] = vt.STRING;
			vt.fieldTypes[vt.fieldNumByName("PC2")] = vt.STRING;
			vt.fieldTypes[vt.fieldNumByName("PC3")] = vt.STRING;
			vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.recalculateBasis(vd, 1);
			VLinearFunction function = new VLinearFunction();
			function.setDimension(vd.coordCount);
			function.set(pca.getBasis().a0[0], pca.getBasis().basis[0]);
			VDataTable vt1 = contributionToLinearFunction(vt, vd, function);
			analysisOfFactorImportance(vt1);
			vt1.fieldTypes[vt.fieldNumByName("PC1")] = vt.NUMERICAL;
			vt1.fieldTypes[vt.fieldNumByName("PC1check")] = vt.NUMERICAL;
			vt1.fieldTypes[vt.fieldNumByName("PC3")] = vt.NUMERICAL;
			vt1.fieldTypes[vt.fieldNumByName("YEAR")] = vt.NUMERICAL;			
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"_contributionPC1.dat");*/
			
			/*VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
			VDataTable vt1 = dynamismIndex(vt,vd);
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"_dist.dat");*/
			
			/*VDataTable state = VSimpleProcedures.extractClass(vt, 0, "StateOrganization");
			VDatReadWrite.saveToVDatFile(state, prefix+file1+"_state.dat");
			VDataTable threats = VSimpleProcedures.extractClass(vt, 0, "Threats");
			VDatReadWrite.saveToVDatFile(threats, prefix+file1+"_threats.dat");
			VDataTable quality = VSimpleProcedures.extractClass(vt, 0, "QualityOfLife");
			VDatReadWrite.saveToVDatFile(quality, prefix+file1+"_quality.dat");
			VDataTable democracy = VSimpleProcedures.extractClass(vt, 0, "Democracy");
			VDatReadWrite.saveToVDatFile(democracy, prefix+file1+"_democracy.dat");
			VDataTable influence = VSimpleProcedures.extractClass(vt, 0, "Influence");
			VDatReadWrite.saveToVDatFile(influence, prefix+file1+"_influence.dat");*/

			
			// Non-linear index construction
			VDataTable vtng = VSimpleProcedures.filterMissingValues(vt, 1e-3f);
			//vtng.fieldTypes[vtng.fieldNumByName("YEAR")] = vt.STRING;
			VDataSet vdng = VSimpleProcedures.SimplyPreparedDataset(vtng, -1);
			vdng.calcStatistics();
			Grid gr = constructPrincipalCurve(vtng,false);

			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			vd.calcStatistics();
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.calcBasis(1);
			double disp[] = pca.calcDispersions();
			double dispr[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
			float msepca = pca.calcMSE(vd);
			System.out.println("msepca="+msepca+"\ttotalDispersion="+vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion+"\tdisp[0]="+disp[0]+"\texp1="+dispr[0]);
			
			//gr.saveToFile(prefix+file1+".vem",file1+"_nogap");
			gr.saveToFile(prefix+file1+".vem",file1+"");
			gr.MakeNodesCopy();
			Vector taxons = gr.calcTaxons(vdng);
			double mse = gr.calcMSE(vdng, taxons);
			
			ElmapProjection elmapprojectiont = new ElmapProjection();
		    ElmapAlgorithm elmapt = new ElmapAlgorithm();
		    elmapt.grid = gr;
		    elmapprojectiont.setElmap(elmapt);
		    elmapprojectiont.setDataSet(vd);
		    elmapprojectiont.elmap.grid.MakeNodesCopy();
		    elmapprojectiont.elmap.taxons = elmapprojectiont.elmap.grid.calcTaxons(vd);
		    float mseelmapt = elmapprojectiont.calculateMSEToProjection(vd);
		    VDataSet vdp = elmapprojectiont.getProjectedDataset();
		    VDatReadWrite.saveToVDatFile(vdp, prefix+file1+"_projnl.dat");
		    
		    vdp = pca.getProjectedDataset();
		    VDatReadWrite.saveToVDatFile(vdp, prefix+file1+"_projl.dat");
			
			System.out.println("disp=\t"+disp[0]+"\tmse="+mse+"\tmsep="+mseelmapt);
			
			//Principal curves for countries
			/*prefix+="tables/";
			file1 = "Iran";
			VDataTable vtc = VDatReadWrite.LoadFromVDatFile(prefix+file1+".dat");
			vtc.fieldTypes[vt.fieldNumByName("TERACTS_NUM")] = vt.STRING;
			vtc.fieldTypes[vt.fieldNumByName("TERACTS_WOUNDED")] = vt.STRING;
			vtc.fieldTypes[vt.fieldNumByName("TERACTS_KILLED")] = vt.STRING;
			Grid grid = constructPrincipalCurve(vtc,true);
			grid.saveToFile(prefix+file1+".vem",file1);
			
			analyzeSmoothedTrajectory(vtc,grid,prefix+file1,0.1f);

			
			/*for(int i=0;i<vd.pointCount;i++){
				float pcap[] = pca.projectionFunction(vd.getVector(i));
				float grp[] = gr.projectPoint(vd.getVector(i), gr.PROJECTION_CLOSEST_POINT);
				float pcax[] = pca.projectFromInToOut(pcap);
				float grx[] = elmapprojectiont.projectFromInToOut(grp);
				System.out.println(pcax[0]+"\t"+grx[0]);
		    }*/			
			
			/*VDataTable vt1 = checkPC1AsIndex(vt,gr);
			//VDataTable vt1 = VSimpleProcedures.filterMissingValues(vt, 1e-3f);
			vt1.fieldTypes[vt.fieldNumByName("YEAR")] = vt.NUMERICAL;
			VDatReadWrite.saveToVDatFile(vt1, prefix+file1+"_nogap.dat");
			VDatReadWrite.saveToVDatFile(vt, prefix+file1+"_pc1.dat");
			
			vt.fieldTypes[vt.fieldNumByName("GAPPED")] = vt.STRING;
			VDataTable vtreg = calcCorrelationYearPCA(vt,"PNC");
			VDatReadWrite.saveToVDatFile(vtreg, prefix+file1+"_pc1_regr.dat");*/
			
			
			//vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
			//makeLines(vt,prefix+file1+".vev");
			
			
			//separateIntoSubtables(vt,prefix+"/tables/");
			
			//analysisOfPCADynamics(prefix+"/tables/");
			
			//Vector<String> rec = calcCorrelationTable(vt,prefix+file1+".corr",0);
			
			//calcCorrelationsByYear(prefix+"/tables/",rec,0.4f);
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void restoreValueByLinearTrend(VDataTable vt, String fieldToRecover, float coord, Vector<String> trendColumns, boolean round){
		float x[] = new float[trendColumns.size()];
		for(int j=0;j<trendColumns.size();j++) x[j] = j;
		for(int i=0;i<vt.rowCount;i++){
			float f[] = new float[trendColumns.size()];
			int nonEmpties = 0;
			for(int j=0;j<trendColumns.size();j++){
				String val = vt.stringTable[i][vt.fieldNumByName(trendColumns.get(j))];
				if(val.equals("@"))
					f[j] = Float.NaN;
				else{
					f[j] = Float.parseFloat(val);
					nonEmpties++;
				}
			}
			float recov = Float.NaN;
			if(nonEmpties>=4){
			float xnew[] = new float[nonEmpties];
			float vals[] = new float[nonEmpties];
			int kk=0;
			for(int k=0;k<trendColumns.size();k++){
				if(!Float.isNaN(f[k])){
					xnew[kk]=x[k];
					vals[kk]=f[k];
					kk++;
				}
			}
				float coeff[] = Algorithms.fitPolynome(xnew, vals, 1);
				recov = Algorithms.evalPolynome(coord, coeff);
			}
			int intrecov = Math.round(recov);
			String s = "";
			if(round)
				s = ""+intrecov;
			else
				s = ""+recov;
			if(Float.isNaN(recov)) s = "@";
			vt.stringTable[i][vt.fieldNumByName(fieldToRecover)] = s;
		}
	}
	
	public static void separateIntoSubtables(VDataTable vt, String dir){
		// by countries
		Vector country_list = new Vector();
		HashMap country_names = new HashMap();
		for(int i=0;i<vt.rowCount;i++)
			if(country_list.indexOf(vt.stringTable[i][vt.fieldNumByName("ABBREV")])<0){
				String id = vt.stringTable[i][vt.fieldNumByName("ABBREV")];
				//String name = vt.stringTable[i][vt.fieldNumByName("COUNTR_A")];
				String name = vt.stringTable[i][vt.fieldNumByName("ABBREV")];
				//name = name.substring(0, name.length()-3);
				country_names.put(id,name);
				country_list.add(id);
			}
		Vector ids = new Vector();
		for(int i=0;i<country_list.size();i++){
			String id = (String)country_list.get(i);
			String cname = (String)country_names.get(id);
			ids.clear(); ids.add(id);
			VDataTable vts = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(vt, ids, "ABBREV");
			vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts, dir+cname+".dat");
		}
		// by years
		/*for(int year=1989;year<=2005;year++){
			String syear = ""+year;
			ids.clear(); ids.add(syear);
			VDataTable vts = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(vt, ids, "YEAR");
			vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts, dir+syear+".dat");
		}*/
		Vector<String> years = new Vector<String>();
		years.add("2005_1");years.add("2005_2");years.add("2005_3");years.add("2005_4");
		years.add("2006_1");years.add("2006_2");years.add("2006_3");years.add("2006_4");
		years.add("2007_1");years.add("2007_2");years.add("2007_3");years.add("2007_4");
		years.add("2008_1");years.add("2008_2");years.add("2008_3");years.add("2008_4");
		years.add("2009_1");years.add("2009_2");years.add("2009_3");
		for(int k=0;k<years.size();k++){
		String syear = years.get(k);
		ids.clear(); ids.add(syear);
		VDataTable vts = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(vt, ids, "YEAR");
		vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vts, dir+syear+".dat");
		}
		
	}
	
	public static VDataTable convertToLogarithms(VDataTable vt, Vector<String> v){
		DecimalFormat df = new DecimalFormat("##.##"); 
		VDataTable vtr = new VDataTable();
		vtr.copyHeader(vt);
		vtr.rowCount = vt.rowCount;
		vtr.colCount = vt.colCount;
		vtr.stringTable = new String[vt.rowCount][vt.colCount];
		for(int i=0;i<vtr.colCount;i++)for(int j=0;j<vtr.rowCount;j++)vtr.stringTable[j][i]=vt.stringTable[j][i];
		Vector<String> years = new Vector<String>();
		years.add("89");years.add("90");years.add("91");years.add("92");years.add("93");
		years.add("94");years.add("95");years.add("96");years.add("97");years.add("98");
		years.add("99");years.add("00");years.add("01");years.add("02");years.add("03");
		years.add("04");years.add("05");
		for(int i=0;i<v.size();i++){
			String name = v.get(i);
			boolean containsZero = false;
			for(int j=0;j<years.size();j++){
				String fn = name+"_"+years.get(j);
				for(int k=0;k<vt.rowCount;k++){
					if(vt.fieldNumByName(fn)==-1)
						System.out.println("Field "+fn+" is not found!");
					String val = vtr.stringTable[k][vt.fieldNumByName(fn)];
					if(!val.equals("@")){
						double f = Float.parseFloat(val);
						if(f==0){
							containsZero = true;
							f = 1;
						}
						f = Math.log10(f);
						vtr.stringTable[k][vt.fieldNumByName(fn)] = df.format(f);
					}
				}
			}
			if(containsZero)
				System.out.println(name+" contains zero");
		}
		return vtr;
	}
	
	public static VDataTable transformTableTrajectories(VDataTable vt){
		VDataTable vtr = new VDataTable();
		Vector<String> fieldList = new Vector<String>();
		Vector<String> years = new Vector<String>();
		years.add("89");years.add("90");years.add("91");years.add("92");years.add("93");
		years.add("94");years.add("95");years.add("96");years.add("97");years.add("98");
		years.add("99");years.add("00");years.add("01");years.add("02");years.add("03");
		years.add("04");years.add("05");
		
		for(int i=0;i<vt.colCount;i++){
			if(vt.fieldTypes[i]==vt.STRING){
				vtr.addNewColumn(vt.fieldNames[i], vt.fieldClasses[i], vt.fieldDescriptions[i], vt.fieldTypes[i], "_");
			}
		}
		vtr.addNewColumn("YEAR", "", "", vt.NUMERICAL, "_");
		for(int i=0;i<vt.colCount;i++){
			String fn = vt.fieldNames[i];
			for(int j=0;j<years.size();j++){
				if(fn.indexOf("_"+years.get(j))>=0){
					fn = fn.substring(0, fn.length()-3);
					if(fieldList.indexOf(fn)<0)
						fieldList.add(fn);
				}
			}
		}
		for(int i=0;i<fieldList.size();i++)
			vtr.addNewColumn(fieldList.get(i), "", "", vt.NUMERICAL, "_");
		
		vtr.rowCount = vt.rowCount*years.size();
		vtr.stringTable = new String[vtr.rowCount][vtr.colCount];
		
		int k=0;
		for(int i=0;i<vt.rowCount;i++){
			for(int j=0;j<years.size();j++){
				for(int ii=0;ii<vt.colCount;ii++){
					if(vt.fieldTypes[ii]==vt.STRING){
						vtr.stringTable[k][vtr.fieldNumByName(vt.fieldNames[ii])] = vt.stringTable[i][ii];
					}
				}
				vtr.stringTable[k][vtr.fieldNumByName("COUNTR_A")] = vt.stringTable[i][vt.fieldNumByName("COUNTR_A")]+"_"+years.get(j);
				//vtr.stringTable[k][vtr.fieldNumByName("ABBREV")] = vt.stringTable[i][vt.fieldNumByName("ABBREV")]+"_"+years.get(j);
				if(years.get(j).startsWith("8")||years.get(j).startsWith("9"))
					vtr.stringTable[k][vtr.fieldNumByName("YEAR")] = "19"+years.get(j);
				else
					vtr.stringTable[k][vtr.fieldNumByName("YEAR")] = "20"+years.get(j);
				
				for(int ii=0;ii<fieldList.size();ii++){
					String fname = fieldList.get(ii)+"_"+years.get(j);
					if(vt.fieldNumByName(fname)==-1)
						System.out.println(fname+" NOT FOUND");
					vtr.stringTable[k][vtr.fieldNumByName(fieldList.get(ii))] = vt.stringTable[i][vt.fieldNumByName(fname)];
				}
				
				k++;
			}
		}
		
		return vtr;
	}
	
	public static VDataTable makePCATableAndLines(VDataTable vt, String fn){
		if(vt.fieldNumByName("YEAR")<0)
			vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
		VDataTable vtpca = TableUtils.PCAtable(vt, true, true);
		String memab = vtpca.stringTable[0][vtpca.fieldNumByName("ABBREV")];
		try{
		FileWriter fw = new FileWriter(fn); 
		for(int i=1;i<vtpca.rowCount;i++){
			String ab = vtpca.stringTable[i][vtpca.fieldNumByName("ABBREV")];
			if(ab.equals(memab)){
				fw.write(vtpca.stringTable[i-1][vtpca.fieldNumByName("PC1")]+" "+vtpca.stringTable[i-1][vtpca.fieldNumByName("PC2")]+" "+vtpca.stringTable[i-1][vtpca.fieldNumByName("PC3")]+"\r\n");
				fw.write(vtpca.stringTable[i][vtpca.fieldNumByName("PC1")]+" "+vtpca.stringTable[i][vtpca.fieldNumByName("PC2")]+" "+vtpca.stringTable[i][vtpca.fieldNumByName("PC3")]+"\r\n");
			}else{
				
			}
			memab = ab;
		}
		fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return vtpca;
	}
	
	public static void makeLines(VDataTable vt, String fn){
		if(vt.fieldNumByName("YEAR")<0)
			vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
		String memab = vt.stringTable[0][vt.fieldNumByName("ABBREV")];
		try{
		FileWriter fw = new FileWriter(fn); 
		for(int i=1;i<vt.rowCount;i++){
			String ab = vt.stringTable[i][vt.fieldNumByName("ABBREV")];
			if(ab.equals(memab)){
				for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL)
					fw.write(vt.stringTable[i-1][j]+" ");
				fw.write("\n");
				for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL)
					fw.write(vt.stringTable[i][j]+" ");
				fw.write("\n");
			}else{
				
			}
			memab = ab;
		}
		fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void analysisOfPCADynamics(String fn){
		Vector<PCAMethod> pcas = new Vector<PCAMethod>();
		VDataTable vtsample = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(fn+"1989.dat");
		int numOfDim = 0;
		for(int year=1989;year<=2005;year++){
			String filename = fn+year+".dat";
			VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(filename);
			vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			numOfDim = vd.coordCount;
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.calcBasis(pca.getDataSet().getCoordCount());
			pcas.add(pca);
		}
		
		System.out.print("\t");
		for(int year=1989;year<=2005;year++){
			System.out.print(""+year+"\t");
		}
		System.out.println();
		for(int i=0;i<numOfDim;i++){
			System.out.print(vtsample.fieldNames[i+5]+"\t");
			for(int year=1989;year<=2005;year++){
				PCAMethod pca = pcas.get(year-1989);
				System.out.print(""+pca.getBasis().basis[0][i]+"\t");
			}
			System.out.println();
		}
		for(int year=1989;year<=2005;year++){
			PCAMethod pca = pcas.get(year-1989);
			double disp[] = pca.calcDispersions();
			float totalDisp = 0;
			for(int i=0;i<disp.length;i++)
				totalDisp+=disp[i];
			System.out.println(""+year+"\t"+disp[0]+"\t"+disp[1]+"\t"+disp[2]+"\t"+totalDisp+"\t"+disp[0]/totalDisp+"\t"+disp[0]/disp[1]);
		}
		
		
	}
	
	public static Vector<String> calcCorrelationTable(VDataTable vt, String fileName, double pvalue){ // correlationType:0-Pearson,1-Spearman
		if(vt.fieldNumByName("YEAR")!=-1)
			vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		Vector<String> records = new Vector<String>();
		try{
			FileWriter fw = new FileWriter(fileName);
			fw.write("FIELD1\tFIEILD2\tPEARSON\tSPEARMAN\tPEARSON_ROBUST\tSPEARMAN_ROBUST\n");
			Vector<Float> maxvs = new Vector<Float>();
		for(int i=0;i<vd.coordCount;i++)for(int j=i+1;j<vd.coordCount;j++){
			//System.out.println(""+i+","+j+"/"+vd.coordCount);
			Vector<Float> Xi = new Vector<Float>();
			Vector<Float> Xj= new Vector<Float>();
			for(int k=0;k<vd.pointCount;k++){
				if(!Float.isNaN(vd.massif[k][i]))if(!Float.isNaN(vd.massif[k][j])){
					Xi.add(new Float(vd.massif[k][i]));
					Xj.add(new Float(vd.massif[k][j]));
				}
			}
			if(Xi.size()>3){
				float xi[] = new float[Xi.size()];
				float xj[] = new float[Xj.size()];
				for(int k=0;k<xi.length;k++){
					xi[k] = Xi.get(k);
					xj[k] = Xj.get(k);
				}
				float corrPearson = VSimpleFunctions.calcCorrelationCoeff(xi, xj);
				float corrSpearman = VSimpleFunctions.calcSpearmanCorrelationCoeff(xi, xj);
				float corrPearsonRobust = 0;// VSimpleFunctions.calcCorrelationCoeffLeaveOneOut(xi, xj, 1.96f, 0);
				float corrSpearmanRobust = 0; // VSimpleFunctions.calcCorrelationCoeffLeaveOneOut(xi, xj, 1.96f, 1);
				double p1 = VSimpleFunctions.calcCorrelationPValue(corrPearson, xi.length);
				double p2 = VSimpleFunctions.calcCorrelationPValue(corrSpearman, xi.length);
				double p3 = VSimpleFunctions.calcCorrelationPValue(corrPearsonRobust, xi.length);
				double p4 = VSimpleFunctions.calcCorrelationPValue(corrSpearmanRobust, xi.length);
				//if(Math.min(Math.min(p1,p2),Math.min(p3,p4))<=pvalue){
				float max = Math.abs(corrPearson);
				if(!Float.isNaN(corrSpearman))if(Math.abs(corrSpearman)>max) max = Math.abs(corrSpearman);
				if(!Float.isNaN(corrPearsonRobust))if(Math.abs(corrPearsonRobust)>max) max = Math.abs(corrPearsonRobust);
				if(!Float.isNaN(corrSpearmanRobust))if(Math.abs(corrSpearmanRobust)>max) max = Math.abs(corrSpearmanRobust);
				//System.out.println(max);
				if(max>=pvalue){
					String fni = vt.fieldNames[vd.selector.selectedColumns[i]];
					String fnj = vt.fieldNames[vd.selector.selectedColumns[j]];
					//fw.write(fni+"\t"+fnj+"\t"+corrPearson+"\t"+corrSpearman+"\t"+corrPearsonRobust+"\t"+corrSpearmanRobust+"\t"+p1+"\t"+p2+"\t"+p3+"\t"+p4+"\n");
					String s = fni+"\t"+fnj+"\t"+corrPearson+"\t"+corrSpearman+"\t"+corrPearsonRobust+"\t"+corrSpearmanRobust+"\n";
					records.add(s);
					maxvs.add(max);
					//fw.write();
					//fw.flush();
				}
			}
		}
		
		float maxvsf[] = new float[maxvs.size()];
		for(int k=0;k<maxvsf.length;k++)
			maxvsf[k]=maxvs.get(k);
		
		int inds[] = Algorithms.SortMass(maxvsf);
		for(int k=0;k<inds.length;k++){
			fw.write(records.get(inds[inds.length-k-1]));
		}
		
		fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return records;
	}

	public static void calcCorrelationsByYear(String dir, Vector<String> globalRecords, float corrval){
		DecimalFormat df = new DecimalFormat("#.##");
		Vector<String> glRec = new Vector<String>();
		Vector<Float> glRecVal = new Vector<Float>();
		fillRecords(globalRecords,glRec,glRecVal);
		
		VDataTable vtDynamics = new VDataTable();
		vtDynamics.rowCount = glRec.size();
		vtDynamics.addNewColumn("FIELD_PAIR", "", "", VDataTable.STRING, "_");
		for(int year=1989;year<=2005;year++)
			vtDynamics.addNewColumn("Y"+year, "", "", VDataTable.NUMERICAL, "0");
		vtDynamics.addNewColumn("AMPLITUDE", "", "", VDataTable.NUMERICAL, "0");
		vtDynamics.addNewColumn("GLOBAL", "", "", VDataTable.NUMERICAL, "0");
		vtDynamics.stringTable = new String[vtDynamics.rowCount][vtDynamics.colCount];
		for(int k=0;k<glRec.size();k++)
			vtDynamics.stringTable[k][vtDynamics.fieldNumByName("FIELD_PAIR")] = glRec.get(k);
		
		System.out.println("\n=====  Global corr.adaptometry ========");
		for(int year=1989;year<=2005;year++){
			String filename = dir+year+".dat";
			VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(filename);
			Vector<String> records = calcCorrelationTable(vt,dir+year+".corr",corrval);
			Vector<String> recs = new Vector<String>();
			Vector<Float> recVals = new Vector<Float>();
			fillRecords(records,recs,recVals);
			float avCorr = 0f;
			for(int k=0;k<recVals.size();k++)
				avCorr+=Math.abs(recVals.get(k));
			avCorr/=recVals.size();
			System.out.println(""+year+"\t"+avCorr);
			
			for(int k=0;k<recs.size();k++){
				int ind = glRec.indexOf(recs.get(k));
				//float f = recVals.get(k)-glRecVal.get(k);
				float f = recVals.get(k);
				vtDynamics.stringTable[ind][vtDynamics.fieldNumByName("Y"+year)] = df.format(f);
			}
		}
		
		for(int k=0;k<glRec.size();k++){
			vtDynamics.stringTable[k][vtDynamics.fieldNumByName("GLOBAL")] = df.format(glRecVal.get(k));
			float minv = Float.MAX_VALUE;
			float maxv = -Float.MAX_VALUE;
			if(glRec.get(k).equals("EXTERNAL_AID@EXTERNAL_AID_ABS")){
				System.out.println();
			}
			for(int year=1989;year<=2005;year++){
				String val = vtDynamics.stringTable[k][vtDynamics.fieldNumByName("Y"+year)];
				if((val==null)||(val.equals(""))){
					vtDynamics.stringTable[k][vtDynamics.fieldNumByName("Y"+year)] = "0";
					val = "0";
				}
				float f = Float.parseFloat(val);
				if(f<minv) minv = f;
				if(f>maxv) maxv = f;
			}
			if((Math.abs(maxv-minv))<10)
				vtDynamics.stringTable[k][vtDynamics.fieldNumByName("AMPLITUDE")] = df.format(maxv-minv);
			else
				vtDynamics.stringTable[k][vtDynamics.fieldNumByName("AMPLITUDE")] = ""+0;
		}
		
		vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(vtDynamics, dir+"corrDynamics.dat");
	}
	
	public static void fillRecords(Vector<String> records, Vector<String> glRec, Vector<Float> glRecVal){
		for(int i=0;i<records.size();i++){
			String s = records.get(i);
			StringTokenizer st = new StringTokenizer(s,"\t");
			String fni = st.nextToken();
			String fnj = st.nextToken();
			glRec.add(fni+"@"+fnj);
			float valmax = 0;
			String val = st.nextToken();
			if(!val.equals("NaN")) if(Math.abs(Float.parseFloat(val))>Math.abs(valmax)) valmax = Float.parseFloat(val); 
			val = st.nextToken();
			if(!val.equals("NaN")) if(Math.abs(Float.parseFloat(val))>Math.abs(valmax)) valmax = Float.parseFloat(val);
			val = st.nextToken();
			if(!val.equals("NaN")) if(Math.abs(Float.parseFloat(val))>Math.abs(valmax)) valmax = Float.parseFloat(val);
			val = st.nextToken();
			if(!val.equals("NaN")) if(Math.abs(Float.parseFloat(val))>Math.abs(valmax)) valmax = Float.parseFloat(val);
			glRecVal.add(valmax);
		}
	}
	
	public static VDataTable calcCorrelationYearPCA(VDataTable vt, String fieldPrefix){
		
		DecimalFormat df = new DecimalFormat("#.####");
		
		VDataTable res = new VDataTable();
		//res.copyHeader(vt);
		
		int numberOfPCAFields = 0;
		
		res.rowCount = 0;
		
		for(int i=0;i<vt.colCount;i++){
			if(vt.fieldTypes[i]==vt.STRING){
				res.addNewColumn(vt.fieldNames[i], "", "", vt.STRING, "_");
			}
			if(vt.fieldNames[i].startsWith(fieldPrefix)){
				res.addNewColumn(vt.fieldNames[i]+"a", "", "", vt.NUMERICAL, "0");
				res.addNewColumn(vt.fieldNames[i]+"b", "", "", vt.NUMERICAL, "0");				
				numberOfPCAFields++;
			}
		}
		
		for(int i=0;i<numberOfPCAFields;i++){
		for(int year=1989;year<=2005;year++){
			res.addNewColumn(fieldPrefix+(i+1)+"_"+year, "", "", vt.NUMERICAL, "0");
		}
		}
		
		Vector<String> states = new Vector<String>();
		for(int i=0;i<vt.rowCount;i++){
			String state = vt.stringTable[i][vt.fieldNumByName("COUNTR_A")];
			state = state.substring(0, state.length()-3);
			if(states.indexOf(state)<0)
				states.add(state);
		}
		res.rowCount = states.size();
		res.stringTable = new String[res.rowCount][res.colCount];
		
		for(int i=0;i<states.size();i++){
			String state = states.get(i);
			System.out.println(state);
			float years[] = new float[17];
			float pcacorrs[][] = new float[numberOfPCAFields][17];
			int k=0;
			for(int j=0;j<vt.rowCount;j++){
				String state_a = vt.stringTable[j][vt.fieldNumByName("COUNTR_A")];
				String year = vt.stringTable[j][vt.fieldNumByName("YEAR")];
				String suffix = year.substring(2, 4);
				if(state_a.equals(state+"_"+suffix)){
					int y = Integer.parseInt(vt.stringTable[j][vt.fieldNumByName("YEAR")]);
					years[k] = y-1998;
					for(int pcan=1;pcan<=numberOfPCAFields;pcan++){
						pcacorrs[pcan-1][k] = Float.parseFloat(vt.stringTable[j][vt.fieldNumByName(fieldPrefix+pcan)]);
					}
					k++;
				for(int s=0;s<vt.colCount;s++)
					if(vt.fieldTypes[s]==vt.STRING)
						res.stringTable[i][res.fieldNumByName(vt.fieldNames[s])] = vt.stringTable[j][s];
				for(int pcan=1;pcan<=numberOfPCAFields;pcan++)
					res.stringTable[i][res.fieldNumByName(fieldPrefix+pcan+"_"+year)] = vt.stringTable[j][vt.fieldNumByName(fieldPrefix+pcan)];
				}
			}
			res.stringTable[i][res.fieldNumByName("COUNTR_A")] = state;
			for(int pcan=1;pcan<=numberOfPCAFields;pcan++){
				//float corrVal = VSimpleFunctions.calcCorrelationCoeff(years, pcacorrs[pcan-1]);
				float ab[] = VSimpleFunctions.calcLinearRegression(years, pcacorrs[pcan-1]);
				//res.stringTable[i][res.fieldNumByName("PC"+pcan+"YearCorr")] = ""+df.format(corrVal);
				res.stringTable[i][res.fieldNumByName(fieldPrefix+pcan+"a")] = ""+df.format(ab[1]);
				res.stringTable[i][res.fieldNumByName(fieldPrefix+pcan+"b")] = ""+df.format(ab[0]);
			}
		}
		return res;
	}

	public static VDataTable dynamismIndex(VDataTable vt, VDataSet vd){
		
		DecimalFormat df = new DecimalFormat("#.####");
		
		VDataTable res = new VDataTable();
		int numberOfNumericFields = 0;

		for(int i=0;i<vt.colCount;i++)
			if(vt.fieldTypes[i]==vt.STRING)
				res.addNewColumn(vt.fieldNames[i], vt.fieldClasses[i], vt.fieldDescriptions[i], vt.fieldTypes[i], "_");
			else
				numberOfNumericFields++;
		res.addNewColumn("DIST8905", "", "", vt.NUMERICAL, "0");
		res.addNewColumn("DISTN", "", "", vt.NUMERICAL, "0");
		res.addNewColumn("DISTPC3", "", "", vt.NUMERICAL, "0");
		res.addNewColumn("DISTPC10", "", "", vt.NUMERICAL, "0");
		
		Vector<String> states = new Vector<String>();
		for(int i=0;i<vt.rowCount;i++){
			String state = vt.stringTable[i][vt.fieldNumByName("COUNTR_A")];
			state = state.substring(0, state.length()-3);
			if(states.indexOf(state)<0)
				states.add(state);
		}
		res.rowCount = states.size();
		res.stringTable = new String[res.rowCount][res.colCount];
		
		PCAMethod pca3 = new PCAMethod();
		pca3.setDataSet(vd); pca3.calcBasis(3);
		PCAMethod pca10 = new PCAMethod();
		pca10.setDataSet(vd); pca10.calcBasis(10);
		
		for(int i=0;i<states.size();i++){
			String state = states.get(i);
			System.out.println(state);
			float trajectory[][] = new float[17][numberOfNumericFields];
			for(int j=0;j<vt.rowCount;j++){
				String state_a = vt.stringTable[j][vt.fieldNumByName("COUNTR_A")];
				String year = vt.stringTable[j][vt.fieldNumByName("YEAR")];
				String suffix = year.substring(2, 4);
				if(state_a.equals(state+"_"+suffix)){
					int y = Integer.parseInt(vt.stringTable[j][vt.fieldNumByName("YEAR")]);
					trajectory[y-1989] = vd.getVector(j);
					for(int s=0;s<vt.colCount;s++)
						if(vt.fieldTypes[s]==vt.STRING)
							res.stringTable[i][res.fieldNumByName(vt.fieldNames[s])] = vt.stringTable[j][s];
				}
			}
			// Now analysis of the trajectory
			float distn = calcTrajectoryLength(trajectory);
			
			float trajectory3[][] = new float[trajectory.length][trajectory[0].length];
			float trajectory10[][] = new float[trajectory.length][trajectory[0].length];
			
			for(int j=0;j<trajectory.length;j++)
				trajectory3[j] = pca3.projectionFunction(trajectory[j]);
			for(int j=0;j<trajectory.length;j++)
				trajectory10[j] = pca10.projectionFunction(trajectory[j]);
			
			float dist3 = calcTrajectoryLength(trajectory3);
			float dist10 = calcTrajectoryLength(trajectory10);
			float dist8905 = (float)Math.sqrt(VVectorCalc.SquareEuclDistanceGap(trajectory[0], trajectory[trajectory.length-1]));
			
			res.stringTable[i][res.fieldNumByName("DIST8905")] = df.format(dist8905);
			res.stringTable[i][res.fieldNumByName("DISTPC3")] = df.format(dist3);
			res.stringTable[i][res.fieldNumByName("DISTPC10")] = df.format(dist10);
			res.stringTable[i][res.fieldNumByName("DISTN")] = df.format(distn);
		}
		
		return res;
	}
	
	public static float calcTrajectoryLength(float traj[][]){
		float len = 0f;
		for(int i=0;i<traj.length-1;i++){
			double dist = VVectorCalc.SquareEuclDistanceGap(traj[i], traj[i+1]);
			len+=Math.sqrt(dist);
		}
		return len;
	}
	
	public static VDataTable contributionToLinearFunction(VDataTable vt, VDataSet vd, VLinearFunction function){
		VDataTable res = new VDataTable();
		DecimalFormat df = new DecimalFormat("#.####");
		res.copyHeader(vt);
		res.rowCount = vt.rowCount;
		res.stringTable = new String[res.rowCount][res.colCount];
		for(int i=0;i<vt.rowCount;i++){
			for(int j=0;j<vt.colCount;j++){
				res.stringTable[i][j] = vt.stringTable[i][j];
			}
			float x[] = vd.getVector(i);
			res.stringTable[i][res.fieldNumByName("PC2")] = ""+df.format(function.calculate(x));
			int k=0;
			double coeffs[] = function.getCoeff();
			for(int j=0;j<vt.colCount;j++){
				if(vt.fieldTypes[j]==vt.NUMERICAL){
					if(Float.isNaN(x[k])) 
						res.stringTable[i][j] = "0";
					else
						res.stringTable[i][j] = df.format(x[k]*coeffs[k]);
					k++;
				}
			}
		}
		res.fieldNames[res.fieldNumByName("PC2")] = "PC1check";
		
		return res;
	}
	
	public static void analysisOfFactorImportance(VDataTable vt){
		for(int i=0;i<vt.rowCount;i++){
			Vector<Float> vp = new Vector<Float>();
			Vector<Float> vn = new Vector<Float>();
			Vector<Integer> vpi = new Vector<Integer>();
			Vector<Integer> vni = new Vector<Integer>();
			for(int j=0;j<vt.colCount;j++){
				if(vt.fieldTypes[j]==vt.NUMERICAL){
					float val = Float.parseFloat(vt.stringTable[i][j]);
					if(val>0) {vp.add(val); vpi.add(j);}
					if(val<0) {vn.add(val); vni.add(j);}
				}
			}
			float xp[] = new float[vp.size()*2];
			float xn[] = new float[vn.size()*2];
			float xpr[] = new float[vp.size()];
			float xnr[] = new float[vn.size()];			
			for(int j=0;j<vp.size();j++){
				xp[j] = vp.get(j); xp[j+vp.size()] = -vp.get(j); xpr[j] = vp.get(j);
			}
			for(int j=0;j<vn.size();j++){
				xn[j] = vn.get(j); xn[j+vn.size()] = -vn.get(j); xnr[j] = vn.get(j);
			}
			int ip[] = Algorithms.SortMass(xpr);			
			int in[] = Algorithms.SortMass(xnr);
			float threshp = VSimpleFunctions.calcStandardDeviation(xp)*1.5f;
			float threshn = VSimpleFunctions.calcStandardDeviation(xn)*1.5f;
			System.out.print(vt.stringTable[i][vt.fieldNumByName("COUNTR_A")]+"\t"+vt.stringTable[i][vt.fieldNumByName("YEAR")]+"\t");
			for(int j=0;j<vp.size();j++){
				if(vp.get(ip[ip.length-j-1])>threshp) System.out.print(vt.fieldNames[vpi.get(ip[ip.length-j-1])]+",");
			}
			System.out.print("\t");
			for(int j=0;j<vn.size();j++){
				if(vn.get(in[j])<-threshn) System.out.print(vt.fieldNames[vni.get(in[j])]+",");
			}
			System.out.println();
		}
	}
	
	public static VDataTable checkPC1AsIndex(VDataTable vt, Grid gr){
		DecimalFormat df = new DecimalFormat("#.###");	
		vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
		VDataTable vtng = VSimpleProcedures.filterMissingValues(vt, 1e-3f);
		VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vtng, -1);
		VDataSet vdfull = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		vd.calcStatistics();
		PCAMethod pca = new PCAMethod();
		pca.setDataSet(vd); pca.calcBasis(3);
		double d[] = pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);
		System.out.println("Explained variation:\t"+d[0]+"\t"+d[1]+"\t"+d[2]+"\td[0]/d[1]="+d[0]/d[1]);
		
		vt.addNewColumn("PC1", "", "", vt.NUMERICAL, "0");
		vt.addNewColumn("RANK", "", "", vt.NUMERICAL, "0");
		if(gr!=null){
			vt.addNewColumn("PNC1", "", "", vt.NUMERICAL, "0");
			vt.addNewColumn("RANK_PNC", "", "", vt.NUMERICAL, "0");
		}
		vt.addNewColumn("GAPPED", "", "", vt.NUMERICAL, "0");		
		
		int numOfCountries = (int)(1f/17f*vt.rowCount+0.5f);
		float projs[][] = new float[17][numOfCountries];
		
		vdfull.preProcess.clear();
		for(int j=0;j<vd.preProcess.size();j++){
			vdfull.addToPreProcess((VTablePreprocess)vd.preProcess.get(j));
		}
		vdfull.preProcessData();
		
		for(int i=0;i<vt.rowCount;i++){
			int year = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("YEAR")]);
			int num = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("NUMBER")]);
			float x[] = vdfull.getVector(i);
			if(Utils.isVectorGapped(x))
				vt.stringTable[i][vt.fieldNumByName("GAPPED")] = "1";
			float xp[] = pca.projectionFunction(x);
			projs[year-1989][num-1] = xp[0];
			vt.stringTable[i][vt.fieldNumByName("PC1")] = df.format(projs[year-1989][num-1]);
		}
		
		int inds[][] = new int[17][numOfCountries];
		for(int i=0;i<17;i++){
			int is[] = Algorithms.SortMass(projs[i]);
			for(int j=0;j<is.length;j++)
						inds[i][is[j]] = is.length-j;
		}
		
		for(int i=0;i<vt.rowCount;i++){
			int year = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("YEAR")]);
			int num = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("NUMBER")]);
			vt.stringTable[i][vt.fieldNumByName("RANK")] = ""+inds[year-1989][num-1];
		}
		
		if(gr!=null){
			for(int i=0;i<vt.rowCount;i++){
				int year = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("YEAR")]);
				int num = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("NUMBER")]);
				float x[] = vdfull.getVector(i);
				float xp[] = gr.projectPointGap(x, gr.PROJECTION_CLOSEST_POINT);
				projs[year-1989][num-1] = xp[0];
				vt.stringTable[i][vt.fieldNumByName("PNC1")] = df.format(projs[year-1989][num-1]);
			}
			
			inds = new int[17][numOfCountries];
			for(int i=0;i<17;i++){
				int is[] = Algorithms.SortMass(projs[i]);
				for(int j=0;j<is.length;j++)
							inds[i][is[j]] = is.length-j;
			}
			
			for(int i=0;i<vt.rowCount;i++){
				int year = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("YEAR")]);
				int num = Integer.parseInt(vt.stringTable[i][vt.fieldNumByName("NUMBER")]);
				vt.stringTable[i][vt.fieldNumByName("RANK_PNC")] = ""+inds[year-1989][num-1];
			}
		}
		
		return vtng;
	}
	
	public static Grid constructPrincipalCurve(VDataTable vt, boolean initializeFromPoints){
		vt.fieldTypes[vt.fieldNumByName("YEAR")] = vt.STRING;
		VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
		Grid grid = null;
		if(!initializeFromPoints){
		    grid = ElmapAlgorithm.computeElasticGrid(vd,"elmap.ini",12);
		}else{
			PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			vd.hasGaps = true;

			int resFactor = 2;
			
			int maxnumdim = 15;
			float vars[] = new float[maxnumdim];
			for(int i=1;i<=maxnumdim;i++){
				pca.calcBasis(i);
				grid = initializeGrid(vd,pca,resFactor);
				ElmapAlgorithm elmapt = new ElmapAlgorithm();
		        //elmapt.setGrid(grid);
		        elmapt.setData(vd);
		        elmapt.calcTaxons();
				float mse = (float)grid.calcMSE(vd, elmapt.taxons);
				vars[i-1] = mse;
			}
			for(int i=1;i<=maxnumdim;i++){
				System.out.println(i+"\t"+vars[i-1]);
			}
			int inds[] = Algorithms.SortMass(vars);
			System.out.println(""+(inds[0]+1)+" dimensions are chosen for missing values recovery.");
			
			pca.calcBasis(inds[0]+1);
			grid = initializeGrid(vd,pca,resFactor);

			Vector epochs = new Vector();
			ElmapAlgorithmEpoch ep = new ElmapAlgorithmEpoch();
			ep.extrapolate = 3;
			epochs.add(ep);
			ElmapAlgorithm.computeElasticGridPreinitialized(vd,grid,epochs);
			
			ElmapProjection elmapprojectiont = new ElmapProjection();
		    ElmapAlgorithm elmapt = new ElmapAlgorithm();
		    elmapt.grid = grid;
		    elmapprojectiont.setElmap(elmapt);
		    elmapprojectiont.setDataSet(vd);
		    elmapprojectiont.elmap.grid.MakeNodesCopy();
		    elmapprojectiont.elmap.taxons = elmapprojectiont.elmap.grid.calcTaxons(vd);
		    float mseelmapt = elmapprojectiont.calculateMSEToProjection(vd);
		    System.out.println("Final Elmap MSE = "+mseelmapt+"\n");
			
		}
		return grid;
	}
	
	public static void analyzeSmoothedTrajectory(VDataTable vt, Grid grid, String fn, float thresh){
		try{
		
		FileWriter fw1 = new FileWriter(fn+".pcurve1");
		FileWriter fw2 = new FileWriter(fn+".pcurve2");
			
		DecimalFormat df = new DecimalFormat("#.##");
		VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
		
		Vector<Float> vn = new Vector<Float>();
		for(int i=1;i<grid.Nodes.length-1;i++){
			float tension = grid.getNodeTension(i);
			vn.add(tension);
		}
		if(Float.isNaN(thresh))
			thresh = findPositiveThreshold(vn,0f,1.5f);
		System.out.println("Threshold = "+thresh);
		
		Vector<Integer> period = findPeriods(vn,thresh);
		
		for(int i=1;i<grid.Nodes.length-1;i++){
			float x = grid.Nodes3D[i][0];
			float x1 = grid.Nodes3D[i+1][0];
			float tension = grid.getNodeTension(i);
			String years = "";
			String yearsn = "";
			for(int j=0;j<vd.pointCount;j++){
				float xy[] = vd.getVector(j);
				float p[] = grid.projectPointGap(xy, grid.PROJECTION_CLOSEST_POINT);
				if((p[0]>=x)&&(p[0]<x1)){
					years+=vt.stringTable[j][vt.fieldNumByName("YEAR")]+"/";
					yearsn+=df.format(p[0])+";";
				}
			}
			if(!years.equals("")){
				years = years.substring(0,years.length()-1);
				yearsn = yearsn.substring(0,yearsn.length()-1);
			}
			String thresholded = "";
			if(tension>thresh)
				thresholded = ""+tension;
			System.out.println(x+"\t"+tension+"\t"+thresholded+"\t"+period.get(i-1)+"\t"+years+"\t"+yearsn);
			fw1.write(x+"\t"+tension+"\n");
		}
		System.out.println("\n\n");
		for(int j=0;j<vd.pointCount;j++){
			float xy[] = vd.getVector(j);
			float p[] = grid.projectPointGap(xy, grid.PROJECTION_CLOSEST_POINT);
			//System.out.println(vt.stringTable[j][vt.fieldNumByName("YEAR")]+"\t"+p[0]);
			fw2.write(vt.stringTable[j][vt.fieldNumByName("YEAR")]+"\t"+p[0]+"\n");
		}
		
		fw1.close();
		fw2.close();
		
		// Interpretation
		int numberOfPeriods = period.get(period.size()-1);
		for(int k=0;k<numberOfPeriods;k++){
			Vector<Integer> ids = new Vector<Integer>(); 
			System.out.print("Period\t"+(k+1)+"\t");
			for(int i=0;i<period.size();i++)
				if(period.get(i)==(k+1)){
					if(ids.indexOf(i)<0) ids.add(i);
					if(ids.indexOf(i+1)<0) ids.add(i+1);
					if(ids.indexOf(i-1)<0) ids.add(i-1);
				}
			Collections.sort(ids);
			VDataSet vdp = new VDataSet();
			vdp.coordCount = grid.dimension;
			vdp.pointCount = ids.size();
			vdp.massif = new float[vdp.pointCount][vdp.coordCount];
			for(int i=0;i<ids.size();i++){
				vdp.massif[i] = grid.Nodes[ids.get(i)+1];
				//System.out.println("Period="+k+" id="+ids.get(i));
			}
			PCAMethod pcap = new PCAMethod();
			pcap.setDataSet(vdp);
			pcap.calcBasis(1);
			Vector<Float> pn = new Vector<Float>();
			Vector<Float> pp = new Vector<Float>();
			float ps[] = new float[vdp.coordCount];
			for(int i=0;i<vdp.coordCount;i++){
				if(pcap.getBasis().basis[0][i]<0) pn.add((float)pcap.getBasis().basis[0][i]);
				if(pcap.getBasis().basis[0][i]>0) pp.add((float)pcap.getBasis().basis[0][i]);
				ps[i] = (float)pcap.getBasis().basis[0][i];
			}
			int sign = +1;
			float fromStartToEnd[] = new float[vdp.coordCount];
			for(int i=0;i<vdp.coordCount;i++)
				fromStartToEnd[i] = vdp.massif[vdp.pointCount-1][i]-vdp.massif[0][i];
			if(VVectorCalc.ScalarMult(ps, fromStartToEnd)<0) 
				ps = VVectorCalc.Product_(ps, -1);
			int inds[] = Algorithms.SortMass(ps);
			float zthresh = 1.3f;
			float thp = findPositiveThreshold(pp,0f,zthresh);
			float thn = findPositiveThreshold(pn,0f,zthresh);
			for(int i=0;i<inds.length;i++){
				if((ps[inds[i]]>0)&&(ps[inds[i]]>thp))
					System.out.print("("+df.format(ps[inds[i]])+")"+vt.fieldNames[vd.selector.selectedColumns[inds[i]]]+"\t");
				if((ps[inds[i]]<0)&&(ps[inds[i]]<-thn))
					System.out.print("("+df.format(ps[inds[i]])+")"+vt.fieldNames[vd.selector.selectedColumns[inds[i]]]+"\t");
			}
			System.out.println();
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static float findPositiveThreshold(Vector<Float> values, float zeroPoint, float zthresh){
		float xp[] = new float[values.size()*2];
		float xpr[] = new float[values.size()];
		for(int j=0;j<values.size();j++){
			xp[j] = values.get(j)-zeroPoint; xp[j+values.size()] = -values.get(j)+zeroPoint; xpr[j] = values.get(j)-zeroPoint;
		}
		float threshp = VSimpleFunctions.calcStandardDeviation(xp)*zthresh;
		return threshp+zeroPoint;
	}
	
	public static float findNegativeThreshold(Vector<Float> values, float zeroPoint, float zthresh){
		float xp[] = new float[values.size()*2];
		float xpr[] = new float[values.size()];
		for(int j=0;j<values.size();j++){
			xp[j] = values.get(j)-zeroPoint; xp[j+values.size()] = -values.get(j)+zeroPoint; xpr[j] = values.get(j)-zeroPoint;
		}
		float threshp = VSimpleFunctions.calcStandardDeviation(xp)*zthresh;
		return threshp;
	}
	
	public static Vector<Integer> findPeriods(Vector<Float> vn, float thresh){
		Vector<Integer> res = new Vector<Integer>();
		int period=1;
		float valprev = 0; 
		float valnext = 0;
		for(int i=0;i<vn.size()-1;i++){
			float val = vn.get(i)>=thresh?vn.get(i):0f;
			valnext = vn.get(i+1)>=thresh?vn.get(i+1):0f;
			res.add(period);
			if((val>valprev)&&(val>valnext)){
				period++;
			}
			valprev = val;
		}
		res.add(period);
		return res;
	}
	
	public static Curve initializeGrid(VDataSet vd, PCAMethod pca, int resFactor){
		int numberOfNodes = resFactor*(vd.pointCount-1)+1;
		Curve grid = new Curve(numberOfNodes,vd.coordCount);
		grid.Nodes = new float[numberOfNodes][grid.dimension];
		grid.setDefaultEP(0.1f);
		grid.setDefaultRP(0.05f);
		int nodei = 0;
		for(int i=0;i<vd.pointCount;i++){
			float xn[] = vd.getVector(i);
			float xn1[] = null;
			if(i!=vd.pointCount-1)
				xn1 = vd.getVector(i+1);
			if(Utils.isVectorGapped(xn)) 
				xn = pca.recoverMissingValues(xn);
			if(xn1!=null)
				if(Utils.isVectorGapped(xn1)) 
					xn1 = pca.recoverMissingValues(xn1);
			if(i!=vd.pointCount-1){
				for(int k=0;k<resFactor;k++){
					float u = (float)k/(float)resFactor;
					grid.Nodes[nodei] = VVectorCalc.Add_(xn, VVectorCalc.Product_(VVectorCalc.Subtract_(xn1, xn), u));
					nodei++;
				}
			}else{
				grid.Nodes[numberOfNodes-1] = xn;
			}
		}
		return grid;
	}

	
}
