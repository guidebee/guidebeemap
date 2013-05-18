//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21AUG2009  James Shen                 	          Code review
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
// 21AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Objects of this class store information about a single step within a route
 * in a directions result.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/08/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapStep extends MapObject{


    /**
     * First point of the step.
     */
    public GeoLatLng firstLatLng;
    
    /**
     * the index of the first point in the polyline of given direciton.
     */
    public int firstLocationIndex;
    
    /**
     * last point of the step.
     */
    public GeoLatLng lastLatLng;
    
    /**
     * the index of the last point in the polyline of given direciton.
     */
    public int lastLocationIndex;
    
    /**
     * Description about this step.
     */
    public String description;

    /**
     * Description about this step in English.
     */
    public String descriptionEnglish;

    /**
     * total duration of the step in seconds.
     */
    public double duration;
    
    /**
     * total distance of the step in meters.
     */
    public double distance;

    /**
     * bearing [0-360)
     */
    public int bearing;

    /**
     * Direction type.
     */
    public MapDirectionCommandType calculatedDirectionType;

    /**
     * Direction command elements used to navigaion.
     */
    public MapDirectionCommandElement[] directionCommandElements;


    /**
     * current road name.
     */
    public String currentRoadName;

    /**
     * tag related to this map steps ,like image or else.
     */
    public Object tag;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    MapStep() {
        calculatedDirectionType=new MapDirectionCommandType(MapDirectionCommandType.COMMAND_INVALID);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 31DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return direction command type if has any.
     * @return the direction command type.
     */
    public int getDirectionCommandType(){
        try{
            return directionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                    .directionCommandType.type;
        }catch(Exception e){}
        return MapDirectionCommandType.COMMAND_INVALID;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Debug .
     */
    public String toString() {
        StringBuffer sb=new StringBuffer();
        sb.append("Current road name:" + currentRoadName+"\r\n");
        sb.append("Description:" + description+"\r\n");
        sb.append("Distance:" + distance+"\r\n");
        sb.append("Duration:" + duration+"\r\n");
        sb.append("Bearing:" + bearing);
        sb.append("Calculated Direction:" + calculatedDirectionType.toString()+"\r\n");

        if(directionCommandElements[2]!=null){
            sb.append("Analysed Direction:"
                    + directionCommandElements[2]
                    .directionCommandType.toString()+"\r\n");
        }
        if (directionCommandElements != null) {
            for (int i = 0; i < directionCommandElements.length; i++) {
                if (directionCommandElements[i] != null) {
                    sb.append(directionCommandElements[i].description + "-"+"\r\n");
                } else {
                    sb.append(" " + "-"+"\r\n");
                }
            }
            sb.append("\r\n");
        }

        return sb.toString();
    }
    
    
}
