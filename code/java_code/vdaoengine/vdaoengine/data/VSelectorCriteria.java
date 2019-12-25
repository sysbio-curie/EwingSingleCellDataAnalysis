package vdaoengine.data;

import vdaoengine.data.*;
import vdaoengine.utils.*;
import java.util.Random;
import java.lang.*;

public class VSelectorCriteria {

public final static int EQUAL = 0;
public final static int INCLUDE = 1;
public final static int NOT_EQUAL = 2;
public final static int NOT_INCLUDE = 3;

public final static int FIELD_VALUE = 0;
public final static int RANDOM = 1;

String fieldName;
String fieldValue;
int relation; // 0 - =, 1 - include, 2 - <>, 3 - not include

float probability;
Random rnd;
int type; // 0 - by Field, 1 - random, 2 - by class

public VSelectorCriteria(float prob){
type = 1;
probability = prob;
rnd = new Random();
}

public VSelectorCriteria(String fn, String fv, int rel){
type = FIELD_VALUE;
fieldName = fn;
fieldValue = fv;
relation = rel;
}

public boolean isSatisfy(VDataTable tab, String[] row){
boolean res = false;
int fn=-1;
switch(type){
case FIELD_VALUE:
	fn=tab.fieldNumByName(fieldName);
        if(fn!=-1){
        switch(relation){
        case EQUAL:
		if(tab.fieldTypes[fn]==VDataTable.NUMERICAL){
			float vt = Float.parseFloat(row[fn]);
			float vc = Float.parseFloat(fieldValue);
			res = Math.abs(vt-vc)<Math.abs(vt+vc)*1e-6;
		}
		if(tab.fieldTypes[fn]==VDataTable.STRING){
			res = fieldValue.compareTo(row[fn]) == 0;
		}
	break;
        case NOT_EQUAL:
		if(tab.fieldTypes[fn]==VDataTable.NUMERICAL){
			float vt = Float.parseFloat(row[fn]);
			float vc = Float.parseFloat(fieldValue);
			res = Math.abs(vt-vc)>Math.abs(vt+vc)*1e-6;
		}
		if(tab.fieldTypes[fn]==VDataTable.STRING){
			res = !(fieldValue.compareTo(row[fn])==0);
		}
	break;
        case INCLUDE:
		if(tab.fieldTypes[fn]==VDataTable.STRING){
			res = fieldValue.indexOf(row[fn])>=0;
		}
	break;
        case NOT_INCLUDE:
		if(tab.fieldTypes[fn]==VDataTable.STRING){
			res = fieldValue.indexOf(row[fn])<0;
		}
	break;
        }
	}
	break;
case RANDOM:
if(rnd.nextFloat()>1-probability) res=true;
break;
}
return res;
}

}
