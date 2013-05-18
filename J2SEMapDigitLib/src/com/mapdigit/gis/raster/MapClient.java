//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 23OCT2009  James Shen                              Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;

import com.mapdigit.util.Log;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 23OCT2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Map Client ,prvoide improved dynamic performance of the raster map.Asynchronized
 * for pan ,zoom in/zoom out.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 23/10/09
 * @author      Guidebee Pty Ltd.
 */
public class MapClient extends RasterMap implements Runnable,IReaderListener {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Thread to process map operation.
     */
    public void run() {
        Log.p( Thread.currentThread().getName()+" thread started");
        while (!stopRunning) {
            try {
                synchronized (mapCommands) {
                    if (mapCommands.size() > 0) {
                        removeDuplicatedMapOperation();
                    } else {
                        try {
                            mapCommands.wait();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.p( Thread.currentThread().getName()+" thread stopped");
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void panDirection(int dx, int dy) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.param_dx = dx;
        mapOperationParameters.param_dy = dy;
        mapOperationParameters.mapCommand = MapOperation.MAP_PAN_DIRECTION;
        putMapOperation(mapOperationParameters);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void panTo(GeoLatLng center) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.param_center = center;
        mapOperationParameters.mapCommand = MapOperation.MAP_PAN_TO;
        putMapOperation(mapOperationParameters);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setCenter(GeoLatLng center, int zoomLevel) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.param_center = center;
        mapOperationParameters.param_zoom = zoomLevel;
        mapOperationParameters.param_maptype = getMapType();
        mapOperationParameters.mapCommand = MapOperation.MAP_SET_CENTER;
        putMapOperation(mapOperationParameters);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setCenter(GeoLatLng center, int zoomLevel, int mapType) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.param_center = center;
        mapOperationParameters.param_zoom = zoomLevel;
        mapOperationParameters.param_maptype = mapType;
        mapOperationParameters.mapCommand = MapOperation.MAP_SET_CENTER;
        putMapOperation(mapOperationParameters);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void zoomIn() {
        currentZoomLevel = currentZoomLevel + 1;
        currentZoomLevel = Math.min(
                maxMapZoomLevel,
                currentZoomLevel);
        setCenter(getScreenCenter(), currentZoomLevel);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void zoomOut() {
        currentZoomLevel = currentZoomLevel - 1;
        currentZoomLevel = Math.max(
                minMapZoomLevel,
                currentZoomLevel);
        setCenter(getScreenCenter(), currentZoomLevel);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setZoom(int level) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.mapCommand = MapOperation.MAP_SET_ZOOM;
        mapOperationParameters.param_zoom = level;
        putMapOperation(mapOperationParameters);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setMapType(int mapType) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.mapCommand = MapOperation.MAP_SET_MAP_TYPE;
        mapOperationParameters.param_maptype = mapType;
        putMapOperation(mapOperationParameters);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    public void setMapDownloadingListener(IReaderListener mapReadingListener) {
        this.mapReadingListener = mapReadingListener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void resize(GeoLatLngBounds bounds) {
        MapOperationParameters mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.mapCommand = MapOperation.MAP_RESIZE;
        mapOperationParameters.param_bounds = bounds;
        putMapOperation(mapOperationParameters);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * start the map client.
     */
    public void start() {
        stop();
        mapClientThread = new Thread(this, "MapClient");
        //mapClientThread.setPriority(Thread.MIN_PRIORITY + 2);
        processMapOperationThread = new ProcessMapOperationThread();
        //processMapOperationThread.setPriority(Thread.MIN_PRIORITY + 1);
        watchDogService.addThreadToWatchList(processMapOperationThread,
                "processMapOperationThread");
        Hashtable threadList = getMapTileDownloadManager().getThreads();
        Enumeration keys = threadList.keys();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            Thread workThread = (Thread) threadList.get(name);
            watchDogService.addThreadToWatchList(workThread, name);

        }
        mapClientThread.start();
        processMapOperationThread.start();
        getMapTileDownloadManager().start();
        watchDogService.start();

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * restart the map client
     */
    public void restart() {
        synchronized (restartSyncObject) {
            getMapTileDownloadManager().restartWorker();
            Hashtable threadList = getMapTileDownloadManager().getThreads();
            Enumeration keys = threadList.keys();
            while (keys.hasMoreElements()) {
                String name = (String) keys.nextElement();
                Thread workThread = (Thread) threadList.get(name);
                watchDogService.addThreadToWatchList(workThread, name);

            }

            if (processMapOperationThread != null) {
                processMapOperationThread.stopProcessThreadRunning = true;
                 synchronized (mapDispatchedCommands) {
                     mapDispatchedCommands.notifyAll();
                 }
                processMapOperationThread = null;
            }
            processMapOperationThread = new ProcessMapOperationThread();
            //processMapOperationThread.setPriority(Thread.MIN_PRIORITY + 1);
            watchDogService.addThreadToWatchList(processMapOperationThread,
                    "processMapOperationThread");
        }
        processMapOperationThread.start();

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * stop the map client.
     */
    public void stop() {
        if (mapClientThread != null) {
            try {
                stopRunning = true;
                synchronized (mapCommands){
                    mapCommands.notifyAll();
                }
                watchDogService.stop();
                processMapOperationThread.stopProcessThreadRunning=true;
                 synchronized (mapDispatchedCommands) {
                     mapDispatchedCommands.notifyAll();
                 }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            mapClientThread = null;
            stopRunning = false;
        }
        getMapTileDownloadManager().stop();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new Map client with given width and height.
     * @param width the width of the map image.
     * @param height the height of the map image.
     */
    public MapClient(int width, int height)  {
        this(width,height,MapType.MICROSOFTMAP);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new Map client with given width and height.
     * @param width the width of the map image.
     * @param height the height of the map image.
     * @param mapType map type.
     */
    public MapClient(int width, int height, int mapType)  {
        super(width,height,mapType);
        this.setDownloadManager(new  MapTileDownloadManager(this));
        rasterMap =this;
        watchDogService=new WatchdogService(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new Map client with given width and height.
     * @param width the width of the map image.
     * @param height the height of the map image.
     * @param mapType map type.
     * @param mapTileDownloadManager map tile download manager.
     */
    public MapClient(int width, int height, int mapType,
            MapTileDownloadManager mapTileDownloadManager) {
        super(width, height, mapType, mapTileDownloadManager);
        rasterMap =this;
        watchDogService=new WatchdogService(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public MapClient(int width, int height,
            MapTileDownloadManager mapTileDownloadManager)  {
        this(width,height,MapType.MICROSOFTMAP,mapTileDownloadManager);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 23OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Process map operation in a thread.
     * @param mapOperationParameters
     */
    private void processMapOperation(MapOperationParameters currentMapOperationParameters) {
        MapOperationParameters mapOperationParameters = null;
        mapOperationParameters = new MapOperationParameters();
        mapOperationParameters.mapCommand = currentMapOperationParameters.mapCommand;
        mapOperationParameters.param_center = currentMapOperationParameters.param_center;
        mapOperationParameters.param_dx = currentMapOperationParameters.param_dx;
        mapOperationParameters.param_dy = currentMapOperationParameters.param_dy;
        mapOperationParameters.param_maptype = currentMapOperationParameters.param_maptype;
        mapOperationParameters.param_zoom = currentMapOperationParameters.param_zoom;
        mapOperationParameters.param_bounds = currentMapOperationParameters.param_bounds;
        switch (mapOperationParameters.mapCommand) {
            case MapOperation.MAP_SET_CENTER:
                super.setCenter(mapOperationParameters.param_center,
                        mapOperationParameters.param_zoom,
                        mapOperationParameters.param_maptype);
                break;
            case MapOperation.MAP_RESIZE:
                super.resize(mapOperationParameters.param_bounds);
                break;
            case MapOperation.MAP_PAN_DIRECTION:
                super.panDirection(mapOperationParameters.param_dx,
                        mapOperationParameters.param_dy);
                break;

        }
        currentZoomLevel = getZoom();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 31AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add one map operation.
     * @param command
     */
    private void putMapOperation(MapOperationParameters command) {
        synchronized (mapCommands) {
            mapCommands.addElement(command);
            mapCommands.notify();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 31AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Remove duplicated map operation in the operation array.
     */
    private void removeDuplicatedMapOperation() {
        MapOperationParameters mapPanDirectionOperationParameters = null;
        MapOperationParameters mapSetCenterOperationParameters = null;
        MapOperationParameters mapResizeOperationParameters = null;
        MapOperationParameters mapUpdateMapOperationParameters = null;

        for (int i = 0; i < mapCommands.size(); i++) {
            MapOperationParameters mapOperationParameters =
                    (MapOperationParameters) mapCommands.elementAt(i);
            switch (mapOperationParameters.mapCommand) {
                case MapOperation.MAP_PAN_DIRECTION:
                     {
                        if (mapPanDirectionOperationParameters == null) {
                            mapPanDirectionOperationParameters = mapOperationParameters;
                        } else {
                            mapPanDirectionOperationParameters.param_dx += mapOperationParameters.param_dx;
                            mapPanDirectionOperationParameters.param_dy += mapOperationParameters.param_dy;

                        }
                    }
                    break;
                case MapOperation.MAP_PAN_TO:
                case MapOperation.MAP_SET_CENTER:
                case MapOperation.MAP_SET_MAP_TYPE:
                case MapOperation.MAP_ZOOM_IN:
                case MapOperation.MAP_ZOOM_OUT:
                case MapOperation.MAP_SET_ZOOM:
                     {
                        if (mapSetCenterOperationParameters == null) {
                            mapSetCenterOperationParameters =
                                    new MapOperationParameters();
                            mapSetCenterOperationParameters.mapCommand = MapOperation.MAP_SET_CENTER;
                        }
                        switch (mapOperationParameters.mapCommand) {
                            case MapOperation.MAP_PAN_TO:
                                mapSetCenterOperationParameters.param_center = mapOperationParameters.param_center;
                                mapSetCenterOperationParameters.param_maptype = getMapType();
                                mapSetCenterOperationParameters.param_zoom = getZoom();
                                break;
                            case MapOperation.MAP_SET_CENTER:
                                mapSetCenterOperationParameters.param_center = mapOperationParameters.param_center;
                                mapSetCenterOperationParameters.param_maptype = mapOperationParameters.param_maptype;
                                mapSetCenterOperationParameters.param_zoom = mapOperationParameters.param_zoom;
                                break;
                            case MapOperation.MAP_SET_MAP_TYPE:
                                mapSetCenterOperationParameters.param_center = getCenter();
                                mapSetCenterOperationParameters.param_maptype = mapOperationParameters.param_maptype;
                                mapSetCenterOperationParameters.param_zoom = getZoom();
                                break;
                            case MapOperation.MAP_ZOOM_IN:
                                mapSetCenterOperationParameters.param_center = getCenter();
                                mapSetCenterOperationParameters.param_maptype = getMapType();
                                currentZoomLevel = currentZoomLevel + 1;
                                currentZoomLevel =
                                        Math.min(
                                        maxMapZoomLevel,
                                        currentZoomLevel);
                                mapSetCenterOperationParameters.param_zoom = currentZoomLevel;
                                break;
                            case MapOperation.MAP_ZOOM_OUT:
                                mapSetCenterOperationParameters.param_center = getCenter();
                                mapSetCenterOperationParameters.param_maptype = getMapType();
                                currentZoomLevel = currentZoomLevel - 1;
                                currentZoomLevel = Math.max(
                                        minMapZoomLevel,
                                        currentZoomLevel);
                                mapSetCenterOperationParameters.param_zoom = currentZoomLevel;
                                break;
                            case MapOperation.MAP_SET_ZOOM:
                                mapSetCenterOperationParameters.param_center = getCenter();
                                mapSetCenterOperationParameters.param_maptype = getMapType();
                                mapSetCenterOperationParameters.param_zoom = mapOperationParameters.param_zoom;
                                break;

                        }
                    }
                    break;
                case MapOperation.MAP_RESIZE:
                    mapResizeOperationParameters = mapOperationParameters;
                    break;
                case MapOperation.MAP_UPDATE_MAP:
                    mapUpdateMapOperationParameters = mapOperationParameters;
                    break;


            }
        }
        mapCommands.removeAllElements();

        synchronized (mapDispatchedCommands) {
            if (mapPanDirectionOperationParameters != null) {
                mapDispatchedCommands.addElement(mapPanDirectionOperationParameters);
            }
            if (mapResizeOperationParameters != null) {
                mapDispatchedCommands.addElement(mapResizeOperationParameters);
            }
            if (mapSetCenterOperationParameters != null) {
                mapDispatchedCommands.addElement(mapSetCenterOperationParameters);
            }
            if (mapUpdateMapOperationParameters != null) {
                mapDispatchedCommands.addElement(mapUpdateMapOperationParameters);
            }
            if (!mapDispatchedCommands.isEmpty()) {
                mapDispatchedCommands.notify();
            }
        }
    }

    public boolean checkHealth() {
        if (mapClientThread == null || stopRunning) {
            return false;
        }
        return true;
    }


    private IReaderListener mapReadingListener = null;
    /**
     * Map operation thread.
     */
    //private final Object mapCommandListMutex = new Object();
    private final Vector mapCommands = new Vector();    //private final Object mapDispatchedCommandListMutex = new Object();
    private final Vector mapDispatchedCommands = new Vector();
    private ProcessMapOperationThread processMapOperationThread = null;
    private volatile boolean stopRunning = false;
    private Thread mapClientThread = null;
    /**
     * Server map objects.
     */
    private final RasterMap rasterMap;
    private final WatchdogService watchDogService;
    private int currentZoomLevel = 1;
    private final Object restartSyncObject = new Object();


    class ProcessMapOperationThread extends Thread {

        public ProcessMapOperationThread(){
            super("ProcessMapOperationThread");
        }

        volatile boolean stopProcessThreadRunning = false;

        ////////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS ------------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ----------------------
        // 23SEP2008  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////////
        /**
         * Remove duplicated map operation in the operation array.
         */
        private void removeDispatchedDuplicatedMapOperation() {

            MapOperationParameters mapPanDirectionOperationParameters = null;
            MapOperationParameters mapSetCenterOperationParameters = null;
            MapOperationParameters mapResizeOperationParameters = null;
            MapOperationParameters mapUpdateMapOperationParameters = null;
            for (int i = 0; i < mapDispatchedCommands.size(); i++) {
                MapOperationParameters mapOperationParameters =
                        (MapOperationParameters) mapDispatchedCommands.elementAt(i);
                switch (mapOperationParameters.mapCommand) {
                    case MapOperation.MAP_PAN_DIRECTION:
                         {
                            if (mapPanDirectionOperationParameters == null) {
                                mapPanDirectionOperationParameters = mapOperationParameters;
                            } else {
                                mapPanDirectionOperationParameters.param_dx += mapOperationParameters.param_dx;
                                mapPanDirectionOperationParameters.param_dy += mapOperationParameters.param_dy;

                            }
                        }
                        break;
                    case MapOperation.MAP_PAN_TO:
                    case MapOperation.MAP_SET_CENTER:
                    case MapOperation.MAP_SET_MAP_TYPE:
                    case MapOperation.MAP_ZOOM_IN:
                    case MapOperation.MAP_ZOOM_OUT:
                    case MapOperation.MAP_SET_ZOOM:
                         {
                            if (mapSetCenterOperationParameters == null) {
                                mapSetCenterOperationParameters =
                                        new MapOperationParameters();
                                mapSetCenterOperationParameters.mapCommand = MapOperation.MAP_SET_CENTER;
                            }
                            switch (mapOperationParameters.mapCommand) {
                                case MapOperation.MAP_PAN_TO:
                                    mapSetCenterOperationParameters.param_center = mapOperationParameters.param_center;
                                    mapSetCenterOperationParameters.param_maptype = rasterMap.getMapType();
                                    mapSetCenterOperationParameters.param_zoom = rasterMap.getZoom();
                                    break;
                                case MapOperation.MAP_SET_CENTER:
                                    mapSetCenterOperationParameters.param_center = mapOperationParameters.param_center;
                                    mapSetCenterOperationParameters.param_maptype = mapOperationParameters.param_maptype;
                                    mapSetCenterOperationParameters.param_zoom = mapOperationParameters.param_zoom;
                                    break;
                                case MapOperation.MAP_SET_MAP_TYPE:
                                    mapSetCenterOperationParameters.param_center = rasterMap.getCenter();
                                    mapSetCenterOperationParameters.param_maptype = mapOperationParameters.param_maptype;
                                    mapSetCenterOperationParameters.param_zoom = rasterMap.getZoom();
                                    break;
                                case MapOperation.MAP_ZOOM_IN:
                                    mapSetCenterOperationParameters.param_center = rasterMap.getCenter();
                                    mapSetCenterOperationParameters.param_maptype = rasterMap.getMapType();
                                    currentZoomLevel = currentZoomLevel + 1;
                                    currentZoomLevel =
                                            Math.min(
                                            maxMapZoomLevel,
                                            currentZoomLevel);
                                    mapSetCenterOperationParameters.param_zoom = currentZoomLevel;
                                    break;
                                case MapOperation.MAP_ZOOM_OUT:
                                    mapSetCenterOperationParameters.param_center = rasterMap.getCenter();
                                    mapSetCenterOperationParameters.param_maptype = rasterMap.getMapType();
                                    currentZoomLevel = currentZoomLevel - 1;
                                    currentZoomLevel = Math.max(
                                            minMapZoomLevel,
                                            currentZoomLevel);
                                    mapSetCenterOperationParameters.param_zoom = currentZoomLevel;
                                    break;
                                case MapOperation.MAP_SET_ZOOM:
                                    mapSetCenterOperationParameters.param_center = rasterMap.getCenter();
                                    mapSetCenterOperationParameters.param_maptype = rasterMap.getMapType();
                                    mapSetCenterOperationParameters.param_zoom = mapOperationParameters.param_zoom;
                                    break;

                            }
                        }
                        break;
                    case MapOperation.MAP_RESIZE:
                        mapResizeOperationParameters = mapOperationParameters;
                        break;
                    case MapOperation.MAP_UPDATE_MAP:
                        mapUpdateMapOperationParameters = mapOperationParameters;
                        break;


                }
            }
            mapDispatchedCommands.removeAllElements();
            if (mapPanDirectionOperationParameters != null) {
                mapDispatchedCommands.addElement(mapPanDirectionOperationParameters);
            }
            if (mapResizeOperationParameters != null) {
                mapDispatchedCommands.addElement(mapResizeOperationParameters);
            }
            if (mapSetCenterOperationParameters != null) {
                mapDispatchedCommands.addElement(mapSetCenterOperationParameters);
            }
            if (mapUpdateMapOperationParameters != null) {
                mapDispatchedCommands.addElement(mapUpdateMapOperationParameters);
            }


        }

        public void run() {
            Log.p( Thread.currentThread().getName()+" thread started");
            while (!stopProcessThreadRunning) {
                MapOperationParameters mapOperationParameters = null;
                synchronized (mapDispatchedCommands) {
                    if (mapDispatchedCommands.size() == 0) {
                        try {
                            mapDispatchedCommands.wait();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        removeDispatchedDuplicatedMapOperation();
                        mapOperationParameters = (MapOperationParameters) mapDispatchedCommands.lastElement();
                        if (mapOperationParameters != null) {
              
                            mapDispatchedCommands.removeElement(mapOperationParameters);
                        }
                    }

                }
                if (mapOperationParameters != null) {
                    processMapOperation(mapOperationParameters);
                }



            }
            Log.p( Thread.currentThread().getName()+" thread stopped");
        }
    }

    public void readProgress(int bytes, int total) {
        if(mapReadingListener!=null){
            mapReadingListener.readProgress(bytes, total);
        }
    }
}
/**
 * map operation
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 31/08/09
 * @author      Guidebee Pty Ltd.
 */
interface MapOperation {

    public final static int MAP_ZOOM_OUT = 1;
    public final static int MAP_ZOOM_IN = 2;
    public final static int MAP_SET_ZOOM = 3;
    public final static int MAP_SET_MAP_TYPE = 4;
    public final static int MAP_RESIZE = 5;
    public final static int MAP_PAN_DIRECTION = 6;
    public final static int MAP_PAN_TO = 7;
    public final static int MAP_SET_CENTER = 8;
    public final static int MAP_UPDATE_MAP = 9;
}

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 31AUG2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * map operation parameters.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 31/08/09
 * @author      Guidebee Pty Ltd.
 */
class MapOperationParameters {

    public int mapCommand = 0;
    public int param_dx = 0;
    public int param_dy = 0;
    public int param_maptype = 0;
    public GeoLatLng param_center = null;
    public int param_zoom = 0;
    public GeoLatLngBounds param_bounds = null;
}

