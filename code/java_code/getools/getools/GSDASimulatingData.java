package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

import vdaoengine.analysis.PCAMethod;

public class GSDASimulatingData extends GSDA{
  public static void main(String[] args) {
    try{

      String geneNames[] = {"PRKCB1","TP53","CAV1","ETS2","FGFR3","VEGFB","RAMP1","FGFR2","GAB2","CAV2","WIPI1"};
      //int sizes[] = {10,15,20,30,50,100,300,700};
      //int sizes[] = {12,15,20,30,50,100,500};
      int sizes[] = {100};

      VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/gsdatest/brain_gcrma.dat");
      VDataTable vts = vdaoengine.utils.VSimpleProcedures.substituteRowsByStatistics(vt,"GeneSymbol",4);
      VDataSet vd = VSimpleProcedures.SimplyPreparedDataset(vts,-1);

      vts.makePrimaryHash("GeneSymbol");
      
      FileWriter fw = new FileWriter("c:/datas/gsdatest/simulated.gmt");
      
      for(int g=0;g<geneNames.length;g++){
    	  
    	  String geneName1 = geneNames[g];

      Vector v = (Vector)vts.tableHashPrimary.get(geneName1);
      int k = ((Integer)v.get(0)).intValue();

      float x[] = vd.getVector(k);
      float corrs[] = new float[vts.rowCount];
      float dists[] = new float[vts.rowCount];
      float corrs_pos[] = new float[vts.rowCount];
      float vars[] = new float[vts.rowCount];

      

      for(int i=0;i<vd.pointCount;i++){
        float y[] = vd.getVector(i);
        float corr = VSimpleFunctions.calcCorrelationCoeff(x,y);
        //if(Math.abs(corr)>0.9)
        //  System.out.println(vts.stringTable[i][vts.fieldNumByName("GeneSymbol")]+"\t"+corr);
        corrs[i] = Math.abs(corr);
        corrs_pos[i] = corr;
        float d = (float)VVectorCalc.Distance(x,y);
        dists[i] = d;
        vars[i] = VSimpleFunctions.calcStandardDeviation(y);
      }
      int inds_corr[] = Algorithms.SortMass(corrs);
      int inds_corr_pos[] = Algorithms.SortMass(corrs_pos);
      int inds_dist[] = Algorithms.SortMass(dists);
      int inds_vars[] = Algorithms.SortMass(vars);

      for(int i=0;i<sizes.length;i++){
        fw.write(geneName1+"_CORR_"+sizes[i]+"\tna\t");
        for(int j=0;j<sizes[i];j++){
           fw.write(vts.stringTable[inds_corr[inds_corr.length-j-1]][vts.fieldNumByName("GeneSymbol")]+"\t");
        }
        fw.write("\n");
      }
      for(int i=0;i<sizes.length;i++){
        fw.write(geneName1+"_CORRP_"+sizes[i]+"\tna\t");
        for(int j=0;j<sizes[i];j++){
           fw.write(vts.stringTable[inds_corr_pos[inds_corr_pos.length-j-1]][vts.fieldNumByName("GeneSymbol")]+"\t");
        }
        fw.write("\n");
      }
      for(int i=0;i<sizes.length;i++){
        fw.write(geneName1+"_DIST_"+sizes[i]+"\tna\t");
        for(int j=0;j<sizes[i];j++){
           fw.write(vts.stringTable[inds_dist[j]][vts.fieldNumByName("GeneSymbol")]+"\t");
        }
        fw.write("\n");
      }
      Random r = new Random();
      // skewed signatures
      for(int i=0;i<sizes.length;i++){
         fw.write(geneName1+"_SKW_"+sizes[i]+"\tna\t");
         int skewed = (int)(sizes[i]*0.1f);
         for(int j=0;j<skewed;j++)
           fw.write(vts.stringTable[inds_corr_pos[inds_corr_pos.length-j-1]][vts.fieldNumByName("GeneSymbol")]+"\t");
         for(int j=skewed+1;j<sizes[i];j++)
           fw.write(vts.stringTable[r.nextInt(vts.rowCount)][vts.fieldNumByName("GeneSymbol")]+"\t");
         fw.write("\n");
      }

      for(int kk=0;kk<100;kk++){
      for(int i=0;i<sizes.length;i++){
        fw.write("RND_"+sizes[i]+"_"+(kk+1)+"_"+(g+1)+"\tna\t");
        for(int j=0;j<sizes[i];j++){
           fw.write(vts.stringTable[r.nextInt(vts.rowCount)][vts.fieldNumByName("GeneSymbol")]+"\t");
        }
        fw.write("\n");
      }
      }
      }
      fw.close();


    }catch(Exception e){
      e.printStackTrace();
    }
  }
}