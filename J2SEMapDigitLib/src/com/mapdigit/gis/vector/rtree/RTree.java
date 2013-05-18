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

//--------------------------------- IMPORTS ------------------------------------
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * To create a new RTree use the first two constructors. You must specify the
 * dimension, the fill factor as a float between 0 and 0.5 (0 to 50% capacity)
 * and the variant of the RTree which is one of:
 * <ul>
 *  <li>RTREE_QUADRATIC</li>
 * </ul>
 * The first constructor creates by default a new memory resident page file.
 * The second constructor takes
 * the page file as an argument. If the given page file is not empty,
 * then all data are deleted.
 * <p>
 * The third constructor initializes the RTree from an already filled page file.
 * Thus, you may store the
 * RTree into a persistent page file and recreate it again at any time.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class RTree {
    
     /**
     * version
     */
    public final String version = "2.009";
    /**
     * date
     */
    public final String date = "December 21st 2008";
    
    /**
     * Page file where data is stored.
     */
    protected PageFile file = null;
    
    /**
     * static identifier used for the parent of the root node.
     */
    public static final int NIL = -1;
    
    /**
     * Available RTree variants
     */
    public static final int RTREE_LINEAR = 0;
    public static final int RTREE_QUADRATIC = 1;
    public static final int RTREE_EXPONENTIAL = 2;
    public static final int RSTAR = 3;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an rtree from an already initialized page file, probably stored
     * into persistent storage.
     */
    public RTree(PageFile file) {
        if (file.tree != null) {
            throw new IllegalArgumentException
                    ("PageFile already in use by another rtree instance.");
        }
        
        if (file.treeType == -1) {
            throw new IllegalArgumentException
                    ("PageFile is empty. Use some other RTree constructor.");
        }
        
        file.tree = this;
        this.file = file;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Retruns the maximun capacity of each Node.
     */
    public int getNodeCapacity() {
        return file.nodeCapacity;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the percentage between 0 and 0.5, used to calculate minimum 
     * number of entries present in each node.
     */
    public double getFillFactor() {
        return file.fillFactor;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the data dimension.
     */
    public int getDimension() {
        return file.dimension;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the page Length.
     */
    public int getPageSize() {
        return file.pageSize;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the level of the root Node, which signifies the level of the 
     * whole tree. Loads one page into main memory.
     */
    public int getTreeLevel() {
        return  file.readNode(0).getLevel();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the RTree variant used.
     */
    public int getTreeType() {
        return file.treeType;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a Vector containing all tree nodes from bottom to top, left to 
     * right.
     * CAUTION: If the tree is not memory resident, all nodes will be loaded 
     * into main memory.
     *
     * @param root The node from which the traverse should begin.
     * @return A Vector containing all Nodes in the correct order.
     */
    public Vector traverseByLevel(AbstractNode root) {
        if (root == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }
        
        Vector ret = new Vector();
        Vector v = traversePostOrder(root);
        
        for (int i = 0; i <= getTreeLevel(); i++) {
            Vector a = new Vector();
            for (int j = 0; j < v.size(); j++) {
                Node n = (Node) v.elementAt(j);
                if (n.getLevel() == i) {
                    a.addElement(n);
                }
            }
            for (int j = 0; j < a.size(); j++) {
                ret.addElement(a.elementAt(j));
            }
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
     * Returns an Enumeration containing all tree nodes from bottom to top, 
     * left to right.
     *
     * @return An Enumeration containing all Nodes in the correct order.
     */
    public Enumeration traverseByLevel() {
        class ByLevelEnum implements Enumeration {
            // there is at least one node, the root node.
            private boolean hasNext = true;
            
            private Vector nodes;
            
            private int index = 0;
            
            public ByLevelEnum() {
                AbstractNode root = file.readNode(0);
                nodes = traverseByLevel(root);
            }
            
            public boolean hasMoreElements() {
                return hasNext;
            }
            
            public Object nextElement() {
                if (! hasNext) {
                    throw new NoSuchElementException("traverseByLevel");
                }
                
                Object n = nodes.elementAt(index);
                index++;
                if (index == nodes.size()) {
                    hasNext = false;
                }
                return n;
            }
        }
        
        return new ByLevelEnum();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Post order traverse of tree nodes.
     * CAUTION: If the tree is not memory resident, all nodes will be loaded 
     * into main memory.
     *
     * @param root The node where the traversing should begin.
     * @return A Vector containing all tree nodes in the correct order.
     */
    public Vector traversePostOrder(AbstractNode root) {
        if (root == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }
        
        Vector v = new Vector();
        v.addElement(root);
        
        if (root.isLeaf()) {
        } else {
            for (int i = 0; i < root.usedSpace; i++) {
                Vector a = traversePostOrder(((Index) root).getChild(i));
                for (int j = 0; j < a.size(); j++) {
                    v.addElement(a.elementAt(j));
                }
            }
        }
        return v;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Post order traverse of all tree nodes, begging with root.
     * CAUTION: If the tree is not memory resident, all nodes will be loaded 
     * into main memory.
     *
     * @return An Enumeration containing all tree nodes in the correct order.
     */
    public Enumeration traversePostOrder() {
        class PostOrderEnum implements Enumeration {
            private boolean hasNext = true;
            
            private Vector nodes;
            
            private int index = 0;
            
            public PostOrderEnum() {
                AbstractNode root = file.readNode(0);
                nodes = traversePostOrder(root);
            }
            
            public boolean hasMoreElements() {
                return hasNext;
            }
            
            public Object nextElement() {
                if (! hasNext) {
                    throw new NoSuchElementException("traversePostOrder");
                }
                
                Object n = nodes.elementAt(index);
                index++;
                if (index == nodes.size()) {
                    hasNext = false;
                }
                return n;
            }
        }
        
        return new PostOrderEnum();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Pre order traverse of tree nodes.
     * CAUTION: If the tree is not memory resident, all nodes will be loaded 
     * into main memory.
     *
     * @param root The node where the traversing should begin.
     * @return A Vector containing all tree nodes in the correct order.
     */
    public Vector traversePreOrder(AbstractNode root) {
        if (root == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }
        
        Vector v = new Vector();
        
        if (root.isLeaf()) {
            v.addElement(root);
        } else {
            for (int i = 0; i < root.usedSpace; i++) {
                Vector a = traversePreOrder(((Index) root).getChild(i));
                for (int j = 0; j < a.size(); j++) {
                    v.addElement(a.elementAt(j));
                }
            }
            v.addElement(root);
        }
        return v;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Pre order traverse of all tree nodes, begging with root.
     * CAUTION: If the tree is not memory resident, all nodes will be loaded 
     * into main memory.
     *
     * @return An Enumeration containing all tree nodes in the correct order.
     */
    public Enumeration traversePreOrder() {
        class PreOrderEnum implements Enumeration {
            private boolean hasNext = true;
            
            private Vector nodes;
            
            private int index = 0;
            
            public PreOrderEnum() {
                AbstractNode root = file.readNode(0);
                nodes = traversePreOrder(root);
            }
            
            public boolean hasMoreElements() {
                return hasNext;
            }
            
            public Object nextElement() {
                if (! hasNext) {
                    throw new NoSuchElementException("traversePreOrder");
                }
                
                Object n = nodes.elementAt(index);
                index++;
                if (index == nodes.size()) {
                    hasNext = false;
                }
                return n;
            }
        }
        
        return new PreOrderEnum();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a Vector with all nodes that intersect with the given HyperCube.
     * The nodes are returned in post order traversing
     *
     * @param h The given HyperCube that is tested for overlapping.
     * @param root The node where the search should begin.
     * @return A Vector containing the appropriate nodes in the correct order.
     */
    public Vector intersection(HyperCube h, AbstractNode root) {
        if (h == null || root == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        
        if (h.getDimension() != file.dimension) {
            throw new IllegalArgumentException
                    ("HyperCube dimension different than RTree dimension.");
        }
        
        Vector v = new Vector();
        
        if (root.getNodeMbb().intersection(h)) {
            v.addElement(root);
            
            if (!root.isLeaf()) {
                for (int i = 0; i < root.usedSpace; i++) {
                    if (root.data[i].intersection(h)) {
                        Vector a = intersection(h, ((Index) root).getChild(i));
                        for (int j = 0; j < a.size(); j++) {
                            v.addElement(a.elementAt(j));
                        }
                    }
                }
            }
        }
        return v;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an Enumeration with all nodes present in the tree that intersect 
     * with the given HyperCube. The nodes are returned in post order traversing
     *
     * @param h The given HyperCube that is tested for overlapping.
     * @return An Enumeration containing the appropriate nodes in the correct 
     * order.
     */
    public Enumeration intersection(HyperCube h) {
        class IntersectionEnum implements Enumeration {
            private boolean hasNext = true;
            
            private Vector nodes;
            
            private int index = 0;
            
            public IntersectionEnum(HyperCube hh) {
                nodes = intersection(hh, file.readNode(0));
                if (nodes.isEmpty()) {
                    hasNext = false;
                }
            }
            
            public boolean hasMoreElements() {
                return hasNext;
            }
            
            public Object nextElement() {
                if (! hasNext) {
                    throw new NoSuchElementException("intersection");
                }
                
                Object c = nodes.elementAt(index);
                index++;
                if (index == nodes.size()) {
                    hasNext = false;
                }
                return c;
            }
        }
        
        return new IntersectionEnum(h);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a Vector with all Hypercubes that completely contain HyperCube 
     * <B>h</B>.
     * The HyperCubes are returned in post order traversing, according to the 
     * Nodes where they belong.
     *
     * @param h The given HyperCube.
     * @param root The node where the search should begin.
     * @return A Vector containing the appropriate HyperCubes in the correct 
     * order.
     */
    public Vector enclosure(HyperCube h, AbstractNode root) {
        if (h == null || root == null) throw new
                IllegalArgumentException("Arguments cannot be null.");
        
        if (h.getDimension() != file.dimension) throw new
                IllegalArgumentException("HyperCube dimension " +
                "different than RTree dimension.");
        
        Vector v = new Vector();
        
        if (root.getNodeMbb().enclosure(h)) {
            v.addElement(root);
            
            if (!root.isLeaf()) {
                for (int i = 0; i < root.usedSpace; i++) {
                    if (root.data[i].enclosure(h)) {
                        Vector a = enclosure(h, ((Index) root).getChild(i));
                        for (int j = 0; j < a.size(); j++) {
                            v.addElement(a.elementAt(j));
                        }
                    }
                }
            }
        }
        return v;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an Enumeration with all Hypercubes present in the tree that 
     * contain the given HyperCube. The HyperCubes are returned in post order 
     * traversing, according to the Nodes where they belong.
     *
     * @param h The given HyperCube.
     * @return An Enumeration containing the appropriate HyperCubes in the
     * correct order.
     */
    public Enumeration enclosure(HyperCube h) {
        class ContainEnum implements Enumeration {
            private boolean hasNext = true;
            
            private Vector cubes;
            
            private int index = 0;
            
            public ContainEnum(HyperCube hh) {
                cubes = enclosure(hh, file.readNode(0));
                if (cubes.isEmpty()) {
                    hasNext = false;
                }
            }
            
            public boolean hasMoreElements() {
                return hasNext;
            }
            
            public Object nextElement() {
                if (!hasNext) throw new
                        NoSuchElementException("enclosure");
                
                Object c = cubes.elementAt(index);
                index++;
                if (index == cubes.size()) {
                    hasNext = false;
                }
                return c;
            }
        }
        
        return new ContainEnum(h);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a Vector with all Hypercubes that completely contain point 
     * <B>p</B>.
     * The HyperCubes are returned in post order traversing, according to the
     * Nodes where they belong.
     *
     * @param p The given point.
     * @param root The node where the search should begin.
     * @return A Vector containing the appropriate HyperCubes in the correct 
     * order.
     */
    public Vector enclosure(Point p, AbstractNode root) {
        return enclosure(new HyperCube(p, p), root);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an Enumeration with all Hypercubes present in the tree that 
     * contain the given
     * point. The HyperCubes are returned in post order traversing, according 
     * to the Nodes where they belong.
     *
     * @param p The query point.
     * @return An Enumeration containing the appropriate HyperCubes in the correct order.
     */
    public Enumeration enclosure(Point p) {
        return enclosure(new HyperCube(p, p));
    }
    
}
