package com.casic.modifyingheaddemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * @project： LeanCloudDemo2
 * @package： com.gjcw.leanclouddemo.view
 * @date： 2017/3/21 15:24
 * @author： 郭宝
 * @brief: 照片选择对话框
 */
public class PhotoSelectionDialog extends Dialog {

    private Dialog mDialog;
    private View mView;
    private Button mPhotoSelectionDialog_gallery;
    private Button mPhotoSelectionDialog_photograph;
    private Button mPhotoSelectionDialog_cancel;

    public OnGalleryListener mOnGalleryListener;

    /**
     * 自定义的图库回调接口
     */
    public interface OnGalleryListener{
        void onGallery();
    }

    /**
     * 向外暴露一个设置监听器的方法
     * @param onGalleryListener
     */
    public void setOnGalleryListener(OnGalleryListener onGalleryListener){
        mOnGalleryListener = onGalleryListener;
    }


    public OnPhotographListener mOnPhotographListener;

    /**
     * 自定义拍照回调接口
     */
    public interface OnPhotographListener{
        void onPhotograph();
    }

    /**
     * 向外暴露一个设置拍照监听器的方法
     * @param onPhotographListener
     */
    public void  setOnPhotographListener(OnPhotographListener onPhotographListener){
        mOnPhotographListener = onPhotographListener;
    }


    public PhotoSelectionDialog(Context context) {
        super(context);
        init(context);
    }

    public PhotoSelectionDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected PhotoSelectionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }


    private void init(Context context) {

        mView = View.inflate(context, R.layout.photo_choose_dialog, null);
        mDialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        //		传入上下文，自定义风格
        mDialog.setContentView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //		设置内容视图,传入自定义的对话框布局视图,传入视图的布局参数
        Window window = mDialog.getWindow();
        //		获取窗口
        window.setWindowAnimations(R.style.main_menu_animstyle);
        //		设置窗口动画
        WindowManager.LayoutParams wl = window.getAttributes();
        //		获取属性
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        // 获取手机屏幕的高
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //给对话框设置一个窗口属性改变改变
        mDialog.onWindowAttributesChanged(wl);
        //设置响应外面的触摸实现当点击外面的窗体时取消对话框
        mDialog.setCanceledOnTouchOutside(true);

        //初始化视图
        initView();
        initEvent();
    }




    @Override
    public void show() {
        if (mDialog!=null&&!mDialog.isShowing()){
            mDialog.show();
        }
        //禁止父类的对话框显示
        //super.show();
    }

    private void initView() {

        //图库
        mPhotoSelectionDialog_gallery = (Button) mView.findViewById(R.id.photoSelectionDialog_gallery);
        //拍照
        mPhotoSelectionDialog_photograph = (Button) mView.findViewById(R.id.photoSelectionDialog_photograph);
        //取消
        mPhotoSelectionDialog_cancel = (Button) mView.findViewById(R.id.photoSelectionDialog_cancel);

    }

    private void initEvent() {
        //图库
        mPhotoSelectionDialog_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当点击图库时调用实现该接口的子类对象中的onGallery()
                mOnGalleryListener.onGallery();
                cancel();
            }
        });

        //拍照
        mPhotoSelectionDialog_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnPhotographListener.onPhotograph();
                cancel();
            }
        });
        //取消
        mPhotoSelectionDialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }

    @Override
    public void cancel() {

        //屏蔽父类的取消
        //super.cancel();
        //取消自定义的对话框
        if (mDialog!=null&&mDialog.isShowing()){
            mDialog.cancel();
        }

    }
}
