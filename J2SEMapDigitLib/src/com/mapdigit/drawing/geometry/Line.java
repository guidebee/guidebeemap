//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////

/**
 * A line segment specified with int coordinates.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 09/05/10
 * @author      Guidebee Pty Ltd.
 */
public  class Line implements IShape  {

    /**
     * The X coordinate of the start point of the line segment.
     */
    public int x1;

    /**
     * The Y coordinate of the start point of the line segment.
     */
    public int y1;

    /**
     * The X coordinate of the end point of the line segment.
     */
    public int x2;

    /**
     * The Y coordinate of the end point of the line segment.
     */
    public int y2;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a Line with coordinates (0, 0) -> (0, 0).
     */
    public Line() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a <code>Line</code> from the
     * specified coordinates.
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     */
    public Line(int x1, int y1, int x2, int y2) {
        setLine(x1, y1, x2, y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a <code>Line</code> from the
     * specified <code>Point</code> objects.
     * @param p1 the start <code>Point</code> of this line segment
     * @param p2 the end <code>Point</code> of this line segment
     */
    public Line(Point p1, Point p2) {
        setLine(p1, p2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the X coordinate of the start point in integer.
     * @return the X coordinate of the start point of this
     *         {@code Line} object.
     */
    public int getX1() {
        return  x1 ;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the Y coordinate of the start point in integer.
     * @return the Y coordinate of the start point of this
     *         {@code Line} object.
     */
    public int getY1() {
        return  y1;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the X coordinate of the end point.
     * @return the X coordinate of the end point of this
     *         {@code Line} object.
     */
    public int getX2() {
        return  x2 ;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the Y coordinate of the end point.
     * @return the Y coordinate of the end point of this
     *         {@code Line} object.
     */
    public int getY2() {
        return y2;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the start <code>Point</code> of this <code>Line</code>.
     * @return the start <code>Point</code> of this <code>Line</code>.
     */
    public Point getP1() {
        return new Point(x1, y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the end <code>Point</code> of this <code>Line</code>.
     * @return the end <code>Point</code> of this <code>Line</code>.
     */
    public Point getP2() {
        return new Point(x2, y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location of the end points of this <code>Line</code> to
     * the specified coordinates.
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     */
    public void setLine(int x1, int y1, int x2, int y2) {
        this.x1 =  x1 ;
        this.y1 =  y1 ;
        this.x2 =  x2 ;
        this.y2 =  y2 ;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location of the end points of this <code>Line</code> to
     * the specified <code>Point</code> coordinates.
     * @param p1 the start <code>Point</code> of the line segment
     * @param p2 the end <code>Point</code> of the line segment
     */
    public void setLine(Point p1, Point p2) {
	setLine(p1.x, p1.y, p2.x, p2.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location of the end points of this <code>Line</code> to
     * the same as those end points of the specified <code>Line</code>.
     * @param l the specified <code>Line</code>
     */
    public void setLine(Line l) {
	setLine(l.x1, l.y1, l.x2, l.y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public Rectangle getBounds() {
        int x, y, w, h;
        if (x1 < x2) {
            x = x1;
            w = x2 - x1;
        } else {
            x = x2;
            w = x1 - x2;
        }
        if (y1 < y2) {
            y = y1;
            h = y2 - y1;
        } else {
            y = y2;
            h = y1 - y2;
        }
        return new Rectangle(x, y, w, h);
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an indicator of where the specified point
     * {@code (px,py)} lies with respect to the line segment from
     * {@code (x1,y1)} to {@code (x2,y2)}.
     * The return value can be either 1, -1, or 0 and indicates
     * in which direction the specified line must pivot around its
     * first end point, {@code (x1,y1)}, in order to point at the
     * specified point {@code (px,py)}.
     * <p>A return value of 1 indicates that the line segment must
     * turn in the direction that takes the positive X axis towards
     * the negative Y axis.  In the default coordinate system used by
     * Java 2D, this direction is counterclockwise.
     * <p>A return value of -1 indicates that the line segment must
     * turn in the direction that takes the positive X axis towards
     * the positive Y axis.  In the default coordinate system, this
     * direction is clockwise.
     * <p>A return value of 0 indicates that the point lies
     * exactly on the line segment.  Note that an indicator value
     * of 0 is rare and not useful for determining colinearity
     * because of floating point rounding issues.
     * <p>If the point is colinear with the line segment, but
     * not between the end points, then the value will be -1 if the point
     * lies "beyond {@code (x1,y1)}" or 1 if the point lies
     * "beyond {@code (x2,y2)}".
     *
     * @param x1 the X coordinate of the start point of the
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           specified line segment
     * @param px the X coordinate of the specified point to be
     *           compared with the specified line segment
     * @param py the Y coordinate of the specified point to be
     *           compared with the specified line segment
     * @return an integer that indicates the position of the third specified
     *			coordinates with respect to the line segment formed
     *			by the first two specified coordinates.
     */
    public static int relativeCCW(int x1, int y1,
				  int x2, int y2,
				  int px, int py)
    {
	x2 -= x1;
	y2 -= y1;
	px -= x1;
	py -= y1;
	int ccw = px * y2 - py * x2;
	if (ccw == 0) {
	    // The point is colinear, classify based on which side of
	    // the segment the point falls on.  We can calculate a
	    // relative value using the projection of px,py onto the
	    // segment - a negative value indicates the point projects
	    // outside of the segment in the direction of the particular
	    // endpoint used as the origin for the projection.
	    ccw = px * x2 + py * y2;
	    if (ccw >0) {
		// Reverse the projection to be relative to the original x2,y2
		// x2 and y2 are simply negated.
		// px and py need to have (x2 - x1) or (y2 - y1) subtracted
		//    from them (based on the original values)
		// Since we really want to get a positive answer when the
		//    point is "beyond (x2,y2)", then we want to calculate
		//    the inverse anyway - thus we leave x2 & y2 negated.
		px -= x2;
		py -= y2;
		ccw = px * x2 + py * y2;
		if (ccw < 0) {
		    ccw = 0;
		}
	    }
	}
	return (ccw < 0) ? -1 : ((ccw > 0) ? 1 : 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an indicator of where the specified point
     * {@code (px,py)} lies with respect to this line segment.
     * See the method comments of
     * {@link #relativeCCW(int, int, int, int, int, int)}
     * to interpret the return value.
     * @param px the X coordinate of the specified point
     *           to be compared with this <code>Line</code>
     * @param py the Y coordinate of the specified point
     *           to be compared with this <code>Line</code>
     * @return an integer that indicates the position of the specified
     *         coordinates with respect to this <code>Line</code>
     */
    public int relativeCCW(int px, int py) {
	return relativeCCW(x1, y1, x2, y2, px, py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an indicator of where the specified <code>Point</code>
     * lies with respect to this line segment.
     * See the method comments of
     * {@link #relativeCCW(int, int, int, int, int, int)}
     * to interpret the return value.
     * @param p the specified <code>Point</code> to be compared
     *          with this <code>Line</code>
     * @return an integer that indicates the position of the specified
     *         <code>Point</code> with respect to this <code>Line</code>
     */
    public int relativeCCW(Point p) {
	return relativeCCW(x1, y1, x2, y2,
			   p.x, p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the line segment from {@code (x1,y1)} to
     * {@code (x2,y2)} intersects the line segment from {@code (x3,y3)}
     * to {@code (x4,y4)}.
     *
     * @param x1 the X coordinate of the start point of the first
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the first
     *           specified line segment
     * @param x2 the X coordinate of the end point of the first
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the first
     *           specified line segment
     * @param x3 the X coordinate of the start point of the second
     *           specified line segment
     * @param y3 the Y coordinate of the start point of the second
     *           specified line segment
     * @param x4 the X coordinate of the end point of the second
     *           specified line segment
     * @param y4 the Y coordinate of the end point of the second
     *           specified line segment
     * @return <code>true</code> if the first specified line segment
     *			and the second specified line segment intersect
     *			each other; <code>false</code> otherwise.
     */
    public static boolean linesIntersect(int x1, int y1,
					 int x2, int y2,
					 int x3, int y3,
					 int x4, int y4)
    {
	return ((relativeCCW(x1, y1, x2, y2, x3, y3) *
		 relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
		&& (relativeCCW(x3, y3, x4, y4, x1, y1) *
		    relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the line segment from {@code (x1,y1)} to
     * {@code (x2,y2)} intersects this line segment.
     *
     * @param x1 the X coordinate of the start point of the
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           specified line segment
     * @return <true> if this line segment and the specified line segment
     *			intersect each other; <code>false</code> otherwise.
     */
    public boolean intersectsLine(int x1, int y1, int x2, int y2) {
	return linesIntersect(x1, y1, x2, y2,
			      this.x1, this.y1, this.x2, this.y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified line segment intersects this line segment.
     * @param l the specified <code>Line</code>
     * @return <code>true</code> if this line segment and the specified line
     *			segment intersect each other;
     *			<code>false</code> otherwise.
     */
    public boolean intersectsLine(Line l) {
	return linesIntersect(l.x1, l.y1, l.x2, l.y2,
			      this.x1, this.y1, this.x2, this.y2);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from a point to a line segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the specified end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.
     *
     * @param x1 the X coordinate of the start point of the
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           specified line segment
     * @param px the X coordinate of the specified point being
     *           measured against the specified line segment
     * @param py the Y coordinate of the specified point being
     *           measured against the specified line segment
     * @return a int value that is the square of the distance from the
     *			specified point to the specified line segment.
     */
    public static int ptSegDistSq(int x1, int y1,
				     int x2, int y2,
				     int px, int py)
    {
	// Adjust vectors relative to x1,y1
	// x2,y2 becomes relative vector from x1,y1 to end of segment
	x2 -= x1;
	y2 -= y1;
	// px,py becomes relative vector from x1,y1 to test point
	px -= x1;
	py -= y1;
	int dotprod = px * x2 + py * y2;
	int projlenSq;
	if (dotprod <= 0) {
	    // px,py is on the side of x1,y1 away from x2,y2
	    // distance to segment is length of px,py vector
	    // "length of its (clipped) projection" is now 0.0
	    projlenSq = 0;
	} else {
	    // switch to backwards vectors relative to x2,y2
	    // x2,y2 are already the negative of x1,y1=>x2,y2
	    // to get px,py to be the negative of px,py=>x2,y2
	    // the dot product of two negated vectors is the same
	    // as the dot product of the two normal vectors
	    px = x2 - px;
	    py = y2 - py;
	    dotprod = px * x2 + py * y2;
	    if (dotprod <= 0) {
		// px,py is on the side of x2,y2 away from x1,y1
		// distance to segment is length of (backwards) px,py vector
		// "length of its (clipped) projection" is now 0.0
		projlenSq = 0;
	    } else {
		// px,py is between x1,y1 and x2,y2
		// dotprod is the length of the px,py vector
		// projected on the x2,y2=>x1,y1 vector times the
		// length of the x2,y2=>x1,y1 vector
		projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
	    }
	}
	// Distance to line is now the length of the relative point
	// vector minus the length of its projection onto the line
	// (which is zero if the projection falls outside the range
	//  of the line segment).
	int lenSq = px * px + py * py - projlenSq;
	if (lenSq < 0) {
	    lenSq = 0;
	}
	return lenSq;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from a point to a line segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the specified end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.
     *
     * @param x1 the X coordinate of the start point of the
     *           specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           specified line segment
     * @param px the X coordinate of the specified point being
     *           measured against the specified line segment
     * @param py the Y coordinate of the specified point being
     *           measured against the specified line segment
     * @return a int value that is the distance from the specified point
     *				to the specified line segment.
     */
    public static int ptSegDist(int x1, int y1,
				   int x2, int y2,
				   int px, int py)
    {
        long dis=ptSegDistSq(x1, y1, x2, y2, px, py);
        dis<<=MathFP.DEFAULT_PRECISION;
        return MathFP.toInt(MathFP.sqrt(dis));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from a point to this line segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the current line's end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.0.
     *
     * @param px the X coordinate of the specified point being
     *           measured against this line segment
     * @param py the Y coordinate of the specified point being
     *           measured against this line segment
     * @return a int value that is the distance from the specified
     *			point to the current line segment.
     */
    public int ptSegDist(int px, int py) {
	return ptSegDist(x1, y1, x2, y2, px, py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from a <code>Point</code> to this line
     * segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the current line's end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.
     * @param pt the specified <code>Point</code> being measured
     *		against this line segment
     * @return a int value that is the distance from the specified
     *				<code>Point</code> to the current line
     *				segment.
     */
    public int ptSegDist(Point pt) {
	return ptSegDist(x1, y1, x2, y2,
			 pt.x, pt.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from a point to this line segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the current line's end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.0.
     *
     * @param px the X coordinate of the specified point being
     *           measured against this line segment
     * @param py the Y coordinate of the specified point being
     *           measured against this line segment
     * @return a int value that is the square of the distance from the
     *			specified point to the current line segment.
     */
    public int ptSegDistSq(int px, int py) {
	return ptSegDistSq(x1, y1, x2, y2, px, py);
    }

     ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from a <code>Point</code> to
     * this line segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the current line's end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.0.
     * @param pt the specified <code>Point</code> being measured against
     *	         this line segment.
     * @return a int value that is the square of the distance from the
     *			specified <code>Point</code> to the current
     *			line segment.
     */
    public int ptSegDistSq(Point pt) {
	return ptSegDistSq(x1, y1, x2, y2,
			   pt.x, pt.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from a point to a line.
     * The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line
     * defined by the specified coordinates.  If the specified point
     * intersects the line, this method returns 0.
     *
     * @param x1 the X coordinate of the start point of the specified line
     * @param y1 the Y coordinate of the start point of the specified line
     * @param x2 the X coordinate of the end point of the specified line
     * @param y2 the Y coordinate of the end point of the specified line
     * @param px the X coordinate of the specified point being
     *           measured against the specified line
     * @param py the Y coordinate of the specified point being
     *           measured against the specified line
     * @return a int value that is the square of the distance from the
     *			specified point to the specified line.
     */
    public static int ptLineDistSq(int x1, int y1,
				      int x2, int y2,
				      int px, int py)
    {
	// Adjust vectors relative to x1,y1
	// x2,y2 becomes relative vector from x1,y1 to end of segment
	x2 -= x1;
	y2 -= y1;
	// px,py becomes relative vector from x1,y1 to test point
	px -= x1;
	py -= y1;
	int dotprod = px * x2 + py * y2;
	// dotprod is the length of the px,py vector
	// projected on the x1,y1=>x2,y2 vector times the
	// length of the x1,y1=>x2,y2 vector
	int projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
	// Distance to line is now the length of the relative point
	// vector minus the length of its projection onto the line
	int lenSq = px * px + py * py - projlenSq;
	if (lenSq < 0) {
	    lenSq = 0;
	}
	return lenSq;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from a specified
     * <code>Point</code> to this line.
     * The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line
     * defined by this <code>Line</code>.  If the specified point
     * intersects the line, this method returns 0.0.
     * @param pt the specified <code>Point</code> being measured
     *           against this line
     * @return a int value that is the square of the distance from a
     *			specified <code>Point</code> to the current
     *			line.
     */
    public int ptLineDistSq(Point pt) {
	return ptLineDistSq(x1, y1, x2, y2,
			    pt.x, pt.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 05NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from a point to this line.
     * The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line
     * defined by this <code>Line</code>.  If the specified point
     * intersects the line, this method returns 0.
     *
     * @param px the X coordinate of the specified point being
     *           measured against this line
     * @param py the Y coordinate of the specified point being
     *           measured against this line
     * @return a int value that is the square of the distance from a
     *			specified point to the current line.
     */
    public int ptLineDistSq(int px, int py) {
	return ptLineDistSq(x1, y1, x2, y2, px, py);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from a point to a line.
     * The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line
     * defined by the specified coordinates.  If the specified point
     * intersects the line, this method returns 0.
     *
     * @param x1 the X coordinate of the start point of the specified line
     * @param y1 the Y coordinate of the start point of the specified line
     * @param x2 the X coordinate of the end point of the specified line
     * @param y2 the Y coordinate of the end point of the specified line
     * @param px the X coordinate of the specified point being
     *           measured against the specified line
     * @param py the Y coordinate of the specified point being
     *           measured against the specified line
     * @return a int value that is the distance from the specified
     *			 point to the specified line.
     */
    public static int ptLineDist(int x1, int y1,
            int x2, int y2,
            int px, int py) {
        long dis = ptLineDistSq(x1, y1, x2, y2, px, py);
        dis <<= MathFP.DEFAULT_PRECISION;
       return MathFP.toInt(MathFP.sqrt(dis));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from a point to this line.
     * The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line
     * defined by this <code>Line</code>.  If the specified point
     * intersects the line, this method returns 0.
     *
     * @param px the X coordinate of the specified point being
     *           measured against this line
     * @param py the Y coordinate of the specified point being
     *           measured against this line
     * @return a int value that is the distance from a specified point
     *			to the current line.
     */
    public int ptLineDist(int px, int py) {
	return ptLineDist(x1, y1, x2, y2, px, py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from a <code>Point</code> to this line.
     * The distance measured is the distance between the specified
     * point and the closest point on the infinitely-extended line
     * defined by this <code>Line</code>.  If the specified point
     * intersects the line, this method returns 0.
     * @param pt the specified <code>Point</code> being measured
     * @return a int value that is the distance from a specified
     *			<code>Point</code> to the current line.
     */
    public int ptLineDist(Point pt) {
	return ptLineDist(x1, y1, x2, y2,
			 pt.x, pt.y);
    }
    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if a specified coordinate is inside the boundary of this
     * <code>Line</code>.  This method is required to implement the
     * {@link IShape} interface, but in the case of <code>Line</code>
     * objects it always returns <code>false</code> since a line contains
     * no area.
     * @param x the X coordinate of the specified point to be tested
     * @param y the Y coordinate of the specified point to be tested
     * @return <code>false</code> because a <code>Line</code> contains
     * no area.
     */
    public boolean contains(int x, int y) {
	return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 05NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if a given <code>Point</code> is inside the boundary of
     * this <code>Line</code>.
     * This method is required to implement the {@link IShape} interface,
     * but in the case of <code>Line</code> objects it always returns
     * <code>false</code> since a line contains no area.
     * @param p the specified <code>Point</code> to be tested
     * @return <code>false</code> because a <code>Line</code> contains
     * no area.
     */
    public boolean contains(Point p) {
	return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the interior of this <code>Line</code> entirely contains
     * the specified <code>Rectangle</code>.
     * This method is required to implement the <code>IShape</code> interface,
     * but in the case of <code>Line</code> objects it always returns
     * <code>false</code> since a line contains no area.
     * @param r the specified <code>Rectangle</code> to be tested
     * @return <code>false</code> because a <code>Line</code> contains
     * no area.
     */
    public boolean contains(Rectangle r) {
	return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the interior of this <code>Line</code> entirely contains
     * the specified set of rectangular coordinates.
     * This method is required to implement the <code>IShape</code> interface,
     * but in the case of <code>Line</code> objects it always returns
     * false since a line contains no area.
     * @param x the X coordinate of the upper-left corner of the
     *          specified rectangular area
     * @param y the Y coordinate of the upper-left corner of the
     *          specified rectangular area
     * @param w the width of the specified rectangular area
     * @param h the height of the specified rectangular area
     * @return <code>false</code> because a <code>Line</code> contains
     * no area.
     */
    public boolean contains(int x, int y, int w, int h) {
	return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean intersects(int x, int y, int w, int h) {
	return intersects(new Rectangle(x, y, w, h));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean intersects(Rectangle r) {
	return r.intersectsLine(x1, y1, x2, y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an iteration object that defines the boundary of this
     * <code>Line</code>.
     * The iterator for this class is not multi-threaded safe,
     * which means that this <code>Line</code> class does not
     * guarantee that modifications to the geometry of this
     * <code>Line</code> object do not affect any iterations of that
     * geometry that are already in process.
     * @param at the specified {@link AffineTransform}
     * @return a {@link IPathIterator} that defines the boundary of this
     *		<code>Line</code>.
     */
    public IPathIterator getPathIterator(AffineTransform at) {
	return new LineIterator(this, at);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an iteration object that defines the boundary of this
     * flattened <code>Line</code>.
     * The iterator for this class is not multi-threaded safe,
     * which means that this <code>Line</code> class does not
     * guarantee that modifications to the geometry of this
     * <code>Line</code> object do not affect any iterations of that
     * geometry that are already in process.
     * @param at the specified <code>AffineTransform</code>
     * @param flatness the maximum amount that the control points for a
     *		given curve can vary from colinear before a subdivided
     *		curve is replaced by a straight line connecting the
     *		end points.  Since a <code>Line</code> object is
     *	        always flat, this parameter is ignored.
     * @return a <code>IPathIterator</code> that defines the boundary of the
     *			flattened <code>Line</code>
     */
    public IPathIterator getPathIterator(AffineTransform at, int flatness) {
	return new LineIterator(this, at);
    }


}