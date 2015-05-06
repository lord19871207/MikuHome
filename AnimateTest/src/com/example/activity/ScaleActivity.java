package com.example.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.animatetest.R;
import com.example.animation.FlipAnimation;
import com.example.common.util.Utils;

/**
 * 类描述：
 * 
 * @Package com.example.activity
 * @ClassName: ScaleActivity
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-5-5 下午7:32:08
 */
public class ScaleActivity extends Activity {
    private ImageView im;
    private ImageView im1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagescale);
        im1 = (ImageView) findViewById(R.id.scaleimage);
        im = (ImageView) findViewById(R.id.scaleimage1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        im.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                float t = Utils.getScreenSize(ScaleActivity.this)[0] / im.getWidth();
                float t1 = Utils.getScreenSize(ScaleActivity.this)[1] / im.getHeight();
                float left = im.getX();
                float right = Utils.getScreenSize(ScaleActivity.this)[0] - im.getWidth()-left;
                float top = im.getY();
                float bottom = Utils.getScreenSize(ScaleActivity.this)[1] - im.getWidth()-top;

                float centerx = left / (left + right);
                float centrty = top / (top + bottom);

                final ScaleAnimation animation = new ScaleAnimation(1.0f, t, 1.0f, t1,
                        Animation.RELATIVE_TO_SELF, centerx, Animation.RELATIVE_TO_SELF, centrty);

                animation.setDuration(2000);// 设置动画持续时间
                /** 常用方法 */
                // animation.setRepeatCount(int repeatCount);//设置重复次数
                animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
                im.startAnimation(animation);
                // Utils.showAnimation(im, FlipAnimation.ROTATEY);
            }
        });

    }
}
