package getools;

import java.util.*;
import java.io.*;
import java.text.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;


public class GSDAMergeSignatures {
  public static void main(String[] args) {

    try{
    	
    	
      String gmtFileName = args[0];
      
      String outFileName = gmtFileName.substring(0, gmtFileName.length()-4);
      
      String suffixUp = args[1];
      String suffixDown = args[2];
      
      boolean addSign = args[3].toLowerCase().equals("true");

      GMTReader gmt = new GMTReader();
      Vector sigs = gmt.readGMTDatabase(gmtFileName);

      Vector newSig = new Vector();

      for(int i=0;i<sigs.size();i++){
        String fndn = ((GESignature)sigs.get(i)).name;
        if(fndn.endsWith(suffixDown)){
          fndn = fndn.substring(0,fndn.length()-3);
          for(int j=0;j<sigs.size();j++){
            String fnup = ((GESignature)sigs.get(j)).name;
            if(fnup.equals(fndn+suffixUp)){
              System.out.println("Found "+fndn);
              GESignature gdn = (GESignature)sigs.get(i);
              GESignature gup = (GESignature)sigs.get(j);
              GESignature nsig = new GESignature();
              for(int k=0;k<gdn.geneNames.size();k++)
            	  if(!addSign)
            		  nsig.geneNames.add(gdn.geneNames.get(k));
            	  else
            		  nsig.geneNames.add(gdn.geneNames.get(k)+"[-1]");
              for(int k=0;k<gup.geneNames.size();k++)
            	  if(!addSign)
            		  nsig.geneNames.add(gup.geneNames.get(k));
            	  else
            		  nsig.geneNames.add(gup.geneNames.get(k)+"[+1]");
              nsig.name = fndn;
              nsig.probeSets = nsig.geneNames;
              newSig.add(nsig);
            }
          }
        }
      }

      GMTReader.saveSignaturesTOGMTFormat(newSig,outFileName+"_merged.gmt");


    }catch(Exception e){
      e.printStackTrace();
    }

  }
}