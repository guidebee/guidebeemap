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
import java.io.IOException;

import com.mapdigit.gis.drawing.AbstractGraphicsFactory;
import com.mapdigit.gis.drawing.IFont;
import com.mapdigit.gis.drawing.IImage;
import java.awt.Font;

//[------------------------------ MAIN CLASS ----------------------------------]
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Java SE implementation of the AbstractGraphicsFactory class.
 * <hr><b>&copy; Copyright 2010 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
public class JavaSEGraphicsFactory extends AbstractGraphicsFactory{

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the graphics factory instance.
     * @return
     */
    public static JavaSEGraphicsFactory getInstance(){
        return INSTANCE;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create image.
     * @param width
     * @param height
     * @return
     */
    public IImage createImage(int width, int height) {
        return JavaSEImage.createImage(width,height);
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create image.
     * @param bytes
     * @param offset
     * @param len
     * @return
     */
    public IImage createImage(byte[] bytes, int offset, int len) {
         return JavaSEImage.createImage(bytes,offset,len);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create image.
     * @param rgb
     * @param width
     * @param height
     * @return
     */
    public IImage createImage(int[] rgb, int width, int height) {
        return JavaSEImage.createImage(rgb,width,height);
    }


    /**
     * single insntace.
     */
    private final static JavaSEGraphicsFactory INSTANCE=new JavaSEGraphicsFactory();


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private construct.
     */
    private JavaSEGraphicsFactory(){

    }



}
