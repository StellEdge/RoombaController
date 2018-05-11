package com.sjtu.cs.roombacontroller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import android.view.Window;
//import java.awt.Button;
//import java.awt.Shape;

public class MainActivity extends AppCompatActivity {///李桐：希望我们能弄个text输出一下当前速度和半径
    int widthPixels;//litong:屏幕尺寸
    int heightPixels;
    int speed=0, radius=66666;
    String temp;
    PaintBoard paintBoard = new PaintBoard(this);
    Canvas canvas = new Canvas();
    int cx=this.widthPixels/2, cy = 500;

    Location mlocation = new Location();

    private BluetoothSPP bt = new BluetoothSPP(this);
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
    private TextView teller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!bt.isBluetoothAvailable()) {
            BTavailable=false;
            //Log.d(BT, "onCreate: NO BLUETOOTH SUPPORT");
        } else {
            BTavailable=true;
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);
            if(!bt.isBluetoothEnabled()) {
                bt.enable();
            }
        }
        //文本接收
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // Do something when data incoming
            }
        });
        teller = (TextView)findViewById(R.id.teller);
        teller.setText("temp");
        Button button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                //Intent intent = new Intent(mContext, Main2Activity.class);//开启下一项活动
                //startActivity(intent);
                //Shape circle = (Shape) findViewById(R.id.circle);
                //v.setVisibility(0);
                BluetoothSend("","80 83");
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
                BluetoothSend("", "90 7F 00 00");
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

        PaintBoard newView = new PaintBoard(this);
        setContentView(newView);

        /*private Handler handler = new Handler(){
            public void handlieMessage(Message msg){
                teller.setText(temp);
            }
        }*/

       /* Handler handler = new Handler(){
            public void handlerMessage(Message msg){
                switch (msg.what){
                    case 1:
                        String temp = "speed:"+speed+", radius:"+radius;
                        teller.setText(temp);
                        break;
                    default:
                        break;

                }
            }
        };*/
    }

    private Handler mhandler = new Handler(){
        public void handleMessage(Message msg){
            teller.setText((String)msg.obj);

            paintBoard.draw(canvas);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
    };

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
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mlocation.update(event.getX(), event.getY());
                mlocation.update_condition(true);
                break;
            case MotionEvent.ACTION_UP:
                mlocation.update_condition(false);
                break;
            default:
                break;
        }
    /**
     * Called when the user touches the button
     */

        calculate(mlocation);
        BluetoothSend(" ",CirSend(this.speed, this.radius));
        //李桐：这里是给蓝牙传输，tag我用了空字符串
        return true;
    }

    //litong:下面是摇杆的重新绘制函数
    public class PaintBoard extends View {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.axis);//LITONG:yaogan
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth= bitmap.getWidth();

        public PaintBoard(MainActivity mainActivity) {
            super(mainActivity);
        }
        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            canvas.drawBitmap(bitmap,cx-bitmapWidth, cy-bitmapHeight, paint);
        }
    }


    //李桐：下面是计算速度,对应圆盘操作模式,更新this.speed和this.radius
    private void calculate(Location mlocation){
        double centerx = 500;
        double centery = this.heightPixels/2;
        double x = mlocation.x() - centerx;
        double y = mlocation.y() - centery;
        final double R = 350, k = 1.5;//R is the radius of the visible circle
        double a, r;
        final double b = 1.0,  i = Math.PI*8.0/18;//i is the top angle
        double tmp;
        if (!mlocation.conditon()){
            this.speed = 0;
            this.radius = 10000;
            this.cx =  this.heightPixels/2;
            this.cy = 500;

        }else{
            r = Math.sqrt(x*x+y*y);
            a = Math.atan(Math.abs(y*1.0/x));

            if (r>R && r<k*R){//k*R is the max valiad radius
                this.speed = (int)Math.signum(-y)*500;
                this.cx = (int)(R*Math.cos(a)*Math.signum(x)+centerx);
                this.cy = (int)(R*Math.sin(a)*Math.signum(y)+centery);
            }else {
                if (r <= R) {
                    this.speed = (int) (Math.signum(-y) * r * 500 / R);
                    this.cx = (int)mlocation.x();
                    this.cy = (int)mlocation.y();
                }else this.speed = 0;
            }

            if (a>i) this.radius = 10000;
            else {
                tmp = (Math.exp(b*a)-1.0)/(Math.exp(b*i)-1.0);
                this.radius = (int)(2000*tmp*(Math.signum(-x)));
            }
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
    //StellEdge：不存在的
    void BluetoothSend(String tag,String commandline){
        //tag目前就是多留个接口
        //直接调用这个函数来进行蓝牙数据发送
        //If you want to send any data. boolean parameter is mean that data will send with ending by LF and CR or not.
        //If yes your data will added by LF & CR 末尾添加回车或换行
        //Log.d(TAG, "BluetoothSend: "+commandline);
        byte[] myb=HexCommandtoByte(commandline.getBytes());
        if (bt.isServiceAvailable()) {
            bt.send(myb, false);
        }else{
            Log.d(TAG, "BluetoothSend:No bluetooth connection.");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (radius >= 2000) {
                    temp = "speed:" + speed + ", radius:" + "66666";
                }
                else{
                    temp = "speed:" + speed + ", radius:" + radius;}
                Log.d("hello", temp);
                mhandler.sendMessage(mhandler.obtainMessage(0,temp));
                /*try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();
    }
    //StellEdge:16进制byte字符串转byte编码
    public static byte[] HexCommandtoByte(byte[] data) {
        if (data == null) {
            return null;
        }
        int nLength = data.length;
        String TemString = new String(data, 0, nLength);
        String[] strings = TemString.split(" ");
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
