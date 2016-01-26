/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

/**
 * Created by yutao on 15/10/9.
 */
public class ContactUtil {
    private static final String TAG = "ContactUtil";

    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID, Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 从手机获取通讯录
     *
     * @param encrypt 是否加密
     *
     * @return 通讯录列表
     */
    public static List<String> getPhoneContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        List<String> contactList = new ArrayList<>();
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                String phoneName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                Log.d("yutao", "name:" + phoneName + " number:" + phoneNumber);
            }

            phoneCursor.close();
        }
        return contactList;
    }
}