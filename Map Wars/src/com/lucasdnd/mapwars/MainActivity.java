package com.lucasdnd.mapwars;

import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.lucasdnd.mapwars.game.Entity;
import com.lucasdnd.mapwars.game.LocationUtil;
import com.lucasdnd.mapwars.maps.GridTileProvider;
import com.lucasdnd.mapwars.views.OnHoldDownListener;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnCameraChangeListener {

	// Map
	private GoogleMap map;
	private GridTileProvider gridTileProvider;
	
	// Targets
	private ArrayList<Entity> targets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Setup Google Maps
		this.setupMap();
		
		// Create the TileProvider
		gridTileProvider = new GridTileProvider(Color.argb(128,0,128,128), 1f);
		
		// Add the Tile Overlay to the map using our Grid Tile Provider
		TileOverlay tileOverlay = map.addTileOverlay(new TileOverlayOptions ().tileProvider (gridTileProvider));
		
		// Add a Camera Listener. We use the Camera Listener to update the Current Location used by the Tile Provider.
		// The Tile Provider needs an updated location to calculate the Grid heights at different Latitudes.
		map.setOnCameraChangeListener(this);
		
		// Targets!
		targets = new ArrayList<Entity>();
		
		// Setup views
		this.setupViews();
		
		
	}

	/**
	 * Button Actions
	 */
	private void setupViews() {
		
		Button rotateRightButton = (Button) this.findViewById(R.id.mainActivity_rotateRight);
		rotateRightButton.setOnTouchListener(new OnHoldDownListener(map, 0.1f));
		
		Button rotateLeftButton = (Button) this.findViewById(R.id.mainActivity_rotateLeft);
		rotateLeftButton.setOnTouchListener(new OnHoldDownListener(map, -0.1f));
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
			
			// Enable location, disable tilt
			map.setMyLocationEnabled(true);
			
		}
	}
	
	/**
	 * Creates targets around the user
	 * 
	 * @param numTargets
	 * @param distance
	 */
	private void createTargets(LatLng latLng, int numTargets) {
		
		for(int i = 0; i < numTargets; i++) {
			
			// Create the Targets
			Entity target = new Entity(LocationUtil.getRandomLatLng(latLng, 0.03, 0.01));
			target.setCircle(map.addCircle(target.getCircleOptions()));
			targets.add(target);
		}
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		gridTileProvider.setCurrentLatLng(map.getCameraPosition().target);
	}
}
