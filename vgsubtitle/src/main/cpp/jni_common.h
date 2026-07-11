#ifndef JNI_COMMON_H
#define JNI_COMMON_H

#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <mutex>
#include "whisper.h"

#define TAG "VGSubtitleNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#include "EngineRuntime.h"

// Global mutex is now inside RuntimeContext, but we might still need one for management
extern std::mutex g_mgmt_mutex;

// Utility functions
std::string jstring_to_string(JNIEnv *env, jstring jstr);
void throw_java(JNIEnv *env, const char *message, int error_code = 0);

#endif // JNI_COMMON_H
