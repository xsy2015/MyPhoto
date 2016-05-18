/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xsy.photo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xsy.photo.R;
import com.xsy.photo.utils.MyUtils;

import java.util.HashSet;
import java.util.List;


import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/1 下午8:42
 */
public class ChoosePhotoListAdapter extends BaseAdapter {
    private List<PhotoInfo> mList;
    private LayoutInflater mInflater;
    private int mScreenWidth;
    private View.OnClickListener mOnClickListener;

    public ChoosePhotoListAdapter(Activity activity, List<PhotoInfo> list,View.OnClickListener onClickListener) {
        this.mList = list;
        this.mInflater = LayoutInflater.from(activity);
        this.mScreenWidth = DeviceUtils.getScreenPix(activity).widthPixels;
        this.mOnClickListener=onClickListener;
    }

    @Override
    public int getCount() {

            return mList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_grid_view, null);
            holder.mImageView=(ImageView)convertView.findViewById(R.id.iv);
            convertView.setTag(holder);

        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        //设置数据
        //setHeight(holder.mImageView);

     if (parent.getChildCount()==position){
        if (position<mList.size()){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.ic_gf_default_photo)
                    .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                    .showImageOnLoading(R.drawable.ic_gf_default_photo).build();

            holder.mImageView.setOnClickListener(null);

            // ImageView ivPhoto = (ImageView) mInflater.inflate(R.layout.item_grid_view, null);
            PhotoInfo photoInfo = mList.get(position);
            ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), holder.mImageView, options);
            System.out.println("---------"+position+"-------");
        }else{

            holder.mImageView.setOnClickListener(mOnClickListener);
            holder.mImageView.setImageResource(R.drawable.icon_addpic_unfocused);
            if (position==8){
                holder.mImageView.setVisibility(View.GONE);
            }
            System.out.println("**********" + position + "-------");
        }
     }
      /*  if (position==mList.size()){

            holder.mImageView.setOnClickListener(mOnClickListener);
        }else {
            holder.mImageView.setOnClickListener(null);
        }*/
        System.out.println("@@@@@@" + mList.size() + "@@@@@");
        return convertView;
    }

    private void setHeight(final View convertView) {

        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new LinearLayout.LayoutParams(height, height));
    }

    public class ViewHolder{
        private ImageView mImageView;
    }


}
