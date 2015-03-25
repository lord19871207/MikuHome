package com.example.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.example.animatetest.R;
import com.example.fragment.BitmapMeshFragment;
import com.example.fragment.PolygonFragment;
import com.example.fragment.SquareFragment;
import com.example.viewport.PullToZoomListView;

public class MainActivity extends FragmentActivity {

    protected static final int POLYGONFRAGMENT_DEMO = 0;
    protected static final int BITMAPMESHFRAGMENT_DEMO1 = 1;
    protected static final int BITMAPMESHFRAGMENT_DEMO2 = 2;
    protected static final int BITMAPMESHFRAGMENT_DEMO3 = 3;
    private ArrayList<String> drawerList;
    private PullToZoomListView drawerListView;
    private FragmentManager manager;
    private DrawerLayout drawLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        switchToNextFragment(new SquareFragment());
        intView();
        intData();
        Toast.makeText(this, "侧滑或点击菜单可以显示更多哦~", Toast.LENGTH_SHORT).show();
        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_filename, drawerList));
        drawerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawLayout.closeDrawers();
                switch (position-1) {
                case POLYGONFRAGMENT_DEMO:      // 20面体
                    switchToNextFragment(new PolygonFragment());
                    break;
                case BITMAPMESHFRAGMENT_DEMO1: // mesh网格形成的窗帘效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.FRAGMENT_MESH));
                    break;

                case BITMAPMESHFRAGMENT_DEMO2: // 放大镜效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.MAGNIFIER));
                    break;
                    
                case BITMAPMESHFRAGMENT_DEMO3: // 点击扭曲效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.TOUCHWRAPVIEW));
                    break;    
                default:
                    break;
                }
                Toast.makeText(MainActivity.this, drawerList.get(position), 0).show();
            }

        });

    }

    /**
     * 方法描述：
     * @author 尤洋
     * @Title: switchToNextFragment
     * @param squareFragment
     * @return void
     * @date 2015-3-25 下午3:36:26
     */
    private void switchToNextFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
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
        drawerListView = (PullToZoomListView) findViewById(R.id.left_drawer);
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
        drawerList = new ArrayList<String>();
        drawerList.add("OpenGL绘制，颜色渲染");
        drawerList.add("bitmapMesh窗帘效果");
        drawerList.add("magnifier放大镜效果");
        drawerList.add("点击水波纹效果");
        drawerList.add("Blur高斯模糊效果(研究中)");
        drawerList.add("颜色过滤器效果(研究中)");
        
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
