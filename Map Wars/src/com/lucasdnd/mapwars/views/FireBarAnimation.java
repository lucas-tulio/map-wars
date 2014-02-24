package com.lucasdnd.mapwars.views;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;

public class FireBarAnimation extends Animation implements AnimationListener {

	private View view;
	private int originalStartingHeight;
	private int originalEndingHeight;
	
	private int oldHeight;
	private int newHeight;
	private int diff;
	
	 public FireBarAnimation(View view, int newHeight) {
		 
		 // Basic stuff
		 this.setAnimationListener(this);
		 this.view = view;
	     this.oldHeight = view.getLayoutParams().height;
	     this.newHeight = newHeight;
	     diff = newHeight - oldHeight;
	     
	     // Save the Original heights so we can restart the animation
	     originalStartingHeight = view.getLayoutParams().height;
	     originalEndingHeight = newHeight;
	     
	     // Reverse
	     this.setRepeatMode(Animation.REVERSE);
	     this.setRepeatCount(Animation.INFINITE);
	 }

	 @Override
	 protected void applyTransformation(float interpolatedTime, Transformation t) {
	     view.getLayoutParams().height = oldHeight + (int)(diff * interpolatedTime);
	     view.requestLayout();
	 }

	 @Override
	 public void initialize(int width, int height, int parentWidth, int parentHeight) {
	     super.initialize(width, height, parentWidth, parentHeight);
	 }

	 @Override
	 public boolean willChangeBounds()  {
	     return true;
	 }

	@Override
	public void onAnimationStart(Animation animation) {}

	@Override
	public void onAnimationEnd(Animation animation) {
		oldHeight = originalStartingHeight;
		newHeight = originalEndingHeight;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}
}
