package com.xsy.photo.utils;

import android.content.Context;
import android.os.Environment;

import com.xsy.photo.base.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: zzp(zhao_zepeng@hotmail.com)
 * @since: 2015-06-22
 * Description:
 */
public class CommonUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    /**
     * md5加密
     */
    public static String md5(Object object) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(toByteArray(object));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }



    /**
     * 获取存储路径
     */
    public static String getDataPath() {
        String path;
        if (isExistSDcard())
            path = Environment.getExternalStorageDirectory().getPath() + "/albumSelect";
        else
            path = MyApplication.getInstance().getFilesDir().getPath();
        if (!path.endsWith("/"))
            path = path + "/";
        return path;
    }


    /**
     * 检测SDcard是否存在
     *
     * @return
     */
    public static boolean isExistSDcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else {
            return false;
        }
    }

    /**
     * MD5 加密
     */
    public static String toMD5(String data) {

        try {
            // 实例化一个指定摘要算法为MD5的MessageDigest对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 重置摘要以供再次使用
            md5.reset();
            // 使用bytes更新摘要
            md5.update(data.getBytes());
            // 使用指定的byte数组对摘要进行最的更新，然后完成摘要计算
            return toHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    // 将字符串中的每个字符转换为十六进制
    private static String toHexString(byte[] bytes) {

        StringBuilder hexstring = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexstring.append('0');
            }
            hexstring.append(hex);

        }

        return hexstring.toString();
    }

}
