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
 * Internal node of the RTree. Used to access Leaf nodes, where real data lies.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class Index extends AbstractNode {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Retrieves the <B>i-th</B> child node. Loads one page into main memory.
     *
     * @param  i The index of the child in the data array.
     * @return The i-th child.
     */
    public AbstractNode getChild(int i) {
        if (i < 0 || i >= usedSpace) {
            throw new IndexOutOfBoundsException("" + i);
        }

        return tree.file.readNode(branches[i]);
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
    protected Index(RTree tree, int parent, int pageNumber, int level) {
        super(tree, parent, pageNumber, level);
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
    protected Leaf chooseLeaf(HyperCube h) {
        int i;
        
        switch (tree.getTreeType()) {
            case RTree.RTREE_LINEAR:
            case RTree.RTREE_QUADRATIC:
            case RTree.RTREE_EXPONENTIAL:
                i = findLeastEnlargement(h);
                break;
            case RTree.RSTAR:
                if (level == 1) {
                    // if this node points to leaves...
                    i = findLeastOverlap(h);
                } else {
                    i = findLeastEnlargement(h);
                }
                break;
            default:
                throw new IllegalStateException("Invalid tree type.");
        }
        
        return ((AbstractNode) getChild(i)).chooseLeaf(h);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add the new HyperCube to all mbbs present in this node. Calculate the 
     * area difference and choose the entry with the least enlargement. Based 
     * on that metric we choose the path that leads to the leaf that will 
     * hold the new HyperCube.
     * [A. Guttman 'R-trees a dynamic index structure for spatial searching']
     *
     * @return The index of the branch of the path that leads to the Leaf where 
     * the new HyperCube should be inserted.
     */
    private int findLeastEnlargement(HyperCube h) {
        double area = Double.POSITIVE_INFINITY;
        int sel = -1;
        
        for (int i = 0; i < usedSpace; i++) {
            double enl = data[i].getUnionMbb(h).getArea() - data[i].getArea();
            if (enl < area) {
                area = enl;
                sel = i;
            } else if (enl == area) {
                sel = (data[sel].getArea() <= data[i].getArea()) ? sel : i;
            }
        }
        return sel;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * R*-tree criterion for choosing the best branch to follow.
     * [Beckmann, Kriegel, Schneider, Seeger 'The R*-tree: An efficient and 
     * Robust Access Method for Points and Rectangles]
     *
     * @return The index of the branch of the path that leads to the Leaf where 
     * the new HyperCube should be inserted.
     */
    private int findLeastOverlap(HyperCube h) {
        float overlap = Float.POSITIVE_INFINITY;
        int sel = -1;
        
        for (int i = 0; i < usedSpace; i++) {
            AbstractNode n = (AbstractNode) getChild(i);
            float o = 0;
            for (int j = 0; j < n.data.length; j++) {
                o += h.intersectingArea(n.data[j]);
            }
            if (o < overlap) {
                overlap = o;
                sel = i;
            } else if (o == overlap) {
                double area1 = data[i].getUnionMbb(h).getArea() 
                    - data[i].getArea();
                double area2 = data[sel].getUnionMbb(h).getArea()
                    - data[sel].getArea();
                
                if (area1 == area2) {
                    sel = (data[sel].getArea() <= data[i].getArea()) ? sel : i;
                } else {
                    sel = (area1 < area2) ? i : sel;
                }
            }
        }
        return sel;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * findLeaf returns the leaf that contains the given hypercube, null if the
     * hypercube is not contained in any of the leaves of this node.
     * @param h The HyperCube to search for.
     * @return The leaf where the HyperCube is contained, null if such a leaf 
     * is not found.
     */
    protected Leaf findLeaf(HyperCube h) {
        for (int i = 0; i < usedSpace; i++) {
            if (data[i].enclosure(h)) {
                Leaf l = ((AbstractNode) getChild(i)).findLeaf(h);
                if (l != null) {
                    return l;
                }
            }
        }
        
        return null;
    }
    
    
}
