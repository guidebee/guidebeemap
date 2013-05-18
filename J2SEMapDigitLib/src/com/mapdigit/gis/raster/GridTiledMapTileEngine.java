//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 02SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Hashtable;
import java.util.Vector;
import com.mapdigit.util.MathEx;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.drawing.geometry.Rectangle;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 02SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Grid Map Tile Engine. (not fully function)
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 0q/09/10
 * @author      Guidebee Pty Ltd.
 */
final class GridTiledMapTileEngine extends TiledMapTileEngine {

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
    public GridTiledMapTileEngine(int width, int height,
            MapTileDownloadManager mapTileDownloadManager, RasterMap rasterMap) {
        super(width, height, mapTileDownloadManager, rasterMap);
        if (mapTileDownloadManager != null) {
            this.mapTileDownloadManager = mapTileDownloadManager;
            this.mapTileDownloadManager.mapPanThread = null;
            this.mapTileDownloadManager.mapTileReadyListener = mapTileReadyListener;
            this.mapTileDownloadManager.rasterMap = rasterMap;
        }
        //the following Rect(s) are all temporary variables, initialze here
        //to avoid new /gc so ,these temp vairables are heavily used.
        centerRect = new GeoBounds(0, 0, 0, 0);
        drawRect = new GeoBounds(0, 0, 0, 0);

        mapRect = new GeoBounds(0, 0, 0, 0);
        newCenterRect = new GeoBounds(0, 0, 0, 0);

        
        mapRects = new GeoBounds[rows * cols];
        newMapRects = new GeoBounds[rows * cols];
        for (int i = 0; i < newMapRects.length; i++) {
            mapRects[i] = new GeoBounds(0, 0, 0, 0);
            newMapRects[i] = new GeoBounds(0, 0, 0, 0);
        }


        tiles = new int[rows][cols];
        mapRectangle.setBounds(0, 0, DEFAULT_ROW_COL_NUMS * MapLayer.MAP_TILE_WIDTH,
                DEFAULT_ROW_COL_NUMS * MapLayer.MAP_TILE_WIDTH);
        mapSize.x = 0;
        mapSize.y = 0;
        mapSize.width = DEFAULT_ROW_COL_NUMS * MapLayer.MAP_TILE_WIDTH;
        mapSize.height = DEFAULT_ROW_COL_NUMS * MapLayer.MAP_TILE_WIDTH;

    }

    
    private void reorderTiles() {
        reorderTilesQueue.clear();
        for (int i = 0; i < mapTileImages.length; i++) {
            if (!mapTileImages[i].avaiable) {
                String key = mapTileImages[i].mapTileIndex.mapType + "|"
                        + (mapTileImages[i].mapTileIndex.xIndex) + "|"
                        + (mapTileImages[i].mapTileIndex.yIndex) + "|"
                        + mapTileImages[i].mapTileIndex.mapZoomLevel;
                reorderTilesQueue.put(key, new Integer(i));
            }
        }
        GeoPoint topLeft;

        int mapZoomLevel = rasterMap.getZoom();
        GeoPoint center = RasterMap.fromLatLngToPixel(rasterMap.getCenter(), mapZoomLevel);
        topLeft = new GeoPoint(center.x - mapSize.width / 2.0, center.y
                - mapSize.height / 2.0);


  
        int mapType = rasterMap.getMapType();
        //the centered rectangle
        centerRect.setRect(center.x - rasterMap.getScreenWidth() / 2.0,
                center.y - rasterMap.getScreenHeight() / 2.0,
                (double) rasterMap.getScreenWidth(), (double) rasterMap.getScreenHeight());


//            GeoPoint bottomRight;

            topLeft = new GeoPoint(center.x - mapSize.width / 2.0,
                    center.y - mapSize.height / 2.0);
//            bottomRight = new GeoPoint(center.x + mapSize.width / 2.0,
//                    center.y + mapSize.height / 2.0);



            topLeft.x = centerRect.x / MapLayer.MAP_TILE_WIDTH - 1;
            topLeft.y = centerRect.y / MapLayer.MAP_TILE_WIDTH - 1;

//            bottomRight.x = (centerRect.x + centerRect.width) / MapLayer.MAP_TILE_WIDTH + 1;
//            bottomRight.y = (centerRect.y + centerRect.height) / MapLayer.MAP_TILE_WIDTH + 1;
//


        for (int xIndex = 0; xIndex < DEFAULT_ROW_COL_NUMS; xIndex++) {
            for (int yIndex = 0; yIndex < DEFAULT_ROW_COL_NUMS; yIndex++) {
                String key = mapType
                        + "|" + (xIndex + (int) topLeft.x)
                        + "|" + (yIndex + (int) topLeft.y) + "|" + mapZoomLevel;
                mapRect.setRect((xIndex + (int) topLeft.x) * MapLayer.MAP_TILE_WIDTH,
                        (yIndex + (int) topLeft.y) * MapLayer.MAP_TILE_WIDTH,
                        MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);

                if (mapRect.intersects(centerRect.getX(),
                        centerRect.getY(),
                        centerRect.getWidth(),
                        centerRect.getHeight())) {
                    Integer index = (Integer) reorderTilesQueue.get(key);
                    if (index != null) {
                        tiles[xIndex][yIndex] = index.intValue();
                    } else {

                        //get the missing tile
                        ImageTileIndex imageTileIndex = new ImageTileIndex();
                        imageTileIndex.mapType = mapType;
                        imageTileIndex.xIndex = xIndex + (int) topLeft.x;
                        imageTileIndex.yIndex = yIndex + (int) topLeft.y;
                        imageTileIndex.mapZoomLevel = mapZoomLevel;
                        int tileIndex = getAvaiableMapTileIndex(imageTileIndex);
                        tiles[xIndex][yIndex] = tileIndex;
                        if (tileIndex > TILE_DOWNLOADING) {
                            mapTileImages[tileIndex].updateMapCanvas();
                        }
                       //  tiles[xIndex][yIndex] = TILE_DOWNLOADING;

                    }
                } else {
                    tiles[xIndex][yIndex] = TILE_DOWNLOADING;
                }
            }
        }

    }

    private void drawMapImage(IGraphics graphics, int offsetX, int offsetY) {
        Rectangle drawRect1 = new Rectangle(0, 0, 0, 0);
        Rectangle r = new Rectangle(screenRectangle);
        r.x = r.y = 0;
        int mapZoomLevel = rasterMap.getZoom();
        GeoPoint center = RasterMap.fromLatLngToPixel(rasterMap.getCenter(), mapZoomLevel);
        GeoPoint topLeft = new GeoPoint(center.x - rasterMap.getMapWidth() / 2.0,
                center.y - rasterMap.getMapHeight() / 2.0);
        
        GeoLatLng topLeftLatLng = RasterMap.fromPixelToLatLng(topLeft,
                mapZoomLevel);

        GeoPoint topLeftIndex;

        topLeftIndex = MapLayer.convertCoordindates2Tiles(topLeftLatLng.lat(),
                topLeftLatLng.lng(), mapZoomLevel);

        double realCenterX=((int)topLeftIndex.x)* MapLayer.MAP_TILE_WIDTH+mapSize.width/2;
        double realCenterY=((int)topLeftIndex.y)* MapLayer.MAP_TILE_WIDTH+mapSize.height/2;


        int deltaX=-(int)(center.x-realCenterX);
        int deltaY=-(int)(center.y-realCenterY);
        drawTileDownloading(graphics,offsetX+deltaX,offsetY+deltaY);
        System.out.println("-------");
        reorderTiles();
        for (int i = 0; i < DEFAULT_ROW_COL_NUMS; i++) {
            for (int j = 0; j < DEFAULT_ROW_COL_NUMS; j++) {
                drawRect1.setBounds(offsetX + i * MapLayer.MAP_TILE_WIDTH+deltaX,
                        offsetY + (j) * MapLayer.MAP_TILE_WIDTH+deltaY,
                        MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);

                int tile = tiles[i][j];
                if (tile > TILE_DOWNLOADING) {
                    System.out.print("(" + mapTileImages[tile].mapTileIndex.xIndex + ","
                            + mapTileImages[tile].mapTileIndex.yIndex + "),");
                } else {
                    System.out.print("(-1,-1),");
                }
                if (drawRect1.intersects(r))
                {
                    if (tile > TILE_DOWNLOADING) {
                        graphics.drawImage(mapTileImages[tile].mapImage, drawRect1.x, drawRect1.y);

                    } else {
                        graphics.drawImage(IMAGE_DOWNLOADING, drawRect1.x, drawRect1.y);
                    }
                }
            }
            System.out.println();

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

    private void updateMapCanvas() {
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

        int xIndex, yIndex;
        numOfMapRects = 0;
        //the centered rectangle
        centerRect.setRect(center.x - rasterMap.getScreenWidth() / 2.0,
                center.y - rasterMap.getScreenHeight() / 2.0,
                (double) rasterMap.getScreenWidth(), (double) rasterMap.getScreenHeight());
        drawRect.setRect(center.x - mapSize.width / 2.0,
                center.y - mapSize.height / 2.0,
                (double) mapSize.width, (double) mapSize.height);


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

                int mapType = rasterMap.getMapType();
                ImageTileIndex imageTileIndex = new ImageTileIndex();
                imageTileIndex.mapType = mapType;
                imageTileIndex.xIndex = xIndex;
                imageTileIndex.yIndex = yIndex;
                imageTileIndex.mapZoomLevel = mapZoomLevel;
                int tileIndex = getAvaiableMapTileIndex(imageTileIndex);
                if (tileIndex > TILE_DOWNLOADING) {
                    mapTileImages[tileIndex].updateMapCanvas();
                    if (mapDrawingListener != null) {
                        mapDrawingListener.done();
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
     * @inheritDoc
     */
    public void drawMapCanvas() {
        try {
            synchronized (syncObject) {
                screenOffsetX = (rasterMap.getMapWidth()
                        - rasterMap.getScreenWidth()) / 2;
                screenOffsetY = (rasterMap.getMapHeight()
                        - rasterMap.getScreenHeight()) / 2;
                updateMapCanvas();
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
     * Get updated map images.(the screen rect still within the map rect)
     */
    public void drawUpdatedMapCanvas() {
        try {
            System.out.println("drawUpdatedMapCanvas");
            synchronized (syncObject) {

                int xIndex, yIndex;
                for (int i = 0; i < needToUpdateMapIndexes.size(); i++) {
                    GeoPoint mapCanvasIndex =
                            (GeoPoint) needToUpdateMapIndexes.elementAt(i);

                    xIndex = (int) (mapCanvasIndex.getX() + 0.5);
                    yIndex = (int) (mapCanvasIndex.getY() + 0.5);
                    mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                            yIndex * MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);


                    int mapType = rasterMap.getMapType();
                    int mapZoomLevel = rasterMap.getZoom();

                    ImageTileIndex imageTileIndex = new ImageTileIndex();
                    imageTileIndex.mapType = mapType;
                    imageTileIndex.xIndex = xIndex;
                    imageTileIndex.yIndex = yIndex;
                    imageTileIndex.mapZoomLevel = mapZoomLevel;
                    int tileIndex = getAvaiableMapTileIndex(imageTileIndex);

                    if (tileIndex > TILE_DOWNLOADING) {
                        mapTileImages[tileIndex].updateMapCanvas();
                        if (mapDrawingListener != null) {
                            mapDrawingListener.done();
                        }
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   
    private final Hashtable reorderTilesQueue = new Hashtable();
    private GeoBounds centerRect;
    private int numOfMapRects = 0;
    private GeoBounds[] mapRects;
    private GeoBounds newCenterRect;
    private int numOfNewMapRects = 0;
    private GeoBounds[] newMapRects;
    private GeoBounds drawRect;
    private GeoBounds mapRect;
    private Vector needToUpdateMapIndexes = new Vector();
    private final int DEFAULT_ROW_COL_NUMS = 3;
    
    private final int rows = DEFAULT_ROW_COL_NUMS, cols = DEFAULT_ROW_COL_NUMS;
    // the matrix for storing the tiles
    private int[][] tiles;
    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUL2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * need to get a new map image.
     * @return
     */
    protected boolean needToGetNewMapImage() {
        int mapZoomLevel = rasterMap.getZoom();
        GeoPoint center = RasterMap.fromLatLngToPixel(rasterMap.getCenter(), mapZoomLevel);
        GeoPoint topLeft = new GeoPoint(center.x - rasterMap.getMapWidth() / 2.0,
                center.y - rasterMap.getMapHeight() / 2.0);

        GeoLatLng topLeftLatLng = RasterMap.fromPixelToLatLng(topLeft,
                mapZoomLevel);

        GeoPoint topLeftIndex;

        topLeftIndex = MapLayer.convertCoordindates2Tiles(topLeftLatLng.lat(),
                topLeftLatLng.lng(), mapZoomLevel);

        double realCenterX=((int)topLeftIndex.x)* MapLayer.MAP_TILE_WIDTH+mapSize.width/2;
        double realCenterY=((int)topLeftIndex.y)* MapLayer.MAP_TILE_WIDTH+mapSize.height/2;


        int deltaX=-(int)(center.x-realCenterX);
        int deltaY=-(int)(center.y-realCenterY);
        screenRectangle.setX(screenOffsetX-deltaX);
        screenRectangle.setY(screenOffsetY-deltaY);
        return !mapRectangle.contains(screenRectangle);
    }

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


            int xIndex, yIndex;
            numOfNewMapRects = 0;
            newCenterRect.setRect(center.x - rasterMap.getScreenWidth() / 2.0,
                    center.y - rasterMap.getScreenHeight() / 2.0,
                    (double) rasterMap.getScreenWidth(),
                    (double) rasterMap.getScreenHeight());


            topLeft.x = newCenterRect.x / MapLayer.MAP_TILE_WIDTH - 1;
            topLeft.y = newCenterRect.y / MapLayer.MAP_TILE_WIDTH - 1;

            bottomRight.x = (newCenterRect.x + newCenterRect.width) / MapLayer.MAP_TILE_WIDTH + 1;
            bottomRight.y = (newCenterRect.y + newCenterRect.height) / MapLayer.MAP_TILE_WIDTH + 1;
            int mapType = rasterMap.getMapType();


            for (xIndex = (int) topLeft.x;
                    xIndex <= bottomRight.x; xIndex++) {
                for (yIndex = (int) topLeft.y;
                        yIndex <= bottomRight.y; yIndex++) {
                    mapRect.setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                            yIndex * MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH,
                            MapLayer.MAP_TILE_WIDTH);
                    if (mapRect.intersects(newCenterRect.getX(),
                            newCenterRect.getY(),
                            newCenterRect.getWidth(),
                            newCenterRect.getHeight())) {
                        newMapRects[numOfNewMapRects].setRect(xIndex * MapLayer.MAP_TILE_WIDTH,
                                yIndex * MapLayer.MAP_TILE_WIDTH,
                                MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);
                        numOfNewMapRects++;
                    }
                }
            }
            needToUpdateMapIndexes.removeAllElements();

            for (int i = 0; i < numOfNewMapRects; i++) {
                xIndex = (int) (newMapRects[i].getX()
                        / MapLayer.MAP_TILE_WIDTH + 0.5);
                yIndex = (int) (newMapRects[i].getY()
                        / MapLayer.MAP_TILE_WIDTH + 0.5);


                ImageTileIndex imageTileIndex = new ImageTileIndex();
                imageTileIndex.mapType = mapType;
                imageTileIndex.xIndex = xIndex;
                imageTileIndex.yIndex = yIndex;
                imageTileIndex.mapZoomLevel = mapZoomLevel;
                int tileIndex = getAvaiableMapTileIndex(imageTileIndex);
                if (tileIndex > TILE_DOWNLOADING) {
                    if (mapTileImages[tileIndex].needToUpdate()) {
                        GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                        needToUpdateMapIndexes.addElement(mapCavasIndex);
                    }
                } else {
                    GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                    needToUpdateMapIndexes.addElement(mapCavasIndex);
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
        drawMapImage(graphics, offsetX, offsetY);
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

    
}
