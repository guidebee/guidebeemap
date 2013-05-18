//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 06NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.collections.Arrays;
import com.mapdigit.drawing.geometry.parser.PathParser;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 06NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The {@code Path} class provides a simple, yet flexible
 * shape which represents an arbitrary geometric path.
 * It can fully represent any path which can be iterated by the
 * {@link IPathIterator} interface including all of its segment
 * types and winding rules and it implements all of the
 * basic hit testing methods of the {@link IShape} interface.
 * <p>
 * {@code Path} provides exactly those facilities required for
 * basic construction and management of a geometric path and
 * implementation of the above interfaces with little added
 * interpretation.
 * If it is useful to manipulate the interiors of closed
 * geometric shapes beyond simple hit testing then the
 * {@link Area} class provides additional capabilities
 * specifically targeted at closed figures.
 * While both classes nominally implement the {@code IShape}
 * interface, they differ in purpose and together they provide
 * two useful views of a geometric shape where {@code Path}
 * deals primarily with a trajectory formed by path segments
 * and {@code Area} deals more with interpretation and manipulation
 * of enclosed regions of 2D geometric space.
 * <p>
 * The {@link IPathIterator} interface has more detailed descriptions
 * of the types of segments that make up a path and the winding rules
 * that control how to determine which regions are inside or outside
 * the path.
 *
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 06/11/08
 * @author      Guidebee Pty Ltd.
 */
public class Path implements IShape {

    /**
     * An even-odd winding rule for determining the interior of
     * a path.
     */
    public static final int WIND_EVEN_ODD = IPathIterator.WIND_EVEN_ODD;
    /**
     * A non-zero winding rule for determining the interior of a
     * path.
     */
    public static final int WIND_NON_ZERO = IPathIterator.WIND_NON_ZERO;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20NOV2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses Path object from a path data input string that contains the moveto
     * , line, curve (both cubic and quadratic Beziers) and closepath
     * instructions. For example, "M 100 100 L 300 100 L 200 300 z". . *
     * @param input path input string
     * @return path object.
     */
    public static Path fromString(String input){
        synchronized(pathParser){
            return pathParser.parsePath(input);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new int precision {@code Path} object
     * from an arbitrary {@link IShape} object, transformed by an
     * {@link AffineTransform} object.
     * All of the initial geometry and the winding rule for this path are
     * taken from the specified {@code IShape} object and transformed
     * by the specified {@code AffineTransform} object.
     *
     * @param s the specified {@code IShape} object
     * @param at the specified {@code AffineTransform} object
     */
    public Path(IShape s, AffineTransform at) {
        if (s instanceof Path) {
            Path p2d = (Path) s;
            setWindingRule(p2d.windingRule);
            this.numTypes = p2d.numTypes;
            this.pointTypes = Arrays.copyOf(p2d.pointTypes,
                    p2d.pointTypes.length);
            this.numCoords = p2d.numCoords;
            this.intCoords = p2d.cloneCoords(at);
        } else {
            IPathIterator pi = s.getPathIterator(at);
            setWindingRule(pi.getWindingRule());
            this.pointTypes = new byte[INIT_SIZE];
            this.intCoords = new int[INIT_SIZE * 2];
            append(pi, false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new empty int precision {@code Path} object
     * with a default winding rule of {@link #WIND_NON_ZERO}.
     */
    public Path() {
        this(WIND_NON_ZERO, INIT_SIZE);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new empty int precision {@code Path} object
     * with the specified winding rule to control operations that
     * require the interior of the path to be defined.
     *
     * @param rule the winding rule
     */
    public Path(int rule) {
        this(rule, INIT_SIZE);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new {@code Path} object from the given
     * specified initial values.
     * This method is only intended for internal use and should
     * not be made public if the other constructors for this class
     * are ever exposed.
     *
     * @param rule the winding rule
     * @param initialTypes the size to make the initial array to
     *                     store the path segment types
     */
    public Path(int rule, int initialTypes) {
        setWindingRule(rule);
        this.pointTypes = new byte[initialTypes];
        intCoords = new int[initialTypes * 2];
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new int precision {@code Path} object
     * from an arbitrary {@link IShape} object.
     * All of the initial geometry and the winding rule for this path are
     * taken from the specified {@code IShape} object.
     *
     * @param s the specified {@code IShape} object
     */
    public Path(IShape s) {
        this(s, null);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a new object of the same class as this object.
     *
     * @return     a clone of this instance.
     */
    public final Object clone() {
        return new Path(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Closes the current subpath by drawing a straight line back to
     * the coordinates of the last {@code moveTo}.  If the path is already
     * closed then this method has no effect.
     */
    public final synchronized void closePath() {
        if (numTypes == 0 || pointTypes[numTypes - 1] != SEG_CLOSE) {
            needRoom(true, 0);
            pointTypes[numTypes++] = SEG_CLOSE;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Appends the geometry of the specified {@code IShape} object to the
     * path, possibly connecting the new geometry to the existing path
     * segments with a line segment.
     * If the {@code connect} parameter is {@code true} and the
     * path is not empty then any initial {@code moveTo} in the
     * geometry of the appended {@code IShape}
     * is turned into a {@code lineTo} segment.
     * If the destination coordinates of such a connecting {@code lineTo}
     * segment match the ending coordinates of a currently open
     * subpath then the segment is omitted as superfluous.
     * The winding rule of the specified {@code IShape} is ignored
     * and the appended geometry is governed by the winding
     * rule specified for this path.
     *
     * @param s the {@code IShape} whose geometry is appended
     *          to this path
     * @param connect a boolean to control whether or not to turn an initial
     *                {@code moveTo} segment into a {@code lineTo} segment
     *                to connect the new geometry to the existing path
     */
    public final void append(IShape s, boolean connect) {
        append(s.getPathIterator(null), connect);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the fill style winding rule.
     *
     * @return an integer representing the current winding rule.
     */
    public final synchronized int getWindingRule() {
        return windingRule;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the winding rule for this path to the specified value.
     *
     * @param rule an integer representing the specified
     *             winding rule
     * @exception IllegalArgumentException if
     *		{@code rule} is not either
     *		{@link #WIND_EVEN_ODD} or
     *		{@link #WIND_NON_ZERO}
     */
    public final void setWindingRule(int rule) {
        if (rule != WIND_EVEN_ODD && rule != WIND_NON_ZERO) {
            throw new IllegalArgumentException("winding rule must be " +
                    "WIND_EVEN_ODD or " +
                    "WIND_NON_ZERO");
        }
        windingRule = rule;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the coordinates most recently added to the end of the path
     * as a {@link Point} object.
     *
     * @return a {@code Point} object containing the ending coordinates of
     *         the path or {@code null} if there are no points in the path.
     */
    public final synchronized Point getCurrentPoint() {
        int index = numCoords;
        if (numTypes < 1 || index < 1) {
            return null;
        }
        if (pointTypes[numTypes - 1] == SEG_CLOSE) {
            loop:
            for (int i = numTypes - 2; i > 0; i--) {
                switch (pointTypes[i]) {
                    case SEG_MOVETO:
                        break loop;
                    case SEG_LINETO:
                        index -= 2;
                        break;
                    case SEG_QUADTO:
                        index -= 4;
                        break;
                    case SEG_CUBICTO:
                        index -= 6;
                        break;
                    case SEG_CLOSE:
                        break;
                }
            }
        }
        return getPoint(index - 2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Resets the path to empty.  The append position is set back to the
     * beginning of the path and all coordinates and point types are
     * forgotten.
     */
    public final synchronized void reset() {
        numTypes = numCoords = 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a new {@code IShape} representing a transformed version
     * of this {@code Path}.
     * Note that the exact type and coordinate precision of the return
     * value is not specified for this method.
     * The method will return a IShape that contains no less precision
     * for the transformed geometry than this {@code Path} currently
     * maintains, but it may contain no more precision either.
     * If the tradeoff of precision vs. 
     *
     * @param at the {@code AffineTransform} used to transform a
     *           new {@code IShape}.
     * @return a new {@code IShape}, transformed with the specified
     *         {@code AffineTransform}.
     */
    public final synchronized IShape createTransformedShape(AffineTransform at) {
        Path p2d = (Path) clone();
        if (at != null) {
            p2d.transform(at);
        }
        return p2d;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified coordinates are inside the closed
     * boundary of the specified {@link IPathIterator}.
     * <p>
     * This method provides a basic facility for implementors of
     * the {@link IShape} interface to implement support for the
     * {@link IShape#contains(int, int)} method.
     *
     * @param pi the specified {@code IPathIterator}
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     * @return {@code true} if the specified coordinates are inside the
     *         specified {@code IPathIterator}; {@code false} otherwise
     */
    public static boolean contains(IPathIterator pi, int x, int y) {
        if (x * 0 + y * 0 == 0) {
            /* N * 0 is 0 only if N is finite.
             * Here we know that both x and y are finite.
             */
            int mask = (pi.getWindingRule() == WIND_NON_ZERO ? -1 : 1);
            int cross = Curve.pointCrossingsForPath(pi, x, y);
            return ((cross & mask) != 0);
        } else {
            /* Either x or y was infinite or NaN.
             * A NaN always produces a negative response to any test
             * and Infinity values cannot be "inside" any path so
             * they should return false as well.
             */
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified {@link Point} is inside the closed
     * boundary of the specified {@link IPathIterator}.
     * <p>
     * This method provides a basic facility for implementors of
     * the {@link IShape} interface to implement support for the
     * {@link IShape#contains(Point)} method.
     *
     * @param pi the specified {@code IPathIterator}
     * @param p the specified {@code Point}
     * @return {@code true} if the specified coordinates are inside the
     *         specified {@code IPathIterator}; {@code false} otherwise
     */
    public static boolean contains(IPathIterator pi, Point p) {
        return contains(pi, p.x, p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public final boolean contains(int x, int y) {
        if (x * 0 + y * 0 == 0) {
            /* N * 0 is 0 only if N is finite.
             * Here we know that both x and y are finite.
             */
            if (numTypes < 2) {
                return false;
            }
            int mask = (windingRule == WIND_NON_ZERO ? -1 : 1);
            return ((pointCrossings(x, y) & mask) != 0);
        } else {
            /* Either x or y was infinite or NaN.
             * A NaN always produces a negative response to any test
             * and Infinity values cannot be "inside" any path so
             * they should return false as well.
             */
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public final boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified rectangular area is entirely inside the
     * closed boundary of the specified {@link IPathIterator}.
     * <p>
     * This method provides a basic facility for implementors of
     * the {@link IShape} interface to implement support for the
     * {@link IShape#contains(int, int, int, int)} method.
     * <p>
     * This method object may conservatively return false in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such segments could lie entirely within the interior of the
     * path if they are part of a path with a {@link #WIND_NON_ZERO}
     * winding rule or if the segments are retraced in the reverse
     * direction such that the two sets of segments cancel each
     * other out without any exterior area falling between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     *
     * @param pi the specified {@code IPathIterator}
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     * @param w the width of the specified rectangular area
     * @param h the height of the specified rectangular area
     * @return {@code true} if the specified {@code IPathIterator} contains
     *         the specified rectangluar area; {@code false} otherwise.
     */
    public static boolean contains(IPathIterator pi,
            int x, int y, int w, int h) {
        if (java.lang.Double.isNaN(x + w) || java.lang.Double.isNaN(y + h)) {
            /* [xy]+[wh] is NaN if any of those values are NaN,
             * or if adding the two together would produce NaN
             * by virtue of adding opposing Infinte values.
             * Since we need to add them below, their sum must
             * not be NaN.
             * We return false because NaN always produces a
             * negative response to tests
             */
            return false;
        }
        if (w <= 0 || h <= 0) {
            return false;
        }
        int mask = (pi.getWindingRule() == WIND_NON_ZERO ? -1 : 2);
        int crossings = Curve.rectCrossingsForPath(pi, x, y, x + w, y + h);
        return (crossings != Curve.RECT_INTERSECTS &&
                (crossings & mask) != 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified {@link Rectangle} is entirely inside the
     * closed boundary of the specified {@link IPathIterator}.
     * <p>
     * This method provides a basic facility for implementors of
     * the {@link IShape} interface to implement support for the
     * {@link IShape#contains(Rectangle)} method.
     * <p>
     * This method object may conservatively return false in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such segments could lie entirely within the interior of the
     * path if they are part of a path with a {@link #WIND_NON_ZERO}
     * winding rule or if the segments are retraced in the reverse
     * direction such that the two sets of segments cancel each
     * other out without any exterior area falling between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     *
     * @param pi the specified {@code IPathIterator}
     * @param r a specified {@code Rectangle}
     * @return {@code true} if the specified {@code IPathIterator} contains
     *         the specified {@code Rectangle}; {@code false} otherwise.
     */
    public static boolean contains(IPathIterator pi, Rectangle r) {
        return contains(pi, r.x, r.y, r.width, r.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * <p>
     * This method object may conservatively return false in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such segments could lie entirely within the interior of the
     * path if they are part of a path with a {@link #WIND_NON_ZERO}
     * winding rule or if the segments are retraced in the reverse
     * direction such that the two sets of segments cancel each
     * other out without any exterior area falling between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     */
    public final boolean contains(int x, int y, int w, int h) {
        if (java.lang.Double.isNaN(x + w) || java.lang.Double.isNaN(y + h)) {
            /* [xy]+[wh] is NaN if any of those values are NaN,
             * or if adding the two together would produce NaN
             * by virtue of adding opposing Infinte values.
             * Since we need to add them below, their sum must
             * not be NaN.
             * We return false because NaN always produces a
             * negative response to tests
             */
            return false;
        }
        if (w <= 0 || h <= 0) {
            return false;
        }
        int mask = (windingRule == WIND_NON_ZERO ? -1 : 2);
        int crossings = rectCrossings(x, y, x + w, y + h);
        return (crossings != Curve.RECT_INTERSECTS &&
                (crossings & mask) != 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * <p>
     * This method object may conservatively return false in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such segments could lie entirely within the interior of the
     * path if they are part of a path with a {@link #WIND_NON_ZERO}
     * winding rule or if the segments are retraced in the reverse
     * direction such that the two sets of segments cancel each
     * other out without any exterior area falling between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     */
    public final boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the interior of the specified {@link IPathIterator}
     * intersects the interior of a specified set of rectangular
     * coordinates.
     * <p>
     * This method provides a basic facility for implementors of
     * the {@link IShape} interface to implement support for the
     * {@link IShape#intersects(int, int, int, int)} method.
     * <p>
     * This method object may conservatively return true in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such a case may occur if some set of segments of the
     * path are retraced in the reverse direction such that the
     * two sets of segments cancel each other out without any
     * interior area between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     *
     * @param pi the specified {@code IPathIterator}
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     * @param w the width of the specified rectangular coordinates
     * @param h the height of the specified rectangular coordinates
     * @return {@code true} if the specified {@code IPathIterator} and
     *         the interior of the specified set of rectangular
     *         coordinates intersect each other; {@code false} otherwise.
     */
    public static boolean intersects(IPathIterator pi,
            int x, int y, int w, int h) {
        if (java.lang.Double.isNaN(x + w) || java.lang.Double.isNaN(y + h)) {
            /* [xy]+[wh] is NaN if any of those values are NaN,
             * or if adding the two together would produce NaN
             * by virtue of adding opposing Infinte values.
             * Since we need to add them below, their sum must
             * not be NaN.
             * We return false because NaN always produces a
             * negative response to tests
             */
            return false;
        }
        if (w <= 0 || h <= 0) {
            return false;
        }
        int mask = (pi.getWindingRule() == WIND_NON_ZERO ? -1 : 2);
        int crossings = Curve.rectCrossingsForPath(pi, x, y, x + w, y + h);
        return (crossings == Curve.RECT_INTERSECTS ||
                (crossings & mask) != 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the interior of the specified {@link IPathIterator}
     * intersects the interior of a specified {@link Rectangle}.
     * <p>
     * This method provides a basic facility for implementors of
     * the {@link IShape} interface to implement support for the
     * {@link IShape#intersects(Rectangle)} method.
     * <p>
     * This method object may conservatively return true in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such a case may occur if some set of segments of the
     * path are retraced in the reverse direction such that the
     * two sets of segments cancel each other out without any
     * interior area between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     *
     * @param pi the specified {@code IPathIterator}
     * @param r the specified {@code Rectangle}
     * @return {@code true} if the specified {@code IPathIterator} and
     *         the interior of the specified {@code Rectangle}
     *         intersect each other; {@code false} otherwise.
     */
    public static boolean intersects(IPathIterator pi, Rectangle r) {
        return intersects(pi, r.x, r.y, r.width, r.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * <p>
     * This method object may conservatively return true in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such a case may occur if some set of segments of the
     * path are retraced in the reverse direction such that the
     * two sets of segments cancel each other out without any
     * interior area between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     */
    public final boolean intersects(int x, int y, int w, int h) {
        if (java.lang.Double.isNaN(x + w) || java.lang.Double.isNaN(y + h)) {
            /* [xy]+[wh] is NaN if any of those values are NaN,
             * or if adding the two together would produce NaN
             * by virtue of adding opposing Infinte values.
             * Since we need to add them below, their sum must
             * not be NaN.
             * We return false because NaN always produces a
             * negative response to tests
             */
            return false;
        }
        if (w <= 0 || h <= 0) {
            return false;
        }
        int mask = (windingRule == WIND_NON_ZERO ? -1 : 2);
        int crossings = rectCrossings(x, y, x + w, y + h);
        return (crossings == Curve.RECT_INTERSECTS ||
                (crossings & mask) != 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * <p>
     * This method object may conservatively return true in
     * cases where the specified rectangular area intersects a
     * segment of the path, but that segment does not represent a
     * boundary between the interior and exterior of the path.
     * Such a case may occur if some set of segments of the
     * path are retraced in the reverse direction such that the
     * two sets of segments cancel each other out without any
     * interior area between them.
     * To determine whether segments represent true boundaries of
     * the interior of the path would require extensive calculations
     * involving all of the segments of the path and the winding
     * rule and are thus beyond the scope of this implementation.
     */
    public final boolean intersects(Rectangle r) {
        return intersects(r.x, r.y, r.width, r.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * <p>
     * The iterator for this class is not multi-threaded safe,
     * which means that this {@code Path} class does not
     * guarantee that modifications to the geometry of this
     * {@code Path} object do not affect any iterations of
     * that geometry that are already in process.
     */
    public IPathIterator getPathIterator(AffineTransform at,
            int flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    /*
     * Support fields and methods for serializing the subclasses.
     */
//    private static final byte SERIAL_STORAGE_FLT_ARRAY = 0x30;
//    private static final byte SERIAL_STORAGE_DBL_ARRAY = 0x31;
//
//    private static final byte SERIAL_SEG_FLT_MOVETO    = 0x40;
//    private static final byte SERIAL_SEG_FLT_LINETO    = 0x41;
//    private static final byte SERIAL_SEG_FLT_QUADTO    = 0x42;
//    private static final byte SERIAL_SEG_FLT_CUBICTO   = 0x43;
//
//    private static final byte SERIAL_SEG_DBL_MOVETO    = 0x50;
//    private static final byte SERIAL_SEG_DBL_LINETO    = 0x51;
//    private static final byte SERIAL_SEG_DBL_QUADTO    = 0x52;
//    private static final byte SERIAL_SEG_DBL_CUBICTO   = 0x53;
//
//    private static final byte SERIAL_SEG_CLOSE         = 0x60;
//    private static final byte SERIAL_PATH_END          = 0x61;
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    static abstract class Iterator implements IPathIterator {

        int typeIdx;
        int pointIdx;
        Path path;
        static final int curvecoords[] = {2, 2, 4, 6, 0};

        Iterator(Path path) {
            this.path = path;
        }

        public int getWindingRule() {
            return path.getWindingRule();
        }

        public boolean isDone() {
            return (typeIdx >= path.numTypes);
        }

        public void next() {
            int type = path.pointTypes[typeIdx++];
            pointIdx += curvecoords[type];
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a point to the path by moving to the specified
     * coordinates specified in double precision.
     *
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public final synchronized void moveTo(int x, int y) {
        if (numTypes > 0 && pointTypes[numTypes - 1] == SEG_MOVETO) {
            intCoords[numCoords - 2] = x;
            intCoords[numCoords - 1] = y;
        } else {
            needRoom(false, 2);
            pointTypes[numTypes++] = SEG_MOVETO;
            intCoords[numCoords++] = x;
            intCoords[numCoords++] = y;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a point to the path by drawing a straight line from the
     * current coordinates to the new specified coordinates
     * specified in double precision.
     *
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public final synchronized void lineTo(int x, int y) {
        needRoom(true, 2);
        pointTypes[numTypes++] = SEG_LINETO;
        intCoords[numCoords++] = x;
        intCoords[numCoords++] = y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a curved segment, defined by two new points, to the path by
     * drawing a Quadratic curve that intersects both the current
     * coordinates and the specified coordinates {@code (x2,y2)},
     * using the specified point {@code (x1,y1)} as a quadratic
     * parametric control point.
     * All coordinates are specified in double precision.
     *
     * @param x1 the X coordinate of the quadratic control point
     * @param y1 the Y coordinate of the quadratic control point
     * @param x2 the X coordinate of the final end point
     * @param y2 the Y coordinate of the final end point
     */
    public final synchronized void quadTo(int x1, int y1,
            int x2, int y2) {
        needRoom(true, 4);
        pointTypes[numTypes++] = SEG_QUADTO;
        intCoords[numCoords++] = x1;
        intCoords[numCoords++] = y1;
        intCoords[numCoords++] = x2;
        intCoords[numCoords++] = y2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a curved segment, defined by three new points, to the path by
     * drawing a B&eacute;zier curve that intersects both the current
     * coordinates and the specified coordinates {@code (x3,y3)},
     * using the specified points {@code (x1,y1)} and {@code (x2,y2)} as
     * B&eacute;zier control points.
     * All coordinates are specified in double precision.
     *
     * @param x1 the X coordinate of the first B&eacute;zier control point
     * @param y1 the Y coordinate of the first B&eacute;zier control point
     * @param x2 the X coordinate of the second B&eacute;zier control point
     * @param y2 the Y coordinate of the second B&eacute;zier control point
     * @param x3 the X coordinate of the final end point
     * @param y3 the Y coordinate of the final end point
     */
    public final synchronized void curveTo(int x1, int y1,
            int x2, int y2,
            int x3, int y3) {
        needRoom(true, 6);
        pointTypes[numTypes++] = SEG_CUBICTO;
        intCoords[numCoords++] = x1;
        intCoords[numCoords++] = y1;
        intCoords[numCoords++] = x2;
        intCoords[numCoords++] = y2;
        intCoords[numCoords++] = x3;
        intCoords[numCoords++] = y3;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Appends the geometry of the specified
     * {@link IPathIterator} object
     * to the path, possibly connecting the new geometry to the existing
     * path segments with a line segment.
     * If the {@code connect} parameter is {@code true} and the
     * path is not empty then any initial {@code moveTo} in the
     * geometry of the appended {@code IShape} is turned into a
     * {@code lineTo} segment.
     * If the destination coordinates of such a connecting {@code lineTo}
     * segment match the ending coordinates of a currently open
     * subpath then the segment is omitted as superfluous.
     * The winding rule of the specified {@code IShape} is ignored
     * and the appended geometry is governed by the winding
     * rule specified for this path.
     *
     * @param pi the {@code IPathIterator} whose geometry is appended to
     *           this path
     * @param connect a boolean to control whether or not to turn an initial
     *                {@code moveTo} segment into a {@code lineTo} segment
     *                to connect the new geometry to the existing path
     */
    public final void append(IPathIterator pi, boolean connect) {
        int coords[] = new int[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case SEG_MOVETO:
                    if (!connect || numTypes < 1 || numCoords < 1) {
                        moveTo(coords[0], coords[1]);
                        break;
                    }
                    if (pointTypes[numTypes - 1] != SEG_CLOSE &&
                            intCoords[numCoords - 2] == coords[0] &&
                            intCoords[numCoords - 1] == coords[1]) {
                        // Collapse out initial moveto/lineto
                        break;
                    }
                // NO BREAK;
                case SEG_LINETO:
                    lineTo(coords[0], coords[1]);
                    break;
                case SEG_QUADTO:
                    quadTo(coords[0], coords[1],
                            coords[2], coords[3]);
                    break;
                case SEG_CUBICTO:
                    curveTo(coords[0], coords[1],
                            coords[2], coords[3],
                            coords[4], coords[5]);
                    break;
                case SEG_CLOSE:
                    closePath();
                    break;
            }
            pi.next();
            connect = false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Transforms the geometry of this path using the specified
     * {@link AffineTransform}.
     * The geometry is transformed in place, which permanently changes the
     * boundary defined by this object.
     *
     * @param at the {@code AffineTransform} used to transform the area
     */
    public final void transform(AffineTransform at) {
        at.transform(intCoords, 0, intCoords, 0, numCoords / 2);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public final synchronized Rectangle getBounds() {
        int x1, y1, x2, y2;
        int i = numCoords;
        if (i > 0) {
            y1 = y2 = intCoords[--i];
            x1 = x2 = intCoords[--i];
            while (i > 0) {
                int y = intCoords[--i];
                int x = intCoords[--i];
                if (x < x1) {
                    x1 = x;
                }
                if (y < y1) {
                    y1 = y;
                }
                if (x > x2) {
                    x2 = x;
                }
                if (y > y2) {
                    y2 = y;
                }
            }
        } else {
            x1 = y1 = x2 = y2 = 0;
        }
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * <p>
     * The iterator for this class is not multi-threaded safe,
     * which means that the {@code Path} class does not
     * guarantee that modifications to the geometry of this
     * {@code Path} object do not affect any iterations of
     * that geometry that are already in process.
     *
     * @param at an {@code AffineTransform}
     * @return a new {@code IPathIterator} that iterates along the boundary
     *         of this {@code IShape} and provides access to the geometry
     *         of this {@code IShape}'s outline
     */
    public IPathIterator getPathIterator(AffineTransform at) {
        if (at == null) {
            return new CopyIterator(this);
        } else {
            return new TxIterator(this, at);
        }
    }



    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * To a SVG string.
     * @param shape the shape object.
     * @return a SVG string
     */
    public static String toSVG(IShape shape) {
        IPathIterator pathIterator = shape.getPathIterator(null);
        StringBuffer svgString = new StringBuffer("<path d='");
        int[] coords = new int[6];
        int type;
        while (!pathIterator.isDone()) {
            type = pathIterator.currentSegment(coords);
            switch (type) {
                case IPathIterator.SEG_CLOSE:
                    svgString.append("Z ");
                    break;
                case IPathIterator.SEG_CUBICTO:
                    svgString.append("C ");
                    svgString.append(coords[0] + " ");
                    svgString.append(coords[1] + " ");
                    svgString.append(coords[2] + " ");
                    svgString.append(coords[3] + " ");
                    svgString.append(coords[4] + " ");
                    svgString.append(coords[5]);
                    break;
                case IPathIterator.SEG_LINETO:
                    svgString.append("L ");
                    svgString.append(coords[0] + " ");
                    svgString.append(coords[1]);
                    break;
                case IPathIterator.SEG_MOVETO:
                    svgString.append("M ");
                    svgString.append(coords[0] + " ");
                    svgString.append(coords[1]);
                    break;
                case IPathIterator.SEG_QUADTO:
                    svgString.append("Q ");
                    svgString.append(coords[0] + " ");
                    svgString.append(coords[1] + " ");
                    svgString.append(coords[2] + " ");
                    svgString.append(coords[3]);
                    break;
            }

            pathIterator.next();

        }
        svgString.append("' />");
        return svgString.toString();
    }

    

    // For code simplicity, copy these constants to our namespace
    // and cast them to byte constants for easy storage.
    protected static final byte SEG_MOVETO = (byte) IPathIterator.SEG_MOVETO;
    protected static final byte SEG_LINETO = (byte) IPathIterator.SEG_LINETO;
    protected static final byte SEG_QUADTO = (byte) IPathIterator.SEG_QUADTO;
    protected static final byte SEG_CUBICTO = (byte) IPathIterator.SEG_CUBICTO;
    protected static final byte SEG_CLOSE = (byte) IPathIterator.SEG_CLOSE;
    protected transient byte[] pointTypes;
    protected transient int numTypes;
    protected transient int numCoords;
    protected transient int windingRule;
    protected static final int INIT_SIZE = 20;
    protected static final int EXPAND_MAX = 500;
    protected transient int intCoords[];
    private final static PathParser pathParser=new PathParser();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    private int pointCrossings(int px, int py) {
        int movx, movy, curx, cury, endx, endy;
        int coords[] = intCoords;
        curx = movx = coords[0];
        cury = movy = coords[1];
        int crossings = 0;
        int ci = 2;
        for (int i = 1; i < numTypes; i++) {
            switch (pointTypes[i]) {
                case IPathIterator.SEG_MOVETO:
                    if (cury != movy) {
                        crossings +=
                                Curve.pointCrossingsForLine(px, py,
                                curx, cury,
                                movx, movy);
                    }
                    movx = curx = coords[ci++];
                    movy = cury = coords[ci++];
                    break;
                case IPathIterator.SEG_LINETO:
                    crossings +=
                            Curve.pointCrossingsForLine(px, py,
                            curx, cury,
                            endx = coords[ci++],
                            endy = coords[ci++]);
                    curx = endx;
                    cury = endy;
                    break;
                case IPathIterator.SEG_QUADTO:
                    crossings +=
                            Curve.pointCrossingsForQuad(px, py,
                            curx, cury,
                            coords[ci++],
                            coords[ci++],
                            endx = coords[ci++],
                            endy = coords[ci++],
                            0);
                    curx = endx;
                    cury = endy;
                    break;
                case IPathIterator.SEG_CUBICTO:
                    crossings +=
                            Curve.pointCrossingsForCubic(px, py,
                            curx, cury,
                            coords[ci++],
                            coords[ci++],
                            coords[ci++],
                            coords[ci++],
                            endx = coords[ci++],
                            endy = coords[ci++],
                            0);
                    curx = endx;
                    cury = endy;
                    break;
                case IPathIterator.SEG_CLOSE:
                    if (cury != movy) {
                        crossings +=
                                Curve.pointCrossingsForLine(px, py,
                                curx, cury,
                                movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    break;
            }
        }
        if (cury != movy) {
            crossings +=
                    Curve.pointCrossingsForLine(px, py,
                    curx, cury,
                    movx, movy);
        }
        return crossings;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    private int[] cloneCoords(AffineTransform at) {
        int ret[];
        if (at == null) {
            ret = Arrays.copyOf(this.intCoords,
                    this.intCoords.length);
        } else {
            ret = new int[intCoords.length];
            at.transform(intCoords, 0, ret, 0, numCoords / 2);
        }
        return ret;
    }
//    ////////////////////////////////////////////////////////////////////////////
//    //--------------------------------- REVISIONS ------------------------------
//    // Date       Name                 Tracking #         Description
//    // ---------  -------------------  -------------      ----------------------
//    // 06NOV2008  James Shen                 	          Code review
//    ////////////////////////////////////////////////////////////////////////////
//    private void append(int x, int y) {
//        intCoords[numCoords++] = x;
//        intCoords[numCoords++] = y;
//    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    private Point getPoint(int coordindex) {
        Point pt = new Point();
        pt.x = intCoords[coordindex];
        pt.y = intCoords[coordindex + 1];
        return pt;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    private void needRoom(boolean needMove, int newCoords) {
        if (needMove && numTypes == 0) {
            throw new IllegalPathStateException("missing initial moveto " +
                    "in path definition");
        }
        int size = pointTypes.length;
        if (numTypes >= size) {
            int grow = size;
            if (grow > EXPAND_MAX) {
                grow = EXPAND_MAX;
            }
            pointTypes = Arrays.copyOf(pointTypes, size + grow);
        }
        size = intCoords.length;
        if (numCoords + newCoords > size) {
            int grow = size;
            if (grow > EXPAND_MAX * 2) {
                grow = EXPAND_MAX * 2;
            }
            if (grow < newCoords) {
                grow = newCoords;
            }
            intCoords = Arrays.copyOf(intCoords, size + grow);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    private int rectCrossings(int rxmin, int rymin,
            int rxmax, int rymax) {
        int coords[] = intCoords;
        int curx, cury, movx, movy, endx, endy;
        curx = movx = coords[0];
        cury = movy = coords[1];
        int crossings = 0;
        int ci = 2;
        for (int i = 1;
                crossings != Curve.RECT_INTERSECTS && i < numTypes;
                i++) {
            switch (pointTypes[i]) {
                case IPathIterator.SEG_MOVETO:
                    if (curx != movx || cury != movy) {
                        crossings =
                                Curve.rectCrossingsForLine(crossings,
                                rxmin, rymin,
                                rxmax, rymax,
                                curx, cury,
                                movx, movy);
                    }
                    // Count should always be a multiple of 2 here.
                    // assert((crossings & 1) != 0);
                    movx = curx = coords[ci++];
                    movy = cury = coords[ci++];
                    break;
                case IPathIterator.SEG_LINETO:
                    endx = coords[ci++];
                    endy = coords[ci++];
                    crossings =
                            Curve.rectCrossingsForLine(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            endx, endy);
                    curx = endx;
                    cury = endy;
                    break;
                case IPathIterator.SEG_QUADTO:
                    crossings =
                            Curve.rectCrossingsForQuad(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            coords[ci++],
                            coords[ci++],
                            endx = coords[ci++],
                            endy = coords[ci++],
                            0);
                    curx = endx;
                    cury = endy;
                    break;
                case IPathIterator.SEG_CUBICTO:
                    crossings =
                            Curve.rectCrossingsForCubic(crossings,
                            rxmin, rymin,
                            rxmax, rymax,
                            curx, cury,
                            coords[ci++],
                            coords[ci++],
                            coords[ci++],
                            coords[ci++],
                            endx = coords[ci++],
                            endy = coords[ci++],
                            0);
                    curx = endx;
                    cury = endy;
                    break;
                case IPathIterator.SEG_CLOSE:
                    if (curx != movx || cury != movy) {
                        crossings =
                                Curve.rectCrossingsForLine(crossings,
                                rxmin, rymin,
                                rxmax, rymax,
                                curx, cury,
                                movx, movy);
                    }
                    curx = movx;
                    cury = movy;
                    // Count should always be a multiple of 2 here.
                    // assert((crossings & 1) != 0);
                    break;
            }
        }
        if (crossings != Curve.RECT_INTERSECTS &&
                (curx != movx || cury != movy)) {
            crossings =
                    Curve.rectCrossingsForLine(crossings,
                    rxmin, rymin,
                    rxmax, rymax,
                    curx, cury,
                    movx, movy);
        }
        // Count should always be a multiple of 2 here.
        // assert((crossings & 1) != 0);
        return crossings;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    static class CopyIterator extends Path.Iterator {

        int intCoords[];

        CopyIterator(Path p2dd) {
            super(p2dd);
            this.intCoords = p2dd.intCoords;
        }

        public int currentSegment(int[] coords) {
            int type = path.pointTypes[typeIdx];
            int numCoords = curvecoords[type];
            if (numCoords > 0) {
                for (int i = 0; i < numCoords; i++) {
                    coords[i] = intCoords[pointIdx + i];
                }
            }
            return type;
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    static class TxIterator extends Path.Iterator {

        int intCoords[];
        AffineTransform affine;

        TxIterator(Path p2dd, AffineTransform at) {
            super(p2dd);
            this.intCoords = p2dd.intCoords;
            this.affine = at;
        }

        public int currentSegment(int[] coords) {
            int type = path.pointTypes[typeIdx];
            int numCoords = curvecoords[type];
            if (numCoords > 0) {
                affine.transform(intCoords, pointIdx,
                        coords, 0, numCoords / 2);
            }
            return type;
        }
    }
}