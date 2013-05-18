//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 08AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.util;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.rms.RecordStore;
import com.mapdigit.rms.RecordStoreException;
import java.io.Writer;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 08AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Pluggable logging framework that allows a developer to log into storage
 * using the file connector API. It is highly recommended to use this
 * class coupled with Netbeans preprocessing tags to reduce its overhead
 * completely in runtime.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     1.00, 08/08/09
 * @author Guidebee Pty Ltd
 */
public class Log {

    /**
     * Constant indicating the logging level Debug is the default and the
     * lowest level followed by info, warning and error
     */
    public static final int DEBUG = 1;
    /**
     * Constant indicating the logging level Debug is the default and the
     * lowest level followed by info, warning and error
     */
    public static final int INFO = 2;
    /**
     * Constant indicating the logging level Debug is the default and the
     * lowest level followed by info, warning and error
     */
    public static final int WARNING = 3;
    /**
     * Constant indicating the logging level Debug is the default and the
     * lowest level followed by info, warning and error
     */
    public static final int ERROR = 4;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Installs a log subclass that can replace the logging destination/behavior
     *
     * @param newInstance the new instance for the Log object
     */
    public static void install(Log newInstance) {
        instance = newInstance;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * use the dummy log, dummy log only print on screen, system.out.
     */
    public static void useDummyLog() {
        DummyLog dummyLog=new DummyLog();
        instance = dummyLog;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default println method invokes the print instance method, uses DEBUG level
     *
     * @param text the text to print
     */
    public static void p(String text) {
        p(text, DEBUG);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default println method invokes the print instance method, uses given level
     *
     * @param text the text to print
     * @param level one of DEBUG, INFO, WARNING, ERROR
     */
    public static void p(String text, int level) {
        instance.print(text, level);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 31MAY2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the logging level for printing log details, the lower the value
     * the more verbose would the printouts be
     *
     * @param level one of DEBUG, INFO, WARNING, ERROR
     */
    public static void setLevel(int level) {
        instance.level = level;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the logging level for printing log details, the lower the value
     * the more verbose would the printouts be
     *
     * @return one of DEBUG, INFO, WARNING, ERROR
     */
    public static int getLevel() {
        return instance.level;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the contents of the log as a single long string to be displayed by
     * the application any way it sees fit
     *
     * @return string containing the whole log
     */
    public static String getLogContent() {
        try {
            String text = "";

            RecordStore store = RecordStore.openRecordStore(logRecordStore, true);
            int size = store.getNumRecords();
            for (int iter = 1; iter <= size; iter++) {
                text += new String(store.getRecord(iter));
            }
            store.closeRecordStore();

            return text;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    protected int level = DEBUG;
    private static Log instance = new Log();
    private long zeroTime = System.currentTimeMillis();
    private Writer output;
    private static String logRecordStore="guidebee_log";

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default log implementation prints to the console and the file connector
     * if applicable. Also prepends the thread information and time before
     *
     * @param text the text to print
     * @param level one of DEBUG, INFO, WARNING, ERROR
     */
    protected void print(String text, int level) {
        if (this.level > level) {
            return;
        }
        text = getThreadAndTimeStamp() + " - " + text;
        System.out.println(text);

//        try {
//            RecordStore outputStore = RecordStore.openRecordStore(logRecordStore, true);
//            byte[] bytes = text.getBytes();
//            outputStore.addRecord(bytes, 0, bytes.length);
//            outputStore.closeRecordStore();
//        } catch (RecordStoreException ex) {
//            ex.printStackTrace();
//        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a simple string containing a timestamp and thread name.
     */
    protected String getThreadAndTimeStamp() {
        long time = System.currentTimeMillis() - zeroTime;
        long milli = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60;
        time /= 60;
        long hour = time % 60;

        return "[" + Thread.currentThread().getName() + "] " + hour + ":"
                + min + ":" + sec + "," + milli;
    }
}

 class DummyLog extends Log{
    protected void print(String text, int level){
        if(this.level > level) {
            return;
        }
        text = getThreadAndTimeStamp() + " - " + text;
        System.out.println(text);
    }

}
