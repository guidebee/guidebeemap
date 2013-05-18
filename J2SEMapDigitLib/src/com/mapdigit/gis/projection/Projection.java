//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 15MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.projection;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoBounds;
import com.mapdigit.gis.geometry.GeoPoint;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The superclass for all map projections
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 15/03/08
 * @author      Guidebee Pty Ltd.
 */
public abstract class Projection {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Project a lat/long point (in degrees), producing a result in metres
     * @param src the lat/long point in degree
     * @param dst the projection coordinate on the plane in meters.
     * @return the projection coordinate on the plane in meters.
     */
    public GeoPoint transform(GeoPoint src, GeoPoint dst) {
        double x = src.x * DTR;
        if (projectionLongitude != 0) {
            x = MapMath.normalizeLongitude(x - projectionLongitude);
        }
        project(x, src.y * DTR, dst);
        dst.x = totalScale * dst.x + falseEasting;
        dst.y = totalScale * dst.y + falseNorthing;
        return dst;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Project a lat/long point, producing a result in metres
     * @param src lat/long point in radians.
     * @param dst the projection coordinate on the plane in meters.
     * @return the projection coordinate on the plane in meters.
     */
    public GeoPoint transformRadians(GeoPoint src, GeoPoint dst) {
        double x = src.x;
        if (projectionLongitude != 0) {
            x = MapMath.normalizeLongitude(x - projectionLongitude);
        }
        project(x, src.y, dst);
        dst.x = totalScale * dst.x + falseEasting;
        dst.y = totalScale * dst.y + falseNorthing;
        return dst;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * The method which actually does the projection. This should be overridden 
     * for all projections.
     * @param x longitude
     * @param y latitude
     * @param dst return result.
     * @return the projected coordinates in map.
     */
    public GeoPoint project(double x, double y, GeoPoint dst) {
        dst.x = x;
        dst.y = y;
        return dst;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Project a number of lat/long points (in degrees), producing a result in metres
     * @param srcPoints the source point array of lat/long in degrees.
     * @param srcOffset the start index of the source array.
     * @param dstPoints the result array.
     * @param dstOffset the start index of the result array.
     * @param numPoints the number of points need to be transformed.
     */
    public void transform(double[] srcPoints, int srcOffset, double[] dstPoints,
            int dstOffset, int numPoints) {
        GeoPoint in = new GeoPoint();
        GeoPoint out = new GeoPoint();
        for (int i = 0; i < numPoints; i++) {
            in.x = srcPoints[srcOffset++] * DTR;
            in.y = srcPoints[srcOffset++] * DTR;
            transform(in, out);
            dstPoints[dstOffset++] = out.x;
            dstPoints[dstOffset++] = out.y;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Project a number of lat/long points (in radians), producing a result in metres
     * @param srcPoints the source point array of lat/long in radians.
     * @param srcOffset the start index of the source array.
     * @param dstPoints the result array.
     * @param dstOffset the start index of the result array.
     * @param numPoints the number of points need to be transformed.
     */
    public void transformRadians(double[] srcPoints, int srcOffset,
            double[] dstPoints, int dstOffset, int numPoints) {
        GeoPoint in = new GeoPoint();
        GeoPoint out = new GeoPoint();
        for (int i = 0; i < numPoints; i++) {
            in.x = srcPoints[srcOffset++];
            in.y = srcPoints[srcOffset++];
            transform(in, out);
            dstPoints[dstOffset++] = out.x;
            dstPoints[dstOffset++] = out.y;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Inverse-project a point (in metres), producing a lat/long result in degrees
     * @param src the point in meters on the plane map.
     * @param dst the lat/long result in degrees.
     * @return the lat/long result in degrees.
     */
    public GeoPoint inverseTransform(GeoPoint src, GeoPoint dst) {
        double x = (src.x - falseEasting) / totalScale;
        double y = (src.y - falseNorthing) / totalScale;
        projectInverse(x, y, dst);
        if (dst.x < -Math.PI) {
            dst.x = -Math.PI;
        } else if (dst.x > Math.PI) {
            dst.x = Math.PI;
        }
        if (projectionLongitude != 0) {
            dst.x = MapMath.normalizeLongitude(dst.x + projectionLongitude);
        }
        dst.x *= RTD;
        dst.y *= RTD;
        return dst;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Inverse-project a point (in metres), producing a lat/long result in radians
     * @param src the point in meters on the plane map.
     * @param dst the lat/long result in radians.
     * @return the lat/long result in radians.
     */
    public GeoPoint inverseTransformRadians(GeoPoint src, GeoPoint dst) {
        double x = (src.x - falseEasting) / totalScale;
        double y = (src.y - falseNorthing) / totalScale;
        projectInverse(x, y, dst);
        if (dst.x < -Math.PI) {
            dst.x = -Math.PI;
        } else if (dst.x > Math.PI) {
            dst.x = Math.PI;
        }
        if (projectionLongitude != 0) {
            dst.x = MapMath.normalizeLongitude(dst.x + projectionLongitude);
        }
        return dst;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * The method which actually does the inverse projection. This should be 
     * overridden for all projections.
     * @param x x coordinate
     * @param y y coordinate
     * @param dst the result coordinates.
     * @return the inverse transformed coordinates.
     */
    public GeoPoint projectInverse(double x, double y, GeoPoint dst) {
        dst.x = x;
        dst.y = y;
        return dst;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Inverse-project a number of points (in metres), producing 
     * a lat/long result in degrees
     * @param srcPoints the source point array of lat/long in degrees.
     * @param srcOffset the start index of the source array.
     * @param dstPoints the result array.
     * @param dstOffset the start index of the result array.
     * @param numPoints the number of points need to be transformed.
     */
    public void inverseTransform(double[] srcPoints, int srcOffset,
            double[] dstPoints, int dstOffset, int numPoints) {
        GeoPoint in = new GeoPoint();
        GeoPoint out = new GeoPoint();
        for (int i = 0; i < numPoints; i++) {
            in.x = srcPoints[srcOffset++];
            in.y = srcPoints[srcOffset++];
            inverseTransform(in, out);
            dstPoints[dstOffset++] = out.x * RTD;
            dstPoints[dstOffset++] = out.y * RTD;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Inverse-project a number of points (in metres), producing
     * a lat/long result in radians
     * @param srcPoints the source point array of lat/long in radians.
     * @param srcOffset the start index of the source array.
     * @param dstPoints the result array.
     * @param dstOffset the start index of the result array.
     * @param numPoints the number of points need to be transformed.
     */
    public void inverseTransformRadians(double[] srcPoints, int srcOffset,
            double[] dstPoints, int dstOffset, int numPoints) {
        GeoPoint in = new GeoPoint();
        GeoPoint out = new GeoPoint();
        for (int i = 0; i < numPoints; i++) {
            in.x = srcPoints[srcOffset++];
            in.y = srcPoints[srcOffset++];
            inverseTransform(in, out);
            dstPoints[dstOffset++] = out.x;
            dstPoints[dstOffset++] = out.y;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Finds the smallest lat/long rectangle wholly inside the given view rectangle.
     * This is only a rough estimate.
     */
    public GeoBounds inverseTransform(GeoBounds r) {
        GeoPoint in = new GeoPoint();
        GeoPoint out = new GeoPoint();
        GeoBounds bounds = null;
        if (isRectilinear()) {
            for (int ix = 0; ix < 2; ix++) {
                double x = r.getX() + r.getWidth() * ix;
                for (int iy = 0; iy < 2; iy++) {
                    double y = r.getY() + r.getHeight() * iy;
                    in.x = x;
                    in.y = y;
                    inverseTransform(in, out);
                    if (ix == 0 && iy == 0) {
                        bounds = new GeoBounds(out.x, out.y, 0, 0);
                    } else {
                        bounds.add(out.x, out.y);
                    }
                }
            }
        } else {
            for (int ix = 0; ix < 7; ix++) {
                double x = r.getX() + r.getWidth() * ix / 6;
                for (int iy = 0; iy < 7; iy++) {
                    double y = r.getY() + r.getHeight() * iy / 6;
                    in.x = x;
                    in.y = y;
                    inverseTransform(in, out);
                    if (ix == 0 && iy == 0) {
                        bounds = new GeoBounds(out.x, out.y, 0, 0);
                    } else {
                        bounds.add(out.x, out.y);
                    }
                }
            }
        }
        return bounds;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Transform a bounding box. This is only a rough estimate.
     */
    public GeoBounds transform(GeoBounds r) {
        GeoPoint in = new GeoPoint();
        GeoPoint out = new GeoPoint();
        GeoBounds bounds = null;
        if (isRectilinear()) {
            for (int ix = 0; ix < 2; ix++) {
                double x = r.getX() + r.getWidth() * ix;
                for (int iy = 0; iy < 2; iy++) {
                    double y = r.getY() + r.getHeight() * iy;
                    in.x = x;
                    in.y = y;
                    transform(in, out);
                    if (ix == 0 && iy == 0) {
                        bounds = new GeoBounds(out.x, out.y, 0, 0);
                    } else {
                        bounds.add(out.x, out.y);
                    }
                }
            }
        } else {
            for (int ix = 0; ix < 7; ix++) {
                double x = r.getX() + r.getWidth() * ix / 6;
                for (int iy = 0; iy < 7; iy++) {
                    double y = r.getY() + r.getHeight() * iy / 6;
                    in.x = x;
                    in.y = y;
                    transform(in, out);
                    if (ix == 0 && iy == 0) {
                        bounds = new GeoBounds(out.x, out.y, 0, 0);
                    } else {
                        bounds.add(out.x, out.y);
                    }
                }
            }
        }
        return bounds;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this projection is conformal
     */
    public boolean isConformal() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this projection is equal area
     */
    public boolean isEqualArea() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if this projection has an inverse
     */
    public boolean hasInverse() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if lat/long lines form a rectangular grid for this projection
     */
    public boolean isRectilinear() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if latitude lines are parallel for this projection
     */
    public boolean parallelsAreParallel() {
        return isRectilinear();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns true if the given lat/long point is visible in this projection
     */
    public boolean inside(double x, double y) {
        x = normalizeLongitude((float) (x * DTR - projectionLongitude));
        return minLongitude <= x && x <= maxLongitude
                && minLatitude <= y && y <= maxLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the name of this projection.
     */
    public void setName(String name) {
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    public String getName() {
        if (name != null) {
            return name;
        }
        return toString();
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * to a string.
     * @return a string.
     */
    public String toString() {
        return "None";
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the minimum latitude. This is only used for Shape clipping
     * and doesn't affect projection.
     */
    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the minimum latitude.
     */
    public double getMinLatitude() {
        return minLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the maximum latitude. This is only used for Shape clipping
     * and doesn't affect projection.
     */
    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the maximum latitude.
     */
    public double getMaxLatitude() {
        return maxLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the maximum latitude in degrees.
     */
    public double getMaxLatitudeDegrees() {
        return maxLatitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the minumum latitude in degrees.
     */
    public double getMinLatitudeDegrees() {
        return minLatitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the minumum latitude.
     */
    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the minumum longitude.
     */
    public double getMinLongitude() {
        return minLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the minumum longitude in degrees.
     */
    public void setMinLongitudeDegrees(double minLongitude) {
        this.minLongitude = DTR * minLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the minumum longitude in degrees.
     */
    public double getMinLongitudeDegrees() {
        return minLongitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set the maxumum longitude.
     */
    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the maxumum longitude.
     */
    public double getMaxLongitude() {
        return maxLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the maxumum longitude in degrees.
     */
    public void setMaxLongitudeDegrees(double maxLongitude) {
        this.maxLongitude = DTR * maxLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the maxumum longitude in degrees.
     */
    public double getMaxLongitudeDegrees() {
        return maxLongitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection latitude in radians.
     */
    public void setProjectionLatitude(double projectionLatitude) {
        this.projectionLatitude = projectionLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection latitude in radians.
     */
    public double getProjectionLatitude() {
        return projectionLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection latitude in degrees.
     */
    public void setProjectionLatitudeDegrees(double projectionLatitude) {
        this.projectionLatitude = DTR * projectionLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection latitude in degrees.
     */
    public double getProjectionLatitudeDegrees() {
        return projectionLatitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection longitude in radians.
     */
    public void setProjectionLongitude(double projectionLongitude) {
        this.projectionLongitude = normalizeLongitudeRadians(projectionLongitude);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection longitude in radians.
     */
    public double getProjectionLongitude() {
        return projectionLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection longitude in degrees.
     */
    public void setProjectionLongitudeDegrees(double projectionLongitude) {
        this.projectionLongitude = DTR * projectionLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection longitude in degrees.
     */
    public double getProjectionLongitudeDegrees() {
        return projectionLongitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the latitude of true scale in radians. This is only used by certain projections.
     */
    public void setTrueScaleLatitude(double trueScaleLatitude) {
        this.trueScaleLatitude = trueScaleLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the latitude of true scale in radians.
     */
    public double getTrueScaleLatitude() {
        return trueScaleLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the latitude of true scale in degrees. This is only used by certain projections.
     */
    public void setTrueScaleLatitudeDegrees(double trueScaleLatitude) {
        this.trueScaleLatitude = DTR * trueScaleLatitude;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the latitude of true scale in degrees. This is only used by certain projections.
     */
    public double getTrueScaleLatitudeDegrees() {
        return trueScaleLatitude * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection latitude in radians.
     */
    public void setProjectionLatitude1(double projectionLatitude1) {
        this.projectionLatitude1 = projectionLatitude1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection latitude in radians.
     */
    public double getProjectionLatitude1() {
        return projectionLatitude1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection latitude in degrees.
     */
    public void setProjectionLatitude1Degrees(double projectionLatitude1) {
        this.projectionLatitude1 = DTR * projectionLatitude1;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection latitude in degrees.
     */
    public double getProjectionLatitude1Degrees() {
        return projectionLatitude1 * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection latitude in radians.
     */
    public void setProjectionLatitude2(double projectionLatitude2) {
        this.projectionLatitude2 = projectionLatitude2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection latitude in radians.
     */
    public double getProjectionLatitude2() {
        return projectionLatitude2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection latitude in degrees.
     */
    public void setProjectionLatitude2Degrees(double projectionLatitude2) {
        this.projectionLatitude2 = DTR * projectionLatitude2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection latitude in degrees.
     */
    public double getProjectionLatitude2Degrees() {
        return projectionLatitude2 * RTD;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the false Northing in projected units.
     */
    public void setFalseNorthing(double falseNorthing) {
        this.falseNorthing = falseNorthing;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the false Northing in projected units.
     */
    public double getFalseNorthing() {
        return falseNorthing;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the false Easting in projected units.
     */
    public void setFalseEasting(double falseEasting) {
        this.falseEasting = falseEasting;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the false Easting in projected units.
     */
    public double getFalseEasting() {
        return falseEasting;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the projection scale factor. This is set to 1 by default.
     */
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the projection scale factor. This is set to 1 by default.
     */
    public double getScaleFactor() {
        return scaleFactor;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the equator radius.
     */
    public double getEquatorRadius() {
        return a;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the conversion factor from metres to projected units. This is set to 1 by default.
     */
    public void setFromMetres(double fromMetres) {
        this.fromMetres = fromMetres;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the conversion factor from metres to projected units.
     * This is set to 1 by default.
     */
    public double getFromMetres() {
        return fromMetres;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the ellipsoid for this project.
     * @param ellipsoid the ellipsoid object.
     */
    public void setEllipsoid(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
        a = ellipsoid.equatorRadius;
        e = ellipsoid.eccentricity;
        es = ellipsoid.eccentricity2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the ellipsoid for this project.
     */
    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initialize the projection. This should be called after setting
     * parameters and before using the projection.
     * This is for performance reasons as initialization may be expensive.
     */
    public void initialize() {
        spherical = e == 0.0;
        one_es = 1 - es;
        rone_es = 1.0 / one_es;
        totalScale = a * fromMetres;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Normalized the longitude
     * @param angle the input float.
     * @return the normalized longitude.(from -180 to 180).
     */
    public static float normalizeLongitude(float angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle)) {
            throw new IllegalArgumentException("Infinite longitude");
        }
        while (angle > 180) {
            angle -= 360;
        }
        while (angle < -180) {
            angle += 360;
        }
        return angle;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Normalized the longitude in radians.
     * @param angle the input float.
     * @return the normalized longitude.(from -PI to PI).
     */
    public static double normalizeLongitudeRadians(double angle) {
        if (Double.isInfinite(angle) || Double.isNaN(angle)) {
            throw new IllegalArgumentException("Infinite longitude");
        }
        while (angle > Math.PI) {
            angle -= MapMath.TWOPI;
        }
        while (angle < -Math.PI) {
            angle += MapMath.TWOPI;
        }
        return angle;
    }

    /**
     * The minimum latitude of the bounds of this projection
     */
    protected double minLatitude = -Math.PI / 2;

    /**
     * The minimum longitude of the bounds of this projection.
     * This is relative to the projection centre.
     */
    protected double minLongitude = -Math.PI;

    /**
     * The maximum latitude of the bounds of this projection
     */
    protected double maxLatitude = Math.PI / 2;

    /**
     * The maximum longitude of the bounds of this projection.
     * This is relative to the projection centre.
     */
    protected double maxLongitude = Math.PI;

    /**
     * The latitude of the centre of projection
     */
    protected double projectionLatitude = 0.0;

    /**
     * The longitude of the centre of projection
     */
    protected double projectionLongitude = 0.0;

    /**
     * Standard parallel 1 (for projections which use it)
     */
    protected double projectionLatitude1 = 0.0;

    /**
     * Standard parallel 2 (for projections which use it)
     */
    protected double projectionLatitude2 = 0.0;

    /**
     * The projection scale factor
     */
    protected double scaleFactor = 1.0;

    /**
     * The false Easting of this projection
     */
    protected double falseEasting = 0;

    /**
     * The false Northing of this projection
     */
    protected double falseNorthing = 0;

    /**
     * The latitude of true scale. Only used by specific projections.
     */
    protected double trueScaleLatitude = 0.0;

    /**
     * The equator radius
     */
    protected double a = 0;

    /**
     * The eccentricity
     */
    protected double e = 0;

    /**
     * The eccentricity squared
     */
    protected double es = 0;

    /**
     * 1-(eccentricity squared)
     */
    protected double one_es = 0;

    /**
     * 1/(1-(eccentricity squared))
     */
    protected double rone_es = 0;

    /**
     * The ellipsoid used by this projection
     */
    protected Ellipsoid ellipsoid;

    /**
     * True if this projection is using a sphere (es == 0)
     */
    protected boolean spherical;

    /**
     * True if this projection is geocentric
     */
    protected boolean geocentric;

    /**
     * The name of this projection
     */
    protected String name = null;

    /**
     * Conversion factor from metres to whatever units the projection uses.
     */
    protected double fromMetres = 1;

    /**
     * The total scale factor = Earth radius * units
     */
    private double totalScale = 0;

    // Some useful constants
    protected final static double EPS10 = 1e-10;
    protected final static double RTD = 180.0 / Math.PI;
    protected final static double DTR = Math.PI / 180.0;

    protected Projection() {
        setEllipsoid(Ellipsoid.SPHERE);
    }
}

