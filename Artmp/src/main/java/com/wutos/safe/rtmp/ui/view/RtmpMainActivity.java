package com.wutos.safe.rtmp.ui.view;

import android.os.Bundle;
import android.view.View;

import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.ToolbarUtil;
import com.wutos.safe.rtmp.R;
import com.wutos.safe.rtmp.R2;
import com.wutos.safe.rtmp.app.activity.RtmpCompatActivity;

import butterknife.OnClick;

public class RtmpMainActivity extends RtmpCompatActivity {

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
            DialogUtils.showToast(this, "全屏录制待开发");
        }else if(viewId == R.id.play_h5){
            readGo(RtmpPlayerActivity.class);
        }
    }
}
