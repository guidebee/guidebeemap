//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location.nmea;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Date;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * NEMA Data record for GPGLL sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEAGPGLLDataRecord extends NMEADataRecord{

    /**
     * Time stamp.
     */
    public Date timeStamp=null;

    /**
     * Latitude.
     */
    public double latitude;

    /**
     * Longitude.
     */
    public double longitude;

    /**
     * status: A= Data valid , V= data not valid.
     */
    public boolean status;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     */
    public NMEAGPGLLDataRecord() {
        recordType=TYPE_GPGLL;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return string of GPGLL.
     * @return a string
     */
    public String toString(){
        return "Time:" + timeStamp.toString() + "\n" +
                "Latitude:" + latitude + "\n" +
                "Longitude:" + longitude + "\n" +
                "Status:" + status + "\n";
    }
}