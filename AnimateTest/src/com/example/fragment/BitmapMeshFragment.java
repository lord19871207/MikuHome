package com.example.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animatetest.R;
import com.example.viewport.Magnifier;
import com.example.viewport.TouchWrapView;

/**
 * 类描述：窗帘效果
 * @Package com.example.fragment
 * @ClassName: BitmapMeshFragment
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-24 下午1:12:33
 */
@SuppressLint("ValidFragment")
public class BitmapMeshFragment extends Fragment{
    public static final String FRAGMENT_MESH = "fragment_mesh";
    public static final String MAGNIFIER = "Magnifier";
    public static final String TOUCHWRAPVIEW = "Touchwrapview";
    public static final String COLORIMAGEVIEW = "ColorImageView";
    
    String type="";
    private View meshView;
    
    public BitmapMeshFragment() {
    }
    public BitmapMeshFragment(String type) {
        this.type=type;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+
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
        if(type.equals(FRAGMENT_MESH)){
            meshView=inflater.inflate(R.layout.fragment_mesh, container, false);
        }else if(type.equals(MAGNIFIER)){
            meshView=new Magnifier(getActivity());
        }else if(type.equals(TOUCHWRAPVIEW)){
            meshView=inflater.inflate(R.layout.fragment_mesh_touch, container, false);
            
        }else if(type.equals(COLORIMAGEVIEW)){
            meshView=inflater.inflate(R.layout.fragment_color_matrix, container, false);
        }
        return meshView;
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
    
    
}
