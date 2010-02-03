/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.WritableRaster;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class Util {
    public static final Quaternion Z45  = new Quaternion().fromAngleAxis( FastMath.QUARTER_PI, Vector3f.UNIT_Z );
    public static final Quaternion Z90  = new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Z );
    public static final Quaternion Z180 = new Quaternion().fromAngleAxis( FastMath.PI, Vector3f.UNIT_Z );
    public static final Quaternion Z270 = new Quaternion().fromAngleAxis( FastMath.PI*3/2, Vector3f.UNIT_Z );
    public static final Quaternion Y45  = new Quaternion().fromAngleAxis( FastMath.QUARTER_PI, Vector3f.UNIT_Y );
    public static final Quaternion Y90  = new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Y );
    public static final Quaternion Y180 = new Quaternion().fromAngleAxis( FastMath.PI, Vector3f.UNIT_Y );
    public static final Quaternion Y270 = new Quaternion().fromAngleAxis( FastMath.PI*3/2, Vector3f.UNIT_Y );
    public static final Quaternion X45  = new Quaternion().fromAngleAxis( FastMath.QUARTER_PI, Vector3f.UNIT_X );
    public static final Quaternion X90  = new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_X );
    public static final Quaternion X180 = new Quaternion().fromAngleAxis( FastMath.PI, Vector3f.UNIT_X);
    public static final Quaternion X270 = new Quaternion().fromAngleAxis( FastMath.PI*3/2, Vector3f.UNIT_X );
    public static final Quaternion INVY = new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X );
    
    
    public static String extensionOf( String file ) {
        String fileName = new String( file );
        int lastDot = fileName.lastIndexOf( '.' );
        String extension;
        if ( lastDot >= 0 ) {
            extension = fileName.substring( lastDot + 1 );
        } else {
            extension = "";
        }
        return extension;
    }
    
    
    public static BufferedImage grayScaleToAlpha(BufferedImage src) {
    	// Create a pure grayscale image
    	BufferedImage temp = ImageConverter.toBufferedImage(src,BufferedImage.TYPE_BYTE_GRAY);
    	// Make a nice byte stream of it
    	byte[] tempData=(byte[])temp.getRaster().getDataElements(0, 0, temp.getWidth(), temp.getHeight(), null);
    	// Create our destination image
    	BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
    			BufferedImage.TYPE_INT_ARGB);
    	// The other stream...
    	@SuppressWarnings("unused")
		int[] destData=(int[])dest.getRaster().getDataElements(0, 0, dest.getWidth(), dest.getHeight(), null);
    	// How many bytes are stored per pixel
    	int destBytesPerPixel=4;
    	int tempBytesPerPixel=1;

    	for(int h=0;h<dest.getHeight();h++) {
    		for(int w=0;w<dest.getWidth();w++) {
    			@SuppressWarnings("unused")
				int destIndex=(h*destBytesPerPixel*dest.getHeight())+(w*destBytesPerPixel);
    			int tempIndex=(h*tempBytesPerPixel*temp.getHeight())+(w*tempBytesPerPixel);
    			set(dest,w,h,tempData[tempIndex]);
    		}
    	}
    	return dest;
    }

    public static void set(BufferedImage img,int x, int y, int value) {
    	int rgb = 0x00FFFFFF | (value << 24);
    	img.setRGB(x, y, rgb);
    }

    static private class ImageConverter
    {
    	// Posted by DrLaszloJamf to Java Technology Forums
    	//
    	// Copyright 1994-2004 Sun Microsystems, Inc. All Rights Reserved. 
    	// Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
    	//
    	// Redistribution of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    	//
    	// Redistribution in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
    	//
    	// Neither the name of Sun Microsystems, Inc. or the names of contributors may be used to endorse or promote products derived from this software without specific prior written permission. 
    	// 
    	// This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. 
    	//
    	//
    	// You acknowledge that this software is not designed, licensed or intended for use in the design, construction, operation or maintenance of any nuclear facility. 

    	//    preserves image's colormodel. Assumes image is loaded
    	@SuppressWarnings("unused")
		public static BufferedImage toBufferedImage(Image image)
    	{
    		if (image instanceof BufferedImage)
    			return (BufferedImage)image;
    		ColorModel cm = getColorModel(image);
    		int width = image.getWidth(null);
    		int height = image.getHeight(null);
    		return copy(createBufferedImage(cm, width, height), image);
    	}

    	public static BufferedImage toBufferedImage(Image image, int type)
    	{
    		if (image instanceof BufferedImage
    				&& ((BufferedImage)image).getType() == type)
    			return (BufferedImage)image;
    		int width = image.getWidth(null);
    		int height = image.getHeight(null);
    		return copy(new BufferedImage(width, height, type), image);
    	}

    	//    Returns target. Assumes source is loaded
    	public static BufferedImage copy(BufferedImage target, Image source)
    	{
    		Graphics2D g = target.createGraphics();
    		g.drawImage(source, 0, 0, null);
    		g.dispose();
    		return target;
    	}

    	public static ColorModel getColorModel(Image image)
    	{
    		try
    		{
    			PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
    			pg.grabPixels();
    			return pg.getColorModel();
    		}
    		catch (InterruptedException e)
    		{
    			throw new RuntimeException("Unexpected interruption", e);
    		}
    	}

    	public static BufferedImage createBufferedImage(
    			ColorModel cm,
    			int w,
    			int h)
    	{
    		WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
    		boolean isRasterPremultiplied = cm.isAlphaPremultiplied();
    		return new BufferedImage(cm, raster, isRasterPremultiplied, null);
    	}
    }
}