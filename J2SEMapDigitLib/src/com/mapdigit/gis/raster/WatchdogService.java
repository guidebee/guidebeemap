//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 31AUG2009  James Shen                              Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.util.Log;
import java.util.Enumeration;
import java.util.Hashtable;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 31AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Watch dog service.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 31/08/09
 * @author      Guidebee Pty Ltd.
 */
class WatchdogService implements Runnable{
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor
     * @param mapClient map client class.
     */
    WatchdogService(MapClient mapClient){
        this.mapClient=mapClient;

    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add thread to the watch list.
     * @param thread
     * @param name
     */
    public void addThreadToWatchList(Thread thread, String name) {
        synchronized(syncObject){
            if(!threadList.containsKey(name)){
                threadList.put(name, thread);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check thread is alive or not
     * @param name
     * @return
     */
    public boolean isThreadLive(String name) {
       synchronized(syncObject){
           Thread thread=(Thread)threadList.get(name);
           if(thread!=null){
               return thread.isAlive();
           }
           return false;

       }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void run() {
        Log.p( Thread.currentThread().getName()+" thread started");
        while(!stopRunning){
            synchronized(syncObject){
                Log.p("Watchdog checking...");
                Enumeration enumeration=threadList.keys();
                boolean needTorestart=false;
                while(enumeration.hasMoreElements()){
                    String name=(String)enumeration.nextElement();
                    Thread thread=(Thread)threadList.get(name);
                    if(thread.isAlive()){
                    }else{
                        needTorestart=true;
                   }
                }
                if (needTorestart) {
                    try {
                        threadList.clear();
                        Log.p("restart workers");
                        mapClient.clearMapDirection();
                        mapClient.restart();
                        syncObject.wait(CHECK_PERIOD);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    syncObject.wait(CHECK_PERIOD);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        Log.p( Thread.currentThread().getName()+" thread stopped");
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * start the watch dog.
     */
    public void start(){

        watchdogThread=new Thread(this,"Watchdog");
        //watchdogThread.setPriority(Thread.MAX_PRIORITY);
        watchdogThread.start();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Stop the watch dog.
     */
    public void stop(){
        stopRunning=true;
        synchronized(syncObject){
            syncObject.notifyAll();
        }
    }

    private final Hashtable threadList=new Hashtable();
    private final Object syncObject=new Object();
    private volatile boolean stopRunning=false;
    private final int CHECK_PERIOD=60000;
    private Thread watchdogThread=null;
    private final MapClient mapClient;

}
