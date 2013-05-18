//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Hashtable;
import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Base tiled Map Tile Engine.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/09/10
 * @author      Guidebee Pty Ltd.
 */
abstract class TiledMapTileEngine extends MapTileEngine {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param width  the width of the map.
     * @param height the height of the map.
     * @param mapTileDownloadManager map tile download manager
     * @param rasterMap raster map instance.
     */
    public TiledMapTileEngine(int width, int height,
            MapTileDownloadManager mapTileDownloadManager, RasterMap rasterMap) {
        super(width, height, mapTileDownloadManager, rasterMap);
        //create a dowloading image.
        IImage imageDownloading = MapTileDownloadManager.TILE_DOWNLOADING;
        IMAGE_DOWNLOADING = RasterMap.getAbstractGraphicsFactory()
                .createImage(MapLayer.MAP_TILE_WIDTH,
                MapLayer.MAP_TILE_WIDTH);
        IGraphics graphics = IMAGE_DOWNLOADING.getGraphics();
        if (imageDownloading != null) {
            int xIndex, yIndex;
            int tileWidth = imageDownloading.getHeight();
            int count = MapLayer.MAP_TILE_WIDTH / tileWidth;
            for (xIndex = 0; xIndex < count; xIndex++) {
                for (yIndex = 0;
                        yIndex <= count;
                        yIndex++) {
                    graphics.drawImage(imageDownloading,
                            xIndex * tileWidth, yIndex * tileWidth);
                }
            }
        }
        int howManyPhysicalTiles = (width / MapLayer.MAP_TILE_WIDTH)
                * (height / MapLayer.MAP_TILE_WIDTH);
        if (howManyPhysicalTiles < 4) {
            howManyPhysicalTiles = 4;
        }
        mapTileImages = new MapTile[howManyPhysicalTiles];
        for (int i = 0; i < howManyPhysicalTiles; i++) {
            mapTileImages[i] = new MapTile();
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear map cache.
     */
    public void clearMapCache() {
        synchronized (syncObject) {
            mapTileDownloadManager.clearMapCache();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clear map (routes etc).
     */
    public void clearMapDirection() {
        synchronized (syncObject) {
            mapTileDownloadManager.setMapDirection(null);
            rasterMap.setCenter(rasterMap.getScreenCenter(), rasterMap.getZoom());
            System.gc(); System.gc();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
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
    // 03SEP2010  James Shen                 	          Code review
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
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set routing direction.
     * @param newDirection new routing direction.
     */
    public void setMapDirections(MapDirection[] newDirections) {
        synchronized (syncObject) {
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
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set routing direction.
     * @param newDirection new routing direction.
     */
    public void setMapDirection(MapDirection newDirection) {
        synchronized (syncObject) {
            mapTileDownloadManager.setMapDirection(newDirection);
            if (newDirection != null) {
            } else {
                clearMapDirection();
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
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

    /**
     * the downloding image.
     */
    protected final IImage IMAGE_DOWNLOADING;
    /**
     * tile download image index.
     */
    protected final int TILE_DOWNLOADING = -1;

    /**
     * map tile array.
     */
    protected final MapTile[] mapTileImages;

    /**
     * update one map tile handler
     */
    protected final IMapTileReadyListener mapTileReadyListener
            = new IMapTileReadyListener() {

        public void done(ImageTileIndex imageTileIndex, IImage image) {
            try {
                int zoomLevel = rasterMap.getZoom();
                //ingnore the image not same with current map zoom Level.
                if (zoomLevel == imageTileIndex.mapZoomLevel) {
                    drawMapTileInMapCanvas(imageTileIndex, image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw map cavns with given tile image.
     * @param imageTileIndex index of the tile
     * @param image map image.
     */
    protected void drawMapTileInMapCanvas(ImageTileIndex imageTileIndex,
            IImage image) {
//        Log.p("drawMapTileInMapCanvas x="+imageTileIndex.xIndex+",y="
//                +imageTileIndex.yIndex+",z="+imageTileIndex.mapZoomLevel+",t="+
//                imageTileIndex.mapType);
        ImageTileIndex imageTileIndex1 = new ImageTileIndex();
                imageTileIndex1.mapType =rasterMap.getMapType();
                imageTileIndex1.xIndex = imageTileIndex.xIndex;
                imageTileIndex1.yIndex = imageTileIndex.yIndex;
                imageTileIndex1.mapZoomLevel = imageTileIndex.mapZoomLevel;
        int tileIndex = getAvaiableMapTileIndex(imageTileIndex1);
        if (tileIndex > TILE_DOWNLOADING) {
            mapTileImages[tileIndex].drawMapTileImage(imageTileIndex, image);
            if (mapDrawingListener != null) {
                mapDrawingListener.done();
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear all map tiles.
     */
    protected void clearAllMapTiles(){
        synchronized (syncObject) {
            //first check the array if already has the image.
            for (int i = 0; i < mapTileImages.length; i++) {
                mapTileImages[i].clearMapCanvas();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get avaiable map cavnas from array.
     * @param imageTileIndex index of the map tile.
     * @return index of the array.
     */
    protected int getAvaiableMapTileIndex(ImageTileIndex imageTileIndex) {
        synchronized (syncObject) {
            //first check the array if already has the image.
            for (int i = 0; i < mapTileImages.length; i++) {
                if (!mapTileImages[i].avaiable
                        && mapTileImages[i].mapTileIndex.mapType
                                == imageTileIndex.mapType
                        && mapTileImages[i].mapTileIndex.xIndex
                                == imageTileIndex.xIndex
                        && mapTileImages[i].mapTileIndex.yIndex
                                == imageTileIndex.yIndex
                        && mapTileImages[i].mapTileIndex.mapZoomLevel
                                == imageTileIndex.mapZoomLevel) {
                    return i;
                }
            }
            //second check if there's a free image.
            for (int i = 0; i < mapTileImages.length; i++) {
                if (mapTileImages[i].avaiable
                        || mapTileImages[i].mapTileIndex.mapZoomLevel
                        != imageTileIndex.mapZoomLevel) {
                    mapTileImages[i].setMapTileIndex(imageTileIndex);
                    return i;
                }
            }

            //calculate the max distance.
            int[] distances = new int[mapTileImages.length];
            for (int i = 0; i < mapTileImages.length; i++) {
                distances[i] = (mapTileImages[i].mapTileIndex.xIndex
                        - imageTileIndex.xIndex) *
                        (mapTileImages[i].mapTileIndex.xIndex
                        - imageTileIndex.xIndex)
                        + (mapTileImages[i].mapTileIndex.yIndex
                        - imageTileIndex.yIndex) *
                        (mapTileImages[i].mapTileIndex.yIndex
                        - imageTileIndex.yIndex);
            }
            int maxValue = distances[0];
            for (int i = 1; i < distances.length; i++) {
                if (maxValue < distances[i]) {
                    maxValue = distances[i];
                }
            }

            //now get the index which has the longest distance
            for (int i = 0; i < distances.length; i++) {
                if (maxValue == distances[i]) {
                    mapTileImages[i].setMapTileIndex(imageTileIndex);
                    return i;
                }
            }

            return TILE_DOWNLOADING;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * ask download mananger to download required map tile.
     * @param mtype
     * @param x
     * @param y
     * @param zoomLevel
     * @return
     */
    protected IImage getCachedImage(int mtype, int x, int y, int zoomLevel) {
        return mapTileDownloadManager.getCachedImage(mtype, x, y,
                zoomLevel);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * ask download mananger to download required map tile.
     * @param mtype
     * @param x
     * @param y
     * @param zoomLevel
     * @return
     */
    protected IImage getImage(int mtype, int x, int y, int zoomLevel) {
        return mapTileDownloadManager.getImage(mtype, x, y,
                zoomLevel);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  draw default downloading tile.
     * @param graphics
     * @param offsetX
     * @param offsetY
     */
    protected void drawTileDownloading(IGraphics graphics, int offsetX, int offsetY) {
        IImage imageDownloading = IMAGE_DOWNLOADING;
        if (imageDownloading != null) {
            graphics.setColor(0xFFFFFF);
            graphics.setClip(0, 0, rasterMap.getScreenWidth(),
                    rasterMap.getScreenHeight());
            graphics.fillRect(0, 0, rasterMap.getScreenWidth(),
                    rasterMap.getScreenHeight());
            int xIndex, yIndex;
            int tileWidth = MapLayer.MAP_TILE_WIDTH;
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


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Map tile class.
     * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
     * @version     2.00, 03/09/10
     * @author      Guidebee Pty Ltd.
     */
    class MapTile{

        /**
         * the map canvas
         */
        public final IImage mapImage;
        /**
         * Graphics Object for map canvas.
         */
        public final IGraphics mapGraphics;
        /**
         * mapt tile index.
         */
        public final ImageTileIndex mapTileIndex;
        /**
         * used or not.
         */
        public boolean avaiable = true;


        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 03SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Default constructor
         */
        public MapTile() {
            mapImage = RasterMap.getAbstractGraphicsFactory()
                    .createImage(MapLayer.MAP_TILE_WIDTH,
                    MapLayer.MAP_TILE_WIDTH);
            mapGraphics = mapImage.getGraphics();
            mapTileIndex = new ImageTileIndex();
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 03SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * check to see this map cavas need to update.
         * @return true ,need update.
         */
        public boolean needToUpdate() {
            boolean retValue = false;
            int mapType = rasterMap.getMapType();
            int mapZoomLevel = mapTileIndex.mapZoomLevel;
            int xIndex = mapTileIndex.xIndex;
            int yIndex = mapTileIndex.yIndex;
            int[] map_sequences1 =
                    (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
            int[] map_sequences;
            if (mapTileDownloadManager.getMapDirection() != null) {
                map_sequences = new int[map_sequences1.length + 1];
                System.arraycopy(map_sequences1, 0, map_sequences,
                        0, map_sequences1.length);
                map_sequences[map_sequences1.length] = MapType.ROUTING_DIRECTION;
            } else {
                map_sequences = map_sequences1;
                //check if there's routing image ,if it's ,then
                //the whole map canvas need to be clear
                String key = MapType.ROUTING_DIRECTION
                        + "|" + (xIndex) + "|"
                        + (yIndex) + "|" + mapZoomLevel;
                if (whatsInMapCanvas.containsKey(key)) {
                    clearMapCanvas();
                    return true;
                }
            }
            for (int mapSequenceIndex = 0;
                    mapSequenceIndex < map_sequences.length;
                    mapSequenceIndex++) {
                String key = map_sequences[mapSequenceIndex]
                        + "|" + (xIndex) + "|"
                        + (yIndex) + "|" + mapZoomLevel;
                if (!whatsInMapCanvas.containsKey(key)) {
                    retValue = true;
                    break;
                }
            }
            return retValue;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 03SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * set the map tile used for given map tile index.
         * @param imageTileIndex the index of the map tile.
         */
        public void setMapTileIndex(ImageTileIndex imageTileIndex) {
            avaiable = false;
            mapTileIndex.mapType = imageTileIndex.mapType;
            mapTileIndex.xIndex = imageTileIndex.xIndex;
            mapTileIndex.yIndex = imageTileIndex.yIndex;
            mapTileIndex.mapZoomLevel = imageTileIndex.mapZoomLevel;
            mapGraphics.drawImage(IMAGE_DOWNLOADING, 0, 0);
            whatsInMapCanvas.clear();
            pendingDrawImageQueue.clear();

        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 03SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * clear the map canvs with downloading image.
         */
        public void clearMapCanvas(){
            avaiable=true;
            mapGraphics.drawImage(IMAGE_DOWNLOADING, 0, 0);
            whatsInMapCanvas.clear();
            pendingDrawImageQueue.clear();
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 03SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * draw testing info.
         */
        public void drawTesting(){
            String strIndex = (mapTileIndex.xIndex) + "," + (mapTileIndex.yIndex);
            mapGraphics.setColor(0xFFFF0000);
            mapGraphics.drawString(strIndex, 10, 10);
            mapGraphics.drawRect(0, 0, 256, 256);
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 03SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Update map canvas, check to see if need to download missing images.
         */
        public void updateMapCanvas() {
            int mapType = rasterMap.getMapType();
            int mapZoomLevel = mapTileIndex.mapZoomLevel;
            int xIndex = mapTileIndex.xIndex;
            int yIndex = mapTileIndex.yIndex;
            int[] map_sequences1 =
                    (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
            int[] map_sequences;
            if (mapTileDownloadManager.getMapDirection() != null){
                map_sequences=new int[map_sequences1.length+1];
                System.arraycopy(map_sequences1, 0, map_sequences,
                        0, map_sequences1.length);
                map_sequences[map_sequences1.length]=MapType.ROUTING_DIRECTION;
            }else{
                map_sequences=map_sequences1;
            }
            for (int mapSequenceIndex = 0;
                    mapSequenceIndex < map_sequences.length;
                    mapSequenceIndex++) {
                IImage image = getCachedImage(
                        map_sequences[mapSequenceIndex],
                        xIndex,
                        yIndex,
                        mapZoomLevel);

                String key = map_sequences[mapSequenceIndex]
                        + "|" + (xIndex) + "|"
                        + (yIndex) + "|" + mapZoomLevel;
                boolean hasImage=false;

                //draw the image.
                if (image != null
                        && image
                        != MapTileDownloadManager.TILE_DOWNLOADING) {
                    mapGraphics.drawImage(image, 0, 0);
                    hasImage=true;

                } else {
                    image = (IImage) pendingDrawImageQueue.get(key);
                    if (image != null) {
                        mapGraphics.drawImage(image, 0, 0);
                        hasImage=true;

                    }
                }
                if (hasImage && image != MapTileDownloadManager.TILE_NOT_AVAIABLE) {
                    GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                    whatsInMapCanvas.put(key, mapCavasIndex);
                } else {
                    image = getImage(map_sequences[mapSequenceIndex],
                            xIndex, yIndex, mapZoomLevel);
                    if (image != null
                            && image
                            != MapTileDownloadManager.TILE_DOWNLOADING) {
                        mapGraphics.drawImage(image, 0, 0);
                        if (image != MapTileDownloadManager.TILE_NOT_AVAIABLE) {
                            GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                            whatsInMapCanvas.put(key, mapCavasIndex);
                        }

                    }
                }
            }
            //drawTesting();
        }


        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // --------   -------------------  -------------      ------------------
        // 02SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * drawn give map tile image on the map tile canvas.
         * @param imageTileIndex the index of the map tile.
         * @param image the image to be drawn.
         */
        public void drawMapTileImage(ImageTileIndex imageTileIndex,
                IImage image) {
            //check to see this image index is same as mine.
            if (!(imageTileIndex.xIndex == mapTileIndex.xIndex
                    && imageTileIndex.yIndex == mapTileIndex.yIndex
                    && imageTileIndex.mapZoomLevel == mapTileIndex.mapZoomLevel)){
                return;
            }

            //check to see if the image is null or download image.
            if (image == null || image
                    == MapTileDownloadManager.TILE_DOWNLOADING) {
                return;
            }

            String key = imageTileIndex.mapType
                            + "|" + (imageTileIndex.xIndex) + "|"
                            + (imageTileIndex.yIndex)
                            + "|" + imageTileIndex.mapZoomLevel;
            if(whatsInMapCanvas.containsKey(key)){
                return;
            }

            int mapType = rasterMap.getMapType();
            int mapZoomLevel = mapTileIndex.mapZoomLevel;
            int xIndex = mapTileIndex.xIndex;
            int yIndex = mapTileIndex.yIndex;
            int[] map_sequences1 =
                    (int[]) MapType.MAP_SEQUENCES.get(new Integer(mapType));
            int[] map_sequences;
            if (mapTileDownloadManager.getMapDirection() != null){
                map_sequences=new int[map_sequences1.length+1];
                System.arraycopy(map_sequences1, 0, map_sequences,
                        0, map_sequences1.length);
                map_sequences[map_sequences1.length]=MapType.ROUTING_DIRECTION;
            }else{
                map_sequences=map_sequences1;
            }
            //check to see need to draw this map tile
            int i = 0;
            for (i = 0; i < map_sequences.length; i++) {
                if (map_sequences[i] == imageTileIndex.mapType) {
                    break;
                }
            }
            if (i == map_sequences.length) {
                return;
            }
           //yes ,this image is for me.start drawing.
            //one map tile can be consisited with several layers. for example
            //a hybrid tile consists two layers. one for satellite ,one for notes.
            //and the direction layer is on top of other layers.
            try {
               //first draw map tiles below current map tile.
                //if there's map tile below current map tile havn't
                //be drawn, adding current map tile to pending queue.
                boolean needToAddToQueue = false;
                for (int mapSequenceIndex = 0;
                        mapSequenceIndex < map_sequences.length;
                        mapSequenceIndex++) {
                    if (imageTileIndex.mapType
                            == map_sequences[mapSequenceIndex]) {
                        break;
                    }
                    IImage backImage = getCachedImage(
                            map_sequences[mapSequenceIndex],
                            xIndex,
                            yIndex,
                            mapZoomLevel);

                    key = map_sequences[mapSequenceIndex]
                            + "|" + (xIndex) + "|" + (yIndex)
                            + "|" + mapZoomLevel;
                    if (!whatsInMapCanvas.containsKey(key)) {
                        needToAddToQueue = true;
                    }
                    if (backImage != null
                            && backImage
                            != MapTileDownloadManager.TILE_DOWNLOADING) {
                        mapGraphics.drawImage(backImage, 0, 0);

                    } else {
                        needToAddToQueue = true;
                    }
                }

                if (needToAddToQueue) {
                    key = imageTileIndex.mapType + "|"
                            + (xIndex) + "|" + (yIndex) + "|" + mapZoomLevel;
                    pendingDrawImageQueue.put(key, image);
                }

                //draw current map tile.
                mapGraphics.drawImage(image, 0, 0);

                //find next image sequence.
                int nextImageSequence = 0;
                for (int mapSequenceIndex = 0;
                        mapSequenceIndex < map_sequences.length;
                        mapSequenceIndex++) {
                    if (imageTileIndex.mapType == map_sequences[mapSequenceIndex]) {
                        nextImageSequence = mapSequenceIndex + 1;
                        break;
                    }
                }
                //draw next image sequences.
                for (int mapSequenceIndex = nextImageSequence;
                        mapSequenceIndex < map_sequences.length;
                        mapSequenceIndex++) {
                    key = map_sequences[mapSequenceIndex] + "|"
                            + (xIndex) + "|" + (yIndex) + "|"
                            + mapZoomLevel;

                    IImage pendImage = (IImage) pendingDrawImageQueue.get(key);
                    if (pendImage != null) {
                        mapGraphics.drawImage(pendImage, 0, 0);

                    }
                }
                //now add the drawn image index to the in canvas table.
                String inMapCanvsKey = imageTileIndex.mapType
                        + "|" + (xIndex) + "|" + (yIndex)
                        + "|" + imageTileIndex.mapZoomLevel;
                if (image != MapTileDownloadManager.TILE_NOT_AVAIABLE) {
                    GeoPoint mapCavasIndex = new GeoPoint(xIndex, yIndex);
                    whatsInMapCanvas.put(inMapCanvsKey,
                            mapCavasIndex);
                }

                //check to see to remove from pendingDrawImageQueue
                boolean needRemovePendingImage = true;
                for (int mapSequenceIndex = 0;
                        mapSequenceIndex < map_sequences.length;
                        mapSequenceIndex++) {

                     key = map_sequences[mapSequenceIndex] + "|"
                            + (xIndex) + "|" + (yIndex) + "|"
                            + mapZoomLevel;
                    if (!whatsInMapCanvas.containsKey(key)) {
                        needRemovePendingImage = false;
                        break;
                    }
                }

                if (needRemovePendingImage) {
                    pendingDrawImageQueue.clear();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //drawTesting();

        }

        /**
         * pending drawing image queue, used to order the routing and map images.
         */
        private final Hashtable pendingDrawImageQueue = new Hashtable();

        /**
         * record what have been drawn on the map tile canvas.
         */
        private final Hashtable whatsInMapCanvas = new Hashtable();

    }

}