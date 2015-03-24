package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.example.animatetest.R;
import com.example.animation.MyAnimation;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=View.inflate(this, R.layout.activity_splash, null);
		RelativeLayout view1=new RelativeLayout(this);
		view1.addView(view);
		view1.setBackgroundResource(R.drawable.splash_load);
		setContentView(view1);
		MyAnimation openAnimation=new MyAnimation();
		view.startAnimation(openAnimation);
		openAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent =new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	}


    @Override
    protected void onPause() {
        super.onPause();
    }
}
