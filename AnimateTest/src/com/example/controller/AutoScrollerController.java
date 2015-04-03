package com.example.controller;

import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.interfaces.BookSimulationPageFlip;
import com.example.viewport.AutoScrollerView;

/**
 * 类描述：
 * @Package com.example.controller
 * @ClassName: AutoScrollerController
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-2 下午11:07:29
 */
public class AutoScrollerController {
    private Bitmap bitmap_0;  // old
    private Bitmap bitmap_1;  // new
    public static float density;
    public AutoScrollerView mView;
    public static final int LEFTSPACE = 10;
    private static final int RIGHTSPACE = 10;
    private int autoScrollDrawableHeight = 6;
    private BookSimulationPageFlip impl;
    public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public AutoScrollerController(Context context2,AutoScrollerView view) {
        this.mView=view;
        this.context=context2;
        this.paint.setAntiAlias(true);
        width=context2.getResources().getDisplayMetrics().widthPixels;
        height=context2.getResources().getDisplayMetrics().heightPixels;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context2).getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
    }
    /** 缓存picture */
    private Picture cachePicture;
    private Context context;
    private Runnable autoScrollRunnable;
    private int autoScrollOffset=0;
    private int width;
    private int height;
    Timer autoScrollTimer;
    private TimerTask autoScrollTask;
    /** 开始滚屏 */
    private GradientDrawable autoScrollDrawable;
    
    public void setNeedImpl(BookSimulationPageFlip impl) {
        this.impl = impl;
        bitmap_0 = impl.loadBitmap(0);
        bitmap_1 = impl.loadBitmap(1);
        paint.setColor(Color.BLUE);
        
    }
    
    private int index=0;
    public void startAutoScroll() {
        autoScrollTimer = new Timer();
        if (autoScrollRunnable == null) {
            autoScrollRunnable = new Runnable() {

                @Override
                public void run() {
                    autoScrollOffset=autoScrollOffset+4;
                    if (autoScrollOffset > height) {
                        index++;
                        autoScrollOffset = 0;
//                        preparePicture(6, 0);
                    }
                    
//                    preparePicture(0, 0);
                }
            };
        }
//        if (cachePicture == null) {
//            cachePicture = new Picture();
//        }
//        preparePicture(0, 0);
//        preparePicture(6, 0);
//        autoScrollOffset = 0;
        autoScrollTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity)context).runOnUiThread(autoScrollRunnable);
            }
        };
        autoScrollTimer.schedule(autoScrollTask, 50,50);
    }

    /**
     * 停止滚屏 isNormalStop = true :正常停止 isNormalStop = false:滚屏到结尾停止
     */
    private void stopAutoScroll(boolean isNormalStop) {
            autoScrollTask.cancel();
            autoScrollTimer.cancel();
            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    public void draw1(Canvas canvas){
        canvas.save();
        canvas.clipRect(0, 0, width, autoScrollOffset);
        if(index%2==0){
            canvas.drawBitmap(bitmap_0, 0, 0, paint);
        }else{
            canvas.drawBitmap(bitmap_1, 0, 0, paint);
        }
        
        canvas.restore();
    }
    
    public void draw2(Canvas canvas){
        canvas.save();
        canvas.clipRect(0, autoScrollOffset, width, height);
        if (autoScrollDrawable == null) {
            int[] colors = new int[] { 0x80111111, 0x111111 };
            autoScrollDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, colors);
        }
        autoScrollDrawable.setBounds(
                LEFTSPACE - Math.round(2 * density),
                autoScrollOffset,
                width - RIGHTSPACE
                        + Math.round(2 * density), autoScrollOffset
                        + autoScrollDrawableHeight);
        autoScrollDrawable.draw(canvas);
        canvas.drawLine(
                LEFTSPACE - Math.round(2 * density),
                autoScrollOffset,
                width - RIGHTSPACE
                        + Math.round(2 * density),
                autoScrollOffset, paint);
        if(index%2==1){
            canvas.drawBitmap(bitmap_0, 0, 0, paint);
        }else{
            canvas.drawBitmap(bitmap_1, 0, 0, paint);
        }
        canvas.restore();
        mView.postInvalidate();
    }
    
    
    public void draw3(Canvas canvas){
        int top,bottom;
        if(autoScrollOffset>2){
            top=autoScrollOffset-3;
        }else{
            top=1;
        }
        
        if(autoScrollOffset<height-2){
            bottom=autoScrollOffset+2;
        }else{
            bottom=autoScrollOffset-1;
        }
        canvas.drawRect(0, top, width, bottom, paint);
    }
    
    
    public void preparePicture(int which, int scrollDirection) {

        Picture picture = null;
        switch (which) {
        case 0:// 默认画面内容
            picture = mView.getDefaultPagePicture();
            picture.endRecording();
            Canvas canvas = picture.beginRecording(width, height);
            // 当自动滚屏时 上半部分
                canvas.save();
                canvas.clipRect(0, 0, width, autoScrollOffset);
                if(autoScrollOffset%2==0){
                    canvas.drawBitmap(bitmap_0, 0, 0, paint);
                }else{
                    canvas.drawBitmap(bitmap_1, 0, 0, paint);
                }
                
                canvas.restore();
                
              //下半部分  
                canvas.save();
                canvas.clipRect(0, autoScrollOffset, width, height);
                if (autoScrollDrawable == null) {
                    int[] colors = new int[] { 0x80111111, 0x111111 };
                    autoScrollDrawable = new GradientDrawable(
                            GradientDrawable.Orientation.TOP_BOTTOM, colors);
                }
                autoScrollDrawable.setBounds(
                        LEFTSPACE - Math.round(2 * density),
                        autoScrollOffset,
                        width - RIGHTSPACE
                                + Math.round(2 * density), autoScrollOffset
                                + autoScrollDrawableHeight);
                autoScrollDrawable.draw(canvas);
                canvas.drawLine(
                        LEFTSPACE - Math.round(2 * density),
                        autoScrollOffset,
                        width - RIGHTSPACE
                                + Math.round(2 * density),
                        autoScrollOffset, paint);
                if (cachePicture != null) {
                    canvas.drawPicture(cachePicture);
                    Log.i("youyang", "canvas.drawPicture(cachePicture);");
                }
                
                canvas.restore();
                mView.postInvalidate();
            break;
        case 6:// 画自动滚屏的缓存picture
            if (cachePicture == null) {
                cachePicture = new Picture();
            }
            cachePicture.endRecording();
            Log.i("youyang", "画自动滚屏的缓存picture");
            Canvas canvas2 = cachePicture.beginRecording(width, height);
            mView.getDefaultPagePicture().draw(canvas2);
            break;
        }
    }

}
