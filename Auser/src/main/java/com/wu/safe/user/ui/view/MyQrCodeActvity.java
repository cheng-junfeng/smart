package com.wu.safe.user.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadTask;
import com.wu.safe.base.config.GlobalConfig;
import com.wu.safe.base.ui.widget.CommEditText;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.ShareUtil;
import com.wu.safe.base.utils.TextUtil;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseActivity;
import com.wu.safe.zxinglib.utils.QRCodeUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class MyQrCodeActvity extends UserBaseActivity {


    @BindView(R2.id.dialog_content)
    CommEditText dialogContent;
    @BindView(R2.id.qrcode_img)
    ImageView qrcodeImg;

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_my_qrcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME, "");
        dialogContent.setText(userName);
        if(TextUtil.isEmpty(userName)){
            DialogUtils.showToast(this, "输入不能为空");
        }else{
            qrcodeImg.setImageBitmap(QRCodeUtil.createQRCodeBitmap(userName, 120));
        }
    }

    @OnClick({R2.id.cancel, R2.id.start})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.cancel) {
            finish();
        } else if (viewId == R.id.start) {
            String input = dialogContent.getText().toString();
            if(TextUtil.isEmpty(input)){
                DialogUtils.showToast(this, "输入不能为空");
            }else{
                qrcodeImg.setImageBitmap(QRCodeUtil.createQRCodeBitmap(input, 100));
            }
        }
    }
}
