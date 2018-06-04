package com.smart.net.service;



import com.smart.net.bean.AreaInput;
import com.smart.net.bean.AreaOutput;
import com.base.net.control.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AreaService {

    @POST("/getArea")
    Observable<HttpResult<AreaOutput>> queryArea(@Body AreaInput input);
}
