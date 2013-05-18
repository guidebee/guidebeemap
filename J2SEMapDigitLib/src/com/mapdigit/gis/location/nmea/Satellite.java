//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 08MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location.nmea;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 08MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * GPS Satellite information.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 08/03/09
 * @author      Guidebee Pty Ltd.
 */
public final class Satellite {
    
    /**
     * the PRN of the satellite.
     */
    public int id=0;
    
    /**
     * the SNR of the satellite.
     */
    public int snr=0;
    
    /**
     * The Elevation, in degree, maxiume 90.
     */
    public int elevation=0;
    
    /**
     * The azimuth of the satellite. true from 000 to 359.
     */
    public int azimuth=0;
    
    /**
     * is the satellite active.
     */
    public boolean active=true;
    
    
    Satellite() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return string of Satellite.
     * @return a string
     */
    public String toString(){
        return "Satellite No:" + id + "\n" +
                "Azimuth:" + azimuth + "\n" +
                "Elevation:" + elevation + "\n"+
                "SNR:" + snr+"\n";

    }
    
    
}
