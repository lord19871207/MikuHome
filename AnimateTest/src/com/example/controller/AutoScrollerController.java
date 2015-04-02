package com.example.controller;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
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
    }
    
    
    public void startAutoScroll() {
        autoScrollTimer = new Timer();
        if (autoScrollRunnable == null) {
            autoScrollRunnable = new Runnable() {
                @Override
                public void run() {
                    autoScrollOffset++;
                    if (autoScrollOffset > height) {
                        autoScrollOffset = 0;
                        preparePicture(6, 0);
                    }
                    preparePicture(0, 0);
                }
            };
        }
        if (cachePicture == null) {
            cachePicture = new Picture();
        }
        preparePicture(0, 0);
        preparePicture(6, 0);
        autoScrollOffset = 0;
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
    
    
    
    public void preparePicture(int which, int scrollDirection) {

        Picture picture = null;
        switch (which) {
        case 0:// 默认画面内容
            picture = mView.getDefaultPagePicture();
            picture.endRecording();
            Canvas canvas = picture.beginRecording(width, height);
            // 当自动滚屏时
                canvas.save();
                canvas.clipRect(0, 0, width, autoScrollOffset);
                
                canvas.drawBitmap(bitmap_0, 0, 0, paint);
                
                
                
            // 当前页有本章节内容时，画信息栏
            // 当自动滚屏时
                canvas.restore();
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
                canvas.drawPicture(cachePicture);
                canvas.restore();
                mView.postInvalidate();
            break;
        case 6:// 画自动滚屏的缓存picture
            if (cachePicture == null) {
                cachePicture = new Picture();
            }
            cachePicture.endRecording();
            Canvas canvas2 = cachePicture.beginRecording(width, height);
            mView.getDefaultPagePicture().draw(canvas2);
            break;
        }
    }

}
