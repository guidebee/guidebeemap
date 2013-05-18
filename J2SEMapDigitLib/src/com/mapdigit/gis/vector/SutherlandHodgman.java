//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.vector;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Vector;

import com.mapdigit.gis.geometry.GeoLatLng;
import com.mapdigit.gis.geometry.GeoLatLngBounds;


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Class SutherlandHodgman stands for Sutherland-Hodgmanclip algorithem.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/01/09
 * @author      Guidebee Pty Ltd.
 */
public class SutherlandHodgman {

    private OutputStage stageOut;
    private ClipStage stageBottom;
    private ClipStage stageLeft;
    private ClipStage stageTop;
    private ClipStage stageRight;
    private GeoLatLngBounds rectBounds;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * constructor.
     * @param clipRect
     * 	clip region
     */
    public SutherlandHodgman(GeoLatLngBounds clipRect) {
        stageOut = new OutputStage();
        stageBottom = new ClipStage(stageOut, 3, (clipRect.y + clipRect.height));
        stageLeft = new ClipStage(stageBottom, 4, clipRect.x);
        stageTop = new ClipStage(stageLeft, 1, clipRect.y);
        stageRight = new ClipStage(stageTop, 2, (clipRect.x + clipRect.width));
        rectBounds = new GeoLatLngBounds(clipRect);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clip the a pline.
     * @param input the pline to be clipped.
     * @return the clipped pline.
     */
    public Vector ClipPline(GeoLatLng[] input) {
        Vector clipped = new Vector();
        GeoLatLng p, prev = null;
        boolean isInside = false;
        boolean isInsidePrev = false;
        clipped.removeAllElements();
        for (int i = 0; i < input.length; i++) {
            p = (GeoLatLng) input[i];
            isInside = rectBounds.contains(p);
            if (isInside) {
                if (!isInsidePrev && (((clipped.size() != 0) &&
                        (!prev.equals((GeoLatLng) clipped.lastElement()))) ||
                        ((clipped.size() == 0 && (prev != null))))) {
                    clipped.addElement(prev);
                }
                clipped.addElement(p);
            } else if (isInsidePrev) {
                clipped.addElement(p);
            } else if (prev != null) {

                GeoLatLngBounds rect = new GeoLatLngBounds(Math.min(p.x, prev.x),
                        Math.min(p.y, prev.y),
                        Math.max(p.x, prev.x) - Math.min(p.x, prev.x),
                        Math.max(p.y, prev.y) - Math.min(p.y, prev.y));

                if (rect.intersects(rectBounds)) {

                    Vector line1 = new Vector();
                    line1.addElement(prev);
                    line1.addElement(p);

                    Vector line2 = new Vector();
                    line2.addElement(new GeoLatLng(rectBounds.y, rectBounds.x));
                    line2.addElement(new GeoLatLng(
                            (rectBounds.y + rectBounds.height),
                            (rectBounds.x + rectBounds.width)));

                    Vector line3 = new Vector();
                    line3.addElement(new GeoLatLng((rectBounds.y + rectBounds.height),
                            rectBounds.x));
                    line3.addElement(new GeoLatLng(
                            rectBounds.y,
                            (rectBounds.x + rectBounds.width)));

                    if (IsLineInter(line1, line2) ||
                            IsLineInter(line1, line3)) {

                        clipped.addElement(prev);
                        clipped.addElement(p);
                    }
                }
            }
            isInsidePrev = isInside;
            prev = p;

        }

        return clipped;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clip the a pline.
     * @param input the pline to be clipped.
     * @return the clipped pline.
     */
    public Vector ClipPline(Vector input) {
        Vector clipped = new Vector();
        GeoLatLng p, prev = null;
        boolean isInside = false;
        boolean isInsidePrev = false;
        clipped.removeAllElements();
        for (int i = 0; i < input.size(); i++) {
            p = (GeoLatLng) input.elementAt(i);
            isInside = rectBounds.contains(p);
            if (isInside) {
                if (!isInsidePrev && (((clipped.size() != 0) &&
                        (!prev.equals((GeoLatLng) clipped.lastElement()))) ||
                        ((clipped.size() == 0 && (prev != null))))) {
                    clipped.addElement(prev);
                }
                clipped.addElement(p);
            } else if (isInsidePrev) {
                clipped.addElement(p);
            } else if (prev != null) {

                GeoLatLngBounds rect = new GeoLatLngBounds(Math.min(p.x, prev.x),
                        Math.min(p.y, prev.y),
                        Math.max(p.x, prev.x) - Math.min(p.x, prev.x),
                        Math.max(p.y, prev.y) - Math.min(p.y, prev.y));

                if (rect.intersects(rectBounds)) {

                    Vector line1 = new Vector();
                    line1.addElement(prev);
                    line1.addElement(p);

                    Vector line2 = new Vector();
                    line2.addElement(new GeoLatLng(rectBounds.y, rectBounds.x));
                    line2.addElement(new GeoLatLng(
                            (rectBounds.y + rectBounds.height),
                            (rectBounds.x + rectBounds.width)));

                    Vector line3 = new Vector();
                    line3.addElement(new GeoLatLng((rectBounds.y + rectBounds.height),
                            rectBounds.x));
                    line3.addElement(new GeoLatLng(
                            rectBounds.y,
                            (rectBounds.x + rectBounds.width)));

                    if (IsLineInter(line1, line2) ||
                            IsLineInter(line1, line3)) {

                        clipped.addElement(prev);
                        clipped.addElement(p);
                    }
                }
            }
            isInsidePrev = isInside;
            prev = p;

        }

        return clipped;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clip one region.
     *
     * @param input
     *            the region to be clipped.
     * @return the clipped region.
     */
    public Vector ClipRegion(GeoLatLng[] input) {

        Vector clipped = new Vector();
        clipped.removeAllElements();
        stageOut.SetDestination(clipped);

        for (int i = 0; i < input.length; i++) {
            stageRight.HandleVertex((GeoLatLng) input[i]);
        }
        // Do the final step.
        stageRight.Finalize();

        return clipped;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clip one region.
     *
     * @param input
     *            the region to be clipped.
     * @return the clipped region.
     */
    public Vector ClipRegion(Vector input) {

        Vector clipped = new Vector();
        clipped.removeAllElements();
        stageOut.SetDestination(clipped);

        for (int i = 0; i < input.size(); i++) {
            stageRight.HandleVertex((GeoLatLng) input.elementAt(i));
        }
        // Do the final step.
        stageRight.Finalize();

        return clipped;
    }

    private double crossMulti(Object object1, Object object2, Object object0) {
        double x1, y1, x2, y2;
        GeoLatLng p1 = (GeoLatLng) object1;
        GeoLatLng p2 = (GeoLatLng) object2;
        GeoLatLng p0 = (GeoLatLng) object0;
        x1 = p1.x - p0.x;
        y1 = p1.y - p0.y;
        x2 = p2.x - p0.x;
        y2 = p2.y - p0.y;
        return x1 * y2 - x2 * y1;
    }

    private boolean IsLineInter(Vector line1, Vector line2) {
        double t1 = crossMulti(line2.firstElement(), line1.lastElement(),
                line1.firstElement());
        double t2 = crossMulti(line2.lastElement(), line1.lastElement(),
                line1.firstElement());
        double t = t1 * t2;
        if (t > 0.0) {
            return false;
        }

        t1 = crossMulti(line1.firstElement(), line2.lastElement(),
                line2.firstElement());

        t2 = crossMulti(line1.lastElement(), line2.lastElement(),
                line2.firstElement());
        t = t1 * t2;
        if (t > 0.0) {
            return false;
        }

        return true;
    }
}

class ClipStage {

    /**
     * The next stage
     */
    private ClipStage m_NextStage;
    /**
     * true if no vertices have been handled.
     */
    private boolean m_bFirst;
    /**
     * the first vertex.
     */
    private GeoLatLng m_pntFirst;
    /**
     * the previous vertex.
     */
    private GeoLatLng m_pntPrevious;
    /**
     * true if the previous vertex was inside the Boundary.
     */
    private boolean m_bPreviousInside;
    private int m_intDirection;
    private double m_dblX = 0;
    private double m_dblY = 0;

    public ClipStage() {
    }

    //	1-Top,2-Right,3-Bottom,4-Left,5 out
    public ClipStage(ClipStage nextStage, int direction, double cord) {
        m_NextStage = nextStage;
        m_bFirst = true;
        m_intDirection = direction;
        switch (m_intDirection) {
            case 1:
            case 3:
                m_dblY = cord;
                break;
            case 2:
            case 4:
                m_dblX = cord;
                break;
        }
    }

    public boolean IsInside(GeoLatLng pt) {
        boolean ret = false;
        switch (m_intDirection) {
            case 1://Top
                ret = pt.y >= m_dblY;
                break;
            case 2://Right
                ret = pt.x < m_dblX;
                break;
            case 3://Bottom
                ret = pt.y < m_dblY;
                break;
            case 4://Left
                ret = pt.x >= m_dblX;
                break;
        }
        return ret;
    }

    //	 Function to handle one vertex
    public void HandleVertex(GeoLatLng pntCurrent) {
        {
            if ((Object) pntCurrent == null) {
                return;
            }
            boolean bCurrentInside = IsInside(pntCurrent);
            // See if vertex is inside the boundary.

            if (m_bFirst) // If this is the first vertex...
            {
                m_pntFirst = pntCurrent;	// ... just remember it,...

                m_bFirst = false;
            } else // Common cases, not the first vertex.
            {
                if (bCurrentInside) // If this vertex is inside...
                {
                    if (!m_bPreviousInside) // ... and the previous one was outside
                    {
                        m_NextStage.HandleVertex(Intersect(m_pntPrevious, pntCurrent));
                    }
                    // ... first output the intersection point.

                    m_NextStage.HandleVertex(pntCurrent);	// Output the current vertex.
                } else if (m_bPreviousInside) // If this vertex is outside, and the previous one was inside...
                {
                    m_NextStage.HandleVertex(Intersect(m_pntPrevious, pntCurrent));
                }
            // ... output the intersection point.

            // If neither current vertex nor the previous one are inside, output nothing.
            }
            m_pntPrevious = pntCurrent;		// Be prepared for next vertex.
            m_bPreviousInside = bCurrentInside;
        }
    }

    public void Finalize() {
        HandleVertex(m_pntFirst);		// Close the polygon.
        m_NextStage.Finalize();			// Delegate to the next stage.
        m_bFirst = true;
    }

    public GeoLatLng Intersect(GeoLatLng p0, GeoLatLng p1) {
        GeoLatLng r = new GeoLatLng(0, 0);
        GeoLatLng d;
        switch (m_intDirection) {
            case 1:
            case 3:
                d = new GeoLatLng(p1.y - p0.y, p1.x - p0.x);
                double xslope = d.x / d.y;
                r.y = m_dblY;
                r.x = p0.x + xslope * (m_dblY - p0.y);
                break;
            case 2:
            case 4:
                d = new GeoLatLng(p1.y - p0.y, p1.x - p0.x);
                double yslope = d.y / d.x;
                r.x = m_dblX;
                r.y = p0.y + yslope * (m_dblX - p0.x);
                break;
        }
        return r;
    }
}

class OutputStage extends ClipStage {

    private Vector m_pDest;

    public OutputStage() {
    }

    public void SetDestination(Vector pDest) {
        m_pDest = pDest;
    }

    // Append the vertex to the output container.
    public void HandleVertex(GeoLatLng pnt) {
        if (m_pDest.isEmpty() || !pnt.equals((GeoLatLng) m_pDest.lastElement())) {
            m_pDest.addElement(pnt);
        }
    }

    //	 Do nothing.
    public void Finalize() {
    }
}



