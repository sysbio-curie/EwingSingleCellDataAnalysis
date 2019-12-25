package vdaoengine;

import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.*;
import vdaoengine.analysis.elmap.*;
import vdaoengine.analysis.cluster.*;

public class test {

  public test() {
  }
  public static void main(String[] args) {
    try{
    	
    	/*for(float score=0;score<=5;score+=0.1f){
    		float pval = (float)vdaoengine.utils.VSimpleFunctions.ttest((double)score, 30);
    		System.out.println(score+"\t"+pval);
    	}*/
    	//float x1[] = {1,2,3,4,5,6, 7, 8};
    	//float x2[] = {7,8,9,10,11,12};
    	float x1[] = {-0.1767f,-0.2155f,-0.1797f,-0.1434f,-0.2252f,-0.2317f,-0.1164f,-0.1196f,-0.0390f,-0.0782f,-0.1546f,-0.0734f};
    	float x2[] = {0.2137f, 0.1519f,-0.0400f, 0.2420f, 0.0708f, 0.0584f,-0.0150f, 0.1396f,-0.0342f,
    			0.1823f, 0.1405f,-0.0764f, 0.2338f, 0.0279f,-0.0775f, 0.0132f,-0.0645f, 0.1456f,
    			0.2459f,-0.0455f,-0.1219f, 0.0881f,-0.0008f, 0.2217f, 0.0889f, 0.0380f, 0.1550f,
    			0.2105f,-0.0503f, 0.1023f,-0.1317f,-0.0414f, 0.0304f, 0.2080f,-0.1311f, 0.0208f,
    			-0.0882f, 0.0428f, 0.0707f, 0.0750f, 0.1110f, 0.1161f};
    	
    	Vector set1 = new Vector();
    	Vector set2 = new Vector();
    	VStatistics stat1 = new VStatistics(1); 
    	VStatistics stat2 = new VStatistics(1);
    	for(int i=0;i<x1.length;i++) { set1.add(new Float(x1[i])); float x[] = new float[1]; x[0]=x1[i]; stat1.addNewPoint(x); }
    	for(int i=0;i<x2.length;i++) { set2.add(new Float(x2[i])); float x[] = new float[1]; x[0]=x2[i]; stat2.addNewPoint(x); }
    	//double d = vdaoengine.utils.VSimpleFunctions.calcTTest(set1,set2);
    	Vector<Integer> dfvec = new Vector<Integer>();
    	double d = vdaoengine.utils.VSimpleFunctions.calcTTest(set1,set2,false,dfvec);
    	stat1.calculate();
    	stat2.calculate();
    	int df = set1.size()+set2.size()-2;
    	df = dfvec.get(0).intValue();
    	System.out.println(df);
    	float pval = (float)vdaoengine.utils.VSimpleFunctions.ttest(d, df);    	
    	System.out.println("Stdev1 = "+stat1.getStdDev(0));
    	System.out.println("Stdev2 = "+stat2.getStdDev(0));    	
    	System.out.println("Std = "+Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)/stat1.nums[0]+stat2.getStdDev(0)*stat2.getStdDev(0)/stat2.nums[0]));
    	System.out.println("TTest = "+d);
    	System.out.println("Pval = "+pval);    	
    	
    	System.exit(0);

    	VDataTable vtt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/docs/mypapers/Jtheorbiol/species_names_new1.txt", true, "\t");
    	String s = Utils.loadString("c:/docs/mypapers/JtheorBiol/model_reduction1_az3.tex");
    	for(int i=0;i<vtt.rowCount;i++){
    		String id = vtt.stringTable[i][0];
    		String name = vtt.stringTable[i][1];
    		if(!id.equals(name)){
    			s = Utils.replaceString(s, id, name);
    		}
    	}
    	
    	FileWriter fw = new FileWriter("c:/docs/mypapers/JtheorBiol/model_reduction1_az4.tex");
    	fw.write(s);
    	fw.close();
    	
    	System.exit(0);

    	
      VDataTable rnk1 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/kinetics/fit/inhib1.score.txt",false,"\t");
      VDataTable rnk2 = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/kinetics/fit/070307/inhib1score.txt",false,"\t");
      VDataTable vtm = VSimpleProcedures.MergeTables(rnk1,"PS",rnk2,"PS","NA");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtm,"c:/datas/ewing/kinetics/fit/070307/compscores.dat");


      /*VDataTable vt1 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kinetics/geneviewer/tsc2_1n.dat");
      VDataTable vt2 = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kinetics/geneviewer/tsc2_2n.dat");
      VDataTable vt3 = vdaoengine.utils.VSimpleProcedures.MergeTables(vt1,"CHIP",vt2,"CHIP","@");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt3,"c:/datas/ewing/kinetics/geneviewer/tsc2n.dat");

      VDataTable vta = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/install/systemsbiology/gsea/annotations/HG_U133_Plus_2.chip",true,"\t");
      VDataTable vt3a = vdaoengine.utils.VSimpleProcedures.MergeTables(vt3,"CHIP",vta,"ProbeSetID","---");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt3a,"c:/datas/ewing/kinetics/geneviewer/tsc2na.dat");

      //VDataTable vt3a = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kinetics/geneviewer/tsc1na.dat");
      Vector cols = new Vector();
      cols.add("CHIP"); cols.add("D0"); cols.add("D1"); cols.add("D2"); cols.add("D3");
      cols.add("D6"); cols.add("D9"); cols.add("D11"); cols.add("D13"); cols.add("D15");
      cols.add("D17"); cols.add("D13T"); cols.add("D15T"); cols.add("D17T");
      cols.add("GeneSymbol");

      VDataTable vt3as = vdaoengine.utils.VSimpleProcedures.SelectColumns(vt3a,cols);
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt3as,"c:/datas/ewing/kinetics/geneviewer/tsc2nas.dat");*/

      /*VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/ewing/kinetics/vida/test.dat");

      VPlot vp = new VPlot();
      vp.columnsVector.add("KL1_T0;KL2_24h;KL3_48h;KL4_72h;KL5_6j;KL6_9j;KL7_11j;KL8_13j;KL10_15j;KL12_17j");
      vp.Xpoints = "0;1;2;3;6;9;11;13;15;17";
      vp.suffixes.add(" inh");
      vp.title = "Expression kinetics";
      vp.LabelId = "PS";
      vp.LabelName = "GSymbol";
      vp.table = vt;
      vp.createPlot();
      vp.showPlot();*/


      /*String annotfile = "c:/datas/HG_U133_Plus_2_annot.csv";
      VDataTable annt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(annotfile,true,",\t");
      for(int i=0;i<annt.rowCount;i++)
         System.out.println(annt.stringTable[i][2]);*/
      //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/EWING/data-ewing.txt",true," \"");
      //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"C:/Datas/EWING/data-ewing_.dat");
      //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("C:/Datas/EWING/data-ewing_.dat");
      //VDataTable vtt = vt.transposeTable("label");
      //vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt,"C:/Datas/EWING/data-ewing.dat");

      /*VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/breastcancer/old/temp.dat");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vt,"c:/datas/breastcancer/old/temp1.dat");
      VDataTable vtt = vt.transposeTable("CHIP");
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt,"c:/datas/breastcancer/old/tempt.dat");*/

      //VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/endometrial/PCA_EndometrialTissue/matricepca_10pc",false," \t");
      //VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
      //vdaoengine.utils.IO.makeMatrixDisplayGIF(vdaoengine.utils.IO.convertFloatArrayToDouble(vd.massif),"c:/datas/endometrial/PCA_EndometrialTissue/matricepca_10pc.gif",15);

      //vdaoengine.utils.VSimpleProcedures.SeparateTableByField("c:/datas/breastcancer/onlya/an_a.dat",13);

      /*VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/breastcancer/GSEA/test.xls",true,"\t");
      for(int i=0;i<vt.rowCount;i++){
        for(int j=0;j<vt.colCount;j++)
          System.out.print(vt.stringTable[i][j]+"\t");
        System.out.println();
      }*/

      /*String fn = "c:/myprograms/ewingkinetics/tsc1_1n.dat";
      String chipID = "CHIP";
      VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(fn);
      VDataTable vti = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("c:/datas/ewing/ewing_network/biobase/prot_chosen.xls",false,"\t");
      VDataTable vto = vdaoengine.utils.VSimpleProcedures.MergeTables(vti,"N2",vt,chipID,"NA");
      vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(vto,"c:/datas/ewing/ewing_network/biobase/prot.dat");*/

      //String project = "ec_out7";
      String project = "d3ef3000_t_pca";

      VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(project+".dat");
      VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization (vt,-1);

      ElasticTree elt = new ElasticTree(vd.coordCount);
      elt.loadFromFile(project+".eltree.vem");
      elt.filterEdges();

      /*VDataSet vdg = new VDataSet();
      vdg.pointCount = elt.Nodes.length;
      vdg.coordCount = elt.dimension;
      vdg.massif = elt.Nodes;
      PCAMethod pcg = new PCAMethod();
      pcg.setDataSet(vdg);
      pcg.calcBasis(2);
      float pcgd[][] = new float[2][vdg.coordCount];
      for(int i=0;i<2;i++)
        for(int j=0;j<vdg.coordCount;j++){
            pcgd[i][j] = (float)pcg.getBasis().basis[i][j];
        }
      IntegerVector nrib = elt.getStarByCentralNode(0);
      elt.calcAnglesOfStar(nrib,1,pcgd);*/

      /*VDataTable vti = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/myprograms/vidarus/data/iris.dat");
      VDataTable vto = new VDataTable();
      vto.copyHeader(vti);
      LineNumberReader lr = new LineNumberReader(new FileReader("c:/myprograms/vidarus/data/iris_hc_order"));
      String s = lr.readLine();
      StringTokenizer st = new StringTokenizer(s,"\t ");
      Vector v = new Vector();
      while(st.hasMoreTokens())
        v.add(st.nextToken());
      vto.stringTable = new String[v.size()][vti.colCount];
      for(int i=0;i<v.size();i++){
        int k = Integer.parseInt((String)v.elementAt(i));
        //System.out.println(k);
        vto.stringTable[i] = vti.stringTable[k-1];
      }
      vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vto,"c:/myprograms/vidarus/data/iris_o.dat");*/


      //elt.saveToFile("out1.vem",project);

      //elt.flips.add(new Integer(44));

      // sc
      //elt.multipliers.put(new Integer(1),new Float(2.0f));
      //elt.multipliers.put(new Integer(14),new Float(2.0f));
      //elt.multipliers.put(new Integer(32),new Float(2.0f));

      // bh
      /*elt.multipliers.put(new Integer(0),new Float(4.0f));
      elt.multipliers.put(new Integer(3),new Float(1.8f));
      elt.multipliers.put(new Integer(10),new Float(1.8f));
      elt.multipliers.put(new Integer(4),new Float(2f));
      elt.multipliers.put(new Integer(9),new Float(2f));
      elt.multipliers.put(new Integer(16),new Float(2f));
      elt.multipliers.put(new Integer(49),new Float(2f));
      elt.multipliers.put(new Integer(28),new Float(2f));*/

      // ec
      /*elt.multipliers.put(new Integer(5),new Float(1.5f));
      elt.multipliers.put(new Integer(17),new Float(1.5f));
      elt.multipliers.put(new Integer(44),new Float(1.5f));
      elt.multipliers.put(new Integer(75),new Float(0.7f));
      elt.multipliers.put(new Integer(48),new Float(0.7f));
      elt.multipliers.put(new Integer(49),new Float(1.5f));


      //elt.flips.add(new Integer(47));*/


      //D3 dataset
      //elt.flips.add(new Integer(11));


      //elt.flips.add(new Integer(12));
      //elt.flips.add(new Integer(0));

      elt.flips.add(new Integer(0));
      elt.flips.add(new Integer(12));
      elt.flips.add(new Integer(22));
      elt.flips.add(new Integer(5));
      //elt.flips.add(new Integer(3));
      elt.multipliers.put(new Integer(19),new Float(1.5f));
      elt.multipliers.put(new Integer(50),new Float(1.5f));
      elt.multipliers.put(new Integer(30),new Float(1.5f));
      elt.multipliers.put(new Integer(0),new Float(1.5f));
      elt.multipliers.put(new Integer(1),new Float(1.5f));
      elt.multipliers.put(new Integer(13),new Float(1.5f));
      elt.multipliers.put(new Integer(53),new Float(1.5f));
      elt.multipliers.put(new Integer(23),new Float(1.8f));
      elt.multipliers.put(new Integer(33),new Float(1.5f));
      elt.multipliers.put(new Integer(4),new Float(3f));
      elt.multipliers.put(new Integer(5),new Float(2f));

      //elt.multipliers.put(new Integer(4),new Float(3f));

      elt.calculateTreeLayout(-1,true);
      elt.type = 10;
      //elt.saveToFile("out2.vem",project);
      elt.writeOutEdges("temp/out.edges");
      elt.writeOutTreeNodes2D("temp/out.nodes2d");
      elt.writeOutTreeNodes("temp/out.nodes");

      VDataSet vdtn = new VDataSet();
      vdtn.pointCount = elt.Nodes3D.length;
      vdtn.coordCount = elt.dimension;
      vdtn.massif = elt.Nodes;
      vdaoengine.data.io.VDatReadWrite.saveToVDatFileLabeled(vdtn,"testn.dat");
      elt.saveToFile("outn.vem","testn");

      //VClassifier vcl = VSimpleProcedures.ClassifyDataSetByField(vd,"IRIS-SETOS",VClassifier.CHANGE_FILLCOLOR);
      VClassifier vcl = VSimpleProcedures.ClassifyDataSetByField(vd,"D1",VClassifier.CHANGE_FILLCOLOR);

      Vector v = vcl.getClassVector();
      for(int i=0;i<v.size();i++){
        VDataClass cl = (VDataClass)v.elementAt(i);
        //System.out.print(cl.name+"\t("+cl.getIDset().size()+")\n");
        System.out.print(cl.name+"\n");
      }


      elt.saveNodeClassClusters("temp/out.clusters",vd,vcl);

      elt.dimension = 3;
      elt.Nodes = elt.Nodes3D;
      VDataSet vdt = new VDataSet();
      vdt.pointCount = elt.Nodes3D.length;
      vdt.coordCount = 3;
      vdt.massif = elt.Nodes3D;
      vdaoengine.data.io.VDatReadWrite.saveToVDatFileLabeled(vdt,"test.dat");
      elt.saveToFile("out3.vem","test");

      elt.saveNodeClassClusters("temp/out.clusters2",vdt,vcl);

    }catch(Exception e){
      e.printStackTrace();
    }
  }
}