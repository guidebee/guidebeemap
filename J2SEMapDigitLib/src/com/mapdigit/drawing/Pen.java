//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.collections.Arrays;

import com.mapdigit.drawing.core.PenFP;
import com.mapdigit.drawing.core.SingleFP;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>Pen</code> class defines a basic set of rendering
 * attributes for the outlines of graphics primitives, which are rendered
 * with a {@link Graphics2D} object that has its Stroke attribute set to
 * this <code>Pen</code>.
 * The rendering attributes defined by <code>Pen</code> describe
 * the shape of the mark made by a pen drawn along the outline of a
 * IShape and the decorations applied at the ends and joins of
 * path segments of the <code>IShape</code>.
 * These rendering attributes include:
 * <dl compact>
 * <dt><i>width</i>
 * <dd>The pen width, measured perpendicularly to the pen trajectory.
 * <dt><i>end caps</i>
 * <dd>The decoration applied to the ends of unclosed subpaths and
 * dash segments.  Subpaths that start and end on the same point are
 * still considered unclosed if they do not have a CLOSE segment.
 * <dd>The limit to trim a line join that has a JOIN_MITER decoration.
 * A line join is trimmed when the ratio of miter length to stroke
 * width is greater than the miterlimit value.  The miter length is
 * the diagonal length of the miter, which is the distance between
 * the inside corner and the outside corner of the intersection.
 * The smaller the angle formed by two line segments, the longer
 * the miter length and the sharper the angle of intersection.  The
 * default miterlimit value of 10 causes all angles less than
 * 11 degrees to be trimmed.  Trimming miters converts
 * the decoration of the line join to bevel.
 * <dt><i>dash attributes</i>
 * <dd>The definition of how to make a dash pattern by alternating
 * between opaque and transparent sections.
 * </dl>
 * For more information on the user space coordinate system and the
 * rendering process, see the <code>Graphics2D</code> class comments.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 21/04/10
 * @author      Guidebee Pty Ltd.
 */
public class Pen {

    //[------------------------------ CONSTANTS -------------------------------]
    /**
     * Joins path segments by extending their outside edges until
     * they meet.
     */
    public final static int JOIN_MITER = PenFP.LINEJOIN_MITER;
    /**
     * Joins path segments by rounding off the corner at a radius
     * of half the line width.
     */
    public final static int JOIN_ROUND = PenFP.LINEJOIN_ROUND;
    /**
     * Joins path segments by connecting the outer corners of their
     * wide outlines with a straight segment.
     */
    public final static int JOIN_BEVEL = PenFP.LINEJOIN_BEVEL;
    /**
     * Ends unclosed subpaths and dash segments with no added
     * decoration.
     */
    public final static int CAP_BUTT = PenFP.LINECAP_BUTT;
    /**
     * Ends unclosed subpaths and dash segments with a round
     * decoration that has a radius equal to half of the width
     * of the pen.
     */
    public final static int CAP_ROUND = PenFP.LINECAP_ROUND;
    /**
     * Ends unclosed subpaths and dash segments with a square
     * projection that extends beyond the end of the segment
     * to a distance equal to half of the line width.
     */
    public final static int CAP_SQUARE = PenFP.LINECAP_SQUARE;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>Pen</code> with the specified
     * attributes.
     * @param brush brush used to construct the pen object.
     * @param width the width of this <code>Pen</code>.  The
     *         width must be greater than or equal to 0.  If width is
     *         set to 0, the stroke is rendered as the thinnest
     *         possible line for the target device and the antialias
     *         hint setting.
     * @param cap the decoration of the ends of a <code>Pen</code>
     * @param join the decoration applied where path segments meet
     * @param dash the array representing the dashing pattern
     * @param dash_phase the offset to start the dashing pattern
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public Pen(Brush brush, int width, int cap, int join,
            int dash[], int dash_phase) {

        this(Color.BLACK, width, cap, join, dash, dash_phase);
        this.brush = brush;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>Pen</code> with the specified
     * attributes.
     * @param color the color of the pen.
     * @param width the width of this <code>Pen</code>.  The
     *         width must be greater than or equal to 0.  If width is
     *         set to 0, the stroke is rendered as the thinnest
     *         possible line for the target device and the antialias
     *         hint setting.
     * @param cap the decoration of the ends of a <code>Pen</code>
     * @param join the decoration applied where path segments meet
     * @param dash the array representing the dashing pattern
     * @param dash_phase the offset to start the dashing pattern
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public Pen(Color color, int width, int cap, int join,
            int dash[], int dash_phase) {
        if (width < 0) {
            throw new IllegalArgumentException("negative width");
        }
        if (cap != CAP_BUTT && cap != CAP_ROUND && cap != CAP_SQUARE) {
            throw new IllegalArgumentException("illegal end cap value");
        }
        if (join != JOIN_ROUND && join != JOIN_BEVEL && join != JOIN_MITER) {
            throw new IllegalArgumentException("illegal line join value");
        }
        if (dash != null) {
            if (dash_phase < 0) {
                throw new IllegalArgumentException("negative dash phase");
            }
            boolean allzero = true;
            for (int i = 0; i < dash.length; i++) {
                int d = dash[i];
                if (d > 0) {
                    allzero = false;
                } else if (d < 0) {
                    throw new IllegalArgumentException("negative dash length");
                }
            }
            if (allzero) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
        }
        this.width = width;
        this.cap = cap;
        this.join = join;
        this.color = color;
        if (dash != null) {
            this.dash = dash;
        }

        this.dash_phase = dash_phase;
        this.wrappedPenFP = new PenFP(color.value, width << SingleFP.DECIMAL_BITS,
                cap, cap, join);
        if (dash != null) {
            int[] newDash = new int[dash.length];
            for (int i = 0; i < newDash.length; i++) {
                newDash[i] = dash[i] << SingleFP.DECIMAL_BITS;
            }
            this.wrappedPenFP.setDashArray(newDash, dash_phase);
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a solid <code>Pen</code> with the specified
     * attributes.
     * @param brush brush used to create the pen.
     * @param width the width of the <code>Pen</code>
     * @param cap the decoration of the ends of a <code>Pen</code>
     * @param join the decoration applied where path segments meet
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public Pen(Brush brush, int width, int cap, int join) {
        this(brush, width, cap, join, null, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a solid <code>Pen</code> with the specified
     * attributes.
     * @param color the color of the <code>Pen</code>
     * @param width the width of the <code>Pen</code>
     * @param cap the decoration of the ends of a <code>Pen</code>
     * @param join the decoration applied where path segments meet
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public Pen(Color color, int width, int cap, int join) {
        this(color, width, cap, join, null, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a solid <code>Pen</code> with the specified
     * line width and with default values for the cap and join
     * styles.
     * @param brush the brush used to create the pen.
     * @param width the width of the <code>Pen</code>
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public Pen(Brush brush, int width) {
        this(brush, width, CAP_SQUARE, JOIN_MITER, null, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a solid <code>Pen</code> with the specified
     * line width and with default values for the cap and join
     * styles.
     * @param color the color of the <code>Pen</code>
     * @param width the width of the <code>Pen</code>
     * @throws IllegalArgumentException if <code>width</code> is negative
     */
    public Pen(Color color, int width) {
        this(color, width, CAP_SQUARE, JOIN_MITER, null, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>Pen</code> with defaults for all
     * attributes.
     * The default attributes are a solid line of width 1.0, CAP_SQUARE,
     * JOIN_MITER, a miter limit of 10.
     * @param brush brush used to create the pen.
     */
    public Pen(Brush brush) {
        this(brush, 1, CAP_SQUARE, JOIN_MITER, null, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new <code>Pen</code> with defaults for all
     * attributes.
     * The default attributes are a solid line of width 1.0, CAP_SQUARE,
     * JOIN_MITER, a miter limit of 10.
     * @param color the color of the <code>Pen</code>
     */
    public Pen(Color color) {
        this(color, 1, CAP_SQUARE, JOIN_MITER, null, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the line width.  Line width is represented in user space,
     * which is the default-coordinate space used by Java 2D.  See the
     * <code>Graphics2D</code> class comments for more information on
     * the user space coordinate system.
     * @return the line width of this <code>Pen</code>.
     */
    public int getLineWidth() {
        return width;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the pen color.
     * @return the color of this <code>Pen</code>.
     */
    public Color getPenColor() {
        return color;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the end cap style.
     * @return the end cap style of this <code>Pen</code> as one
     * of the static <code>int</code> values that define possible end cap
     * styles.
     */
    public int getEndCap() {
        return cap;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the line join style.
     * @return the line join style of the <code>Pen</code> as one
     * of the static <code>int</code> values that define possible line
     * join styles.
     */
    public int getLineJoin() {
        return join;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the array representing the lengths of the dash segments.
     * Alternate entries in the array represent the user space lengths
     * of the opaque and transparent segments of the dashes.
     * As the pen moves along the outline of the <code>IShape</code>
     * to be stroked, the user space
     * distance that the pen travels is accumulated.  The distance
     * value is used to index into the dash array.
     * The pen is opaque when its current cumulative distance maps
     * to an even element of the dash array and transparent otherwise.
     * @return the dash array.
     */
    public int[] getDashArray() {
        if (dash == null) {
            return null;
        }
        return dash;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the current dash phase.
     * The dash phase is a distance specified in user coordinates that
     * represents an offset into the dashing pattern. In other words, the dash
     * phase defines the point in the dashing pattern that will correspond to
     * the beginning of the stroke.
     * @return the dash phase as a <code>int</code> value.
     */
    public int getDashPhase() {
        return dash_phase;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this stroke.
     * @return      a hash code for this stroke.
     */
    public int hashCode() {

        int hash = Float.floatToIntBits(width);
        hash = hash * 31 + join;
        hash = hash * 31 + cap;
        if (dash != null) {
            hash = hash * 31 + Float.floatToIntBits(dash_phase);
            for (int i = 0; i < dash.length; i++) {
                hash = hash * 31 + Float.floatToIntBits(dash[i]);
            }
        }
        return hash;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this Pen represents the same
     * stroking operation as the given argument.
     */
    /**
     * Tests if a specified object is equal to this <code>Pen</code>
     * by first testing if it is a <code>Pen</code> and then comparing
     * its width, join, cap, miter limit, dash, and dash phase attributes with
     * those of this <code>Pen</code>.
     * @param  obj the specified object to compare to this
     *              <code>Pen</code>
     * @return <code>true</code> if the width, join, cap, miter limit, dash, and
     *            dash phase are the same for both objects;
     *            <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Pen)) {
            return false;
        }

        Pen bs = (Pen) obj;
        if (width != bs.width) {
            return false;
        }

        if (join != bs.join) {
            return false;
        }

        if (cap != bs.cap) {
            return false;
        }


        if (color != bs.color) {
            return false;
        }

        if (dash != null) {
            if (dash_phase != bs.dash_phase) {
                return false;
            }

            if (!Arrays.equals(dash, bs.dash)) {
                return false;
            }
        } else if (bs.dash != null) {
            return false;
        }

        return true;
    }

    int width;
    int join;
    int cap;
    int dash[];
    int dash_phase;
    Color color;
    Brush brush = null;
    PenFP wrappedPenFP = null;
}
