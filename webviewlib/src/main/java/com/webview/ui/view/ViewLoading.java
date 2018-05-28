package com.webview.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.webview.R;


public abstract class ViewLoading extends Dialog {

    public ViewLoading(Context context, String content) {
        super(context, R.style.Loading);
        setContentView(R.layout.loading_dialog);
        TextView message = (TextView) findViewById(R.id.message);
        if (content != null && content.length() > 0) {
            message.setText(content);
        } else {
            message.setText("加载中");
        }
        ImageView progressImageView = (ImageView) findViewById(R.id.iv_image);
        //创建旋转动画
        Animation animation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        //动画的重复次数
        animation.setRepeatCount(10);
        //设置为true，动画转化结束后被应用
        animation.setFillAfter(true);
        //开始动画
        progressImageView.startAnimation(animation);
        // 设置Dialog参数
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    /**
     * 封装Dialog消失的回调
     */
    @Override
    public void onBackPressed() {
        //回调
        loadCancel();
        //关闭Loading
        dismiss();
    }

    /**
     * 抽象方法，子类继承实现
     * 处理消失后的逻辑
     */
    protected abstract void loadCancel();
}
