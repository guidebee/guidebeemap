//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IGeocodingListener;
import com.mapdigit.gis.service.IIpAddressGeocodingListener;
import com.mapdigit.gis.service.IReverseGeocodingListener;
import com.mapdigit.gis.service.IRoutingListener;
import com.mapdigit.gis.service.google.GoogleMapService;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * DigitalMap is the base class for VectorMap and RasterMap. It's an abstract
 * class provides all common functions for digtial maps.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 04/01/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class DigitalMap extends MapLayerContainer {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set digital map service instance for this digital map.
     * @param digitalMapService an instance of the ditigal map service.
     */
    public void setDigitalMapService(DigitalMapService digitalMapService) {
        this.digitalMapService = digitalMapService;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the digital map service associcated with this map.
     * @return the digital map service instance.
     */
    public DigitalMapService getDigitalMapService() {
        return digitalMapService;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to get the direction
     * @param type  map type or some other option.
     * @param query  address to query
     */
    public void getDirections(int type,String query) {
       digitalMapService.getDirections(type, query);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to get the direction
     * @param query  address to query
     */
    public void getDirections(GeoLatLng[] query) {
       digitalMapService.getDirections(query);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to get the direction
     * @param query  address to query
     */
    public void getDirections(String query) {
       digitalMapService.getDirections(mapType, query);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for geocoding query.
     * @param geocodingListener callback when query is done and in progress

     */
    public void setGeocodingListener(IGeocodingListener geocodingListener) {
        digitalMapService.setGeocodingListener(geocodingListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for reverse geocoding query.
     * @param reverseGeocodingListener callback when query is done and in progress
     */
    public void setReverseGeocodingListener(IReverseGeocodingListener
            reverseGeocodingListener) {
        digitalMapService.setReverseGeocodingListener(reverseGeocodingListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for geocoding query.
     * @param geocodingListener callback when query is done and in progress
     */
    public void setIpAddressGeocodingListener(IIpAddressGeocodingListener
            geocodingListener) {
        digitalMapService.setIpAddressGeocodingListener(geocodingListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the listener for direction query.
     * @param routingListener the routing listener
     */
    public void setRoutingListener(IRoutingListener routingListener) {
        digitalMapService.setRoutingListener(routingListener);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param ipaddress  address to query
     */
    public void getIpLocations(String ipaddress) {
        digitalMapService.getIpLocations(ipaddress);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param type map type or other query option.
     * @param address  address to query
     */
    public void getLocations(int type,String address) {
       digitalMapService.getLocations(type, address);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param address  address to query
     */
    public void getLocations(String address) {
       digitalMapService.getLocations(mapType, address);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * local search, sends a request to server for local search.
     * @param type map type or some other query option.
     * @param address business name
     * @param start   start index.
     * @param center  search center
     * @param bound   search boundary.
     */
    public void getLocations(int type,String address,int start, GeoLatLng center, GeoBounds bound){
        digitalMapService.getLocations(type, address,start,center,bound);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * local search, sends a request to server for local search.
     * @param address business name
     * @param start   start index.
     * @param center  search center
     * @param bound   search boundary.
     */
    public void getLocations(String address,int start, GeoLatLng center, GeoBounds bound){
        digitalMapService.getLocations(mapType, address,start,center,bound);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * search based on cell id.
     * @param MMC
     * @param MNC
     * @param LAC
     * @param CID
     */
    public void getLocations(String MMC,String MNC,String LAC,String CID){
        digitalMapService.getLocations(MMC, MNC, LAC, CID);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param latlngAddress  address to query
     */
    public void getReverseLocations(GeoLatLng latlngAddress) {
        digitalMapService.getReverseLocations(latlngAddress);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param latlngAddress  address to query
     */
    public void getReverseLocations(String latlngAddress) {
        digitalMapService.getReverseLocations(mapType, latlngAddress);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to servers to geocode the specified address
     * @param type  map type or query option.
     * @param latlngAddress  address to query
     */
    public void getReverseLocations(int type,String latlngAddress) {
        digitalMapService.getReverseLocations(type, latlngAddress);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Update map tile url.
     */
    public void updateMapTileUrl(){
         if(digitalMapService instanceof GoogleMapService){
            GoogleMapService.updateMapServiceUrl();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get lib version no.
     * @return the version no.
     */
    public String getVersionNo(){
        return "2.0.0";
    }
    /**
     * the type of map.
     */
    protected int mapType;

    /**
     * Digital map service instance.
     */
    protected DigitalMapService digitalMapService;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new DigitalMap with given width and height.
     * @param width the width of the map image.
     * @param height the height of the map image.
     */
    protected DigitalMap(int width,int height) {
        super(width,height);
        digitalMapService=DigitalMapService.getCurrentMapService(DigitalMapService.GOOGLE_MAP_SERVICE);
    }
}
