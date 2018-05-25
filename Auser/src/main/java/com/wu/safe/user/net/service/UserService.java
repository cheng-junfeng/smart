package com.wu.safe.user.net.service;


import com.wu.safe.user.net.bean.MyChangeOutput;
import com.wu.safe.user.net.bean.MyInfoOutput;
import com.wu.safe.user.net.bean.MyLoginOutput;
import com.smart.base.net.control.HttpResult;
import com.wu.safe.user.net.bean.MyTokenOutput;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    // 登陆
    @FormUrlEncoded
    @POST("/api/TokenAuth/AppLogin")
    Observable<HttpResult<MyLoginOutput>> login(@Field("UsernameOrEmailAddress") String userName, @Field("Password") String pwd, @Field("retureUrl") String url);

    // 验证token
    @POST("/api/TokenAuth/TokenAuthVerification")
    Observable<HttpResult<MyTokenOutput>> verifyToken();

    // 获取登陆用户头像
    @GET("/Profile/GetProfilePicture")
    Observable<byte[]> getProfilePic();

    // 获取用户信息
    @GET("/User/GetUserInfo")
    Observable<HttpResult<MyInfoOutput>> getUserInfo();

    // 更改密码
    @FormUrlEncoded
    @POST("/User/AppResetPassword")
    Observable<HttpResult<MyChangeOutput>> changePassword(@FieldMap Map<String, String> params);

    // 更改名称，普通的post请求，一个参数
    @FormUrlEncoded
    @POST("/User/AppResetName")
    Observable<HttpResult<MyChangeOutput>> changeUserName(@Field("newName") String newName);

    // 更改邮箱
    @FormUrlEncoded
    @POST("/User/AppResetEamil")
    Observable<HttpResult<Object>> changeEmail(@Field("newEmail") String newEmail);
}
