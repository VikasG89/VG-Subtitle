#include "jni_common.h"
#include "FFmpegRuntime.h"

using namespace vg::subtitle::ffmpeg;

extern "C" JNIEXPORT jlong JNICALL
Java_com_vg_subtitle_native_bridge_NativeMediaProbe_getDurationMs(
        JNIEnv * env,
        jobject,
        jstring path) {
    std::string input = jstring_to_string(env, path);
    return (jlong)FFmpegRuntime::getDurationMs(input);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_vg_subtitle_native_bridge_NativeMediaProbe_hasAudio(
        JNIEnv * env,
        jobject,
        jstring path) {
    std::string input = jstring_to_string(env, path);
    return FFmpegRuntime::hasAudio(input) ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_vg_subtitle_native_bridge_NativeMediaProbe_getFormat(
        JNIEnv * env,
        jobject,
        jstring path) {
    // Stub implementation
    return env->NewStringUTF("mp4");
}
