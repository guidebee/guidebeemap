//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
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
 * a 2D Line class.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public class LineFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the lenght of the line.
     */
    public int getLength() {
        return PointFP.distance(pt1, pt2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the center of the line.
     * @return
     */
    public PointFP getCenter() {
        return new PointFP((pt1.x + pt2.x) / 2, (pt1.y + pt2.y) / 2);
    }

    /**
     * start point the line.
     */
    public PointFP pt1 = new PointFP(0, 0);

    /**
     * end point of the line.
     */
    public PointFP pt2 = new PointFP(0, 0);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public LineFP() {
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param l
     */
    public LineFP(LineFP l) {
        reset(l.pt1, l.pt2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param p1
     * @param p2
     */
    public LineFP(PointFP p1, PointFP p2) {
        reset(p1, p2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param ff_x1
     * @param ff_y1
     * @param ff_x2
     * @param ff_y2
     */
    public LineFP(int ff_x1, int ff_y1, int ff_x2, int ff_y2) {
        reset(ff_x1, ff_y1, ff_x2, ff_y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the line the same location as given line.
     * @param l
     */
    public void reset(LineFP l) {
        reset(l.pt1, l.pt2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the line the to given points.
     * @param p1
     * @param p2
     */
    public void reset(PointFP p1, PointFP p2) {
        this.pt1.reset(p1);
        this.pt2.reset(p2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * reset the line to given coorindate.
     * @param ff_x1
     * @param ff_y1
     * @param ff_x2
     * @param ff_y2
     */
    public void reset(int ff_x1, int ff_y1, int ff_x2, int ff_y2) {
        pt1.reset(ff_x1, ff_y1);
        pt2.reset(ff_x2, ff_y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the head outline.
     * @param ff_rad
     * @return
     */
    public LineFP getHeadOutline(int ff_rad) {
        PointFP p = new PointFP(pt1.x - pt2.x, pt1.y - pt2.y);
        int len = getLength();
        if(len!=0){
            p.reset(MathFP.div(-p.y, len), MathFP.div(p.x, len));
            p.reset(MathFP.mul(p.x, ff_rad), MathFP.mul(p.y, ff_rad));
        }
        return new LineFP(pt1.x - p.x, pt1.y - p.y, pt1.x + p.x, pt1.y + p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the tail outline.
     * @param ff_rad
     * @return
     */
    public LineFP getTailOutline(int ff_rad) {
        PointFP c = getCenter();
        PointFP p = new PointFP(pt2.x - c.x, pt2.y - c.y);
        p.reset(p.y, -p.x);
        int dis = PointFP.distance(PointFP.ORIGIN, p);
        if (dis == 0) {
            dis = 1;
        }
        p.reset(MathFP.div(MathFP.mul(p.x, ff_rad), dis),
                MathFP.div(MathFP.mul(p.y, ff_rad), dis));
        return new LineFP(pt2.x - p.x, pt2.y - p.y, pt2.x + p.x, pt2.y + p.y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see the value are equal.
     * @param ff_val1
     * @param ff_val2
     * @return
     */
    private static boolean isEqual(int ff_val1, int ff_val2) {
        return isZero(ff_val1 - ff_val2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see a value is zero.
     * @param ff_val
     * @return
     */
    private static boolean isZero(int ff_val) {
        return MathFP.abs(ff_val) < (1 << SingleFP.DECIMAL_BITS / 2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if two line intects and return the the intersction point.
     * @param l1
     * @param l2
     * @param intersection
     * @return
     */
    public static boolean intersects(LineFP l1, LineFP l2, PointFP intersection) {
        int x = SingleFP.NaN;
        int y = SingleFP.NaN;

        if (intersection != null) {
            intersection.reset(x, y);
        }

        int ax0 = l1.pt1.x;
        int ax1 = l1.pt2.x;
        int ay0 = l1.pt1.y;
        int ay1 = l1.pt2.y;
        int bx0 = l2.pt1.x;
        int bx1 = l2.pt2.x;
        int by0 = l2.pt1.y;
        int by1 = l2.pt2.y;

        int adx = (ax1 - ax0);
        int ady = (ay1 - ay0);
        int bdx = (bx1 - bx0);
        int bdy = (by1 - by0);

        if (isZero(adx) && isZero(bdx)) {
            return isEqual(ax0, bx0);
        } else if (isZero(ady) && isZero(bdy)) {
            return isEqual(ay0, by0);
        } else if (isZero(adx)) {
            // A  vertical
            x = ax0;
            y = isZero(bdy) ? by0 : MathFP.mul(MathFP.div(bdy, bdx), x - bx0) + by0;
        } else if (isZero(bdx)) {
            // B vertical
            x = bx0;
            y = isZero(ady) ? ay0 : MathFP.mul(MathFP.div(ady, adx), x - ax0) + ay0;
        } else if (isZero(ady)) {
            y = ay0;
            x = MathFP.mul(MathFP.div(bdx, bdy), y - by0) + bx0;
        } else if (isZero(bdy)) {
            y = by0;
            x = MathFP.mul(MathFP.div(adx, ady), y - ay0) + ax0;
        } else {
            int xma = MathFP.div(ady, adx); // slope segment A
            int xba = ay0 - (MathFP.mul(ax0, xma)); // y intercept of segment A

            int xmb = MathFP.div(bdy, bdx); // slope segment B
            int xbb = by0 - (MathFP.mul(bx0, xmb)); // y intercept of segment B

            // parallel lines?
            if (xma == xmb) {
                // Need trig functions
                return xba == xbb;
            } else {
                // Calculate points of intersection
                // At the intersection of line segment A and B,
                //XA=XB=XINT and YA=YB=YINT
                x = MathFP.div((xbb - xba), (xma - xmb));
                y = (MathFP.mul(xma, x)) + xba;
            }
        }

        // After the point or points of intersection are calculated, each
        // solution must be checked to ensure that the point of intersection lies
        // on line segment A and B.

        int minxa = MathFP.min(ax0, ax1);
        int maxxa = MathFP.max(ax0, ax1);

        int minya = MathFP.min(ay0, ay1);
        int maxya = MathFP.max(ay0, ay1);

        int minxb = MathFP.min(bx0, bx1);
        int maxxb = MathFP.max(bx0, bx1);

        int minyb = MathFP.min(by0, by1);
        int maxyb = MathFP.max(by0, by1);

        if (intersection != null) {
            intersection.reset(x, y);
        }
        return ((x >= minxa) && (x <= maxxa) && (y >= minya) && (y <= maxya)
                && (x >= minxb) && (x <= maxxb) && (y >= minyb) && (y <= maxyb));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param distance
     * @return
     */
    PointFP getPointAtDistance(int distance) {
       int lineLength=getLength();
       if(distance>lineLength){
           return null;
       }else if(distance==lineLength){
           return new PointFP(pt2);
       }else{
           int scale=MathFP.div(distance, lineLength);
           PointFP pointFP=new PointFP();
           pointFP.reset(pt1.x+MathFP.mul(pt2.x-pt1.x, scale),
                   pt1.y+MathFP.mul(pt2.y-pt1.y, scale));
           return pointFP;
       }
    }
}
