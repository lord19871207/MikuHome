package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.animatetest.R;
import com.example.animation.YRotateAnimation;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=View.inflate(this, R.layout.activity_splash, null);
		RelativeLayout view1=new RelativeLayout(this);
		view1.addView(view);
		view1.setBackgroundResource(R.drawable.cat);
		
		final ScaleAnimation animation =new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
		        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		        animation.setDuration(2000);//设置动画持续时间
		        /** 常用方法 */
		        //animation.setRepeatCount(int repeatCount);//设置重复次数
		        //animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状态 
		
		
		
		
		
		setContentView(view1);
		YRotateAnimation openAnimation=new YRotateAnimation();
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
