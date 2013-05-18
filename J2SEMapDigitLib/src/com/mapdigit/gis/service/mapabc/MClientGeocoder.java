//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.mapabc;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.network.HttpConnection;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IGeocoder;
import com.mapdigit.gis.service.IGeocodingListener;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.Log;
import java.io.UnsupportedEncodingException;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 28DEC2010  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to communicate directly with MapAbc servers to obtain
 * geocodes for user specified addresses. In addition, a geocoder maintains
 * its own cache of addresses, which allows repeated queries to be answered
 * without a round trip to the server.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 28/12/10
 * @author      Guidebee Pty Ltd.
 */
public final class MClientGeocoder implements IGeocoder {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to MapAbc servers to geocode the specified address
     * @param address  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(String address, IGeocodingListener listener) {
        getLocations(-1, address, listener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to MapAbc servers to geocode the specified address
     * @param cityCode city code ,-1 for all china.
     * @param address  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(int cityCode, String address, IGeocodingListener listener) {
        this.listener = listener;
        searchAddress = address;
        try {
            address = com.mapdigit.util.HTML2Text.encodeutf8(address.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        queryKey = MapAbcMapService.getMapAbcKey();
        MapPoint mapPoint = (MapPoint) addressCache.get(searchAddress);
        if (mapPoint == null) {

            Vector argList = new Vector();
            argList.addElement(new Arg("highLight", "false"));
            argList.addElement(new Arg("enc", "utf-8"));
            if (cityCode == -1 || cityCode<10) {
                argList.addElement(new Arg("cityCode", "total"));
            } else {
                argList.addElement(new Arg("cityCode", String.valueOf(cityCode)));
            }

            argList.addElement(new Arg("config", "BESN"));
            argList.addElement(new Arg("searchName", address));
            SearchOptions routeOptions = DigitalMapService.getSearchOptions();
            argList.addElement(new Arg("number ", String.valueOf(routeOptions.NumberOfSearchResult)));
            argList.addElement(new Arg("searchType", ""));
            argList.addElement(new Arg("batch", "1"));
            if (MapAbcMapService.usingJson) {
                argList.addElement(new Arg("resType", "JSON"));
            } else {
                argList.addElement(new Arg("resType", "XML"));
            }
            argList.addElement(new Arg("a_k", queryKey));
            final Arg[] args = new Arg[argList.size() + 1];
            argList.copyInto(args);
            args[argList.size()] = null;
            Request.get(SEARCH_BASE_CHINA, args, null, addressQuery, this);


        } else {
            MapPoint[] mapPoints = new MapPoint[1];
            mapPoints[0] = mapPoint;
            listener.done(mapPoint.name, mapPoints);
        }

    }

    private Hashtable addressCache = new Hashtable();
    private String searchAddress = null;
    private IGeocodingListener listener = null;
    private AddressQuery addressQuery = new AddressQuery();
    private static final String SEARCH_BASE_CHINA = "http://search1.mapabc.com/sisserver";
    private String queryKey = "b0a7db0b3a30f944a21c3682064dc70ef5b738b062f6479a5eca39725798b1ee300bd8d5de3a4ae3";

    private class AddressQuery implements IRequestListener {

        public void readProgress(Object context, int bytes, int total) {
            if (context instanceof MClientGeocoder) {
                MClientGeocoder geoCoder = (MClientGeocoder) context;
                if (geoCoder.listener != null) {
                    geoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(Object context, int bytes, int total) {
        }

        public void done(Object context, Response response) throws Exception {
            if (context instanceof MClientGeocoder) {
                MClientGeocoder geoCoder = (MClientGeocoder) context;
                searchResponse(geoCoder, response);
            }
        }

        private void searchResponse(MClientGeocoder geoCoder, final Response response) {
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
                if (!MapAbcMapService.usingJson) {
                    prefix = "";
                }
                final int resultCount = result.getSizeOfArray(prefix + "poilist");
                if (resultCount > 0) {

                    mapPoints = new MapPoint[resultCount];
                    for (int i = 0; i < resultCount; i++) {
                        mapPoints[i] = new MapPoint();

                        mapPoints[i].name = result.getAsString(prefix + "poilist[" + i + "].name");
                        String latitude = result.getAsString(prefix + "poilist[" + i + "].y");
                        String longitude = result.getAsString(prefix + "poilist[" + i + "].x");
                        String location = "[" + longitude + "," + latitude + ",0]";
                        GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                        String address = result.getAsString(prefix + "poilist[" + i + "].address");
                        String tel = result.getAsString(prefix + "poilist[" + i + "].tel");
                        mapPoints[i].setNote(address + " " + tel);
                        mapPoints[i].setPoint(latLng);

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

