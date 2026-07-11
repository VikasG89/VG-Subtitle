#include "jni_common.h"
#include <stdexcept>

using namespace vg::subtitle;

extern "C" JNIEXPORT jlong JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_initialize(
        JNIEnv * env,
        jobject,
        jstring model_path) {
    try {
        std::string path = jstring_to_string(env, model_path);
        LOGI("Initializing EngineRuntime with model: %s", path.c_str());

        auto runtime = std::make_unique<EngineRuntime>();
        if (!runtime->initialize(path)) {
            throw std::runtime_error("Failed to initialize EngineRuntime with model: " + path);
        }

        LOGI("EngineRuntime initialized successfully.");
        return reinterpret_cast<jlong>(runtime.release());

    } catch (const std::exception & e) {
        LOGE("Error in nativeInitialize: %s", e.what());
        throw_java(env, e.what(), 1);
        return 0;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_verifyModels(
        JNIEnv * env,
        jobject,
        jstring model_path) {
    std::string path = jstring_to_string(env, model_path);
    EngineRuntime runtime;
    return runtime.verifyModel(path) ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT void JNICALL
Java_com_vg_subtitle_native_bridge_NativeBridge_release(
        JNIEnv * env,
        jobject,
        jlong context_ptr) {
    auto runtime = reinterpret_cast<EngineRuntime *>(context_ptr);
    if (runtime) {
        LOGI("Releasing EngineRuntime.");
        delete runtime;
    }
}
