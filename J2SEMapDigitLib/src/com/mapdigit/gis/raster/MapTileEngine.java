//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 02SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE -----------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapDirection;
import com.mapdigit.drawing.Pen;
import com.mapdigit.drawing.geometry.Rectangle;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 02SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Base map tile Engine.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 02/09/10
 * @author      Guidebee Pty Ltd.
 */
abstract class MapTileEngine {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * the screen offset X
     * @return screen offset x
     */
    public int getScreenOffsetX() {
        return screenOffsetX;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get screen offset y
     * @return screen offset y
     */
    public int getScreenOffsetY() {
        return screenOffsetY;
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
        if (mapDrawingListener != null) {
            mapDrawingListener.done();
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
    public void zoomOut() {
        if (mapDrawingListener != null) {
            mapDrawingListener.done();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * pan direction.
     * @param dx
     * @param dy
     */
    public void panDirection(final int dx, final int dy) {
        screenOffsetX -= dx;
        screenOffsetY -= dy;
        if (mapDrawingListener != null) {
            mapDrawingListener.done();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set screen size.
     * @param width
     * @param height
     */
    public void setScreenSize(int width, int height) {
        screenOffsetX = (mapSize.width - width) / 2;
        screenOffsetY = (mapSize.height - height) / 2;
        screenRectangle.width = width;
        screenRectangle.height = height;
        screenRectangle.x = screenRectangle.y = 0;
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
        synchronized (syncObject) {
            mapTileDownloadManager.setRoutePen(routePen);
        }
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
        this.mapDrawingListener = mapDrawingListener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the map tile download manager.
     * @return
     */
    public MapTileDownloadManager getMapTileDownloadManager() {
        return mapTileDownloadManager;
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
            GeoPoint pt = rasterMap.fromLatLngToMapPixel(latlng);
            pt.x -= getScreenOffsetX();
            pt.y -= getScreenOffsetY();
            return pt;
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
            GeoPoint pt1 = new GeoPoint(pt);
            pt1.x += getScreenOffsetX();
            pt1.y += getScreenOffsetY();
            return rasterMap.fromMapPixelToLatLng(pt1);
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
            GeoPoint pt = new GeoPoint(getScreenOffsetX()
                    + screenRectangle.width / 2,
                    getScreenOffsetY()
                    + screenRectangle.height / 2);
            return rasterMap.fromMapPixelToLatLng(pt);
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
    public abstract void clearMapCache();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Restore map image cache from persistent memory.
     */
    public abstract void restoreMapCache();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Save map image cache to persistent memory.
     */
    public abstract void saveMapCache();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * paint map to given graphics canvas.
     * @param graphics graphics object.
     */
    public abstract void paint(IGraphics graphics);


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * paint map to given canvas at given location.
     * @param graphics  graphics object.
     * @param offsetX   location of x coord
     * @param offsetY   location of y coord.
     */
    public abstract void paint(IGraphics graphics, int offsetX, int offsetY);

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
    public abstract void setMapDirection(MapDirection newDirection);

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
    public abstract void setMapDirections(MapDirection[] newDirections);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clear map (routes etc).
     */
    public abstract void clearMapDirection();

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
    public abstract void setDownloadManager(MapTileDownloadManager mapTileDownloadManager);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw map canvas.
     */
    public abstract void drawMapCanvas();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw updated map canvas.
     */
    public abstract void drawUpdatedMapCanvas();

    /**
     * Map drawing  listener.
     */
    protected IMapDrawingListener mapDrawingListener = null;
    /**
     * Map tiles downloader
     */
    protected MapTileDownloadManager mapTileDownloadManager = null;
    protected final Rectangle mapSize = new Rectangle();
    protected final Rectangle screenRectangle;
    protected int screenOffsetX;
    protected int screenOffsetY;
    protected final Object syncObject;
    protected Rectangle mapRectangle = null;
    protected final RasterMap rasterMap;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param width width of the map.
     * @param height height of the map.
     * @param mapTileDownloadManager  map tile downloader manager
     * @param rasterMap raster map instance.
     */
    protected MapTileEngine(int width, int height,
            MapTileDownloadManager mapTileDownloadManager, RasterMap rasterMap) {
        this.rasterMap = rasterMap;
        syncObject = rasterMap.getSyncRoot();
        screenRectangle = new Rectangle(0, 0, rasterMap.getScreenWidth(),
                rasterMap.getScreenHeight());
        screenOffsetX = (width - screenRectangle.width) / 2;
        screenOffsetY = (height - screenRectangle.height) / 2;
        mapSize.x = 0;
        mapSize.y = 0;
        mapSize.width = width;
        mapSize.height = height;
        mapRectangle = new Rectangle(0, 0, width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * need to get a new map image.
     * @return
     */
    protected boolean needToGetNewMapImage() {
        screenRectangle.setX(screenOffsetX);
        screenRectangle.setY(screenOffsetY);
        return !mapRectangle.contains(screenRectangle);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * start engine.
     */
    void start(){

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * stop engine.
     */
    void stop(){

    }

}
