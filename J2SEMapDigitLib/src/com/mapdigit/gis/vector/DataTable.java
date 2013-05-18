//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
import java.io.IOException;

import com.mapdigit.gis.vector.mapfile.TabularData;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Define one database table.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class DataTable {

    /**
     * Data table definition.
     */
    public DataField[] fields;
    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    DataTable(TabularData tabularData, DataField[] fields, int recordCount) {
        this.tabularData = tabularData;
        this.fields = fields;
        this.recordCount = recordCount;
        currentIndex = 1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get one record (index) of give mapInfo ID.
     */
    public DataRowValue getRecord(int mapInfoID) throws IOException {
        currentIndex = mapInfoID;
        return tabularData.getRecord(mapInfoID);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the total record number.
     * @return the total record number.
     */
    public int getRecordCount() {
        return recordCount;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get next record (index).
     */
    public DataRowValue moveFirst() throws IOException {
        currentIndex = 1;
        return getRecord(currentIndex);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get next record (index).
     */
    public DataRowValue moveLast() throws IOException {
        currentIndex = recordCount;
        return getRecord(currentIndex);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get next record (index).
     */
    public DataRowValue movePrevious() throws IOException {
        currentIndex--;
        if (currentIndex == 0) {
            throw new IOException("Passed the first record!");
        }
        return readOneRecord();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get previous record (index).
     */
    public DataRowValue moveNext() throws IOException {
        currentIndex++;
        if (currentIndex > recordCount) {
            throw new IOException("Passed the last the record!");
        }
        return readOneRecord();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the column index of given column name.
     * @param fieldName the name of the column.
     * @return the index of the column(field) or -1 if not found.
     */
    public int getFieldIndex(String fieldName) {
        int ret = -1;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equalsIgnoreCase(fieldName)) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    /**
     * current index id.
     */
    private int currentIndex = 0;
    /**
     * Tabular data
     */
    private TabularData tabularData;
    /**
     * total record Count;
     */
    private int recordCount = 0;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * read current record.
     */
    private DataRowValue readOneRecord() throws IOException {
        return getRecord(currentIndex);
    }
}
