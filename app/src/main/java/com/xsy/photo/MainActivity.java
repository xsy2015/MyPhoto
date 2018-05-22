package com.xsy.photo;

import java.util.ArrayList;
import java.util.List;

import com.xsy.photo.activity.UploadImgActivity;
import com.xsy.photo.activity.ViewPhotosActivity;
import com.xsy.photo.album.PicPathEvent;
import com.xsy.photo.album.PickOrTakeImageActivity;
import com.xsy.photo.loader.ImageLoader;
import com.xsy.photo.utils.LoaderNativeImage;
import com.xsy.photo.utils.MyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends Activity implements OnScrollListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    private List<String> mUrList = new ArrayList<String>();
    ImageLoader mImageLoader;
    private GridView mImageGridView;
    private BaseAdapter mImageAdapter;

    private boolean mIsGridViewIdle = true;
    private int mImageWidth = 0;
    private boolean mIsWifi = false;
    private boolean mCanGetBitmapFromNetWork = false;
    private ImageView iv_go_back;
    private TextView tv_upload;
    private LoaderNativeImage thumbnailImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initData();
        initView();
        mImageLoader = ImageLoader.build(MainActivity.this);
        thumbnailImageLoader = new LoaderNativeImage(this);
    }

    private void initData() {
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int) MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;
        mIsWifi = MyUtils.isWifi(this);
        if (mIsWifi) {
            mCanGetBitmapFromNetWork = true;
        }
    }

    private void initView() {

        iv_go_back = (ImageView) findViewById(R.id.iv_go_back);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        iv_go_back.setOnClickListener(this);
        tv_upload.setOnClickListener(this);

        mImageGridView = (GridView) findViewById(R.id.gridView1);
        mImageAdapter = new ImageAdapter(this);
        mImageGridView.setAdapter(mImageAdapter);
        mImageGridView.setOnScrollListener(this);

        mImageGridView.setOnItemClickListener(this);

        if (!mIsWifi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("初次使用会从网络下载大概5MB的图片，确认要下载吗？");
            builder.setTitle("注意");
            builder.setPositiveButton("是", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCanGetBitmapFromNetWork = true;
                    mImageAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(MainActivity.this, ViewPhotosActivity.class);
        intent.putStringArrayListExtra("mUrList", (ArrayList<String>) mUrList);
        intent.putExtra("index", i);
        intent.putExtra("viewType", 1);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back://返回
                break;
            case R.id.tv_upload://上传照片
            {
                Intent intent = new Intent(this, PickOrTakeImageActivity.class);
                intent.putExtra("pic_max", 50);
                startActivityForResult(intent, 10086);
            }
            break;
            default:
                break;
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Drawable mDefaultBitmapDrawable;

        private ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mDefaultBitmapDrawable = context.getResources().getDrawable(R.drawable.image_default);
        }

        @Override
        public int getCount() {
            return mUrList.size();
        }

        @Override
        public String getItem(int position) {
            return mUrList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = holder.imageView;
            final String tag = (String) imageView.getTag();
            final String uri = getItem(position);
            if (!uri.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }
            if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
                imageView.setTag(uri);
                //mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);
                thumbnailImageLoader.displayImage(uri, imageView);
            }
            return convertView;
        }

    }

    private static class ViewHolder {
        public ImageView imageView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            mIsGridViewIdle = true;
            mImageAdapter.notifyDataSetChanged();
        } else {
            mIsGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // ignored

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PicPathEvent event) {
        mUrList.clear();
        mUrList.addAll(event.getPathList());
        mImageAdapter.notifyDataSetChanged();
    }
}
