package com.lucasdnd.mapwars.views;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnHoldDownListener implements OnTouchListener {

	private GoogleMap map;
	private float rotationAngle;
	private Handler handler;
	
	private float currentAcceleration = 0f;
	private float acceleration = 0.01f;
	private float maxAcceleration = 5f;

	public OnHoldDownListener(GoogleMap map, float rotationAngle) {
		this.map = map;
		this.rotationAngle = rotationAngle;
		
		if(rotationAngle < 0) {
			acceleration *= -1;
			maxAcceleration *= -1;
		}
	}
	
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        
    	switch(event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (handler != null) return true;
            handler = new Handler();
            handler.postDelayed(mAction, 10);
            break;
        case MotionEvent.ACTION_UP:
            if (handler == null) return true;
            handler.removeCallbacks(mAction);
            handler = null;
            currentAcceleration = 0f;
            break;
        }
        return false;
    }

    Runnable mAction = new Runnable() {
    	
        @Override
        public void run() {
            this.rotate();
            handler.postDelayed(this, 10);
        }
        
        private void rotate() {
        	
        	if(rotationAngle > 0) {
	        	if(currentAcceleration <= maxAcceleration) {
	        		currentAcceleration += acceleration;
	        	}
        	} else {
        		if(currentAcceleration >= maxAcceleration) {
	        		currentAcceleration += acceleration;
	        	}
        	}
    		CameraPosition cameraPos = new CameraPosition(map.getCameraPosition().target, map.getCameraPosition().zoom, map.getCameraPosition().tilt, map.getCameraPosition().bearing + rotationAngle + currentAcceleration);
    		map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
    	}
    };
}
