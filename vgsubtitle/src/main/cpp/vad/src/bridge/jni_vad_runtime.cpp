#include "jni_common.h"
#include "VadRuntime.h"
#include "VadConfig.h"

using namespace vg::subtitle::vad;

// Declared in jni_vad_config.cpp
VadConfig java_to_native_vad_config(JNIEnv* env, jobject config_obj);

extern "C" JNIEXPORT jlong JNICALL
Java_com_vg_subtitle_native_bridge_NativeVadBridge_initializeVad(
        JNIEnv * env,
        jobject,
        jobject config_obj) {
    try {
        VadConfig config = java_to_native_vad_config(env, config_obj);
        auto runtime = VadRuntime::create(config);
        auto session = runtime->createSession();
        return reinterpret_cast<jlong>(session.release());
    } catch (const std::exception & e) {
        throw_java(env, e.what(), 1);
        return 0;
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeVadBridge_releaseVad(
        JNIEnv * env,
        jobject,
        jlong session_handle) {
    auto session = reinterpret_cast<VadSession *>(session_handle);
    if (session) {
        delete session;
    }
}
