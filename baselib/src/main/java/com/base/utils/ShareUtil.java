package com.base.utils;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;

import java.util.Collections;
import java.util.Set;

public class ShareUtil {
    private final static String SP_NAME = "base";
    /**
     * SP中写入String
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@NonNull String key, @NonNull String value) {
        SPUtils.getInstance(SP_NAME).put(key, value);
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code ""}
     */
    public static String getString(@NonNull String key) {
        return getString(key, "");
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static String getString(@NonNull String key, @NonNull String defaultValue) {
        return SPUtils.getInstance(SP_NAME).getString(key, defaultValue);
    }

    /**
     * SP中写入int
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@NonNull String key, int value) {
        SPUtils.getInstance(SP_NAME).put(key, value);
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static int getInt(@NonNull String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static int getInt(@NonNull String key, int defaultValue) {
        return SPUtils.getInstance(SP_NAME).getInt(key, defaultValue);
    }

    /**
     * SP中写入long
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@NonNull String key, long value) {
        SPUtils.getInstance(SP_NAME).put(key, value);
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static long getLong(@NonNull String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static long getLong(@NonNull String key, long defaultValue) {
        return SPUtils.getInstance(SP_NAME).getLong(key, defaultValue);
    }

    /**
     * SP中写入float
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@NonNull String key, float value) {
        SPUtils.getInstance(SP_NAME).put(key, value);
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public static float getFloat(@NonNull String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static float getFloat(@NonNull String key, float defaultValue) {
        return SPUtils.getInstance(SP_NAME).getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@NonNull String key, boolean value) {
        SPUtils.getInstance(SP_NAME).put(key, value);
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public static boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return SPUtils.getInstance(SP_NAME).getBoolean(key, defaultValue);
    }

    /**
     * SP中写入String集合
     *
     * @param key    键
     * @param values 值
     */
    public static void put(@NonNull String key, @NonNull Set<String> values) {
        SPUtils.getInstance(SP_NAME).put(key, values);
    }

    /**
     * SP中读取StringSet
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code Collections.<String>emptySet()}
     */
    public static Set<String> getStringSet(@NonNull String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * SP中读取StringSet
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static Set<String> getStringSet(@NonNull String key, @NonNull Set<String> defaultValue) {
        return SPUtils.getInstance(SP_NAME).getStringSet(key, defaultValue);
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean contains(@NonNull String key) {
        return SPUtils.getInstance(SP_NAME).contains(key);
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public static void remove(@NonNull String key) {
        SPUtils.getInstance(SP_NAME).remove(key);
    }

    /**
     * SP中清除所有数据
     */
    public static void clear() {
        SPUtils.getInstance(SP_NAME).clear();
    }

    private static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}