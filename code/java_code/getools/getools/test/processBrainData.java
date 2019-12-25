package getools.test;

import getools.*;
import java.util.*;
import java.io.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

public class processBrainData {
	
	public static void main(String args[]){
		try{
			
			VDataTable vt1 = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/gsda/data/p53gsea/p53_hgu95av2.txt", true, "\t");
			VDataTable vt2 = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/gsda/data/p53gsea/p53_hgu95av2.txt", true, "\t");;
			
			for(int i=2;i<vt1.colCount;i++){
			for(int j=0;j<vt1.rowCount;j++){
				float f = Float.parseFloat(vt1.stringTable[j][i]);
				f = (float)Math.log(f);
				vt2.stringTable[j][i] = ""+f;
			}}
			
			VDatReadWrite.saveToSimpleDatFile(vt2, "c:/datas/gsda/data/p53gsea/p53_log.txt");
			
			
			System.exit(0);
			
			String prefix = "c:/datas/gsda/data/brain/test/pierre/";
			String file = "gse1572";
			
			/*VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+"/"+file+".txt", true, "\t");
			
			VDataTable annot = VDatReadWrite.LoadFromSimpleDatFile("c:/datas/GSDA/HG_U95Av2.chip", true, "\t");
			
			vt = VSimpleProcedures.MergeTables(vt, "ID", annot, "Probe", "_");
			
			VDatReadWrite.saveToVDatFile(vt, prefix+"/"+file+".dat");*/
			
			/*VDataTable vt = VDatReadWrite.LoadFromVDatFile(prefix+"/"+file+".dat");
			
			Vector sigs = GMTReader.readGMTDatabase("c:/datas/gsda/merged.gmt");
			GESignature ageing = null;
			for(int i=0;i<sigs.size();i++){
				GESignature sig = (GESignature)sigs.get(i);
				if(sig.name.equals("AGEING_BRAIN"))
					ageing = sig;
			}
			
			vt.makePrimaryHash("GeneSymbol");
			VDataTable vtag = VSimpleProcedures.selectRowsFromList(vt, ageing.geneNames);
			
			VDatReadWrite.saveToVDatFile(vtag, prefix+"/"+file+"_ageing.dat");*/
			
			VDataTable vt = VDatReadWrite.LoadFromVDatFile(prefix+"/"+file+"_ageing.dat");
			//vt = TableUtils.normalizeVDat(vt, true, false);
			vt = VSimpleProcedures.substituteRowsByStatistics(vt, "GeneSymbol", 4);
			VDatReadWrite.saveToVDatFile(vt, prefix+"/"+file+"_ageing_collapsed.dat");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
