//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
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

import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;
import com.mapdigit.gis.raster.MapType;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IGeocoder;
import com.mapdigit.gis.service.IGeocodingListener;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import com.mapdigit.network.HttpConnection;

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
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class GClientGeocoder implements IGeocoder {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public GClientGeocoder() {
        addressQuery = new AddressQuery();
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
    public void getLocations(String address, IGeocodingListener listener) {
        this.listener = listener;
        searchAddress = address;
        try {
            address = com.mapdigit.util.HTML2Text.encodeutf8(address.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        setGoogleKey(GoogleMapService.getGoogleKey());
        MapPoint mapPoint = (MapPoint) addressCache.get(searchAddress);
        if (mapPoint == null) {

            String input = null;
            if (isChina) {
                input = GoogleMapService.getMapServiceURL(GoogleMapService.GEOCODERCHINA);
            } else {
                input = GoogleMapService.getMapServiceURL(GoogleMapService.GEOCODER);

            }
            if (input != null) {
                String queryURL = replaceMetaString(address, input);
                Request.get(queryURL, null, null, addressQuery, this);
            } else {

                Vector argList = new Vector();
                argList.addElement(new Arg("q", address));
                if (GoogleMapService.usingJson) {
                    argList.addElement(new Arg("output", "json"));
                } else {
                    argList.addElement(new Arg("output", "kml"));
                }
                argList.addElement(new Arg("oe", "utf8"));
                argList.addElement(new Arg("ie", "utf8"));
                argList.addElement(new Arg("key", queryKey));

                SearchOptions searchOptions = DigitalMapService.getSearchOptions();
                if (searchOptions.LanguageID.length() != 0 && !isChina) {
                    argList.addElement(new Arg("hl", searchOptions.LanguageID));
                }
                final Arg[] args = new Arg[argList.size() + 1];
                argList.copyInto(args);
                args[argList.size()] = null;
                if (!isChina) {

                    Request.get(SEARCH_BASE, args, null, addressQuery, this);
                } else {

                    Request.get(SEARCH_BASE_CHINA, args, null, addressQuery, this);
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

    public void getLocations(int mapType, String address, IGeocodingListener listener) {
        isChina = mapType == MapType.MICROSOFTCHINA || mapType == MapType.GOOGLECHINA
                || mapType == MapType.MAPABCCHINA  || mapType==MapType.GENERIC_MAPTYPE_CHINA;
        setChina(isChina);
        getLocations(address, listener);
    }
    private static final String SEARCH_BASE = "http://maps.google.com/maps/geo";
    private static final String SEARCH_BASE_CHINA = "http://ditu.google.cn/maps/geo";
    Hashtable addressCache = new Hashtable();
    String searchAddress = null;
    IGeocodingListener listener = null;
    AddressQuery addressQuery = null;
    private String queryKey = "ABQIAAAAi44TY0V29QjeejKd2l3ipRTRERdeAiwZ9EeJWta3L_JZVS0bOBQlextEji5FPvXs8mXtMbELsAFL0w";
    private boolean isChina = false;


    private class AddressQuery implements IRequestListener {

        AddressQuery() {
        }

        private void searchResponse(GClientGeocoder geoCoder, final Response response) {
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
                String prefix="";
                if(!GoogleMapService.usingJson){
                    prefix="kml.Response.";
                }
                String addressName = result.getAsString(prefix+"name");
                final int resultCount = result.getSizeOfArray(prefix+"Placemark");
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
                    if (geoCoder.addressCache.size() > 24) {
                        for (int j = 0; j < 12; j++) {
                            Enumeration keys = geoCoder.addressCache.keys();
                            String key1 = (String) keys.nextElement();
                            geoCoder.addressCache.remove(key1);
                        }
                    }
                    geoCoder.addressCache.put(mapPoints[0].name, mapPoints[0]);
                }

            }catch(OutOfMemoryError ome){
                System.gc();System.gc();
            } catch (Exception rex) {
                Log.p("Error extracting result information:"+rex.getMessage(),Log.ERROR);

            }
            if (geoCoder.listener != null) {
                geoCoder.listener.done(geoCoder.searchAddress, mapPoints);
            }

        }

        public void readProgress(final Object context, final int bytes, final int total) {
            if (context instanceof GClientGeocoder) {
                GClientGeocoder geoCoder = (GClientGeocoder) context;
                if (geoCoder.listener != null) {
                    geoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            if (context instanceof GClientGeocoder) {
                GClientGeocoder geoCoder = (GClientGeocoder) context;
                searchResponse(geoCoder, response);
            }
        }

    }
}

