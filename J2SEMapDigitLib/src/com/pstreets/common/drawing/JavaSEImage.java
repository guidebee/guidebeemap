//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 09SEP2010  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.pstreets.common.drawing;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.drawing.IGraphics;
import com.mapdigit.gis.drawing.IImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;


//[------------------------------ MAIN CLASS ----------------------------------]
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 09SEP2010  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Java SE implemeation of the IImage interface.
 * <hr><b>&copy; Copyright 2010 Guidebee, Inc. </b>
 * @version     1.00, 09/09/10
 * @author      Guidebee Pty Ltd.
 */
class JavaSEImage implements IImage {

   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create a empty image with given width and height.
     * @param width
     * @param height
     * @return
     */
    public static IImage createImage(int width,int height) {
        JavaSEImage javaSEImage = new JavaSEImage();
        javaSEImage.image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        return javaSEImage;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create image from byte array.
     * @param bytes
     * @param offset
     * @param len
     * @return
     */
    public static IImage createImage(byte[] bytes,int offset,int len) {
        ByteArrayInputStream bis=new ByteArrayInputStream(bytes,offset,len);
        try {
            JavaSEImage javaSEImage = new JavaSEImage();
           javaSEImage.image = ImageIO.read(bis);
           return javaSEImage;
        } catch (IOException ex) {

        }
        return null;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * create image from rgb array.
     * @param rgb
     * @param width
     * @param height
     * @return
     */
    public static IImage createImage(int[] rgb, int width, int height) {
        JavaSEImage javaSEImage = new JavaSEImage();
        javaSEImage.image =  new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        javaSEImage.image.setRGB(0, 0, width, height, rgb, 0, width);
        return javaSEImage;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get graphics object of the image.
     * @return
     */
    public IGraphics getGraphics() {
        JavaSEGraphics lwuitGraphics = new JavaSEGraphics(image.getGraphics());
        return lwuitGraphics;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the rgb array of the image.
     * @return
     */
    public int[] getRGB() {
        return image.getRGB(0,0,image.getWidth(),image.getHeight(),null,
                0,image.getWidth());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the height of the image.
     * @return
     */
    public int getHeight() {
        return image.getHeight();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get width of the image.
     * @return
     */
    public int getWidth() {
        return image.getWidth();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09SEP2010  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get native image object.
     * @return
     */
    public Object getNativeImage() {
        return image;
    }

    /**
     * Java SE Image object.
     */
    BufferedImage image = null;

}
