package com.example.viewport;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类描述：
 * 
 * @Package com.example.viewport
 * @ClassName: SimulateView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 下午2:10:37
 */
public class SimulateView extends GLSurfaceView {

    public SimulateView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public SimulateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:

            break;
        case MotionEvent.ACTION_MOVE:

            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:

            break;
        default:
            break;
        }
        return true;
    }
    
    
    

}
