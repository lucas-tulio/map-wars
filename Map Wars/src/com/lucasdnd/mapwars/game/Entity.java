package com.lucasdnd.mapwars.game;

import android.graphics.Color;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class Entity {
	
	private CircleOptions circleOptions;
	
	public Entity(LatLng latLng) {

		circleOptions = new CircleOptions()
			.center(latLng)
			.radius(100.0)
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
}
