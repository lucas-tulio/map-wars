package com.lucasdnd.mapwars.maps;

import android.graphics.Point;
import android.graphics.PointF;

public class GeometryUtil {
	
	// Earth metrics
	public static final double	EARTH_RADIUS			= 6378137.0;
	public static final double	EARTH_CIRCUMFERENCE		= 2 * Math.PI * EARTH_RADIUS;
	public static final int		GRID_SIZE_IN_METERS		= 1000;
	public static final double	GRID_SIZE_IN_DEGREES	= GRID_SIZE_IN_METERS / (EARTH_CIRCUMFERENCE / 360.0);
	
	// Returns the distance between two latitudes
	//
	// Spherical law of cosines:
	// d = acos( sin(φ1).sin(φ2) + cos(φ1).cos(φ2).cos(Δλ) ).R
	public static float getDistanceInMetersAtLatLng(PointF p1, PointF p2){
		
		double lat1 = degreesToRadians(p1.y);
		double lon1 = degreesToRadians(p1.x);
		double lat2 = degreesToRadians(p2.y);
		double lon2 = degreesToRadians(p2.x);

		return (float) (Math.acos(Math.sin(lat1) * Math.sin(lat2) + 
		          Math.cos(lat1) * Math.cos(lat2) *
		          Math.cos(lon2-lon1)) * EARTH_RADIUS);
	}
	
	public static double degreesToRadians(double angle)
	{
		return Math.PI * angle / 180.0;
	}
	public static double radiansToDegrees(double angle)
	{
		return angle * (180.0 / Math.PI);
	}
	
	
}
