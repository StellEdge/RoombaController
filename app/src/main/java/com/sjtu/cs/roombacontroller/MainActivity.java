package com.sjtu.cs.roombacontroller;

import android.location.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onTouchEvent(View view, MotionEvent event) {//李桐：就这样了这块儿
        Location mlocation = new Location();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
                mlocation.update(event.getX(), event.getY());
                mlocation.update_condition(true);
                break;
            case MotionEvent.ACTION_UP:
                mlocation.update_condition(false);
        }
        return true;
    }
    private BluetoothController BTC;
    void BluetoothSend(String tag,String commandline){
        BTC.SendMsg(commandline);
        //tag目前就是多留个接口
        //直接调用这个函数来进行蓝牙数据发送
    }
    void BluetoothReceive(String tag,String commandline){
        //蓝牙数据接受状况未定，
    }
}