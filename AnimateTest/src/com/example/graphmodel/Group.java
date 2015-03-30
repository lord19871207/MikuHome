package com.example.graphmodel;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * 类描述：多个平面模型的组合  的基类
 * @ClassName: Group
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-15 下午9:44:47
 */
public class Group extends Mesh {

    private Vector<Mesh> children=new Vector<Mesh>();
    /**
     * 构造方法描述：
     * @Title: Group
     * @date 2015-3-15 下午9:44:47
     */
    public Group() {
    }
    
    @Override
    public void draw(GL10 gl){
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(gl);
        }
    }
    
    public void add (int position, Mesh object){
        children.add(position,object);
    }
    
    public boolean add(Mesh object){
        return children.add(object);
        
    }
    
    public void clear(){
        children.clear();
    }
    
    public Mesh getChild(int location){
        return children.get(location);
    }
    
    public Mesh removeChild(int location){
        return children.remove(location);
    }
    
    public boolean rmoveChildren(Mesh object){
        return children.remove(object);
    }
    
    public int size(){
        return children.size();
    }
}
