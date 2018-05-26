package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class PicChooseActivity extends BaseCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private final static String TAG = "PicChooseActivity";

    @BindView(R.id.get_img)
    ImageView getImg;

    private Uri imageUri;
    private CropOptions cropOptions;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    @Override
    protected int setContentView() {
        return R.layout.activity_pic_choose;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "图选", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void startGetCamPhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        configCompress(takePhoto);
        takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
    }

    private void startGetPhotoFromFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        configCompress(takePhoto);
        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
    }

    private void configCompress(TakePhoto takePhoto) {
        //配置图片压缩
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(102400)                                                 //压缩到的最大大小
                .setMaxPixel(SizeUtils.dp2px(80.0f))        //长或宽不超过的最大像素
                .enablePixelCompress(true)                                          //是否启用像素压缩
                .enableQualityCompress(true)                                        //是否启用质量压缩
                .enableReserveRaw(true)                                             //是否保留原文件
                .create();
        takePhoto.onEnableCompress(config, false);

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setCorrectImage(true);              //是否裁切图片
        builder.setWithOwnGallery(true);            //设置自带裁切工具
        takePhoto.setTakePhotoOptions(builder.create());

        //cropOptions 裁剪配置类
        cropOptions = new CropOptions.Builder()
                .setAspectX(SizeUtils.dp2px(75.0f))
                .setAspectY(SizeUtils.dp2px(100.0f))
                .setWithOwnCrop(true)
                .create();
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this)
                    .bind(new TakePhotoImpl(PicChooseActivity.this, this));
        }
        return takePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        LogUtil.d(TAG, "takeSuccess：" + result.getImage().getCompressPath());
        TImage image = result.getImage();
        Bitmap bmpDefaultPic = BitmapFactory.decodeFile(image.getCompressPath(), null);
        getImg.setImageBitmap(bmpDefaultPic);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @OnClick({R.id.camera_view, R.id.choose_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.camera_view:
                startGetCamPhoto();
                break;
            case R.id.choose_view:
                startGetPhotoFromFile();
                break;
        }
    }
}
