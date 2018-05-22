package com.xsy.photo.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.xsy.photo.R;
import com.xsy.photo.utils.LoaderNativeImage;
import com.xsy.photo.utils.PhotoUtils;

import java.util.ArrayList;


/**
 * @author: zzp
 * @since: 2015-06-15
 * Description: 仿微信大图选择界面
 */
public class PickBigImagesActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{
    private ViewPager viewPager;
    private TextView tv_choose_pic;
    private ImageView iv_choose_state;
//    private Button btn_choose_finish;
    private TextView tv_titleBar_right;

    private MyViewPagerAdapter adapter;

    private ArrayList<SingleImageModel> allimages;
    ArrayList<String> picklist;
    /** 当前选中的图片 */
    private int currentPic;

    private int last_pics;
    private int total_pics;

    private boolean isFinish = false;

    /** 选择的照片文件夹 */
    public final static String EXTRA_DATA = "extra_data";
    /** 所有被选中的图片 */
    public final static String EXTRA_ALL_PICK_DATA = "extra_pick_data";
    /** 当前被选中的照片 */
    public final static String EXTRA_CURRENT_PIC = "extra_current_pic";
    /** 剩余的可选择照片 */
    public final static String EXTRA_LAST_PIC = "extra_last_pic";
    /** 总的照片 */
    public final static String EXTRA_TOTAL_PIC = "extra_total_pic";

    private LoaderNativeImage loaderNativeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_big_image);
        allimages = PhotoUtils.allimages;
        initFindView();
        initData();
    }

    protected void initFindView() {
        viewPager = (ViewPager) findViewById(R.id.vp_content);
        tv_choose_pic = (TextView) findViewById(R.id.tv_choose_pic);
        iv_choose_state = (ImageView) findViewById(R.id.iv_choose_state);
        tv_choose_pic.setOnClickListener(this);
        iv_choose_state.setOnClickListener(this);

        ImageView iv_titleBar_left = (ImageView)findViewById(R.id.iv_titleBar_left);
        iv_titleBar_left.setVisibility(View.VISIBLE);
        iv_titleBar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_titleBar_right = (TextView) findViewById(R.id.tv_titleBar_right);
        tv_titleBar_right.setText("完成");
        tv_titleBar_right.setVisibility(View.VISIBLE);
        tv_titleBar_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isFinish = true;
                finish();
            }
        });

        if(last_pics < total_pics) {
            tv_titleBar_right.setTextColor(getResources().getColor(R.color.white));
            tv_titleBar_right.setText(String.format(getString(R.string.choose_pic_finish_with_num), total_pics - last_pics, total_pics));
        }
    }

    protected void initData() {
//        allimages = (ArrayList<SingleImageModel>) getIntent().getSerializableExtra(EXTRA_DATA);
        picklist = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_ALL_PICK_DATA);
        loaderNativeImage = new LoaderNativeImage(this);
        if (picklist == null)
            picklist = new ArrayList<String>();
        currentPic = getIntent().getIntExtra(EXTRA_CURRENT_PIC, 0);

        last_pics = getIntent().getIntExtra(EXTRA_LAST_PIC, 0);
        total_pics = getIntent().getIntExtra(EXTRA_TOTAL_PIC, 9);

        setTitle((currentPic + 1) + "/" + getImagesCount());
        //如果该图片被选中
        if (getChooseStateFromList(currentPic)){
            iv_choose_state.setBackgroundResource(R.mipmap.image_choose);
        }else{
            iv_choose_state.setBackgroundResource(R.mipmap.image_not_chose);
        }

        adapter = new MyViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(currentPic);
    }

//    private void getAllImages() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                SingleImageModel temp = new SingleImageModel();
////                temp.path = tempPath;
////                temp.date = System.currentTimeMillis();
////                temp.id = 0;
//
//
//                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                ContentResolver contentResolver = getContentResolver();
//                //获取jpeg和png格式的文件，并且按照时间进行倒序
//                Cursor cursor = contentResolver.query(uri, null, MediaStore.Images.Media.MIME_TYPE + "=\"image/jpeg\" or " +
//                        MediaStore.Images.Media.MIME_TYPE + "=\"image/png\"", null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
//                if (cursor != null) {
//                    allimages.clear();
////                    if (!TextUtils.isEmpty(temp.path)) {
////                        allImages.add(temp);
////                    }
//                    while (cursor.moveToNext()) {
//                        SingleImageModel singleImageModel = new SingleImageModel();
//                        singleImageModel.path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                        try {
//                            singleImageModel.date = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
//                        } catch (NumberFormatException e) {
//                            singleImageModel.date = System.currentTimeMillis();
//                        }
//                        try {
//                            singleImageModel.id = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
//                        } catch (NumberFormatException e) {
//                            singleImageModel.id = 0;
//                        }
//
//                        //存入按照目录分配的list
//                        String path = singleImageModel.path;
//                        File file = new File(path);
//                        String parentPath = file.getParent();
//
//                        //图片是否破损
//                        boolean isFalseFile = false;
//                        BitmapFactory.Options options = null;
//                        if (options == null) options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
//
//                        BitmapFactory.decodeFile(path, options); //filePath代表图片路径
//                        if (options.mCancel || options.outWidth == -1
//                                || options.outHeight == -1) {
//                            //表示图片已损毁
//                            isFalseFile = true;
//                        }
//
//                        if (file.length() > 0 && !isFalseFile) {
//                            allimages.add(singleImageModel);
////                            putImageToParentDirectories(parentPath, path, singleImageModel.date, singleImageModel.id);
//                        }
//                    }
//                    cursor.close();
////                    handler.sendEmptyMessage(0);
//                }
//            }
//        }).start();
//    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //如果该图片被选中
        if (getChooseStateFromList(position)){
            iv_choose_state.setBackgroundResource(R.mipmap.image_choose);
        }else{
            iv_choose_state.setBackgroundResource(R.mipmap.image_not_chose);
        }
        currentPic = position;
//        ((TextView)findViewById(R.id.tv_title)).setName((currentPic + 1) + "/" + getImagesCount());
        //TODO 标题栏
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View view) {
        toggleChooseState(currentPic);
        //如果被选中
        if(getChooseStateFromList(currentPic)){
            if (last_pics <= 0){
                toggleChooseState(currentPic);
                Toast.makeText(this, String.format(getString(R.string.choose_pic_num_out_of_index), total_pics), Toast.LENGTH_SHORT).show();
                return ;
            }
            picklist.add(getPathFromList(currentPic));
            last_pics --;
            iv_choose_state.setBackgroundResource(R.mipmap.image_choose);
            if(last_pics == total_pics-1){
                tv_titleBar_right.setTextColor(getResources().getColor(R.color.light_green));
            }
            tv_titleBar_right.setText(String.format(getString(R.string.choose_pic_finish_with_num), total_pics - last_pics, total_pics));
        }else{
            picklist.remove(getPathFromList(currentPic));
            last_pics ++;
            iv_choose_state.setBackgroundResource(R.mipmap.image_not_chose);
            if(last_pics == total_pics){
                tv_titleBar_right.setTextColor(getResources().getColor(R.color.found_description_color));
                tv_titleBar_right.setText(getString(R.string.finish));
            }else{
                tv_titleBar_right.setText(String.format(getString(R.string.choose_pic_finish_with_num), total_pics - last_pics, total_pics));
            }
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getImagesCount();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(PickBigImagesActivity.this).inflate(R.layout.widget_zoom_iamge, null);
//            final ZoomImageView zoomImageView = (ZoomImageView) view.findViewById(R.id.zoom_image_view);
//
//            AlbumBitmapCacheHelper.getInstance().addPathToShowlist(getPathFromList(position));
//            zoomImageView.setTag(getPathFromList(position));
//            Bitmap bitmap = AlbumBitmapCacheHelper.getInstance().getBitmap(getPathFromList(position), 0, 0, new AlbumBitmapCacheHelper.ILoadImageCallback() {
//                @Override
//                public void onLoadImageCallBack(Bitmap bitmap, String path, Object... objects) {
//                    ZoomImageView view = ((ZoomImageView)viewPager.findViewWithTag(path));
//                    if (view != null && bitmap != null)
//                        ((ZoomImageView)viewPager.findViewWithTag(path)).setSourceImageBitmap(bitmap, PickBigImagesActivity.this);
//                }
//            }, position);
//            if (bitmap != null){
//                zoomImageView.setSourceImageBitmap(bitmap, PickBigImagesActivity.this);
//            }
            ImageView zoomImageView =(ImageView)view.findViewById(R.id.zoom_image_view);
            loaderNativeImage.displayImage(getPathFromList(position),zoomImageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            AlbumBitmapCacheHelper.getInstance().removePathFromShowlist(getPathFromList(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 通过位置获取该位置图片的path
     */
    private String getPathFromList(int position){
        return allimages.get(position).path;
    }

    /**
     * 通过位置获取该位置图片的选中状态
     */
    private boolean getChooseStateFromList(int position){
        return allimages.get(position).isPicked;
    }

    /**
     * 反转图片的选中状态
     */
    private void toggleChooseState(int position){
        allimages.get(position).isPicked = !allimages.get(position).isPicked;
    }

    /**
     * 获得所有的图片数量
     */
    private int getImagesCount(){
        return allimages.size();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        PhotoUtils.allimages = null;
        data.putExtra("pick_data", picklist);
        data.putExtra("isFinish", isFinish);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
