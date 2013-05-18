//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	           Code review
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
 * The <code>GreateCircleCalculator</code> compute true course and distance
 * between points.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
class GreateCircleCalculator {

    /**
     * Sphere model.
     */
    public final static int EARTH_MODEL_SPHERE = 1;
    /**
     * WGS84 model. radius=6378.137km
     */
    public final static int EARTH_MODEL_WGS84 = 2;
    /**
     * NAD27 model. radius=6378.2064km
     */
    public final static int EARTH_MODEL_NAD27 = 3;
    /**
     * international model. radius=6378.388km.
     */
    public final static int EARTH_MODEL_INTERNATIONAL = 4;
    /**
     * krasovsky model. radius=6378.245km.
     */
    public final static int EARTH_MODEL_KRASOVSKY = 5;
    /**
     * bessel model. radius=6377.397155km.
     */
    public final static int EARTH_MODEL_BESSEL = 6;
    /**
     * WGS72 model. radius=6378.135km.
     */
    public final static int EARTH_MODEL_WGS72 = 7;
    /**
     * WGS66 model. radius=6378.145.
     */
    public final static int EARTH_MODEL_WGS66 = 8;
    /**
     * FAI sphere. radius=6371.0km.
     */
    public final static int EARTH_MODEL_FAI_SPHERE = 9;
    /**
     * User defined model.
     */
    //public final static int EARTH_MODEL_USER=0;
    /**
     * in nautical mile.
     */
    public final static int UNIT_NM = 1;
    /**
     * in Kilometers.
     */
    public final static int UNIT_KM = 2;
    private int currentEarthMode = 2;
    private int currentUnit = 2;
    private static double[][] earth_model = new double[][]{
        {0, 0},
        {180 * 60 / MathEx.PI, Double.POSITIVE_INFINITY},
        {6378.137 / 1.852, 298.257223563},
        {6378.2064 / 1.852, 294.9786982138},
        {6378.388 / 1.852, 297.0},
        {6378.245 / 1.852, 298.3},
        {6377.397155 / 1.852, 299.1528},
        {6378.135 / 1.852, 298.26},
        {6378.145 / 1.852, 298.25},
        {6371.0 / 1.852, 1000000000.}
    };

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Constructor.
     * @param earthModel the model of the earth.
     * @param unit  Km or mile, default is km.
     */
    public GreateCircleCalculator(int earthModel, int unit) {
        currentEarthMode = earthModel % 10;
        currentUnit = unit;
        if (unit > 2 || unit < 1) {
            currentUnit = UNIT_KM;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Calculate the distance between two point in great circle.
     * @param pt1  the first location.
     * @param pt2  the second location.
     * @return the distance between the two points.
     */
    public double calculateDistance(GeoLatLng pt1, GeoLatLng pt2) {
        crsdistResult ret = ComputeFormCD(pt1, pt2);
        return ret.d;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Calculate the distance between two point in great circle.
     * @param pt1  the first location.
     * @param pt2  the second location.
     * @return the distance and course between the 2 point.
     */
    GeoLatLng calculateDistanceAndCourse(GeoLatLng pt1, GeoLatLng pt2) {
        crsdistResult ret = ComputeFormCD(pt1, pt2);
        GeoLatLng ret1 = new GeoLatLng(ret.crs12, ret.d);
        return ret1;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Get a location with given distance ,direction of one point.
     * @param pt1  the first location.
     * @param distance  the distance to the first point.
     * @param direction the direction to the first point.
     * @return the second point.
     */
    public GeoLatLng getLocationWithDistance(GeoLatLng pt1, double distance, double direction) {
        return ComputeFormDir(pt1, distance, direction);
    }

    private GeoLatLng ComputeFormDir(GeoLatLng pt1, double distance, double crs) {
        //get select  values
        double dc;
        double lat1, lon1;
        double lat2, lon2;
        double retLat, retLon;
        double d12, crs12;
        /* Input and validate data */

        lat1 = pt1.latRadians();
        lon1 = -pt1.lngRadians();

        d12 = distance;
        if (currentUnit == UNIT_NM) {
            dc = 1;
        } else {
            dc = 1.852;
        }
        d12 /= dc;
        crs12 = crs * MathEx.PI / 180.; // radians
        if (currentEarthMode == EARTH_MODEL_SPHERE) {
            // spherical code
            d12 /= (180 * 60 / MathEx.PI);  // in radians
            directResult cd = direct(lat1, lon1, crs12, d12);
            lat2 = cd.lat * (180 / MathEx.PI);
            lon2 = cd.lon * (180 / MathEx.PI);
        } else {
            // elliptic code// ellipse uses East negative
            directResult cde = direct_ell(lat1, -lon1, crs12, d12, currentEarthMode); 
            lat2 = cde.lat * (180 / MathEx.PI);
            lon2 = -cde.lon * (180 / MathEx.PI);// ellipse uses East negative
        }
        retLat = lat2;
        retLon = -lon2;
        return new GeoLatLng(retLat, retLon);
    }

    private crsdistResult ComputeFormCD(GeoLatLng pt1, GeoLatLng pt2) {
        double dc;
        double lat1, lat2, lon1, lon2;
        double d, crs12, crs21;
        crsdistResult out = new crsdistResult();
        lat1 = pt1.latRadians();
        lat2 = pt2.latRadians();
        lon1 = -pt1.lngRadians();
        lon2 = -pt2.lngRadians();
        if (currentUnit == UNIT_NM) {
            dc = 1;
        } else {
            dc = 1.852;
        }

        if (currentEarthMode == EARTH_MODEL_SPHERE) {
            // spherical code// compute crs and distance
            crsdistResult cd = crsdist(lat1, lon1, lat2, lon2); 
            crs12 = cd.crs12 * (180 / MathEx.PI);
            crs21 = cd.crs21 * (180 / MathEx.PI);
            d = cd.d * (180 / MathEx.PI) * 60 * dc; // go to physical units
        } else {
            // elliptic code // ellipse uses East negative
            crsdistResult cde = crsdist_ell(lat1, -lon1, lat2, -lon2, currentEarthMode);  
            crs12 = cde.crs12 * (180 / MathEx.PI);
            crs21 = cde.crs21 * (180 / MathEx.PI);
            d = cde.d * dc; // go to physical units
        }
        out.crs12 = crs12;
        out.crs21 = crs21;
        out.d = d;
        return out;
    }

    private crsdistResult ComputeFormCD(String NS1, String NS2,
            String EW1, String EW2,
            String strLat1, String strLat2,
            String strLon1, String strLon2) {
        crsdistResult out = new crsdistResult();
        //get select  values
        int signlat1, signlat2, signlon1, signlon2;
        double dc;
        double lat1, lat2, lon1, lon2;
        double d, crs12, crs21;
        double argacos;
        double a, invf;
        /* Input and validate data */
        signlat1 = signLatlon(NS1);
        signlat2 = signLatlon(NS2);
        signlon1 = signLatlon(EW1);
        signlon2 = signLatlon(EW2);

        lat1 = (MathEx.PI / 180) * signlat1 * checkField("latitude", strLat1);
        lat2 = (MathEx.PI / 180) * signlat2 * checkField("latitude", strLat2);
        lon1 = (MathEx.PI / 180) * signlon1 * checkField("longitude", strLon1);
        lon2 = (MathEx.PI / 180) * signlon2 * checkField("longitude", strLon2);

        if (currentUnit == UNIT_NM) {
            dc = 1;
        } else {
            dc = 1.852;
        }

        if (currentEarthMode == EARTH_MODEL_SPHERE) {
            // spherical code
            crsdistResult cd = crsdist(lat1, lon1, lat2, lon2); // compute crs and distance
            crs12 = cd.crs12 * (180 / MathEx.PI);
            crs21 = cd.crs21 * (180 / MathEx.PI);
            d = cd.d * (180 / MathEx.PI) * 60 * dc; // go to physical units
        } else {
            // elliptic code
            crsdistResult cde = crsdist_ell(lat1, -lon1, lat2, -lon2, currentEarthMode);  // ellipse uses East negative
            crs12 = cde.crs12 * (180 / MathEx.PI);
            crs21 = cde.crs21 * (180 / MathEx.PI);
            d = cde.d * dc; // go to physical units
        }
        out.crs12 = crs12;
        out.crs21 = crs21;
        out.d = d;
        return out;
    }

    private boolean isPosInteger(String instr) { //integer only
        String str = "" + instr; // force to string type
        for (int i = 0; i < str.length(); i++) {
            char oneChar = str.charAt(i);
            if (oneChar < '0' || oneChar > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean isPosNumber(String instr) { //integer or float
        String str;
        boolean oneDecimal;
        str = "" + instr; // force to string type
        oneDecimal = false;
        char oneChar;
        for (int i = 0; i < str.length(); i++) {
            oneChar = str.charAt(i);
            if (oneChar == '.' && !oneDecimal) {
                oneDecimal = true;
                continue;
            }
            if (oneChar < '0' || oneChar > '9') {
                return false;
            }
        }
        return true;
    }

    private void badLLFormat(String str) {
        throw new IllegalArgumentException(str + " is an invalid lat/lon format\n" +
                "Use DD.DD DD:MM.MM or DD:MM:SS.SS");
    }

    private double parseLatlng(String instr) throws IllegalArgumentException {
        String str, degstr, minstr;
        int colonIndex;
        double deg, min, sec;
        str = instr;
        colonIndex = str.indexOf(":");
        if (colonIndex == -1) { // dd.dd?
            if (!isPosNumber(str)) {
                badLLFormat(instr);
                return 0.;
            } else {
                return Double.parseDouble(str);
            }
        } // falls through if we have a colon

        degstr = str.substring(0, colonIndex);  //DD
        str = str.substring(colonIndex + 1, str.length());//MM...
        if (!isPosInteger(degstr)) {
            badLLFormat(instr);
            return 0.;
        } else {
            deg = Double.parseDouble(degstr + ".0");
        }
        //now repeat to pick off minutes
        colonIndex = str.indexOf(":");
        if (colonIndex == -1) { // dd:mm.mm?
            if (!isPosNumber(str)) {
                badLLFormat(instr);
                return 0.;
            } else {
                min = Double.parseDouble(str);
                if (min < 60.) {
                    return deg + Double.parseDouble(str) / 60.;
                } else {
                    badLLFormat(instr);
                    return 0.;
                }

            }
        }
        return 0.;
    }

    private int signLatlon(String selection) {
        int sign;
        if (selection.equals("N") || selection.equals("W")) {
            sign = 1;
        } else {
            sign = -1;
        }
        return sign;
    }

    private double checkField(String fieldName, String fieldValue) {
        String str = fieldName;
        double latlon;
        latlon = parseLatlng(fieldValue);
        if (str.substring(0, 3).equals("lat")) {
            if (latlon > 90.) {
                throw new IllegalArgumentException("Latitudes cannot exceed 90 degrees");

            }
        }
        if (str.substring(0, 3).equals("lon")) {
            if (latlon > 180.) {
                throw new IllegalArgumentException("Longitudes cannot exceed 180 degrees");

            }
        }
        return latlon;
    }

    private double acosf(double x) { /* protect against rounding error on input argument */
        if (MathEx.abs(x) > 1) {
            x /= MathEx.abs(x);
        }
        return MathEx.acos(x);
    }

    private double atan2(double y, double x) {
        double out = 0;
        if (x < 0) {
            out = MathEx.atan(y / x) + MathEx.PI;
        }
        if ((x > 0) && (y >= 0)) {
            out = MathEx.atan(y / x);
        }
        if ((x > 0) && (y < 0)) {
            out = MathEx.atan(y / x) + 2 * MathEx.PI;
        }
        if ((x == 0) && (y > 0)) {
            out = MathEx.PI / 2;
        }
        if ((x == 0) && (y < 0)) {
            out = 3 * MathEx.PI / 2;
        }
        if ((x == 0) && (y == 0)) {
            out = 0;
        }
        return out;
    }

    private double mod(double x, double y) {
        return x - y * MathEx.floor(x / y);
    }

    private double modlon(double x) {
        return mod(x + MathEx.PI, 2 * MathEx.PI) - MathEx.PI;
    }

    private double modcrs(double x) {
        return mod(x, 2 * MathEx.PI);
    }

    private double modlat(double x) {
        return mod(x + MathEx.PI / 2, 2 * MathEx.PI) - MathEx.PI / 2;
    }

    private String degtodm(double deg, int decplaces) {
        // returns a rounded string DD:MM.MMMM
        double deg1 = MathEx.floor(deg);
        double min = 60. * (deg - MathEx.floor(deg));
        String mins = format(min, decplaces);
        //alert("deg1="+deg1+" mins="+mins)
        // rounding may have rounded mins to 60.00-- sigh
        if (mins.substring(0, 1).equals("6") && Double.parseDouble(mins) > 59.0) {
            deg1 += 1;
            mins = format(0, decplaces);
        }
        return deg1 + ":" + mins;
    }

    private String format(double expr, int decplaces) {
        String str = "" + (int) (expr * MathEx.pow(10, decplaces) + 0.5);
        while (str.length() <= decplaces) {
            str = "0" + str;
        }
        int decpoint = str.length() - decplaces;
        return str.substring(0, decpoint) + "." + str.substring(decpoint, str.length());
    }

    private crsdistResult crsdist(double lat1, double lon1, double lat2, double lon2) { // radian args
        /* compute course and distance (spherical) */
        double d, crs12, crs21;
        double argacos;
        if ((lat1 + lat2 == 0.) && (MathEx.abs(lon1 - lon2) == MathEx.PI) && (MathEx.abs(lat1) != (MathEx.PI / 180) * 90.)) {
            throw new IllegalArgumentException("Course between antipodal points is undefined");
        }

        d = MathEx.acos(MathEx.sin(lat1) * MathEx.sin(lat2) + MathEx.cos(lat1) * MathEx.cos(lat2) * MathEx.cos(lon1 - lon2));

        if ((d == 0.) || (lat1 == -(MathEx.PI / 180) * 90.)) {
            crs12 = 2 * MathEx.PI;
        } else if (lat1 == (MathEx.PI / 180) * 90.) {
            crs12 = MathEx.PI;
        } else {
            argacos = (MathEx.sin(lat2) - MathEx.sin(lat1) * MathEx.cos(d)) / (MathEx.sin(d) * MathEx.cos(lat1));
            if (MathEx.sin(lon2 - lon1) < 0) {
                crs12 = acosf(argacos);
            } else {
                crs12 = 2 * MathEx.PI - acosf(argacos);
            }
        }
        if ((d == 0.) || (lat2 == -(MathEx.PI / 180) * 90.)) {
            crs21 = 0.;
        } else if (lat2 == (MathEx.PI / 180) * 90.) {
            crs21 = MathEx.PI;
        } else {
            argacos = (MathEx.sin(lat1) - MathEx.sin(lat2) * MathEx.cos(d)) / (MathEx.sin(d) * MathEx.cos(lat2));
            if (MathEx.sin(lon1 - lon2) < 0) {
                crs21 = acosf(argacos);
            } else {
                crs21 = 2 * MathEx.PI - acosf(argacos);
            }
        }

        crsdistResult out = new crsdistResult();
        out.d = d;
        out.crs12 = crs12;
        out.crs21 = crs21;
        return out;
    }

    private crsdistResult crsdist_ell(double glat1, double glon1, double glat2, double glon2, int ellipse) {
        // glat1 initial geodetic latitude in radians N positive
        // glon1 initial geodetic longitude in radians E positive
        // glat2 final geodetic latitude in radians N positive
        // glon2 final geodetic longitude in radians E positive
        double a = earth_model[ellipse][0];
        double f = 1 / earth_model[ellipse][1];
        //alert("a="+a+" f="+f)
        double r, tu1, tu2, cu1, su1, cu2, s1, b1, f1;
        double x = 0, sx = 0, cx = 0, sy = 0, cy = 0, y = 0, sa = 0, c2a = 0, cz = 0, e = 0, c = 0, d = 0;
        double EPS = 0.00000000005;
        double faz, baz, s;
        double iter = 1;
        double MAXITER = 100;
        crsdistResult out = new crsdistResult();
        if ((glat1 + glat2 == 0.) && (MathEx.abs(glon1 - glon2) == MathEx.PI)) {
            glat1 = glat1 + 0.00001; // allow algorithm to complete

        }
        if (glat1 == glat2 && (glon1 == glon2 || MathEx.abs(MathEx.abs(glon1 - glon2) - 2 * MathEx.PI) < EPS)) {


            out.d = 0;
            out.crs12 = 0;
            out.crs21 = MathEx.PI;
            return out;
        }
        r = 1 - f;
        tu1 = r * MathEx.tan(glat1);
        tu2 = r * MathEx.tan(glat2);
        cu1 = 1. / MathEx.sqrt(1. + tu1 * tu1);
        su1 = cu1 * tu1;
        cu2 = 1. / MathEx.sqrt(1. + tu2 * tu2);
        s1 = cu1 * cu2;
        b1 = s1 * tu2;
        f1 = b1 * tu1;
        x = glon2 - glon1;
        d = x + 1;// force one pass
        while ((MathEx.abs(d - x) > EPS) && (iter < MAXITER)) {
            iter = iter + 1;
            sx = MathEx.sin(x);
            cx = MathEx.cos(x);
            tu1 = cu2 * sx;
            tu2 = b1 - su1 * cu2 * cx;
            sy = MathEx.sqrt(tu1 * tu1 + tu2 * tu2);
            cy = s1 * cx + f1;
            y = atan2(sy, cy);
            sa = s1 * sx / sy;
            c2a = 1 - sa * sa;
            cz = f1 + f1;
            if (c2a > 0.) {
                cz = cy - cz / c2a;
            }
            e = cz * cz * 2. - 1.;
            c = ((-3. * c2a + 4.) * f + 4.) * c2a * f / 16.;
            d = x;
            x = ((e * cy * c + cz) * sy * c + y) * sa;
            x = (1. - c) * x * f + glon2 - glon1;
        }
        faz = modcrs(atan2(tu1, tu2));
        baz = modcrs(atan2(cu1 * sx, b1 * cx - su1 * cu2) + MathEx.PI);
        x = MathEx.sqrt((1 / (r * r) - 1) * c2a + 1);
        x += 1;
        x = (x - 2.) / x;
        c = 1. - x;
        c = (x * x / 4. + 1.) / c;
        d = (0.375 * x * x - 1.) * x;
        x = e * cy;
        s = ((((sy * sy * 4. - 3.) * (1. - e - e) * cz * d / 6. - x) * d / 4. + cz) * sy * d + y) * c * a * r;

        out.d = s;
        out.crs12 = faz;
        out.crs21 = baz;
        if (MathEx.abs(iter - MAXITER) < EPS) {
            throw new IllegalArgumentException("Algorithm did not converge");
        }
        return out;

    }

    private directResult direct(double lat1, double lon1, double crs12, double d12) {
        double EPS = 0.00000000005;
        double dlon, lat, lon;
        if ((MathEx.abs(MathEx.cos(lat1)) < EPS) && !(MathEx.abs(MathEx.sin(crs12)) < EPS)) {
            throw new IllegalArgumentException("Only N-S courses are meaningful, starting at a pole!");
        }

        lat = MathEx.asin(MathEx.sin(lat1) * MathEx.cos(d12) +
                MathEx.cos(lat1) * MathEx.sin(d12) * MathEx.cos(crs12));
        if (MathEx.abs(MathEx.cos(lat)) < EPS) {
            lon = 0.; //endpoint a pole
        } else {
            dlon = MathEx.atan2(MathEx.sin(crs12) * MathEx.sin(d12) * MathEx.cos(lat1),
                    MathEx.cos(d12) - MathEx.sin(lat1) * MathEx.sin(lat));
            lon = mod(lon1 - dlon + MathEx.PI, 2 * MathEx.PI) - MathEx.PI;
        }
        directResult out = new directResult();
        out.lat = lat;
        out.lon = lon;
        return out;
    }

    private directResult direct_ell(double glat1, double glon1, double faz, double s, int ellipse) {
        // glat1 initial geodetic latitude in radians N positive
        // glon1 initial geodetic longitude in radians E positive
        // faz forward azimuth in radians
        // s distance in units of a (=nm)

        double EPS = 0.00000000005;
        double r = 0, tu = 0, sf = 0, cf = 0, b = 0, cu = 0, su = 0;
        double sa = 0, c2a = 0, x = 0, c = 0, d = 0, y = 0, sy = 0, cy = 0, cz = 0, e = 0;
        double glat2, glon2, baz, f;

        if ((MathEx.abs(MathEx.cos(glat1)) < EPS) && !(MathEx.abs(MathEx.sin(faz)) < EPS)) {
            throw new IllegalArgumentException("Only N-S courses are meaningful, starting at a pole!");
        }

        double a = earth_model[ellipse][0];
        f = 1 / earth_model[ellipse][1];
        r = 1 - f;
        tu = r * MathEx.tan(glat1);
        sf = MathEx.sin(faz);
        cf = MathEx.cos(faz);
        if (cf == 0) {
            b = 0.;
        } else {
            b = 2. * atan2(tu, cf);
        }
        cu = 1. / MathEx.sqrt(1 + tu * tu);
        su = tu * cu;
        sa = cu * sf;
        c2a = 1 - sa * sa;
        x = 1. + MathEx.sqrt(1. + c2a * (1. / (r * r) - 1.));
        x = (x - 2.) / x;
        c = 1. - x;
        c = (x * x / 4. + 1.) / c;
        d = (0.375 * x * x - 1.) * x;
        tu = s / (r * a * c);
        y = tu;
        c = y + 1;
        while (MathEx.abs(y - c) > EPS) {
            sy = MathEx.sin(y);
            cy = MathEx.cos(y);
            cz = MathEx.cos(b + y);
            e = 2. * cz * cz - 1.;
            c = y;
            x = e * cy;
            y = e + e - 1.;
            y = (((sy * sy * 4. - 3.) * y * cz * d / 6. + x) *
                    d / 4. - cz) * sy * d + tu;
        }

        b = cu * cy * cf - su * sy;
        c = r * MathEx.sqrt(sa * sa + b * b);
        d = su * cy + cu * sy * cf;
        glat2 = modlat(atan2(d, c));
        c = cu * cy - su * sy * cf;
        x = atan2(sy * sf, c);
        c = ((-3. * c2a + 4.) * f + 4.) * c2a * f / 16.;
        d = ((e * cy * c + cz) * sy * c + y) * sa;
        glon2 = modlon(glon1 + x - (1. - c) * d * f);// fix date line problems
        baz = modcrs(atan2(sa, b) + MathEx.PI);

        directResult out = new directResult();
        out.lat = glat2;
        out.lon = glon2;
        out.crs21 = baz;
        return out;
    }
}

class crsdistResult {

    public double d;
    public double crs12;
    public double crs21;
}

class directResult {

    double lat;
    double lon;
    double crs21;
}
