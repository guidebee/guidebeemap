//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 06MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.util.MathEx;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 06MAY2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class defines an arc specified in {@code Double} precision.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 06/05/10
 * @author      Guidebee Pty Ltd.
 */
public class Arc extends RectangularShape  {

    /**
     * The closure type for an open arc with no path segments
     * connecting the two ends of the arc segment.
     */
    public final static int OPEN = 0;

    /**
     * The closure type for an arc closed by drawing a straight
     * line segment from the start of the arc segment to the end of the
     * arc segment.
     */
    public final static int CHORD = 1;

    /**
     * The closure type for an arc closed by drawing straight line
     * segments from the start of the arc segment to the center
     * of the full ellipse and from that point to the end of the arc segment.
     */
    public final static int PIE = 2;

    /**
     * The X coordinate of the upper-left corner of the framing
     * rectangle of the arc.
     */
    public double x;

    /**
     * The Y coordinate of the upper-left corner of the framing
     * rectangle of the arc.
     */
    public double y;

    /**
     * The overall width of the full ellipse of which this arc is
     * a partial section (not considering the angular extents).
     */
    public double width;

    /**
     * The overall height of the full ellipse of which this arc is
     * a partial section (not considering the angular extents).
     */
    public double height;

    /**
     * The starting angle of the arc in degrees.
     */
    public double start;

    /**
     * The angular extent of the arc in degrees.
     */
    public double extent;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     * <p>
     * This constructor creates an object with a default closure
     * type of {@link #OPEN}.  It is provided only to enable
     * serialization of subclasses.
     */
    public Arc() {
        this(OPEN);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     *
     * @param type The closure type of this arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public Arc(int type) {
	setArcType(type);
    }

    ///////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new arc, initialized to the specified location,
     * size, angular extents, and closure type.
     *
     * @param ellipseBounds The framing rectangle that defines the
     * outer boundary of the full ellipse of which this arc is a
     * partial section.
     * @param start The starting angle of the arc in degrees.
     * @param extent The angular extent of the arc in degrees.
     * @param type The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public Arc(Rectangle ellipseBounds,
                  double start, double extent, int type) {
        this(type);
        this.x = ellipseBounds.getX();
        this.y = ellipseBounds.getY();
        this.width = ellipseBounds.getWidth();
        this.height = ellipseBounds.getHeight();
        this.start = start;
        this.extent = extent;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * Note that the arc
     * <a href="Arc.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     */
    public int getX() {
        return (int)(x+.5);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * Note that the arc
     * <a href="Arc.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     */
    public int getY() {
        return (int)(y+.5);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * Note that the arc
     * <a href="Arc.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     */
    public int getWidth() {
        return (int)(width+.5);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * Note that the arc
     * <a href="Arc.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     */
    public int getHeight() {
        return (int)(height+.5);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the start angle.
     * @return the start angle.
     */
    public double getAngleStart() {
        return start;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the extent angle.
     * @return the angle extent.
     */
    public double getAngleExtent() {
        return extent;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return (width <= 0.0 || height <= 0.0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location, size, angular extents, and closure type of
     * this arc to the specified double values.
     *
     * @param x The X coordinate of the upper-left corner of the arc.
     * @param y The Y coordinate of the upper-left corner of the arc.
     * @param w The overall width of the full ellipse of which
     *          this arc is a partial section.
     * @param h The overall height of the full ellipse of which
     *          this arc is a partial section.
     * @param angSt The starting angle of the arc in degrees.
     * @param angExt The angular extent of the arc in degrees.
     * @param closure The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public void setArc(double x, double y, double w, double h,
                       double angSt, double angExt, int closure) {
        this.setArcType(closure);
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.start = angSt;
        this.extent = angExt;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the starting angle of this arc to the specified double
     * value.
     *
     * @param angSt The starting angle of the arc in degrees.
     */
    public void setAngleStart(double angSt) {
        this.start = angSt;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the angular extent of this arc to the specified double
     * value.
     *
     * @param angExt The angular extent of the arc in degrees.
     */
    public void setAngleExtent(double angExt) {
        this.extent = angExt;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a <code>Rectangle</code> of the appropriate precision
     * to hold the parameters calculated to be the framing rectangle
     * of this arc.
     *
     * @param x The X coordinate of the upper-left corner of the
     * framing rectangle.
     * @param y The Y coordinate of the upper-left corner of the
     * framing rectangle.
     * @param w The width of the framing rectangle.
     * @param h The height of the framing rectangle.
     * @return a <code>Rectangle</code> that is the framing rectangle
     *     of this arc.
     */
    protected Rectangle makeBounds(double x, double y,
                                     double w, double h) {
        return new Rectangle((int)(x+.5),
                (int)(y+.5),
                (int)(w+.5),
                (int)(h+.5));

    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new arc, initialized to the specified location,
     * size, angular extents, and closure type.
     *
     * @param x The X coordinate of the upper-left corner
     *          of the arc's framing rectangle.
     * @param y The Y coordinate of the upper-left corner
     *          of the arc's framing rectangle.
     * @param w The overall width of the full ellipse of which this
     *          arc is a partial section.
     * @param h The overall height of the full ellipse of which this
     *          arc is a partial section.
     * @param start The starting angle of the arc in degrees.
     * @param extent The angular extent of the arc in degrees.
     * @param type The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public Arc(double x, double y, double w, double h,
                  double start, double extent, int type) {
        this(type);
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.start = start;
        this.extent = extent;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the arc closure type of the arc: {@link #OPEN},
     * {@link #CHORD}, or {@link #PIE}.
     * @return One of the integer constant closure types defined
     * in this class.
     */
    public int getArcType() {
	return type;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the starting point of the arc.  This point is the
     * intersection of the ray from the center defined by the
     * starting angle and the elliptical boundary of the arc.
     *
     * @return A <CODE>Point</CODE> object representing the
     * x,y coordinates of the starting point of the arc.
     */
    public Point getStartPoint() {
	double angle = MathEx.toRadians(-getAngleStart());
	double xp = getX() + (MathEx.cos(angle) * 0.5 + 0.5) * getWidth();
	double yp = getY() + (MathEx.sin(angle) * 0.5 + 0.5) * getHeight();
	return new Point((int)(xp+.5),(int)(yp+.5));
    }

     ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the ending point of the arc.  This point is the
     * intersection of the ray from the center defined by the
     * starting angle plus the angular extent of the arc and the
     * elliptical boundary of the arc.
     *
     * @return A <CODE>Point</CODE> object representing the
     * x,y coordinates  of the ending point of the arc.
     */
    public Point getEndPoint() {
	double angle = MathEx.toRadians(-getAngleStart() - getAngleExtent());
	double xp = getX() + (MathEx.cos(angle) * 0.5 + 0.5) * getWidth();
	double yp = getY() + (MathEx.sin(angle) * 0.5 + 0.5) * getHeight();
	return new Point((int)(xp+.5),(int)(yp+.5));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location, size, angular extents, and closure type of
     * this arc to the specified values.
     *
     * @param loc The <CODE>Point</CODE> representing the coordinates of
     * the upper-left corner of the arc.
     * @param size The <CODE>Dimension</CODE> representing the width
     * and height of the full ellipse of which this arc is
     * a partial section.
     * @param angSt The starting angle of the arc in degrees.
     * @param angExt The angular extent of the arc in degrees.
     * @param closure The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public void setArc(Point loc, Dimension size,
		       double angSt, double angExt, int closure) {
	setArc(loc.getX(), loc.getY(), size.getWidth(), size.getHeight(),
	       angSt, angExt, closure);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location, size, angular extents, and closure type of
     * this arc to the specified values.
     *
     * @param rect The framing rectangle that defines the
     * outer boundary of the full ellipse of which this arc is a
     * partial section.
     * @param angSt The starting angle of the arc in degrees.
     * @param angExt The angular extent of the arc in degrees.
     * @param closure The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public void setArc(Rectangle rect, double angSt, double angExt,
		       int closure) {
	setArc(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(),
	       angSt, angExt, closure);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets this arc to be the same as the specified arc.
     *
     * @param a The <CODE>Arc</CODE> to use to set the arc's values.
     */
    public void setArc(Arc a) {
	setArc(a.getX(), a.getY(), a.getWidth(), a.getHeight(),
	       a.getAngleStart(), a.getAngleExtent(), a.type);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the position, bounds, angular extents, and closure type of
     * this arc to the specified values. The arc is defined by a center
     * point and a radius rather than a framing rectangle for the full ellipse.
     *
     * @param x The X coordinate of the center of the arc.
     * @param y The Y coordinate of the center of the arc.
     * @param radius The radius of the arc.
     * @param angSt The starting angle of the arc in degrees.
     * @param angExt The angular extent of the arc in degrees.
     * @param closure The closure type for the arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     */
    public void setArcByCenter(double x, double y, double radius,
			       double angSt, double angExt, int closure) {
	setArc(x - radius, y - radius, radius * 2.0, radius * 2.0,
	       angSt, angExt, closure);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the position, bounds, and angular extents of this arc to the
     * specified value. The starting angle of the arc is tangent to the
     * line specified by points (p1, p2), the ending angle is tangent to
     * the line specified by points (p2, p3), and the arc has the
     * specified radius.
     *
     * @param p1 The first point that defines the arc. The starting
     * angle of the arc is tangent to the line specified by points (p1, p2).
     * @param p2 The second point that defines the arc. The starting
     * angle of the arc is tangent to the line specified by points (p1, p2).
     * The ending angle of the arc is tangent to the line specified by
     * points (p2, p3).
     * @param p3 The third point that defines the arc. The ending angle
     * of the arc is tangent to the line specified by points (p2, p3).
     * @param radius The radius of the arc.
     */
    public void setArcByTangent(Point p1, Point p2, Point p3,
				double radius) {
	double ang1 = MathEx.atan2(p1.getY() - p2.getY(),
				 p1.getX() - p2.getX());
	double ang2 = MathEx.atan2(p3.getY() - p2.getY(),
				 p3.getX() - p2.getX());
	double diff = ang2 - ang1;
	if (diff > MathEx.PI) {
	    ang2 -= MathEx.PI * 2.0;
	} else if (diff < -MathEx.PI) {
	    ang2 += MathEx.PI * 2.0;
	}
	double bisect = (ang1 + ang2) / 2.0;
	double theta = MathEx.abs(ang2 - bisect);
	double dist = radius / MathEx.sin(theta);
	double xp = p2.getX() + dist * MathEx.cos(bisect);
	double yp = p2.getY() + dist * MathEx.sin(bisect);
	// REMIND: This needs some work...
	if (ang1 < ang2) {
	    ang1 -= MathEx.PI / 2.0;
	    ang2 += MathEx.PI / 2.0;
	} else {
	    ang1 += MathEx.PI / 2.0;
	    ang2 -= MathEx.PI / 2.0;
	}
	ang1 = MathEx.toDegrees(-ang1);
	ang2 = MathEx.toDegrees(-ang2);
	diff = ang2 - ang1;
	if (diff < 0) {
	    diff += 360;
	} else {
	    diff -= 360;
	}
	setArcByCenter(xp, yp, radius, ang1, diff, type);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the starting angle of this arc to the angle that the
     * specified point defines relative to the center of this arc.
     * The angular extent of the arc will remain the same.
     *
     * @param p The <CODE>Point</CODE> that defines the starting angle.
     */
    public void setAngleStart(Point p) {
	// Bias the dx and dy by the height and width of the oval.
	double dx = getHeight() * (p.getX() - getCenterX());
	double dy = getWidth() * (p.getY() - getCenterY());
	setAngleStart(-MathEx.toDegrees(MathEx.atan2(dy, dx)));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the starting angle and angular extent of this arc using two
     * sets of coordinates. The first set of coordinates is used to
     * determine the angle of the starting point relative to the arc's
     * center. The second set of coordinates is used to determine the
     * angle of the end point relative to the arc's center.
     * The arc will always be non-empty and extend counterclockwise
     * from the first point around to the second point.
     *
     * @param x1 The X coordinate of the arc's starting point.
     * @param y1 The Y coordinate of the arc's starting point.
     * @param x2 The X coordinate of the arc's ending point.
     * @param y2 The Y coordinate of the arc's ending point.
     */
    public void setAngles(double x1, double y1, double x2, double y2) {
	double xp = getCenterX();
	double yp = getCenterY();
	double w = getWidth();
	double h = getHeight();
	// Note: reversing the Y equations negates the angle to adjust
	// for the upside down coordinate system.
	// Also we should bias atans by the height and width of the oval.
	double ang1 = MathEx.atan2(w * (yp - y1), h * (x1 - xp));
	double ang2 = MathEx.atan2(w * (yp - y2), h * (x2 - xp));
	ang2 -= ang1;
	if (ang2 <= 0.0) {
	    ang2 += MathEx.PI * 2.0;
	}
	setAngleStart(MathEx.toDegrees(ang1));
	setAngleExtent(MathEx.toDegrees(ang2));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the starting angle and angular extent of this arc using
     * two points. The first point is used to determine the angle of
     * the starting point relative to the arc's center.
     * The second point is used to determine the angle of the end point
     * relative to the arc's center.
     * The arc will always be non-empty and extend counterclockwise
     * from the first point around to the second point.
     *
     * @param p1 The <CODE>Point</CODE> that defines the arc's
     * starting point.
     * @param p2 The <CODE>Point</CODE> that defines the arc's
     * ending point.
     */
    public void setAngles(Point p1, Point p2) {
	setAngles(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the closure type of this arc to the specified value:
     * <CODE>OPEN</CODE>, <CODE>CHORD</CODE>, or <CODE>PIE</CODE>.
     *
     * @param type The integer constant that represents the closure
     * type of this arc: {@link #OPEN}, {@link #CHORD}, or
     * {@link #PIE}.
     *
     * @throws IllegalArgumentException if <code>type</code> is not
     * 0, 1, or 2.+
     */
    public void setArcType(int type) {
	if (type < OPEN || type > PIE) {
	    throw new IllegalArgumentException("invalid type for Arc: "+type);
	}
	this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * {@inheritDoc}
     * Note that the arc
     * <a href="Arc.html#inscribes">partially inscribes</a>
     * the framing rectangle of this {@code RectangularShape}.
     */
    public void setFrame(int x, int y, int w, int h) {
	setArc(x, y, w, h, getAngleStart(), getAngleExtent(), type);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the high-precision framing rectangle of the arc.  The framing
     * rectangle contains only the part of this <code>Arc</code> that is
     * in between the starting and ending angles and contains the pie
     * wedge, if this <code>Arc</code> has a <code>PIE</code> closure type.
     * <p>
     * This method differs from the
     * {@link RectangularShape#getBounds() getBounds} in that the
     * <code>getBounds</code> method only returns the bounds of the
     * enclosing ellipse of this <code>Arc</code> without considering
     * the starting and ending angles of this <code>Arc</code>.
     *
     * @return the <CODE>Rectangle</CODE> that represents the arc's
     * framing rectangle.
     */
    public Rectangle getBounds() {
	if (isEmpty()) {
	    return makeBounds(getX(), getY(), getWidth(), getHeight());
	}
	double x1, y1, x2, y2;
	if (getArcType() == PIE) {
	    x1 = y1 = x2 = y2 = 0.0;
	} else {
	    x1 = y1 = 1.0;
	    x2 = y2 = -1.0;
	}
	double angle = 0.0;
	for (int i = 0; i < 6; i++) {
	    if (i < 4) {
		// 0-3 are the four quadrants
		angle += 90.0;
		if (!containsAngle(angle)) {
		    continue;
		}
	    } else if (i == 4) {
		// 4 is start angle
		angle = getAngleStart();
	    } else {
		// 5 is end angle
		angle += getAngleExtent();
	    }
	    double rads = MathEx.toRadians(-angle);
	    double xe = MathEx.cos(rads);
	    double ye = MathEx.sin(rads);
	    x1 = MathEx.min(x1, xe);
	    y1 = MathEx.min(y1, ye);
	    x2 = MathEx.max(x2, xe);
	    y2 = MathEx.max(y2, ye);
	}
	double w = getWidth();
	double h = getHeight();
	x2 = (x2 - x1) * 0.5 * w;
	y2 = (y2 - y1) * 0.5 * h;
	x1 = getX() + (x1 * 0.5 + 0.5) * w;
	y1 = getY() + (y1 * 0.5 + 0.5) * h;
	return makeBounds(x1, y1, x2, y2);
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the specified angle is within the
     * angular extents of the arc.
     *
     * @param angle The angle to test.
     *
     * @return <CODE>true</CODE> if the arc contains the angle,
     * <CODE>false</CODE> if the arc doesn't contain the angle.
     */
    public boolean containsAngle(double angle) {
	double angExt = getAngleExtent();
	boolean backwards = (angExt < 0.0);
	if (backwards) {
	    angExt = -angExt;
	}
	if (angExt >= 360.0) {
	    return true;
	}
	angle = normalizeDegrees(angle) - normalizeDegrees(getAngleStart());
	if (backwards) {
	    angle = -angle;
	}
	if (angle < 0.0) {
	    angle += 360.0;
	}


	return (angle >= 0.0) && (angle < angExt);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the specified point is inside the boundary
     * of the arc.
     *
     * @param x The X coordinate of the point to test.
     * @param y The Y coordinate of the point to test.
     *
     * @return <CODE>true</CODE> if the point lies within the bound of
     * the arc, <CODE>false</CODE> if the point lies outside of the
     * arc's bounds.
     */
    public boolean contains(int x, int y) {
	// Normalize the coordinates compared to the ellipse
	// having a center at 0,0 and a radius of 0.5.
	double ellw = getWidth();
	if (ellw <= 0.0) {
	    return false;
	}
	double normx = (x - getX()) / ellw - 0.5;
	double ellh = getHeight();
	if (ellh <= 0.0) {
	    return false;
	}
	double normy = (y - getY()) / ellh - 0.5;
	double distSq = (normx * normx + normy * normy);
	if (distSq >= 0.25) {
	    return false;
	}
	double angExt = MathEx.abs(getAngleExtent());
	if (angExt >= 360.0) {
	    return true;
	}
	boolean inarc = containsAngle(-MathEx.toDegrees(MathEx.atan2(normy,
								 normx)));
	if (type == PIE) {
	    return inarc;
	}
	// CHORD and OPEN behave the same way
	if (inarc) {
	    if (angExt >= 180.0) {
		return true;
	    }
	    // point must be outside the "pie triangle"
	} else {
	    if (angExt <= 180.0) {
		return false;
	    }
	    // point must be inside the "pie triangle"
	}
	// The point is inside the pie triangle iff it is on the same
	// side of the line connecting the ends of the arc as the center.
	double angle = MathEx.toRadians(-getAngleStart());
	double x1 = MathEx.cos(angle);
	double y1 = MathEx.sin(angle);
	angle += MathEx.toRadians(-getAngleExtent());
	double x2 = MathEx.cos(angle);
	double y2 = MathEx.sin(angle);
	boolean inside = (Line.relativeCCW((int)(x1+.5),
                 (int)(y1+.5),
                 (int)(x2+.5),
                 (int)(y2+.5),
                 (int)(2*normx+.5),
                 (int)(2*normy+.5)) *
			  Line.relativeCCW((int)(x1+.5),
                          (int)(y1+.5),
                          (int)(x2+.5),
                          (int)(y2+.5), 0, 0) >= 0);
	return inarc ? !inside : inside;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the interior of the arc intersects
     * the interior of the specified rectangle.
     *
     * @param x The X coordinate of the rectangle's upper-left corner.
     * @param y The Y coordinate of the rectangle's upper-left corner.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     *
     * @return <CODE>true</CODE> if the arc intersects the rectangle,
     * <CODE>false</CODE> if the arc doesn't intersect the rectangle.
     */
    public boolean intersects(int x, int y, int w, int h) {

	double aw = getWidth();
	double ah = getHeight();

	if ( w <= 0 || h <= 0 || aw <= 0 || ah <= 0 ) {
	    return false;
	}
	double ext = getAngleExtent();
	if (ext == 0) {
	    return false;
	}

	double ax  = getX();
	double ay  = getY();
	double axw = ax + aw;
	double ayh = ay + ah;
	double xw  = x + w;
	double yh  = y + h;

	// check bbox
	if (x >= axw || y >= ayh || xw <= ax || yh <= ay) {
	    return false;
	}

	// extract necessary data
	double axc = getCenterX();
	double ayc = getCenterY();
	Point sp = getStartPoint();
	Point ep = getEndPoint();
	double sx = sp.getX();
	double sy = sp.getY();
	double ex = ep.getX();
	double ey = ep.getY();

	/*
	 * Try to catch rectangles that intersect arc in areas
	 * outside of rectagle with left top corner coordinates
	 * (min(center x, start point x, end point x),
	 *  min(center y, start point y, end point y))
	 * and rigth bottom corner coordinates
	 * (max(center x, start point x, end point x),
	 *  max(center y, start point y, end point y)).
	 * So we'll check axis segments outside of rectangle above.
	 */
	if (ayc >= y && ayc <= yh) { // 0 and 180
	    if ((sx < xw && ex < xw && axc < xw &&
	         axw > x && containsAngle(0)) ||
	        (sx > x && ex > x && axc > x &&
	         ax < xw && containsAngle(180))) {
		return true;
	    }
	}
	if (axc >= x && axc <= xw) { // 90 and 270
	    if ((sy > y && ey > y && ayc > y &&
	         ay < yh && containsAngle(90)) ||
	        (sy < yh && ey < yh && ayc < yh &&
	         ayh > y && containsAngle(270))) {
		return true;
	    }
	}

	/*
	 * For PIE we should check intersection with pie slices;
	 * also we should do the same for arcs with extent is greater
	 * than 180, because we should cover case of rectangle, which
	 * situated between center of arc and chord, but does not
	 * intersect the chord.
	 */
	Rectangle rect = new Rectangle((int)(x+.5),
                (int)(y+.5),
                (int)(w+.5),
                (int)(h+.5));
	if (type == PIE || MathEx.abs(ext) > 180) {
	    // for PIE: try to find intersections with pie slices
	    if (rect.intersectsLine((int)(axc+.5),
                    (int)(ayc+.5),
                    (int)(sx+.5),
                    (int)(sy+.5)) ||
		rect.intersectsLine((int)(axc+.5),
                (int)(ayc+.5),
                (int)(ex+.5),
                (int)(ey+.5))) {
		return true;
	    }
	} else {
	    // for CHORD and OPEN: try to find intersections with chord
	    if (rect.intersectsLine((int)(sx+.5),
                    (int)(sy+.5), (int)(ex+.5),
                    (int)(ey+.5))) {
		return true;
	    }
	}

	// finally check the rectangle corners inside the arc
	if (contains(x, y) || contains(x + w, y) ||
	    contains(x, y + h) || contains(x + w, y + h)) {
	    return true;
	}

	return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the interior of the arc entirely contains
     * the specified rectangle.
     *
     * @param x The X coordinate of the rectangle's upper-left corner.
     * @param y The Y coordinate of the rectangle's upper-left corner.
     * @param w The width of the rectangle.
     * @param h The height of the rectangle.
     *
     * @return <CODE>true</CODE> if the arc contains the rectangle,
     * <CODE>false</CODE> if the arc doesn't contain the rectangle.
     */
    public boolean contains(int x, int y, int w, int h) {
	return contains(x, y, w, h, null);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the interior of the arc entirely contains
     * the specified rectangle.
     *
     * @param r The <CODE>Rectangle</CODE> to test.
     *
     * @return <CODE>true</CODE> if the arc contains the rectangle,
     * <CODE>false</CODE> if the arc doesn't contain the rectangle.
     */
    public boolean contains(Rectangle r) {
	return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight(), r);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns an iteration object that defines the boundary of the
     * arc.
     * This iterator is multithread safe.
     * <code>Arc</code> guarantees that
     * modifications to the geometry of the arc
     * do not affect any iterations of that geometry that
     * are already in process.
     *
     * @param at an optional <CODE>AffineTransform</CODE> to be applied
     * to the coordinates as they are returned in the iteration, or null
     * if the untransformed coordinates are desired.
     *
     * @return A <CODE>IPathIterator</CODE> that defines the arc's boundary.
     */
    public IPathIterator getPathIterator(AffineTransform at) {
	return new ArcIterator(this, at);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>Arc</code>.
     * @return the hashcode for this <code>Arc</code>.
     */
    public int hashCode() {
        long bits = Double.doubleToLongBits(getX());
        bits += Double.doubleToLongBits(getY()) * 37;
        bits += Double.doubleToLongBits(getWidth()) * 43;
        bits += Double.doubleToLongBits(getHeight()) * 47;
        bits += Double.doubleToLongBits(getAngleStart()) * 53;
        bits += Double.doubleToLongBits(getAngleExtent()) * 59;
        bits += getArcType() * 61;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not the specified <code>Object</code> is
     * equal to this <code>Arc</code>.  The specified
     * <code>Object</code> is equal to this <code>Arc</code>
     * if it is an instance of <code>Arc</code> and if its
     * location, size, arc extents and type are the same as this
     * <code>Arc</code>.
     * @param obj  an <code>Object</code> to be compared with this
     *             <code>Arc</code>.
     * @return  <code>true</code> if <code>obj</code> is an instance
     *          of <code>Arc</code> and has the same values;
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Arc) {
            Arc a2d = (Arc) obj;
            return ((getX() == a2d.getX()) &&
                    (getY() == a2d.getY()) &&
                    (getWidth() == a2d.getWidth()) &&
                    (getHeight() == a2d.getHeight()) &&
                    (getAngleStart() == a2d.getAngleStart()) &&
                    (getAngleExtent() == a2d.getAngleExtent()) &&
                    (getArcType() == a2d.getArcType()));
        }
        return false;
    }

    private int type;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param origrect
     * @return
     */
    private boolean contains(int x, int y, int w, int h,
			     Rectangle origrect) {
	if (!(contains(x, y) &&
	      contains(x + w, y) &&
	      contains(x, y + h) &&
	      contains(x + w, y + h))) {
	    return false;
	}
	// If the shape is convex then we have done all the testing
	// we need.  Only PIE arcs can be concave and then only if
	// the angular extents are greater than 180 degrees.
	if (type != PIE || MathEx.abs(getAngleExtent()) <= 180.0) {
	    return true;
	}
	// For a PIE shape we have an additional test for the case where
	// the angular extents are greater than 180 degrees and all four
	// rectangular corners are inside the shape but one of the
	// rectangle edges spans across the "missing wedge" of the arc.
	// We can test for this case by checking if the rectangle intersects
	// either of the pie angle segments.
	if (origrect == null) {
	    origrect = new Rectangle((int)(x+.5),
                    (int)(y+.5),
                    (int)(w+.5),
                    (int)(h+.5));
	}
	double halfW = getWidth() / 2.0;
	double halfH = getHeight() / 2.0;
	double xc = getX() + halfW;
	double yc = getY() + halfH;
	double angle = MathEx.toRadians(-getAngleStart());
	double xe = xc + halfW * MathEx.cos(angle);
	double ye = yc + halfH * MathEx.sin(angle);
	if (origrect.intersectsLine((int)(xc+.5),
                (int)(yc+.5),
                (int)(xe+.5),
                (int)(ye+.5))) {
	    return false;
	}
	angle += MathEx.toRadians(-getAngleExtent());
	xe = xc + halfW * MathEx.cos(angle);
	ye = yc + halfH * MathEx.sin(angle);
	return !origrect.intersectsLine((int)(xc+.5),
                (int)(yc+.5),
                (int)(xe+.5),
                (int)(ye+.5));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 06MAY2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /*
     * Normalizes the specified angle into the range -180 to 180.
     */
    static double normalizeDegrees(double angle) {
	if (angle > 180.0) {
	    if (angle <= (180.0 + 360.0)) {
		angle = angle - 360.0;
	    } else {
		angle = MathEx.IEEEremainder(angle, 360.0);
		// IEEEremainder can return -180 here for some input values...
		if (angle == -180.0) {
		    angle = 180.0;
		}
	    }
	} else if (angle <= -180.0) {
	    if (angle > (-180.0 - 360.0)) {
		angle = angle + 360.0;
	    } else {
		angle = MathEx.IEEEremainder(angle, 360.0);
		// IEEEremainder can return -180 here for some input values...
		if (angle == -180.0) {
		    angle = 180.0;
		}
	    }
	}
	return angle;
    }
}