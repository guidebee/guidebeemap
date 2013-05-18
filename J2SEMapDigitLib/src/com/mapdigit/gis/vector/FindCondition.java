//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Defines a find condition when seach for records.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public class FindCondition {

    /**
     * the colume to be matched.
     */
    public int fieldIndex;
    /**
     * String to be matched.
     */
    public String matchString = "";
    
    /**
     * search for begin with.
     */
    private static final int MATCH_BEGIN = 0;
    /**
     * seach for end with.
     */
    private static final int MATCH_END = 1;
    /**
     * either begin with or end with.
     */
    private int searchDirection;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param fieldIndex the index of columm in the table.
     * @param searchDirection either begin with or end with.
     * @param matchString String to be matched.
     */
    FindCondition(int fieldIndex, String matchString) {
        this.fieldIndex = fieldIndex;
        this.searchDirection = MATCH_BEGIN;
        this.matchString = matchString;
    }
}
