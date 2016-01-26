/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.baidu.image.training.R;
import com.baidu.image.training.adapter.UserAdapter;
import com.baidu.image.training.model.UserModel;
import com.baidu.image.training.util.JsonUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by yutao on 16/1/24.
 */
public class NetworkRequestActivity extends BaseActivity implements View.OnClickListener {
    private static final String mNetworkInterface = "http://172.24.40.150/training/test.json";
    private static final String mNetworkArrayInterface = "http://172.24.40.150/training/test_array.json";
    private static final int MSG_REQUEST_FAILED = -1;
    private static final int MSG_ENTITY_RETURNED = 0;
    private static final int MSG_LIST_RETURNED = 1;
    private TextView mContent;
    private ListView mListView;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ENTITY_RETURNED:
                    UserModel userModel = (UserModel) msg.obj;
                    mContent.setText("名字:" + userModel.name);
                    break;
                case MSG_LIST_RETURNED:
                    UserAdapter adapter = new UserAdapter(NetworkRequestActivity.this);
                    adapter.setData((ArrayList) msg.obj);
                    mListView.setAdapter(adapter);
                    break;
                case MSG_REQUEST_FAILED:
                    mContent.setText("请求失败");
                    break;
            }
        }
    };
    private Runnable mRequestEntityRunnable = new Runnable() {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            URL url = null;
            try {
                url = new URL(mNetworkInterface);
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                String output = outputStream.toString("UTF-8");
                inputStream.close();
                UserModel userModel = JsonUtil.ParseJsonObject(UserModel.class, output);
                Message message = mHandler.obtainMessage(MSG_ENTITY_RETURNED);
                message.obj = userModel;
                mHandler.sendMessage(message);
            } catch (Exception e) {
                Message message = mHandler.obtainMessage(MSG_REQUEST_FAILED);
                mHandler.sendMessage(message);
            }
        }
    };
    private Runnable mRequestListRunnable = new Runnable() {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            URL url = null;
            try {
                url = new URL(mNetworkArrayInterface);
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                String output = outputStream.toString("UTF-8");
                inputStream.close();
                List<UserModel> userModelList = JsonUtil.ParseJsonArray(UserModel.class, output);
                Message message = mHandler.obtainMessage(MSG_LIST_RETURNED);
                message.obj = userModelList;
                mHandler.sendMessage(message);
            } catch (Exception e) {
                Message message = mHandler.obtainMessage(MSG_REQUEST_FAILED);
                mHandler.sendMessage(message);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_request);
        mContent = easyFindViewById(R.id.tv_content);
        mListView = easyFindViewById(R.id.lv_user);
        findViewById(R.id.bt_request).setOnClickListener(this);
        findViewById(R.id.bt_request_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_request:
                new Thread(mRequestEntityRunnable).start();
                break;
            case R.id.bt_request_list:
                new Thread(mRequestListRunnable).start();
        }
    }
}
