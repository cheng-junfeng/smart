package com.wu.safe.base.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.wu.safe.base.R;
import com.wu.safe.base.net.helper.ApiExceptionHelper;
import com.wu.safe.base.app.listener.OnInputClickListener;
import com.wu.safe.base.app.listener.OnPositionSelectListener;
import com.wu.safe.base.app.listener.OnSelectClickListener;

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

    public static void showProgressDialog(Context context, String msg) {
        dismissProgressDialog();
        if (null == progressdialog) {
            progressdialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_loading, null);
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

    public static void showChooseDialog(Context context, final OnPositionSelectListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_delete, null);
            TextView deleteBtn = view.findViewById(R.id.btn_delete);
            TextView deleteTypeBtn = view.findViewById(R.id.btn_delete_type);
            TextView clearAllBtn = view.findViewById(R.id.btn_clear_all);
            dialog.setContentView(view);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onPositiveSelect(0);
                }
            });
            deleteTypeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onPositiveSelect(1);
                }
            });
            clearAllBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                    listener.onPositiveSelect(2);
                }
            });
        }
        if (null != dialog && !dialog.isShowing()) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
        }
    }

    public static void showConfirmDialog(Context context, String message, final OnSelectClickListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_select, null);
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

    public static void showConfirmDialog(Context context, String message, final View.OnClickListener listener) {
        dismissDialog();
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
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

    public static void showInputDialog(final Context context, String title, String defaultStr, final OnInputClickListener listener) {
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
                        DialogUtils.showToast(context, "输入不能为空");
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

    public static void showToast(Context context, String toastStr) {
        if (context == null) {
            return;
        }
        final Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_toast, null, false);
        LinearLayout llToast = (LinearLayout) view.findViewById(R.id.toast);
        TextView lltext = (TextView) view.findViewById(R.id.toast_tv);
        lltext.setText(toastStr);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }

    public static void showThrowable(Context context, Throwable e) {
        showThrowable(context, e, false);
    }

    public static void showThrowable(Context context, Throwable e, boolean isFocus) {
        if (context == null) {
            return;
        }
        if(!isFocus){
            if(!ApiExceptionHelper.isBadNetwork(e)){
                return;
            }
        }
        String errorMsg = ApiExceptionHelper.getMessage(e);
        final Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout_toast,null,false);
        LinearLayout llToast = (LinearLayout) view.findViewById(R.id.toast);
        TextView lltext = (TextView) view.findViewById(R.id.toast_tv);
        lltext.setText(errorMsg);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }
}
