package com.xsy.photo.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * 项目名称：bat
 * 类描述：
 * 创建人：whan
 * 创建时间：2017/5/12 10:29
 */
class PhotoParams {

    public static final String CROP_TYPE = "image/*";
    public static final String CROP_FILE_NAME = "crop_file.jpg";
    public static final String OUTPUT_FORMAT = Bitmap.CompressFormat.JPEG.toString();

    public static final int DEFAULT_ASPECT = 1;
    public static final int DEFAULT_OUTPUT = 300;

    /** 临时地址 **/
    public Uri uri;
    /** 输出地址 **/
    public Uri outputUri;

    /** 类型 **/
    public String type;

    /** 输入类型，图片如jpg **/
    public String outputFormat;

    /** crop为true可以剪裁 **/
    public String crop;
    public boolean scale;
    public boolean returnData;
    public boolean noFaceDetection;
    public boolean scaleUpIfNeeded;

    /** aspectX aspectY 是宽高的比例 **/
    public int aspectX;
    public int aspectY;

    /** outputX,outputY 是剪裁图片的宽高 **/
    public int outputX;
    public int outputY;

    public PhotoParams(File saveFile) {
        crop = "true";
        type = CROP_TYPE;
        uri = buildUri(saveFile);
        outputUri = buildUri(saveFile);
        scale = true;
        returnData = true;
        noFaceDetection = true;
        scaleUpIfNeeded = true;
        outputFormat = OUTPUT_FORMAT;
        aspectX = DEFAULT_ASPECT;
        aspectY = DEFAULT_ASPECT;
        outputX = DEFAULT_OUTPUT;
        outputY = DEFAULT_OUTPUT;
    }

    private Uri buildUri(File saveFile) {
        return Uri.fromFile(new File(saveFile,CROP_FILE_NAME));
    }

}
