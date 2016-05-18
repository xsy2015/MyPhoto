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
import com.xsy.photo.base.MyApplication;
import com.xsy.photo.customview.ActionSheetDialog;

import com.xsy.photo.utils.Bimp;
import com.xsy.photo.utils.BitmapCacheHelper;
import com.xsy.photo.utils.FileUtils;
import com.xsy.photo.utils.ImageItem;
import com.xsy.photo.utils.MyUtils;
import com.xsy.photo.utils.UILImageLoader;



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

    private static final int TAKE_PICTURE = 0x000001;
    private List<String> imgUrls=new ArrayList<String>();
    private TextView tv_upload;
    private List<String> mImgUrl;

    private List<PhotoInfo> mPhotoList;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private FunctionConfig mFunctionConfig;
    private FunctionConfig.Builder mFunctionConfigBuilder;
    private int mImageWidth;
    private ImageView mIv_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        initView();

        initData();
    }

    private void initData() {
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int)MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;

    }

    private void initView() {
        mPhotoList = new ArrayList<>();
        gv = (GridView) findViewById(R.id.gv);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_upload.setOnClickListener(this);
        mIv_go_back = (ImageView) findViewById(R.id.iv_go_back);
        mIv_go_back.setOnClickListener(this);

        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList,this);
        gv.setAdapter(mChoosePhotoListAdapter);
    }

    private void showSheetDialog() {

        mFunctionConfigBuilder = new FunctionConfig.Builder();
        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(10, 195, 158))
                .setFabNornalColor(Color.rgb(0x4C, 0xAF, 0x50))
                .setFabPressedColor(Color.rgb(0x38, 0x8E, 0x3C))
                .setCheckSelectedColor(Color.rgb(0x4C, 0xAF, 0x50))
                .setCropControlColor(Color.rgb(0x4C, 0xAF, 0x50))
                .build();

       // mFunctionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        //配置功能
        mFunctionConfig = mFunctionConfigBuilder
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setRotateReplaceSource(true)
                .setMutiSelectMaxSize(8)
                .build();

        //配置imageloader
        ImageLoader imageloader = new UILImageLoader();

        CoreConfig coreConfig = new CoreConfig.Builder(UploadImgActivity.this, imageloader,theme)
                // .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(mFunctionConfig)
               // .setPauseOnScrollListener(pauseOnScrollListener)
                //.setNoAnimcation(mCbNoAnimation.isChecked())
                .build();
        GalleryFinal.init(coreConfig);



        new ActionSheetDialog(this).builder()
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.DEFAULT,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mFunctionConfig, mOnHanlderResultCallback);
                            }
                        })
                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.DEFAULT,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,mFunctionConfig, mOnHanlderResultCallback);
                            }
                        }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_upload:
            {

//                for (int i = 0; i < mImgUrl.size(); i++) {
//                    DataSupport.deleteAll(Photos.class, "imgUrl=?",mImgUrl.get(i));
//                }


                Toast.makeText(UploadImgActivity.this,"提交成功",Toast.LENGTH_LONG).show();
            }
                break;
            case R.id.iv:
                {
                    //GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, MyApplication.getFunctionConfig(), mOnHanlderResultCallback);
                    showSheetDialog();
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



    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

            if (resultList != null) {
                mPhotoList.addAll(resultList);
               // mPhotoList=removeDuplicate(mPhotoList);
               mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(UploadImgActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public   List  removeDuplicate(List<PhotoInfo> list)  {
//        HashSet<PhotoInfo> h  =   new  HashSet<PhotoInfo>(list);
//        list.clear();
//        list.addAll(h);
        for (int i = 0; i < list.size(); i++) {
            for (int j = list.size()-1; j>i ; j--) {
                if (list.get(i).equals(list.get(j))){
                    list.remove(i);
                }
            }
        }
        System.out.println("List<PhotoInfo>="+list);
        return list;
    }

}
