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
 * Defines a filed of a database table.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class DataField {

    /**
     * Data type is char(string).
     */
    public static final byte TYPE_CHAR = 1;
    /**
     * Data type is integer(4 bytes).
     */
    public static final byte TYPE_INTEGER = 2;
    /**
     * Data type is short (2 bytes).
     */
    public static final byte TYPE_SMALLINT = 3;
    /**
     * Data type is double.
     */
    public static final byte TYPE_DECIMAL = 4;
    /**
     * Data type is float.
     */
    public static final byte TYPE_FLOAT = 5;
    /**
     * Data type is date.
     */
    public static final byte TYPE_DATE = 6;
    /**
     * Data type is boolean.
     */
    public static final byte TYPE_LOGICAL = 7;
    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param name the name of the field.
     * @param type the type of the filed.
     * @param width the width of the field.
     * @param precision the precision of the field.
     */
    public DataField(String name, byte type, int width, short precision) {
        this.fieldName = name;
        this.fieldType = type;
        this.fieldWidth = width;
        this.fieldPrecision = precision;
        if (fieldType == 0) {
            fieldType = TYPE_CHAR;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the name of the field.
     * @return the name of the field.
     */
    public String getName() {
        return fieldName;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the type of the field.
     * @return the type of the field.
     */
    public byte getType() {
        return fieldType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the width of the field.
     * @return the width of the field.
     */
    public int getWidth() {
        return fieldWidth;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the precision of the field.
     * @return the precision of the field.
     */
    public short getPrecision() {
        return fieldPrecision;
    }

    /**
     * the name of the field.
     */
    private String fieldName;
    /**
     * the type of the field.
     */
    private byte fieldType;
    /**
     * the width of the fields.
     */
    private int fieldWidth;
    /**
     * the precision of the field.
     */
    private short fieldPrecision = 0;
}
