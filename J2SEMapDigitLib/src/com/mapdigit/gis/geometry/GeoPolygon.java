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
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Polygon on map.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class GeoPolygon {
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public GeoPolygon() {
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
     * @param polygon polygon object copied from.
     */
    public GeoPolygon(GeoPolygon polygon) {
        if (polygon.latlngs != null) {
            this.latlngs = new GeoLatLng[polygon.latlngs.length];
            System.arraycopy(latlngs, 0, this.latlngs, 0, latlngs.length);
            levels = new int[polygon.levels.length];
            for (int i = 0; i < levels.length; i++) {
                levels[i] = polygon.levels[i];
            }
            this.bounds = new GeoLatLngBounds(polygon.bounds);
        }
        this.strokeColor = polygon.strokeColor;
        this.strokeOpacity = polygon.strokeOpacity;
        this.strokeWeight = polygon.strokeWeight;
        this.fillColor = polygon.fillColor;
        this.fillOpacity = polygon.fillOpacity;
        zoomFactor = polygon.zoomFactor;
        numLevels = polygon.numLevels;
        visible = polygon.visible;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a polygon from an array of vertices.  The weight is the width of
     * the line in pixels. The opacity is given as a number between 0 and 1.
     * The line will be antialiased and semitransparent.
     * @param latlngs array of points.
     * @param strokeColor the color of the polygon stroke.
     * @param strokeWeight the width of the polygon stroke.
     * @param strokeOpacity the opacity of the polygon stroke.
     * @param fillColor the inner color of the polygon.
     * @param fillOpacity the inner opacity of the polygon.
     */
    public GeoPolygon(GeoLatLng[] latlngs, int strokeColor, int strokeWeight,
            double strokeOpacity, int fillColor, double fillOpacity) {
        if (latlngs != null) {
            this.latlngs = new GeoLatLng[latlngs.length];
            System.arraycopy(latlngs, 0, this.latlngs, 0, latlngs.length);
            levels = new int[latlngs.length];
            for (int i = 0; i < levels.length; i++) {
                levels[i] = 0;
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
        this.strokeColor = strokeColor;
        this.strokeOpacity = strokeOpacity;
        this.strokeWeight = strokeWeight;
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
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
     * Creates a polygon from encoded strings of aggregated points and levels.
     * zoomFactor and  numLevels  these two values determine the precision
     * of the levels within an encoded polygon.
     * @param strokeColor the color of the polygon.
     * @param strokeWeight width of the line in pixels.
     * @param strokeOpacity the opacity of the polygon.
     * @param fillColor the inner color of the polygon.
     * @param fillOpacity the inner opacity of the polygon.
     * @param points a string containing the encoded latitude and longitude
     *  coordinates.
     * @param zoomFactor  the magnification between adjacent sets of zoom levels
     * in the encoded levels string.
     * @param levels a string containing the encoded polygon zoom level groups.
     * @param numLevels the number of zoom levels contained in the encoded levels string.
     * @return Geo polygon object.
     */
    public static GeoPolygon fromEncoded(int strokeColor, int strokeWeight,
            double strokeOpacity, int fillColor, double fillOpacity,
            String points, int zoomFactor, String levels, int numLevels) {
        Vector trk = PolylineEncoder.createDecodings(points);
        GeoLatLng[] array = new GeoLatLng[trk.size()];
        trk.copyInto(array);
        GeoPolygon polygon = new GeoPolygon(array, strokeColor, strokeWeight, strokeOpacity,
                fillColor, fillOpacity);
        polygon.levels = PolylineEncoder.decodeLevel(levels);
        polygon.zoomFactor = zoomFactor;
        polygon.numLevels = numLevels;
        return polygon;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the number of vertices in the polygon.
     * @return  the number of vertices in the polygon.
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
     * Returns the vertex with the given index in the polygon.
     * @param index the index of the point.
     * @return  the vertex with the given index in the polygon.
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
     * Returns the bounds for this polygon. 
     * @return  the bounds for this polygon. 
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
     * Hides the polygon. 
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
     * Shows the polygon. 
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
     * Returns true if the polygon is currently hidden. Otherwise returns false.
     * @return true if the polygon is currently hidden.
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
     * Returns true if GeoPolygon.hide() is supported 
     * @return always is true.
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
     * array store points in the polygon
     */
    private GeoLatLng[] latlngs;

    /**
     * stroke color of the polygon
     */
    private int strokeColor;

    /**
     * stroke width of the polygon
     */
    private int strokeWeight;

    /**
     * stroke opacity of the polygon
     */
    private double strokeOpacity;

    /**
     * fill color
     */
    private int fillColor;

    /**
     * fill opacity of the polygon
     */
    private double fillOpacity;

    /**
     * Zoom factor
     */
    private int zoomFactor;

    /**
     * total zoom level, default 18
     */
    private int numLevels;

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
