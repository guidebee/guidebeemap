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

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Current position on the route (nearest).
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 19/09/09
 * @author      Guidebee Pty Ltd.
 */
public class WalkOnRoute {

    /**
     * navigation route.
     */
    public MapDirection mapDirection;

    /**
     * index of the route.
     */
    public int routeIndex;

    /**
     * index of the step of the route.
     */
    public int stepIndex;

    /**
     * point index of the polyline.
     */
    public int pointIndex;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    WalkOnRoute(){

    }

}
