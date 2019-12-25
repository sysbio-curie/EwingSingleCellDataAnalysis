package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;


public class GSDASampling extends GSDA {

  VDataTable restable = null;
  int numberofresampling = 20;

  public static void main(String[] args) {
	  
	try{

    GSDASampling gsda = new GSDASampling();

    for(int i=0;i<args.length;i++){
      if(args[i].equals("-data"))
        gsda.data = args[i+1];
      if(args[i].equals("-res"))
        gsda.resfile = args[i+1];
      if(args[i].equals("-sigdb"))
        gsda.sigdb = args[i+1];
      if(args[i].equals("-annot"))
        gsda.annotfile = args[i+1];
      if(args[i].equals("-nos"))
        gsda.numberofresampling = (new Integer(args[i+1])).intValue();
      if(args[i].equals("-sannot"))
      	gsda.sampleAnnotationFile = args[i+1];
      if(args[i].equals("-sassd"))
       	gsda.sampleAssociationsDescriptions.add("d_"+args[i+1]);
      if(args[i].equals("-sassc"))
        gsda.sampleAssociationsDescriptions.add("c_"+args[i+1]);
    }

    //int gssizes[] = {10,50,100,500};
    int gssizes[] = {12,15,20,30,50,100,500};

    gsda.restable = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile(gsda.resfile,true,"\t");

    gsda.loadAnnotation();
    gsda.loadData();
    gsda.loadSignatures();
    gsda.selectOnlySignatureGenes();
    gsda.loadSampleAnnotation();
    gsda.calcGlobalDisplacement();

    VHistogram hists_gap1[] = new VHistogram[gssizes.length];
    VHistogram hists_gap2[] = new VHistogram[gssizes.length];
    Vector samplings = new Vector();
    Date tm = new Date();
    
    String scorefile = gsda.resfile.substring(0,gsda.resfile.length()-4)+"_scores."+gsda.resfile.substring(gsda.resfile.length()-3,gsda.resfile.length());
    
	FileWriter fw = new FileWriter(scorefile);
	
	gsda.writeResultHeader(fw);
    
    for(int i=0;i<gssizes.length;i++){
      int size = gssizes[i];
      Vector s = gsda.calcSampling(size,fw);
      samplings.add(s);
      //hists_gap1[i] = gsda.getHistogramGap1(s);
      //hists_gap2[i] = gsda.getHistogramGap2(s);
    }
    System.out.println("Time spent: "+((new Date()).getTime()-tm.getTime()));
    
	fw.close();
    
    /*gsda.restable.addNewColumn("pL1","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pGAP1","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pGAP1F","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pGAP2","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pDISPL","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pVAR1","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pSKEW","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pSKEWF","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pMEDCORR","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pABSMEDCORR","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("pABSMEDSPCORR","","",VDataTable.NUMERICAL,"NaN");

    gsda.restable.addNewColumn("L1Mean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("L1StdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("GAP1Mean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("GAP1StdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("GAP2Mean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("GAP2StdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("GAP1SMean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("GAP1SStdDev","","",VDataTable.NUMERICAL,"NaN");

    gsda.restable.addNewColumn("DISPLMean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("DISPLStdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("VAR1Mean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("VAR1StdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("SKEWMean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("SKEWStdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("SKEWFMean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("SKEWFStdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("ABSMEDCORRMean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("ABSMEDCORRStdDev","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("ABSMEDSPCORRMean","","",VDataTable.NUMERICAL,"NaN");
    gsda.restable.addNewColumn("ABSMEDSPCORRStdDev","","",VDataTable.NUMERICAL,"NaN");



    for(int i=0;i<gsda.restable.rowCount;i++){
      String sgap1 = gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP1")];
      if(!sgap1.equals("NaN")){
        float gap1 = Float.parseFloat(sgap1);
        float l1 = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("L1")]);
        float gap2 = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP2")]);
        float gap1s = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP1S")]);
        float displ = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("DISPLCMNT")]);
        float var1 = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("VAR1")]);
        float skew = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SKEW")]);
        float skewf = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SKEWF")]);
        float medcorr = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("MEDCORR")]);
        float absmedcorr = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("ABSMEDCORR")]);
        float absmedspcorr = Float.parseFloat(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("ABSMEDSPCORR")]);

        int size = Integer.parseInt(gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SIZE")]);
        int closesthist = -1; int mindist = Integer.MAX_VALUE;
        for(int j=0;j<gssizes.length;j++){
          int dist = Math.abs(gssizes[j]-size);
          if(dist<mindist){
            mindist = dist; closesthist = j;
          }
        }
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pL1")] = ""+gsda.getScorePValue(l1,size,gssizes,samplings,0);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("L1Mean")] = ""+gsda.getScoreMean(size,gssizes,samplings,0);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("L1StdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,0);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pGAP1")] = ""+gsda.getScorePValue(gap1,size,gssizes,samplings,1);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP1Mean")] = ""+gsda.getScoreMean(size,gssizes,samplings,1);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP1StdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,1);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pGAP1S")] = ""+gsda.getScorePValue(gap1s,size,gssizes,samplings,14);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP1SMean")] = ""+gsda.getScoreMean(size,gssizes,samplings,14);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP1SStdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,14);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pGAP2")] = ""+gsda.getScorePValue(gap2,size,gssizes,samplings,4);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP2Mean")] = ""+gsda.getScoreMean(size,gssizes,samplings,4);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("GAP2StdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,4);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pDISPL")] = ""+gsda.getScorePValue(displ,size,gssizes,samplings,8);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("DISPLMean")] = ""+gsda.getScoreMean(size,gssizes,samplings,8);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("DISPLStdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,8);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pVAR1")] = ""+gsda.getScorePValue(var1,size,gssizes,samplings,10);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("VAR1Mean")] = ""+gsda.getScoreMean(size,gssizes,samplings,10);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("VAR1StdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,10);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pSKEW")] = ""+gsda.getScorePValue(skew,size,gssizes,samplings,11);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SKEWMean")] = ""+gsda.getScoreMean(size,gssizes,samplings,11);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SKEWStdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,11);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pSKEWF")] = ""+gsda.getScorePValue(skewf,size,gssizes,samplings,16);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SKEWFMean")] = ""+gsda.getScoreMean(size,gssizes,samplings,16);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("SKEWFStdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,16);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pMEDCORR")] = ""+gsda.getScorePValue(medcorr,size,gssizes,samplings,12);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pABSMEDCORR")] = ""+gsda.getScorePValue(absmedcorr,size,gssizes,samplings,13);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("ABSMEDCORRMean")] = ""+gsda.getScoreMean(size,gssizes,samplings,13);
        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("ABSMEDCORRStdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,13);

        gsda.restable.stringTable[i][gsda.restable.fieldNumByName("pABSMEDSPCORR")] = ""+gsda.getScorePValue(absmedspcorr,size,gssizes,samplings,15);
         gsda.restable.stringTable[i][gsda.restable.fieldNumByName("ABSMEDSPCORRMean")] = ""+gsda.getScoreMean(size,gssizes,samplings,15);
         gsda.restable.stringTable[i][gsda.restable.fieldNumByName("ABSMEDSPCORRStdDev")] = ""+gsda.getScoreStdDev(size,gssizes,samplings,15);

      }
    }*/

    vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(gsda.restable,gsda.resfile.substring(0,gsda.resfile.length()-4)+"_s."+gsda.resfile.substring(gsda.resfile.length()-3,gsda.resfile.length()));

	}catch(Exception e){
		e.printStackTrace();
	}
  }

  public Vector calcSampling(int size, FileWriter fw) throws Exception{
  
    Vector v = new Vector();

    for(int j=0;j<numberofresampling;j++){
    System.out.println(size+":"+(j+1));
    Random r = new Random();
    GESignature rs = new GESignature();
    for(int i=0;i<size;i++)
      rs.geneNames.add(table.stringTable[r.nextInt(table.rowCount)][table.fieldNumByName("GeneSymbol")]);

    Vector<Float> sampleAssociationsScores = new Vector<Float>();
    Vector<Double> sampleAssociationsPValues = new Vector<Double>();      
    double d[] = getSignatureScores(rs,sampleAssociationsScores,sampleAssociationsPValues);
    
    //writeScores(fw, rs, scores, counteri, counterassoc)
    
    v.add(d);
    }
    
    return v;
  }

  public VHistogram getHistogramGap1(Vector v){
    VHistogram hist = new VHistogram();
    Vector fn = new Vector();
    fn.add("GAP1");
    hist.setBordersMinMax(restable,Math.min((int)(0.05f*numberofresampling),50),fn);
    hist.initBars(1);
    for(int i=0;i<v.size();i++){
      double s[] = (double[])v.get(i);
      hist.addValue((float)s[1],0);
    }
    return hist;
  }

  public VHistogram getHistogramGap2(Vector v){
    VHistogram hist = new VHistogram();
    Vector fn = new Vector();
    fn.add("GAP2");
    hist.setBordersMinMax(restable,Math.min((int)(0.1f*numberofresampling),50),fn);
    //hist.setBorders(1f,5f,Math.min((int)(0.05f*numberofresampling),50));
    hist.initBars(1);
    for(int i=0;i<v.size();i++){
      double s[] = (double[])v.get(i);
      hist.addValue((float)s[4],0);
    }
    return hist;
    /*VHistogram hist = new VHistogram();
    double mingap2 = Float.MAX_VALUE;
    double maxgap2 = Float.MIN_VALUE;
    for(int i=0;i<v.size();i++){
      double s[] = (double[])v.get(i);
      if(mingap2<s[4]) mingap2 = s[4];
      if(maxgap2>s[4]) maxgap2 = s[4];
    }
    hist.setBorders((float)mingap2,(float)maxgap2,100);
    return hist;*/
  }

  public void writeHistoInTable(VHistogram[] hists, int gssize[], String fn){
        VDataTable hist = new VDataTable();
        hist.colCount = 0;
        hist.rowCount = hists[0].Bars.size();
        hist.fieldNames = new String[0];
        hist.fieldClasses = new String[0];
        hist.fieldDescriptions = new String[0];
        hist.fieldTypes = new int[0];
        hist.stringTable = new String[hist.rowCount][hist.colCount];
        hist.addNewColumn("GAP","","",hist.NUMERICAL,"_");
        for(int i=0;i<gssize.length;i++)
          hist.addNewColumn("GS"+gssize[i],"","",hist.NUMERICAL,"_");
        for(int i=0;i<hist.rowCount;i++){
          hist.stringTable[i][0] = ""+hists[0].barPosition(i);
          for(int j=1;j<hist.colCount;j++){
            hist.stringTable[i][j] = ""+((float[])hists[j-1].Bars.get(i))[0];
          }
        }
        vdaoengine.data.io.VDatReadWrite.saveToSimpleDatFile(hist,fn);
  }

  public float getScorePValue(float gapvalue, int size, int gssizes[], Vector samplings, int nscore){
    float p = 0f;
    int closest = -1; int mindist = Integer.MAX_VALUE;
    for(int j=0;j<gssizes.length;j++){
      int dist = Math.abs(gssizes[j]-size);
      if(dist<mindist){
        mindist = dist; closest = j;
      }
    }
    Vector s = (Vector)samplings.get(closest);
    for(int i=0;i<s.size();i++){
      double d[] = (double[])s.get(i);
      if(d==null){
      	System.out.println("ERROR: d=null for the size "+closest+", index "+i);  
        }else      
      if(d[nscore]>gapvalue)
        p+=1f;
    }
    p/=s.size();
    return p;
  }

  public float getScoreMean(int size, int gssizes[], Vector samplings, int nscore){
    float p = 0f;
    int closest = -1; int mindist = Integer.MAX_VALUE;
    for(int j=0;j<gssizes.length;j++){
      int dist = Math.abs(gssizes[j]-size);
      if(dist<mindist){
        mindist = dist; closest = j;
      }
    }
    Vector s = (Vector)samplings.get(closest);
    for(int i=0;i<s.size();i++){
      double d[] = (double[])s.get(i);
      if(d==null){
    	System.out.println("ERROR: d=null for the size "+closest+", index "+i);  
      }else
        p+=d[nscore];
    }
    p/=s.size();
    return p;
  }

  public float getScoreStdDev(int size, int gssizes[], Vector samplings, int nscore){
    float p = 0f;
    int closest = -1; int mindist = Integer.MAX_VALUE;
    for(int j=0;j<gssizes.length;j++){
      int dist = Math.abs(gssizes[j]-size);
      if(dist<mindist){
        mindist = dist; closest = j;
      }
    }
    Vector s = (Vector)samplings.get(closest);
    float p2 = 0;
    for(int i=0;i<s.size();i++){
      double d[] = (double[])s.get(i);
      if(d==null){
      	System.out.println("ERROR: d=null for the size "+closest+", index "+i);  
        }else{
      p+=d[nscore];
      p2+=d[nscore]*d[nscore];
        }
    }
    p=(float)Math.sqrt(p2/s.size()-(p/s.size())*(p/s.size()));
    return p;
  }


}