package getools.scripts;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;

public class XenopusPeshkinDataAnalysis {

	public static void main(String[] args) {
		try{

			String fields[] = {"PC1","PC2","PC3"};
			//MakeVevFileFromJSONGraph("C:/GitPrograms/SPRING-master/datasets/frog_s22/graph_data.json","C:/Datas/Peshkin/demo/day22/s22fnga.txt",fields);
			
			
			/*String folder = "C:/Datas/Peshkin/xenopus1/Processed/ica/";
			String prefix = "day22";
			Vector<String> geneNames = Utils.loadVectorOfStrings(folder+"genenames.txt");
			System.out.println("genes = "+geneNames.size());
			//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(folder+prefix+".txt", false, "\t",true);
			//System.out.println(vt.colCount+"\t"+vt.rowCount);
			//System.out.println("'"+vt.stringTable[0][0]+"'\t"+"'"+vt.stringTable[0][1]+"'\t'"+vt.stringTable[0][2]+"'\t...");
			
			String line = null;
			LineNumberReader lr = new LineNumberReader(new FileReader(folder+prefix+".txt"));
			FileWriter fw = new FileWriter(folder+prefix+"f.txt");
			DecimalFormat df = new DecimalFormat("#.###");
			
			int colCount = 0;
			int count = 0;
			
			line = lr.readLine();
			String nums[] = line.split(" ");
			for(int i=0;i<nums.length;i++) if(!nums[i].trim().equals("")) colCount++;
			
			fw.write("GENE\t");
			for(int j=0;j<colCount;j++){
				String s = ""+j;
				if(s.length()==1) s = "000"+s;
				if(s.length()==2) s = "00"+s;
				if(s.length()==3) s = "0"+s;
				fw.write(prefix+"_"+s+"\t");
			}
			fw.write("\n");
			
			
			do{

				fw.write(geneNames.get(count)+"\t");
				
				nums = line.split(" ");
				int cc = 0;
				for(int i=0;i<nums.length;i++) 
					if(!nums[i].trim().equals("")){ 
						String s = nums[i];
						s = df.format(Float.parseFloat(s));
						fw.write(s+"\t");
				}
					
				fw.write("\n");
				count++;
				if(count==(int)((float)(count)/100)*100)
					System.out.println(count);
			
			}while((line=lr.readLine())!=null);
			
			lr.close();
			fw.close();
			*/
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
