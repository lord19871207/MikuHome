package com.example.viewport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.animatetest.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * 类描述：可拖动的布局，主要用于熟悉 ViewDragHelper类
 * 
 * 2013年谷歌i/o大会上介绍了两个新的layout： SlidingPaneLayout 和DrawerLayout，
 *  现在这俩个类被广泛的运用，其实研究他们的源码你会 发现这两个类都运用了ViewDragHelper来处理拖动。
 * ViewDragHelper是framework中不为人知却非常有用的一个工具。
 * 
 * 其实ViewDragHelper并不是第一个用于分析手势处理的类，gesturedetector也是， 
 * 但是在和拖动相关的手势分析方面gesturedetector只能说是勉为其难。关于ViewDragHelper有如下几点：
 * ViewDragHelper.Callback是连接ViewDragHelper与view之间的桥梁（ 这个view一
 * 般是指拥子view的容器即parentView）；ViewDragHelper的实例是通过静态工厂方法创建的；
 * 你能够指定拖动的方向；ViewDragHelper可以检测到是否触及到边缘； ViewDragHelper并
 * 不是直接作用于要被拖动的View，而是使其控制的视图容器中的
 * 子View可以被拖动，如果要指定某个子view的行为，需要在Callback中想办法； ViewDragHelper
 * 的本质其实是分析onInterceptTouchEvent和onTouchEvent
 * 的MotionEvent参数，然后根据分析的结果去改变一个容器中被拖动子View的位置（
 *  通过offsetTopAndBottom(int offset)和offsetLeftAndRight(int offset) 方法
 * ），他能在触摸的时候判断当前拖动的是哪个子View； 虽然ViewDragHelper的实例方法 
 * ViewDragHelper create(ViewGroup forParent, Callback cb)
 * 可以指定一个被ViewDragHelper处理拖动事件的对象 ，但ViewDragHelper类的设计决定了
 * 其适用于被包含在一个自定义 ViewGroup之中，而不是对任意一个布局上的视图容器使用ViewDragHelper。
 * 
 * @Package com.example.viewport
 * @ClassName: DragInnerLayout
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-30 下午8:10:02
 */
@SuppressLint("NewApi")
public class DragInnerLayout extends LinearLayout {
    private final ViewDragHelper dragHelper;
    private View mDragView1;
    private View mDragView2;

    private boolean mDragEdge;
    private boolean mDragHorizontal;
    private boolean mDragCapture;
    private boolean mDragVertical;

    public DragInnerLayout(Context context) {
        this(context, null);
    }

    public DragInnerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragInnerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        dragHelper = ViewDragHelper.create(this, new DragHelpCallback());
    }

    @Override
    protected void onFinishInflate() {
        mDragView1 = findViewById(R.id.drag1);
        mDragView2 = findViewById(R.id.drag2);
    }

    public void setDragHorizontal(boolean dragHorizontal) {
        mDragHorizontal = dragHorizontal;
        mDragView2.setVisibility(View.GONE);
    }

    public void setDragVertical(boolean dragVertical) {
        mDragVertical = dragVertical;
        mDragView2.setVisibility(View.GONE);
    }

    public void setDragEdge(boolean dragEdge) {
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mDragEdge = dragEdge;
        mDragView2.setVisibility(View.GONE);
    }

    public void setDragCapture(boolean dragCapture) {
        mDragCapture = dragCapture;
    }

    private class DragHelpCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View arg0, int arg1) {
            if (mDragCapture) {
                return arg0 == mDragView1;
            }
            return true;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            invalidate();
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if (mDragEdge) {
                dragHelper.captureChildView(mDragView1, pointerId);
            }
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mDragHorizontal || mDragCapture || mDragEdge) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mDragView1.getWidth();
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mDragVertical) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mDragView1.getHeight();
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }
            return super.clampViewPositionVertical(child, top, dy);
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel();
            return false;
        }
        return dragHelper.shouldInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        animateView(mDragView1,event.getX()/getWidth());
        return true;
    }
    
    private void animateView(View view,float percent) {
        float f1 = 1 - percent * 0.5f;
        ViewHelper.setScaleX(view, f1);
        ViewHelper.setScaleY(view, f1);
    }
}
