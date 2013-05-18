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
import java.util.Date;
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11MAR2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * NEMA 0183 parser.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 11/03/09
 * @author      Guidebee Pty Ltd.
 */
class NMEAParser {

    /**
     *   GPS DOP and active satellites.
     */
    public static final String DOLLAR_SIGN_GPGSA = "$GPGSA";

    /**
     * Global positioning system fixed data.
     */
    public static final String DOLLAR_SIGN_GPGGA = "$GPGGA";

    /**
     * Recommended minimum specific GPS/Transit data.
     */
    public static final String DOLLAR_SIGN_GPRMC = "$GPRMC";

    /**
     * Satellites in view.
     */
    public static final String DOLLAR_SIGN_GPGSV = "$GPGSV";

    /**
     * Location Fix.
     */
    public static final String DOLLAR_SIGN_GPGLL = "$GPGLL";

    /**
     * ground speed.
     */
    public static final String DOLLAR_SIGN_GPVTG = "$GPVTG";

    /**
     * Size of the string buffer. This should be a little more than the size of
     * the byte array plus 80 (the max size of an NMEA sentence).
     */
    public static final short OUTPUT_BUFFER_MAX_SIZE = 2048;

    /**
     * The maximum size of a sentence according to the NMEA standards is 82. We
     * will use 128 to be safe.
     */
    public static final short MAX_SENTENCE_SIZE = 128;

    /**
     * Sentence characters
     */
    public static final byte SENTENCE_START = '$';

    /**
     * checksum start.
     */
    public static final byte CHECKSUM_START = '*';

    /**
     * sentence end character.
     */
    public static final byte SENTENCE_END = '\n';

    /**
     * parameter delimter.
     */
    public static final byte DELIMITER = ',';

    /**
     * There was not enough to process
     */
    public static final short TYPE_NOTHING_TO_PROCESS = -1;

    /**
     * Parsed NMEA data records.
     */
    public final Vector NMEADataRecords = new Vector();
    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Cosntructor.
     *
     */
    public NMEAParser() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Append the input and parse it. The size of the input must always be
     * less than {@link #OUTPUT_BUFFER_MAX_SIZE}.
     *
     * @param input - the input to parse in bytes
     * @param size - the size of the input
     * @return a integer to indicate which sentences were parsed
     */
    public int parse(byte[] input, int size) {
        append(input, size);
        return doParse();
    }

    /**
     * The current data read from the GPS device
     */
    private final byte[] data = new byte[OUTPUT_BUFFER_MAX_SIZE];

    /**
     * The length of <code>data</code>
     */
    private int dataLength = 0;

    /**
     * temp hashtable used to determin all GSV sentences have been parsed.
     */
    private final Vector GPGSVTable = new Vector();

    /**
     * temporaily stored satellites in view.
     */
    private final NMEAGPGSVDataRecord satellitesInfo = new NMEAGPGSVDataRecord();

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Append the input to the input buffer. The size of the input must
     * always be less than {@link #OUTPUT_BUFFER_MAX_SIZE}.
     *
     * @param input - the input in bytes
     * @param size - the size of the input
     */
    private void append(byte[] input, int size) {

        // Allocate a new buffer if we got so much data that it will
        // contain all our information and the buffer from before can
        // be discarded to save the overhead of processing it.
        if (dataLength + size >= data.length) {
            flush();
        }

        // Append input to data left over from a past append().
        int start = 0;
        int length = size;

        if (size > data.length) {
            start = size - data.length;
            length = size - start;
        }

        System.arraycopy(input, start, data, dataLength, length);
        dataLength += size;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Flush the buffer.
     */
    private void flush() {
        dataLength = 0;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parse the data from the Bluetooth GPS device into NMEA sentences.
     * @return a integer to indicate which sentences were parsed
     */
    private int doParse() {
 
        int parsedSentenceTypes = NMEADataRecord.TYPE_NONE;

        // If there is hardly anything in the buffer, there won't be a
        // NMEA sentence, so don't bother processing it.
        if (dataLength < 16) {
            return NMEADataRecord.TYPE_NONE;
        }

        // Set the current index to be the
        int currentIndex = dataLength - 1;

        // True if the current sentence is the last sentence in the buffer
        boolean isLastSentence = true;

        // Index of the start of the last sentence
        int lastSentenceStart = -1;

        // While there are characters left to process
        while (currentIndex > 0) {
            // Find the start of the last NMEA sentence.
            int sentenceStart =
                    lastIndexOf(data, SENTENCE_START, currentIndex);

            // Did we find the start of a sentence?
            if (sentenceStart != -1) {
                // We found the start of a sentence, look for the end.
                int sentenceEnd =
                        indexOf(data, SENTENCE_END, sentenceStart, dataLength);

                // Did we find the sentence end?
                if (sentenceEnd != -1) {
                    // Look for the first delimitter to get the sentence type.
                    // (i.e. String.indexOf(DELIMITER, sentenceStart))
                    int sentenceTypeEnd =
                            indexOf(data, DELIMITER, sentenceStart, sentenceEnd);

                    // If we found the type end and the sentence end is within
                    // this sentence, then process the sentence. By checking t
                    // hat the sentence end is less than the current index then 
                    // we handle the the case that we have a buffer left of
                    // "$GPRMC,45667,V,4354.788"
                    // and the first chunch of the new chars does not end the
                    // same sentence
                    // but instead starts a new one.
                    if ((sentenceTypeEnd != -1) &&
                            (sentenceEnd <= currentIndex)) {
                        try {
                            String type = new String(data, sentenceStart,
                                    sentenceTypeEnd - sentenceStart);

                            if ((type.equals(DOLLAR_SIGN_GPRMC)) &&
                                    ((parsedSentenceTypes & NMEADataRecord.TYPE_GPRMC) == 0)) {
                                parsedSentenceTypes = parsedSentenceTypes |
                                        processSentence(data,
                                        sentenceStart, sentenceEnd, NMEADataRecord.TYPE_GPRMC);
                            } else if ((type.equals(DOLLAR_SIGN_GPGGA)) &&
                                    ((parsedSentenceTypes & NMEADataRecord.TYPE_GPGGA) == 0)) {
                                parsedSentenceTypes = parsedSentenceTypes |
                                        processSentence(data,
                                        sentenceStart, sentenceEnd, NMEADataRecord.TYPE_GPGGA);
                            } else if ((type.equals(DOLLAR_SIGN_GPGSA)) &&
                                    ((parsedSentenceTypes & NMEADataRecord.TYPE_GPGSA) == 0)) {
                                parsedSentenceTypes = parsedSentenceTypes |
                                        processSentence(data,
                                        sentenceStart, sentenceEnd, NMEADataRecord.TYPE_GPGSA);
                            } else if ((type.equals(DOLLAR_SIGN_GPGSV)) &&
                                    ((parsedSentenceTypes & NMEADataRecord.TYPE_GPGSV) == 0)) {
                                parsedSentenceTypes = parsedSentenceTypes |
                                        processSentence(data,
                                        sentenceStart, sentenceEnd, NMEADataRecord.TYPE_GPGSV);
                            } else if ((type.equals(DOLLAR_SIGN_GPGLL)) &&
                                    ((parsedSentenceTypes & NMEADataRecord.TYPE_GPGLL) == 0)) {
                                parsedSentenceTypes = parsedSentenceTypes |
                                        processSentence(data,
                                        sentenceStart, sentenceEnd, NMEADataRecord.TYPE_GPGLL);
                            } else if ((type.equals(DOLLAR_SIGN_GPVTG)) &&
                                    ((parsedSentenceTypes & NMEADataRecord.TYPE_GPVTG) == 0)) {
                                parsedSentenceTypes = parsedSentenceTypes |
                                        processSentence(data,
                                        sentenceStart, sentenceEnd, NMEADataRecord.TYPE_GPVTG);
                            }
                        } catch (Throwable t) {
                            // We are kind of screwed at this point so just return
                            // what we have and flush the buffer.
                            flush();
                            return parsedSentenceTypes;
                        }
                        // move the current position
                        currentIndex = sentenceStart - 1;
                        // Check if we have a complete record. If so we do
                        // not need to keep working with this buffer
                        // Check if we have a complete record. If so we do
                        // not need to keep working with this buffer
                        if (parsedSentenceTypes == NMEADataRecord.ALL_TYPES_MASK) {
                            break;
                        }

                    } else {
                        // This sentence is junk, so just skip it
                        currentIndex = sentenceStart - 1;
                    }
                } else {
                    // If this is the last sentence in the buffer, then keep the
                    // index of the start so that we do not delete it.
                    if (isLastSentence) {
                        lastSentenceStart = sentenceStart;
                    }

                    currentIndex = sentenceStart - 1;


                }
            } else {
                break;
            }

            // Once we have completed an iteration, set the last sentence flag
            // to false
            isLastSentence = false;
        } // while

        // Throw away everything that has already been parsed.
        if (lastSentenceStart < 0) {
            // Processed everything.  No partial sentence left at the end.
            flush();
        } else {
            // Keep the partial last sentence.
            dataLength -= lastSentenceStart;
            System.arraycopy(data, lastSentenceStart, data, 0, dataLength);
        }

        // If we parsed any of the sentences that we care about, put the record
        // in the buffer.
        if (parsedSentenceTypes != 0) {
            // Put the record in the record buffer
            //setRecordBuffer( record );
            // Create a new record from the existing record
            //record = new GPSRecord(record);
        }

        return parsedSentenceTypes;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Looks for the first occurance of a byte <code>b</code> in 
     * <code>array</code> between 
     * [<code>fromIndex</code>, <code>stopIndex</code>).
     *
     * @param array is the data to scan.
     * @param b is the byte to match.
     * @param fromIndex is the first index into array to check.
     * @param stopIndex is one past the last index into array to check.
     * @return The first index where <code>b</code> was found; -1 if it was not
     *  found.
     */
    private static int indexOf(byte[] array, byte b, int fromIndex,
            int stopIndex) {
        for (int position = fromIndex; position < stopIndex; position++) {
            if (array[position] == b) {
                return position;
            }
        }

        // If we made it here, b was not found.
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Looks for the last occurance of a byte <code>b</code> in
     * <code>array</code> going
     * backwards from <code>fromIndex</code>.
     *
     * @param array is the data to scan.
     * @param b is the byte to match.
     * @param fromIndex is the first index into array to check.
     * @return The last index where <code>b</code> was found; -1 if it was not
     *  found.
     */
    private static int lastIndexOf(byte[] array, byte b, int fromIndex) {
        for (int position = fromIndex; position >= 0; position--) {
            if (array[position] == b) {
                return position;
            }
        }

        // If we made it here, b was not found.
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check the checksum is correct or not.
     */
    private boolean checkSum(byte[] buffer) {
        String nmeaSentence = new String(buffer);
        int intstarpos = nmeaSentence.indexOf('*');
        if (intstarpos >= 0) {
            // we have a checksum so check it...
            String strChecksum = nmeaSentence.substring(intstarpos + 1);
            // remove checksum from end of string
            String strData = nmeaSentence.substring(0, nmeaSentence.length()
                    - strChecksum.length() - 1);
            int intor = 0;
            //go from first character upto last *
            for (int i = 1; (i < strData.length()); i++) {
                intor = intor ^ (int) (strData.charAt(i));
            }

            int y = 0;

            try {
                y = Integer.parseInt(strChecksum, 16);
            } catch (Exception e) {
                return false;
            }
            if (intor != y) {
                // debug for checksum failures
                intor += 0;
            }
            return (intor == y);
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Process the sentence of the specified type.  The sentence is
     * every ASCII character stored in <code>data</code> between
     * <code>offset</code> and <code>stop</code>.
     *
     * @param data contains the NMEA sentence to process.
     * @param offset is the index that starts the NMEA sentence within 
     * <code>data</code>.
     * @param stop is the index of the final character in the sentence.
     * @param type - the sentence type
     * @return the type of the setence processed. If the sentence cannot be
     *         processed this returns 0.
     */
    private short processSentence(byte[] data, int offset, int stop, short type) {
        // If the sentence is greater than the max size just discard it
        short retType = NMEADataRecord.TYPE_NONE;
        if (stop - offset <= MAX_SENTENCE_SIZE) {
            //get rid of \r\n
            int length = stop - offset + 1;
            if (data[stop] == 10) {
                length--;
            }
            if (data[stop - 1] == 13) {
                length--;
            }
            byte[] buffer = new byte[length];
            System.arraycopy(data, offset, buffer, 0, buffer.length);
            if (checkSum(buffer)) {
                String nmeaSentence = new String(buffer);
                switch (type) {
                    case NMEADataRecord.TYPE_GPRMC:
                        retType = processGPRMC(nmeaSentence);
                        break;
                    case NMEADataRecord.TYPE_GPGGA:
                        retType = processGPGGA(nmeaSentence);
                        break;
                    case NMEADataRecord.TYPE_GPGSA:
                        retType = processGPGSA(nmeaSentence);
                        break;
                    case NMEADataRecord.TYPE_GPGSV:
                        retType = processGPGSV(nmeaSentence);
                        break;
                    case NMEADataRecord.TYPE_GPGLL:
                        retType = processGPGLL(nmeaSentence);
                        break;
                    case NMEADataRecord.TYPE_GPVTG:
                        retType = processGPVTG(nmeaSentence);
                        break;
                    default:
                        break;
                }
            }
        }

        return retType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * latitude in ddmm.mmmmmm
     */
    private double latitudeToDouble(String latitude, String direction) {
        double ret = 0.0;
        try {
            String strDegree = latitude.substring(0, 2);
            String strMinutes = latitude.substring(2, latitude.length());
            double dblDegree = Double.parseDouble(strDegree);
            double dblMinute = Double.parseDouble(strMinutes) / 60.0;
            ret = dblDegree + dblMinute;
            if (direction.equalsIgnoreCase("S")) {
                ret = -ret;
            }
        } catch (Exception e) {
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * longitude in dddmm.mmmmmm
     */
    private double longitudeToDouble(String longitude, String direction) {
        double ret = 0.0;
        try {
            String strDegree = longitude.substring(0, 3);
            String strMinutes = longitude.substring(3, longitude.length());
            double dblDegree = Double.parseDouble(strDegree);
            double dblMinute = Double.parseDouble(strMinutes) / 60.0;
            ret = dblDegree + dblMinute;
            if (direction.equalsIgnoreCase("W")) {
                ret = -ret;
            }
        } catch (Exception e) {
        }
        return ret;
    }

    /**
        RMC Recommended Minimum Navigation Information
            12
            1 2 3 4 5 6 7 8 9 10 11|
            | | | | | | | | | | | |
            $--RMC,hhmmss.ss,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,xxxx,x.x,a*hh
            1) Time (UTC)
            2) Status, V = Navigation receiver warning
            3) Latitude
            4) N or S
            5) Longitude
            6) E or W
            7) Speed over ground, knots
            8) Track made good, degrees true
            9) Date, ddmmyy
            10) Magnetic Variation, degrees
            11) E or W
            12) Checksum
     **/
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process GPRMC sentence.
     */
    private short processGPRMC(String nmeaSentence) {
        NMEAGPRMCDataRecord dataRecord = new NMEAGPRMCDataRecord();

        //UTC
        int commaIndex1 = nmeaSentence.indexOf(DELIMITER);
        int commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strUTC = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Status
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strStatus = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Latitude
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLatitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        //Direction
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLatitudeDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Longitude
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLongitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        //Direction
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLongitudeDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Speed
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strSpeed = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Course
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strCourse = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Date
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strDate = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Magnetic
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strMagnetic = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //MagneticDir
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        if (commaIndex2 == -1) {
            commaIndex2 = nmeaSentence.indexOf(CHECKSUM_START, commaIndex1 + 1);
        }
        String strMagneticDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        int utchours = Integer.parseInt(strUTC.substring(0, 2));
        int utcminutes = Integer.parseInt(strUTC.substring(2, 4));
        int utcseconds = Integer.parseInt(strUTC.substring(4, 6));
        int day = Integer.parseInt(strDate.substring(0, 2));
        int month = Integer.parseInt(strDate.substring(2, 4));
        // available for this century
        int year = Integer.parseInt(strDate.substring(4, 6)) + 2000;
//        Calendar calendar=Calendar.getInstance();
//        calendar.set(calendar.YEAR,year);
//        calendar.set(calendar.MONTH,month);
//        calendar.set(calendar.DAY_OF_MONTH,day);
//        calendar.set(calendar.HOUR,utchours);
//        calendar.set(calendar.MINUTE,utcminutes);
//        calendar.set(calendar.SECOND,utcseconds);
        dataRecord.timeStamp = new Date();

        if (strStatus.equalsIgnoreCase("A")) {
            dataRecord.status = true;
        } else {
            dataRecord.status = false;
        }

        dataRecord.latitude = latitudeToDouble(strLatitude, strLatitudeDir);
        dataRecord.longitude = longitudeToDouble(strLongitude, strLongitudeDir);
        if (strSpeed.length() > 0) {
            dataRecord.speed = Double.parseDouble(strSpeed);
        }
        if (strCourse.length() > 0) {
            dataRecord.course = Double.parseDouble(strCourse);
        }
        if (strMagnetic.length() > 0) {
            dataRecord.magneticCourse = Double.parseDouble(strMagnetic);
            if (strMagneticDir.equalsIgnoreCase("W")) {
                dataRecord.magneticCourse = -dataRecord.magneticCourse;
            }
        }
        NMEADataRecords.addElement(dataRecord);



        return NMEADataRecord.TYPE_GPRMC;
    }

    /**
     * GGA Global Positioning System Fix Data. Time, Position and fix related data
        for a GPS receiver
                            11
        1 2 3 4 5 6 7 8 9 10 | 12 13 14 15
        | | | | | | | | | | | | | | |
        $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh
        1) Time (UTC)
        2) Latitude
        3) N or S (North or South)
        4) Longitude
        5) E or W (East or West)
        6) GPS Quality Indicator,
        0 - fix not available,
        1 - GPS fix,
        2 - Differential GPS fix
        7) Number of satellites in view, 00 - 12
        8) Horizontal Dilution of precision
        9) Antenna Altitude above/below mean-sea-level (geoid)
        10) Units of antenna altitude, meters
        11) Geoidal separation, the difference between the WGS-84 earth
        ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level below ellipsoid
        12) Units of geoidal separation, meters
        13) Age of differential GPS data, time in seconds since last SC104
        type 1 or 9 update, null field when DGPS is not used
        14) Differential reference station ID, 0000-1023
        15) Checksum
     */
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process GPGGA sentence.
     */
    short processGPGGA(String nmeaSentence) {
        NMEAGPGGADataRecord dataRecord = new NMEAGPGGADataRecord();
        //GMT
        int commaIndex1 = nmeaSentence.indexOf(DELIMITER);
        int commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strGMT = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);


        //Latitude
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLatitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        //Direction
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLatitudeDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Longitude
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLongitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        //Direction
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLongitudeDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //ReceiveMode
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strMode = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Number of satellite
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strNumberOfSatellites = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //HDOP
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strHDOP = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Altitude
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strAltitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //AltitudeUnit
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strAltitudeUnit = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Geoidal Separation
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strGeoidalSeparation = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Separation Unit
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strSeparationUnit = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Age of differential corrections in seconds
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strAOD = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Differential reference Station ID
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(CHECKSUM_START, commaIndex1 + 1);
        String strStationID = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        int utchours = Integer.parseInt(strGMT.substring(0, 2));
        int utcminutes = Integer.parseInt(strGMT.substring(2, 4));
        int utcseconds = Integer.parseInt(strGMT.substring(4, 6));
//        Calendar calendar=Calendar.getInstance();
//        calendar.set(calendar.HOUR,utchours);
//        calendar.set(calendar.MINUTE,utcminutes);
//        calendar.set(calendar.SECOND,utcseconds);
        dataRecord.timeStamp = new Date();
        dataRecord.latitude = latitudeToDouble(strLatitude, strLatitudeDir);
        dataRecord.longitude = longitudeToDouble(strLongitude, strLongitudeDir);
        if (strMode.length() > 0) {
            dataRecord.receiverMode = Integer.parseInt(strMode);
        }
        if (strNumberOfSatellites.length() > 0) {
            dataRecord.numberOfSatellites = Integer.parseInt(strNumberOfSatellites);
        }
        if (strAltitude.length() > 0) {
            dataRecord.altitude = Double.parseDouble(strAltitude);
        }
        if (strHDOP.length() > 0) {
            dataRecord.HDOP = Double.parseDouble(strHDOP);
        }
        NMEADataRecords.addElement(dataRecord);
        return NMEADataRecord.TYPE_GPGGA;
    }


    /**
     * GSA GPS DOP and active satellites
        1 2 3 14 15 16 17 18
        | | | | | | | |
        $--GSA,a,a,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x.x,x.x,x.x*hh
        1) Selection mode
        2) Mode
        3) ID of 1st satellite used for fix
        4) ID of 2nd satellite used for fix
        ...
        14) ID of 12th satellite used for fix
        15) PDOP in meters
        16) HDOP in meters
        17) VDOP in meters
        18) Checksum
     */
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process GPGSA sentence.
     */
    short processGPGSA(String nmeaSentence) {
        NMEAGPGSADataRecord dataRecord = new NMEAGPGSADataRecord();
        //Manual or Automatic Mode
        int commaIndex1 = nmeaSentence.indexOf(DELIMITER);
        int commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strMAMode = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //2D3D Mode
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String str2D3DMode = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        String[] strPRN = new String[12];

        for (int i = 0; i < 12; i++) {
            //PRN Numbers
            commaIndex1 = commaIndex2;
            commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
            strPRN[i] = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        }

        //PDOP
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strPDOP = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //HDOP
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strHDOP = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //VDOP
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        if (commaIndex2 == -1) {
            commaIndex2 = nmeaSentence.indexOf(CHECKSUM_START, commaIndex1 + 1);
        }
        String strVDOP = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        if (strMAMode.equalsIgnoreCase("M")) {
            dataRecord.manualMode = true;
        }
        if (str2D3DMode.length() > 0) {
            dataRecord.operationMode = Integer.parseInt(str2D3DMode);
        }
        for (int i = 0; i < 12; i++) {
            if (strPRN[i].length() > 0) {
                dataRecord.PRNs[i] = Integer.parseInt(strPRN[i]);
            }
        }
        if (strPDOP.length() > 0) {
            dataRecord.PDOP = Double.parseDouble(strPDOP);
        }
        if (strHDOP.length() > 0) {
            dataRecord.HDOP = Double.parseDouble(strHDOP);
        }
        if (strVDOP.length() > 0) {
            dataRecord.VDOP = Double.parseDouble(strVDOP);
        }
        NMEADataRecords.addElement(dataRecord);
        return NMEADataRecord.TYPE_GPGSA;
    }


    /**
     * GSV Satellites in view
        1 2 3 4 5 6 7 n
        | | | | | | | |
        $--GSV,x,x,x,x,x,x,x,...*hh
        1) total number of messages
        2) message number
        3) satellites in view
        4) satellite number
        5) elevation in degrees
        6) azimuth in degrees to true
        7) SNR in dB
        more satellite infos like 4)-7)
        n) Checksum
     */
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process GPGSV sentence.
     */
    short processGPGSV(String nmeaSentence) {

        //Total number of message

        int commaIndex1 = nmeaSentence.indexOf(DELIMITER);
        int commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strNumberOfMessage = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        int numberOfMessages = Integer.parseInt(strNumberOfMessage);

        int intPosOfStar = nmeaSentence.indexOf(CHECKSUM_START);
        //Message no
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strMessageNo = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        Integer messageNo = new Integer(Integer.parseInt(strMessageNo));

        if (GPGSVTable.contains(messageNo)) {
            return NMEADataRecord.TYPE_NONE;
        }
        if(GPGSVTable.isEmpty()){
            satellitesInfo.satellites.removeAllElements();
        }
        GPGSVTable.addElement(messageNo);
        //Total Satellite no
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strTotalSatellte = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        if (strTotalSatellte.length() > 0) {
            satellitesInfo.numberOfSatelltes = Integer.parseInt(strTotalSatellte);
        }
        while (commaIndex2 < intPosOfStar) {
            //PRN no
            commaIndex1 = commaIndex2;
            commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
            String strPRN = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

            // elevation ,degree ,maximum 90
            commaIndex1 = commaIndex2;
            commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
            String strElevation = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

            // Azimuth ,degree ,true 000 to 359
            commaIndex1 = commaIndex2;
            commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
            String strAzimuth = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

            // SNR (C/Nno) 00-99 db
            commaIndex1 = commaIndex2;
            commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
            if (commaIndex2 == -1) {
                commaIndex2 = nmeaSentence.indexOf(CHECKSUM_START, commaIndex1 + 1);
            }
            String strSNR = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

            Satellite satellite = new Satellite();
            if (strPRN.length() > 0) {
                satellite.id = Integer.parseInt(strPRN);
            }
            if (strElevation.length() > 0) {
                satellite.elevation = Integer.parseInt(strElevation);
            }
            if (strAzimuth.length() > 0) {
                satellite.azimuth = Integer.parseInt(strAzimuth);
            }
            if (strSNR.length() > 0) {
                satellite.snr = Integer.parseInt(strSNR);
            }
            satellitesInfo.satellites.addElement(satellite);

        }

        if (GPGSVTable.size() == numberOfMessages) {
            GPGSVTable.removeAllElements();
            NMEADataRecords.addElement(satellitesInfo);
            return NMEADataRecord.TYPE_GPGSV;
        } else {
            return NMEADataRecord.TYPE_NONE;
        }
    }

    /**
     * GLL Geographic Position Latitude/Longitude
        1 2 3 4 5 6 7
        | | | | | | |
        $--GLL,llll.ll,a,yyyyy.yy,a,hhmmss.ss,A*hh
        1) Latitude
        2) N or S (North or South)
        3) Longitude
        4) E or W (East or West)
        5) Time (UTC)
        6) Status A - Data Valid, V - Data Invalid
        7) Checksum
     */
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process GPGLL sentence.
     */
    short processGPGLL(String nmeaSentence) {

        NMEAGPGLLDataRecord dataRecord = new NMEAGPGLLDataRecord();
        //Latitude
        int commaIndex1 = nmeaSentence.indexOf(DELIMITER);
        int commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLatitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Latitude Dir
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLatitudeDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Longitude
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLongitude = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Longitude Dir
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strLongitudeDir = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //UTC
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strUTC = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);


        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        if (commaIndex2 == -1) {
            commaIndex2 = nmeaSentence.indexOf(CHECKSUM_START, commaIndex1 + 1);
        }
        String strStatus = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);
        int utchours = Integer.parseInt(strUTC.substring(0, 2));
        int utcminutes = Integer.parseInt(strUTC.substring(2, 4));
        int utcseconds = Integer.parseInt(strUTC.substring(4, 6));
//        Calendar calendar=Calendar.getInstance();
//        calendar.set(calendar.HOUR,utchours);
//        calendar.set(calendar.MINUTE,utcminutes);
//        calendar.set(calendar.SECOND,utcseconds);
        dataRecord.timeStamp = new Date();
        dataRecord.latitude = latitudeToDouble(strLatitude, strLatitudeDir);
        dataRecord.longitude = longitudeToDouble(strLongitude, strLongitudeDir);
        if (strStatus.equalsIgnoreCase("A")) {
            dataRecord.status = true;
        }
        NMEADataRecords.addElement(dataRecord);
        return NMEADataRecord.TYPE_GPGLL;
    }


    /**
     * VTG Track Made Good and Ground Speed
        1 2 3 4 5 6 7 8 9
        | | | | | | | | |
        $--VTG,x.x,T,x.x,M,x.x,N,x.x,K*hh
        1) Track Degrees
        2) T = True
        3) Track Degrees
        4) M = Magnetic
        5) Speed Knots
        6) N = Knots
        7) Speed Kilometers Per Hour
        8) K = Kilometres Per Hour
        9) Checksum
     */
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11MAR2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process GPVTG sentence.
     */
    short processGPVTG(String nmeaSentence) {

        NMEAGPVTGDataRecord dataRecord = new NMEAGPVTGDataRecord();
        //Course degree, true
        int commaIndex1 = nmeaSentence.indexOf(DELIMITER);
        int commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strCourse = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Indicates true course
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strTrueCourse = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Course ,degree , magenetic
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strMagnetic = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //indicates magnetic course
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strMagneticCourse = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Speed Knot
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strSpeedKnot = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Speed Knot unit
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strSpeedKnotUnit = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        //Speed Km
        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        String strSpeedKm = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        commaIndex1 = commaIndex2;
        commaIndex2 = nmeaSentence.indexOf(DELIMITER, commaIndex1 + 1);
        if (commaIndex2 == -1) {
            commaIndex2 = nmeaSentence.indexOf(CHECKSUM_START, commaIndex1 + 1);
        }
        String strSpeedKmUnit = nmeaSentence.substring(commaIndex1 + 1, commaIndex2);

        if (strCourse.length() > 0) {
            dataRecord.course = Double.parseDouble(strCourse);
        }

        if (strMagnetic.length() > 0) {
            dataRecord.courseMagnetic = Double.parseDouble(strMagnetic);
        }

        if (strSpeedKnot.length() > 0) {
            dataRecord.speedKnot = Double.parseDouble(strSpeedKnot);
        }

        if (strSpeedKm.length() > 0) {
            dataRecord.speedKm = Double.parseDouble(strSpeedKm);
        }
        NMEADataRecords.addElement(dataRecord);
        return NMEADataRecord.TYPE_GPVTG;
    }
}