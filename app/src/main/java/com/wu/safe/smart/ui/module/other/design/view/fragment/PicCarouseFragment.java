package com.wu.safe.smart.ui.module.other.design.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.ui.widget.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PicCarouseFragment extends BaseCompatFragment {
    private final static String TAG = "PicCarouseFragment";
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.fragment_pic_carouse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);

        mContext = this.getContext();
        initView();
        return containerView;
    }

    private void initView() {
        List<String> info1 = new ArrayList<>();
        info1.add("1.坚持读书，写作，源于内心的动力！");
        info1.add("2.坚持锻炼！");
        info1.add("3.早睡！");
        info1.add("4.学习！");
        marqueeView.startWithList(info1);
        // 在代码里设置自己的动画
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                DialogUtils.showToast(mContext, "click:"+position);
            }
        });
    }
}
