package com.wu.safe.smart.ui.module.other.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.module.other.design.view.BottomViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.DialogViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.FloatButtonActivity;
import com.wu.safe.smart.ui.module.other.design.view.GridViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.MoreTabActivity;
import com.wu.safe.smart.ui.module.other.design.view.PageViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.TabActivity;

import butterknife.OnClick;


public class DesignActivity extends BaseCompatActivity {
    private final static String TAG = "DesignActivity";

    @Override
    protected int setContentView() {
        return R.layout.activity_design;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "界面", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.dialog_view, R.id.grid_view, R.id.float_view, R.id.page_view, R.id.stable_tab_view, R.id.more_tab_view, R.id.bottom_view, R.id.left_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_view:
                readGo(DialogViewActivity.class);
                break;
            case R.id.grid_view:
                readGo(GridViewActivity.class);
                break;
            case R.id.float_view:
                readGo(FloatButtonActivity.class);
                break;
            case R.id.page_view:
                readGo(PageViewActivity.class);
                break;
            case R.id.stable_tab_view:
                readGo(TabActivity.class);
                break;
            case R.id.more_tab_view:
                readGo(MoreTabActivity.class);
                break;
            case R.id.bottom_view:
                readGo(BottomViewActivity.class);
                break;
            case R.id.left_view:
                break;
        }
    }
}
