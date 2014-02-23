package com.lucasdnd.mapwars;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.lucasdnd.mapwars.game.LocationUtil;
import com.lucasdnd.mapwars.maps.Enemy;
import com.lucasdnd.mapwars.maps.GridTileProvider;
import com.lucasdnd.mapwars.views.OnHoldDownListener;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnCameraChangeListener, LocationListener {

	// Map
	private GoogleMap map;
	private GridTileProvider gridTileProvider;
	private int playZoomLevel = 13;
	private TileOverlay tileOverlay;
	
	// The first time the Camera zooms in the User location, the TileOverlay is drawn on the map using wrong Lat/Lng
	// values, causing the grid to be displayed incorrectly. At the first location change, we need to clear the Tile
	// and draw it again
	private boolean shouldClearTileOverlay = true;
	
	// Targets
	private ArrayList<Enemy> targets;
	
	// Control Mode. Camera mode allows map gestures
	private boolean isCameraMode = true;
	
	// User Location
	private Location userLocation;
	
	// Views
	Button rotateRightButton, rotateLeftButton, fireButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Setup Google Maps
		this.setupMap();
		
		// Create the TileProvider
		gridTileProvider = new GridTileProvider(Color.argb(128,0,128,128), 1f);
		
		// Add the Tile Overlay to the map using our Grid Tile Provider
		tileOverlay = map.addTileOverlay(new TileOverlayOptions ().tileProvider (gridTileProvider));
		
		// Add a Camera Listener. We use the Camera Listener to update the Current Location used by the Tile Provider.
		// The Tile Provider needs an updated location to calculate the Grid heights at different Latitudes.
		map.setOnCameraChangeListener(this);
		
		// Targets!
		targets = new ArrayList<Enemy>();
		
		// Setup views
		this.setupViews();
	}

	/**
	 * Button Actions
	 */
	private void setupViews() {
		
		rotateRightButton = (Button) this.findViewById(R.id.mainActivity_rotateRightButton);
		rotateRightButton.setOnTouchListener(new OnHoldDownListener(map, +0.01f));
		
		rotateLeftButton = (Button) this.findViewById(R.id.mainActivity_rotateLeftButton);
		rotateLeftButton.setOnTouchListener(new OnHoldDownListener(map, -0.01f));
		
		fireButton = (Button) this.findViewById(R.id.mainActivity_fireButton);
		fireButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Which mode?
				if(isCameraMode) {
					
					// Trying to enter Fire Mode
					
					// Do we have the User Location?
					if(userLocation != null) {
						
						// Fire Mode!
						isCameraMode = false;
						
						// Lock Position!
						CameraPosition cameraPos = new CameraPosition(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), playZoomLevel, 90, map.getCameraPosition().bearing);
						map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
						
						// Disable map gestures
						map.getUiSettings().setAllGesturesEnabled(false);
						
						// Show the Targeting Buttons
						rotateRightButton.setVisibility(View.VISIBLE);
						rotateLeftButton.setVisibility(View.VISIBLE);
						
						((Button)v).setText("Click to enter Camera mode");
						
					} else {
						
						// Request location again
						requestLocations();
						
						// Show an alert saying we need the User Location
						new AlertDialog.Builder(v.getContext())
							.setTitle("Location Services")
							.setMessage("Need location to enter Fire mode")
							.create()
							.show();
					}
					
				} else {
					
					// Camera Mode!
					isCameraMode = true;
					
					// Do a slight zoom out
					CameraPosition cameraPos = new CameraPosition(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude), playZoomLevel - 1, 90, map.getCameraPosition().bearing);
					map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
					
					// Enable map gestures
					map.getUiSettings().setAllGesturesEnabled(true);
					
					// Hide the Targeting Buttons
					rotateRightButton.setVisibility(View.GONE);
					rotateLeftButton.setVisibility(View.GONE);
					
					((Button)v).setText("Click to enter Fire mode");
				}
			}
			
		});
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
			
			// Enable location
			map.setMyLocationEnabled(true);
			this.requestLocations();
		}
	}
	
	/**
	 * Request a single location update from both GPS and Network
	 */
	private void requestLocations() {
		LocationManager locationManager = (LocationManager)this.getBaseContext().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
		locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
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
			Enemy target = new Enemy(LocationUtil.getRandomLatLng(latLng, 0.03, 0.01));
			target.setCircle(map.addCircle(target.getCircleOptions()));
			targets.add(target);
		}
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		
		// Tell the TileOverlay where we are
		gridTileProvider.setCurrentLatLng(map.getCameraPosition().target);
		
//		// Clear the Tile Overlay to prevent the drawing bug
//		if(shouldClearTileOverlay) {
//			tileOverlay.clearTileCache();
//			shouldClearTileOverlay = false;
//		}
	}

	@Override
	public void onLocationChanged(Location location) {
		
		// Save the User Location
		userLocation = location;
		
		// Go to the User Location
		CameraPosition cameraPos = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), playZoomLevel - 1, 90, 0);
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}
}
