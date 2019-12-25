package vdaoengine;

import java.util.*;
import vdaoengine.data.io.*;
import vdaoengine.data.*;

public class SignaturesScript {
  public static void main(String[] args) {
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/7cluster/cu_table1.dat");
    String signRef = "*-+--+_+*-*+-";
    for(int i=0;i<vt.rowCount;i++){
      String sign = vt.stringTable[i][vt.fieldNumByName("SIGN")];
      System.out.print(vt.stringTable[i][vt.fieldNumByName("NAME")]+"\t"+vt.stringTable[i][vt.fieldNumByName("GC")]+"\t"+sign+"\t"+DistanceBetweenSignatures(sign,signRef)+"\t"+DistanceBetweenSignaturesWithoutThirdBase(sign,signRef)+"\n");
    }
  }

public static int DistanceBetweenSignatures(String sign1, String sign2){
  int dist = 0;
  for(int i=0;i<13;i++){
    char c1 = sign1.charAt(i);
    char c2 = sign2.charAt(i);
    if(c1!=c2){
      if((c1=='+')&&(c2=='-')) dist+=2;
      if((c1=='-')&&(c2=='+')) dist+=2;
      if((c1=='+')&&(c2=='*')) dist+=1;
      if((c1=='-')&&(c2=='*')) dist+=1;
      if((c1=='*')&&(c2=='+')) dist+=1;
      if((c1=='*')&&(c2=='-')) dist+=1;
    }
  }
  return dist;
}

public static int DistanceBetweenSignaturesWithoutThirdBase(String sign1, String sign2){
  int dist = 0;
  for(int i=0;i<13;i++){
    char c1 = sign1.charAt(i);
    char c2 = sign2.charAt(i);
    if(c1!=c2)if(i!=2)if(i!=5)if(i!=9)if(i!=12){
      if((c1=='+')&&(c2=='-')) dist+=2;
      if((c1=='-')&&(c2=='+')) dist+=2;
      if((c1=='+')&&(c2=='*')) dist+=1;
      if((c1=='-')&&(c2=='*')) dist+=1;
      if((c1=='*')&&(c2=='+')) dist+=1;
      if((c1=='*')&&(c2=='-')) dist+=1;
    }
  }
  return dist;
}


}