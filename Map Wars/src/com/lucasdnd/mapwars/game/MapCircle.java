package com.lucasdnd.mapwars.game;

import android.graphics.Color;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapCircle {
	
	private CircleOptions circleOptions;
	private Circle circle;
	
	public MapCircle(LatLng latLng, double radius) {
		
		circleOptions = new CircleOptions()
			.center(latLng)
			.radius(radius)
			.strokeColor(Color.BLACK)
			.fillColor(Color.argb(255, 255, 128, 0))
			.strokeWidth(1f);
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
