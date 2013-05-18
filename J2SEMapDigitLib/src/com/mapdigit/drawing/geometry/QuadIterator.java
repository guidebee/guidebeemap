//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 08NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

import java.util.NoSuchElementException;

//--------------------------------- IMPORTS ------------------------------------
/**
 * A utility class to iterate over the path segments of a quadratic curve
 * segment through the IPathIterator interface.
 *
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 08/11/08
 * @author      Guidebee Pty Ltd.
 */
class QuadIterator implements IPathIterator {

    QuadCurve quad;
    AffineTransform affine;
    int index;

    QuadIterator(QuadCurve q, AffineTransform at) {
        this.quad = q;
        this.affine = at;
    }

    /**
     * Return the winding rule for determining the insideness of the
     * path.
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    /**
     * Tests if there are more points to read.
     * @return true if there are more points to read
     */
    public boolean isDone() {
        return (index > 1);
    }

    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
        index++;
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A int array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of int x,y coordinates.
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
            throw new NoSuchElementException("quad iterator iterator out of bounds");
        }
        int type;
        if (index == 0) {
            coords[0] = quad.getX1();
            coords[1] = quad.getY1();
            type = SEG_MOVETO;
        } else {
            coords[0] = quad.getCtrlX();
            coords[1] = quad.getCtrlY();
            coords[2] = quad.getX2();
            coords[3] = quad.getY2();
            type = SEG_QUADTO;
        }
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, index == 0 ? 1 : 2);
        }
        return type;
    }
}
