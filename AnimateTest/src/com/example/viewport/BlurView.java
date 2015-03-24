package com.example.viewport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.animatetest.R;

/**
 * 类描述：
 * 一个可以自己设置模糊程度的自定义控件，并且能随着手势上下滑动控制模糊图片的透明度
 * @Package com.example.mikuhome.view
 * @ClassName: BlurView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-12 上午11:20:26
 */
public class BlurView extends ImageView {
    private Context context;
    private int width, height;
    /**
     * 构造方法描述：
     * 
     * @Title: BlurView
     * @param context
     * @param attrs
     * @date 2015-3-12 上午11:20:59
     */
    @SuppressLint("NewApi")
    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        int resourceId=attrs.getAttributeResourceValue(null, "blurbackground", R.drawable.ic_launcher);
        Bitmap bitmap =BitmapFactory.decodeResource(getResources(),resourceId); 
        Bitmap outbitmap =blurBitmap(bitmap,25.f);
        setBackground(new BitmapDrawable(outbitmap));
        setOnTouchListener(new OnTouchListener() {

            private float mLastY;

            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float y = event.getY();
//                    float alphaDelt = (y - mLastY) / 1000;
//                    float alpha = getAlpha() + alphaDelt;
//                    if (alpha > 1.0) {
//                        alpha = 1.0f;
//                    } else if (alpha < 0.0) {
//                        alpha = 0.0f;
//                    }
                    setAlpha(y/height);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                }
                return true;
            }
        });
    }

    
    
    @SuppressLint("NewApi")
    public Bitmap blurBitmap(Bitmap bitmap,float radius) {

        // Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        // Set the radius of the blur 25
        blurScript.setRadius(radius);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        bitmap.recycle();

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;

    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

}
