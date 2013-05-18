//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 04JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.util;

//--------------------------------- IMPORTS ------------------------------------
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapReader read little-endian data format.
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     1.00, 04/01/09
 * @author      Guidebee Pty Ltd.
 */
public class DataReader {

    /**
     * is the data format java or windows( big endian or little endian).
     */
    public static boolean isJava=true;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Read a double value.
     * @param in data input stream.
     * @return a double value.
     * @throws IOException
     */
    public static double readDouble(DataInput in) throws IOException {
        double ret=0;
        if(!isJava){

            byte[] buffer=new byte[8];
            for(int i=0;i<8;i++) {
                buffer[7-i]=in.readByte();
            }
            ByteArrayInputStream bais=new ByteArrayInputStream(buffer);
            DataInputStream dis=new DataInputStream(bais);
            ret=dis.readDouble();
            dis.close();
            bais.close();
        } else {
            ret=in.readDouble();
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Read a long value.
     * @param in data input stream.
     * @return a long value.
     * @throws IOException
     */
    public static long readLong(DataInput in) throws IOException {
        long ret=0;
        if(!isJava){
            byte[] buffer=new byte[8];
            for(int i=0;i<8;i++) {
                buffer[7-i]=in.readByte();
            }
            ByteArrayInputStream bais=new ByteArrayInputStream(buffer);
            DataInputStream dis=new DataInputStream(bais);
            ret=dis.readLong();
            dis.close();
            bais.close();
        } else {
            ret=in.readLong();
        }

        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Read a integer value.
     * @param in data input stream.
     * @return a integer value.
     * @throws IOException
     */
    public static int readInt(DataInput in) throws IOException {
        int ret=0;
        if(!isJava){
            byte[] buffer=new byte[4];
            for(int i=0;i<4;i++) {
                buffer[3-i]=in.readByte();
            }
            ByteArrayInputStream bais=new ByteArrayInputStream(buffer);
            DataInputStream dis=new DataInputStream(bais);
            ret=dis.readInt();
            dis.close();
            bais.close();
        }else {
            ret=in.readInt();
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Read a short integer value.
     * @param in data input stream.
     * @return a short integer value.
     * @throws IOException
     */
    public static short readShort(DataInput in) throws IOException {
        short ret=0;
        if(!isJava){
            byte[] buffer=new byte[2];
            for(int i=0;i<2;i++) {
                buffer[1-i]=in.readByte();
            }
            ByteArrayInputStream bais=new ByteArrayInputStream(buffer);
            DataInputStream dis=new DataInputStream(bais);
            ret=dis.readShort();
            dis.close();
            bais.close();
        }else {
            ret=in.readShort();
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Read a string value.
     * @param in data input stream.
     * @return a string value.
     * @throws IOException
     */
    public static String readString(DataInput in) throws IOException {
        String retStr="";
        if(!isJava){
            int len=in.readUnsignedByte();
            byte []buffer=new byte[len+2];
            buffer[0]=0;
            buffer[1]=(byte)len;
            for(int i=0;i<len;i++) {
                buffer[i+2]=in.readByte();
            }
            ByteArrayInputStream bis=new ByteArrayInputStream(buffer);
            DataInputStream dis=new DataInputStream(bis);
            retStr=dis.readUTF();
            bis.close();
            dis.close();
        } else {
            retStr=in.readUTF();
        }
        return retStr;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * move the file pointer to the given position.
     * @param in data input stream.
     * @param offset offset to move the file pointer.
     * @throws IOException 
     */
    public static void seek(DataInputStream in,long offset) throws IOException {
        long total=offset;
        in.reset();
        long skipped=in.skip(offset);
        long remain=total-skipped;
        while(remain>0) {
            total=remain;
            skipped=in.skip(total);
            remain=total-skipped;
        }

    }
}
