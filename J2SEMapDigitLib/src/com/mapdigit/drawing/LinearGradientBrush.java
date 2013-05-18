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
import com.mapdigit.drawing.geometry.Rectangle;
import com.mapdigit.drawing.geometry.Point;
import com.mapdigit.util.MathEx;

import com.mapdigit.drawing.core.MathFP;
import com.mapdigit.drawing.core.LinearGradientBrushFP;
import com.mapdigit.drawing.core.RectangleFP;
import com.mapdigit.drawing.core.SingleFP;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The {@code LinearGradientBrush} class provides a way to fill
 * a IShape with a linear color gradient pattern.  The user
 * may specify two or more gradient colors, and this brush will provide an
 * interpolation between each color.  The user also specifies start and end
 * points which define where in user space the color gradient should begin 
 * and end.
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
 * The user may also select what action the {@code LinearGradientBrush}
 * should take when filling color outside the start and end points.
 * If no cycle method is specified, {@code NO_CYCLE} will be chosen by
 * default, which means the endpoint colors will be used to fill the
 * remaining area.
 * <p>
 * The following code demonstrates typical usage of
 * {@code LinearGradientBrush}:
 * <p>
 * <pre>
 *     Point start = new Point(0, 0);
 *     Point end = new Point(50, 50);
 *     int[] dist = {0, 100f, 255};
 *     Color[] colors = {Color.RED, Color.WHITE, Color.BLUE};
 *     LinearGradientBrush p =
 *         new LinearGradientBrush(start, end, dist, colors);
 * </pre>
 * <p>
 * This code will create a {@code LinearGradientBrush} which interpolates
 * between red and white for the first 20% of the gradient and between white
 * and blue for the remaining 80%.
 *
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class LinearGradientBrush extends Brush {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code LinearGradientBrush} with a default 
     * {@code NO_CYCLE} repeating method and {@code SRGB} color space.
     *
     * @param startX the X coordinate of the gradient axis start point 
     *               in user space
     * @param startY the Y coordinate of the gradient axis start point 
     *               in user space
     * @param endX   the X coordinate of the gradient axis end point 
     *               in user space
     * @param endY   the Y coordinate of the gradient axis end point 
     *               in user space
     * @param fractions numbers ranging from 0 to 255 specifying the 
     *                  distribution of colors along the gradient
     * @param colors array of colors corresponding to each fractional value
     *     
     * @throws NullPointerException
     * if {@code fractions} array is null,
     * or {@code colors} array is null,
     * @throws IllegalArgumentException
     * if start and end points are the same points,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 0.0 or greater than 1.0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public LinearGradientBrush(int startX, int startY,
            int endX, int endY,
            int[] fractions, Color[] colors, int fillType) {
        this(new Point(startX, startY),
                new Point(endX, endY),
                fractions,
                colors, fillType);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code LinearGradientBrush} with a default
     * {@code NO_CYCLE} repeating method and {@code SRGB} color space.
     *
     * @param start the gradient axis start {@code Point} in user space
     * @param end the gradient axis end {@code Point} in user space
     * @param fractions numbers ranging from 0 to 255 specifying the
     *                  distribution of colors along the gradient
     * @param colors array of colors corresponding to each fractional value
     *
     * @throws NullPointerException
     * if one of the points is null,
     * or {@code fractions} array is null,
     * or {@code colors} array is null
     * @throws IllegalArgumentException
     * if start and end points are the same points,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 0.0 or greater than 1.0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public LinearGradientBrush(Point start, Point end,
            int[] fractions, Color[] colors) {
        this(start, end,
                fractions, colors,
                NO_CYCLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code LinearGradientBrush} with a default {@code SRGB}
     * color space.
     *
     * @param start the gradient axis start {@code Point} in user space
     * @param end the gradient axis end {@code Point} in user space
     * @param fractions numbers ranging from 0 to 255 specifying the 
     *                  distribution of colors along the gradient
     * @param colors array of colors corresponding to each fractional value
     * @param fillType either {@code NO_CYCLE}, {@code REFLECT},
     *                    or {@code REPEAT}
     *   
     * @throws NullPointerException
     * if one of the points is null,
     * or {@code fractions} array is null,
     * or {@code colors} array is null,
     * or {@code cycleMethod} is null
     * @throws IllegalArgumentException
     * if start and end points are the same points,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 100 or greater than 0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public LinearGradientBrush(Point start, Point end,
            int[] fractions, Color[] colors,
            int fillType) {
        this(start, end,
                fractions, colors,
                new AffineTransform(), fillType);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code LinearGradientBrush} with two colors and angle.
     * @param rect rectangle area of the linear gradien brush.
     * @param color1 start color
     * @param color2 end color
     * @param angle the anagle from start color to the end color.
     */
    public LinearGradientBrush(Rectangle rect, Color color1, Color color2,
            float angle) {
        this(rect, color1, color2, angle, NO_CYCLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a {@code LinearGradientBrush} with two colors and angle.
     * @param rect rectangle area of the linear gradien brush.
     * @param color1 start color
     * @param color2 end color
     * @param angle the anagle from start color to the end color.
     * @param fillType fill type.
     */
    public LinearGradientBrush(Rectangle rect, Color color1, Color color2,
            float angle, int fillType) {
        start = new Point(rect.x, rect.y);
        end = new Point(rect.x + rect.width, rect.y + rect.height);
        gradientTransform = new AffineTransform();
        fractions = new int[]{0, 100};
        colors = new Color[]{color1, color2};
        boolean opaque = true;
        for (int i = 0; i < colors.length; i++) {
            opaque = opaque && (colors[i].getAlpha() == 0xff);
        }
        this.transparency = opaque ? Color.OPAQUE : Color.TRANSLUCENT;
        RectangleFP r = Utils.toRectangleFP(rect);
        wrappedBrushFP = new LinearGradientBrushFP(r.getLeft(), r.getTop(),
                r.getRight(), r.getBottom(),
                MathFP.toRadians(SingleFP.fromFloat(angle)));
        for (int i = 0; i < colors.length; i++) {
            ((LinearGradientBrushFP) wrappedBrushFP)
                    .setGradientColor(SingleFP.fromFloat((float) fractions[i]
                    / 255.0f),
                    colors[i].value);
        }
        ((LinearGradientBrushFP) wrappedBrushFP).updateGradientTable();
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
     * Constructs a {@code LinearGradientBrush}.
     *
     * @param start the gradient axis start {@code Point} in user space
     * @param end the gradient axis end {@code Point} in user space
     * @param fractions numbers ranging from 0 to 255 specifying the 
     *                  distribution of colors along the gradient
     * @param colors array of colors corresponding to each fractional value
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
     * if start and end points are the same points,
     * or {@code fractions.length != colors.length},
     * or {@code colors} is less than 2 in size,
     * or a {@code fractions} value is less than 0.0 or greater than 1.0,
     * or the {@code fractions} are not provided in strictly increasing order
     */
    public LinearGradientBrush(Point start, Point end,
            int[] fractions, Color[] colors,
            AffineTransform gradientTransform, int fillType) {

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

        // check input parameters
        if (start == null || end == null) {
            throw new NullPointerException("Start and end points must be" +
                    "non-null");
        }

        if (start.equals(end)) {
            throw new IllegalArgumentException("Start point cannot equal" +
                    "endpoint");
        }

        // copy the points...
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());

        Rectangle rectangle = new Rectangle(start, end);
        float dx = start.x - end.x;
        float dy = start.y - end.y;
        double angle = MathEx.atan2(dy, dx);
        int intAngle = SingleFP.fromDouble(angle);
        RectangleFP r = Utils.toRectangleFP(rectangle);
        wrappedBrushFP = new LinearGradientBrushFP(r.getLeft(), r.getTop(),
                r.getRight(), r.getBottom(),
                intAngle);
        for (int i = 0; i < colors.length; i++) {
            ((LinearGradientBrushFP) wrappedBrushFP).setGradientColor
                    (SingleFP.fromFloat((float) fractions[i] / 100.0f),
                    colors[i].value);
        }
        ((LinearGradientBrushFP) wrappedBrushFP).updateGradientTable();
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
     * Returns a copy of the start point of the gradient axis.
     *
     * @return a {@code Point} object that is a copy of the point
     * that anchors the first color of this {@code LinearGradientBrush}
     */
    public Point getStartPoint() {
        return new Point(start.getX(), start.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a copy of the end point of the gradient axis.
     *
     * @return a {@code Point} object that is a copy of the point
     * that anchors the last color of this {@code LinearGradientBrush}
     */
    public Point getEndPoint() {
        return new Point(end.getX(), end.getY());
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

    // Gradient start and end points.
    private final Point start,  end;
    private final AffineTransform gradientTransform;
    // Gradient keyframe values in the range 0 to 1.
    private final int[] fractions;
    // Gradient colors. 
    private final Color[] colors;
    // The transparency of this paint object.
    final int transparency;
}
