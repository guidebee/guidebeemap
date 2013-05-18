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
 * Defines an object used to draw lines and curves.
 * This class cannot be inherited.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 20/04/10
 * @author      Guidebee Pty Ltd.
 */
public final class PenFP {

    /**
     * Specifies a butt line cap.
     */
    public final static int LINECAP_BUTT = 1;

    /**
     * Specifies a round line cap.
     */
    public final static int LINECAP_ROUND = 2;

    /**
     * Specifies a square line cap.
     */
    public final static int LINECAP_SQUARE = 3;

    /**
     * Specifies a mitered join. This produces a sharp corner or a clipped
     * corner, depending on whether the length of the miter exceeds the miter
     * limit.
     */
    public final static int LINEJOIN_MITER = 1;

    /**
     * Specifies a circular join. This produces a smooth, circular arc
     * between the lines.
     */
    public final static int LINEJOIN_ROUND = 2;

    /**
     * Specifies a beveled join. This produces a diagonal corner.
     */
    public final static int LINEJOIN_BEVEL = 3;    //public int Color;


    /**
     * the stroke width of the pen.
     */
    public int width;

    /**
     * the line join for this pen.
     */
    public int lineJoin;

    /**
     * the brush
     */
    public BrushFP brush;

    /**
     * cap style used at the beginning of lines drawn with this Pen.
     */
    public int startCap;

    /**
     * cap style used at the edning of lines drawn with this Pen.
     */
    public int endCap;

    /**
     * the dash Array ,and if dash array is not null,
     *  then startCap = PenFP.LINECAP_BUTT;
     *  endCap = PenFP.LINECAP_BUTT;
     *  and  lineJoin = PenFP.LINEJOIN_BEVEL;
     */
    public int []dashArray=null;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param color the color of this pen.
     */
    public PenFP(int color) {
        this(color, SingleFP.ONE);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor
     * @param color the color of this pen.
     * @param ff_width the width of this pen.
     */
    public PenFP(int color, int ff_width) {
        this(color, ff_width, LINECAP_BUTT, LINECAP_BUTT, LINEJOIN_MITER);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param color   the color of the pen
     * @param ff_width  the width of the pen
     * @param linecap  the start cap style of this pen.
     * @param linejoin the end cap style of this pen.
     */
    public PenFP(int color, int ff_width, int linecap, int linejoin) {
        this(color, ff_width, linecap, linecap, linejoin);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constrcutor.
     * @param brush   the brush.
     * @param ff_width the width of this pen.
     * @param linecap  the start cap style of this pen.
     * @param linejoin the end cap style of this pen.
     */
    public PenFP(BrushFP brush, int ff_width, int linecap, int linejoin) {
        this(brush, ff_width, linecap, linecap, linejoin);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param color  the color.
     * @param ff_width
     * @param startlinecap
     * @param endlinecap
     * @param linejoin
     */
    public PenFP(int color, int ff_width, int startlinecap, int endlinecap,
            int linejoin) {
        this(new SolidBrushFP(color), ff_width, startlinecap, endlinecap,
                linejoin);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param brush
     * @param ff_width
     * @param startlinecap
     * @param endlinecap
     * @param linejoin
     */
    public PenFP(BrushFP brush, int ff_width, int startlinecap,
            int endlinecap, int linejoin) {
        this.brush = brush;
        this.width = ff_width;
        this.startCap = startlinecap;
        this.endCap = endlinecap;
        this.lineJoin = linejoin;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the dash array for this pen.
     * @param dashArray
     * @param offset
     */
    public void setDashArray(int []dashArray,int offset){
        int len=dashArray.length-offset;
        this.dashArray=null;
        if(len>1){
            this.dashArray=new int[len];
            System.arraycopy(dashArray, offset, this.dashArray, 0, len);
        }
    }
}