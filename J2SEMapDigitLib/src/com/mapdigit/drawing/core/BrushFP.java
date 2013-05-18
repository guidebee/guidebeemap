//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 18APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.core;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Defines objects used to fill the interiors of graphical shapes such as
 * rectangles, ellipses, pies, polygons, and paths.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 18/04/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class BrushFP {

    /**
     * Use the terminal colors to fill the remaining area.
     */
    public final static int NO_CYCLE=0;

    /**
     * Cycle the gradient colors start-to-end, end-to-start
     * to fill the remaining area.
     */
    public final static int  REFLECT=1;

    /**
     * Cycle the gradient colors start-to-end, start-to-end
     * to fill the remaining area.
     */
    public final static int  REPEAT= 2;

    /**
     * Fill mode of the brush.
     */
    public int fillMode=REPEAT;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the matrix associated with this brush.
     * @return the matrix.
     */
    public MatrixFP getMatrix() {
        return matrix;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the matrix for this brush.
     * @param value a new matrix.
     */
    public void setMatrix(MatrixFP value) {
        matrix = new MatrixFP(value);
        matrix.invert();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Transform with a new matrix.
     * @param m1 a new matrix.
     */
    public void transform(MatrixFP m1) {
        MatrixFP m = new MatrixFP(m1);
        m.invert();
        if (matrix == null) {
            matrix = m;
        } else {
            matrix.multiply(m);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if it's a mono color brush.
     * @return true if it's mono color brush.
     */
    public abstract boolean isMonoColor();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the color value at given position.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param singlePoint single point or not.
     * @return the color value.
     */
    public abstract int getColorAt(int x, int y, boolean singlePoint);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the next color for this brush.
     * @return the next color.
     */
    public abstract int getNextColor();

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the matrix associated with the graphics object.
     * @return the matrix of the graphics object.
     */
    MatrixFP getGraphicsMatrix() {
        return graphicsMatrix;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the graphics matrix.
     * @param value the graphics matrix.
     */
    void setGraphicsMatrix(MatrixFP value) {
        graphicsMatrix = new MatrixFP(value);
        graphicsMatrix.invert();
        finalMatrix = new MatrixFP(graphicsMatrix);
        if (matrix != null) {
            finalMatrix.multiply(matrix);
        }
    }

    /**
     * Brush matrix.
     */
    protected MatrixFP matrix;

    /**
     * Graphics matrix.
     */
    protected MatrixFP graphicsMatrix;

    /**
     * The combined matrix.
     */
    protected MatrixFP finalMatrix;
}