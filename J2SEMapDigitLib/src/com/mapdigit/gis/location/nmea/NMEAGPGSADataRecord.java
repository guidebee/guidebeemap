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

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * NEMA Data record for GPGSA sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEAGPGSADataRecord extends NMEADataRecord{

    /**
     * Mode M or A.
     */
    public boolean manualMode=false;

    /**
     * Mode 1 -fix not avaiable, 2 2D ,3 3D.
     */
    public int operationMode;

    /**
     * PRN numbers of saltellite used in solution.
     */
    public int []PRNs=new int[12];

    /**
     * PDOP.
     */
    public double PDOP;

    /**
     * HDOP.
     */
    public double HDOP;

    /**
     * VDOP.
     */
    public double VDOP;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     */
    public NMEAGPGSADataRecord() {
        recordType=TYPE_GPGSA;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return string of GPGSA.
     * @return a string
     */
    public String toString(){
        String ret= "Selection Mode:" + manualMode + "\n" +
                "Operation Mode:" + operationMode + "\n" +
                "HDOP:" +HDOP +"\n" +
                "PDOP:" +PDOP +"\n" +
                "VDOP:" +VDOP +"\n" +
                "PRNs:";

        for(int i=0;i<PRNs.length;i++){
            ret+=PRNs[i]+",";
        }
        ret+="\n";
        return ret;
    }
}

