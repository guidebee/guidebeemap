//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 08NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 08NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>NoninvertibleTransformException</code> class represents
 * an exception that is thrown if an operation is performed requiring
 * the inverse of an {@link AffineTransform} object but the 
 * <code>AffineTransform</code> is in a non-invertible state.
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 08/11/08
 * @author      Guidebee Pty Ltd.
 */
public class NoninvertibleTransformException extends java.lang.Exception {
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 08NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs an instance of
     * <code>NoninvertibleTransformException</code>
     * with the specified detail message.
     * @param   s     the detail message
     */
    public NoninvertibleTransformException(String s) {
        super (s);
    }
}
