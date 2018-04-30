package com.sjtu.cs.roombacontroller;

/**
 * Created by tong on 2018/4/30.
 */

public class location {
    private float x, y;
    private boolean condition;

    public void update(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void change_condition(boolean condition){
        this.condition = condition;
    }

    public boolean conditon(){
        return this.condition;
    }
    public float x(){
        return this.x;
    }

    public float y(){
        return this.y;
    }


}
