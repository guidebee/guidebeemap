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
import com.mapdigit.gis.geometry.GeoBounds;

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
import com.mapdigit.gis.service.IGeocodingListener;
import com.mapdigit.gis.service.ILocalSearch;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.Log;
import java.io.UnsupportedEncodingException;
import com.mapdigit.network.HttpConnection;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to communicate directly with mapabc servers to obtain
 * geocodes for user specified addresses. In addition, a geocoder maintains
 * its own cache of addresses, which allows repeated queries to be answered
 * without a round trip to the server.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 28/12/10
 * @author      Guidebee Pty Ltd.
 */
public final class MLocalSearch implements ILocalSearch {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @inheritDoc
     */
    public void getLocations(String address, int start, GeoLatLng center,
            GeoBounds bound, IGeocodingListener listener) {
        getLocations(-1, address, start, center, bound, listener);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @inheritDoc
     */
    public void getLocations(int cityCode, String address, int start,
            GeoLatLng center, GeoBounds bound, IGeocodingListener listener) {
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
            argList.addElement(new Arg("ver", MapAbcMapService.MAPABC_SERVICE_VER));
            argList.addElement(new Arg("cityCode", "total"));

            argList.addElement(new Arg("config", "BELSBN"));
            argList.addElement(new Arg("centerPoiXY", center.toString()));
            argList.addElement(new Arg("searchName", address));
            SearchOptions routeOptions = DigitalMapService.getSearchOptions();
            argList.addElement(new Arg("number ", String.valueOf(routeOptions.NumberOfSearchResult)));
            argList.addElement(new Arg("searchType", ""));
            int distance = (int) (1000 * GeoLatLng.distance(center, new GeoLatLng(bound.getCenterY(), bound.getCenterX())));
            argList.addElement(new Arg("range", String.valueOf(distance)));
            int batchId = (start / routeOptions.NumberOfSearchResult) + 1;
            argList.addElement(new Arg("batch", String.valueOf(batchId)));
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
    private LocalAddressQuery addressQuery = new LocalAddressQuery();
    private static final String SEARCH_BASE_CHINA = "http://search1.mapabc.com/sisserver";
    private String queryKey = "b0a7db0b3a30f944a21c3682064dc70ef5b738b062f6479a5eca39725798b1ee300bd8d5de3a4ae3";

    private class LocalAddressQuery implements IRequestListener {

        public void readProgress(Object context, int bytes, int total) {
            if (context instanceof MLocalSearch) {
                MLocalSearch geoCoder = (MLocalSearch) context;
                if (geoCoder.listener != null) {
                    geoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(Object context, int bytes, int total) {
        }

        public void done(Object context, Response response) throws Exception {
            if (context instanceof MLocalSearch) {
                MLocalSearch geoCoder = (MLocalSearch) context;
                searchResponse(geoCoder, response);
            }
        }

        private void searchResponse(MLocalSearch geoCoder, final Response response) {
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
                if (resultCount >= 0) {
                    if (resultCount == 0) {
                        mapPoints = new MapPoint[1];
                        int i = 0;
                        mapPoints[i] = new MapPoint();
                        mapPoints[i].name = result.getAsString(prefix + "poilist.name");
                        String latitude = result.getAsString(prefix + "poilist.y");
                        String longitude = result.getAsString(prefix + "poilist.x");
                        String location = "[" + longitude + "," + latitude + ",0]";
                        GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                        String address = result.getAsString(prefix + "poilist.address");
                        String tel = result.getAsString(prefix + "poilist.tel");
                        mapPoints[i].setNote(address + " " + tel);
                        mapPoints[i].setPoint(latLng);


                    } else {
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

