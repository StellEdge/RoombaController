package com.sjtu.cs.roombacontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("send", "You can text here");
        // to tong 在连接完后可以用这个log测试一下鼠标移动输出指令的工作情况
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

    // Created by hd on 2018/4/30.

    private String Hexa(int num) {
        if (num < 0)
        {
            return Integer.toHexString(num).substring(4);
        }
        String str = Integer.toHexString(num);
        while (str.length()<4)
        {
            str = "0" + str;
        }
        return str;
    }
    // 这个是我自己为了算数用的函数，真.private，不要调用

    private String CirSend(int v, int r){
        String str = "89" + Hexa(v) + Hexa(r);
        str = str.toUpperCase();
        return str;
    }
    //这个返回大写十六进制command，对应圆形UI界面

    private String LinSend(int right, int left){
        String str = "91" + Hexa(right) + Hexa(left);
        str = str.toUpperCase();
        return str;
    }
    //这个返回大写十六进制command，对应双轮条形UI界面

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
