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

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Defines a row of a database table.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class DataRowValue {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public DataRowValue(String[] fieldValues) {
        this.fieldValues = fieldValues;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the value of the specified column as a String.
     * @param ordinal The zero-based column ordinal.
     * @return The value of the specified column as a String.
     */
    public String getString(int ordinal) {
        if (ordinal >= 0 && ordinal < fieldValues.length) {
            return fieldValues[ordinal];
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the value of the specified column as a integer.
     * @param ordinal The zero-based column ordinal.
     * @return The value of the specified column as a integer.
     */
    public int getInt(int ordinal) {
        try {
            if (ordinal >= 0 && ordinal < fieldValues.length) {
                return Integer.parseInt(fieldValues[ordinal]);
            }
        } catch (Exception e) {
            //ingore the exception.
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the value of the specified column as a short.
     * @param ordinal The zero-based column ordinal.
     * @return The value of the specified column as a short.
     */
    public short getShort(int ordinal) {
        try {
            if (ordinal >= 0 && ordinal < fieldValues.length) {
                return Short.parseShort(fieldValues[ordinal]);
            }
        } catch (Exception e) {
            //ingore the exception.
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the value of the specified column as a double.
     * @param ordinal The zero-based column ordinal.
     * @return The value of the specified column as a double.
     */
    public double getDouble(int ordinal) {
        try {
            if (ordinal >= 0 && ordinal < fieldValues.length) {
                return Double.parseDouble(fieldValues[ordinal]);
            }
        } catch (Exception e) {
            //ingore the exception.
        }

        return 0.0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the value of the specified column as a string(Date).
     * @param ordinal The zero-based column ordinal.
     * @return The value of the specified column as a string.
     */
    public String getDate(int ordinal) {
        try {
            if (ordinal >= 0 && ordinal < fieldValues.length) {
                return fieldValues[ordinal];
            }
        } catch (Exception e) {
            //ingore the exception.
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the value of the specified column as a boolean.
     * @param ordinal The zero-based column ordinal.
     * @return The value of the specified column as a boolean.
     */
    public boolean getBoolean(int ordinal) {
        try {
            if (ordinal >= 0 && ordinal < fieldValues.length) {
                if (Byte.parseByte(fieldValues[ordinal]) != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            //ingore the exception.
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert to string.
     */
    public String toString() {
        String retStr = "";
        for (int i = 0; i < fieldValues.length - 1; i++) {
            if (isNumber(fieldValues[i])) {
                retStr += fieldValues[i] + ",";
            } else {
                retStr += "\"" + fieldValues[i] + "\"" + ",";
            }
        }
        if (isNumber(fieldValues[fieldValues.length - 1])) {
            retStr += fieldValues[fieldValues.length - 1];
        } else {
            retStr += "\"" + fieldValues[fieldValues.length - 1] + "\"";
        }
        return retStr;
    }


    /**
     * internal store all field values in string format.
     */
    private String[] fieldValues;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check if the string can be converted to a number.
     */
    private boolean isNumber(String strValue) {
        try {
            double value = Double.parseDouble(strValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
