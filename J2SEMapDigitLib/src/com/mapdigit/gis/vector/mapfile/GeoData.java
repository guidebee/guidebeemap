//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector.mapfile;

//--------------------------------- IMPORTS ------------------------------------
import java.io.DataInputStream;
import java.io.IOException;

import com.mapdigit.gis.MapCollection;
import com.mapdigit.gis.MapMultiPline;
import com.mapdigit.gis.MapMultiPoint;
import com.mapdigit.gis.MapMultiRegion;
import com.mapdigit.gis.MapNoneObject;
import com.mapdigit.gis.MapObject;
import com.mapdigit.gis.MapPline;
import com.mapdigit.gis.MapPoint;
import com.mapdigit.gis.MapRegion;
import com.mapdigit.gis.MapText;
import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoPolygon;
import com.mapdigit.gis.geometry.GeoPolyline;
import com.mapdigit.util.DataReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * geo data section of the map file.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class GeoData extends Section {

    /**
     * when store latitude/longitude , it store as integer.
     * to convert to an interget ,it muliple by DOUBLE_PRECISION.
     */
    private static final double DOUBLE_PRECISION = 10000000.0;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public GeoData(DataInputStream reader, long offset, long size)
            throws IOException {
        super(reader, offset, size);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map object at given index.
     */
    public MapObject getMapObject(RecordIndex recordIndex) throws IOException {
        DataReader.seek(reader, recordIndex.recordOffset);
        MapObject mapObject = null;
        switch (recordIndex.mapObjectType) {
            case MapObject.NONE:
                mapObject = new MapNoneObject();
                break;
            case MapObject.POINT:
                mapObject = getMapPoint(recordIndex);
                break;
            case MapObject.MULTIPOINT:
                mapObject = getMapMultiPoint(recordIndex);
                break;
            case MapObject.PLINE:
                mapObject = getMapPline(recordIndex);
                break;
            case MapObject.MULTIPLINE:
                mapObject = getMapMultiPline(recordIndex);
                break;
            case MapObject.REGION:
                mapObject = getMapRegion(recordIndex);
                break;
            case MapObject.MULTIREGION:
                mapObject = getMapMultiRegion(recordIndex);
                break;
            case MapObject.COLLECTION:
                mapObject = getMapCollection(recordIndex);
                break;
            case MapObject.TEXT:
                mapObject = getMapText(recordIndex);
                break;

        }
        return mapObject;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map point object at given index.
     */
    private MapPoint getMapPoint(RecordIndex recordIndex) {
        MapPoint mapPoint = new MapPoint();
        mapPoint.symbolType.shape = recordIndex.param1;
        mapPoint.symbolType.color = recordIndex.param2;
        mapPoint.symbolType.size = recordIndex.param3;
        mapPoint.point.x = ((double) (recordIndex.minX)) / DOUBLE_PRECISION;
        mapPoint.point.y = ((double) (recordIndex.minY)) / DOUBLE_PRECISION;
        mapPoint.bounds.x = mapPoint.point.x;
        mapPoint.bounds.y = mapPoint.point.y;
        mapPoint.bounds.width = 0;
        mapPoint.bounds.height = 0;
        return mapPoint;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map multipoint object at given index.
     */
    private MapMultiPoint getMapMultiPoint(RecordIndex recordIndex)
            throws IOException {
        MapMultiPoint mapMultiPoint = new MapMultiPoint();
        mapMultiPoint.symbolType.shape = recordIndex.param1;
        mapMultiPoint.symbolType.color = recordIndex.param2;
        mapMultiPoint.symbolType.size = recordIndex.param3;
        mapMultiPoint.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapMultiPoint.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapMultiPoint.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapMultiPoint.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        int numberOfPoints = DataReader.readInt(reader);
        mapMultiPoint.points = new GeoLatLng[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            int x = DataReader.readInt(reader);
            int y = DataReader.readInt(reader);
            mapMultiPoint.points[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                    (double) x / DOUBLE_PRECISION);
        }
        return mapMultiPoint;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map pline object at given index.
     */
    private MapPline getMapPline(RecordIndex recordIndex) throws IOException {
        MapPline mapPline = new MapPline();
        mapPline.penStyle.pattern = recordIndex.param1;
        mapPline.penStyle.width = recordIndex.param2;
        mapPline.penStyle.color = recordIndex.param3;
        mapPline.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapPline.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapPline.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapPline.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        int numberOfPoints = DataReader.readInt(reader);
        GeoLatLng[] latLngs = new GeoLatLng[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            int x = DataReader.readInt(reader);
            int y = DataReader.readInt(reader);
            latLngs[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                    (double) x / DOUBLE_PRECISION);
        }
        mapPline.pline = new GeoPolyline(latLngs, mapPline.penStyle.color,
                mapPline.penStyle.width, mapPline.penStyle.pattern);
        return mapPline;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map multipline object at given index.
     */
    private MapMultiPline getMapMultiPline(RecordIndex recordIndex)
            throws IOException {
        MapMultiPline mapMultiPline = new MapMultiPline();
        mapMultiPline.penStyle.pattern = recordIndex.param1;
        mapMultiPline.penStyle.width = recordIndex.param2;
        mapMultiPline.penStyle.color = recordIndex.param3;
        mapMultiPline.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapMultiPline.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapMultiPline.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapMultiPline.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        int numberOfPart = DataReader.readInt(reader);
        mapMultiPline.plines = new GeoPolyline[numberOfPart];
        for (int j = 0; j < numberOfPart; j++) {

            int numberOfPoints = DataReader.readInt(reader);
            GeoLatLng[] latLngs = new GeoLatLng[numberOfPoints];
            for (int i = 0; i < numberOfPoints; i++) {
                int x = DataReader.readInt(reader);
                int y = DataReader.readInt(reader);
                latLngs[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                        (double) x / DOUBLE_PRECISION);
            }
            mapMultiPline.plines[j] = new GeoPolyline(latLngs,
                    mapMultiPline.penStyle.color,
                    mapMultiPline.penStyle.width,
                    mapMultiPline.penStyle.pattern);
        }
        return mapMultiPline;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map region object at given index.
     */
    private MapRegion getMapRegion(RecordIndex recordIndex) throws IOException {
        MapRegion mapRegion = new MapRegion();
        mapRegion.brushStyle.pattern = recordIndex.param1;
        mapRegion.brushStyle.foreColor = recordIndex.param2;
        mapRegion.brushStyle.backColor = recordIndex.param3;
        mapRegion.penStyle.pattern = DataReader.readInt(reader);
        mapRegion.penStyle.width = DataReader.readInt(reader);
        mapRegion.penStyle.color = DataReader.readInt(reader);
        mapRegion.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapRegion.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapRegion.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapRegion.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        int centerX = DataReader.readInt(reader);
        int centerY = DataReader.readInt(reader);
        mapRegion.centerPt.x = (double) centerX / DOUBLE_PRECISION;
        mapRegion.centerPt.y = (double) centerY / DOUBLE_PRECISION;
        int numberOfPoints = DataReader.readInt(reader);
        GeoLatLng[] latLngs = new GeoLatLng[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++) {
            int x = DataReader.readInt(reader);
            int y = DataReader.readInt(reader);
            latLngs[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                    (double) x / DOUBLE_PRECISION);
        }
        mapRegion.region = new GeoPolygon(latLngs, mapRegion.penStyle.color,
                mapRegion.penStyle.width, mapRegion.penStyle.pattern,
                mapRegion.brushStyle.foreColor, mapRegion.brushStyle.pattern);
        return mapRegion;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map multiregion object at given index.
     */
    private MapMultiRegion getMapMultiRegion(RecordIndex recordIndex)
            throws IOException {
        MapMultiRegion mapMultiRegion = new MapMultiRegion();
        mapMultiRegion.brushStyle.pattern = recordIndex.param1;
        mapMultiRegion.brushStyle.foreColor = recordIndex.param2;
        mapMultiRegion.brushStyle.backColor = recordIndex.param3;
        mapMultiRegion.penStyle.pattern = DataReader.readInt(reader);
        mapMultiRegion.penStyle.width = DataReader.readInt(reader);
        mapMultiRegion.penStyle.color = DataReader.readInt(reader);
        mapMultiRegion.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapMultiRegion.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapMultiRegion.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapMultiRegion.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        int centerX = DataReader.readInt(reader);
        int centerY = DataReader.readInt(reader);
        mapMultiRegion.centerPt.x = (double) centerX / DOUBLE_PRECISION;
        mapMultiRegion.centerPt.y = (double) centerY / DOUBLE_PRECISION;
        int numberOfPart = DataReader.readInt(reader);
        mapMultiRegion.regions = new GeoPolygon[numberOfPart];
        for (int j = 0; j < numberOfPart; j++) {
            int numberOfPoints = DataReader.readInt(reader);
            GeoLatLng[] latLngs = new GeoLatLng[numberOfPoints];
            for (int i = 0; i < numberOfPoints; i++) {
                int x = DataReader.readInt(reader);
                int y = DataReader.readInt(reader);
                latLngs[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                        (double) x / DOUBLE_PRECISION);
            }
            mapMultiRegion.regions[j] = new GeoPolygon(latLngs,
                    mapMultiRegion.penStyle.color,
                    mapMultiRegion.penStyle.width,
                    mapMultiRegion.penStyle.pattern,
                    mapMultiRegion.brushStyle.foreColor,
                    mapMultiRegion.brushStyle.pattern);
        }
        return mapMultiRegion;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map collection object at given index.
     */
    private MapCollection getMapCollection(RecordIndex recordIndex)
            throws IOException {
        MapCollection mapCollection = new MapCollection();
        int regionPart = recordIndex.param1;
        int plinePart = recordIndex.param2;
        int multiPointPart = recordIndex.param3;
        mapCollection.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapCollection.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapCollection.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapCollection.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        if (regionPart > 0) {
            mapCollection.multiRegion = new MapMultiRegion();
            mapCollection.multiRegion.brushStyle.pattern = DataReader.readInt(reader);
            mapCollection.multiRegion.brushStyle.foreColor = DataReader.readInt(reader);
            mapCollection.multiRegion.brushStyle.backColor = DataReader.readInt(reader);
            mapCollection.multiRegion.penStyle.pattern = DataReader.readInt(reader);
            mapCollection.multiRegion.penStyle.width = DataReader.readInt(reader);
            mapCollection.multiRegion.penStyle.color = DataReader.readInt(reader);
            int centerX = DataReader.readInt(reader);
            int centerY = DataReader.readInt(reader);
            mapCollection.multiRegion.centerPt.x = (double) centerX / DOUBLE_PRECISION;
            mapCollection.multiRegion.centerPt.y = (double) centerY / DOUBLE_PRECISION;
            mapCollection.multiRegion.regions = new GeoPolygon[regionPart];
            for (int j = 0; j < regionPart; j++) {
                int numberOfPoints = DataReader.readInt(reader);
                GeoLatLng[] latLngs = new GeoLatLng[numberOfPoints];
                for (int i = 0; i < numberOfPoints; i++) {
                    int x = DataReader.readInt(reader);
                    int y = DataReader.readInt(reader);
                    latLngs[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                            (double) x / DOUBLE_PRECISION);
                }
                mapCollection.multiRegion.regions[j] = new GeoPolygon(latLngs,
                        0,
                        0,
                        0,
                        0,
                        0);
            }
        }
        if (plinePart > 0) {
            mapCollection.multiPline = new MapMultiPline();
            mapCollection.multiPline.penStyle.pattern = DataReader.readInt(reader);
            mapCollection.multiPline.penStyle.width = DataReader.readInt(reader);
            mapCollection.multiPline.penStyle.color = DataReader.readInt(reader);
            mapCollection.multiPline.plines = new GeoPolyline[plinePart];
            for (int j = 0; j < plinePart; j++) {

                int numberOfPoints = DataReader.readInt(reader);
                GeoLatLng[] latLngs = new GeoLatLng[numberOfPoints];
                for (int i = 0; i < numberOfPoints; i++) {
                    int x = DataReader.readInt(reader);
                    int y = DataReader.readInt(reader);
                    latLngs[i] = new GeoLatLng((double) y / DOUBLE_PRECISION,
                            (double) x / DOUBLE_PRECISION);
                }
                mapCollection.multiPline.plines[j] = new GeoPolyline(latLngs,
                        0,
                        0,
                        0);
            }
        }
        if (multiPointPart > 0) {
            mapCollection.multiPoint = new MapMultiPoint();
            mapCollection.multiPoint.symbolType.shape = DataReader.readInt(reader);
            mapCollection.multiPoint.symbolType.color = DataReader.readInt(reader);
            mapCollection.multiPoint.symbolType.size = DataReader.readInt(reader);
            multiPointPart = DataReader.readInt(reader);
            mapCollection.multiPoint.points = new GeoLatLng[multiPointPart];
            for (int i = 0; i < multiPointPart; i++) {
                int x = DataReader.readInt(reader);
                int y = DataReader.readInt(reader);
                mapCollection.multiPoint.points[i] =
                        new GeoLatLng((double) y / DOUBLE_PRECISION,
                        (double) x / DOUBLE_PRECISION);
            }
        }
        return mapCollection;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get a map text object at given index.
     */
    private MapText getMapText(RecordIndex recordIndex) throws IOException {
        MapText mapText = new MapText();
        mapText.angle = recordIndex.param1;
        mapText.foreColor = recordIndex.param2;
        mapText.backColor = recordIndex.param3;
        mapText.bounds.x = ((double) recordIndex.minX) / DOUBLE_PRECISION;
        mapText.bounds.y = ((double) recordIndex.minY) / DOUBLE_PRECISION;
        mapText.bounds.width =
                ((double) (recordIndex.maxX - recordIndex.minX)) / DOUBLE_PRECISION;
        mapText.bounds.height =
                ((double) (recordIndex.maxY - recordIndex.minY)) / DOUBLE_PRECISION;
        mapText.justification = DataReader.readInt(reader);
        mapText.spacing = DataReader.readInt(reader);
        mapText.lineType = DataReader.readInt(reader);
        mapText.textString = DataReader.readString(reader);
        return mapText;
    }
}
