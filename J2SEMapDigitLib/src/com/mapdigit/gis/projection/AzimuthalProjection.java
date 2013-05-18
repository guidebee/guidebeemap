/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mapdigit.gis.projection;

/**
 * The superclass for all azimuthal map projections
 */
public class AzimuthalProjection extends Projection {

	public final static int NORTH_POLE = 1;
	public final static int SOUTH_POLE = 2;
	public final static int EQUATOR = 3;
	public final static int OBLIQUE = 4;

	protected int mode;
	protected double sinphi0, cosphi0;
	private double mapRadius = 90.0;

	public AzimuthalProjection() {
		this( Math.toRadians(45.0), Math.toRadians(45.0) );
	}

	public AzimuthalProjection(double projectionLatitude, double projectionLongitude) {
		this.projectionLatitude = projectionLatitude;
		this.projectionLongitude = projectionLongitude;
		initialize();
	}

	public void initialize() {
		super.initialize();
		if (Math.abs(Math.abs(projectionLatitude) - MapMath.HALFPI) < EPS10)
			mode = projectionLatitude < 0. ? SOUTH_POLE : NORTH_POLE;
		else if (Math.abs(projectionLatitude) > EPS10) {
			mode = OBLIQUE;
			sinphi0 = Math.sin(projectionLatitude);
			cosphi0 = Math.cos(projectionLatitude);
		} else
			mode = EQUATOR;
	}

	public boolean inside(double lon, double lat) {
		return MapMath.greatCircleDistance( Math.toRadians(lon), Math.toRadians(lat), projectionLongitude, projectionLatitude) < Math.toRadians(mapRadius);
	}

	/**
	 * Set the map radius (in degrees). 180 shows a hemisphere, 360 shows the whole globe.
	 */
	public void setMapRadius(double mapRadius) {
		this.mapRadius = mapRadius;
	}

	public double getMapRadius() {
		return mapRadius;
	}

}

