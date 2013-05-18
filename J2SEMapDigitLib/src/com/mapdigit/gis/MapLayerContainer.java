//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.collections.Vector;

import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapLayerContainer is a collection of map layers.
 * <hr>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 04/01/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class MapLayerContainer extends MapLayer{

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add a map layer to the tail of the container.
     * @param mapLayer
     */
    public void addMapLayer(MapLayer mapLayer) {
        synchronized (mapLayers) {
            if (!mapLayers.contains(mapLayer)) {
                mapLayers.add(mapLayer);
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
     * Add a map layer after given index
     * @param index the index after which a new map layer is added
     * @param mapLayer a map layer inserted into the container
     */
    public void addMapLayerAt(int index,MapLayer mapLayer){
        synchronized (mapLayers) {
            mapLayers.add(index,mapLayer);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Remove all map layers from the container.
     */
    public void removeAllMapLayers(){
        synchronized (mapLayers) {
            mapLayers.removeAllElements();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * remove a map layer at given index
     * @param index the index of the map layer.
     */
    public void removeMapLayerAt(int index){
        synchronized (mapLayers) {
            mapLayers.removeElementAt(index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * remove a givne map layer from the container.
     * @param mapLayer
     */
    public void removeMapLayer(MapLayer mapLayer){
        synchronized (mapLayers) {
            mapLayers.removeElement(mapLayer);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get a map layer at given index.
     * @param index the index of the map layer
     * @return the map layer at given index.
     */
    public MapLayer getMapLayerAt(int index){
        synchronized (mapLayers) {
            return (MapLayer)mapLayers.get(index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get all map layers as an array.
     * @return all map layers included in this container.
     */
    public MapLayer[] getMapLayers(){
        synchronized (mapLayers) {
            return (MapLayer[])mapLayers.toArray();
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the count of the map layers in this container.
     * @return the count of the map layers.
     */
    public int getMapLayerCount(){
        synchronized (mapLayers) {
            return mapLayers.size();
        }
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * @param graphics the graphics object to paint.
     * @param offsetX
     * @param offsetY
     */
    public void paint(IGraphics graphics, int offsetX, int offsetY) {
       // synchronized (mapLayers) 
        {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.paint(graphics, offsetX, offsetY);
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
     * Starts a pan with given distance in pixels.
     * directions. +1 is right and down, -1 is left and up, respectively.
     * @param dx x offset.
     * @param dy y offset.
     */
    public void panDirection(int dx, int dy){
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.panDirection(dx, dy);
            }
        }
        super.panDirection(dx, dy);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Changes the center point of the map to the given point.
     * @param center a new center point of the map.
     */
    public void panTo(GeoLatLng center) {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.panTo(center);
            }
        }
        super.panTo(center);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the map view to the given center.
     * @param center the center latitude,longitude of the map.
     * @param zoomLevel the zoom Level of the map [0,17].
     */
    public void setCenter(GeoLatLng center, int zoomLevel) {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.setCenter(center, zoomLevel);
            }
        }
        super.setCenter(center, zoomLevel);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Increments zoom level by one.
     */
    public void zoomIn() {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.zoomIn();
            }
        }
        super.zoomIn();
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Decrements zoom level by one.
     */
    public void zoomOut() {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.zoomOut();
            }
        }
        super.zoomOut();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the zoom level to the given new value.
     * @param level new map zoom level.
     */
    public void setZoom(int level) {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.setZoom(level);
            }
        }
        super.setZoom(level);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Resize the map to a level that include given bounds
     * @param bounds new bound.
     */
    public void resize(GeoLatLngBounds bounds) {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.resize(bounds);
            }
        }
        super.resize(bounds);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the size for the map.
     * @param width the width of the map.
     * @param height the height of the map.
     */
    public void setMapSize(int width, int height) {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.setMapSize(width, height);
            }
        }
        super.setMapSize(width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the view size of the map.
     * @param width the width of the view.
     * @param height the height of the view.
     */
    public void setScreenSize(int width, int height) {
        synchronized (mapLayers) {
            for (int i = 0; i < mapLayers.size(); i++) {
                MapLayer mapLayer = (MapLayer) mapLayers.get(i);
                mapLayer.setScreenSize(width, height);
            }
        }
        super.setScreenSize(width, height);
    }
    
    /**
     * Vector store all map layers.
     */
    protected final Vector mapLayers=new Vector();
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param width the width of the map layer container.
     * @param height the height of the map layer container.
     */
    protected MapLayerContainer(int width,int height){
        super(width,height);
    }

}
