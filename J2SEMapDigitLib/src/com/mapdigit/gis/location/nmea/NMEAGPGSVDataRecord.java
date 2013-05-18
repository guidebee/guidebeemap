//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location.nmea;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Vector;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * NEMA Data record for GPGSV sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEAGPGSVDataRecord extends NMEADataRecord{

    /**
     * Total number of satellites in view.
     */
    public int numberOfSatelltes;
    
    /**
     * Satellite info.
     */
    public Vector satellites=new Vector();
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     */
    public NMEAGPGSVDataRecord() {
        recordType=TYPE_GPGSV;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return string of GPGSV.
     * @return a string
     */
    public String toString(){
        String ret="Number of Satellte:" + numberOfSatelltes +"\n";
        for(int i=0;i<satellites.size();i++){
            ret+=satellites.elementAt(i).toString() +"\n";
        }
        return ret;

    }
}
