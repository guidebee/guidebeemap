//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.drawing.geometry.AffineTransform;
import com.mapdigit.drawing.core.BrushFP;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Classes derived from this abstract base class define objects used to fill the
 * interiors of graphical shapes such as rectangles, ellipses, pies, polygons,
 * and paths.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class Brush {

    /**
     * Use the terminal colors to fill the remaining area.
     */
    public final static int NO_CYCLE=BrushFP.NO_CYCLE;

    /**
     * Cycle the gradient colors start-to-end, end-to-start
     * to fill the remaining area.
     */
    public final static int  REFLECT=BrushFP.REFLECT;

    /**
     * Cycle the gradient colors start-to-end, start-to-end
     * to fill the remaining area.
     */
    public final static int  REPEAT= BrushFP.REPEAT;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default private constructor to avoid this class be intantialized.
     */
    protected Brush(){

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the matrix associated with this brush.
     * @return the matrix.
     */
    public AffineTransform getMatrix() {
        return Utils.toMatrix(wrappedBrushFP.getMatrix());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the matrix for this brush.
     * @param value a new matrix.
     */
    public void setMatrix(AffineTransform value) {
        wrappedBrushFP.setMatrix(Utils.toMatrixFP(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Transform with a new matrix.
     * @param m1 a new matrix.
     */
    public void transform(AffineTransform m1) {
        wrappedBrushFP.transform(Utils.toMatrixFP(m1));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the transparency mode for this <code>Color</code>.  This is
     * required to implement the <code>Paint</code> interface.
     * @return this <code>Color</code> object's transparency mode.
     */
    public abstract int getTransparency();

    /**
     * internal wrapped brush in the drawing core package.
     */
    protected BrushFP wrappedBrushFP=null;

}
