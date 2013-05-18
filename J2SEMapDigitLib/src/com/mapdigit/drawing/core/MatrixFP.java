//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 20APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.core;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 20APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * affine matrix in fixed point format.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public class MatrixFP {

    /**
     * Scale X factor.
     */
    public int scaleX = 0; // ScaleX

    /**
     * Scale Y factor.
     */
    public int scaleY = 0; // ScaleY

    /**
     * Rotate/Shear X factor.
     */
    public int rotateX = 0; // RotateSkewX

    /**
     * Rotate/Shear Y factor.
     */
    public int rotateY = 0; // RotateSkewY

    /**
     * Translate X.
     */
    public int translateX = 0; // TranslateX

    /**
     * Translate Y.
     */
    public int translateY = 0; // TranslateY

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public MatrixFP() {
        scaleX = scaleY = SingleFP.ONE;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param ff_sx
     * @param ff_sy
     * @param ff_rx
     * @param ff_ry
     * @param ff_tx
     * @param ff_ty
     */
    public MatrixFP(int ff_sx, int ff_sy, int ff_rx, int ff_ry,
            int ff_tx, int ff_ty) {
        reset(ff_sx, ff_sy, ff_rx, ff_ry, ff_tx, ff_ty);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param m
     */
    public MatrixFP(MatrixFP m) {
        reset(m.scaleX, m.scaleY, m.rotateX, m.rotateY, m.translateX, m.translateY);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset to identity matrix.
     */
    public void reset() {
        reset(SingleFP.ONE, SingleFP.ONE, 0, 0, 0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Identiry matrix.
     * @return
     */
    public static MatrixFP IDENTITY=new MatrixFP(SingleFP.ONE, SingleFP.ONE, 0, 0, 0, 0);
   

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if it's identity matrix.
     * @return
     */
    public boolean isIdentity() {
        return scaleX == SingleFP.ONE && scaleY == SingleFP.ONE && rotateX == 0
                && rotateY == 0 && translateX == 0 && translateY == 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if its invertiable.
     * @return
     */
    public boolean isInvertible() {
        return determinant() != 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the matrix to give value.
     * @param ff_sx
     * @param ff_sy
     * @param ff_rx
     * @param ff_ry
     * @param ff_tx
     * @param ff_ty
     */
    public void reset(int ff_sx, int ff_sy, int ff_rx, int ff_ry,
            int ff_tx, int ff_ty) {
        this.scaleX = ff_sx;
        this.scaleY = ff_sy;
        this.rotateX = ff_rx;
        this.rotateY = ff_ry;
        this.translateX = ff_tx;
        this.translateY = ff_ty;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * rotate operation.
     * @param ff_ang
     * @return
     */
    public MatrixFP rotate(int ff_ang) {
        int ff_sin = MathFP.sin(ff_ang);
        int ff_cos = MathFP.cos(ff_ang);
        return multiply(new MatrixFP(ff_cos, ff_cos, ff_sin, -ff_sin, 0, 0));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shear or rotate operation.
     * @param ff_rx
     * @param ff_ry
     * @return
     */
    public MatrixFP rotateSkew(int ff_rx, int ff_ry) {
        return multiply(new MatrixFP(SingleFP.ONE, SingleFP.ONE, ff_rx, ff_ry, 0, 0));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * translate operation.
     * @param ff_dx
     * @param ff_dy
     * @return
     */
    public MatrixFP translate(int ff_dx, int ff_dy) {
        this.translateX += ff_dx;
        this.translateY += ff_dy;
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Scale operation.
     * @param ff_sx
     * @param ff_sy
     * @return
     */
    public MatrixFP scale(int ff_sx, int ff_sy) {
        reset(MathFP.mul(ff_sx, this.scaleX), MathFP.mul(ff_sy, this.scaleY),
                MathFP.mul(ff_sy, this.rotateX), MathFP.mul(ff_sx, this.rotateY),
                MathFP.mul(ff_sx, this.translateX), MathFP.mul(ff_sy, this.translateY));
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * multipy with another matrix.
     * @param m
     * @return
     */
    public MatrixFP multiply(MatrixFP m) {
        reset(MathFP.mul(m.scaleX, scaleX) + MathFP.mul(m.rotateY, rotateX),
                MathFP.mul(m.rotateX, rotateY) + MathFP.mul(m.scaleY, scaleY),
                MathFP.mul(m.rotateX, scaleX) + MathFP.mul(m.scaleY, rotateX),
                MathFP.mul(m.scaleX, rotateY) + MathFP.mul(m.rotateY, scaleY),
                MathFP.mul(m.scaleX, translateX) + MathFP.mul(m.rotateY, translateY) + m.translateX,
                MathFP.mul(m.rotateX, translateX) + MathFP.mul(m.scaleY, translateY) + m.translateY);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * compare with another object.
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj instanceof MatrixFP) {
            MatrixFP m = (MatrixFP) obj;
            return m != null && m.rotateX == rotateX && m.rotateY == rotateY
                    && m.scaleX == scaleX && m.scaleY == scaleY && m.translateX == translateX && m.translateY == translateY;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this transform.
     * @return      a hash code for this transform.
     */
    public int hashCode() {
	return rotateX<<24+rotateY<<20+scaleX<<16+scaleY<<8+translateX<<4+translateY;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * calculat the determinat of the matrix.
     * @return
     */
    private int determinant() {
        int ff_det;
        ff_det = MathFP.mul(scaleX, scaleY) - MathFP.mul(rotateX, rotateY);
        return ff_det;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * invert the matrix.
     * @return
     */
    public MatrixFP invert() {
        int ff_det = determinant();
        if (ff_det == 0) {
            reset();
        } else {
            int ff_sx_new = MathFP.div(scaleY, ff_det);
            int ff_sy_new = MathFP.div(scaleX, ff_det);
            int ff_rx_new = -MathFP.div(rotateX, ff_det);
            int ff_ry_new = -MathFP.div(rotateY, ff_det);
            int ff_tx_new = MathFP.div(MathFP.mul(translateY, rotateY)
                    - MathFP.mul(translateX, scaleY), ff_det);
            int ff_ty_new = -MathFP.div(MathFP.mul(translateY, scaleX)
                    - MathFP.mul(translateX, rotateX), ff_det);
            reset(ff_sx_new, ff_sy_new, ff_rx_new, ff_ry_new,
                    ff_tx_new, ff_ty_new);
        }
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * to a string.
     * @return
     */
    public String toString() {
        return " Matrix(sx,sy,rx,ry,tx,ty)=(" + new SingleFP(scaleX)
                + "," + new SingleFP(scaleY) + "," + new SingleFP(rotateX)
                + "," + new SingleFP(rotateY) + "," + new SingleFP(translateX)
                + "," + new SingleFP(translateY) + ")";
    }
}
