LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := JniCount
LOCAL_SRC_FILES := jniCount.cpp
include $(BUILD_SHARED_LIBRARY)