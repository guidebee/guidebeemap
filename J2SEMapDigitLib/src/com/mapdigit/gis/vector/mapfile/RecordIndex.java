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

import com.mapdigit.util.DataReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Record index section of the map file.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class RecordIndex extends Section {

    /**
     * The type of the map object.
     */
    public byte mapObjectType;
    /**
     * the offset of the record.
     */
    public int recordOffset;
    /**
     * the lenght of the record.
     */
    public int recordLength;
    /**
     *  the minX of the MBR.
     */
    public int minX;
    /**
     *  the minY of the MBR.
     */
    public int minY;
    /**
     *  the maxX of the MBR.
     */
    public int maxX;
    /**
     *  the maxY of the MBR.
     */
    public int maxY;
    /**
     * Map object param1 (depends on map object type).
     */
    public int param1;
    /**
     * Map object param2 (depends on map object type).
     */
    public int param2;
    /**
     * Map object param3 (depends on map object type).
     */
    public int param3;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public RecordIndex(DataInputStream reader, long offset, long size)
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
     * Get one record (index) of give mapInfo ID.
     */
    public void getRecord(int mapInfoID) throws IOException {
        int recordID = mapInfoID - 1;
        if (mapInfoID < 1) {
            throw new IOException("MapInfo ID starts from 1");
        }
        currentIndex = recordID;
        readOneRecord();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get next record (index).
     */
    public void movePrevious() throws IOException {
        currentIndex--;
        readOneRecord();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get previous record (index).
     */
    public void moveNext() throws IOException {
        currentIndex++;
        readOneRecord();
    }

    /**
     * the size of the record index record.
     */
    private static final int RECORDSIZE = 37;
    /**
     * current index id.
     */
    private int currentIndex = 0;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * read current record.
     */
    private void readOneRecord() throws IOException {
        DataReader.seek(reader, offset + currentIndex * RECORDSIZE);
        mapObjectType = reader.readByte();
        recordOffset = DataReader.readInt(reader);
        recordLength = DataReader.readInt(reader);
        minX = DataReader.readInt(reader);
        minY = DataReader.readInt(reader);
        maxX = DataReader.readInt(reader);
        maxY = DataReader.readInt(reader);
        param1 = DataReader.readInt(reader);
        param2 = DataReader.readInt(reader);
        param3 = DataReader.readInt(reader);
    }
}
