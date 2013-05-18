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
import com.mapdigit.drawing.geometry.Rectangle;
import com.mapdigit.util.MathEx;

import com.mapdigit.gis.drawing.AbstractGraphicsFactory;
import com.mapdigit.gis.drawing.IGraphics;

import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPoint;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapLayer defines a map layer.Computer maps are organized into layers. Think
 * of the layers as transparencies that are stacked on top of one another. Each
 * layer contains different aspects of the entire map. Each layer contains
 * different map objects, such as regions, points, lines and text.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 04/01/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class MapLayer {
    
    /**
     * the width of each map tile
     */
    public final static int MAP_TILE_WIDTH = 256;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the graphics factory for the map layer.
     * @param abstractGraphicsFactory
     */
    public static void setAbstractGraphicsFactory(AbstractGraphicsFactory
            abstractGraphicsFactory) {
        MapLayer.abstractGraphicsFactory = abstractGraphicsFactory;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the graphics factory used by this map layer.
     * @return the graphics factory used by this map layer.
     */
    public static AbstractGraphicsFactory getAbstractGraphicsFactory() {
        return abstractGraphicsFactory;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert String to Latitude,Longitude pair, the input string has this format
     * [longitude,Latitude,altitude] for example  [115.857562,-31.948275,0]
     * @param location  location string
     * @return the geographical coordinates.
     */
    public static GeoLatLng fromStringToLatLng(String location) {
        location = location.trim();
        location = location.substring(1, location.length() - 1);
        int commaIndex = location.indexOf(",");
        String longitude = location.substring(0, commaIndex);
        int commaIndex1 = location.indexOf(",", commaIndex + 1);
        String latitude = location.substring(commaIndex + 1, commaIndex1);
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        return new GeoLatLng(lat, lng);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the pixel coordinates of the given geographical point .
     * @param latLng  latitude,longitude pair of give point
     * @param zoomLevel   current zoom level
     * @return the pixel coordinates.
     */
    public static GeoPoint fromLatLngToPixel(GeoLatLng latLng, int zoomLevel) {
        double latitude = latLng.lat();
        double longitude = latLng.lng();
        double power = 8 + zoomLevel;
        double mapsize = MathEx.pow(2, power);
        double origin = mapsize / 2;
        double longdeg = MathEx.abs(-180 - longitude);
        double longppd = mapsize / 360;
        double longppdrad = mapsize / (2 * Math.PI);
        double pixelx = longdeg * longppd;
        double e = MathEx.sin(latitude * (1 / 180.0 * MathEx.PI));
        if (e > 0.9999) {
            e = 0.9999;
        }
        if (e < -0.9999) {
            e = -0.9999;
        }

        double pixely = origin + 0.5 * MathEx.log((1 + e) / (1 - e)) * (-longppdrad);
        return new GeoPoint(pixelx, pixely);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the geographical coordinates from pixel coordinates.
     * @param pt  pixel coordinates.
     * @param zoomLevel   current zoom level
     * @return the geographical coordinates (latitude,longitude) pair
     */
    public static GeoLatLng fromPixelToLatLng(GeoPoint pt, int zoomLevel) {
        double maxLat = Math.PI;
        double zoom = zoomLevel;
        double maxTileY;
        double TileWidth = 256.0;
        double TileHeight = 256.0;
        double TileY = (pt.y / TileHeight);
        double y = (pt.y - TileY * TileHeight);
        double MercatorY, res, a;
        maxTileY = MathEx.pow(2, zoom);
        MercatorY = TileY + y / TileHeight;
        res = maxLat * (1 - 2 * MercatorY / maxTileY);
        a = MathEx.exp(2 * res);
        a = (a - 1) / (a + 1);
        a = a / MathEx.sqrt(1 - a * a);
        double lat = MathEx.atan(a) * 180 / Math.PI;

        double TileX = pt.x / TileHeight;
        double x = (pt.x - TileX * TileHeight);
        double maxTileX;
        double MercatorX;
        maxTileX = MathEx.pow(2, zoom);//2^zoom
        MercatorX = TileX + x / TileWidth;
        res = MercatorX / maxTileX;
        double lng = 360 * res - 180;
        return new GeoLatLng(lat, lng);
    }
 
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the max map zoom level,default is 17
     * @param zoom new max zoom Level.
     */
    public void setMaxZoomLevel(int zoom){
        maxMapZoomLevel=zoom;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the min map zoom level,default is 17
     * @param zoom new min zoom Level.
     */
    public void setMinZoomLevel(int zoom){
        minMapZoomLevel=zoom;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the pixel coordinates of the given geographical point in the map.
     * @param latlng the geographical coordinates.
     * @return the pixel coordinates in the map.
     */
    public GeoPoint fromLatLngToMapPixel(GeoLatLng latlng) {
        GeoPoint center = fromLatLngToPixel(mapCenterPt, mapZoomLevel);
        GeoPoint topLeft = new GeoPoint(center.x - mapSize.width / 2.0,
                center.y - mapSize.height / 2.0);
        GeoPoint pointPos = fromLatLngToPixel(latlng, mapZoomLevel);
        pointPos.x -= topLeft.x;
        pointPos.y -= topLeft.y;
        return new GeoPoint((int) (pointPos.x + 0.5), (int) (pointPos.y + 0.5));

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the geographical coordinates from pixel coordinates in the map.
     * @param pt pixel coordinates in the map.
     * @return the the geographical coordinates.
     */
    public GeoLatLng fromMapPixelToLatLng(GeoPoint pt) {
        GeoPoint center = fromLatLngToPixel(mapCenterPt, mapZoomLevel);
        GeoPoint topLeft = new GeoPoint(center.x - mapSize.width / 2.0,
                center.y - mapSize.height / 2.0);
        GeoPoint pointPos = new GeoPoint(pt.x, pt.y);
        pointPos.x += topLeft.x;
        pointPos.y += topLeft.y;
        GeoLatLng latLng = fromPixelToLatLng(pointPos, mapZoomLevel);
        return latLng;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return screen boundary in geo coordinates.
     * @param pt the center of the screen.
     * @return screen boundary in geo coordinates.
     */
    public GeoLatLngBounds getScreenBounds(GeoLatLng pt) {
        synchronized (syncObject) {
            GeoPoint topLeft;
            GeoPoint bottomRight;
            GeoPoint center = fromLatLngToPixel(pt, mapZoomLevel);
            int shiftWidth = screenSize.width ;
            topLeft = new GeoPoint(center.x - screenSize.width / 2.0 - shiftWidth,
                    center.y - screenSize.height / 2.0 - screenSize.height);
            bottomRight = new GeoPoint(center.x + screenSize.width / 2.0 + shiftWidth,
                    center.y + screenSize.height / 2.0 + screenSize.height);
            GeoLatLng topLeftLatLng = fromPixelToLatLng(topLeft, mapZoomLevel);
            GeoLatLng bottomRightLatLng = fromPixelToLatLng(bottomRight, mapZoomLevel);
            double minX, minY;
            double maxX, maxY;
            minY = Math.min(bottomRightLatLng.lat(), topLeftLatLng.lat());
            maxY = Math.max(bottomRightLatLng.lat(), topLeftLatLng.lat());
            minX = Math.min(bottomRightLatLng.lng(), topLeftLatLng.lng());
            maxX = Math.max(bottomRightLatLng.lng(), topLeftLatLng.lng());
            return new GeoLatLngBounds(minX, minY, maxX - minX, maxY - minY);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return screen boundary in geo coordinates.
     * @return screen boundary in geo coordinates.
     */
    public GeoLatLngBounds getScreenBounds(){
        return getScreenBounds(mapCenterPt);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return screen boundary in geo coordinates.
     * @param pt the center of the screen.
     * @return screen boundary in geo coordinates.
     */
    public GeoLatLngBounds getMapBounds(GeoLatLng pt) {
      synchronized (syncObject) {
            GeoPoint topLeft;
            GeoPoint bottomRight;
            GeoPoint center = fromLatLngToPixel(pt, mapZoomLevel);
            int shiftWidth = mapSize.width / 8;
            topLeft = new GeoPoint(center.x - mapSize.width / 2.0 - shiftWidth,
                    center.y - mapSize.height / 2.0 - mapSize.height);
            bottomRight = new GeoPoint(center.x + mapSize.width / 2.0 + shiftWidth,
                    center.y + mapSize.height / 2.0 + mapSize.height);
            GeoLatLng topLeftLatLng = fromPixelToLatLng(topLeft, mapZoomLevel);
            GeoLatLng bottomRightLatLng = fromPixelToLatLng(bottomRight, mapZoomLevel);

            double minX, minY;
            double maxX, maxY;
            minY = Math.min(bottomRightLatLng.lat(), topLeftLatLng.lat());
            maxY = Math.max(bottomRightLatLng.lat(), topLeftLatLng.lat());
            minX = Math.min(bottomRightLatLng.lng(), topLeftLatLng.lng());
            maxX = Math.max(bottomRightLatLng.lng(), topLeftLatLng.lng());
            return new GeoLatLngBounds(minX, minY, maxX - minX, maxY - minY);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return screen boundary in geo coordinates.
     * @return screen boundary in geo coordinates.
     */
    public GeoLatLngBounds getMapBounds() {
        return getMapBounds(mapCenterPt);
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
    public void panDirection(int dx, int dy) {
        synchronized (syncObject) {
            GeoPoint center = fromLatLngToPixel(mapCenterPt, mapZoomLevel);
            center.x += dx;
            center.y += dy;
            GeoLatLng newCenter = fromPixelToLatLng(center, mapZoomLevel);
            panTo(newCenter);
        }
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
        synchronized (syncObject) {
            this.mapCenterPt.x = center.x;
            this.mapCenterPt.y = center.y;
            drawMapCanvas();
        }
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
        synchronized (syncObject) {
            this.mapZoomLevel = zoomLevel;
            this.mapCenterPt.x = center.x;
            this.mapCenterPt.y = center.y;
            drawMapCanvas();
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the center point of the map.
     * @return current map center point.
     */
    public GeoLatLng getCenter() {
        synchronized (syncObject) {
            return this.mapCenterPt;
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the sync root. this sync object is used to sync the map operation.
     * @return sync root.
     */
    public Object  getSyncRoot(){
        return syncObject;
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
        synchronized (syncObject) {
            mapZoomLevel++;
            if (mapZoomLevel >= maxMapZoomLevel) {
                mapZoomLevel = maxMapZoomLevel;
            }
            drawMapCanvas();
        }
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
        synchronized (syncObject) {
            mapZoomLevel--;
            if (mapZoomLevel < minMapZoomLevel) {
                mapZoomLevel = minMapZoomLevel;
            }
            drawMapCanvas();
        }
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
        synchronized (syncObject) {
            this.mapZoomLevel = level;
            drawMapCanvas();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the zoom level of the map.
     * @return current map zoom level.
     */
    public int getZoom() {
        synchronized (syncObject) {
            return this.mapZoomLevel;
        }
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
        synchronized (syncObject) {
            GeoLatLng sw = bounds.getSouthWest();
            GeoLatLng ne = bounds.getNorthEast();
            GeoLatLng center =new GeoLatLng();
            center.x=(sw.x+ne.x)/2.0;
            center.y=(sw.y+ne.y)/2.0;
            GeoPoint pt1, pt2;
            double dblWidth, dblHeight;
            for (int i = maxMapZoomLevel; i >= minMapZoomLevel; i--) {
                pt1 = fromLatLngToPixel(sw, i);
                pt2 = fromLatLngToPixel(ne, i);
                dblWidth = Math.abs(pt1.x - pt2.x);
                dblHeight = Math.abs(pt1.y - pt2.y);
                if (dblWidth < mapSize.width && dblHeight < mapSize.height) {
                    this.mapZoomLevel = i;
                    setCenter(center, i);
                    break;
                }
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
     * is the point in current screen (is shown or not).
     * @param pt point to be tested.
     * @return true is in screen range.
     */
    public boolean isPointVisible(GeoLatLng pt) {
        GeoPoint screenPt = fromLatLngToMapPixel(pt);
        return mapSize.contains((int)screenPt.x,(int)screenPt.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map screen width.
     * @return the map screen width.
     */
    public int getMapWidth() {
        return  mapSize.width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map screen height.
     * @return the map screen height.
     */
    public int getMapHeight() {
        return  mapSize.height;
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
        synchronized (syncObject) {
            mapSize.width = width;
            mapSize.height = height;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 27DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the view size of the map.
     * @param width the width of the view.
     * @param height the height of the view.
     */
    public void setViewSize(int width, int height) {
        setScreenSize(width,height);
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
        synchronized (syncObject) {
            screenSize.width = width;
            screenSize.height = height;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map screen width.
     * @return the map screen width.
     */
    public int getScreenWidth() {
        return  screenSize.width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map screen height.
     * @return the map screen height.
     */
    public int getScreenHeight() {
        return  screenSize.height;
    }

        
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Draw the map layer to an graphics.
     * @param graphics
     */
    public void paint(IGraphics graphics){
        paint(graphics,0,0);
    }

    /**
     * Max map zoom Level
     */
    protected static int maxMapZoomLevel = 17;

    /**
     * min map zoom Level
     */
    protected static int minMapZoomLevel = 1;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Draw the map layer to an graphics.
     * @param graphics the graphics object where the map is drawn.
     * @param offsetX the drawing start X coordinate.
     * @param offsetY the drawing start Y coordinate.
     */
    public abstract void paint(IGraphics graphics,int offsetX,int offsetY);

    /**
     * the center of this map.
     */
    protected volatile GeoLatLng mapCenterPt = new GeoLatLng();

    /**
     * current map zoom level
     */
    protected volatile int mapZoomLevel = 1;

    /**
     * the size of the map size.
     */
    protected volatile Rectangle mapSize = new Rectangle();

    /**
     * the size of the screen size.
     */
    protected volatile Rectangle screenSize = new Rectangle();

    /**
     * sync object.
     */
    protected final Object syncObject = new Object();

    /**
     * Abstract graphics factory.
     */
    protected static AbstractGraphicsFactory abstractGraphicsFactory=null;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw map on the image canvas
     */
    protected void drawMapCanvas(){}

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     * @param width the width of the map layer.
     * @param height the height of the map layer.
     */
    protected MapLayer(int width,int height) {
        screenSize.x=0; screenSize.y=0;
        screenSize.width = MAP_TILE_WIDTH;
        screenSize.height = MAP_TILE_WIDTH;
        mapSize.x = 0; mapSize.y = 0;
        mapSize.width = width; mapSize.height = height;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
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

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the index of map tiles based on given geographical coordinates
     * @param latitude  y coordinates in geographical space.
     * @param longitude x coordinates in geographical space.
     * @param zoomLevel   current zoom level
     * @return the the index of map tiles
     */
    public static GeoPoint convertCoordindates2Tiles(double latitude,
            double longitude, int zoomLevel) {
        GeoPoint pt = fromLatLngToPixel(new GeoLatLng(latitude, longitude), zoomLevel);
        int tilex = cast2Integer(pt.x / MAP_TILE_WIDTH);
        int tiley = cast2Integer(pt.y / MAP_TILE_WIDTH);
        return new GeoPoint(tilex, tiley);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  cast double to integer
     * @param f the double value.
     * @return the closed interger for the double value.
     */
    protected static int cast2Integer(double f) {
        if (f < 0) {
            return (int) MathEx.ceil(f);
        } else {
            return (int) MathEx.floor(f);
        }
    }
}
