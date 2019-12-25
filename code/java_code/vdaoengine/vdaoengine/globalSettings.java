package vdaoengine;



/**
 * Title:        VDAO Engine and testing enviroment
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0
 */

import java.awt.*;
import java.util.Random;

public class globalSettings {

public static double epsOnCalculatingVectorComp = 0.0001;
public static int MAX_NUM_ITER = 1000;
public static int principalComponentCalculationMethod = 0; // 0 - iterational, 1 - by covariance matrix
public static Color defaultFillColor = Color.blue;
public static float defaultPointSize = 4f;
public static int defaultPointShape = 4;
public static Color defaultPointBorderColor = Color.blue;
public static boolean defaultPointBorder = false;
public static float defaultPointBorderThick = 2;
public static int defaultFillType = 0;
public static int maximumNumberOfRows = 1000000;
public static Color BackgroundColor = Color.gray;
public static boolean defaultPointSimplified = true;
public static Color[] standardColors = {Color.red,Color.yellow,Color.green,Color.blue,Color.magenta,Color.pink,Color.cyan,Color.orange};
public static int[] standardShapes = {8,4,0,5,1,2,3,6,7};
public static String defaultCaptureFileName = new String("image");

public final static int TRIANGLE_UP = 0;
public final static int TRIANGLE_DOWN = 1;
public final static int TRIANGLE_LEFT = 2;
public final static int TRIANGLE_RIGHT = 3;
public final static int SQUARE = 4;
public final static int RHOMB = 5;
public final static int PENTAGON = 6;
public final static int HEXAGON = 7;
public final static int CIRCLE = 8;
public final static int CROSS = 9;
public final static int CROSS_ROTATED = 10;


public static void LoadFromConfFile(String fn){
}

public static void AddMoreStandardColors(int n){
Color[] nColor = new Color[n];
for(int i=0;i<standardColors.length;i++) nColor[i] = standardColors[i];
Random r = new Random();
int i=standardColors.length;
while(i<n){
float rr = r.nextFloat();
float gg = r.nextFloat();
float bb = r.nextFloat();
Color c = new Color(rr,gg,bb);
boolean found = false;
for(int j=0;j<nColor.length;j++) if(nColor[j]!=null){
   float cr = (c.getRed()-nColor[j].getRed());
   float cg = (c.getGreen()-nColor[j].getGreen());
   float cb = (c.getBlue()-nColor[j].getBlue());
   float rrr = cr*cr+cg*cg+cb*cb;
   if(rrr<100) found = true;
   }
if(!found){
    nColor[i]=c;
    i++;
    }
}
standardColors = nColor;

}


}