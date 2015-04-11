package com.example.viewport;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.animatetest.R;
import com.example.common.util.Utils;
import com.example.controller.BookSimulationControl;
import com.example.interfaces.BookSimulationPageFlip;

/**
 * 类描述：仿真翻页的控件
 * 
 * @Package com.example.viewport
 * @ClassName: SimulateView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 下午2:10:37
 */
public class SimulateView extends View implements BookSimulationPageFlip{
    private static final  String TAG="SimulateView";
    private Activity context;
    private BookSimulationControl flip;
    private PointF pointf;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    /** touch事件down时的x轴坐标 */
    private float mTouchDownX;
    /** touch事件down时的y轴坐标 */
    private float mTouchDownY;
    /** 一次完整touch事件中上一次的x轴坐标 */
    private float mLastTouchX;
    /** 一次完整touch事件中上一次的y轴坐标 */
    private float mLastTouchY;
    
    
    public SimulateView(Context context) {
        super(context);
    }

    public SimulateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pointf=new PointF();
        
        bitmap = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 512, 720);
        bitmap1 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 512, 720);
        flip=new BookSimulationControl(context,this) ;
        flip.setNeedImpl(this);
        
        try {
            Class<View> c = View.class;
            Method setLayerTypeMethod = c.getDeclaredMethod("setLayerType", int.class, Paint.class);
            if (setLayerTypeMethod != null) {
                int layerType = 1; // View.LAYER_TYPE_SOFTWARE
                setLayerTypeMethod.invoke(this, layerType, null);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointf.set(event.getX(),event.getY());
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchDownX=event.getX();
            mTouchDownY=event.getY();
            
            break;
        case MotionEvent.ACTION_MOVE:
            Log.i(TAG, "MotionEvent.ACTION_MOVE");
            flip.controllTouchPointWhenMove();
            
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            mLastTouchX=event.getX();
            mLastTouchY=event.getY();
            Log.i(TAG, "MotionEvent.ACTION_UP");
            flip.controllTouchPointWhenUp();
            break;
        default:
            break;
        }
        invalidate();
        return true;
    }
        
        @Override
        public void onAnimationEnd() {
            
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
            return pointf;
        }
        
        @Override
        public int[] getShadowColor() {
            return new int[] {0xff242424, 0x242424};
        }
        
        @Override
        public int getDirection() {
            return 1;
        }
        
        @Override
        public int getCurrentSimulationTheme() {
            return 1;
        }
    
        @Override
        protected void onDraw(Canvas canvas) {
            flip.nextPage(canvas);
        }

}
