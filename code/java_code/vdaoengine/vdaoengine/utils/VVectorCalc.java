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
import java.util.*;

public class VVectorCalc {

public static int distanceType = 0; // 0 - Euclidean 1 - l1
public static int EUCLIDEAN_DISTANCE = 0;
public static int L1_DISTANCE = 1;

  public VVectorCalc() {
  }

  
  public static void Add(double V1[],double V2[]){
  for(int i=0; i<V1.length; i++)
    V1[i]+=V2[i];
  }

  public static void Add(double V1[],float V2[]){
  for(int i=0; i<V1.length; i++)
    V1[i]+=V2[i];
  }

  public static void Add(float V1[],float V2[]){
	  for(int i=0; i<V1.length; i++)
	    V1[i]+=V2[i];
	  }


  public static void Subtr(double V1[],double V2[]){
  for(int i=0; i<V1.length; i++)
    V1[i]-=V2[i];
  }

  public static void Subtr(double V1[],float V2[]){
  for(int i=0; i<V1.length; i++)
    V1[i]-=V2[i];
  }

  public static void Subtr(float V1[],float V2[]){
  for(int i=0; i<V1.length; i++)
    V1[i]-=V2[i];
  }

  public static void Mult(double V1[],double V2){
  for(int i=0; i<V1.length; i++)
    V1[i]*=V2;
  }

  public static void Mult(float V1[],float V2){
  for(int i=0; i<V1.length; i++)
    V1[i]*=V2;
  }
  
  public static float[] Mult_(float V1[],float V2){
  float res[] = new float[V1.length];
  for(int i=0; i<V1.length; i++)
    res[i] = V1[i]*V2;
  return res;
  }

  public static float ScalarMult(float V1[],float V2[]){
    float r=0;
  for(int i=0; i<V1.length; i++)
    r+=V1[i]*V2[i];
    return r;
  }
  
  public static float ScalarMultGap(float V1[],float V2[]){
	    float r=0;
	  for(int i=0; i<V1.length; i++)
		if((!Float.isNaN(V1[i]))&&(!Float.isNaN(V2[i])))
			r+=V1[i]*V2[i];
	    return r;
	  }
  

  public static float ScalarMult(float V1[],double V2[]){
    float r=0;
  for(int i=0; i<V1.length; i++)
    r+=V1[i]*V2[i];
    return r;
  }


  public static double[] randomUnit(int dimen){
  double res[] = new double[dimen];
  for(int i=0;i<dimen;i++)
    res[i] = Math.random();
  Normalize(res);
  return res;
  }

  public static double[] randomUnitAllSpace(int dimen){
  double res[] = new double[dimen];
  for(int i=0;i<dimen;i++)
    res[i] = Math.random()-0.5;
  Normalize(res);
  return res;
  }

  public static float[] randomUnitAllSpacef(int dimen){
	  float res[] = new float[dimen];
	  for(int i=0;i<dimen;i++)
	    res[i] = (float)(Math.random()-0.5);
	  Normalize(res);
	  return res;
	  }
  

  public static double[] random(int dimen){
  double res[] = new double[dimen];
  for(int i=0;i<dimen;i++)
    res[i] = Math.random();
  return res;
  }

  public static float[] randomf(int dimen){
	  float res[] = new float[dimen];
	  for(int i=0;i<dimen;i++)
	    res[i] = (float)Math.random();
	  return res;
	  }
  

  public static double[] nulVector(int dimen){
  return new double[dimen];
  }

  public static float[] nulVectorF(int dimen){
  return new float[dimen];
  }


  public static double Norm(double V1[]){
  return Math.sqrt(SquareDistance(V1,nulVector(V1.length)));
  }

  public static double Norm(float V1[]){
  return Math.sqrt(SquareDistance(V1,nulVectorF(V1.length)));
  }


  public static void Normalize(double V1[]){
  Mult(V1,1.0/Norm(V1));
  }

  public static void Normalize(float V1[]){
  Mult(V1,(float)(1.0/Norm(V1)));
  }


  public static double Distance(double V1[],double V2[]){
  double res = 0;
  for(int i=0; i<V1.length; i++)
  switch(distanceType)
  {case 0: res +=(V1[i]-V2[i])*(V1[i]-V2[i]); break;
   case 1: res +=Math.abs(V1[i]-V2[i]); break;
  }
  switch(distanceType)
  {
  case 0: res = java.lang.Math.sqrt(res); break;
  case 1: res = res; break;
  }
  return res;
  }

  public static double Distance(double V1[],float V2[]){
  double res = 0;
  for(int i=0; i<V1.length; i++)
  switch(distanceType)
  {case 0: res +=(V1[i]-V2[i])*(V1[i]-V2[i]); break;
   case 1: res +=Math.abs(V1[i]-V2[i]); break;
  }
  switch(distanceType)
  {
  case 0: res = java.lang.Math.sqrt(res); break;
  case 1: res = res; break;
  }
  return res;
  }

  public static double Distance(float V1[],float V2[]){
  double res = 0;
  for(int i=0; i<V1.length; i++)
  switch(distanceType)
  {case 0: res +=(V1[i]-V2[i])*(V1[i]-V2[i]); break;
   case 1: res +=Math.abs(V1[i]-V2[i]); break;
  }
  switch(distanceType)
  {
  case 0: res = java.lang.Math.sqrt(res); break;
  case 1: res = res; break;
  }
  return res;
  }


  public static double SquareDistance(double V1[],double V2[]){
  double res = 0;
  for(int i=0; i<V1.length; i++)
  switch(distanceType)
  {case 0: res +=(V1[i]-V2[i])*(V1[i]-V2[i]); break;
   case 1: res +=Math.abs(V1[i]-V2[i]); break;
  }
  switch(distanceType)
  {
  case 0: res = res; break;
  case 1: res = res; break;
  }
  return res;
  }

  public static double SquareDistance(double V1[],float V2[]){
  double res = 0;
  for(int i=0; i<V1.length; i++)
  switch(distanceType)
  {case 0: res +=(V1[i]-V2[i])*(V1[i]-V2[i]); break;
   case 1: res +=Math.abs(V1[i]-V2[i]); break;
  }
  switch(distanceType)
  {
  case 0: res = res; break;
  case 1: res = res*res; break;
  }
  return res;
  }

  public static float SquareDistance(float V1[],float V2[]){
  float res = 0;
  for(int i=0; i<V1.length; i++)
  switch(distanceType)
  {case 0: res +=(V1[i]-V2[i])*(V1[i]-V2[i]); break;
   case 1: res +=Math.abs(V1[i]-V2[i]); break;
  }
  switch(distanceType)
  {
  case 0: res = res; break;
  case 1: res = res*res; break;
  }
  return res;
  }

  public static float SquareEuclDistanceShift(float v1[],int shift,float v2[],int dimen){
  float s = 0f;
  int i = 1;
  float sf = 0f;
  for(i=0;i<dimen;i++) {
          sf = v1[i+shift]-v2[i];
          s+= sf*sf;
  }
  return s;
  }

  public static float SquareEuclDistanceShiftGap(float v1[],int shift,float v2[],int dimen){
  float s = 0f;
  int i = 1;
  float sf = 0f;
  for(i=0;i<dimen;i++) if(!Float.isNaN(v2[i])) {
          sf = v1[i+shift]-v2[i];
          s+= sf*sf;
  }
  return s;
  }
/**************************************************************************************************/
//added to file
   public static float VecSquareEuclDistanceShift(Vector v1,int shift,float v2[],int dimen){
  float s = 0f;
  int i = 1;
  float vec[] = ((float[])v1.elementAt(shift)); ///? copy the elements ?
  float sf = 0f;
  for(i=0;i<dimen;i++) {
          sf = vec[i]-v2[i];
          s+= sf*sf;
  }
  return s;
  }
  /************************************************************************************************/
  public static float SquareEuclDistance(float v1[],float v2[]){
  float s = 0;
  for(int i=0;i<v1.length;i++) s+=(v1[i]-v2[i])*(v1[i]-v2[i]);
  return s;
  }

  public static float SquareEuclDistanceGap(float v1[],float v2[]){
  float s = 0;
  for(int i=0;i<v1.length;i++) if(!Float.isNaN(v1[i])) if(!Float.isNaN(v2[i]))
    s+=(v1[i]-v2[i])*(v1[i]-v2[i]);
  return s;
  }


  public static float[] projectOnSegment(float point[], float p1[], float p2[]){
          float m = 0, s = 0;
          float u = 0f;
          int dimen = point.length;
          u = 0;
          for(int i=0;i<dimen;i++){
                  m = p2[i]-p1[i];
                  u+=(point[i]-p1[i])*m;
                  s+=m*m;
          }
          u/=s;
          if(u<0) u = 0;
          if(u>1) u = 1;
          s = 0;
          for(int i=0;i<dimen;i++){
                  s+=(point[i]-(p1[i]+u*(p2[i]-p1[i])));
          }

          float resv[] = new float[2];
          resv[0] = u;
          resv[1] = s;
          return resv;

  }

  public static float[] projectOnSegmentGap(float point[], float p1[], float p2[]){
          float m = 0, s = 0;
          float u = 0f;
          int dimen = point.length;
          u = 0;
          for(int i=0;i<dimen;i++) if(!Float.isNaN(point[i])) {
                  m = p2[i]-p1[i];
                  u+=(point[i]-p1[i])*m;
                  s+=m*m;
          }
          u/=s;
          if(u<0) u = 0;
          if(u>1) u = 1;
          s = 0;
          for(int i=0;i<dimen;i++) if(!Float.isNaN(point[i])) {
                  s+=(point[i]-(p1[i]+u*(p2[i]-p1[i])));
          }

          float resv[] = new float[2];
          resv[0] = u;
          resv[1] = s;
          return resv;

  }


  public static float[] coordinatesInBasis2D(float point[], float p1[], float p2[], float p3[]){
          float m = 0, n = 0, mn = 0, m2 = 0, n2 = 0, xrm = 0, xrn = 0, den = 0;
          int dimen = point.length;
          float u = 0f; float v = 0f;
          for(int i=0;i<dimen;i++){
                  m = p2[i] - p1[i];
                  n = p3[i] - p1[i];
                  mn+=m*n;
                  m2+=m*m;
                  n2+=n*n;
                  xrm+=(point[i]-p1[i])*m;
                  xrn+=(point[i]-p1[i])*n;
          }
          den=m2*n2-mn*mn;
          if(den<1e-8f) den=1e-8f;
          u = (xrm*n2-mn*xrn)/den;
          v = (xrn*m2-mn*xrm)/den;
          float resv[] = new float[2];
          resv[0] = u;  resv[1] = v;
          return resv;
  }


  public static float[] projectOnTriangle(float point[], float p1[], float p2[], float p3[]){
   float res = 0, m = 0, n = 0, mn = 0, m2 = 0, n2 = 0, den = 0, xrn =0, xrm = 0, tmp = 0;
   float u1 = 0, u2 = 0, u3 = 0;
   float u = 0f, v = 0f;
   float resv[] = new float[3];
   int dimen = point.length;
   for(int i=0;i<dimen;i++){
           m = p2[i] - p1[i];
           n = p3[i] - p1[i];
           mn+=m*n;
           m2+=m*m;
           n2+=n*n;
           xrn+=(point[i]-p1[i])*n;
           xrm+=(point[i]-p1[i])*m;
   }
   den=m2*n2-mn*mn;
   if(den<1e-8f) den = 1e-8f;
   u=(xrm*n2-mn*xrn)/den;
   v=(xrn*m2-mn*xrm)/den;
   if((u>=0)&&(v>=0)&&((u+v)<=1)){
     res=0;
   }else{
          float rr1[] = projectOnSegment(point,p1,p2);
          u1 = rr1[0]; float r1 = rr1[1];
          float rr2[] = projectOnSegment(point,p1,p3);
          u2 = rr2[0]; float r2 = rr2[1];
          float rr3[] = projectOnSegment(point,p3,p2);
          u3 = rr3[0]; float r3 = rr3[1];
          if(Math.min(r1,Math.min(r2,r3))==r1){
        v=0; u=u1;
          }else if(Math.min(r1,Math.min(r2,r3))==r2){
        u=0; v=u2;
          } else if(Math.min(r1,Math.min(r2,r3))==r3){
            float pt[] = new float[dimen];
            for(int i=0;i<dimen;i++) pt[i] = (p3[i] - p2[i])*u3;
            float uv[] = coordinatesInBasis2D(pt,p1,p2,p3);
            u = uv[0]; v = uv[1];
      }
          res = 0;
          for(int i=0;i<dimen;i++){
                  tmp =(point[i] - (p1[i] + (p2[i]-p1[i])*u + (p3[i]-p1[i])*v));
                  res+=tmp*tmp;
          }
   }
   resv[0] = u;
   resv[1] = v;
   resv[2] = res;
   return resv;
  }

  public static float[] projectOnTriangleGap(float point[], float p1[], float p2[], float p3[]){
   float res = 0, m = 0, n = 0, mn = 0, m2 = 0, n2 = 0, den = 0, xrn =0, xrm = 0, tmp = 0;
   float u1 = 0, u2 = 0, u3 = 0;
   float u = 0f, v = 0f;
   float resv[] = new float[3];
   int dimen = point.length;
   for(int i=0;i<dimen;i++){
           m = p2[i] - p1[i];
           n = p3[i] - p1[i];
           mn+=m*n;
           m2+=m*m;
           n2+=n*n;
           if(!Float.isNaN(point[i])){
             xrn+=(point[i]-p1[i])*n;
             xrm+=(point[i]-p1[i])*m;
           }
   }
   den=m2*n2-mn*mn;
   if(den<1e-8f) den = 1e-8f;
   u=(xrm*n2-mn*xrn)/den;
   v=(xrn*m2-mn*xrm)/den;
   if((u>=0)&&(v>=0)&&((u+v)<=1)){
     res=0;
   }else{
          float rr1[] = projectOnSegmentGap(point,p1,p2);
          u1 = rr1[0]; float r1 = rr1[1];
          float rr2[] = projectOnSegmentGap(point,p1,p3);
          u2 = rr2[0]; float r2 = rr2[1];
          float rr3[] = projectOnSegmentGap(point,p3,p2);
          u3 = rr3[0]; float r3 = rr3[1];
          if(Math.min(r1,Math.min(r2,r3))==r1){
        v=0; u=u1;
          }else if(Math.min(r1,Math.min(r2,r3))==r2){
        u=0; v=u2;
          } else if(Math.min(r1,Math.min(r2,r3))==r3){
            float pt[] = new float[dimen];
            for(int i=0;i<dimen;i++) pt[i] = (p3[i] - p2[i])*u3;
            float uv[] = coordinatesInBasis2D(pt,p1,p2,p3);
            u = uv[0]; v = uv[1];
      }
          res = 0;
          for(int i=0;i<dimen;i++) if(!Float.isNaN(point[i])) {
                  tmp =(point[i] - (p1[i] + (p2[i]-p1[i])*u + (p3[i]-p1[i])*v));
                  res+=tmp*tmp;
          }
   }
   resv[0] = u;
   resv[1] = v;
   resv[2] = res;
   return resv;
  }

  public static float[] Add_(float V1[],float V2[]){
  float res[] = new float[V1.length];
  for(int i=0; i<V1.length; i++)
    res[i]=V1[i]+V2[i];
  return res;
  }
  public static double[] Add_(double V1[],double V2[]){
	  double res[] = new double[V1.length];
	  for(int i=0; i<V1.length; i++)
	    res[i]=V1[i]+V2[i];
	  return res;
	  }  
  public static float[] Subtract_(float V1[],float V2[]){
  float res[] = new float[V1.length];
  for(int i=0; i<V1.length; i++)
    res[i]=V1[i]-V2[i];
  return res;
  }
  public static double[] Subtract_(double V1[],double V2[]){
	  double res[] = new double[V1.length];
	  for(int i=0; i<V1.length; i++)
	    res[i]=V1[i]-V2[i];
	  return res;
	  }
  public static float[] Product_(float V1[],float V2){
  float res[] = new float[V1.length];
  for(int i=0; i<V1.length; i++)
    res[i]=V1[i]*V2;
  return res;
  }
  public static double[] Product_(double V1[],double V2){
	  double res[] = new double[V1.length];
	  for(int i=0; i<V1.length; i++)
	    res[i]=V1[i]*V2;
	  return res;
  }


}