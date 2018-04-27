package com.sjtu.cs.roombacontroller;

/**
 * Created by StellEdge on 2018/4/27.
 */
import android.support.v7.app.AppCompatActivity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

//蓝牙是我们的施工地点--StellEdge：下面代码框架来自网络，仅供测试。

public class BluetoothController{
    private BluetoothAdapter mAdapter;
    /**做成Service？
    public static final String TAG = "BluetoothController";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "in onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "in onStartCommand");
        Log.w(TAG, "MyService:" + this);
        String name = intent.getStringExtra("name");
        Log.w(TAG, "name:" + name);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "in onDestroy");
    }
    public BluetoothController(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

     * 判断当前设备是否支持蓝牙
     * @return
     */
    public boolean isSupportBluetooth(){
        if(mAdapter!=null){
            return true;
        }
        return false;
    }

    /**
     * 获取蓝牙的状态
     * @return
     */
    public boolean getBluetoothStatus(){
        if(mAdapter!=null){
            return mAdapter.isEnabled();
        }
        return false;
    }

    /**
     * 打开蓝牙
     * @param activity
     * @param requestCode
     */
    public void turnOnBluetooth(AppCompatActivity activity,int requestCode){
        if(mAdapter!=null&&!mAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 关闭蓝牙
     */
    public void turnOffBluetooth(){
        if(mAdapter!=null&&mAdapter.isEnabled()) {
            mAdapter.disable();
        }
    }
}
