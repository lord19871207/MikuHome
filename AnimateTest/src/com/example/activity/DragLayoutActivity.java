package com.example.activity;

import android.app.Activity;
import android.os.Bundle;
import com.example.animatetest.R;
import com.example.viewport.DragInnerLayout;

/**
 * 类描述：
 * @Package com.example.activity
 * @ClassName: DragLayoutActivity
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-30 下午10:33:20
 */
public class DragLayoutActivity extends Activity {

    public DragLayoutActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        DragInnerLayout dragLayout = (DragInnerLayout) findViewById(R.id.dragLayout);

        if(getIntent().getBooleanExtra("horizontal", false)) {
            dragLayout.setDragHorizontal(true);
        }
        if(getIntent().getBooleanExtra("vertical", false)) {
            dragLayout.setDragVertical(true);
        }
        if(getIntent().getBooleanExtra("edge", false)) {
            dragLayout.setDragEdge(true);
        }
        if(getIntent().getBooleanExtra("capture", false)) {
            dragLayout.setDragCapture(true);
        }
    }
}
