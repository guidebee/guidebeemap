//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 12SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//[------------------------------ MAIN CLASS ----------------------------------]
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import com.mapdigit.rms.RecordStore;
import com.mapdigit.rms.RecordStoreException;

import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 12SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Defines the map types and relations between map types.
 * <hr>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 12/09/10
 * @author      Guidebee Pty Ltd.
 */
public final class MapType {

    /**
     * Google Road Maps
     */
    public static final int GOOGLEMAP = 0;
    /**
     * Google Satellite Images
     */
    public static final int GOOGLESATELLITE = 1;
    /**
     * Google Satellite Images with Road Maps Overlayed
     */
    public static final int GOOGLEHYBRID = 2;
    /**
     * Google Road Maps (China)
     */
    public static final int GOOGLECHINA = 3;
    /**
     * Yahoo Road Maps
     */
    public static final int YAHOOMAP = 4;
    /**
     * Yahoo Satellite Images
     */
    public static final int YAHOOSATELLITE = 5;
    /**
     * Yahoo Satellite Images with Road Maps Overlayed
     */
    public static final int YAHOOHYBRID = 6;
    /**
     * Yahoo India Road Maps
     */
    static final int YAHOOINDIAMAP = 7;
    /**
     * Yahoo Satellite Images with India Road Maps Overlayed
     */
    static final int YAHOOINDIAHYBRID = 8;
    /**
     * Ask.com Road Maps
     */
    public static final int ASKDOTCOMMAP = 9;
    /**
     * Ask.com Satellite Images
     */
    public static final int ASKDOTCOMSATELLITE = 10;
    /**
     * Ask.com Satellite Images with Labels
     */
    public static final int ASKDOTCOMHYBRID = 11;
    /**
     *  Microsoft Road Maps
     */
    public static final int MICROSOFTMAP = 12;
    /**
     *  Microsoft Satellite Maps
     */
    public static final int MICROSOFTSATELLITE = 13;
    /**
     * Microsoft Satellite Images with Road Maps Overlayed
     */
    public static final int MICROSOFTHYBRID = 14;
    /**
     * Microsoft Live China
     */
    public static final int MICROSOFTCHINA = 15;
    /**
     * Nokia normal map
     */
    public static final int NOKIAMAP = 16;
    /**
     * Map abc china
     */
    public static final int MAPABCCHINA = 17;
    /**
     * Google terren
     */
    public static final int GOOGLETERREN = 18;
    /**
     * OpenStreetMap.org Maps
     */
    public static final int OPENSTREETMAP = 19;
    /**
     * Open satellite Maps
     */
    public static final int OPENSATELLITETMAP = 20;
    /**
     * Open cycle Maps
     */
    public static final int OPENCYCLEMAP = 21;
    /**
     * OSMA Maps
     */
    public static final int OSMAMAP = 22;
    /**
     * Microsoft terren
     */
    public static final int MICROSOFTTERREN = 23;
    /**
     * max map type
     */
    public static final int MAXMAPTYPE = MICROSOFTTERREN;


    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_1 = 190;
    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_2 = 191;
    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_3 = 192;
    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_4 = 193;
    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_5 = 194;
    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_6 = 195;
    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_7 = 196;

    /**
     * Generic map type ,used to extension.
     */
    public static final int GENERIC_MAPTYPE_CHINA = 197;
    /**
     * Routing direction map.
     */
    public static final int ROUTING_DIRECTION = 198;
    /**
     * Mapinfo Vector map type
     */
    public static final int MAPINFOVECTORMAP = 199;
    /**
     * for each map type, what consists each map type.
     * some map type like hybrid may consistes two map types, the satellites
     * and the hybrid itself. 
     */
    public static final Hashtable MAP_SEQUENCES = new Hashtable();
    /**
     * map type names and it's index
     */
    public static final Hashtable MAP_TYPE_NAMES = new Hashtable();
    /**
     * map tile urls.
     */
    public static final Hashtable MAP_TYPE_URLS = new Hashtable();

    /**
     * empty tile urls.
     */
    public static final String EMPTY_TILE_URL="guidebee://emptytileurl";
    
    static {
        MAP_SEQUENCES.put(new Integer(GOOGLEMAP), new int[]{GOOGLEMAP});
        MAP_SEQUENCES.put(new Integer(GOOGLESATELLITE), new int[]{MICROSOFTSATELLITE});
        MAP_SEQUENCES.put(new Integer(GOOGLEHYBRID), new int[]{MICROSOFTSATELLITE,GOOGLEHYBRID});
        MAP_SEQUENCES.put(new Integer(GOOGLECHINA), new int[]{GOOGLECHINA});
        MAP_SEQUENCES.put(new Integer(GOOGLETERREN), new int[]{GOOGLETERREN});
        MAP_SEQUENCES.put(new Integer(YAHOOMAP), new int[]{YAHOOMAP});
        MAP_SEQUENCES.put(new Integer(YAHOOSATELLITE), new int[]{YAHOOSATELLITE});
        MAP_SEQUENCES.put(new Integer(YAHOOHYBRID), new int[]{YAHOOSATELLITE, GOOGLEHYBRID});
        MAP_SEQUENCES.put(new Integer(YAHOOINDIAMAP), new int[]{YAHOOINDIAMAP});
        MAP_SEQUENCES.put(new Integer(YAHOOINDIAHYBRID), new int[]{YAHOOINDIAHYBRID});
        MAP_SEQUENCES.put(new Integer(ASKDOTCOMMAP), new int[]{ASKDOTCOMMAP});
        MAP_SEQUENCES.put(new Integer(ASKDOTCOMSATELLITE), new int[]{ASKDOTCOMSATELLITE});
        MAP_SEQUENCES.put(new Integer(ASKDOTCOMHYBRID), new int[]{ASKDOTCOMSATELLITE, ASKDOTCOMHYBRID});
        MAP_SEQUENCES.put(new Integer(MICROSOFTMAP), new int[]{MICROSOFTMAP});
        MAP_SEQUENCES.put(new Integer(MICROSOFTSATELLITE), new int[]{MICROSOFTSATELLITE});
        MAP_SEQUENCES.put(new Integer(MICROSOFTHYBRID), new int[]{MICROSOFTSATELLITE, MICROSOFTHYBRID});
        MAP_SEQUENCES.put(new Integer(MICROSOFTCHINA), new int[]{MICROSOFTCHINA});
        MAP_SEQUENCES.put(new Integer(MICROSOFTTERREN), new int[]{MICROSOFTTERREN});
        MAP_SEQUENCES.put(new Integer(OPENSTREETMAP), new int[]{OPENSTREETMAP});
        MAP_SEQUENCES.put(new Integer(OPENSATELLITETMAP), new int[]{OPENSATELLITETMAP});
        MAP_SEQUENCES.put(new Integer(OPENCYCLEMAP), new int[]{OPENCYCLEMAP});
        MAP_SEQUENCES.put(new Integer(OSMAMAP), new int[]{OSMAMAP});
        MAP_SEQUENCES.put(new Integer(NOKIAMAP), new int[]{NOKIAMAP});
        MAP_SEQUENCES.put(new Integer(MAPABCCHINA), new int[]{MAPABCCHINA});
        MAP_SEQUENCES.put(new Integer(ROUTING_DIRECTION), new int[]{ROUTING_DIRECTION});
        MAP_SEQUENCES.put(new Integer(MAPINFOVECTORMAP), new int[]{MAPINFOVECTORMAP});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_1), new int[]{GENERIC_MAPTYPE_1});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_2), new int[]{GENERIC_MAPTYPE_2});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_3), new int[]{GENERIC_MAPTYPE_3});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_4), new int[]{GENERIC_MAPTYPE_4});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_5), new int[]{GENERIC_MAPTYPE_5});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_CHINA), new int[]{GENERIC_MAPTYPE_CHINA});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_6), new int[]{GENERIC_MAPTYPE_6});
        MAP_SEQUENCES.put(new Integer(GENERIC_MAPTYPE_7), new int[]{GENERIC_MAPTYPE_6, GENERIC_MAPTYPE_7});


        MAP_TYPE_NAMES.put("GOOGLEMAP", new Integer(GOOGLEMAP));
        MAP_TYPE_NAMES.put("GOOGLESATELLITE", new Integer(GOOGLESATELLITE));
        MAP_TYPE_NAMES.put("GOOGLEHYBRID", new Integer(GOOGLEHYBRID));
        MAP_TYPE_NAMES.put("GOOGLECHINA", new Integer(GOOGLECHINA));
        MAP_TYPE_NAMES.put("GOOGLETERREN", new Integer(GOOGLETERREN));
        MAP_TYPE_NAMES.put("YAHOOMAP", new Integer(YAHOOMAP));
        MAP_TYPE_NAMES.put("YAHOOSATELLITE", new Integer(YAHOOSATELLITE));
        MAP_TYPE_NAMES.put("YAHOOHYBRID", new Integer(YAHOOHYBRID));
        MAP_TYPE_NAMES.put("YAHOOINDIAMAP", new Integer(YAHOOINDIAMAP));
        MAP_TYPE_NAMES.put("YAHOOINDIAHYBRID", new Integer(YAHOOINDIAHYBRID));
        MAP_TYPE_NAMES.put("MICROSOFTMAP", new Integer(MICROSOFTMAP));
        MAP_TYPE_NAMES.put("MICROSOFTSATELLITE", new Integer(MICROSOFTSATELLITE));
        MAP_TYPE_NAMES.put("MICROSOFTHYBRID", new Integer(MICROSOFTHYBRID));
        MAP_TYPE_NAMES.put("MICROSOFTCHINA", new Integer(MICROSOFTCHINA));
        MAP_TYPE_NAMES.put("MICROSOFTTERREN", new Integer(MICROSOFTTERREN));
        MAP_TYPE_NAMES.put("OPENSTREETMAP", new Integer(OPENSTREETMAP));
        MAP_TYPE_NAMES.put("OPENSATELLITETMAP", new Integer(OPENSATELLITETMAP));
        MAP_TYPE_NAMES.put("OPENCYCLEMAP", new Integer(OPENCYCLEMAP));
        MAP_TYPE_NAMES.put("OSMAMAP", new Integer(OSMAMAP));
        MAP_TYPE_NAMES.put("NOKIAMAP", new Integer(NOKIAMAP));
        MAP_TYPE_NAMES.put("MAPABCCHINA", new Integer(MAPABCCHINA));

        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_1", new Integer(GENERIC_MAPTYPE_1));
        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_2", new Integer(GENERIC_MAPTYPE_2));
        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_3", new Integer(GENERIC_MAPTYPE_3));
        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_4", new Integer(GENERIC_MAPTYPE_4));
        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_5", new Integer(GENERIC_MAPTYPE_5));
        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_6", new Integer(GENERIC_MAPTYPE_6));
        MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_7", new Integer(GENERIC_MAPTYPE_7));
         MAP_TYPE_NAMES.put("GENERIC_MAPTYPE_CHINA", new Integer(GENERIC_MAPTYPE_CHINA));


    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private constructor.
     */
    private MapType() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12AUG2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Manually set the map tile url template
     * @param mType map type.
     * @param urlTemplate url tempalte
     */
    public static void setCustomMapTileUrl(ICustomMapType customMapTypeUrl){
        customMapType=customMapTypeUrl;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12AUG2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Manually set the map tile url template
     * @param mType map type.
     * @param urlTemplate url tempalte
     */
    public static void setMapTileUrl(int mType,String urlTemplate){
        synchronized (MapType.MAP_TYPE_URLS){
            MapType.MAP_TYPE_URLS.put(new Integer(mType), urlTemplate);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * update MapTile Url.
     */
    public static void updateMapTileUrl() {
        try {
            synchronized (syncObject) {
                restoreMapUrls();
                Request.get(QUERY_URL + "version", null, null, mapTileUrlQuery, "version");
                syncObject.wait(5000);
            }
        } catch (Exception e) {
            Log.p(e.getMessage());
        }
    }
    /**
     * record store
     */
    private static RecordStore mapDataRecordStore = null;

    private static byte[] toByteArray(Integer mapType, String url) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(mapType.intValue());
            dos.writeUTF(url);
            dos.close();
            baos.close();
            return baos.toByteArray();
        } catch (IOException ex) {
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Save map image cache to persistent memory.
     */
    static void saveMapUrls() {

        try {
            RecordStore.deleteRecordStore(MAP_TILE_URL_RECORDSTORE_NAME);
        } catch (RecordStoreException ingore) {
        }
        try {

            synchronized (MAP_TYPE_URLS) {
                mapDataRecordStore = RecordStore.openRecordStore(MAP_TILE_URL_RECORDSTORE_NAME, true);
                byte[] version = new byte[1];
                version[0] = (byte) VERSION;
                mapDataRecordStore.addRecord(version, 0, 1);
                Enumeration emu = MAP_TYPE_URLS.keys();
                while (emu.hasMoreElements()) {
                    Integer mapTypeIndex = (Integer) emu.nextElement();
                    String Url = (String) MAP_TYPE_URLS.get(mapTypeIndex);
                    byte[] recordDate = toByteArray(mapTypeIndex, Url);
                    if (recordDate != null) {
                        mapDataRecordStore.addRecord(recordDate, 0, recordDate.length);
                    }

                }

            }
            mapDataRecordStore.closeRecordStore();
            mapDataRecordStore = null;

        } catch (RecordStoreException e) {
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Restore map image cache from persistent memory.
     */
    static void restoreMapUrls() {
        {
            try {
                synchronized (MAP_TYPE_URLS) {
                    MAP_TYPE_URLS.clear();
                    if (mapDataRecordStore == null) {
                        mapDataRecordStore = RecordStore.openRecordStore(MAP_TILE_URL_RECORDSTORE_NAME, false);

                        int numOfRecords = mapDataRecordStore.getNumRecords();
                        if (numOfRecords > 0) {
                            byte[] recordDate = mapDataRecordStore.getRecord(1);
                            VERSION = recordDate[0];
                        }
                        for (int i = 1; i < numOfRecords; i++) {
                            byte[] recordDate = mapDataRecordStore.getRecord(i + 1);
                            ByteArrayInputStream bais = new ByteArrayInputStream(recordDate);
                            DataInputStream dis = new DataInputStream(bais);
                            try {
                                int mapType = dis.readInt();
                                String Url = dis.readUTF();
                                MAP_TYPE_URLS.put(new Integer(mapType), Url);
                                bais.close();
                                dis.close();
                            } catch (IOException ex) {
                            }




                        }
                        mapDataRecordStore.closeRecordStore();
                        mapDataRecordStore = null;
                    }
                }

            } catch (Exception ignore) {
            }
        }

    }

    private static String replaceMetaString(String input, int mtype, int x, int y, int zoomLevel) {
        int digit = ((x + y) % 4);
        zoomLevel = NUMZOOMLEVELS - zoomLevel;
        String[] pattern = new String[]{
            "{GOOGLE_DIGIT}",
            "{X}",
            "{Y}",
            "{ZOOM}",
            "{GALILEO}",
            "{MS_DIGIT}",
            "{QUAD}",
            "{YAHOO_DIGIT}",
            "{YAHOO_Y}",
            "{YAHOO_ZOOM}",
            "{YAHOO_ZOOM_2}",
            "{OAM_ZOOM}",
            "{NOKIA_ZOOM}"
        };

        String quad = "";
        for (int i = zoomLevel - 1; i >= 0; i--) {
            quad = quad + (((((y >> i) & 1) << 1) + ((x >> i) & 1)));
        }
        String[] replace = new String[]{
            Integer.toString(digit),//"{GOOGLE_DIGIT}"
            Integer.toString(x),//"{X}"
            Integer.toString(y),//"{Y}"
            Integer.toString(zoomLevel),//"{ZOOM}"
            "Galileo".substring(0,(3 * x + y) % 8),//"{GALILEO}"
            Integer.toString((((y & 1) << 1) + (x & 1))),//"{MS_DIGIT}"
            quad,//"{QUAD}"
            Integer.toString((1 + ((x + y) % 3))),//"{YAHOO_DIGIT}"
            Integer.toString((((1 << (zoomLevel)) >> 1) - 1 - y)),//"{YAHOO_Y}"
            Integer.toString(zoomLevel + 1),//"{YAHOO_ZOOM}"
            Integer.toString(NUMZOOMLEVELS - zoomLevel + 1),//"{YAHOO_ZOOM_2}"
            Integer.toString(NUMZOOMLEVELS - zoomLevel),//"{OAM_ZOOM}"
            Integer.toString(zoomLevel)//"{NOKIA_ZOOM}"
        };
        String url = Utils.replace(pattern, replace, input);
        return url;
    }

    static String getTileURL(int mtype, int x, int y, int zoomLevel) {
        String retUrl="";
        if(customMapType!=null){
            retUrl=customMapType.getTileURL(mtype, x, y, NUMZOOMLEVELS-zoomLevel);
        }
        if(retUrl!=null && retUrl.length()!=0){
            return retUrl;
        }else{

            Integer mapTypeIndex = new Integer(mtype);
            String metaURL = null;
            synchronized (MAP_TYPE_URLS) {
                metaURL = (String) MAP_TYPE_URLS.get(mapTypeIndex);
            }
            if (metaURL != null) {
                return replaceMetaString(metaURL, mtype, x, y, zoomLevel);
            } else {
                return getTileInternalURL(mtype, x, y, zoomLevel);
            }
        }

    }

    private static String getTileInternalURL(int mtype, int x, int y, int zoomLevel) {

        String url = "";
        mtype = mtype % (MapType.MAXMAPTYPE + 1);
        String galileo="Galileo".substring(0,(3 * x + y) % 8);
        switch (mtype) {
            case MapType.GOOGLETERREN:
                url = "http://mt" + ((x + y) % 4) + ".google.com/vt/lyrs=t@108,r@138";
                url +="&hl=en&s="+galileo;
                url += "&x=" + x + "&y=" + y + "&z=" + (NUMZOOMLEVELS-zoomLevel);
                break;
         case MapType.MAPABCCHINA:
                url = "http://emap" + ((x + y) % 4) + ".mapabc.com/mapabc/maptile?v=";
                url += "w2.99";
                url += "&x=" + x + "&y=" + y + "&zoom=" + zoomLevel;
                break;
            case MapType.GOOGLECHINA:
                url = "http://mt"+((x + y) % 4)+".google.cn/vt/lyrs=m@138&hl=zh-cn&gl=cn";
                url += "&s="+galileo;
                url += "&x=" + x + "&y=" + y + "&zoom=" + zoomLevel;
                break;
            case MapType.GOOGLEMAP:
                url = "http://mt" + ((x + y) % 4) + ".google.com/vt/lyrs=m@138&hl=en";
                url += "&s="+galileo;
                url += "&x=" + x + "&y=" + y + "&z=" + (NUMZOOMLEVELS - zoomLevel);
                break;
            case MapType.GOOGLEHYBRID:
                url = "http://mt" + ((x + y) % 4) + ".google.com/vt/lyrs=h@138&hl=en";
                url += "&s="+galileo;
                url += "&x=" + x + "&y=" + y + "&z=" + (NUMZOOMLEVELS - zoomLevel);
                break;

            case MapType.GOOGLESATELLITE:
            {
                url = "http://khm" + ((x + y) % 4) + ".google.com/kh/v=58";
                url += "&s="+galileo;
                url += "&x=" + x + "&y=" + y + "&z=" + (NUMZOOMLEVELS - zoomLevel);
                break;
            }
            case MapType.YAHOOMAP:
                url = "http://maps" + (1 + ((x + y) % 3)) + ".yimg.com/hx/";
                url += "tl?v=4.3";
                url += "&.intl=en&x=" + x + "&y=" + (((1 << (NUMZOOMLEVELS - zoomLevel)) >> 1) - 1 - y)
                        + "&z=" + (NUMZOOMLEVELS - zoomLevel + 1) + "&r=1";
                break;
            case MapType.YAHOOSATELLITE:
                url = "http://maps" + (1 + ((x + y) % 3)) + ".yimg.com/ae/ximg?";
                url += "v=1.9&t=a&s=256";
                url += "&.intl=en&x=" + x + "&y=" + (((1 << (NUMZOOMLEVELS - zoomLevel)) >> 1) - 1 - y)
                        + "&z=" + (NUMZOOMLEVELS - zoomLevel + 1) + "&r=1";
                break;
                
            case MapType.YAHOOHYBRID:
                url = "http://maps" + (1 + ((x + y) % 3)) + ".yimg.com/hx/";
                url += "tl?v=4.3&t=h";
                url += "&.intl=en&x=" + x + "&y=" + (((1 << (NUMZOOMLEVELS - zoomLevel)) >> 1) - 1 - y)
                        + "&z=" + (NUMZOOMLEVELS - zoomLevel + 1) + "&r=1";
                break;

            
            case MapType.YAHOOINDIAHYBRID:
            case MapType.YAHOOINDIAMAP:
                url = "http://maps.yimg.com/hw/tile?locale=en&imgtype=png&yimgv=1.2&v=4.1";
                url += "&x=" + x + "&y=" + (((1 << (NUMZOOMLEVELS - zoomLevel)) >> 1) - 1 - y)
                        + "&z=" + (zoomLevel + 1);
                break;
            case MapType.MICROSOFTHYBRID:
            case MapType.MICROSOFTMAP:
            case MapType.MICROSOFTSATELLITE:
                url = "http://ecn.t";
                url += (((y & 1) << 1) + (x & 1))
                        + ".tiles.virtualearth.net/tiles/";
                url += (mtype == MapType.MICROSOFTMAP)
                        ? "r"
                        : (mtype == MapType.MICROSOFTSATELLITE)
                        ? "a" : "h";
                for (int i = NUMZOOMLEVELS - zoomLevel - 1; i >= 0; i--) {
                    url = url + (((((y >> i) & 1) << 1) + ((x >> i) & 1)));
                }
                url +=".png?g=556";
                break;
            case MapType.MICROSOFTCHINA:
                url = "http://r";
                url += (((y & 1) << 1) + (x & 1))
                        + ".tiles.ditu.live.com/tiles/";
                url += "r";
                for (int i = NUMZOOMLEVELS - zoomLevel - 1; i >= 0; i--) {
                    url = url + (((((y >> i) & 1) << 1) + ((x >> i) & 1)));
                }
                url += ".png?g=54";
                break;
            case MapType.MICROSOFTTERREN:
                url = "http://ecn.t";
                url += (((y & 1) << 1) + (x & 1))
                        + ".tiles.virtualearth.net/tiles/";
                url += "r";
                for (int i = NUMZOOMLEVELS - zoomLevel - 1; i >= 0; i--) {
                    url = url + (((((y >> i) & 1) << 1) + ((x >> i) & 1)));
                }
                url += ".png?g=556&mkt=en-us&shading=hill&n=z";
                break;
            case MapType.ASKDOTCOMHYBRID:
            case MapType.ASKDOTCOMMAP:
            case MapType.ASKDOTCOMSATELLITE:
                url = (zoomLevel > 6)
                        ? "http://mapstatic"
                        : "http://mapcache";
                url += ((x + y) % 4 + 1) + ".ask.com/";
                url += (mtype == MapType.ASKDOTCOMMAP)
                        ? "map"
                        : (mtype == MapType.ASKDOTCOMSATELLITE)
                        ? "sat" : "mapt";
                url += "/" + (zoomLevel + 2) + "/";
                url += (x - ((1 << (NUMZOOMLEVELS - zoomLevel)) >> 1))
                        + "/" + (y - ((1 << (NUMZOOMLEVELS - zoomLevel)) >> 1));
                url += "?partner=&tc=28";
                break;
            case MapType.OPENSTREETMAP:
                url = "http://tile.openstreetmap.org/"
                        + (NUMZOOMLEVELS - zoomLevel)
                        + "/" + x + "/" + y + ".png";
                break;
            case MapType.NOKIAMAP:
                url = "http://maptile.svc.nokia.com.edgesuite.net/maptiler/maptile/0.1.22.103/normal.day/"
                        + (NUMZOOMLEVELS - zoomLevel)
                        + "/" + x + "/" + y + "/256/png";
                break;
            case MapType.OPENSATELLITETMAP:
                url = "http://tile.openaerialmap.org/tiles/?v=mgm&;layer=openaerialmap-900913";
                url += "&x=" + x + "&y=" +y
                        + "&z=" + zoomLevel;
                break;
            case MapType.OPENCYCLEMAP:
                url = "http://andy.sandbox.cloudmade.com/tiles/cycle/";
                url +=  (NUMZOOMLEVELS - zoomLevel)
                        + "/" + x + "/" + y +".png";
                break;
            case MapType.OSMAMAP:
                url = "http://tah.openstreetmap.org/Tiles/tile/";
                url +=  (NUMZOOMLEVELS - zoomLevel)
                        + "/" + x + "/" + y +".png";
                break;
        }
        return url;
    }

    static ICustomMapType customMapType=null;
    /**
     * total zoom levels
     */
    static final int NUMZOOMLEVELS = 17;
    static int VERSION = 0;
    static MapTileUrlQuery mapTileUrlQuery = new MapTileUrlQuery();
    static String QUERY_URL = "http://www.mapdigit.com/guidebeemap/config.php?q=";
    final static Object syncObject = new Object();
    /**
     * record store name
     */
    private static final String MAP_TILE_URL_RECORDSTORE_NAME = "Guidebee_MapTileUrl";
}

class MapTileUrlQuery implements IRequestListener {

    public void readProgress(Object context, int bytes, int total) {
    }

    public void writeProgress(Object context, int bytes, int total) {
    }

    public void done(Object context, Response response) throws Exception {
        synchronized (MapType.syncObject) {
            if (context == "version") {
                if (response != null) {
                    try {
                        String ver = response.getResult().getAsString("Version");
                        int verNo = Integer.parseInt(ver);
                        if (verNo > MapType.VERSION || verNo==0) {
                            MapType.VERSION=verNo;
                            Request.get(MapType.QUERY_URL + "maptileurl", null, null, MapType.mapTileUrlQuery, "maptileurl");
                        } else {
                            MapType.syncObject.notifyAll();
                        }
                    } catch (Exception e) {
                    }
                }
            } else if (context == "maptileurl") {
                if (response != null) {
                    final Result result = response.getResult();
                    int count = result.getSizeOfArray("maptileUrls");
                    synchronized (MapType.MAP_TYPE_URLS) {
                        MapType.MAP_TYPE_URLS.clear();
                        for (int i = 0; i < count; i++) {
                            String mapType = result.getAsString("maptileUrls[" + i + "].type");
                            String mapTileUrl = result.getAsString("maptileUrls[" + i + "].URL");
                            Integer mapTypeIndex = (Integer) MapType.MAP_TYPE_NAMES.get(mapType);
                            if (mapTypeIndex != null) {
                                MapType.MAP_TYPE_URLS.put(mapTypeIndex, mapTileUrl);
                            }
                        }
                    }

                    MapType.saveMapUrls();
                }
                MapType.syncObject.notifyAll();
            }
        }
    }
}
