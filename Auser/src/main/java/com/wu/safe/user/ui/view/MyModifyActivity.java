package com.wu.safe.user.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.custom.widget.CommEditText;
import com.hint.utils.ToastUtils;
import com.base.app.event.RxBusHelper;
import com.base.config.GlobalConfig;
import com.base.net.helper.ApiExceptionHelper;
import com.base.utils.LogUtil;
import com.base.utils.ShareUtil;
import com.base.utils.ToolbarUtil;

import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;
import com.wu.safe.user.config.UserExtra;
import com.wu.safe.user.db.entity.UserEntity;
import com.wu.safe.user.db.helper.UserHelper;
import com.wu.safe.user.net.bean.MyChangeOutput;
import com.base.net.helper.HttpHelper;
import com.base.net.control.HttpResult;
import com.wu.safe.user.net.control.RetrofitHelper;
import com.wu.safe.user.net.service.UserService;
import com.wu.safe.user.ui.event.MyEvent;
import com.wu.safe.user.ui.event.MyType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MyModifyActivity extends UserBaseCompatActivity {

    private final static String TAG = "MyModifyActivity";

    @BindView(R2.id.et_modify)
    CommEditText etModify;

    private int mType = 0;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_modify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        mType = bundle.getInt(UserExtra.MY_MODIFY_TYPE, 0);
        String title = "修改用户名";
        if (mType == 0) {
            etModify.setHint("请输入用户名");
        } else {
            etModify.setHint("请输入邮箱");
            title = "修改邮箱";
        }
        ToolbarUtil.setToolbarLeft(toolbar, title, null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick(R2.id.bt_complete)
    public void onViewClicked() {
        final String modify = etModify.getText().toString();
        if (TextUtils.isEmpty(modify)) {
            ToastUtils.showToast(mContext, "输入不能为空");
            return;
        }

        if (mType == 0) {
            Observable<HttpResult<MyChangeOutput>> nameObservable = RetrofitHelper.tokenCreate(UserService.class).changeUserName(modify);
            HttpHelper.getObservable(nameObservable, this)
                    .subscribe(new Consumer<HttpResult<MyChangeOutput>>() {
                        @Override
                        public void accept(HttpResult<MyChangeOutput> value) throws Exception {
                            if (value != null && value.isSuccess()) {
                                String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
                                String email = "";
                                UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
                                if (userEntity != null) {
                                    userEntity.setUser_name(modify);
                                    email = userEntity.getUser_email();
                                    UserHelper.getInstance().update(userEntity);
                                }
                                MyEvent event = new MyEvent.Builder(MyType.UPDATE)
                                        .userName(modify)
                                        .email(email).build();
                                RxBusHelper.post(event);
                                ShareUtil.put(GlobalConfig.MY_USERNAME, modify);
                                ToastUtils.showToast(mContext, "修改用户名成功");
                                finish();
                            } else {
                                LogUtil.e(TAG, "change password null");
                                ToastUtils.showToast(mContext, "修改用户名失败，请稍后再试");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e(TAG, "change password fail");
                            ToastUtils.showToast(mContext, ApiExceptionHelper.getMessage(throwable));
                        }
                    });

        } else {
            Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
            Matcher m = p.matcher(modify);
            if (!m.matches()) {
                etModify.getText().clear();
                ToastUtils.showToast(mContext, "格式不合法");
                return;
            }

            Observable<HttpResult<Object>> emailObservable = RetrofitHelper.tokenCreate(UserService.class).changeEmail(modify);
            HttpHelper.getObservable(emailObservable, this)
                    .subscribe(new Consumer<HttpResult<Object>>() {
                        @Override
                        public void accept(HttpResult<Object> value) throws Exception {
                            if (value != null && value.isSuccess()) {
                                String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
                                UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
                                if (userEntity != null) {
                                    userEntity.setUser_email(modify);
                                    UserHelper.getInstance().update(userEntity);
                                }
                                MyEvent event = new MyEvent.Builder(MyType.UPDATE)
                                        .userName(userName)
                                        .email(modify).build();
                                RxBusHelper.post(event);

                                ToastUtils.showToast(mContext, "修改邮箱成功");
                                finish();
                            } else {
                                LogUtil.e(TAG, "change email null");
                                ToastUtils.showToast(mContext, "修改邮箱失败，请稍后再试");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtil.e(TAG, "change email fail");
                            ToastUtils.showToast(mContext, ApiExceptionHelper.getMessage(throwable));
                        }
                    });
        }
    }
}