package com.sjtu.cs.roombacontroller;

import android.util.Log;

/**
 * Created by tong on 2018/4/30.
 */

public class Location {

    private float x, y;
    private boolean condition;

    public Location() {
    }

    public void update(float x, float y){
        this.x = x;
        this.y = y;
        Log.d("Touch", "update: "+x);
    }

    public void update_condition(boolean condition){
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
