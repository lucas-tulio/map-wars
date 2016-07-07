package com.lucasdnd.mapwars.maps;

import com.google.android.gms.maps.model.LatLng;

public class GeometryUtil {
	
	// Earth metrics
	public static final double	EARTH_RADIUS			= 6378137.0;
	public static final double	EARTH_CIRCUMFERENCE		= 2.0 * Math.PI * EARTH_RADIUS;
	public static final double	GRID_SIZE_IN_METERS		= 1000.0;
	public static final double	GRID_SIZE_IN_DEGREES	= GRID_SIZE_IN_METERS / (EARTH_CIRCUMFERENCE / 360.0);

	/**
	 * Returns the distance, in meters, between two latitudes
	 * 
	 * Spherical law of cosines:
	 * d = acos( sin(φ1).sin(φ2) + cos(φ1).cos(φ2).cos(Δλ) ).R
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double getDistanceInMeters(double lat1, double lng1, double lat2, double lng2) {
		
		lat1 = degreesToRadians(lat1);
		lng1 = degreesToRadians(lng1);
		lat2 = degreesToRadians(lat2);
		lng2 = degreesToRadians(lng2);

		return (float) (Math.acos(Math.sin(lat1) * Math.sin(lat2) + 
		          Math.cos(lat1) * Math.cos(lat2) *
		          Math.cos(lng2-lng1)) * EARTH_RADIUS);
	}
	
	/**
	 * Given a LatLng source, calculate the new LatLng after moving distanceInMeters in that bearing
	 * 
	 * @param source
	 * @param range
	 * @param bearing
	 * @return
	 */
	public static LatLng getLatLngAwayFromSource(LatLng source, double distanceInMeters, double bearing) {
		
	    double latA = degreesToRadians(source.latitude);
	    double lonA = degreesToRadians(source.longitude);
	    double angularDistance = distanceInMeters / EARTH_RADIUS;
	    double trueCourse = degreesToRadians(bearing);

	    double lat = Math.asin(
	        Math.sin(latA) * Math.cos(angularDistance) + 
	        Math.cos(latA) * Math.sin(angularDistance) * Math.cos(trueCourse));

	    double dlon = Math.atan2(
	        Math.sin(trueCourse) * Math.sin(angularDistance) * Math.cos(latA), 
	        Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

	    double lon = ((lonA + dlon + Math.PI) % (Math.PI*2.0)) - Math.PI;

	    return new LatLng(
	        radiansToDegrees(lat), 
	        radiansToDegrees(lon));
	}
	
	public static double degreesToRadians(double angle) {
		return Math.PI * angle / 180.0;
	}
	public static double radiansToDegrees(double angle) {
		return angle * (180.0 / Math.PI);
	}
}
