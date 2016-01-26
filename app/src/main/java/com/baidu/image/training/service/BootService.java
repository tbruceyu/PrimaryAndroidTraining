/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.service;

import com.baidu.image.training.aidl.IRemoteCallback;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Created by yutao on 16/1/23.
 */
public class BootService extends Service {
    IRemoteCallback mCallback;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            Toast.makeText(context, "Local service recieved broadcast, data:" + data, Toast.LENGTH_LONG).show();
            if (mCallback != null) {
                try {
                    mCallback.onCallback();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public class LocalBinder extends Binder {
        public BootService getService() {
            return BootService.this;
        }
    }

    public void registerCallback(IRemoteCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.baidu.image.BootService.receiver");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {

    }
}
