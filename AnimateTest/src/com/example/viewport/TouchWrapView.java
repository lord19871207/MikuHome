package com.example.viewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.animatetest.R;

/**
 * 通过mesh网格点的坐标修改，触摸点附近会发生扭曲
 * 
 * @Package com.example.viewport
 * @ClassName: TouchWrapView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-24 下午6:04:49
 */
public class TouchWrapView extends View {

    private Paint paint;
    private Bitmap bitmap;
    private int width, height;
    private int bitmapWidth = 30;
    private int bitmapHeight = 10;

    private int touchX;
    private int touchY;

    private int delayOffsetX;

    private Handler handler = new Handler();
    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            delayOffsetX += (touchX - delayOffsetX) * 0.5F;
            handler.postDelayed(this, 20);
            invalidate();
        }
    };

    public TouchWrapView(Context context) {
        super(context);
    }

    public TouchWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview();
    }

    private void initview() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash_load);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        handler.post(delayRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float[] verts = new float[(bitmapWidth + 1) * (bitmapHeight + 1) * 2];
        int[] colors = new int[(bitmapWidth + 1) * (bitmapHeight + 1)];
        int index = 0;
        for (int y = 0; y <= bitmapHeight; y++) {
            for (int x = 0; x <= bitmapWidth; x++) {
                if (Math.abs(touchX - width / bitmapWidth * x) < 60
                        && Math.abs(touchY - height / bitmapHeight * y) < 60) {
                    verts[index * 2 + 0] = touchX;
                    verts[index * 2 + 1] = touchY;
                } else {
                    verts[index * 2 + 0] = width / bitmapWidth * x;
                    verts[index * 2 + 1] = height / bitmapHeight * y;
                }
                index += 1;
            }
        }
        canvas.drawBitmapMesh(bitmap, bitmapWidth, bitmapHeight, verts, 0, colors, 0, null);
        paint.setColor(Color.BLUE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            touchX = (int) event.getX();
            touchY = (int) event.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            touchX = (int) event.getX();
            touchY = (int) event.getY();
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            break;
        }

        invalidate();

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

    }

}
