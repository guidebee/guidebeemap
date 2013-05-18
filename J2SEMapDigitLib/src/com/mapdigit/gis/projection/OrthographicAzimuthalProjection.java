/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mapdigit.gis.projection;

import com.mapdigit.gis.geometry.GeoPoint;
import com.mapdigit.util.MathEx;

/**
 * The Orthographic Azimuthal or Globe map projection.
 */
public class OrthographicAzimuthalProjection extends AzimuthalProjection {

	public OrthographicAzimuthalProjection() {
		initialize();
	}

	public GeoPoint project(double lam, double phi, GeoPoint xy) {
		double sinphi;
		double cosphi = MathEx.cos(phi);
		double coslam = MathEx.cos(lam);

		// Theoretically we should throw the ProjectionExceptions below, but for practical purposes
		// it's better not to as they tend to crop up a lot up due to rounding errors.
		switch (mode) {
		case EQUATOR:
//			if (cosphi * coslam < - EPS10)
//				throw new ProjectionException();
			xy.y = MathEx.sin(phi);
			break;
		case OBLIQUE:
			sinphi = MathEx.sin(phi);
//			if (sinphi0 * (sinphi) + cosphi0 * cosphi * coslam < - EPS10)
//				;
//			   throw new ProjectionException();
			xy.y = cosphi0 * sinphi - sinphi0 * cosphi * coslam;
			break;
		case NORTH_POLE:
			coslam = - coslam;
		case SOUTH_POLE:
//			if (MathEx.abs(phi - projectionLatitude) - EPS10 > MapMath.HALFPI)
//				throw new ProjectionException();
			xy.y = cosphi * coslam;
			break;
		}
		xy.x = cosphi * MathEx.sin(lam);
		return xy;
	}

	public GeoPoint projectInverse(double x, double y, GeoPoint lp) {
		double  rh, cosc, sinc;

		if ((sinc = (rh = MapMath.distance(x, y))) > 1.) {
			if ((sinc - 1.) > EPS10) throw new ProjectionException();
				sinc = 1.;
		}
		cosc = MathEx.sqrt(1. - sinc * sinc); /* in this range OK */
		if (MathEx.abs(rh) <= EPS10)
			lp.y = projectionLatitude;
		else switch (mode) {
		case NORTH_POLE:
			y = -y;
			lp.y = MathEx.acos(sinc);
			break;
		case SOUTH_POLE:
			lp.y = - MathEx.acos(sinc);
			break;
		case EQUATOR:
			lp.y = y * sinc / rh;
			x *= sinc;
			y = cosc * rh;
			if (MathEx.abs(lp.y) >= 1.)
				lp.y = lp.y < 0. ? -MapMath.HALFPI : MapMath.HALFPI;
			else
				lp.y = MathEx.asin(lp.y);
			break;
		case OBLIQUE:
			lp.y = cosc * sinphi0 + y * sinc * cosphi0 / rh;
			y = (cosc - sinphi0 * lp.y) * rh;
			x *= sinc * cosphi0;
			if (MathEx.abs(lp.y) >= 1.)
				lp.y = lp.y < 0. ? -MapMath.HALFPI : MapMath.HALFPI;
			else
				lp.y = MathEx.asin(lp.y);
			break;
		}
		lp.x = (y == 0. && (mode == OBLIQUE || mode == EQUATOR)) ?
			 (x == 0. ? 0. : x < 0. ? -MapMath.HALFPI : MapMath.HALFPI) : MathEx.atan2(x, y);
		return lp;
	}

	public boolean hasInverse() {
		return true;
	}

	public String toString() {
		return "Orthographic Azimuthal";
	}

}
