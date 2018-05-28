package com.user.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.custom.widget.CommEditText;
import com.hint.utils.ToastUtils;
import com.base.app.event.RxBusHelper;
import com.base.utils.ToolbarUtil;
import com.user.R;
import com.user.R2;
import com.user.app.acitvity.UserBaseCompatActivity;
import com.user.ui.event.CountryEvent;
import com.user.utils.PhoneCheckUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MyRegistActivity extends UserBaseCompatActivity {

    @BindView(R2.id.tv_country)
    TextView tvCountry;
    @BindView(R2.id.tv_country_code)
    TextView tvCountryCode;
    @BindView(R2.id.et_phone)
    CommEditText etPhone;
    @BindView(R2.id.bt_sendCode)
    Button btSendCode;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        ToolbarUtil.setToolbarLeft(toolbar, "注册", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        RxBusHelper.doOnMainThread(this, CountryEvent.class, new RxBusHelper.OnEventListener<CountryEvent>() {
            @Override
            public void onEvent(CountryEvent myEvent) {
                tvCountry.setText(myEvent.getName());
                tvCountryCode.setText(myEvent.getCode());
            }
        });

        //设置发送按钮是否可用
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    btSendCode.setEnabled(true);
                } else {
                    btSendCode.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R2.id.rl_country, R2.id.bt_sendCode, R2.id.bt_next})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.rl_country) {
            readGo(MySelectCountryActivity.class);
        } else if (viewId == R.id.bt_sendCode) {
            String phone = etPhone.getText().toString().trim();
            if (!PhoneCheckUtils.checkPhone(phone)) {
                ToastUtils.showToast(mContext, "手机号不合法");
            } else {
                //send code
                ToastUtils.showToast(mContext, "短信已发送，请注意查收");
            }
        } else if (viewId == R.id.bt_next) {
            //check code
            ToastUtils.showToast(mContext, "验证码不正确");
        }
    }
}