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
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Defines a find condition collection when seach for records.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public class FindConditions {

    /**
     * condition is OR operation.
     */
    public static final int LOGICAL_OR = 0;
    /**
     * condition is AND operation.
     */
    public static final int LOGICAL_AND = 1;
    /**
     * the max matching records, default 100;
     */
    public int maxMatchRecord = 100;
    /**
     * the table field defintion.
     */
    public DataField[] fields = null;
    /**
     * the total conditions.
     */
    private Vector findConditions = null;
    /**
     * the logical operation ,either OR or AND.
     */
    private int logicalOperation = LOGICAL_OR;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public FindConditions() {
        findConditions = new Vector();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear all conditaions.
     */
    public void clearCondition() {
        findConditions.removeAllElements();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add one condition.
     * @param fieldIndex the index of column(field) in the table.
     * @param matchString sting to be matched (start with).
     */
    public void addCondition(int fieldIndex, String matchString) {
        FindCondition condition = new FindCondition(fieldIndex, matchString);
        findConditions.addElement(condition);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Return the condition list.
     * @return the condition list.
     */
    public Vector getConditions() {
        return findConditions;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param fieldName the name of columm in the table. If no match, the first
     *  column is selected.
     * @param matchString String to be matched.
     */
    public void addCondition(String fieldName, String matchString) {
        int fieldIndex = 0;
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().equalsIgnoreCase(fieldName)) {
                    fieldIndex = i;
                    break;
                }
            }
        }
        FindCondition condition = new FindCondition(fieldIndex, matchString);
        findConditions.addElement(condition);
    }
}
