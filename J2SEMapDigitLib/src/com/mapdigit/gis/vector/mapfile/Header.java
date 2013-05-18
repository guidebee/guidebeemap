//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector.mapfile;

//--------------------------------- IMPORTS ------------------------------------
import java.io.DataInputStream;
import java.io.IOException;

import com.mapdigit.gis.vector.DataField;
import com.mapdigit.gis.geometry.GeoLatLngBounds;
import com.mapdigit.util.DataReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Header section of the map file.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class Header extends Section{
    
    public String fileVersion;
    public String fileFormat;
    public String dominantType;
    public int indexOffset = 0;
    public int indexLength = 0;
    public int rtreeIndexOffset = 0;
    public int rtreeIndexLength = 0;
    public int stringIndexOffset = 0;
    public int stringIndexLength = 0;
    public int stringDataOffset = 0;
    public int stringDataLength = 0;
    public int geoDataOffset = 0;
    public int geoDataLength = 0;
    public int tabularDataOffset = 0;
    public int tabularDataLength = 0;
    public int recordCount=0;
    public GeoLatLngBounds mapBounds=new GeoLatLngBounds();
    public int numberOfFields=0;
    public boolean isJava=false;
    public DataField[] fields;  
    
    public static final byte [] javaArray=new byte[]{0x00,0x04,0x4a,0x41,0x56,0x41};
    /**
     * when store latitude/longitude , it store as integer.
     * to convert to an interget ,it muliple by DOUBLE_PRECISION.
     */
    private static final double DOUBLE_PRECISION=10000000.0;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public Header(DataInputStream reader,long offset,long size) 
        throws IOException{
        super(reader, offset, size);
        
        if(!isJavaFormat()){
            throw new IOException("Invalid map file format!");
        }
        DataReader.seek(reader,0);
        fileVersion=DataReader.readString(reader);
        DataReader.seek(reader,16);
        fileFormat=DataReader.readString(reader);
        DataReader.seek(reader,32);
        dominantType=DataReader.readString(reader);
        DataReader.seek(reader,48);
        recordCount=DataReader.readInt(reader);
        int minX,minY,maxX,maxY;
        minX=DataReader.readInt(reader);
        minY=DataReader.readInt(reader);
        maxX=DataReader.readInt(reader);
        maxY=DataReader.readInt(reader);
        mapBounds.x=(double)minX/DOUBLE_PRECISION;
        mapBounds.y=(double)minY/DOUBLE_PRECISION;
        mapBounds.width=(double)(maxX-minX)/DOUBLE_PRECISION;
        mapBounds.height=(double)(maxY-minY)/DOUBLE_PRECISION;
        indexOffset = DataReader.readInt(reader);
        indexLength = DataReader.readInt(reader);
        rtreeIndexOffset = DataReader.readInt(reader);
        rtreeIndexLength = DataReader.readInt(reader);
        stringIndexOffset = DataReader.readInt(reader);
        stringIndexLength = DataReader.readInt(reader);
        stringDataOffset = DataReader.readInt(reader);
        stringDataLength = DataReader.readInt(reader);
        geoDataOffset = DataReader.readInt(reader);
        geoDataLength = DataReader.readInt(reader);
        tabularDataOffset = DataReader.readInt(reader);
        tabularDataLength = DataReader.readInt(reader);
        numberOfFields = DataReader.readInt(reader);
        fields=new DataField[numberOfFields];
        for(int i=0;i<numberOfFields;i++){
            String fieldName;
            byte fieldType;
            int fieldWidth;
            short fieldPrecision;
            fieldName=DataReader.readString(reader);
            fieldType=reader.readByte();
            fieldWidth=DataReader.readInt(reader);
            fieldPrecision=DataReader.readShort(reader);
            fields[i]=new DataField(fieldName,fieldType,fieldWidth,fieldPrecision);
        }
        this.size=(numberOfFields+1)*256;
        
        
    }
    
    
    private boolean isJavaFormat() throws IOException{
        byte [] buffer=new byte[javaArray.length];
        DataReader.seek(reader,16);
        reader.read(buffer);
        for(int i=0;i<buffer.length;i++) {
            if(buffer[i]!=javaArray[i]){
                return false;
            }
        }
        return true;
        
        
    }
    
}
