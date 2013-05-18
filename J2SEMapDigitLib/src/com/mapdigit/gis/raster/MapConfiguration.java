//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.drawing.Brush;
import com.mapdigit.drawing.Color;
import com.mapdigit.drawing.Pen;

//////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * map configuration.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public class MapConfiguration {

    /**
     * is cache on or not.
     */
    public final static int IS_CACHE_ON=1;

    /**
     * the no of worker thread.
     */
    public final static int WORKER_THREAD_NUMBER=8;

    /**
     * the map cache size.
     */
    public final static int MAP_CACHE_SIZE_IN_BYTES=256*1024;

    /**
     * the direction render size = 256/MAP_DIRECTION_RENDER_BLOCKS;
     */
    private final static int MAP_DIRECTION_RENDER_BLOCKS=5;

    /**
     * draw route or not.
     */
    public final static int LOW_MEMORY_MODE=6;

    /**
     * route start icon.
     */
    public final static int ROUTE_START_ICON=7;

    /**
     * route middle icon.
     */
    public final static int ROUTE_MIDDLE_ICON=8;

    /**
     * route end icon.
     */
    public final static int ROUTE_END_ICON=9;

    /**
     * route draw pen.
     */
    public final static int ROUTE_DRAW_PEN=10;

    /**
     * is mark supported or not.
     */
    public final static int IS_MARK_SUPPORTED=11;

    /**
     * ignore the map type in stored map or not.
     */
    public final static int IGNORE_MAP_TYPE_FOR_STORED_MAP=12;



    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set map configuration parameter.
     * @param field
     * @param value
     */
    public static void setParameter(int field,boolean value){
        switch(field){
            case IS_CACHE_ON:
                isCacheOn=value;
                break;
            case LOW_MEMORY_MODE:
                lowMemoryMode=value;
                break;
            case IS_MARK_SUPPORTED:
                isMarkSupported=value;
                break;
            case IGNORE_MAP_TYPE_FOR_STORED_MAP:
                ignoreMapTypeInStoredMap=value;
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set map configuration parameter.
     * @param field
     * @param value
     */
    public static void setParameter(int field,int value){
        switch(field){
            case WORKER_THREAD_NUMBER:
                if (value < 0 || value > 64) {
                    throw new IllegalArgumentException("Thread no should between 1 and 64");
                }
                workerThreadNumber = value;
                break;
            case MAP_CACHE_SIZE_IN_BYTES:
                if (value < 0 && isCacheOn) {
                    throw new IllegalArgumentException("Cache size shall be great than 0");
                }
                mapCacheSizeInBytes = value * 1024;
                break;
            case MAP_DIRECTION_RENDER_BLOCKS:
                if (!(value == 1 || value == 2 ||
                        value == 4)) {
                    throw new IllegalArgumentException("block size should be 1, or 2, or 4");
                }
                mapDirectionRenderBlocks = 1;
                break;
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set configuration
     * @param field name of the configuration item
     * @param value value for the configuration item.
     */
    public static void setParameter(int field,Object value){
        switch(field){
            case ROUTE_START_ICON:
                if(value instanceof Brush){
                    startIconBrush=(Brush)value;
                }
                break;
            case ROUTE_MIDDLE_ICON:
                if(value instanceof Brush){
                    middleIconBrush=(Brush)value;
                }
                break;
           case ROUTE_END_ICON:
                if(value instanceof Brush){
                    endIconBrush=(Brush)value;
                }
                break;
            case ROUTE_DRAW_PEN:
                if(value instanceof Pen){
                    routePen=(Pen)value;
                }
                break;


        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set configuraiton.
     * @param pen
     * @param start
     * @param middle
     * @param end
     */
    public static void setParameters(Pen pen,Brush start,
            Brush middle,Brush end){
        if (pen != null) {
            routePen = pen;
        }
        if (start != null) {
            startIconBrush = start;
        }
        if (middle != null) {
            middleIconBrush = middle;
        }
        if (end != null) {
            endIconBrush = end;
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Reset map configuration parameters. the resetting parameters should before
     * the initialization of DigitalMap ,and MapTileDownloaderManager.
     * @param cacheOn if cache is on, Digital Map will appy an internal cache
     * which can save some loaded map tile to speed up map rendering, but it'll
     * consume some memory whose max size is speicfied by cachesize.
     * @param workerThreadNo how many worker thread,default is 4, these threads
     * are in charge of downloading/reading/render map tiles from server,stored
     * map tile file or vector map file.the thread no should between 1 and 8.
     * @param cacheSize the max size of internal map tile caches.
     * @param directionRenderBlocks when render direction, it uses an internal
     *  vector picture engine to draw the polyline, which requires memory ,the
     * memory size is determined by the block size, the default render picture
     * size is 256X256 ,which takes 256X256X4 bytes(256K), for memory constraints
     * device, speicify a small block size requires smaller memory useage. but
     * it effects the render performace,the valid value for directionRenderBlocks
     * is 1,2,4, whose corrosponing block size is 256X256X4 bytes(256K)(default)
     * 128X128X4 bytes(64K) and 64X64X4 bytes(16K).
     */
    public static void setParameters(boolean cacheOn,
            int workerThreadNo,
            long cacheSize,
            boolean isLowMemory,
            int directionRenderBlocks){
        isCacheOn=cacheOn;
        lowMemoryMode=isLowMemory;
        if(workerThreadNo<0 || workerThreadNo>8){
            throw new IllegalArgumentException("Thread no should between 1 and 8");
        }
        workerThreadNumber=workerThreadNo;
        if(cacheSize<0 && cacheOn){
            throw new IllegalArgumentException("Cache size shall be great than 0");
        }
        mapCacheSizeInBytes=cacheSize;
        if(!(directionRenderBlocks==1 || directionRenderBlocks==2 ||
                directionRenderBlocks==4)){
            throw new IllegalArgumentException("block size should be 1, or 2, or 4");
        }
        mapDirectionRenderBlocks=directionRenderBlocks;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private constructor.
     */
    private MapConfiguration(){

    }

    /**
     * is mark supported not.
     */
    static boolean isMarkSupported=false;

    /**
     * is cache on not.
     */
    static boolean isCacheOn=true;

    /**
     * the no of worker thread.
     */
    static int workerThreadNumber=4;

    /**
     * the map cache size.
     */
    static long mapCacheSizeInBytes=256*1024;

    /**
     * the direction render size = 256/MAP_DIRECTION_RENDER_BLOCKS;
     * this will always be 1 for new version. if low memory ,it should
     * disable the drawRouting, and use native graphics to draw routes.
     */
    static int mapDirectionRenderBlocks=1;

    /**
     * is low memory mode,
     */
    static boolean  lowMemoryMode=false;

    /**
     * default route drawing pen.
     */
    static Pen routePen=new Pen(new Color(0x7F00FF00, false), 4);

    /**
     * start route icon.
     */
    static Brush startIconBrush=null;

    /**
     * start route icon.
     */
    static Brush middleIconBrush=null;


    /**
     * start route icon.
     */
    static Brush endIconBrush=null;

    /**
     * ignore map type in stored map or not.
     */
    static boolean ignoreMapTypeInStoredMap=true;

}
