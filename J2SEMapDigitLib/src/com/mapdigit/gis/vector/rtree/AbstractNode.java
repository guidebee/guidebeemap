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

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Implements basic functions of Node interface. Also implements splitting
 * functions.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public abstract class AbstractNode implements Node {

    /**
     * Level of this node. Leaves always have a level equal to 0.
     */
    public int level;
    /**
     * Parent of all nodes.
     */
    public RTree tree;
    /**
     * The pageNumber where the parent of this node is stored.
     */
    public int parent;
    /**
     * The pageNumber where this node is stored.
     */
    public int pageNumber;
    /**
     * All node data are stored into this array. It must have a size of
     * <B>nodeCapacity + 1</B> to hold
     * all data plus an overflow HyperCube, when the node must be split.
     */
    public HyperCube[] data;
    /**
     * Holds the pageNumbers containing the children of this node.
     * Always has same size with the data array.
     * If this is a Leaf node, than all branches should point to the real
     * data objects.
     */
    public int[] branches;
    /**
     * How much space is used up into this node. If equal to nodeCapacity
     * then node is full.
     */
    public int usedSpace;

   
    //
    // Node interface.
    //
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the node level. Always zero for leaf nodes.
     *
     * @return Level of node.
     */
    public int getLevel() {
        return level;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  Returns true if this node is the root node.
     */
    public boolean isRoot() {
        return (parent == RTree.NIL);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  Returns true if this node is an Index. Root node is an Index too, 
     *  unless it is a Leaf.
     */
    public boolean isIndex() {
        return (level != 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this node is a Leaf. Root may be a Leaf too.
     */
    public boolean isLeaf() {
        return (level == 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the mbb of all HyperCubes present in this node.
     *
     * @return A new HyperCube object, representing the mbb of this node.
     */
    public HyperCube getNodeMbb() {
        if (usedSpace > 0) {
            HyperCube[] h = new HyperCube[usedSpace];
            System.arraycopy(data, 0, h, 0, usedSpace);
            return HyperCube.getUnionMbb(h);
        } else {
            return new HyperCube(new Point(new double[]{0, 0}),
                    new Point(new double[]{0, 0}));
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a unique id for this node. The page number is unique for every 
     * node.
     *
     * @return A string representing a unique id for this node.
     */
    public String getUniqueId() {
        return Integer.toString(pageNumber);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the parent of this node. If there is a parent, it must be an 
     * Index.If this node is the root, returns null. This function loads one 
     * disk page into main memory.
     */
    public AbstractNode getParent() {
        if (isRoot()) {
            return null;
        } else {
            return tree.file.readNode(parent);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Return a copy of the HyperCubes present in this node.
     * @return An array of HyperCubes containing copies of the original data.
     */
    public HyperCube[] getHyperCubes() {
        HyperCube[] h = new HyperCube[usedSpace];

        for (int i = 0; i < usedSpace; i++) {
            h[i] = (HyperCube) data[i].clone();
        }

        return h;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * to a string.
     */
    public String toString() {
        String s = "< Page: " + pageNumber + ", Level: " 
                + level + ", UsedSpace: " + usedSpace
                + ", Parent: " + parent + " >\n";

        for (int i = 0; i < usedSpace; i++) {
            s += "  " + (i + 1) + ") " + data[i].toString()
                    + " --> " + " page: " + branches[i] + "\n";
        }

        return s;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    protected AbstractNode(RTree tree, int parent, int pageNumber, int level) {
        this.parent = parent;
        this.tree = tree;
        this.pageNumber = pageNumber;
        this.level = level;
        data = new HyperCube[tree.getNodeCapacity() + 1];
        branches = new int[tree.getNodeCapacity() + 1];
        usedSpace = 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * chooseLeaf finds the most appropriate leaf where the given HyperCube 
     * should be stored.
     *
     * @param h The new HyperCube.
     *
     * @return The leaf where the new HyperCube should be inserted.
     */
    protected abstract Leaf chooseLeaf(HyperCube h);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * findLeaf returns the leaf that contains the given hypercube, null if the
     * hypercube is not contained in any of the leaves of this node.
     *
     * @param h The HyperCube to search for.
     *
     * @return The leaf where the HyperCube is contained, null if such a leaf is not found.
     */
    protected abstract Leaf findLeaf(HyperCube h);
} 

