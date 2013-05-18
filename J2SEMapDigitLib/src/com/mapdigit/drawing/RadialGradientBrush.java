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
import com.mapdigit.drawing.geometry.AffineTransform;
import com.mapdigit.drawing.geometry.Point;
import com.mapdigit.drawing.core.RadialGradientBrushFP;
import com.mapdigit.drawing.core.SingleFP;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The {@code RadialGradientBrush} class provides a way to fill a shape with
 * a circular radial color gradient pattern. The user may specify 2 or more
 * gradient colors, and this paint will provide an interpolation between
 * each color.
 * <p>
 * The user must specify the circle controlling the gradient pattern,
 * which is described by a center point and a radius.  The user can also
 * specify a separate focus point within that circle, which controls the
 * location of the first color of the gradient.  By default the focus is
 * set to be the center of the circle.
 * <p>
 * This paint will map the first color of the gradient to the focus point,
 * and the last color to the perimeter of the circle, interpolating
 * smoothly for any in-between colors specified by the user.  Any line drawn
 * from the focus point to the circumference will thus span all the gradient
 * colors.
 * <p>
 * Specifying a focus point outside of the circle's radius will result in the
 * focus being set to the intersection point of the focus-center line and the
 * perimeter of the circle.
 * <p>
 * The user must provide an array of integers specifying how to distribute the
 * colors along the gradient.  These values should range from 0 to 255 and
 * act like keyframes along the gradient (they mark where the gradient should
 * be exactly a particular color).
 * <p>
 * In the event that the user does not set the first keyframe value equal
 * to 0 and/or the last keyframe value equal to 255, keyframes will be created
 * at these positions and the first and last colors will be replicated there.
 * So, if a user specifies the following arrays to construct a gradient:<br>
 * <pre>
 *     {Color.BLUE, Color.RED}, {100, 140}
 * </pre>
 * this will be converted to a gradient with the following keyframes:<br>
 * <pre>
 *     {Color.BLUE, Color.BLUE, Color.RED, Color.RED}, {0, 100, 140, 255}
 * </pre>
 *
 * <p>
 * The user may also select what action the {@code RadialGradientBrush}
 * should take when filling color outside the bounds of the circle's radius.
 * If no cycle method is specified, {@code NO_CYCLE} will be chosen by
 * default, which means the the last keyframe color will be used to fill the
 * remaining area.
 * <p>
 * The following code demonstrates typical usage of
 * {@code RadialGradientBrush}, where the center and focus points are
 * the same:
 * <p>
 * <pre>
 *     Point center = new Point(50, 50);
 *     int radius = 25;
 *     int[] dist = {0, 52, 255};
 *     Color[] colors = {Color.RED, Color.WHITE, Color.BLUE};
 *     RadialGradientBrush p =
 *         new RadialGradientBrush(center, radius, dist, colors);
 * </pre>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class RadialGradientBrush extends Brush {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code RadialGradientBrush} with a default
     * {@code NO_CYCLE} repeating method and {@code SRGB} color space,
     * using the center as the focus point.
     *
     * @param cx the X coordinate in user space of the center point of the
     *           circle defining the gradient.  The last color of the
     *           gradient is mapped to the perimeter of this circle.
     * @param cy the Y coordinate in user space of the center point of the
     *           circle defining the gradient.  The last color of the
     *           gradient is mapped to the perimeter of this circle.
     * @param radius the radius of the circle defining the extents of the
     *               color gradient
     * @param fractions numbers ranging from 0.0 to 1.0 specifying the
     *                  distribution of colors along the gradient
     * @param colors array of colors to use in the gradient.  The first color
     *               is used at the focus point, the last color around the
     *               perimeter of the circle.
     *
     * @throws NullPointerException
     * if {@code fractions} array is null,
     * or {@code colors} array is null
     * @throws IllegalArgumentException
     * if {@code radius} is non-positive,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 0.0 or greater than 1.0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public RadialGradientBrush(int cx, int cy, int radius,
            int[] fractions, Color[] colors) {
        this(new Point(cx, cy),
                radius,
                fractions,
                colors,
                NO_CYCLE, new AffineTransform());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code RadialGradientBrush} with a default
     * {@code NO_CYCLE} repeating method and {@code SRGB} color space,
     * using the center as the focus point.
     *
     * @param center the center point, in user space, of the circle defining
     *               the gradient
     * @param radius the radius of the circle defining the extents of the
     *               color gradient
     * @param fractions numbers ranging from 0.0 to 1.0 specifying the
     *                  distribution of colors along the gradient
     * @param colors array of colors to use in the gradient.  The first color
     *               is used at the focus point, the last color around the
     *               perimeter of the circle.
     *
     * @throws NullPointerException
     * if {@code center} point is null,
     * or {@code fractions} array is null,
     * or {@code colors} array is null
     * @throws IllegalArgumentException
     * if {@code radius} is non-positive,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 0.0 or greater than 1.0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public RadialGradientBrush(Point center, int radius,
            int[] fractions, Color[] colors) {
        this(center,
                radius,
                fractions,
                colors,
                NO_CYCLE, new AffineTransform());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code RadialGradientBrush}.
     *
     * @param center the center point in user space of the circle defining the
     *               gradient.  The last color of the gradient is mapped to
     *               the perimeter of this circle.
     * @param radius the radius of the circle defining the extents of the
     *               color gradient
     * @param fractions numbers ranging from 0.0 to 1.0 specifying the
     *                  distribution of colors along the gradient
     * @param colors array of colors to use in the gradient.  The first color
     *               is used at the focus point, the last color around the
     *               perimeter of the circle.
     * @param fillType either {@code NO_CYCLE}, {@code REFLECT},
     *                    or {@code REPEAT}
     * @param gradientTransform transform to apply to the gradient
     *
     * @throws NullPointerException
     * if one of the points is null,
     * or {@code fractions} array is null,
     * or {@code colors} array is null,
     * or {@code cycleMethod} is null,
     * or {@code colorSpace} is null,
     * or {@code gradientTransform} is null
     * @throws IllegalArgumentException
     * if {@code radius} is non-positive,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 0.0 or greater than 1.0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public RadialGradientBrush(Point center,
            int radius,
            int[] fractions, Color[] colors,
            int fillType,
            AffineTransform gradientTransform) {
        if (fractions == null) {
            throw new NullPointerException("Fractions array cannot be null");
        }

        if (colors == null) {
            throw new NullPointerException("Colors array cannot be null");
        }

        if (gradientTransform == null) {
            throw new NullPointerException("Gradient transform cannot be " +
                    "null");
        }

        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Colors and fractions must " +
                    "have equal size");
        }

        if (colors.length < 2) {
            throw new IllegalArgumentException("User must specify at least " +
                    "2 colors");
        }

        // check that values are in the proper range and progress
        // in increasing order from 0 to 1
        int previousFraction = -255;
        for (int i = 0; i < fractions.length; i++) {
            int currentFraction = fractions[i];
            if (currentFraction < 0 || currentFraction > 255) {
                throw new IllegalArgumentException("Fraction values must " +
                        "be in the range 0 to 255: " +
                        currentFraction);
            }

            if (currentFraction <= previousFraction) {
                throw new IllegalArgumentException("Keyframe fractions " +
                        "must be increasing: " +
                        currentFraction);
            }

            previousFraction = currentFraction;
        }

        // We have to deal with the cases where the first gradient stop is not
        // equal to 0 and/or the last gradient stop is not equal to 1.
        // In both cases, create a new point and replicate the previous
        // extreme point's color.
        boolean fixFirst = false;
        boolean fixLast = false;
        int len = fractions.length;
        int off = 0;

        if (fractions[0] != 0) {
            // first stop is not equal to zero, fix this condition
            fixFirst = true;
            len++;
            off++;
        }
        if (fractions[fractions.length - 1] != 255) {
            // last stop is not equal to one, fix this condition
            fixLast = true;
            len++;
        }

        this.fractions = new int[len];
        System.arraycopy(fractions, 0, this.fractions, off, fractions.length);
        this.colors = new Color[len];
        System.arraycopy(colors, 0, this.colors, off, colors.length);

        if (fixFirst) {
            this.fractions[0] = 0;
            this.colors[0] = colors[0];
        }
        if (fixLast) {
            this.fractions[len - 1] = 255;
            this.colors[len - 1] = colors[colors.length - 1];
        }

        // copy the gradient transform
        this.gradientTransform = new AffineTransform(gradientTransform);

        // determine transparency
        boolean opaque = true;
        for (int i = 0; i < colors.length; i++) {
            opaque = opaque && (colors[i].getAlpha() == 0xff);
        }
        this.transparency = opaque ? Color.OPAQUE : Color.TRANSLUCENT;


        // check input arguments
        if (center == null) {
            throw new NullPointerException("Center point must be non-null");
        }


        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater " +
                    "than zero");
        }

        // copy parameters
        this.center = new Point(center.x, center.y);
        this.radius = radius;
        this.fillType = fillType;


        wrappedBrushFP = new RadialGradientBrushFP(SingleFP.fromInt(center.x),
                SingleFP.fromInt(center.y), SingleFP.fromInt(radius), 0);
        for (int i = 0; i < colors.length; i++) {
            ((RadialGradientBrushFP) wrappedBrushFP)
                    .setGradientColor
                    (SingleFP.fromFloat((float) fractions[i] / 100.0f),
                    colors[i].value);
        }
        ((RadialGradientBrushFP) wrappedBrushFP).updateGradientTable();
        wrappedBrushFP.setMatrix(Utils.toMatrixFP(gradientTransform));
        wrappedBrushFP.fillMode = fillType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a copy of the center point of the radial gradient.
     *
     * @return a {@code Point} object that is a copy of the center point
     */
    public Point getCenterPoint() {
        return new Point(center.x, center.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the radius of the circle defining the radial gradient.
     *
     * @return the radius of the circle defining the radial gradient
     */
    public int getRadius() {
        return radius;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get fill type of the radial gradient brush
     * @return the fill type.
     */
    public int getFillType() {
        return fillType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a copy of the transform applied to the gradient.
     *
     * @return a copy of the transform applied to the gradient
     */
    public final AffineTransform getTransform() {
        return gradientTransform;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a copy of the array of floats used by this gradient
     * to calculate color distribution.
     * The returned array always has 0 as its first value and 1 as its
     * last value, with increasing values in between.
     *
     * @return a copy of the array of floats used by this gradient to
     * calculate color distribution
     */
    public final int[] getFractions() {
        return fractions;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a copy of the array of colors used by this gradient.
     * The first color maps to the first value in the fractions array,
     * and the last color maps to the last value in the fractions array.
     *
     * @return a copy of the array of colors used by this gradient
     */
    public final Color[] getColors() {
        return colors;
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
        return transparency;
    }

    // Center of the circle defining the 100% gradient stop X coordinate.
    private final Point center;
    // Radius of the outermost circle defining the 100% gradient stop.
    private final int radius;
    private final int fillType;
    private final AffineTransform gradientTransform;
    // Gradient keyframe values in the range 0 to 1.
    private final int[] fractions;
    // Gradient colors.
    private final Color[] colors;
    // The transparency of this paint object.
    final int transparency;
}
