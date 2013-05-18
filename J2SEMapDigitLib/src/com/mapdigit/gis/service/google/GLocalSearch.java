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
import java.util.Hashtable;

import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;
import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.raster.MapType;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IGeocodingListener;
import com.mapdigit.gis.service.ILocalSearch;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.HTML2Text;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
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
public final class GLocalSearch implements ILocalSearch {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public GLocalSearch() {
        addressQuery = new LocalAddressQuery();
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
    public void getLocations(String address, int start, GeoLatLng center,
            GeoBounds bound, IGeocodingListener listener) {
        this.listener = listener;
        searchAddress = address;
        try {
            address = com.mapdigit.util.HTML2Text.encodeutf8(address.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        MapPoint mapPoint = (MapPoint) addressCache.get(searchAddress);
        setGoogleKey(GoogleMapService.getGoogleKey());

        if (mapPoint == null) {
            String input = GoogleMapService.getMapServiceURL(GoogleMapService.LOCALSEARCH);
            if (input != null) {
                String queryURL = replaceMetaString(address, start, center, bound, input);
                Request.get(queryURL, null, null, addressQuery, this);
            } else {


                Vector argList = new Vector();
                argList.addElement(new Arg("q", address));
                argList.addElement(new Arg("v", LOCAL_SEARCH_VER));
                argList.addElement(new Arg("output", "json"));
                argList.addElement(new Arg("oe", "utf8"));
                argList.addElement(new Arg("ie", "utf8"));
                argList.addElement(new Arg("start", Integer.toString(start)));
                argList.addElement(new Arg("sll", center.lat() + "," + center.lng()));
                argList.addElement(new Arg("sspn", bound.getWidth() + "," + bound.getHeight()));
                SearchOptions searchOptions = DigitalMapService.getSearchOptions();
                if (searchOptions.LanguageID.length() != 0 && !isChina) {
                    argList.addElement(new Arg("hl", searchOptions.LanguageID));
                }
                argList.addElement(new Arg("key", queryKey));
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

    private String replaceMetaString(String address, int start, GeoLatLng center,
            GeoBounds bound, String input) {

        String[] pattern = new String[]{
            "{QUERY}",
            "{GOOGLE_KEY}",
            "{START}",
            "{CENTER}",
            "{BOUND}",
            "{LANG}",
            " ",};

        String lang = "";

        SearchOptions searchOptions = DigitalMapService.getSearchOptions();
        if (searchOptions.LanguageID.length() != 0 && !isChina) {
            lang = "&hl=" + searchOptions.LanguageID;
        }

        String[] replace = new String[]{
            address,
            queryKey,
            Integer.toString(start),
            center.lat() + "," + center.lng(),
            bound.getWidth() + "," + bound.getHeight(),
            lang,
            "+"
        };

        String url = Utils.replace(pattern, replace, input);
        return url;
    }

    public void getLocations(int mapType, String address, int start, GeoLatLng center, GeoBounds bound, IGeocodingListener listener) {
        isChina = mapType == MapType.MICROSOFTCHINA || mapType == MapType.GOOGLECHINA
                || mapType == MapType.MAPABCCHINA || mapType==MapType.GENERIC_MAPTYPE_CHINA;
        setChina(isChina);
        getLocations(address, start, center, bound, listener);

    }
    private static final String SEARCH_BASE = "http://ajax.googleapis.com/ajax/services/search/local";
    private static final String SEARCH_BASE_CHINA = "http://ajax.googleapis.com/ajax/services/search/local";
    private static String LOCAL_SEARCH_VER = "1.0";
    Hashtable addressCache = new Hashtable();
    HTML2Text html2Text = new HTML2Text();
    String searchAddress = null;
    IGeocodingListener listener = null;
    LocalAddressQuery addressQuery = null;
    private String queryKey = "ABQIAAAAi44TY0V29QjeejKd2l3ipRTRERdeAiwZ9EeJWta3L_JZVS0bOBQlextEji5FPvXs8mXtMbELsAFL0w";
    private boolean isChina = false;

    private class LocalAddressQuery implements IRequestListener {

        LocalAddressQuery() {
        }

        private void searchResponse(GLocalSearch localSearch, final Response response) {
            MapPoint[] mapPoints = null;
            final Throwable ex = response.getException();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if (ex instanceof OutOfMemoryError) {
                    Log.p("Dont have enough memory", Log.ERROR);
                    if (localSearch.listener != null) {
                        localSearch.listener.done(null, null);
                    }
                } else {
                    Log.p("Error connecting to search service", Log.ERROR);
                    if (localSearch.listener != null) {
                        localSearch.listener.done(localSearch.searchAddress, null);
                    }
                }

                return;
            }
            try {
                final Result result = response.getResult();
                String statusCode = result.getAsString("responseStatus");
                if (statusCode.endsWith("200")) {
                    final int resultCount = result.getSizeOfArray("responseData.results");
                    if (resultCount > 0) {
                        mapPoints = new MapPoint[resultCount];
                        for (int i = 0; i < resultCount; i++) {
                            mapPoints[i] = new MapPoint();
                            String address = result.getAsString("responseData.results[" + i + "].title") + "(" + result.getAsString("responseData.results[" + i + "].streetAddress") + "," + result.getAsString("responseData.results[" + i + "].city") + ")";
                            mapPoints[i].name = html2Text.convert(address);
                            String lat = result.getAsString("responseData.results[" + i + "].lat");
                            String lng = result.getAsString("responseData.results[" + i + "].lng");
                            String location = "[" + lng + "," + lat + ",0]";
                            GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                            mapPoints[i].setPoint(latLng);
                            int phoneNumberCount = result.getSizeOfArray("responseData.results[" + i + "].phoneNumbers");
                            String note = result.getAsString("responseData.results[" + i + "].titleNoFormatting") + "\r\n";
                            for (int j = 0; j < phoneNumberCount; j++) {
                                String phoneNo = result.getAsString("responseData.results[" + i + "].phoneNumbers[" + j + "].number");
                                note += phoneNo + " ";
                            }
                            mapPoints[i].setNote(note);

                        }
                        if (localSearch.addressCache.size() > 24) {
                            for (int j = 0; j < 12; j++) {
                                Enumeration keys = localSearch.addressCache.keys();
                                String key1 = (String) keys.nextElement();
                                localSearch.addressCache.remove(key1);
                            }
                        }
                        localSearch.addressCache.put(mapPoints[0].name, mapPoints[0]);
                    }
                }

            } catch (Exception rex) {
                Log.p("Error extracting result information:" + rex.getMessage(), Log.ERROR);

            }
            if (localSearch.listener != null) {
                localSearch.listener.done(localSearch.searchAddress, mapPoints);
            }

        }

        public void readProgress(final Object context, final int bytes, final int total) {
            if (context instanceof GLocalSearch) {
                GLocalSearch geoCoder = (GLocalSearch) context;
                if (geoCoder.listener != null) {
                    geoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            if (context instanceof GLocalSearch) {
                GLocalSearch geoCoder = (GLocalSearch) context;
                searchResponse(geoCoder, response);
            }
        }
    }
}

