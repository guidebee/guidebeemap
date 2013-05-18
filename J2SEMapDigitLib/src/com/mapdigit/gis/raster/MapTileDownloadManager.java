//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mapdigit.rms.RecordStore;
import com.mapdigit.rms.RecordStoreException;

import com.mapdigit.collections.Hashtable;

import com.mapdigit.drawing.Pen;
import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.drawing.IImage;
import com.mapdigit.util.MathEx;
import com.mapdigit.util.Log;


////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * map tile download manager.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 04/01/10
 * @author      Guidebee Pty Ltd.
 */
public final class MapTileDownloadManager implements Runnable {

    /**
     * The map tile not avaiable image.
     */
    public static IImage TILE_NOT_AVAIABLE = null;

    /**
     * the map tile is downloading image.
     */
    public static IImage TILE_DOWNLOADING = null;

    /**
     * blank tile image.
     */
    static IImage TILE_BLANK=null;

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Constructor.
     */
    public MapTileDownloadManager() {
        this(null,  null);
    }


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set reader listener
     * @param mapDownloadingListener download listener
     */
    public void setReaderListener(IReaderListener mapDownloadingListener){
    	this.mapDownloadingListener = mapDownloadingListener;
    	for (int i = 0; i < MAX_IMAGE_DOWNLOAD_WORKDER; i++) {
            if (imageDownloadWorkers[i] != null) {
                imageDownloadWorkers[i].mapTileReader.readListener=mapDownloadingListener;
            }
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param mapDownloadingListener donwload listener
     */
    public MapTileDownloadManager(IReaderListener mapDownloadingListener) {
        this(mapDownloadingListener,  null);
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param mapDownloadingListener donwload listener
     * @param mapTileReader the map tile downloader.
     */
    public MapTileDownloadManager(IReaderListener mapDownloadingListener,
            MapTileAbstractReader mapTileReader) {
        IS_CACHE_ON=MapConfiguration.isCacheOn;
        NOT_LOW_MEMORY_MODE=!MapConfiguration.lowMemoryMode;
        MAX_IMAGE_DOWNLOAD_WORKDER=MapConfiguration.workerThreadNumber;
        MAX_BYTES_IN_CACHE=MapConfiguration.mapCacheSizeInBytes;
        imageDownloadWorkers =
            new MapTileDownloadWorker[MAX_IMAGE_DOWNLOAD_WORKDER+1];
        this.mapDownloadingListener = mapDownloadingListener;
        synchronized (threadListMutex) {
            threadLists.clear();
            for (int i = 0; i < MAX_IMAGE_DOWNLOAD_WORKDER; i++) {
                if (mapTileReader != null) {
                    imageDownloadWorkers[i] =
                            new MapTileDownloadWorker(this, mapTileReader,
                            "MapTileDownloadWorker" + i);
                } else {
                    imageDownloadWorkers[i] = new MapTileDownloadWorker(this,
                            "MapTileDownloadWorker" + i);
                }
                threadLists.put("MapTileDownloadWorker" + i,
                        imageDownloadWorkers[i].mapTileDownloadWorkerThread);
            }
            //if it's not low memory mode, add one more download worker for
            //rendering map direction.
            if (NOT_LOW_MEMORY_MODE) {
                imageDownloadWorkers[MAX_IMAGE_DOWNLOAD_WORKDER] = new MapDirectionRendererWorker(this);
                threadLists.put("MapDirectionRendererWorker",
                        imageDownloadWorkers[MAX_IMAGE_DOWNLOAD_WORKDER].mapTileDownloadWorkerThread);
            }

            mapTileDownloadManagerThread = new Thread(this,"MapTileDownloadManager");
            //stream reader for stored map tiles.
            mapTileStreamReader=new MapTileStreamReader();
            mapTileStreamReader.setMapDownloadingListener(mapDownloadingListener);

       }
    }


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * start the manager thread and the worker threads.
     */
    public void start() {
        stopDownloadManager = false;
        int threadCount=MAX_IMAGE_DOWNLOAD_WORKDER;
        if (NOT_LOW_MEMORY_MODE) {
            threadCount=MAX_IMAGE_DOWNLOAD_WORKDER+1;
        }
        for (int i = 0; i < threadCount; i++) {
            imageDownloadWorkers[i].start();
        }
        //mapTileDownloadManagerThread.setPriority(Thread.MIN_PRIORITY);
        mapTileDownloadManagerThread.start();
        if (rasterMap.usePanThread) {
            if (this.mapPanThread == null) {
                mapPanThread = rasterMap.getNewPandirectionThread();
            }
            mapPanThread.start();
        }
        rasterMap.mapTileEngine.start();
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * restart the worker thread in case worker thread is died.
     */
    public void restartWorker() {
        System.gc(); System.gc();
        synchronized (threadListMutex) {
           threadLists.clear();
            for (int i = 0; i < MAX_IMAGE_DOWNLOAD_WORKDER; i++) {
                if (imageDownloadWorkers[i] != null) {
                    imageDownloadWorkers[i].stop();
                    imageDownloadWorkers[i] = null;
                }
                imageDownloadWorkers[i] = new MapTileDownloadWorker(this,
                        "MapTileDownloadWorker" + i);
                imageDownloadWorkers[i].start();
                threadLists.put("MapTileDownloadWorker" + i,
                        imageDownloadWorkers[i].mapTileDownloadWorkerThread);
            }
            if (NOT_LOW_MEMORY_MODE) {
                imageDownloadWorkers[MAX_IMAGE_DOWNLOAD_WORKDER] = new MapDirectionRendererWorker(this);
                threadLists.put("MapDirectionRendererWorker",
                        imageDownloadWorkers[MAX_IMAGE_DOWNLOAD_WORKDER].mapTileDownloadWorkerThread);
            }
        }
    }


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get internal stream reader.
     * @return the internal stream reader.
     */
    public MapTileStreamReader getInteralMapTileStreamReader(){
        return mapTileStreamReader;
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the manager thread and all the worker threads.
     */
    public void close(){
        try {
            stop();
            mapTileStreamReader.close();
         } catch (Exception e) {
            Log.p("close" + e.getMessage());
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Stop the manager thread and all the worker threads.
     */
    public void stop() {
        stopDownloadManager = true;
        synchronized (assignedMapDirectionRenderListMutex){
            assignedMapDirectionRenderListMutex.notifyAll();
        }
        synchronized (syncObjectManager){
            syncObjectManager.notifyAll();
        }
        int threadCount=MAX_IMAGE_DOWNLOAD_WORKDER;
        if (NOT_LOW_MEMORY_MODE) {
            threadCount=MAX_IMAGE_DOWNLOAD_WORKDER+1;
        }
        for (int i = 0; i < threadCount; i++) {
            if (imageDownloadWorkers[i] != null) {
                imageDownloadWorkers[i].stop();
            }
        }
        if (rasterMap.usePanThread) {
            if (mapPanThread != null) {
                mapPanThread.stopThread();
                mapPanThread = null;
            }
        }
        rasterMap.mapTileEngine.stop();
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return all the worker threads.
     * @return the hashtable contains all the worker threads.
     */
    public java.util.Hashtable getThreads() {
        synchronized (threadListMutex) {
            return threadLists;
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * the running method of this manager thread.
     */
    public void run() {
        Log.p( Thread.currentThread().getName()+" thread started");
        while (!stopDownloadManager) {
            int size = imageTileDownloadList.size();
            if (size > 0) {
                int threadCount = MAX_IMAGE_DOWNLOAD_WORKDER;
                if (NOT_LOW_MEMORY_MODE) {
                    threadCount = MAX_IMAGE_DOWNLOAD_WORKDER + 1;
                }
                for (int i = 0; i < threadCount; i++) {
                   if (imageDownloadWorkers[i].isPaused()) {
                        imageDownloadWorkers[i].resume();
                    }
                }
            }
            synchronized (syncObjectManager) {
                try {
                    syncObjectManager.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }


        }
        Log.p( Thread.currentThread().getName()+" thread stopped");
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear map cache;
     */
    void clearMapCache(){
        if (IS_CACHE_ON) {
            synchronized (imageCache) {
                imageCache.clear();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Save map image cache to persistent memory.
     */
    void saveMapCache() {
        if (IS_CACHE_ON) {
            try {
                RecordStore.deleteRecordStore(MAP_DATA_RECORDSTORE_NAME);
            } catch (RecordStoreException ingore) {
                ingore.printStackTrace();
            }
            try {

                synchronized (imageCache) {
                    mapDataRecordStore = RecordStore.openRecordStore(MAP_DATA_RECORDSTORE_NAME, true);
                    int numberOfRecord = Math.min(MAX_MAP_TILES_NUMBERS, imageCache.size());
                    for (int i = numberOfRecord - 1; i >= 0; i--) {
                        String key = (String) imageCache.keyAt(i);

                        byte[] imageArray = (byte[]) imageCache.get(key);
                        if (imageArray != null) {
                            byte[] recordDate = image2ByteArray(key, imageArray);
                            mapDataRecordStore.addRecord(recordDate, 0, recordDate.length);
                        }

                    }
                }
                mapDataRecordStore.closeRecordStore();
                mapDataRecordStore = null;

            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Restore map image cache from persistent memory.
     */
    void restoreMapCache() {
        if (IS_CACHE_ON) {
            try {
                synchronized (imageCache) {
                    imageCache.clear();
                    if (mapDataRecordStore == null) {
                        mapDataRecordStore = RecordStore.openRecordStore(MAP_DATA_RECORDSTORE_NAME, false);
                        int numOfRecords = mapDataRecordStore.getNumRecords();
                        numOfRecords = Math.min(numOfRecords, MAX_MAP_TILES_NUMBERS);
                        for (int i = 0; i < numOfRecords; i++) {
                            byte[] recordDate = mapDataRecordStore.getRecord(i + 1);
                            addOneImageToCacheFromRecordStore(recordDate);

                        }
                        mapDataRecordStore.closeRecordStore();
                        mapDataRecordStore = null;
                    }
                }

            } catch (RecordStoreException ignore) {
                ignore.printStackTrace();
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the route direction.
     * @return get the first map direction.
     */
    MapDirection getMapDirection() {
        return mapDirectionRenderer.getMapDirection();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the route direction.
     * @return get the map direction array.
     */
    MapDirection[] getMapDirections() {
        return mapDirectionRenderer.getMapDirections();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the route direction.
     * @param newDirection first map direction.
     */
    void setMapDirection(MapDirection newDirection) {
        mapDirectionRenderer.setMapDirection(newDirection);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the route directions.
     * @param newDirections ,map direction array.
     */
    void setMapDirections(MapDirection[] newDirections) {
        mapDirectionRenderer.setMapDirections(newDirections);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set route pen
     * @param routePen
     */
    void setRoutePen(Pen routePen){
        mapDirectionRenderer.setRoutePen(routePen);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JUN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear the task list
     */
    void clearTaskList(){
        synchronized (assignedImageTileDownloadListMutex){
            assignedImageTileDownloadList.clear();
        }
        synchronized(assignedMapDirectionRenderListMutex){
            assignedMapDirectionRenderList.clear();
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get a map tile image in cache.
     * @param mtype the map type of the map tile.
     * @param x the x index of the map tile.
     * @param y the y index of the map tile.
     * @param zoomLevel the zoom level of the map tile.
     * @return the image at give location.
     */
    IImage getCachedImage(int mtype, int x, int y, int zoomLevel) {
        int maxTile = (int) (MathEx.pow(2, zoomLevel) + 0.5);
        x=x % maxTile; y=y % maxTile;
        String key = mtype + "|" +
                x + "|" +
                y + "|" +
                zoomLevel;
        IImage image = null;
        lastestZoomLevel = zoomLevel;
        final byte[] imageArray;
        synchronized (imageCache) {
            imageArray = (byte[]) imageCache.get(key);
        }
       if (imageArray == null) {
            image = TILE_DOWNLOADING;
        } else {
            image = DigitalMap.getAbstractGraphicsFactory().
                    createImage(imageArray, 0, imageArray.length);
        }

        return image;
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get a map tile image.
     * @param mtype the map type of the map tile.
     * @param x the x index of the map tile.
     * @param y the y index of the map tile.
     * @param zoomLevel the zoom level of the map tile.
     * @return the image at give location.
     */
    IImage getImage(int mtype, int x, int y, int zoomLevel) {
        if(mtype==MapType.ROUTING_DIRECTION && !NOT_LOW_MEMORY_MODE) return null;
        int maxTile = (int) (MathEx.pow(2, zoomLevel) + 0.5);
        x=x % maxTile; y=y % maxTile;
        String key = mtype + "|" +
                x + "|" +
                y + "|" +
                zoomLevel;
        IImage image = null;
        lastestZoomLevel = zoomLevel;
        byte[] imageArray;
        synchronized (imageCache) {
            imageArray = (byte[]) imageCache.get(key);
        }

        if (imageArray == null) {
            //Add check for empty url
            String url = MapType.getTileURL(mtype, x, y, MapType.NUMZOOMLEVELS- zoomLevel);
            if (url.startsWith(MapType.EMPTY_TILE_URL)) {
                image=TILE_NOT_AVAIABLE;
            } else {
                final ImageTileIndex needToDownloadImageTileIndex = new ImageTileIndex();
                needToDownloadImageTileIndex.mapType = mtype;
                needToDownloadImageTileIndex.xIndex = x;
                needToDownloadImageTileIndex.yIndex = y;
                needToDownloadImageTileIndex.mapZoomLevel = zoomLevel;
                addToImageDownloadList(needToDownloadImageTileIndex);
                image = TILE_DOWNLOADING;
            }
        } else {
            image = DigitalMap.getAbstractGraphicsFactory().createImage(imageArray, 0, imageArray.length);
        }
        return image;
    }

    //The follow are configuration variables
    /**
     *  the maximum number of map tile to be downloaded in the queue
     */
    private final static int MAX_DOWNLOAD_MAP_TILE = 256;
    /**
     * cache size.
     */
    private final static int MAX_MAP_TILES_NUMBERS = 256;


    /**
     * max sizes in the image cache.
     */
    private final long MAX_BYTES_IN_CACHE;

    /**
     * maximum image download worker thread size
     */
    private final int MAX_IMAGE_DOWNLOAD_WORKDER;

    /**
     * is cache on or not.
     */
    private final boolean IS_CACHE_ON;

    /**
     * is it low momory mode or not.
     */
    private final boolean NOT_LOW_MEMORY_MODE;

    /**
     * worker thread array ,the extra one is mapdirectionrendererworker
     */
    private final MapTileDownloadWorker[] imageDownloadWorkers;
    /**
     * This image cache stores map tiles downloaded
     */
    private final Hashtable imageCache = new Hashtable(MAX_MAP_TILES_NUMBERS + 1);
    /**
     * the download map tile list.
     */
    private final Hashtable imageTileDownloadList = new Hashtable(MAX_DOWNLOAD_MAP_TILE);


    private final Object threadListMutex = new Object();
    private IReaderListener mapDownloadingListener = null;
    IMapTileReadyListener mapTileReadyListener = null;
    RasterMap.PandirectionThread mapPanThread=null;
    RasterMap rasterMap=null;

    private final java.util.Hashtable threadLists = new java.util.Hashtable();

    private final Hashtable assignedImageTileDownloadList = new Hashtable(MAX_DOWNLOAD_MAP_TILE);
    private final Object assignedImageTileDownloadListMutex = new Object();

    private final Hashtable assignedMapDirectionRenderList=new Hashtable(MAX_DOWNLOAD_MAP_TILE);
    private final Object assignedMapDirectionRenderListMutex = new Object();


    private volatile boolean stopDownloadManager = false;
    private Thread mapTileDownloadManagerThread = null;
    private volatile int lastestZoomLevel=-1;

    /**
     * route direction renderer.
     */
    private final MapDirectionRenderer mapDirectionRenderer=new MapDirectionRenderer();

    /**
     * record store
     */
    private static RecordStore mapDataRecordStore = null;
    /**
     * record store name
     */
    private static final String MAP_DATA_RECORDSTORE_NAME = "Guidebee_MapData";
    /**
     * thread sync object.
     */
    private final Object syncObjectManager = new Object();

    /**
     * stream reader used to read map tiles from SD card
     */
    private final MapTileStreamReader mapTileStreamReader;

    /**
     * the following code is for Java SE and Android Version.
     */
    private static byte[] ImageDownloadingArray = new byte[]{
         (byte)0x89,  (byte)0x50,  (byte)0x4e,  (byte)0x47,  (byte)0x0d,  (byte)0x0a,  (byte)0x1a,  (byte)0x0a,
         (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x0d,  (byte)0x49,  (byte)0x48,  (byte)0x44,  (byte)0x52,
         (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x40,  (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x40,
         (byte)0x04,  (byte)0x03,  (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x58,  (byte)0x47,  (byte)0x6c,
         (byte)0xed,  (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x30,  (byte)0x50,  (byte)0x4c,  (byte)0x54,
         (byte)0x45,  (byte)0xab,  (byte)0xa6,  (byte)0x9c,  (byte)0xaf,  (byte)0xaa,  (byte)0xa2,  (byte)0xb6,
         (byte)0xb2,  (byte)0xaa,  (byte)0xc1,  (byte)0xbc,  (byte)0xb6,  (byte)0xc7,  (byte)0xc3,  (byte)0xbb,
         (byte)0xcd,  (byte)0xcb,  (byte)0xc5,  (byte)0xd5,  (byte)0xd1,  (byte)0xcc,  (byte)0xdc,  (byte)0xda,
         (byte)0xd5,  (byte)0xe1,  (byte)0xdf,  (byte)0xdc,  (byte)0xe7,  (byte)0xe4,  (byte)0xdf,  (byte)0xeb,
         (byte)0xea,  (byte)0xe7,  (byte)0xf0,  (byte)0xed,  (byte)0xe8,  (byte)0xf0,  (byte)0xef,  (byte)0xee,
         (byte)0xf5,  (byte)0xf5,  (byte)0xf3,  (byte)0xfa,  (byte)0xfa,  (byte)0xf9,  (byte)0xff,  (byte)0xff,
         (byte)0xff,  (byte)0xfd,  (byte)0xa4,  (byte)0xa8,  (byte)0x3f,  (byte)0x00,  (byte)0x00,  (byte)0x00,
         (byte)0xc9,  (byte)0x49,  (byte)0x44,  (byte)0x41,  (byte)0x54,  (byte)0x48,  (byte)0xc7,  (byte)0x63,
         (byte)0xf8,  (byte)0xff,  (byte)0x7f,  (byte)0xf7,  (byte)0x6e,  (byte)0x7c,  (byte)0x98,  (byte)0x81,
         (byte)0x0e,  (byte)0x0a,  (byte)0xf0,  (byte)0x4b,  (byte)0xff,  (byte)0xff,  (byte)0x4f,  (byte)0x0f,
         (byte)0x05,  (byte)0xa3,  (byte)0xe1,  (byte)0x30,  (byte)0x1a,  (byte)0x0e,  (byte)0x38,  (byte)0xc3,
         (byte)0xe1,  (byte)0x5f,  (byte)0x12,  (byte)0x83,  (byte)0x50,  (byte)0x2c,  (byte)0x3e,  (byte)0x5f,
         (byte)0x1c,  (byte)0x62,  (byte)0x60,  (byte)0x30,  (byte)0x33,  (byte)0xca,  (byte)0xc6,  (byte)0xad,
         (byte)0xe0,  (byte)0x8f,  (byte)0x22,  (byte)0x83,  (byte)0xe5,  (byte)0xff,  (byte)0x7f,  (byte)0xee,
         (byte)0xb8,  (byte)0x15,  (byte)0x3c,  (byte)0x12,  (byte)0x64,  (byte)0x01,  (byte)0xd2,  (byte)0x3b,
         (byte)0x71,  (byte)0x87,  (byte)0x83,  (byte)0x03,  (byte)0x83,  (byte)0x3e,  (byte)0x3e,  (byte)0x5f,
         (byte)0x7c,  (byte)0x5f,  (byte)0x2b,  (byte)0xc0,  (byte)0xd0,  (byte)0x0f,  (byte)0xa2,  (byte)0xcf,
         (byte)0xe3,  (byte)0x50,  (byte)0xf0,  (byte)0x55,  (byte)0x90,  (byte)0x81,  (byte)0x61,  (byte)0xd6,
         (byte)0xff,  (byte)0xdd,  (byte)0xbb,  (byte)0x02,  (byte)0xed,  (byte)0x71,  (byte)0x28,  (byte)0xf8,
         (byte)0xa7,  (byte)0xc8,  (byte)0xc0,  (byte)0x20,  (byte)0xd8,  (byte)0xbf,  (byte)0x7b,  (byte)0x92,
         (byte)0xa0,  (byte)0x18,  (byte)0xae,  (byte)0x70,  (byte)0x28,  (byte)0x10,  (byte)0x14,  (byte)0x14,
         (byte)0xdf,  (byte)0xfd,  (byte)0x0b,  (byte)0xa8,  (byte)0x6c,  (byte)0x3d,  (byte)0x0e,  (byte)0x5f,
         (byte)0x6c,  (byte)0x15,  (byte)0x14,  (byte)0x9e,  (byte)0xbd,  (byte)0xbb,  (byte)0x11,  (byte)0x68,
         (byte)0x91,  (byte)0x1e,  (byte)0x2e,  (byte)0x6f,  (byte)0x1a,  (byte)0xc6,  (byte)0xed,  (byte)0xde,
         (byte)0xfd,  (byte)0x6b,  (byte)0x22,  (byte)0x03,  (byte)0x83,  (byte)0x28,  (byte)0x2e,  (byte)0x05,
         (byte)0xc7,  (byte)0x41,  (byte)0x74,  (byte)0x23,  (byte)0x83,  (byte)0xe4,  (byte)0x72,  (byte)0xbc,
         (byte)0xe9,  (byte)0x41,  (byte)0x41,  (byte)0x30,  (byte)0x1f,  (byte)0x67,  (byte)0x6c,  (byte)0x82,
         (byte)0xc2,  (byte)0x70,  (byte)0x0b,  (byte)0x83,  (byte)0xd0,  (byte)0x7b,  (byte)0x9c,  (byte)0x0a,
         (byte)0xb6,  (byte)0x7b,  (byte)0xfd,  (byte)0xff,  (byte)0x6b,  (byte)0xc0,  (byte)0xa0,  (byte)0x83,
         (byte)0x27,  (byte)0x3d,  (byte)0x24,  (byte)0x9a,  (byte)0x19,  (byte)0x32,  (byte)0x30,  (byte)0x9d,
         (byte)0xc7,  (byte)0x93,  (byte)0x1e,  (byte)0xfe,  (byte)0x3a,  (byte)0x32,  (byte)0x08,  (byte)0xd7,
         (byte)0x8d,  (byte)0xe6,  (byte)0x8b,  (byte)0xd1,  (byte)0xf2,  (byte)0x61,  (byte)0xc4,  (byte)0x87,
         (byte)0x03,  (byte)0x00,  (byte)0x95,  (byte)0x74,  (byte)0xb0,  (byte)0xed,  (byte)0x65,  (byte)0x48,
         (byte)0x6d,  (byte)0x06,  (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x49,  (byte)0x45,
         (byte)0x4e,  (byte)0x44,  (byte)0xae,  (byte)0x42,  (byte)0x60,  (byte)0x82};




        private static byte[] ImageNoavaiableArray = new byte[]{
            (byte)0x89,(byte)0x50,(byte)0x4E,(byte)0x47,(byte)0x0D,(byte)0x0A,(byte)0x1A,
            (byte)0x0A,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x0D,(byte)0x49,(byte)0x48,(byte)0x44,
            (byte)0x52,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,
            (byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x6B,(byte)0xAC,
            (byte)0x58,(byte)0x54,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0x67,(byte)0x41,
            (byte)0x4D,(byte)0x41,(byte)0x00,(byte)0x00,(byte)0xB1,(byte)0x8E,(byte)0x7C,(byte)0xFB,
            (byte)0x51,(byte)0x93,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x20,(byte)0x63,(byte)0x48,
            (byte)0x52,(byte)0x4D,(byte)0x00,(byte)0x00,(byte)0x7A,(byte)0x25,(byte)0x00,(byte)0x00,
            (byte)0x80,(byte)0x83,(byte)0x00,(byte)0x00,(byte)0xF9,(byte)0xFF,(byte)0x00,(byte)0x00,
            (byte)0x80,(byte)0xE6,(byte)0x00,(byte)0x00,(byte)0x75,(byte)0x2E,(byte)0x00,(byte)0x00,
            (byte)0xEA,(byte)0x5F,(byte)0x00,(byte)0x00,(byte)0x3A,(byte)0x97,(byte)0x00,(byte)0x00,
            (byte)0x17,(byte)0x6F,(byte)0x69,(byte)0xE4,(byte)0xC4,(byte)0x2B,(byte)0x00,(byte)0x00,
            (byte)0x03,(byte)0x00,(byte)0x50,(byte)0x4C,(byte)0x54,(byte)0x45,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x48,(byte)0xCA,
            (byte)0xB7,(byte)0x07,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x74,(byte)0x52,
            (byte)0x4E,(byte)0x53,(byte)0xFF,(byte)0xFF,(byte)0x00,(byte)0xD7,(byte)0xCA,(byte)0x0D,
            (byte)0x41,(byte)0x00,(byte)0x00,(byte)0x05,(byte)0xAC,(byte)0x49,(byte)0x44,(byte)0x41,
            (byte)0x54,(byte)0x78,(byte)0x9C,(byte)0x62,(byte)0x60,(byte)0x1A,(byte)0xE1,(byte)0x00,
            (byte)0x20,(byte)0x80,(byte)0x18,(byte)0x06,(byte)0xDA,(byte)0x01,(byte)0x03,(byte)0x0D,
            (byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,
            (byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,
            (byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,
            (byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,
            (byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,
            (byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,
            (byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,
            (byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,
            (byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,
            (byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,
            (byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,
            (byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,
            (byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,
            (byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,
            (byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,
            (byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,
            (byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,
            (byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,
            (byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,
            (byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,
            (byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,
            (byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,
            (byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,
            (byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,
            (byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,
            (byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,
            (byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,
            (byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,
            (byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,
            (byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,
            (byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,
            (byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,
            (byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,
            (byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,
            (byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,
            (byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,
            (byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,
            (byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,
            (byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,
            (byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,
            (byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,
            (byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,
            (byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,
            (byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,
            (byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,
            (byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,
            (byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,
            (byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,
            (byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,
            (byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,
            (byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,
            (byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,
            (byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,
            (byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,
            (byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,
            (byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,
            (byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,
            (byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,
            (byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,
            (byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,
            (byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,
            (byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,
            (byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,
            (byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,
            (byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,
            (byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,
            (byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,
            (byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,
            (byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,
            (byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,
            (byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,
            (byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,
            (byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,
            (byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,
            (byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,
            (byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,
            (byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,
            (byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,
            (byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,
            (byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,
            (byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,
            (byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,
            (byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,
            (byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,
            (byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,
            (byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,
            (byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,
            (byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,
            (byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,
            (byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,
            (byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,
            (byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,
            (byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,
            (byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,
            (byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,
            (byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,
            (byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,
            (byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,
            (byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,
            (byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,
            (byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,
            (byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,
            (byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,
            (byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,
            (byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,
            (byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,
            (byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,
            (byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,
            (byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,
            (byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,
            (byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,
            (byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,
            (byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,
            (byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,
            (byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,
            (byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,
            (byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,
            (byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,
            (byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,
            (byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,
            (byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,
            (byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,
            (byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,
            (byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,
            (byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,
            (byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,
            (byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,
            (byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,
            (byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,
            (byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,
            (byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,
            (byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,
            (byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,
            (byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,
            (byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,
            (byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,
            (byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,
            (byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,
            (byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,
            (byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,
            (byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,
            (byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,
            (byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,
            (byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,
            (byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,
            (byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,
            (byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,
            (byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,
            (byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,
            (byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,
            (byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,
            (byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,
            (byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,
            (byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,
            (byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,
            (byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,
            (byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,
            (byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,
            (byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,
            (byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,
            (byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,
            (byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,
            (byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,
            (byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,
            (byte)0x00,(byte)0x10,(byte)0x40,(byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,
            (byte)0x68,(byte)0xC4,(byte)0x07,(byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,
            (byte)0x00,(byte)0x00,(byte)0x08,(byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,
            (byte)0x01,(byte)0x34,(byte)0xE2,(byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,
            (byte)0x7C,(byte)0x00,(byte)0x00,(byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,
            (byte)0x80,(byte)0x00,(byte)0x1A,(byte)0xF1,(byte)0x01,(byte)0x00,(byte)0x10,(byte)0x40,
            (byte)0x23,(byte)0x3E,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x68,(byte)0xC4,(byte)0x07,
            (byte)0x00,(byte)0x40,(byte)0x00,(byte)0x8D,(byte)0xF8,(byte)0x00,(byte)0x00,(byte)0x08,
            (byte)0xA0,(byte)0x11,(byte)0x1F,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x34,(byte)0xE2,
            (byte)0x03,(byte)0x00,(byte)0x20,(byte)0x80,(byte)0x46,(byte)0x7C,(byte)0x00,(byte)0x00,
            (byte)0x04,(byte)0xD0,(byte)0x88,(byte)0x0F,(byte)0x00,(byte)0x80,(byte)0x00,(byte)0x03,
            (byte)0x00,(byte)0x10,(byte)0xF0,(byte)0x00,(byte)0x1F,(byte)0xBE,(byte)0x86,(byte)0x97,
            (byte)0x97,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x49,(byte)0x45,(byte)0x4E,
            (byte)0x44,(byte)0xAE,(byte)0x42,(byte)0x60,(byte)0x82};

    /**
     * Intialized the images.
     */
   static {
        try {
            TILE_NOT_AVAIABLE = DigitalMap.getAbstractGraphicsFactory()
                    .createImage(ImageNoavaiableArray,0,ImageNoavaiableArray.length);
            TILE_DOWNLOADING = DigitalMap.getAbstractGraphicsFactory()
                    .createImage(ImageDownloadingArray,0,ImageDownloadingArray.length);
            TILE_BLANK= DigitalMap.getAbstractGraphicsFactory()
            .createImage(ImageDownloadingArray,0,ImageDownloadingArray.length);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


//    /**
//     * Intialized the images.
//     */
//   static {
//        try {
//            TILE_NOT_AVAIABLE = DigitalMap.getAbstractGraphicsFactory()
//                    .createImage(new Object().getClass().getResourceAsStream("/tile-na.png"));
//            TILE_DOWNLOADING = DigitalMap.getAbstractGraphicsFactory()
//                    .createImage(new Object().getClass().getResourceAsStream("/downloading.png"));
//            TILE_BLANK= DigitalMap.getAbstractGraphicsFactory()
//                    .createImage(new Object().getClass().getResourceAsStream("/downloading.png"));
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add one map tile index to the assigned download list.
     * @param imageTileIndex
     */
    private void addToAssignedImageDownloadList(ImageTileIndex imageTileIndex) {
        synchronized (assignedImageTileDownloadListMutex) {
            String key = imageTileIndex.mapType + "|" +
                    imageTileIndex.xIndex + "|" +
                    imageTileIndex.yIndex + "|" +
                    imageTileIndex.mapZoomLevel;
            assignedImageTileDownloadList.put(key, imageTileIndex);
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add one map tile index to the download list.
     * @param imageTileIndex
     */
    private void addToImageDownloadList(ImageTileIndex imageTileIndex) {
        synchronized (assignedImageTileDownloadListMutex) {
            String key = imageTileIndex.mapType + "|" +
                    imageTileIndex.xIndex + "|" +
                    imageTileIndex.yIndex + "|" +
                    imageTileIndex.mapZoomLevel;
            Object object = imageCache.get(key);
            Object object1 = imageTileDownloadList.get(key);
            Object object2 = assignedImageTileDownloadList.get(key);

            if (object == null && object1 == null && object2 == null) {

                ImageTileIndex newImagetileIndex = new ImageTileIndex();
                newImagetileIndex.mapType = imageTileIndex.mapType;
                newImagetileIndex.xIndex = imageTileIndex.xIndex;
                newImagetileIndex.yIndex = imageTileIndex.yIndex;
                newImagetileIndex.mapZoomLevel = imageTileIndex.mapZoomLevel;

                imageTileDownloadList.put(key, newImagetileIndex);
                synchronized (syncObjectManager) {
                    syncObjectManager.notify();
                }
            }
        }
    }

    private void oneDownloadImageTileDone(String key) {
        synchronized (assignedImageTileDownloadListMutex) {
            assignedImageTileDownloadList.remove(key);
        }
    }

    private ImageTileIndex getAImageTileIndex() {
        synchronized (assignedImageTileDownloadListMutex) {
            if (imageTileDownloadList.size() > 0) {
                Object key = imageTileDownloadList.
                        keyAt(imageTileDownloadList.size() - 1);
                ImageTileIndex imageTileIndex =
                        (ImageTileIndex) imageTileDownloadList.get(key);
                imageTileDownloadList.remove(key);
                return imageTileIndex;
            } else {
                return null;
            }

        }
    }

    private void addToImageCache(String key, byte[] imageArray) {
        if (IS_CACHE_ON) {
            long bytesInCache = 0;
            for (int i = 0; i < imageCache.size(); i++) {
                byte[] array = (byte[]) imageCache.elementAt(i);
                bytesInCache += array.length;

            }
            if (bytesInCache > MAX_BYTES_IN_CACHE) {
                imageCache.removeHalfElements();
            }
            imageCache.put(key, imageArray);
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Save map image cache to persistent memory.
     */
    private void addOneImageToCacheFromRecordStore(byte[] imageArray) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageArray);
        DataInputStream dis = new DataInputStream(bais);
        String key = null;
        byte[] image;
        try {
            key = dis.readUTF();
            int imageSize = dis.readInt();
            image = new byte[imageSize];
            dis.read(image);

            addToImageCache(key, image);

        } catch (Exception ingore) {
            ingore.printStackTrace();
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * each record has the format of [Key,imageLength,imagedata]
     */
    private byte[] image2ByteArray(String key, byte[] image) {
        byte[] imageArray = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(key);
            dos.writeInt(image.length);
            dos.write(image);
            imageArray = baos.toByteArray();
            dos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageArray;
    }

    private class MapDirectionRendererWorker extends MapTileDownloadWorker{
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Constructor.
         * @param manager the downloade manager instance.
         * @param threadName the thread name.
         */
        private MapDirectionRendererWorker(MapTileDownloadManager manager){
            super(manager,mapDirectionRenderer,"MapDirectionRendererWorker");
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * get one map tile.
         * @param imageTileIndex the index of given map tile.
         */
        private void getImage(ImageTileIndex imageTileIndex) {
            try {
                String key = imageTileIndex.mapType + "|" +
                        imageTileIndex.xIndex + "|" +
                        imageTileIndex.yIndex + "|" +
                        imageTileIndex.mapZoomLevel;
                IImage image = MapTileDownloadManager.TILE_DOWNLOADING;
                if(mapTileReader instanceof MapDirectionRenderer){
                    MapDirectionRenderer mapDirectionRenderer=(MapDirectionRenderer)mapTileReader;
                     //this is a block methods,it returns when the download is done.
                    if (imageTileIndex.mapZoomLevel == lastestZoomLevel) {
                        image = mapDirectionRenderer.getImage(
                                imageTileIndex.xIndex, imageTileIndex.yIndex,
                                imageTileIndex.mapZoomLevel);
                    }
                    if(image==null){
                        image = MapTileDownloadManager.TILE_DOWNLOADING;
                    }
                    mapTileDownloadManager.oneDownloadImageTileDone(key);
                    mapTileDownloadManager.mapTileReadyListener.done(imageTileIndex, image);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * the actually thread run.
         */
        public void run() {
            Log.p( Thread.currentThread().getName()+" thread started");
            while (!stopDownloadWorker) {
                try {

                    synchronized (assignedMapDirectionRenderListMutex) {
                        if (assignedMapDirectionRenderList.size() > 0) {
                            Object key = assignedMapDirectionRenderList.keyAt(assignedMapDirectionRenderList.size() - 1);
                            ImageTileIndex imageTileIndex =
                                    (ImageTileIndex) assignedMapDirectionRenderList.get(key);
                            assignedMapDirectionRenderList.remove(key);
                            getImage(imageTileIndex);

                        } else {
                            {
                                try {
                                    pauseDownloadWorker = true;
                                    assignedMapDirectionRenderListMutex.wait(MAX_WAITING_TIME * 1000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    //catch whatever exception to make sure the thread is not dead.
                    e.printStackTrace();
                }

            }
            Log.p( Thread.currentThread().getName()+" thread stopped");
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * map download work thread.
     * <p>
     * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
     * @version     2.00, 04/01/10
     * @author      Guidebee Pty Ltd.
     */
    private class MapTileDownloadWorker implements Runnable {

        /**
         * the map tile downloader actually do the download work.
         */
        protected MapTileAbstractReader mapTileReader=null;
        /**
         * Download manager object.
         */
        protected MapTileDownloadManager mapTileDownloadManager = null;
        protected volatile boolean stopDownloadWorker = false;
        protected volatile boolean pauseDownloadWorker = false;
        protected final Thread mapTileDownloadWorkerThread;
        protected final String threadName;
        protected final Object syncObjectWorker = new Object();
        /**
         * max wait time for download an image in seconds.
         */
        protected int MAX_WAITING_TIME = 120;

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Constructor.
         * @param manager the downloade manager instance.
         * @param threadName the thread name.
         */
        private MapTileDownloadWorker(MapTileDownloadManager manager, String threadName) {
            mapTileDownloadManager = manager;
            this.threadName = threadName;
            mapTileDownloadWorkerThread = new Thread(this,threadName);
            mapTileReader = new MapTileDownloader();
            mapTileReader.setMapDownloadingListener(manager.mapDownloadingListener);
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Constructor.
         * @param manager the downloade manager instance.
         * @param threadName the thread name.
         */
        private MapTileDownloadWorker(MapTileDownloadManager manager,
                MapTileAbstractReader mapTileReader, String threadName) {
            mapTileDownloadManager = manager;
            this.threadName = threadName;
            mapTileDownloadWorkerThread = new Thread(this,threadName);
            this.mapTileReader=mapTileReader;
            mapTileReader.setMapDownloadingListener(manager.mapDownloadingListener);
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * start the worker thread.
         */
        public void start() {
            stopDownloadWorker = false;
            mapTileDownloadWorkerThread.setPriority(Thread.MIN_PRIORITY);
            mapTileDownloadWorkerThread.start();
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * get one map tile.
         * @param imageTileIndex the index of given map tile.
         */
        private void getImage(ImageTileIndex imageTileIndex) {
            try {
                String key = imageTileIndex.mapType + "|" +
                        imageTileIndex.xIndex + "|" +
                        imageTileIndex.yIndex + "|" +
                        imageTileIndex.mapZoomLevel;
                byte[] imageArray = null;
                //commented, dont use TILE NOT AVAIABLE AGAIN
                //IImage image = MapTileDownloadManager.TILE_NOT_AVAIABLE;
                IImage image = MapTileDownloadManager.TILE_DOWNLOADING;
                //this is a block methods,it returns when the download is done.
                boolean isImageValid=false;
                synchronized (mapTileStreamReader) {
                    if (imageTileIndex.mapType != MapType.ROUTING_DIRECTION &&
                            imageTileIndex.mapType!=MapType.MICROSOFTSATELLITE
                            && imageTileIndex.mapType!=MapType.MICROSOFTHYBRID) {
                        mapTileStreamReader.getImage(imageTileIndex.mapType,
                                imageTileIndex.xIndex, imageTileIndex.yIndex,
                                imageTileIndex.mapZoomLevel);
                        imageArray = mapTileStreamReader.imageArray;
                        if(imageArray!=null){
                            isImageValid=true;
                        }

                    }
                }

                if (imageArray == null) {
                    mapTileReader.getImage(imageTileIndex.mapType,
                            imageTileIndex.xIndex, imageTileIndex.yIndex,
                            imageTileIndex.mapZoomLevel);
                    //if the downloading is successful
                    if (mapTileReader.imageArraySize > 0 && mapTileReader.isImagevalid) {
                        imageArray = mapTileReader.imageArray;
                        isImageValid=true;
                    }else{
                       imageArray=null;
                       isImageValid=false;
                    }
                }

                if (imageArray != null) {
                    try {
                        image = DigitalMap.getAbstractGraphicsFactory().createImage(imageArray, 0, imageArray.length);
                    } catch (Exception e) {
                        //the createImage could fail.
                        e.printStackTrace();
                        mapTileReader.isImagevalid = false;
                        image = MapTileDownloadManager.TILE_DOWNLOADING;

                    }
                    if (isImageValid) {
                        mapTileDownloadManager.addToImageCache(key, imageArray);
                    }
                }


                mapTileDownloadManager.oneDownloadImageTileDone(key);
                mapTileDownloadManager.mapTileReadyListener.done(imageTileIndex, image);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *  stop the thread.
         */
        public void stop() {
            stopDownloadWorker = true;
            synchronized (syncObjectWorker) {
                syncObjectWorker.notifyAll();
            }
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * the actually thread run.
         */
        public void run() {
            Log.p( Thread.currentThread().getName()+" thread started");
            while (!stopDownloadWorker) {
                try {
                    //get one map tile index from dowload manager's download list.
                    ImageTileIndex imageTileIndex = mapTileDownloadManager
                            .getAImageTileIndex();
                    if (imageTileIndex != null) {
                        pauseDownloadWorker = false;
                        mapTileDownloadManager.
                                addToAssignedImageDownloadList(imageTileIndex);
                        if(imageTileIndex.mapType==MapType.ROUTING_DIRECTION){
                            //if it's to render the map direction, just assign
                            //it to  mapDirectionRenderer.
                            synchronized (assignedMapDirectionRenderListMutex) {
                                String key = imageTileIndex.mapType + "|" +
                                        imageTileIndex.xIndex + "|" +
                                        imageTileIndex.yIndex + "|" +
                                        imageTileIndex.mapZoomLevel;
                                if (!assignedMapDirectionRenderList.containsKey(key)) {
                                    ImageTileIndex newImagetileIndex = new ImageTileIndex();
                                    newImagetileIndex.mapType = imageTileIndex.mapType;
                                    newImagetileIndex.xIndex = imageTileIndex.xIndex;
                                    newImagetileIndex.yIndex = imageTileIndex.yIndex;
                                    newImagetileIndex.mapZoomLevel = imageTileIndex.mapZoomLevel;
                                    assignedMapDirectionRenderList.put(key, newImagetileIndex);
                                    assignedMapDirectionRenderListMutex.notify();
                                }

                            }
                        }else{
                            getImage(imageTileIndex);
                        }
                    } else {
                        synchronized (syncObjectWorker) {
                            try {
                                pauseDownloadWorker = true;
                                syncObjectWorker.wait(MAX_WAITING_TIME * 1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                    }
                } catch (Exception e) {
                    //catch whatever exception to make sure the thread is not dead.
                    e.printStackTrace();
                }

            }
            Log.p( Thread.currentThread().getName()+" thread stopped");
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * resume the thread.
         */
        public void resume() {
            synchronized (syncObjectWorker) {
                pauseDownloadWorker = false;
                syncObjectWorker.notify();
            }
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * check if the thread is paused or not
         * @return if ture then the thread is paused.
         */
        public boolean isPaused() {
            if (mapTileDownloadWorkerThread != null) {
                return pauseDownloadWorker;
            }
            return false;
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * set the worker thread pause status.
         * @param value the pause status.
         */
        public void setPaused(boolean value) {
            pauseDownloadWorker = value;
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 04JAN2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *  check if the thread is alive.
         * @return
         */
        public boolean isAlive() {
            if (mapTileDownloadWorkerThread != null) {
                return mapTileDownloadWorkerThread.isAlive();
            }
            return false;
        }
    }
}


