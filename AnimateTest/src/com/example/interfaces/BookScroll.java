package com.example.interfaces;

import android.graphics.Bitmap;
import android.view.VelocityTracker;

/**
 * 类描述：
 * @Package com.example.interfaces
 * @ClassName: BookScroll
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-3 下午4:26:16
 */
public interface BookScroll {
    /**
     * 方法描述：加载控件中的bitmap图片
     * 
     * @param int type为返回的bitmap是哪一页 0 1
     * @return void
     * @date 2015-3-31 上午10:13:51
     */
    public Bitmap loadBitmap(int type);
    
    /**
     * 方法描述：获取背景主题
     * 
     * @return int
     * @date 2015-3-31 上午11:15:27
     */
    public int getCurrentSimulationTheme();

    /**
     * 方法描述：动画结束时需要执行的操作
     * 
     * @return void
     * @date 2015-3-31 上午11:37:48
     */
    public void onAnimationEnd();
    
    /**
     * 
     * 方法描述：返回加速度计
     * @author 尤洋
     * @Title: getVelocityTracker
     * @return
     * @return VelocityTracker
     * @date 2015-4-3 下午4:33:28
     */
    public VelocityTracker getVelocityTracker();
    /**
     * 方法描述：获取滑动方向，返回数字1  表示从右向左滑动
     * @author 尤洋
     * @return int
     * @date 2015-3-31 下午1:11:58
     */
    public int getDirection() ;
}
