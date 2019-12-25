package getools.scripts;

import java.io.*;
import java.util.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.cluster.*;

public class elmapCluster {
  public static void main(String[] args) {
    try{
    //VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("tsc2_1nf.dat");
    //VDataTable vt2 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("tsc2_2nf.dat");
    //VDataTable map1 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("elmapclust1212_1nf_c2.txt",false,"\t ");
    //VDataTable map2 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("elmapclust1212_2nf_c2.txt",false,"\t ");
    /*VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("time_ser_chrono_1nf.dat");
    VDataTable vt2 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("time_ser_chrono_2nf.dat");
    VDataTable map1 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("elmapclust1212_1nf.txt",false,"\t ");
    VDataTable map2 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("elmapclust1212_2nf.txt",false,"\t ");*/

   String prefix = "c:/datas/radulescu/nuclear_receptors_data/";
    	
   String annotfile = "c:/datas/HG_U133_Plus_2_annot.csv";
   VDataTable annt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(annotfile,true,",\t");
   VTableQuery qr = new VTableQuery();
   qr.table = annt;
   qr.hashTable("Probe");

    int ngrid = 12;

    //VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("tsc1_1nfca.dat");

    VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+"nuclear_transposed.dat");
    //VDataTable vt2 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/sebastian/tsc1_1n_1percent.dat");
    VDataTable map1 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(prefix+"elmapclust1212.txt",false,"\t");
    //VDataTable map2 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/sebastian/map1perc_lowvar.txt",false,"\t ");

    /*VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("tsc1_1nfca.dat");
    VDataTable vt2 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("tsc1_1nfca.dat");
    VDataTable map1 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("selcor2_11_0_85_elmap.txt",false,"\t ");
    VDataTable map2 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("selcor2_11_0_85_elmap.txt",false,"\t ");*/

    //VDataSet VD2 = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt2,-1);

    VDataTable map = map1;
    VDataTable vt = vt1;

    VDataSet mapd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(map,-1);

    VDataSet VD = new VDataSet();
    VTableSelector VS = new VTableSelector();
    //VS.selectLastNumericalColumns(vt,28);
    VD.loadFromDataTable(vt,VS);
    VD.table = vt;
    
    VD.calcStatistics();
    VNormalization N = new VNormalization(VD.simpleStatistics);
    for(int i=0;i<VS.selectedColumns.length;i++){
    	  N.setType(i,0);
    }
    VD.addToPreProcess(N);
    VD.preProcessData();
    
    
    PCAMethod pca = new PCAMethod();
    pca.setDataSet(VD);
    pca.calcBasis(VD.coordCount);
    //pca.setDataSet(mapd);
    VDataSet vdp = pca.getProjectedDataset();
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vdp,prefix+"pcadecomp.dat");

    VKMeansClusterization km = new VKMeansClusterization(map.rowCount);
    for(int i=0;i<map.rowCount;i++){
      double f[] = new double[map.colCount];
      for(int j=0;j<map.colCount;j++)
         f[j]=Float.parseFloat(map.stringTable[i][j]);
      km.centroids.add(f);
    }
    km.calculateClassListsFromCentroids(VD);
    
    FileWriter fw = new FileWriter(prefix+"clustsizes.txt");
    for(int i=0;i<map.rowCount;i++){
      int l[] = (int[])km.classLists.elementAt(i);
      fw.write(l.length+"\r\n");
    }
    fw.close();
    
    fw = new FileWriter(prefix+"clustindices.txt");
    int maxlen = -1;
    for(int i=0;i<map.rowCount;i++){
      int l[] = (int[])km.classLists.elementAt(i);
      if(l.length>maxlen) maxlen=l.length;
    }
    for(int i=0;i<map.rowCount;i++){
        int l[] = (int[])km.classLists.elementAt(i);
        for(int j=0;j<l.length;j++)
        	fw.write(l[j]+"\t");
        for(int j=l.length;j<maxlen;j++)
        	fw.write("-1\t");
        fw.write("\n");
      }
    fw.close();
    
    
    
    fw = new FileWriter(prefix+"cluster_lists.txt");
    FileWriter fws = new FileWriter(prefix+"cluster_lists1.txt");
    for(int i=0;i<km.classLists.size();i++){
      fws.write(clusterCode(i,ngrid)+"\t");
      int lst[] = (int[])km.classLists.elementAt(i);
      for(int j=0;j<lst.length;j++){
        fw.write(VD.table.stringTable[lst[j]][0]+"\t");
        fws.write(VD.table.stringTable[lst[j]][0]+"\t");
      }
      fw.write("\r\n");
      fws.write("\r\n");
    }
    fw.close();
    fws.close();

    //VD = VD2;
    km.calculateCentroidsFromClassLists(VD,false);

    fw = new FileWriter(prefix+"pcadecomp_av.txt");
    FileWriter fw1 = new FileWriter(prefix+"centr_av.txt");
    FileWriter fw2 = new FileWriter(prefix+"centr_std.txt");
    FileWriter fw3 = new FileWriter(prefix+"centr_min.txt");
    FileWriter fw4 = new FileWriter(prefix+"centr_max.txt");
    for(int i=0;i<km.centroids.size();i++){

      String id = clusterCode(i,ngrid);
      /*fw1.write(id+"\t");
      fw2.write(id+"\t");
      fw3.write(id+"\t");
      fw4.write(id+"\t");*/

      double centr[] = (double[])km.centroids.elementAt(i);
      for(int j=0;j<centr.length;j++)
        fw1.write(centr[j]+"\t");
      fw1.write("\r\n");
          int lst[] = (int[])km.classLists.elementAt(i);
          if(lst.length>0){
          VStatistics vst = new VStatistics(centr.length);
          vst.initialize();
          for(int j=0;j<lst.length;j++){
            vst.addNewPoint(VD.getVector(lst[j]));
          }
          vst.calculate();
            for(int kk=0;kk<VD.coordCount;kk++)
              fw2.write(vst.getStdDev(kk)+"\t");
            fw2.write("\r\n");
            for(int kk=0;kk<VD.coordCount;kk++)
              fw3.write(vst.getMin(kk)+"\t");
            fw3.write("\r\n");
            for(int kk=0;kk<VD.coordCount;kk++)
              fw4.write(vst.getMax(kk)+"\t");
            fw4.write("\r\n");
          }else{
            for(int kk=0;kk<VD.coordCount;kk++)
              fw2.write("0\t");
            fw2.write("\r\n");
            for(int kk=0;kk<VD.coordCount;kk++)
              fw3.write("0\t");
            fw3.write("\r\n");
            for(int kk=0;kk<VD.coordCount;kk++)
              fw4.write("0\t");
            fw4.write("\r\n");
          }

      centr = pca.projectionFunction(centr);
      for(int j=0;j<centr.length;j++)
        fw.write(centr[j]+"\t");
      fw.write("\r\n");
    }
    fw.close();
    fw1.close();
    fw2.close();
    fw3.close();
    fw4.close();

    /*FileWriter fwmd = new FileWriter(prefix+"map.dat");
    int gene_id = vt1.fieldNumByName("GeneSymbol");
    int probe_id = vt1.fieldNumByName("PS");
    int chip_id = vt1.fieldNumByName("CHIP");
    fwmd.write((map.colCount+4)+" "+map.rowCount+"\r\n");
    fwmd.write("ID STRING\r\n");
    fwmd.write("GENES STRING\r\n");
    fwmd.write("NUMBER STRING\r\n");
    for(int i=0;i<map.colCount;i++)
        fwmd.write("T"+(i+1)+" FLOAT\r\n");
    fwmd.write("PS STRING\r\n");
    for(int i=0;i<ngrid;i++){
        for(int j=0;j<ngrid;j++){
          String id = ""+(i+1);
          if(id.length()==1) id="0"+id;
          String jd = ""+(j+1);
          if(jd.length()==1) jd="0"+jd;
          id = id + "_" + jd;
          fwmd.write(id+" ");
          fwmd.write("\"_\" ");
          int ind = i*ngrid+(ngrid-1-j);
            int l[] = (int[])km.classLists.elementAt(ind);
            fwmd.write(l.length+" ");
          float f[] = mapd.getVector(ind);
          for(int k=0;k<map.colCount;k++){
             fwmd.write(f[k]+" ");
          }
          fwmd.write("\"_\" ");
          fwmd.write("\r\n");
        }
      }
    fwmd.close();

    FileWriter fwmc = new FileWriter(prefix+"mapc.xls");
    VDataTable vmt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(prefix+"map.dat");
    VDataSet vm = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vmt,10);
    fwmc.write("Group\t");
    for(int i=0;i<vm.pointCount;i++)
       fwmc.write(vm.table.stringTable[i][vmt.fieldNumByName("ID")]+"\t");
    fwmc.write("\r\n");
    for(int i=0;i<vm.pointCount;i++){
       fwmc.write(vm.table.stringTable[i][vmt.fieldNumByName("ID")]+"\t");
       for(int j=0;j<vm.pointCount;j++){
         float fi[] = vm.getVector(i);
         float fj[] = vm.getVector(j);
         float cf = vdaoengine.utils.VSimpleFunctions.calcCorrelationCoeff(fi,fj);
         //vdaoengine.utils.VVectorCalc.distanceType = 0;
         //float cf = (float)vdaoengine.utils.VVectorCalc.Distance(fi,fj);

         if(i<j) fwmc.write(" "+"\t");
         else{
         if(cf>0.85)
            fwmc.write(cf+"\t");
         else
            fwmc.write(" "+"\t");
         //fwmc.write(cf+"\t");
         }
       }
    fwmc.write("\r\n");
    }
    fwmc.close();

    Vector genes = new Vector();
    Vector probes = new Vector();
    Vector clusts = new Vector();
    int gn_id = annt.fieldNumByName("GeneSymbol");
    for(int i=0;i<vt1.rowCount;i++){
      float f[] = VD.getVector(i);
      int k = km.calculateClosestCentroid(f);
      String id = clusterCode(k,ngrid);
      int kk = qr.queryHash(vt1.stringTable[i][probe_id]);
      probes.add(vt1.stringTable[i][probe_id]);
      genes.add(annt.stringTable[kk][gn_id]);
      clusts.add(new Integer(k));
    }

    FileWriter fwa = new FileWriter(prefix+"genes.xls");
    for(int i=0;i<genes.size();i++){
      String gs = (String)genes.elementAt(i);
      String ps = (String)probes.elementAt(i);
      int cl = ((Integer)clusts.elementAt(i)).intValue();
      fwa.write(ps+"\t"+gs+"\t"+clusterCode(cl,ngrid)+"\r\n");
    }
    fwa.close();*/


    }catch(Exception e){ e.printStackTrace();}
  }

  public static String clusterCode(int i,int ngrid){
    int ir = (int)(1f*i/ngrid);
    int ic = -i+ir*ngrid+ngrid-1;
    String id = ""+(ir+1);
    if(id.length()==1) id="0"+id;
    String jd = ""+(ic+1);
    if(jd.length()==1) jd="0"+jd;
    id = id + "_" + jd;
    return id;
  }
}