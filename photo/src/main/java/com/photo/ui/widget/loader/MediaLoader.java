package com.photo.ui.widget.loader;

import android.content.Context;
import android.widget.ImageView;


public interface MediaLoader {
    String ZOOM = "zoom";
    String URL = "url";
    String IS_LOCKED = "isLocked";
    String IMAGE = "image";

    /**
     * @return true if implementation load's image, otherwise false
     */
    boolean isImage();

    void loadMedia(Context context, ImageView imageView, SuccessCallback callback);


    void loadThumbnail(Context context, ImageView thumbnailView, SuccessCallback callback);

    interface SuccessCallback {
        void onSuccess();
    }
}