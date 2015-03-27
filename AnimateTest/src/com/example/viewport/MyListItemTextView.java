package com.example.viewport;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.animatetest.R;

public class MyListItemTextView extends TextView {
	

	private Paint marginPaint;
	private Paint linePaint;
	private int pageColor;
	private float margin;
	
	private float touchx;
    private float touchy;
    private ColorMatrix cmatrix=new ColorMatrix(new float[]{
            0.5F, 0, 0.6f, 0, 0,  
            0, 0.5F, 0.4f, 0, 0,  
            0, 0, 0.5F, 0, 0,  
            0, 0.7f, 0, 1, 0,   
    });

	public MyListItemTextView(Context context) {
		super(context);
		init();
	}

	public MyListItemTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyListItemTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		//获取资源引用
		Resources myresource=getResources();
		//获取笔刷
		marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//给笔刷上色
		marginPaint.setColor(myresource.getColor(R.color.item_margin));
		linePaint.setColor(getResources().getColor(R.color.item_lines	));
		pageColor = myresource.getColor(R.color.item_paper);
		margin = myresource.getDimension(R.dimen.item_margin	);
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//绘制背景颜色
		canvas.drawColor(pageColor);
		//绘制边缘
		canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
		canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(),
				getMeasuredHeight(), linePaint);
		canvas.drawLine(margin, 0, margin, 
				getMeasuredHeight(), marginPaint);
		
		canvas.drawCircle(margin, getMeasuredHeight()/4, getMeasuredHeight()/5, marginPaint);
		canvas.save();
		super.onDraw(canvas);
		canvas.restore();
	}
	
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        touchx=event.getX();
        touchy=event.getY();
        cmatrix=new ColorMatrix(new float[]{
                0.5F, touchx/getMeasuredHeight(), 0.6f, touchy/getMeasuredHeight(), 0,  
                0, 0.5F, 0.4f, 0, 0,  
                0, touchy/getMeasuredHeight(), 0.5F, 0, 0,  
                0, 0.7f, touchy/getMeasuredHeight(), touchy/getMeasuredHeight(), 0,   
        });
        marginPaint.setColorFilter(new ColorMatrixColorFilter(cmatrix));
        linePaint.setColorFilter(new ColorMatrixColorFilter(cmatrix));
        postInvalidate();
        return false;
    }

}
