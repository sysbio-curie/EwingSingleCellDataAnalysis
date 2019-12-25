package vdaoengine.utils;

import java.io.*;
import java.util.*;

import vdaoengine.data.VDataSet;
import vdaoengine.data.VDataTable;
import vdaoengine.data.VStatistics;


public class Utils {

  public static long lastUsedMemory;

  public static String replaceString(String source,String shabl,String val){
        int i=0;
        String s1 = new String(source);
        StringBuffer sb = new StringBuffer(s1);
        while((i=sb.indexOf(shabl))>=0){
                sb.replace(i,i+shabl.length(),val);
        }
        s1 = sb.toString();
        return s1;
  }

  public static String replaceStringCount(String source,String shabl,String val){
        int i=0;
        String s1 = new String(source);
        int k = 0;
        StringBuffer sb = new StringBuffer(s1);
        while((i=sb.indexOf(shabl))>=0){
                sb.replace(i,i+shabl.length(),val);
                //if((int)(k/100)*100==k)
                //  System.out.print(k+" ");
                k++;
        }
        //System.out.println();
        s1 = sb.toString();
        return s1;
  }


public static String loadString(String fn){
  StringBuffer sb = new StringBuffer();
  try{
  LineNumberReader lr = new LineNumberReader(new FileReader(fn));
  String s = null;
  while((s=lr.readLine())!=null){
    sb.append(s+"\n");
  }
  lr.close();
  }catch(Exception e){
    e.printStackTrace();
  }
  return sb.toString();
}

public static Vector<String> loadVectorOfStrings(String fn){
	Vector<String> res = new Vector<String>();
	  try{
	  LineNumberReader lr = new LineNumberReader(new FileReader(fn));
	  String s = null;
	  while((s=lr.readLine())!=null){
	    res.add(s);
	  }
	  lr.close();
	  }catch(Exception e){
	    e.printStackTrace();
	  }
	  return res;
	}




public static String loadString(InputStream is){
  StringBuffer sb = new StringBuffer();
  try{
    int b = 0;
    while((b = is.read())>=0){
      sb.append((char)b);
    }
  }catch(Exception e){
    e.printStackTrace();
  }
  return sb.toString();
}



public static String correctName(String name){
  name = Utils.replaceString(name," ","_");
  name = Utils.replaceString(name,"*","_");
  name = Utils.replaceString(name,"-","_");
  name = Utils.replaceString(name,"[","_");
  name = Utils.replaceString(name,"]","_");
  name = Utils.replaceString(name,"__","_");
  name = Utils.replaceString(name,"__","_");
  name = Utils.replaceString(name,":","_");
  if(name.endsWith("_"))
    name = name.substring(0,name.length()-1);

  byte mc[] = name.getBytes();
  StringBuffer sb = new StringBuffer(name);
  for(int i=0;i<mc.length;i++)
    //System.out.println(name.charAt(i)+"\t"+mc[i]);
    if(mc[i]<=0)
      sb.setCharAt(i,'_');
  return sb.toString();
}


public static void printUsedMemory(){
    long mem = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
    lastUsedMemory = mem;
    //long mem = Runtime.getRuntime().freeMemory();
    mem = (long)(1e-6f*mem);
    System.out.println("Used memory "+mem+"M");
}

public static void printUsedMemorySinceLastTime(){
    long mem = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
    long used = mem - lastUsedMemory;
    lastUsedMemory = mem;
    //long mem = Runtime.getRuntime().freeMemory();
    used = (long)(1e-3f*used);
    System.out.println("Used since last time "+used+"K");
}

public static long getUsedMemorySinceLastTime(){
    long mem = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
    long used = mem - lastUsedMemory;
    lastUsedMemory = mem;
    //long mem = Runtime.getRuntime().freeMemory();
    //used = (long)(1e-3f*used);
    //System.out.println(used);
    return used;
}
public static long getUsedMemory(){
    long mem = Runtime.getRuntime().totalMemory();
    //long used = mem - lastUsedMemory;
    //lastUsedMemory = mem;
    //long mem = Runtime.getRuntime().freeMemory();
    //used = (long)(1e-3f*used);
    //System.out.println(used);
    return mem;
}
public static long getUsedMemoryMb(){
    long mem = Runtime.getRuntime().totalMemory();
    //long used = mem - lastUsedMemory;
    //lastUsedMemory = mem;
    //long mem = Runtime.getRuntime().freeMemory();
    //used = (long)(1e-3f*used);
    //System.out.println(used);
    return (int)((float)mem*1e-6+0.5f);
}


public static void printUsedMemorySinceLastTimeByte(){
    long mem = ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
    long used = mem - lastUsedMemory;
    lastUsedMemory = mem;
    //long mem = Runtime.getRuntime().freeMemory();
    //used = (long)(1e-3f*used);
    System.out.println("Used since last time "+used+"Bytes");
}

public static int compareTwoSets(Vector v1, Vector v2){
 int inters = 0;
 for(int i=0;i<v1.size();i++){
   if(v2.indexOf(v1.get(i))>=0)
     inters++;
   else
     System.out.println(v1.get(i)+" from list1 not found in list2");
 }
 for(int i=0;i<v2.size();i++){
   if(v1.indexOf(v2.get(i))<0)
     System.out.println(v2.get(i)+" from list2 not found in list1");
 }
 return inters;
}

public static Set UnionOfSets(Set set1, Set set2){
    Iterator it = set2.iterator();
    while(it.hasNext()){
      Object o = it.next();
      if(!set1.contains(o))
        set1.add(o);
    }
    return set1;
  }
public static HashSet IntersectionOfSets(Set set1, Set set2){
	HashSet seti = new HashSet();
    Iterator it = set2.iterator();
    while(it.hasNext()){
      Object o = it.next();
      if(set1.contains(o))
        seti.add(o);
    }
    return seti;
  }



public static float[] doubleArrayTofloat(double x[]){
	float res[] = new float[x.length];
	for(int i=0;i<x.length;i++)
		res[i] = (float)x[i];
	return res;
}

public static boolean isVectorGapped(float x[]){
	boolean gapped = false;
	for(int i=0;i<x.length;i++)
		if(Float.isNaN(x[i])){
			gapped = true;
			break;
		}
	return gapped;
}

public static Vector<String> loadStringListFromFile(String fn){
	Vector<String> list = new Vector<String>(); 
	try{
	LineNumberReader lr = new LineNumberReader(new FileReader(fn));
	String s = null;
	while((s=lr.readLine())!=null){
	  list.add(s.trim());
	}
	lr.close();
	}catch(Exception e){
	  e.printStackTrace();
	}
	return list;
	}

public static VDataTable PrepareTableForVidaExpert(VDataTable vt){
	for(int i=0;i<vt.rowCount;i++)
		for(int j=0;j<vt.colCount;j++){
			String s = vt.stringTable[i][j];
			if(vt.stringTable[i][j].equals(""))
				vt.stringTable[i][j] = "_";
			s = Utils.replaceString(s, "'", "_");
			vt.stringTable[i][j] = s;
		}
	return vt;
}

public static void writeMassif1DToFile(int massif[], String fn){
	try{
		FileWriter fw = new FileWriter(fn);
		for(int i=0;i<massif.length;i++)
			fw.write(massif[i]+"\n");
		fw.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}

public static Vector<Integer> findDistributionOutliersByGapAnalysis(float[] vals, float threshold, float gapValue) {
	Vector<Integer> outliers = new Vector<Integer>();
	int inds[] = Algorithms.SortMass(vals);
	int indsreverse[] = new int[inds.length];
	for(int i=0;i<inds.length;i++) indsreverse[i] = inds[inds.length-i-1];
	int maxIndWithGap = -1;
	for(int k=0;k<indsreverse.length;k++){
		if(vals[indsreverse[k]]<threshold) break;
		float gap = vals[indsreverse[k]]/vals[indsreverse[k+1]];
		//System.out.println(gap);
		if(gap>=gapValue) maxIndWithGap = k; 
	}
	if(maxIndWithGap>=0){
		for(int k=0;k<=maxIndWithGap;k++)
			outliers.add(indsreverse[k]);
	}
	return outliers;
}

public static float[] makeLongestTailPositive(float[] vals, float threshold) {
	float newvals[] = new float[vals.length];
	int sign = determineTheLongestTale(vals,threshold);
	for(int i=0;i<vals.length;i++)
		newvals[i] = vals[i]*sign;
	return newvals;
}

public static int[] determineTheLongestTales(VDataSet vd, float thresh){
	int signs[] = new int[vd.coordCount];
	for(int i=0;i<vd.coordCount;i++){
		float vals[] = new float[vd.pointCount];
		for(int k=0;k<vals.length;k++){
			vals[k] = vd.massif[k][i];
		}
		signs[i] = determineTheLongestTale(vals, thresh);
	}
	return signs;
}

public static int determineTheLongestTale(float vals[], float thresh){
		int sign = 1;
		float sumpositive = 0f;
		float sumnegative = 0f;
		for(int j=0;j<vals.length;j++)if(!Float.isNaN(vals[j])){
			if(vals[j]>thresh) sumpositive+=vals[j];
			if(vals[j]<-thresh) sumnegative+=-vals[j];
		}
		if(sumnegative>sumpositive) sign=-1;
	return sign;
}




}