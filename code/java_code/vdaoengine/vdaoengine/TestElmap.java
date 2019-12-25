package vdaoengine;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.elmap.*;
import vdaoengine.analysis.*;

public class TestElmap {



  public static void main(String[] args) throws IOException {


    /*try{
    int num = 500;
    FileWriter fw = new FileWriter("tree22.dat");
    fw.write("3 "+num+"\r\n");
    fw.write("N1 FLOAT\r\n");
    fw.write("N2 FLOAT\r\n");
    fw.write("N3 FLOAT  \r\n");
    Random r = new Random();
    float noise = 0.1f;
    for(int i=0;i<num;i++){
      float x = 0f;
      float y = r.nextFloat()*2-1;
      float z = 0f;
      if(y<-0.2) { x = (r.nextFloat()-0.5f)*noise; }
      if(y>-0.2){
        if(r.nextFloat()>0.5f) {
            x = (y+0.2f)+(r.nextFloat()-0.5f)*noise*2f;
            if(y>0.1f){
              if(r.nextFloat()>0.5f){
                if(r.nextFloat()>0.5f){
                  x = y + 0.15f + (r.nextFloat()-0.5f)*noise*2f;
                  y = 0.1f + (r.nextFloat()-0.5f)*noise*1f;
                }else{
                  x = 0.25f + (r.nextFloat()-0.5f)*noise*2f;
                  y = y + 0.15f + (r.nextFloat()-0.5f)*noise*2f;
                }
              }
            }
        }
        else
        {
          if(y>0.5f){
            if(r.nextFloat()>0.5f)
              x = - (y+0.2f)+(r.nextFloat()-0.5f)*noise*2+(y-0.5f)*2f;
            else
              x = - (y+0.2f)+(r.nextFloat()-0.5f)*noise*2-(y-0.5f)*0.2f;
          }else
            x = - (y+0.2f)+(r.nextFloat()-0.5f)*noise*2;
        }
      }
    //z = 10*x*x*y*y*y+(r.nextFloat()-0.5f)*noise*10;
    z = 0f;
    fw.write(x+"\t"+y+"\t"+z+"\r\n");
    }
    fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }*/

    System.out.println("Testing elmap package...");

    //String project = "tree22";
    //String project = "iris";
    //String project = "ec_out7";
    String project = "d3ef3000_t_pca";
    //String project = "dna1";

    System.out.println("Creating dataset "+project+"...");
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(project+".dat");
    VDataSet ds = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("Number of iterations?");
    //String line1 = in.readLine();
    int it = 70;

    System.out.println("Lambda?");
    //String line2 = in.readLine();
    //double lm = 0.015;

    double ep = 0.05;
    double rp = 0.1;

    System.out.println("Extension factor?");
    //String line3 = in.readLine();
    float le = 0.1f;

    System.out.println("Computing elmap...");

    Date cld = new Date();
    Grid gr = ElasticTreeAlgorithm.computeElasticTree(ds,"elmap.ini",9,it,ep,rp,le);
    System.out.println("Time: "+(int)((new Date()).getTime()-cld.getTime())+" ms");

    // Saves projections on the elastic map
    System.out.println("Saving projections...");
    gr.saveProjections("projt",ds,gr.PROJECTION_CLOSEST_NODE);

    // Saves .vem file for VidaExpert
    System.out.println("Saving .vem file...");
    gr.saveToFile(project+".eltree.vem",project);

  }
}