//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
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
 * NEMA Data record for GPGGA sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEAGPGGADataRecord extends NMEADataRecord{

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
     * Receiver mode.
     */
    public int receiverMode;

    /**
     * number Of Satellites used in fix.
     */
    public int numberOfSatellites;

    /**
     * Horizontal dilution of precision.
     */
    public double HDOP;

    /**
     * Altitude.
     */
    public double altitude;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     *
     */
    public NMEAGPGGADataRecord() {
        recordType=TYPE_GPGGA;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return string of GPGGA.
     * @return a string
     */
    public String toString(){
        return "Time:" + timeStamp.toString() + "\n" +
                "Latitude:" + latitude + "\n" +
                "Longitude:" + longitude + "\n" +
                "Quality indicator:" + receiverMode + "\n" +
                "Number of Satellite:" + numberOfSatellites + "\n" +
                "HDOP:"+HDOP +"\n";
    }
}
