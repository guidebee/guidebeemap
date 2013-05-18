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
package com.mapdigit.ajax;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Arg defines HTTP header/value pair.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public final class Arg {

    // some commonly used http header names
    /**
     * Http header authorization.
     */
    public static final String AUTHORIZATION = "authorization";

    /**
     * Http header content-length.
     */
    public static final String CONTENT_LENGTH = "content-length";
    
    /**
     * Http header content-type.
     */
    public static final String CONTENT_TYPE = "content-type";
    
    /**
     * Http header content-disposition.
     */
    public static final String CONTENT_DISPOSITION = "content-disposition";
    
    /**
     * Http header content-transfer-encoding.
     */
    public static final String CONTENT_TRANSFER_ENCODING
            = "content-transfer-encoding";

    /**
     * Http header last-modified.
     */
    public static final String LAST_MODIFIED = "last-modified";
    
    /**
     * Http header if-modified-since.
     */
    public static final String IF_MODIFIED_SINCE = "if-modified-since";

    /**
     * Http header etag.
     */
    public static final String ETAG = "etag";
    
    /**
     * Http header if-none-match.
     */
    public static final String IF_NONE_MATCH = "if-none-match";
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.  value is allowed to be null
     * @param k http header name.
     * @param v http header value.
     */
    public Arg(final String k, final String v) {
        if (k == null || k.length()==0) {
            throw new IllegalArgumentException("invalid key");
        }
        key = k;
        value = v;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the http header name.
     * @return the http header name.
     */
    public String getKey() {
        return key;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the http header value.
     * @return the http header value.
     */
    public String getValue() {
        return value;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a hash code value for the object. 
     * @return the hash code.
     */
    public int hashCode() {
        return value == null ? key.hashCode()
                : key.hashCode() ^ value.hashCode();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Indicates whether some other object is "equal to" this one. 
     * @param other the reference object with which to compare. 
     * @return true if this object is the same as the obj argument; false
     * otherwise.
     */
    public boolean equals(final Object other) {
        if (other instanceof Arg) {
            final Arg oa = ((Arg) other);
            return value == null ? key.equals(oa.key) :
                key.equals(oa.key) && value.equals(oa.value);
        }
        return false;
    }

    /**
     * the http header key.
     */
    private final String key;

    /**
     * the http header value.
     */
    private final String value;

}
