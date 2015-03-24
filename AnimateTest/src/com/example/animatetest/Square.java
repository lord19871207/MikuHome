package com.example.animatetest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {

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
    }
  //4个顶点
    private float vertices[] ={
      -1.0f,1.0f,0.0f,    //左上
      -1.0f,-1.0f,0.0f,   //左下
      1.0f,-1.0f,0.0f,    //右上  
      1.0f,1.0f,0.0f      //右下
    };

    //几个顶点 标号 的连接顺序
    private short[] indices={
      0,1,2,0,2,3      
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
        //
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertivesBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_VERTEX_ARRAY);
    }
    
}
