package com.xsy.photo.adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xsy.photo.R;
import com.xsy.photo.loader.ImageLoader;
import com.xsy.photo.ui.ZoomImageView;
import com.xsy.photo.utils.BitmapCacheHelper;
import com.xsy.photo.utils.LoaderNativeImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/12.
 */
public class ViewPhotoAdapter extends PagerAdapter {

    private List<String> mData;
    private Context mContext;
    private ViewPager mViewPager;
    private final LoaderNativeImage loaderNativeImage;

    public ViewPhotoAdapter(Context context,ArrayList<String> mUrList,ViewPager viewPager){
        this.mData=mUrList;
        this.mContext=context;
        this.mViewPager=viewPager;
        loaderNativeImage = new LoaderNativeImage(mContext);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_photo, null);
        ImageView zoomImageView = (ImageView) view.findViewById(R.id.zoom_image_view);
        loaderNativeImage.displayImage(mData.get(position),zoomImageView);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
