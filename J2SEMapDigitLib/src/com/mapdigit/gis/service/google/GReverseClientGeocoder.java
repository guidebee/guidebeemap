//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.google;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Enumeration;
import java.util.Hashtable;

import com.mapdigit.network.HttpConnection;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;

import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.raster.MapType;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IReverseGeocoder;
import com.mapdigit.gis.service.IReverseGeocodingListener;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to communicate directly with Google servers to obtain
 * geocodes for user specified addresses. In addition, a geocoder maintains
 * its own cache of addresses, which allows repeated queries to be answered
 * without a round trip to the server.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class GReverseClientGeocoder implements IReverseGeocoder {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public GReverseClientGeocoder() {
        reverseAddressQuery = new ReverseAddressQuery();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set google china or not.
     * @param china query china or not.
     */
    public void setChina(boolean china) {
        isChina = china;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set google query key.
     * @param key google query key.
     */
    public void setGoogleKey(String key) {
        queryKey = key;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to Google servers to geocode the specified address
     * @param address  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(String address, IReverseGeocodingListener listener) {
        this.listener = listener;
        searchAddress = address;
        setGoogleKey(GoogleMapService.getGoogleKey());
        MapPoint mapPoint = (MapPoint) addressCache.get(address);

        if (mapPoint == null) {

            String input = null;
            if (isChina) {
                input = GoogleMapService.getMapServiceURL(GoogleMapService.REVERSEGEOCODERCHINA);
            } else {
                input = GoogleMapService.getMapServiceURL(GoogleMapService.REVERSEGEOCODER);
            }
            if (input != null) {
                String queryURL = replaceMetaString(address, input);
                Request.get(queryURL, null, null, reverseAddressQuery, this);
            } else {

                Vector argList = new Vector();
                argList.addElement(new Arg("ll", address));
                if (GoogleMapService.usingJson) {
                    argList.addElement(new Arg("output", "json"));
                } else {
                    argList.addElement(new Arg("output", "kml"));
                }
                argList.addElement(new Arg("oe", "utf8"));
                argList.addElement(new Arg("ie", "utf8"));
                SearchOptions searchOptions = DigitalMapService.getSearchOptions();
                if (searchOptions.LanguageID.length() != 0 && !isChina) {
                    argList.addElement(new Arg("hl", searchOptions.LanguageID));
                }
                argList.addElement(new Arg("key", queryKey));
                final Arg[] args = new Arg[argList.size() + 1];
                argList.copyInto(args);
                args[argList.size()] = null;
                if (!isChina) {

                    Request.get(SEARCH_BASE, args, null, reverseAddressQuery, this);
                } else {

                    Request.get(SEARCH_BASE_CHINA, args, null, reverseAddressQuery, this);
                }
            }
        } else {
            MapPoint[] mapPoints = new MapPoint[1];
            mapPoints[0] = mapPoint;
            listener.done(mapPoint.name, mapPoints);
        }
    }

    private String replaceMetaString(String address, String input) {

        String[] pattern = new String[]{
            "{ADDRESS}",
            "{GOOGLE_KEY}",
            "{LANG}",
            " "
        };

        String lang = "";
        SearchOptions searchOptions = DigitalMapService.getSearchOptions();
        if (searchOptions.LanguageID.length() != 0 && !isChina) {
            lang = "&hl=" + searchOptions.LanguageID;
        }

        String[] replace = new String[]{
            address,
            queryKey,
            lang,
            "+"
        };

        String url = Utils.replace(pattern, replace, input);
        return url;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * @inheritDoc
     */
    public void getLocations(int mapType, String address, IReverseGeocodingListener listener) {
        isChina = mapType == MapType.MICROSOFTCHINA || mapType == MapType.GOOGLECHINA
                || mapType == MapType.MAPABCCHINA || mapType==MapType.GENERIC_MAPTYPE_CHINA;
        setChina(isChina);

        getLocations(address, listener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to Google servers to reverse geocode the specified address
     * @param address address to query
     * @param listener  callback when query is done.
     */
    public void getLocations(GeoLatLng address, IReverseGeocodingListener listener) {
        getLocations(address.lng()+","+address.lat(),listener);
    }
    
    private static final String SEARCH_BASE = "http://maps.google.com/maps/geo";
    private static final String SEARCH_BASE_CHINA = "http://ditu.google.cn/maps/geo";
    Hashtable addressCache = new Hashtable();
    String searchAddress = null;
    IReverseGeocodingListener listener = null;
    ReverseAddressQuery reverseAddressQuery = null;
    private String queryKey = "ABQIAAAAi44TY0V29QjeejKd2l3ipRTRERdeAiwZ9EeJWta3L_JZVS0bOBQlextEji5FPvXs8mXtMbELsAFL0w";
    private boolean isChina = false;

    private class ReverseAddressQuery implements IRequestListener {

        ReverseAddressQuery() {
        }

        private void searchResponse(GReverseClientGeocoder reverseGeoCoder, final Response response) {
            MapPoint[] mapPoints = null;
            final Throwable ex = response.getException();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if (ex instanceof OutOfMemoryError) {
                    Log.p("Dont have enough memory", Log.ERROR);
                    if (reverseGeoCoder.listener != null) {
                        reverseGeoCoder.listener.done(null, null);
                    }
                } else {
                    Log.p("Error connecting to search service", Log.ERROR);
                    if (reverseGeoCoder.listener != null) {
                        reverseGeoCoder.listener.done(reverseGeoCoder.searchAddress, null);
                    }
                }

                return;
            }
            try {
                final Result result = response.getResult();
                String addressName = result.getAsString("name");
                String prefix = "";
                if (!GoogleMapService.usingJson) {
                    prefix = "kml.Response.";
                }
                final int resultCount = result.getSizeOfArray(prefix + "Placemark");
                if (resultCount >= 0) {
                    if (resultCount == 0) {
                        mapPoints = new MapPoint[1];
                        int i = 0;
                        mapPoints[i] = new MapPoint();
                        mapPoints[i].name = result.getAsString(prefix + "Placemark.address");
                        if (isChina) {
                            if (mapPoints[i].name.startsWith(GoogleMapService.CHINESE_FULL_NAME)) {
                                mapPoints[i].name = mapPoints[i].name.substring(GoogleMapService.CHINESE_FULL_NAME.length());
                            }
                        }
                        String location = result.getAsString(prefix + "Placemark.Point.coordinates");
                        GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                        mapPoints[i].setPoint(latLng);


                    } else {
                        mapPoints = new MapPoint[resultCount];
                        for (int i = 0; i < resultCount; i++) {
                            mapPoints[i] = new MapPoint();
                            mapPoints[i].name = result.getAsString(prefix + "Placemark[" + i + "].address");
                            if (isChina) {
                                if (mapPoints[i].name.startsWith(GoogleMapService.CHINESE_FULL_NAME)) {
                                    mapPoints[i].name = mapPoints[i].name.substring(GoogleMapService.CHINESE_FULL_NAME.length());
                                }
                            }
                            String location = result.getAsString(prefix + "Placemark[" + i + "].Point.coordinates");
                            GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                            mapPoints[i].setPoint(latLng);

                        }
                    }
                    if (reverseGeoCoder.addressCache.size() > 24) {
                        for (int j = 0; j < 12; j++) {
                            Enumeration keys = reverseGeoCoder.addressCache.keys();
                            String key1 = (String) keys.nextElement();
                            reverseGeoCoder.addressCache.remove(key1);
                        }
                    }
                    reverseGeoCoder.addressCache.put(mapPoints[0].name, mapPoints[0]);
                }

            } catch (OutOfMemoryError ome) {
                System.gc();
                System.gc();
            } catch (Exception rex) {
                Log.p("Error extracting result information:" + rex.getMessage(), Log.ERROR);

            }
            if (reverseGeoCoder.listener != null) {
                reverseGeoCoder.listener.done(reverseGeoCoder.searchAddress, mapPoints);
            }

        }

        public void readProgress(final Object context, final int bytes, final int total) {
            if (context instanceof GReverseClientGeocoder) {
                GReverseClientGeocoder reverseGeoCoder = (GReverseClientGeocoder) context;
                if (reverseGeoCoder.listener != null) {
                    reverseGeoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            if (context instanceof GReverseClientGeocoder) {
                GReverseClientGeocoder reverseGeoCoder = (GReverseClientGeocoder) context;
                searchResponse(reverseGeoCoder, response);

            }
        }
    }
}

