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
import com.mapdigit.gis.MapLayer;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * GeoLatLngBounds is a bound in geographical coordinates longitude and latitude.
 * Note: the positive of North is from top to bottom instead of from bottom to 
 * top internally.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public class GeoLatLngBounds extends GeoBounds {

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
    public GeoLatLngBounds() {
        this(0, 0, 0, 0);
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
    public GeoLatLngBounds(GeoLatLngBounds r) {
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
    public GeoLatLngBounds(double x, double y, double width, double height) {
        super(x, y, width, height);
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
    public GeoLatLngBounds(int width, int height) {
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
     * specified by the GeoPoint argument, and
     * whose width and height are specified by the 
     * {@link GeoSize} argument.
     * @param p a <code>GeoPoint</code> that is the upper-left corner of 
     * the <code>GeoBounds</code>
     * @param size a <code>GeoSize</code>, representing the 
     * width and height of the <code>GeoBounds</code>
     */
    public GeoLatLngBounds(GeoPoint p, GeoSize size) {
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
    public GeoLatLngBounds(GeoPoint p) {
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
    public GeoLatLngBounds(GeoSize size) {
        this(0, 0, size.width, size.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a rectangle from the points at its south-west and north-east 
     * corners.
     * @param sw  south-west point of the rectangle.
     * @param ne  north-east point of the rectangle.
     */
    public GeoLatLngBounds(GeoLatLng sw, GeoLatLng ne) {
        this(sw.x, sw.y, ne.x - sw.x, ne.y - sw.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if the geographical coordinates of the point lie within 
     * this rectangle
     * @param latlng  the given point.
     * @return  if the geographical coordinates of the point lie within 
     * this rectangle
     */
    public boolean containsLatLng(GeoLatLng latlng) {
        return contains(latlng.x, latlng.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the interior of the IShape intersects the interior of a 
     * specified rectangular area.
     * @param other  the given rectangle.
     * @return  true if the interior of the IShape and the interior of the 
     * rectangular area intersect.
     */
    public boolean intersects(GeoLatLngBounds other) {
        return intersects(other.x, other.y, other.width, other.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the interior of the IShape entirely contains the specified 
     * rectangular area. 
     * @param other  the given rectangle.
     * @return  true if the interior of the IShape entirely contains the 
     * specified rectangular area; 
     */
    public boolean containsBounds(GeoLatLngBounds other) {
        return contains(other.x, other.y, other.width, other.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Enlarges this rectangle such that it contains the given point. 
     * @param latlng  the new GeoLatLng to add to this rectangle.
     */
    public void extend(GeoLatLng latlng) {
        add(latlng.x, latlng.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the point at the south-west corner of the rectangle.
     * @return the point at the south-west corner of the rectangle.
     */
    public GeoLatLng getSouthWest() {
        return new GeoLatLng(this.y, this.x);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the point at the north-east corner of the rectangle.
     * @return the point at the north-east corner of the rectangle.
     */
    public GeoLatLng getNorthEast() {
        return new GeoLatLng(this.y + height, this.x + width);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a GLatLng whose cordinates represent the size of this rectangle.
     * @return the point whose cordinates represent the size of this rectangle.
     */
    public GeoLatLng toSpan() {
        return new GeoLatLng(width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this rectangle extends from the south pole to the north pole.
     * @return true if this rectangle extends from the south pole to the north pole.
     */
    public boolean isFullLat() {
        return (y == -90 && height == 180);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this rectangle extends fully around the earth in the
     * longitude direction.
     * @return true if this rectangle extends fully around the earth in the
     * longitude direction.
     */
    public boolean isFullLng() {
        return (y == -180 && height == 360);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this rectangle is empty.
     * @return true if this rectangle is empty.
     */
    public boolean isEmpty() {
        return (width <= 0) || (height <= 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the point at the center of the rectangle.
     * @return the point at the center of the rectangle.
     */
    public GeoLatLng getCenter() {
        GeoPoint pt1=MapLayer.fromLatLngToPixel(getSouthWest(), 15);
        GeoPoint pt2=MapLayer.fromLatLngToPixel(getNorthEast(), 15);
        GeoPoint pt=new GeoPoint();
        pt.x=pt1.x+(pt2.x-pt1.x)/2;
        pt.y=pt1.y+(pt2.y-pt1.y)/2;
        return MapLayer.fromPixelToLatLng(pt, 15);
    }
}
