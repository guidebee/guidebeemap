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
import java.util.Vector;

import com.mapdigit.util.MathEx;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * PolylineEncoder encode/decode google encoded polyline string.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
class PolylineEncoder {

    /**
     * total levels
     */
    private int numLevels = 18;
    /**
     *
     */
    private int zoomFactor = 2;
    private double verySmall = 0.00001;
    private boolean forceEndpoints = true;
    private double[] zoomLevelBreaks;
    private GeoLatLngBounds bounds;

    // constructor
    public PolylineEncoder(int numLevels, int zoomFactor, double verySmall,
            boolean forceEndpoints) {

        this.numLevels = numLevels;
        this.zoomFactor = zoomFactor;
        this.verySmall = verySmall;
        this.forceEndpoints = forceEndpoints;

        this.zoomLevelBreaks = new double[numLevels];

        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall * MathEx.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    public PolylineEncoder() {
        this.zoomLevelBreaks = new double[numLevels];
        for (int i = 0; i < numLevels; i++) {
            this.zoomLevelBreaks[i] = verySmall * MathEx.pow(this.zoomFactor, numLevels - i - 1);
        }
    }

    public static int[] decodeLevel(String levels) {
        int len = levels.length();
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            char ch = levels.charAt(i);
            ret[i] = ch - '?';
        }

        return ret;
    }

    private static int floor1e5(double coordinate) {
        return (int) MathEx.floor(coordinate * 1e5);
    }

    private static String encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return (encodeNumber(sgn_num));
    }

    private static String encodeNumber(int num) {

        StringBuffer encodeString = new StringBuffer();

        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            encodeString.append((char) (nextValue));
            num >>= 5;
        }

        num += 63;
        encodeString.append((char) (num));

        return encodeString.toString();
    }

    /**
     * Now we can use the previous function to march down the list of points and
     * encode the levels. Like createEncodings, we ignore points whose distance
     * (in dists) is undefined.
     */
    private String encodeLevels(GeoLatLng[] points, double[] dists,
            double absMaxDist) {
        int i;
        StringBuffer encoded_levels = new StringBuffer();

        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels - computeLevel(absMaxDist) - 1));
        }
        for (i = 1; i < points.length - 1; i++) {
            if (dists[i] != 0) {
                encoded_levels.append(encodeNumber(this.numLevels - computeLevel(dists[i]) - 1));
            }
        }
        if (this.forceEndpoints) {
            encoded_levels.append(encodeNumber(this.numLevels - 1));
        } else {
            encoded_levels.append(encodeNumber(this.numLevels - computeLevel(absMaxDist) - 1));
        }
        return encoded_levels.toString();
    }

    /**
     * This computes the appropriate zoom level of a point in terms of it's
     * distance from the relevant segment in the DP algorithm. Could be done in
     * terms of a logarithm, but this approach makes it a bit easier to ensure
     * that the level is not too large.
     */
    private int computeLevel(double absMaxDist) {
        int lev = 0;
        if (absMaxDist > this.verySmall) {
            lev = 0;
            while (absMaxDist < this.zoomLevelBreaks[lev]) {
                lev++;
            }
            return lev;
        }
        return lev;
    }

    private String createEncodings(GeoLatLng[] points, double[] dists) {
        StringBuffer encodedPoints = new StringBuffer();

        double maxlat = 0, minlat = 0, maxlon = 0, minlon = 0;

        int plat = 0;
        int plng = 0;

        for (int i = 0; i < points.length; i++) {

            // determin bounds (max/min lat/lon)
            if (i == 0) {
                maxlat = minlat = points[i].lat();
                maxlon = minlon = points[i].lng();
            } else {
                if (points[i].lat() > maxlat) {
                    maxlat = points[i].lat();
                } else if (points[i].lat() < minlat) {
                    minlat = points[i].lat();
                } else if (points[i].lng() > maxlon) {
                    maxlon = points[i].lng();
                } else if (points[i].lng() < minlon) {
                    minlon = points[i].lng();
                }
            }

            if (dists[i] != 0 || i == 0 || i == points.length - 1) {
                GeoLatLng point = points[i];

                int late5 = floor1e5(point.lat());
                int lnge5 = floor1e5(point.lng());

                int dlat = late5 - plat;
                int dlng = lnge5 - plng;

                plat = late5;
                plng = lnge5;

                encodedPoints.append(encodeSignedNumber(dlat));
                encodedPoints.append(encodeSignedNumber(dlng));

            }
        }
        GeoLatLng sw, ne;
        sw = new GeoLatLng(minlat, minlon);
        ne = new GeoLatLng(maxlat, maxlon);
        bounds = new GeoLatLngBounds(sw, ne);
        return encodedPoints.toString();
    }

    public static Vector createDecodings(String polyline) {
        byte[] encoded = polyline.getBytes();
        int len = encoded.length;
        int index = 0;
        int lat = 0;
        int lng = 0;
        int dlat;
        int dlng;
        int shift = 0;
        int result = 0;
        int a = 0;
        int b = 0;
        Vector array = new Vector();
        while (index < len) {
            shift = 0;
            result = 0;
            do {
                a = encoded[index];
                b = a - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                index += 1;
            } while (b >= 0x20);

            dlat = ((result & 1) == 1 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;


            do {
                a = encoded[index];
                b = a - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
                index += 1;
            } while (b >= 0x20);

            dlng = ((result & 1) == 1 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            array.addElement(new GeoLatLng(lat * 1e-5, lng * 1e-5));
        }

        return array;
    }

    public static String[] createEncodings(GeoLatLng[] track, int level, int step) {

        String[] resultMap = new String[2];
        StringBuffer encodedPoints = new StringBuffer();
        StringBuffer encodedLevels = new StringBuffer();



        int plat = 0;
        int plng = 0;
        int counter = 0;

        int listSize = track.length;

        GeoLatLng trackpoint;

        for (int i = 0; i < listSize; i += step) {
            counter++;
            trackpoint = (GeoLatLng) track[i];

            int late5 = floor1e5(trackpoint.lat());
            int lnge5 = floor1e5(trackpoint.lng());

            int dlat = late5 - plat;
            int dlng = lnge5 - plng;

            plat = late5;
            plng = lnge5;

            encodedPoints.append(encodeSignedNumber(dlat)).append(
                    encodeSignedNumber(dlng));
            encodedLevels.append(encodeNumber(level));

        }

        resultMap[0] = encodedPoints.toString();
        resultMap[1] = encodedLevels.toString();

        return resultMap;
    }
}

