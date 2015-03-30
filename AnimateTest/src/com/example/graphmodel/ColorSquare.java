package com.example.graphmodel;

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
        setColors(colors);
        
    }

    public void draw(GL10 gl){
        super.draw(gl);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
