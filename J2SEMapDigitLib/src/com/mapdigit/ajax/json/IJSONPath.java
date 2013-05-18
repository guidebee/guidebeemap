//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.ajax.json;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * JSONPath defines a simple expression language which works a lot like a very 
 * small subset of XPath. - the expression syntax uses the dot character 
 * for sub-elements and square brackets for arrays. Some sample expressions are,
 * for example - "photos.photo[1].title", "[0].location", "[1].status.text", etc 
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd. & JSON.org
 */
public interface IJSONPath {

    /**
     * Dot character used as the separator.
     */
    public static final char SEPARATOR = '.';
    
    /**
     * Array start character.
     */
    public static final char ARRAY_START = '[';
    
    /**
     * Array end character.
     */
    public static final char ARRAY_END = ']';
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the boolean value based on the path.
     * @param path the path string.
     * @return the boolean values.
     * @throws JSONException if the path is invalid or the value cannot be 
     * casted to boolean.
     */
    public boolean getAsBoolean(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the integer value based on the path.
     * @param path the path string.
     * @return the integer values.
     * @throws JSONException if the path is invalid or the value cannot be 
     * casted to integer.
     */
    public int getAsInteger(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the long value based on the path.
     * @param path the path string.
     * @return the long values.
     * @throws JSONException if the path is invalid or the value cannot be 
     * casted to long integer.
     */
    public long getAsLong(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the double value based on the path.
     * @param path the path string.
     * @return the double values.
     * @throws JSONException if the path is invalid or the value cannot be 
     * casted to double.
     */
    public double getAsDouble(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the String value based on the path.
     * @param path the path string.
     * @return the string values.
     * @throws JSONException if the path is invalid or the value cannot be 
     * casted to string.
     */
    public String getAsString(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the size of the array.
     * @param path the path string.
     * @return the size of a given arrary.
     * @throws JSONException if the path is invalid or the value cannot be 
     * casted to array.
     */
    public int getSizeOfArray(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the a string array.
     * @param path the path string.
     * @return a string array.
     * @throws JSONException if the path is invalid .
     */
    public String[] getAsStringArray(String path) throws JSONException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the a integer array.
     * @param path the path string.
     * @return a integer array.
     * @throws JSONException if the path is invalid .
     */
    public int[] getAsIntegerArray(String path) throws JSONException;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the a double array.
     * @param path the path string.
     * @return a double array.
     * @throws JSONException if the path is invalid .
     */
    public double[] getAsDoubleArray(String path) throws JSONException;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the a Json array.
     * @param path the path string.
     * @return a json array.
     * @throws JSONException if the path is invalid .
     */
    public JSONArray getAsArray(String path) throws JSONException;
    
}
