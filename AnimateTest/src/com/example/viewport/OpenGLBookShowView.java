package com.example.viewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.animatetest.R;
import com.example.common.util.Utils;
import com.example.graphmodel.Cube;
import com.example.graphmodel.Square;
import com.example.render.OpenGLRender;
import com.example.render.OpenGLRender.IOpenGLDemo;

/**
 * 类描述：三张图片绕Y轴旋转的3D 自定义控件
 * @Package com.example.viewport
 * @ClassName: OpenGLBookShowView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-28 下午11:13:55
 */
public class OpenGLBookShowView extends GLSurfaceView implements IOpenGLDemo, Runnable {
    private int angle;
    private Square square;
    private Square square1;
    private Square square2;
    private boolean isOntouch = false;
    private boolean isLeft;
    private int d;
    private boolean isplus;
    private float mPreviousX;
    private float y;
    private Cube cube;
    private Cube cube1;
    private Cube cube2;
    
    public OpenGLBookShowView(Context context) {
        this(context, null);
    }

    public OpenGLBookShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);//将view放到顶端
        setEGLConfigChooser(8,8,8,8,16,0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//设置透明
        if(attrs.getAttributeResourceValue(null, "modeltype", 0)!=0){
            init_model(1);
        }else{
            init_model(1);
        }
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); //
    }

    /**
     * 方法描述：初始化数据
     * @author 尤洋
     * @Title: init_model
     * @return void
     * @date 2015-3-30 上午3:56:34
     */
    private void init_model(int type) {
        angle = 90;
        Bitmap bitmap = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 100, 100);
        Bitmap bitmap1 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 100, 100);
        Bitmap bitmap2 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.qian, 100, 100);
        
        bitmap=getTexture(bitmap);
        bitmap1=getTexture(bitmap1);
        bitmap2=getTexture(bitmap2);
        if(type==0){
            square = new Square();
            square1 = new Square();
            square2 = new Square();
            square.loadBitmap(bitmap);
            square1.loadBitmap(bitmap1);
            square2.loadBitmap(bitmap2);
            setRenderer(new OpenGLRender(this));
        }else if(type==1){
            cube = new Cube();
            cube1 = new Cube();
            cube2 = new Cube();
            cube.loadBitmap(bitmap);
            cube1.loadBitmap(bitmap1);
            cube2.loadBitmap(bitmap2);
            setRenderer(new OpenGLRender(this));
        }
        
        
    }
    
    /**
     * Calculates the next highest power of two for a given integer.
     */
    private static int getNextHighestPO2(int n) {
        n -= 1;
        n = n | (n >> 1);
        n = n | (n >> 2);
        n = n | (n >> 4);
        n = n | (n >> 8);
        n = n | (n >> 16);
        n = n | (n >> 32);
        return n + 1;
    }

    /**
     * Generates nearest power of two sized Bitmap for give Bitmap. Returns this
     * new Bitmap using default return statement + original texture coordinates
     * are stored into RectF.
     */
    private static Bitmap getTexture(Bitmap bitmap) {
        // Bitmap original size.
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Bitmap size expanded to next power of two. This is done due to
        // the requirement on many devices, texture width and height should
        // be power of two.
        int newW = getNextHighestPO2(w);
        int newH = getNextHighestPO2(h);

        // TODO: Is there another way to create a bigger Bitmap and copy
        // original Bitmap to it more efficiently? Immutable bitmap anyone?
        Bitmap bitmapTex = Bitmap.createBitmap(newW, newH, bitmap.getConfig());
        Canvas c = new Canvas(bitmapTex);
        c.drawBitmap(bitmap, 0, 0, null);

        // Calculate final texture coordinates.
        float texX = (float) w / newW;
        float texY = (float) h / newH;
        return bitmapTex;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            removeCallbacks(this);
            isOntouch = false;
            //渲染器会不停地渲染场景
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); 
            break;
        case MotionEvent.ACTION_MOVE:
            y = event.getY();
            isOntouch = false;
            float dx = x - mPreviousX;
            if (dx > 0) {
                isLeft = true;
                angle = angle - 8;
            } else {
                isLeft = false;
                angle = angle + 8;
            }
            
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            isOntouch = false;
         // 设置为当数据变化时才更新界面
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); 

            int a = Math.abs(angle % 360 - 90);
            int b = Math.abs(angle % 360 - 210);
            int c = Math.abs(angle % 360 - 330);
            post(this);
            d = getMiin(a, b, c);

            if (d == a && angle % 360 - 90 < 0) {
                isplus = true;
            } else if (d == b && angle % 360 - 210 < 0) {
                isplus = true;
            } else if (d == c && angle % 360 - 330 < 0) {
                isplus = true;
            } else {
                isplus = false;
            }
            break;

        default:
            break;
        }
        mPreviousX = x;
        return true;
    }

    /**
     * 方法描述：获取三个数字中的最小值
     * 
     * @author 尤洋
     * @Title: getMax
     * @param a
     * @param b
     * @param c
     * @return
     * @return int
     * @date 2015-3-29 下午2:31:44
     */
    private int getMiin(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    @Override
    public void drawScene(GL10 gl) {
        if (isOntouch) {
            if (isLeft) {
                angle--;
            } else {
                angle++;
            }
        }
        // 测试材质效果
        // drawMateril(gl);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);   //切换至模型观察矩阵
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0);//设置视点和模型中心位置
        gl.glTranslatef(0, 0, -3);
        
      //第一张图片
        gl.glPushMatrix();
        //从（0，0，0）到（x，y，z） 的轴沿着逆时针旋转
        gl.glRotatef(-angle, 0, 1, 0.5f-y/getHeight());
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(-angle, 0, -1, y/getHeight()-0.5f);
        gl.glRotatef(60*(0.5f-y/getHeight()), 1, 0, 0);
//        gl.glRotatef(20, 0, 1, 0);
//        gl.glScalef(.5f, .5f, .5f);
        cube.draw(gl);
        gl.glPopMatrix();
        /*
         * 要理解（先旋转再平移） 和 （先平移再旋转） 的区别 角度递增的情况下 
         * 先旋转再平移的话 其实是以平移的距离为半径 进行旋转 而先平移再旋转 
         * 其实是自转 原因是 永远都按照 旋转之后的坐标系进行平移
         */
        //第二张图片
        gl.glPushMatrix();
        gl.glRotatef(120 - angle, 0, 1, 0.5f-y/getHeight());
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(120 - angle, 0, -1, y/getHeight()-0.5f);
        gl.glRotatef(60*(0.5f-y/getHeight()), 1, 0, 0);
//        gl.glRotatef(20, 0, 1, 0);
//        gl.glScalef(.5f, .5f, .5f);
        cube1.draw(gl);
        gl.glPopMatrix();

        //第三张图片
        gl.glPushMatrix();
        gl.glRotatef(240 - angle, 0, 1, 0.5f-y/getHeight());
        gl.glTranslatef(1, 0, 0);
        gl.glRotatef(240 - angle, 0, -1, y/getHeight()-0.5f);
        gl.glRotatef(60*(0.5f-y/getHeight()), 1, 0, 0);
//        gl.glRotatef(20, 0, 1, 0);
//        gl.glScalef(.5f, .5f, .5f);
        cube2.draw(gl);
        gl.glPopMatrix();
    }

    /**
     * 方法描述：绘制材质
     * 使用之后未显示材质效果，暂时不使用
     * @author 尤洋
     * @Title: drawMateril
     * @return void
     * @date 2015-3-30 上午12:23:30
     */
    private void drawMateril(GL10 gl) {
        float[] mat_amb = { 0.2f * 0.4f, 0.2f * 0.4f,
                0.2f * 1.0f, 1.0f, };
        float[] mat_diff = { 0.4f, 0.4f, 1.0f, 1.0f, };
        float[] mat_spec = { 1.0f, 1.0f, 1.0f, 1.0f, };

        ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
        mabb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
        mat_ambBuf.put(mat_amb);
        mat_ambBuf.position(0);

        ByteBuffer mdbb = ByteBuffer.allocateDirect(mat_diff.length * 4);
        mdbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
        mat_diffBuf.put(mat_diff);
        mat_diffBuf.position(0);

        ByteBuffer msbb = ByteBuffer.allocateDirect(mat_spec.length * 4);
        msbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_specBuf = msbb.asFloatBuffer();
        mat_specBuf.put(mat_spec);
        mat_specBuf.position(0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
                GL10.GL_AMBIENT, mat_ambBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
                GL10.GL_DIFFUSE, mat_diffBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
                GL10.GL_SPECULAR, mat_specBuf);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK,
                GL10.GL_SHININESS, 64.0f);
    }

    @Override
    public void run() {
        for (int i = 0; i < d; i++) {
            if (isplus) {
                angle++;
            } else {
                angle--;
            }
            SystemClock.sleep(10);
            requestRender();
        }
    }

    /**
     * 初始化光源
     */
    @Override
    public void initScene(GL10 gl) {
        float[] mat_amb = { 0.2f * 1.0f, 0.2f * 0.4f, 0.2f * 0.4f, 1.0f, };
        float[] mat_diff = { 1.0f, 0.4f, 0.4f, 1.0f, };
        float[] mat_spec = { 1.0f, 1.0f, 0.5f, 1.0f, };

        ByteBuffer mabb = ByteBuffer.allocateDirect(mat_amb.length * 4);
        mabb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_ambBuf = mabb.asFloatBuffer();
        mat_ambBuf.put(mat_amb);
        mat_ambBuf.position(0);

        ByteBuffer mdbb = ByteBuffer.allocateDirect(mat_diff.length * 4);
        mdbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_diffBuf = mdbb.asFloatBuffer();
        mat_diffBuf.put(mat_diff);
        mat_diffBuf.position(0);

        ByteBuffer msbb = ByteBuffer.allocateDirect(mat_spec.length * 4);
        msbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_specBuf = msbb.asFloatBuffer();
        mat_specBuf.put(mat_spec);
        mat_specBuf.position(0);

        gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, mat_specBuf);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 64.0f);

        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f, 0.0f, 10.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);

    }

    
}
