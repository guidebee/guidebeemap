//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 30DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.mapabc;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.network.HttpConnection;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;

import com.mapdigit.util.HTML2Text;

import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.MapRoute;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPolyline;
import com.mapdigit.gis.service.IDirectionQuery;
import com.mapdigit.gis.service.IRoutingListener;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 30DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to obtain driving directions results.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 30/12/10
 * @author      Guidebee Pty Ltd.
 */
public final class MDirections implements IDirectionQuery {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 30DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public MDirections() {
        directionQuery = new DirectionQuery();

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 30DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void getDirection(GeoLatLng[] waypoints, IRoutingListener listener) {
        String queryString = "";
        if (waypoints != null && waypoints.length > 1) {

            for (int i = 0; i < waypoints.length - 1; i++) {

                queryString +=
                        +waypoints[i].lng()
                        + "," + waypoints[i].lat() + ",";
            }

            queryString +=
                    +waypoints[waypoints.length - 1].lng()
                    + "," + waypoints[waypoints.length - 1].lat();
            getDirection(queryString, listener);
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 30DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method issues a new directions query. The query parameter is
     * a string containing any valid directions query,
     * @param query the directions query string. Lontitude,Latitude lists.
     * @param listener the routing listener.
     */
    public void getDirection(String query, IRoutingListener listener) {

        this.listener = listener;
        this.routeQuery = query;
        queryKey = MapAbcMapService.getMapAbcKey();
        String []lists=Utils.tokenize(query, ',');
        if(lists!=null){
            Vector argList = new Vector();
            argList.addElement(new Arg("highLight", "false"));
            argList.addElement(new Arg("enc", "utf-8"));
            argList.addElement(new Arg("ver", MapAbcMapService.MAPABC_SERVICE_VER));
            argList.addElement(new Arg("config", "R"));
            if (MapAbcMapService.usingJson) {
                argList.addElement(new Arg("resType", "JSON"));
            } else {
                argList.addElement(new Arg("resType", "XML"));
            }
            for(int i=0;i<lists.length/2;i++){
                argList.addElement(new Arg("x"+(i+1), lists[i*2]));
                argList.addElement(new Arg("y"+(i+1), lists[i*2+1]));
            }
            argList.addElement(new Arg("routeType", "0"));
            argList.addElement(new Arg("per", "150"));
            argList.addElement(new Arg("xs", ""));
            argList.addElement(new Arg("ys", ""));
            argList.addElement(new Arg("a_k", queryKey));
            final Arg[] args = new Arg[argList.size() + 1];
            argList.copyInto(args);
            args[argList.size()] = null;
            Request.get(SEARCH_BASE_CHINA, args, null, directionQuery, this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 30DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void getDirection(int mapType, String query, IRoutingListener listener) {

        getDirection(query, listener);
    }


    private static final String SEARCH_BASE_CHINA = "http://search1.mapabc.com/sisserver";
    private String queryKey = "b0a7db0b3a30f944a21c3682064dc70ef5b738b062f6479a5eca39725798b1ee300bd8d5de3a4ae3";
    DirectionQuery directionQuery = null;
    MapDirection mapDirection = null;
    IRoutingListener listener = null;
    String routeQuery = null;
    boolean isChina = false;

    private static String MINIUTE_CHINESE;
    private static String KM_CHINESE;
    private static String TOTAL_CHINESE;
    private static String ESTIMATEDTIME_CHINESE;

    static{
        try{
            MINIUTE_CHINESE = new String(new byte[]{(byte)0xE5,(byte)0x88,
              (byte)0x86,(byte)0xE9,(byte)0x92,(byte)0x9F}, "utf-8");
            KM_CHINESE = new String(new byte[]{(byte)0xE5,(byte)0x85,
            (byte)0xAC,(byte)0xE9,(byte)0x87,(byte)0x8C}, "utf-8");
            TOTAL_CHINESE = new String(new byte[]{(byte)0xE5,(byte)0x85,
            (byte)0xA8,(byte)0xE9,(byte)0x95,(byte)0xBF}, "utf-8");
            ESTIMATEDTIME_CHINESE = new String(new byte[]{(byte)0xE9,(byte)0xA2,
            (byte)0x84,(byte)0xE8,(byte)0xAE,(byte)0xA1,(byte)0xE9,(byte)0xA9,
            (byte)0xBE,(byte)0xE9,(byte)0xA9,(byte)0xAD,(byte)0xE6,(byte)0x97,
            (byte)0xB6,(byte)0xE9,(byte)0x97,(byte)0xB4}, "utf-8");
        }catch(Exception e){}
    }

    class DirectionQuery implements IRequestListener {

        HTML2Text html2Text = new HTML2Text();

        DirectionQuery() {
        }



        private void searchResponse(MDirections mDirection, final Response response) {

            final Throwable ex = response.getException();
            Vector level16CoordsVector=new Vector();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if (ex instanceof OutOfMemoryError) {
                    Log.p("Dont have enough memory", Log.ERROR);
                    if (mDirection.listener != null) {
                        mDirection.listener.done(null, null);
                    }
                } else {
                    Log.p("Error connecting to search service", Log.ERROR);
                    if (mDirection.listener != null) {
                        mDirection.listener.done(mDirection.routeQuery, null);
                    }
                }

                return;
            }
            try {
                final Result result = response.getResult();
                mDirection.mapDirection.name="";
                mDirection.mapDirection.status=200;
                String level16Coords=result.getAsString("coors");
                String[] level16CoordsList=Utils.tokenize(level16Coords,',');

                Vector level8CoordsVector=new Vector();
                int currentPointIndex=0;
                for(int i=0;i<level16CoordsList.length/2;i++){
                    double lng=Double.parseDouble(level16CoordsList[2*i]);
                    double lat=Double.parseDouble(level16CoordsList[2*i+1]);
                    GeoLatLng latLng=new GeoLatLng(lat,lng);
                    level16CoordsVector.addElement(latLng);
                }
                int numOfSteps = result.getSizeOfArray("segmengList");
                mDirection.mapDirection.routes = new MapRoute[1];
                mDirection.mapDirection.routes[0]=MapDirection.newRoute();
                mDirection.mapDirection.routes[0].steps = new MapStep[numOfSteps];
                for (int i = 0; i < numOfSteps; i++) {
                    String stepString = "segmengList[" + i + "]";
                    mDirection.mapDirection.routes[0].steps[i] = MapRoute.newStep();
                    mDirection.mapDirection.routes[0].steps[i].description = result.getAsString(stepString + ".textInfo");
                    mDirection.mapDirection.routes[0].steps[i].currentRoadName = result.getAsString(stepString + ".roadName");
                    mDirection.mapDirection.routes[0].steps[i].distance = result.getAsDouble(stepString + ".roadLength");
                    String duration = result.getAsString(stepString + ".driveTime");
                    int minIndex = duration.indexOf(MINIUTE_CHINESE);//feizhong
                    if (minIndex > 0) {
                        try {
                            double durationSeconds = Double.parseDouble(duration.substring(0, minIndex));
                            mDirection.mapDirection.routes[0].steps[i].duration = (int) (durationSeconds * 60);
                        } catch (Exception e) {
                        }
                    }
                    mDirection.mapDirection.distance+=mDirection.mapDirection.routes[0].steps[i].distance;
                    mDirection.mapDirection.duration+=mDirection.mapDirection.routes[0].steps[i].duration;
                    int offset=0;
                    if(i!=0) offset=1;
                    String level8Coords = result.getAsString(stepString + ".coor");
                    String[] level8CoordsList = Utils.tokenize(level8Coords, ',');
                    mDirection.mapDirection.routes[0].steps[i].firstLocationIndex=currentPointIndex;
                    currentPointIndex+=(level8CoordsList.length/2-1)-offset;
                    mDirection.mapDirection.routes[0].steps[i].lastLocationIndex=currentPointIndex;
                    for (int j = offset; j < level8CoordsList.length / 2; j++) {
                        double lng = Double.parseDouble(level8CoordsList[2 * j]);
                        double lat = Double.parseDouble(level8CoordsList[2 * j + 1]);
                        GeoLatLng latLng = new GeoLatLng(lat, lng);
                        level8CoordsVector.addElement(latLng);
                    }
                    mDirection.mapDirection.routes[0].steps[i].firstLatLng
                            =(GeoLatLng)level8CoordsVector
                            .elementAt(mDirection.mapDirection.routes[0].steps[i].firstLocationIndex);
                    mDirection.mapDirection.routes[0].steps[i].lastLatLng
                            =(GeoLatLng)level8CoordsVector
                            .elementAt(mDirection.mapDirection.routes[0].steps[i].lastLocationIndex);
                    String actionCommand=result.getAsString(stepString + ".action");
                    mDirection.mapDirection.routes[0].steps[i].directionCommandElements
                            =MDirectionCommandAnalyzer.analyse(actionCommand,mDirection.mapDirection.routes[0].steps[i].currentRoadName);

                }
                //now need to create the map direction polyline.
                GeoLatLng []latLngs=new GeoLatLng[level8CoordsVector.size()];
                level8CoordsVector.copyInto(latLngs);
                mDirection.mapDirection.polyline=new GeoPolyline(latLngs,0x00FF00,1,1);
                mDirection.mapDirection.polyline.numLevels=2;
                //fill the gecode for map direciton.
                mDirection.mapDirection.geoCodes = new MapPoint[2];
                mDirection.mapDirection.geoCodes[0]=new MapPoint();
                mDirection.mapDirection.geoCodes[0].name="Start";
                mDirection.mapDirection.geoCodes[0].setPoint(latLngs[0]);
                mDirection.mapDirection.geoCodes[1]=new MapPoint();
                mDirection.mapDirection.geoCodes[1].name="End";
                mDirection.mapDirection.geoCodes[1].setPoint(latLngs[latLngs.length-1]);
                mDirection.mapDirection.routes[0].startGeocode=mDirection.mapDirection.geoCodes[0];
                mDirection.mapDirection.routes[0].endGeocode=mDirection.mapDirection.geoCodes[1];
                mDirection.mapDirection.summary=TOTAL_CHINESE + (((int) (mDirection.mapDirection.distance/1000)*100)/100) + KM_CHINESE;
                mDirection.mapDirection.summary+=" "+ESTIMATEDTIME_CHINESE + (int)(mDirection.mapDirection.duration/60) + MINIUTE_CHINESE;
                mDirection.mapDirection.routes[0].summary=mDirection.mapDirection.summary;
                mDirection.mapDirection.routes[0].distance=mDirection.mapDirection.distance;
                mDirection.mapDirection.routes[0].duration=mDirection.mapDirection.duration;

            } catch (OutOfMemoryError rex) {
                if (mDirection.listener != null) {
                    mDirection.listener.done(null, null);
                }
                return;

            } catch (Exception rex) {
                if (mDirection.listener != null) {
                    mDirection.listener.done(mDirection.routeQuery, null);
                }
                return;

            }

            if (mDirection.listener != null) {
                try {

                    GeoPolyline polyline = mapDirection.polyline;
                    if (polyline.getVertexCount() > 1) {
                        GeoLatLng latLngTemp = polyline.getVertex(0);
                        mapDirection.bounds = new GeoLatLngBounds(latLngTemp, latLngTemp);

                        for (int i = 0; i < mapDirection.routes.length; i++) {
                            MapRoute mapRoute = mapDirection.routes[i];
                            latLngTemp = polyline.getVertex(mapRoute.steps[0].firstLocationIndex);
                            mapRoute.bounds = new GeoLatLngBounds(latLngTemp, latLngTemp);
                            for (int j = 0; j < mapRoute.steps.length; j++) {
                                latLngTemp = polyline.getVertex(mapRoute.steps[j].firstLocationIndex);
                                MapStep mapStep = mapRoute.steps[j];
                                mapStep.bounds = new GeoLatLngBounds(latLngTemp, latLngTemp);
                                for (int k = mapStep.firstLocationIndex; k <= mapStep.lastLocationIndex; k++) {
                                    GeoLatLng latLng = polyline.getVertex(k);
                                    mapStep.bounds.add(latLng.lng(), latLng.lat());
                                    mapRoute.bounds.add(latLng.lng(), latLng.lat());
                                    mapDirection.bounds.add(latLng.lng(), latLng.lat());
                                    polyline.setLevel(k, 2);
                                    for(int p=0;p<level16CoordsVector.size();p++){
                                        GeoLatLng lngLat16=(GeoLatLng)level16CoordsVector.elementAt(p);
                                        if(((latLng.x-lngLat16.x)*(latLng.x-lngLat16.x)+
                                                (latLng.y-lngLat16.y)*(latLng.y-lngLat16.y))<0.0001){
                                            polyline.setLevel(k, 16);
                                            break;
                                        }
                                    }
                                }

                            }

                        }
                    }
                    mapDirection.calculateMapStepDirections();

                    mDirection.listener.done(mDirection.routeQuery, mapDirection);
                } catch (OutOfMemoryError err) {
                    mDirection.listener.done(null, null);
                }
            }


        }

        public void readProgress(final Object context, final int bytes, final int total) {
            MDirections mDirection = (MDirections) context;
            if (mDirection.listener != null) {
                mDirection.listener.readProgress(bytes, total);
            }
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            MDirections mDirection = (MDirections) context;
            mDirection.mapDirection = new MapDirection();
            searchResponse(mDirection, response);
        }
    }
}
