package com.xsy.photo;

import java.util.ArrayList;
import java.util.List;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xsy.photo.activity.ViewPhotosActivity;
import com.xsy.photo.adapter.PictureListAdapter;
import com.xsy.photo.album.PicPathEvent;
import com.xsy.photo.album.PickOrTakeImageActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private List<String> mUrList = new ArrayList<String>();
    private ImageView iv_go_back;
    private TextView tv_upload;
    private final static int MAXIMGNUMBER = 10;
    private RecyclerView mRecyclerView;
    private PictureListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mUrList.add(null);

        initView();
        initEvent();
    }


    private void initView() {
        iv_go_back = (ImageView) findViewById(R.id.iv_go_back);
        iv_go_back.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new PictureListAdapter(this, R.layout.image_list_item, mUrList);
        mRecyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mUrList.size() < MAXIMGNUMBER && position == mUrList.size() - 1) {
                    int surplus_pics = MAXIMGNUMBER - mUrList.size() + 1;//mUrList没有变
                    Intent intent = new Intent(MainActivity.this, PickOrTakeImageActivity.class);
                    intent.putExtra("pic_max", surplus_pics);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, ViewPhotosActivity.class);
                    intent.putStringArrayListExtra("mUrList", (ArrayList<String>) mUrList);
                    intent.putExtra("index", position);
                    intent.putExtra("viewType", 2);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back://返回
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PicPathEvent event) {
        mUrList.remove(null);
        mUrList.addAll(event.getPathList());
        if (mUrList.size() < MAXIMGNUMBER) {
            mUrList.add(null);
        }
        event.getPathList().clear();
        adapter.notifyDataSetChanged();
    }
}
