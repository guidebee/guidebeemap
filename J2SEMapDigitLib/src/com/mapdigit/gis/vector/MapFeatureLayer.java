//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mapdigit.gis.vector.mapfile.MapFile;
import com.mapdigit.gis.MapObject;
import com.mapdigit.gis.geometry.GeoLatLngBounds;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapLayer defines a map layer.Computer maps are organized into layers. Think 
 * of the layers as transparencies that are stacked on top of one another. Each
 * layer contains different aspects of the entire map. Each layer contains 
 * different map objects, such as regions, points, lines and text.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapFeatureLayer  {
    
    /**
     *  Predominant feature type is Point.
     */
    public static final int FEATURE_TYPE_POINT=MapObject.POINT;
    
    /**
     *  Predominant feature type is polyline.
     */
    public static final int FEATURE_TYPE_PLINE=MapObject.PLINE;
    
    /**
     *  Predominant feature type is region.
     */
    public static final int FEATURE_TYPE_REGION=MapObject.REGION;
    
    /**
     * This property controls whether a layer is automatically labeled. In 
     * order for a label to be displayed automatically, its centroid must be 
     * within the viewable map area. This is a Boolean value, and its default
     * is true.
     */
    public boolean autoLabel=true;

    /**
     * font color.
     */
    public int fontColor=0x000000;
    
    /**
     * A Rectangle object representing the geographic extents 
     * (i.e., the minimum bounding rectangle) of all objects in the layer.
     */
    public GeoLatLngBounds bounds=null;
    
    /**
     * The tabular data table object associated with this map layer.
     */
    public DataTable dataTable=null;
    
    /**
     * This string property identifies the column (field) name in the layer's
     * tabular table that will be the name property of a feature object. 
     * It currently defaults to the first column in the layer's table or the 
     * column with name as "Name" if there is any.
     */
    public DataField keyField=null;
    
    /**
     * Predominant feature type. can be POINT,PLINE or REGION.
     */
    public int predominantFeatureType;
    
    /**
     * Is this map layer visible.
     */
    public boolean visible=true;
    
    /**
     * Description for this mapLayer.
     */
    public String description="";
    
    /**
     * This controls whether the layer is zoom layered. Zoom layering controls 
     * the range of zoom levels (distance across map) for which the layer is 
     * displayed. If Zoom Layering is on, then the values stored in the zoomMax 
     * and zoomMin properties are used. This is a Boolean value, and the default
     * is false.
     */
    public boolean zoomLevel=false;
    
    /**
     * If ZoomLayering is on (zoomLevel=true), then this specifies 
     * the maximum zoom value for which a layer will be drawn on the map.
     * This takes a double value specifying distance in Map units (Map.mapUnit).
     */
    public double zoomMax=0;
    
    /**
     * If ZoomLayering is on (zoomLevel=true), then this specifies 
     * the minimum zoom value for which a layer will be drawn on the map. 
     * This takes a double value specifying distance in Map units (Map.MapUnit).
     */
    public double zoomMin=0;
    
    
    /**
     * the index of the key field.
     */
    private int keyFieldIndex=0;
    
    /**
     * the map file related to this map layer.
     */
    private MapFile mapFile=null;

    private volatile boolean opened=false;
    /**
     * MapObject internal cache.
     */
    private final Hashtable mapObjectCache=new Hashtable(CACHE_SIZE);
    
    /**
     * the internal cache size
     */
    private static final int CACHE_SIZE=32;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param reader  the input stream of the map file.
     */
    public MapFeatureLayer(DataInputStream reader)  {
        mapFile=new MapFile(reader);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open the map layer.
     */
    public void open() throws IOException{
        if (!opened) {
            opened = true;
            mapFile.open();
            this.dataTable = new DataTable(mapFile.tabularData, mapFile.header.fields,
                    mapFile.header.recordCount);
            int foundName = -1;
            for (int i = 0; i < mapFile.header.fields.length; i++) {
                keyField = mapFile.header.fields[i];
                if (keyField.getName().toLowerCase().startsWith("name")) {
                    foundName = i;
                    keyFieldIndex = i;
                    break;
                }
            }
            if (foundName == -1) {
                for (int i = 0; i < mapFile.header.fields.length; i++) {
                    keyField = mapFile.header.fields[i];
                    if (keyField.getType() == DataField.TYPE_CHAR) {
                        foundName = i;
                        keyFieldIndex = i;
                        break;
                    }
                }
            }
            if (foundName == -1) {
                keyField = mapFile.header.fields[0];
                keyFieldIndex = 0;
            }
            if (mapFile.header.dominantType.equalsIgnoreCase("POINT")) {
                this.predominantFeatureType = FEATURE_TYPE_POINT;
            } else if (mapFile.header.dominantType.equalsIgnoreCase("PLINE")) {
                this.predominantFeatureType = FEATURE_TYPE_PLINE;
            } else {
                this.predominantFeatureType = FEATURE_TYPE_REGION;
            }
            this.bounds = mapFile.header.mapBounds;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the map layer.
     */
    public void close() throws IOException{
        if (opened) {
            mapFile.close();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get MapFeature at given mapInfoID.
     * @param mapInfoID the index of the record(MapInfoID).
     * @return MapFeature at given mapInfoID.
     */
    public MapFeature getMapFeatureByID(int mapInfoID) throws IOException {
        MapFeature mapFeature = null;
        Integer mapObjectIDKey = new Integer(mapInfoID);

        if (mapObjectCache.containsKey(mapObjectIDKey)) {
            mapFeature = (MapFeature) mapObjectCache.get(mapObjectIDKey);
        } else {
            mapFeature = new MapFeature();
            mapFeature.mapInfoID = mapInfoID;
            mapFeature.mapObject = getMapObjectByID(mapInfoID);
            mapFeature.dataRowValue = getDataRowValueByID(mapInfoID);
            mapFeature.mapObject.name =
                    mapFeature.dataRowValue.getString(keyFieldIndex);
            storeToCache(mapObjectIDKey, mapFeature);
        }

        return mapFeature;
    }
   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get MapObject at given mapInfoID.
     * @param mapInfoID the index of the record(MapInfoID).
     * @return MapObject at given mapInfoID.
     */
    public MapObject getMapObjectByID(int mapInfoID) throws IOException{
        return mapFile.getMapObject(mapInfoID);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get tabular record at given mapInfo ID.
     * @param mapInfoID the index of the record(MapInfoID).
     * @return tabular record at given mapInfoID.
     */
    public DataRowValue getDataRowValueByID(int mapInfoID)throws IOException{
        return mapFile.getDataRowValue(mapInfoID);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all records based on search condition.
     * @param findConditions the search condition.
     * @return a hashtable of all matched record.the key is the mapInfo ID. the
     *  value is the matched string.
     */
    public Hashtable search(FindConditions findConditions) throws IOException{
        return mapFile.search(findConditions);
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
     * @return a hashtable of all matched record.the key is the mapInfo ID. the
     * value is the matched MapObject's MBR.
     */
    public Hashtable search(GeoLatLngBounds rectGeo) throws IOException{
        return mapFile.searchMapObjectsInRect(rectGeo);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the total record number.
     * @return the total record number.
     */
    public int getRecordCount(){
        return mapFile.getRecordCount();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if this map player can be shown.
     * @param screenWidthDistance the distance of the screen width (in same unit
     * as the map layer(km or mile).
     */
    boolean canBeShown(double screenWidthDistance){
        boolean isShown=visible;
        if(zoomLevel){
            if(!(screenWidthDistance>=zoomMin &&
                    screenWidthDistance<=zoomMax)){
              isShown=false;  
            }
        }
        return isShown;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Store the mapFeature to cache.
     * @param mapObjectIDKey the mapInfo ID key.
     * @param mapFeature the map feature to be cached.
     */
    private void storeToCache(Integer mapObjectIDKey,MapFeature mapFeature){
        if(mapObjectCache.size()>=CACHE_SIZE){
            MapFeature [] mapFeatures=new MapFeature[CACHE_SIZE];
            Enumeration enu=mapObjectCache.elements();
            int index=0;
            while(enu.hasMoreElements()){
                mapFeatures[index++]=(MapFeature)enu.nextElement();
            }
            sortMapFeature(mapFeatures);
            for(int i=0;i<CACHE_SIZE/2;i++){
                Integer deleteMapObjectID=new Integer(mapFeatures[i].mapInfoID);
                mapObjectCache.remove(deleteMapObjectID);
            }
            
        }
        mapObjectCache.put(mapObjectIDKey,mapFeature);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * sort the map feature array.
     */
    private void sortMapFeature(MapFeature [] mapFeatures){
        int n=mapFeatures.length;
        int i,j;
        MapFeature ai;
        
        for(i=1;i<n;i++){
            j=i-1;
            ai=mapFeatures[i];
            while(mapFeatures[j].mapObject.cacheAccessTime.getTime()>
                    ai.mapObject.cacheAccessTime.getTime()){
               mapFeatures[j+1]=mapFeatures[j];
               j--;
               if(j<0) break;
            }
            mapFeatures[j+1]=ai;
        }
    }
}
