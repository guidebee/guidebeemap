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

import com.mapdigit.gis.vector.DataField;
import com.mapdigit.gis.vector.DataRowValue;
import com.mapdigit.util.DataReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * tabular data section of the map file.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public final class TabularData extends Section {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public TabularData(DataInputStream reader, long offset, long size,
            DataField[] fields, StringData stringData, StringIndex stringIndex)
            throws IOException {
        super(reader, offset, size);
        this.fields = fields;
        this.stringData = stringData;
        this.stringIndex = stringIndex;
        int numberOfField = fields.length;
        recordSize = 0;
        for (int i = 0; i < numberOfField; i++) {
            switch (fields[i].getType()) {
                case DataField.TYPE_CHAR://char
                    recordSize += 4;
                    break;
                case DataField.TYPE_INTEGER://int
                    recordSize += 4;
                    break;
                case DataField.TYPE_SMALLINT://shot
                    recordSize += 2;
                    break;
                case DataField.TYPE_FLOAT://float
                    recordSize += 8;
                    break;
                case DataField.TYPE_DECIMAL://float
                    recordSize += 8;
                    break;
                case DataField.TYPE_DATE://date
                    recordSize += 4;
                    break;
                case DataField.TYPE_LOGICAL://bool
                    recordSize += 1;
                    break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get one record (index) of give mapInfo ID.
     */
    public DataRowValue getRecord(int mapInfoID) throws IOException {
        int recordID = mapInfoID - 1;
        if (mapInfoID < 1) {
            throw new IOException("MapInfo ID starts from 1");
        }
        DataReader.seek(reader, offset + recordID * recordSize);
        String[] fieldValues = new String[fields.length];

        int intValue;
        int shortValue;
        int stringID;
        byte boolValue;
        double doubleValue;
        for (int i = 0; i < fieldValues.length; i++) {
            switch (fields[i].getType()) {
                case DataField.TYPE_CHAR://char
                case DataField.TYPE_DATE://date
                    stringID = DataReader.readInt(reader);
                    fieldValues[i] = Integer.toString(stringID);
                    break;
                case DataField.TYPE_INTEGER://int
                    intValue = DataReader.readInt(reader);
                    fieldValues[i] = Integer.toString(intValue);
                    break;
                case DataField.TYPE_SMALLINT://short
                    shortValue = DataReader.readShort(reader);
                    fieldValues[i] = Integer.toString(shortValue);
                    break;
                case DataField.TYPE_DECIMAL://short
                case DataField.TYPE_FLOAT://float
                    doubleValue = DataReader.readDouble(reader);
                    fieldValues[i] = Double.toString(doubleValue);
                    break;
                case DataField.TYPE_LOGICAL://bool
                    boolValue = reader.readByte();
                    fieldValues[i] = Integer.toString(boolValue);
                    break;
            }
        }

        //read string data.
        for (int i = 0; i < fieldValues.length; i++) {
            switch (fields[i].getType()) {
                case DataField.TYPE_CHAR://char
                case DataField.TYPE_DATE://date
                    stringID = Integer.parseInt(fieldValues[i]);
                    if (stringID != -1) {
                        stringIndex.getRecord(stringID);
                        fieldValues[i] = stringData.getRecord(stringIndex.recordOffset);
                    } else {
                        fieldValues[i] = "";
                    }
                    break;
            }
        }
        DataRowValue ret = new DataRowValue(fieldValues);
        return ret;
    }

    /**
     * table field defintion.
     */
    private DataField[] fields;
    /**
     * the lenght of one record.
     */
    private int recordSize;
    /**
     * String data section object.
     */
    private StringData stringData;
    /**
     * String index section object.
     */
    private StringIndex stringIndex;
}
