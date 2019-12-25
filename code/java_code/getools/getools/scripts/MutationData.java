package getools.scripts;

import java.io.FileWriter;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;

public class MutationData {

	public static void main(String[] args) {
		try{
			
			String prefix = "C:/Datas/NetNorm/BRCA_mutation";
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(prefix+".csv", true, ",", true);
			
			FileWriter fwg = new FileWriter(prefix+".genes");
			for(int i=1;i<vt.colCount-2;i++)
				fwg.write(vt.fieldNames[i]+"\n");
			fwg.close();

			FileWriter fws = new FileWriter(prefix+".samples");
			for(int i=0;i<vt.rowCount;i++)
				fws.write(vt.stringTable[i][0]+"\n");
			fws.close();
			
			FileWriter fwn = new FileWriter(prefix+".numerical");
			for(int i=0;i<vt.rowCount;i++){
				for(int j=1;j<vt.colCount-2;j++)
					fwn.write(vt.stringTable[i][j]+"\t");
				fwn.write("\n");
			}
			fwn.close();
			

			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
