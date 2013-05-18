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

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Polyline on map.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class GeoPolyline {

    /**
     * Zoom factor
     */
    public int zoomFactor;
    /**
     * total zoom level, default 18
     */
    public int numLevels;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public GeoPolyline() {
        latlngs = null;
        levels = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * copy constructor.
     * @param pline pline object copied from.
     */
    public GeoPolyline(GeoPolyline pline) {
        if (pline.latlngs != null) {
            this.latlngs = new GeoLatLng[pline.latlngs.length];
            System.arraycopy(pline.latlngs, 0, this.latlngs, 0, latlngs.length);
            levels = new int[pline.levels.length];
            for (int i = 0; i < levels.length; i++) {
                levels[i] = pline.levels[i];
            }
            this.bounds = new GeoLatLngBounds(pline.bounds);
        }
        this.color = pline.color;
        this.weight = pline.weight;
        this.opacity = pline.opacity;
        zoomFactor = pline.zoomFactor;
        numLevels = pline.numLevels;
        visible = pline.visible;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a polyline from an array of vertices.  The weight is the width of
     * the line in pixels. The opacity is given as a number between 0 and 1.
     * The line will be antialiased and semitransparent.
     * @param latlngs array of points.
     * @param color the color of the polyline.
     * @param weight the width of the polyline.
     * @param opacity the opacity of the polyline.
     */
    public GeoPolyline(GeoLatLng[] latlngs, int color, int weight,
            double opacity) {
        if (latlngs != null) {
            this.latlngs = new GeoLatLng[latlngs.length];
            System.arraycopy(latlngs, 0, this.latlngs, 0, latlngs.length);
            levels = new int[latlngs.length];
            for (int i = 0; i < levels.length; i++) {
                levels[i] = 16;
            }
            double maxlat = 0, minlat = 0, maxlon = 0, minlon = 0;
            GeoLatLng[] points = this.latlngs;
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
            }
            GeoLatLng sw, ne;
            sw = new GeoLatLng(minlat, minlon);
            ne = new GeoLatLng(maxlat, maxlon);
            this.bounds = new GeoLatLngBounds(sw, ne);
        }
        this.color = color;
        this.weight = weight;
        this.opacity = opacity;
        zoomFactor = 1;
        numLevels = 0;
        visible = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a polyline from encoded strings of aggregated points and levels.
     * zoomFactor and  numLevels  these two values determine the precision
     * of the levels within an encoded polyline.
     * @param color the color of the polyline.
     * @param weight width of the line in pixels.
     * @param opacity the opacity of the polyline.
     * @param points a string containing the encoded latitude and longitude
     *  coordinates.
     * @param zoomFactor  the magnification between adjacent sets of zoom levels
     * in the encoded levels string.
     * @param levels a string containing the encoded polyline zoom level groups.
     * @param numLevels the number of zoom levels contained in the encoded
     * levels string.
     * @return Geo polyline object.
     */
    public static GeoPolyline fromEncoded(int color, int weight, double opacity,
            String points, int zoomFactor, String levels, int numLevels) {
        Vector trk = PolylineEncoder.createDecodings(points);
        GeoLatLng[] array = new GeoLatLng[trk.size()];
        trk.copyInto(array);
        GeoPolyline polyline = new GeoPolyline(array, color, weight, opacity);
        polyline.levels = PolylineEncoder.decodeLevel(levels);
        polyline.zoomFactor = zoomFactor;
        polyline.numLevels = numLevels;
        return polyline;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from GeoPoint (x,y) to the closest line
     * segment in the Poly (int[] xpts, int[] ypts).
     * <p>
     * This procedure assumes that xpts.length == ypts.length.
     * <p>
     *
     * @param xpts X points of the polygon
     * @param ypts Y points of the polygon
     * @param ptx x location of the point
     * @param pty y location of the point
     * @param isPolygon polyline or polygon
     * @return the distance.
     */
    public final static double distanceToPoly(double ptx, double pty,double[] xpts,
            double[] ypts,boolean isPolygon) {
        return IndexOfClosestdistanceToPoly(ptx,pty,xpts,ypts,isPolygon).x;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from GeoPoint (x,y) to the closest line
     * segment in the Poly (int[] xpts, int[] ypts).
     * <p>
     * This procedure assumes that xpts.length == ypts.length.
     * <p>
     *
     * @param xpts X points of the polygon
     * @param ypts Y points of the polygon
     * @param ptx x location of the point
     * @param pty y location of the point
     * @param isPolygon polyline or polygon
     * @return GeoPoint whose x is the closes distance, y is the index of the poly
     */
    public final static GeoPoint IndexOfClosestdistanceToPoly(double ptx, double pty,
            double[] xpts,
            double[] ypts,boolean isPolygon) {
        GeoPoint retValue=new GeoPoint();
        if (xpts.length == 0)
        {
             retValue.x=Double.POSITIVE_INFINITY;
             retValue.y=0;
        }
        if (xpts.length == 1)
        {
            retValue.x= distance(xpts[0], ypts[0], ptx, pty);
            retValue.y=0;
        }

        double temp=0, distance = Double.POSITIVE_INFINITY;
        int i, j;

        for (i = 0, j = 1; j < xpts.length; i++, j++) {
            temp = distanceToLine(xpts[i],
                    ypts[i],
                    xpts[j],
                    ypts[j],
                    ptx,
                    pty);
            if (temp < distance){
                distance = temp;
                retValue.x=distance;
                retValue.y=i;
            }
        }

        // connect
        if (isPolygon) {
            temp = distanceToLine(xpts[i],
                    ypts[i],
                    xpts[0],
                    ypts[0],
                    ptx,
                    pty);
            if (temp < distance){
                distance = temp;
                retValue.x=distance;
                retValue.y=i;
            }
        }
        return retValue;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from GeoPoint (x,y) to the closest line
     * segment in the Poly (int[] xpts, int[] ypts).
     * <p>
     * This procedure assumes that xpts.length == ypts.length.
     * <p>
     *
     * @param latLng  location of the point
     * @return GeoPoint whose x is the closes distance, y is the index of the poly
     */
    public GeoPoint IndexOfClosestdistanceToPoly(GeoLatLng latLng) {
         double[] xpts=new double[latlngs.length];
         double[] ypts=new double[latlngs.length];
         for(int i=0;i<latlngs.length;i++){
             xpts[i]=latlngs[i].x;
             ypts[i]=latlngs[i].y;
         }
         return IndexOfClosestdistanceToPoly(latLng.x,latLng.y,xpts,ypts,false);
    }



    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 27AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns n or n+1 points along a line.
     * <p>
     *
     * @param pt1 point
     * @param pt2 point
     * @param n count
     * @param include_last boolean
     * @return GeoPoint[]
     */
    public final static GeoPoint[] lineSegments(GeoPoint pt1, GeoPoint pt2, int n,
                                             boolean include_last) {

        GeoPoint v = new GeoPoint(pt2.x - pt1.x, pt2.y - pt1.y);
        int end = include_last ? n + 1 : n;
        GeoPoint[] ret_val = new GeoPoint[end];
        double inc = 1f / (double) n;
        double t = inc;

        ret_val[0] = pt1;
        for (int i = 1; i < end; i++, t += inc) {
            ret_val[i] = new GeoPoint(pt1.x +  ((double) v.x * t), pt1.y
                    +  ((double) v.y * t));
        }
        return ret_val;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 27AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Find the closest point on a line from a given pt.
     * @param pt1 start point of the line.
     * @param pt2 end point of the line.
     * @param pt  distance to this point.
     * @param segmentClamp is segment or not.
     * @return the closet point on the line
     */
    public final static GeoPoint getClosetPoint(GeoPoint pt1, GeoPoint pt2,
            GeoPoint pt, boolean segmentClamp) {
        GeoPoint p1 = new GeoPoint( pt.x - pt1.x,pt.y - pt1.y);
        GeoPoint p2 = new GeoPoint(pt2.x - pt1.x,pt2.y - pt1.y);
        double ab2 = p2.x * p2.x + p2.y * p2.y;
        double ap_ab = p1.x * p2.x + p1.y * p2.y;
        double t = ap_ab / ab2;
        if (segmentClamp) {
            if (t < 0.0f) {
                t = 0.0f;
            } else if (t > 1.0f) {
                t = 1.0f;
            }

        }
        GeoPoint closestPt = new GeoPoint(pt1.x + p2.x * t,pt1.y + p2.y * t);
        return closestPt;

    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Distance to closest endpoint.
     * <p>
     *
     * @param x1 x coord
     * @param y1 y coord
     * @param x2 x coord
     * @param y2 y coord
     * @param x x coord of point
     * @param y y coord of point
     * @return double distance to endpoint
     */
    public final static double distanceToEndpoint(int x1, int y1, int x2,
                                                   int y2, int x, int y) {

        return (double) Math.min(distance(x1, y1, x, y), distance(x2, y2, x, y));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the distance from a point to a line segment.
     * <p>
     * Variable usage as follows:
     * <p>
     * <ul>
     * <li>x12 x distance from the first endpoint to the second.
     * <li>y12 y distance from the first endpoint to the second.
     * <li>x13 x distance from the first endpoint to point being
     * tested.
     * <li>y13 y distance from the first endpoint to point being
     * tested.
     * <li>x23 x distance from the second endpoint to point being
     * tested.
     * <li>y23 y distance from the second endpoint to point being
     * tested.
     * <li>D12 Length of the line segment.
     * <li>pp distance along the line segment to the intersection of
     * the perpendicular from the point to line extended.
     * </ul>
     *
     * Procedure:
     * <p>
     *
     * Compute D12, the length of the line segment. Compute pp, the
     * distance to the perpendicular. If pp is negative, the
     * intersection is before the start of the line segment, so return
     * the distance from the start point. If pp exceeds the length of
     * the line segment, then the intersection is beyond the end point
     * so return the distance of the point from the end point.
     * Otherwise, return the absolute value of the length of the
     * perpendicular line. The sign of the length of the perpendicular
     * line indicates whether the point lies to the right or left of
     * the line as one travels from the start point to the end point.
     * <p>
     *
     * @param x1 line x coord1
     * @param y1 line y coord1
     * @param x2 line x coord2
     * @param y2 line y coord2
     * @param x point x coord
     * @param y point y coord
     * @return double distance to line segment
     *
     */
    public final static double distanceToLine(double x1, double y1, double x2, double y2,
                                               double x, double y) {

        // algorithm courtesy of Ray 1/16/98
        double x12 = x2 - x1;
        double y12 = y2 - y1;
        double x13 = x - x1;
        double y13 = y - y1;
        double D12 = (double) Math.sqrt(x12 * x12 + y12 * y12);
        if(D12==0){
            return (double) Math.sqrt(x13 * x13 + y13 * y13);
        }
        double pp = (x12 * x13 + y12 * y13) / D12;
        if (pp < 0.0) {
            return (double) Math.sqrt(x13 * x13 + y13 * y13);
        }
        if (pp > D12) {
            double x23 = x - x2;
            double y23 = y - y2;
            return (double) Math.sqrt(x23 * x23 + y23 * y23);
        }
        return (double) Math.abs(((x12 * y13 - y12 * x13) / D12));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * 2D distance formula.
     * <p>
     *
     * @param x1 x coord
     * @param y1 y coord
     * @param x2 x coord
     * @param y2 y coord
     * @return double distance
     */
    public final static double distance(double x1, double y1, double x2, double y2) {
        double xdiff = x2 - x1;
        double ydiff = y2 - y1;
        return (double) Math.sqrt((xdiff * xdiff + ydiff * ydiff));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 22AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * 2D distance formula.
     * <p>
     *
     * @param x1 x coord
     * @param y1 y coord
     * @param x2 x coord
     * @param y2 y coord
     * @return double distance
     */
    public final static double distance(int x1, int y1, int x2, int y2) {
        int xdiff = x2 - x1;
        int ydiff = y2 - y1;
        return (double) Math.sqrt((double) (xdiff * xdiff + ydiff * ydiff));
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the number of vertices in the polyline.
     * @return  the number of vertices in the polyline.
     */
    public int getVertexCount() {
        if (latlngs != null) {
            return latlngs.length;
        }
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the vertex with the given index in the polyline.
     * @param index the index of the point.
     * @return  the vertex with the given index in the polyline.
     */
    public GeoLatLng getVertex(int index) {
        if (latlngs != null) {
            return latlngs[index];
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the level with the given index in the polyline.
     * @param index the index of the point.
     * @param level  the level with the given index in the polyline.
     */
    public void setLevel(int index,int level) {
        if (levels != null) {
            levels[index]=level;
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the level with the given index in the polyline.
     * @param index the index of the point.
     * @return  the level with the given index in the polyline.
     */
    public int getLevel(int index) {
        if (levels != null) {
            return levels[index];
        }
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the length (in meters) of the polyline along the surface of a
     * spherical Earth
     * @return  the length (in meters) of the polyline.
     */
    public int getLength() {
        int len = 0;
        if (latlngs != null) {
            double length = 0;
            GeoLatLng pt1;
            GeoLatLng pt2;
            pt1 = new GeoLatLng(latlngs[0].lat(), latlngs[0].lng());
            for (int i = 1; i < latlngs.length; i++) {
                pt2 = new GeoLatLng(latlngs[i].lat(), latlngs[i].lng());
                length += pt1.distanceFrom(pt2);
                pt1 = pt2;
            }
            len = (int) (length * 1000.0 + 0.5);
        }
        return len;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the length (in meters) of the polyline along the surface of a
     * spherical Earth
     * @param startIndex start point index;
     * @param endIndex  end point idnex(not included);
     * @return  the length (in meters) of the polyline between two point.
     */
    public int getLength(int startIndex, int endIndex) {
        int len = 0;
        if (latlngs != null && startIndex>=0 && endIndex<latlngs.length &&
                startIndex<endIndex) {
            double length = 0;
            GeoLatLng pt1;
            GeoLatLng pt2;
            pt1 = new GeoLatLng(latlngs[startIndex].lat(), latlngs[startIndex].lng());
            for (int i = startIndex+1; i < endIndex; i++) {
                pt2 = new GeoLatLng(latlngs[i].lat(), latlngs[i].lng());
                length += pt1.distanceFrom(pt2);
                pt1 = pt2;
            }
            len = (int) (length * 1000.0 + 0.5);
        }
        return len;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the bounds for this polyline.
     * @return  the bounds for this polyline.
     */
    public GeoLatLngBounds getBounds() {
        return bounds;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Hides the polyline.
     */
    public void hide() {
        visible = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shows the polyline.
     */
    public void show() {
        visible = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if the polyline is currently hidden. Otherwise returns false.
     * @return true if the polyline is currently hidden.
     */
    public boolean isHidden() {
        return !visible;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if GeoPolyline.hide() is supported
     * @return always true.
     */
    public boolean supportsHide() {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the array of points which consist of the line.
     * @param points array of points
     */
    public void setPoints(GeoLatLng[] points) {
        this.latlngs = points;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the array of points which consist of the line.
     * @return the points stored in the line
     */
    public GeoLatLng[] getPoints() {
        return latlngs;
    }

    /**
     * array store points in the polyline
     */
    private GeoLatLng[] latlngs;
    /**
     * Color of the polyline
     */
    private int color;
    /**
     * width of the polyline
     */
    private int weight;
    /**
     * Opacity of the polyline
     */
    private double opacity;

    /**
     * level for each point.
     */
    private int[] levels;
    /**
     * the bounds of the polyline
     */
    private GeoLatLngBounds bounds = null;
    /**
     * visible or not
     */
    private boolean visible = true;
}
