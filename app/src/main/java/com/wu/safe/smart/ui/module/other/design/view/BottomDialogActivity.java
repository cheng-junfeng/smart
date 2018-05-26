package com.wu.safe.smart.ui.module.other.design.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.hint.utils.ToastUtils;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.ui.widget.CustomSelectDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


public class BottomDialogActivity extends BaseCompatActivity {
    private final static String TAG = "BottomDialogActivity";

    private Context mContext;


    @Override
    protected int setContentView() {
        return R.layout.activity_bottom_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "底部框", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick(R.id.show_view)
    public void onViewClicked() {
        showDialogAnim();
    }

    private void showDialogAnim() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        showDialog(new CustomSelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:                 //拍照
                        ToastUtils.showToast(mContext, "拍照");
                        break;
                    case 1:                 //相册
                        ToastUtils.showToast(mContext, "相册");
                        break;
                }
            }
        }, names);
    }

    private CustomSelectDialog showDialog(CustomSelectDialog.SelectDialogListener listener, List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }
}
