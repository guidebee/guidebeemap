//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 06MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Enumeration;
import java.util.NoSuchElementException;

import com.mapdigit.collections.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 06MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * An <code>Area</code> object stores and manipulates a
 * resolution-independent description of an enclosed area of
 * 2-dimensional space.
 * <code>Area</code> objects can be transformed and can perform
 * various Constructive Area Geometry (CAG) operations when combined
 * with other <code>Area</code> objects.
 * The CAG operations include area
 * {@link #add addition}, {@link #subtract subtraction},
 * {@link #intersect intersection}, and {@link #exclusiveOr exclusive or}.
 * See the linked method documentation for examples of the various
 * operations.
 * <p>
 * The <code>Area</code> class implements the <code>IShape</code>
 * interface and provides full support for all of its hit-testing
 * and path iteration facilities, but an <code>Area</code> is more
 * specific than a generalized path in a number of ways:
 * <ul>
 * <li>Only closed paths and sub-paths are stored.
 *     <code>Area</code> objects constructed from unclosed paths
 *     are implicitly closed during construction as if those paths
 *     had been filled by the <code>Graphics2D.fill</code> method.
 * <li>The interiors of the individual stored sub-paths are all
 *     non-empty and non-overlapping.  Paths are decomposed during
 *     construction into separate component non-overlapping parts,
 *     empty pieces of the path are discarded, and then these
 *     non-empty and non-overlapping properties are maintained
 *     through all subsequent CAG operations.  Outlines of different
 *     component sub-paths may touch each other, as long as they
 *     do not cross so that their enclosed areas overlap.
 * <li>The geometry of the path describing the outline of the
 *     <code>Area</code> resembles the path from which it was
 *     constructed only in that it describes the same enclosed
 *     2-dimensional area, but may use entirely different types
 *     and ordering of the path segments to do so.
 * </ul>
 * Interesting issues which are not always obvious when using
 * the <code>Area</code> include:
 * <ul>
 * <li>Creating an <code>Area</code> from an unclosed (open)
 *     <code>IShape</code> results in a closed outline in the
 *     <code>Area</code> object.
 * <li>Creating an <code>Area</code> from a <code>IShape</code>
 *     which encloses no area (even when "closed") produces an
 *     empty <code>Area</code>.  A common example of this issue
 *     is that producing an <code>Area</code> from a line will
 *     be empty since the line encloses no area.  An empty
 *     <code>Area</code> will iterate no geometry in its
 *     <code>IPathIterator</code> objects.
 * <li>A self-intersecting <code>IShape</code> may be split into
 *     two (or more) sub-paths each enclosing one of the
 *     non-intersecting portions of the original path.
 * <li>An <code>Area</code> may take more path segments to
 *     describe the same geometry even when the original
 *     outline is simple and obvious.  The analysis that the
 *     <code>Area</code> class must perform on the path may
 *     not reflect the same concepts of "simple and obvious"
 *     as a human being perceives.
 * </ul>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 06/05/10
 * @author      Guidebee Pty Ltd.
 */
public class Area implements IShape {
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor which creates an empty area.
     */
    public Area() {
        curves = EmptyCurves;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * The <code>Area</code> class creates an area geometry from the
     * specified {@link IShape} object.  The geometry is explicitly
     * closed, if the <code>IShape</code> is not already closed.  The
     * fill rule (even-odd or winding) specified by the geometry of the
     * <code>IShape</code> is used to determine the resulting enclosed area.
     * @param s  the <code>IShape</code> from which the area is constructed
     * @throws NullPointerException if <code>s</code> is null
     */
    public Area(IShape s) {
        if (s instanceof Area) {
            curves = ((Area) s).curves;
        } else {
            curves = pathToCurves(s.getPathIterator(null));
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds the shape of the specified <code>Area</code> to the
     * shape of this <code>Area</code>.
     * The resulting shape of this <code>Area</code> will include
     * the union of both shapes, or all areas that were contained
     * in either this or the specified <code>Area</code>.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.add(a2);
     *
     *        a1(before)     +         a2         =     a1(after)
     *
     *     ################     ################     ################
     *     ##############         ##############     ################
     *     ############             ############     ################
     *     ##########                 ##########     ################
     *     ########                     ########     ################
     *     ######                         ######     ######    ######
     *     ####                             ####     ####        ####
     *     ##                                 ##     ##            ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be added to the
     *          current shape
     * @throws NullPointerException if <code>rhs</code> is null
     */
    public void add(Area rhs) {
        curves = new AreaOp.AddOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Subtracts the shape of the specified <code>Area</code> from the
     * shape of this <code>Area</code>.
     * The resulting shape of this <code>Area</code> will include
     * areas that were contained only in this <code>Area</code>
     * and not in the specified <code>Area</code>.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.subtract(a2);
     *
     *        a1(before)     -         a2         =     a1(after)
     *
     *     ################     ################
     *     ##############         ##############     ##
     *     ############             ############     ####
     *     ##########                 ##########     ######
     *     ########                     ########     ########
     *     ######                         ######     ######
     *     ####                             ####     ####
     *     ##                                 ##     ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be subtracted from the
     *		current shape
     * @throws NullPointerException if <code>rhs</code> is null
     */
    public void subtract(Area rhs) {
        curves = new AreaOp.SubOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the shape of this <code>Area</code> to the intersection of
     * its current shape and the shape of the specified <code>Area</code>.
     * The resulting shape of this <code>Area</code> will include
     * only areas that were contained in both this <code>Area</code>
     * and also in the specified <code>Area</code>.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.intersect(a2);
     *
     *      a1(before)   intersect     a2         =     a1(after)
     *
     *     ################     ################     ################
     *     ##############         ##############       ############
     *     ############             ############         ########
     *     ##########                 ##########           ####
     *     ########                     ########
     *     ######                         ######
     *     ####                             ####
     *     ##                                 ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be intersected with this
     *		<code>Area</code>
     * @throws NullPointerException if <code>rhs</code> is null
     */
    public void intersect(Area rhs) {
        curves = new AreaOp.IntOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the shape of this <code>Area</code> to be the combined area
     * of its current shape and the shape of the specified <code>Area</code>,
     * minus their intersection.
     * The resulting shape of this <code>Area</code> will include
     * only areas that were contained in either this <code>Area</code>
     * or in the specified <code>Area</code>, but not in both.
     * <pre>
     *     // Example:
     *     Area a1 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 0,8]);
     *     Area a2 = new Area([triangle 0,0 =&gt; 8,0 =&gt; 8,8]);
     *     a1.exclusiveOr(a2);
     *
     *        a1(before)    xor        a2         =     a1(after)
     *
     *     ################     ################
     *     ##############         ##############     ##            ##
     *     ############             ############     ####        ####
     *     ##########                 ##########     ######    ######
     *     ########                     ########     ################
     *     ######                         ######     ######    ######
     *     ####                             ####     ####        ####
     *     ##                                 ##     ##            ##
     * </pre>
     * @param   rhs  the <code>Area</code> to be exclusive ORed with this
     *		<code>Area</code>.
     * @throws NullPointerException if <code>rhs</code> is null
     */
    public void exclusiveOr(Area rhs) {
        curves = new AreaOp.XorOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Removes all of the geometry from this <code>Area</code> and
     * restores it to an empty area.
     */
    public void reset() {
        curves = new Vector();
        invalidateBounds();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests whether this <code>Area</code> object encloses any area.
     * @return    <code>true</code> if this <code>Area</code> object
     * represents an empty area; <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return (curves.size() == 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests whether this <code>Area</code> consists entirely of
     * straight edged polygonal geometry.
     * @return    <code>true</code> if the geometry of this
     * <code>Area</code> consists entirely of line segments;
     * <code>false</code> otherwise.
     */
    public boolean isPolygonal() {
        Enumeration enu = curves.elements();
        while (enu.hasMoreElements()) {
            if (((Curve) enu.nextElement()).getOrder() > 1) {
                return false;
            }
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests whether this <code>Area</code> is rectangular in shape.
     * @return    <code>true</code> if the geometry of this
     * <code>Area</code> is rectangular in shape; <code>false</code>
     * otherwise.
     */
    public boolean isRectangular() {
        int size = curves.size();
        if (size == 0) {
            return true;
        }
        if (size > 3) {
            return false;
        }
        Curve c1 = (Curve) curves.get(1);
        Curve c2 = (Curve) curves.get(2);
        if (c1.getOrder() != 1 || c2.getOrder() != 1) {
            return false;
        }
        if (c1.getXTop() != c1.getXBot() || c2.getXTop() != c2.getXBot()) {
            return false;
        }
        if (c1.getYTop() != c2.getYTop() || c1.getYBot() != c2.getYBot()) {
            // One might be able to prove that this is impossible...
            return false;
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests whether this <code>Area</code> is comprised of a single
     * closed subpath.  This method returns <code>true</code> if the
     * path contains 0 or 1 subpaths, or <code>false</code> if the path
     * contains more than 1 subpath.  The subpaths are counted by the
     * number of {@link IPathIterator#SEG_MOVETO SEG_MOVETO}  segments
     * that appear in the path.
     * @return    <code>true</code> if the <code>Area</code> is comprised
     * of a single basic geometry; <code>false</code> otherwise.
     */
    public boolean isSingular() {
        if (curves.size() < 3) {
            return true;
        }
        Enumeration enum_ = curves.elements();
        enum_.nextElement(); // First Order0 "moveto"
        while (enum_.hasMoreElements()) {
            if (((Curve) enum_.nextElement()).getOrder() == 0) {
                return false;
            }
        }
        return true;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a bounding {@link Rectangle} that completely encloses
     * this <code>Area</code>.
     * <p>
     * The Area class will attempt to return the tightest bounding
     * box possible for the IShape.  The bounding box will not be
     * padded to include the control points of curves in the outline
     * of the IShape, but should tightly fit the actual geometry of
     * the outline itself.  Since the returned object represents
     * the bounding box with integers, the bounding box can only be
     * as tight as the nearest integer coordinates that encompass
     * the geometry of the IShape.
     * @return    the bounding <code>Rectangle</code> for the
     * <code>Area</code>.
     */
    public Rectangle getBounds() {
        return getCachedBounds().getBounds();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests whether the geometries of the two <code>Area</code> objects
     * are equal.
     * This method will return false if the argument is null.
     * @param   other  the <code>Area</code> to be compared to this
     *		<code>Area</code>
     * @return  <code>true</code> if the two geometries are equal;
     *		<code>false</code> otherwise.
     */
    public boolean equals(Area other) {
        // REMIND: A *much* simpler operation should be possible...
        // Should be able to do a curve-wise comparison since all Areas
        // should evaluate their curves in the same top-down order.
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        Vector c = new AreaOp.XorOp().calculate(this.curves, other.curves);
        return c.isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Transforms the geometry of this <code>Area</code> using the specified
     * {@link AffineTransform}.  The geometry is transformed in place, which
     * permanently changes the enclosed area defined by this object.
     * @param t  the transformation used to transform the area
     * @throws NullPointerException if <code>t</code> is null
     */
    public void transform(AffineTransform t) {
        if (t == null) {
            throw new NullPointerException("transform must not be null");
        }
        // REMIND: A simpler operation can be performed for some types
        // of transform.
        curves = pathToCurves(getPathIterator(t));
        invalidateBounds();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a new <code>Area</code> object that contains the same
     * geometry as this <code>Area</code> transformed by the specified
     * <code>AffineTransform</code>.  This <code>Area</code> object
     * is unchanged.
     * @param t  the specified <code>AffineTransform</code> used to transform
     *           the new <code>Area</code>
     * @throws NullPointerException if <code>t</code> is null
     * @return   a new <code>Area</code> object representing the transformed
     *           geometry.
     */
    public Area createTransformedArea(AffineTransform t) {
        Area a = new Area(this);
        a.transform(t);
        return a;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean contains(int x, int y) {
        if (!getCachedBounds().contains(x, y)) {
            return false;
        }
        Enumeration enum_ = curves.elements();
        int crossings = 0;
        while (enum_.hasMoreElements()) {
            Curve c = (Curve) enum_.nextElement();
            crossings += c.crossingsFor(x, y);
        }
        return ((crossings & 1) == 1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean contains(Point p) {
        return contains(p.getX(), p.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean contains(int x, int y, int w, int h) {
        if (w < 0 || h < 0) {
            return false;
        }
        if (!getCachedBounds().contains(x, y, w, h)) {
            return false;
        }
        Crossings c = Crossings.findCrossings(curves, x, y, x + w, y + h);
        return (c != null && c.covers(y, y + h));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean contains(Rectangle r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean intersects(int x, int y, int w, int h) {
        if (w < 0 || h < 0) {
            return false;
        }
        if (!getCachedBounds().intersects(x, y, w, h)) {
            return false;
        }
        Crossings c = Crossings.findCrossings(curves, x, y, x + w, y + h);
        return (c == null || !c.isEmpty());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean intersects(Rectangle r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a {@link IPathIterator} for the outline of this
     * <code>Area</code> object.  This <code>Area</code> object is unchanged.
     * @param at an optional <code>AffineTransform</code> to be applied to
     * the coordinates as they are returned in the iteration, or
     * <code>null</code> if untransformed coordinates are desired
     * @return    the <code>IPathIterator</code> object that returns the
     *		geometry of the outline of this <code>Area</code>, one
     *		segment at a time.
     */
    public IPathIterator getPathIterator(AffineTransform at) {
        return new AreaIterator(curves, at);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a <code>IPathIterator</code> for the flattened outline of
     * this <code>Area</code> object.  Only uncurved path segments
     * represented by the SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point
     * types are returned by the iterator.  This <code>Area</code>
     * object is unchanged.
     * @param at an optional <code>AffineTransform</code> to be
     * applied to the coordinates as they are returned in the
     * iteration, or <code>null</code> if untransformed coordinates
     * are desired
     * @param flatness the maximum amount that the control points
     * for a given curve can vary from colinear before a subdivided
     * curve is replaced by a straight line connecting the end points
     * @return    the <code>IPathIterator</code> object that returns the
     * geometry of the outline of this <code>Area</code>, one segment
     * at a time.
     */
    public IPathIterator getPathIterator(AffineTransform at, int flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    private static Vector EmptyCurves = new Vector();
    private Vector curves;
    private Rectangle cachedBounds;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param pi
     * @return
     */
    private static Vector pathToCurves(IPathIterator pi) {
        Vector curves = new Vector();
        int windingRule = pi.getWindingRule();
        // coords array is big enough for holding:
        //     coordinates returned from currentSegment (6)
        //     OR
        //         two subdivided quadratic curves (2+4+4=10)
        //         AND
        //             0-1 horizontal splitting parameters
        //             OR
        //             2 parametric equation derivative coefficients
        //     OR
        //         three subdivided cubic curves (2+6+6+6=20)
        //         AND
        //             0-2 horizontal splitting parameters
        //             OR
        //             3 parametric equation derivative coefficients
        int coords[] = new int[23];
        double movx = 0, movy = 0;
        double curx = 0, cury = 0;
        double newx, newy;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case IPathIterator.SEG_MOVETO:
                    Curve.insertLine(curves, curx, cury, movx, movy);
                    curx = movx = coords[0];
                    cury = movy = coords[1];
                    Curve.insertMove(curves, movx, movy);
                    break;
                case IPathIterator.SEG_LINETO:
                    newx = coords[0];
                    newy = coords[1];
                    Curve.insertLine(curves, curx, cury, newx, newy);
                    curx = newx;
                    cury = newy;
                    break;
                case IPathIterator.SEG_QUADTO:
                     {
                        newx = coords[2];
                        newy = coords[3];
                        double[] dblCoords = new double[coords.length];
                        for (int i = 0; i < coords.length; i++) {
                            dblCoords[i] = coords[i];
                        }
                        Curve.insertQuad(curves, curx, cury, dblCoords);
                        curx = newx;
                        cury = newy;
                    }
                    break;
                case IPathIterator.SEG_CUBICTO:
                     {
                        newx = coords[4];
                        newy = coords[5];
                        double[] dblCoords = new double[coords.length];
                        for (int i = 0; i < coords.length; i++) {
                            dblCoords[i] = coords[i];
                        }
                        Curve.insertCubic(curves, curx, cury, dblCoords);
                        curx = newx;
                        cury = newy;
                    }
                    break;
                case IPathIterator.SEG_CLOSE:
                    Curve.insertLine(curves, curx, cury, movx, movy);
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        Curve.insertLine(curves, curx, cury, movx, movy);
        AreaOp operator;
        if (windingRule == IPathIterator.WIND_EVEN_ODD) {
            operator = new AreaOp.EOWindOp();
        } else {
            operator = new AreaOp.NZWindOp();
        }
        return operator.calculate(curves, EmptyCurves);
    }

   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     */
    private void invalidateBounds() {
        cachedBounds = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @return
     */
    private Rectangle getCachedBounds() {
        if (cachedBounds != null) {
            return cachedBounds;
        }
        Rectangle r = new Rectangle();
        if (curves.size() > 0) {
            Curve c = (Curve) curves.get(0);
            // First point is always an order 0 curve (moveto)
            r.setRect((int) (c.getX0() + .5),
                    (int) (c.getY0() + .5), 0, 0);
            for (int i = 1; i < curves.size(); i++) {
                ((Curve) curves.get(i)).enlarge(r);
            }
        }
        return (cachedBounds = r);
    }

}

class AreaIterator implements IPathIterator {

    private AffineTransform transform;
    private Vector curves;
    private int index;
    private Curve prevcurve;
    private Curve thiscurve;

    public AreaIterator(Vector curves, AffineTransform at) {
        this.curves = curves;
        this.transform = at;
        if (curves.size() >= 1) {
            thiscurve = (Curve) curves.get(0);
        }
    }

    public int getWindingRule() {
        // REMIND: Which is better, EVEN_ODD or NON_ZERO?
        //         The paths calculated could be classified either way.
        //return WIND_EVEN_ODD;
        return WIND_NON_ZERO;
    }

    public boolean isDone() {
        return (prevcurve == null && thiscurve == null);
    }

    public void next() {
        if (prevcurve != null) {
            prevcurve = null;
        } else {
            prevcurve = thiscurve;
            index++;
            if (index < curves.size()) {
                thiscurve = (Curve) curves.get(index);
                if (thiscurve.getOrder() != 0 &&
                        prevcurve.getX1() == thiscurve.getX0() &&
                        prevcurve.getY1() == thiscurve.getY0()) {
                    prevcurve = null;
                }
            } else {
                thiscurve = null;
            }
        }
    }

    public int currentSegment(int coords[]) {
        int segtype;
        int numpoints;
        if (prevcurve != null) {
            // Need to finish off junction between curves
            if (thiscurve == null || thiscurve.getOrder() == 0) {
                return SEG_CLOSE;
            }
            coords[0] = (int) (thiscurve.getX0() + .5);
            coords[1] = (int) (thiscurve.getY0() + .5);
            segtype = SEG_LINETO;
            numpoints = 1;
        } else if (thiscurve == null) {
            throw new NoSuchElementException("area iterator out of bounds");
        } else {
            double[] dblCoords = new double[coords.length];
            
            segtype = thiscurve.getSegment(dblCoords);
            numpoints = thiscurve.getOrder();
            if (numpoints == 0) {
                numpoints = 1;
            }
            for (int i = 0; i < coords.length; i++) {
                coords[i] = (int)(dblCoords[i]+.5);
            }
        }
        if (transform != null) {
            transform.transform(coords, 0, coords, 0, numpoints);
        }
        return segtype;
    }

}
