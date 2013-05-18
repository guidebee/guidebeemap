//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
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
 * This class actually renders path in memory.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 18/04/10
 * @author      Guidebee Pty Ltd.
 */
class GraphicsPathRendererFP extends GraphicsPathSketchFP {

    /**
     * paint mode XOR
     */
    public final static int MODE_XOR = 1;
    /**
     * paint mode ZERO.(copy)
     */
    public final static int MODE_ZERO = 2;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public GraphicsPathRendererFP() {
        this(1, 1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor
     * @param width the width for the drawing canvas
     * @param height the height for the drawing cavas.
     */
    public GraphicsPathRendererFP(int width, int height) {
        ff_xmin = SingleFP.MAX_VALUE;
        ff_xmax = SingleFP.MIN_VALUE;
        ff_ymin = SingleFP.MAX_VALUE;
        ff_ymax = SingleFP.MIN_VALUE;
        reset(width, height);
        scanbuf = new int[4096];
        scanbuf_tmp = new int[4096];

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the width of the graphics object.
     * @return
     */
    public int getWidth() {
        return width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the height of the graphics.
     * @return
     */
    public int getHeight() {
        return height;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param path
     * @param style
     * @param mode
     */
    public void drawPath(GraphicsPathFP path, BrushFP style, int mode) {
        scanIndex = 0;
        drawMode = mode;
        path.visit(this);
        radixSort(scanbuf, scanbuf_tmp, scanIndex);
        fillStyle = style;
        if (transformMatrix != null) {
            fillStyle.setGraphicsMatrix(transformMatrix);
        }
        drawBuffer();
        fillStyle = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param path
     * @param matrix
     * @param fillStyle
     * @param mode
     */
    public void drawPath(GraphicsPathFP path, MatrixFP matrix,
            BrushFP fillStyle, int mode) {
        transformMatrix = matrix;
        drawPath(path, fillStyle, mode);
        transformMatrix = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param width
     * @param height
     * @param scanline
     */
    public void reset(int width, int height) {
        buffer = new int[width * height];
        this.width = width;
        this.height = height;
        setClip(0,0,width,height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param color
     */
    public void clear(int color) {
        backGroundColor=color;
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = color;
        }
        setClip(0,0,width,height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param color
     */
    public void finalizeBuffer(int color) {
        backGroundColor=color;
        ColorFP bk = ColorFP.fromArgb(color);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (clipContains(x, y)) {
                    ColorFP c = ColorFP.fromArgb(buffer[x + y * width]);
                    if (c.alpha != 0x00) {
                        if (c.alpha != 0xFF) {

                            buffer[x + y * width] = ColorFP.fromArgb(
                                    (c.red * c.alpha +
                                    (0xFF - c.alpha) * bk.red) >> 8,
                                    (c.green * c.alpha
                                    + (0xFF - c.alpha) * bk.green) >> 8,
                                    (c.blue * c.alpha
                                    + (0xFF - c.alpha) * bk.blue) >> 8).value;
                        }
                    } else {
                        buffer[x + y * width] = color;
                    }
                }
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
     *
     * @param point
     */
    public void moveTo(PointFP point) {
        transformedPoint = new PointFP(point);
        if (transformMatrix != null) {
            transformedPoint.transform(transformMatrix);
        }
        super.moveTo(point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param point
     */
    public void lineTo(PointFP point) {
        PointFP pntTemp = new PointFP(point);

        ff_xmin = MathFP.min(ff_xmin, currentPoint().x);
        ff_xmax = MathFP.max(ff_xmax, point.x);
        ff_ymin = MathFP.min(ff_ymin, currentPoint().y);
        ff_ymax = MathFP.max(ff_ymax, point.y);

        if (transformMatrix != null) {
            pntTemp.transform(transformMatrix);
        }

        scanline(transformedPoint.x, transformedPoint.y, pntTemp.x, pntTemp.y);
        transformedPoint = pntTemp;
        super.lineTo(point);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the clip
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setClip(int x,
                    int y,
                    int width,
                    int height){
        this.clipX=x;
        this.clipY=y;
        this.clipHeight=height;
        this.clipWidth=width;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check to see if this rectangle contains given point.
     * @param p
     * @return
     */
    private boolean clipContains(int x,int y) {
        return clipX <= x &&x <= clipX+clipWidth
                && clipY <=y && y <= clipY+clipHeight;
    }



    int[] buffer = null;
    int backGroundColor=0x00FFFFFF;
    private final static int RENDERER_FRAC_Y = 4;
    private final static int RENDERER_FRAC_X = 4;
    private final static int RENDERER_REAL_X = 12;
    private final static int RENDERER_REAL_Y = 11;
    private final static int BUFFERSIZE = 2048;
    private final static int RENDERER_REAL_X_MASK = (1 << RENDERER_REAL_X) - 1;
    private final static int RENDERER_REAL_Y_MASK = (1 << RENDERER_REAL_Y) - 1;
    private final static int RENDERER_FRAC_X_FACTOR = 1 << RENDERER_FRAC_X;
    private final static int RENDERER_FRAC_X_MASK = (1 << RENDERER_FRAC_X) - 1;
    private MatrixFP transformMatrix = null;
    private BrushFP fillStyle = null;
    private static int[] scanbuf = null;
    private static int[] scanbuf_tmp = null;
    private static int[] counts = new int[256];
    private static int[] index = new int[256];
    private PointFP transformedPoint;
    private int width = 0;
    private int height = 0;
    private int drawMode = MODE_XOR;
    private int scanIndex = 0;
    private int ff_xmin;
    private int ff_xmax;
    private int ff_ymin;
    private int ff_ymax;

    int clipX=0;
    int clipY=0;
    int clipWidth=0;
    int clipHeight=0;

    private static void radixSort(int[] data_src, int[] data_tmp, int num) {
        int shift, i;
        int[] src = data_src;
        int[] dst = data_tmp;
        int[] tmp;
        for (shift = 0; shift <= 24; shift += 8) {
            for (i = 0; i < 256; i++) {
                counts[i] = 0;
            }

            for (i = 0; i < num; i++) {
                counts[(src[i] >> shift) & 0xFF]++;
            }
            int indexnow = 0;
            for (i = 0; i < 256; i++) {
                index[i] = indexnow;
                indexnow += counts[i];
            }
            for (i = 0; i < num; i++) {
                dst[index[(src[i] >> shift) & 0xFF]++] = src[i];
            }
            tmp = src;
            src = dst;
            dst = tmp;
        }
    }

    private void drawBuffer() {
        int curd = 0;
        int cure = 0;
        int cura = 0;
        int cula = 0;
        int cury = 0;
        int curx = 0;
        int curs = 0;
        int count = scanIndex;
        for (int c = 0; c <= count; c++) {
            curs = c == count ? 0 : scanbuf[c];

            int newy = ((curs >> (RENDERER_REAL_X + RENDERER_FRAC_X + 1))
                    & RENDERER_REAL_Y_MASK);
            int newx = ((curs >> (RENDERER_FRAC_X + 1)) & RENDERER_REAL_X_MASK);
            if ((newx != curx) || (newy != cury)) {
                int alp = (256 * cure) / (RENDERER_FRAC_Y) +
                        (256 * cula) / (RENDERER_FRAC_Y
                        * (RENDERER_FRAC_X_FACTOR - 1)) +
                        (256 * cura) / (RENDERER_FRAC_Y
                        * (RENDERER_FRAC_X_FACTOR - 1));
                if (alp != 0) {
                    if (drawMode == MODE_XOR) {
                        alp = (alp & 0x100) != 0
                                ? (0xFF - (alp & 0xFF)) : (alp & 0xFF);
                    } else {
                        alp = MathFP.min(255, MathFP.abs(alp));
                    }
                    if (alp != 0) {
                        mergePixels(curx, cury, 1, alp);
                    }
                }
                cure = curd;

                if (newy == cury) {
                    if (curd != 0) {
                        alp = (256 * curd) / RENDERER_FRAC_Y;
                        if (alp != 0) {
                            if (drawMode == MODE_XOR) {
                                alp = (alp & 0x100) != 0
                                        ? (0xFF - (alp & 0xFF)) : (alp & 0xFF);
                            } else {
                                alp = MathFP.min(255, MathFP.abs(alp));
                            }
                            if (alp != 0) {
                                mergePixels(curx + 1, cury, newx - curx - 1, alp);
                            }
                        }
                    }
                } else {
                    cury = newy;
                    curd = cure = 0;
                }

                curx = newx;
                cura = cula = 0;
            }

            if ((curs & 1) != 0) {
                curd++;
                cula += ((~(curs >> 1)) & RENDERER_FRAC_X_MASK);
            } else {
                curd--;
                cura -= ((~(curs >> 1)) & RENDERER_FRAC_X_MASK);
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
     *
     * @param ff_sx
     * @param ff_sy
     * @param ff_ex
     * @param ff_ey
     */
    private void scanline(int ff_sx, int ff_sy, int ff_ex, int ff_ey) {
        int sx = ff_sx >> (SingleFP.DECIMAL_BITS - RENDERER_FRAC_X);
        int ex = ff_ex >> (SingleFP.DECIMAL_BITS - RENDERER_FRAC_X);
        int sy = (ff_sy * RENDERER_FRAC_Y) >> SingleFP.DECIMAL_BITS;
        int ey = (ff_ey * RENDERER_FRAC_Y) >> SingleFP.DECIMAL_BITS;
        int xmin = MathFP.min(sx, ex);
        int xmax = MathFP.max(sx, ex);
        int ymin = MathFP.min(sy, ey);
        int ymax = MathFP.max(sy, ey);
        int incx = ff_sx < ff_ex && ff_sy < ff_ey || ff_sx >= ff_ex
                && ff_sy >= ff_ey ? 1 : - 1;
        int x = incx == 1 ? xmin : xmax;
        int dire = ff_sy < ff_ey ? 1 : 0;

        if (((ymin < 0) && (ymax < 0)) || ((ymin >= (height * RENDERER_FRAC_Y))
                && (ymax >= (height * RENDERER_FRAC_Y)))) {
            return;
        }

        int n = MathFP.abs(xmax - xmin);
        int d = MathFP.abs(ymax - ymin);
        int i = d;

        ymax = MathFP.min(ymax, height * RENDERER_FRAC_Y);

        for (int y = ymin; y < ymax; y++) {
            if (y >= 0) {
                if (scanIndex >= scanbuf.length) {
                    int bufSize = scanIndex / BUFFERSIZE;
                    if ((scanIndex + 1) % BUFFERSIZE != 0) {
                        bufSize += 1;
                    }
                    scanbuf_tmp = new int[bufSize * BUFFERSIZE];
                    System.arraycopy(scanbuf, 0, scanbuf_tmp, 0, scanIndex);
                    scanbuf = new int[bufSize * BUFFERSIZE];


                    System.arraycopy(scanbuf_tmp, 0, scanbuf, 0, scanIndex);
                    System.gc();
                    System.gc();

                }
                scanbuf[scanIndex++] = ((y / RENDERER_FRAC_Y) 
                        << (RENDERER_REAL_X + RENDERER_FRAC_X + 1))
                        | (MathFP.max(0, MathFP.min((width *
                        RENDERER_FRAC_X_FACTOR) - 1, x)) << 1) | dire;

            }
            i += n;
            if (i > d) {
                int idivd = (i - 1) / d;
                x += incx * idivd;
                i -= d * idivd;
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
     *
     * @param x
     * @param y
     * @param count
     * @param opacity
     */
    private void mergePixels(int x, int y, int count, int opacity) {
        boolean isMonoColor = fillStyle.isMonoColor();
        int color = 0;
        if (isMonoColor) {
            color = fillStyle.getNextColor();
            color = ((((color >> 24) & 0xFF) * opacity) >> 8)
                    << 24 | color & 0xFFFFFF;
        }
        int lastBackColor = 0;
        int lastMergedColor = 0;
        for (int i = 0; i < count; i++) {
            if (clipContains(x + i, y)) {
                int bkColor = buffer[x + i + y * width];
                if (!isMonoColor) {
                    color = i == 0 ? fillStyle.getColorAt(x + i, y, count == 1)
                            : fillStyle.getNextColor();
                    if (opacity != 0xFF) {
                        color = ((((color >> 24) & 0xFF) * opacity) >> 8)
                                << 24 | color & 0xFFFFFF;
                    }
                }
                if (lastBackColor == bkColor && isMonoColor) {
                    buffer[x + i + y * width] = lastMergedColor;

                } else {
                    buffer[x + i + y * width] = merge(bkColor, color);
                    lastBackColor = bkColor;
                    lastMergedColor = buffer[x + i + y * width];
                }
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
     *
     * @param color1
     * @param color2
     * @return
     */
    private static int merge(int color1, int color2) {
        int a2 = (color2 >> 24) & 0xFF;
        if (a2 == 0xFF || color1 == 0x0) {
            return color2;
        } else if (a2 == 0) {
            return color1;
        } else {
            int a1 = 0xFF - ((color1 >> 24) & 0xFF);
            int a3 = 0xFF - a2;
            int b1 = color1 & 0xFF;
            int g1 = (color1 >> 8) & 0xFF;
            int r1 = (color1 >> 16) & 0xFF;
            int b2 = color2 & 0xFF;
            int g2 = (color2 >> 8) & 0xFF;
            int r2 = (color2 >> 16) & 0xFF;

            int Ca = (0xFF * 0xFF - a1 * a3) >> 8;
            int Cr = (r1 * a3 + r2 * a2) >> 8;
            int Cg = (g1 * a3 + g2 * a2) >> 8;
            int Cb = (b1 * a3 + b2 * a2) >> 8;
            return Ca << 24 | Cr << 16 | Cg << 8 | Cb;
        }
    }
}
