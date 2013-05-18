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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;


//[------------------------------ MAIN CLASS ----------------------------------]
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Java SE implemeation of the IFont interface.
 * <hr><b>&copy; Copyright 2010 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
public class JavaSEFont implements IFont{

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * default constructor.
     */
    public JavaSEFont(){
        BufferedImage image=new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        fontMetrics=image.getGraphics().getFontMetrics(font);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the navtive font object.
     * @return native font object.
     */
    public Object getNativeFont() {
        return font;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the width of the char array.
     * @param ch
     * @param offset
     * @param length
     * @return
     */
    public int charsWidth(char[] ch, int offset, int length) {
        return fontMetrics.charsWidth(ch, offset, length);
    }


    /**
     * system font object.
     */
    Font font;

    /**
     * font metrics
     */
    private final FontMetrics fontMetrics;

}
