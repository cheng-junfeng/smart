package com.wu.safe.smart.ui.module.other.design.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class BottomViewActivity extends BaseCompatActivity {
    private final static String TAG = "BottomViewActivity";

    @BindView(R.id.bottomsheet)
    BottomSheetLayout bottomsheet;

    private TextView tvImg;
    private View resourcedataView;

    @Override
    protected int setContentView() {
        return R.layout.activity_bottom_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "底部视图", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        bottomsheet.setPeekOnDismiss(true);
    }

    @OnClick(R.id.show_view)
    public void onViewClicked() {
        if (resourcedataView == null) {
            resourcedataView = LayoutInflater.from(this).inflate(R.layout.item_bottom_view, bottomsheet, false);
        }
        tvImg = resourcedataView.findViewById(R.id.iv_img);
        bottomsheet.setPeekSheetTranslation(resourcedataView.getHeight());
        tvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomsheet.dismissSheet();
            }
        });
        bottomsheet.showWithSheetView(resourcedataView);
    }
}
