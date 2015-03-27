package com.example.viewport;
import android.app.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.animatetest.R;

public class MatrixImageView extends ImageView {
    private static final int MODE_NONE = 0x00123;// 默认的触摸模式
    private static final int MODE_DRAG = 0x00321;// 拖拽模式
    private static final int MODE_ZOOM = 0x00132;// 缩放or旋转模式

    private int mode;// 当前的触摸模式

    private float preMove = 1F;// 上一次手指移动的距离
    private float saveRotate = 0F;// 保存了的角度值
    private float rotate = 0F;// 旋转的角度

    private float[] preEventCoor;// 上一次各触摸点的坐标集合

    private PointF start, mid;// 起点、中点对象
    private Matrix currentMatrix, savedMatrix;// 当前和保存了的Matrix对象
    private Context mContext;// Fuck……

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        /*
         * 实例化对象
         */
        currentMatrix = new Matrix();
        savedMatrix = new Matrix();
        start = new PointF();
        mid = new PointF();

        // 模式初始化
        mode = MODE_NONE;

        /*
         * 设置图片资源
         */
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bu1);
        bitmap = Bitmap.createScaledBitmap(bitmap, getScreenSize((Activity) mContext)[0],
                getScreenSize((Activity) mContext)[1], true);
        setImageBitmap(bitmap);
    }

    /**
     * 方法描述：
     * 
     * @author 尤洋
     * @Title: getScreenSize
     * @param mContext2
     * @return
     * @return int[]
     * @date 2015-3-27 下午3:32:25
     */
    private int[] getScreenSize(Context mContext2) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int[] size=new int[2];
        size[0]=width;
        size[1]=height;
        return size;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:// 单点接触屏幕时
            savedMatrix.set(currentMatrix);
            start.set(event.getX(), event.getY());
            mode = MODE_DRAG;
            preEventCoor = null;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:// 第二个点接触屏幕时
            preMove = calSpacing(event);
            if (preMove > 10F) {
                savedMatrix.set(currentMatrix);
                calMidPoint(mid, event);
                mode = MODE_ZOOM;
            }
            preEventCoor = new float[4];
            preEventCoor[0] = event.getX(0);
            preEventCoor[1] = event.getX(1);
            preEventCoor[2] = event.getY(0);
            preEventCoor[3] = event.getY(1);
            saveRotate = calRotation(event);
            break;
        case MotionEvent.ACTION_UP:// 单点离开屏幕时
        case MotionEvent.ACTION_POINTER_UP:// 第二个点离开屏幕时
            mode = MODE_NONE;
            preEventCoor = null;
            break;
        case MotionEvent.ACTION_MOVE:// 触摸点移动时
            /*
             * 单点触控拖拽平移
             */
            if (mode == MODE_DRAG) {
                currentMatrix.set(savedMatrix);
                float dx = event.getX() - start.x;
                float dy = event.getY() - start.y;
                currentMatrix.postTranslate(dx, dy);
            }
            /*
             * 两点触控拖放旋转
             */
            else if (mode == MODE_ZOOM && event.getPointerCount() == 2) {
                float currentMove = calSpacing(event);
                currentMatrix.set(savedMatrix);
                /*
                 * 指尖移动距离大于10F缩放
                 */
                if (currentMove > 10F) {
                    float scale = currentMove / preMove;
                    currentMatrix.postScale(scale, scale, mid.x, mid.y);
                }
                /*
                 * 保持两点时旋转
                 */
                if (preEventCoor != null) {
                    rotate = calRotation(event);
                    float r = rotate - saveRotate;
                    currentMatrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                }
            }
            break;
        }

        setImageMatrix(currentMatrix);
        return true;
    }

    /**
     * 计算两个触摸点间的距离
     */
    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两个触摸点的中点坐标
     */
    private void calMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 计算旋转角度
     * 
     * @param 事件对象
     * @return 角度值
     */
    private float calRotation(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }
}
