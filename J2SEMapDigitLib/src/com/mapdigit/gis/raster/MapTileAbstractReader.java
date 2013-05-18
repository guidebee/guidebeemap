//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Base class for all map tile downloader/reader/render.
 * <p></p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class MapTileAbstractReader {

    /**
     * This image array stores map tiles downloaded
     */
    public volatile byte[] imageArray = null;

    /**
     * the actual image size in the image array
     */
    public volatile int imageArraySize = 0;

    /**
     * indicates the data in the image array is valid or not.
     */
    public volatile boolean isImagevalid = false;
    
    /**
     * total bytes downloaded with this downloader
     */
    public static volatile long totaldownloadedBytes = 0;

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set downloader listener
     * @param listener
     */
    public void setMapDownloadingListener(IReaderListener listener) {
        this.readListener = listener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get image at given location. when the reader is done, imageArray shall
     * store the image byte array. imageArraySize is the actually data size.
     * isImagevalid indicate the data is valid or not. normally this shall be
     * an async call.
     * @param mtype the map type of the map tile.
     * @param x the x index of the map tile.
     * @param y the y index of the map tile.
     * @param zoomLevel the zoom level of the map tile.
     */
    public abstract void getImage(int mtype, int x, int y, int zoomLevel);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * a way app can cancel the reading process.
     */
    public void cancelRead() {
    }


    /**
     * downloader listener
     */
    protected IReaderListener readListener = null;


}
