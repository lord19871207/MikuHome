package com.example.viewport;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.animatetest.R;

/**
 * 类描述： 通过颜色矩阵相乘获取不同滤镜效果的图片
 * 
 * @Package com.example.mikuhome.view
 * @ClassName: ColorTextView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-12 下午2:56:38
 */
public class ColorImageView extends ImageView {

    private static final int COLOR_SHADOW = 0; // 阴影
    private static final int COLOR_GRAY = 1;// 灰色
    private static final int COLOR_REFLECT = 2;// 反色效果
    private static final int COLOR_OREIGNE = 3;// 橙色
    private static final int COLOR_OLD = 4;// 怀旧
    private static final int COLOR_HIGHCOMPARE = 5;// 高对比度
    private int index = 3;
    private Paint mpaint1;
    private Paint mpaint2;
    private Paint mpaint3;
    private Paint mpaint4;
    private Paint mpaint5;
    private Paint mpaint6;
    private Context context;
    private float touchx;
    private float touchy;
    private int width;
    private int height;
    private int x;
    private int y;
    private Bitmap bitmap;
    private ArrayList<Paint> list;
    private float area1;
    private float area2;
    private float area3;
    private float area4;
    private float area5;
    private float area6;
    private ArrayList<Float> arealist;

    /**
     * 构造方法描述：
     * 
     * @Title: ColorTextView
     * @param context
     * @date 2015-3-12 下午2:56:39
     */
    public ColorImageView(Context context) {
        this(context, null);
    }

    /**
     * 方法描述：
     * 
     * @author 尤洋
     * @Title: getColorMatrix
     * @return void
     * @date 2015-3-26 上午11:24:46
     */
    private void setColorMatrix() {
        mpaint1.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                0.5F, 0, 0f, 0, 0,
                0, 0.5F, 0f, 0, 0,
                0, 0, 0.5F, 0, 0,
                0, 0f, 0, 1, 0,
        })));
        mpaint2.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0, 0, 0, 1, 0,
        })));
        mpaint3.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                -1, 0, 0, 1, 1,
                0, -1, 0, 1, 1,
                0, 0, -1, 1, 1,
                0, 0, 0, 1, 0,
        })));
        mpaint4.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                0, 0, 1, 0, 0,
                0, 1, 0, 0, 0,
                1, 0, 0, 0, 0,
                0, 0, 0, 1, 0,
        })));
        mpaint5.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                0.393F, 0.769F, 0.189F, 0, 0,
                0.349F, 0.686F, 0.168F, 0, 0,
                0.272F, 0.534F, 0.131F, 0, 0,
                0, 0, 0, 1, 0,
        })));
        mpaint6.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                1.5F, 1.5F, 1.5F, 0, -1,
                1.5F, 1.5F, 1.5F, 0, -1,
                1.5F, 1.5F, 1.5F, 0, -1,
                0, 0, 0, 1, 0,
        })));
    }

    /**
     * 构造方法描述：
     * 
     * @Title: ColorTextView
     * @param context
     * @param attrs
     * @date 2015-3-12 下午2:56:39
     */
    public ColorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法描述：
     * 
     * @Title: ColorTextView
     * @param context
     * @param attrs
     * @param defStyle
     * @date 2015-3-12 下午2:56:39
     */
    public ColorImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView(attrs);
    }

    /**
     * 方法描述：初始化自定义控件
     * 
     * @author 尤洋
     * @Title: initView
     * @return void
     * @date 2015-3-12 下午3:56:47
     */
    private void initView(AttributeSet attr) {
        inintPaint();
        initTouchArea();
        int resourceId = attr.getAttributeResourceValue(null, "colorbackground", R.drawable.ac);
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        x = width / 2 - bitmap.getWidth() / 2;
        y = height / 2 - bitmap.getHeight() / 2;
    }

    /**
     * 方法描述：初始化点击区域
     * 
     * @author 尤洋
     * @Title: initTouchArea
     * @return void
     * @date 2015-3-26 下午12:42:09
     */
    private void initTouchArea() {
        arealist = new ArrayList<Float>();
        arealist.add(area1);
        arealist.add(area2);
        arealist.add(area3);
        arealist.add(area4);
        arealist.add(area5);
        arealist.add(area6);
    }

    /**
     * 方法描述：初始化6种颜色矩阵的画笔
     * 
     * @author 尤洋
     * @Title: inintPaint
     * @return void
     * @date 2015-3-26 下午12:14:28
     */
    private void inintPaint() {
        mpaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint5 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mpaint6 = new Paint(Paint.ANTI_ALIAS_FLAG);
        list = new ArrayList<Paint>();
        list.add(mpaint1);
        list.add(mpaint2);
        list.add(mpaint3);
        list.add(mpaint4);
        list.add(mpaint5);
        list.add(mpaint6);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setStyle(Paint.Style.FILL);
            list.get(i).setColor(Color.GREEN);
            list.get(i).setStrokeWidth(15);
        }
        setColorMatrix();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.TextView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawLine(arealist.get(index), 0, arealist.get(index), height, list.get(index));
        canvas.drawBitmap(bitmap, 0, 0, list.get(index));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        area1 = (float) width / 6;
        area2 = (float) 2 * width / 6;
        area3 = (float) 3 * width / 6;
        area4 = (float) 4 * width / 6;
        area5 = (float) 5 * width / 6;
        area6 = (float) width;
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    // @Override
    // public boolean onTouch(View v, MotionEvent event){
    // switch (event.getAction()) {
    // case MotionEvent.ACTION_DOWN:
    //
    // break;
    //
    //
    // default:
    // break;
    // }
    // }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        touchx = event.getX();
        touchy = event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
            Log.i("youyang", "1点击后的事件是event.getAction()=" + event.getAction());
            controlClickArea();
            postInvalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.i("youyang", "2点击后的事件是event.getAction()=" + event.getAction());
            index = 2;
            postInvalidate();
            break;
        default:
            break;
        }

        return true;
    }

    /**
     * 方法描述：在不同点击区域设置不同的index
     * 
     * @author 尤洋
     * @Title: controlClickArea
     * @return void
     * @date 2015-3-26 下午1:37:52
     */
    private void controlClickArea() {
        if (touchx < area1) {
            setIndex(0);
        } else if (touchx < area2 && touchx > area1) {
            setIndex(1);
        } else if (touchx < area3 && touchx > area2) {
            setIndex(2);
        } else if (touchx < area4 && touchx > area3) {
            setIndex(3);
        } else if (touchx < area5 && touchx > area4) {
            setIndex(4);
        } else if (touchx < area6 && touchx > area5) {
            setIndex(5);
        }
    }

}
