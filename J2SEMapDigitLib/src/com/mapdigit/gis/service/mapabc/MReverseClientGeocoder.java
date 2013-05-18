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
import com.mapdigit.gis.service.IReverseGeocodingListener;
import com.mapdigit.gis.service.IReverseGeocoder;
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
import com.mapdigit.network.HttpConnection;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to communicate directly with MapAbc servers to obtain
 * geocodes for user specified addresses. In addition, a geocoder maintains
 * its own cache of addresses, which allows repeated queries to be answered
 * without a round trip to the server.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 28/12/10
 * @author      Guidebee Pty Ltd.
 */
public final class MReverseClientGeocoder implements IReverseGeocoder {

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
     * Sends a request to MapAbc servers to reverse geocode the specified address
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
     * Sends a request to MapAbc servers to geocode the specified address
     * @param cityCode city code ,-1 for all china.
     * @param address  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(int cityCode, String address, IReverseGeocodingListener listener) {
        this.listener = listener;
        searchAddress = address;
        boolean isLngLat = true;

        try {
            String tmpAddress =address;
            String strLngLat = "[" + tmpAddress + ",0]";
            DigitalMap.fromStringToLatLng(strLngLat);
            searchAddress = tmpAddress;

        } catch (Exception e) {
            isLngLat = false;
        }
        if (isLngLat) {
            queryKey = MapAbcMapService.getMapAbcKey();
            MapPoint mapPoint = (MapPoint) addressCache.get(address);
            if (mapPoint == null) {

                Vector argList = new Vector();
                argList.addElement(new Arg("highLight", "false"));
                argList.addElement(new Arg("enc", "utf-8"));
                argList.addElement(new Arg("ver", MapAbcMapService.MAPABC_SERVICE_VER));
                if (cityCode == -1 || cityCode<10) {
                    argList.addElement(new Arg("cityCode", "total"));
                } else {
                    argList.addElement(new Arg("cityCode", String.valueOf(cityCode)));
                }
                if (MapAbcMapService.usingJson) {
                    argList.addElement(new Arg("resType", "JSON"));
                } else {
                    argList.addElement(new Arg("resType", "XML"));
                }

                argList.addElement(new Arg("config", "SPAS"));
                String []lngLat=Utils.tokenize(searchAddress,',');
                SearchOptions routeOptions = DigitalMapService.getSearchOptions();
                String xmlRequest=replaceMetaString(lngLat[0],lngLat[1],String.valueOf(routeOptions.NumberOfSearchResult));
                argList.addElement(new Arg("spatialXml",xmlRequest));

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

    }

    private Hashtable addressCache = new Hashtable();
    private String searchAddress = null;
    private IReverseGeocodingListener listener = null;
    private ReverseAddressQuery addressQuery = new ReverseAddressQuery();
    private static final String SEARCH_BASE_CHINA = "http://search1.mapabc.com/sisserver";
    private String queryKey = "b0a7db0b3a30f944a21c3682064dc70ef5b738b062f6479a5eca39725798b1ee300bd8d5de3a4ae3";
    private static final String spatialXML="%3C?xml%20version=%221.0%22%20encoding" +
            "=%22gb2312%22?%3E%0D%0A%3Cspatial_request%20method=" +
            "%22searchPoint%22%3E%3Cx%3E{LONGITUDE}%3C/x%3E%3Cy%3E{LATITUDE}%3C/y%3E%3Cxs/%3E%3Cys/%3E%3C" +
            "poiNumber%3E{NUMBER}%3C/poiNumber%3E%3Crange%3ENaN%3C/range%3E%3Cpattern%3E1%3C/pattern%3E" +
            "%3CroadLevel%3E0%3C/roadLevel%3E%3Cexkey/%3E%3C/spatial_request%3E%0D%0A";



    private String replaceMetaString(String longitude,String latitude,String number) {

        String[] pattern = new String[]{
            "{LONGITUDE}",
            "{LATITUDE}",
            "{NUMBER}",
        };


        String[] replace = new String[]{
            longitude,
            latitude,
            number,

        };

        String url = Utils.replace(pattern, replace, spatialXML);
        return url;
    }

    private class ReverseAddressQuery implements IRequestListener {

        public void readProgress(Object context, int bytes, int total) {
            if (context instanceof MReverseClientGeocoder) {
                MReverseClientGeocoder geoCoder = (MReverseClientGeocoder) context;
                if (geoCoder.listener != null) {
                    geoCoder.listener.readProgress(bytes, total);
                }
            }
        }

        public void writeProgress(Object context, int bytes, int total) {
        }

        public void done(Object context, Response response) throws Exception {
            if (context instanceof MReverseClientGeocoder) {
                MReverseClientGeocoder geoCoder = (MReverseClientGeocoder) context;
                searchResponse(geoCoder, response);
            }
        }

        private void searchResponse(MReverseClientGeocoder geoCoder, final Response response) {
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
                String prefix = "SpatialBean.";
                if (!MapAbcMapService.usingJson) {
                    prefix = "";
                }
                final int poiListCount = result.getSizeOfArray(prefix + "poiList");
                final int roadListCount = result.getSizeOfArray(prefix + "roadList");
                int resultCount=poiListCount+roadListCount;
                if (resultCount > 0) {

                    mapPoints = new MapPoint[resultCount];
                    //get poi list.
                    if (poiListCount > 0) {
                        for (int i = 0; i < poiListCount; i++) {
                            mapPoints[i] = new MapPoint();

                            mapPoints[i].name = result.getAsString(prefix + "poiList[" + i + "].name");
                            String latitude = result.getAsString(prefix + "poiList[" + i + "].y");
                            String longitude = result.getAsString(prefix + "poiList[" + i + "].x");
                            String location = "[" + longitude + "," + latitude + ",0]";
                            GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                            String address = result.getAsString(prefix + "poiList[" + i + "].address");
                            String tel = result.getAsString(prefix + "poiList[" + i + "].tel");
                            mapPoints[i].setNote(address + " " + tel);
                            mapPoints[i].setPoint(latLng);

                        }
                    }
                    //road list
                    if (roadListCount > 0) {

                        String latitude = result.getAsString(prefix + "District.y");
                        String longitude = result.getAsString(prefix + "District.x");
                        String location = "[" + longitude + "," + latitude + ",0]";
                        GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                        for (int i = 0; i < roadListCount; i++) {
                            mapPoints[i + poiListCount] = new MapPoint();
                            mapPoints[i + poiListCount].name = result.getAsString(prefix + "roadList[" + i + "].name");
                            String address = result.getAsString(prefix + "roadList[" + i + "].distance");
                            mapPoints[i + poiListCount].setNote(address + "m");
                            mapPoints[i + poiListCount].setPoint(latLng);

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

