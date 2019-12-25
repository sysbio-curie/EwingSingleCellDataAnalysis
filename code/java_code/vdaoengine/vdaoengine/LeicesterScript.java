package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

import vdaoengine.analysis.elmap.*;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

public class LeicesterScript {

  public LeicesterScript() {
  }
  public static void main(String[] args) {

    try{

    	
    VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/BreastCancer/Wang/wangn5000.dat");
    vtt = vtt.transposeTable("CHIP");
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt, "C:/Datas/BreastCancer/Wang/wangn5000_t.dat");
    System.exit(0);

    // Table Preparation

    //String prefix = "d3ef3000";
    //String idfield = "ID";

    //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
    //VDataTable vtt = vt.transposeTable(idfield);
    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt,prefix+"_t.dat");

    //int genesToselect = 3000;
    //VDataTable vtr = filterByVariation(vt,genesToselect,false);
    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtr,prefix+genesToselect+".dat");

    //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+"_t.dat");

    //VDataTable vtrPCA = PCAtable(vt,false);
    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtrPCA,prefix+genesToselect+"samplePCA.dat");


    //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("iris_gap.dat");
    //VDataTable vtrPCA = PCAtable(vt,false);
    //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtrPCA,"iris_PCA.dat");


    //String prefix = "iris_gap";
    //String prefix = "d3ef3000";
    //String prefix = "d2f3000_t";
    //String prefix = "d3ef3000_t";

    //String prefix = "d13000_t";
    //String prefix = "iris";
    //String prefix = "d2f3000_t";
    String prefix = "d3ef3000_t";

    /*ElmapAlgorithm ela = new ElmapAlgorithm();
    SimpleRectangularGrid grt = new SimpleRectangularGrid();
    grt.loadFromFile(prefix+".vem");
    for(int i=0;i<grt.Nodes.length;i++)
      for(int j=0;j<grt.dimension;j++)
        grt.Nodes[i][j]/=32;
    grt.saveToFile(prefix+".div32.vem",prefix);*/

    VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
    VDataTable vtrPCA1 = PCAMethod.PCAtable(vt1,50,true);
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtrPCA1,prefix+"_pca.dat");

    if(true)
      throw new Exception();


    // Computing elastic map
    /*VDataTable vtm = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
    VDataSet vdm = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vtm,-1);

    String settings = "elmap.ini";
    settings = vdaoengine.utils.IO.loadString(settings);
    ElmapAlgorithm ela = new ElmapAlgorithm();
    ela.setData(vdm);
    ela.readIniFileStr(settings,8);
    ela.computeElasticGrid();
    ela.grid.saveToFile(prefix+".vem",prefix);

    //ElmapAlgorithm ela = new ElmapAlgorithm();
    //SimpleRectangularGrid grt = new SimpleRectangularGrid();
    //grt.loadFromFile(prefix+".vem");
    //ela.grid = grt;
    //ela.grid.MakeNodesCopy();
    //ela.setData(vdm);

    ElmapProjection elmap = new ElmapProjection();
    elmap.setElmap(ela);
    VDataSet vdmp = elmap.getProjectedDataset();
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vdmp,prefix+".elmapproj");*/

    /*VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
    VDataSet vdt = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vtt,-1);
    SimpleRectangularGrid grt = new SimpleRectangularGrid();
    grt.loadFromFile(prefix+".vem");
    ElmapProjection elmapprojectiont = new ElmapProjection();
    ElmapAlgorithm elmapt = new ElmapAlgorithm();
    elmapt.grid = grt;
    elmapprojectiont.setElmap(elmapt);
    elmapprojectiont.elmap.grid.MakeNodesCopy();
    elmapprojectiont.elmap.taxons = elmapprojectiont.elmap.grid.calcTaxons(vdt);

    //elmapprojectiont = elmap;
    float mseelmapt = elmapprojectiont.calculateMSEToProjection(vdt);
    System.out.println(mseelmapt+"\t"+elmapprojectiont.elmap.grid.calcMSE(vdt,elmapprojectiont.elmap.taxons));*/




    /*VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    VDataSet vd1 = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    VDistanceMatrix mnd = new VDistanceMatrix();
    mnd.calculatePairs = true;
    mnd.calculateMatrix(vd);
    NPCAMethod npca = new NPCAMethod();
    npca.calc(mnd);
    FileWriter fw = new FileWriter(prefix+".vev");
    for(int i=0;i<npca.componentPairs.size();i++){
      PairInt pi = (PairInt)npca.componentPairs.get(i);
      System.out.println(pi.i1+"\t"+pi.i2);
      for(int j=0;j<vd1.coordCount;j++){
        fw.write(vd1.getVector(pi.i1)[j]+" ");
      }
      fw.write("\r\n");
      for(int j=0;j<vd1.coordCount;j++){
          fw.write(vd1.getVector(pi.i2)[j]+" ");
      }
      fw.write("\r\n");
    }
    fw.close();*/

    //Tables for analysis
    int PCAComponentNumber = 20;
    Vector componentsToCalculate = new Vector();
    componentsToCalculate.add(new Integer(1));
    componentsToCalculate.add(new Integer(2));
    componentsToCalculate.add(new Integer(3));
    componentsToCalculate.add(new Integer(4));
    componentsToCalculate.add(new Integer(5));
    componentsToCalculate.add(new Integer(10));
    componentsToCalculate.add(new Integer(15));
    componentsToCalculate.add(new Integer(20));
    //componentsToCalculate.add(new Integer(20));
    //componentsToCalculate.add(new Integer(40));
    //componentsToCalculate.add(new Integer(50));
    //componentsToCalculate.add(new Integer(100));
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".dat");
    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    System.out.println("Calculating PCA...");
    VDataSet pcad[] = new VDataSet[componentsToCalculate.size()];
    VDataTable vtrPCA = null;
    if(!(new File(prefix+".pca"+PCAComponentNumber+".dat")).exists()){
      vtrPCA = PCAMethod.PCAtable(vt,PCAComponentNumber,true);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtrPCA,prefix+".pca"+PCAComponentNumber+".dat");
    }
    else{
      vtrPCA = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".pca"+PCAComponentNumber+".dat");
    }

    for(int i=0;i<componentsToCalculate.size();i++){
       int k = ((Integer)componentsToCalculate.get(i)).intValue();
       pcad[i] = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vtrPCA,k);
    }

    //Sheppard plots
    Vector dms = new Vector();
    VDistanceMatrix mnd = new VDistanceMatrix();
    mnd.calculatePairs = true;
    mnd.calculateMatrix(vd);
    NPCAMethod npca = new NPCAMethod();
    System.out.println("Calculating NPCA...");
      npca.calc(mnd);
      //npca.addAllDistances(mnd);

      VDataTable elmapprojtab = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+".elmapproj");
      VDataSet elmapproj = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(elmapprojtab,-1);

      VDistanceMatrix melmap = new VDistanceMatrix();
      melmap.calculateMatrix(elmapproj);

    // Now Testing
    //melmap.calculateNeighbours(10);
    /*int kk = vd.table.fieldNumByName("D1");
    int kk1 = vd.table.fieldNumByName("NAME");
    System.out.println(vd.table.stringTable[39][kk1]);
    int nn[] = (int[])melmap.calculatePointNeighbours(39,10);
    for(int i=0;i<10;i++){
      System.out.print(nn[i]+":"+vd.table.stringTable[nn[i]][kk1]+"\t");
    }
    System.out.println();*/

    dms.add(mnd); dms.add(melmap);
    System.out.println("Calculating Distance matrices...");
    for(int i=0;i<pcad.length;i++){
      VDistanceMatrix mpc = new VDistanceMatrix();
      mpc.calculateMatrix(pcad[i]);
      dms.add(mpc);
    }

    // Writing down sheppard file
    FileWriter fw = new FileWriter(prefix+".shepp.xls");
    fw.write("ND\t");
    fw.write("ELMAP\t");
    for(int i=0;i<pcad.length;i++)
        fw.write("PC"+(((Integer)componentsToCalculate.get(i)).intValue())+"\t"); fw.write("\n");

    float shepp[][] = VDistanceMatrix.getDataForSheppardPlot(dms,npca.componentPairs);
    for(int i=0;i<shepp.length;i++){
      for(int j=0;j<dms.size();j++){
        fw.write(shepp[i][j]+"\t");
      }
      fw.write("\n");
    }
    fw.close();
    // Writing down relations file
    fw = new FileWriter(prefix+".shepp.r.xls");
    fw.write("ND\t");
    fw.write("ELMAP\t");
    for(int i=0;i<pcad.length;i++)
        fw.write("PC"+(((Integer)componentsToCalculate.get(i)).intValue())+"\t"); fw.write("\n");

    for(int i=0;i<shepp.length;i++){
      for(int j=0;j<dms.size();j++){
        fw.write(shepp[i][j]/shepp[i][0]*shepp[0][0]/shepp[0][j]+"\t");
      }
      fw.write("\n");
    }
    fw.close();

    // Computing distance correlations
    System.out.println("Distance correlations:");
    System.out.print("ELMAP\t");
    for(int i=2;i<dms.size();i++) System.out.print("PC"+((Integer)componentsToCalculate.get(i-2)).intValue()+"\t");
    System.out.print("\n");
    float shnd[] = new float[npca.componentPairs.size()];
    for(int j=0;j<npca.componentPairs.size();j++)
      shnd[j] = shepp[j][0];
    for(int i=1;i<dms.size();i++){
        float sh[] = new float[npca.componentPairs.size()];
        for(int j=0;j<npca.componentPairs.size();j++)
          sh[j] = shepp[j][i];
        System.out.print(VSimpleFunctions.calcCorrelationCoeff(shnd,sh)+"\t");
    }
    System.out.println();

    // Computing distance Spearman correlations
    System.out.println("Distance Spearman correlations:");
    System.out.print("ELMAP\t");
    for(int i=2;i<dms.size();i++) System.out.print("PC"+((Integer)componentsToCalculate.get(i-2)).intValue()+"\t");
    System.out.print("\n");
    shnd = new float[npca.componentPairs.size()];
    for(int j=0;j<npca.componentPairs.size();j++)
      shnd[j] = shepp[j][0];
    for(int i=1;i<dms.size();i++){
        float sh[] = new float[npca.componentPairs.size()];
        for(int j=0;j<npca.componentPairs.size();j++)
          sh[j] = shepp[j][i];
        System.out.print(VSimpleFunctions.calcSpearmanCorrelationCoeff(shnd,sh)+"\t");
    }
    System.out.println();


    // Computing MSEs
    /*PCAMethod pcam = new PCAMethod();
    pcam.setDataSet(vd); pcam.calcBasis(PCAComponentNumber);

    System.out.println("MSEs:");
    System.out.print("ELMAP\t");
    for(int i=2;i<dms.size();i++) System.out.print("PC"+((Integer)componentsToCalculate.get(i-2)).intValue()+"\t");
    System.out.print("\n");
    SimpleRectangularGrid gr = new SimpleRectangularGrid();
    gr.loadFromFile(prefix+".vem");
    ElmapProjection elmapprojection = new ElmapProjection();
    ElmapAlgorithm elmape = new ElmapAlgorithm();
    elmape.grid = gr;
    elmapprojection.setElmap(elmape);
    elmapprojection.setDataSet(vd);
    float mseelmap = elmapprojection.calculateMSEToProjection(vd);
    System.out.print(mseelmap+"\t");
    for(int i=0;i<componentsToCalculate.size();i++){
       int k = ((Integer)componentsToCalculate.get(i)).intValue();
       pcam.getBasis().numberOfBasisVectors = k;
       float mse = pcam.calculateMSEToProjection(vd);
       System.out.print(mse+"\t");
    }
    System.out.println();*/

    // Computing local neighbourhood preservation
    int numberOfNeighbours = 5;
    VStatistics vst = mnd.calculateNeighbourhoodPreservation(dms,mnd,numberOfNeighbours);
    System.out.println("Local neighbourhood preservation");
    System.out.print("\tND\tELMAP\t");
    for(int i=2;i<dms.size();i++) System.out.print("PC"+((Integer)componentsToCalculate.get(i-2)).intValue()+"\t");
    System.out.print("RANDOM\n");
    System.out.println(vst.toString());

    /*for(int i=0;i<mnd.numberOfPoints;i++){
      float f[] = (float[])vst.points.get(i);
      for(int j=0;j<f.length;j++){
        System.out.print(f[j]+"\t");
      }
      System.out.println();
    }*/
    // and calculate for different numberOfNeighbours
    /*for(int i=1;i<20;i++){
       VStatistics vsti = mnd.calculateNeighbourhoodPreservation(dms,mnd,i);
       System.out.print(""+i+":\t");
       for(int j=0;j<vst.dimension;j++)
         System.out.print(""+vsti.means[j]+"\t");
       System.out.print("\n");
    }*/

    // Computing local class neighbourhood
    numberOfNeighbours = 2;
    //String classname = "IRIS-SETOS";
    String classname = "D1";
    VClassifier Cl = VSimpleProcedures.ClassifyDataSetByField(vd,classname,VClassifier.CHANGE_FILLCOLOR);
    Vector clv = Cl.getClassVector();
    System.out.print("\tND\tELMAP\t");
    for(int i=2;i<dms.size();i++) System.out.print("PC"+((Integer)componentsToCalculate.get(i-2)).intValue()+"\t");
    System.out.print("RANDOM\n");
    for(int i=0;i<clv.size();i++){
      VDataClass dcl = (VDataClass)clv.get(i);
      if(dcl.getIDset().size()>1){
      System.out.println("Class "+dcl.shortname+"("+dcl.getIDset().size()+"):");
      VStatistics vstc = VDistanceMatrix.calculateClassNeighbourhood(dms,mnd,Math.min(numberOfNeighbours,dcl.getIDset().size()-1),dcl);
      System.out.println(vstc.toString());

    if(dcl.shortname.equals("BLADDER"))
    for(int ii=0;ii<dcl.getIDset().size();ii++){
      float f[] = (float[])vstc.points.get(ii);
      for(int jj=0;jj<f.length;jj++){
        System.out.print(f[jj]+"\t");
      }
      System.out.println();
    }
      }

    }

    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static VDataTable filterByVariation(VDataTable vt, int numOfGenes, boolean doScaling){
    float var[] = new float[vt.rowCount];
    VDataTable vtr = new VDataTable();
    vtr.copyHeader(vt);
    vtr.rowCount = numOfGenes;
    vtr.colCount = vt.colCount;
    vtr.stringTable = new String[numOfGenes][vt.colCount];
    VDataSet vd = null;
    if(doScaling)
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDataset(vt,-1);
    else
      vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
    for(int i=0;i<vt.rowCount;i++){
      float v[] = vd.getVector(i);
      float av = 0f;
      int nn = 0;
      for(int k=0;k<v.length;k++){
        if(!Float.isNaN(v[k])){
          av+=v[k];
          nn++;
      }
      }
      av/=nn;
      for(int k=0;k<v.length;k++)
        v[k]-=av;
      var[i] = (float)norm(v);
    }
    int ord[] = Sort(var);
    for(int i=0;i<numOfGenes;i++){
      int k = ord[i];
      vtr.stringTable[i] = vt.stringTable[k];
    }
    return vtr;
  }

  public static int[] Sort(float cais[]){
   int res[]=new int[cais.length];
   for (int i = 0; i < res.length; i++) res[i]=i;

   int i,j,k,inc,n=cais.length;
   float v;

   inc=1;
   do {
           inc *= 3;
           inc++;
   } while (inc <= n);

   do {
           inc /= 3;
           for (i=inc+1;i<=n;i++) {
                   v=cais[res[i-1]];
                   j=i;
                   k=res[i-1];
                   while (cais[res[j-inc-1]]<v) {
                           //cais[j]=cais[j-inc];
                           res[j-1]=res[j-inc-1];
                           j -= inc;
                           if (j <= inc) break;
                   }
                   //cais[j]=v;
                   res[j-1]=k;
           }
   } while (inc > 0);

   return res;
   }

   public static double norm( float[] data )
       {
        double d = 0;
        for(int i=0;i<data.length;i++){
            float val = data[i];
            if(!Float.isNaN(val))
              d+=val*val;
        }
        return Math.sqrt(d);
       }



}