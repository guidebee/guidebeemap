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
import java.io.DataInputStream;
import java.io.IOException;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.util.DataReader;
import com.mapdigit.util.Log;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Define a tiled map zone.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public class MapTiledZone {

    /**
     * the boundary of this map zone.
     */
    public GeoLatLngBounds bounds=null;

    /**
     * the map type of this map zone.
     */
    public int mapType=0;

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param reader  the input stream of the map file.
     */
    public MapTiledZone(DataInputStream reader)  {
        this.reader=reader;
        this.isMarkSupported=true;
        this.fileName=null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open the map file.
     * @throws IOException
     */
    public void open() throws IOException {
        if (opened) {
            return;
        }
        if (!isMarkSupported) {
            reader=skipBytes(0);
        }

        if (!reader.markSupported() && isMarkSupported) {
            throw new IOException("mark is not supported!");
        }
        if (isMarkSupported) {
            reader.mark(Integer.MAX_VALUE);
        }
        ensureClose();
        opened = true;
        seek(0);//        DataReader.seek(reader, 0);
        String fileVersion = DataReader.readString(reader);
        ensureClose();
        seek(16);//DataReader.seek(reader, 16);
        String fileFormat = DataReader.readString(reader);
        ensureClose();
        seek(32);// DataReader.seek(reader, 32);
        String mapTile = DataReader.readString(reader);
        ensureClose();
        if (!mapTile.equalsIgnoreCase("TILE")) {
            throw new IOException("Invalid map format!");
        }
        seek(48);//DataReader.seek(reader, 48);
        mapType = DataReader.readInt(reader);
        int numOfLevel = DataReader.readInt(reader);
        double minX, minY, maxX, maxY;
        minY = DataReader.readDouble(reader);
        minX = DataReader.readDouble(reader);
        maxY = DataReader.readDouble(reader);
        maxX = DataReader.readDouble(reader);
        bounds = new GeoLatLngBounds(minX, minY, maxX - minX, maxY - minY);
        ensureClose();
        seek(HEADSIZE);//  DataReader.seek(reader, HEADSIZE);
        levelInfos = new LevelInfo[numOfLevel];
        for (int i = 0; i < numOfLevel; i++) {
            levelInfos[i] = new LevelInfo();
            levelInfos[i].mapType=mapType;
            levelInfos[i].levelNo = DataReader.readInt(reader);
            levelInfos[i].minX = DataReader.readInt(reader);
            levelInfos[i].minY = DataReader.readInt(reader);
            levelInfos[i].maxX = DataReader.readInt(reader);
            levelInfos[i].maxY = DataReader.readInt(reader);
            levelInfos[i].offset = DataReader.readInt(reader);
            levelInfos[i].length = DataReader.readInt(reader);
        }
       ensureClose();
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get image at given location
     * @param level zoom level.
     * @param x x index of the map tile.
     * @param y y index of the map tile.
     * @return the map tile image.
     * @throws java.io.IOException
     */
    public byte[] getImage(int mapType,int level,int x,int y) throws IOException{
        LevelInfo levelInfo=getLevelInfo(level);
        byte []buffer=null;
        if (levelInfo != null) {
            int rows = levelInfo.maxY + 1 - levelInfo.minY;
            //int cols = levelInfo.maxX + 1 - levelInfo.minX;
            boolean checkMapType=!MapConfiguration.ignoreMapTypeInStoredMap?
                levelInfo.mapType==mapType: true;
            if (checkMapType
                    && x <= levelInfo.maxX
                    && x >= levelInfo.minX &&
                    y <= levelInfo.maxY && y >= levelInfo.minY) {
                int imageIndex = (x - levelInfo.minX)*rows+y-levelInfo.minY;

                seek( levelInfo.offset + imageIndex * 8);
                int offset = DataReader.readInt(reader);
                int length = DataReader.readInt(reader);
                
                ensureClose();
                if(length>100*1024){//the file format is wrong ,return null
                    return null;
                }
                buffer = new byte[length];
                seek(offset);
                int howManyKs = length / 1024;
                int remainBytes = length - howManyKs * 1024;
                for (int i = 0; i < howManyKs; i++) {
                    reader.read(buffer, i * 1024, 1024);
                    if (readListener != null) {
                        readListener.readProgress(i * 1024, length);
                    }
                }
                if (remainBytes > 0) {
                    reader.read(buffer, howManyKs * 1024, remainBytes);
                    if (readListener != null) {
                        readListener.readProgress(length, length);
                    }

                }
                ensureClose();
            }
        }
        return buffer;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the map file.
     * @throws IOException
     */
    public void close() throws IOException {
        if (isMarkSupported) {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void seek(long offset) throws IOException {
        if (isMarkSupported) {
            DataReader.seek(reader, offset);
        } else {
            reader = skipBytes(offset);
        }
        if (reader == null) {
            Log.p("Reader is null", Log.ERROR);
            throw new NullPointerException("reader is null");
        }
    }

    public void ensureClose() throws IOException {
        if (!isMarkSupported) {
            reader.close();
        }
    }

    public DataInputStream skipBytes(long offset) {
        DataInputStream tempReader = null;
        
        return tempReader;
    }

    /**
     * support mark or not.
     */
    public boolean isMarkSupported=false;

    /**
     * file name for the map zone.
     */
    public String fileName=null;

    /**
     * reader to read the data from the map file.
     */
    private DataInputStream reader=null;


    /**
     * check if it's opened;
     */
    private boolean opened=false;

    /**
     * level info.
     */
    private LevelInfo[] levelInfos=null;

    /**
     * the header size.
     */
    private static final int HEADSIZE=256;

    /**
     * the level section size.
     */
    private static final int LEVELSIZE=1024;

    /**
     * downloader listener
     */
    IReaderListener readListener = null;

    private LevelInfo getLevelInfo(int level){
        for(int i=0;i<levelInfos.length;i++){
            if(levelInfos[i].levelNo==level){
                return levelInfos[i];
            }
        }
        return null;
    }

    private class LevelInfo {

        public int mapType;
        public int levelNo;
        public int minX;
        public int minY;
        public int maxX;
        public int maxY;
        public int offset;
        public int length;
    }


}

