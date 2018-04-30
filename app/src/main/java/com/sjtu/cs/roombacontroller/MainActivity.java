package com.sjtu.cs.roombacontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.MotionEvent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //李桐:主活动里添加了一个按钮，点了就切换模式（到另一个activity），同样另一个activity可回来，但另一个activity还没写……
        Button button = (Button) findViewById(R.id.change_module);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /////////////////////////////
            }
        });

    public boolean onTouch(View v, MotionEvent event) {
        // 李桐：这里是触摸监听处理
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
        }
        return true;
    }
   /* void BluetoothSend(String tag,String commandline){
        //直接调用这个函数来进行蓝牙数据发送
    }
    void BluetoothReceive(String tag,String commandline){
        //蓝牙数据接受状况未定，
    }*/
}
//注释