package vdaoengine.utils;

import java.io.FileWriter;
import java.util.*;

import vdaoengine.data.*;

public class VSimpleFunctions {
	
  public static void main(String args[]){
	  try{
		  
		  /*
		   * Test corrected correlation
		   */
		  
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }

  public static float calcCorrelationCoeff(float m1[], float m2[]){
  float res = 0;
  int N = m1.length;
  float xy = 0f, x2 = 0f, y2 = 0f, x = 0f, y = 0f;
  for(int i=0;i<N;i++){
    xy+=m1[i]*m2[i];
    x+=m1[i];
    y+=m2[i];
    x2+=m1[i]*m1[i];
    y2+=m2[i]*m2[i];
  }
  double disp = Math.sqrt((x2-x*x/N)*(y2-y*y/N));
  if(Math.abs(disp)<1e-20)
	  res = 0;
  else
      res = (float)((xy-x*y/N)/disp);
  return res;
  }
  
  public static float calcCorrelationCoeffMissingValues(float m1[], float m2[]){
	  float corr = 0;
	  Vector<float[]> complete = new Vector<float[]>();
	  for(int i=0;i<m1.length;i++){
		  if(!Float.isNaN(m1[i]))if(!Float.isNaN(m2[i])){
			  float v[] = new float[2];
			  v[0] = m1[i];
			  v[1] = m2[i];
			  complete.add(v);
		  }
	  }
	  float m1complete[] = new float[complete.size()];
	  float m2complete[] = new float[complete.size()];
	  for(int i=0;i<complete.size();i++){
		  float v[] = complete.get(i);
		  m1complete[i] = v[0];
		  m2complete[i] = v[1];
	  }
	  corr = calcCorrelationCoeff(m1complete,m2complete);
	  return corr;
  }
  
  public static int getNumberOfCompleteNumberPairs(float m1[], float m2[]){
	  float corr = 0;
	  Vector<float[]> complete = new Vector<float[]>();
	  for(int i=0;i<m1.length;i++){
		  if(!Float.isNaN(m1[i]))if(!Float.isNaN(m2[i])){
			  float v[] = new float[2];
			  v[0] = m1[i];
			  v[1] = m2[i];
			  complete.add(v);
		  }
	  }
	  return complete.size();
  }
  
  
  
  public static double calcCorrelationPValue(float correlationValue, int numberOfPoints){
	  double pvalue = 0;
	  int degreeOfFreedom = numberOfPoints-2;
	  if(Math.abs(correlationValue)<1)if(degreeOfFreedom>2){
		  double tt = correlationValue/Math.sqrt((1-correlationValue*correlationValue)/(degreeOfFreedom));
		  //System.out.println("corr="+correlationValue+" tt="+tt);
		  if(Math.abs(correlationValue)>=1) pvalue=0;		  
		  if(degreeOfFreedom==2) pvalue=1;
		  pvalue = (1-tcdf(tt,degreeOfFreedom))*2;
	  }else{ pvalue=1; }
	  if(pvalue<1e-16) pvalue = 1e-16;
	  return pvalue;
  }
  
  public static float[] calcLinearRegression(float m1[], float m2[]){
	  float res[] = new float[2];
	  res = Algorithms.fitPolynome(m1, m2, 1);
	  return res;
	  }
  

  /**
   * 
   * @param m1
   * @param m2
   * @param ZValueThreshold
   * @param corrType  0 - for Pearson correlation, 1 - for Spearman correlation
   * @return
   */
  public static float calcCorrelationCoeffLeaveOneOut(float m1[], float m2[], float ZValueThreshold, int corrType){
	  float res = 0;
	  
	  float correlationCoeffs[] = new float[m1.length];

	  float m1s[] = new float[m1.length-1];
	  float m2s[] = new float[m2.length-1];
	  
	  for(int i=0;i<m1.length;i++){
		  int k=0;
		  for(int j=0;j<m1.length;j++){
			  if(j!=i){
				  m1s[k] = m1[j];
				  m2s[k] = m2[j];
				  k++;
			  }
		  }
		  float corrCoeff = 0f;
		  switch(corrType){
		  	case 0: corrCoeff = calcCorrelationCoeff(m1s,m2s); break;
		  	case 1: corrCoeff = calcSpearmanCorrelationCoeff(m1s,m2s); break;
		  }
		  correlationCoeffs[i] = corrCoeff;
	  }
	  
	  Vector m1new = new Vector();
	  Vector m2new = new Vector();
	  
	  float corrCoeff1[] = new float[m1.length-1];
	  for(int i=0;i<m1.length;i++){
		  int k=0;
		  for(int j=0;j<m1.length;j++){
			  if(j!=i){
				  corrCoeff1[k] = correlationCoeffs[j];
				  k++;
			  }
		  }
		  float meanValue = calcMean(corrCoeff1);
		  float stdDev = calcStandardDeviation(corrCoeff1);
		  float zvalue = (correlationCoeffs[i]-meanValue)/stdDev;
		  if(Math.abs(zvalue)<ZValueThreshold){
			 m1new.add(new Float(m1[i])); 
			 m2new.add(new Float(m2[i]));
		  }
	  }
	  
	  m1s = new float[m1new.size()];
	  m2s = new float[m2new.size()];
	  for(int i=0;i<m1new.size();i++){
		  m1s[i] = ((Float)m1new.get(i)).floatValue();
		  m2s[i] = ((Float)m2new.get(i)).floatValue();
	  }

	  switch(corrType){
	  	case 0: res = calcCorrelationCoeff(m1s,m2s); break;
	  	case 1: res = calcSpearmanCorrelationCoeff(m1s,m2s); break;
	  }
	  
	  /*## Function to compute robust correlation coeffients between mRNA-microRNA expression profiles
	  ## Input parameters:
	  ## a,b = vectors values 
	  ## lth = P-value threshold for robust correlation
	  lout <- function(x,y,lth)
	  {
	  	x <- as.numeric(x)
	  	y <- as.numeric(y)
	  	lth <- lth 
	  	pear.out <- c()
	  	spe.out <- c()
	  	for (k in 1:length(x))
	  	{
	  		tempx <- c()
	  		tempy <- c()
	  		for (z in 1:length(x))
	  		{
	  			if (z != k)
	  			{
	  				tempx <- c(tempx, x[z])
	  				tempy <- c(tempy, y[z])
	  			}
	  		}
	  		testpear <- cor.test(tempx,tempy, method = "pearson")
	  		pear.out <- c(pear.out, as.numeric(testpear$estimate))
	  		testspe <- cor.test(tempx,tempy, method = "spearman")
	  		spe.out <- c(spe.out, as.numeric(testspe$estimate))
	  	}
	  	newx <- c()
	  	newy <- c()
	  	for (k in 1:length(x))
	  	{
	  		ptemp <- pnorm(pear.out[k], mean(pear.out), sd(pear.out), lower.tail = TRUE, log.p = FALSE)
	  		pv <- 2*(1 - ptemp)
	  		if (pv >= lth)
	  		{
	  			newx <- c(newx, x[k])
	  			newy <- c(newy, y[k])
	  		}
	  	}
	  	pear.tot <<- cor.test(x,y, method = "pearson")
	  	spe.tot <<- cor.test(x,y, method = "spearman")
	  	pear.rob <<- cor.test(newx,newy, method = "pearson")
	  	spe.rob <<- cor.test(newx,newy, method = "spearman")
	  	return("ok")
	  }*/
	  
	  
	  return res;
  }
  
  public static float calcSpearmanCorrelationCoeffMissingValues(float m1[], float m2[]){
	  float corr = 0;
	  Vector<float[]> complete = new Vector<float[]>();
	  for(int i=0;i<m1.length;i++){
		  if(!Float.isNaN(m1[i]))if(!Float.isNaN(m2[i])){
			  float v[] = new float[2];
			  v[0] = m1[i];
			  v[1] = m2[i];
			  complete.add(v);
		  }
	  }
	  float m1complete[] = new float[complete.size()];
	  float m2complete[] = new float[complete.size()];
	  for(int i=0;i<complete.size();i++){
		  float v[] = complete.get(i);
		  m1complete[i] = v[0];
		  m2complete[i] = v[1];
	  }
	  corr = calcSpearmanCorrelationCoeff(m1complete,m2complete);
	  return corr;
  }
  

  public static float calcSpearmanCorrelationCoeff(float m1[], float m2[]){
  float res = 0;
  int N = m1.length;
  int ind1[] = Algorithms.SortMass(m1);
  int ind2[] = Algorithms.SortMass(m2);
  
  int rank1[] = ind2rank(ind1);  
  int rank2[] = ind2rank(ind2);
  
  //for(int i=0;i<ind2.length;i++)
  //   System.out.println(rank2[i]);
  
  float rank1f[] = new float[ind1.length];
  float rank2f[] = new float[ind1.length];
  for(int i=0;i<ind1.length;i++) rank1f[i] = (float)rank1[i];
  for(int i=0;i<ind2.length;i++) rank2f[i] = (float)rank2[i];
  /*long d2 = 0;
  for(int i=0;i<ind1.length;i++){
    d2+=(ind1[i]-ind2[i])*(ind1[i]-ind2[i]);
  }
  res = 1f-6f*(float)d2/((float)N*((float)N*(float)N-1f));*/
  res = calcCorrelationCoeff(rank1f,rank2f);
  return res;
  }
  
  public static int[] ind2rank(int ind[]){
	  int rank[] = new int[ind.length];
	  for(int i=0;i<ind.length;i++)
		  rank[ind[i]] = i;
	  return rank;
  }


  public static int IntersectionOfSets(Vector set1, Vector set2){
    int res = 0;
    for(int i=0;i<set1.size();i++){
      Object o1 = set1.get(i);
      if(set2.indexOf(o1)>=0)
        res++;
    }
    return res;
  }

  public static Set UnionOfSets(Set set1, Set set2){
    Iterator it = set2.iterator();
    while(it.hasNext()){
      Object o = it.next();
      if(!set1.contains(o))
        set1.add(o);
    }
    return set1;
  }


  public static int IntersectionOfIntegerSets(int set1[], int set2[]){
    Vector set1v = new Vector();
    Vector set2v = new Vector();
    for(int i=0;i<set1.length;i++)
      set1v.add(new Integer(set1[i]));
    for(int i=0;i<set2.length;i++)
      set2v.add(new Integer(set2[i]));
    return IntersectionOfSets(set1v,set2v);
  }

  public static int IntersectionOfStringSets(String set1[], String[] set2){
    Vector set1v = new Vector();
    Vector set2v = new Vector();
    for(int i=0;i<set1.length;i++)
      set1v.add(new String(set1[i]));
    for(int i=0;i<set2.length;i++)
      set2v.add(new String(set2[i]));
    return IntersectionOfSets(set1v,set2v);
  }

  public static int[] randomIntVector(int dim, int limit, Random r){
    int rand[] = new int[dim];
    for(int i=0;i<dim;i++)
      rand[i] = r.nextInt(limit);
    return rand;
  }

  public static float calcMedian(float f[]){
    float r = 0;
    int ind[] = Algorithms.SortMass(f);
    if(f.length!=0){
    if(f.length==2*(int)(0.5f*f.length)){
      int mid1 = (int)(0.5f*f.length)-1;
      int mid2 = (int)(0.5f*f.length);
      r = 0.5f*(f[ind[mid1]]+f[ind[mid2]]);
    }else{
      int mid = (int)(0.5f*f.length);
      r = f[ind[mid]];
    }}
    return r;
  }

  public static float calcStandardDeviation(float f[]){
    float r = 0;
    float x = 0;
    float x2 = 0;
    for(int i=0;i<f.length;i++){
      x+=f[i];
      x2+=f[i]*f[i];
    }
    x/=f.length;
    r = (float)Math.sqrt((x2/f.length-x*x)*(float)f.length/((float)f.length-1));
    return r;
  }

  public static float calcMaxAbsValue(float f[]){
	    float r = 0;
	    for(int i=0;i<f.length;i++)
	    	if(Math.abs(f[i])>r) r = Math.abs(f[i]); 
	    return r;
	  }
  
  
  public static float calcZeroCenteredStandardDeviation(float f[]){
	    float r = 0;
	    float x = 0;
	    float x2 = 0;
	    for(int i=0;i<f.length;i++){
	      x+=f[i];
	      x2+=f[i]*f[i];
	    }
	    x=0;
	    r = (float)Math.sqrt((x2/f.length-x*x)*(float)f.length/((float)f.length-1));
	    return r;
	  }
  
  
  public static float calcStandardDeviationBiggerThan(float f[], float val){
	    float r = 0;
	    float x = 0;
	    float x2 = 0;
	    int k = 0;
	    for(int i=0;i<f.length;i++)if(f[i]>val){
	      x+=f[i];
	      x2+=f[i]*f[i];
	      k++;
	    }
	    if(k>1){
	    	x/=k;
	    	r = (float)Math.sqrt((x2/k-x*x)*(float)k/((float)k-1));
	    }
	    return r;
	  }
  
  
  public static float calcDispersion(float f[]){
	    float r = 0;
	    float x = 0;
	    float x2 = 0;
	    for(int i=0;i<f.length;i++){
	      x+=f[i];
	      x2+=f[i]*f[i];
	    }
	    x/=f.length;
	    r = (float)(x2/f.length-x*x);
	    return r;
	  }
  

  public static float calcMean(float f[]){
    float x = 0;
    for(int i=0;i<f.length;i++){
      x+=f[i];
    }
    return x/f.length;
  }
  
  public static double calcMeanBiggerThan(float f[], float val){
	    float x = 0;
	    int k=0;
	    for(int i=0;i<f.length;i++)if(f[i]>val){
	      x+=f[i];
	      k++;
	    }
	    if(k>0)
	    	x/=k;
	    return x;
  }
  


  public static float calcMedianPairwiseCorrelation(VDataSet vd, boolean absvalue){
    float mc = 0f;
    float mcs[] = new float[Math.round(0.5f*vd.pointCount*(vd.pointCount-1))];
    int k=0;
    for(int i=0;i<vd.pointCount;i++){
       for(int j=i+1;j<vd.pointCount;j++)
         if(absvalue)
           mcs[k++] = Math.abs(calcCorrelationCoeff(vd.getVector(i),vd.getVector(j)));
         else
           mcs[k++] = calcCorrelationCoeff(vd.getVector(i),vd.getVector(j));
    }
    mc = calcMedian(mcs);
    return mc;
  }

  public static float calcMedianPairwiseSpearmanCorrelation(VDataSet vd, boolean absvalue){
    float mc = 0f;
    float mcs[] = new float[Math.round(0.5f*vd.pointCount*(vd.pointCount-1))];
    int k=0;
    for(int i=0;i<vd.pointCount;i++){
       for(int j=i+1;j<vd.pointCount;j++)
         if(absvalue)
           mcs[k++] = Math.abs(calcSpearmanCorrelationCoeff(vd.getVector(i),vd.getVector(j)));
         else
           mcs[k++] = calcSpearmanCorrelationCoeff(vd.getVector(i),vd.getVector(j));
    }
    mc = calcMedian(mcs);
    return mc;
  }
  
  public static float calcMeanPairwiseCorrelation(VDataSet vd, boolean absvalue, float threshold){
    float mc = 0f;
    float mcs[] = new float[Math.round(0.5f*vd.pointCount*(vd.pointCount-1))];
    int k=0;
    for(int i=0;i<vd.pointCount;i++){
       for(int j=i+1;j<vd.pointCount;j++){
    	 mcs[k] = 0;
    	 float cc = calcCorrelationCoeff(vd.getVector(i),vd.getVector(j));
    	 if(Math.abs(cc)>=threshold){
         if(absvalue)
           mcs[k] = Math.abs(cc);
         else
           mcs[k] = cc;
         k++;
    	 }
       }
    }
    //mc = calcMean(mcs);
    for(int i=0;i<k;i++) mc+=mcs[i]; if(k>0) mc/=k;
    return mc;
  }
  
  public static float calcMeanPairwiseCorrelation(VDataSet vd1, VDataSet vd2, boolean absvalue, float threshold, boolean skipCorrelationOne){
	    float mc = 0f;
	    float mcs[] = new float[vd1.pointCount*vd2.pointCount];
	    int k=0;
	    for(int i=0;i<vd1.pointCount;i++){
	       for(int j=0;j<vd2.pointCount;j++){
	    	 mcs[k] = 0;
	    	 float cc = calcCorrelationCoeff(vd1.getVector(i),vd2.getVector(j));
	    	 if(Math.abs(cc)>=threshold)if((!skipCorrelationOne)||cc<(1f-1e-6f)){
	         if(absvalue)
	           mcs[k] = Math.abs(cc);
	         else
	           mcs[k] = cc;
	         k++;
	    	 }
	       }
	    }
	    //mc = calcMean(mcs);
	    for(int i=0;i<k;i++) mc+=mcs[i]; if(k>0) mc/=k;
	    return mc;
	  }
  
  
  	public static int[] findClosestDataPoints(float vector[], VDataSet ds, int numberOfNeighbours){
  		int neigh[] = new int[numberOfNeighbours];
  		float dist[] = new float[ds.pointCount];
  		for(int i=0;i<ds.pointCount;i++){
  			dist[i] = VVectorCalc.SquareDistance(vector, ds.getVector(i));
  		}
  		int ind[] = Algorithms.SortMass(dist);
  		for(int i=0;i<numberOfNeighbours;i++){
  			neigh[i] = ind[i];
  			//System.out.print(dist[ind[i]]+"\t");
  		}
  		System.out.println();
  		return neigh;
  	}
  
  public static float[] getBrokenStickDistribution(int dim){
	  float res[] = new float[dim];
	  for(int i=0;i<dim;i++){
		  for(int j=i+1;j<=dim;j++){
			  res[i]+=1f/(float)j;
		  }
		  res[i]/=dim;
	  }
	  return res;
  }
  
  public static float calcChavezIntrinsicDimension(VDataSet vd){
	    float mc = 0f;
	    float mc2 = 0f;	    
	    int k=0;
	    for(int i=0;i<vd.pointCount;i++){
	       for(int j=i+1;j<vd.pointCount;j++){
	           float d = (float)VVectorCalc.Distance(vd.getVector(i),vd.getVector(j));
	           mc+=d;
	           mc2+=d*d;
	           k++;
	       }
	    }
	    mc/=k;
	    mc2 = mc2/k-mc*mc;
	    return mc*mc/(2*mc2);
  }
  
  public static double calcTTest(Vector set1, Vector set2){
	  return calcTTest(set1, set2, true, null);
  }
  
  public static double calcMean(Vector<Float> set1){
	  double r = 0;
	  for(int i=0;i<set1.size();i++)
		  r+=set1.get(i);
	  r/=set1.size();
	  return r;
  }
  
  public static double calcMeanBiggerThan(Vector<Float> set1, float val){
	  double r = 0;
	  int k = 0;
	  for(int i=0;i<set1.size();i++)
		  if(set1.get(i)>val){
			  r+=set1.get(i);
			  k++;
		  }
	  if(k>0)
		  r/=k;
	  return r;
  }
  
  
  
  public static double calcTTest(Vector set1, Vector set2, boolean VariationEqual, Vector<Integer> dfval){
	    double r = 0;
	    VStatistics stat1 = new VStatistics(1);
	    VStatistics stat2 = new VStatistics(1);
	    float d[] = new float[1];
	    for(int i=0;i<set1.size();i++){
	      d[0] = ((Float)set1.elementAt(i)).floatValue();
	      stat1.addNewPoint(d);
	    }
	    for(int i=0;i<set2.size();i++){
	      d[0] = ((Float)set2.elementAt(i)).floatValue();
	      stat2.addNewPoint(d);
	    }
	    stat1.calculate();
	    stat2.calculate();
	    if(!VariationEqual){
	    	r = (stat1.getMean(0)-stat2.getMean(0))/Math.sqrt(stat1.getStdDev(0)*stat1.getStdDev(0)/set1.size()+stat2.getStdDev(0)*stat2.getStdDev(0)/set2.size());
	    	double s21 = stat1.getStdDev(0)*stat1.getStdDev(0)/set1.size();
	    	double s22 = stat2.getStdDev(0)*stat2.getStdDev(0)/set2.size();
	    	int n1 = set1.size();
	    	int n2 = set2.size();
	    	int df = (int)((s21+s22)*(s21+s22)/(s21*s21/(n1-1)+s22*s22/(n2-1)));
	    	dfval.add(new Integer(df));
	    }else{
	    	double std = Math.sqrt((stat1.getStdDev(0)*stat1.getStdDev(0)*(set1.size()-1)+stat2.getStdDev(0)*stat2.getStdDev(0)*(set2.size()-1))/(set1.size()+set2.size()-2));
	    	r = (stat1.getMean(0)-stat2.getMean(0))/Math.sqrt(std*std/set1.size()+std*std/set2.size());
	    	//r = (stat1.getMean(0)-stat2.getMean(0))/std;
	    }
	    return r;
	  }  
  
  /**
   * logarithm of gamma function
   * @param xx
   * @return
   */
  public static double gammln(double xx)
  {
  	int j;
  	double x,y,tmp,ser;
  	double cof[]= {76.18009172947146,-86.50532032941677,24.01409824083091,-1.231739572450155,0.1208650973866179e-2,-0.5395239384953e-5};

  	y=x=xx;
  	tmp=x+5.5;
  	tmp -= (x+0.5)*Math.log(tmp);
  	ser=1.000000000190015;
  	for (j=0;j<6;j++) ser += cof[j]/++y;
  	return -tmp+Math.log(2.5066282746310005*ser/x);
  }
  /**
   * continued fraction used by betai
   */
  public static double betacf(double a, double b, double x)
  {
  	int MAXIT=100;
  	double EPS=1e-10;
  	double FPMIN=Double.MIN_VALUE/EPS;
  	int m,m2;
  	double aa,c,d,del,h,qab,qam,qap;

  	qab=a+b;
  	qap=a+1.0;
  	qam=a-1.0;
  	c=1.0;
  	d=1.0-qab*x/qap;
  	if (Math.abs(d) < FPMIN) d=FPMIN;
  	d=1.0/d;
  	h=d;
  	for (m=1;m<=MAXIT;m++) {
  		m2=2*m;
  		aa=m*(b-m)*x/((qam+m2)*(a+m2));
  		d=1.0+aa*d;
  		if (Math.abs(d) < FPMIN) d=FPMIN;
  		c=1.0+aa/c;
  		if (Math.abs(c) < FPMIN) c=FPMIN;
  		d=1.0/d;
  		h *= d*c;
  		aa = -(a+m)*(qab+m)*x/((a+m2)*(qap+m2));
  		d=1.0+aa*d;
  		if (Math.abs(d) < FPMIN) d=FPMIN;
  		c=1.0+aa/c;
  		if (Math.abs(c) < FPMIN) c=FPMIN;
  		d=1.0/d;
  		del=d*c;
  		h *= del;
  		if (Math.abs(del-1.0) <= EPS) break;
  	}
  	if (m > MAXIT) System.out.println("ERROR: a or b too big, or MAXIT too small in betacf");
  	return h;
  }
  /**
   * incomplete beta function
   * @param a
   * @param b
   * @param x
   * @return
   */
  public static double betai(double a, double b, double x)
  {
	double bt;

  	if (x < 0.0 || x > 1.0) System.out.println("ERROR: Bad x in routine betai");
  	if (x == 0.0 || x == 1.0) bt=0.0;
  	else
  		bt=Math.exp(gammln(a+b)-gammln(a)-gammln(b)+a*Math.log(x)+b*Math.log(1.0-x));
  	if (x < (a+1.0)/(a+b+2.0))
  		return bt*betacf(a,b,x)/a;
  	else
  		return 1.0-bt*betacf(b,a,1.0-x)/b;
  }
  /**
   * Student's t-test for difference of means
   * @param t
   * @param df
   * @return
   */
  public static double ttest(double t, int df)
  {
  	return betai(0.5*df,0.5,df/(df+t*t));
  }
  /**
   * Matlab's tcdf
   * @param t
   * @param df
   * @return
   */
  public static double tcdf(double t, int df)
  {
  	return 1-ttest(t,df)/2;
  }
  
  public static double calcTrianglePatternPValue(float x[], float y[], float incline, int numberOfPermutations){
	  double pval = 0;
	  float nullDistribution[][] = calculatePermutationsPairs(x,y,numberOfPermutations);
	  
	  	float bestb = -10f;
	  	float maxdist = -10f;
	  	float bestratio = 0;
	  	
	  	
		for(float b=-3f;b<+3f;b+=0.01){
			int countr = 0;
			for(int i=0;i<x.length;i++){
				if(incline*x[i]-y[i]+b>0) countr++;
			}
			if(countr==x.length) break;
			if((float)(countr)/x.length>0.5f){
			int countnd = 0;
			for(int i=0;i<nullDistribution[0].length;i++){
				if(incline*nullDistribution[0][i]-nullDistribution[1][i]+b>0) countnd++;
			}
			float dist = (float)(countr)/x.length-(float)(countnd)/nullDistribution[0].length;
			if(dist>maxdist){
				maxdist = dist;
				bestb = b;
				bestratio = (float)(countr)/x.length;
			}
			}
		}

		Random r = new Random();
		int countpval = 0;
		//Date d = new Date();
		for(int j=0;j<numberOfPermutations;j++){
			//System.out.println(j);
			int counttest = 0;
			for(int k=0;k<x.length;k++){
					int pn = r.nextInt(nullDistribution[0].length);
					if(incline*nullDistribution[0][pn]-nullDistribution[1][pn]+bestb>0) counttest++;
			}
			if((float)counttest/x.length>=bestratio) countpval++;
		}
		//System.out.println("calcPValue : "+((new Date()).getTime()-d.getTime()));
		
	  pval = (double)countpval/numberOfPermutations;
		
	  return pval;
  }
  
	public static float[][] calculatePermutationsPairs(float x[],float y[],int numberOfPermutations){
		float nd[][]=new float[2][numberOfPermutations];
		Random r = new Random();
		for(int i=0;i<numberOfPermutations;i++){
			nd[0][i] = x[r.nextInt(x.length)];
			nd[1][i] = y[r.nextInt(y.length)];
			//System.out.println(nd[0][i]+"\t"+nd[1][i]);
		}
		return nd;
	}
	
	public static void makeCorrelationTable(VDataTable vt, float correlationThreshold, String fout) throws Exception{
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		FileWriter fw = new FileWriter(fout);
		fw.write("NODE1\tNODE2\tCORR\tABSCORR\n");
		for(int i=0;i<vt.rowCount;i++)
			for(int j=i+1;j<vt.rowCount;j++){
				float corr = VSimpleFunctions.calcCorrelationCoeff(vd.massif[i], vd.massif[j]);
				//System.out.println(corr);
				if(Math.abs(corr)>correlationThreshold)
					fw.write(vt.stringTable[i][0]+"\t"+vt.stringTable[j][0]+"\t"+corr+"\t"+Math.abs(corr)+"\n");
			}
		fw.close();
	}

	public static VDataTable makeObjectCorrelationTable(VDataTable vt, float correlationThreshold) throws Exception{
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);
		VDataTable res = new VDataTable();
		res.addNewColumn("ID", "", "", vt.STRING, "_");
		res.stringTable = new String[vt.rowCount][1];
		res.rowCount = vt.rowCount;
		for(int i=0;i<vt.rowCount;i++){
			res.stringTable[i][0] = vt.stringTable[i][0];
			res.addNewColumn(vt.stringTable[i][0], "", "", vt.NUMERICAL, "_");
		}
		for(int i=0;i<vt.rowCount;i++)
			for(int j=0;j<vt.rowCount;j++){
				float corr = VSimpleFunctions.calcCorrelationCoeff(vd.massif[i], vd.massif[j]);
				//System.out.println(corr);
				if(Math.abs(corr)>correlationThreshold)
					res.stringTable[i][j+1] = ""+corr;
				else
					res.stringTable[i][j+1] = "0";
			}
		return res;
	}

	
	public static void makeCorrelationTableCorrectedForCommonFactor(VDataTable vt, float correlationThreshold, String commonFactor, String fout) throws Exception{
		VDataSet vd = VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt, -1);

		int commonFactorK = -1;
		/*for(int i=0;i<vd.selector.selectedColumns.length;i++)
			if(vt.fieldNames[vd.selector.selectedColumns[i]].equals(commonFactor))
				commonFactorK = i;
		*/
		for(int i=0;i<vt.rowCount;i++){
			System.out.println(vt.stringTable[i][0]);
			if(vt.stringTable[i][0].equals(commonFactor))
				commonFactorK = i;
		}
		
		FileWriter fw = new FileWriter(fout);
		fw.write("NODE1\tNODE2\tCORR\tABSCORR\n");
		for(int i=0;i<vt.rowCount;i++)
			for(int j=i+1;j<vt.rowCount;j++){
				float corr = VSimpleFunctions.calcCorrelationCoefCorrectedForCommonFactor(vd.massif[i], vd.massif[j], vd.massif[commonFactorK]);
				//System.out.println(corr);
				if(Math.abs(corr)>correlationThreshold)
					fw.write(vt.stringTable[i][0]+"\t"+vt.stringTable[j][0]+"\t"+corr+"\t"+Math.abs(corr)+"\n");
			}
		
		float commonfactor[] = vd.massif[commonFactorK];
		for(int i=0;i<vt.rowCount;i++)if(i!=commonFactorK){
			float factor[] = VVectorCalc.Product_(commonfactor, (float)1/(float)VVectorCalc.Norm(commonfactor));
			float x[] = VVectorCalc.Product_(vd.massif[i], (float)1/(float)VVectorCalc.Norm(vd.massif[i]));
			float corr = VVectorCalc.ScalarMult(x, factor);
			if(Math.abs(corr)>correlationThreshold)
				fw.write(vt.stringTable[i][0]+"\t"+commonFactor+"\t"+corr+"\t"+Math.abs(corr)+"\n");
		}
		fw.close();
	}
	
	
	public static float calcCorrelationCoefCorrectedForCommonFactor(float x[], float y[], float commonFactor[]){
		float coeff = 0;
		float factor[] = VVectorCalc.Product_(commonFactor, (float)1/(float)VVectorCalc.Norm(commonFactor));
		float projectionx[] = VVectorCalc.Product_(factor, VVectorCalc.ScalarMult(x, factor));
		float projectiony[] = VVectorCalc.Product_(factor, VVectorCalc.ScalarMult(y, factor));
		float xc[] = VVectorCalc.Subtract_(x, projectionx);
		float yc[] = VVectorCalc.Subtract_(y, projectiony);
		coeff = VSimpleFunctions.calcCorrelationCoeff(xc, yc);
		return coeff;
	}
	
	/*
	 * Returns a massif of numbers corrected by projecting on a common factor
	 * First index is the point number, second index is the coordinate number
	 */
	public static float[][] calcResiduesFromProjectionOnCommonFactor(float points[][], float commonFactor[]){
		int numberOfPoints = points.length;
		int numberOfCoordinates = points[0].length;
		float pointsc[][] = new float[numberOfPoints][numberOfCoordinates];
		float factor[] = VVectorCalc.Product_(commonFactor, (float)1/(float)VVectorCalc.Norm(commonFactor));
		for(int i=0;i<numberOfPoints;i++){
			float x[] = points[i];
			float projectionx[] = VVectorCalc.Product_(factor, VVectorCalc.ScalarMult(x, factor));
			float xc[] = VVectorCalc.Subtract_(x, projectionx);
			pointsc[i] = xc;
		}
		return pointsc;
	}
	
	public static float[][] calcResiduesFromRegressionOnCommonFactor(float points[][], float commonFactor[]){
		int numberOfPoints = points.length;
		int numberOfCoordinates = points[0].length;
		float pointsc[][] = new float[numberOfPoints][numberOfCoordinates];
		for(int i=0;i<numberOfCoordinates;i++){
			Vector<Float> x = new Vector<Float>();
			Vector<Float> y = new Vector<Float>();
			for(int j=0;j<numberOfPoints;j++)if(!Float.isNaN(commonFactor[j]))if(!Float.isNaN(points[j][i])){
				x.add(commonFactor[j]);
				y.add(points[j][i]);
			}
			float xf[] = new float[x.size()];
			float yf[] = new float[y.size()];
			for(int k=0;k<x.size();k++) xf[k]=x.get(k);
			for(int k=0;k<y.size();k++) yf[k]=y.get(k);
			float p[] = calcLinearRegression(xf, yf);
			//System.out.println(p[0]+"\t"+p[1]);
			for(int j=0;j<numberOfPoints;j++){
				pointsc[j][i] = points[j][i]-p[0]-commonFactor[j]*p[1];
			}
		}
		return pointsc;
	}
	
	
	public static float[] calcCommonFactorByPC1(float points[][]){
		int numberOfPoints = points.length;
		int numberOfCoordinates = points[0].length;
		float factor[] = new float[numberOfCoordinates];
		
		return factor;
	}
	

	public class VQuadraticFunction implements VMathFunction {
		public float getValue(float x, float parameters[]) {
			return x*x;
	}
		public float getValue(float x) {
			return x*x;
	}
	}
	public class VAbsFunction implements VMathFunction {
		public float getValue(float x, float parameters[]) {
			return Math.abs(x);
	}
		public float getValue(float x) {
			return Math.abs(x);
	}
	}
	public class VPolynomialFunction implements VMathFunction {
		public float getValue(float x, float parameters[]) {
			float y = 0f;
			for(int i=0;i<parameters.length;i++)
				y +=parameters[i]*Math.pow(x, i);
			return y;
	}
		public float getValue(float x) {
			return x*x;
		}
	}
	public class VSqrtFunction implements VMathFunction {
		public float getValue(float x, float parameters[]) {
			return (float)Math.sqrt(Math.abs(x));
	}
		public float getValue(float x) {
			return (float)Math.sqrt(Math.abs(x));
		}
	}
	

	

}