package com.example.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.example.animatetest.R;

/**
 * 类描述：
 * @Package com.example.activity
 * @ClassName: BookcontentActivity
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 下午3:06:52
 */
public class BookcontentActivity extends ActionBarActivity {

    /**
     * 构造方法描述：
     * @Title: BookcontentActivity
     * @date 2015-3-31 下午3:06:53
     */
    public BookcontentActivity() {
        // TODO Auto-generated constructor stub
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS); // 去标题栏
        setContentView(R.layout.activity_bookcontent);
    }

}
