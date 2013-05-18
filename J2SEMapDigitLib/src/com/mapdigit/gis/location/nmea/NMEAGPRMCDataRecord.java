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
 * NEMA Data record for GPRMC sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEAGPRMCDataRecord extends NMEADataRecord{

    /**
     * Time stamp.
     */
    public Date timeStamp=null;

    /**
     * Status.
     */
    public boolean status=false;

    /**
     * Latitude.
     */
    public double latitude;

    /**
     * Longitude.
     */
    public double longitude;

    /**
     * speed over ground.
     */
    public double speed;

    /**
     * course over ground, degree true.
     */
    public double course;

    /**
     * Magnetic variation.
     */
    public double magneticCourse;


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
    public NMEAGPRMCDataRecord() {
        recordType=TYPE_GPRMC;
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
        return "Time:" + timeStamp.toString() + "\n" +
                "Latitude:" + latitude + "\n" +
                "Longitude:" + longitude + "\n" +
                "Course:" + course + "\n" +
                "Magnetic Course:" + magneticCourse + "\n" +
                "Speed:" + speed + "\n" +
                "status:"+status +"\n";

    }
}

