package vdaoengine.utils;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.analysis.elmap.*;
import vdaoengine.visproc.*;

import java.util.*;
import java.math.*;

public class VFunctionCalculator {

  public static double VCalculateDensity(VDataSet D, float dev, VFunction fi){
    double smax = 0;
    fi.values.clear();
    VStatistics st = D.calcStatistics();
    float d2 = st.totalDispersion*st.totalDispersion/10f;
    for(int i=0;i<fi.points.size();i++){
      if(Math.round(i/1000)*1000==i) System.out.print(i+" ");
      float p[] = (float[])fi.points.elementAt(i);
      double s = 0;
      for(int j=0;j<D.pointCount;j++){
        float x[] = D.getVector(j);
        float dist = VVectorCalc.SquareDistance(p,x);
        dist/=d2;
        dist = dist/(dev*dev);
        s = s+Math.exp(-(double)dist);
      }
      if(s>smax) smax = s;
      fi.values.add(new Float(s));
    }
    System.out.println();
    for(int i=0;i<fi.values.size();i++){
      Float x = (Float)fi.values.elementAt(i);
      fi.values.setElementAt(new Float(x.floatValue()/smax),i);
    }
    return smax;
  }


  public static double VCalculateDensityContrasted(VDataSet D, float dev1, float dev2, float thresh,VFunction fi){
    double smax = 0;
    double smin = Double.MAX_VALUE;
    fi.values.clear();
    VStatistics st = D.calcStatistics();
    float d2 = st.totalDispersion*st.totalDispersion/10f;
    for(int i=0;i<fi.points.size();i++){
      if(Math.round(i/1000)*1000==i) System.out.print(i+" ");
      float p[] = (float[])fi.points.elementAt(i);
      double s1 = 0;
      double s2 = 0;
      for(int j=0;j<D.pointCount;j++){
        float x[] = D.getVector(j);
        float dist = VVectorCalc.SquareDistance(p,x);
        dist/=d2;
        float dist1 = dist/(dev1*dev1);
        s1 = s1+Math.exp(-(double)dist1);
        float dist2 = dist/(dev2*dev2);
        s2 = s2+Math.exp(-(double)dist2);
        }
      float rat = 0;
      if(s2>1e-10) rat = (float)(s1/s2);
      if(s1/D.pointCount<thresh) rat = 0;
      //if(rat<1e-10f) rat = 1e-10f;
      //rat = (float)Math.log(rat);

      if(rat>smax) smax = rat;
      if(rat<smin) smin = rat;
      fi.values.add(new Float(rat));
    }
    System.out.println();
    for(int i=0;i<fi.values.size();i++){
      Float x = (Float)fi.values.elementAt(i);
      fi.values.setElementAt(new Float((x.floatValue()-smin)/(smax-smin)),i);
    }
    return smax;
  }


  public static double VCalculateDensityND(VDataSet D, VProjection proj, float dev, VFunction fi){
    double smax = 0;
    fi.values.clear();
    VStatistics st = D.calcStatistics();
    float d2 = st.totalDispersion*st.totalDispersion/10f;
    for(int i=0;i<fi.points.size();i++){
      float p[] = (float[])fi.points.elementAt(i);
      double s = 0;
      float pND[] = proj.projectFromInToOut(p);
      for(int j=0;j<D.pointCount;j++){
        float x[] = D.getVector(j);
        float dist = VVectorCalc.SquareDistance(pND,x);
        dist/=d2;
        dist = dist/(dev*dev);
        s = s+Math.exp(-(double)dist);
      }
      if(s>smax) smax = s;
      fi.values.add(new Float(s));
    }
    for(int i=0;i<fi.values.size();i++){
      Float x = (Float)fi.values.elementAt(i);
      fi.values.setElementAt(new Float(x.floatValue()/smax),i);
    }
    return smax;
  }


  public static double VCalculateDensityClass(VDataSet D, VTableSelector sel, float dev, VFunction fi, double maxdens, VFunction maxfunc, boolean relative){
   double smax = 0;
    fi.values.clear();
    VStatistics st = D.calcStatistics();
    float d2 = st.totalDispersion*st.totalDispersion/10f;
    for(int i=0;i<fi.points.size();i++){
      float p[] = (float[])fi.points.elementAt(i);
      double s = 0;
      for(int j=0;j<sel.selectedRows.length;j++){
        float x[] = D.getVector(sel.selectedRows[j]);
        float dist = VVectorCalc.SquareDistance(p,x);
        dist/=d2;
        dist = dist/(dev*dev);
        s = s+Math.exp(-(double)dist);
      }
      if(relative){
        float f = ((Float)maxfunc.values.elementAt(i)).floatValue();
        if(Math.abs(f)>1e-30) s/=f;
        else s = 1;
      }
      if(s>smax) smax = s;
      fi.values.add(new Float(s));
    }
    for(int i=0;i<fi.values.size();i++){
      Float x = (Float)fi.values.elementAt(i);
      if(relative)
        fi.values.setElementAt(new Float(x.floatValue()/maxdens),i);
      else
        fi.values.setElementAt(new Float(x.floatValue()/smax),i);
    }
    return smax;
  }

  public static double VCalculateDensityClassND(VDataSet D, VProjection proj, VTableSelector sel, float dev, VFunction fi, double maxdens, VFunction maxfunc, boolean relative){
   double smax = 0;
    fi.values.clear();
    VStatistics st = D.calcStatistics();
    float d2 = st.totalDispersion*st.totalDispersion/10f;
    for(int i=0;i<fi.points.size();i++){
      float p[] = (float[])fi.points.elementAt(i);
      double s = 0;
      float pND[] = proj.projectFromInToOut(p);
      for(int j=0;j<sel.selectedRows.length;j++){
        float x[] = D.getVector(sel.selectedRows[j]);
        float dist = VVectorCalc.SquareDistance(pND,x);
        dist/=d2;
        dist = dist/(dev*dev);
        s = s+Math.exp(-(double)dist);
      }
      if(relative){
        float f = ((Float)maxfunc.values.elementAt(i)).floatValue();
        if(Math.abs(f)>1e-30) s/=f;
        else s = 1;
      }
      if(s>smax) smax = s;
      fi.values.add(new Float(s));
    }
    for(int i=0;i<fi.values.size();i++){
      Float x = (Float)fi.values.elementAt(i);
      if(relative)
        fi.values.setElementAt(new Float(x.floatValue()/maxdens),i);
      else
        fi.values.setElementAt(new Float(x.floatValue()/smax),i);
    }
    return smax;
  }


  public static void NormalizeFunctionOnUnitInterval(VFunction fi){
    fi.smax = Double.MIN_VALUE;
    fi.smin = Double.MAX_VALUE;
    for(int i=0;i<fi.values.size();i++){
      float x = ((Float)fi.values.elementAt(i)).floatValue();
      if(x>fi.smax) fi.smax = x;
      if(x<fi.smin) fi.smin = x;
    }
    for(int i=0;i<fi.values.size();i++){
      float x = ((Float)fi.values.elementAt(i)).floatValue();
      if(Math.abs(fi.smax-fi.smin)>1e-20)
        x = (float)((x-fi.smin)/(fi.smax-fi.smin));
      else
        x = 0;
      fi.values.setElementAt(new Float(x),i);
    }
  }

  public static VFunction VCalculate2DGridForFunction(VDataSet proj,int nx, int ny){
    VStatistics st = proj.calcStatistics();
    VFunction res = new VFunction(2);
    double dx = (st.getMax(0)-st.getMin(0))/nx;
    double dy = (st.getMax(1)-st.getMin(1))/ny;
    for(int i=0;i<=nx;i++)
          for(int j=0;j<=ny;j++){
              float x[] = new float[2];
              x[0] = st.getMin(0)+(float)dx*i;
              x[1] = st.getMin(1)+(float)dy*j;
              res.points.add(x);
          }
    int k = 0;
    for(int i=0;i<=nx;i++)
        for(int j=0;j<=ny;j++){
        if((i!=nx)&&(j!=ny)){
        int tr[] = new int[3];
        tr[0]=k;
        tr[1]=k+1;
        tr[2]=k+ny+1;
        res.triangles.addElement(tr);
        tr = new int[3];
        tr[0]=k+1;
        tr[1]=k+ny+1;
        tr[2]=k+ny+2;
        res.triangles.addElement(tr);
        }
        k++;
        }
  return res;
  }

  public static void VCalculateLinearColor(VLinearBasis bas,VLinearFunction lf,VFunction fi){
    fi.values.clear();
    for(int i=0;i<fi.points.size();i++){
      float p[] = (float[])fi.points.elementAt(i);
      float pN[] = bas.projectFromInToOut(p);
      fi.values.add(new Float(lf.calculate(pN)));
    }
    NormalizeFunctionOnUnitInterval(fi);
  }

  public static void VBinarizeFunction(VFunction fi, float thresh){
    for(int i=0;i<fi.points.size();i++){
      float v = ((Float)fi.values.elementAt(i)).floatValue();
      float vm = 0.5f;
      if(v<0.25f) vm = 0f;
      if(v>0.75) vm = 1f;
      fi.values.setElementAt(new Float(vm),i);
      //System.out.println(v+" "+vm);
    }
  }

  public static void VDiscretizeFunctionThreeLevels(VFunction fi, float thresh){
    for(int i=0;i<fi.points.size();i++){
      float v = ((Float)fi.values.elementAt(i)).floatValue();
      v = (float)(fi.smin+v*(fi.smax-fi.smin));
      float vm = 0.5f;
      if(v<-1f+thresh) vm = 0f;
      if(v>1f-thresh) vm = 1f;
      fi.values.setElementAt(new Float(vm),i);
      //System.out.println(v+" "+vm);
    }
  }

  public static void VCalculateGridLinearColor(Grid gr,VLinearFunction lf,VFunction fi){
    fi.values.clear();
    for(int i=0;i<fi.points.size();i++){
      float p[] = (float[])fi.points.elementAt(i);
      float pN[] = ((SimpleRectangularGrid)gr).projectFromInToOut(p[0],p[1]);
      fi.values.add(new Float(lf.calculate(pN)));
    }
    NormalizeFunctionOnUnitInterval(fi);
  }

  public static void VCalculateLinearFunctionValues(VDataSet vd,VLinearFunction lf,VFunction fi){
    fi.values.clear();
    fi.pointIDs = new Vector();
    for(int i=0;i<vd.pointCount;i++){
      float p[] = vd.getVector(i);
      int id = vd.getVectorID(i);
      fi.values.add(new Float(lf.calculate(p)));
      fi.pointIDs.add(new Integer(id));
    }
  }

}