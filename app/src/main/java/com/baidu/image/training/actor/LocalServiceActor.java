/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.actor;

import com.baidu.image.training.aidl.IRemoteCallback;
import com.baidu.image.training.service.BootService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by yutao on 16/1/24.
 */
public class LocalServiceActor implements IActor {
    public static final int ACT_CODE_BIND_SERVICE = 0;
    public static final int ACT_CODE_UNBIND_SERVICE = 1;
    private final Intent mServiceIntent;
    Context mContext;
    private BootService mBootService;
    private Button mBindServiceButton;
    private boolean mBinded;

    private IRemoteCallback mCallback = new IRemoteCallback.Stub() {
        @Override
        public void onCallback() throws RemoteException {
            Toast.makeText(mContext, "This message is in callback!!",
                    Toast.LENGTH_LONG).show();
        }
    };

    public LocalServiceActor(Context context, Button button) {
        mContext = context;
        mBindServiceButton = button;
        mServiceIntent = new Intent(mContext, BootService.class);
    }

    @Override
    public void act() {
        bindOrUnbindService();
    }

    private void bindOrUnbindService() {
        if (mBootService == null) {
            mContext.bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            mContext.unbindService(mConnection);
            mBootService = null;
            mBinded = false;
            mBindServiceButton.setText("Bind service");
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBootService = ((BootService.LocalBinder) service).getService();
            Toast.makeText(mContext, "Service connected",
                    Toast.LENGTH_SHORT).show();
            mBindServiceButton.setText("Unbind service");
            mBinded = true;
            mBootService.registerCallback(mCallback);
        }

        public void onServiceDisconnected(ComponentName className) {
            mBootService = null;
        }
    };
}
