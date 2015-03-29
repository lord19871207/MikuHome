package com.example.animatetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Square {
    private FloatBuffer mTextureBuffer; // New variable.
    private int mTextureId = -1; // New variable.
    private Bitmap textureBitmap;
    private boolean mShouldLoadTexture = false; // New variable.
    
    public Square() {
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        vertivesBuffer=vbb.asFloatBuffer();
        vertivesBuffer.put(vertices);
        vertivesBuffer.position(0);
        
        ByteBuffer ibb=ByteBuffer.allocateDirect(indices.length*2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer=ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        
        ByteBuffer byteBuf = ByteBuffer
                .allocateDirect(textureCoordinates.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureBuffer = byteBuf.asFloatBuffer();
        mTextureBuffer.put(textureCoordinates);
        mTextureBuffer.position(0);
    }
  //4个顶点
    private float vertices[] ={
      -1.0f,1.3f,0.0f,    //左上  0 
      -1.0f,-1.3f,0.0f,   //左下  1 
      1.0f,-1.3f,0.0f,    //右下   2
      1.0f,1.3f,0.0f      //右上   3
//            -0.5f, -0.5f, 0.0f,  // 0
//            0.5f, -0.5f, 0.0f,   // 1
//            -0.5f, 0.5f, 0.0f,    // 2
//            0.5f, 0.5f, 0.0f   // 3
    };

    //几个顶点 标号 的连接顺序
    private short[] indices={
      0,2,3 ,0,1,2, 
//      0, 1, 2, 1, 3, 2 
    };
    
    float textureCoordinates[] = { 
            0.0f, 0.0f, //
            0.0f, 1.0f, //
            1.0f, 1.0f, //
            1.0f, 0.0f, //
    };
    //顶点的缓存区
    protected FloatBuffer vertivesBuffer;
    //连线标号的缓存区
    private ShortBuffer indicesBuffer;
    

    
    
    /**
     * 绘制正方形
     * 方法描述：
     * @author 尤洋
     * @Title: draw
     * @param gl
     * @return void
     * @date 2015-2-3 下午8:13:26
     */
    public void draw(GL10 gl){
       //设置逆时针的面，为前面
        gl.glFrontFace(GL10.GL_CCW);
        //打开忽略后面的设置
        gl.glEnable(GL10.GL_CULL_FACE);
        //指出忽略背面
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertivesBuffer);
        if (mShouldLoadTexture) {
            loadGLTexture(gl);
            mShouldLoadTexture = false;
        }
        if (mTextureId != -1 && mTextureBuffer != null) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            // Enable the texture state
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Point to our buffers
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        }
        
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        if (mTextureId != -1 && mTextureBuffer != null) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_VERTEX_ARRAY);
    }
    
    /**
     * Set the bitmap to load into a texture.
     * 
     * @param bitmap
     */
    public void loadBitmap(Bitmap bitmap) { // New function.
        this.textureBitmap = bitmap;
        mShouldLoadTexture = true;
    }
    
    /**
     * Loads the texture.
     * 
     * @param gl
     */
    private void loadGLTexture(GL10 gl) { // New function
        // Generate one texture pointer...
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        mTextureId = textures[0];

        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        // Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);

        // Use the Android GLUtils to specify a two-dimensional texture image
        // from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);
    }
    
}
