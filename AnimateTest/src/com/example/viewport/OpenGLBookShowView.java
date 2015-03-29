package com.example.viewport;

import javax.microedition.khronos.opengles.GL10;

import com.example.animatetest.R;
import com.example.animatetest.Square;
import com.example.render.OpenGLRender;
import com.example.render.OpenGLRender.IOpenGLDemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 类描述：
 * 
 * @Package com.example.viewport
 * @ClassName: OpenGLBookShowView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-28 下午11:13:55
 */
public class OpenGLBookShowView extends GLSurfaceView implements IOpenGLDemo{
    private int angle;
    private Square square;
    private Square square1;
    private Square square2;
    private boolean isOntouch=true;
    private PointF start;
    private boolean isLeft;
    /**
     * 构造方法描述：
     * 
     * @Title: OpenGLBookShowView
     * @param context
     * @date 2015-3-28 下午11:13:55
     */
    public OpenGLBookShowView(Context context) {
        this(context,null);
    }

    /**
     * 构造方法描述：
     * 
     * @Title: OpenGLBookShowView
     * @param context
     * @param attrs
     * @date 2015-3-28 下午11:13:55
     */
    public OpenGLBookShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        angle = 120;
        square = new Square();
        square1 = new Square();
        square2 = new Square();
        square.loadBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.bu1));
        square1.loadBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.bu3));
        square2.loadBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.qian));
        
        start = new PointF();
        setRenderer(new OpenGLRender(this));
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            isOntouch=false;
            angle=angle+120;
            start.set(event.getX(), event.getY());
            Log.i("youyang", "-----isOntouch=false;-----------");
            break;
        case MotionEvent.ACTION_MOVE:
            isOntouch=false;
            float dx = event.getX() - start.x;
            if(dx>0){
                isLeft = true;
                angle=angle-3;
            }else{
                isLeft = false;
                angle=angle+3; 
            }
            
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            isOntouch=true;
            Log.i("youyang", "-----isOntouch=true;-----------");
            break;

        default:
            break;
        }

        return true;
    }

    
    @Override
    public void drawScene(GL10 gl) {
        
        if(isOntouch){
            if(isLeft){
                angle--;
            }else{
                angle++;
            }
            
        }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -6);
        gl.glPushMatrix();
        gl.glRotatef(-angle, 0, 1, 0);
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(-angle, 0, -1, 0);
        gl.glScalef(.5f, .5f, .5f);
        square.draw(gl);

        /*
         * 要理解（先旋转再平移） 和 （先平移再旋转） 的区别 角度递增的情况下 先旋转再平移的话 其实是以平移的距离为半径 进行旋转 而先平移再旋转 其实是自转 原因是 永远都按照 旋转之后的坐标系进行平移
         */
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(120 - angle, 0, 1, 0);
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(120 - angle, 0, -1, 0);
        gl.glScalef(.5f, .5f, .5f);
        square1.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(240 - angle, 0, 1, 0);
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(240 - angle, 0, -1, 0);
        gl.glScalef(.5f, .5f, .5f);
        square2.draw(gl);
        gl.glPopMatrix();
        
        
//        
    }
}
