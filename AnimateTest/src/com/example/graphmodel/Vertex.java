package com.example.graphmodel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Vertex {
    private float[] vertexArray =new float[]{
            -0.8f,-0.4f*  1.732f,0.0f,   
            0.8f,-0.4f*  1.732f,0.0f,
            0.0f,0.4f*  1.732f,0.0f,
            0.4f,0.4f*1.732f,0.0f
          };
    
    private short[] indiceArray=new short[]{
            0,1,2,0,2,3
    };
    private float x,y,z;
    private FloatBuffer fbf;

    private ShortBuffer iff;
    
    public Vertex(float x,float y,float z) {
        ByteBuffer bbf=ByteBuffer.allocateDirect(vertexArray.length*4);
        bbf.order(ByteOrder.nativeOrder());
        fbf = bbf.asFloatBuffer();
        fbf.put(vertexArray);
        fbf.position(0);
        this.x=x;
        this.y=y;
        this.z=z;
        ByteBuffer ibb=ByteBuffer.allocateDirect(indiceArray.length*2);
        ibb.order(ByteOrder.nativeOrder());
        iff = ibb.asShortBuffer();
        iff.put(indiceArray);
        iff.position(0);
    }
    
    public void draw(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);  //逆时针为前部
        gl.glEnable(GL10.GL_CULL_FACE);//忽略背部的绘制
        gl.glCullFace(GL10.GL_BACK);  //明确指出忽略背面
        
        gl.glColor4f(0.0f, 0.0f, 0.7f, 0.5f);
        gl.glPointSize(8f);
        gl.glLoadIdentity();
       // gl.glTranslatef(0, 0.6f, -4);
        gl.glTranslatef(x, y, z);        
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, fbf);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
    

}
