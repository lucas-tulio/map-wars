package com.lucasdnd.mapwars;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.lucasdnd.mapwars.maps.GridTileProvider;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Menu;

public class MainActivity extends Activity implements OnCameraChangeListener {

	private GoogleMap map;
	private GridTileProvider gridTileProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Setup the Map
		this.setupMap();
		
		// Create the TileProvider
		gridTileProvider = new GridTileProvider(Color.argb(255,255,80,0), 1f);
		
		// Add the Tile Overlay to the map using our Grid Tile Provider
		TileOverlay tileOverlay = map.addTileOverlay(new TileOverlayOptions ().tileProvider (gridTileProvider));
		
		// Add a Camera Listener. We use the Camera Listener to update the Current Location used by the Tile Provider.
		// The Tile Provider needs an updated location to calculate the Grid heights at different Latitudes.
		map.setOnCameraChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Starts Google Maps and set basic stuff
	 */
	private void setupMap() {
		
		// Check if we already have a Map
		if(map == null) {
			
			// Get the Map View
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mainActivity_map)).getMap();
			
			// Check if we're good to go
			if(map == null) {
				System.out.println("Could not start the Map");
				return;
			}
			
			// Enable My Location Button
			map.setMyLocationEnabled(true);
		}
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		gridTileProvider.setCurrentLatLng(map.getCameraPosition().target);
	}

}
