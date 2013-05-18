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
package com.mapdigit.gis.vector.rtree;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Abstract class for all classes implementing a storage manager for the RTree.
 * Every node should be stored in a unique page. The root node is always stored
 * in page 0. The storage manager should have the control over the page numbers
 * where each node should be stored.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public abstract class PageFile{
    
    /**
     * related rtree object.
     */
    public RTree tree = null;
    
    /**
     * Dimension of data inserted into the tree.
     */
    public int dimension = -1;
    
    /**
     * fillFactor specifies minimum node entries present in each node.
     * It must be a double between 0 and 0.5.
     */
    public double fillFactor = -1;
    
    /**
     * Maximum node capacity. Each node will be able to hold at most 
     * nodeCapacity entries.
     */
    public int nodeCapacity = -1;
    
    /**
     * The page size needed in bytes to store a full node. Calculated using
     * the following formula:
     * [nodeCapacity * (sizeof(HyperCube) + sizeof(Branch))] + 
     *    parent + level + usedSpace =
     * {nodeCapacity * [(2 * dimension * sizeof(double)) + sizeof(int)]} +
     * sizeof(int) + sizeof(int) + sizeof(int)
     */
    public int pageSize = -1;
    
    /**
     * RTree variant used. Specified when creating a new tree.
     */
    public int treeType = -1;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the node stored in the requested page.
     */
    protected abstract AbstractNode readNode(int page) throws PageFaultException;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    protected void initialize(RTree tree, int dimension, float fillFactor,
            int capacity, int treeType) {
        this.dimension = dimension;
        this.fillFactor = fillFactor;
        this.nodeCapacity = capacity;
        this.treeType = treeType;
        this.tree = tree;
        this.pageSize = capacity * (8 * dimension + 4) + 12;
    }
    
    
}
