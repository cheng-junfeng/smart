package com.wu.safe.smart.ui.module.other.design.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.ui.widget.GalleryImageView;
import com.wu.safe.smart.ui.widget.loader.DefaultImageLoader;
import com.wu.safe.smart.ui.widget.model.MediaInfo;
import com.wu.safe.smart.ui.widget.loader.MediaLoader;
import com.wu.safe.smart.ui.widget.loader.PicassoImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class PicGalleryFragment extends BaseCompatFragment {
    private final static String TAG = "PicGalleryFragment";

    @BindView(R.id.gallery_view)
    GalleryImageView galleryView;

    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg"
    ));

    @Override
    protected int setContentView() {
        return R.layout.fragment_pic_gallery;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);

        initView();
        return containerView;
    }

    private void initView() {
        List<MediaInfo> infos = new ArrayList<>(images.size());
        for (String url : images) {
            infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));
        }

        galleryView
                .setThumbnailSize(SizeUtils.dp2px(100))
                .setZoom(true)
                .setFragmentManager(getFragmentManager())
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.mipmap.wallpaper1)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(toBitmap(R.mipmap.wallpaper2))))
                .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
                    @Override
                    public boolean isImage() {
                        return true;
                    }

                    @Override
                    public void loadMedia(Context context, ImageView imageView, MediaLoader.SuccessCallback callback) {
                        imageView.setImageBitmap(toBitmap(R.mipmap.wallpaper3));
                        callback.onSuccess();
                    }

                    @Override
                    public void loadThumbnail(Context context, ImageView thumbnailView, MediaLoader.SuccessCallback callback) {
                        thumbnailView.setImageBitmap(toBitmap(R.mipmap.wallpaper4));
                        callback.onSuccess();
                    }
                }))
                .addMedia(infos);
    }

    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }
}
