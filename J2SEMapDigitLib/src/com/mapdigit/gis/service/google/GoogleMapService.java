//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 18DEC2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.google;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.ajax.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.Result;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.MapKey;
import com.mapdigit.gis.service.MapKeyRepository;
import com.mapdigit.util.Log;
import com.mapdigit.rms.RecordStore;
import com.mapdigit.rms.RecordStoreException;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18DEC2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to store driving directions results
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 18/12/09
 * @author      Guidebee Pty Ltd.
 */
public class GoogleMapService extends DigitalMapService {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18DEC2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public GoogleMapService() {
        localSearch = new GLocalSearch();
        geocoder = new GClientGeocoder();
        reverseGeocoder = new GReverseClientGeocoder();
        directionQuery = new GDirections();
        
    }
    
    static boolean usingJson = true;//use JSON or KML
    
    static String getGoogleKey() {
        return MapKeyRepository.getKey(MapKey.MAPKEY_TYPE_GOOGLE);

    }
    
    private static byte[] CHINESE_FULL_NAME_ARRAY = new byte[]{
        (byte) 0xE4, (byte) 0xB8, (byte) 0xAD, (byte) 0xE5, (byte) 0x8D,
        (byte) 0x8E, (byte) 0xE4, (byte) 0xBA, (byte) 0xBA,
        (byte) 0xE6, (byte) 0xB0, (byte) 0x91, (byte) 0xE5,
        (byte) 0x85, (byte) 0xB1, (byte) 0xE5, (byte) 0x92,
        (byte) 0x8C, (byte) 0xE5, (byte) 0x9B, (byte) 0xBD};
    static String CHINESE_FULL_NAME;

    static {
        try {
            CHINESE_FULL_NAME = new String(CHINESE_FULL_NAME_ARRAY, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * map service urls.
     */
    static final Hashtable MAP_SERVICES_URLS = new Hashtable();
    static final String GEOCODERCHINA = "GEOCODERCHINA";
    static final String GEOCODER = "GEOCODER";
    static final String DIRECTION = "DIRECTION";
    static final String LOCALSEARCH = "LOCALSEARCH";
    static final String REVERSEGEOCODER = "REVERSEGEOCODER";
    static final String REVERSEGEOCODERCHINA = "REVERSEGEOCODERCHINA";
    static int VERSION = 0;
    static MapServiceUrlQuery mapServiceUrlQuery = new MapServiceUrlQuery();
    static String QUERY_URL = "http://www.mapdigit.com/guidebeemap/config.php?q=";
    final static Object syncObject = new Object();

    /**
     * map service urls.
     */
    public static final Hashtable MAP_SERVICE_URLS = new Hashtable();
    /**
     * record store name
     */
    private static final String MAP_SERVICE_URL_RECORDSTORE_NAME = "Guidebee_ServiceUrl";

    public static void updateMapServiceUrl() {
        try {
            synchronized (syncObject) {
                restoreMapServiceUrls();
                Request.get(QUERY_URL + "version", null, null, mapServiceUrlQuery, "version");
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

    private static byte[] toByteArray(String mapType, String url) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(mapType);
            dos.writeUTF(url);
            dos.close();
            baos.close();
            return baos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
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
    static void saveMapServiceUrls() {

        try {
            RecordStore.deleteRecordStore(MAP_SERVICE_URL_RECORDSTORE_NAME);
        } catch (RecordStoreException ingore) {
            ingore.printStackTrace();
        }
        try {

            synchronized (MAP_SERVICE_URLS) {
                mapDataRecordStore = RecordStore.openRecordStore(MAP_SERVICE_URL_RECORDSTORE_NAME, true);
                byte[] version = new byte[1];
                version[0] = (byte) VERSION;
                mapDataRecordStore.addRecord(version, 0, 1);
                Enumeration emu = MAP_SERVICE_URLS.keys();
                while (emu.hasMoreElements()) {
                    String mapTypeIndex = (String) emu.nextElement();
                    String Url = (String) MAP_SERVICE_URLS.get(mapTypeIndex);
                    byte[] recordDate = toByteArray(mapTypeIndex, Url);
                    if (recordDate != null) {
                        mapDataRecordStore.addRecord(recordDate, 0, recordDate.length);
                    }

                }

            }
            mapDataRecordStore.closeRecordStore();
            mapDataRecordStore = null;

        } catch (RecordStoreException e) {
            e.printStackTrace();
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
    static void restoreMapServiceUrls() {
        {
            try {
                synchronized (MAP_SERVICE_URLS) {
                    MAP_SERVICE_URLS.clear();
                    if (mapDataRecordStore == null) {
                        mapDataRecordStore = RecordStore.openRecordStore(MAP_SERVICE_URL_RECORDSTORE_NAME, false);

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
                                String mapType = dis.readUTF();
                                String Url = dis.readUTF();
                                MAP_SERVICE_URLS.put(mapType, Url);
                                bais.close();
                                dis.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }




                        }
                        mapDataRecordStore.closeRecordStore();
                        mapDataRecordStore = null;
                    }
                }

            } catch (RecordStoreException ignore) {
                ignore.printStackTrace();
            }
        }

    }

    static String getMapServiceURL(String serviceType){
        synchronized (MAP_SERVICE_URLS) {
            return (String)MAP_SERVICE_URLS.get(serviceType);
        }
    }

}

class MapServiceUrlQuery implements IRequestListener {

    public void readProgress(Object context, int bytes, int total) {
    }

    public void writeProgress(Object context, int bytes, int total) {
    }

    public void done(Object context, Response response) throws Exception {
        synchronized (GoogleMapService.syncObject) {
            if (context == "version") {
                if (response != null) {
                    try {
                        String ver = response.getResult().getAsString("Version");
                        int verNo = Integer.parseInt(ver);
                        if (verNo > GoogleMapService.VERSION || verNo == 0) {
                            GoogleMapService.VERSION = verNo;
                            Request.get(GoogleMapService.QUERY_URL + "serviceurl", null, null, GoogleMapService.mapServiceUrlQuery, "serviceurl");
                        } else {
                            GoogleMapService.syncObject.notifyAll();
                        }
                    } catch (Exception e) {
                    }
                }
            } else if (context == "serviceurl") {
                if (response != null) {
                    final Result result = response.getResult();
                    int count = result.getSizeOfArray("serviceUrls");
                    synchronized (GoogleMapService.MAP_SERVICE_URLS) {
                        GoogleMapService.MAP_SERVICE_URLS.clear();
                        for (int i = 0; i < count; i++) {
                            String mapType = result.getAsString("serviceUrls[" + i + "].type");
                            String mapServiceUrl = result.getAsString("serviceUrls[" + i + "].URL");
                            GoogleMapService.MAP_SERVICE_URLS.put(mapType, mapServiceUrl);
                        }
                    }

                    GoogleMapService.saveMapServiceUrls();
                }
                GoogleMapService.syncObject.notifyAll();
            }
        }
    }
}
