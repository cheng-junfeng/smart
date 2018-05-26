package com.base.net.control;

import com.base.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogInterceptor implements Interceptor {
    private final static String TAG = "LogInterceptor";

    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        double time = (t2 - t1) / 1e6d;

        ResponseBody body = response.body();
        MediaType contentType;
        if (body != null) {
            contentType = body.contentType();
            if (contentType.toString().equals("application/json; charset=utf-8")) {
                // 当response为json格式时，解析为String并打印log信息
                String bodyString = body.string();
                // 在调用了response.body()方法之后，response中的流会被关闭，我们需要创建出一个新的response
                ResponseBody newBody = ResponseBody.create(contentType, bodyString);

                if (request.method().equals("GET")) {
                    LogUtil.d(TAG, String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                } else if (request.method().equals("POST")) {
                    LogUtil.d(TAG, String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                } else if (request.method().equals("PATCH")) {
                    LogUtil.d(TAG, String.format("PATCH " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                } else if (request.method().equals("PUT")) {
                    LogUtil.d(TAG, String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
                } else if (request.method().equals("DELETE")) {
                    LogUtil.d(TAG, String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), response.code(), response.headers()));
                }

                return response.newBuilder().body(newBody).build();
            } else if (contentType.toString().equals("image/jpeg")) {
                // 当response为jpg图片格式时，解析为byte数组
                byte[] bodyBytes = body.bytes();
                ResponseBody newBody = ResponseBody.create(contentType, bodyBytes);
                return response.newBuilder().body(newBody).build();
            }
        }

        return response;
    }

    private static String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private String stringifyResponseBody(String responseBody) {
        return responseBody;
    }
}
