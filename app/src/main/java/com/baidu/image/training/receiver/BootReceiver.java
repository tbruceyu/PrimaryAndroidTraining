/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.receiver;

import com.baidu.image.training.service.BootService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            context.startService(new Intent(context,
                    BootService.class));
            Toast.makeText(context, "BootReceiver service has started!", Toast.LENGTH_LONG).show();
        }
    }
}