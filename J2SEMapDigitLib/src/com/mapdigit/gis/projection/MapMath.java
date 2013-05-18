//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 15MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.projection;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.util.MathEx;

import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoPoint;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Math function used by Map projection.
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 15/03/08
 * @author      Guidebee Pty Ltd.
 */
class MapMath {

    public final static double HALFPI = MathEx.PI / 2.0;
    public final static double QUARTERPI = MathEx.PI / 4.0;
    public final static double TWOPI = MathEx.PI * 2.0;
    public final static double RTD = 180.0 / MathEx.PI;
    public final static double DTR = MathEx.PI / 180.0;
    public final static GeoBounds WORLD_BOUNDS_RAD =
            new GeoBounds(-MathEx.PI, -MathEx.PI / 2, MathEx.PI * 2, MathEx.PI);
    public final static GeoBounds WORLD_BOUNDS
            = new GeoBounds(-180, -90, 360, 180);

    /**
     * Degree versions of trigonometric functions
     */
    public static double sind(double v) {
        return MathEx.sin(v * DTR);
    }

    public static double cosd(double v) {
        return MathEx.cos(v * DTR);
    }

    public static double tand(double v) {
        return MathEx.tan(v * DTR);
    }

    public static double asind(double v) {
        return MathEx.asin(v) * RTD;
    }

    public static double acosd(double v) {
        return MathEx.acos(v) * RTD;
    }

    public static double atand(double v) {
        return MathEx.atan(v) * RTD;
    }

    public static double atan2d(double y, double x) {
        return MathEx.atan2(y, x) * RTD;
    }

    public static double asin(double v) {
        if (MathEx.abs(v) > 1.) {
            return v < 0.0 ? -MathEx.PI / 2 : MathEx.PI / 2;
        }
        return MathEx.asin(v);
    }

    public static double acos(double v) {
        if (MathEx.abs(v) > 1.) {
            return v < 0.0 ? MathEx.PI : 0.0;
        }
        return MathEx.acos(v);
    }

    public static double sqrt(double v) {
        return v < 0.0 ? 0.0 : MathEx.sqrt(v);
    }

    public static double distance(double dx, double dy) {
        return MathEx.sqrt(dx * dx + dy * dy);
    }

    public static double distance(GeoPoint a, GeoPoint b) {
        return distance(a.x - b.x, a.y - b.y);
    }

    public static double hypot(double x, double y) {
        if (x < 0.0) {
            x = -x;
        } else if (x == 0.0) {
            return y < 0.0 ? -y : y;
        }
        if (y < 0.0) {
            y = -y;
        } else if (y == 0.0) {
            return x;
        }
        if (x < y) {
            x /= y;
            return y * MathEx.sqrt(1.0 + x * x);
        } else {
            y /= x;
            return x * MathEx.sqrt(1.0 + y * y);
        }
    }

    public static double atan2(double y, double x) {
        return MathEx.atan2(y, x);
    }

    public static double trunc(double v) {
        return v < 0.0 ? MathEx.ceil(v) : MathEx.floor(v);
    }

    public static double frac(double v) {
        return v - trunc(v);
    }

    public static double degToRad(double v) {
        return v * MathEx.PI / 180.0;
    }

    public static double radToDeg(double v) {
        return v * 180.0 / MathEx.PI;
    }

    // For negative angles, d should be negative, m & s positive.
    public static double dmsToRad(double d, double m, double s) {
        if (d >= 0) {
            return (d + m / 60 + s / 3600) * MathEx.PI / 180.0;
        }
        return (d - m / 60 - s / 3600) * MathEx.PI / 180.0;
    }

    // For negative angles, d should be negative, m & s positive.
    public static double dmsToDeg(double d, double m, double s) {
        if (d >= 0) {
            return (d + m / 60 + s / 3600);
        }
        return (d - m / 60 - s / 3600);
    }

    public static double normalizeLatitude(double angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle)) {
            throw new ProjectionException("Infinite latitude");
        }
        while (angle > MapMath.HALFPI) {
            angle -= MathEx.PI;
        }
        while (angle < -MapMath.HALFPI) {
            angle += MathEx.PI;
        }
        return angle;
    }

    public static double normalizeLongitude(double angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle)) {
            throw new ProjectionException("Infinite longitude");
        }
        while (angle > MathEx.PI) {
            angle -= TWOPI;
        }
        while (angle < -MathEx.PI) {
            angle += TWOPI;
        }
        return angle;
    }

    public static double normalizeAngle(double angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle)) {
            throw new ProjectionException("Infinite angle");
        }
        while (angle > TWOPI) {
            angle -= TWOPI;
        }
        while (angle < 0) {
            angle += TWOPI;
        }
        return angle;
    }


    public static double greatCircleDistance(double lon1, double lat1,
            double lon2, double lat2) {
        double dlat = MathEx.sin((lat2 - lat1) / 2);
        double dlon = MathEx.sin((lon2 - lon1) / 2);
        double r = MathEx.sqrt(dlat * dlat + MathEx.cos(lat1)
                * MathEx.cos(lat2) * dlon * dlon);
        return 2.0 * MathEx.asin(r);
    }

    public static double sphericalAzimuth(double lat0, double lon0,
            double lat, double lon) {
        double diff = lon - lon0;
        double coslat = MathEx.cos(lat);

        return MathEx.atan2(
                coslat * MathEx.sin(diff),
                (MathEx.cos(lat0) * MathEx.sin(lat) -
                MathEx.sin(lat0) * coslat * MathEx.cos(diff)));
    }

    public static boolean sameSigns(double a, double b) {
        return a < 0 == b < 0;
    }

    public static boolean sameSigns(int a, int b) {
        return a < 0 == b < 0;
    }

    public static double takeSign(double a, double b) {
        a = MathEx.abs(a);
        if (b < 0) {
            return -a;
        }
        return a;
    }

    public static int takeSign(int a, int b) {
        a = MathEx.abs(a);
        if (b < 0) {
            return -a;
        }
        return a;
    }
    public final static int DONT_INTERSECT = 0;
    public final static int DO_INTERSECT = 1;
    public final static int COLLINEAR = 2;

    public static int intersectSegments(GeoPoint aStart, GeoPoint aEnd,
            GeoPoint bStart, GeoPoint bEnd, GeoPoint p) {
        double a1, a2, b1, b2, c1, c2;
        double r1, r2, r3, r4;
        double denom, offset, num;

        a1 = aEnd.y - aStart.y;
        b1 = aStart.x - aEnd.x;
        c1 = aEnd.x * aStart.y - aStart.x * aEnd.y;
        r3 = a1 * bStart.x + b1 * bStart.y + c1;
        r4 = a1 * bEnd.x + b1 * bEnd.y + c1;

        if (r3 != 0 && r4 != 0 && sameSigns(r3, r4)) {
            return DONT_INTERSECT;
        }

        a2 = bEnd.y - bStart.y;
        b2 = bStart.x - bEnd.x;
        c2 = bEnd.x * bStart.y - bStart.x * bEnd.y;
        r1 = a2 * aStart.x + b2 * aStart.y + c2;
        r2 = a2 * aEnd.x + b2 * aEnd.y + c2;

        if (r1 != 0 && r2 != 0 && sameSigns(r1, r2)) {
            return DONT_INTERSECT;
        }

        denom = a1 * b2 - a2 * b1;
        if (denom == 0) {
            return COLLINEAR;
        }

        offset = denom < 0 ? -denom / 2 : denom / 2;

        num = b1 * c2 - b2 * c1;
        p.x = (num < 0 ? num - offset : num + offset) / denom;

        num = a2 * c1 - a1 * c2;
        p.y = (num < 0 ? num - offset : num + offset) / denom;

        return DO_INTERSECT;
    }

    public static double dot(GeoPoint a, GeoPoint b) {
        return a.x * b.x + a.y * b.y;
    }

    public static GeoPoint perpendicular(GeoPoint a) {
        return new GeoPoint(-a.y, a.x);
    }

    public static GeoPoint add(GeoPoint a, GeoPoint b) {
        return new GeoPoint(a.x + b.x, a.y + b.y);
    }

    public static GeoPoint subtract(GeoPoint a, GeoPoint b) {
        return new GeoPoint(a.x - b.x, a.y - b.y);
    }

    public static GeoPoint multiply(GeoPoint a, GeoPoint b) {
        return new GeoPoint(a.x * b.x, a.y * b.y);
    }

    public static double cross(GeoPoint a, GeoPoint b) {
        return a.x * b.y - b.x * a.y;
    }

    public static double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - x2 * y1;
    }

    public static void normalize(GeoPoint a) {
        double d = distance(a.x, a.y);
        a.x /= d;
        a.y /= d;
    }

    public static void negate(GeoPoint a) {
        a.x = -a.x;
        a.y = -a.y;
    }

    public static double longitudeDistance(double l1, double l2) {
        return MathEx.min(
                MathEx.abs(l1 - l2),
                ((l1 < 0) ? l1 + MathEx.PI : MathEx.PI - l1)
                + ((l2 < 0) ? l2 + MathEx.PI : MathEx.PI - l2));
    }

    public static double geocentricLatitude(double lat, double flatness) {
        double f = 1.0 - flatness;
        return MathEx.atan((f * f) * MathEx.tan(lat));
    }

    public static double geographicLatitude(double lat, double flatness) {
        double f = 1.0 - flatness;
        return MathEx.atan(MathEx.tan(lat) / (f * f));
    }

    public static double tsfn(double phi, double sinphi, double e) {
        sinphi *= e;
        return (MathEx.tan(.5 * (MapMath.HALFPI - phi)) /
                MathEx.pow((1. - sinphi) / (1. + sinphi), .5 * e));
    }

    public static double msfn(double sinphi, double cosphi, double es) {
        return cosphi / MathEx.sqrt(1.0 - es * sinphi * sinphi);
    }
    private final static int N_ITER = 15;

    public static double phi2(double ts, double e) {
        double eccnth, phi, con, dphi;
        int i;

        eccnth = .5 * e;
        phi = MapMath.HALFPI - 2. * MathEx.atan(ts);
        i = N_ITER;
        do {
            con = e * MathEx.sin(phi);
            dphi = MapMath.HALFPI - 2. * MathEx.atan(ts * MathEx.pow((1. - con)
                    / (1. + con), eccnth)) - phi;
            phi += dphi;
        } while (MathEx.abs(dphi) > 1e-10 && --i != 0);
        if (i <= 0) {
            throw new ProjectionException();
        }
        return phi;
    }
    private final static double C00 = 1.0;
    private final static double C02 = .25;
    private final static double C04 = .046875;
    private final static double C06 = .01953125;
    private final static double C08 = .01068115234375;
    private final static double C22 = .75;
    private final static double C44 = .46875;
    private final static double C46 = .01302083333333333333;
    private final static double C48 = .00712076822916666666;
    private final static double C66 = .36458333333333333333;
    private final static double C68 = .00569661458333333333;
    private final static double C88 = .3076171875;
    private final static int MAX_ITER = 10;

    public static double[] enfn(double es) {
        double t;
        double[] en = new double[5];
        en[0] = C00 - es * (C02 + es * (C04 + es * (C06 + es * C08)));
        en[1] = es * (C22 - es * (C04 + es * (C06 + es * C08)));
        en[2] = (t = es * es) * (C44 - es * (C46 + es * C48));
        en[3] = (t *= es) * (C66 - es * C68);
        en[4] = t * es * C88;
        return en;
    }

    public static double mlfn(double phi, double sphi, double cphi, double[] en) {
        cphi *= sphi;
        sphi *= sphi;
        return en[0] * phi - cphi * (en[1] + sphi * (en[2]
                + sphi * (en[3] + sphi * en[4])));
    }

    public static double inv_mlfn(double arg, double es, double[] en) {
        double s, t, phi, k = 1. / (1. - es);

        phi = arg;
        for (int i = MAX_ITER; i != 0; i--) {
            s = MathEx.sin(phi);
            t = 1. - es * s * s;
            phi -= t = (mlfn(phi, s, MathEx.cos(phi), en) - arg)
                    * (t * MathEx.sqrt(t)) * k;
            if (MathEx.abs(t) < 1e-11) {
                return phi;
            }
        }
        return phi;
    }
    private final static double P00 = .33333333333333333333;
    private final static double P01 = .17222222222222222222;
    private final static double P02 = .10257936507936507936;
    private final static double P10 = .06388888888888888888;
    private final static double P11 = .06640211640211640211;
    private final static double P20 = .01641501294219154443;

    public static double[] authset(double es) {
        double t;
        double[] APA = new double[3];
        APA[0] = es * P00;
        t = es * es;
        APA[0] += t * P01;
        APA[1] = t * P10;
        t *= es;
        APA[0] += t * P02;
        APA[1] += t * P11;
        APA[2] = t * P20;
        return APA;
    }

    public static double authlat(double beta, double[] APA) {
        double t = beta + beta;
        return (beta + APA[0] * MathEx.sin(t) + APA[1] * MathEx.sin(t + t)
                + APA[2] * MathEx.sin(t + t + t));
    }

    public static double qsfn(double sinphi, double e, double one_es) {
        double con;

        if (e >= 1.0e-7) {
            con = e * sinphi;
            return (one_es * (sinphi / (1. - con * con) -
                    (.5 / e) * MathEx.log((1. - con) / (1. + con))));
        } else {
            return (sinphi + sinphi);
        }
    }

    /*
     * Java translation of "Nice Numbers for Graph Labels"
     * by Paul Heckbert
     * from "Graphics Gems", Academic Press, 1990
     */
    public static double niceNumber(double x, boolean round) {
        int expv;				/* exponent of x */
        double f;				/* fractional part of x */
        double nf;				/* nice, rounded fraction */

        expv = (int) MathEx.floor(MathEx.log(x) / MathEx.log(10));
        f = x / MathEx.pow(10., expv);		/* between 1 and 10 */
        if (round) {
            if (f < 1.5) {
                nf = 1.;
            } else if (f < 3.) {
                nf = 2.;
            } else if (f < 7.) {
                nf = 5.;
            } else {
                nf = 10.;
            }
        } else if (f <= 1.) {
            nf = 1.;
        } else if (f <= 2.) {
            nf = 2.;
        } else if (f <= 5.) {
            nf = 5.;
        } else {
            nf = 10.;
        }
        return nf * MathEx.pow(10., expv);
    }
}

