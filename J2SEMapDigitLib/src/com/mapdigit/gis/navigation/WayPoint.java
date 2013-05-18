//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.navigation;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19SEP2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * define a navigation way point, it defines the name and Lat/Lng pair.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 19/09/09
 * @author      Guidebee Pty Ltd.
 */
public class WayPoint {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param name name of the way point.
     * @param comment some comments.
     * @param latLng latitude and longitude of the way point
     */
    public WayPoint(String name, String comment, GeoLatLng latLng) {
        this.name = name;
        this.comment = comment;
        this.latLng.x = latLng.x;
        this.latLng.y = latLng.y;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param latLng latitude and longitude of the way point
     */
    public WayPoint(GeoLatLng latLng) {
        this("", "", latLng);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param name some comments.
     * @param latLng latitude and longitude of the way point
     */
    public WayPoint(String name, GeoLatLng latLng) {
        this(name, "", latLng);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the name of the way point.
     * @return name of the way point.
     */
    public String getName() {
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get comment of the way point
     * @return the comment of the way point.
     */
    public String geComment() {
        return comment;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the latlng of the way point.
     * @return the lat/lng pair of the way point.
     */
    public GeoLatLng geLatLng() {
        return latLng;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the name of the way point.
     * @param name new name of the way point.
     */
    public void setName(String name) {
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set a comment for the way point.
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the lat/lng pair for the way point.
     * @param latLng
     */
    public void setLatLng(GeoLatLng latLng) {
        this.latLng.x = latLng.x;
        this.latLng.y = latLng.y;
    }

    public String toString(){
        return name;
    }

    /**
     * name of the way point.
     */
    private String name;
    /**
     * some comment about the way point.
     */
    private String comment;
    /**
     * latitude and longitude of the way point.
     */
    private final GeoLatLng latLng = new GeoLatLng();
}
