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
package com.mapdigit.gis.service.mapabc;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.MapKey;
import com.mapdigit.gis.service.MapKeyRepository;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class define MapABC map services.
 * <p></p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 28/12/10
 * @author      Guidebee Pty Ltd.
 */
public class MapAbcMapService extends DigitalMapService {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public MapAbcMapService() {
        localSearch = new MLocalSearch();
        geocoder = new MClientGeocoder();
        reverseGeocoder = new MReverseClientGeocoder();
        directionQuery = new MDirections();

    }

    static boolean usingJson = true;//use JSON or KML

    static String getMapAbcKey() {
        return MapKeyRepository.getKey(MapKey.MAPKEY_TYPE_MAPABC);

    }

    static final String MAPABC_SERVICE_VER="2.0";
  
}
