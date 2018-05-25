package com.wu.safe.smart.net.service;



import com.wu.safe.smart.net.bean.AreaInput;
import com.wu.safe.smart.net.bean.AreaOutput;
import com.smart.base.net.control.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AreaService {

    @POST("/getArea")
    Observable<HttpResult<AreaOutput>> queryArea(@Body AreaInput input);
}
