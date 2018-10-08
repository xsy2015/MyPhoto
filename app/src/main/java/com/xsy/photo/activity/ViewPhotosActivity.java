package com.xsy.photo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xsy.photo.R;
import com.xsy.photo.adapter.ViewNetworkPhotoAdapter;
import com.xsy.photo.adapter.ViewPhotoAdapter;

import java.util.ArrayList;

/**
 * Created by ${xsy} on 2016/4/12.
 */
public class ViewPhotosActivity extends Activity implements ViewPager.OnPageChangeListener{

    private ViewPager vp_content;
    private TextView tv_index;
    private ArrayList<String> mUrList;
    private int index;
    private int mViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        initView();
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        mUrList = intent.getStringArrayListExtra("mUrList");
        mUrList.remove(null);
        index = intent.getIntExtra("index", 0);
        mViewType = intent.getIntExtra("viewType", 0);

        int currentPic=index;

        if (mViewType==1){
            ViewNetworkPhotoAdapter adapter = new ViewNetworkPhotoAdapter(ViewPhotosActivity.this, mUrList, vp_content);
            vp_content.setAdapter(adapter);
        }else {
            ViewPhotoAdapter adapter = new ViewPhotoAdapter(ViewPhotosActivity.this,mUrList,vp_content);
            vp_content.setAdapter(adapter);
        }


        vp_content.setCurrentItem(index);

        tv_index.setText((currentPic + 1) + "/" + mUrList.size());

    }

    private void initView() {
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        tv_index = (TextView) findViewById(R.id.tv_index);
        vp_content.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tv_index.setText((position + 1) + "/" + mUrList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
