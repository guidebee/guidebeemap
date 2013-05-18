//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.mapdigit.gis.vector.DataField;
import com.mapdigit.gis.vector.DataRowValue;
import com.mapdigit.gis.vector.FindCondition;
import com.mapdigit.gis.vector.FindConditions;
import com.mapdigit.gis.vector.rtree.AbstractNode;
import com.mapdigit.gis.vector.rtree.HyperCube;
import com.mapdigit.gis.vector.rtree.Leaf;
import com.mapdigit.gis.vector.rtree.RTree;
import com.mapdigit.gis.MapObject;
import com.mapdigit.gis.geometry.GeoLatLngBounds;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Guidebee Map file reader.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public final class MapFile {
    
    /**
     * file header section object.
     */
    public Header header = null;

    /**
     * tabular data section.
     */
    public TabularData tabularData = null;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param reader  the input stream of the map file.
     */
    public MapFile(DataInputStream reader) {
        this.reader = reader;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open the map file.
     */
    public void open() throws IOException {
        
        if (!reader.markSupported()) {
            throw new IOException("mark is not supported!");
        }
        if (header != null) {
            return;
        }
        reader.mark(Integer.MAX_VALUE);
        header = new Header(reader, 0, 0);
        recordIndex = new RecordIndex(reader, header.indexOffset,
                header.indexLength);
        stringIndex = new StringIndex(reader, header.stringIndexOffset,
                header.stringIndexLength);
        stringData = new StringData(reader, header.stringDataOffset,
                header.stringDataLength);
        geoData = new GeoData(reader, header.geoDataOffset,
                header.geoDataLength);
        tabularData = new TabularData(reader, header.tabularDataOffset,
                header.tabularDataLength, header.fields,
                stringData, stringIndex);
        rtreeIndex = new RTreeIndex(reader, header.rtreeIndexOffset,
                header.rtreeIndexLength);
        tree = new RTree(rtreeIndex.File);
   }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the map file.
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the total record number.
     * @return the total record number.
     */
    public int getRecordCount() {
        if (header != null) {
            return header.recordCount;
        }
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get MapObject at given mapInfoID.
     * @param mapInfoID the index of the record(MapInfoID).
     * @return MapObject at given mapInfoID.
     */
    public MapObject getMapObject(int mapInfoID) throws IOException {
        recordIndex.getRecord(mapInfoID);
        MapObject mapObject = geoData.getMapObject(recordIndex);
        mapObject.mapInfo_ID = mapInfoID;
        return mapObject;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get tabular record at given mapInfo ID.
     * @param mapInfoID the index of the record(MapInfoID).
     * @return tabular record at given mapInfoID.
     */
    public DataRowValue getDataRowValue(int mapInfoID) throws IOException {
        return tabularData.getRecord(mapInfoID);
    }

    /**
     * reader to read the data from the map file.
     */
    private DataInputStream reader = null;

    /**
     *  record index section.
     */
    private RecordIndex recordIndex = null;
    /**
     * rtree index section.
     */
    private RTreeIndex rtreeIndex = null;
    /**
     * string index section.
     */
    private StringIndex stringIndex = null;
    /**
     * String data section.
     */
    private StringData stringData = null;
    /**
     * geo data section.
     */
    private GeoData geoData = null;

    /**
     * when store latitude/longitude , it store as integer.
     * to convert to an interget ,it muliple by DOUBLE_PRECISION.
     */
    private static final double DOUBLE_PRECISION = 10000000.0;
    /**
     * Rtree index file.
     */
    private RTree tree;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * binary search (English).
     * @param queryValue query string.
     * @return the string recrod ID.
     */
    private int binarySearch(String queryValue) throws IOException {
        int left = 0;
        int right = (int) (stringIndex.size / StringIndex.RECORDSIZE) - 1;
        while (left <= right) {
            int middle = (int) Math.floor((double) ((left + right) / 2));
            {
                stringIndex.getRecord(middle);
                String middleValue = stringData.getRecord(stringIndex.recordOffset);
                stringIndex.getRecord(left);
                String leftValue = stringData.getRecord(stringIndex.recordOffset);
                stringIndex.getRecord(right);
                String rightValue = stringData.getRecord(stringIndex.recordOffset);
                if (leftValue.length() > queryValue.length()) {
                    leftValue = leftValue.substring(0, queryValue.length());
                }
                if (middleValue.length() > queryValue.length()) {
                    middleValue = middleValue.substring(0, queryValue.length());
                }
                if (rightValue.length() > queryValue.length()) {
                    rightValue = rightValue.substring(0, queryValue.length());
                }

                if (queryValue.compareTo(middleValue) == 0) {
                    return middle;
                } else if (queryValue.compareTo(middleValue) > 0) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }

        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * binary search (Chinese Pinyin).
     * @param queryValue query string.
     * @param GB2312File  the GB2312file.
     * @return the string recrod ID.
     */
    private int binarySearch(String queryValue, DataInputStream GB2312File)
            throws IOException {
        int left = 0;
        int right = (int) (stringIndex.size / StringIndex.RECORDSIZE) - 1;
        String queryValuePinYin = GB2312.getPinyinCode(queryValue, GB2312File);
        while (left <= right) {
            int middle = (int) Math.floor((double) ((left + right) / 2));
            {
                stringIndex.getRecord(middle);
                String middleValue =
                        stringData.getRecord(stringIndex.recordOffset);
                String middleValuePinYin =
                        GB2312.getPinyinCode(middleValue, GB2312File);
                stringIndex.getRecord(left);
                String leftValue = stringData.getRecord(stringIndex.recordOffset);
                String leftValuePinYin =
                        GB2312.getPinyinCode(leftValue, GB2312File);
                stringIndex.getRecord(right);
                String rightValue = stringData.getRecord(stringIndex.recordOffset);
                String rightValuePinYin =
                        GB2312.getPinyinCode(rightValue, GB2312File);
                if (leftValuePinYin.length() > queryValuePinYin.length()) {
                    leftValuePinYin =
                            leftValuePinYin.substring(0, queryValuePinYin.length());
                }
                if (middleValuePinYin.length() > queryValuePinYin.length()) {
                    middleValuePinYin = middleValuePinYin.substring(0, queryValuePinYin.length());
                }
                if (rightValuePinYin.length() > queryValuePinYin.length()) {
                    rightValuePinYin = rightValuePinYin.substring(0, queryValuePinYin.length());
                }

                if (queryValuePinYin.compareTo(middleValuePinYin) == 0) {
                    return middle;
                } else if (queryValuePinYin.compareTo(middleValuePinYin) > 0) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }

        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * binary search (Chinese Pinyin).
     * @param queryValue query string.
     * @param GB2312File  the GB2312file.
     * @return the string recrod ID.
     */
    private int binarySearchPinYin(String queryValue, DataInputStream GB2312File)
            throws IOException {
        int left = 0;
        int right = (int) (stringIndex.size / StringIndex.RECORDSIZE) - 1;
        String queryValuePinYin = queryValue;
        while (left <= right) {
            int middle = (int) Math.floor((double) ((left + right) / 2));
            {
                stringIndex.getRecord(middle);
                String middleValue =
                        stringData.getRecord(stringIndex.recordOffset);
                String middleValuePinYin =
                        GB2312.getPinyinCode(middleValue, GB2312File);
                stringIndex.getRecord(left);
                String leftValue = stringData.getRecord(stringIndex.recordOffset);
                String leftValuePinYin =
                        GB2312.getPinyinCode(leftValue, GB2312File);
                stringIndex.getRecord(right);
                String rightValue = stringData.getRecord(stringIndex.recordOffset);
                String rightValuePinYin =
                        GB2312.getPinyinCode(rightValue, GB2312File);
                if (leftValuePinYin.length() > queryValuePinYin.length()) {
                    leftValuePinYin =
                            leftValuePinYin.substring(0, queryValuePinYin.length());
                }
                if (middleValuePinYin.length() > queryValuePinYin.length()) {
                    middleValuePinYin = middleValuePinYin.substring(0,
                            queryValuePinYin.length());
                }
                if (rightValuePinYin.length() > queryValuePinYin.length()) {
                    rightValuePinYin = rightValuePinYin.substring(0,
                            queryValuePinYin.length());
                }

                if (queryValuePinYin.compareTo(middleValuePinYin) == 0) {
                    return middle;
                } else if (queryValuePinYin.compareTo(middleValuePinYin) > 0) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }

        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all tabular record based on search condition.
     * @param findConditions the search condition.
     * @return a hashtable of all matched map objects.the key is the mapInfo ID.
     */
    private Hashtable search(FindConditions findConditions,
            DataInputStream GB2312File) throws IOException {
        Hashtable retTable = new Hashtable();
        Vector allCondition = findConditions.getConditions();
        for (int i = 0; i < allCondition.size(); i++) {
            FindCondition findCondition = (FindCondition) allCondition.elementAt(i);
            int stringID = binarySearch(findCondition.matchString, GB2312File);
            int fieldIndex = findCondition.fieldIndex;
            if (stringID != -1) {
                stringIndex.getRecord(stringID);
                String strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                while (strValue.startsWith(findCondition.matchString)) {
                    int fieldCount = stringData.fieldCount;
                    for (int j = 0; j < fieldCount; j++) {
                        if (stringData.fieldID[j] == fieldIndex) {
                            Integer mapInfoID = new Integer(stringData.mapInfoID[j]);
                            if (!retTable.containsKey(mapInfoID)) {
                                retTable.put(mapInfoID,
                                        strValue);
                            }
                            if (retTable.size() > findConditions.maxMatchRecord) {
                                return retTable;
                            }
                        }
                    }
                    stringIndex.moveNext();
                    strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                }
                stringIndex.getRecord(stringID);
                strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                while (strValue.startsWith(findCondition.matchString)) {
                    int fieldCount = stringData.fieldCount;
                    for (int j = 0; j < fieldCount; j++) {
                        if (stringData.fieldID[j] == fieldIndex) {
                            Integer mapInfoID = new Integer(stringData.mapInfoID[j]);
                            if (!retTable.containsKey(mapInfoID)) {
                                retTable.put(mapInfoID,
                                        strValue);
                            }
                            if (retTable.size() > findConditions.maxMatchRecord) {
                                return retTable;
                            }
                        }
                    }
                    stringIndex.movePrevious();
                    strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                }
            }

        }
        return retTable;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all tabular record based on search condition.
     * @param findConditions the search condition.
     * @return a hashtable of all matched map objects.the key is the mapInfo ID.
     */
    private Hashtable searchPinYin(FindConditions findConditions,
            DataInputStream GB2312File) throws IOException {
        Hashtable retTable = new Hashtable();
        Vector allCondition = findConditions.getConditions();
        for (int i = 0; i < allCondition.size(); i++) {
            FindCondition findCondition = (FindCondition) allCondition.elementAt(i);
            int stringID = binarySearchPinYin(findCondition.matchString, GB2312File);
            int fieldIndex = findCondition.fieldIndex;
            if (stringID != -1) {
                stringIndex.getRecord(stringID);
                String strValue = stringData.getRecord(stringIndex.recordOffset);
                while (strValue.startsWith(findCondition.matchString)) {
                    int fieldCount = stringData.fieldCount;
                    for (int j = 0; j < fieldCount; j++) {
                        if (stringData.fieldID[j] == fieldIndex) {
                            Integer mapInfoID = new Integer(stringData.mapInfoID[j]);
                            if (!retTable.containsKey(mapInfoID)) {
                                retTable.put(mapInfoID,
                                        strValue);
                            }
                            if (retTable.size() > findConditions.maxMatchRecord) {
                                return retTable;
                            }
                        }
                    }
                    stringIndex.moveNext();
                    strValue = stringData.getRecord(stringIndex.recordOffset);
                }
                stringIndex.getRecord(stringID);
                strValue = stringData.getRecord(stringIndex.recordOffset);
                while (strValue.startsWith(findCondition.matchString)) {
                    int fieldCount = stringData.fieldCount;
                    for (int j = 0; j < fieldCount; j++) {
                        if (stringData.fieldID[j] == fieldIndex) {
                            Integer mapInfoID = new Integer(stringData.mapInfoID[j]);
                            if (!retTable.containsKey(mapInfoID)) {
                                retTable.put(mapInfoID,
                                        strValue);
                            }
                            if (retTable.size() > findConditions.maxMatchRecord) {
                                return retTable;
                            }
                        }
                    }
                    stringIndex.movePrevious();
                    strValue = stringData.getRecord(stringIndex.recordOffset);
                }
            }

        }
        return retTable;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all records based on search condition.
     * @param findConditions the search condition.
     * @return a hashtable of all matched record.the key is the mapInfo ID.
     */
    public Hashtable search(FindConditions findConditions) throws IOException {
        Hashtable retTable = new Hashtable();
        Vector allCondition = findConditions.getConditions();
        for (int i = 0; i < allCondition.size(); i++) {
            FindCondition findCondition = (FindCondition) allCondition.elementAt(i);
            int stringID = binarySearch(findCondition.matchString);
            int fieldIndex = findCondition.fieldIndex;
            if (stringID != -1) {
                boolean bDone = false;
                stringIndex.getRecord(stringID);
                String strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                while (strValue.startsWith(findCondition.matchString) && (!bDone)) {
                    int fieldCount = stringData.fieldCount;
                    for (int j = 0; j < fieldCount; j++) {
                        if (stringData.fieldID[j] == fieldIndex) {
                            Integer mapInfoID = new Integer(stringData.mapInfoID[j]);
                            if (!retTable.containsKey(mapInfoID)) {
                                retTable.put(mapInfoID,
                                        strValue);
                            }
                            if (retTable.size() > findConditions.maxMatchRecord) {
                                return retTable;
                            }
                        }
                    }
                    bDone = stringIndex.movePrevious();
                    if (!bDone) {
                        strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                    }
                }
                bDone = false;
                stringIndex.getRecord(stringID);
                strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                while (strValue.startsWith(findCondition.matchString) && (!bDone)) {
                    int fieldCount = stringData.fieldCount;
                    for (int j = 0; j < fieldCount; j++) {
                        if (stringData.fieldID[j] == fieldIndex) {
                            Integer mapInfoID = new Integer(stringData.mapInfoID[j]);
                            if (!retTable.containsKey(mapInfoID)) {
                                retTable.put(mapInfoID,
                                        strValue);
                            }
                            if (retTable.size() > findConditions.maxMatchRecord) {
                                return retTable;
                            }
                        }
                    }
                    bDone = stringIndex.moveNext();
                    if (!bDone) {
                        strValue = stringData.getMapInfoIDAndField(stringIndex.recordOffset);
                    }
                }
            }

        }
        return retTable;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get table field definition.
     * @return table field definition.
     */
    public DataField[] getFields() {
        return header.fields;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get all records based on given rectangle.
     * @param rectGeo the boundary..
     * @return a hashtable of all matched record.the key is the mapInfo ID.
     */
    public Hashtable searchMapObjectsInRect(GeoLatLngBounds rectGeo)
            throws IOException {
        com.mapdigit.gis.vector.rtree.Point pt1, pt2;
        pt1 = new com.mapdigit.gis.vector.rtree.Point(new int[]{
                    (int) (rectGeo.x * DOUBLE_PRECISION),
                    (int) (rectGeo.y * DOUBLE_PRECISION)});
        pt2 = new com.mapdigit.gis.vector.rtree.Point(new int[]{
                    (int) ((rectGeo.x + rectGeo.width) * DOUBLE_PRECISION),
                    (int) ((rectGeo.y + rectGeo.height) * DOUBLE_PRECISION)});
        HyperCube h1 = new HyperCube(pt1, pt2);
        int index = 0;
        int mapinfo_id = 0;
        Hashtable retArrayList = new Hashtable();
        com.mapdigit.gis.vector.rtree.Point p11, p12;
        for (Enumeration e1 = tree.intersection(h1); e1.hasMoreElements();) {

            AbstractNode node = (AbstractNode) (e1.nextElement());
            if (node.isLeaf()) {
                index = 0;
                HyperCube[] data = node.getHyperCubes();
                HyperCube cube;
                for (int cubeIndex = 0; cubeIndex < data.length; cubeIndex++) {
                    cube = data[cubeIndex];
                    if (cube.intersection(h1)) {
                        p11 = cube.getP1();
                        p12 = cube.getP2();
                        mapinfo_id = ((Leaf) node).getDataPointer(index);
                        Integer mapInfo_ID = new Integer(mapinfo_id);
                        GeoLatLngBounds mbr = new GeoLatLngBounds();
                        mbr.x = p11.getFloatCoordinate(0) / DOUBLE_PRECISION;
                        mbr.y = p11.getFloatCoordinate(1) / DOUBLE_PRECISION;
                        mbr.width = ((p12.getFloatCoordinate(0) - p11.getFloatCoordinate(0))) / DOUBLE_PRECISION;
                        mbr.height = ((p12.getFloatCoordinate(1) - p11.getFloatCoordinate(1))) / DOUBLE_PRECISION;
                        if (!retArrayList.contains(mapInfo_ID)) {
                            retArrayList.put(mapInfo_ID, mbr);
                        }

                    }
                    index++;

                }
            }
        }
        return retArrayList;
    }
}
