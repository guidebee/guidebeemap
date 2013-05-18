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

import com.mapdigit.drawing.geometry.Path;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Represents a series of connected lines and curves.
 * This class cannot be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 18/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class GraphicsPathFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public GraphicsPathFP() {
        cmds = null;
        pnts = null;
        cmdsSize = pntsSize = 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param from the one to be copied.
     */
    public GraphicsPathFP(GraphicsPathFP from) {
        cmdsSize = from.cmdsSize;
        pntsSize = from.pntsSize;
        if (cmdsSize > 0) {
            cmds = new int[cmdsSize];
            pnts = new PointFP[pntsSize];
            System.arraycopy(from.cmds, 0, cmds, 0, cmdsSize);
            for (int i = 0; i < pntsSize; i++) {
                pnts[i] = new PointFP(from.pnts[i]);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create the line path from given coordinates.
     * @param ff_x1
     * @param ff_y1
     * @param ff_x2
     * @param ff_y2
     * @return
     */
    public static GraphicsPathFP createLine(int ff_x1, int ff_y1,
            int ff_x2, int ff_y2) {
        GraphicsPathFP path = new GraphicsPathFP();
        path.addMoveTo(new PointFP(ff_x1, ff_y1));
        path.addLineTo(new PointFP(ff_x2, ff_y2));
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create the oval path from given rectangle.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @return
     */
    public static GraphicsPathFP createOval(int ff_xmin, int ff_ymin,
            int ff_xmax, int ff_ymax) {
        GraphicsPathFP path = GraphicsPathFP.createArc(ff_xmin, ff_ymin,
                ff_xmax, ff_ymax, 0, MathFP.PI * 2, false);
        path.addClose();
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create an round rectangle path from given parameter.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_rx
     * @param ff_ry
     * @return
     */
    public static GraphicsPathFP createRoundRect(int ff_xmin, int ff_ymin,
            int ff_xmax, int ff_ymax, int ff_rx, int ff_ry) {
        int ff_rmax;
        int FF_PI = MathFP.PI;
        GraphicsPathFP path = new GraphicsPathFP();
        path.addMoveTo(new PointFP(ff_xmin + ff_rx, ff_ymin));
        path.addLineTo(new PointFP(ff_xmax - ff_rx, ff_ymin));
        ff_rmax = MathFP.min(ff_xmax - ff_xmin, ff_ymax - ff_ymin) / 2;
        if (ff_rx > ff_rmax) {
            ff_rx = ff_rmax;
        }
        if (ff_ry > ff_rmax) {
            ff_ry = ff_rmax;
        }
        if (ff_rx != 0 && ff_ry != 0) {
            path.addPath(GraphicsPathFP.createArc(ff_xmax - ff_rx * 2, 
                    ff_ymin, ff_xmax, ff_ymin + ff_ry * 2,
                    (-FF_PI) / 2, 0, false, false));
        }
        path.addLineTo(new PointFP(ff_xmax, ff_ymin + ff_ry));
        path.addLineTo(new PointFP(ff_xmax, ff_ymax - ff_ry));
        if (ff_rx != 0 && ff_ry != 0) {
            path.addPath(GraphicsPathFP.createArc(ff_xmax - ff_rx * 2, 
                    ff_ymax - ff_ry * 2, ff_xmax, ff_ymax, 0,
                    FF_PI / 2, false, false));
        }
        path.addLineTo(new PointFP(ff_xmax - ff_rx, ff_ymax));
        path.addLineTo(new PointFP(ff_xmin + ff_rx, ff_ymax));
        if (ff_rx != 0 && ff_ry != 0) {
            path.addPath(GraphicsPathFP.createArc(ff_xmin, ff_ymax - ff_ry * 2,
                    ff_xmin + ff_rx * 2, ff_ymax,
                    FF_PI / 2, FF_PI, false, false));
        }
        path.addLineTo(new PointFP(ff_xmin, ff_ymax - ff_ry));
        path.addLineTo(new PointFP(ff_xmin, ff_ymin + ff_ry));
        if (ff_rx != 0 && ff_ry != 0) {
            path.addPath(GraphicsPathFP.createArc(ff_xmin, ff_ymin, 
                    ff_xmin + ff_rx * 2, ff_ymin + ff_ry * 2, -FF_PI,
                    (-FF_PI) / 2, false, false));
        }
        path.addClose();
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a smooth curve from given parameters.
     * @param points
     * @param offset
     * @param numberOfSegments
     * @param ff_factor
     * @param closed
     * @return
     */
    public static GraphicsPathFP createSmoothCurves(PointFP[] points,
            int offset, int numberOfSegments, int ff_factor, boolean closed) {
        int len = points.length;
        GraphicsPathFP path = new GraphicsPathFP();

        if (numberOfSegments < 1 ||
                numberOfSegments > points.length - 1 ||
                offset < 0 ||
                offset + numberOfSegments > len - 1) {
            return path;
        }

        PointFP[] PC1s = new PointFP[points.length];
        PointFP[] PC2s = new PointFP[points.length];
        if (!closed) {
            PC1s[0] = points[0];
            PC2s[len - 1] = points[len - 1];
        } else {
            PC1s[0] = calcControlPoint(points[len - 1],
                    points[0], points[1], ff_factor);
            PC2s[0] = calcControlPoint(points[1], points[0],
                    points[len - 1], ff_factor);
            PC1s[len - 1] = calcControlPoint(points[len - 2], points[len - 1],
                    points[0], ff_factor);
            PC2s[len - 1] = calcControlPoint(points[0], points[len - 1],
                    points[len - 2], ff_factor);
        }
        for (int i = 1; i < len - 1; i++) {
            PC1s[i] = calcControlPoint(points[i - 1], points[i],
                    points[i + 1], ff_factor);
            PC2s[i] = calcControlPoint(points[i + 1], points[i],
                    points[i - 1], ff_factor);
        }

        path.addMoveTo(points[offset]);
        for (int i = 0; i < numberOfSegments; i++) {
            path.addCurveTo(PC1s[offset + i], PC2s[offset + i + 1],
                    points[offset + i + 1]);
        }
        if (closed) {
            path.addCurveTo(PC1s[len - 1], PC2s[0], points[0]);
            path.addClose();
        }
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create a polyline path.
     * @param points
     * @return
     */
    public static GraphicsPathFP createPolyline(PointFP[] points) {
        GraphicsPathFP path = new GraphicsPathFP();
        if (points.length > 0) {
            path.addMoveTo(points[0]);
            for (int i = 1; i < points.length; i++) {
                path.addLineTo(points[i]);
            }
        }
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a polygon path.
     * @param points
     * @return
     */
    public static GraphicsPathFP createPolygon(PointFP[] points) {
        GraphicsPathFP path = createPolyline(points);
        if (points.length > 0) {
            path.addClose();
        }
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a rect path.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @return
     */
    public static GraphicsPathFP createRect(int ff_xmin, int ff_ymin,
            int ff_xmax, int ff_ymax) {
        return createPolygon(
                new PointFP[]{
                    new PointFP(ff_xmin, ff_ymin),
                    new PointFP(ff_xmax, ff_ymin),
                    new PointFP(ff_xmax, ff_ymax),
                    new PointFP(ff_xmin, ff_ymax)
                });
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create arc path.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_arg1
     * @param ff_arg2
     * @param closed
     * @return
     */
    public static GraphicsPathFP createArc(int ff_xmin, int ff_ymin,
            int ff_xmax, int ff_ymax, int ff_arg1, int ff_arg2,
            boolean closed) {
        return createArc(ff_xmin, ff_ymin, ff_xmax, ff_ymax, ff_arg1,
                ff_arg2, closed, true);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create arc path.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_startangle
     * @param ff_sweepangle
     * @param closed
     * @param standalone
     * @return
     */
    public static GraphicsPathFP createArc(int ff_xmin, int ff_ymin,
            int ff_xmax, int ff_ymax, int ff_startangle,
            int ff_sweepangle, boolean closed, boolean standalone) {
        if (ff_sweepangle < 0) {
            ff_startangle += ff_sweepangle;
            ff_sweepangle = -ff_sweepangle;
        }
        int segments = MathFP.round(MathFP.div(4 * MathFP.abs(ff_sweepangle),
                MathFP.PI)) >> SingleFP.DECIMAL_BITS;
        if (segments == 0) {
            segments = 1;
        }
        GraphicsPathFP path = new GraphicsPathFP();
        int ff_darg = ff_sweepangle / segments;
        int ff_arg = ff_startangle;
        int ff_lastcos = MathFP.cos(ff_startangle);
        int ff_lastsin = MathFP.sin(ff_startangle);
        int ff_xc = (ff_xmin + ff_xmax) / 2;
        int ff_yc = (ff_ymin + ff_ymax) / 2;
        int ff_rx = (ff_xmax - ff_xmin) / 2;
        int ff_ry = (ff_ymax - ff_ymin) / 2;
        int ff_RXBETA = MathFP.mul(17381, ff_rx);
        int ff_RYBETA = MathFP.mul(17381, ff_ry);
        int ff_currcos, ff_currsin, ff_x1, ff_y1, ff_x2, ff_y2;

        if (closed) {
            path.addMoveTo(new PointFP(ff_xc, ff_yc));
        }

        for (int i = 1; i <= segments; i++) {
            ff_arg = i == segments ? ff_startangle + ff_sweepangle
                    : ff_arg + ff_darg;
            ff_currcos = MathFP.cos(ff_arg);
            ff_currsin = MathFP.sin(ff_arg);
            ff_x1 = ff_xc + MathFP.mul(ff_rx, ff_lastcos);
            ff_y1 = ff_yc + MathFP.mul(ff_ry, ff_lastsin);
            ff_x2 = ff_xc + MathFP.mul(ff_rx, ff_currcos);
            ff_y2 = ff_yc + MathFP.mul(ff_ry, ff_currsin);
            if (i == 1) {
                if (closed) {
                    path.addLineTo(new PointFP(ff_x1, ff_y1));
                } else if (standalone) {
                    path.addMoveTo(new PointFP(ff_x1, ff_y1));
                }
            }

            path.addCurveTo(
                    new PointFP(ff_x1 - MathFP.mul(ff_RXBETA, ff_lastsin),
                    ff_y1 + MathFP.mul(ff_RYBETA, ff_lastcos)),
                    new PointFP(ff_x2 + MathFP.mul(ff_RXBETA, ff_currsin),
                    ff_y2 - MathFP.mul(ff_RYBETA, ff_currcos)),
                    new PointFP(ff_x2, ff_y2));
            ff_lastcos = ff_currcos;
            ff_lastsin = ff_currsin;
        }
        if (closed) {
            path.addClose();
        }
        return path;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add a path to this path.
     * @param path
     */
    public void addPath(GraphicsPathFP path) {
        if (path.cmdsSize > 0) {
            extendIfNeeded(path.cmdsSize, path.pntsSize);
            System.arraycopy(path.cmds, 0, cmds, cmdsSize, path.cmdsSize);
            for (int i = 0; i < path.pntsSize; i++) {
                pnts[i + pntsSize] = new PointFP(path.pnts[i]);
            }
            cmdsSize += path.cmdsSize;
            pntsSize += path.pntsSize;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * add move to this path.
     * @param point
     */
    public void addMoveTo(PointFP point) {
        extendIfNeeded(1, 1);
        cmds[cmdsSize++] = CMD_MOVETO;
        pnts[pntsSize++] = new PointFP(point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add line to this path.
     * @param point
     */
    public void addLineTo(PointFP point) {
        extendIfNeeded(1, 1);
        cmds[cmdsSize++] = CMD_LINETO;
        pnts[pntsSize++] = new PointFP(point);
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add curve to.
     * @param control
     * @param point
     */
    public void addQuadTo(PointFP control, PointFP point) {
        if (control.equals(point)) {
            addLineTo(point);
            return;
        }
        extendIfNeeded(1, 2);
        cmds[cmdsSize++] = CMD_QCURVETO;
        pnts[pntsSize++] = new PointFP(control);
        pnts[pntsSize++] = new PointFP(point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param control1
     * @param control2
     * @param point
     */
    public void addCurveTo(PointFP control1, PointFP control2, PointFP point) {
        if (pnts[pntsSize - 1].equals(control1)) {
            addQuadTo(control2, point);
            return;
        }
        if (point.equals(control2)) {
            addQuadTo(control1, point);
            return;
        }
        extendIfNeeded(1, 3);
        cmds[cmdsSize++] = CMD_CCURVETO;
        pnts[pntsSize++] = new PointFP(control1);
        pnts[pntsSize++] = new PointFP(control2);
        pnts[pntsSize++] = new PointFP(point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Close the path
     */
    public void addClose() {
        extendIfNeeded(1, 0);
        cmds[cmdsSize++] = CMD_CLOSE;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Calculate outline with given pen.
     * @param lineStyle
     * @return
     */
    public GraphicsPathFP calcOutline(PenFP lineStyle) {
        GraphicsPathFP outline = new GraphicsPathFP();
        GraphicsPathOutlineFP outlineGenerator =
                new GraphicsPathOutlineFP(outline, lineStyle);
        visit(outlineGenerator);
        return outline;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * To path object in drawing package.
     * @return
     */
    public Path toPath() {
        Path path = new Path();
        int j = 0;
        for (int i = 0; i < cmdsSize; i++) {
            switch (cmds[i]) {
                case CMD_NOP:
                    break;
                case CMD_MOVETO: {
                    PointFP pt = pnts[j++];
                    path.moveTo(pt.x >> SingleFP.DECIMAL_BITS,
                            pt.y >> SingleFP.DECIMAL_BITS);
                }
                break;
                case CMD_LINETO: {
                    PointFP pt = pnts[j++];
                    path.lineTo(pt.x >> SingleFP.DECIMAL_BITS,
                            pt.y >> SingleFP.DECIMAL_BITS);
                }

                break;
                case CMD_QCURVETO: {
                    PointFP pt1 = pnts[j++];
                    PointFP pt2 = pnts[j++];
                    path.quadTo(pt1.x >> SingleFP.DECIMAL_BITS,
                            pt1.y >> SingleFP.DECIMAL_BITS,
                            pt2.x >> SingleFP.DECIMAL_BITS,
                            pt2.y >> SingleFP.DECIMAL_BITS);

                }
                break;
                case CMD_CCURVETO: {
                    PointFP pt1 = pnts[j++];
                    PointFP pt2 = pnts[j++];
                    PointFP pt3 = pnts[j++];
                    path.curveTo(pt1.x >> SingleFP.DECIMAL_BITS,
                            pt1.y >> SingleFP.DECIMAL_BITS,
                            pt2.x >> SingleFP.DECIMAL_BITS,
                            pt2.y >> SingleFP.DECIMAL_BITS,
                            pt3.x >> SingleFP.DECIMAL_BITS,
                            pt3.y >> SingleFP.DECIMAL_BITS);
                }
                break;
                case CMD_CLOSE:
                    path.closePath();
                    break;
                default:
            }
        }
        return path;
    }

    int[] cmds = null;
    PointFP[] pnts = null;
    int cmdsSize = 0;
    int pntsSize = 0;

    static final PointFP[] ROUNDCAP = new PointFP[7];
    static final PointFP[] SQUARECAP = new PointFP[2];
    static final int ONE;


    private final static int CMD_NOP = 0;
    private final static int CMD_MOVETO = 1;
    private final static int CMD_LINETO = 2;
    private final static int CMD_QCURVETO = 3;
    private final static int CMD_CCURVETO = 4;
    private final static int CMD_CLOSE = 6;
    private final static int BLOCKSIZE = 16;
   
    static {
        ONE = SingleFP.ONE;
        ROUNDCAP[0] = new PointFP(25080, 60547);
        ROUNDCAP[1] = new PointFP(46341, 46341);
        ROUNDCAP[2] = new PointFP(60547, 25080);
        ROUNDCAP[3] = new PointFP(ONE, 0);
        ROUNDCAP[4] = new PointFP(60547, - 25080);
        ROUNDCAP[5] = new PointFP(46341, - 46341);
        ROUNDCAP[6] = new PointFP(25080, - 60547);
        SQUARECAP[0] = new PointFP(ONE, ONE);
        SQUARECAP[1] = new PointFP(ONE, -ONE);
    }

    void visit(IGraphicsPathIteratorFP iterator) {
        if (iterator != null) {
            iterator.begin();
            int j = 0;
            for (int i = 0; i < cmdsSize; i++) {
                switch (cmds[i]) {

                    case CMD_NOP:
                        break;

                    case CMD_MOVETO:
                        iterator.moveTo(pnts[j++]);
                        break;

                    case CMD_LINETO:
                        iterator.lineTo(pnts[j++]);
                        break;

                    case CMD_QCURVETO:
                        iterator.quadTo(pnts[j++], pnts[j++]);
                        break;

                    case CMD_CCURVETO:
                        iterator.curveTo(pnts[j++], pnts[j++], pnts[j++]);
                        break;

                    case CMD_CLOSE:
                        iterator.close();
                        break;

                    default:
                        return;

                }
            }
            iterator.end();
        }
    }


    private static PointFP calcControlPoint(PointFP p1, PointFP p2,
            PointFP p3, int ff_factor) {
        PointFP ps = new PointFP(p2.x + MathFP.mul(p2.x - p1.x, ff_factor),
                p2.y + MathFP.mul(p2.y - p1.y, ff_factor));
        return new LineFP((new LineFP(p2, ps)).getCenter(),
                (new LineFP(p2, p3)).getCenter()).getCenter();
    }


    void extendIfNeeded(int cmdsAddNum, int pntsAddNum) {
        if (cmds == null) {
            cmds = new int[BLOCKSIZE];
        }
        if (pnts == null) {
            pnts = new PointFP[BLOCKSIZE];
        }

        if (cmdsSize + cmdsAddNum > cmds.length) {
            int[] newdata = new int[cmds.length + (cmdsAddNum > BLOCKSIZE
                    ? cmdsAddNum : BLOCKSIZE)];
            if (cmdsSize > 0) {
                System.arraycopy(cmds, 0, newdata, 0, cmdsSize);
            }
            cmds = newdata;
        }
        if (pntsSize + pntsAddNum > pnts.length) {
            PointFP[] newdata = new PointFP[pnts.length +
                    (pntsAddNum > BLOCKSIZE ? pntsAddNum : BLOCKSIZE)];
            if (pntsSize > 0) {
                System.arraycopy(pnts, 0, newdata, 0, pntsSize);
            }
            pnts = newdata;
        }
    }
}