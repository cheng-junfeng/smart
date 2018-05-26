package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.BindView;


public class TopDraggerActivity extends BaseCompatActivity {
    private final static String TAG = "TopDraggerActivity";
    @BindView(R.id.app_bar)
    AppBarLayout appBar;

    @BindView(R.id.ll_main)
    FrameLayout llMain;
    @BindView(R.id.ll_person)
    RelativeLayout llPerson;
    @BindView(R.id.my_bar)
    Toolbar myBar;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_top_dragger;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "向上缩进", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        myBar.setTitle("Admin");
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LogUtil.d(TAG, "verticalOffset:" + verticalOffset);
                if (verticalOffset < -180) {
                    llPerson.setVisibility(View.GONE);
                    llMain.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                } else {
                    llPerson.setVisibility(View.VISIBLE);
                    llMain.setBackgroundResource(R.mipmap.bg_1);
                }
            }
        });
    }
}
