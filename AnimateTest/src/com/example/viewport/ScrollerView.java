package com.example.viewport;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ScrollView;

import com.example.controller.ScrollerController;

/**
 * 类描述：上下滑动翻页
 * 
 * @Package com.example.viewport
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-3 下午3:18:10
 */
public class ScrollerView extends ScrollView {

    public ScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
    }

    private ScrollerController controller;
    /** touch事件down时的x轴坐标 */
    private float mTouchDownX;
    /** touch事件down时的y轴坐标 */
    private float mTouchDownY;
    /** 一次完整touch事件中上一次的x轴坐标 */
    private float mLastTouchX;
    /** 一次完整touch事件中上一次的y轴坐标 */
    private float mLastTouchY;
    /** 加速度计算工具 */
    private VelocityTracker mVelocityTracker;
    /*
     * (non-Javadoc)
     * 
     * @see android.widget.ScrollView#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchDownX = event.getX();
            mTouchDownY = event.getY();
            
            abortAnimation();
            break;
        case MotionEvent.ACTION_MOVE:

            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            mLastTouchX=event.getX();
            mLastTouchY=event.getY();
            controller.handleVelocity();
            
//            if (calcTouchArea((int) mLastTouchX, (int) mLastTouchY) == ClickAction.PREV_PAGE) {
//                startAnimation(scrollViewH);
//            } else if (calcTouchArea((int) mLastTouchX, (int) mLastTouchY) == ClickAction.NEXT_PAGE) {
//                startAnimation(-scrollViewH);
//            }
            break;
        default:
            break;
        }
        return true;
    }
    
    
    
    /** 终止翻页特效 */
    private void abortAnimation() {
//        if (!mModeScroller.isFinished()) {
//            // 停止动画 上下翻页时在这里停止加速度
//            mFlingRunnable.endFling(true);
//            mModeScroller.abortAnimation();
//        }
    }
    
    

}
