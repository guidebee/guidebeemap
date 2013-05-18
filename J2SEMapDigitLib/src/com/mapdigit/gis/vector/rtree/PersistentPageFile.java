//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector.rtree;

//--------------------------------- IMPORTS ------------------------------------
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.mapdigit.util.DataReader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A page file that stores all pages into Persistent storage. It uses a
 * RandomAccessFile to store node data.
 * The format of the page file is as follows. First, a header is writen
 * that stores important information
 * about the RTree. The header format is as shown below:
 * <br>
 * &nbsp;&nbsp;int dimension<br>
 * &nbsp;&nbsp;double fillFactor<br>
 * &nbsp;&nbsp;int nodeCapacity<br>
 * &nbsp;&nbsp;int pageSize<br>
 * &nbsp;&nbsp;int treeType<br>
 * <p>
 * All the pages are stored after the header, with the following format:
 * <br>
 * &nbsp;&nbsp;int parent<br>
 * &nbsp;&nbsp;int level<br>
 * &nbsp;&nbsp;int usedSpace<br>
 * &nbsp;&nbsp;// HyperCubes<br>
 * &nbsp;&nbsp;for (i in usedSpace)<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;for (j in dimension) {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;double p(i)1 [j]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;double p(i)2 [j]<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;int branch<br>
 * &nbsp;&nbsp;}
 * <p>
 * Deleted pages are stored into a Stack. If a new entry is inserted it
 * is placed in the last deleted page.
 * That way the page file does not grow for ever.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class PersistentPageFile extends PageFile {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public PersistentPageFile(DataInputStream reader, long offset,
            long size) throws IOException {
        this.reader = reader;
        this.offset = offset;
        this.size = size;
        if (size >= headerSize) {
            DataReader.seek(reader, offset);
            dimension = DataReader.readInt(reader);
            fillFactor = DataReader.readDouble(reader);
            nodeCapacity = DataReader.readInt(reader);
            pageSize = DataReader.readInt(reader);
            treeType = DataReader.readInt(reader);

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the file.
     */
    public void Close() throws IOException {
        reader.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    public long Size() {
        return size;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    public AbstractNode readNode(int page) throws PageFaultException {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative.");
        }

        try {
            DataReader.seek(reader, offset + headerSize + page * pageSize);

            byte[] b = new byte[pageSize];
            int l = reader.read(b);
            if (-1 == l) {
                throw new PageFaultException("EOF found while trying to read page "
                        + page + ".");
            }

            DataInputStream ds = new DataInputStream(new ByteArrayInputStream(b));

            int parent = DataReader.readInt(ds);
            if (parent == EMPTY_PAGE) {
                throw new PageFaultException("Page " + page + " is empty.");
            }

            int level = DataReader.readInt(ds);
            int usedSpace = DataReader.readInt(ds);

            AbstractNode n;
            if (level != 0) {
                n = new Index(tree, parent, page, level);
            } else {
                n = new Leaf(tree, parent, page);
            }

            n.parent = parent;
            n.level = level;
            n.usedSpace = usedSpace;

            double[] p1 = new double[dimension];
            double[] p2 = new double[dimension];

            for (int i = 0; i < usedSpace; i++) {
                for (int j = 0; j < dimension; j++) {
                    p1[j] = DataReader.readDouble(ds);
                    p2[j] = DataReader.readDouble(ds);
                }

                n.data[i] = new HyperCube(new Point(p1), new Point(p2));
                n.branches[i] = DataReader.readInt(ds);
            }

            return n;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    
    private static int EMPTY_PAGE = -2;
    /**
     * Stores node data into Persistent storage.
     */
    private DataInputStream reader;
    /**
     * Header size calculated using the following formula:
     * headerSize = dimension + fillFactor + nodeCapacity + pageSize + treeType
     */
    private int headerSize = 24;
    private long offset = 0;
    private long size = 0;
}

