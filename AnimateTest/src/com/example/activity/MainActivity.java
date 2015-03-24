package com.example.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.animatetest.R;
import com.example.fragment.BitmapMeshFragment;
import com.example.fragment.PolygonFragment;
import com.example.fragment.SquareFragment;

public class MainActivity extends FragmentActivity {

    protected static final int POLYGONFRAGMENT_DEMO = 0;
    protected static final int BITMAPMESHFRAGMENT_DEMO1 = 1;
    protected static final int BITMAPMESHFRAGMENT_DEMO2 = 2;
    protected static final int BITMAPMESHFRAGMENT_DEMO3 = 3;
    private ArrayList<String> list;
    private ArrayList<String> drawerList;
    private ListView drawerListView;
    private ArrayAdapter<String> adapter;
    private FragmentManager manager;
    private DrawerLayout drawLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        SquareFragment sf = (SquareFragment) manager.findFragmentById(R.id.content_frame);
        if (sf == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.content_frame, new SquareFragment());
            // transaction.add(R.id.content_frame, new BitmapMeshFragment(BitmapMeshFragment.MAGNIFIER));
            // 调用remove方法后 addToBackStack(null) 可以将对应的fragment加入到back栈
            transaction.commit();
        }
        intView();
        intData();
        Toast.makeText(this, "侧滑或点击菜单可以显示更多哦~", Toast.LENGTH_SHORT).show();
        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_filename, drawerList));
        drawerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawLayout.closeDrawers();
                switch (position) {
                case POLYGONFRAGMENT_DEMO:      // 20面体
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.content_frame, new PolygonFragment());
                    transaction.commit();
                    break;
                case BITMAPMESHFRAGMENT_DEMO1: // mesh网格形成的窗帘效果
                    FragmentTransaction transaction1 = manager.beginTransaction();
                    transaction1.replace(R.id.content_frame, new BitmapMeshFragment(BitmapMeshFragment.FRAGMENT_MESH));
                    transaction1.commit();
                    break;

                case BITMAPMESHFRAGMENT_DEMO2: // 放大镜效果
                    FragmentTransaction transaction2 = manager.beginTransaction();
                    transaction2.replace(R.id.content_frame, new BitmapMeshFragment(BitmapMeshFragment.MAGNIFIER));
                    transaction2.commit();

                    break;
                    
                case BITMAPMESHFRAGMENT_DEMO3: // 点击扭曲效果
                    FragmentTransaction transaction3 = manager.beginTransaction();
                    transaction3.replace(R.id.content_frame, new BitmapMeshFragment(BitmapMeshFragment.TOUCHWRAPVIEW));
                    transaction3.commit();

                    break;    
                default:
                    break;
                }
                Toast.makeText(MainActivity.this, drawerList.get(position), 0).show();
            }

        });

    }

    /**
     * 方法描述：初始化控件
     * 
     * @author 尤洋
     * @Title: intView
     * @return void
     * @date 2015-3-13 上午10:39:58
     */
    private void intView() {
        drawLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
    }

    /**
     * 方法描述：初始化数据
     * 
     * @author 尤洋
     * @Title: intData
     * @return void
     * @date 2015-3-13 上午10:28:38
     */
    private void intData() {
        list = new ArrayList<String>();
        drawerList = new ArrayList<String>();
        drawerList.add("OpenGL绘制，颜色渲染");
        drawerList.add("bitmapMesh窗帘效果");
        drawerList.add("magnifier放大镜效果");
        drawerList.add("点击水波纹效果");
        drawerList.add("Blur高斯模糊效果(正在研究中)");
        drawerList.add("颜色过滤器效果(正在研究中)");
        
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
     */
    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_MENU:
            if (drawLayout.isDrawerOpen(GravityCompat.START)) {
                drawLayout.closeDrawers();
            } else {
                drawLayout.openDrawer(GravityCompat.START);
            }

            break;

        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
