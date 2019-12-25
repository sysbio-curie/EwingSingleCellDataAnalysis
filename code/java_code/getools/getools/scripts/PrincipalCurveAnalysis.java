package getools.scripts;

import java.io.FileWriter;

import vdaoengine.analysis.elmap.ElmapAlgorithm;
import vdaoengine.analysis.elmap.Grid;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.VSimpleProcedures;
import vdaoengine.utils.VVectorCalc;

public class PrincipalCurveAnalysis {

	VDataTable vt = null;
	VDataSet vd = null;
	Grid grid = null;
	String folder = null;
	String prefix = null;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			
			PrincipalCurveAnalysis pcua = new PrincipalCurveAnalysis();
			
			//pcua.folder = "C:/Datas/NotchP53/data/t\\\\\cga/";
			pcua.folder = "C:/Datas/SylviaFre/pcurve_test/";
			//pcua.prefix = "BIOCARTA_TGFB_PATHWAY_T";
			//pcua.prefix = "NotchTargets_T";
			pcua.prefix = "htseq_l.txt";
			pcua.loadData();
			pcua.computePrincipalCurve();
			pcua.saveTangentVectors(pcua.folder+pcua.prefix+"_tangent.txt");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadData(){
		String fn = folder+prefix+".dat";
		vt = VDatReadWrite.LoadFromVDatFile(fn);
		vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
	}
	
	public void computePrincipalCurve(){
		   grid = ElmapAlgorithm.computeElasticGrid(vd,"elmap.ini",12);
		   String fn = folder+prefix+"_projections.txt";
		   String fnvem = folder+prefix+".vem";		   
		   grid.saveProjections(fn, vd, grid.PROJECTION_CLOSEST_POINT);
		   grid.saveToFile(fnvem, prefix);
	}
	
	public void saveTangentVectors(String fn) throws Exception{
		FileWriter fw = new FileWriter(fn);
		fw.write("NODE\t");
		for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL){
			fw.write(vt.fieldNames[i]+"\t");
		}
		fw.write("\n");
		for(int i=0;i<grid.Nodes.length-1;i++){
			float v1[] = grid.Nodes[i];
			float v2[] = grid.Nodes[i+1];
			VVectorCalc.Subtr(v1, v2);
			VVectorCalc.Normalize(v1);
			fw.write((i+1)+"\t");
			int k=0;
			for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
				fw.write(v1[k++]+"\t");
			}
			fw.write("\n");
		}
		fw.close();
	}

}
