package com.example.animatetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ColorSquare extends Square{
    FloatBuffer colorBuffer;
    float colors[] ={
         1f,0f,0f,1f,
         0f,1f,0f,1f,
         0f,0f,1f,1f,
         1f,0f,1f,1f
    };
    public ColorSquare() {
        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer=cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
        
    }

    public void draw(GL10 gl){
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, super.vertivesBuffer);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
       // gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);
        super.draw(gl);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
