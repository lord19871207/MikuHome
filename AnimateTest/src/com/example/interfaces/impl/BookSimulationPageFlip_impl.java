package com.example.interfaces.impl;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.example.interfaces.BookSimulationPageFlip;

/**
 * 类描述：仿真翻页的接口类
 * 
 * @Package com.example.interfaces
 * @ClassName: BookSimulationPageFlip
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 上午10:11:17
 */
public class BookSimulationPageFlip_impl {
    private static final int DIRECTION_RIGHT = 1;//从右往左滑动

    private static final int DIRECTION_LEFT = 0;//从左往右滑动

    /** 需要仿真翻页效果的控件 */
    private View view;

    private Bitmap bitmap_0;  // old
    private Bitmap bitmap_1;  // new
    private Context context;

//    private FlingRunnable mFlingRunnable = new FlingRunnable();
    /** 显示区域宽度 */
    public int mWidth;
    /** 显示区域高度 */
    public int mHeight;
    // 屏幕宽
    public int mScreenWidth;
    // 屏幕高
    public int mScreenHeight;

    /** 为防止Y轴坐标>=View高度，而让其等于View的高度-dI */
    final private float dI = 0.01f;


    private int[] mCurBitmapShadowColors;

    private GradientDrawable mCurBitmapShadowDrawableRL;

    
    
    
    /** 画笔 */
    private Paint paint;
    /** 拖拽点对应的页脚 */
    private int mCornerX;
    private int mCornerY;

    /** 当前页翻起来的部分与下一页显示部分构成的区域path */
    private Path mPath0;
    /** 下一页显示部分构成的区域path */
    private Path mPath1;

    /** 拖拽点 */
    private PointF mTouch;
    /** 贝塞尔曲线起始点 */
    private PointF mBezierStart1 = new PointF();
    /** 贝塞尔曲线控制点 */
    private PointF mBezierControl1 = new PointF();
    /** 贝塞尔曲线顶点 */
    private PointF mBeziervertex1 = new PointF();
    /** 贝塞尔曲线结束点 */
    private PointF mBezierEnd1 = new PointF();

    /** 另一条贝塞尔曲线 */
    private PointF mBezierStart2 = new PointF();
    /** 另一条贝塞尔曲线顶点 */
    private PointF mBezierControl2 = new PointF();
    /** 另一条贝塞尔曲线控制点 */
    private PointF mBeziervertex2 = new PointF();
    /** 另一条贝塞尔曲线结束点 */
    private PointF mBezierEnd2 = new PointF();

    /** 拖拽点与页脚的中点 */
    private float mMiddleX;
    private float mMiddleY;
    /** 旋转角度 */
    private float mDegrees;
    /** 拖拽点与页脚的距离 */
    private float mTouchToCornerDis;

    /** 转换颜色的处理类 */
    private ColorMatrixColorFilter mColorMatrixFilter;
    private Matrix mMatrix;
    private float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };
    private boolean mIsRTandLB; // 是否属于右上左下
    private float mMaxLength = 0;

    /** 颜色矩阵 */
    private ColorMatrix mColorMatr = null;

    /** 相关阴影 */
    private GradientDrawable mBackShadowDrawableLR;
    private GradientDrawable mBackShadowDrawableRL;
    private GradientDrawable mFolderShadowDrawableLR;
    private GradientDrawable mFolderShadowDrawableRL;

    private GradientDrawable mFrontShadowDrawableHBT;
    private GradientDrawable mFrontShadowDrawableHTB;
    private GradientDrawable mFrontShadowDrawableVLR;
    private GradientDrawable mFrontShadowDrawableVRL;

    private BookSimulationPageFlip impl;

    // ----------------仿真翻页属性------------------------

    public BookSimulationPageFlip_impl(Context context) {
        super();
        this.context = context;
        mScreenWidth = getScreenWidth(context);
        mScreenHeight = getScreenHeight(context);
        mWidth=mScreenWidth;
        mHeight=mScreenHeight;
        paint=new Paint();
        paint.setColor(Color.BLUE);

    }
    
    public void setNeedImpl(BookSimulationPageFlip impl){
        this.impl=impl;
        mTouch = impl.getTouchPoint();
        bitmap_0 = impl.loadBitmap(0);
        bitmap_1 = impl.loadBitmap(1);
        initCurBitmapShadow(impl.getShadowColor());
        initSpecEffObjects();
        initSpecEffShadowObjects(impl.getCurrentSimulationTheme());
    }

    

    /**
     * 初始化特效相关对象
     */
    private void initSpecEffObjects() {
        // 初始化区域path
        mPath0 = new Path();
        mPath1 = new Path();
        // 构造颜色矩阵并设置颜色数组
        mColorMatr = new ColorMatrix();
        float array[] =
        { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0, 0.55f, 0, 80.0f, 0, 0, 0,
                0.15f, 0 };
        mColorMatr.set(array);
        // 构造转换颜色的处理类
        mColorMatrixFilter = new ColorMatrixColorFilter(mColorMatr);

        float array2[] =
        { 0.65f, 0, 0, 0, 90.0f, 0, 0.65f, 0, 0, 90.0f, 0, 0, 0.65f, 0, 90.0f, 0, 0, 0,
                0.25f, 0 };
        mColorMatr.set(array2);

        // 构造位置矩阵
        mMatrix = new Matrix();
    }

    /**
     * 初始化特效阴影对象
     */
    private void initSpecEffShadowObjects(int currentTheme) {
        int[] color = null;
        int[] mBackShadowColors = null;
        int[] mFrontShadowColors = null;
        // 夜间或最后一个颜色较深的主题(如果以后增加或减少主题模式此处需要修改)
        if (currentTheme == 3 || currentTheme == 4 || currentTheme == 7 || currentTheme == 8) {
            color = new int[] { 0x1e1e1e, 0xb01e1e1e };// 纵向卷轴阴影颜色数组
            mBackShadowColors = new int[] { 0xff242424, 0x242424 };// 卷角背面的阴影颜色数组
            mFrontShadowColors = new int[] { 0x7a1e1e1e, 0x1e1e1e }; // 卷角前面的阴影颜色数组
        } else {
            color = new int[] { 0x6e6e6e, 0xb06e6e6e };// 纵向卷轴阴影颜色数组
            mBackShadowColors = new int[] { 0xff6e6e6e, 0x6e6e6e };// 卷角背面的阴影颜色数组
            mFrontShadowColors = new int[] { 0x7a6e6e6e, 0x6e6e6e }; // 卷角前面的阴影颜色数组
        }

        // 创建阴影所需的GradientDrawable
        mFolderShadowDrawableRL =
                new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color);
        mFolderShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFolderShadowDrawableLR =
                new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
        mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowDrawableRL =
                new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
        mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mBackShadowDrawableLR =
                new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableVLR =
                new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
        mFrontShadowDrawableVLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFrontShadowDrawableVRL =
                new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
        mFrontShadowDrawableVRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFrontShadowDrawableHTB =
                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
        mFrontShadowDrawableHTB.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFrontShadowDrawableHBT =
                new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
        mFrontShadowDrawableHBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    /**
     * 计算拖拽点对应的拖拽脚
     */
    private void calcCornerXY(float y) {
        mCornerX = mWidth;
        if (y <= mHeight / 2f) {
            mCornerY = 0;
            mIsRTandLB = true;
        } else {
            mCornerY = mHeight;
            mIsRTandLB = false;
        }
    }

    /**
     * 求解直线P1P2和直线P3P4的交点坐标
     */
    private PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
        PointF CrossP = new PointF();
        // 二元函数通式： y=ax+b
        float a1 = (P2.y - P1.y) / (P2.x - P1.x);
        float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);
        float a2 = (P4.y - P3.y) / (P4.x - P3.x);
        float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
        CrossP.x = (b2 - b1) / (a1 - a2);
        CrossP.y = a1 * CrossP.x + b1;
        return CrossP;
    }

    /**
     * 计算贝塞尔曲线各点坐标
     */
    private void mCalculatePoints() {
        // 就算拖拽点与页脚的中点
        mMiddleX = (mTouch.x + mCornerX) / 2;
        mMiddleY = (mTouch.y + mCornerY) / 2;

        // 计算贝塞尔曲线控制点
        mBezierControl1.x =
                mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
        mBezierControl1.y = mCornerY;
        mBezierControl2.x = mCornerX;
        mBezierControl2.y =
                mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);
        // 计算贝塞尔曲线起点
        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
        mBezierStart1.y = mCornerY;

        // 当mBezierStart1.x < 0或者mBezierStart1.x > mWidth时
        // 处理如果继续翻页的bug
        if (mTouch.x > 0 && mTouch.x < mWidth) {
            if (mBezierStart1.x < 0 || mBezierStart1.x > mWidth) {
                if (mBezierStart1.x < 0) {
                    mBezierStart1.x = mWidth - mBezierStart1.x;
                }
                float f1 = Math.abs(mCornerX - mTouch.x);
                float f2 = mWidth * f1 / mBezierStart1.x;
                mTouch.x = Math.abs(mCornerX - f2);
                float f3 = Math.abs(mCornerX - mTouch.x) * Math.abs(mCornerY - mTouch.y) / f1;
                mTouch.y = Math.abs(mCornerY - f3);
                mMiddleX = (mTouch.x + mCornerX) / 2;
                mMiddleY = (mTouch.y + mCornerY) / 2;
                mBezierControl1.x =
                        mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY)
                                / (mCornerX - mMiddleX);
                mBezierControl1.y = mCornerY;
                mBezierControl2.x = mCornerX;
                mBezierControl2.y =
                        mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX)
                                / (mCornerY - mMiddleY);
                mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
            }
        }
        mBezierStart2.x = mCornerX;
        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y) / 2;

        // 拖拽点与页脚的距离
        mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX), (mTouch.y - mCornerY));

        // 计算贝塞尔曲线结束点
        mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1, mBezierStart2);
        mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1, mBezierStart2);

        if (0 == Float.compare(Float.NaN, mBezierEnd1.x))
            mBezierEnd1.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierEnd1.y))
            mBezierEnd1.y = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierEnd2.x))
            mBezierEnd2.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierEnd2.y))
            mBezierEnd2.y = Float.MAX_VALUE;

        /*
         * mBeziervertex1.x 推导 ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于 (mBezierStart1.x+
         * 2*mBezierControl1.x+mBezierEnd1.x) / 4
         * 
         * @计算贝塞尔曲线顶点
         */
        mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
        mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
        mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
        mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;
        if (0 == Float.compare(Float.NaN, mBeziervertex1.x))
            mBeziervertex1.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBeziervertex1.y))
            mBeziervertex1.y = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBeziervertex2.x))
            mBeziervertex2.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBeziervertex2.y))
            mBeziervertex2.y = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mMiddleX))
            mMiddleX = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mMiddleY))
            mMiddleY = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierControl1.x))
            mBezierControl1.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierControl1.y))
            mBezierControl1.y = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierControl2.x))
            mBezierControl2.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierControl2.y))
            mBezierControl2.y = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierStart1.x))
            mBezierStart1.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierStart1.y))
            mBezierStart1.y = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierStart2.x))
            mBezierStart2.x = Float.MAX_VALUE;
        if (0 == Float.compare(Float.NaN, mBezierStart2.y))
            mBezierStart2.y = Float.MAX_VALUE;
    }

    /**
     * 绘制当前页区域
     * 
     * @param canvas
     * @param bitmap
     * @param path
     */
    private void drawCurrentPageArea(Canvas canvas, boolean isPrevPage) {
        if (isPrevPage) {
            if (bitmap_1 == null) {
                return;
            }
        } else {
            if (bitmap_0 == null) {
                return;
            }
        }

        canvas.save();
        // 构造区域path
        mPath0.reset();
        if (mTouch.y == mHeight - dI) {
            mPath0.moveTo(mTouch.x, 0);
            mPath0.lineTo(mWidth, 0);
            mPath0.lineTo(mWidth, mHeight);
            mPath0.lineTo(mTouch.x, mHeight);
        } else {
            mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
            mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y);
            mPath0.lineTo(mTouch.x, mTouch.y);
            mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
            mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y);
            mPath0.lineTo(mCornerX, mCornerY);
        }
        mPath0.close();
        canvas.clipPath(mPath0, Region.Op.XOR);
        if (isPrevPage) {
            canvas.drawBitmap(bitmap_1, 0, 0, null);
        } else {
            canvas.drawBitmap(bitmap_0, 0, 0, null);
        }
        canvas.restore();
    }

    /**
     * 绘制下一页区域以及阴影部分
     * 
     * @param canvas
     * @param bitmap
     */
    private void drawNextPageAreaAndShadow(Canvas canvas, boolean isNextPage) {
        if (isNextPage) {
            if (bitmap_1 == null) {
                return;
            }
        } else {
            if (bitmap_0 == null) {
                return;
            }
        }

        // 计算翻页的角度
        mDegrees =
                (float) Math.toDegrees(Math.atan2(mBezierControl1.x - mCornerX, mBezierControl2.y
                        - mCornerY));
        // 计算阴影的大小
        int leftx;
        int rightx;
        GradientDrawable mBackShadowDrawable;
        if (mIsRTandLB) {
            leftx = (int) (mBezierStart1.x);
            rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4);
            mBackShadowDrawable = mBackShadowDrawableLR;
        } else {
            leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4);
            rightx = (int) mBezierStart1.x;
            mBackShadowDrawable = mBackShadowDrawableRL;
        }

        // 构造区域path
        mPath1.reset();
        if (mTouch.y == mHeight - dI) {
            rightx = (int) ((mBezierStart1.x + 1) + (mBezierStart1.x + 1 - mTouch.x) / 3);
            mPath1.moveTo(rightx, 0);
            mPath1.lineTo(rightx, mHeight);
            mPath1.lineTo(mWidth, mHeight);
            mPath1.lineTo(mWidth, 0);
        } else {
            mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
            mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
            mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
            mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
            mPath1.lineTo(mCornerX, mCornerY);
        }
        mPath1.close();

        // 绘制下一页区域以及阴影部分
        canvas.save();
        canvas.clipPath(mPath0);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        if (isNextPage) {
            canvas.drawBitmap(bitmap_1, 0, 0, null);
        } else {
            canvas.drawBitmap(bitmap_0, 0, 0, null);
        }
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mBackShadowDrawable.setBounds(leftx, (int) mBezierStart1.y, rightx,
                (int) (mMaxLength + mBezierStart1.y));
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制翻起页的阴影
     * 
     * @param canvas
     */
    private void drawCurrentPageShadow(Canvas canvas) {
        double degree;
        int shadowWidth = 20;
        if (mIsRTandLB) {
            degree =
                    Math.PI
                            / 4
                            - Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x - mBezierControl1.x);
        } else {
            degree =
                    Math.PI
                            / 4
                            - Math.atan2(mTouch.y - mBezierControl1.y, mTouch.x - mBezierControl1.x);
        }
        // 翻起页阴影顶点与touch点的距离
        double d1 = shadowWidth * Math.sqrt(2) * Math.cos(degree);
        double d2 = shadowWidth * Math.sqrt(2) * Math.sin(degree);
        float x = (float) (mTouch.x + d1);
        float y;
        if (mIsRTandLB) {
            y = (float) (mTouch.y + d2);
        } else {
            y = (float) (mTouch.y - d2);
        }
        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mTouch.x, mTouch.y);

        /** 防止某些值为Infinite（在4.1中clipPath的path里的值为Infinite时，会报C代码错误），设置成屏幕宽 */
        if (mTouch.y == mHeight - dI) {
            if (Float.isInfinite(mBezierControl1.x)) {
                mBezierControl1.x = mScreenWidth;
            }
            if (Float.isInfinite(mBezierStart1.x)) {
                mBezierStart1.x = mScreenWidth;
            }
        }
        /*******************************************/

        mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
        mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.close();
        float rotateDegrees;
        canvas.save();
        canvas.clipPath(mPath0, Region.Op.XOR);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        int leftx;
        int rightx;
        GradientDrawable mCurrentPageShadow;
        if (mIsRTandLB) {
            leftx = (int) (mBezierControl1.x);
            rightx = (int) mBezierControl1.x + shadowWidth;
            mCurrentPageShadow = mFrontShadowDrawableVLR;
        } else {
            leftx = (int) (mBezierControl1.x - shadowWidth);
            rightx = (int) mBezierControl1.x + 1;
            mCurrentPageShadow = mFrontShadowDrawableVRL;
        }

        rotateDegrees =
                (float) Math.toDegrees(Math.atan2(mTouch.x - mBezierControl1.x, mBezierControl1.y
                        - mTouch.y));
        canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);
        mCurrentPageShadow.setBounds(leftx, (int) (mBezierControl1.y - mMaxLength), rightx,
                (int) (mBezierControl1.y));
        mCurrentPageShadow.draw(canvas);
        canvas.restore();

        /** 此段代码是增加绘制纵向卷轴的左边侧阴影效果 */
        if (mTouch.y == mHeight - dI) {
            canvas.save();
            float i = (mBezierStart1.x + mBezierControl1.x) / 2;
            float f1 = Math.abs(i - mBezierControl1.x);
            float i1 = (mBezierStart2.y + mBezierControl2.y) / 2;
            float f2 = Math.abs(i1 - mBezierControl2.y);
            float f3 = Math.min(f1, f2);
            canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
            GradientDrawable mFolderShadowDrawable;
            int left;
            int right;
            if (mIsRTandLB) {
                left = (int) (mBezierStart1.x - 1);
                right = (int) (mBezierStart1.x + f3 + 1);
                mFolderShadowDrawable = mFolderShadowDrawableLR;
            } else {
                left = (int) (mBezierStart1.x - f3 - 1);
                right = (int) (mBezierStart1.x + 1);
                mFolderShadowDrawable = mFolderShadowDrawableRL;
            }
            canvas.translate(right - mTouch.x + (right - left) - 2, 0);
            mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
                    (int) (mBezierStart1.y + mMaxLength));
            mFolderShadowDrawable.draw(canvas);
            canvas.restore();
            return;
        }

        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mTouch.x, mTouch.y);
        mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.close();
        canvas.save();
        canvas.clipPath(mPath0, Region.Op.XOR);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        if (mIsRTandLB) {
            leftx = (int) (mBezierControl2.y);
            rightx = (int) (mBezierControl2.y + shadowWidth);
            mCurrentPageShadow = mFrontShadowDrawableHTB;
        } else {
            leftx = (int) (mBezierControl2.y - shadowWidth);
            rightx = (int) (mBezierControl2.y + 1);
            mCurrentPageShadow = mFrontShadowDrawableHBT;
        }
        rotateDegrees =
                (float) Math.toDegrees(Math.atan2(mBezierControl2.y - mTouch.y, mBezierControl2.x
                        - mTouch.x));
        canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
        float temp;
        if (mBezierControl2.y < 0) {
            temp = mBezierControl2.y - mHeight;
        } else {
            temp = mBezierControl2.y;
        }
        int ppp = (int) Math.hypot(mBezierControl2.x, temp);
        if (ppp > mMaxLength) {
            mCurrentPageShadow.setBounds((int) (mBezierControl2.x - shadowWidth) - ppp, leftx,
                    (int) (mBezierControl2.x + mMaxLength) - ppp, rightx);
        } else {
            mCurrentPageShadow.setBounds((int) (mBezierControl2.x - mMaxLength), leftx,
                    (int) (mBezierControl2.x), rightx);
        }
        mCurrentPageShadow.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制翻起页背面
     * 
     * @param canvas
     * @param bitmap
     */
    private void drawCurrentBackArea(Canvas canvas, boolean isPrevPage) {
        if (isPrevPage) {
            if (bitmap_1 == null) {
                return;
            }
        } else {
            if (bitmap_0 == null) {
                return;
            }
        }

        // 计算起点到控制点的中点最短距离
        float i = (mBezierStart1.x + mBezierControl1.x) / 2;
        float f1 = Math.abs(i - mBezierControl1.x);
        float i1 = (mBezierStart2.y + mBezierControl2.y) / 2;
        float f2 = Math.abs(i1 - mBezierControl2.y);
        float f3 = Math.min(f1, f2);

        // 绘制阴影部分
        GradientDrawable mFolderShadowDrawable;
        int left;
        int right;
        if (mIsRTandLB) {
            left = (int) (mBezierStart1.x - 1);
            right = (int) (mBezierStart1.x + f3 + 1);
            mFolderShadowDrawable = mFolderShadowDrawableLR;
        } else {
            left = (int) (mBezierStart1.x - f3 - 1);
            right = (int) (mBezierStart1.x + 1);
            mFolderShadowDrawable = mFolderShadowDrawableRL;
        }

        // 构造翻起页背面区域path
        mPath1.reset();
        if (mTouch.y == mHeight - dI) {
            mPath1.moveTo(mTouch.x, 0);
            mPath1.lineTo(mTouch.x, mHeight);
            right = (int) (mBezierStart1.x + 1 + (mBezierStart1.x + 1 - mTouch.x) / 3);
            mPath1.lineTo(right, mHeight);
            mPath1.lineTo(right, 0);
        } else {
            mPath1.moveTo(mBeziervertex2.x, mBeziervertex2.y);
            mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
            mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y);
            mPath1.lineTo(mTouch.x, mTouch.y);
            mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        }
        mPath1.close();
        canvas.save();
        canvas.clipPath(mPath0);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);

        float dis = (float) Math.hypot(mCornerX - mBezierControl1.x, mBezierControl2.y - mCornerY);
        // 构造坐标矩阵
        float f8 = (mCornerX - mBezierControl1.x) / dis;
        float f9 = (mBezierControl2.y - mCornerY) / dis;
        mMatrixArray[0] = 1 - 2 * f9 * f9;
        mMatrixArray[1] = 2 * f8 * f9;
        mMatrixArray[3] = mMatrixArray[1];
        mMatrixArray[4] = 1 - 2 * f8 * f8;
        mMatrix.setValues(mMatrixArray);
        mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y);
        mMatrix.postTranslate(mBezierControl1.x, mBezierControl1.y);

        // 构造并绘制翻起页背面部分
        // 设置画笔颜色过滤器
        paint.setColorFilter(mColorMatrixFilter);
        // 绘制翻起页背面部分
        if (isPrevPage) {
            canvas.drawBitmap(bitmap_1, mMatrix, paint);
        } else {
            canvas.drawBitmap(bitmap_0, mMatrix, paint);
        }
        paint.setColorFilter(null);

        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mFolderShadowDrawable.setBounds(left, (int) mBezierStart1.y, right,
                (int) (mBezierStart1.y + mMaxLength));
        mFolderShadowDrawable.draw(canvas);
        canvas.restore();
    }

    /** 终止翻页特效 */
    private void abortAnimation() {
        mBezierControl1.set(0, 0);
        mBezierEnd1.set(0, 0);
        mBezierControl2.set(0, 0);
        mBezierEnd2.set(0, 0);
    }

    // ***********************************************************仿真翻页处理

    public void controllTouchPointWhenMove() {
        
        
        float t = mTouch.y;
        if (mTouch.y >= mHeight) {
            t = mHeight - dI;
        }
        if (impl.getDirection() == DIRECTION_RIGHT
                || (impl.getDirection() == DIRECTION_LEFT
                        && mTouch.y > mHeight * 0.38 && mTouch.y < mHeight * 0.62)) {
            t = mHeight - dI;
        }
        calcCornerXY(t);
        // 仿真翻页拖动事件处理****************************
        if (mTouch.x >= mWidth) {
            mTouch.x = mWidth - 1;
        } else if (mTouch.x <= 0) {
            mTouch.x = dI;
        }
        if (mTouch.y >= mHeight || mTouch.y == mHeight - 1) {
            mTouch.y = mHeight - dI;
        } else if (mTouch.y <= 0) {
            mTouch.y = dI;
        }
        if (mTouch.y > mHeight * 0.38 && mTouch.y < mHeight * 0.62) {
            if (impl.getDirection() == DIRECTION_RIGHT) {
                mTouch.x -= ((mWidth - mTouch.x)) / 3;
                if (mTouch.x >= mWidth) {
                    mTouch.x = mWidth;
                }
            }
            mTouch.y = mHeight - dI;
        }
        // 仿真翻页拖动事件处理****************************
    }
    
    
    
    public void controllTouchPointWhenUp() {
        mTouch=impl.getTouchPoint();
        if (mTouch.y >= mHeight) {
            mTouch.y = mHeight - dI;
        }
        

        // 计算拖拽点
        float t = mTouch.y;
        if (mTouch.y >= mHeight) {
            t = mHeight - dI;
        }
        if (impl.getDirection() == DIRECTION_RIGHT
                || (impl.getDirection() == DIRECTION_LEFT
                        && mTouch.y > mHeight * 0.38 && mTouch.y < mHeight * 0.62)) {
            t = mHeight - dI;
        }
        calcCornerXY(t);
        if (impl.getDirection() == DIRECTION_RIGHT
                || (impl.getDirection() == DIRECTION_LEFT
                        && mTouch.y > mHeight * 0.38 && mTouch.y < mHeight * 0.62)) {
            if (impl.getDirection() == DIRECTION_RIGHT) {
                mTouch.x = 0;
            } else if (impl.getDirection() == DIRECTION_LEFT
                    && mTouch.y > mHeight * 0.38 && mTouch.y < mHeight * 0.62) {
                mTouch.x = mWidth;
            }
            mTouch.y = mHeight - dI;
        }
        recordModeBookStartAnimation();
     // 重新给mTouch.x和mTouch.y赋值
        mTouch.x = mCornerX;
        mTouch.y = mCornerY;
        if (mTouch.y >= mHeight) {
            mTouch.y = mHeight - dI;
        } else if (mTouch.y <= 0) {
            mTouch.y += dI;
        }
    }

    private Scroller mScroller;

    /**
     * 记录仿真翻页动画结束线程
     * */
    private void recordModeBookStartAnimation() {
//        mFlingRunnable.startAnimationModeBookByTouch();
    }

    private class FlingRunnable implements Runnable {

//        private static final int SCROLL_ANIMATION_DURATION = 200;// 500
//        private static final int TOUCH_ANIMATION_DURATION = 200;
//        private static final int MIN_ANIMATION_DURATION = 200;
        

        public FlingRunnable() {
            mScroller = new Scroller(context, new DecelerateInterpolator());
        }

        /**
         * 移除消息队列里的上个动作
         */
        private void startCommon() {
            view.removeCallbacks(this);
        }

        /**
         * 停止滑动
         * 
         * @param scrollIntoSlots
         */
        private void endFling(boolean scrollIntoSlots) {
            mScroller.forceFinished(true);
            impl.onAnimationEnd();
        }

        // *****************仿真翻页用到的***********************
        /**
         * 开启一个线程来记录仿真翻页动画是否结束
         * */
        public void startAnimationModeBookByTouch() {
            startModeBookUsingDistance();
        }

        public void startModeBookUsingDistance() {
            startCommon();
            view.post(this);
        }

        // *****************仿真翻页用到的***********************

        @Override
        public void run() {
            boolean more = mScroller.computeScrollOffset();// 返回true的话 则动画还没有结束
            if (more) {
                view.post(this);
            } else {
                endFling(true);
                Log.e("mine", "仿真翻页结束。。。。");
            }
        }
        public boolean isFinished() {
            return mScroller.isFinished();
        }
    }

    private void initCurBitmapShadow(int[] color) {

        mCurBitmapShadowColors = color;
        mCurBitmapShadowDrawableRL =
                new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        mCurBitmapShadowColors);
        mCurBitmapShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mCurBitmapShadowDrawableRL.setDither(true);
    }

    public void setCurBitmapShadow(int[] color) {
        initCurBitmapShadow(color);
    }

    /**
     * 设置对应皮肤下仿真翻页的阴影颜色值
     * */
    public void setModeBookShadow(int currentTheme) {
        initSpecEffShadowObjects(currentTheme);
    }

    /**
     * 方法描述：绘制阴影
     * @param canvas
     * @param rightBorder_left
     * @param rightBorder_right
     * @return void
     * @date 2015-3-31 下午12:03:54
     */
    private void drawShadow(Canvas canvas, int rightBorder_left, int rightBorder_right) {
        mCurBitmapShadowDrawableRL.setBounds(rightBorder_left, 0, rightBorder_right, mHeight);
        mCurBitmapShadowDrawableRL.draw(canvas);
    }
    
    
    /**
     * 自由拖动动画
     * 
     * @param scrollMode -1,仿真翻页；0,拖动处理；1,点击向上处理；2,点击向下处理; isGoBsck是否是回翻(即翻页失败)
     */
    private void startScollAnimation( boolean isGoBsck) {
                int dx; // dx 水平方向滑动的距离，负值会使滚动向左滚动
                int dy; // dy 垂直方向滑动的距离，负值会使滚动向上滚动

                if (impl.getDirection() == DIRECTION_LEFT && mTouch.y != mHeight - dI) {
                    PointF p = getCross(mBezierControl1, mBezierEnd1, mBezierControl2, mBezierEnd2);
                    if (p.x >= 0 && p.x <= mWidth && p.y >= 0 && p.y <= mHeight - dI
                            && mBezierControl1.x >= 0 && mBezierControl1.y >= 0
                            && mBezierControl2.x >= 0 && mBezierControl2.y >= 0
                            && mBezierEnd1.x >= 0 && mBezierEnd1.y >= 0 && mBezierEnd2.x >= 0
                            && mBezierEnd2.y >= 0 && Math.round(p.x) != Math.round(p.y)
                            && Math.round(mTouch.x) != Math.round(mTouch.y)) {
                        mTouch.set(p);
                    }
                }

                if (isGoBsck ) { // 如果是往回返执行该动画(即翻页失败)
                    if (impl.getDirection() == DIRECTION_LEFT) {
                        dx = (int) (-(mTouch.x + mWidth));
                        dy = (int) (mCornerY - mTouch.y);
//                        mScrollDirection = DIRECTION_LEFT;
                    } else {
                        dx = (int) (2 * mWidth - mTouch.x);
                        dy = (int) (mCornerY - mTouch.y);
//                        mScrollDirection = DIRECTION_RIGHT;
                    }
                } else {
                    if (impl.getDirection() == DIRECTION_LEFT) {
                        dx = -(int) (mWidth + mTouch.x);
                    } else {
                        dx = (int) (mWidth - mTouch.x + mWidth);
                    }
                    if (mCornerY > 0) {
                        dy = (int) (mHeight - mTouch.y - dI);
                    } else {
                        dy = (int) (dI - mTouch.y); // 防止mTouch.y最终变为0
                    }
                }

                int amationTime = 0;
                if (dx < 0) {
                    amationTime = 400;
                } else {
                    amationTime = 600;
                }
//                mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy, amationTime); // 时间
    }
    
    /**
     * 获取屏幕高
     * 
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     * 
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    
    public void nextPage(Canvas canvas){
     // *****************************
            mTouch.x = mWidth * 3 / 4;
            mTouch.y = mHeight * 5 / 8;
        calcCornerXY(mTouch.y);
            recordModeBookStartAnimation();
            startScollAnimation( false);
            // 重新给mTouch.x和mTouch.y赋值
            mTouch.x = mCornerX;
            mTouch.y = mCornerY;
            if (mTouch.y >= mHeight) {
                mTouch.y = mHeight - dI;
            } else if (mTouch.y <= 0) {
                mTouch.y += dI;
            }
        // *************************************
        mCalculatePoints();
        drawCurrentPageArea(canvas, false);
        drawNextPageAreaAndShadow(canvas, true);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas, false);
    }

}
