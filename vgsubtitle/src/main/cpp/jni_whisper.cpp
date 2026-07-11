#include "jni_common.h"
#include "WhisperRuntime.h"
#include <stdexcept>

using namespace vg::subtitle::whisper;

extern "C" JNIEXPORT jlong JNICALL
Java_com_vg_subtitle_native_bridge_NativeWhisperBridge_initializeWhisper(
        JNIEnv * env,
        jobject,
        jstring model_path) {
    try {
        std::string path = jstring_to_string(env, model_path);
        auto runtime = std::make_unique<WhisperRuntime>();
        if (!runtime->initialize(path)) {
            throw std::runtime_error("Failed to initialize WhisperRuntime");
        }
        return reinterpret_cast<jlong>(runtime.release());
    } catch (const std::exception & e) {
        throw_java(env, e.what(), 1);
        return 0;
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeWhisperBridge_releaseWhisper(
        JNIEnv * env,
        jobject,
        jlong runtime_ptr) {
    auto runtime = reinterpret_cast<WhisperRuntime *>(runtime_ptr);
    if (runtime) {
        delete runtime;
    }
}
