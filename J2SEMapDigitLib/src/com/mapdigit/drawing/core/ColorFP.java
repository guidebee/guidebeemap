//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 18APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.core;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>ColorFP</code> class is used to encapsulate colors in the default
 * sRGB color space  Every color has an implicit alpha value of 1.0 or
 * an explicit one provided in the constructor.  The alpha value
 * defines the transparency of a color and can be represented by
 * a int value in the range 0&nbsp;-&nbsp;255.
 * An alpha value of  255 means that the color is completely
 * opaque and an alpha value of 0 means that the color is
 * completely transparent.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 18/04/10
 * @author      Guidebee Pty Ltd.
 */
public class ColorFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a Color structure from the four 8-bit ARGB components
     * (alpha, red, green, and blue) values.
     * @param color A value specifying the 32-bit ARGB value.
     * @return The Color object that this method creates.
     */
    public static ColorFP fromArgb(int color) {
        return new ColorFP(color);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a Color structure from the four 8-bit ARGB components
     * (alpha, red, green, and blue) values.
     * @param color A value specifying the 32-bit ARGB value.
     * @return The Color object that this method creates.
     */
    public static ColorFP fromArgb(int red, int green, int blue) {
        int value =
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                ((blue & 0xFF) << 0);
        return new ColorFP(value);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an opaque sRGB color with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param rgb the combined RGB components
     */
    public ColorFP(int rgb) {
        value = rgb;
        red=(value >> 16) & 0xFF;
        green=(value >> 8) & 0xFF;
        blue=value & 0xFF;
        alpha=(value >> 24) & 0xff;
    }

    
    /**
     * The color value.
     */
    public int value;

    /**
     * the red component.
     */
    public int red;

    /**
     * the green compoent
     */
    public int green;

    /**
     * the blue component.
     */
    public int blue;

    /**
     * the alpha compoent.
     */
    public int alpha;
    
}
