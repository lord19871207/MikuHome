package com.example.viewport;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * 
 * 类描述：绕Y轴旋转 双面图片展示
 * 
 * 用法： 
 * ImageView img1=new ImageView(this); 
 * img1.setImageResource(R.drawable.icon_two_side_view_img1); 
 * ImageView img2=new ImageView(this);
 * img2.setImageResource(R.drawable.icon_two_side_view_img2); 
 * TwoSidedView tsv=new TwoSidedView(this,img1, img2, 2500);
 * 
 * @Package com.example.viewport
 * @ClassName: TwoSidedView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-4 下午11:48:34
 */
public class TwoSidedView extends RelativeLayout {

    private View frontView;
    private View backView;
    private int duration;
    ViewAnimation animFront;
    ViewAnimation animBack;

    public TwoSidedView(Context context, View frontView, View backView, int duration) {
        super(context);
        this.frontView = frontView;
        this.backView = backView;
        animFront = new ViewAnimation();
        animFront.flag = ViewAnimation.FRONT_ANIM;
        animFront.setFillAfter(true);
        animBack = new ViewAnimation();
        animBack.flag = ViewAnimation.BACK_ANIM;
        animBack.setFillAfter(true);
        this.duration = duration;
        this.addView(frontView);
        this.addView(backView);

        frontView.setOnClickListener(mOnclickListener);
        backView.setOnClickListener(mOnclickListener);
        this.removeAllViews();
        this.addView(backView);
        this.addView(frontView);
    }

    View.OnClickListener mOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.equals(frontView))
            {
                frontView.startAnimation(animFront);
                backView.startAnimation(animBack);

            }
        }
    };

    class ViewAnimation extends Animation {
        private static final int FRONT_ANIM = 0;
        private static final int BACK_ANIM = 1;
        int mCenterX;//
        int mCenterY;
        Camera camera = new Camera();
        int flag;

        public ViewAnimation() {
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCenterX = width / 2;
            mCenterY = height / 2;
            setDuration(duration);
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            final Matrix matrix = t.getMatrix();
            camera.save();
            if (flag == FRONT_ANIM)
            {
                if (interpolatedTime <= (0.5))
                {
                    camera.rotateY(interpolatedTime * 2 * 90);
                    System.out.println("time:" + interpolatedTime * 2 * 90);
                }
                else
                {
                    camera.rotateY(90);
                }
            }
            else if (flag == BACK_ANIM)
            {
                if (interpolatedTime >= 0.5)
                {
                    camera.rotateY((float) (270 + (interpolatedTime - 0.5) * 90 * 2));
                }
                else
                {
                    camera.rotateY(270);
                }

            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-mCenterX, -mCenterY);
            matrix.postTranslate(mCenterX, mCenterY);

        }
    }
}
