package com.base.net.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;
import retrofit2.Response;

public class ApiExceptionHelper {
    public static final String MSG_NET_ERROR = "网络状况不佳";
    public static final String MSG_UNKNOWN = "发生未知错误";

    //{"result":null,"targetUrl":null,"success":false,
    // "error":{"code":0,"message":"对不起,在处理您的请求期间,产生了一个服务器内部错误!","details":null,"validationErrors":null},
    // "unAuthorizedRequest":false,}
    private static final String OBJ_ERROR = "error";
    private static final String OBJ_MESSAGE = "message";

    public static String getMessage(Throwable e) {
        String errorMsg;
        if (isBadNetwork(e)) {
            errorMsg = MSG_NET_ERROR;
        } else if (e instanceof HttpException) {
            errorMsg = getHttpExceptionMsg((HttpException) e);
        } else {
            errorMsg = MSG_UNKNOWN;
        }
        return errorMsg;
    }

    public static boolean isBadNetwork(Throwable e) {
        if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof TimeoutException) {
            return true;
        } else {
            return false;
        }
    }

    private static String getHttpExceptionMsg(HttpException httpException) {

        String errorMsg = null;
        Response<?> response = httpException.response();

        if (response != null && response.errorBody() != null) {
            //先处理body
            try {
                String body = response.errorBody().string();
                if (body != null) {
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.has(OBJ_ERROR)) {
                        JSONObject errorObject = jsonObject.getJSONObject(OBJ_ERROR);
                        if (errorObject.has(OBJ_MESSAGE)) {
                            errorMsg = errorObject.getString(OBJ_MESSAGE);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            errorMsg = MSG_UNKNOWN;
        }

        if (errorMsg == null) {
            errorMsg = httpException.message();
        }
        return errorMsg;
    }
}
