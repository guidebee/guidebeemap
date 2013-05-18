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
 * a 2D rectangle class.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public class RectangleFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the bottom.
     * @return
     */
    public int getBottom() {
       return ff_ymax;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the top.
     * @return
     */
    public int getTop() {
       return ff_ymin;
   }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the left.
     * @return
     */
    public int getLeft() {
        return ff_xmin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the right.
     * @return
     */
    public int getRight() {
        return ff_xmax;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the width.
     * @return
     */
    public int getWidth() {
        return ff_xmax - ff_xmin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the width.
     * @param value
     */
    public void setWidth(int value) {
        if (value < 0) {
            return;
        }
        ff_xmax = ff_xmin + value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the height.
     * @return
     */
    public int getHeight() {
        return ff_ymax - ff_ymin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the height.
     * @param value
     */
    public void setHeight(int value) {

        if (value < 0) {
            return;
        }
        ff_ymax = ff_ymin + value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the X.
     * @return
     */
    public int getX() {
        return ff_xmin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the X.
     * @param value
     */
    public void setX(int value) {
        ff_xmin = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the Y.
     * @return
     */
    public int getY() {
      return ff_ymin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the Y.
     * @param value
     */
    public void setY(int value) {
        ff_ymin = value;

    }
    /**
     * The empty rectangle.
     */
    public static final RectangleFP EMPTY = new RectangleFP();
    private int ff_xmin;
    private int ff_xmax;
    private int ff_ymin;
    private int ff_ymax;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public RectangleFP() {
        ff_xmin = ff_xmax = ff_ymin = ff_ymax = SingleFP.NaN;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param r
     */
    public RectangleFP(RectangleFP r) {
        reset(r);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     */
    public RectangleFP(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        reset(ff_xmin, ff_ymin, ff_xmax, ff_ymax);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Reset the rectangle.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @return
     */
    public RectangleFP reset(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        this.ff_xmin = MathFP.min(ff_xmin, ff_xmax);
        this.ff_xmax = MathFP.max(ff_xmin, ff_xmax);
        this.ff_ymin = MathFP.min(ff_ymin, ff_ymax);
        this.ff_ymax = MathFP.max(ff_ymin, ff_ymax);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the rectangle.
     * @param r
     * @return
     */
    public RectangleFP reset(RectangleFP r) {
        return reset(r.ff_xmin, r.ff_ymin, r.ff_xmax, r.ff_ymax);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if the rectangle is empty.
     * @return
     */
    public boolean isEmpty() {
        return ff_xmin == SingleFP.NaN || ff_xmax == SingleFP.NaN ||
                ff_ymin == SingleFP.NaN || ff_ymax == SingleFP.NaN;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Translate the rectangle.
     * @param ff_dx
     * @param ff_dy
     */
    public void offset(int ff_dx, int ff_dy) {
        if (!isEmpty()) {
            ff_xmin += ff_dx;
            ff_xmax += ff_dx;
            ff_ymin += ff_dy;
            ff_ymax += ff_dy;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Calculate the union of the two rectangle.
     * @param r
     * @return
     */
    public RectangleFP union(RectangleFP r) {
        if (!r.isEmpty()) {
            if (isEmpty()) {
                reset(r);
            } else {
                reset(MathFP.min(ff_xmin, r.ff_xmin),
                        MathFP.max(ff_xmax, r.ff_xmax),
                        MathFP.min(ff_ymin, r.ff_ymin),
                        MathFP.max(ff_ymax, r.ff_ymax));
            }
        }
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the union of the rectangle and the given point.
     * @param p
     * @return
     */
    public RectangleFP union(PointFP p) {
        if (!isEmpty()) {
            reset(MathFP.min(ff_xmin, p.x), MathFP.max(ff_xmax, p.x),
                    MathFP.min(ff_ymin, p.y), MathFP.max(ff_ymax, p.y));
        }
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see this rectange intersect with given rectange.
     * @param r
     * @return
     */
    public boolean intersectsWith(RectangleFP r) {
        return ff_xmin <= r.ff_xmax && r.ff_xmin <= ff_xmax &&
                ff_ymin <= r.ff_ymax && r.ff_ymin <= ff_ymax;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see if this rectangle contains given point.
     * @param p
     * @return
     */
    public boolean contains(PointFP p) {
        return ff_xmin <= p.x && p.x <= ff_xmax
                && ff_ymin <= p.y && p.y <= ff_ymax;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see two rect has same location.
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if(o instanceof RectangleFP){
        RectangleFP r = (RectangleFP) o;
        if (r == null) {
            return false;
        } else {
            return r.ff_xmax == ff_xmax && r.ff_xmin == ff_xmin
                    && r.ff_ymax == ff_ymax && r.ff_ymin == ff_ymin;
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
     * Returns the hashcode for this <code>Rectangle</code>.
     * @return the hashcode for this <code>Rectangle</code>.
     */
    public int hashCode() {
	int bits = ff_xmin & 0xFFFF0000 + ff_ymin & 0x0000FFFF;

        return bits;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * to string.
     * @return
     */
    public String toString() {
        return "Rectangle" + " (" + new SingleFP(ff_xmin) + "," +
                new SingleFP(ff_ymin) + ")-(" + new SingleFP(ff_xmax) + "," +
                new SingleFP(ff_ymax) + ")";
    }
}