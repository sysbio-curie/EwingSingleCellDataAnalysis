package vdaoengine.data;

import java.util.*;
import java.io.*;
import java.awt.*;

public class VAddObject {

  public static int SPHERE = 0;
  public static int CYLINDER = 1;

  public Color color = Color.red;
  public Vector parameters = new Vector();
  public Vector positions = new Vector();
  public String name = "noname";
  public int type = SPHERE;

}