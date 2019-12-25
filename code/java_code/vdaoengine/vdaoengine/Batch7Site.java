package vdaoengine;

import java.util.*;
import java.io.*;

public class Batch7Site {
  public static void main(String[] args) {

    try{

    File f = new File("./");
    File fa[] = f.listFiles();
    int k=0;
    for(int i=0;i<fa.length;i++) if(fa[i].isDirectory())
    {

      k++;
      System.out.println(k+": "+fa[i].getName());
            File ff = new File(fa[i].getName()+"/out.dat");
            if(ff.exists()){
              GenesCU.fileName = ff.getAbsolutePath();
              GenesCU.imgName = fa[i].getName()+"_cu";
              GenesCU.main(new String[0]);
            }
            /*ff = new File(fa[i].getName()+"/out7.dat");
            if(ff.exists()){
              SevenClusters.fileName = ff.getAbsolutePath();
              SevenClusters.imgName = fa[i].getName()+"_sc";
              SevenClusters.main(new String[0]);
            }
            ff = new File(fa[i].getName()+"/codonusage");
            if(ff.exists()){
              CodonUsageGraph.fileName = ff.getAbsolutePath();
              CodonUsageGraph.imgName = fa[i].getName()+"_sc";
              CodonUsageGraph.main(new String[0]);
            }*/

    }
    }catch(Exception e){e.printStackTrace();}


  }
}