package com.smart.ui.module.other.design.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hint.listener.OnChooseListener;
import com.hint.listener.OnConfirmListener;
import com.hint.listener.OnInputListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AlertDialog和Popupwindow的区别：
 1）AlertDialog是非阻塞线程的，Popupwindow是阻塞线程的。
 2）Dialog没法设置宽为整个屏幕宽，总有点边界。Popupwindow可以。
 3)PopupWindow 更方便设置显示位置

 全局的，显示正中间的，主要是提示作用的，用Dialog
 局部的，特定位置显示的，需要全屏的，用PopWindows
 * */
public class DialogViewActivity extends BaseCompatActivity {
    private final static String TAG = "DialogViewActivity";
    @BindView(R.id.progress_view)
    Button progressView;

    @BindView(R.id.test1)
    TextView test1;
    @BindView(R.id.test2)
    TextView test2;

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

    @OnClick({R.id.progress0_view, R.id.progress00_view, R.id.progress_view, R.id.progress2_view, R.id.confirm_view, R.id.confirm_view2,
            R.id.choose_view, R.id.input_view, R.id.toast_view, R.id.test1, R.id.test2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.progress0_view:
                DialogUtils.showLoading(mContext);
                break;
            case R.id.progress00_view:
                DialogUtils.showLoading(mContext, "拼命加载中");
                break;
            case R.id.progress_view:
                DialogUtils.showProgressDialog(mContext);
                break;
            case R.id.progress2_view:
                DialogUtils.showProgressDialog(mContext, "正在玩命加载加载中");
                break;
            case R.id.confirm_view:
                DialogUtils.showConfirmDialog(mContext, "这是一个确认框？", new OnConfirmListener() {
                    @Override
                    public void onClickPositive() {
                        ToastUtils.showToast(mContext, "确认");
                    }

                    @Override
                    public void onClickNegative() {
                        ToastUtils.showToast(mContext, "取消");
                    }
                });
                break;
            case R.id.confirm_view2:
                DialogUtils.showConfirmAlertDialog(mContext, "这是一个确认框？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showToast(mContext, "confirm2:确认");
                    }
                });
                break;
            case R.id.choose_view:
                ArrayList<String> allStr = new ArrayList<>();
                allStr.add("choose1");
                allStr.add("choose2");
                allStr.add("choose3");
                allStr.add("choose4");
                DialogUtils.showChooseDialog(mContext, allStr, new OnChooseListener() {
                    @Override
                    public void onPositive(int pos) {
                        ToastUtils.showToast(mContext, "choose:" + pos);
                    }
                });
                break;
            case R.id.input_view:
                DialogUtils.showInputDialog(mContext, "输入地址", "www.baidu.com", new OnInputListener() {
                    @Override
                    public void onClickPositive(String inputStr) {
                        ToastUtils.showToast(mContext, "confirm:确认");
                    }

                    @Override
                    public void onClickNegative() {
                        ToastUtils.showToast(mContext, "choose:取消");
                    }
                });
                break;
            case R.id.toast_view:
                ToastUtils.showToast(mContext, "这是一个很长很长的提示!...");
                break;

            case R.id.test1:
                showPopupWindow(1);
                break;
            case R.id.test2:
                showPopupWindow(2);
                break;
        }
        progressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtils.dismissProgressDialog();
                DialogUtils.dismissLoading();
            }
        }, 2000);
    }

    PopupWindow typePopupWindow;
    private void showPopupWindow(int pos) {
        if (typePopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_popwinow, null);
            ((TextView) view.findViewById(R.id.tvTitle)).setText("PopWindow");

            view.findViewById(R.id.tvSure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typePopupWindow.dismiss();
                }
            });
            view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typePopupWindow.dismiss();
                }
            });
            typePopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            typePopupWindow.setOutsideTouchable(true);
            typePopupWindow.setFocusable(true);
            typePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if(pos == 1){
            typePopupWindow.showAsDropDown(test1);
        }else{
            typePopupWindow.showAsDropDown(test2);
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        DialogUtils.dismissProgressDialog();
        DialogUtils.dismissDialog();
    }
}
