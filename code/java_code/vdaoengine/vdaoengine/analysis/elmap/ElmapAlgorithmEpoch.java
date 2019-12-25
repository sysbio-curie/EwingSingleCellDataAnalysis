package vdaoengine.analysis.elmap;

public class ElmapAlgorithmEpoch {

  public int type; // 0 - epoch, 1 - adaptation
  public String adapttype;
  public String grammarType = null;
  public float EP = 1;
  public float RP = 1;
  public float epsConvergence = 0.01f;
  public float epsConvergenceSLAU = 0.01f;
  public float epsMSE = 0.01f;
  public int numberOfIterations = 100;
  public int numberOfSteps;
  public int maxNumberOfIterationsForSLAU = 100;
  public int extrapolate = 0;
  public int numevery;
  public boolean minimize = true;
  public boolean robust = false;
  public float trimradius = Float.MAX_VALUE;

}