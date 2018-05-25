package com.wu.safe.user.ui.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.ToolbarUtil;
import com.smart.base.ui.widget.MultiEditInputView;

import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class MyFeedBackActivity extends UserBaseCompatActivity {

    private final static String TAG = "MyFeedBackActivity";

    @BindView(R2.id.mev_view)
    MultiEditInputView mevView;
    @BindView(R2.id.tv_comm_footer)
    TextView tvCommFooter;

    private String phone = "18802734547";
    private String contentText;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_feed_back;
    }

    @Override
    protected int setFootView() {
        return R.layout.base_bar_footer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "意见反馈", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    public void initView() {
        tvCommFooter.setText("提交反馈");
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int headerHeight = BarUtils.getActionBarHeight(MyFeedBackActivity.this) + BarUtils.getStatusBarHeight(MyFeedBackActivity.this);
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > headerHeight) {
                    LogUtil.d(TAG, "keyboard is up");
                } else {
                    LogUtil.d(TAG, "keyboard is hidden");
                }
            }
        });
    }

    private void toSendSms() {
        contentText = mevView.getContentText();
        if (TextUtils.isEmpty(contentText)) {
            Toast.makeText(MyFeedBackActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent smsIntent = IntentUtils.getSendSmsIntent(phone, contentText);
        startActivity(smsIntent);
    }

    private void openDial() {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

    @OnClick({R2.id.tv_call_me, R2.id.tv_comm_footer})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.tv_call_me){
            openDial();
        }else if(viewId == R.id.tv_comm_footer){
            toSendSms();
        }
    }
}
