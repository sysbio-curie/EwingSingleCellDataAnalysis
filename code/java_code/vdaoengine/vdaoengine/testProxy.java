package vdaoengine;

import java.net.*;
import vdaoengine.utils.*;

public class testProxy {
  public static void main(String[] args) {
//some previous code

    System.getProperties().put("proxySet","true");
    System.getProperties().put("proxyPort","3128");
    System.getProperties().put("proxyHost","www-cache");
    try
    {
          String dat = VDownloader.downloadURL("http://www.ihes.fr/~zinovyev/test/elmap.ini");
          System.out.println(dat);

    }
    catch(Exception e)
    {
        e.printStackTrace();
    }

//Rest of the stuff....

  }
}