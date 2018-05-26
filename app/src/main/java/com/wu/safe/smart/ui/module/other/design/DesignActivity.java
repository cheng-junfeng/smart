package com.wu.safe.smart.ui.module.other.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.config.Extra;
import com.wu.safe.smart.ui.module.other.design.view.BottomDialogActivity;
import com.wu.safe.smart.ui.module.other.design.view.BottomViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.CountViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.DialogViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.FloatButtonActivity;
import com.wu.safe.smart.ui.module.other.design.view.GridFilterActivity;
import com.wu.safe.smart.ui.module.other.design.view.LeftViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.MoreTabActivity;
import com.wu.safe.smart.ui.module.other.design.view.PageViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.PicChooseActivity;
import com.wu.safe.smart.ui.module.other.design.view.StatusViewActivity;
import com.wu.safe.smart.ui.module.other.design.view.SwipeMenuActivity;
import com.wu.safe.smart.ui.module.other.design.view.TabActivity;
import com.wu.safe.smart.ui.module.other.design.view.TopDraggerActivity;
import com.wu.safe.smart.ui.module.other.design.view.fragment.FragmentsActivity;

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

    @OnClick({R.id.top_status_view, R.id.top_dragger_view, R.id.dialog_view, R.id.grid_view, R.id.float_view, R.id.page_view, R.id.stable_tab_view, R.id.more_tab_view, R.id.bottom_view, R.id.bottom_dialog
            , R.id.left_view, R.id.count_view, R.id.pic_carouse_view, R.id.pic_gallery_view, R.id.pic_choose_view, R.id.swpe_delete_view})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.top_status_view:
                readGo(StatusViewActivity.class);
                break;
            case R.id.top_dragger_view:
                readGo(TopDraggerActivity.class);
                break;
            case R.id.stable_tab_view:
                readGo(TabActivity.class);
                break;
            case R.id.more_tab_view:
                readGo(MoreTabActivity.class);
                break;
            case R.id.left_view:
                readGo(LeftViewActivity.class);
                break;

            case R.id.dialog_view:
                readGo(DialogViewActivity.class);
                break;
            case R.id.grid_view:
                readGo(GridFilterActivity.class);
                break;
            case R.id.float_view:
                readGo(FloatButtonActivity.class);
                break;
            case R.id.page_view:
                readGo(PageViewActivity.class);
                break;
            case R.id.count_view:
                readGo(CountViewActivity.class);
                break;
            case R.id.bottom_view:
                readGo(BottomViewActivity.class);
                break;
            case R.id.bottom_dialog:
                readGo(BottomDialogActivity.class);
                break;

            case R.id.pic_carouse_view:
                bundle.putInt(Extra.FRAGMENT_POS, 1);
                readGo(FragmentsActivity.class, bundle);
                break;
            case R.id.pic_choose_view:
                readGo(PicChooseActivity.class);
                break;
            case R.id.pic_gallery_view:
                bundle.putInt(Extra.FRAGMENT_POS, 2);
                readGo(FragmentsActivity.class, bundle);
                break;
            case R.id.swpe_delete_view:
                readGo(SwipeMenuActivity.class);
                break;
        }
    }
}
