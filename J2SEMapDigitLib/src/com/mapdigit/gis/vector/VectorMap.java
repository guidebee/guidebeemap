//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.raster.MapType;
import com.mapdigit.gis.raster.MapTileDownloadManager;
import com.mapdigit.gis.raster.RasterMap;

import java.io.IOException;
import java.util.Hashtable;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * VectorMap is the basic building blocks for Guidebee local map. Each map is
 * consists of multiple map Layers.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public class VectorMap extends RasterMap {
     
    private GeoSet geoSet;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     * @param width the width of the map.
     * @param height the height of the map.
     * @param mapTileDownloadManager map download manager.
     * @param geoSet geoset instance.
     * @throws InvalidLicenceException
     */
    public VectorMap(int width, int height,
            MapTileDownloadManager mapTileDownloadManager, GeoSet geoSet)
             {
        super(width, height,mapTileDownloadManager);
        this.geoSet = geoSet;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set a new geoset for this vector map.
     * @param geoSet a new geoset.
     */
    public void setGeoSet(GeoSet geoSet){
        this.geoSet=geoSet;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the map type.
     * @return the map type, always MapType.MAPINFOVECTORMAP.
     */
    public int getMapType() {
        return MapType.MAPINFOVECTORMAP;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map objects in the screen area whose center is given point
     * @return the map ojectes in the screen area.
     * @throws IOException
     */
    public Hashtable[] getScreenObjects() throws IOException {
        return getScreenObjects(mapCenterPt);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Get the map objects in the screen area whose center is given point
     * @param pt center of the screen.
     * @return the map objects in the screen area.
     * @throws IOException
     */
    public Hashtable[] getScreenObjects(GeoLatLng pt) throws IOException {
        mapCenterPt.x = pt.x;
        mapCenterPt.y = pt.y;
        GeoLatLngBounds rectGeo = getScreenBounds(mapCenterPt);
        return geoSet.search(rectGeo);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add a map layer to map's layer collection.
     * @param mapLayer a layer to be added.
     */
    public void addMapFeatureLayer(MapFeatureLayer mapLayer) {
        if(geoSet!=null){
            geoSet.addMapFeatureLayer(mapLayer);
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
        if(geoSet!=null){
            geoSet.getMapFeatureLayer(index);
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
     * return the count of layers in the map.
     * @return the number of map layers in the map layer collection.
     */
    public int getMapFeatureLayerCount() {
        if(geoSet!=null){
            return geoSet.getMapFeatureLayerCount();
        }
        return 0;
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
        if(geoSet!=null){
            geoSet.insertMapFeatureLayer(mapLayer, index);
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
        if(geoSet!=null){
            geoSet.moveMapFeatureLayer(from, to);
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
        if(geoSet!=null){
            geoSet.removeMapFeatureLayer(mapLayer);
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
        if(geoSet!=null){
            geoSet.removeAllMapFeatureLayers();
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
        if(geoSet!=null){
            return geoSet.getMapFeatureLayers();
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
        if(geoSet!=null){
            return geoSet.search(matchString);
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
     * get all records based on given rectangle.
     * @param rectGeo the boundary..
     * @return a hashtable array contains of all matched record.
     *  the key is the mapInfo ID. the value is the MBR of map object.
     * @throws IOException
     */
    public Hashtable[] search(GeoLatLngBounds rectGeo) throws IOException {
         if(geoSet!=null){
            return geoSet.search(rectGeo);
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
     * get all records based on search condition  in give map layer.
     * @param index the index of given map layer.
     * @param findConditions the search condition.
     * @return a hashtable of all matched record.the key is the mapInfo ID.
     * @throws IOException
     */
    public Hashtable search(int index, FindConditions findConditions)
            throws IOException {

        if(geoSet!=null){
            return geoSet.search(index,findConditions);
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
     * get all records based on given rectangle in give map layer.
     * @param index the index of given map layer.
     * @param rectGeo the boundary..
     * @return a hashtable of all matched record.the key is the mapInfo ID.
     * @throws IOException
     */
    public Hashtable search(int index, GeoLatLngBounds rectGeo) throws IOException {

        if(geoSet!=null){
            return geoSet.search(index,rectGeo);
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
     * return the bound rect of the geoset.
     * @return the bound rect of the geoset.
     */
    public GeoLatLngBounds getBounds() {
        if(geoSet!=null){
            return geoSet.getBounds();
        }
        return new GeoLatLngBounds();
    }




    
}
