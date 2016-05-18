package com.xsy.photo.base;

import android.app.Application;
import android.content.Context;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xsy.photo.utils.UILImageLoader;



import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;

import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by ${xsy} on 2016/4/12.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private static FunctionConfig mFunctionConfig;
    private static FunctionConfig.Builder mFunctionConfigBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;



        initImageLoader(getApplicationContext());
      //  initGalleryFinal();
    }

    private void initGalleryFinal() {

        mFunctionConfigBuilder = new FunctionConfig.Builder();
        //设置主题
       //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
        .build();
       //配置功能
        mFunctionConfig = mFunctionConfigBuilder
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(8)
               .build();

        //配置imageloader
        ImageLoader imageloader = new UILImageLoader();

        CoreConfig coreConfig = new CoreConfig.Builder(getInstance(), imageloader, theme)
               // .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(mFunctionConfig)
               .build();
        GalleryFinal.init(coreConfig);
    }


    public static MyApplication getInstance(){
        return instance;
    }
    public static FunctionConfig getFunctionConfig() {
        return mFunctionConfig;
    }
    public static FunctionConfig.Builder getFunctionConfigBuilder() {
        return mFunctionConfigBuilder;
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

}
