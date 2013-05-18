//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>RoundRectangle</code> class defines a rectangle with rounded
 * corners all specified in <code>long</code> coordinates.
  * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 09/05/10
 * @author      Guidebee Pty Ltd.
 */
public class RoundRectangle  extends RectangularShape  {

    /**
     * The X coordinate of this <code>RoundRectangle</code>.
     */
    public double x;

    /**
     * The Y coordinate of this <code>RoundRectangle</code>.
     */
    public double y;

    /**
     * The width of this <code>RoundRectangle</code>.
     */
    public double width;

    /**
     * The height of this <code>RoundRectangle</code>.
     */
    public double height;

    /**
     * The width of the arc that rounds off the corners.
     */
    public double arcwidth;

    /**
     * The height of the arc that rounds off the corners.
     */
    public double archeight;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>RoundRectangle</code>, initialized to
     * location (0.0,&nbsp;0), size (0.0,&nbsp;0.0), and corner arcs
     * of radius 0.0.
     */
    public RoundRectangle() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a <code>RoundRectangle</code>
     * from the specified <code>double</code> coordinates.
     *
     * @param x the X coordinate of the newly
     *          constructed <code>RoundRectangle</code>
     * @param y the Y coordinate of the newly
     *          constructed <code>RoundRectangle</code>
     * @param w the width to which to set the newly
     *          constructed <code>RoundRectangle</code>
     * @param h the height to which to set the newly
     *          constructed <code>RoundRectangle</code>
     * @param arcw the width of the arc to use to round off the
     *             corners of the newly constructed
     *             <code>RoundRectangle</code>
     * @param arch the height of the arc to use to round off the
     *             corners of the newly constructed
     *             <code>RoundRectangle</code>
     */
    public RoundRectangle(double x, double y, double w, double h,
                  double arcw, double arch)
    {
        setRoundRect(x, y, w, h, arcw, arch);
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
    public int getX() {
        return (int)(x+.5);
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
    public int getY() {
        return (int)(y+.5);
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
    public int getWidth() {
        return (int)(width+.5);
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
    public int getHeight() {
        return (int)(height+.5);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the width of the arc that rounds off the corners.
     * @return the width of the arc that rounds off the corners
     * of this <code>RoundRectangle</code>.
     */
    public double getArcWidth() {
        return arcwidth;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the height of the arc that rounds off the corners.
     * @return the height of the arc that rounds off the corners
     * of this <code>RoundRectangle</code>.
     */
    public double getArcHeight() {
        return archeight;
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
    public boolean isEmpty() {
        return (width <= 0.0f) || (height <= 0.0f);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location, size, and corner radii of this
     * <code>RoundRectangle</code> to the specified
     * <code>double</code> values.
     *
     * @param x the X coordinate to which to set the
     *          location of this <code>RoundRectangle</code>
     * @param y the Y coordinate to which to set the
     *          location of this <code>RoundRectangle</code>
     * @param w the width to which to set this
     *          <code>RoundRectangle</code>
     * @param h the height to which to set this
     *          <code>RoundRectangle</code>
     * @param arcw the width to which to set the arc of this
     *                 <code>RoundRectangle</code>
     * @param arch the height to which to set the arc of this
     *                  <code>RoundRectangle</code>
     */
    public void setRoundRect(double x, double y, double w, double h,
                             double arcw, double arch)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.arcwidth = arcw;
        this.archeight = arch;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets this <code>RoundRectangle</code> to be the same as the
     * specified <code>RoundRectangle</code>.
     * @param rr the specified <code>RoundRectangle</code>
     */
    public void setRoundRect(RoundRectangle rr) {
        this.x = rr.getX();
        this.y = rr.getY();
        this.width = rr.getWidth();
        this.height = rr.getHeight();
        this.arcwidth = rr.getArcWidth();
        this.archeight = rr.getArcHeight();
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
        return new Rectangle(
                (int)(x+.5),
                (int)(y+.5),
                (int)(width+.5),
                (int)(height+.5));
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
    public void setFrame(int x, int y, int w, int h) {
	setRoundRect(x, y, w, h, getArcWidth(), getArcHeight());
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
    public boolean contains(int x, int y) {
	if (isEmpty()) {
	    return false;
	}
	double rrx0 = getX();
	double rry0 = getY();
	double rrx1 = rrx0 + getWidth();
	double rry1 = rry0 + getHeight();
	// Check for trivial rejection - point is outside bounding rectangle
	if (x < rrx0 || y < rry0 || x >= rrx1 || y >= rry1) {
	    return false;
	}
	double aw = Math.min(getWidth(), Math.abs(getArcWidth())) / 2.0;
	double ah = Math.min(getHeight(), Math.abs(getArcHeight())) / 2.0;
	// Check which corner point is in and do circular containment
	// test - otherwise simple acceptance
	if (x >= (rrx0 += aw) && x < (rrx0 = rrx1 - aw)) {
	    return true;
	}
	if (y >= (rry0 += ah) && y < (rry0 = rry1 - ah)) {
	    return true;
	}
        double xp = (x - rrx0) / aw;
	double yp = (y - rry0) / ah;
	return (xp * xp + yp * yp <= 1.0);
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
	if (isEmpty() || w <= 0 || h <= 0) {
	    return false;
	}
	double rrx0 = getX();
	double rry0 = getY();
	double rrx1 = rrx0 + getWidth();
	double rry1 = rry0 + getHeight();
	// Check for trivial rejection - bounding rectangles do not intersect
	if (x + w <= rrx0 || x >= rrx1 || y + h <= rry0 || y >= rry1) {
	    return false;
	}
	double aw = Math.min(getWidth(), Math.abs(getArcWidth())) / 2.0;
	double ah = Math.min(getHeight(), Math.abs(getArcHeight())) / 2.0;
	int x0class = classify(x, rrx0, rrx1, aw);
	int x1class = classify(x + w, rrx0, rrx1, aw);
	int y0class = classify(y, rry0, rry1, ah);
	int y1class = classify(y + h, rry0, rry1, ah);
	// Trivially accept if any point is inside inner rectangle
	if (x0class == 2 || x1class == 2 || y0class == 2 || y1class == 2) {
	    return true;
	}
	// Trivially accept if either edge spans inner rectangle
	if ((x0class < 2 && x1class > 2) || (y0class < 2 && y1class > 2)) {
	    return true;
	}
	// Since neither edge spans the center, then one of the corners
	// must be in one of the rounded edges.  We detect this case if
	// a [xy]0class is 3 or a [xy]1class is 1.  One of those two cases
	// must be true for each direction.
	// We now find a "nearest point" to test for being inside a rounded
	// corner.
        double xp=x;double yp=y;
	xp = (x1class == 1) ? (xp = xp + w - (rrx0 + aw)) : (xp = xp - (rrx1 - aw));
	yp = (y1class == 1) ? (yp = yp + h - (rry0 + ah)) : (yp = yp - (rry1 - ah));
	xp = xp / aw;
	yp = yp / ah;
	return (xp * xp + yp * yp <= 1.0);
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
    public boolean contains(int x, int y, int w, int h) {
	if (isEmpty() || w <= 0 || h <= 0) {
	    return false;
	}
	return (contains(x, y) &&
		contains(x + w, y) &&
		contains(x, y + h) &&
		contains(x + w, y + h));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an iteration object that defines the boundary of this
     * <code>RoundRectangle</code>.
     * The iterator for this class is multi-threaded safe, which means
     * that this <code>RoundRectangle</code> class guarantees that
     * modifications to the geometry of this <code>RoundRectangle</code>
     * object do not affect any iterations of that geometry that
     * are already in process.
     * @param at an optional <code>AffineTransform</code> to be applied to
     * the coordinates as they are returned in the iteration, or
     * <code>null</code> if untransformed coordinates are desired
     * @return    the <code>IPathIterator</code> object that returns the
     *          geometry of the outline of this
     *          <code>RoundRectangle</code>, one segment at a time.
     */
    public IPathIterator getPathIterator(AffineTransform at) {
	return new RoundRectIterator(this, at);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>RoundRectangle</code>.
     * @return the hashcode for this <code>RoundRectangle</code>.
     */
    public int hashCode() {
        long bits = Double.doubleToLongBits(getX());
        bits += Double.doubleToLongBits(getY()) * 37;
        bits += Double.doubleToLongBits(getWidth()) * 43;
        bits += Double.doubleToLongBits(getHeight()) * 47;
        bits += Double.doubleToLongBits(getArcWidth()) * 53;
        bits += Double.doubleToLongBits(getArcHeight()) * 59;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the specified <code>Object</code> is
     * equal to this <code>RoundRectangle</code>.  The specified
     * <code>Object</code> is equal to this <code>RoundRectangle</code>
     * if it is an instance of <code>RoundRectangle</code> and if its
     * location, size, and corner arc dimensions are the same as this
     * <code>RoundRectangle</code>.
     * @param obj  an <code>Object</code> to be compared with this
     *             <code>RoundRectangle</code>.
     * @return  <code>true</code> if <code>obj</code> is an instance
     *          of <code>RoundRectangle</code> and has the same values;
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RoundRectangle) {
            RoundRectangle rr2d = (RoundRectangle) obj;
            return ((getX() == rr2d.getX()) &&
                    (getY() == rr2d.getY()) &&
                    (getWidth() == rr2d.getWidth()) &&
                    (getHeight() == rr2d.getHeight()) &&
                    (getArcWidth() == rr2d.getArcWidth()) &&
                    (getArcHeight() == rr2d.getArcHeight()));
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    private int classify(double coord, double left, double right,
                         double arcsize)
    {
	if (coord < left) {
	    return 0;
	} else if (coord < left + arcsize) {
	    return 1;
	} else if (coord < right - arcsize) {
	    return 2;
	} else if (coord < right) {
	    return 3;
	} else {
	    return 4;
	}
    }
}