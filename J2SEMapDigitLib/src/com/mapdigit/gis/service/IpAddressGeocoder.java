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
import com.mapdigit.ajax.Arg;
import com.mapdigit.network.HttpConnection;

import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;
import com.mapdigit.ajax.IRequestListener;

////////////////////////////////////////////////////////////////////////////////
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
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
public final class IpAddressGeocoder implements IIpAddressGeocoder {

    
    private static final String SEARCH_BASE = "http://geoip1.maxmind.com/f";
    private static final String LOCAL_SEARCH_BASE = "http://www.mapdigit.com/guidebeemap/ipaddress.php";
    IIpAddressGeocodingListener listener = null;
    String searchAddress = null;
    AddressQuery addressQuery = null;
    public final static String IP_NOT_FOUND = "IP_NOT_FOUND";

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public IpAddressGeocoder() {
        addressQuery = new AddressQuery();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sends a request to Google servers to geocode the specified address
     * @param ipAddress  address to query
     * @param listener callback when query is done.
     */
    public void getLocations(String ipAddress, IIpAddressGeocodingListener listener) {
        this.listener = listener;
        searchAddress = ipAddress;
        if (searchAddress.endsWith(LOCAL_ADDRESS)) {
            Request.get(LOCAL_SEARCH_BASE, null, null, addressQuery, this);
        } else {
            final Arg[] args = new Arg[2];
            args[0] = new Arg("l", "EuWpotBFCA2I");
            args[1] = new Arg("i", searchAddress);
            Request.get(SEARCH_BASE, args, null, addressQuery, this);
        }

    }

    class AddressQuery implements IRequestListener {

        AddressQuery() {
        }

        private void searchJSONResponse(IpAddressGeocoder geoCoder, final Response response) {
            IpAddressLocation ipAddressLocation = null;
            final Throwable ex = response.getException();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if(ex instanceof OutOfMemoryError){
                     Log.p("Dont have enough memory", Log.ERROR);
                }else{
                     Log.p("Error connecting to search service", Log.ERROR);
                }
                
                if (geoCoder.listener != null) {
                    geoCoder.listener.done(geoCoder.searchAddress, null);
                }
                return;
            }
            try {
                final Result result = response.getResult();
                ipAddressLocation = new IpAddressLocation();
                ipAddressLocation.ipaddress = searchAddress;
                ipAddressLocation.country = result.getAsString("country");
                ipAddressLocation.region = result.getAsString("region");
                ipAddressLocation.city = result.getAsString("city");
                ipAddressLocation.postal = result.getAsString("postal");
                ipAddressLocation.latitude = result.getAsString("latitude");
                ipAddressLocation.longitude = result.getAsString("longitude");
                ipAddressLocation.metro_code = result.getAsString("metro_code");
                ipAddressLocation.area_code = result.getAsString("area_code");
                ipAddressLocation.isp = result.getAsString("isp");
                ipAddressLocation.organization = result.getAsString("organization");
                ipAddressLocation.error = result.getAsString("error");


            } catch (Exception rex) {
                Log.p("Error extracting result information:" + rex.getMessage(), Log.ERROR);

            }
            if (geoCoder.listener != null) {
                geoCoder.listener.done(geoCoder.searchAddress, ipAddressLocation);
            }

        }

        private void searchResponse(IpAddressGeocoder geoCoder, final Response response) {
            IpAddressLocation ipAddressLocation = null;
            if (response.getCode() != HttpConnection.HTTP_OK) {
                Log.p("Error connecting to search service", Log.ERROR);
                if (geoCoder.listener != null) {
                    geoCoder.listener.done(geoCoder.searchAddress, null);
                }
                return;
            }
            try {

                final String content = response.getRawContent();
                String[] values = Utils.tokenize(content, ',');
                ipAddressLocation = new IpAddressLocation();
                if(searchAddress.endsWith(LOCAL_ADDRESS)){
                    if(values.length==11){
                        ipAddressLocation.ipaddress=values[10];
                    }
                }else{
                    ipAddressLocation.ipaddress=searchAddress;
                }
                
                if (values.length >= 10) {
                    ipAddressLocation.country = values[0];
                    ipAddressLocation.region = values[1];
                    ipAddressLocation.city = values[2];
                    ipAddressLocation.postal = values[3];
                    ipAddressLocation.latitude = values[4];
                    ipAddressLocation.longitude = values[5];
                    ipAddressLocation.metro_code = values[6];
                    ipAddressLocation.area_code = values[7];
                    ipAddressLocation.isp = values[8].replace('"', ' ').trim();
                    ipAddressLocation.organization = values[9].replace('"', ' ').trim();
                    ipAddressLocation.error = "";

                } else {
                    ipAddressLocation.error = IP_NOT_FOUND;
                }


            } catch (Exception rex) {
                Log.p("Error extracting result information:" + rex.getMessage(), Log.ERROR);

            }
            if (geoCoder.listener != null) {
                geoCoder.listener.done(geoCoder.searchAddress, ipAddressLocation);
            }

        }

        public void readProgress(final Object context, final int bytes, final int total) {
            IpAddressGeocoder geoCoder = (IpAddressGeocoder) context;
            geoCoder.listener.readProgress(bytes, total);
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            IpAddressGeocoder geoCoder = (IpAddressGeocoder) context;
            searchResponse(geoCoder, response);
        }
    }
}


