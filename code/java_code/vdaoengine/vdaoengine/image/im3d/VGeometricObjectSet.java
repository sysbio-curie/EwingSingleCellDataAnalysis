package vdaoengine.image.im3d;

/**
 * <p>Title: VDAO Engine and testing enviroment</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: IHES</p>
 * @author Andrey Zinovyev
 * @version 1.0
 */

import vdaoengine.image.*;

import java.util.*;

public class VGeometricObjectSet extends VObjectSet {

  HashMap Objects = new HashMap();

  public VGeometricObjectSet(String nam) {
    super(nam);
  }

  public void addObject(VGeometricObject go){
    Objects.put(go.getID(),go);
  }
  public VGeometricObject getObject(String id){
    return (VGeometricObject)Objects.get(id);
  }

}