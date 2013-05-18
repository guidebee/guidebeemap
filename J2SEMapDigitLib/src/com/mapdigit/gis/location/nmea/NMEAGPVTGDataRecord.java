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

//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * NEMA Data record for GPVTG sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEAGPVTGDataRecord extends NMEADataRecord{

    /**
     * Course ,true .
     */
    public double course;

    /**
     * course, magnetic.
     */
    public double courseMagnetic;

    /**
     * speed in knot.
     */
    public double speedKnot;

    /**
     * speed in km.
     */
    public double speedKm;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     */
    public NMEAGPVTGDataRecord() {
        recordType=TYPE_GPVTG;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return string of GPRMC.
     * @return a string
     */
    public String toString(){
         return "Course:" + course + "\n" +
                "Magnetic Course:" + courseMagnetic + "\n" +
                "SpeedKm" + speedKm + "\n" +
                "SpeedKnot:"+speedKnot +"\n";
    }
}