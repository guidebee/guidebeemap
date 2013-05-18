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
 * Encapsulates a 2D drawing surface.
 * be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 18/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class GraphicsFP {

    /**
     * draw mode , XOR
     */
    public final static int MODE_XOR = GraphicsPathRendererFP.MODE_XOR;

    /**
     * draw mode. nothing.
     */
    public final static int MODE_ZERO = GraphicsPathRendererFP.MODE_ZERO;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default Constructor.
     */
    public GraphicsFP() {
        initBlock();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor. create a graphics object with given width and height
     */
    public GraphicsFP(int width, int height) {
        initBlock();
        resize(width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the content of this image as ARGB array.
     * @return the ARGB array of the image content.
     */
    public int[] getRGB() {
        return renderer.buffer;
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the brush object of the graphics.
     * @return the brush object.
     */
    public BrushFP getBrush() {
        return fillStyle;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set a new brush for this graphics object.
     * @param value a new brush.
     */
    public void setBrush(BrushFP value) {
        fillStyle = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the pen object for this graphics.
     * @return the pen object.
     */
    public PenFP getPen() {
        return lineStyle;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the new pen for this graphics object.
     * @param value a new pen object.
     */
    public void setPen(PenFP value) {
        lineStyle = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the paint mode.
     * @return the paint mode.
     */
    public int getPaintMode() {
        return paintMode;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the paint mode for this graphics.
     * @param value
     */
    public void setPaintMode(int value) {
        paintMode = value;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the graphics drawing matrix.
     * @return the drawing matrix.
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
     * Set the graphics matrix.
     * @param value the new matrix.
     */
    public void setMatrix(MatrixFP value) {
        matrix = value == null ? null : new MatrixFP(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * resize the graphics object.
     * @param width the new width of the graphics.
     * @param height the new height of the graphics object.
     */
    public void resize(int width, int height) {
        renderer.reset(width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clear the graphics content with given color.
     * @param color the clear color.
     */
    public void clear(int color) {
        renderer.clear(color);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Draw a line.
     * @param ff_x1  the x coord of the first point of the line.
     * @param ff_y1  the y coord of the first point of the line.
     * @param ff_x2  the x coord of the second point of the line.
     * @param ff_y2  the y coord of the second point of the line.
     */
    public void drawLine(int ff_x1, int ff_y1, int ff_x2, int ff_y2) {
        drawPath(GraphicsPathFP.createLine(ff_x1, ff_y1, ff_x2, ff_y2));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a polyline.
     * @param points the coordinates  of the polyline.
     */
    public void drawPolyline(PointFP[] points) {
        drawPath(GraphicsPathFP.createPolyline(points));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Draw a polygon
     * @param points the coordinates  of the polygon.
     */
    public void drawPolygon(PointFP[] points) {
        drawPath(GraphicsPathFP.createPolygon(points));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw curves.
     * @param points
     * @param offset
     * @param numberOfSegments
     * @param ff_factor
     */
    public void drawCurves(PointFP[] points, int offset, int numberOfSegments,
            int ff_factor) {
        drawPath(GraphicsPathFP.createSmoothCurves(points,
                offset, numberOfSegments, ff_factor, false));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param points
     * @param offset
     * @param numberOfSegments
     * @param ff_factor
     */
    public void drawClosedCurves(PointFP[] points, int offset,
            int numberOfSegments, int ff_factor) {
        drawPath(GraphicsPathFP.createSmoothCurves(points, offset,
                numberOfSegments, ff_factor, true));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a round rect
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_rx    the radius of the round circle.
     * @param ff_ry    the radius of the round circle.
     */
    public void drawRoundRect(int ff_xmin, int ff_ymin, int ff_xmax,
            int ff_ymax, int ff_rx, int ff_ry) {
        drawPath(GraphicsPathFP.createRoundRect(ff_xmin, ff_ymin, ff_xmax,
                ff_ymax, ff_rx, ff_ry));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a rectangle.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     */
    public void drawRect(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        drawPath(GraphicsPathFP.createRect(ff_xmin, ff_ymin, ff_xmax, ff_ymax));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a oval.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     */
    public void drawOval(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        drawPath(GraphicsPathFP.createOval(ff_xmin, ff_ymin, ff_xmax, ff_ymax));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw an arc.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_startangle
     * @param ff_sweepangle
     */
    public void drawArc(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax,
            int ff_startangle, int ff_sweepangle) {
        drawPath(GraphicsPathFP.createArc(ff_xmin, ff_ymin, ff_xmax, ff_ymax,
                ff_startangle, ff_sweepangle, false));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a pie.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_startangle
     * @param ff_sweepangle
     */
    public void drawPie(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax,
            int ff_startangle, int ff_sweepangle) {
        drawPath(GraphicsPathFP.createArc(ff_xmin, ff_ymin, ff_xmax, ff_ymax,
                ff_startangle, ff_sweepangle, true));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a path.
     * @param path
     */
    public void drawPath(GraphicsPathFP path) {
        if(lineStyle.dashArray!=null){
            PenFP newlineStyle=new PenFP(lineStyle.brush,lineStyle.width,
                    PenFP.LINECAP_BUTT,PenFP.LINECAP_BUTT,PenFP.LINEJOIN_MITER);

            newlineStyle.dashArray=lineStyle.dashArray;
            GraphicsPathDasherFP dasher = new GraphicsPathDasherFP(path,
                    newlineStyle.dashArray,0);
            GraphicsPathFP newPath = dasher.GetDashedGraphicsPath();
            renderer.drawPath(newPath.calcOutline(newlineStyle), matrix,
                lineStyle.brush, GraphicsPathRendererFP.MODE_ZERO);

        }else{

            renderer.drawPath(path.calcOutline(lineStyle), matrix,
                lineStyle.brush, GraphicsPathRendererFP.MODE_ZERO);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill  closed curves.
     * @param points
     * @param offset
     * @param numberOfSegments
     * @param ff_factor
     */
    public void fillClosedCurves(PointFP[] points, int offset,
            int numberOfSegments, int ff_factor) {
        fillPath(GraphicsPathFP.createSmoothCurves(points, offset,
                numberOfSegments, ff_factor, true));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a polygon.
     * @param points
     */
    public void fillPolygon(PointFP[] points) {
        fillPath(GraphicsPathFP.createPolygon(points));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a round rectangle.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_rx
     * @param ff_ry
     */
    public void fillRoundRect(int ff_xmin, int ff_ymin, int ff_xmax,
            int ff_ymax, int ff_rx, int ff_ry) {
        fillPath(GraphicsPathFP.createRoundRect(ff_xmin, ff_ymin, ff_xmax,
                ff_ymax, ff_rx, ff_ry));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a rectangle.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     */
    public void fillRect(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        GraphicsPathFP path = GraphicsPathFP.createRect(ff_xmin, ff_ymin,
                ff_xmax, ff_ymax);
        path.addClose();
        fillPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a oval.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     */
    public void fillOval(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        GraphicsPathFP path = GraphicsPathFP.createOval(ff_xmin,
                ff_ymin, ff_xmax, ff_ymax);
        path.addClose();
        fillPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a pie.
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @param ff_startangle
     * @param ff_sweepangle
     */
    public void fillPie(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax,
            int ff_startangle, int ff_sweepangle) {
        fillPath(GraphicsPathFP.createArc(ff_xmin, ff_ymin, ff_xmax, ff_ymax,
                ff_startangle, ff_sweepangle, true));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a path.
     * @param path
     */
    public void fillPath(GraphicsPathFP path) {
        renderer.drawPath(path, matrix, fillStyle, paintMode);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the clip height.
     * @return
     */
    public int getClipHeight(){
        return renderer.clipHeight;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the clip width
     * @return
     */
    public int getClipWidth(){
        return renderer.clipWidth;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get top left of the clip area
     * @return
     */
    public int getClipX(){
        return renderer.clipX;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the right bottom of the clip area.
     * @return
     */
    public int getClipY(){
        return renderer.clipY;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the clip area.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setClip(int x,
                    int y,
                    int width,
                    int height) {
        renderer.setClip(x, y, width, height);
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
        renderer.finalizeBuffer(color);
    }

    private PenFP lineStyle;
    private BrushFP fillStyle;
    private GraphicsPathRendererFP renderer = new GraphicsPathRendererFP();
    private int paintMode;
    private MatrixFP matrix = null;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * init the pen, brush, paint mode.
     */
    private void initBlock() {
        lineStyle = new PenFP(0x0, SingleFP.ONE);
        fillStyle = new SolidBrushFP(0x0);
        paintMode = MODE_XOR;
    }
}
