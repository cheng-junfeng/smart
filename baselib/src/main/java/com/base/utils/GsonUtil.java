package com.base.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okio.Buffer;
import okio.ByteString;


public class GsonUtil {
    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    /**
     * 解析json
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析json数组
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public  static <T> String toJsonUtf_8(T t){
        try {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"));
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            TypeToken<T> typeToken = (TypeToken<T>) TypeToken.get(t.getClass());
            TypeAdapter<T> adapter = gson.getAdapter(typeToken);
            adapter.write(jsonWriter, t);
            jsonWriter.close();

            ByteString byteString = buffer.readByteString();
            return byteString.string(Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
