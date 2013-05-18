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
 * The <code>Ellipse</code> class defines an ellipse specified
 * in <code>Double</code> precision.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 09/05/10
 * @author      Guidebee Pty Ltd.
 */
public class Ellipse extends RectangularShape {

    /**
     * The X coordinate of the upper-left corner of the
     * framing rectangle of this {@code Ellipse}.
     */
    public double x;

    /**
     * The Y coordinate of the upper-left corner of the
     * framing rectangle of this {@code Ellipse}.
     */
    public double y;

    /**
     * The overall width of this <code>Ellipse</code>.
     */
    public double width;

    /**
     * The overall height of the <code>Ellipse</code>.
     */
    public double height;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>Ellipse</code>, initialized to
     * location (0,&nbsp;0) and size (0,&nbsp;0).
     */
    public Ellipse() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes an <code>Ellipse</code> from the
     * specified coordinates.
     *
     * @param x the X coordinate of the upper-left corner
     *        of the framing rectangle
     * @param y the Y coordinate of the upper-left corner
     *        of the framing rectangle
     * @param w the width of the framing rectangle
     * @param h the height of the framing rectangle
     */
    public Ellipse(double x, double y, double w, double h) {
        setFrame((int)(x+.5),
                (int)(y+.5), (int)(w+.5),
                (int)(h+.5));
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
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return (width <= 0.0 || height <= 0.0);
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
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
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
    public boolean contains(int x, int y) {
	// Normalize the coordinates compared to the ellipse
	// having a center at 0,0 and a radius of 0.5.
	double ellw = getWidth();
	if (ellw <= 0.0) {
	    return false;
	}
	double normx = (x - getX()) / ellw - 0.5;
	double ellh = getHeight();
	if (ellh <= 0.0) {
	    return false;
	}
	double normy = (y - getY()) / ellh - 0.5;
	return (normx * normx + normy * normy) < 0.25;
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
	if (w <= 0.0 || h <= 0.0) {
	    return false;
	}
	// Normalize the rectangular coordinates compared to the ellipse
	// having a center at 0,0 and a radius of 0.5.
	double ellw = getWidth();
	if (ellw <= 0.0) {
	    return false;
	}
	double normx0 = (x - getX()) / ellw - 0.5;
	double normx1 = normx0 + w / ellw;
	double ellh = getHeight();
	if (ellh <= 0.0) {
	    return false;
	}
	double normy0 = (y - getY()) / ellh - 0.5;
	double normy1 = normy0 + h / ellh;
	// find nearest x (left edge, right edge, 0.0)
	// find nearest y (top edge, bottom edge, 0.0)
	// if nearest x,y is inside circle of radius 0.5, then intersects
	double nearx, neary;
	if (normx0 > 0.0) {
	    // center to left of X extents
	    nearx = normx0;
	} else if (normx1 < 0.0) {
	    // center to right of X extents
	    nearx = normx1;
	} else {
	    nearx = 0.0;
	}
	if (normy0 > 0.0) {
	    // center above Y extents
	    neary = normy0;
	} else if (normy1 < 0.0) {
	    // center below Y extents
	    neary = normy1;
	} else {
	    neary = 0.0;
	}
	return (nearx * nearx + neary * neary) < 0.25;
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
     * <code>Ellipse</code>.
     * The iterator for this class is multi-threaded safe, which means
     * that this <code>Ellipse</code> class guarantees that
     * modifications to the geometry of this <code>Ellipse</code>
     * object do not affect any iterations of that geometry that
     * are already in process.
     * @param at an optional <code>AffineTransform</code> to be applied to
     * the coordinates as they are returned in the iteration, or
     * <code>null</code> if untransformed coordinates are desired
     * @return    the <code>IPathIterator</code> object that returns the
     *          geometry of the outline of this <code>Ellipse</code>,
     *		one segment at a time.
     */
    public IPathIterator getPathIterator(AffineTransform at) {
	return new EllipseIterator(this, at);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>Ellipse</code>.
     * @return the hashcode for this <code>Ellipse</code>.
     */
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(getX());
        bits += java.lang.Double.doubleToLongBits(getY()) * 37;
        bits += java.lang.Double.doubleToLongBits(getWidth()) * 43;
        bits += java.lang.Double.doubleToLongBits(getHeight()) * 47;
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
     * equal to this <code>Ellipse</code>.  The specified
     * <code>Object</code> is equal to this <code>Ellipse</code>
     * if it is an instance of <code>Ellipse</code> and if its
     * location and size are the same as this <code>Ellipse</code>.
     * @param obj  an <code>Object</code> to be compared with this
     *             <code>Ellipse</code>.
     * @return  <code>true</code> if <code>obj</code> is an instance
     *          of <code>Ellipse</code> and has the same values;
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Ellipse) {
            Ellipse e2d = (Ellipse) obj;
            return ((getX() == e2d.getX()) &&
                    (getY() == e2d.getY()) &&
                    (getWidth() == e2d.getWidth()) &&
                    (getHeight() == e2d.getHeight()));
        }
        return false;
    }
}