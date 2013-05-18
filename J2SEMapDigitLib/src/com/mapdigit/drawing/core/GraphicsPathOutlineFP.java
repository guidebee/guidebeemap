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
 * Create outline for a given path.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 18/04/10
 * @author      Guidebee Pty Ltd.
 */
class GraphicsPathOutlineFP extends GraphicsPathSketchFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param outline
     * @param lineStyle
     */
    public GraphicsPathOutlineFP(GraphicsPathFP outline,
            PenFP lineStyle) {
        this.outline = outline;
        ff_rad = lineStyle.width / 2;
        startLineCap = lineStyle.startCap;
        endLineCap = lineStyle.endCap;
        lineJoin = lineStyle.lineJoin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void end() {
        finishCurrentSegment();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void moveTo(PointFP point) {
        finishCurrentSegment();
        needDrawStartCap = true;
        closed = false;
        startCapP1 = startCapP2 = null;
        super.moveTo(point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void quadTo(PointFP control, PointFP point) {
        curveBegin(control);
        super.quadTo(control, point);
        curveEnd(control, control, point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void curveTo(PointFP control1, PointFP control2, PointFP point) {
        curveBegin(control1);
        super.curveTo(control1, control2, point);
        curveEnd(control1, control2, point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void close() {
        closed = true;
        if (startCapP1 != null && startCapP2 != null &&
                lastPoint != null && currPoint != null) {
            addLineJoin(startCapP1.equals(currPoint)
                    ? lastPoint : currPoint, startCapP1, startCapP2);
        }
        lineTo(startPoint);
        started = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     */
    public void lineTo(PointFP point) {
        if (point.equals(currPoint)) {
            return;
        }

        LineFP head, tail;
        calcHeadTail(currPoint, point,
                head = new LineFP(), tail = new LineFP());

        if (drawingCurve) {
            if (lastCurveTail != null) {
                curvePath1.addLineTo(lastCurveTail.pt1);
                curvePath2.addLineTo(lastCurveTail.pt2);
            }
            lastCurveTail = new LineFP(tail);
        } else {
            if (needDrawStartCap) {
                startCapP1 = new PointFP(currPoint);
                startCapP2 = new PointFP(point);
                needDrawStartCap = false;
            }
            addLineJoin(lastPoint, currPoint, point);

            outline.addMoveTo(head.pt1);
            outline.addLineTo(tail.pt1);
            outline.addLineTo(tail.pt2);
            outline.addLineTo(head.pt2);
            outline.addLineTo(head.pt1);
            outline.addClose();
            lastPoint = new PointFP(currPoint);
        }
        super.lineTo(point);
    }

    private void finishCurrentSegment() {
        if (closed) {
            return;
        }
        if (startCapP1 != null && startCapP2 != null) {
            addLineCap(startCapP2, startCapP1, startLineCap);
        }
        if (lastPoint != null) {
            addLineCap(lastPoint, currPoint, endLineCap);
        }
    }

    private void addLineCap(PointFP p1, PointFP p2, int lineCap) {
        if (lineCap == PenFP.LINECAP_BUTT || p1.equals(p2)) {
            return;
        }
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        int len = PointFP.distance(dx, dy);
        PointFP[] cap = lineCap == PenFP.LINECAP_ROUND
                ? GraphicsPathFP.ROUNDCAP : GraphicsPathFP.SQUARECAP;

        dx = MathFP.mul(ff_rad, MathFP.div(dx, len));
        dy = MathFP.mul(ff_rad, MathFP.div(dy, len));

        MatrixFP m = new MatrixFP(dx, dx, dy, -dy, p2.x, p2.y);
        outline.addMoveTo(new PointFP(0, GraphicsPathFP.ONE).transform(m));
        for (int i = 0; i < cap.length; i++) {
            outline.addLineTo(new PointFP(cap[i]).transform(m));
        }
        outline.addLineTo(new PointFP(0, -GraphicsPathFP.ONE).transform(m));
        outline.addClose();
    }

    private void calcHeadTail(PointFP p1, PointFP p2, LineFP head,
            LineFP tail) {
        
            LineFP curr = new LineFP(p1, p2);
            head.reset(curr.getHeadOutline(ff_rad));
            int dx = p2.x - p1.x;
            int dy = p2.y - p1.y;
            tail.reset(head.pt1.x + dx, head.pt1.y + dy,
                    head.pt2.x + dx, head.pt2.y + dy);
       
    }

    private void addLineJoin(PointFP lastPoint, PointFP currPoint,
            PointFP nextPoint) {
        if (lastPoint == null || currPoint == null || nextPoint == null
                || nextPoint.equals(currPoint) || lastPoint.equals(currPoint)) {
            return;
        }

        PointFP p1 = null, p2 = null;
        LineFP head, tail, lastHead, lastTail;
        calcHeadTail(currPoint, nextPoint,
                head = new LineFP(), tail = new LineFP());
        calcHeadTail(lastPoint, currPoint,
                lastHead = new LineFP(), lastTail = new LineFP());
        boolean cross1, cross2, needLineJoin = false;
        PointFP pi1 = new PointFP();
        PointFP pi2 = new PointFP();

        cross1 = LineFP.intersects(new LineFP(head.pt1, tail.pt1),
                new LineFP(lastHead.pt1, lastTail.pt1), pi1);
        cross2 = LineFP.intersects(new LineFP(head.pt2, tail.pt2),
                new LineFP(lastHead.pt2, lastTail.pt2), pi2);
        if (cross1 && !cross2 && pi1.x != SingleFP.NaN) {
            p1 = lastTail.pt2;
            p2 = head.pt2;
            needLineJoin = true;
        } else if (!cross1 && cross2 && pi2.x != SingleFP.NaN) {
            p1 = lastTail.pt1;
            p2 = head.pt1;
            needLineJoin = true;
        }
        if (needLineJoin) {
            outline.addMoveTo(cross1 ? pi1 : pi2);
            outline.addLineTo(cross1 ? p2 : p1);
            if (lineJoin == PenFP.LINEJOIN_MITER) {
                outline.addLineTo(cross1 ? pi2 : pi1);
            }
            outline.addLineTo(cross1 ? p1 : p2);
            outline.addClose();
            if (lineJoin == PenFP.LINEJOIN_ROUND) {
                addLineCap(cross2 ? pi2 : pi1, currPoint, PenFP.LINECAP_ROUND);
            }
        }
    }

    private void curveBegin(PointFP control) {
        addLineJoin(lastPoint, currPoint, control);
        drawingCurve = true;
        curvePath1 = new GraphicsPathFP();
        curvePath2 = new GraphicsPathFP();
        curveBegin = new PointFP(currPoint);
    }

    private void curveEnd(PointFP control1, PointFP control2, PointFP curveEnd) {
        drawingCurve = false;
        if (needDrawStartCap) {
            startCapP1 = new PointFP(curveBegin);
            startCapP2 = new PointFP(control1);
            needDrawStartCap = false;
        }
        LineFP head = new LineFP();
        LineFP tail = new LineFP();
        calcHeadTail(curveBegin, control1, head, new LineFP());
        outline.addMoveTo(head.pt1);
        outline.addPath(curvePath1);
        calcHeadTail(control2, curveEnd, new LineFP(), tail);
        outline.addLineTo(tail.pt1);
        outline.addLineTo(tail.pt2);
        outline.extendIfNeeded(curvePath1.cmdsSize, curvePath1.pntsSize);
        int j = curvePath2.pntsSize - 1;
        for (int i = curvePath2.cmdsSize - 1; i >= 0; i--) {
            outline.addLineTo(curvePath2.pnts[j--]);
        }
        outline.addLineTo(head.pt2);
        outline.addClose();
        curvePath1 = null;
        curvePath2 = null;
        lastCurveTail = null;
        lastPoint = new PointFP(control2);
        drawingCurve = false;
    }

    private int ff_rad;
    private int startLineCap;
    private int endLineCap;
    private int lineJoin;
    private boolean needDrawStartCap = false;
    private PointFP lastPoint = null;
    private LineFP lastCurveTail = null;
    private GraphicsPathFP curvePath1 = null;
    private GraphicsPathFP curvePath2 = null;
    private PointFP curveBegin = null;
    private boolean drawingCurve = false;
    private GraphicsPathFP outline;
    private PointFP startCapP1,  startCapP2;
    private boolean closed = true;
}