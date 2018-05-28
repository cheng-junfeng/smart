package com.webview.utils;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.webview.R;


public class ToolbarUtil {

    public static void setToolbarLeft(Toolbar toolbar, String title, String leftStr, final View.OnClickListener listener) {
        TextView titleTv = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView leftIm = (ImageView) toolbar.findViewById(R.id.tv_image_left);
        TextView leftTv = (TextView) toolbar.findViewById(R.id.tv_title_left);
        if(!TextUtils.isEmpty(title)){
            titleTv.setText(title);
        }
        if(TextUtils.isEmpty(leftStr)){
            leftTv.setVisibility(View.GONE);
            leftIm.setVisibility(View.VISIBLE);
            leftIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
        }else{
            leftIm.setVisibility(View.GONE);
            leftTv.setText(leftStr);
            leftTv.setVisibility(View.VISIBLE);
            leftTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
        }
    }
}
