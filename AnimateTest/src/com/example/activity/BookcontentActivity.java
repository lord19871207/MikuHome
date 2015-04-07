package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.animatetest.R;

/**
 * 类描述：
 * @Package com.example.activity
 * @ClassName: BookcontentActivity
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-31 下午3:06:52
 */
public class BookcontentActivity extends Activity {
    public BookcontentActivity() {
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        int id =intent.getIntExtra("layoutid", R.layout.activity_bookcontent);
        setContentView(id);
//        setContentView(R.layout.activity_bookcontent_scroll);
    }

}
