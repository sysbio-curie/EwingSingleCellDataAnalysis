package vdaoengine.image.im3d;

/*
 * Title:        VDAO Engine and testing enviroment
 * Description:  Generic interface for visualization procedure
 * Copyright:    Copyright (c) 2002
 * Company:      IHES
 * @author Andrey Zinovyev
 * @version 1.0

*/


import vdaoengine.data.*;
import vdaoengine.image.*;
import vdaoengine.visproc.*;
import java.util.*;
import java.lang.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import vdaoengine.image.im3d.VUtils3D;

public class VDataImage3D extends VDataImage {

public boolean drawDefaultAxes = true;

public VDataImage3D(){
}

public VDataImage3D(VDataImage imag){
this.elements = imag.elements;
}

public BranchGroup createSceneGraph(){
System.out.println("Image elements");
BranchGroup objRoot = new BranchGroup();

if(drawDefaultAxes) objRoot.addChild(new vdaoengine.image.im3d.Axis());

for(int i=0;i<elements.size();i++){
  String className = elements.elementAt(i).getClass().getName();
  VDataImageElement vie = (VDataImageElement)(elements.elementAt(i));
  System.out.println(className+": \""+vie.name+"\"");
  if(vie.visible){
     if(className.equals("vdaoengine.image.VDataPointSet")) visualizeDataPoints(objRoot, (VDataPointSet)(elements.elementAt(i)));
  }
  }
//objRoot.addChild(new ColorCube(0.15));

return objRoot;
}

//--------------- visualizeFlatPoints -------------------//

/*public void visualizeDataPoints(BranchGroup root, VDataPointSet points){

VObjectDescriptorSet dss = points.getDescriptorSet();

HashSet uniqSet = dss.getUniqueList();

for(Iterator itds=uniqSet.iterator();itds.hasNext();){

	VObjectDescriptor ds = (VObjectDescriptor)(itds.next());
        //System.out.println("In DataImage: "+ds+" "+ds.visible);
        if(!ds.visible) continue;
        Appearance app = VUtils3D.getAppearanceForDataPoint(ds,false);
        Vector ids = null;
        if(ds.equals(VObjectDescriptor.DefaultDescriptor()))
                ids = dss.getIDsForDefault(points.IDs);
          else
        	ids = dss.getIDsByDescriptor(ds);
        if(ids.size()>0)
	if(ds.isSimplified()){ // Draw data points just as pointarray

		BranchGroup pointGroup = new BranchGroup();
                Shape3D po = new Shape3D();
		PointArray pa = new PointArray(ids.size(), GeometryArray.COORDINATES|GeometryArray.COLOR_3);
		Point3f verts[] = new Point3f[ids.size()];
		Color3f colors[] = new Color3f[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			int ID = ((Integer)(ids.elementAt(i))).intValue();
			float trv[] = points.getPointPos(ID);
                	verts[i] = new Point3f(trv);
                        colors[i] = new Color3f(ds.getFillColor());
		}
                pa.setCoordinates(0,verts);
		pa.setColors(0,colors);
                po.setGeometry(pa);
		po.setAppearance(app);
		pointGroup.addChild(po);
		pointGroup.compile();
		root.addChild(pointGroup);
		}
	else{ // Every data point is a complicated geometry

		BranchGroup pointGroup = new BranchGroup();

		for (int i = 0; i < ids.size(); i++) {
			int ID = ((Integer)(ids.elementAt(i))).intValue();
			float trv[] = points.getPointPos(ID);
                        addFlatPointToBranchGroup(pointGroup,ds,trv);
		}
		pointGroup.compile();
		root.addChild(pointGroup);
	} // else
} // for
}*/

public void visualizeDataPoints(BranchGroup root, VDataPointSet points){

VObjectDescriptorSet dss = points.getDescriptorSet();

HashSet uniqSet = dss.getUniqueList();

// Calculate the number of points first
int numberOfVisiblePoints = 0;
for(Iterator itds=uniqSet.iterator();itds.hasNext();){
VObjectDescriptor ds = (VObjectDescriptor)(itds.next());
Vector ids = dss.getIDsByDescriptor(ds);
if(ds.visible)
  numberOfVisiblePoints+=ids.size();
}

for(Iterator itds=uniqSet.iterator();itds.hasNext();){

	VObjectDescriptor ds = (VObjectDescriptor)(itds.next());
        //System.out.println("In DataImage: "+ds+" "+ds.visible);
        if(!ds.visible) continue;
        Appearance app = VUtils3D.getAppearanceForDataPoint(ds,false);
        Vector ids = null;
        if(ds.equals(VObjectDescriptor.DefaultDescriptor()))
                ids = dss.getIDsForDefault(points.IDs);
          else
        	ids = dss.getIDsByDescriptor(ds);
        if(ids.size()>0)
	if(ds.isSimplified()){ // Draw data points just as pointarray

		BranchGroup pointGroup = new BranchGroup();
                Shape3D po = new Shape3D();
		PointArray pa = new PointArray(ids.size(), GeometryArray.COORDINATES|GeometryArray.COLOR_3);
		Point3f verts[] = new Point3f[ids.size()];
		Color3f colors[] = new Color3f[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			int ID = ((Integer)(ids.elementAt(i))).intValue();
			float trv[] = points.getPointPos(ID);
                	verts[i] = new Point3f(trv);
                        colors[i] = new Color3f(ds.getFillColor());
		}
                pa.setCoordinates(0,verts);
		pa.setColors(0,colors);
                po.setGeometry(pa);
		po.setAppearance(app);
		pointGroup.addChild(po);
		pointGroup.compile();
		root.addChild(pointGroup);
		}
	else{ // Every data point is a complicated geometry

		BranchGroup pointGroup = new BranchGroup();

		for (int i = 0; i < ids.size(); i++) {
			int ID = ((Integer)(ids.elementAt(i))).intValue();
			float trv[] = points.getPointPos(ID);
                        addFlatPointToBranchGroup(pointGroup,ds,trv);
		}
		pointGroup.compile();
		root.addChild(pointGroup);
	} // else
} // for
}

//---------------                       -------------------//
public void addFlatPointToBranchGroup(BranchGroup pointGroup, VObjectDescriptor ds, float[] pos){
VFlatPoint3D p = new VFlatPoint3D(ds.getShape(),ds.getSize()/100);
p.shape.setAppearance(VUtils3D.getAppearanceForDataPoint(ds,false));
Transform3D tr = new Transform3D();
tr.set(new Vector3f(pos));
TransformGroup trang = new TransformGroup(tr);
trang.addChild(p.shape);
if(ds.isWithBorder()) {
  p.setBorder(ds.getBorderThick());
  p.border.setAppearance(VUtils3D.getAppearanceForDataPoint(ds,true));
  trang.addChild(p.border);
}
pointGroup.addChild(trang);
}
//---------------                       -------------------//

}
