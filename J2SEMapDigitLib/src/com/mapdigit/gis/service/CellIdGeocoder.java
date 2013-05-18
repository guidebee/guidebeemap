//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Part;
import com.mapdigit.ajax.PostData;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.util.Log;
import com.mapdigit.network.HttpConnection;

////////////////////////////////////////////////////////////////////////////////
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
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class CellIdGeocoder implements ICellIdGeocoder {

    private static final String SEARCH_BASE = "http://www.google.com/glm/mmap";
    IGeocodingListener listener = null;
    CellIdAddressQuery addressQuery = null;
    String searchCellInfo;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public CellIdGeocoder() {
        addressQuery = new CellIdAddressQuery();
    }

    public void getLocations(String stringMMC, String stringMNC, String stringLAC, String stringCID,
            IGeocodingListener listener) {
        long MCC, MNC, LAC, CID;
        try {
            this.listener=listener;
            MCC = Long.parseLong(stringMMC);
            MNC = Long.parseLong(stringMNC);
            LAC = Long.parseLong(stringLAC);
            CID = Long.parseLong(stringCID);
            byte[] pd = PostData(MCC, MNC, LAC, CID,
                    false);
             Arg[] httpArgs=new Arg[1];
             httpArgs[0] = new Arg("ContentType", "application/binary");
             Part part=new Part(pd,null);
             PostData postData=new PostData(new Part[]{part},"");
             Request.post(SEARCH_BASE, null, httpArgs, addressQuery, postData, this);
             searchCellInfo=stringMMC + "-" + stringMNC + "-" + stringLAC + "-" + stringCID;
        } catch (Exception e) {
            if (listener != null) {
                listener.done(searchCellInfo,  null);
            }
        }





    }

    private static byte[] PostData(long MCC, long MNC, long LAC, long CID,
            boolean shortCID) {
        /* The shortCID parameter follows heuristic experiences:
         * Sometimes UMTS CIDs are build up from the original GSM CID (lower 4 hex digits)
         * and the RNC-ID left shifted into the upper 4 digits.
         */
        byte[] pd = new byte[]{
            0x00, 0x0e,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00,
            0x00, 0x00,
            0x00, 0x00,
            0x1b,
            0x00, 0x00, 0x00, 0x00, // Offset 0x11
            0x00, 0x00, 0x00, 0x00, // Offset 0x15
            0x00, 0x00, 0x00, 0x00, // Offset 0x19
            0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, // Offset 0x1f
            0x00, 0x00, 0x00, 0x00, // Offset 0x23
            0x00, 0x00, 0x00, 0x00, // Offset 0x27
            0x00, 0x00, 0x00, 0x00, // Offset 0x2b
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            0x00, 0x00, 0x00, 0x00
        };

        if (shortCID) {
            CID &= 0xFFFF;      /* Attempt to resolve the cell using the
                                    GSM CID part */
        }

        if (CID > 65536) /* GSM: 4 hex digits, UTMS: 6 hex
        digits */ {
            pd[0x1c] = 5;
        } else {
            pd[0x1c] = 3;
        }

        pd[0x11] = (byte) ((MNC >> 24) & 0xFF);
        pd[0x12] = (byte) ((MNC >> 16) & 0xFF);
        pd[0x13] = (byte) ((MNC >> 8) & 0xFF);
        pd[0x14] = (byte) ((MNC >> 0) & 0xFF);

        pd[0x15] = (byte) ((MCC >> 24) & 0xFF);
        pd[0x16] = (byte) ((MCC >> 16) & 0xFF);
        pd[0x17] = (byte) ((MCC >> 8) & 0xFF);
        pd[0x18] = (byte) ((MCC >> 0) & 0xFF);

        pd[0x27] = (byte) ((MNC >> 24) & 0xFF);
        pd[0x28] = (byte) ((MNC >> 16) & 0xFF);
        pd[0x29] = (byte) ((MNC >> 8) & 0xFF);
        pd[0x2a] = (byte) ((MNC >> 0) & 0xFF);

        pd[0x2b] = (byte) ((MCC >> 24) & 0xFF);
        pd[0x2c] = (byte) ((MCC >> 16) & 0xFF);
        pd[0x2d] = (byte) ((MCC >> 8) & 0xFF);
        pd[0x2e] = (byte) ((MCC >> 0) & 0xFF);

        pd[0x1f] = (byte) ((CID >> 24) & 0xFF);
        pd[0x20] = (byte) ((CID >> 16) & 0xFF);
        pd[0x21] = (byte) ((CID >> 8) & 0xFF);
        pd[0x22] = (byte) ((CID >> 0) & 0xFF);

        pd[0x23] = (byte) ((LAC >> 24) & 0xFF);
        pd[0x24] = (byte) ((LAC >> 16) & 0xFF);
        pd[0x25] = (byte) ((LAC >> 8) & 0xFF);
        pd[0x26] = (byte) ((LAC >> 0) & 0xFF);

        return pd;
    }

    private void searchResponse(CellIdGeocoder geoCoder, final Response response) {
        if (response.getCode() != HttpConnection.HTTP_OK) {
            Log.p("Error connecting to search service", Log.ERROR);
            if (geoCoder.listener != null) {
                geoCoder.listener.done(searchCellInfo, null);
            }
            return;
        }try{
             byte []ps=response.getRawContentArray();
             short opcode1 = (short)(ps[0] << 8 | ps[1]);
                    byte opcode2 = ps[2];
                    int ret_code = (int)((ps[3] << 24) | (ps[4] << 16) |
                                   (ps[5] << 8) | (ps[6]));
                    if (ret_code == 0)
                    {
                        int [] values=new int[4];
                        for(int i=0;i<4;i++){
                            if(ps[7+i]<0){
                                values[i]=ps[7+i]+256;
                            }else{
                              values[i]=ps[7+i];
                            }
                        }
                        long latLng=(values[0] << 24) | (values[1] << 16)
                                     | (values[2] << 8) | (values[3]);

                        double lat = (double) latLng/1000000.0;

                        for(int i=0;i<4;i++){
                            if(ps[11+i]<0){
                                values[i]=ps[11+i]+256;
                            }else{
                              values[i]=ps[11+i];
                            }
                        }
                        latLng=(values[0] << 24) | (values[1] << 16)
                                     | (values[2] << 8) | (values[3]);

                        double lon = (double) latLng/1000000.0;
                        MapPoint mapPoint=new MapPoint();
                        mapPoint.point.x=lon;
                        mapPoint.point.y=lat;
                        if (listener != null) {
                            listener.done(searchCellInfo, new MapPoint[]{mapPoint});
                        }

                    }else{
                        if (listener != null) {
                            listener.done(searchCellInfo,  null);
                        }
                    }

        }catch (Exception e) {
            if (listener != null) {
                listener.done(searchCellInfo,  null);
            }
        }

    }
    class CellIdAddressQuery implements IRequestListener {

        CellIdAddressQuery() {
        }

        public void readProgress(final Object context, final int bytes, final int total) {
            CellIdGeocoder geoCoder = (CellIdGeocoder) context;
            geoCoder.listener.readProgress(bytes, total);
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            searchResponse((CellIdGeocoder)context,response);
        }
    }
}


