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
import java.io.IOException;
import java.io.InputStream;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A subclass of inputStream which  provide read progress informaiton.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
public class ProgressInputStream extends InputStream {
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param is
     * @param total
     * @param listener
     * @param context
     * @param notifyInterval
     */
    public ProgressInputStream(final InputStream is, final int total,
        final IRequestListener listener, final Object context,
        final int notifyInterval) {
        this.is = is;
        this.total = total;
        this.listener = listener;
        this.context = context;
        this.notifyInterval = notifyInterval;
        nread = 0;
    }
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * read a int.
     * @return
     * @throws IOException
     */
    public int read() throws IOException {
        if ((++nread % notifyInterval) == 0) {
            try {
                listener.readProgress(context, nread, total);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return is.read();
    }
    
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * close the input stream.
     * @throws IOException
     */
    public void close() throws IOException {
        is.close();
        super.close();
    }

    private final InputStream is;
    private final int total;
    private final IRequestListener listener;
    private final Object context;
    private final int notifyInterval;
    private int nread;

}
