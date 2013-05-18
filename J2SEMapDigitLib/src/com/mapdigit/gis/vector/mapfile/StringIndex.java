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
 * string index section of the map file.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class StringIndex extends Section {

    /**
     * the offset of the record.
     */
    public int recordOffset;
    /**
     * the lenght of the record.
     */
    public int recordLength;
    /**
     * the size of the record index record.
     */
    public static final int RECORDSIZE = 6;
    
    /**
     * total record count;
     */
    public int recordCount = 0;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public StringIndex(DataInputStream reader, long offset, long size)
            throws IOException {
        super(reader, offset, size);
        this.recordCount = (int) (size / RECORDSIZE);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get one record (index) of give record ID.
     */
    public void getRecord(int recordID) throws IOException {
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
    public boolean movePrevious() throws IOException {
        boolean ret = false;
        currentIndex--;
        if (currentIndex < 0) {
            ret = true;
        } else {
            readOneRecord();
        }
        return ret;
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
    public boolean moveNext() throws IOException {
        boolean ret = false;
        currentIndex++;
        if (currentIndex >= recordCount) {
            ret = true;
        } else {
            readOneRecord();
        }
        return ret;
    }

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
        recordOffset = DataReader.readInt(reader);
        recordLength = DataReader.readInt(reader);
    }
}
