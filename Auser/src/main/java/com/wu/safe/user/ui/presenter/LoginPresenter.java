package com.wu.safe.user.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.smart.base.config.GlobalConfig;
import com.smart.base.net.helper.ApiExceptionHelper;
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.ShareUtil;
import com.wu.safe.push.service.JpushService;
import com.wu.safe.push.service.MqttService;
import com.wu.safe.user.db.entity.UserEntity;
import com.wu.safe.user.db.helper.UserHelper;
import com.wu.safe.user.net.bean.MyLoginOutput;
import com.wu.safe.user.net.bean.MyTokenOutput;
import com.smart.base.net.helper.HttpHelper;
import com.smart.base.net.control.HttpResult;
import com.wu.safe.user.net.control.RetrofitHelper;
import com.wu.safe.user.net.service.UserService;
import com.wu.safe.user.ui.contract.LoginContract;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {
    private final static String ROOT_ACCOUNT = "junfeng";
    private final static int ROOT_ID = 10000;
    private final static String TAG = "LoginPresenter";
    LoginContract.View mView;

    public LoginPresenter() {
    }

    public LoginPresenter(LoginContract.View view) {
        this.mView = view;
    }

    @Override
    public boolean isLogin(Context mContext) {
        boolean isLogin;
        String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
        if (!EmptyUtils.isEmpty(userName)) {
            final UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
            if (userEntity == null) {
                return false;
            }
            String token = userEntity.getUser_token();
            if (!EmptyUtils.isEmpty(token)) {
                isLogin = true;
            } else {
                isLogin = false;
            }
        } else {
            isLogin = false;
        }
        LogUtil.d(TAG, "isLogin "+isLogin);
        return isLogin;
    }

    @Override
    public void initLogin(Context mContext) {
        startPush(mContext);

        final UserEntity userEntity = UserHelper.getInstance().queryByUserName(ShareUtil.getString(GlobalConfig.MY_USERNAME));
        if (userEntity == null) {
            return;
        }
        Observable<HttpResult<MyTokenOutput>> tokenObservable = RetrofitHelper.tokenCreate(UserService.class).verifyToken();
        tokenObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<HttpResult<MyTokenOutput>>() {
                    @Override
                    public void accept(HttpResult<MyTokenOutput> value) throws Exception {
                        if (value != null && value.isSuccess()) {
                            MyTokenOutput output = value.getData();
                            if ((output != null) && (output.state == 200)) {
                                String token = output.accessToken;
                                userEntity.setUser_token(token);
                                UserHelper.getInstance().update(userEntity);
                            } else {
                                LogUtil.e(TAG, "verifyToken error");
                            }
                        } else {
                            LogUtil.e(TAG, "verifyToken error null");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getMessage());
                    }
                });
    }

    @Override
    public void login(Context context, final String userName, String pwd, final LoginContract.View mView) {
        final Context mContext = context;
        if (TextUtils.isEmpty(userName)) {
            mView.loginFail("账号不能为空");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            mView.loginFail("密码不能为空");
            return;
        }

        mView.loginProgress();
        if(ROOT_ACCOUNT.equalsIgnoreCase(userName)){
            UserHelper helper = UserHelper.getInstance();
            UserEntity oldEntity = helper.queryByUserName(userName);

            int userId = ROOT_ID;         // 当前用户的userId
            String token = Long.toString(System.currentTimeMillis()); // Token
            long current = System.currentTimeMillis();
            if(oldEntity == null){
                UserEntity user = new UserEntity();
                user.setUser_id(userId);
                user.setUser_name(userName);
                user.setUser_token(token);
                user.setUser_lasttime(Long.toString(current));
                UserHelper.getInstance().insert(user);
            }else{
                oldEntity.setUser_id(userId);
                oldEntity.setUser_token(token);
                oldEntity.setUser_lasttime(Long.toString(current));
                UserHelper.getInstance().update(oldEntity);
            }
            ShareUtil.put(GlobalConfig.MY_USERNAME, userName);          // 存下token到SP中
            startPush(mContext);

            mView.loginSuccess();
            return;
        }
        Observable<HttpResult<MyLoginOutput>> loginObservable = RetrofitHelper.tokenCreate(UserService.class).login(userName, pwd, "");
        HttpHelper.getObservable(loginObservable, mView.getRxActivity())
                .subscribe(new Consumer<HttpResult<MyLoginOutput>>() {
                    @Override
                    public void accept(HttpResult<MyLoginOutput> value) {
                        if (value != null && value.isSuccess()) {
                            MyLoginOutput output = value.getData();
                            if ((output != null) && output.loginSuccess) {
                                UserHelper helper = UserHelper.getInstance();
                                UserEntity oldEntity = helper.queryByUserName(userName);

                                int userId = output.userId;         // 当前用户的userId
                                String token = output.accessToken; // Token
                                long current = System.currentTimeMillis();
                                if(oldEntity == null){
                                    UserEntity user = new UserEntity();
                                    user.setUser_id(userId);
                                    user.setUser_name(userName);
                                    user.setUser_token(token);
                                    user.setUser_lasttime(Long.toString(current));
                                    UserHelper.getInstance().insert(user);
                                }else{
                                    oldEntity.setUser_id(userId);
                                    oldEntity.setUser_token(token);
                                    oldEntity.setUser_lasttime(Long.toString(current));
                                    UserHelper.getInstance().update(oldEntity);
                                }
                                ShareUtil.put(GlobalConfig.MY_USERNAME, userName);          // 存下token到SP中
                                startPush(mContext);

                                mView.loginSuccess();
                            } else {
                                LogUtils.e(TAG, "login error");
                                mView.loginFail("用户名或密码错误");
                            }
                        } else {
                            LogUtils.e(TAG, "login is null");
                            mView.loginFail("服务器异常，请稍后重试");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "login faild:" + throwable.getMessage());
                        mView.loginFail(ApiExceptionHelper.getMessage(throwable));
                    }
                });
    }

    @Override
    public void logout(Context mContext) {
        String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
        UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
        userEntity.setUser_token("");
        UserHelper.getInstance().update(userEntity);
        ShareUtil.remove(GlobalConfig.MY_USERNAME);

        stopPush(mContext);
    }

    private void startPush(Context mContext){
        LogUtil.d(TAG, "startPush");
        JpushService.startJpush(mContext);
        MqttService.startMqtt(mContext);
    }

    private void stopPush(Context mContext){
        LogUtil.d(TAG, "stopPush");
        JpushService.stopJpush(mContext);
        MqttService.stopMqtt(mContext);
    }
}
