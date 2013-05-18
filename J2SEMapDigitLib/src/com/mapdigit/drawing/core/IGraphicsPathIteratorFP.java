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
 * The IPathIterator interface provides the mechanism for objects that
 *  return the geometry of their boundary by allowing a caller to retrieve the
 * path of that boundary a segment at a time.
 * This class cannot be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
interface IGraphicsPathIteratorFP {

    void begin();

    void end();

    void moveTo(PointFP point);

    void lineTo(PointFP point);

    void quadTo(PointFP control, PointFP point);

    void curveTo(PointFP control1, PointFP control2, PointFP point);

    void close();
}
