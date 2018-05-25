package com.hintlib.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.hintlib.R;
import com.hintlib.adapter.BaseListAdapter;
import com.hintlib.listener.OnChooseListener;
import com.hintlib.listener.OnConfirmListener;
import com.hintlib.listener.OnInputListener;

import java.util.List;

public class DialogUtils {

    private static Dialog progressdialog = null;

    public static void showProgressDialog(Context context) {
        dismissProgressDialog();
        if (null == progressdialog) {
            progressdialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
            progressdialog.setContentView(view);
        }
        if (null != progressdialog && !progressdialog.isShowing()) {
            progressdialog.show();
            Window window = progressdialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void showProgressMsgDialog(Context context, String msg) {
        dismissProgressDialog();
        if (null == progressdialog) {
            progressdialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_msg, null);
            TextView tipsTv = view.findViewById(R.id.tv_tips);
            progressdialog.setContentView(view);
            tipsTv.setText(msg);
        }
        if (null != progressdialog && !progressdialog.isShowing()) {
            progressdialog.show();
            Window window = progressdialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void dismissProgressDialog() {
        if (null != progressdialog) {
            if (progressdialog.isShowing()) {
                progressdialog.cancel();
                progressdialog.dismiss();
            }
            progressdialog = null;
        }
    }

    private static Dialog dialog = null;
    public static void showChooseDialog(Context context, List<String> allStr, final OnChooseListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose, null);
            RecyclerView mainView = view.findViewById(R.id.main_view);
            BaseListAdapter mAdapter = new BaseListAdapter(allStr);
            mAdapter.setOnListener(listener);

            mainView.setLayoutManager(new LinearLayoutManager(context));
            mainView.setHasFixedSize(true);
            mainView.setAdapter(mAdapter);
            dialog.setContentView(view);
        }
        if (null != dialog && !dialog.isShowing()) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void showConfirmDialog(Context context, String message, final OnConfirmListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
            Button confirmBtn = view.findViewById(R.id.btn_confirm);
            Button cancelBtn = view.findViewById(R.id.btn_cancel);
            TextView messageTv = view.findViewById(R.id.tv_message);
            messageTv.setText(message);
            dialog.setContentView(view);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onClickPositive();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onClickNegative();
                }
            });
        }
        if (null != dialog && !dialog.isShowing()) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void showConfirmAlertDialog(Context context, String message, final View.OnClickListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_alert, null);
            Button confirmBtn = view.findViewById(R.id.btn_confirm);
            TextView messageTv = view.findViewById(R.id.tv_message);
            messageTv.setText(message);
            dialog.setContentView(view);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onClick(view);
                }
            });
        }
        if (null != dialog && !dialog.isShowing()) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void showInputDialog(final Context context, String title, String defaultStr, final OnInputListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
            Button confirmBtn = view.findViewById(R.id.btn_confirm);
            Button cancelBtn = view.findViewById(R.id.btn_cancel);
            TextView titleTv = view.findViewById(R.id.tv_title);
            titleTv.setText(title);
            final EditText inputTv = (EditText)view.findViewById(R.id.tv_message);
            inputTv.setText(defaultStr);
            dialog.setContentView(view);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String inputStr = inputTv.getText().toString();
                    if(TextUtils.isEmpty(inputStr)){
                        ToastUtils.showToast(context, "输入不能为空");
                        return;
                    }
                    dismissDialog();
                    listener.onClickPositive(inputStr);
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onClickNegative();
                }
            });
        }
        if (null != dialog && !dialog.isShowing()) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void dismissDialog() {
        if (null != dialog) {
            if (dialog.isShowing()) {
                dialog.cancel();
                dialog.dismiss();
            }
            dialog = null;
        }
    }
}