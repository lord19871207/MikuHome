package com.example.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.animatetest.R;
import com.example.viewport.ColorImageView;
import com.example.viewport.Magnifier;
import com.example.viewport.TouchWrapView;

/**
 * 类描述：窗帘效果
 * 
 * @Package com.example.fragment
 * @ClassName: BitmapMeshFragment
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-24 下午1:12:33
 */
@SuppressLint("ValidFragment")
public class BitmapMeshFragment extends Fragment implements OnClickListener {
    public static final String FRAGMENT_MESH = "fragment_mesh";
    public static final String MAGNIFIER = "Magnifier";
    public static final String TOUCHWRAPVIEW = "Touchwrapview";
    public static final String COLORIMAGEVIEW = "ColorImageView";
    private Activity context;
    String type = "";
    private View meshView;
    private ColorImageView view;

    public BitmapMeshFragment() {
    }

    public BitmapMeshFragment(String type) {
        this.type = type;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            context = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    "must implement onNewItemAddedSetAdapterListerner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (type.equals(FRAGMENT_MESH)) {
            meshView = inflater.inflate(R.layout.fragment_mesh, container, false);
        } else if (type.equals(MAGNIFIER)) {
            meshView = new Magnifier(getActivity());
        } else if (type.equals(TOUCHWRAPVIEW)) {
            meshView = inflater.inflate(R.layout.fragment_mesh_touch, container, false);

        } else if (type.equals(COLORIMAGEVIEW)) {
            meshView = inflater.inflate(R.layout.fragment_color_matrix, container, false);
            init(meshView);

        }
        return meshView;
    }

    /**
     * 方法描述：
     * 
     * @author 尤洋
     * @Title: init
     * @param meshView2
     * @return void
     * @date 2015-3-26 下午5:00:31
     */
    private void init(View meshView2) {
        view = (ColorImageView) meshView2.findViewById(R.id.color_matrix);
        Button bt1 = (Button) meshView2.findViewById(R.id.bt1);
        Button bt2 = (Button) meshView2.findViewById(R.id.bt2);
        Button bt3 = (Button) meshView2.findViewById(R.id.bt3);
        Button bt4 = (Button) meshView2.findViewById(R.id.bt4);
        Button bt5 = (Button) meshView2.findViewById(R.id.bt5);
        Button bt6 = (Button) meshView2.findViewById(R.id.bt6);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt1:
            Toast.makeText(context, "阴影效果", 0).show();
            view.setIndex(0);
            break;
        case R.id.bt2:
            Toast.makeText(context, "灰色效果", 0).show();
            view.setIndex(1);
            break;
        case R.id.bt3:
            Toast.makeText(context, "反色效果", 0).show();
            view.setIndex(2);
            break;
        case R.id.bt4:
            Toast.makeText(context, "橙色效果", 0).show();
            view.setIndex(3);
            break;
        case R.id.bt5:
            Toast.makeText(context, "怀旧效果", 0).show();
            view.setIndex(4);
            break;
        case R.id.bt6:
            Toast.makeText(context, "高对比度效果", 0).show();
            view.setIndex(5);
            break;

        default:
            break;
        }
        view.invalidate();
    }

}
