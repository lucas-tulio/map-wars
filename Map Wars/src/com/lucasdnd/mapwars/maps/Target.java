package com.lucasdnd.mapwars.maps;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class Target {
	
	private Marker marker;
	private MarkerOptions markerOptions;
	
	public Target(Location userLocation) {
		
		// Create the polygon
		markerOptions = new MarkerOptions()
			.position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
			.title("aeaeae")
			.visible(false);
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public MarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(MarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}

}
