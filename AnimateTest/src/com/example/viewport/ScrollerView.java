package com.example.viewport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.animatetest.R;
import com.example.common.util.Utils;

/**
 * 类描述：上下滑动翻页
 * 
 * @Package com.example.viewport
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-3 下午3:18:10
 */
@SuppressLint("NewApi")
public class ScrollerView extends View {

    public ScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    int scrollViewH;
    /** 上下翻页中绘制区域的 边界值，防止绘制过程中频繁计算 */
    private int broad1;
    private int broad2;
    private int broad3;
    private int broad4;
    private int broad5;
    private int broad6;
    private int broad7;
    private int broad8;

    /**
     * 逻辑层所需的类型值 scroll_return_type[0] 显示在用户眼前的两张bitmap的第一张的标号 scroll_return_type[1] 显示在用户眼前的两张bitmap的第二张的标号
     * scroll_return_type[2] 返回是否需要加载数据，3 需要， 4 不需要
     * */
    private int[] scroll_return_type = { 0, 1, 4 };
    private int broad9;
    private int broad10;

    // private ScrollerController controller;
    /** touch事件down时的x轴坐标 */
    private float mTouchDownX;
    /** touch事件down时的y轴坐标 */
    private float mTouchDownY;
    /** 一次完整touch事件中上一次的x轴坐标 */
    private float mLastTouchX;
    /** 一次完整touch事件中上一次的y轴坐标 */
    private float mLastTouchY;

    private float moveX;
    private float moveY;
    /** 加速度计算工具 */
    // private VelocityTracker mVelocityTracker;
    private float preY;
    private float y;
    private float dy;

    private Bitmap bitmap; // old
    private Bitmap bitmap1; // new
    private Bitmap bitmap2;// 整页图片作为分割线 cache
    private float drawY;
    private float tempY;
    private int width;

    public ScrollerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public ScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ScrollerView(Context context) {
        super(context);
        init(context);
    }
    
    
    

    public void init(Context context) {
        scrollViewH = getViewHeight(context);
        width = getViewWidth(context);
        bitmap = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu1, 512, 720);
        bitmap1 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.bu2, 512, 720);
        bitmap2 = Utils.decodeSampledBitmapFromResource
                (getResources(), R.drawable.cat, 512, 720);

        intDrawBoard();
        // controller=new ScrollerController();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        y = event.getY();
        // mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchDownX = event.getX();
            mTouchDownY = event.getY();
            abortAnimation();
            break;
        case MotionEvent.ACTION_MOVE:
            dy = y - preY;
            moveX = event.getX();
            moveY = event.getY();
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            mLastTouchX = event.getX();
            mLastTouchY = event.getY();
            break;
        default:
            break;
        }
        preY = y;
        postInvalidate();
        return true;
    }

    /** 终止翻页特效 */
    private void abortAnimation() {
        // if (!mModeScroller.isFinished()) {
        // // 停止动画 上下翻页时在这里停止加速度
        // mFlingRunnable.endFling(true);
        // mModeScroller.abortAnimation();
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        drawScrollContentCanvas_new(canvas);
    }

    private void drawScrollContentCanvas_new(Canvas canvas) {
//        tempY = mLastTouchY;
        // 手指滑动过程的每一刻偏移量
        tempY += dy;
        // 控制缓存偏移量的范围在0到3倍显示区域高度之间
        tempY = (tempY % (3 * scrollViewH));
        drawY = tempY;
        controllBitmaoDisplay(canvas);
        // controlReturnType();
    }

    private void intDrawBoard() {
        broad1 = -3 * scrollViewH / 2;
        broad2 = -scrollViewH / 2;
        broad3 = -5 * scrollViewH / 2;
        broad4 = -3 * scrollViewH;
        broad5 = scrollViewH / 2;
        broad6 = 3 * scrollViewH / 2;
        broad7 = 5 * scrollViewH / 2;
        broad8 = 3 * scrollViewH;
        broad9 = 2 * scrollViewH;
        broad10 = -2 * scrollViewH;
    }

    private void controllBitmaoDisplay(Canvas canvas) {
        Log.i("youyang", "==========");
        /* 控制显示左上角标题 */
        if (tempY >= broad1 && tempY <= broad2) {
            // 1 2 3 (-1.5,-0.5)
            Log.i("youyang", "==========1");
            canvas.drawBitmap(bitmap, 0, drawY, null);
            canvas.drawBitmap(bitmap1, 0, drawY + scrollViewH, null);
            canvas.drawBitmap(bitmap2, 0, drawY + scrollViewH * 2, null);
        } else if (tempY >= (broad3) && tempY < (broad1)) {
            // 2 3 1 (-2.5--1.5)
            Log.i("youyang", "==========2");
            canvas.drawBitmap(bitmap1, 0, drawY + scrollViewH, null);
            canvas.drawBitmap(bitmap2, 0, drawY + 2 * scrollViewH, null);
            canvas.drawBitmap(bitmap, 0, drawY + scrollViewH * 3, null);
        } else if (tempY >= (broad4) && tempY < (broad3)) {
            // 3 1 2 (-3,-2.5)
            Log.i("youyang", "==========3");
            canvas.drawBitmap(bitmap2, 0, drawY + 2 * scrollViewH, null);
            canvas.drawBitmap(bitmap, 0, drawY + 3 * scrollViewH, null);
            canvas.drawBitmap(bitmap1, 0, drawY + scrollViewH * 4, null);

        } else if (tempY > (broad2) || tempY < (broad5)) {
            // 3 1 2 (-0.5-0.5)
            canvas.drawBitmap(bitmap2, 0, drawY - scrollViewH, null);
            canvas.drawBitmap(bitmap, 0, drawY, null);
            canvas.drawBitmap(bitmap1, 0, drawY + scrollViewH, null);
        } else if (tempY >= (broad5) && tempY < (broad6)) {
            // 2 3 1 (0.5-1.5)
            canvas.drawBitmap(bitmap1, 0, drawY - 2 * scrollViewH, null);
            canvas.drawBitmap(bitmap2, 0, drawY - scrollViewH, null);
            canvas.drawBitmap(bitmap, 0, drawY, null);
        } else if (tempY >= (broad6) && tempY < (broad7)) {
            // 1 ,2 ,3 (1.5-2.5)
            canvas.drawBitmap(bitmap, 0, drawY - 3 * scrollViewH, null);
            canvas.drawBitmap(bitmap1, 0, drawY - 2 * scrollViewH, null);
            canvas.drawBitmap(bitmap2, 0, drawY - scrollViewH, null);
        } else if (tempY >= broad7 && tempY < broad8) {
            // 3 1 2 (2.5-3)
            canvas.drawBitmap(bitmap2, 0, 2 * scrollViewH - drawY, null);
            canvas.drawBitmap(bitmap, 0, 3 * scrollViewH - drawY, null);
            canvas.drawBitmap(bitmap1, 0, 4 * scrollViewH - drawY, null);
        }
    }

    /** 获取自定义View的高度 */
    private int getViewHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    private int getViewWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
