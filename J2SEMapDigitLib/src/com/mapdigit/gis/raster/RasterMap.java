//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 02SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------


import com.mapdigit.ajax.Request;
import com.mapdigit.util.Log;

import com.mapdigit.drawing.Pen;
import com.mapdigit.drawing.geometry.Rectangle;

import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;
import com.mapdigit.gis.service.DigitalMapService;

import java.util.Random;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 02SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * RasterMap a map class uses to display raster map.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 02/09/10
 * @author      Guidebee Pty Ltd.
 */
public class RasterMap extends DigitalMap {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get total downloaded bytes, (approx)
     * @return the total byte has be downloaded
     */
    public static long getTotalDownloadedBytes() {
        return Request.totaldownloadedBytes
                + MapTileAbstractReader.totaldownloadedBytes;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new ServerMap with given width and height.
     * @param width the width of the map image.
     * @param height the height of the map image.
     * @param mapTileDownloadManager map tile download manager.
     * @throws InvalidLicenceException invalid licence.
     */
    public RasterMap(int width, int height,
            MapTileDownloadManager mapTileDownloadManager)
            {
        this(width, height, MapType.MICROSOFTMAP, mapTileDownloadManager);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new ServerMap with given width and height.
     * @param width the width of the map image.
     * @param height the height of the map image.
     * @param mapType map type.
     * @param mapTileDownloadManager map tile download manager.
     * @throws InvalidLicenceException invalid licence.
     */
    public RasterMap(int width, int height, int mapType,
            MapTileDownloadManager mapTileDownloadManager)
            {
        super(width, height);
        screenRectangle = new Rectangle(0, 0, screenSize.width,
                screenSize.height);
        if(MapConfiguration.lowMemoryMode){
           mapTileEngine = new DefaultMapTileEngine(width, height,
                   mapTileDownloadManager, this);
        }else{
           mapTileEngine = new ArrayTiledMapTileEngine(width, height,
                   mapTileDownloadManager, this);
        }
        this.mapType = mapType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for map drawing .
     * @param mapDrawingListener callback when mapdrawing is done.

     */
    public void setMapDrawingListener(IMapDrawingListener mapDrawingListener) {
        mapTileEngine.setMapDrawingListener(mapDrawingListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setScreenSize(int width, int height) {
        synchronized (syncObject) {
            super.setScreenSize(width, height);
            screenSize.height = height;
            screenSize.width = width;
            mapTileEngine.setScreenSize(width, height);
            screenRectangle = new Rectangle(0, 0, screenSize.width,
                    screenSize.height);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set current  map service type.
     * @param mapServiceType map service type.defined in DigitalMapService.
     */
    public void setCurrentMapService(int mapServiceType){
        digitalMapService=DigitalMapService.getCurrentMapService(mapServiceType);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the map view to the given center.
     * @param center the center latitude,longitude of the map.
     * @param zoomLevel the zoom Level of the map [0,17].
     * @param mapType msn map, yahoo map etc.
     */
    public void setCenter(GeoLatLng center, int zoomLevel, int mapType) {
        synchronized (syncObject) {
            this.mapType = mapType;
            super.setCenter(center, zoomLevel);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * is the point in current screen (is shown or not).
     * @param pt point to be tested.
     * @return true is in screen range.
     */
    public boolean isPointVisible(GeoLatLng pt) {
        synchronized (syncObject) {
            GeoPoint screenPt = fromLatLngToMapPixel(pt);
            screenRectangle.setX(mapTileEngine.getScreenOffsetX());
            screenRectangle.setY(mapTileEngine.getScreenOffsetY());
            return screenRectangle.contains((int) screenPt.x, (int) screenPt.y);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the latitude,longitude of the screen center.
     * @return the center of the screen in latitude,longititude pair.
     */
    public GeoLatLng getScreenCenter() {
        synchronized (syncObject) {
           return mapTileEngine.getScreenCenter();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Increments zoom level by one.
     */
    public void zoomIn() {

        getMapTileDownloadManager().clearTaskList();
        synchronized (syncObject) {
            mapTileEngine.zoomIn();
            GeoLatLng center=getScreenCenter();
            this.mapCenterPt.x = center.x;
            this.mapCenterPt.y = center.y;
            synchronized (mapLayers) {
                for (int i = 0; i < mapLayers.size(); i++) {
                    MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                    GeoLatLng layerCenter = mapLayer.getCenter();
                    layerCenter.x = center.x;
                    layerCenter.y = center.y;
                }
            }
            super.zoomIn();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Decrements zoom level by one.
     */
    public void zoomOut() {
        getMapTileDownloadManager().clearTaskList();
        synchronized (syncObject) {
            mapTileEngine.zoomOut();
            GeoLatLng center = getScreenCenter();
            this.mapCenterPt.x = center.x;
            this.mapCenterPt.y = center.y;
            synchronized (mapLayers) {
                for (int i = 0; i < mapLayers.size(); i++) {
                    MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                    GeoLatLng layerCenter = mapLayer.getCenter();
                    layerCenter.x = center.x;
                    layerCenter.y = center.y;
                }
            }
            super.zoomOut();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Starts a pan with given distance in pixels.
     * directions. +1 is right and down, -1 is left and up, respectively.
     * @param dx x offset.
     * @param dy y offset.
     */
    public void panDirection(final int dx, final int dy) {
        synchronized (syncObject) {
           mapTileEngine.panDirection(dx, dy);
           GeoLatLng center=getScreenCenter();
            synchronized (mapLayers) {
                for (int i = 0; i < mapLayers.size(); i++) {
                    MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                    GeoLatLng layerCenter=mapLayer.getCenter();
                    layerCenter.x=center.x;
                    layerCenter.y=center.y;
                    mapLayer.panDirection(dx, dy);
                }
            }

        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets current map type
     * @param mapType new map type.
     */
    public void setMapType(int mapType) {
        getMapTileDownloadManager().clearTaskList();
        synchronized (syncObject) {
            this.mapType = mapType;
            drawMapCanvas();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets new route pen
     * @param routePen new route pen.
     */
    public void setRoutePen(Pen routePen) {
        mapTileEngine.setRoutePen(routePen);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the map type.
     * @return the map type.
     */
    public int getMapType() {
        synchronized (syncObject) {
            return this.mapType;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Stores the current map position and zoom level for later
     * recall by returnToSavedPosition().
     */
    public void savePosition() {
        synchronized (syncObject) {
            storedZoomLevel = mapZoomLevel;
            storedPosition = new GeoLatLng(this.mapCenterPt);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Restores the map view that was saved by savePosition().
     */
    public void returnToSavedPosition() {
        synchronized (syncObject) {
            if (storedPosition != null) {
                this.mapZoomLevel = storedZoomLevel;
                panTo(storedPosition);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear map cache.
     */
    public void clearMapCache() {
        mapTileEngine.clearMapCache();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Restore map image cache from persistent memory.
     */
    public void restoreMapCache() {
        mapTileEngine.restoreMapCache();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Save map image cache to persistent memory.
     */
    public void saveMapCache() {
        mapTileEngine.saveMapCache();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void paint(IGraphics graphics, int offsetX, int offsetY) {
        mapTileEngine.paint(graphics, offsetX, offsetY);
        drawLogo(graphics);
        super.paint(graphics, offsetX, offsetY);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw map canvas
     */
    public void drawMapCanvas() {
        synchronized (syncObject) {
            mapTileEngine.drawMapCanvas();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * convert laititude,longitude pair to the coordinates on screen.
     * @param latlng the latitude,longitude location.
     * @return x,y coordniate on screen.
     */
    public GeoPoint fromLatLngToScreenPixel(GeoLatLng latlng) {
        synchronized (syncObject) {
            return mapTileEngine.fromLatLngToScreenPixel(latlng);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * convert the coordinates on screen to laititude,longitude pair .
     * @param pt (x,y) coordinates on screen
     * @return the latitude,longitude position.
     */
    public GeoLatLng fromScreenPixelToLatLng(GeoPoint pt) {
        synchronized (syncObject) {
            return mapTileEngine.fromScreenPixelToLatLng(pt);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set routing direction.
     * @param newDirection new routing direction.
     */
    public void setMapDirection(MapDirection newDirection) {
        mapTileEngine.setMapDirection(newDirection);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set routing direction.
     * @param newDirections new routing directions.
     */
    public void setMapDirections(MapDirection[] newDirections) {
        mapTileEngine.setMapDirections(newDirections);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clear map (routes etc).
     */
    public void clearMapDirection() {
        mapTileEngine.clearMapDirection();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map tile download manager.
     * @return the map tile download manager.
     */
    public MapTileDownloadManager getMapTileDownloadManager() {
        return mapTileEngine.getMapTileDownloadManager();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set download manager
     * @param mapTileDownloadManager
     */
    public void setDownloadManager(MapTileDownloadManager mapTileDownloadManager) {
        mapTileEngine.setDownloadManager(mapTileDownloadManager);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param width
     * @param height
     * @param mapType
     */
    RasterMap(int width, int height, int mapType)  {
        this(width, height, mapType, null);
    }

    private void drawLogo(IGraphics graphics) {
        
    }
    //turn on the pan thread or not.
    boolean usePanThread = true;

    void setCenterCommand() {
        if (usePanThread) {
            pandirectionThread.setCenterCommand();
        } else {
            setCenter(getScreenCenter(), mapZoomLevel, mapType);
        }
    }

    void updatedMapCommand() {
        if (usePanThread) {
            pandirectionThread.updatedMapCommand();
        } else {
            mapTileEngine.drawUpdatedMapCanvas();
        }
    }

    class PandirectionThread extends Thread {

        private volatile int command = -1;
        private final Object panSyncObject = new Object();
        private SetCenterThread setCenterThread = new SetCenterThread();
        private UpdateMapThread updateMapThread = new UpdateMapThread();

        PandirectionThread() {
            super("PandirectionThread");
            //setCenterThread.setPriority(Thread.MAX_PRIORITY);
            //updateMapThread.setPriority(Thread.MAX_PRIORITY);
        }

        public void start() {
            super.start();
            setCenterThread.start();
            updateMapThread.start();
        }

        public void run() {
            Log.p(Thread.currentThread().getName() + " thread started");
            while (!panStopThread) {
                switch (command) {
                    case 1:
                        setCenterThread.setCenterCommand();
                        break;
                    case 2:
                        updateMapThread.updateMapCommand();
                        break;
                    default:

                        break;
                }
                synchronized (panSyncObject) {
                    try {
                        panSyncObject.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }


            }
            Log.p(Thread.currentThread().getName() + " thread stopped");
        }

        public void stopThread() {
            panStopThread = true;
            synchronized (panSyncObject) {
                panSyncObject.notifyAll();
            }
            synchronized (setCenterThread.panSyncObject) {
                setCenterThread.panSyncObject.notifyAll();
            }
            synchronized (updateMapThread.panSyncObject) {
                updateMapThread.panSyncObject.notifyAll();
            }
            pandirectionThread = null;
            setCenterThread = null;
            updateMapThread = null;
        }

        public void setCenterCommand() {
            synchronized (panSyncObject) {
                command = 1;
                panSyncObject.notifyAll();

            }
        }

        public void updatedMapCommand() {
            synchronized (panSyncObject) {
                command = 2;
                panSyncObject.notifyAll();

            }
        }
    }

    PandirectionThread getNewPandirectionThread() {
        pandirectionThread = new PandirectionThread();
        //pandirectionThread.setPriority(Thread.MIN_PRIORITY);
        return pandirectionThread;
    }

    private static Random rand=new Random();
    /**
     * screen rectangle
     */
    private Rectangle screenRectangle = null;

    /**
     * map tile engine.
     */
    MapTileEngine mapTileEngine;

    /**
     * stored position
     */
    private GeoLatLng storedPosition = null;
    /**
     * stored zoom Level
     */
    private int storedZoomLevel = 0;
    private volatile boolean panStopThread = false;
    private PandirectionThread pandirectionThread = null;

     private static byte[] imageGuidebeeLogoArray = new byte[]{
        (byte)0x89,(byte)0x50,(byte)0x4e,(byte)0x47,(byte)0x0d,(byte)0x0a,(byte)0x1a,(byte)0x0a,
        (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0d,(byte)0x49,(byte)0x48,(byte)0x44,(byte)0x52,
        (byte)0x00,(byte)0x00,(byte)0x00,(byte)0xe2,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x37,
        (byte)0x08,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x11,(byte)0xe3,(byte)0x96,
        (byte)0x4b,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x67,(byte)0x41,(byte)0x4d,
        (byte)0x41,(byte)0x00,(byte)0x00,(byte)0xb1,(byte)0x8e,(byte)0x7c,(byte)0xfb,(byte)0x51,
        (byte)0x93,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x20,(byte)0x63,(byte)0x48,(byte)0x52,
        (byte)0x4d,(byte)0x00,(byte)0x00,(byte)0x7a,(byte)0x25,(byte)0x00,(byte)0x00,(byte)0x80,
        (byte)0x83,(byte)0x00,(byte)0x00,(byte)0xf9,(byte)0xff,(byte)0x00,(byte)0x00,(byte)0x80,
        (byte)0xe8,(byte)0x00,(byte)0x00,(byte)0x75,(byte)0x30,(byte)0x00,(byte)0x00,(byte)0xea,
        (byte)0x60,(byte)0x00,(byte)0x00,(byte)0x3a,(byte)0x97,(byte)0x00,(byte)0x00,(byte)0x17,
        (byte)0x6f,(byte)0x97,(byte)0xa9,(byte)0x99,(byte)0xd4,(byte)0x00,(byte)0x00,(byte)0x09,
        (byte)0x4e,(byte)0x49,(byte)0x44,(byte)0x41,(byte)0x54,(byte)0x78,(byte)0x9c,(byte)0x62,
        (byte)0xfc,(byte)0xff,(byte)0xff,(byte)0x3f,(byte)0xc3,(byte)0x28,(byte)0x18,(byte)0x05,
        (byte)0xa3,(byte)0x60,(byte)0x60,(byte)0x01,(byte)0x40,(byte)0x00,(byte)0x31,(byte)0x0d,
        (byte)0xb4,(byte)0x03,(byte)0x46,(byte)0xc1,(byte)0x28,(byte)0x18,(byte)0x05,(byte)0x0c,
        (byte)0x0c,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0x9a,(byte)0x11,(byte)0x47,(byte)0xc1,
        (byte)0x28,(byte)0x18,(byte)0x04,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x33,
        (byte)0xe2,(byte)0x28,(byte)0x18,(byte)0x05,(byte)0x83,(byte)0x00,(byte)0x00,(byte)0x04,
        (byte)0xd0,(byte)0x68,(byte)0x46,(byte)0x1c,(byte)0x05,(byte)0xa3,(byte)0x60,(byte)0x10,
        (byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1a,(byte)0xcd,(byte)0x88,(byte)0xa3,(byte)0x60,
        (byte)0x14,(byte)0x0c,(byte)0x02,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0xa3,(byte)0x19,
        (byte)0x71,(byte)0x14,(byte)0x8c,(byte)0x82,(byte)0x41,(byte)0x00,(byte)0x00,(byte)0x02,
        (byte)0x68,(byte)0x34,(byte)0x23,(byte)0x8e,(byte)0x82,(byte)0x51,(byte)0x30,(byte)0x08,
        (byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8d,(byte)0x66,(byte)0xc4,(byte)0xa1,(byte)0x0a,
        (byte)0x2e,(byte)0x5e,(byte)0x14,(byte)0x60,(byte)0x98,(byte)0x3d,(byte)0xdb,(byte)0x8d,
        (byte)0xa1,(byte)0xb6,(byte)0x36,(byte)0x65,(byte)0xa0,(byte)0x9d,(byte)0x82,(byte)0x01,
        (byte)0x6e,(byte)0xdd,(byte)0x62,(byte)0x63,(byte)0x58,(byte)0xb1,(byte)0xc2,(byte)0x82,
        (byte)0x21,(byte)0x33,(byte)0xb3,(byte)0x62,(byte)0xa0,(byte)0x9d,(byte)0x32,(byte)0x54,
        (byte)0x00,(byte)0x40,(byte)0x00,(byte)0x31,(byte)0x52,(byte)0x6d,(byte)0x42,(byte)0x1f,
        (byte)0x14,(byte)0xf8,(byte)0x4f,(byte)0x9e,(byte)0xc8,(byte)0x30,(byte)0x9c,(byte)0x3f,
        (byte)0x6f,(byte)0x04,(byte)0xe6,(byte)0x1f,(byte)0x3c,(byte)0x68,(byte)0xcf,(byte)0xb0,
        (byte)0x79,(byte)0xb3,(byte)0x0f,(byte)0x86,(byte)0xba,(byte)0x8c,(byte)0x8c,(byte)0x99,
        (byte)0x0c,(byte)0x2a,(byte)0x2a,(byte)0x77,(byte)0xc0,(byte)0x6c,(byte)0x43,(byte)0xc3,
        (byte)0x73,(byte)0x0c,(byte)0x5c,(byte)0x5c,(byte)0xdf,(byte)0x18,(byte)0x2c,(byte)0x2c,
        (byte)0x5e,(byte)0x50,(byte)0xc7,(byte)0x11,(byte)0x74,(byte)0x04,(byte)0x20,(byte)0xff,
        (byte)0xf6,(byte)0xf7,(byte)0x17,(byte)0x31,(byte)0xcc,(byte)0x98,(byte)0x91,(byte)0x0e,
        (byte)0xf6,(byte)0x53,(byte)0x61,(byte)0x61,(byte)0x1f,(byte)0x83,(byte)0x9a,(byte)0xda,
        (byte)0x2f,(byte)0x9a,(byte)0xd9,(byte)0xb7,(byte)0x6f,(byte)0x9f,(byte)0x12,(byte)0xc3,
        (byte)0xab,(byte)0x57,(byte)0x62,(byte)0xc0,(byte)0x70,(byte)0x75,(byte)0x00,(byte)0xdb,
        (byte)0x09,(byte)0x01,(byte)0x0a,(byte)0x40,(byte)0xfc,(byte)0x80,(byte)0xe1,(byte)0xff,
        (byte)0x7f,(byte)0x45,(byte)0x9a,(byte)0xd9,(byte)0x4b,(byte)0x0c,(byte)0x38,(byte)0x71,
        (byte)0x42,(byte)0x82,(byte)0xe1,(byte)0xf5,(byte)0x6b,(byte)0x31,(byte)0x60,(byte)0x98,
        (byte)0xa8,(byte)0x31,(byte)0x94,(byte)0x94,(byte)0x74,(byte)0x43,(byte)0x45,(byte)0x15,
        (byte)0x18,(byte)0x06,(byte)0x83,(byte)0xdb,(byte)0x86,(byte)0x08,(byte)0x00,(byte)0x08,
        (byte)0x20,(byte)0xca,(byte)0x32,(byte)0x22,(byte)0x28,(byte)0x02,(byte)0xb6,(byte)0x6e,
        (byte)0xf5,(byte)0x61,(byte)0x68,(byte)0x69,(byte)0xa9,(byte)0x86,(byte)0x8a,(byte)0x28,
        (byte)0x40,(byte)0xe9,(byte)0x07,(byte)0x44,(byte)0xe8,(byte)0xc6,(byte)0x54,(byte)0xdb,
        (byte)0xd3,(byte)0x53,(byte)0xca,(byte)0x60,(byte)0x6d,(byte)0x7d,(byte)0x64,(byte)0xd0,
        (byte)0x67,(byte)0x4c,(byte)0x50,(byte)0x26,(byte)0x54,(byte)0x57,(byte)0xbf,(byte)0xc9,
        (byte)0x80,(byte)0xf0,(byte)0x03,(byte)0x08,(byte)0x3c,(byte)0x60,(byte)0xb8,(byte)0x79,
        (byte)0x53,(byte)0x9d,(byte)0xaa,(byte)0x99,(byte)0x91,(byte)0x91,(byte)0xf1,(byte)0x3e,
        (byte)0x9a,(byte)0x88,(byte)0x02,(byte)0x16,(byte)0x55,(byte)0x03,(byte)0x93,(byte)0xd8,
        (byte)0x07,(byte)0xb3,(byte)0xdb,(byte)0x86,(byte)0x20,(byte)0x00,(byte)0x08,(byte)0x20,
        (byte)0x06,(byte)0x50,(byte)0x46,(byte)0x24,(byte)0x19,(byte)0xef,(byte)0xdd,(byte)0xab,
        (byte)0xf4,(byte)0xdf,(byte)0xd7,(byte)0x77,(byte)0x32,(byte)0x30,(byte)0x0b,(byte)0xdf,
        (byte)0x07,(byte)0xe2,(byte)0xff,(byte)0x50,(byte)0x7c,(byte)0x1f,(byte)0x8c,(byte)0x67,
        (byte)0xcd,(byte)0x72,(byte)0xfb,(byte)0x7f,(byte)0xfc,(byte)0xb8,(byte)0x04,(byte)0x5e,
        (byte)0xfd,(byte)0x20,(byte)0x79,(byte)0x90,(byte)0x3a,(byte)0x98,(byte)0x1e,(byte)0x84,
        (byte)0x19,(byte)0x10,(byte)0x73,(byte)0x40,(byte)0x66,(byte)0x93,(byte)0xe3,(byte)0x2e,
        (byte)0x7a,(byte)0xe1,(byte)0x8c,(byte)0x8c,(byte)0x0a,(byte)0x34,(byte)0x37,(byte)0x43,
        (byte)0x30,(byte)0x48,(byte)0x9c,(byte)0x9a,(byte)0xf6,(byte)0x20,(byte)0xc2,(byte)0x07,
        (byte)0x5b,(byte)0x38,(byte)0x21,(byte)0xc2,(byte)0x6b,(byte)0x20,(byte)0xc2,(byte)0x60,
        (byte)0x30,(byte)0xbb,(byte)0x6d,(byte)0x08,(byte)0x62,(byte)0x80,(byte)0x00,(byte)0x22,
        (byte)0x5d,(byte)0x13,(byte)0x24,(byte)0x11,(byte)0xde,(byte)0xff,(byte)0x8f,(byte)0x9e,
        (byte)0x01,(byte)0x97,(byte)0x2f,(byte)0xb7,(byte)0x20,(byte)0xcb,(byte)0x11,(byte)0x9b,
        (byte)0x36,(byte)0xe9,(byte)0x61,(byte)0x44,(byte)0x66,(byte)0x4f,(byte)0x4f,(byte)0xc8,
        (byte)0x40,(byte)0x07,(byte)0x0c,(byte)0x5e,(byte)0x3c,(byte)0x10,(byte)0x09,(byte)0x0f,
        (byte)0x54,(byte)0xf8,(byte)0x61,(byte)0xb7,(byte)0x97,(byte)0x76,(byte)0x76,(byte)0x12,
        (byte)0x8b,(byte)0x41,(byte)0x71,(byte)0x3f,(byte)0x58,(byte)0xdd,(byte)0x36,(byte)0x44,
        (byte)0x30,(byte)0x40,(byte)0x00,(byte)0x11,(byte)0xaf,(byte)0x18,(byte)0x54,(byte)0x8b,
        (byte)0x61,(byte)0x96,(byte)0x7e,(byte)0xd4,(byte)0xab,(byte)0xbd,(byte)0x20,(byte)0x35,
        (byte)0x2c,(byte)0xc4,(byte)0x5c,(byte)0x50,(byte)0xa2,(byte)0x1b,(byte)0x04,(byte)0x81,
        (byte)0x83,(byte)0x13,(byte)0xd3,(byte)0xab,(byte)0x46,(byte)0x44,(byte)0xc7,(byte)0x83,
        (byte)0x39,(byte)0xb1,(byte)0x0f,(byte)0x66,(byte)0xb7,(byte)0x0d,(byte)0x01,(byte)0x0c,
        (byte)0x10,(byte)0x40,(byte)0xc4,(byte)0x29,(byte)0x44,(byte)0xd4,(byte)0x5a,(byte)0xff,
        (byte)0x69,(byte)0x92,(byte)0x09,(byte)0x41,(byte)0xf8,(byte)0xe6,(byte)0x4d,(byte)0x36,
        (byte)0x78,(byte)0x46,(byte)0xbf,(byte)0x70,(byte)0x41,(byte)0x60,(byte)0xa0,(byte)0x03,
        (byte)0x86,(byte)0x48,(byte)0xb7,(byte)0xa2,(byte)0x86,(byte)0x07,(byte)0x48,(byte)0x9c,
        (byte)0x96,(byte)0xf6,(byte)0x0e,(byte)0xe6,(byte)0xc4,(byte)0x3e,(byte)0x98,(byte)0xdd,
        (byte)0x36,(byte)0x04,(byte)0x30,(byte)0x40,(byte)0x00,(byte)0x11,(byte)0x56,(byte)0x04,
        (byte)0xca,(byte)0x14,(byte)0xd8,(byte)0x02,(byte)0x99,(byte)0x16,(byte)0xfd,(byte)0x38,
        (byte)0x58,(byte)0x46,(byte)0x1c,(byte)0x04,(byte)0x01,(byte)0x43,(byte)0x10,(byte)0x83,
        (byte)0x32,(byte)0x1d,(byte)0xac,(byte)0x99,(byte)0x0e,(byte)0xa2,(byte)0x69,(byte)0x9d,
        (byte)0x09,(byte)0x11,(byte)0xe1,(byte)0xf3,(byte)0x7f,(byte)0x50,(byte)0x26,(byte)0xf6,
        (byte)0xc1,(byte)0xec,(byte)0xb6,(byte)0x21,(byte)0x80,(byte)0x01,(byte)0x02,(byte)0x88,
        (byte)0xf0,(byte)0xa8,(byte)0x29,(byte)0x64,(byte)0x74,(byte)0x4c,(byte)0x01,(byte)0x4d,
        (byte)0xf4,(byte)0x01,(byte)0xc3,(byte)0xf1,(byte)0xe3,(byte)0x96,(byte)0x54,(byte)0x1f,
        (byte)0xdd,(byte)0x84,(byte)0x8d,(byte)0xc4,(byte)0x8d,(byte)0x8e,(byte)0xb4,(byte)0x61,
        (byte)0x07,(byte)0xb8,(byte)0xe2,(byte)0x62,(byte)0x30,(byte)0x84,(byte)0xd7,(byte)0x60,
        (byte)0x76,(byte)0xdb,(byte)0x10,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x84,(byte)0x7f,
        (byte)0x42,(byte)0xbf,(byte)0xb7,(byte)0x37,(byte)0x04,(byte)0xab,(byte)0x38,(byte)0x68,
        (byte)0x9a,(byte)0x61,(byte)0xb0,(byte)0x4f,(byte)0x31,(byte)0x8c,(byte)0x82,(byte)0x51,
        (byte)0x30,(byte)0x84,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0xb1,(byte)0xe0,(byte)0x94,
        (byte)0x01,(byte)0xcd,(byte)0x95,(byte)0x41,(byte)0x26,(byte)0x67,(byte)0x15,(byte)0xd0,
        (byte)0x64,(byte)0x1e,(byte)0x30,(byte)0xb8,(byte)0xb8,(byte)0xec,(byte)0xa1,(byte)0x89,
        (byte)0x6b,(byte)0x46,(byte)0x4b,(byte)0xcf,(byte)0x51,(byte)0x30,(byte)0x42,(byte)0x01,
        (byte)0x40,(byte)0x00,(byte)0xe1,(byte)0xae,(byte)0x11,(byte)0x37,(byte)0x6f,(byte)0xf6,
        (byte)0x63,(byte)0xc0,(byte)0x36,(byte)0x49,(byte)0xeb,(byte)0xeb,(byte)0xbb,(byte)0x85,
        (byte)0x41,(byte)0x5f,(byte)0xff,(byte)0x03,(byte)0xed,(byte)0x9c,(byte)0x34,(byte)0x0a,
        (byte)0x46,(byte)0xc1,(byte)0xc8,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x84,(byte)0x3b,
        (byte)0x23,(byte)0x22,(byte)0x96,(byte)0x2a,(byte)0xa1,(byte)0x82,(byte)0xa8,(byte)0xa8,
        (byte)0xa5,(byte)0xb4,(byte)0x72,(byte)0x0c,(byte)0x45,(byte)0x00,(byte)0xb4,(byte)0xf6,
        (byte)0x92,(byte)0x9c,(byte)0xf5,(byte)0x8d,(byte)0xb0,(byte)0x75,(byte)0x91,(byte)0x7e,
        (byte)0x7e,(byte)0x93,(byte)0xc9,(byte)0xb2,(byte)0x97,(byte)0x9a,(byte)0xeb,(byte)0x2a,
        (byte)0x61,(byte)0xe6,(byte)0x80,(byte)0xfa,(byte)0x5b,(byte)0x30,(byte)0x0c,(byte)0x5a,
        (byte)0x4b,(byte)0x0a,(byte)0x5a,(byte)0x53,(byte)0x0a,(byte)0x5a,(byte)0xc5,(byte)0x44,
        (byte)0x6d,(byte)0x00,(byte)0xb2,(byte)0x0f,(byte)0x64,(byte)0x3e,(byte)0xb2,(byte)0x7d,
        (byte)0xa0,(byte)0xee,(byte)0x08,(byte)0x2d,(byte)0xec,(byte)0x82,(byte)0x01,(byte)0x50,
        (byte)0x3c,(byte)0x81,(byte)0xec,(byte)0x40,(byte)0xf7,(byte)0xe3,(byte)0xe6,(byte)0xcd,
        (byte)0x7a,(byte)0x54,(byte)0x31,(byte)0x9f,(byte)0xd6,(byte)0x7e,(byte)0xa2,(byte)0x91,
        (byte)0xf9,(byte)0x00,(byte)0x01,(byte)0x84,(byte)0x7d,(byte)0x14,(byte)0x07,(byte)0xd7,
        (byte)0x48,(byte)0xe9,(byte)0x60,(byte)0x9a,(byte)0x5a,(byte)0x00,(byte)0xcd,(byte)0x35,
        (byte)0x82,(byte)0x26,(byte)0x92,(byte)0x6b,(byte)0x6a,(byte)0x52,(byte)0xd0,(byte)0xe6,
        (byte)0x37,(byte)0xf1,(byte)0x8f,(byte)0xd4,(byte)0x81,(byte)0xf4,(byte)0x81,(byte)0xa6,
        (byte)0x63,(byte)0x40,(byte)0x8b,(byte)0x06,(byte)0x48,(byte)0xd1,(byte)0x07,(byte)0xc3,
        (byte)0xa0,(byte)0xf9,(byte)0x54,(byte)0x4a,(byte)0xf4,(byte)0x63,(byte)0xc3,(byte)0x88,
        (byte)0x09,(byte)0xf1,(byte)0xc3,(byte)0x48,(byte)0x66,(byte)0xde,(byte)0xc7,(byte)0x11,
        (byte)0x07,(byte)0x98,(byte)0x71,(byte)0x42,(byte)0xaa,(byte)0x7d,(byte)0x08,(byte)0xb7,
        (byte)0xa3,(byte)0xdb,(byte)0x87,(byte)0x30,(byte)0x13,(byte)0x34,(byte)0x2a,(byte)0x4e,
        (byte)0xca,(byte)0x7c,(byte)0x2e,(byte)0x31,(byte)0xa3,(byte)0xa6,(byte)0x08,(byte)0x7b,
        (byte)0xb1,(byte)0xfb,(byte)0x03,(byte)0x84,(byte)0xc9,(byte)0x9d,(byte)0x43,(byte)0xa6,
        (byte)0x85,(byte)0x9f,(byte)0xe8,(byte)0x68,(byte)0x3e,(byte)0x40,(byte)0x00,(byte)0xe1,
        (byte)0x4b,(byte)0x18,(byte)0xd4,(byte)0x89,(byte)0x74,(byte)0x6a,(byte)0x62,(byte)0x72,
        (byte)0x97,(byte)0x55,(byte)0x51,(byte)0xba,(byte)0x1c,(byte)0x8b,(byte)0x56,(byte)0xcb,
        (byte)0xb9,(byte)0x40,(byte)0x53,(byte)0x1e,(byte)0x90,(byte)0x85,(byte)0x0c,(byte)0x87,
        (byte)0xc1,(byte)0x11,(byte)0x8d,(byte)0x3e,(byte)0x05,(byte)0x02,(byte)0xca,(byte)0xf4,
        (byte)0x98,(byte)0x2b,(byte)0x99,(byte)0xc8,(byte)0xb7,(byte)0x17,(byte)0xb1,(byte)0x3a,
        (byte)0xe7,(byte)0x3e,(byte)0x46,(byte)0xa2,(byte)0x47,(byte)0xb8,(byte)0xe5,(byte)0x3e,
        (byte)0x8a,(byte)0xd9,(byte)0xc4,(byte)0xae,(byte)0x98,(byte)0x22,(byte)0x94,(byte)0x11,
        (byte)0x11,(byte)0x05,(byte)0x26,(byte)0x2e,(byte)0x7f,(byte)0x90,(byte)0x6e,(byte)0x27,
        (byte)0xad,(byte)0xfd,(byte)0x44,(byte)0x0f,(byte)0xf3,(byte)0xa1,(byte)0x18,(byte)0x20,
        (byte)0x80,(byte)0xb0,(byte)0x4b,(byte)0x40,(byte)0x72,(byte)0x3f,(byte)0x75,(byte)0x12,
        (byte)0x1b,(byte)0x35,(byte)0xf1,(byte)0x70,(byte)0xca,(byte)0x88,(byte)0x88,(byte)0x5a,
        (byte)0x10,(byte)0xb2,(byte)0x3e,(byte)0x17,(byte)0x9f,(byte)0x5a,(byte)0xc4,(byte)0xba,
        (byte)0x5c,(byte)0xf2,(byte)0xed,(byte)0x45,(byte)0xcd,(byte)0x08,(byte)0xf7,(byte)0x71,
        (byte)0xae,(byte)0x07,(byte)0xc6,(byte)0xb6,(byte)0x50,(byte)0x81,(byte)0x98,(byte)0x52,
        (byte)0x1e,(byte)0x5f,(byte)0x46,(byte)0x84,(byte)0x15,(byte)0x36,(byte)0xc4,(byte)0xd7,
        (byte)0xf6,(byte)0xb8,(byte)0xdd,(byte)0x47,(byte)0x4f,(byte)0x3f,(byte)0xd1,(byte)0xda,
        (byte)0x7c,(byte)0x24,(byte)0x0c,(byte)0x10,(byte)0x40,(byte)0xd8,(byte)0x25,(byte)0x90,
        (byte)0x97,(byte)0x9b,(byte)0x0d,(byte)0xa6,(byte)0x8c,(byte)0x88,(byte)0x8c,(byte)0x11,
        (byte)0x4b,(byte)0xee,(byte)0x48,(byte)0x73,(byte)0x23,(byte)0xa5,(byte)0x6b,(byte)0x36,
        (byte)0xa9,(byte)0xb1,(byte)0xe6,(byte)0x13,(byte)0x39,(byte)0x82,(byte)0x89,(byte)0x5d,
        (byte)0x18,(byte)0x81,(byte)0xbb,(byte)0x59,(byte)0x47,(byte)0xd8,(byte)0x5e,(byte)0xf4,
        (byte)0xda,(byte)0x08,(byte)0xdf,(byte)0x52,(byte)0x3c,(byte)0x48,(byte)0xa6,(byte)0x47,
        (byte)0xb5,(byte)0x83,(byte)0x18,(byte)0x37,(byte)0xe2,(byte)0x72,(byte)0x9b,(byte)0xaf,
        (byte)0x6f,(byte)0x28,(byte)0x18,(byte)0xa3,(byte)0x27,(byte)0x4c,(byte)0x50,(byte)0xfc,
        (byte)0x61,(byte)0xd6,(byte)0x26,(byte)0xc4,(byte)0xdb,(byte)0x49,(byte)0x6b,(byte)0x3f,
        (byte)0xd1,(byte)0x23,(byte)0xcc,(byte)0x90,(byte)0x30,(byte)0x40,(byte)0x00,(byte)0x91,
        (byte)0x12,(byte)0xa8,(byte)0xa4,(byte)0x25,(byte)0x36,(byte)0x7a,(byte)0x60,(byte)0x72,
        (byte)0x13,(byte)0x26,(byte)0xa5,(byte)0x19,(byte)0x89,(byte)0x12,(byte)0xfd,(byte)0xe8,
        (byte)0x89,(byte)0x8f,(byte)0x94,(byte)0x66,(byte)0x0c,(byte)0x39,(byte)0xf6,(byte)0x62,
        (byte)0x5b,(byte)0x90,(byte)0x8d,(byte)0xcf,(byte)0x4e,(byte)0xec,(byte)0xe3,(byte)0x03,
        (byte)0x84,(byte)0xc7,(byte)0x06,(byte)0x70,(byte)0x67,(byte)0x44,(byte)0xfc,(byte)0x09,
        (byte)0x12,(byte)0x5f,(byte)0xa1,(byte)0x8f,(byte)0xcb,(byte)0x4e,(byte)0x5a,(byte)0xfb,
        (byte)0x89,(byte)0x5e,(byte)0x61,(byte)0x86,(byte)0x84,(byte)0x01,(byte)0x02,(byte)0x88,
        (byte)0x94,(byte)0x40,(byte)0x25,(byte)0x3e,(byte)0xb1,(byte)0xd1,(byte)0x0b,(byte)0x0f,
        (byte)0xb5,(byte)0x8c,(byte)0x88,(byte)0x19,(byte)0xc1,(byte)0xa4,(byte)0x0d,(byte)0x7e,
        (byte)0x91,(byte)0x63,(byte)0x2f,(byte)0xb6,(byte)0x04,(byte)0x42,(byte)0xa8,(byte)0xd9,
        (byte)0x87,(byte)0xcd,(byte)0x1e,(byte)0x42,(byte)0xcd,(byte)0x67,(byte)0x72,(byte)0xc3,
        (byte)0x04,(byte)0xf7,(byte)0xc0,(byte)0x20,(byte)0x6e,(byte)0x3b,(byte)0x69,(byte)0xed,
        (byte)0x27,(byte)0x7a,(byte)0x85,(byte)0x19,(byte)0x12,(byte)0x06,(byte)0x08,(byte)0xa0,
        (byte)0xd1,(byte)0xa3,(byte)0x32,(byte)0xe8,(byte)0x09,(byte)0x22,(byte)0x23,(byte)0x97,
        (byte)0x33,(byte)0xa0,(byte)0xcf,(byte)0xcd,(byte)0xd2,(byte)0x72,(byte)0x4e,(byte)0x16,
        (byte)0xd7,(byte)0x90,(byte)0x3a,(byte)0x39,(byte)0xab,(byte)0xa2,(byte)0x1e,(byte)0x3d,
        (byte)0x92,(byte)0xa3,(byte)0xd4,(byte)0x39,(byte)0x58,(byte)0x01,(byte)0xc8,(byte)0xff,
        (byte)0xa0,(byte)0x13,(byte)0x0e,(byte)0xb0,(byte)0x81,(byte)0x73,(byte)0xe7,(byte)0x8c,
        (byte)0x30,(byte)0xc4,(byte)0x68,(byte)0xed,(byte)0xa7,(byte)0x01,(byte)0x0a,(byte)0x33,
        (byte)0x80,(byte)0x00,(byte)0x1a,(byte)0xcd,(byte)0x88,(byte)0xf4,(byte)0x02,(byte)0xa0,
        (byte)0xf9,(byte)0x33,(byte)0x7a,(byte)0x83,(byte)0xcb,(byte)0x97,(byte)0x41,(byte)0x73,
        (byte)0x73,(byte)0x0a,(byte)0x54,(byte)0x31,(byte)0x0b,(byte)0x71,(byte)0x0a,(byte)0x03,
        (byte)0xf5,(byte)0x41,(byte)0x68,(byte)0xe8,(byte)0x2a,(byte)0x06,(byte)0x6c,(byte)0xa7,
        (byte)0x3a,(byte)0x20,(byte)0x8e,(byte)0x04,(byte)0x41,(byte)0x00,(byte)0x5a,(byte)0xfb,
        (byte)0x69,(byte)0x80,(byte)0xc2,(byte)0x0c,(byte)0x20,(byte)0x80,(byte)0xb0,(byte)0x67,
        (byte)0x44,(byte)0xd0,(byte)0x5a,(byte)0xd2,(byte)0x51,(byte)0x40,(byte)0x5d,(byte)0x70,
        (byte)0xea,(byte)0x94,(byte)0x19,(byte)0x03,(byte)0xb5,(byte)0x22,(byte)0x98,(byte)0x58,
        (byte)0x40,(byte)0xab,(byte)0x5a,(byte)0x8c,(byte)0xda,(byte)0xc0,(byte)0xc9,(byte)0xe9,
        (byte)0x1e,(byte)0xd1,(byte)0x6a,(byte)0x69,(byte)0xed,(byte)0xa7,(byte)0x01,(byte)0x0a,
        (byte)0x33,(byte)0x80,(byte)0x00,(byte)0xc2,(byte)0x9e,(byte)0x11,(byte)0xa5,(byte)0xa5,
        (byte)0x9f,(byte)0xe0,(byte)0xd4,(byte)0x01,(byte)0x3a,(byte)0xc4,(byte)0x68,(byte)0x14,
        (byte)0x90,(byte)0x0e,(byte)0x06,(byte)0x22,(byte)0x82,(byte)0x69,(byte)0x59,(byte)0x8b,
        (byte)0x0d,(byte)0x14,(byte)0xa0,(byte)0xb5,(byte)0x9f,(byte)0x06,(byte)0x28,(byte)0xcc,
        (byte)0x00,(byte)0x02,(byte)0x08,(byte)0xfb,(byte)0xa2,(byte)0x6f,(byte)0x05,(byte)0x85,
        (byte)0x07,(byte)0x0c,(byte)0x90,(byte)0xa6,(byte)0x82,(byte)0x02,(byte)0xba,(byte)0x0c,
        (byte)0xc3,(byte)0xdd,(byte)0xbb,(byte)0x2a,(byte)0x24,(byte)0x95,(byte)0x60,(byte)0x30,
        (byte)0x80,(byte)0x79,(byte)0xd8,(byte)0x10,(byte)0x71,(byte)0x60,(byte)0xb8,(byte)0x2c,
        (byte)0x04,(byte)0x1f,(byte)0x4c,(byte)0x99,(byte)0x82,(byte)0xf4,(byte)0xb8,(byte)0x78,
        (byte)0x40,(byte)0x0b,(byte)0x67,(byte)0x10,(byte)0x04,(byte)0x35,(byte)0x35,(byte)0xad,
        (byte)0x44,(byte)0xab,(byte)0xa5,(byte)0xb5,(byte)0x9f,(byte)0x68,(byte)0x6c,(byte)0x3e,
        (byte)0x40,(byte)0x00,(byte)0x61,(byte)0xaf,(byte)0x11,(byte)0xf1,(byte)0x75,(byte)0x4c,
        (byte)0x37,(byte)0x6f,(byte)0xf6,(byte)0x25,(byte)0xc9,(byte)0x39,(byte)0xf8,(byte)0x81,
        (byte)0x02,(byte)0x0e,(byte)0x0c,(byte)0x01,(byte)0xb3,(byte)0x66,(byte)0x61,(byte)0xf6,
        (byte)0x11,(byte)0x46,(byte)0x01,(byte)0xbd,(byte)0xc0,(byte)0x03,(byte)0x60,(byte)0x17,
        (byte)0x25,(byte)0x94,(byte)0x61,(byte)0xd3,(byte)0x26,(byte)0x7d,(byte)0xf0,(byte)0xe9,
        (byte)0x74,(byte)0x03,(byte)0x51,(byte)0x20,(byte)0x6a,(byte)0x6b,(byte)0x5f,(byte)0xa1,
        (byte)0xb2,(byte)0x89,(byte)0xb4,(byte)0xf6,(byte)0x13,(byte)0xd9,(byte)0xe6,(byte)0x03,
        (byte)0x04,(byte)0x10,(byte)0xee,(byte)0x6d,(byte)0x50,(byte)0xa0,(byte)0x7e,(byte)0x62,
        (byte)0x49,(byte)0xc9,(byte)0x6a,(byte)0x0c,(byte)0x71,(byte)0xd0,(byte)0x59,(byte)0xa5,
        (byte)0x17,(byte)0x2f,(byte)0xd6,(byte)0x92,(byte)0x3c,(byte)0xda,(byte)0x87,(byte)0xee,
        (byte)0x28,(byte)0xd0,(byte)0xe2,(byte)0x59,(byte)0x6c,(byte)0xa3,(byte)0x88,(byte)0xb8,
        (byte)0xd4,(byte)0x8f,(byte)0x02,(byte)0xea,(byte)0x81,(byte)0xa1,(byte)0x11,(byte)0xb6,
        (byte)0x0f,(byte)0x18,(byte)0x34,(byte)0x35,(byte)0x6f,(byte)0x10,(byte)0xad,(byte)0x9a,
        (byte)0xd6,(byte)0x7e,(byte)0xa2,(byte)0xb1,(byte)0xf9,(byte)0x00,(byte)0x01,(byte)0x84,
        (byte)0x7b,(byte)0xd4,(byte)0x14,(byte)0xb2,(byte)0xe7,(byte)0xf0,(byte)0x01,(byte)0x16,
        (byte)0x19,(byte)0x05,(byte)0x86,(byte)0x3d,(byte)0x7b,(byte)0x5c,(byte)0x28,(byte)0xb6,
        (byte)0x39,(byte)0x22,(byte)0xe2,(byte)0x04,(byte)0xc5,(byte)0x66,(byte)0x0c,(byte)0x07,
        (byte)0x40,(byte)0xcb,(byte)0xd1,(byte)0x54,(byte)0x5c,(byte)0xd3,(byte)0x02,(byte)0x43,
        (byte)0x05,(byte)0x60,(byte)0x2b,(byte)0xec,(byte)0x69,(byte)0xed,(byte)0xa7,(byte)0x01,
        (byte)0x0a,(byte)0x33,(byte)0x80,(byte)0x00,(byte)0xc2,(byte)0x9d,(byte)0x11,(byte)0x41,
        (byte)0x81,(byte)0x00,(byte)0x19,(byte)0x3d,(byte)0x7d,(byte)0x80,(byte)0x21,(byte)0x07,
        (byte)0xda,(byte)0x22,(byte)0x05,(byte)0xda,(byte)0xfe,(byte)0x33,(byte)0x0a,(byte)0x88,
        (byte)0x07,(byte)0xd8,(byte)0xfb,(byte)0x3b,(byte)0x0a,(byte)0x0c,(byte)0xd7,(byte)0xaf,
        (byte)0x6b,(byte)0xd0,(byte)0xcc,(byte)0x4e,(byte)0xd8,(byte)0x89,(byte)0xea,(byte)0xe8,
        (byte)0x60,(byte)0x20,(byte)0xa6,(byte)0x52,(byte)0x48,(byte)0x03,(byte)0x0f,(byte)0x18,
        (byte)0x96,(byte)0x2f,(byte)0x8f,(byte)0xc4,(byte)0x2a,(byte)0x43,(byte)0x6b,(byte)0x3f,
        (byte)0x0d,(byte)0x50,(byte)0x98,(byte)0x01,(byte)0x04,(byte)0x10,(byte)0xfe,(byte)0x79,
        (byte)0xc4,(byte)0xe2,(byte)0xe2,(byte)0x35,(byte)0x38,(byte)0x64,(byte)0x14,(byte)0xc0,
        (byte)0xc7,(byte)0xcd,(byte)0x8f,(byte)0x02,(byte)0xe2,(byte)0x01,(byte)0xae,(byte)0xfe,
        (byte)0xce,(byte)0xb2,(byte)0x65,(byte)0xd1,(byte)0x34,(byte)0xb3,(byte)0x13,(byte)0x74,
        (byte)0xa5,(byte)0x01,(byte)0x66,(byte)0x41,(byte)0x4a,(byte)0xdb,(byte)0xcc,(byte)0x4f,
        (byte)0x2d,(byte)0x80,(byte)0xab,(byte)0xc5,(byte)0x44,(byte)0x6b,(byte)0x3f,(byte)0x0d,
        (byte)0x50,(byte)0x98,(byte)0x01,(byte)0x04,(byte)0x10,(byte)0xe1,(byte)0x09,(byte)0x7d,
        (byte)0xd0,(byte)0x21,(byte)0x51,(byte)0xb8,(byte)0x26,(byte)0x5b,(byte)0x41,(byte)0x1b,
        (byte)0x56,(byte)0x07,(byte)0x23,(byte)0xc0,(byte)0x57,(byte)0x5b,(byte)0x0f,(byte)0xd4,
        (byte)0xf4,(byte)0x0b,(byte)0xa4,(byte)0xbf,(byte)0xf3,(byte)0x00,(byte)0x43,(byte)0x1c,
        (byte)0xd4,(byte)0xe7,(byte)0x26,(byte)0xc6,(byte)0x4d,(byte)0xe4,(byte)0x6c,(byte)0x9c,
        (byte)0xc5,(byte)0x35,(byte)0xba,(byte)0x4d,(byte)0xcb,(byte)0xcc,(byte)0x4f,(byte)0x0e,
        (byte)0x40,(byte)0xf5,(byte)0xdb,(byte)0x03,(byte)0x86,(byte)0xbd,(byte)0x7b,(byte)0x9d,
        (byte)0x71,(byte)0xaa,(byte)0xa5,(byte)0xb5,(byte)0x9f,(byte)0x06,(byte)0x28,(byte)0xcc,
        (byte)0x00,(byte)0x02,(byte)0x88,(byte)0xb8,(byte)0x35,(byte)0x8e,(byte)0xf8,(byte)0x4e,
        (byte)0x72,(byte)0x26,(byte)0xf7,(byte)0x84,(byte)0x6f,(byte)0xdc,(byte)0xeb,(byte)0x13,
        (byte)0x89,(byte)0x5b,(byte)0xa3,(byte)0x08,(byte)0xc2,(byte)0xb8,(byte)0x16,(byte)0x0c,
        (byte)0xe3,(byte)0x5b,(byte)0xe3,(byte)0x47,(byte)0x8d,(byte)0x9d,(byte)0x25,(byte)0xe4,
        (byte)0xae,(byte)0xab,(byte)0x24,(byte)0x67,(byte)0x81,(byte)0x33,(byte)0x08,(byte)0xe3,
        (byte)0xde,(byte)0x69,(byte)0x42,(byte)0xd8,(byte)0x5e,(byte)0xec,(byte)0xbb,(byte)0x36,
        (byte)0xc8,(byte)0xdf,(byte)0x80,(byte)0x4b,(byte)0xed,(byte)0x30,(byte)0x01,(byte)0x61,
        (byte)0xc4,(byte)0x81,(byte)0xcd,(byte)0xc4,(byte)0xa5,(byte)0x27,(byte)0x5a,(byte)0xfb,
        (byte)0x89,(byte)0x5e,(byte)0x61,(byte)0x86,(byte)0x84,(byte)0x01,(byte)0x02,(byte)0x88,
        (byte)0x78,(byte)0xc5,(byte)0xb4,(byte)0xc8,(byte)0x8c,(byte)0x94,(byte)0x66,(byte)0x44,
        (byte)0x6c,(byte)0xdb,(byte)0x4f,(byte)0x70,(byte)0xb9,(byte)0x09,(byte)0xfb,(byte)0x7d,
        (byte)0x1d,(byte)0xa4,(byte)0xdb,(byte)0x89,(byte)0xdb,(byte)0xdd,(byte)0x84,(byte)0xf5,
        (byte)0xe3,(byte)0xde,(byte)0x42,(byte)0x85,(byte)0xdd,(byte)0xcd,(byte)0xa0,(byte)0x8d,
        (byte)0xa7,(byte)0xf8,(byte)0x77,(byte)0xb5,(byte)0x13,(byte)0x67,(byte)0x2f,(byte)0x36,
        (byte)0xfd,(byte)0xd4,(byte)0x3e,(byte)0x97,(byte)0x96,(byte)0xb2,(byte)0x45,(byte)0xdf,
        (byte)0x87,(byte)0x49,(byte)0x4e,(byte)0xe8,(byte)0xb4,(byte)0xf6,(byte)0x13,(byte)0x3d,
        (byte)0xc2,(byte)0x0c,(byte)0x09,(byte)0x03,(byte)0x04,(byte)0x10,(byte)0x69,(byte)0x1a,
        (byte)0x40,(byte)0x81,(byte)0x86,(byte)0x3d,(byte)0x31,(byte)0xdf,(byte)0x27,(byte)0xf9,
        (byte)0xb8,(byte)0x79,(byte)0x42,(byte)0xc7,(byte)0x26,(byte)0x10,(byte)0xeb,(byte)0x1e,
        (byte)0x7c,(byte)0x66,(byte)0xa0,(byte)0x63,(byte)0x90,(byte)0xdb,(byte)0xf1,(byte)0x1d,
        (byte)0x03,(byte)0x42,(byte)0x4c,(byte)0x81,(byte)0x42,(byte)0x69,(byte)0x81,(byte)0x84,
        (byte)0x7f,(byte)0xa7,(byte)0x3a,(byte)0xa6,(byte)0x9b,(byte)0x61,(byte)0xee,(byte)0xc6,
        (byte)0x7e,(byte)0xba,(byte)0x38,(byte)0x44,(byte)0x0f,(byte)0xa1,(byte)0x55,(byte)0xfe,
        (byte)0x94,(byte)0x9e,(byte)0xd4,(byte)0x4e,(byte)0xcc,(byte)0x26,(byte)0x5d,(byte)0xf2,
        (byte)0xb7,(byte)0x41,(byte)0x85,(byte)0x82,(byte)0xc3,(byte)0x84,(byte)0x94,(byte)0xb4,
        (byte)0x43,(byte)0x0f,(byte)0x3f,(byte)0xd1,(byte)0x23,(byte)0xcc,(byte)0x90,(byte)0x30,
        (byte)0x40,(byte)0x00,(byte)0x91,(byte)0xe6,(byte)0x79,(byte)0x18,(byte)0x46,(byte)0xde,
        (byte)0x5d,(byte)0x8e,(byte)0x9e,(byte)0x88,(byte)0x40,(byte)0x19,(byte)0x12,(byte)0x97,
        (byte)0x23,(byte)0x40,(byte)0x99,(byte)0x00,(byte)0xf3,(byte)0xac,(byte)0x97,(byte)0xff,
        (byte)0x58,(byte)0xcd,(byte)0x20,(byte)0xd6,(byte)0x2d,(byte)0xc4,(byte)0x1d,(byte)0xc1,
        (byte)0x70,(byte)0x1f,(byte)0x25,(byte)0xb2,(byte)0x89,(byte)0xcd,(byte)0x08,(byte)0xa8,
        (byte)0xea,(byte)0xef,(byte)0xe3,(byte)0x71,(byte)0x37,(byte)0x7e,(byte)0xfd,(byte)0xe4,
        (byte)0xb9,(byte)0xf9,(byte)0x3f,(byte)0x46,(byte)0x26,(byte)0x23,(byte)0xd5,(byte)0xdd,
        (byte)0xd8,(byte)0xe3,(byte)0x0c,(byte)0x53,(byte)0x1f,(byte)0xb6,(byte)0x02,(byte)0x04,
        (byte)0x94,(byte)0xf1,(byte)0x61,(byte)0x7a,(byte)0x88,(byte)0xb9,(byte)0x14,(byte)0x08,
        (byte)0x9f,(byte)0xdb,(byte)0xb0,(byte)0x9d,(byte)0xe5,(byte)0x02,(byte)0x29,(byte)0xd4,
        (byte)0x43,(byte)0x81,(byte)0x76,(byte)0xf0,(byte)0x90,(byte)0x95,(byte)0x06,(byte)0xe9,
        (byte)0xe1,(byte)0x27,(byte)0x5a,(byte)0x9b,(byte)0x8f,(byte)0x84,(byte)0x01,(byte)0x02,
        (byte)0x88,(byte)0xbc,(byte)0x00,(byte)0x40,(byte)0x76,(byte)0x28,(byte)0xa2,(byte)0x86,
        (byte)0xbc,(byte)0x8f,(byte)0xe1,(byte)0x58,(byte)0xec,(byte)0x18,(byte)0xd3,(byte)0x53,
        (byte)0x20,(byte)0x33,(byte)0x88,(byte)0xb9,(byte)0xce,(byte)0x0d,(byte)0x17,(byte)0x26,
        (byte)0x54,(byte)0xbb,(byte)0xa2,(byte)0x97,(byte)0xb8,(byte)0xa8,(byte)0x6e,(byte)0xc6,
        (byte)0x8d,(byte)0x51,(byte)0x13,(byte)0x19,(byte)0xe9,(byte)0x98,(byte)0xb8,(byte)0x48,
        (byte)0xc6,(byte)0xe6,(byte)0x6e,(byte)0x44,(byte)0xed,(byte)0x4d,(byte)0x89,(byte)0xbb,
        (byte)0xb1,(byte)0x25,(byte)0x14,(byte)0xec,(byte)0x67,(byte)0xe0,(byte)0xe0,(byte)0x36,
        (byte)0x8b,(byte)0xd8,(byte)0x1a,(byte)0x00,(byte)0x59,(byte)0x3d,(byte)0xbe,(byte)0xb8,
        (byte)0x86,(byte)0xe0,(byte)0xc3,(byte)0xe0,(byte)0x4c,(byte)0x48,(byte)0x8d,(byte)0x83,
        (byte)0xc8,(byte)0x68,(byte)0xe9,(byte)0x27,(byte)0x7a,(byte)0x98,(byte)0x0f,(byte)0xc5,
        (byte)0x00,(byte)0x01,(byte)0xc4,(byte)0x08,(byte)0xce,(byte)0x8d,(byte)0xd4,(byte)0x00,
        (byte)0xa0,(byte)0x91,(byte)0x3f,(byte)0xd0,(byte)0x3a,(byte)0xd4,(byte)0x4f,(byte)0x9f,
        (byte)0xf8,(byte)0x70,(byte)0xde,(byte)0x16,(byte)0x0c,(byte)0x9a,(byte)0x4b,(byte)0x13,
        (byte)0x10,(byte)0xf8,(byte)0xc0,(byte)0xc0,(byte)0xc7,(byte)0xf7,(byte)0x89,(byte)0x41,
        (byte)0x59,(byte)0xf9,(byte)0x0e,(byte)0x83,(byte)0x8c,(byte)0xcc,(byte)0x13,(byte)0xaa,
        (byte)0x5d,(byte)0xec,(byte)0x09,(byte)0x9a,(byte)0xe7,(byte)0x01,(byte)0xed,(byte)0x70,
        (byte)0x00,(byte)0x2d,(byte)0xc1,(byte)0x83,(byte)0xd9,(byte)0x0d,(byte)0x9a,(byte)0x07,
        (byte)0x05,(byte)0x0d,(byte)0x47,(byte)0x93,(byte)0xb3,(byte)0x36,(byte)0x96,(byte)0x5e,
        (byte)0x00,(byte)0xb4,(byte)0xc2,(byte)0xe8,(byte)0xea,(byte)0x55,(byte)0x1d,(byte)0xf8,
        (byte)0x5a,(byte)0x54,(byte)0xd0,(byte)0x84,(byte)0xb2,(byte)0xbd,(byte)0xfd,(byte)0x01,
        (byte)0x9a,(byte)0x2e,(byte)0x78,(byte)0x00,(byte)0x85,(byte)0x15,(byte)0x68,(byte)0x38,
        (byte)0x1e,(byte)0xd9,(byte)0x5e,(byte)0x18,(byte)0x80,(byte)0xc5,(byte)0x11,(byte)0x28,
        (byte)0xdc,(byte)0x48,(byte)0x89,(byte)0x1f,(byte)0xd0,(byte)0x91,(byte)0x82,(byte)0xa0,
        (byte)0x78,(byte)0x4d,(byte)0x4d,(byte)0xdd,(byte)0x05,(byte)0xe6,(byte)0x83,(byte)0xf6,
        (byte)0xf5,(byte)0x81,(byte)0xb6,(byte)0x14,(byte)0x81,(byte)0x16,(byte)0xbb,(byte)0x23,
        (byte)0xdb,(byte)0x01,(byte)0x32,(byte)0xdf,(byte)0xcc,(byte)0xec,(byte)0x14,(byte)0x83,
        (byte)0xaf,(byte)0xef,(byte)0x25,(byte)0xea,(byte)0x79,(byte)0x88,(byte)0x81,(byte)0x36,
        (byte)0x7e,(byte)0xa2,(byte)0xa3,(byte)0xf9,(byte)0x00,(byte)0x01,(byte)0x44,(byte)0xbd,
        (byte)0x8c,(byte)0x38,(byte)0x0a,(byte)0x46,(byte)0xc1,(byte)0x28,(byte)0x20,(byte)0x1b,
        (byte)0x00,(byte)0x04,(byte)0xd0,(byte)0xe8,(byte)0xc6,(byte)0xe0,(byte)0x51,(byte)0x30,
        (byte)0x0a,(byte)0x06,(byte)0x01,(byte)0x00,(byte)0x08,(byte)0xa0,(byte)0xd1,(byte)0x8c,
        (byte)0x38,(byte)0x0a,(byte)0x46,(byte)0xc1,(byte)0x20,(byte)0x00,(byte)0x00,(byte)0x01,
        (byte)0x34,(byte)0x9a,(byte)0x11,(byte)0x47,(byte)0xc1,(byte)0x28,(byte)0x18,(byte)0x04,
        (byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x33,(byte)0xe2,(byte)0x28,(byte)0x18,
        (byte)0x05,(byte)0x83,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xd0,(byte)0x68,(byte)0x46,
        (byte)0x1c,(byte)0x05,(byte)0xa3,(byte)0x60,(byte)0x10,(byte)0x00,(byte)0x80,(byte)0x00,
        (byte)0x1a,(byte)0xcd,(byte)0x88,(byte)0xa3,(byte)0x60,(byte)0x14,(byte)0x0c,(byte)0x02,
        (byte)0x00,(byte)0x10,(byte)0x40,(byte)0xa3,(byte)0x19,(byte)0x71,(byte)0x14,(byte)0x8c,
        (byte)0x82,(byte)0x41,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0x34,(byte)0x23,
        (byte)0x8e,(byte)0x82,(byte)0x51,(byte)0x30,(byte)0x08,(byte)0x00,(byte)0x40,(byte)0x00,
        (byte)0x8d,(byte)0x66,(byte)0xc4,(byte)0x51,(byte)0x30,(byte)0x0a,(byte)0x06,(byte)0x01,
        (byte)0x00,(byte)0x08,(byte)0xa0,(byte)0xd1,(byte)0x8c,(byte)0x38,(byte)0x0a,(byte)0x46,
        (byte)0xc1,(byte)0x20,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x06,(byte)0x00,(byte)0xa6,
        (byte)0xcd,(byte)0x81,(byte)0x26,(byte)0x7d,(byte)0xe2,(byte)0x03,(byte)0x5d,(byte)0x00,
        (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x49,(byte)0x45,(byte)0x4e,(byte)0x44,(byte)0xae,
        (byte)0x42,(byte)0x60,(byte)0x82
    };

    private static IImage IMAGE_LOGO;
    static {
        try {
          IMAGE_LOGO = DigitalMap.getAbstractGraphicsFactory()
                    .createImage(imageGuidebeeLogoArray,0,imageGuidebeeLogoArray.length);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private class SetCenterThread extends Thread {

        private final Object panSyncObject = new Object();

        public SetCenterThread() {
            super("SetCenterThread");
        }

        public void run() {
            Log.p(Thread.currentThread().getName() + " thread started");
            while (!panStopThread) {

                synchronized (panSyncObject) {
                    try {
                        panSyncObject.wait();
                        setCenter(getScreenCenter(), mapZoomLevel, mapType);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
            Log.p(Thread.currentThread().getName() + " thread stopped");
        }

        public void setCenterCommand() {
            synchronized (panSyncObject) {
                panSyncObject.notifyAll();

            }
        }
    }

    private class UpdateMapThread extends Thread {

        private final Object panSyncObject = new Object();

        public UpdateMapThread() {
            super("UpdateMapThread");
        }

        public void run() {
            Log.p(Thread.currentThread().getName() + " thread started");
            while (!panStopThread) {
                synchronized (panSyncObject) {
                    try {
                        panSyncObject.wait();
                        mapTileEngine.drawUpdatedMapCanvas();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
            Log.p(Thread.currentThread().getName() + " thread stopped");
        }

        public void updateMapCommand() {
            synchronized (panSyncObject) {
                panSyncObject.notifyAll();

            }
        }
    }

}

