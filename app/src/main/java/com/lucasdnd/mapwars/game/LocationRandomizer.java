package com.lucasdnd.mapwars.game;

import java.util.Random;

import com.google.android.gms.maps.model.LatLng;
import com.lucasdnd.mapwars.maps.GeometryUtil;

public class LocationRandomizer {
	
	public static LatLng getRandomLatLng(LatLng currentLocation, double min, double max) {
		
		Random r = new Random();
		
		// Generate a random bearing
		double bearing = r.nextDouble() * 360;
		
		// How far?
		double distance = (r.nextDouble() * (max - min)) + min;
		
		// Where would that be?
		LatLng resultingLatLng = GeometryUtil.getLatLngAwayFromSource(currentLocation, distance, bearing);
		
		return resultingLatLng;
	}
}
