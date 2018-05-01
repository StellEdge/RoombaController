package com.sjtu.cs.roombacontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import java.lang.Math;

import static java.lang.Math.PI;

public class MainActivity extends AppCompatActivity {///李桐：希望我们能弄个text输出一下当前速度和半径
    int widthPixels;//litong:屏幕尺寸
    int heightPixels;
    int speed, radius;
    //int lspeed, rspeed;

    Location mlocation = new Location();

    private void measure(){//这个函数用来获得屏幕尺寸
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.widthPixels = metrics.widthPixels;
        this.heightPixels = metrics.heightPixels;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("send", "You can text here");
        measure();//李桐：这一行我写的……
        // to tong 在连接完后可以用这个log测试一下鼠标移动输出指令的工作情况
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//李桐：触摸监听，更新mlocation中的坐标
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
                mlocation.update(event.getX(), event.getY());
                mlocation.update_condition(true);
                break;
            case MotionEvent.ACTION_UP:
                mlocation.update_condition(false);
        }

        calculate(mlocation);
        BluetoothSend(" ",CirSend(this.speed, this.radius));
        //李桐：这里是给蓝牙传输，tag我用了空字符串

        return true;
    }

    //李桐：下面是计算速度,对应圆盘操作模式,更新this.speed和this.radius
    private void calculate(Location mlocation){
        double x = mlocation.x() - (this.widthPixels/2);
        double y = mlocation.y() - (this.heightPixels/2);
        final double R = this.widthPixels/3;//R is the radius of the visible circle
        double a, r;

        if (!mlocation.conditon()){
            this.speed = 0;
            this.radius = 1;
        }else{
            r = Math.sqrt(x*x+y*y);
            a = -Math.signum(y)*Math.atan(x*1.0/y);

            if (r>R && r<this.widthPixels/2){//widthPixels is the max valiad radius
                this.speed = (int)Math.signum(y)*500;
            }else {
                if (r <= R) {
                    this.speed = (int) (Math.signum(y) * r * 500 / R);
                }else this.speed = 0;
            }
            this.radius =(int)(2000*a/(PI/300));
        }
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
        if(r == 0) {
            r = 1;
        }
        if (r>2000) {
            r = 32768;
        }
        if (r<-2000) {
            r = 32767;
        }
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
