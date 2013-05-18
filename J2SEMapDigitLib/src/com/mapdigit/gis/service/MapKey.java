//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class defines the map service key for different map servers. i.e.
 * Google map key, bing map key, MapAbc Key ,OpenStreet Map Key.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 28/12/10
 * @author      Guidebee Pty Ltd.
 */
public class MapKey {

    /**
     * google map key.
     */
    public final static int MAPKEY_TYPE_GOOGLE=1;

    /**
     * bing map key.
     */
    public final static int MAPKEY_TYPE_BING=2;

    /**
     * map abc map key.
     */
    public final static int MAPKEY_TYPE_MAPABC=3;

    /**
     * open street map key.
     */
    public final static int MAPKEY_TYPE_CLOUDMADE=4;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public MapKey(int key,String keyValue) {
        keyType=key;
        keyString=keyValue;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the key type.
     * @return the type of the key.
     */
    public int getkeyType(){
        return keyType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the key value.
     * @return the key string.
     */
    public String getKeyValue(){
        return keyString;
    }

    /**
     * key type.
     */
    private int keyType=MAPKEY_TYPE_GOOGLE;

    /**
     * key string.
     */
    private String keyString;

}
