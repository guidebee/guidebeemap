//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 10NOV2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.navigation;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Vector;

import com.mapdigit.util.Log;

import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapDirectionCommandElement;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.geometry.GeoPolyline;
import com.mapdigit.gis.location.ILocationListener;
import com.mapdigit.gis.location.Location;
import com.mapdigit.gis.location.LocationProvider;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IRoutingListener;
import com.mapdigit.gis.service.google.GoogleMapService;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 10NOV2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Navigation Engine. it include 3 threads , 1) location monitor thread used to
 * monitor current location againest current navigation route if there's one
 * and ajust raw location to the nearest position on the route. 2) voice command
 * generate thread create upcoming voice command based on current step and
 * location and put them to the voice command queue. 3) voice command processor
 * pick up the voice command from the queue and notify the voice command listener.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 10/11/09
 * @author      Guidebee Pty Ltd.
 */
public class NavigationEngine extends LocationProvider {

    /**
     * idle status.
     */
    public final static int STATUS_IDLE = 0;
    /**
     * routing status.
     */
    public final static int STATUS_ROUTING_START = 1;
    /**
     * routing status.
     */
    public final static int STATUS_ROUTING_END = 2;
    /**
     * navigating status on road mode
     */
    public final static int STATUS_NAVIGATING_ON_ROAD_MODE = 3;

    /**
     * navigating status on road mode preparation (starting point)
     */
    public final static int STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION = 4;


    /**
     * navigating status on road mode (near destination)
     */
//    private final static int STATUS_NAVIGATING_ON_ROAD_MODE_NEAR_DESTINATION = 5;

    /**
     * navigating status off road mode.
     */
    public final static int STATUS_NAVIGATING_OFF_ROAD_MODE = 6;

    /**
     * deviation status.
     */
    public final static int STATUS_DEVIATION = 7;

    /**
     * navigation engine paused.
     */
    public final static int STATUS_PAUSED = 10;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 10NOV2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param rawLocationProvider raw location provider. the raw location provider
     * refer the GPS devices(either internal or bluetooth etc).
     */
    public NavigationEngine(LocationProvider rawLocationProvider) {
        this(rawLocationProvider, new GoogleMapService());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param rawLocationProvider raw location provider.
     * @param digitalMapService digital map service.
     */
    public NavigationEngine(LocationProvider rawLocationProvider,
            DigitalMapService digitalMapService){
        if(rawLocationProvider==null || digitalMapService==null){
            throw new IllegalArgumentException("Argument cannot be null");
        }
        this.rawLocationProvider=rawLocationProvider;
        this.digitalMapService=digitalMapService;
        currentLocationProvider=rawLocationProvider;
        pauseThread=true;
        stopThread=true;
        //location monitor intercepts location update from location provider
        currentLocationProvider.setLocationListener(locationMonitor, 1, -1, -1);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * cancel current navigation
     */
    public void cancelNavigation() {
        synchronized (syncObject) {
            if (engineStatus == STATUS_NAVIGATING_ON_ROAD_MODE ||
                 engineStatus ==   STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION) {
                wayPoints.removeAllElements();
                mapDirection = null;
                engineStatus = STATUS_IDLE;
                pause();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Start navigation.
     * @param mapDirection the map direction used to navigation,it can be null,
     * if it's null,navigation engine will try to use digital map service to
     * find the diection from current location to all it's first way point.
     * the last wayPoint is considered as the destination.
     * @param wayPointArray way point array.
     */
    public void startNavigation(MapDirection mapDirection,
            WayPoint[] wayPointArray) {
        synchronized (syncObject) {
            if (wayPointArray == null) {
                throw new IllegalArgumentException("wayPointArray cannot be null");
            }
            //cancel previous navigation.
            if(this.mapDirection!=null){
                cancelNavigation();
                resume();
            }
            wayPoints.removeAllElements();
            for (int i = 0; i < wayPointArray.length; i++) {
                wayPoints.addElement(wayPointArray[i]);
            }
            this.mapDirection = mapDirection;
            currentWalkOnRoute.mapDirection=mapDirection;
            currentWalkOnRoute.pointIndex=-1;
            currentWalkOnRoute.routeIndex=-1;
            currentWalkOnRoute.stepIndex=-1;
            try {
                Location location = currentLocationProvider.getLocation(10);
                currentMonitorLocation.copy(location);
                currentLatLng.x=currentMonitorLocation.longitude;
                currentLatLng.y=currentMonitorLocation.latitude;
                previousLatLng.x=currentLatLng.x;
                previousLatLng.y=currentLatLng.y;
                locationMonitor.initialize();

            } catch (Exception e) {
            }
            locationMonitor.flushCommandQueue();
            voiceCommandGenerator.nearestIndex=-1;
            if (mapDirection == null) {
                //if the map direction is null and allow rerouting
                //then go to routing start status
                //otherwise in off road navigation mode.
                if (allowRerouting) {
                    firstRouting=true;
                    engineStatus = STATUS_ROUTING_START;
                } else {
                    engineStatus = STATUS_NAVIGATING_OFF_ROAD_MODE;
                    int lastIndex=wayPointArray.length;
                    destinationLatLng.x=wayPointArray[lastIndex-1].geLatLng().x;
                    destinationLatLng.y=wayPointArray[lastIndex-1].geLatLng().y;
                }
            } else {
                firstRouting=false;
                engineStatus = STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION;
                destinationLatLng.x=mapDirection.polyline
                        .getVertex(mapDirection.polyline.getVertexCount()-1).x;
                destinationLatLng.y=mapDirection.polyline
                        .getVertex(mapDirection.polyline.getVertexCount()-1).y;
            }
            if (navigationListener != null) {
                navigationListener.statusChange(STATUS_PAUSED, engineStatus);
            }

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the routing listener.
     * @param listener routing listener.
     */
    public void setNavigationListener(INavigationListener listener){
        setNavigationListener(listener, 1, -1, -1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the voice command listener.
     * @param listener voice command listener.
     */
    public void setVoiceCommandListener(IVoiceCommandListener listener){
        voiceCommandListener=listener;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
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
    public void setNavigationListener(INavigationListener listener,
                                int interval,
                                int timeout,
                                int maxAge){

        navigationListener=listener;
        super.setLocationListener(navigationListener, interval,timeout, maxAge);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Start simualtion
     * @param mapDirectionSimu map direction object, cannot be null.
     * @param speed  simulation driving speed  in kph or mile/h
     * @param isMile is mile or not.
     */
    public void startSimulation(MapDirection mapDirectionSimu,int speed,
            boolean isMile) {
        synchronized (syncObject) {
            //cancel previous navigation.
            if(this.mapDirection!=null){
                cancelNavigation();
                resume();
            }
            currentLocationProvider.setLocationListener(null, 1, 1, 1);
            currentLocationProvider = simulatedDirectionLocationProvider;
            simulatedDirectionLocationProvider.stopSimulation();
            currentLocationProvider
                    .setLocationListener(locationMonitor, 1, -1, -1);
            simulatedDirectionLocationProvider
                    .startSimulation(mapDirectionSimu, speed, isMile);

        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * pause simulation.
     */
    public void pauseSimulation() {
        synchronized (syncObject) {
            if (currentLocationProvider == simulatedDirectionLocationProvider) {
                simulatedDirectionLocationProvider.pauseSimulation();
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * resume simulation.
     */
    public void resumeSimulation() {
        synchronized (syncObject) {
            if (currentLocationProvider == simulatedDirectionLocationProvider) {
                simulatedDirectionLocationProvider.resumeSimulation();
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * force to stop simulation.
     */
    public void stopSimulation(){
        synchronized (syncObject) {
            if (currentLocationProvider == simulatedDirectionLocationProvider) {
                currentLocationProvider.setLocationListener(null, 1, 1, 1);
                simulatedDirectionLocationProvider.stopSimulation();
                currentLocationProvider = rawLocationProvider;
                currentLocationProvider.setLocationListener(locationMonitor,
                        1, -1, -1);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Allow rerouting or not.
     * @param allow true, allow rerouting.
     */
    public void setAutoRerouting(boolean allow){
        allowRerouting=allow;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the distance unit. either use kilometer/meter or mile/yard
     * @param useKilometer true, uses kilometer otherwise uses miles.
     */
    public void setDistanceUnit(boolean useKilometer){
        this.useKilometer=useKilometer;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set if the navigation engines give more or less voice command .
     * default is false, then for each turn ,maximum 4 voice commands.
     * @param moreVoiceCommand true, navigation will give more voice command.
     */
    public void setMoreVoiceCommand(boolean moreVoiceCommand){
        this.moreVoiceCommand=moreVoiceCommand;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * start the Navigation engine.
     */
    public void start() {
        synchronized (syncObject) {
            if (stopThread) {
                stopThread = false;
                pauseThread = false;
                locationMonitorThread = new Thread(locationMonitor,
                        "locationMonitor");
                locationMonitorThread.start();
                voiceCommandGeneratorThread = new Thread(voiceCommandGenerator,
                        "voiceCommandGenerator");
                voiceCommandGeneratorThread.start();
                voiceCommandProcessorThread = new Thread(voiceCommandProcessor,
                        "voiceCommandProcessor");
                voiceCommandProcessorThread.start();
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
     * Returns the current state of this LocationProvider. The return value
     * shall be one of the availability status code constants defined in
     * VirtualGPSDevice class.
     * @return the availability state of this device.
     */
    public int getState(){
        return currentLocationProvider.getState();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get current engine status.
     * @return current engine status.
     */
    public int getStatus(){
        return engineStatus;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get status string.
     * @param status
     * @return status string format.
     */
    public static String getStatusString(int status){
        String statusString="STATUS_IDLE";
        switch(status){
            case STATUS_IDLE:
                statusString="STATUS_IDLE";
                break;
            case STATUS_ROUTING_START:
                statusString="STATUS_ROUTING_START";
                break;
            case STATUS_ROUTING_END:
                statusString="STATUS_ROUTING_END";
                break;
            case STATUS_NAVIGATING_ON_ROAD_MODE:
                statusString="STATUS_NAVIGATING_ON_ROAD_MODE";
                break;
            case STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION:
                statusString="STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION";
                break;
            case STATUS_NAVIGATING_OFF_ROAD_MODE:
                statusString="STATUS_NAVIGATING_OFF_ROAD_MODE";
                break;
            case STATUS_DEVIATION:
                statusString="STATUS_DEVIATION";
                break;
            case STATUS_PAUSED:
                statusString="STATUS_PAUSED";
                break;
            default:
                statusString="STATUS_IDLE";
                break;
        }
        return statusString;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * pause Navigation Engine.
     */
    public void pause() {
        synchronized (syncObject) {
            if (!pauseThread) {
                pauseThread = true;
                pauseStatus = engineStatus;
                engineStatus = STATUS_PAUSED;
                if (navigationListener != null) {
                    navigationListener.statusChange(pauseStatus, engineStatus);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * resume Navigation Engine.
     */
    public void resume() {
        synchronized (syncObject) {
            if (pauseThread == true) {
                pauseThread = false;
                synchronized (pauseObject) {
                    pauseObject.notifyAll();
                }
                engineStatus = pauseStatus;
                if (navigationListener != null) {
                    navigationListener.statusChange(STATUS_PAUSED, engineStatus);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * force to stop Navigation engine.
     */
    public void stop() {
        synchronized (syncObject) {
            if (stopThread == false) {
                stopThread = true;
                try {
                    //stop location monitor
                    synchronized (pauseObject) {
                        pauseObject.notifyAll();
                    }
                    synchronized (currentMonitorLocation) {
                        currentMonitorLocation.notifyAll();
                    }
                    //the routingobject is used to make the get
                    //direction call a synchronized one.
                    synchronized (locationMonitor.routingObject) {
                        locationMonitor.routingObject.notifyAll();
                    }
                    synchronized (voiceCommandQueue) {
                        voiceCommandQueue.notifyAll();
                    }

                    synchronized(voiceCommandGenerator.generatorObject){
                        voiceCommandGenerator.generatorObject.notifyAll();
                    }
                    synchronized(voiceCommandProcessor.currentLocation){
                        voiceCommandProcessor.currentLocation.notifyAll();
                    }

                    locationMonitorThread = null;
                    voiceCommandGeneratorThread = null;
                    voiceCommandProcessorThread = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            simulatedDirectionLocationProvider.stopSimulation();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the offline mode current target, if not found or in
     * other status,return null;
     * @return offroad target location.
     */
    public GeoLatLng getOffRoadNavigationTarget(){
        synchronized (syncObject) {
            if (engineStatus != STATUS_NAVIGATING_OFF_ROAD_MODE) {
                return null;
            }
            if (mapDirection != null) {
                locationMonitor.resetCurrentPointIndexBasedOnCurrentLocation();
                return mapDirection.polyline.getVertex(locationMonitor.currentPointIndex);

            }
            return new GeoLatLng(destinationLatLng);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return current walk on route info.
     * @return current walk on route info
     */
    public WalkOnRoute getCurrentWalkOnRoute() {
        synchronized (syncObject) {
            if (mapDirection == null) {
                return null;
            }
            GeoPoint routeStepIndex = mapDirection
                    .getMapRouteStepIndexByPointIndex(locationMonitor
                    .currentPointIndex);
            int routeIndex = (int) routeStepIndex.x;
            int stepIndex = (int) routeStepIndex.y;
            WalkOnRoute walkOnRoute = new WalkOnRoute();
            walkOnRoute.mapDirection = mapDirection;
            walkOnRoute.routeIndex = routeIndex;
            walkOnRoute.stepIndex = stepIndex;
            walkOnRoute.pointIndex = locationMonitor.currentPointIndex;
            return walkOnRoute;
        }
    }

    public void reset() {
        if(rawLocationProvider!=null) rawLocationProvider.reset();
        simulatedDirectionLocationProvider.reset();
    }


    /**
     * the distance limit of command is 50 meters.
     */
    private final static int COMMAND_DISTANCE_LIMIT = 100;
    /**
     * if current speed exceets 60kph, it's considered as highway.
     */
    private final static int HIGHWAY_SPEED_LIMIT = 60;


    /**
     * all distances used for voice command.
     */
    private final static int []VOICE_DISTANCES={
            VoiceCommandType.DISTANCE_050,                      //0
            VoiceCommandType.DISTANCE_100,                      //1
            VoiceCommandType.DISTANCE_150,                      //2
            VoiceCommandType.DISTANCE_200,                      //3
            VoiceCommandType.DISTANCE_250,                      //4
            VoiceCommandType.DISTANCE_300,                      //5
            VoiceCommandType.DISTANCE_400,                      //6
            VoiceCommandType.DISTANCE_500,                      //7
            VoiceCommandType.DISTANCE_600,                      //8
            VoiceCommandType.DISTANCE_700,                      //9
            VoiceCommandType.DISTANCE_800,                      //10
            VoiceCommandType.DISTANCE_900,                      //11
            VoiceCommandType.DISTANCE_1000,                      //12
            VoiceCommandType.DISTANCE_1100,                      //13
            VoiceCommandType.DISTANCE_1200,                      //14
            VoiceCommandType.DISTANCE_1300,                      //15
            VoiceCommandType.DISTANCE_1400,                      //16
            VoiceCommandType.DISTANCE_1500,                      //17
            VoiceCommandType.DISTANCE_1600,                      //18
            VoiceCommandType.DISTANCE_1700,                      //19
            VoiceCommandType.DISTANCE_1800,                      //20
            VoiceCommandType.DISTANCE_1900,                      //21
            VoiceCommandType.DISTANCE_002K,                      //22
            VoiceCommandType.DISTANCE_003K,                      //23
            VoiceCommandType.DISTANCE_004K,                      //24
            VoiceCommandType.DISTANCE_005K,                      //25
            VoiceCommandType.DISTANCE_006K,                      //26
            VoiceCommandType.DISTANCE_005K,                      //27
            VoiceCommandType.DISTANCE_008K,                      //28
            VoiceCommandType.DISTANCE_009K,                      //29
            VoiceCommandType.DISTANCE_010K,                      //30
            VoiceCommandType.DISTANCE_015K,                      //31
            VoiceCommandType.DISTANCE_020K,                      //32
            VoiceCommandType.DISTANCE_025K,                      //33
            VoiceCommandType.DISTANCE_030K,                      //34
            VoiceCommandType.DISTANCE_035K,                      //35
            VoiceCommandType.DISTANCE_040K,                      //36
            VoiceCommandType.DISTANCE_045K,                      //37
            VoiceCommandType.DISTANCE_050K,                      //38
            VoiceCommandType.DISTANCE_055K,                      //39
            VoiceCommandType.DISTANCE_060K,                      //40
            VoiceCommandType.DISTANCE_065K,                      //41
            VoiceCommandType.DISTANCE_070K,                      //42
            VoiceCommandType.DISTANCE_075K,                      //43
            VoiceCommandType.DISTANCE_080K,                      //44
            VoiceCommandType.DISTANCE_085K,                      //45
            VoiceCommandType.DISTANCE_090K,                      //46
            VoiceCommandType.DISTANCE_095K,                      //47
            VoiceCommandType.DISTANCE_100K,                      //48
    };


    /**
     * all distances used for voice command which is optional.
     */
    private final static int []OPTIONAL_VOICE_DISTANCES={
            VoiceCommandType.DISTANCE_1100,                      //13
            VoiceCommandType.DISTANCE_1200,                      //14
            VoiceCommandType.DISTANCE_1300,                      //15
            VoiceCommandType.DISTANCE_1400,                      //16
            VoiceCommandType.DISTANCE_1500,                      //17
            VoiceCommandType.DISTANCE_1600,                      //18
            VoiceCommandType.DISTANCE_1700,                      //19
            VoiceCommandType.DISTANCE_1800,                      //20
            VoiceCommandType.DISTANCE_1900,                      //21
            VoiceCommandType.DISTANCE_015K,                      //31
            VoiceCommandType.DISTANCE_020K,                      //32
            VoiceCommandType.DISTANCE_025K,                      //33
            VoiceCommandType.DISTANCE_030K,                      //34
            VoiceCommandType.DISTANCE_035K,                      //35
            VoiceCommandType.DISTANCE_040K,                      //36
            VoiceCommandType.DISTANCE_045K,                      //37
            VoiceCommandType.DISTANCE_050K,                      //38
            VoiceCommandType.DISTANCE_055K,                      //39
            VoiceCommandType.DISTANCE_060K,                      //40
            VoiceCommandType.DISTANCE_065K,                      //41
            VoiceCommandType.DISTANCE_070K,                      //42
            VoiceCommandType.DISTANCE_075K,                      //43
            VoiceCommandType.DISTANCE_080K,                      //44
            VoiceCommandType.DISTANCE_085K,                      //45
            VoiceCommandType.DISTANCE_090K,                      //46
            VoiceCommandType.DISTANCE_095K,                      //47
            VoiceCommandType.DISTANCE_100K,                      //48
    };




    /**
     * all way points.
     */
    private final Vector wayPoints = new Vector();

    /**
     * if it's the first routing, will use all way points, otherwize only
     * use the last way point as destination.
     */
    private volatile boolean firstRouting=true;

    /**
     * raw location provider.
     */
    private final LocationProvider rawLocationProvider;
    /**
     * current location provider.either be the raw or the simulated.
     */
    private LocationProvider currentLocationProvider;
    /**
     * Digital map service,used to routing.
     */
    private final DigitalMapService digitalMapService;
    /**
     * current navigation engine status;
     */
    private volatile int engineStatus = STATUS_IDLE;

    /**
     * current walk on route.
     */
    private final WalkOnRoute currentWalkOnRoute=new WalkOnRoute();

    /**
     * record engine status when paused /resumed.
     */
    private volatile int pauseStatus=STATUS_IDLE;

    /**
     * current map direction.
     */
    private MapDirection mapDirection = null;
    ;
    /**
     * simulated location provider.
     */
    private final SimulatedDirectionLocationProvider
            simulatedDirectionLocationProvider
            = new SimulatedDirectionLocationProvider();

    /**
     * navigation listener
     */
    private INavigationListener navigationListener = null;

    /**
     * start time deviation Limit ,default is 100 meter.
     */
    private static int startPointDeviationLimit=100;

    /**
     * deviation limit in meter ,default is 20 meters.
     */
    private static int deviationLimit = 35;

    /**
     * deviation limit counter max time, default is 3 times.
     */
    private static int deviationMaxTimes = 2;

    /**
     * location monitor.
     */
    private final LocationMonitor locationMonitor = new LocationMonitor();

    /**
     * voice command generator.
     */
    private final VoiceCommandGenerator voiceCommandGenerator
            = new VoiceCommandGenerator();

    /**
     * voice command processor.
     */
    private final VoiceCommandProcessor voiceCommandProcessor
            = new VoiceCommandProcessor();


    /**
     * offline navigation voice command interval.
     */
    private int offlineNavigationVoiceCommandInterval=15;
    /**
     * allow rerouting or not,default is true.
     */
    private volatile boolean allowRerouting = true;

    /**
     * default distance unit is km or mile
     */
    private boolean useKilometer=true;

    /**
     * generate more voice command or not.
     */
    private boolean moreVoiceCommand=false;

    /**
     * voice command queue
     */
    private final Vector voiceCommandQueue=new Vector();

    /**
     * minium count of the command in the queue.
     */
    private final static int VOICE_COMMAND_QUEUE_SIZE=8;

    /**
     * voice command listener.
     */
    private IVoiceCommandListener voiceCommandListener=null;

    /**
     * stop the thead or not.
     */
    private volatile boolean stopThread = true;

    /**
     * use to control the pause/resume the thread.
     */
    private volatile boolean pauseThread=true;

    /**
     * object use to signal pause/resume.
     */
    private final Object pauseObject=new Object();

    /**
     * location monitor thread.
     */
    private Thread locationMonitorThread = null;

    /**
     * voice command generator thread
     */
    private Thread voiceCommandGeneratorThread=null;

    /**
     * voice command processor thread
     */
    private Thread voiceCommandProcessorThread=null;

    /**
     * current monitor lat/longitude.
     */
    private final GeoLatLng currentLatLng = new GeoLatLng();

    /**
     * current location.
     */
    private final Location currentMonitorLocation = new Location();
    /**
     * previous lat/longitude,temp used to avoid going backwards when adjust
     * the location on route
     */
    private final GeoLatLng previousLatLng = new GeoLatLng();

    /**
     * destination
     */
    private final GeoLatLng destinationLatLng = new GeoLatLng();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if need pause curren thread.
     */
    private void checkIfNeedPauseThread() {
        //check if this thread need to be paused.
        if (pauseThread) {
            synchronized (pauseObject) {
                try {
                    Log.p(Thread.currentThread().getName() + " thread paused");
                    pauseObject.wait();
                    Log.p(Thread.currentThread().getName() + " thread resumed");
                } catch (InterruptedException ex) {
                    //inglore the exception
                    Log.p(Thread.currentThread().getName() + " thread interrupted:"
                            + ex.getMessage());
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see given distance command is optional or not.
     * @param type distance command type.
     * @return true is optional.
     */
    private static boolean isOptionalDistanceCommand(int type) {
        for (int i = 0; i < OPTIONAL_VOICE_DISTANCES.length; i++) {
            if (OPTIONAL_VOICE_DISTANCES[i] == type) {
                return true;
            }
        }
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Location monitor class, used to monitor current location againest
     * current routing direction.
     * <P>
     * <hr>
     * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
     * @version     2.00, 19/09/09
     * @author      Guidebee Pty Ltd.
     */
    private class LocationMonitor implements Runnable, IRoutingListener,
            ILocationListener {

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * state mechine, used to monitor current location.
         */
        public void run() {
           Log.p( Thread.currentThread().getName()+" thread started");
            while (!stopThread) {
                try {

                    checkIfNeedPauseThread();
                //state machine ,transition.

                    switch (engineStatus) {
                        case STATUS_IDLE:
                            //Log.p("STATUS_IDLE");
                            break;
                        case STATUS_PAUSED:
                            //Log.p("STATUS_PAUSED");
                            break;
                        case STATUS_ROUTING_START:
                            //Log.p("STATUS_ROUTING_START");
                             {

                                currentMonitorLocation
                                        .copy(currentLocationProvider
                                        .getLocation(10));
                                if (currentMonitorLocation != null &&
                                        !Double.isNaN(currentMonitorLocation.longitude)
                                        && !Double.isNaN(currentMonitorLocation.latitude)) {
                                    GeoLatLng estimatedStartPoint
                                            = getEstimatedLocation
                                            (ESTIMATED_ROUTING_TIME);
                                    if(!Double.isNaN(estimatedStartPoint.x) &&
                                            !Double.isNaN(estimatedStartPoint.y)){
                                        String queryString = "from:" + "@"
                                                + estimatedStartPoint.lat()
                                                + "," + estimatedStartPoint.lng();
                                        if(firstRouting){
                                            firstRouting=false;
                                            for(int i=0;i<wayPoints.size();i++){
                                                WayPoint wayPoint = (WayPoint)
                                                    wayPoints.elementAt(i);
                                                queryString+=" to:" + "@"
                                                    + wayPoint.geLatLng().lat()
                                                    + "," + wayPoint.geLatLng().lng();
                                            }
                                        }else{
                                            WayPoint wayPoint=(WayPoint)
                                                    wayPoints.lastElement();
                                            queryString+=" to:" + "@"
                                                    + wayPoint.geLatLng().lat()
                                                    + "," + wayPoint.geLatLng().lng();
                                        }
                                        Log.p("Rerouting started:" + queryString);
                                        digitalMapService.setRoutingListener(this);
                                        digitalMapService.getDirections(queryString);
                                        synchronized (routingObject) {
                                            routingObject.wait();
                                        }
                                        if (navigationListener != null) {
                                            navigationListener
                                                    .reroutingDone(queryString,
                                                    mapDirection);
                                        }
                                        if (mapDirection != null) {
                                            Log.p("Start:" + mapDirection.geoCodes[0].name);
                                            for (int i = 1; i < mapDirection.geoCodes.length - 1; i++) {
                                                Log.p("Waypoint:" + mapDirection.geoCodes[i].name);
                                            }
                                            Log.p("End:" + mapDirection
                                                    .geoCodes[mapDirection.geoCodes.length - 1].name);
                                            counter = 0;
                                            engineStatus = STATUS_ROUTING_END;
                                            if (navigationListener != null) {
                                                navigationListener.statusChange(STATUS_ROUTING_START,
                                                        engineStatus);
                                            }
                                            continue;
                                        }

                                    } else {
                                        Log.p("Cannot routing with given way points");
                                    }
                                }else{
                                    Log.p("Cannot get a fix,Sleep 5 seconds and then try");
                                    Thread.sleep(5000);
                                }
                            }
                            break;
                        case STATUS_ROUTING_END:
                            //Log.p("STATUS_ROUTING_END");
                             {

                                if (counter > deviationMaxTimes) {
                                    //still can not make current location
                                    //on the route, need to reroute.
                                    counter = 0;
                                    engineStatus = STATUS_ROUTING_START;
                                    if (navigationListener != null) {
                                        navigationListener.statusChange
                                                (STATUS_ROUTING_END,
                                                engineStatus);
                                    }
                                    continue;
                                }
                                long currentTimeStamp=System.currentTimeMillis();
                                resetCurrentPointIndexBasedOnCurrentLocation();
                                boolean onTrack=isCurrentPointIndexOnTrack();
                                if (onTrack) {
                                    counter = 0;
                                    initialize();
                                    flushCommandQueue();
                                    engineStatus = STATUS_NAVIGATING_ON_ROAD_MODE;
                                    if (navigationListener != null) {
                                        navigationListener.statusChange
                                                (STATUS_ROUTING_END,
                                                engineStatus);
                                    }
                                    continue;
                                }
                                if (!onTrack &&
                                        (currentTimeStamp-lastTimeStamp>
                                        ESTIMATED_ROUTING_TIME*1000) ) {
                                    counter++;
                                    Log.p("regain route failed:" + counter);
                                    lastTimeStamp=currentTimeStamp;
                                }
                            }
                            break;

                        case STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION:
                            //Log.p("STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION");
                             {
                                 GeoLatLng firstLatlng = mapDirection.polyline.getVertex(0);
                                currentLatLng.y = currentMonitorLocation.latitude;
                                currentLatLng.x = currentMonitorLocation.longitude;
                                 double distance = GeoLatLng.distance(currentLatLng,
                                         firstLatlng) * 1000;
                                 if (distance < deviationLimit) {

                                     counter = 0;
                                     initialize();
                                     engineStatus = STATUS_NAVIGATING_ON_ROAD_MODE;
                                     if (navigationListener != null) {
                                         navigationListener
                                                 .statusChange(STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION,
                                                 engineStatus);
                                     }
                                     continue;
                                 }
                                 if (distance > startPointDeviationLimit) {
                                     counter = 0;
                                     engineStatus = STATUS_DEVIATION;
                                     if (navigationListener != null) {
                                         navigationListener
                                                 .statusChange(STATUS_NAVIGATING_ON_ROAD_MODE,
                                                 engineStatus);
                                     }
                                     continue;

                                 }
                            }
                            break;

                        case STATUS_NAVIGATING_ON_ROAD_MODE:
                            //Log.p("STATUS_NAVIGATING_ON_ROAD_MODE");
                             {
                                //save the raw location before align
                                //raw location to the map direction's polyline
                                rawLocation.copy(currentMonitorLocation);
                                if (counter > deviationMaxTimes-1) {
                                    //get the nearest location from the route,
                                    //if still within the limit, change current point index
                                    //and keep navgtion, otherwise ,change to deviation
                                    //status
                                   resetCurrentPointIndexBasedOnCurrentLocation();
                                   if (isCurrentPointIndexOnTrack()) {
                                        flushCommandQueue();
                                        Log.p("Found nearest position, continue navgating");
                                        counter = 0;
                                    }else{
                                        counter = 0;
                                        engineStatus = STATUS_DEVIATION;
                                       if (navigationListener != null) {
                                           navigationListener.statusChange
                                                   (STATUS_NAVIGATING_ON_ROAD_MODE,
                                                   engineStatus);
                                       }
                                        continue;
                                    }

                                }
                                //now to try to find the nearest point on the
                                //map direction's polyline.
                                int polylineLength = mapDirection.polyline.getVertexCount();
                                currentLatLng.y = currentMonitorLocation.latitude;
                                currentLatLng.x = currentMonitorLocation.longitude;
                                int howManyPoints = Math.min(36,
                                        polylineLength-currentPointIndex);
                                tempVector.removeAllElements();
                                int i=0;
                                GeoLatLng oldTempLatLng=new GeoLatLng();
                                while(tempVector.size()<howManyPoints
                                        && (currentPointIndex + i<polylineLength)){
                                    GeoLatLng tempLatLng = mapDirection
                                                .polyline
                                                .getVertex(currentPointIndex + i++);
                                    if(tempLatLng.x!=oldTempLatLng.x &&
                                           tempLatLng.y!=oldTempLatLng.y ){
                                        tempVector.addElement(tempLatLng);
                                        oldTempLatLng.x=tempLatLng.x;
                                        oldTempLatLng.y=tempLatLng.y;
                                    }
                                }
                                howManyPoints=tempVector.size();
                                if (howManyPoints > 0) {
                                    double[] xpts = new double[howManyPoints];
                                    double[] ypts = new double[howManyPoints];
                                    for ( i = 0; i < howManyPoints; i++) {
                                        GeoLatLng tempLatLng =(GeoLatLng) tempVector.elementAt(i);
                                        xpts[i] = tempLatLng.x;
                                        ypts[i] = tempLatLng.y;
                                    }
                                    GeoPoint result = GeoPolyline
                                            .IndexOfClosestdistanceToPoly
                                            (currentLatLng.x,
                                            currentLatLng.y, xpts, ypts, false);
                                    currentPointIndex = Math.max(oldPointIndex,
                                            (int) result.y
                                            + currentPointIndex);
                                    oldPointIndex=currentPointIndex;
                                }
                              if (currentPointIndex >= 0 &&
                                        currentPointIndex < polylineLength - 1) {
                                    boolean onTrack=isCurrentPointIndexOnTrack();
                                    long currentTimeStamp =
                                            System.currentTimeMillis();
                                    if (!onTrack
                                            && (currentTimeStamp - lastTimeStamp >
                                            ESTIMATED_ROUTING_TIME*1000)) {//need rerouting
                                        counter++;
                                        Log.p("deviation detected:" + counter);
                                        lastTimeStamp = currentTimeStamp;

                                    }
                                    if (onTrack) {
                                        //reset counter and ajust current location to the route.
                                        if (counter > 0) {
                                            Log.p("deviation amended");
                                        }
                                        counter = 0;
                                        lastTimeStamp = currentTimeStamp;

                                    }
                                    //adjust location ot avoid going backwards
                                 if (previousLatLng.x == 0 ||
                                         previousLatLng.y == 0) {

                                     previousLatLng.y =
                                             currentMonitorLocation.latitude;
                                     previousLatLng.x =
                                             currentMonitorLocation.longitude;

                                 }
                                 if(currentPointIndex==0 && onTrack){
                                     GeoLatLng pt1 = mapDirection.polyline
                                            .getVertex(currentPointIndex);
                                    currentMonitorLocation.latitude = pt1.lat();
                                    currentMonitorLocation.longitude = pt1.lng();
                                 }

                                }

                                alignCurrentLocationOnTheRoad();
                                if (locationListener != null) {
                                    locationListener
                                            .locationUpdated(currentLocationProvider,
                                            currentMonitorLocation);
                                }
                                if (navigationListener != null) {
                                    navigationListener
                                            .locationUpdated(currentLocationProvider,
                                            rawLocation, currentMonitorLocation);
                                }
                                notifyVoiceCommandProcessor();
                            }
                            break;

                        case STATUS_NAVIGATING_OFF_ROAD_MODE:
                            {
                                currentLatLng.y = currentMonitorLocation.latitude;
                                currentLatLng.x = currentMonitorLocation.longitude;
                                double distance = GeoLatLng.distance(currentLatLng,
                                        destinationLatLng) * 1000;
                                if (distance < deviationLimit) {
                                    //navigation is done, return to idle status.
                                    navigationListener.navigationDone();
                                    counter = 0;
                                    engineStatus = STATUS_IDLE;
                                    if (navigationListener != null) {
                                        navigationListener.statusChange
                                                (STATUS_NAVIGATING_OFF_ROAD_MODE,
                                                engineStatus);
                                    }
                                    continue;
                                }
                                if(mapDirection!=null){
                                    //if the map direction is not null
                                    //check if it's on track,if so
                                    //go the on road navigation mode
                                    resetCurrentPointIndexBasedOnCurrentLocation();
                                    if(isCurrentPointIndexOnTrack()){
                                        counter = 0;
                                        engineStatus = STATUS_NAVIGATING_ON_ROAD_MODE;
                                        if (navigationListener != null) {
                                            navigationListener.statusChange
                                                    (STATUS_NAVIGATING_OFF_ROAD_MODE,
                                                    engineStatus);
                                        }
                                        continue;
                                    }
                                }
                                notifyVoiceCommandProcessor();
                            }

                            break;
                        case STATUS_DEVIATION:
                           // Log.p("STATUS_DEVIATION");
                             {

                                if (counter > deviationMaxTimes) {
                                    counter = 0;
                                    if (allowRerouting) {
                                        flushCommandQueue();
                                        engineStatus = STATUS_ROUTING_START;
                                    } else {
                                        //dont allow rerouting,
                                        //go to off road navigation mode.
                                        engineStatus = STATUS_NAVIGATING_OFF_ROAD_MODE;
                                        if (navigationListener != null) {
                                            navigationListener.statusChange
                                                    (STATUS_DEVIATION, engineStatus);
                                        }
                                        counter=0;
                                    }
                                    continue;
                                }
                                resetCurrentPointIndexBasedOnCurrentLocation();
                                boolean onTrack=isCurrentPointIndexOnTrack();
                                if (onTrack) {
                                    counter = 0;
                                    Log.p("regained route successfully,continue navigating");
                                    engineStatus = STATUS_NAVIGATING_ON_ROAD_MODE;
                                    if (navigationListener != null) {
                                        navigationListener.statusChange
                                                (STATUS_DEVIATION, engineStatus);
                                    }
                                    continue;
                                }
                                long currentTimeStamp = System.currentTimeMillis();
                                    if (!onTrack &&
                                            (currentTimeStamp - lastTimeStamp >
                                            ESTIMATED_ROUTING_TIME*1000)) {
                                        counter++;
                                        Log.p("confirmed deviation:" + counter);
                                   lastTimeStamp=currentTimeStamp;
                                }
                                notifyVoiceCommandProcessor();
                            }
                            break;
                    }
                    synchronized (locationReady) {
                        locationReady.wait();
                    }

                } catch (Exception e) {
                    //catch all exeptions.
                    Log.p(e.getMessage());
                }
            }
            Log.p(Thread.currentThread().getName() + " thread stopped!");
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * notify voice command process
         */
        private void notifyVoiceCommandProcessor() {
            //notifiy voice command processor.
            synchronized (voiceCommandProcessor.currentLocation) {
                voiceCommandProcessor.currentLocation.x = currentMonitorLocation.longitude;
                voiceCommandProcessor.currentLocation.y = currentMonitorLocation.latitude;
                voiceCommandProcessor.currentBearing = currentMonitorLocation.course;
                voiceCommandProcessor.currentLocation.notify();

            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * direction query done.
         * @param query
         * @param result
         */
        public void done(String query, MapDirection result) {
            mapDirection = result;
            if(mapDirection!=null){
                //reset the destination location.
                destinationLatLng.x=
                        mapDirection.polyline
                        .getVertex(mapDirection.polyline.getVertexCount()-1).x;
                destinationLatLng.y=
                        mapDirection.polyline
                        .getVertex(mapDirection.polyline.getVertexCount()-1).y;
            }
            synchronized (routingObject) {
                Log.p("routing is done!");
                routingObject.notify();
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * direction query progress
         * @param bytes
         * @param total
         */
        public void readProgress(int bytes, int total) {
            if (navigationListener != null) {
                navigationListener.reroutingProgress(bytes, total);
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * raw location provider location listener handler
         * @param device
         * @param location
         */
        public void locationUpdated(LocationProvider device, Location location) {
            synchronized (locationReady) {
                currentLocation.copy(location);
                currentMonitorLocation.copy(location);
                if (locationListener != null) {
                    //if it's not in navagion status, just use the raw location
                    //because no alignment is needed.
                    if (engineStatus != STATUS_NAVIGATING_ON_ROAD_MODE) {
                        locationListener.locationUpdated(device,
                                currentMonitorLocation);
                        if (navigationListener != null) {
                            navigationListener.locationUpdated(device,
                                    currentMonitorLocation, currentMonitorLocation);
                        }
                    }
                }
                //notify the location monitor thread.
                locationReady.notifyAll();
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * raw location provider status change.
         * @param device
         * @param newState
         */
        public void providerStateChanged(LocationProvider device, int newState) {
            if (locationListener != null) {
                locationListener.providerStateChanged(device, newState);
            }
        }



        /**
         * current location.
         */
        private final Location rawLocation = new Location();

        /**
         * estimated routing calculating time ,default is 5 seconds.
         */
        private final static int ESTIMATED_ROUTING_TIME = 5;

        /**
         * use to sync routing result;
         */
        private final Object routingObject = new Object();

        private final Vector tempVector=new Vector();

        /**
         * counter to count deviation
         */
        private int counter;

        /**
         * current point index of the map directions' polyline.
         */
        private int currentPointIndex = 0;

        /**
         * last time stamp used to detects deviation.
         */
        private long lastTimeStamp=0;

        /**
         * old point index, to avoid going backwards
         */
        private int oldPointIndex=0;

        /**
         * used to drive location thread.
         */
        private final Object locationReady=new Object();

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * initiaize variables.
         */
        private void initialize(){
            counter=0;
            currentPointIndex=0;
            lastTimeStamp=0;
            oldPointIndex=0;
            rawLocation.latitude=0;
            rawLocation.longitude=0;
            currentWalkOnRoute.pointIndex=-1;
            currentWalkOnRoute.routeIndex=-1;
            currentWalkOnRoute.stepIndex=-1;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 09SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * flush command queue
         */
        private void flushCommandQueue() {
            voiceCommandProcessor.clearCommandQueue = true;
            notifyVoiceCommandProcessor();
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * calculate estimated location with current speed and time
         * @param time in seconds
         * @return estimated location.
         */
        private GeoLatLng getEstimatedLocation(int time) {
            if (currentMonitorLocation != null) {
                //km/h
                double currentSpeed = currentMonitorLocation.speed;
                //in meter
                double distance = currentSpeed * (double) time / 3600.0 * 1000.0;
                double delta = 0.0001;
                currentLatLng.y = currentMonitorLocation.latitude;
                currentLatLng.x = currentMonitorLocation.longitude;
                GeoLatLng estimatedLatLng = new GeoLatLng();
                double angle = currentMonitorLocation.course;
                if ( Double.isNaN(distance)|| Double.isNaN(angle) || distance == 0 ) {
                    //in degree
                    estimatedLatLng.x = currentLatLng.x;
                    estimatedLatLng.y = currentLatLng.y;
                } else {

                    estimatedLatLng.x = currentLatLng.x + delta * Math.sin(angle);
                    estimatedLatLng.y = currentLatLng.y + delta * Math.cos(angle);
                    double estimatedDistance = GeoLatLng.distance(currentLatLng,
                            estimatedLatLng) * 1000;

                    estimatedLatLng.x = currentLatLng.x + delta * Math.sin(angle)
                            * distance / estimatedDistance;
                    estimatedLatLng.y = currentLatLng.y + delta * Math.cos(angle)
                            * distance / estimatedDistance;
                }

                return estimatedLatLng;
            }
            return null;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * reset current point index based on current location.
         */
        private void resetCurrentPointIndexBasedOnCurrentLocation(){
            currentLatLng.y = currentMonitorLocation.latitude;
            currentLatLng.x = currentMonitorLocation.longitude;
            GeoPoint result = mapDirection.polyline
                    .IndexOfClosestdistanceToPoly(currentLatLng);
            currentPointIndex = (int) result.y;
            oldPointIndex = currentPointIndex;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * check to see if still on track.
         * @return
         */
        private boolean isCurrentPointIndexOnTrack() {
            int polyLineCount = mapDirection.polyline.getVertexCount();
            GeoLatLng nearestPoint = mapDirection
                    .polyline.getVertex(currentPointIndex);
            double distance2 = Double.POSITIVE_INFINITY;
            if (currentPointIndex >= 0
                    && currentPointIndex < polyLineCount - 1) {
                int nextIndex = currentPointIndex + 1;
                GeoLatLng nearestPoint1 = mapDirection.polyline.getVertex(nextIndex);
                while (nearestPoint1.x == nearestPoint.x &&
                        nearestPoint1.y == nearestPoint.y &&
                        nextIndex < polyLineCount-1) {
                    nearestPoint1 = mapDirection.polyline.getVertex(++nextIndex);
                }
                if (nearestPoint1.x == nearestPoint.x &&
                        nearestPoint1.y == nearestPoint.y) {
                    distance2 = GeoLatLng.distance(currentLatLng, nearestPoint) * 1000;
                } else {
                    GeoPoint closestPointOnStep = GeoPolyline
                            .getClosetPoint(nearestPoint, nearestPoint1,
                            currentLatLng, false);
                    GeoLatLng closestLatLng = new GeoLatLng(closestPointOnStep.y,
                            closestPointOnStep.x);
                    distance2 = GeoLatLng.distance(currentLatLng, closestLatLng) * 1000;
                }
            } else {
                distance2 = GeoLatLng.distance(currentLatLng, nearestPoint) * 1000;
            }
            if (distance2 < deviationLimit) {
               return true;
            }
            return false;
        }


        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * align current location to the route,(route matching)
         * and check to see if going backwards, it back word detected, keep the
         * current location.
         */
        private void alignCurrentLocationOnTheRoad() {
            int polyLineCount = mapDirection.polyline.getVertexCount();
            currentLatLng.y =
                    rawLocation.latitude;
            currentLatLng.x =
                    rawLocation.longitude;
            GeoLatLng pt1 = mapDirection.polyline.getVertex(currentPointIndex);
            int nextPointIndex=currentPointIndex + 1;
            GeoLatLng pt2 = mapDirection.polyline.getVertex(nextPointIndex);
            while(pt2.x==pt1.x && pt2.y==pt1.y && nextPointIndex<polyLineCount-1){
                pt2 = mapDirection.polyline.getVertex(++nextPointIndex);
            }

            currentPointIndex=nextPointIndex-1;
            GeoPoint rt = GeoPolyline.getClosetPoint(pt1, pt2,
                    currentLatLng, true);
            //if equals the end of the line, try next line segment.
            int tryTimes=0;
            while(rt.x==pt2.x && rt.y==pt2.y && tryTimes<3 && nextPointIndex<polyLineCount-1){
                currentPointIndex=nextPointIndex;
                nextPointIndex=currentPointIndex + 1;
                 pt1 = mapDirection.polyline.getVertex(currentPointIndex);
                 pt2 = mapDirection.polyline.getVertex(nextPointIndex);
                 while(pt2.x==pt1.x && pt2.y==pt1.y && nextPointIndex<polyLineCount-1){
                    pt2 = mapDirection.polyline.getVertex(++nextPointIndex);
                }
                rt = GeoPolyline.getClosetPoint(pt1, pt2,
                    currentLatLng, true);
                currentPointIndex=nextPointIndex-1;
                tryTimes++;
            }
            GeoLatLng clostPoint = new GeoLatLng(rt.y, rt.x);
            currentMonitorLocation.latitude = clostPoint.lat();
            currentMonitorLocation.longitude = clostPoint.lng();
            currentMonitorLocation.course=GeoLatLng.azimuthTo(pt1, pt2);
//            if((pt1.x==clostPoint.x && pt1.y == clostPoint.y) ||
//                    (pt2.x==clostPoint.x && pt2.y == clostPoint.y)){
//                //do nothing
//            }else{
//                double angle1=GeoLatLng.azimuthTo(clostPoint, previousLatLng);
//                double angle2=GeoLatLng.azimuthTo(pt2, clostPoint);
//                if(Math.abs(angle2-angle1)>90){
//                    //Log.p("Backwards detected:"+(angle2-angle1));
//                    currentMonitorLocation.latitude = previousLatLng.lat();
//                    currentMonitorLocation.longitude = previousLatLng.lng();
//                }else{
//                    previousLatLng.x=currentMonitorLocation.longitude;
//                    previousLatLng.y=currentMonitorLocation.latitude;
//                }
//            }
            currentLatLng.y =
                    currentMonitorLocation.latitude;
            currentLatLng.x =
                    currentMonitorLocation.longitude;
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Voice command generator. it's driven by voice command processor.
     */
    private class VoiceCommandGenerator implements Runnable {

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *  thread used to create the voice command and put them to the queue.
         */
        public void run() {
            Log.p(Thread.currentThread().getName() + " thread started.");
            while (!stopThread) {
                try {

                    //check if this thread need to be paused.
                    checkIfNeedPauseThread();
                    //voice command processor wakes generator up
                    synchronized(generatorObject){
                        generatorObject.wait();
                    }
                    switch (engineStatus) {
                        case STATUS_DEVIATION:
                            break;
                        case STATUS_NAVIGATING_OFF_ROAD_MODE:

                            break;
                        case STATUS_NAVIGATING_ON_ROAD_MODE:
                             {
                                 //initialize current walk on route
                                 //normaly it's not actually 'current walkonroute'
                                 //it's current walkonroute for command
                                 //generation, normaly it's ahead of current
                                 //locations
                                 if (currentWalkOnRoute.routeIndex == -1) {
                                    WalkOnRoute walkOnRoute = getCurrentWalkOnRoute();
                                    currentWalkOnRoute.routeIndex = walkOnRoute.routeIndex;
                                    currentWalkOnRoute.stepIndex = walkOnRoute.stepIndex;
                                    currentWalkOnRoute.pointIndex = walkOnRoute.pointIndex;
                                }
                                synchronized (voiceCommandQueue) {
                                    generateOnRoadVoiceCommand();
                                }
                            }

                            break;
                    }

                } catch (Exception e) {
                    //catch all exeptions.
                    Log.p(e.getMessage());
                }
            }
            Log.p(Thread.currentThread().getName() + " thread stopped!");
        }

        /**
         * the distance limit on normal road is 150 meters.
         */
        private final static int NORMAL_DISTANCE_LIMIT = 150;


        /**
         * mile to km;
         */
        private final static double MILE_TO_KM=1.609344 ;

        /**
         * notify generator.
         */
        private final Object generatorObject=new Object();

        /**
         * internal usage.
         */
        private int nearestIndex=-1;



        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Get the location at given distance towards the end of the step.
         * @param walkOnRoute current walk on route
         * @param mapStep   on which step.
         * @param distance  distance in meters
         * @return the location.
         */
        private GeoLatLng getLocationAtDistance(WalkOnRoute walkOnRoute,
                MapStep mapStep, double distance) {
            GeoLatLng latLng = new GeoLatLng();

            //should calculate backwards from the end of the step
            int startIndex = mapStep.lastLocationIndex;
            int endIndex = walkOnRoute.pointIndex;
            if(!useKilometer){
                distance*=MILE_TO_KM;
            }

            double length1 = 0;
            double length2=0;
            GeoLatLng pt1=new GeoLatLng();
            GeoLatLng pt2=new GeoLatLng();
            GeoLatLng[] latlngs = mapDirection.polyline.getPoints();
            nearestIndex=-1;
            pt1.x =latlngs[startIndex].x;
            pt1.y =latlngs[startIndex].y;
            for (int i = startIndex - 1; i >= endIndex; i--) {
                pt2.x =latlngs[i].x;
                pt2.y =latlngs[i].y;
                length1=length2;
                length2 += pt1.distanceFrom(pt2) * 1000;
                if(length2>=distance && length1<=distance){
                    nearestIndex=i;
                    break;
                }
                pt1.x = pt2.x;
                pt1.y = pt2.y;
            }
            if(nearestIndex>=0 ){
                double deltaDistance=length2-length1;
                double remainDistance=distance-length1;
                if(deltaDistance!=0){
                    latLng.x=pt1.x +(pt2.x-pt1.x)*remainDistance/deltaDistance;
                    latLng.y=pt1.y +(pt2.y-pt1.y)*remainDistance/deltaDistance;
                }else{
                    latLng.x=pt2.x;
                    latLng.y=pt2.y;

                }
            }
            if(length2<50){
                //if total remain lenght is less than 50 meters,
                //the minimum voice command distance, just use the end point
                latLng.x=latlngs[endIndex].x;
                latLng.y=latlngs[endIndex].y;
                nearestIndex=endIndex;
            }
            return latLng;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *  get the distnace command to the end of the step.
         *  in revser order, for example , 250 --> 200 --> 150 -->100
         * @param walkOnRoute
         * @param mapStep
         * @param isHighway
         * @return a vector contains all voice command distances.
         */
        private Vector getDistanceToEndOfStep(WalkOnRoute walkOnRoute,
                MapStep mapStep,boolean isHighway){
            Vector retVector=null;
            Vector distanceToStep=new Vector();
            int addMile=useKilometer? VoiceCommandType.DISTANCE_UNIT_KILOMETER:
                VoiceCommandType.DISTANCE_UNIT_MILE;
            //should calculate backwards from the end of the step
            int startIndex=mapStep.lastLocationIndex;
            int endIndex=walkOnRoute.pointIndex;
            //get the remaining lenght of the step in meters.
            double distanceToEndOfStep=0;
            if (endIndex == mapStep.firstLocationIndex) {
                distanceToEndOfStep = mapStep.distance;
            } else {
                distanceToEndOfStep = mapDirection.polyline.getLength(endIndex,
                        startIndex);
            }
            //inglore the short remaining step.
            if(distanceToEndOfStep<1) return distanceToStep;
            int maxDistanceIndex=getMaximumVoiceCommandIndex(distanceToEndOfStep,
                    !useKilometer);
            for (int i = maxDistanceIndex - 1; i >= 0; i--) {
                int voiceCommandType = VOICE_DISTANCES[i];
                if (isOptionalDistanceCommand(VOICE_DISTANCES[i]) && !moreVoiceCommand) {
                    continue;
                } else {
                    double distance = voiceCommandType;
                    GeoLatLng latLng = getLocationAtDistance(walkOnRoute, mapStep, distance);
                    if (nearestIndex >= 0) {
                        VoiceCommandArg newCommandArg =
                                new VoiceCommandArg(voiceCommandType | addMile,
                                mapStep.currentRoadName);
                        newCommandArg.commandLocation = new GeoLatLng(latLng);
                        newCommandArg.pointIndex = nearestIndex;
                        newCommandArg.routeIndex = walkOnRoute.routeIndex;
                        newCommandArg.stepIndex = walkOnRoute.stepIndex;
                        newCommandArg.subVoiceCommandType = voiceCommandType;
                        distanceToStep.addElement(newCommandArg);
                    }
                }
            }
            if(moreVoiceCommand ||
                    distanceToEndOfStep < COMMAND_DISTANCE_LIMIT
                    || distanceToStep.size()<=4 ){
                retVector= distanceToStep;
            }
            else{
                //choose maximum 4 entries in the vector.
                Vector selectVector=new Vector();
                int lenOfTotal=distanceToStep.size();
                int middle=lenOfTotal/4;
                //get the first one (longest distance)
                selectVector.addElement(distanceToStep.elementAt(0));

                //get the second first one
                selectVector.addElement(distanceToStep.elementAt(middle));

                //which is the second one depends on the highway on not
                if (!isHighway) {
                    selectVector.addElement(distanceToStep.elementAt(lenOfTotal
                            - middle-1));
                } else {
                    if (lenOfTotal > 11 && middle < lenOfTotal-11) {
                        selectVector.addElement(distanceToStep
                                    .elementAt(lenOfTotal-11));
                    }else{
                       selectVector.addElement(distanceToStep.elementAt(
                               (middle+lenOfTotal
                               - 1)/2));
                    }
                }
                //get the last one (nearest command 50 meters).
                selectVector.addElement(distanceToStep.elementAt(lenOfTotal-1));
                retVector=selectVector;
            }
            return retVector;
       }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * get nearest distance command.
         * @param distance
         * @param isMile
         * @return
         */
        private int getMaximumVoiceCommandIndex(double distance, boolean isMile) {
            int ret = 0;
            double miles = isMile ? MILE_TO_KM : 1.0;
            double yards = isMile ? 1.76 : 1.0;
            int maxIndex = VOICE_DISTANCES.length;
            int delimiterIndex = 12; //1K
            for (int i = maxIndex - 1; i >= delimiterIndex; i--) {
                if (isOptionalDistanceCommand(VOICE_DISTANCES[i]) && !moreVoiceCommand) {
                    continue;
                } else {
                    if (distance >= ((double) VOICE_DISTANCES[i]) * miles) {
                        ret = i;
                        break;
                    }
                }
            }
            if (ret < delimiterIndex) {

                for (int i = delimiterIndex - 1; i >= 0; i--) {
                    if (isOptionalDistanceCommand(VOICE_DISTANCES[i]) && !moreVoiceCommand) {
                        continue;
                    } else {
                        if (distance >= ((double) VOICE_DISTANCES[i]) * miles * yards) {
                            ret = i;
                            break;
                        }
                    }
                }
            }
            return ret+1;

        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Add to voice command queue.
         * @param voiceCommandArgs
         */
        private void addToVoiceCommandQueue(VoiceCommandArg[] voiceCommandArgs) {
            voiceCommandQueue.addElement(voiceCommandArgs);
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * generate on road command.
         */
        private void generateOnRoadVoiceCommand() {
            boolean isHighway = currentMonitorLocation.speed
                    > HIGHWAY_SPEED_LIMIT;
            MapStep currentMapStep = null;
            if (mapDirection != null &&
                    currentWalkOnRoute.routeIndex < mapDirection.routes.length) {
                int maxStepLength = mapDirection
                        .routes[currentWalkOnRoute.routeIndex].steps.length;
                if (currentWalkOnRoute.stepIndex < maxStepLength) {
                    currentMapStep = mapDirection
                            .routes[currentWalkOnRoute.routeIndex]
                            .steps[currentWalkOnRoute.stepIndex];
                    //next voice command type and route name
                    int voiceCommandType;
                    int voiceSubCommandType;
                    String commandRouteName="";
                    //next next voice command type and route name if needed
                    boolean needIncludeNextStep = false;
                    String nextCommandRouteName="";
                    int nextVoiceCommandType = VoiceCommandType.NONE;
                    int nextVoiceSubCommandType = VoiceCommandType.NONE;
                    //if current step is not the last step
                    //then still need to create next voice command.
                    if (currentWalkOnRoute.stepIndex < maxStepLength - 1) {
                        MapStep nextCommmandMapStep = mapDirection
                                .routes[currentWalkOnRoute.routeIndex]
                                .steps[currentWalkOnRoute.stepIndex + 1];
                        double nextDistanceToEndOfStep = nextCommmandMapStep.distance;
                        //if next step is too short, we will include
                        //next next turn if there's one.
                        if (nextDistanceToEndOfStep < NORMAL_DISTANCE_LIMIT) {
                            needIncludeNextStep = true;
                        }
                        commandRouteName = nextCommmandMapStep.currentRoadName;
                        if (nextCommmandMapStep.directionCommandElements
                                [MapDirectionCommandElement.DIRECTION_COMMAND] != null) {
                            voiceCommandType = nextCommmandMapStep
                                    .directionCommandElements
                                    [MapDirectionCommandElement.DIRECTION_COMMAND]
                                    .directionCommandType.type;
                        } else {
                            voiceCommandType = nextCommmandMapStep.calculatedDirectionType.type;
                        }
                        //calculated command type.
                        voiceSubCommandType = nextCommmandMapStep.calculatedDirectionType.type;
                    } else {
                        //it's the last step ,the create the destination command.
                        commandRouteName = currentMapStep.currentRoadName;
                        voiceCommandType = VoiceCommandType.DESTINATION;
                        voiceSubCommandType = VoiceCommandType.DESTINATION;
                    }
                    if (needIncludeNextStep) {
                        //if need to include step ,the check if there's one
                        if (currentWalkOnRoute.stepIndex <
                                mapDirection.routes[currentWalkOnRoute.routeIndex].steps.length - 2) {
                            MapStep nextNextCommandMapStep = mapDirection
                                    .routes[currentWalkOnRoute.routeIndex]
                                    .steps[currentWalkOnRoute.stepIndex + 2];
                            nextCommandRouteName = nextNextCommandMapStep.currentRoadName;
                            if (nextNextCommandMapStep.directionCommandElements
                                    [MapDirectionCommandElement.DIRECTION_COMMAND] != null) {
                                nextVoiceCommandType = nextNextCommandMapStep
                                        .directionCommandElements
                                        [MapDirectionCommandElement.DIRECTION_COMMAND]
                                        .directionCommandType.type;
                            } else {
                                nextVoiceCommandType = nextNextCommandMapStep.calculatedDirectionType.type;
                            }
                            nextVoiceSubCommandType = nextNextCommandMapStep.calculatedDirectionType.type;
                        } else {
                            //otherwise ,next command is reach the destination.
                            nextVoiceCommandType = VoiceCommandType.DESTINATION;
                            nextVoiceSubCommandType = nextVoiceCommandType;
                            nextCommandRouteName = commandRouteName;
                        }
                    }

                    Vector distanceToNextStep = getDistanceToEndOfStep(currentWalkOnRoute,
                            currentMapStep, isHighway);
                    int commandLength = distanceToNextStep.size();
                    //the command has distance to xxx and action.
                    for (int i = 0; i < commandLength - 2; i++) {
                        VoiceCommandArg voiceCommandArg = (VoiceCommandArg)
                                distanceToNextStep.elementAt(i);
                        VoiceCommandArg[] voiceCommandArgs = new VoiceCommandArg[2];
                        voiceCommandArgs[0] = voiceCommandArg;
                        VoiceCommandArg voiceCommandArg1 =
                                new VoiceCommandArg(voiceCommandType,
                                commandRouteName);
                        voiceCommandArg1.pointIndex = voiceCommandArg.pointIndex;
                        voiceCommandArg1.routeIndex = voiceCommandArg.routeIndex;
                        voiceCommandArg1.stepIndex = voiceCommandArg.stepIndex;
                        voiceCommandArg1.commandLocation = voiceCommandArg.commandLocation;
                        voiceCommandArg1.subVoiceCommandType = voiceSubCommandType;
                        voiceCommandArgs[1] = voiceCommandArg1;
                        addToVoiceCommandQueue(voiceCommandArgs);
                    }

                    //get the last 2 commands
                    if (commandLength > 0) {
                        VoiceCommandArg[] voiceCommandArgs;
                        VoiceCommandArg voiceCommandArg;
                        VoiceCommandArg voiceCommandArg1;
                        if (commandLength > 1) {
                            voiceCommandArg = (VoiceCommandArg)
                                    distanceToNextStep.elementAt(commandLength - 2);
                            if (needIncludeNextStep) {
                                voiceCommandArgs = new VoiceCommandArg[3];
                                VoiceCommandArg voiceCommandArg2 =
                                        new VoiceCommandArg(nextVoiceCommandType,
                                        nextCommandRouteName);
                                voiceCommandArg2.pointIndex = voiceCommandArg.pointIndex;
                                voiceCommandArg2.routeIndex = voiceCommandArg.routeIndex;
                                voiceCommandArg2.stepIndex = voiceCommandArg.stepIndex;
                                voiceCommandArg2.commandLocation = voiceCommandArg.commandLocation;
                                voiceCommandArg2.optional = voiceCommandArg.optional;
                                voiceCommandArg2.subVoiceCommandType = nextVoiceSubCommandType;
                                voiceCommandArgs[2] = voiceCommandArg2;
                            } else {
                                voiceCommandArgs = new VoiceCommandArg[2];
                            }
                            voiceCommandArgs[0] = voiceCommandArg;
                            voiceCommandArg1 = new VoiceCommandArg(voiceCommandType,
                                    commandRouteName);
                            voiceCommandArg1.pointIndex = voiceCommandArg.pointIndex;
                            voiceCommandArg1.routeIndex = voiceCommandArg.routeIndex;
                            voiceCommandArg1.stepIndex = voiceCommandArg.stepIndex;
                            voiceCommandArg1.commandLocation = voiceCommandArg.commandLocation;
                            voiceCommandArg1.optional = voiceCommandArg.optional;
                            voiceCommandArg1.subVoiceCommandType = voiceSubCommandType;
                            voiceCommandArgs[1] = voiceCommandArg1;
                            addToVoiceCommandQueue(voiceCommandArgs);
                        }


                        //only the command near the turning point is not optional
                        voiceCommandArg = (VoiceCommandArg)
                                distanceToNextStep.elementAt(commandLength - 1);
                        voiceCommandArg.optional = false;
                        if (needIncludeNextStep) {
                            voiceCommandArgs = new VoiceCommandArg[2];
                            VoiceCommandArg voiceCommandArg2 =
                                    new VoiceCommandArg(nextVoiceCommandType,
                                    nextCommandRouteName);
                            voiceCommandArg2.pointIndex = voiceCommandArg.pointIndex;
                            voiceCommandArg2.routeIndex = voiceCommandArg.routeIndex;
                            voiceCommandArg2.stepIndex = voiceCommandArg.stepIndex;
                            voiceCommandArg2.commandLocation = voiceCommandArg.commandLocation;
                            voiceCommandArg2.optional = voiceCommandArg.optional;
                            voiceCommandArg2.subVoiceCommandType = nextVoiceSubCommandType;
                            voiceCommandArgs[1] = voiceCommandArg2;
                        } else {
                            voiceCommandArgs = new VoiceCommandArg[1];
                        }
                        voiceCommandArg1 = new VoiceCommandArg(voiceCommandType,
                                commandRouteName);
                        voiceCommandArg1.pointIndex = voiceCommandArg.pointIndex;
                        voiceCommandArg1.routeIndex = voiceCommandArg.routeIndex;
                        voiceCommandArg1.stepIndex = voiceCommandArg.stepIndex;
                        voiceCommandArg1.commandLocation = voiceCommandArg.commandLocation;
                        voiceCommandArg1.optional = voiceCommandArg.optional;
                        voiceCommandArg1.subVoiceCommandType = voiceSubCommandType;
                        voiceCommandArgs[0] = voiceCommandArg1;
                        addToVoiceCommandQueue(voiceCommandArgs);
                    }
                    //move one step forward
                    if (currentWalkOnRoute.stepIndex
                            < mapDirection.routes[currentWalkOnRoute.routeIndex]
                            .steps.length - 1) {
                        currentWalkOnRoute.stepIndex += 1;
                    } else if (currentWalkOnRoute.routeIndex
                            < mapDirection.routes.length - 1) {
                        currentWalkOnRoute.stepIndex = 0;
                        currentWalkOnRoute.routeIndex += 1;
                    }
                    //we have finish generate voice command for this step,
                    //move the  point index to next step.
                    currentWalkOnRoute.pointIndex = currentMapStep.lastLocationIndex;
                }

            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Voice command processor. it's driven by the location monitor.
     */
    private class VoiceCommandProcessor implements Runnable{

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 09SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *  thread used to create the voice command and put them to the queue.
         */
        public void run() {
            Log.p( Thread.currentThread().getName()+" thread started.");
            while (!stopThread) {
                try {

                    //check if this thread need to be paused.
                    checkIfNeedPauseThread();
                    synchronized (currentLocation) {
                        currentLocation.wait();
                    }
                    if (engineStatus == STATUS_NAVIGATING_ON_ROAD_MODE ||
                            engineStatus == STATUS_NAVIGATING_ON_ROAD_MODE_PREPRATION) {
                        if (needToGenerateCommand()) {
                            synchronized (voiceCommandGenerator.generatorObject) {
                                voiceCommandGenerator.generatorObject.notify();
                            }
                        }
                    }
                    if (engineStatus == STATUS_NAVIGATING_ON_ROAD_MODE) {

                        getOneVoiceCommandFromQueue();
                    }
                    else if (engineStatus == STATUS_DEVIATION){
                        generateDeviationVoiceCommand();
                    }
                    else{
                        generateOffRoadVoiceCommand();
                    }
                } catch (Exception e) {
                    //catch all exeptions.
                    Log.p(e.getMessage());
                }
            }
          Log.p( Thread.currentThread().getName()+" thread stopped!");
        }

        /**
         * current GPS location.
         */
        private final GeoLatLng currentLocation=new GeoLatLng();

        /**
         * current bearing.
         */
        private double currentBearing=-1;

        /**
         * last voice command time;
         */
        private long lastVoiceCommandTime=-1;

        /**
         * last location of off road voice command.
         */
        private double lastVoiceCommandDistance=Double.POSITIVE_INFINITY;

        /**
         * deviation command type.
         */
        private final VoiceCommandArg []reachedTargetCommandArgs
                            ={new VoiceCommandArg(VoiceCommandType.REACHED_TARGET,
                                    VoiceCommandType.REACHED_TARGET,
                            "")};

        /**
         * deviation command type.
         */
        private final VoiceCommandArg []deviationVoiceCommandArgs
                            ={new VoiceCommandArg(VoiceCommandType.TURN_AROUND,
                                    VoiceCommandType.TURN_AROUND,
                            "")};

        /**
         * clear the command Queue.
         */
        private volatile boolean clearCommandQueue=false;

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * check if need to generate more command.
         * @return
         */
        private boolean needToGenerateCommand() {
            synchronized (voiceCommandQueue) {
                if(clearCommandQueue){
                    voiceCommandQueue.removeAllElements();
                    clearCommandQueue=false;
                }
                WalkOnRoute wor = getCurrentWalkOnRoute();
                if ((voiceCommandQueue.size() < VOICE_COMMAND_QUEUE_SIZE) &&
                        wor.pointIndex<mapDirection.polyline.getVertexCount()-1) {
                    return true;
                }
                return false;
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * generate deviation voice command.
         */
        private void generateDeviationVoiceCommand() {
            long currentTime=System.currentTimeMillis();
            if(currentTime>lastVoiceCommandTime+
                    offlineNavigationVoiceCommandInterval*1000){
                lastVoiceCommandTime=currentTime;
               deviationVoiceCommandArgs[0].commandLocation = new GeoLatLng(currentLatLng);
                voiceCommandListener.voiceCommandAction(deviationVoiceCommandArgs, true);

            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *  generate off road voice command, off road voice command is generated
         * peroidically.
         */
        private void generateOffRoadVoiceCommand() {
            long currentTime=System.currentTimeMillis();
            if(currentTime>lastVoiceCommandTime+
                    offlineNavigationVoiceCommandInterval*1000){
                lastVoiceCommandTime=currentTime;
                double angle=GeoLatLng.azimuthTo(currentLatLng,destinationLatLng);
                double bearing=angle-currentBearing;

                if(bearing<0) bearing+=360;
                double distance=GeoLatLng.distance(destinationLatLng, currentLatLng)*1000;
                boolean isClosing=true;
                if(lastVoiceCommandDistance<distance){
                    isClosing=false;
                }
                lastVoiceCommandDistance = distance;
                if (voiceCommandListener != null) {
                    if (distance < 5) {
                        reachedTargetCommandArgs[0].commandLocation = new GeoLatLng(destinationLatLng);
                        voiceCommandListener.voiceCommandAction(reachedTargetCommandArgs, true);
                        //navigation is done, return to idle status.
                        engineStatus = STATUS_IDLE;
                        if (navigationListener != null) {
                            navigationListener.navigationDone();
                            navigationListener.statusChange(STATUS_NAVIGATING_OFF_ROAD_MODE,
                                    engineStatus);
                        }

                    } else {
                        sendOneOffRoadVoiceCommand(bearing, isClosing);
                    }
                }

            }
        }


        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *
         * @param bearing
         * @param isClosing
         */
        private void sendOneOffRoadVoiceCommand(double bearing,boolean isClosing){
            final Integer arg=new Integer((int)(bearing+0.5));
            final GeoLatLng argLatLng=new GeoLatLng(currentLatLng);
            VoiceCommandArg[] voiceCommandArgs=new VoiceCommandArg[2];
            if(isClosing){
                voiceCommandArgs[1]=new VoiceCommandArg(VoiceCommandType.CLOSING_TARGET,arg,true);
                voiceCommandArgs[1].subVoiceCommandType=VoiceCommandType.CLOSING_TARGET;
            }else{
                voiceCommandArgs[1]=new VoiceCommandArg(VoiceCommandType.AWAYFROM_TARGET,arg,true);
                voiceCommandArgs[1].subVoiceCommandType=VoiceCommandType.AWAYFROM_TARGET;
            }

            voiceCommandArgs[1].commandLocation=argLatLng;

            bearing-=15;
            if(bearing<0) bearing+=360;
            int oclock=((int)bearing/30) % 12;
            int commandType=VoiceCommandType.TARGET_AT_01_OCLOCK+oclock;
            voiceCommandArgs[0]=new VoiceCommandArg(commandType,arg,true);
            voiceCommandArgs[0].subVoiceCommandType=commandType;
            voiceCommandArgs[0].commandLocation=argLatLng;
            voiceCommandListener.voiceCommandAction(voiceCommandArgs, true);
        }



        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 19SEP2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Get one voice command from the command queue.
         */
        private void getOneVoiceCommandFromQueue() {
            GeoLatLng latLng;
            synchronized (currentLocation) {
                latLng = new GeoLatLng(currentLocation);

            }
            synchronized (voiceCommandQueue) {
                {
                    double distance = GeoLatLng.distance(currentLatLng,
                            destinationLatLng) * 1000;
                    if (distance < 5) {
                        //navigation is done, return to idle status.
                        engineStatus = STATUS_IDLE;
                        if (navigationListener != null) {
                            navigationListener.navigationDone();
                            navigationListener.statusChange(STATUS_NAVIGATING_ON_ROAD_MODE,
                                    engineStatus);
                        }
                        voiceCommandQueue.removeAllElements();
                    }
                }
                if (voiceCommandQueue.size() == 0) {
                    return;
                }
                int closestIndex = -1;
                int expiredIndex=-1;
                WalkOnRoute wor = getCurrentWalkOnRoute();
                int currentPointIndex=wor.pointIndex;
                double distanceLimit=COMMAND_DISTANCE_LIMIT/4;
                if(currentMonitorLocation.speed
                    > HIGHWAY_SPEED_LIMIT){
                    distanceLimit=COMMAND_DISTANCE_LIMIT;
                }
                for (int i = 0; i < voiceCommandQueue.size(); i++) {
                    VoiceCommandArg[] elems = (VoiceCommandArg[])
                            voiceCommandQueue.elementAt(i);
                    GeoLatLng latLng1 = elems[0].commandLocation;
                    int routeIndex=elems[0].routeIndex;
                    int stepIndex=elems[0].stepIndex;
                    double distance = latLng.distanceFrom(latLng1) * 1000;
                    if (distance < distanceLimit
                            && routeIndex<=wor.routeIndex &&
                            stepIndex<=wor.stepIndex) {
                        closestIndex = i;
                    }
                    if (elems[0].pointIndex < currentPointIndex) {
                        expiredIndex = i;
                    }
                }

                int needToDelete=Math.max(closestIndex, expiredIndex);
                if (needToDelete >= 0) {
                    Object[] objectToDelete = new Object[needToDelete + 1];
                    for (int i = 0; i < objectToDelete.length; i++) {
                        objectToDelete[i] = voiceCommandQueue.elementAt(i);

                    }
                    VoiceCommandArg[] lastVoiceCommandQueue=
                           (VoiceCommandArg[]) objectToDelete[needToDelete];
                    if (voiceCommandListener != null) {
                            voiceCommandListener.voiceCommandAction(lastVoiceCommandQueue,
                                    lastVoiceCommandQueue[0].optional);

                        }
                    for (int i = 0; i < objectToDelete.length; i++) {
                        voiceCommandQueue.removeElement(objectToDelete[i]);
                    }
                }

            }

        }

    }
}
