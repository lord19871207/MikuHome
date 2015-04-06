package com.example.viewport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

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
    private VelocityTracker mVelocityTracker;
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

    public ScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        y = event.getY();
        mVelocityTracker.addMovement(event);
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
            
            handleVelocity();
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

    private Scroller mScroller;
    /** 控制是否开启加速度逻辑 */
    private boolean isComputeScroll = false;

    /**
     * 
     * 处理上下翻页加速度
     * 
     * @Title: handleVelocity
     * @return void
     * @date 2015-3-18 下午4:02:45
     */
    public void handleVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocityY = (int) mVelocityTracker.getYVelocity();
        // Log4an.e("yyy", "velocityY=" + Math.abs(velocityY));
        if (Math.abs(velocityY) > 500) {
            isComputeScroll = true;
            // startScollAnimation(0, false);
            mScroller.fling(0, (int) mLastTouchY, 0,
                    (int) mVelocityTracker.getYVelocity(), Integer.MIN_VALUE,
                    Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            isComputeScroll = false;
        }
        // }

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (isComputeScroll) {
                dy = mScroller.getCurrY() % scrollViewH - mLastTouchY;
                mLastTouchY = mScroller.getCurrY() % scrollViewH;
                postInvalidate();
            }
        }
    }
    
    
    
    
    /**
     * 
     * 此处写类描述：控制bitmap左右和上下滚动动画的线程
     * 
     * @Package com.shuqi.activity.viewport
     * @ClassName: FlingRunnable
     * @author author youyang
     * @mail youyang@ucweb.com
     * @date 2015-3-25 上午11:52:54
     */
    private class FlingRunnable implements Runnable {

        /** 动画持续的时间 */
        private static final int SCROLL_ANIMATION_DURATION = 500;// 500
        private static final int TOUCH_ANIMATION_DURATION = 200;
        private static final int MIN_ANIMATION_DURATION = 200;

//        private Scroller mScroller;
        private int mLastFlingX;
        private int mLastFlingY;

        public FlingRunnable() {
            // 初始化scroller对象
//            mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        }

        /**
         * 移除消息队列里的上个动作
         */
        private void removeLastOperation() {
            removeCallbacks(this);
        }

        /**
         * 另外开启一个线程来横向滑动书页
         * 
         * @param distance 滑动的距离
         */
        public void startByScroll(int distance) {
            startUsingDistance(distance, SCROLL_ANIMATION_DURATION);
        }

        public void startByTouch(int distance) {
            startUsingDistance(distance, TOUCH_ANIMATION_DURATION);
        }

        /**
         * 异步书页偏移的 实际执行方法
         * 
         * @param distance 偏移距离
         * @param during 持续时间
         */
        private void startUsingDistance(int distance, int during) {
            if (distance == 0)
                return;
            // 在操作之前 先移除上一次的线程操作
            removeLastOperation();
            mLastFlingY = 0;
            // 起始点为（0，0），x偏移量为 -distance ，y的偏移量为 0，持续时间
            // Log4an.d("ysd", "mLastTouchY=" + ((int) mLastTouchY) + ",distance=" + (-distance)
            // + ",时间=" + Math.abs(distance) * during / mHeight);
            mScroller.startScroll(0, 0, 0, -distance,
                    Math.max(MIN_ANIMATION_DURATION, Math.abs(distance) * during / scrollViewH));
            post(this);
        }

        /**
         * 停止滑动
         * 
         * @param scrollIntoSlots
         */
        private void endFling(boolean scrollIntoSlots) {
            mScroller.forceFinished(true);
        }

        @Override
        public void run() {
                boolean more = mScroller.computeScrollOffset();// 返回true的话 则动画还没有结束
                final int y = mScroller.getCurrY();// 返回滚动时 当前的Y坐标
                // Log4an.e("draw", "y的值:"+y);
                int delta = mLastFlingY - y;
                if (delta != 0) {
                    mLastTouchY += delta;
                    // Log4an.e("draw", "mLastTouchY:"+mLastTouchY);
                    // Log4an.i("draw", "more:" + more + ",delta渐变距离：" + delta);
                    postInvalidate();
                }
                if (more) {
                    mLastFlingY = y;
                    // Log4an.e("draw", "mLastTouchY:"+mLastTouchY);
                    post(this);
                } else {
                    endFling(true);
                    // Log4an.e("draw", "mLastTouchY:"+mLastTouchY);
                    // Log4an.d("draw", "...................上下翻页结束............");
                }

        }

        public boolean isFinished() {
            return mScroller.isFinished();
        }
    }

}
