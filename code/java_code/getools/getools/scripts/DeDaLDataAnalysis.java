package getools.scripts;

import java.io.FileWriter;
import java.util.Date;
import java.util.Vector;

import vdaoengine.analysis.NPCAMethod;
import vdaoengine.analysis.PCAMethod;
import vdaoengine.analysis.VDistanceMatrix;
import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.PairInt;
import vdaoengine.utils.VSimpleProcedures;
import vdaoengine.utils.VVectorCalc;
import fr.curie.BiNoM.pathways.analysis.structure.*;
import fr.curie.BiNoM.pathways.wrappers.XGMML;
import getools.TableUtils;

public class DeDaLDataAnalysis {

	public static void main(String[] args) {
		try{
	
			Graph gr_fd = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML("C:/Datas/DeDaL/appealing_example/forpaper/tissues_fd.xgmml"));
			Graph gr_selnet = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML("C:/Datas/DeDaL/appealing_example/forpaper/tissues_selnet.xgmml"));

			Vector<String> genes = new Vector<String>();
			for(Node n: gr_fd.Nodes)
				if(!genes.contains(n.Id)) genes.add(n.Id);
			System.out.println(genes.size()+" nodes in the network");
			
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/DeDaL/appealing_example/forpaper/tissues_selnet2_c.txt", true, "\t");
			TableUtils.findAllNumericalColumns(vt);
			vt.makePrimaryHash("GENE");
			vt = VSimpleProcedures.selectRowsFromList(vt, genes);
			vt = TableUtils.normalizeVDat(vt, true, false);
			VDatReadWrite.saveToVDatFile(vt, "C:/Datas/DeDaL/appealing_example/forpaper/tissues_selnet2_c.dat");
			VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vt, -1);
			System.out.println(vd.massif[0].length+" dimensional data");
			//vd.pointCount = 00;
			
			/*PCAMethod pca = new PCAMethod();
			pca.setDataSet(vd);
			pca.calcBasis(3);
			vd = pca.getProjectedDataset();*/
			
		    VDistanceMatrix mnd = new VDistanceMatrix();
			//VVectorCalc.distanceType = VVectorCalc.EUCLIDEAN_DISTANCE;
		    mnd.calculatePairs = true;
		    Date tm = new Date();
		    System.out.println("Calculating distance matrix...");
		    mnd.calculateMatrix(vd);
		    NPCAMethod npca = new NPCAMethod();
		    System.out.println("Calculating NPCA...");
		    npca.calc(mnd);
		    System.out.println("Calculated "+((new Date().getTime()-tm.getTime())));

		    
		    
			FileWriter fw = new FileWriter("C:/Datas/DeDaL/appealing_example/forpaper/dist.txt");
			FileWriter fw_pairs = new FileWriter("C:/Datas/DeDaL/appealing_example/forpaper/pairs.txt");
		    for(int k=0;k<npca.componentPairs.size();k++){
		    	//System.out.println("Component "+i+"\t"+((PairInt)npca.componentPairs.get(i)).i1+"\t"+((PairInt)npca.componentPairs.get(i)).i2);
		    	int i = ((PairInt)npca.componentPairs.get(k)).i1;
		    	int j = ((PairInt)npca.componentPairs.get(k)).i2;
				float nim[] = vd.massif[i];
				float njm[] = vd.massif[j];
				float ni_fd[] = new float[2];
				ni_fd[0] = gr_fd.getNode(genes.get(i)).x;
				ni_fd[1] = gr_fd.getNode(genes.get(i)).y;
				float nj_fd[] = new float[2];
				nj_fd[0] = gr_fd.getNode(genes.get(j)).x;
				nj_fd[1] = gr_fd.getNode(genes.get(j)).y;
				float ni_selnet[] = new float[2];
				ni_selnet[0] = gr_selnet.getNode(genes.get(i)).x;
				ni_selnet[1] = gr_selnet.getNode(genes.get(i)).y;
				float nj_selnet[] = new float[2];
				nj_selnet[0] = gr_selnet.getNode(genes.get(j)).x;
				nj_selnet[1] = gr_selnet.getNode(genes.get(j)).y;
				
				VVectorCalc.distanceType = VVectorCalc.EUCLIDEAN_DISTANCE;
				double distm = VVectorCalc.Distance(nim, njm);
				double dist_fd = VVectorCalc.Distance(ni_fd, nj_fd);
				double dist_selnet = VVectorCalc.Distance(ni_selnet, nj_selnet);
				
				fw_pairs.write(""+genes.get(i)+"\t"+genes.get(j)+"\n");
				fw.write(""+distm+"\t"+dist_fd+"\t"+dist_selnet+"\n");
		    }
			fw.close();
			fw_pairs.close();
			
			/*Vector<String> genes = new Vector<String>();
			for(Node n: gr_fd.Nodes)
				if(!genes.contains(n.Id)) genes.add(n.Id);
			System.out.println(genes.size()+" nodes in the network");
			
			FileWriter fw = new FileWriter("C:/Datas/DeDaL/appealing_example/forpaper/dist.txt");
			FileWriter fw_pairs = new FileWriter("C:/Datas/DeDaL/appealing_example/forpaper/pairs.txt");
			for(int i=0;i<genes.size();i++)
				for(int j=i+1;j<genes.size();j++){
					float nim[] = vd.massif[i];
					float njm[] = vd.massif[j];
					float ni_fd[] = new float[2];
					ni_fd[0] = gr_fd.getNode(genes.get(i)).x;
					ni_fd[1] = gr_fd.getNode(genes.get(i)).y;
					float nj_fd[] = new float[2];
					nj_fd[0] = gr_fd.getNode(genes.get(j)).x;
					nj_fd[1] = gr_fd.getNode(genes.get(j)).y;
					float ni_selnet[] = new float[2];
					ni_selnet[0] = gr_selnet.getNode(genes.get(i)).x;
					ni_selnet[1] = gr_selnet.getNode(genes.get(i)).y;
					float nj_selnet[] = new float[2];
					nj_selnet[0] = gr_selnet.getNode(genes.get(j)).x;
					nj_selnet[1] = gr_selnet.getNode(genes.get(j)).y;
					
					VVectorCalc.distanceType = VVectorCalc.EUCLIDEAN_DISTANCE;
					double distm = VVectorCalc.Distance(nim, njm);
					double dist_fd = VVectorCalc.Distance(ni_fd, nj_fd);
					double dist_selnet = VVectorCalc.Distance(ni_selnet, nj_selnet);
					
					fw_pairs.write(""+genes.get(i)+"\t"+genes.get(j)+"\n");
					fw.write(""+distm+"\t"+dist_fd+"\t"+dist_selnet+"\n");
				}
			fw.close();
			fw_pairs.close();
			*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
