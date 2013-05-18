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
 * A Leaf node. Containts pointers to the real data.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class Leaf extends AbstractNode {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the pointer of the <i>i-th</i> data entry.
     *
     * @param  i The index of the child in the data array.
     * @return The pointer of the <i>i-th</i> child.
     */
    public int getDataPointer(int i) {
        if (i < 0 || i >= usedSpace) {
            throw new IndexOutOfBoundsException("" + i);
        }

        return branches[i];
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
    protected Leaf(RTree tree, int parent, int pageNumber) {
        // Leaf nodes belong by default to level 0.
        super(tree, parent, pageNumber, 0);
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
    protected Leaf(RTree tree, int parent) {
        // Leaf nodes belong by default to level 0.
        super(tree, parent, -1, 0);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * chooseLeaf.
     */
    protected Leaf chooseLeaf(HyperCube h) {
        return this;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * findLeaf.
     */
    protected Leaf findLeaf(HyperCube h) {
        for (int i = 0; i < usedSpace; i++) {
            if (data[i].enclosure(h)) {
                return this;
            }
        }
        
        return null;
    }
    
}
