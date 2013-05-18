//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 30DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.cloudmade;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.network.HttpConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;
import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IReverseGeocodingListener;
import com.mapdigit.gis.service.IReverseGeocoder;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 30DEC2010  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to communicate directly with CloudMade servers to obtain
 * geocodes for user specified addresses. In addition, a geocoder maintains
 * its own cache of addresses, which allows repeated queries to be answered
 * without a round trip to the server.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 30/12/10
 * @author      Guidebee Pty Ltd.
 */
public final class CReverseClientGeocoder implements IReverseGeocoder {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 30DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to CloudMade servers to reverse geocode the specified address
     * @param address  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(String address, IReverseGeocodingListener listener) {
        getLocations(-1, address, listener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to CloudMade servers to reverse geocode the specified address
     * @param address address to query
     * @param listener  callback when query is done.
     */
    public void getLocations(GeoLatLng address, IReverseGeocodingListener listener) {
        getLocations(address.lng()+","+address.lat(),listener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to CloudMade servers to geocode the specified address
     * @param mapType maptype ,not used.
     * @param address  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(int mapType, String address, IReverseGeocodingListener listener) {
        this.listener = listener;

        queryKey = CloudMadeMapService.getCloudMadeKey();
        boolean isLngLat = true;

        try {
            String tmpAddress = address;
            String strLngLat = "[" + tmpAddress + ",0]";
            DigitalMap.fromStringToLatLng(strLngLat);
            searchAddress = tmpAddress;

        } catch (Exception e) {
            isLngLat = false;
        }
        String searchBase=replaceMetaString(SEARCH_BASE);
        if (isLngLat) {

            Vector argList = new Vector();
            argList.addElement(new Arg("around", searchAddress));
            SearchOptions routeOptions = DigitalMapService.getSearchOptions();
            argList.addElement(new Arg("results ", String.valueOf(routeOptions.NumberOfSearchResult)));
            argList.addElement(new Arg("distance", "closest"));
            argList.addElement(new Arg("return_location", "true"));
            final Arg[] args = new Arg[argList.size() + 1];
            argList.copyInto(args);
            args[argList.size()] = null;
            Request.get(searchBase, args, null, addressQuery, this);

        }

    }
    private Hashtable addressCache = new Hashtable();
    private String searchAddress = null;
    private IReverseGeocodingListener listener = null;
    private ReverseAddressQuery addressQuery = new ReverseAddressQuery();
    private static final String SEARCH_BASE = "http://geocoding.cloudmade.com/{CLOUDMADE_KEY}/geocoding/v2/find.js";
    private String queryKey = "8ee2a50541944fb9bcedded5165f09d9";



    private String replaceMetaString(String input) {

        String[] pattern = new String[]{
            "{CLOUDMADE_KEY}",
            " "
        };

        String[] replace = new String[]{
            queryKey,
            "+"
        };

        String url = Utils.replace(pattern, replace, input);
        return url;
    }



    private class ReverseAddressQuery implements IRequestListener {

        public void readProgress(Object context, int bytes, int total) {
            if (context instanceof CReverseClientGeocoder) {
                CReverseClientGeocoder geoCoder = (CReverseClientGeocoder) context;
                if (geoCoder.listener != null) {
                    geoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(Object context, int bytes, int total) {
        }

        public void done(Object context, Response response) throws Exception {
            if (context instanceof CReverseClientGeocoder) {
                CReverseClientGeocoder geoCoder = (CReverseClientGeocoder) context;
                searchResponse(geoCoder, response);
            }
        }

        private void searchResponse(CReverseClientGeocoder geoCoder, final Response response) {
            MapPoint[] mapPoints = null;
            final Throwable ex = response.getException();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if (ex instanceof OutOfMemoryError) {
                    Log.p("Dont have enough memory", Log.ERROR);
                    if (geoCoder.listener != null) {
                        geoCoder.listener.done(null, null);
                    }
                } else {
                    Log.p("Error connecting to search service", Log.ERROR);
                    if (geoCoder.listener != null) {
                        geoCoder.listener.done(geoCoder.searchAddress, null);
                    }
                }

                return;
            }
            try {
                final Result result = response.getResult();
                String prefix = "";

                final int resultCount = result.getSizeOfArray(prefix + "features");
                if (resultCount > 0) {

                    mapPoints = new MapPoint[resultCount];
                    for (int i = 0; i < resultCount; i++) {
                        mapPoints[i] = new MapPoint();
                        mapPoints[i].name = result.getAsString(prefix + "features[" + i + "].properties.addr:housenumber");
                        mapPoints[i].name +=","+ result.getAsString(prefix + "features[" + i + "].properties.addr:street");
                        double []latLngArray=result.getAsDoubleArray(prefix + "features[" + i + "].centroid.coordinates");
                        String address=result.getAsString(prefix + "features[" + i + "].location.county");
                        address+=","+result.getAsString(prefix + "features[" + i + "].location.country");
                        mapPoints[i].setNote(address);
                        mapPoints[i].setPoint(new GeoLatLng(latLngArray[0],latLngArray[1]));

                    }

                    if (geoCoder.addressCache.size() > 24) {
                        for (int j = 0; j < 12; j++) {
                            Enumeration keys = geoCoder.addressCache.keys();
                            String key1 = (String) keys.nextElement();
                            geoCoder.addressCache.remove(key1);
                        }
                    }
                    geoCoder.addressCache.put(mapPoints[0].name, mapPoints[0]);

                }
            } catch (OutOfMemoryError ome) {
                System.gc();
                System.gc();
            } catch (Exception rex) {
                Log.p("Error extracting result information:" + rex.getMessage(), Log.ERROR);

            }
            if (geoCoder.listener != null) {
                geoCoder.listener.done(geoCoder.searchAddress, mapPoints);
            }

        }
    }
}

