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
 * An class that describes a gradient, composed of gradient stops.
 * Classes that inherit from GradientBrush describe different ways of
 * interpreting gradient stops.
 * be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public class RadialGradientBrushFP extends BrushFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create the gradient brush.
     * @param ff_xmin the top left coordinate.
     * @param ff_ymin the top left coordinate.
     * @param ff_xmax the bottom right coordinate.
     * @param ff_ymax the bottom right coordinate.
     * @param ff_angle the angle for this gradient.
     * @param type  the type of the gradient brush.
     */
    public RadialGradientBrushFP(int ff_x, int ff_y, int ff_radius,
            int ff_angle) {

        matrix = new MatrixFP();
        centerPt.reset(ff_x,
                ff_y);
        matrix.translate(-centerPt.x, -centerPt.y);
        matrix.rotate(-ff_angle);
        this.ff_radius=ff_radius;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set gradient color at given ratio.
     * @param ratio the ratio value.
     * @param color the color value.
     */
    public void setGradientColor(int ratio, int color) {
        ratio = ratio >> SingleFP.DECIMAL_BITS - RATIO_BITS;
        int i;
        ratio = ratio < 0 ? 0 : (ratio > RATIO_MAX ? RATIO_MAX : ratio);
        if (ratioCount == ratios.length) {
            int[] rs = new int[ratioCount + 16];
            System.arraycopy(ratios, 0, rs, 0, ratioCount);
            ratios = rs;
        }
        gradientColors[ratio] = color;
        for (i = ratioCount; i > 0; i--) {
            if (ratio >= ratios[i - 1]) {
                break;
            }
        }
        if (!(i > 0 && ratio == ratios[i])) {
            if (i < ratioCount) {
                System.arraycopy(ratios, i, ratios, i + 1, ratioCount - i);
            }
            ratios[i] = ratio;
            ratioCount++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * update the gradient table.
     */
    public void updateGradientTable() {
        if (ratioCount == 0) {
            return;
        }
        int i;
        for (i = 0; i < ratios[0]; i++) {
            gradientColors[i] = gradientColors[ratios[0]];
        }
        for (i = 1; i < ratioCount; i++) {
            int r1 = ratios[i - 1];
            int r2 = ratios[i];
            for (int j = r1 + 1; j < r2; j++) {
                gradientColors[j] = interpolate(gradientColors[r1],
                        gradientColors[r2], 256 * (j - r1) / (r2 - r1));
            }
        }
        for (i = ratios[ratioCount - 1]; i <= RATIO_MAX; i++) {
            gradientColors[i] = gradientColors[ratios[ratioCount - 1]];
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * @return always return false.
     */
    public boolean isMonoColor() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * @param x the x coordinate
     * @param y the y coordinate
     * @param singlePoint
     * @return the color at given position.
     */
    public int getColorAt(int x, int y, boolean singlePoint) {
        int pos;
        PointFP p = new PointFP(x << SingleFP.DECIMAL_BITS,
                y << SingleFP.DECIMAL_BITS);
        nextPt.x=p.x+ SingleFP.ONE;
        nextPt.y=p.y;
        PointFP newCenterPt=new PointFP(centerPt);
        if (finalMatrix != null) {
            p.transform(finalMatrix);
            //newCenterPt.transform(finalMatrix);

        }
        ff_currpos = MathFP.div(PointFP.distance(p.x-newCenterPt.x,
                p.y-newCenterPt.y), ff_radius);
        pos = ff_currpos >> SingleFP.DECIMAL_BITS - RATIO_BITS;

        switch(fillMode){
            case REFLECT:
                pos = pos % (RATIO_MAX *2);
                pos = pos < 0 ? pos+ RATIO_MAX *2 : pos;
                pos = (pos < RATIO_MAX) ? pos : RATIO_MAX *2-pos;
                break;
            case REPEAT:
                 pos = pos % RATIO_MAX;
                 pos = pos < 0 ? pos+ RATIO_MAX : pos;
                break;
            case NO_CYCLE:
                pos = pos < 0 ? 0 : (pos > RATIO_MAX ? RATIO_MAX : pos);
                break;
        }

        return gradientColors[pos];
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * @return next color of the gradient brush.
     */
    public int getNextColor() {
        int pos;
        PointFP p = new PointFP(nextPt);
        nextPt.x=p.x+ SingleFP.ONE;
        nextPt.y=p.y;
        PointFP newCenterPt=new PointFP(centerPt);
        if (finalMatrix != null) {
            p.transform(finalMatrix);
            //newCenterPt.transform(finalMatrix);

        }
        ff_currpos = MathFP.div(PointFP.distance(p.x-newCenterPt.x,
                p.y-newCenterPt.y), ff_radius);
        pos = ff_currpos >> SingleFP.DECIMAL_BITS - RATIO_BITS;

        switch(fillMode){
            case REFLECT:
                pos = pos % (RATIO_MAX *2);
                pos = pos < 0 ? pos+ RATIO_MAX *2 : pos;
                pos = (pos < RATIO_MAX) ? pos : RATIO_MAX *2-pos;
                break;
            case REPEAT:
                 pos = pos % RATIO_MAX;
                 pos = pos < 0 ? pos+ RATIO_MAX : pos;
                break;
            case NO_CYCLE:
                pos = pos < 0 ? 0 : (pos > RATIO_MAX ? RATIO_MAX : pos);
                break;
        }

        return gradientColors[pos];
    }

    private final static int RATIO_BITS = 10;
    private final static int RATIO_MAX = (1 << RATIO_BITS) - 1;

    private int[] gradientColors = new int[1 << RATIO_BITS];
    private int[] ratios = new int[64];
    private int ratioCount = 0;
    private int ff_radius;
    private int ff_currpos;
    protected PointFP centerPt=new PointFP();
    private final PointFP nextPt=new PointFP(0,0);

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param a
     * @param b
     * @param pos
     * @return
     */
    private static int interpolate(int a, int b, int pos) {
        int p2 = pos & 0xFF;
        int p1 = 0xFF - p2;
        int ca = ((a >> 24) & 0xFF) * p1 + ((b >> 24) & 0xFF) * p2;
        int cr = ((a >> 16) & 0xFF) * p1 + ((b >> 16) & 0xFF) * p2;
        int cg = ((a >> 8) & 0xFF) * p1 + ((b >> 8) & 0xFF) * p2;
        int cb = (a & 0xFF) * p1 + (b & 0xFF) * p2;
        return ((ca >> 8) << 24) | ((cr >> 8) << 16) | ((cg >> 8) << 8) | ((cb >> 8));
    }

}