package com.wu.safe.smart.ui.module.other.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.wu.safe.smart.utils.PhoneUtils;

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
        ToolbarUtil.setToolbarLeft(toolbar, "Info", null, new View.OnClickListener() {
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
