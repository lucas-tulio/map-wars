package com.lucasdnd.mapwars.game;

import java.util.Random;

import com.google.android.gms.maps.model.LatLng;

public class LocationRandomizer {
	
	public static LatLng getRandomLatLng(LatLng currentLocation, double random, double min) {
		
		Random r = new Random();
		
		// Positive or negative?
		boolean negativeLat = r.nextBoolean();
		boolean negativeLng = r.nextBoolean();
		
		// Random Lat, Lng
		double randomLat, randomLng;
		randomLat = randomLng = random;
		
		// Min Lat, Lng
		double minLat, minLng;
		minLat = minLng = min;
		
		if(negativeLat) {
			randomLat *= -1;
			minLat *= -1;
		}
		if(negativeLng) {
			randomLng *= -1;
			minLng *= -1;
		}
		
		// Get the randoms!
		double lat = r.nextDouble() * randomLat + minLat;
		double lng = r.nextDouble() * randomLng + minLng;
					
		return new LatLng(currentLocation.latitude + lat, currentLocation.longitude + lng);
	}
}
