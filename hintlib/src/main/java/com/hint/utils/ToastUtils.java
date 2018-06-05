package com.hint.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hint.R;


public class ToastUtils {
    public static void showToast(Context context, String toastStr) {
        if (context == null) {
            return;
        }
        final Context applicationContext = context.getApplicationContext();
        final Toast toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_toast, null, false);
        LinearLayout llToast = (LinearLayout) view.findViewById(R.id.toast);
        TextView lltext = (TextView) view.findViewById(R.id.toast_tv);
        lltext.setText(toastStr);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }
}
