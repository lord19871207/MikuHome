package com.example.controller;

import android.view.VelocityTracker;

import com.example.interfaces.BookScroll;

/**
 * 类描述：
 * @Package com.example.controller
 * @ClassName: ScrollerControl
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-3 下午4:02:53
 */
public class ScrollerController {
    
    
    private BookScroll bookScroll;
    private VelocityTracker mVelocityTracker;
    /**
     * 构造方法描述：
     * @Title: ScrollerControl
     * @date 2015-4-3 下午4:02:54
     */
    public ScrollerController() {
        
    }
    
    /** 控制是否开启加速度逻辑 */
    private boolean isComputeScroll = false;
    
    public void setImpl(BookScroll bookScroll){
        this.bookScroll=bookScroll;
        mVelocityTracker=bookScroll.getVelocityTracker();
        
    }
    /**
     * 
     * 处理上下翻页加速度
     * 
     * @Title: handleVelocity
     * @return void
     * @date 2015-3-18 下午4:02:45
     */
    public void handleVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocityY = (int) mVelocityTracker.getYVelocity();
        // Log4an.e("yyy", "velocityY=" + Math.abs(velocityY));
        if (Math.abs(velocityY) > 500) {
            isComputeScroll = true;
//            startScollAnimation(0, false);
        } else {
            isComputeScroll = false;
        }
        // }

    }
    

}
