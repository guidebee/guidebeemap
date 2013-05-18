//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.ajax;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.ajax.json.IJSONPath;
import com.mapdigit.ajax.json.JSONArray;
import com.mapdigit.ajax.json.JSONException;
import com.mapdigit.ajax.json.JSONObject;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Http result object (JSONObject or JSONArray).
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public class Result implements IJSONPath{
    
    /**
     * text/javascript content type.
     */
    public static final String JS_CONTENT_TYPE = "text/javascript";
    
    /**
     * application/json content type.
     */
    public static final String JSON_CONTENT_TYPE = "application/json";
    
    /**
     * text/plain content type.
     */
    public static final String PLAIN_TEXT_CONTENT_TYPE = "text/plain";
    
    /**
     * text/xml content type.
     */
    public static final String TEXT_XML_CONTENT_TYPE = "text/xml";

    /**
     * text/xml content type.
     */
    public static final String TEXT_HTML_CONTENT_TYPE = "text/html";
    
    /**
     * application/xml content type.
     */
    public static final String APPLICATION_XML_CONTENT_TYPE = "application/xml";

    /**
     * application/kml content type.
     */
    public static final String APPLICATION_KML_CONTENT_TYPE
            = "application/vnd.google-earth.kml+xml";
   
     
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the hash code.
     * @return the hash code of this object.
     */
    public int hashCode() {
        return isArray ? array.hashCode() : json.hashCode();
    }
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Indicates whether some other object is "equal to" this one. 
     * @param other the reference object with which to compare. 
     * @return true if this object is the same as the obj argument;
     * false otherwise.
     */
    public boolean equals(final Object other) {
        if (other instanceof Result) {
            return isArray ? array.equals(other) : json.equals(other);
        }
        return false;
    }
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    public String toString() {
        try {
            return isArray ? ((JSONArray) array).toString(2) : 
                ((JSONObject) json).toString(2);
        } catch (Exception ingnore) {
            return json.toString();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public boolean getAsBoolean(final String path) throws JSONException {
        return isArray? array.getAsBoolean(path): json.getAsBoolean(path);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public int getAsInteger(final String path) throws JSONException {
        return isArray? array.getAsInteger(path): json.getAsInteger(path);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public long getAsLong(final String path) throws JSONException {
        return isArray? array.getAsLong(path): json.getAsLong(path);
    }
    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public double getAsDouble(final String path) throws JSONException {
         return isArray? array.getAsDouble(path): json.getAsDouble(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public String getAsString(final String path) throws JSONException {
         return isArray? array.getAsString(path): json.getAsString(path);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public int getSizeOfArray(final String path) throws JSONException {
       return isArray? array.getSizeOfArray(path): json.getSizeOfArray(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public String[] getAsStringArray(final String path) throws JSONException {
        return isArray? array.getAsStringArray(path): json.getAsStringArray(path);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public int[] getAsIntegerArray(final String path) throws JSONException {
         return isArray? array.getAsIntegerArray(path)
                 : json.getAsIntegerArray(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public double[] getAsDoubleArray(final String path) throws JSONException {
         return isArray? array.getAsDoubleArray(path)
                 : json.getAsDoubleArray(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public JSONArray getAsArray(final String path) throws JSONException {
         return isArray? array.getAsArray(path)
                 : json.getAsArray(path);
    }
    
    private final JSONObject json;
    private final JSONArray array;
    private final boolean isArray;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * construct a Result from input string.
     * @param content
     * @param contentType
     * @return a result object.
     * @throws JSONException
     */
    static Result fromContent(final String content,
        final String contentType) throws JSONException {
        
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        
        if (JS_CONTENT_TYPE.equals(contentType) ||
            TEXT_HTML_CONTENT_TYPE.equals(contentType) ||
            JSON_CONTENT_TYPE.equals(contentType) ||
            // some sites return JSON with the plain text content type
            PLAIN_TEXT_CONTENT_TYPE.equals(contentType)) {
            try {
                boolean startWithSquare=content.startsWith("[");
                return startWithSquare ?
                    new Result(new JSONArray(content)) :
                    new Result(new JSONObject(content));
            } catch (Exception ex) {
                throw new JSONException(ex.getMessage());
            }
        }

         else if (TEXT_XML_CONTENT_TYPE.equals(contentType) ||
             APPLICATION_XML_CONTENT_TYPE.equals(contentType) ||
             APPLICATION_KML_CONTENT_TYPE.equals(contentType) ||
            // default to XML if content type is not specified
             contentType == null) {
            try {
                 return new Result(JSONObject.fromXMLString(content.trim()));
             } catch (Exception ex) {
                 throw new JSONException(ex.getMessage());
             }
         }
       throw new JSONException("Unsupported content-type: " + contentType);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor for a JSON object.
     * @param obj
     */
    private Result(final JSONObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException("json object cannot be null");
        }
        isArray = false;
        this.json = obj;
        this.array = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor from a JSON array.
     * @param obj
     */
    private Result(final JSONArray obj) {
        if (obj == null) {
            throw new IllegalArgumentException("json object cannot be null");
        }
        isArray = true;
        this.json = null;
        this.array = obj;
    }
    
}
