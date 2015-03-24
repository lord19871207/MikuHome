package com.example.viewport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathEffect;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

import com.example.animatetest.R;

    /**
     * 放大镜实现方式2
     * @author chroya
     *
     */
    public class Magnifier extends View{
        private Path mPath = new Path();
        private Matrix matrix = new Matrix();
        private Bitmap bitmap;
        private Paint paint=new Paint();
        //放大镜的半径
        private static final int RADIUS = 120;
        //放大倍数
        private static final int FACTOR = 2;
        private int mCurrentX, mCurrentY;

        public Magnifier(Context context) {
            super(context);
            mPath.addCircle(RADIUS, RADIUS, RADIUS, Direction.CCW);
            matrix.setScale(FACTOR, FACTOR);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);
            paint.setPathEffect(new PathEffect());
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash_load);
        }   
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mCurrentX = (int) event.getX();
            mCurrentY = (int) event.getY();
            invalidate();
            return true;
        }
        
        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            //底图
            canvas.drawBitmap(bitmap, 0, 0, null);
            //剪切
            canvas.translate(mCurrentX - RADIUS, mCurrentY - RADIUS);
            canvas.clipPath(mPath, Region.Op.REPLACE); 
            //画放大后的图
            canvas.translate(RADIUS-mCurrentX*FACTOR, RADIUS-mCurrentY*FACTOR);
            canvas.drawBitmap(bitmap, matrix, paint);  
            canvas.restore();
            invalidate();
        }
    }
