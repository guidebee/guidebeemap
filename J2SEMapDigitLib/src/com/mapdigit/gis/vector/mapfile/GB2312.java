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
import com.mapdigit.util.DataReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * GB2312 get Pinyin code for a chinese.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public final class GB2312 {
    
    /**
     * the data input reader for the map file.
     */
    private DataInputStream reader;
    
    /**
     * how many chinese character.
     */
    private static final int NUMBER_OF_CHINESE=27954;
    
    /**
     * the size of each record.
     */
    private static final int RECORDSIZE=16;
    
    /**
     * the first Pinyin letter;
     */
    private String firstLetter;
    
    
    /**
     * the first Pinyin letter;
     */
    private static String staticFirstLetter;
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public GB2312(DataInputStream reader) {
        this.reader=reader;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * binary search (Chinese).
     * @param queryValue query string.
     * @return the string recrod ID.
     */
    public int binarySearch(String queryValue) throws IOException{
        int left=0;
        int right=NUMBER_OF_CHINESE-1;
        while(left<=right) {
            int middle=(int)Math.floor((double)((left+right)/2));
            {
                DataReader.seek(reader,middle*RECORDSIZE);
                String middleValuePinYin=DataReader.readString(reader);
                DataReader.seek(reader,middle*RECORDSIZE+8);
                String middleValue=DataReader.readString(reader);
                
                DataReader.seek(reader,left*RECORDSIZE);
                String leftValuePinYin=DataReader.readString(reader);
                DataReader.seek(reader,left*RECORDSIZE+8);
                String leftValue=DataReader.readString(reader);
                
                DataReader.seek(reader,right*RECORDSIZE);
                String rightValuePinYin=DataReader.readString(reader);
                DataReader.seek(reader,right*RECORDSIZE+8);
                String rightValue=DataReader.readString(reader);
                
                if(leftValue.length()>queryValue.length())
                    leftValue=leftValue.substring(0,queryValue.length());
                if(middleValue.length()>queryValue.length())
                    middleValue=middleValue.substring(0,queryValue.length());
                if(rightValue.length()>queryValue.length())
                    rightValue=rightValue.substring(0,queryValue.length());
                
                if(queryValue.compareTo(middleValue)==0) {
                    firstLetter=middleValuePinYin.substring(0,1);
                    return middle;
                } else if (queryValue.compareTo(middleValue)>0) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }
            
        }
        firstLetter=queryValue.substring(0,1);
        return -1;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * binary search (Chinese).
     * @param queryValue query string.
     * @return the string recrod ID.
     */
    public static int binarySearch(String queryValue,DataInputStream reader) throws IOException{
        int left=0;
        int right=NUMBER_OF_CHINESE-1;
        while(left<=right) {
            int middle=(int)Math.floor((double)((left+right)/2));
            {
                DataReader.seek(reader,middle*RECORDSIZE);
                String middleValue=DataReader.readString(reader);
                DataReader.seek(reader,middle*RECORDSIZE+8);
                String middleValuePinYin=DataReader.readString(reader);
                
                DataReader.seek(reader,left*RECORDSIZE);
                String leftValue=DataReader.readString(reader);
                DataReader.seek(reader,left*RECORDSIZE+8);
                String leftValuePinYin=DataReader.readString(reader);
                
                DataReader.seek(reader,right*RECORDSIZE);
                String rightValue=DataReader.readString(reader);
                DataReader.seek(reader,right*RECORDSIZE+8);
                String rightValuePinYin=DataReader.readString(reader);
                
                if(leftValue.length()>queryValue.length())
                    leftValue=leftValue.substring(0,queryValue.length());
                if(middleValue.length()>queryValue.length())
                    middleValue=middleValue.substring(0,queryValue.length());
                if(rightValue.length()>queryValue.length())
                    rightValue=rightValue.substring(0,queryValue.length());
                
                if(queryValue.compareTo(middleValue)==0) {
                    staticFirstLetter=middleValuePinYin;
                    return middle;
                } else if (queryValue.compareTo(middleValue)>0) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            }
            
        }
        staticFirstLetter=queryValue;
        return -1;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get pinyin code for given chinese string. the pinyin code is consists with
     * first letter of pinyin for each Chinese character.
     * @param chinese the Chinese string.
     * @return pinyin code.
     */
    public String getPinyinCode(String chinese){
        String ret="";
        try{
            for(int i=0;i<chinese.length();i++) {
                String keyValue=chinese.substring(i,i+1);
                binarySearch(keyValue);
                ret+=firstLetter;
            }
            
        }catch(IOException e) {
        }
        
        return ret;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get pinyin code for given chinese string. the pinyin code is consists with
     * first letter of pinyin for each Chinese character.
     * @param chinese the Chinese string.
     * @return pinyin code.
     */
    public static String getPinyinCode(String chinese,DataInputStream reader){
        String ret="";
        try{
            for(int i=0;i<chinese.length();i++) {
                String keyValue=chinese.substring(i,i+1);
                binarySearch(keyValue,reader);
                ret+=staticFirstLetter;
            }
            
        }catch(IOException e) {
        }
        
        return ret;
    }
    
    private static String getPinYinAtPosition(int chineseID,String queryValue,
            DataInputStream reader) throws IOException{
        DataReader.seek(reader,chineseID*RECORDSIZE);
        String retValue=DataReader.readString(reader);
        DataReader.seek(reader,chineseID*RECORDSIZE+8);
        String retValuePinYin=DataReader.readString(reader);
        if(retValue.compareTo(queryValue)==0){
            return retValuePinYin;
        }
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get pinyin code for given chinese string. the pinyin code is consists with
     * first letter of pinyin for each Chinese character.
     * @param chinese the Chinese string.
     * @return pinyin code.
     */
    public static Vector []getPinyinCodes(String chinese,DataInputStream reader){
        int strLen=chinese.length();
        Vector [] pinyinCodes=new Vector[strLen];
        try{
            for(int i=0;i<strLen;i++) {
                pinyinCodes[i]=new Vector();
                String keyValue=chinese.substring(i,i+1);
                int chineseID=binarySearch(keyValue,reader);
                if(chineseID!=-1){
                    int checkID=chineseID;
                    String pinyin=getPinYinAtPosition(checkID,keyValue,reader);
                    while(pinyin!=null){
                        pinyinCodes[i].addElement(pinyin);
                        checkID++;
                        pinyin=getPinYinAtPosition(checkID,keyValue,reader);
                    }
                    checkID=chineseID-1;
                    pinyin=getPinYinAtPosition(checkID,keyValue,reader);
                    while(pinyin!=null){
                        pinyinCodes[i].addElement(pinyin);
                        checkID--;
                        pinyin=getPinYinAtPosition(checkID,keyValue,reader);
                    } 
                    
                }else{
                    pinyinCodes[i].addElement(keyValue);
                }
            }
            
        }catch(IOException e) {
        }
        
        return pinyinCodes;
    }
}
