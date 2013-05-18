//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Objects of this class store information about a single route in a 
 * directions result. it should not directly create objects of this class. 
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapRoute extends MapObject{
    
    /**
     * Steps included in this route.
     */
    public MapStep []steps;
    
    /**
     * Start point of this route.
     */
    public MapPoint startGeocode;
    
    /**
     * end point of this route.
     */
    public MapPoint endGeocode;
    
    /**
     * last position of the route.
     */
    public GeoLatLng lastLatLng;
    
    /**
     * Summary of this route.
     */
    public String summary;
    
    /**
     * total distance of the route in meters.
     */
    public double distance;
    
    /**
     * total duration of the route in seconds.
     */
    public double duration;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    MapRoute() {
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new MapStep object.
     * @return a new MapStep object.
     */
    public static MapStep newStep(){
        return new MapStep();
    }
   
}
