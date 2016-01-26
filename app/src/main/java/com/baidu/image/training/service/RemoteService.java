/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.service;

import com.baidu.image.training.aidl.IRemoteCall;
import com.baidu.image.training.aidl.IRemoteCallback;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class RemoteService extends Service {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            Toast.makeText(context, "Remote service recieved broadcast, data:" + data, Toast.LENGTH_LONG).show();
            if (mCallback != null) {
                try {
                    mCallback.onCallback();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    IRemoteCallback mCallback;

    public final IRemoteCall.Stub mBinder = new IRemoteCall.Stub() {
        @Override
        public void foo() throws RemoteException {
            Toast.makeText(RemoteService.this, "service call foo!", Toast.LENGTH_SHORT).show();
            if (mCallback != null) {
                mCallback.onCallback();
            }
        }

        @Override
        public void registerCallback(IRemoteCallback cb) throws RemoteException {
            mCallback = cb;
        }

        @Override
        public void unregisterCallback(IRemoteCallback cb) throws RemoteException {
            mCallback = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.baidu.image.RemoteService.receiver");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}