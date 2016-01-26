/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.actor;

import com.baidu.image.training.aidl.IRemoteCall;
import com.baidu.image.training.aidl.IRemoteCallback;
import com.baidu.image.training.service.RemoteService;

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
public class RemoteServiceActor implements IActor {
    private final Intent mServiceIntent;
    Context mContext;
    private IRemoteCall mRemoteCall;
    private Button mBindServiceButton;
    private boolean mBinded;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mRemoteCall = IRemoteCall.Stub.asInterface(service);
            Toast.makeText(mContext, "Remote Service connected",
                    Toast.LENGTH_SHORT).show();
            mBindServiceButton.setText("Unbind service");
            mBinded = true;
            try {
                mRemoteCall.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mRemoteCall = null;
        }
    };
    private IRemoteCallback mCallback = new IRemoteCallback.Stub() {
        @Override
        public void onCallback() throws RemoteException {
            mBindServiceButton.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "This message is in callback!!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    public RemoteServiceActor(Context context, Button button) {
        mContext = context;
        mBindServiceButton = button;
        mServiceIntent = new Intent(mContext, RemoteService.class);
    }

    @Override
    public void act() {
        bindOrUnbindService();
    }

    private void bindOrUnbindService() {
        if (mRemoteCall == null && mBinded == false) {
            mContext.bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            mContext.unbindService(mConnection);
            mRemoteCall = null;
            mBinded = false;
            mBindServiceButton.setText("Bind remote");
        }
    }

}
