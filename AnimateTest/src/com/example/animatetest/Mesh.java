package com.example.animatetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * 类描述：平面模型的基类
 * 
 * @ClassName: Mesh
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-14 下午7:06:59
 */
public class Mesh {

    /**
     * 构造方法描述：
     * 
     * @Title: Mesh
     * @date 2015-3-14 下午7:06:59
     */
    public Mesh() {
    }

    // 缓存顶点坐标
    private FloatBuffer verteicesbuffer = null;
    // 缓存顶点连接顺序
    private ShortBuffer indicesBuffer = null;
    // 背景颜色
    private float[] rgba = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    // 绘制区域颜色缓存
    private FloatBuffer colorBuffer = null;
    
    private FloatBuffer mTextureBUffer=null;
    
    private Bitmap bitmap;
    
    private int mTextureId=-1;
    
    private boolean isLoadTexture=false;
    
    // index的数量
    private int indexNum = -1;

    // 平移的坐标
    protected float x;
    protected float y;
    protected float z;

    // 旋转的角度
    public float rx;
    public float ry;
    public float rz;

    public  void draw(GL10 gl) {
        // 设置逆时针的面为前面
        gl.glFrontFace(GL10.GL_CCW);
        // 打开忽略背面的设置
        gl.glEnable(GL10.GL_CULL_FACE);
        // 设置忽略背面
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verteicesbuffer);
        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);

        if (colorBuffer != null) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, indicesBuffer);
        }
        
        if(isLoadTexture){
            loadGLTexture(gl);
            isLoadTexture=false;
        }
        
        if (mTextureId != -1 && mTextureBUffer != null) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            // Enable the texture state
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Point to our buffers
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBUffer);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        }
        
        gl.glTranslatef(x, y, z);

        /**
         * 从 坐标（0，0，0）即原点，引出一条线到（1,0,0）, 用右手握住这条线，这时，你会问，如何握？ 右手大拇指指向 （0，0，0）至（1，0，0）的方向 才握。 另外四个手指的弯曲指向 即是物体旋转方向。
         */
        gl.glRotatef(rx, 1, 0, 0);// 沿着x轴旋转
        gl.glRotatef(ry, 0, 1, 0);// 沿着y轴旋转
        gl.glRotatef(rz, 0, 0, 1);// 沿着z轴旋转
        gl.glDrawElements(GL10.GL_TRIANGLES, indexNum, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        if (mTextureId != -1 && mTextureBUffer != null) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
        gl.glDisableClientState(GL10.GL_CULL_FACE);
    }

    /**
     * 方法描述：
     * @author 尤洋
     * @Title: loadGLTexture
     * @param gl
     * @return void
     * @date 2015-3-15 下午10:52:59
     */
    private void loadGLTexture(GL10 gl) {
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
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    // 设置顶点
    protected void setVerteices(float[] verteices) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(verteices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verteicesbuffer =vbb.asFloatBuffer();
        verteicesbuffer.put(verteices);
        verteicesbuffer.position(0);
    }

    // 设置颜色
    protected void setColors(float[] colors) {
        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer=cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }
    // 设置顺序
    protected void setIndeices(short[] indices) {
        ByteBuffer ibb=ByteBuffer.allocateDirect(indices.length*2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer=ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        indexNum=indices.length;
    }

    // 设置背景颜色
    protected void setColor(float red, float green, float blue, float alpha) {
        rgba[0]=red;
        rgba[1]=green;
        rgba[2]=blue;
        rgba[3]=alpha;
    }
    
    public void loadBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
        isLoadTexture=true;
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
        mTextureBUffer = byteBuf.asFloatBuffer();
        mTextureBUffer.put(textureCoords);
        mTextureBUffer.position(0);
    }
    

}
