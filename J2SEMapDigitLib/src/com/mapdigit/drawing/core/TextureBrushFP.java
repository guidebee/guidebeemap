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
 * Defines a brush of a single color. Brushes are used to fill graphics shapes,
 * such as rectangles, ellipses, pies, polygons, and paths. This class cannot
 * be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class TextureBrushFP extends BrushFP {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * Always return true.
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
     * Default constructor.default color is white.
     */
    public TextureBrushFP(int []image,int width,int height) {
        textureBuffer=new int[image.length];
        System.arraycopy(image, 0, textureBuffer, 0, textureBuffer.length);
        this.width=width;
        this.height=height;

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
     * @param singlePoint  single point
     * @return the color value at given location.
     */
    public int getColorAt(int x, int y, boolean singlePoint) {
        PointFP p = new PointFP(x << SingleFP.DECIMAL_BITS,
                y << SingleFP.DECIMAL_BITS);
        nextPt.x=p.x+ SingleFP.ONE;
        nextPt.y=p.y;
        if (finalMatrix != null) {
            p.transform(finalMatrix);

        }
        int xPos=(p.x >> SingleFP.DECIMAL_BITS) % width;
        int yPos=(p.y >> SingleFP.DECIMAL_BITS) % height;

        if(xPos<0) xPos+=width;
        if(yPos<0) yPos+=height;
       
        return textureBuffer[(xPos+yPos*width)];
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @inheritDoc
     * @return next color.
     */
    public int getNextColor() {
        PointFP p = new PointFP(nextPt);

        nextPt.x+= SingleFP.ONE;
        nextPt.y=p.y;

        if (finalMatrix != null) {
            p.transform(finalMatrix);

        }
        int xPos=(p.x >> SingleFP.DECIMAL_BITS) % width;
        int yPos=(p.y >> SingleFP.DECIMAL_BITS) % height;

        if(xPos<0) xPos+=width;
        if(yPos<0) yPos+=height;
        
        return textureBuffer[xPos+yPos*width];
    }


    /**
     * the width of the texture
     */
    private int width=1;

    /**
     * the height of the texture brush
     */
    private int height=1;

    /**
     * the texture buffer
     */
    private int [] textureBuffer=null;

    /**
     * next point position.
     */
    private final PointFP nextPt=new PointFP(0,0);
}
