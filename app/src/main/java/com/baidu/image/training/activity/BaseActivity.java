/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.activity;

import android.app.Activity;

/**
 * Created by yutao on 16/1/24.
 */
public class BaseActivity extends Activity {
    protected <T> T easyFindViewById(int id) {
        return (T) super.findViewById(id);
    }
}
