package vdaoengine.analysis.fastica;

import java.util.*;
import java.lang.*;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;
import vdaoengine.TableUtils;
import vdaoengine.data.*;
import vdaoengine.utils.*;



public class testFastICA1{


	public static void main(String[] args){


		Algebra alg=new Algebra();
                //DoubleMatrix2D X = new DenseDoubleMatrix2D(10,10);

                VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/FastICA_Java/test/pdx1000_ica.txt", true, "\t");
                TableUtils.findAllNumericalColumns(vt);
                VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
                DoubleMatrix2D X = new DenseDoubleMatrix2D(vd.coordCount,vd.pointCount);
                for(int i=0;i<vd.pointCount;i++)
                  for(int j=0;j<vd.coordCount;j++)
                    X.set(j,i,(double)vd.massif[i][j]);

		// Y : test matrix
		/*DoubleMatrix2D Y = new DenseDoubleMatrix2D(10,10);
		for(int i=0;i<Y.rows();i++)
			for(int j=0;j<Y.columns();j++)
				if(i==j)
					Y.set(i,j,1);
				else
					Y.set(i,j,2*i+j);

		//System.out.println("Matrice Y :");
		//System.out.println(Y);
		//System.out.println("");
                */

		//X : matrix without incovenients
		//DoubleMatrix2D X=alg.mult(alg.transpose(Y),Y);
		//System.out.println("Matrice X :");
		//System.out.println(X);
		//System.out.println("");

		// Argument list for fastica :
		LinkedList arguments = new LinkedList();
		System.out.println(arguments.size());
		arguments.add(0,"numOfIC");
		arguments.add(1,new Integer(2));
                //arguments.add(1,"2");
		//arguments.add(2,"lastEig");
		//arguments.add(3,new Integer(5));

		// fastica :
		fastica resultICA = new fastica(X,arguments);

		// output result :
		DoubleMatrix2D icasig = resultICA.getIcaSignal();
		System.out.println("Matrice icasig :");
		System.out.println(icasig);
		System.out.println("");

		DoubleMatrix2D A = resultICA.getA();
		System.out.println("Matrice A :");
		System.out.println(A);
		System.out.println("");

		DoubleMatrix2D W = resultICA.getW();
		System.out.println("Matrice W :");
		System.out.println(W);
		System.out.println("");


	}

        public static VDataTable convertColtMatrix(DoubleMatrix2D mat){
          VDataTable vt = new VDataTable();
          vt.rowCount = mat.rows();
          vt.colCount = mat.columns()+1;
          vt.stringTable = new String[vt.rowCount][vt.colCount];
          vt.fieldNames = new String[vt.colCount];
          vt.fieldClasses = new String[vt.colCount];
          vt.fieldDescriptions = new String[vt.colCount];
          vt.fieldTypes = new int[vt.colCount];
          vt.fieldNames[0] = "ID";
          vt.fieldTypes[0] = vt.STRING;
          for(int i=1;i<vt.colCount;i++){
            vt.fieldNames[i] = "N"+i;
            vt.fieldTypes[i] = vt.NUMERICAL;
          }
          for(int i=0;i<mat.rows();i++)
            for(int j=0;j<mat.columns();j++){
              vt.stringTable[i][j+1] = ""+mat.get(i,j);
            }
          return vt;
        }

}