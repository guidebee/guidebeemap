//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.geometry;

//--------------------------------- IMPORTS ------------------------------------
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * GeoBounds is a rectangular area of the map in pixel coordinates
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public class GeoBounds {

    /**
     * The bitmask that indicates that a point lies to the left of
     * this <code>GeoBounds</code>.
     */
    public static final int OUT_LEFT = 1;
    /**
     * The bitmask that indicates that a point lies above
     * this <code>GeoBounds</code>.
     */
    public static final int OUT_TOP = 2;
    /**
     * The bitmask that indicates that a point lies to the right of
     * this <code>GeoBounds</code>.
     */
    public static final int OUT_RIGHT = 4;
    /**
     * The bitmask that indicates that a point lies below
     * this <code>GeoBounds</code>.
     */
    public static final int OUT_BOTTOM = 8;
    /**
     * The X coordinate of this <code>GeoBounds</code>.
     */
    public double x;
    /**
     * The Y coordinate of this <code>GeoBounds</code>.
     */
    public double y;
    /**
     * The width of this <code>GeoBounds</code>.
     */
    public double width;
    /**
     * The height of this <code>GeoBounds</code>.
     */
    public double height;
    /**
     * The x coordinate of the left edge of the rectangle.
     */
    public double minX;
    /**
     * The y coordinate of the top edge of the rectangle.
     */
    public double minY;
    /**
     * The x coordinate of the right edge of the rectangle.
     */
    public double maxX;
    /**
     * The y coordinate of the bottom edge of the rectangle.
     */
    public double maxY;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code> whose upper-left corner 
     * is at (0,&nbsp;0) in the coordinate space, and whose width and 
     * height are both zero. 
     */
    public GeoBounds() {
        this(0, 0, 0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the top left x coordinate.
     * @return the top left x coordinate.
     */
    public double getX() {
        return x;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the top left y coordinate.
     * @return the top left y coordinate.
     */
    public double getY() {
        return y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the width of the geo bound.
     * @return the width of the geo bound.
     */
    public double getWidth() {
        return width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the height of the geo bound.
     * @return the height of the geo bound.
     */
    public double getHeight() {
        return height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if the geo bound is a empty rectangle.
     * @return true,it's empty.
     */
    public boolean isEmpty() {
        return (width <= 0.0) || (height <= 0.0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Reset the geo bound with new position and size.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param w the width
     * @param h the height.
     */
    public void setRect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Reset the geo bound with same position and size with given geo bound.
     * @param r the geo bound to copy from.
     */
    public void setRect(GeoBounds r) {
        this.x = r.getX();
        this.y = r.getY();
        this.width = r.getWidth();
        this.height = r.getHeight();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check give (x,y)'s relative postion to this geo bound
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return the relative position of the point.
     */
    public int outcode(double x, double y) {
        int out = 0;
        if (this.width <= 0) {
            out |= OUT_LEFT | OUT_RIGHT;
        } else if (x < this.x) {
            out |= OUT_LEFT;
        } else if (x > this.x + this.width) {
            out |= OUT_RIGHT;
        }
        if (this.height <= 0) {
            out |= OUT_TOP | OUT_BOTTOM;
        } else if (y < this.y) {
            out |= OUT_TOP;
        } else if (y > this.y + this.height) {
            out |= OUT_BOTTOM;
        }
        return out;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create the intersection rectangle between this rectangle and r rectangle.
     * @param r the other rectangle
     * @return the intersection rectangle.
     */
    public GeoBounds createIntersection(GeoBounds r) {
        GeoBounds dest = new GeoBounds();
        GeoBounds.intersect(this, r, dest);
        return dest;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create the union rectangle of the two rectangles.
     * @param r the other rectangle.
     * @return union rectangle of the two rectangles.
     */
    public GeoBounds createUnion(GeoBounds r) {
        GeoBounds dest = new GeoBounds();
        GeoBounds.union(this, r, dest);
        return dest;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a rectangle that contains all the given points.
     * @param points an array of points.
     */
    public GeoBounds(GeoPoint[] points) {

        this();
        if (points == null) {
            this.setRect(0, 0, 0, 0);
        }
        int count = points.length;
        switch (count) {
            case 0:
                this.setRect(0, 0, 0, 0);
                break;
            case 1:
                this.setRect(points[0].x, points[0].y, 0, 0);
                break;
            case 2:
                 {
                    double x1 = Math.min(points[0].x, points[1].x);
                    double x2 = Math.max(points[0].x, points[1].x);
                    double y1 = Math.min(points[0].y, points[1].y);
                    double y2 = Math.max(points[0].y, points[1].y);
                    this.setRect(x1, y1, x2 - x1, y2 - y1);
                }
                break;
            default:
                 {
                    double x1 = Math.min(points[0].x, points[1].x);
                    double x2 = Math.max(points[0].x, points[1].x);
                    double y1 = Math.min(points[0].y, points[1].y);
                    double y2 = Math.max(points[0].y, points[1].y);
                    this.setRect(x1, y1, x2 - x1, y2 - y1);
                }
                for (int i = 2; i < count; i++) {
                    this.add(points[i].x, points[i].y);
                }
                break;
        }
        this.minX = this.x;
        this.minY = this.y;
        this.maxX = this.x + this.width;
        this.maxY = this.y + this.height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code>, initialized to match 
     * the values of the specified <code>GeoBounds</code>.
     * @param r  the <code>GeoBounds</code> from which to copy initial values
     *           to a newly constructed <code>GeoBounds</code>
     */
    public GeoBounds(GeoBounds r) {
        this(r.x, r.y, r.width, r.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code> whose upper-left corner is 
     * specified as
     * {@code (x,y)} and whose width and height 
     * are specified by the arguments of the same name. 
     * @param     x the specified X coordinate
     * @param     y the specified Y coordinate
     * @param     width    the width of the <code>GeoBounds</code>
     * @param     height   the height of the <code>GeoBounds</code>
     */
    public GeoBounds(double x, double y, double width, double height) {
        setRect(x, y, width, height);
        this.minX = this.x;
        this.minY = this.y;
        this.maxX = this.x + this.width;
        this.maxY = this.y + this.height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code> whose upper-left corner 
     * is at (0,&nbsp;0) in the coordinate space, and whose width and 
     * height are specified by the arguments of the same name. 
     * @param width the width of the <code>GeoBounds</code>
     * @param height the height of the <code>GeoBounds</code>
     */
    public GeoBounds(double width, double height) {
        this(0, 0, width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code> whose upper-left corner is 
     * specified by the {@link GeoPoint} argument, and
     * whose width and height are specified by the 
     * {@link GeoSize} argument. 
     * @param p a <code>GeoPoint</code> that is the upper-left corner of 
     * the <code>GeoBounds</code>
     * @param size a <code>GeoSize</code>, representing the 
     * width and height of the <code>GeoBounds</code>
     */
    public GeoBounds(GeoPoint p, GeoSize size) {
        this(p.x, p.y, size.width, size.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code> whose upper-left corner is the  
     * specified <code>GeoPoint</code>, and whose width and height are both zero. 
     * @param p a <code>GeoPoint</code> that is the top left corner 
     * of the <code>GeoBounds</code>
     */
    public GeoBounds(GeoPoint p) {
        this(p.x, p.y, 0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>GeoBounds</code> whose top left corner is  
     * (0,&nbsp;0) and whose width and height are specified  
     * by the <code>GeoSize</code> argument. 
     * @param size a <code>GeoSize</code>, specifying width and height
     */
    public GeoBounds(GeoSize size) {
        this(0, 0, size.width, size.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the pixel coordinates of the center of the rectangular area.
     * @return the center point of the GeoBounds.
     */
    public GeoPoint mid() {
        GeoPoint point = new GeoPoint((minX + maxX) / 2, (minY + maxY) / 2);
        return point;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the pixel coordinates of the upper left corner of the rectangular
     *  area.
     * @return the  upper left corner of the rectangular area.
     */
    public GeoPoint min() {
        GeoPoint point = new GeoPoint(minX, minY);
        return point;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the pixel coordinates of the lower right corner of the 
     * rectangular area.
     * @return the  upper lower right of the rectangular area.
     */
    public GeoPoint max() {
        GeoPoint point = new GeoPoint(maxX, maxY);
        return point;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified line segment intersects the interior of this
     * <code>GeoBounds</code>.
     *
     * @param x1 the X coordinate of the start point of the specified
     *           line segment
     * @param y1 the Y coordinate of the start point of the specified
     *           line segment
     * @param x2 the X coordinate of the end point of the specified
     *           line segment
     * @param y2 the Y coordinate of the end point of the specified
     *           line segment
     * @return <code>true</code> if the specified line segment intersects
     * the interior of this <code>GeoBounds</code>; <code>false</code>
     * otherwise.
     */
    public boolean intersectsLine(double x1, double y1, double x2, double y2) {
        int out1, out2;
        if ((out2 = outcode(x2, y2)) == 0) {
            return true;
        }
        while ((out1 = outcode(x1, y1)) != 0) {
            if ((out1 & out2) != 0) {
                return false;
            }
            if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
                double tempX = getX();
                if ((out1 & OUT_RIGHT) != 0) {
                    tempX += getWidth();
                }
                y1 = y1 + (tempX - x1) * (y2 - y1) / (x2 - x1);
                x1 = tempX;
            } else {
                double tempY = getY();
                if ((out1 & OUT_BOTTOM) != 0) {
                    tempY += getHeight();
                }
                x1 = x1 + (tempY - y1) * (x2 - x1) / (y2 - y1);
                y1 = tempY;
            }
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines where the specified {@link GeoPoint} lies with
     * respect to this <code>GeoBounds</code>.
     * This method computes a binary OR of the appropriate mask values
     * indicating, for each side of this <code>GeoBounds</code>,
     * whether or not the specified <code>GeoPoint</code> is on the same
     * side of the edge as the rest of this <code>GeoBounds</code>.
     * @param p the specified <code>GeoPoint</code>
     * @return the logical OR of all appropriate out codes.
     */
    public int outcode(GeoPoint p) {
        return outcode(p.getX(), p.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location and size of the outer bounds of this
     * <code>GeoBounds</code> to the specified rectangular values.
     *
     * @param x the X coordinate of the upper-left corner
     *          of this <code>GeoBounds</code>
     * @param y the Y coordinate of the upper-left corner
     *          of this <code>GeoBounds</code>
     * @param w the width of this <code>GeoBounds</code>
     * @param h the height of this <code>GeoBounds</code>
     */
    public void setFrame(double x, double y, double w, double h) {
        setRect(x, y, w, h);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this rectangle contains given point.
     * @param x the x coordinate of the given point.
     * @param y the y coordinate of the given point.
     * @return true if this rectangle contains given point.
     */
    public boolean contains(double x, double y) {
        double x0 = getX();
        double y0 = getY();
        return (x >= x0 &&
                y >= y0 &&
                x < x0 + getWidth() &&
                y < y0 + getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this rectangle intersects with given rectangle.
     * @param x the x coordinate of the other rectangle.
     * @param y the y coordinate of the other rectangle.
     * @param w the width of the other rectangle.
     * @param h the height of the other rectangle.
     * @return true, if they intersect.
     */
    public boolean intersects(double x, double y, double w, double h) {
        if (isEmpty() || w <= 0 || h <= 0) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x + w > x0 &&
                y + h > y0 &&
                x < x0 + getWidth() &&
                y < y0 + getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this rectangle contains given rectangle.
     * @param x the x coordinate of the other rectangle.
     * @param y the y coordinate of the other rectangle.
     * @param w the width of the other rectangle.
     * @param h the height of the other rectangle.
     * @return true, if it totally contains other rectangle.
     */
    public boolean contains(double x, double y, double w, double h) {
        if (isEmpty() || w <= 0 || h <= 0) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x >= x0 &&
                y >= y0 &&
                (x + w) <= x0 + getWidth() &&
                (y + h) <= y0 + getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Intersects the pair of specified source <code>GeoBounds</code>
     * objects and puts the result into the specified destination
     * <code>GeoBounds</code> object.  One of the source rectangles
     * can also be the destination to avoid creating a third GeoBounds
     * object, but in this case the original points of this source
     * rectangle will be overwritten by this method.
     * @param src1 the first of a pair of <code>GeoBounds</code>
     * objects to be intersected with each other
     * @param src2 the second of a pair of <code>GeoBounds</code>
     * objects to be intersected with each other
     * @param dest the <code>GeoBounds</code> that holds the
     * results of the intersection of <code>src1</code> and
     * <code>src2</code>
     */
    public static void intersect(GeoBounds src1,
            GeoBounds src2,
            GeoBounds dest) {
        double x1 = Math.max(src1.getMinX(), src2.getMinX());
        double y1 = Math.max(src1.getMinY(), src2.getMinY());
        double x2 = Math.min(src1.getMaxX(), src2.getMaxX());
        double y2 = Math.min(src1.getMaxY(), src2.getMaxY());
        dest.setFrame(x1, y1, x2 - x1, y2 - y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Unions the pair of source <code>GeoBounds</code> objects
     * and puts the result into the specified destination
     * <code>GeoBounds</code> object.  One of the source rectangles
     * can also be the destination to avoid creating a third GeoBounds
     * object, but in this case the original points of this source
     * rectangle will be overwritten by this method.
     * @param src1 the first of a pair of <code>GeoBounds</code>
     * objects to be combined with each other
     * @param src2 the second of a pair of <code>GeoBounds</code>
     * objects to be combined with each other
     * @param dest the <code>GeoBounds</code> that holds the
     * results of the union of <code>src1</code> and
     * <code>src2</code>
     */
    public static void union(GeoBounds src1,
            GeoBounds src2,
            GeoBounds dest) {
        double x1 = Math.min(src1.getMinX(), src2.getMinX());
        double y1 = Math.min(src1.getMinY(), src2.getMinY());
        double x2 = Math.max(src1.getMaxX(), src2.getMaxX());
        double y2 = Math.max(src1.getMaxY(), src2.getMaxY());
        dest.setFrameFromDiagonal(x1, y1, x2, y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the smallest X coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>double</code>
     * precision.
     * @return the smallest X coordinate of the framing
     * 		rectangle of the <code>IShape</code>.
     */
    public double getMinX() {
        return getX();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the smallest Y coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>double</code>
     * precision.
     * @return the smallest Y coordinate of the framing
     * 		rectangle of the <code>IShape</code>.
     */
    public double getMinY() {
        return getY();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the largest X coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>double</code>
     * precision.
     * @return the largest X coordinate of the framing
     * 		rectangle of the <code>IShape</code>.
     */
    public double getMaxX() {
        return getX() + getWidth();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the largest Y coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>double</code>
     * precision.
     * @return the largest Y coordinate of the framing
     *		rectangle of the <code>IShape</code>.
     */
    public double getMaxY() {
        return getY() + getHeight();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the X coordinate of the center of the framing
     * rectangle of the <code>IShape</code> in <code>double</code>
     * precision.
     * @return the X coordinate of the center of the framing rectangle
     * 		of the <code>IShape</code>.
     */
    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the Y coordinate of the center of the framing
     * rectangle of the <code>IShape</code> in <code>double</code>
     * precision.
     * @return the Y coordinate of the center of the framing rectangle
     * 		of the <code>IShape</code>.
     */
    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the framing {@link GeoBounds}
     * that defines the overall shape of this object.
     * @return a <code>GeoBounds</code>, specified in
     * <code>double</code> coordinates.
     */
    public GeoBounds getFrame() {
        return new GeoBounds(getX(), getY(), getWidth(), getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location and size of the framing rectangle of this
     * <code>IShape</code> to the specified {@link GeoPoint} and
     * {@link GeoSize}, respectively.  The framing rectangle is used
     * by the subclasses of <code>RectangularShape</code> to define
     * their geometry.
     * @param loc the specified <code>GeoPoint</code>
     * @param size the specified <code>GeoSize</code>
     */
    public void setFrame(GeoPoint loc, GeoSize size) {
        setFrame(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the framing rectangle of this <code>IShape</code> to
     * be the specified <code>GeoBounds</code>.  The framing rectangle is
     * used by the subclasses of <code>RectangularShape</code> to define
     * their geometry.
     * @param r the specified <code>GeoBounds</code>
     */
    public void setFrame(GeoBounds r) {
        setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the diagonal of the framing rectangle of this <code>IShape</code>
     * based on the two specified coordinates.  The framing rectangle is
     * used by the subclasses of <code>RectangularShape</code> to define
     * their geometry.
     *
     * @param x1 the X coordinate of the start point of the specified diagonal
     * @param y1 the Y coordinate of the start point of the specified diagonal
     * @param x2 the X coordinate of the end point of the specified diagonal
     * @param y2 the Y coordinate of the end point of the specified diagonal
     */
    public void setFrameFromDiagonal(double x1, double y1,
            double x2, double y2) {
        if (x2 < x1) {
            double t = x1;
            x1 = x2;
            x2 = t;
        }
        if (y2 < y1) {
            double t = y1;
            y1 = y2;
            y2 = t;
        }
        setFrame(x1, y1, x2 - x1, y2 - y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the diagonal of the framing rectangle of this <code>IShape</code>
     * based on two specified <code>GeoPoint</code> objects.  The framing
     * rectangle is used by the subclasses of <code>RectangularShape</code>
     * to define their geometry.
     *
     * @param p1 the start <code>GeoPoint</code> of the specified diagonal
     * @param p2 the end <code>GeoPoint</code> of the specified diagonal
     */
    public void setFrameFromDiagonal(GeoPoint p1, GeoPoint p2) {
        setFrameFromDiagonal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the framing rectangle of this <code>IShape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     */
    public void setFrameFromCenter(double centerX, double centerY,
            double cornerX, double cornerY) {
        double halfW = Math.abs(cornerX - centerX);
        double halfH = Math.abs(cornerY - centerY);
        setFrame(centerX - halfW, centerY - halfH, halfW * 2.0, halfH * 2.0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the framing rectangle of this <code>IShape</code> based on a
     * specified center <code>GeoPoint</code> and corner
     * <code>GeoPoint</code>.  The framing rectangle is used by the subclasses
     * of <code>RectangularShape</code> to define their geometry.
     * @param center the specified center <code>GeoPoint</code>
     * @param corner the specified corner <code>GeoPoint</code>
     */
    public void setFrameFromCenter(GeoPoint center, GeoPoint corner) {
        setFrameFromCenter(center.getX(), center.getY(),
                corner.getX(), corner.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this rectangle contains given point.
     * @param p the point to be checked
     * @return true,it contains given point.
     */
    public boolean contains(GeoPoint p) {
        return contains(p.getX(), p.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this rectangle intersects given rectangle.
     * @param r the rectangle to be checked.
     * @return true, it intersects given rectangle.
     */
    public boolean intersects(GeoBounds r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this rectangle contains given rectangle.
     * @param r the rectangle to be checked.
     * @return true, it totally contains given rectangle.
     */
    public boolean contains(GeoBounds r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return a new geo bounds of this rectangle.
     * @return a new copy of this geo bound.
     */
    public GeoBounds getBounds() {
        double tempWidth = getWidth();
        double tempHeight = getHeight();
        if (tempWidth < 0 || tempHeight < 0) {
            return new GeoBounds();
        }
        double tempX = getX();
        double tempY = getY();
        double x1 = Math.floor(tempX);
        double y1 = Math.floor(tempY);
        double x2 = Math.ceil(tempX + tempWidth);
        double y2 = Math.ceil(tempY + tempHeight);
        return new GeoBounds((int) x1, (int) y1,
                (int) (x2 - x1), (int) (y2 - y1));
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Adds a point, specified by the double precision arguments
     * <code>newx</code> and <code>newy</code>, to this
     * <code>GeoBounds</code>.  The resulting <code>GeoBounds</code>
     * is the smallest <code>GeoBounds</code> that
     * contains both the original <code>GeoBounds</code> and the
     * specified point.
     * <p>
     * After adding a point, a call to <code>contains</code> with the
     * added point as an argument does not necessarily return
     * <code>true</code>. The <code>contains</code> method does not
     * return <code>true</code> for points on the right or bottom
     * edges of a rectangle. Therefore, if the added point falls on
     * the left or bottom edge of the enlarged rectangle,
     * <code>contains</code> returns <code>false</code> for that point.
     * @param newx the X coordinate of the new point
     * @param newy the Y coordinate of the new point
     */
    public void add(double newx, double newy) {
        double x1 = Math.min(getMinX(), newx);
        double x2 = Math.max(getMaxX(), newx);
        double y1 = Math.min(getMinY(), newy);
        double y2 = Math.max(getMaxY(), newy);
        setRect(x1, y1, x2 - x1, y2 - y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds the <code>GeoPoint</code> object <code>pt</code> to this
     * <code>GeoBounds</code>.
     * The resulting <code>GeoBounds</code> is the smallest
     * <code>GeoBounds</code> that contains both the original
     * <code>GeoBounds</code> and the specified <code>GeoPoint</code>.
     * <p>
     * After adding a point, a call to <code>contains</code> with the
     * added point as an argument does not necessarily return
     * <code>true</code>. The <code>contains</code>
     * method does not return <code>true</code> for points on the right
     * or bottom edges of a rectangle. Therefore, if the added point falls
     * on the left or bottom edge of the enlarged rectangle,
     * <code>contains</code> returns <code>false</code> for that point.
     * @param     pt the new <code>GeoPoint</code> to add to this
     * <code>GeoBounds</code>.
     */
    public void add(GeoPoint pt) {
        add(pt.getX(), pt.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a <code>GeoBounds</code> object to this
     * <code>GeoBounds</code>.  The resulting <code>GeoBounds</code>
     * is the union of the two <code>GeoBounds</code> objects.
     * @param r the <code>GeoBounds</code> to add to this
     * <code>GeoBounds</code>.
     */
    public void add(GeoBounds r) {
        double x1 = Math.min(getMinX(), r.getMinX());
        double x2 = Math.max(getMaxX(), r.getMaxX());
        double y1 = Math.min(getMinY(), r.getMinY());
        double y2 = Math.max(getMaxY(), r.getMaxY());
        setRect(x1, y1, x2 - x1, y2 - y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>GeoBounds</code>.
     * @return the hashcode for this <code>GeoBounds</code>.
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
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the specified <code>Object</code> is
     * equal to this <code>GeoBounds</code>.  The specified
     * <code>Object</code> is equal to this <code>GeoBounds</code>
     * if it is an instance of <code>GeoBounds</code> and if its
     * location and size are the same as this <code>GeoBounds</code>.
     * @param obj an <code>Object</code> to be compared with this
     * <code>GeoBounds</code>.
     * @return     <code>true</code> if <code>obj</code> is an instance
     *                     of <code>GeoBounds</code> and has
     *                     the same values; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof GeoBounds) {
            GeoBounds r2d = (GeoBounds) obj;
            return ((getX() == r2d.getX()) &&
                    (getY() == r2d.getY()) &&
                    (getWidth() == r2d.getWidth()) &&
                    (getHeight() == r2d.getHeight()));
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the bounding <code>GeoSize</code> of this <code>GeoSize</code>
     * to match the specified <code>GeoSize</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     * @param r the specified <code>GeoSize</code>
     */
    public void setBounds(GeoBounds r) {
        setBounds(r.x, r.y, r.width, r.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the bounding <code>GeoSize</code> of this
     * <code>GeoSize</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     * @param x the new X coordinate for the upper-left
     *                    corner of this <code>GeoSize</code>
     * @param y the new Y coordinate for the upper-left
     *                    corner of this <code>GeoSize</code>
     * @param width the new width for this <code>GeoSize</code>
     * @param height the new height for this <code>GeoSize</code>
     */
    public void setBounds(double x, double y, double width, double height) {
        reshape(x, y, width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the bounding <code>GeoSize</code> of this
     * <code>GeoSize</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p>
     * @param x the new X coordinate for the upper-left
     *                    corner of this <code>GeoSize</code>
     * @param y the new Y coordinate for the upper-left
     *                    corner of this <code>GeoSize</code>
     * @param width the new width for this <code>GeoSize</code>
     * @param height the new height for this <code>GeoSize</code>
     */
    private void reshape(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check whether the current rectangle entirely contains other rectangle.
     * @param other the other rectangle.
     * @return true if the passed rectangular area is entirely contained 
     * in this rectangular area.
     */
    public boolean containsBounds(GeoBounds other) {
        this.setBounds(minX, minY, maxX - minX, maxY - minY);
        return this.contains(other.minX, other.minY,
                other.maxX - other.minX,
                other.maxY - other.minY);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check whether the current rectangle contains given point.
     * @param point the point object.
     * @return true if the rectangular area (inclusively) contains the pixel 
     * coordinates. 
     */
    public boolean containsPoint(GeoPoint point) {
        this.setBounds(minX, minY, maxX - minX, maxY - minY);
        return this.contains(point.x, point.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Enlarges this box so that the point is also contained in this box.
     * @param point the point object.
     */
    public void extend(GeoPoint point) {
        this.setBounds(minX, minY, maxX - minX, maxY - minY);
        this.add(point.x, point.y);
        this.minX = this.x;
        this.minY = this.y;
        this.maxX = this.x + this.width;
        this.maxY = this.y + this.height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a <code>String</code> representing this 
     * <code>GBounds</code> and its values.
     * @return Returns a string that contains the coordinates of the upper left 
     * and the lower right corner points of the box, in this order, separated 
     * by comma, surrounded by parentheses.
     */
    public String toString() {
        return minX + "," + minY + "," + maxX + "," + maxY;
    }
}
