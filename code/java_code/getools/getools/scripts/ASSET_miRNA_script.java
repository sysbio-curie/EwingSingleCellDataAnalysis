package getools.scripts;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.utils.Utils;

public class ASSET_miRNA_script {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			// create gmt files
			HashMap<String, Vector<String>> gene2miRNA = new HashMap<String, Vector<String>>();
			Vector<String> lines = Utils.loadStringListFromFile("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/hsa-vtm-gene.txt");
			
			Vector<String> pathway_lines = Utils.loadStringListFromFile("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/genesets.gmt");
			
			for(int i=0;i<lines.size();i++){
				StringTokenizer st = new StringTokenizer(lines.get(i),"\t");
				String miRNA = st.nextToken();
				while(st.hasMoreTokens()){
					String gene = st.nextToken();
					Vector<String> mirnas = gene2miRNA.get(gene);
					if(mirnas==null) mirnas = new Vector<String>();
					if(!mirnas.contains(miRNA)) mirnas.add(miRNA);
					gene2miRNA.put(gene, mirnas);
				}
			}
			
			Set<String> it = gene2miRNA.keySet();
			FileWriter fw = new FileWriter("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/gene.gmt");
			FileWriter fwn = new FileWriter("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/mirna_network.sif");
			for(String gene: it){
				fw.write(gene+"\tna\t");
				Vector<String> mirnas = gene2miRNA.get(gene);
				for(String mirna: mirnas) { fw.write(mirna+"\t"); fwn.write(mirna+"\tmirna\t"+gene+"\n"); }
				fw.write("\n");
			}
			fw.close();
			fwn.close();
			
			FileWriter fw1 = new FileWriter("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/pathway.gmt");
			FileWriter fw1n = new FileWriter("C:/Datas/ASSET/miRNA_Iljin_Kristina/analysis/pathway.sif");
			for(String pthwl: pathway_lines){
				StringTokenizer st = new StringTokenizer(pthwl,"\t");
				String pthw = st.nextToken();
				st.nextToken();
				Vector<String> mirnas = new Vector<String>();
				while(st.hasMoreTokens()){
					String gene = st.nextToken();
					fw1n.write(gene+"\tpathway\t"+pthw+"\n");
					Vector<String> mr = gene2miRNA.get(gene);
					if(mr!=null){
						for(String s: mr)
						if(!mirnas.contains(s)) mirnas.add(s);
					}
				}
				fw1.write(pthw+"\tna\t");
				for(String s: mirnas) { fw1.write(s+"\t"); } 
				fw1.write("\n");
			}
			fw1.close();
			fw1n.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
