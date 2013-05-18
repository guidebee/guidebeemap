//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.drawing.core.SolidBrushFP;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Defines a brush of a single color. Brushes are used to fill graphics shapes,
 * such as rectangles, ellipses, pies, polygons, and paths. This class cannot
 * be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class SolidBrush extends Brush{

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an opaque sRGB brush with the specified red, green,
     * and blue values in the range (0 - 255).
     * The actual color used in rendering depends
     * on finding the best match given the color space
     * available for a given output device.
     * Alpha is defaulted to 255.
     *
     * @throws IllegalArgumentException if <code>r</code>, <code>g</code>
     *        or <code>b</code> are outside of the range
     *        0 to 255, inclusive
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     */
    public SolidBrush(int r, int g, int b) {
        brushColor=new Color(r, g, b);
        wrappedBrushFP=new SolidBrushFP(brushColor.value);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an sRGB brush with the specified red, green, blue, and alpha
     * values in the range (0 - 255).
     *
     * @throws IllegalArgumentException if <code>r</code>, <code>g</code>,
     *        <code>b</code> or <code>a</code> are outside of the range
     *        0 to 255, inclusive
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */
    public SolidBrush(int r, int g, int b, int a) {
        brushColor=new Color(r, g, b,a);
        wrappedBrushFP=new SolidBrushFP(brushColor.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an opaque sRGB brush with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param rgb the combined RGB components
     */
    public SolidBrush(int rgb) {
        brushColor=new Color(rgb);
        wrappedBrushFP=new SolidBrushFP(brushColor.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an sRGB brush with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param rgba the combined RGBA components
     * @param hasalpha <code>true</code> if the alpha bits are valid;
     *        <code>false</code> otherwise
     */
    public SolidBrush(int rgba, boolean hasalpha) {
        brushColor=new Color(rgba,hasalpha);
        wrappedBrushFP=new SolidBrushFP(brushColor.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an sRGB brush with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param color the color of the brush
     */
    public SolidBrush(Color color) {
        brushColor=color;
        wrappedBrushFP=new SolidBrushFP(brushColor.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the color of the solid brush.
     * @return the color of the brush.
     */
    public Color getColor(){
        return brushColor;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the transparency mode for this <code>Color</code>.  This is
     * required to implement the <code>Paint</code> interface.
     * @return this <code>Color</code> object's transparency mode.
     */
    public int getTransparency(){
        return brushColor.getTransparency();
    }

    //the color of the solid brush
    private Color brushColor=null;

}
