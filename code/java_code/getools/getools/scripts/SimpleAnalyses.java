package getools.scripts;

import java.util.Vector;

import vdaoengine.utils.Utils;

public class SimpleAnalyses {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			countNumberOfOperands("C:/_DatasArchive/Metastasis/descs.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void countNumberOfOperands(String fn){
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		int numberOfOperands1 = 0;
		int numberOfOperands2 = 0;
		for(String s:lines){
			String parts[] = s.split(";");
			if(parts.length>1){
			String rule1 = parts[1];
			int numberOfOrs = countNumberOfChar(rule1,'|');
			int numberOfAnds = countNumberOfChar(rule1,'&');
			numberOfOperands1 = numberOfOrs+numberOfAnds; 
			if(parts.length>2){
				String rule2 = parts[3];
				numberOfOrs = countNumberOfChar(rule2,'|');
				numberOfAnds = countNumberOfChar(rule2,'&');
				numberOfOperands2 = numberOfOrs+numberOfAnds; 
			}
			}
			System.out.print(""+numberOfOperands1+"\t"+numberOfOperands2+"\n");
		}
	}
	
	public static int countNumberOfChar(String s, char c){
		int res = 0;
		char cs[] = s.toCharArray();
		for(int i=0;i<cs.length;i++)
			if(cs[i]==c)
				res++;
		return res;
	}

}
