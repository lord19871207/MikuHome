package com.example.viewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.animatetest.R;
import com.example.animation.FlipAnimation;
import com.example.common.util.Utils;
import com.example.controller.AutoScrollerController;
import com.example.interfaces.BookSimulationPageFlip;

/**
 * 类描述：自动滚屏翻页控件
 * 
 * @Package com.example.viewport
 * @ClassName: AutoScrollerView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-2 下午11:06:10
 */
public class AutoScrollerView extends View implements BookSimulationPageFlip {
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;//分割线
    private Bitmap bitmap3;//整页图片作为分割线
    private AutoScrollerController controll;
    private int type=1;
    private int flag=0;

    public AutoScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        controll = new AutoScrollerController(context, this);
        bitmap = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 512, 720);
        bitmap1 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 512, 720);
        bitmap2 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.line1, 480, 10);
        bitmap3 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.cat, 512, 720);
        controll.setNeedImpl(this);
        
        controll.startAutoScroll(type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        controll.draw1(canvas);
        controll.draw2(canvas);
        controll.draw3(canvas,type);
    }

    @Override
    public int[] getShadowColor() {
        return null;
    }

    @Override
    public Bitmap loadBitmap(int type) {
        switch (type) {
        case 0:
            return bitmap;
        case 1:
            return bitmap1;
        case 2:
            return bitmap2;
        case 3:
            return bitmap3;
        default:
            break;
        }
        return bitmap;
    }

    @Override
    public PointF getTouchPoint() {
        return null;
    }

    @Override
    public int getCurrentSimulationTheme() {
        return 0;
    }

    @Override
    public int getDirection() {
        return 0;
    }

    public void onAnimationEnd() {
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
            controll.setAutoScrollOffset((int)event.getY());
            if(event.getAction()==MotionEvent.ACTION_UP){
                flag++;
                if(flag%2==0){
                    type=0;  
                }else{
                    type=1;
                }
//                controll.startAutoScroll(type);
//                FlipAnimation openAnimation=new FlipAnimation(90,270,0,200);
//                startAnimation(openAnimation);
                
            }
        return true;
    }

}
