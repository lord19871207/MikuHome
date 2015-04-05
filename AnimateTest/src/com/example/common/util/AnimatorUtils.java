package com.example.common.util;
import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 
 * 类描述：nineold 属性动画工具类
 * 用法：
 * ValueAnimator colorAnim = ObjectAnimator.ofFloat(myView, "scaleX", 0.3f);
   colorAnim.setDuration(1000);
   colorAnim.start();
   
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-5 上午11:04:58
 */
public class AnimatorUtils {

    public static ObjectAnimator rotationCloseToRight(View v) {
        return ObjectAnimator.ofFloat(v, "rotationY", 0, -90);
    }

    public static ObjectAnimator rotationOpenFromRight(View v) {
        return ObjectAnimator.ofFloat(v, "rotationY", -90, 0);
    }

    public static ObjectAnimator rotationCloseVertical(View v) {
        return ObjectAnimator.ofFloat(v, "rotationX", 0, -90);
    }

    public static ObjectAnimator rotationOpenVertical(View v) {
        return ObjectAnimator.ofFloat(v, "rotationX", -90, 0);
    }

    public static ObjectAnimator alfaDisappear(View v) {
        return ObjectAnimator.ofFloat(v, "alpha", 1, 0);
    }

    public static ObjectAnimator alfaAppear(View v) {
        return ObjectAnimator.ofFloat(v, "alpha", 0, 1);
    }

    public static ObjectAnimator translationRight(View v, float x) {
        return ObjectAnimator.ofFloat(v, "translationX", 0, x);
    }
    public static ObjectAnimator translationLeft(View v, float x) {
        return ObjectAnimator.ofFloat(v, "translationX", x, 0);
    }

    public static AnimatorSet fadeOutSet(View v, float x){
        AnimatorSet fadeOutSet = new AnimatorSet();
        fadeOutSet.playTogether(alfaDisappear(v), translationRight(v,x));
        return fadeOutSet;
    }

}
