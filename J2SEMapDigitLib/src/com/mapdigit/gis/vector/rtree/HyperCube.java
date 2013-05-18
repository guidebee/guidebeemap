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

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21DEC2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A HyperCube in the n-dimensional space. It is represented by two points of
 * n dimensions each.Implements basic calculation functions, like <B>getArea()</B>
 * and <B>getUnionMbb()</B>.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class HyperCube  {
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public HyperCube(Point p1, Point p2) {
        boolean bSwitch=false;
        if (p1 == null || p2 == null) {
            throw new
                    IllegalArgumentException("Points cannot be null.");
        }
        
        if (p1.getDimension() != p2.getDimension()) {
            throw new
             IllegalArgumentException("Points must be of the same dimension.");
        }
        
        for (int i = 0; i < p1.getDimension(); i++) {
            if (p1.getFloatCoordinate(i) > p2.getFloatCoordinate(i)) {
                bSwitch=true;
                break;
            }
        }
        
        if(!bSwitch) {
            this.p1 = (Point) p1.clone();
            this.p2 = (Point) p2.clone();
        } else {
            this.p1 = (Point) p2.clone();
            this.p2 = (Point) p1.clone();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * getDimension.
     */
    public int getDimension() {
        return p1.getDimension();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * getP1.
     */
    public Point getP1() {
        return (Point) p1.clone();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * getP2.
     */
    public Point getP2() {
        return (Point) p2.clone();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * equals.
     */
    public boolean equals(HyperCube h) {
        if (p1.equals(h.getP1()) && p2.equals(h.getP2())) {
            return true;
        } else {
            return false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * Tests to see whether <B>h</B> has any common points with this HyperCube.
     * If <B>h</B> is inside this object (or vice versa), it returns true.
     *
     * @return True if <B>h</B> and this HyperCube intersect, false otherwise.
     */
    public boolean intersection(HyperCube h) {
        if (h == null) throw new
                IllegalArgumentException("HyperCube cannot be null.");
        
        if (h.getDimension() != getDimension()) throw new
                  IllegalArgumentException
                ("HyperCube dimension is different from current dimension.");
        
        boolean intersect = true;
        for (int i = 0; i < getDimension(); i++) {
            if (p1.getFloatCoordinate(i) > h.p2.getFloatCoordinate(i) ||
                    p2.getFloatCoordinate(i) < h.p1.getFloatCoordinate(i)) {
                intersect = false;
                break;
            }
        }
        return intersect;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests to see whether <B>h</B> is inside this HyperCube.
     * If <B>h</B> is exactly the same shape as this object, it is considered 
     * to be inside.
     *
     * @return True if <B>h</B> is inside, false otherwise.
     */
    public boolean enclosure(HyperCube h) {
        if (h == null) throw new
                IllegalArgumentException("HyperCube cannot be null.");
        
        if (h.getDimension() != getDimension()) throw new
                IllegalArgumentException
                ("HyperCube dimension is different from current dimension.");
        
        boolean inside = true;
        for (int i = 0; i < getDimension(); i++) {
            if (p1.getFloatCoordinate(i) > h.p1.getFloatCoordinate(i) ||
                    p2.getFloatCoordinate(i) < h.p2.getFloatCoordinate(i)) {
                inside = false;
                break;
            }
        }
        
        return inside;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests to see whether <B>p</B> is inside this HyperCube.
     * If <B>p</B> lies on the boundary
     * of the HyperCube, it is considered to be inside the object.
     *
     * @return True if <B>p</B> is inside, false otherwise.
     */
    public boolean enclosure(Point p) {
        if (p == null) throw new
                IllegalArgumentException("Point cannot be null.");
        
        if (p.getDimension() != getDimension()) throw new
                IllegalArgumentException
                ("Point dimension is different from HyperCube dimension.");
        
        return enclosure(new HyperCube(p, p));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the area of the intersecting region between this HyperCube and 
     * the argument.
     *
     * Below, all possible situations are depicted.
     *
     *     -------   -------      ---------   ---------      ------         ------
     *    |2      | |2      |    |2        | |1        |    |2     |       |1     |
     *  --|--     | |     --|--  | ------  | | ------  |  --|------|--   --|------|--
     * |1 |  |    | |    |1 |  | ||1     | | ||2     | | |1 |      |  | |2 |      |  |
     *  --|--     | |     --|--  | ------  | | ------  |  --|------|--   --|------|--
     *     -------   -------      ---------   ---------      ------         ------
     *
     * @param h Given HyperCube.
     * @return Area of intersecting region.
     */
    public double intersectingArea(HyperCube h) {
        if (!intersection(h)) {
            return 0;
        } else {
            double ret = 1;
            for (int i = 0; i < getDimension(); i++) {
                double l1 = p1.getFloatCoordinate(i);
                double h1 = p2.getFloatCoordinate(i);
                double l2 = h.p1.getFloatCoordinate(i);
                double h2 = h.p2.getFloatCoordinate(i);
                
                if (l1 <= l2 && h1 <= h2) {
                    // cube1 left of cube2.
                    ret *= (h1 - l1) - (l2 - l1);
                } else if (l2 <= l1 && h2 <= h1) {
                    // cube1 right of cube2.
                    ret *= (h2 - l2) - (l1 - l2);
                } else if (l2 <= l1 && h1 <= h2) {
                    // cube1 inside cube2.
                    ret *= h1 - l1;
                } else if (l1 <= l2 && h2 <= h1) {
                    // cube1 contains cube2.
                    ret *= h2 - l2;
                } else if (l1 <= l2 && h2 <= h1) {
                    // cube1 crosses cube2.
                    ret *= h2 - l2;
                } else if (l2 <= l1 && h1 <= h2) {
                    // cube1 crossed by cube2.
                    ret *= h1 - l1;
                }
            }
            if (ret <= 0) throw new
                    ArithmeticException("Intersecting area cannot be negative!");
            return ret;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Static impementation. Takes an array of HyperCubes and calculates the 
     * mbb of their union.
     *
     * @param  a The array of HyperCubes.
     * @return The mbb of their union.
     */
    public static HyperCube getUnionMbb(HyperCube[] a) {
        if (a == null || a.length == 0) throw new
                IllegalArgumentException("HyperCube array is empty.");
        
        HyperCube h = (HyperCube) a[0].clone();
        
        for (int i = 1; i < a.length; i++) {
            h = h.getUnionMbb(a[i]);
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
     * Return a new HyperCube representing the mbb of the union of this 
     * HyperCube and <B>h</B>
     *
     * @param  h The HyperCube that we want to union with this HyperCube.
     * @return  A HyperCube representing the mbb of their union.
     */
    public HyperCube getUnionMbb(HyperCube h) {
        if (h == null) throw new
                IllegalArgumentException("HyperCube cannot be null.");
        
        if (h.getDimension() != getDimension()) throw new
                IllegalArgumentException
                ("HyperCubes must be of the same dimension.");
        
        double[] min = new double[getDimension()];
        double[] max = new double[getDimension()];
        
        for (int i = 0; i < getDimension(); i++) {
            min[i] = Math.min(p1.getFloatCoordinate(i),
                    h.p1.getFloatCoordinate(i));
            max[i] = Math.max(p2.getFloatCoordinate(i), 
                    h.p2.getFloatCoordinate(i));
        }
        
        return new HyperCube(new Point(min), new Point(max));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the area of this HyperCube.
     *
     * @return The area as a double.
     */
    public double getArea() {
        double area = 1;
        
        for (int i = 0; i < getDimension(); i++) {
            area *= p2.getFloatCoordinate(i) - p1.getFloatCoordinate(i);
        }
        
        return area;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * The MINDIST criterion as described by Roussopoulos.
     *  FIXME: better description here...
     */
    public double getMinDist(Point p) {
        if (p == null) throw new
                IllegalArgumentException("Point cannot be null.");
        
        if (p.getDimension() != getDimension()) throw new
                IllegalArgumentException
                ("Point dimension is different from HyperCube dimension.");
        
        double ret = 0;
        for (int i = 0; i < getDimension(); i++) {
            double q = p.getFloatCoordinate(i);
            double s = p1.getFloatCoordinate(i);
            double t = p2.getFloatCoordinate(i);
            double r;
            
            if (q < s) r = s;
            else if (q > t) r = t;
            else r = q;
            
            ret += Math.abs(q - r)*Math.abs(q - r);
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
     * Clone
     */
    public Object clone() {
        return new HyperCube((Point) p1.clone(), (Point) p2.clone());
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * toString
     */
    public String toString() {
        return "P1" + p1.toString() + ":P2" + p2.toString();
    }

    /**
     * point 1 .
     */
    private Point p1;

    /**
     * point 2.
     */
    private Point p2;
}
