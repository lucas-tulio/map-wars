package com.lucasdnd.mapwars.views;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;

public class FireBarAnimation extends Animation implements AnimationListener {

	private View view;
	private int startingHeight;
	private int diff;
	
	 public FireBarAnimation(View view, int finalHeight) {
		 
		 // Basic stuff
		 this.setAnimationListener(this);
		 this.view = view;
	     diff = finalHeight - startingHeight;
	     
	     // Save the Original height
	     startingHeight = view.getLayoutParams().height;
	     
	     // Reverse
	     this.setRepeatMode(Animation.REVERSE);
	     this.setRepeatCount(Animation.INFINITE);
	     this.setInterpolator(new LinearInterpolator());
	 }

	 @Override
	 protected void applyTransformation(float interpolatedTime, Transformation t) {
	     view.getLayoutParams().height = startingHeight + (int)(diff * interpolatedTime);
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
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}
}
