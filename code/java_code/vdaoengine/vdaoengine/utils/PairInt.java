package vdaoengine.utils;

import java.util.*;

public class PairInt {
  public int i1;
  public int i2;
  public PairInt(int i, int j) {
    i1 = i; i2 = j;
  }
  public boolean equals(PairInt pair){
    boolean r = false;
    if((pair.i1==i1)&&(pair.i2==i2))  r = true;
    return r;
  }
  public String getId(){
    String r = null;
    if(i1>i2)
      r = ""+i2+"_"+i1;
    else
      r = ""+i1+"_"+i2;
    return r;
  }
  public static PairInt getFromId(String id){
    StringTokenizer st = new StringTokenizer(id,"_");
    int i1 = Integer.parseInt(st.nextToken());
    int i2 = Integer.parseInt(st.nextToken());
    return new PairInt(i1,i2);
  }

}