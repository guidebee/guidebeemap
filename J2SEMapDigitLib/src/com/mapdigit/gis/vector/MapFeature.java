//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapObject;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapFeature defines a map feature in a map layer.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public class MapFeature {
    
    /**
     * The MapInfo ID of this feature.
     */
    public int mapInfoID=-1;
    
    /**
     * The geo infomation for this map feature.
     */
    public MapObject mapObject=null;
    
    /**
     * the tabular information for this map feature.
     */
    public DataRowValue dataRowValue=null;
    
    /**
     * Carriage return.
     */
    protected static final String CRLF="\n";
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor. MapFeature cannot be instantiated directly.
     */
    MapFeature(){
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert to String.
     * @return a  string.
     */
    public String toString(){
        String ret="MapInfo ID:" + mapInfoID + "\tName:"+ mapObject.name+ CRLF;
        ret+=dataRowValue.toString()+ CRLF;
        ret+=mapObject.toString();
        return ret;
        
    }
}
