//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.drawing.geometry.AffineTransform;
import com.mapdigit.drawing.geometry.Point;
import com.mapdigit.drawing.geometry.Rectangle;
import com.mapdigit.drawing.core.MatrixFP;
import com.mapdigit.drawing.core.PointFP;
import com.mapdigit.drawing.core.RectangleFP;
import com.mapdigit.drawing.core.SingleFP;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * utility functions to convert class from drawing to drawingfp.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
abstract class Utils {


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from Affline Matrix to it's FP matrix
     * @param matrix
     * @return
     */
    static MatrixFP toMatrixFP(AffineTransform matrix) {
        if (matrix == null) {
            return null;
        }
        if (matrix.isIdentity()) {
            return MatrixFP.IDENTITY;
        }

        MatrixFP matrixFP = new MatrixFP(SingleFP.fromDouble(matrix.getScaleX()),
                SingleFP.fromDouble(matrix.getScaleY()),
                SingleFP.fromDouble(-matrix.getShearX()),
                SingleFP.fromDouble(-matrix.getShearY()),
                SingleFP.fromDouble(matrix.getTranslateX()),
                SingleFP.fromDouble(matrix.getTranslateY()));
        return matrixFP;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from FP matrix to affine matrix
     * @param matrixFP
     * @return
     */
    static AffineTransform toMatrix(MatrixFP matrixFP) {
        if (matrixFP == null) {
            return null;
        }
        if (matrixFP.isIdentity()) {
            return new AffineTransform();
        }
        AffineTransform matrix = new AffineTransform(SingleFP.toDouble(matrixFP.scaleX),
                SingleFP.toDouble(-matrixFP.rotateX),
                SingleFP.toDouble(-matrixFP.rotateY),
                SingleFP.toDouble(matrixFP.scaleY),
                SingleFP.toDouble(matrixFP.translateX),
                SingleFP.toDouble(matrixFP.translateY));
        return matrix;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from rectange to rectangeFP
     * @param rect
     * @return
     */
    static RectangleFP toRectangleFP(Rectangle rect) {
        return new RectangleFP(
                SingleFP.fromInt(rect.getMinX()),
                SingleFP.fromInt(rect.getMinY()),
                SingleFP.fromInt(rect.getMaxX()),
                SingleFP.fromInt(rect.getMaxY()));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from point to pointFP
     * @param pnt
     * @return
     */
    static PointFP toPointFP(Point pnt) {
        return new PointFP(SingleFP.fromInt(pnt.x), SingleFP.fromInt(pnt.y));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from PointFP to Point
     * @param pnt
     * @return
     */
    static Point toPoint(PointFP pnt) {
        return new Point(SingleFP.toInt(pnt.x), SingleFP.toInt(pnt.y));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from Point array to pintFP array
     * @param pnts
     * @return
     */
    static PointFP[] toPointFPArray(Point[] pnts) {
        PointFP[] result = new PointFP[pnts.length];
        for (int i = 0; i < pnts.length; i++) {
            result[i] = toPointFP(pnts[i]);
        }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * from PointFP array to point array
     * @param pnts
     * @return
     */
    static Point[] ToPointArray(PointFP[] pnts) {
        Point[] result = new Point[pnts.length];
        for (int i = 0; i < pnts.length; i++) {
            result[i] = toPoint(pnts[i]);
        }
        return result;
    }
}
