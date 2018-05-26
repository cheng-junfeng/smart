package com.wu.safe.user.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.custom.widget.CommEditText;
import com.hint.utils.ToastUtils;
import com.base.net.helper.ApiExceptionHelper;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;
import com.wu.safe.user.net.bean.MyChangeOutput;
import com.wu.safe.user.net.bean.MyTokenOutput;
import com.base.net.helper.HttpHelper;
import com.base.net.control.HttpResult;
import com.wu.safe.user.net.control.RetrofitHelper;
import com.wu.safe.user.net.service.UserService;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MyModifyPswActivity extends UserBaseCompatActivity {

    private final static String TAG = "MyModifyPswActivity";

    @BindView(R2.id.et_old_psw)
    CommEditText etOldPsw;
    @BindView(R2.id.et_psw)
    CommEditText etPsw;
    @BindView(R2.id.et_pswConfirm)
    CommEditText etPswConfirm;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_modify_psw;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "修改密码", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick(R2.id.bt_complete)
    public void onViewClicked() {
        String oldPsw = etOldPsw.getText().toString();
        String newPsw = etPsw.getText().toString();
        String confirmPsw = etPswConfirm.getText().toString();
        if (TextUtils.isEmpty(oldPsw)) {
            ToastUtils.showToast(mContext, "旧密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(newPsw)) {
            ToastUtils.showToast(mContext, "新密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(confirmPsw)) {
            ToastUtils.showToast(mContext, "请确认新密码");
            return;
        }

        if (!newPsw.equals(confirmPsw)) {
            ToastUtils.showToast(mContext, "两次密码不一致");
            return;
        }
        HashMap params = new HashMap<String, String>();
        params.put("oldPassword", oldPsw);
        params.put("newPassword", confirmPsw);
        Observable<HttpResult<MyTokenOutput>> changeObservable = RetrofitHelper.tokenCreate(UserService.class).changePassword(params);
        HttpHelper.getObservable(changeObservable, this)
                .subscribe(new Consumer<HttpResult<MyChangeOutput>>() {
                    @Override
                    public void accept(HttpResult<MyChangeOutput> value) throws Exception {
                        if (value != null && value.isSuccess()) {
                            MyChangeOutput output = value.getData();
                            if ((output != null) && (output.state == 200)) {
                                LogUtil.d(TAG, "change password success");
                                ToastUtils.showToast(mContext, "修改成功，请重新登录");
                                readGoFinish(MyLoginActivity.class);
                            } else if (output.state == 402) {
                                etOldPsw.getText().clear();
                                etOldPsw.setHint("原密码不正确");
                            }
                        } else {
                            LogUtil.e(TAG, "change pwd fail");
                            ToastUtils.showToast(mContext, "修改密码失败，请稍后再试！");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(TAG, "change password fail");
                        ToastUtils.showToast(mContext, ApiExceptionHelper.getMessage(throwable));
                    }
                });
    }
}