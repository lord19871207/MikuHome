package com.example.graphmodel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * 类描述：
 * 
 * @Package com.example.graphmodel
 * @ClassName: Cube
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-4-1 上午10:33:43
 */
public class Cube {
    private float width = 0.35f;// 宽度 x轴
    private float height = 0.5f; // 高度 y轴
    private float deepth = 0.1f; // 深度 z轴
    private boolean isLoadTexture = false;
    // 背景颜色
    private float[] rgba = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    private FloatBuffer mTextureBUffer = null;
    // 缓存顶点坐标
    private FloatBuffer verteicesbuffer = null;
    // 缓存顶点连接顺序
    private ShortBuffer indicesBuffer = null;
    private FloatBuffer colorBuffer = null;

    private Bitmap bitmap;
    private int mTextureId = -1;
    float box[] = new float[] {
            // FRONT
            -width, -height, deepth,
            width, -height, deepth,
            -width, height, deepth,
            width, height, deepth,
            // BACK
            -width, -height, -deepth,
            -width, height, -deepth,
            width, -height, -deepth,
            width, height, -deepth,
            // LEFT
            -width, -height, deepth,
            -width, height, deepth,
            -width, -height, -deepth,
            -width, height, -deepth,
            // RIGHT
            width, -height, -deepth,
            width, height, -deepth,
            width, -height, deepth,
            width, height, deepth,
            // TOP
            -width, height, deepth,
            width, height, deepth,
            -width, height, -deepth,
            width, height, -deepth,
            // BOTTOM
            -width, -height, deepth,
            -width, -height, -deepth,
            width, -height, deepth,
            width, -height, -deepth,
    };

    float lightAmbient[] = new float[] { 0.5f, 0.5f, 0.6f, 1.0f };  // 环境光
    float lightDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };// 漫反射光
    float[] lightPos = new float[] { 0, 0, 3, 1 };  // 光源位置
    /*
     * 因为进行光照处理，你必须告知系统你定义的模型各个面的方向，以便系统计算光影情况，方向的描述是通过向量点来描述的
     */
    float norms[] = new float[] { // 法向量数组，用于描述个顶点的方向，以此说明各个面的方向
    // FRONT
            0f, 0f, 1f, // 方向为(0,0,0)至(0,0,1)即Z轴正方向
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            // BACK
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            0f, 0f, -1f,
            // LEFT
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            -1f, 0f, 0f,
            // RIGHT
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            // TOP
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            0f, 1f, 0f,
            // BOTTOM
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f
    };

    float texCoords[] = new float[] { // 纹理坐标对应数组
    // FRONT
            0.0f, 0.7f,
            0.5f, 0.7f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            // BACK
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            // LEFT
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // RIGHT
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // TOP
            0.0f, 0.0f,
            0.6f, 0.0f,
            0.0f, 1.5f,
            0.6f, 1.5f,
            // BOTTOM
            0.6f, 0.0f,
            0.6f, 1.5f,
            0.0f, 0.0f,
            0.0f, 1.5f
    };
    private FloatBuffer normalBuffer;

    /**
     * 构造方法描述：
     * 
     * @Title: Cube
     * @date 2015-4-1 上午10:33:43
     */
    public Cube() {
        setVerteices(box);
        // setIndeices(indices);
        setNormalFloatBuffer(norms);
        setTextureCoordinates(texCoords);
    }

    /**
     * 方法描述：加载纹理
     * 
     * @author 尤洋
     * @Title: loadTexture
     * @return void
     * @date 2015-4-1 下午12:13:43
     */
    private void loadTexture(GL10 gl) {
        // 使用纹理步骤：
        // 1.开启贴图
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // 2.生成纹理ID
        int[] tmp_tex = new int[1];// 尽管只有一个纹理，但使用一个元素的数组
        // glGenTextures(申请个数，存放数组，偏移值）
        gl.glGenTextures(1, tmp_tex, 0); // 向系统申请可用的，用于标示纹理的ID
        mTextureId = tmp_tex[0];

        // 3.绑定纹理，使得指定纹理处于活动状态。一次只能激活一个纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        // 4.绑定纹理数据，传入指定图片
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // 5.传递各个顶点对应的纹理坐标
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBUffer);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // 开启纹理坐标数组

        // 6.设置纹理参数 （可选）
        /*
         * 下面的两行参数告诉OpenGL在显示图像时，当它比放大得原始的纹理大( GL_TEXTURE_MAG_FILTER )或缩小得比原始得纹理小( GL_TEXTURE_MIN_FILTER )
         * 时OpenGL采用的滤波方式。通常这两种情况下我都采用 GL_LINEAR 。这使得纹理从很远处到离屏幕很近时都平滑显示。使用 GL_LINEAR 需要CPU和显卡做更多的运算。如果您的机器很慢，您也许应该采用
         * GL_NEAREST 。过滤的纹理在放大的时候，看起来马赛克的很。您也可以结合这两种滤波方式。在近处时使用 GL_LINEAR ，远处时 GL_NEAREST 。
         */
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);

    }

    public void loadBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        isLoadTexture = true;
    }

    public void draw(GL10 gl) {
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
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        }

        if (isLoadTexture) {
            loadTexture(gl);
            isLoadTexture = false;
        }

        if (mTextureId != -1 && mTextureBUffer != null) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            // Enable the texture state
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Point to our buffers
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBUffer);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        }

        /**
         * 从 坐标（0，0，0）即原点，引出一条线到（1,0,0）, 用右手握住这条线，这时，你会问，如何握？ 右手大拇指指向 （0，0，0）至（1，0，0）的方向 才握。 另外四个手指的弯曲指向 即是物体旋转方向。
         */
        gl.glColor4f(1.0f, 0, 0, 1.0f);   // 设置颜色，红色
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);  // 绘制正方型FRONT面
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);

        gl.glColor4f(0, 1.0f, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);

        gl.glColor4f(0, 0, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        if (mTextureId != -1 && mTextureBUffer != null) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
        gl.glDisableClientState(GL10.GL_CULL_FACE);
    }

    // 设置顶点
    protected void setVerteices(float[] verteices) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(verteices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verteicesbuffer = vbb.asFloatBuffer();
        verteicesbuffer.put(verteices);
        verteicesbuffer.position(0);
    }

    // 设置颜色
    protected void setColors(float[] colors) {
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    // 设置顺序
    protected void setIndeices(short[] indices) {
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
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

    public void setNormalFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);// 分配缓冲空间，一个float占4个字节
        bb.order(ByteOrder.nativeOrder()); // 设置字节顺序， 其中ByteOrder.nativeOrder()是获取本机字节顺序
        normalBuffer = bb.asFloatBuffer();
        normalBuffer.put(arr);        // 添加数据
        normalBuffer.position(0);      // 设置数组的起始位置
    }
}
