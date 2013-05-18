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
package com.mapdigit.gis.service.cloudmade;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.service.SearchOptions;
import com.mapdigit.network.HttpConnection;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;

import com.mapdigit.ajax.json.JSONArray;
import com.mapdigit.util.HTML2Text;

import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapRoute;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPolyline;
import com.mapdigit.gis.service.DigitalMapService;
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
public final class CDirections implements IDirectionQuery {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 30DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public CDirections() {
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
     * @param query the directions query string.Longitude,Latitude lists.
     * @param listener the routing listener.
     */
    public void getDirection(String query, IRoutingListener listener) {

        this.listener = listener;
        this.routeQuery = query;
        queryKey = CloudMadeMapService.getCloudMadeKey();
        String[] lists = Utils.tokenize(query, ',');
        if (lists != null && lists.length > 3) {
            Vector argList = new Vector();
            String startPoint = lists[1] + "," + lists[0];
            String endPoint = lists[3] + "," + lists[2];
            if (lists.length / 2 > 2) {
                StringBuffer sb = new StringBuffer();
                for (int i = 2; i < lists.length / 2; i++) {
                    sb.append(lists[i * 2 + 1]);
                    sb.append(",");
                    sb.append(lists[i * 2]);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length());
                argList.addElement(new Arg("transit_points", sb.toString()));

            }
            SearchOptions routeOptions = DigitalMapService.getSearchOptions();
            String routeType;
            switch (routeOptions.RoutingType) {
                case SearchOptions.ROUTE_TYPE_COMMUTING:
                    routeType = "bicycle";
                    break;

                case SearchOptions.ROUTE_TYPE_WALKING:
                    routeType = "foot";
                    break;
                default:
                    routeType = "car";
                    break;


            }
            argList.addElement(new Arg("lang", routeOptions.LanguageID.substring(0, 2)));
            switch (routeOptions.RouteUnit) {
                case SearchOptions.ROUTE_UNIT_KM:
                    argList.addElement(new Arg("units", "km"));
                    break;
                case SearchOptions.ROUTE_UNIT_MILE:
                    argList.addElement(new Arg("units", "mile"));
                    break;
            }

            final Arg[] args = new Arg[argList.size() + 1];
            argList.copyInto(args);
            args[argList.size()] = null;
            String searchBase = replaceMetaString(startPoint, endPoint, routeType, "");
            Request.get(searchBase, args, null, directionQuery, this);
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
    private static final String SEARCH_BASE = "http://routes.cloudmade.com/{CLOUDMADE_KEY}/api/0.3/{START_POINT},{END_POINT}/{ROUTE_TYPE}{ROUTE_TYPE_MODIFIER}.js";
    private String queryKey = "8ee2a50541944fb9bcedded5165f09d9";
    DirectionQuery directionQuery = null;
    MapDirection mapDirection = null;
    IRoutingListener listener = null;
    String routeQuery = null;

    private String replaceMetaString(String startPoint,
            String endPoint, String routeType, String routeTypeModifier) {

        String[] pattern = new String[]{
            "{CLOUDMADE_KEY}",
            "{START_POINT}",
            "{END_POINT}",
            "{ROUTE_TYPE}",
            "{ROUTE_TYPE_MODIFIER}",
            " ",};



        String[] replace = new String[]{
            queryKey,
            startPoint,
            endPoint,
            routeType,
            routeTypeModifier,
            "+"
        };

        String url = Utils.replace(pattern, replace, SEARCH_BASE);
        return url;
    }

    class DirectionQuery implements IRequestListener {

        HTML2Text html2Text = new HTML2Text();

        DirectionQuery() {
        }

        ////////////////////////////////////////////////////////////////////////////
        //--------------------------------- REVISIONS ------------------------------
        // Date       Name                 Tracking #         Description
        // ---------  -------------------  -------------      ----------------------
        // 04JAN2010  James Shen                 	          Code review
        ////////////////////////////////////////////////////////////////////////////
        /**
         * Convert String to Latitude,Longitude pair, the input string has this format
         * [Latitude,longitude] for example  [-31.948275,115.857562]
         * @param location  location string
         * @return the geographical coordinates.
         */
        private GeoLatLng fromStringToLatLng(String location) {
            location = location.trim();
            location = location.substring(1, location.length() - 2);
            int commaIndex = location.indexOf(",");
            String latitude = location.substring(0, commaIndex);
            String longitude = location.substring(commaIndex + 1);
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);
            return new GeoLatLng(lat, lng);
        }

        private void fillMapStepInfo(MapStep mapStep, Object[] instructions) {
            
            String[] lists = new String[instructions.length];
            for(int i=0;i<lists.length;i++){
                lists[i]=instructions[i].toString();
            }
            mapStep.description = lists[0];
            mapStep.distance = Double.parseDouble(lists[1]);
            mapStep.firstLocationIndex = Integer.parseInt(lists[2]);
            mapStep.duration = Double.parseDouble(lists[3]);
            mapStep.bearing = 0;
            String command = "C";
            if (lists.length > 7) {
                mapStep.bearing = (int) Double.parseDouble(lists[8]);
                command = lists[7];
            }
            mapStep.directionCommandElements = CDirectionCommandAnalyzer.analyse(command, "");

        }

        private void searchResponse(CDirections cDirection, final Response response) {

            final Throwable ex = response.getException();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if (ex instanceof OutOfMemoryError) {
                    Log.p("Dont have enough memory", Log.ERROR);
                    if (cDirection.listener != null) {
                        cDirection.listener.done(null, null);
                    }
                } else {
                    Log.p("Error connecting to search service", Log.ERROR);
                    if (cDirection.listener != null) {
                        cDirection.listener.done(cDirection.routeQuery, null);
                    }
                }

                return;
            }
            try {
                final Result result = response.getResult();
                cDirection.mapDirection.name = "";
                int status = result.getAsInteger("status");
                if (status == 0) {
                    cDirection.mapDirection.status = 200;
                } else {
                    cDirection.mapDirection.status = 400;
                }
                cDirection.mapDirection.distance = result.getAsInteger("route_summary.total_distance");
                cDirection.mapDirection.duration = result.getAsDouble("route_summary.total_time");
                cDirection.mapDirection.summary = "Total distance:" + cDirection.mapDirection.distance + "m" + " Estimated time:" + cDirection.mapDirection.duration + "s";
                int totalPoint = result.getSizeOfArray("route_geometry");
                if (totalPoint > 0) {
                    Vector level8CoordsVector = new Vector();
                    String pointPath = "route_geometry";
                    JSONArray jsonArray = result.getAsArray(pointPath);
                    for (int i = 0; i < totalPoint; i++) {
                        String geometry = jsonArray.getJSONArray(i).toString();

                        GeoLatLng latLng = fromStringToLatLng(geometry);
                        level8CoordsVector.addElement(latLng);
                    }
                    GeoLatLng[] latLngs = new GeoLatLng[level8CoordsVector.size()];
                    level8CoordsVector.copyInto(latLngs);
                    cDirection.mapDirection.polyline = new GeoPolyline(latLngs, 0x00FF00, 1, 1);
                    cDirection.mapDirection.polyline.numLevels = 2;
                    //fill the gecode for map direciton.
                    cDirection.mapDirection.geoCodes = new MapPoint[2];
                    cDirection.mapDirection.geoCodes[0] = new MapPoint();
                    cDirection.mapDirection.geoCodes[0].name = "Start";
                    cDirection.mapDirection.geoCodes[0].setPoint(latLngs[0]);
                    cDirection.mapDirection.geoCodes[1] = new MapPoint();
                    cDirection.mapDirection.geoCodes[1].name = "End";
                    cDirection.mapDirection.geoCodes[1].setPoint(latLngs[latLngs.length - 1]);

                }


                int numOfSteps = result.getSizeOfArray("route_instructions");
                cDirection.mapDirection.routes = new MapRoute[1];
                cDirection.mapDirection.routes[0] = MapDirection.newRoute();
                cDirection.mapDirection.routes[0].steps = new MapStep[numOfSteps];
                cDirection.mapDirection.routes[0].startGeocode = cDirection.mapDirection.geoCodes[0];
                cDirection.mapDirection.routes[0].endGeocode = cDirection.mapDirection.geoCodes[1];
                cDirection.mapDirection.routes[0].summary = cDirection.mapDirection.summary;
                cDirection.mapDirection.routes[0].distance = cDirection.mapDirection.distance;
                cDirection.mapDirection.routes[0].duration = cDirection.mapDirection.duration;
                JSONArray route_instruction = result.getAsArray("route_instructions");
                for (int i = 0; i < numOfSteps; i++) {
                    cDirection.mapDirection.routes[0].steps[i] = MapRoute.newStep();
                    JSONArray jsonArray=route_instruction.getJSONArray(i);
                    Object []objects=new Object[jsonArray.length()];
                    for(int k=0;k<objects.length;k++){
                        objects[k]=jsonArray.get(k);
                    }
                    fillMapStepInfo(cDirection.mapDirection.routes[0].steps[i],
                            objects);
                }


            } catch (OutOfMemoryError rex) {
                if (cDirection.listener != null) {
                    cDirection.listener.done(null, null);
                }
                return;

            } catch (Exception rex) {
                if (cDirection.listener != null) {
                    cDirection.listener.done(cDirection.routeQuery, null);
                }
                return;

            }
            if (cDirection.listener != null) {
                try {
                    MapDirection mapDirection = cDirection.mapDirection;

                    for (int i = 0; i < mapDirection.routes.length; i++) {
                        MapRoute mapRoute = mapDirection.routes[i];
                        for (int j = 0; j < mapRoute.steps.length - 1; j++) {
                            MapStep mapStep = mapRoute.steps[j];
                            mapStep.firstLatLng = mapDirection.polyline.getVertex(mapStep.firstLocationIndex);
                            mapStep.lastLocationIndex = mapRoute.steps[j + 1].firstLocationIndex;
                            mapStep.lastLatLng = mapDirection.polyline.getVertex(mapRoute.steps[j + 1].firstLocationIndex);
                        }
                        mapRoute.steps[mapRoute.steps.length - 1].lastLocationIndex =
                                mapDirection.polyline.getVertexCount() - 1;
                        mapRoute.steps[mapRoute.steps.length - 1].lastLatLng =
                                mapDirection.polyline.getVertex(mapDirection.polyline.getVertexCount() - 1);
                    }

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
                                    polyline.setLevel(k, 16);

                                }

                            }

                        }
                    }
                    cDirection.listener.done(cDirection.routeQuery, mapDirection);
                } catch (OutOfMemoryError err) {
                    cDirection.listener.done(null, null);
                }
            }

        }

        public void readProgress(final Object context, final int bytes, final int total) {
            CDirections cDirection = (CDirections) context;
            if (cDirection.listener != null) {
                cDirection.listener.readProgress(bytes, total);
            }
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {
            CDirections cDirection = (CDirections) context;
            cDirection.mapDirection = new MapDirection();
            searchResponse(cDirection, response);
        }
    }
}
