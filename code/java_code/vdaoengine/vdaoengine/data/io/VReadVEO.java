package vdaoengine.data.io;

import java.util.*;
import java.io.*;
import java.awt.*;
import vdaoengine.data.*;

public class VReadVEO {

  public static Vector readVEO(String fn, int dim){
    Vector objs = new Vector();
    try{
    LineNumberReader lr = new LineNumberReader(new FileReader(fn));
    String s = "";
    while((s=lr.readLine())!=null){
      if(s.indexOf("sphere")>0){
        VAddObject ao = new VAddObject();
        ao.type = ao.SPHERE;
        StringTokenizer st = new StringTokenizer(s," \t");
        while(st.hasMoreTokens()){
          ao.name = st.nextToken(); st.nextToken();
          float pos[] = new float[dim];
          for(int i=0;i<dim;i++) pos[i] = Float.parseFloat(st.nextToken());
          ao.positions.add(pos);
          ao.parameters.add(new Float(Float.parseFloat(st.nextToken())));
          int r = Integer.parseInt(st.nextToken());
          int g = Integer.parseInt(st.nextToken());
          int b = Integer.parseInt(st.nextToken());
          ao.color = new Color(r,g,b);
          objs.add(ao);
        }
      }
    }
    lr.close();
    }catch(Exception e){e.printStackTrace();}
    return objs;
  }

}