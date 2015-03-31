package com.example.activity;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.render.GLRender;

public class CubeActivity extends Activity {
    
    GLRender render = new GLRender(this);
    
    private float mPreviousX;
    private float mPreviousY;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new GLSurfaceView(this);
        
        glView.setRenderer(render);
        setContentView(glView);
    }
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        render.onKeyUp(keyCode, event);
        return false;
    }
     public boolean onTrackballEvent(MotionEvent e) {
         render.xrot += e.getX() * TRACKBALL_SCALE_FACTOR;
         render.yrot += e.getY() * TRACKBALL_SCALE_FACTOR;
            return true;
        }
         public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                render.xrot += dx * TOUCH_SCALE_FACTOR;
                render.yrot += dy * TOUCH_SCALE_FACTOR;
            }
            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
}
