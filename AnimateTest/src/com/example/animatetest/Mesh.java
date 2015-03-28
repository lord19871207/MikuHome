package com.example.animatetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * 类描述：
 * 
 * @Package com.example.animatetest
 * @ClassName: Mesh
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-10 下午8:18:06
 */
public abstract class Mesh
{
    private Bitmap mBitmap;
    private int mTextureId = -1;
    protected int WIDTH = 40;
    protected int HEIGHT = 40;
    protected int mBmpWidth = -1;
    protected int mBmpHeight = -1;
    protected final float[] mVerts;
    // Indicates if we need to load the texture.
    private boolean mShouldLoadTexture = false; // New variable.
    // Our UV texture buffer.
    private FloatBuffer mTextureBuffer; // New variable.

    public Mesh(int width, int height)
    {
        WIDTH = width;
        HEIGHT = height;
        mVerts = new float[(WIDTH + 1) * (HEIGHT + 1) * 2];
    }

    public float[] getVertices()
    {
        return mVerts;
    }

    public int getWidth()
    {
        return WIDTH;
    }

    public int getHeight()
    {
        return HEIGHT;
    }

    public static void setXY(float[] array, int index, float x, float y)
    {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    public void setBitmapSize(int w, int h)
    {
        mBmpWidth = w;
        mBmpHeight = h;
    }

    public abstract void buildPaths(float endX, float endY);

    public abstract void buildMeshes(int index);

    public void buildMeshes(float w, float h)
    {
        int index = 0;
        for (int y = 0; y <= HEIGHT; ++y)
        {
            float fy = y * h / HEIGHT;
            for (int x = 0; x <= WIDTH; ++x)
            {
                float fx = x * w / WIDTH;
                setXY(mVerts, index, fx, fy);
                index += 1;
            }
        }
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
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
    }

    /**
     * Set the bitmap to load into a texture.
     * 
     * @param bitmap
     */
    public void loadBitmap(Bitmap bitmap) { // New function.
        this.mBitmap = bitmap;
        mShouldLoadTexture = true;
    }

    public void draw(GL10 gl) {
        // New part...
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
        // ... end new part.

    }

    /**
     * Set the texture coordinates.
     * 
     * @param textureCoords
     */
    protected void setTextureCoordinates(float[] textureCoords) { // New
                                                                  // function.
        // float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer byteBuf = ByteBuffer
                .allocateDirect(textureCoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureBuffer = byteBuf.asFloatBuffer();
        mTextureBuffer.put(textureCoords);
        mTextureBuffer.position(0);
    }
}
