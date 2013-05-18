//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.raster;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.drawing.IImage;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * one map tile is downloaded.
 * <p>
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd.
 */
interface IMapTileReadyListener{

    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * indicate one map tile downloading is done.
     * @param imageTileIndex the index for givn map tile
     * @param image the image of given map tile.
     */
    void done(ImageTileIndex imageTileIndex,IImage image);
}
