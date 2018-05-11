package com.wu.safe.user.ui.contract;

import android.content.Context;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public interface LoginContract {

    interface View {
        void loginFail(String statusStr);

        void loginProgress();
        void loginSuccess();
        RxAppCompatActivity getRxActivity();
    }

    interface Presenter {
        boolean isLogin(Context mContext);
        void initLogin(Context mContext);

        void login(Context mContext, String userName, String pwd, View mView);
        void logout(Context mContext);
    }
}