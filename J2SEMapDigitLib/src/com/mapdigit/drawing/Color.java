//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>Color</code> class is used to encapsulate colors in the default
 * sRGB color space  Every color has an implicit alpha value of 255 or
 * an explicit one provided in the constructor.  The alpha value
 * defines the transparency of a color and can be represented by
 * a int value in the range 0&nbsp;-&nbsp;255.
 * An alpha value of  255 means that the color is completely
 * opaque and an alpha value of 0 means that the color is
 * completely transparent.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
public class Color {

    //[------------------------------ CONSTANTS -------------------------------]
    /**
     * Represents image data that is guaranteed to be completely opaque,
     * meaning that all pixels have an alpha value of 255.
     */
    public final static int OPAQUE            = 1;

    /**
     * Represents image data that is guaranteed to be either completely
     * opaque, with an alpha value of 255, or completely transparent,
     * with an alpha value of 0.
     */
    public final static int BITMASK = 2;

    /**
     * Represents image data that contains or might contain arbitrary
     * alpha values between and including 0 and 255.
     */
    public final static int TRANSLUCENT        = 3;


    /**
     * The color white.
     */
    public final static Color white     = new Color(255, 255, 255);

    /**
     * The color white.
     */
    public final static Color WHITE = white;

    /**
     * The color light gray.
     */
    public final static Color lightGray = new Color(192, 192, 192);

    /**
     * The color light gray.  In the default sRGB space.
     */
    public final static Color LIGHT_GRAY = lightGray;

    /**
     * The color gray.
     */
    public final static Color gray      = new Color(128, 128, 128);

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static Color GRAY = gray;

    /**
     * The color dark gray.
     */
    public final static Color darkGray  = new Color(64, 64, 64);

    /**
     * The color dark gray.
     */
    public final static Color DARK_GRAY = darkGray;

    /**
     * The color black.
     */
    public final static Color black 	= new Color(0, 0, 0);

    /**
     * The color black.
     */
    public final static Color BLACK = black;

    /**
     * The color red.
     */
    public final static Color red       = new Color(255, 0, 0);

    /**
     * The color red.
     */
    public final static Color RED = red;

    /**
     * The color pink.
     */
    public final static Color pink      = new Color(255, 175, 175);

    /**
     * The color pink.  In the default sRGB space.
     */
    public final static Color PINK = pink;

    /**
     * The color orange.
     */
    public final static Color orange 	= new Color(255, 200, 0);

    /**
     * The color orange.
     */
    public final static Color ORANGE = orange;

    /**
     * The color yellow.
     */
    public final static Color yellow 	= new Color(255, 255, 0);

    /**
     * The color yellow.
     */
    public final static Color YELLOW = yellow;

    /**
     * The color green.
     */
    public final static Color green 	= new Color(0, 255, 0);

    /**
     * The color green.
     */
    public final static Color GREEN = green;

    /**
     * The color magenta.
     */
    public final static Color magenta	= new Color(255, 0, 255);

    /**
     * The color magenta.
     */
    public final static Color MAGENTA = magenta;

    /**
     * The color cyan.
     */
    public final static Color cyan 	= new Color(0, 255, 255);

    /**
     * The color cyan.
     */
    public final static Color CYAN = cyan;

    /**
     * The color blue.
     */
    public final static Color blue 	= new Color(0, 0, 255);

    /**
     * The color blue.
     */
    public final static Color BLUE = blue;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an opaque sRGB color with the specified red, green,
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
    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an sRGB color with the specified red, green, blue, and alpha
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
    public Color(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF) << 0);
	testColorValueRange(r,g,b,a);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
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
    public Color(int rgb) {
        value = 0xff000000 | rgb;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an sRGB color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>hasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param rgba the combined RGBA components
     * @param hasalpha <code>true</code> if the alpha bits are valid;
     *        <code>false</code> otherwise
     */
    public Color(int rgba, boolean hasalpha) {
        if (hasalpha) {
            value = rgba;
        } else {
            value = 0xff000000 | rgba;
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the red component in the range 0-255 in the default sRGB
     * space.
     * @return the red component.
     */
    public int getRed() {
	return (getRGB() >> 16) & 0xFF;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the green component in the range 0-255 in the default sRGB
     * space.
     * @return the green component.
     */
    public int getGreen() {
	return (getRGB() >> 8) & 0xFF;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the blue component in the range 0-255 in the default sRGB
     * space.
     * @return the blue component.
     */
    public int getBlue() {
	return (getRGB() >> 0) & 0xFF;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the alpha component in the range 0-255.
     * @return the alpha component.
     */
    public int getAlpha() {
        return (getRGB() >> 24) & 0xff;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the RGB value representing the color in the default sRGB
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     * @return the RGB value of the color in the default sRGB
     */
    public int getRGB() {
	return value;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a new <code>Color</code> that is a brighter version of this
     * <code>Color</code>.
     * <p>
     * This method applies an arbitrary scale factor to each of the three RGB
     * components of this <code>Color</code> to create a brighter version
     * of this <code>Color</code>. Although <code>brighter</code> and
     * <code>darker</code> are inverse operations, the results of a
     * series of invocations of these two methods might be inconsistent
     * because of rounding errors.
     * @return     a new <code>Color</code> object that is
     *                 a brighter version of this <code>Color</code>.
     */
    public Color brighter() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int)(1.0/(1.0-FACTOR));
        if ( r == 0 && g == 0 && b == 0) {
           return new Color(i, i, i);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/FACTOR), 255),
                         Math.min((int)(g/FACTOR), 255),
                         Math.min((int)(b/FACTOR), 255));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a new <code>Color</code> that is a darker version of this
     * <code>Color</code>.
     * <p>
     * This method applies an arbitrary scale factor to each of the three RGB
     * components of this <code>Color</code> to create a darker version of
     * this <code>Color</code>.  Although <code>brighter</code> and
     * <code>darker</code> are inverse operations, the results of a series
     * of invocations of these two methods might be inconsistent because
     * of rounding errors.
     * @return  a new <code>Color</code> object that is
     *                    a darker version of this <code>Color</code>.
     */
    public Color darker() {
	return new Color(Math.max((int)(getRed()  *FACTOR), 0),
			 Math.max((int)(getGreen()*FACTOR), 0),
			 Math.max((int)(getBlue() *FACTOR), 0));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the hash code for this <code>Color</code>.
     * @return     a hash code value for this object.
     */
    public int hashCode() {
	return value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether another object is equal to this
     * <code>Color</code>.
     * <p>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>Color</code> object that has the same
     * red, green, blue, and alpha values as this object.
     * @param       obj   the object to test for equality with this
     *				<code>Color</code>
     * @return      <code>true</code> if the objects are the same;
     *                             <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        return obj instanceof Color && ((Color)obj).value == this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string representation of this <code>Color</code>. This
     * method is intended to be used only for debugging purposes.  The
     * content and format of the returned string might vary between
     * implementations. The returned string might be empty but cannot
     * be <code>null</code>.
     *
     * @return  a string representation of this <code>Color</code>.
     */
    public String toString() {
        return getClass().getName() + "[r=" + getRed()
                + ",g=" + getGreen() + ",b=" + getBlue() + "]";
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
    public int getTransparency() {
        int alpha = getAlpha();
        if (alpha == 0xff) {
            return OPAQUE;
        }
        else if (alpha == 0) {
            return BITMASK;
        }
        else {
            return TRANSLUCENT;
        }
    }

    /**
     * The color value.
     */
    int value;

    private static final double FACTOR = 0.7;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Checks the color integer components supplied for validity.
     * Throws an {@link IllegalArgumentException} if the value is out of
     * range.
     * @param r the Red component
     * @param g the Green component
     * @param b the Blue component
     **/
    private static void testColorValueRange(int r, int g, int b, int a) {
        boolean rangeError = false;
	String badComponentString = "";

	if ( a < 0 || a > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Alpha";
	}
        if ( r < 0 || r > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Red";
	}
	if ( g < 0 || g > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Green";
	}
	if ( b < 0 || b > 255) {
	    rangeError = true;
	    badComponentString = badComponentString + " Blue";
	}
	if ( rangeError == true ) {
	throw new IllegalArgumentException("Color parameter outside of expected range:"
					   + badComponentString);
	}
    }

}
