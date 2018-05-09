package com.sjtu.cs.roombacontroller;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

//import java.awt.Button;
//import java.awt.Shape;
import java.lang.Math;
import java.lang.Byte;
/*
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import app.akexorcist.bluetotohspp.library.BluetoothService;
*/
import static java.lang.Math.PI;

public class MainActivity extends AppCompatActivity {///李桐：希望我们能弄个text输出一下当前速度和半径
    int widthPixels;//litong:屏幕尺寸
    int heightPixels;
    int speed, radius;

    Location mlocation = new Location();

    //private BluetoothController BTC = new BluetoothController(this);
    private BluetoothSPP bt = new BluetoothSPP(this); //暂时先用着这个外部库吧
    private boolean BTavailable;
    private String BT="Bluetooth";
    private Context mContext=MainActivity.this;
    private String TAG="Main Activity";
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
        if(!bt.isBluetoothAvailable()) {
            BTavailable=false;
            Log.d(BT, "onCreate: NO BLUETOOTH SUPPORT");
            // any command for bluetooth is not available
        } else {
            BTavailable=true;
            Log.d(BT, "onCreate: Has Bluetooth");
            bt.setupService();
            //bt.startService(BluetoothState.DEVICE_ANDROID);这里用的是HC-06
            bt.startService(BluetoothState.DEVICE_OTHER);
            Intent intent = new Intent(mContext, DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            if(!bt.isBluetoothEnabled()) {
                // Do somthing if bluetooth is disable
            } else {
                // Do something if bluetooth is already enable
            }
        }
        //文本接收
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // Do something when data incoming
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(mContext, Main2Activity.class);//开启下一项活动
                startActivity(intent);
                //Shape circle = (Shape) findViewById(R.id.circle);
                //v.setVisibility(0);
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSend("", "90 7F 7F 00");
                }
        });
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSend("", "90 00 00 00");
            }
        });
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSend("", "90 7F 7F 40");
            }
        });
        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSend("", "90 7F 7F 19");
            }
        });
        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSend("", "90 7F 7F 00");
            }
        });
        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSend("", "90 7F 7F 7F");
            }
        });
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt.isBluetoothAvailable()){
                    BluetoothSend("","80 83");
                    Intent intent = new Intent(MainActivity.this, DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                } else {
                    Log.d(BT, "onClick: NO BLUETOOTH SUPPORT");
                }
            }
        });
        measure();
        //BTC.start(); 旧东西统统爆破了
        BluetoothSend("","80 83");//李桐：这一行我写的……
        // to tong 在连接完后可以用这个log测试一下鼠标移动输出指令的工作情况
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) { //判断数据是否为空
            return;
        }

        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == RESULT_OK){
                String address = data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
                bt.connect(address);
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == RESULT_OK) {
                bt.setupService();
                //bt.startService(BluetoothState.DEVICE_ANDROID);这里用的是HC-06
                bt.startService(BluetoothState.DEVICE_OTHER);
                //setup();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
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
    /**
     * Called when the user touches the button
     */


        calculate(mlocation);
        BluetoothSend(" ",CirSend(this.speed, this.radius));
        //李桐：这里是给蓝牙传输，tag我用了空字符串
        return true;
    }

    //李桐：下面是计算速度,对应圆盘操作模式,更新this.speed和this.radius
    private void calculate(Location mlocation){
        double x = mlocation.x() - (this.widthPixels/2);
        double y = (this.heightPixels/2) - mlocation.y();
        final double R = this.widthPixels/3;//R is the radius of the visible circle
        double a, r;

        if (!mlocation.conditon()){
            this.speed = 0;
            this.radius = 10000;
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
            this.radius =(int)(2000*a/(PI/3));
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
        String Hexstrv=Hexa(v);
        String Hexstrr=Hexa(r);
        String str = "89 " + Hexstrv.substring(0,2)+" "+Hexstrv.substring(2)
                +" "+Hexstrr.substring(0,2)+" "+Hexstrr.substring(2);

        str = str.toUpperCase();
        return str;
    }
    //这个返回大写十六进制command，对应圆形UI界面

    private String LinSend(int right, int left){
        String Hexstrl=Hexa(left);
        String Hexstrr=Hexa(right);
        String str = "91" +" "+Hexstrr.substring(0,2)+" "+Hexstrr.substring(2)
                +" "+Hexstrl.substring(0,2)+" "+Hexstrl.substring(2);
        str = str.toUpperCase();
        return str;
    }
    //这个返回大写十六进制command，对应双轮条形UI界面


    void BluetoothSend(String tag,String commandline){
        //tag目前就是多留个接口
        //直接调用这个函数来进行蓝牙数据发送
        //If you want to send any data. boolean parameter is mean that data will send with ending by LF and CR or not.
        //If yes your data will added by LF & CR 末尾添加回车或换行
        Log.d(TAG, "BluetoothSend: "+commandline);
        byte[] myb=HexCommandtoByte(commandline.getBytes());
        if (bt.isServiceAvailable()) {
            bt.send(myb, false);
        }else{
            Log.d(TAG, "BluetoothSend:No bluetooth connection.");
        }
    }
    public static byte[] HexCommandtoByte(byte[] data) {
        if (data == null) {
            return null;
        }
        int nLength = data.length;

        String strTemString = new String(data, 0, nLength);
        String[] strings = strTemString.split(" ");
        nLength = strings.length;
        data = new byte[nLength];
        for (int i = 0; i < nLength; i++) {
            if (strings[i].length() != 2) {
                data[i] = 00;
                continue;
            }
            try {
                data[i] = (byte)Integer.parseInt(strings[i], 16);
            } catch (Exception e) {
                data[i] = 00;
                continue;
            }
        }

        return data;
    }
}
