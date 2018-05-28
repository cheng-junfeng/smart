package com.user.net.service;


import com.user.net.bean.MyChangeOutput;
import com.user.net.bean.MyInfoOutput;
import com.user.net.bean.MyLoginOutput;
import com.base.net.control.HttpResult;
import com.user.net.bean.MyTokenOutput;

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
    @POST("/user/login")
    Observable<HttpResult<MyLoginOutput>> login(@Field("Username") String userName, @Field("Password") String pwd, @Field("retureUrl") String url);

    // 验证token
    @POST("/user/token")
    Observable<HttpResult<MyTokenOutput>> verifyToken();

    // 获取登陆用户头像
    @GET("/user/getPicture")
    Observable<byte[]> getProfilePic();

    // 获取用户信息
    @GET("/user/getInfo")
    Observable<HttpResult<MyInfoOutput>> getUserInfo();

    // 更改密码
    @FormUrlEncoded
    @POST("/user/resetPassword")
    Observable<HttpResult<MyChangeOutput>> changePassword(@FieldMap Map<String, String> params);

    // 更改名称，普通的post请求，一个参数
    @FormUrlEncoded
    @POST("/user/resetName")
    Observable<HttpResult<MyChangeOutput>> changeUserName(@Field("newName") String newName);

    // 更改邮箱
    @FormUrlEncoded
    @POST("/user/resetEamil")
    Observable<HttpResult<Object>> changeEmail(@Field("newEmail") String newEmail);
}
