//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import java.io.IOException;
import java.io.InputStream;
import com.mapdigit.network.Connector;
import com.mapdigit.network.HttpConnection;

import com.mapdigit.ajax.Arg;
import com.mapdigit.gis.geometry.GeoPoint;

import com.mapdigit.util.Log;
import com.mapdigit.util.MathEx;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapTileDownloader download map image tiles from server (msn,yahoo,etc).
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public class MapTileDownloader extends MapTileAbstractReader{

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a map downloader
     */
    public MapTileDownloader() {

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map tile image and stored in the map cache.
     * @param mtype the map tile (msn,yahoo etc).
     * @param x the x index of the map tile.
     * @param y the y index of the map tile.
     * @param zoomLevel   current zoom level
     */
    public void getImage(int mtype, int x, int y, int zoomLevel) {
        this.mapType = mtype;
        this.mapXIndex = x;
        this.mapYIndex = y;
        this.mapZoomLevel = zoomLevel;
        int imgResponseCode = -1;
        isImagevalid = true;
        imageArraySize = 0;
        final byte[] readBuffer = new byte[1024];
        try {
            String location = getTileURL(mtype, x, y, NUMZOOMLEVELS - zoomLevel);
            imgConn = (HttpConnection) Connector.open(location);
            //imgConn.setRequestProperty("Accept", "image/png");
            imgResponseCode = imgConn.getResponseCode();
        } catch (Exception iex) {
            iex.printStackTrace();
            isImagevalid = false;
        }

        if (imgResponseCode != HttpConnection.HTTP_OK) {
            Log.p("HTTP error downloading map image: "
                    + imgResponseCode,Log.WARNING);

            isImagevalid = false;
        }

        if (isImagevalid) {
            try {
                final int totalToReceive
                        = imgConn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
                totaldownloadedBytes += totalToReceive;
                final InputStream is =
                        new MapProgressInputStream(imgConn.openInputStream(),
                        totalToReceive, this.readListener, null, 1024);
                int totalToRead = Math.max(totalToReceive, is.available());
                imageArraySize = totalToRead;
                imageArray = new byte[totalToRead];
                int readLength = 0;
                while (readLength < totalToRead) {
                    int thisChunkLength = is.read(readBuffer);
                    System.arraycopy(readBuffer, 0, imageArray,
                            readLength, thisChunkLength);
                    readLength += thisChunkLength;
                }
                is.close();
            } catch (Exception iex) {
                isImagevalid = false;
                imageArray = null;
                System.gc(); System.gc();
                iex.printStackTrace();
            }
        }
        try {
            imgConn.close();
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        imgConn = null;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Cancel the download http connection.
     */
    public void cancelRead() {
        if (imgConn != null) {
            isImagevalid = false;
            try {
                imgConn.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the URL of map tile image.
     * @param mtype the map tile (msn,yahoo etc).
     * @param x the x index of the map tile.
     * @param y the y index of the map tile.
     * @param zoomLevel   current zoom level
     * @return the url of given map tile.
     */
    public String getTileURL(int mtype, int x, int y, int zoomLevel) {
        String url=MapType.getTileURL(mtype, x, y, zoomLevel);
        return url;
  }


    /**
     * total zoom levels
     */
    protected static final int NUMZOOMLEVELS = 17;
    /**
     * map type
     */
    protected int mapType;
    /**
     * X index of the map tile
     */
    protected int mapXIndex;
    /**
     * Y index of the map tile
     */
    protected int mapYIndex;
    /**
     * zoom Level of the map tile.
     */
    protected int mapZoomLevel;
    /**
     * max wait time for download an image in seconds.
     */
    protected int MAX_WAITING_TIME = 90;
    /**
     *  Http connection for donwloading images.
     */
    protected HttpConnection imgConn = null;
    
    /**
     * the map tile width.
     */
    protected static int MAP_TILE_WIDTH = 256;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    protected static int cast2Integer(double f) {
        if (f < 0) {
            return (int) MathEx.ceil(f);
        } else {
            return (int) MathEx.floor(f);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the index of map tiles based on given piexl coordinates
     * @param x  x coordinates
     * @param y y coordinates .
     * @return the the index of map tiles
     */
    protected static GeoPoint GetMapIndex(double x, double y) {
        double longtiles = x / MAP_TILE_WIDTH;
        int tilex = cast2Integer(longtiles);
        int tiley = cast2Integer(y / MAP_TILE_WIDTH);
        return new GeoPoint(tilex, tiley);
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	      Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * PostData defines HTTP multi-part Post message contents.
     * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
     * @version     2.00, 11/09/10
     * @author      Guidebee Pty Ltd.
     */
    protected class MapProgressInputStream extends InputStream {

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 11SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        public MapProgressInputStream(final InputStream is, final int total,
                final IReaderListener listener, final Object context,
                final int notifyInterval) {
            this.is = is;
            this.total = total;
            this.listener = listener;
            this.context = context;
            this.notifyInterval = notifyInterval;
            nread = 0;
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 11SEP2010  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *
         * @return
         * @throws IOException
         */
        public int read() throws IOException {
            if ((++nread % notifyInterval) == 0) {
                try {
                    if(listener!=null) listener.readProgress(nread, total);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return is.read();
        }

        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 11SEP2010  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *
         * @throws IOException
         */
        public void close() throws IOException {
            is.close();
            super.close();
        }

        private final InputStream is;
        private final int total;
        private final IReaderListener listener;
        private final Object context;
        private final int notifyInterval;
        private int nread;
    }
}



