//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 06MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Date;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 06MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The Location class represents the standard set of basic location information.
 * This includes the timestamped coordinates, accuracy, speed, course, and
 * information about the positioning method used for the location.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 08/03/09
 * @author      Guidebee Pty Ltd.
 */
public class Location {

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
     * Altitude.
     */
    public double altitude;

    /**
     * speed over ground.
     */
    public double speed;

    /**
     * course over ground, degree true.
     */
    public double course;

    /**
     * PDOP.
     */
    public double PDOP;

    /**
     * HDOP.
     */
    public double HDOP;

    /**
     * VDOP.
     */
    public double VDOP;

    /**
     * Status.
     */
    public boolean status=false;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * A protected constructor for the Location to allow implementations of
     * LocationProviders to construct the Location instances.
     * This method is not intended to be used by applications.
     * This constructor sets the fields to implementation specific default
     * values. Location Devices are expected to set the fields to the correct
     * values after constructing the object instance.
     */
    public Location() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the terminal's course made good in degrees relative to true
     * north. The value is always in the range [0.0,360.0) degrees.
     * @return the terminal's course made good in degrees relative to true
     * north or Float.NaN if the course is not known.
     */
    public double getCourse(){
        return this.course;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the coordinates of this location and their accuracy.
     * @return a QualifiedCoordinates object, if the coordinates are not known,
     * returns null.
     */
    public QualifiedCoordinates getQualifiedCoordinates(){
        QualifiedCoordinates qualifiedCoordinates=
                new QualifiedCoordinates(latitude,
                longitude,altitude,HDOP,VDOP);
        return qualifiedCoordinates;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the terminal's current ground speed in meters per second (m/s)
     * at the time of measurement. The speed is always a non-negative value.
     * Note that unlike the coordinates, speed does not have an associated
     * accuracy because the methods used to determine the speed typically are
     * not able to indicate the accuracy.
     * @return the current ground speed in m/s for the terminal or Float.NaN
     * if the speed is not known.
     */
    public double getSpeed(){
        return this.speed;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the timestamp at which the data was collected. This timestamp
     * should represent the point in time when the measurements were made.
     * Implementations make best effort to set the timestamp as close to this
     * point in time as possible. The time returned is the time of the local
     * clock in the terminal in milliseconds using the same clock and same time
     * representation as System.currentTimeMillis().
     * @return a timestamp representing the time.
     */
    public long getTimestamp(){
        return this.timeStamp.getTime();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns whether this Location instance represents a valid location with
     * coordinates or an invalid one where all the data, especially the latitude
     * and longitude coordinates, may not be present.
     * @return a boolean value with true indicating that this Location instance
     * is valid.
     */
    public boolean isValid(){
        return this.status;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29AUG2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy from anthoer location object
     * @param location object to copy from.
     */
    public void copy(Location location){
        this.HDOP=location.HDOP;
        this.PDOP=location.PDOP;
        this.VDOP=location.VDOP;
        this.altitude=location.altitude;
        this.course=location.course;
        this.latitude=location.latitude;
        this.longitude=location.longitude;
        this.speed=location.speed;
        this.status=location.status;
        this.timeStamp=location.timeStamp;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29AUG2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * @return string format.
     */
    public String toString(){
        String strRet="";
        strRet+="Latitude="+ this.latitude +"\n";
        strRet+="Longitude="+ this.longitude +"\n";
        strRet+="Altitude="+ this.altitude +"\n";
        strRet+="Speed="+ this.speed +"\n";
        strRet+="Course="+ this.course +"\n";
        strRet+="Datetime="+ this.timeStamp.toString() +"\n";
        return strRet;
    }
}
