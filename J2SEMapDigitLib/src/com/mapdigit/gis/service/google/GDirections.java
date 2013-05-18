//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.google;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.service.SearchOptions;

import com.mapdigit.ajax.Arg;
import com.mapdigit.ajax.Request;
import com.mapdigit.ajax.IRequestListener;
import com.mapdigit.ajax.Response;
import com.mapdigit.ajax.Result;

import com.mapdigit.util.HTML2Text;

import com.mapdigit.gis.DigitalMap;
import com.mapdigit.gis.MapDirection;
import com.mapdigit.gis.MapRoute;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPolyline;
import com.mapdigit.gis.raster.MapType;
import com.mapdigit.gis.service.DigitalMapService;
import com.mapdigit.gis.service.IDirectionQuery;
import com.mapdigit.gis.service.IRoutingListener;
import com.mapdigit.util.Log;
import com.mapdigit.util.Utils;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import com.mapdigit.network.HttpConnection;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is used to obtain driving directions results.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class GDirections implements IDirectionQuery {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set google china or not.
     * @param china query china or not.
     */
    public void setChina(boolean china) {
        isChina = china;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set google query key.
     * @param key google query key.
     */
    public void setGoogleKey(String key) {
        queryKey = key;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public GDirections() {
        directionQuery = new DirectionQuery();

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method issues a new directions query. The query parameter is
     * a string containing any valid directions query,
     * e.g. "from: Seattle to: San Francisco" or
     * "from: Toronto to: Ottawa to: New York".
     * @param query the directions query string
     * @param listener the routing listener.
     */
    public void getDirection(String query, IRoutingListener listener) {

        this.listener = listener;
        this.routeQuery = query;
        try {
            query = com.mapdigit.util.HTML2Text.encodeutf8(query.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        setGoogleKey(GoogleMapService.getGoogleKey());
        String input = GoogleMapService.getMapServiceURL(GoogleMapService.DIRECTION);
        if (input != null) {
            String queryURL = replaceMetaString(query, input);
            Request.get(queryURL, null, null, directionQuery, this);
        } else {

            Vector argList = new Vector();
            argList.addElement(new Arg("q", query));
            argList.addElement(new Arg("output", "js"));
            argList.addElement(new Arg("oe", "utf8"));
            argList.addElement(new Arg("key", queryKey));
            SearchOptions routeOptions = DigitalMapService.getSearchOptions();
            String dirOption = "";
            if (routeOptions.AvoidHighway) {
                dirOption = "h";
            }
            if (routeOptions.AvoidTollway) {
                dirOption += "t";
            }
            switch (routeOptions.RoutingType) {
                case SearchOptions.ROUTE_TYPE_COMMUTING:
                    break;
                case SearchOptions.ROUTE_TYPE_DRIVING:
                    break;
                case SearchOptions.ROUTE_TYPE_WALKING:
                    dirOption = "w";
                    break;

            }
            if (dirOption.length() != 0) {
                argList.addElement(new Arg("dirflg", dirOption));
            }
            if (!needSecondQuery) {
                argList.addElement(new Arg("hl", "en-US"));
            } else {
                argList.addElement(new Arg("hl", routeOptions.LanguageID));
            }
            final Arg[] args = new Arg[argList.size() + 1];
            argList.copyInto(args);
            args[argList.size()] = null;
            if (!isChina) {

                Request.get(SEARCH_BASE, args, null, directionQuery, this);
            } else {

                Request.get(SEARCH_BASE_CHINA, args, null, directionQuery, this);
            }
        }
    }

    private String replaceMetaString(String query, String input) {

        String[] pattern = new String[]{
            "{QUERY}",
            "{GOOGLE_KEY}",
            "{OPTION}",
            " ",};

        String option = "";
        SearchOptions routeOptions = DigitalMapService.getSearchOptions();
        String dirOption = "";
        if (routeOptions.AvoidHighway) {
            dirOption = "h";
        }
        if (routeOptions.AvoidTollway) {
            dirOption += "t";
        }
        switch (routeOptions.RoutingType) {
            case SearchOptions.ROUTE_TYPE_COMMUTING:
                break;
            case SearchOptions.ROUTE_TYPE_DRIVING:
                break;
            case SearchOptions.ROUTE_TYPE_WALKING:
                dirOption = "w";
                break;

        }
        if (dirOption.length() != 0) {
            option = "&dirflg=" + dirOption;
        }

        String[] replace = new String[]{
            query,
            queryKey,
            option,
            "+"
        };

        String url = Utils.replace(pattern, replace, input);
        return url;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void getDirection(int mapType, String query, IRoutingListener listener) {
        isChina = mapType == MapType.MICROSOFTCHINA
                || mapType == MapType.GOOGLECHINA
                || mapType == MapType.MAPABCCHINA
                || mapType == MapType.GENERIC_MAPTYPE_CHINA;
        setChina(isChina);
        getDirection(query, listener);
    }
    private static final String SEARCH_BASE = "http://maps.google.com/maps/nav";
    //private static final String SEARCH_BASE_CHINA =  "http://ditu.google.cn/maps/nav";
    private static final String SEARCH_BASE_CHINA = "http://maps.google.com/maps/nav";
    private String queryKey = "ABQIAAAAi44TY0V29QjeejKd2l3ipRTRERdeAiwZ9EeJWta3L_JZVS0bOBQlextEji5FPvXs8mXtMbELsAFL0w";
    DirectionQuery directionQuery = null;
    MapDirection mapDirection = null;
    MapDirection mapDirectionEnglish = null;
    IRoutingListener listener = null;
    String routeQuery = null;
    boolean isChina = false;
    private boolean needSecondQuery = false;
    private boolean returnedResult = false;

    public void getDirection(GeoLatLng[] waypoints, IRoutingListener listener) {
        if (waypoints != null && waypoints.length > 1) {
            String queryString = "from:" + "@"
                    + waypoints[0].lat()
                    + "," + waypoints[0].lng();
            for (int i = 1; i < waypoints.length; i++) {

                queryString += " to:" + "@"
                        + waypoints[i].lat()
                        + "," + waypoints[i].lng();
            }
            getDirection(queryString, listener);
        }

    }

    class DirectionQuery implements IRequestListener {

        HTML2Text html2Text = new HTML2Text();

        DirectionQuery() {
        }

        private void searchResponse(GDirections gDirection, final Response response) {

            final Throwable ex = response.getException();
            if (ex != null || response.getCode() != HttpConnection.HTTP_OK) {
                if (ex instanceof OutOfMemoryError) {
                    Log.p("Dont have enough memory", Log.ERROR);
                    if (gDirection.listener != null) {
                        gDirection.listener.done(null, null);
                    }
                } else {
                    Log.p("Error connecting to search service", Log.ERROR);
                    if (gDirection.listener != null) {
                        gDirection.listener.done(gDirection.routeQuery, null);
                    }
                }

                return;
            }
            try {
                final Result result = response.getResult();
                MapDirection currentMapDirection;
                if (!needSecondQuery) {
                    gDirection.mapDirectionEnglish = new MapDirection();
                    currentMapDirection = gDirection.mapDirectionEnglish;
                } else {
                    gDirection.mapDirection = new MapDirection();
                    currentMapDirection = gDirection.mapDirection;

                }

                currentMapDirection.name = result.getAsString("name");
                currentMapDirection.status = result.getAsInteger("Status.code");
                currentMapDirection.duration = result.getAsInteger("Directions.Duration.seconds");
                currentMapDirection.distance = result.getAsInteger("Directions.Distance.meters");
                currentMapDirection.summary = html2Text.convert(result.getAsString("Directions.summaryHtml"));
                if (!needSecondQuery) {
                    String points = result.getAsString("Directions.Polyline.points");
                    String levels = result.getAsString("Directions.Polyline.levels");
                    int zoomFactor = result.getAsInteger("Directions.Polyline.zoomFactor");
                    int numLevels = result.getAsInteger("Directions.Polyline.numLevels");
                    currentMapDirection.polyline = GeoPolyline.fromEncoded(0x00FF00, 4, 1, points,
                            zoomFactor, levels, numLevels);
                }
                int numOfGeocodes = result.getSizeOfArray("Placemark");
                if (numOfGeocodes > 0) {
                    currentMapDirection.geoCodes = new MapPoint[numOfGeocodes];
                    for (int i = 0; i < numOfGeocodes; i++) {
                        currentMapDirection.geoCodes[i] = new MapPoint();
                        currentMapDirection.geoCodes[i].name = result.getAsString("Placemark[" + i + "].address");
                        String location = result.getAsString("Placemark[" + i + "].Point.coordinates");
                        GeoLatLng latLng = DigitalMap.fromStringToLatLng(location);
                        currentMapDirection.geoCodes[i].setPoint(latLng);

                    }
                }

                int numOfRoutes = result.getSizeOfArray("Directions.Routes");
                if (numOfRoutes > 0) {
                    currentMapDirection.routes = new MapRoute[numOfRoutes];
                    for (int i = 0; i < numOfRoutes; i++) {
                        String routeString = "Directions.Routes[" + i + "]";
                        currentMapDirection.routes[i] = MapDirection.newRoute();
                        currentMapDirection.routes[i].summary = html2Text.convert(result.getAsString(routeString + ".summaryHtml"));
                        currentMapDirection.routes[i].distance = result.getAsInteger(routeString + ".Distance.meters");
                        currentMapDirection.routes[i].duration = result.getAsInteger(routeString + ".Duration.seconds");
                        String lastLatLng = result.getAsString(routeString + ".End.coordinates");
                        currentMapDirection.routes[i].lastLatLng = DigitalMap.fromStringToLatLng(lastLatLng);
                        int numOfSteps = result.getSizeOfArray(routeString + ".Steps");
                        if (numOfSteps > 0) {
                            currentMapDirection.routes[i].steps = new MapStep[numOfSteps];
                            for (int j = 0; j < numOfSteps; j++) {
                                String stepString = routeString + ".Steps[" + j + "]";
                                currentMapDirection.routes[i].steps[j] = MapRoute.newStep();
                                currentMapDirection.routes[i].steps[j].description = html2Text.convert(result.getAsString(stepString + ".descriptionHtml"));
                                currentMapDirection.routes[i].steps[j].descriptionEnglish = currentMapDirection.routes[i].steps[j].description;
                                currentMapDirection.routes[i].steps[j].distance = result.getAsInteger(stepString + ".Distance.meters");
                                currentMapDirection.routes[i].steps[j].duration = result.getAsInteger(stepString + ".Duration.seconds");
                                currentMapDirection.routes[i].steps[j].firstLocationIndex = result.getAsInteger(stepString + ".polylineIndex");
                                String firstLocation = result.getAsString(stepString + ".Point.coordinates");
                                currentMapDirection.routes[i].steps[j].firstLatLng = DigitalMap.fromStringToLatLng(firstLocation);
                                if (!needSecondQuery) {
                                    currentMapDirection.routes[i].steps[j].directionCommandElements = GDirectionCommandAnalyzer.analyse(currentMapDirection.routes[i].steps[j].description);
                                }

                            }
                        }

                    }
                }

            } catch (OutOfMemoryError rex) {
                if (gDirection.listener != null) {
                    gDirection.listener.done(null, null);
                }
                return;

            } catch (Exception rex) {
                if (gDirection.listener != null) {
                    gDirection.listener.done(gDirection.routeQuery, null);
                }
                return;

            }
            if (gDirection.listener != null) {
                try {
                    MapDirection currentMapDirection;
                    if (!needSecondQuery) {
                        currentMapDirection = gDirection.mapDirectionEnglish;
                    } else {
                        currentMapDirection = gDirection.mapDirection;
                    }

                    if (!needSecondQuery) {

                        if (currentMapDirection.geoCodes.length == currentMapDirection.routes.length + 1) {
                            for (int i = 0; i < currentMapDirection.routes.length; i++) {
                                currentMapDirection.routes[i].startGeocode = currentMapDirection.geoCodes[i];
                                currentMapDirection.routes[i].endGeocode = currentMapDirection.geoCodes[i + 1];
                            }
                        }

                        for (int i = 0; i < currentMapDirection.routes.length; i++) {
                            MapRoute mapRoute = currentMapDirection.routes[i];
                            for (int j = 0; j < mapRoute.steps.length - 1; j++) {
                                MapStep mapStep = mapRoute.steps[j];

                                mapStep.lastLocationIndex = mapRoute.steps[j + 1].firstLocationIndex;
                                mapStep.lastLatLng = currentMapDirection.polyline.getVertex(mapRoute.steps[j + 1].firstLocationIndex);
                            }
                            mapRoute.steps[mapRoute.steps.length - 1].lastLocationIndex =
                                    currentMapDirection.polyline.getVertexCount() - 1;
                            mapRoute.steps[mapRoute.steps.length - 1].lastLatLng =
                                    currentMapDirection.polyline.getVertex(currentMapDirection.polyline.getVertexCount() - 1);
                        }
                        GeoPolyline polyline = currentMapDirection.polyline;
                        if (polyline.getVertexCount() > 1) {
                            GeoLatLng latLngTemp = polyline.getVertex(0);
                            currentMapDirection.bounds = new GeoLatLngBounds(latLngTemp, latLngTemp);

                            for (int i = 0; i < currentMapDirection.routes.length; i++) {
                                MapRoute mapRoute = currentMapDirection.routes[i];
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
                                        currentMapDirection.bounds.add(latLng.lng(), latLng.lat());
                                    }

                                }

                            }
                        }


                        currentMapDirection.calculateMapStepDirections();
                        //check last step ,need to check if it has destination on the left/right
                        int numOfRoutes = currentMapDirection.routes.length;
                        int stepLength = currentMapDirection.routes[numOfRoutes - 1].steps.length;
                        MapStep lastMapStep = currentMapDirection.routes[numOfRoutes - 1].steps[stepLength - 1];
                        MapStep newStep = GDirectionCommandAnalyzer.analyseLastStep(lastMapStep);
                        if (newStep != null) {
                            MapStep[] oldSteps = currentMapDirection.routes[numOfRoutes - 1].steps;
                            currentMapDirection.routes[numOfRoutes - 1].steps = new MapStep[stepLength + 1];
                            System.arraycopy(oldSteps, 0, currentMapDirection.routes[numOfRoutes - 1].steps,
                                    0, stepLength);
                            currentMapDirection.routes[numOfRoutes - 1].steps[stepLength] = newStep;
                        }
                    }

                    //this is the actual return.
                    SearchOptions routeOptions = DigitalMapService.getSearchOptions();
                    if (routeOptions.LanguageID.equalsIgnoreCase("en-US") && !returnedResult) {
                        needSecondQuery = false;

                    } else {
                        needSecondQuery = true;

                    }

                    if (!needSecondQuery || returnedResult) {
                        if (needSecondQuery && gDirection.mapDirection != null) {
                            gDirection.mapDirection.polyline = gDirection.mapDirectionEnglish.polyline;
                            gDirection.mapDirection.bounds = gDirection.mapDirectionEnglish.bounds;

                            //copy the map step's direction for english one.
                            for (int i = 0; i < gDirection.mapDirection.routes.length; i++) {
                                MapRoute mapRoute = gDirection.mapDirection.routes[i];
                                MapRoute mapRouteEnglish = gDirection.mapDirectionEnglish.routes[i];
                                mapRoute.bounds = mapRouteEnglish.bounds;
                                mapRoute.startGeocode = mapRouteEnglish.startGeocode;
                                mapRoute.endGeocode = mapRouteEnglish.endGeocode;
                                MapStep[] oldSteps = mapRoute.steps;
                                mapRoute.steps = new MapStep[mapRouteEnglish.steps.length];
                                System.arraycopy(mapRouteEnglish.steps, 0, mapRoute.steps,
                                        0, mapRouteEnglish.steps.length);

                                for (int j = 0; j < oldSteps.length; j++) {
                                    MapStep mapStep = oldSteps[j];
                                    MapStep mapStepEnglish = mapRoute.steps[j];
                                    mapStepEnglish.description = mapStep.description;

                                }
                                if (oldSteps.length + 1 == mapRoute.steps.length) {
                                    mapRoute.steps[mapRoute.steps.length - 1].descriptionEnglish = mapRoute.steps[mapRoute.steps.length - 1].description;
                                    mapRoute.steps[mapRoute.steps.length - 1].description = "";
                                }

                            }
                        }
                        if (!needSecondQuery) {
                            gDirection.mapDirection = gDirection.mapDirectionEnglish;
                        }
                        gDirection.listener.done(gDirection.routeQuery, gDirection.mapDirection);
                        needSecondQuery = false;
                        returnedResult = false;
                    } else {
                        getDirection(routeQuery, listener);
                    }
                    returnedResult = true;
                } catch (OutOfMemoryError err) {
                    gDirection.listener.done(null, null);
                }
            }

        }

        public void readProgress(final Object context, final int bytes, final int total) {
            GDirections gDirection = (GDirections) context;
            if (gDirection.listener != null) {
                gDirection.listener.readProgress(bytes, total);
            }
        }

        public void writeProgress(final Object context, final int bytes, final int total) {
        }

        public void done(final Object context, final Response response) throws Exception {

            GDirections gDirection = (GDirections) context;
            searchResponse(gDirection, response);
        }
    }
}
