//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17OCT2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Date;

import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.gis.geometry.GeoPolyline;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17OCT2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 *  This class is used to store driving directions results
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 17/10/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapDirection extends MapObject{

    /**
     * Waypoints along the direction.
     */
    public MapPoint [] geoCodes=null;
    
    /**
     * total distance in meters.
     */
    public double distance;
    
    /**
     * total duration in seconds.
     */
    public double duration;
    
    /**
     * the polyline of this direction.
     */
    public GeoPolyline polyline;
    
    /**
     * total routes included in this direction.
     */
    public MapRoute [] routes;
    
    /**
     * Summary information about this direction.
     */
    public String summary;
    
    /**
     * Response status of this query.
     */
    public int status;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>MapDirection</code> object.
     */
    public MapDirection() {
        setType(MapObject.DIRECTION);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Cpoy Constructs a new <code>MapDirection</code> object.
     * @param mapDirection copy from.
     */
    public MapDirection(MapDirection mapDirection) {
        setType(MapObject.DIRECTION);
        distance=mapDirection.distance;
        duration=mapDirection.duration;
        geoCodes=new MapPoint[mapDirection.geoCodes.length];
        for(int i=0;i<geoCodes.length;i++){
            geoCodes[i]=new MapPoint(mapDirection.geoCodes[i]);
        }
        name=mapDirection.name;
        objectNote=mapDirection.objectNote;
        mapInfo_ID=mapDirection.mapInfo_ID;
        status=mapDirection.status;
        summary=mapDirection.summary;
        highlighted=mapDirection.highlighted;
        polyline=new GeoPolyline(mapDirection.polyline);
        routes=new MapRoute[mapDirection.routes.length];
        for(int i=0;i<routes.length;i++){
            routes[i]=new MapRoute();
            routes[i].distance=mapDirection.routes[i].distance;
            routes[i].duration=mapDirection.routes[i].duration;
            routes[i].endGeocode=new MapPoint(mapDirection.routes[i].endGeocode);
            routes[i].startGeocode=new MapPoint(mapDirection.routes[i].startGeocode);
            routes[i].summary=mapDirection.routes[i].summary;
            routes[i].steps=new MapStep[mapDirection.routes[i].steps.length];
            for(int j=0;j<routes[i].steps.length;j++){
                routes[i].steps[j]=new MapStep();
                routes[i].steps[j].bearing=mapDirection.routes[i].steps[j].bearing;
                routes[i].steps[j].bounds=new GeoLatLngBounds(mapDirection.routes[i].steps[j].bounds);
                routes[i].steps[j].cacheAccessTime=new Date();
                routes[i].steps[j].description=mapDirection.routes[i].steps[j].description;
                routes[i].steps[j].descriptionEnglish=mapDirection.routes[i].steps[j].descriptionEnglish;
                routes[i].steps[j].calculatedDirectionType=mapDirection.routes[i].steps[j].calculatedDirectionType;
                routes[i].steps[j].distance=mapDirection.routes[i].steps[j].distance;
                routes[i].steps[j].firstLatLng=new GeoLatLng(mapDirection.routes[i].steps[j].firstLatLng);
                routes[i].steps[j].firstLocationIndex=mapDirection.routes[i].steps[j].firstLocationIndex;
                routes[i].steps[j].lastLatLng=new GeoLatLng(mapDirection.routes[i].steps[j].lastLatLng);
                routes[i].steps[j].lastLocationIndex=mapDirection.routes[i].steps[j].lastLocationIndex;
                routes[i].steps[j].mapInfo_ID=mapDirection.routes[i].steps[j].mapInfo_ID;
                routes[i].steps[j].name=mapDirection.routes[i].steps[j].name;
                routes[i].steps[j].directionCommandElements=new MapDirectionCommandElement[mapDirection.routes[i].steps[j].directionCommandElements.length];
                for (int k = 0; k < routes[i].steps[j].directionCommandElements.length; k++) {
                    if (mapDirection.routes[i].steps[j].directionCommandElements[k] != null) {
                        routes[i].steps[j].directionCommandElements[k] = new MapDirectionCommandElement(mapDirection.routes[i].steps[j].directionCommandElements[k]);
                    }
                }
            }

        }


    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new MapRoute object.
     * @return a new MapRoute object.
     */
    public static MapRoute newRoute(){
        return new MapRoute();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * calculate map step direction based on geographical shape of the route.
     */
    public void calculateMapStepDirections() {
        for (int i = 0; i < routes.length; i++) {
            MapRoute mapRoute = routes[i];
            GeoLatLng pt1, pt2, pt3;
            int nextIndex;
            int ptIndex;
            if (mapRoute.steps.length > 1) {
                ptIndex = mapRoute.steps[0].firstLocationIndex;
                nextIndex = 1;
                pt1 = polyline.getVertex(ptIndex);
                pt2 = polyline.getVertex(ptIndex + nextIndex);
                while (pt1.x == pt2.x && pt1.y == pt2.y) {
                    nextIndex++;
                    pt1 = polyline.getVertex(ptIndex + nextIndex);
                }
                double headAngle = GeoLatLng.azimuthTo(pt1, pt2);
                if (headAngle < 0) {
                    headAngle += 360;
                }
                mapRoute.steps[0].calculatedDirectionType.type =
                        getMapHeadDirectionCommandTypeByBearing(headAngle);
                mapRoute.steps[0].bearing=(int)(headAngle+0.5);
                if(mapRoute.steps[0].currentRoadName==null ||
                        mapRoute.steps[0].currentRoadName.length()==0){
                    if(mapRoute.steps[0].directionCommandElements
                            [MapDirectionCommandElement.FROM_ROAD_NAME]!=null){
                        mapRoute.steps[0].currentRoadName=mapRoute.steps[0].directionCommandElements
                            [MapDirectionCommandElement.FROM_ROAD_NAME].description;
                    }else if(mapRoute.steps[0].directionCommandElements
                            [MapDirectionCommandElement.TO_ROAD_NAME]!=null){
                        mapRoute.steps[0].currentRoadName=mapRoute.steps[0].directionCommandElements
                            [MapDirectionCommandElement.TO_ROAD_NAME].description;
                    }
                }

                for (int j = 1; j < mapRoute.steps.length; j++) {

                    nextIndex = 1;
                    ptIndex = mapRoute.steps[j].firstLocationIndex;
                    pt2 = polyline.getVertex(ptIndex);
                    pt1 = polyline.getVertex(ptIndex - nextIndex);
                    while (pt1.x == pt2.x && pt1.y == pt2.y) {
                        nextIndex++;
                        pt1 = polyline.getVertex(ptIndex - nextIndex);
                    }
                    nextIndex = 1;
                    pt3 = polyline.getVertex(ptIndex + nextIndex);
                    while (pt3.x == pt2.x && pt3.y == pt2.y) {
                        nextIndex++;
                        pt3 = polyline.getVertex(ptIndex + nextIndex);
                    }
                    double bearing = GeoLatLng.getBearing(pt1, pt2, pt3);
                    mapRoute.steps[j].bearing = (int)(bearing+0.5);
                    mapRoute.steps[j].calculatedDirectionType.type =
                            getMapDirectionCommandTypeByBearing(bearing);

                    if(mapRoute.steps[j].currentRoadName==null ||
                        mapRoute.steps[j].currentRoadName.length()==0){
                        if (mapRoute.steps[j].directionCommandElements
                                [MapDirectionCommandElement.TO_ROAD_NAME] != null) {
                            mapRoute.steps[j].currentRoadName = mapRoute.steps[j]
                                    .directionCommandElements
                                    [MapDirectionCommandElement.TO_ROAD_NAME].description;
                        } else if (mapRoute.steps[j].directionCommandElements
                                [MapDirectionCommandElement.FROM_ROAD_NAME] != null) {
                            mapRoute.steps[j].currentRoadName = mapRoute.steps[j]
                                    .directionCommandElements
                                    [MapDirectionCommandElement.FROM_ROAD_NAME].description;

                        }else if (mapRoute.steps[j-1].directionCommandElements
                                [MapDirectionCommandElement.TO_ROAD_NAME] != null) {
                            mapRoute.steps[j].currentRoadName = mapRoute.steps[j-1]
                                    .directionCommandElements
                                    [MapDirectionCommandElement.TO_ROAD_NAME].description;
                        }
                        else{
                             String routeName=mapRoute.steps[j-1].currentRoadName;
                             mapRoute.steps[j].currentRoadName=routeName;
                        }
                    }
                }
            } else {
                if(mapRoute.steps[0].currentRoadName==null ||
                        mapRoute.steps[0].currentRoadName.length()==0){
                    if (mapRoute.steps.length == 1) {
                        mapRoute.steps[0].calculatedDirectionType.type
                                = MapDirectionCommandType.COMMAND_INVALID;
                        if (mapRoute.steps[0].directionCommandElements
                                [MapDirectionCommandElement.FROM_ROAD_NAME] != null) {
                            mapRoute.steps[0].currentRoadName
                                    = mapRoute.steps[0].directionCommandElements
                                    [MapDirectionCommandElement.FROM_ROAD_NAME].description;
                        } else if (mapRoute.steps[0].directionCommandElements
                                [MapDirectionCommandElement.TO_ROAD_NAME] != null) {
                            mapRoute.steps[0].currentRoadName
                                    = mapRoute.steps[0].directionCommandElements
                                    [MapDirectionCommandElement.TO_ROAD_NAME].description;
                        }
                    }
                }

            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map step by the point index in the polyline.
     * @param pointIndex point index in the polyline
     * @return the map step object contains the point.
     */
    public MapStep getMapStepIndexByPointIndex(int pointIndex){
        GeoPoint result=getMapRouteStepIndexByPointIndex(pointIndex);
        if(result!=null){
            return routes[(int)result.x].steps[(int)result.y];
        }
        return null;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the map route step by the point index in the polyline.
     * @param pointIndex point index in the polyline
     * @return GeoPoint object, where x is the route index, y is the step index
     */
    public GeoPoint getMapRouteStepIndexByPointIndex(int pointIndex){
        for(int i=0;i<routes.length;i++){
            MapRoute mapRoute=routes[i];
            for(int j=0;j<mapRoute.steps.length;j++){
                MapStep mapStep=mapRoute.steps[j];
                if(mapStep.firstLocationIndex<=pointIndex &&
                        mapStep.lastLocationIndex>pointIndex){
                    GeoPoint geoPoint=new GeoPoint(i,j);
                    return geoPoint;
                }
            }
        }
        return null;

    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get total map step counts for this direction.
     * @return total map step count.
     */
    public int getMapStepCount(){
        int totalStep=0;
        for(int i=0;i<routes.length;i++){
            totalStep+=routes[i].steps.length;
        }
        return totalStep;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the first map step.
     * @return the first map Step;
     */
    public MapStep firstMapStep(){
        currentRouteIndex=0;
        currentMapStepIndex=0;
        return routes[currentRouteIndex].steps[currentMapStepIndex];
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the last map step.
     */
    public MapStep lastMapStep(){
        currentRouteIndex=routes.length-1;
        currentMapStepIndex=routes[currentRouteIndex].steps.length-1;
        return routes[currentRouteIndex].steps[currentMapStepIndex];

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return next map step.
     * @return next map step or null if reaches the end of the direction.
     */
    public MapStep nextMapStep(){
        if(currentMapStepIndex<routes[currentRouteIndex].steps.length-1){
            currentMapStepIndex++;
        }else if(currentRouteIndex<routes.length-1){
            currentRouteIndex++;
            currentMapStepIndex=0;
        }else{
            return null;
        }
        return routes[currentRouteIndex].steps[currentMapStepIndex];
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return prev map step.
     * @return prve map step or null if reaches the start of the direction.
     */
    public MapStep prevMapStep(){
        if(currentMapStepIndex>0){
            currentMapStepIndex--;
        }else if(currentRouteIndex>0){
            currentRouteIndex--;
            currentMapStepIndex=routes[currentRouteIndex].steps.length-1;
        }else{
            return null;
        }
        return routes[currentRouteIndex].steps[currentMapStepIndex];
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get map step at given index
     * @param index the index of the map step.
     * @return map step object or null if out of boundary.
     */
    public MapStep getMapStepAt(int index){
        if(index>=0 && index<getMapStepCount()){
            int mapStepIndex=0;
            for(int i=0;i<routes.length;i++){
                for(int j=0;j<routes[i].steps.length;j++){
                    mapStepIndex++;
                    if(mapStepIndex==index){
                        currentMapStepIndex=j;
                        currentRouteIndex=i;
                        return routes[currentRouteIndex].steps[currentMapStepIndex];
                    }
                }
            }
        }
        return null;
        
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * jump to given map step on the direction.
     * @param mapStep
     */
    public void jumpToStep(MapStep mapStep) {
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes[i].steps.length; j++) {

                if (routes[i].steps[j] == mapStep) {
                    currentMapStepIndex = j;
                    currentRouteIndex = i;
                    return;
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Debug only ,for printing.
     */
    public String toString() {
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < routes.length; i++) {
            MapRoute mapRoute = routes[i];
            for (int j = 0; j < mapRoute.steps.length; j++) {
                MapStep mapStep = mapRoute.steps[j];
                sb.append(mapStep.toString());

            }
        }
        return sb.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the remaining distance of this route.
     * @param location current location
     * @return the distance (in meters) ,-1 invalid input.
     */
    public GeoPoint getRemainingDistance(GeoLatLng location){
        double remainingDistance=-1;
        GeoPoint finalResult = new GeoPoint();
        GeoPoint result = polyline.IndexOfClosestdistanceToPoly(location);
       int nearestPoint = (int) result.y;
        GeoPoint result1 = this.getMapRouteStepIndexByPointIndex(nearestPoint);
        int routeIndex = (int) result1.x;
        int stepIndex = (int) result1.y;
        remainingDistance = getRemainingDistance(location, routeIndex, stepIndex);
        finalResult.x = remainingDistance;
        for (int i = stepIndex + 1; i < routes[routeIndex].steps.length; i++) {
            remainingDistance += routes[routeIndex].steps[i].distance;
        }
        for (int i = routeIndex + 1; i < routes.length; i++) {
            for (int j = 0; j < routes[i].steps.length; j++) {
                remainingDistance += routes[i].steps[j].distance;
            }
        }
        finalResult.y=remainingDistance;
        return finalResult;


    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17OCT2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the remaining distance along given step.
     * @param location current location
     * @param stepIndex the index of the map step.
     * @return the distance (in meters) .
     */
    private double getRemainingDistance(GeoLatLng location, int routeIndex,
            int stepIndex){
        if(routeIndex>routes.length-1 ||
                stepIndex>routes[routeIndex].steps.length-1){
            throw new IllegalArgumentException("Index out of boundary");
        }
        double remainingDistance=0;
        MapStep mapStep=routes[routeIndex].steps[stepIndex];
        int startIndex=mapStep.firstLocationIndex;
        int endIndex=mapStep.lastLocationIndex;
        GeoLatLng pt=polyline.getVertex(startIndex);
        double nearestDistance=pt.distanceFrom(location);
        int nearestIndex=startIndex;
        for(int i=startIndex+1;i<endIndex;i++){
            pt=polyline.getVertex(i);
            double dis=pt.distanceFrom(location);
            if(dis<nearestDistance){
                nearestIndex=i;
                nearestDistance=dis;
            }
        }

        //if(result.x<5)
        {
            remainingDistance=0;
            if(nearestIndex+1<endIndex)
            {
                GeoLatLng pt1=polyline.getVertex(nearestIndex);
                GeoLatLng pt2=polyline.getVertex(nearestIndex+1);
                double bearing=GeoLatLng.getBearing(pt1, location, pt2);
                if((bearing>=0 && bearing<=5) ||
                        (bearing>=355 && bearing<=360)){//the location is on the right side of pt1
                    nearestIndex+=1;
                }
                pt1=polyline.getVertex(nearestIndex);
                remainingDistance=GeoLatLng.distance(location, pt1)*1000;
                for(int i=nearestIndex+1;i<endIndex;i++ ){
                    pt2=polyline.getVertex(i);
                    remainingDistance+=GeoLatLng.distance(pt1, pt2)*1000;
                    pt1=pt2;
                }
            }else{
                GeoLatLng pt1=location;
                GeoLatLng pt2=polyline.getVertex(endIndex);
                remainingDistance=GeoLatLng.distance(pt1, pt2)*1000;
            }
        }
        
        return remainingDistance;

    }

    private int currentRouteIndex=0;
    private int currentMapStepIndex=0;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get head command type based on bearing
     * @param bearing the bearing the routes
     * @return the head command type.
     */
    private int getMapHeadDirectionCommandTypeByBearing(double bearing){
        int dirType=getMapDirectionCommandTypeByBearing(bearing);
        int retType=MapDirectionCommandType.COMMAND_INVALID;
        switch(dirType){
            case MapDirectionCommandType.COMMAND_NO_TURN:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_N;
                break;
            case MapDirectionCommandType.COMMAND_BEAR_RIGHT:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_NE;
                break;
            case MapDirectionCommandType.COMMAND_TURN_RIGHT:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_E;
                break;
            case MapDirectionCommandType.COMMAND_SHARP_RIGHT:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_SE;
                break;
            case MapDirectionCommandType.COMMAND_U_TURN:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_S;
                break;
            case MapDirectionCommandType.COMMAND_SHARP_LEFT:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_SW;
                break;
            case MapDirectionCommandType.COMMAND_TURN_LEFT:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_W;
                break;
            case MapDirectionCommandType.COMMAND_BEAR_LEFT:
                retType=MapDirectionCommandType.COMMAND_HEAD_DIRECTION_NW;
                break;

        }
        return retType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get direction type based on bearing of the route.
     * @param bearing bearing of the route step.
     * @return the direction command type.
     */
    private int getMapDirectionCommandTypeByBearing(double bearing){
        int type=MapDirectionCommandType.COMMAND_INVALID;
        if((bearing>=350 && bearing<360) || (bearing>=0 && bearing<10)){
            type=MapDirectionCommandType.COMMAND_NO_TURN;
        }else if(bearing>=10 && bearing<40){
            type=MapDirectionCommandType.COMMAND_BEAR_RIGHT;
        }else if(bearing>=40 && bearing<112.5){
            type=MapDirectionCommandType.COMMAND_TURN_RIGHT;
        }else if(bearing>=112.5 && bearing<175){
            type=MapDirectionCommandType.COMMAND_SHARP_RIGHT;
        }else if(bearing>=175 && bearing<185){
            type=MapDirectionCommandType.COMMAND_U_TURN;
        }else if(bearing>=185 && bearing<247.5){
            type=MapDirectionCommandType.COMMAND_SHARP_LEFT;
        }else if(bearing>=247.5 && bearing<320){
            type=MapDirectionCommandType.COMMAND_TURN_LEFT;
        }else if(bearing>=320 && bearing<350){
            type=MapDirectionCommandType.COMMAND_BEAR_LEFT;
        }
        return type;
    }

}
