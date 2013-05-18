//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09SEP2010  James Shen                 	      Code Review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.rms;

//--------------------------------- IMPORTS ------------------------------------
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	          Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * A class representing a record store. A record store consists of a collection
 * of records which will remain persistent across multiple invocations of the
 * MIDlet. The platform is responsible for making its best effort to maintain
 * the integrity of the MIDlet's record stores throughout the normal use of the
 * platform, including reboots, battery changes, etc.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee, Inc. </b>
 * @version     1.00, 09/09/10
 * @author      Guidebee, Inc.
 */
public class RecordStore {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param recordStoreName
     * @throws RecordStoreException
     * @throws RecordStoreNotFoundException
     */
    public static void deleteRecordStore(String recordStoreName)
            throws RecordStoreException, RecordStoreNotFoundException {
        RecordStore recordStore = new RecordStore();
        recordStore._indexFileName = recordStore._internalPathPrefix + recordStoreName + ".idx";
        recordStore._dataFileName = recordStore._internalPathPrefix + recordStoreName + ".dat";
        recordStore._indexFile = new File(recordStore._indexFileName);
        recordStore._dataFile = new File(recordStore._dataFileName);
        if (!recordStore._indexFile.exists() || !recordStore._dataFile.exists()) {
            throw new RecordStoreNotFoundException("Record store not found");
        }
        try {
            recordStore._indexFile.delete();
            recordStore._dataFile.delete();

        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param recordStoreName
     * @param createIfNecessary
     * @return
     * @throws RecordStoreException
     * @throws RecordStoreFullException
     * @throws RecordStoreNotFoundException
     */
    public static RecordStore openRecordStore(String recordStoreName, 
            boolean createIfNecessary)
            throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
        RecordStore recordStore = new RecordStore();
        recordStore._indexFileName = recordStore._internalPathPrefix + recordStoreName + ".idx";
        recordStore._dataFileName = recordStore._internalPathPrefix + recordStoreName + ".dat";
        recordStore._indexFile = new File(recordStore._indexFileName);
        recordStore._dataFile = new File(recordStore._dataFileName);
        if (!createIfNecessary) {
            if (!recordStore._indexFile.exists() || !recordStore._dataFile.exists()) {
                throw new RecordStoreNotFoundException("Record store not found");
            }
        }
        try {
            recordStore._indexFileStream = new RandomAccessFile(recordStore._indexFileName, "rw");
            recordStore._dataFileStream = new RandomAccessFile(recordStore._dataFileName, "rw");
            return recordStore;
        } catch (IOException e) {
            throw new RecordStoreException(e.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @throws RecordStoreNotOpenException
     * @throws RecordStoreException
     */
    public void closeRecordStore()
            throws RecordStoreNotOpenException, RecordStoreException {
        try {
            if (_indexFileStream != null) {
                _indexFileStream.close();
                _indexFileStream = null;
            }
            if (_dataFileStream != null) {
                _dataFileStream.close();
                _dataFileStream = null;
            }

        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @return
     * @throws RecordStoreNotOpenException
     */
    public int getNumRecords() throws RecordStoreNotOpenException {
        if (_indexFileStream == null) {
            throw new RecordStoreNotOpenException("not open");
        }
        try {
            long length = _indexFileStream.length();
            return (int) length / (8);
        } catch (Exception e) {
        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @return
     * @throws RecordStoreNotOpenException
     */
    public int getSize() throws RecordStoreNotOpenException {
        if (_indexFileStream == null || _dataFileStream == null) {
            throw new RecordStoreNotOpenException("not open");
        }
        try {
            return (int) (_indexFileStream.length() + _dataFileStream.length());
        } catch (IOException ex) {
        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @return
     * @throws RecordStoreNotOpenException
     * @throws RecordStoreException
     */
    public int getNextRecordID()
            throws RecordStoreNotOpenException, RecordStoreException {
        return getNumRecords() + 1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @throws RecordStoreNotOpenException
     * @throws RecordStoreException
     */
    private void readAllRecords()
            throws RecordStoreNotOpenException, RecordStoreException {
        try {
            if (_indexFileStream == null || _dataFileStream == null) {
                throw new RecordStoreNotOpenException("not open");
            }
            _indexFileStream.seek(0);
            _recordDataIndex.clear();
            {
                RecordData recordData = new RecordData();
                recordData.RecordID = _indexFileStream.readInt();
                recordData.BlockSize = _indexFileStream.readInt();
                recordData.DataOffset = _indexFileStream.readInt();
                recordData.DataLength = _indexFileStream.readInt();
                _recordDataIndex.add(recordData);
            }
            for (int i = 0; i < _recordDataIndex.size(); i++) {
                RecordData list = _recordDataIndex.elementAt(i);
                _dataFileStream.seek(list.DataOffset);
                list.Data = new byte[list.BlockSize];
                _dataFileStream.read(list.Data);
            }
        } catch (IOException ex) {
            throw new RecordStoreException(ex.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param data
     * @param offset
     * @param numBytes
     * @return
     * @throws RecordStoreNotOpenException
     * @throws RecordStoreException
     * @throws RecordStoreFullException
     */
    public int addRecord(byte[] data, int offset, int numBytes)
            throws RecordStoreNotOpenException, RecordStoreException,
            RecordStoreFullException {
        try {
            int blockSize = numBytes / BLOCKSIZE;
            if (blockSize * BLOCKSIZE < numBytes) {
                blockSize += 1;
            }
            RecordData recordData = new RecordData();
            recordData.RecordID = getNumRecords() + 1;
            recordData.Data = new byte[blockSize * BLOCKSIZE];
            recordData.BlockSize = recordData.Data.length;
            recordData.DataOffset = (int) _dataFileStream.length();
            recordData.DataLength = numBytes;
            System.arraycopy(data, offset, recordData.Data, 0, numBytes);
            if (_indexFileStream == null || _dataFileStream == null) {
                throw new RecordStoreNotOpenException("not open");
            }
            _indexFileStream.seek((recordData.RecordID - 1) * 4 * 2);
            {
                _indexFileStream.writeInt(recordData.RecordID);
                _indexFileStream.writeInt(recordData.BlockSize);
                _indexFileStream.writeInt(recordData.DataOffset);
                _indexFileStream.writeInt(recordData.DataLength);
            }
            _dataFileStream.seek(_dataFileStream.length());
            {
                _dataFileStream.write(recordData.Data);
            }
            return recordData.RecordID;
        } catch (IOException ex) {
            throw new RecordStoreException(ex.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * delete record with given record id
     * @param recordId record id to be deleted.
     * @throws RecordStoreNotOpenException
     * @throws InvalidRecordIDException
     * @throws RecordStoreException
     */
    public void deleteRecord(int recordId)
            throws RecordStoreNotOpenException, InvalidRecordIDException,
            RecordStoreException {
        if (recordId > getNumRecords() || recordId < 1) {
            throw new InvalidRecordIDException("Record ID is out of range");
        }
        readAllRecords();
        try {
            _recordDataIndex.remove(recordId - 1);
            _indexFileStream.setLength(0);
            for (int i = 0; i < _recordDataIndex.size(); i++) {
                RecordData recordData = _recordDataIndex.elementAt(i);
                _indexFileStream.writeInt(recordData.RecordID);
                _indexFileStream.writeInt(recordData.BlockSize);
                _indexFileStream.writeInt(recordData.DataOffset);
                _indexFileStream.writeInt(recordData.DataLength);
            }
            _dataFileStream.setLength(0);
            _dataFileStream.seek(0);
            for (int i = 0; i < _recordDataIndex.size(); i++) {
                RecordData recordData = _recordDataIndex.elementAt(i);
                _dataFileStream.write(recordData.Data);
            }


        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the record size of the given record
     * @param recordId id of the record
     * @return
     * @throws RecordStoreNotOpenException
     * @throws InvalidRecordIDException
     * @throws RecordStoreException
     */
    public int getRecordSize(int recordId)
            throws RecordStoreNotOpenException, InvalidRecordIDException,
            RecordStoreException {
        if (recordId > getNumRecords() || recordId < 1) {
            throw new InvalidRecordIDException("Record ID is out of range");
        }
        if (_indexFileStream == null || _dataFileStream == null) {
            throw new RecordStoreNotOpenException("not open");
        }
        try {
            _indexFileStream.seek((recordId - 1) * 4 * 2);

            {
                RecordData recordData = new RecordData();
                recordData.RecordID = _indexFileStream.readInt();
                recordData.BlockSize = _indexFileStream.readInt();
                recordData.DataOffset = _indexFileStream.readInt();
                recordData.DataLength = _indexFileStream.readInt();
                return recordData.DataLength;
            }
        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the data of given record
     * @param recordId id of the record
     * @param buffer byte buffer to copy to.
     * @param offset start index of the buffer
     * @return
     * @throws RecordStoreNotOpenException
     * @throws InvalidRecordIDException
     * @throws RecordStoreException
     */
    public int getRecord(int recordId, byte[] buffer, int offset)
            throws RecordStoreNotOpenException, InvalidRecordIDException,
            RecordStoreException {

        if (recordId > getNumRecords() || recordId < 1) {
            throw new InvalidRecordIDException("Record ID is out of range");
        }
        if (_indexFileStream == null || _dataFileStream == null) {
            throw new RecordStoreNotOpenException("not open");
        }
        try {
            _indexFileStream.seek((recordId - 1) * 4 * 2);
            RecordData recordData = new RecordData();

            {

                recordData.RecordID = _indexFileStream.readInt();
                recordData.BlockSize = _indexFileStream.readInt();
                recordData.DataOffset = _indexFileStream.readInt();
                recordData.DataLength = _indexFileStream.readInt();
            }
            _dataFileStream.seek(recordData.DataOffset);

            {
                byte[] tempBuffer = new byte[recordData.BlockSize];
                _dataFileStream.read(tempBuffer);
                System.arraycopy(tempBuffer, 0, buffer, 0, recordData.DataLength);

            }
            return recordData.DataLength;
        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the data of given record.
     * @param recordId
     * @return
     * @throws RecordStoreNotOpenException
     * @throws InvalidRecordIDException
     * @throws RecordStoreException
     */
    public byte[] getRecord(int recordId) throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {
        if (recordId > getNumRecords() || recordId < 1) {
            throw new InvalidRecordIDException("Record ID is out of range");
        }
        if (_indexFileStream == null || _dataFileStream == null) {
            throw new RecordStoreNotOpenException("not open");
        }
        try {
            _indexFileStream.seek((recordId - 1) * 4 * 2);
            RecordData recordData = new RecordData();

            {

                recordData.RecordID = _indexFileStream.readInt();
                recordData.BlockSize = _indexFileStream.readInt();
                recordData.DataOffset = _indexFileStream.readInt();
                recordData.DataLength = _indexFileStream.readInt();
            }
            _dataFileStream.seek(recordData.DataOffset);

            {

                byte[] tempBuffer = new byte[recordData.BlockSize];
                _dataFileStream.read(tempBuffer);

                return tempBuffer;

            }

        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set a new value for given record.
     * @param recordId
     * @param newData
     * @param offset
     * @param numBytes
     * @throws RecordStoreNotOpenException
     * @throws InvalidRecordIDException
     * @throws RecordStoreException
     * @throws RecordStoreFullException
     */
    public void setRecord(int recordId, byte[] newData, int offset, int numBytes)
            throws RecordStoreNotOpenException, InvalidRecordIDException,
            RecordStoreException, RecordStoreFullException {
        if (recordId > getNumRecords() || recordId < 1) {
            throw new InvalidRecordIDException("Record ID is out of range");
        }
        if (_indexFileStream == null || _dataFileStream == null) {
            throw new RecordStoreNotOpenException("not open");
        }
        try {
            _indexFileStream.seek((recordId - 1) * 4 * 2);
            RecordData recordData = new RecordData();

            {

                recordData.RecordID = _indexFileStream.readInt();
                recordData.BlockSize = _indexFileStream.readInt();
                recordData.DataOffset = _indexFileStream.readInt();
                recordData.DataLength = _indexFileStream.readInt();
            }
            if (recordData.BlockSize >= numBytes) {
                _indexFileStream.seek((recordId - 1) * 4 * 2);
                recordData.DataLength = numBytes;
                _indexFileStream.writeInt(recordData.RecordID);
                _indexFileStream.writeInt(recordData.BlockSize);
                _indexFileStream.writeInt(recordData.DataOffset);
                _indexFileStream.writeInt(recordData.DataLength);

                _dataFileStream.seek(recordData.DataOffset);

                {

                    _dataFileStream.write(newData, offset, numBytes);
                }
            } else {
                readAllRecords();
                try {

                    int blockSize = numBytes / BLOCKSIZE;
                    if (blockSize * BLOCKSIZE < numBytes) {
                        blockSize += 1;
                    }
                    _recordDataIndex.elementAt(recordId - 1).Data = new byte[blockSize * BLOCKSIZE];
                    System.arraycopy(newData, offset, _recordDataIndex.elementAt(recordId - 1).Data, 0, numBytes);
                    _recordDataIndex.elementAt(recordId - 1).BlockSize = blockSize * BLOCKSIZE;
                    _recordDataIndex.elementAt(recordId - 1).DataLength = numBytes;
                    for (int i = recordId; i < getNumRecords(); i++) {
                        _recordDataIndex.elementAt(i).DataOffset = _recordDataIndex.elementAt(i - 1).DataOffset + _recordDataIndex.elementAt(i - 1).BlockSize;
                    }


                    _indexFileStream.setLength(0);
                    _indexFileStream.seek(0);

                    for (int i = 0; i < _recordDataIndex.size(); i++) {
                        RecordData list = _recordDataIndex.elementAt(i);
                        _indexFileStream.writeInt(list.RecordID);
                        _indexFileStream.writeInt(list.BlockSize);
                        _indexFileStream.writeInt(list.DataOffset);
                        _indexFileStream.writeInt(list.DataLength);
                    }



                    _dataFileStream.setLength(0);
                    _dataFileStream.seek(0);

                    for (int i = 0; i < _recordDataIndex.size(); i++) {
                        RecordData list = _recordDataIndex.elementAt(i);
                        _dataFileStream.write(list.Data);
                    }


                } catch (Exception e) {
                    throw new RecordStoreException(e.getMessage());
                }

            }

        } catch (Exception e) {
            throw new RecordStoreException(e.getMessage());
        }
    }
    
    private String _internalPathPrefix = "./";
    private int BLOCKSIZE = 256;
    private String _indexFileName;
    private String _dataFileName;
    private RandomAccessFile _indexFileStream = null;
    private RandomAccessFile _dataFileStream = null;
    private File _indexFile;
    private File _dataFile;
    private Vector<RecordData> _recordDataIndex = new Vector<RecordData>();

    private RecordStore() {
        String properties = "java.io.tmpdir";
        _internalPathPrefix = System.getProperty(properties);
        if(_internalPathPrefix.length()>0){
            if(_internalPathPrefix.charAt(_internalPathPrefix.length()-1)!=File.separatorChar){
                _internalPathPrefix+=File.separatorChar;
            }

        }else{
            _internalPathPrefix = "./";
        }
        _internalPathPrefix+="guidebee"+File.separatorChar;

        File directoryName=new File(_internalPathPrefix);
        if(!directoryName.exists()){
           directoryName.mkdir();
        }

    }
}

class RecordData {

    public int RecordID;
    public int BlockSize;
    public int DataLength;
    public int DataOffset;
    public byte[] Data;
}




