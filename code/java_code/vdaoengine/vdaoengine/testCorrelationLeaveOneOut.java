package vdaoengine;

import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.*;
import java.io.*;

public class testCorrelationLeaveOneOut {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			
		float x1[] = {86  ,  97  ,  99  , 100 ,  101 ,  103 ,  106 ,  110 ,  112 ,  113};
		float x2[] = {0  ,  0  ,  0 ,   0  ,  1  ,  0  ,   0  ,  0   ,  0  ,  1};
		System.out.println(VSimpleFunctions.calcSpearmanCorrelationCoeff(x1, x2));
		System.exit(0);
			
			
		/*for(float t=0f;t<5f;t+=0.1f)
			System.out.println(t+"\t"+VSimpleFunctions.tcdf(t, 9));*/
		//float x1[] = {86  ,  97  ,  99  , 100 ,  101 ,  103 ,  106 ,  110 ,  112 ,  113};
		//float x2[] = {0  ,  20  ,  28 ,   27  ,  50  ,  29  ,   7  ,  17   ,  6  ,  12};
		
		//float x1[] = {6.87661784015723f,	9.4459300505392f,	7.62364376829494f,	8.34300234998605f,	8.70820916988971f,	9.60864562628952f,	10.0603047160111f,	9.56770205982109f,	9.14636579259694f,	8.18896208343503f};
		//float x2[] = {25.54f,	28.52f,	26.41f,	27.33f,	27.14f,	28.81f,	28.58f,	28.66f,	27.98f,	27.81f};
		
		//float x2[] = {90  ,  100  ,  100 ,   100  ,  100  ,  105  ,   110  ,  110   ,  115  ,  120};
		//System.out.println(VSimpleFunctions.calcSpearmanCorrelationCoeff(x1, x2));
		//float cv = VSimpleFunctions.calcCorrelationCoeff(x1, x2);
		/*float cv = VSimpleFunctions.calcSpearmanCorrelationCoeff(x1, x2);
		System.out.println(cv+"\t"+VSimpleFunctions.calcCorrelationPValue(cv, x1.length));
		System.exit(0);*/
			
		
		String prefix = "c:/datas/Ewing/mirna/correlation/";
		
		//float Zth = 1.96f;  
		float Zth = 1.96f;
		double pvalTh = 1e-5;
		
		VDataTable genes = VDatReadWrite.LoadFromSimpleDatFile(prefix+"tab_Data-Ewing_filtered_4.txt", true, "\t");
		VDataTable microRNAs = VDatReadWrite.LoadFromSimpleDatFile(prefix+"tab_mirna_patients_filtered_4.txt", true, "\t");
		//VDataTable genes = VDatReadWrite.LoadFromSimpleDatFile(prefix+"gene_test.txt", true, "\t");
		//VDataTable microRNAs = VDatReadWrite.LoadFromSimpleDatFile(prefix+"mirna_test.txt", true, "\t");
		
		
		for(int i=1;i<genes.colCount;i++) genes.fieldTypes[i] = VDataTable.NUMERICAL;
		for(int i=1;i<microRNAs.colCount;i++) microRNAs.fieldTypes[i] = VDataTable.NUMERICAL;
		
		VDataSet genesV = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(genes, -1);
		VDataSet mirnaV = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(microRNAs, -1);
		
		System.out.println("Genes: "+genes.rowCount+", "+(genes.colCount-1)+" samples");
		System.out.println("MiRNA: "+microRNAs.rowCount+", "+(microRNAs.colCount-1)+" samples");
		
		float fgenes[] = new float[genes.colCount-1];
		float fmirna[] = new float[fgenes.length];
		
		FileWriter fw = new FileWriter(prefix+"out.txt");
		
		for(int i=0;i<genes.rowCount;i++){
			fgenes = genesV.getVector(i);
			if(i==Math.round(i*0.01)*100)
				System.out.print(i+"\t");
			for(int j=0;j<microRNAs.rowCount;j++){
				fmirna = mirnaV.getVector(j);
				float corrPr = VSimpleFunctions.calcCorrelationCoeff(fgenes, fmirna);
				float corrSp = VSimpleFunctions.calcSpearmanCorrelationCoeff(fgenes, fmirna);
				float corrPrLo = VSimpleFunctions.calcCorrelationCoeffLeaveOneOut(fgenes, fmirna, Zth, 0);
				float corrSpLo = VSimpleFunctions.calcCorrelationCoeffLeaveOneOut(fgenes, fmirna, Zth, 1);
				float pval = (float)VSimpleFunctions.calcCorrelationPValue(corrPr,fgenes.length);
				float pvalLo = (float)VSimpleFunctions.calcCorrelationPValue(corrPrLo,fgenes.length);
				if(VSimpleFunctions.calcCorrelationPValue(corrPrLo,fgenes.length)<pvalTh)
					fw.write(genes.stringTable[i][0]+"\t"+microRNAs.stringTable[j][0]+"\t"+corrPr+"\t"+corrPrLo+"\t"+corrSp+"\t"+corrSpLo+"\t"+pval+"\t"+pvalLo+"\n");
			}
		}
		System.out.println();
		
		fw.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
