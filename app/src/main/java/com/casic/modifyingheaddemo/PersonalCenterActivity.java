package com.casic.modifyingheaddemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author 郭宝
 * @project： FeedbackDemo
 * @package： com.casic.feedbackdemo
 * @date： 2019/5/8 0008 9:48
 * @brief: 个人中心
 *
 * 头像修改逻辑
 * 1、先选择图片（图库/拍照）
 * 2、将选择的图片上传到服务器，服务器返回一个服务器的图片地址
 * 3、下次加载时就直接加载服务器的图片地址
 */
public class PersonalCenterActivity extends AppCompatActivity {

    //        /storage/emulated/0/personal_photo
    private static final String FEEDBACK_PHOTO_PATH = Environment.getExternalStorageDirectory().getPath() + "/personal_photo";
    private static final int PHOTOGRAPH = 0x005;
    private static final int DELETE = 0X007;
    private static final String TAG = "PersonalCenterActivity";
    private ImageView mIv_personalPortrait;

    private static final int IMAGE_PICK_REQUEST = 0x001;
    private File mImgFile;
    private Uri mFileUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_center);
        initView();
        initData();
        initEvent();

    }

    private void initEvent() {
        mIv_personalPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PhotoSelectionDialog photoSelectionDialog = new PhotoSelectionDialog(PersonalCenterActivity.this);
                photoSelectionDialog.show();
                photoSelectionDialog.setOnGalleryListener(new PhotoSelectionDialog.OnGalleryListener() {
                    @Override
                    public void onGallery() {
                        //打开系统自带的图库页面设置图片
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        //设置数据和类型
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, IMAGE_PICK_REQUEST);

                    }
                });

                photoSelectionDialog.setOnPhotographListener(new PhotoSelectionDialog.OnPhotographListener() {


                    @Override
                    public void onPhotograph() {
                        // 利用系统自带的相机应用:拍照
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(FEEDBACK_PHOTO_PATH);

                        if (file.exists() == false || file.isDirectory() == false) {
                            file.mkdirs();
                        }
                        mImgFile = new File(FEEDBACK_PHOTO_PATH + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis())) + ".png");
                        mFileUri = Uri.fromFile(mImgFile);
                        // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                        startActivityForResult(intent2, PHOTOGRAPH);

                    }
                });

            }
        });
    }

    private void initData() {
        Glide.with(PersonalCenterActivity.this)
                .load(R.mipmap.head_portrait_)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(mIv_personalPortrait);

    }

    private void initView() {
        mIv_personalPortrait = findViewById(R.id.iv_personalPortrait);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_PICK_REQUEST:

                    if (data != null) {
                        Uri originalUri = data.getData(); // 获得图片的uri
                        // 这里开始的第二部分，获取图片的路径：
                        String[] proj = {MediaStore.Images.Media.DATA};

                        // 好像是android多媒体数据库的封装接口，具体的看Android文档
                        @SuppressWarnings("deprecation")
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        // 按我个人理解 这个是获得用户选择的图片的索引值
                        if (cursor != null) {
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                            cursor.moveToFirst();
                            // 最后根据索引值获取图片路径
                            String pathimg = cursor.getString(column_index);

                            if (!TextUtils.isEmpty(pathimg)) {
                                Log.i(TAG, "pathimg" + pathimg);
                                ///storage/emulated/0/default/line_3/tiananmen/image/145992928913280.jpg


                                /*Bitmap bitmap = BitmapFactory.decodeFile(pathimg);
                                //加载本地图片
                                mIv_personalPortrait.setImageBitmap(bitmap);*/

                                //Glide 加载图片简单用法
                                Glide.with(PersonalCenterActivity.this)
                                        .load(pathimg)
                                        .centerCrop()
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(mIv_personalPortrait);



                            } else {
                                Toast.makeText(PersonalCenterActivity.this, "图像不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(PersonalCenterActivity.this, "取消发送图像", Toast.LENGTH_SHORT).show();
                    }

                    break;

            }
        }


        switch (requestCode) {
            case PHOTOGRAPH:    // 拍照
                if (RESULT_OK == resultCode) {
                    String path = mFileUri.getPath();


                    String url = "file://" + path;

                    /*//加载本地图片
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    mIv_personalPortrait.setImageBitmap(bitmap);*/


                    //Glide 加载图片简单用法
                    Glide.with(PersonalCenterActivity.this)
                            .load(path)
                            .centerCrop()
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(mIv_personalPortrait);

                    //通知系统更新图库
                    MediaScannerConnection.scanFile(this, new String[]{mImgFile.getAbsolutePath()}, null, null);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(PersonalCenterActivity.this, "取消拍照", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalCenterActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
