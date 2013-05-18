//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09SEP2010  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.pstreets.common.drawing;

//--------------------------------- IMPORTS ------------------------------------

import com.mapdigit.gis.drawing.IFont;
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//[------------------------------ MAIN CLASS ----------------------------------]
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Java SE implemeation of the IGraphics interface.
 * <hr><b>&copy; Copyright 2010 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
public class JavaSEGraphics implements IGraphics {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param graphics
     */
    public JavaSEGraphics(Graphics graphics) {
        this.graphics = graphics;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the clip region.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setClip(int x, int y, int width, int height) {
        graphics.setClip(x, y, width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw image at given location.
     * @param img
     * @param x
     * @param y
     */
    public void drawImage(IImage img, int x, int y) {
        graphics.drawImage(((JavaSEImage) img).image, x, y,null);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set color.
     * @param RGB
     */
    public void setColor(int RGB) {
        graphics.setColor(new Color(RGB));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill a rectangle.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillRect(int x, int y, int width, int height) {
        graphics.fillRect(x, y, width, height);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set font.
     * @param font
     */
    public void setFont(IFont font) {
        graphics.setFont((Font) font.getNativeFont());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw string.
     * @param str
     * @param x
     * @param y
     */
    public void drawString(String str, int x, int y) {
        graphics.drawString(str, x, y);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw rectangle.
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void drawRect(int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
    }

    /**
     * java se native graphics object
     */
    private Graphics graphics;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw a line.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * fill triangle.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int []xPoints=new int[3];
        int []yPoints=new int[3];
        xPoints[0]=x1;xPoints[1]=x2;xPoints[2]=x3;
        yPoints[0]=x1;yPoints[1]=x2;yPoints[2]=x3;
        graphics.fillPolygon(xPoints,yPoints,3);
    }

}
