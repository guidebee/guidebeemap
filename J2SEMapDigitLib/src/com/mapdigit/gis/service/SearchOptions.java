//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 15AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to store driving directions options
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 15/08/09
 * @author      Guidebee Pty Ltd.
 */
public class SearchOptions {
    
    /**
     * route for car driving
     */
    public final static int ROUTE_TYPE_DRIVING=0;

    /**
     * route for walking
     */
    public final static int ROUTE_TYPE_WALKING=1;

    /**
     * route for bus /train
     */
    public final static int ROUTE_TYPE_COMMUTING=2;
   
    /**
     * KM
     */
    public final static int ROUTE_UNIT_KM=0;

    /**
     * Mile.
     */
    public final static int ROUTE_UNIT_MILE=1;

    /**
     * Routint type. default is for car drving.
     */
    public int RoutingType=ROUTE_TYPE_DRIVING;

    /**
     * Route unit.
     */
    public int RouteUnit=ROUTE_UNIT_KM;

    /**
     * language id ,default is en-US.
     */
    public String LanguageID="en-US";
    
    /**
     * Avoid high way or not.
     */
    public boolean AvoidHighway=false;

    /**
     * Avoid toll way or not.
     */
    public boolean AvoidTollway=false;

    /**
     * default number of search result.
     */
    public int NumberOfSearchResult=10;
}
