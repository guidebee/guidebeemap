//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY 
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 01NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------

import com.mapdigit.drawing.geometry.parser.NumberListParser;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 01NOV2008  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * A point representing a location in {@code (x,y)} coordinate space,
 * specified in integer.
 *
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 01/11/08
 * @author      Guidebee Pty Ltd.
 */
public class Point {

    /**
     * The X coordinate of this <code>Point</code>.
     * If no X coordinate is set it will default to 0.
     */
    public int x;

    /**
     * The Y coordinate of this <code>Point</code>. 
     * If no Y coordinate is set it will default to 0.
     */
    public int y;

   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the origin 
     * (0,&nbsp;0) of the coordinate space. 
     */
    public Point() {
	this(0, 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point with the same location as
     * the specified <code>Point</code> object.
     * @param       p a point
     */
    public Point(Point p) {
	this.x=p.x;
        this.y=p.y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs and initializes a point at the specified 
     * {@code (x,y)} location in the coordinate space. 
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     */
    public Point(int x, int y) {
	this.x = x;
	this.y = y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * parse point from a string.
     * @param input
     * @return a array of point
     */
    public static Point[] fromString(String input) {
        synchronized (numberListParser) {
            float[] coords = numberListParser.parseNumberList(input);
            int length = coords.length / 2;
            Point[] points=new Point[length];
            if (length >= 2) {
                for (int i = 0; i < length; i++) {
                    points[i]=new Point();
                    points[i].x=(int) coords[i * 2];
                    points[i].y=(int) coords[i * 2 + 1];
                }
                return points;
            } else {
                return null;
            }

        }
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the X coordinate of this <code>Point</code>.
     * @return the X coordinate of this <code>Point</code>.
     */
    public int getX() {
	return x;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the Y coordinate of this <code>Point</code>.
     * @return the Y coordinate of this <code>Point</code>.
     */
    public int getY() {
	return y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the location of this point.
     * This method is included for completeness, to parallel the
     * <code>getLocation</code> method of <code>Component</code>.
     * @return      a copy of this point, at the same location
     */
    public Point getLocation() {
        return new Point(x, y);
    }	

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the location of the point to the specified location.
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * @param       p  a point, the new location for this point
     */
    public void setLocation(Point p) {
	this.x=p.x;
        this.y=p.y;
    }	

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Changes the point to have the specified location.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * Its behavior is identical with <code>move(int,&nbsp;int)</code>.
     * @param       x the X coordinate of the new location
     * @param       y the Y coordinate of the new location
     */
    public void setLocation(int x, int y) {
	this.x=x;
        this.y=y;
    }	

    

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Moves this point to the specified location in the 
     * {@code (x,y)} coordinate plane. This method
     * is identical with <code>setLocation(int,&nbsp;int)</code>.
     * @param       x the X coordinate of the new location
     * @param       y the Y coordinate of the new location
     */
    public void move(int x, int y) {
	this.x = x ;
	this.y = y;
    }	

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Translates this point, at location {@code (x,y)}, 
     * by {@code dx} along the {@code x} axis and {@code dy} 
     * along the {@code y} axis so that it now represents the point 
     * {@code (x+dx,y+dy)}.
     *
     * @param       dx   the distance to move this point 
     *                            along the X axis
     * @param       dy    the distance to move this point 
     *                            along the Y axis
     */
    public void translate(int dx, int dy) {
	this.x +=  dx ;
	this.y +=  dy ;
    }	

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Determines whether or not two points are equal. Two instances of
     * <code>Point</code> are equal if the values of their
     * <code>x</code> and <code>y</code> member fields, representing
     * their position in the coordinate space, are the same.
     * @param obj an object to be compared with this <code>Point</code>
     * @return <code>true</code> if the object to be compared is
     *         an instance of <code>Point</code> and has
     *         the same values; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
	if (obj instanceof Point) {
	    Point pt = (Point)obj;
	    return (x == pt.x) && (y == pt.y);
	}
	return super.equals(obj);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a string representation of this point and its location 
     * in the {@code (x,y)} coordinate space. This method is 
     * intended to be used only for debugging purposes, and the content 
     * and format of the returned string may vary between implementations. 
     * The returned string may be empty but may not be <code>null</code>.
     * 
     * @return  a string representation of this point
     */
    public String toString() {
	return  "POINT [" + x +
                "," + y + "]";
    }

   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the square of the distance between the two
     * sets of specified coordinates.
     */
    public static int distanceSq(int x1, int y1,
				    int x2, int y2)
    {
	x1 -= x2;
	y1 -= y2;
	return (x1 * x1 + y1 * y1);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance between two points.
     *
     * @param x1 the X coordinate of the first specified point
     * @param y1 the Y coordinate of the first specified point
     * @param x2 the X coordinate of the second specified point
     * @param y2 the Y coordinate of the second specified point
     * @return the distance between the two sets of specified
     * coordinates.
     */
    public static int distance(int x1, int y1,
				  int x2, int y2)
    {
	x1 -= x2 ;
	y1 -= y2;
        long disSQ=x1 * x1 + y1 * y1;
        long dis=MathFP.sqrt(disSQ<<MathFP.DEFAULT_PRECISION);
	return MathFP.toInt(dis);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from this
     * <code>Point</code> to a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this <code>Point</code>
     * @param py the Y coordinate of the specified point to be measured
     *           against this <code>Point</code>
     * @return the square of the distance between this
     * <code>Point</code> and the specified point.
     */
    public int distanceSq(int px, int py) {
	px -= getX();
	py -= getY();
        return (px * px + py * py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the square of the distance from this
     * <code>Point</code> to a specified <code>Point</code>.
     *
     * @param pt the specified point to be measured
     *           against this <code>Point</code>
     * @return the square of the distance between this
     * <code>Point</code> to a specified <code>Point</code>.
     */
    public int distanceSq(Point pt) {
	int px = pt.getX() - this.getX();
	int py = pt.getY() - this.getY();
	return (px * px + py * py);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from this <code>Point</code> to
     * a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this <code>Point</code>
     * @param py the Y coordinate of the specified point to be measured
     *           against this <code>Point</code>
     * @return the distance between this <code>Point</code>
     * and a specified point.
     */
    public int distance(int px, int py) {
	px -= getX();
	py -= getY();
        long disSQ=px * px + py * py;
        long dis=MathFP.sqrt(disSQ<<MathFP.DEFAULT_PRECISION);
	return MathFP.toInt(dis);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the distance from this <code>Point</code> to a
     * specified <code>Point</code>.
     *
     * @param pt the specified point to be measured
     *           against this <code>Point</code>
     * @return the distance between this <code>Point</code> and
     * the specified <code>Point</code>.
     */
    public int distance(Point pt) {
	int px = pt.getX() - this.getX();
	int py = pt.getY() - this.getY();
	long disSQ=px * px + py * py;
        long dis=MathFP.sqrt(disSQ<<MathFP.DEFAULT_PRECISION);
	return MathFP.toInt(dis);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 02NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the hashcode for this <code>Point</code>.
     * @return      a hash code for this <code>Point</code>.
     */
    public int hashCode() {
	int bits = (getX() <<16) & 0xFFFF0000;
	bits ^= getY();
	return (int)bits;
    }

     private static final NumberListParser numberListParser=new NumberListParser();

}
