//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import com.mapdigit.gis.drawing.IFont;
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.util.DataReader;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to store driving directions results
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class GeoSet {

    /**
     * map unit is in miles.
     */
    public static final int MAPUNIT_MILE = 0;
    /**
     * map unit is in kilometer.
     */
    public static final int MAPUNIT_KM = 1;
    /**
     * map unit.
     */
    public volatile int mapUnit = MAPUNIT_KM;
    /**
     * default font Ex
     */
    public volatile IFont fontEx = null;
    /**
     * the backcolor for this map.
     */
    public volatile int backColor = 0xffffff;
    /**
     * The name of the map layer.
     */
    public String name;
    /**
     * zoom level distance in mapunit.
     */
    public double zoomLevel = 0;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Constructor.
     */
    public GeoSet()  {

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param reader GeoSet input stream.
     * @throws IOException
     */
    public GeoSet(DataInputStream reader) throws IOException {
       String [] layerNames= readGeoSet(reader);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add a map layer to map's layer collection.
     * @param mapLayer a map feature layer to be added.
     */
    public void addMapFeatureLayer(MapFeatureLayer mapLayer) {
        synchronized (mapFeatureLayers) {
            if (!mapFeatureLayers.contains(mapLayer)) {
                mapFeatureLayers.addElement(mapLayer);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the count of layers in the map.
     * @param index the index of the map layer.
     * @return the number of map layers in the map layer collection.
     */
    public MapFeatureLayer getMapFeatureLayer(int index) {
        synchronized (mapFeatureLayers) {
            MapFeatureLayer mapLayer = null;
            if (index >= 0 && index < mapFeatureLayers.size()) {
                mapLayer = (MapFeatureLayer) mapFeatureLayers.elementAt(index);
            }
            return mapLayer;
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the count of layers in the map.
     * @return the number of map layers in the map layer collection.
     */
    public int getMapFeatureLayerCount() {
        synchronized (mapFeatureLayers) {
            return mapFeatureLayers.size();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Inserts the specified map layer to map's layer collection at the
     * specified index. Each map layer in map's layer collection  with an index
     * greater or equal to the specified index is shifted upward to have an
     * index one greater than the value it had previously.
     * @param mapLayer the map layer to insert.
     * @param index  where to insert the new map layer.
     */
    public void insertMapFeatureLayer(MapFeatureLayer mapLayer, int index) {
        synchronized (mapFeatureLayers) {
            if (!mapFeatureLayers.contains(mapLayer)) {
                mapFeatureLayers.insertElementAt(mapLayer, index);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Moves a layer in the Layer collection to change the order in which
     * layers are drawn.
     * @param from Index number of the layer to move. The topmost layer is 0.
     * @param to New location for the layer. For example, if you want it to be
     *  the second layer, use 1
     */
    public void moveMapFeatureLayer(int from, int to) {
        synchronized (mapFeatureLayers) {
            if (from < 0 || from >= mapFeatureLayers.size() ||
                    to < 0 || to >= mapFeatureLayers.size()) {
                return;
            }
            MapFeatureLayer mapLayerFrom = (MapFeatureLayer) mapFeatureLayers.elementAt(from);
            MapFeatureLayer mapLayerTo = (MapFeatureLayer) mapFeatureLayers.elementAt(to);
            mapFeatureLayers.setElementAt(mapLayerTo, from);
            mapFeatureLayers.setElementAt(mapLayerFrom, to);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Remove a map layer from map's layer collection.
     * @param mapLayer map layer to be removed.
     */
    public void removeMapFeatureLayer(MapFeatureLayer mapLayer) {
        synchronized (mapFeatureLayers) {
            if (mapFeatureLayers.contains(mapLayer)) {
                mapFeatureLayers.removeElement(mapLayer);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Remove all map layers from map's layer collection.
     */
    public void removeAllMapFeatureLayers() {
        synchronized (mapFeatureLayers) {
            mapFeatureLayers.removeAllElements();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map layer collection.
     * @return the map layer collection.
     */
    public MapFeatureLayer[] getMapFeatureLayers() {
        synchronized (mapFeatureLayers) {
            if (mapFeatureLayers.size() > 0) {
                MapFeatureLayer[] copiedFeatureLayers = new MapFeatureLayer[mapFeatureLayers.size()];
                mapFeatureLayers.copyInto(copiedFeatureLayers);
                return copiedFeatureLayers;
            }

        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all records based on given string. the seach will search based on
     * map layer's key field.
     * @param matchString
     * @return a hashtable array contains of all matched record.
     *  the key is the mapInfo ID. the value is the matched string.
     * @throws IOException
     */
    public Hashtable[] search(String matchString) throws IOException {
        synchronized (mapFeatureLayers) {
            Hashtable[] retTable = new Hashtable[mapFeatureLayers.size()];
            for (int i = 0; i < mapFeatureLayers.size(); i++) {
                MapFeatureLayer mapLayer = (MapFeatureLayer) mapFeatureLayers.elementAt(i);
                FindConditions findConditions = new FindConditions();
                findConditions.addCondition(mapLayer.dataTable.getFieldIndex(mapLayer.keyField.getName()), matchString);
                retTable[i] = mapLayer.search(findConditions);
            }
            return retTable;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * get all records based on given rectangle.
     * @param rectGeo the boundary..
     * @return a hashtable array contains of all matched record.
     *  the key is the mapInfo ID. the value is the MBR of map object.
     * @throws IOException
     */
    public Hashtable[] search(GeoLatLngBounds rectGeo) throws IOException {
        synchronized (mapFeatureLayers) {
            Hashtable[] retTable = new Hashtable[mapFeatureLayers.size()];
            GeoLatLng pt1 = new GeoLatLng(rectGeo.y, rectGeo.x);
            GeoLatLng pt2 = new GeoLatLng(rectGeo.y + rectGeo.height,
                    rectGeo.x + rectGeo.width);
            double distance = GeoLatLng.distance(pt1, pt2);

            if (this.mapUnit == MAPUNIT_MILE) {
                distance /= 1.632;
            }

            for (int i = 0; i < mapFeatureLayers.size(); i++) {
                MapFeatureLayer mapLayer = (MapFeatureLayer) mapFeatureLayers.elementAt(i);
                if (mapLayer.canBeShown(distance)) {
                    retTable[i] = mapLayer.search(rectGeo);
                } else {
                    retTable[i] = new Hashtable();
                }
            }
            return retTable;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open the map.
     * @throws IOException
     */
    public void open() throws IOException {
        synchronized (mapFeatureLayers) {
            int layerCount = mapFeatureLayers.size();


            if (layerCount > 0) {
                ((MapFeatureLayer) mapFeatureLayers.elementAt(0)).open();
                bounds = ((MapFeatureLayer) mapFeatureLayers.elementAt(0)).bounds;
            } else {
                bounds = new GeoLatLngBounds();
            }

            for (int i = 1; i < layerCount; i++) {
                MapFeatureLayer mapLayer = (MapFeatureLayer) mapFeatureLayers.elementAt(i);
                mapLayer.open();
                GeoBounds.union(mapLayer.bounds, bounds, bounds);
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the map.
     * @throws IOException
     */
    public void close() throws IOException {
        synchronized (mapFeatureLayers) {
            int layerCount = mapFeatureLayers.size();
            for (int i = 0; i < layerCount; i++) {
                MapFeatureLayer mapLayer = (MapFeatureLayer) mapFeatureLayers.elementAt(i);
                mapLayer.close();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all records based on search condition  in give map layer.
     * @param index the index of given map layer.
     * @param findConditions the search condition.
     * @return a hashtable of all matched record.the key is the mapInfo ID.
     * @throws IOException
     */
    public Hashtable search(int index, FindConditions findConditions)
            throws IOException {
        synchronized (mapFeatureLayers) {
            MapFeatureLayer mapLayer = getMapFeatureLayer(index);
            if (mapLayer != null) {
                return mapLayer.search(findConditions);
            }

            return null;
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all records based on given rectangle in give map layer.
     * @param index the index of given map layer.
     * @param rectGeo the boundary..
     * @return a hashtable of all matched record.the key is the mapInfo ID.
     * @throws IOException 
     */
    public Hashtable search(int index, GeoLatLngBounds rectGeo) throws IOException {
        synchronized (mapFeatureLayers) {
            MapFeatureLayer mapLayer = getMapFeatureLayer(index);
            if (mapLayer != null) {
                return mapLayer.search(rectGeo);
            }
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * read from the geoset file.
     */
    private String[] readGeoSet(DataInputStream reader) throws IOException {
        if (reader == null) {
            throw new IOException("can not read from null reader!");
        }
        if (!reader.markSupported()) {
            throw new IOException("mark is not supported!");
        }
        mapFeatureLayerInfos.removeAllElements();
        String[] layerNames = null;
        reader.mark(Integer.MAX_VALUE);
        DataReader.seek(reader, 0);
        String fileVersion = DataReader.readString(reader);
        DataReader.seek(reader, 16);
        String fileFormat = DataReader.readString(reader);
        DataReader.seek(reader, 32);
        String pstType = DataReader.readString(reader);
        DataReader.seek(reader, 48);
        if (!(fileFormat.equalsIgnoreCase("JAVA") &&
                pstType.equalsIgnoreCase("PST"))) {
            throw new IOException("Invalid file format!");
        }
        name = DataReader.readString(reader);
        DataReader.seek(reader, 128);
        mapUnit = DataReader.readInt(reader);
        zoomLevel = DataReader.readDouble(reader);
        int mapLayerCount = DataReader.readInt(reader);

        for (int i = 0; i < mapLayerCount; i++) {
            DataReader.seek(reader, (long) (i * 512 + 144));
            String layerName = DataReader.readString(reader);
            String description = DataReader.readString(reader);
            byte layerVisible = reader.readByte();
            double zoomMax = DataReader.readDouble(reader);
            double zoomMin = DataReader.readDouble(reader);
            MapFeatureLayerInfo mapLayerInfo = new MapFeatureLayerInfo();
            mapLayerInfo.description = description;
            mapLayerInfo.layerName=layerName;

            if (layerVisible == 1) {
                mapLayerInfo.visible = true;
            } else {
                mapLayerInfo.visible = false;
            }
            mapLayerInfo.zoomMax = zoomMax;
            mapLayerInfo.zoomMin = zoomMin;
            if (zoomMax == zoomMin && zoomMin < 0.001) {
                mapLayerInfo.zoomLevel = false;
            } else {
                mapLayerInfo.zoomLevel = true;
            }
            mapFeatureLayerInfos.addElement(mapLayerInfo);
        }
        reader.close();
        if (mapFeatureLayerInfos.size() > 0) {
            layerNames = new String[mapFeatureLayerInfos.size()];
            for(int i=0;i<mapFeatureLayerInfos.size();i++){
                layerNames[i]=((MapFeatureLayerInfo)mapFeatureLayerInfos
                        .elementAt(i)).layerName;
            }
        }
        return layerNames;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the bound rect of the geoset.
     * @return the bound rectangle of the geoset.
     */
    public GeoLatLngBounds getBounds() {
        return new GeoLatLngBounds(bounds);
    }
    /**
     * map Layers object.
     */
    private final Vector mapFeatureLayers=new Vector();
    private final Vector mapFeatureLayerInfos=new Vector();
    /**
     * the boundary of this map.
     */
    private GeoLatLngBounds bounds = null;

    private class MapFeatureLayerInfo {

        public boolean zoomLevel;
        public boolean visible;
        public double zoomMax;
        public double zoomMin;
        public String layerName;
        public String description;
    }
}
