package com.example.graphmodel;

import javax.microedition.khronos.opengles.GL10;

public class Square extends Mesh{
    public Square() {
        setVerteices(vertices);
        setIndeices(indices);
        setTextureCoordinates(textureCoordinates);
    }
  //4个顶点
    private float vertices[] ={
      -1.0f,1.3f,0.0f,    //左上  0 
      -1.0f,-1.3f,0.0f,   //左下  1 
      1.0f,-1.3f,0.0f,    //右下   2
      1.0f,1.3f,0.0f      //右上   3
    };

    //几个顶点 标号 的连接顺序
    private short[] indices={
      0,2,3 ,0,1,2, 
    };
    
    float textureCoordinates[] = { 
            0.0f, 0.0f, //        
            0.0f, 1.0f, //
            1.0f, 1.0f, //
            1.0f, 0.0f, //
    };
    
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
        super.draw(gl);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_VERTEX_ARRAY);
    }
    
}
