package com.example.viewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.example.animatetest.R;
import com.example.common.util.Utils;
import com.example.controller.AutoScrollerController;
import com.example.interfaces.BookSimulationPageFlip;

/**
 * 类描述：
 * @Package com.example.viewport
 * @ClassName: AutoScrollerView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-2 下午11:06:10
 */
public class AutoScrollerView extends View implements BookSimulationPageFlip{
    /** 缓存 */
    private Picture defaultPagePicture = new Picture();
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private AutoScrollerController controll;
    
    public AutoScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        controll=new AutoScrollerController(context, this);
        
        bitmap = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 512, 720);
        bitmap1 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 512, 720);
        controll.setNeedImpl(this);
        controll.startAutoScroll();
    }
    
    /** 获得默认画面的Picture */
    public Picture getDefaultPagePicture() {
        return defaultPagePicture;
    }

    
    /* (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        defaultPagePicture.draw(canvas);
        controll.draw1(canvas);
        controll.draw2(canvas);
        controll.draw3(canvas);
    }

    /* (non-Javadoc)
     * @see com.example.interfaces.BookSimulationPageFlip#getShadowColor()
     */
    @Override
    public int[] getShadowColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bitmap loadBitmap(int type) {
        switch (type) {
        case 0:
            
            return bitmap;
        case 1:
            
            return bitmap1;

        default:
            break;
        }
        return bitmap;
    }

    @Override
    public PointF getTouchPoint() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.example.interfaces.BookSimulationPageFlip#getCurrentSimulationTheme()
     */
    @Override
    public int getCurrentSimulationTheme() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.example.interfaces.BookSimulationPageFlip#getDirection()
     */
    @Override
    public int getDirection() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public void onAnimationEnd() {
    }
    
    
}
