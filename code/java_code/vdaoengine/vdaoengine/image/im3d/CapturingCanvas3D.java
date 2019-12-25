package vdaoengine.image.im3d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.image.codec.jpeg.*;
import vdaoengine.special.GIFOutputStream;

/** Class CapturingCanvas3D, using the instructions from the Java3D
    FAQ pages on how to capture a still image in jpeg format.

    A capture button would call a method that looks like


    public static void captureImage(CapturingCanvas3D MyCanvas3D) {
	MyCanvas3D.writeJPEG_ = true;
	MyCanvas3D.repaint();
    }


    Peter Z. Kunszt
    Johns Hopkins University
    Dept of Physics and Astronomy
    Baltimore MD
*/

public class CapturingCanvas3D extends Canvas3D  {

    public boolean writeGIF_ = false;
    private int postSwapCount_;
    public String captureFileName = new String("capture.gif");
    public Dimension captureSize = new Dimension(512,512);

    public CapturingCanvas3D(GraphicsConfiguration gc) {
	super(gc);
	postSwapCount_ = 0;
    }

    public void postSwap() {
	if(writeGIF_) {
	    System.out.println("Writing GIF");
	    GraphicsContext3D  ctx = getGraphicsContext3D();
	    // The raster components need all be set!
	    Raster ras = new Raster(
                   new Point3f(-1.0f,-1.0f,-1.0f),
		   Raster.RASTER_COLOR,
		   0,0,
		   (int)(captureSize.getWidth()),(int)(captureSize.getWidth()),
		   new ImageComponent2D(
                             ImageComponent.FORMAT_RGB,
			     new BufferedImage((int)(captureSize.getWidth()),(int)(captureSize.getWidth()),
					       BufferedImage.TYPE_INT_RGB)),
		   null);

	    ctx.readRaster(ras);

	    // Now strip out the image info
	    BufferedImage img = ras.getImage().getImage();

	    // write that to disk....
	    try {
		//FileOutputStream out = new FileOutputStream("Capture"+postSwapCount_+".jpg");
		//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		//JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
		//param.setQuality(0.9f,false); // 90% qualith JPEG
		//encoder.setJPEGEncodeParam(param);
		//encoder.encode(img);
                FileOutputStream out = new FileOutputStream(captureFileName);
                GIFOutputStream G = new GIFOutputStream(out);
                G.write(img);
		writeGIF_ = false;
		out.close();
	    } catch ( IOException e ) {
		System.out.println("I/O exception!");
	    }
	    postSwapCount_++;
	}
    }
}
