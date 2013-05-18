//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.drawing.Brush;
import java.util.Vector;

import com.mapdigit.drawing.Color;
import com.mapdigit.drawing.Graphics2D;
import com.mapdigit.drawing.Pen;
import com.mapdigit.drawing.SolidBrush;
import com.mapdigit.drawing.geometry.Polygon;
import com.mapdigit.drawing.geometry.Rectangle;

import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.vector.SutherlandHodgman;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Vector map render, each time, the renderer draw one map tile.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
class MapDirectionRenderer extends MapTileAbstractReader{

    /**
     * transparent color value.
     */
    private static int transparency = 0x00FFFFFF;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constrcutor.
     */
    public MapDirectionRenderer(){

        if(MapConfiguration.routePen!=null){
            routePen=MapConfiguration.routePen;}
        else{
            routePen=new Pen(new Color(0x7F00FF00, false), 4);
        }
        mapDirectionLayer=new MapDirectionLayer(MapLayer.MAP_TILE_WIDTH,
                MapLayer.MAP_TILE_WIDTH);
        if(MapConfiguration.startIconBrush!=null){
            startRouteBrush=MapConfiguration.startIconBrush;}
        else{
            startRouteBrush=new SolidBrush(Color.BLUE);
        }
        if(MapConfiguration.endIconBrush!=null){
            endRouteBrush=MapConfiguration.endIconBrush;}
        else{
            endRouteBrush=new SolidBrush(Color.RED);
        }
        if(MapConfiguration.middleIconBrush!=null){
            middleRouteBrush=MapConfiguration.middleIconBrush;}
        else{
            middleRouteBrush=new SolidBrush(Color.ORANGE);
        }
        linePen=new Pen(new Color(0x7F000000, false), 2);
       
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 10OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set a new route pen.
     * @param routePen
     */
    public void setRoutePen(Pen routePen){
        if(routePen!=null){
            this.routePen=routePen;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the route direction.
     * @param newDirection
     */
    public void setMapDirection(MapDirection newDirection) {
        mapDirectionLayer.setMapDirection(newDirection);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the route direction.
     * @param newDirection
     */
    public void setMapDirections(MapDirection[] newDirections) {
        mapDirectionLayer.setMapDirections(newDirections);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the route direction.
     * @param newDirection
     */
    public MapDirection getMapDirection() {
        return mapDirectionLayer.getMapDirection();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the route direction.
     * @param newDirection
     */
    public MapDirection[] getMapDirections() {
        return mapDirectionLayer.getMapDirections();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * empty implmetation.
     * @inheritDoc
     */
    public void getImage(int mtype, int x, int y, int zoomLevel) {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the image at given x,y zoom level.
     * @param x  x index of the map tile
     * @param y  y index of the map tile.
     * @param zoomLevel zoom level of the map tile
     * @return the given image.
     */
    public IImage getImage(int x, int y, int zoomLevel) {
        MapDirection mapDirection=getMapDirection();
        if(mapDirection!=null){
            try {
                int shiftWidth=4;
                GeoPoint pt1 = new GeoPoint(x * MapLayer.MAP_TILE_WIDTH-shiftWidth,
                        y * MapLayer.MAP_TILE_WIDTH-shiftWidth);
                GeoPoint pt2 = new GeoPoint((x + 1) * MapLayer.MAP_TILE_WIDTH+shiftWidth,
                        (y + 1) * MapLayer.MAP_TILE_WIDTH+shiftWidth);
                GeoLatLng latLng1 = MapLayer.fromPixelToLatLng(pt1, zoomLevel);
                GeoLatLng latLng2 = MapLayer.fromPixelToLatLng(pt2, zoomLevel);
                double minY = Math.min(latLng1.lat(), latLng2.lat());
                double maxY = Math.max(latLng1.lat(), latLng2.lat());
                double minX = Math.min(latLng1.lng(), latLng2.lng());
                double maxX = Math.max(latLng1.lng(), latLng2.lng());
                GeoLatLngBounds geoBounds = new GeoLatLngBounds(minX, minY,
                        maxX - minX, maxY - minY);
                GeoLatLng centerPt = geoBounds.getCenter();
                mapDirectionLayer.setCenter(centerPt, zoomLevel);
                mapDirectionLayer.screenBounds=geoBounds;
                mapDirectionLayer.paint(null);
                isImagevalid = true;
                return MapLayer.getAbstractGraphicsFactory()
                  .createImage(mapDirectionLayer.getRGB(),
                  MapLayer.MAP_TILE_WIDTH, MapLayer.MAP_TILE_WIDTH);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * empty implmetation.
     * @inheritDoc
     */
    public void cancelRead() {
    }

    final MapDirectionLayer mapDirectionLayer;

    private Pen routePen;
    private final Pen linePen;
    private final Brush startRouteBrush;
    private final Brush middleRouteBrush;
    private final Brush endRouteBrush;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // --------   -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  This a map layer delicated to draw routing result.
     * <p></p>
     * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
     * @version     2.00, 03/01/09
     * @author      Guidebee Pty Ltd.
     */
   private class MapDirectionLayer extends MapLayer {


        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * Constructor.
         * @param width
         * @param height
         */
        public MapDirectionLayer(int width, int height) {
            super(width, height);
            MAP_DRAWING_TILE_WIDTH=MAP_TILE_WIDTH/MapConfiguration.mapDirectionRenderBlocks;
            if(!MapConfiguration.lowMemoryMode ){
                routeGraphics2D = new Graphics2D(MAP_DRAWING_TILE_WIDTH,
                    MAP_DRAWING_TILE_WIDTH);
            }

        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * set the route direction.
         * @param newDirection
         */
        public void setMapDirection(MapDirection newDirection) {
            if (newDirection != null) {
                currentMapDirections = new MapDirection[1];
                currentMapDirections[0] = newDirection;
            } else {
                currentMapDirections = null;
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * set the route direction.
         * @param newDirection
         */
        public void setMapDirections(MapDirection[] newDirections) {
            currentMapDirections = newDirections;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * set the route direction.
         * @param newDirection
         */
        public MapDirection getMapDirection() {
            if(currentMapDirections!=null){
                return currentMapDirections[0];
            }
            return null;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * set the route direction.
         * @param newDirection
         */
        public MapDirection[] getMapDirections() {
            return currentMapDirections;
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 06OCT2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * get the rgb array.
         * @return
         */
        int []getRGB(){
            return routeGraphics2D.getRGB();
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * @inheritDoc
         */
        public void paint(IGraphics graphics) {
            paint(graphics, 0, 0);
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * @inheritDoc
         */
        public void paint(IGraphics graphics, int offsetX, int offsetY) {
            paint(graphics, offsetX, offsetY, MAP_TILE_WIDTH, MAP_TILE_WIDTH);
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * @inheritDoc
         */
        public void paint(IGraphics graphics, int offsetX, int offsetY,
                int width, int height) {
            if (currentMapDirections != null) {
                synchronized (syncObject) {
                    drawRouteCanvas(offsetX, offsetY, width, height);
                }

            }
        }
        
        private GeoLatLngBounds screenBounds;
        private Graphics2D routeGraphics2D=null;
        private volatile MapDirection[] currentMapDirections = null;
        /**
         * SutherlandHodgman clip pline and region.
         */
        private SutherlandHodgman sutherlandHodgman = null;
        
        /**
         * When draw the map tile, the default map tile width is 64X64
         * using 64X64 istread of 256X256 mainly for saving memory usage in
         * memeory constrained devices. it can change to a bigger value if the
         * memory is not issue.
         */
        private final int MAP_DRAWING_TILE_WIDTH;

        private final static int ROUTE_ICON_WIDTH=12;


        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * check if need to show on map.
         * @param numLevel
         * @param zoomLevel
         * @return
         */
        private int needShowLevel(int numLevel, int zoomLevel) {
            int totalZoomLevel = 16;
            int mapGrade = (totalZoomLevel - zoomLevel) / numLevel - 1;
            return mapGrade;

        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *
         * @param x
         * @param y
         * @param width
         * @param height
         */
        private void drawRouteCanvas(int x, int y,
                int width, int height) {
            if (currentMapDirections != null) {
                try {
                    drawRouteImage(currentMapDirections, x, y, width, height);
                    drawRouteIcons(currentMapDirections, x, y, width, height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         *
         * @param mapDirection
         * @param x
         * @param y
         * @param width
         * @param height
         */
        private void drawRouteImage(MapDirection[] mapDirections, int x, int y,
               int width, int height) {
          

               sutherlandHodgman = new SutherlandHodgman(screenBounds);
               routeGraphics2D.clear(transparency);
               for (int k = 0; k < mapDirections.length; k++) {
                   MapDirection mapDirection = mapDirections[k];
                   Vector polyline = new Vector();
                   int minLevel = needShowLevel(mapDirection.polyline.numLevels, getZoom());
                   for (int i = 0; i < mapDirection.polyline.getVertexCount(); i++) {
                       int level = mapDirection.polyline.getLevel(i);
                       if (level >= minLevel) {
                           polyline.addElement(mapDirection.polyline.getVertex(i));
                       }
                   }
                   Vector clippedPts = sutherlandHodgman.ClipPline(polyline);

                   boolean hasPt1 = false;
                   GeoLatLng pt1 = null, pt2 = null, pt;
                   GeoPoint newPt1 = new GeoPoint(0, 0);
                   GeoPoint newPt2 = new GeoPoint(0, 0);
                   int level;
                   GeoPoint drawPt1 = new GeoPoint(0, 0), drawPt2 = new GeoPoint(0, 0);
                   int steps = 1;
                   int numOfTiles = MAP_TILE_WIDTH / MAP_DRAWING_TILE_WIDTH;
                   Rectangle drawArea = new Rectangle();
                   Rectangle intersectRect = new Rectangle(0, 0, width, height);
                   int xIndex, yIndex;
                   for (xIndex = 0; xIndex < numOfTiles; xIndex++) {
                       for (yIndex = 0; yIndex < numOfTiles; yIndex++) {
                           hasPt1 = false;
                           pt1 = null;
                           pt2 = null;
                           
                           drawArea.x = xIndex * MAP_DRAWING_TILE_WIDTH;
                           drawArea.y = yIndex * MAP_DRAWING_TILE_WIDTH;
                           drawArea.width = drawArea.height = MAP_DRAWING_TILE_WIDTH;
                           drawArea = intersectRect.intersection(drawArea);
                           int totalPointSize = clippedPts.size();
                           if (!drawArea.isEmpty()) {
                               routeGraphics2D.setClip(0, 0,
                                       drawArea.width, drawArea.height);
                               try {
                                   for (int j = 0; j < totalPointSize; j += steps) {
                                       pt = (GeoLatLng) clippedPts.elementAt(j);
                                       level = minLevel;
                                       if (hasPt1 == false) {
                                           if (level >= minLevel) {
                                               {
                                                   {
                                                       hasPt1 = true;
                                                       pt1 = pt;
                                                       continue;
                                                   }
                                               }
                                           }
                                       }
                                       if (hasPt1) {
                                           if (level >= minLevel) {
                                               pt2 = pt;
                                               newPt1 = fromLatLngToMapPixel(pt1);
                                               newPt2 = fromLatLngToMapPixel(pt2);
                                               newPt1.x -= x + xIndex * MAP_DRAWING_TILE_WIDTH;
                                               newPt1.y -= y + yIndex * MAP_DRAWING_TILE_WIDTH;
                                               newPt2.x -= x + xIndex * MAP_DRAWING_TILE_WIDTH;
                                               newPt2.y -= y + yIndex * MAP_DRAWING_TILE_WIDTH;
                                               drawPt1.x = (int) newPt1.x;
                                               drawPt1.y = (int) newPt1.y;
                                               drawPt2.x = (int) newPt2.x;
                                               drawPt2.y = (int) newPt2.y;

                                               if ((drawPt1.distance(drawPt2) > 0)) {
                                                   routeGraphics2D.drawLine(routePen, (int) drawPt1.x,
                                                           (int) drawPt1.y,
                                                           (int) drawPt2.x, (int) drawPt2.y);
                                                   hasPt1 = true;
                                                   pt1 = pt2;
                                                   if (readListener != null) {
                                                       readListener.readProgress(j,
                                                               totalPointSize);
                                                   }
                                               }
                                           }
                                       }
                                   }
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           }

                           
                       }
                   }
                   int[] rgb = routeGraphics2D.getRGB();
                   modifyAlpha(rgb, (byte) 160, transparency);
               }
    
       }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * modify alpha
         * @param arr
         * @param alpha
         * @param removeColor
         */
        private void modifyAlpha(int[] arr, byte alpha, int removeColor) {
           removeColor = removeColor & 0xffffff;
           int w = MAP_DRAWING_TILE_WIDTH;
           int h = MAP_DRAWING_TILE_WIDTH;
           int size = w * h;
           int alphaInt = (((int) alpha) << 24) & 0xff000000;
           for (int iter = 0; iter < size; iter++) {
               if ((arr[iter] & 0xff000000) != 0) {
                   arr[iter] = (arr[iter] & 0xffffff) | alphaInt;
                   if (removeColor == (0xffffff & arr[iter])) {
                       arr[iter] = 0;
                   }
               }
           }

       }

        private void drawRouteIcon(int x,int y,Brush brush){
            int[] xPoints=new int[4];
            int[] yPoints=new int[4];
            int width=ROUTE_ICON_WIDTH/2;
            xPoints[0]=x;yPoints[0]=y-width;
            xPoints[1]=x-width;yPoints[1]=y;
            xPoints[2]=x;yPoints[2]=y+width;
            xPoints[3]=x+width;yPoints[3]=y;
            Polygon polygon=new Polygon(xPoints,yPoints,4);
            routeGraphics2D.fillPolygon(brush, polygon);
            routeGraphics2D.drawPolygon(linePen, polygon);

        }

        ////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS --------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ------------------
        // 03JAN2009  James Shen                 	      Code review
        ////////////////////////////////////////////////////////////////////////
        /**
         * draw route icon in given rectangle.
         * @param mapDirection
         * @param x
         * @param y
         * @param width
         * @param height
         */
        private void drawRouteIcons(MapDirection[] mapDirections, int x, int y,
               int width, int height) {

           for (int k = 0; k < mapDirections.length; k++) {
               MapDirection mapDirection = mapDirections[k];
               GeoPoint newPt;
               GeoLatLng pt;
               routeGraphics2D.setClip(0, 0, width, height);
               for (int j = 0; j < mapDirection.routes.length; j++) {
                   for (int i = 0; i < mapDirection.routes[j].steps.length - 1; i++) {
                       MapStep mapStep = mapDirection.routes[j].steps[i];
                       pt = mapStep.lastLatLng;
                       if (screenBounds.containsLatLng(pt)) {
                           newPt = fromLatLngToMapPixel(pt);
                           newPt.x -= x;
                           newPt.y -= y;
                           drawRouteIcon((int) newPt.x, (int) newPt.y, middleRouteBrush);
                       }
                   }
               }
               pt = mapDirection.polyline.getVertex(0);
               newPt = fromLatLngToMapPixel(pt);
               newPt.x -= x;
               newPt.y -= y;
               drawRouteIcon((int) newPt.x, (int) newPt.y, startRouteBrush);
               pt = mapDirection.polyline.getVertex(mapDirection.polyline.getVertexCount() - 1);
               newPt = fromLatLngToMapPixel(pt);
               newPt.x -= x;
               newPt.y -= y;
               drawRouteIcon((int) newPt.x, (int) newPt.y, endRouteBrush);
           }

       }

        protected void drawMapCanvas() {
        }

   }

}
