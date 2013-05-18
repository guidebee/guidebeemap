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
package com.mapdigit.gis.service;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.service.cloudmade.CloudMadeMapService;
import com.mapdigit.gis.service.google.GoogleMapService;
import com.mapdigit.gis.service.mapabc.MapAbcMapService;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to store driving directions results
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public abstract class DigitalMapService {

    /**
     * google map service
     */
    public final static int GOOGLE_MAP_SERVICE = 0;
    /**
     * map abc map service
     */
    public final static int MAPABC_MAP_SERVICE = 1;
    /**
     * Bing map service
     */
    public final static int BING_MAP_SERVICE = 2;
    /**
     * cloud made map service
     */
    public final static int CLOUDMADE_MAP_SERVICE = 3;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get current map service.
     * @param mapServiceType map service type.
     * @return map service instance.
     */
    public static DigitalMapService getCurrentMapService(int mapServiceType) {
        switch (mapServiceType) {
            case MAPABC_MAP_SERVICE:
                return mapAbcMapService;
            case GOOGLE_MAP_SERVICE:
                return googleMapService;
            case CLOUDMADE_MAP_SERVICE:
                return cloudMadeMapService;
        }
        return googleMapService;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for geocoding query.
     * @param geocodingListener callback when query is done and in progress

     */
    public void setGeocodingListener(IGeocodingListener geocodingListener) {
        this.geocodingListener = geocodingListener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for reverse geocoding query.
     * @param reverseGeocodingListener callback when query is done and in progress
     */
    public void setReverseGeocodingListener(IReverseGeocodingListener reverseGeocodingListener) {
        this.reverseGeocodingListener = reverseGeocodingListener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for geocoding query.
     * @param geocodingListener callback when query is done and in progress

     */
    public void setIpAddressGeocodingListener(IIpAddressGeocodingListener geocodingListener) {
        this.ipAddressGeocodingListener = geocodingListener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for direction query.
     * @param routingListener the routing listener

     */
    public void setRoutingListener(IRoutingListener routingListener) {
        this.routingListener = routingListener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to reverse geocode the specified address
     * @param latlngAddress  address to query
     */
    public void getReverseLocations(GeoLatLng latlngAddress) {
        if (this.reverseGeocodingListener != null) {
            reverseGeocoder.getLocations(latlngAddress,
                    this.reverseGeocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to reverse geocode the specified address
     * @param latlngAddress  address to query
     * @deprecated use getReverseLocations(GeoLatLng latlngAddress)
     */
    public void getReverseLocations(String latlngAddress) {
        if (this.reverseGeocodingListener != null) {
            reverseGeocoder.getLocations(latlngAddress,
                    this.reverseGeocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to reverse geocode the specified address
     * @param mapType map type.
     * @param latlngAddress  latitude,longitude string address.delimited by comma
     */
    public void getReverseLocations(int mapType, String latlngAddress) {
        if (this.reverseGeocodingListener != null) {
            reverseGeocoder.getLocations(mapType, latlngAddress,
                    this.reverseGeocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param address  address to query
     */
    public void getLocations(String address) {
        if (this.geocodingListener != null) {
            geocoder.getLocations(address, this.geocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * local search.
     * @param address  name of the address
     * @param center   center of the search
     * @param bound    boundary
     */
    public void getLocations(String address, int start, GeoLatLng center, GeoBounds bound) {
        if (this.localSearch != null) {
            localSearch.getLocations(address, start, center, bound, this.geocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * local search
     * @param mapType map type.
     * @param address  name of the address
     * @param center   center of the search
     * @param bound    boundary
     */
    public void getLocations(int mapType, String address, int start,
            GeoLatLng center, GeoBounds bound) {
        if (this.localSearch != null) {
            localSearch.getLocations(mapType, address, start, center, bound, this.geocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get location based on cellid.
     * @param MMC MMC code.
     * @param MNC NMC code.
     * @param LAC LAC code.
     * @param CID CID code.
     */
    public void getLocations(String MMC, String MNC, String LAC, String CID) {
        if (cellIdGeocoder != null) {
            cellIdGeocoder.getLocations(MMC, MNC, LAC, CID, this.geocodingListener);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Sends a request to servers to geocode the specified address
     * @param mapType the map type.
     * @param address  address to query
     */
    public void getLocations(int mapType, String address) {
        if (this.geocodingListener != null) {
            geocoder.getLocations(mapType, address, this.geocodingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param address  address to query
     */
    public void getIpLocations(String address) {
        if (this.ipAddressGeocodingListener != null) {
            ipAddressGeocoder.getLocations(address, this.ipAddressGeocodingListener);

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set search Option.
     */
    public static void setSearchOptions(SearchOptions options) {
        searchOptions = options;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get search options
     */
    public static SearchOptions getSearchOptions() {
        return searchOptions;
    }
    private static SearchOptions searchOptions = new SearchOptions();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to get the direction
     * @param waypoints  waypoints to query
     */
    public void getDirections(GeoLatLng[] waypoints) {
        if (this.routingListener != null) {
            directionQuery.getDirection(waypoints, this.routingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to get the direction
     * @param query  address to query
     * @deprecated use  getDirections(GeoLatLng[] waypoints)
     */
    public void getDirections(String query) {
        if (this.routingListener != null) {
            directionQuery.getDirection(query, this.routingListener);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to get the direction
     * @param mapType the map type.
     * @param query  address to query
     */
    public void getDirections(int mapType, String query) {
        if (this.routingListener != null) {
            directionQuery.getDirection(mapType, query, this.routingListener);
        }
    }
    protected IIpAddressGeocodingListener ipAddressGeocodingListener = null;
    protected IGeocodingListener geocodingListener = null;
    protected IReverseGeocodingListener reverseGeocodingListener = null;
    protected IRoutingListener routingListener = null;
    protected IGeocoder geocoder = null;
    protected ILocalSearch localSearch = null;
    protected IReverseGeocoder reverseGeocoder = null;
    protected IDirectionQuery directionQuery = null;
    protected IIpAddressGeocoder ipAddressGeocoder = new IpAddressGeocoder();
    protected ICellIdGeocoder cellIdGeocoder = new CellIdGeocoder();
    private final static GoogleMapService googleMapService = new GoogleMapService();
    private final static MapAbcMapService mapAbcMapService = new MapAbcMapService();
    private final static CloudMadeMapService cloudMadeMapService = new CloudMadeMapService();
}
