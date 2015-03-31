package com.example.interfaces;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * 类描述：仿真翻页的接口类
 * @Package com.example.interfaces
 * @ClassName: BookSimulationPageFlip
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 上午10:11:17
 */
public interface BookSimulationPageFlip {
    /**
     * 方法描述：
     * 
     * @author 尤洋
     * @Title: getShadowColor
     * @return
     * @return int[]
     * @date 2015-3-31 上午11:56:50
     */
    public int[] getShadowColor();

    /**
     * 方法描述：加载控件中的bitmap图片
     * 
     * @param int type为返回的bitmap是哪一页 0 1
     * @return void
     * @date 2015-3-31 上午10:13:51
     */
    public Bitmap loadBitmap(int type);

    /**
     * 方法描述：设置触摸点的坐标
     * 
     * @param x
     * @param y
     * @return Point
     * @date 2015-3-31 上午10:46:51
     */
    public PointF getTouchPoint();

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
     * 方法描述：获取滑动方向，返回数字1  表示从右向左滑动
     * @author 尤洋
     * @return int
     * @date 2015-3-31 下午1:11:58
     */
    public int getDirection() ;
    
    

}
