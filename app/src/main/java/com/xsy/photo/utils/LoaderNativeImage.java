package com.xsy.photo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xsy.photo.R;


public class LoaderNativeImage {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public LoaderNativeImage(Context context){
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.ic_product_9) // 在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic) // image连接地址为空时
                .showImageOnFail(R.drawable.default_pic) // image加载失败
                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成
    }

    public LoaderNativeImage(Context context, int circle){//这个构造方法用来加载圆形的本地图片
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.ic_product_9) // 在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.drawable.default_pic) // image连接地址为空时
                .showImageOnFail(R.drawable.default_pic) // image加载失败
                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new CircleBitmapDisplayer())//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成
    }

    public void displayImage(String uri, ImageView imageView) {
        imageLoader.displayImage("file://" + uri, imageView, options);
    }
}
