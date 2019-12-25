package vdaoengine.utils;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.lang.Math.*;
import java.util.Vector;

public class VLinearFunction implements VMathFunction {

int dimension = 0;
double a0;
double[] coeff;

public VLinearFunction(){

}

public VLinearFunction(int n){
setDimension(n);
}

public void setDimension(int n){
dimension = n;
coeff = new double[n];
}

public int getDimension(int n){
return n;
}

public void set(double Centr, double[] Coeff){
for(int i=0;i<dimension;i++) coeff[i] = Coeff[i];
a0 = Centr;
}

public double[] getCoeff(){
return coeff;
}

public double getShift(){
return a0;
}


public double calculate(float[] vec){
double res = 0;
for(int i=0;i<dimension;i++) if(!Double.isNaN(vec[i])) res+=(double)vec[i]*coeff[i];
return (res+a0);
}

public double calculate(double[] vec){
double res = 0;
for(int i=0;i<dimension;i++) res+=vec[i]*coeff[i];
return (res+a0);
}

public float getValue(float x, float parameters[]){
	return x;
}
public float getValue(float x){
	return x;
}


}