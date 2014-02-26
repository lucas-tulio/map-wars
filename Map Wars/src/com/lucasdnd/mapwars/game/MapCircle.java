package com.lucasdnd.mapwars.game;

import android.graphics.Color;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapCircle {
	
	private CircleOptions circleOptions;
	private Circle circle;
	private boolean gotHit;
	
	public MapCircle(LatLng latLng, double radius, int fillColor) {
		
		circleOptions = new CircleOptions()
			.center(latLng)
			.radius(radius)
			.strokeColor(Color.BLACK)
			.fillColor(fillColor)
			.strokeWidth(1f);
	}

	public boolean gotHit() {
		return gotHit;
	}

	public void setGotHit(boolean gotHit) {
		this.gotHit = gotHit;
	}

	public CircleOptions getCircleOptions() {
		return circleOptions;
	}

	public void setCircleOptions(CircleOptions circleOptions) {
		this.circleOptions = circleOptions;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
}