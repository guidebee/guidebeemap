//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09MAY2010  James Shen                 	      Code review
/**
 * The <code>Dimension</code> class encapsulates the width and
 * height of a component (in integer=) in a single object.
 * The class is
 * associated with certain properties of components. Several methods
 * defined by the <code>Component</code> class and the
 * <code>LayoutManager</code> interface return a
 * <code>Dimension</code> object.
 * <p>
 * Normally the values of <code>width</code>
 * and <code>height</code> are non-negative integers.
 * The constructors that allow you to create a dimension do
 * not prevent you from setting a negative value for these properties.
 * If the value of <code>width</code> or <code>height</code> is
 * negative, the behavior of some methods defined by other objects is
 * undefined.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 09/05/10
 * @author      Guidebee Pty Ltd.
 */
public class Dimension {

    /**
     * The width dimension; negative values can be used.
     */
    public int width;
    /**
     * The height dimension; negative values can be used.
     */
    public int height;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an instance of <code>Dimension</code> with a width
     * of zero and a height of zero.
     */
    public Dimension() {
        this(0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates an instance of <code>Dimension</code> whose width
     * and height are the same as for the specified dimension.
     *
     * @param    d   the specified dimension for the
     *               <code>width</code> and
     *               <code>height</code> values
     */
    public Dimension(Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a <code>Dimension</code> and initializes
     * it to the specified width and specified height.
     *
     * @param width the specified width
     * @param height the specified height
     */
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the width of this <code>Dimension</code> in double
     * precision.
     * @return the width of this <code>Dimension</code>.
     */
    public int getWidth() {
        return width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the height of this <code>Dimension</code> in double
     * precision.
     * @return the height of this <code>Dimension</code>.
     */
    public int getHeight() {
        return height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the size of this <code>Dimension</code> object to
     * the specified width and height in int precision.
     * Note that if <code>width</code> or <code>height</code>
     * are larger than <code>Integer.MAX_VALUE</code>, they will
     * be reset to <code>Integer.MAX_VALUE</code>.
     *
     * @param width  the new width for the <code>Dimension</code> object
     * @param height the new height for the <code>Dimension</code> object
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the size of this <code>Dimension</code> object.
     * This method is included for completeness, to parallel the
     * <code>getSize</code> method defined by <code>Component</code>.
     *
     * @return   the size of this dimension, a new instance of
     *           <code>Dimension</code> with the same width and height
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the size of this <code>Dimension</code> object to the specified size.
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method defined by <code>Component</code>.
     * @param    d  the new size for this <code>Dimension</code> object
     */
    public void setSize(Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Checks whether two dimension objects have equal values.
     * @param obj object to compare
     * @return true,if they have same size.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Dimension) {
            Dimension d = (Dimension) obj;
            return (width == d.width) && (height == d.height);
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the width of the dimension
     *
     * @param width the dimention width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the height of the dimension
     *
     * @param height the dimention height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hash code for this <code>Dimension</code>.
     *
     * @return    a hash code for this <code>Dimension</code>
     */
    public int hashCode() {
        int sum = width + height;
        return (int) (sum * (sum + 1) / 2 + width);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string representation of the values of this
     * <code>Dimension</code> object's <code>height</code> and
     * <code>width</code> fields. This method is intended to be used only
     * for debugging purposes, and the content and format of the returned
     * string may vary between implementations. The returned string may be
     * empty but may not be <code>null</code>.
     *
     * @return  a string representation of this <code>Dimension</code>
     *          object
     */
    public String toString() {
        return "SIZE [width=" + width + ",height=" + height + "]";
    }
}
