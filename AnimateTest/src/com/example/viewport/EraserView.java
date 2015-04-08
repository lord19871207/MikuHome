package com.example.viewport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.animatetest.R;
import com.example.common.util.Utils;

public class EraserView extends View {
    private static final int MIN_MOVE_DIS = 5;// 最小的移动距离：如果我们手指在屏幕上的移动距离小于此值则不会绘制

    private Bitmap fgBitmap, bgBitmap;// 前景橡皮擦的Bitmap和背景我们底图的Bitmap
    private Canvas mCanvas;// 绘制橡皮擦路径的画布
    private Paint mPaint;// 橡皮檫路径画笔
    private Path mPath;// 橡皮擦绘制路径

    private int screenW, screenH;// 屏幕宽高
    private float preX, preY;// 记录上一个触摸事件的位置坐标

    public EraserView(Context context, AttributeSet set) {
        super(context, set);

        // 计算参数
        cal(context);

        // 初始化对象
        init(context);
    }

    /**
     * 计算参数
     * 
     * @param context
     *            上下文环境引用
     */
    private void cal(Context context) {
        // 获取屏幕尺寸数组
        int[] screenSize = Utils.getScreenSize((Activity) context);

        // 获取屏幕宽高
        screenW = screenSize[0];
        screenH = screenSize[1];
    }

    /**
     * 初始化对象
     */
    private void init(Context context) {
        // 实例化路径对象
        mPath = new Path();

        // 实例化画笔并开启其抗锯齿和抗抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 设置画笔透明度为0是关键！我们要让绘制的路径是透明的，然后让该路径与前景的底色混合“抠”出绘制路径
        mPaint.setARGB(128, 255, 0, 0);

        // 设置混合模式为DST_IN
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        // 设置画笔风格为描边
        mPaint.setStyle(Paint.Style.STROKE);

        // 设置路径结合处样式
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        // 设置笔触类型
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 设置描边宽度
        mPaint.setStrokeWidth(50);

        // 生成前景图Bitmap
        fgBitmap = Bitmap.createBitmap(screenW, screenH, Config.ARGB_8888);

        // 将其注入画布
        mCanvas = new Canvas(fgBitmap);

        // 绘制画布背景为中性灰
        mCanvas.drawColor(0xFF808080);

        // 获取背景底图Bitmap
        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ccc);

        // 缩放背景底图Bitmap至屏幕大小
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenW, screenH, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景
        canvas.drawBitmap(bgBitmap, 0, 0, null);

        // 绘制前景
        canvas.drawBitmap(fgBitmap, 0, 0, null);

        /*
         * 这里要注意canvas和mCanvas是两个不同的画布对象
         * 当我们在屏幕上移动手指绘制路径时会把路径通过mCanvas绘制到fgBitmap上
         * 每当我们手指移动一次均会将路径mPath作为目标图像绘制到mCanvas上，而在上面我们先在mCanvas上绘制了中性灰色
         * 两者会因为DST_IN模式的计算只显示中性灰，但是因为mPath的透明，计算生成的混合图像也会是透明的
         * 所以我们会得到“橡皮擦”的效果
         */
        mCanvas.drawPath(mPath, mPaint);
    }

    /**
     * View的事件将会在7/12详解
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * 获取当前事件位置坐标
         */
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:// 手指接触屏幕重置路径
            mPath.reset();
            mPath.moveTo(x, y);
            preX = x;
            preY = y;
            break;
        case MotionEvent.ACTION_MOVE:// 手指移动时连接路径
            float dx = Math.abs(x - preX);
            float dy = Math.abs(y - preY);
            if (dx >= MIN_MOVE_DIS || dy >= MIN_MOVE_DIS) {
                mPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                preX = x;
                preY = y;
            }
            break;
        }

        // 重绘视图
        invalidate();
        return true;
    }
}
