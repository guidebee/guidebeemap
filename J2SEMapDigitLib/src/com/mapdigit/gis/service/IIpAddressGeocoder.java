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
package com.mapdigit.gis.service;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * query ip address geo addresses.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public interface IIpAddressGeocoder {
    
    public final static String LOCAL_ADDRESS="127.0.0.1";

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to Google servers to geocode the specified address
     * @param ipAddress  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(String ipAddress, IIpAddressGeocodingListener listener);

}
