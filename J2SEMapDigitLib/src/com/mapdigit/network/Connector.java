//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.network;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class is factory for creating new Connection objects. 
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
public class Connector {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param name The URL for the connection.
     * @return A new Connection object.
     * @throws IOException
     */
    public static HttpConnection open(String name) throws IOException {
        return new HttpConnectionImpl(new URL(name));
    }
}

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * internal class implements HttpConnection interface..
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
class HttpConnectionImpl implements HttpConnection {

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param url url of the HTTP connection.
     */
    public HttpConnectionImpl(URL url)  throws IOException{
        httpURLConnection=(HttpURLConnection)url.openConnection();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setRequestMethod(String method) {
        try {
            httpURLConnection.setRequestMethod(method);
        } catch (ProtocolException ex) {
            Log.p("setRequestMethod:ProtocolException");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void setRequestProperty(String key, String value) {
        httpURLConnection.setRequestProperty(key, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public OutputStream openOutputStream() throws IOException {
        return httpURLConnection.getOutputStream();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public int getResponseCode() throws IOException {
        return httpURLConnection.getResponseCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void close() throws IOException {
        httpURLConnection.disconnect();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public int getHeaderFieldInt(String name, int def) {
        return httpURLConnection.getHeaderFieldInt(name, def);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public InputStream openInputStream() throws IOException {
        return httpURLConnection.getInputStream();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public String getHeaderFieldKey(int i) {
        return httpURLConnection.getHeaderFieldKey(i);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public String getHeaderField(int i) {
        return httpURLConnection.getHeaderField(i);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public String getHeaderField(String name) {
        return httpURLConnection.getHeaderField(name);
    }

    /**
     * intenral uses HTTPURLConnection
     */
    private final HttpURLConnection httpURLConnection;
    
}
