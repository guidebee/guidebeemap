//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09JUL2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Hashtable;
import java.util.Vector;

import com.mapdigit.util.MathEx;
import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.vector.SutherlandHodgman;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09JUL2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Default Map Tile Engine.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 09/07/10
 * @author      Guidebee Pty Ltd.
 */
final class DefaultMapTileEngine extends MapTileEngine {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param width  the width of the map.
     * @param height the height of the map.
     * @param mapTileDownloadManager map tile download manager
     * @param rasterMap raster map instance.
     */
    public DefaultMapTileEngine(int width, int height,
            MapTileDownloadManager mapTileDownloadManager, RasterMap rasterMap) {
        super(width, height, mapTileDownloadManager, rasterMap);
        if (mapTileDownloadManager != null) {
            this.mapTileDownloadManager = mapTileDownloadManager;
            this.mapTileDownloadManager.mapPanThread = null;
            this.mapTileDownloadManager.mapTileReadyListener = mapTileReadyListener;
            this.mapTileDownloadManager.rasterMap = rasterMap;
        }
        isLowMemoryMode=MapConfiguration.lowMemoryMode;
        mapImage = RasterMap.getAbstractGraphicsFactory().createImage(width, height);
        mapGraphics = mapImage.getGraphics();
        //the following Rect(s) are all temporary variables, initialze here
        //to avoid new /gc so ,these temp vairables are heavily used.
        centerRect = new GeoBounds(0, 0, 0, 0);
        drawRect = new GeoBounds(0, 0, 0, 0);
        mapRects = new GeoBounds[16];
        newMapRects = new GeoBounds[16];
        for (int i = 0; i < 16; i++) {
            mapRects[i] = new GeoBounds(0, 0, 0, 0);
            newMapRects[i] = new GeoBounds(0, 0, 0, 0);
        }
        mapRect = new GeoBounds(0, 0, 0, 0);
        newCenterRect = new GeoBounds(0, 0, 0, 0);
        
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void paint(IGraphics graphics) {
        paintInternal(graphics, -screenOffsetX,
                -screenOffsetY);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear map cache.
     */
    public void clearMapCache() {
        synchronized (syncObject) {
            mapTileDownloadManager.clearMapCache();
            whatsInMapCanvas.clear();
            whatsInMapCanvasMapDirection.clear();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clear map (routes etc).
     */
    public void clearMapDirection() {
        synchronized (syncObject) {
            mapTileDownloadManager.setMapDirection(null);
            whatsInMapCanvasMapDirection.clear();
            rasterMap.setCenter(rasterMap.getScreenCenter(), rasterMap.getZoom());
            System.gc();System.gc();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set download manager
     * @param mapTileDownloadManager
     */
    public void setDownloadManager(MapTileDownloadManager mapTileDownloadManager) {
        if (mapTileDownloadManager != null) {
            this.mapTileDownloadManager = mapTileDownloadManager;
            this.mapTileDownloadManager.mapTileReadyListener = mapTileReadyListener;
            this.mapTileDownloadManager.mapPanThread = null;
            this.mapTileDownloadManager.rasterMap = rasterMap;
        }
    }

    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * pan direction.
     * @param dx
     * @param dy
     */
    public void panDirection(final int dx, final int dy) {
        super.panDirection(dx, dy);
        if (needToGetNewMapImage()) {
            rasterMap.setCenterCommand();
        } else {
            //isMapImageStale check if the map rect has some image need to
            //update.
            if (isMapImageStale(rasterMap.getScreenCenter())) {
                rasterMap.updatedMapCommand();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void drawMapCanvas() {
        try {
            synchronized (syncObject) {
                pendingDrawImageQueue.clear();
                screenOffsetX = (rasterMap.getMapWidth()
                        - rasterMap.getScreenWidth()) / 2;
                screenOffsetY = (rasterMap.getMapHeight()
                        - rasterMap.getScreenHeight()) / 2;
                GeoPoint topLeft;
                GeoPoint bottomRight;
                int mapZoomLevel = rasterMap.getZoom();
                GeoPoint center = RasterMap.fromLatLngToPixel(rasterMap.getCenter(), mapZoomLevel);
                topLeft = new GeoPoint(center.x - mapSize.width / 2.0, center.y
                        - mapSize.height / 2.0);
                bottomRight = new GeoPoint(center.x + mapSize.width / 2.0,
                        center.y + mapSize.height / 2.0);
                GeoLatLng topLeftLatLng = RasterMap.fromPixelToLatLng(topLeft,
                        mapZoomLevel);
                GeoLatLng bottomRightLatLng = RasterMap.fromPixelToLatLng(bottomRight,
                        mapZoomLevel);
                GeoPoint topLeftIndex;
                GeoPoint bottomRightIndex;
                topLeftIndex = MapLayer.convertCoordindates2Tiles(topLeftLatLng.lat(),
                        topLeftLatLng.lng(), mapZoomLevel);
                bottomRightIndex =
                        MapLayer.convertCoordindates2Tiles(bottomRightLatLng.lat(),
                        bottomRightLatLng.lng(), mapZoomLevel);
                int maxTile = (int) (MathEx.pow(2, mapZoomLevel) + 0.5);
                int howMapTile = (Math.max(mapSize.getHeight(), mapSize.getWidth()) + 10)
                        / MapLayer.MAP_TILE_WIDTH;
                if (maxTile < howMapTile) {
                    topLeftIndex.x = 0;
                    topLeftIndex.y = 0;
                    bottomRightIndex.x = maxTile;
                    bottomRightIndex.y = maxTile;
                }
                int srcX, srcY;
                int srcWidth, srcHeight;
                int destX, destY;
                int xIndex, yIndex;
                mapGraphics.setColor(0xFFFFFF);
                mapGraphics.setClip(0, 0, mapSize.width,
                        mapSize.height);
                mapGraphics.fillRect(0, 0, mapSize.width,
                        mapSize.height);
                numOfMapRects = 0;
                //the centered rectangle
                centerRect.setRect(center.x - rasterMap.getScreenWidth() / 2.0,
                        center.y - rasterMap.getScreenHeight() / 2.0,
                        (double) rasterMap.getScreenWidth(), (double) rasterMap.getScreenHeight());
                drawRect.setRect(center.x - mapSize.width / 2.0,
                        center.y - mapSize.height / 2.0,
                        (double) mapSize.width, (double) mapSize.height);
                for (xIndex = 0; xIndex <= mapSize.width / MapTileDownloadManager.TILE_DOWNLOADING.getWidth(); xIndex++) {
                    for (yIndex = 0;
                            yIndex <= mapSize.height / MapTileDownloadManager.TILE_DOWNLOADING.getHeight();
                            yIndex++) {

                        mapGraphics.drawImage(MapTileDownloadManager.TILE_DOWNLOADING,
                                xIndex * MapTileDownloadManager.TILE_DOWNLOADING.getWidth(),
                                yIndex * MapTileDownloadManager.TILE_DOWNLOADING.getHeight());
                    }
                }
                whatsInMapCanvas.clear();
                if (!isLowMemoryMode) {
                    whatsInMapCanvasMapDirection.clear();
                }

                //get all the mapRect 256X256 which has intersection with
                //the center rectangle(screen rectangle).
                for (xIndex = (int) topLeftIndex.x;
                        xIndex <= bottomRightIndex.x; xIndex++) {
                    for (yIndex = (int) topLeftIndex.y;
                            yIndex <= bottomRightIndex.y; yIndex++) {
                        mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                                yIndex * MapLayer.MAP_TILE_WIDTH,
                                MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);
                        if (mapRect.intersects(centerRect.getX(),
                                centerRect.getY(),
                                centerRect.getWidth(),
                                centerRect.getHeight())) {

                            mapRects[numOfMapRects].setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                                    yIndex * MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH,
                                    MapLayer.MAP_TILE_WIDTH);
                            numOfMapRects++;
                        }
                    }
                }

                //reorder the map rects, make the most close to center rectangle
                //the first.
                reorderMapRects();

                for (int i = 0; i < numOfMapRects; i++) {
                    xIndex = (int) (mapRects[i].getX() / MapLayer.MAP_TILE_WIDTH + 0.5);
                    yIndex = (int) (mapRects[i].getY() / MapLayer.MAP_TILE_WIDTH + 0.5);
                    mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                            yIndex * MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH);
                    if (mapRect.intersects(centerRect.getX(), centerRect.getY(),
                            centerRect.getWidth(), centerRect.getHeight())) {

                        GeoBounds intersectRect = mapRect.createIntersection(drawRect);
                        srcX = (int) (intersectRect.getX()
                                - xIndex * MapLayer.MAP_TILE_WIDTH + 0.5);
                        srcY = (int) (intersectRect.getY()
                                - yIndex * MapLayer.MAP_TILE_WIDTH + 0.5);
                        srcWidth = MapLayer.MAP_TILE_WIDTH - srcX;

                        srcHeight = MapLayer.MAP_TILE_WIDTH - srcY;
                        destX = (int) (intersectRect.getX()
                                - drawRect.getX() + 0.5);
                        destY = (int) (intersectRect.getY()
                                - drawRect.getY() + 0.5);
                        int mapType = rasterMap.getMapType();
                        int[] map_sequences = (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
                        for (int mapSequenceIndex = 0; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                            IImage image = getImage(map_sequences[mapSequenceIndex], xIndex,
                                    yIndex,
                                    mapZoomLevel);

                            try {
                                if (image != null
                                        && image != MapTileDownloadManager.TILE_DOWNLOADING) {
                                    drawRegion(mapGraphics, image,
                                            srcX, srcY, srcWidth, srcHeight,
                                            destX, destY);
                                    String inMapCanvsKey =
                                            map_sequences[mapSequenceIndex] + "|" + xIndex + "|" + yIndex + "|" + mapZoomLevel;
                                    GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                                    whatsInMapCanvas.put(inMapCanvsKey, mapCavasIndex);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        //check if there's map direction need to draw.
                        if (mapTileDownloadManager.getMapDirection() != null) {
                            if (!isLowMemoryMode) {
                                String key = MapType.ROUTING_DIRECTION + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                IImage mapDirectionImage = (IImage) whatsInMapCanvasMapDirection.get(key);
                                if (mapDirectionImage != null) {
                                    drawRegion(mapGraphics, mapDirectionImage,
                                            srcX, srcY, srcWidth, srcHeight,
                                            destX, destY);
                                } else {
                                    getImage(MapType.ROUTING_DIRECTION, xIndex,
                                            yIndex,
                                            mapZoomLevel);
                                }
                            } else {
                                drawMapDirectionInLowMemoryMode(mapGraphics);
                            }

                        }
                    }
                    if (mapDrawingListener != null) {
                        mapDrawingListener.done();
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void paint(IGraphics graphics, int offsetX, int offsetY) {
        paintInternal(graphics, -screenOffsetX + offsetX,
                -screenOffsetY + offsetY);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Restore map image cache from persistent memory.
     */
    public void restoreMapCache() {
        synchronized (syncObject) {
            mapTileDownloadManager.restoreMapCache();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Save map image cache to persistent memory.
     */
    public void saveMapCache() {
        synchronized (syncObject) {
            mapTileDownloadManager.saveMapCache();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set routing direction.
     * @param newDirection new routing direction.
     */
    public void setMapDirections(MapDirection[] newDirections) {
        synchronized (syncObject) {
            whatsInMapCanvasMapDirection.clear();
            mapTileDownloadManager.setMapDirections(newDirections);
            if (newDirections != null) {
            } else {
                clearMapDirection();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get updated map images.(the screen rect still within the map rect)
     */
    public void drawUpdatedMapCanvas() {
        try {
            synchronized (syncObject) {
                int srcX, srcY;
                int srcWidth, srcHeight;
                int destX, destY;
                int xIndex, yIndex;
                for (int i = 0; i < needToUpdateMapIndexes.size(); i++) {
                    GeoPoint mapCanvasIndex =
                            (GeoPoint) needToUpdateMapIndexes.elementAt(i);

                    xIndex = (int) (mapCanvasIndex.getX() + 0.5);
                    yIndex = (int) (mapCanvasIndex.getY() + 0.5);
                    mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                            yIndex * MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);

                    GeoBounds intersectRect = mapRect.createIntersection(drawRect);
                    srcX = (int) (intersectRect.getX()
                            - xIndex * MapLayer.MAP_TILE_WIDTH + 0.5);
                    srcY = (int) (intersectRect.getY()
                            - yIndex * MapLayer.MAP_TILE_WIDTH + 0.5);
                    srcWidth = MapLayer.MAP_TILE_WIDTH - srcX;

                    srcHeight = MapLayer.MAP_TILE_WIDTH - srcY;
                    destX = (int) (intersectRect.getX() - drawRect.getX() + 0.5);
                    destY = (int) (intersectRect.getY() - drawRect.getY() + 0.5);
                    int mapType = rasterMap.getMapType();
                    int mapZoomLevel = rasterMap.getZoom();

                    int[] map_sequences
                            = (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
                    for (int mapSequenceIndex = 0;
                    mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                        IImage image = getImage(map_sequences[mapSequenceIndex],
                                xIndex,
                                yIndex,
                                mapZoomLevel);

                        try {
                            if (image != null && image
                                    != MapTileDownloadManager.TILE_DOWNLOADING) {
                                drawRegion(mapGraphics, image,
                                        srcX, srcY, srcWidth, srcHeight,
                                        destX, destY);
                                String inMapCanvsKey 
                                        = map_sequences[mapSequenceIndex]
                                        + "|" + xIndex + "|" + yIndex
                                        + "|" + mapZoomLevel;
                                GeoPoint mapCavasIndex
                                        = new GeoPoint(xIndex, yIndex);
                                whatsInMapCanvas.put(inMapCanvsKey, mapCavasIndex);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    if (mapTileDownloadManager.getMapDirection() != null) {
                        getImage(MapType.ROUTING_DIRECTION, xIndex,
                                yIndex,
                                mapZoomLevel);

                    }

                }
                if (mapDrawingListener != null) {
                    mapDrawingListener.done();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set routing direction.
     * @param newDirection new routing direction.
     */
    public void setMapDirection(MapDirection newDirection) {
        synchronized (syncObject) {
            whatsInMapCanvasMapDirection.clear();
            mapTileDownloadManager.setMapDirection(newDirection);
            if (newDirection != null) {
            } else {
                clearMapDirection();
            }
        }
    }

    /**
     * the map canvas
     */
    private IImage mapImage = null;
    /**
     * Graphics Object for map canvas.
     */
    private IGraphics mapGraphics = null;
    /**
     * pending drawing image queue, used to order the routing and map images.
     */
    private boolean isLowMemoryMode=false;
    private final Hashtable pendingDrawImageQueue = new Hashtable();
    private GeoBounds centerRect;
    private int numOfMapRects = 0;
    private GeoBounds[] mapRects;
    private GeoBounds newCenterRect;
    private int numOfNewMapRects = 0;
    private GeoBounds[] newMapRects;
    private GeoBounds drawRect;
    private GeoBounds mapRect;
    private final Hashtable whatsInMapCanvas = new Hashtable();
    private final Hashtable whatsInMapCanvasMapDirection = new Hashtable();
    private Vector needToUpdateMapIndexes = new Vector();

    private final IMapTileReadyListener mapTileReadyListener
            = new IMapTileReadyListener() {

        public void done(ImageTileIndex imageTileIndex, IImage image) {
            try {
                drawMapTileInMapCanvas(imageTileIndex, image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see if need to download images according to the new center.
     */
    private boolean isMapImageStale(GeoLatLng newCenter) {
        synchronized (syncObject) {
            GeoPoint topLeft;
            GeoPoint bottomRight;
            int mapZoomLevel = rasterMap.getZoom();
            GeoPoint center = RasterMap.fromLatLngToPixel(newCenter, mapZoomLevel);
            topLeft = new GeoPoint(center.x - mapSize.width / 2.0,
                    center.y - mapSize.height / 2.0);
            bottomRight = new GeoPoint(center.x + mapSize.width / 2.0,
                    center.y + mapSize.height / 2.0);
            GeoLatLng topLeftLatLng
                    = RasterMap.fromPixelToLatLng(topLeft, mapZoomLevel);
            GeoLatLng bottomRightLatLng
                    = RasterMap.fromPixelToLatLng(bottomRight, mapZoomLevel);

            GeoPoint topLeftIndex;
            GeoPoint bottomRightIndex;
            topLeftIndex = MapLayer.convertCoordindates2Tiles(topLeftLatLng.lat(),
                    topLeftLatLng.lng(), mapZoomLevel);
            bottomRightIndex
                    = MapLayer.convertCoordindates2Tiles(bottomRightLatLng.lat(),
                    bottomRightLatLng.lng(), mapZoomLevel);
            int xIndex, yIndex;
            numOfNewMapRects = 0;
            newCenterRect.setRect(center.x - rasterMap.getScreenWidth() / 2.0,
                    center.y - rasterMap.getScreenHeight() / 2.0,
                    (double) rasterMap.getScreenWidth(),
                    (double) rasterMap.getScreenHeight());

            for (xIndex = (int) topLeftIndex.x;
                    xIndex <= bottomRightIndex.x; xIndex++) {
                for (yIndex = (int) topLeftIndex.y;
                        yIndex <= bottomRightIndex.y; yIndex++) {
                    mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                            yIndex * MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH);
                    if (mapRect.intersects(newCenterRect.getX(),
                            newCenterRect.getY(),
                            newCenterRect.getWidth(),
                            newCenterRect.getHeight())) {
                        newMapRects[numOfNewMapRects]
                                .setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                                yIndex * MapLayer.MAP_TILE_WIDTH,
                                MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);
                        numOfNewMapRects++;
                    }
                }
            }
            needToUpdateMapIndexes.removeAllElements();
            int mapType = rasterMap.getMapType();
            int[] map_sequences
                    = (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
            for (int i = 0; i < numOfNewMapRects; i++) {
                xIndex = (int) (newMapRects[i].getX()
                        / MapLayer.MAP_TILE_WIDTH + 0.5);
                yIndex = (int) (newMapRects[i].getY()
                        / MapLayer.MAP_TILE_WIDTH + 0.5);
                for (int mapSequenceIndex = 0;
                mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                    String key = map_sequences[mapSequenceIndex] + "|"
                            + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                    if (!whatsInMapCanvas.containsKey(key)) {
                        GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                        needToUpdateMapIndexes.addElement(mapCavasIndex);
                        break;
                    }
                    if (mapTileDownloadManager.getMapDirection() != null
                            && !isLowMemoryMode) {
                        key = MapType.ROUTING_DIRECTION + "|"
                                + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                        if (!whatsInMapCanvasMapDirection.containsKey(key)) {
                            GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                            needToUpdateMapIndexes.addElement(mapCavasIndex);
                            break;
                        }
                    }

                }
            }
        }
        return needToUpdateMapIndexes.size() != 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * paint internal.
     * @param graphics
     * @param offsetX
     * @param offsetY
     */
    private void paintInternal(IGraphics graphics, int offsetX, int offsetY) {
        drawTileDownloading(graphics, offsetX, offsetY);
        graphics.drawImage(mapImage,
                offsetX,
                offsetY);
    }
    
    /**
     *
     */
    private void drawMapTileInMapCanvas(ImageTileIndex imageTileIndex,
            IImage image) {
        try {
            synchronized (syncObject) {
                int mapType = rasterMap.getMapType();
                int mapZoomLevel = rasterMap.getZoom();
                //check to see need to draw this map tile
                if (imageTileIndex.mapType != MapType.ROUTING_DIRECTION) {
                    int[] map_sequences
                            = (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
                    int i = 0;
                    for (i = 0; i < map_sequences.length; i++) {
                        if (map_sequences[i] == imageTileIndex.mapType) {
                            break;
                        }
                    }
                    if (i == map_sequences.length) {
                        return;
                    }
                }
                if (mapZoomLevel == imageTileIndex.mapZoomLevel) {
                    GeoPoint topLeft;
                    GeoPoint bottomRight;
                    GeoPoint center = RasterMap.fromLatLngToPixel(rasterMap.getScreenCenter(),
                            mapZoomLevel);
                    topLeft = new GeoPoint(center.x - mapSize.width / 2.0,
                            center.y - mapSize.height / 2.0);
                    bottomRight = new GeoPoint(center.x + mapSize.width / 2.0,
                            center.y + mapSize.height / 2.0);
                    GeoLatLng topLeftLatLng =
                            RasterMap.fromPixelToLatLng(topLeft, mapZoomLevel);
                    GeoLatLng bottomRightLatLng =
                            RasterMap.fromPixelToLatLng(bottomRight, mapZoomLevel);

                    GeoPoint topLeftIndex;
                    GeoPoint bottomRightIndex;
                    topLeftIndex = MapLayer.convertCoordindates2Tiles(topLeftLatLng.lat(),
                            topLeftLatLng.lng(), mapZoomLevel);
                    bottomRightIndex = MapLayer.convertCoordindates2Tiles(
                            bottomRightLatLng.lat(),
                            bottomRightLatLng.lng(), mapZoomLevel);
                    int maxTile = (int) (MathEx.pow(2, mapZoomLevel) + 0.5);
                    int howMapTile = (Math.max(mapSize.getHeight(), mapSize.getWidth()) + 10) / MapLayer.MAP_TILE_WIDTH;
                    if (maxTile < howMapTile) {
                        topLeftIndex.x = 0;
                        topLeftIndex.y = 0;
                        bottomRightIndex.x = howMapTile;
                        bottomRightIndex.y = howMapTile;
                    }
                    int xIndex, yIndex;
                    int destX, destY;
                    int srcX, srcY;
                    int srcWidth, srcHeight;
                    numOfMapRects = 0;
                    centerRect.setRect(center.x - rasterMap.getScreenWidth() / 2.0,
                            center.y - rasterMap.getScreenHeight() / 2.0,
                            (double) rasterMap.getScreenWidth(),
                            (double) rasterMap.getScreenHeight());


                    for (xIndex = (int) topLeftIndex.x;
                            xIndex <= bottomRightIndex.x; xIndex++) {
                        for (yIndex = (int) topLeftIndex.y;
                                yIndex <= bottomRightIndex.y; yIndex++) {
                            mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                                    yIndex * MapLayer.MAP_TILE_WIDTH,
                                    MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);
                            if (mapRect.intersects(centerRect.getX(),
                                    centerRect.getY(),
                                    centerRect.getWidth(),
                                    centerRect.getHeight())) {

                                mapRects[numOfMapRects].setRect(
                                        xIndex * MapLayer.MAP_TILE_WIDTH,
                                        yIndex * MapLayer.MAP_TILE_WIDTH,
                                        MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);
                                numOfMapRects++;
                            }
                        }
                    }

                    for (int i = 0; i < numOfMapRects; i++) {
                        xIndex = (int) (mapRects[i].getX()
                                / MapLayer.MAP_TILE_WIDTH + 0.5);
                        yIndex = (int) (mapRects[i].getY()
                                / MapLayer.MAP_TILE_WIDTH + 0.5);
                        if (xIndex == imageTileIndex.xIndex
                                && yIndex == imageTileIndex.yIndex) {
                            mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                                    yIndex * MapLayer.MAP_TILE_WIDTH,
                                    MapLayer.MAP_TILE_WIDTH,
                                    MapLayer.MAP_TILE_WIDTH);
                            if (mapRect.intersects(centerRect.getX(),
                                    centerRect.getY(),
                                    centerRect.getWidth(),
                                    centerRect.getHeight())) {
                                GeoBounds intersectRect
                                        = mapRect.createIntersection(drawRect);
                                srcX = (int) (intersectRect.getX()
                                        - xIndex * MapLayer.MAP_TILE_WIDTH + 0.5);
                                srcY = (int) (intersectRect.getY()
                                        - yIndex * MapLayer.MAP_TILE_WIDTH + 0.5);
                                srcWidth = MapLayer.MAP_TILE_WIDTH - srcX;

                                srcHeight = MapLayer.MAP_TILE_WIDTH - srcY;
                                destX = (int) (intersectRect.getX()
                                        - drawRect.getX() + 0.5);
                                destY = (int) (intersectRect.getY()
                                        - drawRect.getY() + 0.5);
                                try {
                                    if (image != null && image
                                            != MapTileDownloadManager.TILE_DOWNLOADING) {
                                        if (imageTileIndex.mapType
                                                != MapType.ROUTING_DIRECTION) {

                                            //first draw map tiles below current map tile.
                                            //if there's map tile below current map tile havn't
                                            //be drawn, adding current map tile to pending queue.
                                            int[] map_sequences
                                                    = (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
                                            boolean needToAddToQueue = false;
                                            for (int mapSequenceIndex = 0; 
                                            mapSequenceIndex < map_sequences.length;
                                                mapSequenceIndex++) {
                                                if (imageTileIndex.mapType
                                                        == map_sequences[mapSequenceIndex]) {
                                                    break;
                                                }
                                                IImage backImage 
                                                        = getCachedImage(map_sequences[mapSequenceIndex],
                                                        xIndex,
                                                        yIndex,
                                                        mapZoomLevel);

                                                String key = map_sequences[mapSequenceIndex]
                                                        + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                if (!whatsInMapCanvas.containsKey(key)) {
                                                    needToAddToQueue = true;
                                                }
                                                if (backImage != null && backImage != MapTileDownloadManager.TILE_DOWNLOADING) {
                                                    drawRegion(mapGraphics, backImage,
                                                            srcX, srcY, srcWidth,
                                                            srcHeight, destX, destY);

                                                } else {
                                                    needToAddToQueue = true;
                                                }
                                            }

                                            if (needToAddToQueue) {
                                                String key = imageTileIndex.mapType + "|"
                                                        + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                pendingDrawImageQueue.put(key, image);
                                            }

                                            //draw current map tile.
                                            drawRegion(mapGraphics, image,
                                                    srcX, srcY, srcWidth,
                                                    srcHeight, destX, destY);

                                            //find next image sequence.
                                            int nextImageSequence = 0;
                                            for (int mapSequenceIndex = 0; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                                                if (imageTileIndex.mapType == map_sequences[mapSequenceIndex]) {
                                                    nextImageSequence = mapSequenceIndex + 1;
                                                    break;
                                                }
                                            }
                                            //draw next image sequences.
                                            for (int mapSequenceIndex = nextImageSequence; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                                                String key = map_sequences[mapSequenceIndex] + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;

                                                IImage pendImage = (IImage) pendingDrawImageQueue.get(key);
                                                if (pendImage != null) {
                                                    drawRegion(mapGraphics, pendImage,
                                                            srcX, srcY, srcWidth,
                                                            srcHeight, destX, destY);

                                                }
                                            }
                                            if (mapTileDownloadManager.getMapDirection() != null) {
                                                if (!isLowMemoryMode) {
                                                    String key = MapType.ROUTING_DIRECTION + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;

                                                    IImage pendImage = (IImage) pendingDrawImageQueue.get(key);
                                                    if (pendImage != null) {
                                                        drawRegion(mapGraphics, pendImage,
                                                                srcX, srcY, srcWidth,
                                                                srcHeight, destX, destY);

                                                        //check to see need to remove the route image.
                                                        boolean needRemoveRoutingImage = true;
                                                        for (int mapSequenceIndex = 0; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {

                                                            IImage pendImage1 = getCachedImage(map_sequences[mapSequenceIndex], xIndex,
                                                                    yIndex,
                                                                    mapZoomLevel);
                                                            String key1 = map_sequences[mapSequenceIndex] + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                            if (pendImage1 == null || pendImage1 == MapTileDownloadManager.TILE_DOWNLOADING || !whatsInMapCanvas.containsKey(key1)) {
                                                                needRemoveRoutingImage = false;
                                                                break;
                                                            }
                                                        }

                                                        if (needRemoveRoutingImage) {
                                                            pendingDrawImageQueue.remove(key);
                                                        }
                                                    }
                                                } else {
                                                    drawMapDirectionInLowMemoryMode(mapGraphics);
                                                }

                                            }
                                            String inMapCanvsKey = imageTileIndex.mapType + "|" + (xIndex) + "|" + (yIndex) + "|" + imageTileIndex.mapZoomLevel;
                                            GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                                            whatsInMapCanvas.put(inMapCanvsKey,
                                                    mapCavasIndex);

                                            //check to see to remove from pendingDrawImageQueue

                                            boolean needRemovePendingImage = true;
                                            for (int mapSequenceIndex = 0; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {

                                                IImage pendImage1 = getCachedImage(map_sequences[mapSequenceIndex], xIndex,
                                                        yIndex,
                                                        mapZoomLevel);
                                                String key = map_sequences[mapSequenceIndex] + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                if (pendImage1 == null || pendImage1 == MapTileDownloadManager.TILE_DOWNLOADING) {
                                                    needRemovePendingImage = false;
                                                    break;
                                                }
                                            }

                                            if (needRemovePendingImage) {
                                                for (int mapSequenceIndex = 0; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                                                    String key = map_sequences[mapSequenceIndex] + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                    if (pendingDrawImageQueue.containsKey(key)) {
                                                        pendingDrawImageQueue.remove(key);
                                                    }
                                                }

                                            }


                                        } else {//routing image.

                                            if (!isLowMemoryMode) {
                                                int[] map_sequences = (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
                                                boolean needToAddToQueue = false;
                                                for (int mapSequenceIndex = 0; mapSequenceIndex < map_sequences.length; mapSequenceIndex++) {
                                                    IImage backImage = getCachedImage(map_sequences[mapSequenceIndex], xIndex,
                                                            yIndex,
                                                            mapZoomLevel);

                                                    String key = map_sequences[mapSequenceIndex] + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                    if (!whatsInMapCanvas.containsKey(key)) {
                                                        needToAddToQueue = true;
                                                    }

                                                    if (backImage != null && backImage != MapTileDownloadManager.TILE_DOWNLOADING) {
                                                        drawRegion(mapGraphics, backImage,
                                                                srcX, srcY, srcWidth,
                                                                srcHeight, destX, destY);

                                                    } else {
                                                        needToAddToQueue = true;
                                                    }
                                                }

                                                if (needToAddToQueue) {
                                                    String key = MapType.ROUTING_DIRECTION + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                    pendingDrawImageQueue.put(key, image);

                                                }
                                              drawRegion(mapGraphics, image,
                                                        srcX, srcY, srcWidth,
                                                        srcHeight, destX, destY);
                                                String key = MapType.ROUTING_DIRECTION + "|" + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                                                whatsInMapCanvasMapDirection.put(key, image);
                                            } else {
                                                drawMapDirectionInLowMemoryMode(mapGraphics);
                                            }
                                        }



                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                    }
                    if (mapDrawingListener != null) {
                        mapDrawingListener.done();
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS --------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////
    /**
     * check if need to show on map.
     * @param numLevel
     * @param zoomLevel
     * @return
     */
    private int needShowLevel(int numLevel, int zoomLevel) {
        int totalZoomLevel = 16;
        int mapGrade = (totalZoomLevel - zoomLevel) / numLevel - 1;
        return mapGrade;

    }

    private final static int ROUTE_ICON_WIDTH = 8;

    private void drawRouteIconInLowMemoryMode(IGraphics g, int x, int y, int color) {
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        int width = ROUTE_ICON_WIDTH / 2;
        xPoints[0] = x;
        yPoints[0] = y - width;
        xPoints[1] = x - width;
        yPoints[1] = y;
        xPoints[2] = x;
        yPoints[2] = y + width;
        xPoints[3] = x + width;
        yPoints[3] = y;

        g.setColor(color);
        g.fillTriangle(xPoints[0], yPoints[0],
                xPoints[1], yPoints[1],
                xPoints[2], yPoints[2]);
        g.fillTriangle(xPoints[2], yPoints[2],
                xPoints[3], yPoints[3],
                xPoints[0], yPoints[0]);
        g.setColor(0);
        for (int i = 0; i < 4; i++) {
            g.drawLine(xPoints[i], yPoints[i], xPoints[(i + 1) % 4], yPoints[(i + 1) % 4]);
        }

    }

    ////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS --------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ------------------
    // 09JUL2010  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////
    /**
     * draw route icon in given rectangle.
     * @param mapDirection
     * @param x
     * @param y
     * @param width
     * @param height
     */
    private void drawRouteIconsInLowMemoryMode(IGraphics g, MapDirection[] mapDirections,
            int x, int y) {

        for (int k = 0; k < mapDirections.length; k++) {
            MapDirection mapDirection = mapDirections[k];
            GeoPoint newPt;
            GeoLatLng pt;
            //g.setClip(0, 0, width, height);
            GeoLatLngBounds mapBounds = rasterMap.getMapBounds();
            for (int j = 0; j < mapDirection.routes.length; j++) {
                for (int i = 0; i < mapDirection.routes[j].steps.length - 1; i++) {
                    MapStep mapStep = mapDirection.routes[j].steps[i];
                    pt = mapStep.lastLatLng;
                    if (mapBounds.containsLatLng(pt)) {
                        newPt = rasterMap.fromLatLngToMapPixel(pt);
                        newPt.x -= x;
                        newPt.y -= y;
                        drawRouteIconInLowMemoryMode(g, (int) newPt.x, (int) newPt.y, 0xFFA000);
                    }
                }
            }
            pt = mapDirection.polyline.getVertex(0);
            newPt = rasterMap.fromLatLngToMapPixel(pt);
            newPt.x -= x;
            newPt.y -= y;
            drawRouteIconInLowMemoryMode(g, (int) newPt.x, (int) newPt.y, 0xFF0000);
            pt = mapDirection.polyline.getVertex(mapDirection.polyline.getVertexCount() - 1);
            newPt = rasterMap.fromLatLngToMapPixel(pt);
            newPt.x -= x;
            newPt.y -= y;
            drawRouteIconInLowMemoryMode(g, (int) newPt.x, (int) newPt.y, 0x0000FF);
        }

    }

    private void drawMapDirectionInLowMemoryMode(IGraphics g) {
        MapDirection[] mapDirections = mapTileDownloadManager.getMapDirections();
        if (mapDirections != null) {
            for (int k = 0; k < mapDirections.length; k++) {
                MapDirection mapDirection = mapDirections[k];
                GeoLatLngBounds mapBounds = rasterMap.getMapBounds();
                SutherlandHodgman sutherlandHodgman = new SutherlandHodgman(mapBounds);
                Vector polyline = new Vector();
                int minLevel = needShowLevel(mapDirection.polyline.numLevels, rasterMap.getZoom());
                for (int i = 0; i < mapDirection.polyline.getVertexCount(); i++) {
                    int level = mapDirection.polyline.getLevel(i);
                    if (level >= minLevel) {
                        polyline.addElement(mapDirection.polyline.getVertex(i));
                    }
                }
                Vector clippedPts = sutherlandHodgman.ClipPline(polyline);
                boolean hasPt1 = false;
                GeoLatLng pt1 = null, pt2 = null, pt;
                GeoPoint newPt1 = new GeoPoint(0, 0);
                GeoPoint newPt2 = new GeoPoint(0, 0);
                int level;
                GeoPoint drawPt1 = new GeoPoint(0, 0), drawPt2 = new GeoPoint(0, 0);

                int steps = 1;
                g.setColor(0x00FF00);
                hasPt1 = false;
                pt1 = null;
                pt2 = null;
                int totalPointSize = clippedPts.size();


                try {
                    for (int j = 0; j < totalPointSize; j += steps) {
                        pt = (GeoLatLng) clippedPts.elementAt(j);
                        level = minLevel;
                        if (hasPt1 == false) {
                            if (level >= minLevel) {
                                {
                                    {
                                        hasPt1 = true;
                                        pt1 = pt;
                                        continue;
                                    }
                                }
                            }
                        }
                        if (hasPt1) {
                            if (level >= minLevel) {
                                pt2 = pt;
                                newPt1 = rasterMap.fromLatLngToMapPixel(pt1);
                                newPt2 = rasterMap.fromLatLngToMapPixel(pt2);

                                drawPt1.x = (int) newPt1.x;
                                drawPt1.y = (int) newPt1.y;
                                drawPt2.x = (int) newPt2.x;
                                drawPt2.y = (int) newPt2.y;

                                if ((drawPt1.distance(drawPt2) > 0)) {
                                    g.drawLine((int) drawPt1.x,
                                            (int) drawPt1.y,
                                            (int) drawPt2.x, (int) drawPt2.y);
                                    g.drawLine((int) drawPt1.x,
                                            (int) drawPt1.y + 1,
                                            (int) drawPt2.x, (int) drawPt2.y + 1);
                                    g.drawLine((int) drawPt1.x,
                                            (int) drawPt1.y - 1,
                                            (int) drawPt2.x, (int) drawPt2.y - 1);
                                    hasPt1 = true;
                                    pt1 = pt2;

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
        drawRouteIconsInLowMemoryMode(g, mapDirections, 0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  draw default downloading tile.
     * @param graphics
     * @param offsetX
     * @param offsetY
     */
    private void drawTileDownloading(IGraphics graphics, int offsetX, int offsetY) {
        if (needToGetNewMapImage()) {
            IImage imageDownloading = MapTileDownloadManager.TILE_DOWNLOADING;
            if (imageDownloading != null) {
                graphics.setColor(0xFFFFFF);
                graphics.setClip(0, 0, rasterMap.getScreenWidth(),
                        rasterMap.getScreenHeight());
                graphics.fillRect(0, 0, rasterMap.getScreenWidth(),
                        rasterMap.getScreenHeight());
                int xIndex, yIndex;
                int tileWidth = imageDownloading.getHeight();
                int offX = offsetX - offsetX / tileWidth * tileWidth;
                int offY = offsetY - offsetY / tileWidth * tileWidth;
                for (xIndex = -1; xIndex <= rasterMap.getScreenWidth() / tileWidth + 1; xIndex++) {
                    for (yIndex = -1;
                            yIndex <= rasterMap.getScreenHeight() / tileWidth + 1;
                            yIndex++) {
                        graphics.drawImage(imageDownloading,
                                offX + xIndex * tileWidth, offY + yIndex * tileWidth);
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reorder the map tiles, make the most close to center download first
     */
    private void reorderMapRects() {
        int i, j;
        double dj = 0, dai = 0;
        GeoBounds ai = new GeoBounds(0, 0, 0, 0);
        for (i = 1; i < numOfMapRects; i++) {
            j = i - 1;
            ai.setRect(mapRects[i].getX(),
                    mapRects[i].getY(),
                    mapRects[i].getWidth(),
                    mapRects[i].getHeight());
            dj = (mapRects[j].getCenterX() - centerRect.getCenterX())
                    * (mapRects[j].getCenterX() - centerRect.getCenterX())
                    + (mapRects[j].getCenterY() - centerRect.getCenterY())
                    * (mapRects[j].getCenterY() - centerRect.getCenterY());

            dai = (ai.getCenterX() - centerRect.getCenterX())
                    * (ai.getCenterX() - centerRect.getCenterX())
                    + (ai.getCenterY() - centerRect.getCenterY())
                    * (ai.getCenterY() - centerRect.getCenterY());
            while (dj > dai && j >= 0) {
                mapRects[j + 1].setRect(mapRects[j].getX(),
                        mapRects[j].getY(),
                        mapRects[j].getWidth(),
                        mapRects[j].getHeight());
                j--;
            }
            mapRects[j + 1].setRect(ai.getX(),
                    ai.getY(),
                    ai.getWidth(),
                    ai.getHeight());

        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * ask download mananger to download required map tile.
     * @param mtype
     * @param x
     * @param y
     * @param zoomLevel
     * @return
     */
    private IImage getCachedImage(int mtype, int x, int y, int zoomLevel) {
        return mapTileDownloadManager.getCachedImage(mtype, x, y,
                zoomLevel);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * ask download mananger to download required map tile.
     * @param mtype
     * @param x
     * @param y
     * @param zoomLevel
     * @return
     */
    private IImage getImage(int mtype, int x, int y, int zoomLevel) {
        return mapTileDownloadManager.getImage(mtype, x, y,
                zoomLevel);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Draw specific region on the map canvas.
     * @param graphics
     * @param src
     * @param x_src
     * @param y_src
     * @param width
     * @param height
     * @param x_dest
     * @param y_dest
     */
    private void drawRegion(IGraphics graphics, IImage src,
            int x_src,
            int y_src,
            int width,
            int height,
            int x_dest,
            int y_dest) {
        if (width != 0 && height != 0) {
            graphics.setClip(x_dest, y_dest, width, height);
            graphics.drawImage(src, -x_src + x_dest, -y_src + y_dest);

        }
    }

}
