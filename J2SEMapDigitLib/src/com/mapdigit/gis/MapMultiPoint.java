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
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Class MapMultiPoint stands for a map points collection.
 * <p></p>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/08
 * @author      Guidebee Pty Ltd.
 */
public final class MapMultiPoint extends MapObject{
    
    /**
     * The symbol type of the map point.
     */
    public MapSymbol symbolType;
    
    /**
     * The location of the map point.
     */
    public GeoLatLng[] points;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param mapPoints     map object copy from.
     */
    public MapMultiPoint(MapMultiPoint mapPoints) {
        super(mapPoints);
        setType(MapObject.MULTIPOINT);
        symbolType = new MapSymbol(mapPoints.symbolType);
        this.points=new GeoLatLng[mapPoints.points.length];
        for(int i=0;i<points.length;i++){
          this.points[i]=new GeoLatLng(mapPoints.points[i]);  
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor.
     */
    public MapMultiPoint() {
        super();
        setType(MapObject.MULTIPOINT);
        symbolType = new MapSymbol();
        this.points=null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the symbol type of the map point.
     * @return the symbol type
     */
    public MapSymbol getSymbolType() {
        return symbolType;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the symbol type of the map point.
     * @param symbol the symbol type
     */
    public void setSymbolType(MapSymbol symbol) {
        symbolType = symbol;
    }
   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the location of the map points.
     * @return the location array.
     */
    public GeoLatLng[] getPoints() {
        return points;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the location of the map points.
     * @param pts  the location
     */
    public void setPoint(GeoLatLng[] pts) {
        this.points=pts;
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
        String retStr="MULTIPOINT    ";
        retStr+=points.length + CRLF;
        for(int i=0;i<points.length;i++) {
            retStr+=points[i].x + " " +points[i].y +CRLF;
        }
        retStr+="\t" + "SYMBOL(" + symbolType.shape +"," + symbolType.color +","
                +symbolType.size + ")" +CRLF;
        return retStr;
    }
}
