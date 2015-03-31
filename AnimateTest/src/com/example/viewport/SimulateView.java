package com.example.viewport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.animatetest.R;
import com.example.common.util.Utils;
import com.example.interfaces.BookSimulationPageFlip;
import com.example.interfaces.impl.BookSimulationPageFlip_impl;

/**
 * 类描述：
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
    private BookSimulationPageFlip_impl flip;
    private PointF pointf;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    
    
    public SimulateView(Context context) {
        super(context);
    }

    public SimulateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pointf=new PointF();
        bitmap = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 384, 512);
        bitmap1 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 384, 512);
        flip=new BookSimulationPageFlip_impl(context) ;
        flip.setNeedImpl(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            pointf.set(event.getX(),event.getY());
            break;
        case MotionEvent.ACTION_MOVE:
            Log.i(TAG, "MotionEvent.ACTION_MOVE");
            pointf.set(event.getX(),event.getY());
            flip.controllTouchPointWhenMove();
            
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            Log.i(TAG, "MotionEvent.ACTION_UP");
            pointf.set(event.getX(),event.getY());
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
            // TODO Auto-generated method stub
            return 1;
        }
    
//        public void computeScroll() {
//            if (pageTurnMode == PAGETURN_MODE_SMOOTH) {
//                return;
//            }
//            if (mModeScroller.computeScrollOffset()) {
//                mTouch.x = mModeScroller.getCurrX();
//                mTouch.y = mModeScroller.getCurrY();
//                if (mTouch.y >= mHeight - 1) {
//                    mTouch.y = mHeight - dI;
//                } else if (mTouch.y < 1) {
//                    mTouch.y = 1;
//                }
//
//                mLastTouchY = mModeScroller.getCurrY();
//                // mLastTouchX = mModeScroller.getCurrX();
//                postInvalidate();
//            } 
//        }
        
        /* (non-Javadoc)
         * @see android.view.View#onDraw(android.graphics.Canvas)
         */
        @Override
        protected void onDraw(Canvas canvas) {
            flip.nextPage(canvas);
        }

}
