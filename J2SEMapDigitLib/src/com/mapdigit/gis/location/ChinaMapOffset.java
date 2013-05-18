//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19FEB2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location;

//--------------------------------- IMPORTS ------------------------------------
import java.io.InputStream;
import com.mapdigit.network.Connector;
import com.mapdigit.network.HttpConnection;

import com.mapdigit.collections.Hashtable;
import com.mapdigit.gis.MapLayer;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPoint;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19FEB2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The map in china has so-call offet, the laitude/longitude received by GPS device
 * is not actually mapped to the real position, it has "offset", this class
 * is used for amend such offset.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 19/02/10
 * @author      Guidebee Pty Ltd.
 */
public class ChinaMapOffset {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19FEB2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get map offset at given location and level.
     * @param longitude longitude of WGS location.
     * @param latitude   latitude of WGS location.
     * @param mapLevel   map Level, 0-18.
     * @return offset in china.
     */
    public GeoPoint getOffset(double longitude, double latitude, int mapLevel) {
        if (mapLevel < 11) {
            return new GeoPoint(0, 0);
        }
        GeoPoint queryPoint = getQueryLocation(latitude, longitude);
        String key = queryPoint.x + "|" + queryPoint.y;
        GeoPoint cachedPoint = (GeoPoint) offsetCache.get(key);
        if (cachedPoint == null) {
            GeoPoint pt = getOffsetFromServer(queryPoint.x / 100.0, queryPoint.y / 100.0);
            offsetCache.put(key, pt);
            cachedPoint = pt;
        }
        return new GeoPoint((int)cachedPoint.x >> (18 - mapLevel),
                (int)cachedPoint.y>>(18 - mapLevel));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19FEB2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert coordinates from WGS to Mars(China)
     * @param earth WGS lat/lng pair.
     * @return Mars' coordinates  lat/lng with deviation in China.
     */
    public GeoLatLng fromEarthToMars(GeoLatLng earth) {
        GeoPoint ptOffset = getOffset(earth.x, earth.y, 18);
        if (ptOffset.x != 0 || ptOffset.y != 0) {
            GeoPoint pt = MapLayer.fromLatLngToPixel(earth, 18);
            pt.x += ptOffset.x;
            pt.y += ptOffset.y;
            return MapLayer.fromPixelToLatLng(pt, 18);

        } else {
            return new GeoLatLng(earth);
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19FEB2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert coordinates from  Mars(China) to WGS
     * @param mar   lat/lng with deviation in China.
     * @return WGS coordinates
     */
    public GeoLatLng fromMarsToEarth(GeoLatLng mar) {
        GeoPoint ptOffset = getOffset(mar.x, mar.y, 18);
        if (ptOffset.x != 0 || ptOffset.y != 0) {
            GeoPoint pt = MapLayer.fromLatLngToPixel(mar, 18);
            pt.x -= ptOffset.x;
            pt.y -= ptOffset.y;
            return MapLayer.fromPixelToLatLng(pt, 18);

        } else {
            return new GeoLatLng(mar);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19FEB2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the offset query base url.
     * @param baseUrl base url.
     */
    public void setQueryBaseURL(String baseUrl){
       QUERY_URL=baseUrl;
    }

    /**
     * internal cache.
     */
    private Hashtable offsetCache = new Hashtable(128);

    /**
     * default sever url.
     */
    private String QUERY_URL = "http://www.mapdigit.com/guidebeemap/offset.php?";

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19FEB2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * normalized latitude and longitude.
     * @param latitude
     * @param longitude
     * @return
     */
    private static GeoPoint getQueryLocation(double latitude, double longitude) {
        int lat = (int) (latitude * 100);
        int lng = (int) (longitude * 100);
        double lat1 = ((int) (latitude * 1000 + 0.499999)) / 10.0;
        double lng1 = ((int) (longitude * 1000 + 0.499999)) / 10.0;
        for (double x = longitude; x < longitude + 1; x += 0.5) {
            for (double y = latitude; x < latitude + 1; y += 0.5) {
                if (x <= lng1 && lng1 < (x + 0.5) && lat1 >= y && lat1 < (y + 0.5)) {
                    return new GeoPoint((int) (x + 0.5), (int) (y + 0.5));
                }
            }
        }
        return new GeoPoint(lng, lat);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19FEB2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the offset from the sever.
     * @param longitude
     * @param latitude
     * @return
     */
    private GeoPoint getOffsetFromServer(double longitude, double latitude) {
        String queryURL = QUERY_URL + "lng=" + longitude + "&lat=" + latitude;
        HttpConnection c = null;
        InputStream is = null;
        int rc;
        try {

            try {
                c = (HttpConnection) Connector.open(queryURL);
                rc = c.getResponseCode();
                if (rc == HttpConnection.HTTP_OK) {
                    is = c.openInputStream();
                    byte[] data = new byte[32];
                    int len = is.read(data);

                    String offsetString = new String(data, 0, len);
                    int index = offsetString.indexOf(",");
                    if (index > 0) {
                        String OffsetX = offsetString.substring(0, index).trim();
                        String OffsetY = offsetString.substring(index + 1).trim();
                        int x = Integer.parseInt(OffsetX);
                        int y = Integer.parseInt(OffsetY);
                        return new GeoPoint(x, y);
                    }

                }
            } finally {

                if (is != null) {
                    is.close();
                }
                if (c != null) {
                    c.close();
                }
            }


        } catch (Exception e) {
        }
       return new GeoPoint(0, 0);


    }
}
