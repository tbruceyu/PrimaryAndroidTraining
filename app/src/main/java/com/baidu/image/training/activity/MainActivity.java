/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.activity;

import com.baidu.image.training.R;
import com.baidu.image.training.actor.IActor;
import com.baidu.image.training.actor.LocalServiceActor;
import com.baidu.image.training.actor.RemoteServiceActor;
import com.baidu.image.training.util.ContactUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final int RESULT_CODE = 1000;
    Button mBindServiceButton;
    Button mBindRemoteServiceButton;
    RemoteServiceActor mRemoteServiceActor;
    LocalServiceActor mLocalServiceActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_to_second).setOnClickListener(this);
        mBindServiceButton = easyFindViewById(R.id.bt_bind_service);
        mBindServiceButton.setOnClickListener(this);
        mBindRemoteServiceButton = easyFindViewById(R.id.bt_bind_remote_service);
        mBindRemoteServiceButton.setOnClickListener(this);
        findViewById(R.id.bt_send_broadcast).setOnClickListener(this);
        findViewById(R.id.bt_start_network_request).setOnClickListener(this);
        findViewById(R.id.bt_intent_test).setOnClickListener(this);
        findViewById(R.id.bt_get_contact).setOnClickListener(this);
    }

    private void goToSecondActivity() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("data", "我是传递的数据");
        startActivityForResult(intent, RESULT_CODE);
    }

    private void doSendBroadcast() {
        Intent intent = new Intent("com.baidu.image.BootService.receiver");
        intent.putExtra("data", "hehe");
        sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (RESULT_CODE == requestCode && resultCode == RESULT_OK) {
            String data = intent.getStringExtra("retData");
            Toast.makeText(this, "返回的数据:" + data, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        IActor actor = null;
        switch (v.getId()) {
            case R.id.bt_to_second:
                goToSecondActivity();
                break;
            case R.id.bt_bind_service:
                if (mLocalServiceActor == null) {
                    mLocalServiceActor = new LocalServiceActor(this, mBindServiceButton);
                }
                actor = mLocalServiceActor;
                break;
            case R.id.bt_bind_remote_service:
                if (mRemoteServiceActor == null) {
                    mRemoteServiceActor = new RemoteServiceActor(this, mBindRemoteServiceButton);
                }
                actor = mRemoteServiceActor;
                break;
            case R.id.bt_send_broadcast:
                doSendBroadcast();
                break;
            case R.id.bt_start_network_request:
                startActivity(new Intent(MainActivity.this, NetworkRequestActivity.class));
                break;
            case R.id.bt_intent_test:
                Uri uri = Uri.parse("http://image.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                Intent intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("image/jpg");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                startActivity(intent);
                break;
            case R.id.bt_get_contact:
                ContactUtil.getPhoneContacts(this);
                break;
            default:
                // Do nothing
        }
        if (actor != null) {
            actor.act();
        }
    }
}
