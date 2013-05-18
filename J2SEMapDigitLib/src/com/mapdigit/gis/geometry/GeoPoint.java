//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.geometry;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.util.MathEx;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A point representing a location in {@code (x,y)} coordinate space,
 * specified in integer precision.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 02/01/09
 * @author      Guidebee Pty Ltd.
 */
public class GeoPoint {

    /**
     * The X coordinate of this <code>GeoPoint</code>.
     */
    public double x;
    /**
     * The Y coordinate of this <code>GeoPoint</code>.
     */
    public double y;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the origin 
     * (0,&nbsp;0) of the coordinate space. 
     */
    public GeoPoint() {
        super();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point with the same location as
     * the specified <code>Point</code> object.
     * @param       p a point
     */
    public GeoPoint(GeoPoint p) {
        this(p.x, p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the specified 
     * {@code (x,y)} location in the coordinate space. 
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     */
    public GeoPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string representation of this point and its location 
     * in the {@code (x,y)} coordinate space. This method is 
     * intended to be used only for debugging purposes, and the content 
     * and format of the returned string may vary between implementations. 
     * The returned string may be empty but may not be <code>null</code>.
     * 
     * @return  Returns a string that contains the x and y coordinates, 
     * in this order, separated by a comma.
     */
    public String toString() {
        return x + "," + y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the x coordinate.
     * @return the x coordinate(longitude)
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
     *  return the y coordinate.
     * @return the y coordinate(longitude)
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
     * Sets the location of this <code>GeoPoint</code> to the same
     * coordinates as the specified <code>GeoPoint</code> object.
     * @param p the specified <code>GeoPoint</code> to which to set
     * this <code>GeoPoint</code>
     */
    public void setLocation(GeoPoint p) {
        setLocation(p.getX(), p.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the square of the distance between the two
     * sets of specified coordinates.
     */
    public static double distanceSq(double x1, double y1,
            double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return (x1 * x1 + y1 * y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the distance between the two sets of specified
     * coordinates.
     */
    public static double distance(double x1, double y1,
            double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return MathEx.sqrt(x1 * x1 + y1 * y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from this
     * <code>GeoPoint</code> to a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this <code>GeoPoint</code>
     * @param py the Y coordinate of the specified point to be measured
     *           against this <code>GeoPoint</code>
     * @return the square of the distance between this
     * <code>GeoPoint</code> and the specified point.
     */
    public double distanceSq(double px, double py) {
        px -= getX();
        py -= getY();
        return (px * px + py * py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from this
     * <code>GeoPoint</code> to a specified <code>GeoPoint</code>.
     *
     * @param pt the specified point to be measured
     *           against this <code>GeoPoint</code>
     * @return the square of the distance between this
     * <code>GeoPoint</code> to a specified <code>GeoPoint</code>.
     */
    public double distanceSq(GeoPoint pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        return (px * px + py * py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from this <code>GeoPoint</code> to
     * a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this <code>GeoPoint</code>
     * @param py the Y coordinate of the specified point to be measured
     *           against this <code>GeoPoint</code>
     * @return the distance between this <code>GeoPoint</code>
     * and a specified point.
     */
    public double distance(double px, double py) {
        px -= getX();
        py -= getY();
        return MathEx.sqrt(px * px + py * py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from this <code>GeoPoint</code> to a
     * specified <code>GeoPoint</code>.
     *
     * @param pt the specified point to be measured
     *           against this <code>GeoPoint</code>
     * @return the distance between this <code>GeoPoint</code> and
     * the specified <code>GeoPoint</code>.
     */
    public double distance(GeoPoint pt) {
        double px = pt.getX() - this.getX();
        double py = pt.getY() - this.getY();
        return MathEx.sqrt(px * px + py * py);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>GeoPoint</code>.
     * @return      a hash code for this <code>GeoPoint</code>.
     */
    public int hashCode() {
        long bits = java.lang.Double.doubleToLongBits(getX());
        bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not two points are equal. Two instances of
     * <code>GeoPoint</code> are equal if the values of their
     * <code>x</code> and <code>y</code> member fields, representing
     * their position in the coordinate space, are the same.
     * @param obj an object to be compared with this <code>GeoPoint</code>
     * @return <code>true</code> if the object to be compared is
     *         an instance of <code>GeoPoint</code> and has
     *         the same values; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof GeoPoint) {
            GeoPoint p2d = (GeoPoint) obj;
            return (getX() == p2d.getX()) && (getY() == p2d.getY());
        }
        return super.equals(obj);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the new location.
     * @param x new x coordinate.
     * @param y new y coordinate.
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
