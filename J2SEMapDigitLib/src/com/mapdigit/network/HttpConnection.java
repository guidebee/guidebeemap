//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.network;

//--------------------------------- IMPORTS ------------------------------------
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This interface defines the necessary methods and constants for an HTTP
 * connection.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
public interface HttpConnection {

    /**
     * HTTP Get method.
     */
    public static String GET = "GET";

    /**
     * HTTP POST method.
     */
    public static String POST = "POST";

    /**
     * 200: The request has succeeded.
     */
    public static int HTTP_OK = HttpURLConnection.HTTP_OK;

    /**
     * 404: The server has not found anything matching the Request-URI.
     * No indication is given of whether the condition is temporary or permanent.
     */
    public static int HTTP_NOT_FOUND = HttpURLConnection.HTTP_NOT_FOUND;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the method for the URL request, one of:  GET, POST, HEAD
     * @param method the HTTP method
     */
    public void setRequestMethod(String method);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the general request property. If a property with the key already
     * exists, overwrite its value with the new value.
     * @param key the keyword by which the request is known (e.g., "accept").
     * @param value  the value associated with it
     */
    public void setRequestProperty(String key, String value);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open and return an output stream for a connection.
     * @return An output stream
     */
    public OutputStream openOutputStream() throws IOException;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the HTTP response status code. It parses responses like:
     * HTTP/1.0 200 OK
     * HTTP/1.0 401 Unauthorized
     * and extracts the ints 200 and 401 respectively. from the response
     * (i.e., the response is not valid HTTP).
     * @return the HTTP Status-Code or -1 if no status code can be discerned.
     */
    public int getResponseCode() throws IOException;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the connection. 
     * @throws IOException
     */
    public void close() throws IOException;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the value of the named field parsed as a number.
     * @param name the name of the header field.
     * @param def the default value.
     * @return the value of the named field, parsed as an integer. The def value
     * is returned if the field is missing or malformed.
     */
    public int getHeaderFieldInt(String name, int def);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Open and return an input stream for a connection
     * @return An input stream
     */
    public InputStream openInputStream() throws IOException;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets a header field key by index.
     * @param i the index of the header field
     * @return the key of the nth header field or null if the array index is
     * out of range.
     */
    public String getHeaderFieldKey(int i);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets a header field value by index.
     * @param i the index of the header field
     * @return the value of the nth header field or null if the array index is
     * out of range. An empty String is returned if the field does not have a value
     */
    public String getHeaderField(int i);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the value of the named header field.
     * @param name of a header field.
     * @return the value of the named header field, or null if there is no
     * such field in the header
     */
    public String getHeaderField(String name);
}
