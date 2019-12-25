package vdaoengine.analysis;

/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.*;
import vdaoengine.data.*;
import vdaoengine.utils.*;
import vdaoengine.visproc.*;
import java.util.Vector;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Iterator;


public class VHistogram {

public final static int  BASIC_HISTOGRAM = 1;
public final static int  LABELED_HISTOGRAM = 2;
public final static int  LABELED_ERR_HISTOGRAM = 4;
public final static int  LABELED_ERR_MIN_MAX_HISTOGRAM = 8;

public int firstLabelSymbols = -1;
public boolean checkLabelsForInclusion = true;

public Vector Labels;
public Vector AltLabels;
public Vector Bars;
public Vector ColNames;
public Vector Borders;
public Vector FieldNames;
public VClassifier classifier = null;
public int type = -1;

public VHistogram(){
Labels = new Vector();
AltLabels = new Vector();
Bars = new Vector();
Borders =new Vector();
ColNames = new Vector();
}

public void setBorders(float st, float en, int n){
Borders.clear();
float wid = (en-st)/n;
for(int i=0;i<n;i++){
  float b[] = new float[2];
  b[0] = st+wid*(float)i;
  b[1] = st+wid*(float)i+wid;
  Borders.addElement(b);
}
}

public int barNumber(float x){
int res = -1;
for(int i=0;i<Borders.size();i++){
  float b[] =(float[])(Borders.elementAt(i));
  if((x>=b[0])&&(x<b[1])) res=i;
  //if((i==Borders.size()-1)&&(Math.abs(x-b[1])<Math.abs(b[1])*1e-5)) res=i;
  if((i==Borders.size()-1)&&(x==b[1])) res=i;
}
if(res==-1){
  float b1 = ((float[])(Borders.elementAt(0)))[0];
  float b2 = ((float[])(Borders.elementAt(Borders.size()-1)))[1];
  //System.out.println("WARNING: The value "+x+" is not in the diapason ["+b1+";"+b2+"]");
  if(x<=b1) res = 0;
  if(x>=b2) res = Borders.size()-1;
}
return res;
}

public int[] barNumber(String labl){
Vector v = new Vector();
try{
for(int i=0;i<Labels.size();i++){
  if(firstLabelSymbols!=-1)
     if(labl.length()>firstLabelSymbols)
      labl = labl.substring(0,firstLabelSymbols+1);
  String s = (String)(Labels.elementAt(i));
  if(s.compareTo(labl)==0) v.addElement(new Integer(i));
  else
  if (checkLabelsForInclusion)
    { //if((labl.indexOf(";")>0)&&(labl.indexOf(s)>0)){
        if(labl.indexOf(s)>=0){
              v.addElement(new Integer(i));
        //if(labl.indexOf("S03")>=0)
        //    System.out.println(labl+" "+s);
        //labl = (String)Labels.elementAt(i);
        }
     //if(labl.indexOf(";")==0)
    }

}}catch(Exception e){}
int res[] = new int[v.size()];
for(int i=0;i<v.size();i++) res[i]=((Integer)(v.elementAt(i))).intValue();
//for(int i=0;i<res.length;i++) System.out.print(res[i]+" ");
//System.out.println();
return res;
}

public void addValue(float value, int nvalue){
  int k = barNumber(value);
  if(k>=0){
    float f[] = (float[])Bars.get(k);
    f[nvalue]+=1.0f;
  }
}

public void setBordersMinMax(VDataTable tab, int n, Vector fields){
int fn[] = new int[fields.size()];
for(int i=0;i<fn.length;i++) fn[i]=tab.fieldNumByName((String)(fields.elementAt(i)));
float mn = Float.MAX_VALUE;
float mx = -Float.MAX_VALUE;
float v=0;
for(int i=0;i<fn.length;i++)
  for(int j=0;j<tab.rowCount;j++){
  String s = tab.getV(j,fn[i]);
   if(!s.equals("")&&(!s.equals("NaN")))
    {
    v = Float.parseFloat(tab.getV(j,fn[i]));
    if(v<mn) mn=v;
    if(v>mx) mx=v;
    }}
float diap = mx-mn;
mn = mn - diap*0.001f;
mx = mx + diap*0.001f;
setBorders(mn,mx,n);
}

public void initBars(int nvalues){
  int n = Borders.size();
  for(int i=0;i<n;i++){
    float b[] = new float[nvalues];
    Bars.add(b);
    StringBuffer s = new StringBuffer();
    s.append(barPosition(i));
    Labels.add(s.toString());
  }
}

public float barPosition(int n){
float res = 0;
float a = ((float [])(Borders.elementAt(n)))[0];
float b = ((float [])(Borders.elementAt(n)))[1];
return (a+b)/2;
}

public void MakeBasicHistogram(VDataTable tab, Vector fields){
int fn[] = new int[fields.size()];
ColNames.clear();
for(int i=0;i<fn.length;i++) {
  fn[i]=tab.fieldNumByName((String)(fields.elementAt(i)));
  ColNames.add((String)(fields.elementAt(i)));
}
Bars.clear();
Labels.clear();
int n = Borders.size();
for(int i=0;i<n;i++){
  float b[] = new float[fields.size()];
  Bars.add(b);
  StringBuffer s = new StringBuffer();
  s.append(barPosition(i));
  Labels.add(s.toString());
}
float v=0;
for(int j=0;j<tab.rowCount;j++)
   for(int i=0;i<fn.length;i++)
   if(!tab.getV(j,fn[i]).equals(""))
    {
    v = Float.parseFloat(tab.getV(j,fn[i]));
    int k = barNumber(v);
    if(k!=-1){
       ((float [])(Bars.elementAt(k)))[i]+=1.0f;
    }
    }
type = BASIC_HISTOGRAM;
}

public void MakeHistogramBasedOnLabelField(VDataTable tab, String labelField, String altField){
int lablFn = tab.fieldNumByName(labelField);
int altLablFn = tab.fieldNumByName(altField);
Bars.clear();
Labels.clear();
AltLabels.clear();
ColNames.clear();
ColNames.addElement(new String(labelField));
if(lablFn!=-1)
	for(int j=0;j<tab.rowCount;j++){
		String s = tab.getV(j,lablFn);
		int k[] = barNumber(s);
		if(k.length==0){
                        if(checkLabelsForInclusion){
                        StringTokenizer st = new StringTokenizer(s,";");
                        while(st.hasMoreTokens())
                           {
                           float b[] = new float[1];
                           Bars.addElement(b);
                           Labels.addElement(st.nextToken());
                           if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));
                           }
                        }else
			{float b[] = new float[1];
			Bars.addElement(b);
                        Labels.addElement(s);
                        if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));}
			k = barNumber(s);
		}
               for(int y=0;y<k.length;y++)
        	       ((float [])(Bars.elementAt(k[y])))[0]+=1.0f;
	}
type = LABELED_HISTOGRAM;
}

public void MakeAverageForLabelHistogram(VDataTable tab, Vector fields, String labelField, String altLabelField){
//System.out.println("In the proc...");
int lablFn = tab.fieldNumByName(labelField);
int altLablFn = tab.fieldNumByName(altLabelField);
Bars.clear();
Labels.clear();
AltLabels.clear();
ColNames.clear();
FieldNames = fields;
int fn[] = new int[fields.size()];
for(int i=0;i<fields.size();i++) {
   ColNames.addElement((String)(fields.elementAt(i))+"_MX");
   ColNames.addElement((String)(fields.elementAt(i))+"_MN");
   ColNames.addElement((String)(fields.elementAt(i))+"_AV");
   fn[i]=tab.fieldNumByName((String)(fields.elementAt(i)));
   }
if(lablFn!=-1){
	for(int j=0;j<tab.rowCount;j++){
		String s = tab.getV(j,lablFn);

                if(checkLabelsForInclusion){
                StringTokenizer st = new StringTokenizer(s,";");
                //System.out.println(s);
                while(st.hasMoreTokens())
                   {
                        String ss = st.nextToken();
                        //if(s.indexOf("S03")>=0){
                        //  System.out.println(s+" "+ss);
                        //}
                        if(firstLabelSymbols!=-1)
                         if(ss.length()>firstLabelSymbols)
                           ss = ss.substring(0,firstLabelSymbols+1);
                        if(Labels.indexOf(ss)<0){
                          //for(int ii=0;ii<Labels.size();ii++)
                          //     System.out.print("\""+Labels.elementAt(ii)+"\" ");
                          //System.out.println("include \""+ss+"\"");
                          float b[] = new float[3*fields.size()];
                          Bars.addElement(b);
                          Labels.addElement(ss);
                          if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));
                        }
                      }
                }

		int k[] = barNumber(s);

		if(k.length==0){
                        if(checkLabelsForInclusion){
                        //System.out.println("check = "+checkLabelsForInclusion);
                        StringTokenizer st = new StringTokenizer(s,";");
                        while(st.hasMoreTokens())
                           {
        			float b[] = new float[3*fields.size()];
	        		Bars.addElement(b);
                                String ss = st.nextToken();
                                if(s.indexOf("S03")>=0){
                                  System.out.println(s+" "+ss);
                                }
                                if(firstLabelSymbols!=-1)
                                 if(ss.length()>firstLabelSymbols)
                                   ss = ss.substring(0,firstLabelSymbols+1);
                                //if(Labels.indexOf(s)==0){
                                  for(int ii=0;ii<Labels.size();ii++)
                                       System.out.print("\""+Labels.elementAt(ii)+"\" ");
                                  System.out.println("include \""+ss+"\"");
                                  Labels.addElement(ss);
                                //}
                                if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));
                           }
                        }else
			{
			float b[] = new float[3*fields.size()];
			Bars.addElement(b);
                        if(firstLabelSymbols!=-1)
                         if(s.length()>firstLabelSymbols)
                           s = s.substring(0,firstLabelSymbols+1);
			Labels.addElement(s);
                        if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));
                        }
			k = barNumber(s);
		}
                for(int y=0;y<k.length;y++)
         		for(int i=0;i<fields.size();i++) {
	        		float v = Float.parseFloat(tab.getV(j,fn[i]));
	                	((float [])(Bars.elementAt(k[y])))[i*3]+=v;
	        	        ((float [])(Bars.elementAt(k[y])))[i*3+1]+=v*v;
        	        	((float [])(Bars.elementAt(k[y])))[i*3+2]+=1;
		}
	}
for(int j=0;j<Bars.size();j++){
   	float b[] =(float[])(Bars.elementAt(j));
	for(int i=0;i<fields.size();i++){
		float n = ((float [])(Bars.elementAt(j)))[i*3+2];
		float av = ((float [])(Bars.elementAt(j)))[i*3]/n;
		float av2 = ((float [])(Bars.elementAt(j)))[i*3+1]/n;
		float sig = (float)Math.sqrt(av2-av*av+1e-5);
		((float [])(Bars.elementAt(j)))[i*3]=av+sig;
		((float [])(Bars.elementAt(j)))[i*3+1]=av-sig;
		((float [])(Bars.elementAt(j)))[i*3+2]=av;
	}
}
}
type = LABELED_ERR_HISTOGRAM;
}

public void MakeMinMaxForLabelHistogram(VDataTable tab, Vector fields, String labelField, String altLabelField){
int lablFn = tab.fieldNumByName(labelField);
int altLablFn = tab.fieldNumByName(altLabelField);
Bars.clear();
Labels.clear();
AltLabels.clear();
ColNames.clear();
FieldNames = fields;
int fn[] = new int[fields.size()];
for(int i=0;i<fields.size();i++) {
   ColNames.addElement((String)(fields.elementAt(i))+"_MX");
   ColNames.addElement((String)(fields.elementAt(i))+"_MN");
   ColNames.addElement((String)(fields.elementAt(i))+"_AV");
   fn[i]=tab.fieldNumByName((String)(fields.elementAt(i)));
   }
if(lablFn!=-1){
	for(int j=0;j<tab.rowCount;j++){
		String s = tab.getV(j,lablFn);
		int k[] = barNumber(s);
		if(k.length==0){

                        if(checkLabelsForInclusion){
                        StringTokenizer st = new StringTokenizer(s,";");
                        while(st.hasMoreTokens())
                           {
        			float b[] = new float[4*fields.size()];
                                for(int jj=0;jj<fields.size();jj++) b[4*jj+2]=Float.MAX_VALUE;
                                for(int jj=0;jj<fields.size();jj++) b[4*jj+3]=-Float.MAX_VALUE;
	        		Bars.addElement(b);
                                s = st.nextToken();
                                if(firstLabelSymbols!=-1)
                                 if(s.length()>firstLabelSymbols)
                                   s = s.substring(0,firstLabelSymbols+1);
        			Labels.addElement(s);
                                if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));
                           }
                        }else
			{
			float b[] = new float[4*fields.size()];
                        for(int jj=0;jj<fields.size();jj++) b[4*jj+2]=Float.MAX_VALUE;
                        for(int jj=0;jj<fields.size();jj++) b[4*jj+3]=-Float.MAX_VALUE;

			Bars.addElement(b);
                        if(firstLabelSymbols!=-1)
                         if(s.length()>firstLabelSymbols)
                           s = s.substring(0,firstLabelSymbols+1);
			Labels.addElement(s);
                        if(altLablFn!=-1) AltLabels.addElement(tab.getV(j,altLablFn));
                        }
			k = barNumber(s);

		}
                for(int y=0;y<k.length;y++)
         		for(int i=0;i<fields.size();i++) {
	        		float v = Float.parseFloat(tab.getV(j,fn[i]));
	                	((float [])(Bars.elementAt(k[y])))[i*4]+=v;
	        	        ((float [])(Bars.elementAt(k[y])))[i*4+1]+=1;
                                if(v<((float [])(Bars.elementAt(k[y])))[i*4+2])
                	        	((float [])(Bars.elementAt(k[y])))[i*4+2]=v;
                                if(v>((float [])(Bars.elementAt(k[y])))[i*4+3])
                	        	((float [])(Bars.elementAt(k[y])))[i*4+3]=v;
		}
	}
for(int j=0;j<Bars.size();j++){
   	float b[] =(float[])(Bars.elementAt(j));
	for(int i=0;i<fields.size();i++){
		float n = ((float [])(Bars.elementAt(j)))[i*4+1];
		float av = ((float [])(Bars.elementAt(j)))[i*4]/n;
		float mn = ((float [])(Bars.elementAt(j)))[i*4+2];
		float mx = ((float [])(Bars.elementAt(j)))[i*4+3];
		((float [])(Bars.elementAt(j)))[i*4]=mx;
		((float [])(Bars.elementAt(j)))[i*4+1]=mn;
		((float [])(Bars.elementAt(j)))[i*4+2]=av;
	}
}
}
type = LABELED_ERR_MIN_MAX_HISTOGRAM;
}


public void sortLabels(){
for(int i=Labels.size()-2;i>=0;i--)
 for(int j=0;j<=i;j++){
    String si = (String)(Labels.elementAt(j));
    String sj = (String)(Labels.elementAt(j+1));
    if(si.compareTo(sj)>0){
      Labels.set(j,sj);
      Labels.set(j+1,si);
      float f1[] = (float[])(Bars.elementAt(j));
      float f2[] = (float[])(Bars.elementAt(j+1));
      Bars.set(j,f2);
      Bars.set(j+1,f1);
      }
    }
}

public String toString(){
StringBuffer s = new StringBuffer();
s.append("Label \t");
if(AltLabels.size()==Labels.size()) s.append("Alt_Label \t");
for(int i=0;i<ColNames.size();i++) s.append((String)(ColNames.elementAt(i))+"\t");
s.append("\n");
for(int i=0; i<Bars.size();i++){
  s.append((String)(Labels.elementAt(i))+"\t");
  if(AltLabels.size()==Labels.size()) s.append((String)(AltLabels.elementAt(i))+"\t");
  float b[] = (float[])(Bars.elementAt(i));
  for(int j=0;j<b.length;j++) s.append(b[j]+"\t");
  s.append("\n");
}
return s.toString();
}

public float[] getMean(){
  float f[] = new float[Bars.size()];
  float sum[] = new float[Bars.size()];
  for(int i=0;i<Bars.size();i++){
    float v[] = (float[])Bars.get(i);
    for(int k=0;k<v.length;k++) sum[k]+=v[k];
    for(int k=0;k<v.length;k++) f[k]+=v[k]*barPosition(i);
  }
  for(int k=0;k<f.length;k++) f[k]/=sum[k];
  return f;
}

public float[] getStdDev(){
  float f[] = new float[Bars.size()];
  float f2[] = new float[Bars.size()];
  float sum[] = new float[Bars.size()];
  for(int i=0;i<Bars.size();i++){
    float v[] = (float[])Bars.get(i);
    for(int k=0;k<v.length;k++) sum[k]+=v[k];
    for(int k=0;k<v.length;k++) f[k]+=v[k]*barPosition(i);
    for(int k=0;k<v.length;k++) f2[k]+=v[k]*barPosition(i)*barPosition(i);
  }
  for(int k=0;k<f.length;k++) f[k]/=sum[k];
  for(int k=0;k<f.length;k++) f[k]=(float)Math.sqrt(f2[k]/sum[k]-f[k]*f[k]);
  return f;
}

public float[] getRightPValue(float x){
  float f[] = new float[Bars.size()];
  float sum[] = new float[Bars.size()];
  for(int i=0;i<Bars.size();i++){
    float xb = barPosition(i);
    float v[] = (float[])Bars.get(i);
    for(int k=0;k<v.length;k++) sum[k]+=v[k];
    if(xb>x){
      for(int k=0;k<v.length;k++) f[k]+=v[k];
    }
  }
  for(int k=0;k<f.length;k++) f[k]/=sum[k];
  return f;
}


}
