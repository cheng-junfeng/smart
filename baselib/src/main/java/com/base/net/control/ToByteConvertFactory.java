package com.base.net.control;

import com.base.utils.LogUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class ToByteConvertFactory extends Converter.Factory {
    private static final String TAG = "ToByteConvertFactory";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/octet-stream");

    public static ToByteConvertFactory create() {
        return new ToByteConvertFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        LogUtil.d(TAG, "convert: Converter<?, RequestBody> " + type + " " + byte[].class);
        if ("byte[]".equals(type + "")) {
            return new Converter<ResponseBody, byte[]>() {
                @Override
                public byte[] convert(ResponseBody value) throws IOException {
                    LogUtil.d(TAG, "convert: Converter<ResponseBody, ?>");
                    return value.bytes();
                }
            };
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        LogUtil.d(TAG, "convert: Converter<?, RequestBody>");
        if ("byte[]".equals(type + "")) {
            return new Converter<byte[], RequestBody>() {
                @Override
                public RequestBody convert(byte[] value) throws IOException {
                    LogUtil.d(TAG, "convert: Converter<?, RequestBody>");
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}
