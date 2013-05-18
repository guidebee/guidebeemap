//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------

import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;

import com.mapdigit.drawing.Color;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.drawing.IFont;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.raster.MapTileAbstractReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Vector map render, each time, the renderer draw one map tile.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public class VectorMapRenderer extends MapTileAbstractReader {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constrcutor.
     * @param geoSet
     */
    public VectorMapRenderer(GeoSet geoSet) {
        this.geoSet = geoSet;
        vectorMapCanvas = new VectorMapCanvas();

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set font color.
     * @param fontColor font color.
     */
    public void setFontColor(int fontColor){
        vectorMapCanvas.setFontColor(fontColor);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set font.
     * @param font new font.
     */
    public void setFont(IFont font){
        vectorMapCanvas.setFont(font);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set a new vectorMapCanvas instance.
     * @param vectorMapCanvas a new vectorMapCanvas instance.
     */
    public void setVectorMapCanvas(VectorMapCanvas vectorMapCanvas) {
        if (vectorMapCanvas != null) {
            this.vectorMapCanvas = vectorMapCanvas;
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void getImage(int mtype, int x, int y, int zoomLevel) {
        synchronized (VectorMapCanvas.graphic2DMutex) {
            int shiftWidth = 4;
            GeoPoint pt1 = new GeoPoint(x * MapLayer.MAP_TILE_WIDTH
                    - shiftWidth,
                    y * MapLayer.MAP_TILE_WIDTH - shiftWidth);
            GeoPoint pt2 = new GeoPoint((x + 1) * MapLayer.MAP_TILE_WIDTH
                    + shiftWidth,
                    (y + 1) * MapLayer.MAP_TILE_WIDTH + shiftWidth);
            GeoLatLng latLng1 = MapLayer.fromPixelToLatLng(pt1, zoomLevel);
            GeoLatLng latLng2 = MapLayer.fromPixelToLatLng(pt2, zoomLevel);
            double minX, minY;
            double maxX, maxY;
            minY = Math.min(latLng1.lat(), latLng2.lat());
            maxY = Math.max(latLng1.lat(), latLng2.lat());
            minX = Math.min(latLng1.lng(), latLng2.lng());
            maxX = Math.max(latLng1.lng(), latLng2.lng());
            GeoLatLngBounds geoBounds = new GeoLatLngBounds(minX, minY,
                    maxX - minX, maxY - minY);
            try {
                Hashtable[] mapFeatures = geoSet.search(geoBounds);
                int totalSize = 0;
                int zOrder = 0;
                for (int i = 0; i < mapFeatures.length; i++) {
                    Hashtable mapFeaturesInLayer = mapFeatures[i];
                    totalSize += mapFeaturesInLayer.size();
                }
                totalSize += 1;
                int mapObjectIndex = 0;
                vectorMapCanvas.clearCanvas(Color.WHITE);
                for (int i = 0; i < mapFeatures.length; i++) {
                    zOrder = mapFeatures.length - 1 - i;
                    Hashtable mapFeaturesInLayer = mapFeatures[zOrder];
                    Enumeration enu = mapFeaturesInLayer.keys();
                    MapFeatureLayer mapLayer = geoSet.getMapFeatureLayer(zOrder);
                    
                    while (enu.hasMoreElements()) {
                        Integer MapInfo_ID = (Integer) enu.nextElement();
                        MapFeature mapFeature = mapLayer
                                .getMapFeatureByID(MapInfo_ID.intValue());
                        mapObjectIndex++;
                        vectorMapCanvas.setFontColor(mapLayer.fontColor);
                        vectorMapCanvas.drawMapObject(mapFeature.mapObject,
                                geoBounds, zoomLevel);
                        if (readListener != null) {
                            readListener.readProgress(mapObjectIndex,
                                    totalSize);
                        }
                    }
                }
                vectorMapCanvas.drawMapText();

                imageArray = PNGEncoder.getPNGRGB(MapLayer.MAP_TILE_WIDTH,
                        MapLayer.MAP_TILE_WIDTH,
                        vectorMapCanvas.getRGB());
                imageArraySize = imageArray.length;
                isImagevalid = true;
                if (readListener != null) {
                    readListener.readProgress(totalSize, totalSize);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
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
     * @inheritDoc
     */
    public void cancelRead() {
    }
    
    /**
     * Geoset as the database for renderer.
     */
    private final GeoSet geoSet;
    /**
     * Vector map canvas.
     */
    private VectorMapCanvas vectorMapCanvas;
}
