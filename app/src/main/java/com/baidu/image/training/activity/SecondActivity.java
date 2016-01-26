/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.activity;

import com.baidu.image.training.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yutao on 16/1/23.
 */
public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        TextView textView = easyFindViewById(R.id.tv_content);
        textView.setText("Passed data is:" + data);

        findViewById(R.id.bt_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
                intent.putExtra("retData", "I'm the returned data");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Log.d("yutao", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("yutao", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("yutao", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("yutao", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("yutao", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("yutao", "onDestroy");
    }
}
