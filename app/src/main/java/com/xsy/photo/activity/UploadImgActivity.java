package com.xsy.photo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xsy.photo.R;
import com.xsy.photo.adapter.ChoosePhotoListAdapter;
import com.xsy.photo.album.PicPathEvent;
import com.xsy.photo.album.PickOrTakeImageActivity;
import com.xsy.photo.base.MyApplication;
import com.xsy.photo.customview.ActionSheetDialog;

import com.xsy.photo.utils.Bimp;
import com.xsy.photo.utils.BitmapCacheHelper;
import com.xsy.photo.utils.FileUtils;
import com.xsy.photo.utils.ImageItem;
import com.xsy.photo.utils.MyUtils;
import com.xsy.photo.utils.UILImageLoader;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by ${xsy} on 2016/4/13.
 */
public class UploadImgActivity extends Activity implements View.OnClickListener{

    private GridView gv;
    public static final int MAX_IMG_NUM = 9;
    private List<String> imgUrls=new ArrayList<String>();
    private TextView tv_upload;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private int mImageWidth;
    private ImageView mIv_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initData() {
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int)MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;

    }

    private void initView() {

        gv = (GridView) findViewById(R.id.gv);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_upload.setOnClickListener(this);
        mIv_go_back = (ImageView) findViewById(R.id.iv_go_back);
        mIv_go_back.setOnClickListener(this);

        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, imgUrls,this);
        gv.setAdapter(mChoosePhotoListAdapter);
    }

    private void showSheetDialog() {
        new ActionSheetDialog(this).builder()
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.DEFAULT,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                            }
                        })
                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.DEFAULT,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                            }
                        }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_upload:
            {
                Toast.makeText(UploadImgActivity.this,"提交成功",Toast.LENGTH_LONG).show();
            }
                break;
            case R.id.iv:
                {
                    int surplus_pics =MAX_IMG_NUM - imgUrls.size();
                    Intent intent = new Intent(this, PickOrTakeImageActivity.class);
                    intent.putExtra("pic_max", surplus_pics);
                    startActivityForResult(intent, 10086);
                }
                break;
            case R.id.iv_go_back:
                {
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PicPathEvent event) {
        //imgUrls.clear();
        imgUrls.addAll(event.getPathList());
        mChoosePhotoListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
