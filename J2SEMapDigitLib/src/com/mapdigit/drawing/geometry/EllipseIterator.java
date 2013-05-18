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
import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of an ellipse
 * through the IPathIterator interface.
 *
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 09/05/10
 * @author      Guidebee Pty Ltd.
 */
class EllipseIterator implements IPathIterator {

    double x, y, w, h;
    AffineTransform affine;
    int index;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param e
     * @param at
     */
    EllipseIterator(Ellipse e, AffineTransform at) {
        this.x = e.getX();
        this.y = e.getY();
        this.w = e.getWidth();
        this.h = e.getHeight();
        this.affine = at;
        if (w < 0 || h < 0) {
            index = 6;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
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
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if there are more points to read.
     * @return true if there are more points to read
     */
    public boolean isDone() {
        return index > 5;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
        index++;
    }    // ArcIterator.btan(Math.PI/2)

    public static final double CtrlVal = 0.5522847498307933;

    /*
     * ctrlpts contains the control points for a set of 4 cubic
     * bezier curves that approximate a circle of radius 0.5
     * centered at 0.5, 0.5
     */
    private static final double pcv = 0.5 + CtrlVal * 0.5;
    private static final double ncv = 0.5 - CtrlVal * 0.5;
    private static double ctrlpts[][] = {
        {1.0, pcv, pcv, 1.0, 0.5, 1.0},
        {ncv, 1.0, 0.0, pcv, 0.0, 0.5},
        {0.0, ncv, ncv, 0.0, 0.5, 0.0},
        {pcv, 0.0, 1.0, ncv, 1.0, 0.5}
    };

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
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
            throw new NoSuchElementException("ellipse iterator out of bounds");
        }
        if (index == 5) {
            return SEG_CLOSE;
        }
        if (index == 0) {
            double ctrls[] = ctrlpts[3];
            coords[0] = (int) (x + ctrls[4] * w + .5);
            coords[1] = (int) (y + ctrls[5] * h + .5);
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, 1);
            }
            return SEG_MOVETO;
        }
        double ctrls[] = ctrlpts[index - 1];
        coords[0] = (int) (x + ctrls[0] * w + .5);
        coords[1] = (int) (y + ctrls[1] * h + .5);
        coords[2] = (int) (x + ctrls[2] * w + .5);
        coords[3] = (int) (y + ctrls[3] * h + .5);
        coords[4] = (int) (x + ctrls[4] * w + .5);
        coords[5] = (int) (y + ctrls[5] * h + .5);
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 3);
        }
        return SEG_CUBICTO;
    }
}
