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
import com.lucasdnd.mapwars.game.Entity;
import com.lucasdnd.mapwars.game.LocationUtil;
import com.lucasdnd.mapwars.maps.GridTileProvider;
import com.lucasdnd.mapwars.views.OnHoldDownListener;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends Activity implements OnCameraChangeListener, LocationListener {

	// Map
	private GoogleMap map;
	private GridTileProvider gridTileProvider;
	private int playZoomLevel = 13;
	
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
		rotateRightButton.setOnTouchListener(new OnHoldDownListener(map, +0.01f));
		
		Button rotateLeftButton = (Button) this.findViewById(R.id.mainActivity_rotateLeft);
		rotateLeftButton.setOnTouchListener(new OnHoldDownListener(map, -0.01f));
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
			
			// Enable location, disable gestures
			map.setMyLocationEnabled(true);
			map.getUiSettings().setAllGesturesEnabled(false);
			
			// Get Location Manager
			LocationManager locationManager = (LocationManager)this.getBaseContext().getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000l, 100f, this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000l, 100f, this);
			
			// Apply max tilt
			CameraPosition cameraPos = new CameraPosition(map.getCameraPosition().target, playZoomLevel, 90, 0);
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
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

	@Override
	public void onLocationChanged(Location location) {
		
		// Go to the User Location
		CameraPosition cameraPos = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), playZoomLevel, 30, 0);
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
