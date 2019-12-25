/*
 * @(#)JPEGFlip.java	1.6  98/12/03
 *
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package vdaoengine;

import java.awt.*;
import java.awt.event.*;
import com.sun.image.codec.jpeg.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.geom.GeneralPath;
import java.io.*;
import javax.swing.*;


/**
 * Renders a filled star & duke into a BufferedImage, saves the BufferedImage
 * as a JPEG, displays the BufferedImage using the decoded JPEG BufferedImage
 * DataBuffer, flips the elements, displays the JPEG flipped BufferedImage.
 */
public class JPEGFlip extends JApplet {

    private static Image img;

    public void init() {
        setBackground(Color.white);
        img = getToolkit().getImage(JPEGFlip.class.getResource("duke.gif"));
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {}
    }

    public void drawDemo(int w, int h, Graphics2D g2) {

        int hh = h/2;

        BufferedImage bi = (BufferedImage) createImage(w, hh);
        Graphics2D big = bi.createGraphics();

        // .. use rendering hints from DemoSurface ..
        big.setRenderingHints(g2.getRenderingHints());
        big.setBackground(getBackground());
        big.clearRect(0, 0, w, hh);
        big.setColor(Color.green.darker());

        /*
         * creates a new general path which represents the outline of
         * the green star
         */
        GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        p.moveTo(- w / 2.0f, - hh / 8.0f);
        p.lineTo(+ w / 2.0f, - hh / 8.0f);
        p.lineTo(- w / 4.0f, + hh / 2.0f);
        p.lineTo(+     0.0f, - hh / 2.0f);
        p.lineTo(+ w / 4.0f, + hh / 2.0f);
        p.closePath();
        big.translate(w/2, hh/2);
        big.fill(p);

        int iw = img.getWidth(this);
        int ih = img.getHeight(this);

        /*
         * if the image of duke is too big for the upper half of the
         * window, decreases ih
         */
        if (hh < ih * 1.5) {
            ih = (int) (ih * ((hh / (ih*1.5))));
        }
        big.drawImage(img, -img.getWidth(this)/2, -ih/2, iw, ih, this);

        g2.drawImage(bi, 0, 0, this);
        g2.setFont(new Font("Dialog", Font.PLAIN, 10));
        g2.setColor(Color.black);
        g2.drawString("BufferedImage", 4, 12);


        BufferedImage bi1 = null;

        try {
	    /*
             * To write the jpeg to a file uncomment the File* lines and
             * comment out the ByteArray*Stream lines.
             * File file = new File("images", "test.jpg");
             * FileOutputStream out = new FileOutputStream(file);
             */

            File file = new File("test.jpg");
            FileOutputStream out = new FileOutputStream(file);

	    // encodes bi as a JPEG data stream
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            // Have to comment it due to incompativility with java 7
            /*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
            param.setQuality(1f, false);
            encoder.setJPEGEncodeParam(param);
            encoder.encode(bi);

            FileInputStream in = new FileInputStream(file);

            // decodes the JPEG data stream into a BufferedImage
            //ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
            bi1 = decoder.decodeAsBufferedImage();
            
            */
        } catch (Exception ex) {
            ex.printStackTrace();
            g2.setColor(Color.red);
            g2.drawString("write permissions?", 5, hh*2-5);
        }

        if (bi1 == null) {
            g2.setColor(Color.red);
            g2.drawString("decodeAsBufferedImage=null", 5, hh*2-5);
            return;
        }

        /*
         * transfers BufferedImage data elements from bi1 to bi2,  bi1
         * elements are reversed into bi2.
         */
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(),bi1.getHeight(),bi1.getType());
        DataBuffer db1 = bi1.getRaster().getDataBuffer();
        DataBuffer db2 = bi2.getRaster().getDataBuffer();
        for (int i = db1.getSize()-1, j = 0; i >= 0; --i, j++) {
            db2.setElem(j, db1.getElem(i));
        }

        // draws the flipped image onto the bottom half of the window
        g2.drawImage(bi2, 0, hh, this);

        g2.drawString("JPEGImage Flipped", 4, hh*2-4);
        g2.drawLine(0, hh, w, hh);
    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
	Dimension d = getSize();
        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        drawDemo(d.width, d.height, g2);
    }


    public static void main(String argv[]) {
        final JPEGFlip demo = new JPEGFlip();
        demo.init();
        JFrame f = new JFrame("Java 2D(TM) Demo - JPEGFlip");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(400,300));
        f.show();
    }
}
