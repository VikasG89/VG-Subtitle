#include "jni_common.h"
#include "FFmpegRuntime.h"
#include <stdexcept>

using namespace vg::subtitle::ffmpeg;

extern "C" JNIEXPORT jint JNICALL
Java_com_vg_subtitle_native_bridge_NativeAudioExtractor_extractToWav(
        JNIEnv * env,
        jobject,
        jstring video_path,
        jstring output_path,
        jobject callback) {
    try {
        std::string input = jstring_to_string(env, video_path);
        std::string output = jstring_to_string(env, output_path);

        jclass callback_cls = nullptr;
        jmethodID progress_mid = nullptr;
        if (callback) {
            callback_cls = env->GetObjectClass(callback);
            progress_mid = env->GetMethodID(callback_cls, "onProgress", "(I)V");
        }

        auto progress_func = [&](int percent) {
            if (callback && progress_mid) {
                env->CallVoidMethod(callback, progress_mid, percent);
            }
        };

        bool success = FFmpegRuntime::extractAudio(input, output, progress_func);
        return success ? 0 : -1;

    } catch (const std::exception & e) {
        throw_java(env, e.what(), 1);
        return -1;
    }
}
