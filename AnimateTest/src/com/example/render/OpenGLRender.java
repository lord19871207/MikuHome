package com.example.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class OpenGLRender implements Renderer {
    private IOpenGLDemo openGLDemo;
    
    public OpenGLRender(IOpenGLDemo openGLDemo) {
        this.openGLDemo=openGLDemo;
    }

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
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if(openGLDemo!=null){
            openGLDemo.drawScene(gl);
        }
        
    }
    public interface IOpenGLDemo{
        public void drawScene(GL10 gl);
    }
    
}

