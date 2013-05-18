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
import java.io.IOException;
import java.util.Vector;

import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLngBounds;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to store driving directions results
 * <p></p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public final class MapTileStreamReader extends MapTileAbstractReader {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a map downloader
     */
    public MapTileStreamReader() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open the map.
     * @throws IOException if there's problem opening the map.
     */
    public void open() throws IOException {
        synchronized (mapTiledZones) {
            int layerCount = mapTiledZones.size();
            if (layerCount > 0) {
                ((MapTiledZone) mapTiledZones.elementAt(0)).open();
                bounds = ((MapTiledZone) mapTiledZones.elementAt(0)).bounds;
            } else {
                bounds = new GeoLatLngBounds();
            }
           for (int i = 1; i < layerCount; i++) {
                MapTiledZone mapTiledZone = (MapTiledZone) mapTiledZones.elementAt(i);
                mapTiledZone.open();
                GeoBounds.union(mapTiledZone.bounds, bounds, bounds);
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
     * cpen the map.
     * @throws IOException if there's problem closing the map.
     */
    public void close() throws IOException{
        synchronized (mapTiledZones) {
           int layerCount = mapTiledZones.size();
           for (int i = 1; i < layerCount; i++) {
                MapTiledZone mapTiledZone = (MapTiledZone) mapTiledZones.elementAt(i);
                mapTiledZone.close();
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
     * Add a map zone to map's zone collection.
     * @param mapZone a map zone to add.
     */
    public void addZone(MapTiledZone mapZone) {
        synchronized (mapTiledZones) {
            if (!mapTiledZones.contains(mapZone)) {
                mapZone.readListener = this.readListener;
                mapTiledZones.addElement(mapZone);
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
     * Set downloader listener
     * @param listener
     */
    public void setMapDownloadingListener(IReaderListener listener) {
        super.setMapDownloadingListener(listener);
        synchronized (mapTiledZones) {
            for (int i = 0; i < mapTiledZones.size(); i++) {
                ((MapTiledZone) mapTiledZones.elementAt(i)).readListener
                        = listener;
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
     * return map zone object of given index.
     * @param index index of the map zone.
     * @return map zone object of given index.
     */
    public MapTiledZone getMapZone(int index) {
        synchronized (mapTiledZones) {
            MapTiledZone mapZone = null;
            if (index >= 0 && index < mapTiledZones.size()) {
                mapZone = (MapTiledZone) mapTiledZones.elementAt(index);
            }
            return mapZone;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the count of zones in the map.
     * @return the number of map zones in the map zone collection.
     */
    public int getZoneCount() {
        synchronized (mapTiledZones) {
            return mapTiledZones.size();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Inserts the specified map zone to map's zone collection at the
     * specified index. Each map zone in map's zone collection  with an index
     * greater or equal to the specified index is shifted upward to have an
     * index one greater than the value it had previously.
     * @param mapZone the map zone to insert.
     * @param index  where to insert the new map zone.
     */
    public void insertZone(MapTiledZone mapZone, int index) {
        synchronized (mapTiledZones) {
            if (!mapTiledZones.contains(mapZone)) {
                mapTiledZones.insertElementAt(mapZone, index);
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
     * Moves a zone in the Zone collection to change the order in which
     * zones are drawn.
     * @param from Index number of the zone to move. The topmost zone is 0.
     * @param to New location for the zone. For example, if you want it to be
     *  the second zone, use 1
     */
    public void moveZone(int from, int to) {
        synchronized (mapTiledZones) {
            if (from < 0 || from >= mapTiledZones.size() ||
                    to < 0 || to >= mapTiledZones.size()) {
                return;
            }
            MapTiledZone mapZoneFrom = (MapTiledZone) mapTiledZones.elementAt(from);
            MapTiledZone mapZoneTo = (MapTiledZone) mapTiledZones.elementAt(to);
            mapTiledZones.setElementAt(mapZoneTo, from);
            mapTiledZones.setElementAt(mapZoneFrom, to);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Remove a map zone from map's zone collection.
     * @param mapZone map zone to be removed.
     */
    public void removeZone(MapTiledZone mapZone) {
        synchronized (mapTiledZones) {
            if (mapTiledZones.contains(mapZone)) {
                mapTiledZones.removeElement(mapZone);
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
     * Remove all map zones from map's zone collection.
     */
    public void removeAllZones() {
        synchronized (mapTiledZones) {
            mapTiledZones.removeAllElements();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map zone collection.
     * @return the map zone collection.
     */
    public Vector getMapZones() {
        return mapTiledZones;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void getImage(int mtype, int x, int y, int zoomLevel) {
        byte[] imgBuffer = null;
        try {
            synchronized (mapTiledZones) {
                int zoneCount = mapTiledZones.size();
                for (int i = 0; i < zoneCount; i++) {
                    MapTiledZone mapTiledZone
                            = (MapTiledZone) mapTiledZones.elementAt(i);
                    imgBuffer = mapTiledZone.getImage(mtype,zoomLevel, x, y);
                    if (imgBuffer != null) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        //inglore the error
        }
        if (imgBuffer == null) {
            isImagevalid = false;
            imageArray = null;

        } else {
            imageArray = imgBuffer;
            isImagevalid = true;
            imageArraySize = imageArray.length;
        }

    }
   
    /**
     * map zones object.
     */
    private final Vector mapTiledZones = new Vector();
    /**
     * the boundary of this map.
     */
    private GeoLatLngBounds bounds = null;



}
