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
import com.mapdigit.gis.drawing.IFont;
import com.mapdigit.gis.geometry.GeoLatLng;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  Class MapText stands for a text map object.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapText extends MapObject {
    
    /**
     * default font 
     */
    public IFont font=null;
    
    /**
     * The angle of the map text.
     */
    public int angle;
    
    /**
     * The location of the map text.
     */
    public GeoLatLng point;
    
    /**
     * The back color of the map text.
     */
    public int backColor;
    
    /**
     * The fore color of the map text.
     */
    public int foreColor;
    
    /**
     * The justification of the map text.
     */
    public int justification;
    
    /**
     * The spacing of the map text.
     */
    public int spacing;
    
    /**
     * The lineType of the map text.
     */
    public int lineType;
    
    /**
     * the text string of the text.
     */
    public String textString;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param mapText     map object copy from.
     */
    public MapText(MapText mapText) {
        super(mapText);
        setType(MapObject.TEXT);
        this.point=new GeoLatLng(mapText.point);
        this.angle=mapText.angle;
        this.backColor=mapText.backColor;
        this.foreColor=mapText.foreColor;
        this.justification=mapText.justification;
        this.spacing=mapText.spacing;
        this.lineType=mapText.lineType;
        this.textString=mapText.textString;
        this.font=mapText.font;
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
    public MapText() {
        super();
        setType(MapObject.TEXT);
        this.point=new GeoLatLng();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the angle of the map text.
     * @param angle the map text.
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the angle of the map text.
     * @return the angle of the map text.
     */
    public int getAngle() {
        return angle;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the back color of the map text.
     * @param backColor color the map text.
     */
    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the back color of the map text.
     * @return the back color of the map text.
     */
    public int getBackColor() {
        return backColor;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the fore color of the map text.
     * @param foreColor fore color the map text.
     */
    public void setForeColor(int foreColor) {
        this.foreColor = foreColor;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the foreColor of the map text.
     * @return the foreColor of the map text.
     */
    public int getForeColor() {
        return foreColor;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the justification of the map text.
     * @param justification the map text.
     */
    public void setJustification(int justification) {
        this.justification = justification;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the justification of the map text.
     * @return the justification of the map text.
     */
    public int getJustification() {
        return justification;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the spacing of the map text.
     * @param spacing the map text.
     */
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the spacing of the map text.
     * @return the spacing of the map text.
     */
    public int getSpacing() {
        return spacing;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the lineType of the map text.
     * @param lineType the map text.
     */
    public void setLineType(int lineType) {
        this.lineType = lineType;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the lineType of the map text.
     * @return the lineType of the map text.
     */
    public int getLineType() {
        return lineType;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the textString of the map text.
     * @param textString the map text.
     */
    public void setTextString(String textString) {
        this.textString = textString;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the textString of the map text.
     * @return the textString of the map text.
     */
    public String getTextString() {
        return textString;
    }
    
   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the location of the map point.
     * @return the location
     */
    public GeoLatLng getPoint() {
        return point;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the location of the map point.
     * @param p  the location
     */
    public void setPoint(GeoLatLng p) {
        point = new GeoLatLng(p);
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
        String retStr="TEXT  ";
        retStr+="\"" + textString + "\"" + CRLF;
        
        retStr+=bounds.getMinX() + " " + bounds.getMinY() + " " +
                bounds.getMaxX() + " " + bounds.getMaxY()+CRLF;
        
        return retStr;
    }
    
}
