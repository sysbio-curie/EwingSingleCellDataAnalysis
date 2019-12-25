package vdaoengine;

import java.io.FileWriter;
import java.util.Random;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;

public class processDebts {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:\\Datas\\Kolodyazhnii\\debts\\xgboost\\complete_without_CREDIT_DURATION\\debt_full_predictPAYED_AT_ALL.txt", true,"\t");
			
			Random r = new Random();
			
			FileWriter fw_train = new FileWriter("C:\\Datas\\Kolodyazhnii\\debts\\xgboost\\complete_without_CREDIT_DURATION\\debt_full_predictPAYED_AT_ALL_train.txt");
			FileWriter fw_test = new FileWriter("C:\\Datas\\Kolodyazhnii\\debts\\xgboost\\complete_without_CREDIT_DURATION\\debt_full_predictPAYED_AT_ALL_test.txt");
			
			for(int i=0;i<vt.colCount;i++) fw_train.write(vt.fieldNames[i]+"\t"); fw_train.write("\n");
			for(int i=0;i<vt.colCount;i++) fw_test.write(vt.fieldNames[i]+"\t"); fw_test.write("\n");
			
			for(int j=0;j<vt.rowCount;j++){
				float f = r.nextFloat();
				if(f<0.8f){
					for(int i=0;i<vt.colCount;i++) fw_train.write(vt.stringTable[j][i]+"\t"); fw_train.write("\n");
				}else{
					for(int i=0;i<vt.colCount;i++) fw_test.write(vt.stringTable[j][i]+"\t"); fw_test.write("\n");
				}
			}
			
			fw_train.close();
			fw_test.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
