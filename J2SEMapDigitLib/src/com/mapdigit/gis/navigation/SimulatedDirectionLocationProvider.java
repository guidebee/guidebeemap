//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.navigation;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Date;
import java.util.Random;

import com.mapdigit.util.Log;

import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.geometry.GeoPolyline;
import com.mapdigit.gis.location.LocationProvider;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19SEP2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Simulation location provider, it uses current route as the simulation source.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 19/09/09
 * @author      Guidebee Pty Ltd.
 */
public class SimulatedDirectionLocationProvider extends LocationProvider
  implements Runnable{
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public SimulatedDirectionLocationProvider(){
        currentLocation.status=true;
        currentState=LocationProvider.AVAILABLE;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Start simualtion
     * @param mapDirection map direction object, cannot be null.
     * @param speed  simulation driving speed  in kph or mile/h
     * @param isMile is mile or not.
     */
    public void startSimulation(MapDirection mapDirection, int speed,
            boolean isMile) {
        synchronized (syncObject) {
            if (stopThread) {
                if (mapDirection == null) {
                    throw new IllegalArgumentException("Map Direction cannot be null");
                }
               //make a copy of the input mapDirection object.
                this.mapDirection = new MapDirection(mapDirection);
                GeoLatLng firstLocation=mapDirection.polyline.getVertex(0);
                synchronized (currentLocation) {
                    currentPointIndex = -1;
                    currentLocation.latitude = firstLocation.y;
                    currentLocation.longitude = firstLocation.x;
                }
                simulationSpeed = speed;
                this.isMile = isMile;
                stopThread = false;
                pauseThread = false;
                if (thisThread != null) {
                    thisThread.interrupt();
                    thisThread = null;
                }
                thisThread = new Thread(this,
                        "SimulationDirectionLocationProvider" + currentThreadIndex++);
                thisThread.start();
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
     * pause simulation.
     */
    public void pauseSimulation(){
        pauseThread=true;
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
    public void resumeSimulation(){
        if(pauseThread==true){
            synchronized (pauseObject) {
                   pauseObject.notify();
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
    public void stopSimulation() {
        if (!stopThread) {
            stopThread = true;
            try {
                synchronized (pauseObject) {
                    pauseObject.notifyAll();
                }
                thisThread.interrupt();
                thisThread=null;
                currentPointIndex=-1;
                currentLocation.latitude=currentLocation.longitude=0;
            } catch (Exception ex) {
                ex.printStackTrace();
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
     * set the deviation value, if not zero, the simulated location will have
     * deviation with a random value with a circle with radius of value
     * from it's exact location.
     * @param value the deviation value in meters.
     */
    public void setDeviation(int value){
        variation=value;
    }

     public void reset() {
        stopSimulation();
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void run() {
        Log.p(Thread.currentThread().getName() + " thread started!");
        currentPointIndex = 0;
        while (!stopThread) {
            try {
                if (pauseThread) {
                    synchronized (pauseObject) {
                        try {
                            Log.p("Simulation thread paused", Log.DEBUG);
                            pauseObject.wait();
                            Log.p("Simulation thread resumed", Log.DEBUG);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                if (currentPointIndex < mapDirection.polyline.getVertexCount()) {
                    GeoLatLng latLng = mapDirection.polyline.getVertex(currentPointIndex);
                    if (currentPointIndex == 0) {
                        synchronized (currentLocation) {
                            currentLocation.latitude = latLng.lat();
                            currentLocation.longitude = latLng.lng();
                            currentLocation.speed = simulationSpeed;
                            currentLocation.timeStamp = new Date();
                            currentLocation.course = 0;
                        }
                        if (locationListener != null) {
                            locationListener.locationUpdated(this, currentLocation);
                        }

                    } else {
                        GeoLatLng latLng1 = mapDirection.polyline
                                .getVertex(currentPointIndex - 1);
                        if (latLng1.x != latLng.x || latLng1.y != latLng.y) {
                            currentLocation.course = GeoLatLng.azimuthTo(latLng1, latLng);
                            double distance = GeoLatLng.distance(latLng1, latLng);
                            double deltaRadius = 0;
                            if (variation != 0) {
                                double distInMeter = distance * 1000;
                                double deltaX = (variation / distInMeter)
                                        * Math.abs(latLng1.x - latLng.x);
                                double deltaY = (variation / distInMeter)
                                        * Math.abs(latLng1.y - latLng.y);
                                deltaRadius = Math.min(deltaX, deltaY);

                            }
                            double time = 0;
                            if (isMile) {
                                time = distance / (simulationSpeed * 1.6);

                            } else {
                                time = distance / (simulationSpeed);
                            }
                            time = time * 3600 / locationInterval;
                            int numOfNewPoint = (int) time;
                            GeoPoint[] newPoints = GeoPolyline.lineSegments(latLng1, latLng,
                                    numOfNewPoint, true);
                            if (newPoints != null) {
                                for (int i = 0; i < newPoints.length; i++) {
                                    synchronized (currentLocation) {
                                        currentLocation.latitude = newPoints[i].y;
                                        currentLocation.longitude = newPoints[i].x;
                                        if (variation != 0) {
                                            Random rd = new Random(System.currentTimeMillis());
                                            int angle = rd.nextInt(360);
                                            int deviation = rd.nextInt(variation);
                                            currentLocation.latitude =
                                                    currentLocation.latitude 
                                                    + deviation * deltaRadius
                                                    * Math.sin(angle * Math.PI / 180.0);
                                            currentLocation.longitude =
                                                    currentLocation.longitude 
                                                    + deviation * deltaRadius
                                                    * Math.cos(angle * Math.PI / 180.0);
                                        }
                                        currentLocation.speed = simulationSpeed;
                                        currentLocation.timeStamp = new Date();
                                    }
                                    if (locationListener != null) {
                                        locationListener.locationUpdated(this, currentLocation);
                                    }
                                    //wait some time
                                    try {
                                        Thread.sleep(locationInterval * 1000);
                                    } catch (InterruptedException ex) {
                                        //ex.printStackTrace();
                                    }


                                }
                            }

                        }

                    }

                    currentPointIndex++;
                    if (currentPointIndex > mapDirection.polyline.getVertexCount() - 1) {
                        //last location will be send 10 times
                        for (int i = 0; i < 10; i++) {
                            latLng = mapDirection.polyline
                                    .getVertex(mapDirection.polyline
                                    .getVertexCount() - 1);
                            synchronized (currentLocation) {
                                currentLocation.latitude = latLng.lat();
                                currentLocation.longitude = latLng.lng();
                                currentLocation.speed = simulationSpeed;
                                currentLocation.timeStamp = new Date();
                                currentLocation.course = 0;
                            }
                            if (locationListener != null) {
                                locationListener.locationUpdated(this, currentLocation);
                            }
                            try {
                                Thread.sleep(locationInterval * 1000);
                            } catch (InterruptedException ex) {
                                //ex.printStackTrace();
                            }
                        }

                        break;
                    }
                } else {
                    currentPointIndex = 0;
                    GeoLatLng latLng = mapDirection.polyline.getVertex(currentPointIndex);
                    synchronized (currentLocation) {
                        currentLocation.latitude = latLng.lat();
                        currentLocation.longitude = latLng.lng();
                        currentLocation.speed = simulationSpeed;
                        currentLocation.timeStamp = new Date();
                        currentLocation.course = 0;
                    }
                    if (locationListener != null) {
                        locationListener.locationUpdated(this, currentLocation);
                    }
                }
            } catch (Exception e) {
                Log.p(Thread.currentThread().getName() 
                        + " thread stopped with exception!"+e.getMessage());
            }

        }
        Log.p(Thread.currentThread().getName() + " thread stopped!");
    }

    /**
     * Map Direction for simulation.
     */
    private MapDirection mapDirection=null;

    /**
     * allowed varation in meters,default is 0.
     */
    private int variation=0;

    /**
     * Simulation speed, default is 50Kph.
     */
    private int simulationSpeed=50;


    /**
     * use to control the stop the thread.
     */
    private volatile boolean stopThread=true;

    /**
     * current index of point in the polyline.
     */
    private volatile int currentPointIndex=-1;


    /**
     * Is the speed mile or not.
     */
    private boolean isMile=false;


    /**
     * use to control the pause/resume the thread.
     */
    private volatile boolean pauseThread=false;


    /**
     * object use to signal pause/resume.
     */
    private final Object pauseObject=new Object();

    /**
     * this thread.
     */
    private Thread thisThread=null;


    /**
     * thread index
     */
    private static int currentThreadIndex=0;


    
}
