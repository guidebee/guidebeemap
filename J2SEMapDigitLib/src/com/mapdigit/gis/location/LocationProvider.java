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

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 06MAR2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This is the starting point for applications using location information in
 * this API and represents a source of the location information.
 * A VirtualGPSDevice represents a vitural location-providing module, generating
 * Locations.
 * Applications obtain VirtualGPSDevice instances (classes implementing the
 * actual functionality by extending this abstract class) by calling the one
 * of the factory methods.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 06/03/09
 * @author      Guidebee Pty Ltd.
 */
public abstract class LocationProvider {

    /**
     * Availability status code: the location device is available.
     */
    public static final int AVAILABLE=0;

    /**
     * Availability status code: the location device is out of service. Being
     * out of service means that the method is unavailable and the
     * implementation is not able to expect that this situation would change
     * in the near future. An example is when using a location method
     * implemented in an external device and the external device is detached.
     */
    public static final int OUT_OF_SERVICE=1;

    /**
     * Availability status code: the location device is temporarily unavailable.
     * Temporary unavailability means that the method is unavailable due to
     * reasons that can be expected to possibly change in the future and the
     * provider to become available. An example is not being able to receive
     * the signal because the signal used by the location method is currently
     * being obstructed, e.g. when deep inside a building for satellite based
     * methods. However, a very short transient obstruction of the signal
     * should not cause the provider to toggle quickly between
     * TEMPORARILY_UNAVAILABLE and AVAILABLE.
     */
    public static final int TEMPORARILY_UNAVAILABLE=2;



    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the last known location that the implementation has. This is the
     * best estimate that the implementation has for the previously known
     * location.
     * Applications can use this method to obtain the last known location and
     * check the timestamp and other fields to determine if this is recent
     * enough and good enough for the application to use without needing to make
     * a new request for the current location.
     * @return a Location object, null is returned if the implementation does
     * not have any previous location information.
     */
    public Location getLastKnownLocation(){
        return currentLocation;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Retrieves a Location associated with this class. If no result could be
     * retrieved, a LocationException is thrown. If the location can not be
     * determined within the timeout period specified in the parameter,
     * the method shall throw a LocationException.
     * If the device is temporarily unavailable, the implementation shall wait
     * and try to obtain the location until the timeout expires. If the provider
     * is out of service, then the LocationException is thrown immediately.
     * @param timeout a timeout value in seconds, -1 is used to indicate that
     * the implementation shall use its default timeout value for this device.
     * return a Location object
     */
    public Location getLocation(int timeout)
                     throws LocationException,
                            java.lang.InterruptedException{
        synchronized(syncObject){
            getOneFix=true;
            if(timeout!=-1){
                this.locationTimeout=timeout;
            }
            syncObject.wait(1);
            getOneFix=false;
            return currentLocation;
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the current state of this LocationProvider. The return value
     * shall be one of the availability status code constants defined in
     * VirtualGPSDevice class.
     * @return the availability state of this device.
     */
    public int getState(){
        return currentState;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a LocationListener for updates at the defined interval. The listener
     * will be called with updated location at the defined interval. The
     * listener also gets updates when the availablilty state of the
     * Device changes.
     * Passing in -1 as the interval selects the default interval which is
     * dependent on the used location method.
     * Only one listener can be registered with each LocationProvider instance.
     * Setting the listener replaces any possibly previously set listener.
     * Setting the listener to null cancels the registration of any previously
     * set listener.
     * @param listener The listener to be registered. If set to null the
     * registration of any previously set listener is cancelled.
     * @param interval The interval in seconds. -1 is used for the default
     * interval of this provider.
     * @param timeout Timeout value in seconds, must be greater than 0.
     * If the value is -1, the default timeout for this provider is used.
     * @param maxAge  Maximum age of the returned location in seconds,must
     * be greater than 0 or equal to -1 to indicate that the default maximum
     * age for this provider is used.
     */
    public void setLocationListener(ILocationListener listener,
                                int interval,
                                int timeout,
                                int maxAge){
        synchronized(syncObject){
            this.locationListener=listener;
            if(interval!=-1){
                this.locationInterval=interval;
            }
            if(timeout!=-1){
                this.locationTimeout=timeout;
            }
            if(maxAge!=-1){
                this.locationMaxAge=maxAge;
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return last known orientation.
     * @return last known orientation.
     */
    public Orientation getOrientation() {
        return currentOrientation;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the location provider
     */
    public abstract void reset();

    /**
     * interval.
     */
    protected int locationInterval=1;

    /**
     * time out for location.
     */
    protected int locationTimeout=30;

    /**
     * max age for location.
     */
    protected int locationMaxAge=300;

    /**
     * current device status.
     */
    public int currentState=OUT_OF_SERVICE;

    /**
     * current location.
     */
    public final Location currentLocation=new Location();

    /**
     * current Orientation.
     */
    protected Orientation currentOrientation=null;

    /**
     * the location listener.
     */
    public ILocationListener locationListener=null;

    /**
     * Get one fix or not.
     */
    protected boolean getOneFix=false;

    /**
     * sync object.
     */
    protected final Object syncObject=new Object();

    /**
     * previous fix time;
     */
    protected long previousFixtime=0;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor to help implementations and extensions. This is not
     * intended to be used by applications. Applications should not make
     * subclasses of this class and invoke this constructor from the subclass.
     */
    protected LocationProvider() {
    }

}
