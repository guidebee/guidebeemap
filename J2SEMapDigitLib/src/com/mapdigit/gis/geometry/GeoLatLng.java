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

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * GeoLatLng is a point in geographical coordinates longitude and latitude.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public class GeoLatLng extends GeoPoint {

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the origin 
     * (0,&nbsp;0) of the geographical coordinate space. 
     */
    public GeoLatLng() {
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
     * the specified <code>GeoLatLng</code> object.
     * @param       p a point
     */
    public GeoLatLng(GeoLatLng p) {
        this(p.lat(), p.lng());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the specified 
     * {@code (lat,lng)} location in the coordinate space. 
     * @param lat the latitude coordinate.
     * @param lng the longitute coordinate.
     */
    public GeoLatLng(double lat, double lng) {
        this(lat, lng, true);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the specified 
     * {@code (lat,lng)} location in the coordinate space. 
     * @param lat the latitude coordinate.
     * @param lng the longitute coordinate.
     * @param unbounded whether the point of bounded or not.
     */
    public GeoLatLng(double lat, double lng, boolean unbounded) {
        double lat1 = lat;
        double lng1 = lng;
        this.unbounded = unbounded;
        if (!unbounded) {

            lng1 = lng1 - (((int) (lng1)) / 360) * 360;

            if (lng1 < 0) {
                lng1 += 360;
            }
            if (lat1 < -90) {
                lat1 = -90;
            } else if (-90 <= lat1 && lat1 < 90) {
                //lat1 = lat1;
            } else if (90 <= lat1) {
                lat1 = 90;
            }
            if (0 <= lng1 && lng1 < 180) {
                //lng1 = lng1;
            } else {
                lng1 = lng1 - 360;
            }
        }
        setLocation(lng1, lat1);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the latitude coordinate in degrees, as a number between -90 and 
     * +90. If the unbounded flag was set in the constructor,
     * this coordinate can be outside this interval.
     * @return  the latitude coordinate in degrees.
     */
    public double lat() {
        return y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the longitude coordinate in degrees, as a number between -180 and 
     * +180. If the unbounded flag was set in the constructor,
     * this coordinate can be outside this interval.
     * @return  the longitude coordinate in degrees.
     */
    public double lng() {
        return x;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the latitude coordinate in radians, as a number between -PI/2 
     * and +PI/2. If the unbounded flag was set in the constructor, 
     * this coordinate can be outside this interval.
     * @return  the latitude coordinate in radians.
     */
    public double latRadians() {
        return MathEx.toRadians(y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the longitude coordinate in radians, as a number between -PI 
     * and +PI. If the unbounded flag was set in the constructor, 
     * this coordinate can be outside this interval.
     * @return  the longitude coordinate in radians.
     */
    public double lngRadians() {
        return MathEx.toRadians(x);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not two points are equal. Two instances of
     * <code>GeoLatLng</code> are equal if the values of their 
     * <code>x</code> and <code>y</code> member fields, representing
     * their position in the coordinate space, are the same.
     * @param obj an object to be compared with this <code>GeoLatLng</code>
     * @return <code>true</code> if the object to be compared is
     *         an instance of <code>GeoLatLng</code> and has
     *         the same values; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof GeoLatLng) {
            GeoLatLng p2d = (GeoLatLng) obj;
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
     * Returns the distance, in meters, from this point to the given point. 
     * By default, this distance is calculated given the default equatorial 
     * earth radius of 6378137 meters. The earth is approximated as a sphere,
     * hence the distance could be off as much as 0.3%,
     * especially in the polar extremes. 
     * @param pt1 the first point.
     * @param pt2 the other point.
     * @return the distance, in kilo meters.
     */
    public static double distance(GeoLatLng pt1, GeoLatLng pt2) {
        GreateCircleCalculator cal = new GreateCircleCalculator(
                GreateCircleCalculator.EARTH_MODEL_WGS84,
                GreateCircleCalculator.UNIT_KM);
        return cal.calculateDistance(pt1, pt2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance, in meters, from this point to the given point. 
     * By default, this distance is calculated given the default equatorial 
     * earth radius of 6378137 meters. The earth is approximated as a sphere,
     * hence the distance could be off as much as 0.3%,
     * especially in the polar extremes. 
     * @param other the other point.
     * @return the distance, in Kilo meters.
     */
    public double distanceFrom(GeoLatLng other) {
        GreateCircleCalculator cal = new GreateCircleCalculator(
                GreateCircleCalculator.EARTH_MODEL_WGS84,
                GreateCircleCalculator.UNIT_KM);
        return cal.calculateDistance(this, other);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Calculate bearing
     * @param pt1 first location.
     * @param pt2 2nd location.
     * @param pt3 3rd location.
     * @return the bearing angle (from 0 to 360).
     */
    public static double getBearing(GeoLatLng pt1,GeoLatLng pt2,GeoLatLng pt3){
        double course1=azimuthTo(pt1,pt2);
        double course2=azimuthTo(pt2,pt3);
        double bearing=course2-course1;
        if(bearing<0) bearing+=360;
        return bearing;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Calculates the azimuth between the two points according to the ellipsoid
     * model of WGS84. The azimuth is relative to true north. The Coordinates
     * object on which this method is called is considered the origin for the
     * calculation and the Coordinates object passed as a parameter is the
     * destination which the azimuth is calculated to. When the origin is the
     * North pole and the destination is not the North pole, this method returns
     * 180.0. When the origin is the South pole and the destination is not the
     * South pole, this method returns 0.0. If the origin is equal to the
     * destination, this method returns 0.0. The implementation shall calculate
     * the result as exactly as it can. However, it is required that the result
     * is within 1 degree of the correct result.
     * @param from - the <code>Coordinates</code> of the origin
     * @param to - the <code>Coordinates</code> of the destination
     * @return the azimuth to the destination in degrees. Result is within the
     *         range [0.0 ,360.0).
     * @throws java.lang.NullPointerException - if the parameter is <code>null</code>
     */
    public static double azimuthTo(GeoLatLng from,GeoLatLng to) {
        if ( to == null || from==null) {
            throw new IllegalArgumentException
                    ( "azimuthTo does not accept a null parameter." );
        }

        // Convert from degrees to radians.
        double lat1 = Math.toRadians( from.lat() );
        double lon1 = Math.toRadians( from.lng() );
        double lat2 = Math.toRadians( to.lat());
        double lon2 = Math.toRadians( to.lng() );

        // Formula for computing the course between two points.
        // It is explained in detail here:
        //   http://williams.best.vwh.net/avform.htm
        //   http://www.movable-type.co.uk/scripts/LatLong.html
        // course = atan2(
        //            sin(lon2-lon1)*cos(lat2),
        //            cos(lat1)*sin(lat2)-sin(lat1)*cos(lat2)*cos(lon2-lon1))

        double deltaLon = lon2 - lon1;
        double cosLat2 = Math.cos( lat2 );
        double c1 = Math.sin(deltaLon) * cosLat2;
        double c2 = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * cosLat2 * Math.cos(deltaLon);
        double courseInRadians = MathEx.atan2( c1, c2 );

        double course = Math.toDegrees( courseInRadians );
        course = (360.0 + course) % 360.0;  // Normalize to [0,360)
        return (double)course;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string that represents this point in a format suitable 
     * for use as a URL parameter value, separated by a comma, without 
     * whitespace. By default, precision is returned to 6 digits, 
     * which corresponds to a resolution to 4 inches/ 11 centimeters. 
     * An optional precision parameter allows you to specify a lower 
     * precision to reduce server load.
     * @param precision the precision of the output.
     * @return string that represents this point.
     */
    public String toUrlValue(int precision) {
        long multiple = 1;
        double lat = this.y;
        double lng = this.x;
        if (precision < 0) {
            precision = 6;
        }
        for (int i = 0; i < precision; i++) {
            multiple *= 10;
        }
        lat = (((int) (lat * multiple + 0.5)) / ((double) multiple));
        lng = (((int) (lng * multiple + 0.5)) / ((double) multiple));
        return lat + "," + lng;
    }

    private boolean unbounded = true;

    
    private static GeoPoint projection(GeoLatLng point) {
        double RefLat = 0;
        double N0;
        double q1, q2, q;
        double dLat = point.y;
        double dLong = point.x;
        double CentralMeridian = 0;
        double x, y;
        N0 = 6378137.0 / MathEx.sqrt(1 - MathEx.pow(0.081819190843, 2) * MathEx.pow(MathEx.sin(RefLat * MathEx.PI / 180), 2));
        q1 = MathEx.log(MathEx.tan((180.0 / 4.0 + dLat / 2.0) * MathEx.PI / 180.0));
        q2 = 0.081819190843 / 2 * MathEx.log((1 + 0.081819190843 * MathEx.sin(dLat * MathEx.PI / 180.0)) /
                (1 - 0.081819190843 * MathEx.sin(dLat * MathEx.PI / 180.0)));
        q = q1 - q2;
        x = N0 * MathEx.cos(RefLat * MathEx.PI / 180.0) * ((dLong - CentralMeridian) / 57.29577951);
        y = N0 * MathEx.cos(RefLat * MathEx.PI / 180.0) * q;
        return new GeoPoint(x, y);
    }
}
