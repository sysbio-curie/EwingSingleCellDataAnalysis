/*
 *	@(#)HelloUniverse.java 1.52 01/01/11 07:33:44
 *
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package vdaoengine;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.*;

public class Hello extends Applet {

    private SimpleUniverse u = null;
    
    public BranchGroup createSceneGraph() {
	// Create the root of the branch graph
	BranchGroup objRoot = new BranchGroup();

	// Create the TransformGroup node and initialize it to the
	// identity. Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at run time. Add it to
	// the root of the subgraph.
        Transform3D rot1 = new Transform3D();
        rot1.rotY(Math.PI/4);
	TransformGroup objTrans = new TransformGroup(rot1);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRoot.addChild(objTrans);

	// Create a simple Shape3D node; add it to the scene graph.
	//objTrans.addChild(new ColorCube(0.1));

        Random rnd = new Random();
//        Point3f p1 = new Point3f(x, y+0.05f, z);
//        Point3f p2 = new Point3f(x-sg*0.05f, y-0.05f, z);
//        Point3f p3 = new Point3f(x+sg*0.05f, y-0.05f, z);

        Point3f p1 = new Point3f(0, +0.03f, 0);
        Point3f p2 = new Point3f(-0.03f, -0.03f, 0);
        Point3f p3 = new Point3f(+0.03f, -0.03f, 0);
        Point3f p4 = new Point3f(+0, -0.03f, 0);

        Shape3D po = new Shape3D();
        PointArray pa = new PointArray(100, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
        Appearance app = new Appearance();
        PointAttributes patr = new PointAttributes(5,false);
        app.setPointAttributes(patr);

        Point3f verts[] = new Point3f[100];
        Color3f colors[] = new Color3f[100];
        
        for(int i=0;i<100;i++){
        float x = rnd.nextFloat()-0.5f;
        float y = rnd.nextFloat()-0.5f;
        float z = rnd.nextFloat()-0.5f;
        colors[i] = new Color3f(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat());
        verts[i] = new Point3f(x,y,z);
        }
        pa.setCoordinates(0,verts);
        pa.setColors(0,colors);
        po.setGeometry(pa);
        po.setAppearance(app);
        objTrans.addChild(po);

        po = new Shape3D();
        pa = new PointArray(100, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
        app = new Appearance();
        patr = new PointAttributes(6,true);
        app.setPointAttributes(patr);

        verts = new Point3f[100];
        colors = new Color3f[100];
        
        for(int i=0;i<100;i++){
        float x = rnd.nextFloat()-0.5f;
        float y = rnd.nextFloat()-0.5f;
        float z = rnd.nextFloat()-0.5f;
        colors[i] = new Color3f(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat());
        verts[i] = new Point3f(x,y,z);
        }
        pa.setCoordinates(0,verts);
        pa.setColors(0,colors);
        po.setGeometry(pa);
        po.setAppearance(app);
        objTrans.addChild(po);


        for(int i=0;i<100;i++){
        OrientedShape3D tr = new OrientedShape3D();
        float x = rnd.nextFloat()-0.5f;
        float y = rnd.nextFloat()-0.5f;
        float z = rnd.nextFloat()-0.5f;
        int sg = -1;
        if(rnd.nextDouble()>0.5)  sg=1;
        TriangleArray tetra = new TriangleArray(3,TriangleArray.COORDINATES | TriangleArray.NORMALS);
        tetra.setCoordinate(0,p1);
        tetra.setCoordinate(1,p2);
        tetra.setCoordinate(2,p4);
//        tetra.setCoordinate(3,p3);
//        tetra.setCoordinate(4,p1);

        tr.setGeometry(tetra);
        

        Transform3D rot2 = new Transform3D();
        //rot2.transpose(rot1);

        Transform3D tran = new Transform3D();
        Vector3f trv = new Vector3f(x,y,z);
        tran.set(trv);

        rot2.mul(tran);

        TransformGroup gr = new TransformGroup(rot2);
        gr.addChild(tr);
        objTrans.addChild(gr);}

        
	// Create a new Behavior object that will perform the
	// desired operation on the specified transform and add
	// it into the scene graph.
	Transform3D zAxis = new Transform3D();
	Alpha rotationAlpha = new Alpha(-1, 4000);

	RotationInterpolator rotator =
	    new RotationInterpolator(rotationAlpha, objTrans, zAxis,
				     0.0f, (float) Math.PI*2.0f);
	BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), .1);
	rotator.setSchedulingBounds(bounds);
	objRoot.addChild(rotator);

        for(int i=0;i<20;i++){
        StringBuffer sb = new StringBuffer();
        sb.append(i);
        Text2D text2D = new Text2D(sb.toString(), new Color3f(1.0f,0f,0f),
                                    "Helvetica", 12, Font.BOLD);
        Transform3D trt = new Transform3D();
        float x = rnd.nextFloat()-0.5f;
        float y = rnd.nextFloat()-0.5f;
        float z = rnd.nextFloat()-0.5f;

        trt.set(new Vector3f(x,y,z));
        TransformGroup ttr1 = new TransformGroup(trt);
        TransformGroup ttr = new TransformGroup();
        ttr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        ttr.addChild(ttr1);
        ttr1.addChild(text2D);
        objTrans.addChild(ttr);
        Billboard BB = new Billboard(ttr);
        BB.setSchedulingBounds(bounds);
        objTrans.addChild(BB);}
        
        // Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();

	return objRoot;
    }

    public Hello() {
    }

    public void init() {
	setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

	Canvas3D c = new Canvas3D(config);
        Panel p = new Panel(); 
        Button b = new Button("Fuck");
        p.add(b);
        add(p);
	add(c);
	//add("Center", c);

	// Create a simple scene and attach it to the virtual universe
	BranchGroup scene = createSceneGraph();
	u = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();

	u.addBranchGraph(scene);
    }

    public void destroy() {
	u.removeAllLocales();
    }

    //
    // The following allows HelloUniverse to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
	new MainFrame(new Hello(), 256, 256);
    }
}
