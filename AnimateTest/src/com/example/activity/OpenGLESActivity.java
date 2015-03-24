package com.example.activity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.example.animatetest.ColorSquare;
import com.example.animatetest.Polygon;
import com.example.animatetest.Vertex;
import com.example.render.OpenGLRender;
import com.example.render.OpenGLRender.IOpenGLDemo;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

public class OpenGLESActivity extends Activity implements IOpenGLDemo{
    private GLSurfaceView mGlSurfaceView;
    private Vertex vt;
    private Vertex vt1;
    private Vertex vt2;
    private Polygon pol;
    private ColorSquare s1;
    private ColorSquare s2;
    private ColorSquare s3;
    
    public OpenGLESActivity() {
        vt=new Vertex(-1.2f,0,-8);
        vt1=new Vertex(1.2f,0,-8);
        vt2=new Vertex(0,1.2f,-8);
        pol=new Polygon();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
        mGlSurfaceView=new GLSurfaceView(this);
        mGlSurfaceView.setRenderer(new OpenGLRender(this));
        setContentView(mGlSurfaceView);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    public void drawScene(GL10 gl) {
        gl.glClearColor(1.0f, 0.5f, 0.1f, 0.7f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//        vt.draw(gl);
//        vt1.draw(gl);
//        vt2.draw(gl);
        gl.glTranslatef(-1.2f, 0.6f, -4);
        gl.glPushMatrix();
        vt.draw(gl);
        gl.glPopMatrix();
        
        
        
        gl.glTranslatef(1.2f, -0.6f, -4);
        gl.glPushMatrix();
        vt2.draw(gl);
        gl.glPopMatrix();
        
    }

}
