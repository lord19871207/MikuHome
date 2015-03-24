package com.example.fragment;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.animatetest.ColorSquare;
import com.example.animatetest.R;
//import android.app.Fragment;

public class SquareFragment extends Fragment {
	private ColorSquare s;
    private int angle;
    
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
	    GLSurfaceView view = new GLSurfaceView(getActivity());
        s = new ColorSquare();
        angle = 20;
        view.setRenderer(new GLSurfaceView.Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                //设置背景色
                gl.glClearColor(0.5f, 0.0f, 0.0f, 0.5f);
                //使绘制的图形光滑
                gl.glShadeModel(GL10.GL_SMOOTH);
                gl.glClearDepthf(1.0f);
                gl.glEnable(GL10.GL_DEPTH_TEST);
                gl.glDepthFunc(GL10.GL_LEQUAL);
                gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                // TODO Auto-generated method stub
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadIdentity();
                GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();
                gl.glTranslatef(0, 0, -6);
                // gl.glRotatef(30.0f, 10.0f, 20.0f, 10.0f);
                gl.glPushMatrix();
                gl.glRotatef(angle, 0, 20, 40);
                s.draw(gl);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotatef(-angle, 0, 0, 1);
                gl.glTranslatef(2, 0, 0);
                gl.glScalef(.5f, .5f, .5f);
                s.draw(gl);

                gl.glPushMatrix();
                /*
                 * 要理解（先旋转再平移） 和 （先平移再旋转） 的区别 
                 * 角度递增的情况下 先旋转再平移的话 其实是以平移的距离为半径 进行旋转 
                 * 而先平移再旋转 其实是自转 
                 * 原因是 永远都按照 旋转之后的坐标系进行平移
                 */
                gl.glRotatef(-angle, 0, 0, 1);
                gl.glTranslatef(2, 0, 0);
                gl.glScalef(.5f, .5f, .5f);
                gl.glRotatef(angle * 10, 0, 0, 1);
                s.draw(gl);
                gl.glPopMatrix();
                gl.glPopMatrix();

                angle++;
            }
        });
		return view;
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
