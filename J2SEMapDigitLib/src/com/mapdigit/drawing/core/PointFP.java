//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 20APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.core;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 20APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * a 2D point class.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public class PointFP {

    /**
     * X coordinate.
     */
    public int x = 0;

    /**
     * Y coordinate.
     */
    public int y = 0;

    /**
     * the (0,0) point.
     */
    public static final PointFP ORIGIN = new PointFP(0, 0);

    /**
     * Empty point.
     */
    public static final PointFP EMPTY = new PointFP(SingleFP.NaN, SingleFP.NaN);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public PointFP() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param p
     */
    public PointFP(PointFP p) {
        reset(p);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param ff_x
     * @param ff_y
     */
    public PointFP(int ff_x, int ff_y) {
        reset(ff_x, ff_y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see if the point is empty one.
     * @param p
     * @return
     */
    public static boolean isEmpty(PointFP p) {
        return EMPTY.equals(p);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the point to the same location as the given point.
     * @param p
     * @return
     */
    public PointFP reset(PointFP p) {
        return reset(p.x, p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the point to give location.
     * @param ff_x
     * @param ff_y
     * @return
     */
    public PointFP reset(int ff_x, int ff_y) {
        this.x = ff_x;
        this.y = ff_y;
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * transform the point with give matrix.
     * @param m
     * @return
     */
    public PointFP transform(MatrixFP m) {
        reset(MathFP.mul(x, m.scaleX) + MathFP.mul(y, m.rotateY) + m.translateX,
                MathFP.mul(y, m.scaleY) + MathFP.mul(x, m.rotateX) + m.translateY);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * the distance between 2 points.
     * @param p1
     * @param p2
     * @return
     */
    static public int distance(PointFP p1, PointFP p2) {
        return distance(p1.x - p2.x, p1.y - p2.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * calculate the distance.
     * @param dx
     * @param dy
     * @return
     */
    static public int distance(int dx, int dy) {
        dx = MathFP.abs(dx);
        dy = MathFP.abs(dy);
        if (dx == 0) {
            return dy;
        } else if (dy == 0) {
            return dx;
        }

        long len = (((long) dx * dx) >> SingleFP.DECIMAL_BITS)
                + (((long) dy * dy) >> SingleFP.DECIMAL_BITS);
        long s = (dx + dy) - (MathFP.min(dx, dy) >> 1);
        s = (s + ((len << SingleFP.DECIMAL_BITS) / s)) >> 1;
        s = (s + ((len << SingleFP.DECIMAL_BITS) / s)) >> 1;
        return (int) s;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add given point the location to this point.
     * @param p
     * @return
     */
    public PointFP add(PointFP p) {
        reset(x + p.x, y + p.y);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * substract given distance (x,y) to this point.
     * @param p
     * @return
     */
    public PointFP sub(PointFP p) {
        reset(x - p.x, y - p.y);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see the two point are equal.
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if(obj instanceof PointFP){
        PointFP p = (PointFP) obj;
        if (p != null) {
            return x == p.x && y == p.y;
        } else {
            return false;
        }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>Point</code>.
     * @return      a hash code for this <code>Point</code>.
     */
    public int hashCode() {
	int bits = (x <<16) & 0xFFFF0000;
	bits ^= y;
	return (int)bits;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * convert to a string.
     * @return
     */
    public String toString() {
        return "Point(" + new SingleFP(x) + "," + new SingleFP(y) + ")";
    }
}
