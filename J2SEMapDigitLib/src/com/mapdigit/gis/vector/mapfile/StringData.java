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

import com.mapdigit.util.DataReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * string data section of the map file.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class StringData extends Section {

    public int fieldCount = 0;
    public int[] mapInfoID;
    public int[] fieldID;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public StringData(DataInputStream reader, long offset, long size)
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
     * Get one string value.
     */
    public String getRecord(long offset) throws IOException {
        DataReader.seek(reader, offset);
        String ret = DataReader.readString(reader);
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get one string value.
     */
    public String getMapInfoIDAndField(long offset) throws IOException {
        DataReader.seek(reader, offset);
        String ret = DataReader.readString(reader);
        fieldCount = DataReader.readShort(reader);
        mapInfoID = new int[fieldCount];
        fieldID = new int[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            mapInfoID[i] = DataReader.readInt(reader);
            fieldID[i] = reader.readByte();
        }
        return ret;
    }
}
