//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location.nmea;

//--------------------------------- IMPORTS ------------------------------------
import java.io.IOException;
import java.util.Vector;
import java.io.InputStream;

import com.mapdigit.gis.location.LocationProvider;
import com.mapdigit.util.Log;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A NMEACompatibleLocationProvider represents a NEMA0183 compatible devcie
 * generating Locations.
 * Applications obtain NMEACompatibleLocationProvider instances (classes
 * implementing the actual functionality by extending this abstract class)
 * by calling the one of the factory methods.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
public class NMEACompatibleLocationProvider extends LocationProvider{


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Start lister to an NMEA data input stream
     * @param input the NMEA input.
     */
    public void startListening(InputStream input) throws IOException{
        nmeaInputStream=input;
        InputStream stream=new Object().getClass().getResourceAsStream("/guidebee.lic");
        if(stream==null){
            throw new IOException("No licence file found!");
        }
        if(input!=null){
            stopWorker=true;
            if(nmeaDataWorkerThread!=null){
                try {
                    nmeaDataWorkerThread.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            stopWorker=false;
            nmeaDataWorkerThread=new Thread(new NMEADataWorker(),"NMEADataWorker");
            nmeaDataWorkerThread.setPriority(Thread.MIN_PRIORITY);
            nmeaDataWorkerThread.start();
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Stop the listerning.
     */
    public void stopListening(){
        stopWorker=true;
        if(nmeaDataWorkerThread!=null){
            try {
                nmeaDataWorkerThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        nmeaDataWorkerThread=null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 12MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set NMEA data listener.
     * @param listener - the NMEA data listener.
     */
    public void setNMEADataListener(INMEADataListener listener){
        nmeaDataListener=listener;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parse the given string.
     * @param input - the input string to parse.
     */
    public void parse(String input){
       byte[] array=input.getBytes();
       parse(array,array.length);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parse the given byte array.
     * @param input - the input to parse in bytes
     */
    public void parse(byte[] input,int len){
        synchronized(syncObject){
            //if need one fix or periodically update.
            boolean bNeedFix=false;
            long currentTime=System.currentTimeMillis();
            if(getOneFix){
                bNeedFix=true;
            }else if(locationListener!=null){

                if(currentTime-previousFixtime>locationInterval*1000){
                    bNeedFix=true;
                }
            }
            if(bNeedFix || nmeaDataListener!=null){
                nmeaParser.parse(input,len);
                processNMEADataRecords();
                previousFixtime=currentTime;
            }

        }
    }

    /**
     * NMEA Parser
     */
    private final NMEAParser nmeaParser=new NMEAParser();


    /**
     * the NMEA data listener.
     */
    protected INMEADataListener nmeaDataListener=null;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parse the given string.
     * @param input - the input string to parse.
     */
    private void processNMEADataRecords(){
        Vector dataRecords=nmeaParser.NMEADataRecords;
        for(int i=0;i<dataRecords.size();i++){
            NMEADataRecord dataRecord=(NMEADataRecord)
                dataRecords.elementAt(i);
            switch(dataRecord.recordType){
                case NMEADataRecord.TYPE_GPGGA:
                    {
                        NMEAGPGGADataRecord GPGGADataRecord=
                          (NMEAGPGGADataRecord)dataRecord;
                        currentLocation.latitude=GPGGADataRecord.latitude;
                        currentLocation.longitude=GPGGADataRecord.longitude;
                        currentLocation.altitude=GPGGADataRecord.altitude;
                        currentLocation.timeStamp=GPGGADataRecord.timeStamp;
                        currentLocation.HDOP=GPGGADataRecord.HDOP;
                        if(GPGGADataRecord.receiverMode==0){
                            currentState=LocationProvider.TEMPORARILY_UNAVAILABLE;
                        }else{
                            currentState=LocationProvider.AVAILABLE;
                        }
                    }
                    break;
                case NMEADataRecord.TYPE_GPRMC:
                    {
                        NMEAGPRMCDataRecord GPRMCDataRecord=
                          (NMEAGPRMCDataRecord)dataRecord;
                        currentLocation.latitude=GPRMCDataRecord.latitude;
                        currentLocation.longitude=GPRMCDataRecord.longitude;
                        currentLocation.speed=GPRMCDataRecord.speed;
                        currentLocation.timeStamp=GPRMCDataRecord.timeStamp;
                        currentLocation.course=GPRMCDataRecord.course;
                        currentLocation.status=GPRMCDataRecord.status;
                    }
                    break;
                case NMEADataRecord.TYPE_GPVTG:
                    {
                        NMEAGPVTGDataRecord GPVTGDataRecord=
                          (NMEAGPVTGDataRecord)dataRecord;
                        currentLocation.speed=GPVTGDataRecord.speedKnot;
                        currentLocation.course=GPVTGDataRecord.course;

                    }
                    break;
                case NMEADataRecord.TYPE_GPGLL:
                    {
                        NMEAGPGLLDataRecord GPGLLDataRecord=
                          (NMEAGPGLLDataRecord)dataRecord;
                        currentLocation.latitude=GPGLLDataRecord.latitude;
                        currentLocation.longitude=GPGLLDataRecord.longitude;
                        currentLocation.timeStamp=GPGLLDataRecord.timeStamp;
                        currentLocation.status=GPGLLDataRecord.status;
                        if(!GPGLLDataRecord.status){
                            currentState=LocationProvider.TEMPORARILY_UNAVAILABLE;
                        }else{
                            currentState=LocationProvider.AVAILABLE;
                        }

                    }
                    break;
                case NMEADataRecord.TYPE_GPGSA:
                    {
                        NMEAGPGSADataRecord GPGSADataRecord=
                          (NMEAGPGSADataRecord)dataRecord;
                        currentLocation.PDOP=GPGSADataRecord.PDOP;
                        currentLocation.VDOP=GPGSADataRecord.VDOP;
                        currentLocation.HDOP=GPGSADataRecord.HDOP;
                    }
                    break;
                case NMEADataRecord.TYPE_GPGSV:

                    break;


            }
            if(nmeaDataListener!=null){
                nmeaDataListener.nmeaDataReceived(this, dataRecord);
            }
        }

        nmeaParser.NMEADataRecords.removeAllElements();

        currentLocation.speed*=1.609;
        if(getOneFix){
            syncObject.notify();
        }
        if(locationListener!=null){
            locationListener.locationUpdated(this,currentLocation);
        }

    }

    public void reset() {
        stopListening();
        if(nmeaInputStream!=null){
            try {
                nmeaInputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private volatile boolean  stopWorker=false;
    private InputStream nmeaInputStream=null;
    private Thread nmeaDataWorkerThread=null;



    private class NMEADataWorker implements Runnable{

        private final byte[] buffer=new byte[1024];
        public void run() {
            Log.p(Thread.currentThread().getName()+" started");
            while(!stopWorker && nmeaInputStream!=null){
                try {

                    if (nmeaInputStream.available() >0) {
                        int len=nmeaInputStream.read(buffer);
                        if(nmeaDataListener!=null || locationListener!=null){
                            parse(buffer, len);
                        }
                    } Thread.sleep(1000);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
            Log.p(Thread.currentThread().getName()+" stopped");
        }

    }

}
