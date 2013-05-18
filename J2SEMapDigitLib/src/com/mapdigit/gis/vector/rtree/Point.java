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
 * A point in the n-dimensional space. All dimensions are stored in an array of
 * doubles.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00,21/12/08
 * @author      Guidebee Pty Ltd.
 */
public class Point extends Object{
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     */
    public Point(double[] d) {
        if (d == null) throw new
                IllegalArgumentException("Coordinates cannot be null.");
        
        if (d.length < 2) throw new
                IllegalArgumentException
                ("Point dimension should be greater than 1.");
        
        data = new double[d.length];
        System.arraycopy(d, 0, data, 0, d.length);
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
    public Point(int[] d) {
        if (d == null) throw new
                IllegalArgumentException("Coordinates cannot be null.");
        
        if (d.length < 2) throw new
                IllegalArgumentException
                ("Point dimension should be greater than 1.");
        
        data = new double[d.length];
        
        for (int i = 0; i < d.length; i++) {
            data[i] = (double) d[i];
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * getdoubleCoordinate.
     */
    public double getFloatCoordinate(int i) {
        return data[i];
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * getIntCoordinate.
     */
    public int getIntCoordinate(int i) {
        return (int) data[i];
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    /**
     * getDimension.
     */
    public int getDimension() {
        return data.length;
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
    public boolean equals(Point p) {
        if (p.getDimension() != getDimension()) {
            throw new IllegalArgumentException("Points must be of equal dimensions to be compared.");
        }
        
        boolean ret = true;
        for (int i = 0; i < getDimension(); i++) {
            if (getFloatCoordinate(i) != p.getFloatCoordinate(i)) {
                ret = false;
                break;
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
     * Clone.
     */
    public Object clone() {
        double[] f = new double[data.length];
        System.arraycopy(data, 0, f, 0, data.length);
        
        return new Point(f);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21DEC2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * toString.
     */
    public String toString() {
        String s = "(" + data[0];
        
        for (int i = 1; i < data.length; i++) {
            s += ", " + data[i];
        }
        
        return s + ")";
    }


    /**
     * dimensions data.
     */
    private double[] data;

    
}
