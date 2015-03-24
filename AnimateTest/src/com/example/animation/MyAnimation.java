package com.example.animation;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class MyAnimation extends Animation {

	int mCenterX;
	int mCenterY;
	Camera camera = new Camera();

	public MyAnimation() {
	}

	public MyAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCenterX = width / 2;
		mCenterY = height / 2;
		setDuration(1200);
		setFillAfter(true);
		// setRepeatCount(100);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		Matrix matrix = t.getMatrix();
		camera.save();
		camera.translate(0.0f, 0.0f, (1300 - 1300.0f * interpolatedTime));
		camera.rotateY(360 * interpolatedTime);
		//camera.rotateZ(360 * interpolatedTime);
		//camera.rotateX(360 * interpolatedTime);
		camera.getMatrix(matrix);
		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);
		camera.restore();
	}

}
