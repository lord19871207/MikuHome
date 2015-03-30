package com.example.fragment;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animatetest.ColorSquare;
import com.example.animatetest.Polygon;
import com.example.animatetest.Vertex;
import com.example.render.OpenGLRender;
import com.example.render.OpenGLRender.IOpenGLDemo;

/**
 * 类描述：
 * @Package com.example.fragment
 * @ClassName: PolygonFragment
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-24 下午1:12:33
 */
public class PolygonFragment extends Fragment implements IOpenGLDemo{
    private GLSurfaceView mGlSurfaceView;
    private Vertex vt;
    private Vertex vt1;
    private Vertex vt2;
    private Polygon pol;
    private ColorSquare s1;
    private ColorSquare s2;
    private ColorSquare s3;
    
    public PolygonFragment() {
        vt=new Vertex(-1.2f,0,-8);
        vt1=new Vertex(1.2f,0,-8);
        vt2=new Vertex(0,1.2f,-8);
        pol=new Polygon();
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
        mGlSurfaceView=new GLSurfaceView(getActivity());
        mGlSurfaceView.setRenderer(new OpenGLRender(this));
        return mGlSurfaceView;
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
        mGlSurfaceView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
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
    public void drawScene(GL10 gl) {
        gl.glClearColor(1.0f, 0.5f, 0.1f, 0.7f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glTranslatef(-1.2f, 0.6f, -4);
        gl.glPushMatrix();
        pol.draw(gl);
        gl.glPopMatrix();
        
    }

    /* (non-Javadoc)
     * @see com.example.render.OpenGLRender.IOpenGLDemo#initScene(javax.microedition.khronos.opengles.GL10)
     */
    @Override
    public void initScene(GL10 gl) {
        // TODO Auto-generated method stub
        
    }
}
