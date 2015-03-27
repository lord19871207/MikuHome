package com.example.viewport;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.animatetest.R;

/**
 * 三张图片可以互相切换位置，可以自定以相关属性
 * 目前尚未完成
 * 
 * @Package com.example.viewport.view
 * @ClassName: ThirddimensionView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-27 下午12:03:15
 */
public class ThirddimensionView extends RelativeLayout {
    private View view;
    private ImageView centerImageView;
    public ImageView leftImageView;
    private ImageView rightImageView;
    private int width, height;
    private PointF start;

    public ThirddimensionView(Context context) {
        this(context, null);
    }

    public ThirddimensionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public ThirddimensionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        start = new PointF();
        /*
         * @author youyang 设置为true时，允许多子类进行静态转换 也就是说把这个属 性设成true的时候每次viewGroup(看Gallery的源码就可以看到它是从ViewGroup间
         * 接继承过来的)在重新画它的child的时候都会促发getChildStaticTransformation 这个函数,所以我 们只需要在这个函数里面去加上旋转和放大的操作就可以了
         */
        setStaticTransformationsEnabled(true);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_three_picture, this);
        centerImageView = (ImageView) view.findViewById(R.id.img_2);
        leftImageView = (ImageView) view.findViewById(R.id.img_1);
        rightImageView = (ImageView) view.findViewById(R.id.img_3);
        rightLp = (MarginLayoutParams) rightImageView.getLayoutParams();
        leftLp = (MarginLayoutParams) leftImageView.getLayoutParams();
        centerLp = (MarginLayoutParams) centerImageView.getLayoutParams();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bu1);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.bu3);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 120, true);
        bitmap1 = Bitmap.createScaledBitmap(bitmap1, 100, 120, true);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, 100, 120, true);
        leftImageView.setImageBitmap(bitmap);
        centerImageView.setImageBitmap(bitmap1);
        rightImageView.setImageBitmap(bitmap2);
        list = new ArrayList<View>();
        list.add(leftImageView);
        list.add(centerImageView);
        list.add(rightImageView);
        addChildrenForAccessibility(list);

    }

    private int maxZoom = -250;// 图片缩放最大值
    private int maxRotateAngle = 50;// 选中的最大角度
    private int centerOfLayout;
    private Camera camera = new Camera();
    private ArrayList<View> list;
    private Matrix savedRightMatrix;
    private ViewGroup.MarginLayoutParams rightLp;
    private ViewGroup.MarginLayoutParams leftLp;
    private ViewGroup.MarginLayoutParams centerLp;

    // 得到图片中心点位置的方法
    private int centerOfView(View view) {
        // 中点位置就是view的左边坐标加上view宽度的1/2，
        return view.getLeft() + view.getWidth() / 2;
    }

    // 整个控件展示中点
    private int centerOfLayout() {
        // 中点的坐标所在位置
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    // 控件大小的改变 复写onSizeChanged方法
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 当控件大小一改变，就获得新的中心
        centerOfLayout = centerOfLayout();
        super.onSizeChanged(w, h, oldw, oldh);

    }

    // 图片变形
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        // 在改变图片之前先清空上一次图片的变形效果
        t.clear();
        // 设置类型为矩阵
        t.setTransformationType(Transformation.TYPE_MATRIX);
        // 倾斜角度为0
        int rotateAngle = 0;
        // 获得视图的中点
        int centerOfChild = centerOfView(child);
        int width = child.getWidth();

        // 当前图片在中心位置的时候,直接显示图片
        if (centerOfChild == centerOfLayout) {
//            transformationBitmap(child, t, rotateAngle);
        } else {
            // 旋转角度,里面强转为float避免整数相除出现0的情况。最后再强转为int
            rotateAngle = (int) ((float) (centerOfChild - centerOfLayout) / width * maxRotateAngle);

            if (Math.abs(rotateAngle) > maxRotateAngle) {
                rotateAngle = rotateAngle < 0 ? -maxRotateAngle : maxRotateAngle;
            }

//            transformationBitmap(child, t, rotateAngle);
        }

        return true;
    }

    // 图形变化的核心方法
    private void transformationBitmap(View child, Transformation t, int rotateAngle) {
        // 记录图片变形效果，想要达到图片变形效果，必须要用到camera对象
        camera.save();

        // 获得矩阵
        Matrix imagemMatrix = t.getMatrix();

        // 获得角度
        int rotate = Math.abs(rotateAngle);
        int width = child.getWidth();
        int height = child.getHeight();

//         做图片的平移效果 分别对应于x轴 y轴 z轴，对应的效果是图片绕x轴 y轴旋转的立体效果，z轴是远近缩放
//         camera.translate(0.0f, 0.0f, 100.0f);

        // 图片离中点越远 角度越大，对此作出判断
         if (rotate < maxRotateAngle) {

        // 给图片缩放值赋予一个变量，这样当角度变化时 缩放也跟着变化
        float zoom = (float) (rotate * 1.5 + maxZoom);

        // 缩放图片
         camera.translate(0.0f, 0.0f, zoom);

        // 图片透明度也随着rotate变化
        // child.setAlpha((int) (255 - rotate * 2.5));

         }
        // 可以随意选择x y z轴交叉旋转产生各种不同的效果
        // 以y轴进行旋转
        // camera.rotateX(rotateAngle);
        // 以x轴进行旋转
//         camera.rotateY(rotateAngle);
        // 以z轴进行旋转
        // camera.rotateZ(rotateAngle);

        // 将图片的效果交给imageMatrix处理
        camera.getMatrix(imagemMatrix);

        // 相当于图片先左移width/2，再上移height/2，使图片中心点和旋转点重合
        imagemMatrix.preTranslate(-(width / 2), -(height / 2));
        // 相当于图片先右移width/2，再下移移height/2，旋转完毕后再移回原位置
        imagemMatrix.postTranslate((width / 2), (height / 2));
        // 变形完后 再还原，方便下一次变形。
        camera.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            start.set(event.getX(), event.getY());
            // reset(leftLp);
            break;
        case MotionEvent.ACTION_MOVE:
            float dx = event.getX() - start.x;
            float dy = event.getY() - start.y;
            setPosition(leftImageView,leftLp, dx, dy,350);
            setPosition(rightImageView,rightLp, dx, dy,350);
            Log.i("youyang", "centerImageView" + "----centerLp.leftMargin:" + centerLp.leftMargin + ",centerLp.topMargin:"
                    + centerLp.topMargin
                    + "centerLp.rightMargin:" + centerLp.rightMargin + ",centerLp.bottomMargin:" + centerLp.bottomMargin
                    );
            setPosition(centerImageView,centerLp, dx, dy,360);
//             rightMatrix.postTranslate(dx, dy);
            break;
        case MotionEvent.ACTION_CANCEL:
            break;

        default:
            break;
        }

        // rightImageView.setImageMatrix(rightMatrix);
        // postInvalidate();
        return true;
    }


    /**
     * 方法描述：切换三张图片的位置
     * 
     * @author 尤洋
     * @Title: setPosition
     * @param leftLp2
     * @param touchX2
     * @param touchY2
     * @return void
     * @date 2015-3-27 下午5:22:53
     */
    private void setPosition(ImageView imageView,MarginLayoutParams leftLp2, float dx, float dy,int top) {
        int leftMargin = (int) (leftLp2.leftMargin + dx / 1000)%(width/5);
        int topMargin = (int) (leftLp2.topMargin + dy / 1000)%(height/2);
        int rightMargin = (int) (leftLp2.rightMargin+ dx/1000)%(width/5);
        int bottomMargin = (int) (leftLp2.bottomMargin+ dy/1000)%(height/2);
        // Log.i("youyang", "dx的值为："+dx+"dy的值为："+dy);
        leftLp2.setMargins(leftMargin+170, top, rightMargin+170, 0);
        imageView.setLayoutParams(leftLp2);
    }

}
