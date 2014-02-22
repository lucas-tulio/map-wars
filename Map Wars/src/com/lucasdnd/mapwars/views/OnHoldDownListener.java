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

	public OnHoldDownListener(GoogleMap map, float rotationAngle) {
		this.map = map;
		this.rotationAngle = rotationAngle;
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
            break;
        }
        return false;
    }

    Runnable mAction = new Runnable() {
        @Override public void run() {
            this.rotate();
            handler.postDelayed(this, 10);
        }
        
        private void rotate() {
    		CameraPosition cameraPos = new CameraPosition(map.getCameraPosition().target, map.getCameraPosition().zoom, map.getCameraPosition().tilt, map.getCameraPosition().bearing - rotationAngle);
    		map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
    	}
    };
}
