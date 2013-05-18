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
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPolygon;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Class MapMultiRegion stands for map regions' collection.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapMultiRegion extends MapObject{
    
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
    public GeoPolygon[] regions;
    
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
     * @param multiRegion     map object copy from.
     */
    public MapMultiRegion(MapMultiRegion multiRegion) {
        super(multiRegion);
        setType(MapObject.MULTIREGION);
        penStyle = new MapPen(multiRegion.penStyle);
        brushStyle=new MapBrush(multiRegion.brushStyle);
        this.regions=new GeoPolygon[multiRegion.regions.length];
        for(int i=0;i<this.regions.length;i++){
           this.regions[i]=new  GeoPolygon(multiRegion.regions[i]);
        }
        this.centerPt=new GeoLatLng(multiRegion.centerPt);
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
    public MapMultiRegion() {
        super();
        setType(MapObject.MULTIREGION);
        penStyle = new MapPen();
        brushStyle=new MapBrush();
        this.centerPt=new GeoLatLng();
        this.regions=null;
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
    public GeoPolygon[] getRegions() {
        return regions;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set GeoPolygon array of the map Region.
     * @param regions  the GeoPolygon object array.
     */
    public void setRegions(GeoPolygon[] regions) {
        this.regions=regions;
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
        String retStr="REGION ";
        retStr+=regions.length+CRLF;
        for(int j=0;j<regions.length;j++){
            retStr+="  "+regions[j].getVertexCount()+CRLF;
            for(int i=0;i<regions[j].getVertexCount();i++){
                GeoLatLng latLng=regions[j].getVertex(i);
                retStr+=latLng.x + " " + latLng.y + CRLF;
            }
        }
        retStr+="\t" + "PEN(" + penStyle.width +"," + penStyle.pattern +","
                +penStyle.color + ")" + CRLF;
        retStr+="\t" + "BRUSH(" + brushStyle.pattern +"," + brushStyle.foreColor +","
                +brushStyle.backColor + ")" + CRLF;
        retStr+="\tCENTER "+centerPt.x + " " +centerPt.y +CRLF;
        return retStr;
    }
}
