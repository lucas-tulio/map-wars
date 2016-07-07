package com.lucasdnd.mapwars.game;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Circle;
import com.lucasdnd.mapwars.maps.GeometryUtil;

public class CollisionChecker {
	
	public static void checkCollision(Circle explosion, ArrayList<MapCircle> targets) {
		
		double explosionLat = explosion.getCenter().latitude;
		double explosionLng = explosion.getCenter().longitude;
		double explosionRadius = explosion.getRadius();
		
		for (MapCircle target : targets) {
			
			// Get the center and radius of the Target
			double targetLat = target.getCircleOptions().getCenter().latitude;
			double targetLng = target.getCircleOptions().getCenter().longitude;
			double targetRadius = target.getCircleOptions().getRadius();
			
			// Get the distance between them
			double distance = GeometryUtil.getDistanceInMeters(explosionLat, explosionLng, targetLat, targetLng);
			
			// Check their radius
			if(distance <= explosionRadius + targetRadius) {
				target.setGotHit(true);
			}
		}
		
	}
}
