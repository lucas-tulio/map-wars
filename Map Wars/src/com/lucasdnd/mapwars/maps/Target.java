package com.lucasdnd.mapwars.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Target {
	
	private Polygon target;
	private PolygonOptions targetOptions;
	
	public Target(LatLng userLocation) {
		
		// Create the polygon
		targetOptions = new PolygonOptions()
        .add(new LatLng(userLocation.latitude + 0.005, userLocation.longitude),
             new LatLng(userLocation.latitude+ 0.015, userLocation.longitude+ 0.005),
             new LatLng(userLocation.latitude+ 0.025, userLocation.longitude+ 0.0012),
             new LatLng(userLocation.latitude+ 0.035, userLocation.longitude+ 0.0018));
	}

	public Polygon getTarget() {
		return target;
	}

	public void setTarget(Polygon target) {
		this.target = target;
	}

	public PolygonOptions getTargetOptions() {
		return targetOptions;
	}

	public void setTargetOptions(PolygonOptions targetOptions) {
		this.targetOptions = targetOptions;
	}
}
