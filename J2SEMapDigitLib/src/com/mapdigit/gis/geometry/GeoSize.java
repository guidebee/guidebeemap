//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.geometry;

//--------------------------------- IMPORTS ------------------------------------
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>GeoSize</code> class encapsulates the width and 
 * height of a component (in integer precision) in a single object. 
 *
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public class GeoSize {

    /**
     * The width GeoSize; negative values can be used.
     */
    public double width;
    /**
     * The height GeoSize; negative values can be used.
     */
    public double height;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Creates an instance of <code>GeoSize</code> with a width 
     * of zero and a height of zero. 
     */
    public GeoSize() {
        this(0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Creates an instance of <code>GeoSize</code> whose width  
     * and height are the same as for the specified GeoSize.
     *
     * @param    size   the specified GeoSize for the 
     *               <code>width</code> and 
     *               <code>height</code> values
     */
    public GeoSize(GeoSize size) {
        this(size.width, size.height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Constructs a <code>GeoSize</code> and initializes
     * it to the specified width and specified height.
     *
     * @param width the specified width 
     * @param height the specified height
     */
    public GeoSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the width.
     * @return the width of the geo size.
     */
    public double getWidth() {
        return width;
    }

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the size of this <code>GeoSize</code> object to
     * match the specified size.
     * This method is included for completeness, to parallel the
     * <code>getSize</code> method of <code>Component</code>.
     * @param d  the new size for the <code>GeoSize</code>
     * object
     */
    public void setSize(GeoSize d) {
        setSize(d.getWidth(), d.getHeight());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the height.
     * @return the height of the geo size.
     */
    public double getHeight() {
        return height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the size of this <code>GeoSize</code> object to
     * the specified width and height in double precision.
     * Note that if <code>width</code> or <code>height</code>
     * are larger than <code>Integer.MAX_VALUE</code>, they will
     * be reset to <code>Integer.MAX_VALUE</code>.
     *
     * @param width  the new width for the <code>GeoSize</code> object
     * @param height the new height for the <code>GeoSize</code> object
     */
    public void setSize(double width, double height) {
        this.width = (int) Math.ceil(width);
        this.height = (int) Math.ceil(height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the size of this <code>GeoSize</code> object.
     * This method is included for completeness, to parallel the
     * <code>getSize</code> method defined by <code>Component</code>.
     *
     * @return   the size of this GeoSize, a new instance of
     *           <code>GeoSize</code> with the same width and height
     */
    public GeoSize getSize() {
        return new GeoSize(width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the size of this <code>GeoSize</code> object
     * to the specified width and height.
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method defined by <code>Component</code>.
     *
     * @param    width   the new width for this <code>GeoSize</code> object
     * @param    height  the new height for this <code>GeoSize</code> object
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Checks whether two GeoSize objects have equal values.
     * @param obj the object to compare
     * @return ture if they have same size.
     */
    public boolean equals(Object obj) {
        if (obj instanceof GeoSize) {
            GeoSize d = (GeoSize) obj;
            return (width == d.width) && (height == d.height);
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hash code for this <code>GeoSize</code>.
     *
     * @return    a hash code for this <code>GeoSize</code>
     */
    public int hashCode() {
        double sum = width + height;
        return (int) (sum * (sum + 1) / 2 + width);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string representation of the values of this
     * <code>GeoSize</code> object's <code>height</code> and
     * <code>width</code> fields. This method is intended to be used only
     * for debugging purposes, and the content and format of the returned
     * string may vary between implementations. The returned string may be
     * empty but may not be <code>null</code>.
     *
     * @return  a string representation of this <code>GeoSize</code>
     *          object
     */
    public String toString() {
        return getClass().getName() + "[width=" + width + ",height=" + height + "]";
    }
}
