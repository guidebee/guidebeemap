//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.navigation;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.location.ILocationListener;
import com.mapdigit.gis.location.Location;
import com.mapdigit.gis.location.LocationProvider;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19SEP2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Navigation listener.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 19/09/09
 * @author      Guidebee Pty Ltd.
 */
public interface INavigationListener extends ILocationListener{

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Called by the VirtualGPSDevice to which this listener is registered.
     * This method will be called periodically according to the interval defined
     * when registering the listener to provide updates of the current location.
     * @param device  the source of the event.
     * @param rawLocation the raw location to which the event relates, i.e. the
     * new position direction from Location Provider.
     * @param adjustLocation the adjust location to which the event relates,it may be
     * adjusted to put on the map direction.
     */
    void locationUpdated(LocationProvider device, Location rawLocation,
            Location adjustLocation);


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Navigation is done.
     */
    void navigationDone();


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Navigation status change happend.
     * @param oldStatus old status
     * @param newStatus new status.
     */
    void statusChange(int oldStatus, int newStatus);


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * rerouting is done.
     * @param query message query context(string).
     * @param result the result object.
     */
    public void reroutingDone(final String query, final MapDirection result);


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * rerouing progress progress notification.
     * @param bytes the number of bytes has been read.
     * @param total total bytes to be read.Total will be zero if not available
     * (content-length header not set)
     */
    public void reroutingProgress(final int bytes, final int total);

}
