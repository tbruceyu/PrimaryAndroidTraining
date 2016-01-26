/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.image.training.adapter;

import java.util.ArrayList;

import com.baidu.image.training.R;
import com.baidu.image.training.model.UserModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yutao on 16/1/24.
 */
public class UserAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<UserModel> mUserList;

    public UserAdapter(Context context) {
        mContext = context;
    }
    public void setData(ArrayList<UserModel> userList) {
        mUserList = userList;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public UserModel getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_user_list, null);
            holder.nameText = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.portraitImage = (ImageView) convertView.findViewById(R.id.iv_portrait);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserModel userModel = getItem(position);
        holder.nameText.setText(userModel.name);
        ImageLoader.getInstance().displayImage(userModel.portrait, holder.portraitImage);
        return convertView;
    }

    private static class ViewHolder {
        TextView nameText;
        ImageView portraitImage;
    }
}
