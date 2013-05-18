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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import com.mapdigit.network.Connector;
import com.mapdigit.network.HttpConnection;
import com.mapdigit.rms.RecordStore;
import com.mapdigit.rms.RecordStoreException;
import com.mapdigit.rms.RecordStoreNotOpenException;

import com.mapdigit.ajax.json.JSONException;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * With Requst object, application can issue a asynchronous http requst to a 
 * server and Requst handles the message in a seperate thread.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public final class Request implements Runnable {

    /**
     * UTF-8 encoding. (Charset value)
     */
    public static final String UTF8_CHARSET = "utf-8";

    /**
     * ISO-8859-1 encoding.(Charset value)
     */
    public static final String ISO8859_CHARSET = "iso-8859-1";

    /**
     * GB2312 encoding.(Charset value)
     */
    public static final String GB2312_CHARSET = "gb2312";
    
    /**
     * total bytes downloaded
     */
    public static long totaldownloadedBytes=0;

    /**
     * if this set to true, which means the device has low memory
     * will use record store try to minimize the memeory usage.
     */
    public static boolean lowMemory=false;

    /**
     * record store name
     */
    private static final String REQUEST_RECORDSTORE_NAME = "Guidebee_RequestData";
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Issue a synchronous GET requst.
     * @param url Http request url.
     * @param inputArgs  argument of for the url.
     * @param httpArgs extra http header.
     * @param listener RequestLister used to handle the sync http response.
     * @return the http response object.
     * @throws IOException any IOException.
     */
    public static Response get(final String url,
            final Arg[] inputArgs,
            final Arg[] httpArgs,
            final IRequestListener listener)
            throws IOException {
        return sync(HttpConnection.GET, url, inputArgs, httpArgs,
                listener, null);
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Issue a asynchronous GET requst.
     * @param url Http request url.
     * @param inputArgs  argument of for the url.
     * @param httpArgs extra http header.
     * @param listener RequestLister used to handle the async http response.
     * @param context message context ,wiil pass as the same in done().
     */
    public static void get(
            final String url,
            final Arg[] inputArgs,
            final Arg[] httpArgs,
            final IRequestListener listener,
            final Object context) {

        async(HttpConnection.GET, url, inputArgs, httpArgs, listener, null,
                context);
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Issue a synchronous POST requst.
     * @param url Http request url.
     * @param inputArgs  argument of for the url.
     * @param httpArgs extra http header.
     * @param listener RequestLister used to handle the sync http response.
     * @param multiPart message body.
     * @return the http response object.
     * @throws IOException any IOException
     */
    public static Response post(final String url,
            final Arg[] inputArgs,
            final Arg[] httpArgs,
            final IRequestListener listener,
            final PostData multiPart)
            throws IOException {

        return sync(HttpConnection.POST, url, inputArgs, httpArgs, listener,
                multiPart);
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Issue an asynchronous POST requst.
     * @param url Http request url.
     * @param inputArgs  argument of for the url.
     * @param httpArgs extra http header.
     * @param listener RequestLister used to handle the async http response.
     * @param multiPart message body.
     * @param context message context ,wiil pass as the same in done().
     */
    public static void post(
            final String url,
            final Arg[] inputArgs,
            final Arg[] httpArgs,
            final IRequestListener listener,
            final PostData multiPart,
            final Object context) {

        async(HttpConnection.POST, url, inputArgs, httpArgs, listener,
                multiPart, context);
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Cancel this http requst.
     */
    public void cancel() {
        interrupted = true;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (thread != null) {
            thread.interrupt();
        }
    }
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  Start a thread to process this http request/response, application shall
     *  not call this function directly.
     */
    public void run() {
        final Response response = new Response();
        try {
            doHTTP(response);
        } catch (Exception ex) {
            response.ex = ex;
        } catch(OutOfMemoryError e){
            System.gc();System.gc();
            response.ex = e;
        }
        finally {
            if (listener != null) {
                try {
                    listener.done(context, response);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    private static final int BUFFER_SIZE = 1024;
    private Object context = null;
    private String url = null;
    private String method = null;
    private Arg[] httpArgs = null;
    private Arg[] inputArgs = null;
    private PostData multiPart = null;
    private IRequestListener listener = null;
    private Thread thread = null;
    private volatile boolean interrupted = false;
    private int totalToSend = 0;
    private int totalToReceive = 0;
    private int sent = 0;
    private int received = 0;

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * make a sync request.
     * @param method
     * @param url
     * @param inputArgs
     * @param httpArgs
     * @param listener
     * @param multiPart
     * @return
     * @throws IOException
     */
    private static Response sync(
            final String method,
            final String url,
            final Arg[] inputArgs,
            final Arg[] httpArgs,
            final IRequestListener listener,
            final PostData multiPart)
            throws IOException {

        final Request request = new Request();
        request.method = method;
        request.url = url;
        request.httpArgs = httpArgs;
        request.inputArgs = inputArgs;
        request.multiPart = multiPart;
        request.listener = listener;

        final Response response = new Response();
        request.doHTTP(response);
        return response;
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * make async http request.
     * @param method
     * @param url
     * @param inputArgs
     * @param httpArgs
     * @param listener
     * @param multiPart
     * @param context
     */
    private static void async(
            final String method,
            final String url,
            final Arg[] inputArgs,
            final Arg[] httpArgs,
            final IRequestListener listener,
            final PostData multiPart,
            final Object context) {

        final Request request = new Request();
        request.method = method;
        request.context = context;
        request.listener = listener;
        request.url = url;
        request.httpArgs = httpArgs;
        request.inputArgs = inputArgs;
        request.multiPart = multiPart;

        // TODO: implement more sophisticated pooling, queuing and scheduling
        //strategies
        request.thread = new Thread(request,"Request");
        request.thread.start();
    }

    private Request() {
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * data may be large, send in chunks while reporting progress and checking
     *for interruption
     * @param os
     * @param data
     * @throws IOException
     */
    private void write(final OutputStream os, final byte[] data)
            throws IOException {

        if (interrupted) {
            return;
        }

        // optimization if a small amount of data is being sent
        if (data.length <= BUFFER_SIZE) {
            os.write(data);
            sent += data.length;
            if (listener != null) {
                try {
                    listener.writeProgress(context, sent, totalToSend);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        } else {
            int offset = 0;
            int length = 0;
            do {
                length = Math.min(BUFFER_SIZE, data.length - offset);
                if (length > 0) {
                    os.write(data, offset, length);
                    offset += length;
                    sent += length;
                    if (listener != null) {
                        try {
                            listener.writeProgress(context, sent, totalToSend);
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                }
            } while (!interrupted && length > 0);
        }
    }


    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * do an http request.
     * @param response
     * @throws IOException
     */
    private void doHTTP(final Response response) throws IOException {
        final StringBuffer args = new StringBuffer();
        if (inputArgs != null) {
            if (inputArgs.length > 0) {
                for (int i = 0; i < inputArgs.length; i++) {
                    if (inputArgs[i] != null) {
                        args.append(encode(inputArgs[i].getKey()));
                        args.append("=");
                        args.append(encode(inputArgs[i].getValue()));
                        if (i + 1 < inputArgs.length &&
                                inputArgs[i + 1] != null) {
                            args.append("&");
                        }
                    }
                }
            }
        }

        final StringBuffer location = new StringBuffer(url);
        if (HttpConnection.GET.equals(method) && args.length() > 0) {
            location.append("?");
            location.append(args.toString());
        }

        HttpConnection conn = null;
        try {
            conn = (HttpConnection) Connector.open(location.toString());
            conn.setRequestMethod(method);
            if (httpArgs != null) {
                for (int i = 0; i < httpArgs.length; i++) {
                    if (httpArgs[i] != null) {
                        final String value = httpArgs[i].getValue();
                        if (value != null) {
                            conn.setRequestProperty(httpArgs[i].getKey(), value);
                        }
                    }
                }
            }

            if (interrupted) {
                return;
            }

            if (HttpConnection.POST.equals(method)) {
                OutputStream os = null;
                try {
                    os = conn.openOutputStream();
                    writePostData(args, os);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException ignore) {
                        }
                    }
                }
            }

            if (interrupted) {
                return;
            }

            copyResponseHeaders(conn, response);

            response.responseCode = conn.getResponseCode();
            if (response.responseCode != HttpConnection.HTTP_OK) {
                // TODO: handle redirects
                return;
            }

            if (interrupted) {
                return;
            }
            
            processContentType(conn, response);
            readResponse(conn, response);
        }catch(OutOfMemoryError ome){
            System.gc();System.gc();
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * write post data.
     * @param args
     * @param os
     * @throws IOException
     */
    private void writePostData(final StringBuffer args, final OutputStream os)
            throws IOException {
        if (multiPart != null) {
            if (multiPart.isMultiPart()) {
                final byte[] multipartBoundaryBits
                        = multiPart.getBoundary().getBytes();
                final byte[] newline = "\r\n".getBytes();
                final byte[] dashdash = "--".getBytes();

                // estimate totalBytesToSend
                final Part[] parts = multiPart.getParts();
                for (int i = 0; i < parts.length; i++) {
                    final Arg[] headers = parts[i].getHeaders();
                    for (int j = 0; j < headers.length; j++) {
                        totalToSend += headers[j].getKey().getBytes().length;
                        totalToSend += headers[j].getValue().getBytes().length;
                        totalToSend += multipartBoundaryBits.length
                                + dashdash.length + 3 * newline.length;
                    }
                    totalToSend += parts[i].getData().length;
                }
                // closing boundary marker
                totalToSend += multipartBoundaryBits.length
                        + 2 * dashdash.length + 2 * newline.length;

                for (int i = 0; i < parts.length && !interrupted; i++) {
                    write(os, newline);
                    write(os, dashdash);
                    write(os, multipartBoundaryBits);
                    write(os, newline);

                    boolean wroteAtleastOneHeader = false;
                    final Arg[] headers = parts[i].getHeaders();
                    for (int j = 0; j < headers.length; j++) {
                        write(os, (headers[j].getKey() + ": "
                                + headers[j].getValue()).getBytes());
                        write(os, newline);
                        wroteAtleastOneHeader = true;
                    }
                    if (wroteAtleastOneHeader) {
                        write(os, newline);
                    }

                    write(os, parts[i].getData());
                }

                // closing boundary marker
                write(os, newline);
                write(os, dashdash);
                write(os, multipartBoundaryBits);
                write(os, dashdash);
                write(os, newline);
            } else {
                final Part part = multiPart.getParts()[0];
                totalToSend += part.getData().length;
                write(os, part.getData());
            }
       } else if (inputArgs != null) {
            final byte[] argBytes = args.toString().getBytes();
            totalToSend = argBytes.length;
            write(os, argBytes);
        } else {
            throw new IOException("No data to POST -"
                    + " either input args or multipart must be non-null");
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * read response from the record store.
     * @param conn
     * @param response
     * @throws IOException
     */
    private void readResposeViaRecordStore(final HttpConnection conn,
            final Response response) throws IOException {
        totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
        
        totalToReceive=0;

        final byte[] cbuf = new byte[BUFFER_SIZE];
        Vector recordId=new Vector();
        Vector recordSize=new Vector();
        InputStream is = null;
        RecordStore requestDataRecordStore = null;
        try {
            
             try {
                RecordStore.deleteRecordStore(REQUEST_RECORDSTORE_NAME);
            } catch (RecordStoreException ingore) {
                ingore.printStackTrace();
            }
            is = conn.openInputStream();
            int nread = 0;
            requestDataRecordStore
                    = RecordStore.openRecordStore(REQUEST_RECORDSTORE_NAME, true);
            while ((nread = is.read(cbuf)) > 0 && !interrupted) {
                recordId.addElement(new Integer(requestDataRecordStore
                        .addRecord(cbuf, 0, nread)));
                recordSize.addElement(new Integer(nread));
                received += nread;
                totalToReceive+= nread;
                if (listener != null) {
                    try {
                        listener.readProgress(context, received, totalToReceive);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        } catch (RecordStoreException e) {
                e.printStackTrace();
         }
        finally {
            if (is != null) {
                is.close();
            }
            totaldownloadedBytes += totalToReceive;

        }

        if (interrupted) {
            return;
        }

        System.gc();System.gc();
        long freeMemory=Runtime.getRuntime().freeMemory();
        if(freeMemory<totalToReceive){
            //no enough memory ,don't bother to continue.
            System.out.println("no enough memory");
        }

        if (requestDataRecordStore != null) {
            try {
                final StringBuffer content = new StringBuffer();
                for(int i=0;i<recordId.size();i++){
                    int rd=((Integer) recordId.elementAt(i)).intValue();
                    int rdSize=requestDataRecordStore.getRecord(rd, cbuf, 0);
                    content.append(new String(cbuf,0,rdSize,"utf-8"));
                }
                response.rawContent = content.toString();
                System.gc();System.gc();
                response.result = Result.fromContent(response.rawContent,
                        response.contentType);

                
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }finally{
                try {
                    requestDataRecordStore.closeRecordStore();
                } catch (RecordStoreNotOpenException ex) {
                    ex.printStackTrace();
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * read the response
     * @param conn
     * @param response
     * @throws IOException
     */
    private void readResponse(final HttpConnection conn,
            final Response response) throws IOException {

        if (lowMemory) {
            readResposeViaRecordStore(conn, response);
        } else {
            int thisTotalToReceive = 0;
            totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);

            final byte[] cbuf = new byte[BUFFER_SIZE];
            ByteArrayOutputStream bos = null;
            InputStream is = null;
            try {
                is = conn.openInputStream();
                bos = new ByteArrayOutputStream();
                int nread = 0;
                while ((nread = is.read(cbuf)) > 0 && !interrupted) {
                    bos.write(cbuf, 0, nread);
                    received += nread;
                    thisTotalToReceive += nread;
                    if (listener != null) {
                        try {
                            listener.readProgress(context, received, totalToReceive);
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                }
            } finally {
                if (is != null) {
                    is.close();
                }
                if (bos != null) {
                    bos.close();
                }
                totaldownloadedBytes += thisTotalToReceive;
            }

            if (interrupted) {
                return;
            }

            response.rawArray = bos.toByteArray();
            final String content = new String(response.rawArray, "utf-8");
            response.rawContent = content;
            try {
                response.result = Result.fromContent(content, response.contentType);
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * copy the response header.
     * @param conn
     * @param response
     * @throws IOException
     */
    private void copyResponseHeaders(final HttpConnection conn,
            final Response response) throws IOException {

        // pass 1 - count the number of headers
        int headerCount = 0;
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                headerCount++;
            }
        }

        response.headers = new Arg[headerCount];

        // pass 2 - now copy the headers
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                response.headers[i] = new Arg(key, val);
            }
        }
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process the content
     * @param conn
     * @param response
     * @throws IOException
     */
    private void processContentType(final HttpConnection conn,
            final Response response) throws IOException {

        response.contentType = conn.getHeaderField(Arg.CONTENT_TYPE);
        if (response.contentType == null) {
            // assume UTF-8 and XML if not specified
            response.contentType = Result.APPLICATION_XML_CONTENT_TYPE;
            response.charset = UTF8_CHARSET;
            return;
        }
        final int semi = response.contentType.indexOf(';');
        if (semi >= 0) {
            response.charset = response.contentType.substring(semi + 1).trim();
            final int eq = response.charset.indexOf('=');
            if (eq < 0) {
                throw new IOException("Missing charset value: " + response.charset);
            }
            response.charset = unquote(response.charset.substring(eq + 1).trim());
            response.contentType = response.contentType.substring(0, semi).trim();
        }
        if (response.charset != null) {
            String charset = response.charset.toLowerCase();
            if (!(charset.startsWith(UTF8_CHARSET) ||
                    charset.endsWith(UTF8_CHARSET) ||
                    charset.startsWith(ISO8859_CHARSET) ||
                    charset.endsWith(ISO8859_CHARSET) ||
                    charset.startsWith(GB2312_CHARSET) ||
                    charset.endsWith(GB2312_CHARSET))) {
                throw new IOException("Unsupported charset: " + response.charset);
            }
        }

    }

    private static String unquote(final String str) {
        if (str.startsWith("\"") && str.endsWith("\"") ||
                str.startsWith("'") && str.endsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    
    private static String encode(final String str)
            throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return str.replace(' ', '+');
    }
}
