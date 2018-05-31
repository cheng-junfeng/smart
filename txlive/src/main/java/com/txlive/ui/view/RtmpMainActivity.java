package com.txlive.ui.view;

import android.os.Bundle;
import android.view.View;

import com.hint.utils.ToastUtils;
import com.base.utils.ToolbarUtil;
import com.txlive.R;
import com.txlive.R2;
import com.txlive.app.TxCompatActivity;

import butterknife.OnClick;

public class RtmpMainActivity extends TxCompatActivity {

    private final static String TAG = "RtmpMainActivity";

    @Override
    protected int setContentView() {
        return R.layout.activity_rtmp_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "RTMP", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R2.id.push_view, R2.id.play_view, R2.id.global_screen, R2.id.play_h5})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.push_view){
            readGo(RtmpPublisherActivity.class);
        }else if(viewId == R.id.play_view){
            readGo(LivePlayerActivity.class);
        }else if(viewId == R.id.global_screen){
            ToastUtils.showToast(this, "全屏录制待开发");
        }else if(viewId == R.id.play_h5){
            readGo(RtmpPlayerActivity.class);
        }
    }
}
