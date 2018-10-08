package com.xsy.photo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xsy.photo.R;
import com.xsy.photo.utils.LoaderNativeImage;

import java.util.List;

/**
 * @Description描述:
 * @Author作者: xsy
 * @Date日期: 2018/10/8
 */

public class PictureListAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private Context mContext;
    private final LoaderNativeImage loaderNativeImage;

    public PictureListAdapter(Context mContext,int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
        this.mContext=mContext;
        loaderNativeImage = new LoaderNativeImage(mContext);
    }

    @Override
    protected void convert(BaseViewHolder helper, String url) {
        ImageView img = helper.getView(R.id.image);
        if (TextUtils.isEmpty(url)){
            img.setImageResource(R.drawable.ic_add_img);
        }else {
            loaderNativeImage.displayImage(url, img);
        }
    }
}
