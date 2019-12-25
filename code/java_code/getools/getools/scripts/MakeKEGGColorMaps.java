package getools.scripts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.data.VStatistics;
import vdaoengine.utils.Utils;

public class MakeKEGGColorMaps {
	
	public static float threshold = 2f;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			HashMap<String, Float> values = loadValuesFromFile("C:/Datas/Ivashkine/ZebraFish/gsea_ic3.rnk");
			String text = createTextForKEGGColorMaps("C:/Datas/Ivashkine/ZebraFish/MAPK_pathway.txt", values);
			System.out.println(text);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static String createTextForKEGGColorMaps(String fn_pathway_def, HashMap<String, Float> values){
		String text = "";
		HashMap<String, String> keggids = loadKEGGIdentifiersFromPathwayDescription(fn_pathway_def);
		HashMap<String, Float> kovalues = new HashMap<String, Float>();
		Set<String> it = keggids.keySet();
		for(String s: it){
			Float value = values.get(s);
			//if(s.equals("cacna1ba")||s.equals("cacna1bb"))
			//	System.out.println("");
			if(value!=null){
				String ko = keggids.get(s);
				if(ko!=null){
					Float temp = kovalues.get(ko);
					if(temp==null)
						kovalues.put(ko, value);
					else{
						if(Math.abs(value)>Math.abs(temp))
							kovalues.put(ko, value);
					}
				}
			}
		}
		
		it = kovalues.keySet();
		/*VStatistics vst = new VStatistics(1); 
		for(String s: it){
			float val[] = new float[1];
			val[0] = kovalues.get(s);
			vst.addNewPoint(val);
			//text+=s+"\t"+kovalues.get(s)+"\n";
		}
		vst.calculate();*/
		for(String s: it){
			float val = kovalues.get(s);
			if(val>threshold) val = threshold;
			if(val<-threshold) val = -threshold;
			int red = 0;
			int green = 0;
			int blue = 0;
			if(val>0){
				red = 255;
				green = 255-(int)(val/threshold*255);
				blue = 255-(int)(val/threshold*255);
			}
			if(val<0){
				red = 255-(int)(-val/threshold*255);				
				green = 255;
				blue = 255-(int)(-val/threshold*255);								
			}
			//System.out.println(red+"\t"+green+"\t"+blue);
			String rd = ""+Integer.toHexString(red);
			String gr = ""+Integer.toHexString(green);
			String bl = ""+Integer.toHexString(blue);
			if(rd.length()==1) rd = "0"+rd;
			if(gr.length()==1) gr = "0"+gr;
			if(bl.length()==1) bl = "0"+bl;			
			String sval = "#"+rd+gr+bl;
			text+=s+"\t"+sval+"\n";
		}
		return text;
	}
	
	public static HashMap<String, Float> loadValuesFromFile(String fn){
		HashMap<String, Float> values = new HashMap<String, Float>();
		Vector<String> ss = Utils.loadStringListFromFile(fn);
		for(String s: ss){
			StringTokenizer st = new StringTokenizer(s,"\t");
			String name = st.nextToken();
			Float val = Float.parseFloat(st.nextToken());
			values.put(name, val);
		}
		return values;
	}
	
	public static HashMap<String, String> loadKEGGIdentifiersFromPathwayDescription(String fn){
		HashMap<String, String> keggids = new HashMap<String, String>();
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		for(String s: lines){
			if(s.contains("[KO:")){
				StringTokenizer st = new StringTokenizer(s,";:\t] ,");
				String name = st.nextToken();
				String ko = null;
				while(st.hasMoreTokens()){
					String tok = st.nextToken();
					if(tok.equals("[KO"))
						ko = st.nextToken();
				}
				keggids.put(name, ko);
			}
		}
		return keggids;
	}

}
