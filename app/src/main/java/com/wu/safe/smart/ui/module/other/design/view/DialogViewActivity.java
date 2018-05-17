package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.wu.safe.base.app.listener.OnInputClickListener;
import com.wu.safe.base.app.listener.OnPositionSelectListener;
import com.wu.safe.base.app.listener.OnSelectClickListener;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class DialogViewActivity extends BaseCompatActivity {
    private final static String TAG = "DialogViewActivity";
    @BindView(R.id.progress_view)
    Button progressView;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_dialog_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "弹框", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.progress_view, R.id.progress2_view, R.id.confirm_view, R.id.choose_view, R.id.input_view, R.id.toast_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.progress_view:
                DialogUtils.showProgressDialog(mContext);
                break;
            case R.id.progress2_view:
                DialogUtils.showProgressDialog(mContext, "正在进行");
                break;
            case R.id.confirm_view:
                DialogUtils.showConfirmDialog(mContext, "确认？", new OnSelectClickListener() {
                    @Override
                    public void onClickPositive() {
                        DialogUtils.dismissDialog();
                        DialogUtils.showToast(mContext, "confirm:确认");
                    }

                    @Override
                    public void onClickNegative() {
                        DialogUtils.dismissDialog();
                        DialogUtils.showToast(mContext, "choose:取消");
                    }
                });
                break;
            case R.id.choose_view:
                DialogUtils.showChooseDialog(mContext, new OnPositionSelectListener() {
                    @Override
                    public void onPositiveSelect(int pos) {
                        DialogUtils.dismissDialog();
                        DialogUtils.showToast(mContext, "choose:" + pos);
                    }
                });
                break;
            case R.id.input_view:
                DialogUtils.showInputDialog(mContext, "输入地址", "www.baidu.com", new OnInputClickListener() {
                    @Override
                    public void onClickPositive(String inputStr) {
                        DialogUtils.dismissDialog();
                        DialogUtils.showToast(mContext, "confirm:确认");
                    }

                    @Override
                    public void onClickNegative() {
                        DialogUtils.dismissDialog();
                        DialogUtils.showToast(mContext, "choose:取消");
                    }
                });
                break;
            case R.id.toast_view:
                DialogUtils.showToast(mContext, "提示");
                break;
        }
        progressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtils.dismissProgressDialog();
                DialogUtils.dismissDialog();
            }
        }, 5000);
    }
}
