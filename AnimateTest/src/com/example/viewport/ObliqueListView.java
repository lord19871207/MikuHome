package com.example.viewport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 类描述：带有倾斜的listview
 * @Package com.example.viewport
 * @ClassName: ObliqueListView
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-25 上午9:35:45
 */
public class ObliqueListView extends ListView {
    private Camera camera;
    private Matrix matrix;
    private int resourceIdx;
    private int resourceIdy;
    private int resourceIdz;

    /**
     * 构造方法描述：
     * @Title: ObliqueListView
     * @param context
     * @date 2015-3-25 上午9:35:45
     */
    public ObliqueListView(Context context) {
        super(context);
    }

    /**
     * 构造方法描述：
     * @Title: ObliqueListView
     * @param context
     * @param attrs
     * @date 2015-3-25 上午9:35:45
     */
    public ObliqueListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        resourceIdx = attrs.getAttributeIntValue(null, "xvalue", 0);
        resourceIdy = attrs.getAttributeIntValue(null, "yvalue", 0);
        resourceIdz = attrs.getAttributeIntValue(null, "zvalue", 0);
        
        camera=new Camera();
        matrix=new Matrix();
    }

    /**
     * 构造方法描述：
     * @Title: ObliqueListView
     * @param context
     * @param attrs
     * @param defStyle
     * @date 2015-3-25 上午9:35:45
     */
    public ObliqueListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    /* (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        camera.save();
        camera.rotate(resourceIdx, resourceIdy, resourceIdz);
        camera.getMatrix(matrix);
        matrix.preTranslate(-getWidth() / 2, -getHeight() / 2);  
        matrix.postTranslate(getWidth() / 2, getHeight() / 2);  
        canvas.concat(matrix);
        super.onDraw(canvas);
        camera.restore();
    }

}
