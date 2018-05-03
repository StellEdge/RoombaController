package com.sjtu.cs.roombacontroller;

/**
 * Created by StellEdge on 2018/4/27.
 */
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Set;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//蓝牙是我们的施工地点--StellEdge。

public class BluetoothController{
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mAdapter;
    private BluetoothDevice roomba;
    private BluetoothSocket mSocket;
    private PrintWriter out;
    private BufferedReader in;

    public BluetoothController(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }/*构造函数*/
    public boolean isSupportBluetooth(){
        if(mAdapter!=null){
            return true;
        }
        return false;
    }/*蓝牙支持？*/

    public boolean isBluetoothEnabled(){
        if(mAdapter!=null){
            return mAdapter.isEnabled();
        }
        return false;
    }/* 蓝牙是否打开？*/
    /**
     * 打开蓝牙
     * @param activity 是一个AppCompatActivity对象
     * @param requestCode 作为onActivityResult()的返回值
     */
    public void turnOnBluetooth(AppCompatActivity activity,int requestCode){
        if(mAdapter!=null&&!mAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 关闭蓝牙
     *///
    public void turnOffBluetooth(){
        if(mAdapter!=null&&mAdapter.isEnabled()) {
            mAdapter.disable();
        }
    }
    /* 获取已经配对的设备,返回一个set集合*/
    public Set<BluetoothDevice> getConnetedDevices() {
        if (mAdapter != null && mAdapter.isEnabled()) {
            return mAdapter.getBondedDevices();
        }
        return null;
    }

    public boolean Connecttoroomba() {
        Set<BluetoothDevice> devices=getConnetedDevices();
        for (BluetoothDevice i : devices)
        {
            if (i.getName() == "HC-06");
                roomba = i;
        }
        if (roomba == null)
            return false;
        startClient();
        return true;
    }

    public void startClient() {
        if (roomba != null) {
            new Thread(new ConnectThread(roomba)).start();
        }
    }

    public boolean SendMsg(String msg){
        if (mSocket != null) {
            out.println(msg);
            out.flush();
            return true;
        }return false;
    }

    private class ConnectThread extends Thread{//单独的连接线程
        private BluetoothDevice mDevice;
        public ConnectThread(BluetoothDevice device){
            BluetoothSocket temp = null;
            mDevice = device;
            try {
                temp = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = temp;
        }

        @Override
        public void run() {
            super.run();
            mAdapter.cancelDiscovery();//取消当前搜索
            try {
                //通过socket连接设备，这是一个阻塞操作，知道连接成功或发生异常
                mSocket.connect();
            } catch (IOException e) {
                //无法连接，关闭socket并且退出
                try {
                    mSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (mSocket != null) {
                try {

                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream())), true);
                    out.println("ROOMBA! OPEN FIRE!");
                    out.flush();

                    in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    String str = in.readLine();
                } catch (Exception e) {
                }
            }
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) { }
        }/* 取消正在进行的链接，关闭socket */
    }
}

//hello world