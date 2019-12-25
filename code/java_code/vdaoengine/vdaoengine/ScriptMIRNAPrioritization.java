package vdaoengine;

import vdaoengine.*;
import java.io.*;
import java.util.*;
import vdaoengine.data.*;
import vdaoengine.data.io.*;
import vdaoengine.utils.VSimpleProcedures;
import vdaoengine.utils.VSimpleFunctions;
import vdaoengine.analysis.*;
import vdaoengine.TableUtils.*;


public class ScriptMIRNAPrioritization {

public static void main(String[] args) {

try{
String path = "C:/Datas/EWING/microRNA/prioritization/";
Vector<String> files = new Vector<String>();
files.add("short_table_summary_dset1_20p_seq_based");
VDatReadWrite.numberOfDigitsToKeep = 2;

for(int i=0;i</*files.size()*/1;i++){
	String fn = path+files.get(i)+".txt";
	System.out.println("Processing file: "+fn);
	//VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, false, "\t");
	//vt.fieldTypes[0] = vt.STRING;

	VDataSet vd = VDatReadWrite.LoadFileWithFirstIDColumn(fn,false);
	
	VDataSet origvd = VDatReadWrite.LoadFileWithFirstIDColumn(fn,false);
	VDataSet vd1 = VDatReadWrite.LoadFileWithFirstIDColumn(fn,false);

	vd.calcStatistics();
	VNormalization N = new VNormalization(vd.simpleStatistics);
	for(int k=0;k<vd.coordCount;k++)
		N.setType(k,0);  
	vd.addToPreProcess(N);
	vd.preProcessData();
	//vd = TableUtils.filterByVariation(vd, vd.pointCount);
	
	vd1.calcStatistics();
	N = new VNormalization(vd1.simpleStatistics);
	for(int k=0;k<vd1.coordCount;k++)
		N.setType(k,0);  
	vd1.addToPreProcess(N);
	vd1.preProcessData();
	
	vd = TableUtils.filterByVariation(vd, vd.pointCount);
		//for(int s=0;s<200;s++)
		// System.out.println(VSimpleFunctions.calcStandardDeviation(vd.massif[s]));
	int order[] = TableUtils.filterByVariationOrder(vd1, vd.pointCount);
	int numberOfNonZeros = 0;
		//for(int k=0;k<vd.pointCount;k++){

	numberOfNonZeros = 200;

	VDataSet vdf = new VDataSet();
    vdf.coordCount = vd.coordCount;
    vdf.pointCount = numberOfNonZeros;
    vdf.massif = new float[vdf.pointCount][vdf.coordCount];

    for(int m=0;m<numberOfNonZeros;m++){
    	int k = origvd.pointCount-order[m]-1;
    	vdf.massif[m] = origvd.massif[k];
    	System.out.println(k+"\t"+origvd.massif[k][0]+"\t"+origvd.massif[k][1]+"\t"+origvd.massif[k][2]);
    }

    System.out.println("Saving...");
    VDatReadWrite.saveToVDatFile(vd, path+files.get(i)+"_filtered.dat");
    VDatReadWrite.saveToVDatFile(vdf, path+files.get(i)+"_original.dat");

if (1 == 2){
	int numberOfPrincipalComponents = 5;
	PCAMethod pca = new PCAMethod();
	pca.setDataSet(vd);
	pca.calcBasis(numberOfPrincipalComponents);
	vd.calcStatistics();
	double eig[] =
		pca.calcDispersionsRelative(vd.simpleStatistics.totalDispersion*vd.simpleStatistics.totalDispersion);

	for(int j=0;j<numberOfPrincipalComponents;j++)

		System.out.println("PC"+(j+1)+"\t"+eig[j]);

	VDataSet vdp = pca.getProjectedDataset();
	System.out.println("Saving...");
	VDatReadWrite.saveToVDatFile(vdp, path+files.get(i)+"_projected.dat");
}}

}catch(Exception e){
e.printStackTrace();
}}}




