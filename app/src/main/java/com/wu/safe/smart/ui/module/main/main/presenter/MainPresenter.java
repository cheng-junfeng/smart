package com.wu.safe.smart.ui.module.main.main.presenter;


import com.blankj.utilcode.util.LogUtils;
import com.smart.base.config.GlobalConfig;
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.ShareUtil;

import com.wu.safe.user.db.entity.UserEntity;
import com.wu.safe.user.db.helper.UserHelper;
import com.wu.safe.user.net.bean.MyInfoOutput;
import com.smart.base.net.helper.HttpHelper;
import com.smart.base.net.control.HttpResult;
import com.wu.safe.smart.net.control.RetrofitHelper;
import com.wu.safe.user.net.service.UserService;
import com.wu.safe.smart.ui.module.main.main.contract.MainContract;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MainPresenter implements MainContract.Presenter {
    private final static String TAG = "MainPresenter";
    MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        this.mView = view;
    }

    @Override
    public void getUserImg() {
        Observable<byte[]> imgObservable = RetrofitHelper.byteCreate(UserService.class).getProfilePic();
        HttpHelper.getObservable(imgObservable, mView.getRxActivity())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] value) throws Exception {
                        if (value != null) {
                            LogUtil.i(TAG, "getProfilePic success = value");
                            String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
                            UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
                            if (userEntity != null) {
                                userEntity.setUser_img(value);
                                UserHelper.getInstance().update(userEntity);
                            }
                        } else {
                            LogUtil.i(TAG, "getProfilePic null");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "getProfilePic fail:" + throwable.getMessage());
                    }
                });
    }

    @Override
    public void getUserInfo() {
        Observable<HttpResult<MyInfoOutput>> imgObservable = RetrofitHelper.tokenCreate(UserService.class).getUserInfo();
        HttpHelper.getObservable(imgObservable, mView.getRxActivity())
                .subscribe(new Consumer<HttpResult<MyInfoOutput>>() {
                    @Override
                    public void accept(HttpResult<MyInfoOutput> value) throws Exception {
                        if (value != null && value.isSuccess()) {
                            LogUtil.i(TAG, "getUserInfo success = value");
                            MyInfoOutput output = value.getData();
                            if (output != null) {
                                String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME);
                                UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
                                if (userEntity != null) {
                                    userEntity.setUser_lasttime(output.lastLoginTime);
                                    userEntity.setUser_email(output.emailAddress);
                                    UserHelper.getInstance().update(userEntity);
                                }
                            } else {
                                LogUtil.i(TAG, "getUserInfo output null");
                            }
                        } else {
                            LogUtil.i(TAG, "getUserInfo null");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "getUserInfo fail:" + throwable.getMessage());
                    }
                });
    }
}
