//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
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
 * NEMA Data record for each NMEA sentence..
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public abstract class NMEADataRecord {

    /**
     * No type
     */
    public static final short TYPE_NONE = 0;

    /**
     * Type GPRMC.
     */
    public static final short TYPE_GPRMC = 1;

    /**
     * Type GPGGA.
     */
    public static final short TYPE_GPGGA = 2;

    /**
     * Type GPGSA.
     */
    public static final short TYPE_GPGSA = 4;

    /**
     * Type GPRMC.
     */
    public static final short TYPE_GPGSV = 8;

    /**
     * Type GPGGA.
     */
    public static final short TYPE_GPGLL = 16;

    /**
     * Type GPGSA.
     */
    public static final short TYPE_GPVTG = 32;

    /**
     * TYPE_GPRMC | TYPE_GPGGA | TYPE_GPGSA |
     * TYPE_GPGSV | TYPE_GPGLL | TYPE_GPVTG
     */
    public static final short ALL_TYPES_MASK = 63;
    
    /**
     * record Type.
     */
    public short recordType = TYPE_NONE;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     *
     */
    protected NMEADataRecord() {
    }


}










