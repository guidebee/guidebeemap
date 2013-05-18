//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 12OCT2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// 12OCT2009   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * query cell id geo addresses.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public interface ICellIdGeocoder {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get location based on cell id information.
     * @param MMC MMC code
     * @param MNC MNC code.
     * @param LAC LAC code.
     * @param CID CID code.
     * @param listener callback when return the result.
     */
    public void getLocations(String MMC,String MNC,String LAC,String CID, IGeocodingListener listener);

}
