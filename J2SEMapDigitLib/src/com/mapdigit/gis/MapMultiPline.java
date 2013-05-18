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
import com.mapdigit.gis.geometry.GeoPolyline;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Class MapMultiPline stands for map plines¡¯ collection.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapMultiPline extends MapObject{
    
    /**
     * the pen style of the pline.
     */
    public MapPen penStyle;
    
    /**
     * the geo information for the pline object.
     */
    public GeoPolyline[] plines;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param multiPline     map object copy from.
     */
    public MapMultiPline(MapMultiPline multiPline) {
        super(multiPline);
        setType(MapObject.MULTIPLINE);
        penStyle = new MapPen(multiPline.penStyle);
        this.plines=new GeoPolyline[multiPline.plines.length];
        for(int i=0;i<this.plines.length;i++){
           this.plines[i]=new GeoPolyline(multiPline.plines[i]);
        }
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
    public MapMultiPline() {
        super();
        setType(MapObject.MULTIPLINE);
        penStyle = new MapPen();
        this.plines=null;
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
     * Get the GeoPolyline of the map MultiPline.
     * @return the GeoPolyline object.
     */
    public GeoPolyline[] getPlines() {
        return plines;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set GeoPolyline array of the map MultiPline.
     * @param plines  the GeoPolyline object array.
     */
    public void setPlines(GeoPolyline[] plines) {
        this.plines=plines;
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
        String retStr="PLINE  MULTIPLE  ";
        retStr+=plines.length+CRLF;
        for(int j=0;j<plines.length;j++){
            retStr+="  "+plines[j].getVertexCount()+CRLF;
            for(int i=0;i<plines[j].getVertexCount();i++){
                GeoLatLng latLng=plines[j].getVertex(i);
                retStr+=latLng.x + " " + latLng.y + CRLF;
            }
        }
        retStr+="\t" + "PEN(" + penStyle.width +"," + penStyle.pattern +","
                +penStyle.color + ")" +CRLF;
        return retStr;
    }
}
