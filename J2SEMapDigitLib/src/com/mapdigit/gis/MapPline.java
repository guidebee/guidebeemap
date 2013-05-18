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
import com.mapdigit.gis.geometry.GeoPolyline;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Class MapPline stands for a map pline object.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapPline extends MapObject{
    
    /**
     * the pen style of the pline.
     */
    public MapPen penStyle;
    
    /**
     * the geo information for the pline object.
     */
    public GeoPolyline pline;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param pline     map object copy from.
     */
    public MapPline(MapPline pline) {
        super(pline);
        setType(MapObject.PLINE);
        penStyle = new MapPen(pline.penStyle);
        this.pline=new GeoPolyline(pline.pline);
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
    public MapPline() {
        super();
        setType(MapObject.PLINE);
        penStyle = new MapPen();
        this.pline=null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the pen type of the map pline.
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
     * Get the GeoPolyline of the map Pline.
     * @return the GeoPolyline object.
     */
    public GeoPolyline getPline() {
        return pline;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set GeoPolyline of the map Pline.
     * @param pline  the GeoPolyline object.
     */
    public void setPline(GeoPolyline pline) {
        this.pline=pline;
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
        String retStr="PLINE";
        retStr+="  "+pline.getVertexCount()+CRLF;
        for(int i=0;i<pline.getVertexCount();i++){
            GeoLatLng latLng=pline.getVertex(i);
            retStr+=latLng.x + " " + latLng.y + CRLF;
        }
        retStr+="\t" + "PEN(" + penStyle.width +"," + penStyle.pattern +","
                +penStyle.color + ")" + CRLF;
        return retStr;
    }
}
