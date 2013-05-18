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
import java.util.Date;
import com.mapdigit.gis.geometry.GeoLatLngBounds;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Base class of all map objects.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public abstract class MapObject {
    
    /**
     * Unkown Object type.
     */
    public static final byte UNKOWN = -1;
    
    /**
     * None Object type.
     */
    public static final byte NONE = 0;
    
    /**
     * Point Object type.
     */
    public static final byte POINT = 1;
    
    /**
     * multi point Object type.
     */
    public static final byte MULTIPOINT = 2;
    
    /**
     * Pline Object type.
     */
    public static final byte PLINE = 3;
    
    /**
     * multi pline Object type.
     */
    public static final byte MULTIPLINE = 4;
    
    /**
     * region Object type.
     */
    public static final byte REGION = 5;
    
    
    /**
     * multi region Object type.
     */
    public static final byte MULTIREGION = 6;
    
    
    /**
     * Collection Object type.
     */
    public static final byte COLLECTION = 7;
    
    /**
     * text Object type.
     */
    public static final byte TEXT = 8;
    
    /**
     * ROAD Object type.
     */
    public static final byte ROAD = 15;
    
    /**
     * Direction Object type.
     */
    public static final byte DIRECTION = 16;
    
    /**
     * Route Object type.
     */
    public static final byte ROUTE = 17;
    
    /**
     * route step Object type.
     */
    public static final byte ROUTE_STEP = 18;
    
    /**
     * The MapInfo ID of the map object.
     */
    public int mapInfo_ID;
    
    /**
     * The name of the map object.
     */
    public String name;

    /**
     * any note related to this object
     */
    public String objectNote;
    
    /**
     * The out bound of the map object.
     */
    public GeoLatLngBounds bounds;
    
    /**
     * The type of the map object.
     */
    protected int mapObjectType;
    
    /**
     * The Time for cache
     */
    public Date cacheAccessTime;
    
    /**
     * indicate Highlighted or not
     */
    public boolean highlighted=false;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param mapObject map object copy from.
     */
    public MapObject(MapObject mapObject) {
        mapInfo_ID = mapObject.mapInfo_ID;
        name = mapObject.name;
        bounds = new GeoLatLngBounds(mapObject.bounds);
        mapObjectType = mapObject.mapObjectType;
        cacheAccessTime = mapObject.cacheAccessTime;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constuctor.
     */
    public MapObject() {
        mapObjectType = MapObject.UNKOWN;
        mapInfo_ID = -1;
        name = "Unknown";
        bounds = new GeoLatLngBounds();
        cacheAccessTime = new Date();
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the mapinfo ID of the map object.
     * @return the mapinfo ID of the map object
     */
    public int getMapInfo_ID() {
        return mapInfo_ID;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the mapinfo ID of the map object.
     *
     * @param id the mapinfo ID
     */
    public void setMapInfo_ID(int id) {
        mapInfo_ID = id;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the name of the map object.
     * @return the name of the map object
     */
    public String getName() {
        return name;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the note of the map object.
     * @return the note of the map object
     */
    public String getNote() {
        return objectNote;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the name of the map object.
     * @param name the name of the map object.
     */
    public void setName(String name) {
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the note of the map object.
     * @param note the note of the map object.
     */
    public void setNote(String note) {
        this.objectNote = note;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the out bound of the map object.
     * @return the out bound
     */
    public GeoLatLngBounds getBound() {
        return bounds;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the out bound of the map object.
     * @param bounds   the out bound
     */
    public void setBound(GeoLatLngBounds bounds) {
        bounds = new GeoLatLngBounds( bounds );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the out type of the map object.
     * @return the type
     */
    public int getType() {
        return mapObjectType;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the type of the map object.
     * @param type  the type of the map object.
     */
    public void setType(int type) {
        mapObjectType = type;
    }

    /**
     * Carriage return.
     */
    protected static final String CRLF="\n";
    
    
}
