//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------

import java.util.Vector;

import com.mapdigit.drawing.Color;
import com.mapdigit.drawing.Graphics2D;
import com.mapdigit.drawing.Pen;
import com.mapdigit.drawing.SolidBrush;
import com.mapdigit.drawing.TextureBrush;
import com.mapdigit.drawing.geometry.Polygon;
import com.mapdigit.drawing.geometry.Polyline;
import com.mapdigit.drawing.geometry.Rectangle;
import com.mapdigit.gis.MapBrush;
import com.mapdigit.gis.MapCollection;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.MapMultiPline;
import com.mapdigit.gis.MapMultiPoint;
import com.mapdigit.gis.MapMultiRegion;
import com.mapdigit.gis.MapObject;
import com.mapdigit.gis.MapPen;
import com.mapdigit.gis.MapPline;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.MapRegion;
import com.mapdigit.gis.MapText;

import com.mapdigit.gis.drawing.IFont;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.geometry.GeoPolygon;
import com.mapdigit.gis.geometry.GeoPolyline;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to draw vector map objects.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public class VectorMapCanvas {

    /**
     * Shared graphics2D instance used to drawing map objects.
     */
    public static Graphics2D SHARED_GRAPHICS2D=null;
    
    /**
     * Graphics2D mutex.
     */
    public static final Object graphic2DMutex = new Object();

    /**
     * default font color.
     */
    public int fontColor=0x000000;


    public IFont font=null;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the font color for this map canvas.
     * @param fontColor the font color.
     */
    public void setFontColor(int fontColor){
        this.fontColor=fontColor;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the font for the map canvas.
     * @param font the font the map canvas.
     */
    public void setFont(IFont font){
        this.font=font;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the graphics2D instance. the graphics 2D shall be protected by
     * graphic2DMutex to support multi-threading.
     * @return the shared the graphics2D objects.
     */
    public static Graphics2D getGraphics2DInstance() {
        synchronized (graphic2DMutex) {
            if (SHARED_GRAPHICS2D == null) {
                SHARED_GRAPHICS2D = new Graphics2D(MapLayer.MAP_TILE_WIDTH,
                        MapLayer.MAP_TILE_WIDTH);
            }
        }
        return SHARED_GRAPHICS2D;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Construtor.
     */
    public VectorMapCanvas() {
        getGraphics2DInstance();
        textImage=MapLayer.getAbstractGraphicsFactory()
                .createImage(MapLayer.MAP_TILE_WIDTH,
                IMAGE_PATERN_WIDTH);
        textGraphics=textImage.getGraphics();
        textGraphics.setColor(0x00FF00FF);
        textGraphics.fillRect(0,0,textImage.getWidth(),textImage.getHeight());
        fontTranspency=textImage.getRGB()[0];
        imagePattern=MapLayer.getAbstractGraphicsFactory()
                .createImage(IMAGE_PATERN_WIDTH,IMAGE_PATERN_WIDTH);
        imagePatternGraphics=imagePattern.getGraphics();
        mapSize.x = 0; mapSize.y = 0;
        mapSize.width = MapLayer.MAP_TILE_WIDTH;
        mapSize.height = MapLayer.MAP_TILE_WIDTH;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the pixel coordinates of the given geographical point in the map.
     * @param latlng the geographical coordinates.
     * @return the pixel coordinates in the map.
     */
    public GeoPoint fromLatLngToMapPixel(GeoLatLng latlng) {
        GeoPoint center = MapLayer.fromLatLngToPixel(mapCenterPt, mapZoomLevel);
        GeoPoint topLeft = new GeoPoint(center.x - mapSize.width / 2.0,
                center.y - mapSize.height / 2.0);
        GeoPoint pointPos = MapLayer.fromLatLngToPixel(latlng, mapZoomLevel);
        pointPos.x -= topLeft.x;
        pointPos.y -= topLeft.y;
        return new GeoPoint((int) (pointPos.x + 0.5), (int) (pointPos.y + 0.5));

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the pixel coordinates of the given geographical point vector
     * in the map.
     * @param vpts the geographical coordinates vector.
     * @return the pixel coordinates in the map.
     */
    public GeoPoint[] fromLatLngToMapPixel(Vector vpts) {

        GeoPoint[] retPoints = new GeoPoint[vpts.size()];
        for (int i = 0; i < vpts.size(); i++) {
            retPoints[i] = fromLatLngToMapPixel(
                    (GeoLatLng) vpts.elementAt(i));
        }
        return retPoints;

    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the RGB array of the map canvas.
     * @return the rgb array of the map cavas.
     */
    public int[] getRGB() {
        if (SHARED_GRAPHICS2D != null) {
            return SHARED_GRAPHICS2D.getRGB();
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
     * draw a point.
     * @param mapPoint a map point object.
     */
    public void drawPoint(MapPoint mapPoint) {
        GeoPoint screenPt=fromLatLngToMapPixel(mapPoint.point);
        SolidBrush brush=new SolidBrush(new Color(mapPoint.symbolType.color));
        SHARED_GRAPHICS2D.fillRectangle(brush, new Rectangle((int)screenPt.x-2,
                (int)screenPt.y-2,4,4));

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a pline.
     * @param mapPen the pen used to draw the polyline
     * @param pline the polyline object.
     */
    public void drawPline(MapPen mapPen, GeoPolyline pline) {
        Vector clippedPts = sutherlandHodgman.ClipPline(pline.getPoints());
        GeoPoint[] screenPts = fromLatLngToMapPixel(clippedPts);
        if (screenPts.length > 1) {
            {
                int penWidth = mapPen.width;
                if (mapPen.pattern > 62) {
                    penWidth = mapPen.width * 2;
                }
                Pen pen = new Pen(new Color(mapPen.color), penWidth);
                SHARED_GRAPHICS2D.setDefaultPen(pen);
                int[] xpoints = new int[screenPts.length];
                int[] ypoints = new int[screenPts.length];
                for (int i = 0; i < screenPts.length; i++) {
                    xpoints[i] = (int) screenPts[i].x;
                    ypoints[i] = (int) screenPts[i].y;

                }
                Polyline polyline = new Polyline();
                polyline.xpoints = xpoints;
                polyline.ypoints = ypoints;
                polyline.npoints = xpoints.length;

                SHARED_GRAPHICS2D.drawPolyline(null, polyline);
            }

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a region.
     * @param mapPen  pen for the border of the region.
     * @param mapBrush brush to fill the region.
     * @param region the polygon object.
     */
    public void drawRegion(MapPen mapPen, MapBrush mapBrush, GeoPolygon region) {
        Pen pen = new Pen(new Color(mapPen.color), mapPen.width);
        TextureBrush brush = getImagePatternBrush(mapBrush);
        Vector clippedPts = sutherlandHodgman.ClipRegion(region.getPoints());
        GeoPoint[] screenPts = fromLatLngToMapPixel(clippedPts);

        if (screenPts.length > 2) {
            {
                int[] xpoints = new int[screenPts.length];
                int[] ypoints = new int[screenPts.length];
                for (int i = 0; i < screenPts.length; i++) {
                    xpoints[i] = (int) screenPts[i].x;
                    ypoints[i] = (int) screenPts[i].y;

                }
                Polygon polygon = new Polygon();
                polygon.xpoints = xpoints;
                polygon.ypoints = ypoints;
                polygon.npoints = xpoints.length;

                if (mapBrush.pattern != 1) {
                    SHARED_GRAPHICS2D.setPenAndBrush(pen, brush);
                    SHARED_GRAPHICS2D.drawPolygon(null, polygon);
                    SHARED_GRAPHICS2D.fillPolygon(null, polygon);
                } else {
                    SHARED_GRAPHICS2D.setDefaultPen(pen);
                    SHARED_GRAPHICS2D.drawPolygon(null, polygon);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a text.
     * @param mapText a map text object.
     */
    public void drawText(MapText mapText) {
        {
            fontColor = mapText.foreColor;
            drawString(font, mapText.textString,
                    (int) mapText.point.x,
                    (int) mapText.point.y);
        }
    }



    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a texture brush based on region's brush attributes.
     * @param brush the map brush object.
     * @return the texture brush to used for the map brush.
     */
    public TextureBrush getImagePatternBrush(MapBrush brush) {
        TextureBrush textureBrush = null;
        int alpha = 255;
        switch (brush.pattern) {
            case 1:
                break;
            case 2:
                imagePatternGraphics.setColor(brush.foreColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                break;
            case 3:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                imagePatternGraphics.setColor(brush.backColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                imagePatternGraphics.setColor(brush.foreColor);
                for (int i = 0; i < 4; i++) {
                    imagePatternGraphics.drawLine(0, i * 4, IMAGE_PATERN_WIDTH, i * 4);
                }
                break;
            case 4:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                imagePatternGraphics.setColor(brush.backColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                imagePatternGraphics.setColor(brush.foreColor);
                for (int i = 0; i < 4; i++) {
                    imagePatternGraphics.drawLine(i * 4, 0, i * 4, IMAGE_PATERN_WIDTH);
                }
                break;
            case 5:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
                imagePatternGraphics.setColor(brush.backColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                imagePatternGraphics.setColor(brush.foreColor);
                for (int i = 0; i < 8; i++) {
                    imagePatternGraphics.drawLine(0, i * 4, i * 4, 0);
                }
                break;
            case 6:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                imagePatternGraphics.setColor(brush.backColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                imagePatternGraphics.setColor(brush.foreColor);
                for (int i = 0; i < 8; i++) {
                    imagePatternGraphics.drawLine(0, IMAGE_PATERN_WIDTH - i * 4, i * 4, 0);
                }
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 48:
            case 49:
            case 50:
            case 51:
            case 53:
                alpha = 128;
                imagePatternGraphics.setColor(brush.backColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                imagePatternGraphics.setColor(brush.foreColor);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        imagePatternGraphics.fillRect(i * 4, j * 4, 1, 1);
                    }
                }
                break;
            default:
                imagePatternGraphics.setColor(brush.backColor);
                imagePatternGraphics.fillRect(0, 0, IMAGE_PATERN_WIDTH,
                        IMAGE_PATERN_WIDTH);
                imagePatternGraphics.setColor(brush.foreColor);
                for (int i = 0; i < 4; i++) {
                    imagePatternGraphics.drawLine(0, i * 4, IMAGE_PATERN_WIDTH, i * 4);
                    imagePatternGraphics.drawLine(i * 4, 0, i * 4, IMAGE_PATERN_WIDTH);
                }


                break;

        }

        int[] rgbData =imagePattern.getRGB();
        textureBrush = new TextureBrush(rgbData, IMAGE_PATERN_WIDTH, IMAGE_PATERN_WIDTH);
        return textureBrush;
    }


    
    /**
     * current map zoom level
     */
    protected volatile int mapZoomLevel = 1;

    /**
     * the center of this map.
     */
    protected volatile GeoLatLng mapCenterPt = new GeoLatLng();


   /**
     * the size of the map size.
     */
    protected volatile GeoBounds mapSize = new GeoBounds();

    /**
     * SutherlandHodgman clip pline and region.
     */
    protected SutherlandHodgman sutherlandHodgman = null;

    /**
     * pattern Image;
     */
    protected IImage imagePattern;
    /**
     * draw the image pattern.
     */
    protected IGraphics imagePatternGraphics = null;
    /**
     * used to show text on the image.
     */
    protected final Vector mapNameHolder = new Vector();


    /**
     * image used to draw char with system fonts.
     */
    private final IImage textImage;

    /**
     * graphics used to draw char with system fonts.
     */
    private final IGraphics textGraphics;


    private final int fontTranspency;
    /**
     * defautl imagePattern size;
     */
    protected static final int IMAGE_PATERN_WIDTH = 16;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a map object.
     * @param mapObject the map object to be drawing.
     * @param drawBoundary the drawing boundry.
     * @param zoomLevel the current zoomLevel.
     */
    void drawMapObject(MapObject mapObject, GeoLatLngBounds drawBoundary,
            int zoomLevel) {
            GeoLatLng drawPt = new GeoLatLng();
            sutherlandHodgman = new SutherlandHodgman(drawBoundary);
            mapZoomLevel = zoomLevel;
            mapCenterPt.x = drawBoundary.getCenterX();
            mapCenterPt.y = drawBoundary.getCenterY();
            boolean pointFound = false;
            switch (mapObject.getType()) {
                case MapObject.NONE:
                    break;
                case MapObject.POINT:
                     {
                        MapPoint mapPoint = (MapPoint) mapObject;
                        drawPoint(mapPoint);
                        drawPt.x = mapPoint.point.x;
                        drawPt.y = mapPoint.point.y;
                        pointFound = true;
                    }
                    break;
                case MapObject.MULTIPOINT:
                     {
                        MapMultiPoint mapMultiPoint = (MapMultiPoint) mapObject;
                        for (int i = 0; i < mapMultiPoint.points.length; i++) {
                            MapPoint mapPoint=new MapPoint();
                            mapPoint.symbolType=mapMultiPoint.symbolType;
                            mapPoint.point=new GeoLatLng(mapMultiPoint.points[i]);
                            drawPoint(mapPoint);
                        }
                        for (int i = 0; i < mapMultiPoint.points.length; i++) {
                            if (drawBoundary.contains(mapMultiPoint.points[i])) {
                                drawPt.x = mapMultiPoint.points[i].x;
                                drawPt.y = mapMultiPoint.points[i].y;
                                pointFound = true;
                                break;
                            }
                        }

                    }
                    break;
                case MapObject.PLINE:
                     {
                        MapPline mapPline = (MapPline) mapObject;
                        drawPline(mapPline.penStyle, mapPline.pline);
                        for (int i = 0; i < mapPline.pline.getVertexCount(); i++) {
                            if (drawBoundary.contains(mapPline.pline.getVertex(i))) {
                                drawPt.x = mapPline.pline.getVertex(i).x;
                                drawPt.y = mapPline.pline.getVertex(i).y;
                                pointFound = true;
                                break;
                            }
                        }
                    }
                    break;
                case MapObject.MULTIPLINE:
                     {
                        MapMultiPline mapMultiPline = (MapMultiPline) mapObject;
                        for (int i = 0; i < mapMultiPline.plines.length; i++) {
                            drawPline(mapMultiPline.penStyle,
                                    mapMultiPline.plines[i]);
                            for (int j = 0; j < mapMultiPline.plines[i].getVertexCount(); j++) {
                                if (drawBoundary.contains(mapMultiPline.plines[i].getVertex(j))) {
                                    drawPt.x = mapMultiPline.plines[i].getVertex(j).x;
                                    drawPt.y = mapMultiPline.plines[i].getVertex(j).y;
                                    pointFound = true;
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case MapObject.REGION:
                     {
                        MapRegion mapRegion = (MapRegion) mapObject;
                        drawRegion(mapRegion.penStyle, mapRegion.brushStyle,
                                mapRegion.region);
                        drawPt.x = mapRegion.centerPt.x;
                        drawPt.y = mapRegion.centerPt.y;
                        pointFound = true;
                    }
                    break;
                case MapObject.MULTIREGION:
                     {
                        MapMultiRegion mapMultiRegion = (MapMultiRegion) mapObject;
                        for (int i = 0; i < mapMultiRegion.regions.length; i++) {
                            drawRegion(mapMultiRegion.penStyle,
                                    mapMultiRegion.brushStyle,
                                    mapMultiRegion.regions[i]);

                        }
                        drawPt.x = mapMultiRegion.centerPt.x;
                        drawPt.y = mapMultiRegion.centerPt.y;
                        pointFound = true;
                    }
                    break;
                case MapObject.COLLECTION:
                     {
                        MapCollection mapCollection = (MapCollection) mapObject;
                        if (mapCollection.multiRegion != null) {
                            MapMultiRegion mapMultiRegion = mapCollection.multiRegion;
                            for (int i = 0; i < mapMultiRegion.regions.length; i++) {
                                drawRegion(mapMultiRegion.penStyle,
                                        mapMultiRegion.brushStyle,
                                        mapMultiRegion.regions[i]);
                            }
                        }
                        if (mapCollection.multiPline != null) {
                            MapMultiPline mapMultiPline = mapCollection.multiPline;
                            for (int i = 0; i < mapMultiPline.plines.length; i++) {
                                drawPline(mapMultiPline.penStyle,
                                        mapMultiPline.plines[i]);
                            }
                        }
                        if (mapCollection.multiPoint != null) {
                            MapMultiPoint mapMultiPoint = mapCollection.multiPoint;
                            for (int i = 0; i < mapMultiPoint.points.length; i++) {
                                MapPoint mapPoint=new MapPoint();
                                mapPoint.symbolType=mapMultiPoint.symbolType;
                                mapPoint.point=new GeoLatLng(mapMultiPoint.points[i]);
                                drawPoint(mapPoint);
                            }
                        }
                        pointFound = true;
                        drawPt.x = mapCollection.bounds.x + mapCollection.bounds.width / 2;
                        drawPt.y = mapCollection.bounds.y + mapCollection.bounds.height / 2;

                    }
                    break;
                case MapObject.TEXT:
                     {
                        MapText mapText = (MapText) mapObject;
                        drawPt.x = mapText.point.x;
                        drawPt.y = mapText.point.y;
                        pointFound = true;
                    }
                    break;
            }
            if(!mapObject.name.equalsIgnoreCase("Unknown") && pointFound ){
            MapText mapName=new MapText();
            mapName.font=font;
            mapName.setForeColor(fontColor);
            mapName.textString=mapObject.name;
            GeoPoint screenPt=fromLatLngToMapPixel(drawPt);
            mapName.point.x=screenPt.x;
            mapName.point.y=screenPt.y;
            mapName.bounds.x= mapName.point.x;
            mapName.bounds.y= mapName.point.y;
            if(this.font!=null){
                mapName.bounds.height=IMAGE_PATERN_WIDTH;
                mapName.bounds.width=font.charsWidth(mapObject.name.toCharArray(),0,
                        mapObject.name.toCharArray().length );

            }
            addMapName(mapName);

        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clear the map canvas with given color.
     * @param color the color to fill the whole map canvas.
     */
    void clearCanvas(Color color) {
        if (SHARED_GRAPHICS2D != null) {
            SHARED_GRAPHICS2D.clear(color);
            mapNameHolder.removeAllElements();
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw the map text.
     */
    void drawMapText(){
        for(int i=0;i<mapNameHolder.size();i++){
                MapText mapText=(MapText)mapNameHolder.elementAt(i);
                if(font!=null){
                    drawText(mapText);
                }
            }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * add a map text to the drawing list.
     * @param mapText 
     */
    protected void addMapName(MapText mapText) {
        GeoLatLngBounds mapTextBounds = mapText.bounds;
        for (int i = 0; i < mapNameHolder.size(); i++) {
            GeoLatLngBounds storedMapTextBounds =
                    ((MapText) mapNameHolder.elementAt(i)).bounds;
            if (storedMapTextBounds.intersects(mapTextBounds)) {
                return;
            }
        }
        if(mapSize.contains(mapTextBounds)) {
            mapNameHolder.addElement(mapText);
        }
    }


    
    
    /////////////////////////////////////////////////////////////////////////////
   //--------------------------------- REVISIONS -------------------------------
   // Date       Name                 Tracking #         Description
   // ---------  -------------------  -------------      -----------------------
   // 03JAN2009  James Shen                 	          Code review
   /////////////////////////////////////////////////////////////////////////////
   /**
    * Draws the specified characters using the given font.
    * The offset and length parameters must specify a valid range of characters
    * within the character array data. The offset parameter must be within the
    * range [0..(data.length)], inclusive. The length parameter must be a
    * non-negative integer such that (offset + length) <= data.length.
    * @param font the font object.
    * @param str the array of characters to be drawn.
    * @param x the x coordinate of the anchor point.
    * @param y the y coordinate of the anchor point.
    */
   protected void drawString(IFont font,String str, int x, int y){
       synchronized (textGraphics) {
           textGraphics.setColor(0x00FF00FF);
           textGraphics.fillRect(0, 0, textImage.getWidth(), textImage.getHeight());
           textGraphics.setFont(font);
           textGraphics.setColor(fontColor);
           textGraphics.drawString(str, 0, 0);
           int charWidth = MapLayer.MAP_TILE_WIDTH;
           int[] txtRGB = textImage.getRGB();

           SHARED_GRAPHICS2D.drawImage(txtRGB, charWidth,
                   IMAGE_PATERN_WIDTH, x, y, fontTranspency);
       }
   }

    
}
