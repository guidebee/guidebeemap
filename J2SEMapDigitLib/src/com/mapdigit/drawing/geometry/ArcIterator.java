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
import java.util.NoSuchElementException;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 06MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A utility class to iterate over the path segments of an arc
 * through the IPathIterator interface.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 06/05/10
 * @author      Guidebee Pty Ltd.
 */
class ArcIterator implements IPathIterator {
    double x, y, w, h, angStRad, increment, cv;
    AffineTransform affine;
    int index;
    int arcSegs;
    int lineSegs;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param a
     * @param at
     */
    ArcIterator(Arc a, AffineTransform at) {
	this.w = a.getWidth() / 2;
	this.h = a.getHeight() / 2;
	this.x = a.getX() + w;
	this.y = a.getY() + h;
	this.angStRad = -Math.toRadians(a.getAngleStart());
	this.affine = at;
	double ext = -a.getAngleExtent();
	if (ext >= 360.0 || ext <= -360) {
	    arcSegs = 4;
	    this.increment = Math.PI / 2;
	    // btan(Math.PI / 2);
	    this.cv = 0.5522847498307933;
	    if (ext < 0) {
		increment = -increment;
		cv = -cv;
	    }
	} else {
	    arcSegs = (int) Math.ceil(Math.abs(ext) / 90.0);
	    this.increment = Math.toRadians(ext / arcSegs);
	    this.cv = btan(increment);
	    if (cv == 0) {
		arcSegs = 0;
	    }
	}
	switch (a.getArcType()) {
	case Arc.OPEN:
	    lineSegs = 0;
	    break;
	case Arc.CHORD:
	    lineSegs = 1;
	    break;
	case Arc.PIE:
	    lineSegs = 2;
	    break;
	}
	if (w < 0 || h < 0) {
	    arcSegs = lineSegs = -1;
	}
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Return the winding rule for determining the insideness of the
     * path.
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    public int getWindingRule() {
	return WIND_NON_ZERO;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if there are more points to read.
     * @return true if there are more points to read
     */
    public boolean isDone() {
	return index > arcSegs + lineSegs;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
	index++;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A float array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of float x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    public int currentSegment(int[] coords) {
	if (isDone()) {
	    throw new NoSuchElementException("arc iterator out of bounds");
	}
	double angle = angStRad;
	if (index == 0) {
	    coords[0] = (int) (x + Math.cos(angle) * w+.5);
	    coords[1] = (int) (y + Math.sin(angle) * h+.5);
	    if (affine != null) {
		affine.transform(coords, 0, coords, 0, 1);
	    }
	    return SEG_MOVETO;
	}
	if (index > arcSegs) {
	    if (index == arcSegs + lineSegs) {
		return SEG_CLOSE;
	    }
	    coords[0] = (int) (x+.5);
	    coords[1] = (int) (y+.5);
	    if (affine != null) {
		affine.transform(coords, 0, coords, 0, 1);
	    }
	    return SEG_LINETO;
	}
	angle += increment * (index - 1);
	double relx = Math.cos(angle);
	double rely = Math.sin(angle);
	coords[0] = (int) (x + (relx - cv * rely) * w+.5);
	coords[1] = (int) (y + (rely + cv * relx) * h+.5);
	angle += increment;
	relx = Math.cos(angle);
	rely = Math.sin(angle);
	coords[2] = (int) (x + (relx + cv * rely) * w+.5);
	coords[3] = (int) (y + (rely - cv * relx) * h+.5);
	coords[4] = (int) (x + relx * w+.5);
	coords[5] = (int) (y + rely * h+.5);
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, 3);
	}
	return SEG_CUBICTO;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /*
     * btan computes the length (k) of the control segments at
     * the beginning and end of a cubic bezier that approximates
     * a segment of an arc with extent less than or equal to
     * 90 degrees.  This length (k) will be used to generate the
     * 2 bezier control points for such a segment.
     *
     *   Assumptions:
     *     a) arc is centered on 0,0 with radius of 1.0
     *     b) arc extent is less than 90 degrees
     *     c) control points should preserve tangent
     *     d) control segments should have equal length
     *
     *   Initial data:
     *     start angle: ang1
     *     end angle:   ang2 = ang1 + extent
     *     start point: P1 = (x1, y1) = (cos(ang1), sin(ang1))
     *     end point:   P4 = (x4, y4) = (cos(ang2), sin(ang2))
     *
     *   Control points:
     *     P2 = (x2, y2)
     *     | x2 = x1 - k * sin(ang1) = cos(ang1) - k * sin(ang1)
     *     | y2 = y1 + k * cos(ang1) = sin(ang1) + k * cos(ang1)
     *
     *     P3 = (x3, y3)
     *     | x3 = x4 + k * sin(ang2) = cos(ang2) + k * sin(ang2)
     *     | y3 = y4 - k * cos(ang2) = sin(ang2) - k * cos(ang2)
     *
     * The formula for this length (k) can be found using the
     * following derivations:
     *
     *   Midpoints:
     *     a) bezier (t = 1/2)
     *        bPm = P1 * (1-t)^3 +
     *              3 * P2 * t * (1-t)^2 +
     *              3 * P3 * t^2 * (1-t) +
     *              P4 * t^3 =
     *            = (P1 + 3P2 + 3P3 + P4)/8
     *
     *     b) arc
     *        aPm = (cos((ang1 + ang2)/2), sin((ang1 + ang2)/2))
     *
     *   Let angb = (ang2 - ang1)/2; angb is half of the angle
     *   between ang1 and ang2.
     *
     *   Solve the equation bPm == aPm
     *
     *     a) For xm coord:
     *        x1 + 3*x2 + 3*x3 + x4 = 8*cos((ang1 + ang2)/2)
     *
     *        cos(ang1) + 3*cos(ang1) - 3*k*sin(ang1) +
     *        3*cos(ang2) + 3*k*sin(ang2) + cos(ang2) =
     *        = 8*cos((ang1 + ang2)/2)
     *
     *        4*cos(ang1) + 4*cos(ang2) + 3*k*(sin(ang2) - sin(ang1)) =
     *        = 8*cos((ang1 + ang2)/2)
     *
     *        8*cos((ang1 + ang2)/2)*cos((ang2 - ang1)/2) +
     *        6*k*sin((ang2 - ang1)/2)*cos((ang1 + ang2)/2) =
     *        = 8*cos((ang1 + ang2)/2)
     *
     *        4*cos(angb) + 3*k*sin(angb) = 4
     *
     *        k = 4 / 3 * (1 - cos(angb)) / sin(angb)
     *
     *     b) For ym coord we derive the same formula.
     *
     * Since this formula can generate "NaN" values for small
     * angles, we will derive a safer form that does not involve
     * dividing by very small values:
     *     (1 - cos(angb)) / sin(angb) =
     *     = (1 - cos(angb))*(1 + cos(angb)) / sin(angb)*(1 + cos(angb)) =
     *     = (1 - cos(angb)^2) / sin(angb)*(1 + cos(angb)) =
     *     = sin(angb)^2 / sin(angb)*(1 + cos(angb)) =
     *     = sin(angb) / (1 + cos(angb))
     *
     */
    private static double btan(double increment) {
	increment /= 2.0;
	return 4.0 / 3.0 * Math.sin(increment) / (1.0 + Math.cos(increment));
    }

   
}
