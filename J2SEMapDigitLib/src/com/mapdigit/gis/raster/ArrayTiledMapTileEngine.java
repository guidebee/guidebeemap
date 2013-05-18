//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.drawing.geometry.Rectangle;
import com.mapdigit.util.Log;
import com.mapdigit.util.MathEx;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Array tiled Map Tile Engine.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/09/10
 * @author      Guidebee Pty Ltd.
 */
final class ArrayTiledMapTileEngine extends TiledMapTileEngine {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param width width of the map.
     * @param height height of the map.
     * @param mapTileDownloadManager  map tile downloader manager
     * @param rasterMap raster map instance.
     */
    public ArrayTiledMapTileEngine(int width, int height,
            MapTileDownloadManager mapTileDownloadManager, RasterMap rasterMap) {
        super(width, height, mapTileDownloadManager, rasterMap);
        if (mapTileDownloadManager != null) {
            this.mapTileDownloadManager = mapTileDownloadManager;
            this.mapTileDownloadManager.mapPanThread = null;
            this.mapTileDownloadManager.mapTileReadyListener = mapTileReadyListener;
            this.mapTileDownloadManager.rasterMap = rasterMap;
        }
        viewRect.x = viewRect.y = 0;
        countDownTimer=new DelayUpdateCanvasTimer(5,updateCanvasTask);
        viewRect.setSize(screenRectangle.width, screenRectangle.height);
        mapRect.setSize(MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set screen size.
     * @param width
     * @param height
     */
    public void setScreenSize(int width, int height) {
        super.setScreenSize(width, height);
        viewRect.setSize(screenRectangle.width, screenRectangle.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * pan direction.
     * @param dx
     * @param dy
     */
    public void panDirection(final int dx, final int dy) {
        viewRect.x -= dx;
        viewRect.y -= dy;
        countDownTimer.reset();
        super.panDirection(dx, dy);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void paint(IGraphics graphics) {
        paint(graphics, 0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void paint(IGraphics graphics, int offsetX, int offsetY) {
        paintInternal(graphics, offsetX, offsetY);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void drawMapCanvas() {
        Log.p("drawMapCanvas");
        //clearAllMapTiles();
        int mapZoomLevel = rasterMap.getZoom();
        GeoPoint center = RasterMap.fromLatLngToPixel(rasterMap.getCenter(),
                mapZoomLevel);
        viewRect.x = (int) center.x - screenRectangle.width / 2;
        viewRect.y = (int) center.y - screenRectangle.height / 2;
        screenOffsetX = (rasterMap.getMapWidth()
                - rasterMap.getScreenWidth()) / 2;
        screenOffsetY = (rasterMap.getMapHeight()
                - rasterMap.getScreenHeight()) / 2;
        if (mapDrawingListener != null) {
            mapDrawingListener.done();
        }
        forceUpdateMapTiles(false);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void drawUpdatedMapCanvas() {
        Log.p("drawUpdatedMapCanvas");
        countDownTimer.reset();
        if (mapDrawingListener != null) {
            mapDrawingListener.done();
        }
    }

    /**
     * safe guard check period
     */
    private final int SAFE_GUARD_PERIOD=10000;
    /**
     * view rectangle.
     */
    private final Rectangle  viewRect = new Rectangle();
    /**
     * temp variable avoid create the rectangle again and again.
     */
    private Rectangle mapRect = new Rectangle();

    private final DelayUpdateCanvasTimer countDownTimer;

    private volatile boolean stopThread = false;

    private final Object safeGuardWakeupObject = new Object();

    private int normalizedValue(int value,int max){
        int ret=value % max;
        if(ret<0) ret+=max;
        return ret;
    }

    private void forceUpdateMapTiles(boolean forceUpdate) {
        Log.p("forceUpdateMapTiles");
        //synchronized(viewRect)
        {
            Rectangle  drawRect =new Rectangle(viewRect);
            GeoPoint topLeft = new GeoPoint();
            GeoPoint bottomRight = new GeoPoint();
            int mapType = rasterMap.getMapType();
            int mapZoomLevel = rasterMap.getZoom();
            topLeft.x = drawRect.x / MapLayer.MAP_TILE_WIDTH;
            topLeft.y = drawRect.y / MapLayer.MAP_TILE_WIDTH ;
            bottomRight.x = (drawRect.x + drawRect.width)
                    / MapLayer.MAP_TILE_WIDTH ;
            bottomRight.y = (drawRect.y + drawRect.height)
                    / MapLayer.MAP_TILE_WIDTH ;
            int X1 = (int) topLeft.x;
            int Y1 = (int) topLeft.y;
            int X2 = (int) bottomRight.x;
            int Y2 = (int) bottomRight.y;
            int maxTile = (int) (MathEx.pow(2, mapZoomLevel) + 0.5);
            if(X1*MapLayer.MAP_TILE_WIDTH>drawRect.x) X1-=1;
            if(Y1*MapLayer.MAP_TILE_WIDTH>drawRect.y) Y1-=1;
            if(X2*MapLayer.MAP_TILE_WIDTH<drawRect.x+drawRect.width) X2+=1;
            if(Y2*MapLayer.MAP_TILE_WIDTH<drawRect.y+drawRect.height) Y2+=1;
            boolean needUpdate = false;
            for (int xIndex = X1; xIndex < X2; xIndex++) {
                for (int yIndex = Y1; yIndex < Y2; yIndex++) {
                    int x =normalizedValue(xIndex,maxTile);
                    int y = normalizedValue(yIndex,maxTile);
                    mapRect.x = x * MapLayer.MAP_TILE_WIDTH;
                    mapRect.y = y * MapLayer.MAP_TILE_WIDTH;
                    ImageTileIndex imageTileIndex = new ImageTileIndex();
                    imageTileIndex.mapType = mapType;
                    imageTileIndex.xIndex = x;
                    imageTileIndex.yIndex = y;
                    imageTileIndex.mapZoomLevel = mapZoomLevel;
                    if (mapRect.intersects(drawRect) && x >= 0 && y >= 0) {
                        final int tileIndex = getAvaiableMapTileIndex(imageTileIndex);
                        if (tileIndex > TILE_DOWNLOADING) {
                            if (mapTileImages[tileIndex].needToUpdate()) {
                                mapTileImages[tileIndex].updateMapCanvas();
                                needUpdate = true;
                            }
                        }
                    }
                }
            }
            if (needUpdate || forceUpdate) {
                if (mapDrawingListener != null) {
                    mapDrawingListener.done();
                }
            }
        }
    }

    private Runnable updateCanvasTask = new Runnable() {

        public void run() {
            forceUpdateMapTiles(false);
        }
    };

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw the map.
     * @param graphics graphics object.
     * @param offsetX  offset x
     * @param offsetY  offset y
     */
    private void drawMapImage(IGraphics graphics, final int offsetX,
            final int offsetY) {

        GeoPoint topLeft = new GeoPoint();
        GeoPoint bottomRight = new GeoPoint();
        int mapType = rasterMap.getMapType();
        int mapZoomLevel = rasterMap.getZoom();
        topLeft.x = viewRect.x / MapLayer.MAP_TILE_WIDTH;
        topLeft.y = viewRect.y / MapLayer.MAP_TILE_WIDTH;
        bottomRight.x = (viewRect.x + viewRect.width)
                / MapLayer.MAP_TILE_WIDTH;
        bottomRight.y = (viewRect.y + viewRect.height)
                / MapLayer.MAP_TILE_WIDTH;
        int X1 = (int) topLeft.x;
        int Y1 = (int) topLeft.y;
        int X2 = (int) bottomRight.x;
        int Y2 = (int) bottomRight.y;
        int maxTile = (int) (MathEx.pow(2, mapZoomLevel) + 0.5);
        if(X1*MapLayer.MAP_TILE_WIDTH>viewRect.x) X1-=1;
        if(Y1*MapLayer.MAP_TILE_WIDTH>viewRect.y) Y1-=1;
        if(X2*MapLayer.MAP_TILE_WIDTH<viewRect.x+viewRect.width) X2+=1;
        if(Y2*MapLayer.MAP_TILE_WIDTH<viewRect.y+viewRect.height) Y2+=1;
        if(X1<0 || Y1<0 || X2>maxTile || Y2>maxTile){
        	clearBackground(graphics,0XFFFFFFFF);
        }
        for (int xIndex = X1; xIndex < X2; xIndex++) {
            for (int yIndex = Y1; yIndex < Y2; yIndex++) {
                int x =normalizedValue(xIndex,maxTile);
                int y = normalizedValue(yIndex,maxTile);
                mapRect.x = x * MapLayer.MAP_TILE_WIDTH + offsetX;
                mapRect.y = y * MapLayer.MAP_TILE_WIDTH + offsetY;
                ImageTileIndex imageTileIndex = new ImageTileIndex();
                imageTileIndex.mapType = mapType;
                imageTileIndex.xIndex = x;
                imageTileIndex.yIndex = y;
                imageTileIndex.mapZoomLevel = mapZoomLevel;
                if (mapRect.intersects(viewRect)) {
                    final int tileIndex = getAvaiableMapTileIndex(imageTileIndex);
                    if (tileIndex > TILE_DOWNLOADING) {
                        graphics.drawImage(mapTileImages[tileIndex].mapImage,
                                mapRect.x - viewRect.x,
                                mapRect.y - viewRect.y);
                    }else{
                       graphics.drawImage(IMAGE_DOWNLOADING,
                                mapRect.x - viewRect.x,
                                mapRect.y - viewRect.y);
                    }
                }
            }
        }

    }

    private void clearBackground(IGraphics graphics,int color) {
        graphics.setColor(color);
        graphics.setClip(0, 0, rasterMap.getScreenWidth(),
                rasterMap.getScreenHeight());
        graphics.fillRect(0, 0, rasterMap.getScreenWidth(),
                rasterMap.getScreenHeight());
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * paint internal.
     * @param graphics graphics object.
     * @param offsetX  offset x
     * @param offsetY  offset y
     */
    private void paintInternal(IGraphics graphics, int offsetX, int offsetY) {
        //clearBackground(graphics,0xFFFFFFFF);
        drawMapImage(graphics, offsetX, offsetY);
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
        (new SafeguardThread()).start();
        countDownTimer.start();
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
    void stop() {
        countDownTimer.stop();
        synchronized (safeGuardWakeupObject) {
            safeGuardWakeupObject.notifyAll();
        }
    }

    class SafeguardThread extends Thread{

        SafeguardThread(){
            super("Safeguard");
        }

        public void run() {
            Log.p(Thread.currentThread().getName() + " thread started");
            while (!stopThread) {
                synchronized (safeGuardWakeupObject) {
                    try {
                        safeGuardWakeupObject.wait(SAFE_GUARD_PERIOD);//10 seconds
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    forceUpdateMapTiles(true);
                }
            }
            Log.p(Thread.currentThread().getName() + " thread stopped");
        }
    }

    class DelayUpdateCanvasTimer implements Runnable {

        private int period;
        private Runnable task;
        private Thread thread;
        private final Object wakeupObject = new Object();
        private volatile int counter = 0;


        public DelayUpdateCanvasTimer(int period, Runnable runner) {
            this.period = period;
            this.task = runner;
        }

        public void reset() {
            synchronized (wakeupObject) {
                wakeupObject.notifyAll();
            }
            counter = 0;
        }

        public void stop() {
            stopThread = true;
            reset();
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void start() {
            stopThread=false;
            thread = new Thread(this, "DelayUpdateCanvasTimer");
            thread.start();

        }

        public void run() {
            Log.p(Thread.currentThread().getName() + " thread started");
            while (!stopThread) {
                synchronized (wakeupObject) {
                    try {
                        wakeupObject.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                if (task != null && !stopThread) {
                    while (counter < period) {
                        counter++;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                    }
                    try {
                        task.run();
                    } catch (Exception e) {
                    }
                }
            }
            Log.p(Thread.currentThread().getName() + " thread stopped");
        }
    }
}
