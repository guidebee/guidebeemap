//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPolygon;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Class MapRegion stands for a map region object.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapRegion extends MapObject{
    
    /**
     * the pen style of the region.
     */
    public MapPen penStyle;
    
    /**
     * the brush style of the region.
     */
    public MapBrush brushStyle;
    
    /**
     * the geo information for the region object.
     */
    public GeoPolygon region;
    
    /**
     * the center of the region.
     */
    public GeoLatLng centerPt;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param region     map object copy from.
     */
    public MapRegion(MapRegion region) {
        super(region);
        setType(MapObject.REGION);
        penStyle = new MapPen(region.penStyle);
        brushStyle=new MapBrush(region.brushStyle);
        this.region=new GeoPolygon(region.region);
        this.centerPt=new GeoLatLng(region.centerPt);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public MapRegion() {
        super();
        setType(MapObject.REGION);
        penStyle = new MapPen();
        brushStyle=new MapBrush();
        this.centerPt=new GeoLatLng();
        this.region=null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the pen type of the map region.
     * @return the pen type
     */
    public MapPen getPenType() {
        return penStyle;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the pen type of the map point.
     * @param mapPen the pen type
     */
    public void setPenType(MapPen mapPen) {
        penStyle = mapPen;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the brush type of the map region.
     * @return the brush type
     */
    public MapBrush getBrushType() {
        return brushStyle;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the brush type of the map region.
     * @param mapBrush the brush type
     */
    public void setPenType(MapBrush mapBrush) {
        brushStyle = mapBrush;
    }
   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the GeoPolygon of the map Region.
     * @return the GeoPolygon object.
     */
    public GeoPolygon getRegion() {
        return region;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set GeoPolygon of the map Region.
     * @param region  the GeoPolygon object.
     */
    public void setRegion(GeoPolygon region) {
        this.region=region;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert to MapInfo String.
     * @return a MapInfo MIF string.
     */
    public String toString(){
        String retStr="REGION 1" + CRLF;
        retStr+="\t"+region.getVertexCount()+CRLF;
        for(int i=0;i<region.getVertexCount();i++){
            GeoLatLng latLng=region.getVertex(i);
            retStr+=latLng.x + " " + latLng.y + CRLF;
        }
        retStr+="\t" + "PEN(" + penStyle.width +"," + penStyle.pattern +","
                +penStyle.color + ")" + CRLF;
        retStr+="\t" + "BRUSH(" + brushStyle.pattern +"," + brushStyle.foreColor +","
                +brushStyle.backColor + ")" + CRLF;
        retStr+="\tCENTER "+centerPt.x + " " +centerPt.y +CRLF;
        return retStr;
    }
}
