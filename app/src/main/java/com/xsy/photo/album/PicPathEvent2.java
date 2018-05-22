package com.xsy.photo.album;

import java.util.ArrayList;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/10
 */
public class PicPathEvent2 {
    ArrayList<String> pathList;

    public PicPathEvent2(ArrayList<String> data)
    {
        pathList = new ArrayList<String>();
        if (data!=null && data.size()>0) {
            pathList.addAll(data);
        }
    }

    public ArrayList<String> getPathList(){
        return pathList;
    }
}
