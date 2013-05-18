//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service;

//[------------------------------ MAIN CLASS ----------------------------------]
import com.mapdigit.gis.geometry.GeoLatLng;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to query driving directions.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public interface IDirectionQuery {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method issues a new directions query. The query parameter is
     * a string containing any valid directions query,
     * @param waypoints the directions query string
     * @param listener the routing listener.
     */
    public void getDirection(GeoLatLng[] waypoints, IRoutingListener listener);
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method issues a new directions query. The query parameter is
     * a string containing any valid directions query,
     * e.g. "from: Seattle to: San Francisco" or
     * "from: Toronto to: Ottawa to: New York".
     * or the longitude,latitude list depends on which map server.
     * @param query the directions query string
     * @param listener the routing listener.
     * @deprecated use getDirection(GeoLatLng[] waypoints, IRoutingListener listener).
     */
    public void getDirection(String query, IRoutingListener listener);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method issues a new directions query. The query parameter is
     * a string containing any valid directions query,
     * e.g. "from: Seattle to: San Francisco" or
     * "from: Toronto to: Ottawa to: New York".
     * @param mapType the map type.
     * @param query the directions query string
     * @param listener the routing listener.
     */
    public void getDirection(int mapType,String query, IRoutingListener listener);

}
