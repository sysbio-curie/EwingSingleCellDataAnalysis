package vdaoengine;

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;

public class calcPCAforMicroarray {

  public static void main(String[] args) {
    // transpose
    VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile("c:/datas/sabrina/normData_ApcRasApc_test.dat");
    VDataTable vtt = vt.transposeTable("Identifier");
    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vtt,"c:/datas/sabrina/transposed.dat");
    // make PCA
    VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vtt,-1);
    PCAMethod PCA = new PCAMethod();
    PCA.setDataSet(vd);
    int numberOfcomponents = 3;
    PCA.calcBasis(numberOfcomponents);

    //System.out.println(PCA.getBasis().toString());
    /*VLinearBasis vb = PCA.getBasis();
    for (int i = 0; i < vb.spaceDimension; i++){
    System.out.print(vtt.fieldNames[i+1]+"\t");
    for(int j=0; j<numberOfcomponents;j++)
        System.out.print(vb.basis[j][i]+"\t");
    System.out.println();
    }*/

    System.out.println(PCA.getProjectedDataset().toString());

  }
}