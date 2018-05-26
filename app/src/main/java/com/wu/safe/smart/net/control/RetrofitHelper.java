package com.wu.safe.smart.net.control;


import android.text.TextUtils;

import com.base.config.GlobalConfig;
import com.base.net.control.LogInterceptor;
import com.base.net.control.ToByteConvertFactory;
import com.base.utils.ShareUtil;
import com.wu.safe.user.config.NetConfig;
import com.wu.safe.user.db.entity.UserEntity;
import com.wu.safe.user.db.helper.UserHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private RetrofitHelper() {
    }

    public static <T> T tokenCreate(final Class<T> service) {
        return new Retrofit.Builder()
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetConfig.IP_ADDRESS).build().create(service);
    }

    public static <T> T tokenCreate(final Class<T> service, String ipAddress) {
        return new Retrofit.Builder()
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ipAddress).build().create(service);
    }

    public static <T> T byteCreate(final Class<T> service) {
        return new Retrofit.Builder()
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ToByteConvertFactory.create())
                .baseUrl(NetConfig.IP_ADDRESS).build().create(service);
    }

    public static <T> T byteCreate(final Class<T> service, String ipAddress) {
        return new Retrofit.Builder()
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ToByteConvertFactory.create())
                .baseUrl(ipAddress).build().create(service);
    }

    private static OkHttpClient getClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String token = "";
                        String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME, "");
                        UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
                        if(userEntity != null){
                            token = userEntity.getUser_token();
                        }
                        if (TextUtils.isEmpty(token)) {   // �״ε�½
                            return chain.proceed(originalRequest);
                        }else{
                            Request authorised = originalRequest.newBuilder()
                                    .header(NetConfig.AUTHORIZATION, "Bearer "+ token)
                                    .header(NetConfig.CONTENT_TYPE, "application/x-www-form-urlencoded")
                                    .build();
                            return chain.proceed(authorised);
                        }
                    }
                })
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .build();
        return okHttpClient;
    }
}
