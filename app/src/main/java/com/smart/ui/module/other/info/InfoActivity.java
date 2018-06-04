package com.smart.ui.module.other.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.base.utils.ToolbarUtil;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.utils.PhoneUtils;

import butterknife.BindView;


public class InfoActivity extends BaseCompatActivity {
    private final static String TAG = "InfoActivity";

    @BindView(R.id.ll_info)
    TextView llInfo;


    @Override
    protected int setContentView() {
        return R.layout.activity_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "信息", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        PhoneUtils siminfo = new PhoneUtils(this);
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(siminfo.getNativePhoneNumber()+"\n");
        strBuf.append(siminfo.getPhoneInfo()+"\n");
        strBuf.append(siminfo.getProvidersName()+"\n");
        llInfo.setText(strBuf.toString());
    }
}
